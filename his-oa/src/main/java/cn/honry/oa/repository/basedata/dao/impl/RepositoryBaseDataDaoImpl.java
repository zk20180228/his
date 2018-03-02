package cn.honry.oa.repository.basedata.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RepositoryBaseData;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.repository.basedata.dao.RepositoryBaseDataDao;
@Repository("repositoryBaseDataDao")
@SuppressWarnings({ "all" })
public class RepositoryBaseDataDaoImpl extends HibernateEntityDao<RepositoryBaseData> implements RepositoryBaseDataDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<RepositoryBaseData> getBaseData(String name, String page,
			String rows) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryBaseData where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(name)){
			sb.append("AND (term like '%"+name+"%' ");
			sb.append(" or interpretation like '%"+name+"%' ");
			sb.append(" or remark like '%"+name+"%' )");
		}
		sb.append(" order by createTime Desc ");
		return this.getPage(sb.toString(), page, rows);
	}

	@Override
	public int getBaseDataTotal(String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryBaseData where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(name)){
			sb.append(" AND (term like '%"+name+"%' ");
			sb.append(" or interpretation like '%"+name+"%' ");
			sb.append(" or remark like '%"+name+"%' )");
		}
		return this.getTotal(sb.toString());
	}

}
