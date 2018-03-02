package cn.honry.inner.outpatient.register.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;

@Repository("registerInfoInInterDAO")
@SuppressWarnings({ "all" })
public class RegisterInfoInInterDAOImpl extends HibernateEntityDao<RegisterInfo> implements RegisterInfoInInterDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	private PatinmentInnerDao patinmentInnerDao;
	
	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public InfoInInterPatient queryRegisterInfo(String idcardNo) {
//		String hql = "select p.patinent_id as patientId,i.idcard_id as idCardNo,p.patient_name as name,p.patient_sex as sex,p.patient_birthday as dates,p.medicalrecord_id as infoMedicalrecordId,p.patient_certificatesno as patientCertificatesno,p.unit_id as cout " +
		//idcard_id改为idcard_NO 2017-02-15
		String hql = "select p.patinent_id as patientId,i.idcard_NO as idCardNo,p.patient_name as name,p.patient_sex as sex,p.patient_birthday as dates,p.medicalrecord_id as infoMedicalrecordId,p.patient_certificatesno as patientCertificatesno,p.unit_id as cout " +
			    	 "from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_idcard i left join "+HisParameters.HISPARSCHEMAHISUSER+"t_patient p on i.patinent_id = p.patinent_id where i.idcard_no=:idcardNo and i.del_flg=0  and  i.stop_flg=0  and p.del_flg=0 and p.stop_flg=0";
		 SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("idCardNo").addScalar("name").addScalar("sex",Hibernate.INTEGER).addScalar("dates",Hibernate.DATE).addScalar("infoMedicalrecordId").addScalar("patientCertificatesno").addScalar("patientId").addScalar("cout");
		 queryObject.setParameter("idcardNo", idcardNo);
		 List<InfoInInterPatient> infoPatient = queryObject.setResultTransformer(Transformers.aliasToBean(InfoInInterPatient.class)).list();
		  if(infoPatient!=null&&infoPatient.size()>0){
			  return infoPatient.get(0);
		  }
		  return new InfoInInterPatient();
	}
	
	@Override
	public RegisterInfo getRegisterInfoByPatientNoAndNo(String patientNo,String no) {
		//String hql = " FROM RegisterInfo i WHERE i.stop_flg=0 AND i.del_flg=0 AND TO_CHAR(i.date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' AND i.midicalrecordId = '"+patientNo+"' AND i.no = '"+no+"' AND i.dept = '"+ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId()+"'";
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String hql = " FROM RegisterInfo i WHERE i.stop_flg=0 AND i.del_flg=0 AND TO_CHAR(i.date,'yyyy-MM-dd')='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' AND i.midicalrecordId = '"+patientNo+"' AND i.no = '"+no+"' AND i.dept = '"+dept.getDeptCode()+"'";
		List<RegisterInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return null;
		}
		return infoList.get(0);
	}
	
	/**  
	 *  
	 * @Description：  查询患者树
	 *@Author：wujiao
	 * @CreateDate：2015-6-25 上午3:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 上午3:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterInfo> getInfoByEmployeeId(String id,String type) {
		String hql="from RegisterInfo r where r.del_flg=0 and r.stop_flg=0 AND r.expxrt = '"+id+"' ";
		if(type!=null){
			hql += " AND r.seeFlag = "+type+"";
		}
		List<RegisterInfo>  registerInfoList =super.findByObjectProperty(hql, null);
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return null;
	}
	
	@Override
	public List<RegisterInfo> getInfo(String no) {
		String sql="select t.register_id as id ,t.register_no as no,e.employee_name as expxrtName,d.dept_name as deptName " +
					"from "+HisParameters.HISPARSCHEMAHISUSER+"t_department d right join "+HisParameters.HISPARSCHEMAHISUSER+"t_register_info t  on t.register_dept=d.dept_id left join t_employee e on t.register_expert=e.employee_id where 1=1 ";
		if(StringUtils.isNotBlank(no)){
			sql = sql+" AND t.idcard_id = (SELECT i.IDCARD_ID FROM T_PATIENT_IDCARD i WHERE i.IDCARD_NO = '"+no+"') ";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString()).addScalar("id").addScalar("no").addScalar("expxrtName").addScalar("deptName");
		List<RegisterInfo> registerInfoList = queryObject.setResultTransformer(Transformers.aliasToBean(RegisterInfo.class)).list();
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return new ArrayList<RegisterInfo>();
	}
	
	/**  
	 *  
	 * @Description：  根据条件获得挂号信息 
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-6 上午09:07:48  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-6 上午09:07:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param juri 是不是查询部门  id （user/dept）  no 就诊/病例 号  sTime开始时间  eTime 结束时间
	 *
	 */
	@Override
	public List<RegisterInfo> getInfo(String juri,String id, String no, String sTime,String eTime) {
		String hql="from RegisterInfo r where r.del_flg=0 and r.stop_flg=0 ";
		if(juri!=null&&"2".equals(juri)){//部门级权限  可以查询该部门下的所有患者
			hql = hql+" AND r.dept = '"+id+"'";
		}else{//个人级权限  只可以查询自己治疗的患者
			hql = hql+" AND r.expxrt = '"+id+"'";
		}
		if(StringUtils.isNotBlank(sTime)){
			hql = hql+" AND TO_CHAR(r.date,'YYYY-MM-DD ') >= '"+sTime+"' ";
		}
		if(StringUtils.isNotBlank(eTime)){
			hql = hql+" AND TO_CHAR(r.date,'YYYY-MM-DD') <= '"+eTime+"' ";
		}
		if(StringUtils.isNotBlank(no)){
			hql = hql+" AND r.midicalrecordId = '"+no+"' OR r.idcardId = '"+no+"' ";
		}
		List<RegisterInfo>  registerInfoList = super.findByObjectProperty(hql, null);
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return new ArrayList<RegisterInfo>();
	}
	
	@Override
	public String getIdByNo(String certificatesType,String certificatesNo) {
		return patinmentInnerDao.getIdByNo(certificatesType,certificatesNo);
	}

	@Override
	public List<RegistrationNow> getInfoByTime(Date startTime, Date endTime, String payTypeId, String registrarId) {
		String sql ="select t.clinic_code as clinicCode ,t.trans_type as transType,  t.card_no as cardNo,  t.reg_date as regDate, t.noon_code as noonCode,  t.paykind_code as paykindCode, t.paykind_name as paykindName, t.pact_code as pactCode,  t.pact_name as pactName, t.medical_type as medicalType,"
				+ "t.mcard_no as mcardNo, t.reglevl_code as reglevlCode , t.reglevl_name as reglevlName,  t.dept_code as deptCode,t.dept_name as deptName,t.ynregchrg as ynregchrg ,t.invoice_no as invoiceNo,t.reg_fee as regFee,t.reg_fee_code as regFeeCode ,t.chck_fee_code as chckFeeCode ,t.chck_fee as chckFee,"
				+ "t.diag_fee_code as diagFeeCode,t.diag_fee as diagFee,t.oth_fee_code as othFeeCode,t.oth_fee as othFee,t.book_fee_code as bookFeeCode,t.book_fee as bookFee, t.book_flag as bookFlag, t.eco_cost as ecoCost,t.own_cost as ownCost,t.pub_cost as pubCost,t.pay_cost as payCost,t.sum_cost as sumCost,t.valid_flag as validFlag "
				+ ", t.balance_flag as balanceFlag , t.balance_no as balanceNo,t.balance_opcd as balanceOpcd, t.balance_date as balanceDate ,t.IN_STATE      as inState  "
				+ "from t_register_main_now t inner join t_business_paymode_now b on t.invoice_no=b.invoice_no   ";
		//String sql = "from Registration t  where   t.del_flg = 0 AND t.createUser = '"+registrarId+"' ";
		sql+="  where  t.stop_flg=0 and t.del_flg=0  and t.createUser = '"+registrarId+"'  ";
		if(StringUtils.isNotBlank(payTypeId)){
			sql = sql+ " AND b.mode_code = '"+payTypeId+"' ";
		}
		if(startTime!=null&&endTime!=null){
			sql = sql+ " AND t.createTime > TO_DATE('"+DateUtils.formatDateY_M_D_H_M_S(startTime)+"','YYYY-MM-DD HH24:MI:SS') AND t.createTime <= TO_DATE('"+DateUtils.formatDateY_M_D_H_M_S(endTime)+"','YYYY-MM-DD HH24:MI:SS')";
		}
		 SQLQuery query = this.getSession().createSQLQuery(sql.toString()).addScalar("clinicCode").addScalar("transType",Hibernate.INTEGER).addScalar("cardNo").addScalar("regDate",Hibernate.DATE).addScalar("noonCode",Hibernate.INTEGER).addScalar("paykindCode")
				 .addScalar("paykindName").addScalar("pactCode").addScalar("pactName").addScalar("medicalType").addScalar("mcardNo").addScalar("reglevlCode").addScalar("reglevlName").addScalar("deptCode").addScalar("deptName")
				 .addScalar("ynregchrg",Hibernate.INTEGER).addScalar("invoiceNo").addScalar("regFee",Hibernate.DOUBLE).addScalar("regFeeCode").addScalar("chckFeeCode").addScalar("chckFee",Hibernate.DOUBLE)
				 .addScalar("diagFeeCode").addScalar("diagFee",Hibernate.DOUBLE).addScalar("othFeeCode").addScalar("othFee",Hibernate.DOUBLE).addScalar("bookFeeCode").addScalar("bookFee",Hibernate.DOUBLE).addScalar("bookFlag",Hibernate.INTEGER)
				 .addScalar("ecoCost",Hibernate.DOUBLE).addScalar("ownCost",Hibernate.DOUBLE).addScalar("pubCost",Hibernate.DOUBLE).addScalar("payCost",Hibernate.DOUBLE).addScalar("sumCost",Hibernate.DOUBLE).addScalar("validFlag",Hibernate.INTEGER)
				 .addScalar("balanceFlag",Hibernate.INTEGER).addScalar("balanceNo").addScalar("balanceOpcd").addScalar("balanceDate",Hibernate.DATE).addScalar("inState",Hibernate.INTEGER);
		 List<RegistrationNow> list= query.setResultTransformer(Transformers.aliasToBean(RegistrationNow.class)).list();
		//List<Registration>  list = this.find(sql, null);
		if(list.size()>0){
			return list;
		}else{
			return new ArrayList<RegistrationNow>();
		}
	}
}

