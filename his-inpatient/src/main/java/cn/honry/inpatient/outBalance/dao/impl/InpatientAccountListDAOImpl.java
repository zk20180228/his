package cn.honry.inpatient.outBalance.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientAccountListDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("inpatientAccountListDAO")
@SuppressWarnings("all")
public class InpatientAccountListDAOImpl extends HibernateEntityDao<InpatientAccount> implements InpatientAccountListDAO{
	
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<InpatientAccount> getID(String id) {
		String hql="from InpatientAccount where inpatientNo='"+id+"' and del_flg = 0 ";
		List<InpatientAccount> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientAccount>();
	}
	
	@Override
	public List<PatientAccountrepaydetail> Queryzhuanya(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(a.detail_debitamount) as detailDebitamount from  T_PATIENT_ACCOUNT t "
				+ "join t_patient_accountrepaydetail a "
				+ "on a.account_id=t.account_id where a.detail_depitamountt_type=8 "
				+ "and t.medicalrecord_id='"+id+"' and t.del_flg = 0");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("detailDebitamount",Hibernate.DOUBLE);
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}
	
	@Override
	public List<PatientAccountrepaydetail> SumAccount(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(t.DETAIL_DEBITAMOUNT) as detailDebitamount from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_accountrepaydetail t "
				+ " where t.account_id='"+id+"' "
				+ " and t.DETAIL_ACCOUNTTYPE=2 and t.DETAIL_DEPITAMOUNTT_TYPE=7 "
				+ " and t.DEL_FLG=0 and t.STOP_FLG= 0 ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("detailDebitamount",Hibernate.DOUBLE);
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}
	
	@Override
	public List<PatientAccountrepaydetail> getAccount(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(t.DETAIL_DEBITAMOUNT) as detailDebitamount from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_accountrepaydetail t "
				+ " where t.account_id='"+id+"' "
				+ " and t.DETAIL_ACCOUNTTYPE=2 and t.DETAIL_DEPITAMOUNTT_TYPE=5 "
				+ " and t.DEL_FLG=0 and t.STOP_FLG= 0 ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("detailDebitamount",Hibernate.DOUBLE);
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}

	@Override
	public List<InpatientInfo> getinpatient(String inpatientNo) {
		String hql="from InpatientInfo where inpatientNo=? and del_flg = 0 ";
		List<InpatientInfo> iList = super.find(hql, inpatientNo);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfo>();
	}

	@Override
	public List<InpatientBalanceHead> queryinpatientNo(String inpatientNo) {
		String hql="from InpatientBalanceHead where inpatientNo=? and del_flg = 0 ";
		List<InpatientBalanceHead> iList = super.find(hql, inpatientNo);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientBalanceHead>();
	}

	@Override
	public List<InpatientBalancePay> getbalanceOpercode(String balanceOpercode) {
		String hql="from InpatientBalancePay where balanceOpercode=? and del_flg = 0 ";
		List<InpatientBalancePay> iList = super.find(hql, balanceOpercode);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientBalancePay>();
	}
	
	@Override
	public void UpdateInpatientInPrepay(String inpatientNo,String balanceNo,String invoiceNo) {
		Date dt=new Date();
		String uesrId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql="update t_inpatient_inprepay set balance_state= 1,balance_no=?,invoice_no=?,balance_date=?,balance_opercode=? where inpatient_no = ? ";
		this.getSession().createSQLQuery(sql).setString(0, balanceNo).setString(1, invoiceNo).setDate(2, dt).setString(3, uesrId).setString(4, inpatientNo).executeUpdate();
	}
	
	@Override
	public void UpdateInpatientChangeprepay(String inpatientNo,String balanceNo) {
		Date dt=new Date();
		String uesrId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql="update t_inpatient_changeprepay set balance_state= 1,balance_no=?,updatetime=?,updateuser=? where clinic_no = ? ";
		this.getSession().createSQLQuery(sql).setString(0, balanceNo).setDate(1, dt).setString(2, uesrId).setString(3, inpatientNo).executeUpdate();
	}
	
	@Override
	public void UpdateInpatientDerate(String inpatientNo,String balanceNo,String invoiceNo) {
		String sql="update t_inpatient_derate set balance_state= 1,BALANCE_NO=?,INVOICE_NO=? where clinic_no = ? ";
		this.getSession().createSQLQuery(sql).setString(0, balanceNo).setString(1, invoiceNo).setString(2, inpatientNo).executeUpdate();
	}

	@Override
	public void UpdateInpatientFeeInfo(String inpatientNo,String balanceNo,String invoiceNo) {
		Date dt=new Date();
		String uesrId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql="update T_INPATIENT_FEEINFO set BALANCE_STATE= 1,BALANCE_NO=?,INVOICE_NO=?,BALANCE_DATE=?,BALANCE_OPERCODE=? where INPATIENT_NO =? ";
		this.getSession().createSQLQuery(sql).setString(0, balanceNo).setString(1, invoiceNo).setDate(2, dt).setString(3, uesrId).setString(4, inpatientNo).executeUpdate();
	}

	@Override
	public void UpdateInpatientMedicineList(String inpatientNo,String balanceNo,String invoiceNo) {
		String sql="update T_INPATIENT_MEDICINELIST set BALANCE_STATE= 1,BALANCE_NO=?,INVOICE_NO=? where INPATIENT_NO = ? ";
		this.getSession().createSQLQuery(sql).setString(0, balanceNo).setString(1, invoiceNo).setString(2, inpatientNo).executeUpdate();
	}

	@Override
	public void UpdateInpatientItemList(String inpatientNo,String balanceNo,String invoiceNo) {
		String sql="update T_INPATIENT_ITEMLIST set BALANCE_STATE= 1 where INPATIENT_NO = ? ";
		this.getSession().createSQLQuery(sql).setString(0, inpatientNo).executeUpdate();
	}

	@Override
	public void UpdateInpatientInfo(String inpatientNo) {
		String sql="update T_INPATIENT_INFO set IN_STATE= 'O' where INPATIENT_NO = ? ";
		this.getSession().createSQLQuery(sql).setString(0, inpatientNo).executeUpdate();
	}

	@Override
	public void saveInpatientBalancePay(InpatientBalancePay inpatientBalancePay) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		inpatientBalancePay.setCreateUser(user.getId());
		inpatientBalancePay.setCreateDept(dept.getId());
		inpatientBalancePay.setCreateTime(new Date());
		inpatientBalancePay.setStop_flg(0);
		inpatientBalancePay.setDel_flg(0);
		super.save(inpatientBalancePay);
	}

	@Override
	public void saveInpatientDerate(InpatientDerate inpatientDerate) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		inpatientDerate.setDerateCause("结算减免");
		inpatientDerate.setValidFlag("1");
		inpatientDerate.setDerateType("0");
		inpatientDerate.setTransType(1);
		inpatientDerate.setDerateKind("1");
		inpatientDerate.setBalanceState("1");
		inpatientDerate.setCreateUser(user.getId());
		inpatientDerate.setCreateDept(dept.getId());
		inpatientDerate.setCreateTime(new Date());
		inpatientDerate.setStop_flg(0);
		inpatientDerate.setDel_flg(0);
		super.save(inpatientDerate);
	}
	@Override
	public void saveInpatientShiftData(InpatientShiftData inpatientShiftData) {
		InpatientShiftData shiftData = new InpatientShiftData();
		shiftData.setShiftType("O");//代表出院
		shiftData.setOldDataName("病号");
		shiftData.setNewDataName("出院");
		shiftData.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		shiftData.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		shiftData.setCreateTime(DateUtils.getCurrentTime());
		this.save(shiftData);
	}

	@Override
	public List<PatientAccountrepaydetail> getmedicalrecordId(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(a.detail_debitamount) as detailDebitamount from  T_PATIENT_ACCOUNT t "
				+ "join t_patient_accountrepaydetail a "
				+ "on a.account_id=t.account_id where a.detail_depitamountt_type=6 "
				+ "and t.medicalrecord_id='"+id+"' and t.del_flg = 0");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("detailDebitamount",Hibernate.DOUBLE);
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}
	
	@Override
	public List<PatientAccountrepaydetail> getmedicalrecordIdZhuanya(String medicalrecordId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(a.detail_debitamount) as detailDebitamount from  T_PATIENT_ACCOUNT t "
				+ "join t_patient_accountrepaydetail a "
				+ "on a.account_id=t.account_id where a.detail_depitamountt_type=8 "
				+ "and t.medicalrecord_id='"+medicalrecordId+"'");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("detailDebitamount",Hibernate.DOUBLE);
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}

	@Override
	public List<PatientAccountrepaydetail> QueryPatientAccountrepaydetail(String id) {
		String hql="from PatientAccountrepaydetail where account.id='"+id+"' "
				+ "and del_flg = 0 and detailDepitamounttType = 7 and detailAccounttype= 2 ";
		List<PatientAccountrepaydetail> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}

	@Override
	public List<PatientAccountrepaydetail> QueryPatientAccountrepaydetailID(
			String id) {
		String hql="from PatientAccountrepaydetail where id='"+id+"' "
				+ "and del_flg = 0 and detailDepitamounttType = 7 and detailAccounttype= 2 ";
		List<PatientAccountrepaydetail> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}

	@Override
	public void saveInpatientBalanceHead(InpatientBalanceHead inpatientBalanceHead) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		inpatientBalanceHead.setTransType(1);
		inpatientBalanceHead.setCreateUser(user.getId());
		inpatientBalanceHead.setCreateDept(dept.getId());
		inpatientBalanceHead.setCreateTime(new Date());
		inpatientBalanceHead.setStop_flg(0);
		inpatientBalanceHead.setDel_flg(0);
		super.save(inpatientBalanceHead);
		
	}

	@Override
	public List<PatientAccountrepaydetail> getpayment(String medicalrecordId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.createtime as createTime,a.DETAIL_DEBITAMOUNT as detailDebitamount,a.Detail_Paytype as detailPaytype from  T_PATIENT_ACCOUNT t "
				+ "join t_patient_accountrepaydetail a "
				+ "on a.account_id=t.account_id where a.detail_depitamountt_type=4 "
				+ "and t.medicalrecord_id='"+medicalrecordId+"' and t.del_flg=0");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("createTime",Hibernate.TIME)
		.addScalar("detailDebitamount",Hibernate.DOUBLE)
		.addScalar("detailPaytype");
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}
	
	@Override
	public List<PatientAccountrepaydetail> getpaymentzonge(String medicalrecordId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(a.DETAIL_DEBITAMOUNT) as detailDebitamount from  T_PATIENT_ACCOUNT t " 
				+ "join t_patient_accountrepaydetail a "
				+ "on a.account_id=t.account_id where a.detail_depitamountt_type=4  "
				+ "and t.medicalrecord_id='"+medicalrecordId+"' and t.del_flg=0");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("detailDebitamount",Hibernate.DOUBLE);
		List<PatientAccountrepaydetail> list = queryObject.setResultTransformer(Transformers.aliasToBean(PatientAccountrepaydetail.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<PatientAccountrepaydetail>();
	}
	
	@Override
	public SysEmployee queryEmployee(String userId) {
		String hql = "from SysEmployee i where i.userId.id ='"+userId+"'";
		List<SysEmployee> list  = this.getSession().createQuery(hql).list();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		return new SysEmployee();
	}
}

