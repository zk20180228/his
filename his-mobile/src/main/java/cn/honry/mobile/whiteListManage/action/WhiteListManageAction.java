package cn.honry.mobile.whiteListManage.action;

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
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.whiteListManage.service.WhiteListManageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/whiteListManage")
public class WhiteListManageAction {

	private Logger logger=Logger.getLogger(WhiteListManageAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "whiteListManageService")
	private WhiteListManageService whiteListManageService;
	public void setWhiteListManageService(
			WhiteListManageService whiteListManageService) {
		this.whiteListManageService = whiteListManageService;
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
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"BMDDXGL:function:view"})
	@Action(value = "whiteManageList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteListManage/whiteListManageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  whiteManageList(){
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
	@RequiresPermissions(value={"BMDDXGL:function:query"})
	@Action(value = "findWhiteManage")
	public void findWhiteManage(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MMobileTypeManage> list = whiteListManageService.getWhiteManageList(rows, page, mobileCategory);
			Integer total = whiteListManageService.getWhiteManageCount(mobileCategory);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("BMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDDXGL","白名单（短信）管理","2","0"), e);
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
	@RequiresPermissions(value={"BMDDXGL:function:add"})
	@Action(value = "addWhiteManage", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteListManage/whiteListManageEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  addWhiteManage(){
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
	@RequiresPermissions(value={"BMDDXGL:function:edit"})
	@Action(value = "editWhiteManage", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteListManage/whiteListManageEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editWhiteManage() {
		try{
			mobileTypeManage = whiteListManageService.get(id);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("BMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDDXGL","白名单（短信）管理","2","1"), e);
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
	@RequiresPermissions(value={"BMDDXGL:function:del"})
	@Action(value = "delWhiteManage")
	public void delWhiteManage(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			whiteListManageService.delWhiteManage(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("BMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDDXGL","白名单（短信）管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	 * 根据手机号和类型校验该信息是否存在
	 * @param request
	 * @param response
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"BMDDXGL:function:edit"})
	@Action(value = "checkExist")
	public void checkExist(){
		Map<String, String> map = new HashMap<String, String>();
		List<MMobileTypeManage> list;
		try {
			list = whiteListManageService.checkExist(mobileCategory,"1");
			if(list!=null&&list.size()>0){
				map.put("resCode", "1");
				map.put("resMsg", "该手机类型在白名单已经存在！");
			}else{
				list = whiteListManageService.checkExist(mobileCategory,"2");
				if(list!=null&&list.size()>0){
					map.put("resCode", "2");
					map.put("resMsg", "该手机类型在黑名单已经存在！");
				}else{
					map.put("resCode", "0");
					map.put("resMsg", "校验通过！");
				}
				
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 保存白名单信息
	 * @param request
	 * @param response
	 * @param whiteList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"BMDDXGL:function:save"})
	@Action(value = "saveWhiteListData")
	public void saveWhiteListData(){
		Map<String, String> map = new HashMap<String, String>();
			try {
				whiteListManageService.save(mobileTypeManage,flg);
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
	
	/** 删除
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BMDDXGL:function:save"})
	@Action(value = "initData")
	public void initData(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			whiteListManageService.initData();
			map.put("resCode", "0");
			map.put("resMsg", "初始化成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "初始化失败！");
			e.printStackTrace();
			logger.error("BMDDXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDDXGL","白名单（短信）管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 移动至白名单
	 * @param request
	 * @param response
	 * @param mBlackList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"BMDDXGL:function:edit"})
	@Action(value = "moveBlack")
	public void moveBlack(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			whiteListManageService.moveBlack(ids);
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
}
