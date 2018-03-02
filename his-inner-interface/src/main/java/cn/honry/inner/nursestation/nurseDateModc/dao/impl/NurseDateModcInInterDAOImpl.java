package cn.honry.inner.nursestation.nurseDateModc.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DeptDateModc;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.nursestation.nurseDateModc.dao.NurseDateModcInInterDAO;


@Repository("nurseDateModcInInterDAO")
@SuppressWarnings({ "all" })
public class NurseDateModcInInterDAOImpl extends HibernateEntityDao<DeptDateModc> implements NurseDateModcInInterDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 根据部门id查询医嘱分解时间设置
	 * @author  lyy
	 * @createDate： 2015年12月26日 上午10:15:40 
	 * @modifier lyy
	 * @modifyDate：2015年12月26日 上午10:15:40  
	 * @modifyRmk：  
	 * @param deptId 获得登录部门 
	 * @version 1.0
	 */
	@Override
	public DeptDateModc findNurseDateModcByDeptId(String deptId) {
		String hql = "from DeptDateModc where deptId=? ";
		DeptDateModc deptDateModc = (DeptDateModc) this.getSession().createQuery(hql).setParameter(0, deptId).uniqueResult();
		return deptDateModc;
	}

}
