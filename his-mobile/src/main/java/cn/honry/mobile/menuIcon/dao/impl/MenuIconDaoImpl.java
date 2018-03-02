package cn.honry.mobile.menuIcon.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MenuIcon;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.menuIcon.dao.MenuIconDao;


@Repository("menuIconDao")
@SuppressWarnings({ "all" })
public class MenuIconDaoImpl  extends HibernateEntityDao<MenuIcon> implements MenuIconDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MenuIcon> getMenuIconList(String rows, String page,
			String queryName) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MenuIcon where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and picName like '%").append(queryName).append("%'");
		}
		sb.append(" order by picName");
		List<MenuIcon> list = super.getPage(sb.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MenuIcon>();
	}

	@Override
	public Integer getMenuIconCount(String queryName) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MenuIcon where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and picName like '%").append(queryName).append("%'");
		}
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<MenuIcon> ckeckName(String ckeckName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MenuIcon where stop_flg=0 and del_flg=0 and picName=:ckeckName ");
		Query query=this.getSession().createQuery(sb.toString());
		query.setParameter("ckeckName", ckeckName);
		List<MenuIcon> list=query.list();
		return list;
	}
}
