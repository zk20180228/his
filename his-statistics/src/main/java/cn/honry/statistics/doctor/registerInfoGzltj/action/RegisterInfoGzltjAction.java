package cn.honry.statistics.doctor.registerInfoGzltj.action;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.statistics.bi.bistac.imStacData.service.ImStacDataService;
import cn.honry.statistics.doctor.registerInfoGzltj.service.RegisterInfoGzltjService;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.DoctorVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.PieNameAndValue;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description：  门诊工作量统计
 * @Author：zpty
 * @CreateDate：2015-8-26 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/RegisterInfoGzltj")
public class RegisterInfoGzltjAction extends ActionSupport implements ModelDriven<RegisterInfoGzltjVo>{

	private static final long serialVersionUID = 1L;
	public String sTime;//开始时间
	public String eTime;//结束时间
	public String dept;//部门的编号
	public String expxrt;//医生的编号
	private String page;//当前页
	private String rows;//每页显示多少行
	private String dateSign;
	private String date;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateSign() {
		return dateSign;
	}
	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}
	//new挂号信息表统计用的实体类
	private RegisterInfoGzltjVo registerInfoGzltjVo =  new RegisterInfoGzltjVo();
	@Override
	public RegisterInfoGzltjVo getModel() {
		return registerInfoGzltjVo;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "registerInfoGzltjService")
	private RegisterInfoGzltjService registerInfoGzltjService;
	@Autowired
	@Qualifier(value = "imStacDataService")
	private ImStacDataService imStacDataService;
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(RegisterInfoGzltjAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;

	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
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
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	
	/**  
	 *  
	 * @Description： 挂号医生工作量统计
	 * @param 
	 * @Author：zpty
	 * @CreateDate：2015-8-26
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "listRgisterInfoGzltj", results = { @Result(name = "list", location = "/WEB-INF/pages/register/registerInfoGzltj/registerInfoGzltj.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRgisterInfoGzltj()  {
		
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateY_M(date)+"-01";
		
		return "list";
	}
	
	/**  
	 *  
	 * @Description： 挂号医生工作量统计
	 * @param 
	 * @Author：zpty
	 * @CreateDate：2015-8-27  
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk: 加入分区,原调用方法findInfo先调用方法statRegDorWorkload
	 * @version 1.0
	 *
	 */
	@Action(value = "queryRegisterInfoGzltj", results = { @Result(name = "json", type = "json") })
	public void queryRegisterInfoGzltj() {
		Map<String,Object> map =null;
		try {
			if(StringUtils.isBlank(sTime)){
				Date date = new Date();
				sTime = DateUtils.formatDateY_M(date)+"-01";
			}
			if(StringUtils.isBlank(eTime)){
				Date date = new Date();
				eTime = DateUtils.formatDateY_M_D(date);
			}
			map = registerInfoGzltjService.statRegDorWorkByES(sTime,eTime,registerInfoGzltjVo.getDept(),registerInfoGzltjVo.getExpxrt(),page,rows,menuAlias);
			String json = JSONUtils.toJson(map);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			//发生异常时返回空的内容
			map= new HashMap<String, Object>();
			map.put("total", new ArrayList<RegisterInfoGzltjVo>());
			map.put("rows", 0);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_GHYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_GHYSGZLTJ", "门诊统计分析_挂号医生工作量统计", "2","0"), e);
		}
	}
	
	/**  
	* @Title: registerDeptInfo 
	* @Description: 挂号科室工作量统计
	* @return String    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月20日
	*/
	@Action(value = "registerDeptInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/register/registerDeptInfo/registerDeptInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String registerDeptInfo()  {
		
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateY_M(date)+"-01";
		
		return "list";
	}
	/** 
	* @Title: queryRegisterDeptInfo 
	* @Description: 挂号科室工作量统计查询
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月24日
	* @version 1.0
	*/
	@Action(value="queryRegisterDeptInfo")
	public void queryRegisterDeptInfo(){
		Map<String, Object> map=null;
		try {
			dept = ServletActionContext.getRequest().getParameter("dept");
			if (StringUtils.isBlank(dept) || "all".equals(dept)) {
				List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,
								ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
				for (int i = 0; i < deptList.size(); i++) {
					dept += deptList.get(i).getDeptCode() + ",";
				}
				if (dept.endsWith(",")) {
					dept = dept.substring(0, dept.lastIndexOf(","));
				}
			}
			map = registerInfoGzltjService.getRegisterDeptInfo(sTime,eTime, dept, page, rows);
			WebUtils.webSendJSON(JSONUtils.toJson(map));
			
		} catch (Exception e) {
			
			//发生异常时返回空的内容
			map= new HashMap<String, Object>();
			map.put("total", new ArrayList<RegisterInfoVo>());
			map.put("rows", 0);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_GHKSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_GHKSGZLTJ", "门诊统计分析_挂号科室工作量统计", "2","0"), e);
		}
	}
	
	/**
	 * @Description 根据科室code集合获取医生
	 * @author  marongbin
	 * @createDate： 2017年2月14日 上午9:30:06 
	 * @modifier 
	 * @modifyDate：
	 * @param deptCodes
	 * @return: void 无返回值
	 * @version 1.0
	 */
	@Action(value = "getDoctorBydeptCodes", results = { @Result(name = "json", type = "json") })
	public void getDoctorBydeptCodes(){
		try {
			String deptCodes = ServletActionContext.getRequest().getParameter("deptCodes");
			List<DoctorVo> list = registerInfoGzltjService.getDoctorBydeptCodes(deptCodes);
			String json = JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			List<DoctorVo> list = new ArrayList<DoctorVo>();
			String json = JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_GHYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_GHYSGZLTJ", "门诊统计分析_挂号医生工作量统计", "2","0"), e);
		}
	}

	/**
	 * @Description 查询所有医生
	 * @author  gaotiantian
	 * @createDate： 2017-4-20 上午9:29:24 
	 * @modifyDate：
	 * @return: 无返回值
	 * @version 1.0
	 */
	@Action(value = "getDoctorList")
	public void getDoctorList(){
		try {
			String deptTypes = ServletActionContext.getRequest().getParameter("deptTypes");
			List<cn.honry.inner.vo.MenuListVO> list = registerInfoGzltjService.getDoctorList(deptTypes,menuAlias);
			String json = JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			List<DoctorVo> list = new ArrayList<DoctorVo>();
			String json = JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
			
			e.printStackTrace();
			logger.error("TJFXGL_GHYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_GHYSGZLTJ", "门诊统计分析_挂号医生工作量统计", "2","0"), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	 * <p>PC端挂号量工作统计对应移动端挂号量统计</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月26日 下午7:32:37 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月26日 下午7:32:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@Action(value = "rgisterAmountTotal", results = { @Result(name = "list", location = "/WEB-INF/pages/register/registerAmount/registerAmount.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String rgisterAmountTotal()  {
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateY_M(date)+"-01";
		return "list";
	}
	/**
	 * 
	 * 
	 * <p>门诊挂号量统计与移动端挂号量对应</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月26日 下午7:49:02 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月26日 下午7:49:02 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryRegisterCharts", results = { @Result(name = "json", type = "json") })
	public void queryOutpatientCharts() {
		String json ="";
		try {
			if("1".equals(dateSign)||"2".equals(dateSign)||"3".equals(dateSign)||"4".equals(dateSign)){
				json=registerInfoGzltjService.queryRegisterCharts(date, dateSign);
				if (StringUtils.isBlank(json)) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("fee", new ArrayList<PieNameAndValue>());
					map.put("area", new ArrayList<PieNameAndValue>());
					map.put("Huanbi", new ArrayList<PieNameAndValue>());
					map.put("TongbiByDay", new ArrayList<PieNameAndValue>());
					json = JSONUtils.toJson(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TJFXGL_SRTJB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_SRTJB", "住院统计分析_住院收入统计", "2","0"), e);
		}
		WebUtils.webSendJSON(json);
	}
	
}
