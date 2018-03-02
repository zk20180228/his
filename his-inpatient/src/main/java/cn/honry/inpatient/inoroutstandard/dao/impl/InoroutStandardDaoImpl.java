package cn.honry.inpatient.inoroutstandard.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
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

import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.inoroutstandard.dao.InoroutStandardDao;
import cn.honry.inpatient.inoroutstandard.vo.StandardVO;
@Repository("inoroutStandardDao")
@SuppressWarnings({"all"})
public class InoroutStandardDaoImpl extends HibernateEntityDao<InoroutStandard> implements
		InoroutStandardDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InoroutStandard> getStandardList(String code) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from InoroutStandard where standCode = ? and del_flg=0 ");
		List<InoroutStandard> list = super.find(sb.toString(), code);
		if(list!= null && list.size()>0){
			return list;
		}
		return new ArrayList<InoroutStandard>();
	}

	@Override
	public List<StandardVO> getAllStandard() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Select Distinct t.Stand_Code As standCode,t.Stand_Name As StandName From t_Inorout_Standard t Where t.Del_Flg = 0");
		List<StandardVO> list = this.createHibernateTemplate(getSessionFactory()).execute(new HibernateCallback<List<StandardVO>>() {

			@Override
			public List<StandardVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.addScalar("standCode").addScalar("StandName");
				List<StandardVO> list = query.setResultTransformer(Transformers.aliasToBean(StandardVO.class)).list();
				return list;
			}
		});
		if(list!=null &&list.size()>0){
			return list;
		}
		return new ArrayList<StandardVO>();
	}


}
