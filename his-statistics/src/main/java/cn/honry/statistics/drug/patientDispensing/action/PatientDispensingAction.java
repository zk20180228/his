package cn.honry.statistics.drug.patientDispensing.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.drug.patientDispensing.service.PatientDispensingService;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 病区患者摆药查询
 * @Description:
 * @author: donghe
 * @CreateDate: 2016年6月23日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/PatientDispensing")
public class PatientDispensingAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;

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
	 * 现在时间
	 */
	private String now;
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}
	/**
	 * 开始时间
	 */
	private String beginDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
    @Qualifier(value = "patientDispensingService")
 	private PatientDispensingService patientDispensingService;
	public void setPatientDispensingService(
			PatientDispensingService patientDispensingService) {
		this.patientDispensingService = patientDispensingService;
	}
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 药品名称
	 */
	private String tradeName;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	/***
	 * 
	 * @Description:病区患者摆药查询  页面跳转
	 * @author:  donghe
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	//@RequiresPermissions(value={"CDMXRB:function:view"}) 
	@Action(value="listPatientDispensing",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/patientDispensing/patientDispensing.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String listPatientDispensing(){
		//获取时间
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = simpledateformat.format(calendar.getTime());
		return "list";
	}
	/**
	 * @Description： 查询患者树
	 * @throws IOException 
	 * @Author：zhenglin
	 */
	@Action(value = "InfoTree")
	public void InfoTree() throws IOException {
		String patientTree=patientDispensingService.queryPatientTree(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		WebUtils.webSendJSON(patientTree);		
	}
	/***
	 * 
	 * @Description:结账处冲单明细日报List
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 */
	@Action(value = "queryVinpatientApplyoutlist")
	public void queryVinpatientApplyoutlist() {
		Map<String,Object> retMap = new HashMap<String,Object>();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String type = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptType();
		List<VinpatientApplyout> list = patientDispensingService.queryVinpatientApplyout(deptId, type, page, rows,tradeName,inpatientNo,endDate,beginDate);
		for (VinpatientApplyout v : list) {
			v.setDoseUnit(v.getDoseUnit());
			v.setDrugDeptCode(patientDispensingService.querySysDepartment(v.getDrugDeptCode()).getDeptName());
			v.setBillclassCode(patientDispensingService.queryDrugBillclass(v.getBillclassCode()).getBillclassName());
			v.setApplyOpercode(patientDispensingService.queryUser(v.getApplyOpercode()).getName());
			v.setPrintEmpl(patientDispensingService.queryUser(v.getPrintEmpl()).getName());
		}
		int total = patientDispensingService.qqueryVinpatientApplyoutTotal(deptId, type,tradeName,inpatientNo,endDate,beginDate);
		retMap.put("total", total);
		retMap.put("rows", list);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
}
