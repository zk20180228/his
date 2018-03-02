package cn.honry.mobile.menuIcon.action;

import java.io.File;
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

import cn.honry.base.bean.model.MenuIcon;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.menuIcon.service.MenuIconService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/menuIcon")
public class MenuIconAction {
	private Logger logger=Logger.getLogger(MenuIconAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "menuIconService")
	private MenuIconService menuIconService;
	public void setMenuIconService(MenuIconService menuIconService) {
		this.menuIconService = menuIconService;
	}
	
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
	}
	
	/**
	 * 列表查询参数
	 */
	private  String queryName;
	
	/**
	 * 分页页数
	 */
	private String page;
	/**
	 * 分页行数
	 */
	private String rows;

	private String menuAlias;
	
	/**
	 * 栏目图标维护
	 */
	private MenuIcon menuIcon;
	
	private File mFile;
	
	/**
	 * 栏目图标维护id，多个用逗号分隔
	 */
	private String ids;
	
	/**
	 * 栏目图标id
	 */
	private String id;

	/**
	 * 用于校验名称
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public File getmFile() {
		return mFile;
	}

	public void setmFile(File mFile) {
		this.mFile = mFile;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public MenuIcon getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(MenuIcon menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
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


	/**  
	 * 
	 * 跳转至移动端图标维护页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"LMTBWH:function:view"})
	@Action(value = "menuIconList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/menuIcon/menuIconList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  menuIconList(){
		return "list";
	}
	
	
	/**  
	 * 
	 * 列表数据
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:43:56 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月21日 上午10:43:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"LMTBWH:function:query"})
	@Action(value = "findMenuIconList")
	public void findMenuIconList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MenuIcon> list = menuIconService.getMenuIconList(rows, page, queryName);
			Integer total = menuIconService.getMenuIconCount(queryName);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YJXGLYWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LMTBWH","栏目图标维护","2","0"), e);
		}
		String json = JSONUtils.toJson(map,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}

	
	/**  
	 * 
	 * 跳转至移动端图标维护添加页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"LMTBWH:function:add"})
	@Action(value = "menuIconAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/menuIcon/menuIconEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  menuIconEdit(){
		return "list";
	}
	

	
	/** 保存栏目图标
	 * @param request
	 * @param response
	 * @param menuIcon 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@Action(value = "saveMenuIcon")
	public void saveMenuIcon(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			if(mFile == null || mFile.length() == 0){
				menuIconService.saveMenuIcon(menuIcon);
				map.put("resCode", "0");
				map.put("resMsg", "保存成功！");
			}else{
				String fileAddress = null;
				if(mFile!=null){
					fileAddress = uploadFileService.fileUpload(mFile,"pic.png","2","mobileIcon");
					menuIcon.setPicPath(fileAddress);
					menuIconService.saveMenuIcon(menuIcon);
					map.put("resCode", "0");
					map.put("resMsg", "保存成功！");
				}
			}
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "保存失败！");
			e.printStackTrace();
			logger.error("LMTBWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LMTBWH","栏目图片维护","2","1"), e);
		} 
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	
	
	/** 删除栏目图标
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"LMTBWH:function:del"})
	@Action(value = "delMenuIcon")
	public void delMenuIcon(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			menuIconService.delMenuIcon(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("LMTBWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LMTBWH","栏目图片维护","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	* 跳转修改栏目图标界面
	* @param request
	* @param response
	* @param id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"LMTBWH:function:edit"})
	@Action(value = "toEditMenuIcon", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/menuIcon/menuIconEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditMenuIcon() {
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String serverPath = HisParameters.getfileURI(request);
			menuIcon = menuIconService.get(id);
			if(menuIcon!=null&&StringUtils.isNotBlank(menuIcon.getPicPath())){
				menuIcon.setPicShow(serverPath+menuIcon.getPicPath());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("LMTBWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LMTBWH","栏目图片维护","2","1"), e);
		}
		return "list";
	}
	
	/** 
	* 跳转查看栏目图标界面
	* @author zxl 
	* @return 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"LMTBWH:function:view"})
	@Action(value = "toViewMenuIcon", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/menuIcon/menuIconView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewMenuIcon(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String serverPath = HisParameters.getfileURI(request);
		menuIcon = menuIconService.get(id);
		if(menuIcon!=null&&StringUtils.isNotBlank(menuIcon.getPicPath())){
			menuIcon.setPicShow(serverPath+menuIcon.getPicPath());
		}
		return "list";
	}
	
	/** 删除栏目图标
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@Action(value = "ckeckName")
	public void ckeckName(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			String str=menuIconService.ckeckName(name,id);
			if("ok".equals(str)){
				map.put("resCode", "0");
				map.put("resMsg", "通过！");
			}else{
				map.put("resCode", "2");
				map.put("resMsg", "该图片名称已经存在！");
			}
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "校验失败！");
			e.printStackTrace();
			logger.error("LMTBWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LMTBWH","栏目图片维护","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
