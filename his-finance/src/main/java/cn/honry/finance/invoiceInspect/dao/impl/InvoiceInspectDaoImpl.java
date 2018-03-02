package cn.honry.finance.invoiceInspect.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.invoiceInspect.dao.InvoiceInspectDao;
import cn.honry.finance.invoiceInspect.vo.InvoiceInspectVo;
import cn.honry.utils.HisParameters;
@Repository("invoiceInspectDao")
@SuppressWarnings({ "all" })
public class InvoiceInspectDaoImpl extends HibernateEntityDao<FinanceInvoiceInfoNow> implements InvoiceInspectDao {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<InvoiceInspectVo> queryInvoiceInfoList(Date beginTime,
			Date endTime, String balanceOpcd, String encode,String page,String rows) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		String s = getSql(beginTime, endTime, balanceOpcd, encode);
		if("01".equals(encode)){
					SQLQuery queryObject = this.getSession().createSQLQuery(s);
					queryObject.addScalar("id").addScalar("invoiceNo")
					.addScalar("invoiceSeq")
					.addScalar("totCost", Hibernate.DOUBLE)
					.addScalar("transType", Hibernate.INTEGER)
					.addScalar("operDate", Hibernate.DATE)
					.addScalar("cancelFlag",Hibernate.INTEGER);
					List<FinanceInvoiceInfoNow> bii=queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(FinanceInvoiceInfoNow.class)).list();
					if(bii!=null&&bii.size()>0){
						List<InvoiceInspectVo> iisVo=new ArrayList<InvoiceInspectVo>();
						for(FinanceInvoiceInfoNow businessInvoiceInfo :bii){
							InvoiceInspectVo invoiceInspectVo= new InvoiceInspectVo();
							invoiceInspectVo.setMainId(businessInvoiceInfo.getId());
							invoiceInspectVo.setCheckFlag(businessInvoiceInfo.getCheckFlag());
							invoiceInspectVo.setCancelFlag(businessInvoiceInfo.getCancelFlag());
							invoiceInspectVo.setInvoiceNo(businessInvoiceInfo.getInvoiceNo());
							invoiceInspectVo.setOperDate(businessInvoiceInfo.getOperDate());
							invoiceInspectVo.setTotCost(businessInvoiceInfo.getTotCost());
							iisVo.add(invoiceInspectVo);
						}
						return iisVo;
					}
					return new ArrayList<InvoiceInspectVo>();
		}else if("03".equals(encode)){
			SQLQuery queryObject = this.getSession().createSQLQuery(s);
			queryObject.addScalar("id").addScalar("invoiceNo")
						.addScalar("transType",Hibernate.INTEGER)
						.addScalar("totCost",Hibernate.DOUBLE)
						.addScalar("balanceDate",Hibernate.DATE);
			List<InpatientBalanceHead> ibh=queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(InpatientBalanceHead.class)).list();
			if(ibh!=null&&ibh.size()>0){
				List<InvoiceInspectVo> iisVo03=new ArrayList<InvoiceInspectVo>();
				for(InpatientBalanceHead inpatientBalanceHead :ibh){
					InvoiceInspectVo invoiceInspectVo= new InvoiceInspectVo();
					invoiceInspectVo.setMainId(inpatientBalanceHead.getId());
					invoiceInspectVo.setCheckFlag(inpatientBalanceHead.getCheckFlag());
					invoiceInspectVo.setCancelFlag(inpatientBalanceHead.getTransType());
					invoiceInspectVo.setInvoiceNo(inpatientBalanceHead.getInvoiceNo());
					invoiceInspectVo.setOperDate(inpatientBalanceHead.getBalanceDate());
					invoiceInspectVo.setTotCost(inpatientBalanceHead.getTotCost());
					iisVo03.add(invoiceInspectVo);
				}
				return iisVo03;
			}
			return new ArrayList<InvoiceInspectVo>();
		}else if("02".equals(encode)){
			return new ArrayList<InvoiceInspectVo>();
		}else if("04".equals(encode)){
			return new ArrayList<InvoiceInspectVo>();
		}else if("05".equals(encode)){
			return new ArrayList<InvoiceInspectVo>();
		}
		return new ArrayList<InvoiceInspectVo>();
	}
    public String getSql(Date beginTime,Date endTime, String balanceOpcd, String encode) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		StringBuilder sql = null;
		if("01".equals(encode)){
			sql= new StringBuilder();
			sql.append("select a.invoice_id as id,a.invoice_no as invoiceNo,a.invoice_seq as invoiceSeq,a.tot_cost as totCost,a.trans_type as transType,a.oper_date as operDate, "
					+ "a.cancel_flag as cancelFlag "
					+ "from "
					+ "(select invoice_id,invoice_no, trans_type, invoice_seq, cancel_flag, tot_cost  ,oper_date "
					+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_FINANCE_INVOICEINFO_NOW where balance_flag = '1' and (check_flag = '0' or check_flag is null) ");
					if(!"1".equals(balanceOpcd)){
						sql.append("and balance_opcd='"+balanceOpcd+"'");
					}
					if(beginTime!=null){
						String bgTime=sdf.format(beginTime);
						sql.append("and balance_date >=to_date('"+bgTime+"','yyyy/MM/dd hh24:mi:ss') ");
					}
					if(endTime!=null){
						String edTime=sdf.format(endTime);
						sql.append("and balance_date<=to_date('"+edTime+"','yyyy/MM/dd hh24:mi:ss')");
					}
					sql.append("  and ((cancel_flag = '0' and trans_type = '2') or (cancel_flag = '1' and trans_type = '1')or (cancel_flag = '2' and trans_type = '2') or (cancel_flag = '3' and trans_type = '1'))");
					sql.append( ") a order by a.invoice_no");

		}else if("03".equals(encode)){
			sql= new StringBuilder();
			sql.append("select a.id as id,a.invoice_no as invoiceNo,max(a.trans_type) as transType,sum(a.tot_cost) as totCost,a.balance_date as balanceDate "
					+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_BALANCEHEAD_NOW a "
					+ "where a.daybalance_flag = '1' and (a.check_flag = '0' or a.check_flag is null) ");
					if(!"1".equals(balanceOpcd)){
						sql.append("and a.daybalance_opcd='"+balanceOpcd+"'");
					}
					if(beginTime!=null){
						String bgTime=sdf.format(beginTime);
						sql.append("and a.daybalance_date >=to_date('"+bgTime+"','yyyy/MM/dd hh24:mi:ss') ");
					}
					if(endTime!=null){
						String edTime=sdf.format(endTime);
						sql.append("and a.daybalance_date<=to_date('"+edTime+"','yyyy/MM/dd hh24:mi:ss')");
					}
					sql.append(" group by a.id,a.invoice_no,a.balance_date");
		}else if("02".equals(encode)){
			return null;
		}else if("04".equals(encode)){
			return null;
		}else if("05".equals(encode)){
			return null;
		}
		return sql.toString();

    }
	@Override
	public List<SysEmployee> queryBalanceOpcd(String q)  throws Exception{
		String sql="select e.EMPLOYEE_JOBNO as jobNo,e.EMPLOYEE_NAME as name from T_EMPLOYEE e where e.del_flg=0 and e.stop_flg=0 and e.EMPLOYEE_TYPE='3'";
		if(StringUtils.isNotBlank(q)){
			sql = sql+" AND (e.EMPLOYEE_NAME LIKE :name OR e.EMPLOYEE_JOBNO LIKE :name OR EMPLOYEE_OLDNAME LIKE :name OR EMPLOYEE_PINYIN LIKE :name OR EMPLOYEE_WB LIKE :name OR EMPLOYEE_INPUTCODE LIKE :name)";
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("jobNo").addScalar("name");
		if(StringUtils.isNotBlank(q)){
			queryObject.setParameter("name", "%"+q+"%");
		}
		List<SysEmployee> emplist=queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(emplist!=null&&emplist.size()>0){
			SysEmployee sysEmp=new SysEmployee();
			//下拉框中添加全部选项
			sysEmp.setJobNo("1");
			sysEmp.setName("全部");
			emplist.add(0, sysEmp);
			return emplist;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public void saveDatagrid(List arrlist, String intype,String eid)  throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String dateString=sdf.format(new Date());
		if("01".equals(intype)){
			for(int i =0;i<arrlist.size();i++){
				String sql=" update "+HisParameters.HISPARSCHEMAHISUSER+"T_FINANCE_INVOICEINFO_NOW set check_flag=1,check_opcd='"+eid+"',check_date=to_date('"+dateString+"','yyyy/MM/dd hh24:mi:ss') where invoice_id='"+arrlist.get(i)+"' ";
				this.getSession().createSQLQuery(sql).executeUpdate();
			}
		}else if("03".equals(intype)){
			for(int i =0;i<arrlist.size();i++){
				String sql=" update "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_balancehead_now set check_flag=1,check_opcd='"+eid+"',check_date=to_date('"+dateString+"','yyyy/MM/dd hh24:mi:ss') where id='"+arrlist.get(i)+"'";
				this.getSession().createSQLQuery(sql).executeUpdate();
			}
		}
	}

	@Override
	public int getTotal(Date beginTime, Date endTime, String balanceOpcd, String encode) throws Exception {
		int total=0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String s = "select count(1) from( "+getSql(beginTime, endTime, balanceOpcd, encode)+")";
		if("01".equals(encode)){
			 total= jdbcTemplate.queryForObject(s, java.lang.Integer.class);
					
		}else if("03".equals(encode)){
			 total= jdbcTemplate.queryForObject(s, java.lang.Integer.class);
			
		}else if("02".equals(encode)){
			return 0;
		}else if("04".equals(encode)){
			return 0;
		}else if("05".equals(encode)){
			return 0;
		}
		return total;
	
	}
}
