package cn.honry.statistics.bi.inpatient.bedUseCondition.action;

import java.util.ArrayList;
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

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.bedUseCondition.service.BedUseConditionService;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;
import cn.honry.statistics.bi.inpatient.dischargePerson.service.DischargePersonService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 病床使用情况统计分析
 * @author zhuxiaolu
 * @createDate：2016/7/27
 * @version 1.0
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/bedUseCondition")
@SuppressWarnings({ "all" })
public class BedUseConditionAction {

	/**
	 * service注入
	 */
	private BedUseConditionService bedUseConditionService;
	public BedUseConditionService getBedUseConditionService() {
		return bedUseConditionService;
	}

	@Autowired
    @Qualifier(value = "bedUseConditionService")
	public void setBedUseConditionService(
			BedUseConditionService bedUseConditionService) {
		this.bedUseConditionService = bedUseConditionService;
	}
	/**
	 * 科室id
	 * */
	private String deptCode;
	
	/**
	 * 年份
	 * */
	private String years;
	
	/**
	 *  跳转病床使用情况统计
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value = "bedUseConditionList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/bedUseCondition/bedUseConditionList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String bedUseConditionList() throws Exception {
		return "list";
	}
	
	/**
	 * 查询所有病区
	 * @author zhuxiaolu
	 * @createDate：2016/7/27
	 * @version 1.0
	 */
	@Action(value="queryBedWardload")
	public void queryDeptWorkload(){
		List<SysDepartment> deptList=bedUseConditionService.queryAllDept();
		String json=JSONUtils.toJson(deptList);
		WebUtils.webSendJSON(json);
	}

	/**
	 * 查询列表数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/27
	 * @version 1.0
	 */
	@Action(value="queryBedUseCondition")
	public void queryBedUseCondition(){
		
		List<BedUseConditionVo> bedUseConditionList=bedUseConditionService.queryBedUseCondition(deptCode, years);
		String json=JSONUtils.toJson(bedUseConditionList);
		WebUtils.webSendJSON(json);
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	
}
