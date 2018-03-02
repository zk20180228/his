package cn.honry.inner.drug.Billclass.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.Billclass.dao.BillclassInInterDAO;

/**  
 *  
 * @className：BillclassInInterDAOImpl
 * @Description：  摆药
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("billclassInInterDAO")
public class BillclassInInterDAOImpl extends HibernateEntityDao<DrugBillclass> implements BillclassInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
