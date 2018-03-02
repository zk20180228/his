package cn.honry.finance.invoiceUsageRecord.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.invoiceUsageRecord.dao.InvoiceUsageRecordDao;

@Repository("invoiceUsageRecordDao")
@SuppressWarnings({ "all" })
public class InvoiceUsageRecordDaoImpl extends HibernateEntityDao<InvoiceUsageRecord> implements InvoiceUsageRecordDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InvoiceUsageRecord> queryDatagrid(String page, String rows,
			String code, String type, String num) throws Exception {
		StringBuffer hql=new StringBuffer();
		hql.append(" from InvoiceUsageRecord where del_flg=0 and stop_flg=0 ");
		if(StringUtils.isNotBlank(code)){
			hql.append(" and userId like'%"+code+"%'");
		};
		if(StringUtils.isNotBlank(type)){
			hql.append(" and userType="+type+"");
		};
		if(StringUtils.isNotBlank(num)){
			hql.append(" and invoiceNo='"+num+"'");
		};
		List<InvoiceUsageRecord> iurl=super.getPage(hql.toString(), page, rows);
		if(iurl!=null&&iurl.size()>0){
			return iurl;
		}
		return new ArrayList<InvoiceUsageRecord>();
	}

	@Override
	public int getTotal(String code,String type, String num) throws Exception{
		StringBuffer hql=new StringBuffer();
		hql.append(" from InvoiceUsageRecord where del_flg=0 and stop_flg=0 ");
		if(StringUtils.isNotBlank(code)){
			hql.append(" and userId like'%"+code+"%'");
		};
		if(StringUtils.isNotBlank(type)){
			hql.append(" and userType="+type+"");
		};
		if(StringUtils.isNotBlank(num)){
			hql.append(" and invoiceNo='"+num+"'");
		};
		return super.getTotal(hql.toString());
	}

	@Override
	public InvoiceUsageRecord getRecordModel(String id) throws Exception {
		String hql=" from InvoiceUsageRecord where del_flg=0 and stop_flg=0 and id='"+id+"'";
		List<InvoiceUsageRecord> iurl=super.find(hql, null);
		if(iurl!=null&&iurl.size()>0){
			return iurl.get(0);
		}
		return new InvoiceUsageRecord();
	}

	@Override
	public List<User> queryUserRecord() throws Exception {
		String sql=" select u.USER_ACCOUNT as account ,u.USER_NAME as name  from T_SYS_USER u where u.stop_flg=0 and u.del_flg=0 ";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("account").addScalar("name");
		List<User> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(User.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<User>();
	}
	/**
	 * 查询发票领取表中的记录
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<FinanceInvoice> queryFinInvoice()  throws Exception{
		String hql=" from FinanceInvoice where del_flg=0 and stop_flg=0";
		List<FinanceInvoice> fil=super.find(hql, null);
		if(fil!=null&&fil.size()>0){
			return fil;
		}
		return new ArrayList<FinanceInvoice>();
	}
}
