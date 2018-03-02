package cn.honry.inner.system.moveDate.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.moveDate.dao.MoveDataLogDAO;

/**  
 *  
 * 内部接口：用户
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("moveDataLogDAO")
@SuppressWarnings({ "all" })
public class MoveDataLogDAOImpl extends HibernateEntityDao<MoveDataLog> implements MoveDataLogDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public MoveDataLog queryMoveDataLog(Integer optType, Integer dateType, String tableName, String dataDate) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM MoveDataLog  WHERE optType =:optType AND dateType =:dateType AND dataDate =:dataDate"
				+ " AND tableName =:tableName AND isSuccess = 2");
		Query query = super.createQuery(hql.toString()).setParameter("optType", optType).setParameter("dateType", dateType).
				setParameter("dataDate",dataDate).setParameter("tableName",tableName);
		MoveDataLog moveDataLog = (MoveDataLog) query.uniqueResult();
		return moveDataLog;
	}
	
}
