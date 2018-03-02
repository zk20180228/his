package cn.honry.statistics.sys.reportForms.service.impl;

import java.text.CollationKey;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoGzltjDao;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsDao;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsjdbcDao;
import cn.honry.statistics.sys.reportForms.service.ReportFormsService;
import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.statistics.sys.reportForms.vo.PatientInfoVo;
import cn.honry.statistics.sys.reportForms.vo.ReportVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.statistics.util.echartsVo.PieVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

@Service("reportFormsService")
@Transactional
public class ReportFormsServiceImpl implements ReportFormsService {

    private Logger logger = Logger.getLogger(ReportFormsServiceImpl.class);
    @Resource
    private RedisUtil redis;
    @Autowired
    @Qualifier(value = "reportFormsDAO")
    private ReportFormsDao reportFormsDAO;
    @Autowired
    @Qualifier(value = "reportFormsjdbcDao")
    private ReportFormsjdbcDao reportFormsjdbcDao;


    /**
     * 挂号数据库操作类
     **/
    @Autowired
    @Qualifier(value = "registerInfoGzltjDAO")
    private RegisterInfoGzltjDao registerInfoGzltjDAO;
    @Autowired
    @Qualifier(value = "deptInInterService")
    private DeptInInterService deptInInterService;

    public void setDeptInInterService(DeptInInterService deptInInterService) {
        this.deptInInterService = deptInInterService;
    }

    /**
     * 参数管理接口
     **/
    @Autowired
    @Qualifier(value = "parameterInnerDAO")
    private ParameterInnerDAO parameterInnerDAO;

    /**
     * zzk
     **/
    @Resource(name = "deptInInterDAO")
    private DeptInInterDAO deptInInterDAO;

    @Resource(name = "employeeInInterDAO")
    private EmployeeInInterDAO employeeInInterDAO;

    @Resource(name = "innerCodeDao")
    private CodeInInterDAO innerCodeDao;

    @Resource(name = "client")
    private Client client;

    @Value("${es.outpatient_feedetail.index}")
    private String outpatient_feedetail_index;

    @Value("${es.outpatient_feedetail.type}")
    private String outpatient_feedetail_type;

    /**
     * @param deptw科室
     * @param stimew开始时间
     * @param etimew结束时间
     * @Description： 医生工作量统计查询
     * @Author：wujiao
     * @CreateDate：2016-5-3 下午3:12:16
     * @ModifyRmk：
     */
    @Override
    public List<DoctorWorkloadStatistics> queryReservation(String deptw, String stimew, String etimew, String menuAlias) throws Exception {

        List<DoctorWorkloadStatistics> retList = null;
        try {
        	if (StringUtils.isBlank(deptw)) {
                List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias, ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
                if (deptList != null && deptList.size() > 0 && deptList.size() < 900) {
                    deptw = "";
                    for (SysDepartment dept : deptList) {
                        if (StringUtils.isNotBlank(deptw)) {
                            deptw += ",";
                        }
                        deptw += dept.getDeptCode();
                    }
                }
            }
        	if(new MongoBasicDao().isCollection("YSGZLTJ_TOTAL_DAY")){
        		return reportFormsDAO.queryReservationMongDB(deptw, stimew, etimew);
        	}else{
        		//1.转换查询时间
                Date sTime = DateUtils.parseDateY_M_D(stimew);
                Date eTime = DateUtils.parseDateY_M_D(etimew);

                //2.获取门诊数据保留时间
                String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);

                //3.获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));

                //4.获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);

                //5.定义常量
                List<String> tnL = new ArrayList<String>();
                List<String> pretnl = new ArrayList<String>();

                //6.判断是否查询分区
                if (DateUtils.compareDate(sTime, cTime) == -1) {
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//只查询分区表
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_REGISTER_MAIN", stimew, etimew);
                        pretnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_REGISTER_PREREGISTER", stimew, etimew);
                    } else {//查询在线表及分区表

                        //获取时间差（年）
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stimew);

                        //获取相差年份的分区集合
                        tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_REGISTER_MAIN", yNum + 1);
                        tnL.add(0, "T_REGISTER_MAIN_NOW");
                        pretnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_REGISTER_PREREGISTER", yNum + 1);
                        pretnl.add(0, "T_REGISTER_PREREGISTER_NOW");
                    }
                } else {
                    //只查询在线表
                    tnL.add("T_REGISTER_MAIN_NOW");
                    pretnl.add("T_REGISTER_PREREGISTER_NOW");
                }
                retList = reportFormsDAO.queryReservation(tnL, pretnl, deptw, stimew, etimew, menuAlias);
        	}
        } catch (Exception e) {
            retList = new ArrayList<DoctorWorkloadStatistics>();
            throw new RuntimeException(e);
        }

        return retList;
    }

    /**
     * @param dept科室
     * @param stime开始时间
     * @param etime结束时间
     * @Description： 门诊住院情况统计统计查询
     * @Author：wujiao
     * @CreateDate：2016-5-3 下午3:12:16
     * @ModifyRmk：
     * @version 1.0
     */
    @Override
    public List<PatientInfoVo> queryPatientInfo(String dept, String stime, String etime, String menuType) {
        List<String> infotnl = new ArrayList<String>();
        List<String> feedetialtnl = new ArrayList<String>();
        List<String> notmednl = new ArrayList<String>();
        List<String> mednl = new ArrayList<String>();
        try {

            //转换查询时间
            Date sTime = DateUtils.parseDateY_M_D(stime);
            Date eTime = DateUtils.parseDateY_M_D(etime);

            //获取门诊数据保留时间
            String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
            String s = parameterInnerDAO.getParameterByCode("saveTime");

            //住院
            int zyNum = Integer.parseInt(s) * 30;

            //获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));

            //获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);

            //获得住院在线库数据应保留最小时间
            Date cZYTime = DateUtils.addDay(dTime, -zyNum + 1);


            if (DateUtils.compareDate(sTime, cTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                if (DateUtils.compareDate(eTime, cTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                    feedetialtnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", stime, etime);
                } else {//查询主表和分区表

                    //获取时间差（年）
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stime);

                    //获取相差年份的分区集合
                    feedetialtnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                    feedetialtnl.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）
                feedetialtnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
            }
            if (DateUtils.compareDate(sTime, cZYTime) == -1) {//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
                if (DateUtils.compareDate(eTime, cZYTime) == -1) {//当结束时间小于挂号主表中的最小时间时，只查询分区表
                    infotnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_INFO", stime, etime);
                    notmednl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_ITEMLIST", stime, etime);
                    mednl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_INPATIENT_MEDICINELIST", stime, etime);
                } else {//查询主表和分区表

                    //获取时间差（年）
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cZYTime), stime);

                    //获取相差年份的分区集合
                    infotnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_INFO", yNum + 1);
                    infotnl.add(0, "T_INPATIENT_INFO_NOW");
                    notmednl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_ITEMLIST", yNum + 1);
                    notmednl.add(0, "T_INPATIENT_ITEMLIST_NOW");
                    mednl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_INPATIENT_MEDICINELIST", yNum + 1);
                    mednl.add(0, "T_INPATIENT_MEDICINELIST_NOW");
                }
            } else {//3.只查询主表（查询的开始时间大或等于表中的最小时间）

                infotnl.add("T_INPATIENT_INFO_NOW");
                notmednl.add("T_INPATIENT_ITEMLIST_NOW");
                mednl.add("T_INPATIENT_MEDICINELIST_NOW");
            }
        } catch (Exception e) {
            infotnl = new ArrayList<String>();
            feedetialtnl = new ArrayList<String>();
            throw new RuntimeException(e);
        }

        return reportFormsDAO.queryPatientInfo(infotnl, feedetialtnl, notmednl, mednl, dept, stime, etime, menuType);

    }

    /**
     * @param dept科室
     * @param stime开始时间
     * @param etime结束时间
     * @Description： 医院各项收入统计
     * @Author：wujiao
     * @CreateDate：2016-5-12 下午3:12:16
     * @ModifyRmk：
     * @version 1.0
     */
    @Override
    public List<StatisticsVo> queryStatisticsInfo(String dept, String expxrts, String stime, String etime) {
        String invoiceDetialTabName = null;
        List<String> invoicePartitionName = new ArrayList<String>();
        List<String> feedetialPartitionName = new ArrayList<String>();
        List<StatisticsVo> getlist = null;
        try {//转换查询时间
            Date sTime = DateUtils.parseDateY_M_D(stime);
            Date eTime = DateUtils.parseDateY_M_D(etime);

            //获取门诊数据保留时间
            String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);

            //获得当前时间
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dTime = df.parse(df.format(new Date()));

            //获得在线库数据应保留最小时间
            Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
            List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code
            if (DateUtils.compareDate(sTime, cTime) == -1) {//查询历史表
                if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）

                    //获取需要查询的全部分区
                    feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", stime, etime);
                } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）

                    //获得时间差(年)
                    int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stime);

                    //获取相差年分的分区集合，默认加1
                    feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                    feedetialPartitionName.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                }
            } else {
                feedetialPartitionName.add("T_OUTPATIENT_FEEDETAIL_NOW");
            }

            getlist = reportFormsjdbcDao.getlist(feedetialPartitionName, encode, dept, expxrts, stime, etime);
        } catch (Exception e) {
            invoicePartitionName = new ArrayList<String>();
            feedetialPartitionName = new ArrayList<String>();
            throw new RuntimeException(e);

        }

        return getlist;
    }

    /**
     * @param date     日期 dateSign为4时，格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"
     * @param dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
     * @Description： 门诊收入统计 elasticsearch实现
     * @Author：朱振坤
     */
    @Override
    public Map<String, Object> queryOutpatientChartsByES(String date, String dateSign) {
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
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("pay_flag", 1));
//                .filter(QueryBuilders.termQuery("cancel_flag", 1));
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(outpatient_feedetail_index)
                .setTypes(outpatient_feedetail_type).setQuery(boolQuery)
                .addAggregation(dateRangeBuilder
                        .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
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
        return map;
    }

    /**
     * <p>门诊各项收入统计 </p>
     *
     * @param deptCodes   科室编号
     * @param expxrtCodes 医生编号
     * @param beginDate   开始时间 yyyy-MM-dd
     * @param endDate     结束时间 yyyy-MM-dd
     * @return
     * @throws Exception
     * @Author: zhangkui
     * @CreateDate: 2017-05-19 5:18:28
     * @Modifier: zhangkui
     * @ModifyDate: 2017-05-19 5:18:28
     * @ModifyRmk:
     * @version: V1.0
     * @throws:
     */
    @Override
    public List<StatisticsVo> listStatisticsQueryByMongo(String deptCodes, String expxrtCodes, String beginDate, String endDate) throws Exception {

        return reportFormsjdbcDao.listStatisticsQueryByMongo(deptCodes, expxrtCodes, beginDate, endDate);
    }

    ;

    /**
     * 门诊各项收入统计 elasticsearch实现
     *
     * @param deptCodes   科室code字符串，多个code以“,”隔开
     * @param expxrtCodes 医生code字符串，多个code以“,”隔开
     * @param beginDate   查询开始时间 以“fee_date”为查询字段
     * @param endDate     查询结束时间 包括当日
     * @param page        easyUi分页参数
     * @param rows        easyUi分页参数
     * @return 封装easyUi表格的json数据的集合
     * @Author: 朱振坤
     */
    @Override
    public List<StatisticsVo> listStatisticsQueryByES(String deptCodes, String expxrtCodes, String beginDate, String endDate) {
        List<StatisticsVo> list = new ArrayList<StatisticsVo>();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("pay_flag", 1))
//                .filter(QueryBuilders.termQuery("cancel_flag", 1))
                .filter(QueryBuilders.rangeQuery("fee_date")
                        .gte(DateUtils.parseDateY_M_D(beginDate))
                        .lt(DateUtils.addDay(DateUtils.parseDateY_M_D(endDate), 1)));
        if (StringUtils.isNotBlank(deptCodes)) {
            String[] depts = deptCodes.split(",");
            boolQuery.filter(QueryBuilders.termsQuery("reg_dpcd", depts));
        }
        if (StringUtils.isNotBlank(expxrtCodes)) {
            String[] expxrts = expxrtCodes.split(",");
            boolQuery.filter(QueryBuilders.termsQuery("doct_code", expxrts));
        }
        SearchRequestBuilder searchBuilder = client.prepareSearch(outpatient_feedetail_index)
                .setTypes(outpatient_feedetail_type).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms("regDpcd").field("reg_dpcd").size(0)
                        .subAggregation(AggregationBuilders.terms("deptName").field("dept_name").size(0))
                        .subAggregation(AggregationBuilders.terms("doctCode").field("doct_code").size(0)
                                .subAggregation(AggregationBuilders.terms("doctName").field("doct_name").size(0))
                                .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
                                .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
                                        .subAggregation(AggregationBuilders.sum("totCostByfee").field("tot_cost")))));
        SearchResponse searchResponse = searchBuilder.setSize(0).execute().actionGet();
        logger.info("门诊统计分析_门诊各项收入统计查询用时：" + searchResponse.getTookInMillis() + "ms,  分片个数：" + searchResponse.getTotalShards());
        Terms regDpcds = searchResponse.getAggregations().get("regDpcd");
        for (Terms.Bucket regDpcd : regDpcds.getBuckets()) {
            Terms doctCodes = regDpcd.getAggregations().get("doctCode");
            for (Terms.Bucket doct : doctCodes.getBuckets()) {
                StatisticsVo vo = new StatisticsVo();
                vo.setDeptCode(regDpcd.getKeyAsString());
                Terms deptNames = regDpcd.getAggregations().get("deptName");
                if (deptNames.getBuckets() != null && deptNames.getBuckets().size() > 0) {
                    vo.setDept(deptNames.getBuckets().get(0).getKeyAsString());
                }
                vo.setDocterCode(doct.getKeyAsString());
                Terms doctNames = doct.getAggregations().get("doctName");
                if (doctNames.getBuckets() != null && doctNames.getBuckets().size() > 0) {
                    vo.setName(doctNames.getBuckets().get(0).getKeyAsString());
                }
                Sum totCost = doct.getAggregations().get("totCost");
                vo.setTotle(Double.valueOf(NumberUtil.init().format(totCost.getValue(), 2)));
                Terms feeStatCodes = doct.getAggregations().get("feeStatCode");
                double drugCost = 0;
                int drugNum = 0;
                for (Terms.Bucket feeStat : feeStatCodes.getBuckets()) {
                    Sum totCostByfee = feeStat.getAggregations().get("totCostByfee");
                    if ("01".equals(feeStat.getKeyAsString()) || "02".equals(feeStat.getKeyAsString()) || "03".equals(feeStat.getKeyAsString())) {
                        drugCost += totCostByfee.getValue();
                        drugNum += feeStat.getDocCount();
                    }
                    this.setCost(vo, (int) feeStat.getDocCount(), Double.valueOf(NumberUtil.init().format(totCostByfee.getValue(), 2)), feeStat.getKeyAsString());
                }
                vo.setAllCost(Double.valueOf(NumberUtil.init().format(drugCost, 2)));
                vo.setAllNum(drugNum);
                vo.setMedicalNum((int) doct.getDocCount() - drugNum);
                if (vo.getMedicalNum() == 0) {// 防止浮点数相减造成误差
                    vo.setMedicalCost(0.0);
                } else {
                    vo.setMedicalCost(Double.valueOf(NumberUtil.init().format(totCost.value() - drugCost, 2)));
                }
                list.add(vo);
            }
        }
        Collections.sort(list, new Comparator<StatisticsVo>() {
            private Collator collator = Collator.getInstance(java.util.Locale.CHINA);

            @Override
            public int compare(StatisticsVo o1, StatisticsVo o2) {
                CollationKey key1 = collator.getCollationKey(o1.getDept());
                CollationKey key2 = collator.getCollationKey(o2.getDept());
                return key1.compareTo(key2);
            }
        });
        return list;
    }

    @Override
    public Map TBTotalIncome(String dateSign, String searchTime) throws Exception {
        List<String> list = this.getTnl(dateSign, searchTime, "TB");//获取分区表集合
        List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code

        return reportFormsjdbcDao.TBTotalIncome(dateSign, searchTime, list, encode);
    }

    @Override
    public Map HBTotalIncome(String dateSign, String searchTime) throws Exception {
        List<String> list = this.getTnl(dateSign, searchTime, "HB");//获取分区表集合
        List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code

        return reportFormsjdbcDao.HBTotalIncome(dateSign, searchTime, list, encode);
    }

    @Override
    public Map deptTopF(String dateSign, String searchTime) throws Exception {
        List<String> list = this.getTnl(dateSign, searchTime, "TOP");//获取分区表集合
        List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code

        return reportFormsjdbcDao.deptTopF(dateSign, searchTime, list, encode);
    }

    @Override
    public Map docterTopF(String dateSign, String searchTime) throws Exception {
        List<String> list = this.getTnl(dateSign, searchTime, "TOP");//获取分区表集合
        List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code

        return reportFormsjdbcDao.docterTopF(dateSign, searchTime, list, encode);
    }

    /**
     * @param dateSign
     * @param searchTime
     * @param MethodFlag TB-->同比，HB---->环比 ,top---->TOP
     * @return List<String>
     * @Description:根据标记dateSign，给定时间searchTime，返回分区表集合
     * @exception:
     * @author: zhangkui
     * @time:2017年5月26日 下午2:02:38
     */
    public List<String> getTnl(String dateSign, String searchTime, String MethodFlag) {
        String stime = null;
        String etime = null;
        //年
        if ("1".equals(dateSign)) {
            //环比
            if ("HB".equals(MethodFlag)) {
                String[] time = conYear(searchTime, dateSign);
                stime = time[time.length - 1] + "-01" + "-01";
                etime = time[0] + "-12" + "-31";
            }
            //top
            if ("TOP".equals(MethodFlag)) {
                stime = searchTime.substring(0, 4) + "-01-01";
                etime = searchTime.substring(0, 4) + "-12-31";
            }
        }
        //月
        if ("2".equals(dateSign)) {
            //同比
            if ("TB".equals(MethodFlag)) {
                String[] time = conMonth(searchTime, dateSign);
                //获取该月最后一天
                String lastDay = getLastDay(searchTime);
                stime = time[time.length - 1] + "-01";
                etime = time[0] + "-" + lastDay;
            }
            //环比
            if ("HB".equals(MethodFlag)) {
                String[] time = conYear(searchTime, dateSign);
                //获取该月最后一天
                String lastDay = getLastDay(searchTime);
                stime = time[time.length - 1] + "-01";
                etime = time[0] + "-" + lastDay;
            }
            //TOP
            if ("TOP".equals(MethodFlag)) {
                String lastDay = getLastDay(searchTime);
                stime = searchTime.substring(0, 7) + "-01";
                etime = searchTime.substring(0, 7) + "-" + lastDay;
            }

        }
        //日
        if ("3".equals(dateSign)) {
            //同比
            if ("TB".equals(MethodFlag)) {
                String[] time = conMonth(searchTime, dateSign);
                stime = time[time.length - 1];
                etime = time[0];
            }
            //环比
            if ("HB".equals(MethodFlag)) {
                String[] time = conYear(searchTime, dateSign);
                stime = time[time.length - 1];
                etime = time[0];
            }
            //TOP
            if ("TOP".equals(MethodFlag)) {
                stime = searchTime;
                etime = searchTime;
            }
        }
        List<String> feedetialPartitionName = new ArrayList<String>();
        if (stime != null && etime != null) {
            try {//转换查询时间
                Date sTime = DateUtils.parseDateY_M_D(stime);
                Date eTime = DateUtils.parseDateY_M_D(etime);

                //获取门诊数据保留时间
                String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);

                //获得当前时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date dTime = df.parse(df.format(new Date()));

                //获得在线库数据应保留最小时间
                Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
                if (DateUtils.compareDate(sTime, cTime) == -1) {//查询历史表
                    if (DateUtils.compareDate(eTime, cTime) == -1) {//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）

                        //获取需要查询的全部分区
                        feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", stime, etime);
                    } else {//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）

                        //获得时间差(年)
                        int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stime);

                        //获取相差年分的分区集合，默认加1
                        feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB, "T_OUTPATIENT_FEEDETAIL", yNum + 1);
                        feedetialPartitionName.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
                    }
                } else {
                    feedetialPartitionName.add("T_OUTPATIENT_FEEDETAIL_NOW");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return feedetialPartitionName;
    }


    /**
     * 环比，工具
     *
     * @param date     时间 yyyy-MM-dd
     * @param dateSing 年，月，日 分别是1，2，3
     * @return 返回环比的时间字符串数组:如["2017-05","2017-04","2017-03","2017-02","2017-01","2016-12"]
     */
    public String[] conYear(String date, String dateSing) {
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        String[] dateOne = date.split("-");
        String[] strArr = new String[6];
        if (dateOne.length != 3) {
            dateOne = sdf.format(new Date(System.currentTimeMillis())).split("-");
        }
        ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1]) - 1, Integer.parseInt(dateOne[2]));
        for (int i = 0; i < 6; i++) {
            if ("1".equals(dateSing)) {
                strArr[i] = sdf2.format(ca.getTime());
                ca.add(Calendar.YEAR, -1);
            } else if ("2".equals(dateSing)) {
                strArr[i] = sdf1.format(ca.getTime());
                ca.add(Calendar.MONTH, -1);
            } else {
                strArr[i] = sdf.format(ca.getTime());
                ca.add(Calendar.DATE, -1);
            }
        }

        return strArr;
    }


    /**
     * 同比，工具
     *
     * @param date     时间
     * @param dateSing
     * @return 返回同比的时间字符串数组["2017-05","2016-05","2015-05","2014-05","2013-05","2012-05"]
     */
    public String[] conMonth(String date, String dateSing) {

        String[] strArr = new String[6];
        String[] dateArr = date.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (dateArr.length != 3) {
            dateArr = sdf.format(new Date(System.currentTimeMillis())).split("-");
        }
        int dateTemp = Integer.parseInt(dateArr[0]);
        for (int i = 0; i < 6; i++) {
            if ("2".equals(dateSing)) {//月同比
                strArr[i] = (dateTemp - i) + "-" + dateArr[1];
            } else {
                strArr[i] = (dateTemp - i) + "-" + dateArr[1] + "-" + dateArr[2];
            }
        }

        return strArr;
    }

    /**
     * <p> 获取输入时间对应的月的最后一天，闰年29天，平年28天，时间格式必须是长度大于：yyyy-MM-dd的格式</p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年7月3日 下午4:54:59
     * @Modifier: zhangkui
     * @ModifyDate: 2017年7月3日 下午4:54:59
     * @ModifyRmk:
     * @version: V1.0
     * @param: date 长度大yyyy-MM-dd
     * @throws:RuntimeException
     * @return: String 月份最后一天的字符串表示形式
     */
    public String getLastDay(String date) {

        date = date.substring(0, 7);
        System.out.println(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date time = null;

        try {
            time = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        final int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = calendar.getTime();
        lastDate.setDate(lastDay);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat1.format(lastDate).substring(8, 10);

    }


    /**
     * @param vo
     * @param count
     * @param cost
     * @param code  统计费用代码
     * @return
     */
    private StatisticsVo setCost(StatisticsVo vo, int count, double cost, String code) {

        if (StringUtils.isBlank(code) || cost == 0) {
            return vo;
        }
        switch (code.trim()) {
            case "01"://西药费
                vo.setWesternNum(count);
                vo.setWesternCost(cost);
                break;
            case "02"://中成药费
                vo.setChineseNum(count);
                vo.setChineseCost(cost);
                break;
            case "03"://中草药费
                vo.setHerbalNum(count);
                vo.setHerbalCost(cost);
                break;
            case "04"://床位费
                vo.setChuangweiNum(count);
                vo.setChuangweiCost(cost);
                break;
            case "05"://治疗费
                vo.setTreatmentNum(count);
                vo.setTreatmentCost(cost);
                break;
            case "07"://检查费
                vo.setInspectNum(count);
                vo.setInspectCost(cost);
                break;
            case "08"://放射费
                vo.setRadiationNum(count);
                vo.setRadiationCost(cost);
                break;
            case "09"://化验费
                vo.setTestNum(count);
                vo.setTestCost(cost);
                break;
            case "10"://手术费
                vo.setShoushuNum(count);
                vo.setShoushuCost(cost);
                break;
            case "11"://输血费
                vo.setBloodNum(count);
                vo.setBloodCost(cost);
                break;
            case "12"://输氧费
                vo.setO2Num(count);
                vo.setO2Cost(cost);
                break;
            case "13"://材料费
                vo.setCailiaoNum(count);
                vo.setCailiaoCost(cost);
                break;
            case "14"://其他
                vo.setOtherNum(count);
                vo.setOtherCost(cost);
                break;
            case "18"://疫苗费
                vo.setYimiaoNum(count);
                vo.setYimiaoCost(cost);
                break;
            default:break;
        }
        return vo;
    }

    /**
     * 门诊收入统计--从mongodb中获取数据
     *
     * @param date     日期
     * @param dateSign 日期类型
     * @return
     */
    @Override
    public String queryOutpatientChartsByMongo(String date, String dateSign) {
        if ("4".equals(dateSign)) {
            return queryCostom(date);
        }
        String key = "SRTJB_";
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
        String s = null;
        DBCursor cursor = new MongoBasicDao().findAlldata(key, where);
        while (cursor.hasNext()) {
            s = cursor.next().get("value").toString();
        }
        if (StringUtils.isBlank(s)) {
            Map<String,Object> map = new HashMap<>(7);
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
            } else if("3".equals(dateSign)) {
                map.put("yearTotCost", 0.0);
                map.put("feeOfYear", new ArrayList<String>());
                map.put("areaOfYear", new ArrayList<String>());
                map.put("huanbiByYear", new ArrayList<String>());
                map.put("xAxisByHuanbiByYear", new ArrayList<String>());
            } else if("4".equals(dateSign)) {
                map.put("customTotCost", 0.0);
                map.put("feeOfCustom", new ArrayList<String>());
                map.put("areaOfCustom", new ArrayList<String>());
            }
            s = JSONUtils.toJson(map);
        }
        return s;
    }

    /**
     * 门诊收入统计--自定义统计
     *
     * @param date 日期
     *             开始时间和结束时间之间有一整月的数据则按月查询,有整年的数据则按年查询;
     *             例如:查询 2015-03-16至2017-02-05 的数据:
     *             2015-03-16至2015-03-31之间的数据按天查询,从日表(SRTJB_DAY)中获取;
     *             2015-04-01至2015-12-31之间有整月的数据(4月、5月、6月、...、12月),从月表(SRTJB_MONTH)中获取;
     *             2016-01-01至2016-12-31之间有整年的数据(2016年),从年表(SRTJB_YEAR)中获取;
     *             2017-01-01至2017-01-31之间有整月的数据(1月),从月表(SRTJB_MONTH)中获取;
     *             2017-02-01至2017-02-05之间的数据按天查询,从日表(SRTJB_DAY)中获取.
     * @return
     */
    public String queryCostom(String date) {

        if (StringUtils.isBlank(date)) {
            return "";
        }
        String[] dates = date.split(",");

        Map<String, List<String>> dateMap = ResultUtils.getDate(dates[0], dates[1]);
        List<String> list = new ArrayList<>();
        List<String> yearList = dateMap.get("year");//按年统计的list
        if (yearList.size() > 0) {
            for (String dateY : yearList) {
                String s = queryOutpatientChartsByMongo(dateY, "3");
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }

        List<String> monthList = dateMap.get("month");//按月统计的list
        if (monthList.size() > 0) {
            for (String dateM : monthList) {
                String s = queryOutpatientChartsByMongo(dateM, "2");
                if (StringUtils.isNotBlank(s)) {
                    list.add(s);
                }
            }
        }

        List<String> dayList = dateMap.get("day");//按日统计的list
        if (dayList.size() > 0) {
            for (String dateD : dayList) {
                String s = queryOutpatientChartsByMongo(dateD, "1");//mongodb中的结果
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
