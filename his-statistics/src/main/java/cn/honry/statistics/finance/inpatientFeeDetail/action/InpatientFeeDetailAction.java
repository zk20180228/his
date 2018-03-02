package cn.honry.statistics.finance.inpatientFeeDetail.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.inpatientFeeDetail.service.InpatientFeeDetailService;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.CostDetailsVo;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.FeeDetailVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/statistics/InpatientFeeDetail")
@SuppressWarnings({"all"})
public class InpatientFeeDetailAction extends ActionSupport{
	private Logger logger=Logger.getLogger(InpatientFeeDetailAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String inpatientNo;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	/**
	 * 注入chargeBillService
	 */
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "inpatientFeeDetailService")
	private InpatientFeeDetailService inpatientFeeDetailService;		
	public void setInpatientFeeDetailService(InpatientFeeDetailService inpatientFeeDetailService) {
		this.inpatientFeeDetailService = inpatientFeeDetailService;
	}
	/**
	 * @see注入报表借口口
	 */
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/***
	 * 注入员工Service
	 */
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}	
	
	private InpatientInfo inpatientInfo;
	
	public InpatientInfo getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(InpatientInfo inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	private HttpServletRequest request = null;
	/**
	 * 在院和出院未结算标记
	 */
	private String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @Description:获取list页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-5-29
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"STAT-HZFYMXCX:function:view"})
	@Action(value="toViewFeeDetailStatList",results={@Result(name="list",location = "/WEB-INF/pages/stat/inpatientFeeDetailStat/inpatientFeeDetailStat.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewFeeDetailStatList(){
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(sys!=null){
			 ServletActionContext.getRequest().setAttribute("deptId",sys.getDeptCode());//获取当前病区
		}
		return "list";
	}	
	/**
	 * @Description:查询费用汇总列表
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryFeeDetailInfo", results = { @Result(name = "json", type = "json") })
	public void queryFeeDetailInfo(){
		try{
			List<FeeDetailVo> infoList = inpatientFeeDetailService.queryFeeInfo(inpatientInfo);
			String json = JSONUtils.toJson(infoList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_BQHZFYJMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_BQHZFYJMX", "住院统计_病区患者费用及明细", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description:查询费用汇总列表PDF
	 * @author conglin
	 * @CreateDate:2017-2-27
	 * @return binary
	 */
	@Action(value = "queryFeeDetailInfoPDF")
	public void queryFeeDetailInfoPDF(){
		try {
			request=ServletActionContext.getRequest();
			//收集信息
			ArrayList<FeeDetailVo> feeDetailVo=new ArrayList<FeeDetailVo>();
			feeDetailVo.add(new FeeDetailVo());
			//查询子报表记录
			feeDetailVo.get(0).setFeeDetailvo(inpatientFeeDetailService.queryFeeInfo(inpatientInfo));
			//姓名
			feeDetailVo.get(0).setFeeName(java.net.URLDecoder.decode(inpatientInfo.getAccountId(), "UTF-8"));
	
			//日期
			feeDetailVo.get(0).setFeeCode(inpatientInfo.getProfCode());
			//金额
			if(StringUtils.isBlank(inpatientInfo.getAlterType())){
				feeDetailVo.get(0).setTotcost(0.00);
			}else{
				feeDetailVo.get(0).setTotcost(Double.valueOf(inpatientInfo.getAlterType()));
			}
			//计算总金额
			double cost=0.00;
			 for(int i=0;i<feeDetailVo.get(0).getFeeDetailvo().size()-1;i++){
				 cost+=feeDetailVo.get(0).getFeeDetailvo().get(i).getTotcost();
			 }
			//数据传输到Jasper中
			request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/Collectfees.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("cost", cost);
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("serviceHopital", "费用汇总");
			 InpatientInfoNow inpatientInfoNow = inpatientFeeDetailService.queryFeeInpatientInfo(inpatientInfo.getInpatientNo());
			 if(inpatientInfoNow!=null){
				 parameters.put("feeName", inpatientInfoNow.getPatientName());
				 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				  String feeCode = formatter.format( inpatientInfoNow.getInDate());
				  parameters.put("feeCode",feeCode);
				 parameters.put("totcost", inpatientInfoNow.getFreeCost());
			 }else{
				 parameters.put("feeName","");
				 parameters.put("feeCode","");
				 parameters.put("totcost","");
			 }
				iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(feeDetailVo));
		} catch (Exception e) {
			logger.error("ZYSF_BQHZFYJMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_BQHZFYJMX", "住院统计_病区患者费用及明细", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description:查费用明细询列表
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryCostDetails", results = { @Result(name = "json", type = "json") })
	public void queryCostDetails(){
		try{
			List<CostDetailsVo> infoList = inpatientFeeDetailService.queryCostDetails(inpatientInfo);
			String json = JSONUtils.toJson(infoList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_BQHZFYJMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_BQHZFYJMX", "住院统计_病区患者费用及明细", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description:费用明细报表
	 * @author conglin
	 * @throws Exception
	 */
	@Action(value = "queryCostDetailsPDF")
	public void queryCostDetailsPDF(){
		try {
			ArrayList<CostDetailsVo> cosDetailsVo=new ArrayList<CostDetailsVo>();
			cosDetailsVo.add(new CostDetailsVo());
			cosDetailsVo.get(0).setCostDetailsVo(inpatientFeeDetailService.queryCostDetails(inpatientInfo));
			//渲染单位
			List<BusinessDictionary> cstl=innerCodeService.getDictionary("nonmedicineencoding");
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary cst:cstl){
				map.put(cst.getEncode(), cst.getName());
			}
			Map<String,String> drugpackagingunitMap = innerCodeService.getBusDictionaryMap("packunit");
			//渲染人
			Map<String, String> empMap = employeeInInterService.queryEmpCodeAndNameMap();
			for(CostDetailsVo vo:cosDetailsVo.get(0).getCostDetailsVo()){
				vo.setFeeOpercode(empMap.get(vo.getFeeOpercode()));
				//渲染单位
				if(vo.getItemType() == 2){
					if(map.get(vo.getCurrentUnit()) != null){
						vo.setCurrentUnit(map.get(vo.getCurrentUnit()));
					}
				}
				if(vo.getItemType() == 1){
					if(drugpackagingunitMap.get(vo.getCurrentUnit()) != null){
						vo.setCurrentUnit(drugpackagingunitMap.get(vo.getCurrentUnit()));
					}
				}
				
			}
			request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/CostDetails.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("serviceHopital", "费用明细");
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(cosDetailsVo));
		} catch (Exception e) {
			logger.error("ZYSF_BQHZFYJMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_BQHZFYJMX", "住院统计_病区患者费用及明细", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description： 查询患者树
	 * @throws IOException 
	 * @Author：zhenglin
	 */
	@Action(value = "InfoTree")
	public void InfoTree() {
		try{
			SysDepartment dept= ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			String json =null;
			if(dept!=null){
				List<TreeJson> patientTree=inpatientFeeDetailService.queryPatientTree(null,flag);
				json = JSONUtils.toJson(patientTree);
			}else{
				Map<String,String> map=new HashMap<String,String>();
				map.put("resCode", "error");
				map.put("resMsg", "请选择登录科室");
				json=JSONUtils.toJson(map);
			}
			WebUtils.webSendJSON(json);	
		}catch(Exception e){
			logger.error("ZYSF_BQHZFYJMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_BQHZFYJMX", "住院统计_病区患者费用及明细", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description： 查询患者
	 * @throws IOException 
	 * @Author：qh
	 */
	@Action(value = "queryFeeInpatientInfo")
	public void queryFeeInpatientInfo() {
		try{
			InpatientInfoNow inpatientInfo=inpatientFeeDetailService.queryFeeInpatientInfo(inpatientNo);
			String json = JSONUtils.toJson(inpatientInfo);
			WebUtils.webSendJSON(json);	
		}catch(Exception e){
			logger.error("ZYSF_BQHZFYJMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_BQHZFYJMX", "住院统计_病区患者费用及明细", "2", "0"), e);
			e.printStackTrace();
		}
	}
}
