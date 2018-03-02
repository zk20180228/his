package cn.honry.inner.patient.account.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.patient.account.dao.AccountInInterDAO;
/**  
 *  
 * 内部接口：账户
 * @Author：tangfeishuai
 * @CreateDate：2016-7-07 上午11:56:31  
 * @Modifier：tangfeishuai
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("accountInInterDAO")
@SuppressWarnings({ "all" })
public class AccountInInterDaoImpl extends HibernateEntityDao<PatientAccount> implements AccountInInterDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public PatientAccount getAccountByMedicalrecord(String medicalrecordId) {
		String hql = "from PatientAccount p where p.medicalrecordId = '"+medicalrecordId +"' and p.del_flg=0 ";
		List<PatientAccount> list = this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
