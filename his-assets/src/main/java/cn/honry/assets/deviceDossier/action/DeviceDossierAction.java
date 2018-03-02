package cn.honry.assets.deviceDossier.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import cn.honry.assets.device.service.DeviceService;
import cn.honry.assets.deviceDossier.service.DeviceDossierService;
import cn.honry.assets.deviceDossier.vo.DeviceDossierVo;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description 设备档案管理action
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
@Namespace(value = "/assets/deviceDossier")
public class DeviceDossierAction extends ActionSupport implements ModelDriven<AssetsDeviceDossier>{
	private Logger logger=Logger.getLogger(DeviceDossierAction.class);
	private static final long serialVersionUID = 1L;
	private AssetsDeviceDossier deviceDossier  = new AssetsDeviceDossier();
	private HttpServletRequest request = ServletActionContext.getRequest();
	private String menuAlias;
	@Override
	public AssetsDeviceDossier getModel() {
		return deviceDossier;
	}
	private String page;
	private String rows;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@Autowired
	@Qualifier(value="deviceDossierService")
	private DeviceDossierService deviceDossierService;
	public DeviceDossierService getDeviceDossierService() {
		return deviceDossierService;
	}
	public void setDeviceDossierService(DeviceDossierService deviceDossierService) {
		this.deviceDossierService = deviceDossierService;
	}
	

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
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
	@Autowired
	private DeviceService deviceService;
	public DeviceService getDeviceService() {
		return deviceService;
	}
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	/**
	 * 查询数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBDAGL:function:view"})
	@Action(value = "queryAssetsDeviceDossierUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceDossier/deviceDossierList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDeviceDossierUrl(){
		return "list";
	}
	@Action(value = "queryAssetsDeviceDossier",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceDossier(){
		List<AssetsDeviceDossier> list = deviceDossierService.queryDeviceDossier(deviceDossier);
		int total = deviceDossierService.getDeviceDossierCount(deviceDossier);
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
	@RequiresPermissions(value={"SBDAGL:function:add"})
	@Action(value = "saveDeviceDossierUrl", results = { @Result(name = "save", location = "/WEB-INF/pages/assets/deviceDossier/deviceDossierEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrupdateDeviceDossierUrl(){
		if(StringUtils.isNotBlank(deviceDossier.getId())){
			request.setAttribute("deviceDossier", deviceDossierService.get(deviceDossier.getId()));
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
	@RequiresPermissions(value={"SBDAGL:function:edit"})
	@Action(value = "updateDeviceDossierUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/deviceDossier/deviceDossierEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateDeviceDossierUrl(){
		if(StringUtils.isNotBlank(deviceDossier.getId())){
			request.setAttribute("deviceDossier", deviceDossierService.get(deviceDossier.getId()));
		}
		return "update";
	}
	@Action(value = "saveOrupdateDeviceDossier")
	public void saveOrupdateDeviceDossier(){
		try{
			AssetsDeviceDossier deviceDossierSave=new AssetsDeviceDossier();
			if(StringUtils.isNotBlank(deviceDossier.getId())){
				deviceDossierSave = deviceDossierService.get(deviceDossier.getId());//先用ID查出该数据
			}
			deviceDossierSave.setOfficeCode(deviceDossier.getOfficeCode());//办公用途编码
			deviceDossierSave.setOfficeName(deviceDossier.getOfficeName());//办公用途名称
			deviceDossierSave.setClassCode(deviceDossier.getClassCode());//设备分类编码
			deviceDossierSave.setClassName(deviceDossier.getClassName());//设备分类名称
			deviceDossierSave.setDeviceNo(deviceDossier.getDeviceNo());//设备条码号
			deviceDossierSave.setDeviceCode(deviceDossier.getDeviceCode());//设备代码
			deviceDossierSave.setDeviceName(deviceDossier.getDeviceName());//设备名称
			deviceDossierSave.setMeterUnit(deviceDossier.getMeterUnit());//计量单位
			deviceDossierSave.setPurchPrice(deviceDossier.getPurchPrice());//采购单价(元)
			deviceDossierSave.setUseDeptCode(deviceDossier.getUseDeptCode());//领用部门编码
			deviceDossierSave.setUseDeptName(deviceDossier.getUseDeptName());//领用部门名称
			deviceDossierSave.setUseAcc(deviceDossier.getUseAcc());// 领用人工号 
			deviceDossierSave.setUseName(deviceDossier.getUseName());//领用人姓名 
			deviceDossierService.saveOrupdata(deviceDossierSave);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBDAGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBDAGL_XG", "设备档案管理_修改", "2", "0"), e);
		}
	}
	/**
	 * 删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBDAGL:function:delete"})
	@Action(value = "deleteDeviceDossier")
	public void deleteDeviceDossier(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceDossierService.delete(deviceDossier);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败");
			logger.error("SBDAGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBDAGL_XG", "设备档案管理_修改", "2", "0"), e);
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
	@RequiresPermissions(value={"SBDAGL:function:query"})
	@Action(value = "viewAssetsDeviceDossier", results = { @Result(name = "view", location = "/WEB-INF/pages/assets/deviceDossier/deviceDossierView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAssetsDeviceDossier(){
		request.setAttribute("deviceDossier", deviceDossierService.get(deviceDossier.getId()));
		return "view";
	}
	
	/**
	 * 停用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "disableDeviceDossier")
	public void disableDeviceDossier(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceDossierService.disableDeviceDossier(deviceDossier);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "停用成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "停用失败");
			logger.error("SBDAGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBDAGL_XG", "设备档案管理_修改", "2", "0"), e);
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
	@Action(value = "enableDeviceDossier")
	public void enableDeviceDossier(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceDossierService.enableDeviceDossier(deviceDossier);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "启用成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "启用失败");
			logger.error("SBDAGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBDAGL_XG", "设备档案管理_修改", "2", "0"), e);
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
	@Action(value = "queryDeviceDossierList",results={@Result(name="json",type="json")})
	public void queryDeviceDossierList() throws Exception{
		List<AssetsDeviceDossier> list = deviceDossierService.findAll();
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
	@Action(value = "queryDeviceDossierMapList",results={@Result(name="json",type="json")})
	public void queryDeviceDossierMapList() throws Exception{
		List<AssetsDeviceDossier> list = deviceDossierService.findAll();
		String json = JSONUtils.toExposeJson(list, false, null, "code","name");
		WebUtils.webSendJSON(json);
	}
	
	public AssetsDeviceDossier getDeviceDossier() {
		return deviceDossier;
	}
	public void setDeviceDossier(AssetsDeviceDossier deviceDossier) {
		deviceDossier.setDel_flg(0);
		deviceDossier.setStop_flg(0);
		this.deviceDossier = deviceDossier;
	}
	

	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "findAllDeviceDossier")
	public String findAllDeviceDossier(){
		List<AssetsDeviceDossier> AssetsDeviceDossierList=deviceDossierService.findAll();
		String json = JSONUtils.toExposeJson(AssetsDeviceDossierList, false, null, "code","name");
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
		String data = deviceDossierService.verification(deviceDossier);
		String json = JSONUtils.toJson(data);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 设备资产
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"ZCTJFXGL:function:query"})
	@Action(value = "queryDeviceDossierValue", results = { @Result(name = "json",type = "json") })
	public void queryDeviceDossierValue(){
		try {
			List<DeviceDossierVo> list = deviceDossierService.queryDeviceDossierValue(deviceDossier);
			int total=deviceDossierService.queryDeviceDossierTotal(deviceDossier);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBZCGL_SBZC", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_SBZC", "医疗设备资产管理_设备资产", "2", "0"), e);
		}
	}
	
	@Action(value = "queryAssetsRepair")
	public void queryAssetsRepair() {
		String state = request.getParameter("state");
		String officeName = request.getParameter("officeName");
		String className = request.getParameter("className");
		String deviceCode = request.getParameter("deviceCode");
		String deviceName = request.getParameter("deviceName");
		AssetsDeviceDossier assets = new AssetsDeviceDossier();
		assets.setOfficeName(officeName);
		assets.setClassName(className);
		assets.setDeviceCode(deviceCode);
		assets.setDeviceName(deviceName);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<AssetsDeviceDossier> assetsList = deviceDossierService.queryAllAssetsByData(page,rows,assets,state);
			int total = deviceDossierService.getTotalList(page,rows,assets,state);
			map.put("total", total);
			map.put("rows", assetsList);
			
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList<AssetsPurchplan>());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "repairResonView", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsRepair/assetsRepairReson.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String assetsRepair() {
		deviceDossier = deviceDossierService.get(deviceDossier.getId());
		return "list";
	}
	@Action(value = "scrapResonView", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsRepair/assetsRepairScrap.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String scrapResonView() {
		deviceDossier = deviceDossierService.get(deviceDossier.getId());
		return "list";
	}
	@Action(value = "saveRepairReson", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsRepair/assetsRepair.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void saveRepairReson(){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			String repairReson = request.getParameter("repairReson");
			deviceDossierService.saveRepairReson(deviceDossier,repairReson);
			map.put("resCode","success");
			map.put("resMsg","维修成功！");
		} catch (Exception e) {
			map.put("resCode","error");
			map.put("resMsg","维修失败");
			logger.error("saveOrUpdateAssets", e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action(value = "assetScrap", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsRepair/assetsRepairScrap.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void assetScrap(){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			String assetScrap = request.getParameter("assetScrap");
			deviceDossierService.saveRepairScrap(deviceDossier,assetScrap);
			map.put("resCode","success");
			map.put("resMsg","报废成功！");
		} catch (Exception e) {
			map.put("resCode","error");
			map.put("resMsg","报废失败");
			logger.error("saveOrUpdateAssets", e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
	//***************************************************以下是设备领用管理******************************************************//
	
	/**
	 * 查询数据--设备领用管理进入页面
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBLYGL:function:view"})
	@Action(value = "queryAssetsDeviceUseUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceUse/deviceUseList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDeviceUseUrl(){
		return "list";
	}
	
	@Action(value = "deviceMyUse", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceUse/deviceMyUse.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceMyUse(){
		return "list";
	}
	
	@Action(value = "deviceReceive", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceUse/deviceReceive.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceReceive(){
		return "list";
	}
	
	@Action(value = "viewReceive", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceUse/deviceViewList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewReceive(){
		request.setAttribute("viewId", deviceDossier.getId());
		return "list";
	}
	
	/**
	 * 查询数据--我的设备列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "queryAssetsDeviceMyUse",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceMyUse(){
		List<AssetsDeviceDossier> list = deviceDossierService.queryDeviceDossierMyUse(deviceDossier);
		int total = deviceDossierService.getDeviceDossierCountMyUse(deviceDossier);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查询数据--设备领用列表,这里的列表是所有通过入库审批的记录
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "queryAssetsDeviceReceive",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceReceive(){
		List<AssetsDevice> list = deviceDossierService.queryDeviceDossierReceive(deviceDossier);
		int total = deviceDossierService.getDeviceDossierCountReceive(deviceDossier);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 *  
	 * @Description：领用数据  --进入领用编辑页面
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "deviceReceiveUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/deviceUse/deviceReceiveEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceReceiveUrl(){
		// 获取当前登录人
		String userName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();//领用人姓名
		String userMobile = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getMobile();//联系电话
		String loginDeptName = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();//领用人科室名称
		if(StringUtils.isNotBlank(deviceDossier.getId())){
			AssetsDevice assetsDevice = deviceService.get(deviceDossier.getId());
			deviceDossier.setUseName(userName);
			deviceDossier.setUseDeptName(loginDeptName);
			deviceDossier.setUseAcc(userMobile);//借用字段存领用人联系方式
			deviceDossier.setUseDeptCode((assetsDevice.getDeviceNum()-assetsDevice.getOutNum())+"");//借用字段存领用人剩余库存量
			request.setAttribute("deviceDossier", deviceDossier);
		}
		return "update";
	}
	/**
	 *  
	 * @Description：保存领用数据  
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveDeviceUse")
	public void saveDeviceUse(){
		try{
			deviceDossierService.saveDeviceUse(deviceDossier);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBDAGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBDAGL_XG", "设备档案管理_修改", "2", "0"), e);
		}
	}
	
	/**
	 * 查询数据--领用数据列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "queryUseViewList",results={@Result(name="json",type="json")})
	public void queryUseViewList(){
		AssetsDevice assetsDevice = deviceService.get(deviceDossier.getId());
		String deviceCode = assetsDevice.getDeviceCode();//拿到需要查看的入库审批记录的设备号
		List<AssetsDeviceDossier> list = deviceDossierService.queryDeviceUseView(deviceCode,deviceDossier);
		int total = deviceDossierService.getDeviceCountUseView(deviceCode,deviceDossier);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 维修数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "repairMyUse")
	public void repairMyUse(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceDossierService.repairMyUse(deviceDossier);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "确认维修成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "确认维修失败");
			logger.error("SBDAGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBDAGL_XG", "设备档案管理_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
}
