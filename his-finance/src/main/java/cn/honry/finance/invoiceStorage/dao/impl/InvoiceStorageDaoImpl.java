package cn.honry.finance.invoiceStorage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.invoiceStorage.dao.InvoiceStorageDao;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;
@Repository("invoiceStorageDao")
@SuppressWarnings({ "all" })
public class InvoiceStorageDaoImpl extends HibernateEntityDao<FinanceInvoiceStorage> implements InvoiceStorageDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<BusinessDictionary> queryInvoiceType(String type, String encode) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select c.CODE_ENCODE as encode,c.CODE_NAME as name,c.CODE_PINYIN as pinyin,c.CODE_WB as wb,c.CODE_INPUTCODE as inputCode");
		sql.append(" from T_BUSINESS_DICTIONARY c where c.del_flg=0 and c.stop_flg=0");
		if(type!=null){
			sql.append("and c.CODE_TYPE = '"+type+"' ");
		}
		if(encode!=null){
			sql.append("and c.CODE_ENCODE = '"+encode+"' ");
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(sql.toString())
				.addScalar("encode").addScalar("name").addScalar("pinyin").addScalar("wb").addScalar("inputCode");
//		 queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(DrugInfo.class)).list();
//			if(druginfoList!=null&&druginfoList.size()>0){
//				return druginfoList;
//			}
//			return new ArrayList<DrugInfo>();
		List<BusinessDictionary> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(BusinessDictionary.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<BusinessDictionary>();
	}

	@Override
	public List<FinanceInvoiceStorage> queryInvoiceStorage(String page,
			String rows, FinanceInvoiceStorage fis)  throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id as id, t.invoice_type as invoiceCode,t.invoice_startno as invoiceStartno,t.invoice_endno as invoiceEndno,t.invoice_usedno as invoiceUsedno,t.invoice_usestate as invoiceUseState,t.invoice_pinyin as invoicePinyin,t.invoice_wb as invoiceWb,t.invoice_inputcode as invoiceInputcode,t.invoice_remark as invoiceRemark from  T_FINANCE_INSTORAGE t  where t.stop_flg=0 and t.del_flg=0  ");
		this.whereJoin(fis,sql);
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.addScalar("id").addScalar("invoiceCode").addScalar("invoiceStartno").addScalar("invoiceEndno").addScalar("invoiceUsedno").addScalar("invoiceUseState",Hibernate.INTEGER).addScalar("invoicePinyin").addScalar("invoiceWb").addScalar("invoiceInputcode").addScalar("invoiceRemark");
		query.setResultTransformer(Transformers.aliasToBean(FinanceInvoiceStorage.class));
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		query.setFirstResult((start - 1) * count).setMaxResults(count);
		return query.list();
	}
	/**
	 * 动态拼接条件
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	private void whereJoin(FinanceInvoiceStorage fis,StringBuilder sql)  throws Exception{
		if(fis!=null){
			if(StringUtils.isNotBlank(fis.getInvoiceType())){
				sql.append(" and t.invoice_type = '"+fis.getInvoiceType()+"'");
			}
			if(StringUtils.isNotBlank(fis.getInvoiceEndno())){
				sql.append(" and t.invoice_endno = '"+fis.getInvoiceEndno()+"'");
			}
			if(StringUtils.isNotBlank(fis.getInvoiceStartno())){
				sql.append(" and t.invoice_startno = '"+fis.getInvoiceStartno()+"'");
			}
			if(fis.getInvoiceUseState() != null){
				sql.append(" and t.invoice_usestate = '"+fis.getInvoiceUseState()+"'");
			}
		}
	}
	@Override
	public int getTotal(FinanceInvoiceStorage fis) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.invoice_type as invoiceCode,t.invoice_startno as invoiceStartno,t.invoice_endno as invoiceEndno,t.invoice_usedno as invoiceUsedno,t.invoice_usestate as invoiceUseState,t.invoice_pinyin as invoicePinyin,t.invoice_wb as invoiceWb,t.invoice_inputcode as invoiceInputcode,t.invoice_remark as invoiceRemark from  T_FINANCE_INSTORAGE t  where t.stop_flg=0 and t.del_flg=0  ");
		this.whereJoin(fis,sql);
		return super.getSqlTotal(sql.toString());
	}

	@Override
	public FinanceInvoiceStorage queryFISById(String id)  throws Exception{
		String hql=" from FinanceInvoiceStorage where del_flg=0 and stop_flg=0 and id='"+id+"'";
		List<FinanceInvoiceStorage> fisl=super.find(hql, null);
		if(fisl!=null&&fisl.size()>0){
			return fisl.get(0);
		}
		return new FinanceInvoiceStorage();
	}

	@Override
	public List<FinanceInvoiceStorage> queryFISByType(String type)  throws Exception{
		String hql="from FinanceInvoiceStorage where del_flg=0 and stop_flg=0 and invoiceType='"+type+"' ";
		List<FinanceInvoiceStorage> fisl=super.find(hql, null);
		if(fisl!=null&&fisl.size()>0){
			return fisl;
		}
		return new ArrayList<FinanceInvoiceStorage>();
	}
	
}
