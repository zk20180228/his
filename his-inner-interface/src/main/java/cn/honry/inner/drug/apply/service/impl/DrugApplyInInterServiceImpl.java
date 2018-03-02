package cn.honry.inner.drug.apply.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.drug.apply.dao.DrugApplyInInterDAO;
import cn.honry.inner.drug.apply.service.DrugApplyInInterService;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("bdrugApplyInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class DrugApplyInInterServiceImpl implements DrugApplyInInterService {

	/***
	 * 注入本类退费申请DAO
	 */
	@Autowired
	@Qualifier("bdrugApplyInInterDao")
	private DrugApplyInInterDAO bdrugApplyInInterDao; 
	
	/***
	 * 注入药品 druginfo的service
	 * 调用接口
	 */
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService businessStockInfoService;
	
	/***
	 * 住院费用接口 
	 */
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;
	public void setInpatientInfoInInterService(InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}

	@Override
	public InpatientCancelitemNow get(String arg0) {
		return bdrugApplyInInterDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientCancelitemNow arg0) {
		bdrugApplyInInterDao.save(arg0);
	}
	
//	@Override
	public void saveAdd(List<DrugApplyoutNow> druglist) {
		//当前登录信息	
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user=(User) SessionUtils.getCurrentEmployeeFromShiroSession();//当前操作人
		String deptid=dept.getDeptCode();
		String userid=user.getAccount();
		//String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId();
		//String userid=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getId();
		Date date = DateUtils.getCurrentTime();
		//产生的退费申请集合
		List<InpatientCancelitemNow> cancelList = new ArrayList<InpatientCancelitemNow>();
		List<Object> list = new ArrayList<Object>();
		//申请单号
		String	billCode = bdrugApplyInInterDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
		
		for(DrugApplyoutNow model : druglist){
			InpatientCancelitemNow cancelitem = new InpatientCancelitemNow();
			cancelitem.setBillCode(billCode);
			
			//申请归属标识 1门诊/2住院
//			cancelitem.setApplyFlag(2);
			//住院流水号
			cancelitem.setInpatientNo(model.getPatientId());
			//患者姓名
			cancelitem.setName(model.getName());
			//是否婴儿用药
			cancelitem.setBabyFlag(model.getBabyFlag());
			//患者所在科室
			cancelitem.setDeptCode(model.getDeptCode());
			cancelitem.setDeptName(model.getDeptName());;
			//所在病区
//			cancelitem.setCellCode(cellCode);
			//药品标志,1药品/2非药
			cancelitem.setDrugFlag(1);
			//项目编码
			cancelitem.setItemCode(model.getDrugCode());
			//项目名称
			cancelitem.setItemName(model.getDrugName());
			//规格
			cancelitem.setSpecs(model.getSpecs());
			//零售价
			cancelitem.setSalePrice(model.getDrugRetailprice());
			//申请数量
			cancelitem.setQuantity(model.getApplyNum());
			//付数
			cancelitem.setDays(model.getDays());
			//计价单位
			cancelitem.setPriceUnit(model.getShowFlag()+"");
			//执行科室
			cancelitem.setExecDpcd(model.getDrugedDept());
			cancelitem.setExecDpcdName(model.getDrugedDeptName());
			//操作员信息
			cancelitem.setOperCode(userid);
			cancelitem.setOperName(user.getName());
			cancelitem.setOperDate(date);
			cancelitem.setOperDpcd(deptid);
			cancelitem.setOperDpcdName(dept.getDeptName());
			cancelitem.setReturnReason(model.getRecipeNo());
			cancelitem.setSequenceNo(model.getSequenceNo());
			//退药确认标识 0未确认/1确认
			cancelitem.setConfirmFlag(1);
			cancelitem.setConfirmDpcd(deptid);
			cancelitem.setConfirmDpcdName(dept.getDeptName());
			cancelitem.setConfirmCode(userid);
			cancelitem.setConfirmName(user.getName());
			cancelitem.setConfirmDate(date);
			//退费标识 0未退费/1退费/2作废
			cancelitem.setChargeFlag(0);
			//1 包装 单位 0, 最小单位
			cancelitem.setExtFlag(model.getShowFlag() == null ? null : Integer.valueOf(model.getShowFlag()));
			cancelitem.setCreateUser(userid);
			cancelitem.setCreateDept(deptid);
			cancelitem.setCreateTime(date);
			cancelitem.setUpdateUser(userid);
			cancelitem.setUpdateTime(date);
			cancelList.add(cancelitem);
			
			model.setOpType(8);
			model.setUpdateUser(userid);
			model.setUpdateTime(date);
			list.add(model);
		}
		bdrugApplyInInterDao.saveOrUpdateList(cancelList);
		bdrugApplyInInterDao.saveOrUpdateList1(list);
	}

	@Override
	public void directSave(List<DrugApplyoutNow> druglist) {
		//当前登录信息
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user=  ShiroSessionUtils.getCurrentUserFromShiroSession();//当前操作人
		String deptid=dept.getDeptCode();
		String userid=user.getAccount();
		//String deptid=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId();
		//String userid=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getId();
		Date date = DateUtils.getCurrentTime();
		//产生的退费申请集合
		List<InpatientCancelitemNow> cancelList = new ArrayList<InpatientCancelitemNow>();
		List<Object> list = new ArrayList<Object>();
		//明细表正交易集合
		List<FeeInInterVo> feeVoList1 = new ArrayList<FeeInInterVo>();
		//明细表反交易集合
		List<FeeInInterVo> feeVoList2 = new ArrayList<FeeInInterVo>();
		//申请单号
		String	billCode = bdrugApplyInInterDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
		
		for(DrugApplyoutNow model : druglist){
			InpatientCancelitemNow cancelitem = new InpatientCancelitemNow();
			cancelitem.setBillCode(billCode);
			//住院流水号
			cancelitem.setInpatientNo(model.getPatientId());
			//患者姓名
			cancelitem.setName(model.getName());
			//是否婴儿用药
			cancelitem.setBabyFlag(model.getBabyFlag());
			//患者所在科室
			cancelitem.setDeptCode(model.getDeptCode());
			cancelitem.setDeptName(model.getDeptName());
			//所在病区
//			cancelitem.setCellCode(cellCode);
			//药品标志,1药品/2非药
			cancelitem.setDrugFlag(1);
			//项目编码
			cancelitem.setItemCode(model.getDrugCode());
			//项目名称
			cancelitem.setItemName(model.getTradeName());
			//规格
			cancelitem.setSpecs(model.getSpecs());
			//零售价
			cancelitem.setSalePrice(model.getDrugRetailprice());
			//申请数量
			cancelitem.setQuantity(model.getApplyNum());
			//付数
			cancelitem.setDays(model.getDays());
			//计价单位
			cancelitem.setPriceUnit(model.getShowFlag()+"");
			//执行科室
			cancelitem.setExecDpcd(model.getDrugedDept());
			cancelitem.setExecDpcdName(model.getDrugedDeptName());
			//操作员信息
			cancelitem.setOperCode(userid);
			cancelitem.setOperName(user.getName());
			cancelitem.setOperDate(date);
			cancelitem.setOperDpcd(deptid);
			cancelitem.setOperDpcdName(dept.getDeptName());
			cancelitem.setReturnReason(model.getRecipeNo());
			cancelitem.setSequenceNo(model.getSequenceNo());
			//退药确认标识 0未确认/1确认
			cancelitem.setConfirmFlag(1);
			cancelitem.setConfirmDpcd(deptid);
			cancelitem.setConfirmDpcdName(dept.getDeptName());
			cancelitem.setConfirmCode(userid);
			cancelitem.setConfirmName(user.getName());
			cancelitem.setConfirmDate(date);
			//确认单号
			cancelitem.setBillNo(bdrugApplyInInterDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14));
			//退费标识 0未退费/1退费/2作废
			cancelitem.setChargeFlag(1);
			//1 包装 单位 0, 最小单位
			cancelitem.setExtFlag(model.getShowFlag() == null ? null : Integer.valueOf(model.getShowFlag()));
			cancelitem.setCreateUser(userid);
			cancelitem.setCreateDept(deptid);
			cancelitem.setCreateTime(date);
			cancelitem.setUpdateUser(userid);
			cancelitem.setUpdateTime(date);
			cancelList.add(cancelitem);
			
			model.setOpType(8);
			model.setUpdateUser(userid);
			model.setUpdateTime(date);
			list.add(model);
			
			/***
			 * 费用接口信息
			 */
			InpatientMedicineListNow medicine = bdrugApplyInInterDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
			if(medicine.getNobackNum() != 0){
				FeeInInterVo feevo = new FeeInInterVo();
				feevo.setItemCode(medicine.getDrugCode());
				feevo.setTy("1");
				feevo.setTransType(1);
				feevo.setInpatientNo(medicine.getInpatientNo());
				feevo.setNurseCellCode(medicine.getNurseCellCode());
				feevo.setRecipeDeptCode(medicine.getRecipeDeptCode());
				feevo.setExecuteDeptCode(medicine.getExecuteDeptCode());
				feevo.setMedicineDeptcode(medicine.getMedicalteamCode());
				feevo.setRecipeDocCode(medicine.getRecipeDocCode());
				feevo.setFeeCode(medicine.getFeeCode());
				feevo.setCenterCode(medicine.getCenterCode());
				feevo.setHomeMadeFlag(medicine.getHomeMadeFlag());
				feevo.setCurrentUnit(medicine.getCurrentUnit());
				feevo.setQty(medicine.getNobackNum());
				feevo.setSenddrugFlag(medicine.getSenddrugFlag());
				feevo.setBabyFlag(medicine.getBabyFlag());
				feevo.setNobackNum(medicine.getNobackNum());
				feevo.setPactCode(medicine.getPactCode());
				feevo.setOldRecipeNo(medicine.getRecipeNo()+"_"+medicine.getSequenceNo());
				//1 代表不进行欠费判断
				feevo.setGoon(1);
				feeVoList1.add(feevo);
			}
			//费用接口--生成冲账记录
			FeeInInterVo feevo = new FeeInInterVo();
			//药品非药品标识1药品2非药品   *
			feevo.setTy("1");
			//交易类型,1正交易，2反交易                 
			feevo.setTransType(2);
			feevo.setRecipeNo(model.getRecipeNo());
			feevo.setSequenceNo(model.getSequenceNo());
			feevo.setInpatientNo(model.getPatientId());
			feeVoList2.add(feevo);
		}
		
		bdrugApplyInInterDao.saveOrUpdateList(cancelList);
		bdrugApplyInInterDao.saveOrUpdateList1(list);
		inpatientInfoInInterService.saveInpatientFeeInfo(feeVoList2);
		
		//产生新的申请，新的处方号和处方流水号需要反写到出库申请
		if(feeVoList1.size() > 0){
			Map<String,Object> map = new HashMap<String,Object>();
			map = inpatientInfoInInterService.saveInpatientFeeInfo(feeVoList1);
			if("success".equals(map.get("resCode"))){
				Map<String,String> drugMap = new HashMap<String,String>();
				Map<String,String> matMap = new HashMap<String,String>();
				List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
				
				for(FeeInInterVo vo : feeVoList1){
					//药品
					if("1".equals(vo.getTy())){
						drugMap.put(vo.getOldRecipeNo(), vo.getRecipeNo()+"_"+vo.getSequenceNo());
					}else{
						//物资
						if(2 == vo.getItemFlag()){
							matMap.put(vo.getOldRecipeNo(), vo.getRecipeNo()+"_"+vo.getSequenceNo());
						}
					}
				}
				
				if(drugMap.size() > 0){
					for(String key : drugMap.keySet()){
						String keystr[] = key.split("_");
						String valstr[] = drugMap.get(key).split("_");
						DrugApplyoutNow applyout = bdrugApplyInInterDao.obtainApplyout(keystr[0], Integer.valueOf(keystr[1]));
						applyout.setRecipeNo(valstr[0]);
						applyout.setSequenceNo(Integer.valueOf(valstr[1]));
						bdrugApplyInInterDao.update(applyout);
					}
				}
				
				if(matMap.size() > 0){
					for(String key : matMap.keySet()){
						String keystr[] = key.split("_");
						String valstr[] = matMap.get(key).split("_");
						
						MatOutput output = bdrugApplyInInterDao.getOutputByRecAndSeq(keystr[0],Integer.valueOf(keystr[1]));
						output.setRecipeNo(valstr[0]);
						output.setSequenceNo(Integer.valueOf(valstr[0]));
						bdrugApplyInInterDao.update(output);
					}
				}
			}
		}
	}
	
}
