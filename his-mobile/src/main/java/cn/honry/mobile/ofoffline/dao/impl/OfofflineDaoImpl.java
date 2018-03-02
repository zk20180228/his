package cn.honry.mobile.ofoffline.dao.impl;

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

import cn.honry.base.bean.model.MOfoffline;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.ofoffline.dao.OfofflineDao;


@Repository("ofofflineDao")
@SuppressWarnings({ "all" })
public class OfofflineDaoImpl extends HibernateEntityDao<MOfoffline> implements OfofflineDao{


	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<MOfoffline> getOfofflineList(String rows, String page,String queryName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" select o.USERNAME as userName, o.MESSAGEID as messageId,TO_CHAR(substr(o.CREATIONDATE, 3)/ (1000 * 60 * 60 * 24) ");
		sb.append("  + TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as creationDate,");
		sb.append(" o.MESSAGESIZE as messageSize ,LONG_TO_CHAR_HD(o.rowid,  'ofoffline', 'STANZA') as stanza,t.user_name as uname ");
		sb.append(" from ofoffline o left join t_sys_user t on t.del_flg=0 and t.stop_flg=0 and o.USERNAME=t.user_account  where 1=1 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and USERNAME like :queryName");
		}
		sb.append(" order by creationDate desc");
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("userName").addScalar("messageId",Hibernate.INTEGER).addScalar("creationDate",Hibernate.TIMESTAMP)
				.addScalar("messageSize",Hibernate.INTEGER).addScalar("stanza").addScalar("uname");
		if(StringUtils.isNotBlank(queryName)){
			queryObject.setParameter("queryName", "%"+queryName+"%");
		}
		
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		List<MOfoffline> list = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(MOfoffline.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MOfoffline>();
	}

	@Override
	public Integer getOfofflineCount(String queryName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" from MOfoffline where 1=1 ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append(" and userName like '%").append(queryName).append("%'");
		}
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public void delOfoffline(String ids) {
		if(StringUtils.isNotBlank(ids)){
			List list = Arrays.asList(ids.split(","));
			StringBuffer sb= new StringBuffer();
			sb.append(" delete from ofoffline  where  MESSAGEID in (:ids)");
			SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString());
			queryObject.setParameterList("ids", list);
			queryObject.executeUpdate();
		}
	}
}
