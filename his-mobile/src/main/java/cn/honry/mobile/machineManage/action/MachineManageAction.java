package cn.honry.mobile.machineManage.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.machineManage.service.MachineManageService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@RequestMapping("/mosys/machineManage")
public class MachineManageAction extends ActionSupport{

	@Autowired
	@Qualifier(value = "machineManageService")
	private MachineManageService machineManageService;
	
	public void setMachineManageService(MachineManageService machineManageService) {
		this.machineManageService = machineManageService;
	}

	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
 	private Logger logger=Logger.getLogger(MachineManageAction.class);
	
 	private String menuAlias;
	private MMachineManage machineManage;
	private String page;
	private String rows;
	/**  
	 * 
	 * @Fields queryIsLost : 是否丢失  7全部1否2是
	 *
	 */
	private String queryIsLost;
	/**  
	 * 
	 * @Fields queryName : 查询信息 
	 *
	 */
	private String queryName;
	/**  
	 * 
	 * @Fields whiteOrBlack : 黑白名单 1百2黑
	 *
	 */
	private String whiteOrBlack;
	/**  
	 * 
	 * @Fields ids : 删除id组合
	 *
	 */
	private String ids;
	/**  
	 * 
	 * @Fields id : id 
	 *
	 */
	private String id;

	/**  
	 * 
	 * @Fields userAndMach : 用户账号和设备码（用"_"分隔）
	 *
	 */
	private String userAndMach;

	/**  
	 * 
	 * @Fields userAccunt : 用户账号，验证账号是否存在
	 *
	 */
	private String userAccunt;


	/**  
	 * 
	 * @Fields userAccounts : 账号组合逗号分隔
	 *
	 */
	private String userAccounts;
	
	/**  
	 * 
	 * @Fields machineCode : 设备码
	 *
	 */
	private String machineCode;
	
	/**  
	 * 
	 * @Fields machineMobile : SIM码
	 *
	 */
	private String machineMobile;
	
	@RequiresPermissions(value={"MOSBGL:function:view"})
	@Action(value = "machineManageList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/machineManage/machineManageList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  machineManageList(){
		return "list";
	}
	/** 查询设备管理列表
	* @param request
	* @param response
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"MOSBGL:function:query"})
	@Action(value = "findMachineManageList")
	public void findMachineManageList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			MMachineManage machineManage = new MMachineManage();
			if(StringUtils.isNotBlank(queryName)){
				machineManage.setUser_account(queryName);
			}
			
			if(StringUtils.isNotBlank(queryIsLost)&&!"7".equals(queryIsLost)){
				machineManage.setIs_lost(Integer.parseInt(queryIsLost));
			}
			if(StringUtils.isNotBlank(whiteOrBlack)){
				if("1".equals(whiteOrBlack)){
					machineManage.setIs_white(2);
				}else{
					machineManage.setIs_black(2);
				}
				
			}
			List<MMachineManage> list = machineManageService.getPagedMachineList(page, rows, machineManage);
			Integer total = machineManageService.getCount(machineManage);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("MOSBGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/** 删除设备信息
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:del"})
	@Action(value = "delMachines")
	public void delMachines(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			machineManageService.delMachines(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 跳转查看设备信息界面
	* @param request
	* @param response
	* @param id 版本id
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:view"})
	@Action(value = "toViewMachine", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/machineManage/machineManageView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewMachine() throws Exception  {
		machineManage = machineManageService.get(id);
		return "list";
	}
	
	/** 跳转添加设备管理界面
	* @author zxl
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:add"})
	@Action(value = "toAddMachine", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/machineManage/machineManageEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAddMachine() {
		return "list";
	}
	

	
	/** 跳转修改版本信息管理界面
	* @author zxl
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:edit"})
	@Action(value = "toEditMachine", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/machineManage/machineManageEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditMachine() {
		try {
			machineManage = machineManageService.get(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MOSBGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		return "list";
	}
	
	/** 保存设备管理信息
	 * @param request
	 * @param response
	 * @param user 保存实体
	 * @author zxl
	 * @date 2017年6月20日
	 */
	@RequiresPermissions(value={"MOSBGL:function:save"})
	@Action(value = "saveMachine")
	public void saveMachine(){
		Map<String, String> map = new HashMap<String, String>();
			try {
				machineManageService.save(machineManage);
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
	
	/** 根据用户账户查询设备管理信息
	* @param request
	* @param response
	* @author zxl 
	* @date 2017年6月15日
	*/
	@RequiresPermissions(value={"MOSBGL:function:query"})
	@Action(value = "findMachineByUserAccunt")
	public void findMachineByUserAccunt(){
		Map<String, String> map = new HashMap<String, String>();
		List<MMachineManage> list=new ArrayList<MMachineManage>();
		try{
			if(StringUtils.isNotBlank(userAccunt)){
				list=machineManageService.findMachineByUserAccunt(userAccunt);
				if(StringUtils.isNotBlank(id)){
					 MMachineManage machine = machineManageService.get(id);
					 if(list!=null&&list.size()>=1&&!machine.getUser_account().equals(list.get(0).getUser_account())){
							map.put("resCode", "1");
							map.put("resMsg", "该账号已经被绑定！");
							String json= JSONUtils.toJson(map);
							WebUtils.webSendJSON(json);
							return;
					 }
					//根据设备码获取该设备是否绑定
					MMachineManage machineManage=machineManageService.getMachineByMachineCode(machineCode);
					if(machineManage!=null&&!machine.getMachine_code().equals(machineManage.getMachine_code())){
						map.put("resCode", "1");
						map.put("resMsg", "该设备码已经被绑定！");
						String json= JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
					//根据SIM码获取该设备是否绑定
					List<String> mobileNum=Arrays.asList(machineMobile.replace("，",",").split(","));
					List<MMachineManage> mMachineNum=machineManageService.getMachineByMobileNum(mobileNum);
					for(int i=0;i<mMachineNum.size();i++){
						if(mMachineNum.get(i).getUser_account().equals(machine.getUser_account())){
							if(!mobileNum.contains(mMachineNum.get(i).getMobiles())){
								map.put("resCode", "1");
								map.put("resMsg", "该SIM码已经被绑定！");
								String json= JSONUtils.toJson(map);
								WebUtils.webSendJSON(json);
								return;
							}
						}else{
							map.put("resCode", "1");
							map.put("resMsg", "该SIM码已经被绑定！");
							String json= JSONUtils.toJson(map);
							WebUtils.webSendJSON(json);
							return;
						}
					}
				}else{
					if(list!=null&&list.size()>=1){
						map.put("resCode", "1");
						map.put("resMsg", "该账号已经被绑定！");
						String json= JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
					//根据设备码获取该设备是否绑定
					MMachineManage machineManage=machineManageService.getMachineByMachineCode(machineCode);
					if(machineManage!=null){
						map.put("resCode", "1");
						map.put("resMsg", "该设备码已经被绑定！");
						String json= JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
					
					//根据SIM码获取该设备是否绑定
					List<String> mobileNum=Arrays.asList(machineMobile.replace("，",",").split(","));
					List<MMachineManage> mMachineNum=machineManageService.getMachineByMobileNum(mobileNum);
					if(mMachineNum.size()>0){
						map.put("resCode", "1");
						map.put("resMsg", "该SIM码已经被绑定！");
						String json= JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
				}
			}
		}catch(Exception e ){
			e.printStackTrace();
			logger.error("MOSBGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		map.put("resCode", "0");
		map.put("resMsg", "校验通过！");
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 挂失设备信息
	* @param request
	* @param response
	* @param ids 要挂失的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:save"})
	@Action(value = "lossMachines")
	public void lossMachines(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			machineManageService.updateLossOrActivate(ids,"2",userAndMach);
			map.put("resCode", "0");
			map.put("resMsg", "挂失成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "挂失失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 激活设备信息
	* @param request
	* @param response
	* @param ids 要激活的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:save"})
	@Action(value = "activateMachines")
	public void activateMachines(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			machineManageService.updateLossOrActivate(ids,"1",userAndMach);
			map.put("resCode", "0");
			map.put("resMsg", "激活成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "激活失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 移至白名单
	* @param request
	* @param response
	* @param ids 要移动的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:save"})
	@Action(value = "moveToWhite")
	public void moveToWhite(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			machineManageService.updateWhiteOrBlack(ids,"1",userAndMach);
			map.put("resCode", "0");
			map.put("resMsg", "移至白名单成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "移至白名单失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/** 移至黑名单
	* @param request
	* @param response
	* @param ids 要移动的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"MOSBGL:function:save"})
	@Action(value = "moveToBlack")
	public void moveToBlack(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			machineManageService.updateWhiteOrBlack(ids,"2",userAndMach);
			map.put("resCode", "0");
			map.put("resMsg", "移至黑名单成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "移至黑名单失败！");
			logger.error(e);
			e.printStackTrace();
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("MOSBGL","设备管理","2","1"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	public MMachineManage getMachineManage() {
		return machineManage;
	}
	public void setMachineManage(MMachineManage machineManage) {
		this.machineManage = machineManage;
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
	public String getQueryIsLost() {
		return queryIsLost;
	}
	public void setQueryIsLost(String queryIsLost) {
		this.queryIsLost = queryIsLost;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getWhiteOrBlack() {
		return whiteOrBlack;
	}
	public void setWhiteOrBlack(String whiteOrBlack) {
		this.whiteOrBlack = whiteOrBlack;
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
	public String getUserAccunt() {
		return userAccunt;
	}
	public void setUserAccunt(String userAccunt) {
		this.userAccunt = userAccunt;
	}
	public String getUserAccounts() {
		return userAccounts;
	}
	public void setUserAccounts(String userAccounts) {
		this.userAccounts = userAccounts;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getUserAndMach() {
		return userAndMach;
	}
	public void setUserAndMach(String userAndMach) {
		this.userAndMach = userAndMach;
	}
	public String getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	public String getMachineMobile() {
		return machineMobile;
	}
	public void setMachineMobile(String machineMobile) {
		this.machineMobile = machineMobile;
	}
	
}
