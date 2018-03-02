package cn.honry.oa.wages.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.honry.base.bean.model.OaWages;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.wages.service.WagesService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.MD5;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

/**
 * @Description: 工资管理action
 * @author zx
 * @date 2017年7月17日
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/Wages")
public class WagesAction extends ActionSupport implements ModelDriven<OaWages> {
	private Logger logger=Logger.getLogger(ActionSupport.class);
	private static final long serialVersionUID = 1L;
	private String weagesPassword;
	private OaWages oaWages = new OaWages();
	/** 
	* 工作表
	*/ 
	private Workbook wb;
	/** 
	* 表格 
	*/ 
	private Sheet sheet;
	/** 
	* 行
	*/ 
	private Row row;
	/**
	 * 工资月份
	 */
	private String wagesTime;
	/**
	 * 工资号
	 */
	private String wagesAccount;
	/**
	 * 员工姓名
	 */
	private String name;
	/**
	 * 分页
	 */
	private String page;
	/**
	 * 分页
	 */
	private String rows;
	/**
	 * 上传Excel文件
	 */
	private File fileEsign;

	/**
	 * 上传Excel文件名
	 */
	private String fileEsignFileName;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	/** 工资管理Service **/
	@Autowired
	@Qualifier(value = "wagesService")
	private WagesService wagesService;
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	public void setWagesService(WagesService wagesService) {
		this.wagesService = wagesService;
	}

	public String getWagesAccount() {
		return wagesAccount;
	}

	public void setWagesAccount(String wagesAccount) {
		this.wagesAccount = wagesAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWagesTime() {
		return wagesTime;
	}

	public void setWagesTime(String wagesTime) {
		this.wagesTime = wagesTime;
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

	public File getFileEsign() {
		return fileEsign;
	}

	public void setFileEsign(File fileEsign) {
		this.fileEsign = fileEsign;
	}

	public String getFileEsignFileName() {
		return fileEsignFileName;
	}

	public void setFileEsignFileName(String fileEsignFileName) {
		this.fileEsignFileName = fileEsignFileName;
	}

	public String getWeagesPassword() {
		return weagesPassword;
	}

	public void setWeagesPassword(String weagesPassword) {
		this.weagesPassword = weagesPassword;
	}

	@Override
	public OaWages getModel() {
		return oaWages;
	}
	

	/** 
	* @Description: 人事处页面 
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "wagesManageInfo", results = {@Result(name = "list", location = "/WEB-INF/pages/oa/wages/wagesInfo.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String wagesManageList() {
		return "list";
	}
	/** 
	* @Description: 工资管理页面 
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "wagesManage", results = {@Result(name = "list", location = "/WEB-INF/pages/oa/wages/wagesList.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String wagesManage() {
		return "list";
	}
	/** 
	* @Description: 个人查询页面
	* @return
	* @return String    返回类型 
	* @throws 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "toEmployee", results = {@Result(name = "list", location = "/WEB-INF/pages/oa/wages/wagesEmployee.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String toEmployee() {
		return "list";
	}
	/** 
	* @Description: 修改工资查询密码页面 
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "updatePwdList", results = {@Result(name = "list", location = "/WEB-INF/pages/oa/wages/updataUserPWD.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String updatePwdList() {
		try {
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			if(user!=null&&StringUtils.isNotBlank(user.getAccount())){
				SysEmployee employee = wagesService.checkAccountForInit(user.getAccount());
				if(StringUtils.isNotBlank(employee.getWagesPassword())){
					request.setAttribute("wagesAccount", user.getAccount());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("工资 管理", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		return "list";
	}
	/** 
	* @Description: 初始化工资查询密码页面
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "wagesInitList", results = {@Result(name = "list", location = "/WEB-INF/pages/oa/wages/wagesPassInit.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String wagesInitList() {
		return "list";
	}
	/** 
	* @Description: 加载工资管理树
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "queryColumns")
	public void queryColumns(){
		List<TreeJson> list = null;
		try {
			//查询工资管理树
			list = wagesService.queryColumns();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("工资管理树加载失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(list);
    	WebUtils.webSendJSON(json);
	}
	/** 
	* @Description: 工资查询密码初始化 
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "wagesInit")
	public void wagesInit(){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			if(user!=null&&StringUtils.isNotBlank(user.getAccount())){
				//查询是否已经设置过密码
				SysEmployee employee = wagesService.checkAccountForInit(user.getAccount());
				if(StringUtils.isNotBlank(employee.getWagesPassword())){
					map.put("resCode", "error");
					map.put("resMsg", "你已经设置过密码,请选择修改密码!");
				}else{
					//初始化查询密码
					String result = wagesService.initPasswordToWeages(user.getAccount(), MD5.MD5Encode("0000")); 
					if("success".equals(result)){
						map.put("resCode", "success");
						map.put("resMsg", "密码初始化成功");
					}else{
						map.put("resCode", "error");
						map.put("resMsg", "密码初始化失败！");
					}
				}
			}else{
				map.put("resCode", "error");
				map.put("resMsg", "密码初始化失败！");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "密码初始化失败！");
			e.printStackTrace();
			logger.error("工资查询密码初始化失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/** 
	* @Description: 员工自助查询登录
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "loginWages")
	public void loginWages() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			//登录账号的类型
			String accountType = request.getParameter("accountType");
			//登录账号
			String wagesAccount = request.getParameter("wagesAccount");
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			if(user!=null&&StringUtils.isNotBlank(user.getAccount())){
				if("2".equals(accountType)){
					//查询当前登录人的员工信息
					SysEmployee jobInfo = wagesService.getIcdFoUser(user.getAccount());
					if(jobInfo!=null &&StringUtils.isNoneBlank(jobInfo.getWagesAccount())){
						//工资账号
						if(wagesAccount.equals(jobInfo.getWagesAccount())){
							//查询是否已经设置过密码
							SysEmployee empl = wagesService.checkAccountForInit(jobInfo.getWagesAccount());
							if(StringUtils.isNotBlank(empl.getWagesPassword())){
								// 验证密码是否正确
								SysEmployee employee = wagesService.checkAccountByAId(wagesAccount, MD5.MD5Encode(weagesPassword));
								if (employee!=null&&(StringUtils.isNotBlank(employee.getWagesAccount())||StringUtils.isNotBlank(employee.getWagesPassword()))) {
									map.put("resCode", "success");
									map.put("resMsg", "登录成功");
								} else {
									map.put("resCode", "error");
									map.put("resMsg", "密码不正确，请核准！");
								}
							}else{
								map.put("resCode", "error");
								map.put("resMsg", "第一次登录请在个人中心设置查询密码!");
							}
						}else{
							map.put("resCode", "error");
							map.put("resMsg", "只能查询自己的工资信息,请核准信息！");
						}
					}else{
						map.put("resCode", "error");
						map.put("resMsg", "员工信息有误！");
					}
				}
				else if("1".equals(accountType)){
					//查询当前登陆人的身份证信息
					SysEmployee employee = wagesService.getIcdFoUser(user.getAccount());
					if (employee!=null&&StringUtils.isNotBlank(employee.getIdEntityCard())) {
						if(employee.getIdEntityCard().equals(wagesAccount)){
							//查询是否已经设置过密码
							SysEmployee empl = wagesService.checkAccountForInit(employee.getWagesAccount());
							if(StringUtils.isNotBlank(empl.getWagesPassword())){
								// 验证密码是否正确
								SysEmployee emp = wagesService.checkAccountByAId(wagesAccount, MD5.MD5Encode(weagesPassword));
								if (emp!=null&&(StringUtils.isNotBlank(emp.getWagesAccount())||StringUtils.isNotBlank(emp.getWagesPassword()))) {
									map.put("resCode", "success");
									map.put("resMsg", "登录成功");
								} else {
									map.put("resCode", "error");
									map.put("resMsg", "密码不正确，请核准！");
								}
							}else{
								map.put("resCode", "error");
								map.put("resMsg", "第一次登录请在个人中心设置查询密码!");
							}
						}else{
							map.put("resCode", "error");
							map.put("resMsg", "只能查询自己的工资信息,请核准信息！");
						}
					}else{
						map.put("resCode", "error");
						map.put("resMsg", "请核查你的个人资料是否预留了身份证号");
					}
				}
			}else{
				map.put("resCode", "error");
				map.put("resMsg", "未知错误请联系管理员");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "员工自助查询登录失败！");
			e.printStackTrace();
			logger.error("员工自助查询登录失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/** 
	* @Description: 修改工资查询密码 
	* @throws Exception
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value="userUpdataPWD")
	public void userUpdataPWD() throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		try {
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String txtPassword = request.getParameter("txtPassword");
			User user = (User)SessionUtils.getCurrentUserFromShiroSession();
			if(user!=null&&StringUtils.isNotBlank(user.getAccount())){
				if(StringUtils.isNotBlank(oldPassword)){
					String userPwdOld=new MD5().MD5Encode(oldPassword);
					//查询原密码
					SysEmployee employee = wagesService.checkAccount(user.getAccount(), userPwdOld);
					if(!userPwdOld.equals(employee.getWagesPassword())){
						map.put("resCode", "error");
						map.put("resMsg", "原密码出入有误!");
					}else{
						if(StringUtils.isBlank(newPassword)||StringUtils.isBlank(txtPassword)){
							map.put("resCode", "error");
							map.put("resMsg", (StringUtils.isBlank(newPassword)?"新":"确认")+"密码为空!");
						}else{
							String userPwdNew=new MD5().MD5Encode(newPassword);
							if(oldPassword.equals(newPassword)){
								map.put("resCode", "error");
								map.put("resMsg", "原密码和新密码太相似，请重新输入!");
							}else if(!newPassword.equals(txtPassword)){
								map.put("resCode", "error");
								map.put("resMsg", "两次输入的密码不一致，请重新输入!");
							}else{
								//更新工资查询密码
								String result=wagesService.initPasswordToWeages(user.getAccount(),userPwdNew);
								if("success".equals(result)){
									map.put("resCode", "success");
									map.put("resMsg", "设置成功!");
								}else{
									map.put("resCode", "error");
									map.put("resMsg", "未知错误，请重新设置！");
								}
							}
						}
					
					}
				}else{
					String userPwdNew=new MD5().MD5Encode(newPassword);
					if(!newPassword.equals(txtPassword)){
						map.put("resCode", "error");
						map.put("resMsg", "两次输入的密码不一致，请重新输入!");
					}else{
						//更新工资查询密码
						String result=wagesService.initPasswordToWeages(user.getAccount(),userPwdNew);
						if("success".equals(result)){
							map.put("resCode", "success");
							map.put("resMsg", "设置成功!");
						}else{
							map.put("resCode", "error");
							map.put("resMsg", "未知错误，请重新设置！");
						}
					}
				}
			}else{
				map.put("resCode", "error");
				map.put("resMsg", "未知错误，请重新登录！");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "工资查询密码修改失败！");
			e.printStackTrace();
			logger.error("工资查询密码修改失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/** 
	* @Description: 分页查询工资信息 
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "listWagesQuery")
	public void listWagesQuery() {
		String wagesAccount = request.getParameter("wagesAccount");
		String name = request.getParameter("name");
		String wagesTime = request.getParameter("wagesTime");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//查询工资数据
			map = wagesService.listWagesQuery(wagesAccount, name, wagesTime, page, rows);
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList<OaWages>());
			e.printStackTrace();
			logger.error("员工自助查询登录失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/** 
	 * @Description: 员工自助查询工资信息 
	 * @return void    返回类型 
	 * @author zx 
	 * @date 2017年7月24日
	 */
	@Action(value = "listWagesPersonQuery")
	public void listWagesPersonQuery() {
		Map<String, Object> map = new HashMap<String, Object>();
		String accountType = request.getParameter("accountType");
		String wagesAccount = request.getParameter("wagesAccount");
		String name ="";
		String account="";
		if("2".equals(accountType)){
			account = wagesAccount;
		}else if("1".equals(accountType)){
			name = wagesAccount;
		}
		try {
			if(StringUtils.isNotBlank(name)||StringUtils.isNotBlank(account)){
				map = wagesService.listWagesQuery(account, name, wagesTime, page, rows);
			}else{
				map.put("total", 0);
				map.put("rows", new ArrayList<OaWages>());
			}
			
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList<OaWages>());
			e.printStackTrace();
			logger.error("员工自助查询工资信息失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/** 
	 * @Description: 员工自助查询工资信息 
	 * @return void    返回类型 
	 * @author zx 
	 * @date 2017年7月24日
	 */
	@Action(value = "queryInfoByTime")
	public void queryInfoByTime() {
		Map<String, Object> map = new HashMap<String, Object>();
		String wagesTime = request.getParameter("wagesTime");
		String name ="";
		String account="";
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		if(user!=null&&StringUtils.isNotBlank(user.getAccount())){
			account = user.getAccount();
			try {
				if(StringUtils.isNotBlank(name)||StringUtils.isNotBlank(account)){
					//查询工资数据
					map = wagesService.listWagesQuery(account, name, wagesTime, page, rows);
				}else{
					map.put("total", 0);
					map.put("rows", new ArrayList<OaWages>());
				}
				
			} catch (Exception e) {
				map.put("total", 0);
				map.put("rows", new ArrayList<OaWages>());
				e.printStackTrace();
				logger.error("员工自助查询工资信息失败", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
			}
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/** 
	* @Description: 解析Excel文件上传数据
	* @throws Exception
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "saveOrUpdateEmployee")
	public void saveOrUpdate() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String userAccount = "";
		String deptCode = "";
		String wagesTime = ServletActionContext.getRequest().getParameter("wagesTime");
		Date date1 = DateUtils.parseDateY_M(wagesTime);
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();// 当前用户
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();// 当前科室
		if (user != null && StringUtils.isNotBlank(user.getAccount())) {
			userAccount = user.getAccount();
		}
		if (dept != null && StringUtils.isNotBlank(dept.getDeptCode())) {
			deptCode = dept.getDeptCode();
		}
		try {
			if (null != fileEsign) {
				//上传文件的输入流
				InputStream is = new FileInputStream(fileEsign.getAbsolutePath());
				wb = WorkbookFactory.create(is);
				sheet = wb.getSheetAt(0);
				List<OaWages> OaWagesList = new ArrayList<OaWages>();
				// 获取当前工作薄的每一行
				for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
					row = sheet.getRow(rowNum);
					if (row != null) {
						if (StringUtils.isNotBlank(getCellValue(row.getCell(1)))) {
							if (StringUtils.isNotBlank(getCellValue(row.getCell(2)))) {
								OaWages wages = new OaWages();
								wages.setWagesAccount(getCellValue(row.getCell(1)));
								wages.setName(getCellValue(row.getCell(2)));
								wages.setDeptName(getCellValue(row.getCell(3)));
								wages.setCategory(getCellValue(row.getCell(4)));
								wages.setPostPay(getCellValue(row.getCell(5)));
								wages.setBasePay(getCellValue(row.getCell(6)));
								wages.setNursinTeach(getCellValue(row.getCell(7)));
								wages.setAchievements(getCellValue(row.getCell(8)));
								wages.setNursinTeaching(getCellValue(row.getCell(9)));
								wages.setKeepThink(getCellValue(row.getCell(10)));
								wages.setHealthAllowance(getCellValue(row.getCell(11)));
								wages.setOnlyChildFee(getCellValue(row.getCell(12)));
								wages.setHygieneFee(getCellValue(row.getCell(13)));
								wages.setPHDFee(getCellValue(row.getCell(14)));
								wages.setSubsidyFee(getCellValue(row.getCell(15)));
								wages.setIncreased(getCellValue(row.getCell(16)));
								wages.setIncreasing(getCellValue(row.getCell(17)));
								wages.setTotalShould(getCellValue(row.getCell(18)));
								wages.setDeductRent(getCellValue(row.getCell(19)));
								wages.setHousingFund(getCellValue(row.getCell(20)));
								wages.setBoardingFee(getCellValue(row.getCell(21)));
								wages.setMedicalInsurance(getCellValue(row.getCell(22)));
								wages.setOverallPlanning(getCellValue(row.getCell(23)));
								wages.setUnemploymentInsurance(getCellValue(row.getCell(24)));
								wages.setDeductWages(getCellValue(row.getCell(25)));
								wages.setHeatingCosts(getCellValue(row.getCell(26)));
								wages.setAccountEeceivable(getCellValue(row.getCell(27)));
								wages.setTotalActual(getCellValue(row.getCell(28)));
								wages.setProvidentFundAccount(getCellValue(row.getCell(29)));
								wages.setIDCard(getCellValue(row.getCell(30)));
								wages.setCreateUser(userAccount);
								wages.setCreateDept(deptCode);
								wages.setCreateTime(new Date());
								wages.setWagesTime(date1);
								OaWagesList.add(wages);
							} else {
								OaWagesList.removeAll(OaWagesList);
								rowNum = rowNum+1;
								map.put("resCode", "noName");
								map.put("resMsg", "Excel表格中第" + rowNum + "行没有员工姓名,请核查!");
								break;
							}
						} else {
							OaWagesList.removeAll(OaWagesList);
							rowNum = rowNum+1;
							map.put("resCode", "noAccount");
							map.put("resMsg", "Excel表格中第" + rowNum + "行没有工资号,请核查!");
							break;
						}
					}
				}
				if(OaWagesList!=null&&OaWagesList.size()>0){
					//批量插入数据
					wagesService.insertWagesByBatch(OaWagesList);
					map.put("resCode", "true");
					map.put("resMsg", "导入成功");
				}
			} else {
				map.put("resCode", "null");
				map.put("resMsg", "上传文件不能为空!");
			}
		} catch (FileNotFoundException e) {
			map.put("resCode", "error");
			map.put("resMsg", "导入失败");
			e.printStackTrace();
			logger.error("Excel数据导入失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		} catch (IOException e) {
			map.put("resCode", "error");
			map.put("resMsg", "导入失败");
			e.printStackTrace();
			logger.error("Excel数据导入失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/** 
	* @Description: 导出
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Action(value = "exportList")
	public void exportList() {
		try {
			// jasper文件名称 不含后缀
			String eReportTime = request.getParameter("eReportTime");// 表头时间
			String wagesAccount = request.getParameter("exeReportAccount");
			String wagesName = request.getParameter("exeReportName");
			List<OaWages> list = wagesService.listWagesQueryForExport(wagesAccount, wagesName, eReportTime);
			String[] time = eReportTime.split("-");
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录!");
			}
			String name = time[0] + "年" + time[1] + "月工资表";
			String head = "";
			//表头信息
			String[] headMessage = { "工资号", "姓名", "工资月份", "职务工资","基本工资", "保险基数","养老保险", "单位养老","个人养老","医疗保险","单位医疗","个人医疗","生育保险","单位生育","失业保险",
					"单位失业","个人失业","工伤保险","单位工伤","住房公积金","单位住房","个人住房"
			};
			
			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/"
					+ fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			//导出
			fUtil = wagesService.export(list, fUtil);
			fUtil.close();
			//下载
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据导出失败", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GZGL", "工资管理", "2", "0"), e);
		}
	}

	/** 
	* @Description: 获取单元格的值
	* @param cell 单元格
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	private static String getCellValue(Cell cell) {
		String cellValue = "";
		DataFormatter formatter = new DataFormatter();
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = formatter.formatCellValue(cell);
				} else {
					double value = cell.getNumericCellValue();
					int intValue = (int) value;
					cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		} else {
			return "";
		}
		return cellValue.trim();
	}

}
