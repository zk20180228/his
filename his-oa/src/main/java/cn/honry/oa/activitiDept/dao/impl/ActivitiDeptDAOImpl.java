package cn.honry.oa.activitiDept.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.oa.activitiDept.dao.ActivitiDeptDAO;
import cn.honry.utils.HisParameters;

@Repository("activitiDeptDAO")
@SuppressWarnings({ "all" })
public class ActivitiDeptDAOImpl extends HibernateEntityDao<OaActivitiDept> implements ActivitiDeptDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<SysDepartment> queryDept(){
		List<OaActivitiDept> activitiDeptList = this.queryActivitiDept();
		List<SysDepartment> list=null;
		String sql = "select d.dept_code as deptCode,d.dept_name as deptName from T_DEPARTMENT d where 1=1 ";
		if(activitiDeptList.size()>0){
			sql+= "and d.dept_Code not in (select a.dept_Code from T_OA_ACTIVITI_DEPT a where a.dept_Type=0) ";
		}
		sql+= "and d.stop_flg=0 and d.del_flg=0 order by d.DEPT_ORDER";
		SQLQuery query = this.getSession().createSQLQuery(sql.toString()).addScalar("deptCode").addScalar("deptName");
		list = query.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<OaActivitiDept> queryActivitiDept() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from OaActivitiDept a order by a.deptOrder");
		List<OaActivitiDept> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaActivitiDept>();
	}

	@Override
	public void delActivitiDept() {
		String sql="delete from T_OA_ACTIVITI_DEPT ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	@Override
	public List<SysDepartment> queryAllDept() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from SysDepartment d where d.stop_flg=0 and d.del_flg=0 ");
		List<SysDepartment> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<OaActivitiDept> getPage( String page,String rows,OaActivitiDept entity) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(OaActivitiDept entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}
	
	public String joint(OaActivitiDept entity){
		String hql="FROM OaActivitiDept d WHERE 1=1 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getDeptName())){
				hql = hql +" AND (d.deptCode LIKE '%"+entity.getDeptName().toUpperCase()+"%'"
					+ " OR d.deptName LIKE '%" + entity.getDeptName() + "%')";
			}
			if(StringUtils.isNotBlank(entity.getParentCode())){
				hql = hql +" AND d.parentCode = '"+entity.getParentCode()+"'";
			}
		}
		hql = hql+" ORDER BY d.deptOrder";
		return hql;
	}

	@Override
	public SysDepartment queryDeptById(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from SysDepartment d where d.id= '"+id+"' and d.stop_flg=0 and d.del_flg=0 ");
		List<SysDepartment> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	/**  
	 * 
	 * 获取所有工作流科室..树
	 * @Author: zpty
	 * @CreateDate: 2017年8月21日 上午10:26:01 
	 * @version: V1.0
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<OaActivitiDept> queryAllTreeDept() {
		String hql="from OaActivitiDept t order by t.parentUppath,t.deptOrder";
		List<OaActivitiDept> list=super.find(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
			return new ArrayList<OaActivitiDept>();
	}

	@Override
	public String searchDouble(OaActivitiDept activitiDept) {
		String hql="from OaActivitiDept t where t.deptCode='"+activitiDept.getDeptCode()+"' ";
		if(StringUtils.isNotBlank(activitiDept.getId())){
			hql += " and t.id != '"+activitiDept.getId()+"' ";
		}
		List<OaActivitiDept> list=super.find(hql, null);
		if(list!=null && list.size()>0){
			return "1";
		}
			return "0";
	}

	@Override
	public void delete(String dId) {
		String sql = "delete from T_Oa_Activiti_Dept t where t.id='"+dId+"' or t.parentUppath like '%"+dId+"%'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
}
