package cn.honry.mobile.openFireInit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.bean.model.Ofuser;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.openFireInit.dao.OpenFireInitDao;

@Repository("openFireInitDao")
@SuppressWarnings({ "all" })
public class OpenFireInitDaoImpl extends HibernateEntityDao<Ofuser> implements OpenFireInitDao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void delOfUserAccount(String account1, String account2)
			throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("delete from Ofuser where userName not in ('");
		hql.append(account1);
		hql.append("','");
		hql.append(account2);
		hql.append("')");
		super.excUpdateHql(hql.toString(), null);
	}

	@Override
	public List<User> findUsers() {
		String hql=" from User where stop_flg=0 and del_flg=0 and userAppUsageStatus!=0";
		List<User> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<User>();
	}


}
