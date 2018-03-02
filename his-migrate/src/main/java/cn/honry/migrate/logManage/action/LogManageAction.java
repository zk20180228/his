package cn.honry.migrate.logManage.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.LogManage;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.logManage.service.LogManageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/migrate/LogManage")
public class LogManageAction extends ActionSupport{
	private static final long serialVersionUID = -12575988050794941L;
	private Logger logger=Logger.getLogger(LogManageAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value="logManageService")
	private LogManageService logManageService;
	
	public void setLogManageService(LogManageService logManageService) {
		this.logManageService = logManageService;
	}
	private String id;
	private String code;
	private String menuAlias;
	private String page;
	private String rows;
	private String param;
	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**  
	 * 跳转到查询日志页面
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="logManage" ,results={@Result(name="list",location = "/WEB-INF/pages/migrate/outInterfaceManager/logManage.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String logManage(){
		System.out.println(code);
		return "list";
	}
	/**  
	 * 查询日志
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="queryLogManage")
	public void queryLogManage(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<LogManage> list = logManageService.queryLogManage(code,page,rows,param);
			int total = logManageService.queryTotal(code,param);
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "厂商接口管理_对外接口管理", "2", "0"), e);
		}
	}
}
