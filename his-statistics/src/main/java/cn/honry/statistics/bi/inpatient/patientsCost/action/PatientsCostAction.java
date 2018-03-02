/**
 * 
 */
package cn.honry.statistics.bi.inpatient.patientsCost.action;

import java.util.List;

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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.patientsCost.service.PatientsCostService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 在院病人费用分析Action
 * @author luysnshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/patientsCost")
@SuppressWarnings({ "all" })
public class PatientsCostAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private PatientsCostService patientsCostService;

	@Autowired
    @Qualifier(value = "patientsCostService")
	public void setPatientsCostService(PatientsCostService patientsCostService) {
		this.patientsCostService = patientsCostService;
	}
	
	private DateVo datevo;//接收前台传递的时间参数
	
	public DateVo getDatevo() {
		return datevo;
	}

	public void setDatevo(DateVo datevo) {
		this.datevo = datevo;
	}
	
	private String disease;//疾病类别
	private String cost;//费用类别
	private String dept;//科室
	private int n;//统计方式(1-按年统计;2-按季统计;3-按月统计;4-按日统计)

	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public int getN() {
		return n;
	}
	
	public void setN(int n) {
		this.n = n;
	}

	/**
	 * 跳转页面
	 * @return
	 */
	@Action(value = "patientsView", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/patientsCost/patientsCost.jsp") }, 
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String patientsView(){
		return "list";
	}
	
	/**
	 * 获取住院下的科室信息
	 */
	@Action(value="getDeptInfo")
	public void getDeptInfo(){
		List<SysDepartment> list = patientsCostService.getDeptInfo();
		SysDepartment department = new SysDepartment();
		department.setId("0");
		department.setDeptName("全部");
		list.add(department);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	

	/**
	 * 获取统计费用名称
	 */
	@Action(value="getFeeName")
	public void getFeeName(){
		List<MinfeeStatCode> list = patientsCostService.getFeeName();
		MinfeeStatCode minfeeStatCode = new MinfeeStatCode();
		minfeeStatCode.setFeeStatName("全部");
		list.add(minfeeStatCode);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 获取统计结果
	 */
	@Action(value="getResults")
	public void getResults(){
		try {
			if(datevo!=null){
				//System.out.println("test******");
				String results = patientsCostService.getResults(datevo, disease, cost, dept, n);
				WebUtils.webSendJSON(results);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
