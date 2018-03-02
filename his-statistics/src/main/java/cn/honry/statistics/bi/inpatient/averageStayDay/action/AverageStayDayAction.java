package cn.honry.statistics.bi.inpatient.averageStayDay.action;

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
import cn.honry.statistics.bi.inpatient.averageStayDay.service.AverageStayDayService;
import cn.honry.statistics.bi.inpatient.averageStayDay.vo.AverageStayDayVo;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 平均住院日统计分析
 * @author zhuxiaolu
 * @createDate：2016/7/27
 * @version 1.0
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/averageStayDay")
@SuppressWarnings({ "all" })
public class AverageStayDayAction {

	/**
	 * service注入
	 */
	private AverageStayDayService  averageStayDayService;

	public AverageStayDayService getAverageStayDayService() {
		return averageStayDayService;
	}
	
	@Autowired
    @Qualifier(value = "averageStayDayService")
	public void setAverageStayDayService(AverageStayDayService averageStayDayService) {
		this.averageStayDayService = averageStayDayService;
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
	 *  跳转住院平均日页面
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value = "averageStayDayList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/averageStayDay/averageStayDayList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String averageStayDayList() throws Exception {
		return "list";
	}
	
	/**
	 * 查询所有住院科室
	 * @author zhuxiaolu
	 * @createDate：2016/7/27
	 * @version 1.0
	 */
	@Action(value="queryDeptload")
	public void queryDeptload(){
		List<SysDepartment> deptList=averageStayDayService.queryAllDept();
		String json=JSONUtils.toJson(deptList);
		WebUtils.webSendJSON(json);
	}

	
	/**
	 * 查询列表数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/27
	 * @version 1.0
	 */
	@Action(value="queryAverageStayDay")
	public void queryAverageStayDay(){
		
		List<AverageStayDayVo> averageStayDayList=averageStayDayService.queryAverageStayDay(deptCode, years);
		String json=JSONUtils.toJson(averageStayDayList);
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
