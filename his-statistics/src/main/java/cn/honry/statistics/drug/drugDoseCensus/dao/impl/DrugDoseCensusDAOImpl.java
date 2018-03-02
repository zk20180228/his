 package cn.honry.statistics.drug.drugDoseCensus.dao.impl;

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
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.drugDoseCensus.dao.DrugDoseCensusDAO;
import cn.honry.statistics.drug.pharmacy.vo.PharmacyVo;
import cn.honry.utils.HisParameters;

/**
 * 住院发药量统计dao实现类
 * @author  lyy
 * @createDate： 2016年6月20日 下午3:47:06 
 * @modifier lyy
 * @modifyDate：2016年6月20日 下午3:47:06
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("drugDoseCensusDao")
@SuppressWarnings({ "all" })
public class DrugDoseCensusDAOImpl extends HibernateEntityDao<DrugOutstore> implements DrugDoseCensusDAO {
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
	public List<DrugOutstore> queryDrugDose(List<String> tnL, String startData,String endData,String drugstore,String page, String rows) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<DrugOutstore>();
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sql=null;
		if(rows.endsWith("P")){
			endData=endData+"|";
			sql=this.HqlPar(tnL,startData, endData, drugstore,"1");
			endData=endData.substring(0, endData.length()-1);
			rows=rows.substring(0,rows.length()-1);
		}else{
			sql=this.HqlPar(tnL,startData, endData, drugstore,"1");
			paraMap.put("page", page==null?"1":page);
			paraMap.put("row", rows==null?"20":rows);
		}
		

		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		
		
		List<DrugOutstore> voList =namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<DrugOutstore>(){
			@Override
			public DrugOutstore mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugOutstore vo = new DrugOutstore();
				vo.setDrugDeptCode(rs.getString("drugDeptCode"));
				vo.setApproveDate(rs.getDate("approveDate"));
				vo.setApproveOpercode(rs.getString("approveOpercode"));
				vo.setExamOpercode(rs.getString("examOpercode"));
				vo.setOpType(rs.getString("opType"));
				vo.setOutNum(rs.getDouble("outNum"));
				return vo;
		}});
		return voList ;
	}
	@Override
	public int getTatalDrugDose(List<String> tnL, String startData,String endData,String drugstore) throws Exception{
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		String sql=this.HqlPar(tnL,startData, endData, drugstore,"0").toString();
		sql = "select count(1)  from ( "+sql+" )";
		Map<String, Object> paraMap = new HashMap<String, Object>();

		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		return  namedParameterJdbcTemplate.queryForObject(sql, paraMap, java.lang.Integer.class);
	}
	private String HqlPar(List<String> tnL, String startData,String endData,String drugstore,String type) {
		
		final StringBuffer sb = new StringBuffer();
		if("1".equals(type)){
			sb.append("select * from ( ");
			if(tnL.size()>0){
				sb.append("select drugDeptCode,approveDate,approveOpercode,examOpercode,opType, outNum ,rownum as rn from( ");
			}
		}else{
			if(tnL.size()>0){
				sb.append("select drugDeptCode,approveDate,approveOpercode,examOpercode,opType, outNum from( ");
			}
		}
		
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION  ");
			}
			sb.append("SELECT rm").append(i).append(".drug_dept_code AS drugDeptCode,rm").append(i).append(".APPROVE_DATE AS approveDate,rm")
			.append(i).append(".druged_code AS approveOpercode,rm").append(i).append(".druged_name AS examOpercode,decode(rm").append(i).append(".op_type,'4','发药','5','退药') as opType ,rm")
			.append(i).append(".out_num as outNum ");
			sb.append(" FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append(" WHERE rm")
			.append(i).append(".DEL_FLG = 0 AND rm").append(i).append(".STOP_FLG = 0  AND rm").append(i).append(".op_type in ('4','5') and  rm")
			.append(i).append(".out_state = '2' ");
			
			if(StringUtils.isNotBlank(startData)){
				sb.append("AND rm").append(i).append(".APPROVE_DATE >= to_date(:startData,'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append("AND rm").append(i).append(".APPROVE_DATE <= to_date(:endData,'yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(drugstore)){
				sb.append("AND rm").append(i).append(".drug_dept_code = :drugstore " );
			}
			
		}
		if(tnL.size()>0){
			sb.append(") ");
		}
		if("1".equals(type)){
			if(endData.endsWith("|")){
				sb.append(" ) ");
			}else{
				sb.append("  where ROWNUM <=(:page) * :row )  where rn > (:page -1) * :row  ");
			}
			
		}
		return sb.toString();
	}
	@Override
	public List<SysDepartment> queryStoreDept(String name) throws Exception{
		String hql="from SysDepartment t where t.del_flg=0 and t.stop_flg=0 and t.deptType='P'";
		if(StringUtils.isNotBlank(name)){
			hql+=" and (t.deptName like :name or t.deptPinyin like :name or t.deptWb like :name or t.deptInputcode like :name or t.deptCode like :name)";
		}
		
		Query query=this.getSession().createQuery(hql);
		if(StringUtils.isNotBlank(name)){
			query.setParameter("name", "%"+name+"%");
		}
		List<SysDepartment> list=query.list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
	@Override
	public List<DrugOutstore> expQueryDrugDoseCensus(List<String> tnL, String startData,String endData,String drugstore) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<DrugOutstore>();
		}
		String sql=this.HqlPar(tnL,startData, endData, drugstore,"0");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		List<DrugOutstore> voList =namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<DrugOutstore>(){
			@Override
			public DrugOutstore mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugOutstore vo = new DrugOutstore();
				vo.setDrugDeptCode(rs.getString("drugDeptCode"));
				vo.setApproveDate(rs.getDate("approveDate"));
				vo.setApproveOpercode(rs.getString("approveOpercode"));
				vo.setExamOpercode(rs.getString("examOpercode"));
				vo.setOpType(rs.getString("opType"));
				vo.setOutNum(rs.getDouble("outNum"));
				return vo;
		}});
		return voList ;
	}
	
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author:zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMin() throws Exception{
		final String sql = "SELECT MAX(mn.APPROVE_DATE) AS eTime ,MIN(mn.APPROVE_DATE) AS sTime FROM T_DRUG_OUTSTORE_NOW mn";
	     List<StatVo> list =namedParameterJdbcTemplate.query(sql, new RowMapper<StatVo>() {
				@Override
				public StatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StatVo vo  = new StatVo();
					vo.setsTime(rs.getDate("sTime"));
					vo.seteTime(rs.getDate("eTime"));
					return vo;
				}
			});
		return list.get(0); 
	}
	@Override
	public Map<String, String> getStoreDEptMap() throws Exception{
		
		String hql="from SysDepartment t where t.del_flg=0 and t.stop_flg=0";
		List<SysDepartment> list=this.find(hql, null);
		Map<String,String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getDeptCode(), list.get(i).getDeptName());
		}
		return map;
	}
	
	/**  
	 * 住院病人动态报表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<DrugOutstore> queryDrugDoseCen(List<String> tnL,List<String> tnL1,String startData,String endData,String deptCode,String menuAlias,String page,String rows) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<DrugOutstore>();
		}
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * from (");
		sql.append("SELECT  ROWNUM AS n, bedName,name,tradeName,specs,doseOnce,doseUnit,(select t.frequency_name from t_business_frequency t where t.del_flg=0 and t.stop_flg=0 and rownum<2 and t.frequency_encode=dfqFreq ) dfqFreq,useName,num,(select t.code_name from t_business_dictionary t where  rownum<2 and t.del_flg=0 and t.stop_flg=0 and t.code_type='minunit' and t.code_encode=minUnit) minUnit,drugDeptCode,sendType, "
				+ "billclassCode,(select t.EMPLOYEE_NAME from t_employee t where  rownum<2 and t.del_flg=0 and t.stop_flg=0 and t.EMPLOYEE_CODE=applyOpercode) applyOpercode,applyDate,(select t.EMPLOYEE_NAME from t_employee t where  rownum<2 and t.del_flg=0 and t.stop_flg=0 and t.EMPLOYEE_CODE=printEmpl) printEmpl,printDate,validState,namePinyin,nameWb,notPrintDate  FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sql.append(" UNION ALL ");
			}
		sql.append(" select r.bed_name as bedName, r.patient_name as name, rm").append(i).append(".trade_name as tradeName, rm").append(i).append(".specs as specs, rm").append(i)
		.append(".dose_once as doseOnce,  rm").append(i).append(".dose_unit as doseUnit, rm").append(i).append(".dfq_freq as dfqFreq, rm").append(i).append(".use_name as useName,sum(rm").append(i).append(".apply_num) as num, rm").append(i)
		.append(".min_unit as minUnit, (select dept_name from t_department where dept_code = rm").append(i).append(".drug_dept_code) as drugDeptCode,decode(rm").append(i).append(".send_type,'1','集中','临时') as sendType, "
				+ "(select p.billclass_name from t_drug_billclass p where p.billclass_code = rm").append(i).append(".billclass_code) as billclassCode, "
				+ "rm").append(i).append(".apply_opercode as applyOpercode,to_char(rm").append(i).append(".apply_date,'yyyy-mm-dd hh24:mi:ss') as applyDate,rm").append(i).append(".print_empl as printEmpl, "
				+ "to_char(rm").append(i).append(".print_date,'yyyy-mm-dd hh24:mi:ss') as printDate,decode(rm").append(i).append(".valid_state,'1','有效','无效') as validState, "
				+ "s.DRUG_NAMEPINYIN as namePinyin,s.DRUG_NAMEWB as nameWb,decode(rm").append(i).append(".PRINT_DATE,'','未摆','已摆') AS notPrintDate  "
				+ "from "+tnL.get(i)+" rm").append(i).append(","+tnL1.get(i)+" r, t_drug_info s where rm").append(i).append(".patient_id = r.inpatient_no "
				+ "and rm").append(i).append(".drug_code = s.drug_code ");
				if(StringUtils.isNotBlank(deptCode)){
					deptCode=deptCode.replace(",", "','");
					sql.append(" and rm").append(i).append(".dept_code in('"+deptCode+"') ");
				}
				if(StringUtils.isNotBlank(startData)){
					sql.append(" and rm").append(i).append(".apply_date >= to_date('"+startData+"','yyyy-mm-dd hh24:mi:ss')");
				}
				if(StringUtils.isNotBlank(endData)){
					sql.append(" and rm").append(i).append(".apply_date <= to_date('"+endData+"','yyyy-mm-dd hh24:mi:ss ')");
				}
				sql.append("group by r.bed_name,r.patient_name,rm").append(i).append(".trade_name,rm").append(i).append(".specs,rm").append(i).append(".dose_once,rm").append(i).append(".dose_unit,rm").append(i).append(".dfq_freq,rm").append(i).append(".use_name, "
				+ "rm").append(i).append(".min_unit,rm").append(i).append(".drug_dept_code,rm").append(i).append(".send_type,rm").append(i).append(".billclass_code,rm").append(i).append(".print_empl,rm").append(i).append(".print_date, "
				+ "decode(rm").append(i).append(".valid_state,'1','有效','无效'),s.DRUG_NAMEPINYIN,s.DRUG_NAMEWB,rm").append(i).append(".apply_opercode,rm").append(i).append(".apply_date  "
				+ "order by r.bed_name,r.patient_name "); 
		sql.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("page", start);
		paraMap.put("rows", count);
		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		List<DrugOutstore> DrugOutstoreList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DrugOutstore>() {
			@Override
			public DrugOutstore mapRow(ResultSet rs, int rowNum)throws SQLException {
				DrugOutstore vo = new DrugOutstore();
				vo.setBedName(rs.getString("bedName"));
				vo.setName(rs.getString("name"));
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setDoseOnce(rs.getString("doseOnce"));
				vo.setDoseUnit(rs.getString("doseUnit"));
				vo.setDfqFreq(rs.getString("dfqFreq"));
				vo.setUseName(rs.getString("useName"));
				vo.setNum(rs.getDouble("num"));
				vo.setMinUnit(rs.getString("minUnit"));
				vo.setDrugDeptCode(rs.getString("drugDeptCode"));
				vo.setSendType(rs.getString("sendType"));
				vo.setBillclassCode(rs.getString("billclassCode"));
				vo.setApplyOpercode(rs.getString("applyOpercode"));
				vo.setApplyDate(rs.getString("applyDate"));
				vo.setPrintEmpl(rs.getString("printEmpl"));
				vo.setPrintDate(rs.getString("printDate"));
				vo.setValidState(rs.getString("validState"));
				vo.setNamePinyin(rs.getString("namePinyin"));
				vo.setNameWb(rs.getString("nameWb"));
				vo.setNotPrintDate(rs.getString("notPrintDate"));
				return vo;
			}
		});
		if(DrugOutstoreList.size()>0){
			return DrugOutstoreList;
		}
		return new ArrayList<DrugOutstore>();
		}
		return null;
	}
	
	
	/**  
	 * 住院统计工作量  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalDrugDoseCen(List<String> tnL,List<String> tnL1,String startData, String endData,String deptCode, String menuAlias) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1)  FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sql.append(" UNION ALL ");
			}
		sql.append(" select r.bed_name as bedName, r.patient_name as name, rm").append(i).append(".trade_name as tradeName, rm").append(i).append(".specs as specs, rm").append(i)
		.append(".dose_once as doseOnce,  rm").append(i).append(".dose_unit as doseUnit, rm").append(i).append(".dfq_freq as dfqFreq, rm").append(i).append(".use_name as useName,sum(rm").append(i).append(".apply_num) as num, rm").append(i)
		.append(".min_unit as minUnit, (select dept_name from t_department where dept_code = rm").append(i).append(".drug_dept_code) as drugDeptCode,decode(rm").append(i).append(".send_type,'1','集中','临时') as sendType, "
				+ "(select p.billclass_name from t_drug_billclass p where p.billclass_code = rm").append(i).append(".billclass_code) as billclassCode, "
				+ "rm").append(i).append(".apply_opercode as applyOpercode,to_char(rm").append(i).append(".apply_date,'yyyy-mm-dd hh24:mi:ss') as applyDate,rm").append(i).append(".print_empl as printEmpl, "
				+ "to_char(rm").append(i).append(".print_date,'yyyy-mm-dd hh24:mi:ss') as printDate,decode(rm").append(i).append(".valid_state,'1','有效','无效') as validState, "
				+ "s.DRUG_NAMEPINYIN as namePinyin,s.DRUG_NAMEWB as nameWb,decode(rm").append(i).append(".PRINT_DATE,'','未摆','已摆') AS notPrintDate  "
				+ "from "+tnL.get(i)+" rm").append(i).append(","+tnL1.get(i)+" r, t_drug_info s where rm").append(i).append(".patient_id = r.inpatient_no "
				+ "and rm").append(i).append(".drug_code = s.drug_code ");
				if(StringUtils.isNotBlank(deptCode)){
					deptCode=deptCode.replace(",", "','");
					sql.append(" and rm").append(i).append(".dept_code in('"+deptCode+"') ");
				}
				if(StringUtils.isNotBlank(startData)){
					sql.append(" and rm").append(i).append(".apply_date >= to_date('"+startData+"','yyyy-mm-dd hh24:mi:ss')");
				}
				if(StringUtils.isNotBlank(endData)){
					sql.append(" and rm").append(i).append(".apply_date <= to_date('"+endData+"','yyyy-mm-dd hh24:mi:ss ')");
				}
				sql.append("group by r.bed_name,r.patient_name,rm").append(i).append(".trade_name,rm").append(i).append(".specs,rm").append(i).append(".dose_once,rm").append(i).append(".dose_unit,rm").append(i).append(".dfq_freq,rm").append(i).append(".use_name, "
				+ "rm").append(i).append(".min_unit,rm").append(i).append(".drug_dept_code,rm").append(i).append(".send_type,rm").append(i).append(".billclass_code,rm").append(i).append(".print_empl,rm").append(i).append(".print_date, "
				+ "decode(rm").append(i).append(".valid_state,'1','有效','无效'),s.DRUG_NAMEPINYIN,s.DRUG_NAMEWB,rm").append(i).append(".apply_opercode,rm").append(i).append(".apply_date  "); 
		}
		sql.append(")");
	
		return jdbcTemplate.queryForObject(sql.toString(), java.lang.Integer.class);
}

	/**  
	 * 住院统计工作量  mongdb查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<DrugOutstore> queryDrugDoseCenForDB(String startData,String endData, String deptCode, String menuAlias, String page,String rows) {

		return null;
	}
}
