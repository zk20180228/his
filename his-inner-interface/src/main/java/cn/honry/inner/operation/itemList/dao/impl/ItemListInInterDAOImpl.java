package cn.honry.inner.operation.itemList.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.itemList.dao.ItemListInInterDAO;

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
@Repository("itemListInInterDAO")
public class ItemListInInterDAOImpl extends HibernateEntityDao<InpatientItemListNow> implements ItemListInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 *
	 * 通过处方号和处方流水号查询非药品明细
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月28日 下午9:05:03 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public InpatientItemList getitemListByNo(String recipeNo, Integer sequenceNo) {
		String hql = "from InpatientItemList i where i.recipeNo = ? and i.sequenceNo = ?";
		List<InpatientItemList> list = getSession().createQuery(hql).setString(0, recipeNo).setInteger(1, sequenceNo).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
