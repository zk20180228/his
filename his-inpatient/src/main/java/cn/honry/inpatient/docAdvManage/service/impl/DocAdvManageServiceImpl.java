package cn.honry.inpatient.docAdvManage.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessAdvdrugnature;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessOdditionalitem;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.inpatient.inpatientOrder.service.InpatientOrderInInterService;
import cn.honry.inner.inpatient.kind.dao.InpatientKindInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inpatient.docAdvManage.dao.DepartmentInfoDAO;
import cn.honry.inpatient.docAdvManage.dao.DocAdvManageDAO;
import cn.honry.inpatient.docAdvManage.service.DocAdvManageService;
import cn.honry.inpatient.docAdvManage.vo.AdviceLong;
import cn.honry.inpatient.docAdvManage.vo.ProInfoVo;
import cn.honry.inpatient.docAdvManage.vo.UnitsVo;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.UUIDGenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
@Service("docAdvManageService")
@Transactional
@SuppressWarnings({ "all" })
public class DocAdvManageServiceImpl implements DocAdvManageService{
	/**
     * 缓存类
     */
    @Resource
    private RedisUtil redisUtil;
    //基础工具类,不支持参数名传参
  	@Resource
  	private JdbcTemplate jdbcTemplate;

  	//扩展工具类,支持参数名传参
  	@Resource
  	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  	
	@Autowired
	@Qualifier(value = "inpatientOrderInInterService")
	private InpatientOrderInInterService inpatientOrderService;		
	public void setInpatientOrderService(InpatientOrderInInterService inpatientOrderService) {
		this.inpatientOrderService = inpatientOrderService;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "docAdvManageDAO")
	private DocAdvManageDAO docAdvManageDAO;
	/**公用编码接口**/
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value = "departmentInfoDAO")
	private DepartmentInfoDAO departmentInfoDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;//科室
	/**医嘱类型接口**/
	@Autowired
	@Qualifier(value = "inpatientKindInInterDAO")
	private InpatientKindInInterDAO inpatientKindInInterDAO;
	
	@Autowired
	@Qualifier(value = "inpatientInfoDAO")
	private InpatientInfoDAO inpatientInfoDAO;//住院主表service
	@Autowired
	@Qualifier(value = "drugInfoInInerDAO")
	private DrugInfoInInerDAO drugInfoInInerDAO;//药品service
	@Autowired
	@Qualifier(value = "undrugInInterDAO")
	private UndrugInInterDAO undrugInInterDAO;//非药品service
	
	/** 参数数据库操作类 **/
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	@Override
	public void removeUnused(String id) {			
	}

	@Override
	public InpatientOrder get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientOrder entity) {		
	}

	@Override
	public List<InpatientOrder> queryInpatientOrder(String decmpsState,String inpatientNo,String recordId) {
		List<InpatientOrder> inpatientOrderList = docAdvManageDAO.queryInpatientOrder(decmpsState,inpatientNo,recordId);
		for (InpatientOrder inpatientOrder : inpatientOrderList) {
			if("1".equals(inpatientOrder.getItemType())){
				if(inpatientOrder.getMark2()!=null){
					if("1".equals(inpatientOrder.getMark2())){
						inpatientOrder.setPriceUnit("B"+inpatientOrder.getPriceUnit());
					}else if("2".equals(inpatientOrder.getMark2())){
						inpatientOrder.setPriceUnit("Z"+inpatientOrder.getPriceUnit());
					}
				}
			}
		}
		return inpatientOrderList;
	}

	@Override
	public String queryDrugOrNodrug(String itemType) {
		if("1".equals(itemType)){
			List<DrugInfo> drugInfoList = docAdvManageDAO.queryDrugInfo();
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.setDateFormat("yyyy-MM-dd HH:mm:ss")  
			.create();
			return gson.toJson(drugInfoList);
		}else if("2".equals(itemType)){
			List<DrugUndrug> drugUndrugList = docAdvManageDAO.queryNoDrugInfo(null);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.setDateFormat("yyyy-MM-dd HH:mm:ss")  
			.create();
			return gson.toJson(drugUndrugList);
		}
		
		return null;
	}

	@Override
	public List<UnitsVo> queryUnits() {
		List<UnitsVo> unitsList = docAdvManageDAO.queryUnits();
		return unitsList;
	}

	@Override
	public List<ProInfoVo> querySysInfo(String page, String rows,String name,String type,String sysTypeName,String typeCode,String id) {		
		List<ProInfoVo> sysInfoList = docAdvManageDAO.querySysInfo(page,rows,name,type,sysTypeName,typeCode,id);
		return sysInfoList;
	}
	
	@Override
	public int querySysInfoTotal(String name, String type, String sysTypeName,String typeCode,
			String id) {
		return docAdvManageDAO.querySysInfoTotal(name, type, sysTypeName,typeCode, id);
	}
	
	@Override
	public Map<String, String> queryDrugpackagingunit() {		
		return docAdvManageDAO.queryDrugpackagingunit();
	}
	
	@Override
	public Map<String, String> queryNonmedicineencoding() {
		return docAdvManageDAO.queryNonmedicineencoding();
	}

	@Override
	public Map<String, String> queryDruggrade() {
		return docAdvManageDAO.queryDruggrade();		
	}

	@Override
	public Map<String, String> queryImplDepartment(String deptCode) {
		return  docAdvManageDAO.queryImplDepartment(deptCode);
		
	}

	@Override
	public Map<String, String> queryDrugStorage() {
		return docAdvManageDAO.queryDrugStorage();		 
	}

	@Override
	public Map<String, String> querySystemtype() {
		return docAdvManageDAO.querySystemtype();
	}

	@Override
	public Map<String, String> queryFrequency() {
		return docAdvManageDAO.queryFrequency();
	}

	@Override
	public Map<String, String> queryDrugUsemode() {		
		return docAdvManageDAO.queryDrugUsemode();
	}
	
	@Override
	public Map<String,Object> saveOrUpdateInpatientOrder(InpatientOrderNow entity,String str) throws Exception {
		JSONArray jsonArray = JSONArray.fromObject(str);
		//渲染包装单位和最小单位
		List<BusinessDictionary> mainName=innerCodeDao.getDictionary("minunit' or t.type='packunit");//最小单位
		Map<String,String> packAndMinUnit=new HashMap<String,String>();
		for(BusinessDictionary vo:mainName){
			packAndMinUnit.put(vo.getEncode(), vo.getName());
		}
		
		String id = null;
		String typeCode = null;//医嘱类型代码
		String typeName = null;//医嘱类型名称
		String classCode = null;//系统类别代码
		String className = null;//系统类别名称
		String itemCode = null;//项目Id
		String itemName = null;//项目名称
		String combNo = null;//组合号
		double qtyTot = 0;//总量
		int packQty = 0;//包装数量
		String priceUnit = null;//计价单位
		String doseOnce = null;
		String doseUnit = null;
		String specs = null;//规格
		String doseModelCode = null;
		String drugType = null;//药品类别
		String drugQuality = null;//药品性质
		String itemPrice = null;
		String usageCode = null;//用法代码
		String useName = null;//用法名称
		String useDays = null;//草药付数
		String frequencyCode = null;//频次代码
		String frequencyName = null;//频次名称
		Date dateBgn = null;//医嘱开始时间
		Date dateEnd = null;//医嘱结束时间
		Date moDate = null;	//医嘱开立时间
		String execDpcd = null;
		String execDpnm = null;
		String emcFlag = null;
		String labCode = null;
		String itemNote = null;//检查检体
		String moNote2 = null;//备注	
		String drugBasicdose = null;
		String permission = null;//患者是否同意
		String hypotest = null;//皮试
		String itemType = null;//药品非药品标志
		String moStat = null;//医嘱状态
		String drugpackagingUnit = null;//包装单位Id
		String minUnit = null;//最小单位Id
		String listDpcd = null;//开立科室
		String pharmacyCode = null;//扣库科室
		String docCode = null;//开立医师Id
		String docName = null;//开立医师名称
		String execTimes = null;//执行时间点[特殊频次]
		String execDose = null;//执行剂量[特殊频次]
		String combFlag = null;//该组合中的医嘱是否都未保存的标志
		String combNo1 = "";//上一条医嘱的组合号
		String combno = "";//生成的最新组合号
		InpatientInfoNow infoNow = null;
		if(StringUtils.isNotBlank(entity.getInpatientNo())){
			 infoNow = inpatientInfoDAO.inpatientIdGet(entity.getInpatientNo());
		}
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();//获取登录人
		SysEmployee curEmployee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		SysDepartment  loginDept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		SysDepartment  dept = (SysDepartment)SessionUtils.getCurrentUserLoginPharmacyFromShiroSession();//获取当前用户选择的药房
		Date d = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<BusinessOdditionalitem> odditionalitem =null;	
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			for (int i = 0; i < jsonArray.size(); i++) {
				InpatientOrderNow inpatientOrder = null;
				combNo = JSONObject.fromObject(jsonArray.getString(i)).getString("combNo");
				if(i>0){
					combNo1 = JSONObject.fromObject(jsonArray.getString(i-1)).getString("combNo");
				}
				combFlag = JSONObject.fromObject(jsonArray.getString(i)).getString("combFlag");
				if("".equals(combNo)||(!"".equals(combNo)&&(!"1".equals(combFlag)||("1".equals(combFlag)&&!combNo.equals(combNo1))))){
					String[] strArr = docAdvManageDAO.getSeqByName("SEQ_ADVICE_GROUPNO",1);	
					combno = strArr[0];
					combNo = strArr[0];
				}
				if(!"".equals(combNo)&&("1".equals(combFlag)&&combNo.equals(combNo1))){
					combNo = combno;
				}
				JSONObject jsonObj = JSONObject.fromObject(jsonArray.getString(i));				
				if("".equals(jsonObj.getString("id"))){
					typeCode = jsonObj.getString("typeCode");
					typeName = jsonObj.getString("typeName");	
					classCode = jsonObj.getString("classCode");
					className = jsonObj.getString("className");
					itemCode = jsonObj.getString("itemCode");
					itemName = jsonObj.getString("itemName");	
					docName = jsonObj.getString("docName");	
					if(StringUtils.isNotBlank(jsonObj.getString("qtyTot"))){
						qtyTot = Integer.parseInt(jsonObj.getString("qtyTot"));//总量
					}
					if(StringUtils.isNotBlank(jsonObj.getString("packQty"))){
						packQty = Integer.parseInt(jsonObj.getString("packQty"));//包装数量
					}														
					minUnit = jsonObj.getString("minUnit");	//计价单位		
					priceUnit = jsonObj.getString("priceUnit");	//计价单位	
					doseOnce = jsonObj.getString("doseOnce");	
					doseUnit = jsonObj.getString("doseUnit");	
					specs = jsonObj.getString("specs");//规格
					doseModelCode = jsonObj.getString("doseModelCode");//剂型代码
					drugType = jsonObj.getString("drugType");//药品类别
					drugQuality = jsonObj.getString("drugQuality");
					itemPrice = jsonObj.getString("itemPrice");		
					usageCode = jsonObj.getString("usageCode");	//用法Id				
					useName = jsonObj.getString("useName");//用法名称				
					useDays = jsonObj.getString("useDays");//草药付数
					frequencyCode = jsonObj.getString("frequencyCode");//频次id
					frequencyName = jsonObj.getString("frequencyName");	//频次名称	
					//开立时间
					String str1 = parameterInnerService.getparameter("yzkldatec");//医嘱开立时间是否以当前开立时间为准
					if("".equals(str1)){
						str1 = "1";
					}
					if("1".equals(str1)){												
						moDate = DateUtils.getCurrentTime();												
					}
					if("0".equals(str1)){
						Date moDate1 = dateFormat.parse(DateUtils.getDate()+" 12:00:00");
						Date moDate2 = DateUtils.getCurrentTime();
						Date moDate3 = dateFormat.parse(DateUtils.getDate()+" 00:00:00");						
						if(moDate1.before(moDate2)){
								moDate = moDate1;	
						}else{
								moDate = moDate3;
						}					
					}
					if(entity.getDecmpsState()==1){						
						String str2 = parameterInnerService.getparameter("klyzyxts");//新开医嘱默认生效间隔天数
						if("".equals(str2)){
							str2 = "0";
						}
						//长期医嘱开始时间、结束时间
						if("".equals(jsonObj.getString("dateBgn"))){
							if(Integer.parseInt(str2)>0){
								dateBgn = DateUtils.addDay(dateFormat.parse(DateUtils.getDate()+" 00:02:00"),Integer.parseInt(str2));
							}else{
								dateBgn = DateUtils.getCurrentTime();
							}					
						}else{
							String bgn = jsonObj.getString("dateBgn");								
							dateBgn = dateFormat.parse(bgn);
						}
						if(!"".equals(jsonObj.getString("dateEnd"))){							
							dateEnd = dateFormat.parse(jsonObj.getString("dateEnd"));
						}	
					}
					if(entity.getDecmpsState()==0){	
						dateBgn = moDate;
						dateEnd = new Date(dateBgn.getTime()+1 * 24 * 60 * 60 * 1000);												
					}				
					execDpcd = jsonObj.getString("execDpcd");
					execDpnm = jsonObj.getString("execDpnm");	
					emcFlag = jsonObj.getString("emcFlag");	//加急标志
					labCode = jsonObj.getString("labCode");	//样本类型Id
					itemNote = jsonObj.getString("itemNote");//检查检体Id				
					moNote2 = jsonObj.getString("moNote2");//备注			
					drugBasicdose = jsonObj.getString("baseDose");			
					permission = jsonObj.getString("permission");//是否需要患者同意 0 不需要  1需要
					hypotest = jsonObj.getString("hypotest");//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴
					itemType = jsonObj.getString("itemType");
					moStat = jsonObj.getString("moStat");//医嘱状态			
					drugpackagingUnit = jsonObj.getString("drugpackagingUnit"); //包装单位
					listDpcd = jsonObj.getString("listDpcd");//开立科室代码
					execTimes = jsonObj.getString("execTimes");//执行时间点[特殊频次]
					execDose = jsonObj.getString("execDose");//执行剂量[特殊频次]
					
					inpatientOrder = new InpatientOrderNow();				
					inpatientOrder.setId(null);
					inpatientOrder.setInpatientNo(entity.getInpatientNo());//住院号
					inpatientOrder.setPatientNo(entity.getPatientNo());//住院病历号
					inpatientOrder.setDeptCode(entity.getDeptCode());//住院科室代码
					if(StringUtils.isBlank(entity.getDeptCode())){
						inpatientOrder.setDeptName(null);
					}else{
						inpatientOrder.setDeptName(deptInInterDAO.getDeptCode(entity.getDeptCode()).getDeptName());//住院科室名称
					}
					inpatientOrder.setNurseCellCode(entity.getNurseCellCode());//住院护理站代码
					if(StringUtils.isBlank(entity.getNurseCellCode())){
						inpatientOrder.setNurseCellName(null);
					}else{
						inpatientOrder.setNurseCellName(deptInInterDAO.getDeptCode(entity.getNurseCellCode()).getDeptName());//住院护理站名称
					}
					UUIDGenerator uuid1 = new UUIDGenerator(); 	
					inpatientOrder.setMoOrder(uuid1.getUUID().substring(0, 15));//医嘱流水号
					inpatientOrder.setMoDate(moDate);//医嘱开立时间
					inpatientOrder.setBabyFlag(entity.getBabyFlag());//婴儿标记
					inpatientOrder.setHappenNo(entity.getHappenNo());//婴儿序号
					inpatientOrder.setSetItmattr(1);//项目属性,1院内项目/2院外项目
					inpatientOrder.setTypeCode(typeCode);
					inpatientOrder.setTypeName(typeName);
					inpatientOrder.setDecmpsState(entity.getDecmpsState());
					inpatientOrder.setChargeState(1);//是否计费
					if("1".endsWith(itemType)){
						inpatientOrder.setNeedDrug(1);//药房是否配药
						DrugInfo drugInfo = drugInfoInInerDAO.getByCode(itemCode);
						if(drugInfo.getDrugIsterminalsubmit()==null){
							inpatientOrder.setNeedConfirm(0);//是否确认
						}else{
							inpatientOrder.setNeedConfirm(drugInfo.getDrugIsterminalsubmit());//是否确认
						}
					}else{
						inpatientOrder.setNeedDrug(0);//药房是否配药
						DrugUndrug undrug = undrugInInterDAO.getCode(itemCode);
						if(undrug.getUndrugIssubmit()==null||undrug.getUndrugIspreorder()==null){
							inpatientOrder.setNeedConfirm(0);//是否确认
						}else{
							if(undrug.getUndrugIssubmit()==1||undrug.getUndrugIspreorder()==1){
								inpatientOrder.setNeedConfirm(1);//是否确认
							}else{
								inpatientOrder.setNeedConfirm(0);//是否确认
							}
						}
					}
					inpatientOrder.setPrnExelist(0);
					inpatientOrder.setPrmMorlist(0);//是否打印医嘱单
					if("1".equals(itemType)){
						inpatientOrder.setItemType(itemType);//项目类别1为药品，2为非药品
					}else{
						inpatientOrder.setItemType("2");
					}
					if("转科".equals(className)||"预约出院".equals(className)||"会诊".equals(className)||"转床".equals(className)||"膳食".equals(className)||"描述医嘱".equals(className)){
						inpatientOrder.setItemCode(itemCode);
						inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
						inpatientOrder.setFrequencyName(frequencyName);//频次名称
						odditionalitem = docAdvManageDAO.queryOdditionalitem(itemCode,listDpcd,2);
						if(odditionalitem.size()>0){
							inpatientOrder.setSetSubtbl(1);//是否包含附材
						}else{
							inpatientOrder.setSetSubtbl(0);
						}	
					}else{			
						inpatientOrder.setItemCode(itemCode);
						if(entity.getDecmpsState()==1){//判断是否为长期医嘱
							if("1".equals(itemType)){
								inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
								inpatientOrder.setFrequencyName(frequencyName);//频次名称
							}else{
								inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
								inpatientOrder.setFrequencyName(frequencyName);//频次名称
							}							
						}
						if(entity.getDecmpsState()==0){//判断是否为临时医嘱
							if("出院带药".equals(typeName)||"请假带药".equals(typeName)){
								inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
								inpatientOrder.setFrequencyName(frequencyName);//频次名称
								inpatientOrder.setMark1("25:00");
							}else{
								if("1".equals(itemType)){
									inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
									inpatientOrder.setFrequencyName(frequencyName);//频次名称
								}else{
									inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
									inpatientOrder.setFrequencyName(frequencyName);//频次名称
								}
							}						
						}
 						if("1".equals(itemType)){
							odditionalitem = docAdvManageDAO.queryOdditionalitem(usageCode,listDpcd,1);
							if(odditionalitem.size()>0){
								inpatientOrder.setSetSubtbl(1);//是否包含附材
							}else{
								inpatientOrder.setSetSubtbl(0);
							}
						}else{
							odditionalitem = docAdvManageDAO.queryOdditionalitem(itemCode,listDpcd,2);
							if(odditionalitem.size()>0){
								inpatientOrder.setSetSubtbl(1);//是否包含附材
							}else{
								inpatientOrder.setSetSubtbl(0);
							}
						}
					}				
					inpatientOrder.setItemName(itemName);
					inpatientOrder.setClassCode(classCode);
					inpatientOrder.setClassName(className);				
					if("1".equals(itemType)){
						inpatientOrder.setPharmacyCode(dept.getDeptCode());//取药药房、扣库科室
					}			
					Map<String,String> implDepartment = docAdvManageDAO.queryImplDepartment(entity.getDeptCode());
					String deptName =implDepartment.get(entity.getDeptCode());
					if(StringUtils.isNotBlank(execDpcd)){
						inpatientOrder.setExecDpcd(execDpcd);//执行科室id
					}else{
						inpatientOrder.setExecDpcd(entity.getDeptCode());//执行科室id
					}
					if(StringUtils.isNotBlank(execDpnm)){
						inpatientOrder.setExecDpnm(execDpnm);//执行科室名称	
					}else{
						inpatientOrder.setExecDpnm(deptName);//执行科室名称	
					}
					if(StringUtils.isNotBlank(drugBasicdose)){
						inpatientOrder.setBaseDose(Double.parseDouble(drugBasicdose));//药品基本剂量
					}else{
						inpatientOrder.setBaseDose(null);
					}
					inpatientOrder.setDoseUnit(doseUnit);//剂量单位
					
					inpatientOrder.setDrugpackagingUnit(drugpackagingUnit);//包装单位
					
					inpatientOrder.setMinUnit(minUnit);//最小单位
					inpatientOrder.setMinUnitName(packAndMinUnit.get(minUnit));
					if(priceUnit.indexOf("B")!=-1){
						priceUnit=priceUnit.replace("B","");
						inpatientOrder.setMark2("1");//扩展备注1   1包装单位  2最小单位
					}
					if(priceUnit.indexOf("Z")!=-1){
						priceUnit=priceUnit.replace("Z","");
						inpatientOrder.setMark2("2");//扩展备注1   1包装单位  2最小单位
					}
					inpatientOrder.setPriceUnit(priceUnit);//计价单位
					inpatientOrder.setPriceUnitName(packAndMinUnit.get(priceUnit));
					inpatientOrder.setPackQty(Integer.valueOf(packQty));//包装数量
					inpatientOrder.setSpecs(specs);//规格
					inpatientOrder.setDoseModelCode(doseModelCode);//剂型代码
					inpatientOrder.setDrugType(drugType);//药品类别
					inpatientOrder.setDrugQuality(drugQuality);//药品性质		
					if(!"".equals(itemPrice)){				
						inpatientOrder.setItemPrice(Double.parseDouble(itemPrice));//价格
					}else{
						inpatientOrder.setItemPrice(null);//价格
					}					
					inpatientOrder.setCombNo(combNo);//组合序号
					inpatientOrder.setMainDrug(1);//主药标记				
					inpatientOrder.setMoStat(Integer.parseInt(moStat));//医嘱状态,0开立，1审核，2执行，3作废，4重整，5需要上级审核，6上级审核不通过				
					inpatientOrder.setUsageCode(usageCode);//用法ID
					inpatientOrder.setUseName(useName);//用法名称
					inpatientOrder.setEnglishAb("");//用法英文缩写？					
					if(StringUtils.isNotBlank(doseOnce)){
						inpatientOrder.setDoseOnce(Double.parseDouble(doseOnce));//每次剂量
					}else{
						inpatientOrder.setDoseOnce(null);//每次剂量
					}
					inpatientOrder.setStocKin(1);//0扣护士站常备药/1扣药房 即是否扣药房
					inpatientOrder.setQtyTot(qtyTot);//项目总量	
					if(StringUtils.isNotBlank(useDays)){
						inpatientOrder.setUseDays(Integer.parseInt(useDays));
					}else{
						inpatientOrder.setUseDays(null);	//付数
					}		
					//开始时间、结束时间、第一次分解时间、第二次分解时间
					if(entity.getDecmpsState()==1){//判断是否为长期医嘱
						List<BusinessFrequency> frequencyList = docAdvManageDAO.queryBusinessFrequency(frequencyCode);
						String str3 = parameterInnerService.getparameter("axsjfpccode");
						String str4 = parameterInnerService.getparameter("sfsryl");
						if("".equals(str4)){
							str4 = "0";
						}
						if("".equals(str3)){
							str3 = "0";
						}
						String[] ary = str3.split(",");//调用API方法按照逗号分隔字符串
						int a=0;
						int b=0;
						if(frequencyList.size()>0){
							for(String item: ary){
							   if(item.equals(frequencyList.get(0).getEncode())){
								   		a=a+1;   
							   }		  
							}
							if(a>0){//判断医嘱的频次是否等于参数表中按小时计费的频次代码
								inpatientOrder.setDateBgn(moDate);//医嘱开始时间
								inpatientOrder.setDateEnd(DateUtils.addDay(moDate,1));	//医嘱结束时间
							}
							for(int j=0;j<frequencyList.get(0).getEncode().length();j++){//执行频次是否为星期						
						            char  item1 =  frequencyList.get(0).getEncode().charAt(j);
						            if("W".equals(item1)){
						            	b=b+1;
						            }
							}
						}
						if(a==0 && b>0 && "0".equals(str4) && "1".equals(itemType)){//不按小时计费、首日量不为空、执行频次是否为星期、是否为药品
							inpatientOrder.setDateBgn(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));
							inpatientOrder.setDateEnd(DateUtils.addDay(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"),1));
						}else{
							inpatientOrder.setDateBgn(dateBgn);
							inpatientOrder.setDateEnd(dateEnd);
						}
					}
					if(entity.getDecmpsState()==0){//判断是否为临时医嘱
						inpatientOrder.setDateBgn(dateBgn);//医嘱开始时间
						inpatientOrder.setDateEnd(dateEnd);	//医嘱结束时间
					}				
					inpatientOrder.setDcFlag(0);//医嘱停止标志
					inpatientOrder.setMoNote2(moNote2);	//备注
					if(StringUtils.isNotBlank(hypotest)){
						inpatientOrder.setHypotest(Integer.parseInt(hypotest));//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴	
					}else{
						inpatientOrder.setHypotest(null);//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴	
					}											
					inpatientOrder.setItemNote(itemNote);//检查部位检体
					inpatientOrder.setApplyNo(null);
					inpatientOrder.setEmcFlag(Integer.parseInt(emcFlag));//加急标记: 0普通/1加急
					inpatientOrder.setGetFlag(0);//医嘱提取标记: 0待提取/1已提取/2DC提取
					inpatientOrder.setSubtblFlag(0);//是否附材''1''是viewStackInfo
					int sortId =0;
					List<InpatientOrderNow> list=docAdvManageDAO.queryMaxInpatientOrderSortId();
					if(list!=null&&list.size()>0){
						if(docAdvManageDAO.queryMaxInpatientOrderSortId().get(0).getSortId()!=null){
							sortId = docAdvManageDAO.queryMaxInpatientOrderSortId().get(0).getSortId();
						}
					}							
					inpatientOrder.setSortId(sortId+1);//排列序号，按排列序号由大到小顺序显示医嘱
					inpatientOrder.setLabCode(labCode);//样本类型 名称
					if(StringUtils.isNotBlank(permission)){
						inpatientOrder.setPermission(Integer.parseInt(permission));//是否需要患者同意 0 不需要  1需要
					}else{
						inpatientOrder.setPermission(null);
					}
					inpatientOrder.setDcDate(new Date());//开立时间
					inpatientOrder.setDcDoccd(user.getAccount());//开立医师
					inpatientOrder.setDcDocnm(user.getName());//开立医师名称
					inpatientOrder.setPackageCode(null);//组套编码
					inpatientOrder.setPackageName(null);//组套名称
					inpatientOrder.setDocCode(curEmployee.getJobNo());	//开立医师代码 -hedong 20160829 改存工号
					inpatientOrder.setDocName(user.getName());//开立医师名字
					inpatientOrder.setListDpcd(listDpcd);//开立科室代码
					inpatientOrder.setListDpcdName(deptInInterDAO.getDeptCode(listDpcd).getDeptName());//开立科室代码
					inpatientOrder.setDecoFlag(0);//整档标记 0无/1有
					inpatientOrder.setRecUsercd(curEmployee.getJobNo());//录入人员代码-hedong 20160829 改存工号
					inpatientOrder.setRecUsernm(user.getName());//录入人员名称
					inpatientOrder.setExecTimes(execTimes);//执行时间点[特殊频次]
					inpatientOrder.setExecDose(execDose);//执行剂量[特殊频次]
					inpatientOrder.setId(null);			
					inpatientOrder.setCreateUser(user.getAccount());
					inpatientOrder.setCreateDept(loginDept.getDeptCode());//创建科室
					inpatientOrder.setCreateTime(DateUtils.getCurrentTime());//创建时间
					inpatientOrder.setUpdateUser(user.getAccount());
					inpatientOrder.setUpdateTime(DateUtils.getCurrentTime());	
					inpatientOrder.setPatientName(infoNow.getPatientName());
					inpatientOrder.setAreaCode(entity.getAreaCode());
					inpatientOrder.setHospitalId(entity.getHospitalId());
					docAdvManageDAO.save(inpatientOrder);
					OperationUtils.getInstance().conserve(null,"医嘱管理","INSERT INTO","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONINSERT);
				}else{//修改已保存的医嘱
					id = jsonObj.getString("id");
					typeCode = jsonObj.getString("typeCode");
					typeName = jsonObj.getString("typeName");	
					classCode = jsonObj.getString("classCode");
					className = jsonObj.getString("className");
					itemCode = jsonObj.getString("itemCode");
					itemName = jsonObj.getString("itemName");
					itemType = jsonObj.getString("itemType");//药品非药品标记
					qtyTot = Double.valueOf(jsonObj.getString("qtyTot"));
					packQty = Integer.valueOf(jsonObj.getString("packQty"));									
					priceUnit = jsonObj.getString("priceUnit");	
					if("1".equals(itemType)){
						drugBasicdose = jsonObj.getString("baseDose");	
						doseOnce = jsonObj.getString("doseOnce");	
						doseUnit = jsonObj.getString("doseUnit");
						pharmacyCode = jsonObj.getString("pharmacyCode");//扣库科室
					}											
					usageCode = jsonObj.getString("usageCode");	//用法Id				
					useName = jsonObj.getString("useName");//用法名称
					useDays = jsonObj.getString("useDays");
					frequencyCode = jsonObj.getString("frequencyCode");//频次id
					frequencyName = jsonObj.getString("frequencyName");	//频次名称						
					moDate = dateFormat.parse(jsonObj.getString("moDate"));
					moStat = jsonObj.getString("moStat");//医嘱状态
					if(entity.getDecmpsState()==1){						
						String str2 = parameterInnerService.getparameter("klyzyxts");//新开医嘱默认生效间隔天数
						if("".equals(str2)){
							str2 = "0";
						}
						//长期医嘱开始时间、结束时间
						if("".equals(jsonObj.getString("dateBgn"))){
							if(Integer.parseInt(str2)>0){
								dateBgn = DateUtils.addDay(dateFormat.parse(DateUtils.getDate()+" 00:02:00"),Integer.parseInt(str2));
							}else{
								dateBgn = DateUtils.getCurrentTime();
							}					
						}else{
							String bgn = jsonObj.getString("dateBgn");								
							dateBgn = dateFormat.parse(bgn);
						}
						if(!"".equals(jsonObj.getString("dateEnd"))){							
							dateEnd = dateFormat.parse(jsonObj.getString("dateEnd"));
						}	
					}
					if(entity.getDecmpsState()==0){	
						dateBgn = moDate;
						dateEnd = new Date(dateBgn.getTime()+1 * 24 * 60 * 60 * 1000);												
					}
					inpatientOrder = docAdvManageDAO.queryInpatientOrder(id);
					//开始时间、结束时间、第一次分解时间、第二次分解时间
					if(entity.getDecmpsState()==1){//判断是否为长期医嘱
						List<BusinessFrequency> frequencyList = docAdvManageDAO.queryBusinessFrequency(frequencyCode);
						String str3 = parameterInnerService.getparameter("axsjfpccode");
						String str4 = parameterInnerService.getparameter("sfsryl");
						if("".equals(str4)){
							str4 = "0";
						}
						if("".equals(str3)){
							str3 = "0";
						}
						String[] ary = str3.split(",");//调用API方法按照逗号分隔字符串
						int a=0;
						int b=0;
						if(frequencyList.size()>0){
							for(String item: ary){
							   if(item.equals(frequencyList.get(0).getEncode())){
								   		a=a+1;   
							   }		  
							}
							if(a>0){//判断医嘱的频次是否等于参数表中按小时计费的频次代码
								inpatientOrder.setDateBgn(moDate);//医嘱开始时间
								inpatientOrder.setDateEnd(DateUtils.addDay(moDate,1));	//医嘱结束时间
								inpatientOrder.setDateCurmodc(moDate);//医嘱本次分解时间
								inpatientOrder.setDateNxtmodc(moDate);//医嘱下次分解时间
							}
							for(int j=0;j<frequencyList.get(0).getEncode().length();j++){//执行频次是否为星期						
						            char  item1 =  frequencyList.get(0).getEncode().charAt(j);
						            if("W".equals(item1)){
						            	b=b+1;
						            }
							}
						}
						if(a==0 && b>0 && "0".equals(str4) && "1".equals(itemType)){//不按小时计费、首日量不为空、执行频次是否为星期、是否为药品
							inpatientOrder.setDateBgn(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));
							inpatientOrder.setDateEnd(DateUtils.addDay(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"),1));
							inpatientOrder.setDateCurmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));//医嘱本次分解时间
							inpatientOrder.setDateNxtmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));//医嘱下次分解时间
						}else{
							inpatientOrder.setDateBgn(dateBgn);
							inpatientOrder.setDateEnd(dateEnd);
							inpatientOrder.setDateCurmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:00:00"));//医嘱本次分解时间
							inpatientOrder.setDateNxtmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:00:00"));//医嘱下次分解时间
						}
					}
					if(entity.getDecmpsState()==0){//判断是否为临时医嘱
						inpatientOrder.setDateBgn(dateBgn);//医嘱开始时间
						inpatientOrder.setDateEnd(dateEnd);	//医嘱结束时间
					}	
					execDpcd = jsonObj.getString("execDpcd");//执行科室id
					execDpnm = jsonObj.getString("execDpnm");//执行科室名称		
					emcFlag = jsonObj.getString("emcFlag");	//加急标志
					labCode = jsonObj.getString("labCode");	//样本类型Id
					itemNote = jsonObj.getString("itemNote");//检查检体Id
					try {
						moNote2 = jsonObj.getString("moNote2");//备注	
					} catch (JSONException e) {
						moNote2 = "";
					}						
					hypotest = jsonObj.getString("hypotest");//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴					
					listDpcd = jsonObj.getString("listDpcd");//开立科室代码
					docCode = jsonObj.getString("docCode");//开立科室代码
					docName = jsonObj.getString("docName");//开立科室代码
					execTimes = jsonObj.getString("execTimes");//执行时间点[特殊频次]
					execDose = jsonObj.getString("execDose");//执行剂量[特殊频次]
					
					inpatientOrder.setId(id);
					inpatientOrder.setCombNo(combNo);//组号
					if("描述医嘱".equals(className)){
						inpatientOrder.setItemCode("userdefined");
						List<BusinessFrequency> frequencyList = docAdvManageDAO.queryFrequencyByEncode("QD1");
						inpatientOrder.setFrequencyCode(frequencyList.get(0).getEncode());//频次代码
						inpatientOrder.setFrequencyName(frequencyList.get(0).getName());//频次名称
						odditionalitem = docAdvManageDAO.queryOdditionalitem("userdefined",listDpcd,2);
						if(odditionalitem.size()>0){
							inpatientOrder.setSetSubtbl(1);//是否包含附材
						}else{
							inpatientOrder.setSetSubtbl(0);
						}	
					}else{			
						inpatientOrder.setItemCode(itemCode);
						if(entity.getDecmpsState()==1){//判断是否为长期医嘱
							inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
							inpatientOrder.setFrequencyName(frequencyName);//频次名称
						}
						if(entity.getDecmpsState()==0){//判断是否为临时医嘱
							if("出院带药".equals(typeName)||"请假带药".equals(typeName)){
								inpatientOrder.setFrequencyCode(frequencyCode);//频次代码
								inpatientOrder.setFrequencyName(frequencyName);//频次名称
								inpatientOrder.setMark1("25:00");
							}else{
								if("1".equals(itemType)){
									List<BusinessFrequency> frequencyList = docAdvManageDAO.queryFrequencyByEncode("ONCE");
									inpatientOrder.setFrequencyCode(frequencyList.get(0).getEncode());//频次代码
									inpatientOrder.setFrequencyName(frequencyList.get(0).getName());//频次名称
								}else{
									List<BusinessFrequency> frequencyList = docAdvManageDAO.queryFrequencyByEncode("QD1");
									inpatientOrder.setFrequencyCode(frequencyList.get(0).getEncode());//频次代码
									inpatientOrder.setFrequencyName(frequencyList.get(0).getName());//频次名称
								}
							}						
						}
						if("1".equals(itemType)){
							odditionalitem = docAdvManageDAO.queryOdditionalitem(usageCode,listDpcd,1);
							if(odditionalitem.size()>0){
								inpatientOrder.setSetSubtbl(1);//是否包含附材
							}else{
								inpatientOrder.setSetSubtbl(0);
							}
						}else{
							odditionalitem = docAdvManageDAO.queryOdditionalitem(itemCode,listDpcd,2);
							if(odditionalitem.size()>0){
								inpatientOrder.setSetSubtbl(1);//是否包含附材
							}else{
								inpatientOrder.setSetSubtbl(0);
							}
						}
					}				
					inpatientOrder.setItemName(itemName);//项目名称
					inpatientOrder.setClassCode(classCode);//系统类别代码
					inpatientOrder.setClassName(className);//系统类别名称				
					if("1".equals(itemType)){
						inpatientOrder.setPharmacyCode(dept.getDeptCode());//取药药房
					}
					Map<String,String> implDepartment = docAdvManageDAO.queryImplDepartment(entity.getDeptCode());
					String deptName =implDepartment.get(entity.getDeptCode());
					if(StringUtils.isNotBlank(execDpcd)){
						inpatientOrder.setExecDpcd(execDpcd);//执行科室id
					}else{
						inpatientOrder.setExecDpcd(entity.getDeptCode());//执行科室id
					}
					if(StringUtils.isNotBlank(execDpnm)){
						inpatientOrder.setExecDpnm(execDpnm);//执行科室名称	
					}else{
						inpatientOrder.setExecDpnm(deptName);//执行科室名称	
					}
					if(StringUtils.isNotBlank(drugBasicdose)){
						inpatientOrder.setBaseDose(Double.parseDouble(drugBasicdose));//药品基本剂量
					}else{
						inpatientOrder.setBaseDose(null);
					}
					inpatientOrder.setDoseUnit(doseUnit);//剂量单位	
					if(priceUnit.indexOf("B")!=-1){
						priceUnit=priceUnit.replace("B","");
						inpatientOrder.setMark2("1");//扩展备注1   1包装单位  2最小单位
					}
					if(priceUnit.indexOf("Z")!=-1){
						priceUnit=priceUnit.replace("Z","");
						inpatientOrder.setMark2("2");//扩展备注1   1包装单位  2最小单位
					}
					inpatientOrder.setPriceUnit(priceUnit);//计价单位
					inpatientOrder.setPackQty(Integer.valueOf(packQty));//包装数量								
					inpatientOrder.setEnglishAb("");//用法英文缩写？
					
					if(StringUtils.isNotBlank(doseOnce)){
						inpatientOrder.setDoseOnce(Double.parseDouble(doseOnce));//每次剂量
					}else{
						inpatientOrder.setDoseOnce(null);//每次剂量
					}
					inpatientOrder.setQtyTot(qtyTot);//项目总量	
					if(StringUtils.isNotBlank(useDays)){
						inpatientOrder.setUseDays(Integer.parseInt(useDays));
					}else{
						inpatientOrder.setUseDays(null);	//付数
					}
					inpatientOrder.setMoNote2(moNote2);	//备注
					if(StringUtils.isNotBlank(hypotest)){
						inpatientOrder.setHypotest(Integer.parseInt(hypotest));//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴	
					}else{
						inpatientOrder.setHypotest(null);//1不需要皮试/2需要皮试，未做/3皮试阳/4皮试阴	
					}											
					inpatientOrder.setItemNote(itemNote);//检查部位检体
					inpatientOrder.setApplyNo(null);
					inpatientOrder.setEmcFlag(Integer.parseInt(emcFlag));//加急标记: 0普通/1加急			
					inpatientOrder.setLabCode(labCode);//样本类型 名称	
					inpatientOrder.setListDpcd(listDpcd);//开立科室
					inpatientOrder.setListDpcdName(deptInInterDAO.getDeptCode(listDpcd).getDeptName());//开立科室代码
					inpatientOrder.setPharmacyCode(pharmacyCode);//扣库科室
					inpatientOrder.setDocCode(docCode);//开立医师代码
					inpatientOrder.setDocName(docName);//开立医师名称
					inpatientOrder.setExecTimes(execTimes);//执行时间点[特殊频次]
					inpatientOrder.setExecDose(execDose);//执行剂量[特殊频次]
					List<InpatientOrderNow> inpatientOrderList1 = docAdvManageDAO.queryInpatientOrderById(id,null,"1","0");
					if(usageCode.equals(inpatientOrderList1.get(0).getUsageCode())&&inpatientOrderList1.get(0).getUsageCode()!=null){
						odditionalitem=null;
					}
					if(!usageCode.equals(inpatientOrderList1.get(0).getUsageCode())&&inpatientOrderList1.get(0).getUsageCode()!=null){
						List<InpatientOrderNow> inpatientOrderList2 = docAdvManageDAO.queryInpatientOrderById(null,inpatientOrderList1.get(0).getCombNo(),"0","1");
						for(int k=0;k<inpatientOrderList2.size();k++){			
							docAdvManageDAO.delInpatientOrder(inpatientOrderList2.get(k).getId());
							OperationUtils.getInstance().conserve(inpatientOrderList2.get(k).getId(),"医嘱管理","UPDATE","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONDELETE);	
						}				
					}
					inpatientOrder.setMoStat(Integer.parseInt(moStat));//医嘱状态,0开立，1审核，2执行，3作废，4重整，5需要上级审核，6上级审核不通过	
					inpatientOrder.setUsageCode(usageCode);//用法ID
					inpatientOrder.setUseName(useName);//用法名称
					inpatientOrder.setUpdateUser(user.getAccount());
					inpatientOrder.setUpdateTime(DateUtils.getCurrentTime());
					inpatientOrder.setPatientName(infoNow.getPatientName());
					docAdvManageDAO.update(inpatientOrder);
					OperationUtils.getInstance().conserve(id,"医嘱管理","UPDATE","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONUPDATE);
				}						
				if(odditionalitem!=null && odditionalitem.size()>0){//保存附材医嘱
					for(int h=0;h<odditionalitem.size();h++){
						List<DrugUndrug> drugUndrugList = docAdvManageDAO.queryNoDrugInfo(odditionalitem.get(h).getItemCode());
						InpatientOrder oddInpatientOrder = new InpatientOrder();
						oddInpatientOrder.setId(null);
						oddInpatientOrder.setInpatientNo(entity.getInpatientNo());
						oddInpatientOrder.setPatientNo(entity.getPatientNo());
						oddInpatientOrder.setDeptCode(entity.getDeptCode());//住院科室代码
						oddInpatientOrder.setDeptName(deptInInterDAO.getDeptCode(entity.getDeptCode()).getDeptName());//住院科室名称
						oddInpatientOrder.setNurseCellCode(entity.getNurseCellCode());//住院护理站代码 	
						oddInpatientOrder.setNurseCellName(deptInInterDAO.getDeptCode(entity.getNurseCellCode()).getDeptName());//住院护理站名称		
						UUIDGenerator uuid1 = new UUIDGenerator(); 	
						oddInpatientOrder.setMoOrder(uuid1.getUUID().substring(0, 15));//医嘱流水号
						oddInpatientOrder.setMoDate(moDate);//医嘱日期
						oddInpatientOrder.setBabyFlag(entity.getBabyFlag());//婴儿标记
						oddInpatientOrder.setHappenNo(entity.getHappenNo());//婴儿序号
						oddInpatientOrder.setSetItmattr(1);//项目属性,1院内项目/2院外项目
						oddInpatientOrder.setTypeCode(typeCode);//医嘱类型
						oddInpatientOrder.setTypeName(typeName);//医嘱名称
						oddInpatientOrder.setDecmpsState(entity.getDecmpsState());//医嘱标识，1：长期、2：临时
						oddInpatientOrder.setChargeState(1);//是否计费?
						if("1".endsWith(itemType)){
							inpatientOrder.setNeedDrug(1);//药房是否配药
							DrugInfo drugInfo = drugInfoInInerDAO.getByCode(itemCode);
							if(drugInfo.getDrugIsterminalsubmit()==null){
								inpatientOrder.setNeedConfirm(0);//是否确认
							}else{
								inpatientOrder.setNeedConfirm(drugInfo.getDrugIsterminalsubmit());//是否确认
							}
						}else{
							inpatientOrder.setNeedDrug(0);//药房是否配药
							DrugUndrug undrug = undrugInInterDAO.getCode(itemCode);
							if(undrug.getUndrugIssubmit()==null||undrug.getUndrugIspreorder()==null){
								inpatientOrder.setNeedConfirm(0);//是否确认
							}else{
								if(undrug.getUndrugIssubmit()==1||undrug.getUndrugIspreorder()==1){
									inpatientOrder.setNeedConfirm(1);//是否确认
								}else{
									inpatientOrder.setNeedConfirm(0);//是否确认
								}
							}
						}
						oddInpatientOrder.setPrnExelist(0);//打印执行单
						oddInpatientOrder.setPrmMorlist(0);//是否打印医嘱单
						oddInpatientOrder.setItemType("2");//项目类别1为药品，2为非药品
						oddInpatientOrder.setItemCode(odditionalitem.get(h).getItemCode());//项目Id				
						oddInpatientOrder.setItemName(drugUndrugList.get(0).getName());//项目名称
						oddInpatientOrder.setClassCode(classCode);//系统类别Id
						oddInpatientOrder.setClassName(className);//系统类别名称
						Map<String,String> implDepartment = docAdvManageDAO.queryImplDepartment(drugUndrugList.get(0).getUndrugDept());
						String deptName =implDepartment.get(drugUndrugList.get(0).getUndrugDept());												
						oddInpatientOrder.setExecDpcd(inpatientOrder.getExecDpcd());//执行科室名称							
						oddInpatientOrder.setExecDpnm(inpatientOrder.getExecDpnm());//执行科室名称
						oddInpatientOrder.setPharmacyCode(inpatientOrder.getPharmacyCode());//扣库科室
						oddInpatientOrder.setDocCode(inpatientOrder.getDocCode());//开立医师代码
						oddInpatientOrder.setDocName(inpatientOrder.getDocName());//开立医师名称
						oddInpatientOrder.setMoStat(inpatientOrder.getMoStat());//医嘱状态
						if(odditionalitem.get(h).getUseInterval()==24){
							List<BusinessFrequency> frequencyList = docAdvManageDAO.queryFrequencyByEncode("QD1");
							oddInpatientOrder.setFrequencyCode(frequencyList.get(0).getEncode());//频次代码
							oddInpatientOrder.setFrequencyName(frequencyList.get(0).getName());//频次名称
						}else{			
							oddInpatientOrder.setFrequencyCode(inpatientOrder.getFrequencyCode());//频次代码
							oddInpatientOrder.setFrequencyName(inpatientOrder.getFrequencyName());//频次名称
						}
						oddInpatientOrder.setSetSubtbl(0);//是否包含附材
						oddInpatientOrder.setItemPrice(drugUndrugList.get(0).getDefaultprice());//价格
						oddInpatientOrder.setCombNo(combNo);//组合序号
						oddInpatientOrder.setMainDrug(0);//主药标记
						oddInpatientOrder.setPriceUnit(drugUndrugList.get(0).getUnit());//单位
						if(odditionalitem.get(h).getQty() != null){
							oddInpatientOrder.setQtyTot(odditionalitem.get(h).getQty());//项目总量	
						}else{
							oddInpatientOrder.setQtyTot(1.0);//项目总量	
						}						
						//开始时间、结束时间、第一次分解时间、第二次分解时间
						if(entity.getDecmpsState()==1){//判断是否为长期医嘱
							List<BusinessFrequency> frequencyList = docAdvManageDAO.queryBusinessFrequency(frequencyCode);
							String str3 = parameterInnerService.getparameter("axsjfpccode");
							String str4 = parameterInnerService.getparameter("sfsryl");
							if("".equals(str4)){
								str4 = "0";
							}
							if("".equals(str3)){
								str3 = "0";
							}
							String[] ary = str3.split(",");//调用API方法按照逗号分隔字符串
							int a=0;
							int b=0;
							if(frequencyList.size()>0){
								for(String item: ary){
									   if(item.equals(frequencyList.get(0).getEncode())){
										   		a=a+1;   
									   }		  
									}
									if(a>0){//判断医嘱的频次是否等于参数表中按小时计费的频次代码
										oddInpatientOrder.setDateBgn(moDate);//医嘱开始时间
										oddInpatientOrder.setDateEnd(DateUtils.addDay(moDate,1));	//医嘱结束时间
										oddInpatientOrder.setDateCurmodc(moDate);//医嘱本次分解时间
										oddInpatientOrder.setDateNxtmodc(moDate);//医嘱下次分解时间
									}
									for(int j=0;j<frequencyList.get(0).getEncode().length();j++){						
								            char  item1 =  frequencyList.get(0).getEncode().charAt(j);
								            if("W".equals(item1)){
								            	b=b+1;
								            }
									}
							}							
							if(a==0 && b>0 && "0".equals(str4) && "1".equals(itemType)){//不按小时计费、首日量不为空、执行频次是否为星期、是否为药品
								oddInpatientOrder.setDateBgn(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));
								oddInpatientOrder.setDateEnd(DateUtils.addDay(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"),1));
								oddInpatientOrder.setDateCurmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));//医嘱本次分解时间
								oddInpatientOrder.setDateNxtmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:01:00"));//医嘱下次分解时间
							}else{
								oddInpatientOrder.setDateBgn(dateBgn);
								oddInpatientOrder.setDateEnd(dateEnd);
								oddInpatientOrder.setDateCurmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:00:00"));//医嘱本次分解时间
								oddInpatientOrder.setDateNxtmodc(dateFormat.parse(DateUtils.formatDateY_M_D(moDate)+" 00:00:00"));//医嘱下次分解时间
							}
						}
						if(entity.getDecmpsState()==0){//判断是否为临时医嘱
							oddInpatientOrder.setDateBgn(dateBgn);//医嘱开始时间
							oddInpatientOrder.setDateEnd(dateEnd);	//医嘱结束时间
						}	
						if(!"检查".equals(className)){
							oddInpatientOrder.setItemNote(drugUndrugList.get(0).getUndrugInspectionsite());//检查部位检体
						}else{
							oddInpatientOrder.setLabCode(drugUndrugList.get(0).getUndrugLabsample());//样本类型 名称
						}					
						oddInpatientOrder.setSubtblFlag(1);//是否附材''1''是					
						oddInpatientOrder.setPermission(drugUndrugList.get(0).getUndrugIsinformedconsent());//是否需要患者同意 0 不需要  1需要
						oddInpatientOrder.setPackageCode(null);//组套编码
						oddInpatientOrder.setPackageName(null);//组套名称
						oddInpatientOrder.setCreateUser(user.getAccount());//创建人
						oddInpatientOrder.setCreateDept(loginDept.getDeptCode());//创建科室
						oddInpatientOrder.setCreateTime(DateUtils.getCurrentTime());//创建时间
						oddInpatientOrder.setPatientName(infoNow.getPatientName());
						docAdvManageDAO.save(oddInpatientOrder);
						OperationUtils.getInstance().conserve(null,"医嘱管理","INSERT INTO","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONINSERT);
					}
				}
			}
			map.put("resMsg", "success");
			map.put("resCode", "医嘱保存成功");
		}catch(Exception e){
			e.printStackTrace();
			map.put("resMsg", "error");
			map.put("resCode", "医嘱保存失败！");
		}
		return map;
	}

	@Override
	public List<BusinessOdditionalitem> queryOdditionalitem(String code,String deptId,int drugFlag) {
		List<BusinessOdditionalitem> odditionalitem = docAdvManageDAO.queryOdditionalitem(code,deptId,drugFlag);
		return odditionalitem;
	}

	@Override
	public List<SysDruggraDecontraStrank> queryDruggraDecontraStrank(
			String userId, String drugGrade) {
		List<SysDruggraDecontraStrank> druggraDecontraStrank = docAdvManageDAO.queryDruggraDecontraStrank(userId, drugGrade);
		return druggraDecontraStrank;
	}

	@Override
	public List<BusinessAdvdrugnature> queryAdvdrugnature(String drugNature) {
		List<BusinessAdvdrugnature> advdrugnature = docAdvManageDAO.queryAdvdrugnature(drugNature);
		return advdrugnature;
	}

	@Override
	public List<InpatientOrderNow> getPage(String page, String rows,
			InpatientOrderNow entity,String recordId) {
		return docAdvManageDAO.getPage(page, rows, entity,recordId);
	}

	@Override
	public int getTotal(InpatientOrderNow entity,String recordId) {
		return docAdvManageDAO.getTotal(entity,recordId);
	}

	@Override
	public List<SysDepartment> queryPage(String page, String rows,SysDepartment entity) {
		return departmentInfoDAO.queryPage(page, rows, entity);
	}

	@Override
	public int queryTotal(SysDepartment entity) {
		return departmentInfoDAO.queryTotal(entity);
	}
	
	@Override
	public int queryAuditInfo(String userId, String parameterCode) {
		List<BusinessDictionary> codeTitle = docAdvManageDAO.queryCodeTitle(userId);
		int a=0; 
		if(codeTitle.size()==0){
			return a;
		}
		String str = parameterInnerService.getparameter(parameterCode);
		if("".equals(str)){
			str = "0,1";
		}
		String[] ary = str.split(",");//调用API方法按照逗号分隔字符串
		
		for(String item: ary){
		   if(item.equals(codeTitle.get(0).getEncode())){
			   a=a+1;   
		   }		  
		}		
		return a;
		
	}

	@Override
	public void updateMoStat(InpatientOrderNow entity) {
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		InpatientOrderNow inpatientOrder = docAdvManageDAO.queryInpatientOrder(entity.getId());
		inpatientOrder.setMoStat(entity.getMoStat());
		if(entity.getMoNote1()!=null && !"".equals(entity.getMoNote1())){
			inpatientOrder.setMoNote1(entity.getMoNote1());
		}else{
			inpatientOrder.setMoNote1("");
		}
		inpatientOrder.setUpdateUser(user.getAccount());
		inpatientOrder.setUpdateTime(DateUtils.getCurrentTime());		
		docAdvManageDAO.update(inpatientOrder);
		OperationUtils.getInstance().conserve(entity.getId(),"医嘱管理","UPDATE","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void delDocAdvInfo(String combNo) {
		List<InpatientOrderNow> inpatientOrderList = docAdvManageDAO.queryInpatientOrderById(null,combNo,null,null);
		for(int i=0;i<inpatientOrderList.size();i++){
			docAdvManageDAO.delInpatientOrder(inpatientOrderList.get(i).getId());
			OperationUtils.getInstance().conserve(inpatientOrderList.get(i).getId(),"医嘱管理","UPDATE","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONDELETE);	
		}			
	}
	/**  
	 *  
	 * @Description： 作废医嘱
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-28 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void obsoleteAdvice(InpatientOrderNow entity,String timeFlag,String stopTime,String advStopReasonId,String advStopReason,String adviceJson) {
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		String [] ids = adviceJson.split(",");
		for (String id : ids) {
			InpatientOrderNow inpatientOrder = docAdvManageDAO.getinpaorder(id);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if("1".equals(timeFlag)){
				try {
					inpatientOrder.setDcDate(dateFormat.parse(stopTime));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if("0".equals(timeFlag)){				
				inpatientOrder.setDcDate(DateUtils.getCurrentTime());
			}
			try {
				inpatientOrder.setDateEnd(dateFormat.parse(stopTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			inpatientOrder.setDcFlag(1);
			inpatientOrder.setMoStat(3);	
			inpatientOrder.setDcCode(advStopReasonId);
			inpatientOrder.setDcName(advStopReason);	
			inpatientOrder.setDcDoccd(user.getAccount());
			inpatientOrder.setDcDocnm(user.getName());
			inpatientOrder.setDcUsercd(user.getId());
			inpatientOrder.setDcUsernm(user.getName());	
			inpatientOrder.setUpdateUser(user.getAccount());
			inpatientOrder.setUpdateTime(DateUtils.getCurrentTime());		
			docAdvManageDAO.update(inpatientOrder);
			OperationUtils.getInstance().conserve(inpatientOrder.getId(),"医嘱管理","UPDATE","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONUPDATE);
		}
	}


	@Override
	public List<BusinessFrequency> queryBusinessFrequency(String frequencyCode) {
		List<BusinessFrequency> businessFrequencyList = docAdvManageDAO.queryBusinessFrequency(frequencyCode);
		return businessFrequencyList;
	}
	
	@Override
	public List<DrugUndrug> queryNoDrugInfo(String id) {
		List<DrugUndrug> drugUndrugList = docAdvManageDAO.queryNoDrugInfo(id);
		return drugUndrugList;
	}

	@Override
	public void updateSpeFreInfo(InpatientOrderNow entity) {
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		InpatientOrderNow inpatientOrder1 = docAdvManageDAO.queryInpatientOrder(entity.getId());
		List<InpatientOrderNow> list=docAdvManageDAO.queryInpatientOrderById(null,inpatientOrder1.getCombNo(),null,null);
		for(InpatientOrderNow inpatientOrder:list){
			inpatientOrder.setExecTimes(entity.getExecTimes());		
			inpatientOrder.setExecDose(entity.getExecDose());	
			inpatientOrder.setUpdateUser(user.getAccount());
			inpatientOrder.setUpdateTime(DateUtils.getCurrentTime());		
			docAdvManageDAO.update(inpatientOrder);
			OperationUtils.getInstance().conserve(entity.getId(),"医嘱管理","UPDATE","T_INPATIENT_ORDER_NOW",OperationUtils.LOGACTIONUPDATE);
		}			
	}

	@Override
	public List<BusinessFrequency> queryFrequency(String frequencyEncode) {
		List<BusinessFrequency> businessFrequencyList = docAdvManageDAO.queryFrequencyByEncode(frequencyEncode);
		return businessFrequencyList;
	}

	@Override
	public List<InpatientOrderNow> queryInpatientOrderById(String id,String combNo,String mainDrug,String subtblFlag) {
		List<InpatientOrderNow> inpatientOrderList = docAdvManageDAO.queryInpatientOrderById(id,combNo,mainDrug,subtblFlag);
		return inpatientOrderList;
	}

	@Override
	public Map<String, String> queryCheckpointMap() {
		return docAdvManageDAO.queryCheckpointMap();
	}
	
	@Override
	public Map<String, String> querySampleTeptMap() {
		return docAdvManageDAO.querySampleTeptMap();
	}

	@Override
	public List<InpatientKind> queryDocAdvType(InpatientKind inpatientKind) {
		return docAdvManageDAO.queryDocAdvType(inpatientKind);
	}

	@Override
	public List<BusinessContractunit> queryReglist() {
		return docAdvManageDAO.queryReglist();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyList() {
		return docAdvManageDAO.queryFrequencyList();
	}

	@Override
	public String reformAdvice(String adviceJson) {
		//医嘱json串转换成list
		List<InpatientOrderNow> orderList = null;
		try {
			adviceJson=adviceJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
					.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
			orderList =JSONUtils.fromJson(adviceJson,  new TypeToken<List<InpatientOrderNow>>(){}, "yyyy-MM-dd hh:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//1.将每条医嘱设置为重整状态，mo_stat=4
		for (InpatientOrderNow inpatientOrderNow : orderList) {
			docAdvManageDAO.updateAdviceList(inpatientOrderNow);
		}
		//2.同时复制一套原医嘱的数据，复制的医嘱中需要记录原医嘱的医嘱号。
		//注意：复制生成的医嘱记录中：医嘱号、组合号、排序号重新生成，记录重整人信息，mark3记录重整前的医嘱号，剩下的信息均来源于原医嘱。医嘱状态不变。
		UUIDGenerator uuid1 = new UUIDGenerator();
		String[] strArr = docAdvManageDAO.getSeqByName("SEQ_ADVICE_GROUPNO",1);
		String comBo = strArr[0];
		Map<String, String> map = new HashMap<String, String>();
		for (InpatientOrderNow inpatientOrderNow : orderList) {
			inpatientOrderNow.setId(null); 
			inpatientOrderNow.setMark3(inpatientOrderNow.getMoOrder());
			inpatientOrderNow.setMoOrder(uuid1.getUUID().substring(0, 15));//医嘱号
			int sortId =0;
			List<InpatientOrderNow> list=docAdvManageDAO.queryMaxInpatientOrderSortId();
			if(list!=null&&list.size()>0){
				if(docAdvManageDAO.queryMaxInpatientOrderSortId().get(0).getSortId()!=null){
					sortId = docAdvManageDAO.queryMaxInpatientOrderSortId().get(0).getSortId();
				}
			}
			inpatientOrderNow.setSortId(sortId+1);//排序号
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession(); 
			inpatientOrderNow.setRecUsercd(user.getAccount());
			inpatientOrderNow.setRecUsernm(user.getName());
			docAdvManageDAO.save(inpatientOrderNow);
		}
		return "success";
	}

	@Override
	public String dispensingSf(String drugCode, String deptCode,String typeCode,String useCode) {
		String flag = "";
		DrugInfo drugInfo = drugInfoInInerDAO.getByCode(drugCode);
		if(drugInfo==null){
			flag = "1";
		}else{
			DrugBilllist billlist = docAdvManageDAO.getListByProperty(deptCode, typeCode,useCode, drugInfo.getDrugType(),drugInfo.getDrugNature(), drugInfo.getDrugDosageform());
			if(billlist==null||billlist.getDrugBillclass()==null||billlist.getDrugBillclass().getBillclassCode()==null){
				BusinessDictionary drugType = innerCodeDao.getDictionaryByCode("drugType",drugInfo.getDrugType());//药品类型
				BusinessDictionary dosageForm = innerCodeDao.getDictionaryByCode("dosageForm",drugInfo.getDrugDosageform());//剂型
				BusinessDictionary drugQuality = innerCodeDao.getDictionaryByCode("drugProperties",drugInfo.getDrugNature());//药品性质
				BusinessDictionary useCode1 = innerCodeDao.getDictionaryByCode("useage",useCode);//药品用法
				InpatientKind typeName=inpatientKindInInterDAO.getByCode(typeCode);//医嘱类型
				flag = "对应的摆药单未进行设置! 请与药学部或信息科联系。"
						+ " \n医嘱类型:" + (typeName!=null?typeName.getTypeName():"医嘱类型异常") 
						+ " \n药品类型:" + (drugType!=null?drugType.getName():"药品类型异常") 
						+ " \n用法:" + (useCode1!=null?useCode1.getName():"用法异常")
						+ " \n药品性质:" + (drugQuality!=null?drugQuality.getName():"药品性质异常")
						+ " \n药品剂型:" + (dosageForm!=null?dosageForm.getName():"药品剂型异常");
			}else{
				flag = "success";
			}
		}
		return flag;
	}

	@Override
	public Map<String, Object> getoutPatient(String startTime, String endTime,
			String condition, String type,String page,String rows) {
		Map<String,Object> retMap = null;
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
			//5.定义常量
			List<String> tnL = new ArrayList<String>();//挂号分区
			List<String> mainL=new ArrayList<String>();//门诊处方
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL= ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",startTime,endTime);
					mainL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_RECIPEDETAIL",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获取时间差（年）
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
					//获取相差年份的分区集合 
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					mainL=ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_RECIPEDETAIL",yNum+1);
					tnL.add(0,"T_REGISTER_MAIN_NOW");
					mainL.add(0,"T_OUTPATIENT_RECIPEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_REGISTER_MAIN_NOW");
				mainL.add(0,"T_OUTPATIENT_RECIPEDETAIL_NOW");
			}
			retMap = docAdvManageDAO.getoutPatient(tnL,mainL,startTime, endTime, condition, type, page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RegistrationNow>());
		}
		
		return retMap;
	}

	@Override
	public List<OutpatientRecipedetailNow> queryOutpatientRecipedetail(
			 String clinicCode,String startTime,String endTime) {
		List<OutpatientRecipedetailNow> recipedetailNows = null;
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
			//5.定义常量
			List<String> tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_RECIPEDETAIL",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获取时间差（年）
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
					//获取相差年份的分区集合 
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_RECIPEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_RECIPEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_RECIPEDETAIL_NOW");
			}
			recipedetailNows = docAdvManageDAO.queryOutpatientRecipedetail(tnL, clinicCode, startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
			recipedetailNows = new ArrayList<OutpatientRecipedetailNow>();
		}
		
		return recipedetailNows;
	}

	@Override
	public List<AdviceLong> printAdviceLong(String inpatientNo,String flag) {
		return docAdvManageDAO.printAdviceLong(inpatientNo,flag);
	}

	@Override
	public List<AdviceLong> printAdvicehis(String inpatientNo, String flag) {
		return docAdvManageDAO.printAdvicehis(inpatientNo, flag);
	}
}
