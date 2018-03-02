package cn.honry.migrate.logAccess.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.LogAccess;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.logAccess.dao.LogAccessDao;
@SuppressWarnings("all")
@Repository("logAccessDao")
public class LogAccessDaoImpl extends HibernateEntityDao<LogAccess> implements LogAccessDao {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 查询日志
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<LogAccess> queryLogAccess(String code,String page,String rows,String param) {
		String hql="from LogAccess t where t.interCode='"+code+"' ";
		if(StringUtils.isNotBlank(param)){
			hql+=" and (t.interName like '%"+param+"%' )";
		}
		hql+=" order by t.createtime desc ";
		return super.getPage(hql, page, rows);
	}
	@Override
	public int queryTotal(String code,String param) {
		String hql="from LogAccess t where t.interCode='"+code+"' ";
		if(StringUtils.isNotBlank(param)){
			hql+=" and (t.interName like '%"+param+"%' )";
		}
		return super.getTotal(hql);
	}
}
