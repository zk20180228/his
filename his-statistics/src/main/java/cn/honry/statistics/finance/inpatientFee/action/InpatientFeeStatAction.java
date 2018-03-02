package cn.honry.statistics.finance.inpatientFee.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.inpatientFee.service.InpatientFeeStatService;
import cn.honry.statistics.finance.inpatientFee.vo.FeeInfosVo;
import cn.honry.statistics.finance.inpatientFee.vo.InpatientInfosVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/stat/inpatientFee")
@SuppressWarnings({"all"})
public class InpatientFeeStatAction extends ActionSupport{
	private Logger logger=Logger.getLogger(InpatientFeeStatAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Autowired
	@Qualifier(value = "inpatientFeeStatService")
	private InpatientFeeStatService inpatientFeeStatService;		
	public void setInpatientFeeStatService(InpatientFeeStatService inpatientFeeStatService) {
		this.inpatientFeeStatService = inpatientFeeStatService;
	}
	
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
/**		报表打印**/
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
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
	/**获取http请求**/
	private HttpServletRequest request;
	
	/**
	 * dsfdf:患者信息树节点Id
	 */
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @Description:获取list页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-5-29
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"STAT-HZFYCX:function:view"})
	@Action(value="toViewFeeStatList",results={@Result(name="list",location = "/WEB-INF/pages/stat/inpatientFeeStat/inpatientFeeStat.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewFeeStatList()throws Exception{
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(sys!=null){
			 ServletActionContext.getRequest().setAttribute("deptId",sys.getId());//获取当前病区
		}
		return "list";
	}	
	/**
	 * @Description:查询列表
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryInpatientInfo")
	public void queryInpatientInfo() throws Exception{
		try{
			List<InpatientInfosVo> inpatientInfoList = inpatientFeeStatService.queryInpatientInfo(inpatientInfo,id);
			for(InpatientInfosVo vo:inpatientInfoList){
			vo.setBalancePrepay(Double.parseDouble(new DecimalFormat("#.00").format(vo.getBalancePrepay())));
			vo.setBalanceCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getBalanceCost())));
			vo.setOwnCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getOwnCost())));
			vo.setFreeCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getFreeCost())));
			vo.setPayCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getPayCost())));
			vo.setPrepayCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getPrepayCost())));
			vo.setPubCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getPubCost())));
			vo.setTotCost(Double.parseDouble(new DecimalFormat("#.00").format(vo.getTotCost())));
		}
		String json = JSONUtils.toJson(inpatientInfoList, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description:患者住院信息打印
	 * @Author：  conglin
	 * @CreateDate： 2017-3-1
	 * @param    
	 * @return stream  
	 * @version 1.0
	**/
	@Action(value = "queryInpatientInfoPDF")
	public void queryInpatientInfoPDF() throws Exception{
		ArrayList<InpatientInfosVo> inpatientInfosVoList=new ArrayList<InpatientInfosVo>();
		InpatientInfosVo inpatientInfosVo=new InpatientInfosVo();
		inpatientInfosVo.setInpatientInfosVo( inpatientFeeStatService.queryInpatientInfo(inpatientInfo,id));
		//获取患者名
		inpatientInfosVo.setPatientName(inpatientInfosVo.getInpatientInfosVo().get(0).getPatientName());
		//获取住院号
		inpatientInfosVo.setInpatientNo(inpatientInfosVo.getInpatientInfosVo().get(0).getInpatientNo());
		for(InpatientInfosVo vo:inpatientInfosVo.getInpatientInfosVo()){
			if("R".equals(vo.getInState())){
				vo.setInState("住院登记");
			}else if("I".equals(vo.getInState())){
				vo.setInState("病房接诊");
			}else if("B".equals(vo.getInState())){
				vo.setInState("出院登记");
			}else if("O".equals(vo.getInState())){
				vo.setInState("出院结算");
			}else if("P".equals(vo.getInState())){
				vo.setInState("预约出院");
			}else if("I".equals(vo.getInState())){
				vo.setInState("病房接诊");
			}else if("N".equals(vo.getInState())){
				vo.setInState("出院退费");
			}else if("C".equals(vo.getInState())){
				vo.setInState("婴儿封账");
			}else {
				vo.setInState("暂无状态");
			}
		}
		request=ServletActionContext.getRequest();
		 String root_path = request.getSession().getServletContext().getRealPath("/");
		 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/queryMedicineList.jasper";
		 HashMap<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
		 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
		 inpatientInfosVoList.add(inpatientInfosVo);
		 try {
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inpatientInfosVoList));
		} catch (Exception e) {
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * 获取药品明细查询界面
	 * 
	 */
	@Action(value = "queryMedicineInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/inpatientFeeStat/medicineList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryMedicineInfo() {	
		return "list";
	}
	/**
	 * @Description:获取药品明细信息
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryMedicineList")
	public void queryMedicineList() {
		try{
			List<InpatientMedicineList> medicinelist = inpatientFeeStatService.queryMedicineList(inpatientInfo.getInpatientNo(),id);
			String json = JSONUtils.toJson(medicinelist);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取药品明细信息报表
	 * @Author：  丛林
	 * @CreateDate： 2017-3-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryMedicineListPDF")
	public void queryMedicineListPDF() throws Exception{
		InpatientMedicineList inpatientMedicine=new InpatientMedicineList();
		inpatientMedicine.setInpatientMedicineList( inpatientFeeStatService.queryMedicineList(inpatientInfo.getInpatientNo(),id));
		Map<String, String> employeeMap = inpatientFeeStatService.queryEmployeeMap();
		for(InpatientMedicineList vo:inpatientMedicine.getInpatientMedicineList()){
			vo.setFeeOpercode(employeeMap.get(vo.getFeeOpercode()));
			vo.setSenddrugOpercode(employeeMap.get(vo.getSenddrugOpercode()));
		}
		request=ServletActionContext.getRequest();
		 String root_path = request.getSession().getServletContext().getRealPath("/");
		 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/InpatientMedicine.jasper";
		 ArrayList<InpatientMedicineList> inpatientMedicineList=new ArrayList<InpatientMedicineList>();
		 HashMap<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
		 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
		 inpatientMedicineList.add(inpatientMedicine);
		 try {
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inpatientMedicineList));
		} catch (Exception e) {
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description:获取非药品明细信息
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryItemList", results = { @Result(name = "json", type = "json") })
	public void queryItemList() {
		try{
			List<InpatientItemList> itemList = inpatientFeeStatService.queryItemList(inpatientInfo.getInpatientNo(),id);
			String json = JSONUtils.toJson(itemList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取非药品明细信息报表
	 * @Author：  conglin
	 * @CreateDate： 2017-3-2
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryItemListPDF", results = { @Result(name = "json", type = "json") })
	public void queryItemListPDF() {
		try {
			InpatientItemList inpatientItem=new InpatientItemList();
			inpatientItem.setInpatientItemList( inpatientFeeStatService.queryItemList(inpatientInfo.getInpatientNo(),id));
			
			Map<String, String> employeeMap = inpatientFeeStatService.queryEmployeeMap();
			for(InpatientItemList vo:inpatientItem.getInpatientItemList()){
				vo.setFeeOpercode(employeeMap.get(vo.getFeeOpercode()));
			}
			request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/InpatientItem.jasper";
			 ArrayList<InpatientItemList> inpatientMedicineList=new ArrayList<InpatientItemList>();
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("name", java.net.URLDecoder.decode(inpatientInfo.getPatientName(), "UTF-8"));
			 parameters.put("id", inpatientInfo.getInpatientNo());
			 inpatientMedicineList.add(inpatientItem);
		
			 iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inpatientMedicineList));
		} catch (Exception e) {
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询", "2", "0"), e);
			e.printStackTrace();
		}
		
	}
	/**
	* @Title: queryInPrepay
	* @Description: 获取预交金信息
	* @param @throws Exception   
	* @return void    
	* @date 2016年6月7日下午6:52:37
	 */
	@Action(value = "queryInPrepay", results = { @Result(name = "json", type = "json") })
	public void queryInPrepay(){
		try{
			List<InpatientInPrepay> inPrepayList = inpatientFeeStatService.queryInPrepay(inpatientInfo.getInpatientNo(),id);
			String json = JSONUtils.toJson(inPrepayList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	/**
	* @Title: queryInPrepayPDF
	* @Description: 获取预交金信息报表
	* @param @throws Exception   
	* @return void    
	* @date 2017年3月2日下午
	 */
	@Action(value = "queryInPrepayPDF", results = { @Result(name = "json", type = "json") })
	public void queryInPrepayPDF(){
		try {
			InpatientInPrepay inpatientInPrepay=new InpatientInPrepay();
			inpatientInPrepay.setInpatientInPrepay( inpatientFeeStatService.queryInPrepay(inpatientInfo.getInpatientNo(),id));
			Map<String, String> employeeMap = inpatientFeeStatService.queryEmployeeMap();
			List<BusinessDictionary> typeList = innerCodeService.getDictionary("payway");
			for(InpatientInPrepay vo:inpatientInPrepay.getInpatientInPrepay()){
				if(vo.getBalanceState()==0){
					vo.setOpenBank("未结算");
				}else if(vo.getBalanceState()==1){
					vo.setOpenBank("已结算");
				}else if(vo.getBalanceState()==2){
					vo.setOpenBank("已结转");
				}else{
					vo.setOpenBank("异常状态");
				}
				vo.setCreateUser(employeeMap.get(vo.getCreateUser()));
				for(int i=0;i<typeList.size();i++){
					if(vo.getPayWay().equals(typeList.get(i).getEncode())){
						vo.setPayWay(typeList.get(i).getName());
						break;
					}
				}
			}
			request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/InpatientInPrepay.jasper";
			 ArrayList<InpatientInPrepay> inpatientMedicineList=new ArrayList<InpatientInPrepay>();
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("name", java.net.URLDecoder.decode(inpatientInfo.getPatientName(), "UTF-8"));
			 parameters.put("id", inpatientInfo.getInpatientNo());
			 inpatientMedicineList.add(inpatientInPrepay);
		
			 iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inpatientMedicineList));
		} catch (Exception e) {
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	* @Title: queryInPrepay
	* @Description: 获取费用汇总信息
	* @param @throws Exception   
	* @return void    
	* @date 2016年6月7日下午6:52:37
	 */
	@Action(value = "queryFeeInfo")
	public void queryFeeInfo() throws Exception{
		try{
			List<FeeInfosVo> feeInfoList = inpatientFeeStatService.queryFeeInfo(inpatientInfo.getInpatientNo(),id);
			String json = JSONUtils.toJson(feeInfoList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	* @Title: queryInPrepayPDF
	* @Description: 获取费用汇总信息报表
	* @param @throws Exception   
	* @return void    
	* @date 2017-3-2
	 */
	@Action(value = "queryFeeInfoPDF")
	public void queryFeeInfoPDF() throws Exception{
		FeeInfosVo feeInfosVo=new FeeInfosVo();
		feeInfosVo.setFeeInfosVo(inpatientFeeStatService.queryFeeInfo(inpatientInfo.getInpatientNo(),id));
		List<BusinessDictionary> type=innerCodeService.getDictionary("drugMinimumcost");
		for(FeeInfosVo vo:feeInfosVo.getFeeInfosVo()){
			for(BusinessDictionary vo1:type){
				if(vo.getFeeCode().equals(vo1.getEncode())){
					vo.setFeeCode(vo1.getName());
					break;
				}
				
			}
			
		}
		type=null;
		 try {
			 request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/report2FeeInfosVo.jasper";
			 ArrayList<FeeInfosVo> inpatientMedicineList=new ArrayList<FeeInfosVo>();
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("name", java.net.URLDecoder.decode(inpatientInfo.getPatientName(), "UTF-8"));
			 parameters.put("id", inpatientInfo.getInpatientNo());
			 inpatientMedicineList.add(feeInfosVo);
		
			 iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inpatientMedicineList));
		} catch (Exception e) {
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	* @Title: queryInPrepay
	* @Description: 获取结算信息
	* @param @throws Exception   
	* @return void    
	* @date 2016年6月7日下午6:52:37
	 */
	@Action(value = "queryBalanceInfo", results = { @Result(name = "json", type = "json") })
	public void queryBalanceInfo() {
		try{
			List<InpatientBalanceHead> balanceInfoList = inpatientFeeStatService.queryBalanceInfo(inpatientInfo.getInpatientNo(),id);
			String json = JSONUtils.toJson(balanceInfoList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	/**
	* @Title:
	* @Description: 获取结算信息报表
	* @param @throws Exception   
	* @return void    
	* @date 2016年6月7日下午6:52:37
	 */
	@Action(value = "queryBalanceInfoPDF")
	public void queryBalanceInfoPDF(){
		try {
			InpatientBalanceHead inpatientBalanceHead=new InpatientBalanceHead();
			inpatientBalanceHead.setInpatientBalanceHead( inpatientFeeStatService.queryBalanceInfo(inpatientInfo.getInpatientNo(),id));
			 request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/InpatientBalanceHead.jasper";
			 ArrayList<InpatientBalanceHead> inpatientMedicineList=new ArrayList<InpatientBalanceHead>();
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("name", java.net.URLDecoder.decode(inpatientInfo.getPatientName(), "UTF-8"));
			 parameters.put("id", inpatientInfo.getInpatientNo());
			 inpatientMedicineList.add(inpatientBalanceHead);
		 
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inpatientMedicineList));
		} catch (Exception e) {
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询", "2", "0"), e);
			e.printStackTrace();
		}
	
	}
	/**
	 * 获得渲染数据-员工Map
	 * 
	 */
	@Action(value="queryEmployeeMap")
	public void queryEmployeeMap(){	
		try{
			Map<String, String> employeeMap = inpatientFeeStatService.queryEmployeeMap();
			String json = JSONUtils.toJson(employeeMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	/**
	 * 获得渲染数据-费用名称Map
	 * 
	 */
	@Action(value="queryFeeNameMap")
	public void queryFeeNameMap(){	
		try{
			Map<String, String> feeNameMap = inpatientFeeStatService.queryFeeNameMap();
			String json = JSONUtils.toJson(feeNameMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  人员类别
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "paykind")
	public void paykind(){
		try{
			List<BusinessDictionary> typeList = innerCodeService.getDictionary("paykind");
			String mapJosn = JSONUtils.toJson(typeList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description： 支付方式渲染
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "payWay")
	public void payWay(){
		try{
			List<BusinessDictionary> typeList = innerCodeService.getDictionary("payway");
			String mapJosn = JSONUtils.toJson(typeList);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
	/**
	* @Title: queryBalanceList
	* @Description: 获取结算信息
	* @param inpatientNo 住院流水号
	* @param a 
	* @param @return   
	* @return List<InpatientBalanceList>    
	* @date 2016年6月7日下午6:40:45
	 */
	@Action(value = "queryBalanceList", results = { @Result(name = "json", type = "json") })
	public void queryBalanceList() throws Exception{
		try{
			List<InpatientBalanceList> itemList = inpatientFeeStatService.queryBalanceList(inpatientInfo.getInpatientNo(),id);
			String json = JSONUtils.toJson(itemList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_HZFYCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_HZFYCX", "住院统计_患者费用查询_打印", "2", "0"), e);
		}
	}
}
