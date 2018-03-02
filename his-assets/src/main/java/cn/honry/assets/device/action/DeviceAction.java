package cn.honry.assets.device.action;

import java.util.Date;
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

import cn.honry.assets.device.service.DeviceService;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description 设备入库管理action
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
@Namespace(value = "/assets/device")
public class DeviceAction extends ActionSupport implements ModelDriven<AssetsDevice>{

	private static final long serialVersionUID = 1L;

	@Override
	public AssetsDevice getModel() {
		return device;
	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private DeviceService deviceService;
	private AssetsDevice device  = new AssetsDevice();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	public DeviceService getDeviceService() {
		return deviceService;
	}
	@Autowired
	@Qualifier(value="deviceService")
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	private Logger logger=Logger.getLogger(DeviceAction.class);

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
	@RequiresPermissions(value={"SBRKGL:function:view"})
	@Action(value = "queryAssetsDeviceUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/device/deviceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDeviceUrl(){
		return "list";
	}
	
	/**
	 * 查询数据--审批
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBRKSPGL:function:view"})
	@Action(value = "queryDeviceExamination", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/device/deviceExaminationList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryDeviceExamination(){
		return "list";
	}
	
	@Action(value = "deviceExamination", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/device/deviceExamination.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceExamination(){
		return "list";
	}
	
	@Action(value = "deviceStorage", results = { @Result(name = "storage", location = "/WEB-INF/pages/assets/device/deviceStorage.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceStorage(){
		return "storage";
	}
	
	@Action(value = "deviceDraft", results = { @Result(name = "draft", location = "/WEB-INF/pages/assets/device/deviceDraft.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceDraft(){
		return "draft";
	}
	
	@Action(value = "deviceApproval", results = { @Result(name = "approval", location = "/WEB-INF/pages/assets/device/deviceApproval.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceApproval(){
		return "approval";
	}
	
	@Action(value = "deviceNotApp", results = { @Result(name = "notApp", location = "/WEB-INF/pages/assets/device/deviceNotApp.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deviceNotApp(){
		return "notApp";
	}
	
	@Action(value = "queryAssetsDeviceStorage",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceStorage(){
		List<AssetsDevice> list = deviceService.queryDeviceStorage(device);
		int total = deviceService.getDeviceCountStorage(device);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "queryAssetsDeviceDraft",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceDraft(){
		List<AssetsDevice> list = deviceService.queryDeviceDraft(device);
		int total = deviceService.getDeviceCountDraft(device);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "queryAssetsDeviceApproval",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceApproval(){
		List<AssetsDevice> list = deviceService.queryDeviceApproval(device);
		int total = deviceService.getDeviceCountApproval(device);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "queryAssetsDeviceNotApp",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceNotApp(){
		List<AssetsDevice> list = deviceService.queryDeviceNotApp(device);
		int total = deviceService.getDeviceCountNotApp(device);
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
	@RequiresPermissions(value={"SBRKGL:function:add"})
	@Action(value = "saveDeviceUrl", results = { @Result(name = "save", location = "/WEB-INF/pages/assets/device/deviceEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrupdateDeviceUrl(){
		if(StringUtils.isNotBlank(device.getId())){
			request.setAttribute("device", deviceService.get(device.getId()));
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
	@RequiresPermissions(value={"SBRKGL:function:edit"})
	@Action(value = "updateDeviceUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/device/deviceEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateDeviceUrl(){
		if(StringUtils.isNotBlank(device.getId())){
			request.setAttribute("device", deviceService.get(device.getId()));
		}
		return "update";
	}
	@Action(value = "saveOrupdateDevice")
	public void saveOrupdateDevice(){
		try{
			AssetsDevice deviceSave=new AssetsDevice();
			if(StringUtils.isNotBlank(device.getId())){
				deviceSave = deviceService.get(device.getId());//先用ID查出该数据
			}
			deviceSave.setOfficeCode(device.getOfficeCode());//办公用途编码
			deviceSave.setOfficeName(device.getOfficeName());//办公用途名称
			deviceSave.setClassCode(device.getClassCode());//设备分类编码
			deviceSave.setClassName(device.getClassName());//设备分类名称
			deviceSave.setDeviceNo(device.getDeviceNo());//设备条码号
			deviceSave.setDeviceCode(device.getDeviceCode());//设备代码
			deviceSave.setDeviceName(device.getDeviceName());//设备名称
			deviceSave.setMeterUnit(device.getMeterUnit());//计量单位
			deviceSave.setPurchPrice(device.getPurchPrice());//采购单价(元)
			deviceSave.setDeviceNum(device.getDeviceNum());//入库数量
			deviceSave.setPurchTotal(device.getPurchTotal());//采购总价(元)
			deviceSave.setDeviceModeCode(device.getDeviceModeCode());//入库方式编码
			deviceSave.setDeviceModeName(device.getDeviceModeName());//入库方式名称
			deviceSave.setApplAcc(device.getApplAcc());//申报人工号
			deviceSave.setApplName(device.getApplName());//申报人姓名
			deviceSave.setDepreciation(device.getDepreciation());//折旧年限
			deviceSave.setDeviceState(device.getDeviceState());//入库状态(0草稿1申请、待审核2未批准3已入库)
			deviceSave.setDepotCode(device.getDepotCode());//所属仓库code
			deviceSave.setDepotName(device.getDepotName());//所属仓库name
			if(device.getDeviceState()==1){
				deviceSave.setApplDate(new Date());//申报时间
			}
			deviceService.saveOrupdata(deviceSave);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBRKGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBRKGL_XG", "设备入库管理_修改", "2", "0"), e);
		}
	}
	/**
	 * 删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBRKGL:function:delete"})
	@Action(value = "deleteDevice")
	public void deleteDevice(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceService.delete(device);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败");
			logger.error("SBRKGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBRKGL_XG", "设备入库管理_修改", "2", "0"), e);
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
	@RequiresPermissions(value={"SBRKGL:function:query"})
	@Action(value = "viewAssetsDevice", results = { @Result(name = "view", location = "/WEB-INF/pages/assets/device/deviceView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAssetsDevice(){
		request.setAttribute("device", deviceService.get(device.getId()));
		return "view";
	}
	/**
	 * @Description:返回供应商信息下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryDeviceList",results={@Result(name="json",type="json")})
	public void queryDeviceList() throws Exception{
		List<AssetsDevice> list = deviceService.findAll();
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
	@Action(value = "queryDeviceMapList",results={@Result(name="json",type="json")})
	public void queryDeviceMapList() throws Exception{
		List<AssetsDevice> list = deviceService.findAll();
		String json = JSONUtils.toExposeJson(list, false, null, "code","name");
		WebUtils.webSendJSON(json);
	}
	
	public AssetsDevice getDevice() {
		return device;
	}
	public void setDevice(AssetsDevice device) {
		device.setDel_flg(0);
		device.setStop_flg(0);
		this.device = device;
	}
	

	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "findAllDevice")
	public String findAllDevice(){
		List<AssetsDevice> AssetsDeviceList=deviceService.findAll();
		String json = JSONUtils.toExposeJson(AssetsDeviceList, false, null, "code","name");
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
		String data = deviceService.verification(device);
		String json = JSONUtils.toJson(data);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 不通过数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "reasonView", results = { @Result(name = "reasonView", location = "/WEB-INF/pages/assets/device/deviceScrap.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String reasonView(){
		return "reasonView";
	}
	@Action(value = "disableDevice")
	public void disableDevice(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceService.disableDevice(device);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "审批成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "审批失败");
			logger.error("SBRKSPGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBRKSPGL_XG", "设备入库审批管理_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
	/**
	 * 通过数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "enableDevice")
	public void enableDevice(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceService.enableDevice(device);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "审批成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "审批失败");
			logger.error("SBRKSPGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBRKSPGL_XG", "设备入库审批管理_修改", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	
}
