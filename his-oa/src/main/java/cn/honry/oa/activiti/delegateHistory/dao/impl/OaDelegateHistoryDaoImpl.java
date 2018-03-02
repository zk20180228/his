package cn.honry.oa.activiti.delegateHistory.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaDelegateHistory;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.delegateHistory.dao.OaDelegateHistoryDao;

/**
 * 代理记录DAO实现类
 * @author luyanshou
 *
 */
@Repository("oaDelegateHistoryDao")
@SuppressWarnings({ "all" })
public class OaDelegateHistoryDaoImpl extends
		HibernateEntityDao<OaDelegateHistory> implements OaDelegateHistoryDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
