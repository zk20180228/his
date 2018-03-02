package cn.honry.patinent.idcardChange.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.User;
import cn.honry.patinent.idcardChange.service.IdcardChangeService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @className：IdcardAction
 * @Description： 就诊卡变更信息action
 * @Author：GH
 * @CreateDate：2017年2月20日
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/patient/idcardChange")
public class IdcardChangeAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡变更记录  弹窗
	 * @return
	 */ 
	@RequiresPermissions(value = { "JZKGL:function:change" })
	@Action(value = "changeSelectDialog", results = { @Result(name = "changeSelectDialog", location = "/WEB-INF/pages/patient/idcard/changeSelectDialog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String changeSelectDialog(){
		return "changeSelectDialog";
	}
	/**
	 * GH
	 * 2017年2月20日
	 * 根据患者id查询就诊卡变更记录
	 * @return
	 */ 
	@Action(value = "queryChange", results = { @Result(name = "json", type="json") })
	public void queryChange(){
		int total=idcardChangeService.getTotal(patientId,startTime,endTime,type);
		List<PatientIdcardChange> list = idcardChangeService.queryChange(page,rows,patientId,startTime,endTime,type);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查询用户Map
	 * @author zpty
	 * @CreateDate：2017-05-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryUser")
	public void queryUser(){
		List<User> ul=idcardChangeService.queryUserRecord();
		Map<String,String> map=new HashMap<String,String>();
		for(User user:ul){
			map.put(user.getAccount(), user.getName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	//患者信息id
	private String patientId;
	
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//变更类型
	private String type;
	 // 分页用的page
	private String page;
    
     // 分页用的rows
	private String rows;
	
	@Autowired 
	@Qualifier(value = "idcardChangeService")
	private IdcardChangeService idcardChangeService;
	public void setIdcardChangeService(IdcardChangeService idcardChangeService) {
		this.idcardChangeService = idcardChangeService;
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



	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
