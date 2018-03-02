package cn.honry.inner.outpatient.preregister.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.preregister.dao.PreregisterInInterDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("preregisterInInterDAO")
@SuppressWarnings({ "all" })

public class PreregisterInInterDaoImpl extends HibernateEntityDao<RegisterPreregister> implements PreregisterInInterDao{
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<RegisterPreregister> findRegisterPreregister() {
		String hql="from RegisterPreregister where status=1 and  to_char(preregisterDate,'yyyy-MM-dd') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'";
		List<RegisterPreregister> preregisterList = super.find(hql, null);
		if(preregisterList!=null||preregisterList.size()>0){
			return preregisterList;
		}else{
			return new ArrayList<RegisterPreregister>();
		}
		
	}

	@Override
	public void saveRegisterPreregister(RegisterPreregister registerPreregister) {
		super.update(registerPreregister);
	}

	@Override
	public List<RegisterPreregister> queryMissTime(String idcardId) {
		String hql="from RegisterPreregister where status=4 and idcardId='"+idcardId+"'";
		List<RegisterPreregister> preregisterList = super.find(hql, null);
		if(preregisterList!=null||preregisterList.size()>0){
			return preregisterList;
		}else{
			return new ArrayList<RegisterPreregister>();
		}
	}

	@Override
	public HospitalParameter queryTime() {
		String hql="from HospitalParameter where parameterCode='validPeriod' and id='402880c057fa6b590157fa996e440004'";
		List<HospitalParameter> hospitalParList = super.find(hql, null);
		if(hospitalParList.size()<=0){
			new HospitalParameter();
		}
		return hospitalParList.get(0);
		
	}

	
}
