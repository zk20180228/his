package cn.honry.assets.deviceCode.action;

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

import cn.honry.assets.deviceCode.service.DeviceCodeService;
import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description 设备条码管理action
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
@Namespace(value = "/assets/deviceCode")
public class DeviceCodeAction extends ActionSupport implements ModelDriven<AssetsDeviceCode>{

	private static final long serialVersionUID = 1L;

	@Override
	public AssetsDeviceCode getModel() {
		return deviceCode;
	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private DeviceCodeService deviceCodeService;
	private AssetsDeviceCode deviceCode  = new AssetsDeviceCode();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	public DeviceCodeService getDeviceCodeService() {
		return deviceCodeService;
	}
	@Autowired
	@Qualifier(value="deviceCodeService")
	public void setDeviceCodeService(DeviceCodeService deviceCodeService) {
		this.deviceCodeService = deviceCodeService;
	}
	
	private Logger logger=Logger.getLogger(DeviceCodeAction.class);

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
	@RequiresPermissions(value={"SBTMGL:function:view"})
	@Action(value = "queryAssetsDeviceCodeUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceCode/deviceCodeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryAssetsDeviceCodeUrl(){
		return "list";
	}
	@Action(value = "queryAssetsDeviceCode",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceCode(){
		List<AssetsDeviceCode> list = deviceCodeService.queryDeviceCode(deviceCode);
		int total = deviceCodeService.getDeviceCodeCount(deviceCode);
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
	@RequiresPermissions(value={"SBTMGL:function:add"})
	@Action(value = "saveDeviceCodeUrl", results = { @Result(name = "save", location = "/WEB-INF/pages/assets/deviceCode/deviceCodeEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrupdateDeviceCodeUrl(){
		if(StringUtils.isNotBlank(deviceCode.getId())){
			request.setAttribute("deviceCode", deviceCodeService.get(deviceCode.getId()));
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
	@RequiresPermissions(value={"SBTMGL:function:edit"})
	@Action(value = "updateDeviceCodeUrl", results = { @Result(name = "update", location = "/WEB-INF/pages/assets/deviceCode/deviceCodeEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updateDeviceCodeUrl(){
		if(StringUtils.isNotBlank(deviceCode.getId())){
			request.setAttribute("deviceCode", deviceCodeService.get(deviceCode.getId()));
		}
		return "update";
	}
	@Action(value = "saveOrupdateDeviceCode")
	public void saveOrupdateDeviceCode(){
		try{
			AssetsDeviceCode deviceCodeSave=new AssetsDeviceCode();
			if(StringUtils.isNotBlank(deviceCode.getId())){
				deviceCodeSave = deviceCodeService.get(deviceCode.getId());//先用ID查出该数据
			}
			deviceCodeSave.setOfficeCode(deviceCode.getOfficeCode());//办公用途编码
			deviceCodeSave.setOfficeName(deviceCode.getOfficeName());//办公用途名称
			deviceCodeSave.setClassCode(deviceCode.getClassCode());//设备分类编码
			deviceCodeSave.setClassName(deviceCode.getClassName());//设备分类名称
			deviceCodeSave.setDeviceNo(deviceCode.getDeviceNo());//设备条码号
			deviceCodeSave.setDeviceCode(deviceCode.getDeviceCode());//设备代码
			deviceCodeSave.setDeviceName(deviceCode.getDeviceName());//设备名称
			deviceCodeSave.setMeterUnit(deviceCode.getMeterUnit());//计量单位
			deviceCodeSave.setPurchPrice(deviceCode.getPurchPrice());//采购单价(元)
			deviceCodeSave.setState(0);//打印--每次修改都要变成未打印状态
			deviceCodeService.saveOrupdata(deviceCodeSave);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SBTMGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBTMGL_XG", "设备条码管理_修改", "2", "0"), e);
		}
	}
	/**
	 * 删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@RequiresPermissions(value={"SBTMGL:function:delete"})
	@Action(value = "deleteDeviceCode")
	public void deleteDeviceCode(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceCodeService.delete(deviceCode);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败");
			logger.error("SBTMGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBTMGL_XG", "设备条码管理_修改", "2", "0"), e);
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
	@RequiresPermissions(value={"SBTMGL:function:query"})
	@Action(value = "viewAssetsDeviceCode", results = { @Result(name = "view", location = "/WEB-INF/pages/assets/deviceCode/deviceCodeView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAssetsDeviceCode(){
		request.setAttribute("deviceCode", deviceCodeService.get(deviceCode.getId()));
		return "view";
	}
	
	/**
	 * 打印数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "disableDeviceCode")
	public void disableDeviceCode(){
		Map<String, String> retMap=new HashMap<String, String>();
		try {
			deviceCodeService.disableDeviceCode(deviceCode);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "打印成功");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "打印失败");
			logger.error("SBTMGL_XG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SBTMGL_XG", "设备条码管理_修改", "2", "0"), e);
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
	@Action(value = "queryDeviceCodeList",results={@Result(name="json",type="json")})
	public void queryDeviceCodeList() throws Exception{
		List<AssetsDeviceCode> list = deviceCodeService.findAll();
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
	@Action(value = "queryDeviceCodeMapList",results={@Result(name="json",type="json")})
	public void queryDeviceCodeMapList() throws Exception{
		List<AssetsDeviceCode> list = deviceCodeService.findAll();
		String json = JSONUtils.toExposeJson(list, false, null, "code","name");
		WebUtils.webSendJSON(json);
	}
	
	public AssetsDeviceCode getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(AssetsDeviceCode deviceCode) {
		deviceCode.setDel_flg(0);
		deviceCode.setStop_flg(0);
		this.deviceCode = deviceCode;
	}
	

	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Action(value = "findAllDeviceCode")
	public String findAllDeviceCode(){
		List<AssetsDeviceCode> AssetsDeviceCodeList=deviceCodeService.findAll();
		String json = JSONUtils.toExposeJson(AssetsDeviceCodeList, false, null, "code","name");
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
		String data = deviceCodeService.verification(deviceCode);
		String json = JSONUtils.toJson(data);
		WebUtils.webSendJSON(json);
	}
}
