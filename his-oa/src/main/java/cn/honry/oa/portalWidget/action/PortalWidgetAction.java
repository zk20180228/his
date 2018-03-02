package cn.honry.oa.portalWidget.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.portalWidget.service.PortalWidgetService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * OA系统首页组件维护
 * @author  zpty
 * @date 2017-7-15 15：40
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/portalWidget")
public class PortalWidgetAction extends ActionSupport{
	private Logger logger=Logger.getLogger(PortalWidgetAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}

	private static final long serialVersionUID = 1L;
	private PortalWidgetService portalWidgetService;
	@Autowired
	@Qualifier(value = "portalWidgetService")
	public void setPortalWidgetService(PortalWidgetService portalWidgetService) {
		this.portalWidgetService = portalWidgetService;
	}
	
	/**
	 * 首页组件表
	 */
	private OaPortalWidget portalWidget = new OaPortalWidget();

	/**
	 * 组件编号id
	 */
	private String portalWidgetid;
	
	public OaPortalWidget getPortalWidget() {
		return portalWidget;
	}

	public void setPortalWidget(OaPortalWidget portalWidget) {
		this.portalWidget = portalWidget;
	}
	
	public String getPortalWidgetid() {
		return portalWidgetid;
	}

	public void setPortalWidgetid(String portalWidgetid) {
		this.portalWidgetid = portalWidgetid;
	}

	/**
	 * @Description:翻页参数
	 */
	public String page;
	public void setPage(String page) {
		this.page = page;
	}
	public String rows;
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * @Description:查询用传递参数
	 */
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**  
	 * 
	 * 首页组件列表
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:48:26 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "listPortalWidget", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/portalWidget/portalWidgetList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPortalWidget() {
		return "list";
	}
	
	/**  
	 * 
	 * 查询首页组件信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:49:06 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryPortalWidget")
	public void queryPortalWidget() {	
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			portalWidget.setName(name);//将从前台传过来的查询条件放入实体
			int total = portalWidgetService.getTotal(portalWidget);
			List<OaPortalWidget> portalList =portalWidgetService.query(portalWidget,page,rows);
			map.put("total", total);
			map.put("rows", portalList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("SYZJGL_CXLB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYZJGL_CXLB", "OA系统首页组件管理_查询列表", "2", "0"), e);
		}
	}

	/**  
	 * 
	 * 跳转添加组件页面
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:49:53 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "addPortalWidget", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/portalWidget/portalWidgetEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addPortalWidget() {
		return "add";
	}
	
	/**  
	 * 
	 * 跳转组件编辑页面
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:50:48 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "editPortalWidget", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/portalWidget/portalWidgetEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editPortalWidget() {
		portalWidget = portalWidgetService.get(portalWidgetid);
		return "edit";
	}
	
	/**  
	 * 
	 * 跳转组件查看页面
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:51:21 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "viewPortalWidget", results = { @Result(name = "view", location = "/WEB-INF/pages/oa/portalWidget/portalWidgetView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewPortalWidget() {
		portalWidget = portalWidgetService.get(portalWidgetid);
		return "view";
	}
	/**  
	 * 
	 * 添加&修改
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:51:46 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "savePortalWidget")
	public void savePortalWidget(){
		try{
			String sign=null;
			portalWidgetService.saveOrUpdate(portalWidget);
			sign="true";
			WebUtils.webSendJSON(sign);
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SYZJGL_TJXG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYZJGL_TJXG", "OA系统首页组件管理_添加修改", "2", "0"), e);
		}
	}

	/**  
	 * 
	 * 删除
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:00:47 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "delPortalWidget", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/portalWidget/portalWidgetList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delPortalWidget() {
		try {
			portalWidgetService.del(portalWidgetid);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("SYZJGL_SC", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYZJGL_SC", "OA系统首页组件管理_删除", "2", "0"), e);
		}
		return "list";
	}
}
