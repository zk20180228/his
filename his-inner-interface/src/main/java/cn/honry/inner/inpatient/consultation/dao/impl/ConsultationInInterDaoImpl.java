package cn.honry.inner.inpatient.consultation.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.consultation.dao.ConsultationInInterDao;


/**   
* @className：ConsultationDaoImpl
* @description：  会诊申请单DaoImpl
* @author：tuchuanjiang
* @createDate：2015-12-11 下午19：24  
* @version 1.0
 */
@Repository("consultationInInterDAO")
@SuppressWarnings({ "all" })
public class ConsultationInInterDaoImpl extends HibernateEntityDao<InpatientConsultation> implements ConsultationInInterDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	

	/**  
	 * @Description： 通过id查询员工对象
	 * @Author：tcj
	 * @CreateDate：2015-12-19  上午10:09
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public SysEmployee queryById(String id) {
		String hql=" from SysEmployee s where s.del_flg = 0 and s.userId.id = ?";
		List<SysEmployee> sysEmployee = super.find(hql, id);
		if(sysEmployee!=null && sysEmployee.size()>0){
			return sysEmployee.get(0);
		}
		return new SysEmployee();
	}

	
}
