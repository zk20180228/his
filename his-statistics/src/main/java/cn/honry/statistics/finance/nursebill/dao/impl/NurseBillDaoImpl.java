package cn.honry.statistics.finance.nursebill.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.statistics.finance.nursebill.dao.NurseBillDAO;
import cn.honry.statistics.finance.nursebill.vo.NurseBillHzVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillMxVo;
import cn.honry.utils.HisParameters;

@Repository("nurseBillDAO")
@SuppressWarnings({ "all" })
public class NurseBillDaoImpl extends HibernateEntityDao<NurseBillHzVo> implements NurseBillDAO{
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public String getHzHql(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception {
		String sql = "";
		sql="select t.TRADE_NAME as drugName, t.SPECS as specs, t.apply_num as applySum,"
				+ " t.min_unit as minUnit,"
				+ " t.dept_code as applyDept,"
				+ " t.DRUG_DEPT_CODE as drugDept,"
				+ " (select bill.billclass_name from T_DRUG_BILLCLASS bill where bill.billclass_id = t.billclass_code) as billClassName,"
				+ " DECODE(T.VALID_STATE, '1', '有效', '2', '无效', '3', '不摆药') AS validState,"
				+ " decode(t.PRINT_DATE, '', '未摆', '已摆') AS states from T_DRUG_APPLYOUT_NOW t, T_DRUG_INFO s where ";
				if(StringUtils.isNotBlank(beginTime)){
					sql += "  t.APPLY_DATE  >= to_date(:beginTime,'yyyy-MM-dd')" ;
				}
				if(StringUtils.isNotBlank(endTime)){
					sql += " and t.APPLY_DATE  <= to_date(:endTime,'yyyy-MM-dd')" ;
				}
				if(StringUtils.isNotBlank(applyState)){
					sql += " and t.APPLY_STATE = :applyState ";
				}
				if(StringUtils.isNotBlank(deptCode)){
					sql += " and t.DEPT_CODE = :deptCode ";
				}
				if(StringUtils.isNotBlank(billClassCode)){
					sql += " and t.BILLCLASS_CODE = :billClassCode ";
				}
			
				sql+= " "
				+ " union all"
				+ " select t.TRADE_NAME as drugName, t.SPECS as specs,  t.apply_num as applySum,"
				+ " t.min_unit as minUnit,"
				+ " t.dept_code as applyDept,"
				+ " t.DRUG_DEPT_CODE as drugDept,"
				+ " (select bill.billclass_name    from T_DRUG_BILLCLASS bill where bill.billclass_id = t.billclass_code) as billClassName,"
				+ " DECODE(T.VALID_STATE,  '1', '有效', '2', '无效','3','不摆药') AS validState, "
				+ " decode(t.PRINT_DATE, '', '未摆', '已摆') AS states  from T_DRUG_APPLYOUT t, T_DRUG_INFO s  where ";
				if(StringUtils.isNotBlank(beginTime)){
					sql += "  t.APPLY_DATE  >= to_date(:beginTime,'yyyy-MM-dd')" ;
				}
				if(StringUtils.isNotBlank(endTime)){
					sql += " and t.APPLY_DATE  <= to_date(:endTime,'yyyy-MM-dd')" ;
				}
				if(StringUtils.isNotBlank(applyState)){
					sql += " and t.APPLY_STATE = :applyState ";
				}
				if(StringUtils.isNotBlank(deptCode)){
					sql += " and t.DEPT_CODE = :deptCode ";
				}
				if(StringUtils.isNotBlank(billClassCode)){
					sql += " and t.BILLCLASS_CODE = :billClassCode ";
				}
				return sql.toString();
		}

	@Override
	public List<NurseBillHzVo> getNurseBillHz(List<String> tnL, String deptCode,String billClassCode,String stime, 
			String etime,String applyState,String page,String rows,String drugName) throws Exception  {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<NurseBillHzVo>();
		}
		Map<String, Object> pMap = new HashMap<>();
		if(!rows.endsWith("P")){//判断是全打印还是分页查询
			int start = Integer.parseInt(page == null ? "1" : page);
			int count = Integer.parseInt(rows == null ? "20" : rows);
				pMap.put("page", start);
				pMap.put("rows", count);
		}
		if(StringUtils.isNotBlank(stime)){
			pMap.put("stime", stime);
		}
		if(StringUtils.isNotBlank(etime)){
			pMap.put("etime", etime);
		}
		if(StringUtils.isNotBlank(applyState)){
			pMap.put("applyState", applyState);
		}
		if(StringUtils.isNotBlank(deptCode)){
			pMap.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(billClassCode)){
			pMap.put("billClassCode", billClassCode);
		}
		if(drugName != null && StringUtils.isNotBlank(drugName)){
			pMap.put("drugName", "%" + drugName + "%");
		}
		final StringBuffer sb = new StringBuffer(1000);
		sb.append("SELECT * from (SELECT rownum as n, drugName,specs,applySum,minUnit,applyDept,drugDept,billClassName,validState,states FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION all ");
			}
			sb.append("SELECT  rm").append(i).append(".TRADE_NAME AS drugName,rm").append(i).append(".SPECS AS specs,rm").append(i)
			.append(".apply_num AS applySum, rm")
			.append(i).append(".min_unit as minUnit,rm").append(i).append(".dept_code as applyDept,")
			.append("rm").append(i).append(".DRUG_DEPT_CODE as drugDept,")
			.append("rm").append(i).append(".billclass_code as billClassName,")
			.append("DECODE(rm").append(i).append(".VALID_STATE, '1', '有效', '2', '无效', '3', '不摆药') AS validState,")
			.append("DECODE(rm").append(i).append(".PRINT_DATE, '', '未摆', '已摆') AS states ");
			sb.append(" FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" where ");
			if(StringUtils.isNotBlank(stime)){
				sb.append(" rm").append(i).append(".APPLY_DATE  >= to_date(:stime,'yyyy-MM-dd')  ") ;
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append(" and rm").append(i).append(".APPLY_DATE  <= to_date(:etime,'yyyy-MM-dd')") ;
			}
			if(StringUtils.isNotBlank(applyState)){
				sb.append(" and rm").append(i).append(".APPLY_STATE = :applyState ");
			}
			if(StringUtils.isNotBlank(deptCode)){
				sb.append(" and rm").append(i).append(".DEPT_CODE = :deptCode ");
			}
			if(StringUtils.isNotBlank(billClassCode)){
				sb.append(" and rm").append(i).append(".BILLCLASS_CODE = :billClassCode ");
			}
			if(drugName != null && StringUtils.isNotBlank(drugName)){
				sb.append(" and rm").append(i).append(".TRADE_NAME  like :drugName ");
			}
		}
			if(rows.endsWith("P")){
				sb.append(") )");
			}else{
				sb.append(")  where rownum <= :page * :rows ) where n > (:page - 1) * :rows ");
			}
			
			
			List<NurseBillHzVo> operaArragVoList = namedParameterJdbcTemplate.query(sb.toString(), pMap, new RowMapper<NurseBillHzVo>(){

				@Override
				public NurseBillHzVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					NurseBillHzVo vo = new NurseBillHzVo(); 
					vo.setDrugName(rs.getString("drugName"));
					vo.setSpecs(rs.getString("specs"));
					vo.setApplySum(rs.getDouble("applySum"));
					vo.setMinUnit(rs.getString("minUnit"));
					vo.setApplyDept(rs.getString("applyDept"));
					vo.setDrugDept(rs.getString("drugDept"));
					vo.setBillClassName(rs.getString("billClassName"));
					vo.setValidState(rs.getString("validState"));
					vo.setStates(rs.getString("states"));
					return vo;
				}
				
			});
			if(operaArragVoList!=null&&operaArragVoList.size()>0){
				return operaArragVoList;
			}
		return new ArrayList<NurseBillHzVo>();
	}

	@Override
	public int getHzTotal(List<String> tnL,String deptCode,String billClassCode,String stime, String etime,String applyState,String drugName) throws Exception {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		Map<String, Object> pMap = new HashMap<>();
		if(StringUtils.isNotBlank(stime)){
			pMap.put("stime", stime);
		}
		if(StringUtils.isNotBlank(etime)){
			pMap.put("etime", etime);
		}
		if(StringUtils.isNotBlank(applyState)){
			pMap.put("applyState", applyState);
		}
		if(StringUtils.isNotBlank(deptCode)){
			pMap.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(billClassCode)){
			pMap.put("billClassCode", billClassCode);
		}
		if(drugName != null && StringUtils.isNotBlank(drugName)){
			pMap.put("drugName", "%" + drugName + "%");
		}
		final StringBuffer sb = new StringBuffer();
			sb.append("SELECT COUNT(1) FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION all ");
			}
		
		sb.append("SELECT  rm").append(i).append(".TRADE_NAME AS drugName ");
		sb.append(" FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" where ");
		if(StringUtils.isNotBlank(stime)){
			sb.append(" rm").append(i).append(".APPLY_DATE  >= to_date(:stime,'yyyy-MM-dd')") ;
		}
		if(StringUtils.isNotBlank(etime)){
			sb.append(" and rm").append(i).append(".APPLY_DATE  <= to_date(:etime,'yyyy-MM-dd')") ;
		}
		if(StringUtils.isNotBlank(applyState)){
			sb.append(" and rm").append(i).append(".APPLY_STATE = :applyState ");
		}
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and rm").append(i).append(".DEPT_CODE = :deptCode ");
		}
		if(StringUtils.isNotBlank(billClassCode)){
			sb.append(" and rm").append(i).append(".BILLCLASS_CODE = :billClassCode ");
		}
		if(drugName != null && StringUtils.isNotBlank(drugName)){
			sb.append(" and rm").append(i).append(".TRADE_NAME like :drugName ");
		}
	}
		sb.append(") ");
		int result =  namedParameterJdbcTemplate.queryForObject(sb.toString(), pMap, Integer.class);
		return result;
	}

	@Override
	public String getMxHql(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.bed_no as bedNo,r.patient_name as patientName,r.MEDICALRECORD_ID as medicalRecordID,s.DRUG_COMMONNAME as drugName,t.specs as specs, ");
		sb.append(" t.dose_once as doseOnce,(select t.code_name from t_business_dictionary  t where t.code_type= 'packagingunit' and t.code_encode=t.dose_unit) as doseUnit,(select frequency_name from t_business_frequency tbf where tbf.frequency_encode =  t.dfq_freq) as dfqFreq,");
		sb.append(" t.use_name as useName,sum(t.apply_num) as applyNum, (select t.code_name from t_business_dictionary  t where t.code_type= 'packagingunit' and t.code_encode= t.min_unit) as minUnit,(select dept_name from T_DEPARTMENT d where d.dept_code = t.dept_code) as applyDept,");
		sb.append(" (select dept_name from T_DEPARTMENT d where d.dept_code = t.DRUG_DEPT_CODE) as drugDept,(select bill.billclass_name from T_DRUG_BILLCLASS bill where bill.billclass_id = t.billclass_code) as billClassName,");
		sb.append(" DECODE(T.VALID_STATE, '1', '有效', '2','无效','3','不摆药') AS validState, s.DRUG_BASICPINYIN as drugBasicPinYin, s.DRUG_BASICWB as drugBasicWb, decode(t.PRINT_DATE, '', '未摆', '已摆') AS states,t.druged_bill as drugedBill,t.PRINT_DATE as printDate ");
		sb.append(" from t_drug_applyout t, t_drug_info s, (SELECT	i.*, i .BED_NAME as bed_no FROM	t_inpatient_info i ) r	");
		
		sb.append(" where t.drug_code = s.drug_id and t.patient_id = r.inpatient_no");
		if(StringUtils.isNotBlank(beginTime)){
			sb.append(" and to_char(t.APPLY_DATE,'yyyy-MM-dd') >= '"+beginTime+"' ");
		}
		if(StringUtils.isNotBlank(endTime)){
			sb.append(" and to_char(t.APPLY_DATE,'yyyy-MM-dd') <= '"+endTime+"' ");
		}
		if(StringUtils.isNotBlank(applyState)){
			sb.append(" and t.APPLY_STATE = '"+applyState+"' ");
		}
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and t.DEPT_CODE = '"+deptCode+"' ");
		}
		if(StringUtils.isNotBlank(billClassCode)){
			sb.append(" and t.BILLCLASS_CODE = '"+billClassCode+"' ");
		}
		if(drugName != null && StringUtils.isNotBlank(drugName)){
			drugName = drugName.toUpperCase();
			sb.append(" and (upper(s.DRUG_COMMONNAME) like '%"+drugName+"%'  ");
			sb.append(" or upper(s.DRUG_BASICPINYIN) like '%"+drugName+"%'  ");
			sb.append(" or upper(s.DRUG_BASICWB) like '%"+drugName+"%' ) ");
		}
		sb.append(" group by r.MEDICALRECORD_ID,r.bed_no,r.patient_name,s.DRUG_COMMONNAME, t.specs, t.dose_once,t.dose_unit,t.dfq_freq,t.use_name,");
		sb.append(" t.apply_num,t.min_unit,t.dept_code,t.DRUG_DEPT_CODE,t.billclass_code, t.valid_state,s.DRUG_BASICPINYIN,s.DRUG_BASICWB, t.PRINT_DATE, t.druged_bill,t.PRINT_DATE");
		return sb.toString();
	}

	@Override
	public List<NurseBillMxVo> getNurseBillMx(List<String> tnL,String deptCode, String billClassCode, String applyState, String page, String rows,String drugName,String beginTime, String endTime)  throws Exception {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<NurseBillMxVo>();
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=null;
		if(rows.endsWith("P")){
			endTime+="|";
			sb=this.getNewMxHql(tnL,deptCode, billClassCode, beginTime, endTime, applyState,drugName,"1");
			endTime=endTime.substring(0, endTime.length()-1);
		}else{
			sb=this.getNewMxHql(tnL,deptCode, billClassCode, beginTime, endTime, applyState,drugName,"1");
			int start = Integer.parseInt(page == null ? "1" : page);
			int count = Integer.parseInt(rows == null ? "20" : rows);
			paraMap.put("page", start);
			paraMap.put("rows", count);
		}
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", endTime);
		}
		if(StringUtils.isNotBlank(applyState)){
			paraMap.put("applyState", applyState);
		}
		if(StringUtils.isNotBlank(deptCode)){
			paraMap.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(billClassCode)){
			paraMap.put("billClassCode", billClassCode);
		}
		if(StringUtils.isNotBlank(drugName)){
			paraMap.put("drugName","%"+drugName+"%");
		}
		List<NurseBillMxVo> list =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<NurseBillMxVo>() {
			@Override
			public NurseBillMxVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				NurseBillMxVo vo = new NurseBillMxVo();
				vo.setBedNo(rs.getString("bedNo"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setMedicalRecordID(rs.getString("medicalRecordID"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setDoseOnce(rs.getDouble("doseOnce"));
				vo.setDoseUnit(rs.getString("doseUnit"));
				vo.setDfqFreq(rs.getString("dfqFreq"));
				vo.setUseName(rs.getString("useName"));
				vo.setApplyNum(rs.getDouble("applyNum"));
				vo.setMinUnit(rs.getString("minUnit"));
				vo.setApplyDept(rs.getString("applyDept"));
				vo.setDrugDept(rs.getString("drugDept"));
				vo.setBillClassName(rs.getString("billClassName"));
				vo.setValidState(rs.getString("validState"));
				vo.setStates(rs.getString("states"));
				vo.setDrugedBill(rs.getString("drugedBill"));
				vo.setPrintDate(rs.getDate("printDate"));
				return vo;
			}
			
		});
		return list;
	}
	@Override
	public int getMxTotal(List<String> tnL,String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName) throws Exception {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		String sb=this.getNewMxHql(tnL,deptCode, billClassCode, beginTime, endTime, applyState,drugName,"0");
		String sql="select count(1) from ("+sb.toString()+")";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", endTime);
		}
		if(StringUtils.isNotBlank(applyState)){
			paraMap.put("applyState", applyState);
		}
		if(StringUtils.isNotBlank(deptCode)){
			paraMap.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(billClassCode)){
			paraMap.put("billClassCode", billClassCode);
		}
		if(StringUtils.isNotBlank(drugName)){
			paraMap.put("drugName", "%"+drugName+"%");
		}
		int total =  namedParameterJdbcTemplate.queryForObject(sql,paraMap,java.lang.Integer.class); 
		return total;
	}

	/**
	 * @Description:根据条件查询所有医院领药单汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态  
 	 * @return List<NurseBillHzVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	@Override
	public List<NurseBillHzVo> getAllNurseBillHz(String deptCode,String billClassCode,String beginTime,
			String endTime,String applyState,String drugName) throws Exception {
		Map<String, Object> pMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(beginTime)){
			pMap.put("beginTime", beginTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			pMap.put("endTime", endTime);
		}
		if(StringUtils.isNotBlank(applyState)){
			pMap.put("applyState", applyState);
		}
		if(StringUtils.isNotBlank(deptCode)){
			pMap.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(billClassCode)){
			pMap.put("billClassCode", billClassCode);
		}
		if(drugName != null && StringUtils.isNotBlank(drugName)){
			pMap.put("drugName", "%" + drugName + "%");
		}
		String sb=this.getHzHql(deptCode, billClassCode, beginTime, endTime, applyState,drugName);
		List<NurseBillHzVo> list = namedParameterJdbcTemplate.query(sb.toString(), pMap, new RowMapper<NurseBillHzVo>(){

			@Override
			public NurseBillHzVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				NurseBillHzVo vo = new NurseBillHzVo();
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setApplySum(rs.getDouble("applySum"));
				vo.setMinUnit(rs.getString("minUnit"));
				vo.setApplyDept(rs.getString("applyDept"));
				vo.setDrugDept(rs.getString("drugDept"));
				vo.setBillClassName(rs.getString("billClassName"));
				vo.setValidState(rs.getString("validState"));
				vo.setStates(rs.getString("states"));
				return vo;
			}
			
		});
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<NurseBillHzVo>();
	}
	
	/**
	 * @Description:根据条件查询所有医院领药单明细
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:beganTime 开始时间； endTime 结束时间； deptCode 查询科室； billClassCode 摆药单id；applyState 申请状态  
 	 * @return List<NurseBillMxVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<NurseBillMxVo> getAllNurseBillMx(String deptCode,String billClassCode,String beginTime,
			String endTime,String applyState,String drugName)  throws Exception {
		String sb=this.getMxHql(deptCode, billClassCode, beginTime, endTime, applyState,drugName);
		SQLQuery queryObject = this.getSession().createSQLQuery(sb.toString())
				.addScalar("bedNo").addScalar("patientName").addScalar("medicalRecordID")
				.addScalar("drugName").addScalar("specs").addScalar("doseOnce",Hibernate.DOUBLE).addScalar("doseUnit")
				.addScalar("dfqFreq").addScalar("useName").addScalar("applyNum",Hibernate.DOUBLE)
				.addScalar("minUnit").addScalar("applyDept").addScalar("drugDept")
				.addScalar("billClassName").addScalar("validState").addScalar("drugBasicPinYin")
				.addScalar("drugBasicWb").addScalar("states").addScalar("drugedBill")
				.addScalar("printDate",Hibernate.DATE);
		List<NurseBillMxVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(NurseBillMxVo.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<NurseBillMxVo>();
	}

	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author: wsj
	 * @CreateDate: 2016年12月02日 
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMin()  throws Exception {
		Map<String, Object> pMap = new HashMap<String, Object>();
		final String sql = "SELECT MAX(mn.APPLY_DATE) AS eTime ,MIN(mn.APPLY_DATE) AS sTime FROM T_DRUG_APPLYOUT_NOW mn";
		StatVo vo =  namedParameterJdbcTemplate.queryForObject(sql, pMap,new BeanPropertyRowMapper<StatVo>(StatVo.class));
		return vo;
	}
	
	@Override
	public String getNewMxHql(List<String> tnL,String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName,String type)  throws Exception {
		final StringBuffer sb = new StringBuffer();
		if("1".equals(type)){
			sb.append("SELECT * from (");
			sb.append("select rownum as n,bedNo,patientName,medicalRecordID,drugName,specs,doseOnce,doseUnit,dfqFreq,useName,applyNum,minUnit,applyDept,drugDept,billClassName,");
			sb.append("validState,states,drugedBill,printDate  from(");
		}else{
			sb.append("select bedNo,patientName,medicalRecordID,drugName,specs,doseOnce,doseUnit,dfqFreq,useName,applyNum,minUnit,applyDept,drugDept,billClassName,");
			sb.append("validState,states,drugedBill,printDate  from(");
		}
		
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append(" UNION ALL ");
			}
			String table="";
			if("T_DRUG_APPLYOUT_NOW".equals(tnL.get(i))){
				table="t_inpatient_info_now";
			}else{
				table="t_inpatient_info";
			}
			
			sb.append(" select i.bed_name as bedNo, i.patient_name as patientName,  i.MEDICALRECORD_ID as medicalRecordID, t.trade_name as drugName,t.specs as specs,");
			sb.append("t.dose_once as doseOnce,t.dose_unit as doseUnit,t.DFQ_CEXP as dfqFreq,t.use_name as useName,t.apply_num as applyNum,t.min_unit as minUnit, ");
			sb.append(" t.DEPT_NAME as applyDept, t.DRUG_DEPT_NAME as drugDept,t.billclass_code as billClassName,");
			sb.append(" DECODE(T.VALID_STATE, '1', '有效', '2', '无效', '3', '不摆药') AS validState,decode(t.PRINT_DATE, '', '未摆', '已摆') AS states,");
			sb.append(" t.druged_bill as drugedBill, t.PRINT_DATE as printDate  from  ").append(tnL.get(i)).append(" t ,").append(table).append(" i ");
			sb.append("   where t.patient_id = i.inpatient_no ");
			if(StringUtils.isNotBlank(beginTime)){
				sb.append(" and t.APPLY_DATE >=to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(endTime)){
				sb.append(" and t.APPLY_DATE<=to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(applyState)){
				sb.append(" and t.APPLY_STATE = :applyState ");
			}
			if(StringUtils.isNotBlank(deptCode)){
				sb.append(" and t.DEPT_CODE = :deptCode");
			}
			if(StringUtils.isNotBlank(billClassCode)){
				sb.append(" and t.BILLCLASS_CODE = :billClassCode ");
			}
			if(StringUtils.isNotBlank(drugName)){
				sb.append(" and t.TRADE_NAME like :drugName ");
			}
		}
		sb.append(" )");
		if("1".equals(type)){
			if(endTime.endsWith("|")){
				sb.append(" ) ");
			}else{
				sb.append("  where rownum <= :page * :rows )  where n > (:page - 1) * :rows");
			}
		}
		
		return sb.toString();
	}

	@Override
	public List queryPackUnitMap()  throws Exception {
		String sql="select t.CODE_ENCODE,t.CODE_NAME from t_business_dictionary t where t.CODE_TYPE='drugPackagingunit'";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		List list = query.list();
		return list;
	}
	
}
