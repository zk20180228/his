package cn.honry.assets.assetsDeviceCheck.action;

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

import cn.honry.assets.assetsDeviceCheck.service.AssetsDeviceCheckService;
import cn.honry.base.bean.model.AssetsDeviceCheck;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/assets/assetsDeviceCheck")
public class AssetsDeviceCheckAction extends ActionSupport implements ModelDriven<AssetsDeviceCheck>{
	private Logger logger=Logger.getLogger(AssetsDeviceCheckAction.class);
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private AssetsDeviceCheck assetsDeviceCheck =new AssetsDeviceCheck();
	@Override
	public AssetsDeviceCheck getModel() {
		return assetsDeviceCheck;
	}
	@Autowired
	@Qualifier(value="assetsDeviceCheckService")
	private AssetsDeviceCheckService assetsDeviceCheckService;
	
	public AssetsDeviceCheckService getAssetsDeviceCheckService() {
		return assetsDeviceCheckService;
	}
	public void setAssetsDeviceCheckService(
			AssetsDeviceCheckService assetsDeviceCheckService) {
		this.assetsDeviceCheckService = assetsDeviceCheckService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**
	 * 查询数据
	 * @author huzhenguo
	 * @date 2017-11-18
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBPDGL:function:view"})
	@Action(value = "queryAssetsDeviceCheckUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsDeviceCheck/assetsDeviceCheckList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDeviceCheckUrl(){
		return "list";
	}
	/**
	 * 盘点数据
	 * @author huzhenguo
	 * @date 2017-11-18
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBPDGL:function:edit"})
	@Action(value = "updateAssetsDeviceCheckUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/assetsDeviceCheck/assetsDeviceCheckEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateAssetsDeviceCheckUrl(){
		if (StringUtils.isNoneBlank(assetsDeviceCheck.getId())) {
			request.setAttribute("assetsDeviceCheck", assetsDeviceCheckService.get(assetsDeviceCheck.getId()));
		}
		return "update";
	}
	/**  
	 * 
	 * 设备盘点管理
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"SBPDGL:function:query"})
	@Action(value = "queryAssetsDeviceCheck", results = { @Result(name = "json",type = "json") })
	public void queryAssetsDeviceCheck(){
		try {
			List<AssetsDeviceCheck> list = assetsDeviceCheckService.queryAssetsDeviceCheck(assetsDeviceCheck);
			int total=assetsDeviceCheckService.queryTotal(assetsDeviceCheck);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBZCGL_SBPDGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_SBPDGL", "医疗设备资产管理_设备盘点管理", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 盘点
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "saveOrUpdate")
	public void saveOrUpdate(){
		try{
			assetsDeviceCheckService.saveOrUpdate(assetsDeviceCheck);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBPDGL_PD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_SBPDGL", "医疗设备资产管理_盘点", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 校正
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "Correcting")
	public void Correcting(){
		try{
			assetsDeviceCheckService.Correcting(assetsDeviceCheck);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBPDGL_JZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_SBPDGL", "医疗设备资产管理_校正", "2", "0"), e);
		}
	}
}
