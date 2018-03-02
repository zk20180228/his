package cn.honry.inpatient.arrearage.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.arrearage.dao.ArrearageDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("arrearageDAO")
@SuppressWarnings("all")
public class ArrearageDAOImpl extends HibernateEntityDao<InpatientInPrepay> implements ArrearageDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
																		
	@Override
	public List<InpatientInPrepay> QueryInpatientInPrepay(String inpatientNo
			,String outDate,String inDate) throws Exception{
		String hql="from InpatientInPrepay where inpatientNo=? and to_char(balanceDate,'yyyy-MM-dd hh:mm:ss')<? and to_char(balanceDate,'yyyy-MM-dd hh:mm:ss')>? and del_flg = 0 and stop_flg = 0 ";
		List<InpatientInPrepay> iList = super.find(hql, inpatientNo,inDate,outDate);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInPrepay>();
	}
	
	@Override
	public List<InpatientInPrepay> QueryprepayCost(String inpatientNo
			,String outDate,String inDate) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(t.PREPAY_COST) as prepayCost from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INPREPAY t "
				+"WHERE (t.INPATIENT_NO = '"+inpatientNo+"') "
			    +"AND (t.BALANCE_STATE = '0') AND(to_char(t.BALANCE_DATE,'yyyy-MM-dd hh:mm:ss')> '"+outDate+"') AND (to_char(t.BALANCE_DATE,'yyyy-MM-dd hh:mm:ss')< '"+inDate+"') "
			    + "AND STOP_FLG=0 AND DEL_FLG=0 ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("prepayCost",Hibernate.DOUBLE);
		List<InpatientInPrepay> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInPrepay.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientInPrepay>();
	}

	@Override
	public List<InpatientFeeInfo> QueryInpatientFeeInfo(String inpatientNo,
			String outDate, String inDate) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT T_INPATIENT_FEEINFO.FEE_CODE as feeCode,sum(tot_cost) as totCost,"
					+ "sum(own_cost) ownCost, sum(pub_cost) pubCost,"
					+ "sum(pay_cost) payCost,sum(eco_cost) ecoCost "
				    +" FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_FEEINFO "
				    +"WHERE (T_INPATIENT_FEEINFO.INPATIENT_NO = '"+inpatientNo+"') "
				    +"AND (T_INPATIENT_FEEINFO.BALANCE_STATE = '0') AND(to_char(T_INPATIENT_FEEINFO.BALANCE_DATE,'yyyy-MM-dd hh:mm:ss')< '"+outDate+"') AND (to_char(T_INPATIENT_FEEINFO.BALANCE_DATE,'yyyy-MM-dd hh:mm:ss')> '"+inDate+"') "
				    + "AND (STOP_FLG=0) AND (DEL_FLG=0) group by T_INPATIENT_FEEINFO.FEE_CODE HAVING sum(tot_cost)<> 0 ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("feeCode")
		.addScalar("totCost",Hibernate.DOUBLE).addScalar("ownCost",Hibernate.DOUBLE).addScalar("pubCost",Hibernate.DOUBLE).addScalar("payCost",Hibernate.DOUBLE)
		.addScalar("ecoCost",Hibernate.DOUBLE);
		List<InpatientFeeInfo> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientFeeInfo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientFeeInfo>();
	}

	@Override
	public List<InpatientFeeInfo> QuerytotCost(String inpatientNo,
			String outDate, String inDate) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(t.tot_cost) as totCost,"
						+ "sum(t.own_cost) as ownCost,"
						+ "sum(t.pub_cost) as pubCost,"
						+ "sum(t.pay_cost) as payCost,"
						+ "sum(t.eco_cost) as ecoCost " 
						+ "FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_FEEINFO t "
						+ "WHERE (t.INPATIENT_NO = '"+inpatientNo+"') "
						+ "AND (t.BALANCE_STATE = '0') "
						+ "AND (to_char(t.BALANCE_DATE,'yyyy-MM-dd hh:mm:ss')> '"+inDate+"') "
						+ "AND (to_char(t.BALANCE_DATE,'yyyy-MM-dd hh:mm:ss')< '"+outDate+"') "
						+ "AND t.STOP_FLG=0 AND t.DEL_FLG=0 ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("ownCost",Hibernate.DOUBLE).addScalar("pubCost",Hibernate.DOUBLE).addScalar("payCost",Hibernate.DOUBLE)
		.addScalar("ecoCost",Hibernate.DOUBLE);
		List<InpatientFeeInfo> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientFeeInfo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientFeeInfo>();
	}

	@Override
	public List<InpatientInPrepay> QueryInpatientInPrepayID(String id) throws Exception{
		String hql="from InpatientInPrepay where id=? and del_flg = 0 and stop_flg = 0 ";
		List<InpatientInPrepay> iList = super.find(hql, id);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInPrepay>();
	}

	@Override
	public void SaveInpatientBalancePay(InpatientBalancePay inpatientBalancePay) throws Exception{
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		inpatientBalancePay.setCreateUser(user.getId());
		inpatientBalancePay.setCreateDept(dept.getId());
		inpatientBalancePay.setCreateTime(new Date());
		inpatientBalancePay.setStop_flg(0);
		inpatientBalancePay.setDel_flg(0);
		super.save(inpatientBalancePay);
	}

	@Override
	public List<InpatientFeeInfo> QueryID(String id) throws Exception{
		String hql="from InpatientFeeInfo where feeCode='"+id+"' and del_flg = 0 and stop_flg = 0 ";
		List<InpatientFeeInfo> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientFeeInfo>();
	}
}
