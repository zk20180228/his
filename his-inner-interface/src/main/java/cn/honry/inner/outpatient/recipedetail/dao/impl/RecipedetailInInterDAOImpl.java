package cn.honry.inner.outpatient.recipedetail.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.recipedetail.dao.RecipedetailInInterDAO;

@Repository("recipedetailInInterDAO")
@SuppressWarnings({ "all" })
public class RecipedetailInInterDAOImpl extends HibernateEntityDao<OutpatientRecipedetail> implements RecipedetailInInterDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
