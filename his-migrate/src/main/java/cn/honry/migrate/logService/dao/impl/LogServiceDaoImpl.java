package cn.honry.migrate.logService.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.LogServiceVo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.logService.dao.LogServiceDao;
@Repository("logServiceDao")
@SuppressWarnings("all")
public class LogServiceDaoImpl extends HibernateEntityDao<LogServiceVo> implements LogServiceDao {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<LogServiceVo> queryLogService(String serviceCode,
			String STime,String Etime, String page, String rows, String menuAlias) {
		String hql=" from LogServiceVo t where 1=1 ";
		if(StringUtils.isNotBlank(serviceCode)){
			serviceCode=serviceCode.toUpperCase();
			hql+=" and upper(t.serviceCode) like '%"+serviceCode+"%' ";
		}
		if(StringUtils.isNotBlank(STime)){
			hql+=" and t.heartNewTime >=to_date("+STime+" ,'yyyy-mm-dd HH24:mi:ss') ";
		}
		if(StringUtils.isNotBlank(Etime)){
			hql+=" and t.heartNewTime <=to_date("+Etime+" ,'yyyy-mm-dd HH24:mi:ss') ";
		}
		hql+=" order by t.heartNewTime desc";
		int page1=Integer.valueOf(page==null?"1":page);
		int row1=Integer.valueOf(rows==null?"20":rows);
		int frist=(page1-1)*row1;
		List<LogServiceVo> list=this.getSession().createQuery(hql).setFirstResult(frist).setMaxResults(row1).list();
		if(list.size()>0){
			return list;
		}
		return new ArrayList<LogServiceVo>();
	}

	@Override
	public int totalService(String serviceCode,String STime,String Etime) {
		String sql="select to_char(count(1)) serviceCode from I_LOG_SERVER t where 1=1 ";
		if(StringUtils.isNotBlank(serviceCode)){
			serviceCode=serviceCode.toUpperCase();
			sql+=" and upper(t.SERVER_CODE) like '%"+serviceCode+"%' ";
		}
		if(StringUtils.isNotBlank(STime)){
			sql+=" and t.HEART_NEWTIME >=to_date("+STime+" ,'yyyy-mm-dd HH24:mi:ss') ";
		}
		if(StringUtils.isNotBlank(Etime)){
			sql+=" and t.HEART_NEWTIME <=to_date("+Etime+" ,'yyyy-mm-dd HH24:mi:ss') ";
		}
		List<LogServiceVo> total= this.getSession().createSQLQuery(sql).addScalar("serviceCode")
				.setResultTransformer(Transformers.aliasToBean(LogServiceVo.class)).list();
		if(total!=null){
			return Integer.parseInt(total.get(0).getServiceCode());
		}
		return 0;
	}

}
