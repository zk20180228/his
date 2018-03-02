package cn.honry.mobile.personMenu.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.inner.system.menu.service.MenuInInterService;
import cn.honry.inner.system.menu.vo.ParentMenuVo;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.personMenu.service.PersonMenuService;
import cn.honry.mobile.personMenu.vo.MenuVo;
import cn.honry.mobile.personMenu.vo.UserVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**  
 * 
 * <p>个人栏目管理</p>
 * @Author: dutianliang
 * @CreateDate: 2017年7月15日 下午4:45:06 
 * @Modifier: dutianliang
 * @ModifyDate: 2017年7月15日 下午4:45:06 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/mosys/personMenu")
public class PersonMenuAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger=Logger.getLogger(PersonMenuAction.class);
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "personMenuService")
	private PersonMenuService personMenuService;
	public void setPersonMenuService(PersonMenuService personMenuService) {
		this.personMenuService = personMenuService;
	}
	@Autowired
	@Qualifier(value = "menuInInterService")
	private MenuInInterService menuInInterService;
	public void setMenuInInterService(MenuInInterService menuInInterService) {
		this.menuInInterService = menuInInterService;
	}

	private String menuAlias;
	private String q;
	/**  
	 * 
	 * @Fields parentId : 父级栏目id 
	 *
	 */
	private String parentId;

	/**  
	 * 
	 * @Fields userAccount : 用户账号 
	 *
	 */
	private String userAccount;
	/**  
	 * 
	 * @Fields infoJson : 保存json串
	 *
	 */
	private String infoJson;
	private String page;
	private String rows;


	/**  
	 * 
	 * <p>进入管理界面</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月15日 下午5:21:26 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月15日 下午5:21:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"GRLMGL:function:view"})
	@Action(value = "personMenuList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/personMenu/personMenuList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String personMenuList(){
		return "list";
	}
	
	/**  
	 * 
	 * <p>获取用户列表</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月11日 上午10:12:17 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月11日 上午10:12:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param request
	 * @param response
	 * @param q
	 * @param page
	 * @param rows:
	 *
	 */
	@Action(value = "queryUserList")
	public void queryUserList() {
		Map<String,Object> map = new HashMap<String, Object>();
		q = StringUtils.isNotBlank(q) && !"null".equals(q) ? ("%" + q.toUpperCase() + "%") : "";
		int total = personMenuService.getUserTotal(q);
		List<UserVo> list = personMenuService.getUserRows(page, rows, q);
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * <p>查询栏目下拉框（只有别名和名称两个字段，并且都是一级栏目）</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午5:36:27 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午5:36:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: List<ParentMenuVo> 栏目下拉框数据
	 *
	 */
	@Action(value = "queryMobileParentMenu")
	public void queryMobileParentMenu() {
		List<ParentMenuVo> list = menuInInterService.queryMobileParentMenu();
		String json = JSONUtils.toExposeJson(list, true, null, "name","id");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * <p>查询栏目列表信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午5:54:43 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午5:54:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param request
	 * @param response:
	 * @param parentId: 父级id
	 * @param userAccount: 用户账号
	 *
	 */
	@Action(value = "queryMobileMenuList")
	public void queryMobileMenuList() {
		List<MenuVo> list = personMenuService.queryMenuList(parentId, userAccount);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * <p>保存信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午7:27:18 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午7:27:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param request
	 * @param response
	 * @param infoJson json串
	 * @param userAccount 账号
	 * @param parentId 父级id
	 *
	 */
	@Action(value = "saveMenus")
	public void saveMenus(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			personMenuService.saveMenus(infoJson, userAccount, parentId);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YHLMGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YHLMGL-用户栏目管理","用户栏目管理","2","1"), e);
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}


	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getInfoJson() {
		return infoJson;
	}

	public void setInfoJson(String infoJson) {
		this.infoJson = infoJson;
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
	
	
	
}
