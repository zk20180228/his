package cn.honry.inpatient.docAdvManage.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.docAdvManage.dao.DepartmentInfoDAO;

@Repository("departmentInfoDAO")
@SuppressWarnings({ "all" })
public class DepartmentInfoDAOImpl extends HibernateEntityDao<SysDepartment> implements DepartmentInfoDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<SysDepartment> queryPage(String page, String rows,
			SysDepartment entity) {
		String hql = jointDept(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int queryTotal(SysDepartment entity) {
		String hql = jointDept(entity);
		return super.getTotal(hql);
	}
	
	public String jointDept(SysDepartment entity){
		String hql = "from SysDepartment t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(entity.getDeptType())){
			hql = hql+" and t.deptType ='"+entity.getDeptType()+"'";
		}
		if(StringUtils.isNotBlank(entity.getDeptName())){
			hql = hql+" and t.deptName like '%"+entity.getDeptName()+"%'";
		}
		return hql;
	}

}
