package cn.honry.statistics.sys.medicalFeeDetail.service.Impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.statistics.sys.medicalFeeDetail.action.MedicalFeeDetailAction;
import cn.honry.statistics.sys.medicalFeeDetail.dao.MedicalFeeDetailDAO;
import cn.honry.statistics.sys.medicalFeeDetail.service.MedicalFeeDetailService;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeDetailsVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeNameVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.InpatientFeeVo;
import cn.honry.utils.DateUtils;

@Service("medicalFeeDetailService")
@Transactional
public class MedicalFeeDetailServiceImpl implements MedicalFeeDetailService{

	private Logger logger = Logger.getLogger(MedicalFeeDetailServiceImpl.class);
	@Autowired
	@Qualifier(value = "medicalFeeDetailDAO")
	private MedicalFeeDetailDAO medicalFeeDetailDAO;

	@Resource(name = "client")
	private Client client;

	@Value("${es.inpatient_list.index}")
	private String inpatient_list_index;

	@Value("${es.inpatient_list.type}")
	private String inpatient_list_type;
	@Override
	public InpatientInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientInfo arg0) {
		
	}

	@Override
	public List<FeeNameVo> queryFeeName() throws Exception {
        List<FeeNameVo> feeNameVoListNew = new ArrayList<>();
        List<FeeNameVo> feeNameVoList = medicalFeeDetailDAO.queryFeeName();
        for (FeeNameVo vo : feeNameVoList) {
            vo.setFeeStatCode(MedicalFeeDetailAction.CODEPREFIX+vo.getFeeStatCode());
            feeNameVoListNew.add(vo);
        }
        return feeNameVoListNew;
	}

	@Override
	public List<FeeDetailsVo> queryFeeDetails() throws Exception {
		return medicalFeeDetailDAO.queryFeeDetails();
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String medicalrecordId,String startTime,String endTime) throws Exception {
		return medicalFeeDetailDAO.queryInpatientInfo(medicalrecordId,startTime,endTime);
	}

	@Override
	public List<MinfeeStatCode> queryMinfeeStat(String feeCode) throws Exception {
		return medicalFeeDetailDAO.queryMinfeeStat(feeCode);
	}

	@Override
	public List<FeeNameVo> queryFeeNameToMinfee() throws Exception {
		return  medicalFeeDetailDAO.queryFeeNameToMinfee();
	}

	@Override
	public Map<String, Object> queryFeeDetailsByES(String medicalrecordId,String startTime,String endTime, String page, String rows) {
        Map<String, Object> resultMap = new HashMap<>(2);
		List<InpatientFeeVo> list = new ArrayList<>();
		// 过滤分页数量
		List<String> filterMedicalrecordIds = new ArrayList<>();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//                .filter(QueryBuilders.termsQuery("in_state", "R", "I"))
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			boolQuery.filter(QueryBuilders.rangeQuery("in_date")
					.gte(DateUtils.parseDateY_M_D(startTime))
					.lt(DateUtils.addDay(DateUtils.parseDateY_M_D(endTime), 1)));
		}
        if (StringUtils.isNotBlank(medicalrecordId)) {
            boolQuery.filter(QueryBuilders.termQuery("medicalrecord_id", medicalrecordId));
            resultMap.put("total", 1);
        } else {
            SearchResponse filterSearchResponse = client.prepareSearch(inpatient_list_index)
                    .setTypes(inpatient_list_type).setQuery(boolQuery)
                    .addSort("in_date", SortOrder.DESC)
                    .addAggregation(AggregationBuilders.terms("medicalrecordId").field("medicalrecord_id").size(0))
                    .setSize(0).get();
            Terms medicalrecordIdTerms = filterSearchResponse.getAggregations().get("medicalrecordId");
            for (int i = (Integer.valueOf(page) - 1) * Integer.valueOf(rows);
                 i < medicalrecordIdTerms.getBuckets().size() && i < Integer.valueOf(page) * Integer.valueOf(rows); i++) {
                filterMedicalrecordIds.add(medicalrecordIdTerms.getBuckets().get(i).getKeyAsString());
            }
            boolQuery.filter(QueryBuilders.termsQuery("medicalrecord_id", filterMedicalrecordIds));
            resultMap.put("total", medicalrecordIdTerms.getBuckets().size());
        }
        SearchResponse searchResponse = client.prepareSearch(inpatient_list_index)
				.setTypes(inpatient_list_type).setQuery(boolQuery)
                .addSort("in_date", SortOrder.DESC)
				.addAggregation(AggregationBuilders.terms("medicalrecordId").field("medicalrecord_id").size(0)
						.subAggregation(AggregationBuilders.terms("name").field("name").size(0))
						.subAggregation(AggregationBuilders.terms("prepayCost").field("prepay_cost").size(0))
                        .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                                .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))))
				.setSize(0).get();
		Terms medicalrecordIds = searchResponse.getAggregations().get("medicalrecordId");
		for (Terms.Bucket medicalrecord : medicalrecordIds.getBuckets()) {
		    Double totCost = 0d;
            InpatientFeeVo inpatientFeeVo = new InpatientFeeVo();
            inpatientFeeVo.setMedicalrecordId(medicalrecord.getKeyAsString());
			Terms names = medicalrecord.getAggregations().get("name");
			if (names.getBuckets() != null && names.getBuckets().size() > 0) {
                inpatientFeeVo.setPatientName(names.getBuckets().get(0).getKeyAsString());
			}
			Terms prepayCosts = medicalrecord.getAggregations().get("prepayCost");
            if (prepayCosts.getBuckets() != null && prepayCosts.getBuckets().size() > 0) {
                inpatientFeeVo.setPrepayCost((Double) prepayCosts.getBuckets().get(0).getKeyAsNumber());
            }
            Terms feeStatCodes = medicalrecord.getAggregations().get("feeStatCode");
            for (Terms.Bucket feeStat : feeStatCodes.getBuckets()) {
				Sum sum = feeStat.getAggregations().get("totCost");
                totCost += sum.getValue();
				this.setCost(inpatientFeeVo, sum.getValue(), feeStat.getKeyAsString());
			}
            inpatientFeeVo.setFeeTot(totCost);
            inpatientFeeVo.setExitFee(inpatientFeeVo.getPrepayCost() - inpatientFeeVo.getFeeTot());
			list.add(inpatientFeeVo);
		}
        resultMap.put("rows", list);
		logger.info("住院统计分析_患者医药费用明细查询用时："+searchResponse.getTookInMillis()+"ms,  分片个数："+searchResponse.getTotalShards());
		return resultMap;
	}

    /**
     *
     * @param vo
     * @param cost 费用金额数值
     * @param code 统计费用代码
     * @return vo
     */
    private InpatientFeeVo setCost(InpatientFeeVo vo, Double cost, String code) {
        if (StringUtils.isBlank(code) || cost == 0) {
            return vo;
        }
        try {
            //"totCost"+"统计代码"是vo的各个字段
            Field field = InpatientFeeVo.class.getDeclaredField(MedicalFeeDetailAction.CODEPREFIX + code);
            field.setAccessible(true);
            field.set(vo, cost);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("InpatientFeeVo中不存在该统计类别！", e);
        }
        return vo;
    }

}
