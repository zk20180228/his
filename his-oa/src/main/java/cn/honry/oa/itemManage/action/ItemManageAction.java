package cn.honry.oa.itemManage.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.TmItems;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.itemManage.service.ItemManageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/oa/itemManage")
public class ItemManageAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(ItemManageAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}

	@Autowired
	@Qualifier(value = "itemManageService")
	private ItemManageService itemManageService;
	public void setItemManageService(ItemManageService itemManageService) {
		this.itemManageService = itemManageService;
	}
	
	private TmItems tmItems = new TmItems();
	public TmItems getTmItems() {
		return tmItems;
	}
	public void setTmItems(TmItems tmItems) {
		this.tmItems = tmItems;
	}

	/**
	 * id多个用逗号分隔
	 */
	private String ids;
	/**
	 *cid分类 类别
	 */
	private String cid;
	/**
	 *id
	 */
	private String id;
	private String parentId;
	private String path;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@RequiresPermissions(value={"YDDMKGL:function:list"})
	@Action(value = "itemList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/itemManage/itemList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  adviceManageList(){
		return "list";
	}
	/**
	 * 
	 * <p>查找全部模块类别树</p>
	 * @Modifier: yuke
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getAllType")
	public void getAllType(){
		TmItems item = new TmItems();
		item.setType(0);
		item.setTypeName("模块类别");
		
		try {
			List<TmItems> list =  itemManageService.getAllType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String json = "["+JSONUtils.toJson(item)+"]";
		json = json.replaceAll("typeName", "text");
		json = json.replaceAll("type", "id");
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * <p>查找全部模块类别</p>
	 * @Modifier: yuke
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getTypes")
	public void getTypes(){
		List<TmItems> list = null;
		try {
			list =  itemManageService.getAllType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(list);
		json = json.replaceAll("typeName", "text");
		json = json.replaceAll("type", "id");
		WebUtils.webSendJSON(json);
	}
	/** 
	 * <p>根据分类查找</p>
	 * @Author: yuke
	 * @param:分类type:cid
	 * @throws:
	 *
	 */
	@Action(value="getItemsByType")
	public void getItemsByType(){
		List<TmItems> list = itemManageService.queryTmItems(id, parentId, path);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	 * <p>跳转到添加界面</p>
	 * @Author: yuke
	 * @param:分类类型
	 * @throws:
	 *
	 */
	@RequiresPermissions(value={"YDDMKGL:function:add"})
	@Action(value = "addItem", results = { @Result(name = "addItem", location = "/WEB-INF/pages/mobile/itemManage/editItem.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  addItem() {
		TmItems item = new TmItems();
		List<TmItems> list = itemManageService.getItemsById(id);
		item.setType(list.get(0).getType());
		item.setTypeName(list.get(0).getTypeName());
		item.setIsParent(list.get(0).getIsParent()+1);
		request.setAttribute("item", item);
		return "addItem";
	}
	/** 
	 * <p>跳转到修改界面</p>
	 * @Author: yuke
	 * @param:id
	 * @throws:
	 *
	 */
	@RequiresPermissions(value={"YDDMKGL:function:edit"})
	@Action(value = "editItem", results = { @Result(name = "editItem", location = "/WEB-INF/pages/mobile/itemManage/editItem.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  editItem() {
		String ids = (String) request.getAttribute("ids");
		List<TmItems> list = itemManageService.getItemsById(ids);
		TmItems item = list.get(0);
		request.setAttribute("item", item);
		return "editItem";
	}
	/** 
	 * <p>跳转到分类添加界面</p>
	 * @Author: yuke
	 * @throws:
	 *
	 */
	@RequiresPermissions(value={"YDDMKGL:function:add"})
	@Action(value = "addItemType", results = { @Result(name = "addItemType", location = "/WEB-INF/pages/mobile/itemManage/editItemType.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  addItemType() {
		System.out.println(type);
		if("undefined".equals(type)){
			type = null;
		}
		return "addItemType";
	}
	
	/** 
	 * <p>保存单个模块</p>
	 * @Author: yuke
	 * @throws:
	 *
	 */
	@Action(value ="saveItem")
	public void saveItem(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			itemManageService.saveOrUpdateItem(tmItems);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDDGNWH", "移动端功能维护", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	
	/** 
	 * <p>保存模块分类</p>
	 * @Author: yuke
	 * @throws:
	 *
	 */
	@Action(value ="saveItemType")
	public void saveItemType(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			itemManageService.saveOrUpdateItemType(tmItems);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDDGNWH", "移动端功能维护", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	 * <p>根据id删除</p>
	 * @Author: yuke
	 * @param:一个或多个id,多个id中间,间隔
	 * @throws:
	 *
	 */
	@Action(value ="delItem")
	public void delItem() {
		Map<String,Object> map=new HashMap<String,Object>();
		String ids = (String) request.getAttribute("ids");
		try {
			itemManageService.delItem(ids);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDDGNWH", "移动端功能维护", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/** 
	 * <p>根据分类type删除该分类下所有</p>
	 * @Author: yuke
	 * @param:分类类型type
	 * @throws:
	 *
	 */
	@Action(value ="delType")
	public void delType() {
		Map<String,Object> map=new HashMap<String,Object>();
		String cid = (String) request.getAttribute("cid");
		Integer type;
		if (StringUtils.isNotBlank(cid)) {
			type = Integer.valueOf(cid);
		}else {
			type = 0;
		}
		try {
			itemManageService.delType(type,parentId,id);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YDDGNWH", "移动端功能维护", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * <p>查找全部模块类别树</p>
	 * @Modifier: donghe
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryAllType")
	public void queryAllType(){
		List<TreeJson> treeJsons = itemManageService.queryAllType(id);
		String json = JSONUtils.toJson(treeJsons);
		WebUtils.webSendJSON(json);
	}

}
