package cn.honry.mobile.importExcel.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.importExcel.dao.ImportExcelDao;
@Repository("importExcelDao")
public class ImportExcelDaoImpl extends HibernateEntityDao<SysEmployee> implements ImportExcelDao{
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void updateEmp(SysEmployee emp) {
		StringBuffer hql = new StringBuffer();
		hql.append("update SysEmployee set name = ?,officePhone = ?,mobile = ?,updateUser = ?,updateTime = ? ");
		hql.append("where jobNo = ? and stop_flg = 0 and del_flg = 0");
		this.excUpdateHql(hql.toString(), emp.getName(),emp.getOfficePhone(),emp.getMobile(),emp.getUpdateUser(),emp.getUpdateTime(),emp.getJobNo());
	}

}
