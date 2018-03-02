package cn.honry.migrate.synDateManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.IDataSynch;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.synDateManage.dao.SynDateDao;
@SuppressWarnings("all")
@Repository("synDateDao")
public class SynDateDaoImpl extends HibernateEntityDao<IDataSynch> implements SynDateDao {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<IDataSynch> querySynDate(String queryCode, String rows,
			String page, String menuAlias,String dateState) {
		String hql="from IDataSynch t where 1=1 ";
		if(StringUtils.isNotBlank(queryCode)){
			queryCode=queryCode.toUpperCase();
			hql+=" and  (upper(t.code) like '%"+queryCode+"%' or upper(t.tableName) like '%"+queryCode+"%' or upper(t.tableZhName) like '%"+queryCode+"%' or  upper(t.viewName) like '%"+queryCode+"%' or upper(t.viewZhName) like '%"+queryCode+"%' or upper(t.tableFromUser) like '%"+queryCode+"%') ";
		}
		if(StringUtils.isNotBlank(dateState)){
			hql+=" and t.state= "+dateState;
		}
		hql+=" order by to_number(t.code) ";
		int page1=Integer.valueOf(page==null?"1":page);
		int row1=Integer.valueOf(rows==null?"20":rows);
		int frist=(page1-1)*row1;
		List<IDataSynch> list=this.getSession().createQuery(hql).setFirstResult(frist).setMaxResults(row1).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<IDataSynch>();
	}

	@Override
	public int querySynDateTotal(String queryCode, String menuAlias,String dateState) {
		String hql="select count(1) as viewOrder  from i_data_synch t where 1=1 ";
		if(StringUtils.isNotBlank(queryCode)){
			queryCode=queryCode.toUpperCase();
			hql+=" and  (upper(t.code) like '%"+queryCode+"%' or upper(t.TABLE_NAME) like '%"+queryCode+"%' or upper(t.TABLE_ZHNAME) like '%"+queryCode+"%' or upper(t.VIEW_NAME) like '%"+queryCode+"%' or upper(t.VIEW_ZHNAME) like '%"+queryCode+"%' or upper(t.tableFromUser) like '%"+queryCode+"%') ";
		}
		if(StringUtils.isNotBlank(dateState)){
			hql+=" and t.state= "+dateState;
		}
		List<IDataSynch> total=this.getSession().createSQLQuery(hql).addScalar("viewOrder",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(IDataSynch.class)).list();
		if(total!=null&&total.size()>0){
			return total.get(0).getViewOrder();
		}
		return 0;
	}

	@Override
	public void delSynDate(String id) {
		String sql="delete from i_data_synch where id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public IDataSynch getOneDate(String id) {
		String hql="from IDataSynch t where t.id= '"+id+"'";
		List<IDataSynch> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new IDataSynch();
	}

	@Override
	public void updateState(String id, String state) throws Exception {
		String sql="update i_data_synch set STATE= "+state+" where ID='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public List<ServiceManagement> getServiceCode(String code) {
		String hql="select code,name from ServiceManagement ";
		if(StringUtils.isNoneBlank(code)){
			hql +="where code like '%"+code+"%'";
		}
		return null;
	}

	@Override
	public int maxCode(String tableName, String field) {
		String sql="select nvl(max(to_number(t."+field+"))+1,1) viewOrder from "+tableName+" t ";
		List<IDataSynch> total=this.getSession().createSQLQuery(sql).addScalar("viewOrder",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(IDataSynch.class)).list();
		if(total.size()>0){
			return total.get(0).getViewOrder();
		}
		return 0;
	}
	
	
}
