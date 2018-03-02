package cn.honry.inner.material.adjustpricedetail.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MatStockInfo;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.base.bean.model.TMatAdjustpricedetail;
import cn.honry.base.bean.model.TMatAdjustpricehead;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.material.adjustpricedetail.dao.AdjustpricedetailInInterDAO;

/**  
 *  
 * @className：AdjustpricedetailInInterDAOImpl
 * @Description：   物资调价
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("adjustpricedetailInInterDAO")
public class AdjustpricedetailInInterDAOImpl extends HibernateEntityDao<TMatAdjustpricedetail> implements AdjustpricedetailInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<MatStockdetail> matisaAmple(String storageCode, String itemCode) {
		String hql="from MatStockdetail t where t.stop_flg='0' and t.del_flg='0' and itemCode='"+itemCode+"' order by stockCode";
		List<MatStockdetail> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MatStockdetail>();
	}
	
	@Override
	public void saveOrupdate(MatStockdetail model) {
		this.getSession().saveOrUpdate(model);
	}
	
	@Override
	public List<MatStockInfo> queryStockinfo(String itemCode) {
		String hql="from MatStockInfo t where t.stop_flg='0' and t.del_flg='0' and t.itemCode='"+itemCode+"'";
		List<MatStockInfo> list= super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MatStockInfo>();
	}
	
	@Override
	public List<TMatAdjustpricehead> queryForAdjustListCode(String adjustListCode) {
		String hql="from TMatAdjustpricehead t where t.stop_flg='0' and t.del_flg='0' and t.adjustListCode='"+adjustListCode+"'";
		List<TMatAdjustpricehead> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<TMatAdjustpricehead>();
	}
	
}
