package cn.honry.oa.hospitalFileManager.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.hospitalFileManager.dao.EmployeeDeptDao;
@Repository("employeeDeptDao")
public class EmployeeDeptDaoImp extends HibernateEntityDao<EmployeeExtend> implements EmployeeDeptDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* 根据部门编号查询部门人员  */
	@Override
	public List<EmployeeExtend> getDeptMan(String deptCode) {
		String hql="From EmployeeExtend t where t.stop_flg = 0 and t.del_flg = 0 and t.departmentCode = '"+deptCode+"'";
		List<EmployeeExtend> list =(List<EmployeeExtend>) this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<EmployeeExtend>();	
	}
}
