package cn.honry.statistics.finance.department.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.statistics.finance.department.service.DeptIncomeService;
import cn.honry.statistics.finance.statistic.dao.StatisticDao;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsjdbcDao;
import cn.honry.statistics.util.echartsVo.PieVo;
import cn.honry.utils.DateUtils;

@Service("deptIncomeService")
public class DeptIncomeServiceImpl implements DeptIncomeService {
    private Logger logger = Logger.getLogger(DeptIncomeServiceImpl.class);
    @Resource(name = "statisticDao")
    private StatisticDao statisticDao;

    @Resource(name = "reportFormsjdbcDao")
    private ReportFormsjdbcDao reportFormsjdbcDao;

    @Resource(name = "innerCodeDao")
    private CodeInInterDAO innerCodeDao;

    @Resource(name = "client")
    private Client client;

    @Value("${es.inpatient_list.index}")
    private String inpatient_list_index;

    @Value("${es.inpatient_list.type}")
    private String inpatient_list_type;

    @Value("${es.outpatient_feedetail.index}")
    private String outpatient_feedetail_index;

    @Value("${es.outpatient_feedetail.type}")
    private String outpatient_feedetail_type;

    /**
     * @param date     日期 dateSign为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"，为4时格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，
     * @param dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
     * @throws Exception
     * @Description： 科室收入统计  elasticsearch实现
     * @author 朱振坤
     */
    @Override
    public Map<String, Object> deptIncomeChartsByES(String date, String dateSign, String deptCodes) throws Exception {
        //过滤后的住院科室
        List<String> inpatientIds = new ArrayList<>();
        List<String> outpatientIds = new ArrayList<>();
        String[] deptCodeArray = deptCodes.split(",");
        List<SysDepartment> inpatientList = statisticDao.getDept();
        List<SysDepartment> outpatientList = reportFormsjdbcDao.getDept();
//		//将过滤后的科室划分为住院科室和门诊科室
        for (String code : deptCodeArray) {
            for (SysDepartment anInpatient : inpatientList) {
                if (code.equals(anInpatient.getDeptCode())) {
                    inpatientIds.add(code);
                    break;
                }
            }
            for (SysDepartment anOutpatient : outpatientList) {
                if (code.equals(anOutpatient.getDeptCode())) {
                    outpatientIds.add(code);
                    break;
                }
            }
        }
        Map<String, Object> queryMap = new HashMap<>(7);
        DateRangeBuilder dateRangeBuilder = AggregationBuilders.dateRange("date").field("fee_date");
        DateRangeBuilder huanbiBuilder = AggregationBuilders.dateRange("huanbi").field("fee_date");
        DateRangeBuilder tongbiBuilder = AggregationBuilders.dateRange("tongbi").field("fee_date");
        if ("1".equals(dateSign)) {
            Date thisDayOfThisYear = DateUtils.parseDateY_M_D(date);//当天0点
            Date nextDayOfThisYear = DateUtils.addDay(thisDayOfThisYear, 1);//当天的下一天0点
            dateRangeBuilder.addRange("day", thisDayOfThisYear, nextDayOfThisYear);
            for (int i = 1; i <= 6; i++) {//比较6个柱子
                Date startDayByhuanbi = DateUtils.addDay(thisDayOfThisYear, -i + 1);
                Date startDayBytongbi = DateUtils.addYear(thisDayOfThisYear, -i + 1);
                huanbiBuilder.addRange(DateUtils.formatDateY_M_D(startDayByhuanbi), startDayByhuanbi, DateUtils.addDay(startDayByhuanbi, 1));
                tongbiBuilder.addRange(DateUtils.formatDateY_M_D(startDayBytongbi), startDayBytongbi, DateUtils.addDay(startDayBytongbi, 1));
            }
        } else if ("2".equals(dateSign)) {
            Date thisMonthOfThisYear = DateUtils.parseDateY_M(date);//当月1日0点
            Date nextMonthOfThisYear = DateUtils.addMonth(thisMonthOfThisYear, 1);//下月1日0点
            dateRangeBuilder.addRange("month", thisMonthOfThisYear, nextMonthOfThisYear);
            for (int i = 1; i <= 6; i++) {
                Date startMonthByhuanbi = DateUtils.addMonth(thisMonthOfThisYear, -i + 1);
                Date startMonthBytongbi = DateUtils.addYear(thisMonthOfThisYear, -i + 1);
                huanbiBuilder.addRange(DateUtils.formatDateY_M(startMonthByhuanbi), startMonthByhuanbi, DateUtils.addMonth(startMonthByhuanbi, 1));
                tongbiBuilder.addRange(DateUtils.formatDateY_M(startMonthBytongbi), startMonthBytongbi, DateUtils.addMonth(startMonthBytongbi, 1));
            }
        } else if ("3".equals(dateSign)) {
            Date thisYear = DateUtils.parseDateY(date);//当年1月1日0点
            Date nextYear = DateUtils.addYear(thisYear, 1);//当年的下一年1月1日0点
            dateRangeBuilder.addRange("year", thisYear, nextYear);
            for (int i = 1; i <= 6; i++) {
                Date startYearByhuanbi = DateUtils.addYear(thisYear, -i + 1);
                huanbiBuilder.addRange(DateUtils.formatDateY(startYearByhuanbi), startYearByhuanbi, DateUtils.addYear(startYearByhuanbi, 1));
            }
        } else if ("4".equals(dateSign)) {
            String[] dates = date.split(",");
            Date customStartDate = DateUtils.parseDateY_M_D(dates[0]);//自定义开始日期
            Date nextDayOfcustomsEndDate = DateUtils.addDay(DateUtils.parseDateY_M_D(dates[1]), 1);//自定义结束日期的下一天0点
            dateRangeBuilder.addRange("custom", customStartDate, nextDayOfcustomsEndDate);
        } else {
            throw new IllegalArgumentException("dateSign参数无效！");
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.typeQuery(inpatient_list_type))
                        .filter(QueryBuilders.termsQuery("inhos_deptcode", inpatientIds)))
                .should(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.typeQuery(outpatient_feedetail_type))
                        .filter(QueryBuilders.termQuery("pay_flag", 1))
                        .filter(QueryBuilders.termsQuery("reg_dpcd", outpatientIds)));
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(inpatient_list_index, outpatient_feedetail_index)
                .setTypes(inpatient_list_type, outpatient_feedetail_type).setQuery(boolQuery)
                .addAggregation(dateRangeBuilder
                        .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
                        .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                                .order(Terms.Order.term(true))
                                .subAggregation(AggregationBuilders.terms("feeStatName").field("fee_stat_name").size(0))
                                .subAggregation(AggregationBuilders.sum("totCostByFee").field("tot_cost")))
                        .subAggregation(AggregationBuilders.terms("areaCode").field("area_code").size(0)
                                .order(Terms.Order.term(true))
                                .subAggregation(AggregationBuilders.sum("totCostByArea").field("tot_cost"))));
        TermsBuilder inpatientBuilder = null;
        TermsBuilder outpatientBuilder = null;
        if (!"4".equals(dateSign)) {
            inpatientBuilder = AggregationBuilders.terms("inhosDeptcode").field("inhos_deptcode").size(0)
                    .subAggregation(AggregationBuilders.terms("inhosName").field("inhos_name").size(0))
                    .subAggregation(huanbiBuilder);
            outpatientBuilder = AggregationBuilders.terms("regDpcd").field("reg_dpcd").size(0)
                    .subAggregation(AggregationBuilders.terms("regDpcdName").field("dept_name").size(0))
                    .subAggregation(huanbiBuilder
                            .subAggregation(AggregationBuilders.sum("totCostByHuanbi").field("tot_cost")));
        } else {
            inpatientBuilder = AggregationBuilders.terms("inhosDeptcode").field("inhos_deptcode").size(0)
                    .subAggregation(AggregationBuilders.terms("inhosName").field("inhos_name").size(0));
            outpatientBuilder = AggregationBuilders.terms("regDpcd").field("reg_dpcd").size(0)
                    .subAggregation(AggregationBuilders.terms("regDpcdName").field("dept_name").size(0));
        }
        if (!"3".equals(dateSign) && !"4".equals(dateSign)) {
            inpatientBuilder.subAggregation(tongbiBuilder);
            outpatientBuilder.subAggregation(tongbiBuilder
                    .subAggregation(AggregationBuilders.sum("totCostByTongbi").field("tot_cost")));
        }
        searchRequestBuilder.addAggregation(inpatientBuilder).addAggregation(outpatientBuilder);
        SearchResponse searchResponse = searchRequestBuilder.setSize(0).execute().actionGet();
        Range rangeDates = searchResponse.getAggregations().get("date");
        for (Range.Bucket rangeDate : rangeDates.getBuckets()) {
            List<PieVo> feeList = new ArrayList<PieVo>();
            List<PieVo> areaList = new ArrayList<PieVo>();
            Sum totCostSum = rangeDate.getAggregations().get("totCost");
            Terms feeStatCodes = rangeDate.getAggregations().get("feeStatCode");
            for (Terms.Bucket feeStat : feeStatCodes.getBuckets()) {
                PieVo vo = new PieVo();
                Terms feeStatNames = feeStat.getAggregations().get("feeStatName");
                if (feeStatNames.getBuckets() != null && feeStatNames.getBuckets().size() > 0) {
                    vo.setName(feeStatNames.getBuckets().get(0).getKeyAsString());
                }
                Sum sum = feeStat.getAggregations().get("totCostByFee");
                vo.setValue((double) Math.round(sum.getValue() * 100) / 100);
                feeList.add(vo);
            }
            double cost2And3 = 0;
            Terms areaCodes = rangeDate.getAggregations().get("areaCode");
            for (Terms.Bucket area : areaCodes.getBuckets()) {
                if ("2".equals(area.getKeyAsString()) || "3".equals(area.getKeyAsString())) {//住院表的院区code有0，处方表院区code没有0
                    PieVo vo = new PieVo();
                    Sum sum = area.getAggregations().get("totCostByArea");
                    cost2And3 += sum.getValue();
                    vo.setName(innerCodeDao.getDictionaryByCode("hospitalArea", area.getKeyAsString()).getName());
                    vo.setValue((double) Math.round(sum.getValue() * 100) / 100);
                    areaList.add(vo);
                }
            }
            //另一个院区的收入,除了code为1，还有code为0或null,等于总收入减去code为2和3的收入
            double otherTotCost = (double) Math.round(totCostSum.getValue() * 100 - cost2And3 * 100) / 100;

            if (otherTotCost != 0) {
                PieVo otherVo = new PieVo();
                otherVo.setName(innerCodeDao.getDictionaryByCode("hospitalArea", "1").getName());
                otherVo.setValue(otherTotCost);
                areaList.add(otherVo);
            }
            if ("day".equals(rangeDate.getKeyAsString())) {
                queryMap.put("dayTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                queryMap.put("feeOfDay", feeList);
                queryMap.put("areaOfDay", areaList);
            } else if ("month".equals(rangeDate.getKeyAsString())) {
                queryMap.put("monthTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                queryMap.put("feeOfMonth", feeList);
                queryMap.put("areaOfMonth", areaList);
            } else if ("year".equals(rangeDate.getKeyAsString())) {
                queryMap.put("yearTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                queryMap.put("feeOfYear", feeList);
                queryMap.put("areaOfYear", areaList);
            } else if ("custom".equals(rangeDate.getKeyAsString())) {
                queryMap.put("customTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                queryMap.put("feeOfCustom", feeList);
                queryMap.put("areaOfCustom", areaList);
            }
        }
        List<String> deptsName = new ArrayList<String>();
        Set<String> xAxisByHuanbiByDay = new LinkedHashSet<String>();
        List<List<Double>> huanbiByDay = new ArrayList<List<Double>>();
        Set<String> xAxisByTongbiByDay = new LinkedHashSet<String>();
        List<List<Double>> tongbiByDay = new ArrayList<List<Double>>();
        Set<String> xAxisByHuanbiByMonth = new LinkedHashSet<String>();
        List<List<Double>> huanbiByMonth = new ArrayList<List<Double>>();
        Set<String> xAxisByTongbiByMonth = new LinkedHashSet<String>();
        List<List<Double>> tongbiByMonth = new ArrayList<List<Double>>();
        Set<String> xAxisByHuanbiByYear = new LinkedHashSet<String>();
        List<List<Double>> huanbiByYear = new ArrayList<List<Double>>();
        Terms inhosDeptcodes = searchResponse.getAggregations().get("inhosDeptcode");
        Terms regDpcds = searchResponse.getAggregations().get("regDpcd");
        for (Terms.Bucket inhosDept : inhosDeptcodes.getBuckets()) {
            Terms inhosNames = inhosDept.getAggregations().get("inhosName");
            String inhosName = null;
            if (inhosNames.getBuckets() != null && inhosNames.getBuckets().size() > 0) {
                inhosName = inhosNames.getBuckets().get(0).getKeyAsString();
                deptsName.add(inhosName);
            }
            List<Double> deptCostByHuanbi = new ArrayList<Double>();
            List<Double> deptCostByTongbi = new ArrayList<Double>();
            if (!"4".equals(dateSign)) {
                Range huanbiDates = inhosDept.getAggregations().get("huanbi");
                for (Range.Bucket huanbiDate : huanbiDates.getBuckets()) {
                    if ("1".equals(dateSign)) {
                        Sum sum = huanbiDate.getAggregations().get("totCostByHuanbi");
                        deptCostByHuanbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByHuanbiByDay.add(huanbiDate.getKeyAsString());
                    } else if ("2".equals(dateSign)) {
                        Sum sum = huanbiDate.getAggregations().get("totCostByHuanbi");
                        deptCostByHuanbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByHuanbiByMonth.add(huanbiDate.getKeyAsString());
                    } else if ("3".equals(dateSign)) {
                        Sum sum = huanbiDate.getAggregations().get("totCostByHuanbi");
                        deptCostByHuanbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByHuanbiByYear.add(huanbiDate.getKeyAsString());
                    }
                }
            }
            if (!"3".equals(dateSign) && !"4".equals(dateSign)) {
                Range tongbiDates = inhosDept.getAggregations().get("tongbi");
                for (Range.Bucket tongbiDate : tongbiDates.getBuckets()) {
                    if ("1".equals(dateSign)) {
                        Sum sum = tongbiDate.getAggregations().get("totCostByTongbi");
                        deptCostByTongbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByTongbiByDay.add(tongbiDate.getKeyAsString());
                    } else if ("2".equals(dateSign)) {
                        Sum sum = tongbiDate.getAggregations().get("totCostByTongbi");
                        deptCostByTongbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByTongbiByMonth.add(tongbiDate.getKeyAsString());
                    }
                }
            }
            if ("1".equals(dateSign)) {
                huanbiByDay.add(deptCostByHuanbi);
                tongbiByDay.add(deptCostByTongbi);
            } else if ("2".equals(dateSign)) {
                huanbiByMonth.add(deptCostByHuanbi);
                tongbiByMonth.add(deptCostByTongbi);
            } else if ("3".equals(dateSign)) {
                huanbiByYear.add(deptCostByHuanbi);
            }
        }
        for (Terms.Bucket regDpcd : regDpcds.getBuckets()) {
            Terms regDpcdNames = regDpcd.getAggregations().get("regDpcdName");
            String regDpcdName = null;
            if (regDpcdNames.getBuckets() != null && regDpcdNames.getBuckets().size() > 0) {
                regDpcdName = regDpcdNames.getBuckets().get(0).getKeyAsString();
                deptsName.add(regDpcdName);
            }
            List<Double> deptCostByHuanbi = new ArrayList<Double>();
            List<Double> deptCostByTongbi = new ArrayList<Double>();
            if (!"4".equals(dateSign)) {
                Range huanbiDates = regDpcd.getAggregations().get("huanbi");
                for (Range.Bucket huanbiDate : huanbiDates.getBuckets()) {
                    if ("1".equals(dateSign)) {
                        Sum sum = huanbiDate.getAggregations().get("totCostByHuanbi");
                        deptCostByHuanbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByHuanbiByDay.add(huanbiDate.getKeyAsString());
                    } else if ("2".equals(dateSign)) {
                        Sum sum = huanbiDate.getAggregations().get("totCostByHuanbi");
                        deptCostByHuanbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByHuanbiByMonth.add(huanbiDate.getKeyAsString());
                    } else if ("3".equals(dateSign)) {
                        Sum sum = huanbiDate.getAggregations().get("totCostByHuanbi");
                        deptCostByHuanbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByHuanbiByYear.add(huanbiDate.getKeyAsString());
                    }
                }
            }
            if (!"3".equals(dateSign) && !"4".equals(dateSign)) {
                Range tongbiDates = regDpcd.getAggregations().get("tongbi");
                for (Range.Bucket tongbiDate : tongbiDates.getBuckets()) {
                    if ("1".equals(dateSign)) {
                        Sum sum = tongbiDate.getAggregations().get("totCostByTongbi");
                        deptCostByTongbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByTongbiByDay.add(tongbiDate.getKeyAsString());
                    } else if ("2".equals(dateSign)) {
                        Sum sum = tongbiDate.getAggregations().get("totCostByTongbi");
                        deptCostByTongbi.add((double) Math.round(sum.getValue() * 100) / 100);
                        xAxisByTongbiByMonth.add(tongbiDate.getKeyAsString());
                    }
                }
            }
            if ("1".equals(dateSign)) {
                huanbiByDay.add(deptCostByHuanbi);
                tongbiByDay.add(deptCostByTongbi);
            } else if ("2".equals(dateSign)) {
                huanbiByMonth.add(deptCostByHuanbi);
                tongbiByMonth.add(deptCostByTongbi);
            } else if ("3".equals(dateSign)) {
                huanbiByYear.add(deptCostByHuanbi);
            }
        }
        queryMap.put("deptsName", deptsName);
        if ("1".equals(dateSign)) {
            queryMap.put("xAxisByHuanbiByDay", xAxisByHuanbiByDay);
            queryMap.put("huanbiByDay", huanbiByDay);
            queryMap.put("xAxisByTongbiByDay", xAxisByTongbiByDay);
            queryMap.put("tongbiByDay", tongbiByDay);
        } else if ("2".equals(dateSign)) {
            queryMap.put("xAxisByHuanbiByMonth", xAxisByHuanbiByMonth);
            queryMap.put("huanbiByMonth", huanbiByMonth);
            queryMap.put("xAxisByTongbiByMonth", xAxisByTongbiByMonth);
            queryMap.put("tongbiByMonth", tongbiByMonth);
        } else if ("3".equals(dateSign)) {
            queryMap.put("xAxisByHuanbiByYear", xAxisByHuanbiByYear);
            queryMap.put("huanbiByYear", huanbiByYear);
        }
        logger.info("科室收入统计查询用时：" + searchResponse.getTookInMillis() + "ms,  分片个数：" + searchResponse.getTotalShards());
        return queryMap;
    }

}
