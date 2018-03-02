package cn.honry.oa.personalAddressList.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.personalAddressList.service.PersonalAddressListService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/personalAddressList")
public class PersonalAddressListAction {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "personalAddressListService")
	private PersonalAddressListService personalAddressListService;
	public void setPersonalAddressListService(
			PersonalAddressListService personalAddressListService) {
		this.personalAddressListService = personalAddressListService;
	}
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private Logger logger=Logger.getLogger(PersonalAddressListAction.class);
	
	/**
	 * 上级id
	 */
	private String parentCode;
	
	/**
	 * 分组id
	 */
	private String id;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 * 分组name
	 */
	private String groupName;
	
	/**
	 * 当前页数
	 */
    private String page;
	
    /**
	 * 行数
	 */
	private String rows;
	
	/**
	 * 查询参数
	 */
	private String queryName;
	
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	private PersonalAddressList personalAddress=new PersonalAddressList();
	
	
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PersonalAddressList getPersonalAddress() {
		return personalAddress;
	}
	public void setPersonalAddress(PersonalAddressList personalAddress) {
		this.personalAddress = personalAddress;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}


	/**  
	 *  
	 * 跳转list页面
	 * @Author：zxl
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：zxl
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GRTXL:function:view"})
	@Action(value = "listPersonalAddress", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/personalAddressList/personalAddressList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listFormInfo() {
		return "list";
	}
	
	
	/**  
	 * 
	 * 通讯录树
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "treePersonalAddress", results = { @Result(name = "json", type = "json") })
	public void treePersonalAddress() {
		List<TreeJson> treeDepar = personalAddressListService.QueryTree(id);
		String json = JSONUtils.toJson(treeDepar);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 添加分组
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "addGroup", results = { @Result(name = "addTree", location = "/WEB-INF/pages/oa/personalAddressList/groupTreeEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addGroup() {
		return "addTree";
	}

	/**  
	 * 
	 * 分组保存
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:personalAddress 通讯录实体
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "saveGroup", results = { @Result(name = "json", type = "json") })
	public void saveGroup(){
		 Map<String,String> retMap = new HashMap<>();
		 try{
			 personalAddressListService.saveOrUpdateTree(personalAddress);
			 retMap.put("resCode", "success");
			 retMap.put("resMsg", "保存成功！");
		 }catch (Exception e) {
			 retMap.put("resCode", "error");
			 retMap.put("resMsg", "保存失败！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "2", "0"), e);
		 }
		 WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	/**  
	 * 
	 * 分组删除
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id 分组id
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "delGroup", results = { @Result(name = "json", type = "json") })
	public void delGroup(){
		 Map<String,String> retMap = new HashMap<>();
		 try{
			 personalAddressListService.delGroup(id);
			 retMap.put("resCode", "success");
			 retMap.put("resMsg", "移除成功！");
		 }catch (Exception e) {
			 retMap.put("resCode", "error");
			 retMap.put("resMsg", "移除失败！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "2", "0"), e);
		 }
		 WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	/**  
	 * 
	 * 修改分组
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "editGroup", results = { @Result(name = "addTree", location = "/WEB-INF/pages/oa/personalAddressList/groupTreeEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editGroup() {
		try {
			groupName=URLDecoder.decode(groupName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "3", "0"), e);
		}
		return "addTree";
	}
	
	
	/**  
	 *  
	 * 获取所有联系人列表
	 * @Author：zxl
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：zxl
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GRTXL:function:query"}) 
	@Action(value = "queryPersonalAddress")
	public void queryPersonalAddress() {
		Map<String,Object> map = new HashMap<String, Object>();
		if("root".equals(parentCode)){
			parentCode=null;
		}
		int total = personalAddressListService.getPersonalTotal(parentCode,queryName);
		List<PersonalAddressList> list = personalAddressListService.getPersonalLists(page,rows,parentCode,queryName);
		map.put("total", total);
		map.put("rows", list);
		String json =JSONUtils.toJson(map, DateUtils.DATE_FORMAT);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 跳转添加个人信息页面
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "addPersonalMes", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/personalAddressList/personalAddressEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addPersonalMes() {
		try {
			personalAddress.setParentCode(parentCode);
			personalAddress.setBelongGroupName(URLDecoder.decode(groupName,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "3", "0"), e);
		}
		return "add";
	}
	
	/**  
	 * 
	 * 个人信息保存
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:personalAddress 通讯录实体
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "savePersonalAddress", results = { @Result(name = "json", type = "json") })
	public void savePersonalAddress(){
		 Map<String,String> retMap = new HashMap<>();
		 try{
			 personalAddressListService.savePersonalAddress(personalAddress);
			 retMap.put("resCode", "success");
			 retMap.put("resMsg", "保存成功！");
		 }catch (Exception e) {
			 retMap.put("resCode", "error");
			 retMap.put("resMsg", "保存失败！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "2", "0"), e);
		 }
		 WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	/**  
	 * 
	 * 跳转添加个人信息页面
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "editPersonalMes", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/personalAddressList/personalAddressEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editPersonalMes() {
		personalAddress=personalAddressListService.personalAddressById(id);
		return "edit";
	}
	
	/**  
	 * 
	 * 删除个人信息
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id 个人信息id
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "delPersonalMes", results = { @Result(name = "json", type = "json") })
	public void delPersonalMes(){
		 Map<String,String> retMap = new HashMap<>();
		 try{
			 personalAddressListService.delPersonalMes(id);
			 retMap.put("resCode", "success");
			 retMap.put("resMsg", "删除成功！");
		 }catch (Exception e) {
			 retMap.put("resCode", "error");
			 retMap.put("resMsg", "删除失败！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "2", "0"), e);
		 }
		 WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	/**  
	 * 
	 * 跳转查看个人信息页面
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "viewPersonalMes", results = { @Result(name = "view", location = "/WEB-INF/pages/oa/personalAddressList/personalAddressView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewPersonalMes() {
		personalAddress=personalAddressListService.personalAddressById(id);
		return "view";
	}
	
	/**  
	 * 
	 * 个人信息保存
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:personalAddress 通讯录实体
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "findAllGroup", results = { @Result(name = "json", type = "json") })
	public void findAllGroup(){
		 List<PersonalAddressList> list =personalAddressListService.findAllGroup(parentCode);
		 WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	
	/**  
	 * 
	 * 个人信息移至其他分组
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:personalAddress 通讯录实体
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "movePersonalToGroup", results = { @Result(name = "json", type = "json") })
	public void movePersonalToGroup(){
		 Map<String,String> retMap = new HashMap<>();
		 try{
			 personalAddressListService.movePersonalToGroup(id,parentCode);
			 retMap.put("resCode", "success");
			 retMap.put("resMsg", "移动成功！");
		 }catch (Exception e) {
			 retMap.put("resCode", "error");
			 retMap.put("resMsg", "移动失败！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("GRTXL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GRTXL", "个人通讯录", "2", "0"), e);
		 }
		 WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
}
