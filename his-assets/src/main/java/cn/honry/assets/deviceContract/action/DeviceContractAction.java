package cn.honry.assets.deviceContract.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import cn.honry.assets.deviceContract.service.DeviceContractService;
import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/assets/deviceContract")
@SuppressWarnings({"all"})	
public class DeviceContractAction extends ActionSupport{
	private DeviceContractService deviceContractService;
	@Autowired
	@Qualifier(value = "deviceContractService")
	public void setDeviceContractService(
			DeviceContractService deviceContractService) {
		this.deviceContractService = deviceContractService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private Logger logger=Logger.getLogger(DeviceContractAction.class);
//	@Autowired
//	@Qualifier(value = "hiasExceptionService")
//	private HIASExceptionService hiasExceptionService;
//	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
//	this.hiasExceptionService = hiasExceptionService;
//	}
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}
	private String fileServerURL;
	
	public String getFileServerURL() {
		return fileServerURL;
	}

	public void setFileServerURL(String fileServerURL) {
		this.fileServerURL = fileServerURL;
	}
	//分页
	private String page;
	private String rows;
	private String id;
	private String officeCode;
	private String officeName;
	private String className;
	private String classCode;
	private String deviceName;
	private String menuAlias;//权限
	private String serviceName;//服务名称
	private ServiceManagement serviceManagement=new ServiceManagement();
	private File fileBase;//上传文件
	private String fileBaseFileName;//上传文件名
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public File getFileBase() {
		return fileBase;
	}
	public void setFileBase(File fileBase) {
		this.fileBase = fileBase;
	}
	public String getFileBaseFileName() {
		return fileBaseFileName;
	}
	public void setFileBaseFileName(String fileBaseFileName) {
		this.fileBaseFileName = fileBaseFileName;
	}
	public ServiceManagement getServiceManagement() {
		return serviceManagement;
	}
	public void setServiceManagement(ServiceManagement serviceManagement) {
		this.serviceManagement = serviceManagement;
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
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * 跳转到设备合同管理页面
	 * @return
	 */
	@RequiresPermissions(value={"SBHTGL:function:view"})
	@Action(value = "listDeviceContract", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/deviceContract/deviceContract.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDeviceContract() {
		fileServerURL = HisParameters.getfileURI(ServletActionContext.getRequest());
		return "list";
	}
	/**  
	 * 跳转到添加合同页面
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@RequiresPermissions(value={"SBHTGL:function:add"}) 
	@Action(value = "addDeviceContract", results = { @Result(name = "add", location = "/WEB-INF/pages/assets/deviceContract/deviceContractEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addDeviceContract() {
		fileServerURL = HisParameters.getfileURI(ServletActionContext.getRequest());
		if(StringUtils.isNotBlank(id)){
			request.setAttribute("deviceContractId", id);
		}
		return "add";
	}
	
	/**  
	 * 设备合同管理列表(查询)需要查询已通过审批的设备采购申请数据
	 * @Author: zpty
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="queryDevicePurch")
	public void queryDevicePurch(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<AssetsPurch> list = deviceContractService.queryDevicePurch(officeCode,page,rows,menuAlias);
			int total = deviceContractService.queryTotalPurch(officeCode);
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBHTGL", e);
//			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBHTGL", "医疗设备管理_设备合同管理", "2", "0"), e);
		}
	}
	
	/**  
	 * 设备合同管理列表(查询)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="queryDeviceContract")
	public void queryDeviceContract(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			AssetsDeviceContract contract = new AssetsDeviceContract();
			contract.setOfficeName(officeName);
			contract.setClassCode(classCode);
			contract.setClassName(className);
			contract.setDeviceName(deviceName);
			List<AssetsDeviceContract> list = deviceContractService.queryDeviceContract(contract,page,rows,menuAlias);
			int total = deviceContractService.queryTotal(contract);
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBHTGL", e);
//			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBHTGL", "医疗设备管理_设备合同管理", "2", "0"), e);
		}
	}
	
	/**
	 * 上传文件
	 * @author  wangshujuan
	 * @Modifier：
	 * @ModifyDate：2017年11月15日 下午4:09:43
	 */
	@Action(value = "devConUpload", results = { @Result(name = "json", type = "json") })
	public void devConUpload() throws IOException {
		fileServerURL = HisParameters.getfileURI(ServletActionContext.getRequest());
		PrintWriter out = WebUtils.getResponse().getWriter();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			fileBaseFileName = uploadFileService.fileUpload(fileBase, fileBaseFileName,"0",ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			AssetsDeviceContract entity=new AssetsDeviceContract();
			//通过ID将这条记录取出来
			entity = deviceContractService.get(id);
			entity.setAttach(fileBaseFileName);
			entity.setState(1);//已上传
			deviceContractService.save(entity);
			map.put("fileServerURL", fileServerURL);
			map.put("fileName", fileBaseFileName);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YLSBGL_SBHTGL", e);
//			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBHTGL", "医疗设备管理_设备合同管理", "2", "0"), e);
			out.write("error");
		}
	}
}