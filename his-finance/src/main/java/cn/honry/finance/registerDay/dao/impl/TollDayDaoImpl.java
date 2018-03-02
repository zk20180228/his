package cn.honry.finance.registerDay.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.registerDay.dao.TollDayDao;
import cn.honry.finance.registerDay.vo.DayBalanceVO;
import cn.honry.finance.registerDay.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("tollDayDao")
@SuppressWarnings({ "all" })
public class TollDayDaoImpl extends HibernateEntityDao<OutpatientDaybalance> implements TollDayDao {
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public Date getBeginDate(String userid) {
		String s = "from OutpatientDaybalance t where t.del_flg = 0 and t.stop_flg = 0"
				+ " and t.operCode = ? order by t.operDate desc";
		List<OutpatientDaybalance> list1 = super.find(s, userid);
		if(list1 != null && list1.size() > 0){
			return list1.get(0).getCreateTime();
		}else{
			s = "from FinanceInvoiceInfoNow t where t.del_flg = 0 and t.stop_flg = 0 and t.createUser = ? order by t.createTime Asc";
			List<FinanceInvoiceInfoNow> list2 = super.find(s, userid);
			if(list2 != null && list2.size() > 0){
				return list2.get(0).getCreateTime();
			}else{
				return DateUtils.parseDateY_M_D_H_M_S(DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+ " 00:00:00");
			}
		}
	}

	@Override
	public List<InfoVo> queryInvoiceinfo(String userid, Date beginDate, Date endDate) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("select i.invoice_no AS invoice, ");
		sBuffer.append("sum(nvl(decode(p.cancel_flag,'3',0,i.REAL_COST),0)) AS realCost, ");//实收金额
		sBuffer.append("sum(nvl(decode(p.cancel_flag,'3',0,i.PUB_COST),0)) AS pubCost, ");//记帐金额
		sBuffer.append("i.TRANS_TYPE AS transType, ");//正反交易
		sBuffer.append("i.cancel_flag AS cancel, ");//日结项目：'0'-正常/'1'-作废/'2'-重打/'3'-取消 
		sBuffer.append("i.EXT_FLAG AS extFlag, ");//自费记帐特殊：1 自费 2 记帐 3 特殊
		sBuffer.append("sum((nvl(decode(i.cancel_flag,'3',0,i.own_cost),0) + nvl(decode(i.cancel_flag,'3',0,i.pay_cost),0))) AS totalNum ");//合计
		sBuffer.append("from ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_FINANCE_INVOICEINFO_NOW i,").append(HisParameters.HISPARSCHEMAHISUSER).append("t_Business_Paymode_now p ");
		sBuffer.append("where p.invoice_no = i.invoice_no ");
		sBuffer.append("AND i.balance_flag = 0 ");
		sBuffer.append("AND p.balance_flag = 0 ");
		sBuffer.append("AND i.oper_code = '").append(userid).append("' ");
		sBuffer.append("AND p.oper_code = '").append(userid).append("' ");
		sBuffer.append("AND i.oper_date > TO_DATE('").append(DateUtils.formatDateY_M_D_H_M_S(beginDate)).append("','YYYY-MM-DD HH24:MI:SS') ");
		sBuffer.append("AND i.oper_date <= TO_DATE('").append(DateUtils.formatDateY_M_D_H_M_S(endDate)).append("','YYYY-MM-DD HH24:MI:SS') ");
		sBuffer.append("GROUP BY i.invoice_no,i.PUB_COST,i.own_cost,i.pay_cost,i.cancel_flag,i.EXT_FLAG,i.TRANS_TYPE ");
		sBuffer.append("ORDER BY i.invoice_no");
		
		SQLQuery queryObject = this.getSession().createSQLQuery(sBuffer.toString())
				.addScalar("invoice")
				.addScalar("realCost",Hibernate.DOUBLE)
				.addScalar("pubCost",Hibernate.DOUBLE)
				.addScalar("transType",Hibernate.INTEGER)
				.addScalar("cancel",Hibernate.INTEGER)
				.addScalar("extFlag",Hibernate.INTEGER)
				.addScalar("totalNum",Hibernate.DOUBLE);
		List<InfoVo> voList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoVo.class)).list();
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	@Override
	public int updateOutFeedetail(String userid, Date beginDate, Date endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Update t_Outpatient_Feedetail_Now Set Ext_Flag2 = 1 Where Invoice_No In ");
		sb.append(" (Select i.Invoice_No From t_Finance_Invoiceinfo_Now i, t_Business_Paymode_Now p ");
		sb.append(" Where p.Invoice_Seq = i.Invoice_Seq And i.Balance_Flag = 0 And p.Balance_Flag = 0 ");
		sb.append(" And i.Oper_Code = :userid And p.Oper_Code = :userid And i.Oper_Date > To_Date(:beginDate, 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" And i.Oper_Date <= To_Date(:endDate, 'YYYY-MM-DD HH24:MI:SS')) ");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.setParameter("userid", userid);
		query.setParameter("beginDate", DateUtils.formatDateY_M_D_H_M_S(beginDate));
		query.setParameter("endDate", DateUtils.formatDateY_M_D_H_M_S(endDate));
		return query.executeUpdate();
	}

	@Override
	public int updateInvoiceInfo(String userName, String dayBalanceNo, String userid,Date beginDate, Date endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Update T_FINANCE_INVOICEINFO_NOW Set BALANCE_FLAG = 1,BALANCE_OPCD_NAME = :userName,BALANCE_OPCD = :userid,BALANCE_NO = :dayBalanceNo,BALANCE_DATE = :nowTime Where Invoice_No In ");
		sb.append(" (Select i.Invoice_No From t_Finance_Invoiceinfo_Now i, t_Business_Paymode_Now p ");
		sb.append(" Where p.Invoice_Seq = i.Invoice_Seq And i.Balance_Flag = 0 And p.Balance_Flag = 0 ");
		sb.append(" And i.Oper_Code = :userid And p.Oper_Code = :userid And i.Oper_Date > To_Date(:beginDate, 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" And i.Oper_Date <= To_Date(:endDate, 'YYYY-MM-DD HH24:MI:SS')) ");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.setParameter("userid", userid);
		query.setParameter("beginDate", DateUtils.formatDateY_M_D_H_M_S(beginDate));
		query.setParameter("endDate", DateUtils.formatDateY_M_D_H_M_S(endDate));
		query.setParameter("dayBalanceNo", dayBalanceNo);
		query.setParameter("nowTime", new Date());
		query.setParameter("userName", userName);
		return query.executeUpdate();
	}

	@Override
	public int updateInvoicedetail(String userName,String dayBalanceNo, String userid,Date beginDate, Date endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Update T_FINANCE_INVOICEDETAIL_NOW t Set t.BALANCE_FLAG = 1,t.BALANCE_OPCD_NAME = :userName,t.BALANCE_OPCD = :userid,t.BALANCE_NO = :dayBalanceNo,t.BALANCE_DATE = :nowTime Where t.INVOICE_NO In ");
		sb.append(" (Select i.Invoice_No From t_Finance_Invoiceinfo_Now i, t_Business_Paymode_Now p ");
		sb.append(" Where p.Invoice_Seq = i.Invoice_Seq And i.Balance_Flag = 0 And p.Balance_Flag = 0 ");
		sb.append(" And i.Oper_Code = :userid And p.Oper_Code = :userid And i.Oper_Date > To_Date(:beginDate, 'YYYY-MM-DD HH24:MI:SS') ");
		sb.append(" And i.Oper_Date <= To_Date(:endDate, 'YYYY-MM-DD HH24:MI:SS')) ");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.setParameter("userid", userid);
		query.setParameter("beginDate", DateUtils.formatDateY_M_D_H_M_S(beginDate));
		query.setParameter("endDate", DateUtils.formatDateY_M_D_H_M_S(endDate));
		query.setParameter("dayBalanceNo", dayBalanceNo);
		query.setParameter("nowTime", new Date());
		query.setParameter("userName", userName);
		return query.executeUpdate();
	}

	@Override
	public int updatePaymodeNow(String userName, String dayBalanceNo,String userid, Date beginDate, Date endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Update T_BUSINESS_PAYMODE_NOW t set t.BALANCE_FLAG = 1,t.BALANCE_OPCD_NAME = :userName,t.BALANCE_OPCD = :userid,t.BALANCE_NO = :dayBalanceNo,t.BALANCE_DATE = :nowTime ");
		sb.append(" where t.Balance_Flag = 0 And t.Oper_Code = :userid and t.OPER_DATE > To_Date(:beginDate, 'YYYY-MM-DD HH24:MI:SS') and t.OPER_DATE <= To_Date(:endDate, 'YYYY-MM-DD HH24:MI:SS')");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.setParameter("userid", userid);
		query.setParameter("beginDate", DateUtils.formatDateY_M_D_H_M_S(beginDate));
		query.setParameter("endDate", DateUtils.formatDateY_M_D_H_M_S(endDate));
		query.setParameter("dayBalanceNo", dayBalanceNo);
		query.setParameter("nowTime", new Date());
		query.setParameter("userName", userName);
		return query.executeUpdate();
	}

	@Override
	public List<DayBalanceVO> getBalance(String code, String startDate,String endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append(" Select a.Paykind_Code payKindCode,a.Paykind_Name payKindName,Nvl(Sum(a.Own_Cost),0) totalCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '03' And a.Pub_Cost = 0 Then a.Own_Cost End),0) sybOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '03' And a.Pub_Cost = 0 Then a.Pay_Cost + a.Pub_Cost End), 0) sybPubCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '03' And a.Pub_Cost != 0 Then a.Own_Cost End),0) smxbOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '03' And a.Pub_Cost != 0 Then a.Pay_Cost  End),0) smxbPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '03' And a.Pub_Cost != 0 Then a.Pub_Cost End),0) smxbPubCost,");
		sb.append(" Nvl(Sum(Case When a.Pact = '02' Then a.Own_Cost End), 0) slxOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '02' Then a.Pay_Cost + a.Pub_Cost End),0) slxPayCost,");
		sb.append(" Nvl(Sum(Case When a.Pact = '05' Then a.Own_Cost End),0) shiybOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '05' Then a.Pay_Cost + a.Pub_Cost  End),0) shiybPubCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact In ('01', '15') Then a.Own_Cost End),0) ownCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '07' Then a.Own_Cost End),0) ssyOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '07' Then a.Pay_Cost End),0) ssyPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '07' Then a.Pub_Cost End),0) ssyPuvCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '19' Then a.Own_Cost End),0) nhOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '19' Then a.Pay_Cost End),0) nhPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '19' Then a.Pub_Cost End),0) nhPubCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '20' Then a.Own_Cost End),0) nyhgOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '20' Then a.Pub_Cost End),0) nyhgPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '20' Then a.Pub_Cost End),0) nyhgPubCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '10' Then a.Own_Cost End),0) sbjjbxOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '10' Then a.Pub_Cost End),0) sbjjbxPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '10' Then a.Pub_Cost End),0) sbjjbxPubCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '13' Then a.Own_Cost End),0) sjxnhptOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '13' Then a.Pub_Cost End),0) sjxnhptPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '13' Then a.Pub_Cost End),0) sjxnhptPubCost,  ");
		sb.append(" Nvl(Sum(Case When a.Pact = '90' Then a.Own_Cost End),0) ydjysdOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '90' Then a.Pub_Cost End),0) ydjysdPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '90' Then a.Pub_Cost End),0) ydjysdPubCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '08' Then a.Own_Cost End),0) stlylbxOwnCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '08' Then a.Pub_Cost End),0) stlylbxPayCost, ");
		sb.append(" Nvl(Sum(Case When a.Pact = '08' Then a.Pub_Cost End),0) stlylbxPubCost ");
		sb.append(" From (Select t.Invoice_No, ");
		sb.append(" (Select Info.Pact_Code From t_Finance_Invoiceinfo_Now Info Where Info.Invoice_No = t.Invoice_No ");
		sb.append(" And Info.Trans_Type = t.Trans_Type And Info.Invoice_Seq = t.Invoice_Seq) Pact, ");
		sb.append(" t.Paykind_Code,t.Paykind_Name,t.Own_Cost,t.Pay_Cost,t.Pub_Cost ");
		sb.append(" From t_Finance_Invoiceinfo_Now t Where t.Balance_Flag = '0' And t.trans_type = 1 And t.CANCEL_FLAG = '1' And t.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" And t.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')  And t.Oper_Code = :code ");
		sb.append(" And t.Invoice_No In (Select t.Invoice_No From t_Outpatient_Feedetail_Now t Where t.Fee_Cpcd = :code  And t.CANCEL_FLAG = 1 "
				+ " And t.EXT_FLAG2 = 0 And t.TRANS_TYPE = 1 And t.Account_Flag = '0' ");
		sb.append(" And t.Pay_Flag = 1 And t.Fee_Date Between To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And  To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss'))");
		sb.append(" And t.Invoice_Seq In (Select t.Invoice_Seq From t_Outpatient_Feedetail_Now t Where t.Fee_Cpcd = :code And t.Account_Flag = '0'  And t.CANCEL_FLAG = 1 And t.EXT_FLAG2 = 0 And t.TRANS_TYPE = 1");
		sb.append(" And t.Pay_Flag = 1 And t.Fee_Date Between To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')) ");
		sb.append(" Union All ");
		sb.append(" Select '' As Invoice_No,'01' As Pact,z.Fee_Stat_Cate As Paykind_Code,z.Fee_Stat_Name As Paykind_Name,y.Own_Cost As Own_Cost, ");
		sb.append(" 0 As Pay_Cost,0 As Pub_Cost From t_Outpatient_Feedetail_Now y,Fin_Com_Feecodestat z,t_Register_Main_Now x ");
		sb.append(" Where y.Clinic_Code = x.Clinic_Code And y.Fee_Code = z.Fee_Code And y.Account_Flag = '1' And y.Pay_Flag = 1 ");
		sb.append(" And z.Report_Code = 'MZ01' And y.Fee_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss')  And y.Fee_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" And x.Trans_Type = 1 And y.FEE_CPCD = :code  And y.CANCEL_FLAG = 1 And y.EXT_FLAG2 = 0 And y.TRANS_TYPE = 1 ");
		sb.append(" ) a Group By a.Paykind_Code, a.Paykind_Name Order By a.Paykind_Code ");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("code", code);
		query.addScalar("payKindCode");
		query.addScalar("payKindName");
		query.addScalar("totalCost", Hibernate.DOUBLE).addScalar("sybOwnCost", Hibernate.DOUBLE).addScalar("sybPubCost", Hibernate.DOUBLE);
		query.addScalar("smxbOwnCost", Hibernate.DOUBLE).addScalar("smxbPayCost", Hibernate.DOUBLE).addScalar("smxbPubCost", Hibernate.DOUBLE);
		query.addScalar("slxOwnCost", Hibernate.DOUBLE).addScalar("slxPayCost", Hibernate.DOUBLE);
		query.addScalar("shiybOwnCost", Hibernate.DOUBLE).addScalar("shiybPubCost", Hibernate.DOUBLE).addScalar("ownCost",Hibernate.DOUBLE);
		query.addScalar("ssyOwnCost", Hibernate.DOUBLE).addScalar("ssyPayCost", Hibernate.DOUBLE).addScalar("ssyPuvCost",Hibernate.DOUBLE);
		query.addScalar("nhOwnCost", Hibernate.DOUBLE).addScalar("nhPayCost", Hibernate.DOUBLE).addScalar("nhPubCost",Hibernate.DOUBLE);
		query.addScalar("nyhgOwnCost", Hibernate.DOUBLE).addScalar("nyhgPayCost", Hibernate.DOUBLE).addScalar("nyhgPubCost",Hibernate.DOUBLE);
		query.addScalar("sbjjbxOwnCost", Hibernate.DOUBLE).addScalar("sbjjbxPayCost", Hibernate.DOUBLE).addScalar("sbjjbxPubCost",Hibernate.DOUBLE);
		query.addScalar("sjxnhptOwnCost", Hibernate.DOUBLE).addScalar("sjxnhptPayCost", Hibernate.DOUBLE).addScalar("sjxnhptPubCost",Hibernate.DOUBLE);
		query.addScalar("ydjysdOwnCost", Hibernate.DOUBLE).addScalar("ydjysdPayCost", Hibernate.DOUBLE).addScalar("ydjysdPubCost",Hibernate.DOUBLE);
		query.addScalar("stlylbxOwnCost", Hibernate.DOUBLE).addScalar("stlylbxPayCost", Hibernate.DOUBLE).addScalar("stlylbxPubCost",Hibernate.DOUBLE);
		List<DayBalanceVO> list = query.setResultTransformer(Transformers.aliasToBean(DayBalanceVO.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DayBalanceVO>();
	}

	@Override
	public OutpatientDaybalance getOutDayBlance(final String code, final String startDate,final String endDate) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select Sum(Tot) totCost,Sum(Ca) caCost,Sum(Db) cdCost,Sum(Ch) chCost,Sum(Ys) ysCost,Sum(Incoicenum) balanceInvoiceNum,Sum(Backinvoicenum) backInvoiceNum, ");
		sb.append(" Sum(Cancel) cancelInvoiceNum,Sum(Backysdrug) backysdrug,Sum(Backfee) backFee,Sum(Backdrug) backDrug,Sum(Backundrug) backUndrug,Sum(Backysundrug) backysundrug, ");
		sb.append(" Sum(YSPREPAYCA) ysPrepayca,Sum(YSPREPAYDB) ysPrepaydb,Sum(YSPAY) ysPay,Sum(balanceCost) ysBalanceCost ");
		sb.append(" From (Select Nvl(Sum(t.Own_Cost), 0) Tot, 0 Ca,0 Db,0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" From t_Finance_Invoicedetail_Now t Where t.Balance_Flag = '0' And t.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And t.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" And t.Oper_Code = :code And t.Invoice_No In (Select t.Invoice_No From t_Outpatient_Feedetail_Now t Where t.Fee_Cpcd = :code And t.Account_Flag = 0 And t.Pay_Flag = 1  ");
		sb.append(" And t.Fee_Date Between To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')) ");
		sb.append(" Union All ");
		sb.append(" Select 0 Tot,Nvl(Sum(Ca), 0) Ca,Nvl(Sum(Db), 0) Db,Nvl(Sum(Ch), 0) Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" From (Select Nvl(Decode(Pay.Mode_Code, 'CA', pay.TOT_COST), 0) Ca,Nvl(Decode(Pay.Mode_Code, 'DB', pay.TOT_COST), 0) Db,Nvl(Decode(Pay.Mode_Code, 'CH', pay.TOT_COST), 0) Ch ");
		sb.append(" From t_Business_Paymode_Now Pay Where Pay.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And Pay.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') And Pay.Oper_Code = :code And Pay.Balance_Flag = 0) ");
		sb.append(" Union All ");
		sb.append(" Select 0 Tot,0 Ca,0 Db,0 Ch,0 Ys,Count(Invoice_No) Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" From t_Finance_Invoiceinfo_Now Info Where Oper_Code = :code And Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append("  And Info.Trans_Type = 1 And Info.Cancel_Flag = 1 And Info.Balance_Flag = 0 ");
		sb.append(" Union All ");
		sb.append(" Select 0 Tot,0 Ca,0 Db,0 Ch,0 Ys,0 Incoicenum,Count(Invoice_No) Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" From t_Finance_Invoiceinfo_Now Info Where Oper_Code = :code And Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') And Info.Trans_Type = 2 And Info.Cancel_Flag = 0 ");
		sb.append(" Union All ");
		sb.append(" Select 0 Tot,0 Ca,0 Db,0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,Count(Invoice_No) Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" From t_Finance_Invoiceinfo_Now Info Where Oper_Code = :code And Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') And Info.Trans_Type = 2 And Info.Cancel_Flag = 0 ");
		sb.append(" Union All ");
		sb.append(" Select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,Nvl(Sum(t.Tot_Cost), 0) Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" From (Select Invoice_No, -tot_Cost As Tot_Cost, Oper_Code, Oper_Date From t_Finance_Invoiceinfo_Now a  Where a.Trans_Type = '2'  And a.Cancel_Flag = 0 And a.Account_Flag = 0 ");
		sb.append(" And (Select Count(*) From t_Finance_Invoiceinfo_Now b Where b.Cancel_Invoice = a.Invoice_No And b.Trans_Type = 1 And b.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And b.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')) = 0 ");
		sb.append(" And a.Oper_Code = :code And a.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And a.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" Union All ");
		sb.append(" Select Invoice_No, -tot_Cost As Tot_Cost, Oper_Code, Oper_Date From t_Finance_Invoiceinfo_Now c Where c.Trans_Type In ('1') And (c.Cancel_Invoice Is Not Null) And c.Account_Flag = 0 And c.Cancel_Flag In ('0', '2') ");
		sb.append(" And c.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And c.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" Union All ");
		sb.append(" Select Invoice_No, -tot_Cost As Tot_Cost, Oper_Code, Oper_Date From t_Finance_Invoiceinfo_Now d Where d.Trans_Type = 2 And d.Account_Flag = 0 And d.Invoice_No In (Select Distinct c.Cancel_Invoice From t_Finance_Invoiceinfo_Now c ");
		sb.append(" Where c.Trans_Type In ('1') And c.Cancel_Invoice = d.Invoice_No And (c.Cancel_Invoice Is Not Null) And c.Cancel_Flag In ('0', '2') And c.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And c.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')) ");
		sb.append(" And d.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And d.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" Union All ");
		sb.append(" Select Invoice_No, -tot_Cost As Tot_Cost, Oper_Code, Oper_Date From t_Finance_Invoiceinfo_Now c Where c.Trans_Type In ('1') And (c.Cancel_Invoice Is Not Null) And c.Cancel_Flag In ('1') And c.Account_Flag = 0 ");
		sb.append(" And c.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And c.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" Union All ");
		sb.append(" Select Invoice_No, -tot_Cost As Tot_Cost, Oper_Code, Oper_Date From t_Finance_Invoiceinfo_Now d Where d.Trans_Type = 2 And d.Account_Flag = 0 And d.Invoice_No In (Select Distinct c.Cancel_Invoice From t_Finance_Invoiceinfo_Now c ");
		sb.append(" Where c.Trans_Type In ('1') And c.Cancel_Invoice = d.Invoice_No And (c.Cancel_Invoice Is Not Null) And c.Account_Flag = 0 And c.Cancel_Flag In ('1') And c.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" And c.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')) And d.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss')  And d.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss')) t ");
		sb.append(" Where t.Oper_Code = :code And t.Oper_Date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') And t.Oper_Date < To_Date(:endDate, 'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" Union All ");
		sb.append(" SELECT	0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,nvl(SUM(T.TOT_COST), 0) BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
		sb.append(" FROM ( SELECT A.INVOICE_NO,- (AA.OWN_COST + AA.PAY_COST + AA.PUB_COST) AS TOT_COST,A.OPER_CODE,A.OPER_DATE FROM t_Finance_Invoiceinfo_Now A, T_OUTPATIENT_FEEDETAIL_NOW AA ");
		sb.append(" WHERE A.INVOICE_NO = AA.INVOICE_NO AND A.TRANS_TYPE = AA.TRANS_TYPE AND A.CANCEL_FLAG = AA.CANCEL_FLAG AND A.BALANCE_FLAG = 0 AND A.TRANS_TYPE = 2 AND A.CANCEL_FLAG = 0 ");
		sb.append(" AND (SELECT COUNT(*) FROM t_Finance_Invoiceinfo_Now B WHERE B.CANCEL_INVOICE = A.INVOICE_NO AND B.TRANS_TYPE = '1' AND b.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND b.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) = 0 ");
		sb.append(" AND A.OPER_CODE = :code AND (aa.ITEM_CODE IN ('F00000061054', 'F00000061055', 'F00000061056') or AA.DRUG_FLAG = '1') AND a.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND a.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT C.INVOICE_NO,- (BB.OWN_COST + BB.PUB_COST + BB.PAY_COST) AS TOT_COST,C.OPER_CODE,C.OPER_DATE FROM t_Finance_Invoiceinfo_Now C, T_OUTPATIENT_FEEDETAIL_NOW BB WHERE C.INVOICE_NO = BB.INVOICE_NO ");
		sb.append("  AND C.TRANS_TYPE = BB.TRANS_TYPE AND C.CANCEL_FLAG = BB.CANCEL_FLAG AND C.BALANCE_FLAG = 0 AND C.TRANS_TYPE IN ('1') AND (C.CANCEL_INVOICE IS NOT NULL) AND C.CANCEL_FLAG IN ('0', '2') ");
		sb.append(" AND (bb.ITEM_CODE IN ('F00000061054', 'F00000061055', 'F00000061056') or BB.DRUG_FLAG = 1) AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT D.INVOICE_NO,- (CC.OWN_COST + CC.PUB_COST + CC.PAY_COST) AS TOT_COST,D.OPER_CODE,D.OPER_DATE FROM t_Finance_Invoiceinfo_Now D, T_OUTPATIENT_FEEDETAIL_NOW CC WHERE D.INVOICE_NO = CC.INVOICE_NO ");
		sb.append(" AND D.CANCEL_FLAG = CC.CANCEL_FLAG AND D.TRANS_TYPE = CC.TRANS_TYPE AND D.BALANCE_FLAG = 0  AND D.TRANS_TYPE = '2' AND (cc.ITEM_CODE IN ('F00000061054', 'F00000061055', 'F00000061056') or CC.DRUG_FLAG = '1') ");
		sb.append(" AND D.INVOICE_NO IN (SELECT DISTINCT C.CANCEL_INVOICE FROM t_Finance_Invoiceinfo_Now C WHERE C.TRANS_TYPE IN ('1') AND C.CANCEL_INVOICE = D.INVOICE_NO AND (C.CANCEL_INVOICE IS NOT NULL) ");
		sb.append(" AND C.CANCEL_FLAG IN ('0', '2') AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) AND d.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND d.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT C.INVOICE_NO,- (DD.OWN_COST + DD.PUB_COST + DD.PAY_COST) AS TOT_COST,C.OPER_CODE,C.OPER_DATE FROM t_Finance_Invoiceinfo_Now C, T_OUTPATIENT_FEEDETAIL_NOW DD WHERE C.INVOICE_NO = DD.INVOICE_NO ");
		sb.append(" AND C.TRANS_TYPE = DD.TRANS_TYPE AND C.CANCEL_FLAG = DD.CANCEL_FLAG AND C.BALANCE_FLAG = 0  AND C.TRANS_TYPE IN ('1') AND (C.CANCEL_INVOICE IS NOT NULL) AND C.CANCEL_FLAG IN ('1') ");
		sb.append(" AND (dd.ITEM_CODE IN ('F00000061054', 'F00000061055', 'F00000061056') or DD.DRUG_FLAG = '1') AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT D.INVOICE_NO,- (EE.OWN_COST + EE.PUB_COST + EE.PAY_COST) AS TOT_COST,D.OPER_CODE,D.OPER_DATE FROM t_Finance_Invoiceinfo_Now D, T_OUTPATIENT_FEEDETAIL_NOW EE WHERE D.INVOICE_NO = EE.INVOICE_NO ");
		sb.append(" AND D.CANCEL_FLAG = EE.CANCEL_FLAG AND D.TRANS_TYPE = EE.TRANS_TYPE AND D.BALANCE_FLAG = 0 AND (ee.ITEM_CODE IN ('F00000061054', 'F00000061055', 'F00000061056') or EE.DRUG_FLAG = '1') ");
		sb.append(" AND D.INVOICE_NO IN (SELECT DISTINCT C.CANCEL_INVOICE FROM t_Finance_Invoiceinfo_Now C WHERE C.TRANS_TYPE IN ('1') AND C.CANCEL_INVOICE = D.INVOICE_NO AND (C.CANCEL_INVOICE IS NOT NULL) ");
		sb.append(" AND C.CANCEL_FLAG IN ('1') AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) AND d.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" AND d.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ) T  WHERE T.OPER_CODE = :code AND T.OPER_DATE >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND T.OPER_DATE < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
		sb.append(" Union All ");
		sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,-nvl(sum(cost), 0) BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
        sb.append(" from (select nvl(sum(own_cost), 0) cost from T_OUTPATIENT_FEEDETAIL_NOW a where a.trans_type = 2 and a.cancel_flag = 0 and a.account_flag = 1  and a.drug_flag = 1 ");
        sb.append(" and a.fee_cpcd = :code and a.fee_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and a.fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" union all ");
        sb.append(" select nvl(sum(own_cost), 0) cost from T_OUTPATIENT_FEEDETAIL_NOW b,(select distinct (a.recipe_no || a.sequence_no) key from T_OUTPATIENT_FEEDETAIL_NOW a where a.trans_type = 2 and a.cancel_flag = 0 and a.account_flag = 1 ");
        sb.append(" and a.drug_flag = 1 and a.fee_cpcd = :code and a.fee_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and a.fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) f where (b.recipe_no || b.sequence_no) = f.key and b.account_flag = 1 ");
        sb.append(" and b.trans_type = 1 and b.drug_flag = '1' and b.fee_cpcd = :code and b.fee_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and b.fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) ");
        sb.append(" Union All ");
        sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,nvl(sum(t.tot_cost), 0) BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
        sb.append(" from (select a.invoice_no,- (aa.OWN_COST + aa.PUB_COST + aa.PUB_COST) as tot_cost,a.oper_code,a.oper_date from t_Finance_Invoiceinfo_Now a, T_OUTPATIENT_FEEDETAIL_NOW aa ");
		sb.append(" where a.INVOICE_NO = aa.INVOICE_NO AND a.TRANS_TYPE = aa.TRANS_TYPE AND a.CANCEL_FLAG = aa.CANCEL_FLAG AND a.BALANCE_FLAG = 0 AND a.trans_type = 2 and a.cancel_flag = 0 and a.account_flag = 0 ");
        sb.append(" and a.cancel_flag = '0' and a.account_flag = '0' and (select count(*) from t_Finance_Invoiceinfo_Now b where b.cancel_invoice =a.invoice_no and b.trans_type = 1 ");
        sb.append(" AND b.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND b.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) = 0 and a.oper_code = :code ");
        sb.append(" AND (aa.ITEM_CODE NOT IN ('F00000061054', 'F00000061055', 'F00000061056') and DRUG_FLAG = 0) AND a.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND a.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" union all ");
        sb.append(" select c.invoice_no,- (bb.OWN_COST + bb.PUB_COST + bb.PAY_COST) as tot_cost,c.oper_code,c.oper_date from t_Finance_Invoiceinfo_Now c, T_OUTPATIENT_FEEDETAIL_NOW bb ");
        sb.append(" where c.INVOICE_NO = bb.INVOICE_NO AND c.TRANS_TYPE = bb.TRANS_TYPE AND c.CANCEL_FLAG = bb.CANCEL_FLAG AND c.BALANCE_FLAG = 0 ");
        sb.append(" and c.account_flag = '0' AND c.trans_type in ('1')  and (c.cancel_invoice is not null) and c.cancel_flag in ('0', '2') AND (bb.ITEM_CODE NOT IN ('F00000061054', 'F00000061055', 'F00000061056') and DRUG_FLAG = 0) ");
        sb.append(" AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" union all ");
        sb.append(" select d.invoice_no,- (cc.OWN_COST + cc.PUB_COST + cc.PAY_COST) as tot_cost,d.oper_code,d.oper_date from t_Finance_Invoiceinfo_Now d, T_OUTPATIENT_FEEDETAIL_NOW cc ");
        sb.append(" where d.INVOICE_NO = cc.INVOICE_NO AND d.CANCEL_FLAG = cc.CANCEL_FLAG AND d.TRANS_TYPE = cc.TRANS_TYPE AND d.BALANCE_FLAG = 0 AND d.trans_type = 2 and d.account_flag = 0 ");
        sb.append(" AND (cc.ITEM_CODE NOT IN ('F00000061054', 'F00000061055', 'F00000061056') and DRUG_FLAG = '0') and d.invoice_no in ");
        sb.append(" (select distinct c.cancel_invoice from t_Finance_Invoiceinfo_Now c where c.trans_type in ('1') and c.cancel_invoice = d.invoice_no and (c.cancel_invoice is not null) and c.cancel_flag in ('0', '2') ");
        sb.append(" AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) AND d.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND d.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" union all ");
        sb.append(" select c.invoice_no,- (dd.OWN_COST + dd.PUB_COST + dd.PAY_COST) as tot_cost,c.oper_code,c.oper_date from t_Finance_Invoiceinfo_Now c, T_OUTPATIENT_FEEDETAIL_NOW dd ");
        sb.append(" where c.INVOICE_NO = dd.INVOICE_NO AND c.TRANS_TYPE = dd.TRANS_TYPE AND c.CANCEL_FLAG = dd.CANCEL_FLAG AND c.BALANCE_FLAG = 0 and c.account_flag = '0' AND c.trans_type in ('1') and (c.cancel_invoice is not null) and c.cancel_flag in ('1') ");
        sb.append(" AND (dd.ITEM_CODE NOT IN ('F00000061054', 'F00000061055', 'F00000061056') and DRUG_FLAG = '0') AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" union all ");
        sb.append(" select d.invoice_no, - (ee.OWN_COST + ee.PUB_COST + ee.PAY_COST) as tot_cost,d.oper_code,d.oper_date from t_Finance_Invoiceinfo_Now d, T_OUTPATIENT_FEEDETAIL_NOW ee ");
        sb.append(" where d.INVOICE_NO = ee.INVOICE_NO AND d.CANCEL_FLAG = ee.CANCEL_FLAG AND d.TRANS_TYPE = ee.TRANS_TYPE AND d.BALANCE_FLAG = 0 AND d.trans_type = 2 and d.account_flag = 0");
        sb.append(" AND (ee.ITEM_CODE NOT IN ('F00000061054', 'F00000061055', 'F00000061056') and DRUG_FLAG = '0') and d.invoice_no in (select distinct c.cancel_invoice from t_Finance_Invoiceinfo_Now c ");
        sb.append(" where c.trans_type in ('1') and c.cancel_invoice = d.invoice_no and (c.cancel_invoice is not null) and c.cancel_flag in ('1')  AND c.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) ");
        sb.append(" AND d.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') AND d.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ) t where t.oper_code = :code and t.oper_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and t.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" Union All ");
        sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,-nvl(sum(cost), 0) BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
        sb.append(" from (select sum(own_cost) cost from T_OUTPATIENT_FEEDETAIL_NOW a where a.trans_type = 2 and a.cancel_flag = 0 and a.account_flag = 1 ");
        sb.append(" and a.drug_flag = '0' and a.fee_cpcd = :code and a.fee_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and a.fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') ");
        sb.append(" union all ");
        sb.append(" select sum(own_cost) cost from T_OUTPATIENT_FEEDETAIL_NOW b,(select distinct (a.recipe_no || a.sequence_no) key from T_OUTPATIENT_FEEDETAIL_NOW a where a.trans_type = 2 and a.cancel_flag = 0 and a.account_flag = 1 ");
        sb.append(" and a.drug_flag = '0' and a.fee_cpcd =:code and a.fee_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and a.fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) f ");
        sb.append(" where (b.recipe_no || b.sequence_no) = f.key and b.account_flag = 1 and b.trans_type = 1 and b.drug_flag = '0' and b.fee_cpcd = :code and b.fee_date >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and b.fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')) ");
        sb.append(" union All ");
        sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,nvl(sum(nvl(prepay_cost, 0)), 0) As YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,0 balanceCost ");
        sb.append(" from t_outpatient_outprepay k where CREATETIME >= To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and CREATETIME < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') and CREATEUSER = :code and prepay_type = 'CA' ");
        sb.append(" Union All ");
        sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,nvl(sum(nvl(prepay_cost, 0)), 0) As YSPREPAYDB,0 YSPAY,0 balanceCost ");
        sb.append(" from t_outpatient_outprepay k where balance_flag = '0' and CREATETIME >=  To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and CREATETIME <  TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') and CREATEUSER = :code and prepay_type = 'DB' ");
        sb.append(" Union All ");
        sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,nvl(sum(cost), 0) YSPAY,0 balanceCost ");
        sb.append(" from (select sum(nvl(tot_cost, 0)) cost from t_Business_Paymode_Now where mode_code = 'YS' and oper_code = :code and oper_date >=  To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') and balance_flag = 0 ");
        sb.append(" union all ");
        sb.append(" select sum(nvl(own_cost, 0)) cost from T_OUTPATIENT_FEEDETAIL_NOW b where fee_cpcd = :code and fee_date >=  To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and fee_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss') and invoice_no is null and account_flag = '1')  ");
        sb.append(" Union All ");
        sb.append(" select 0 Tot, 0 Ca,0 Db, 0 Ch,0 Ys,0 Incoicenum,0 Backinvoicenum,0 Cancel,0 Backfee,0 BACKDRUG,0 BACKYSDRUG,0 BACKUNDRUG,0 BACKYSUNDRUG,0 YSPREPAYCA,0 YSPREPAYDB,0 YSPAY,-nvl(sum(nvl(c.money, 0)), 0) balanceCost ");
        sb.append(" from T_OUTPATIENT_ACCOUNTRECORD c where c.opertype = '10' and c.oper_code = :code and c.oper_date >=  To_Date(:startDate, 'yyyy-MM-dd HH24:mi:ss') and c.oper_date < TO_DATE(:endDate,'yyyy-MM-dd HH24:mi:ss')   ) ");
        OutpatientDaybalance dayBlance = (OutpatientDaybalance) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
						query.addScalar("totCost",Hibernate.DOUBLE).addScalar("caCost",Hibernate.DOUBLE).addScalar("cdCost",Hibernate.DOUBLE);
		        		query.addScalar("chCost",Hibernate.DOUBLE).addScalar("ysCost",Hibernate.DOUBLE).addScalar("balanceInvoiceNum",Hibernate.DOUBLE);
		        		query.addScalar("backInvoiceNum",Hibernate.DOUBLE).addScalar("cancelInvoiceNum",Hibernate.DOUBLE).addScalar("backysdrug",Hibernate.DOUBLE);
		        		query.addScalar("backFee",Hibernate.DOUBLE).addScalar("backDrug",Hibernate.DOUBLE).addScalar("backUndrug",Hibernate.DOUBLE);
		        		query.addScalar("backysundrug",Hibernate.DOUBLE).addScalar("ysPrepayca",Hibernate.DOUBLE).addScalar("ysPrepaydb",Hibernate.DOUBLE);
		        		query.addScalar("ysPay",Hibernate.DOUBLE).addScalar("ysBalanceCost",Hibernate.DOUBLE);
				return query
						.setParameter("code", code)
						.setParameter("startDate", startDate)
						.setParameter("endDate", endDate).setResultTransformer(Transformers.aliasToBean(OutpatientDaybalance.class)).uniqueResult();
			}
		});
		return dayBlance;
	}
}
