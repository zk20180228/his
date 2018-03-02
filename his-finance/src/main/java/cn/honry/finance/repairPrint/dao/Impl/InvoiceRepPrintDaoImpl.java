package cn.honry.finance.repairPrint.dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.repairPrint.dao.InvoiceRepPrintDao;
import cn.honry.finance.repairPrint.vo.InvoiceRepPrintVo;
import cn.honry.utils.HisParameters;
@Repository("invoiceRepPrintDao")
@SuppressWarnings({ "all" })
public class InvoiceRepPrintDaoImpl extends HibernateEntityDao<FinanceInvoiceInfoNow> implements InvoiceRepPrintDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<InvoiceRepPrintVo> queryInvoiceInfo(InvoiceRepPrintVo invoiceRepPrintVo) {
		/*String sql = "SELECT a.INVOICE_NO as invoiceNo,a.balance_date as balanceDate,a.PAYKIND_CODE as paykindName,c.EMPLOYEE_NAME as balanceOperName,a.TOT_COST as totCost,a.OWN_COST as ownCost,a.PAY_COST as payCost,a.PUB_COST as pubCost,";
			sql = sql+"d.PATIENT_NAME as patientName,e.UNIT_NAME as pactName,f.DEPT_NAME as deptName,k.DEPT_NAME as nurseCellName,l.EMPLOYEE_NAME as houseDocName,n.BED_NAME as bedName,d.REPORT_BIRTHDAY as reportBirthday,d.PREPAY_COST as prepayCost,a.balance_no as balanceNo FROM T_INPATIENT_BALANCEHEAD a ";
			sql = sql+" LEFT JOIN T_EMPLOYEE c on c.EMPLOYEE_ID = a.balance_opercode";
			sql = sql+" LEFT JOIN T_INPATIENT_INFO d on d.inpatient_no = a.inpatient_no";
			sql = sql+" LEFT JOIN T_BUSINESS_CONTRACTUNIT e on e.UNIT_CODE = a.pact_code";
			sql = sql+" LEFT JOIN T_DEPARTMENT f on f.DEPT_ID = d.DEPT_CODE";
			sql = sql+" LEFT JOIN T_BUSINESS_BEDWARD g on g.BEDWARD_ID in( select j.BEDWARD_ID from t_business_hospitalbed j where j.BED_ID in( select i.bed_id from t_inpatient_bedinfo i where i.bedinfo_id in(select h.bedinfo_id from t_inpatient_info h where h.INPATIENT_NO = d.INPATIENT_NO)))";
			sql = sql+" LEFT JOIN T_DEPARTMENT k on g.BEDWARD_NURSESTATION = k.DEPT_ID";
			sql = sql+" LEFT JOIN T_EMPLOYEE l on l.EMPLOYEE_ID in (select m.HOUSE_DOC_CODE FROM T_INPATIENT_BEDINFO m WHERE m.BEDINFO_ID = d.BEDINFO_ID)";
			sql = sql+" LEFT JOIN T_INPATIENT_BEDINFO aa on aa.BEDINFO_ID = d.BEDINFO_ID";
			sql = sql+" LEFT JOIN T_BUSINESS_HOSPITALBED n on n.BED_ID = aa.BED_ID";*/
		
		 String sql="SELECT a.INVOICE_NO as invoiceNo, a.balance_date as balanceDate,a.PAYKIND_CODE as paykindName, a.balance_opername as balanceOperName, a.TOT_COST  as totCost,a.OWN_COST as ownCost,a.PAY_COST as payCost,a.PUB_COST as pubCost, d.PATIENT_NAME as patientName, e.UNIT_NAME as pactName,d.DEPT_NAME as deptName,";
		 sql += " d.nurse_cell_name as nurseCellName,d.house_doc_name as houseDocName, d.BED_NAME  as bedName,d.REPORT_BIRTHDAY as reportBirthday, d.PREPAY_COST  as prepayCost,a.balance_no as balanceNo FROM T_INPATIENT_BALANCEHEAD_NOW a ";	
		 sql += " LEFT JOIN T_INPATIENT_INFO_NOW d on d.inpatient_no = a.inpatient_no LEFT JOIN T_BUSINESS_CONTRACTUNIT e on e.UNIT_CODE = a.pact_code ";
		 sql = sql+" where a.INVOICE_NO like '%"+invoiceRepPrintVo.getInvoiceNo()+"' and a.stop_flg = 0 and a.del_flg = 0 and a.trans_type = 1";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
							.addScalar("invoiceNo")
							.addScalar("balanceDate",Hibernate.TIMESTAMP)														
							.addScalar("paykindName")
							.addScalar("balanceOperName")
							.addScalar("patientName")
							.addScalar("totCost",Hibernate.DOUBLE)
							.addScalar("ownCost",Hibernate.DOUBLE)
							.addScalar("payCost",Hibernate.DOUBLE)
							.addScalar("pubCost",Hibernate.DOUBLE)							
							.addScalar("patientName")
							.addScalar("pactName")
							.addScalar("deptName")
							.addScalar("nurseCellName")							
							.addScalar("houseDocName")
							.addScalar("bedName")	
							.addScalar("reportBirthday",Hibernate.TIMESTAMP)	
							.addScalar("prepayCost",Hibernate.DOUBLE)
							.addScalar("balanceNo",Hibernate.INTEGER);
		List<InvoiceRepPrintVo> invoiceRepPrintList=queryObject.setResultTransformer(Transformers.aliasToBean(InvoiceRepPrintVo.class)).list();
		if(invoiceRepPrintList!=null && invoiceRepPrintList.size()>0){
			return invoiceRepPrintList;
		}		
		return new ArrayList<InvoiceRepPrintVo>();
		}

	@Override
	public List<InpatientBalanceListNow> queryBalanceList(InpatientBalanceList inpatientBalanceList) {
		String hql = "from InpatientBalanceListNow t where t.stop_flg = 0 and t.del_flg = 0 and t.invoiceNo = '"+inpatientBalanceList.getInvoiceNo()+"' and t.balanceNo = '"+inpatientBalanceList.getBalanceNo()+"'";
		List<InpatientBalanceListNow> balanceList = this.getSession().createQuery(hql).list();
		if(balanceList!=null && balanceList.size()>0){
			return balanceList;
		}
		return new ArrayList<InpatientBalanceListNow>();
	}
	
}
