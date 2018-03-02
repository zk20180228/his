package cn.honry.oa.userSign.action;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OaUserSign;
import cn.honry.base.bean.model.OaUserSignChange;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysRole;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.oa.userSign.service.UserSignService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**  
 *  
 * @className：UserSignAction
 * @Description：  用户电子签章维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-14 下午16:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-14 下午16:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/userSign")
public class UserSignAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(UserSignAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	@Autowired
	@Qualifier(value = "userSignService")
	private UserSignService userSignService;
	public void setUserSignService(UserSignService userSignService) {
		this.userSignService = userSignService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private String userAccount;
	private String userName;
	private String menuAlias;
	private String password;
	private String signid;
	private String page;
	private String q;
	private String rows;
	
	private String search;
	
	private OaUserSign userSign;
	
	private File signInfoFile;
	
	private String type;
	private String signType;
	private String id;
	private String version;
	
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSignid() {
		return signid;
	}

	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public void setSignid(String signid) {
		this.signid = signid;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public OaUserSign getUserSign() {
		return userSign;
	}

	public void setUserSign(OaUserSign userSign) {
		this.userSign = userSign;
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
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	public File getSignInfoFile() {
		return signInfoFile;
	}

	public void setSignInfoFile(File signInfoFile) {
		this.signInfoFile = signInfoFile;
	}

	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZGL:function:view"})
	@Action(value = "listUserSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userSignList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listUserSign() {
		return "list";
	}
	/**  
	 *  
	 * @Description：  跳转list个人页面
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "listUserOneSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userOneSignList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listUserOneSign() {
		return "list";
	}
	/**  
	 *  
	 * @Description：  获取列表
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZGL:function:query"}) 
	@Action(value = "queryUserSignList")
	public void queryUserSignList() {
		Map<String,Object> map = new HashMap<String, Object>();
		int total = userSignService.getUserSignTotal(StringUtils.isNotBlank(search)?search.toUpperCase():null);
		List<OaUserSign> list = userSignService.getUserSignRows(page,rows,StringUtils.isNotBlank(search)?search.toUpperCase():null);
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  获取个人签名列表
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryOneUserSignList")
	public void queryOneUserSignList() {
		List<OaUserSign> list = userSignService.getUserOneSignRows(StringUtils.isNotBlank(search)?search.toUpperCase():null,userAccount);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  验证密码是否正确
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getSignRow")
	public void getSignRow() {
		OaUserSign oaUserSign = userSignService.getSignRow(signid, password, userAccount);
		if(oaUserSign!=null&&oaUserSign.getSignPassword()!=null){
			oaUserSign.setSignPassword(convertMD5(oaUserSign.getSignPassword()));
		}
		String json = JSONUtils.toJson(oaUserSign);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  停用方法
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "stopSign")
	public void stopSign() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			userSignService.stop(signid);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "停用成功!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "停用失败!");
			logger.error("电子签章修改失败：", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章维护_修改", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  删除方法
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "deleteSign")
	public void deleteSign() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			userSignService.delete(signid);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败!");
			logger.error("电子签章修改失败：", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章维护_修改", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZGL:function:add"})
	@Action(value = "addUserSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userSignEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addUserSign() {
//		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
//		userSign=new OaUserSign();
//		userSign.setUserAcc(user.getAccount());
//		userSign.setUserAccName(user.getName());
//		userAccount = user.getAccount();
//		userName = user.getName();
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZGL:function:edit"})
	@Action(value = "editUserSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userSignEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editUserSign() {
		userSign = userSignService.get(search);
        userSign.setSignPassword(convertMD5(userSign.getSignPassword()));
		return "list";
	}
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "addOneUserSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userOneSignEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addOneUserSign() {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		userAccount = user.getAccount();
		userName = user.getName();
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "editOneUserSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userOneSignEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editOneUserSign() {
		userSign = userSignService.get(search);
        userSign.setSignPassword(convertMD5(userSign.getSignPassword()));
		return "list";
	}
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryOpen", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/userSignOpen.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryOpen() {
		return "list";
	}
	/**
	 * 解析二进制数据
	 */
	@Action(value = "getuserSignInfo")
	public void getuserSignInfo() {
		try {
			userSign = userSignService.get(search);
			Blob blob = userSign.getSignInfo();
			InputStream inputStream = blob.getBinaryStream();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("image/*"); // 设置返回的文件类型
            OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
            int len;
            byte[] bs = new byte[1024];
            while ((len = inputStream.read(bs)) != -1) {
                toClient.write(bs, 0, len);
            }
            toClient.flush();
            toClient.close();
		} catch (Exception e) {
			     
		}
	}
	/**  
	 *  
	 * @Description：  查询所有员工信息
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午14:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午14:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getEmpInfo")
	public void getEmpInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysEmployee> list = new ArrayList<SysEmployee>();
		int total = 0 ;
		try {
			if("1".equals(type)){
				list = employeeInInterService.getAffuseDoctor(rows, page, q);
				total = employeeInInterService.getAffuseDoctorTotal(q);
			}else if("2".equals(type)){
				List<SysRole> roles = userSignService.getSysRole( page,  rows,q);
				total = userSignService.getSysRoleTotal(q);
				for (SysRole sysRole : roles) {
					SysEmployee employee = new SysEmployee();
					employee.setJobNo(sysRole.getAlias());
					employee.setName(sysRole.getName());
					list.add(employee);
				}
			}else if("3".equals(type)){
				List<BusinessDictionary> businessDictionaries = userSignService.getBusinessDictionary( page,  rows,q);
				total = userSignService.getBusinessDictionaryTotal(q);
				for (BusinessDictionary businessDictionary : businessDictionaries) {
					SysEmployee employee = new SysEmployee();
					employee.setJobNo(businessDictionary.getEncode());
					employee.setName(businessDictionary.getName());
					list.add(employee);
				}
			}else if("4".equals(type)){
				List<SysDepartment> departments = deptInInterService.getPageByQ(page,rows,q);
				total = deptInInterService.getTotalByQ(q);
				for (SysDepartment sysDepartment : departments) {
					SysEmployee employee = new SysEmployee();
					employee.setJobNo(sysDepartment.getDeptCode());
					employee.setName(sysDepartment.getDeptName());
					list.add(employee);
				}
			}
		} catch (Exception e) {
			logger.error("电子签章修改失败：", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章维护_查询", "2", "0"), e);
		}
		map.put("total", total);
//		Gson gson = new Gson();
//		String json=gson.toJson(list, SysEmployee.class);
		map.put("rows", list);
		String json=JSONUtils.toExposeJson(map, false, null, "name","jobNo");
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  保存电子签章信息
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-14 下午17:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-14 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveUserSign")
	public void saveUserSign() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		userSign.setSignPassword(convertMD5(userSign.getSignPassword()));
		if(StringUtils.isNotBlank(userSign.getId())){
			if(SecurityUtils.getSubject().isPermitted("YZGL:function:edit")){
				try {
					userSignService.save(userSign,signInfoFile);
					retMap.put("resCode", "success");
					retMap.put("resMsg", "修改成功!");
				} catch (Exception e) {
					retMap.put("resCode", "error");
					retMap.put("resMsg", "修改失败!");
					logger.error("电子签章修改失败：", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章维护_修改", "2", "0"), e);
				}
				
			}else{
				retMap.put("resCode", "error");
				retMap.put("resMsg", "无修改权限，请联系管理员进行授权!");
			}
		}else {
			if(SecurityUtils.getSubject().isPermitted("YZGL:function:add")){
				try {
					userSignService.save(userSign,signInfoFile);
					retMap.put("resCode", "success");
					retMap.put("resMsg", "添加成功!");
				} catch (Exception e) {
					retMap.put("resCode", "error");
					retMap.put("resMsg", "添加失败!");
					logger.error("电子签章添加失败：", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章维护_添加", "2", "0"), e);
				}
			}else{
				retMap.put("resCode", "error");
				retMap.put("resMsg", "无添加权限，请联系管理员进行授权!");
			}
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * 
	 * <p> 用户信息个人签名保存方法</p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月7日 上午10:50:23 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月7日 上午10:50:23 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "saveExecSign")
	public void saveExecSign() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		userSign.setSignPassword(convertMD5(userSign.getSignPassword()));
		try {
			userSignService.save(userSign,signInfoFile);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "修改成功!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "修改失败!");
			logger.error("电子签章修改失败：", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章维护_修改", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	 /*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }
    /**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：donghe
	 * @CreateDate：2017-7-19 下午17:56:59  
	 * @Modifier：donghe
	 * @ModifyDate：2017-7-19 下午17:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "oneUserSign", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/userSign/oneUserSign.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String oneUserSign() {
		userSign = userSignService.getElecSign(userAccount);
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		userAccount = user.getAccount();
		userName = user.getName();
		if(userSign.getId()==null||"".equals(userSign.getId())){
		}else{
			userSign.setSignPassword(convertMD5(userSign.getSignPassword()));
		}
		return "list";
	}
	/**
	 * @Description:通过员工号查询员工所能使用的签名
	 * @Author: donghe
	 * @CreateDate: 2017年7月20日
	 * @param:account-员工号
	 * @return:List<OaUserSign>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryOaUserSigns")
	public void queryOaUserSigns() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			List<OaUserSign> list = userSignService.queryOaUserSigns(userAccount);
			List<OaUserSign> list1 = new ArrayList<OaUserSign>();
			List<OaUserSign> list2 = new ArrayList<OaUserSign>();
			String namespace = "/oa/userSign/";
			String action = "getuserSignInfoById.action";
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setUrl("http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+namespace+action);
				if(list.get(i).getSignType()==1){
					list1.add(list.get(i));
				}else if(list.get(i).getSignType()==2){
					list2.add(list.get(i));
				}
			}
			retMap.put("seal", list2);//签章
			retMap.put("sign", list1);//签名
			retMap.put("resCode", "success");
			retMap.put("resMsg", "查询成功!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "查询失败!");
			logger.error("电子签章查询失败：", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章查询失败", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:通过id和版本号查询员工所能使用的签名
	 * @Author: donghe
	 * @CreateDate: 2017年7月20日
	 * @param:account-员工号
	 * @return:List<OaUserSign>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value = "queryOaUserSignByid")
	public void queryOaUserSignByid() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			OaUserSign oaUserSign = userSignService.queryOaUserSignByid(id, version);
			retMap.put("data", oaUserSign);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "查询成功!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "查询失败!");
			logger.error("电子签章查询失败：", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZGL", "用户电子签章查询失败", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 解析二进制数据
	 */
	@Action(value = "getuserSignInfoById")
	public void getuserSignInfoById() {
		try {
			Blob blob = null;
			userSign = userSignService.queryOaUserSignByid(id,version);
			if(userSign.getId()==null){
				OaUserSignChange change = userSignService.queryOaUserSignChangeByid(id, version);
				blob = change.getNewSignInfo();
			}else{
				blob = userSign.getSignInfo();
			}
			InputStream inputStream = blob.getBinaryStream();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("image/*"); // 设置返回的文件类型
            OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
            int len;
            byte[] bs = new byte[1024];
            while ((len = inputStream.read(bs)) != -1) {
                toClient.write(bs, 0, len);
            }
            toClient.flush();
            toClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
