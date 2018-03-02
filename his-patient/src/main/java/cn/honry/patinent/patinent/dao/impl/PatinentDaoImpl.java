package cn.honry.patinent.patinent.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.patient.vo.CheckAccountVO;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.patinent.dao.PatinmentDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Repository("patinmentDao")
@SuppressWarnings({ "all" })
public class PatinentDaoImpl extends HibernateEntityDao<Patient> implements PatinmentDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
//	@Autowired
//	@Qualifier(value = "inpatientInfoDAO")
//	private InpatientInfoDAO inpatientInfoDAO;

	/**
	 * 查询数据
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<PatientIdcardVO> listPatient(PatientIdcardVO vo, String page, String rows) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.PATINENT_ID as id,p.PATIENT_NAME as patientName,p.PATIENT_SEX as patientSex,p.PATIENT_HANDBOOK as patientHandbook,p.PATIENT_EMAIL as patientEmail,p.PATIENT_PHONE as patientPhone,p.PATIENT_LINKMAN as patientLinkman,p.PATIENT_LINKRELATION as patientLinkrelation,p.MEDICALRECORD_ID as medicalrecordId,i.IDCARD_ID as idcardId,i.IDCARD_NO as idcardNo,i.IDCARD_TYPE as idcardType,i.IDCARD_CREATETIME as idcardCreatetime,i.IDCARD_OPERATOR as idcardOperator ,p.patient_certificatesno as patientCertificatesno from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT p LEFT JOIN T_PATIENT_IDCARD i ON p.PATINENT_ID = i.PATINENT_ID WHERE p.DEL_FLG=0 ");
		this.whereJoin(vo,sql);
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("patientName").addScalar("patientSex",Hibernate.INTEGER).addScalar("patientHandbook").addScalar("patientEmail")
		.addScalar("patientPhone").addScalar("patientLinkman").addScalar("patientLinkrelation").addScalar("idcardId").addScalar("idcardNo")
		.addScalar("idcardType").addScalar("medicalrecordId").addScalar("idcardCreatetime",Hibernate.DATE).addScalar("idcardOperator").addScalar("patientCertificatesno");
		query.setResultTransformer(Transformers.aliasToBean(PatientIdcardVO.class));
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		query.setFirstResult((start - 1) * count).setMaxResults(count);
		return query.list();
	}
	
	/**
	 * 查询数据,第二版本,用next标注
	 * @date 2015-12-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<PatientIdcardVO> listPatientNext(PatientIdcardVO vo,String idcardId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.PATIENT_BIRTHDAY as patientBirthday,p.PATIENT_CERTIFICATESTYPE as patientCertificatestype,"
				+ "p.UNIT_ID as businessContractunit,"//zpty20160426增加合同单位
				+ "p.PATIENT_CERTIFICATESNO as patientCertificatesno,p.PATIENT_ADDRESS as patientAddress,p.PATIENT_CITY as patientCity,"
				+ "p.PATINENT_ID as id,p.PATIENT_NAME as patientName,p.PATIENT_SEX as patientSex,p.PATIENT_HANDBOOK as patientHandbook,"
				+ "p.PATIENT_EMAIL as patientEmail,p.PATIENT_PHONE as patientPhone,p.PATIENT_LINKMAN as patientLinkman,"
				+ "p.PATIENT_LINKRELATION as patientLinkrelation,p.MEDICALRECORD_ID as medicalrecordId,i.IDCARD_ID as idcardId,i.IDCARD_NO as idcardNo,"
				+ "i.IDCARD_TYPE as idcardType,i.IDCARD_CREATETIME as idcardCreatetime,i.IDCARD_OPERATOR as idcardOperator,i.IDCARD_REMARK as idcardRemark,"
				+ "i.IDCARD_STATUS as idcardStatus from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT p  LEFT JOIN T_PATIENT_IDCARD i  ON p.PATINENT_ID = i.PATINENT_ID WHERE p.DEL_FLG=0 ");
		this.whereJoinNext(vo,sql,idcardId);
		
		sql.append("  ORDER BY  i.IDCARD_CREATETIME desc");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("patientName").addScalar("patientSex",Hibernate.INTEGER).addScalar("patientHandbook").addScalar("patientEmail")
		.addScalar("patientPhone").addScalar("patientLinkman").addScalar("patientLinkrelation").addScalar("idcardId").addScalar("idcardNo")
		.addScalar("idcardType").addScalar("medicalrecordId").addScalar("idcardCreatetime",Hibernate.DATE).addScalar("idcardOperator")
		.addScalar("idcardRemark").addScalar("patientBirthday",Hibernate.DATE).addScalar("patientCertificatestype").addScalar("patientCertificatesno")
		.addScalar("patientAddress").addScalar("patientCity").addScalar("idcardStatus",Hibernate.INTEGER).addScalar("businessContractunit");
		query.setResultTransformer(Transformers.aliasToBean(PatientIdcardVO.class));
		return query.list();
	}
	
	/**
	 * 服务于就诊卡管理  多条数据时使用病历卡查询
	 * @date 2016年9月28日17:45:16
	 * @author GH
	 * @version 1.0
	 * @return
	 */
	@Override
	public List listPatientByMedicalrecord(String medicalrecord) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.PATIENT_BIRTHDAY as patientBirthday,p.PATIENT_CERTIFICATESTYPE as patientCertificatestype,"
				+ "p.UNIT_ID as businessContractunit,"
				+ "p.PATIENT_CERTIFICATESNO as patientCertificatesno,p.PATIENT_ADDRESS as patientAddress,p.PATIENT_CITY as patientCity,"
				+ "p.PATINENT_ID as id,p.PATIENT_NAME as patientName,p.PATIENT_SEX as patientSex,p.PATIENT_HANDBOOK as patientHandbook,"
				+ "p.PATIENT_EMAIL as patientEmail,p.PATIENT_PHONE as patientPhone,p.PATIENT_LINKMAN as patientLinkman,"
				+ "p.PATIENT_LINKRELATION as patientLinkrelation,p.MEDICALRECORD_ID as medicalrecordId,i.IDCARD_ID as idcardId,i.IDCARD_NO as idcardNo,"
				+ "i.IDCARD_TYPE as idcardType,i.IDCARD_CREATETIME as idcardCreatetime,i.IDCARD_OPERATOR as idcardOperator,i.IDCARD_REMARK as idcardRemark,"
				+ "i.IDCARD_STATUS as idcardStatus from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT p  LEFT JOIN T_PATIENT_IDCARD i  ON p.PATINENT_ID = i.PATINENT_ID WHERE p.DEL_FLG=0 ");
		if(StringUtils.isNotBlank(medicalrecord)){
			sql.append(" AND p.MEDICALRECORD_ID='"+medicalrecord+"' ");
		}
		sql.append("  ORDER BY  i.IDCARD_CREATETIME desc");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("patientName").addScalar("patientSex",Hibernate.INTEGER).addScalar("patientHandbook").addScalar("patientEmail")
		.addScalar("patientPhone").addScalar("patientLinkman").addScalar("patientLinkrelation").addScalar("idcardId").addScalar("idcardNo")
		.addScalar("idcardType").addScalar("medicalrecordId").addScalar("idcardCreatetime",Hibernate.DATE).addScalar("idcardOperator")
		.addScalar("idcardRemark").addScalar("patientBirthday",Hibernate.DATE).addScalar("patientCertificatestype").addScalar("patientCertificatesno")
		.addScalar("patientAddress").addScalar("patientCity").addScalar("idcardStatus",Hibernate.INTEGER).addScalar("businessContractunit");
		query.setResultTransformer(Transformers.aliasToBean(PatientIdcardVO.class));
		return query.list();
	}
	/**
	 * 查询数据总数
	 * @date 2015-06-02
	 * @author sgt
	 * @Modifier：liujl
	 * @ModifyDate：2016-6-6 上午09:56:35  
	 * @ModifyRmk：  修改获取记录总条数方法
	 * @version 1.0
	 * @return
	 */
	@Override
	public int getPatientCount(PatientIdcardVO vo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.PATINENT_ID as id,p.PATIENT_NAME as patientName,p.PATIENT_SEX as patientSex,p.PATIENT_HANDBOOK as patientHandbook,p.PATIENT_EMAIL as patientEmail,p.PATIENT_PHONE as patientPhone,p.PATIENT_LINKMAN as patientLinkman,p.PATIENT_LINKRELATION as patientLinkrelation,p.MEDICALRECORD_ID as medicalrecordId,i.IDCARD_ID as idcardId,i.IDCARD_NO as idcardNo,i.IDCARD_TYPE as idcardType,i.IDCARD_CREATETIME as idcardCreatetime,i.IDCARD_OPERATOR as idcardOperator from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT p LEFT JOIN T_PATIENT_IDCARD i ON p.PATINENT_ID = i.PATINENT_ID WHERE p.DEL_FLG=0 ");
		this.whereJoin(vo,sql);
		/*SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("patientName").addScalar("patientSex",Hibernate.INTEGER).addScalar("patientHandbook").addScalar("patientEmail").addScalar("patientPhone").addScalar("patientLinkman").addScalar("patientLinkrelation").addScalar("idcardId").addScalar("idcardNo").addScalar("idcardType").addScalar("medicalrecordId").addScalar("idcardCreatetime",Hibernate.DATE).addScalar("idcardOperator");
		query.setResultTransformer(Transformers.aliasToBean(PatientIdcardVO.class));
		return query.list()!=null?query.list().size():0;*/
		return super.getSqlTotal(sql.toString());
	}
	
	/**
	 * 动态拼接条件
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	private void whereJoin(PatientIdcardVO vo,StringBuilder sql) {
		if(vo != null){
			if(StringUtils.isNotBlank(vo.getPatientName())){
				sql.append(" AND (p.PATIENT_NAME LIKE '%"+vo.getPatientName()+"%'");
				sql.append(" or i.IDCARD_NO LIKE '%"+vo.getPatientName()+"%'");
				sql.append(" or p.MEDICALRECORD_ID LIKE '%"+vo.getPatientName()+"%'");
				sql.append(" or p.PATIENT_HANDBOOK LIKE '%"+vo.getPatientName()+"%')");
			}
			if(StringUtils.isNotBlank(vo.getName())){
				sql.append(" AND p.PATIENT_NAME LIKE '%"+vo.getName()+"%'");
				sql.append(" or p.PATIENT_WB LIKE '%"+vo.getName()+"%'");
				sql.append(" or p.PATIENT_INPUTCODE LIKE '%"+vo.getName()+"%'");
				sql.append(" or p.PATIENT_PINYIN LIKE '%"+vo.getName()+"%'");
				sql.append(" or p.PATIENT_CERTIFICATESNO LIKE '%"+vo.getName()+"%'");
				sql.append(" or i.IDCARD_NO LIKE '%"+vo.getName()+"%'");
			}
			if(StringUtils.isNotBlank(vo.getIdcardType())){
				sql.append(" AND i.IDCARD_TYPE = '"+vo.getIdcardType()+"'");
			}
		}
	}
	
	/**
	 * 动态拼接条件,第二版,用next标注
	 * @date 2015-12-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 * @ModifyUser GH
	 * @modifys 查询情况修改， 如果只有患名称，  则患者名称为精确查询， 如果查询条件有患者名称并且有别的条件，则用模糊查询
	 */
	private void whereJoinNext(PatientIdcardVO vo,StringBuilder sql,String idcardId) {
		if(vo != null){
			if(StringUtils.isNotBlank(idcardId)){
				sql.append(" AND i.idcard_no='"+idcardId+"' ");
			}
			if(vo.getPatientSex()!=null){//性别
				sql.append(" AND p.PATIENT_SEX = "+vo.getPatientSex());
			}
			if(StringUtils.isNotBlank(vo.getBrithDayCondition())){//生日
				/** 修改 gh 生日直接类型string比较**/
				/**  最新修改  gh 2017年2月9日15:51:09 将条件使用to_date查询*/
				Date date=DateUtils.parseDateY_M_D(vo.getBrithDayCondition());
				Date dateEnd=DateUtils.addDay(date, 1);
				String endDateString = DateUtils.formatDateY_M_SLASH(dateEnd);
				sql.append(" AND p.PATIENT_BIRTHDAY >= TO_DATE('"+vo.getBrithDayCondition()+"','yyyy-MM-dd') and p.PATIENT_BIRTHDAY < TO_DATE('"+endDateString+"','yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(vo.getPatientPhone())){//联系方式
				sql.append(" AND p.PATIENT_PHONE = '"+vo.getPatientPhone()+"'");
			}
			if(StringUtils.isNotBlank(vo.getPatientCertificatestype())){//证件类型
				sql.append(" AND p.PATIENT_CERTIFICATESTYPE = '"+vo.getPatientCertificatestype()+"'");
			}
			if(StringUtils.isNotBlank(vo.getPatientCertificatesno())){//证件号
				sql.append(" AND p.PATIENT_CERTIFICATESNO = '"+vo.getPatientCertificatesno()+"'");
			}
			if(StringUtils.isNotBlank(vo.getPatientCity())){//省市县
				sql.append(" AND p.PATIENT_CITY = '"+vo.getPatientCity()+"'");
			}
			if(StringUtils.isNotBlank(vo.getPatientAddress())){//家庭地址
				sql.append(" AND p.PATIENT_ADDRESS LIKE  '%"+vo.getPatientAddress()+"%'");
			}
			if(StringUtils.isNotBlank(vo.getPatientName())){//姓名
				sql.append(" AND p.PATIENT_NAME = '"+vo.getPatientName()+"'");
			}
		}
	}

	/**
	 * 修改
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */	
	@Override
	public void edit(Patient patient) {
		this.save(patient);
		OperationUtils.getInstance().conserve(null, "就诊卡管理", "INSERT_INTO", "T_PATIENT", OperationUtils.LOGACTIONINSERT);
	}
	/**
	 * 删除
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */	
	@Override
	public void delete(Patient patient) {
		StringBuilder sql = new StringBuilder();
		sql.append("update Patient set del_flg =1");
		sql.append(" where Id in ('"+patient.getId()+"') ");
		this.getSession().createQuery(sql.toString()).executeUpdate();

	}
	/**
	 * 根据ID返回数据
	 * @date 2015-12-10
	 * @author zhenglin
	 * @version 1.0
	 * @return
	 */	
	@Override
	public Patient queryById(String patient) {
		String hql ="from Patient where patientCertificatesno = '"+patient+"' ";
		List<Patient> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new Patient();
	}
	
	@Override
	public String getIdByNo(String certificatesType,String certificatesNo) {
		String hql="from Patient p where p.del_flg=0 and p.stop_flg=0 and p.patientCertificatestype='"+certificatesType+"' and p.patientCertificatesno='"+certificatesNo+"'";
		List<Patient>  patientList = super.findByObjectProperty(hql, null);
		if(patientList!=null&&patientList.size()>0){
			return patientList.get(0).getId();
		}
		return null;
	}
	/**  
	 *  
	 * @Description： 点击树节点查询
	 * @Author：wujiao
	 * @CreateDate：2015-7-6 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-6 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Patient gethzid(String id, String type, String typeno) {
		Patient patient = super.get(id);
		if(patient!=null){
			String ehql="FROM InpatientInfo i WHERE i.del_flg = 0 and i.certificatesNo = '"+patient.getPatientCertificatesno()+"'";
//			List<InpatientInfo> list=inpatientInfoDAO.findByObjectProperty(ehql, null);
			List<InpatientInfo> list=this.find(ehql, null);
			/*if(list!=null&&list.size()>0){
				patient.setNo(list.get(0).getMedicalrecordId());
			}*/
		}
		return patient;
	}
	@Override
	public boolean checkHandBook(String handBook,String patientId) {
		String hql="from Patient p where p.del_flg=0 and p.stop_flg=0 and p.patientHandbook='"+handBook+"'";
		if(StringUtils.isNotEmpty(patientId)){
			hql += " and p.id != '" + patientId + "'";
		}
		List<Patient>  patientList = this.getSession().createQuery(hql).list();
		if(patientList!=null&&patientList.size()>0){
			return true;
		}
		return false;
	}
	@Override
	public CheckAccountVO checkAllAccount(String medicalId) {
		String sql = "select (select t.in_state from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info t where t.medicalrecord_id='"+medicalId+"') as state, " +
				"NVL((select t1.account_balance from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_account t1 where t1.medicalrecord_id='"+medicalId+"'),0) as ibalance, " +
				"NVL((select t2.account_balance from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_account t2 where t2.idcard_id=(select t3.idcard_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_patient_idcard t3 where t3.PATINENT_ID = (select t4.PATINENT_ID from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT t4 where t4.medicalrecord_id='"+medicalId+"'))),0) as balance " +
				"from dual ";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("state").addScalar("ibalance",Hibernate.DOUBLE).addScalar("balance",Hibernate.DOUBLE);
		
		List<CheckAccountVO> list=null;
		try {
			list = query.setResultTransformer(Transformers.aliasToBean(CheckAccountVO.class)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()!=0){
			return list.get(0);
		}
		return new CheckAccountVO();
	}
	@Override
	public Patient savePatient(Patient patient) {
		this.getSession().save(patient);
		this.getSession().flush();
		this.getSession().refresh(patient);
		return patient;
	}
	/**
	 * @Description: 通过病历号查询患者信息
	 * @Author：  tcj
	 * @CreateDate： 2015-12-25
	 * @version 1.0
	**/
	@Override
	public Patient queryByMedicalrecordId(String medicalrecordId) {
		String hql="from Patient p where p.del_flg = 0 and p.medicalrecordId = ? ";
		List<Patient> patientList=super.find(hql, medicalrecordId);
		if(patientList!=null&&patientList.size()>0){
			return patientList.get(0);
		}
		return new Patient();
	}
	
	/**
	 * 根据员工工号查询员工
	 * zpty 20160324
	 * @param jobNo
	 * @return
	 */
	@Override
	public List<SysEmployee> findEmpByjobNo(String jobNo) {
		String hql = "from SysEmployee r where r.del_flg=0 and r.stop_flg=0 and r.jobNo='"+jobNo+"'";
		List<SysEmployee> list = super.find(hql, null);
		return list.size()==0?new ArrayList<SysEmployee>():list;
	}
	
	/**  
	 *  
	 * @Description：  合同单位下拉框
	 * @Author：zpty
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessContractunit> queryUnitCombobox() {
		String hql = " from BusinessContractunit where del_flg=0 and stop_flg=0  ";
		List<BusinessContractunit> contractunitList = super.find(hql, null);
		if(contractunitList==null||contractunitList.size()<=0){
			return new ArrayList<BusinessContractunit>();
		}
		return contractunitList;
	}
}
