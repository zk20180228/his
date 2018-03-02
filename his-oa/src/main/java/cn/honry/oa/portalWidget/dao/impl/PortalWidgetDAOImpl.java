package cn.honry.oa.portalWidget.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.oa.portalWidget.dao.PortalWidgetDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;

@Repository("portalWidgetDAO")
@SuppressWarnings({ "all" })
public class PortalWidgetDAOImpl extends HibernateEntityDao<OaPortalWidget> implements PortalWidgetDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public int getTotal(OaPortalWidget entity) {
		String hql=" from OaPortalWidget as bf where 1=1 ";
		hql= this.getWhereHql(entity,hql);
		int c = super.getTotal(hql);
		return c;
	}

	@Override
	public List query(OaPortalWidget entity,String page,String rows) {
		String hql=" from OaPortalWidget as bf where 1=1 ";
		hql= this.getWhereHql(entity,hql);
		List<OaPortalWidget> portalWidgetList=super.getPage(hql, page==null?"1":page, rows==null?"20":rows);
		return portalWidgetList;
	} 

	/**
	 * 拼接查询条件
	 * @author zpty
	 * @date 2017-7-20 14：:4
	 * @param queryName 查询条件,一个检索条件查询多项内容
	 * @param hql
	 * @return
	 */
	private String getWhereHql(OaPortalWidget entity ,String hql) {
		if(entity !=null){
			if(StringUtils.isNotBlank(entity.getName())){
				String queryName = "'%" + entity.getName().toUpperCase() + "%'";
				hql+=" and (upper(bf.name) like "+queryName
					+" or upper(bf.url) like "+queryName
					+" or upper(bf.id) like "+queryName
					+")";
			}
		}
		hql+=" order by bf.id ";
		return hql;
	}

	@Override
	public void deleteLogicById(String id) {
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		String sql="update T_OA_USER_PORTAL set GLOBAL_STATUS=1 where "
				+ "WIDGET_ID = '"+id+"' and "+DataRightUtils.connectHQLSentence(null)+"";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	@Override
	public OaPortalWidget getById(String id) {
			String hql=" from OaPortalWidget  where id='"+id+"'";
		return (OaPortalWidget) super.find(hql).get(0);
	}
	@Override
	public String getMaxId() {
		//id中虽然存的是数字,但是是varchar类型的,所以要先转成int,然后比较最大,否则容易出错
		String hql = "select to_char(Max(to_number(t.id))) as id from T_OA_PORTAL_WIDGET t";
		List<OaPortalWidget> list=super.getSession().createSQLQuery(hql).addScalar("id").setResultTransformer(Transformers.aliasToBean(OaPortalWidget.class)).list();
		if(list!=null && list.size()>0 && StringUtils.isNotBlank(list.get(0).getId())){
			return list.get(0).getId();
		}
		return "0";
	}
	@Override
	public void updateStatus(String id, Integer status) {
		String sql="update T_OA_USER_PORTAL set GLOBAL_STATUS="+status+" where "
				+ "WIDGET_ID = '"+id+"' and "+DataRightUtils.connectHQLSentence(null)+"";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
}
