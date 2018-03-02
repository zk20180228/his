package cn.honry.inpatient.apply.Service.impl;

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
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoMobileterService;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.apply.Service.ConfirmService;
import cn.honry.inpatient.apply.dao.DrugApplyDAO;
import cn.honry.inpatient.apply.dao.ConfirmDAO;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;


@Service("confirmService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({ "all" })
public class ConfirmServiceImpl implements ConfirmService {
	
	/***
	 * 注入本类Dao
	 */
	@Autowired
	@Qualifier("confirmDao")
	private ConfirmDAO confirmDao;
	
	/***
	 * 注入退费申请DAO
	 */
	@Autowired
	@Qualifier("drugApplyDao")
	private DrugApplyDAO drugApplyDao; 
	
	/***
	 * 住院费用接口 
	 */
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;
	public void setInpatientInfoInInterService(InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	@Autowired
	@Qualifier(value="inpatientInfoMobileterService")
	private InpatientInfoMobileterService inpatientInfoMobileterService;
	
	public void setInpatientInfoMobileterService(
			InpatientInfoMobileterService inpatientInfoMobileterService) {
		this.inpatientInfoMobileterService = inpatientInfoMobileterService;
	}
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
	@Override
	public void removeUnused(String id) {
	}

	@Override
	public InpatientCancelitemNow get(String id) {
		return confirmDao.get(id);
	}

	@Override
	public void saveOrUpdate(InpatientCancelitemNow entity) {
	}

	@Override
	public int getTatalDrugConfirm(ApplyVo entity) throws Exception {
		return confirmDao.getTatalDrugConfirm(entity);
	}

	@Override
	public List<ApplyVo> getPageDrugConfirm(ApplyVo entity, String page, String rows) throws Exception {
		return confirmDao.getPageDrugConfirm(entity, page, rows);
	}

	@Override
	public int getTatalNotDrugConfirm(ApplyVo entity) throws Exception {
		return confirmDao.getTatalNotDrugConfirm(entity);
	}

	@Override
	public List<ApplyVo> getPageNotDrugConfirm(ApplyVo apply, String page, String rows) throws Exception {
		return confirmDao.getPageNotDrugConfirm(apply, page, rows);
	}
	@Override
	public String confirmBack(String ids) throws Exception {
		User user=ShiroSessionUtils.getCurrentUserFromShiroSession();  //获得登录人 （目前是登录人，以后会是登录人所对应的员工）
		SysDepartment dept=ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();  //获得登录部门
		List<InpatientCancelitemNow> applyList = confirmDao.getChildByIds(ids);  //根据退费申请表id查询
		String arr[]=ids.split(",");
		String id="";
		String retVal=null;
		for (int i = 0; i < arr.length; i++) {
			id=arr[i];
			retVal = confirmDao.confirmBack(id, user.getAccount(), dept.getDeptCode(),user.getName(),dept.getDeptName());
		}
		OperationUtils.getInstance().conserve(id,"确认退费","UPDATE","T_INPATIENT_CANCELITEM",OperationUtils.LOGACTIONUPDATE);
		return retVal;
	}
/****************************************************************   分割线       *************************************************************************************/

	@Override
	public Map<String, Object> applyState(String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resCode","0");
		
		for(String id : ids){
			InpatientCancelitemNow model = this.get(id);
			//该申请是否被处理
			if(model.getChargeFlag() != 0){
				map.put("resCode","-1");
				map.put("resMes","项目名称（"+model.getItemName()+"），已被处理，此操作不可进行！");
				return map;
			}
		}
		
		if("0".equals(map.get("resCode"))){
			Double sum = 0.0;
			for(String id : ids){
				InpatientCancelitemNow model = this.get(id);
				sum += model.getSalePrice() * model.getQuantity();
			}
			map.put("resMes", sum);
			return map;
		}
		return map;
	}

	@Override
	public void applyConfirm(String[] ids,List<InpatientCancelitemNow> cancelList) throws Exception{
		//当前登录信息
 		SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		Date date  = DateUtils.getCurrentTime();
		
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		//正交易集合
		List<FeeInInterVo> feeVoList1 = new ArrayList<FeeInInterVo>();
		//反交易集合
		List<FeeInInterVo> feeVoList2 = new ArrayList<FeeInInterVo>();
	    //存放手术序号
		List<String> newList=new ArrayList<String>();
		if(ids != null){
			for(String id : ids){
				//操作病区退费申请表
				InpatientCancelitemNow model = this.get(id);
				String billNo = confirmDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
				//操作员编码
				model.setOperCode(user.getAccount());
				model.setOperName(user.getName());
				model.setOperDate(date);
				model.setOperDpcd(dept.getDeptCode());
				model.setOperDpcdName(dept.getDeptName());
				//确认单号
				model.setBillNo(billNo);
				//确认信息
				model.setConfirmDpcd(dept.getDeptCode());
				model.setConfirmDpcdName(dept.getDeptName());
				model.setConfirmDate(date);
				model.setConfirmCode(user.getAccount());
				model.setConfirmName(user.getName());
				//退费标识
				model.setChargeFlag(1);
				model.setUpdateUser(user.getAccount());
				model.setUpdateTime(date);
				list.add(model);
				
				if(model.getDrugFlag()==1){
					InpatientMedicineListNow medicine = confirmDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(medicine.getOperationId())){
						if(!newList.contains(medicine.getOperationId())){
							newList.add(medicine.getOperationId());
						}
					}
				}else{
					InpatientItemListNow itemList = confirmDao.getItemListByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(itemList.getOperationId())){
						if(!newList.contains(itemList.getOperationId())){
							newList.add(itemList.getOperationId());
						}
					}
				}
				
				//费用接口--生成交易记录
				FeeInInterVo vo = FeeVoforCancelitem (model,model.getQuantity());
				if(vo != null){
					feeVoList1.add(vo);
				}
			}
		}else{
			for(InpatientCancelitemNow model : cancelList){
				String billNo = confirmDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
				//操作员编码
				model.setOperName(user.getName());
				model.setOperDate(date);
				model.setOperDpcd(dept.getDeptCode());
				model.setOperDpcdName(dept.getDeptName());
				//确认单号
				model.setBillNo(billNo);
				//确认信息
				model.setConfirmDpcd(dept.getDeptCode());
				model.setConfirmDpcdName(dept.getDeptName());
				model.setConfirmDate(date);
				model.setConfirmCode(user.getAccount());
				model.setConfirmName(user.getName());
				//退费标识
				model.setChargeFlag(1);
				model.setUpdateUser(user.getAccount());
				model.setUpdateTime(date);
				list.add(model);
				
				if(model.getDrugFlag()==1){
					InpatientMedicineListNow medicine = confirmDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(medicine.getOperationId())){
						if(!newList.contains(medicine.getOperationId())){
							newList.add(medicine.getOperationId());
						}
					
					}
				}else{
					InpatientItemListNow itemList = confirmDao.getItemListByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(itemList.getOperationId())){
						if(!newList.contains(itemList.getOperationId())){
							newList.add(itemList.getOperationId());
						}
					}
				}
				
				//费用接口--生成交易记录
				FeeInInterVo vo = FeeVoforCancelitem(model,model.getQuantity());
				if(vo != null){
					feeVoList1.add(vo);
				}
			}
		}
		
		confirmDao.saveOrUpdateList(list);
		//产生新的申请，新的处方号和处方流水号需要反写到出库申请
		Map<String,Object> map = new HashMap<String,Object>();
		map = inpatientInfoInInterService.reverseTran(feeVoList1);
		if(feeVoList1.size() > 0){
			if("success".equals(map.get("resCode"))){
				List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
				
				for(FeeInInterVo vo : feeVoList1){
					//药品
					if("1".equals(vo.getTy())){
						if(map.size() > 0){
							String valueStr = (String) map.get(vo.getRecipeNo()+"-"+vo.getSequenceNo().toString());
							if(valueStr==null){
							}else{
								DrugApplyoutNow applyout = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
								String valstr[] = valueStr.split("-");
								applyout.setRecipeNo(valstr[0]);
								applyout.setSequenceNo(Integer.valueOf(valstr[1]));
								listApplyout.add(applyout);
								drugApplyDao.saveOrUpdateListApplyout(listApplyout);
							}
						}
					}else{
						//物资
						if(vo.getItemFlag()!=null){
							if(2 == vo.getItemFlag()){
								//判断非药品是否是物资
								String valueStr = (String) map.get(vo.getRecipeNo()+"-"+vo.getSequenceNo().toString());
								if(valueStr==null){
								}else{
									MatOutput output = confirmDao.getOutputByRecAndSeq(vo.getRecipeNo(), vo.getSequenceNo());
									if(output!=null){
										String valstr[] = valueStr.split("-");
										String recipeNo = valstr[0];
										output.setRecipeNo(recipeNo);
										output.setSequenceNo(Integer.valueOf(valstr[1]));
										confirmDao.updateMatOutput(output);
									}
								}
							}
						}
					}
				}
			}
		}
		//如果为手术退费，修改手术申请表，及登记表中的收费标记，改为未收费
		confirmDao.updateOperApply(newList);
		confirmDao.updateOperRecord(newList);
		
	}
	
	/***
	 * 根据退费申请，调用接口，生成费用记录
	 * @Title: FeeVoforCancelitem 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @return FeeVo
	 * @version 1.0
	 */
	public FeeInInterVo FeeVoforCancelitem(InpatientCancelitemNow CancelModel,Double qty) throws Exception{
		FeeInInterVo feevo = new FeeInInterVo();
		// 药品标志,1药品/2非药
		if(CancelModel.getDrugFlag() != null && CancelModel.getDrugFlag().equals(1)){
			InpatientMedicineListNow medicine = confirmDao.getChildByRecipe(CancelModel.getRecipeNo(), CancelModel.getSequenceNo());
			feevo.setItemCode(medicine.getDrugCode());
			feevo.setTy("1");
			feevo.setTransType(2);
			feevo.setRecipeNo(medicine.getRecipeNo());
			feevo.setSequenceNo(medicine.getSequenceNo());
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
			feevo.setQty(qty);
			feevo.setSenddrugFlag(medicine.getSenddrugFlag());
			feevo.setBabyFlag(medicine.getBabyFlag());
			feevo.setNobackNum(medicine.getNobackNum());
			feevo.setPactCode(medicine.getPactCode());
			return feevo;
		}else{
			InpatientItemListNow item = confirmDao.getItemListByRecipe(CancelModel.getRecipeNo(), CancelModel.getSequenceNo());
			feevo.setItemCode(item.getItemCode());
			feevo.setTy("2");
			feevo.setTransType(2);
			feevo.setRecipeNo(item.getRecipeNo());
			feevo.setSequenceNo(item.getSequenceNo());
			feevo.setInpatientNo(item.getInpatientNo());
			feevo.setNurseCellCode(item.getNurseCellCode());
			feevo.setRecipeDeptCode(item.getRecipeDeptcode());
			feevo.setExecuteDeptCode(item.getExecuteDeptcode());
			feevo.setMedicineDeptcode(item.getMedicalteamCode());
			feevo.setRecipeDocCode(item.getRecipeDoccode());
			feevo.setFeeCode(item.getFeeCode());
			feevo.setCenterCode(item.getCenterCode());
			feevo.setCurrentUnit(item.getCurrentUnit());
			feevo.setQty(qty);
			feevo.setSenddrugFlag(item.getSendFlag());
			feevo.setBabyFlag(item.getBabyFlag());
			feevo.setNobackNum(item.getNobackNum());
			feevo.setPactCode(item.getPactCode());
			feevo.setItemFlag(item.getItemFlag());
			return feevo;
		}
	}

	@Override
	public void applyConfirm(String[] ids,List<InpatientCancelitemNow> cancelList, String deptCode,
			String empJobNo) throws Exception {
		//当前登录信息
		User user=userInInterService.getByCode(empJobNo);
		SysDepartment logDept=deptInInterService.getDeptCode(deptCode);
		Date date  = DateUtils.getCurrentTime();
		
		List<InpatientCancelitemNow> list = new ArrayList<InpatientCancelitemNow>();
		//正交易集合
		List<FeeInInterVo> feeVoList1 = new ArrayList<FeeInInterVo>();
		//反交易集合
		List<FeeInInterVo> feeVoList2 = new ArrayList<FeeInInterVo>();
	    //存放手术序号
		List<String> newList=new ArrayList<String>();
		if(ids != null){
			for(String id : ids){
				//操作病区退费申请表
				InpatientCancelitemNow model = this.get(id);
				String billNo = confirmDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
				//操作员编码
				model.setOperCode(empJobNo);
				model.setOperName(user.getName());
				model.setOperDate(date);
				model.setOperDpcd(deptCode);
				model.setOperDpcdName(logDept.getDeptName());
				//确认单号
				model.setBillNo(billNo);
				//确认信息
				model.setConfirmDpcd(deptCode);
				model.setConfirmDpcdName(logDept.getDeptName());
				model.setConfirmDate(date);
				model.setConfirmCode(empJobNo);
				model.setConfirmName(user.getName());
				//退费标识
				model.setChargeFlag(1);
				model.setUpdateUser(empJobNo);
				model.setUpdateTime(date);
				list.add(model);
				
				if(model.getDrugFlag()==1){
					InpatientMedicineListNow medicine = confirmDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(medicine.getOperationId())){
						if(!newList.contains(medicine.getOperationId())){
							newList.add(medicine.getOperationId());
						}
					}
				}else{
					InpatientItemListNow itemList = confirmDao.getItemListByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(itemList.getOperationId())){
						if(!newList.contains(itemList.getOperationId())){
							newList.add(itemList.getOperationId());
						}
					}
				}
				
				//费用接口--生成交易记录
				FeeInInterVo vo = FeeVoforCancelitem (model,model.getQuantity());
				if(vo != null){
					feeVoList1.add(vo);
				}
			}
		}else{
			for(InpatientCancelitemNow model : cancelList){
				String billNo = confirmDao.getSeqByNameorNum("SEQ_INPATIENT_CAN_BILLCODE", 14);
				//操作员编码
				model.setOperName(empJobNo);
				model.setOperDate(date);
				model.setOperDpcd(deptCode);
				model.setOperDpcdName(logDept.getDeptName());
				//确认单号
				model.setBillNo(billNo);
				//确认信息
				model.setConfirmDpcd(deptCode);
				model.setConfirmDpcdName(logDept.getDeptName());
				model.setConfirmDate(date);
				model.setConfirmCode(empJobNo);
				model.setConfirmName(user.getName());
				//退费标识
				model.setChargeFlag(1);
				model.setUpdateUser(empJobNo);
				model.setUpdateTime(date);
				list.add(model);
				
				if(model.getDrugFlag()==1){
					InpatientMedicineListNow medicine = confirmDao.getChildByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(medicine.getOperationId())){
						if(!newList.contains(medicine.getOperationId())){
							newList.add(medicine.getOperationId());
						}
					
					}
				}else{
					InpatientItemListNow itemList = confirmDao.getItemListByRecipe(model.getRecipeNo(), model.getSequenceNo());
					if(StringUtils.isNotBlank(itemList.getOperationId())){
						if(!newList.contains(itemList.getOperationId())){
							newList.add(itemList.getOperationId());
						}
					}
				}
				
				//费用接口--生成交易记录
				FeeInInterVo vo = FeeVoforCancelitem(model,model.getQuantity());
				if(vo != null){
					feeVoList1.add(vo);
				}
			}
		}
		
		confirmDao.saveOrUpdateList(list);
		//产生新的申请，新的处方号和处方流水号需要反写到出库申请
		Map<String,Object> map = new HashMap<String,Object>();
		map = inpatientInfoMobileterService.reverseTran(feeVoList1,empJobNo,deptCode);
		if(feeVoList1.size() > 0){
			if("success".equals(map.get("resCode"))){
				List<DrugApplyoutNow> listApplyout = new ArrayList<DrugApplyoutNow>();
				
				for(FeeInInterVo vo : feeVoList1){
					//药品
					if("1".equals(vo.getTy())){
						if(map.size() > 0){
							String valueStr = (String) map.get(vo.getRecipeNo()+"-"+vo.getSequenceNo().toString());
							if(valueStr==null){
							}else{
								DrugApplyoutNow applyout = drugApplyDao.obtainApplyout(vo.getRecipeNo(), vo.getSequenceNo());
								String valstr[] = valueStr.split("-");
								applyout.setRecipeNo(valstr[0]);
								applyout.setSequenceNo(Integer.valueOf(valstr[1]));
								listApplyout.add(applyout);
								drugApplyDao.saveOrUpdateListApplyout(listApplyout);
							}
						}
					}else{
						//物资
						if(vo.getItemFlag()!=null){
							if(2 == vo.getItemFlag()){
								//判断非药品是否是物资
								String valueStr = (String) map.get(vo.getRecipeNo()+"-"+vo.getSequenceNo().toString());
								if(valueStr==null){
								}else{
									MatOutput output = confirmDao.getOutputByRecAndSeq(vo.getRecipeNo(), vo.getSequenceNo());
									if(output!=null){
										String valstr[] = valueStr.split("-");
										String recipeNo = valstr[0];
										output.setRecipeNo(recipeNo);
										output.setSequenceNo(Integer.valueOf(valstr[1]));
										confirmDao.updateMatOutput(output);
									}
								}
							}
						}
					}
				}
			}
		}
		//如果为手术退费，修改手术申请表，及登记表中的收费标记，改为未收费
		confirmDao.updateOperApply(newList,empJobNo);
		confirmDao.updateOperRecord(newList,empJobNo);
		
	}
	
}
