package cn.honry.oa.documentManage.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.DocManage;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.documentManage.service.DepartmentDocService;
import cn.honry.oa.documentManage.service.DocumentManageService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Title: 
 * Description:
 * @author cxw
 * @time 2017年11月22日 下午1:50:21
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/userPortal")
public class DocumentManageACtion extends ActionSupport {

	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	@Autowired
	@Qualifier(value = "documentManageService")
	private DocumentManageService documentManageService;
	
	private DepartmentDocService departmentDocService; 
	@Autowired
	@Qualifier(value = "departmentDocService")
	public void setDepartmentDocService(DepartmentDocService departmentDocService) {
		this.departmentDocService = departmentDocService;
	}

	private UploadFileService uploadFileService;
	@Autowired
	@Qualifier(value = "uploadFileService")
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}
	
	//上传文件
	private File mFile;
	private String mFileFileName;
	private String page;
	private String rows;
	//文档名称
	private String queryName;
	//上传类型
	private String queryType;
	//修改部门
	private String deptName;
	//查询
	private String dType;
	private String deptCode;
	
	//文档管理表对应的实体类
	private DocManage docManage;
	private Logger logger=Logger.getLogger(DocumentManageACtion.class);
	public DocumentManageService getDocumentManageService() {
		return documentManageService;
	}
	public void setDocumentManageService(DocumentManageService documentManageService) {
		this.documentManageService = documentManageService;
	}
	public DocManage getDocManage() {
		return docManage;
	}
	public void setDocManage(DocManage docManage) {
		this.docManage = docManage;
	}
	/* 跳转 主页面  */
	@RequiresPermissions(value={"WDGL:function:view"})
	@Action(value = "list", results = { @Result( 
			name = "docList",location = "/WEB-INF/pages/oa/documentManage/documentList.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String list(){
		return "docList";
	}
	
	/* 查看详情页  */
	@Action(value = "documentView", results = { @Result( 
			name = "docList",location = "/WEB-INF/pages/oa/documentManage/docView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String documentView(){
		String id =request.getParameter("id");
		HttpServletRequest request = ServletActionContext.getRequest();
		DocManage docManage = documentManageService.getListDocument(id);
		//根据部门编号获取部门名称
		SysDepartment sysDepartment=departmentDocService.getDeptName(docManage.getUploadDept());
		docManage.setUploadDept(sysDepartment.getDeptName());
		request.getSession().setAttribute("docManage", docManage);
		return "docList";
	}
	
	/* 跳转到添加页面  */
	@RequiresPermissions(value={"WDGL:function:add"})
	@Action(value = "toAddDocument", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/documentManage/addDoc.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddVersion() {
		HttpServletRequest request = ServletActionContext.getRequest();
		//获取登陆者的部门信息
		String deptName = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();
		request.setAttribute("deptName", deptName);
		return "list";
	}
	
	/* 展示文档管理表中所有的数据*/
	@RequiresPermissions(value={"WDGL:function:query"})
	@Action(value = "getlist")
	public void getlist(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		DocManage docManage1 = new DocManage();
		if(StringUtils.isNotBlank(queryName)){
			docManage1.setDocName(queryName);
			docManage1.setCreateUser(queryName);
		}
		if(StringUtils.isNotBlank(dType)){
			docManage1.setDeptType(dType);
		}
		if(StringUtils.isNotBlank(deptCode)){
			docManage1.setUploadDept(deptCode);
		}
		List<DocManage> list = documentManageService.queryDcoManage(docManage1,rows,page);
		//获取数据库总条数
		int count = list.size() ;
		for (DocManage docManage : list) {
			//查询科室的名称
			SysDepartment sysDepartment=departmentDocService.getDeptName(docManage.getUploadDept());
			docManage.setUploadDept(sysDepartment.getDeptName());
			String imgBaseUrl = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
				if (docManage.getDocDownAddr()!=null&&docManage.getDocDownAddr().contains("group")) {
					docManage.setDocDownAddr(imgBaseUrl+docManage.getDocDownAddr());
				}else {
					docManage.setDocDownAddr(HisParameters.getfileURI(request)+docManage.getDocDownAddr());
				}
		}
		map.put("total", count);
		map.put("rows", list);
		
		String json = JSONUtils.toJson(map, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
		/*request.getSession().setAttribute("list", list);*/
	}
	
	
	@Action(value = "/saveDocument")
	public void saveDocument(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String,String> map = new HashMap<String, String>();
		String id = request.getParameter("id");
		String uploadDept = request.getParameter("uploadDept");
		String docDes = request.getParameter("docDes");
		String deptName = request.getParameter("deptName");
		String createUser = request.getParameter("createUser");
		String docName = request.getParameter("docName");
		String docDownAddr = request.getParameter("docDownAddr");
		String deptType = request.getParameter("deptType");
		DocManage docManage = new DocManage();
		docManage.setCreateDate(new Date());
		docManage.setDeptType(deptType);
		if(docDes!=null && docDes!=""){
			docManage.setDocDes(docDes);
		}
		if(docName!=null && docName!=""){
			docManage.setDocName(docName);
		}
		if(id!=null){
			/* 修改文档中的数据*/
			try {
				docManage.setCreateUser(createUser);
				if(deptName.equals(uploadDept) ){
					//根据部门名称查询编号
					SysDepartment sysDepartment=departmentDocService.getDeptCode(uploadDept);
					if(sysDepartment!=null){
						docManage.setUploadDept(sysDepartment.getDeptCode());
					}
				}else{
					if(uploadDept!=null && uploadDept!=""){
						docManage.setUploadDept(uploadDept);
					}else{
						//如果部门前台传过来的是null
						docManage.setUploadDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					}
				}
				docManage.setId(id);
				if(mFile!=null){
					//获取上传文件地址
					FastDFSClient client = new FastDFSClient();
					String fileAddress = client.uploadFile(mFile, mFileFileName);
//					String fileAddress =uploadFileService.fileUpload(mFile,mFileFileName,"1",ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					if(fileAddress != null){
						docManage.setDocDownAddr(fileAddress);
						documentManageService.updateDcoManage(docManage);
						//修改成功
						map.put("resCode", "0");
						map.put("resMsg", "保存成功！");
						String json = JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
					}else{
							logger.info("未获取文件服务器返回地址");
						}
				}else{
					docManage.setDocDownAddr(docDownAddr);
					documentManageService.updateDcoManage(docManage);
					//修改成功
					map.put("resCode", "0");
					map.put("resMsg", "保存成功！");
					String json = JSONUtils.toJson(map);
					WebUtils.webSendJSON(json);
				}
			}catch (Exception e) {
				map.put("resCode", "1");
				map.put("resMsg", "保存失败！");
				e.printStackTrace();
				logger.error("WDGL修改", e);
			} finally{
				String json = JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
			}
		}else{
			/* 添加文档中的数据*/
			if(uploadDept.equals(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName()) ){
				//根据部门名称查询编号
				SysDepartment sysDepartment=departmentDocService.getDeptCode(uploadDept);
				if(sysDepartment!=null){
					docManage.setUploadDept(sysDepartment.getDeptCode());
				}
			}else{
				if(uploadDept!=null && uploadDept!=""){
					docManage.setUploadDept(uploadDept);
				}else{
					//如果部门前台传过来的是null
					docManage.setUploadDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				}
			}
			docManage.setId(UUID.randomUUID().toString().replace("_", ""));
			//docManage.setUploadDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
			docManage.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			try {
				//获取上传文件地址
				FastDFSClient client = new FastDFSClient();
				String fileAddress = client.uploadFile(mFile, mFileFileName);
//				String fileAddress =uploadFileService.fileUpload(mFile,mFileFileName,"1",ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				if(fileAddress != null){
					docManage.setDocDownAddr(fileAddress);
					documentManageService.insertDocument(docManage);
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
	@Action(value = "/deleteDocument")
	public void deleteDocument(){
		Map<String,String> map = new HashMap<String, String>();
		try {
			documentManageService.deleteDcoManage(ids);
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
	
	/* 获取文档管理表中回显的数据*/
	@Action(value = "/getListDocument", results = { @Result(
			name = "getListDocument", location = "/WEB-INF/pages/oa/documentManage/updateDoc.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String getListDocument(){
		String id =request.getParameter("id");
		HttpServletRequest request = ServletActionContext.getRequest();
		DocManage docManage = documentManageService.getListDocument(id);
		//根据部门编号获取部门名称
		SysDepartment sysDepartment=departmentDocService.getDeptName(docManage.getUploadDept());
		docManage.setUploadDept(sysDepartment.getDeptName());
		request.getSession().setAttribute("docManage", docManage);
		return "getListDocument";
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

	private String ids;
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getdType() {
		return dType;
	}
	public void setdType(String dType) {
		this.dType = dType;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	
}
