package cn.honry.outpatient.preregister.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.outpatient.preregister.service.BusinessPreregisterService;
import cn.honry.outpatient.preregister.vo.CalendarVO;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.outpatient.preregister.vo.IdCardPreVo;
import cn.honry.outpatient.preregister.vo.RegInfoInInterVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description：  预约挂号（医生站）
 * @Author：liudelin
 * @CreateDate：2015-6-5 下午05:12:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
/**
 * @author admin
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/business")
@Namespace(value = "/outpatient/businesspreregister")
public class BusinessPreregisterAction extends ActionSupport implements ModelDriven<RegisterPreregisterNow>{
	private Logger logger=Logger.getLogger(BusinessPreregisterAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Override
	public RegisterPreregisterNow getModel() {
		return preregister;
	}
	RegisterPreregisterNow preregister = new RegisterPreregisterNow();
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value = "businessPreregisterService")
	private BusinessPreregisterService businessPreregisterService;
	public void setBusinessPreregisterService(
			BusinessPreregisterService businessPreregisterService) {
		this.businessPreregisterService = businessPreregisterService;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private CodeInInterService codeInInterService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setCodeInInterService(CodeInInterService codeInInterService) {
		this.codeInInterService = codeInInterService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Resource
	private RedisUtil redis;
	
	/**  
	 * @Description：  预约挂号（医生站）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "listPreregister", results = { @Result(name = "list", location = "/WEB-INF/pages/business/preregister/preregisterList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPreregister() {
		try {
			String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			String dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
//			SysEmployee sysEmployee = businessPreregisterService.queryInfoPeret(userId);
			SysEmployee sysEmployee = employeeInInterService.getEmpByJobNo(userId);//从接口中获取
			RegisterGrade registerGrade = businessPreregisterService.findGradeEdit(sysEmployee.getTitle());
			ServletActionContext.getRequest().setAttribute("empId", sysEmployee.getJobNo());
			if(sysEmployee.getDeptId()!=null){
				ServletActionContext.getRequest().setAttribute("deptId",dept);
			}
			ServletActionContext.getRequest().setAttribute("gradeId", registerGrade.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
		return "list";
	}
	/**  
	 *  
	 * @Description： 数据源
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryInfoPeret", results = { @Result(name = "json", type = "json") })
	public void queryInfoPeret(){
		try {
			//查询排班信息（剩余号数）
			List<RegInfoInInterVo> infoVoList = businessPreregisterService.findInfoList(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			String middayTime = null;
			String endTime=null;
			CalendarVO calendar=null;
			List<CalendarVO> list=new ArrayList<CalendarVO>();
			for (RegInfoInInterVo vo : infoVoList){
				Integer tempID=0;
				String str= DateUtils.formatDateY_M_D(vo.getDates());
				String field=vo.getDeptId()+"-"+vo.getEmpId()+"-"+vo.getMidday();
				try {
					Boolean flag = redis.hexists(str, field);
					if(flag){
						tempID = redis.hincr(str, field, 0L).intValue();//剩余号源
					}else{
						tempID=vo.getLimit()-vo.getInfoAlready();
						int i = DateUtils.compareDate(vo.getDates(), new Date()); 
						if(i==1){//预约时间大于今天
							Long hincr = redis.hincr(str, field, tempID.longValue());
							if(tempID.longValue()!=hincr){//避免同时操作
								redis.hincr(str, field, -tempID.longValue());
							}
						}
					}
				} catch (Exception e) {
					tempID=vo.getLimit()-vo.getInfoAlready();
					e.printStackTrace();
					logger.error("MZYSZ_YYGHY", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "1"), e);
				}
				String type="";
				List<HospitalParameter> middyParameter = parameterInnerService.getMiddyParameter(HisParameters.WORKTIME);
				Map<String,String> mpMap = new HashMap<String,String>();
				for(HospitalParameter hp : middyParameter){
					String middyNum = hp.getParameterValue();
					String parameterTime = hp.getParameterDownlimit()+","+hp.getParameterUplimit();
					
					switch (middyNum) {
					case "1":
						mpMap.put("1", parameterTime);
						break;
					case "2":
						mpMap.put("2", parameterTime);
						break;
					case "3":
						mpMap.put("3", parameterTime);
						break;
					default:
						break;
					}
				}
				if(vo.getMidday()==1){
					middayTime = mpMap.get("1").split(",")[0];//
					endTime=mpMap.get("1").split(",")[1];
					type="上午   am";
				}else if(vo.getMidday()==2){
					middayTime = mpMap.get("2").split(",")[0];
					endTime=mpMap.get("2").split(",")[1];
					type="下午   pm";
				}else{
					middayTime = mpMap.get("3").split(",")[0];
					endTime=mpMap.get("3").split(",")[1];
					type="晚上   em";
				}
				calendar=new CalendarVO();
				calendar.setAllDay(false);
				calendar.setId(tempID+"|"+str);
				calendar.setTitle(type+" 预约限额"+vo.getLimit()+"    挂号总数"+vo.getInfoAlready()+"    剩余号数"+tempID);
				calendar.setEditable(false);
				calendar.setStart(str+" "+middayTime);
				calendar.setEnd(str+" "+endTime);
				list.add(calendar);
			}
			String mapJosn = JSONUtils.toJson(list,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  预约挂号（医生站）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "preregisterEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/business/preregister/preregisterEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String preregisterEdit() {
		
		try {
			String date = ServletActionContext.getRequest().getParameter("date");
			String empId = ServletActionContext.getRequest().getParameter("empId");
			String deptId = ServletActionContext.getRequest().getParameter("deptId");
			String gradeId = ServletActionContext.getRequest().getParameter("gradeId");
			String midday = ServletActionContext.getRequest().getParameter("midday");
			String preregisterStarttime = ServletActionContext.getRequest().getParameter("preregisterStarttime");
			String preregisterEndtime = ServletActionContext.getRequest().getParameter("preregisterEndtime");
			String preregisterMiddayname = ServletActionContext.getRequest().getParameter("preregisterMiddayname");
			try {
				preregisterMiddayname=URLDecoder.decode(preregisterMiddayname,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error("MZYSZ_YYGHY", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
			}
			ServletActionContext.getRequest().setAttribute("date", date);
			ServletActionContext.getRequest().setAttribute("empId", empId);
			ServletActionContext.getRequest().setAttribute("deptId", deptId);
			ServletActionContext.getRequest().setAttribute("gradeId", gradeId);
			ServletActionContext.getRequest().setAttribute("midday", midday);
			ServletActionContext.getRequest().setAttribute("preregisterStarttime", preregisterStarttime);
			ServletActionContext.getRequest().setAttribute("preregisterEndtime", preregisterEndtime);
			ServletActionContext.getRequest().setAttribute("preregisterMiddayname", preregisterMiddayname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 * @Description：  预约挂号（医生站）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "searchIdcard")
	public void searchIdcard(){
		try {
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			IdCardPreVo idCardPreVo = businessPreregisterService.searchIdcard(idcardNo);
			String mapJosn = JSONUtils.toJson(idCardPreVo,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：添加&修改
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "editPreregisterVo")
	public void editPreregisterVo()  {
		Map<String,String> map=new HashMap<>();
		String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
		InfoInInterPatient infoPatient = businessPreregisterService.queryRegisterInfo(idcardNo);
		Boolean flag=false;
		String key = DateUtils.formatDateY_M_D(preregister.getPreregisterDate());
		String field=preregister.getPreregisterDept()+"-"+preregister.getPreregisterExpert()+"-"+preregister.getMidday();
		try {
			if(StringUtils.isBlank(infoPatient.getIdCardNo())){//新增操作
				flag = redis.hexists(key, field);
				if(flag){
					Long hincr = redis.hincr(key, field, -1L);
					if(hincr<0){//预约号源已满
						map.put("resCode", "error");
						map.put("resMsg", "该医生的预约号源已满!");
						String json = JSONUtils.toJson(map);
						WebUtils.webSendJSON(json);
						return;
					}
				}
			}
		} catch (Exception e) {
			if(flag){
				redis.hincr(key, field, 1L);
			}
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "1"), e);
		}
		preregister.setIdcardId(infoPatient.getIdCardNo());
		try {
			businessPreregisterService.editPreregisterVo(preregister);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功!");
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
			if(flag){
				redis.hincr(key, field, 1L);
				map.put("resCode", "error");
				map.put("resMsg", "发生异常,请联系管理员!");
				String json = JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
				return;
			}
		}
		
	}
	/**  
	 * @Description：  通过预约午别带入开始结束时间
	 * @Author：wujiao
	 * @CreateDate：2016-1-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getTimeBymiddy")
	public void getTimeBymiddy(){
		try {
			String time = ServletActionContext.getRequest().getParameter("preregisterDate");
			String middy = ServletActionContext.getRequest().getParameter("middy");
			String gradeid = ServletActionContext.getRequest().getParameter("gradeid");
			String deptid = ServletActionContext.getRequest().getParameter("deptid");
			Map<String,String> timeMap = new HashMap<String, String>();
			timeMap = businessPreregisterService.queryAll(time,gradeid,deptid,middy);
			String mapJosn = JSONUtils.toJson(timeMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  验证该午别下的该医生是否已经有这患者的预约
	 * @Author：ldl
	 * @CreateDate：2016-06-06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPreInfo")
	public void queryPreInfo(){
		try {
			String dates = ServletActionContext.getRequest().getParameter("dates");
			String idCardno = ServletActionContext.getRequest().getParameter("idCardno");
			String midday = ServletActionContext.getRequest().getParameter("midday");
			String empId = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			Map<String,String> map = businessPreregisterService.queryPreInfo(dates,idCardno,empId,midday);
			String mapJosn = JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
	}
	/**  
	 * @Description： 证件类别下拉
	 * @Author：ldl
	 * @CreateDate：2016-08-16
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "likeCertificate")
	public void likeCertificate(){
		try {
			List<BusinessDictionary> codeRegistertypeList = codeInInterService.getDictionary("certificate");
			String mapJosn = JSONUtils.toJson(codeRegistertypeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
	}
	/**  
	 * @Description： 挂号医生挂号级别
	 * @Author：hzr
	 * @CreateDate：2016-12-19
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "searchempId")
	public void searchempId(){
		try {
			String empId = ServletActionContext.getRequest().getParameter("empId");
			EmpScheduleVo emp = businessPreregisterService.getEmpee(empId);
			String mapJosn = JSONUtils.toJson(emp);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZYSZ_YYGHY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSZ_YYGHY", "门诊医生站_预约挂号(医)", "2", "0"), e);
		}
	}
	
	
}