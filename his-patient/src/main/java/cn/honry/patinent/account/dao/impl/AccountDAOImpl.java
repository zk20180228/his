package cn.honry.patinent.account.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.patinent.account.dao.AccountDAO;

@Repository("accountDAO")
@SuppressWarnings({ "all" })
public class AccountDAOImpl extends HibernateEntityDao<PatientAccount> implements AccountDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<PatientAccount> getPage(PatientAccount entity, String page,
			String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(PatientAccount entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}
	public String joint(PatientAccount entity){
		String hql="FROM PatientAccount a WHERE a.del_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getAccountName())){
				hql = hql+" AND a.accountName LIKE '%"+entity.getAccountName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getAccountType())){
				hql = hql+" AND a.accountType LIKE '%"+entity.getAccountType()+"%'";
			}
		}
		hql = hql+" ORDER BY a.createTime";
		return hql;
	}

	@Override
	public PatientAccount queryByIdcardId(String idcardId) {
		String sql = "from PatientAccount where idcard.id = '"+idcardId +"' and del_flg=0 ";
		List<PatientAccount> list = this.getSession().createQuery(sql).list();
		return  list.size()==0?null:list.get(0);
	}
	@Override
	public PatientAccount queryById(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("from PatientAccount where id = '"+ id +"' and del_flg=0 ");
		return (PatientAccount) this.getSession().get(PatientAccount.class, id);
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
