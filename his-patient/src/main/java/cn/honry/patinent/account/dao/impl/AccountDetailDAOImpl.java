package cn.honry.patinent.account.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.patinent.account.dao.AccountDetailDAO;

@Repository("detailDAO")
@SuppressWarnings({ "all" })
public class AccountDetailDAOImpl extends HibernateEntityDao<PatientAccountdetail> implements AccountDetailDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<PatientAccountdetail> getPage(PatientAccountdetail entity, String page,
			String rows,String accountId) {
		String hql = joint(entity,accountId);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(PatientAccountdetail entity,String accountId) {
		String hql = joint(entity,accountId);
		return super.getTotal(hql);
	}
	public String joint(PatientAccountdetail entity ,String accountId){
		String hql="FROM PatientAccountdetail a WHERE a.account.id='"+accountId+"' and a.del_flg = 0 ";
		hql = hql+" ORDER BY a.createTime";
		return hql;
	}

	@Override
	public void delByParentId(String id) {
		String hql="delete PatientAccountdetail a WHERE a.account.id = ? ";
		this.excUpdateHql(hql, id);
	}
	/**
	 * @Description:根据领取人，发票类型，使用状态查询
	 * @Author：  wj
	 * @CreateDate： 2015-12-8
	 * @ModifyRmk：  
	 * @version 1.0
	 * @return 
	**/
	
	
	@Override
	public FinanceInvoice findbyall(String emp,String type) {
		String hql="from FinanceInvoice f where f.invoiceGetperson='"+emp+"' and f.invoiceType='"+type +"' and f.del_flg='0'and f.stop_flg='0' ";
		List<FinanceInvoice> financeList = this.find(hql);
		if(financeList!=null&&financeList.size()>0){
			return financeList.get(0);
		}
		return null;
	}

	@Override
	public SysEmployee findEmpByUserid(String id) {
		String hql="from SysEmployee e where e.userId='"+id+"' and e.del_flg='0'and e.stop_flg='0' ";
		List<SysEmployee> empList = this.find(hql, null);
		if(empList!=null&&empList.size()>0){
			return empList.get(0);
		}
		return null;
	}

	@Override
	public FinanceUsergroup findFinanceByempId(String id) {
		String hql="from FinanceUsergroup f where f.employee='"+id+"' and f.del_flg='0'and f.stop_flg='0' ";
		List<FinanceUsergroup> finacnceList = this.find(hql);
		if(finacnceList!=null&&finacnceList.size()>0){
			return finacnceList.get(0);
		}
		return null;
	}
}
