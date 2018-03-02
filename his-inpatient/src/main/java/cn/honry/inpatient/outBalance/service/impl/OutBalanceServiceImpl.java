package cn.honry.inpatient.outBalance.service.impl;

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

import cn.honry.base.bean.model.BusinessEcoformula;
import cn.honry.base.bean.model.BusinessEcoicdfee;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.inpatient.feeInfo.dao.InpatientFeeInfoInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.costDerate.dao.CostDerateDao;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
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
import cn.honry.inpatient.outBalance.service.OutBalanceService;
import cn.honry.inpatient.outBalance.vo.InvoicePrintVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.reflect.TypeToken;
@Service("outBalanceService")
@Transactional
@SuppressWarnings({ "all" })
public class OutBalanceServiceImpl implements OutBalanceService{
	private static final String InpatientBalancePay = null;
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
	/**获取当前院区接口**/
	@Autowired
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}
	@Override
	public List<InpatientInfoNow> queryInfoByPatientNo(String medicalrecordId) throws Exception {
		return outBalanceDAO.queryInfoByPatientNo(medicalrecordId);
	}

	@Override
	public String getDeptName(String deptCode) throws Exception {
		return outBalanceDAO.getDeptName(deptCode);
	}

	@Override
	public void saveInvoiceFinance(String id, String invoiceNo,
			String invoiceType) {
		
	}

	@Override
	public Map<String, Object> findFinanceInvoice(String id, String invoiceType) {
		return null;
	}

	@Override
	public SysEmployee findEmployee(String userId) {
		return null;
	}

	@Override
	public Map<String, String> queryFinanceInvoiceNo(String id,String invoiceType) throws Exception {
		//获得领取发票的那一条信息
		Map<String,String> map = outBalanceDAO.queryFinanceInvoiceNo(id,invoiceType);
		return map;
	}

	@Override
	public SysEmployee queryEmployee(String id) throws Exception {
		SysEmployee employee = outBalanceDAO.queryEmployee(id);
		return employee;
	}

	@Override
	public List<InpatientInfoNow> getbalanceNo(String inpatientNo) throws Exception {
		return outBalanceDAO.getbalanceNo(inpatientNo);
	}

	@Override
	public List<InpatientSurety> queryInpatientSurety(String inpatientNo,
			String outDate, String inDate) throws Exception {
		return outBalanceDAO.queryInpatientSurety(inpatientNo, outDate, inDate);
	}

	@Override
	public List<InpatientCancelitemNow> getInpatientCancelitem(String inpatientNo) throws Exception {
		return outBalanceDAO.getInpatientCancelitem(inpatientNo);
	}

	@Override
	public List<OutpatientDrugcontrol> queryDrugcontrol(String inpatientNo) throws Exception {
		return outBalanceDAO.queryDrugcontrol(inpatientNo);
	}

	@Override
	public List<InpatientInPrepayNow> queryInPrepay(String inpatientNo) throws Exception {
		return outBalanceDAO.queryInPrepay(inpatientNo);
	}

	@Override
	public String UpdateInpatientInfoList(String inpatientNo) throws Exception {
		InpatientInfoNow info = outBalanceDAO.queryInfoByinpatientNo(inpatientNo);
		String result = "";
		if(info==null){
			result = "未查到患者信息，封账失败！";
		}else{
			String sql = "update T_INPATIENT_INFO_NOW set STOP_ACOUNT = ?  where INPATIENT_ID = ?";  
		    Object args[] = new Object[]{1,info.getId()};  
		    int t = jdbcTemplate.update(sql,args);  
			result = "success";
		}
		return result;
	}

	@Override
	public List<InpatientInPrepayNow> queryInpatientInPrepay(String inpatientNo,
			String inDate, String outDate) throws Exception {
		return outBalanceDAO.queryInpatientInPrepay(inpatientNo,inDate,outDate);
	}

	@Override
	public List<InpatientFeeInfoNow> InpatientFeeList(String inpatientNo,
			String inDate1, String outDate1) throws Exception {
		return outBalanceDAO.InpatientFeeList(inpatientNo,inDate1,outDate1);
	}

	@Override
	public List<BusinessEcoformula> getclinicCode(String clinicCode) throws Exception {
		return outBalanceDAO.getclinicCode(clinicCode);
	}

	@Override
	public List<BusinessEcoicdfee> getcost(String sysdate, String inpatientNo) throws Exception {
		return outBalanceDAO.getcost(sysdate, inpatientNo);
	}

	@Override
	public List<InpatientDerate> getclinicNo(String inpatientNo,
			String inDate1, String outDate1) throws Exception {
		return outBalanceDAO.getclinicNo(inpatientNo, inDate1, outDate1);
	}

	@Override
	public List<InpatientInfoNow> InpatientInfoqueryFee(String inpatientNo) throws Exception {
		return outBalanceDAO.InpatientInfoqueryFee(inpatientNo);
	}
	
	@Override
	public String saveBalance(Map<String, String> parameterMap, String zfJson,String costJson) throws Exception { 
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String msg = "";
		//14.修改发票
		String resu = updateInvoiceNo(user.getAccount(),parameterMap.get("invoiceNo"));
		if("success".equals(resu)){
		}else{
			msg="修改发票失败！";
			return msg;
		}
		List<InpatientMedicineListNow> inpatientMedicineLists = outBalanceDAO.queryInpatientMedicineList(parameterMap.get("inpatientNo"));
		List<InpatientItemListNow> inpatientItemLists = outBalanceDAO.queryInpatientItemList(parameterMap.get("inpatientNo"));
		
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		Date balanceDate = DateUtils.getCurrentTime();//结算时间
		Integer hospatialId=HisParameters.CURRENTHOSPITALID;
		String areaCode="";
		if(StringUtils.isNotBlank(dept.getDeptCode())){
			areaCode=inprePayService.getDeptArea(dept.getDeptCode());
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
								prepay.setBalanceOpercode(user.getAccount());//结算人
								prepay.setBalanceOpername(user.getName());
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
								entity.setBalanceOpercode(user.getAccount());//结算人代码
								entity.setBalanceDate(balanceDate);//结算时间
								entity.setCreateUser(user.getAccount());
								entity.setCreateTime(new Date());
								entity.setUpdateUser(user.getAccount());
								entity.setUpdateTime(new Date());
								entity.setCreateDept(user.getCreateDept());
								entity.setHospitalId(hospatialId);
								entity.setAreaCode(areaCode);
								inpatientBalancePayListDAO.save(entity);
							}
						}
						OperationUtils.getInstance().conserve(null, "住院收费实付表","INSERT_INTO", "T_INPATIENT_BALANCEPAY_NOW",OperationUtils.LOGACTIONINSERT);
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
									inpatientDerate.setDeptCode(dept.getDeptCode());//减免科室
									inpatientDerate.setCreateUser(user.getAccount());
									inpatientDerate.setCreateTime(new Date());
									inpatientDerate.setUpdateUser(user.getAccount());
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
						String sh = parameterMap.get("sh");
						if("".equals(sh1)){
							Head.setSupplyCost(Double.valueOf(parameterMap.get("sh")));//补收金额
						}
						if("".equals(parameterMap.get("sh"))){
							Head.setReturnCost(Double.valueOf(parameterMap.get("sh1")));//返还金额
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						try {
							Date inDate = sdf.parse(parameterMap.get("inDate"));
							Date outDate = sdf.parse(parameterMap.get("outDate"));
							Head.setBeginDate(inDate);//起始日期
							Head.setEndDate(outDate);//终止日期
						} catch (ParseException e) {
							throw new RuntimeException("处理时间时出错！");
						}
						Head.setBalanceType(2);//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
						Head.setBalanceOpercode(user.getAccount());//结算人代码
						Head.setBalanceOpername(user.getName());//结算人
						Head.setBalanceDate(balanceDate);//结算时间
						Head.setBalanceoperDeptcode(dept.getDeptCode());//结算员科室
						Head.setBalanceoperDeptname(dept.getDeptName());//结算员科室
						Head.setCheckFlag(0);//0未核查/1已核查
						Head.setCreateUser(user.getAccount());
						Head.setCreateTime(new Date());
						Head.setUpdateUser(user.getAccount());
						Head.setUpdateTime(new Date());
						Head.setCreateDept(dept.getDeptCode());
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
								String string2 = codes[j];
								inpatientBalanceList.setStatCode(codes[0]);//统计大类
								inpatientBalanceList.setStatName(codes[1]);//统计大类名称
							}
							inpatientBalanceList.setTotCost(inpatientFeeInfo.getTotCost());//费用金额
							inpatientBalanceList.setOwnCost(inpatientFeeInfo.getOwnCost());//自费金额
							inpatientBalanceList.setPayCost(inpatientFeeInfo.getPayCost());//自付金额
							inpatientBalanceList.setPubCost(inpatientFeeInfo.getPubCost());//公费金额
							inpatientBalanceList.setEcoCost(inpatientFeeInfo.getEcoCost());//优惠金额
							inpatientBalanceList.setBalanceOpercode(user.getAccount());//结算人代码
							inpatientBalanceList.setBalanceDate(balanceDate);//结算时间
							inpatientBalanceList.setBalanceType(3);//结算类型 1:在院结算，2:转科结算，3:出院结算，4:重结算 5:结转
							inpatientBalanceList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							inpatientBalanceList.setBalanceoperDeptcode(dept.getDeptCode());//结算员科室
							inpatientBalanceList.setBalanceoperDeptname(dept.getDeptName());//结算员科室
							inpatientBalanceList.setCreateUser(user.getAccount());
							inpatientBalanceList.setCreateTime(new Date());
							inpatientBalanceList.setUpdateUser(user.getAccount());
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
						model.setBalanceOpercode(user.getAccount());//结算人代码
						model.setBalanceDate(balanceDate);//结算时间
						model.setCreateUser(user.getAccount());
						model.setCreateTime(new Date());
						model.setUpdateUser(user.getAccount());
						model.setUpdateTime(new Date());
						OperationUtils.getInstance().conserve(null, "住院收费实付表","INSERT_INTO", "T_INPATIENT_BALANCEPAY_NOW",OperationUtils.LOGACTIONINSERT);
						model.setHospitalId(hospatialId);
						model.setAreaCode(areaCode);
						inpatientBalancePayListDAO.save(model);
						
						//12.更新住院费用汇总信息为结算状态，记录结算序号、结算时间、结算操作人，发票号
						for (InpatientFeeInfoNow fee : InpatientFeeInfoList) {
							fee.setBalanceState(1);//结算状态
							fee.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							fee.setBalanceDate(balanceDate);//结算时间
							fee.setBalanceOpercode(user.getAccount());//结算操作人
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
								inpatientMedicineList.setUpdateUser(user.getAccount());
								inpatientMedicineDAO.save(inpatientMedicineList);
							}
						}
						if(inpatientItemLists.size()>0){
							for (InpatientItemListNow inpatientItemList : inpatientItemLists) {
								inpatientItemList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
								inpatientItemList.setBalanceState(1);
								inpatientItemList.setInvoiceNo(parameterMap.get("invoiceNo"));
								inpatientItemList.setUpdateTime(new Date());
								inpatientItemList.setUpdateUser(user.getAccount());
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
						OperationUtils.getInstance().conserve(null, "资料变更表", "INSERT_INTO", "T_INPATIENT_SHIFTDATA", OperationUtils.LOGACTIONINSERT);
						
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
	public String saveBalanceZhongtu(Map<String, String> parameterMap, String zfJson,String costJson) throws Exception { 
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String msg = "";
		//14.修改发票
		String resu = updateInvoiceNo(user.getAccount(),parameterMap.get("invoiceNo"));
		if("success".equals(resu)){
		}else{
			msg="修改发票失败！";
			return msg;
		}
		List<InpatientMedicineListNow> inpatientMedicineLists = outBalanceDAO.queryInpatientMedicineList(parameterMap.get("inpatientNo"));
		List<InpatientItemListNow> inpatientItemLists = outBalanceDAO.queryInpatientItemList(parameterMap.get("inpatientNo"));
		
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		Date balanceDate = DateUtils.getCurrentTime();//结算时间
		Integer hospatialId=HisParameters.CURRENTHOSPITALID;
		String areaCode="";
		if(StringUtils.isNotBlank(dept.getDeptCode())){
			areaCode=inprePayService.getDeptArea(dept.getDeptCode());
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
							prepay.setBalanceOpercode(user.getAccount());//结算人
							prepay.setBalanceOpername(user.getName());
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
							entity.setBalanceOpercode(user.getAccount());//结算人代码
							entity.setBalanceDate(balanceDate);//结算时间
							entity.setCreateUser(user.getAccount());
							entity.setCreateTime(new Date());
							entity.setUpdateUser(user.getAccount());
							entity.setUpdateTime(new Date());
							entity.setHospitalId(hospatialId);
							entity.setAreaCode(areaCode);
							inpatientBalancePayListDAO.save(entity);
						}
					}
					OperationUtils.getInstance().conserve(null, "住院收费实付表","INSERT_INTO", "T_INPATIENT_BALANCEPAY_NOW",OperationUtils.LOGACTIONINSERT);
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
					String sh = parameterMap.get("sh");
					if("".equals(sh1)){
						Head.setSupplyCost(Double.valueOf(parameterMap.get("sh")));//补收金额
					}
					if("".equals(parameterMap.get("sh"))){
						Head.setReturnCost(Double.valueOf(parameterMap.get("sh1")));//返还金额
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					try {
						Date inDate = sdf.parse(parameterMap.get("inDate"));
						Date outDate = sdf.parse(parameterMap.get("outDate"));
						Head.setBeginDate(inDate);//起始日期
						Head.setEndDate(outDate);//终止日期
					} catch (ParseException e) {
						throw new RuntimeException("处理时间时出错！");
					}
					Head.setBalanceType(2);//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
					Head.setBalanceOpercode(user.getAccount());//结算人代码
					Head.setBalanceOpername(user.getName());//结算人
					Head.setBalanceDate(balanceDate);//结算时间
					Head.setBalanceoperDeptcode(dept.getDeptCode());//结算员科室
					Head.setBalanceoperDeptname(dept.getDeptName());//结算员科室
					Head.setCheckFlag(0);//0未核查/1已核查
					Head.setCreateUser(user.getAccount());
					Head.setCreateTime(new Date());
					Head.setUpdateUser(user.getAccount());
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
							String string2 = codes[j];
							inpatientBalanceList.setStatCode(codes[0]);//统计大类
							inpatientBalanceList.setStatName(codes[1]);//统计大类名称
						}
						inpatientBalanceList.setTotCost(inpatientFeeInfo.getTotCost());//费用金额
						inpatientBalanceList.setOwnCost(inpatientFeeInfo.getOwnCost());//自费金额
						inpatientBalanceList.setPayCost(inpatientFeeInfo.getPayCost());//自付金额
						inpatientBalanceList.setPubCost(inpatientFeeInfo.getPubCost());//公费金额
						inpatientBalanceList.setEcoCost(inpatientFeeInfo.getEcoCost());//优惠金额
						inpatientBalanceList.setBalanceOpercode(user.getAccount());//结算人代码
						inpatientBalanceList.setBalanceDate(balanceDate);//结算时间
						inpatientBalanceList.setBalanceType(3);//结算类型 1:在院结算，2:转科结算，3:出院结算，4:重结算 5:结转
						inpatientBalanceList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						inpatientBalanceList.setBalanceoperDeptcode(dept.getDeptCode());//结算员科室
						inpatientBalanceList.setCreateUser(user.getAccount());
						inpatientBalanceList.setCreateTime(new Date());
						inpatientBalanceList.setUpdateUser(user.getAccount());
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
					model.setBalanceOpercode(user.getAccount());//结算人代码
					model.setBalanceDate(balanceDate);//结算时间
					model.setCreateUser(user.getAccount());
					model.setCreateTime(new Date());
					model.setUpdateUser(user.getAccount());
					model.setUpdateTime(new Date());
					model.setAreaCode(areaCode);
					model.setHospitalId(hospatialId);
					OperationUtils.getInstance().conserve(null, "住院收费实付表","INSERT_INTO", "T_INPATIENT_BALANCEPAY_NOW",OperationUtils.LOGACTIONINSERT);
					inpatientBalancePayListDAO.save(model);
					
					//12.更新住院费用汇总信息为结算状态，记录结算序号、结算时间、结算操作人，发票号
					for (InpatientFeeInfoNow fee : InpatientFeeInfoList) {
						fee.setBalanceState(1);//结算状态
						fee.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						fee.setBalanceDate(balanceDate);//结算时间
						fee.setBalanceOpercode(user.getAccount());//结算操作人
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
							inpatientMedicineList.setUpdateUser(user.getAccount());
							inpatientMedicineDAO.save(inpatientMedicineList);
						}
					}
					if(inpatientItemLists.size()>0){
						for (InpatientItemListNow inpatientItemList : inpatientItemLists) {
							inpatientItemList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));
							inpatientItemList.setBalanceState(1);
							inpatientItemList.setInvoiceNo(parameterMap.get("invoiceNo"));
							inpatientItemList.setUpdateTime(new Date());
							inpatientItemList.setUpdateUser(user.getAccount());
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
					OperationUtils.getInstance().conserve(null, "资料变更表", "INSERT_INTO", "T_INPATIENT_SHIFTDATA", OperationUtils.LOGACTIONINSERT);
					
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
        String keys="";  
        String invoiceType = "03";
		outBalanceDAO.saveInvoiceFinance(userId,invoiceNo,invoiceType); 
        return "success";  
	}

	@Override
	public List<InvoicePrintVo> printBalance(String inpatientNo) throws Exception {
		return outBalanceDAO.printBalance(inpatientNo);
	}
}
