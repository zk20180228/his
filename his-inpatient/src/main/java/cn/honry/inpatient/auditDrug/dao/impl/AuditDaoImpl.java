package cn.honry.inpatient.auditDrug.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.auditDrug.dao.AuditDao;

/**
 * 
 * @ClassName: AuditDaoImpl 
 * @Description: 
 * @author wfj
 * @date 2016年4月11日 下午8:44:06 
 *
 */
@Repository("auditDao")
@SuppressWarnings({"all"})
public class AuditDaoImpl extends  HibernateEntityDao<DrugApplyoutNow> implements AuditDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}


	@Override
	public List<InpatientKind> queryKindFunction() {
		String hql = "from InpatientKind where del_flg=0 and stop_flg=0 ";
		List<InpatientKind> kindList = super.find(hql, null);
		if(kindList==null||kindList.size()<=0){
			return new ArrayList<InpatientKind>();
		}
		return kindList;
	}
	
	@Override
	public List<OutpatientDrugcontrol> queryDrugHouse(String id) {
		String hql = "from OutpatientDrugcontrol where deptCode =? and del_flg=0 and stop_flg=0 and controlName <> '退药台'";
		List<OutpatientDrugcontrol> drugcontrolList = super.find(hql, id);
		if(drugcontrolList==null||drugcontrolList.size()<=0){
			return new ArrayList<OutpatientDrugcontrol>();
		}
		return drugcontrolList;
	}

	@Override
	public OutpatientDrugcontrol queryDrugcontril(String id) {
		String hql = "from OutpatientDrugcontrol where deptCode  = ? ";
		List<OutpatientDrugcontrol> drugcontrolList = super.find(hql, id);
		if(drugcontrolList==null||drugcontrolList.size()<=0){
			return new OutpatientDrugcontrol();
		}
		return drugcontrolList.get(0);
	}
	
	@Override
	public List<DrugBillclass> findDillClassById(String controlId) {
		String hql = "from DrugBillclass where controlId =? and del_flg=0 and stop_flg=0";
		List<DrugBillclass> billclassList = super.find(hql, controlId);
		if(billclassList==null||billclassList.size()<=0){
			return new ArrayList<DrugBillclass>();
		}
		return billclassList;
	}

	@Override
	public List<DrugApplyoutNow> findApplyoutByDept(String deptId, List classIds,Integer sendType, Integer opType,String[] applyStates, String medicalrecordId, String[] sendFlags) {
		String sql = "";
		if(opType==5){
			sql = "select d.dept_code as deptCode,s.dept_name as deptName from T_INPATIENT_STO_MSG_NOW m left join T_DRUG_APPLYOUT_NOW d "
					+ "on m.med_dept_code = d.drug_dept_code and m.billclass_code = d.billclass_code left join t_department s "
					+ "on s.dept_code = d.dept_code  "
					+ "where m.med_dept_code = :deptId and m.send_flag in (:sendFlags) "
					+ "and m.del_flg = 0 and d.del_flg = 0 and d.op_type = :opType "
					+ "and d.apply_state in (:applyStates) and d.billclass_code in (:classIds)";
		}else{
			sql = "select d.dept_code as deptCode,s.dept_name as deptName from T_INPATIENT_STO_MSG_NOW m "
					+ "left join T_DRUG_APPLYOUT_NOW d on m.med_dept_code = d.drug_dept_code and m.billclass_code = d.billclass_code "
					+ "left join t_department s on s.dept_code = d.dept_code  "
					+ "where m.med_dept_code = :deptId and m.send_flag in (:sendFlags) "
					+ "and m.del_flg = 0 and d.del_flg = 0 and d.op_type = :opType "
					+ "and d.apply_state in (:applyStates) and d.billclass_code in (:classIds)";
		}
		if(sendType==3){
			sql = sql + "  and m.send_type in ('1','2','3')";
		}else{
			sql = sql + "  and m.send_type = :sendType";
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			sql = sql + " and d.patient_id = :medicalrecordId";
		}
		sql = sql + " group by d.dept_code,s.dept_name";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("deptName");
		queryObject.setParameter("deptId", deptId).setParameterList("sendFlags", sendFlags).setParameter("opType", opType)
		.setParameterList("applyStates", applyStates).setParameterList("classIds", classIds);
		if(sendType!=3){
			queryObject.setParameter("sendType", sendType);
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			queryObject.setParameter("medicalrecordId", medicalrecordId);
		}
		List<DrugApplyoutNow> applyoutList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugApplyoutNow.class)).list();
		if(applyoutList!=null&&applyoutList.size()>0){
			  return applyoutList;
		}
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public List<DrugApplyoutNow> findApplyoutByClass(String deptId, String deptCode,Integer sendType, Integer opType,String[] applyStates, String medicalrecordId, String[] sendFlags) {
		String sql = "";
		if(opType==5){
			sql = "select d.billclass_code as billclassCode,m.billclass_name as billclassName from T_INPATIENT_STO_MSG_NOW m left join T_DRUG_APPLYOUT_NOW d on m.med_dept_code = d.drug_dept_code and m.billclass_code = d.billclass_code left join t_department s on s.dept_code = d.dept_code  where m.med_dept_code = :deptId and m.send_flag in (:sendFlags) and m.del_flg = 0 and d.del_flg = 0 and d.op_type = :opType and d.apply_state in (:applyStates) and d.dept_code = :deptCode ";
		}else{
			sql = "select d.billclass_code as billclassCode,m.billclass_name as billclassName from T_INPATIENT_STO_MSG_NOW m left join T_DRUG_APPLYOUT_NOW d on m.med_dept_code = d.drug_dept_code and m.billclass_code = d.billclass_code left join t_department s on s.dept_code = d.dept_code  where m.med_dept_code = :deptId and m.send_flag in (:sendFlags) and m.del_flg = 0 and d.del_flg = 0 and d.op_type = :opType and d.apply_state in (:applyStates) and d.dept_code = :deptCode ";
		}
		if(sendType==3){
			sql = sql + "  and m.send_type in ('1','2','3')";
		}else{
			sql = sql + "  and m.send_type = :sendType";
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			sql = sql + " and d.patient_id = :medicalrecordId";
		}   
		sql = sql + " group by d.billclass_code,m.billclass_name ";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("billclassCode").addScalar("billclassName");
		queryObject.setParameter("deptId", deptId).setParameterList("sendFlags", sendFlags).setParameter("opType", opType)
		.setParameterList("applyStates", applyStates).setParameter("deptCode", deptCode);
		if(sendType!=3){
			queryObject.setParameter("sendType", sendType);
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			queryObject.setParameter("medicalrecordId", medicalrecordId);
		}
		
		List<DrugApplyoutNow> applyoutList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugApplyoutNow.class)).list();
		if(applyoutList!=null&&applyoutList.size()>0){
			  return applyoutList;
		  }
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public List<DrugApplyoutNow> findApplyoutBybill(String deptId, String billclassCode,String deptCode, Integer sendType, Integer opType,String[] applyStates, String medicalrecordId, String[] sendFlags) {
		String sql = "";
		if(opType==5){
			sql = "select d.druged_bill as drugedBill from T_INPATIENT_STO_MSG_NOW m left join T_DRUG_APPLYOUT_NOW d on m.med_dept_code = d.drug_dept_code and m.billclass_code = d.billclass_code left join t_department s on s.dept_id = d.dept_code  where m.med_dept_code = :deptId and m.send_flag in (:sendFlags) and m.billclass_code = :billclassCode and d.del_flg = 0 and d.op_type = :opType and d.apply_state in (:applyStates) and d.dept_code = :deptCode ";
		}else{
			sql = "select d.druged_bill as drugedBill from T_INPATIENT_STO_MSG_NOW m left join T_DRUG_APPLYOUT_NOW d on m.med_dept_code = d.drug_dept_code and m.billclass_code = d.billclass_code left join t_department s on s.dept_id = d.dept_code  where m.med_dept_code = :deptId and m.send_flag in (:sendFlags) and m.billclass_code =:billclassCode and d.del_flg = 0 and d.op_type = :opType and d.apply_state in (:applyStates) and d.dept_code = :deptCode ";
		}
		if(sendType==3){
			sql = sql + "  and m.send_type in ('1','2','3')";
		}else{
			sql = sql + "  and m.send_type = :sendType";
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			sql = sql + " and d.patient_id = :medicalrecordId";
		} 
		sql = sql + " group by d.druged_bill ";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("drugedBill");
		queryObject.setParameter("deptId", deptId).setParameterList("sendFlags", sendFlags).setParameter("billclassCode", billclassCode).setParameter("opType", opType)
		.setParameterList("applyStates", applyStates).setParameter("deptCode", deptCode);
		if(sendType!=3){
			queryObject.setParameter("sendType", sendType);
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			queryObject.setParameter("medicalrecordId", medicalrecordId);
		}
		
		List<DrugApplyoutNow> applyoutList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugApplyoutNow.class)).list();
		if(applyoutList!=null&&applyoutList.size()>0){
			  return applyoutList;
		  }
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public List<DrugApplyoutNow> findDeptSummary(String drugedBill, Integer opType,Integer sendType, String sendFlag, String applyState) {
		String [] drugedBills=drugedBill.split(",");
		String [] applyStates=applyState.split(",");
		String sql = "select a.id as id, a.apply_number as applyNumber,i.bed_id as bednoId,i.bed_name as bedno,i.patient_name as name,a.drug_code as drugCode,a.trade_name as  tradeName,a.specs as specs,CONCAT(a.trade_name,CONCAT(CONCAT('[',a.specs),']') )as specsName,"+
				 " a.retail_price as retailPrice,a.min_unit as minUnit,a.apply_num as applyNum,(a.retail_price*a.apply_num) as sumCost,a.dose_once as doseOnce,a.usage_code as usageCode,a.use_name as useName,"+
				 " a.dfq_freq as dfqFreq,a.dfq_cexp as dfqCexp,a.apply_date as applyDate,a.druged_empl as drugedEmpl,a.order_type as orderType"+
				 " from T_DRUG_APPLYOUT_NOW a "+
				 " left join t_inpatient_info_now i on a.patient_id = i.inpatient_no"+
				 " where a.druged_bill in (:drugedBills) and a.valid_state = '1' and a.op_type = :opType and a.apply_state in (:applyStates)";
		sql = sql + "  order by i.bed_name,i.patient_name";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("applyNumber",Hibernate.LONG).addScalar("bednoId").addScalar("bedno").addScalar("name").addScalar("drugCode").addScalar("tradeName").addScalar("specs").addScalar("specsName").addScalar("retailPrice",Hibernate.DOUBLE).addScalar("minUnit").addScalar("applyNum",Hibernate.DOUBLE)
				.addScalar("sumCost",Hibernate.DOUBLE).addScalar("doseOnce",Hibernate.DOUBLE).addScalar("usageCode").addScalar("useName").addScalar("dfqFreq").addScalar("dfqCexp").addScalar("applyDate",Hibernate.DATE).addScalar("drugedEmpl").addScalar("orderType");
		queryObject.setParameterList("drugedBills", drugedBills).setParameter("opType", opType).setParameterList("applyStates", applyStates);
		List<DrugApplyoutNow> applyoutList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugApplyoutNow.class)).list();
		if(applyoutList!=null&&applyoutList.size()>0){
			  return applyoutList;
		  }
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public List<DrugApplyoutNow> findDrugApplyoutByDeptId(String deptId,String applyState, Integer opType) {
		String hql  = "from DrugApplyoutNow where  deptCode = '"+deptId+"' and opType = '"+opType+"' and applyState in ('"+applyState+"')";
		List<DrugApplyoutNow> applyoutList = super.find(hql, null);
		if(applyoutList==null||applyoutList.size()<=0){
			return new ArrayList<DrugApplyoutNow>();
		}
		return applyoutList;
	}

	@Override
	public List<DrugApplyoutNow> findApplyoutByApplyNumber(String applyNumberCode) {
		if(applyNumberCode.indexOf(",")==-1){
		}else{
			applyNumberCode=applyNumberCode.replace(",","','");
		}
		String hql = "from DrugApplyoutNow where  applyNumber  in ('"+applyNumberCode+"')  and  del_flg=0 and stop_flg=0 and drugedBill is not  null";
		List<DrugApplyoutNow> applyoutList = super.find(hql, null);
		if(applyoutList==null||applyoutList.size()<=0){
			return new ArrayList<DrugApplyoutNow>();
		}
		return applyoutList;
	}

	@Override
	public InpatientInfoNow queryInpatientInfo(String patientId) {
		String hql = "from InpatientInfoNow where  inpatientNo = ? and inState = 'I'  ";
		List<InpatientInfoNow> infoList = super.find(hql, patientId);
		if(infoList==null||infoList.size()<=0){
			return new InpatientInfoNow();
		}
		return infoList.get(0);
	}

	@Override
	public DrugInfo queryDrugInfo(String itemCode) {
		String hql = "from DrugInfo where code = ? and del_flg=0 and stop_flg=0 ";
		List<DrugInfo> infoList = super.find(hql, itemCode);
		if(infoList==null||infoList.size()<=0){
			return new DrugInfo();
		}
		return infoList.get(0);
	}

	@Override
	public InpatientMedicineListNow queryInpatientMedicineList(String recipeNo,Integer sequenceNo) {
		String hql = "from InpatientMedicineListNow where  recipeNo = ? and sequenceNo = ?  ";
		List<InpatientMedicineListNow> medicineListList = super.find(hql, recipeNo,sequenceNo);
		if(medicineListList==null||medicineListList.size()<=0){
			return new InpatientMedicineListNow();
		}
		return medicineListList.get(0);
	}

	@Override
	public InpatientExecdrugNow queryInpatientExecdrug(String recipeNo,Integer sequenceNo) {
		String hql = "from InpatientExecdrugNow where  recipeNo = ? and sequenceNo = ?  ";
		List<InpatientExecdrugNow> execdrugList = super.find(hql, recipeNo,sequenceNo);
		if(execdrugList==null||execdrugList.size()<=0){
			return new InpatientExecdrugNow();
		}
		return execdrugList.get(0);
	}

	@Override
	public InpatientOrderNow queryInpatientOrder(String moOrder) {
		String hql = "from InpatientOrderNow where  moOrder = ? and  del_flg=0 and stop_flg=0 ";
		List<InpatientOrderNow> orderList = super.find(hql, moOrder);
		if(orderList==null||orderList.size()<=0){
			return new InpatientOrderNow();
		}
		return orderList.get(0);
	}

	@Override
	public InpatientStoMsgNow queryInpatientStoMsg(String billclassCode) {
		String hql = "from InpatientStoMsgNow where  billclassCode = ? and sendFlag = '0'";
		List<InpatientStoMsgNow> billclassCodeList = super.find(hql, billclassCode);
		if(billclassCodeList==null||billclassCodeList.size()<=0){
			return new InpatientStoMsgNow();
		}
		return billclassCodeList.get(0);
	}

	@Override
	public List<DrugApplyoutNow> findDetailed(String drugedBill, Integer opType,Integer sendType, String sendFlag, String[] applyStates, String deptId) {
		String hql = "select CONCAT(t.trade_name,CONCAT(CONCAT('[',t.specs),']') )as specsName,t.drug_code as drugCode,t.trade_name as tradeName,sum(t.apply_num) as applyNum,(sum(t.apply_num) * t.retail_price) as sumCost,t.apply_number as applyNumber,t.druged_bill as drugedBill from T_DRUG_APPLYOUT_NOW t where t.dept_code in (select a.dept_code from T_DRUG_APPLYOUT_NOW a  where a.druged_bill = :drugedBill) and t.op_type = :opType and t.valid_state = 1 and t.apply_state in (:applyStates)  ";
		       hql = hql + "  group by drug_code, trade_name,t.retail_price,t.specs,t.apply_number ,t.druged_bill  order by  drugedBill";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("specsName").addScalar("drugCode").addScalar("tradeName").addScalar("applyNum",Hibernate.DOUBLE).addScalar("sumCost",Hibernate.DOUBLE).addScalar("applyNumber",Hibernate.LONG).addScalar("drugedBill");
		queryObject.setParameter("drugedBill", drugedBill).setParameter("opType", opType).setParameterList("applyStates", applyStates);
		List<DrugApplyoutNow> applyoutList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugApplyoutNow.class)).list();
		if(applyoutList!=null&&applyoutList.size()>0){
			  return applyoutList;
		  }
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public InpatientInfoNow queryMedicalrecordId(String medicalrecordId) {
		String hql = "from InpatientInfoNow where idcardNo = ? and inState = 'I'  ";
		List<InpatientInfoNow> infoList = super.find(hql, medicalrecordId);
		if(infoList==null||infoList.size()<=0){
			return new InpatientInfoNow();
		}
		return infoList.get(0);
	}

	@Override
	public List<DrugStorage> findDrugStorageByDrugId(String drugDeptCode,String drugCode) {
		String hql = "from DrugStorage where  drugId = ? and storageDeptid = ?  and  del_flg=0 and stop_flg=0 order by groupCode asc";
		List<DrugStorage> storageList = super.find(hql, drugCode,drugDeptCode);
		if(storageList==null||storageList.size()<=0){
			return new ArrayList<DrugStorage>();
		}
		return storageList;
	}

	@Override
	public List<DrugOutstoreNow> findExDrugOutstore(List<String> outstore) {
		String hql = "from DrugOutstoreNow where  drugedBill in (:id) and  del_flg=0 and stop_flg=0 ";
		Query query=this.getSession().createQuery(hql);
		query.setParameterList("id", outstore);
		List<DrugOutstoreNow> outstoreList = query.list();
		if(outstoreList==null||outstoreList.size()<=0){
			return new ArrayList<DrugOutstoreNow>();
		}
		return outstoreList;
	}

	@Override
	public List<DrugApplyoutNow> findDrugApplyoutBywith(String applyNumberCode) {
		if(applyNumberCode.indexOf(",")==-1){
		}else{
			applyNumberCode=applyNumberCode.replace(",","','");
		}
		String hql = "from DrugApplyoutNow where applyNumber in ('"+applyNumberCode+"') and opType='5' and del_flg=0 and stop_flg=0 ";
		List<DrugApplyoutNow> applyoutList = super.find(hql, null);
		if(applyoutList==null||applyoutList.size()<=0){
			return new ArrayList<DrugApplyoutNow>();
		}
		return applyoutList;
	}

	@Override
	public InpatientFeeInfoNow queryInpatientFeeInfo(String recipeNo) {
		String hql = "from InpatientFeeInfoNow where recipeNo = ? and  del_flg=0 and stop_flg=0 ";
		List<InpatientFeeInfoNow> feeInfo = super.find(hql, recipeNo);
		if(feeInfo==null||feeInfo.size()<=0){
			return new InpatientFeeInfoNow();
		}
		return feeInfo.get(0);
	}

	@Override
	public InpatientMedicineListNow getMed(String recipeNo, Integer sequenceNo) {
		String hql = "from InpatientMedicineListNow where recipeNo = ? and sequenceNo=? and  del_flg=0 and stop_flg=0 ";
		List<InpatientMedicineListNow> feeInfo = super.find(hql, recipeNo,sequenceNo);
		if(feeInfo==null||feeInfo.size()<=0){
			return null;
		}
		return feeInfo.get(0);
	}
}
