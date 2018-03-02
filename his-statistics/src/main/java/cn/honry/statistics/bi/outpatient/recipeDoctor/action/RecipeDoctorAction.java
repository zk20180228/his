package cn.honry.statistics.bi.outpatient.recipeDoctor.action;

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

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.opensymphony.xwork2.ActionSupport;

import cn.honry.statistics.bi.outpatient.recipeDoctor.service.RecipeDoctorService;
import cn.honry.statistics.bi.outpatient.recipeDoctor.vo.BiOptFeedetailVo;

/***
 * 门诊开单医生工作量统计
 * 
 * @author  wfj
 * @date 创建时间：2016年7月27日
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/recipeDoctor")
@SuppressWarnings({ "all" })
public class RecipeDoctorAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	/***
	 * 注入本类service
	 */
	@Autowired
    @Qualifier(value = "recipeDoctorService")
	private RecipeDoctorService recipeDoctorService;
	public void setRecipeDoctorService(RecipeDoctorService recipeDoctorService) {
		this.recipeDoctorService = recipeDoctorService;
	}
	
	//栏目别名
	private String menuAlias;
	//vo 
	private BiOptFeedetailVo biOptVo;
	
	
	
	
	
	
	
	
	
	/***
	 * 初始化页面
	 * @Title: recipeDoctorView 
	 * @author  wfj
	 * @date 创建时间：2016年7月27日
	 * @return view
	 * @return String
	 * @version 1.0
	 * @since
	 */
	@Action(value = "recipeDoctorView", results = { @Result(name = "view", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientWorkLoad/outpatientWorkLoadList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String recipeDoctorView(){
		return "view";
	}
	
	
	@Action( value = "queryData")
	public void queryData(){
		String data = recipeDoctorService.queryData(biOptVo);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**----------------------------------------------Get Set--------------------------------------------------*/
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
}
