package cn.honry.inpatient.delivery.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientStoMsg;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.delivery.dao.DeliveryDAO;
import cn.honry.inpatient.delivery.dao.DeliveryNowDAO;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
import cn.honry.utils.HisParameters;
/**
 * 摆药单数据库层
 * @author  lyy
 * @createDate： 2015年12月28日 上午10:51:13 
 * @modifier lyy
 * @modifyDate：2015年12月28日 上午10:51:13  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository(value="deliveryNowDao")
@SuppressWarnings({"all"})
public class DeliveryNowDAOImpl extends HibernateEntityDao<DrugApplyoutNow> implements DeliveryNowDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<DrugApplyoutNow> getDrugPage(String hql, String page, String rows) {
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getDrugTotal(String hql) {
		return super.getTotal(hql);
	}
	@Override
	public List<DeliveryVo> iReportInvoiceBill(String tid,String drugedbill) {
		StringBuffer buffer=new StringBuffer(530);
		buffer.append("SELECT DISTINCT O.INPATIENT_NO AS id,");
		buffer.append("(SELECT K.TYPE_NAME FROM T_INPATIENT_KIND K WHERE K.TYPE_CODE = T.ORDER_TYPE) AS drugCode,");
		buffer.append("(SELECT D.DEPT_NAME FROM T_DEPARTMENT D WHERE D.DEPT_CODE = O.DEPT_CODE) AS choose,");
		buffer.append("(SELECT D.DEPT_NAME FROM T_DEPARTMENT D WHERE D.DEPT_CODE = T.DRUG_DEPT_CODE) AS deptCode ");
		buffer.append("FROM T_DRUG_APPLYOUT_NOW  T,T_INPATIENT_INFO_NOW O,T_DRUG_BILLCLASS A ");
		buffer.append("WHERE T.BILLCLASS_CODE = A.BILLCLASS_CODE AND T.PATIENT_ID = O.INPATIENT_NO ");
		buffer.append("AND T.PATIENT_ID ='"+tid+"' AND T.DRUGED_BILL ='"+drugedbill+"'");
		List<DeliveryVo> list=super.getSession().createSQLQuery(buffer.toString()).addScalar("id")
				.addScalar("choose").addScalar("drugCode").addScalar("deptCode").setResultTransformer(Transformers.aliasToBean(DeliveryVo.class))
				.list();
		buffer=null;
		buffer=new StringBuffer(570);
		buffer.append("SELECT I.PATIENT_NAME AS name,I.MEDICALRECORD_ID AS inpatientNo,");
		buffer.append("I.BED_NAME AS bedId,T.TRADE_NAME AS drugName,");
		buffer.append("T.SPECS AS specs,");
		buffer.append("(SELECT T.CODE_NAME FROM T_BUSINESS_DICTIONARY T ");
		buffer.append("WHERE T.CODE_TYPE='PACKUNIT' AND T.CODE_ENCODE=T.PACK_UNIT) AS minUnit,");
		buffer.append("TO_CHAR(T.DOSE_ONCE) AS doseOnce,T.DFQ_CEXP AS dfqFreq,T.USE_NAME AS useName,");
		buffer.append("T.APPLY_NUM AS applyNumSum,TO_CHAR(T.RETAIL_PRICE*T.APPLY_NUM) AS RetailPrice");
		buffer.append(" FROM T_DRUG_APPLYOUT_NOW T, T_INPATIENT_INFO_NOW I WHERE T.PATIENT_ID = I.INPATIENT_NO ");
		buffer.append("AND T.PATIENT_ID ='"+tid+"' AND T.DRUGED_BILL ='"+drugedbill+"'");
		List<DeliveryVo> listBean=super.getSession().createSQLQuery(buffer.toString()).addScalar("name").addScalar("bedId")
				.addScalar("inpatientNo").addScalar("specs").addScalar("drugName").addScalar("applyNumSum",Hibernate.INTEGER).addScalar("minUnit")
				.addScalar("doseOnce").addScalar("dfqFreq").addScalar("useName").addScalar("RetailPrice",Hibernate.DOUBLE).setResultTransformer(Transformers.aliasToBean(DeliveryVo.class))
				.list();
		for(DeliveryVo vo:list){
			vo.setDeliverList(listBean);
		}
		
		if(list.size()>0){
			return list;
		}
		return new ArrayList<DeliveryVo>();
	}
	
}
