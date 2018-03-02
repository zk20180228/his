package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service.ListTotalIncomeStaticService;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.totalDrugUsed.service.TotalDrugUsedService;
import cn.honry.statistics.bi.bistac.totalUnDrugUsed.service.TotalUnDrugUsedService;
import cn.honry.statistics.sys.reportForms.service.ReportFormsService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = {@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/statistics/listTotalIncomeStatic")
public class ListTotalIncomeStaticAction {
    private Logger logger = Logger.getLogger(ListTotalIncomeStaticAction.class);

    @Autowired
    @Qualifier(value = "hiasExceptionService")
    private HIASExceptionService hiasExceptionService;

    public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
        this.hiasExceptionService = hiasExceptionService;

    }

    /**
     * 医生工作量统计开始时间
     */
    private String sTime;

    /**
     * 医生工作量统计结束时间
     */
    private String eTime;
    @Autowired
    @Qualifier(value = "listTotalIncomeStaticService")
    private ListTotalIncomeStaticService listTotalIncomeStaticService;
    private String dateSign;
    private String date;

    public String getDate() {
        return date;
    }

    @Autowired
    @Qualifier("totalDrugUsedService")
    private TotalDrugUsedService totalDrugUsedService;
    @Autowired
    @Qualifier("totalUnDrugUsedService")
    private TotalUnDrugUsedService totalUnDrugUsedService;

    public void setTotalDrugUsedService(TotalDrugUsedService totalDrugUsedService) {
        this.totalDrugUsedService = totalDrugUsedService;
    }

    @Autowired
    @Qualifier(value = "reportFormsService")
    private ReportFormsService reportFormsService;

    public void setReportFormsService(ReportFormsService reportFormsService) {
        this.reportFormsService = reportFormsService;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDateSign() {
        return dateSign;
    }

    public void setDateSign(String dateSign) {
        this.dateSign = dateSign;
    }

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
    @Action(value = "queryListTotalIncomeStatic")
    public void queryListTotalIncomeStatic() {
        ListTotalIncomeStaticVo queryVo = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            queryVo = listTotalIncomeStaticService.queryVo(date);
            map.put("cost1", queryVo.getCost1());
            map.put("cost2", queryVo.getCost2());
            map.put("cost3", queryVo.getCost3());
            map.put("cost4", queryVo.getCost4());
            map.put("cost5", queryVo.getCost5());
            map.put("cost6", queryVo.getCost6());
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }
        String json = JSONUtils.toJson(map);
        WebUtils.webSendJSON(json);
    }

    /**
     * 总收入情况统计（年）
     *
     * @Author: huzhenguo
     * @CreateDate: 2017年5月4日 下午4:09:43
     * @Modifier: huzhenguo
     * @ModifyDate: 2017年5月4日 下午4:09:43
     * @ModifyRmk:
     * @version: V1.0
     */
    @Action(value = "queryListTotalIncomeStaticYear")
    public void queryListTotalIncomeStaticYear() {
        ListTotalIncomeStaticVo queryVo = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            queryVo = listTotalIncomeStaticService.queryVoYear(startTime, endTime);
            map.put("cost1", queryVo.getCost1());
            map.put("cost2", queryVo.getCost2());
            map.put("cost3", queryVo.getCost3());
            map.put("cost4", queryVo.getCost4());
            map.put("cost5", queryVo.getCost5());
            map.put("cost6", queryVo.getCost6());
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }

        String json = JSONUtils.toJson(map);
        WebUtils.webSendJSON(json);
    }

    /**
     * 获取所有的统计大类名称和code
     *
     * @Author: donghe
     * @CreateDate: 2017年5月9日 下午4:09:43
     * @Modifier: donghe
     * @ModifyDate: 2017年5月9日 下午4:09:43
     * @ModifyRmk:
     * @version: V1.0
     */
    @Action(value = "queryFeeName")
    public void queryFeeName() {
        List<MinfeeStatCode> list = null;
        try {
            list = listTotalIncomeStaticService.queryFeeName();
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }
        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);
    }

    /**
     * 获取所有的院区名称和code
     */
    @Action(value = "queryAreaName")
    public void queryAreaName() {
        List<BusinessDictionary> list = null;
        try {
            list = listTotalIncomeStaticService.queryAreaName();
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }
        String json = JSONUtils.toJson(list);
        WebUtils.webSendJSON(json);
    }

    /**
     * 总收入情况统计 elasticsearch实现
     *
     * @param date     日期 dateSign为4时，格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"
     * @param dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询
     * @author 朱振坤
     */
    @Action(value = "queryTotalCount", results = {@Result(name = "json", type = "json")})
    public void queryTotalCount() {
        try {
            String json = listTotalIncomeStaticService.queryTotalCountByMongo(date, dateSign);
//            Map<String,Object> queryVo = listTotalIncomeStaticService.queryTotalCountByES(date, dateSign);
//            String json=JSONUtils.toJson(queryVo);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);

        }
    }

    @Action(value = "importDate")
    public void importDate() {
        listTotalIncomeStaticService.initTotalForOracle(startTime, endTime);
    }

    /***
     * 门诊住院环状图
     */
    @Action(value = "queryMZZY", results = {@Result(name = "json", type = "json")})
    public void queryMZZY() {
        List<Dashboard> list = new ArrayList<Dashboard>();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Dashboard> queryVo = null;
        try {
            queryVo = listTotalIncomeStaticService.queryTotalCountMZZY(startTime, dateSign);
            Dashboard vo1 = new Dashboard();
            Dashboard vo2 = new Dashboard();
            vo1.setClassType("门诊");
            vo2.setClassType("住院");
            vo1.setDouValue(0.00);
            vo2.setDouValue(0.00);
            Double valu;
            for (Dashboard vo : queryVo) {
                if ("MZ".equals(vo.getClassType())) {
                    if (null != vo.getDouValue()) {
                        valu = vo.getDouValue() + vo1.getDouValue();
                        vo1.setDouValue(valu);
                    }
                } else {
                    if (null != vo.getDouValue()) {
                        valu = vo.getDouValue() + vo2.getDouValue();
                        vo2.setDouValue(valu);
                    }
                }
            }
            Double dou = vo1.getDouValue() + vo2.getDouValue();
            list.add(vo1);
            list.add(vo2);
            map.put("count", dou);
            map.put("sum", list);
            queryVo = null;
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }
        String json = JSONUtils.toJson(map);
        WebUtils.webSendJSON(json);


    }

    /***
     * 总收入 同比
     */
    @Action(value = "queryTotalSame", results = {@Result(name = "json", type = "json")})
    public void queryTotalSame() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Dashboard> List = null;
        try {
            List = listTotalIncomeStaticService.queryTotalCount(startTime, dateSign);
            Double dou = 0.0;
            for (Dashboard vo : List) {
                if (null != vo.getDouValue()) {
                    dou += vo.getDouValue();
                }
            }
            map.put("count", dou);
            map.put("sum", List);
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }
        String json = JSONUtils.toJson(map);
        WebUtils.webSendJSON(json);
    }

    /***
     * 总收入 同比
     */
    @Action(value = "queryTotalSque", results = {@Result(name = "json", type = "json")})
    public void queryTotalSque() {

        Map<String, Object> map = new HashMap<String, Object>();
        List<Dashboard> List = null;
        try {
            List = listTotalIncomeStaticService.queryTotalCountHB(startTime, dateSign);
            Double dou = 0.0;
            for (Dashboard vo : List) {
                if (null != vo.getDouValue()) {
                    dou += vo.getDouValue();
                }
            }
            map.put("count", dou);
            map.put("sum", List);
        } catch (Exception e) {
            WebUtils.webSendJSON("error");
            //hedong 20170407 异常信息输出至日志文件
            logger.error("YZJC_ZSRQKTJ", e);
            //hedong 20170407 异常信息保存至mongodb
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZSRQKTJ", "运营分析_总收入情况统计", "2", "0"), e);
        }
        String json = JSONUtils.toJson(map);
        WebUtils.webSendJSON(json);
    }

    /**
     * <p>住院收入统计图形页面</p>
     *
     * @Author: XCL
     * @CreateDate: 2018年1月25日 下午7:18:28
     * @Modifier: XCL
     * @ModifyDate: 2018年1月25日 下午7:18:28
     * @ModifyRmk:
     * @version: V1.0
     * @return:
     */
//	@RequiresPermissions(value={"SRTJB:function:view"})
//    @Action(value = "listIncome", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/inpatientIncomeAmount.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
//    public String listIncome() {
//        Date date = new Date();
//        sTime = DateUtils.formatDateY_M_D(date);
//        eTime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -7));
//        return "list";
//    }

}
