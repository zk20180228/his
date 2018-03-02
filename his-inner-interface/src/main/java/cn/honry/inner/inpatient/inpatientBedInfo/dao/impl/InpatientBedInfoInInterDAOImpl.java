package cn.honry.inner.inpatient.inpatientBedInfo.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.inpatientBedInfo.dao.InpatientBedInfoInInterDAO;

@Repository("inpatientBedInfoInInterDAO")
@SuppressWarnings({"all"})
public class InpatientBedInfoInInterDAOImpl extends HibernateEntityDao<InpatientBedinfo> implements InpatientBedInfoInInterDAO{
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
		String hql = "from InpatientBedinfoNow i where i.del_flg = 0 and i.id= :id ";
		List<InpatientBedinfoNow> list  = this.getSession().createQuery(hql).setParameter("id", id).list();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		return new InpatientBedinfoNow();
	}
	
	@Override
	public InpatientInfo queryByMedical(String medicalNo) {
		String hql="FROM InpatientInfo i WHERE i.medicalrecordId = '"+ medicalNo +"' and i.del_flg = 0 ";
		List<InpatientInfo> list = this.getSession().createQuery(hql).list();
		return list.size()==0 ? null : list.get(0);
	}

}
