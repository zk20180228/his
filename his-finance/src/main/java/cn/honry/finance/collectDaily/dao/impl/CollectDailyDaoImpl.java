package cn.honry.finance.collectDaily.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientScDreport;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.collectDaily.dao.CollectDailyDao;
import cn.honry.finance.collectDaily.vo.ColDaiVo;
import cn.honry.utils.ShiroSessionUtils;
/**   
*  
* @className：CollectDailyDaoImpl
* @description：结算员日结Dao实现类
* @author：tcj
* @createDate：2016-04-12  
* @modifyRmk：  
* @version 1.0
 */
@Repository("collectDailyDao")
@SuppressWarnings({"all"})
public class CollectDailyDaoImpl  extends HibernateEntityDao<ColDaiVo> implements CollectDailyDao{
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 * @param sessionFactory
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public SysEmployee queryEmpByUserId(String id) throws Exception{
		String hql="from SysEmployee where del_flg=0 and stop_flg=0 and userId.id=?";
		List<SysEmployee> sysl=super.find(hql, id);
		if(sysl!=null&&sysl.size()>0){
			return sysl.get(0);
		}
		return new SysEmployee();
	}
	@Override
	public List<InpatientScDreport> queryCollectMaxTime() throws Exception{
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String hql=" from InpatientScDreport i where i.del_flg=0 and i.stop_flg=0  "
				+ "and i.operCode =? "
				+ "and i.endDate =( select max(d.endDate) from InpatientScDreport d where d.del_flg=0 and d.stop_flg=0 )";
		List<InpatientScDreport> isdl=super.find(hql, user.getAccount());
		if(isdl!=null&&isdl.size()>0){
			return isdl;
		}
		return new ArrayList<InpatientScDreport>();
	}

	@Override
	public List<ColDaiVo> querydetalDaily(String startTime,
			String endTime) throws Exception{
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String sql="select c.fee_stat_code as code,round(nvl(sum(m.tot_cost), 0),2)  as cost,round(nvl(sum(m.OWN_COST), 0),2)  as ownCost,round(nvl(sum(m.PAY_COST), 0),2)  as payCost,round( nvl(sum(m.PUB_COST), 0),2)  as pubCost "
				+ "from t_inpatient_medicinelist_now m, t_charge_minfeetostat c "
				+ " where m.fee_code = c.minfee_code   and m.balance_state = 1  "
				+ "and c.report_code = 'ZY01'   and m.invoice_no in  "
					+ " (select b.invoice_no   from t_inpatient_balancehead_now b  "
						+ "where b.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss')   and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ) "
				+ "group by c.fee_stat_code "
				+ "union all "
				+ "select c.fee_stat_code as code, round(nvl(sum(i.tot_cost), 0),2) as cost, round(nvl(sum(i.OWN_COST), 0),2)  as ownCost,round(nvl(sum(i.PAY_COST), 0),2)  as payCost,round( nvl(sum(i.PUB_COST), 0),2)  as pubCost "
				+ "from t_inpatient_itemlist_now i, t_charge_minfeetostat c "
				+ "where i.fee_code = c.minfee_code and i.balance_state = 1  "
				+ "and c.report_code = 'ZY01' and i.invoice_no in   "
					+ "(select b.invoice_no  from t_inpatient_balancehead_now b   "
						+ "	where b.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss')   and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ) "
				+ "group by c.fee_stat_code";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("code")
		.addScalar("cost",Hibernate.DOUBLE)
		.addScalar("ownCost",Hibernate.DOUBLE)
		.addScalar("payCost",Hibernate.DOUBLE)
		.addScalar("pubCost",Hibernate.DOUBLE);
		queryObject.setParameter("startTime", startTime);
		queryObject.setParameter("endTime", endTime);
		List<ColDaiVo> cdvl=queryObject.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		if(cdvl!=null&&cdvl.size()>0){
			return cdvl;
		}
		return new ArrayList<ColDaiVo>();
	}

	@Override
	public ColDaiVo queryTableDaily(String startTime, String endTime) throws Exception{
		ColDaiVo cvd=new ColDaiVo();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		//当前登录员工Id
		String empId=user.getAccount();
		//医疗预收款借方金额
		StringBuffer sql1=new StringBuffer();
		sql1.append("  select t.trans_type as transType, nvl(sum(t.prepay_cost), 0) as date1, nvl(sum(t.foregift_cost), 0) as date2 ");
		sql1.append("  from t_inpatient_balancehead_now t where t.del_flg = 0 and stop_flg = 0 ");
		sql1.append("  and t.balance_opercode =:empId ");
		sql1.append("  and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss')  ");
		sql1.append("  group by t.trans_type   order by t.trans_type");
		SQLQuery queryObject1 = this.getSession().createSQLQuery(sql1.toString());
		queryObject1.addScalar("transType",Hibernate.INTEGER)
		.addScalar("date1",Hibernate.DOUBLE)
		.addScalar("date2",Hibernate.DOUBLE);
		queryObject1.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl1=queryObject1.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		
		if(cdvl1.size()>1){
			double num1=(cdvl1.get(0).getDate1()-cdvl1.get(0).getDate2())+(cdvl1.get(1).getDate1()-cdvl1.get(1).getDate2());
			BigDecimal   b   =   new   BigDecimal(num1); 
			cvd.setBalancePrepaycost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl1.size()==1){
			double num1=cdvl1.get(0).getDate1()-cdvl1.get(0).getDate2();
			BigDecimal   b   =   new   BigDecimal(num1); 
			cvd.setBalancePrepaycost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//医疗预收款贷方金额
		StringBuffer sql2=new StringBuffer();
		sql2.append(" select t.trans_type as transType,nvl(sum(t.prepay_cost),0) as prepayCost from t_inpatient_inprepay_now t ");
		sql2.append(" where t.del_flg = 0  and t.stop_flg = 0 and t.trans_flag=0");
		sql2.append(" and t.CREATEUSER = :empId and t.CREATETIME between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') " );
		sql2.append("  and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject2 = this.getSession().createSQLQuery(sql2.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("prepayCost",Hibernate.DOUBLE);
		queryObject2.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl2=queryObject2.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num2=0;
		if(cdvl2.size()>1){
			 num2=cdvl2.get(0).getPrepayCost()+cdvl2.get(1).getPrepayCost();
			 BigDecimal   b   =   new   BigDecimal(num2); 
			 cvd.setPrepayCost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl2.size()==1){
			 num2=cdvl2.get(0).getPrepayCost();
			 BigDecimal   b   =   new   BigDecimal(num2); 
			 cvd.setPrepayCost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//医疗应收款贷方金额（无借方）
		StringBuffer sql3=new StringBuffer();
		sql3.append(" select t.trans_type as transType,nvl(sum(t.tot_cost), 0) as balanceCost  from t_inpatient_balancehead_now t ");
		sql3.append(" where t.del_flg = 0 and stop_flg = 0 and t.balance_opercode = :empId ");
		sql3.append(" and t.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss')  and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')  ");
		sql3.append("  group by t.trans_type order by t.trans_type");
		SQLQuery queryObject3 = this.getSession().createSQLQuery(sql3.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("balanceCost",Hibernate.DOUBLE);
		queryObject3.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl3=queryObject3.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num3=0;
		if(cdvl3.size()>1){
			 num3= cdvl3.get(0).getBalanceCost()+cdvl3.get(1).getBalanceCost();
			 BigDecimal   b   =   new   BigDecimal(num3); 
			 cvd.setBalanceCost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl3.size()==1){
			 num3=cdvl3.get(0).getBalanceCost();
			 BigDecimal   b   =   new   BigDecimal(num3); 
			 cvd.setBalanceCost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//银行存款借方金额
		StringBuffer sql41 =new StringBuffer();
		sql41.append("select t.trans_type as transType,nvl(sum(t.prepay_cost),0) as date1 from t_inpatient_inprepay_now t");
		sql41.append(" where t.del_flg = 0  and t.stop_flg = 0 and  t.pay_way in('CH','PO')  and t.trans_flag=0");
		sql41.append("  and t.CREATEUSER = :empId and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		sql41.append(" group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject41 = this.getSession().createSQLQuery(sql41.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject41.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl41=queryObject41.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num41=0;
		if(cdvl41.size()>1){
			 num41 =cdvl41.get(0).getDate1()+cdvl41.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num41); 
			 cvd.setDebitCheck(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl41.size()==1){
			 num41 =cdvl41.get(0).getDate1();
		}
		StringBuffer sql42 =new StringBuffer();
		sql42.append("select t.trans_type as transType,nvl(sum(t.cost),0) as date2  from t_inpatient_balancepay_now t");
		sql42.append(" where  t.stop_flg=0  and t.del_flg=0 and t.balance_opercode =:empId  and t.trans_kind =1");
		sql42.append(" and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') and t.REUTRNORSUPPLY_FLAG = 1");
		sql42.append("  and t.pay_way in ('CH','PO') group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject42 = this.getSession().createSQLQuery(sql42.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("balanceCost",Hibernate.DOUBLE);
		queryObject42.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl42=queryObject42.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num42=0;
		if(cdvl42.size()>1){
			 num42 =cdvl42.get(0).getDate2()+cdvl42.get(1).getDate2();
		}else if(cdvl42.size()==1){
			 num42 =cdvl42.get(0).getDate2();
		}
		double num4=num41+num42;
		 BigDecimal   b1   =   new   BigDecimal(num4); 
		 cvd.setDebitCheck(b1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		//银行存款贷方金额
		StringBuffer sql5 =new StringBuffer();
		sql5.append(" select t.trans_type as transType, nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t");
		sql5.append(" where  t.stop_flg=0 and t.del_flg=0 and t.balance_opercode =:empId and  t.trans_kind =1 and t.pay_way ='CH'");
		sql5.append(" and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		sql5.append(" and t.REUTRNORSUPPLY_FLAG = 2  group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject5 = this.getSession().createSQLQuery(sql5.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject5.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl5=queryObject5.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num5=0;
		if(cdvl5.size()>1){
			 num5 =cdvl5.get(0).getDate1()+cdvl5.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num5); 
			 cvd.setLenderCheck(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl5.size()==1){
			 num5 =cdvl5.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num5); 
			 cvd.setLenderCheck(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//现金的借方金额
		StringBuffer sql61 =new StringBuffer();
		sql61.append("select t.trans_type as transType,nvl(sum(t.prepay_cost),0) as date1 from t_inpatient_inprepay_now t");
		sql61.append("  where t.del_flg = 0  and t.stop_flg = 0 and  t.pay_way ='CA' and t.trans_flag=0");
		sql61.append(" and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		sql61.append(" and t.CREATEUSER =:empId group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject61 = this.getSession().createSQLQuery(sql61.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject61.setParameter("startTime", startTime).setParameter("endTime", endTime).setParameter("empId", empId);
		List<ColDaiVo> cdvl61=queryObject61.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num61=0;
		if(cdvl61.size()>1){
			 num61 =cdvl61.get(0).getDate1()+cdvl61.get(1).getDate1();
		}else if(cdvl61.size()==1){
			 num61 =cdvl61.get(0).getDate1();
		}
		StringBuffer sql62 =new StringBuffer();
		sql62.append(" select t.trans_type as transType, nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t");
		sql62.append("  where  t.stop_flg=0  and t.del_flg=0 and t.balance_opercode =:empId  and t.trans_kind =1");
		sql62.append("  and t.pay_way ='CA' and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		sql62.append("  and t.REUTRNORSUPPLY_FLAG = 1    group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject62 = this.getSession().createSQLQuery(sql62.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject62.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl62=queryObject62.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num62=0;
		if(cdvl62.size()>1){
			 num62 =cdvl62.get(0).getDate1()+cdvl62.get(1).getDate1();
		}else if(cdvl62.size()==1){
			 num62 =cdvl62.get(0).getDate1();
		}
		double num6 =num61+num62;
		BigDecimal   b2   =   new   BigDecimal(num6); 
		cvd.setCashj(b2.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		//现金的贷方现金
		StringBuffer sql7 =new StringBuffer();
		sql7.append("select t.trans_type as transType, nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t");
		sql7.append(" where  t.stop_flg=0  and t.del_flg=0 and t.balance_opercode =:empId  and t.trans_kind =1");
		sql7.append("  and t.pay_way ='CA' and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		sql7.append("  and t.REUTRNORSUPPLY_FLAG = 2   group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject7 = this.getSession().createSQLQuery(sql7.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject7.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl7=queryObject7.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num7=0;
		if(cdvl7.size()>1){
			 num7 =cdvl7.get(0).getDate1()+cdvl7.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num7); 
			 cvd.setCashd(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl7.size()==1){
			 num7 =cdvl7.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num7); 
			 cvd.setCashd(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//银行卡借方金额
		StringBuffer sql81 =new StringBuffer();
		sql81.append("select t.trans_type as transType,nvl(sum(t.prepay_cost),0) as date1 from t_inpatient_inprepay_now t ");
		sql81.append("where t.del_flg = 0 and t.stop_flg = 0   and  t.pay_way ='DB' and t.trans_flag=0");
		sql81.append(" and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')");
		sql81.append("and t.CREATEUSER =:empId  group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject81 = this.getSession().createSQLQuery(sql81.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject81.setParameter("startTime", startTime).setParameter("endTime", endTime).setParameter("empId", empId);
		List<ColDaiVo> cdvl81=queryObject81.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num81=0;
		if(cdvl81.size()>1){
			num81 =cdvl81.get(0).getDate1()+cdvl81.get(1).getDate1();
		}else if(cdvl81.size()==1){
			num81 =cdvl81.get(0).getDate1();
		}
		StringBuffer sql82 =new StringBuffer();
		sql82.append("  select t.trans_type as transType, nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t ");
		sql82.append(" where  t.stop_flg=0   and t.del_flg=0 and t.balance_opercode =:empId and t.trans_kind =1 ");
		sql82.append("   and t.pay_way ='CA' and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		sql82.append(" and t.REUTRNORSUPPLY_FLAG =1  group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject82 = this.getSession().createSQLQuery(sql82.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject82.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl82=queryObject82.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num82=0;
		if(cdvl82.size()>1){
			 num82 =cdvl82.get(0).getDate1()+cdvl82.get(1).getDate1();
		}else if(cdvl82.size()==1){
			 num82 =cdvl82.get(0).getDate1();
		}
		double num8=num81+num82;
		BigDecimal   b3   =   new   BigDecimal(num8); 
		cvd.setDebitBank(b3.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		//银行卡贷方金额
		StringBuffer sql9 =new StringBuffer();
		sql9.append(" select t.trans_type as transType,nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t");
		sql9.append("  where  t.stop_flg=0  and t.del_flg=0 and t.balance_opercode =:empId  and t.trans_kind =1");
		sql9.append(" and t.pay_way ='DB'  and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		sql9.append("  and t.REUTRNORSUPPLY_FLAG =1   group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject9 = this.getSession().createSQLQuery(sql9.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject9.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl9=queryObject9.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num9=0;
		if(cdvl9.size()>1){
			 num9 =cdvl9.get(0).getDate1()+cdvl9.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num9); 
			 cvd.setLenderBank(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl9.size()==1){
			 num9 =cdvl9.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num9); 
			 cvd.setLenderBank(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//院内账户借方金额
		StringBuffer sql10 =new StringBuffer();
		sql10.append("select t.trans_type as transType,nvl(sum(t.prepay_cost),0) as date1 from t_inpatient_inprepay_now t");
		sql10.append(" where t.del_flg = 0   and t.stop_flg = 0 and  t.pay_way ='YS'  and t.trans_flag=0");
		sql10.append("  and t.CREATETIME  between  to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')");
		sql10.append("and t.CREATEUSER = :empId group by t.trans_type order by t.trans_type");
		SQLQuery queryObject10 = this.getSession().createSQLQuery(sql10.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject10.setParameter("startTime", startTime).setParameter("endTime", endTime).setParameter("empId", empId);
		List<ColDaiVo> cdvl10=queryObject10.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num10=0;
		if(cdvl10.size()>1){
			 num10 =cdvl10.get(0).getDate1()+cdvl10.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num10); 
			 cvd.setDebitHos(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl10.size()==1){
			 num10 =cdvl10.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num10); 
			 cvd.setDebitHos(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//院内账户贷方金额
		StringBuffer sql12 =new StringBuffer();
		sql12.append(" select t.trans_type as transType, nvl(sum(t.cost),0) as date1   from t_inpatient_balancepay_now t");
		sql12.append(" where  t.stop_flg=0   and t.del_flg=0 and t.balance_opercode =:empId and t.trans_kind =1 ");
		sql12.append(" and t.pay_way ='YS' and t.balance_date between  to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		sql12.append(" and t.REUTRNORSUPPLY_FLAG =2   group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject12 = this.getSession().createSQLQuery(sql12.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject12.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl12=queryObject12.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num12=0;
		if(cdvl12.size()>1){
			 num12 =cdvl12.get(0).getDate1()+cdvl12.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num12); 
			 cvd.setLenderHos(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl12.size()==1){
			 num12 =cdvl12.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num12); 
			 cvd.setLenderHos(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//其他预收借方金额
		StringBuffer sql111 =new StringBuffer();
		sql111.append("select t.trans_type as transType,nvl(sum(t.prepay_cost),0) as date1 from t_inpatient_inprepay_now t");
		sql111.append(" where t.del_flg = 0 and t.stop_flg = 0 and t.trans_flag in(2,1) ");
		sql111.append("and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		sql111.append("and t.CREATEUSER = :empId  group by t.trans_type order by t.trans_type");
		SQLQuery queryObject111 = this.getSession().createSQLQuery(sql111.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject111.setParameter("startTime", startTime).setParameter("endTime", endTime).setParameter("empId", empId);
		List<ColDaiVo> cdvl111=queryObject111.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num111 =0;
		if(cdvl111.size()>1){
			num111 =cdvl111.get(0).getDate1()+cdvl111.get(1).getDate1();
		}else if(cdvl111.size()==1){
			num111 =cdvl111.get(0).getDate1();
		}
		StringBuffer sql112 =new StringBuffer();
		sql112.append(" select t.trans_type as transType, nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t");
		sql112.append(" where  t.stop_flg=0  and t.del_flg=0 and t.balance_opercode =:empId  and t.pay_way ='PS'");
		sql112.append("and t.balance_date  between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss')  and  to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') and t.trans_kind =1");
		sql112.append("and t.REUTRNORSUPPLY_FLAG =1   group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject112 = this.getSession().createSQLQuery(sql112.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject112.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl112=queryObject112.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num112 =0;
		if(cdvl112.size()>1){
			num112 =cdvl112.get(0).getDate1()+cdvl112.get(1).getDate1();
		}else if(cdvl112.size()==1){
		    num112 =cdvl112.get(0).getDate1();
		}
		double num11=num111+num112;
		 BigDecimal   b4   =   new   BigDecimal(num11); 
		 cvd.setDebitOther(b4.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		//其他预收款贷方金额
		StringBuffer sql13 =new StringBuffer();
		sql13.append("select t.trans_type as transType, nvl(sum(t.cost),0) as date1  from t_inpatient_balancepay_now t");
		sql13.append(" where  t.stop_flg=0  and t.del_flg=0 and t.balance_opercode =:empId  and t.pay_way ='PS'");
		sql13.append(" and t.balance_date between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') and t.trans_kind =1");
		sql13.append(" and t.REUTRNORSUPPLY_FLAG =2   group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject13 = this.getSession().createSQLQuery(sql13.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject13.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl13=queryObject13.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num13=0;
		if(cdvl13.size()>1){
			 num13 =cdvl13.get(0).getDate1()+cdvl13.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num13); 
			 cvd.setLenderOther(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl13.size()==1){
			 num13 =cdvl13.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num13); 
			 cvd.setLenderOther(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//医疗减免借方金额
		StringBuffer sql14 =new StringBuffer();
		sql14.append(" select t.trans_type as transType, nvl(sum(t.DER_COST),0) as date1  from t_inpatient_balancehead_now t");
		sql14.append("  where t.del_flg = 0  and t.stop_flg = 0 and t.balance_opercode = :empId ");
		sql14.append(" and t.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		sql14.append(" group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject14 = this.getSession().createSQLQuery(sql14.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE);
		queryObject14.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl14=queryObject14.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num14=0;
		if(cdvl14.size()>1){
			 num14 =cdvl14.get(0).getDate1()+cdvl14.get(1).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num14); 
			 cvd.setDerateCost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl14.size()==1){
			 num14 =cdvl14.get(0).getDate1();
			 BigDecimal   b   =   new   BigDecimal(num14); 
			 cvd.setDerateCost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//公费记账借方金额
		StringBuffer sql15 =new StringBuffer();
		sql15.append("select t.trans_type as transType,nvl(sum(t.pub_cost), 0) as date1, nvl(sum(t.DER_COST),0) as date2");
		sql15.append(" from t_inpatient_balancehead_now t where t.del_flg = 0  and t.stop_flg = 0 and t.balance_opercode = :empId ");
		sql15.append(" and t.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss')  and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		sql15.append(" and t.PAYKIND_CODE=3 group by t.trans_type  order by t.trans_type");
		SQLQuery queryObject15 = this.getSession().createSQLQuery(sql15.toString())
				.addScalar("transType",Hibernate.INTEGER).addScalar("date1",Hibernate.DOUBLE).addScalar("date2",Hibernate.DOUBLE);
		queryObject15.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl15=queryObject15.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num15 =0;
		if(cdvl15.size()>1){
			 num15 =cdvl15.get(0).getDate1()+cdvl15.get(1).getDate1()+cdvl15.get(0).getDate2()+cdvl15.get(1).getDate2();
			 BigDecimal   b   =   new   BigDecimal(num15); 
			 cvd.setBusaryPubcost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else if(cdvl15.size()==1){
			 num15 =cdvl15.get(0).getDate1()+cdvl15.get(0).getDate2();
			 BigDecimal   b   =   new   BigDecimal(num15); 
			 cvd.setBusaryPubcost(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		//预交金张数
		StringBuffer sql16 =new StringBuffer();
		sql16.append(" select distinct count(t.receipt_no) as date1 from t_inpatient_inprepay_now t");
		sql16.append(" where t.del_flg = 0 and t.stop_flg = 0 and t.CREATEUSER = :empId ");
		sql16.append(" and t.CREATETIME  between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')  and t.prepay_cost>0");
		sql16.append("  and (t.trans_flag=0 or t.change_balance_no=:empId )  ");
		SQLQuery queryObject16 = this.getSession().createSQLQuery(sql16.toString())
				.addScalar("date1",Hibernate.DOUBLE);
		queryObject16.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl16=queryObject16.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num16 =cdvl16.get(0).getDate1();
		cvd.setPrepayinvNum(num16);
		//预交金作废张数
		StringBuffer sql17 =new StringBuffer();
		sql17.append(" select distinct count(t.receipt_no) as date1 from t_inpatient_inprepay_now t ");
		sql17.append("  where t.del_flg = 0 and t.stop_flg = 0 and t.CREATEUSER = :empId and  t.trans_flag in (0,1,2) ");
		sql17.append(" and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss')  and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		sql17.append(" and t.prepay_cost<0  ");
		SQLQuery queryObject17 = this.getSession().createSQLQuery(sql17.toString())
				.addScalar("date1",Hibernate.DOUBLE);
		queryObject17.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl17=queryObject17.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double num17 =cdvl17.get(0).getDate1();
		cvd.setWasteprepayinvNum(num17);
		//预交金票据区间
		StringBuffer sql18 =new StringBuffer();
		sql18.append(" select  t.RECEIPT_NO  as prepayinvZone from t_inpatient_inprepay_now t ");
		sql18.append("  where t.del_flg = 0   and t.stop_flg = 0");
		sql18.append(" and t.CREATEUSER = :empId and t.prepay_cost>0 and t.trans_flag=0 and t.RECEIPT_NO is not null ");
		sql18.append(" and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')  order by t.RECEIPT_NO");
		SQLQuery queryObject18 = this.getSession().createSQLQuery(sql18.toString())
				.addScalar("prepayinvZone");
		queryObject18.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl18=queryObject18.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		String prepayinvZone=null;
		if(cdvl18.size()>1){
			 prepayinvZone=cdvl18.get(0).getPrepayinvZone()+" ~ "+cdvl18.get(cdvl18.size()-1).getPrepayinvZone();
		}else if(cdvl18.size()==1){
			 prepayinvZone=cdvl18.get(0).getPrepayinvZone()+" ~ "+cdvl18.get(0).getPrepayinvZone();
		}
		cvd.setPrepayinvZone(prepayinvZone);
		//预交金作废票据号
		StringBuffer sql19 =new StringBuffer();
		sql19.append(" select  t.old_recipeno as wasteprepayInvno from t_inpatient_inprepay_now t where t.del_flg = 0 and t.stop_flg = 0");
		sql19.append(" and t.CREATEUSER = :empId  and t.prepay_cost<0   and t.ext_flag=1  and t.prepay_state=1  and t.trans_flag=0");
		sql19.append(" and t.CREATETIME between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') ");
		SQLQuery queryObject19 = this.getSession().createSQLQuery(sql19.toString())
				.addScalar("wasteprepayInvno");
		queryObject19.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl19=queryObject19.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		String wasteprepayInvno=null;
		for(int i=0;i<cdvl19.size();i++){
			if(wasteprepayInvno!=null){
				wasteprepayInvno +="|";
			}
			wasteprepayInvno +=cdvl19.get(i).getWasteprepayInvno();
		}
		cvd.setWasteprepayInvno(wasteprepayInvno);
		//结算票据张数
		StringBuffer sql20 =new StringBuffer();
		sql20.append("select  distinct count(t.invoice_no) as balanceinvNum  from t_inpatient_balancehead_now t");
		sql20.append("  where t.del_flg=0  and t.stop_flg=0 and t.balance_opercode = :empId ");
		sql20.append(" and t.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')  and  t.tot_cost > 0");
		SQLQuery queryObject20 = this.getSession().createSQLQuery(sql20.toString())
				.addScalar("balanceinvNum",Hibernate.DOUBLE);
		queryObject20.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl20=queryObject20.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double balanceinvNum=cdvl20.get(0).getBalanceinvNum();
		cvd.setBalanceinvNum(balanceinvNum);
		//结算作废张数
		StringBuffer sql21 =new StringBuffer();
		sql21.append("select  distinct count(t.invoice_no) as wastebalanceinvNum  from t_inpatient_balancehead_now t");
		sql21.append("  where t.del_flg=0  and t.stop_flg=0 and t.balance_opercode = :empId ");
		sql21.append(" and t.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') and t.tot_cost <0");
		SQLQuery queryObject21 = this.getSession().createSQLQuery(sql21.toString())
				.addScalar("wastebalanceinvNum",Hibernate.DOUBLE);
		queryObject21.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl21=queryObject21.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		double wastebalanceinvNum=cdvl21.get(0).getWastebalanceinvNum();
		cvd.setWastebalanceinvNum(wastebalanceinvNum);
		//结算票据区间
		StringBuffer sql22 =new StringBuffer();
		sql22.append("  select t.invoice_no as balanceinvZone from t_inpatient_balancehead_now t");
		sql22.append("  where t.del_flg=0 and t.stop_flg=0   and t.tot_cost >0 and t.balance_opercode =:empId ");
		sql22.append(" and t.balance_date between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')   order by t.invoice_no");
		SQLQuery queryObject22= this.getSession().createSQLQuery(sql22.toString())
				.addScalar("balanceinvZone");
		queryObject22.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl22=queryObject22.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		String balanceinvZone=null;
		if(cdvl22.size()>0){
			 balanceinvZone=cdvl22.get(0).getBalanceinvZone()+" ~ "+cdvl22.get(cdvl22.size()-1).getBalanceinvZone();
		}
		cvd.setBalanceinvZone(balanceinvZone);
		//结算作废票号
		StringBuffer sql23 =new StringBuffer();
		sql23.append("select t.invoice_no as balanceinvZone from t_inpatient_balancehead_now t");
		sql23.append(" where t.del_flg=0 and t.stop_flg=0   and t.tot_cost <0 and t.balance_opercode =:empId ");
		sql23.append(" and t.balance_date between  to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss') and  to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss')  order by t.invoice_no");
		SQLQuery queryObject23 = this.getSession().createSQLQuery(sql23.toString())
				.addScalar("balanceinvZone");
		queryObject23.setParameter("empId", empId).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<ColDaiVo> cdvl23=queryObject23.setResultTransformer(Transformers.aliasToBean(ColDaiVo.class)).list();
		String wastebalanceInvno=null;
		for(int i=0;i<cdvl23.size();i++){
			if(wastebalanceInvno!=null){
				wastebalanceInvno += "|";
			}
			wastebalanceInvno +=cdvl23.get(i).getWastebalanceInvno();
		}
		cvd.setWastebalanceInvno(wastebalanceInvno);	
		
		return cvd;
	}
	@Override
	public List<InpatientBalanceHeadNow> querymedicdatagridDaily(String state,String startTime,String endTime) throws Exception{
		//当前登录员工账号
		String user = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String hql=" from InpatientBalanceHeadNow where del_flg=0 and stop_flg=0 and balanceOpercode=? and balanceDate between to_date(?, 'yyyy-MM-dd hh24:mi:ss')  and to_date(?, 'yyyy-MM-dd hh24:mi:ss') ";
		List<InpatientBalanceHeadNow> ibl=super.find(hql, user,startTime,endTime);
		if(ibl!=null&&ibl.size()>0){
			return ibl;
		}
		return new ArrayList<InpatientBalanceHeadNow>();
	}
	@Override
	public List<InpatientInPrepayNow> queryYjjDatagridDaily(String startTime,
			String endTime) throws Exception{
 		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String hql1="from SysEmployee where del_flg=0 and stop_flg=0 and userId.id=?";
		List<SysEmployee> sysel=super.find(hql1, user.getId());
		//当前登录员工Id
		String empId=sysel.get(0).getJobNo();
		String hql="from InpatientInPrepayNow where del_flg=0 and stop_flg=0 and transFlag=0 and prepayState=0 and createuser in (:empId,:accunt) and createtime between to_date(:startTime, 'yyyy-MM-dd hh24:mi:ss')  and to_date(:endTime, 'yyyy-MM-dd hh24:mi:ss') " ;		Query query =this.getSession().createQuery(hql);
		query.setParameter("empId", empId).setParameter("accunt",user.getAccount()).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<InpatientInPrepayNow> iipl=query.list();
		if(iipl!=null&&iipl.size()>0){
			return iipl;
		}
		return new ArrayList<InpatientInPrepayNow>();
	}
	@Override
	public List<InpatientInPrepayNow> queryInpreList(String startTime,
			String endTime, String id) throws Exception{
		StringBuffer hql=new StringBuffer();
		hql.append(" from InpatientInPrepayNow where del_flg=0 and stop_flg=0 and createUser =:id ");
		hql.append( "and  createTime between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		Query query=this.getSession().createQuery(hql.toString());
		query.setParameter("id", id).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<InpatientInPrepayNow> inprel=query.list();
		if(inprel!=null&&inprel.size()>0){
			return inprel;
		}
		return new ArrayList<InpatientInPrepayNow>();
	}
	@Override
	public List<InpatientBalanceHeadNow> queryinbanheadList(String startTime,
			String endTime, String id) throws Exception{
		StringBuffer hql=new StringBuffer();
		hql.append(" from InpatientBalanceHeadNow where del_flg=0 and stop_flg=0 and balanceOpercode=:id ");
		hql.append(" and balanceDate between to_date(:startTime,'yyyy-MM-dd hh24:mi:ss') and to_date(:endTime,'yyyy-MM-dd hh24:mi:ss') ");
		Query query=this.getSession().createQuery(hql.toString());
		query.setParameter("id", id).setParameter("startTime", startTime).setParameter("endTime", endTime);
		List<InpatientBalanceHeadNow> inbhl=query.list();
		if(inbhl!=null&&inbhl.size()>0){
			return inbhl;
		}
		return new ArrayList<InpatientBalanceHeadNow>();
	}
	@Override
	public List<SysEmployee> queryEmplistdaily() throws Exception{
		String hql=" from SysEmployee where del_flg=0 and stop_flg=0 ";
		List<SysEmployee> empl=super.find(hql, null);
		if(empl!=null&&empl.size()>0){
			return empl;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public List<User> queryUselistdaily() throws Exception{
		String hql=" from User where del_flg=0 and stop_flg=0 ";
		List<User> userl=super.find(hql, null);
		if(userl!=null&&userl.size()>0){
			return userl;
		}
		return new ArrayList<User>();
	}
	@Override
	public List<MinfeeStatCode> queryfreecodedaily() throws Exception{
		String hql="from MinfeeStatCode where del_flg=0 and stop_flg=0";
		List<MinfeeStatCode> codefreel=super.find(hql, null);
		if(codefreel!=null&&codefreel.size()>0){
			return codefreel;
		}
		return new ArrayList<MinfeeStatCode>();
	}

	
}
