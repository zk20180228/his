package cn.honry.mobile.adviceManage.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MAdviceManage;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.adviceManage.dao.AdviceManageDao;
import cn.honry.utils.ShiroSessionUtils;

@Repository("adviceManageDao")
@SuppressWarnings({ "all" })
public class AdviceManageDaoImpl extends HibernateEntityDao<MAdviceManage> implements AdviceManageDao{

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MAdviceManage> getMAdviceManageList(String rows, String page,String queryName) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MAdviceManage where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and (userAccount like '%").append(queryName).append("%'");
			sb.append(" or empName like '%").append(queryName).append("%'");
			sb.append(" )");
		}
		List<MAdviceManage> list = super.getPage(sb.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MAdviceManage>();
	}

	@Override
	public Integer getMAdviceManageCount(String queryName) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MAdviceManage where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and (userAccount like '%").append(queryName).append("%'");
			sb.append(" or empName like '%").append(queryName).append("%'");
			sb.append(" )");
		}
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public void delAdviceManage(String ids) throws Exception {
		String userAccount=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuffer hql = new StringBuffer(); 
		String [] id=ids.split(",");
		hql.append("update MAdviceManage set del_flg = 1,deleteUser =:deleteUser,deleteTime =:deleteTime  where id in(:ids)");
		Query query=this.getSession().createQuery(hql.toString());
		query.setParameter("deleteUser", userAccount);
		query.setParameter("deleteTime", new Date());
		query.setParameterList("ids", id);
		query.executeUpdate();
		
	}

	@Override
	public List<User> getUserManageList(String rows, String page,String queryUser) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" select t.user_id as id,t.user_account as account, t.user_name as name, t.user_sex as sex,t.user_type as type  ");
		sb.append(" from t_sys_user t where t.stop_flg=0  and t.del_flg=0 ");
		sb.append(" and t.user_account not in (select a.user_account from m_advicemanage a where a.stop_flg=0 and a.del_flg=0 ) ");
		if(StringUtils.isNotBlank(queryUser)){
			sb.append(" and (t.user_account like :queryUser");
			sb.append(" or t.user_name like :queryUser ");
			sb.append(" )");
		}
		
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("id").addScalar("account").addScalar("name").addScalar("sex",Hibernate.INTEGER)
				.addScalar("type",Hibernate.INTEGER);
		if(StringUtils.isNotBlank(queryUser)){
			queryObject.setParameter("queryUser", "%"+queryUser+"%");
		}
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<User> list = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(User.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<User>();
	}

	@Override
	public Integer getUserManageCount(String queryUser) throws Exception {
		StringBuffer sb= new StringBuffer();
		sb.append(" select count(t.user_id) from t_sys_user t where t.stop_flg=0  and t.del_flg=0  ");
		sb.append(" and t.user_account not in (select a.user_account from m_advicemanage a where a.stop_flg=0 and a.del_flg=0 ) ");
		if(StringUtils.isNotBlank(queryUser)){
			sb.append(" and (t.user_account like :queryUser");
			sb.append(" or t.user_name like :queryUser ");
			sb.append(" )");
		}
		
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString());
		if(StringUtils.isNotBlank(queryUser)){
			queryObject.setParameter("queryUser", "%"+queryUser+"%");
		}
		
		List<BigDecimal> list =queryObject.list();
		if(list!=null&&list.size()>0){
			return list.get(0).intValue();
		}
		return 0;
	}
}
