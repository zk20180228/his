package cn.honry.inner.drug.manufacturer.dao.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugManufacturer;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.manufacturer.dao.ManufacturerInnerDao;

@Repository("ManufacturerInnerDao")
@SuppressWarnings({ "all" })
public class ManufacturerInnerDaoImp extends HibernateEntityDao<DrugManufacturer> implements ManufacturerInnerDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public Map<String, Object> findAllMap() {
		String  sql = "select MANUFACTURER_ID as id,MANUFACTURER_NAME as manufacturerName from T_DRUG_MANUFACTURER where DEL_FLG=0 and STOP_FLG=0";
		List<DrugManufacturer> list = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("manufacturerName").setResultTransformer(Transformers.aliasToBean(DrugManufacturer.class)).list();
		Map<String, Object> map = new HashMap<String, Object>();
		for (DrugManufacturer drugManufacturer : list) {
			map.put(drugManufacturer.getId(), drugManufacturer.getManufacturerName());
		}
		return map;
	}
}
