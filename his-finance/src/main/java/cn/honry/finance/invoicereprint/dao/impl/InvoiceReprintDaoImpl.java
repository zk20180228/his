package cn.honry.finance.invoicereprint.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.invoicereprint.dao.InvocieReprintDao;
import cn.honry.finance.invoicereprint.vo.InvoiceReprintVO;
@Repository("invocieReprintDao")
@SuppressWarnings({ "all" })
public class InvoiceReprintDaoImpl extends HibernateEntityDao<OutpatientFeedetailNow> implements InvocieReprintDao {
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<InvoiceReprintVO> getInvoiceVO(final String clinicCode,final String invoiceNo) throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select distinct t.Invoice_No invoiceNo, t.Fee_Cpcd feeCode, t.Doct_Deptname deptName ");
		sb.append(" From t_Outpatient_Feedetail_Now t Where ");
		if(StringUtils.isNotBlank(clinicCode)){
			sb.append(" t.CLINIC_CODE = :clinicCode and ");
		}
		if(StringUtils.isNotBlank(invoiceNo)){
			sb.append(" t.Invoice_No In (:invoiceNo) And ");
		}
		sb.append(" t.TRANS_TYPE = 1 And t.CANCEL_FLAG = 1 And t.PAY_FLAG = 1 ");
		List<InvoiceReprintVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<InvoiceReprintVO>>() {

			@Override
			public List<InvoiceReprintVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(StringUtils.isNotBlank(invoiceNo)){
					query.setParameterList("invoiceNo", Arrays.asList(invoiceNo.split(",")));
				}
				if(StringUtils.isNotBlank(clinicCode)){
					query.setParameter("clinicCode", clinicCode);
				}
				query.addScalar("invoiceNo").addScalar("feeCode")
					.addScalar("deptName");
				return query.setResultTransformer(Transformers.aliasToBean(InvoiceReprintVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InvoiceReprintVO>();
	}
	@Override
	public List<OutpatientFeedetailNow> getfee(String invoiceNo) throws Exception {
		String hql = "from OutpatientFeedetailNow where invoiceNo = ? and transType=1 and payFlag = 1 and cancelFlag = 1 and stop_flg=0 and del_flg=0";
		List<OutpatientFeedetailNow> list = this.find(hql, invoiceNo);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OutpatientFeedetailNow>();
	}
	@Override
	public int updateInvoiceInfo(final String oldInvoiceNo, final String newInvoiceNo) throws Exception {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Update T_FINANCE_INVOICEINFO_NOW t Set t.INVOICE_NO='"+newInvoiceNo+"' Where t.INVOICE_NO = '"+oldInvoiceNo+"' And t.trans_type=1 And CANCEL_FLAG = 1 And t.STOP_FLG=0 And t.DEL_FLG=0 ");
		int count = (int)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				return query.executeUpdate();
			}
		});
		return count;
	}
	@Override
	public int updatePayMode(String oldInvoiceNo, String newInvoiceNo)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append(" Update t_business_paymode_now t Set t.invoice_no='"+newInvoiceNo+"' Where t.invoice_no = '"+oldInvoiceNo+"' And t.trans_type=1 And t.CANCEL_FLAG = 1 And t.STOP_FLG=0 And t.DEL_FLG=0 ");
		int count = (int)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				return query.executeUpdate();
			}
		});
		return count;
	}
	@Override
	public int updateInvoiceDetial(String oldInvoiceNo, String newInvoiceNo)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append(" Update T_FINANCE_INVOICEDETAIL_NOW t Set t.INVOICE_NO='"+newInvoiceNo+"' Where t.INVOICE_NO = '"+oldInvoiceNo+"' And t.trans_type=1 And CANCEL_FLAG = 1 And t.STOP_FLG=0 And t.DEL_FLG=0");
		int count = (int)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				return query.executeUpdate();
			}
		});
		return count;
	}
	@Override
	public int updateFeeDetail(String oldInvoiceNo, String newInvoiceNo)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append(" Update T_OUTPATIENT_FEEDETAIL_NOW t Set t.INVOICE_NO='"+newInvoiceNo+"' Where t.INVOICE_NO = '"+oldInvoiceNo+"' And t.trans_type=1 And CANCEL_FLAG = 1 And t.STOP_FLG=0 And t.DEL_FLG=0 ");
		int count = (int)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				return query.executeUpdate();
			}
		});
		return count;
	}
	
}
