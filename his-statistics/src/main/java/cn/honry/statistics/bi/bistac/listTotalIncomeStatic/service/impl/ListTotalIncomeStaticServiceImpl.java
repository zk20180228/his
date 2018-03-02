package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.dao.ListTotalIncomeStaticDao;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service.ListTotalIncomeStaticService;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.statistics.util.echartsVo.PieVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

@Service("listTotalIncomeStaticService")
@Transactional
@SuppressWarnings({"all"})
public class ListTotalIncomeStaticServiceImpl implements ListTotalIncomeStaticService {
    private Logger logger = Logger.getLogger(ListTotalIncomeStaticServiceImpl.class);
    static SimpleDateFormat sdFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Calendar ca = Calendar.getInstance();
    @Autowired
    @Qualifier(value = "listTotalIncomeStaticDao")
    private ListTotalIncomeStaticDao listTotalIncomeStaticDao;
    private final static MongoBasicDao mongoBasicDao = new MongoBasicDao();
    /**
     * 参数管理接口
     **/
    @Autowired
    @Qualifier(value = "parameterInnerDAO")
    private ParameterInnerDAO parameterInnerDAO;
    private MongoBasicDao mbDao = null;

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
     * 总收入情况统计
     *
     * @Author: huzhenguo
     * @CreateDate: 2017年5月4日 下午4:09:43
     * @Modifier: huzhenguo
     * @ModifyDate: 2017年5月4日 下午4:09:43
     * @ModifyRmk:
     * @version: V1.0
     */
    @Override
    public ListTotalIncomeStaticVo queryVo(String date) {
        //获取当前表最大时间及最小时间

        ListTotalIncomeStaticVo vo1 = listTotalIncomeStaticDao.findFeeMaxMin1();
        ListTotalIncomeStaticVo vo2 = listTotalIncomeStaticDao.findFeeMaxMin2();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> tnL1 = new ArrayList<String>();
        List<String> tnL2 = new ArrayList<String>();
        List<String> tnLs = new ArrayList<String>();
        try {
            Date sTime = format.parse(date);
            Date eTime = format.parse(date);
            tnL1 = new ArrayList<String>();
            tnL2 = new ArrayList<String>();
            //判断查询类型
            if (DateUtils.compareDate(sTime, vo1.getsTime1()) == -1) {
                if (DateUtils.compareDate(eTime, vo1.getsTime1()) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                    //获取需要查询的全部分区
                    tnL1 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", date, date);
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                tnL1.add("T_OUTPATIENT_FEEDETAIL_NOW");
            }
            //判断查询类型
            if (DateUtils.compareDate(sTime, vo2.getsTime2()) == -1) {
                if (DateUtils.compareDate(eTime, vo2.getsTime2()) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                    //获取需要查询的全部分区
                    tnL2 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", date, date);
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                tnL2.add("T_INPATIENT_FEEINFO_NOW");
            }
            tnLs.add(tnL1.get(0));
            tnLs.add(tnL2.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listTotalIncomeStaticDao.queryVo(tnLs, date);
    }

    @Override
    public ListTotalIncomeStaticVo queryVoYear(String startTime, String endTime) {
        List<String> tnL = null;
        try {
            //1.时间转换
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);
            //2.获取住院数据保留时间
            String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
            if (dateNum.equals("1")) {
                dateNum = "30";
            }
            //3.获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));
            //4.获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            tnL = new ArrayList<String>();
            //判断查询类型
            if (DateUtils.compareDate(sTime, cTime) == -1) {
                if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                    //获取需要查询的全部分区
                    tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                    //获得时间差(年)
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                    //获取相差年分的分区集合，默认加1
                    tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                    tnL.add(0, "T_INPATIENT_FEEINFO_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                tnL.add("T_INPATIENT_FEEINFO_NOW");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tnL = null;
        }

        List<String> maintnl = null;
        try {
            //转换查询时间
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);
            //获取门诊数据保留时间
            String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
            //获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));
            //获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                    maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                } else {//查询主表和分区表
                    //获取时间差（年）
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                    //获取相差年份的分区集合
                    maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                    maintnl.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");

            }
        } catch (Exception e) {
            e.printStackTrace();
            maintnl = new ArrayList<String>();
        }
        return null;
    }

    @Override
    public List<MinfeeStatCode> queryFeeName() {
        return listTotalIncomeStaticDao.queryFeeName();
    }

    @Override
    public List<BusinessDictionary> queryAreaName() {
        return innerCodeDao.querybkackList("hospitalArea");
    }

    /**
     * 分区查例子
     */
    @Override
    public List<ListTotalIncomeStaticVo> queryTotalCount(String startTime, String endTime, String dateSign) {
        String tableName;
        if ("1".equals(dateSign)) {
            tableName = "YZJEZSRQKTJYEAR";
        } else if ("2".equals(dateSign)) {
            tableName = "YZJEZSRQKTJMONTH";
        } else {
            tableName = "YZJEZSRQKTJDAY";
        }
        mbDao = new MongoBasicDao();
        boolean sign = mbDao.isCollection(tableName);
        if (sign) {
            List<ListTotalIncomeStaticVo> list = listTotalIncomeStaticDao.queryTotalCount(startTime, endTime, dateSign);
            if (list.size() > 0) {
                return list;
            }
            return new ArrayList<ListTotalIncomeStaticVo>();
        }
        List<String> tnL = null;
        Date sTime = DateUtils.parseDateY_M_D(startTime);
        Date eTime = DateUtils.parseDateY_M_D(endTime);
        try {
            //2.获取住院数据保留时间
            String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
            if (dateNum.equals("1")) {
                dateNum = "30";
            }
            //3.获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));
            //4.获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            tnL = new ArrayList<String>();
            //判断查询类型
            if (DateUtils.compareDate(sTime, cTime) == -1) {
                if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                    //获取需要查询的全部分区
                    tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                    //获得时间差(年)
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                    //获取相差年分的分区集合，默认加1
                    tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                    tnL.add(0, "T_INPATIENT_FEEINFO_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                tnL.add("T_INPATIENT_FEEINFO_NOW");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tnL = new ArrayList<String>();
        }

        List<String> maintnl = null;
        try {
            //获取门诊数据保留时间
            String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
            //获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));
            //获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            maintnl = new ArrayList<String>();
            if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                    maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                } else {//查询主表和分区表
                    //获取时间差（年）
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                    //获取相差年份的分区集合
                    maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                    maintnl.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");

            }
        } catch (Exception e) {
            e.printStackTrace();
            maintnl = new ArrayList<String>();
        }
        return listTotalIncomeStaticDao.queryTotalIncom(tnL, maintnl, startTime, endTime);
    }

    /**
     * @param date     日期 dateSign为4时，格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"
     * @param dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
     * @throws Exception
     * @Description： 总收入情况统计 elasticsearch实现
     * @Author：朱振坤
     */
    @Override
    public Map<String, Object> queryTotalCountByES(String date, String dateSign) throws Exception {
        Map<String, Object> map = new HashMap<>(3);
        DateRangeBuilder dateRangeBuilder = AggregationBuilders.dateRange("date").field("fee_date");
        if ("1".equals(dateSign)) {
            Date thisDayOfThisYear = DateUtils.parseDateY_M_D(date);//当天0点
            Date nextDayOfThisYear = DateUtils.addDay(thisDayOfThisYear, 1);//当天的下一天0点
            dateRangeBuilder.addRange("day", thisDayOfThisYear, nextDayOfThisYear);
        } else if ("2".equals(dateSign)) {
            Date thisMonthOfThisYear = DateUtils.parseDateY_M(date);//当月1日0点
            Date nextMonthOfThisYear = DateUtils.addMonth(thisMonthOfThisYear, 1);//下月1日0点
            dateRangeBuilder.addRange("month", thisMonthOfThisYear, nextMonthOfThisYear);
        } else if ("3".equals(dateSign)) {
            Date thisYear = DateUtils.parseDateY(date);//当年1月1日0点
            Date nextYear = DateUtils.addYear(thisYear, 1);//当年的下一年1月1日0点
            dateRangeBuilder.addRange("year", thisYear, nextYear);
        } else if ("4".equals(dateSign)) {
            String[] dates = date.split(",");
            Date customStartDate = DateUtils.parseDateY_M_D(dates[0]);//自定义开始日期
            Date nextDayOfcustomsEndDate = DateUtils.addDay(DateUtils.parseDateY_M_D(dates[1]), 1);//自定义结束日期的下一天0点
            dateRangeBuilder.addRange("custom", customStartDate, nextDayOfcustomsEndDate);
        } else {
            throw new IllegalArgumentException("dateSign参数无效！");
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.typeQuery(inpatient_list_type))
                .should(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.typeQuery(outpatient_feedetail_type))
                        .filter(QueryBuilders.termQuery("pay_flag", 1)));
        SearchResponse searchResponse = client.prepareSearch(inpatient_list_index, outpatient_feedetail_index)
                .setTypes(inpatient_list_type, outpatient_feedetail_type).setQuery(boolQuery)
                .addAggregation(dateRangeBuilder
                        .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
                        .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                                .order(Terms.Order.term(true))
                                .subAggregation(AggregationBuilders.terms("feeStatName").field("fee_stat_name").size(0))
                                .subAggregation(AggregationBuilders.sum("totCostByFee").field("tot_cost")))
                        .subAggregation(AggregationBuilders.terms("areaCode").field("area_code").size(0)
                                .order(Terms.Order.term(true))
                                .subAggregation(AggregationBuilders.sum("totCostByArea").field("tot_cost"))))
                .setSize(0).execute().actionGet();
        logger.info("运营分析_总收入情况统计查询用时：" + searchResponse.getTookInMillis() + "ms,  分片个数：" + searchResponse.getTotalShards());
        Range rangeDates = searchResponse.getAggregations().get("date");
        for (Range.Bucket rangeDate : rangeDates.getBuckets()) {
            List<PieVo> feeList = new ArrayList<PieVo>();
            List<PieVo> areaList = new ArrayList<PieVo>();
            Sum totCostSum = rangeDate.getAggregations().get("totCost");
            DateTime fromAsDate = (DateTime) rangeDate.getFrom();
            DateTime toAsDate = (DateTime) rangeDate.getTo();
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
        return map;
    }

    @Override
    public boolean initTotalMongYear() {
        boolean flag = true;
        listTotalIncomeStaticDao.initTotalMongYear();
        listTotalIncomeStaticDao.initTotalMongMonth();
        listTotalIncomeStaticDao.initTotalMongDate();
        return false;
    }

    @Override
    public boolean initTotalMongYearOneDay() {
        boolean flag = true;
        flag = listTotalIncomeStaticDao.initTotalMongYearOneDay();
        flag = listTotalIncomeStaticDao.initTotalMongMonthOneDay();
        flag = listTotalIncomeStaticDao.initTotalMongDateOneDay();
        return flag;
    }

    @Override
    public boolean initTotalMongTotalWithDept() {
        boolean flag = true;
        flag = listTotalIncomeStaticDao.initTotalMongYearWithDept();
        flag = listTotalIncomeStaticDao.initTotalMongMonthWithDept();
        flag = listTotalIncomeStaticDao.initTotalMongDateWithDept();
        return flag;
    }

    @Override
    public boolean initTotalMongYearOneDayWithDept() {
        boolean flag = true;
        flag = listTotalIncomeStaticDao.initTotalMongYearOneDayToApp();
        flag = listTotalIncomeStaticDao.initTotalMongMonthOneDayToAPP();
        flag = listTotalIncomeStaticDao.initTotalMongDateOneDayTOAPP();
        return flag;
    }

    @Override
    public boolean initTotalMongTotalWithDeptTotal() {
        boolean flag = true;
        flag = listTotalIncomeStaticDao.initTotalMongYearWithDeptTotal();
        flag = listTotalIncomeStaticDao.initTotalMongMonthWithDeptTotal();
        flag = listTotalIncomeStaticDao.initTotalMongDateWithDeptTotal();
        listTotalIncomeStaticDao.initDoctTotal();
        return flag;
    }

    @Override
    public void initTotalDeptDoctor() {
        listTotalIncomeStaticDao.initDeptTotal();


    }

    /**
     * 按天更新mongoDB
     ******************************************************************************************************/
    @Override
    public void initTotalForOracle(String startTime, String endTime) {
        //时间分解
        boolean sign = betweenDateSign(startTime, endTime);//判断如果是当天的 然后两个小时的
        Integer hours = null;
        Map<String, String> map;
        if (!sign) {
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        } else {
            hours = 1;
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        }
        //循环时间段
        for (String dateArr : map.keySet()) {
            startTime = dateArr;//查询的时间段
            endTime = map.get(dateArr);//查询的时间段
            List<String> tnL = null;
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);
            try {
                //2.获取住院数据保留时间
                String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
                if ("1".equals(dateNum)) {
                    dateNum = "30";
                }
                //3.获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //4.获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                tnL = new ArrayList<String>();
                //判断查询类型
                if (DateUtils.compareDate(sTime, cTime) == -1) {
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                        //获取需要查询的全部分区
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                    } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                        //获得时间差(年)
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年分的分区集合，默认加1
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                        tnL.add(0, "T_INPATIENT_FEEINFO_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    tnL.add("T_INPATIENT_FEEINFO_NOW");
                }
            } catch (Exception e) {
                e.printStackTrace();
                tnL = new ArrayList<String>();
            }

            List<String> maintnl = null;
            try {
                //获取门诊数据保留时间
                String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
                //获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                maintnl = new ArrayList<String>();
                if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                    } else {//查询主表和分区表
                        //获取时间差（年）
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年份的分区集合
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                        maintnl.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");

                }
            } catch (Exception e) {
                maintnl = new ArrayList<String>();
            }
            listTotalIncomeStaticDao.initToDBByDayOrHours(tnL, maintnl, startTime, endTime, hours);

        }

    }

    /**
     * @param begin    yyyy  yyyy-MM yyyy-MM-dd
     * @param dateSign 1 2 3
     * @param type     TB HB TB没有年同比
     * @return
     */
    public static Map<String, String> queryBetweenTime(String begin, String dateSign, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new HashMap<String, String>();
        String key;
        if ("TB".equals(type)) {
            if ("2".equals(dateSign)) {
                String[] date = begin.split("-");
                Calendar ca = Calendar.getInstance();
                ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, 1, 0, 0, 0);
                for (int i = 0; i < 6; i++) {
                    key = sdf.format(ca.getTime());
                    ca.add(Calendar.MONTH, 1);
                    map.put(key, sdf.format(ca.getTime()));
                    ca.add(Calendar.MONTH, -1);
                    ca.add(Calendar.YEAR, -1);
                }
            } else if ("3".equals(dateSign)) {
                String[] date = begin.split("-");
                Calendar ca = Calendar.getInstance();
                ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), 0, 0, 0);
                for (int i = 0; i < 6; i++) {
                    key = sdf.format(ca.getTime());
                    ca.add(Calendar.DATE, 1);
                    map.put(key, sdf.format(ca.getTime()));
                    ca.add(Calendar.DATE, -1);
                    ca.add(Calendar.YEAR, -1);
                }
            }
            return map;
        } else if ("HB".equals(type)) {
            if ("1".equals(dateSign)) {
                String[] date = begin.split("-");
                Calendar ca = Calendar.getInstance();
                ca.set(Integer.parseInt(date[0]), 0, 1, 0, 0, 0);
                for (int i = 0; i < 6; i++) {
                    key = sdf.format(ca.getTime());
                    ca.add(Calendar.YEAR, 1);
                    map.put(key, sdf.format(ca.getTime()));
                    ca.add(Calendar.YEAR, -2);
                }
            } else if ("2".equals(dateSign)) {
                String[] date = begin.split("-");
                Calendar ca = Calendar.getInstance();
                ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, 1, 0, 0, 0);
                for (int i = 0; i < 6; i++) {
                    key = sdf.format(ca.getTime());
                    ca.add(Calendar.MONTH, 1);
                    ca.add(Calendar.DATE, 0);
                    map.put(key, sdf.format(ca.getTime()));
                    ca.add(Calendar.MONTH, -2);
                }
            } else if ("3".equals(dateSign)) {
                String[] date = begin.split("-");
                Calendar ca = Calendar.getInstance();
                ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), 0, 0, 0);
                for (int i = 0; i < 6; i++) {
                    key = sdf.format(ca.getTime());
                    ca.add(Calendar.DATE, 1);
                    map.put(key, sdf.format(ca.getTime()));
                    ca.add(Calendar.DATE, -2);
                }
            }
            return map;
        }
        return new HashMap<String, String>();
    }

    public static Map<String, String> queryBetweenTime(String begin, String end, Integer hours, Map<String, String> map) {
        try {
            if (StringUtils.isNotBlank(begin)) {
                Date date = sdFull.parse(begin);//开始时间
                String[] dateone = begin.split(" ");
                String[] dateArr = dateone[0].split("-");
                String[] dateArr1 = dateone[1].split(":");
                Date date1 = sdFull.parse(end);//结束时间
                if (date.getTime() >= date1.getTime()) {//如果开始时间大于结束时间  结束
                    return map;
                } else {
                    ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]) - 1, Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr1[0]), Integer.parseInt(dateArr1[1]), Integer.parseInt(dateArr1[2]));
                    if (hours == null) {//小时为空 按天查
                        String key = sdFull.format(ca.getTime());
                        ca.add(Calendar.DATE, 1);//开始时间加1天
                        begin = sdFull.format(ca.getTime());
                        map.put(key, begin);
                    } else {//按小时查
                        String key = sdFull.format(ca.getTime());
                        ca.add(Calendar.HOUR, hours);
                        begin = sdFull.format(ca.getTime());
                        map.put(key, begin);
                    }
                    return queryBetweenTime(begin, end, hours, map);
                }
            }
        } catch (ParseException e) {
        }//开始时间
        return map;
    }

    public static boolean betweenDateSign(String begin, String end) {
        boolean sign = false;
        try {
            if (StringUtils.isNotBlank(begin)) {


                Date date = sdFull.parse(begin);
                String[] dateone = begin.split(" ");
                String[] dateArr = dateone[0].split("-");
                String[] dateArr1 = dateone[1].split(":");
                Date date1 = sdFull.parse(end);//结束时间
                ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]) - 1, Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr1[0]), Integer.parseInt(dateArr1[1]), Integer.parseInt(dateArr1[2]));
                ca.add(Calendar.HOUR, 2);
                Calendar ca1 = Calendar.getInstance();
                ca1.set(Calendar.HOUR, 0);
                ca1.set(Calendar.MINUTE, 0);
                ca1.set(Calendar.SECOND, 0);
                if (ca.getTime().getTime() >= date1.getTime() && ca.getTime().getTime() > ca1.getTime().getTime()) {
                    sign = true;
                }
            }
        } catch (ParseException e) {
        }

        return sign;

    }

    @Override
    public void initDeptTotalByDayOrHours(String startTime, String endTime) {
        boolean sign = betweenDateSign(startTime, endTime);
        Map<String, String> map;
        Integer hours = null;
        if (!sign) {
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        } else {
            hours = 1;
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        }

        //循环时间段
        for (String dateArr : map.keySet()) {
            startTime = dateArr;//查询的时间段
            endTime = map.get(dateArr);//查询的时间段
            List<String> tnL = null;
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);
            String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
            if ("1".equals(dateNum)) {
                dateNum = "30";
            }
            try {
                //2.获取住院数据保留时间

                //3.获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //4.获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                tnL = new ArrayList<String>();
                //判断查询类型
                if (DateUtils.compareDate(sTime, cTime) == -1) {
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                        //获取需要查询的全部分区
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_MEDICINELIST", startTime, endTime);
                    } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                        //获得时间差(年)
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年分的分区集合，默认加1
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_MEDICINELIST", yNum + 1);
                        tnL.add(0, "T_INPATIENT_MEDICINELIST_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    tnL.add("T_INPATIENT_MEDICINELIST_NOW");
                }

            } catch (Exception e) {
                e.printStackTrace();
                tnL = new ArrayList<String>();
            }

            List<String> maintnl = null;
            try {
                //获取门诊数据保留时间
                //获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                maintnl = new ArrayList<String>();
                if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_ITEMLIST", startTime, endTime);
                    } else {//查询主表和分区表
                        //获取时间差（年）
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年份的分区集合
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_ITEMLIST", yNum + 1);
                        maintnl.add(0, "T_INPATIENT_ITEMLIST_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    maintnl.add("T_INPATIENT_ITEMLIST_NOW");

                }
            } catch (Exception e) {
                maintnl = new ArrayList<String>();
            }
            listTotalIncomeStaticDao.initDeptTotalByDayOrHours(tnL, maintnl, startTime, endTime, hours);

        }

    }

    @Override
    public void initDoctTotalByDayOrHours(String startTime, String endTime) {
        boolean sign = betweenDateSign(startTime, endTime);
        Map<String, String> map;
        Integer hours = null;
        if (!sign) {
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        } else {
            hours = 1;
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        }
        //循环时间段
        for (String dateArr : map.keySet()) {
            startTime = dateArr;//查询的时间段
            endTime = map.get(dateArr);//查询的时间段
            List<String> tnL = null;
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);
            String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
            if ("1".equals(dateNum)) {
                dateNum = "30";
            }
            try {
                //3.获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //4.获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                tnL = new ArrayList<String>();
                //判断查询类型
                if (DateUtils.compareDate(sTime, cTime) == -1) {
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                        //获取需要查询的全部分区
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_MEDICINELIST", startTime, endTime);
                    } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                        //获得时间差(年)
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年分的分区集合，默认加1
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_MEDICINELIST", yNum + 1);
                        tnL.add(0, "T_INPATIENT_MEDICINELIST_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    tnL.add("T_INPATIENT_MEDICINELIST_NOW");
                }

            } catch (Exception e) {
                e.printStackTrace();
                tnL = new ArrayList<String>();
            }

            List<String> maintnl = null;
            try {
                //获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                maintnl = new ArrayList<String>();
                if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_ITEMLIST", startTime, endTime);
                    } else {//查询主表和分区表
                        //获取时间差（年）
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年份的分区集合
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_ITEMLIST", yNum + 1);
                        maintnl.add(0, "T_INPATIENT_ITEMLIST_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    maintnl.add("T_INPATIENT_ITEMLIST_NOW");

                }
            } catch (Exception e) {
                maintnl = new ArrayList<String>();
            }
            listTotalIncomeStaticDao.initDoctTotalByDayOrHours(tnL, maintnl, startTime, endTime, hours);
        }

    }

    @Override
    public void initTotalWithDateToAPP(String startTime, String endTime) {
        boolean sign = betweenDateSign(startTime, endTime);
        Map<String, String> map;
        Integer hours = null;
        if (!sign) {
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        } else {
            hours = 1;
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        }
        //循环时间段
        for (String dateArr : map.keySet()) {
            startTime = dateArr;//查询的时间段
            endTime = map.get(dateArr);//查询的时间段
            List<String> tnL = null;
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);
            try {
                String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);

                //3.获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //4.获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                tnL = new ArrayList<String>();
                //判断查询类型
                if (DateUtils.compareDate(sTime, cTime) == -1) {
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                        //获取需要查询的全部分区
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                    } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                        //获得时间差(年)
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年分的分区集合，默认加1
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                        tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
                }

            } catch (Exception e) {
                e.printStackTrace();
                tnL = new ArrayList<String>();
            }

            List<String> maintnl = null;
            try {
                String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
                if ("1".equals(dateNum)) {
                    dateNum = "30";
                }
                //获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                maintnl = new ArrayList<String>();
                if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                    } else {//查询主表和分区表
                        //获取时间差（年）
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年份的分区集合
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                        maintnl.add(0, "T_INPATIENT_FEEINFO_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    maintnl.add("T_INPATIENT_FEEINFO_NOW");

                }
            } catch (Exception e) {
                maintnl = new ArrayList<String>();
            }
            listTotalIncomeStaticDao.initTotalWithDateToAPP(tnL, maintnl, startTime, endTime, hours);
        }

    }

    @Override
    public void initMZZYTotalByDayOrHours(String startTime, String endTime) {
        boolean sign = betweenDateSign(startTime, endTime);
        Map<String, String> map;
        Integer hours = null;
        if (!sign) {
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        } else {
            hours = 1;
            map = queryBetweenTime(startTime, endTime, hours, new HashMap<String, String>());
        }
        //循环时间段
        for (String dateArr : map.keySet()) {
            startTime = dateArr;//查询的时间段
            endTime = map.get(dateArr);//查询的时间段
            List<String> tnL = null;
            Date sTime = DateUtils.parseDateY_M_D(startTime);
            Date eTime = DateUtils.parseDateY_M_D(endTime);

            try {
                String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);

                //3.获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //4.获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                tnL = new ArrayList<String>();
                //判断查询类型
                if (DateUtils.compareDate(sTime, cTime) == -1) {
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                        //获取需要查询的全部分区
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                    } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                        //获得时间差(年)
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年分的分区集合，默认加1
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                        tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
                }

            } catch (Exception e) {
                e.printStackTrace();
                tnL = new ArrayList<String>();
            }

            List<String> maintnl = null;
            try {
                String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
                if (dateNum.equals("1")) {
                    dateNum = "30";
                }
                //获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));
                //获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                maintnl = new ArrayList<String>();
                if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                    } else {//查询主表和分区表
                        //获取时间差（年）
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                        //获取相差年份的分区集合
                        maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                        maintnl.add(0, "T_INPATIENT_FEEINFO_NOW");
                    }
                } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                    maintnl.add("T_INPATIENT_FEEINFO_NOW");
                }
            } catch (Exception e) {
                maintnl = new ArrayList<String>();
            }
            listTotalIncomeStaticDao.initMZZYTotalByDayOrHours(tnL, maintnl, startTime, endTime, hours);
        }

    }

    /****************************月*******************************************************************************************************/
    @Override
    public void initMonth(String begin, String end) {
        listTotalIncomeStaticDao.initToDBByMonth(begin, end);
        listTotalIncomeStaticDao.initDeptTotalByDayOrHours(begin, end);
        listTotalIncomeStaticDao.initDoctTotalByDayOrHours(begin, end);
        listTotalIncomeStaticDao.initMZZYTotalByDayOrHours(begin, end);
        listTotalIncomeStaticDao.initTotalWithDateToAPP(begin, end);

    }

    /***************************年***********************************************************************************************************/
    @Override
    public void initYear(String begin, String end) {
        listTotalIncomeStaticDao.initToDBByYear(begin, end);
        listTotalIncomeStaticDao.initDeptTotalByDB(begin, end);
        listTotalIncomeStaticDao.initDoctTotalByDB(begin, end);
        listTotalIncomeStaticDao.initMZZYTotalYear(begin, end);
        listTotalIncomeStaticDao.initTotalYear(begin, end);
    }

    /*********************echars接口*************************************************************************************************************************/
    @Override
    public List<Dashboard> queryTotalCountMZZY(String startTime, String dateSign) {
        if (null == startTime && "".equals(startTime) && null == dateSign && "".equals(dateSign)) {
            return new ArrayList<Dashboard>(0);
        }
        String tableName;
        if ("1".equals(dateSign)) {
            tableName = "YZJEZSRQKTJYEARFORAPPMZZY";
        } else if ("2".equals(dateSign)) {
            tableName = "YZJEZSRQKTJMONTHFORAPPMZZY";
        } else {
            tableName = "YZJEZSRQKTJDAYFORAPPMZZY";
        }
        mbDao = new MongoBasicDao();
        boolean sign = mbDao.isCollection(tableName);
        if (sign) {
            List<Dashboard> list = listTotalIncomeStaticDao.queryTotalCountMZZY(startTime, dateSign);
            if (list.size() > 0) {
                return list;
            }
            return new ArrayList<Dashboard>();
        }
        List<String> list1 = this.returnBeginAndEnd(startTime, dateSign);
        List<String> tnL = null;
        startTime = list1.get(0);
        String endTime = list1.get(1);
        Date sTime = DateUtils.parseDateY_M_D(startTime);
        Date eTime = DateUtils.parseDateY_M_D(endTime);

        try {
            String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
            //3.获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));
            //4.获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            tnL = new ArrayList<String>();
            //判断查询类型
            if (DateUtils.compareDate(sTime, cTime) == -1) {
                if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                    //获取需要查询的全部分区
                    tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                    //获得时间差(年)
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                    //获取相差年分的分区集合，默认加1
                    tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                    tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
            }

        } catch (Exception e) {
            e.printStackTrace();
            tnL = new ArrayList<String>();
        }

        List<String> maintnl = null;
        try {

            String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
            if (dateNum.equals("1")) {
                dateNum = "30";
            }
            //获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));
            //获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            maintnl = new ArrayList<String>();
            if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                    maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                } else {//查询主表和分区表
                    //获取时间差（年）
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                    //获取相差年份的分区集合
                    maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                    maintnl.add(0, "T_INPATIENT_FEEINFO_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                maintnl.add("T_INPATIENT_FEEINFO_NOW");

            }
        } catch (Exception e) {
            maintnl = new ArrayList<String>();
        }
        return listTotalIncomeStaticDao.queryForOracleMZZY(tnL, maintnl, startTime, endTime, dateSign);

    }


    public List<String> returnBeginAndEnd(String begin, String dateSign) {

        List<String> list = new ArrayList<String>(2);
        if ("1".equals(dateSign)) {
            list.add(begin + "-01-01 00:00:00");
            list.add(begin + "-12-31 23:59:59");
        } else if ("2".equals(dateSign)) {
            list.add(begin + "-01 00:00:00");
            list.add(begin + "-31 23:59:59");
        } else {
            list.add(begin + " 00:00:00");
            list.add(begin + " 23:59:59");
        }
        return list;
    }

    @Override
    public List<Dashboard> queryTotalCount(String begin, String dateSign) {
        String tableName;
        if (null == begin || null == dateSign) {
            return new ArrayList<Dashboard>();
        }
        int len = begin.length();
        if ("2".equals(dateSign)) {
            if (len != 7) {
                return new ArrayList<Dashboard>();
            }
            tableName = "YZJEZSRQKTJMONTHFORAPP";
        } else {
            if (len != 10) {
                return new ArrayList<Dashboard>();
            }
            tableName = "YZJEZSRQKTJDAYFORAPP";
        }
        mbDao = new MongoBasicDao();
        boolean sign = mbDao.isCollection(tableName);
        List<Dashboard> list = new ArrayList<Dashboard>();
        if (sign) {
            list = listTotalIncomeStaticDao.queryTotalCountSame(begin, dateSign);
            if (list.size() > 0) {
                return list;
            }
            return new ArrayList<Dashboard>();
        } else {
            Map<String, String> dateArr = queryBetweenTime(begin, dateSign, "TB");
            for (String key : dateArr.keySet()) {
                String startTime = key;//查询的时间段
                String endTime = dateArr.get(key);//查询的时间段
                List<String> tnL = null;
                Date sTime = DateUtils.parseDateY_M_D(startTime);
                Date eTime = DateUtils.parseDateY_M_D(endTime);
                try {
                    String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
                    //3.获得当前时间
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dTime = df.parse(df.format(new Date()));
                    //4.获得在线库数据应保留最小时间
                    Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                    tnL = new ArrayList<String>();
                    //判断查询类型
                    if (DateUtils.compareDate(sTime, cTime) == -1) {
                        if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                            //获取需要查询的全部分区
                            tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                        } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                            //获得时间差(年)
                            int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                            //获取相差年分的分区集合，默认加1
                            tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                            tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                        }
                    } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                        tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    tnL = new ArrayList<String>();
                }

                List<String> maintnl = null;
                try {
                    String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
                    if ("1".equals(dateNum)) {
                        dateNum = "30";
                    }
                    //获得当前时间
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dTime = df.parse(df.format(new Date()));
                    //获得在线库数据应保留最小时间
                    Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                    maintnl = new ArrayList<String>();
                    if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                        if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                            maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                        } else {//查询主表和分区表
                            //获取时间差（年）
                            int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                            //获取相差年份的分区集合
                            maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                            maintnl.add(0, "T_INPATIENT_FEEINFO_NOW");
                        }
                    } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                        maintnl.add("T_INPATIENT_FEEINFO_NOW");

                    }
                } catch (Exception e) {
                    maintnl = new ArrayList<String>();
                }
                list.add(listTotalIncomeStaticDao.queryTotalForOracle(tnL, maintnl, startTime, endTime, dateSign));


            }
        }
        return list;
    }

    @Override
    public List<Dashboard> queryTotalCountHB(String begin, String dateSign) {
        String tableName;
        if (null == begin || null == dateSign) {
            return new ArrayList<Dashboard>();
        }
        int len = begin.length();
        if ("1".equals(dateSign)) {
            if (len != 4) {
                return new ArrayList<Dashboard>();
            }
            tableName = "YZJEZSRQKTJYEARFORAPP";
        } else if ("2".equals(dateSign)) {
            if (len != 7) {
                return new ArrayList<Dashboard>();
            }
            tableName = "YZJEZSRQKTJMONTHFORAPP";
        } else {
            if (len != 10) {
                return new ArrayList<Dashboard>();
            }
            tableName = "YZJEZSRQKTJDAYFORAPP";
        }
        mbDao = new MongoBasicDao();
        boolean sign = mbDao.isCollection(tableName);
        List<Dashboard> list = new ArrayList<Dashboard>();
        if (sign) {
            list = listTotalIncomeStaticDao.queryTotalCountSque(begin, dateSign);
            if (list.size() > 0) {
                return list;
            }
            return new ArrayList<Dashboard>();
        } else {
            Map<String, String> dateArr = queryBetweenTime(begin, dateSign, "HB");
            for (String key : dateArr.keySet()) {
                String startTime = key;//查询的时间段
                String endTime = dateArr.get(key);//查询的时间段
                List<String> tnL = null;
                Date sTime = DateUtils.parseDateY_M_D(startTime);
                Date eTime = DateUtils.parseDateY_M_D(endTime);
                try {
                    String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
                    //3.获得当前时间
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dTime = df.parse(df.format(new Date()));
                    //4.获得在线库数据应保留最小时间
                    Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                    tnL = new ArrayList<String>();
                    //判断查询类型
                    if (DateUtils.compareDate(sTime, cTime) == -1) {
                        if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
                            //获取需要查询的全部分区
                            tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", startTime, endTime);
                        } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
                            //获得时间差(年)
                            int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                            //获取相差年分的分区集合，默认加1
                            tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                            tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                        }
                    } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                        tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    tnL = new ArrayList<String>();
                }

                List<String> maintnl = null;
                try {
                    String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
                    if ("1".equals(dateNum)) {
                        dateNum = "30";
                    }
                    //获得当前时间
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dTime = df.parse(df.format(new Date()));
                    //获得在线库数据应保留最小时间
                    Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                    maintnl = new ArrayList<String>();
                    if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                        if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                            maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", startTime, endTime);
                        } else {//查询主表和分区表
                            //获取时间差（年）
                            int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
                            //获取相差年份的分区集合
                            maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_FEEINFO", yNum + 1);
                            maintnl.add(0, "T_INPATIENT_FEEINFO_NOW");
                        }
                    } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                        maintnl.add("T_INPATIENT_FEEINFO_NOW");

                    }
                } catch (Exception e) {
                    maintnl = new ArrayList<String>();
                }
                list.add(listTotalIncomeStaticDao.queryTotalForOracle(tnL, maintnl, startTime, endTime, dateSign));
            }
        }
        return list;
    }

    /**
     * 总收入情况统计--从mongodb中获取数据
     *
     * @param date     日期
     * @param dateSign 日期类型
     * @return
     */
    @Override
    public String queryTotalCountByMongo(String date, String dateSign) {
        if ("4".equals(dateSign)) {
            return this.queryCostom(date);
        }
        String key = "ZSRQKTJ_";
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
        mbDao = new MongoBasicDao();
        DBCursor cursor = mbDao.findAlldata(key, where);
        while (cursor.hasNext()) {
            s = cursor.next().get("value").toString();
        }
        if (StringUtils.isBlank(s)) {
            Map<String, Object> map = new HashMap<>();
            if ("1".equals(dateSign)) {
                map.put("dayTotCost", 0.0);
                map.put("feeOfDay", new ArrayList<String>());
                map.put("areaOfDay", new ArrayList<String>());
            } else if ("2".equals(dateSign)) {
                map.put("monthTotCost", 0.0);
                map.put("feeOfMonth", new ArrayList<String>());
                map.put("areaOfMonth", new ArrayList<String>());
            } else if ("3".equals(dateSign)) {
                map.put("yearTotCost", 0.0);
                map.put("feeOfYear", new ArrayList<String>());
                map.put("areaOfYear", new ArrayList<String>());
            } else {
                map.put("customTotCost", 0.0);
                map.put("feeOfCustom", new ArrayList<String>());
                map.put("areaOfCustom", new ArrayList<String>());
            }
            s = JSONUtils.toJson(map);
        }
        return s;
    }


    /**
     * 总收入情况统计--自定义统计
     *
     * @param date 日期
     *             开始时间和结束时间之间有一整月的数据则按月查询,有整年的数据则按年查询;
     *             例如:查询 2015-03-16至2017-02-05 的数据:
     *             2015-03-16至2015-03-31之间的数据按天查询,从日表(ZSRQKTJ_DAY)中获取;
     *             2015-04-01至2015-12-31之间有整月的数据(4月、5月、6月、...、12月),从月表(ZSRQKTJ_MONTH)中获取;
     *             2016-01-01至2016-12-31之间有整年的数据(2016年),从年表(ZSRQKTJ_YEAR)中获取;
     *             2017-01-01至2017-01-31之间有整月的数据(1月),从月表(ZSRQKTJ_MONTH)中获取;
     *             2017-02-01至2017-02-05之间的数据按天查询,从日表(ZSRQKTJ_DAY)中获取.
     * @return
     */
    public String queryCostom(String date) {
        if (StringUtils.isBlank(date)) {
            return "";
        }
        String[] dates = date.split(",");
        Map<String, List<String>> dateMap = ResultUtils.getDate(dates[0], dates[1]);
        List<String> list = new ArrayList<String>();
        List<String> yearList = dateMap.get("year");//按年统计的list
        if (yearList.size() > 0) {
            for (String dateY : yearList) {
                String s = this.queryTotalCountByMongo(dateY, "3");
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }
        List<String> monthList = dateMap.get("month");//按月统计的list
        if (monthList.size() > 0) {
            for (String dateM : monthList) {
                String s = this.queryTotalCountByMongo(dateM, "2");
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }
        List<String> dayList = dateMap.get("day");//按日统计的list
        if (dayList.size() > 0) {
            for (String dateD : dayList) {
                String s = this.queryTotalCountByMongo(dateD, "1");//mongodb中的结果
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
        ArrayList<String> nameList = new ArrayList<String>();//记录费别的名字，用于保证费别list和别的的有一样的顺序
        for (CustomVo customVo : feeList) {//求各个费别对应的总金额
            CustomVo vo2 = feeMap.get(customVo.getName());
            if (vo2 != null) {
                vo2.setValue(vo2.getValue() + customVo.getValue());
            } else {
                feeMap.put(customVo.getName(), customVo);
                nameList.add(customVo.getName());//只会走完整的一边，和mongo中的顺序一致
            }
        }
        List<CustomVo> areaListCustom = new ArrayList<>();
        areaListCustom.addAll(areaMap.values());

        List<CustomVo> feeListCustom = new ArrayList<>();
        for (String n : nameList) {
            feeListCustom.add(feeMap.get(n));//按照存好的name取出来
        }
        Map<String, Object> map = new HashMap<>();
        map.put("areaOfCustom", areaListCustom);
        map.put("feeOfCustom", feeListCustom);
        map.put(vo.getName(), vo.getValue());
        String json = JSONUtils.toJson(map);
        return json;
    }


}
