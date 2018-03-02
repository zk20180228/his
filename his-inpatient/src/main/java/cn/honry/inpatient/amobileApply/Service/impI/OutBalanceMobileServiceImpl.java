package cn.honry.inpatient.amobileApply.Service.impI;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.drug.applyout.service.ApplyoutInInterService;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.inpatient.feeInfo.dao.InpatientFeeInfoInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.inpatient.amobileApply.Service.OutBalanceMobileService;
import cn.honry.inpatient.auditDrug.dao.AuditDao;
import cn.honry.inpatient.auditDrug.vo.DrugVo;
import cn.honry.inpatient.costDerate.dao.CostDerateDao;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceHeadDAO;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceListDAO;
import cn.honry.inpatient.outBalance.dao.InpatientBalancePayListDAO;
import cn.honry.inpatient.outBalance.dao.InpatientChangeprepayDAO;
import cn.honry.inpatient.outBalance.dao.InpatientInPrepayDAO;
import cn.honry.inpatient.outBalance.dao.InpatientMedicineDAO;
import cn.honry.inpatient.outBalance.dao.InpatientShiftDAO;
import cn.honry.inpatient.outBalance.dao.OutBalanceDAO;
import cn.honry.inpatient.outBalance.dao.impl.InpatientItemDAOImpl;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;

import com.google.gson.reflect.TypeToken;
@Service("outBalanceMobileService")
@Transactional
public class OutBalanceMobileServiceImpl implements OutBalanceMobileService{
	@Autowired
	@Qualifier(value = "outBalanceDAO")
	private OutBalanceDAO outBalanceDAO;
	@Autowired
	@Qualifier(value = "inpatientInfoDAO")
	private InpatientInfoDAO inpatientInfoDAO;
	@Autowired
	@Qualifier(value = "inpatientInPrepayDAO")
	private InpatientInPrepayDAO inpatientInPrepayDAO;
	@Autowired
	@Qualifier(value = "inpatientChangeprepayDAO")
	private InpatientChangeprepayDAO inpatientChangeprepayDAO;
	@Autowired
	@Qualifier(value = "inpatientBalancePayListDAO")
	private InpatientBalancePayListDAO inpatientBalancePayListDAO;
	@Autowired
	@Qualifier(value = "costDerateDao")
	private CostDerateDao costDerateDao;
	@Autowired
	@Qualifier(value = "inpatientBalanceHeadDAO")
	private InpatientBalanceHeadDAO inpatientBalanceHeadDAO;
	@Autowired
	@Qualifier(value = "inpatientBalanceListDAO")
	private InpatientBalanceListDAO inpatientBalanceListDAO;
	@Autowired
	@Qualifier(value = "inpatientFeeInfoInInterDAO")
	private InpatientFeeInfoInInterDAO inpatientFeeInfoInInterDAO;
	@Autowired
	@Qualifier(value = "inpatientMedicineDAO")
	private InpatientMedicineDAO inpatientMedicineDAO;
	@Autowired
	@Qualifier(value = "inpatientItemDAO")
	private InpatientItemDAOImpl inpatientItemDAO;
	@Autowired
	@Qualifier(value = "inpatientShiftDAO")
	private InpatientShiftDAO inpatientShiftDAO;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "applyoutInInterService")
	private ApplyoutInInterService applyoutInInterService;
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService infoService;
	//注入本类dao
	@Autowired
	@Qualifier(value = "auditDao")
	private AuditDao auditDao;
	/***
	 * 注入系统参数的service
	 * 调用接口
	 */
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}
	@Override
	public void removeUnused(String id) {
		
	}
   
	@Override
	public InpatientInfo get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientInfo entity) {
	}
	
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;

	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier(value="userInInterService")
	private UserInInterService userInInterService;
	
	public void setUserInInterService(UserInInterService userInInterService) {
		this.userInInterService = userInInterService;
	}
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	/**获取当前院区接口**/
	@Autowired
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}
	@Override
	public String saveBalance(Map<String, String> parameterMap, String zfJson,String costJson,String empJobNo,String deptCode,String empJobName,String deptName) throws Exception { 
		String msg = "";
		//14.修改发票
		String resu = updateInvoiceNo(empJobNo,parameterMap.get("invoiceNo"));
		if("success".equals(resu)){
		}else{
			msg="修改发票失败！";
			return msg;
		}
		List<InpatientMedicineListNow> inpatientMedicineLists = outBalanceDAO.queryInpatientMedicineList(parameterMap.get("inpatientNo"));
		List<InpatientItemListNow> inpatientItemLists = outBalanceDAO.queryInpatientItemList(parameterMap.get("inpatientNo"));
		
		Date balanceDate = DateUtils.getCurrentTime();//结算时间
		Integer hospatialId=HisParameters.CURRENTHOSPITALID;
		String areaCode="";
		if(StringUtils.isNotBlank(deptCode)){
			areaCode=inprePayService.getDeptArea(deptCode);
		}
		//1.设置系统参数，住院日结后是否可以继续收费
		String rj = parameterInnerDAO.getParameterByCode("Endofday");
		if("".equals(rj)){
			rj = "1";
		}
		if("0".equals(rj)){
			throw new RuntimeException("住院日结后不可以继续收费！");
		}else{
			//2.判断是否存在未确认的退费申请！
			List<InpatientCancelitemNow> Cancelitemlist = outBalanceDAO.getInpatientCancelitem(parameterMap.get("inpatientNo"));
			if(Cancelitemlist.size()>0){
				throw new RuntimeException("存在未确认的退费申请！");
			}else{
				//3.判断是否存在未确认的退药单！
				List<OutpatientDrugcontrol> controlList = outBalanceDAO.queryDrugcontrol(parameterMap.get("inpatientNo"));
				if(controlList.size()>0){
					throw new RuntimeException("存在未确认的退药单！");
				}else{
					//4.重新获取患者信息，和页面的患者信息进行对比，判断患者清空是否发生变化
					List<InpatientInfoNow> InfoList = outBalanceDAO.InpatientInfoqueryFee(parameterMap.get("inpatientNo"));
					if(!"B".equals(InfoList.get(0).getInState())&&!"P".equals(InfoList.get(0).getInState())){
						throw new RuntimeException("患者不是出院登记状态，不能进行出院结算！");
					}else{
						//解析费用汇总数据
						List<InpatientFeeInfoNow> feeList=new ArrayList<InpatientFeeInfoNow>();
						try {
							costJson=costJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
									.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
									.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
							feeList = JSONUtils.fromJson(costJson,  new TypeToken<List<InpatientFeeInfoNow>>(){}, "yyyy-MM-dd hh:mm:ss");
						} catch (Exception e) {
							throw new RuntimeException("解析费用汇总数据出错！");
						}
						//5.将患者未结算的预交金信息（预交金列表中除转入预交金外）设置为结算状态，记录发票号、结算序号、结算时间、结算人

						String prepayIds = parameterMap.get("prepayIds");
						if(StringUtils.isBlank(prepayIds)){
						}else{
							String [] prepayId = prepayIds.split(",");
							for (int i = 0; i < prepayId.length; i++) {
								InpatientInPrepayNow prepay = outBalanceDAO.queryInpatientInPrepayById(prepayId[i]);
								prepay.setBalanceDate(balanceDate);//结算时间
								prepay.setBalanceState(1);//结算状态
								prepay.setBalanceOpercode(empJobNo);//结算人
								prepay.setBalanceOpername(empJobName);
								prepay.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
								prepay.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
								outBalanceDAO.save(prepay);
							}
						}
						
						//7.生成相应支付方式的结算记录，结算状态、结算时间取当前时间、结算人为当前人、结算序号取生成的结算序号、发票号取领取的发票号
						List<InpatientBalancePayNow> modelList=new ArrayList<InpatientBalancePayNow>();
						try {
							zfJson=zfJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
									.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
									.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
							modelList = JSONUtils.fromJson(zfJson,  new TypeToken<List<InpatientBalancePayNow>>(){}, "yyyy-MM-dd hh:mm:ss");
						} catch (Exception e) {
							throw new RuntimeException("解析结算实付表时出错！");
						}
						for (InpatientBalancePayNow entity : modelList) {
							if (entity != null) {
								entity.setId(null);//id
								entity.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
								entity.setTransType(1);//正交易
								entity.setTransKind(1);//交易种类 0 预交款 1 结算款
								entity.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
								entity.setReutrnorsupplyFlag(1);//返回或补收标志 1 补收 2 返还
								entity.setBalanceOpercode(empJobNo);//结算人代码
								entity.setBalanceDate(balanceDate);//结算时间
								entity.setCreateUser(empJobNo);
								entity.setCreateTime(new Date());
								entity.setUpdateUser(empJobNo);
								entity.setUpdateTime(new Date());
								entity.setCreateDept(empJobNo);
								entity.setHospitalId(hospatialId);
								entity.setAreaCode(areaCode);
								inpatientBalancePayListDAO.save(entity);
							}
						}
						List<InpatientFeeInfoNow> InpatientFeeInfoList = outBalanceDAO.inpatientFeeInfoFee(parameterMap.get("inpatientNo"));
						//8.减免金额  如果未修改   将减免的费用设置为结算状态，记录发票号和结算序号    如果修改添加减免一条记录
						//根据系统参数设置是否可修改减免金额
						String value = parameterInnerDAO.getParameterByCode("UpdateDerate");
						if("".equals(value)){
							value = "2";
						}
						if("1".equals(value)){//可修改 
							List<InpatientDerate> derateList = outBalanceDAO.getclinicNo(parameterMap.get("inpatientNo"),null,null);
							Double derateCost = 0.00;
							if(derateList.get(0).getDerateCost()==null){
								derateCost = 0.00;
							}else{
								derateCost = derateList.get(0).getDerateCost();
							}
							if(derateCost.equals(Double.valueOf(parameterMap.get("jmMoney")))){
								List<InpatientDerate> List= outBalanceDAO.getDerate(parameterMap.get("inpatientNo"));
								for (InpatientDerate inpatientDerate : List) {
									inpatientDerate.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
									inpatientDerate.setInvoiceNo(parameterMap.get("invoiceNo"));
									outBalanceDAO.save(inpatientDerate);
								}
							}else{
								
								InpatientDerate inpatient = outBalanceDAO.maxHappon(parameterMap.get("inpatientNo"));//获取患者减免表中最大的发生序号
								Integer happenNo = 1;
								if(inpatient.getHappenNo()==null||inpatient.getHappenNo()==0){
									happenNo = 1;
								}else{
									happenNo = inpatient.getHappenNo();
								}
								for (InpatientFeeInfoNow inpatientFeeInfo : InpatientFeeInfoList) {
									InpatientDerate inpatientDerate = new InpatientDerate();
									inpatientDerate.setId(null);
									inpatientDerate.setTransType(1);//交易类型
									inpatientDerate.setClinicNo(parameterMap.get("inpatientNo"));//流水号
									inpatientDerate.setHappenNo(happenNo);//发生序号
									inpatientDerate.setDerateKind("1");//减免种类
									inpatientDerate.setRecipeNo(inpatientFeeInfo.getRecipeNo());//处方号
									inpatientDerate.setFeeCode(inpatientFeeInfo.getFeeCode());//最小费用代码
									Double OwnCost = (inpatientFeeInfo.getOwnCost()/Double.valueOf(parameterMap.get("zfMoney")))*inpatientFeeInfo.getOwnCost();
									inpatientDerate.setDerateCost(OwnCost);//减免金额
									inpatientDerate.setDerateCause("结算减免");//减免原因
									inpatientDerate.setDerateType("3");//减免类型
									inpatientDerate.setBalanceState("1");//结算状态
									inpatientDerate.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
									inpatientDerate.setDeptCode(deptCode);//减免科室
									inpatientDerate.setCreateUser(empJobNo);
									inpatientDerate.setCreateTime(new Date());
									inpatientDerate.setUpdateUser(empJobNo);
									inpatientDerate.setUpdateTime(new Date());
									happenNo=happenNo+1;
									costDerateDao.save(inpatientDerate);
								}
							}
						}else{//不可修改        
							List<InpatientDerate> List= outBalanceDAO.getDerate(parameterMap.get("inpatientNo"));
							for (InpatientDerate inpatientDerate : List) {
								inpatientDerate.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
								inpatientDerate.setInvoiceNo(parameterMap.get("invoiceNo"));
								outBalanceDAO.save(inpatientDerate);
							}
						}
						
						//9.生成结算头表记录
						Double totCost = 0.00;//费用金额
						Double ownCost = 0.00;//自费金额
						Double payCost = 0.00;//自付金额
						Double pubCost = 0.00;//公费金额
						Double ecoCost = 0.00;//优惠金额
						for (InpatientFeeInfoNow inpatientFeeInfo : feeList) {
							totCost+=inpatientFeeInfo.getTotCost();
							ownCost+=inpatientFeeInfo.getOwnCost();
							payCost+=inpatientFeeInfo.getPayCost();
							pubCost+=inpatientFeeInfo.getPubCost();
							ecoCost+=inpatientFeeInfo.getEcoCost();
						}
						
						InpatientBalanceHeadNow Head=new InpatientBalanceHeadNow();
						Head.setId(null);//id
						Head.setPaykindCode(InfoList.get(0).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
						Head.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号码
						Head.setTransType(1);//交易类型 
						Head.setInpatientNo(parameterMap.get("inpatientNo")); //流水号
						Head.setName(InfoList.get(0).getPatientName()); //姓名
						Head.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						Head.setPactCode(InfoList.get(0).getPactCode());//合同代码
						Head.setPrepayCost(Double.valueOf(parameterMap.get("yj")));//预交金额
						Head.setChangePrepaycost(null);//转入预交金额
						Head.setTotCost(totCost);//费用金额
						Head.setOwnCost(ownCost);//自费金额
						Head.setPayCost(payCost);//自付金额
						Head.setPubCost(pubCost);//公费金额
						Head.setEcoCost(ecoCost);//优惠金额
						Head.setDerCost(Double.valueOf(parameterMap.get("jmMoney")));//减免金额
						Head.setWasteFlag(1);//扩展标志1
						String sh1 = parameterMap.get("sh1");
						if("".equals(sh1)){
							Head.setSupplyCost(Double.valueOf(parameterMap.get("sh")));//补收金额
						}
						if("".equals(parameterMap.get("sh"))){
							Head.setReturnCost(Double.valueOf(parameterMap.get("sh1")));//返还金额
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						try {
							Date inDate = sdf.parse(parameterMap.get("inDate"));
							Date outDate = new Date();
							Head.setBeginDate(inDate);//起始日期
							Head.setEndDate(outDate);//终止日期
						} catch (ParseException e) {
							throw new RuntimeException("处理时间时出错！");
						}
						Head.setBalanceType(2);//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
						Head.setBalanceOpercode(empJobNo);//结算人代码
						Head.setBalanceOpername(empJobName);//结算人
						Head.setBalanceDate(balanceDate);//结算时间
						Head.setBalanceoperDeptcode(deptCode);//结算员科室
						Head.setBalanceoperDeptname(deptName);//结算员科室
						Head.setCheckFlag(0);//0未核查/1已核查
						Head.setCreateUser(empJobNo);
						Head.setCreateTime(new Date());
						Head.setUpdateUser(empJobNo);
						Head.setUpdateTime(new Date());
						Head.setCreateDept(deptCode);
						Head.setAreaCode(areaCode);
						Head.setHospitalId(hospatialId);
						inpatientBalanceHeadDAO.save(Head);
						
						List<MinfeeStatCode> minfeeStatCodelist = outBalanceDAO.queryMinfeeStatCode(null);
						Map<String, String> map = new HashMap<String, String>();
						if(minfeeStatCodelist!=null){
							for (MinfeeStatCode minfeeStatCode : minfeeStatCodelist) {
								map.put(minfeeStatCode.getFeeStatCode()+"-"+minfeeStatCode.getFeeStatName()+"-"+minfeeStatCode.getMinfeeCode(),minfeeStatCode.getMinfeeCode());
							}
						}
						//10.生成结算明细表
						for (InpatientFeeInfoNow inpatientFeeInfo : feeList) {
							InpatientBalanceListNow inpatientBalanceList = new InpatientBalanceListNow();
							inpatientBalanceList.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号码
							inpatientBalanceList.setTransType(1);//交易类型,1正交易，2反交易
							inpatientBalanceList.setName(InfoList.get(0).getPatientName()); //姓名
							inpatientBalanceList.setInpatientNo(parameterMap.get("inpatientNo"));//住院流水号
							inpatientBalanceList.setPaykindCode(InfoList.get(0).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
							inpatientBalanceList.setPactCode(InfoList.get(0).getPactCode());//合同单位
							inpatientBalanceList.setDeptCode(InfoList.get(0).getDeptCode());//在院科室
							
							String key = getKeyByValue(map, inpatientFeeInfo.getFeeCode());
							String [] codes = key.split("-");
							for (int j = 0; j < codes.length; j++) {
								inpatientBalanceList.setStatCode(codes[0]);//统计大类
								inpatientBalanceList.setStatName(codes[1]);//统计大类名称
							}
							inpatientBalanceList.setTotCost(inpatientFeeInfo.getTotCost());//费用金额
							inpatientBalanceList.setOwnCost(inpatientFeeInfo.getOwnCost());//自费金额
							inpatientBalanceList.setPayCost(inpatientFeeInfo.getPayCost());//自付金额
							inpatientBalanceList.setPubCost(inpatientFeeInfo.getPubCost());//公费金额
							inpatientBalanceList.setEcoCost(inpatientFeeInfo.getEcoCost());//优惠金额
							inpatientBalanceList.setBalanceOpercode(empJobNo);//结算人代码
							inpatientBalanceList.setBalanceDate(balanceDate);//结算时间
							inpatientBalanceList.setBalanceType(3);//结算类型 1:在院结算，2:转科结算，3:出院结算，4:重结算 5:结转
							inpatientBalanceList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							inpatientBalanceList.setBalanceoperDeptcode(deptCode);//结算员科室
							inpatientBalanceList.setBalanceoperDeptname(deptName);//结算员科室
							inpatientBalanceList.setCreateUser(empJobNo);
							inpatientBalanceList.setCreateTime(new Date());
							inpatientBalanceList.setUpdateUser(empJobNo);
							inpatientBalanceList.setUpdateTime(new Date());
							inpatientBalanceList.setHospitalId(hospatialId);
							inpatientBalanceList.setAreaCode(areaCode);
							inpatientBalanceListDAO.save(inpatientBalanceList);
						}
						
						//11.根据页面的预交金信息生成一条交易种类为预交款、支付方式为现金的补收状态住院收费结算实付记录
						InpatientBalancePayNow model=new InpatientBalancePayNow();
						model.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
						model.setTransType(1);//正交易
						model.setTransKind(0);//交易种类 0 预交款 1 结算款
						model.setPayWay("SB");//支付方式  现金
						model.setChangePrepaycost(Double.valueOf(parameterMap.get("yj")));//预交金
						model.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						model.setReutrnorsupplyFlag(1);//返回或补收标志 1 补收 2 返还
						model.setBalanceOpercode(empJobNo);//结算人代码
						model.setBalanceDate(balanceDate);//结算时间
						model.setCreateUser(empJobNo);
						model.setCreateTime(new Date());
						model.setUpdateUser(empJobNo);
						model.setUpdateTime(new Date());
						model.setHospitalId(hospatialId);
						model.setAreaCode(areaCode);
						inpatientBalancePayListDAO.save(model);
						
						//12.更新住院费用汇总信息为结算状态，记录结算序号、结算时间、结算操作人，发票号
						for (InpatientFeeInfoNow fee : InpatientFeeInfoList) {
							fee.setBalanceState(1);//结算状态
							fee.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							fee.setBalanceDate(balanceDate);//结算时间
							fee.setBalanceOpercode(empJobNo);//结算操作人
							fee.setInvoiceNo(parameterMap.get("invoiceNo"));
							outBalanceDAO.save(fee);
						}
						
						//13.更新住院药品、非药品费用明细为结算状态、记录结算序号及发票号
						if(inpatientMedicineLists.size()>0){
							for (InpatientMedicineListNow inpatientMedicineList : inpatientMedicineLists) {
								inpatientMedicineList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
								inpatientMedicineList.setBalanceState(1);
								inpatientMedicineList.setInvoiceNo(parameterMap.get("invoiceNo"));
								inpatientMedicineList.setUpdateTime(new Date());
								inpatientMedicineList.setUpdateUser(empJobNo);
								inpatientMedicineDAO.save(inpatientMedicineList);
							}
						}
						if(inpatientItemLists.size()>0){
							for (InpatientItemListNow inpatientItemList : inpatientItemLists) {
								inpatientItemList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
								inpatientItemList.setBalanceState(1);
								inpatientItemList.setInvoiceNo(parameterMap.get("invoiceNo"));
								inpatientItemList.setUpdateTime(new Date());
								inpatientItemList.setUpdateUser(empJobNo);
								inpatientMedicineDAO.save(inpatientItemList);
							}
						}
						
						//14.修改发票
						//String resu = updateInvoiceNo(userId,parameterMap.get("invoiceNo"));
						if("success".equals(resu)){
						}else{
							throw new RuntimeException("修改发票失败！");
						}
						//15.保存资料变更表
						InpatientShiftData inpatientShiftData =new InpatientShiftData();
						inpatientShiftData.setClinicNo(parameterMap.get("inpatientNo"));//住院流水号
						inpatientShiftData.setShiftType("BA");//变更类型
							//查询最大的发生序号
						InpatientShiftData inpatientShift= outBalanceDAO.queryMaxHappon(parameterMap.get("inpatientNo"));
						Integer  happenNo = 1;
						if(inpatientShift.getHappenNo()==null){
							inpatientShiftData.setHappenNo(happenNo);//发生序号
						}else{
							inpatientShiftData.setHappenNo(inpatientShift.getHappenNo()+1);//发生序号
						}
						inpatientShiftDAO.save(inpatientShiftData);
						
						//16.对患者进行开账和修改结算序号
				    	for (InpatientInfoNow info1 : InfoList) {
				    		info1.setInState("O");
				    		info1.setStopAcount(0);
				    		info1.setBalanceNo(info1.getBalanceNo()+1);
				    		info1.setBalanceDate(new Date());
				    		info1.setTotCost(info1.getTotCost()-totCost);//费用金额
				    		info1.setOwnCost(info1.getOwnCost()-ownCost);//自费金额
				    		info1.setPayCost(info1.getPayCost()-payCost);//自付金额
				    		info1.setPubCost(info1.getPubCost()-pubCost);//公费金额
				    		info1.setEcoCost(info1.getEcoCost()-ecoCost);//优惠金额
				    		info1.setBalancePrepay(info1.getBalancePrepay()+Double.valueOf(parameterMap.get("yj")));
				    		inpatientInfoDAO.save(info1);
						}
					}
				}
			}	
		}
 		return "success";
	}
	@Override
	public String saveBalanceZhongtu(Map<String, String> parameterMap, String zfJson,String costJson,String empJobNo,String deptCode,String empJobName,String deptName) throws Exception { 
		String msg = "";
		//14.修改发票
		String resu = updateInvoiceNo(empJobNo,parameterMap.get("invoiceNo"));
		if("success".equals(resu)){
		}else{
			msg="修改发票失败！";
			return msg;
		}
		List<InpatientMedicineListNow> inpatientMedicineLists = outBalanceDAO.queryInpatientMedicineList(parameterMap.get("inpatientNo"));
		List<InpatientItemListNow> inpatientItemLists = outBalanceDAO.queryInpatientItemList(parameterMap.get("inpatientNo"));
		
		Date balanceDate = DateUtils.getCurrentTime();//结算时间
		Integer hospatialId=HisParameters.CURRENTHOSPITALID;
		String areaCode="";
		if(StringUtils.isNotBlank(deptCode)){
			areaCode=inprePayService.getDeptArea(deptCode);
		}
		//1.设置系统参数，住院日结后是否可以继续收费
		String rj = parameterInnerDAO.getParameterByCode("Endofday");
		if("".equals(rj)){
			rj = "1";
		}
		if("0".equals(rj)){
			throw new RuntimeException("住院日结后不可以继续收费！");
		}else{
			//2.判断是否存在未确认的退费申请！
			List<InpatientCancelitemNow> Cancelitemlist = outBalanceDAO.getInpatientCancelitem(parameterMap.get("inpatientNo"));
			if(Cancelitemlist.size()>0){
				throw new RuntimeException("存在未确认的退费申请！");
			}else{
				//3.判断是否存在未确认的退药单！
				List<OutpatientDrugcontrol> controlList = outBalanceDAO.queryDrugcontrol(parameterMap.get("inpatientNo"));
				if(controlList.size()>0){
					throw new RuntimeException("存在未确认的退药单！");
				}else{
					//4.重新获取患者信息，和页面的患者信息进行对比，判断患者清空是否发生变化
					List<InpatientInfoNow> InfoList = outBalanceDAO.InpatientInfoqueryFee(parameterMap.get("inpatientNo"));
					//解析费用汇总数据
					List<InpatientFeeInfoNow> feeList=new ArrayList<InpatientFeeInfoNow>();
					try {
						costJson=costJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
								.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
								.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
						feeList = JSONUtils.fromJson(costJson,  new TypeToken<List<InpatientFeeInfoNow>>(){}, "yyyy-MM-dd hh:mm:ss");
					} catch (Exception e) {
						throw new RuntimeException("解析费用汇总数据出错！");
					}
					//5.将患者未结算的预交金信息（预交金列表中除转入预交金外）设置为结算状态，记录发票号、结算序号、结算时间、结算人

					String prepayIds = parameterMap.get("prepayIds");
					if(StringUtils.isBlank(prepayIds)){
					}else{
						String [] prepayId = prepayIds.split(",");
						for (int i = 0; i < prepayId.length; i++) {
							InpatientInPrepayNow prepay = outBalanceDAO.queryInpatientInPrepayById(prepayId[i]);
							prepay.setBalanceDate(balanceDate);//结算时间
							prepay.setBalanceState(1);//结算状态
							prepay.setBalanceOpercode(empJobNo);//结算人
							prepay.setBalanceOpername(empJobName);
							prepay.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							prepay.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
							outBalanceDAO.save(prepay);
						}
					}
					
					//7.生成相应支付方式的结算记录，结算状态、结算时间取当前时间、结算人为当前人、结算序号取生成的结算序号、发票号取领取的发票号
					List<InpatientBalancePayNow> modelList=new ArrayList<InpatientBalancePayNow>();
					try {
						zfJson=zfJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
								.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
								.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
						modelList = JSONUtils.fromJson(zfJson,  new TypeToken<List<InpatientBalancePayNow>>(){}, "yyyy-MM-dd hh:mm:ss");
					} catch (Exception e) {
						throw new RuntimeException("解析结算实付表时出错！");
					}
					for (InpatientBalancePayNow entity : modelList) {
						if (entity != null) {
							entity.setId(null);//id
							entity.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
							entity.setTransType(1);//正交易
							entity.setTransKind(1);//交易种类 0 预交款 1 结算款
							entity.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							entity.setReutrnorsupplyFlag(1);//返回或补收标志 1 补收 2 返还
							entity.setBalanceOpercode(empJobNo);//结算人代码
							entity.setBalanceDate(balanceDate);//结算时间
							entity.setCreateUser(empJobNo);
							entity.setCreateTime(new Date());
							entity.setUpdateUser(empJobNo);
							entity.setUpdateTime(new Date());
							entity.setHospitalId(hospatialId);
							entity.setAreaCode(areaCode);
							inpatientBalancePayListDAO.save(entity);
						}
					}
					List<InpatientFeeInfoNow> InpatientFeeInfoList = outBalanceDAO.inpatientFeeInfoFee(parameterMap.get("inpatientNo"));
					
					//9.生成结算头表记录
					Double totCost = 0.00;//费用金额
					Double ownCost = 0.00;//自费金额
					Double payCost = 0.00;//自付金额
					Double pubCost = 0.00;//公费金额
					Double ecoCost = 0.00;//优惠金额
					for (InpatientFeeInfoNow inpatientFeeInfo : feeList) {
						totCost+=inpatientFeeInfo.getTotCost();
						ownCost+=inpatientFeeInfo.getOwnCost();
						payCost+=inpatientFeeInfo.getPayCost();
						pubCost+=inpatientFeeInfo.getPubCost();
						ecoCost+=inpatientFeeInfo.getEcoCost();
					}
					
					InpatientBalanceHeadNow Head=new InpatientBalanceHeadNow();
					Head.setId(null);//id
					Head.setPaykindCode(InfoList.get(0).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
					Head.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号码
					Head.setTransType(1);//交易类型 
					Head.setInpatientNo(parameterMap.get("inpatientNo")); //流水号
					Head.setName(InfoList.get(0).getPatientName()); //姓名
					Head.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
					Head.setPactCode(InfoList.get(0).getPactCode());//合同代码
					Head.setPrepayCost(Double.valueOf(parameterMap.get("yj")));//预交金额
					Head.setChangePrepaycost(null);//转入预交金额
					Head.setTotCost(totCost);//费用金额
					Head.setOwnCost(ownCost);//自费金额
					Head.setPayCost(payCost);//自付金额
					Head.setPubCost(pubCost);//公费金额
					Head.setEcoCost(ecoCost);//优惠金额
					Head.setDerCost(Double.valueOf(parameterMap.get("jmMoney")));//减免金额
					Head.setWasteFlag(1);//扩展标志1
					String sh1 = parameterMap.get("sh1");
					if("".equals(sh1)){
						Head.setSupplyCost(Double.valueOf(parameterMap.get("sh")));//补收金额
					}
					if("".equals(parameterMap.get("sh"))){
						Head.setReturnCost(Double.valueOf(parameterMap.get("sh1")));//返还金额
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					try {
						Date inDate = sdf.parse(parameterMap.get("inDate"));
						Date outDate = new Date();
						Head.setBeginDate(inDate);//起始日期
						Head.setEndDate(outDate);//终止日期
					} catch (ParseException e) {
						throw new RuntimeException("处理时间时出错！");
					}
					Head.setBalanceType(2);//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
					Head.setBalanceOpercode(empJobNo);//结算人代码
					Head.setBalanceOpername(empJobName);//结算人
					Head.setBalanceDate(balanceDate);//结算时间
					Head.setBalanceoperDeptcode(deptCode);//结算员科室
					Head.setBalanceoperDeptname(deptName);//结算员科室
					Head.setCheckFlag(0);//0未核查/1已核查
					Head.setCreateUser(empJobNo);
					Head.setCreateTime(new Date());
					Head.setUpdateUser(empJobNo);
					Head.setUpdateTime(new Date());
					Head.setHospitalId(hospatialId);
					Head.setAreaCode(areaCode);
					inpatientBalanceHeadDAO.save(Head);
					
					List<MinfeeStatCode> minfeeStatCodelist = outBalanceDAO.queryMinfeeStatCode(null);
					Map<String, String> map = new HashMap<String, String>();
					if(minfeeStatCodelist!=null){
						for (MinfeeStatCode minfeeStatCode : minfeeStatCodelist) {
							map.put(minfeeStatCode.getFeeStatCode()+"-"+minfeeStatCode.getFeeStatName()+"-"+minfeeStatCode.getMinfeeCode(),minfeeStatCode.getMinfeeCode());
						}
					}
					//10.生成结算明细表
					for (InpatientFeeInfoNow inpatientFeeInfo : feeList) {
						InpatientBalanceListNow inpatientBalanceList = new InpatientBalanceListNow();
						inpatientBalanceList.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号码
						inpatientBalanceList.setTransType(1);//交易类型,1正交易，2反交易
						inpatientBalanceList.setName(InfoList.get(0).getPatientName()); //姓名
						inpatientBalanceList.setInpatientNo(parameterMap.get("inpatientNo"));//住院流水号
						inpatientBalanceList.setPaykindCode(InfoList.get(0).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
						inpatientBalanceList.setPactCode(InfoList.get(0).getPactCode());//合同单位
						inpatientBalanceList.setDeptCode(InfoList.get(0).getDeptCode());//在院科室
						
						String key = getKeyByValue(map, inpatientFeeInfo.getFeeCode());
						String [] codes = key.split("-");
						for (int j = 0; j < codes.length; j++) {
							inpatientBalanceList.setStatCode(codes[0]);//统计大类
							inpatientBalanceList.setStatName(codes[1]);//统计大类名称
						}
						inpatientBalanceList.setTotCost(inpatientFeeInfo.getTotCost());//费用金额
						inpatientBalanceList.setOwnCost(inpatientFeeInfo.getOwnCost());//自费金额
						inpatientBalanceList.setPayCost(inpatientFeeInfo.getPayCost());//自付金额
						inpatientBalanceList.setPubCost(inpatientFeeInfo.getPubCost());//公费金额
						inpatientBalanceList.setEcoCost(inpatientFeeInfo.getEcoCost());//优惠金额
						inpatientBalanceList.setBalanceOpercode(empJobNo);//结算人代码
						inpatientBalanceList.setBalanceDate(balanceDate);//结算时间
						inpatientBalanceList.setBalanceType(3);//结算类型 1:在院结算，2:转科结算，3:出院结算，4:重结算 5:结转
						inpatientBalanceList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						inpatientBalanceList.setBalanceoperDeptcode(deptCode);//结算员科室
						inpatientBalanceList.setCreateUser(empJobNo);
						inpatientBalanceList.setCreateTime(new Date());
						inpatientBalanceList.setUpdateUser(empJobNo);
						inpatientBalanceList.setUpdateTime(new Date());
						inpatientBalanceList.setHospitalId(hospatialId);
						inpatientBalanceList.setAreaCode(areaCode);
						inpatientBalanceListDAO.save(inpatientBalanceList);
					}
					
					//11.根据页面的预交金信息生成一条交易种类为预交款、支付方式为现金的补收状态住院收费结算实付记录
					InpatientBalancePayNow model=new InpatientBalancePayNow();
					model.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
					model.setTransType(1);//正交易
					model.setTransKind(0);//交易种类 0 预交款 1 结算款
					model.setPayWay("SB");//支付方式  现金
					model.setChangePrepaycost(Double.valueOf(parameterMap.get("yj")));//预交金
					model.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
					model.setReutrnorsupplyFlag(1);//返回或补收标志 1 补收 2 返还
					model.setBalanceOpercode(empJobNo);//结算人代码
					model.setBalanceDate(balanceDate);//结算时间
					model.setCreateUser(empJobNo);
					model.setCreateTime(new Date());
					model.setUpdateUser(empJobNo);
					model.setUpdateTime(new Date());
					model.setAreaCode(areaCode);
					model.setHospitalId(hospatialId);
					inpatientBalancePayListDAO.save(model);
					
					//12.更新住院费用汇总信息为结算状态，记录结算序号、结算时间、结算操作人，发票号
					for (InpatientFeeInfoNow fee : InpatientFeeInfoList) {
						fee.setBalanceState(1);//结算状态
						fee.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						fee.setBalanceDate(balanceDate);//结算时间
						fee.setBalanceOpercode(empJobNo);//结算操作人
						fee.setInvoiceNo(parameterMap.get("invoiceNo"));
						outBalanceDAO.save(fee);
					}
					
					//13.更新住院药品、非药品费用明细为结算状态、记录结算序号及发票号
					if(inpatientMedicineLists.size()>0){
						for (InpatientMedicineListNow inpatientMedicineList : inpatientMedicineLists) {
							inpatientMedicineList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
							inpatientMedicineList.setBalanceState(1);
							inpatientMedicineList.setInvoiceNo(parameterMap.get("invoiceNo"));
							inpatientMedicineList.setUpdateTime(new Date());
							inpatientMedicineList.setUpdateUser(empJobNo);
							inpatientMedicineDAO.save(inpatientMedicineList);
						}
					}
					if(inpatientItemLists.size()>0){
						for (InpatientItemListNow inpatientItemList : inpatientItemLists) {
							inpatientItemList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
							inpatientItemList.setBalanceState(1);
							inpatientItemList.setInvoiceNo(parameterMap.get("invoiceNo"));
							inpatientItemList.setUpdateTime(new Date());
							inpatientItemList.setUpdateUser(empJobNo);
							inpatientMedicineDAO.save(inpatientItemList);
						}
					}
					
					//14.修改发票
					//String resu = updateInvoiceNo(userId,parameterMap.get("invoiceNo"));
					if("success".equals(resu)){
					}else{
						throw new RuntimeException("修改发票失败！");
					}
					//15.保存资料变更表
					InpatientShiftData inpatientShiftData =new InpatientShiftData();
					inpatientShiftData.setClinicNo(parameterMap.get("inpatientNo"));//住院流水号
					inpatientShiftData.setShiftType("BA");//变更类型
						//查询最大的发生序号
					InpatientShiftData inpatientShift= outBalanceDAO.queryMaxHappon(parameterMap.get("inpatientNo"));
					Integer  happenNo = 1;
					if(inpatientShift.getHappenNo()==null){
						inpatientShiftData.setHappenNo(happenNo);//发生序号
					}else{
						inpatientShiftData.setHappenNo(inpatientShift.getHappenNo()+1);//发生序号
					}
					inpatientShiftDAO.save(inpatientShiftData);
					
					//16.对患者进行开账和修改结算序号
			    	for (InpatientInfoNow info1 : InfoList) {
			    		info1.setStopAcount(0);
			    		info1.setBalanceNo(info1.getBalanceNo()+1);
			    		info1.setBalanceDate(new Date());
			    		info1.setTotCost(info1.getTotCost()-totCost);//费用金额
			    		info1.setOwnCost(info1.getOwnCost()-ownCost);//自费金额
			    		info1.setPayCost(info1.getPayCost()-payCost);//自付金额
			    		info1.setPubCost(info1.getPubCost()-pubCost);//公费金额
			    		info1.setEcoCost(info1.getEcoCost()-ecoCost);//优惠金额
			    		info1.setBalancePrepay(info1.getBalancePrepay()+Double.valueOf(parameterMap.get("yj")));
			    		inpatientInfoDAO.save(info1);
					}
				}
			}	
		}
 		return "success";
	}
	/**
	 * 统计大类map根据Value取Key 
	 * @param map
	 * @param value
	 * @return
	 */
	public static String getKeyByValue(Map map, Object value) {  
        String keys="";  
        Iterator it = map.entrySet().iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Entry) it.next();  
            Object obj = entry.getValue();  
            if (obj != null && obj.equals(value)) {  
                keys=(String) entry.getKey();  
            }  
        }  
        return keys;  
	}
	/**
	 * 修改发票
	 * @return
	 * @throws Exception 
	 */
	public String updateInvoiceNo(String userId,String invoiceNo) throws Exception {  
        String invoiceType = "03";
		outBalanceDAO.saveInvoiceFinance(userId,invoiceNo,invoiceType); 
        return "success";  
	}

	@Override
	public Map<String, String> approvalDrugSave(String applyNumberCode,
			String parameterHz, String empJobNo, String deptCode)
			throws Exception {
		User user=userInInterService.getByCode(empJobNo);
//		SysDepartment logDept=deptInInterService.getDeptCode(deptCode);
		String parameterSF = parameterInnerService.getparameter("isCharge");
		if("".equals(parameterSF)){
			parameterSF = "0";
		}
		List<DrugApplyoutNow> applyoutList = auditDao.findApplyoutByApplyNumber(applyNumberCode);
		Map<String,String> map = new HashMap<String, String>();
		DrugInfo drugInfo = new DrugInfo();
		List<DrugVo> drugVoList = new ArrayList<DrugVo>();
		List<DrugStorage> storageList = new ArrayList<DrugStorage>();
		DrugStockinfo stockinfoList = new DrugStockinfo();
		Map<String,Double> drugMap = new HashMap<String, Double>();
		if(applyoutList.size()>0){
			for(DrugApplyoutNow applyout : applyoutList){
				InpatientInfoNow info = auditDao.queryInpatientInfo(applyout.getPatientId());
				if(StringUtils.isBlank(info.getId())){
					map.put("resMeg", "error");
					map.put("resCode", "患者已出院，该申请单不能摆药");
					return map;
				}
				//获得每一条的数量
				Double applySum = applyout.getApplyNum();
				//统计药品集合
				if(map.get(applyout.getDrugCode())==null){
					drugMap.put(applyout.getDrugCode()+"_"+applyout.getDrugDeptCode(), applySum);
				}else{
					drugMap.put(applyout.getDrugCode()+"_"+applyout.getDrugDeptCode(),drugMap.get(applyout.getDrugCode())+applySum);
				}
				//把map集合存到VO中
				if(drugMap.size()>0){//当统计之后有数据时
					DrugVo vo = null;
					for(Map.Entry<String, Double> entry : drugMap.entrySet()){//根据页面显示格式将数据改成相应的格式
						vo = new DrugVo();
						String[] arr = entry.getKey().split("_");
						vo.setItemCode(arr[0]);
						vo.setDeptCode(arr[1]);
						BigDecimal itemSum = new   BigDecimal(entry.getValue());
						Double itemSums = itemSum.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue(); 
						vo.setItemSum(itemSums);
						drugVoList.add(vo);
					}
				}
				//判断该物品是包装单位还是最小单位
				if(applyout.getShowFlag() != null && applyout.getShowFlag() - 1 == 0){
					applySum *= drugInfo.getPackagingnum();
				}
			}
			
			for(DrugApplyoutNow applyout : applyoutList){
				Map<String,String> mapOuts = applyoutInInterService.outDrugInterface(applyout,"4",Integer.parseInt(parameterHz));
				Integer sequenceNo=0;
				if(applyout.getSequenceNo()==null){
					sequenceNo=0;
				}else{
					sequenceNo=applyout.getSequenceNo();
				}
				//更新药嘱执行档
				InpatientExecdrugNow execdrug = auditDao.queryInpatientExecdrug(applyout.getRecipeNo(),sequenceNo);
				execdrug.setDrugedFlag(3);//0不需发送/1集中发送/2分散发送/3已配药
				execdrug.setDrugedDate(DateUtils.getCurrentTime());//配药时间
				execdrug.setDrugedUsercd(empJobNo);//配药人
				execdrug.setDrugedUsercdName(user.getName());//配药人姓名
				execdrug.setDrugedDeptcd(applyout.getDeptCode());//配药科室
				execdrug.setDrugedDeptcdName(applyout.getDeptName());//配药科室名称
				
				execdrug.setHospitalId(HisParameters.CURRENTHOSPITALID);
				execdrug.setAreaCode(inprePayDAO.getDeptArea(deptCode));
				auditDao.update(execdrug);
				
				
				
				if("1".equals(parameterSF)){
					//更新住院药品明细表
					InpatientMedicineListNow medicineList = auditDao.queryInpatientMedicineList(applyout.getRecipeNo(),sequenceNo);
					medicineList.setUpdateSequenceno(mapOuts.get("outBillCode"));//更新库存的流水号        
					medicineList.setSenddrugSequence(applyout.getDrugedBill());//发药单序列号
					medicineList.setSenddrugFlag(2);// 发药状态（0 划价 2摆药 1批费）   
					medicineList.setSenddrugOpercode(empJobNo);//发药人
					medicineList.setSenddrugDate(DateUtils.getCurrentTime());//发药时间
					medicineList.setMedicineDeptcode(applyout.getDeptCode());//取药科室
					auditDao.update(medicineList);
					
					//更新住院费用汇总表
					InpatientFeeInfoNow feeInfo = auditDao.queryInpatientFeeInfo(applyout.getRecipeNo());
					feeInfo.setFeeOpercode(empJobNo);//计费人
					feeInfo.setFeeDate(DateUtils.getCurrentTime());//计费时间
					auditDao.update(feeInfo);
				}
				
				//更新医嘱表
				InpatientOrderNow inpatientOrder = auditDao.queryInpatientOrder(applyout.getMoOrder());
				inpatientOrder.setMoStat(2);//医嘱状态,0开立，1审核，2执行，3作废，4重整，-1需要上级审核，-3上级审核不通过
				auditDao.update(inpatientOrder);  
				
				//更新出库申请表
				if("1".equals(parameterHz)){
					applyout.setApplyState(7);//申请状态 0申请，1（配药）打印，2核准（出库），3作废，4暂不摆药  7 审批
				}else{
					applyout.setApplyState(2);//申请状态 0申请，1（配药）打印，2核准（出库），3作废，4暂不摆药  7审批
				}
				applyout.setDrugedDept(applyout.getDeptCode());//摆药科室
				applyout.setDrugedNum(applyout.getDrugedNum());//摆药数量
				applyout.setDrugedEmpl(empJobNo);//摆药人
				applyout.setDrugedEmplName(user.getName());
				applyout.setDrugedDate(new Date());//摆药日期
				auditDao.update(applyout);
			}
		}
		
		List<DrugApplyoutNow> drugApplyoutLists = auditDao.findDeptSummary(applyoutList.get(0).getDrugedBill(),4,null,null,"5,6");
		//更新摆药通知单
		if(drugApplyoutLists.size()==applyoutList.size()){
			InpatientStoMsgNow stoMsg = auditDao.queryInpatientStoMsg(applyoutList.get(0).getBillclassCode());
			stoMsg.setSendFlag("1");//摆药状态
			stoMsg.setSendDtime(DateUtils.getCurrentTime());//摆药时间
			auditDao.update(stoMsg);
		}
		map.put("resMsg", "success");
		map.put("resCode", "摆药完成");
		return map;
	}
	
	@Override
	public Map<String, String> examineDrugSaveAndUpdate(String applyNumber,String ids,String empJobNo,String deptCode) throws Exception {
		List<DrugApplyoutNow> applyoutList = auditDao.findApplyoutByApplyNumber(applyNumber);
		Map<String,String> map = new HashMap<String, String>();
		DrugInfo drugInfo = new DrugInfo();
		List<DrugVo> drugVoList = new ArrayList<DrugVo>();
		List<DrugStorage> storageList = new ArrayList<DrugStorage>();
		DrugStockinfo stockinfoList = new DrugStockinfo();
		Map<String,Double> drugMap = new HashMap<String, Double>();
		List<DrugOutstore> outstoreList = new ArrayList<DrugOutstore>();
		List<InpatientMedicineListNow> inpatientMedicineList = new ArrayList<InpatientMedicineListNow>();
		List<InpatientOrderNow> inpatientOrderList = new ArrayList<InpatientOrderNow>();
		List<InpatientExecdrug> inpatientExecdrugList = new ArrayList<InpatientExecdrug>();
		for(DrugApplyoutNow applyout : applyoutList){
			InpatientInfoNow info = auditDao.queryInpatientInfo(applyout.getPatientId());
			if(StringUtils.isBlank(info.getId())){
				map.put("resMsg", "error");
				map.put("resCode", "患者已出院，该申请单不能核准");
				return map;
			}
			//获得每一条的数量
			Double applySum = applyout.getApplyNum();
			//统计药品集合
			if(map.get(applyout.getDrugCode())==null){
				drugMap.put(applyout.getDrugCode()+"_"+applyout.getDrugDeptCode(), applySum);
			}else{
				drugMap.put(applyout.getDrugCode()+"_"+applyout.getDrugDeptCode(),drugMap.get(applyout.getDrugCode())+applySum);
			}
			//把map集合存到VO中
			if(drugMap.size()>0){//当统计之后有数据时
				DrugVo vo = null;
				for(Map.Entry<String, Double> entry : drugMap.entrySet()){//根据页面显示格式将数据改成相应的格式
					vo = new DrugVo();
					String[] arr = entry.getKey().split("_");
					vo.setItemCode(arr[0]);
					vo.setDeptCode(arr[1]);
					BigDecimal itemSum = new   BigDecimal(entry.getValue());
					Double itemSums = itemSum.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					vo.setItemSum(itemSums);
					drugVoList.add(vo);
				}
			}
			//判断该物品是包装单位还是最小单位
			if(applyout.getShowFlag() != null && applyout.getShowFlag() - 1 == 0){
				applySum *= drugInfo.getPackagingnum();
			}
		}
		
		if(drugVoList.size()>0){
			for(DrugVo drugVos:drugVoList){
				//判断药品库存是否充足
				drugInfo = auditDao.queryDrugInfo(drugVos.getItemCode());
				if(StringUtils.isNotBlank(drugInfo.getId())){
					map.put("resMsg", "error");
					map.put("resCode", "["+drugInfo.getName()+"]在库房中不存在");
					return map;
				}
				//判断申请数量
				Double sum = infoService.getDrugStockInfoStoreNum(drugVos.getDeptCode(), drugVos.getItemCode());
				Double drugSumQty = sum - drugVos.getItemSum();
				//判断库存充足
				if(drugSumQty<0){
					map.put("resMsg", "error");
					map.put("resCode", "["+drugInfo.getName()+"]在库存不足");
					return map;
				}
			}
		}
		
		for(DrugApplyoutNow applyout : applyoutList){
			//生成出库单号一个药品用一个
			String outBillCode = auditDao.getSeqByNameorNum("SEQ_OUT_BILL_CODE", 20);
			//申请数量
			Double drugQty = applyout.getApplyNum();
			storageList = auditDao.findDrugStorageByDrugId(applyout.getDrugDeptCode(),applyout.getDrugCode());
			if(storageList.size()>0){
				for(DrugStorage storage:storageList){
					//单内序号
					int serialCode = 0;
					//出库单据号
					Double storageNum = storage.getStoreSum();
					//库存数量（最小单位）
					if(storageNum > drugQty){
						serialCode++;
						//扣除库存数量
						storage.setStoreSum(storage.getStoreSum() - drugQty);
						//库存金额
						storage.setStoreCost(storage.getStoreCost() - drugQty * storage.getRetailPrice());
						storage.setOperCode("4");
						auditDao.save(storage);
						break;
					}else{
						serialCode++;
						drugQty -= storageNum;
						//扣除库存数量
						storage.setStoreSum(0.0);
						//库存金额
						storage.setStoreCost(0.0);
						storage.setOperCode("4");
						auditDao.save(storage);
					}
				}
			}
			applyout.setApplyState(2);
			auditDao.update(applyout);
		}
		for(DrugApplyoutNow applyout : applyoutList){
			InpatientMedicineListNow med=auditDao.getMed(applyout.getRecipeNo(),applyout.getSequenceNo());
			if(med!=null){
				med.setSenddrugFlag(2);
				auditDao.update(med);
			}
			
		}
		
		List<String> outstore = new ArrayList<String>();
		for(DrugApplyoutNow applyout : applyoutList){
			if(!outstore.contains(applyout.getDrugedBill())){
				outstore.add(applyout.getDrugedBill());
			}
		}
		List<DrugOutstoreNow> outstoreListss = auditDao.findExDrugOutstore(outstore);
		if(outstoreListss.size()>0){
			for(DrugOutstoreNow modls: outstoreListss){
				modls.setOutState(2);//核准
				auditDao.save(modls);
			}
		}
		map.put("resMsg", "success");
		map.put("resCode", "核准完成");
		return map;
	}

}
