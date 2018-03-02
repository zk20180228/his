package cn.honry.oa.repository.categ.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RepositoryCateg;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.repository.categ.dao.RepositoryCategDao;

@Repository(value="repositoryCategDao")
public class RepositoryCategDaoImpl extends HibernateEntityDao<RepositoryCateg> implements RepositoryCategDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<RepositoryCateg> getCateg(String deptCode, String page,
			String rows, String name,String nodeType) {
		StringBuffer sb = new StringBuffer();
		page = StringUtils.isBlank(page)?page:"1";
		rows = StringUtils.isBlank(rows)?rows:"20";
		sb.append(" from RepositoryCateg where del_flg=0 ");
		if(StringUtils.isNotBlank(deptCode)){
			if(!"root".equals(nodeType)){
				sb.append(" and (diseaseCode = '"+deptCode+"' or code = '"+deptCode+"' )");
			}
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" and name like '%"+name+"%'");
		}
		return this.getPage(sb.toString(), page, rows);
	}

	@Override
	public int getCategTotal(String deptCode, String name,String nodeType) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryCateg where del_flg=0 ");
		if(StringUtils.isNotBlank(deptCode)){
			if(!"root".equals(nodeType)){
				sb.append(" and (diseaseCode = '"+deptCode+"' or code = '"+deptCode+"' )");
			}
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" and name like '%"+name+"%'");
		}
		return this.getTotal(sb.toString());
	}

	@Override
	public RepositoryCateg checkCode(String code) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryCateg where del_flg=0 and code =? ");
		List<RepositoryCateg> list = this.find(sb.toString(), code);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<RepositoryCateg> getAllCate() {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryCateg where del_flg=0 and stop_flg = 0 ");
		List<RepositoryCateg> list = this.find(sb.toString());
		if(list!=null && list.size()>0){
			return list;
		}
		return null;
	}

	

}
