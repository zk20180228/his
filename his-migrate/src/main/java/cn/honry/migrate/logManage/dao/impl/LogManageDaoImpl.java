package cn.honry.migrate.logManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.LogManage;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.logManage.dao.LogManageDao;
@SuppressWarnings("all")
@Repository("logManageDao")
public class LogManageDaoImpl extends HibernateEntityDao<LogManage> implements LogManageDao {
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
	public List<LogManage> queryLogManage(String code, String page, String rows,String param) {
		String hql="from LogManage t where t.code='"+code+"' ";
		if(StringUtils.isNotBlank(param)){
			hql+=" and (t.tableName like '%"+param+"%' ";
			hql+=" or t.tableZhname like '%"+param+"%' )";
		}
		hql+=" order by t.createtime desc";
		return super.getPage(hql, page, rows);
	}
	/**  
	 * 查询日志 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(String code,String param) {
		String hql="from LogManage t where t.code='"+code+"' ";
		if(StringUtils.isNotBlank(param)){
			hql+=" and (t.tableName like '%"+param+"%' ";
			hql+=" or t.tableZhname like '%"+param+"%' )";
		}
		return super.getTotal(hql);
	}
}
