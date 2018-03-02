package cn.honry.inpatient.diagnose.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDiagnoseMedicare;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.diagnose.dao.DiagnoseMedicareDAO;

@Repository("diagnoseMedicareDAO")
@SuppressWarnings({ "all" })
public class DiagnoseMedicareDaoImpl extends HibernateEntityDao<BusinessDiagnoseMedicare> implements DiagnoseMedicareDAO{
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 *  
	 * @Description：  重新查询医保信息
	 * @Author：zhangjin
	 * @CreateDate：2015-12-30 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public BusinessDiagnoseMedicare getval(String id) {
		String hql="from BusinessDiagnoseMedicare where del_flg=0";
		if(StringUtils.isNotBlank(id)){
			hql=hql+" and id='"+id+"'";
		}
		List<BusinessDiagnoseMedicare> list = this.getSession().createQuery(hql).list();
		return list.size()==0 ? new BusinessDiagnoseMedicare():list.get(0);
	}
	


}
