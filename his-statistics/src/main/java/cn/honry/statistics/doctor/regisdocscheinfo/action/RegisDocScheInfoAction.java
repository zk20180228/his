package cn.honry.statistics.doctor.regisdocscheinfo.action;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.json.JSONObject;

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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.doctor.regisdocscheinfo.service.RegisDocScheInfoService;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVoToReport;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**
 * 挂号医生信息排班查询 ACTION
 * @author tangfeishuai
 *@CreateDate 2016年6月22日 上午9:47:41 
 *@version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/regisdocscheinfo")
public class RegisDocScheInfoAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	private String menuAlias;
	private Logger logger=Logger.getLogger(RegisDocScheInfoAction.class);
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public String getMenuAlias() {
		return menuAlias;
	}
	
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	/**
	 * 注入手术费用详情Service
	 */
	@Autowired 
	@Qualifier(value = "regisDocScheInfoService")
	private RegisDocScheInfoService regisDocScheInfoService;
	
	@Autowired 
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	
	/**
	 * 医生姓名
	 */
	private String doctorName;
	
	/**
	 * 科室
	 */
	private String deptName;
	
	/**
	 * 开始时间
	 */
	private String begin;
	
	/**
	 * 结束时间
	 */
	private String end;

	/**
	 * 当前用户的职务
	 */
	private String post;
	
	/**
	 * 当前用户的jobno
	 */
	private String nowjobno;
	
	/**
	 * 用于判断查询的模式
	 */
	private String selectStatus;
	
	/**
	 * 当前用户的科室
	 */
	private String nowDept;
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setRegisDocScheInfoService(RegisDocScheInfoService regisDocScheInfoService) {
		this.regisDocScheInfoService = regisDocScheInfoService;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getNowjobno() {
		return nowjobno;
	}

	public void setNowjobno(String nowjobno) {
		this.nowjobno = nowjobno;
	}

	public String getSelectStatus() {
		return selectStatus;
	}

	public void setSelectStatus(String selectStatus) {
		this.selectStatus = selectStatus;
	}

	public String getNowDept() {
		return nowDept;
	}

	public void setNowDept(String nowDept) {
		this.nowDept = nowDept;
	}

	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHYSPBXXCX:function:view"}) 
	@Action(value = "listRegisDocScheInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/register/regisdocscheinfo/regisDocScheInfoList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisDocScheInfo() {
		
		end=DateUtils.getDate();
		begin=new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addMonth(DateUtils.getCurrentTime(), -1));

		return "list";
		
	}
	
	@Action(value = "checkZW", results = { @Result(name = "json", type = "json") })
	public void checkZW(){
		try {
			selectStatus = "0";
			String zw = parameterInnerService.getparameter("qygjzwcxtjsj");
			if("1".equals(zw)){
				post = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getPost();
				if(post==null){
					selectStatus = "1";
					nowjobno = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
					nowDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
				}else{
					if(!"1".equals(post) && !"2".equals(post) && !"27".equals(post) && !"28".equals(post)){
						nowjobno = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
						nowDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
						selectStatus = "1";
					}else{
						if("27".equals(post)||"28".equals(post)){
							nowjobno = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
							nowDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
							selectStatus = "2";
						}
					}
				   }
			}
			
			JSONObject jsonO = new JSONObject();
			jsonO.accumulate("selectStatus", selectStatus);
			jsonO.accumulate("nowJobno", nowjobno);
			jsonO.accumulate("nowDept", nowDept);
			String json = jsonO.toString();
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			e.printStackTrace(); 
			logger.error("MZCX_GHYSPBXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}
	}
	
	/**
	 *
	 * @Description：挂号医生排班信息查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryRegisDocScheInfo")
	public void queryRegisDocScheInfo(){
		
		try {
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<RegisDocScheInfoVo> list = regisDocScheInfoService.getReRegisDocVoList(deptName,doctorName, page, rows,begin,end,menuAlias);
			String redKey = "GHYSPBXXCX"+begin+"_"+end+"_"+deptName+"_"+doctorName;
			redKey=redKey.replaceAll(",", "-");
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
			 	totalNum = regisDocScheInfoService.getTotal(deptName,doctorName,begin,end,menuAlias);
				redisUtil.set(redKey, totalNum);
			}
			String val=parameterInnerService.getparameter("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			retMap.put("total", totalNum);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error("MZCX_GHYSPBXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}
	}
	
	/**
	 * @Description 获取职称
	 * @author  marongbin
	 * @createDate： 2017年1月10日 下午3:17:07 
	 * @modifier 
	 * @modifyDate：: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryTitle")
	public void queryTitle(){
		try {
			List<BusinessDictionary> list = innerCodeService.getDictionary("title");
			Map<String,String> map = new HashMap<String, String>();
			for (BusinessDictionary bd : list) {
				map.put(bd.getEncode(), bd.getName());
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error("MZCX_GHYSPBXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}
	}
	
	/**
	 *
	 * @Description：获取星期编码
	 * @Author：zhangjin
	 * @CreateDate：2016年12月1日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "getDictionary")
	public void getDictionary() {
		
		try {
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<BusinessDictionary> list = innerCodeService.getDictionary("week");
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					if("星期七".equals(list.get(i).getName())){
						list.get(i).setName("星期日");
					}
					retMap.put(list.get(i).getEncode(), list.get(i).getName());
				}
			}
			
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error("MZCX_GHYSPBXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}
	}
	
	/**
	 *
	 * @Description：获取午别
	 * @Author：zhangjin
	 * @CreateDate：2016年12月1日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "getDictionaryTionsex")
	public void getDictionaryTionsex() {
		try {
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<BusinessDictionary> list = innerCodeService.getDictionary("midday");
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					retMap.put(list.get(i).getEncode(), list.get(i).getName());
				}
			}
			
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error("MZCX_GHYSPBXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：挂号医生排班信息查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 * 导出
	 */
	@Action(value = "outRegisDocScheInfo")
	public void outRegisDocScheInfo() {
		
		PrintWriter out=null;
		try {
				
			List<RegisDocScheInfoVo> list = regisDocScheInfoService.regisDocVoList(deptName,doctorName,begin, end,menuAlias);
			List<BusinessDictionary> listMap = innerCodeService.getDictionary("title");
			Map<String,String> map = new HashMap<String, String>();
			for (BusinessDictionary bd : listMap) {
				map.put(bd.getEncode(), bd.getName());
			}
			for (RegisDocScheInfoVo reg : list) {
				reg.setReglevlName(map.get(reg.getReglevlName()));
			}
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head = "";
	
			String name = "挂号医生信息查询";
			String[] headMessage = { "科室", "医生", "职称", "星期","午别", "专长","日期", "拼音码" };
	
			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			
			out = WebUtils.getResponse().getWriter();
			
			fUtil = regisDocScheInfoService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
			
		} catch (Exception e) {
			out.write("error");
			
			e.printStackTrace(); 
			logger.error("MZCX_GHYSPBXXCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	

	/**
	 * 
	 * <p>挂号医生排班工作量统计</p>
	 * @Author: tangfeishuai
	 * @CreateDate: 2017年7月4日 下午4:07:47 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午4:07:47 
	 * @ModifyRmk:  修改注释模板，异常处理
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="iReportResDocWork")
	public void iReportResDocWork(){
		 try{
			  
			   //jasper文件名称 不含后缀
			   String fileName = request.getParameter("fileName");
			   String doctorName2 = request.getParameter("doctorName2");
			   String deptName2 = request.getParameter("deptName2");
			   deptName = request.getParameter("deptName");
			   doctorName = request.getParameter("doctorName");
			   begin= request.getParameter("begin");
			   end= request.getParameter("end");
			   menuAlias=request.getParameter("menuAlias");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<RegisDocScheInfoVo> list =regisDocScheInfoService.regisDocVoList(deptName,doctorName,begin,end,menuAlias);
			
			   //对科室、星期、午别进行渲染
			   Map<String,String> retMap1 = new HashMap<String,String>();
			   Map<String,String> retMap2= new HashMap<String,String>();
			   Map<String,String> retMap3 = new HashMap<String,String>();
			   List<BusinessDictionary> list1 = innerCodeService.getDictionary("week");
			   List<BusinessDictionary> list2 = innerCodeService.getDictionary("title");
			   List<BusinessDictionary> list3 = innerCodeService.getDictionary("midday");
			   if(list1!=null&&list1.size()>0){
					for(int i=0;i<list1.size();i++){
						if("星期七".equals(list1.get(i).getName())){
							list1.get(i).setName("星期日");
						}
						retMap1.put(list1.get(i).getEncode(), list1.get(i).getName());
					}
			   }
			   if(list2!=null&&list2.size()>0){
					for(int i=0;i<list2.size();i++){
						retMap2.put(list2.get(i).getEncode(), list2.get(i).getName());
					}
			   }
			   if(list3!=null&&list3.size()>0){
					for(int i=0;i<list3.size();i++){
						retMap3.put(list3.get(i).getEncode(), list3.get(i).getName());
					}
			   }
			   for (RegisDocScheInfoVo regisDocScheInfoVo : list) {
				   regisDocScheInfoVo.setWeekdayStr(retMap1.get(regisDocScheInfoVo.getWeekday().toString()));
			   }
			   for (RegisDocScheInfoVo regisDocScheInfoVo : list) {
				   regisDocScheInfoVo.setReglevlName(retMap2.get(regisDocScheInfoVo.getReglevlName()));
			   }
			   for (RegisDocScheInfoVo regisDocScheInfoVo : list) {
				   regisDocScheInfoVo.setNoonNameStr(retMap3.get(regisDocScheInfoVo.getNoonName().toString()));
			   }
			  
			   //javaBean数据封装
			   ArrayList<RegisDocScheInfoVoToReport> voList = new ArrayList<RegisDocScheInfoVoToReport>();
			   RegisDocScheInfoVoToReport vo = new RegisDocScheInfoVoToReport();
			   vo.setList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("hName", HisParameters.PREFIXFILENAME);
			   parameters.put("deptName", deptName2);
			   parameters.put("doctorName", doctorName2);
			   parameters.put("begin", begin);
			   
			   //对结束时间-1
			   String midTime= DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(end), -1));	
			   parameters.put("end", midTime);
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch (Exception e) {
			
			  //打印时可能会出现io异常（快速的按打印），但是异常不影响结果，不知道为什么，我就把他记录在日志文件里面了
			  logger.error("MZCX_GHYSPBXXCX", e);
			
			  //将日志信息存入mongodb中
			  hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_GHYSPBXXCX", "门诊查询_挂号医生排班信息查询", "2", "0"), e);
		}
		 
	}
}
