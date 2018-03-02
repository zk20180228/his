package cn.honry.outpatient.advice.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.DrugSpedrug;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.frequency.dao.FrequencyInInterDAO;
import cn.honry.inner.baseinfo.hospital.dao.HospitalInInterDAO;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.inner.drug.drugSpedrug.dao.SpedrugInInterDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.drug.undrugZtinfo.dao.UndrugZtinfoInInterDAO;
import cn.honry.inner.patient.idcard.dao.IdcardInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.technical.terminalApply.dao.TerminalApplyInInterDAO;
import cn.honry.inner.vo.ComboGroupVo;
import cn.honry.inner.vo.StatVo;
import cn.honry.outpatient.advice.dao.AdviceDAO;
import cn.honry.outpatient.advice.service.AdviceService;
import cn.honry.outpatient.advice.vo.AdviceVo;
import cn.honry.outpatient.advice.vo.DrugAndUnDrugVo;
import cn.honry.outpatient.advice.vo.InpatientInfoVo;
import cn.honry.outpatient.advice.vo.InspectionReportList;
import cn.honry.outpatient.advice.vo.IreportPatientVo;
import cn.honry.outpatient.advice.vo.KeyValueVo;
import cn.honry.outpatient.advice.vo.LisVo;
import cn.honry.outpatient.advice.vo.OdditionalitemAndUnDrugVo;
import cn.honry.outpatient.advice.vo.OutpatientVo;
import cn.honry.outpatient.advice.vo.PatientVo;
import cn.honry.outpatient.advice.vo.RecipelInfoVo;
import cn.honry.outpatient.advice.vo.RecipelStatVo;
import cn.honry.outpatient.advice.vo.RegisterMainVo;
import cn.honry.outpatient.advice.vo.StackVo;
import cn.honry.outpatient.advice.vo.ViewInfoVo;
import cn.honry.outpatient.feedetail.dao.FeedetailDAO;
import cn.honry.outpatient.medicalrecord.dao.MedicalrecordDAO;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**  
 *  
 * 门诊医嘱  ServiceImpl
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("adviceService")
@Transactional
@SuppressWarnings({ "all" })
public class AdviceServiceImpl implements AdviceService{

	@Autowired
	@Qualifier(value = "adviceDAO")
	private AdviceDAO adviceDAO;//医嘱
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;//科室
	@Autowired
	@Qualifier(value = "frequencyInInterDAO")
	private FrequencyInInterDAO frequencyInInterDAO;//频次
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;//编码
	@Autowired
	@Qualifier(value = "idcardInInterDAO")
	private IdcardInInterDAO idcardInInterDAO;//就诊卡
	@Autowired
	@Qualifier(value = "ationDAO")
	private RegistrationDAO ationDAO;//挂号
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;//系统参数
	@Autowired
	@Qualifier(value = "feedetailDAO")
	private FeedetailDAO feedetailDAO;//门诊收费
	@Autowired
	@Qualifier(value = "terminalApplyInInterDAO")
	private TerminalApplyInInterDAO terminalApplyInInterDAO;//医技
	@Autowired
	@Qualifier(value = "undrugInInterDAO")
	private UndrugInInterDAO undrugInInterDAO;//非药品
	@Autowired
	@Qualifier(value = "userInInterDAO")
	private UserInInterDAO userInInterDAO;//用户
	@Autowired
	@Qualifier(value = "stackInInterDAO")
	private StackInInterDAO stackInInterDAO;//组套
	@Autowired
	@Qualifier(value = "stackinfoInInterDAO")
	private StackinfoInInterDAO stackinfoInInterDAO;//组套明细
	@Autowired
	@Qualifier(value = "medicalrecordDAO")
	private MedicalrecordDAO medicalrecordDAO;//病历
	@Autowired
	@Qualifier(value = "undrugZtinfoInInterDAO")
	private UndrugZtinfoInInterDAO undrugZtinfoInInterDAO;//复合项目
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;//编码
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;//编码
	@Autowired
	@Qualifier(value = "spedrugInInterDAO")
	private SpedrugInInterDAO spedrugInInterDAO;//特限药
	@Autowired
	@Qualifier(value = "hospitalInInterDAO")
	private HospitalInInterDAO hospitalInInterDAO;//医院

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public OutpatientRecipedetailNow get(String id) {
		return adviceDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OutpatientRecipedetailNow entity) {
		
	}
	
	/**  
	 *  
	 * 获得信息树-异步查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	@Override
	public String queryAdviceTree(String type,String id) {
		List<TreeJson> list = new ArrayList<TreeJson>();
		if(StringUtils.isNotBlank(type)){
			if(StringUtils.isBlank(id)){
				TreeJson pTree = new TreeJson();
				pTree.setId(type);
				pTree.setText("1".equals(type)?"待诊患者信息":"已诊患者信息");
				pTree.setState("open");
				pTree.setIconCls("icon-table");
				List<KeyValueVo> voList = adviceDAO.queryAdviceTreeForPatient(type);
				if(voList!=null && voList.size()>0){
					List<TreeJson> cList = new ArrayList<TreeJson>();
					TreeJson cTree = null;
					Map<String,String> cMap = null;
					for(KeyValueVo vo : voList){
						cTree = new TreeJson();
						cTree.setId(vo.getKey());
						cTree.setText(vo.getValue());
						cTree.setState("closed");
						cMap = new HashMap<String, String>();
						cMap.put("rank","1");
						cMap.put("patientNo",vo.getParam());
						cTree.setAttributes(cMap);
						cList.add(cTree);
					}
					pTree.setChildren(cList);
				}
				list.add(pTree);
			}else{
				List<KeyValueVo> voList = adviceDAO.queryAdviceTreeForRegister(type,id);
				if(voList!=null && voList.size()>0){
					TreeJson cTree = null;
					Map<String,String> cMap = null;
					for(KeyValueVo vo : voList){
						cTree = new TreeJson();
						cTree.setId(vo.getKey());
						cTree.setText(vo.getValue());
						if(vo.getSex()==1){
							cTree.setIconCls("icon-user_b");
						}else if(vo.getSex()==2){
							cTree.setIconCls("icon-user_female");
						}else{
							cTree.setIconCls("icon-user_gray");
						}
						cMap = new HashMap<String, String>();
						cMap.put("rank","2");
						cMap.put("patientNo",vo.getParam());
						cTree.setAttributes(cMap);
						list.add(cTree);
					}
				}
			}
		}
		return JSONUtils.toJson(list);
	}

	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:idCardNo就诊卡号type类型1待诊2已诊
	 *
	 */
	@Override
	public List<PatientVo> queryPatientByidCardNo(String idCardNo,String type) {
		return adviceDAO.queryPatientByidCardNo(idCardNo,type);
	}

	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:clinicNo门诊号
	 *
	 */
	@Override
	public PatientVo queryPatientByclinicNo(String clinicNo) {
		return adviceDAO.queryPatientByclinicNo(clinicNo);
	}

	/**  
	 *  
	 * @Description：  获得药房信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午07:02:50  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午07:02:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysDepartment getPharmacyInfoById(String pharmacyId) {
		return deptInInterDAO.getByCode(pharmacyId);
	}
	
	/**  
	 *  
	 * @Description：  获得医嘱项目信息-查询信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<ViewInfoVo> getViewInfoPage(String page,String rows, ViewInfoVo vo) {
		return adviceDAO.getViewInfoPage(page,rows,vo);
	}
	

	/**  
	 *  
	 * @Description：  获得医嘱项目信息-统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getViewInfoTotal(ViewInfoVo vo) {
		return adviceDAO.getViewInfoTotal(vo);
	}

	/**  
	 *  
	 * @Description：  获得渲染数据-频次
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessFrequency> queryColorInfoFrequencyList() {
		return frequencyInInterDAO.queryFrequencyList();
	}
	
	/**  
	 *  
	 * @Description：  获得渲染数据-执行科室
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> queryColorInfoExeDeptList() {
		List<SysDepartment> list = deptInInterService.queryAllDept();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 *  
	 * @Description：  获得医生职级和药品等级对照关系key药品等级编码value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryJudgeDocDrugGradeMap() {
		return adviceDAO.queryJudgeDocDrugGradeMap();
	}
	
	/**  
	 *  
	 * @Description：  获得药品等级key药品等级id value等级编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryJudgeDrugGradeMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<BusinessDictionary> list = innerCodeDao.getDictionary("drugGrade");
		if(list!=null&&list.size()>0){
			for(BusinessDictionary grade : list){
				map.put(grade.getEncode(), grade.getEncode());
			}
		}
		return map;
	}
	
	/**  
	 *  
	 * @Description：  获得全部药品等级key药品等级id value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryJudgeDrugGradeAllMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<BusinessDictionary> list = innerCodeDao.getDictionary(HisParameters.DRUGGRADE);
		if(list!=null&&list.size()>0){
			for(BusinessDictionary grade : list){
				map.put(grade.getEncode(), grade.getName());
			}
		}
		return map;
	}

	/**  
	 *  
	 * @Description：  通过病历号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午02:56:34  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午02:56:34  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientIdcard getIdcardByPatientNo(String patientNo) {
		return idcardInInterDAO.getIdcardByPatientNo(patientNo);
	}
	
	/**  
	 *  
	 * @Description：  根据病历号和看诊序号查询当天的挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:53:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:53:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegistrationNow getRegisterInfoByPatientNoAndNo(String patientNo,String no) {
		return ationDAO.getRegisterInfoByPatientNoAndNo(patientNo,no);
	}
	
	/**  
	 *  
	 * @Description：  保存/修改医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:17:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 *
	 */
	@Override
	public Map<String,Object> savaAdviceInfo(List<AdviceVo> voList,String patientNo,PatientIdcard idcard,RegistrationNow registration) {
		Map<String,Object> map=new HashMap<String,Object>();//返回提示的Map
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//当前用户
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//当前科室
		SysDepartment userDept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();//所属科室
		//获得医嘱分组及保存参数
		Map<String,Object> paraMap = getAdNoStrAndIsSysStrAndIsGroStr();
		boolean isSys = (Boolean) paraMap.get("isSys");//是否按照系统类别拆分
		boolean isGro = (Boolean) paraMap.get("isGro");//是否优先处理组合医嘱
		/**需求变动 医嘱项目最大数量改为打印时最大数目，不做为生成处方号时的限制条件 2017-02-22 17:45**/
//		int adNo = (Integer) paraMap.get("adNo");//医嘱项目最大数量
		//对前端医嘱信息进行排序和分组
		Map<String,List<AdviceVo>> voMap = classifyForList(isSys,isGro,voList);
		//医嘱保存的逻辑处理
		if(voMap.size()>0){
			//获得医生职级对应的药品等级
			Map<String, String> docDrugGradeMap = adviceDAO.queryJudgeDocDrugGradeMap();
			//获得药品等级编码key药品等级id value等级编码
			Map<String, String> drugGradeCodeMap = queryJudgeDrugGradeMap();
			//获得全部药品等级key药品等级id value等级名称
			Map<String, String> drugGradeNameMap = queryJudgeDrugGradeAllMap();
			//获得收费序列
			String adviceChargeSeq = adviceDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ");
			//获得系统时间
			Date date = new Date();
			//获取系统类别
			Map<String,String> sysTypeMap = innerCodeDao.getBusDictionaryMap("systemType");
			//获取最小费用名称
			Map<String,String> miniMap = innerCodeDao.getBusDictionaryMap("drugMinimumcost");
			//获取剂量单位
			Map<String,String> doseMap = innerCodeDao.getBusDictionaryMap("doseUnit");
			//获取剂型名称
			Map<String,String> dosageMap = innerCodeDao.getBusDictionaryMap("dosageForm");
			//对排序和分组好的医嘱信息进行分装用以保存
			for(Map.Entry<String,List<AdviceVo>> m : voMap.entrySet()){
				Integer index = 0;//辅助用于计算处方内流水号
				OutpatientFeedetailNow feedc = null;
				OutpatientRecipedetailNow recic = null;
				boolean isHaveChimed = false;
				for(AdviceVo vo : m.getValue()){
					index++;
					DrugAndUnDrugVo drugAndUnDrugVo = adviceDAO.finfDrugAndUnDrugById(vo.getAdviceId(),vo.getMinusDeptHid());//获得药品或非药品信息
					boolean isChimed = false;//是否为草药
					if(drugAndUnDrugVo.getTy()==1&&HisParameters.HERBSID.equals(drugAndUnDrugVo.getType())){//为草药
						isChimed = true;
						if(!isHaveChimed){
							isHaveChimed = true;
						}
					}
					boolean subtblFlag = false;//是否有附材
					//获得附材信息
					List<OdditionalitemAndUnDrugVo> oddList = adviceDAO.findOdditionalitem(drugAndUnDrugVo.getTy(),vo.getUsageNameHid(),vo.getAdviceId(),dept.getDeptCode());
					if(oddList!=null&&oddList.size()>0){//存在附材
						subtblFlag = true;
					}
					//是否终端确认或预约
					boolean applyFlg = false;
					if((drugAndUnDrugVo.getIsSubmit()!=null&&drugAndUnDrugVo.getIsSubmit()==1)||(drugAndUnDrugVo.getIsMake()!=null&&drugAndUnDrugVo.getIsMake()==1)){
						applyFlg = true;
					}
					/**业务变更  暂时不处理儿童价(刘) 2017-03-01 aizhonghua**/
					//判断是否是儿童
//					boolean isChil = calAge(registration.getPatientBirthday());
					
					//计算价格
					Double payCost = drugAndUnDrugVo.getPrice();//默认价格
//					if(isChil){
//						payCost = drugAndUnDrugVo.getPriceChil();//儿童价
//					}
					if(vo.getTotalNum()==null){
						vo.setTotalNum(1d);
					}
					payCost = payCost*vo.getTotalNum();
					if(vo.getTy()==1&&vo.getAdMinUnitHid().equals(vo.getTotalUnitHidJudge())){//如果为药品且开立单位为最小单位则价格需要除以包装数量
						payCost = payCost/(vo.getPackagingnum()==null?1:vo.getPackagingnum());
						vo.setAdPrice(vo.getAdPrice()/(vo.getPackagingnum()==null?1:vo.getPackagingnum()));
					}
					
					if(StringUtils.isBlank(vo.getId())){//对于新增医嘱执行的方法
						Map<String,Object> checkMap = checkAdviceVo(drugAndUnDrugVo,vo,docDrugGradeMap,drugGradeCodeMap,drugGradeNameMap);
						if(checkMap!=null){//该药品不合法，无法添加
							return checkMap;
						}
						//对单条医嘱信息进行封装
						OutpatientRecipedetailNow recipedetail = packRecip(vo,m.getKey(),payCost,registration,patientNo,drugAndUnDrugVo,user,dept,isChimed,subtblFlag,applyFlg,adviceChargeSeq,index,date,userDept,sysTypeMap,miniMap,dosageMap,doseMap);//封装医嘱对象
						recic = recipedetail;
						adviceDAO.save(recipedetail);//保存医嘱对象
						//对医嘱信息对应的收费信息进行封装
						if(drugAndUnDrugVo.getTy()==0&&drugAndUnDrugVo.getIsStack()!=null&&drugAndUnDrugVo.getIsStack()==1){
							//查询复合项目
							List<DrugUndrug> zTinfoList = undrugZtinfoInInterDAO.getUndrugZtinfoByPackageCode(drugAndUnDrugVo.getCode());
							List<OutpatientFeedetailNow> feedetailList = packFeedList(zTinfoList,vo,payCost,recipedetail,registration,adviceChargeSeq,user,dept,isChimed,userDept);
							feedetailDAO.saveOrUpdateList(feedetailList);
						}else{
							OutpatientFeedetailNow feedetail = packFeed(vo,payCost,recipedetail,registration,adviceChargeSeq,user,dept,isChimed,userDept);//封装附材对象
							feedc = feedetail;
							feedetailDAO.save(feedetail);//保存附材对象
						}
						if(subtblFlag){//如果存在附材
							List<OutpatientFeedetailNow> feedeList = packFeedSubtbl(oddList,recipedetail,registration,user,dept,date,userDept,adviceChargeSeq,vo.getExecutiveDeptHid());//封装附材集合
							feedetailDAO.saveOrUpdateList(feedeList);//保存附材对象
						}
//						if(applyFlg){//是否终端确认
//							TecTerminalApply apply = packApply(recipedetail,registration,deptId,userId,idcard,date);//封装医技终端确认申请对象
//							adviceDAO.save(apply);//保存医技终端确认申请
//						}
					}else{//对于修改医嘱执行的方法
						OutpatientRecipedetailNow reOdd = adviceDAO.get(vo.getId());
						Map<String,Object> checkMap = checkAdviceVoOdd(reOdd);//判断医嘱是否可以进行修改
						if(checkMap!=null){//该药品不合法，无法修改
							if("no".equals(checkMap.get("resInfo"))){
								continue;
							}else{
								return checkMap;
							}
						}
						//
						Double oldQty = new Double(reOdd.getQty());
						//对单条医嘱信息进行修改封装
						OutpatientRecipedetailNow recipedetail = packRecipUp(vo,reOdd,payCost);
						recic = recipedetail;
						adviceDAO.save(recipedetail);//保存修改后的医嘱对象
						//对医嘱信息对应的收费信息进行修改封装
						if(drugAndUnDrugVo.getTy()==0&&drugAndUnDrugVo.getIsStack()!=null&&drugAndUnDrugVo.getIsStack()==1){//对于复合项目
							List<OutpatientFeedetailNow> feeLdist = feedetailDAO.getgFeeListByRecipeAndSequence(recipedetail.getItemCode(),recipedetail.getRecipeNo(),recipedetail.getSequencenNo(),recipedetail.getRecipeSeq(),recipedetail.getClinicCode());
							if(feeLdist!=null){
								for(OutpatientFeedetailNow fee : feeLdist){
									Map<String,Object> checkOddFeeMap = checkAdviceVoOddFee(fee);//判断医嘱是否可以进行修改
									if(checkMap!=null){//该药品不合法，无法修改
										if("no".equals(checkMap.get("resInfo"))){
											continue;
										}else{
											return checkMap;
										}
									}
									OutpatientFeedetailNow feedetail = packFeedListUp(vo,fee,payCost,recipedetail);
									feedetailDAO.save(feedetail);//保存修改后的附材对象
								}
							}
						}else{
							OutpatientFeedetailNow feeOdd = feedetailDAO.getFeeByRecipeAndSequence(recipedetail.getRecipeNo(),recipedetail.getSequencenNo(),recipedetail.getRecipeSeq(),recipedetail.getClinicCode());
							Map<String,Object> checkOddFeeMap = checkAdviceVoOddFee(feeOdd);//判断医嘱是否可以进行修改
							if(checkMap!=null){//该药品不合法，无法修改
								if("no".equals(checkMap.get("resInfo"))){
									continue;
								}else{
									return checkMap;
								}
							}
							OutpatientFeedetailNow feedetail = packFeedUp(vo,feeOdd,payCost,recipedetail);
							feedc = feedetail;
							feedetailDAO.save(feedetail);//保存修改后的附材对象
							if(subtblFlag){//如果存在附材则需要对原有附材对象进行修改
								List<OutpatientFeedetailNow> feedeOddList = feedetailDAO.getFeeListByRecipeNoAndFeeId(feedetail.getRecipeNo(),recipedetail.getId());//原有附材信息
								if(feedeOddList==null || feedeOddList.size()==0){
									map.put("resMsg", "error");
									map.put("resCode", "医嘱修改失败，医嘱附材信息查询失败，无法修改！");
									return map;
								}
								for(OutpatientFeedetailNow oddVo : feedeOddList){
									if(oddVo==null){
										map.put("resMsg", "error");
										map.put("resCode", "医嘱修改失败，医嘱附材信息查询失败，无法修改！");
										return map;
									}
									if(oddVo.getPayFlag()==1){
										continue;
									}
									OutpatientFeedetailNow feedetailSubtbl = packFeedSubtblUp(vo,oddVo,oldQty,recipedetail);
									feedetailDAO.save(feedetailSubtbl);
								}
							}
						}
//						if(applyFlg){//如果需要终端确认则需要对原有终端确认信息进行修改
//							TecTerminalApply applyOdd = terminalApplyInInterDAO.getApplyApplyNo(recipedetail.getApplyNo());//原有医技信息
//							if(applyOdd==null){//原有医技信息为空需要从新插入医技信息
//								TecTerminalApply apply = packApply(recipedetail,registration,deptId,userId,idcard,date);//封装医技终端确认申请对象
//								terminalApplyInInterDAO.save(apply);//保存医技终端确认申请
//							}else{//原有医技信息不为空需要修改现有医技信息
//								if(applyOdd.getPayFlag().equals("1")){
//									continue;
//								}
//								TecTerminalApply apply = packApplyUp(vo,applyOdd);
//								terminalApplyInInterDAO.save(apply);//保存医技终端确认申请
//							}
//						}
					}
				}
				if(isHaveChimed&&feedc!=null){//如果为草药则需要对原有煎药信息进行修改
					OutpatientFeedetailNow feedeOdd = feedetailDAO.getFeeChimedByRecipeNo(feedc.getRecipeNo(),feedc.getCombNo());//原有煎药信息
					if(feedeOdd==null){//没有煎药信息需要从新插入煎药信息
						DrugUndrug unDrug = undrugInInterDAO.getUnDrugByName(m.getValue().get(0).getRemark());//获得煎药方式
						if(unDrug!=null){//如果煎药方式不为空则需要添加煎药附材
							OutpatientFeedetailNow feedIsChimed = packFeedisChimed(m.getValue().get(0),unDrug,recic,registration,user,dept,date,userDept,adviceChargeSeq,m.getValue().get(0).getExecutiveDeptHid());//封装煎药附材对象
							feedetailDAO.save(feedIsChimed);//保存煎药附材
						}
					}else{//有煎药信息需要修改原有煎药信息
						if(feedeOdd.getPayFlag()==1){
							continue;
						}
						DrugUndrug unDrug = undrugInInterDAO.getUnDrugByName(m.getValue().get(0).getRemark());//获得煎药方式
						if(unDrug!=null){
							OutpatientFeedetailNow IsChimed = packFeedisChimedUp(m.getValue().get(0),feedeOdd,unDrug);
							feedetailDAO.save(IsChimed);//保存修改后的煎药附材
						}
					}
				}
			}
			OperationUtils.getInstance().conserve(null,"门诊看诊","INSERT INTO","T_OUTPATIENT_RECIPEDETAIL",OperationUtils.LOGACTIONCLINICDOC);
			OperationUtils.getInstance().conserve(null,"门诊医技终端申请","INSERT INTO","T_OUTPATIENT_RECIPEDETAIL",OperationUtils.LOGACTIONCLINICDOC);
			map.put("resMsg", "success");
			map.put("resCode", "医嘱保存成功，共"+voList.size()+"条医嘱信息！");
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "医嘱信息有误，重新填写！");
		}
		return map;
	}
	
	private List<OutpatientFeedetailNow> packFeedList(List<DrugUndrug> zTinfoList, AdviceVo vo, Double payCost,OutpatientRecipedetailNow recipedetail, RegistrationNow registration,String adviceChargeSeq, User user, SysDepartment dept,boolean isChimed, SysDepartment userDept) {
		List<OutpatientFeedetailNow> feeList = new ArrayList<OutpatientFeedetailNow>();
		OutpatientFeedetailNow feedetail = null;
		for(DrugUndrug ztinfo : zTinfoList){
			feedetail = new OutpatientFeedetailNow();
			feedetail.setRecipeNo(recipedetail.getRecipeNo());//处方号
			feedetail.setSequenceNo(recipedetail.getRecipeSeq());//处方内项目流水号[4]
			feedetail.setTransType(1);//交易类型,1正交易，2反交易[5]
			feedetail.setCardNo(registration.getCardNo());//就诊卡号
			feedetail.setRegDate(recipedetail.getRegDate());//挂号日期
			feedetail.setRegDpcd(dept.getDeptCode());//开单科室[9]
			feedetail.setDoctCode(user.getAccount());//开方医师[10]
			feedetail.setDoctDept(userDept.getDeptCode());//开方医师所在科室[11]
			feedetail.setItemCode(ztinfo.getCode());//项目代码[12]
			feedetail.setItemName(ztinfo.getName());//项目名称[13]
			feedetail.setDrugFlag("0");//1药品/0非药[14]
			feedetail.setSpecs(vo.getSpecs());//规格[15]
			feedetail.setSelfMade(vo.getIsmanufacture());//自制药标志
			feedetail.setDrugQuality(vo.getNature());//药品性质
			feedetail.setDoseModelCode(vo.getDosageform());//剂型[18]
			feedetail.setFeeCode(ztinfo.getUndrugMinimumcost());//最小费用代码[19]
			feedetail.setClassCode(ztinfo.getUndrugSystype());//系统类别[20]
			feedetail.setUnitPrice(ztinfo.getDefaultprice());//单价[21]
			feedetail.setQty(vo.getTotalNum());//数量[22]
			feedetail.setConfirmNum(0d);//已确认数量
			if(StringUtils.isNotBlank(vo.getDrugType())&&HisParameters.HERBSID.equals(vo.getDrugType())){
				feedetail.setDays(vo.getSetNum());//草药的付数，其他药品为1[23]
			}else{
				feedetail.setDays(1);//草药的付数，其他药品为1[23]
			}
			feedetail.setFrequencyCode(vo.getFrequencyHid());//频次代码[24]
			feedetail.setUsageCode(vo.getUsageNameHid());//用法代码[25]
			feedetail.setUseName(vo.getUsageName());//用法名称[26]
			feedetail.setInjectNumber(vo.getInjectionNum());//院内注射次数[27]
			feedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记:1普通/2加急[28]
			feedetail.setLabType(vo.getSampleTeptHid());//样本类型[29]
			feedetail.setCheckBody(vo.getInspectPartId());//检体[30]
			feedetail.setDoseOnce(vo.getDosageMin());//每次用量[31]
			feedetail.setDoseUnit(vo.getAdDosaUnitHid());//每次用量单位
			feedetail.setBaseDose(vo.getAdDrugBasiHid());//基本剂量[33]
			feedetail.setPackQty(vo.getPackagingnum());//包装数量[34]
			feedetail.setPriceUnit(recipedetail.getItemUnit());//计价单位[35]
			feedetail.setTotCost((vo.getTotalNum()==null?1:vo.getTotalNum())*ztinfo.getDefaultprice());//现金金额[38]
			feedetail.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码[39]更改为发放药房
			feedetail.setExecDpnm(vo.getExecutiveDept());//执行科室名称[40]更改为发放药房
			feedetail.setMainDrug(recipedetail.getDrugFlag());//主药标志[43]
			feedetail.setCombNo(recipedetail.getCombNo());//组合号[44]
			feedetail.setOperCode(user.getAccount());//划价人[45]
			feedetail.setOperDate(recipedetail.getCreateTime());//划价时间[46]
			feedetail.setPayFlag(vo.getAuditing()==1?4:0);//0划价 1收费 3预收费团体体检 4 药品预审核
			feedetail.setCancelFlag(1);//0退费，1正常，2重打，3注销[48]
			feedetail.setConfirmFlag(0);//0未确认/1确认[54]
			feedetail.setExtFlag(0);//扩展标志 特殊项目标志 1 0 非
			feedetail.setExtFlag1(0);//0 正常/1个人体检/2 集体体检
			feedetail.setExtFlag2(0);//日结标志：0：未日结/1：已日结
			feedetail.setExtFlag3(recipedetail.getMinunitFlag());//开立单位1 包装单位0是最小单位
			feedetail.setMoOrder(recipedetail.getSequencenNo());//医嘱项目流水号或者体检项目流水号
			feedetail.setRecipeSeq(adviceChargeSeq);//收费序列
			feedetail.setCostSource(0);//费用来源 0 操作员 1 医嘱 2 终端 3 体检
			feedetail.setSubjobFlag(0);//附材标志
			feedetail.setAccountFlag(0);//0没有扣账户 1 已经扣账户
			feedetail.setUseFlag(0);//0 未出结果 1 已出结果
			feedetail.setClinicCode(recipedetail.getClinicCode());//门诊号
			feedetail.setPatientNo(recipedetail.getPatientNo());;//病历号
			feedetail.setExtendTwo(recipedetail.getId());//扩展标记二，处方id
			feedetail.setCreateUser(recipedetail.getCreateUser());
			feedetail.setCreateDept(recipedetail.getCreateDept());
			feedetail.setCreateTime(recipedetail.getCreateTime());
			feedetail.setPackageCode(vo.getAdviceId());
			feedetail.setPackageName(vo.getAdviceName());
			feedetail.setRegDpcdname(dept.getDeptName());//开单科室名称
			feedetail.setDoctCodename(user.getName());;//开方医师姓名
			feedetail.setDoctDeptname(userDept.getDeptName());//开方医师所在科室名称
			feedetail.setFrequencyName(recipedetail.getFrequencyName());//频次名称
			feedetail.setOperName(user.getName());//划价人姓名
			feedetail.setSampleId(adviceDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER"));
			feeList.add(feedetail);
		}
		return feeList;
	}

	/**  
	 *  
	 * @Description：  获得医嘱分组及保存参数
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:17:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 *
	 */
	public Map<String,Object> getAdNoStrAndIsSysStrAndIsGroStr(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("adNo", 5);
		map.put("isSys", false);
		map.put("isGro", false);
		//获得门诊医嘱最大项目数量
		String adNoStr = parameterInnerDAO.getParameterByCode("clinicAdviceNo");
		if(StringUtils.isNotBlank(adNoStr)){
			map.put("adNo",Integer.parseInt(adNoStr));
		}
		//获得门诊医嘱是否按照系统类别拆分
		String isSysStr = parameterInnerDAO.getParameterByCode("clinicAdviceIsSystemType");
		if(StringUtils.isNotBlank(isSysStr)){
			if(Integer.parseInt(isSysStr)==1){
				map.put("isSys", true);
			}
		}
		//门诊医嘱是否优先处理组合医嘱
		String isGroStr = parameterInnerDAO.getParameterByCode("clinicAdviceIsFirstGroup");
		if(StringUtils.isNotBlank(isGroStr)){
			if(Integer.parseInt(isGroStr)==1){
				map.put("isGro", true);
			}
		}
		return map;
	}
	
	/**  
	 *  
	 * @Description：  对前端医嘱信息进行排序
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:17:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 *
	 */
	public Map<String,List<AdviceVo>> classifyForList(boolean isSys ,boolean isGro,List<AdviceVo> voList){
		Map<String,List<AdviceVo>> voMap = new HashMap<String,List<AdviceVo>>();//用于存放临时医嘱信息的Map
		List<AdviceVo> voMapList = null;//要分组的医嘱信息
		int dataOrder = 0;
		if(isSys){//按照系统类别拆分
			if(isGro){//优先处理组合
				//System.err.println("按照系统类别拆分,优先处理组合");
				for(AdviceVo vo : voList){//执行科室及服数相同的信息分为一组
					dataOrder++;
					vo.setDataOrder(dataOrder);
					if(voMap.get(vo.getSysType()+vo.getExecutiveDeptHid()+(StringUtils.isBlank(vo.getGroup())?"0":vo.getGroup())+(vo.getSetNum()==null?0:vo.getSetNum()))==null){//原map中没有信息需要新增
						voMapList = new ArrayList<AdviceVo>();
						voMapList.add(vo);
						voMap.put(vo.getSysType()+vo.getExecutiveDeptHid()+(StringUtils.isBlank(vo.getGroup())?"0":vo.getGroup())+(vo.getSetNum()==null?0:vo.getSetNum()),voMapList);
					}else{//原map中有信息需要追加
						voMap.get(vo.getSysType()+vo.getExecutiveDeptHid()+(StringUtils.isBlank(vo.getGroup())?"0":vo.getGroup())+(vo.getSetNum()==null?0:vo.getSetNum())).add(vo);
					}
				}
			}else{//不优先处理组合
				//System.err.println("按照系统类别拆分,不优先处理组合");
				for(AdviceVo vo : voList){//执行科室及服数相同的信息分为一组
					dataOrder++;
					vo.setDataOrder(dataOrder);
					if(voMap.get(vo.getSysType()+vo.getExecutiveDeptHid()+(vo.getSetNum()==null?0:vo.getSetNum()))==null){//原map中没有信息需要新增
						voMapList = new ArrayList<AdviceVo>();
						voMapList.add(vo);
						voMap.put(vo.getSysType()+vo.getExecutiveDeptHid()+(vo.getSetNum()==null?0:vo.getSetNum()),voMapList);
					}else{//原map中有信息需要追加
						voMap.get(vo.getSysType()+vo.getExecutiveDeptHid()+(vo.getSetNum()==null?0:vo.getSetNum())).add(vo);
					}
				}
			}
		}else{//不按照系统类别拆分
			if(isGro){//优先处理组合
				//System.err.println("不按照系统类别拆分,优先处理组合");
				for(AdviceVo vo : voList){//执行科室及服数相同的信息分为一组
					dataOrder++;
					vo.setDataOrder(dataOrder);
					if(voMap.get(vo.getExecutiveDeptHid()+(StringUtils.isBlank(vo.getGroup())?"0":vo.getGroup())+(vo.getSetNum()==null?0:vo.getSetNum()))==null){//原map中没有信息需要新增
						voMapList = new ArrayList<AdviceVo>();
						voMapList.add(vo);
						voMap.put(vo.getExecutiveDeptHid()+(StringUtils.isBlank(vo.getGroup())?"0":vo.getGroup())+(vo.getSetNum()==null?0:vo.getSetNum()),voMapList);
					}else{//原map中有信息需要追加
						voMap.get(vo.getExecutiveDeptHid()+(StringUtils.isBlank(vo.getGroup())?"0":vo.getGroup())+(vo.getSetNum()==null?0:vo.getSetNum())).add(vo);
					}
				}
			}else{//不优先处理组合
				//System.err.println("不按照系统类别拆分,不优先处理组合");
				for(AdviceVo vo : voList){//执行科室及服数相同的信息分为一组
					dataOrder++;
					vo.setDataOrder(dataOrder);
					if(voMap.get(vo.getExecutiveDeptHid()+(vo.getSetNum()==null?0:vo.getSetNum()))==null){//原map中没有信息需要新增
						voMapList = new ArrayList<AdviceVo>();
						voMapList.add(vo);
						voMap.put(vo.getExecutiveDeptHid()+(vo.getSetNum()==null?0:vo.getSetNum()),voMapList);
					}else{//原map中有信息需要追加
						voMap.get(vo.getExecutiveDeptHid()+(vo.getSetNum()==null?0:vo.getSetNum())).add(vo);
					}
				}
			}
		}
		Map<String,List<AdviceVo>> retVoMap = new HashMap<String,List<AdviceVo>>();//用于存放保存医嘱信息的Map
		if(voMap.size()>0){//如果医嘱信息不为空，则对其进行分组
			List<AdviceVo> tempList = null;//组对象
			for(Map.Entry<String,List<AdviceVo>> m : voMap.entrySet()){
				String group = null;//记录组合号（需要入库的组合号）
				String voGroup = null;//记录组合号（用来对比的组合号）
				tempList = new ArrayList<AdviceVo>();//存放分组后的医嘱信息的List
				for(AdviceVo vo : m.getValue()){
					if(StringUtils.isNotBlank(vo.getGroup())){//如果前台的组合号不为空
						if(!vo.getGroup().equals(voGroup)){//如果前台的组合号与记录的组合号不同
							voGroup = vo.getGroup();//重新记录组合号
							group = adviceDAO.getSeqByName("SEQ_ADVICE_GROUPNO");//查询新组合号
						}
						if(StringUtils.isBlank(group)){//如果入库组合号为空，需要从新查询新组合号
							group = adviceDAO.getSeqByName("SEQ_ADVICE_GROUPNO");
						}
					}else{//如果前台的组合号为空，则需要查询组合号
						group = adviceDAO.getSeqByName("SEQ_ADVICE_GROUPNO");
					}
					vo.setGroup(new String(group));//保存新组合号到对象
					tempList.add(vo);//添加到组对象
					/**需求变动 医嘱项目最大数量改为打印时最大数目，不做为生成处方号时的限制条件 2017-02-22 17:46**/
//					if(tempList.size()==5){//当满足分组条件时分装对象并重新初始化组对象
//						retVoMap.put(adviceDAO.getSeqByName("SEQ_ADVICE_RECIPENO"),tempList);//key 为改分组下记录的处方号
//						tempList = new ArrayList<AdviceVo>();//初始化组对象
//					}
				}
				if(tempList.size()>0){//将剩余医嘱信息分装成对象
					retVoMap.put(adviceDAO.getSeqByName("SEQ_ADVICE_RECIPENO"),tempList);//key 为改分组下记录的处方号
				}
			}
		}
		return retVoMap;
	}
	
	/**  
	 * 判断是否为儿童
	 * @Description：  判断是否为儿童
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param：生日
	 * @return：true 是 false 否
	 *
	 */
	public static boolean calAge(Date birthday) {
		try {
			int yearOfAge = 0;
			Calendar cal = Calendar.getInstance();
			long nowMillis = cal.getTimeInMillis();
			long birthdayMillis = birthday.getTime();
			if (nowMillis < birthdayMillis) {
				return false;
			}
			
			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			if (yearNow == yearBirth) {
				return true;
			} else {
				yearOfAge = yearNow - yearBirth;
				if(yearOfAge<12){
					return true;
				}else{
					if(yearOfAge>12){
						return false;
					}else{
						if (monthNow < monthBirth) {
							return true;
						} else {
							if(monthNow > monthBirth){
								return false;
							}else{
								if (dayOfMonthNow > dayOfMonthBirth) {
									return false;
								}else{
									return true;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**  
	 *  
	 * @Description：  判断收费信息是否可修改
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:feeOdd 原医嘱附材信息
	 * @return：修改后的收费对象
	 *
	 */
	private Map<String, Object> checkAdviceVoOddFee(OutpatientFeedetailNow feeOdd) {
		if(feeOdd==null){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，未查询到医嘱收费信息！");
			return map;
		}
		if(feeOdd.getPayFlag()==1){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("resInfo", "no");
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，医嘱信息已收费，无法修改！");
			return map;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  校验医嘱信息是否合法
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:17:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 *
	 */
	private Map<String, Object> checkAdviceVo(DrugAndUnDrugVo drugAndUnDrugVo,AdviceVo vo,Map<String, String> docDrugGradeMap,Map<String, String> drugGradeCodeMap,Map<String, String> drugGradeNameMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(drugAndUnDrugVo==null){
			map.put("resMsg", "error");
			map.put("resCode", "【"+vo.getAdviceName()+"】不存在，请重新选择！");
			return map;
		}
		if(drugAndUnDrugVo.getDelFlg()==1){//删除
			map.put("resMsg", "error");
			map.put("resCode", "【"+vo.getAdviceName()+"】已经删除，请重新选择！");
			return map;
		}
		if(drugAndUnDrugVo.getStopFlg()==1){//停用
			map.put("resMsg", "error");
			map.put("resCode", "【"+vo.getAdviceName()+"】已停用，请重新选择！");
			return map;
		}
		if(drugAndUnDrugVo.getTy()==1&&drugAndUnDrugVo.getSurSum()==0){//没有库存
			map.put("resMsg", "error");
			map.put("resCode", "【"+vo.getAdviceName()+"】已没有库存，请重新选择！");
			return map;
		}
//		if(drugAndUnDrugVo.getTy()==1&&StringUtils.isNotBlank(drugAndUnDrugVo.getGrade())){//为药品并且药品等级不为空
//			if(StringUtils.isBlank(drugGradeCodeMap.get(drugAndUnDrugVo.getGrade()))){
//				map.put("resMsg", "error");
//				map.put("resCode", "【"+vo.getAdviceName()+"】的等级信息错误，请联系管理员！");
//				return map;
//			}
//			if(StringUtils.isBlank(docDrugGradeMap.get(drugGradeCodeMap.get(drugAndUnDrugVo.getGrade())))){
//				map.put("resMsg", "error");
//				map.put("resCode", "您无法开立药品【"+vo.getAdviceName()+"】，该药品等级为【"+drugGradeNameMap.get(drugAndUnDrugVo.getGrade())+"】，请重新选择！");
//				return map;
//			}
//		}
//		if(drugAndUnDrugVo.getTy()==1&&!vo.getTotalUnitHid().equals(vo.getAdPackUnitHid())){//如果药品的总单位不等于包装单位需要判断拆分属性
//			if(drugAndUnDrugVo.getSplitattr()==2){//2为门诊不可拆分
//				map.put("resMsg", "error");
//				map.put("resCode", "药品【"+vo.getAdviceName()+"】不可拆分，请您重新修改药品开立单位！");
//				return map;
//			}
//		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  对单条医嘱信息进行修改封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:vo 前台医嘱信息
	 * @param2:reOdd 原医嘱信息
	 * @return：医嘱对象
	 *
	 */
	private OutpatientRecipedetailNow packRecipUp(AdviceVo vo,OutpatientRecipedetailNow reOdd,Double payCost) {
		OutpatientRecipedetailNow recipedetail = reOdd;
		recipedetail.setItemCode(vo.getAdviceId());//项目代码
		recipedetail.setItemName(vo.getAdviceName());//项目名称
		recipedetail.setUnitPrice(vo.getAdPrice());//单价
		recipedetail.setQty(vo.getTotalNum());//开立数量
		recipedetail.setDays(vo.getSetNum());//付数
		recipedetail.setPackQty(vo.getPackagingnum());//包装数量
		recipedetail.setItemUnit(vo.getTotalUnitHid());//计价单位
		recipedetail.setPayCost(payCost);//自负金额
		recipedetail.setOnceDose(vo.getDosageMin());//每次用量
		recipedetail.setOnceUnit(vo.getAdDosaUnitHid());//每次用量单位
		recipedetail.setDoseModelCode(vo.getDosageform());//剂型代码
		recipedetail.setFrequencyCode(vo.getFrequencyHid());//频次
		recipedetail.setUsageCode(vo.getUsageNameHid());//用法
		recipedetail.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码
		recipedetail.setHypotest(vo.getIsSkinHid());//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴
		recipedetail.setInjectNumber(vo.getInjectionNum());//院内注射次数
		recipedetail.setRemark(vo.getRemark());//备注
		recipedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记
		recipedetail.setLabType(vo.getSampleTeptHid());//样本类型
		recipedetail.setCheckBody(vo.getInspectPartId());//检体
		recipedetail.setNeedConfirm(1);//是否需要确认  需要
		recipedetail.setPhamarcyCode(vo.getMinusDeptHid());//发药药房
		if(vo.getTy()==1){//药品
			if(vo.getAdPackUnitHid().equals(vo.getAdMinUnitHid())){//如果包装单位与最小单位相同，存最小单位
				recipedetail.setMinunitFlag(0);//开立单位1 包装单位0是最小单位
			}else if(vo.getAdPackUnitHid().equals(vo.getTotalUnitHid())){//如果包装单位与开立单位相同，存包装单位
				recipedetail.setMinunitFlag(1);//开立单位 1 包装单位0是最小单位
			}else if(vo.getAdMinUnitHid().equals(vo.getTotalUnitHid())){//如果最小单位单位与开立单位相同，存最小单位
				recipedetail.setMinunitFlag(0);//开立单位 1 包装单位0是最小单位
			}
		}else{//非药品默认包装单位
			recipedetail.setMinunitFlag(0);//开立单位 1 包装单位0是最小单位
		}
		recipedetail.setDataorder(vo.getDataOrder());
		recipedetail.setFrequencyName(vo.getFrequency());;//频次名称
		recipedetail.setUsageName(vo.getUsageName());;//用法名称
		recipedetail.setExecDpcdName(vo.getExecutiveDept());;//执行科室名称
		recipedetail.setDataorder(vo.getDataOrder());//排序
		recipedetail.setCombNo(vo.getGroup());//组合号
		return recipedetail;
	}
	
	/**  
	 *  
	 * @Description：  对单条医嘱进行封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:vo 要保存的医嘱对象
	 * @param2:key 处方号
	 * @param3:payCost 金额
	 * @return：医嘱明细对象
	 *
	 */
	private OutpatientRecipedetailNow packRecip(AdviceVo vo,String key,Double payCost,RegistrationNow registration,
											String patientNo,DrugAndUnDrugVo drugAndUnDrugVo,User user,SysDepartment dept,
											boolean isChimed,boolean subtblFlag,boolean applyFlg,String adviceChargeSeq,Integer index,Date date, 
											SysDepartment userDept,Map<String,String> sysTypeMap,Map<String,String> miniMap,Map<String,String> dosageMap,Map<String,String> doseMap){
		OutpatientRecipedetailNow recipedetail = new OutpatientRecipedetailNow();
		recipedetail.setSeeNo(registration.getOrderNo().toString());//看诊序号
		recipedetail.setClinicCode(registration.getClinicCode());//门诊号
		recipedetail.setPatientNo(patientNo);//病历号 
		recipedetail.setRegDate(registration.getRegDate());//挂号日期
		recipedetail.setRegDept(registration.getDeptCode());//挂号科室
		recipedetail.setItemCode(vo.getAdviceId());//项目代码
		recipedetail.setItemName(vo.getAdviceName());//项目名称
		recipedetail.setSpecs(vo.getSpecs());//规格
		recipedetail.setDrugFlag(drugAndUnDrugVo.getTy());//1药品，2非药品
		recipedetail.setClassCode(vo.getSysType());//系统类别
		recipedetail.setFeeCode(vo.getMinimumcost());//最小费用代码
		recipedetail.setUnitPrice(vo.getAdPrice());//单价
		recipedetail.setQty(vo.getTotalNum());//开立数量
		recipedetail.setDays(vo.getSetNum());//付数
		recipedetail.setPackQty(vo.getPackagingnum());//包装数量
		recipedetail.setItemUnit(vo.getTotalUnitHid());//计价单位
		recipedetail.setPayCost(payCost);//自负金额
		recipedetail.setBaseDose(vo.getAdDrugBasiHid());//基本剂量
		recipedetail.setSelfMade(vo.getIsmanufacture());//自制药
		recipedetail.setDrugQuanlity(vo.getNature());//药品性质
		recipedetail.setOnceDose(vo.getDosageMin());//每次用量，更改为每次剂量
		recipedetail.setOnceUnit(vo.getAdDosaUnitHid());//每次用量单位
		recipedetail.setDoseModelCode(vo.getDosageform());//剂型代码
		recipedetail.setFrequencyCode(vo.getFrequencyHid());//频次
		recipedetail.setUsageCode(vo.getUsageNameHid());//用法
		recipedetail.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码
		recipedetail.setMainDrug(drugAndUnDrugVo.getTy());//主药标志
		recipedetail.setCombNo(vo.getGroup());//组合号
		recipedetail.setHypotest(vo.getIsSkinHid());//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴
		recipedetail.setInjectNumber(vo.getInjectionNum());//院内注射次数
		recipedetail.setRemark(vo.getRemark());//备注
		recipedetail.setDoctCode(user.getAccount());//开立医生
		recipedetail.setDoctDpcd(userDept.getDeptCode());//医生科室
		recipedetail.setOperDate(date);//开立时间
		recipedetail.setStatus(vo.getAuditing()==1?4:0);//处方状态0开立，1收费，2确认，3作废,4申请审核，5审核通过，6审核未通过
		recipedetail.setAuditFlg(vo.getAuditing());//审核标记：0无需审核1待审核2通过3未通过
		recipedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记
		recipedetail.setLabType(vo.getSampleTeptHid());//样本类型
		recipedetail.setCheckBody(vo.getInspectPartId());//检体
		if(applyFlg){
			recipedetail.setApplyNo(adviceDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER"));//申请单号
		}
		recipedetail.setSubtblFlag(subtblFlag?1:0);//附材
		recipedetail.setNeedConfirm(1);//是否需要确认  需要
		recipedetail.setChargeFlag(0);//0未收费/1收费
		recipedetail.setRecipeNo(key);//处方号
		recipedetail.setPhamarcyCode(vo.getMinusDeptHid());//发药药房
		if(vo.getTy()==1){//药品
			if(vo.getAdPackUnitHid().equals(vo.getAdMinUnitHid())){//如果包装单位与最小单位相同，存最小单位
				recipedetail.setMinunitFlag(0);//开立单位1 包装单位0是最小单位
			}else if(vo.getAdPackUnitHid().equals(vo.getTotalUnitHidJudge())){//如果包装单位与开立单位相同，存包装单位
				recipedetail.setMinunitFlag(1);//开立单位 1 包装单位0是最小单位
			}else if(vo.getAdMinUnitHid().equals(vo.getTotalUnitHidJudge())){//如果最小单位单位与开立单位相同，存最小单位
				recipedetail.setMinunitFlag(0);//开立单位 1 包装单位0是最小单位
			}
		}else{//非药品默认包装单位
			recipedetail.setMinunitFlag(0);//开立单位 1 包装单位0是最小单位
		}
		recipedetail.setDataorder(vo.getDataOrder());
		recipedetail.setPrintFlag(0);//处方打印标志
		recipedetail.setSequencenNo(adviceDAO.getSeqByName("SEQ_ADVICE_STREAMNO"));//项目流水号 
		recipedetail.setRecipeFeeseq(adviceChargeSeq);//收费序列
		recipedetail.setRecipeSeq(index);//处方内流水号 
		recipedetail.setCreateUser(user.getAccount());
		recipedetail.setCreateDept(dept.getDeptCode());
		recipedetail.setCreateTime(date);
		recipedetail.setHospitalID(HisParameters.CURRENTHOSPITALID);
		recipedetail.setAreaCode(dept.getAreaCode());
		recipedetail.setRegDeptName(registration.getDeptName());//挂号科室名称
		recipedetail.setClassName(sysTypeMap.get(vo.getSysType()));//系统类别名称
		recipedetail.setFeeName(miniMap.get(vo.getMinimumcost()));//最小费用名称
		recipedetail.setOnceUnitName(doseMap.get(vo.getAdDosaUnitHid()));//每次用量单位名称
		recipedetail.setDoseModelName(dosageMap.get(vo.getDosageform()));//剂型名称
		recipedetail.setFrequencyName(vo.getFrequency());;//频次名称
		recipedetail.setUsageName(vo.getUsageName());;//用法名称
		recipedetail.setExecDpcdName(vo.getExecutiveDept());;//执行科室名称
		recipedetail.setDoctName(user.getName());;//开立医生姓名
		recipedetail.setDoctDpcdName(userDept.getDeptName());;//医生科室名称
		return recipedetail;
	}
	
	/**  
	 *  
	 * @Description：  对原有煎药附材信息进行修改封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:oddVo 原医嘱附材信息
	 * @return：修改后的收费附材对象
	 *
	 */
	private OutpatientFeedetailNow packFeedisChimedUp(AdviceVo vo,OutpatientFeedetailNow feedeOdd,DrugUndrug unDrug) {
		OutpatientFeedetailNow feedetail = feedeOdd;
		feedetail.setItemCode(unDrug.getId());//项目代码[12]
		feedetail.setItemName(unDrug.getName());//项目名称[13]
		feedetail.setSpecs(unDrug.getSpec());//规格[15]
		feedetail.setUnitPrice(unDrug.getDefaultprice());//单价[21]
		feedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记:1普通/2加急[28]
		feedetail.setQty(vo.getSetNum().doubleValue());//数量[22]
		feedetail.setFeeCode(unDrug.getUndrugMinimumcost());//最小费用代码
		feedetail.setTotCost(unDrug.getDefaultprice()*vo.getSetNum());//现金金额[38]
		feedetail.setFrequencyName(vo.getFrequency());//频次名称
		feedetail.setCombNo(vo.getGroup());//组合号[44]
		return feedetail;
	}
	
	/**  
	 *  
	 * @Description：  对原有附材信息进行修改封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:oddVo 原医嘱附材信息
	 * @return：修改后的收费附材对象
	 *
	 */
	private OutpatientFeedetailNow packFeedSubtblUp(AdviceVo vo,OutpatientFeedetailNow oddVo,Double oldQty,OutpatientRecipedetailNow recipedetail) {
		OutpatientFeedetailNow feedetailOdd = oddVo;
		feedetailOdd.setQty((oddVo.getQty()/oldQty)*vo.getTotalNum());//数量[22]
		feedetailOdd.setEmcFlag(vo.getIsUrgentHid());//加急标记:1普通/2加急[28]
		feedetailOdd.setTotCost(feedetailOdd.getUnitPrice()*vo.getTotalNum());//现金金额[38]
		feedetailOdd.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码[39]更改为发放药房
		feedetailOdd.setExecDpnm(vo.getExecutiveDept());//执行科室名称[40]更改为发放药房
		feedetailOdd.setFrequencyName(vo.getFrequency());//频次名称
		feedetailOdd.setCombNo(recipedetail.getCombNo());
		return feedetailOdd;
	}
	
	/**  
	 *  
	 * @Description：  对医嘱信息对应的收费信息进行封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:vo 要保存的医嘱对象
	 * @param2:payCost 金额
	 * @param3:recipedetail 对应医嘱对象
	 * @return：医嘱收费明细对象
	 *
	 */
	private OutpatientFeedetailNow packFeed(AdviceVo vo, Double payCost,OutpatientRecipedetailNow recipedetail,
										RegistrationNow registration,String adviceChargeSeq,User user,
										SysDepartment dept,boolean isChimed,SysDepartment userDept) {
		OutpatientFeedetailNow feedetail = new OutpatientFeedetailNow();
		feedetail.setRecipeNo(recipedetail.getRecipeNo());//处方号
		feedetail.setSequenceNo(recipedetail.getRecipeSeq());//处方内项目流水号[4]
		feedetail.setTransType(1);//交易类型,1正交易，2反交易[5]
		feedetail.setCardNo(registration.getCardNo());//就诊卡号
		feedetail.setRegDate(recipedetail.getRegDate());//挂号日期
		feedetail.setRegDpcd(dept.getDeptCode());//开单科室[9]
		feedetail.setDoctCode(user.getAccount());//开方医师[10]
		feedetail.setDoctDept(userDept.getDeptCode());//开方医师所在科室[11]
		feedetail.setItemCode(vo.getAdviceId());//项目代码[12]
		feedetail.setItemName(vo.getAdviceName());//项目名称[13]
		feedetail.setDrugFlag(recipedetail.getDrugFlag()+"");//1药品/0非药[14]
		feedetail.setSpecs(vo.getSpecs());//规格[15]
		feedetail.setSelfMade(vo.getIsmanufacture());//自制药标志
		feedetail.setDrugQuality(vo.getNature());//药品性质
		feedetail.setDoseModelCode(vo.getDosageform());//剂型[18]
		feedetail.setFeeCode(vo.getMinimumcost());//最小费用代码[19]
		feedetail.setClassCode(vo.getSysType());//系统类别[20]
		feedetail.setUnitPrice(vo.getAdPrice());//单价[21]
		feedetail.setQty(vo.getTotalNum());//数量[22]
		feedetail.setConfirmNum(0d);//已确认数量
		if(StringUtils.isNotBlank(vo.getDrugType())&&HisParameters.HERBSID.equals(vo.getDrugType())){
			feedetail.setDays(vo.getSetNum());//草药的付数，其他药品为1[23]
		}else{
			feedetail.setDays(1);//草药的付数，其他药品为1[23]
		}
		feedetail.setFrequencyCode(vo.getFrequencyHid());//频次代码[24]
		feedetail.setUsageCode(vo.getUsageNameHid());//用法代码[25]
		feedetail.setUseName(vo.getUsageName());//用法名称[26]
		feedetail.setInjectNumber(vo.getInjectionNum());//院内注射次数[27]
		feedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记:1普通/2加急[28]
		feedetail.setLabType(vo.getSampleTeptHid());//样本类型[29]
		feedetail.setCheckBody(vo.getInspectPartId());//检体[30]
		feedetail.setDoseOnce(vo.getDosageMin());//每次用量[31]
		feedetail.setDoseUnit(vo.getAdDosaUnitHid());//每次用量单位
		feedetail.setBaseDose(vo.getAdDrugBasiHid());//基本剂量[33]
		feedetail.setPackQty(vo.getPackagingnum());//包装数量[34]
		feedetail.setPriceUnit(recipedetail.getItemUnit());//计价单位[35]
		feedetail.setTotCost(recipedetail.getPayCost());//现金金额[38]
		feedetail.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码[39]更改为发放药房
		feedetail.setExecDpnm(vo.getExecutiveDept());//执行科室名称[40]更改为发放药房
		feedetail.setMainDrug(recipedetail.getDrugFlag());//主药标志[43]
		feedetail.setCombNo(recipedetail.getCombNo());//组合号[44]
		feedetail.setOperCode(user.getAccount());//划价人[45]
		feedetail.setOperDate(recipedetail.getCreateTime());//划价时间[46]
		feedetail.setPayFlag(vo.getAuditing()==1?4:0);//0划价 1收费 3预收费团体体检 4 药品预审核
		feedetail.setCancelFlag(1);//0退费，1正常，2重打，3注销[48]
		feedetail.setConfirmFlag(0);//0未确认/1确认[54]
		feedetail.setExtFlag(0);//扩展标志 特殊项目标志 1 0 非
		feedetail.setExtFlag1(0);//0 正常/1个人体检/2 集体体检
		feedetail.setExtFlag2(0);//日结标志：0：未日结/1：已日结
		feedetail.setExtFlag3(recipedetail.getMinunitFlag());//开立单位1 包装单位0是最小单位
		feedetail.setMoOrder(recipedetail.getSequencenNo());//医嘱项目流水号或者体检项目流水号
		feedetail.setRecipeSeq(adviceChargeSeq);//收费序列
		feedetail.setCostSource(0);//费用来源 0 操作员 1 医嘱 2 终端 3 体检
		feedetail.setSubjobFlag(0);//附材标志
		feedetail.setAccountFlag(0);//0没有扣账户 1 已经扣账户
		feedetail.setUseFlag(0);//0 未出结果 1 已出结果
		feedetail.setClinicCode(recipedetail.getClinicCode());//门诊号
		feedetail.setPatientNo(recipedetail.getPatientNo());;//病历号
		feedetail.setExtendTwo(recipedetail.getId());//扩展标记二，处方id
		feedetail.setCreateUser(recipedetail.getCreateUser());
		feedetail.setCreateDept(recipedetail.getCreateDept());
		feedetail.setCreateTime(recipedetail.getCreateTime());
		feedetail.setHospitalId(recipedetail.getHospitalID());
		feedetail.setAreaCode(recipedetail.getAreaCode());
		feedetail.setRegDpcdname(dept.getDeptName());//开单科室名称
		feedetail.setDoctCodename(user.getName());;//开方医师姓名
		feedetail.setDoctDeptname(userDept.getDeptName());//开方医师所在科室名称
		feedetail.setFrequencyName(recipedetail.getFrequencyName());//频次名称
		feedetail.setOperName(user.getName());//划价人姓名
		feedetail.setSampleId(recipedetail.getApplyNo());
		return feedetail;
	}
	
	/**  
	 *  
	 * @Description：  对医嘱信息对应的附材集合进行封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:oddList 要保存的附材集合
	 * @param2:recipedetail 对应医嘱对象
	 * @return：医嘱收费明细中的附材集合
	 *
	 */
	private List<OutpatientFeedetailNow> packFeedSubtbl(List<OdditionalitemAndUnDrugVo> oddList,OutpatientRecipedetailNow recipedetail,
													RegistrationNow registration,User user,SysDepartment dept,Date date,SysDepartment userDept,
													String adviceChargeSeq,String execDpnm) {
		List<OutpatientFeedetailNow> feedeList = new ArrayList<OutpatientFeedetailNow>();
		OutpatientFeedetailNow feedetailOdd = null;
		for(OdditionalitemAndUnDrugVo oddVo : oddList){
		feedetailOdd = new OutpatientFeedetailNow();
			feedetailOdd.setRecipeNo(recipedetail.getRecipeNo());//处方号
			feedetailOdd.setSequenceNo(0);//处方内项目流水号[4]
			feedetailOdd.setTransType(1);//交易类型,1正交易，2反交易[5]
			feedetailOdd.setCardNo(registration.getCardNo());//就诊卡号
			feedetailOdd.setRegDate(registration.getRegDate());//挂号日期
			feedetailOdd.setRegDpcd(dept.getDeptCode());//开单科室[9]
			feedetailOdd.setDoctCode(user.getAccount());//开方医师[10]
			feedetailOdd.setDoctDept(userDept.getDeptCode());//开方医师所在科室[11]
			feedetailOdd.setItemCode(oddVo.getUnDrugId());//项目代码[12]
			feedetailOdd.setItemName(oddVo.getName());//项目名称[13]
			feedetailOdd.setDrugFlag("0");//1药品/0非药[14]
			feedetailOdd.setSpecs(oddVo.getSpec());//规格[15]
			feedetailOdd.setFeeCode(oddVo.getMinimumCost());//最小费用代码[19]
			feedetailOdd.setClassCode(oddVo.getSysType());//系统类别[20]
			feedetailOdd.setUnitPrice(oddVo.getPrice());//单价[21]
			feedetailOdd.setQty(oddVo.getQty()*recipedetail.getQty());//数量[22]
			feedetailOdd.setConfirmNum(0d);//已确认数量
			feedetailOdd.setDays(1);//草药的付数，其他药品为1[23]
			feedetailOdd.setEmcFlag(recipedetail.getEmcFlag());//加急标记:1普通/2加急[28]
			feedetailOdd.setCheckBody(oddVo.getInspectionsite());//检体[30]
			feedetailOdd.setPriceUnit(oddVo.getUnit());//计价单位[35]
			feedetailOdd.setTotCost(oddVo.getTotalPrice()*recipedetail.getQty());//现金金额[38]
			feedetailOdd.setExecDpcd(recipedetail.getPhamarcyCode());//执行科室代码[39]更改为发放药房
			SysDepartment exeDept = deptInInterDAO.getByCode(execDpnm);
			feedetailOdd.setExecDpnm(exeDept.getDeptName());//执行科室名称[40]更改为发放药房
			feedetailOdd.setMainDrug(0);//主药标志[43]
			feedetailOdd.setCombNo(recipedetail.getCombNo());//组合号[44]
			feedetailOdd.setOperCode(user.getAccount());//划价人[45]
			feedetailOdd.setOperDate(date);//划价时间[46]
			feedetailOdd.setPayFlag(recipedetail.getAuditFlg()==1?4:0);//0划价 1收费 3预收费团体体检 4 药品预审核
			feedetailOdd.setCancelFlag(1);//0退费，1正常，2重打，3注销[48]
			feedetailOdd.setConfirmFlag(0);//0未确认/1确认[54]
			feedetailOdd.setExtFlag(0);//扩展标志 特殊项目标志 1 0 非
			feedetailOdd.setExtFlag1(0);//0 正常/1个人体检/2 集体体检
			feedetailOdd.setExtFlag2(0);//日结标志：0：未日结/1：已日结
			feedetailOdd.setExtFlag3(1);//开立单位1 包装单位0是最小单位
			feedetailOdd.setMoOrder(adviceDAO.getSeqByName("SEQ_ADVICE_STREAMNO"));//医嘱项目流水号或者体检项目流水号
			feedetailOdd.setRecipeSeq(adviceChargeSeq);//收费序列
			feedetailOdd.setCostSource(0);//费用来源 0 操作员 1 医嘱 2 终端 3 体检
			feedetailOdd.setSubjobFlag(1);//附材标志
			feedetailOdd.setAccountFlag(1);//0没有扣账户 1 已经扣账户
			feedetailOdd.setUseFlag(0);//0 未出结果 1 已出结果
			feedetailOdd.setClinicCode(registration.getClinicCode());//门诊号
			feedetailOdd.setPatientNo(recipedetail.getPatientNo());;//病历号
			feedetailOdd.setExtendOne(recipedetail.getSequencenNo());//扩展标记一(如果该项目为附材存主药的医嘱流水号,否则为空)
			feedetailOdd.setExtendTwo(recipedetail.getId());//扩展标记二，处方id
			feedetailOdd.setCreateUser(user.getAccount());
			feedetailOdd.setCreateDept(dept.getDeptCode());
			feedetailOdd.setCreateTime(date);
			feedetailOdd.setRegDpcdname(dept.getDeptName());//开单科室名称
			feedetailOdd.setDoctCodename(user.getName());;//开方医师姓名
			feedetailOdd.setDoctDeptname(userDept.getDeptName());//开方医师所在科室名称
			feedetailOdd.setFrequencyName(recipedetail.getFrequencyName());//频次名称
			feedetailOdd.setOperName(user.getName());//划价人姓名
			feedetailOdd.setSampleId(recipedetail.getApplyNo());
			feedeList.add(feedetailOdd);
		}
		return feedeList;
	}
	
	/**  
	 *  
	 * @Description：  对医嘱信息中需要医技终端确认的项目进行封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:recipedetail 医嘱信息
	 * @return：医技终端申请对象
	 *
	 */
	private TecTerminalApply packApply(OutpatientRecipedetailNow recipedetail,RegistrationNow registration,String deptId,
										String userId,PatientIdcard idcard,Date date) {
		TecTerminalApply apply = new TecTerminalApply();
		String applyNo = "";
		if(StringUtils.isBlank(recipedetail.getApplyNo())){
			applyNo = adviceDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER");
		}else{
			applyNo = recipedetail.getApplyNo();
		}
		apply.setApplyNumber(applyNo);//申请流水号
		apply.setClinicNo(recipedetail.getClinicCode());//住院流水号/门诊号/体检号
		apply.setName(registration.getPatientName());//姓名
		apply.setPactCode(registration.getPactCode());//合同单位
		apply.setDeptCode(deptId);//申请部门编码（科室或者病区）
		apply.setExecuteDeptcode(recipedetail.getExecDpcd());//终端科室编码 
		apply.setInhosDeptcode(registration.getDeptCode());//门诊是挂号科室、住院是在院科室
		apply.setDrugDeptCode(recipedetail.getPhamarcyCode());//发药部门编码
		apply.setRecipeDoccode(userId);//开立医师代码
		apply.setRecipeNo(recipedetail.getRecipeNo());//处方号
		apply.setSequenceNo(recipedetail.getRecipeSeq());//处方内项目流水号
		apply.setItemCode(recipedetail.getItemCode());//项目代码
		apply.setItemName(recipedetail.getItemName());//项目名称
		apply.setUnitPrice(recipedetail.getUnitPrice());//单价
		apply.setQty(recipedetail.getQty());//数量
		apply.setCurrentUnit(recipedetail.getItemUnit());//当前单位
		apply.setTotCost(recipedetail.getPayCost());//费用金额 
		apply.setSendFlag("0");//项目状态（0 划价  1 批费 2 执行（药品发放）
		apply.setPayFlag("0");//0 未收费 1门诊收费 2 扣门诊账户 3预收费团体体检 4 药品预审核
		apply.setExtFlag("1");//新旧项目标识： 0 旧 1 新
		apply.setExtFlag1("1");//1 有效  ,0无效
		apply.setMoOrder(recipedetail.getSequencenNo());//医嘱流水号
		apply.setMoExecSqn(recipedetail.getApplyNo());//医嘱执行单流水号
		apply.setOperCode(userId);//操作员（插入申请单）
		apply.setOperDate(new Date());//操作时间（插入申请单）
		apply.setPatienttype("1");//患者类别：‘1’ 门诊|‘2’ 住院|‘3’ 急诊|‘4’ 体检  5 集体体检
		apply.setPatientsex(registration.getPatientSex()+"");//性别
		apply.setIsphamacy(recipedetail.getDrugFlag()+"");//是否是药品 1：是0：否
		apply.setCardNo(idcard.getIdcardNo());//就诊卡编码
		apply.setCreateUser(userId);
		apply.setCreateDept(deptId);
		apply.setCreateTime(date);
		return apply;
	}
	
	/**  
	 *  
	 * @Description：  校验原有医嘱信息是否可以进行修改
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:17:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 *
	 */
	private Map<String, Object> checkAdviceVoOdd(OutpatientRecipedetailNow reOdd) {
		if(reOdd==null){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，为查询到原有医嘱信息，无法修改！");
			return map;
		}
		if(reOdd.getStatus()==1){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resInfo", "no");
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，医嘱信息已收费，无法修改！");
			return map;
		}
		if(reOdd.getStatus()==2){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resInfo", "no");
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，医嘱信息已确认，无法修改！");
			return map;
		}
		if(reOdd.getStatus()==3){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resInfo", "no");
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，医嘱信息已作废，无法修改！");
			return map;
		}
		if(reOdd.getAuditFlg()==2){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resInfo", "no");
			map.put("resMsg", "error");
			map.put("resCode", "医嘱修改失败，医嘱信息已审核，无法修改！");
			return map;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  对原有收费信息进行修改封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:reOdd 原医嘱信息
	 * @return：修改后的收费对象
	 *
	 */
	private OutpatientFeedetailNow packFeedUp(AdviceVo vo,OutpatientFeedetailNow feeOdd,Double payCost,OutpatientRecipedetailNow recipedetail) {
		OutpatientFeedetailNow feedetail = feeOdd; 
		feedetail.setItemCode(vo.getAdviceId());//项目代码[12]
		feedetail.setItemName(vo.getAdviceName());//项目名称[13]
		feedetail.setUnitPrice(vo.getAdPrice());//单价[21]
		feedetail.setQty(vo.getTotalNum());//数量[22]
		if(StringUtils.isNotBlank(vo.getDrugType())&&HisParameters.HERBSID.equals(vo.getDrugType())){
			feedetail.setDays(vo.getSetNum());//草药的付数，其他药品为1[23]
		}else{
			feedetail.setDays(1);//草药的付数，其他药品为1[23]
		}
		feedetail.setFrequencyCode(vo.getFrequencyHid());//频次代码[24]
		feedetail.setUsageCode(vo.getUsageNameHid());//用法代码[25]
		feedetail.setUseName(vo.getUsageName());//用法名称[26]
		feedetail.setInjectNumber(vo.getInjectionNum());//院内注射次数[27]
		feedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记:1普通/2加急[28]
		feedetail.setLabType(vo.getSampleTeptHid());//样本类型[29]
		feedetail.setCheckBody(vo.getInspectPartId());//检体[30]
		feedetail.setDoseOnce(vo.getDosageMin());//每次用量[31]
		feedetail.setDoseUnit(vo.getAdDosaUnitHid());
		feedetail.setBaseDose(vo.getAdDrugBasiHid());//基本剂量[33]
		feedetail.setPackQty(vo.getPackagingnum());//包装数量[34]
		feedetail.setPriceUnit(recipedetail.getItemUnit());//计价单位[35]
		feedetail.setExtFlag3(recipedetail.getMinunitFlag());//开立单位1 包装单位0是最小单位
		feedetail.setTotCost(payCost);//现金金额[38]
		feedetail.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码[39]更改为发放药房
		feedetail.setExecDpnm(vo.getExecutiveDept());//执行科室名称[40]更改为发放药房
		feedetail.setFrequencyName(recipedetail.getFrequencyName());//频次名称
		feedetail.setCombNo(recipedetail.getCombNo());//组合号[44]
		return feedetail;
	}
	
	/**  
	 *  
	 * @Description：  对原有收费信息的复合项目进行修改封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:reOdd 原医嘱信息
	 * @return：修改后的收费对象
	 *
	 */
	private OutpatientFeedetailNow packFeedListUp(AdviceVo vo,OutpatientFeedetailNow feeOdd,Double payCost,OutpatientRecipedetailNow recipedetail) {
		OutpatientFeedetailNow feedetail = feeOdd; 
		feedetail.setItemCode(feeOdd.getItemCode());//项目代码[12]
		feedetail.setItemName(feeOdd.getItemName());//项目名称[13]
		feedetail.setUnitPrice(feeOdd.getUnitPrice());//单价[21]
		feedetail.setQty(vo.getTotalNum());//数量[22]
		if(StringUtils.isNotBlank(vo.getDrugType())&&HisParameters.HERBSID.equals(vo.getDrugType())){
			feedetail.setDays(vo.getSetNum());//草药的付数，其他药品为1[23]
		}else{
			feedetail.setDays(1);//草药的付数，其他药品为1[23]
		}
		feedetail.setFrequencyCode(vo.getFrequencyHid());//频次代码[24]
		feedetail.setUsageCode(vo.getUsageNameHid());//用法代码[25]
		feedetail.setUseName(vo.getUsageName());//用法名称[26]
		feedetail.setInjectNumber(vo.getInjectionNum());//院内注射次数[27]
		feedetail.setEmcFlag(vo.getIsUrgentHid());//加急标记:1普通/2加急[28]
		feedetail.setLabType(vo.getSampleTeptHid());//样本类型[29]
		feedetail.setCheckBody(vo.getInspectPartId());//检体[30]
		feedetail.setDoseOnce(vo.getDosageMin());//每次用量[31]
		feedetail.setDoseUnit(vo.getAdDosaUnitHid());
		feedetail.setBaseDose(vo.getAdDrugBasiHid());//基本剂量[33]
		feedetail.setPackQty(vo.getPackagingnum());//包装数量[34]
		feedetail.setPriceUnit(vo.getAdMinUnitHid());//计价单位[35]
		feedetail.setExtFlag3(recipedetail.getMinunitFlag());//开立单位1 包装单位0是最小单位
		feedetail.setTotCost((vo.getTotalNum()==null?1:vo.getTotalNum())*feedetail.getUnitPrice());//现金金额[38]
		feedetail.setExecDpcd(vo.getExecutiveDeptHid());//执行科室代码[39]更改为发放药房
		feedetail.setExecDpnm(vo.getExecutiveDept());//执行科室名称[40]更改为发放药房
		feedetail.setFrequencyName(recipedetail.getFrequencyName());//频次名称
		feedetail.setCombNo(recipedetail.getCombNo());//组合号[44]
		return feedetail;
	}
	
	/**  
	 *  
	 * @Description：  对原有医技信息进行修改封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:oddVo 原医嘱附材信息
	 * @return：修改后的收费附材对象
	 *
	 */
	private TecTerminalApply packApplyUp(AdviceVo vo, TecTerminalApply applyOdd) {
		TecTerminalApply apply = applyOdd;
		apply.setQty(vo.getTotalNum());//数量
		apply.setCurrentUnit(vo.getTotalUnitHid());//当前单位
		apply.setTotCost(applyOdd.getUnitPrice()*vo.getTotalNum());//费用金额 
		return apply;
	}
	
	/**  
	 *  
	 * @Description：  对医嘱信息中的煎药附材进行封装
	 * @Author：aizhonghua
	 * @CreateDate：2016-03-29 下午02:37:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-03-29 下午02:37:28  
	 * @ModifyRmk：  
	 * @version 3.0
	 * @param1:vo 前台医嘱信息
	 * @param2:unDrug 附材信息
	 * @param3:recipedetail 医嘱信息
	 * @return：医嘱收费明细对象
	 *
	 */
	private OutpatientFeedetailNow packFeedisChimed(AdviceVo vo,DrugUndrug unDrug, OutpatientRecipedetailNow recipedetail,
												RegistrationNow registration,User user,SysDepartment dept,Date date,SysDepartment userDept,
												String adviceChargeSeq,String execDpnm) {
		OutpatientFeedetailNow feedetailChi = new OutpatientFeedetailNow();
		feedetailChi.setRecipeNo(recipedetail.getRecipeNo());//处方号
		feedetailChi.setSequenceNo(0);//处方内项目流水号[4]
		feedetailChi.setTransType(1);//交易类型,1正交易，2反交易[5]
		feedetailChi.setCardNo(registration.getCardNo());//就诊卡号
		feedetailChi.setRegDate(registration.getRegDate());//挂号日期
		feedetailChi.setRegDpcd(dept.getDeptCode());//开单科室[9]
		feedetailChi.setDoctCode(user.getAccount());//开方医师[10]
		feedetailChi.setDoctDept(userDept.getDeptCode());//开方医师所在科室[11]
		feedetailChi.setItemCode(unDrug.getCode());//项目代码[12]
		feedetailChi.setItemName(unDrug.getName());//项目名称[13]
		feedetailChi.setDrugFlag("0");//1药品/0非药[14]
		feedetailChi.setSpecs(unDrug.getSpec());//规格[15]
		feedetailChi.setUnitPrice(unDrug.getDefaultprice());//单价[21]
		feedetailChi.setQty(recipedetail.getDays().doubleValue());//数量[22]
		feedetailChi.setFeeCode(unDrug.getUndrugMinimumcost());//最小费用代码
		feedetailChi.setConfirmNum(0d);//已确认数量
		feedetailChi.setEmcFlag(recipedetail.getEmcFlag());//加急标记:1普通/2加急[28]
		feedetailChi.setTotCost(unDrug.getDefaultprice()*recipedetail.getDays());//现金金额[38]
		feedetailChi.setExecDpcd(recipedetail.getPhamarcyCode());//执行科室代码[39]更改为发放药房
		SysDepartment exeDept = deptInInterDAO.getByCode(execDpnm);
		feedetailChi.setExecDpnm(exeDept.getDeptName());//执行科室名称[40]更改为发放药房
		feedetailChi.setMainDrug(0);//主药标志[43]
		feedetailChi.setCombNo(recipedetail.getCombNo());//组合号[44]
		feedetailChi.setOperCode(user.getAccount());//划价人[45]
		feedetailChi.setOperDate(date);//划价时间[46]
		feedetailChi.setPayFlag(vo.getAuditing()==1?4:0);//0划价 1收费 3预收费团体体检 4 药品预审核
		feedetailChi.setCancelFlag(1);//0退费，1正常，2重打，3注销[48]
		feedetailChi.setConfirmFlag(0);//0未确认/1确认[54]
		feedetailChi.setExtFlag(0);//扩展标志 特殊项目标志 1 0 非
		feedetailChi.setExtFlag1(0);//0 正常/1个人体检/2 集体体检
		feedetailChi.setExtFlag2(0);//日结标志：0：未日结/1：已日结
		feedetailChi.setExtFlag3(1);//开立单位1 包装单位0是最小单位
		feedetailChi.setMoOrder(adviceDAO.getSeqByName("SEQ_ADVICE_STREAMNO"));//医嘱项目流水号或者体检项目流水号
		feedetailChi.setRecipeSeq(adviceChargeSeq);//收费序列
		feedetailChi.setCostSource(0);//费用来源 0 操作员 1 医嘱 2 终端 3 体检
		feedetailChi.setSubjobFlag(2);//附材标志
		feedetailChi.setUseFlag(0);//0 未出结果 1 已出结果
		feedetailChi.setClinicCode(registration.getClinicCode());//门诊号
		feedetailChi.setPatientNo(recipedetail.getPatientNo());//病历号
		feedetailChi.setExtendOne(recipedetail.getSequencenNo());//扩展标记一(如果该项目为附材存主药的医嘱流水号,否则为空)
		//feedetailChi.setExtendTwo();//扩展标记二，处方id
		feedetailChi.setCreateUser(user.getAccount());
		feedetailChi.setCreateDept(dept.getDeptCode());
		feedetailChi.setCreateTime(date);
		feedetailChi.setRegDpcdname(dept.getDeptName());//开单科室名称
		feedetailChi.setDoctCodename(user.getName());;//开方医师姓名
		feedetailChi.setDoctDeptname(userDept.getDeptName());//开方医师所在科室名称
		feedetailChi.setFrequencyName(recipedetail.getFrequencyName());//频次名称
		feedetailChi.setOperName(user.getName());//划价人姓名
		return feedetailChi;
	}
	
	/**  
	 *  
	 * @Description：  获得历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<AdviceVo> queryMedicalrecordHisList(String clinicNo,String para,String q) {
		List<OutpatientRecipedetailNow> recList = adviceDAO.queryMedicalrecordHisList(clinicNo,para,q);
		return recipedetailToAdviceVo(recList);
	}
	
	/**  
	 *  
	 * @Description：  医嘱信息转化成Vo
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<AdviceVo> recipedetailToAdviceVo(List<OutpatientRecipedetailNow> recList){
		List<AdviceVo> list = new ArrayList<AdviceVo>();
		AdviceVo vo = null;
		if(recList==null || recList.size()==0){
			return new ArrayList<AdviceVo>();
		}
		//获得单位
		List<ComboGroupVo> packList = innerCodeDao.getUnitAllGroup();
		Map<String,String> packMap = new HashMap<String,String>();
		if(packList!=null&&packList.size()>0){
			for(ComboGroupVo comboGroupVo : packList){
				packMap.put((HisParameters.UNDRUGUNIT.equals(comboGroupVo.getOrganize())?comboGroupVo.getName():comboGroupVo.getCode())+"_"+comboGroupVo.getOrganize(), comboGroupVo.getName());
			}
		}
		//获得频次
		List<BusinessFrequency> freqList = frequencyInInterDAO.getAll();
		Map<String,String> freqMap = new HashMap<String,String>();
		if(freqList!=null&&freqList.size()>0){
			for(BusinessFrequency freq : freqList){
				freqMap.put(freq.getEncode(), freq.getName());
			}
		}
		//获得用法
		List<BusinessDictionary> useaList = innerCodeDao.getDictionary("useage");
		Map<String,String> useaMap = new HashMap<String,String>();
		if(useaList!=null&&useaList.size()>0){
			for(BusinessDictionary usea : useaList){
				useaMap.put(usea.getEncode(), usea.getName());
			}
		}
		//获得科室
		List<SysDepartment> deptList = deptInInterDAO.getAll();
		Map<String,String> deptMap = new HashMap<String,String>();
		if(deptList!=null&&deptList.size()>0){
			for(SysDepartment dept : deptList){
				deptMap.put(dept.getDeptCode(), dept.getDeptName());
			}
		}
		//获得检查部位
		List<BusinessDictionary> checList = innerCodeDao.getDictionary("checkpoint");
		Map<String,String> checMap = new HashMap<String,String>();
		if(checList!=null&&checList.size()>0){
			for(BusinessDictionary chec : checList){
				checMap.put(chec.getEncode(), chec.getName());
			}
		}
		//获得样本类型
		List<BusinessDictionary> laboList = innerCodeDao.getDictionary("laboratorysample");
		Map<String,String> laboMap = new HashMap<String,String>();
		if(laboList!=null&&laboList.size()>0){
			for(BusinessDictionary labo : laboList){
				laboMap.put(labo.getId(), labo.getName());
			}
		}
		//获得用户
		List<User> userList = userInInterDAO.getAll();
		Map<String,String> userMap = new HashMap<String,String>();
		if(userList!=null&&userList.size()>0){
			for(User user : userList){
				userMap.put(user.getAccount(), user.getName());
			}
		}
		//获得皮试结果
		Map<Integer,String> skinMap = new HashMap<Integer,String>();
		skinMap.put(1, "不需要皮试");
		skinMap.put(2, "需要皮试，未做");
		skinMap.put(3, "皮试阳");
		skinMap.put(4, "皮试阴");
		String img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url("+ServletActionContext.getRequest().getContextPath()+"/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
		for(OutpatientRecipedetailNow recipedetail : recList){
			ViewInfoVo viewInfoVo = adviceDAO.findDrugAndUnDrugById(recipedetail.getItemCode(),recipedetail.getDrugFlag());
			if(viewInfoVo!=null){
				vo = new AdviceVo();
				vo.setId(recipedetail.getId());
				vo.setAdviceNo(recipedetail.getRecipeNo());
				vo.setColour(recipedetail.getStatus());//状态
				vo.setLimit((viewInfoVo.getIsProvincelimit()!=null&&viewInfoVo.getIsProvincelimit()==1)?"X":(viewInfoVo.getIsCitylimit()!=null&&viewInfoVo.getIsCitylimit()==1)?"S":"");
				vo.setAdviceNameView(viewInfoVo.getTy()==1?""+((recipedetail.getAuditFlg()!=null&&recipedetail.getAuditFlg()==1)?img:"")+"["+viewInfoVo.getPrice()+"元/"+packMap.get(viewInfoVo.getUnit()+"_"+HisParameters.DRUGPACKUNIT)+"]"+recipedetail.getItemName()+(StringUtils.isBlank(recipedetail.getSpecs())?"":"["+recipedetail.getSpecs()+"]"):((viewInfoVo.getIsInformedconsent()!=null&&viewInfoVo.getIsInformedconsent()==1?"√":"")+"["+viewInfoVo.getPrice()+"元/"+recipedetail.getItemUnit()+"]"+recipedetail.getItemName()+(StringUtils.isBlank(recipedetail.getSpecs())?"":"["+recipedetail.getSpecs()+"]")));//医嘱名称
				vo.setType(viewInfoVo.getSysType());//系统类型
				vo.setTy(viewInfoVo.getTy());//是否为药品
				vo.setAdviceType("临时医嘱");//医嘱类型
				vo.setAdviceId(viewInfoVo.getCode());//医嘱名称Id
				vo.setAdviceName(recipedetail.getItemName());//医嘱名称
				vo.setAdPrice(viewInfoVo.getPrice());//价格
				vo.setAdPackUnitHid(recipedetail.getItemUnit()+"_"+(recipedetail.getDrugFlag()==0?HisParameters.UNDRUGUNIT:HisParameters.DRUGPACKUNIT));//包装单位
				vo.setAdMinUnitHid(recipedetail.getDrugFlag()==0?null:viewInfoVo.getMinimumUnit()+"_"+HisParameters.DRUGMINUNIT);//单位
				vo.setAdDosaUnitHid(viewInfoVo.getDoseunit());//剂量
				vo.setAdDosaUnitHidJudge(viewInfoVo.getDoseunit()+"_"+HisParameters.DOSEUNIT);//剂量
				vo.setAdDrugBasiHid(viewInfoVo.getBasicdose());//基本剂量
				vo.setSpecs(viewInfoVo.getSpec());//规格
				vo.setSysType(viewInfoVo.getSysType());//系统类别
				vo.setDrugType(viewInfoVo.getType());//药品类别
				vo.setMinimumcost(viewInfoVo.getMinimumcost());//最小费用代码
				vo.setPackagingnum(viewInfoVo.getPackagingnum());//包装数量
				vo.setNature(viewInfoVo.getNature());;//药品性质
				vo.setIsmanufacture(viewInfoVo.getIsmanufacture());//自制药标志
				vo.setDosageform(viewInfoVo.getDosageform());//剂型
				vo.setIsInformedconsent(viewInfoVo.getIsInformedconsent()==null?0:viewInfoVo.getIsInformedconsent());//是否知情同意书
				vo.setGroup(recipedetail.getCombNo());//组
				vo.setTotalNum(recipedetail.getQty());//总量
				vo.setTotalUnitHid(recipedetail.getItemUnit());//总单位Id
				vo.setTotalUnitHidJudge(recipedetail.getItemUnit()+"_"+(recipedetail.getDrugFlag()==0?HisParameters.UNDRUGUNIT:((recipedetail.getMinunitFlag()!=null&&recipedetail.getMinunitFlag()==1)?HisParameters.DRUGPACKUNIT:HisParameters.DRUGMINUNIT)));//总单位Id
				vo.setTotalUnit(recipedetail.getDrugFlag()==0?packMap.get(recipedetail.getItemUnit()+"_"+HisParameters.UNDRUGUNIT):(recipedetail.getMinunitFlag()!=null&&recipedetail.getMinunitFlag()==1)?packMap.get(recipedetail.getItemUnit()+"_"+HisParameters.DRUGPACKUNIT):packMap.get(recipedetail.getItemUnit()+"_"+HisParameters.DRUGMINUNIT));//总单位
				vo.setDosageHid((viewInfoVo.getTy()==1&&recipedetail.getOnceDose()!=null)?doubleTow(recipedetail.getOnceDose()/(recipedetail.getBaseDose()==0?1:recipedetail.getBaseDose())):null);//每次用量
				vo.setDosageMin(viewInfoVo.getTy()==1?recipedetail.getOnceDose():null);//每次剂量
				vo.setDosage((viewInfoVo.getTy()==1&&recipedetail.getOnceDose()!=null)?(doubleTow(recipedetail.getOnceDose()/(recipedetail.getBaseDose()==0?1:recipedetail.getBaseDose()))+packMap.get(viewInfoVo.getMinimumUnit()+"_"+HisParameters.DRUGMINUNIT)+"="+recipedetail.getOnceDose()+packMap.get(viewInfoVo.getDoseunit()+"_"+HisParameters.DOSEUNIT)):null);//每次用量
				vo.setUnit(packMap.get(recipedetail.getOnceUnit()+"_"+HisParameters.DOSEUNIT));//单位
				vo.setSetNum(recipedetail.getDays());//服数
				vo.setFrequencyHid(recipedetail.getFrequencyCode());;//频次编码Id
				vo.setFrequency(freqMap.get(recipedetail.getFrequencyCode()));//频次编码
				vo.setUsageNameHid(recipedetail.getUsageCode());//用法名称Id
				vo.setUsageName(useaMap.get(recipedetail.getUsageCode()));//用法名称
				vo.setInjectionNum(recipedetail.getInjectNumber());//院注次数
				vo.setOpenDoctor(userMap.get(recipedetail.getDoctCode()));//开立医生
				vo.setExecutiveDeptHid(recipedetail.getExecDpcd());//执行科室
				vo.setExecutiveDept(deptMap.get(recipedetail.getExecDpcd()));//执行科室
				vo.setIsUrgentHid(recipedetail.getEmcFlag());//加急
				vo.setIsUrgent(recipedetail.getEmcFlag()==1?"是":"否");//加急
				vo.setInspectPartId(recipedetail.getCheckBody());//检查部位Id
				vo.setInspectPart(checMap.get(recipedetail.getCheckBody()));//检查部位
				vo.setSampleTeptHid(recipedetail.getLabType());//样本类型
				vo.setSampleTept(laboMap.get(recipedetail.getLabType()));//样本类型
				vo.setMinusDeptHid(recipedetail.getPhamarcyCode());//扣库科室
				vo.setMinusDept(deptMap.get(recipedetail.getPhamarcyCode()));//扣库科室
				vo.setRemark(recipedetail.getRemark());//备注
				vo.setInputPeop(userMap.get(recipedetail.getDoctCode()));//录入人
				vo.setOpenDept(deptMap.get(recipedetail.getDoctDpcd()));//开立科室
				vo.setStartTime(recipedetail.getOperDate());//开立时间
				vo.setEndTime(recipedetail.getCancelDate());//停止时间
				vo.setStopPeop(userMap.get(recipedetail.getCancelUserid()));//停止人
				vo.setIsSkinHid(recipedetail.getHypotest());//是否需要皮试
				vo.setIsSkin(skinMap.get(recipedetail.getHypotest()));//是否需要皮试
				vo.setAuditing((recipedetail.getAuditFlg()!=null&&recipedetail.getAuditFlg()==1)?1:0);
				vo.setSplitattr(viewInfoVo.getSplitattr());
				vo.setProperty(viewInfoVo.getProperty());
				list.add(vo);
			}
		}
		return list;
	}
	
	 public double doubleTow(Double dou) {
		BigDecimal bg = new BigDecimal(dou);
		double rtDou = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return rtDou;
     }
	
	/**  
	 *  
	 * @Description：  保存组套信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void savaStackInfo(String json,String stackFlag,String stackRemark,String stackInputCode,String stackSource,String stackName,String stackType,String stackInpmertype,String parent) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//当前用户
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();//当前科室
		OutpatientRecipedetailNow recipedetail = new OutpatientRecipedetailNow();//门诊处方
		BusinessStack stack = new BusinessStack();
		stack.setType(Integer.valueOf(stackType));
		if("1".equals(stackSource)){
			stack.setDeptId("ALL");
		}else{
			stack.setDeptId(deptId);
		}
		stack.setName(stackName);
		String str = stackInInterDAO.getSpellCode(stackName);
		int index=str.indexOf("$");
		String pinyin=str.substring(0,index);
		String wb=str.substring(index+1);
		stack.setPinYin(pinyin);
		stack.setWb(wb);
		stack.setInputCode(stackInputCode);
		stack.setDoc(userId);
		stack.setSource(stackSource);
		stack.setShareFlag(Integer.valueOf(stackFlag));
		stack.setStackObject(2);
		if("2".equals(stackInpmertype)){//门诊
			stack.setStackInpmertype(2);
		}else if("1".equals(stackInpmertype)){//住院
			stack.setStackInpmertype(1);
			stack.setParent("ROOT");
		}
		stack.setRemark(stackRemark);
		stack.setCreateUser(userId);
		stack.setCreateDept(deptId);
		stack.setCreateTime(new Date());
		stack.setStop_flg(0);
		stack.setDel_flg(0);
		/**业务变更 组套关联医院 2017-03-03 aizhonghua **/
		Hospital hospital = hospitalInInterDAO.getHospitalByCode(HisParameters.CURRENTHOSPITALCODE);
		stack.setHospitalId(hospital);
		if(StringUtils.isBlank(parent)){
			stack.setParent("ROOT");
		}
		stack.setIsValid(1);//有效
		stack.setIsConfirm(0);//是否需要确认
		stack.setIsOrder(0);//是否需要预约
		stackInInterDAO.save(stack);
		Gson gson = new Gson();
		List<StackVo> voList = null;
		try {
			json=json.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			voList =JSONUtils.fromJson(json,  new TypeToken<List<StackVo>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<BusinessStackinfo> infoList = new ArrayList<BusinessStackinfo>();
		BusinessStackinfo info = null;
		for(StackVo vo : voList){
			info = new BusinessStackinfo();
			info.setStackId(stack);
			info.setStackInfoItemId(vo.getId());
			info.setIsDrug(vo.getIsDrugShow());//
			info.setStackInfoNum(vo.getStackInfoNum());
			info.setStackInfoUnit(vo.getUnit());
			info.setStackInfoRemark(vo.getRemark());
			info.setFrequencyCode(vo.getFrequencyCode());
			info.setUsageCode(vo.getUsageCode());
			info.setOnceDose(vo.getOnceDose());
			info.setDoseUnit(vo.getDoseUnit());
			info.setDays(vo.getDays());
			info.setItemNote(vo.getItemNote());
			info.setDateBgn(StringUtils.isBlank(vo.getDateBgn())?null:DateUtils.parseDateY_M_D_H_M_S(vo.getDateBgn()));
			info.setDateEnd(StringUtils.isBlank(vo.getDateEnd())?null:DateUtils.parseDateY_M_D_H_M_S(vo.getDateEnd()));
			info.setDefaultprice(vo.getDefaultprice());
			info.setChildrenprice(vo.getChildrenprice());
			info.setSpecialprice(vo.getSpecialprice());
			info.setCreateUser(userId);
			info.setCreateDept(deptId);
			info.setCreateTime(new Date());
			info.setStop_flg(0);
			info.setDel_flg(0);
			infoList.add(info);
		}
		stackinfoInInterDAO.saveOrUpdateList(infoList);
		OperationUtils.getInstance().conserve(null,"门诊看诊","INSERT INTO","T_BUSINESS_STACK",OperationUtils.LOGACTIONCLINICDOCSTACK);
	}

	/**  
	 *  
	 * @Description：查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String queryHisAdvice(String patientNo) {
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		TreeJson pTree = new TreeJson();
		pTree.setId("root");
		pTree.setText("历史医嘱信息");
		pTree.setState("open");
		pTree.setIconCls("icon-table");
		//获取参数，是否分库
		String isParDb = parameterInnerDAO.getParameterByCode(HisParameters.ISPARDB);
		List<KeyValueVo> voList = adviceDAO.queryHisAdvice(patientNo,isParDb);
		if(voList!=null && voList.size()>0){
			String para = DateUtils.formatDateY(new Date());
			List<TreeJson> cTreeJson = new ArrayList<TreeJson>();
			TreeJson cTree = null;
			Map<String, String> cMap = null;
			for(KeyValueVo vo : voList){
				cTree = new TreeJson();
				cMap = new HashMap<String, String>();
				cTree.setId(vo.getKey());
				cTree.setText(vo.getValue());
				cTree.setIconCls("icon-vcard_edit");
				cTree.setState("open");
				cMap.put("para", vo.getParam());
				cMap.put("time", para);
				cTree.setAttributes(cMap);
				cTreeJson.add(cTree);
			}
			TreeJson sTree = new TreeJson();
			Map<String, String> sMap = new HashMap<String, String>();
			sTree.setId(patientNo);
			sTree.setText("<a>更多信息...</a>");
			sTree.setIconCls("icon-reload");
			sMap.put("time", para);
			sMap.put("isParDb", isParDb);
			sTree.setAttributes(sMap);
			cTreeJson.add(sTree);
			pTree.setChildren(cTreeJson);
		}
		treeJson.add(pTree);
		return JSONUtils.toJson(treeJson);
	}

	/**  
	 *  
	 * @Description：查询病历
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：
	 * @param:clinicNo 看诊号
	 * @version 1.0
	 *
	 */
	@Override
	public OutpatientMedicalrecord queryRecord(String clinicNo) {
		return medicalrecordDAO.getRecordByClinicCode(clinicNo);
	}

	/**  
	 *  
	 * @Description： 删除医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, Object> delAdvice(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OutpatientRecipedetailNow> recipedetailList = adviceDAO.getAdviceListByIds(id);
		if(recipedetailList!=null && recipedetailList.size()>0){
			for(OutpatientRecipedetailNow recipedetail : recipedetailList){
				if(recipedetail.getStatus()==1){
					map.put("resMsg", "error");
					map.put("resCode", "所选信息已收费，无法执行删除操作！");
					return map;
				}else if(recipedetail.getStatus()==5){
					map.put("resMsg", "error");
					map.put("resCode", "所选信息已审核，无法执行删除操作！");
					return map;
				}else if(recipedetail.getAuditFlg()==2){
					map.put("resMsg", "error");
					map.put("resCode", "所选信息已审核，无法执行删除操作！");
					return map;
				}
			}
			List<OutpatientFeedetailNow> feeList = feedetailDAO.getFeedListByIds(id);//获得全部收费信息
			String ids = "";
			for(OutpatientFeedetailNow feedetail : feeList){
				if(feedetail.getPayFlag()==1){
					map.put("resMsg", "error");
					map.put("resCode", "所选信息已收费，无法执行删除操作！");
					return map;
				}else{
					if(!"".equals(ids)){
						ids = ids + ",";
					}
					ids = ids + feedetail.getId();
				}
			}
			String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//当前用户
			adviceDAO.del(id, userId);
			feedetailDAO.delByAdviceIds(ids, userId);
			map.put("resMsg", "success");
			map.put("resCode", "删除成功！");
			OperationUtils.getInstance().conserve(id,"门诊看诊","UPDATE","T_OUTPATIENT_RECIPEDETAIL",OperationUtils.LOGACTIONDELETE);
			return map;
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "删除医嘱时出错！");
			return map;
		}
	}
	
	/**  
	 *  
	 * @Description： 获得医生可开立的全部特限药品
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> querySpecialDrugMap() {
		return adviceDAO.querySpecialDrugMap();
	}

	/**  
	 *  
	 * @Description：  查询该医师是否有审核权限
	 * @Author：aizhonghua
	 * @CreateDate：2015-01-21 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-01-21 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean queryAuditing() {
		return adviceDAO.queryAuditing();
	}

	/**  
	 *  
	 * @Description： 获得待审核的患者信息树
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String queryAuditTree(String id) {
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			TreeJson pTree = new TreeJson();
			pTree.setId("root");
			pTree.setText("待审核医嘱信息");
			pTree.setState("open");
			pTree.setIconCls("icon-table");
			List<KeyValueVo> voList = adviceDAO.queryAuditTree(id);
			if(voList!=null && voList.size()>0){
				List<TreeJson> cTreeJson = new ArrayList<TreeJson>();
				TreeJson cTree = null;
				Map<String,String> cMap = null;
				for(KeyValueVo vo : voList){
					cTree = new TreeJson();
					cTree.setId(vo.getKey());
					cTree.setText(vo.getValue());
					cTree.setState("closed");
					cMap = new HashMap<String, String>();
					cMap.put("rank","1");
					cTree.setAttributes(cMap);
					cTreeJson.add(cTree);
				}
				pTree.setChildren(cTreeJson);
			}
			treeJson.add(pTree);
		}else{
			List<KeyValueVo> voList = adviceDAO.queryAuditTree(id);
			if(voList!=null && voList.size()>0){
				TreeJson sTree = null;
				Map<String,String> sMap = null;
				for(KeyValueVo vo : voList){
					sTree = new TreeJson();
					sTree.setId(vo.getKey());
					sTree.setText(vo.getValue());
					sTree.setState("open");
					if(vo.getSex()==1){
						sTree.setIconCls("icon-user_b");
					}else if(vo.getSex()==2){
						sTree.setIconCls("icon-user_female");
					}else{
						sTree.setIconCls("icon-user_gray");
					}
					sMap = new HashMap<String, String>();
					sMap.put("rank","2");
					sMap.put("patientNo",vo.getParam());
					sTree.setAttributes(sMap);
					treeJson.add(sTree);
				}
			}
		}
		return JSONUtils.toJson(treeJson);
	}

	/**  
	 *  
	 * @Description： 审核
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String savaAdviceAuditing(String id, Integer start, String remarks) {
		OutpatientRecipedetailNow recipedetail = adviceDAO.get(id);
		if(start==1){
			recipedetail.setAuditFlg(2);
			recipedetail.setStatus(5);//处方状态0开立，1收费，2确认，3作废,4申请审核，5审核通过，6审核未通过
		}else{
			recipedetail.setAuditFlg(3);
			recipedetail.setStatus(6);//处方状态0开立，1收费，2确认，3作废,4申请审核，5审核通过，6审核未通过
		}
		recipedetail.setAuditRemark(remarks);
		adviceDAO.save(recipedetail);
		List<OutpatientFeedetailNow> feeList = feedetailDAO.queryFeeByIdAndSequencenNo(recipedetail.getId(),recipedetail.getSequencenNo());
		for(OutpatientFeedetailNow fee : feeList){
			fee.setPayFlag(0);
		}
		feedetailDAO.saveOrUpdateList(feeList);
		OperationUtils.getInstance().conserve(null,"门诊看诊","UPDATE","T_OUTPATIENT_RECIPEDETAIL",OperationUtils.LOGACTIONCLINICDOCAUDIT);
		return recipedetail.getClinicCode();
	}

	/**  
	 *  
	 * @Description： 查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, Object> searchInfoHid(String idCardNo) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		PatientIdcard idcard = idcardInInterDAO.getIdcardByIdCardNo(idCardNo);
		if(idcard==null){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "就诊卡号不存在！");
			return retMap;
		}
		if(idcard.getPatient()==null){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "患者信息不存在！");
			return retMap;
		}
		if(StringUtils.isBlank(idcard.getPatient().getMedicalrecordId())){
			retMap.put("resMsg", "error");
			retMap.put("resCode", "患者病历号不存在！");
			return retMap;
		}
		String json = queryHisAdvice(idcard.getPatient().getMedicalrecordId());
		retMap.put("resMsg", "success");
		retMap.put("resCode", json);
		return retMap;
	}

	/**  
	 *  
	 * @Description：  获得系统类别
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryJudgeSysTypeAllMap() {
		Map<String,String> retMap = new HashMap<String, String>();
		List<BusinessDictionary> list = innerCodeService.getDictionary(HisParameters.SYSTEMTYPE);
		if(list!=null&&list.size()>0){
			for(BusinessDictionary bd : list){
				retMap.put(bd.getName(), bd.getEncode());
			}
		}
		return retMap;
	}

	/**  
	 *  
	 * @Description：查询历史医嘱（加载更多）
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param：id收否分库patientNo就诊卡号para分区时间
	 */
	@Override
	public List<TreeJson> queryHisAdviceNext(String id,String patientNo,String para) {
		List<TreeJson> treeList = new ArrayList<TreeJson>();
		if("1".equals(id)){
			
		}else{
			List<KeyValueVo> voList = adviceDAO.queryHisAdviceNext(id,patientNo,para);
			String retPara = para;
			if(voList==null){//如果该分区数据为空查询下一分区
				//获取该分区以前的全部分区
				List<String> zoneList = ZoneManageUtil.getInstance().getBeforeZoneName(HisParameters.HISONLINEDB, "T_REGISTER_MAIN", para);
				if(zoneList==null || zoneList.size()==0){
					return treeList;
				}
				for(String key : zoneList){
					retPara = key.split("_")[key.split("_").length-1];
					voList = adviceDAO.queryHisAdviceNext(id,patientNo,retPara);
					if(voList!=null && voList.size()>0){//直到下一分区查到数据退出循环
						break;
					}
				}
			}
			if(voList!=null && voList.size()>0){//生成树节点
				TreeJson cTree = null;
				Map<String, String> cMap = null;
				for(KeyValueVo vo : voList){
					cTree = new TreeJson();
					cMap = new HashMap<String, String>();
					cTree.setId(vo.getKey());
					cTree.setText(vo.getValue());
					cTree.setIconCls("icon-vcard_edit");
					cTree.setState("open");
					cMap.put("para", vo.getParam());
					cMap.put("time", retPara);
					cTree.setAttributes(cMap);
					treeList.add(cTree);
				}
				//添加连接用以查询下一分区数据
				TreeJson sTree = new TreeJson();
				Map<String,String> sMap = new HashMap<String, String>();
				sTree.setId(patientNo);
				sTree.setText("<a>更多信息...</a>");
				sTree.setIconCls("icon-reload");
				//下次查询的分区时间为此次时间的前一年
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date;
				try {
					date = dateFormat.parse(retPara+"-01-01");
					sMap.put("time", DateUtils.formatDateY(DateUtils.addToDate(date, -1, 0, 0)));
				} catch (ParseException e) {
					String dateStr = (Integer.parseInt(retPara)-1)+"";
					sMap.put("time", dateStr);
				}
				sMap.put("isParDb", id);
				sTree.setAttributes(sMap);
				treeList.add(sTree);
			}
		}
		return treeList;
	}

	/**  
	 *  
	 * @Description： 查询特限药申请
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  clinicNo门诊号code药品编码
	 * @version 1.0
	 *
	 */
	@Override
	public DrugSpedrug querySpeDrugApply(String clinicNo, String code) {
		return spedrugInInterDAO.querySpeDrugApply(clinicNo,code);
	}
	
	/**  
	 *  
	 * @Description： 查询特限药申请组套
	 * @Author：aizhonghua
	 * @CreateDate：2017-03-01 上午11:50:53  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-03-01 上午11:50:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugSpedrug> querySpeDrugApplyStack(String clinicNo, String para) {
		return spedrugInInterDAO.querySpeDrugApplyStack(clinicNo,para);
	}
	
	/**  
	 *  
	 * 获得处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> queryPatientInfo(String page,String rows,String startTime,String endTime,String type,String para,String vague) {
		try {
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			List<String> tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_STO_RECIPE",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_STO_RECIPE",yNum+1);
					tnL.add(0,"T_STO_RECIPE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_STO_RECIPE_NOW");
			}
			return adviceDAO.queryPatientInfo(page,rows,startTime,endTime,tnL,type,para,vague);
		} catch (ParseException e) {
			Map<String,Object> retMap = new HashMap<String, Object>();
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RecipelStatVo>());
			return retMap;
		}
	}
	
	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RecipelInfoVo> getRecipelInfoRows(String recipeNo,String tab) {
		return adviceDAO.getRecipelInfoRows(recipeNo,tab);
	}
	/**  
	 * 
	 * 打印医嘱患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午5:26:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午5:26:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<RegisterMainVo> getRegisterMainVo(List<String> array) {
		return adviceDAO.getRegisterMainVo(array);
	}
	/**  
	 * 
	 * 打印医嘱处方信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午5:26:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午5:26:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientVo> getOutpatientVo(String clinicCode,String patientNo) {
		return adviceDAO.getOutpatientVo(clinicCode,patientNo);
	}
	/**  
	 * 
	 * 打印检查单患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月8日 下午5:21:38 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月8日 下午5:21:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<IreportPatientVo> getIreportPatientVo(String patientNo,String clinicCode) {
		return adviceDAO.getIreportPatientVo(patientNo, clinicCode);
	}
	
	/**  
	 *  
	 * 获得住院患者信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31    
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, Object> getRows(String page,String rows,String startTime,String endTime,String type,String para,String vague) {
		try {
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			List<String> tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_INFO_NOW");
			}
			return adviceDAO.getRows(page,rows,startTime,endTime,type,para,vague,tnL);
		} catch (ParseException e) {
			Map<String,Object> retMap = new HashMap<String, Object>();
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RecipelStatVo>());
			return retMap;
		}
	}

	/**  
	 *  
	 * 根据住院号查询医嘱信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoVo> getInpatientInfoRows(String inpatientNo,String tab) {
		return adviceDAO.getInpatientInfoRows(inpatientNo, tab);
	}

	@Override
	public List<LisVo> findLis(String cardNo,String page, String rows) {
		return adviceDAO.findLis(cardNo,page,rows);
	}

	@Override
	public Integer findLisNum(String cardNo) {
		return adviceDAO.findLisNum(cardNo);
	}

	@Override
	public List<InspectionReportList> findLisDetail(String inspectionId) {
		return adviceDAO.findLisDetail(inspectionId);
	}

}
