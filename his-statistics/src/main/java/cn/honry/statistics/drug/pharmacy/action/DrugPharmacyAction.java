package cn.honry.statistics.drug.pharmacy.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.pharmacy.service.DrugPharmacyService;
import cn.honry.statistics.drug.pharmacy.vo.CopyOfPharmacyVoSecond;
import cn.honry.statistics.drug.pharmacy.vo.PharmacyVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 住院部取药统计action
 * @author  lyy
 * @createDate： 2016年6月21日 下午4:07:14 
 * @modifier lyy
 * @modifyDate：2016年6月21日 下午4:07:14
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/DrugPharmacy")
public class DrugPharmacyAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(DrugPharmacyAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private DrugPharmacyService drugPharmacyService;
	@Autowired
	@Qualifier(value="drugPharmacyService")
	public void setDrugPharmacyService(DrugPharmacyService drugPharmacyService) {
		this.drugPharmacyService = drugPharmacyService;
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	@Autowired
	@Qualifier(value="inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	/**
	 * @see注入打印接口
	 */
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	/**
	 * 开始时间
	 */
	private String startData;
	/**
	 * 结束时间
	 */
	private String endData;
	/**
	 * 药费类别
	 */
	private String drugCostType;
	/**
	 * 住院要房
	 */
	private String drugstore;
	/**
	 * 默认当前时间
	 */
	private String dataTime;
	public String sTime;
	public String eTime;
	/**
	 * 分页 页数
	 */
	private String page;
	/**
	 * 分页 记录数
	 */
	private String rows;
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
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public String getStartData() {
		return startData;
	}
	public void setStartData(String startData) {
		this.startData = startData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getDrugCostType() {
		return drugCostType;
	}
	public void setDrugCostType(String drugCostType) {
		this.drugCostType = drugCostType;
	}
	public String getDrugstore() {
		return drugstore;
	}
	public void setDrugstore(String drugstore) {
		this.drugstore = drugstore;
	}
	@SuppressWarnings("deprecation")
	@Action(value = "listDrugPharmacy", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/drug/pharmacy/pharmacyList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDrugPharmacy(){
		//获取时间
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	/**
	 * 查询所有的住院部取药列表
	 * @author  lyy
	 * @createDate： 2016年6月22日 上午10:46:33 
	 * @modifier lyy
	 * @modifyDate：2016年6月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value="queryDrugPharmacy")
	public void queryDrugPharmacy(){
		try{
			if(StringUtils.isBlank(startData)){
				Date date = new Date();
				startData = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(endData)){
				Date date = new Date();
				endData = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<PharmacyVo> list = drugPharmacyService.getPageDrugPharmacy(startData,endData,drugCostType,drugstore,page,rows);
			
//			int total = drugPharmacyService.getPageDrugPharmacyTotal(startData,endData,drugCostType,drugstore);
			String redKey="ZYTJ_ZYBQYTJ"+":"+startData+"_"+endData+"_"+drugCostType+"_"+drugstore;
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
			 	totalNum = drugPharmacyService.getPageDrugPharmacyTotal(startData,endData,drugCostType,drugstore);
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
			String json=JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_ZYBQYTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_住院部取药统计", "2", "0"), e);
		}
	}
	/**
	 * 导出
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午7:09:23 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午7:09:23
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="expDrugPharmacy")
	public void expDrugPharmacy() throws IOException{
		List<CopyOfPharmacyVoSecond> list=new ArrayList<>();
		try {
			list = drugPharmacyService.getPageDrugPharmacyPDF(startData,endData,drugCostType,drugstore);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("ZYTJ_ZYBQYTJ", e1);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYBQYTJ", "住院统计_住院部取药统计", "2", "0"), e1);
		}
		if(list==null || list.isEmpty()){
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录!");
		}
		String head = "";
		String name = "住院部取药统计";
		String arrayHead[] ={"药品名称","当前状态","摆药状态","规格","数量","单位","单价","金额"};
		for (String message: arrayHead) {
			head+=","+message;
		}
		head=head.substring(1);
		FileUtil fUtil=new FileUtil();
		String fileName=name+ DateUtils.formatDateY_M_D_H_M(new Date())+ ".csv";
		String filePath=ServletActionContext.getServletContext().getRealPath("/WEB-INF")+fileName;
		fUtil.setFilePath(filePath);
		fUtil.write(head);
		
		PrintWriter out=WebUtils.getResponse().getWriter();
		try{
			fUtil=drugPharmacyService.export(list,fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME+fileName);
			out.write("success");
		}catch (Exception e) {
			out.write("error");
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * @author conglin
	 * @See 住院部取药统计报表
	 */
	@Action(value="queryDrugPharmacyPDF")
	public void queryDrugPharmacyPDF(){
		Date date=null;
		if(StringUtils.isBlank(startData)){
			date= new Date();
			startData = DateUtils.formatDateYM(date)+"-01";
		}
		if(StringUtils.isBlank(endData)){
			date = new Date();
			endData = DateUtils.formatDateY_M_D(date);
		}
		try {
			ArrayList<CopyOfPharmacyVoSecond> pharmacyVoSecondList=new ArrayList<CopyOfPharmacyVoSecond>();
			CopyOfPharmacyVoSecond pharmacyVoSecond=new CopyOfPharmacyVoSecond();
			pharmacyVoSecond.setPharmacyVoSecond(( drugPharmacyService.getPageDrugPharmacyPDF(startData,endData,drugCostType,drugstore)));
			/**住院药房渲染**/
			List<SysDepartment> deptl = inpatientInfoInInterService.queryDeptMapPublic();
			Map<String, String> map=new HashMap<String, String>();
			for(SysDepartment dept:deptl){
				map.put(dept.getDeptCode(), dept.getDeptName());
			}
			
			Double money =0.00;
			//获取总金额
			for (CopyOfPharmacyVoSecond pVo : pharmacyVoSecond.getPharmacyVoSecond()) {
					money=money+pVo.getOutlCost();
					pVo.setDrugDeptCode(map.get(pVo.getDrugDeptCode()));
			}
			map=null;
			request=ServletActionContext.getRequest();
			 String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/PharmacyVo.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
			 parameters.put("cost", money);
			 parameters.put("serviceHopital", "住院部取药统计");
			 pharmacyVoSecondList.add(pharmacyVoSecond);
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(pharmacyVoSecondList));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ZYTJ_ZYBQYTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYBQYTJ", "住院统计_住院部取药统计", "2", "0"), e);
		}
		
	}
	
	/**
	 * 查询所有的住院部取药列表
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	@Action(value="queryDrugPharmacyNew")
	public void queryDrugPharmacyNew(){
		try{
			if(StringUtils.isBlank(startData)){
				Date date = new Date();
				startData = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(endData)){
				Date date = new Date();
				endData = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<PharmacyVo> list = drugPharmacyService.queryDrugPharmacyNew(startData,endData,drugCostType,drugstore,page,rows);
			
//			int total = drugPharmacyService.getTotalDrugPharmacyNew(startData,endData,drugCostType,drugstore);
			
			String redKey="ZYTJ_ZYBQYTJ"+":"+startData+"_"+endData+"_"+drugCostType+"_"+drugstore;
			Integer totalNum = (Integer) redisUtil.get(redKey);
			if(totalNum==null){
			 	totalNum = drugPharmacyService.getTotalDrugPharmacyNew(startData,endData,drugCostType,drugstore);
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
			String json=JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_ZYBQYTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_住院部取药统计", "2", "0"), e);
		}
}
}
