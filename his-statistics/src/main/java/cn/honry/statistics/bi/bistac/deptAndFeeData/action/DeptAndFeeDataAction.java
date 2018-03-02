package cn.honry.statistics.bi.bistac.deptAndFeeData.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.deptAndFeeData.service.DeptAndFeeDataService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatientIncome")
public class DeptAndFeeDataAction {
    private Logger logger = Logger.getLogger(DeptAndFeeDataAction.class);

    @Resource(name = "hiasExceptionService")
    private HIASExceptionService hiasExceptionService;
    public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
        this.hiasExceptionService = hiasExceptionService;
    }

    @Resource(name = "deptAndFeeDataService")
    private DeptAndFeeDataService deptAndFeeDataService;
    private String sTime;
    public String getsTime() {
        return sTime;
    }
    public void setsTime(String sTime) {
        this.sTime = sTime;
    }
    private String eTime;
    public String geteTime() {
        return eTime;
    }
    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    private String dateSign;
    public void setDateSign(String dateSign) {
        this.dateSign = dateSign;
    }

    private String date;
    public void setDate(String date) {
        this.date = date;
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
    @Action(value = "listIncome", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/inpatientIncomeAmount.jsp")}, interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
    public String listIncome() {
        Date date = new Date();
        sTime = DateUtils.formatDateY_M_D(date);
        eTime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -7));
        return "list";
    }

    @Action(value = "queryInpatientCharts", results = {@Result(name = "json", type = "json")})
    public void queryOutpatientCharts() {
        try {
            String json = deptAndFeeDataService.queryInpatientChartsByMongo(date, dateSign);
//            String json = deptAndFeeDataService.queryInpatientChartsByES(date, dateSign);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TJFXGL_SRTJB", e);
            hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_SRTJB", "门诊统计分析_门诊收入统计", "2", "0"), e);
        }
    }
}
