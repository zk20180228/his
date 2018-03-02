package cn.honry.inpatient.exitNoFee.service.impl;

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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessMedicalGroupInfo;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InsuranceSiitem;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.outpatient.inpatNumber.service.InpatNumInnerService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.docAdvManage.dao.DocAdvManageDAO;
import cn.honry.inpatient.exitNoFee.dao.ExitNoFeeDAO;
import cn.honry.inpatient.exitNoFee.service.ExitNoFeeService;
import cn.honry.inpatient.exitNoFee.vo.FeeVo;
import cn.honry.inpatient.exitNoFee.vo.InpatientInfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("exitNoFeeService")
@Transactional
@SuppressWarnings({"all"})
public class ExitNoFeeServiceImpl implements ExitNoFeeService{
	
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
	
	@Autowired
	private ExitNoFeeDAO exitNoFeeDAO;
	@Autowired
	@Qualifier(value = "docAdvManageDAO")
	private DocAdvManageDAO docAdvManageDAO;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**
	 * 注入住院次数Service
	 */
	@Autowired 
	@Qualifier(value = "inpatNumInnerService")
	private InpatNumInnerService inpatNumInnerService;
	
	 
	public void setInpatNumInnerService(InpatNumInnerService inpatNumInnerService) {
		this.inpatNumInnerService = inpatNumInnerService;
	}
	//注入科室接口
	private DeptInInterService deptInInterService;
	@Autowired
	@Qualifier(value="deptInInterService")
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	/**
	 * @Description: 获取患者信息
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-9
	 * @param  inpatientInfoSerch：患者信息实体类
	 * @return List<InpatientInfo>  
	 * @version 1.0
	**/
	@Override
	public List<InpatientInfoVo> getInpatientInfo(InpatientInfoNow inpatientInfoSerch) throws Exception {
		List<InpatientInfoVo> inpatientInfoList = exitNoFeeDAO.getInpatientInfo(inpatientInfoSerch);
		String inpatientNo = inpatientInfoSerch.getInpatientNo();
		if(StringUtils.isNotBlank(inpatientNo)){
			String info = exitNoFeeDAO.isExitNoFee(inpatientNo);
		}
		return inpatientInfoList;
	}
	@Override
	public List<InpatientInfoVo> getInpatientInfoByInNo(InpatientInfoNow inpatientInfoSerch) throws Exception {
		String inpatientNo = inpatientInfoSerch.getInpatientNo();
		List<InpatientInfoVo> inpatientInfoList = new ArrayList<InpatientInfoVo>();
		String info = exitNoFeeDAO.isExitNoFee(inpatientNo);
		if("have".equals(info)){
			return inpatientInfoList;
		}else{
			 inpatientInfoList = exitNoFeeDAO.getInpatientInfo(inpatientInfoSerch);
			 return inpatientInfoList;
		}	
		
		
	}
	/**
	 * @Description:资料变更
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-3-4
	 * @param user：用户信息实体类   
	 * @return void  
	 * @version 1.0
	 * @throws Exception 
	**/
	@Override
	public String changeHospitalState(String ids,InpatientInfoNow inpatientInfo) throws Exception {
		 String flag=exitNoFeeDAO.changeHospitalState(ids);
		 OperationUtils.getInstance().conserve(ids,"无费出院管理","UPDATE","T_BUSINESS_HOSPITALBED",OperationUtils.LOGACTIONUPDATE);
		 List<InpatientInfoVo> inpatientInfoList = exitNoFeeDAO.queryInpatientInfoById(ids);
		 User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//获取登录人
		 SysDepartment  loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
		 if(inpatientInfoList!=null&&inpatientInfoList.size()>0){			 
			 if(StringUtils.isNotBlank(inpatientInfo.getBedId())){
				 if(!inpatientInfo.getBedId().equals(inpatientInfoList.get(0).getBedId())){	 
				 	 InpatientShiftData inpatientShiftData = new InpatientShiftData();
					 inpatientShiftData.setId(null);
					 inpatientShiftData.setClinicNo(inpatientInfo.getInpatientNo());
					 List<InpatientShiftData> inpatientShift= exitNoFeeDAO.queryMaxHappenNo();
					 inpatientShiftData.setHappenNo(inpatientShift.get(0).getHappenNo()+1);
					 inpatientShiftData.setShiftType("OF");
					 inpatientShiftData.setOldDataCode(inpatientInfo.getBedId());
					 inpatientShiftData.setOldDataName(inpatientInfo.getBedName());
					 inpatientShiftData.setNewDataCode(inpatientInfoList.get(0).getBedId());
					 inpatientShiftData.setNewDataName(inpatientInfoList.get(0).getBedName());
					 inpatientShiftData.setShiftCause("无费退院");
					 inpatientShiftData.setTableName("T_INPATIENT_INFO_NOW");
					 inpatientShiftData.setChangePropertyName("BEDINFO_ID");
					 inpatientShiftData.setCreateUser(user.getAccount());//创建人
					 inpatientShiftData.setCreateDept(loginDept.getDeptCode());//创建科室
					 inpatientShiftData.setCreateTime(DateUtils.getCurrentTime());//创建时间
					 exitNoFeeDAO.save(inpatientShiftData);
					 OperationUtils.getInstance().conserve(null,"资料变更","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
				 }			 
			 }
			 if(StringUtils.isNotBlank(inpatientInfo.getDeptCode())){
				 if(!inpatientInfo.getDeptCode().equals(inpatientInfoList.get(0).getDeptCode())){
					 InpatientShiftData inpatientShiftData = new InpatientShiftData();
					 inpatientShiftData.setId(null);
					 inpatientShiftData.setClinicNo(inpatientInfo.getInpatientNo());
					 List<InpatientShiftData> inpatientShift= exitNoFeeDAO.queryMaxHappenNo();
					 inpatientShiftData.setHappenNo(inpatientShift.get(0).getHappenNo()+1);
					 inpatientShiftData.setShiftType("OF");
					 inpatientShiftData.setOldDataCode(inpatientInfo.getDeptCode());
					 inpatientShiftData.setOldDataName(inpatientInfo.getDeptName());
					 inpatientShiftData.setNewDataCode(inpatientInfoList.get(0).getDeptCode());
					 inpatientShiftData.setNewDataName(inpatientInfoList.get(0).getDeptName());
					 inpatientShiftData.setShiftCause("无费退院");
					 inpatientShiftData.setTableName("T_INPATIENT_INFO_NOW");
					 inpatientShiftData.setChangePropertyName("DEPT_CODE");
					 inpatientShiftData.setCreateUser(user.getAccount());//创建人
					 inpatientShiftData.setCreateDept(loginDept.getDeptCode());//创建科室
					 inpatientShiftData.setCreateTime(DateUtils.getCurrentTime());//创建时间
					 exitNoFeeDAO.save(inpatientShiftData);
					 OperationUtils.getInstance().conserve(null,"资料变更","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
				 }		 
			 }
			  if(StringUtils.isNotBlank(inpatientInfo.getNurseCellCode())){
				  if(!inpatientInfo.getNurseCellCode().equals(inpatientInfoList.get(0).getNurseCellCode())){
					 InpatientShiftData inpatientShiftData = new InpatientShiftData();
					 inpatientShiftData.setId(null);
					 inpatientShiftData.setClinicNo(inpatientInfo.getInpatientNo());
					 List<InpatientShiftData> inpatientShift= exitNoFeeDAO.queryMaxHappenNo();
					 inpatientShiftData.setHappenNo(inpatientShift.get(0).getHappenNo()+1);
					 inpatientShiftData.setShiftType("OF");
					 inpatientShiftData.setOldDataCode(inpatientInfo.getNurseCellCode());
					 inpatientShiftData.setOldDataName(inpatientInfo.getBingqu());
					 inpatientShiftData.setNewDataCode(inpatientInfoList.get(0).getNurseCellCode());
					 inpatientShiftData.setNewDataName(inpatientInfoList.get(0).getNurseCellName());
					 inpatientShiftData.setShiftCause("无费退院");
					 inpatientShiftData.setTableName("T_INPATIENT_BEDINFO");
					 inpatientShiftData.setChangePropertyName("NURSE_CELL_CODE");
					 inpatientShiftData.setCreateUser(user.getAccount());//创建人
					 inpatientShiftData.setCreateDept(loginDept.getDeptCode());//创建科室
					 inpatientShiftData.setCreateTime(DateUtils.getCurrentTime());//创建时间
					 exitNoFeeDAO.save(inpatientShiftData);
					 OperationUtils.getInstance().conserve(null,"资料变更","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
				 }			 
			 }
		 }	
		//住院次数
		inpatNumInnerService.saveInpatNum(inpatientInfo.getId());
		return flag;
	}
	/**
	 * @Description:费用接口
	 * @Author： yeguanqun
	 * @CreateDate： 2016-2-19
	 * @param feeVoList：费用结算vo集合
	 * map中的几种消息状态及信息：
	 * resCode：对应的处理状态的信息提示
	 * resMsg：表示处理的状态
	 * resCode="success" 生成费用成功
	 * resCode="arrearage" -患者余额不足
	 * resCode="error"-患者正在出院结算或已经无费退院，不能进行收费操作、婴儿患者母亲信息不存在，不能进行收费操作、婴儿患者母亲非在院状态，请先进行召回操作、患者账户关闭，不能进行收费操作
	 * @version 1.0
	**/
	@Override
	public Map<String, Object> saveInpatientFeeInfo(List<FeeVo> feeVoList) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();//返回信息
		if(feeVoList==null||feeVoList.size()<=0){
			map.put("resCode", "error");
			map.put("resMsg", "无费用信息要保存！");
			return map;
		}
		if(feeVoList.get(0).getTransType()==2){//反交易
			map = reverseTran(feeVoList);
			return map;
		}		
		try{
		List<InpatientInfoNow> inpatientInfoList = exitNoFeeDAO.queryInpatientInfo(feeVoList.get(0).getInpatientNo());	//患者信息-住院主表
		String patientName = null;//患者姓名
		String pactCode = null;//合同单位
		String depCode = null;//在院科室代码
		if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
			if(inpatientInfoList.get(0).getPatientName()!=null){
				patientName = inpatientInfoList.get(0).getPatientName();//患者姓名
			}
			if(feeVoList.get(0).getPactCode()!=null){
				pactCode = feeVoList.get(0).getPactCode();
			}else{
				if(inpatientInfoList.get(0).getPactCode()!=null){
					pactCode = inpatientInfoList.get(0).getPactCode();//合同单位
				}
			}		
			if(inpatientInfoList.get(0).getDeptCode()!=null){
				depCode = inpatientInfoList.get(0).getDeptCode();//在院科室代码
			}
		}
		List<BusinessContractunit> contractunitList = exitNoFeeDAO.queryContractunit(inpatientInfoList.get(0).getPaykindCode());//查询合同单位维护表-合同单位比例
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//获取登录人		
		double totCost2=0;//本次全部费用金额	(未结)
		double ownCost2 = 0;// 本次全部自费金额(未结)
		double payCost2 = 0;// 本次全部自付金额(预留--暂时等于自费金额)(未结)
		double pubCost2 = 0;// 本次全部公费金额(未结)
		double ecoCost2 = 0;// 本次全部优惠金额 (未结)	
		double changeTotcost2 = 0;//本次全部转入费用金额(未结)
		String recipeNo1 ="";//网页面传的处方号集合（汇总表中存在）护士站收费用到
		String recipeNo2 ="";//网页面传的处方号集合（汇总表中不存在）护士站收费用到
		String orderId = "";//网页面传的医嘱Id（汇总表中不存在）
		double qty = 0;//网页面传的数量（汇总表中不存在）
		String recipeNo3 ="";//网页面传的处方号集合 公用
		String SequenceNo3 = "";//网页面传的处方流水号集合 公用
		for(int t=0;t<feeVoList.size();t++){//计算全部费用
			List<DrugInfo> drugInfoList8 = null;//查询药品信息
			List<DrugUndrug> drugUndrugList8 = null;//查询非药品信息
			List<InsuranceSiitem> insuranceSiitemList8 = exitNoFeeDAO.queryInsuranceSiitem(feeVoList.get(t).getItemCode());//查询自负比例
			if(feeVoList.get(0).getOrderId()!=null){
				orderId = feeVoList.get(0).getOrderId();//网页面传的医嘱Id（汇总表中不存在）
			}	
			if(feeVoList.get(0).getQty()!=null){
				qty = feeVoList.get(0).getQty();//网页面传的数量（汇总表中不存在））
			}	
			if("1".equals(feeVoList.get(t).getTy())){//药品
				drugInfoList8 = exitNoFeeDAO.queryDrugInfo(feeVoList.get(t).getItemCode());//查询药品信息
				double num = 0;
				String drugPackagingunit ="";//包装单位
				int packagingnum = 1;//包装数量
				if(drugInfoList8.get(0).getDrugPackagingunit()!=null){
					drugPackagingunit = drugInfoList8.get(0).getDrugPackagingunit();
				}
				if(drugInfoList8.get(0).getPackagingnum()!=null){
					packagingnum = drugInfoList8.get(0).getPackagingnum();
				}
				if(!"".equals(drugPackagingunit)&&feeVoList.get(t).getCurrentUnit().equals(drugPackagingunit)){//当前单位为包装单位
					num = feeVoList.get(t).getQty();
				}else{//当前单位为最小单位
					num = feeVoList.get(t).getQty()/packagingnum;
				}
				double totCost = 0;
				if(drugInfoList8!=null&&drugInfoList8.size()>0){
					if(drugInfoList8.get(0).getDrugRetailprice()!=null){
						totCost=drugInfoList8.get(0).getDrugRetailprice()*num;// 费用金额计算
					}					
				}
				totCost2 = totCost2+totCost;// 本次全部费用金额计算					
				double pubRatio = 0;
				if(contractunitList!=null&& contractunitList.size()>0&&contractunitList.get(0).getPubRatio()!=null){
					pubRatio=contractunitList.get(0).getPubRatio();//公费比例\报销比例
				}	
				String itemGrade="";
				if(insuranceSiitemList8!=null&&insuranceSiitemList8.size()>0&&insuranceSiitemList8.get(0).getItemGrade()!=null){
					itemGrade = insuranceSiitemList8.get(0).getItemGrade();
				}
				double rate=0;
				if(insuranceSiitemList8!=null&&insuranceSiitemList8.size()>0&&insuranceSiitemList8.get(0).getRate()!=null){
					rate = insuranceSiitemList8.get(0).getRate();
				}
				double ecoCost = 0;
				if(feeVoList.get(t).getEcoCost() != null){
					ecoCost = feeVoList.get(t).getEcoCost();
				}
				if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
					double pubCost = (totCost-ecoCost)*pubRatio;
					pubCost2 = pubCost2+pubCost;
					double ownCost = totCost-ecoCost-pubCost;
					ownCost2 = ownCost2+ownCost;
				}
				if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
					double pubCost = (totCost-ecoCost)*(1-rate)*pubRatio;
					pubCost2 = pubCost2+pubCost;
					double ownCost = totCost-ecoCost-pubCost;
					ownCost2 = ownCost2+ownCost;
				}
				if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
					double ownCost = totCost-feeVoList.get(t).getEcoCost();
					ownCost2 = ownCost2+ownCost;
				}				
				ecoCost2 = ecoCost2+feeVoList.get(t).getEcoCost();// 本次全部优惠金额 (未结)
				double changeTotcost3 = 0;
				if(feeVoList.get(t).getChangeTotcost()!=null){
					changeTotcost3=feeVoList.get(t).getChangeTotcost();
				}
				changeTotcost2 =changeTotcost2 +changeTotcost3;//本次全部转入费用金额(未结)			
			}
			if("2".equals(feeVoList.get(t).getTy())){//非药品
				drugUndrugList8 = docAdvManageDAO.queryNoDrugInfo(feeVoList.get(t).getItemCode());//查询非药品信息
				double num = feeVoList.get(t).getQty();			
				double totCost = 0;
				if(drugUndrugList8!=null&&drugUndrugList8.size()>0&&drugUndrugList8.get(0).getDefaultprice()!=null){
					totCost=drugUndrugList8.get(0).getDefaultprice()*num;// 费用金额计算
				}					
				totCost2 = totCost2+totCost;// 本次全部费用金额计算					
				double pubRatio = 0;
				if(contractunitList!=null&& contractunitList.size()>0){
					pubRatio=contractunitList.get(0).getPubRatio();//公费比例\报销比例
				}
				String itemGrade="";
				if(insuranceSiitemList8!=null&&insuranceSiitemList8.size()>0&&insuranceSiitemList8.get(0).getItemGrade()!=null){
					itemGrade = insuranceSiitemList8.get(0).getItemGrade();
				}
				double rate=0;
				if(insuranceSiitemList8!=null&&insuranceSiitemList8.size()>0&&insuranceSiitemList8.get(0).getRate()!=null){
					rate = insuranceSiitemList8.get(0).getRate();
				}
				double ecoCost = 0;
				if(feeVoList.get(t).getEcoCost() != null){
					ecoCost = feeVoList.get(t).getEcoCost();
				}
				if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
					double pubCost = (totCost-ecoCost)*pubRatio;
					pubCost2 = pubCost2+pubCost;
					double ownCost = totCost-ecoCost-pubCost;
					ownCost2 = ownCost2+ownCost;
				}
				if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
					double pubCost = (totCost-ecoCost)*(1-rate)*pubRatio;
					pubCost2 = pubCost2+pubCost;
					double ownCost = totCost-ecoCost-pubCost;
					ownCost2 = ownCost2+ownCost;
				}
				if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
					double ownCost = totCost-ecoCost;
					ownCost2 = ownCost2+ownCost;
				}				
				ecoCost2 = ecoCost2+ecoCost;// 本次全部优惠金额 (未结)
				double changeTotcost3 = 0;
				if(feeVoList.get(t).getChangeTotcost()!=null){
					changeTotcost3=feeVoList.get(t).getChangeTotcost();
				}
				changeTotcost2 =changeTotcost2 +changeTotcost3;//本次全部转入费用金额(未结)			
			}	
		}
		for(int m=0;m<feeVoList.size();m++){//根据最小费用和执行科室分组
			List<DrugInfo> drugInfoList1 = null;//查询药品信息
			List<DrugUndrug> drugUndrugList1 = null;//查询非药品信息
			int w = m;
			int t = 1;	
			String a = null;
			String feeCode = "";//最小费用代码
			String executeDeptCode = null;//执行科室代码 		
			if("1".equals(feeVoList.get(m).getTy())){//药品
				drugInfoList1 = exitNoFeeDAO.queryDrugInfo(feeVoList.get(m).getItemCode());//查询药品信息
				if(drugInfoList1!=null&&drugInfoList1.size()>0&&drugInfoList1.get(0).getDrugMinimumcost()!=null){
					feeCode = drugInfoList1.get(0).getDrugMinimumcost();//最小费用代码
				}					
				feeVoList.get(m).setFeeCode(feeCode);//最小费用代码
				if(StringUtils.isNotBlank(feeVoList.get(m).getRecipeNo())){
					List<InpatientMedicineList> inpatientMedicineList1 = exitNoFeeDAO.queryInpatientMedicineList(feeVoList.get(m).getRecipeNo());//查询药品明细表
					if(inpatientMedicineList1!=null&&inpatientMedicineList1.size()>0){
						a =inpatientMedicineList1.get(0).getRecipeNo();
						t = inpatientMedicineList1.get(0).getSequenceNo()+1;
					}else{
						a = "Y"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
					}
				}else{
					a = "Y"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
				}			
				feeVoList.get(m).setRecipeNo(a);//处方号
				feeVoList.get(m).setSequenceNo(t);//处方内项目流水号				
			}
			if("2".equals(feeVoList.get(m).getTy())){//非药品
				drugUndrugList1 = docAdvManageDAO.queryNoDrugInfo(feeVoList.get(m).getItemCode());//查询非药品信息
				if(drugUndrugList1!=null&&drugUndrugList1.size()>0&&drugUndrugList1.get(0).getUndrugMinimumcost()!=null){
					feeCode = drugUndrugList1.get(0).getUndrugMinimumcost();//最小费用代码
				}				
				feeVoList.get(m).setFeeCode(feeCode);//最小费用代码
				if(StringUtils.isNotBlank(feeVoList.get(m).getRecipeNo())){
					List<InpatientItemList> inpatientItemList1 = exitNoFeeDAO.queryInpatientItemList(feeVoList.get(m).getRecipeNo());//查询非药品明细表
					if(inpatientItemList1!=null&&inpatientItemList1.size()>0){
						a =inpatientItemList1.get(0).getRecipeNo();
						t = inpatientItemList1.get(0).getSequenceNo()+1;
					}else{
						a = "F"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
					}
				}else{
					a = "F"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
				}					
				feeVoList.get(m).setRecipeNo(a);//处方号
				feeVoList.get(m).setSequenceNo(t);//处方内项目流水号				
			}	
			for(int n=m+1;n<feeVoList.size();n++){
				FeeVo feeVo1 = new FeeVo();
				String feeCode1 = "";//最小费用代码
				if("1".equals(feeVoList.get(n).getTy())){//药品
					List<DrugInfo> drugInfoList2 = exitNoFeeDAO.queryDrugInfo(feeVoList.get(n).getItemCode());//查询药品信息
					if(drugInfoList2!=null&&drugInfoList2.size()>0&&drugInfoList2.get(0).getDrugMinimumcost()!=null){
						feeCode1 = drugInfoList2.get(0).getDrugMinimumcost();//最小费用代码
					}				
					feeVoList.get(n).setFeeCode(feeCode1);//最小费用代码
				}
				if("2".equals(feeVoList.get(n).getTy())){//非药品
					List<DrugUndrug> drugUndrugList2 = docAdvManageDAO.queryNoDrugInfo(feeVoList.get(n).getItemCode());//查询非药品信息
					if(drugUndrugList2!=null&&drugUndrugList2.size()>0&&drugUndrugList2.get(0).getUndrugMinimumcost()!=null){
						feeCode1 = drugUndrugList2.get(0).getUndrugMinimumcost();//最小费用代码
					}					
					feeVoList.get(n).setFeeCode(feeCode1);//最小费用代码			
				}	
				if(!"".equals(feeVoList.get(m).getFeeCode())&&!"".equals(feeVoList.get(n).getFeeCode())&&!"".equals(feeVoList.get(m).getExecuteDeptCode())&&!"".equals(feeVoList.get(n).getExecuteDeptCode())&&feeVoList.get(m).getExecuteDeptCode()!=null&&feeVoList.get(m).getExecuteDeptCode()!=null){
					if(feeVoList.get(m).getFeeCode().equals(feeVoList.get(n).getFeeCode())&&feeVoList.get(m).getExecuteDeptCode().equals(feeVoList.get(n).getExecuteDeptCode())&&feeVoList.get(m).getTy().equals(feeVoList.get(n).getTy())){
						feeVo1 = feeVoList.get(n);
						t++;					
						feeVo1.setRecipeNo(a);//处方号
						feeVo1.setSequenceNo(t);//处方内项目流水号										
						feeVoList.remove(n);
						feeVoList.add(m, feeVo1);
						w++;
					}
				}			
			}				
			m = w;
		}
		int qw = 0;
		if(feeVoList.get(0).getGoon()!=null){
			qw = feeVoList.get(0).getGoon();
		}
		if(qw!=1){//未进行欠费判断
			List<InpatientSurety> inpatientSuretyList = exitNoFeeDAO.querysuretyCost(inpatientInfoList.get(0).getInpatientNo());//查询患者账户明细表-获取担保金额
			double cost = 0;//余额或预交金
			double detailDebitamount = 0;//担保金额
			double moneyAlert = 0;//警戒线金额
			if(inpatientInfoList.get(0).getPrepayCost()!=null){
				cost=inpatientInfoList.get(0).getPrepayCost();
			}
			if(inpatientSuretyList.size()>0){
				if(inpatientSuretyList.get(0).getSuretyCost()!=null){
					detailDebitamount=inpatientSuretyList.get(0).getSuretyCost();
				}
			}
			if(inpatientInfoList.get(0).getMoneyAlert()!=null){
				moneyAlert = inpatientInfoList.get(0).getMoneyAlert();
			}
			String paykindCode = "";
			if(contractunitList!=null&& contractunitList.size()>0){
				paykindCode=contractunitList.get(0).getPaykindCode();//公费比例\报销比例
			}	
			if("02".equals(paykindCode)){//患者合同单位的结算类型为02（公费）							
				double h=cost+detailDebitamount-totCost2;//欠费计算
				if(h<moneyAlert){
					map.put("resCode", "arrearage");
					map.put("resMsg", "用户患者余额不足");//用户患者余额不足	
					map.put("userId", user.getAccount());//用户Id
					map.put("totCost", totCost2);//本次全部总费用
					map.put("orderId", orderId);//网页面传的医嘱Id集合（汇总表中不存在）
					map.put("inpatientNo", feeVoList.get(0).getInpatientNo());//患者住院流水号
					map.put("qty", qty);//网页面传的数量（汇总表中不存在）
					return map;									
				}
				else{
					qw=1;
				}
			}else{			
				double j=cost+detailDebitamount-totCost2;//欠费计算				
				if(j<moneyAlert){
					map.put("resCode", "arrearage");
					map.put("resMsg", "用户患者余额不足");//用户患者余额不足	
					map.put("userId", user.getAccount());//用户Id
					map.put("totCost", totCost2);//本次全部总费用
					map.put("orderId", orderId);//网页面传的医嘱Id集合（汇总表中不存在）
					map.put("inpatientNo", feeVoList.get(0).getInpatientNo());//患者住院流水号
					map.put("qty", qty);//网页面传的数量（汇总表中不存在）
					return map;	
				}
				else{
					qw=1;
				}
			}
		}
		if(qw==1){//欠费之后,选择继续操作，收费+医保处理继续
			InpatientInfoNow inpatientInfo = inpatientInfoList.get(0);
			double totCost = 0;
			double ownCost = 0;
			double payCost = 0;
			double pubCost = 0;
			double ecoCost = 0;
			double freeCost = 0;
			double prepayCost = 0;
			if(inpatientInfo.getTotCost()!=null){
				totCost = inpatientInfo.getTotCost();
			}
			if(inpatientInfo.getOwnCost()!=null){
				ownCost = inpatientInfo.getTotCost();
			}
			if(inpatientInfo.getPayCost()!=null){
				payCost = inpatientInfo.getOwnCost();
			}
			if(inpatientInfo.getPubCost()!=null){
				pubCost = inpatientInfo.getPubCost();
			}
			if(inpatientInfo.getEcoCost()!=null){
				ecoCost = inpatientInfo.getEcoCost();
			}
			if(inpatientInfo.getPrepayCost()!=null){
				prepayCost = inpatientInfo.getPrepayCost();
			}
			totCost = totCost+totCost2;
			ownCost = ownCost+ownCost2;
			payCost = payCost+ownCost2;
			pubCost = pubCost+pubCost2;
			ecoCost = ecoCost+ecoCost2;
			freeCost= prepayCost-ownCost;// 全部余额(未结)-未走医保		
			inpatientInfo.setTotCost(totCost);
			inpatientInfo.setOwnCost(ownCost);
			inpatientInfo.setPayCost(payCost);
			inpatientInfo.setPubCost(pubCost);
			inpatientInfo.setEcoCost(ecoCost);
			inpatientInfo.setFreeCost(freeCost);
			inpatientInfo.setChangeTotcost(changeTotcost2);
			inpatientInfo.setUpdateUser(user.getAccount());
			inpatientInfo.setUpdateTime(DateUtils.getCurrentTime());
			exitNoFeeDAO.update(inpatientInfo);//修改汇总费用
			OperationUtils.getInstance().conserve(inpatientInfoList.get(0).getId(),"费用","UPDATE","T_INPATIENT_INFO",OperationUtils.LOGACTIONINSERT);
		}	
		String parameterValue = parameterInnerDAO.getParameterByCode("hszsfkysf");
		if("".equals(parameterValue)){
			parameterValue = "1";
		}
		String feeOpercode = null;//计费人
		Date feeDate = null;//计费时间
		if("1".equals(parameterValue)){
			feeOpercode = user.getAccount();
			feeDate = DateUtils.getCurrentTime();
		}			
		List<BusinessMedicalGroupInfo> medicalGroupInfoList = null;	//药品明细表
		InpatientFeeInfo inpatientFeeInfo =null;//汇总表
		double proPrice =-1;//价格
		double totCost1 = 0;// 费用金额
		double ownCost1 = 0;// 自费金额
		double payCost1 = 0;// 自付金额(预留--暂时等于自费金额)
		double pubCost1 = 0;// 公费金额
		double ecoCost1 = 0;// 优惠金额 	
		Map<String,String> nameMap=deptInInterService.querydeptCodeAndNameMap();
		for(int i=0;i<feeVoList.size();i++){					
			if(inpatientInfoList.get(0).getStopAcount()!=null&&inpatientInfoList.get(0).getStopAcount()==1){
				map.put("resCode", "error");
				map.put("resMsg", "患者账户关闭，不能进行收费操作！");					
				return map;
			}
			else if("O".equals(inpatientInfoList.get(0).getInState())||"N".equals(inpatientInfoList.get(0).getInState())){
				map.put("resCode", "error");
				map.put("resMsg", "患者正在出院结算或已经无费退院，不能进行收费操作！");	
				return map;
			}
			else{//费用处理				
				SysDepartment  loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室				
				List<InsuranceSiitem> insuranceSiitemList = exitNoFeeDAO.queryInsuranceSiitem(feeVoList.get(i).getItemCode());//查询自负比例
				if(inpatientInfoList.get(0).getBabyFlag()==1){//患者为婴儿
					String parameterValue1 = parameterInnerDAO.getParameterByCode("yefyisjlmqss");
					if("".equals(parameterValue1)){
						parameterValue1 = "0";
					}
					if("1".equals(parameterValue1)){
						String mInpatientNo = feeVoList.get(i).getInpatientNo().substring(0, 2)+"00"+feeVoList.get(i).getInpatientNo().substring(5);
						inpatientInfoList = exitNoFeeDAO.queryInpatientInfo(mInpatientNo);//发生的费用记在母亲身上
						if(inpatientInfoList==null||inpatientInfoList.size()==0){
							map.put("resCode", "error");
							map.put("resMsg", "婴儿患者母亲信息不存在，不能进行收费操作！");						
							return map;
						}
						else if("O".equals(inpatientInfoList.get(0).getInState())||"N".equals(inpatientInfoList.get(0).getInState())){
							map.put("resCode", "error");
							map.put("resMsg", "婴儿患者母亲非在院状态，请先进行召回操作！");						
							return map;
						}						
					}
				}
				//进行费用处理
					if(DateUtils.compareDate(DateUtils.addToDate(inpatientInfoList.get(0).getReportBirthday(),15,0,0),DateUtils.getCurrentTime())==-1 && "2".equals(feeVoList.get(i).getTy())){//患者年龄小于15岁，并且为非药品项目										
						List<DrugUndrug> drugUndrugList4 = docAdvManageDAO.queryNoDrugInfo(feeVoList.get(i).getItemCode());//查询非药品信息
						proPrice = drugUndrugList4.get(0).getChildrenprice();//价格-儿童价等
					}
					List<DrugInfo> drugInfoList3 = null;//药品信息
					List<DrugUndrug> drugUndrugList3 = null;//非药品信息
					if("1".equals(feeVoList.get(i).getTy())){//药品
						InpatientMedicineList inpatientMedicineList = new InpatientMedicineList();//药品明细	
						drugInfoList3 = exitNoFeeDAO.queryDrugInfo(feeVoList.get(i).getItemCode());//查询药品信息
						double num = 0;						
						double totCost = 0;
						String itemId =null;//药品代码
						String itemName = null;//药品名称
						double retailprice = 0;//单价
						String drugPackagingunit = "";//包装单位
						int packagingnum = 1;//包装数量
						String spec = null;//规格	
						String drugType = null;//药品类别
						String drugNature = null;//药品性质
						if(drugInfoList3!=null&&drugInfoList3.size()>0){
							if(drugInfoList3.get(0).getDrugPackagingunit()!=null){
								drugPackagingunit = drugInfoList3.get(0).getDrugPackagingunit();
							}
							if(drugInfoList3.get(0).getPackagingnum()!=null){
								packagingnum = drugInfoList3.get(0).getPackagingnum();
							}
							if(!"".equals(drugPackagingunit)&&feeVoList.get(i).getCurrentUnit().equals(drugPackagingunit)){//当前单位为包装单位
								num = feeVoList.get(i).getQty();
							}else{//当前单位为最小单位
								num = feeVoList.get(i).getQty()/packagingnum;
							}							
							if(drugInfoList3.get(0).getDrugRetailprice()!=null){
								retailprice = drugInfoList3.get(0).getDrugRetailprice();								
							}
							totCost=drugInfoList3.get(0).getDrugRetailprice()*num;// 费用金额计算
							if(drugInfoList3.get(0).getId()!=null){
								itemId = drugInfoList3.get(0).getId();
							}
							if(drugInfoList3.get(0).getName()!=null){
								itemName = drugInfoList3.get(0).getName();
							}	
							if(drugInfoList3.get(0).getSpec()!=null){
								spec = drugInfoList3.get(0).getSpec();
							}
							if(drugInfoList3.get(0).getDrugType()!=null){
								drugType = drugInfoList3.get(0).getDrugType();
							}
							if(drugInfoList3.get(0).getDrugNature()!=null){
								drugNature = drugInfoList3.get(0).getDrugNature();
							}
						}
						inpatientMedicineList.setId(null);
						inpatientMedicineList.setRecipeNo(feeVoList.get(i).getRecipeNo());//处方号
						inpatientMedicineList.setSequenceNo(feeVoList.get(i).getSequenceNo());//处方内项目流水号
						inpatientMedicineList.setTransType(feeVoList.get(i).getTransType());//交易类型,1正交易，2反交易    
						inpatientMedicineList.setInpatientNo(feeVoList.get(i).getInpatientNo());//住院流水号 
						inpatientMedicineList.setName(patientName);//患者姓名
						inpatientMedicineList.setPaykindCode(feeVoList.get(i).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干---暂时都按自费？？？？？   
						inpatientMedicineList.setPactCode(pactCode);//合同单位 
						inpatientMedicineList.setInhosDeptCode(depCode);//在院科室代码   
						InpatientInfoNow inpatientInfoSerch = new InpatientInfoNow();
						inpatientInfoSerch.setInpatientNo(feeVoList.get(i).getInpatientNo());
						List<InpatientInfoVo> inpatientInfoList1 = exitNoFeeDAO.getInpatientInfo(inpatientInfoSerch);
						if(inpatientInfoList1!=null&&inpatientInfoList1.size()>0){
							if(inpatientInfoList1.get(0).getNurseCellCode()!=null){
								inpatientMedicineList.setNurseCellCode(inpatientInfoList1.get(0).getNurseCellCode());//护士站代码  
							}							
						}							
						inpatientMedicineList.setRecipeDeptCode(null);//开立科室代码???
						inpatientMedicineList.setExecuteDeptCode(feeVoList.get(i).getExecuteDeptCode());//执行科室代码   
						inpatientMedicineList.setMedicineDeptcode(null);//取药科室代码???
						inpatientMedicineList.setRecipeDocCode(null);//开立医师代码 
						inpatientMedicineList.setDrugCode(itemId);//药品编码 
						inpatientMedicineList.setFeeCode(feeVoList.get(i).getFeeCode());//最小费用代码  
						inpatientMedicineList.setCenterCode(null);//医疗中心项目代码?????
						inpatientMedicineList.setDrug_name(itemName);//药品名称   
						inpatientMedicineList.setSpecs(spec);//规格
						inpatientMedicineList.setDrugType(drugType);//药品类别   
						inpatientMedicineList.setDrugQuality(drugNature);// 药品性质
						inpatientMedicineList.setHomeMadeFlag(null);//自制标识  
						inpatientMedicineList.setUnitPrice(retailprice);// 单价   
						inpatientMedicineList.setCurrentUnit(feeVoList.get(i).getCurrentUnit());//当前单位  
						inpatientMedicineList.setPackQty(packagingnum);//包装数 
						inpatientMedicineList.setQty(feeVoList.get(i).getQty());//数量 
						inpatientMedicineList.setDays(feeVoList.get(i).getDays());//天数							
						inpatientMedicineList.setTotCost(totCost);// 费用金额						
						double pubRatio = 0;
						if(contractunitList!=null&& contractunitList.size()>0){
							pubRatio=contractunitList.get(0).getPubRatio();//公费比例\报销比例
						}	
						double ownCost = 0;// 自费金额
						double payCost = 0;// 自付金额(预留--暂时等于自费金额)
						double pubCost = 0;// 公费金额
						double ecoCost = 0;// 优惠金额 
						if(feeVoList.get(i).getEcoCost()!=null){
							ecoCost = feeVoList.get(i).getEcoCost();
						}
						String itemGrade="";
						if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
							if(insuranceSiitemList.get(0).getItemGrade()!=null){
								itemGrade = insuranceSiitemList.get(0).getItemGrade();
							}							
						}
						double rate=0;
						if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
							if(insuranceSiitemList.get(0).getRate()!=null){
								rate = insuranceSiitemList.get(0).getRate();
							}							
						}
						if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
							pubCost = (totCost-ecoCost)*pubRatio;
							ownCost = totCost-ecoCost-pubCost;
						}
						if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
							pubCost = (totCost-ecoCost)*(1-rate)*pubRatio;
							ownCost = totCost-ecoCost-pubCost;
						}
						if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
							ownCost = totCost-ecoCost;
						}	
						inpatientMedicineList.setOwnCost(ownCost);// 自费金额
						inpatientMedicineList.setPayCost(ownCost);// 自付金额
						inpatientMedicineList.setPubCost(pubCost);// 公费金额
						inpatientMedicineList.setEcoCost(ecoCost);// 优惠金额 
						inpatientMedicineList.setUpdateSequenceno(feeVoList.get(i).getUpdateSequenceno());//更新库存的流水号  
						inpatientMedicineList.setSenddrugSequence(feeVoList.get(i).getSenddrugSequence());//发药单序列号
						inpatientMedicineList.setSenddrugFlag(feeVoList.get(i).getSenddrugFlag());// 发药状态（0 划价 2摆药 1批费）
						inpatientMedicineList.setBabyFlag(0);//是否婴儿用药 0 不是 1 是     
						inpatientMedicineList.setJzqjFlag(feeVoList.get(i).getJzqjFlag());// 急诊抢救标志 
						inpatientMedicineList.setBroughtFlag(feeVoList.get(i).getBroughtFlag());//出院带药标记 0 否 1 是(Change as OrderType)
						inpatientMedicineList.setBalanceNo(feeVoList.get(i).getBalanceNo());// 结算序号  
						inpatientMedicineList.setBalanceState(0);//结算状态
						inpatientMedicineList.setNobackNum(feeVoList.get(i).getNobackNum());//可退数量 
						inpatientMedicineList.setApprno(null);// 审批号(中山一：退费时保存退费申请单号)
						inpatientMedicineList.setCheckOpercode(user.getAccount());//划价人
						inpatientMedicineList.setChargeDate(DateUtils.getCurrentTime());// 划价日期  
						inpatientMedicineList.setFeeOpercode(feeOpercode);//计费人 
						inpatientMedicineList.setFeeDate(feeDate);// 计费时间
						inpatientMedicineList.setExecOpercode(null);//执行人代码  
						inpatientMedicineList.setExecDate(null);// 执行日期
						inpatientMedicineList.setSenddrugOpercode(null);// 发药人
						inpatientMedicineList.setSenddrugDate(null);//发药日期
						inpatientMedicineList.setCheckOpercode(null);//审核人   
						inpatientMedicineList.setCheckNo(null);//审核序号  
						inpatientMedicineList.setMoOrder(feeVoList.get(i).getMoOrder());//医嘱流水号 
						inpatientMedicineList.setMoExecSqn(feeVoList.get(i).getMoExecSqn());//医嘱执行单流水号 
						inpatientMedicineList.setFeeRate(null);//收费比率 
						inpatientMedicineList.setFeeoperDeptcode(loginDept.getDeptCode());// 收费员科室
						inpatientMedicineList.setUploadFlag(null);// 上传标志  
						inpatientMedicineList.setExtFlag2(null);//扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 
						inpatientMedicineList.setExtFlag3(null);// 聊城市医保新增(记录凭单号) 																		
						medicalGroupInfoList = exitNoFeeDAO.queryMedicalGroupInfo(feeVoList.get(i).getRecipeDocCode());//获取医疗组信息
						if(medicalGroupInfoList!=null&&medicalGroupInfoList.size()>0){
							if(medicalGroupInfoList.get(0).getBusinessMedicalgroup().getId()!=null){
								inpatientMedicineList.setMedicalteamCode(medicalGroupInfoList.get(0).getBusinessMedicalgroup().getId());//住院药品明细表-医疗组
							}							
						}						
						inpatientMedicineList.setOperationno(feeVoList.get(i).getOperationno());//手术编码   
						inpatientMedicineList.setTransactionSequenceNumber(null);//医保交易流水号
						inpatientMedicineList.setSiTransactionDatetime(null);//医保交易时间
						inpatientMedicineList.setHisRecipeNo(null);//HIS处方号  
						inpatientMedicineList.setSiRecipeNo(null);//医保处方号   
						inpatientMedicineList.setHisCancelRecipeNo(null);//HIS原处方号
						inpatientMedicineList.setSiCancelRecipeNo(null);//医保原处方号
						inpatientMedicineList.setCreateUser(user.getAccount());
						inpatientMedicineList.setCreateDept(loginDept.getDeptCode());
						inpatientMedicineList.setCreateTime(DateUtils.getCurrentTime());
						recipeNo3 = recipeNo3+feeVoList.get(i).getRecipeNo()+",";
						SequenceNo3 = SequenceNo3+feeVoList.get(i).getSequenceNo()+",";
						exitNoFeeDAO.save(inpatientMedicineList);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_MEDICINELIST",OperationUtils.LOGACTIONINSERT);
					}
					if("2".equals(feeVoList.get(i).getTy())){//非药品
						InpatientItemList inpatientItemList = new InpatientItemList();//非药品明细
						drugUndrugList3 = docAdvManageDAO.queryNoDrugInfo(feeVoList.get(i).getItemCode());//查询非药品信息
						double num = feeVoList.get(i).getQty();	
						double totCost = 0;
						String itemId =null;//非药品代码
						String itemName = null;//非药品名称
						double defaultprice = 0;//单价
						String undrugEquipmentno = null;//设备号
						if(drugUndrugList3!=null&&drugUndrugList3.size()>0){
							itemId = drugUndrugList3.get(0).getId();
							if(drugUndrugList3.get(0).getName()!=null){
								itemName = drugUndrugList3.get(0).getName();
							}
							if(drugUndrugList3.get(0).getDefaultprice()!=null){
								defaultprice = drugUndrugList3.get(0).getDefaultprice();
							}
							totCost=defaultprice*num;// 费用金额计算
							if(drugUndrugList3.get(0).getUndrugEquipmentno()!=null){
								undrugEquipmentno = drugUndrugList3.get(0).getUndrugEquipmentno();
							}
						}
						inpatientItemList.setId(null);
						inpatientItemList.setRecipeNo(feeVoList.get(i).getRecipeNo());//处方号
						inpatientItemList.setSequenceNo(feeVoList.get(i).getSequenceNo());//处方内项目流水号
						inpatientItemList.setTranstype(feeVoList.get(i).getTransType());//交易类型,1正交易，2反交易    
						inpatientItemList.setInpatientNo(feeVoList.get(i).getInpatientNo());//住院流水号 
						inpatientItemList.setName(patientName);//患者姓名
						inpatientItemList.setPaykindCode(feeVoList.get(i).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干---暂时都按自费？？？？？   
						inpatientItemList.setPactCode(pactCode);//合同单位 
						inpatientItemList.setUpdateSequenceno(feeVoList.get(i).getUpdateSequenceno());//更新库存的流水号 
						inpatientItemList.setInhosDeptcode(depCode);//在院科室代码   
						InpatientInfoNow inpatientInfoSerch = new InpatientInfoNow();
						inpatientInfoSerch.setInpatientNo(feeVoList.get(i).getInpatientNo());
						List<InpatientInfoVo> inpatientInfoList1 = exitNoFeeDAO.getInpatientInfo(inpatientInfoSerch);
						if(inpatientInfoList1!=null&&inpatientInfoList1.size()>0){
							if(inpatientInfoList1.get(0).getNurseCellCode()!=null){
								inpatientItemList.setNurseCellCode(inpatientInfoList1.get(0).getNurseCellCode());//护士站代码  
							}							
						}						
						inpatientItemList.setRecipeDeptcode(null);//开立科室代码???
						inpatientItemList.setExecuteDeptcode(feeVoList.get(i).getExecuteDeptCode());//执行科室代码   
						inpatientItemList.setStockDeptcode(null);//扣库科室代码???
						inpatientItemList.setRecipeDoccode(null);//开立医师代码 
						inpatientItemList.setItemCode(itemId);//项目代码 
						inpatientItemList.setFeeCode(feeVoList.get(i).getFeeCode());//最小费用代码  
						inpatientItemList.setCenterCode(null);//医疗中心项目代码?????
						inpatientItemList.setItemName(itemName);//项目名称  
						inpatientItemList.setUnitPrice(defaultprice);//单价   
						inpatientItemList.setQty(feeVoList.get(i).getQty());//数量 
						inpatientItemList.setCurrentUnit(feeVoList.get(i).getCurrentUnit());//当前单位  
						inpatientItemList.setPackageCode(feeVoList.get(i).getPackageCode());//组套代码
						inpatientItemList.setPackageName(feeVoList.get(i).getPackageName());//组套名称 	
						inpatientItemList.setTotCost(totCost);// 费用金额						
						double pubRatio = 0;
						if(contractunitList!=null&& contractunitList.size()>0){
							pubRatio=contractunitList.get(0).getPubRatio();//公费比例\报销比例
						}	
						double ownCost = 0;// 自费金额
						double payCost = 0;// 自付金额(预留--暂时等于自费金额)
						double pubCost = 0;// 公费金额
						double ecoCost = 0;// 优惠金额 
						if(feeVoList.get(i).getEcoCost()!=null){
							ecoCost = feeVoList.get(i).getEcoCost();
						}
						String itemGrade="";
						if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
							if(insuranceSiitemList.get(0).getItemGrade()!=null){
								itemGrade = insuranceSiitemList.get(0).getItemGrade();
							}							
						}
						double rate=0;
						if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
							if(insuranceSiitemList.get(0).getRate()!=null){
								rate = insuranceSiitemList.get(0).getRate();
							}							
						}
						if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
							pubCost = (totCost-ecoCost)*pubRatio;
							ownCost = totCost-ecoCost-pubCost;
						}
						if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
							pubCost = (totCost-ecoCost)*(1-rate)*pubRatio;
							ownCost = totCost-ecoCost-pubCost;
						}
						if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
							ownCost = totCost-ecoCost;
						}	
						inpatientItemList.setOwnCost(ownCost);// 自费金额
						inpatientItemList.setPayCost(ownCost);// 自付金额
						inpatientItemList.setPubCost(pubCost);// 公费金额
						inpatientItemList.setEcoCost(ecoCost);// 优惠金额 
						inpatientItemList.setSendmatSequence(feeVoList.get(i).getSenddrugSequence());//出库单序列号
						inpatientItemList.setSendFlag(feeVoList.get(i).getSenddrugFlag());//发放状态（0 划价 2发放（执行） 1 批费）
						inpatientItemList.setBabyFlag(0);//是否婴儿用药 0 不是 1 是     
						inpatientItemList.setJzqjFlag(feeVoList.get(i).getJzqjFlag());// 急诊抢救标志 
						inpatientItemList.setBroughtFlag(feeVoList.get(i).getBroughtFlag());//出院带药标记 0 否 1 是(Change as OrderType)
						inpatientItemList.setBalanceNo(feeVoList.get(i).getBalanceNo());// 结算序号  
						inpatientItemList.setBalanceState(0);//结算状态
						inpatientItemList.setNobackNum(feeVoList.get(i).getNobackNum());//可退数量 
						inpatientItemList.setApprno(null);// 审批号(中山一：退费时保存退费申请单号)
						inpatientItemList.setCheckOpercode(user.getAccount());//划价人
						inpatientItemList.setChargeDate(DateUtils.getCurrentTime());// 划价日期 
						inpatientItemList.setConfirmNum(feeVoList.get(i).getConfirmNum());//已确认数
						inpatientItemList.setMachineNo(undrugEquipmentno);// 设备号
						inpatientItemList.setExecOpercode(null);//执行人代码  
						inpatientItemList.setExecDate(null);// 执行日期
						inpatientItemList.setSendOpercode(null);//发放人  						
						inpatientItemList.setFeeOpercode(feeOpercode);//计费人 
						inpatientItemList.setFeeDate(feeDate);// 计费时间
						inpatientItemList.setSendDate(null);//发放日期						
						inpatientItemList.setCheckOpercode(null);//审核人   
						inpatientItemList.setCheckNo(null);//审核序号  
						inpatientItemList.setMoOrder(feeVoList.get(i).getMoOrder());//医嘱流水号 
						inpatientItemList.setMoExecSqn(feeVoList.get(i).getMoExecSqn());//医嘱执行单流水号 
						inpatientItemList.setFeeRate(null);//收费比率 
						inpatientItemList.setFeeoperDeptcode(loginDept.getDeptCode());// 收费员科室
						inpatientItemList.setUploadFlag(null);// 上传标志  
						inpatientItemList.setExtFlag2(null);//扩展标志2(收费方式0住院处直接收费,1护士站医嘱收费,2确认收费,3身份变更,4比例调整) 
						inpatientItemList.setExtFlag3(null);// 聊城市医保新增(记录凭单号) 	
						inpatientItemList.setItemFlag(feeVoList.get(i).getItemFlag());// 0非药品 2物资   
						medicalGroupInfoList = exitNoFeeDAO.queryMedicalGroupInfo(feeVoList.get(i).getRecipeDocCode());//获取医疗组信息
						if(medicalGroupInfoList!=null&&medicalGroupInfoList.size()>0){
							if(medicalGroupInfoList.get(0).getBusinessMedicalgroup().getId()!=null){
								inpatientItemList.setMedicalteamCode(medicalGroupInfoList.get(0).getBusinessMedicalgroup().getId());//住院药品明细表-医疗组
							}							
						}						
						inpatientItemList.setOperationno(feeVoList.get(i).getOperationno());//手术编码   
						inpatientItemList.setTransactionSequenceNumber(null);//医保交易流水号
						inpatientItemList.setSiTransactionDatetime(null);//医保交易时间
						inpatientItemList.setHisRecipeNo(null);//HIS处方号  
						inpatientItemList.setSiRecipeNo(null);//医保处方号   
						inpatientItemList.setHisCancelRecipeNo(null);//HIS原处方号
						inpatientItemList.setSiCancelRecipeNo(null);//医保原处方号
						inpatientItemList.setCreateUser(user.getAccount());
						inpatientItemList.setCreateDept(loginDept.getDeptCode());
						inpatientItemList.setCreateTime(DateUtils.getCurrentTime());
						recipeNo3 = recipeNo3+feeVoList.get(i).getRecipeNo()+",";
						SequenceNo3 = SequenceNo3+feeVoList.get(i).getSequenceNo()+",";
						exitNoFeeDAO.save(inpatientItemList);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_ITEMLIST",OperationUtils.LOGACTIONINSERT);
					}					
					List<InpatientFeeInfo> inpatientFeeInfoList1 = exitNoFeeDAO.queryInpatientFeeInfo(feeVoList.get(i).getRecipeNo());//查询费用汇总表
					if(inpatientFeeInfoList1!=null&&inpatientFeeInfoList1.size()>0){//汇总表中存在该处方号
						if(i>0&&!feeVoList.get(i).getRecipeNo().equals(feeVoList.get(i-1).getRecipeNo())){//汇总表中存在
							recipeNo1 = recipeNo1+feeVoList.get(i-1).getRecipeNo()+",";
							exitNoFeeDAO.update(inpatientFeeInfo);//修改汇总费用
							OperationUtils.getInstance().conserve(inpatientFeeInfoList1.get(0).getId(),"费用","UPDATE","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);
							//金额重置
							if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
								totCost1 = inpatientFeeInfoList1.get(0).getTotCost();// 费用金额
							}
							if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
								ownCost1 = inpatientFeeInfoList1.get(0).getOwnCost();// 自费金额
							}
							if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
								payCost1 = inpatientFeeInfoList1.get(0).getPayCost();// 自付金额(预留--暂时等于自费金额)
							}
							if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
								pubCost1 = inpatientFeeInfoList1.get(0).getPubCost();// 公费金额
							}
							if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
								ecoCost1 = inpatientFeeInfoList1.get(0).getEcoCost();// 优惠金额 
							}
							inpatientFeeInfo = (InpatientFeeInfo)inpatientFeeInfoList1.get(0);
							String drugPackagingunit =null;//包装单位
							double packagingnum = 1;//包装数量
							double retailprice = 0;//药品单价
							double defaultprice = 0;//非药品单价
							if(drugInfoList3.get(0).getDrugPackagingunit()!=null){
								drugPackagingunit = drugInfoList3.get(0).getDrugPackagingunit();
							}
							if(drugInfoList3.get(0).getPackagingnum()!=null){
								packagingnum = drugInfoList3.get(0).getPackagingnum();
							}
							if(drugInfoList3.get(0).getDrugRetailprice()!=null){
								retailprice = drugInfoList3.get(0).getDrugRetailprice();								
							}
							if(drugUndrugList3.get(0).getDefaultprice()!=null){
								defaultprice = drugUndrugList3.get(0).getDefaultprice();								
							}
							double num2 = 0;
							if("1".equals(feeVoList.get(i).getTy())){
								if(!"".equals(drugPackagingunit)&&feeVoList.get(i).getCurrentUnit().equals(drugPackagingunit)){//当前单位为包装单位
									num2 = feeVoList.get(i).getQty();
								}else{//当前单位为最小单位
									num2 = feeVoList.get(i).getQty()/packagingnum;
								}
								totCost1 = totCost1+retailprice*num2;// 费用金额计算
							}
							if("2".equals(feeVoList.get(i).getTy())){
								num2 = feeVoList.get(i).getQty();
								totCost1 = totCost1+defaultprice*num2;// 费用金额计算
							}							
							inpatientFeeInfo.setTotCost(totCost1);// 费用金额						
							double pubRatio2 = 0;
							if(contractunitList!=null&& contractunitList.size()>0){
								pubRatio2=contractunitList.get(0).getPubRatio();//公费比例\报销比例
							}	
							double ecoCost = 0;
							if(feeVoList.get(i).getEcoCost()!=null){
								ecoCost = feeVoList.get(i).getEcoCost();
							}
							String itemGrade="";
							if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
								if(insuranceSiitemList.get(0).getItemGrade()!=null){
									itemGrade = insuranceSiitemList.get(0).getItemGrade();
								}							
							}
							double rate=0;
							if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
								if(insuranceSiitemList.get(0).getRate()!=null){
									rate = insuranceSiitemList.get(0).getRate();
								}							
							}
							if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
								pubCost1 = (totCost1-ecoCost)*pubRatio2;
								ownCost1 = totCost1-ecoCost-pubCost1;
							}
							if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
								pubCost1 = (totCost1-ecoCost)*(1-rate)*pubRatio2;
								ownCost1 = totCost1-ecoCost-pubCost1;
							}
							if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
								ownCost1 = totCost1-ecoCost;
							}	
							ecoCost1 = ecoCost1+ecoCost;// 优惠金额 
							inpatientFeeInfo.setOwnCost(ownCost1);// 自费金额
							inpatientFeeInfo.setPayCost(ownCost1);// 自付金额
							inpatientFeeInfo.setPubCost(pubCost1);// 公费金额
							inpatientFeeInfo.setEcoCost(ecoCost1);// 优惠金额 	
							inpatientFeeInfo.setUpdateUser(user.getAccount());
							inpatientFeeInfo.setUpdateTime(DateUtils.getCurrentTime());
						}
						else{
							inpatientFeeInfo = (InpatientFeeInfo)inpatientFeeInfoList1.get(0);
							if(i==0){
								if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
									totCost1 = inpatientFeeInfoList1.get(0).getTotCost();// 费用金额
								}
								if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
									ownCost1 = inpatientFeeInfoList1.get(0).getOwnCost();// 自费金额
								}
								if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
									payCost1 = inpatientFeeInfoList1.get(0).getPayCost();// 自付金额(预留--暂时等于自费金额)
								}
								if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
									pubCost1 = inpatientFeeInfoList1.get(0).getPubCost();// 公费金额
								}
								if(inpatientFeeInfoList1.get(0).getTotCost()!=null){
									ecoCost1 = inpatientFeeInfoList1.get(0).getEcoCost();// 优惠金额 
								}																																						
							}else{
								String drugPackagingunit =null;//包装单位
								double packagingnum = 1;//包装数量
								double retailprice = 0;//药品单价
								double defaultprice = 0;//非药品单价																
								double num2 = 0;
								if("1".equals(feeVoList.get(i).getTy())){
									if(drugInfoList3.get(0).getDrugPackagingunit()!=null){
										drugPackagingunit = drugInfoList3.get(0).getDrugPackagingunit();
									}
									if(drugInfoList3.get(0).getPackagingnum()!=null){
										packagingnum = drugInfoList3.get(0).getPackagingnum();
									}
									if(drugInfoList3.get(0).getDrugRetailprice()!=null){
										retailprice = drugInfoList3.get(0).getDrugRetailprice();								
									}
									if(!"".equals(drugPackagingunit)&&feeVoList.get(i).getCurrentUnit().equals(drugPackagingunit)){//当前单位为包装单位
										num2 = feeVoList.get(i).getQty();
									}else{//当前单位为最小单位
										num2 = feeVoList.get(i).getQty()/packagingnum;
									}
									totCost1 = totCost1+retailprice*num2;// 费用金额计算
								}								
								if("2".equals(feeVoList.get(i).getTy())){
									if(drugUndrugList3.get(0).getDefaultprice()!=null){
										defaultprice = drugUndrugList3.get(0).getDefaultprice();								
									}
									num2 = feeVoList.get(i).getQty();
									totCost1 = totCost1+defaultprice*num2;// 费用金额计算
								}																			
								double pubRatio2 = 0;
								if(contractunitList!=null&& contractunitList.size()>0){
									pubRatio2=contractunitList.get(0).getPubRatio();//公费比例\报销比例
								}	
								double ecoCost = 0;
								if(feeVoList.get(i).getEcoCost()!=null){
									ecoCost = feeVoList.get(i).getEcoCost();
								}
								String itemGrade="";
								if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
									if(insuranceSiitemList.get(0).getItemGrade()!=null){
										itemGrade = insuranceSiitemList.get(0).getItemGrade();
									}							
								}
								double rate=0;
								if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
									if(insuranceSiitemList.get(0).getRate()!=null){
										rate = insuranceSiitemList.get(0).getRate();
									}							
								}
								if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
									pubCost1 = (totCost1-ecoCost)*pubRatio2;
									ownCost1 = totCost1-ecoCost-pubCost1;
								}
								if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
									pubCost1 = (totCost1-ecoCost)*(1-rate)*pubRatio2;
									ownCost1 = totCost1-ecoCost-pubCost1;
								}
								if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
									ownCost1 = totCost1-ecoCost;
								}	
								ecoCost1 = ecoCost1+ecoCost;// 优惠金额 
							}	
							inpatientFeeInfo.setTotCost(totCost1);// 费用金额	
							inpatientFeeInfo.setOwnCost(ownCost1);// 自费金额
							inpatientFeeInfo.setPayCost(ownCost1);// 自付金额
							inpatientFeeInfo.setPubCost(pubCost1);// 公费金额
							inpatientFeeInfo.setEcoCost(ecoCost1);// 优惠金额 	
							inpatientFeeInfo.setUpdateUser(user.getAccount());
							inpatientFeeInfo.setUpdateTime(DateUtils.getCurrentTime());
							if(i==(feeVoList.size()-1)){
								recipeNo1 = recipeNo1+feeVoList.get(i).getRecipeNo();
								exitNoFeeDAO.update(inpatientFeeInfo);//修改汇总费用
								OperationUtils.getInstance().conserve(inpatientFeeInfoList1.get(0).getId(),"费用","UPDATE","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);
							}							
						}
					}else{//汇总表中不存在该处方号						
						if(i>0){	
							if(!feeVoList.get(i).getRecipeNo().equals(feeVoList.get(i-1).getRecipeNo())){
								recipeNo2 = recipeNo2+feeVoList.get(i-1).getRecipeNo()+",";
								exitNoFeeDAO.save(inpatientFeeInfo);//保存新增汇总费用
								OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);
								//金额重置
								totCost1 = 0;// 费用金额
								ownCost1 = 0;// 自费金额
								payCost1 = 0;// 自付金额(预留--暂时等于自费金额)
								pubCost1 = 0;// 公费金额
								ecoCost1 = 0;// 优惠金额 
							}							
						}else{
							inpatientFeeInfo = new InpatientFeeInfo();//汇总	
							inpatientFeeInfo.setId(null);
							inpatientFeeInfo.setRecipeNo(feeVoList.get(i).getRecipeNo());//处方号
							inpatientFeeInfo.setFeeCode(feeVoList.get(i).getFeeCode());//最小费用代码  
							inpatientFeeInfo.setTransType(feeVoList.get(i).getTransType());//交易类型,1正交易，2反交易    
							inpatientFeeInfo.setInpatientNo(feeVoList.get(i).getInpatientNo());//住院流水号 
							inpatientFeeInfo.setName(patientName);//患者姓名
							inpatientFeeInfo.setPaykindCode(feeVoList.get(i).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干---暂时都按自费？？？？？   
							inpatientFeeInfo.setPactCode(pactCode);//合同单位 
							inpatientFeeInfo.setNurseCellCode(feeVoList.get(i).getNurseCellCode());//护士站代码 
							inpatientFeeInfo.setInhosDeptcode(depCode);//在院科室代码   
							InpatientInfoNow inpatientInfoSerch = new InpatientInfoNow();
							inpatientInfoSerch.setInpatientNo(feeVoList.get(i).getInpatientNo());
							List<InpatientInfoVo> inpatientInfoList1 = exitNoFeeDAO.getInpatientInfo(inpatientInfoSerch);
							if(inpatientInfoList1!=null &&inpatientInfoList1.size()>0){
								if(inpatientInfoList1.get(0).getNurseCellCode()!=null){
									inpatientFeeInfo.setNurseCellCode(inpatientInfoList1.get(0).getNurseCellCode());//护士站代码  
								}								
							}	
							if(feeVoList.get(i).getRecipeDeptCode()!=null){
								inpatientFeeInfo.setRecipeDeptcode(feeVoList.get(i).getRecipeDeptCode());//开立科室代码
							}
							inpatientFeeInfo.setExecuteDeptcode(feeVoList.get(i).getExecuteDeptCode());//执行科室代码  
							String stockDeptcode=feeVoList.get(i).getMedicineDeptcode();
							if(stockDeptcode!=null){
								inpatientFeeInfo.setStockDeptcode(stockDeptcode);//扣库科室代码
								inpatientFeeInfo.setStockDeptname(nameMap.get(stockDeptcode));
							}
							if(feeVoList.get(i).getRecipeDocCode()!=null){
								inpatientFeeInfo.setRecipeDoccode(feeVoList.get(i).getRecipeDocCode());//开立医师代码
							}							 	
							String drugPackagingunit =null;//包装单位
							double packagingnum = 1;//包装数量
							double retailprice = 0;//药品单价
							double defaultprice = 0;//非药品单价														
							double num1 = 0;
							if("1".equals(feeVoList.get(i).getTy())){
								if(drugInfoList3.get(0).getDrugPackagingunit()!=null){
									drugPackagingunit = drugInfoList3.get(0).getDrugPackagingunit();
								}
								if(drugInfoList3.get(0).getPackagingnum()!=null){
									packagingnum = drugInfoList3.get(0).getPackagingnum();
								}
								if(drugInfoList3.get(0).getDrugRetailprice()!=null){
									retailprice = drugInfoList3.get(0).getDrugRetailprice();								
								}
								if(!"".equals(drugPackagingunit)&&feeVoList.get(i).getCurrentUnit().equals(drugPackagingunit)){//当前单位为包装单位
									num1 = feeVoList.get(i).getQty();
								}else{//当前单位为最小单位
									num1 = feeVoList.get(i).getQty()/packagingnum;
								}
								totCost1 = totCost1+retailprice*num1;// 费用金额计算
							}								
							if("2".equals(feeVoList.get(i).getTy())){
								if(drugUndrugList3.get(0).getDefaultprice()!=null){
									defaultprice = drugUndrugList3.get(0).getDefaultprice();								
								}
								num1 = feeVoList.get(i).getQty();
								totCost1 = totCost1+defaultprice*num1;// 费用金额计算
							}							
							inpatientFeeInfo.setTotCost(totCost1);// 费用金额						
							double pubRatio1 = 0;
							if(contractunitList!=null&& contractunitList.size()>0){
								pubRatio1=contractunitList.get(0).getPubRatio();//公费比例\报销比例
							}	
							double ecoCost = 0;
							if(feeVoList.get(i).getEcoCost()!=null){
								ecoCost = feeVoList.get(i).getEcoCost();
							}
							String itemGrade="";
							if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
								if(insuranceSiitemList.get(0).getItemGrade()!=null){
									itemGrade = insuranceSiitemList.get(0).getItemGrade();
								}							
							}
							double rate=0;
							if(insuranceSiitemList!=null&&insuranceSiitemList.size()>0){
								if(insuranceSiitemList.get(0).getRate()!=null){
									rate = insuranceSiitemList.get(0).getRate();
								}								
							}
							if("1".equals(itemGrade)){//医保目录等级 1 甲类(统筹全部支付)							
								pubCost1 = (totCost1-ecoCost)*pubRatio1;
								ownCost1 = totCost1-ecoCost-pubCost1;
							}
							if("2".equals(itemGrade)){//2 乙类(准予部分支付)							
								pubCost1 = (totCost1-ecoCost)*(1-rate)*pubRatio1;
								ownCost1 = totCost1-ecoCost-pubCost1;
							}
							if("3".equals(itemGrade)||"".equals(itemGrade)){//3 自费
								ownCost1 = totCost1-ecoCost;
							}	
							ecoCost1 = ecoCost1+ecoCost;// 优惠金额 
							inpatientFeeInfo.setOwnCost(ownCost1);// 自费金额
							inpatientFeeInfo.setPayCost(ownCost1);// 自付金额
							inpatientFeeInfo.setPubCost(pubCost1);// 公费金额
							inpatientFeeInfo.setEcoCost(ecoCost1);// 优惠金额 									
							inpatientFeeInfo.setChargeOpercode(user.getAccount());//划价人
							inpatientFeeInfo.setChargeDate(DateUtils.getCurrentTime());// 划价日期 						
							inpatientFeeInfo.setFeeOpercode(feeOpercode);//计费人 
							inpatientFeeInfo.setFeeDate(feeDate);// 计费时间
							inpatientFeeInfo.setBalanceNo(feeVoList.get(i).getBalanceNo());//结算序号    
							inpatientFeeInfo.setBalanceState(0);//结算标志 0:未结算；1:已结算 2:已结转   	
							if(!"".equals(feeVoList.get(i).getCheckNo())&&feeVoList.get(i).getCheckNo()!=null){
								inpatientFeeInfo.setCheckNo(feeVoList.get(i).getCheckNo());//审核序号  
							}
							if(!"".equals(feeVoList.get(i).getBabyFlag())&&feeVoList.get(i).getBabyFlag()!=null){
								inpatientFeeInfo.setBabyFlag(feeVoList.get(i).getBabyFlag());//婴儿标记1：是，0：否(初始值为0)    
							}							
							inpatientFeeInfo.setFeeoperDeptcode(loginDept.getId());//收费员科室 
							inpatientFeeInfo.setCreateUser(user.getAccount());
							inpatientFeeInfo.setCreateDept(loginDept.getDeptCode());
							inpatientFeeInfo.setCreateTime(DateUtils.getCurrentTime());
							if(i==(feeVoList.size()-1)){
								recipeNo2 = recipeNo2+feeVoList.get(i).getRecipeNo();
								exitNoFeeDAO.save(inpatientFeeInfo);//保存新增汇总费用
								OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);
							}
						}
					}									
				}				
			}	
			nameMap=null;
			map.put("recipeNo1", recipeNo1);
			map.put("recipeNo2", recipeNo2);
			map.put("recipeNo3", recipeNo3);
			map.put("SequenceNo3", SequenceNo3);
			map.put("resCode", "success");
			map.put("resMsg", "费用保存成功！");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "费用保存失败！");
			e.printStackTrace();
		}	
		return map;
	}
	/**
	 * 反交易收费	
	* @Title: reverseTran
	* @Description: 
	* @param feeVoList   
	* @return 
	* @date 2016年5月16日下午5:48:02
	 */
	public Map<String,Object> reverseTran(List<FeeVo> feeVoList) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();//返回信息
		try {
			for(int j = 0;j<feeVoList.size();j++){
				for(int k = j+1;k<feeVoList.size();k++){
					if(feeVoList.get(j).getRecipeNo().equals(feeVoList.get(k).getRecipeNo())){
						if(k!=j+1){
							FeeVo feeVo = new FeeVo();
							feeVo = feeVoList.get(k);
							feeVoList.remove(k);
							feeVoList.add(j+1, feeVo);
						}				
					}
				}
			}
			String recipeNo = null;
			int m = 1;
			double  totCost1 = 0 ;// 费用金额                 
			double  ownCost1 = 0;//  自费金额                 
			double  payCost1 = 0;// 自付金额                
			double  pubCost1 = 0;//公费金额                 
			double  ecoCost1 = 0;// 优惠金额      	
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();//获取登录人
			SysDepartment  loginDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			for(int i = 0;i<feeVoList.size();i++){			
				double  totCost = 0 ;// 费用金额                 
				double  ownCost = 0;//  自费金额                 
				double  payCost = 0;// 自付金额                
				double  pubCost = 0;//公费金额                 
				double  ecoCost = 0;// 优惠金额      						
				if("1".equals(feeVoList.get(i).getTy())){	
					if(i==0){
						recipeNo = "Y"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
						m = 1;
					}
					if(i>0){
						if(!feeVoList.get(i).getRecipeNo().equals(feeVoList.get(i-1).getRecipeNo())){
							recipeNo = "Y"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
							m++;
						}else{
							m = 1;
						}					
					}
					List<InpatientMedicineList> inpatientMedicineLists = exitNoFeeDAO.queryInpatientMedicineList(feeVoList.get(i).getRecipeNo(), feeVoList.get(i).getSequenceNo());
					if(inpatientMedicineLists!=null&&inpatientMedicineLists.size()>0){
						InpatientMedicineList inpatientMedicine  = inpatientMedicineLists.get(0);
						inpatientMedicine.setId(null);
						inpatientMedicine.setRecipeNo(recipeNo);
						inpatientMedicine.setSequenceNo(m);
						if(inpatientMedicine.getTotCost()!=null){
							totCost = inpatientMedicine.getTotCost();
						}
						if(inpatientMedicine.getOwnCost()!=null){
							ownCost = inpatientMedicine.getOwnCost();
						}
						if(inpatientMedicine.getPayCost()!=null){
							payCost = inpatientMedicine.getPayCost();
						}
						if(inpatientMedicine.getPubCost()!=null){
							pubCost = inpatientMedicine.getPubCost();
						}
						if(inpatientMedicine.getEcoCost()!=null){
							ecoCost = inpatientMedicine.getEcoCost();
						}
						inpatientMedicine.setTotCost(totCost*(-1));
						inpatientMedicine.setOwnCost(ownCost*(-1));
						inpatientMedicine.setPayCost(payCost*(-1));
						inpatientMedicine.setPubCost(pubCost*(-1));
						inpatientMedicine.setEcoCost(ecoCost*(-1));
						inpatientMedicine.setCreateUser(user.getAccount());
						inpatientMedicine.setCreateDept(loginDept.getDeptCode());
						inpatientMedicine.setCreateTime(DateUtils.getCurrentTime());
						exitNoFeeDAO.save(inpatientMedicine);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_MEDICINELIST",OperationUtils.LOGACTIONINSERT);					
					}
				}
				if("2".equals(feeVoList.get(i).getTy())){	
					if(i==0){
						recipeNo = "F"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
						m = 1;
					}
					if(i>0){
						if(!feeVoList.get(i).getRecipeNo().equals(feeVoList.get(i-1).getRecipeNo())){
							recipeNo = "F"+"00000"+exitNoFeeDAO.getSequece("SEQ_ZY_FEE_DRUGRECIPE");//生成处方号
							m++;
						}else{
							m = 1;
						}					
					}
					List<InpatientItemList> InpatientItemLists = exitNoFeeDAO.queryInpatientItemList(feeVoList.get(i).getRecipeNo(), feeVoList.get(i).getSequenceNo());
					if(InpatientItemLists!=null&&InpatientItemLists.size()>0){
						InpatientItemList inpatientItem  = InpatientItemLists.get(0);
						inpatientItem.setId(null);
						inpatientItem.setRecipeNo(recipeNo);
						inpatientItem.setSequenceNo(m);
						if(inpatientItem.getTotCost()!=null){
							totCost = inpatientItem.getTotCost();
						}
						if(inpatientItem.getOwnCost()!=null){
							ownCost = inpatientItem.getOwnCost();
						}
						if(inpatientItem.getPayCost()!=null){
							payCost = inpatientItem.getPayCost();
						}
						if(inpatientItem.getPubCost()!=null){
							pubCost = inpatientItem.getPubCost();
						}
						if(inpatientItem.getEcoCost()!=null){
							ecoCost = inpatientItem.getEcoCost();
						}
						inpatientItem.setTotCost(totCost*(-1));
						inpatientItem.setOwnCost(ownCost*(-1));
						inpatientItem.setPayCost(payCost*(-1));
						inpatientItem.setPubCost(pubCost*(-1));
						inpatientItem.setEcoCost(ecoCost*(-1));
						inpatientItem.setCreateUser(user.getAccount());
						inpatientItem.setCreateDept(loginDept.getDeptCode());
						inpatientItem.setCreateTime(DateUtils.getCurrentTime());
						exitNoFeeDAO.save(inpatientItem);
						OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_ITEMLIST",OperationUtils.LOGACTIONINSERT);					
					}
				}
				if(i>0){
					if(!feeVoList.get(i).getRecipeNo().equals(feeVoList.get(i-1).getRecipeNo())){
						List<InpatientFeeInfo> inpatientFeeInfoList = exitNoFeeDAO.queryInpatientFeeInfo(feeVoList.get(i).getRecipeNo());
						if(inpatientFeeInfoList!=null&&inpatientFeeInfoList.size()>0){
							InpatientFeeInfo inpatientFeeInfo  = inpatientFeeInfoList.get(0);
							inpatientFeeInfo.setId(null);
							inpatientFeeInfo.setRecipeNo(recipeNo);
							if(inpatientFeeInfo.getTotCost()!=null){
								totCost = inpatientFeeInfo.getTotCost();
								totCost1 = totCost1+totCost;
							}
							if(inpatientFeeInfo.getOwnCost()!=null){
								ownCost = inpatientFeeInfo.getOwnCost();
								ownCost1 = ownCost1+ownCost;
							}
							if(inpatientFeeInfo.getPayCost()!=null){
								totCost = inpatientFeeInfo.getPayCost();
								payCost1 = payCost1+payCost;
							}
							if(inpatientFeeInfo.getPubCost()!=null){
								totCost = inpatientFeeInfo.getPubCost();
								pubCost1 = pubCost1+pubCost;
							}
							if(inpatientFeeInfo.getEcoCost()!=null){
								totCost = inpatientFeeInfo.getEcoCost();
								ecoCost1 = ecoCost1+ecoCost;
							}
							inpatientFeeInfo.setTotCost(totCost*(-1));
							inpatientFeeInfo.setOwnCost(ownCost*(-1));
							inpatientFeeInfo.setPayCost(payCost*(-1));
							inpatientFeeInfo.setPubCost(pubCost*(-1));
							inpatientFeeInfo.setEcoCost(ecoCost*(-1));
							inpatientFeeInfo.setCreateUser(user.getAccount());
							inpatientFeeInfo.setCreateDept(loginDept.getDeptCode());
							inpatientFeeInfo.setCreateTime(DateUtils.getCurrentTime());
							exitNoFeeDAO.save(inpatientFeeInfo);
							OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);					
						}
					}
				}
				if(i==0){				
						List<InpatientFeeInfo> inpatientFeeInfoList = exitNoFeeDAO.queryInpatientFeeInfo(feeVoList.get(i).getRecipeNo());
						if(inpatientFeeInfoList!=null&&inpatientFeeInfoList.size()>0){
							InpatientFeeInfo inpatientFeeInfo  = inpatientFeeInfoList.get(0);
							inpatientFeeInfo.setId(null);
							inpatientFeeInfo.setRecipeNo(recipeNo);
							if(inpatientFeeInfo.getTotCost()!=null){
								totCost = inpatientFeeInfo.getTotCost();
								totCost1 = totCost1+totCost;
							}
							if(inpatientFeeInfo.getOwnCost()!=null){
								ownCost = inpatientFeeInfo.getOwnCost();
								ownCost1 = ownCost1+ownCost;
							}
							if(inpatientFeeInfo.getPayCost()!=null){
								totCost = inpatientFeeInfo.getPayCost();
								payCost1 = payCost1+payCost;
							}
							if(inpatientFeeInfo.getPubCost()!=null){
								totCost = inpatientFeeInfo.getPubCost();
								pubCost1 = pubCost1+pubCost;
							}
							if(inpatientFeeInfo.getEcoCost()!=null){
								totCost = inpatientFeeInfo.getEcoCost();
								ecoCost1 = ecoCost1+ecoCost;
							}
							inpatientFeeInfo.setTotCost(totCost*(-1));
							inpatientFeeInfo.setOwnCost(ownCost*(-1));
							inpatientFeeInfo.setPayCost(payCost*(-1));
							inpatientFeeInfo.setPubCost(pubCost*(-1));
							inpatientFeeInfo.setEcoCost(ecoCost*(-1));
							inpatientFeeInfo.setCreateUser(user.getAccount());
							inpatientFeeInfo.setCreateDept(loginDept.getDeptCode());
							inpatientFeeInfo.setCreateTime(DateUtils.getCurrentTime());
							exitNoFeeDAO.save(inpatientFeeInfo);
							OperationUtils.getInstance().conserve(null,"费用","INSERT INTO","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);					
						}				
				}
			}
			List<InpatientInfoNow> inpatientInfoList = exitNoFeeDAO.queryInpatientInfo(feeVoList.get(0).getInpatientNo());	//患者信息-住院主表
			if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
				InpatientInfoNow inpatientInfo = inpatientInfoList.get(0);
				if(inpatientInfo.getTotCost()!=null){
					inpatientInfo.setTotCost(inpatientInfo.getTotCost()+totCost1);
				}else{
					inpatientInfo.setTotCost(totCost1);
				}
				if(inpatientInfo.getOwnCost()!=null){
					inpatientInfo.setOwnCost(inpatientInfo.getOwnCost()+ownCost1);
				}else{
					inpatientInfo.setOwnCost(ownCost1);
				}
				if(inpatientInfo.getPayCost()!=null){
					inpatientInfo.setPayCost(inpatientInfo.getPayCost()+payCost1);
				}else{
					inpatientInfo.setPayCost(payCost1);
				}
				if(inpatientInfo.getPubCost()!=null){
					inpatientInfo.setPubCost(inpatientInfo.getPubCost()+pubCost1);
				}else{
					inpatientInfo.setPubCost(pubCost1);
				}
				if(inpatientInfo.getEcoCost()!=null){
					inpatientInfo.setEcoCost(inpatientInfo.getEcoCost()+ecoCost1);
				}else{
					inpatientInfo.setEcoCost(ecoCost1);
				}
				inpatientInfo.setUpdateUser(user.getAccount());
				inpatientInfo.setUpdateTime(DateUtils.getCurrentTime());
				exitNoFeeDAO.update(inpatientInfo);
				OperationUtils.getInstance().conserve(inpatientInfo.getId(),"费用","UPDATE","T_INPATIENT_INFO",OperationUtils.LOGACTIONUPDATE);
			}
			map.put("resCode", "success");
			map.put("resMsg", "费用保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "费用保存失败！");
			e.printStackTrace();
		}
		return map;
	}
	

	/**
	 * @Description:查询登录密码-校验登录密码
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-3-4
	 * @param user：用户信息实体类   
	 * @return void  
	 * @version 1.0
	**/
	@Override
	public List<User> confirmPassword(User user) throws Exception {
		List<User> userList = exitNoFeeDAO.confirmPassword(user);
		return userList;
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String inpatientNo) throws Exception {
		return exitNoFeeDAO.queryInpatientInfo(inpatientNo);
	}

	@Override
	public String queryInpatientNoByMid(String medicalrecordId) throws Exception {
		return exitNoFeeDAO.queryInpatientNoByMid(medicalrecordId);
	}
	
}
