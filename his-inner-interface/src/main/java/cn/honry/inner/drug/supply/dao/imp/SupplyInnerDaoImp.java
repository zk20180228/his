package cn.honry.inner.drug.supply.dao.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.supply.dao.SupplyInnerDao;

@Repository("SupplyInnerDao")
@SuppressWarnings({ "all" })
public class SupplyInnerDaoImp extends HibernateEntityDao<DrugSupplycompany> implements SupplyInnerDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public Map<String, Object> findAllMap() {
		String  sql = "select COMPANY_ID as id,COMPANY_NAME as companyName from T_DRUG_SUPPLYCOMPANY where DEL_FLG=0 and STOP_FLG=0";
		List<DrugSupplycompany> list = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("companyName").setResultTransformer(Transformers.aliasToBean(DrugSupplycompany.class)).list();
		Map<String, Object> map = new HashMap<String, Object>();
		for (DrugSupplycompany drugSupplycompany : list) {
			map.put(drugSupplycompany.getId(), drugSupplycompany.getCompanyName());
		}
		return map;
	}
}
