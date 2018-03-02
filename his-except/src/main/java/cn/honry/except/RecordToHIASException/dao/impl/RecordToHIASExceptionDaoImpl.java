package cn.honry.except.RecordToHIASException.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.except.RecordToHIASException.dao.RecordToHIASExceptionDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-14
 * @version 1.0
 * @remark:平台异常信息记录
 */
@Repository("recordToHIASExceptionDao")
@SuppressWarnings({"all"})
public class RecordToHIASExceptionDaoImpl extends HibernateEntityDao<RecordToHIASException> implements RecordToHIASExceptionDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void saveExceptionInfo(RecordToHIASException hiasException,Exception e) {
		//获取异常信息
		StringWriter sw = new StringWriter(); 
        e.printStackTrace(new PrintWriter(sw, true)); 
        String strException = sw.toString(); 
		//当前登录科室	
		SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		//当前登录员工
		User user= (User) SessionUtils.getCurrentUserFromShiroSession();
		//当前时间
		Date date = DateUtils.getCurrentTime();
		hiasException.setId(null);
		hiasException.setCreateDept(dept.getDeptCode());
		hiasException.setCreateUser(user.getAccount());
		hiasException.setCreateTime(date);
		hiasException.setProcessStatus("0");
		hiasException.setMsg(Hibernate.createClob(strException));//异常详细信息
		super.save(hiasException);
	}
}
