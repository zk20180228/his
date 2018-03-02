package cn.honry.outpatient.preregister.action;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.outpatient.grade.service.GradeInInterService;
import cn.honry.outpatient.preregister.service.PreregisterService;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.outpatient.schedule.service.ScheduleService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**   
*  预约挂号
* @className：
* @description：  PreregisterAction
* @author：wujiao
* @createDate：2015-6-8 上午09:50:36  
* @modifier：姓名
* @modifyDate：2015-6-8 上午09:50:36  
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/preregister")
public class PreregisterAction extends ActionSupport implements ModelDriven<RegisterPreregisterNow>{
	
	private Logger logger=Logger.getLogger(PreregisterAction.class);
	
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private RedisUtil redisUtil;
	@Autowired
	@Qualifier(value="redisUtil")
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

	private PreregisterService preregisterService;
	@Autowired
	@Qualifier(value = "preregisterService")
	public void setPreregisterService(PreregisterService preregisterService) {
		this.preregisterService = preregisterService;
	}

	@Autowired
	@Qualifier(value = "scheduleService")
	private ScheduleService scheduleService;
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}
	private List<RegisterPreregisterNow> registerPreregisterList;
	private RegisterPreregisterNow registerPreregister=new RegisterPreregisterNow() ;
	// 分页
	private String page;//起始页数
	private String rows;//数据列数
	private String menuAlias;
	//combobox的mode属性
	private String q;
	private String time;
	
	/*
	 * 挂号专家科室
	 * */
	private String deptId;
	
	/*
	 * 挂号专家级别
	 * */
	private String grade;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	private GradeInInterService gradeInInterService;
	@Autowired
	@Qualifier(value = "gradeInInterService")
	
	@Override
	public RegisterPreregisterNow getModel() {
		return registerPreregister;
	}
	public void setGradeInInterService(GradeInInterService gradeInInterService) {
		this.gradeInInterService = gradeInInterService;
	}

	private HttpServletRequest request = ServletActionContext.getRequest();

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
	/**  
	 *  
	 * @Description：  预约挂号信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YYGH:function:view"}) 
	@Action(value = "listPreregister", results = { @Result(name = "list", location = "/WEB-INF/pages/register/preregister/preregisterList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPreregister() {
		try {
			String preregisterparameter =  preregisterService.getParameterByCode("reserveTime");
			request.setAttribute("preregisterparameter", preregisterparameter);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  预约挂号信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YYGH:function:query"}) 
	@Action(value = "queryPreregister", results = { @Result(name = "json", type = "json") })
	public void queryPreregister() {
		try {
			//User user = WebUtils.getSessionUser();
			String pNo=ServletActionContext.getRequest().getParameter("pNo");
			String pExpert=ServletActionContext.getRequest().getParameter("pExpert");
			String pGrade=ServletActionContext.getRequest().getParameter("pGrade");
			String pDept=ServletActionContext.getRequest().getParameter("pDept");
			String takeNum=ServletActionContext.getRequest().getParameter("takeNum");
			if(StringUtils.isNotBlank(pNo)){
				registerPreregister.setPreregisterNo(pNo.trim());
			}
			if(StringUtils.isNotBlank(pExpert)){
				registerPreregister.setPreregisterExpert(pExpert.trim());
				//employee.setId(pExpert);
				//registerPreregister.setPreregisterExpert(employee.getJobNo());
			}
			if(StringUtils.isNotBlank(pDept)){
				registerPreregister.setPreregisterDept(pDept.trim());
				//department.setId(pDept);
				//registerPreregister.setPreregisterDept(department.getId());
			}
			if(StringUtils.isNotBlank(pGrade)){
				registerPreregister.setPreregisterGrade(pGrade.trim());
				//grade.setId(pGrade);
				//registerPreregister.setPreregisterGrade(grade.getId());
			}
			if(StringUtils.isNotBlank(takeNum)){
				if("1".equals(takeNum)){
					registerPreregister.setStatus(3);
				}else{
					registerPreregister.setStatus(1);//一代表除掉状态为3的
				}
				
			}else{
				registerPreregister.setStatus(1);//一代表除掉状态为3的
			}
			registerPreregisterList = preregisterService.getPage(request.getParameter("page"),request.getParameter("rows"),registerPreregister);
			int total = preregisterService.getTotal(registerPreregister);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", registerPreregisterList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：添加&修改
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "editInfoPreregister")
	public void editInfoPreregister(){
		try {
			Map<String,String> map = new HashMap<>();  
			if(StringUtils.isBlank(registerPreregister.getId())){//保存操作
				String key=DateUtils.formatDateY_M_D(registerPreregister.getPreregisterDate());//日期作为key
				String field=registerPreregister.getPreregisterDept()+"-"+registerPreregister.getPreregisterExpert()
						+"-"+registerPreregister.getMidday();//科室+医生+午别为field
				Long hincr=-1L;
				try {
					hincr = redisUtil.hincr(key, field, -1L);
				} catch (Exception e1) {
					e1.printStackTrace();
					logger.error("GHGL_YYGH", e1);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "1"), e1);
				}
				if(hincr>=0){//已抢到预约号
					try {
						preregisterService.saveOrUpdatePreregister(registerPreregister);
						map.put("resMsg", "success");
						map.put("resCode", "预约成功！");
					} catch (Exception e) {
						try {
							redisUtil.hincr(key, field, 1L);//保存失败时,恢复原来的号源数量
						} catch (Exception e1) {
							e1.printStackTrace();
							logger.error("GHGL_YYGH", e1);
							hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "1"), e1);
						}
						map.put("resMsg", "error");
						map.put("resCode", "预约失败！");
						e.printStackTrace();
						logger.error("GHGL_YYGH", e);
						hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
					}
				}else{
					map.put("resMsg", "error");
					map.put("resCode", "该医生号源已挂满,请选择其他医生！");
				}
			}else{//修改操作
				preregisterService.saveOrUpdatePreregister(registerPreregister);
				map.put("resMsg", "success");
				map.put("resCode", "预约成功！");
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：删除
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YYGH:function:delete"}) 
	@Action(value = "delPreregister", results = { @Result(name = "list", location = "/WEB-INF/pages/register/preregister/preregisterList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delPreregister() {
		try {
			preregisterService.del(registerPreregister.getId());	
			WebUtils.webSendString("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
		return "list";
	}


	
	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "deptComboBox")
	public void deptComboBox() {
		try {
			List<SysDepartment> departmentList = deptInInterService.queryAllDept();//直接从接口中取
//			List<SysDepartment> departmentList = preregisterService.deptCombobox(q);
			String mapJosn = JSONUtils.toJson(departmentList,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  查询专家信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-3 下午05:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-3 下午05:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryScheduleZJ")
	public void queryScheduleZJ() {
		try {
			String str=ServletActionContext.getRequest().getParameter("str");
			String time=ServletActionContext.getRequest().getParameter("time");
			RegisterScheduleNow schedule = scheduleService.getzj(str,time);
			String mapJosn = JSONUtils.toJson(schedule,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	
	  /**   
	*  
	* @description：挂号级别下拉框通过预约时间
	* @author：wujiao
	* @createDate：2015-6-8 上午09:50:36  
	* @modifier：姓名
	* @modifyDate：2015-6-8 上午09:50:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Action(value = "gradeCom")
	public void gradeCom() {
	    try {
			List<RegisterGrade>	registerGradeList = gradeInInterService.getCombobox(time,q);
			String mapJosn = JSONUtils.toJson(registerGradeList,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	
	
	/**  
	 * @Description：  挂号科室（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "getDeptCom", results = { @Result(name = "json", type = "json") })
	public void getDeptCom() {
		try {
			String time = ServletActionContext.getRequest().getParameter("time");
			List<SysDepartment> departmentList = preregisterService.getDeptCom(time,q);
			String json = JSONUtils.toJson(departmentList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  挂号人员（下拉框）通过预约时间的
	 * @Author：wj
	 * @CreateDate：2015-11-11 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "getEmpCom", results = { @Result(name = "json", type = "json") })
	public void getEmpCom() {
		String time = ServletActionContext.getRequest().getParameter("time");
		String gradeid = ServletActionContext.getRequest().getParameter("gradeid");
		String deptid = ServletActionContext.getRequest().getParameter("deptid");
		String name=ServletActionContext.getRequest().getParameter("q");
		List<EmpScheduleVo> voList = new ArrayList<EmpScheduleVo>();
		List<EmpScheduleVo> list = preregisterService.getEmpCom(request.getParameter("page"),request.getParameter("rows"),time,gradeid,deptid,name);
		//int total = preregisterService.getTotalemp(time,gradeid,deptid,name);
		int total = list.size();
		int i = Integer.parseInt(page);//分页页码
 		int j = Integer.parseInt(rows);//每页显示的条数
 		int first=(i-1)*j;//起始位置
 		try {
			for (int k = 0; k < j; k++) {
				int m=first+k;
				if(m<list.size()){
					EmpScheduleVo vo = list.get(m);
					String key = vo.getPreDate();//预约日期
					String field=vo.getDeptId()+"-"+vo.getId()+"-"+vo.getMidday();
					Boolean hexists = redisUtil.hexists(key, field);//判断缓存中是否存在号源信息
					vo.setNetsy(vo.getNetL()-vo.getNetA());//网络预约剩余数量
					vo.setPhonesy(vo.getPhoneL()-vo.getPhoneA());//电话预约剩余数量
					//现场预约剩余数量=预约总量-已预约总量(即只要电话或网络有剩余的都可以进行现场预约)
					Integer num=vo.getPreL()-vo.getPreA();
					vo.setNowsy(num);
					if(hexists){//如果存在
						Long hincr = redisUtil.hincr(key, field, 0L);//剩余的预约号源数量
						int intValue = hincr.intValue()>=0?hincr.intValue():0;
						vo.setNowsy(intValue);
					}else{//不存在
						Long hincr = redisUtil.hincr(key, field, num.longValue());//将号源信息存到缓存中
						if(num.longValue()!=hincr){//避免同时操作
							redisUtil.hincr(key, field, -num.longValue());
						}
					}
					voList.add(list.get(m));
				}
			}
		} catch (Exception e) {
			voList=list;
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", voList);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * @Description：  挂号专家下拉框(查询)
	 * @Author：wj
	 * @CreateDate：2016-1-5 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "employeeComboboxQuery", results = { @Result(name = "json", type = "json") })
	public void employeeComboboxQuery() {
		try {
			String name=ServletActionContext.getRequest().getParameter("q");
			List<SysEmployee> departmentList = null;
			departmentList = preregisterService.getPageSys(request.getParameter("page"),request.getParameter("rows"),name,deptId,grade);	
			int total = preregisterService.getTotalSys(name,deptId,grade);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total",total);
			map.put("rows", departmentList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	/**   
	*  
	* @description：获得级别编码map id和name
	* @author：wj
	* @createDate：2015-11-23 上午10:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Action(value="likeTitleMap",results={@Result(name="json",type="json")})
	public void likeTitleMap(){
		try {
			List<BusinessDictionary> titleList = innerCodeService.getDictionary("title");
			Map<String,String> gradeMap = new HashMap<String, String>();
			if(titleList!=null){
				for(BusinessDictionary title : titleList){
					gradeMap.put(title.getEncode(), title.getName());
				}
			}
			//Map<String,String> drugpropertiesMap = innerCodeService.getBusDictionaryMap("title");
			String json=JSONUtils.toJson(gradeMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 下午4:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YYGH:function:query"}) 
	@Action(value = "queryPreregisterBymid", results = { @Result(name = "json", type = "json") })
	public void queryPreregisterBymid() {
		try {
			String reservation = preregisterService.queryPreregisterByMid(number,dept,date);
			String json = JSONUtils.toJson(reservation);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  根据就诊卡ID查询患者是否在患者黑名单中
	 * @Author：zpty
	 * @CreateDate：2016-5-26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryCardIdByBlack", results = { @Result(name = "json", type = "json") })
	public void queryCardIdByBlack() {
		try {
			int number =preregisterService.getPatientCount(idcardNo);
			String json = JSONUtils.toJson(number);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  渲染挂号级别
	 * @param:
	 * @Author：zhuxiaolu
	 * @ModifyDate：2015-11-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "likeTitle", results = { @Result(name = "json", type = "json") })
	public void likeTitle() {
		try {
			List<BusinessDictionary> certificateList = innerCodeService.getDictionary("title");
			String json=JSONUtils.toJson(certificateList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_YYGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_YYGH", "挂号管理_预约挂号", "2", "0"), e);
		}
	}
	
	/**
	 * 身份证
	 */
	private String number;
	/**
	 * 科室id
	 */
	private String dept;
	/**
	 * 预约时间
	 */
	private String date;
	/**
	 * 就诊卡ID
	 */
	private String idcardNo;
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	
}
