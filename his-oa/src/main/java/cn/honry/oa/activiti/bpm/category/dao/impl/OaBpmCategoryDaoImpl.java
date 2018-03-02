package cn.honry.oa.activiti.bpm.category.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.category.dao.OaBpmCategoryDao;

/**
 * 流程分类DAO实现类
 * @author luyanshou
 *
 */
@Repository("oaBpmCategoryDao")
@SuppressWarnings({ "all" })
public class OaBpmCategoryDaoImpl extends HibernateEntityDao<OaBpmCategory>
		implements OaBpmCategoryDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 根据租户id分页查询流程分类
	 * @param tenantId 租户id
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示条数
	 * @return
	 */
	public List<OaBpmCategory> getListByPage(String tenantId,String name,int firstResult,int maxResults){
		StringBuffer sbf= new StringBuffer("from OaBpmCategory t where t.stop_flg=0 and t.del_flg=0 ");
		if(StringUtils.isNotBlank(tenantId)){
			sbf.append("and t.tenantId = ? ");
		}
		if(StringUtils.isNotBlank(name)){
			sbf.append("and t.name like ? ");
		}
		sbf.append(" order by priority");
		Query query = this.createQuery(sbf.toString());
		if(StringUtils.isNotBlank(tenantId)){
			query.setString(0, tenantId);
		}
		if(StringUtils.isNotBlank(name)){
			query.setString(1, "%"+name+"%");
		}
		if(firstResult>=0 && maxResults>0){
			query.setFirstResult(firstResult).setMaxResults(maxResults);
		}
		List<OaBpmCategory> list = query.list();
		
		return list;
	}
	
	/**
	 * 根据租户id获取流程分类总条数
	 * @param tenantId
	 * @return
	 */
	public int getTotal(String tenantId,String name){
		StringBuffer sbf= new StringBuffer("select count(id) from OaBpmCategory t where t.stop_flg=0 and t.del_flg=0 ");
		if(StringUtils.isNotBlank(tenantId)){
			sbf.append("and t.tenantId = ? ");
		}
		if(StringUtils.isNotBlank(name)){
			sbf.append("and t.name like ? ");
		}
		Query query = query = this.createQuery(sbf.toString());
		if(StringUtils.isNotBlank(tenantId)){
			query.setString(0, tenantId);
		}
		if(StringUtils.isNotBlank(name)){
			query.setString(1, "%"+name+"%");
		}
		if(StringUtils.isNotBlank(tenantId)){
			query.setString(0, tenantId);
		}
		if(StringUtils.isNotBlank(name)){
			query.setString(1, "%"+name+"%");
		}
		String string = query.uniqueResult().toString();
		if(StringUtils.isNotBlank(string)){
			return Integer.parseInt(string);
		}
		return 0;
	}
}
