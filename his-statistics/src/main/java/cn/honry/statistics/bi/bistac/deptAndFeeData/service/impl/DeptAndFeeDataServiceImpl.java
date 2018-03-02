package cn.honry.statistics.bi.bistac.deptAndFeeData.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.statistics.bi.bistac.deptAndFeeData.service.DeptAndFeeDataService;
import cn.honry.statistics.bi.bistac.deptAndFeeData.vo.StatisticsVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.statistics.util.echartsVo.PieVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

/**
 * 移植移动端住院收入统计，供mongodb预处理
 */
@Service("deptAndFeeDataService")
@Transactional
public class DeptAndFeeDataServiceImpl implements DeptAndFeeDataService {
    @Autowired
    @Qualifier("client")
    private Client client;

    @Value("${es.inpatient_list.index}")
    private String inpatient_list_index;

    @Value("${es.inpatient_list.type}")
    private String inpatient_list_type;
    @Resource(name = "innerCodeDao")
    private CodeInInterDAO innerCodeDao;

    /**
     * 住院收入前十名科室和收费项目饼状图统计
     * elasticsearch实现
     *
     * @throws Exception
     */
    @Override
    public String deptAndFeeData(String date) throws Exception {
        Date startDate;
        Date endDate;
        String yearPattern = "^[1-2][0-9]{3}$";
        String monthPattern = "^[1-2][0-9]{3}-(0?[1-9]|1[0-2])$";
        String janPattern = "(0?[13578]|1[02])-(0?[1-9]|[12][0-9]|3[01])";
        String febPattern = "0?2-(0?[1-9]|[12][0-9])";
        String aprPattern = "(0?[469]|11)-(0?[1-9]|[12][0-9]|30)";
        String dayPattern = String.format("^[1-2][0-9]{3}-(%s|%s|%s)$", janPattern,
                febPattern, aprPattern);
        if (date.matches(dayPattern)) {
            startDate = DateUtils.parseDateY_M_D(date);//今天0点
            endDate = DateUtils.addDay(startDate, 1);//明天0点
        } else if (date.matches(monthPattern)) {
            startDate = DateUtils.parseDateY_M(date);//本月1日0点
            endDate = DateUtils.addMonth(startDate, 1);//下月1日0点
        } else if (date.matches(yearPattern)) {
            startDate = DateUtils.parseDateY(date);//今年1月1日0点
            endDate = DateUtils.addYear(startDate, 1);//明年1月1日0点
        } else {
            throw new IllegalArgumentException("日期参数错误！");
        }
        List<StatisticsVo> totCostList = new ArrayList<>();
        List<StatisticsVo> deptPies = new ArrayList<>();
        List<StatisticsVo> feePies = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>(3);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("fee_date").gte(startDate).lt(endDate));
        SearchResponse searchResponse = client.prepareSearch(inpatient_list_index)
                .setTypes(inpatient_list_type).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
                .addAggregation(AggregationBuilders.terms("inhosDeptcode").field("inhos_deptcode").size(0)
                        .order(Terms.Order.aggregation("totCostByDept", false))
                        .subAggregation(AggregationBuilders.terms("inhosName").field("inhos_name").size(0))
                        .subAggregation(AggregationBuilders.sum("totCostByDept").field("tot_cost")))
                .addAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                        .order(Terms.Order.aggregation("totCostByfeeStat", false))
                        .subAggregation(AggregationBuilders.terms("feeStatName").field("fee_stat_name").size(0))
                        .subAggregation(AggregationBuilders.sum("totCostByfeeStat").field("tot_cost")))
                .setSize(0).execute().actionGet();
        Sum totCost = searchResponse.getAggregations().get("totCost");
        StatisticsVo totCostVo = new StatisticsVo();
        totCostVo.setName("总收入");
        totCostVo.setValue(Double.valueOf(NumberUtil.init().format(totCost.value(), 2)));
        totCostList.add(totCostVo);
        Terms inhosDeptcodes = searchResponse.getAggregations().get("inhosDeptcode");
        //最多取前10个
        for (int i = 0; i < inhosDeptcodes.getBuckets().size() && i < 10; i++) {
            StatisticsVo statisticsVo = new StatisticsVo();
            Terms inhosNames = inhosDeptcodes.getBuckets().get(i).getAggregations().get("inhosName");
            if (inhosNames.getBuckets() != null && inhosNames.getBuckets().size() > 0) {
                //科室名称
                statisticsVo.setName(inhosNames.getBuckets().get(0).getKeyAsString());
            }
            Sum sum = inhosDeptcodes.getBuckets().get(i).getAggregations().get("totCostByDept");
            statisticsVo.setValue(Double.valueOf(NumberUtil.init().format(sum.getValue(), 2)));
            deptPies.add(statisticsVo);
        }
        Terms feeStatCodes = searchResponse.getAggregations().get("feeStatCode");
        for (Terms.Bucket feeStatCode : feeStatCodes.getBuckets()) {
            StatisticsVo statisticsVo = new StatisticsVo();
            Terms feeStatNames = feeStatCode.getAggregations().get("feeStatName");
            if (feeStatNames.getBuckets() != null && feeStatNames.getBuckets().size() > 0) {
                //费用统计类别名称
                statisticsVo.setName(feeStatNames.getBuckets().get(0).getKeyAsString());
            }
            Sum sum = feeStatCode.getAggregations().get("totCostByfeeStat");
            statisticsVo.setValue(Double.valueOf(NumberUtil.init().format(sum.getValue(), 2)));
            feePies.add(statisticsVo);
        }
        map.put("feePies", feePies);
        map.put("totCost", totCostList);
        map.put("deptPies", deptPies);
        System.out.println("查询用时：" + searchResponse.getTookInMillis() + "ms,  分片个数：" + searchResponse.getTotalShards());
        return JSONUtils.toJson(map);
    }

    /**
     * 住院收入同环比统计
     * elasticsearch实现
     *
     * @throws Exception
     */
    @Override
    public String tonghuanbiDataByES(String date, Integer num) throws Exception {
        if (num == null || num < 2) {
            throw new IllegalArgumentException("同环比至少比较2个日期！");
        }
        DateRangeBuilder huanbiBuilder = AggregationBuilders.dateRange("huanbi").field("fee_date");
        DateRangeBuilder tongbiBuilder = AggregationBuilders.dateRange("tongbi").field("fee_date");
        String yearPattern = "^[1-2][0-9]{3}$";
        String monthPattern = "^[1-2][0-9]{3}-(0?[1-9]|1[0-2])$";
        String janPattern = "(0?[13578]|1[02])-(0?[1-9]|[12][0-9]|3[01])";
        String febPattern = "0?2-(0?[1-9]|[12][0-9])";
        String aprPattern = "(0?[469]|11)-(0?[1-9]|[12][0-9]|30)";
        String dayPattern = String.format("^[1-2][0-9]{3}-(%s|%s|%s)$", janPattern,
                febPattern, aprPattern);
        if (date.matches(dayPattern)) {
            Date thisDayOfThisYear = DateUtils.parseDateY_M_D(date);//当天0点
            for (int i = 1; i <= num; i++) {//比较num个柱子
                Date startDayByhuanbi = DateUtils.addDay(thisDayOfThisYear, -i + 1);
                Date startDayBytongbi = DateUtils.addYear(thisDayOfThisYear, -i + 1);
                huanbiBuilder.addRange(DateUtils.formatDateY_M_D(startDayByhuanbi), startDayByhuanbi, DateUtils.addDay(startDayByhuanbi, 1));
                tongbiBuilder.addRange(DateUtils.formatDateY_M_D(startDayBytongbi), startDayBytongbi, DateUtils.addDay(startDayBytongbi, 1));
            }

        } else if (date.matches(monthPattern)) {
            Date thisMonthOfThisYear = DateUtils.parseDateY_M(date);//当月1日0点
            for (int i = 1; i <= num; i++) {
                Date startMonthByhuanbi = DateUtils.addMonth(thisMonthOfThisYear, -i + 1);
                Date startMonthBytongbi = DateUtils.addYear(thisMonthOfThisYear, -i + 1);
                huanbiBuilder.addRange(DateUtils.formatDateY_M(startMonthByhuanbi), startMonthByhuanbi, DateUtils.addMonth(startMonthByhuanbi, 1));
                tongbiBuilder.addRange(DateUtils.formatDateY_M(startMonthBytongbi), startMonthBytongbi, DateUtils.addMonth(startMonthBytongbi, 1));
            }
        } else if (date.matches(yearPattern)) {//yyyy格式日期没有同比
            Date thisYear = DateUtils.parseDateY(date);//当年1月1日0点
            for (int i = 1; i <= num; i++) {
                Date startYearByhuanbi = DateUtils.addYear(thisYear, -i + 1);
                huanbiBuilder.addRange(DateUtils.formatDateY(startYearByhuanbi), startYearByhuanbi, DateUtils.addYear(startYearByhuanbi, 1));
            }
        } else {
            throw new IllegalArgumentException("日期参数错误！");
        }
        List<StatisticsVo> huanbiBars = new ArrayList<>();
        List<StatisticsVo> tongbiBars = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(2);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(inpatient_list_index)
                .setTypes(inpatient_list_type).addAggregation(huanbiBuilder
                        .subAggregation(AggregationBuilders.sum("totCostByhuanbi").field("tot_cost")));
        if (!date.matches(yearPattern)) {
            searchRequestBuilder.addAggregation(tongbiBuilder
                    .subAggregation(AggregationBuilders.sum("totCostBytongbi").field("tot_cost")));
        }
        SearchResponse searchResponse = searchRequestBuilder.setSize(0).execute().actionGet();
        if (!date.matches(yearPattern)) {
            //yyyy格式日期没有同比
            Range tongbiDates = searchResponse.getAggregations().get("tongbi");
            for (Range.Bucket tongbiDate : tongbiDates.getBuckets()) {
                DateTime fromAsDate = (DateTime) tongbiDate.getFrom();
                Sum sum = tongbiDate.getAggregations().get("totCostBytongbi");
                StatisticsVo vo = new StatisticsVo();
                if (date.matches(dayPattern)) {
                    vo.setName(DateUtils.formatDateY_M_D(fromAsDate.toDate()));
                } else {
                    vo.setName(DateUtils.formatDateY_M(fromAsDate.toDate()));
                }
                vo.setValue(Double.valueOf(NumberUtil.init().format(sum.getValue(), 2)));
                tongbiBars.add(vo);
            }
            map.put("tongbiBars", tongbiBars);
        }
        Range huanbiDates = searchResponse.getAggregations().get("huanbi");
        for (Range.Bucket huanbiDate : huanbiDates.getBuckets()) {
            DateTime fromAsDate = (DateTime) huanbiDate.getFrom();
            Sum sum = huanbiDate.getAggregations().get("totCostByhuanbi");
            StatisticsVo vo = new StatisticsVo();
            if (date.matches(dayPattern)) {
                vo.setName(DateUtils.formatDateY_M_D(fromAsDate.toDate()));
            } else if (date.matches(monthPattern)) {
                vo.setName(DateUtils.formatDateY_M(fromAsDate.toDate()));
            } else {
                vo.setName(DateUtils.formatDateY(fromAsDate.toDate()));
            }
            vo.setValue(Double.valueOf(NumberUtil.init().format(sum.getValue(), 2)));
            huanbiBars.add(vo);
        }
        map.put("huanbiBars", huanbiBars);
        System.out.println("查询用时：" + searchResponse.getTookInMillis() + "ms,  分片个数：" + searchResponse.getTotalShards());
        return JSONUtils.toJson(map);
    }

    @Override
    public String queryInpatientChartsByES(String date, String dateSign) throws Exception {
        Map<String, Object> map = new HashMap<>(7);
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
            dateRangeBuilder.addRange("custom", customStartDate, nextDayOfcustomsEndDate);//自定义日期包括结束日期当天
        } else {
            throw new IllegalArgumentException("dateSign参数非法！");
        }

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(inpatient_list_index)
                .setTypes(inpatient_list_type)
                .addAggregation(dateRangeBuilder.subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
                        .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                                .order(Terms.Order.term(true))
                                .subAggregation(AggregationBuilders.terms("feeStatName").field("fee_stat_name").size(0))
                                .subAggregation(AggregationBuilders.sum("totCostByFee").field("tot_cost")))
                        .subAggregation(AggregationBuilders.terms("areaCode").field("area_code").size(0)
                                .order(Terms.Order.term(true))
                                .subAggregation(AggregationBuilders.sum("totCostByArea").field("tot_cost"))));
        if (!"4".equals(dateSign)) {
            searchRequestBuilder.addAggregation(huanbiBuilder.subAggregation(AggregationBuilders.sum("totCostByhuanbi").field("tot_cost")));
        }
        if (!"3".equals(dateSign) && !"4".equals(dateSign)) {
            searchRequestBuilder.addAggregation(tongbiBuilder.subAggregation(AggregationBuilders.sum("totCostBytongbi").field("tot_cost")));
        }
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
                map.put("dayTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                map.put("feeOfDay", feeList);
                map.put("areaOfDay", areaList);
            } else if ("month".equals(rangeDate.getKeyAsString())) {
                map.put("monthTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                map.put("feeOfMonth", feeList);
                map.put("areaOfMonth", areaList);
            } else if ("year".equals(rangeDate.getKeyAsString())) {
                map.put("yearTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                map.put("feeOfYear", feeList);
                map.put("areaOfYear", areaList);
            } else if ("custom".equals(rangeDate.getKeyAsString())) {
                map.put("customTotCost", (double) Math.round(totCostSum.getValue() * 100) / 100);
                map.put("feeOfCustom", feeList);
                map.put("areaOfCustom", areaList);
            }
        }
        List<String> xAxisByHuanbiByDay = new ArrayList<String>();
        List<Double> huanbiByDay = new ArrayList<Double>();
        List<String> xAxisByTongbiByDay = new ArrayList<String>();
        List<Double> tongbiByDay = new ArrayList<Double>();
        List<String> xAxisByHuanbiByMonth = new ArrayList<String>();
        List<Double> huanbiByMonth = new ArrayList<Double>();
        List<String> xAxisByTongbiByMonth = new ArrayList<String>();
        List<Double> tongbiByMonth = new ArrayList<Double>();
        List<String> xAxisByHuanbiByYear = new ArrayList<String>();
        List<Double> huanbiByYear = new ArrayList<Double>();
        if (!"4".equals(dateSign)) {
            Range huanbiDates = searchResponse.getAggregations().get("huanbi");
            for (Range.Bucket huanbiDate : huanbiDates.getBuckets()) {
                Sum sum = huanbiDate.getAggregations().get("totCostByhuanbi");
                if (huanbiDate.getKeyAsString().length() == 10) {
                    xAxisByHuanbiByDay.add(huanbiDate.getKeyAsString());
                    huanbiByDay.add((double) Math.round(sum.getValue() * 100) / 100);
                } else if (huanbiDate.getKeyAsString().length() == 7) {
                    xAxisByHuanbiByMonth.add(huanbiDate.getKeyAsString());
                    huanbiByMonth.add((double) Math.round(sum.getValue() * 100) / 100);
                } else if (huanbiDate.getKeyAsString().length() == 4) {
                    xAxisByHuanbiByYear.add(huanbiDate.getKeyAsString());
                    huanbiByYear.add((double) Math.round(sum.getValue() * 100) / 100);
                }
            }
        }
        if (!"3".equals(dateSign) && !"4".equals(dateSign)) {
            Range tongbiDates = searchResponse.getAggregations().get("tongbi");
            for (Range.Bucket tongbiDate : tongbiDates.getBuckets()) {
                Sum sum = tongbiDate.getAggregations().get("totCostBytongbi");
                if (tongbiDate.getKeyAsString().length() == 10) {
                    xAxisByTongbiByDay.add(tongbiDate.getKeyAsString());
                    tongbiByDay.add((double) Math.round(sum.getValue() * 100) / 100);
                } else if (tongbiDate.getKeyAsString().length() == 7) {
                    xAxisByTongbiByMonth.add(tongbiDate.getKeyAsString());
                    tongbiByMonth.add((double) Math.round(sum.getValue() * 100) / 100);
                }
            }
        }
        if ("1".equals(dateSign)) {
            map.put("xAxisByHuanbiByDay", xAxisByHuanbiByDay);
            map.put("huanbiByDay", huanbiByDay);
            map.put("xAxisByTongbiByDay", xAxisByTongbiByDay);
            map.put("tongbiByDay", tongbiByDay);
        } else if ("2".equals(dateSign)) {
            map.put("xAxisByHuanbiByMonth", xAxisByHuanbiByMonth);
            map.put("huanbiByMonth", huanbiByMonth);
            map.put("xAxisByTongbiByMonth", xAxisByTongbiByMonth);
            map.put("tongbiByMonth", tongbiByMonth);
        } else if ("3".equals(dateSign)) {
            map.put("xAxisByHuanbiByYear", xAxisByHuanbiByYear);
            map.put("huanbiByYear", huanbiByYear);
        }
        return JSONUtils.toJson(map);
    }

    @Override
    public String queryInpatientChartsByMongo(String date, String dateSign) {
        if ("4".equals(dateSign)) {
            return querySeq(date);
        }
        String key = "ZYSRTJ_";
        BasicDBObject where = new BasicDBObject();
        where.append("date", date);
        switch (dateSign) {
            case "1":
                key += "DAY";
                break;
            case "2":
                key += "MONTH";
                break;
            case "3":
                key += "YEAR";
                break;

            default:
                break;
        }
        String s = "";
        DBCursor cursor = new MongoBasicDao().findAlldata(key, where);
        while (cursor.hasNext()) {
            s = cursor.next().get("pcIncome").toString();
        }
        if (StringUtils.isBlank(s)) {
            Map<String, Object> map = new HashMap<>(7);
            if ("1".equals(dateSign)) {
                map.put("dayTotCost", 0.0);
                map.put("feeOfDay", new ArrayList<String>());
                map.put("areaOfDay", new ArrayList<String>());
                map.put("huanbiByDay", new ArrayList<String>());
                map.put("xAxisByHuanbiByDay", new ArrayList<String>());
                map.put("tongbiByDay", new ArrayList<String>());
                map.put("xAxisByTongbiByDay", new ArrayList<String>());
            } else if ("2".equals(dateSign)) {
                map.put("monthTotCost", 0.0);
                map.put("feeOfMonth", new ArrayList<String>());
                map.put("areaOfMonth", new ArrayList<String>());
                map.put("huanbiByMonth", new ArrayList<String>());
                map.put("xAxisByHuanbiByMonth", new ArrayList<String>());
                map.put("tongbiByMonth", new ArrayList<String>());
                map.put("xAxisByTongbiByMonth", new ArrayList<String>());
            } else if ("3".equals(dateSign)) {
                map.put("yearTotCost", 0.0);
                map.put("feeOfYear", new ArrayList<String>());
                map.put("areaOfYear", new ArrayList<String>());
                map.put("huanbiByYear", new ArrayList<String>());
                map.put("xAxisByHuanbiByYear", new ArrayList<String>());
            } else if ("4".equals(dateSign)) {
                map.put("customTotCost", 0.0);
                map.put("feeOfCustom", new ArrayList<String>());
                map.put("areaOfCustom", new ArrayList<String>());
            }
            s = JSONUtils.toJson(map);
        }
        return s;
    }

    public String querySeq(String date) {

        if (StringUtils.isBlank(date)) {
            return "";
        }
        String[] dates = date.split(",");

        Map<String, List<String>> dateMap = ResultUtils.getDate(dates[0], dates[1]);
        List<String> list = new ArrayList<>();
        List<String> yearList = dateMap.get("year");//按年统计的list
        if (yearList.size() > 0) {
            for (String dateY : yearList) {
                String s = queryInpatientChartsByMongo(dateY, "3");
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }

        List<String> monthList = dateMap.get("month");//按月统计的list
        if (monthList.size() > 0) {
            for (String dateM : monthList) {
                String s = queryInpatientChartsByMongo(dateM, "2");
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }

        List<String> dayList = dateMap.get("day");//按日统计的list
        if (dayList.size() > 0) {
            for (String dateD : dayList) {
                String s = queryInpatientChartsByMongo(dateD, "1");//mongodb中的结果
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }
        List<CustomVo> areaList = new ArrayList<>();//存放各个院区的收入
        List<CustomVo> feeList = new ArrayList<>();//存放各个费别对应的金额
        CustomVo vo = new CustomVo();//总收入
        vo.setName("customTotCost");
        if (list.size() > 0) {
            for (String s : list) {
                int i = s.indexOf("areaOf");//院区
                int j = s.indexOf(":", i) + 1;
                String sub = s.substring(j, s.indexOf("]", j) + 1);
                try {
                    List<CustomVo> customVoList = JSONUtils.fromJson(sub, new TypeToken<List<CustomVo>>() {
                    });
                    if (customVoList != null && customVoList.size() > 0) {
                        areaList.addAll(customVoList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int k = s.indexOf("feeOf");//费别
                int l = s.indexOf(":", k) + 1;
                String string = s.substring(l, s.indexOf("]", l) + 1);
                try {
                    List<CustomVo> list2 = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>() {
                    });
                    if (list2 != null && list2.size() > 0) {
                        feeList.addAll(list2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int m = s.indexOf("TotCost") + 9;//总费用
                int n = s.indexOf(",", m);
                if (n == -1) {//TotCost在字符串的最后
                    n = s.indexOf("}", m);
                }
                String val = s.substring(m, n);
                double d = Double.parseDouble(val);
                vo.setValue(vo.getValue() + d);
            }
        }

        Map<String, CustomVo> areaMap = new HashMap<>();
        for (CustomVo customVo : areaList) {//求各个院区的费用之和
            CustomVo vo2 = areaMap.get(customVo.getName());
            if (vo2 != null) {
                vo2.setValue(vo2.getValue() + customVo.getValue());
            } else {
                areaMap.put(customVo.getName(), customVo);
            }
        }
        Map<String, CustomVo> feeMap = new HashMap<>();
        List<String> nameList = new ArrayList<>();//用于对费别的统计结果进行排序
        for (CustomVo customVo : feeList) {//求各个费别对应的总金额
            CustomVo vo2 = feeMap.get(customVo.getName());
            if (vo2 != null) {
                vo2.setValue(vo2.getValue() + customVo.getValue());
            } else {
                feeMap.put(customVo.getName(), customVo);
                nameList.add(customVo.getName());
            }
        }
        List<CustomVo> areaListCustom = new ArrayList<>();
        areaListCustom.addAll(areaMap.values());
        List<CustomVo> feeListCustom = new ArrayList<>();
        for (String str : nameList) {
            CustomVo customVo = feeMap.get(str);
            feeListCustom.add(customVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("areaOfCustom", areaListCustom);
        map.put("feeOfCustom", feeListCustom);
        map.put(vo.getName(), vo.getValue());
        String json = JSONUtils.toJson(map);

        return json;
    }

}
