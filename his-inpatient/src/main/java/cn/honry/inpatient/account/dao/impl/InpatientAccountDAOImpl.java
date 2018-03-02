package cn.honry.inpatient.account.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.account.dao.InpatientAccountDAO;
@SuppressWarnings({"all"})
@Repository("inpatientAccountDAO")
public class InpatientAccountDAOImpl extends HibernateEntityDao<InpatientAccount> implements InpatientAccountDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public InpatientAccount queryByMedical(String string) {
		String hql = "from InpatientAccount a where a.medicalrecordId = '" + string + "' and a.del_flg = 0 ";
		List<InpatientAccount> list = this.getSession().createQuery(hql).list();
		return list.size()==0 ? null :list.get(0);
	}

	@Override
	public InpatientAccount queryByIdcardId(String idcardId) throws Exception {
		String hql = "from InpatientAccount a where a.idcard.id = '" + idcardId + "' and a.del_flg = 0 ";
		List<InpatientAccount> list=null;
		try {
			list = this.getSession().createQuery(hql).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.size()==0 ? null :list.get(0);
	}
}
