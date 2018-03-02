package cn.honry.migrate.tableFiledType.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.TableFiledType;
import cn.honry.base.bean.model.TableFiledType;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.tableFiledType.dao.TableFieldDao;
@SuppressWarnings("all")
@Repository("tableFieldDao")
public class TableFieldDaoImpl extends HibernateEntityDao<TableFiledType> implements TableFieldDao {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<TableFiledType> queryTableField(String queryCode, String rows,
			String page, String menuAlias) {
		String hql="from TableFiledType t ";
		if(StringUtils.isNotBlank(queryCode)){
			queryCode=queryCode.toUpperCase();
			hql+=" where upper(t.code) like '%"+queryCode+"%' or upper(t.tableName) like '%"+queryCode+"%' or upper(t.fieldName) like '%"+queryCode+"%' or  upper(t.fieldType) like '%"+queryCode+"%' or upper(t.javaType) like '%"+queryCode+"%' ";
		}
		int page1=Integer.valueOf(page==null?"1":page);
		int row1=Integer.valueOf(rows==null?"20":rows);
		int frist=(page1-1)*row1;
		List<TableFiledType> list=this.getSession().createQuery(hql).setFirstResult(frist).setMaxResults(row1).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<TableFiledType>();
	}

	@Override
	public int queryTableFieldTotal(String queryCode, String menuAlias) {
		String hql="select to_char(count(1)) as code  from I_TABLE_FIELDTYPE t ";
		if(StringUtils.isNotBlank(queryCode)){
			queryCode=queryCode.toUpperCase();
			hql+=" where upper(t.CODE) like '%"+queryCode+"%' or upper(t.TABLE_NAME) like '%"+queryCode+"%' or upper(t.FIELD_NAME) like '%"+queryCode+"%' or upper(t.FIELD_TYPE) like '%"+queryCode+"%' or upper(t.JAVA_TYPE) like '%"+queryCode+"%' ";
		}
		List<TableFiledType> total=this.getSession().createSQLQuery(hql).addScalar("code")
				.setResultTransformer(Transformers.aliasToBean(TableFiledType.class)).list();
		if(total!=null&&total.size()>0){
			return Integer.parseInt(total.get(0).getCode());
		}
		return 0;
	}

	@Override
	public void delTableField(String id) {
		String sql="delete from I_TABLE_FIELDTYPE where id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public TableFiledType getOneDate(String id) {
		String hql="from TableFiledType t where t.id= '"+id+"'";
		List<TableFiledType> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new TableFiledType();
	}

//	@Override
//	public void updateState(String id, String state) throws Exception {
//		String sql="update i_data_synch set STATE= "+state+" where ID='"+id+"'";
//		this.getSession().createSQLQuery(sql).executeUpdate();
//	}
	
}
