package cn.honry.statistics.bi.bistac.imStacData.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.hospitalDischarge.service.HospitalDisService;
import cn.honry.statistics.bi.bistac.imStacData.dao.ImStacDataDao;
import cn.honry.statistics.bi.bistac.imStacData.service.ImStacDataService;
import cn.honry.statistics.bi.bistac.imStacData.service.InMonthTableService;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service.ListTotalIncomeStaticService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.bi.bistac.operationDept.service.OperationDeptTotalService;
import cn.honry.statistics.bi.bistac.operationIncome.service.OperationIncomeService;
import cn.honry.statistics.bi.bistac.operationNum.service.OperationNumsService;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.statistics.bi.bistac.totalDrugUsed.service.TotalDrugUsedService;
import cn.honry.statistics.bi.bistac.totalUnDrugUsed.service.TotalUnDrugUsedService;
import cn.honry.statistics.deptstat.illMedicalRecoder.service.IllMedicalRecoderService;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.service.WordLoadService;
import cn.honry.statistics.sys.reportForms.service.ReportFormsService;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * mongodb数据导入action
 * @author user
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/imStacDataAction")
@SuppressWarnings({ "all" })
public class ImStacDataAction extends ActionSupport {
	public static final String TABLENAME1 = "YZB";//药占比
	public static final String TABLENAME2 = "YYTS";//门诊用药天数
	public static final String TABLENAME3 = "YSYYJE";//医生用药金额表
	public static final String TABLENAME4 = "KSYYJE";//科室用药金额表
	public static final String TABLENAME5 = "YPJE";//门诊月药品金额，用药数量，人次表
	@Autowired
	@Qualifier(value = "inMonthTableService")
	private InMonthTableService inMonthTableService;
	
	@Autowired
	@Qualifier(value = "imStacDataDao")
	private ImStacDataDao imStacDataDao;
	@Autowired
	@Qualifier(value = "imStacDataService")
	private ImStacDataService imStacDataService;
	
	public ImStacDataService getImStacDataService() {
		return imStacDataService;
	}

	public void setImStacDataService(ImStacDataService imStacDataService) {
		this.imStacDataService = imStacDataService;
	}
	@Autowired
	@Qualifier(value = "monthlyDashboardService")
	private MonthlyDashboardService monthlyDashboardService;
	@Autowired
	@Qualifier(value = "listTotalIncomeStaticService")
	private ListTotalIncomeStaticService listTotalIncomeStaticService;
	@Autowired
	@Qualifier(value = "reportFormsService")
	private ReportFormsService reportFormsService;
	
	@Autowired
	@Qualifier(value="illMedicalRecoderService")
	private IllMedicalRecoderService illMedicalRecoderService;
	public void setIllMedicalRecoderService(
			IllMedicalRecoderService illMedicalRecoderService) {
		this.illMedicalRecoderService = illMedicalRecoderService;
		
	}
	@Autowired
	@Qualifier("hospitalDisService")
	private HospitalDisService hospitalDisService;
	public void setHospitalDisService(HospitalDisService hospitalDisService) {//住出院人次统计
		this.hospitalDisService = hospitalDisService;
	}
	
	@Autowired
	@Qualifier(value = "operationIncomeService")
	private OperationIncomeService operationIncomeService;
	@Autowired
	@Qualifier("totalDrugUsedService")
	private  TotalDrugUsedService totalDrugUsedService;
	public void setTotalDrugUsedService(TotalDrugUsedService totalDrugUsedService) {
		this.totalDrugUsedService = totalDrugUsedService;
	}
	@Autowired
	@Qualifier("totalUnDrugUsedService")
	private TotalUnDrugUsedService totalUnDrugUsedService;
	@Autowired
	@Qualifier("wordLoadService")
	private WordLoadService wordLoadService;
	@Autowired
	@Qualifier("operationDeptTotalService")
	private OperationDeptTotalService operationDeptTotalService;
	@Autowired
	@Qualifier(value = "operationNumsService")
	private OperationNumsService operationNumsService;
	@Action(value="importDate")
	public void importDate(){
		listTotalIncomeStaticService.initTotalMongYear();
		imStacDataService.imTableData_T_INPATIENT_INFO();

		imStacDataService.imTableData_T_OUTPATIENT_FEEDETAIL();
		WebUtils.webSendString("导入成功!");
	}
	 
	private String startTime;
	private String endTime;
	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	@Action(value="inTableData1")
	public void inTableData1(){
//		imStacDataService.inTableData();
//		imStacDataService.inTableData_YYTS();
//		imStacDataService.inTableData_YSYYJE();
//		imStacDataService.inTableData_KSYYJE();
//		imStacDataService.inTableData_YPJE2();
		imStacDataService.inTableData_ZYSRTJ();
//		imStacDataService.imTableData_T_OUTPATIENT_FEEDETAIL();
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将处方明细表导入mongodb(天)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午3:48:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午3:48:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableData")
	public void inTableData(){
//		OutpatientUseMedicVo findMin = imStacDataDao.findMin();
//		String begin= DateUtils.formatDateY_M_D_H_M_S(findMin.getsTime());
		String end = DateUtils.formatDateY_M_D_H_M_S(new Date());
		String begin="2017-05-23 23:59:59";
//		String end = "2017-05-24 00:00:00";
		imStacDataService.inTableData(begin, end);
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将处方明细表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午3:48:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午3:48:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM")
	public void inTableDataM(){
//		OutpatientUseMedicVo findMin = imStacDataDao.findMin();
//		Date begin = findMin.getsTime();
		inMonthTableService.inTableDataM("2017-05-23 23:59:59", DateUtils.formatDateY_M_D_H_M_S(new Date()));
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb(天)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午8:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableData_YYTS")
	public void inTableData_YYTS(){
		String begin="2017-03-01 00:00:00";
		String end = DateUtils.formatDateY_M_D_H_M_S(new Date());
//		OutpatientUseMedicVo findMin = imStacDataDao.findMin();
//		String begin= DateUtils.formatDateY_M_D_H_M_S(findMin.getsTime());
//		String end = DateUtils.formatDateY_M_D_H_M_S(new Date());
		imStacDataService.inTableData_YYTS(begin, end);
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午8:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM_YYTS")
	public void inTableDataM_YYTS(){
		inMonthTableService.inTableDataM_YYTS("2017-03-01 00:00:00", DateUtils.formatDateY_M_D_H_M_S(new Date()));
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将医生用药金额表导入mongodb(天)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午8:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableData_YSYYJE")
	public void inTableData_YSYYJE(){
		String begin="2017-05-01 00:00:00";
		String end = DateUtils.formatDateY_M_D_H_M_S(new Date());
		imStacDataService.inTableData_YSYYJE(begin, end);
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将医生用药金额表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午8:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM_YSYYJE")
	public void inTableDataM_YSYYJE(){
		inMonthTableService.inTableDataM_YSYYJE("2017-05-01 00:00:00", DateUtils.formatDateY_M_D_H_M_S(new Date()));
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将科室用药金额表导入mongodb(天)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午8:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableData_KSYYJE")
	public void inTableData_KSYYJE(){
		String begin="2017-05-01 00:00:00";
		String end = DateUtils.formatDateY_M_D_H_M_S(new Date());
//		String begin="2017-05-15 07:00:00";
//		String end = "2017-05-15 08:00:00";
		imStacDataService.inTableData_KSYYJE(begin, end);
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将科室用药金额表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午8:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM_KSYYJE")
	public void inTableDataM_KSYYJE(){
		inMonthTableService.inTableDataM_KSYYJE("2017-05-01 00:00:00",DateUtils.formatDateY_M_D_H_M_S(new Date()));
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb(天)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableData_YPJE2")
	public void inTableData_YPJE2(){
//		String begin="2017-05-01 00:00:00";
//		String end = DateUtils.formatDateY_M_D_H_M_S(new Date());
		String begin="2017-05-23 19:00:00";
		String end = "2017-05-23 20:00:00";
		imStacDataService.inTableData_YPJE2(begin, end);
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM_YPJE2")
	public void inTableDataM_YPJE2(){
		inMonthTableService.inTableDataM_YPJE2("2017-05-01 00:00:00", DateUtils.formatDateY_M_D_H_M_S(new Date()));
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 将住院的药品和非药品表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM_DRUGANDNO")
	public void inTableDataM_DRUGANDNO(){
		inMonthTableService.inTableDataM_DRUGANDNO("2011-01-01 00:00:00", DateUtils.formatDateY_M_D_H_M_S(new Date()));
		WebUtils.webSendString("导入成功!");
	}
	
	@Action(value="testImportData")
	public void testImportData() throws Exception{
		String startTime = "2017-04-20 00:00:00";
		String endTime="2017-06-29 23:59:00";
		System.out.println("导入成功！.......................................................");
		return ;
	}
	

	
	
	/**
	 * 门急诊人次统计-----有参
	 * @Author zxh
	 * @time 2017年5月23日
	 */
	@Action(value="imMZJZRCTJ")
	public void imMZJZRCTJ(){
	}
	//门诊急诊人次统计
	@Action(value="imTableData")
	public void imTableData(){
		imStacDataService.imTableData();
	}
	//门诊工作量-----按天-----有参
	@Action(value="imTableData_GHYSGZLDAY")
	public void imTableData_GHYSGZLDAY(){
		//imStacDataService.imTableData_GHYSGZLDAY(startTime ,endTime);
		WebUtils.webSendString("导入成功!");
	}
	//门诊工作量-----按月-----有参
	@Action(value="imTableData_GZLMonth1")
	public void imTableData_GZLMonth1(){
		//imStacDataService.imTableData_GZLMonth1(startTime ,endTime);
		WebUtils.webSendString("导入成功!");
	}
	//门诊工作量-----按天
	@Action(value="imTableData_GHYSGZL")
	public void imTableData_GHYSGZL(){
		imStacDataService.imTableData_GHYSGZL();
	}
	//门诊工作量--------按月
	@Action(value="imTableData_GZLMonth")
	public void imTableData_GZLMonth(){
		imStacDataService.imTableData_GZLMonth();
	}
	//门诊工作量--------按年
	@Action(value="imTableData_GZLYear")
	public void imTableData_GZLYear(){
		imStacDataService.imTableData_GZLYear();
	}
	//门诊处方量--------按天
	@Action(value="imTableData_MZCFGZL")
	public void imTableData_MZCFGZL(){
		imStacDataService.imTableData_MZCFGZL();
	}
	//门诊处方量--------按月
	@Action(value="imTableData_MZCFGZLMonth")
	public void imTableData_MZCFGZLMonth(){
		imStacDataService.imTableData_MZCFGZLMonth();
	}
	//门诊处方量--------按年
	@Action(value="imTableData_MZCFGZLYear")
	public void imTableData_MZCFGZLYear(){
		imStacDataService.imTableData_MZCFGZLYear();
	}
	/**
	 * 药品年\月\日with分类初始化
	 */
	@Action(value="imTableData_DrugInit")
	public void imTableData_DrugInit(){
		totalDrugUsedService.initDrugDB();
	}
	/**
	 * 药品年\月\日with科室
	 */
	@Action(value="DrugInitTopFiveDept")
	public void DrugInitTopFiveDept(){
		totalDrugUsedService.initDrugTopFiveDeptDB();
	}
	/**
	 * 药品年\月\日with医生
	 */
	@Action(value="DrugInitTopFiveDoc")
	public void DrugInitTopFiveDoc(){
		totalDrugUsedService.initDrugTopFiveDocDB();
	}
	/**
	 * 非药品年\月\日with分类初始化
	 */
	@Action(value="imTableData_UnDrugInit")
	public void imTableData_UnDrugInit(){
		totalUnDrugUsedService.initMonth("2013-01-01 00:00:00","2016-01-01 00:00:00");
		totalUnDrugUsedService.initYear("2013-01-01 00:00:00","2016-01-01 00:00:00");
	}
	/**
	 * 非药品年\月\日with科室分类初始化
	 */
	@Action(value="UnDrugInitWithDept")
	public void UnDrugInitWithDept(){
		totalUnDrugUsedService.initTopFiveDeptUnDrugDB();
	}
	/**
	 * 非药品年\月\日with医生分类初始化
	 */
	@Action(value="UnDrugInitWithDoc")
	public void UnDrugInitWithDoc(){
		totalUnDrugUsedService.initTopFiveDocUnDrugDB();
	}
	/**
	 * 收入科室年月日统计
	 */
	@Action(value="totalWithDept")
	public void totalWithDept(){
		listTotalIncomeStaticService.initTotalMongTotalWithDept();
	}
	/**
	 * 医生科室住院收入
	 */
	@Action(value="TotalDeptDoctor")
	public void totalWithDeptForApp(){

		listTotalIncomeStaticService.initMonth("2017-01-01 00:00:00", "2017-06-01 00:00:00");
		listTotalIncomeStaticService.initMZZYTotalByDayOrHours(startTime,endTime);

		listTotalIncomeStaticService.initMZZYTotalByDayOrHours(startTime,endTime);

	}
	@Action(value="TotalDeptDoctor1")
	public void totalWithDeptForApp1(){
		listTotalIncomeStaticService.initTotalDeptDoctor();
	}
	/**
	 * 收入门诊住院年月日统计
	 */
	@Action(value="totalWithDeptMZZY")
	public void totalWithDeptMZZY(){
		listTotalIncomeStaticService.initMonth("2014-01-01 00:00:00", "2014-12-31 00:00:00");
	}
	/**
	 * 月分析仪表盘
	 */
	@Action(value="monthlyInitOne")
	public void monthlyInitOne(){
		listTotalIncomeStaticService.initYear("2014-01-01 00:00:00", "2014-12-31 00:00:00");
	}
	/**
	 * 月分析仪表盘
	 */
	@Action(value="initDBTwo")
	public void initDBTwo(){
		try {
			monthlyDashboardService.initDBTwo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 住院医生工做量
	 */
//	@Action(value="initWordLoad")
//	public void initWordLoad(){
//		wordLoadService.initMongDBZYGZTJ();
//	}

	/**
	 * 手术科室工作量(按天查询oracle然后存到mongodb中)
	 * @throws ParseException 
	 */
	@Action(value="initOperationDeptTotalByDay")
	public void initOperationDeptTotalByDay() throws ParseException{
		String startTime = "2013-03-25 08:20:00";
		String endTime = "2013-08-25 08:20:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = sdf.parse(startTime);
		Date end = sdf.parse(endTime);
		if(begin.before(end)){
			try {
				operationDeptTotalService.initOperationDeptTotalByDay(startTime,endTime);
				WebUtils.webSendString("按月同步数据成功");
			} catch (Exception e) {
				WebUtils.webSendString("按月同步数据失败!!请联系管理员");
			}
		}else{
			WebUtils.webSendString("传入日期参数格式不正确!!!");
		}
	}
	
	/**
	 * 初始化手术收入统计(住院+门诊) 按天
	 */
	@Action(value="initOperationIncome")
	public void initOperationIncome(){
		operationIncomeService.initOperationIncome(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(手术分类) 按天
	 */
	@Action(value="initOperationIncomeOpType")
	public void initOperationIncomeOpType(){
		operationIncomeService.initOperationIncomeOpType(startTime,endTime);
	}

	/**
	 * 初始化手术收入统计(科室TOP5) 按天
	 */
	@Action(value="initOperationDeptTopFive")
	public void initOperationDeptTopFive(){
		operationIncomeService.initOperationDeptTopFive(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(医生TOP5) 按天
	 */
	@Action(value="initOperationDocTopFive")
	public void initOperationDocTopFive(){
		operationIncomeService.initOperationDocTopFive(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(同环比) 按天
	 */
	@Action(value="initOperationYoyRatio")
	public void initOperationYoyRatio(){
		operationIncomeService.saveOperYoyRatioToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(住院+门诊)按月份
	 */
	@Action(value="initOperationIncomeMonth")
	public void initOperationIncomeMonth(){
		operationIncomeService.initOperationIncomeMonth(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(住院+门诊)按年份
	 */
	@Action(value="initOperationIncomeYear")
	public void initOperationIncomeYear(){
		operationIncomeService.initOperationIncomeYear(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(手术类别)按月份
	 */
	@Action(value="initOperationIncomeTypeMonth")
	public void initOperationIncomeTypeMonth(){
		operationIncomeService.initOperIncomeTypeMonth(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(手术类别)按年份
	 */
	@Action(value="initOperationIncomeTypeYear")
	public void initOperationIncomeTypeYear(){
		operationIncomeService.initOperIncomeTypeYear(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(科室前五)按月份
	 */
	@Action(value="initOperTopFiveDeptMonth")
	public void initOperTopFiveDeptMonth(){
		operationIncomeService.initOperTopFiveDeptMonth(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(科室前五)按年份
	 */
	@Action(value="initOperTopFiveDeptYear")
	public void initOperTopFiveDeptYear(){
		operationIncomeService.initOperTopFiveDeptYear(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(医生前五)按月份
	 */
	@Action(value="initOperTopFiveDocMonth")
	public void initOperTopFiveDocMonth(){
		operationIncomeService.initOperTopFiveDocMonth(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(医生前五)按年份
	 */
	@Action(value="initOperTopFiveDocYear")
	public void initOperTopFiveDocYear(){
		operationIncomeService.initOperTopFiveDocYear(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(同环比)按月份
	 */
	@Action(value="initOperIncomYoyRatioMonth")
	public void initOperIncomYoyRatioMonth(){
		operationIncomeService.initOperIncomYoyRatioMonth(startTime,endTime);
	}
	
	/**
	 * 初始化手术收入统计(同环比)按年份
	 */
	@Action(value="initOperIncomYoyRatioYear")
	public void initOperIncomYoyRatioYear(){
		operationIncomeService.initOperIncomYoyRatioYear(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(门诊、住院、介入) 按天
	 */
	@Action(value="initOperationNums")
	public void initOperationNums(){
		operationNumsService.saveOperationNumsToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(科室TOP5) 按天
	 */
	@Action(value="initOperNumsTopFiveDept")
	public void initOperNumsTopFiveDept(){
		operationNumsService.saveOperationNumsTopDeptToDB(startTime,endTime);
	}
	
	/**
	 *初始化 手术例数统计(医生TOP5) 按天
	 */
	@Action(value="initOperNumsTopFiveDoc")
	public void initOperNumsTopFiveDoc(){
		operationNumsService.saveOperationNumsTopDocToDB(startTime,endTime);;
	}

	/**
	 * 初始化手术例数统计(同环比) 按天
	 */
	@Action(value="initOperNumsYoyRatio")
	public void initOperNumsYoyRatio(){
		operationNumsService.saveOperNumsYoyRatioToDB(startTime,endTime);
	}

	/**
	 * 初始化手术例数统计(门诊、住院、介入)按月份
	 */
	@Action(value="initOperationNumsMonth")
	public void initOperationNumsMonth(){
		operationNumsService.saveOperationNumsMonthToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(门诊、住院、介入)按年份
	 */
	@Action(value="initOperationNumsYear")
	public void initOperationNumsYear(){
		operationNumsService.saveOperationNumsYearToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(科室前五)按月份
	 */
	@Action(value="initOperNumsTopDeptMonth")
	public void initOperNumsTopDeptMonth(){
		operationNumsService.saveOperNumsTopDeptMonthToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(科室前五)按年份
	 */
	@Action(value="initOperNumsTopDeptYear")
	public void initOperNumsTopDeptYear(){
		operationNumsService.saveOperNumsTopDeptYearToDB(startTime,endTime);
	}

	/**
	 * 初始化手术例数统计(医生前五)按月份
	 */
	@Action(value="initOperNumsTopDocMonth")
	public void initOperNumsTopDocMonth(){
		operationNumsService.saveOperNumsTopDocMonthToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(医生前五)按年份
	 */
	@Action(value="initOperNumsTopDocYear")
	public void initOperNumsTopDocYear(){
		operationNumsService.saveOperNumsTopDocYearToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(同环比)按月份
	 */
	@Action(value="initOperNumsYoyRatioMonth")
	public void initOperNumsYoyRatioMonth(){
		operationNumsService.saveOperNumsYoyRatioMonthToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(同环比)按年份
	 */
	@Action(value="initOperNumsYoyRatioYear")
	public void initOperNumsYoyRatioYear(){
		operationNumsService.saveOperNumsYoyRatioYearToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(手术分类)按天
	 */
	@Action(value="initOperNumsOpType")
	public void initOperNumsOpType(){
		operationNumsService.saveOperNumsOpTypeToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(手术分类)按月
	 */
	@Action(value="initOperNumsOpTypeMonth")
	public void initOperNumsOpTypeMonth(){
		operationNumsService.saveOperNumsOpTypeMonthToDB(startTime,endTime);
	}
	
	/**
	 * 初始化手术例数统计(手术分类)按年
	 */
	@Action(value="initOperNumsOpTypeYear")
	public void initOperNumsOpTypeYear(){
		operationNumsService.saveOperNumsOpTypeYearToDB(startTime,endTime);
	}
	
	/**
	 * mongodb数据导入管理界面
	 * @return
	 */
	@Action(value = "impListView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/imStacDate.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String impListView(){
		return "list";
	}
	
	@Action(value="initOutByEs")
	public void initOutByEs(){
		Date date1 = DateUtils.parseDateY_M_D(startTime);
		Date date2 = DateUtils.parseDateY_M_D(endTime);
		int i = DateUtils.subDateGetDay(date1, date2);
		for (int j = 0; j < i; j++) {
			Date day1 = DateUtils.addDay(date1, j);
			Date day2 = DateUtils.addDay(date1, j+1);
			String date = DateUtils.formatDateY_M_D(day1);
			List<StatisticsVo> list = reportFormsService.listStatisticsQueryByES(null, null, date, DateUtils.formatDateY_M_D(day2));
			imStacDataService.inindata(list, date);
		}
	}
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月5日 下午4:42:03 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月5日 下午4:42:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableData_KSDBB")
	public void inTableData_KSDBB(){
		imStacDataService.inTableData_KSDBB("2016-04-01 00:00:00", "2016-04-30 23:59:59");
		WebUtils.webSendString("导入成功!");
	}
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月5日 下午4:42:03 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月5日 下午4:42:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="inTableDataM_KSDBB")
	public void inTableDataM_KSDBB(){
		inMonthTableService.inTableData_KSDBB("2016-04-01 23:59:59", "2016-04-30 23:59:59");
		WebUtils.webSendString("导入成功!");
	}
	/**
	 * 初始化危重病历人数比例统计分析
	 */
	@Action(value="initIllMedicalRecoder")
	public void initIllMedicalRecoder(){
		illMedicalRecoderService.saveIllMedicalToDB(startTime, endTime);
		WebUtils.webSendString("初始化成功！");
	}
	
}
