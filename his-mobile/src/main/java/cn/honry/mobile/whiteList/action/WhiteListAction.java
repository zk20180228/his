package cn.honry.mobile.whiteList.action;

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
import cn.honry.base.bean.model.MWhiteList;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.blackList.service.BlackListService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.machineManage.service.MachineManageService;
import cn.honry.mobile.whiteList.service.WhiteListService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/mosys/whiteList")
public class WhiteListAction extends ActionSupport{
	private Logger logger=Logger.getLogger(WhiteListAction.class);
	
	@Autowired
	@Qualifier(value = "whiteListService")
	private WhiteListService whiteListService;
	public void setWhiteListService(WhiteListService whiteListService) {
		this.whiteListService = whiteListService;
	}

	@Autowired
	@Qualifier(value = "blackListService")
	private BlackListService blackListService;
	public void setBlackListService(BlackListService blackListService) {
		this.blackListService = blackListService;
	}

	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "machineManageService")
	private MachineManageService machineManageService;
	public void setMachineManageService(MachineManageService machineManageService) {
		this.machineManageService = machineManageService;
	}

	private String menuAlias;
	/**
	 * 列表查询参数
	 */
	private String queryName;

	/**
	 * 分页行数
	 */
	private String rows;

	/**
	 * 分页页数
	 */
	private String page;

	/**
	 * 白名单id 多个用逗号分隔
	 */
	private String ids;

	/**
	 * id
	 */
	private String id;

	/**
	 * 白名单实体
	 */
	private MWhiteList whiteList;

	/**
	 * 区分标记
	 */
	private String flg;

	/**
	 * 用户账户
	 */
	private String userAccunt;

	/**
	 * 设备码
	 */
	private String machineCode;
	
	
	@RequiresPermissions(value={"BMDGL:function:view"})
	@Action(value = "whiteLists", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteList/whiteList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  whiteLists(){
		return "list";
	}

	/** 查询信息列表
	* @Title: findWhiteLists 查询白名单信息列表
	* @param request
	* @param response
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"BMDGL:function:query"})
	@Action(value = "findWhiteLists")
	public void findWhiteLists(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			MWhiteList whiteList = new MWhiteList();
			if(StringUtils.isNotBlank(queryName)){
				whiteList.setUser_account(queryName);
			}
			List<MWhiteList> list = whiteListService.getPagedWhiteList(rows, page, whiteList);
			Integer count = whiteListService.getCount(whiteList);
			map.put("total", count);
			map.put("rows", list);
		}catch(Exception e ){
			e.printStackTrace();
			logger.error("BMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDGL","白名单管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	
	/** 删除白名单信息
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BMDGL:function:del"})
	@Action(value = "delWhiteLists")
	public void delWhiteLists(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			whiteListService.delWhiteLists(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDGL","白名单管理","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 跳转查看白名单信息界面
	* @param request
	* @param response
	* @param id 版本id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BMDGL:function:view"})
	@Action(value = "toViewWhite", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteList/whiteView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewWhite() throws Exception  {
		whiteList = whiteListService.getMWhiteById(id);
		return "list";
	}
	
	/** 跳转添加白名单界面
	* @author zxl
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BMDGL:function:add"})
	@Action(value = "toAddWhite", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteList/whiteEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddWhite() {
		return "list";
	}
	
	/** 跳转修改白名单界面
	* @author zxl
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"BMDGL:function:edit"})
	@Action(value = "toEditWhite", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/whiteList/whiteEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditWhite() {
		try{
			whiteList = whiteListService.get(id);
		}catch(Exception e ){
			e.printStackTrace();
			logger.error("BMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDGL","白名单管理","2","1"), e);
		}
		return "list";
	}
	
	/** 保存白名单信息
	 * @param request
	 * @param response
	 * @param whiteList 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"BMDGL:function:save"})
	@Action(value = "saveWhiteList")
	public void saveWhiteList(){
		Map<String, String> map = new HashMap<String, String>();
			try {
				whiteListService.saveOrUpdate(whiteList,flg);
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
	
	/** 根据用户账户查询
	* @param request
	* @param response
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"BMDGL:function:query"})
	@Action(value = "findDataByUserAccunt")
	public void findDataByUserAccunt(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			
			if(StringUtils.isNotBlank(userAccunt)&&StringUtils.isNotBlank(machineCode)){
				MBlackList mBlack=blackListService.findBlackByUserAccunt(userAccunt,machineCode);
				if(mBlack!=null){
					map.put("resCode", "2");
					map.put("resMsg", "该设备已存在于黑名单！");
					WebUtils.webSendJSON(JSONUtils.toJson(map));
					return ;
				}
				MWhiteList wList=whiteListService.findWhiteByUserAccunt(userAccunt,machineCode);
				if(wList!=null){
					map.put("resCode", "3");
					map.put("resMsg", "该设备已存在于白名单！");
					WebUtils.webSendJSON(JSONUtils.toJson(map));
					return ;
				}
				if(StringUtils.isNotBlank(id)){
					if("black".equals(flg)){
						MBlackList mBlackList = blackListService.get(id);
						//根据设备码获取该设备是否绑定
						MMachineManage machineManage=machineManageService.getMachineByMachineCode(machineCode);
						if(machineManage!=null&&!mBlackList.getMachine_code().equals(machineManage.getMachine_code())){
							map.put("resCode", "1");
							map.put("resMsg", "该设备已经被绑定！");
							String json= JSONUtils.toJson(map);
							WebUtils.webSendJSON(json);
							return;
						}
						List<MMachineManage> lists=machineManageService.findMachineByUserAccunt(userAccunt);
						if(lists.size()>0&&!mBlackList.getUser_account().equals(lists.get(0).getUser_account())){
							map.put("resCode", "1");
							map.put("resMsg", "该账号已经被绑定！");
							String json= JSONUtils.toJson(map);
							WebUtils.webSendJSON(json);
							return;
						}
					}else{
						MWhiteList mWhiteList = whiteListService.get(id);
						//根据设备码获取该设备是否绑定
						MMachineManage machineManage=machineManageService.getMachineByMachineCode(machineCode);
						if(machineManage!=null&&!mWhiteList.getMachine_code().equals(machineManage.getMachine_code())){
							map.put("resCode", "1");
							map.put("resMsg", "该设备已经被绑定！");
							String json= JSONUtils.toJson(map);
							WebUtils.webSendJSON(json);
							return;
						}
						List<MMachineManage> lists=machineManageService.findMachineByUserAccunt(userAccunt);
						if(lists.size()>0&&!mWhiteList.getUser_account().equals(lists.get(0).getUser_account())){
							map.put("resCode", "1");
							map.put("resMsg", "该账号已经被绑定！");
							String json= JSONUtils.toJson(map);
							WebUtils.webSendJSON(json);
							return;
						}
					}
					
				}else{
					//根据设备码获取该设备是否绑定
					MMachineManage machineManage=machineManageService.getMachineByMachineCode(machineCode);
					if(machineManage!=null){
						map.put("resCode", "1");
						map.put("resMsg", "该设备已经被绑定！");
						String json= JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
					List<MMachineManage> lists=machineManageService.findMachineByUserAccunt(userAccunt);
					if(lists.size()>0){
						map.put("resCode", "1");
						map.put("resMsg", "该账号已经被绑定！");
						String json= JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
				}
				map.put("resCode", "0");
				map.put("resMsg", "校验通过！");
			}else{
				map.put("resCode", "1");
				map.put("resMsg", "请完善相关信息！");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("BMDGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("BMDGL","白名单管理","2","1"), e);
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
	@RequiresPermissions(value={"BMDGL:function:edit"})
	@Action(value = "moveBlack")
	public void moveBlack(){
		try {
			whiteListService.moveBlack(ids);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}


	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
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

	public MWhiteList getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(MWhiteList whiteList) {
		this.whiteList = whiteList;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	public String getUserAccunt() {
		return userAccunt;
	}

	public void setUserAccunt(String userAccunt) {
		this.userAccunt = userAccunt;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	
}
