package cn.honry.portal.login.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.UserLogin;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.portal.login.dao.UserLoginDAO;
import cn.honry.utils.DateUtils;

@Repository("userLoginDAO")
@SuppressWarnings({ "all" })
public class UserLoginDAOImpl  extends HibernateEntityDao<UserLogin> implements UserLoginDAO {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public int getTotal(UserLogin userLogin) {
      String hql= joint(userLogin);
		return super.getTotal(hql);
	}
	@Override
	public List<UserLogin> getPage(String page, String rows, UserLogin userLogin) {
		String hql = joint(userLogin);
		return super.getPage(hql, page, rows);
	}
	
	/**  
	 *  
	 * @Description： 查询条件拼接
	 * @Author：wujiao
	 * @CreateDate：2015-8-17 下午05:12:16  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-8-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String joint(UserLogin entity){
		String hql="FROM UserLogin ul  where "+DataRightUtils.connectHQLSentence("ul");
		if(entity!=null){
			if(entity.getLoginTime()!=null){
				hql += " AND TO_CHAR(ul.loginTime,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(entity.getLoginTime())+"'";
			}
			if(StringUtils.isNotBlank(entity.getUserId())){
				hql += " AND ul.userId in (select u.id from User u where u.account LIKE '%"+entity.getUserId()+"%' or u.name LIKE '%"+entity.getUserId()+"%' OR u.nickName LIKE '%"+entity.getUserId()+"%' )" ;
			}  

		}
		hql+=" order by ul.loginTime desc";
		return hql;
	}

}

