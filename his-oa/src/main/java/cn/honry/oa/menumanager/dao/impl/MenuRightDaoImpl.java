package cn.honry.oa.menumanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.menumanager.dao.MenuRightDao;
import cn.honry.oa.menumanager.vo.MenuVo;

@Repository("menuRightDao")
public class MenuRightDaoImpl extends HibernateEntityDao<OaMenuRight> implements MenuRightDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OaMenuRight> findAllByMenuid(String code) {
		StringBuffer sb = new StringBuffer();
		sb.append("select t.id as id, t.RIGHT_RIGHTTYPE as rightType, t.RIGHT_TYPE as right,t.RIGHT_CODE as code, t.CODENAME as codeName from ");
		sb.append(" t_oa_menuright t where t.menu_id = ").append("'"+code+"'");
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("id").addScalar("rightType").addScalar("right").addScalar("code").addScalar("codeName");
		List<OaMenuRight> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(OaMenuRight.class)).list();
		if(list.size() > 0 && list != null){
			return list;
		}
		return new ArrayList<OaMenuRight>();
	}

}
