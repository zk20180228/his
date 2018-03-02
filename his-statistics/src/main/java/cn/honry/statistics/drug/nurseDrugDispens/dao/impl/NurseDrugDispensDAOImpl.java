package cn.honry.statistics.drug.nurseDrugDispens.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.info.vo.InfoInInterVo;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.nurseDrugDispens.dao.NurseDrugDispensDAO;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensDetailVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Repository("nurseDrugDispensDAO")
@SuppressWarnings({ "all" })
public class NurseDrugDispensDAOImpl extends HibernateEntityDao<InpatientInfo>  implements NurseDrugDispensDAO{
	private static final int List = 0;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<DrugDispensSumVo> queryDrugDispensSum(List<String> tnL, InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,
			String inpatientNoSerc,String type,String page,String rows) throws Exception{
		Integer firstResult=null;
		Integer lastResult=null;
		if(StringUtils.isNotBlank(page)){
			Integer p=Integer.parseInt(page);
			Integer r=Integer.parseInt(rows);
			firstResult=(p-1)*r;
			 lastResult=firstResult+r;
		}
		if(tnL==null||tnL.size()<0){
			return new ArrayList<DrugDispensSumVo>();
		}
		final StringBuffer sb = new StringBuffer(1400);
		sb.append(" select * from (select  rownum as rn,t.* from ( ");
		if(tnL.size()>1){
			if(tnL.size()>1){
				sb.append("select drugId,spec,unit,drugDosageform,qtys,drugRetailprice,billclassName,totCosts,deptDrugName,");
				sb.append("deptName,drugCommonname from ( ");
			}
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("select  rm").append(i).append(".Drug_Code as drugId, rm").append(i).append(".SPECS as spec,rm")
			.append(i).append(".PACK_UNIT as unit ,rm").append(i).append(".DOSE_MODEL_CODE as drugDosageform,  rm")
			.append(i).append(".Apply_Num as qtys, rm").append(i).append(".Retail_Price as drugRetailprice,  rm").append(i).append(".BILLCLASS_CODE as billclassName,rm")
			.append(i).append(".Apply_Num * rm").append(i).append(".Retail_Price as totCosts ,rm")
			.append(i).append(".DRUG_DEPT_NAME as deptDrugName, rm").append(i).append(".dept_name as deptName ,rm").append(i).append(".trade_name as drugCommonname ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append("  WHERE rm").append(i).append(".apply_state");
			
			if("0".equals(type)){
				sb.append(" <>2 ");
			}else if("1".equals(type)){
				sb.append(" =2 ");
			}
			sb.append(" AND rm").append(i).append(".OP_TYPE in(4,5) ");
			if(StringUtils.isNotBlank(inpatientInfo.getInpatientNo())){
				String [] inpatientNo=inpatientInfo.getInpatientNo().split(",");
				String inNo="";
				String inNo1="";
				for(int j=0;j<inpatientNo.length;j++){
					if(inNo.split(",").length<999){
						if(!"".equals(inpatientNo[j])){
							inNo = inNo + "','";
						}
						inNo = inNo + inpatientNo[j];
					}else{
						if(!"".equals(inpatientNo[j])){
							inNo1 = inNo1 + "','";
						}
						inNo1 = inNo1 + inpatientNo[j];
					}
				}
				if(inNo.split(",").length==0){
					sb.append(" AND rm").append(i).append(".patient_id in('"+inNo+"')");
				}else{
					sb.append(" AND (rm").append(i).append(".patient_id in('"+inNo+"') or rm").append(i).append(".patient_id in('"+inNo1+"'))");
				}
			}
			if(StringUtils.isNotBlank(drugName)){
				sb.append(" AND rm").append(i).append(".trade_name like :drugName ");
			}
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" AND (rm").append(i).append(".apply_date >=to_date(:startTime, 'yyyy-mm-dd hh24:mi:ss')  and	rm").append(i).append(".apply_date <to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss')) ");

			}
		}
		if(tnL.size()>1){
			sb.append(") ");
		}
		//报表查全纪录
		sb.append(") t ) m  where 1=1");
		if(StringUtils.isNotBlank(page)){
			sb.append(" and m.rn > "+firstResult+"  and m.rn <="+lastResult);
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(drugName)){
			paraMap.put("drugName", "%"+drugName+"%" );
		}
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			paraMap.put("startTime", startTime);
			paraMap.put("endTime", endTime);
		}
		  	List<DrugDispensSumVo> list = null;
				try {

					list = namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<DrugDispensSumVo>() {
						@Override
						public DrugDispensSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {
							DrugDispensSumVo vo = new DrugDispensSumVo();
							vo.setDrugId(rs.getString("drugId"));
							vo.setSpec(rs.getString("spec"));
							vo.setUnit(rs.getString("unit"));
							vo.setDrugDosageform(rs.getString("drugDosageform"));
							vo.setQtys(rs.getInt("qtys"));
							vo.setDrugRetailprice(rs.getDouble("drugRetailprice"));
							vo.setBillclassName(rs.getString("billclassName"));
							vo.setTotCosts(rs.getDouble("totCosts"));
							vo.setDeptDrugName(rs.getString("deptDrugName"));
							vo.setDeptName(rs.getString("deptName"));
							vo.setDrugCommonname(rs.getString("drugCommonname"));
							return vo;
						}
						
					});
				} catch (HibernateException e) {
					e.printStackTrace();
				}
		  	if(list!=null && list.size()>0){
		  		return list;
		  	}
		  	return new ArrayList<DrugDispensSumVo>();	     
				
	}
	@Override
	public List<DrugDispensDetailVo> queryDrugDispensDetail(List<String> tnL, InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,
			String inpatientNoSerc, String type,String page,String rows) throws Exception{
		Integer firstResult=null;
		Integer lastResult=null;
		if(StringUtils.isNotBlank(page)){
			Integer p=Integer.parseInt(page);
			Integer r=Integer.parseInt(rows);
			firstResult=(p-1)*r;
			lastResult=firstResult+r;
		}
		if(tnL==null||tnL.size()<0){
			return new ArrayList<DrugDispensDetailVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select * from (select  rownum as rn,t.* from ( ");
		if(tnL.size()>1){
			sb.append("select bedName,patientName,drugId,spec,unit,drugDosageform,qtys,drugRetailprice,billclassName,totCosts,deptDrugName,");
			sb.append("deptName,drugCommonname from ( ");
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			String table="";
			if("T_DRUG_APPLYOUT_NOW".equals(tnL.get(i))){
				table="T_INPATIENT_INFO_now";
			}else{
				table="T_INPATIENT_INFO";
			}

			sb.append("select  i.bed_name as bedName,i.patient_name as patientName, rm").append(i).append(".Drug_Code as drugId, rm").append(i).append(".SPECS as spec,rm")
			.append(i).append(".PACK_UNIT as unit ,rm").append(i).append(".DOSE_MODEL_CODE as drugDosageform,  rm")
			.append(i).append(".Apply_Num as qtys, rm").append(i).append(".Retail_Price as drugRetailprice,  rm").append(i).append(".BILLCLASS_CODE as billclassName,rm")
			.append(i).append(".Apply_Num * rm").append(i).append(".Retail_Price as totCosts ,rm")
			.append(i).append(".DRUG_DEPT_NAME as deptDrugName, rm").append(i).append(".dept_name as deptName ,rm").append(i).append(".trade_name as drugCommonname ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" join ").append(table)
			.append(" i on rm").append(i).append(".patient_id=i.inpatient_no ").append(" WHERE rm").append(i).append(".apply_state");
			if("0".equals(type)){
				sb.append(" <>2 ");
			}else if("1".equals(type)){
				sb.append(" =2 ");
			}
			sb.append(" AND rm").append(i).append(".OP_TYPE in(4,5) ");
			if(StringUtils.isNotBlank(inpatientInfo.getInpatientNo())){
				String [] inpatientNo=inpatientInfo.getInpatientNo().split(",");
				String inNo="";
				for(int j=0;j<inpatientNo.length;j++){
					if(!"".equals(inpatientNo[j])){
						inNo = inNo + "','";
					}
					inNo = inNo + inpatientNo[j];
				}

				sb.append(" AND rm").append(i).append(".patient_id in ('"+inNo+"') ");
			}
			
			
			if(StringUtils.isNotBlank(drugName)){
				sb.append(" AND rm").append(i).append(".trade_name like :drugName ");
			}
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" AND (rm").append(i).append(".apply_date >= to_date(:startTime, 'yyyy-mm-dd hh24:mi:ss')  and	rm").append(i).append(".apply_date <to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss')) ");

			}
		}
		if(tnL.size()>1){
			sb.append(") ");
		}
		sb.append(") t ) m  where 1=1");
		if(StringUtils.isNotBlank(page)){
			sb.append(" and m.rn > "+firstResult+"  and m.rn <="+lastResult);
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(drugName)){
			paraMap.put("drugName", "%"+drugName+"%");
		}
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			paraMap.put("startTime", startTime);
			paraMap.put("endTime", endTime);
		}
		List<DrugDispensDetailVo> list =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<DrugDispensDetailVo>() {

			@Override
			public DrugDispensDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugDispensDetailVo vo = new DrugDispensDetailVo();
				vo.setBedName(rs.getString("bedName"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setDrugId(rs.getString("drugId"));
				vo.setSpec(rs.getString("spec"));
				vo.setUnit(rs.getString("unit"));
				vo.setDrugDosageform(rs.getString("drugDosageform"));
				vo.setQtys(rs.getInt("qtys"));
				vo.setDrugRetailprice(rs.getDouble("drugRetailprice"));
				vo.setBillclassName(rs.getString("billclassName"));
				vo.setTotCosts(rs.getDouble("totCosts"));
				vo.setDeptDrugName(rs.getString("deptDrugName"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setDrugCommonname(rs.getString("drugCommonname"));
				return vo;
			}
			
		});
		return list;
	}
	
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMin() throws Exception{
		final String sql = "SELECT MAX(mn.apply_date) AS eTime ,MIN(mn.apply_date) AS sTime FROM T_DRUG_APPLYOUT_NOW mn";
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
	
	
	@Override
	public List<InfoInInterVo> treeNurseCharge(String deptId, String type,
			InpatientInfoNow inpatientInfo, String a, String startTime,
			String endTime) throws Exception{
    	String hql="select DISTINCT b.INPATIENT_id as id,b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
				+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,b.EMPL_CODE as emplCode,"
				+ "b.BED_NAME as bedName,b.INPATIENT_NO as inpatientNo from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW b ";
				if("0".equals(type)){
					if("1".equals(a)){
						hql+=" inner join T_DRUG_APPLYOUT_NOW d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE <>'2' where  b.IN_STATE in('I') ";
					}else if("2".equals(a)){
						hql+=" inner join T_DRUG_APPLYOUT_NOW d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE <>'2' where  b.IN_STATE in('O') ";
					}else{
						hql+=" inner join T_DRUG_APPLYOUT_NOW d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE <>'2' where  b.IN_STATE in('O','I') ";
					}
				
				}else if("1".equals(type)){
					if("1".equals(a)){
						hql+=" inner join T_DRUG_APPLYOUT_NOW d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE ='2' where  b.IN_STATE in('I') ";
					}else if("2".equals(a)){
						hql+=" inner join T_DRUG_APPLYOUT_NOW d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE ='2' where  b.IN_STATE in('O') ";
					}else{
						hql+=" inner join T_DRUG_APPLYOUT_NOW d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE ='2' where  b.IN_STATE in('O','I') ";
					}
				}
		
        	   if(StringUtils.isNotBlank(deptId)){
                		hql+=" and  b.NURSE_CELL_CODE= :deptId ";
        		}
				if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
					hql +="and b.MEDICALRECORD_ID like :MedicalrecordId ";
				}
				if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
					hql +="and b.in_Date between to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ";
				}
				if("0".equals(type)){
					if("2".equals(a)){
						 hql+=" union  select DISTINCT b.INPATIENT_id as id,b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
								+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,b.EMPL_CODE as emplCode,"
								+ "b.BED_NAME as bedName,b.INPATIENT_NO as inpatientNo from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO b ";
				            	hql+=" inner join T_DRUG_APPLYOUT d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE <>'2' where  b.IN_STATE in('O') ";
				        	   if(StringUtils.isNotBlank(deptId)){
				                		hql+=" and  b.NURSE_CELL_CODE= :deptId ";
				        		}
								if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
									hql +="and b.MEDICALRECORD_ID like :MedicalrecordId ";
								}
								if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
									hql +="and b.in_Date between to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ";
								}
					}else if("12".equals(a)){
						 hql+=" union  select DISTINCT b.INPATIENT_id as id,b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
									+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,b.EMPL_CODE as emplCode,"
									+ "b.BED_NAME as bedName,b.INPATIENT_NO as inpatientNo from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO b ";
					            	hql+=" inner join T_DRUG_APPLYOUT d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE <>'2' where  b.IN_STATE in('O','I') ";
					        	   if(StringUtils.isNotBlank(deptId)){
					                		hql+=" and  b.NURSE_CELL_CODE= :deptId ";
					        		}
									if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
										hql +="and b.MEDICALRECORD_ID like :MedicalrecordId ";
									}
									if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
										hql +="and b.in_Date between to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ";
									}
					}
				}else{
					if("2".equals(a)){
						 hql+=" union  select DISTINCT b.INPATIENT_id as id,b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
								+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,b.EMPL_CODE as emplCode,"
								+ "b.BED_NAME as bedName,b.INPATIENT_NO as inpatientNo from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO b ";
				            	hql+=" inner join T_DRUG_APPLYOUT d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE ='2' where  b.IN_STATE in('O') ";
				        	   if(StringUtils.isNotBlank(deptId)){
				                		hql+=" and  b.NURSE_CELL_CODE= :deptId ";
				        		}
								if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
									hql +="and b.MEDICALRECORD_ID like :MedicalrecordId ";
								}
								if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
									hql +="and b.in_Date between to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ";
								}
						
					}else if("12".equals(a)){
						hql+=" union  select DISTINCT b.INPATIENT_id as id,b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
								+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,b.EMPL_CODE as emplCode,"
								+ "b.BED_NAME as bedName,b.INPATIENT_NO as inpatientNo from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO b ";
				            	hql+=" inner join T_DRUG_APPLYOUT d on d.PATIENT_ID=b.INPATIENT_NO and d.APPLY_STATE ='2' where  b.IN_STATE in('O','I') ";
				        	   if(StringUtils.isNotBlank(deptId)){
				                		hql+=" and  b.NURSE_CELL_CODE= :deptId ";
				        		}
								if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
									hql +="and b.MEDICALRECORD_ID like :MedicalrecordId ";
								}
								if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
									hql +="and b.in_Date between to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ";
								}
					}
				}
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("medicalrecordId").addScalar("patientName")
				.addScalar("inDate",Hibernate.DATE).addScalar("deptCode").addScalar("bedId").addScalar("moneyAlert",Hibernate.DOUBLE)
				.addScalar("freeCost",Hibernate.DOUBLE).addScalar("emplCode").addScalar("bedName").addScalar("inpatientNo");
		if(StringUtils.isNotBlank(deptId)){
			queryObject.setParameter("deptId", deptId);
    	}
		if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
			queryObject.setParameter("MedicalrecordId", "%"+inpatientInfo.getMedicalrecordId()+"%");
		}
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			queryObject.setParameter("startTime", startTime);
			queryObject.setParameter("endTime", endTime);
		}
		
		
		List<InfoInInterVo> iList = null;
		try {
			iList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoInInterVo.class)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		if(iList!=null&&iList.size()>0){
			return iList; 
		}
		 return null;
	}
	@Override
	public List<InpatientInfoNow> findByinpatientNo(String inpatientNo) throws Exception{
		String hql = "from InpatientInfoNow where inpatientNo=?";
		List<InpatientInfoNow>  registerInfoList = super.find(hql, inpatientNo);
		if(registerInfoList!=null&&registerInfoList.size()>0){
			return registerInfoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public List<DrugBillclass> queryBillNameList() {
		String hql = "from DrugBillclass where stop_flg=0 and del_flg=0";
		List<DrugBillclass> billclassList = super.find(hql, null);
		if(billclassList!=null&&billclassList.size()>0){
			return billclassList;
		}
		return  new ArrayList<DrugBillclass>();
	}
	//摆药汇总查询
	@Override
	public int queryDrugDispensSumTotal(java.util.List<String> tnL,
			InpatientInfoNow inpatientInfo, String startTime, String endTime,
			String drugName, String inpatientNoSerc, String type) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer(1200);
		sb.append(" select count(1) from ( ");
		if(tnL.size()>1){
			if(tnL.size()>1){
				sb.append("select drugId from ( ");
			}
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("select  rm").append(i).append(".Drug_Code as drugId ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append("  WHERE rm").append(i).append(".apply_state");
			
			if("0".equals(type)){
				sb.append(" <>2 ");
			}else if("1".equals(type)){
				sb.append(" =2 ");
			}
			sb.append(" AND rm").append(i).append(".OP_TYPE in(4,5) ");
			if(StringUtils.isNotBlank(inpatientInfo.getInpatientNo())){
				String [] inpatientNo=inpatientInfo.getInpatientNo().split(",");
				String inNo="";
				String inNo1="";
				for(int j=0;j<inpatientNo.length;j++){
					if(inNo.split(",").length<999){
						if(!"".equals(inpatientNo[j])){
							inNo = inNo + "','";
						}
						inNo = inNo + inpatientNo[j];
					}else{
						if(!"".equals(inpatientNo[j])){
							inNo1 = inNo1 + "','";
						}
						inNo1 = inNo1 + inpatientNo[j];
					}
				}
				if(inNo.split(",").length==0){
					sb.append(" AND rm").append(i).append(".patient_id in('"+inNo+"')");
				}else{
					sb.append(" AND (rm").append(i).append(".patient_id in('"+inNo+"') or rm").append(i).append(".patient_id in('"+inNo1+"'))");
				}
			}
			if(StringUtils.isNotBlank(drugName)){
				sb.append(" AND rm").append(i).append(".trade_name like '%"+drugName+"%'");
			}
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" AND (rm").append(i).append(".apply_date >=to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss')  and	rm").append(i).append(".apply_date <to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')) ");

			}
		}
		if(tnL.size()>1){
			sb.append(") ");
		}
		sb.append(")");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		List list =query.list();
		if(list!=null&&list.size()>0){
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}
	@Override
	public int queryDrugDispensDetailTotal(java.util.List<String> tnL,
			InpatientInfoNow inpatientInfo, String startTime, String endTime,
			String drugName, String inpatientNoSerc, String type) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ");
		if(tnL.size()>1){
			sb.append(" select bedName from ( ");
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			String table="";
			if("T_DRUG_APPLYOUT_NOW".equals(tnL.get(i))){
				table="T_INPATIENT_INFO_now";
			}else{
				table="T_INPATIENT_INFO";
			}
			sb.append("select  i.bed_name as bedName ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" join ").append(table)
			.append(" i on rm").append(i).append(".patient_id=i.inpatient_no ").append(" WHERE rm").append(i).append(".apply_state");
			if("0".equals(type)){
				sb.append(" <>2 ");
			}else if("1".equals(type)){
				sb.append(" =2 ");
			}
			sb.append(" AND rm").append(i).append(".OP_TYPE in(4,5) ");
			if(StringUtils.isNotBlank(inpatientInfo.getInpatientNo())){
				String [] inpatientNo=inpatientInfo.getInpatientNo().split(",");
				String inNo="";
				for(int j=0;j<inpatientNo.length;j++){
					if(!"".equals(inpatientNo[j])){
						inNo = inNo + "','";
					}
					inNo = inNo + inpatientNo[j];
				}

				sb.append(" AND rm").append(i).append(".patient_id in ('"+inNo+"') ");
			}
			
			
			if(StringUtils.isNotBlank(drugName)){
				sb.append(" AND rm").append(i).append(".trade_name like '%"+drugName+"%' ");
			}
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" AND (rm").append(i).append(".apply_date >= to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss')  and	rm").append(i).append(".apply_date <to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')) ");

			}
		}
		if(tnL.size()>1){
			sb.append(") ");
		}
		sb.append(") ");
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
		List list =query.list();
		if(list!=null&&list.size()>0){
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}
}
