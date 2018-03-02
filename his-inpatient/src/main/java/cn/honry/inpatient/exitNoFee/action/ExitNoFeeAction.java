package cn.honry.inpatient.exitNoFee.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.exitNoFee.service.ExitNoFeeService;
import cn.honry.inpatient.exitNoFee.vo.InpatientInfoVo;
import cn.honry.inpatient.info.service.InpatientBedInfoService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: InpatientInfoAction.java 
 * @Description: 无费出院action
 * @author yeguanqun
 * @date 2015-12-3
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/exitNofee")
@SuppressWarnings({"all"})
public class ExitNoFeeAction extends ActionSupport{
	private Logger logger=Logger.getLogger(ExitNoFeeAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Autowired
	@Qualifier(value = "exitNoFeeService")
	private ExitNoFeeService exitNoFeeService;
	
	public void setExitNoFeeService(ExitNoFeeService exitNoFeeService) {
		this.exitNoFeeService = exitNoFeeService;
	}
	
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@Autowired
	@Qualifier(value = "inpatientBedInfoService")
	private InpatientBedInfoService bedInfoService;
	public void setBedInfoService(InpatientBedInfoService bedInfoService) {
		this.bedInfoService = bedInfoService;
	}
	
	
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public PatinentInnerService getPatinentInnerService() {
		return patinentInnerService;
	}
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	private InpatientInfoNow inpatientInfo;
	private List<InpatientInfo> inpatientInfoList;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private String page;//起始页数
	private String rows;//数据列数
	/**
	 * 患者Id
	 */
	private String ids;
	/**
	 * 本次费用
	 */
	private double thisTotCost =0;
	/**
	 * 欠费额度
	 */
	private double arrearageCost=0;
	/**
	 * 用户实体类
	 */
	private User user;
	/**
	 * 医嘱分解天数
	 */
	private String idbobob;
	/**
	 * 医嘱Id
	 */
	private String orderInfoId;
	
	/**
	 * 手术序号
	 */
	private String operationId;
	
	
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	private String inpatientNoMerc;
	public String getInpatientNoMerc() {
		return inpatientNoMerc;
	}
	public void setInpatientNoMerc(String inpatientNoMerc) {
		this.inpatientNoMerc = inpatientNoMerc;
	}
	/**
	 * @Description:获取list页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-3
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"WFCYGL:function:view"})
	@Action(value="exitNoFeeList",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/exitNofee/exitNofeeList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientInfoList()throws Exception{
		return "list";
	}
	/**
	 * @Description:获取患者页面
	 * @version 1.0
	**/
	@Action(value="inpatientInfoListxuanze",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/exitNofee/exitNofeeInfo.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientInfoListxuanze()throws Exception{
		return "list";
	}
	/**
	 * @Description:查询列表
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-3
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryExitNoFee", results = { @Result(name = "json", type = "json") })
	public void queryInpatientInfo() {
		try {
			List<InpatientInfoVo> inpatientInfoList = new ArrayList<InpatientInfoVo>();
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String idcardNo=inpatientInfo.getIdcardNo();
			if(StringUtils.isNotBlank(idcardNo)){
				idcardNo=idcardNo.trim();
				}	
			//通过接口查询就诊卡号对应的病历号
			String medicalrecordId = patinentInnerService.getMedicalrecordId(idcardNo);
			if(StringUtils.isNotBlank(medicalrecordId)){
				inpatientInfo.setMedicalrecordId(medicalrecordId);
				inpatientInfoList = exitNoFeeService.getInpatientInfo(inpatientInfo);
			}
			String json = JSONUtils.toJson(inpatientInfoList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询列表
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-3
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryExitNoFeeM", results = { @Result(name = "json", type = "json") })
	public void queryExitNoFeeM(){
		try {
			List<InpatientInfoVo> inpatientInfoList = new ArrayList<InpatientInfoVo>();
			
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId=inpatientInfo.getMedicalrecordId();
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
				inpatientInfo.setMedicalrecordId(medicalrecordId);
				inpatientInfoList = exitNoFeeService.getInpatientInfo(inpatientInfo);
			}
			String json = JSONUtils.toJson(inpatientInfoList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
	@Action(value = "queryExitNoFeeByInNo", results = { @Result(name = "json", type = "json") })
	public void queryExitNoFeeByInNo() {
		try {
			List<InpatientInfoVo> inpatientInfoList = new ArrayList<InpatientInfoVo>();
			inpatientInfoList = exitNoFeeService.getInpatientInfoByInNo(inpatientInfo);
			String json = JSONUtils.toJson(inpatientInfoList,"yyyy-MM-dd");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:无费退院
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-3
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"WFCYGL:function:exit"})
	@Action(value = "exitNoFeeInpatient")
	public void exitNoFeeInpatient(){
		try{
			Map<String,Object> map=new HashMap<String,Object>();		
			String flag=exitNoFeeService.changeHospitalState(ids,inpatientInfo);
			if(("0").equals(flag)){
				map.put("resMsg", "success");
			}else{
				map.put("resMsg", "error");	
			}		
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);		
		}catch(Exception e ){
			WebUtils.webSendJSON("error");
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
		
	/**
	 * @Description:查询登录密码-校验登录密码
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-3-4
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "confirmPassword", results = { @Result(name = "json", type = "json") })
	public void confirmPassword(){	
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			List<User> userList = exitNoFeeService.confirmPassword(user);
			if(userList!=null&&userList.size()>0){
				map.put("resMsg", "success");
			}else{
				map.put("resMsg", "error");
				map.put("resCode", "密码错误或用户已不存在!");
			}		
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：收费接口-跳转到提示欠费页面
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-14 上午11:34:22  
	 * @Modifier：yeguanqun
	 * @ModifyDate：2016-4-14 上午11:34:22  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "arrearageInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/exitNofee/arrearageInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String arrearageInfo() {
		try {
			if(inpatientInfo==null){
				inpatientInfo = new InpatientInfoNow();
			}
			List<InpatientInfoNow> inpatientInfoList = exitNoFeeService.queryInpatientInfo(inpatientInfo.getInpatientNo());
			if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
				inpatientInfo = inpatientInfoList.get(0);
				double freeCost = 0;
				if(inpatientInfo.getFreeCost()!=null){
					freeCost = inpatientInfo.getFreeCost();
				}
				arrearageCost = freeCost-thisTotCost;
				if(arrearageCost<0){
					arrearageCost=-arrearageCost;
				}
				 
			}
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
		
		return "list";
	}
	/**
	 * 根据患者住院流水号查询患者信息
	 * 
	 */
	@Action(value="queryInpatient")
	public void queryInpatient(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			if(inpatientInfo==null){
				inpatientInfo = new InpatientInfoNow();
			}
			List<InpatientInfoNow> inpatientInfoList = exitNoFeeService.queryInpatientInfo(inpatientInfo.getInpatientNo());		
			String json = JSONUtils.toJson(inpatientInfoList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:查看患者详细信息
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-3
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryPatientInfo", results = { @Result(name = "json", type = "json") })
	public void queryPatientInfo(){
		try {
			List<InpatientInfoVo> inpatientInfoList = exitNoFeeService.getInpatientInfo(inpatientInfo);
			String json = JSONUtils.toJson(inpatientInfoList,"yyyy-MM-dd");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_WFCY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_WFCY", "住院管理_无费出院", "2", "0"), e);
		}
	}
	
	
	public InpatientInfoNow getInpatientInfo() {
		return inpatientInfo;
	}

	public void setInpatientInfo(InpatientInfoNow inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	public List<InpatientInfo> getInpatientInfoList() {
		return inpatientInfoList;
	}
	public void setInpatientInfoList(List<InpatientInfo> inpatientInfoList) {
		this.inpatientInfoList = inpatientInfoList;
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
	public double getThisTotCost() {
		return thisTotCost;
	}
	public void setThisTotCost(double thisTotCost) {
		this.thisTotCost = thisTotCost;
	}
	public double getArrearageCost() {
		return arrearageCost;
	}
	public void setArrearageCost(double arrearageCost) {
		this.arrearageCost = arrearageCost;
	}
	public String getIdbobob() {
		return idbobob;
	}
	public void setIdbobob(String idbobob) {
		this.idbobob = idbobob;
	}
	public String getOrderInfoId() {
		return orderInfoId;
	}
	public void setOrderInfoId(String orderInfoId) {
		this.orderInfoId = orderInfoId;
	}
	
}
