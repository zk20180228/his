package cn.honry.inpatient.bill.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.bill.dao.OfficinaTreeDAO;
@Repository("officinaTreeDAO")
@SuppressWarnings({ "all" })
public class OfficinaTreeDAOImpl extends HibernateEntityDao<SysDepartment>  implements OfficinaTreeDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	  *  
	 * @Description：药房药库信息
	 * @Author：dh
	 * @CreateDate：2015-12-24  
	 * @Modifier：dh
	 * @ModifyDate：2015-12-24 下午03:26:39  
	 * @version 1.0
	 * 
	 */
	@Override
	public List<SysDepartment> findTreeType(int flag) {
		String hql="from SysDepartment where deptType  ='P' and stop_flg=0 and del_flg=0";
		List<SysDepartment> list = this.getSession().createQuery(hql).list();
		return list.size() != 0 ? list : null;
	}
	
	
}
