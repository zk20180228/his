package cn.honry.statistics.bi.statisticalSetting.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiDimensionSet;
import cn.honry.base.bean.model.BiIndexSet;
import cn.honry.base.bean.model.BiStatSet;
import cn.honry.base.bean.model.BiSubsectionSet;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.statisticalSetting.dao.StatisticalSettingDAO;
import cn.honry.statistics.bi.statisticalSetting.vo.VoshowList;
import cn.honry.statistics.bi.statisticalSetting.vo.VtableName;

@Repository("statisticalSettingDAO")
@SuppressWarnings({"all"})
public class StatisticalSettingDAOImpl extends HibernateEntityDao<BiStatSet> implements StatisticalSettingDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<BiStatSet> queryBiStatSetList(BiStatSet BiStatSet, String page,
			String rows) {
		String hql=BiStatSetHql(BiStatSet);
	    int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Query query = this.getSession().createQuery(hql);
	    List<BiStatSet> list=query.setFirstResult((start - 1) * count).setMaxResults(count).list();
	    if(list!=null&&list.size()>0){
			 return list;
		 }
		return new ArrayList<BiStatSet>();
	}

	@Override
	public int queryBiStatSetTotal(BiStatSet BiStatSet) {
		String hql=BiStatSetHql(BiStatSet);
		return super.getTotal(hql);
	}
	private String BiStatSetHql(BiStatSet BiStatSet){
		String hql="from BiStatSet c where c.del_flg=0 and c.stop_flg=0 ";
		if(StringUtils.isNotBlank(BiStatSet.getSetStatname())){
			hql+= "and c.setStatname like '%"+BiStatSet.getSetStatname()+"%'";
		}
		hql+= "  order by c.setGroupid";
		return hql;
	}

	@Override
	public List<VtableName> querytablename() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select table_name as tableName from user_tab_comments order by table_name");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("tableName");
		List<VtableName> List = queryObject.setResultTransformer(Transformers.aliasToBean(VtableName.class)).list();
		if(List!=null&&List.size()>0){
			return List;
		}
		return new ArrayList<VtableName>();
	}

	@Override
	public List<VtableName> queryColumnname(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select column_name as columnName from user_tab_cols where table_name='"+tableName+"'");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("columnName");
		List<VtableName> List = queryObject.setResultTransformer(Transformers.aliasToBean(VtableName.class)).list();
		if(List!=null&&List.size()>0){
			return List;
		}
		return new ArrayList<VtableName>();
	}

	@Override
	public List<BiDimensionSet> queryBiDimensionSet(String dimensionNumber) {
		String hql = "from BiDimensionSet c where c.stop_flg=0 and c.del_flg=0 ";
		if(StringUtils.isNotBlank(dimensionNumber)){
			hql+="and c.dimensionNumber='"+dimensionNumber+"'";
		}
		List<BiDimensionSet> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BiDimensionSet>();
	}

	@Override
	public List<BiSubsectionSet> queryBiSubsectionSet(String dimensionNumber) {
		String hql = "from BiSubsectionSet c where c.stop_flg=0 and c.del_flg=0 and c.dimensionNumber='"+dimensionNumber+"' order by c.sort desc";
		List<BiSubsectionSet> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<BiIndexSet> queryBiIndexSet(String dimensionNumber) {
		String hql = "from BiIndexSet c where c.stop_flg=0 and c.del_flg=0 and c.dimensionNumber='"+dimensionNumber+"' order by c.sort desc";
		List<BiIndexSet> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public void createTable(String field,List<String> list,String polymerization) {
		String kong = null;
		String sql = "CREATE TABLE BI_DIMENSION_"+field+"(";
			for (int i = 0; i < list.size(); i++) {
				if(kong==null){
					sql+= ""+list.get(i)+"  VARCHAR2(500)";
					kong="不为null";
				}else{
					sql+= ","+list.get(i)+"  VARCHAR2(500)";
				}
			} 
			sql+= ") "
			+ "tablespace his_statis pctfree 10 "
			+ "initrans 1 maxtrans 255 storage( initial 64K next 1M minextents 1 maxextents unlimited)";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
   
	@Override
	public List<BiIndexSet> quertIndexFiled(String dimensionNumber,
			String groupId) {
		List<BiIndexSet> list = new ArrayList<BiIndexSet>();
		List<BiIndexSet> list1 = new ArrayList<BiIndexSet>();
		String dimensionNumber1 = dimensionNumber.replaceAll("_", "");
		String dimensionNumber2[] = dimensionNumber1.split(",");
		for (int i = 0; i < dimensionNumber2.length; i++) {
			String hql="from BiIndexSet b where (b.dimensionNumber = '"+dimensionNumber2[i]+"') and b.setGroupid='"+groupId+"'";
			list=super.find(hql, null);
			list1.addAll(list);
		}
		if(list1!=null&&list1.size()>0){
			return list1;
		}
		return null;
	}
	@Override
	public List<BiSubsectionSet> quertSubsectionFiled(String dimensionNumber,
			String groupId) {
		List<BiSubsectionSet> list = new ArrayList<BiSubsectionSet>();
		List<BiSubsectionSet> list1 = new ArrayList<BiSubsectionSet>();
		String dimensionNumber1 = dimensionNumber.replaceAll("_", "");
		String dimensionNumber2[] = dimensionNumber1.split(",");
		for (int i = 0; i < dimensionNumber2.length; i++) {
			String hql="from BiSubsectionSet bi where bi.dimensionNumber = '"+dimensionNumber2[i]+"' and bi.setGroupid='"+groupId+"'";
			list=super.find(hql, null);
			list1.addAll(list);
		}
		if(list1!=null&&list1.size()>0){
			return list1;
		}
		return null;
	}
	/**
	 * 
	 */

	@Override
	public List<VtableName> queryViewColumnName(String viewName) {
		String sql = "SELECT aa.column_name as columnName,aa.data_type as columnType FROM user_tab_cols aa WHERE aa.table_name ='"+viewName+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString())
				.addScalar("columnName").addScalar("columnType");
		List<VtableName> List = queryObject.setResultTransformer(Transformers.aliasToBean(VtableName.class)).list();
		if(List!=null&&List.size()>0){
			return List;
		}
		return new ArrayList<VtableName>();
	}

	@Override
	public List<Object> queryObject() {
		String sql="select * from v_dynamics_list";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Object> list= session.createSQLQuery(sql).list();
		return list;
	}

	@Override
	public List<VoshowList> queryListShowList() {
		StringBuffer sql =new StringBuffer();
		String date1 = (char)34 + "2014" + (char)34;
		String date2 = (char)34 + "2015" + (char)34;
		String date3 = (char)34 + "2016" + (char)34;
		sql.append("select v.NAME as NAME,v."+date1+" as date1,v."+date2+" as date2,v."+date3+" as date3,v.COUNT1 as COUNT1,"
				+ "v.DEPTCODE as DEPTCODE,v.COUNT2 as COUNT2 from v_dynamics_list v");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString())
				.addScalar("NAME").addScalar("date1",Hibernate.INTEGER).addScalar("date2",Hibernate.INTEGER)
				.addScalar("date3",Hibernate.INTEGER).addScalar("COUNT1",Hibernate.INTEGER)
				.addScalar("DEPTCODE").addScalar("COUNT2",Hibernate.INTEGER);
		List<VoshowList> List = queryObject.setResultTransformer(Transformers.aliasToBean(VoshowList.class)).list();
		if(List!=null&&List.size()>0){
			return List;
		}
		return null;
	}
}
