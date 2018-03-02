package cn.honry.inner.outpatient.feedetail.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.feedetail.dao.FeedetailTecIntDAO;

/**  
 *  
 * 门诊收费
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("feedetailTecIntDAO")
public class FeedetailTecIntDAOmpl extends HibernateEntityDao<OutpatientFeedetailNow> implements FeedetailTecIntDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
