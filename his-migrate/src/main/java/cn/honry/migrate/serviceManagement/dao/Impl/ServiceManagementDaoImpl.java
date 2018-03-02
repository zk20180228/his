package cn.honry.migrate.serviceManagement.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.migrate.serviceManagement.dao.ServiceManagementDao;
@Repository("serviceManagementDao")
@SuppressWarnings({"all"})
public class ServiceManagementDaoImpl extends HibernateEntityDao<ServiceManagement> implements ServiceManagementDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 服务管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<ServiceManagement> queryServiceManagement(String code, String page,String rows, String menuAlias,String serviceType,String serviceState) {
		String hql="from ServiceManagement t where 1=1 ";
		if(StringUtils.isNotBlank(code)){
			code=code.toUpperCase();
			hql+=" and (upper(t.code) like '%"+code+"%' ";
			hql+=" or upper(t.name) like '%"+code+"%'  ";
			hql+=" or upper(t.ip) like '%"+code+"%'  ";
			hql+=" or upper(t.port) like '%"+code+"%') ";
		}
		if(StringUtils.isNotBlank(serviceType)){
			hql+=" and t.type="+serviceType;
		}
		if(StringUtils.isNotBlank(serviceState)){
			hql+=" and t.state="+serviceState;
		}
		hql+=" order by t.code";
		List<ServiceManagement> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<ServiceManagement>();
	}
	/**  
	 * 服务管(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(String code) {
		String hql="from ServiceManagement t where 1=1 ";
		if(StringUtils.isNotBlank(code)){
			hql+=" and (t.code like '%"+code+"%' ";
			hql+=" or t.name like '%"+code+"%' )";
		}
		return super.getTotal(hql);
	}
	/**  
	 * 服务管理 删除
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void delServiceManagement(String id) {
		String sql="delete from I_SERVE_MANAGE where id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();		
	}
	/**  
	 * 服务管理 查询要修改的记录
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public ServiceManagement getOnedata(String id) {
		String hql="from ServiceManagement t where t.id= '"+id+"'";
		List<ServiceManagement> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new ServiceManagement();
		
	}
	@Override
	public List<ServiceManagement> queryServiceManagement(String queryCode) {
		String hql="from ServiceManagement t where 1=1 ";
		if(StringUtils.isNotBlank(queryCode)){
			hql+=" and t.masterprePare="+queryCode;
		}
		hql+=" order by t.code";
		List<ServiceManagement> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<ServiceManagement>();
	}
	@Override
	public Boolean queryUnionList(String serviceName, Integer masterprePare,String id) {
		String hql="from ServiceManagement t where t.code='"+serviceName+"' and t.masterprePare="+masterprePare;
		List<ServiceManagement> list=this.getSession().createQuery(hql).list();
		if(list.size()>0){
			if(StringUtils.isNotBlank(id)){
				if(list.get(0).getId().equals(id)){
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
