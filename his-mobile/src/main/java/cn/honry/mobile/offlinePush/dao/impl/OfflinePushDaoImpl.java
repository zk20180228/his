package cn.honry.mobile.offlinePush.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MOfofflinepush;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.offlinePush.dao.OfflinePushDao;

@Repository("offlinePushDao")
@SuppressWarnings({ "all" })
public class OfflinePushDaoImpl  extends HibernateEntityDao<MOfofflinepush> implements OfflinePushDao{

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MOfofflinepush> getOfflineMesList(String rows, String page,
			String queryName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" select o.id as id, o.TYPE as type, o.USER_ACCOUNT as userAccount,t.DEVICECODE as  deviceCode,");
		sb.append(" o.CREATE_TIME as createTime, ");
		sb.append(" o.PUSH_TIME as pushTime,o.STATUS as status,o.NUM as num,o.BACKUP as backup,t.user_name as userName ");
		sb.append(" from OFOFFLINEPUSH o left join t_sys_user t on t.del_flg=0 and t.stop_flg=0 and o.USER_ACCOUNT=t.user_account  where 1=1 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and o.USER_ACCOUNT like :queryName");
		}
		sb.append(" order by CREATE_TIME desc");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("id",Hibernate.INTEGER).addScalar("type").addScalar("userAccount").addScalar("deviceCode")
				.addScalar("createTime",Hibernate.TIMESTAMP).addScalar("pushTime",Hibernate.TIMESTAMP)
				.addScalar("status",Hibernate.INTEGER).addScalar("num",Hibernate.INTEGER).addScalar("backup").addScalar("userName");
		if(StringUtils.isNotBlank(queryName)){
			queryObject.setParameter("queryName", "%"+queryName+"%");
		}
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<MOfofflinepush> list = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(MOfofflinepush.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MOfofflinepush>();
	}

	@Override
	public Integer getOfflineMesCount(String queryName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" select count(t.ID) from OFOFFLINEPUSH t where 1=1  ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and t.USER_ACCOUNT like :queryName");
		}
		
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString());
		if(StringUtils.isNotBlank(queryName)){
			queryObject.setParameter("queryName", "%"+queryName+"%");
		}
		
		List<BigDecimal> list =queryObject.list();
		if(list!=null&&list.size()>0){
			return list.get(0).intValue();
		}
		return 0;
		
		
	}

	@Override
	public void delOfflineMes(String ids) {
		if(StringUtils.isNotBlank(ids)){
			List list = Arrays.asList(ids.split(","));
			StringBuffer sb= new StringBuffer();
			sb.append(" delete from OFOFFLINEPUSH  where  USER_ACCOUNT in (:ids)");
			SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString());
			queryObject.setParameterList("ids", list);
			queryObject.executeUpdate();
		}
	}
}
