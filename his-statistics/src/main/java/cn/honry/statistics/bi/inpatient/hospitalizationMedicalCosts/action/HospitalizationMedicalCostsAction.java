package cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.action;

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

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.vo.HospitalizationExpensesVo;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.service.HospitalizationMedicalCostsService;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.vo.HospitalizationMedicalCostsVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;


/**
 * 住院人次费用统计分析
 * @author GengH
 * @createDate：2016年8月4日16:23:49
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/biHospitalizationMedicalCosts")
@SuppressWarnings({ "all" })
public class HospitalizationMedicalCostsAction extends ActionSupport{
	/**
	 * service注入
	 */
	private HospitalizationMedicalCostsService hospitalizationMedicalCostsService;
	@Autowired
    @Qualifier(value = "hospitalizationMedicalCostsService")
	public void setHospitalizationMedicalCostsService(
			HospitalizationMedicalCostsService hospitalizationMedicalCostsService) {
		this.hospitalizationMedicalCostsService = hospitalizationMedicalCostsService;
	}
	//时间
	private Integer timeone;

	//科室名称
	private String nameString;
	
	
	//统计图时间字符串（逗号分隔）
	private String timeString;
	
	
	
	public String getNameString() {
		return nameString;
	}
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	public Integer getTimeone() {
		return timeone;
	}
	public void setTimeone(Integer timeone) {
		this.timeone = timeone;
	}
	/**
	 *  跳转住院人次费用统计分析页面
	 * @author GengH
	 * @createDate：2016年8月5日09:36:30
	 * @version 1.0
	 */
	@Action(value = "queryDategrid", results = { @Result(name = "queryDategrid", location = "/WEB-INF/pages/stat/bi/inpatient/hospitalizationMedicalCosts/hospitalizationMedicalCostsList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryDategrid() throws Exception {
		return "queryDategrid";
	}
	
	/**
	 * 查询所有科室
	 * @author GengH
	 * @createDate：2016年7月22日11:00:45
	 * @version 1.0
	 */
	@Action(value="queryDeptWorkload")
	public void queryDeptWorkload(){
		List<SysDepartment> deptList=hospitalizationMedicalCostsService.queryAllDept();
		String json=JSONUtils.toExposeJson(deptList, false, null, "deptCode","deptName");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询列表数据
	 * @author GengH
	 * @createDate：2016年8月5日09:36:20
	 * @version 1.0 
	 */
	@Action(value="querytDatagrid")
	public void querytDatagrid(){
		List<HospitalizationMedicalCostsVo> list=hospitalizationMedicalCostsService.querytDatagrid(timeone,nameString);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 获取统计图数据
	 * @author GengH
	 */
//	@Action(value="queryStatDate")
//	public void queryStatDate(){
//		String json=JSONUtils.toJson(hospitalizationMedicalCostsService.queryStatDate(timeString,nameString));
//		WebUtils.webSendJSON(json);
//	}
}

