package cn.honry.mobile.ofBatchPush.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.ofBatchPush.dao.OfBatchPushDao;

@Repository("ofBatchPushDao")
@SuppressWarnings({ "all" })
public class OfBatchPushDaoImpl  extends HibernateEntityDao<OFBatchPush> implements OfBatchPushDao{

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OFBatchPush> getOfBatchPushList(String rows, String page,
			String queryName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" from OFBatchPush where 1=1 ");
		if(StringUtils.isNotBlank(queryName)&&!"0".equals(queryName)){
			sb.append(" and status=").append(queryName);
		}
		sb.append(" order by createTime desc");
		List<OFBatchPush> list = super.getPage(sb.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<OFBatchPush>();
	}

	@Override
	public Integer getOfBatchPushCount(String queryName) {
		StringBuffer sb= new StringBuffer();
		sb.append(" from OFBatchPush where 1=1 ");
		if(StringUtils.isNotBlank(queryName)&&!"0".equals(queryName)){
			sb.append(" and status=").append(queryName);
		}
		Integer total = super.getTotal(sb.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public void delOfBatchPush(String ids) {
		if(StringUtils.isNotBlank(ids)){
			List list = Arrays.asList(ids.split(","));
			StringBuffer sb= new StringBuffer();
			sb.append(" delete from OFBatchPush  where  id in (:ids)");
			SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString());
			queryObject.setParameterList("ids", list);
			queryObject.executeUpdate();
		}
		
	}

}
