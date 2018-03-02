package cn.honry.oa.itemManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.TmItems;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.itemManage.dao.ItemManageDao;
@Repository("itemManageDao")
public class ItemManageDaoImpl extends HibernateEntityDao<TmItems> implements ItemManageDao {
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<TmItems> getAllType() {
		String sql =  " select t.type type,t.typename typeName from tm_all_items t where t.del_flg = 0 and t.isparent = 1 group by t.type,t.typename order by type";
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sql).addScalar("type",Hibernate.INTEGER).addScalar("typeName");
		List<TmItems> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(TmItems.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<TmItems>();
	}

	@Override
	public List<TmItems> getItemsByType(Integer type) {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.id id, t.code ,t.name, t.type type,t.typename typeName , t.createtime createTime, ");
		sb.append(" t.updatetime updateTime, t.del_flg, t.stop_flg from tm_all_items t ");
		sb.append(" where t.del_flg = 0 and t.stop_flg = 0 and t.isparent = 0 ");
		if (type != 0) {
			sb.append(" and t.type =" + type);
		}
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString()).addScalar("type",Hibernate.INTEGER).addScalar("typeName").addScalar("id")
				.addScalar("code").addScalar("name").addScalar("createTime",Hibernate.DATE).addScalar("updateTime",Hibernate.DATE).addScalar("del_flg",Hibernate.INTEGER)
				.addScalar("stop_flg",Hibernate.INTEGER);
		List<TmItems> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(TmItems.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<TmItems>();
	}

	@Override
	public List<TmItems> getItemsById(String id) {
		String hql="From TmItems d where d.del_flg=0 and d.stop_flg=0   ";
		if(StringUtils.isNotBlank(id)){
			hql += " and d.id ='"+id+"' ";
		}
		List<TmItems> items=super.find(hql, null);
		if(items!=null&&items.size()>0){
			return items;
		}
		return new ArrayList<TmItems>();
	}

	@Override
	public String getMaxCodeByType(Integer type) {
		String sql = "select max(to_number(t.code)) as code from tm_all_items t where t.type= "+type;
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sql).addScalar("code",Hibernate.STRING);
		List<TmItems> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(TmItems.class)).list();
		if(list != null && list.size() >0){
			return list.get(0).getCode();
		}else {
			return null;
		}
	}

	@Override
	public Integer getMaxType(Integer type) {
		String sql = "select max(t.type) as type from tm_all_items t ";
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sql).addScalar("type",Hibernate.INTEGER);
		List<TmItems> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(TmItems.class)).list();
		if(list != null && list.size() >0){
			return list.get(0).getType();
		}else {
			return 0;
		}
	}

	@Override
	public TmItems getTypeName(Integer type) {
		String sql =  " select t.type type,t.typename typeName from tm_all_items t where t.del_flg = 0 and t.isparent = 1 ";
		sql += " and t.type =" + type +" group by t.type,t.typename ";
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sql).addScalar("type",Hibernate.INTEGER).addScalar("typeName");
		List<TmItems> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(TmItems.class)).list();
			return list.get(0);
	}

	@Override
	public void delItem(String id) {
		String sql = " update tm_all_items t set t.del_flg = 1 where t.id = "+"'"+id+"'";
		SQLQuery query = super.getSession().createSQLQuery(sql);
		query.executeUpdate(); 
	}

	@Override
	public void delType(Integer type,String parentId,String id) {
		String sql = "";
		if("ROOT".equals(parentId)){
			sql = " update tm_all_items t set t.del_flg = 1 where t.type = "+ type;
		}else{
			sql = " update tm_all_items t set t.del_flg = 1 where t.type = '"+type+"' and (t.parentid = '"+id+"' or t.id = '"+id+"')";
		}
		SQLQuery query = super.getSession().createSQLQuery(sql);
		query.executeUpdate(); 
		
	}

	@Override
	public List<TmItems> queryItems(String isParent,String type,String parentId) {
		String hql="From TmItems d where d.del_flg=0 and d.stop_flg=0 and d.isParent ='"+isParent+"'  ";
		if(StringUtils.isNotBlank(type)){
			hql += " and d.type ='"+type+"' ";
		}
		if(StringUtils.isNotBlank(parentId)){
			hql += " and d.parentId ='"+parentId+"' ";
		}
		hql += " order by d.type ";
		List<TmItems> items=super.find(hql, null);
		if(items!=null&&items.size()>0){
			return items;
		}
		return new ArrayList<TmItems>();
	}

	@Override
	public List<TmItems> queryTmItems(String id, String parentId,String path) {
		String hql= "";
		if("ROOT".equals(id)){
			hql="From TmItems d where d.del_flg=0 and d.stop_flg=0 and d.path is not null ";
		}else{
			if("".equals(path)||path==null){
				hql="From TmItems d where d.del_flg=0 and d.stop_flg=0 and d.parentId ='"+id+"' ";
			}else{
				hql="From TmItems d where d.del_flg=0 and d.stop_flg=0 and d.id ='"+id+"' ";
			}
		}
		hql += " order by d.code ";
		List<TmItems> items=super.find(hql, null);
		if(items!=null&&items.size()>0){
			return items;
		}
		return new ArrayList<TmItems>();
	}
}
