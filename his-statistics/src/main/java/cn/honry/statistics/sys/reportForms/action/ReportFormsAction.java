package cn.honry.statistics.sys.reportForms.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.sys.reportForms.service.ReportFormsService;
import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.statistics.sys.reportForms.vo.PatientInfoVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.statistics.util.ESPageExecutor;
import cn.honry.statistics.util.ESUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = {@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/statistics/ReportForms")
public class ReportFormsAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    ObjectMapper mapperObject = new ObjectMapper();
    @Autowired
    @Qualifier(value = "reportFormsService")
    private ReportFormsService reportFormsService;

    public void setReportFormsService(ReportFormsService reportFormsService) {
        this.reportFormsService = reportFormsService;
    }

    private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数

    public String getMenuAlias() {
        return menuAlias;
    }

    public void setMenuAlias(String menuAlias) {
        this.menuAlias = menuAlias;
    }

    @Autowired
    @Qualifier(value = "deptInInterService")
    private DeptInInterService deptInInterService;

    public void setDeptInInterService(DeptInInterService deptInInterService) {
        this.deptInInterService = deptInInterService;
    }

    // 记录异常日志
    private Logger logger = Logger.getLogger(ReportFormsAction.class);

    // 存储异常
    @Resource
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

    /**
     * 医生工作量统计科室
     */
    private String dept;
    private String expxrt;

    /**
     * 当前登录用户
     */
    public String emp;
    private String page;
    private String rows;

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getExpxrt() {
        return expxrt;
    }

    public void setExpxrt(String expxrt) {
        this.expxrt = expxrt;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    /**
     * 门诊收入统计图日期
     */
    private String date;

    /**
     * dateSign:年月日标记：1,2,3
     */
    private String dateSign;

    /**
     * searchTime:时间格式:yyyy-MM-dd
     */
    private String searchTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateSign(String dateSign) {
        this.dateSign = dateSign;
    }

    public String getDateSign() {
        return dateSign;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public String getSearchTime() {
        return searchTime;
    }

    /**
     * @Description： 医生工作量统计报表获取list页面
     * @Author：wujiao
     * @CreateDate：2015-12-9 下午05:11:49
     * @Modifier：wujiao
     * @ModifyDate：2015-12-9 下午05:11:49
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"YSGZLTJ:function:view"})
    @Action(value = "DoctorWorkloadStatistics", results = {@Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/doctorWorkloadStatistics.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String listDeptrelativity() {
        try {
            Date date = new Date();
            eTime = DateUtils.formatDateY_M_D(date);
            sTime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -3));
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("TJFXGL_YSGZLTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_YSGZLTJ", "门诊统计分析_医生工作量统计报表", "2", "0"), e);
        }

        return "list";
    }


    /**
     * @Description： 医生工作量统计查询
     * @Author：wujiao
     * @CreateDate：2016-5-3 10:12:16
     * @ModifyRmk：
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    @RequiresPermissions(value = {"KSGZLTJ:function:query"})
    @Action(value = "listCountquerywrok", results = {@Result(name = "json", type = "json")})
    public void listCountquerywrok() {
        List<DoctorWorkloadStatistics> reservationList = null;
        try {
            reservationList = reportFormsService.queryReservation(dept, sTime, eTime, menuAlias);
//			for(DoctorWorkloadStatistics d:reservationList){//遍历reservationList集合，去掉dept是null的实体，因为dept=null的数据会导致报null指针,2017-07-17，zk
//				if(null==d.getDept()){
//					reservationList.remove(d);
//				}
//			}
            ComparatorChain chain = new ComparatorChain();
            chain.addComparator(new BeanComparator("dept"), false);
            Collections.sort(reservationList, chain);
            String json = JSONUtils.toJson(reservationList);

            WebUtils.webSendJSON(json);
        } catch (Exception e) {

            //发生异常时返回空内容
            reservationList = new ArrayList<DoctorWorkloadStatistics>();
            String json = JSONUtils.toJson(reservationList);

            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_YSGZLTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_YSGZLTJ", "门诊统计分析_医生工作量统计报表", "2", "0"), e);

        }
    }

    /**
     * @Description： 门诊收入统计图 获取list页面
     * @Author：wujiao
     * @CreateDate：2015-12-9 下午05:11:49
     * @Modifier：wujiao
     * @ModifyDate：2015-12-9 下午05:11:49
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"SRTJB:function:view"})
    @Action(value = "listIncome", results = {@Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/incomeStatistics.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String listIncome() {
        Date date = new Date();
        sTime = DateUtils.formatDateY_M_D(date);
        eTime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -7));

        return "list";
    }

    /**
     * 门诊收入统计图 elasticsearch实现
     *
     * @param date     日期 dateSign为4时，格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"
     * @param dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
     * @author 朱振坤
     */
    @RequiresPermissions(value = {"SRTJB:function:query"})
    @Action(value = "queryOutpatientCharts", results = {@Result(name = "json", type = "json")})
    public void queryOutpatientCharts() {
        try {
            String json = reportFormsService.queryOutpatientChartsByMongo(date, dateSign);
//            Map<String, Object> map = reportFormsService.queryOutpatientChartsByES(date, dateSign);
//            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TJFXGL_SRTJB", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_SRTJB", "门诊统计分析_门诊收入统计", "2", "0"), e);
        }
    }

    /**
     * @Description： 获取list页面
     * @Author：wujiao
     * @CreateDate：2015-12-9 下午05:11:49
     * @Modifier：wujiao
     * @ModifyDate：2015-12-9 下午05:11:49
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"JKSJTJB:function:view"})
    @Action(value = "listHealthData", results = {@Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/healthData.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String listHealthData() {

        return "list";
    }

    /**
     * @Description： 门诊各项收入统计 获取list页面
     * @Author：wujiao
     * @CreateDate：2015-12-9 下午05:11:49
     * @Modifier：wujiao
     * @ModifyDate：2015-12-9 下午05:11:49
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"MZGXSRTJ:function:view"})
    @Action(value = "listrevenueStatistics", results = {@Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/revenueStatistics.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String listrevenueStatistics() {

        Date now = new Date();
        sTime = DateUtils.formatDateY_M(now) + "-01";
        eTime = DateUtils.formatDateY_M_D(now);


        return "list";
    }

    /**
     * @throws
     * @Description： 门诊各项收入统计
     * @Author：wujiao
     * @CreateDate：2016-5-12 下午10:12:16
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"MZGXSRTJ:function:query"})
    @Action(value = "listStatisticsQuery", results = {@Result(name = "json", type = "json")})
    public void listStatisticsQuery() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(dept)) {
                List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias, ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
                if (deptList == null || deptList.size() == 0) {
                    expxrt = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
                } else {
                    for (int i = 0; i < deptList.size(); i++) {
                        dept += deptList.get(i).getDeptCode() + ",";
                    }
                    if (dept.endsWith(",")) {
                        dept = dept.substring(0, dept.lastIndexOf(","));
                    }
                }
            }
            List<StatisticsVo> list = new ArrayList<StatisticsVo>();
            MongoBasicDao mbDao = new MongoBasicDao();
            BasicDBObject bdbObject = new BasicDBObject();
            bdbObject.put("menuAlias", menuAlias);
            bdbObject.put("dept", dept);
            bdbObject.put("expxrt", expxrt);
            bdbObject.put("sTime", sTime);
            bdbObject.put("eTime", eTime);
            bdbObject.put("page", page);
            bdbObject.put("rows", rows);
            DBCursor cursor = mbDao.findAlldata(ESUtils.ESAGGPAGE, bdbObject);
            if (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                String jsonList = dbObject.get("jsonList").toString();
                list = mapperObject.readValue(jsonList, new TypeReference<List<StatisticsVo>>() {
                });
                map.put("rows", list);
                map.put("total", dbObject.get("total"));
            } else {
                list = reportFormsService.listStatisticsQueryByES(dept, expxrt, sTime, eTime);
                List<StatisticsVo> pageList = new ArrayList<StatisticsVo>();
                for (int i = (Integer.valueOf(page) - 1) * Integer.valueOf(rows); i < list.size() && i < Integer.valueOf(page) * Integer.valueOf(rows); i++) {
                    pageList.add(list.get(i));
                }
                map.put("rows", pageList);
                map.put("total", list.size());
                //缓存分页数据线程，暂时解决es聚合后的数据list分页问题
                Thread thread = new Thread(new ESPageExecutor<StatisticsVo>(ESUtils.ESAGGPAGE, list, bdbObject, rows));
                thread.start();
            }
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {

            //发生异常时返回空内容
            map = new HashMap<String, Object>();
            map.put("total", 0);
            map.put("rows", new ArrayList<StatisticsVo>());
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_MZGXSRTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZGXSRTJ", "门诊统计分析_门诊各项收入统计", "2", "0"), e);
        }
    }

    /**
     * @Description： 门诊住院情况统计获取list页面
     * @Author：wujiao
     * @CreateDate：2015-12-9 下午05:11:49
     * @Modifier：wujiao
     * @ModifyDate：2015-12-9 下午05:11:49
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"MZZYQKTJ:function:view"})
    @Action(value = "listPatientInfo", results = {@Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/patientInfo.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String listPatientInfo() {

        return "list";
    }

    /**
     * @Description： 门诊住院情况统计统计查询
     * @Author：wujiao
     * @CreateDate：2016-5-3 下午10:12:16
     * @ModifyRmk：
     * @version 1.0
     */
    @RequiresPermissions(value = {"MZZYQKTJ:function:query"})
    @Action(value = "listPatientQuery", results = {@Result(name = "json", type = "json")})
    public void listPatientQuery() {
        List<PatientInfoVo> patientList = null;
        try {
            if (StringUtils.isBlank(sTime)) {
                sTime = DateUtils.formatDateY_M_D(new Date());
            }
            if (StringUtils.isBlank(eTime)) {
                eTime = DateUtils.formatDateY_M_D(new Date());
            }
            if (StringUtils.isNotBlank(dept) && "all".equals(dept)) {
                SysEmployee empl = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
                List<SysDepartment> depts = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias, empl.getJobNo());
                if (depts != null && depts.size() > 0 && depts.size() < 900) {
                    dept = "";
                    for (SysDepartment sys : depts) {
                        if (StringUtils.isNotBlank(dept)) {
                            dept += "','";
                        }
                        dept += sys.getDeptCode();
                    }
                }
            }

            patientList = reportFormsService.queryPatientInfo(dept, sTime, eTime, menuAlias);
            String json = JSONUtils.toJson(patientList);

            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            patientList = new ArrayList<PatientInfoVo>();

            //发生异常时返回空内容
            String json = JSONUtils.toJson(patientList);
            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_MZZYQKTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZZYQKTJ", "门诊统计分析_门诊住院情况统计", "2", "0"), e);
        }
    }

    //门诊同比 http://localhost:8080/his-portal/statistics/ReportForms/TBTotalIncome.action?dateSign=2&searchTime=2017-05-26
    //{"value":[125763.81,0.0,0.0,1.243714140300016E8,1.0764670261000137E8,9.079835918000057E7],"name":["2017-05","2016-05","2015-05","2014-05","2013-05","2012-05"]}

    /**
     * <p>各项收入6月/日同比，注意：同比日dateSign=3，同比月dateSign=2</p>
     *
     * @Author: zhangkui
     * @CreateDate:2017年05月31日 下午2:39:48
     * @Modifier: zhangkui
     * @ModifyDate:2017年05月31日 下午2:39:48
     * @ModifyRmk:
     * @version: V1.0
     * @param:
     * @throws:
     * @return: void 向前台返回json数据 格式：{"value":[125763.81,0.0,0.0,1.243714140300016E8,1.0764670261000137E8,9.079835918000057E7],"name":["2017-05","2016-05","2015-05","2014-05","2013-05","2012-05"]}
     * 当没有数据时，返回"false"
     */
    @RequiresPermissions(value = {"MZGXSRTJ:function:query"})
    @Action(value = "TBTotalIncome", results = {@Result(name = "json", type = "json")})
    public void TBTotalIncome() {
        Map map = null;
        try {
            map = reportFormsService.TBTotalIncome(dateSign, searchTime);
            if (map == null || map.size() <= 0) {
                WebUtils.webSendJSON("false");
                return;
            }
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);
        } catch (Exception e) {

            //发生异常时返回空内容
            map = new HashMap<String, List<Object>>();
            map.put("value", new ArrayList());
            map.put("name", new ArrayList());
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_MZGXSRTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZGXSRTJ", "门诊统计分析_门诊各项收入统计", "2", "0"), e);
        }
    }

    //门诊环比 http://localhost:8080/his-portal/statistics/ReportForms/HBTotalIncome.action?dateSign=2&searchTime=2017-05-26
    //{"value":[125763.81,1000456.8424,1451850.62,397574.17840000003,35488.5,465955.8165],"name":["2017-05","2017-04","2017-03","2017-02","2017-01","2016-12"]}

    /**
     * <p>门诊各项收入6年/月/日环比，注意：环比日dateSign=3，环比月dateSign=2，环比年dateSign=1</p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年05月31日 下午2:39:48
     * @Modifier: zhangkui
     * @ModifyDate: 2017年05月31日 下午2:39:48
     * @ModifyRmk:
     * @version: V1.0
     * @param:
     * @throws:
     * @return:void 向前台返回json数据 格式：{"value":[125763.81,1000456.8424,1451850.62,397574.17840000003,35488.5,465955.8165],"name":["2017-05","2017-04","2017-03","2017-02","2017-01","2016-12"]}
     * 当没有数据时，返回"false"
     */
    @RequiresPermissions(value = {"MZGXSRTJ:function:query"})
    @Action(value = "HBTotalIncome", results = {@Result(name = "json", type = "json")})
    public void HBTotalIncome() {
        Map map = null;
        try {
            map = reportFormsService.HBTotalIncome(dateSign, searchTime);
            if (map == null || map.size() <= 0) {
                WebUtils.webSendJSON("false");
                return;
            }
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {

            //发生异常时返回空内容
            map = new HashMap<String, List<Object>>();
            map.put("value", new ArrayList());
            map.put("name", new ArrayList());
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_MZGXSRTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZGXSRTJ", "门诊统计分析_门诊各项收入统计", "2", "0"), e);
        }
    }

    //门诊deptTop5 http://localhost:8080/his-portal/statistics/ReportForms/deptTopF.action?dateSign=2&searchTime=2017-05-26
    //{"value":[95253.92,21609.19,8875.7,25.0,0.0],"name":["内科门诊","耳鼻喉门诊","骨科门诊","内分泌科一","其他"]}

    /**
     * <p>门诊各项收入科室前5，注意：科室日前5dateSign=3，科室月前5dateSign=2，科室年前5dateSign=1</p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年05月31日 下午2:39:48
     * @Modifier: zhangkui
     * @ModifyDate: 2017年05月31日 下午2:39:48
     * @ModifyRmk:
     * @version: V1.0
     * @param:
     * @throws:
     * @return: void 向前台返回json数据 格式：{"value":[95253.92,21609.19,8875.7,25.0,0.0],"name":["内科门诊","耳鼻喉门诊","骨科门诊","内分泌科一","其他"]}
     * 当没有数据时，返回"false"
     */
    @RequiresPermissions(value = {"MZGXSRTJ:function:query"})
    @Action(value = "deptTopF", results = {@Result(name = "json", type = "json")})
    public void deptTopF() {
        Map map = null;
        try {
            map = reportFormsService.deptTopF(dateSign, searchTime);
            if (map == null || map.size() <= 0) {
                WebUtils.webSendJSON("false");
                return;
            }
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);
        } catch (Exception e) {

            //发生异常时返回空内容
            map = new HashMap<String, List<Object>>();
            map.put("value", new ArrayList());
            map.put("name", new ArrayList());
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_MZGXSRTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZGXSRTJ", "门诊统计分析_门诊各项收入统计", "2", "0"), e);
        }
    }

    //门诊docterTop5 http://localhost:8080/his-portal/statistics/ReportForms/docterTopF.action?dateSign=2&searchTime=2017-05-26
    //{"value":[79092.66,21609.19,16108.259999999998,6180.0,2695.7,78.0],"name":["abing","张大明","马主任","凉生医生","杜天亮医生","其他"]}

    /**
     * <p>门诊各项收入医生前5，注意：医生日前5dateSign=3，医生月前5dateSign=2，医生年前5dateSign=1</p>
     *
     * @Author: zhangkui
     * @CreateDate: 2017年05月31日 下午2:39:48
     * @Modifier: zhangkui
     * @ModifyDate: 2017年05月31日 下午2:39:48
     * @ModifyRmk:
     * @version: V1.0
     * @param:
     * @throws:
     * @return: void 向前台返回json数据 格式：{"value":[79092.66,21609.19,16108.259999999998,6180.0,2695.7,78.0],"name":["abing","张大明","马主任","凉生医生","杜天亮医生","其他"]}
     * 当没有数据时，返回"false"
     */
    @RequiresPermissions(value = {"MZGXSRTJ:function:query"})
    @Action(value = "docterTopF", results = {@Result(name = "json", type = "json")})
    public void docterTopF() {
        Map map = null;
        try {
            map = reportFormsService.docterTopF(dateSign, searchTime);
            if (map == null || map.size() <= 0) {
                WebUtils.webSendJSON("false");
                return;
            }
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);
        } catch (Exception e) {

            //发生异常时返回空内容
            map = new HashMap<String, List<Object>>();
            map.put("value", new ArrayList());
            map.put("name", new ArrayList());
            String json = JSONUtils.toJson(map);

            WebUtils.webSendJSON(json);

            e.printStackTrace();
            logger.error("TJFXGL_MZGXSRTJ", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZGXSRTJ", "门诊统计分析_门诊各项收入统计", "2", "0"), e);
        }
    }
}
