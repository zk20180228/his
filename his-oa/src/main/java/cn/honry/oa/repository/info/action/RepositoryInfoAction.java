package cn.honry.oa.repository.info.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.RepositoryInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.repository.info.service.RepositoryInfoService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/repositoryInfo")
public class RepositoryInfoAction {
	@Autowired
	@Qualifier("repositoryInfoService")
	private RepositoryInfoService repositoryInfoService;
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	
	
	private RepositoryInfo info;
	private String isCollect;//是否为收藏(0否1是)
	private String infoid;
	private String page;
	private String rows;
	private String code;
	private String name;
	private String deptCode;
	private String content;
	private String nodeType;//是否为叶子节点，0是，1否
	/**附件名称**/
	private String attachname;
	/**附件*/
	private List<File> file;
	/**旧文件地址*/
	private String oldfileurl;
	/**旧文件名*/
	private String oldfilename;
	/**文件服务器地址**/
	private String fileServersURL;
	private String tabName;
	private String tabID;
	private String isShowCollect;//是否显示收藏，1-是；0-否
	private String account;
	/**  
	 * <p>转公共知识库页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午3:25:14 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午3:25:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value="toPublicInfoList",results={@Result(name="list",location="/WEB-INF/pages/oa/repository/info/publicRepositoryInfoList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toPublicInfoList(){
		account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		return "list";
	}
	//跳转到公共知识浏览页面
	@Action(value="toPublicRepositoryBrowse",results={@Result(name="list",location="/WEB-INF/pages/oa/repository/info/publicRepositoryBrowse.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toPublicRepositoryBrowse(){
		
		return "list";
	}
	/**  
	 * <p>转个人知识库页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午3:25:14 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午3:25:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value="toPersonalInfoList",results={@Result(name="list",location="/WEB-INF/pages/oa/repository/info/peisonalRepositoryInfoList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toPersonalInfoList(){
		return "list";
	}
	@Action(value="toPersonalInfoAdd",results={@Result(name="add",location="/WEB-INF/pages/oa/repository/info/repositoryInfoAdd.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toPersonalInfoAdd(){
		info = new RepositoryInfo();
		return "add";
	}
	@Action(value="toPersonalInfoEdit",results={@Result(name="edit",location="/WEB-INF/pages/oa/repository/info/repositoryInfoEdit.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toPersonalInfoEdit(){
		//获取文章信息
		info = repositoryInfoService.getInfoByid(infoid);
		fileServersURL =  FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
		return "edit";
	}
	@Action(value="toPersonalInfoView",results={@Result(name="view",location="/WEB-INF/pages/oa/repository/info/repositoryInfoView.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toPersonalInfoView(){
		//获取文章信息
		info = repositoryInfoService.getInfoByid(infoid);
		SysEmployee employee = employeeInInterService.getEmpByJobNo(info.getCreateUser());
		if(employee!=null){
			info.setCreateUser(employee.getName());
		}
		String imgBaseUrl = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
		if (info.getAttach()!=null&&info.getAttach().contains("group")) {
			fileServersURL = imgBaseUrl;
		}else {
			fileServersURL= HisParameters.getfileURI(ServletActionContext.getRequest());
		}
		//更新阅读次数
		repositoryInfoService.updateViews(infoid);
		return "view";
	}
	/**  
	 * <p>公共信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午7:49:00 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午7:49:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("getPublicInfo")
	public void getPublicInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			List<RepositoryInfo> list = repositoryInfoService.getPublicInfo(code, name, page, rows,nodeType);
			int total = repositoryInfoService.getPublicInfoTotal(code, name,nodeType);
			map.put("resCode", "success");
			map.put("resMsg", "成功!");
			map.put("rows", list);
			map.put("total", total);
		}catch(Exception e){
			e.printStackTrace();
			String message = e.getLocalizedMessage();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常，请联系管理员</br>"+message);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>个人信息或收藏信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午7:48:33 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午7:48:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("getPersonalInfo")
	public void getPersonalInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<RepositoryInfo> list = null;
		int total = 0;
		try{
			isCollect = StringUtils.isNotBlank(isCollect)?isCollect:"0";
			if("0".equals(isCollect)){//非收藏
				list = repositoryInfoService.getPersonalInfo(code, name, page, rows);
				total = repositoryInfoService.getPersonalInfoTotal(code, name);
			}else{
				list = repositoryInfoService.getPersonalCollectionInfo(code, name, page, rows);
				total = repositoryInfoService.getPersonalCollectionInfoTotal(code, name);
			}
			map.put("rows", list);
			map.put("total", total);
		}catch(Exception e){
			e.printStackTrace();
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>根据文章id删除文章信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月16日 上午9:59:55 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月16日 上午9:59:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("delInfo")
	public void delInfo(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			repositoryInfoService.delInfo(infoid);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "删除失败："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("saveinfo")
	public void saveinfo(){
		Map<String,String> map = new HashMap<String, String>();
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		try{
			//附件处理
			String attachURL = "";
			String pattachName = "";
			if(file!=null){
				String[] attachName = attachname.split("#");
				int i = 0;
				for (File fil : file) {
					String fname = attachName[i];
					String flname = attachName[i].substring(fname.lastIndexOf("\\")+1, fname.length());
					FastDFSClient client = new FastDFSClient();
					String fileUpload = client.uploadFile(fil, flname);
//					String fileUpload = uploadFileService.fileUpload(fil, flname, "1", account);
					if(StringUtils.isNotBlank(attachURL)){
						attachURL += "#";
					}
					if(StringUtils.isNotBlank(pattachName)){
						pattachName += "#";
					}
					attachURL += fileUpload;
					pattachName += flname;
					i++;
				}
			}
			if(StringUtils.isNotBlank(oldfilename)){
				attachURL = oldfileurl + "#" + attachURL;
				attachname = oldfilename + "#" + pattachName;
			}
			info.setAttach(attachURL);
			info.setAttachName(attachname);
			info.setContentHtml(content);
			repositoryInfoService.saveOrUpdateInfo(info);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		}catch(Exception e){
			e.printStackTrace();
			String message = e.getLocalizedMessage();
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!"+message);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>收藏功能 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月17日 下午7:49:39 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月17日 下午7:49:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("collectionInfo")
	public void collectionInfo(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			repositoryInfoService.savePersonalCollection(infoid);
			map.put("resCode", "success");
			map.put("resMsg", "收藏成功!");
		}catch(Exception e){
			e.printStackTrace();
			map.put("resCode", "success");
			map.put("resMsg", "收藏失败："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public String getInfoid() {
		return infoid;
	}
	public void setInfoid(String infoid) {
		this.infoid = infoid;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public RepositoryInfo getInfo() {
		return info;
	}
	public void setInfo(RepositoryInfo info) {
		this.info = info;
	}
	public String getAttachname() {
		return attachname;
	}
	public void setAttachname(String attachname) {
		this.attachname = attachname;
	}
	public List<File> getFile() {
		return file;
	}
	public void setFile(List<File> file) {
		this.file = file;
	}
	public String getOldfileurl() {
		return oldfileurl;
	}
	public void setOldfileurl(String oldfileurl) {
		this.oldfileurl = oldfileurl;
	}
	public String getOldfilename() {
		return oldfilename;
	}
	public void setOldfilename(String oldfilename) {
		this.oldfilename = oldfilename;
	}
	public String getFileServersURL() {
		return fileServersURL;
	}
	public void setFileServersURL(String fileServersURL) {
		this.fileServersURL = fileServersURL;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public String getTabID() {
		return tabID;
	}
	public void setTabID(String tabID) {
		this.tabID = tabID;
	}
	public String getIsShowCollect() {
		return isShowCollect;
	}
	public void setIsShowCollect(String isShowCollect) {
		this.isShowCollect = isShowCollect;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
}
