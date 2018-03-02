package cn.honry.inner.mobile.dbFile.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.mobile.dbFile.dao.PublicBookOADao;

@Repository("publicBookOADao")
@SuppressWarnings({ "all" })
public class PublicBookOADaoImpl extends HibernateEntityDao<PublicAddressBook> implements PublicBookOADao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<PublicAddressBook> getAllPublicBookOA() {
		String hql = "from PublicAddressBook where stop_flg = 0 and del_flg = 0 order by nodeType,path";
		List<PublicAddressBook> list = super.find(hql);
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}


}
