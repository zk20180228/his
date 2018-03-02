package cn.honry.statistics.bi.wdWin.action;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.BiBaseEmployee;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.bi.wdWin.service.WdWinService;
import cn.honry.statistics.bi.wdWin.vo.DayVO;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
/**
 * BI统计维度弹窗
 * @author hedong
 * @createDate：2016/8/11
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/wdWin")
public class WdWinAction  extends ActionSupport{
    private static final long serialVersionUID = 1L;
    private Logger logger=Logger.getLogger(WdWinAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private RedisUtil redisUtil;
	private String id;
	private String flag;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Autowired
	@Qualifier(value = "redisUtil")
	public void setRedisUtil(RedisUtil redisUtil) { 
		this.redisUtil = redisUtil;
	}
	
	@Autowired			
	@Qualifier(value = "wdWinService")
	private WdWinService wdWinService;
	public void setWdWinService(WdWinService wdWinService) {
		this.wdWinService = wdWinService;
	}
	@Autowired			
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**  
	 * @Description：  
	 * @Author：hedong
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"WDTC:function:view"}) 									
	@Action(value = "listWdDemo", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listWdDemo() {
		return "list";
	}
	/**
	 * 动态根据所选择月份获得月份天数
	 * @author hedong 
	 * @createDate：2016/8/11
	 * @version 1.0
	 */
	@Action(value = "ajaxTogetWdDay", results = { @Result(name = "json", type = "json") })
	public void ajaxTogetWdDay() {
		try {
			String selectMonth = request.getParameter("selectMonth");
			 List<DayVO> dayList = new ArrayList<DayVO>(); 
			 //当前年月默认天数
			 SimpleDateFormat oSdf = new SimpleDateFormat ("yyyy-MM"); 
			 Calendar cal = Calendar.getInstance();
		        try {      
		        	if(selectMonth.equalsIgnoreCase("curMonth")){
		        		cal.setTime(oSdf.parse(oSdf.format(new Date())));      //oSdf.parse("2016-02")
			        }else{
			        	cal.setTime(oSdf.parse(selectMonth));   
			        }
		            
		        } catch (ParseException e) {      
		            e.printStackTrace();      
		        }  
		        
		        int num2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);     
		        for(int i=1;i<=num2;i++){
		        	 String str ="";
		        	 if(i<=9){
		        		 str="0"+i;
		        	 }else{
		        		 str=i+"";
		        	 }
		        	 DayVO vo = new DayVO();
					 vo.setId(str);
					 vo.setName(str);
					 dayList.add(vo);
		        }
			    Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd").create();
				String json =gson.toJson(dayList);
				PrintWriter out = WebUtils.getResponse().getWriter();
				out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**  
	 * @Description：地域树
	 * @Author：hedong
	 * @CreateDate：2016-8-11 
	 * @remark：  可根据id获得指定科室类型的科室(动态加载) 若 provinceOnly=0 则只获取省级数据 否则则是动态加载省市...
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "treeBaseDistrict", results = { @Result(name = "json", type = "json") })
	public void treeBaseDistrict() {
		String provinceOnly = request.getParameter("provinceOnly");
		List<TreeJson> treeDepar =null;
		treeDepar = wdWinService.QueryTreeDistrict(id,provinceOnly);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		String json = gson.toJson(treeDepar);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
	}
	
	/**  
	 * @Description：  科室树
	 * @Author：hedong
	 * @CreateDate：2016-8-11 
	 * @remark：  可根据id获得指定科室类型的科室(动态加载) 
	 * @version 1.0
	 */
	@Action(value = "treeBaseOrg", results = { @Result(name = "json", type = "json") })
	public void treeBaseOrg() {
		String deptTypes = ServletActionContext.getRequest().getParameter("deptType");//需要显示科室树的条件
		List<TreeJson> treeDepar =null;
		treeDepar = wdWinService.QueryTreeOrg(id,deptTypes);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		String json = gson.toJson(treeDepar);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**  
	 * @Description：  门诊科室
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Action(value = "treeBaseOrgOut", results = { @Result(name = "json", type = "json") })
	public void treeBaseOrgOut() {
		 List<BiBaseOrganization> list = wdWinService.treeBaseOrgOut();
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		String json = gson.toJson(list);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**  
	 * @Description：  费用类别
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Action(value = "queryFeecodecom", results = { @Result(name = "json", type = "json") })
	public void queryFeecodecom() {
		 List<BusinessDictionary> list = innerCodeService.getDictionary("casminfee");
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		String json = gson.toJson(list);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**  
	 * @Description：  挂号级别
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22 
	 * @remark：  
	 * @version 1.0
	 */
	@Action(value = "queryregcode", results = { @Result(name = "json", type = "json") })
	public void queryregcode() {
		 List<RegisterGrade> list = wdWinService.queryregcode();
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		String json = gson.toJson(list);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * 用于维度弹窗页面科室数据的处理
	 * @author hedong 
	 * @createDate：2016/8/16
	 * @version 1.0
	 */
	@Action(value = "ajaxTogetDeptMap", results = { @Result(name = "json", type = "json") })
	public void ajaxTogetDeptMap() {
		try {
			   String deptTypes = ServletActionContext.getRequest().getParameter("deptType");//需要显示科室树的条件
			    //获得第一级节点用于存放key 如门诊code 急诊code
			    List<DayVO> voList = new ArrayList<DayVO>(); 
			    List<BiBaseOrganization> parentOrgList = wdWinService.findTreeOrg(deptTypes);
			    if(parentOrgList!=null&&parentOrgList.size()>0){
			    	 DayVO vo = null;
			    	 BiBaseOrganization org=null;
			    	 for(int i=0;i<parentOrgList.size();i++){
			    		 org = parentOrgList.get(i);
 			        	 vo = new DayVO();
						 vo.setId(org.getOrgCode());
						 //获得子节点信息
						 List<BiBaseOrganization> childOrgList = wdWinService.findTreeOrgByParentId(org.getOrgCode());
						 String tmpchildName="";
						 if(childOrgList!=null&&childOrgList.size()>0){
							 for(int x=0;x<childOrgList.size();x++){
								 BiBaseOrganization childOrg = childOrgList.get(x);
								 if(x!=childOrgList.size()-1){
									 tmpchildName=tmpchildName+childOrg.getOrgCode()+",";
								 }else{
									 tmpchildName=tmpchildName+childOrg.getOrgCode();
								 }
							 }
						 }
						 vo.setName(tmpchildName);
						 voList.add(vo);
			        }
			    }
			    Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd").create();
				String json =gson.toJson(voList);
				PrintWriter out = WebUtils.getResponse().getWriter();
				System.out.println("mapJson>>>"+json);
				out.write(json);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	
	@Action(value = "deptEmpTree")
	public void deptEmpTree() {
		String deptTypes = ServletActionContext.getRequest().getParameter("deptType");//需要显示科室树的条件
		List<TreeJson> treeDepar =null;
		treeDepar = wdWinService.queryDeptEmpTree(id,deptTypes);
		String json=JSONUtils.toJson(treeDepar);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 统计大类下拉
	 */
	@Action(value = "casminfeeCom", results = { @Result(name = "json", type = "json") })
	public void casminfeeCom() {
		List<BusinessDictionary> businessDictionaries = innerCodeService.getDictionary("casminfee");
		BusinessDictionary businessDictionary = new BusinessDictionary();
		businessDictionary.setEncode("00");
		businessDictionary.setName("全部");
		businessDictionaries.add(0, businessDictionary);
		String json = JSONUtils.toJson(businessDictionaries);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 获取 入院来源 维度 用于下拉框 
	 * by GH
	 */
	@Action(value = "inSourse", results = { @Result(name = "json", type = "json") })
	public void inSourse() {
		List<BusinessDictionary> businessDictionaries = innerCodeService.getDictionary("source");
		String json = JSONUtils.toJson(businessDictionaries);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 获取 入院来源 MAP
	 * by GH
	 */
	@Action(value = "inSourseMap", results = { @Result(name = "json", type = "json") })
	public void inSourseMap() {
		List<BusinessDictionary> inSourseList = innerCodeService.getDictionary("source");
		Map<String,String> map=new HashMap<String,String>();
		for(BusinessDictionary biorg:inSourseList){
			map.put(biorg.getEncode(), biorg.getName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 获取 患者状态 维度  用于下拉框 
	 * by GH
	 */
	@Action(value = "status", results = { @Result(name = "json", type = "json") })
	public void status() {
		List<BusinessDictionary> businessDictionaries = innerCodeService.getDictionary("status");
		String json = JSONUtils.toJson(businessDictionaries);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 获取 患者状态 MAP code、name
	 * by GH
	 */
	@Action(value = "statusMap", results = { @Result(name = "json", type = "json") })
	public void statusMap() {
		List<BusinessDictionary> list = innerCodeService.getDictionary("status");
		Map<String,String> map=new HashMap<String,String>();
		for(BusinessDictionary biorg:list){
			map.put(biorg.getEncode(), biorg.getName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询挂号级别下拉
	 * @author tcj
	 */
	@Action(value = "queryDocLevelForBiPublic")
	public void queryDocLevelForBiPublic() {
		List<RegisterGrade> birglist = wdWinService.queryDocLevelForBiPublic();
		String json = JSONUtils.toJson(birglist);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询挂号级别MAPcode、name
	 * @author tcj
	 */
	@Action(value="queryDoclevelForBiPublic")
	public void queryDoclevelForBiPublic(){
		List<RegisterGrade> biorglist=wdWinService.queryDocLevelForBiPublic();
		Map<String,String> map=new HashMap<String,String>();
		for(RegisterGrade biorg:biorglist){
			map.put(biorg.getCode(), biorg.getName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询医生MAPcode、name
	 * @author tcj
	 */
	@Action(value="queryDocForBiPublic")
	public void queryDocForBiPublic(){
		List<BiBaseEmployee> biorglist=wdWinService.queryDocForBiPublic();
		Map<String,String> map=new HashMap<String,String>();
		for(BiBaseEmployee biorg:biorglist){
			map.put(biorg.getEmployeeNo(), biorg.getEmployeeName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	//************************************************************维度弹出*********************************************************************************
	
	/**  
	 * @Description：  门急诊工作量维度弹窗
	 * @Author：hedong
	 * @CreateDate：2016-8-11 
	 * @version 1.0
	 */
	@Action(value = "WDWinToOutPatientWorkload", results = { @Result(name = "wdWinToOutPatientWorkload", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutPatientWorkload.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOutPatientWorkload() {
		return "wdWinToOutPatientWorkload";
	}
	
	/**  
	 * @Description： 门诊人次分析维度弹窗
	 * @Author：hedong
	 * @CreateDate：2016-8-11 
	 * @version 1.0
	 */
	@Action(value = "WDWinToOutPatientVisits", results = { @Result(name = "wdWinToOutPatientVisits", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutPatientVisits.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOutPatientVisits() {
		return "wdWinToOutPatientVisits";
	}
	/**  
	 * @Description： 住院费用分析维度弹窗
	 * @Author：donghe
	 * @CreateDate：2016-8-12 
	 * @version 1.0
	 */
	@Action(value = "WDWinToExpensesAnaly", results = { @Result(name = "WDWinToExpensesAnaly", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToExpensesAnaly.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToExpensesAnaly() {
		return "WDWinToExpensesAnaly";
	}
	/**  
	 * @Description： 门诊项目收费均次费用分析维度分析弹窗
	 * @Author：ldl
	 * @CreateDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "WDWinToProject", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutpatientProject.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToProject() {
		return "list";
	}
	/**  
	 * @Description： 挂号人次统计分析维度分析弹窗
	 * @Author：hanzurong
	 * @CreateDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "WDWinToRegister", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToRegisterlist.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToRegister() {
		return "list";
	}   
	/**  
	 * @Description： BI门诊结算费用统计分析弹窗
	 * @Author：ldl
	 * @CreateDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "WDWinToSettlement", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutpatientSettlement.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToSettlement() {
		return "list";
	}   
	/**  
	 * @Description： BI手术台次统计
	 * @Author：tangfeishuai
	 * @CreateDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "WDWinToOpNum", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutpatientOpNum.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOpNum() {
		return "list";
	}   
	/**  
	 * @Description： BI门诊收费类型分析
	 * @Author：tangfeishuai
	 * @CreateDate：2016-8-16
	 * @version 1.0
	 */
	@Action(value = "WDWinToOutFeeType", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutpatientFeeType.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOutFeeType() {
		return "list";
	}   
	/**  
	 * @Description： BI全院手术次数分析
	 * @Author：tangfeishuai
	 * @CreateDate：2016-8-16
	 * @version 1.0
	 */
	@Action(value = "WDWinToOperaFullStat", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOperationFullStat.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOperaFullStat() {
		return "list";
	}   
	
	/**  
	 * @Description：  BI门诊收费统计分析弹窗
	 * @Author：ldl
	 * @CreateDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "WDWinToTotCost", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutpatientTotCost.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToTotCost() {
		return "list";
	}   
	
	/**  
	 * @Description： 住院人次分析维度弹窗
	 * @Author：gengh
	 * @CreateDate：2016-8-12 
	 * @version 1.0
	 */
	@Action(value = "wdWinToInformation", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToHospitalizationInformation.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String wdWinToInformation() {
		return "list";
	}
	/**  
	 * @Description： 住院人次费用分析维度弹窗
	 * @Author：gengh
	 * @CreateDate：2016-8-12 
	 * @version 1.0
	 */
	@Action(value = "wdWinToExpenses", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToHospitalizationExpensesList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String wdWinToExpenses() {
		return "list";
	}   
	/**  
	 * @Description：在院人次统计分析维度分析弹窗
	 * @Author：hanzurong
	 * @CreateDate：2016-8-17
	 * @version 1.0
	 */
	@Action(value = "WDWinToPeopleInhospital", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToPeopleInHospital.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToPeopleInhospital() {
		return "list";
	}   
	/**  
	 * @Description：住院药品比例分析
	 * @Author：yeguanqun
	 * @CreateDate：2016-8-17
	 * @version 1.0
	 */
	@Action(value = "WDWinToInpatientDrugRatio", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToInpatientDrugRatio.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToInpatientDrugRatio() {
		return "list";
	} 
	/**  
	 * @Description：住院均次费用分析
	 * @Author：yeguanqun
	 * @CreateDate：2016-8-17
	 * @version 1.0
	 */
	@Action(value = "WDWinToInpatientAvgFee", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToInpatientAvgFee.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToInpatientAvgFee() {
		return "list";
	} 
	/**  
	 * @Description：住院均次费用分析
	 * @Author：yeguanqun
	 * @CreateDate：2016-8-17
	 * @version 1.0
	 */
	@Action(value = "wdWinToDrugFeeTolFeeRatio", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToDrugFeeTolFeeRatio.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String wdWinToDrugFeeTolFeeRatio() {
		return "list";
	}
	/**  
	 * @Description： 门诊医生工作量分析维度弹窗
	 * @Author：huimingzheng
	 * @CreateDate：2016-8-11 
	 * @version 1.0
	 */
	@Action(value = "WDWinTooptRecipedetail", results = { @Result(name = "WDWinTooptRecipedetail", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOptRecipedetail.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinTooptRecipedetail() {
		return "WDWinTooptRecipedetail";
	}
	/**  
	 * @Description：全院手术费用分析
	 * @Author：zhangjin
	 * @CreateDate：2016-8-17
	 * @version 1.0
	 */
	@Action(value = "WDOperationCostload", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/operation/operationCostList/wdWinToHospitaOperationCostList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDOperationCostload() {
		return "list";
	}
	/**  
	 * @Description：门诊费用分析
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22
	 * @version 1.0
	 */
	@Action(value = "WDWinToOutpatientCostloadcost", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientcost/wdOutpatientCostload.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOutpatientCostloadcost() {
		return "list";
	}
	/**  
	 * @Description：门诊费用分析
	 * @Author：zhangjin
	 * @CreateDate：2016-8-22
	 * @version 1.0
	 */
	@Action(value = "WDWinToOutPatientWorkloadbi", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientcost/wdOutpatientCostloadbi.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOutPatientWorkloadbi() {
		return "list";
	}
	
	
	/**  
	 * @Description：  门诊药品比例分析
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-8-22 
	 * @version 1.0
	 */
	@Action(value = "WDWinToOutPatientDrugRatio", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToOutPatientDrugRatio.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String WDWinToOutPatientDrugRatio() {
		return "list";
	}
	
}
