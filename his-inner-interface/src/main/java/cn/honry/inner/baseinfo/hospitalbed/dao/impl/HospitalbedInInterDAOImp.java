package cn.honry.inner.baseinfo.hospitalbed.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;

@Repository("hospitalbedInInterDAO")
@SuppressWarnings({ "all" })
public class HospitalbedInInterDAOImp extends HibernateEntityDao<BusinessHospitalbed> implements HospitalbedInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	
	/**
	 * 根据id获取对象
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param id
	 * @return
	 */
	@Override
	public BusinessHospitalbed get(String id) {
		String hql=" from BusinessHospitalbed b where b.id= ? ";
		List<BusinessHospitalbed> list=super.find(hql, id);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new BusinessHospitalbed();
		/*StringBuilder sql = new StringBuilder();
		sql.append("select * from t_business_hospitalbed ");
		sql.append("where bed_id = '"+id+"'");
		
		return  (BusinessHospitalbed) this.getSession().createSQLQuery(sql.toString()).addEntity(BusinessHospitalbed.class).uniqueResult();*/
	}
	
	
	/**
	 * 保存与更新
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BusinessHospitalbed entity) {
		if(entity.getId() != "" && null != entity.getId()){
			this.getSession().update(entity);
		}else{
			this.getSession().save(entity);
		}
	}
	
}
