package cn.honry.statistics.bi.bistac.mongoDataInit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MongoCount;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.statistics.deptWorkCount.service.UpdateKSGZLTJService;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.service.OutpatientIndicatorsService;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;
import cn.honry.inner.statistics.toListView.service.InnerToListViewService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.bi.bistac.deptAndFeeData.service.DeptAndFeeDataService;
import cn.honry.statistics.bi.bistac.hospitalDischarge.service.HospitalDisService;
import cn.honry.statistics.bi.bistac.kpi.service.KpiService;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service.ListTotalIncomeStaticService;
import cn.honry.statistics.bi.bistac.mongoDataInit.dao.MongoDataInitDao;
import cn.honry.statistics.bi.bistac.mongoDataInit.service.MongoDataInitService;
import cn.honry.statistics.bi.bistac.mongoDataInit.vo.DoctorWorkCountVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.bi.bistac.operationDept.service.OperationDeptTotalService;
import cn.honry.statistics.bi.bistac.operationIncome.service.OperationIncomeService;
import cn.honry.statistics.bi.bistac.operationNum.service.OperationNumsService;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.service.OutpatientDocRecipeService;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.service.OutpatientUseMedicService;
import cn.honry.statistics.bi.bistac.registerTotal.service.RegisterTotalService;
import cn.honry.statistics.bi.bistac.toListView.service.ToListViewService;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListViewVo;
import cn.honry.statistics.bi.bistac.totalDrugUsed.service.TotalDrugUsedService;
import cn.honry.statistics.bi.bistac.totalUnDrugUsed.service.TotalUnDrugUsedService;
import cn.honry.statistics.deptstat.hospitalday.service.impl.HospitaldayServiceImpl;
import cn.honry.statistics.deptstat.inpatientStatistics.service.InpatientStatisticsService;
import cn.honry.statistics.deptstat.internalCompare1.service.InternalCompare1Service;
import cn.honry.statistics.deptstat.internalCompare2.service.InternalCompare2Service;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.service.ItemVoService;
import cn.honry.statistics.deptstat.operationDeptLevel.service.OperationDeptLevelService;
import cn.honry.statistics.deptstat.operationProportion.service.OperationProportionService;
import cn.honry.statistics.deptstat.outpatientAntPresDetail.service.OutpatientAntService;
import cn.honry.statistics.deptstat.peopleNumOfOperation.service.PeopleNumOfOperationService;
import cn.honry.statistics.doctor.registerInfoGzltj.service.RegisterInfoGzltjService;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.service.WordLoadService;
import cn.honry.statistics.drug.anesthetic.service.AnestheticService;
import cn.honry.statistics.sys.reportForms.service.ReportFormsService;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.statistics.sys.reservationStatistics.service.ReservationStatisticsService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


@Service("mongoDataInitService")
@Transactional
@SuppressWarnings({ "all" })
public class MongoDataInitServiceImpl implements MongoDataInitService {
	
	@Resource
	RedisUtil redisUtils;
	@Autowired
	@Qualifier(value = "anestheticService")
	private AnestheticService anestheticService;
	@Autowired
	@Qualifier(value = "mongoDataInitDao")
	private MongoDataInitDao mongoDataInitDao;
	@Resource
	private UpdateKSGZLTJService updateKSGZLTJService;

	public void setMongoDataInitDao(MongoDataInitDao mongoDataInitDao) {
		this.mongoDataInitDao = mongoDataInitDao;
	}
	@Autowired
	@Qualifier(value = "itemVoService")
	private ItemVoService itemVoService;
	@Autowired
	@Qualifier(value = "outpatientUseMedicService")
	private OutpatientUseMedicService outpatientUseMedicService;
	@Autowired
	@Qualifier(value = "reportFormsService")
	private ReportFormsService reportFormsService;
	
	@Autowired
	@Qualifier(value = "registerInfoGzltjService")
	private RegisterInfoGzltjService registerInfoGzltjService;
	
	@Autowired
	@Qualifier(value = "deptAndFeeDataService")
	private DeptAndFeeDataService deptAndFeeDataService;
	@Autowired
	@Qualifier(value="internalCompare2Service")
	private InternalCompare2Service internalCompare2Service;
	@Autowired
	@Qualifier(value="monthlyDashboardService")
	private MonthlyDashboardService monthlyDashboardService;
	@Autowired
	@Qualifier(value = "listTotalIncomeStaticService")
	private ListTotalIncomeStaticService listTotalIncomeStaticService;
	@Autowired
	@Qualifier(value="peopleNumOfOperationService")
	private PeopleNumOfOperationService peopleNumOfOperationService;
	@Autowired
	@Qualifier(value="totalDrugUsedService")
	private TotalDrugUsedService totalDrugUsedService;
	@Autowired
	@Qualifier(value="totalUnDrugUsedService")
	private TotalUnDrugUsedService totalUnDrugUsedService;
	@Autowired
	@Qualifier(value="wordLoadService")
	private WordLoadService wordLoadService;
	@Autowired
	@Qualifier(value="outpatientAntService")
	private OutpatientAntService outpatientAntService;
	@Autowired
	@Qualifier(value = "toListViewService")
	private ToListViewService toListViewService;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	@Autowired
	@Qualifier(value = "outpatientDocRecipeService")
	private OutpatientDocRecipeService outpatientDocRecipeService;
	@Autowired
	@Qualifier(value = "kpiService")
	private KpiService kpiService;
	@Autowired
	@Qualifier(value = "operationNumsService")
	private OperationNumsService operationNumsService;
	@Autowired
	@Qualifier(value = "operationIncomeService")
	private OperationIncomeService operationIncomeService;
	@Autowired
	@Qualifier(value = "hospitaldayService")
	private HospitaldayServiceImpl hospitaldayService;
	public void setHospitaldayService(HospitaldayServiceImpl hospitaldayService) {
		this.hospitaldayService = hospitaldayService;
	}
	@Autowired
	@Qualifier(value = "outpatientIndicatorsService")
	private OutpatientIndicatorsService outpatientIndicatorsService;
	public void setOutpatientIndicatorsService(
			OutpatientIndicatorsService outpatientIndicatorsService) {
		this.outpatientIndicatorsService = outpatientIndicatorsService;
	}
	@Autowired
	@Qualifier(value="innerToListViewService")
	private InnerToListViewService innerToListViewService;
	@Autowired
	@Qualifier(value = "inpatientStatisticsService")
	private InpatientStatisticsService inpatientStatisticsService;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	private MongoBasicDao mbDao = null;
	
	@Autowired
	@Qualifier("employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value="operationDeptLevelService")
	private OperationDeptLevelService operationDeptLevelService;
	@Autowired
	@Qualifier(value="operationProportionService")
	private OperationProportionService operationProportionService;
	@Autowired
	@Qualifier(value="hospitalDisService")
	private HospitalDisService hospitalDisService;
	@Autowired
	@Qualifier(value="operationDeptTotalService")
	private OperationDeptTotalService operationDeptTotalService;
	@Autowired
	@Qualifier(value="internalCompare1Service")
	private InternalCompare1Service internalCompare1Service;
	@Autowired
	@Qualifier(value="reservationStatisticsService")
	private ReservationStatisticsService reservationStatisticsService;
	@Autowired
	@Qualifier(value="registerTotalService")
	private RegisterTotalService registerTotalService;
	/**
	 * mongodb数据初始化方法
	 * @param menuName 栏目名
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @return 
	 * @throws Exception 
	 */
	@Override
	public void mongoDataInit(String menuName,String beginDate,String endDate,Integer type) throws Exception{
		if("MZGXSRTJ".equals(menuName)){//门诊各项收入统计
			init_MZGXSRTJ(beginDate, endDate, type);
		}
		if("ZSRQKTJ".equals(menuName)){//总收入情况统计
			init_ZSRQKTJ(beginDate, endDate, type);
		}
		if("MJZRCTJ".equals(menuName)){//门急诊人次统计
			init_MJZRCTJ(beginDate, endDate, type);
		}
		if("SRTJB".equals(menuName)){//门诊收入统计（原栏目名为：收入统计表）
			init_SRTJB(beginDate, endDate, type);
		}
		if("ZZSRTJHZ".equals(menuName)){//住院收入统计
			init_ZYSRTj(beginDate, endDate, type);
		}
		if("GHKSGZLTJ".equals(menuName)){//挂号科室工作量统计
			init_GHKSGZLTJ(beginDate, endDate, type);
		}
		if("MZYSGZL".equals(menuName)){//门诊医生工作量
			init_MZGZL(beginDate, endDate, type);//门诊医生、科室工作量(移动端)初始化预处理
		}
		if("YYNKYXBHNEYXBDBBT".equals(menuName)){//医院内科医学部和内二医学部对比表2
			internalCompare2Service.init_YYNKYXBHNEYXBDBBT(beginDate, endDate, type);
		}
		if("MYZHYBP".equals(menuName)){//月分析仪表盘
			monthlyDashboardService.init_MYZHYBP(beginDate, endDate, type);
		}
		if("SSKSSSRSTJ".equals(menuName)){//手术科室手术人数
			peopleNumOfOperationService.init_SSKSSSRSTJ(beginDate, endDate, type);
		}
		if("YPSRFXHZ".equals(menuName)){//住院药品收入
			totalDrugUsedService.init_YPSRFXHZ(beginDate, endDate, type);
		}
		if("FYPSRFXHZ".equals(menuName)){//非药品收入
			totalUnDrugUsedService.init_FYPSRFXHZ(beginDate, endDate, type);
		}
		if("ZYYSGZLTJ".equals(menuName)){//住院医生工作量统计
//			wordLoadService.init_ZYYSGZLTJ(beginDate, endDate, type);
			wordLoadService.init_ZYSRQK(beginDate, endDate, type);
		}	
		if("KSGZLTJ".equals(menuName)){//科室工作量统计
//			updateKSGZLTJService.init_KSGZLTJ(beginDate, endDate, type);
			registerTotalService.initRegisterTotal(beginDate, endDate, type);
//			registerTotalService.tenTimingPerformRegister(beginDate);
		}
		if("MZYSKDGZL".equals(menuName)){//门诊医生开单工作量
			outpatientDocRecipeService.init_MZYSKDGZL(beginDate, endDate, type);
		}
		if("MZKJYWCFBL".equals(menuName)){//门诊抗菌药物处方比例预处理
			outpatientAntService.init_MZKJYWCFBL(beginDate, endDate, type);
		}
		if("MZGXZBTJ".equals(menuName)){//门诊各项指标统计
			init_MZGXZBTJ(beginDate, endDate, type);
		}
		if("SSLSTJ".equals(menuName)){//手术例数统计
			operationNumsService.initDate(beginDate, endDate, type);
		}
		if("SSSRTJ".equals(menuName)){//手术收入统计
			operationIncomeService.initDate(beginDate, endDate, type);
		}
		if("ZYRSTJ".equals(menuName)){//住院人数统计
			inpatientStatisticsService.init_ZYRSTJ(beginDate, endDate, type);
		}
		if("MZYSGZLTJ".equals(menuName)){//挂号医生工作量统计(门诊医生工作量)
			init_DocterWorkCount(beginDate, endDate, type);
		}
		if("SSKSSSFJTJ".equals(menuName)){//手术科室手术分级统计
			operationDeptLevelService.init_SSKSSSFJTJ(beginDate, endDate, type);
		}
		if("BQSHZBTJ".equals(menuName)){//手术占比统计
			operationProportionService.init_SSZBTJ(beginDate, endDate, type);
		}
		if("DRJYSJ".equals(menuName)){//当日经营数据
			hospitaldayService.init_DRJYSJ(beginDate, endDate, type);
		}
		if("ZCYRCTJ".equals(menuName)){//住出院人次统计
			hospitalDisService.init_ZCYRCTj(beginDate, endDate, type);
		}
		if("SSKSHZ".equals(menuName)){//手术科室汇总
			operationDeptTotalService.initOperation(beginDate, endDate, type);
		}
		if("YYNKYXBHNEYXBDBBO".equals(menuName)){//医院内科医学部和内二医学部对比表1
			internalCompare1Service.initCompare1(beginDate, endDate, type);
		}
		if("YYTJ".equals(menuName)){//预约统计预处理
			reservationStatisticsService.initReservation(beginDate, endDate, type);
		}
		if ("MZYYJK".equals(menuName)) {//门诊用药监控
			outpatientUseMedicService.init_MZYYJK(beginDate, endDate, type);
		}
		if ("SBKSDBB".equals(menuName)) {//科室对比表
			itemVoService.init_SBKSDBB(beginDate, endDate, type);
		}
		if("MZJSYPTJ".equals(menuName)){//麻醉精神药品统计
			anestheticService.init_MZJSYPTJ(beginDate, endDate, type);
		}
		if("YSGZLTJ".equals(menuName)){//pc端门诊医生工作量
			updateKSGZLTJService.initPCdoctorWork(beginDate, endDate, type);
		}
		if("MZKPI".equals(menuName)){//门诊KPI
			kpiService.init_MZKPI(beginDate, endDate, type);
		}
	}
	/**
	 * 门诊医生、科室工作量(移动端)初始化预处理
	 * @param beginDate
	 * @param endDate
	 * @param type
	 */
	public void init_MZGZL(String beginDate,String endDate,Integer type){
		MongoBasicDao mbDao = new MongoBasicDao();
		if(type==null ||type==1){//按日统计
//			mongoDataInitDao.init_MZGZL(beginDate, endDate);
			toListViewService.initPcRegisterDoctorWorkTotal(beginDate, "1");
		}else if(type==2){//按月统计
			BasicDBObject bdb = new BasicDBObject();
			Date startTime = DateUtils.parseDateY_M(beginDate.substring(0, 7));
			Date endTime = DateUtils.addMonth(DateUtils.parseDateY_M(endDate.substring(0, 7)), 1);
			while(DateUtils.compareDate(startTime, endTime)==-1){
				String begin = DateUtils.formatDateY_M(startTime);
				startTime = DateUtils.addMonth(startTime, 1);
				toListViewService.initPcRegisterDoctorWorkTotal(begin, "2");
//				BasicDBList dblist = new BasicDBList();
//				BasicDBObject sdb = new BasicDBObject();
//				sdb.put("regdate", new BasicDBObject("$gte", begin+"-01"));
//				BasicDBObject edb = new BasicDBObject();
//				edb.put("regdate", new BasicDBObject("$lt", end+"-01"));
//				dblist.add(sdb);
//				dblist.add(edb);
//				BasicDBObject where = new BasicDBObject();
//				where.put("$and", dblist);
				
				
//				DBCursor cursor = mbDao.findAlldata("T_TJ_MZCFDAY", where);
//				Map<String, DBObject> map = new HashMap<String, DBObject>();
//				while(cursor.hasNext()){
//					DBObject next = cursor.next();
//					String regdate = next.get("regdate").toString().substring(0, 7);
//					String doctCode = next.get("doctCode").toString();
//					String doctName = next.get("doctName").toString();
//					String deptCode = next.get("deptCode").toString();
//					String deptName = next.get("deptName").toString();
//					int cfNum = Integer.parseInt(next.get("cfNum").toString());
//					double totCost = Double.parseDouble(next.get("totCost").toString());
//					String key = regdate+"_"+doctCode+"_"+deptCode;
//					if(map.get(key)==null){
//						next.put("regdate", regdate);
//						map.put(key, next);
//					}else{
//						DBObject dbObject = map.get(key);
//						dbObject.put("regdate", regdate);
//						dbObject.put("cfNum", Integer.parseInt(dbObject.get("cfNum").toString())+cfNum);
//						dbObject.put("totCost", Double.parseDouble(dbObject.get("totCost").toString())+totCost);
//						map.put(key,dbObject);
//					}
//				}
//				for(String key : map.keySet()){
//					BasicDBObject dbObject = (BasicDBObject)map.get(key);
//					BasicDBObject reObject = new BasicDBObject();
//					reObject.put("regdate", dbObject.get("regdate").toString());
//					reObject.put("doctCode", dbObject.get("doctCode").toString()); 
//					reObject.put("deptCode", dbObject.get("deptCode").toString()); 
//					mbDao.remove("T_TJ_MZCFMONTH", reObject);
//					mbDao.addData("T_TJ_MZCFMONTH", dbObject);
//				}
			}
		}else{//按年统计
			BasicDBObject bdb = new BasicDBObject();
			Date startTime = DateUtils.parseDateY(beginDate.substring(0, 4));
			Date endTime = DateUtils.addYear(DateUtils.parseDateY(endDate.substring(0, 4)), 1);
			while(DateUtils.compareDate(startTime, endTime)==-1){
				String begin = DateUtils.formatDateY(startTime);
				startTime = DateUtils.addYear(startTime, 1);
//				String end = DateUtils.formatDateY(startTime);
				toListViewService.initPcRegisterDoctorWorkTotal(begin, "3");
//				bdb.put("regdate", new BasicDBObject("$gte", begin+"-01"));
//				bdb.put("regdate", new BasicDBObject("$lt", end+"-01"));
				
//				BasicDBList dblist = new BasicDBList();
//				BasicDBObject sdb = new BasicDBObject();
//				sdb.put("regdate", new BasicDBObject("$gte", begin+"-01"));
//				BasicDBObject edb = new BasicDBObject();
//				edb.put("regdate", new BasicDBObject("$lt", end+"-01"));
//				dblist.add(sdb);
//				dblist.add(edb);
//				BasicDBObject where = new BasicDBObject();
//				where.put("$and", dblist);
//				
//				DBCursor cursor = mbDao.findAlldata("T_TJ_MZCFMONTH", where);
//				Map<String, DBObject> map = new HashMap<String, DBObject>();
//				while(cursor.hasNext()){
//					DBObject next = cursor.next();
//					String regdate = next.get("regdate").toString().substring(0, 4);
//					String doctCode = next.get("doctCode").toString();
//					String doctName = next.get("doctName").toString();
//					String deptCode = next.get("deptCode").toString();
//					String deptName = next.get("deptName").toString();
//					int cfNum = Integer.parseInt(next.get("cfNum").toString());
//					double totCost = Double.parseDouble(next.get("totCost").toString());
//					String key = regdate+"_"+doctCode+"_"+deptCode;
//					if(map.get(key)==null){
//						next.put("regdate", regdate);
//						map.put(key, next);
//					}else{
//						DBObject dbObject = map.get(key);
//						dbObject.put("regdate", regdate);
//						dbObject.put("cfNum", Integer.parseInt(dbObject.get("cfNum").toString())+cfNum);
//						dbObject.put("totCost", Double.parseDouble(dbObject.get("totCost").toString())+totCost);
//						map.put(key,dbObject);
//					}
//				}
//				for(String key : map.keySet()){
//					BasicDBObject dbObject = (BasicDBObject)map.get(key);
//					BasicDBObject reObject = new BasicDBObject();
//					reObject.put("regdate", dbObject.get("regdate").toString());
//					reObject.put("doctCode", dbObject.get("doctCode").toString()); 
//					reObject.put("deptCode", dbObject.get("deptCode").toString()); 
//					mbDao.remove("T_TJ_MZCFYEAR", reObject);
//					mbDao.addData("T_TJ_MZCFYEAR", dbObject);
//				}
			}
		}
	}
	/**
	 * 门诊各项收入数据初始化
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	public void init_MZGXSRTJ(String beginDate,String endDate,Integer type){
		if(type==null ||type==1){//按日统计

			List<StatisticsVo> list = reportFormsService.listStatisticsQueryByES(null, null, beginDate, endDate);
			
			mongoDataInitDao.init_MZGXSRTJ(list, beginDate, type);
		
		}else if(type==2){//按月统计
			Date date1 = DateUtils.parseDateY_M(beginDate);
			Date date2 = DateUtils.parseDateY_M(endDate);
			while(DateUtils.compareDate(date1, date2)<=0){
				String sdate = DateUtils.formatDateY_M_D(date1);//当前月如2010-01-01
				Date nextDate = DateUtils.addMonth(date1, 1);
				String edate = DateUtils.formatDateY_M_D(nextDate);//下一月-->2010-02-01
				List<StatisticsVo> list = reportFormsService.listStatisticsQueryByES(null, null, sdate, edate);
				
				//规定只存月yyyy-MM格式
				String midMonth=sdate.substring(0,7);
				mongoDataInitDao.init_MZGXSRTJ(list, midMonth, type);
				
				date1=nextDate;//nextDate-->2010-02-01
			}
		}else{//按年统计
			Date date1 = DateUtils.parseDateY(beginDate);
			Date date2 = DateUtils.parseDateY(endDate);
			while(DateUtils.compareDate(date1, date2)<=0){
				String sdate = DateUtils.formatDateY_M_D(date1);//当前年--->2010-01-01
				Date nextDate = DateUtils.addYear(date1, 1);
				
				String edate = DateUtils.formatDateY_M_D(nextDate);//下一年//-->2011-01-01
				
				List<StatisticsVo> list = reportFormsService.listStatisticsQueryByES(null, null, sdate, edate);
				//规定只存年,yyyy格式
				String eYear=sdate.substring(0,4);
				
				mongoDataInitDao.init_MZGXSRTJ(list, eYear, type);
				date1=nextDate;
			}
			
		}
	}
	
	/**
	 * 总收入情况统计数据初始化
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @throws Exception 
	 */
	public void init_ZSRQKTJ(String startTime,String endTime,Integer type) throws Exception{
		if(type==null || type ==1){ //按日统计
				Map<String, Object> map = listTotalIncomeStaticService.queryTotalCountByES(startTime, "1");
				String json = JSONUtils.toJson(map);
				mongoDataInitDao.init_ZSRQKTJ(json, startTime, type);
		
		}else if(type==2){//按月统计
			Date date1 = DateUtils.parseDateY_M(startTime);
			Date date2 = DateUtils.parseDateY_M(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				String date = DateUtils.formatDateY_M(date1);//当前月如2010-01
				Map<String, Object> map = listTotalIncomeStaticService.queryTotalCountByES(date, "2");
				String json = JSONUtils.toJson(map);

				
				//规定只保存yyyy-MM格式
				mongoDataInitDao.init_ZSRQKTJ(json, date, type);
				date1=DateUtils.addMonth(date1, 1);
			}
		}else{//按年统计
			Date date1 = DateUtils.parseDateY(startTime);
			Date date2 = DateUtils.parseDateY(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				String date = DateUtils.formatDateY(date1);//如2010
				Map<String, Object> map = listTotalIncomeStaticService.queryTotalCountByES(date, "3");
				String json = JSONUtils.toJson(map);
				
				//规定只保存yyyy格式
				mongoDataInitDao.init_ZSRQKTJ(json, date, type);
				date1=DateUtils.addYear(date1, 1);
			}
		}
	}
	
	/**
	 * 门急诊人次统计数据初始化
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	public void init_MJZRCTJ(String startTime,String endTime,Integer type){
		String menuAlias="MJZRCTJ";
		if(type==null ||type==1){//按日统计
				ToListViewVo viewVo = toListViewService.queryVoByES(startTime, "1");
				String json = JSONUtils.toJson(viewVo);
				mongoDataInitDao.init_MJZRCTJ(json, startTime, type);
			    innerToListViewService.init(menuAlias, "HIS", startTime);
		}else if(type==2){//按月统计
			Date date1 = DateUtils.parseDateY_M(startTime);
			Date date2 = DateUtils.parseDateY_M(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				String date = DateUtils.formatDateY_M(date1);
				ToListViewVo viewVo = toListViewService.queryVoByES(date, "2");
				String json = JSONUtils.toJson(viewVo);
				mongoDataInitDao.init_MJZRCTJ(json, date, type);
				
				innerToListViewService.init_MonthOrYear(menuAlias, "2", date);
				date1=DateUtils.addMonth(date1, 1);
			}
		}else{//按年统计
			Date date1 = DateUtils.parseDateY(startTime);
			Date date2 = DateUtils.parseDateY(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				String date = DateUtils.formatDateY(date1);
				ToListViewVo viewVo = toListViewService.queryVoByES(date, "3");
				String json = JSONUtils.toJson(viewVo);
				mongoDataInitDao.init_MJZRCTJ(json, date, type);
				innerToListViewService.init_MonthOrYear(menuAlias, "3", date);
				date1=DateUtils.addYear(date1, 1);
			}
		}
	}
	
	/**
	 * 门诊收入统计数据初始化
	 * 栏目别名：SRTJB（原栏目名为：收入统计表）
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	public void init_SRTJB(String startTime,String endTime,Integer type){
		if(type==null || type ==1){ //按日统计

				Map<String, Object> map = reportFormsService.queryOutpatientChartsByES(startTime, "1");
				String json = JSONUtils.toJson(map);
				mongoDataInitDao.init_SRTJB(json, startTime, type);
				
		}else if(type==2){//按月统计
			Date date1 = DateUtils.parseDateY_M(startTime);
			Date date2 = DateUtils.parseDateY_M(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				String date = DateUtils.formatDateY_M(date1);
				Map<String, Object> map = reportFormsService.queryOutpatientChartsByES(date, "2");
				String json = JSONUtils.toJson(map);
				mongoDataInitDao.init_SRTJB(json, date, type);
				date1=DateUtils.addMonth(date1, 1);
			}
		}else{//按年统计
			Date date1 = DateUtils.parseDateY(startTime);
			Date date2 = DateUtils.parseDateY(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				String date = DateUtils.formatDateY(date1);
				Map<String, Object> map = reportFormsService.queryOutpatientChartsByES(date, "3");
				String json = JSONUtils.toJson(map);
				mongoDataInitDao.init_SRTJB(json, date, type);
				date1=DateUtils.addYear(date1, 1);
			}
		}
	}
	
	/**
	 * 住院收入统计数据初始化
	 * 栏目别名：ZYSR（原栏目名为：收入统计表）
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @throws Exception 
	 */
	public void init_ZYSRTj(String startTime,String endTime,Integer type) throws Exception{
		if(type==null || type==1){
				//住院收入前十名科室和收费项目饼状图统计
				String deptAndFeeData = deptAndFeeDataService.deptAndFeeData(startTime);
				//住院收入同环比统计
				String tonghuanbiData = deptAndFeeDataService.tonghuanbiDataByES(startTime, 6);
				String pcIncome=deptAndFeeDataService.queryInpatientChartsByES(startTime, "1");
				mongoDataInitDao.init_ZYSRTJ(deptAndFeeData, tonghuanbiData,pcIncome, startTime, type);

		}else if(type==2){//按月统计
			Date date1 = DateUtils.parseDateY_M(startTime);
			Date date2 = DateUtils.parseDateY_M(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				
				String date = DateUtils.formatDateY_M(date1);
				//住院收入前十名科室和收费项目饼状图统计
				String deptAndFeeData = deptAndFeeDataService.deptAndFeeData(date);
				//住院收入同环比统计
				String tonghuanbiData = deptAndFeeDataService.tonghuanbiDataByES(date, 6);
				String pcIncome=deptAndFeeDataService.queryInpatientChartsByES(date, "2");
				mongoDataInitDao.init_ZYSRTJ(deptAndFeeData, tonghuanbiData,pcIncome, date, type);
				date1=DateUtils.addMonth(date1, 1);
			}
		}else{//按年统计
			
			Date date1 = DateUtils.parseDateY(startTime);
			Date date2 = DateUtils.parseDateY(endTime);
			while(DateUtils.compareDate(date1, date2)<=0){
				
				String date = DateUtils.formatDateY(date1);
				//住院收入前十名科室和收费项目饼状图统计
				String deptAndFeeData = deptAndFeeDataService.deptAndFeeData(date);
				//住院收入同环比统计
				String tonghuanbiData = deptAndFeeDataService.tonghuanbiDataByES(date, 6);
				String pcIncome=deptAndFeeDataService.queryInpatientChartsByES(date, "3");
				mongoDataInitDao.init_ZYSRTJ(deptAndFeeData, tonghuanbiData,pcIncome, date, type);
				date1=DateUtils.addYear(date1, 1);
				
			}
		}
	}
	
	/** 挂号科室工作量统计
	* @Title: init_GHKSGZLTJ 
	* @Description: 
	* @param startTime
	* @param endTime
	* @param type 因为要根据设置来进行年月日的处理，所以这个参数暂时不用
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年6月20日
	*/
	public void init_GHKSGZLTJ(String startTime,String endTime,Integer type){
		String currentUser=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String addD = null;
		Date addDate = null;
		String nextaddM = null;
		Date nextaddMonth = null;
		Map<String, String> map = deptInInterService.querydeptCodeAndNameMap();
		try{
			//获取挂号科室工作量统计的设置信息
			MongoCount count = mongoDataInitDao.getMongoCount("GHKSGZLTJ");
			if(count.getStateY()!=null&&count.getStateY()==1&&type!=null&&type==3){//按年执行开启
				//执行方式  SQL 大数据 间接
				Date dateS = DateUtils.parseDateY(startTime);
				Date dateE = DateUtils.parseDateY(endTime);
				if(count.getExecuteWayY()!=null&&count.getExecuteWayY()==1){//sql
					int i = 0;
					Date addYear = null;
					Date addYear2 = null;
					String formatDateY = null;
					while(true){
						try{
							addYear = DateUtils.addYear(dateS, i);
							addYear2 = DateUtils.addYear(dateS, i+1);
							addD = DateUtils.formatDateY(addYear);
							formatDateY = DateUtils.formatDateY(addYear2);
							mongoDataInitDao.init_GHKSGZLTJ_D_S(addD, formatDateY ,"3", map);
							if(addYear.compareTo(dateE)>=0){
								break;
							}
							i++;
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
				if(count.getExecuteWayY()!=null&&count.getExecuteWayY()==2){//大数据
					int i=0;
					while(true){
						try{
							addDate = DateUtils.addMonth(dateS, i);
							addD = DateUtils.formatDateY(addDate);
							Date addYear1 = DateUtils.addYear(dateS, i+1);
							String addD1 = DateUtils.formatDateY(addYear1);
							mongoDataInitDao.init_GHKSGZLTJ_D_D(addD+"-01-01", addD1+"-01-01","3",map,addD);
							if(addDate.compareTo(dateE)>=0){
								break;
							}
							i++;
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
				if(count.getExecuteWayY()!=null&&count.getExecuteWayY()==3){//间接
					String[] start = startTime.split("-");
					String[] end = endTime.split("-");
					String sTime = start[0]+"-01-01";
					String eTime = end[0]+"-01-01";
					Date sdate = DateUtils.parseDateY_M_D(sTime);
					Date edate = DateUtils.parseDateY_M_D(eTime);
					int i = 0;
					while(true){
						try{
							Date addYear = DateUtils.addYear(sdate, i);
							Date addYearN = DateUtils.addYear(sdate, i+1);
							String date = DateUtils.formatDateY_M(addYear);
							String dateN = DateUtils.formatDateY_M(addYearN);
							mongoDataInitDao.inserDoc(date, dateN, date.split("-")[0], "GHKSGZLTJ_MONTH","GHKSGZLTJ_YEAR", map);
							if(addYear.compareTo(edate)>=0){
								break;
							}
							i++;
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
			}
			if(count.getStateM()!=null&&count.getStateM()==1&&type!=null&&type==2){//按月执行开启
				//执行方式  SQL 大数据 间接
				Date dateS = DateUtils.parseDateY_M(startTime);
				Date dateE = DateUtils.parseDateY_M(endTime);
				if(count.getExecuteWayM()!=null&&count.getExecuteWayM()==1){//sql
					int i = 0;
					while(true){
						try{
							addDate = DateUtils.addMonth(dateS, i);
							nextaddMonth = DateUtils.addMonth(dateS, i+1);
							addD = DateUtils.formatDateY_M(addDate);
							nextaddM = DateUtils.formatDateY_M(nextaddMonth);
							mongoDataInitDao.init_GHKSGZLTJ_D_S(addD, nextaddM ,"2", map);
							if(addDate.compareTo(dateE)>=0){
								break;
							}
							i++;
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
				if(count.getExecuteWayM()!=null&&count.getExecuteWayM()==2){//大数据
					int i=0;
					while(true){
						try{
							addDate = DateUtils.addMonth(dateS, i);
							addD = DateUtils.formatDateY_M(addDate);
							Date addDate1 = DateUtils.addMonth(dateS, i+1);
							String addD1 = DateUtils.formatDateY_M(addDate1);
							mongoDataInitDao.init_GHKSGZLTJ_D_D(addD+"-01", addD1+"-01","2",map,addD);
							if(addDate.compareTo(dateE)>=0){
								break;
							}
							i++;
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
				if(count.getExecuteWayM()!=null&&count.getExecuteWayM()==3){//间接
					String[] start = startTime.split("-");
					String[] end = endTime.split("-");
					String sTime = start[0]+"-"+start[1]+"-01";
					String eTime = end[0]+"-"+end[1]+"-01";
					Date sdate = DateUtils.parseDateY_M_D(sTime);
					Date edate = DateUtils.parseDateY_M_D(eTime);
					int i = 0;
					while(true){
						try{
							Date addMonth = DateUtils.addMonth(sdate, i);
							Date addYearN = DateUtils.addMonth(sdate, i+1);
							String date = DateUtils.formatDateY_M_D(addMonth);
							String dateN = DateUtils.formatDateY_M_D(addYearN);
							String[] split = date.split("-");
							mongoDataInitDao.inserDoc(date, dateN,split[0]+"-"+split[1] , "GHKSGZLTJ_DAY","GHKSGZLTJ_MONTH", map);
							if(addMonth.compareTo(edate)>=0){
								break;
							}
							i++;
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
				
			}
			if(count.getStateD()!=null&&count.getStateD()==1&&type!=null&&type==1){//按天执行开启
				//执行方式  SQL 大数据 间接
				Date sdate = DateUtils.parseDateY_M_D(startTime);
				Date edate = DateUtils.parseDateY_M_D(endTime);
				Date addDay = null;
				String formatDateY_M_D = null;
				if(count.getExecuteWayD()!=null&&count.getExecuteWayD()==2){//按大数据执行
					mongoDataInitDao.init_GHKSGZLTJ_D_D(startTime, endTime,"1",map,startTime);
				}
				if(count.getExecuteWayD()!=null&&count.getExecuteWayD()==1){//按SQL执行
					mongoDataInitDao.init_GHKSGZLTJ_D_S(startTime, endTime ,"1", map);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	@Override
	public List<MongoLog> findMongoLog(String menuType) {
		return mongoDataInitDao.findMongoLog(menuType);
	}
	@Override
	public MongoCount getMongoCount(String menuType) {
		return mongoDataInitDao.getMongoCount(menuType);
	}
	public void init_MZGXZBTJ(String sTime,String eTime,Integer type){
		MongoCount count = mongoDataInitDao.getMongoCount("MZGXZBTJ");
		String eDateY_M_D = null;
		String sDateY_M_D = null;
		try{
			if(count.getStateY()!=null&&count.getStateY()==1&&type!=null&&type==3){//按年执行开启
				sTime += "-01-01";
				eTime += "-01-01";
				Date sdate = DateUtils.parseDateY_M_D(sTime);
				Date edate = DateUtils.parseDateY_M_D(eTime);
				Date sDate = null;
				Date eDate = null;
				int a = 0;
				if(count.getExecuteWayY()!=null&&count.getExecuteWayY()==1){//sql
					while(true){
						sDate = DateUtils.addYear(sdate, a);
						eDate = DateUtils.addYear(sdate, a+1);
						sDateY_M_D = DateUtils.formatDateY_M_D(sDate);
						eDateY_M_D = DateUtils.formatDateY_M_D(eDate);
						Map<String, List<OutpatientIndicatorsVO>> map = outpatientIndicatorsService.findOutpatientIndicators(sDateY_M_D, eDateY_M_D, true);
						//初始化到mongoDB中
						for (Entry<String, List<OutpatientIndicatorsVO>> m : map.entrySet()) {
							String insertDate = DateUtils.formatDateY_M_D(sDate);
							mongoDataInitDao.insertDocMZGXZBTJ(insertDate, m.getKey()+"_YEAR", m.getValue());
						}
						if(sDate.compareTo(edate)>=0){
							break;
						}
						a++;
					}
				}
				if(count.getExecuteWayY()!=null&&count.getExecuteWayY()==2){//大数据  无大数据方法
					
				}
				if(count.getExecuteWayY()!=null&&count.getExecuteWayY()==3){//间接
					while(true){
						sDate = DateUtils.addYear(sdate, a);
						eDate = DateUtils.addYear(sdate, a+1);
						sDateY_M_D = DateUtils.formatDateY_M_D(sDate);
						eDateY_M_D = DateUtils.formatDateY_M_D(eDate);
						String insertDate = DateUtils.formatDateY(sDate);
						List<OutpatientIndicatorsVO> list = mongoDataInitDao.queryFromMongo(sDateY_M_D, eDateY_M_D, "RPJMZRC_MONTH");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "RPJMZRC_YEAR", this.CountAndClassify(list));
						List<OutpatientIndicatorsVO> list2 = mongoDataInitDao.queryFromMongo(sDateY_M_D, eDateY_M_D, "MZRJZCRC_MONTH");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "MZRJZCRC_YEAR", this.CountAndClassify(list2));
						List<OutpatientIndicatorsVO> list3 = mongoDataInitDao.queryFromMongo(sDateY_M_D, eDateY_M_D, "RJMZFY_MONTH");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "RJMZFY_YEAR", this.CountAndClassify(list3));
						List<OutpatientIndicatorsVO> list4 = mongoDataInitDao.queryFromMongo(sDateY_M_D, eDateY_M_D, "RYRS_MONTH");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "RYRS_YEAR", this.CountAndClassify(list4));
						if(sDate.compareTo(edate)>=0){
							break;
						}
						a++;
					}
				}
			}
			if(count.getStateM()!=null&&count.getStateM()==1&&type!=null&&type==2){//按月执行开启
				sTime += "-01";
				eTime += "-01";
				Date sDate = DateUtils.parseDateY_M_D(sTime);
				Date eDate = DateUtils.parseDateY_M_D(eTime);
				String strDateS = null;
				String strDateE = null;
				int a = 0;
				if(count.getExecuteWayM()!=null&&count.getExecuteWayM()==1){//sql
					while(true){
						Date addMonth = DateUtils.addMonth(sDate, a);
						Date addMonth2 = DateUtils.addMonth(sDate, a+1);
						strDateS = DateUtils.formatDateY_M_D(addMonth);
						strDateE = DateUtils.formatDateY_M_D(addMonth2);
						Map<String, List<OutpatientIndicatorsVO>> map = outpatientIndicatorsService.findOutpatientIndicators(strDateS, strDateE, true);
						//初始化到mongoDB中
						for (Entry<String, List<OutpatientIndicatorsVO>> m : map.entrySet()) {
							String insertDate = DateUtils.formatDateY_M(addMonth);
							mongoDataInitDao.insertDocMZGXZBTJ(insertDate, m.getKey()+"_MONTH", m.getValue());
						}
						if(addMonth.compareTo(eDate)>=0){
							break;
						}
						a++;
					}
				}
				if(count.getExecuteWayM()!=null&&count.getExecuteWayM()==2){//大数据
					
				}
				if(count.getExecuteWayM()!=null&&count.getExecuteWayM()==3){//间接
					while(true){
						Date addMonth = DateUtils.addMonth(sDate, a);
						Date addMonth2 = DateUtils.addMonth(sDate, a+1);
						strDateS = DateUtils.formatDateY_M_D(addMonth);
						strDateE = DateUtils.formatDateY_M_D(addMonth2);
						String insertDate = DateUtils.formatDateY_M(addMonth);
						List<OutpatientIndicatorsVO> list = mongoDataInitDao.queryFromMongo(strDateS, strDateE, "RPJMZRC_DAY");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "RPJMZRC_MONTH", this.CountAndClassify(list));
						List<OutpatientIndicatorsVO> list2 = mongoDataInitDao.queryFromMongo(strDateS, strDateE, "MZRJZCRC_DAY");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "MZRJZCRC_MONTH", this.CountAndClassify(list2));
						List<OutpatientIndicatorsVO> list3 = mongoDataInitDao.queryFromMongo(strDateS, strDateE, "RJMZFY_DAY");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "RJMZFY_MONTH", this.CountAndClassify(list3));
						List<OutpatientIndicatorsVO> list4 = mongoDataInitDao.queryFromMongo(strDateS, strDateE, "RYRS_DAY");
						mongoDataInitDao.insertDocMZGXZBTJ(insertDate, "RYRS_MONTH", this.CountAndClassify(list4));
						if(addMonth.compareTo(eDate)>=0){
							break;
						}
						a++;
					}
				}
			}
			if(count.getStateD()!=null&&count.getStateD()==1&&type!=null&&type==1){//按天执行开启
				if(count.getExecuteWayD()!=null&&count.getExecuteWayD()==2){//按大数据执行
					
				}
				if(count.getExecuteWayD()!=null&&count.getExecuteWayD()==1){//按SQL执行
					Map<String, List<OutpatientIndicatorsVO>> map = outpatientIndicatorsService.findOutpatientIndicators(sTime, eTime, true);
					//初始化到mongoDB中
					for (Entry<String, List<OutpatientIndicatorsVO>> m : map.entrySet()) {
						mongoDataInitDao.insertDocMZGXZBTJ(sTime, m.getKey()+"_DAY", m.getValue());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public List<OutpatientIndicatorsVO> CountAndClassify(List<OutpatientIndicatorsVO> volist){
		List<OutpatientIndicatorsVO> list = new ArrayList<OutpatientIndicatorsVO>();
		Map<String,OutpatientIndicatorsVO> map = new HashMap<String, OutpatientIndicatorsVO>();
		for (OutpatientIndicatorsVO vo : volist) {
			if(map.containsKey(vo.getDeptCode())){
				OutpatientIndicatorsVO indicatorsVO = map.get(vo.getDeptCode());
				indicatorsVO.setDenominator(indicatorsVO.getDenominator()+vo.getDenominator());
				indicatorsVO.setNumerator(indicatorsVO.getNumerator()+vo.getNumerator());
				map.put(vo.getDeptCode(), indicatorsVO);
			}else{
				map.put(vo.getDeptCode(), vo);
			}
		}
		for (Entry<String, OutpatientIndicatorsVO> m : map.entrySet()) {
			list.add(m.getValue());
		}
		return list;
	}
	
	
	/**
	 * 
	 * <p>医生工作量统计初始化 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月17日 下午4:57:13 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月17日 下午4:57:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime 开始时间 yyyy-MM-dd
	 * @param etime 结束时间yyyy-MM-dd
	 * @param type 更新集合的类型，年，月，日的类型分别是3，2，1
	 * @throws:
	 *
	 */
	public void init_DocterWorkCount(String stime,String etime,Integer type) {
		
		try{
			mbDao = new MongoBasicDao();
			if(null==type||1==type){
				Map<String, Object> map = registerInfoGzltjService.statRegDorWorkByES(stime, etime, "all", "all",null, null, "GHYSGZLTJ");
				List<RegisterInfoGzltjVo> list  = (List) map.get("rows");//得到list
				if(list==null||list.size()==0){
					return;
				}
				//遍历list,设置挂号日期，挂号医生编号，挂号医生名字，挂号数量
				for(RegisterInfoGzltjVo r :list){
					DoctorWorkCountVo vo = new DoctorWorkCountVo();
					String deptCode=r.getExpxrt();
					vo.setDoctorCode(deptCode);//设置医生的编号
					vo.setDoctorGzlNum(r.getNum());//设置挂号数量
					vo.setRegDate(stime);//设置挂号日期
					
					SysEmployee emp=(SysEmployee) redisUtils.hget("emp",deptCode);//得到缓存中的员工对象
					if(emp!=null&&emp.getName()!=null){
						vo.setDoctorName(emp.getName());//设置医生的名字
					}else{
						SysEmployee employee = employeeInInterService.findEmpByuserCode(deptCode);
						vo.setDoctorName(employee.getName());//设置医生的名字
	//					continue;
					}
					//插入到mongodb中
					mongoDataInitDao.init_DocterWorkCount(vo, type);
				}
			}else{
				
				Date date1 = null;
				Date date2 = null;
				if(2==type){
					date1 = DateUtils.parseDateY_M(stime);
					date2 = DateUtils.parseDateY_M(etime);
				}else{
					date1 = DateUtils.parseDateY(stime);
					date2 = DateUtils.parseDateY(etime);
				}
				String collectionName=null;
				while(DateUtils.compareDate(date1, date2)<=0){
					String date =null;
					if(2==type){//初始化月集合，需要查日表,时间大于等于开始月1号，小于结束月的下月1号，如2015-01-01 - 2015-05-06 stime='2015-01-01' etime='2015-06-01',需要注意的是：一个月一个月查，然后存
						collectionName="GHYSGZLTJ_DAY";
						date = DateUtils.formatDateY_M(date1);
						stime=date+"-01";
						etime=DateUtils.formatDateY_M(DateUtils.addMonth(DateUtils.parseDateY_M(date), 1))+"-01";
					}else{//初始化年集合，需要查月表，时间大于等于开始年1月，小于结束年的下年1月，如2015-01-01 - 2015-05-06 stime='2015-01' etime='2016-01',需要注意的是：一个年一个年查，然后存
						collectionName="GHYSGZLTJ_MONTH";
						date = DateUtils.formatDateY(date1);
						stime=date+"-01";
						etime=DateUtils.formatDateY(DateUtils.addYear(DateUtils.parseDateY(date), 1))+"-01";
					}
					
					BasicDBObject where = new BasicDBObject();
					BasicDBList andList = new BasicDBList();
					//1.先从日表中将指定区间的数据查出来{$and:[{regDate:{$gte:'xx'}},{regDate:{$lt:'xx'}}]}
					andList.add(new BasicDBObject("regDate",new BasicDBObject("$gte",stime)));
					andList.add(new BasicDBObject("regDate",new BasicDBObject("$lt",etime)));
					where.append("$and", andList);
					DBCursor dbCursor = mbDao.findAlldata(collectionName, where);
					HashMap<String,DoctorWorkCountVo> map = new HashMap<String, DoctorWorkCountVo>();//相同编号的doctor要想加
					
					//2.相同deptCode的，结果相累加
					while(dbCursor.hasNext()){
						DBObject dbObject = dbCursor.next();
						String doctorCode=dbObject.get("doctorCode").toString();
						int doctorGzlNum=Integer.parseInt(dbObject.get("doctorGzlNum").toString());
						String regDate  =dbObject.get("regDate").toString();
						String doctorName= dbObject.get("doctorName").toString();
						DoctorWorkCountVo doctorWorkCountVo = map.get(doctorCode);
						if(doctorWorkCountVo!=null){
							//只需要让挂号数量相加即可
							doctorWorkCountVo.setDoctorGzlNum(doctorWorkCountVo.getDoctorGzlNum()+doctorGzlNum);
						}else{
							DoctorWorkCountVo vo = new DoctorWorkCountVo();
							vo.setDoctorCode(doctorCode);
							vo.setDoctorGzlNum(doctorGzlNum);
							vo.setRegDate(regDate);
							vo.setDoctorName(doctorName);
							map.put(doctorCode, vo);
						}
						
					}
					
					//3.存入mongodb中
					//遍历map,一条一条存
					Set<Entry<String, DoctorWorkCountVo>> entrySet = map.entrySet();
					for(Entry<String, DoctorWorkCountVo> e:entrySet){
						mongoDataInitDao.init_DocterWorkCount(e.getValue(), type);
					}
					
					if(2==type){
						date1=DateUtils.addMonth(date1, 1);
					}else{
						date1=DateUtils.addYear(date1, 1);
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	
}
