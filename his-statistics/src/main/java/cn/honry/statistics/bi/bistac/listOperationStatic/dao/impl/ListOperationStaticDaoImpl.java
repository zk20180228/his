package cn.honry.statistics.bi.bistac.listOperationStatic.dao.impl;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.listOperationStatic.dao.ListOperationStaticDao;
import cn.honry.statistics.bi.bistac.listOperationStatic.vo.ListOperationStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;

@Repository("listOperationStaticDao")
@SuppressWarnings({ "all" })
public class ListOperationStaticDaoImpl extends HibernateEntityDao<ListOperationStaticVo> implements ListOperationStaticDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 
	 * 手术情况统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 上午10:30:21 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 上午10:30:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ListOperationStaticVo queryVo(final String startTime,final String endTime) {
		final StringBuffer sb = new StringBuffer();
		sb.append("select (select nvl(count(distinct t.OPERATION_ID),0) from T_OPERATION_RECORD t where ");
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			sb.append(" t.CREATETIME >= to_DATE(:startDate, 'yyyy-MM-dd HH24:MI:SS')  AND  t.CREATETIME<=to_DATE(:endDate,'yyyy-MM-dd HH24:MI:SS')  and ");
		}
		sb.append(" t.pasource=2 and t.YNVALID='1' and t.stop_flg=0 and t.del_flg=0) as num1, ");
		sb.append("(select nvl(count( distinct tt.OPERATION_ID),0) from T_OPERATION_RECORD tt where ");
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			sb.append("tt.CREATETIME >= to_DATE(:startDate, 'yyyy-MM-dd HH24:MI:SS')  AND  tt.CREATETIME<=to_DATE(:endDate,'yyyy-MM-dd HH24:MI:SS') and ");
		}
		sb.append(" tt.pasource=1 and tt.YNVALID='1' and tt.stop_flg=0 and tt.del_flg=0) as num2, nvl(0, 0) as num3  from dual ");
		ListOperationStaticVo vo = (ListOperationStaticVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ListOperationStaticVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("num1",Hibernate.INTEGER).addScalar("num2",Hibernate.INTEGER).addScalar("num3",Hibernate.INTEGER);
				if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
					queryObject.setParameter("startDate",startTime );
					queryObject.setParameter("endDate",endTime );
				}
				return (ListOperationStaticVo) queryObject.setResultTransformer(Transformers.aliasToBean(ListOperationStaticVo.class)).uniqueResult();
			}
		});
		return vo;
	}

}
