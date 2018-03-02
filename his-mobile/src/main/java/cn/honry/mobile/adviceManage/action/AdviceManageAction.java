package cn.honry.mobile.adviceManage.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.MAdviceManage;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.base.bean.model.User;
import cn.honry.mobile.adviceManage.service.AdviceManageService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/adviceManage")
public class AdviceManageAction {

private Logger logger=Logger.getLogger(AdviceManageAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "adviceManageService")
	private AdviceManageService adviceManageService;
	public void setAdviceManageService(AdviceManageService adviceManageService) {
		this.adviceManageService = adviceManageService;
	}
	
	/**
	 * 列表查询参数
	 */
	private  String queryName;
	/**
	 * 用户列表查询参数
	 */
	private  String queryUser;
	/**
	 * 分页页数
	 */
	private String page;
	/**
	 * 分页行数
	 */
	private String rows;
	/**
	 * id多个用逗号分隔
	 */
	private String ids;
	
	/**
	 * 存放用户账号及姓名，多个用逗号隔开
	 */
	private String userStr;
	
	
	public String getUserStr() {
		return userStr;
	}

	public void setUserStr(String userStr) {
		this.userStr = userStr;
	}

	public String getQueryUser() {
		return queryUser;
	}

	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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

	/**  
	 * 
	 * 跳转至意见箱管理员维护页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"YJXGLYWH:function:view"})
	@Action(value = "adviceManageList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/adviceManage/adviceManageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  adviceManageList(){
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
	@RequiresPermissions(value={"YJXGLYWH:function:query"})
	@Action(value = "findAdviceManageList")
	public void findAdviceManageList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MAdviceManage> list = adviceManageService.getMAdviceManageList(rows, page, queryName);
			Integer total = adviceManageService.getMAdviceManageCount(queryName);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YJXGLYWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YJXGLYWH","意见箱管理员维护","2","0"), e);
		}
		String json = JSONUtils.toJson(map,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	
	/** 删除意见箱管理员
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"YJXGLYWH:function:del"})
	@Action(value = "delAdviceManage")
	public void delAdviceManage(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			adviceManageService.delAdviceManage(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YJXGLYWH","意见箱管理员维护","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 跳转至意见箱管理员维护页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"YJXGLYWH:function:add"})
	@Action(value = "userManageList", results = { @Result(name = "add", location = "/WEB-INF/pages/mobile/adviceManage/userManageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  userManageList(){
		return "add";
	}
	
	/**  
	 * 
	 * 用户列表数据
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
	@RequiresPermissions(value={"YJXGLYWH:function:query"})
	@Action(value = "findUserManageList")
	public void findUserManageList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<User> list = adviceManageService.getUserManageList(rows, page, queryUser);
			Integer total = adviceManageService.getUserManageCount(queryUser);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("YJXGLYWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YJXGLYWH","意见箱管理员维护","2","0"), e);
		}
		String json = JSONUtils.toJson(map,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	
	/** 保存意见箱管理员
	* @param request
	* @param response
	* @param userStr 要保存的用户信息
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"YJXGLYWH:function:save"})
	@Action(value = "saveAdviceManage")
	public void saveAdviceManage(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			adviceManageService.saveAdviceManage(userStr);
			map.put("resCode", "0");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "保存失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YJXGLYWH","意见箱管理员维护","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
