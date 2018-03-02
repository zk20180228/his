package cn.honry.statistics.patient.reportStatic.action;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.utils.DateUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 *
* @ClassName:  总收入情况统计
* @author yuke
* @date 2017年7月3日
*
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/StaticReport")
//@Namespace(value = "/sys")
public class StaticReportAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String Etime;
	private String Stime;
	
	public String getEtime() {
		return Etime;
	}

	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getStime() {
		return Stime;
	}

	public void setStime(String stime) {
		Stime = stime;
	}

	/**  
	 * @Description：  获取list页面(总收入情况统计)
	 * @Author：hedong
	 * @CreateDate：2016-06-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"ZSRQKTJ:function:view"})
	@Action(value = "listTotalIncomeStatic", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/totalIncome/totalIncome.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listTotalIncome() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		return "list";
	}
	
	/**  
	 * @Description：  获取list页面(总收入情况明细)
	 * @Author：hedong
	 * @CreateDate：2016-06-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"ZSRQKMX:function:view"})
	@Action(value = "listIncomeDetail", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/totalIncome/incomeDetail.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listIncomeDetail() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		return "list";
	}
	
	/**  
	 * @Description：  获取list页面(手术情况统计)
	 * @Author：hedong
	 * @CreateDate：2016-06-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"SSQKTJ:function:view"})
	@Action(value = "listOperationStatic", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/operation/operation.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperation() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		return "list";
	}
	/**  
	 * @Description：  获取list页面(出入院情况年度统计)
	 * @Author：tangfeishuai
	 * @CreateDate：2016-06-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CRYQKNDTJ:function:view"})
	@Action(value = "listYearAnnualStatis", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/accessToHospital/yearAnnualStatisList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listYearAnnualStatis() {
		return "list";
	}
	
	/**  
	 * @Description：  获取list页面(出入院情况月度统计)
	 * @Author：tangfeishuai
	 * @CreateDate：2016-06-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CRYQKYDTJ:function:view"})
	@Action(value = "listMouthAnnualStatis", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/accessToHospital/mouthAnnualStatisList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listMouthAnnualStatis() {
		return "list";
	}
	
	/**
	 * 获取list页面(门急诊人次统计)
	 */
	@RequiresPermissions(value={"MJZRCTJ:function:view"})
	@Action(value = "toListView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/reportStatic/infoPassengersStatic/infoPassengersStatic.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toListView() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		Stime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -7));
		return "list";
	}


}
