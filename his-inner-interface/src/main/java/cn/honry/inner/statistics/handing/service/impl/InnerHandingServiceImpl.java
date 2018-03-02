package cn.honry.inner.statistics.handing.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.statistics.deptWorkCount.service.UpdateKSGZLTJService;
import cn.honry.inner.statistics.drugAnesthetic.dao.InitDrugAnestheticDao;
import cn.honry.inner.statistics.handing.service.InnerHandingService;
import cn.honry.inner.statistics.hosIncomeCount.service.ZyIncomeService;
import cn.honry.inner.statistics.hospitalday.service.HospitaldayinnerService;
import cn.honry.inner.statistics.inOutPatient.dao.InOutPatientDao;
import cn.honry.inner.statistics.incomeSta.service.IncomeStaService;
import cn.honry.inner.statistics.inneReservation.dao.InnerReserVaDao;
import cn.honry.inner.statistics.inneroperationProportion.dao.InnerOperaPropDao;
import cn.honry.inner.statistics.inpatientStatistics.dao.InitInpatientStatisticsDao;
import cn.honry.inner.statistics.internalCompare1.dao.InnerCompareDao;
import cn.honry.inner.statistics.internalCompare2.dao.InternalCompare2Dao;
import cn.honry.inner.statistics.kidneyDiseaseWithDept.dao.InitKidneyDiseaseWithDeptDao;
import cn.honry.inner.statistics.monthlyDashboard.service.InnerMonthLyService;
import cn.honry.inner.statistics.operationDeptLevel.dao.InnerOperationDeptLevelDao;
import cn.honry.inner.statistics.operationIncome.dao.InnerOperationIncomeDao;
import cn.honry.inner.statistics.operationNum.dao.InnerOperationNumDao;
import cn.honry.inner.statistics.outpatientAntPresDetail.dao.OutpatientAntDao;
import cn.honry.inner.statistics.outpatientDocRecipe.service.InnerOutpatientDocRecipeService;
import cn.honry.inner.statistics.outpatientUseMedic.dao.InitUseMedicDao;
import cn.honry.inner.statistics.peopleNumOfOperation.dao.InnerPeopleNumOfOperationDao;
import cn.honry.inner.statistics.registerInfoGzltj.service.InnerRegisterInfoGzltjService;
import cn.honry.inner.statistics.toListView.service.InnerToListViewService;
import cn.honry.inner.statistics.totalDrugIncome.dao.InnerTotalDrugUsedDao;
import cn.honry.inner.statistics.totalIncomeCount.service.TotalIncomeCountService;
import cn.honry.inner.statistics.totalUndrugIncome.dao.InnerTotalUnDrugDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Service("innerHandingService")
@Transactional
@SuppressWarnings({"all"})
public class InnerHandingServiceImpl implements InnerHandingService {
	@Autowired
	@Qualifier(value = "initDrugAnestheticDao")
	private InitDrugAnestheticDao initDrugAnestheticDao;
	@Autowired
	@Qualifier(value="initKidneyDiseaseWithDeptDao")
	private InitKidneyDiseaseWithDeptDao initKidneyDiseaseWithDeptDao;
	@Autowired
	@Qualifier(value="initUseMedicDao")
	private InitUseMedicDao initUseMedicDao;
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjService")
	private InnerRegisterInfoGzltjService innerRegisterInfoGzltjService;
	@Autowired
	@Qualifier(value = "hospitaldayinnerService")
	private HospitaldayinnerService hospitaldayinnerService;
	@Autowired
	@Qualifier(value="internalCompare2Dao")
	private InternalCompare2Dao internalCompare2Dao;
	@Autowired
	@Qualifier(value="innerMonthLyService")
	private InnerMonthLyService innerMonthLyService;
	@Autowired
	@Qualifier(value="innerPeopleNumOfOperationDao")
	private InnerPeopleNumOfOperationDao innerPeopleNumOfOperationDao;
	@Autowired
	@Qualifier(value="innerTotalDrugUsedDao")
	private InnerTotalDrugUsedDao innerTotalDrugUsedDao;
	@Autowired
	@Qualifier(value="innerTotalUnDrugDao")
	private InnerTotalUnDrugDao innerTotalUnDrugDao;
	@Autowired
	@Qualifier(value="innerOperationDeptLevelDao")
	private InnerOperationDeptLevelDao innerOperationDeptLevelDao;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	@Autowired
	@Qualifier(value="outpatientAntDao")
	private OutpatientAntDao outpatientAntDao;
	@Autowired
	@Qualifier(value = "innerToListViewService")
	private InnerToListViewService innerToListViewService;
	@Autowired
	@Qualifier(value = "innerOperationIncomeDao")
	private InnerOperationIncomeDao innerOperationIncomeDao;
	@Autowired
	@Qualifier(value = "innerOperationNumDao")
	private InnerOperationNumDao innerOperationNumDao;
	@Autowired
	@Qualifier(value = "initInpatientStatisticsDao")
	private InitInpatientStatisticsDao initInpatientStatisticsDao;
	@Autowired
	@Qualifier(value="innerOperaPropDao")
	private InnerOperaPropDao innerOperaPropDao;
	@Resource
	private IncomeStaService incomeStaService;
	@Resource
	private TotalIncomeCountService totalIncomeCountService;//按天统计'总收入情况统计'(ZSRQKTJ)的数据，会级联更新本月，本年的数据的service
	@Autowired
	@Qualifier(value="innerCompareDao")
	private InnerCompareDao innerCompareDao;
	@Resource
	private ZyIncomeService zyIncomeService;//统计'住院收入统计'(ZYSRTJ)的数据，会级联更新本月，本年的数据的service
	
	@Resource
	private UpdateKSGZLTJService updateKSGZLTJService;//定时更新'科室工作量统计'在线表数据，也可以更新历史数据，级联更新当月，当年
	@Resource
	private InnerOutpatientDocRecipeService innerOutpatientDocRecipeService;
	@Autowired
	@Qualifier(value="inOutPatientDao")
	private InOutPatientDao inOutPatientDao;
	@Autowired
	@Qualifier(value="innerReserVaDao")
	private InnerReserVaDao innerReserVaDao;
	/**
	 * 执行预处理方法
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始日期
	 */
	public void handing(String menuAlias,String type,String date){
		if("GHKSGZLTJ".equals(menuAlias)){//挂号科室工作量统计
			innerRegisterInfoGzltjService.init(menuAlias, type, date);
		}
		if("MJZRCTJ".equals(menuAlias)){//门急诊人次统计 
			innerToListViewService.init(menuAlias, type, date);
		}
		if("SRTJB".equals(menuAlias)){//门诊收入统计（原栏目名为：收入统计表）
			incomeStaService.init(menuAlias, type, date);
		}
		if("ZSRQKTJ".equals(menuAlias)){//总收入情况统计
			totalIncomeCountService.init_ZSRQKTJ_dataByDay(menuAlias, type, date);
		}
		if("ZYSRTJ".equals(menuAlias)){//住院收入统计
			zyIncomeService.init_ZYSRTJ_dataByDay(menuAlias, type, date);
		}

		if("KSGZLTJ".equals(menuAlias)){//科室工作量统计
			updateKSGZLTJService.init_KSGZLTJ_ByDay(menuAlias,type,date);
		}

		if("YYNKYXBHNEYXBDBBT".equals(menuAlias)){//医院内科医学部和内二医学部对比表2
			internalCompare2Dao.init_YYNKYXBHNEYXBDBBT(menuAlias, type, date);
		}
		if("MYZHYBP".equals(menuAlias)){//月分析仪表盘
			innerMonthLyService.init_MYZHYBP(menuAlias, type, date);
		}
		if("SSKSSSRSTJ".equals(menuAlias)){//手术科室手术人数
			innerPeopleNumOfOperationDao.init_SSKSSSRSTJ(menuAlias, type, date);
		}
		if("YPSRFXHZ".equals(menuAlias)){//住院药品收入
			innerTotalDrugUsedDao.init_YPSRFXHZ_Dept(menuAlias, type, date);
			innerTotalDrugUsedDao.init_YPSRFXHZ_Doc(menuAlias, type, date);
			innerTotalDrugUsedDao.init_YPSRFXHZ_Total(menuAlias, type, date);
		}
		if("FYPSRFXHZ".equals(menuAlias)){//非药品收入
			innerTotalUnDrugDao.init_FYPSRFXHZ_Dept(menuAlias, type, date);
			innerTotalUnDrugDao.init_FYPSRFXHZ_Doc(menuAlias, type, date);
			innerTotalUnDrugDao.init_FYPSRFXHZ_Total(menuAlias, type, date);
		}
		if("ZYYSGZLTJ".equals(menuAlias)){//住院医生工作量统计
			wordLoadDocDao.init_ZYYSGZLTJ_Detail(menuAlias, type, date);
			wordLoadDocDao.init_ZYYSGZLTJ_Num(menuAlias, type, date);
			wordLoadDocDao.init_ZYYSGZLTJ_Total(menuAlias, type, date);
			wordLoadDocDao.init_ZYSRQK_Dept("ZYSRQK", type, date);
			wordLoadDocDao.init_ZYSRQK_Doc("ZYSRQK", type, date);
			wordLoadDocDao.init_ZYSRQK_MzZy("ZYSRQK", type, date);
		}
		if("MZKJYWCFBL".equals(menuAlias)){//>门诊抗菌药物处方比例在线更新预处理
			outpatientAntDao.init_MZKJYWCFBL(menuAlias, type, date);
		}
		if("SSLSTJ".equals(menuAlias)){//手术例数统计
			innerOperationNumDao.initOperNumsMZJ(menuAlias, type, date);
			innerOperationNumDao.initOperOpTypeToDB();
			innerOperationNumDao.initOperNumsType(menuAlias, type, date);
			innerOperationNumDao.initOperNumsDept(menuAlias, type, date);
			innerOperationNumDao.initOperNumsDoc(menuAlias, type, date);
			innerOperationNumDao.initOperNumsYR(menuAlias, type, date);
		}
		if("SSSRTJ".equals(menuAlias)){//手术收入统计
			innerOperationIncomeDao.initOperIncomeMZ(menuAlias, type, date);
			innerOperationIncomeDao.initOperOpTypeToDB();
			innerOperationIncomeDao.initOperIncomeType(menuAlias, type, date);
			innerOperationIncomeDao.initOperIncomeDept(menuAlias, type, date);
			innerOperationIncomeDao.initOperIncomeDoc(menuAlias, type, date);
			innerOperationIncomeDao.initOperIncomeYoyRatio(menuAlias, type, date);
		}
		if("ZYRSTJ".equals(menuAlias)){//住院人数统计
			try {
				initInpatientStatisticsDao.init_ZYRSTJ(menuAlias, type, date);
				initInpatientStatisticsDao.inHostNumber(menuAlias, type, date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if("SSKSSSFJTJ".equals(menuAlias)){//手术科室手术分级统计
			innerOperationDeptLevelDao.initOperationDeptLeve(menuAlias, type, date);
		}
		if("MZYSKDGZL".equals(menuAlias)){//门诊医生开单工作量
			innerOutpatientDocRecipeService.init_MZYSKDGZL(menuAlias,type, date);
		}
		if("DRJYSJ".equals(menuAlias)){//当天运营数据
			hospitaldayinnerService.init_YYMRHZ(menuAlias,date, type);
		}
		if("ZCYRCTJ".equals(menuAlias)){
			inOutPatientDao.initZCYRCTJ(menuAlias, type, date);
		}
		if("YYNKYXBHNEYXBDBBO".equals(menuAlias)){//医院内科医学部和内二医学部对比表1
			innerCompareDao.initCompare(menuAlias, type, date);
		}
		if("YYTJ".equals(menuAlias)){
			innerReserVaDao.initReserVation(menuAlias, type, date);
		}
		if("MZYYJK".equals(menuAlias)){//门诊用药监控
			initUseMedicDao.init_MZYYJK_YZB(menuAlias, type, date);
			initUseMedicDao.init_MZYYJK_YYTS(menuAlias, type, date);
			initUseMedicDao.init_MZYYJK_KSYYJE(menuAlias, type, date);
			initUseMedicDao.init_MZYYJK_YSYYJE(menuAlias, type, date);
			initUseMedicDao.init_MZYYJK_YPJE(menuAlias, type, date);
		}
		if("SBKSDBB".equals(menuAlias)){//科室对比表
			initKidneyDiseaseWithDeptDao.init_SBKSDBB(menuAlias, type, date);
		}
		if("BQSHZBTJ".equals(menuAlias)){//手术占比统计
			innerOperaPropDao.init_OperaProption(menuAlias, type, date);
		}
		if("MZJSYPTJ".equals(menuAlias)){//麻醉精神药品统计
			initDrugAnestheticDao.init_MZJSYPTJ_DAY(menuAlias, type, date);
		}
		if("YSGZLTJ".equals(menuAlias)){//pc端门诊医生工作量
			updateKSGZLTJService.initPCdoctorWorkTotal(menuAlias, type, date);
		}
	}
}
