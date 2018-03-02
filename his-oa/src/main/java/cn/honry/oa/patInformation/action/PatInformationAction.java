package cn.honry.oa.patInformation.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.InformationAttachDownAuth;
import cn.honry.base.bean.model.InformationSubscripe;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.patInformation.service.PatInformationAttachDownAuthService;
import cn.honry.oa.patInformation.service.PatInformationService;
import cn.honry.oa.patInformation.vo.MenuCkeckedVO;
import cn.honry.oa.patInformation.vo.MenuVO;
import cn.honry.oa.patmenumanager.service.PatMenuManagerService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/patInformation")
public class PatInformationAction extends ActionSupport{
	Logger logger = Logger.getLogger(PatInformationAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "patInformationService")
	private PatInformationService patInformationService;
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	@Autowired
	@Qualifier(value = "patInformationAttachDownAuthService")
	private PatInformationAttachDownAuthService patInformationAttachDownAuthService;
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value = "patMenuManagerService")
	private PatMenuManagerService patMenuManagerService;
	private Information info ;//= new Information();
	/**附件*/
	private List<File> file;
	/**标题图片*/
	private File imgFile;
	private File uploadFile;
	/**权限*/
	private List<String> authority;
	/** 编辑文本 */
	private String content;
	/**栏目id**/
	private String menuId;
	/**职务**/
	private String post;
	/**职称*/
	private String title;
	/**设置权限的文件*/
	private String whichfile;
	/**附件名称**/
	private String attachname;
	private List<String> filename;
	private String page;
	private String rows;
	/**查询条件**/
	private String name;
	/**文章类型**/
	private String type;
	/**旧文件地址*/
	private String oldfileurl;
	/**旧文件名*/
	private String oldfilename;
	/**就文件权限**/
	private List<String> oldfileauth;
	/**文件地址*/
	private String fileurls;
	/**文件名称**/
	private String filenames;
	/**文章ID**/
	private String infoid;
	/**文件服务器地址**/
	private String fileServersURL;
	/**审核标志*/
	private String checkflag;
	/**发布标志*/
	private String pubflag;
	/**用户账号*/
	private String acount;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**  
	 * 转到栏目信息页面
	 * <p> </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 上午11:47:59 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 上午11:47:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "toList", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/patinformation/informationList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toList(){
		return "list";
	}
	/**  
	 * 
	 * <p>获取栏目信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午2:31:37 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午2:31:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getInfotmation")
	public void getInfotmation(){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			List<Information> list = patInformationService.findinformationList(name, menuId, page, rows, type,checkflag,pubflag);
			int total = patInformationService.findinformationTotal(name, menuId, type,checkflag,pubflag);
			map.put("rows", list);
			map.put("total", total);
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
	}
	@Action(value="delInformation")
	public void delInformation(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			patInformationService.delInformation(menuId);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		}catch(Exception e){
			map.put("resCode", "success");
			map.put("resMsg", "删除失败!");
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * 
	 * <p>转到发布页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月15日 下午6:26:22 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月15日 下午6:26:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "toAdd", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/patinformation/informationAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAdd(){
		fileServersURL = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
		info = new Information();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(user!=null){
			acount = user.getAccount();
		}
		return "add";
	}
	@Action(value = "toEdit", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/patinformation/informationEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEdit(){
		fileServersURL = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
		info = patInformationService.getInformationMsg(menuId);
		content = patInformationService.getContentFromMongo(menuId);
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(user!=null){
			acount = user.getAccount();
		}
		return "edit";
	}
	@Action(value = "toAuth", results = { @Result(name = "auth", location = "/WEB-INF/pages/oa/patinformation/informationAuth.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAuth(){
		return "auth";
	}
	/**  
	 * 
	 * <p>浏览页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月15日 下午6:26:37 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月15日 下午6:26:37 
	 * @ModifyRmk:  
	 * @Modifier: zpty
	 * @ModifyDate: 2017年8月13日 下午6:26:37 
	 * @ModifyRmk:  每次阅读的时候都会去更新订阅表的已读标志,这是不对的
	 * @version: V1.0
	 * @return
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "view", results = { @Result(name = "view", location = "/WEB-INF/pages/oa/patinformation/informationView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String view(){
		try{
			
			info = patInformationService.get(infoid);
			Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
			info.setPubuserName(nameMap.get(info.getInfoPubuser()));
			String m_S = DateUtils.formatDateY_M_D_H_M_S(info.getInfoPubtime());
			info.setTime(m_S);
			content = patInformationService.getContentFromMongo(infoid);
			List<InformationAttachDownAuth> list = patInformationAttachDownAuthService.getAuthByMenuID(infoid);
			fileurls = "";
			filenames = "";
			for (InformationAttachDownAuth auth : list) {
				if(StringUtils.isNotBlank(fileurls)){
					fileurls += "#";
					filenames += "#";
				}
				String imgBaseUrl = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
				if (auth.getFileURL()!=null&&auth.getFileURL().contains("group")) {
					fileurls += imgBaseUrl+auth.getFileURL();
				}else {
					fileurls += HisParameters.getfileURI(ServletActionContext.getRequest())+auth.getFileURL();
				}
				filenames += auth.getFilename();
			}
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			if(user!=null){
				//这里要判断此数据是否已读,如果已读就不用去更新
				InformationSubscripe subscripe = patInformationService.querySubscripe(info.getId(),user.getAccount());
				if(subscripe!=null && subscripe.getIsRead()==0){
					patInformationService.updateSubscripe(info.getId(), user.getAccount());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		patInformationService.updViews(infoid);
		return "view";
	}
	/**  
	 * <p>保存信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月15日 下午6:27:51 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月15日 下午6:27:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 * @throws UnsupportedEncodingException 
	 *
	 */
	@Action(value="save")
	public void save(){
		Map<String,String> map = new HashMap<String,String>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("utf-8");
			patInformationService.saveInfo(info, imgFile, request.getParameter("imgfilename"), file, attachname, authority, content);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>修改栏目信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月25日 下午4:15:16 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月25日 下午4:15:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value="editInformation")
	public void editInformation(){
		Map<String,String> map = new HashMap<String,String>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("utf-8");
			patInformationService.editInfo(info, imgFile, request.getParameter("imgfilename"), file, attachname, authority, content, oldfilename, oldfileurl, oldfileauth);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * 
	 * <p> 获取栏目下的员工权限</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 上午10:48:26 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 上午10:48:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getUserAuth")
	public void getUserAuth(){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			List<SysEmployee> list = patInformationService.queryAuthEmp(menuId, post, title,page,rows);
			int total = patInformationService.queryAuthEmpTotal(menuId, post, title);
			map.put("rows", list);
			map.put("total", total);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * 
	 * <p>根据栏目id获取查看权限（角色） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 下午7:21:23 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 下午7:21:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getRoleAuth")
	public void getRoleAuth(){
		List<MenuCkeckedVO> list = patInformationService.queryRoleAuth(menuId);
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**  
	 * 
	 * <p> 根据栏目id获取查看权限（科室）</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 下午8:09:14 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 下午8:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getDeptAuth")
	public void getDeptAuth(){
		List<MenuCkeckedVO> list = patInformationService.queryDeptAuth(menuId);
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**  
	 * 
	 * <p>根据栏目id获取查看权限（职务） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月19日 上午9:40:22 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月19日 上午9:40:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getDutyAuth")
	public void getDutyAuth(){
		List<MenuCkeckedVO> list = patInformationService.queryDutyAuth(menuId);
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**  
	 * 
	 * <p>获取发布权限的栏目 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月20日 下午3:57:10 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月20日 下午3:57:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@SuppressWarnings("unused")
	@Action(value="getMenu")
	public void getMenu(){
		String deptCode = null;
		String dutyCode = null;
		String roleCode = null;
		String acount = null;
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysRole role = ShiroSessionUtils.getCurrentUserLoginRoleFromShiroSession();
		SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		if(department!=null){
			deptCode = department.getDeptCode();
		}
		if(employee!=null){
			dutyCode = employee.getPost();
		}
		if(user!=null){
			acount = user.getAccount();
		}
		if(role!=null){
			roleCode = role.getAlias();
		}
		List<MenuVO> list = null;
		if(StringUtils.isBlank(acount)&&StringUtils.isBlank(roleCode)&&StringUtils.isBlank(dutyCode)&&StringUtils.isBlank(deptCode)){
			list = new ArrayList<MenuVO>();
		}else{
			list = patInformationService.findMenuVo(deptCode, dutyCode, roleCode, acount);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
	/**  
	 * <p>检查权限 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午5:36:43 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午5:36:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value="checkAuth")
	public void checkAuth(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			
//		List<MenuVo> menuByCode = patMenuManagerService.queryMenuByCode(menuId);
//		if(menuByCode!=null&&menuByCode.size()>0){
//			MenuVo menuVo = menuByCode.get(0);
//			Integer stop_flag = menuVo.getStop_flag();
//			if(stop_flag!=null&&stop_flag==0){//栏目未停用
			String deptCode = null;
			String dutyCode = null;
			String titleCode = null;
			String acount = null;
			SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			SysDepartment department = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
			if(department!=null){
				deptCode = department.getDeptCode();
			}
			if(employee!=null){
				dutyCode = employee.getPost();
				titleCode = employee.getTitle();
			}
			if(user!=null){
				acount = user.getAccount();
			}
			map = patInformationService.checkAuth(menuId, deptCode, dutyCode, titleCode, acount);
//			}else{
//				map.put("resCode", "stop");//栏目已停用
//			}
//		}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>删除已上传的附件 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午5:37:30 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午5:37:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value="deleteFile")
	public void deleteFile(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			patInformationService.deleteFile(oldfileurl);
			map.put("resCode", "success");
		}catch(Exception e){
			map.put("resCode", "error");
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>转到审核页面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月27日 下午7:06:45 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月27日 下午7:06:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value = "toAudit", results = { @Result(name = "toaudit", location = "/WEB-INF/pages/oa/patinformation/informationAudit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAudit(){
		try{
			
			info = patInformationService.get(infoid);
			Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
			info.setPubuserName(nameMap.get(info.getInfoPubuser()));
			String m_S = DateUtils.formatDateY_M_D_H_M_S(info.getInfoPubtime());
			info.setTime(m_S);
			content = patInformationService.getContentFromMongo(infoid);
			List<InformationAttachDownAuth> list = patInformationAttachDownAuthService.getAuthByMenuID(infoid);
			fileurls = "";
			filenames = "";
			for (InformationAttachDownAuth auth : list) {
				if(StringUtils.isNotBlank(fileurls)){
					fileurls += "#";
					filenames += "#";
				}
				String imgBaseUrl = FastDFSClient.getBaseImgUrl(ServletActionContext.getRequest());
				if (auth.getFileURL()!=null&&auth.getFileURL().contains("group")) {
					fileurls += imgBaseUrl+auth.getFileURL();
				}else {
					fileurls += HisParameters.getfileURI(ServletActionContext.getRequest())+auth.getFileURL();
				}
				filenames += auth.getFilename();
			}
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		return "toaudit";
	}
	/**  
	 * <p>文章信息审核 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月27日 下午4:47:40 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月27日 下午4:47:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value="audite")
	public void audit(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			map = patInformationService.informationAudit(infoid,type);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>进行权限判断 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月27日 下午6:52:00 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月27日 下午6:52:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value="judgeAuth")
	public void judgeAuth(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			List<MenuCkeckedVO> list = patInformationService.judgeAuthBymenuCode(menuId, type);
			if(list!=null&&list.size()>0){
				map.put("resCode", "success");
			}else{
				map.put("resCode", "error");
			}
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
	}
	
	
	/**
	 * 首页更多信息浏览
	 * @Title: viewList 
	 * @author  zpty
	 * @createDate ：2017年7月28日
	 * @return String
	 * @version 1.0
	 */
	@Action(value="viewList",results={@Result(name="viewList",location="/WEB-INF/pages/oa/patinformation/viewList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String viewList(){
		return "viewList";
	}
	
	/**
	 * 首页更多信息审核
	 * @Title: checkList 
	 * @author  zpty
	 * @createDate ：2017年7月28日
	 * @return String
	 * @version 1.0
	 */
	@Action(value="checkList",results={@Result(name="checkList",location="/WEB-INF/pages/oa/patinformation/checkList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String checkList(){
		return "checkList";
	}
	
	/**  
	 * <p>获取可以查看的文章信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年8月1日 上午9:44:12 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年8月1日 上午9:44:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value="getInformationView")
	public void getInformationView(){
		try{
			Map<String, Object> map = patInformationService.getInfomationView(menuId, page, rows);
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
	}
	
	/**  
	 * <p>获取可以审批的文章信息 </p>
	 * @Author: zpty
	 * @CreateDate: 2017年8月6日 上午9:44:12 
	 * @version: V1.0
	 * void
	 */
	@Action(value="getInformationCheck")
	public void getInformationCheck(){
		try{
			Map<String, Object> map = patInformationService.getInformationCheck(menuId, page, rows);
			WebUtils.webSendJSON(JSONUtils.toJson(map));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("OAXT_XXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAXT_XXGL", "OA系統_信息管理", "3", "1"), e);
		}
	}
	
	@Action(value="upload")
	public void upload(){
		String acount = "";
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(user!=null){
			acount = user.getAccount();
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		String fileName = request.getParameter("fileName");
		System.err.println(file);
		System.err.println(fileName);
		System.err.println(uploadFile);
		FastDFSClient client = null;
		try {
			client = new FastDFSClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String titleImgurl = client.uploadFile(uploadFile, fileName);
//		String titleImgurl = uploadFileService.fileUpload(uploadFile, fileName,"1",acount);
	}
	@Action(value = "toView", results = { @Result(name = "view", location = "/WEB-INF/pages/oa/patinformation/testFile.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView(){
		return "view";
	}
	public Information getInfo() {
		return info;
	}
	public void setInfo(Information info) {
		this.info = info;
	}
	public List<File> getFile() {
		return file;
	}
	public void setFile(List<File> file) {
		this.file = file;
	}
	public List<String> getAuthority() {
		return authority;
	}
	public void setAuthority(List<String> authority) {
		this.authority = authority;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public File getImgFile() {
		return imgFile;
	}
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
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
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getWhichfile() {
		return whichfile;
	}
	public void setWhichfile(String whichfile) {
		this.whichfile = whichfile;
	}
	public String getAttachname() {
		return attachname;
	}
	public void setAttachname(String attachname) {
		this.attachname = attachname;
	}
	public List<String> getFilename() {
		return filename;
	}
	public void setFilename(List<String> filename) {
		this.filename = filename;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public List<String> getOldfileauth() {
		return oldfileauth;
	}
	public void setOldfileauth(List<String> oldfileauth) {
		this.oldfileauth = oldfileauth;
	}
	public String getFileurls() {
		return fileurls;
	}
	public void setFileurls(String fileurls) {
		this.fileurls = fileurls;
	}
	public String getFilenames() {
		return filenames;
	}
	public void setFilenames(String filenames) {
		this.filenames = filenames;
	}
	public String getInfoid() {
		return infoid;
	}
	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}
	public String getFileServersURL() {
		return fileServersURL;
	}
	public void setFileServersURL(String fileServersURL) {
		this.fileServersURL = fileServersURL;
	}
	public String getCheckflag() {
		return checkflag;
	}
	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}
	public String getPubflag() {
		return pubflag;
	}
	public void setPubflag(String pubflag) {
		this.pubflag = pubflag;
	}
	public String getAcount() {
		return acount;
	}
	public void setAcount(String acount) {
		this.acount = acount;
	}
	
}
