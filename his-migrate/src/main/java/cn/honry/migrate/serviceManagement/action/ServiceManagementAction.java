package cn.honry.migrate.serviceManagement.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.serviceManagement.service.ServiceManagementService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/migrate/ServiceManagement")
@SuppressWarnings({"all"})	
public class ServiceManagementAction extends ActionSupport{
	private ServiceManagementService serviceManagementService;
	@Autowired
	@Qualifier(value = "serviceManagementService")
	public void setServiceManagementService(
			ServiceManagementService serviceManagementService) {
		this.serviceManagementService = serviceManagementService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private Logger logger=Logger.getLogger(ServiceManagementAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	//分页
	private String page;
	private String rows;
	private String code;
	private String menuAlias;//权限
	private String serviceName;//服务名称
	private ServiceManagement serviceManagement=new ServiceManagement();
	private String ip;
	private String port;
	private String system;
	private String id;
	private String state;
	private String serviceType;
	private String serviceState;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceState() {
		return serviceState;
	}
	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	/**
	 * 跳转到服务管理页面
	 * @return
	 */
	@RequiresPermissions(value={"FWGL:function:view"})
	@Action(value = "listServiceManagement", results = { @Result(name = "list", location = "/WEB-INF/pages/migrate/serviceManagement/serviceManagement.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listServiceManagement() {
		return "list";
	}
	/**  
	 * 服务管理列表(查询)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	
	@Action(value="queryServiceManagement")
	public void queryServiceManagement(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ServiceManagement> list = serviceManagementService.queryServiceManagement(code,page,rows,menuAlias,serviceType,serviceState);
			int total = serviceManagementService.queryTotal(code);
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("CSJKGL_FWGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_FWGL", "厂商接口管理_服务管理", "2", "0"), e);
		}
	}
	/**  
	 * 服务管理 (跳转到添加页面)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@RequiresPermissions(value={"FWGL:function:add"}) 
	@Action(value = "addServiceManagement", results = { @Result(name = "add", location = "/WEB-INF/pages/migrate/serviceManagement/serviceManagementEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addServiceManagement() {
		String	time = null;
		if(StringUtils.isNotBlank(code)){
			serviceManagement=serviceManagementService.getOnedata(code);
			if(serviceManagement!=null){
				if(serviceManagement.getName()!=null){
					serviceManagement.setName(serviceManagement.getName().substring(0,serviceManagement.getName().lastIndexOf("-")!=-1?serviceManagement.getName().lastIndexOf("-"):serviceManagement.getName().length()));
				}
				if(serviceManagement.getCode()!=null){
					serviceManagement.setCode(serviceManagement.getCode().substring(0,serviceManagement.getCode().lastIndexOf("-")!=-1?serviceManagement.getCode().lastIndexOf("-"):serviceManagement.getCode().length()));
				}
			}
			
			if(serviceManagement.getHeartNewtime()==null){
				time = DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				time = DateUtils.formatDateY_M_D_H_M_S(serviceManagement.getHeartNewtime());
			}
		}else{
			time = DateUtils.formatDateY_M_D_H_M_S(new Date());
		}
		ServletActionContext.getRequest().setAttribute("serviceManagement", serviceManagement);
		ServletActionContext.getRequest().setAttribute("heartNewtime", time);
		return "add";
	}
	@RequiresPermissions(value={"FWGL:function:view"}) 
	@Action(value = "viewServiceManagement", results = { @Result(name = "view", location = "/WEB-INF/pages/migrate/serviceManagement/serviceManagementView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewServiceManagement() {
		String	time = null;
		if(StringUtils.isNotBlank(code)){
			serviceManagement=serviceManagementService.getOnedata(code);
//			serviceManagement.setName(serviceManagement.getName().substring(0,serviceManagement.getName().lastIndexOf("-")));
			if(serviceManagement.getHeartNewtime()==null){
				time = DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				time = DateUtils.formatDateY_M_D_H_M_S(serviceManagement.getHeartNewtime());
			}
		}else{
			time = DateUtils.formatDateY_M_D_H_M_S(new Date());
		}
		ServletActionContext.getRequest().setAttribute("serviceManagement", serviceManagement);
		ServletActionContext.getRequest().setAttribute("heartNewtime", time);
		return "view";
	}
	/**  
	 * 服务管理 (添加/修改)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@RequiresPermissions(value={"FWGL:function:add"})
	@Action(value="saveServiceManagement")
	public void saveServiceManagement(){
		Map<String,String> map=new HashMap<String,String>();
		try {
			map=serviceManagementService.saveServiceManagement(serviceManagement);
			if(map.size()==0){
				map.put("resCode", "success");
				map.put("resMsg", "保存成功！");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 服务管理 (更新)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="delServiceManagement",results={@Result(name="json",type="json")})
	public void delServiceManagement () throws Exception{
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(state)){
				serviceManagementService.delServiceManagement(id,state);
				String returnMsg="停用";
				if(!"1".equals(state)){
					returnMsg="启用";
				}
				resMap.put("resCode", "success");
				resMap.put("resMsg", returnMsg+"成功!");
			}else{
				throw new Exception("参数不全");
			}
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "修改状态失败!");
			logger.error("CSJKGL_FWGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_FWGL", "厂商接口管理_服务管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>删除 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月25日 上午10:26:57 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月25日 上午10:26:57 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="delService",results={@Result(name="json",type="json")})
	public void delService () {
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			serviceManagementService.delService(code);
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
			logger.error("CSJKGL_FWGL", e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}

	/**
	 * 
	 * 
	 * <p>重启服务 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月25日 下午8:50:54 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月25日 下午8:50:54 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="reloadService",results={@Result(name="json",type="json")})
	public void reloadService () {
		ServiceManagement manage=serviceManagementService.getOnedata(code);
//		if(StringUtils.isNotBlank(manage.getIp())&&StringUtils.isNotBlank(manage.get))
//		ClientServiceUtils.clientMachine(null, null, null, null, 1);
		Map<String,String> resMap=new HashMap<String,String>();
		resMap.put("resCode", "success");
		resMap.put("resMsg", "功能未完成!");
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	
//	public void service(){
//		Map<String,String> map=new HashMap<String,String>();
//		try{
//			InetAddress addr = InetAddress.getLocalHost();
//	        String myIp = addr.getHostAddress().toString(); //获取本机ip 判断是否Ip相同
//	        Integer portNet=request.getServerPort();
//	        if(myIp.equals(ip)&&Integer.valueOf(port)==portNet){
//		        String osName= System.getProperty("os.name");//操作系统名称
//		        String realPath=System.getProperty("catalina.home");//tomcate 路径
//		        String shutdown="shutdown.bat";
//		        String startup="startup.bat";
//		        String reloadName="reload.bat";
//		        if("WIN".indexOf(osName.toUpperCase())!=-1){
//		        	shutdown="shutdown.sh";
//			        startup="startup.sh";
//			        reloadName="reload.sh";
//		        }
//		        String realBinPath=realPath+File.separator+"bin"+File.separator;
//		        String reloadBATPath=realBinPath+"reload.bat";
//		        File reloadBAT=new File(reloadBATPath);
//		        if(!reloadBAT.exists()){//判断reaload文件是否存在 如果不存在 则从shutdown重写
//		        	FileReader shudownBAT=new FileReader(new File(realBinPath+shutdown));
//		        	BufferedReader  buffer=new BufferedReader(shudownBAT);
//		        	String read=null;
//		        	List<String> shudownList=new ArrayList<>();
//		        	while((read=buffer.readLine())!=null){
//		        		shudownList.add(read);//放入到list中
//		        	}
//		        	buffer.close();
//		        	if(new File(realBinPath+startup).exists()){
//		        		shudownList.add(shudownList.size()-1,startup);
//		        	}
//		        	BufferedWriter bufferW=new BufferedWriter(new FileWriter(realBinPath+reloadName,false));
//		        	for(String reloadContext:shudownList){
//		        		bufferW.write(reloadContext);
//		        		bufferW.newLine();
//		        	}
//		        	bufferW.flush();
//		        	bufferW.close();
//		        }
//				Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
//				Process ps = null;
//				String pan=realPath.substring(0, realPath.indexOf(File.separator));
//				if("WIN".indexOf(osName.toUpperCase())==-1){
//					ps=rt.exec("cmd /c "+pan+"&&cd "+(realPath+File.separator+"bin")+"&&"+reloadName+"&&echo 执行成功>>log.txt");
//				}else{
//					ps=rt.exec("chmod 777 "+realPath+File.separator+"bin"+File.separator+reloadName);
//				}
//				ps.waitFor();  //等待子进程完成再往下执行。
//	        }else{
//	        	String serverName=request.getContextPath();
//	        	HttpSession session=request.getSession();
//	        	String sessionName =(String) session.getAttribute("");
//	        	String returnString=serviceManagementService.sendRequest(ip, port, serverName);
//	        }
//	        map.put("resCode", "success");
//			map.put("resMsg", "重启成功!");
//		}catch(Exception e){
//			 map.put("resCode", "error");
//			 map.put("resMsg", "重启失败!");
//			 e.printStackTrace();
//		}
//		String json=JSONUtils.toJson(map);
//		WebUtils.webSendJSON(json);
//	}
	/**  
	 * ping方法
	 * @Author: donghe
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="pingAction")
	public void pingAction () throws Exception{
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			boolean boolean1 = PingIpUtils.ping(ip, 5, 5000,system);
			if(boolean1){
				resMap.put("resCode", "success");
				resMap.put("resMsg", "网络畅通!");
			}else{
				resMap.put("resCode", "success");
				resMap.put("resMsg", "网络断开!");
			}
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "网络断开!");
			logger.error("CSJKGL_FWGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_FWGL", "厂商接口管理_服务管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}

}