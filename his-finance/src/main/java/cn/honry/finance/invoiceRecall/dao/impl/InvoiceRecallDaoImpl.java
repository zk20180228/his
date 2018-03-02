package cn.honry.finance.invoiceRecall.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.invoiceRecall.dao.InvoiceRecallDao;
@Repository("invoiceRecallDao")
@SuppressWarnings({ "all" })
public class InvoiceRecallDaoImpl extends HibernateEntityDao<FinanceInvoice> implements InvoiceRecallDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<FinanceInvoice> queryInvoiceRecall(String page,String rows,String name) throws Exception{
		String hql=" from FinanceInvoice where del_flg=0 and stop_flg=0 and invoiceUsestate in(1,0)";
		if(StringUtils.isNotBlank(name)){
			String hql2 = "select e.EMPLOYEE_JOBNO from T_EMPLOYEE e where e.del_flg=0 and e.stop_flg=0 and e.EMPLOYEE_NAME like'%"+name+"%'";
			List<String> jobNoList = this.getSession().createSQLQuery(hql2).list();
			String jobNo = "";
			if(jobNoList.size()>0){
				for(String modls:jobNoList){
					if(!"".equals(jobNo)){
						jobNo = jobNo + "','";
					}
					jobNo = jobNo + modls;
				}
			}
//			String jobNo = (String) this.getSession().createSQLQuery(hql2).list().get(0);
			hql+=" and ( invoiceGetperson in ('"+jobNo+"') or invoiceGetperson in ('"+name+"'))";
		}
		List<FinanceInvoice> fil=super.getPage(hql, page, rows);
		if(fil!=null&&fil.size()>0){
			return fil;
		}
		return new ArrayList<FinanceInvoice>();
	}

	@Override
	public int getTotal(String name){
		String hql=" from FinanceInvoice where del_flg=0 and stop_flg=0 and invoiceUsestate in(1,0)";
		if(StringUtils.isNotBlank(name)){
			String hql2 = "select e.EMPLOYEE_JOBNO from T_EMPLOYEE e where e.del_flg=0 and e.stop_flg=0 and e.EMPLOYEE_NAME like'%"+name+"%'";
			List<String> jobNoList = this.getSession().createSQLQuery(hql2).list();
			String jobNo = "";
			if(jobNoList.size()>0){
				for(String modls:jobNoList){
					if(!"".equals(jobNo)){
						jobNo = jobNo + "','";
					}
					jobNo = jobNo + modls;
				}
			}
//			String jobNo = (String) this.getSession().createSQLQuery(hql2).list().get(0);
			hql+=" and invoiceGetperson in ('"+jobNo+"') or invoiceGetperson in ('"+name+"')";
		}
		return super.getTotal(hql);
	}

	@Override
	public List<SysEmployee> queryEmpMap()  throws Exception{
		String hql="from SysEmployee where del_flg=0 and stop_flg=0 ";
		List<SysEmployee> empl=super.find(hql, null);
		if(empl!=null&&empl.size()>0){
			return empl;
		}
		return new ArrayList<SysEmployee>();
	}
}
