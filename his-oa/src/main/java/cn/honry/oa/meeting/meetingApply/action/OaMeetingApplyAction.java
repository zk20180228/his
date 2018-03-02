package cn.honry.oa.meeting.meetingApply.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.OaMeetingApply;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.meeting.meetingApply.service.OaMeetingApplyService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/meeting/meetingApply")
@SuppressWarnings({"all"})
public class OaMeetingApplyAction extends ActionSupport implements
		ModelDriven<OaMeetingApply> {

	private OaMeetingApply oaMeetingApply = new OaMeetingApply();
	
	public OaMeetingApply getOaMeetingApply() {
		return oaMeetingApply;
	}
	public void setOaMeetingApply(OaMeetingApply oaMeetingApply) {
		this.oaMeetingApply = oaMeetingApply;
	}
	
	private String meetingDescribe;

	public String getMeetingDescribe() {
		return meetingDescribe;
	}
	public void setMeetingDescribe(String meetingDescribe) {
		this.meetingDescribe = meetingDescribe;
	}

	private String fileServersURL;
	
	public void setFileServersURL(String fileServersURL) {
		this.fileServersURL = fileServersURL;
	}

	public String getFileServersURL() {
		return fileServersURL;
	}

	private String findType;//显示查找类型
	
	public String getFindType() {
		return findType;
	}
	public void setFindType(String findType) {
		this.findType = findType;
	}
	/**附件*/
	private List<File> upload;
	private List<String> uploadFileName;
	private List<String> oldFileURL;
	private List<String> oldFileName;
	
	
	public List<String> getOldFileURL() {
		return oldFileURL;
	}
	public void setOldFileURL(List<String> oldFileURL) {
		this.oldFileURL = oldFileURL;
	}
	public List<String> getOldFileName() {
		return oldFileName;
	}
	public void setOldFileName(List<String> oldFileName) {
		this.oldFileName = oldFileName;
	}
	public List<File> getUpload() {
		return upload;
	}
	public void setUpload(List<File> upload) {
		this.upload = upload;
	}
	public List<String> getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(List<String> uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	@Autowired
	@Qualifier(value = "oaMeetingApplyService")
	private OaMeetingApplyService oaMeetingApplyService;
	
	public void setOaMeetingApplyService(OaMeetingApplyService oaMeetingApplyService) {
		this.oaMeetingApplyService = oaMeetingApplyService;
	}
	
	
	private List<OaMeetingApply> oaMeetingApplyList;
	
	private String page;
	private String rows;

	private String acount;
	
	/**
	 * 当前模块的业务一部分和院长办公室业务重叠，在这里我加一个标记，用来区分不同业务
	 */
	private String flag;//当flag="YZBGS"时，走院长办公室的流程
	
	public String getAcount() {
		return acount;
	}
	public void setAcount(String acount) {
		this.acount = acount;
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
	@Override
	public OaMeetingApply getModel() {
//		return null;
		return oaMeetingApply;
	}
	/**
	 * 会议申请页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月18日 上午9:24:49 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月18日 上午9:24:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "showMeetingApply", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingApply/meetingApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMeetingApply(){
		return "list";
	}

	/**
	 * 显示弹出框
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月26日 上午11:06:01 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月26日 上午11:06:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 * @param:	findType
	 * 			0:查看范围
	 * 			1:内部出席人员
	 *
	 */
	@Action(value = "toAuth", results = { @Result(name = "auth", location = "/WEB-INF/pages/oa/meeting/meetingApply/meetingAuth.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAuth(){
		
		if(StringUtils.isNotBlank(flag)){//flag是为了区别是否为院长办公室业务
			ServletActionContext.getRequest().setAttribute("flag",flag);
		}
		
		
		return "auth";
	}
	/**
	 * 会议审批页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 上午11:32:27 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 上午11:32:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "showMeetingApprove", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingApprove/meetingApprove.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMeetingApprove(){
		return "list";
	}
	/**
	 * 查询会议申请
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月18日 上午9:26:52 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月18日 上午9:26:52 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "queryMeetingApply")
	public void queryMeetingApply(){
		try{
//			oaMeetingApply.setApplyFLag("1");//会议申请标识
			int total = oaMeetingApplyService.getTotal(oaMeetingApply);
			oaMeetingApplyList = oaMeetingApplyService.query(oaMeetingApply);
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("total", total);
			map.put("rows", oaMeetingApplyList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 填写会议申请
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月18日 上午9:27:54 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月18日 上午9:27:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "addMeetingApply", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/meeting/meetingApply/addMeetingApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addMeetingApply(){
		fileServersURL = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
		acount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(); 
		
		if(StringUtils.isNotBlank(flag)){//flag是为了区别是否为院长办公室业务
			ServletActionContext.getRequest().setAttribute("flag",flag);
		}
		
		return "add";
	}
	
	/**
	 * 保存会议申请信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月18日 下午6:14:09 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月18日 下午6:14:09 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "saveMeetingApply")
	public void saveMeetingApply(){
		try{
//			System.err.println(upload);
//			System.err.println(uploadFileName);
			List<String> fileurl = new ArrayList<String>();
			if(upload!=null&&upload.size()>0){
				for (int i = 0; i < upload.size(); i++) {
					if(upload.get(i)!=null){
						FastDFSClient client = new FastDFSClient();
						String fileUpload = client.uploadFile(upload.get(i), uploadFileName.get(i));
//						String fileUpload = uploadFileService.fileUpload(upload.get(i), uploadFileName.get(i), "1", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						fileurl.add(fileUpload);
					}
				}
			}
			if(oldFileURL!=null&&oldFileURL.size()>=1&&StringUtils.isNotBlank(oldFileURL.get(0))){
				fileurl.addAll(oldFileURL);
				if(uploadFileName==null){
					uploadFileName = new ArrayList<String>();
				}
				uploadFileName.addAll(oldFileName);
			}
			String furl = "";
			String fname = "";
			for (String string : fileurl) {
				if(StringUtils.isNoneBlank(furl)){
					furl += ";";
				}
				furl += string;
			}
			if(uploadFileName!=null){
				for (String string : uploadFileName) {
					if(StringUtils.isNoneBlank(fname)){
						fname += ";";
					}
					fname += string;
				}
			}
			oaMeetingApply.setMeetingFile(furl);
			oaMeetingApply.setMeetingFileName(fname);
			//判断该会议室申请时间是否已经被使用
			String checkHaveUsed = oaMeetingApplyService.checkHaveUsed(oaMeetingApply);
			if("ok".equals(checkHaveUsed)){
				oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"saveOrUpdate",flag);
			}else{
				WebUtils.webSendString(checkHaveUsed.split(",")[0]+"至"+checkHaveUsed.split(",")[1]+"时间段有会议冲突");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断会议室是否被占用
	 */
	@Action(value = "checkHaveUsed")
	public void checkHaveUsed(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String meetId = request.getParameter("meetId");
			String id = request.getParameter("id");
			String msTime = request.getParameter("msTime");
			String meTime = request.getParameter("meTime");
			String meetingPeriodicity = request.getParameter("meetingPeriodicity");
			OaMeetingApply oma = new OaMeetingApply();
			if(StringUtils.isNotBlank(msTime)&&StringUtils.isNotBlank(meTime)){
				oma.setMeetingStarttime(DateUtils.parseDateY_M_D_H_M_S(msTime));
				oma.setMeetingEndtime(DateUtils.parseDateY_M_D_H_M_S(meTime));
				oma.setMeetId(meetId);
				oma.setMeetingPeriodicity(meetingPeriodicity);
				oma.setId(id);
				oma.setAppointmentFLag("1");//设置预约标记。表示这是查询预约会议
			}else{
				WebUtils.webSendJSON("时间不全");
			}
			String checkHaveUsed = oaMeetingApplyService.checkHaveUsed(oma);
			if("ok".equals(checkHaveUsed)){
				WebUtils.webSendString("ok");
			}else{
				WebUtils.webSendString(checkHaveUsed.split(",")[0]+"至"+checkHaveUsed.split(",")[1]+"时间段有会议冲突");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除会议申请
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月18日 下午6:15:55 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月18日 下午6:15:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "delMeetingApply", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingApply/meetingApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delMeetingApply(){
		try{
			oaMeetingApplyService.del(oaMeetingApply.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "list";
	}
	
	/**
	 * 撤销
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 上午10:46:57 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 上午10:46:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "redoMeetingApply", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingApply/meetingApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String redoMeetingApply(){
		try{
			String ids = oaMeetingApply.getId();
			while(ids.contains(",")){
				oaMeetingApply.setId(ids.split(",")[0]);
				oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"redo",null);
				ids = ids.substring(ids.indexOf(",")+1, ids.length());
			}
			oaMeetingApply.setId(ids);
			oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"redo",flag);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "list";
	}
	
	/**
	 * 会议审批通过
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 下午1:59:07 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 下午1:59:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "approveOk", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingApprove/meetingApprove.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String approveOk(){
		try{
			String ids = oaMeetingApply.getId();
			while(ids.contains(",")){
				oaMeetingApply.setId(ids.split(",")[0]);
				oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"approveOk",null);
				ids = ids.substring(ids.indexOf(",")+1, ids.length());
			}
			oaMeetingApply.setId(ids);
			oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"approveOk",null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "list";
	}
	/**
	 * 会议审批拒绝
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 下午1:59:07 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 下午1:59:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "approveNo", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingApprove/meetingApprove.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String approveNo(){
		try{
			String ids = oaMeetingApply.getId();
			while(ids.contains(",")){
				oaMeetingApply.setId(ids.split(",")[0]);
				oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"approveNo",null);
				ids = ids.substring(ids.indexOf(",")+1, ids.length());
			}
			oaMeetingApply.setId(ids);
			oaMeetingApplyService.saveOrUpdate(oaMeetingApply,"approveNo",null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "list";
	}
	
	/**
	 * 修改会议申请内容
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 上午9:31:40 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 上午9:31:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "editMeetingApply", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/meeting/meetingApply/addMeetingApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editMeetingApply(){
		try{
			fileServersURL = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
			oaMeetingApply = oaMeetingApplyService.get(oaMeetingApply.getId());
			oaMeetingApply.setStartTime(DateUtils.formatDateY_M_D_H_M_S(oaMeetingApply.getMeetingStarttime()));
			oaMeetingApply.setEndTime(DateUtils.formatDateY_M_D_H_M_S(oaMeetingApply.getMeetingEndtime()));
			ServletActionContext.getRequest().setAttribute("flag", flag);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "edit";
	}
	
	/**
	 * 查看会议申请内容
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 上午9:31:40 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 上午9:31:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "showMeetingDetail", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/meeting/meetingApprove/showMeetingDetail.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMeetingDetail(){
		try{
			oaMeetingApply = oaMeetingApplyService.get(oaMeetingApply.getId());
			String imgBaseUrl = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
			if (oaMeetingApply.getMeetingFile()!=null&&oaMeetingApply.getMeetingFile().contains("group")) {
				fileServersURL = imgBaseUrl;
			}else {
				fileServersURL= HisParameters.getfileURI(ServletActionContext.getRequest());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "edit";
	}
	
	/**
	 * 我的会议列表
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年9月18日 下午2:28:57 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年9月18日 下午2:28:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "myMeeting", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/myMeeting/myMeeting.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String myMeeting(){
		return "list";
	}
	
	@Action(value = "queryMyMeeting")
	public void queryMyMeeting(){
		try{
			oaMeetingApply.setMeetingApptype(1);
			List<OaMeetingApply> list = oaMeetingApplyService.queryMyMeeting(oaMeetingApply);
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("total", list.size());
			map.put("rows", list);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Action(value = "queryEmployeeExtend")
	public void queryEmployeeExtend(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
 			String page = request.getParameter("page");
			String rows = request.getParameter("rows");
			String ename = request.getParameter("ename");
			String dname = request.getParameter("dname");
			String workPost = request.getParameter("workPost");
			String workTitle = request.getParameter("workTitle");
			String workType = request.getParameter("workType");
			String dtype = request.getParameter("dtype");
			List<SysEmployee> employeeList = new ArrayList<SysEmployee>();
			SysEmployee sysEmployeSearch=new SysEmployee();
			sysEmployeSearch.setName(ename);
			sysEmployeSearch.setPage(page);
			sysEmployeSearch.setRows(rows);
			sysEmployeSearch.setDeptCode(dname);//员工管理页面左侧树有科室查询,此处科室查询注掉
			sysEmployeSearch.setPost(workPost);
			sysEmployeSearch.setTitle(workTitle);
			sysEmployeSearch.setType(workType);
			int total = oaMeetingApplyService.getEmployeeExtendTotal(sysEmployeSearch,dtype);
			employeeList =oaMeetingApplyService.getEmployeeExtendPage(sysEmployeSearch,dtype);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", employeeList);
			String mapJson=JSONUtils.toExclusionJson(map, true, DateUtils.DATE_FORMAT, "userId");
			WebUtils.webSendJSON(mapJson);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
