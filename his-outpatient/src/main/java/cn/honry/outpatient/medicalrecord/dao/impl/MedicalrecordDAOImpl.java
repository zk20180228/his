package cn.honry.outpatient.medicalrecord.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.medicalrecord.dao.MedicalrecordDAO;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("medicalrecordDAO")
@SuppressWarnings({ "all" })
public class MedicalrecordDAOImpl extends HibernateEntityDao<OutpatientMedicalrecord> implements MedicalrecordDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "ationDAO")
	private RegistrationDAO ationDAO;//挂号信息表
	
	/**  
	 *  
	 * @Description：添加&修改
	 * @Author：wujiao
	 * @CreateDate：2015-7-9 上午15:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-9 上午15:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrUpdatemedicalrecord(OutpatientMedicalrecord entity) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		OutpatientMedicalrecord cord = this.getRecordByClinicCode(entity.getClinicCode());
		if(cord == null){
			RegistrationNow registration = ationDAO.queryInfoByCliNo(entity.getClinicCode());
			SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			entity.setId(null);
			entity.setPatientNo(registration.getMidicalrecordId());
			entity.setName(registration.getPatientName());
			entity.setSex(registration.getPatientSex().toString());
			entity.setAge(Integer.valueOf(registration.getPatientAge()));
			entity.setBirthday(registration.getPatientBirthday());
			entity.setTelephone(registration.getRelaPhone());
			entity.setPasource(1);
			entity.setCreateUser(user.getAccount());
			entity.setCreateDept(dept.getDeptCode());
			entity.setStop_flg(0);
			entity.setDel_flg(0);
			entity.setCreateTime(new Date());
			Date date = new Date();
			registration.setBeginTime(date);
			ationDAO.save(registration);
			super.save(entity);
		}else{
			cord.setDiagnoseType(entity.getDiagnoseType());
			cord.setDiagnoseDate(entity.getDiagnoseDate());
			cord.setMaindesc(entity.getMaindesc());
			cord.setAllergichistory(entity.getAllergichistory());
			cord.setHeredityHis(entity.getHeredityHis());
			cord.setPresentillness(entity.getPresentillness());
			cord.setTemperature(entity.getTemperature());
			cord.setPulse(entity.getPulse());
			cord.setBreathing(entity.getBreathing());
			cord.setBloodPressure(entity.getBloodPressure());
			cord.setPhysicalExamination(entity.getPhysicalExamination());
			cord.setHistoryspecil(entity.getHistoryspecil());
			cord.setAdvice(entity.getAdvice());
			cord.setDiagnose1(entity.getDiagnose1());
			cord.setCheckresult(entity.getCheckresult());
			cord.setUpdateUser(user.getAccount());
			cord.setUpdateTime(new Date());
			super.save(cord);
		}
	}

	/**  
	 *  
	 * @Description：查询病历
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：
	 * @param:clinicNo 看诊号
	 * @version 1.0
	 *
	 */
	@Override
	public OutpatientMedicalrecord getRecordByClinicCode(String clinicCode) {
		String hql = "FROM OutpatientMedicalrecord r WHERE r.clinicCode = ?";
		OutpatientMedicalrecord record = (OutpatientMedicalrecord) super.excHqlGetUniqueness(hql, clinicCode);
		if(record!=null&&StringUtils.isNotBlank(record.getId())){
			return record;
		}
		return null;
	}
	
	/**  
	 * 根据门诊号和病历号查询患者是否有病历信息
	 * @Description：  获得护士站
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean findMedicalByNoAndCode(String patientNo, String clinicCode) {
		String hql = "select count(*) FROM  OutpatientMedicalrecord m WHERE m.patientNo = ? and m.clinicCode = ? and m.stop_flg = 0 and m.del_flg = 0";
		Long count = (Long) this.createQuery(hql, patientNo,clinicCode).uniqueResult();
		if(count>0){
			return true;
		}
		return false;
	}
	
	/**  
	 *  
	 * @Description：根据门诊号获得执行科室ID
	 * @Author：huangbiao
	 * @CreateDate：2016-3-18 上午11:56:59  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public List<String> getDeptId(String clinicCode) {
		String sql = "select DISTINCT t.exec_dpcd from t_outpatient_recipedetail_now t where t.clinic_code ='"+clinicCode+"' and t.class_code = (select code_encode from t_business_dictionary where code_name = '检验')";
		Query q = this.getSession().createSQLQuery(sql);
		List<String> list = q.list();
		return list;
	}
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：tangfeishuai
	 * @CreateDate：2016-03-23下午15:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<String> findExecDpcdByCodeAndPatino(String clinicCode, String patientNo) {
		String sql = "select DISTINCT t.exec_dpcd from "+HisParameters.HISPARSCHEMAHISUSER+"t_outpatient_recipedetail_now t where t.clinic_code = '"+clinicCode+"' and t.class_code = (select code_encode from t_business_dictionary where code_name = '检查')";
		Query q = this.getSession().createSQLQuery(sql);
		List<String> list = q.list();
		return list;
	}
	
}
