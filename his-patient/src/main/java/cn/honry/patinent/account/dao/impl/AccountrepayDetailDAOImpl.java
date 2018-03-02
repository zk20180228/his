package cn.honry.patinent.account.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.patinent.account.dao.AccountrepayDetailDAO;

@Repository("repayDetailDAO")
@SuppressWarnings({ "all" })
public class AccountrepayDetailDAOImpl extends HibernateEntityDao<PatientAccountrepaydetail> implements AccountrepayDetailDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<PatientAccountrepaydetail> getPage(PatientAccountrepaydetail entity, String page,
			String rows,String accountId,int ishis,int detailAccounttype) {
		String hql = joint(entity,accountId ,ishis,detailAccounttype);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(PatientAccountrepaydetail entity,String accountId,int ishis,int detailAccounttype) {
		String hql = joint(entity,accountId, ishis,detailAccounttype);
		return super.getTotal(hql);
	}
	public String joint(PatientAccountrepaydetail entity ,String accountId ,int ishis,int detailAccounttype){
		String hql="FROM PatientAccountrepaydetail a WHERE a.account.id='"+accountId+"' and a.del_flg = 0 and a.detailIshis = " + ishis + " and a.detailAccounttype = " + detailAccounttype;
		hql = hql+" ORDER BY a.createTime";
		return hql;
	}

	@Override
	public double totalAccount(String accountId) {
		//计算此卡内充值金额总和
		String hql = "select sum(detailDebitamount) from PatientAccountrepaydetail where account.id = '" + accountId + "' and del_flg = 0 and detailIshis = 0";
		String totalDebitamount = getSession().createQuery(hql).uniqueResult().toString();
		double retTotalDebitamount = Double.parseDouble(totalDebitamount.toString());
		//计算此卡内消费金额总和
		String hql1 = "select sum(detailCreditamount) from PatientAccountrepaydetail where account.id = '" + accountId + "' and del_flg = 0 and detailIshis = 0";
		String totaldetailCreditamount = getSession().createQuery(hql1).uniqueResult().toString();
		double retTotaldetailCreditamount = Double.parseDouble(totaldetailCreditamount.toString());
		//把预存金记录转为历史
		String hql2 = "update PatientAccountrepaydetail set detailIshis = 1 where account.id = '" + accountId + "' and del_flg = 0 and detailIshis = 0";
		this.getSession().createQuery(hql2).executeUpdate();
		return retTotalDebitamount-retTotaldetailCreditamount;
	}

	@Override
	public List<PatientAccountrepaydetail> listRepayDetail(String accountId) {
		String hql="FROM PatientAccountrepaydetail a WHERE a.account.id='"+accountId+"' and a.del_flg = 0 and a.detailIshis = 0 ";
		List<PatientAccountrepaydetail> list = this.getSession().createQuery(hql).list();
		return list.size()==0 ? null : list;
	}

	@Override
	public void updateIshis(String accountId) {
		//把运存金记录转为历史
		String hql = "update PatientAccountrepaydetail set detailIshis = 1 where account.id = '" + accountId + "' and del_flg = 0 and detailIshis = 0 ";
		this.getSession().createQuery(hql).executeUpdate();
	}

	@Override
	public void delByParentId(String id) {
		String hql = "delete from PatientAccountrepaydetail where account.id = ? ";
		this.excUpdateHql(hql, id);
	}
}
