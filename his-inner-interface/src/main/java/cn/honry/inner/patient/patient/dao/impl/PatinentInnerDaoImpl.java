package cn.honry.inner.patient.patient.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
@Repository("patinmentInnerDao")
@SuppressWarnings({ "all" })
public class PatinentInnerDaoImpl extends HibernateEntityDao<Patient> implements PatinmentInnerDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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
			List<InpatientInfo> list=this.find(ehql, null);
		}
		return patient;
	}
	@Override
	public boolean checkHandBook(String handBook,String patientId) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("handBook", handBook);
		if (StringUtils.isNotBlank(patientId)) {
			pMap.put("patientId", patientId);
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select p.patient_Handbook from t_Patient p where p.del_flg=0 and p.stop_flg=0 and p.patient_Handbook=:handBook");
		sql.append(StringUtils.isNotBlank(patientId) ? " AND p.PATINENT_ID != :patientId " : "");
		List<String> value = namedParameterJdbcTemplate.queryForList(sql.toString(), pMap, String.class);
		if(value!=null&&value.size()>0){
			return true;
		}
		return false;
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

	/**
	 * 根据传过来的病历号来返回住院次数
	 * @date 2016-11-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 */	
	@Override
	public Integer querySumByMedicalreId(String medicalrecordId) {
		String hql="from Patient p where p.del_flg = 0 and p.stop_flg = 0 and p.medicalrecordId = ? ";
		List<Patient> patientList=super.find(hql, medicalrecordId);
		if(patientList!=null&&patientList.size()>0){
			return patientList.get(0).getInpatientSum();
		}
		return 0;
	}

	/**
	 * 根据就诊卡号获取病历号
	 * @date 2016-11-24
	 * @author zhangjin
	 * @version 1.0
	 * @return
	 */	
	@Override
	public String getMedicalrecordId(String cardNo) {
		String hql="select medicalrecordId as medicalrecordId from Patient where stop_flg=0 and del_flg=0 and cardNo='"+cardNo+"'";
		List<Patient> list=this.getSession().createQuery(hql).setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0).getMedicalrecordId();
		}
		return "";
	}
	/**
	 * 更新患者数据中的cardNo字段  根据ID
	 * 2017年2月8日09:42:28
	 * GH
	 */
	@Override
	public void updCradNoByIdOrCard(String id,String idcard, String cradNo) {
		String hql="update Patient set cardNo='"+cradNo+"' where ";
		if(StringUtils.isNotBlank(id)){
			hql+="id='"+id+"'";
		}else{
			hql+="cardNo='"+idcard+"'";
		}
		
		Query query=this.getSession().createQuery(hql);
		query.executeUpdate();
	}
	
}
