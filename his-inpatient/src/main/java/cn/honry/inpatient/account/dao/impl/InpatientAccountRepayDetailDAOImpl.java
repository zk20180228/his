package cn.honry.inpatient.account.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientAccountrepaydetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.account.dao.InpatientAccountRepayDetailDAO;
@SuppressWarnings({"all"})
@Repository("inpatientAccountRepaydetailDAO")
public class InpatientAccountRepayDetailDAOImpl extends HibernateEntityDao<InpatientAccountrepaydetail> implements InpatientAccountRepayDetailDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientAccountrepaydetail> getPage(String page, String rows,
			InpatientAccountrepaydetail entity, String accountId, int ishis) {
		String hql = joint(entity,accountId ,ishis);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(InpatientAccountrepaydetail entity, String accountId,
			int ishis) {
		String hql = joint(entity,accountId, ishis);
		return super.getTotal(hql);
	}
	public String joint(InpatientAccountrepaydetail entity ,String accountId ,int ishis){
		String hql="FROM InpatientAccountrepaydetail a WHERE a.inpatientAccount.id = '" + accountId + "' and a.del_flg = 0 and a.detailIshis = " + ishis;
		hql = hql+" ORDER BY a.id";
		return hql;
	}

	@Override
	public void updateIshis(String accountId) {
		//把运存金记录转为历史
		String hql = "update InpatientAccountrepaydetail set detailIshis = 1 where inpatientAccount.id = '" + accountId + "' and del_flg = 0 and detailIshis = 0 ";
		this.getSession().createQuery(hql).executeUpdate();
	}

	@Override
	public void delByParentId(String id) {
		String hql = "delete InpatientAccountrepaydetail ina where ina.inpatientAccount.id = ? and ";
		this.excUpdateHql(hql, id);
	}
	
}
