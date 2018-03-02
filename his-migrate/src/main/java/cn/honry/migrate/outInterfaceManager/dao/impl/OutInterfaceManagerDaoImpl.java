package cn.honry.migrate.outInterfaceManager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.ExterInter;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.outInterfaceManager.dao.OutInterfaceManagerDao;
@Repository("outInterfaceManagerDao")
public class OutInterfaceManagerDaoImpl extends HibernateEntityDao<ExterInter> implements OutInterfaceManagerDao {

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<ExterInter> findAll(String queryCode, String menuAlias, String page, String rows,String serviceState) throws Exception {
		String hql = "from ExterInter t where 1=1";
		if (StringUtils.isNotBlank(queryCode)) {
			hql+=" and (t.code like '%"+queryCode+"%' ";
			hql+=" or t.name like '%"+queryCode+"%' )";
		}
		if(StringUtils.isNotBlank(serviceState)){
			hql+=" and t.state="+serviceState;
		}
		hql +=" order by t.code";
		int page1=Integer.valueOf(page==null?"1":page);
		int row1=Integer.valueOf(rows==null?"20":rows);
		int frist=(page1-1)*row1;
		int end=page1*row1;
		
		List<ExterInter> list=this.getSession().createQuery(hql).setFirstResult(frist).setMaxResults(end).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<ExterInter>();
	}


	@Override
	public int getTotal(String queryCode, String menuAlias,String serviceState) throws Exception{
		String hql = "from ExterInter t where 1=1";
		if (StringUtils.isNotBlank(queryCode)) {
			hql+=" and (t.code like '%"+queryCode+"%' ";
			hql+=" or t.name like '%"+queryCode+"%' )";
		}
		if(StringUtils.isNotBlank(serviceState)){
			hql+=" and t.state="+serviceState;
		}
		return super.getTotal(hql);
	}

	@Override
	public ExterInter findById(String id) throws Exception {
		String hql="from ExterInter t where t.id= '"+id+"'";
		List<ExterInter> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new ExterInter();
	}

	@Override
	public void delInter(String id) throws Exception{
		String sql="delete from i_exter_inter where id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public ExterInter findByCode(String code) throws Exception {
		String hql="from ExterInter t where t.code= '"+code+"'";
		List<ExterInter> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateState(String id, String state) throws Exception {
		String sql="update i_exter_inter set STATE="+state+"  where id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer getMaxCode() {
		String hql = "select max(a.code) from i_exter_inter a ";
		String s = (String) this.getSession().createSQLQuery(hql).uniqueResult();
		Integer i = Integer.valueOf(s);
		if (i != null) {
			return i;
		}else {
			return 1;
		}
	}

	@Override
	public List<ExterInter> getfireCode(String fireCode) {
		String sql="select t.firmcode code,t.firmname name from i_firm_maintain t where 1=1 ";
		if(StringUtils.isNotBlank(fireCode)){
			sql+=" and t.firmCode like '%"+fireCode+"%' ";
		}
		List<ExterInter> list=this.getSession().createSQLQuery(sql).addScalar("code")
				.addScalar("name").setResultTransformer(Transformers.aliasToBean(ExterInter.class)).list();
		if(list.size()>0){
			return list;
		}
		return new ArrayList<ExterInter>();
	}

}
