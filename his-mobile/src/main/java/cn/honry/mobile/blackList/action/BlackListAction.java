package cn.honry.mobile.blackList.action;

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

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.blackList.service.BlackListService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/blackList")
public class BlackListAction {
	private Logger logger=Logger.getLogger(BlackListAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "blackListService")
	private BlackListService blackListService;
	public void setBlackListService(BlackListService blackListService) {
		this.blackListService = blackListService;
	}

	private String menuAlias;
	/**
	 * 分页页数
	 */
	private String page;
	/**
	 * 分页行数
	 */
	private String rows;
	/**
	 * 列表查询参数
	 */
	private String queryName;
	/**
	 * id，多个用逗号隔开
	 */
	private String ids;
	/**
	 * id
	 */
	private String id;

	/**
	 * 黑名单实体
	 */
	private MBlackList blackList;

	/**
	 * 区分新增还是移至数据
	 */
	private String flg;
	
	/**
	 * 用户账户信息
	 */
	private String userAccount;

	
	
	/**  
	 * 
	 * <p>进入页面</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"HMDGL:function:view"})
	@Action(value = "blackLists", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackList/blackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  blackLists(){
		return "list";
	}
	/** 查询信息列表
	* @Title: findVersionList 查询黑名单信息列表
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"HMDGL:function:query"})
	@Action(value = "findBlackLists")
	public void findBlackLists(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			MBlackList blackList = new MBlackList();
			if(StringUtils.isNotBlank(queryName)){
				blackList.setUser_account(queryName);
			}
			List<MBlackList> list = blackListService.getPagedBlackList(rows, page, blackList);
			Integer count = blackListService.getCount(blackList);
			map.put("total", count);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("HMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("HMDGL","黑名单管理","2","0"), e);
		}
		String json = JSONUtils.toJson(map,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}

	/** 删除黑名单信息
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"HMDGL:function:del"})
	@Action(value = "delBlackLists")
	public void delBlackLists(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			blackListService.delBlackLists(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			logger.error(e);
			e.printStackTrace();
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 跳转查看黑名单信息界面
	* @param request
	* @param response
	* @param id 版本id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"HMDGL:function:view"})
	@Action(value = "toViewBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackList/blackView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewBlack() throws Exception  {
		blackList = blackListService.getMBlackById(id);
		return "list";
	}
	
	/** 跳转添加黑名单界面
	* @author zxl
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"HMDGL:function:add"})
	@Action(value = "toAddBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackList/blackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddBlack() {
		return "list";
	}
	
	/** 跳转修改黑名单界面
	* @author zxl
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"HMDGL:function:edit"})
	@Action(value = "toEditBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackList/blackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditBlack() {
		try{
			blackList = blackListService.get(id);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("HMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("HMDGL","黑名单管理","2","0"), e);
		}
		return "list";
	}
	
	/** 保存黑名单信息
	 * @param request
	 * @param response
	 * @param blackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"HMDGL:function:edit"})
	@Action(value = "saveBlackList")
	public void saveBlackList(){
		Map<String, String> map = new HashMap<String, String>();
			try {
				blackListService.saveOrUpdate(blackList,flg);
				map.put("resCode", "0");
				map.put("resMsg", "保存成功！");
			} catch (Exception e) {
				map.put("resCode", "1");
				map.put("resMsg", "保存失败！");
				logger.error(e);
				e.printStackTrace();
			}
		
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 移动至白名单
	 * @param request
	 * @param response
	 * @param blackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"HMDGL:function:edit"})
	@Action(value = "moveWhite")
	public void moveWhite(){
			try {
				blackListService.moveWhite(ids);
			} catch (Exception e) {
				logger.error(e);
				exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("HMDGL","黑名单管理","2","0"), e);
			}
	}
	
	/** 移动至白名单
	 * @param request
	 * @param response
	 * @param blackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"HMDGL:function:edit"})
	@Action(value = "checkIsLost")
	public void checkIsLost(){
		Map<String, String> map = new HashMap<String, String>();
		List<MMachineManage> list=blackListService.checkIsLost(userAccount);
		if(list!=null&&list.size()>0){
			map.put("resCode", "1");
			map.put("resMsg", "校验未通过！");
		}else{
			map.put("resCode", "0");
			map.put("resMsg", "校验通过！");
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
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
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MBlackList getBlackList() {
		return blackList;
	}
	public void setBlackList(MBlackList blackList) {
		this.blackList = blackList;
	}
	public String getFlg() {
		return flg;
	}
	public void setFlg(String flg) {
		this.flg = flg;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	
}
