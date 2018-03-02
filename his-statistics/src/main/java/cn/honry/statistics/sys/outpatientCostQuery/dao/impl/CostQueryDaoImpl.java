package cn.honry.statistics.sys.outpatientCostQuery.dao.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.FinanceInvoiceInfo;
import cn.honry.base.bean.model.FinanceInvoicedetail;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.sys.outpatientCostQuery.dao.CostQueryDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

@Repository("costQueryDAO")
@SuppressWarnings({ "all" })
public class CostQueryDaoImpl extends HibernateEntityDao<OutpatientFeedetail> implements CostQueryDao{
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public Map<String,Object> findInvoiceNoSummary(final String invoiceNo,final String stime,final String etime,String page,String rows) throws Exception {
		
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		Map<String,Object> map = new HashMap<String,Object>();
		
		StringBuffer sb01 = new StringBuffer();
		sb01.append(" INVOICE_NO as invoiceNo,CARD_NO as cardNo,REG_DATE as regDate,NAME as name,PAYKIND_NAME as paykindCode, ");
		sb01.append(" PACT_CODE as pactCode,MCARD_NO as mcardNo,PUB_COST as pubCost,OWN_COST as ownCost ,PAY_COST as payCost,TOT_COST as totCost, ");
		sb01.append(" REAL_COST as realCost,OPER_NAME as operName,OPER_DATE as operDate,CANCEL_FLAG as cancelFlag,CANCEL_INVOICE as cancelInvoice, ");
		sb01.append(" CANCEL_CODE as cancelCode,CANCEL_DATE as cancelDate,CHECK_FLAG as checkFlag,CHECK_OPCD_NAME as checkOpcdName, ");
		sb01.append(" CHECK_DATE as checkDate,BALANCE_FLAG as balanceFlag,BALANCE_OPCD_NAME as balanceOpceName,BALANCE_DATE as balanceDate, ");
		sb01.append(" INVOICE_SEQ as invoiceSeq,EXT_FLAG as extFlag,TRANS_TYPE as transType ");

		List<String> tnL = new ArrayList<String>();
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dTime = df.parse(df.format(new Date()));
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",yNum+1);
					tnL.add(0,"T_FINANCE_INVOICEINFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_FINANCE_INVOICEINFO_NOW");
			}
			if(tnL==null||tnL.size()<0){
				map.put("rows",  new ArrayList<FinanceInvoiceInfo>());
				map.put("total", 0);
				return map;
			}
		}catch (ParseException e) {
				throw new RuntimeException(e);
		}
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select ").append(sb01).append(" from ").append(tnL.get(i)).append(" where 1=1 ");
			if(StringUtils.isNotBlank(invoiceNo)){
				sb.append(" and INVOICE_NO = :invoiceNo ");
			}
			if(StringUtils.isNotBlank(stime)){
				sb.append(" and CREATETIME >= to_date(to_char(to_date(:stime,'yyyy-MM-dd'),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append(" and CREATETIME <to_date(to_char(to_date(:etime,'yyyy-MM-dd'),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		
		final StringBuffer sbData = new StringBuffer();
		sbData.append(" Select * From (Select tab.*,Rownum rn From ( ");
		sbData.append(sb.toString());
		sbData.append("  ) tab Where Rownum <=").append(p*r).append(")Where rn>").append((p-1)*r);
		List<FinanceInvoiceInfo> list = (List<FinanceInvoiceInfo>)getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<FinanceInvoiceInfo> doInHibernate(Session session)throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sbData.toString());
				query.addScalar("invoiceNo").addScalar("cardNo").addScalar("regDate",Hibernate.TIMESTAMP).addScalar("name").addScalar("paykindCode")
				.addScalar("pactCode").addScalar("mcardNo").addScalar("pubCost",Hibernate.DOUBLE).addScalar("ownCost",Hibernate.DOUBLE)
				.addScalar("payCost",Hibernate.DOUBLE).addScalar("totCost", Hibernate.DOUBLE).addScalar("realCost", Hibernate.DOUBLE).addScalar("operName")
				.addScalar("operDate",Hibernate.TIMESTAMP).addScalar("cancelFlag",Hibernate.INTEGER).addScalar("cancelInvoice").addScalar("cancelCode")
				.addScalar("cancelDate",Hibernate.TIMESTAMP).addScalar("checkFlag",Hibernate.INTEGER).addScalar("checkOpcdName").addScalar("checkDate",Hibernate.TIMESTAMP)
				.addScalar("balanceFlag",Hibernate.INTEGER).addScalar("balanceOpceName").addScalar("balanceDate",Hibernate.TIMESTAMP).addScalar("invoiceSeq")
				.addScalar("extFlag",Hibernate.INTEGER).addScalar("transType",Hibernate.INTEGER);
				if(StringUtils.isNotBlank(invoiceNo)){
					query.setParameter("invoiceNo", invoiceNo);
				}
				if(StringUtils.isNotBlank(stime)){
					query.setParameter("stime", stime);
				}
				if(StringUtils.isNotBlank(etime)){
					query.setParameter("etime", etime);
				}
				List<FinanceInvoiceInfo> list = query.setResultTransformer(Transformers.aliasToBean(FinanceInvoiceInfo.class)).list();
				return list;
			}
		});
		
		StringBuffer rowNumSb = new StringBuffer(); 
		rowNumSb.append(" select count(1) from ( ").append(sb).append(")");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(invoiceNo)){
			paraMap.put("invoiceNo", invoiceNo);
		}
		if(StringUtils.isNotBlank(etime)){
			paraMap.put("etime", etime);
		}
		if(StringUtils.isNotBlank(stime)){
			paraMap.put("stime", stime);
		}
		
		String redKey = "findInvoiceNoSummary_"+"GHYSGZLTJ"+"_"+stime+"_"+etime+"_"+invoiceNo;
		redKey=redKey.replace(",", "-");
		Integer numberRows = (Integer) redisUtil.get(redKey);
		if(numberRows==null){
			numberRows = namedParameterJdbcTemplate.queryForObject(rowNumSb.toString(), paraMap, Integer.class);
			redisUtil.set(redKey, numberRows);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		if(list==null||list.size()<0){
			list = new ArrayList<FinanceInvoiceInfo>();
		}
		
		map.put("rows", list);
		map.put("total", numberRows);
		
		return map;
	}

	@Override
	public Map<String,Object> findInvoiceDetailed(final String invoiceNo,final String stime, final String etime,String page,String rows) throws Exception {
		
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> tnL = new ArrayList<String>();
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dTime = df.parse(df.format(new Date()));
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
		
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEDETAIL",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEDETAIL",yNum+1);
					tnL.add(0,"T_FINANCE_INVOICEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_FINANCE_INVOICEDETAIL_NOW");
			}
			if(tnL==null||tnL.size()<0){
				map.put("rows",  new ArrayList<FinanceInvoicedetail>());
				map.put("total", 0);
				return map;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		StringBuffer column=new StringBuffer();
		column.append(" INVOICE_NO as invoiceNo,TRANS_TYPE as transType,INVO_SEQUENCE as invoSequence,INVO_NAME as invoName, ");
		column.append(" PUB_COST as pubCost,OWN_COST as ownCost,PAY_COST as payCost,OPER_DATE as operDate,OPER_NAME as operName, ");
		column.append(" BALANCE_FLAG as balanceFlag,BALANCE_OPCD_NAME as balanceOpceName,BALANCE_DATE as balanceDate,CANCEL_FLAG as cancelFlag ");

		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select ").append(column.toString()).append(" from ").append(tnL.get(i)).append(" where 1=1 ");
			if(StringUtils.isNotBlank(invoiceNo)){
				sb.append(" and INVOICE_NO = :invoiceNo ");
			}
			if(StringUtils.isNotBlank(stime)){
				sb.append(" and CREATETIME >= to_date(to_char(to_date(:stime,'yyyy-MM-dd'),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append(" and CREATETIME <to_date(to_char(to_date(:etime,'yyyy-MM-dd'),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ");
			}
		}
		final StringBuffer sbData = new StringBuffer();
		sbData.append(" Select * From (Select tab.*,Rownum rn From ( ");
		sbData.append(sb.toString());
		sbData.append("  ) tab Where Rownum <=").append(p*r).append(")Where rn>").append((p-1)*r);
		List<FinanceInvoicedetail> list = (List<FinanceInvoicedetail>)getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public List<FinanceInvoicedetail> doInHibernate(Session session)throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sbData.toString());
				query.addScalar("invoiceNo").addScalar("transType",Hibernate.INTEGER).addScalar("invoSequence")
				.addScalar("invoName").addScalar("pubCost",Hibernate.DOUBLE).addScalar("ownCost",Hibernate.DOUBLE)
				.addScalar("payCost", Hibernate.DOUBLE).addScalar("operDate", Hibernate.TIMESTAMP).addScalar("operName")
				.addScalar("balanceFlag",Hibernate.INTEGER).addScalar("balanceOpceName").addScalar("balanceDate", Hibernate.TIMESTAMP)
				.addScalar("cancelFlag",Hibernate.INTEGER);
				if(StringUtils.isNotBlank(invoiceNo)){
					query.setParameter("invoiceNo", invoiceNo);
				}
				if(StringUtils.isNotBlank(etime)){
					query.setParameter("etime", etime);
				}
				if(StringUtils.isNotBlank(stime)){
					query.setParameter("stime", stime);
				}
				
				List<FinanceInvoicedetail> list = query.setResultTransformer(Transformers.aliasToBean(FinanceInvoicedetail.class)).list();
				return list;
			}
		});
		
		StringBuffer rowNumSb = new StringBuffer(); 
		rowNumSb.append(" select count(1) from ( ").append(sb).append(")");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(invoiceNo)){
			paraMap.put("invoiceNo", invoiceNo);
		}
		if(StringUtils.isNotBlank(etime)){
			paraMap.put("etime", etime);
		}
		if(StringUtils.isNotBlank(stime)){
			paraMap.put("stime", stime);
		}
		
		String redKey = "findInvoiceDetailed_"+"GHYSGZLTJ"+"_"+stime+"_"+etime+"_"+invoiceNo;
		redKey=redKey.replaceAll(",", "-");
		Integer numberRows = (Integer) redisUtil.get(redKey);
		if(numberRows==null){
			numberRows = namedParameterJdbcTemplate.queryForObject(rowNumSb.toString(), paraMap, Integer.class);
			redisUtil.set(redKey, numberRows);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		
		
		if(list==null||list.size()<0){
			list = new ArrayList<FinanceInvoicedetail>();
		}
		
		map.put("rows", list);
		map.put("total", numberRows);
	
		return map;
	}

	@Override
	public Map<String,Object> findCostDetailed(final String invoiceNo,final String stime, final String etime,String page,String rows) throws Exception {
		
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		Map<String,Object> map = new HashMap<String,Object>();
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = new ArrayList<String>();
		try {
			Date dTime = df.parse(df.format(new Date()));
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
		
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
			if(tnL==null||tnL.size()<0){
				map.put("rows",  new ArrayList<OutpatientFeedetail>());
				map.put("total", 0);
				return map;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		StringBuffer column = new StringBuffer();
		column.append(" INVOICE_NO as invoiceNo,ITEM_CODE as itemCode,ITEM_NAME as itemName,UNIT_PRICE as unitPrice, ");
		column.append(" QTY as qty,DAYS as days,PUB_COST as pubCost, PAY_COST as payCost,OWN_COST as ownCost, ");
		column.append(" ECO_COST as ecoCost,INVO_CODE as invoCode,DOCT_CODENAME as doctCodename,DOCT_DEPTNAME as doctDeptname, ");
		column.append(" OPER_CODE as operCode,OPER_DATE as operDate,FEE_CPCDNAME as feeCpcdname,FEE_DATE as feeDate,INVO_SEQUENCE as invoSequence, ");
		column.append(" CANCEL_FLAG as cancelFlag,EXT_FLAG as extFlag,TRANS_TYPE as transType,EXT_FLAG2 as extFlag2,FEE_CODE as feeCode, ");
		column.append(" CLASS_CODE as classCode,RECIPE_NO as recipeNo,SEQUENCE_NO as sequenceNo,NOBACK_NUM as nobackNum, ");
		column.append(" PACK_QTY as packQty,DRUG_QUALITY as drugQuality,DRUG_FLAG as drugFlag,DOSE_MODEL_CODE as doseModelCode,SELF_MADE as selfMade, ");
		column.append(" FREQUENCY_CODE as frequencyCode,USE_NAME as useName,INJECT_NUMBER as injectNumber,EXEC_DPNM as execDpnm, ");
		column.append(" CENTER_CODE as centerCode,ITEM_GRADE as itemGrade,NEW_ITEMRATE as newItemrate, ");
		column.append(" OLD_ITEMRATE as oldItemrate,EXTEND_ONE as extendOne,COMB_NO as combNo,PACKAGE_NAME as packageName,CONFIRM_FLAG as confirmFlag, ");
		column.append(" CONFIRM_CODE as confirmCode,CONFIRM_DEPTNAME as confirmDeptname,CONFIRM_DATE as confirmDate,CONFIRM_NUM as confirmNum, ");
		column.append(" CLINIC_CODE as clinicCode,PATIENT_NO as patientNo,REG_DATE as regDate,REG_DPCDNAME as regDpcdname ");

		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select ").append(column.toString()).append(" from ").append(tnL.get(i)).append(" where PAY_FLAG = 1 ");
			if(StringUtils.isNotBlank(invoiceNo)){
				sb.append(" and INVOICE_NO =:invoiceNo ");
			}
			if(StringUtils.isNotBlank(stime)){
				sb.append(" and FEE_DATE >= to_date(to_char(to_date(:stime,'yyyy-MM-dd'),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append(" and FEE_DATE <to_date(to_char(to_date(:etime,'yyyy-MM-dd'),'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ");
			}
			
		}
		final StringBuffer sbData = new StringBuffer();
		sbData.append(" Select * From (Select tab.*,Rownum rn From ( ");
		sbData.append(sb.toString());
		sbData.append("  ) tab Where Rownum <=").append(p*r).append(")Where rn>").append((p-1)*r);
		List<OutpatientFeedetail> list = (List<OutpatientFeedetail>)this.getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public List<OutpatientFeedetail> doInHibernate(Session session)throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sbData.toString());
				query.addScalar("invoiceNo").addScalar("itemCode").addScalar("itemName").addScalar("unitPrice",Hibernate.DOUBLE)
				.addScalar("qty",Hibernate.DOUBLE).addScalar("days",Hibernate.INTEGER).addScalar("pubCost",Hibernate.DOUBLE)
				.addScalar("payCost", Hibernate.DOUBLE).addScalar("ownCost", Hibernate.DOUBLE).addScalar("ecoCost", Hibernate.DOUBLE)
				.addScalar("invoCode").addScalar("doctCodename").addScalar("doctDeptname").addScalar("operCode").addScalar("operDate", Hibernate.TIMESTAMP)
				.addScalar("feeCpcdname").addScalar("feeDate",Hibernate.TIMESTAMP).addScalar("invoSequence").addScalar("cancelFlag",Hibernate.INTEGER)
				.addScalar("extFlag",Hibernate.INTEGER).addScalar("transType",Hibernate.INTEGER).addScalar("extFlag2",Hibernate.INTEGER)
				.addScalar("feeCode").addScalar("classCode").addScalar("recipeNo").addScalar("sequenceNo",Hibernate.INTEGER).addScalar("drugFlag").addScalar("nobackNum", Hibernate.DOUBLE)
				.addScalar("packQty",Hibernate.INTEGER).addScalar("drugQuality").addScalar("drugFlag").addScalar("doseModelCode").addScalar("selfMade",Hibernate.INTEGER)
				.addScalar("frequencyCode").addScalar("useName").addScalar("injectNumber",Hibernate.INTEGER).addScalar("execDpnm").addScalar("centerCode")
				.addScalar("itemGrade",Hibernate.INTEGER).addScalar("newItemrate",Hibernate.DOUBLE).addScalar("oldItemrate",Hibernate.DOUBLE).addScalar("extendOne").addScalar("combNo").addScalar("packageName")
				.addScalar("confirmFlag",Hibernate.INTEGER).addScalar("confirmCode").addScalar("confirmDeptname").addScalar("confirmDate",Hibernate.TIMESTAMP)
				.addScalar("confirmNum",Hibernate.DOUBLE).addScalar("clinicCode").addScalar("patientNo").addScalar("regDate",Hibernate.TIMESTAMP)
				.addScalar("regDpcdname");
				if(StringUtils.isNotBlank(invoiceNo)){
					query.setParameter("invoiceNo", invoiceNo);
				}
				if(StringUtils.isNotBlank(stime)){
					query.setParameter("stime", stime);
				}
				if(StringUtils.isNotBlank(etime)){
					query.setParameter("etime", etime);
				}
				
				List<OutpatientFeedetail> list = query.setResultTransformer(Transformers.aliasToBean(OutpatientFeedetail.class)).list();
				return list;
			}
		});
		StringBuffer rowNumSb = new StringBuffer(); 
		rowNumSb.append(" select count(1) from ( ").append(sb).append(")");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(invoiceNo)){
			paraMap.put("invoiceNo", invoiceNo);
		}
		if(StringUtils.isNotBlank(etime)){
			paraMap.put("etime", etime);
		}
		if(StringUtils.isNotBlank(stime)){
			paraMap.put("stime", stime);
		}
		
		String redKey = "findCostDetailed_"+"GHYSGZLTJ"+"_"+stime+"_"+etime+"_"+invoiceNo;
		redKey=redKey.replaceAll(",", "-");
		Integer numberRows = (Integer) redisUtil.get(redKey);
		if(numberRows==null){
			numberRows = namedParameterJdbcTemplate.queryForObject(rowNumSb.toString(), paraMap, Integer.class);
			redisUtil.set(redKey, numberRows);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		
		if(list==null||list.size()<=0){
			list =  new ArrayList<OutpatientFeedetail>();
		}
		
		map.put("rows", list);
		map.put("total", numberRows);
		
		return map;
	}


	@Override
	public List<MinfeeStatCode> itemFunction() {
		
		String hql = "from MinfeeStatCode where del_flg=0 and stop_flg=0";
		List<MinfeeStatCode> minfeeStatCodeList = super.find(hql, null);
		if(minfeeStatCodeList==null||minfeeStatCodeList.size()<=0){
			return new ArrayList<MinfeeStatCode>();
		}
		
		return minfeeStatCodeList;
	}
	
	/**
	 * @Description <p>通过表明和字段名 获取业务表中最大及最小时间 </p>
	 * @author  marongbin
	 * @createDate： 2016年12月5日 下午7:10:11 
	 * @modifier 
	 * @modifyDate：
	 * @param tbname 表明
	 * @param column 字段名
	 * @return: StatVo
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public StatVo findMaxMinByTbNameColumn(String tbname,String column){
		
		final String sql = "SELECT MAX(mn."+column+") AS eTime ,MIN(mn."+column+") AS sTime FROM "+tbname+" mn";
		
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		
		return vo;
	}
	
}
