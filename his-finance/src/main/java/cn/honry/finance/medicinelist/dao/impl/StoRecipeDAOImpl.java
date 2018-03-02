package cn.honry.finance.medicinelist.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.medicinelist.dao.StoRecipeDAO;
@Repository("stoRecipeDAO")
@SuppressWarnings({ "all" })
public class StoRecipeDAOImpl extends HibernateEntityDao<StoRecipe> implements StoRecipeDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	
	@Override
	public StoRecipe getByParameter(String recipeNo) {
		String hql = "from StoRecipe s where s.del_flg = 0 and s.stop_flg = 0 and s.recipeNo = ? and s.classMeaningCode = 'M1' and s.validState != 0 and s.recipeState != 3";
		List<StoRecipe> list = this.createQuery(hql, recipeNo).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		
		return new StoRecipe();
	}

	@Override
	public List<StoRecipe> getByParameter(List<String> recipeNoList) {
		String strNo = "";
		for (String str : recipeNoList) {
			if(StringUtils.isNotEmpty(strNo)){
				strNo+=",";
			}
			strNo+=str;
		}
		strNo=strNo.replaceAll(",", "','");
		String hql = "from StoRecipe sr where sr.del_flg = 0 AND and sr.stop_flg = 0 and sr.recipeNo in ('"+strNo+"') and sr.classMeaningCode = 'M1' and sr.validState != 0 and sr.recipeState != 3";
		List<StoRecipe> list = this.createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<StoRecipe>();
	}
}
