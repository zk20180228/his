package cn.honry.assets.depot.action;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.assets.depot.service.DepotService;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description 仓库维护action
 * @author  zpty
 * @createDate： 2017年11月14日 下午1:39:09 
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/assets/depot")
public class DepotAction extends ActionSupport implements ModelDriven<AssetsDepot>{

	private static final long serialVersionUID = 1L;

	@Override
	public AssetsDepot getModel() {
		return depot;
	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private DepotService depotService;
	private AssetsDepot depot  = new AssetsDepot();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	public DepotService getDepotService() {
		return depotService;
	}
	@Autowired
	@Qualifier(value="depotService")
	public void setDepotService(DepotService depotService) {
		this.depotService = depotService;
	}
	
	private Logger logger=Logger.getLogger(DepotAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 * 查询数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"CKWH:function:view"})
	@Action(value = "queryAssetsDepotUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/depot/depotList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDepotUrl(){
		return "list";
	}
	@Action(value = "queryAssetsDepot",results={@Result(name="json",type="json")})
	public void queryAssetsDepot(){
		List<AssetsDepot> list = depotService.queryDepot(depot);
		int total = depotService.getDepotCount(depot);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/**
	 *  
	 * @Description：增加数据  
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CKWH:function:add"})
	@Action(value = "saveDepotUrl", results = { @Result(name = "save", location = "/WEB-INF/pages/assets/depot/depotEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrupdateDepotUrl(){
		if(StringUtils.isNotBlank(depot.getId())){
			request.setAttribute("depot", depotService.get(depot.getId()));
		}
		return "save";
	}
	/**
	 *  
	 * @Description：修改数据  
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CKWH:function:edit"})
	@Action(value = "updateDepotUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/depot/depotEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateDepotUrl(){
		if(StringUtils.isNotBlank(depot.getId())){
			request.setAttribute("depot", depotService.get(depot.getId()));
		}
		return "update";
	}
	@Action(value = "saveOrupdateDepot")
	public void saveOrupdateDepot(){
		try{
			AssetsDepot depotSave=new AssetsDepot();
			if(StringUtils.isNotBlank(depot.getId())){
				depotSave = depotService.get(depot.getId());//先用ID查出该数据
			}
			depotSave.setDepotCode(depot.getDepotCode());//仓库编码
			depotSave.setDepotName(depot.getDepotName());//仓库名称
			depotSave.setAddress(depot.getAddress());//仓库地点
			depotSave.setManageAcc(depot.getManageAcc());//仓库管理员工号
			depotSave.setManageName(depot.getManageName());//仓库管理员姓名
			depotSave.setPhone(depot.getPhone());//联系电话
			depotSave.setStop_flg(depot.getStop_flg());//停启用
			depotService.saveOrupdata(depotSave);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("CKWH_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CKWH_XG", "仓库维护_修改", "2", "0"), e);
		}
	}
	/**
	 * 删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"CKWH:function:delete"})
	@Action(value = "deleteDepot")
	public void deleteDepot(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			depotService.delete(depot);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败");
			logger.error("CKWH_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CKWH_XG", "仓库维护_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	/**
	 *  
	 * @Description：  查看数据
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CKWH:function:query"})
	@Action(value = "viewAssetsDepot", results = { @Result(name = "view", location = "/WEB-INF/pages/assets/depot/depotView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAssetsDepot(){
		request.setAttribute("depot", depotService.get(depot.getId()));
		return "view";
	}
	
	/**
	 * 停用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "disableDepot")
	public void disableDepot(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			depotService.disableDepot(depot);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "停用成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "停用失败");
			logger.error("CKWH_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CKWH_XG", "仓库维护_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	/**
	 * 启用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "enableDepot")
	public void enableDepot(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			depotService.enableDepot(depot);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "启用成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "启用失败");
			logger.error("CKWH_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CKWH_XG", "仓库维护_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	
	/**
	 * @Description:返回供应商信息下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryDepotList",results={@Result(name="json",type="json")})
	public void queryDepotList() throws Exception{
		List<AssetsDepot> list = depotService.findAll();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:返回供应商信息下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryDepotMapList",results={@Result(name="json",type="json")})
	public void queryDepotMapList() throws Exception{
		List<AssetsDepot> list = depotService.findAll();
		String json = JSONUtils.toExposeJson(list, false, null, "code","name");
		WebUtils.webSendJSON(json);
	}
	
	public AssetsDepot getDepot() {
		return depot;
	}
	public void setDepot(AssetsDepot depot) {
		depot.setDel_flg(0);
		depot.setStop_flg(0);
		this.depot = depot;
	}
	

	/**
	 * 查出所有数据---用于查出所有的科室
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "findAllDepot")
	public String findAllDepot(){
		List<AssetsDepot> AssetsDepotList=depotService.findAll();
		String json = JSONUtils.toExposeJson(AssetsDepotList, false, null, "depotCode","depotName");
		WebUtils.webSendJSON(json);
		return null;
	}

	/***
	 * 操作时进行数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "verification")
	public void verification(){
		String data = depotService.verification(depot);
		String json = JSONUtils.toJson(data);
		WebUtils.webSendJSON(json);
	}
}
