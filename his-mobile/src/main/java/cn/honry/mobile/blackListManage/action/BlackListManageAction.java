package cn.honry.mobile.blackListManage.action;

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

import cn.honry.base.bean.model.MMobileTypeManage;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.blackListManage.service.BlackListManageService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/blackListManage")
public class BlackListManageAction {

	private Logger logger=Logger.getLogger(BlackListManageAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "blackListManageService")
	private BlackListManageService blackListManageService;
	public void setBlackListManageService(
			BlackListManageService blackListManageService) {
		this.blackListManageService = blackListManageService;
	}

	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
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
	 * id，多个用逗号隔开
	 */
	private String ids;
	/**
	 * id
	 */
	private String id;
	
	/**
	 * 列表查询参数
	 */
	private  String mobileCategory;
	
	/**
	 * 区分标记
	 */
	private String flg;
	
	private MMobileTypeManage mobileTypeManage;
	
	public String getFlg() {
		return flg;
	}
	public void setFlg(String flg) {
		this.flg = flg;
	}
	public String getMobileCategory() {
		return mobileCategory;
	}
	public void setMobileCategory(String mobileCategory) {
		this.mobileCategory = mobileCategory;
	}
	public MMobileTypeManage getMobileTypeManage() {
		return mobileTypeManage;
	}
	public void setMobileTypeManage(MMobileTypeManage mobileTypeManage) {
		this.mobileTypeManage = mobileTypeManage;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	@RequiresPermissions(value={"HMDDXGL:function:view"})
	@Action(value = "blackManageList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackListManage/blackListManageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  blackLists(){
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
	@RequiresPermissions(value={"HMDDXGL:function:query"})
	@Action(value = "findBlackManage")
	public void findBlackManage(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MMobileTypeManage> list = blackListManageService.getBlackManageList(rows, page, mobileCategory);
			Integer total = blackListManageService.getBlackManageCount(mobileCategory);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("HMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("HMDDXGL","白名单（短信）管理","2","0"), e);
		}
		String json = JSONUtils.toJson(map,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 跳转至添加页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"HMDDXGL:function:add"})
	@Action(value = "addBlackManage", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackListManage/blackListManageEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  addBlackManage(){
		return "list";
	}
	
	/** 
	* 跳转修改界面
	* @param request
	* @param response
	* @param id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"HMDDXGL:function:edit"})
	@Action(value = "editBlackManage", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/blackListManage/blackListManageEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editBlackManage() {
		try{
			mobileTypeManage = blackListManageService.get(id);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("HMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("HMDDXGL","白名单（短信）管理","2","1"), e);
		}
		return "list";
	}
	
	/** 删除
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"HMDDXGL:function:del"})
	@Action(value = "delBlackManage")
	public void delBlackManage(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			blackListManageService.delBlackManage(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("HMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("HMDDXGL","白名单（短信）管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	
	/** 保存黑名单信息
	 * @param request
	 * @param response
	 * @param blackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"HMDDXGL:function:save"})
	@Action(value = "saveBlackListData")
	public void saveBlackListData(){
		Map<String, String> map = new HashMap<String, String>();
			try {
				blackListManageService.save(mobileTypeManage,flg);
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
	 * @param mBlackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"HMDDXGL:function:edit"})
	@Action(value = "moveWhite")
	public void moveWhite(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			blackListManageService.moveWhite(ids);
			map.put("resCode", "0");
			map.put("resMsg", "移动成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "移动失败！");
			logger.error(e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 同步至缓存
	 * @param request
	 * @param response
	 * @param mBlackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"HMDDXGL:function:edit"})
	@Action(value = "synCach")
	public void synCach(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<String> list=blackListManageService.synCach();
			String cacheKey="phoneTypeBlackList";
			if(redisUtil.exists(cacheKey)){
				redisUtil.set("phoneTypeBlackList", list);
			}else{
				redisUtil.set(cacheKey, list);
			}
			map.put("resCode", "0");
			map.put("resMsg", "同步成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "同步失败！");
			logger.error(e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	
}
