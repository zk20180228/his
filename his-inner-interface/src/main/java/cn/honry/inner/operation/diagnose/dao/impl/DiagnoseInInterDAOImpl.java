package cn.honry.inner.operation.diagnose.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OperationDiagnose;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.diagnose.dao.DiagnoseInInterDAO;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("diagnoseInInterDAO")
public class DiagnoseInInterDAOImpl extends HibernateEntityDao<OperationDiagnose> implements DiagnoseInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
