package cn.honry.statistics.finance.statistic.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.finance.statistic.dao.StatisticDao;
import cn.honry.statistics.finance.statistic.service.StatisticService;
import cn.honry.statistics.finance.statistic.vo.ResultVo;
import cn.honry.statistics.finance.statistic.vo.StatisticVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.NumberUtil;

/**
 * 收入统计汇总业务逻辑类
 *
 * @version 1.0
 * @Author:luyanshou
 */
@Service("statisticService")
@Transactional
public class StatisticServiceImpl implements StatisticService {

    private Logger logger = Logger.getLogger(StatisticServiceImpl.class);

    private StatisticDao statisticDao;

    @Autowired
    @Qualifier(value = "statisticDao")
    public void setStatisticDao(StatisticDao statisticDao) {
        this.statisticDao = statisticDao;
    }

    @Autowired
    @Qualifier(value = "deptInInterService")
    private DeptInInterService deptInInterService;

    public void setDeptInInterService(DeptInInterService deptInInterService) {
        this.deptInInterService = deptInInterService;
    }

    @Resource(name = "client")
    private Client client;

    @Value("${es.inpatient_list.index}")
    private String inpatient_list_index;

    @Value("${es.inpatient_list.type}")
    private String inpatient_list_type;

    /**
     * 查询住院下的科室信息
     *
     * @throws Exception
     */
    @Override
    public List<SysDepartment> getDept() throws Exception {
        return statisticDao.getDept();
    }

    /**
     * 根据报表代码查询 最小费用与统计大类对照信息
     *
     * @throws Exception
     */
    @Override
    public List<StatisticVo> getfeetStat(String reportCode) throws Exception {
        return statisticDao.getfeetStat(reportCode);
    }

    /**
     * 根据报表代码查询 统计费用名称
     *
     * @throws Exception
     */
    @Override
    public List<StatisticVo> getfeeStatName(String reportCode) throws Exception {
        return statisticDao.getfeeStatName(reportCode);
    }

    /**
     * 查询报表记录信息
     *
     * @throws Exception
     */
    @Override
    public List<StatisticVo> getreport() throws Exception {
        return statisticDao.getreport();
    }


    /**
     * 查询统计结果信息列表 elasticsearch实现
     *
     * @param sTime 查询开始时间 以“createtime”为查询字段
     * @param eTime 查询结束时间 包括当日
     * @param ids   科室id字符串，多个id以“,”隔开
     * @return 封装easyUi表格的json数据的集合
     * @Author: 朱振坤
     */
    @Override
    public List<ResultVo> statisticDataByES(String sTime, String eTime, String ids) {
        List<ResultVo> list = new ArrayList<>();
        //结束时间加一天
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("fee_date")
                        .gte(DateUtils.parseDateY_M_D(sTime))
                        .lt(DateUtils.addDay(DateUtils.parseDateY_M_D(eTime), 1)))
                .filter(QueryBuilders.termsQuery("inhos_deptcode", ids.split(",")));
        SearchResponse searchResponse = client.prepareSearch(inpatient_list_index)
                .setTypes(inpatient_list_type).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms("inhosDeptcode").field("inhos_deptcode").size(0)
                        .subAggregation(AggregationBuilders.terms("inhosName").field("inhos_name").size(0))
                        .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                                .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))))
                .setSize(0).execute().actionGet();
        Terms inhosDeptcodes = searchResponse.getAggregations().get("inhosDeptcode");
        for (Terms.Bucket inhosDept : inhosDeptcodes.getBuckets()) {
            boolean needFlag = false;
            ResultVo resultVo = new ResultVo();
            resultVo.setInhosDeptcode(inhosDept.getKeyAsString());
            Terms inhosNames = inhosDept.getAggregations().get("inhosName");
            if (inhosNames.getBuckets() != null && inhosNames.getBuckets().size() > 0) {
                resultVo.setDeptName(inhosNames.getBuckets().get(0).getKeyAsString());
            }
            Terms feeStatCodes = inhosDept.getAggregations().get("feeStatCode");
            for (Terms.Bucket feeStat : feeStatCodes.getBuckets()) {
                Sum sum = feeStat.getAggregations().get("totCost");
                double cost = Double.valueOf(NumberUtil.init().format(sum.getValue(), 2));
                if (cost != 0.0) {
                    needFlag = true;
                    this.setCost(resultVo, cost, feeStat.getKeyAsString());
                }
            }
            if (needFlag) {
                // resultVo的各收费类型不都为0时，才返回结果
                list.add(resultVo);
            }
        }
        Collections.sort(list, new Comparator<ResultVo>() {
            private Collator collator = Collator.getInstance(java.util.Locale.CHINA);

            @Override
            public int compare(ResultVo o1, ResultVo o2) {
                CollationKey key1 = collator.getCollationKey(o1.getDeptName());
                CollationKey key2 = collator.getCollationKey(o2.getDeptName());
                return key1.compareTo(key2);
            }
        });
        logger.info("住院统计分析_住院收入统计汇总查询用时：" + searchResponse.getTookInMillis() + "ms,  分片个数：" + searchResponse.getTotalShards());
        return list;
    }

    /**
     * @param vo
     * @param cost 费用金额数值
     * @param code 统计费用代码
     * @return vo
     */
    private ResultVo setCost(ResultVo vo, double cost, String code) {
        if (StringUtils.isBlank(code) || cost == 0.0) {
            return vo;
        }
        try {
            //"totCost"+"统计代码"是vo的各个字段
            Field field = ResultVo.class.getDeclaredField("totCost" + code);
            field.setAccessible(true);
            field.set(vo, cost);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ResultVo中不存在该统计类别！", e);
        }
        return vo;
    }

    @Override
    public List<MenuListVO> getDeptList() throws Exception {
        return statisticDao.getDeptList();
    }

    /**
     * 住院收入统计汇总表导出
     *
     * @Author: huzhenguo
     * @CreateDate: 2017年5月19日 下午6:00:49
     * @Modifier: huzhenguo
     * @ModifyDate: 2017年5月19日 下午6:00:49
     * @ModifyRmk:
     * @version: V1.0
     */
    @Override
    public FileUtil export(List<ResultVo> list, FileUtil fUtil) {
        for (ResultVo model : list) {
            String record = "";
            record = CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
            record += model.getTotCost01() + ",";
            record += model.getTotCost02() + ",";
            record += model.getTotCost03() + ",";
            record += model.getTotCost04() + ",";
            record += model.getTotCost05() + ",";
            record += model.getTotCost07() + ",";
            record += model.getTotCost08() + ",";
            record += model.getTotCost09() + ",";
            record += model.getTotCost10() + ",";
            record += model.getTotCost11() + ",";
            record += model.getTotCost12() + ",";
            record += model.getTotCost13() + ",";
            record += model.getTotCost14() + ",";
            record += model.getTotCost15() + ",";
            record += model.getTotCost16() + ",";
            record += model.getTotCost() + ",";
            try {
                fUtil.write(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fUtil;
    }

}
