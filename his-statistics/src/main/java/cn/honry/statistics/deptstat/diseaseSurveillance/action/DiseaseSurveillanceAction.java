package cn.honry.statistics.deptstat.diseaseSurveillance.action;

import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
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

import cn.honry.statistics.deptstat.diseaseSurveillance.service.DiseaseSurveillanceService;
import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/diseaseSurveillance")
@SuppressWarnings({"all"})
public class DiseaseSurveillanceAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "diseaseSurveillanceService")
	private DiseaseSurveillanceService diseaseSurveillanceService;
	
	private String startTime;
	private String endTime;
	
	public String getStartTime() {
		return startTime;
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
	/**
	 * 跳转住院院重点疾病监测汇总查询页面
	 * @return
	 */
	@Action(value = "diseaseSurveillancelist", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/diseaseSurveillance/diseaseSurveillance.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String diseaseSurveillancelist() {
		//获取时间
		Date date = new Date();
		startTime = DateUtils.formatDateYM(date)+"-01";
		endTime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("startTime", startTime);
		ServletActionContext.getRequest().setAttribute("endTime", endTime);
		return "list";
	}
	/**  
	 * 
	 * 重点疾病监测汇总
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryDiseaseSurveillance")
	public void queryDiseaseSurveillance(){
		List<DiseaseSurveillanceVo> DiseaseSurveillanceList = diseaseSurveillanceService.queryDiseaseSurveillance();
		String json = JSONUtils.toJson(DiseaseSurveillanceList);
		WebUtils.webSendJSON(json);
	}
}
