package cn.honry.inpatient.auditDrug.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.inner.drug.apply.service.DrugApplyInInterService;
import cn.honry.inner.drug.applyout.service.ApplyoutInInterService;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inpatient.auditDrug.dao.AuditDao;
import cn.honry.inpatient.auditDrug.service.AuditService;
import cn.honry.inpatient.auditDrug.vo.DrugVo;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("auditService")
@Transactional
@SuppressWarnings({"all"})
public class AuditServiceImpl implements AuditService {
	
	//注入本类dao
	@Autowired
	@Qualifier(value = "auditDao")
	private AuditDao auditDao;
	
	@Qualifier(value = "auditService")
	private AuditService auditService;
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService infoService;
	@Autowired
	@Qualifier(value = "applyoutInInterService")
	private ApplyoutInInterService applyoutInInterService;
	@Autowired
	@Qualifier(value = "bdrugApplyInInterService")
	private DrugApplyInInterService bdrugApplyService;
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService businessStockInfoInInterService;
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
	public DrugApplyoutNow get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(DrugApplyoutNow arg0) {
		
	}
	

	@Override
	public List<InpatientKind> queryKindFunction() throws Exception {
		return auditDao.queryKindFunction();
	}

	@Override
	public List<OutpatientDrugcontrol> queryDrugHouse(String id) throws Exception {
		return auditDao.queryDrugHouse(id);
	}
	@Override
	public OutpatientDrugcontrol queryDrugcontril(String id) throws Exception {
		return auditDao.queryDrugcontril(id);
	}
	@Override
	public List<TreeJson> approveNoDrugTree(String controlId, Integer sendType,Integer opType,String applyState, String sendFlag, String medicalrecordId) throws Exception {
		//获取当前登陆科室ID
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		//初始化摆药树
		String [] sendFlags=sendFlag.split(",");
		String [] applyStates=applyState.split(",");
		
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List classIds=new ArrayList();
		if(opType==4){
			//获取该摆药台下所有摆药单分类
			List<DrugBillclass> dillclassList = auditDao.findDillClassById(controlId);
			if(dillclassList.size()>0){
				for(DrugBillclass modls:dillclassList){
					classIds.add(modls.getBillclassCode());
				}
			}else{
				return new ArrayList<TreeJson>();
			}
			
		}else if(opType==5){
			classIds.add("R");
		}
		
		List<DrugApplyoutNow> applyoutListDeptCode = auditDao.findApplyoutByDept(deptId,classIds,sendType,opType,applyStates,medicalrecordId,sendFlags);
		if(applyoutListDeptCode.size()>0){
			for(DrugApplyoutNow modl:applyoutListDeptCode){
					//加树的根节点--开立科室
					TreeJson pTreeJson = new TreeJson();
					pTreeJson.setId(modl.getDeptCode());
					pTreeJson.setText(modl.getDeptName());
					pTreeJson.setIconCls("icon-neighbourhood");
					Map<String,String> attributesgen = new HashMap<String, String>();
					attributesgen.put("pid","0");
					attributesgen.put("type","0");
					pTreeJson.setAttributes(attributesgen);
					treeJsonList.add(pTreeJson);
					List<DrugApplyoutNow> applyoutListBillClass = auditDao.findApplyoutByClass(deptId,modl.getDeptCode(),sendType,opType,applyStates,medicalrecordId,sendFlags);
					
					List<TreeJson> treeJsonList1 = new ArrayList<TreeJson>();
					if(applyoutListBillClass.size()>0){
						for(DrugApplyoutNow out :applyoutListBillClass ){
							TreeJson cTreeJson = new TreeJson();
							cTreeJson.setId(out.getBillclassCode());
							cTreeJson.setText(out.getBillclassName());
							cTreeJson.setIconCls("icon-arrow_inout");
							Map<String,String> cttributesgen = new HashMap<String, String>();
							cttributesgen.put("pid", modl.getDeptCode());
							cttributesgen.put("type", "1");
							cTreeJson.setAttributes(cttributesgen);
							treeJsonList1.add(cTreeJson);
							pTreeJson.setChildren(treeJsonList1);
							List<DrugApplyoutNow> applyoutListBillClassBybill = auditDao.findApplyoutBybill(deptId,out.getBillclassCode(),modl.getDeptCode(),sendType,opType,applyStates,medicalrecordId,sendFlags);
							
							List<TreeJson> treeJsonList2 = new ArrayList<TreeJson>();
							for(DrugApplyoutNow outs :applyoutListBillClassBybill ){
								TreeJson sTreeJson = new TreeJson();
								sTreeJson.setId(outs.getDrugedBill());
								sTreeJson.setText(outs.getDrugedBill());
								sTreeJson.setIconCls("icon-application_view_list");
								Map<String,String> sttributesgen = new HashMap<String, String>();
								sttributesgen.put("pid",out.getBillclassCode());
								sttributesgen.put("type","2");
								sttributesgen.put("dept",modl.getDeptCode());
								sTreeJson.setAttributes(sttributesgen);
								treeJsonList2.add(sTreeJson);
								cTreeJson.setChildren(treeJsonList2);
							}
						}
					}
				}
			}
		if(treeJsonList.size()==0){
			return new ArrayList<TreeJson>();
		}
		return treeJsonList;
	}
	
	@Override
	public List<DrugApplyoutNow> findDeptSummary(String drugedBill,Integer opType, Integer sendType, String sendFlag, String applyState) throws Exception {
		return auditDao.findDeptSummary(drugedBill,opType,sendType,sendFlag,applyState);
	}
	
	@Override
	public List<DrugApplyoutNow> findDetailed(String drugedBill, Integer opType,Integer sendType, String sendFlag, String applyState, String deptId) throws Exception {
		String [] applyStates=applyState.split(",");
		return auditDao.findDetailed(drugedBill,opType,sendType,sendFlag,applyStates,deptId);
	}
	
	@Override
	public Map<String, String> approvalDrugSave(String applyNumberCode,String parameterHz) throws Exception {
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
				execdrug.setDrugedUsercd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//配药人
				execdrug.setDrugedUsercdName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//配药人姓名
				execdrug.setDrugedDeptcd(applyout.getDeptCode());//配药科室
				execdrug.setDrugedDeptcdName(applyout.getDeptName());//配药科室名称
				
				execdrug.setHospitalId(HisParameters.CURRENTHOSPITALID);
				execdrug.setAreaCode(inprePayDAO.getDeptArea(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode()));
				auditDao.update(execdrug);
				
				
				
				if("1".equals(parameterSF)){
					//更新住院药品明细表
					InpatientMedicineListNow medicineList = auditDao.queryInpatientMedicineList(applyout.getRecipeNo(),sequenceNo);
					medicineList.setUpdateSequenceno(mapOuts.get("outBillCode"));//更新库存的流水号        
					medicineList.setSenddrugSequence(applyout.getDrugedBill());//发药单序列号
					medicineList.setSenddrugFlag(2);// 发药状态（0 划价 2摆药 1批费）   
					medicineList.setSenddrugOpercode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//发药人
					medicineList.setSenddrugDate(DateUtils.getCurrentTime());//发药时间
					medicineList.setMedicineDeptcode(applyout.getDeptCode());//取药科室
					auditDao.update(medicineList);
					
					//更新住院费用汇总表
					InpatientFeeInfoNow feeInfo = auditDao.queryInpatientFeeInfo(applyout.getRecipeNo());
					feeInfo.setFeeOpercode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//计费人
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
				applyout.setDrugedEmpl(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//摆药人
				applyout.setDrugedEmplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
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
	public Map<String,String> queryMedicalrecordId(String medicalrecordId) throws Exception {
		Map<String,String> map = new HashMap<String, String>();
		InpatientInfoNow info = auditDao.queryMedicalrecordId(medicalrecordId);
		if(StringUtils.isNotBlank(info.getId())){
			map.put("resMeg", "success");
			map.put("resCode", info.getInpatientNo());
		}else{
			map.put("resMeg", "error");
			map.put("resCode", "此患者不在住院状态");
		}
		return map;
	}

	@Override
	public Map<String, String> examineDrugSaveAndUpdate(String applyNumber,String ids) throws Exception {
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

	@Override
	public Map<String, String> withdrawalDrug(String applyNumberCode,String parameterTf) throws Exception {
		Map<String,String> map = new HashMap<String, String>();
		List<DrugApplyoutNow> applyoutList = auditDao.findDrugApplyoutBywith(applyNumberCode);
		if(applyoutList.size()>0){
			for(DrugApplyoutNow app : applyoutList){
				Map<String,String> mapOuts = applyoutInInterService.outDrugInterface(app,"5",2);
			}
		}
		if("1".equals(parameterTf)){
			//生成退费申请
			bdrugApplyService.saveAdd(applyoutList);
		}else{
			//直接退费
			bdrugApplyService.directSave(applyoutList);
		}
		map.put("resMsg", "success");
		map.put("resCode", "退药成功");
		return map;
	}
}
