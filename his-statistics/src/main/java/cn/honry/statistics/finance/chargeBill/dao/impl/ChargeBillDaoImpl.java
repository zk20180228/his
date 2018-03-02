package cn.honry.statistics.finance.chargeBill.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.finance.chargeBill.dao.ChargeBillDao;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.NumberUtil;
/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单dao实现类
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
@Repository("chargeBillDao")
@SuppressWarnings({"all"})
public class ChargeBillDaoImpl extends HibernateEntityDao<ChargeBillVo> implements ChargeBillDao {
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 * @param sessionFactory
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	//扩展工具类,支持参数名传参
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		
	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String medId) throws Exception{
		
		String hql="from InpatientInfoNow  t where ";
		String signCoulum=null;
		if(medId.startsWith("SFZ:")){
			signCoulum=medId.split(":")[1];
			hql+="t.certificatesNo='"+signCoulum+"'";
		}else{
			hql+="t.medicalrecordId='"+medId+"'";
		}
		List<InpatientInfoNow> info1=super.find(hql, null);
		if(info1.size()>0){
			return info1;
		}else{
			if(null!=signCoulum){
				 hql = "from InpatientInfo m where  m.certificatesNo='"+signCoulum+"'";
			}else{
				 hql = "from InpatientInfo m where  m.medicalrecordId='"+medId+"'";
			}
			 info1=super.find(hql, null);
			if(info1.size()>0){
				return info1;
			}
			return new ArrayList<InpatientInfoNow>();
		}
	}
	@Override
	public List<ChargeBillVo> queryDatagridinfo(List<String> tnlItem, List<String> tnlMed, String inpatientNo,String startTime,String endTime,String sendFlag) throws Exception{
		String [] inpatientNoList=inpatientNo.split(",");
		//查询非药品明细表
		if(tnlItem==null||tnlItem.size()<0){
			return new ArrayList<ChargeBillVo>();
		}
		final StringBuffer sb = new StringBuffer();
		if(tnlItem.size()>1){
			sb.append("SELECT itemName,currentUnit,executeDeptcode,chargeOpercode,totCost,ownCost,payCost,pubCost,ecoCost,unitPrice,qty,chargeDate FROM( ");
		}
		for(int i=0;i<tnlItem.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("SELECT rm").append(i).append(".ITEM_NAME AS itemName,rm").append(i).append(".CURRENT_UNIT AS currentUnit,rm")
			.append(i).append(".EXECUTE_DEPTCODE AS executeDeptcode,rm").append(i).append(".FEE_OPERCODE AS chargeOpercode,rm")
			.append(i).append(".TOT_COST AS totCost, rm").append(i).append(".OWN_COST AS ownCost,rm").append(i).append(".PAY_COST AS payCost,rm")
			.append(i).append(".PUB_COST AS pubCost, rm").append(i).append(".ECO_COST AS ecoCost,rm").append(i).append(".UNIT_PRICE AS unitPrice,rm")
			.append(i).append(".QTY AS qty ,rm").append(i).append(".FEE_DATE AS chargeDate ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnlItem.get(i)).append(" rm").append(i).append(" WHERE rm")
			.append(i).append(".DEL_FLG = 0 AND rm").append(i).append(".STOP_FLG = 0 AND rm").append(i).append(".inpatient_no in(:inpatientNo) ");
			
		    if(StringUtils.isNotBlank(sendFlag)&&!"3".equals(sendFlag)){
		    	sb.append(" AND rm").append(i).append(".SEND_FLAG =:sendFlag");
			 }
			 if(StringUtils.isNotBlank(startTime)){
				sb.append(" AND rm").append(i).append(".charge_date > to_date (:startTime,'yyyy-MM-dd hh24:mi:ss') ");
			 }
			 if(StringUtils.isNotBlank(endTime)){
				sb.append(" AND rm").append(i).append(".charge_date < to_date (:endTime,'yyyy-MM-dd hh24:mi:ss') ");
			 }
		}
		if(tnlItem.size()>1){
			sb.append(") ");
		}   
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(sendFlag)){
			paraMap.put("sendFlag", sendFlag);
		 }
		 if(StringUtils.isNotBlank(startTime)){
			 startTime=startTime+" 00:00:00";
			 paraMap.put("startTime", startTime);
		 }
		 if(StringUtils.isNotBlank(endTime)){
			 endTime=endTime+" 23:59:59";
			 paraMap.put("endTime", endTime);
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 paraMap.put("inpatientNo", inpatientNo);
		 }
		final NumberUtil number=NumberUtil.init();
		List<ChargeBillVo> cbl =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<ChargeBillVo>() {
			@Override
			public ChargeBillVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChargeBillVo vo = new ChargeBillVo();
				
				vo.setItemName(rs.getString("itemName"));
				vo.setCurrentUnit(rs.getString("currentUnit"));
				vo.setExecuteDeptcode(rs.getString("executeDeptcode"));
				vo.setChargeOpercode(rs.getString("chargeOpercode"));
				vo.setTotCost(Double.valueOf(number.format(rs.getDouble("totCost"),2)));
				vo.setOwnCost(Double.valueOf(number.format(rs.getDouble("ownCost"),2)));
				vo.setPayCost(Double.valueOf(number.format(rs.getDouble("payCost"),2)));
				vo.setPubCost(Double.valueOf(number.format(rs.getDouble("pubCost"),2)));
				vo.setEcoCost(Double.valueOf(number.format(rs.getDouble("ecoCost"),2)));
				vo.setUnitPrice(Double.valueOf(number.format(rs.getDouble("unitPrice"),2)));
				vo.setQty(Double.valueOf(number.format(rs.getDouble("qty"),2)));
				vo.setChargeDate(rs.getDate("chargeDate"));
				return vo;
		}});

		for(int q=0;q<cbl.size();q++){
			cbl.get(q).setState("f");
			if(cbl.get(q).getOwnCost()==null){
				cbl.get(q).setOwnCost(0.00);
			}
			if(cbl.get(q).getPayCost()==null){
				cbl.get(q).setPayCost(0.00);
			}
		}
		
		
		//查询药品明细表代码
		if(tnlMed==null||tnlMed.size()<0){
			return new ArrayList<ChargeBillVo>();
		}
		final StringBuffer sb1 = new StringBuffer();
		if(tnlMed.size()>1){
			sb1.append("SELECT itemName,currentUnit,executeDeptcode,chargeOpercode,totCost,ownCost,payCost,pubCost,ecoCost,unitPrice,qty,chargeDate FROM( ");
		}
		for(int i=0;i<tnlMed.size();i++){
			if(i!=0){
				sb1.append("UNION ");
			}
			sb1.append("SELECT rm").append(i).append(".DRUG_NAME AS itemName,rm").append(i).append(".CURRENT_UNIT AS currentUnit,rm")
			.append(i).append(".EXECUTE_DEPTCODE AS executeDeptcode,rm").append(i).append(".FEE_OPERCODE AS chargeOpercode,rm")
			.append(i).append(".TOT_COST AS totCost, rm").append(i).append(".OWN_COST AS ownCost,rm").append(i).append(".PAY_COST AS payCost,rm")
			.append(i).append(".PUB_COST AS pubCost, rm").append(i).append(".ECO_COST AS ecoCost,rm").append(i).append(".UNIT_PRICE AS unitPrice,rm")
			.append(i).append(".QTY AS qty ,rm").append(i).append(".FEE_DATE AS chargeDate ");
			sb1.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnlMed.get(i)).append(" rm").append(i).append(" WHERE rm")
			.append(i).append(".DEL_FLG = 0 AND rm").append(i).append(".STOP_FLG = 0 AND rm").append(i).append(".inpatient_no  in(:inpatientNo) ");
			
		    if(StringUtils.isNotBlank(sendFlag)&&!"3".equals(sendFlag)){
		    	sb1.append(" AND rm").append(i).append(".SENDDRUG_FLAG =:sendFlag");
			 }
			 if(StringUtils.isNotBlank(startTime)){
				 sb1.append(" AND rm").append(i).append(".charge_date > to_date (:startTime,'yyyy-MM-dd hh24:mi:ss') ");
			 }
			 if(StringUtils.isNotBlank(endTime)){
				 sb1.append(" AND rm").append(i).append(".charge_date < to_date (:endTime,'yyyy-MM-dd hh24:mi:ss') ");
			 }
		}
		if(tnlMed.size()>1){
			sb1.append(") ");
		}   
		Map<String, Object> paraMap2 = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(sendFlag)){
			paraMap2.put("sendFlag", sendFlag);
		 }
		 if(StringUtils.isNotBlank(startTime)){
			 paraMap2.put("startTime", startTime);
		 }
		 if(StringUtils.isNotBlank(endTime)){
			 paraMap2.put("endTime", endTime);
		 }
		 if(StringUtils.isNotBlank(inpatientNo)){
			 paraMap2.put("inpatientNo", inpatientNo);
		 }
		List<ChargeBillVo> cbl1 =  namedParameterJdbcTemplate.query(sb1.toString(),paraMap2,new RowMapper<ChargeBillVo>() {
			@Override
			public ChargeBillVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChargeBillVo vo = new ChargeBillVo();
				vo.setItemName(rs.getString("itemName"));
				vo.setCurrentUnit(rs.getString("currentUnit"));
				vo.setExecuteDeptcode(rs.getString("executeDeptcode"));
				vo.setChargeOpercode(rs.getString("chargeOpercode"));
				vo.setTotCost(Double.valueOf(number.format(rs.getDouble("totCost"),2)));
				vo.setOwnCost(Double.valueOf(number.format(rs.getDouble("ownCost"),2)));
				vo.setPayCost(Double.valueOf(number.format(rs.getDouble("payCost"),2)));
				vo.setPubCost(Double.valueOf(number.format(rs.getDouble("pubCost"),2)));
				vo.setEcoCost(Double.valueOf(number.format(rs.getDouble("ecoCost"),2)));
				vo.setUnitPrice(Double.valueOf(number.format(rs.getDouble("unitPrice"),2)));
				vo.setQty(Double.valueOf(number.format(rs.getDouble("qty"),2)));
				vo.setChargeDate(rs.getDate("chargeDate"));
				return vo;
		}});
		for(int w=0;w<cbl1.size();w++){
			cbl1.get(w).setState("y");
			if(cbl1.get(w).getOwnCost()==null){
				cbl1.get(w).setOwnCost(0.00);
			}
			if(cbl1.get(w).getPayCost()==null){
				cbl1.get(w).setPayCost(0.00);
			}
		}
		if(cbl!=null&&cbl.size()>0&&cbl1!=null&&cbl1.size()>0){
			for(int i=0;i<cbl.size();i++){
				cbl1.add(cbl.get(i));
			}
			return cbl1;
		}else if(cbl1==null||cbl1.size()==0){
			if(cbl!=null&&cbl.size()>0){
				return cbl;
			}
		}else if(cbl==null||cbl.size()==0){
			if(cbl1!=null&&cbl1.size()>0){
				return cbl1;
			}
		}
		return new ArrayList<ChargeBillVo>();
	}

	@Override
	public List<SysDepartment> queryZYDept() {
		String hql=" from SysDepartment where del_flg=0 and stop_flg=0 ";
		List<SysDepartment> sdl=super.find(hql, null);
		if(sdl!=null&&sdl.size()>0){
			return sdl;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<Patient> queryPatientbill(String medicalrecordId) throws Exception{
		String hql="from Patient  p where p.del_flg=0 and p.stop_flg=0 and p.medicalrecordId like'%"+medicalrecordId+"'";
		List<Patient> patientlist =super.find(hql, null);
		if(patientlist.size()>0&&patientlist!=null){
			return patientlist;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public List<User> queryEmpbill() {
		String hql="from User where del_flg=0 and stop_flg=0 ";
		List<User> sel=super.find(hql, null);
		if(sel!=null&&sel.size()>0){
			return sel;
		}
		return new ArrayList<User>();
	}

	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMinItem() throws Exception{
		final String sql = "SELECT MAX(mn.charge_date) AS eTime ,MIN(mn.charge_date) AS sTime FROM T_INPATIENT_ITEMLIST_NOW mn";
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
	
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMinMed() throws Exception{
		final String sql = "SELECT MAX(mn.charge_date) AS eTime ,MIN(mn.charge_date) AS sTime FROM T_INPATIENT_MEDICINELIST_NOW mn";
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
