package cn.honry.oa.userPortal.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.oa.userPortal.service.UserPortalService;
import cn.honry.oa.workProcessManage.service.WorkProcessManageService;
import cn.honry.oa.workProcessManage.vo.WorkProcessManageVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisManagerUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * OA系统首页个人首页维护
 * @author  zpty
 * @date 2017-7-18 15：40
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/userPortal")
public class UserPortalAction extends ActionSupport{
	private Logger logger=Logger.getLogger(UserPortalAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}

	private static final long serialVersionUID = 1L;
	private UserPortalService userPortalService;
	@Autowired
	@Qualifier(value = "userPortalService")
	public void setUserPortalService(UserPortalService userPortalService) {
		this.userPortalService = userPortalService;
	}

	//我的待办用到的service
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	
	/**
	 * 个人首页维护表
	 */
	private OaUserPortal userPortal = new OaUserPortal();
	/**
	 * 个人首页编辑组件实体json
	 */
	private String userPorEditJson;
	
	public OaUserPortal getUserPortal() {
		return userPortal;
	}

	public void setUserPortal(OaUserPortal userPortal) {
		this.userPortal = userPortal;
	}

	public String getUserPorEditJson() {
		return userPorEditJson;
	}

	public void setUserPorEditJson(String userPorEditJson) {
		this.userPorEditJson = userPorEditJson;
	}

	/**
	 * @Description:翻页参数
	 */
	public String page;
	public void setPage(String page) {
		this.page = page;
	}
	public String rows;
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * @Description:向OA首页展示的组件数据
	 */
	private List<OaPortalWidget> portalWidgetList = new ArrayList<OaPortalWidget>();
	/**
	 * @Description:向OA首页展示的个人首页数据
	 */
	private List<OaUserPortal> userPortalList = new ArrayList<OaUserPortal>();
	/**
	 * @Description:向OA首页展示的组件数据Json
	 */
	private String portalWidgetJson="";
	/**
	 * @Description:向OA首页展示的个人首页数据Json
	 */
	private String userPortalJson="";
	
	public List<OaPortalWidget> getPortalWidgetList() {
		return portalWidgetList;
	}

	public void setPortalWidgetList(List<OaPortalWidget> portalWidgetList) {
		this.portalWidgetList = portalWidgetList;
	}

	public List<OaUserPortal> getUserPortalList() {
		return userPortalList;
	}

	public void setUserPortalList(List<OaUserPortal> userPortalList) {
		this.userPortalList = userPortalList;
	}

	public String getPortalWidgetJson() {
		return portalWidgetJson;
	}

	public void setPortalWidgetJson(String portalWidgetJson) {
		this.portalWidgetJson = portalWidgetJson;
	}

	public String getUserPortalJson() {
		return userPortalJson;
	}

	public void setUserPortalJson(String userPortalJson) {
		this.userPortalJson = userPortalJson;
	}

	@Resource
	private RedisUtil redisUtil;
	@Autowired
	@Qualifier(value = "noticeService")
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	
	/**
	 * 个人首页组件昵称(新增修改用)
	 */
	private String title;
	/**
	 * 个人首页组件组件ID(新增修改用)
	 */
	private String moudelId;
	/**
	 * 个人首页组件顺序号(新增修改用)
	 */
	private String oder;
	/**
	 * 个人首页组件编号id数据ID(新增修改删除用)
	 */
	private String dataId;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMoudelId() {
		return moudelId;
	}

	public void setMoudelId(String moudelId) {
		this.moudelId = moudelId;
	}

	public String getOder() {
		return oder;
	}

	public void setOder(String oder) {
		this.oder = oder;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * 个人首页组件移动传过来的所有组件的ID和顺序号的组合串
	 */
	private String dataJson;
	
	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	/**
	 * 工作流程Service
	 */
	@Autowired
	@Qualifier("workProcessManageService")
	private WorkProcessManageService workProcessManageService;
	public void setWorkProcessManageService(WorkProcessManageService workProcessManageService) {
		this.workProcessManageService = workProcessManageService;
	}
	/**
	 * 初始化传入的数据
	 */
	private String[] butName;
	
	public String[] getButName() {
		return butName;
	}

	public void setButName(String[] butName) {
		this.butName = butName;
	}

	/**
	 * 查询组件信息,接收类型的字段
	 */
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 新增自动刷新开关字段,去参数里查询,来判断是否需要自动刷新
	 */
	private String portalRefresh;
	public String getPortalRefresh() {
		return portalRefresh;
	}

	public void setPortalRefresh(String portalRefresh) {
		this.portalRefresh = portalRefresh;
	}

	/**  
	 * 
	 * 个人首页列表显示
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午5:48:26 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "listUserPortal", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userPortal/userPortalList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listUserPortal() {
		try {
			//获取当前登录人
			String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			//查询首页刷新开关
			portalRefresh = parameterInnerService.getparameter("portalRefresh");
			//查询出所有的组件数据
			portalWidgetList = userPortalService.queryPortalWidget();
			//查询出所有的组件数据Json
			portalWidgetJson=JSONUtils.toJson(portalWidgetList);
			//查询出当前登录人的所有个人组件数据(包括停用的)
			userPortalList = userPortalService.queryUserPortalAll(longinUserAccount);
			//查询出当前登录人的所有个人组件数据Json
			userPortalJson=JSONUtils.toJson(userPortalList);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("SYGL_SYJZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_SYJZ", "OA系统首页管理_首页加载", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 * 
	 * 移除首页组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:00:47 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "delUserPortal", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userPortal/userPortalList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delUserPortal() {
		try {
			//移除组件
			if(StringUtils.isNotBlank(dataId)){
				userPortalService.del(dataId);
			}else{
				WebUtils.webSendJSON("error");
			}
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("SYGL_YCZJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_YCZJ", "OA系统首页管理_移除组件", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 * 
	 * 添加&修改
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:51:46 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "saveUserPortal")
	public void saveUserPortal(){
		try{
			//获取当前登录人
			String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			//判断是否是新增,如果是新增先去查询数据库中是否有此组件了,如果有此组件,返回失败
			String flag = "no";
			if(StringUtils.isBlank(dataId)){
				flag = userPortalService.queryWidgetForUser(longinUserAccount,moudelId);//flag:yes是有此组件了,no为无此组件
			}
			if("yes".equals(flag)){//已经有此组件了
				WebUtils.webSendString("Error");
			}else{
				//将前台传过来的新增修改的数据放入实体用来保存
				userPortal.setId(dataId);//数据ID
				userPortal.setName(title);//组件昵称
				userPortal.setWidget(moudelId);//组件ID
				if(StringUtils.isNotBlank(oder)){
					userPortal.setOrder(Integer.valueOf(oder));//组件顺序号
				}else{
					userPortal.setOrder(-1);//组件顺序号
				}
				//将新增或修改的组件保存
				userPortalService.saveOrUpdate(userPortal);
				//查询出当前登录人的所有个人组件数据(包括停用的)
				userPortalList = userPortalService.queryUserPortalAll(longinUserAccount);
				String strJson=JSONUtils.toJson(userPortalList);
				WebUtils.webSendJSON(strJson);
			}
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SYGL_TJXG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_TJXG", "OA系统首页管理_添加修改", "2", "0"), e);
		}
	}

	
	
	/**  
	 * 
	 * 移动首页组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月19日 上午9:23:50 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "moveWidget")
	public void moveWidget(){
		try{
			//移动组件并储存
			if(StringUtils.isNotBlank(dataJson)){
				userPortalService.moveWidget(dataJson);
			}else{
				WebUtils.webSendJSON("error");
			}
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SYGL_TJXG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_TJXG", "OA系统首页管理_添加修改", "2", "0"), e);
		}
	}
	
	/**  
	 * 
	 * 启用组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:51:46 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "enableUserWidget")
	public void enableUserWidget(){
		try{
			//获取当前登录人
			String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			//将前台传过来的启用组件的数据放入实体用来保存
			userPortal.setId(dataId);//数据ID
			if(StringUtils.isNotBlank(oder)){
				userPortal.setOrder(Integer.valueOf(oder));//组件顺序号
			}else{
				userPortal.setOrder(-1);//组件顺序号
			}
			//将启用的组件保存
			userPortalService.enableUserWidget(userPortal);
			//启用完成后,需要往前台返回当前登录人启用后的组件情况
			//查询出当前登录人的所有个人组件数据(包括停用的)
			userPortalList = userPortalService.queryUserPortalAll(longinUserAccount);
			//查询出当前登录人的所有个人组件数据Json
			userPortalJson=JSONUtils.toJson(userPortalList);
			WebUtils.webSendJSON(userPortalJson);
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SYGL_TJXG", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_TJXG", "OA系统首页管理_添加修改", "2", "0"), e);
		}
	}
	
	
	/**  
	 * 
	 * 个人首页初始化列表显示
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午5:48:26 
	 * @version: V1.0
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "userPortalInitialization", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userPortal/userPortalInitialization.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String userPortalInitialization() {
		try {
			//查询出所有的组件数据
			portalWidgetList = userPortalService.queryPortalWidget();
			//查询出所有的组件数据Json
			portalWidgetJson=JSONUtils.toJson(portalWidgetList);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("SYGL_SYCSH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_SYCSH", "OA系统首页管理_首页初始化", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 * 
	 * 初始化个人组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月15日 下午5:51:46 
	 * @version: V1.0
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "initializationUserPortal")
	public void initializationUserPortal(){
		try{
			//将新传递过来的初始化组件的id数组进行保存
			userPortalService.initialization(butName);
			WebUtils.webSendJSON("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("SYGL_CSH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_CSH", "OA系统首页管理_初始化", "2", "0"), e);
		}
	}
	
	/**  
	 * 
	 * 查询通知公告信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryNotice")
	public void queryNotice(){
		//通知公告信息
		SysInfo info = new SysInfo();
		info.setInfoPubflag(1);
		String cacheKey="";
		List<SysInfo> noticeList = new ArrayList<SysInfo>();
		try {
			cacheKey = RedisManagerUtils.getClassNameByObj(this, "home-1");
			if(redisUtil.exists(cacheKey)){
				noticeList=(List<SysInfo>) redisUtil.get(cacheKey);
				logger.info("读取首页通知公告缓存:key="+cacheKey);
			}else{
				noticeList = userPortalService.getList(info);
				if(noticeList!=null&&noticeList.size()>0){
					redisUtil.set(cacheKey, noticeList);
					logger.info("添加首页通知公告缓存:key="+cacheKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_TZGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_TZGL", "首页管理_通知管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(noticeList,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询院内新闻信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryNews")
	public void queryNews(){
		//新闻信息
		SysInfo info = new SysInfo();
		info.setInfoPubflag(1);
		info.setInfoType(3);
		String cacheKey="";
		List<SysInfo> newsList = new ArrayList<SysInfo>();
		try {
			cacheKey=RedisManagerUtils.getClassNameByObj(this, "home-3");
			if(redisUtil.exists(cacheKey)){
				newsList=(List<SysInfo>) redisUtil.get(cacheKey);
				logger.info("读取首页新闻缓存:key="+cacheKey);
			}else{
				newsList = userPortalService.getList(info);
				if(newsList!=null&&newsList.size()>0){
					redisUtil.set(cacheKey, newsList);
					logger.info("添加首页新闻缓存:key="+cacheKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_XWXX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_XWXX", "首页管理_新闻信息", "2", "0"), e);
		}
		String json=JSONUtils.toJson(newsList,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询医疗前沿信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryMedical")
	public void queryMedical(){
		//医疗前沿
		SysInfo info = new SysInfo();
		info.setInfoPubflag(1);
		info.setInfoType(4);
		String cacheKey="";
		List<SysInfo> medicalList = new ArrayList<SysInfo>();
		try {
			cacheKey=RedisManagerUtils.getClassNameByObj(this, "home-4");
			if(redisUtil.exists(cacheKey)){
				medicalList=(List<SysInfo>) redisUtil.get(cacheKey);
				logger.info("读取首页医疗前沿缓存:key="+cacheKey);
			}else{
				medicalList = userPortalService.getList(info);
				if(medicalList!=null&&medicalList.size()>0){
					redisUtil.set(cacheKey, medicalList);
					logger.info("添加首页医疗前沿缓存:key="+cacheKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_YLQY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_YLQY", "首页管理_医疗前沿", "2", "0"), e);
		}
		String json=JSONUtils.toJson(medicalList,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询护理动态信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryNursing")
	public void queryNursing(){
		//护理动态
		SysInfo info = new SysInfo();
		info.setInfoPubflag(1);
		info.setInfoType(5);
		String cacheKey="";
		List<SysInfo> nursing = new ArrayList<SysInfo>();
		try {
			cacheKey=RedisManagerUtils.getClassNameByObj(this, "home-5");
			if(redisUtil.exists(cacheKey)){
				nursing=(List<SysInfo>) redisUtil.get(cacheKey);
				logger.info("读取首页护理动态缓存:key="+cacheKey);
			}else{
				nursing = userPortalService.getList(info);
				if(nursing!=null&&nursing.size()>0){
					redisUtil.set(cacheKey, nursing);
					logger.info("添加首页护理动态缓存:key="+cacheKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_HLDT", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_HLDT", "首页管理_护理动态", "2", "0"), e);
		}
		String json=JSONUtils.toJson(nursing,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}

	
	/**  
	 * 
	 * 查询日历信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryDate")
	public void queryDate(){
		//前台处理内容,后台不用返回值
		List<OaUserPortal> userPortal = new ArrayList<OaUserPortal>();
		String json=JSONUtils.toJson(userPortal);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询时间信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryTime")
	public void queryTime(){
		//前台处理内容,后台不用返回值
		List<OaUserPortal> userPortal = new ArrayList<OaUserPortal>();
		String json=JSONUtils.toJson(userPortal);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询天气信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryWeather")
	public void queryWeather(){
		//前台处理内容,后台不用返回值
		List<OaUserPortal> userPortal = new ArrayList<OaUserPortal>();
		String json=JSONUtils.toJson(userPortal);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询常用工具
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryUtils")
	public void queryUtils(){
		//前台常用工具增加一个apk下载的工具,需要查询出apk的下载地址
		Map<String,String> map = new HashMap<String,String>();
		try {
			MApkVersion apkVersion = userPortalService.getVersion();
			if(apkVersion!=null && StringUtils.isNotBlank(apkVersion.getApkDownloadAddr())){
				String address = HisParameters.getfileURI(ServletActionContext.getRequest());//判断内外网网址
				String fileurl = address+apkVersion.getApkDownloadAddr();
				String fileName =""; 
				if(StringUtils.isNotBlank(apkVersion.getApkVersionName())){
					fileName =apkVersion.getApkVersionName();
				}
				map.put("resCode", "success");
				map.put("fileurl", fileurl);
				map.put("fileName", fileName);
			}else{
				map.put("resCode", "error");
				map.put("fileurl", "");
				map.put("fileName", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("fileurl", "");
			map.put("fileName", "");
			logger.error("SYGL_CYGJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_CYGJ", "首页管理_常用工具", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 常用工具----扫码下载客户端
	 * @Author: zpty
	 * @CreateDate: 2017年8月11日 上午11:00:47 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	@Action(value = "downLoadApk")
	public void downLoadApk() {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			MApkVersion apkVersion = userPortalService.getVersion();
			if(apkVersion!=null && StringUtils.isNotBlank(apkVersion.getApkDownloadQRAddr())){
				String address = HisParameters.getfileURI(ServletActionContext.getRequest());//判断内外网网址
				String fileurl = address+apkVersion.getApkDownloadQRAddr();//二维码图片路径
				map.put("resCode", "success");
				map.put("fileurl", fileurl);
			}else{
				map.put("resCode", "error");
				map.put("fileurl", "");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("fileurl", "");
			logger.error("SYGL_SMXZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_SMXZ", "OA系统首页管理_扫码下载", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询信息管理信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryInformation")
	public void queryInformation(){
		//信息管理信息查询
		List<Information> information = new ArrayList<Information>();
		try {
			information = userPortalService.getInformationList(type);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_XXGLXX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_XXGLXX", "首页管理_信息管理信息", "2", "0"), e);
		}
		String json=JSONUtils.toJson(information,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 审批组件查询所有需要审批的数据(1:信息审批,2:工作流审批)
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryCheck")
	public void queryCheck(){
		List<Information> information = new ArrayList<Information>();//信息管理
		try {
			//信息审批数据查询
			information = userPortalService.getInformationCheck();
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_SPXX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_SPXX", "首页管理_审批信息", "2", "0"), e);
		}
		String json=JSONUtils.toJson(information,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询工作流信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月20日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryWorkProcess")
	public void queryWorkProcess(){
		WorkProcessManageVo workProcess = new WorkProcessManageVo();
		List<WorkProcessManageVo> workProcessList = new ArrayList<WorkProcessManageVo>();
		try {
			
//			List<ProcessInfoVo> list = workProcessManageService.queryProcessList(menuCode);
			
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_GZL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_GZL", "首页管理_工作流", "2", "0"), e);
		}
		String json=JSONUtils.toJson(workProcess,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询我的待办
	 * @Author: zpty
	 * @CreateDate: 2017年8月6日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryAgency")
	public void queryAgency(){
		List<OaTaskInfo> list = new ArrayList<OaTaskInfo>();
		try{
			String tenantId = tenantService.getTenantId();//租户id
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//登录账户
			if(StringUtils.isNotBlank(tenantId)){
				list = userPortalService.getListForTask(account,tenantId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_WDDB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_WDDB", "首页管理_我的待办", "2", "0"), e);
		}
		String json=JSONUtils.toJson(list,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询我的提醒
	 * @Author: zpty
	 * @CreateDate: 2017年8月6日 下午4:32:17 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	@Action(value = "queryRemind")
	public void queryRemind(){
		List<Schedule>  list=new ArrayList<Schedule>();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		try{
			//查询今天的提醒任务
			list=userPortalService.qeryScheduleList(userAccount);
		}catch(Exception e){
			e.printStackTrace();
			WebUtils.webSendJSON("error");
			logger.error("SYGL_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SYGL_RCAP", "首页管理_日程安排", "2", "0"), e);
		}
		String json = JSONUtils.toJson(list,"yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
}
