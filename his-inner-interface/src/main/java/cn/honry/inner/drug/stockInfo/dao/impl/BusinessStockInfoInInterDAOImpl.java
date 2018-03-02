package cn.honry.inner.drug.stockInfo.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugCheckdetail;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.stockInfo.dao.BusinessStockInfoInInterDAO;

/**
 * @author abc
 *
 */
@Repository("businessStockInfoInInterDAO")
@SuppressWarnings({ "all" })
public class BusinessStockInfoInInterDAOImpl extends HibernateEntityDao<DrugStockinfo> implements BusinessStockInfoInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
	 /** 根据科室和药品得到该药品的可用数量（库存数量减预扣库存数量）
		 * @Description: 根据科室和药品得到该药品的可用数量（库存数量减预扣库存数量）
		 * @param storageDeptId 部门
		 * @param drugId 药品
		 * @Author: dutianliang
		 * @CreateDate: 2016年4月18日
		 * @Version: V 1.0
		 */
	@Override
	public Double getDrugStockInfoStoreNum(String storageDeptId,
			String drugId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select (nvl(t.store_sum, 0) - nvl(t.preout_sum, 0)) as storeNum "
				+ "from t_drug_stockinfo t where t.storage_deptid = :storageDeptId and t.drug_id = :drugId ");
		List list = this.getSession().createSQLQuery(sql.toString()).addScalar("storeNum",Hibernate.DOUBLE)
				.setParameter("storageDeptId", storageDeptId).setParameter("drugId", drugId).list();
		if(list != null && list.size() > 0){
			return (Double) list.get(0);
		}
		return 0.0;
	}

	@Override
	public DrugInfo findInfoById(String drugId) {
		String hql="from DrugInfo where code= '"+drugId+"'";
		List<DrugInfo> list = (List<DrugInfo>) getHibernateTemplate().find(hql);
		return list.size() != 0 ? list.get(0) : null;
	}
	@Override
	public List<DrugCheckdetail> getDrugChecks(String drugCode, String deptCode) {
		StringBuffer hql = new StringBuffer("from DrugCheckdetail where checkState = 0 and del_flg =0 and drugCode= '"+drugCode+"'");
		hql.append("and drugDeptCode in ('0','" + deptCode + "')");
		List<DrugCheckdetail> list = (List<DrugCheckdetail>) getHibernateTemplate().find(hql.toString());
		return list;
	}

	@Override
	public DrugStockinfo getStockInfo(String deptId, String drugId) {
		String hql = "from DrugStockinfo s where s.del_flg = 0 and s.stop_flg = 0 and s.storageDeptid = ? and s.drugId = ?";
		List<DrugStockinfo> list = super.find(hql, deptId, drugId);
		return list.size() != 0 ? list.get(0) : null; 
	}
	
}
