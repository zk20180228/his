package cn.honry.statistics.finance.outprepayDetail.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.outprepayDetail.service.OutDetailService;
import cn.honry.statistics.finance.outprepayDetail.vo.OutpatientOutprepayVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 院内卡充值消费明细统计
 * @ClassName: outDetailAction 
 * @Description: 
 * @author wfj
 * @date 2016年6月22日 上午10:13:20 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/statistics/OutDetail")
public class OutDetailAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private Logger logger=Logger.getLogger(OutDetailAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	private RedisUtil redisUtil;
	@Autowired
	@Qualifier(value = "redisUtil")
	public void setRedisUtil(RedisUtil redisUtil) { 
		this.redisUtil = redisUtil;
	}
	
	//栏目别名,在主界面中点击栏目时传到action的参数
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	/***
	 * 注入本类Service
	 */
	@Autowired
	@Qualifier(value = "outDetailService")
	private OutDetailService outDetailService;
	public void setOutDetailService(OutDetailService outDetailService) {
		this.outDetailService = outDetailService;
	}
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

/********************************	页面传递参数   Begin	*******************************************************************************/	
	//门诊预交金实体
	private OutpatientOutprepay outprepay;
	//病历号
	private String medicalrecordId;
	//检索开始时间
	private String beginDate;
	//检索结束时间
	private String endDate;
	//起始页数
	private String page;
	//数据条数
	private String rows;
	//视图view ： 1充值明细；2消费明细
	private String index;
	//就诊卡
	private String ic;
	//身份正
	private String idCard;
/*******************************    页面传递参数   end  *********************************************************************************/	
	/***
	 * 初始化view
	 * @Title: view 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @return String 视图
	 * @version 1.0
	 */
	@Action(value = "detailView", results = { @Result(name = "view", location = "/WEB-INF/pages/stat/outprepayDetail/detailView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String view(){
		return "view";
	}
	
	/***
	 * 统计
	 * @Title: query 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "query")
	public void query(){
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(medicalrecordId)){//初始化的时候不查询
			Integer total;
			if ("1".equals(index)) {
				String redKey = "YNCZXFMXTJ" + beginDate + "_" + endDate + "_" + medicalrecordId + "_" + index;
				total = (Integer) redisUtil.get(redKey);
				if(total == null){
					total = outDetailService.queryOutprepayTotal(page, rows, medicalrecordId, beginDate, endDate);
					redisUtil.set(redKey, total);
				}
				String val=parameterInnerService.getparameter("deadTime");
				if(StringUtils.isNotBlank(val)){
					redisUtil.expire(redKey,Integer.valueOf(val));
				}else{
					redisUtil.expire(redKey, 300);
				}
				List<OutpatientOutprepay> list = new ArrayList<>();
				if(total != null && total - 0 > 0){
					list = outDetailService.queryOutprepay(page, rows, medicalrecordId, beginDate,endDate);
				}
				map.put("total", total);
				map.put("rows", list);
			} else {
				String redKey = "YNCZXFMXTJ" + beginDate + "_" + endDate + "_" + medicalrecordId + "_" + index;
				total = (Integer) redisUtil.get(redKey);
				if(total == null){
					total = outDetailService.queryRecordTotal(page, rows, medicalrecordId, beginDate, endDate);
					redisUtil.set(redKey, total);
					redisUtil.expire(redKey, 300);
				}
				String val = parameterInnerService.getparameter("deadTime");
				if(StringUtils.isNotBlank(val)){
					redisUtil.expire(redKey,Integer.valueOf(val));
				}else{
					redisUtil.expire(redKey, 300);
				}
				List<OutpatientAccountrecord> list = new ArrayList<>();
				if(total != null && total - 0 > 0){
					list = outDetailService.queryRecord(page, rows, medicalrecordId, beginDate, endDate);
				}
				map.put("total", total);
				map.put("rows", list);
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
	}
	/***
	 * 通过病历号渲染患者
	 * @Title: medicalrecordIdMap 
	 * @author  WFJ
	 * @createDate ：2016年6月22日
	 * @return void
	 * @version 1.0
	 */
	public Map<String, Object> medicalrecordIdMap(){
		List<Patient> list = outDetailService.queryPatient();
		Map<String, Object> map = new HashMap<String,Object>();
		for(Patient model : list){
			map.put(model.getMedicalrecordId(), model.getPatientName());
		}
		return map;
	}
	
	@Action(value = "querymedicalrecord")
	public void querymedicalrecord(){
		List<Patient> registerList = outDetailService.querymedicalrecord(medicalrecordId);
		String json = JSONUtils.toJson(registerList);
		WebUtils.webSendJSON(json);

	}
	/**
	 * @author conglin
	 * @See 通过身份证/就诊卡查询病历号
	 */
	@Action(value="cardQueryMedicalrecord")
	public void cardQueryMedicalrecord(){
		//查询的时候如果是身份证的话,只查询了证件号而没有把证件类型传过来所以下面处理是拿出身份证的证件类型code
		Map<String, String> mapCertificate = innerCodeService
				.getBusDictionaryMap("certificate");
		String codeCertificate=null;
		Set<String> mapset = mapCertificate.keySet();
		Iterator<String> it = mapset.iterator();  
		while (it.hasNext()) {//遍历所有的key如果用这个key查出来的value是身份证,则保留key  
		  String str = it.next();  
		  if("身份证".equals(mapCertificate.get(str))){
			  codeCertificate=str;
			  break;
		  }
		}  
		List<Patient> mediList=outDetailService.cardQueryMedicalrecord(ic, idCard,codeCertificate);
		String json;
		if(mediList.size()>0){
			 json= JSONUtils.toJson(mediList.get(0).getMedicalrecordId());
		}else{
			json="false";
		}
		WebUtils.webSendJSON(json);
	}
	
	/** 导出记录
	* @Title: expOperactionlist 导出记录
	* @Description: 导出记录
	* @author dtl 
	* @date 2017年5月17日
	*/
	@Action(value = "expOperactionlist")
	public void expOperactionlist()  {
		PrintWriter out=null;
		try {
			if ("1".equals(index)) {
				List<OutpatientOutprepay> list = new ArrayList<>();
				list = outDetailService.queryOutprepayList(medicalrecordId, beginDate,endDate);
				if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
				String head = "";
				
				String name = "门诊患者充值明细统计";
				String[] headMessage = { "充值金额", "充值方式", "充值时间", "操作员"};
				
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
				fUtil = outDetailService.exportOutperpay(list, fUtil);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
				out.write("success");
				
			} else {
				List<OutpatientAccountrecord> list = new ArrayList<>();
				list = outDetailService.queryRecordList(medicalrecordId, beginDate, endDate);
				if (list == null || list.isEmpty()) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
				}
				String head = "";
				
				String name = "门诊患者消费明细统计";
				String[] headMessage = { "金额", "余额", "科室", "操作时间","操作人" };
				
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
				fUtil = outDetailService.exportRecord(list, fUtil);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
				out.write("success");
			}
		} catch (Exception e) {
			out.write("error");
			logger.error("YNCZXFMXTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YNCZXFMXTJ", "就诊卡管理_门诊充值消费明细统计", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	
	/** 充值明细打印
	* @Title: iReportMZCZMX 
	* @author dtl 
	* @date 2017年5月17日
	*/
	@Action(value = "iReportMZHZCZMXTJ")
	public void iReportMZHZCZMXTJ()  {
		try{
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String iPatientName = request.getParameter("iPatientName");//患者姓名
			String iMedicalrecordId = request.getParameter("iMedicalrecordId");//病历号
			String iBeginDate = request.getParameter("iBeginDate");//开始时间
			String iEndDate = request.getParameter("iEndDate");//结束时间
			String iIc = request.getParameter("iIc");//就诊卡号
			String iIdCard = request.getParameter("iIdCard");//身份证号
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			List<OutpatientOutprepay> outpatientOutprepays = outDetailService.queryOutprepayList(medicalrecordId, beginDate,endDate);
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			OutpatientOutprepayVo outprepayVo = new OutpatientOutprepayVo(iPatientName, iMedicalrecordId, iBeginDate, iEndDate, iIc, iIdCard, outpatientOutprepays, new ArrayList<OutpatientAccountrecord>());
			List<OutpatientOutprepayVo> list = new ArrayList<>();
			list.add(outprepayVo);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("YNCZXFMXTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YNCZXFMXTJ", "就诊卡管理_门诊充值消费明细统计", "2", "0"), e);
		}
	}
	
	
	
	/** 消费明细打印
	* @Title: iReportMZXFMX 消费明细打印
	* @author dtl 
	* @date 2017年5月17日
	*/
	@Action(value = "iReportMZHZXFMXTJ")
	public void iReportMZHZXFMXTJ()  {
		try{
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String iPatientName = request.getParameter("iPatientName");//患者姓名
			String iMedicalrecordId = request.getParameter("iMedicalrecordId");//病历号
			String iBeginDate = request.getParameter("iBeginDate");//开始时间
			String iEndDate = request.getParameter("iEndDate");//结束时间
			String iIc = request.getParameter("iIc");//就诊卡号
			String iIdCard = request.getParameter("iIdCard");//身份证号
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath + fileName + ".jasper";
			List<OutpatientAccountrecord> outpatientAccountrecords = outDetailService.queryRecordList(medicalrecordId, beginDate, endDate);
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			OutpatientOutprepayVo outprepayVo = new OutpatientOutprepayVo(iPatientName, iMedicalrecordId, iBeginDate, iEndDate, iIc, iIdCard, null, outpatientAccountrecords);
			List<OutpatientOutprepayVo> list = new ArrayList<>();
			list.add(outprepayVo);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("就诊卡管理_门诊充值消费明细统计", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHZCZXFMXTJ", "就诊卡管理_门诊充值消费明细统计", "2", "0"), e);
		}
	}
	
/*--------------------------------------------------------- get and set ------------------------------------------------------------------------*/
	public OutpatientOutprepay getOutprepay() {
		return outprepay;
	}
	public void setOutprepay(OutpatientOutprepay outprepay) {
		this.outprepay = outprepay;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getIc() {
		return ic;
	}
	public void setIc(String ic) {
		this.ic = ic;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
}
