package cn.honry.oa.activiti.task.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaTaskDefBase;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.form.vo.ConfFormVo;
import cn.honry.oa.activiti.task.dao.TaskDefaultDao;
/**
 * 用户任务Service实现类
 * @author luyanshou
 *
 * @param <T>
 */
@Repository("taskDefaultDao")
@SuppressWarnings({ "all" })
public class TaskDefaultDaoImpl<T> extends HibernateEntityDao<T> implements TaskDefaultDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 执行HQL返回唯一结果
	 */
	public <T> T findUnique(String hql,T t,Object... values){
		return (T) this.createQuery(hql, values).setMaxResults(1)
                .uniqueResult();
	}
	
	/**
	 * 保存方法
	 * @param t
	 */
	public <T> void saveOrUpdate(T t){
		this.getHibernateTemplate().saveOrUpdate(t);
	}
	
	/**
	 * 根据主键查询实体
	 * @param id 
	 * @param t
	 * @return
	 */
	public <T> T get(String id,T t){
		return (T) this.getHibernateTemplate().get(entityClass, id);
	}
	
	public <T> List<T> getList(String hql,T t,Object... values){
		return (List<T>)this.createQuery(hql, values).list();
	}
	
	public <T> List<T> getList(String hql,int firstResult,int maxResults,T t,Object... values){
		return (List<T>)this.createQuery(hql, values).setFirstResult(firstResult).setMaxResults(maxResults).list();
	}
	
	public int getCount(String hql,Object... values){
		Object result = this.createQuery(hql, values).uniqueResult();
		if(result==null){
			return 0;
		}
		 String string = result.toString();
		 if(StringUtils.isNotBlank(string)){
			 return Integer.parseInt(string);
		 }
		return 0;		
	}
}
