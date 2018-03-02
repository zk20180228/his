package cn.honry.oa.hospitalFileManager.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.apache.commons.lang3.StringUtils;

import cn.honry.base.bean.model.DocManage;
import cn.honry.base.bean.model.EmployeeBlacklist;
import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.bean.model.HospitalFileManager;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.documentManage.service.DepartmentDocService;
import cn.honry.oa.hospitalFileManager.service.EmployeeDeptService;
import cn.honry.oa.hospitalFileManager.service.FileManageService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * Title: 医院文档管理
 * Description:
 * @author cxw
 * @time 2017年11月20日 下午12:00:25
 */
/**
 * Title: 
 * Description:
 * @author cxw
 * @time 2017年11月22日 下午2:26:55
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/userPortal")
public class FileManageACtion extends ActionSupport {

	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	@Autowired
	@Qualifier(value = "fileManageService")
	private FileManageService fileManageService;
	
	private DepartmentDocService departmentDocService; 
	@Autowired
	@Qualifier(value = "departmentDocService")
	public void setDepartmentDocService(DepartmentDocService departmentDocService) {
		this.departmentDocService = departmentDocService;
	}

	@Autowired
	@Qualifier(value = "employeeDeptService")
	private EmployeeDeptService employeeDeptService;
	
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}
	
	//上传文件
	private File mFile;
	private String mFileFileName;
	private String page;
	private String rows;
	//前台多个删除
	private String ids;
	//修改时间
	private Date cDate;
	//回显部门名称
	private String dName;
	//查询名称
	private String queryName;
	private String queryName1;
	//查询类型
	private String queryType;
	private String queryType1;
	private String sDept;
	private String fileType;
	private String fileStatus;
	
	
	
	//文档管理表对应的实体类
	private Logger logger=Logger.getLogger(FileManageACtion.class);
	/* 跳转 主页面  */
	@RequiresPermissions(value={"WDGL:function:view"})
	@Action(value = "gefilelist", results = { @Result( 
			name = "fileList",location = "/WEB-INF/pages/oa/hospitalFileManager/fileList.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String list(){
		return "fileList";
	}
	
	
	/* 查看详情页  */
	@Action(value = "fileView", results = { @Result( 
			name = "docList",location = "/WEB-INF/pages/oa/hospitalFileManager/fileView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String documentView(){
		String id =request.getParameter("id");
		HospitalFileManager hospitalFileManager = fileManageService.getListFile(id);
		request.getSession().setAttribute("docManage", hospitalFileManager);
		return "docList";
	}
	
	/* 跳转到添加页面  */
	@RequiresPermissions(value={"WDGL:function:add"})
	@Action(value = "toAddFile", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/hospitalFileManager/addFile.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddFile() {
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取登陆者的部门信息
		String deptName = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();
		String fMan = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		request.setAttribute("deptName", deptName);
		request.setAttribute("fMan", fMan);
		return "list";
	}
	
	/* 回显修改中的数据 */
	@Action(value = "/getListFile", results = { @Result(
			name = "getListFile", location = "/WEB-INF/pages/oa/hospitalFileManager/updateFile.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String getListDocument(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id =request.getParameter("id");
		HospitalFileManager hospitalFileManager = fileManageService.getListFile(id);
		request.getSession().setAttribute("docManage", hospitalFileManager);
		return "getListFile";
	}
	
	/* 科室负责人下拉数据*/
	@Action(value = "/deptMan")
	public void getDeptMan(){
		Map<String, Object> map = new HashMap<String, Object>();
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		List<EmployeeExtend> EmployeeExtend = employeeDeptService.getDeptMan(deptCode);
//		map.put("rows", EmployeeExtend);
		String json = JSONUtils.toJson(EmployeeExtend, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/* 展示医院档案管理表中所有的数据*/
	@RequiresPermissions(value={"WDGL:function:query"})
	@Action(value = "queryFileList")
	public void queryFileList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		HospitalFileManager hospitalFileManager=new HospitalFileManager();
		if(StringUtils.isNotBlank(queryName)){
			hospitalFileManager.setFileNumber(queryName);
			hospitalFileManager.setName(queryName);
		}
		if(StringUtils.isNotBlank(queryName1)){
			hospitalFileManager.setFileClassify(queryName1);
		}
		if(StringUtils.isNotBlank(queryType)){
			hospitalFileManager.setBorrow(queryType);
		}
		if(StringUtils.isNotBlank(queryType1)){
			hospitalFileManager.setFileRank(queryType1);
		}
		if(StringUtils.isNotBlank(sDept)){
			//根据部门编号获取部门名称 
			SysDepartment sysDepartment=departmentDocService.getDeptName(sDept);
			hospitalFileManager.setDeptName(sysDepartment.getDeptName());
		}
		if(StringUtils.isNotBlank(fileType)){
			hospitalFileManager.setFileType(fileType);
		}
		if(StringUtils.isNotBlank(fileStatus)){
			hospitalFileManager.setFileStatus(fileStatus);
		}
		
		//获取档案管理所有的数据
		List<HospitalFileManager> getList = fileManageService.getList(hospitalFileManager,page, rows);
		for (HospitalFileManager hos : getList) {
			String imgBaseUrl = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
			if (hos.getFileURL()!=null&&hos.getFileURL().contains("group")) {
				hos.setFileURL(imgBaseUrl+hos.getFileURL());
			}else {
				hos.setFileURL(HisParameters.getfileURI(request)+hos.getFileURL());
			}
		}
		Integer count = getList.size();
		map.put("total", count);
		map.put("rows", getList);
		String json = JSONUtils.toJson(map, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	

	//保存或者是修改数据
	@Action(value = "/saveFile")
	public void saveDocument() throws ParseException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String,String> map = new HashMap<String, String>();
		HospitalFileManager hospitalFileManager=new HospitalFileManager();
		String id = request.getParameter("id");
		String uploadDept = request.getParameter("uploadDept");
		String fileNumber = request.getParameter("fileNumber");
		if(fileNumber!=null){
			hospitalFileManager.setFileNumber(fileNumber);
		}
		String name = request.getParameter("name");
		if(name!=null){
			hospitalFileManager.setName(name);
		}
		String fileClassify = request.getParameter("fileClassify");
		if(fileClassify!=null){
			hospitalFileManager.setFileClassify(fileClassify);
		}
		String fileRank = request.getParameter("fileRank");
		if(fileRank!=null){
			hospitalFileManager.setFileRank(fileRank);
		}
		String fileType = request.getParameter("fileType");
		if(fileType!=null){
			hospitalFileManager.setFileType(fileType);
		}
		String fileStatus = request.getParameter("fileStatus");
		if(fileStatus!=null){
			hospitalFileManager.setFileStatus(fileStatus);
		}
		String borrow = request.getParameter("borrow");
		hospitalFileManager.setBorrow(borrow);
		String fileMan = request.getParameter("fileMan");
		hospitalFileManager.setFileMan(fileMan);
		if(id!=null){
			 //修改文档中的数据
			String createUser = request.getParameter("createUser");
			String createDept = request.getParameter("createDept");
			String cDate = request.getParameter("cDate");
			hospitalFileManager.setUpdateTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			hospitalFileManager.setCreateTime(sdf.parse(cDate));
			hospitalFileManager.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			hospitalFileManager.setCreateUser(createUser);
			hospitalFileManager.setCreateDept(createDept);
			hospitalFileManager.setDel_flg(0);
			hospitalFileManager.setStop_flg(0);
			hospitalFileManager.setId(id);
			//判断上传科室
			if(uploadDept.equals(dName) ){
				hospitalFileManager.setDeptName(uploadDept);
			}else{
				if(uploadDept!=null && uploadDept!=""){
					//根据部门编号获取部门名称 
					SysDepartment sysDepartment=departmentDocService.getDeptName(uploadDept);
					hospitalFileManager.setDeptName(sysDepartment.getDeptName());
					
				}
			}
			try {
				if(mFile!=null){
					//获取上传文件地址
					FastDFSClient client = new FastDFSClient();
					String fileAddress = client.uploadFile(mFile, mFileFileName);
//					String fileAddress =uploadFileService.fileUpload(mFile,mFileFileName,"1",ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					if(fileAddress != null){
						hospitalFileManager.setFileURL(fileAddress);
					}
				}else{
					String fileURL = request.getParameter("fileURL");
					if(fileURL != null){
						hospitalFileManager.setFileURL(fileURL);
					}
				}
				fileManageService.updateFile(hospitalFileManager);
				//保存成功
				map.put("resCode", "0");
				map.put("resMsg", "保存成功！");
				String json = JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
			} catch (Exception e) {
				map.put("resCode", "1");
				map.put("resMsg", "保存失败！");
				e.printStackTrace();
				logger.error("WDGL保存", e);
			} finally{
				String json = JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
			}
		}else{
			 //添加文档中的数据
			if(uploadDept.equals(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName()) ){
				hospitalFileManager.setDeptName(uploadDept);
			}else{
				if(uploadDept!=null && uploadDept!=""){
					//根据部门编号获取部门名称 
					SysDepartment sysDepartment=departmentDocService.getDeptName(uploadDept);
					if(sysDepartment!=null){
						hospitalFileManager.setDeptName(sysDepartment.getDeptName());
					}
				}else{
					//如果部门前台传过来的是null
					hospitalFileManager.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
				}
			}
			hospitalFileManager.setId(UUID.randomUUID().toString().replace("_", ""));
			try {
				//获取上传文件地址
				FastDFSClient client = new FastDFSClient();
				String fileAddress = client.uploadFile(mFile, mFileFileName);
//				String fileAddress =uploadFileService.fileUpload(mFile,mFileFileName,"1",ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				if(fileAddress != null){
					hospitalFileManager.setFileURL(fileAddress);
					fileManageService.insertFile(hospitalFileManager);
					//保存成功
					map.put("resCode", "0");
					map.put("resMsg", "保存成功！");
					String json = JSONUtils.toJson(map);
					WebUtils.webSendJSON(json);
				}else{
					logger.info("未获取文件服务器返回地址");
				}
			} catch (Exception e) {
				map.put("resCode", "1");
				map.put("resMsg", "保存失败！");
				e.printStackTrace();
				logger.error("WDGL保存", e);
			} finally{
				String json = JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
			}
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("WDGL修改", e);
		} finally{
			String json = JSONUtils.toJson("0");
			WebUtils.webSendJSON(json);
		}
	}
	
	/* 删除文档中的数据*/
	@Action(value = "/deleteFile")
	public void deleteDocument(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			fileManageService.deleteFile(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("WDGL", e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	public FileManageService getFileManageService() {
		return fileManageService;
	}

	public void setFileManageService(FileManageService fileManageService) {
		this.fileManageService = fileManageService;
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
	public File getmFile() {
		return mFile;
	}
	public void setmFile(File mFile) {
		this.mFile = mFile;
	}

	public String getmFileFileName() {
		return mFileFileName;
	}
	public void setmFileFileName(String mFileFileName) {
		this.mFileFileName = mFileFileName;
	}

	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	public Date getcDate() {
		return cDate;
	}
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getQueryName1() {
		return queryName1;
	}
	public void setQueryName1(String queryName1) {
		this.queryName1 = queryName1;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getQueryType1() {
		return queryType1;
	}
	public void setQueryType1(String queryType1) {
		this.queryType1 = queryType1;
	}

	public EmployeeDeptService getEmployeeDeptService() {
		return employeeDeptService;
	}
	public void setEmployeeDeptService(EmployeeDeptService employeeDeptService) {
		this.employeeDeptService = employeeDeptService;
	}
	
	public String getsDept() {
		return sDept;
	}
	public void setsDept(String sDept) {
		this.sDept = sDept;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	
}
