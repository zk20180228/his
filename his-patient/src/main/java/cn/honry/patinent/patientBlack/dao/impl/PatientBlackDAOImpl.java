package cn.honry.patinent.patientBlack.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.patinent.patientBlack.dao.PatientBlackDAO;

@Repository("patientBlackDAO")
@SuppressWarnings({ "all" })
public class PatientBlackDAOImpl extends HibernateEntityDao<PatientBlackList> implements PatientBlackDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public int getTotal(PatientBlackList patientBlack) {
		String hql = joint(patientBlack);
		return super.getTotal(hql);
	}
	@Override
	public List<PatientBlackList> getPage(String page, String rows, PatientBlackList patientBlack) {
		String hql = joint(patientBlack);
		return super.getPage(hql, page, rows);
	}
	public String joint(PatientBlackList patientBlack){
		String hql="FROM PatientBlackList p WHERE p.del_flg = 0  and p.stop_flg=0 ";
		if(patientBlack!=null){
			if(org.apache.commons.lang3.StringUtils.isNotEmpty(patientBlack.getMedicalrecordId())){
				String queryName = patientBlack.getMedicalrecordId();
				hql = hql +" and (p.medicalrecordId LIKE '%"+queryName+"%'"
				 			+ " OR p.patient.patientName LIKE '%"+queryName+"%')";
			}
			if(org.apache.commons.lang3.StringUtils.isNotEmpty(patientBlack.getBlacklistType())){
				hql = hql +" and p.blacklistType='"+patientBlack.getBlacklistType()+"'";
			}
		}
		return hql;
	}
	
} 
