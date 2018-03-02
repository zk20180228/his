package cn.honry.assets.deviceseRvice.action;

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

import cn.honry.assets.deviceseRvice.service.DeviceseRviceService;
import cn.honry.base.bean.model.AssetsDeviceseRvice;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description 设备维护action
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
@Namespace(value = "/assets/deviceseRvice")
public class DeviceseRviceAction extends ActionSupport implements ModelDriven<AssetsDeviceseRvice>{

	private static final long serialVersionUID = 1L;

	@Override
	public AssetsDeviceseRvice getModel() {
		return deviceseRvice;
	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private DeviceseRviceService deviceseRviceService;
	private AssetsDeviceseRvice deviceseRvice  = new AssetsDeviceseRvice();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	public DeviceseRviceService getDeviceseRviceService() {
		return deviceseRviceService;
	}
	@Autowired
	@Qualifier(value="deviceseRviceService")
	public void setDeviceseRviceService(DeviceseRviceService deviceseRviceService) {
		this.deviceseRviceService = deviceseRviceService;
	}
	
	private Logger logger=Logger.getLogger(DeviceseRviceAction.class);

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
	@RequiresPermissions(value={"SBWH:function:view"})
	@Action(value = "queryAssetsDeviceseRviceUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceseRvice/deviceseRviceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDeviceseRviceUrl(){
		return "list";
	}
	@Action(value = "queryAssetsDeviceseRvice",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceseRvice(){
		List<AssetsDeviceseRvice> list = deviceseRviceService.queryDeviceseRvice(deviceseRvice);
		int total = deviceseRviceService.getDeviceseRviceCount(deviceseRvice);
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
	@RequiresPermissions(value={"SBWH:function:add"})
	@Action(value = "saveDeviceseRviceUrl", results = { @Result(name = "save", location = "/WEB-INF/pages/assets/deviceseRvice/deviceseRviceEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrupdateDeviceseRviceUrl(){
		if(StringUtils.isNotBlank(deviceseRvice.getId())){
			request.setAttribute("deviceseRvice", deviceseRviceService.get(deviceseRvice.getId()));
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
	@RequiresPermissions(value={"SBWH:function:edit"})
	@Action(value = "updateDeviceseRviceUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/deviceseRvice/deviceseRviceEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateDeviceseRviceUrl(){
		if(StringUtils.isNotBlank(deviceseRvice.getId())){
			request.setAttribute("deviceseRvice", deviceseRviceService.get(deviceseRvice.getId()));
		}
		return "update";
	}
	@Action(value = "saveOrupdateDeviceseRvice")
	public void saveOrupdateDeviceseRvice(){
		try{
			AssetsDeviceseRvice deviceseRviceSave=new AssetsDeviceseRvice();
			if(StringUtils.isNotBlank(deviceseRvice.getId())){
				deviceseRviceSave = deviceseRviceService.get(deviceseRvice.getId());//先用ID查出该数据
			}
			deviceseRviceSave.setOfficeCode(deviceseRvice.getOfficeCode());//办公用途编码
			deviceseRviceSave.setOfficeName(deviceseRvice.getOfficeName());//办公用途名称
			deviceseRviceSave.setClassCode(deviceseRvice.getClassCode());//设备分类编码
			deviceseRviceSave.setClassName(deviceseRvice.getClassName());//设备分类名称
			deviceseRviceSave.setDeviceNo(deviceseRvice.getDeviceNo());//设备条码号
			deviceseRviceSave.setDeviceCode(deviceseRvice.getDeviceCode());//设备代码
			deviceseRviceSave.setDeviceName(deviceseRvice.getDeviceName());//设备名称
			deviceseRviceSave.setMeterUnit(deviceseRvice.getMeterUnit());//计量单位
			deviceseRviceService.saveOrupdata(deviceseRviceSave);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBWH_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBWH_XG", "设备维护_修改", "2", "0"), e);
		}
	}
	/**
	 * 删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBWH:function:delete"})
	@Action(value = "deleteDeviceseRvice")
	public void deleteDeviceseRvice(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceseRviceService.delete(deviceseRvice);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败");
			logger.error("SBWH_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBWH_XG", "设备维护_修改", "2", "0"), e);
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
	@RequiresPermissions(value={"SBWH:function:query"})
	@Action(value = "viewAssetsDeviceseRvice", results = { @Result(name = "view", location = "/WEB-INF/pages/assets/deviceseRvice/deviceseRviceView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAssetsDeviceseRvice(){
		request.setAttribute("deviceseRvice", deviceseRviceService.get(deviceseRvice.getId()));
		return "view";
	}
	/**
	 * @Description:返回设备下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryDeviceseRviceList",results={@Result(name="json",type="json")})
	public void queryDeviceseRviceList() throws Exception{
		List<AssetsDeviceseRvice> list = deviceseRviceService.findAll();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:返回设备下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryDeviceseRviceMapList",results={@Result(name="json",type="json")})
	public void queryDeviceseRviceMapList() throws Exception{
		List<AssetsDeviceseRvice> list = deviceseRviceService.findAll();
		String json = JSONUtils.toExposeJson(list, false, null, "code","name");
		WebUtils.webSendJSON(json);
	}
	
	public AssetsDeviceseRvice getDeviceseRvice() {
		return deviceseRvice;
	}
	public void setDeviceseRvice(AssetsDeviceseRvice deviceseRvice) {
		deviceseRvice.setDel_flg(0);
		deviceseRvice.setStop_flg(0);
		this.deviceseRvice = deviceseRvice;
	}
	

	/**
	 * 查出所有数据----用作设备查询
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "findAllDeviceseRvice")
	public String findAllDeviceseRvice(){
		List<AssetsDeviceseRvice> AssetsDeviceseRviceList=deviceseRviceService.findAll();
		String json = JSONUtils.toExposeJson(AssetsDeviceseRviceList, false, null, "deviceCode","deviceName");
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
		String data = deviceseRviceService.verification(deviceseRvice);
		String json = JSONUtils.toJson(data);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:返回办公用途下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryOfficeMapList",results={@Result(name="json",type="json")})
	public void queryOfficeMapList() throws Exception{
		List<AssetsDeviceseRvice> list = deviceseRviceService.findOffice();
		String json = JSONUtils.toExposeJson(list, false, null, "officeCode","officeName");
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:返回设备分类下拉列表
	 * @author zpty
	 * @date 2017-11-14
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryClassMapList",results={@Result(name="json",type="json")})
	public void queryClassMapList() throws Exception{
		List<AssetsDeviceseRvice> list = deviceseRviceService.findClass();
		String json = JSONUtils.toExposeJson(list, false, null, "classCode","className");
		WebUtils.webSendJSON(json);
	}
}
