package cn.honry.inner.mobile.dbFile.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MContactDBVersion;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.mobile.dbFile.dao.ContactDBFileDao;

@Repository("contactDBFileDao")
@SuppressWarnings({ "all" })
public class ContactDBFileDaoImpl extends HibernateEntityDao<MContactDBVersion> implements ContactDBFileDao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public MContactDBVersion selectNewestVersion() throws Exception {
		final StringBuffer sql = new StringBuffer("SELECT version_id id, db_adress dbAdress,createtime createTime "); 
		sql.append("from m_contactdb_version where version_id=(select max(version_id) from m_contactdb_version)"); 
		List<MContactDBVersion> list = (List<MContactDBVersion>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString())
						.addScalar("id",Hibernate.INTEGER)
						.addScalar("dbAdress")
						.addScalar("createTime",Hibernate.DATE);
				return queryObject.setResultTransformer(Transformers.aliasToBean(MContactDBVersion.class)).list();
			}
		});
		if(list != null && list.size() != 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateNullDBAdress(MContactDBVersion mcontactdbversion)
			throws Exception {
		String hqlString = "update MContactDBVersion set dbAdress= ? where id = (select max(id) from MContactDBVersion)";
		super.excUpdateHql(hqlString, mcontactdbversion.getDbAdress());
	}
}
