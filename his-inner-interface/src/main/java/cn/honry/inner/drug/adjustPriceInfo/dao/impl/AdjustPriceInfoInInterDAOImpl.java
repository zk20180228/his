package cn.honry.inner.drug.adjustPriceInfo.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.adjustPriceInfo.dao.AdjustPriceInfoInInterDAO;

/**  
 *  
 * @className：AdjustPriceInfoInInterDAOImpl
 * @Description：  药品调价审核
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("adjustPriceInfoInInterDAO")
public class AdjustPriceInfoInInterDAOImpl extends HibernateEntityDao<DrugAdjustPriceInfo> implements AdjustPriceInfoInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 *  
	 * @Description：  药品调价方法-定时器执行
	 * @Author：zpty
	 * @CreateDate：2016-6-29
	 *
	 */
	@Override
	public List<DrugAdjustPriceInfo> getDrugChangeList() {
		String hql="FROM DrugAdjustPriceInfo d WHERE d.del_flg = 0 and d.stop_flg = 0 and d.currentState = 3 and to_char(d.inureTime,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') ";
		List<DrugAdjustPriceInfo> adjustPriceList=super.findByObjectProperty(hql, null);
		if(adjustPriceList!=null && adjustPriceList.size()>0){
			return adjustPriceList;
		}
		return null;
	}
}
