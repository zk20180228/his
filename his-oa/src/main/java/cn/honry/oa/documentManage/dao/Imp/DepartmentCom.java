package cn.honry.oa.documentManage.dao.Imp;

import java.util.Iterator;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;


import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.documentManage.dao.DepartmentDocDao;
@Repository("departmentDoc")
public class DepartmentCom extends HibernateEntityDao<SysDepartment> implements DepartmentDocDao {

	
	//为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* 获取科室名称*/
	@Override
	public SysDepartment getDeptName(String uploadDept) {
		// TODO Auto-generated method stub
		String hql="From SysDepartment where deptCode = '"+uploadDept+"'";
		Iterator iterator = this.getHibernateTemplate().find(hql).iterator();
		if(iterator!=null && !"".equals(iterator)){
			SysDepartment mobileUser = (SysDepartment) iterator.next();
			return mobileUser;
		}else{
			return null;
		}
	}

	/* 获取科室编号*/
	@Override
	public SysDepartment getDeptCode(String uploadDept) {
		// TODO Auto-generated method stub
		String hql="From SysDepartment where deptName = '"+uploadDept+"'";
		Iterator iterator = this.getHibernateTemplate().find(hql).iterator();
		if(iterator!=null && !"".equals(iterator)){
			SysDepartment mobileUser = (SysDepartment) iterator.next();
			return mobileUser;
		}else{
			return null;
		}
	}


	
}
