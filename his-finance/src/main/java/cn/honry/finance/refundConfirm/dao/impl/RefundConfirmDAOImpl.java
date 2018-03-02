package cn.honry.finance.refundConfirm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.refundConfirm.dao.RefundConfirmDAO;

@SuppressWarnings("all")
@Repository("refundConfirmDAO")
public class RefundConfirmDAOImpl extends HibernateEntityDao<InpatientCancelitem> implements RefundConfirmDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientCancelitemNow> query(String billNo) {
		String hql = "from InpatientCancelitemNow where billNo in ('"+billNo+"') and confirmFlag = 0 and chargeFlag = 0 and del_flg=0 and stop_flg=0 and applyFlag = 1";
		List<InpatientCancelitemNow> cancelitemList = super.find(hql);
		if(cancelitemList==null||cancelitemList.size()<=0){
			return new ArrayList<InpatientCancelitemNow>();
		}
		return cancelitemList;
	}

	@Override
	public List<InpatientCancelitemNow> findByIds(String drugApplyIds) {
		String hql = "select t.recipe_no as recipeNo,t.sequence_no as sequenceNo,sum(t.quantity) as quantity from t_inpatient_cancelitem_now t where t.apply_flag = 1 and t.confirm_flag=0 and t.charge_flag=0 and t.apply_no in ('"+drugApplyIds+"') group by recipe_no,sequence_no ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("recipeNo").addScalar("sequenceNo",Hibernate.INTEGER).addScalar("quantity",Hibernate.DOUBLE);
		List<InpatientCancelitemNow> cancelitemList = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientCancelitemNow.class)).list();
		if(cancelitemList!=null&&cancelitemList.size()>0){
			  return cancelitemList;
		  }
		return new ArrayList<InpatientCancelitemNow>();
	}

	@Override
	public OutpatientFeedetailNow queryByRecipeNo(String recipeNo,Integer sequenceNo) {
		String hql = "from OutpatientFeedetailNow where recipeNo = ? and sequenceNo = ?  and del_flg=0 and stop_flg=0 and transType = 1 and cancelFlag = 1 ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, recipeNo,sequenceNo);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}

	@Override
	public FinanceInvoiceInfoNow queryByInfo(String invoiceNo) {
		String hql = "from FinanceInvoiceInfoNow where invoiceNo in ('"+invoiceNo+"') and del_flg=0 and stop_flg=0 ";
		List<FinanceInvoiceInfoNow> invoiceInfoList = super.find(hql);
		if(invoiceInfoList==null||invoiceInfoList.size()<=0){
			return new FinanceInvoiceInfoNow();
		}
		return invoiceInfoList.get(0);
	}

	@Override
	public List<InpatientCancelitemNow> findByApplyIds(String drugApplyIds) {
		String hql = "from InpatientCancelitemNow where id in ('"+drugApplyIds+"') and confirmFlag = 0 and chargeFlag = 0 and del_flg=0 and stop_flg=0 and applyFlag = 1";
		List<InpatientCancelitemNow> cancelitemList = super.find(hql, null);
		if(cancelitemList==null||cancelitemList.size()<=0){
			return new ArrayList<InpatientCancelitemNow>();
		}
		return cancelitemList;
	}
}
