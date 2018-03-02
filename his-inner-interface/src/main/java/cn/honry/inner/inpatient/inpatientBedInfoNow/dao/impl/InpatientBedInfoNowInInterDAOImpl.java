package cn.honry.inner.inpatient.inpatientBedInfoNow.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.inpatientBedInfo.dao.InpatientBedInfoInInterDAO;
import cn.honry.inner.inpatient.inpatientBedInfoNow.dao.InpatientBedInfoNowInInterDAO;

@Repository("inpatientBedInfoNowInInterDAO")
@SuppressWarnings({"all"})
public class InpatientBedInfoNowInInterDAOImpl extends HibernateEntityDao<InpatientBedinfoNow> implements InpatientBedInfoNowInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * @Description:通过主键ID获取InpatientBedinfo
	 * @Author：  TCJ
	 * @CreateDate： 2016-1-6
	 * @return InpatientBedinfo  
	 * @version 1.0
	**/
	@Override
	public InpatientBedinfoNow queryBedInfoByMainID(String id) {
		String hql = "from InpatientBedinfoNow i where i.del_flg = 0 and i.id='"+id+"'";
		List<InpatientBedinfoNow> list  = this.getSession().createQuery(hql).list();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		return new InpatientBedinfoNow();
	}
	
	@Override
	public InpatientInfoNow queryByMedical(String medicalNo) {
		String hql="FROM InpatientInfoNow i WHERE i.medicalrecordId = '"+ medicalNo +"' ";
		List<InpatientInfoNow> list = this.getSession().createQuery(hql).list();
		return list.size()==0 ? null : list.get(0);
	}

}
