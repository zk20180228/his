package cn.honry.statistics.drug.pharmacy.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.pharmacy.dao.DrugPharmacyDAO;
import cn.honry.statistics.drug.pharmacy.vo.CopyOfPharmacyVoSecond;
import cn.honry.statistics.drug.pharmacy.vo.PharmacyVo;
import cn.honry.utils.HisParameters;
/**
 * 住院部取药统计DAO实现层
 * @author  lyy
 * @createDate： 2016年6月22日 上午10:21:35 
 * @modifier lyy
 * @modifyDate：2016年6月22日 上午10:21:35
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Repository(value="drugPharmacyDAO")
@SuppressWarnings({"all"})
public class DrugPharmacyDAOImpl  extends HibernateEntityDao<PharmacyVo> implements DrugPharmacyDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setsuperSessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public int getPageDrugPharmacyTotal(List<String> tnL, String startData,
			String endData, String drugCostType, String drugstore) {
		final StringBuffer sb = new StringBuffer(430);
			sb.append("SELECT COUNT(ID) AS spec  FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION all ");
			}
			sb.append("SELECT T.DRUG_ID AS ID ");
			sb.append(" FROM T_DRUG_INFO T  ");
			sb.append(" LEFT JOIN T_BUSINESS_DICTIONARY P ON P.CODE_ENCODE=T.DRUG_PACKAGINGUNIT  AND P.CODE_TYPE='packunit' ");
			sb.append(" LEFT JOIN ");	
			sb.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" OUT").append(i).append(" ON OUT").append(i).append(".DRUG_CODE=T.DRUG_CODE ");
			sb.append(" WHERE (OUT").append(i).append(".OP_TYPE='4' OR OUT").append(i).append(".OP_TYPE='5') ");
			if(StringUtils.isNotBlank(startData)){
				sb.append(" AND OUT").append(i).append(".CREATETIME >= TO_DATE(:startData,'yyyy-mm-dd hh24:mi:ss')");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append(" AND OUT").append(i).append(".CREATETIME <= TO_DATE(:endData,'yyyy-mm-dd hh24:mi:ss ')");
			}
			if(StringUtils.isNotBlank(drugCostType)){
				sb.append(" AND T.DRUG_TYPE=:drugCostType");
			}
			if(StringUtils.isNotBlank(drugstore)){
				sb.append(" AND OUT").append(i).append(".DRUG_DEPT_CODE=:drugstore");
			}
		} 
			sb.append(") ");
		Map<String, Object> paraMap = new HashMap<String, Object>(4);
		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugCostType)){
			paraMap.put("drugCostType", drugCostType);
		}
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		List<PharmacyVo> list =namedParameterJdbcTemplate.query(sb.toString(), paraMap,new RowMapper<PharmacyVo>(){
			@Override
			public PharmacyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PharmacyVo vo = new PharmacyVo();
				vo.setSpec(rs.getString("spec"));;
				return vo;
		}});
		if(list!=null&&list.size()>0){
			return Integer.parseInt(list.get(0).getSpec());
		}
		return 0;
	}
	@Override
	public List<PharmacyVo> getPageDrugPharmacy(List<String> tnL,String startData,String endData,String  drugCostType,String drugstore,String page,String rows) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<PharmacyVo>();
		}
		String s= null;
		StringBuffer sb=this.sqlDrugPharmacy(tnL,startData,endData,drugCostType,drugstore);
		s="select * from (select * from("+sb.toString()+" ) n where  n <= (:page) * :row ) where n > (:page -1) * :row  ";
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", start);
		paraMap.put("row", count);
		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugCostType)){
			paraMap.put("drugCostType", drugCostType);
		}
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		List<PharmacyVo> voList =namedParameterJdbcTemplate.query(s.toString(), paraMap,new RowMapper<PharmacyVo>(){
			@Override
			public PharmacyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PharmacyVo vo = new PharmacyVo();
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setOutState(rs.getString("outState"));
				vo.setOutType(rs.getString("outType"));;
				vo.setSpec(rs.getString("spec"));;
				vo.setOutNum(rs.getDouble("outNum"));
				vo.setDrugPackagingunit(rs.getString("drugPackagingunit"));;
				vo.setDrugRetailprice(rs.getDouble("drugRetailprice"));
				vo.setOutlCost(rs.getDouble("outlCost"));
				vo.setCreateTime(rs.getDate("createTime"));
				vo.setDrugType(rs.getString("drugType"));
				vo.setDrugDeptCode(rs.getString("drugDeptCode"));
				return vo;
		}});
		
		return voList;
	}
	/**
	 * 
	 * @param tnL
	 * @param startData
	 * @param endData
	 * @param drugCostType
	 * @param drugstore
	 * @return CopyOfPharmacyVoSecond
	 */
	@Override
	public List<CopyOfPharmacyVoSecond> getPageDrugPharmacyPDF(List<String> tnL,String startData,String endData,String  drugCostType,String drugstore) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<CopyOfPharmacyVoSecond>();
		}
		StringBuffer sql=this.sqlDrugPharmacy(tnL,startData,endData,drugCostType,drugstore);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugCostType)){
			paraMap.put("drugCostType", drugCostType);
		}
		if(StringUtils.isNotBlank(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		List<CopyOfPharmacyVoSecond> voList =namedParameterJdbcTemplate.query(sql.toString(), paraMap,new RowMapper<CopyOfPharmacyVoSecond>(){
			@Override
			public CopyOfPharmacyVoSecond mapRow(ResultSet rs, int rowNum) throws SQLException {
				CopyOfPharmacyVoSecond vo = new CopyOfPharmacyVoSecond();
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setOutState(rs.getString("outState"));
				vo.setOutType(rs.getString("outType"));;
				vo.setSpec(rs.getString("spec"));;
				vo.setOutNum(rs.getDouble("outNum"));
				vo.setDrugPackagingunit(rs.getString("drugPackagingunit"));;
				vo.setDrugRetailprice(rs.getDouble("drugRetailprice"));
				vo.setOutlCost(rs.getDouble("outlCost"));
				vo.setCreateTime(rs.getDate("createTime"));
				vo.setDrugType(rs.getString("drugType"));
				vo.setDrugDeptCode(rs.getString("drugDeptCode"));
				return vo;
		}});
		
		return voList;
	}
	
	private StringBuffer sqlDrugPharmacy(List<String> tnL,String startData,String endData,String drugCostType,String drugstore) throws Exception{
		final StringBuffer sb = new StringBuffer();
		if(tnL.size()>1){
			sb.append("SELECT drugCode,drugName,outState,outType,spec,outNum,drugPackagingunit,drugRetailprice,");
			sb.append("outlCost,createTime,drugType,drugDeptCode,n FROM( ");
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("select rownum as n,t.drug_code as drugCode,t.drug_commonname as drugName ,decode(out").append(i).append(".out_state,'0','申请','1','审批','2','核准') as outState ,");
			sb.append("decode(out").append(i).append(".op_type,'4','住院摆药','5','住院退药') as outType, ");
			sb.append("t.drug_spec as spec ,sum(out").append(i).append(".out_num) as outNum,p.code_name as drugPackagingunit,");
			sb.append("round(out").append(i).append(".retail_price,2) as drugRetailprice,round(out").append(i).append(".retail_price,2)*sum(out").append(i).append(".out_num) as outlCost,");
			sb.append("out").append(i).append(".createtime as createTime,t.drug_type as drugType,out").append(i).append(".drug_dept_code as drugDeptCode ");
			sb.append(" from t_drug_info t  ");
			sb.append(" left join t_business_dictionary p on p.code_encode=t.drug_packagingunit  and p.code_type='packunit' ");
			sb.append(" left join ");	
			sb.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" out").append(i).append(" on out").append(i).append(".drug_code=t.drug_code ");
			sb.append(" where (out").append(i).append(".op_type='4' or out").append(i).append(".op_type='5') ");
			if(StringUtils.isNotBlank(startData)){
				sb.append(" and out").append(i).append(".createtime >= to_date(:startData,'yyyy-mm-dd hh24:mi:ss')");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append(" and out").append(i).append(".createtime <= to_date(:endData,'yyyy-mm-dd hh24:mi:ss ')");
			}
			if(StringUtils.isNotBlank(drugCostType)){
				sb.append(" and t.drug_type=:drugCostType");
			}
			if(StringUtils.isNotBlank(drugstore)){
				sb.append(" and out").append(i).append(".drug_dept_code=:drugstore");
			}
			sb.append(" GROUP BY rownum,T.DRUG_code,T.DRUG_COMMONNAME,out").append(i).append(".OUT_STATE, ");
			sb.append("out").append(i).append(".op_type, T.DRUG_SPEC, t.DRUG_PACKAGINGUNIt,");
			sb.append("T.DRUG_PACKAGINGNUM,out").append(i).append(".RETAIL_PRICE,");
			sb.append("out").append(i).append(".CREATETIME,T.DRUG_TYPE,p.code_name,out").append(i).append(".DRUG_DEPT_CODE ");
		} 
		if(tnL.size()>1){
			sb.append(") ");
		}
		return sb;
	}
	@Override
	public StatVo findMaxMin() throws Exception{
		final String sql = "SELECT MAX(mn.CREATETIME) AS eTime ,MIN(mn.CREATETIME) AS sTime FROM T_DRUG_OUTSTORE_NOW mn";
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
	
	/**
	 * 查询所有的住院部取药列表
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<PharmacyVo> queryDrugPharmacyNew(List<String> tnL,String startData, String endData, String drugCostType,String drugstore, String page, String rows) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<PharmacyVo>();
		}
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT * from (");
		sb.append("SELECT ROWNUM AS n,drugCode,drugName,outState,outType,spec,outNum,drugPackagingunit,drugRetailprice,outlCost FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT t.DRUG_CODE AS drugCode,t.DRUG_NAME AS drugName, DECODE(rm").append(i).append(".OUT_STATE, '0', '申请','1', '审批', '2', '核准') AS outState, "
					+ "DECODE(rm").append(i).append(".out_type,'Z1','住院摆药','Z2','住院退药') AS outType, t.DRUG_SPEC AS spec, "
					+ "ROUND((SUM(rm").append(i).append(".OUT_NUM) / t.DRUG_PACKAGINGNUM), 2) AS outNum, t.DRUG_PACKAGINGUNIT AS drugPackagingunit, "
					+ "ROUND(rm").append(i).append(".retail_price,2) as drugRetailprice, round(rm").append(i).append(".retail_price, 2) *ROUND((SUM(rm").append(i).append(".OUT_NUM) / t.DRUG_PACKAGINGNUM), 2) AS outlCost  "
					+ "FROM t_drug_info t, "+tnL.get(i)+" rm").append(i).append("  "
					+ "WHERE rm").append(i).append(".DRUG_CODE = t.DRUG_CODE  ");
						if(StringUtils.isNotBlank(startData)&&StringUtils.isNotBlank(endData)){
							sb.append(" AND rm").append(i).append(".CREATETIME >=  to_date('"+startData+"' ,'yyyy-MM-dd hh24-mi-ss') ");
							sb.append(" AND rm").append(i).append(".CREATETIME <=  to_date('"+endData+"' ,'yyyy-MM-dd hh24-mi-ss') ");
						}
						if(StringUtils.isNotBlank(drugstore)&&!"1".equals(drugstore)){
							sb.append(" AND rm").append(i).append(".DRUG_DEPT_CODE =:drugstore ");
						}
			sb.append("AND (t.Drug_Type='A' or 'A'='A')  AND ((rm").append(i).append(".Out_Type = 'Z1') or (rm").append(i).append(".Out_Type = 'Z2')) "
					+ "GROUP BY t.DRUG_NAME, t.DRUG_SPEC, rm").append(i).append(".OUT_STATE, t.DRUG_CODE, "
					+ "DECODE(rm").append(i).append(".OUT_STATE,'0', '申请', '1', '审批', '2', '核准'), "
					+ "DECODE(rm").append(i).append(".out_type,'Z1','住院摆药','Z2','住院退药'), rm").append(i).append(".DRUG_TYPE, rm").append(i).append(".DRUG_QUALITY,rm").append(i).append(".retail_price, t.DRUG_PACKAGINGNUM, "
					+ "t.DRUG_PACKAGINGUNIT ");
		}
		sb.append(") where rownum <= :page * :rows order by drugName,spec,outState,drugCode )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(StringUtils.isNotBlank(startData)){
			paraMap.put("startData", startData);
		}
		if(StringUtils.isNotBlank(endData)){
			paraMap.put("endData", endData);
		}
		if(StringUtils.isNotBlank(drugstore)&&!"1".equals(drugstore)){
			paraMap.put("drugstore", drugstore);
		}
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<PharmacyVo> PharmacyVoList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<PharmacyVo>() {
			@Override
			public PharmacyVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				PharmacyVo vo = new PharmacyVo();
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setOutState(rs.getString("outState"));
				vo.setOutType(rs.getString("outType"));
				vo.setSpec(rs.getString("spec"));
				vo.setOutNum(rs.getDouble("outNum"));
				vo.setDrugPackagingunit(rs.getString("drugPackagingunit"));
				vo.setDrugRetailprice(rs.getDouble("drugRetailprice"));
				vo.setOutlCost(rs.getDouble("outlCost"));
				return vo;
			}
		});
		if(PharmacyVoList.size()>0){
			return PharmacyVoList;
		}
		return new ArrayList<PharmacyVo>();
	}
	@Override
	public int getTotalDrugPharmacyNew(List<String> tnL,String startData, String endData,String drugCostType, String drugstore) {
		StringBuffer sb=new StringBuffer();
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		sb.append("SELECT count(1) from (");
		sb.append("SELECT drugCode FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT t.DRUG_CODE AS drugCode,t.DRUG_NAME AS drugName, DECODE(rm").append(i).append(".OUT_STATE, '0', '申请','1', '审批', '2', '核准') AS outState, "
					+ "DECODE(rm").append(i).append(".out_type,'Z1','住院摆药','Z2','住院退药') AS outType, t.DRUG_SPEC AS spec, "
					+ "ROUND((SUM(rm").append(i).append(".OUT_NUM) / t.DRUG_PACKAGINGNUM), 2) AS outNum, t.DRUG_PACKAGINGUNIT AS drugPackagingunit, "
					+ "ROUND(rm").append(i).append(".retail_price,2) as drugRetailprice, round(rm").append(i).append(".retail_price, 2) *ROUND((SUM(rm").append(i).append(".OUT_NUM) / t.DRUG_PACKAGINGNUM), 2) AS outlCost  "
					+ "FROM t_drug_info t, "+tnL.get(i)+" rm").append(i).append("  "
					+ "WHERE rm").append(i).append(".DRUG_CODE = t.DRUG_CODE  ");
						if(StringUtils.isNotBlank(startData)&&StringUtils.isNotBlank(endData)){
							sb.append(" AND rm").append(i).append(".CREATETIME >=  to_date('"+startData+"' ,'yyyy-MM-dd hh24-mi-ss') ");
							sb.append(" AND rm").append(i).append(".CREATETIME <=  to_date('"+endData+"' ,'yyyy-MM-dd hh24-mi-ss') ");
						}
						if(StringUtils.isNotBlank(drugstore)&&!"1".equals(drugstore)){
							sb.append(" AND rm").append(i).append(".DRUG_DEPT_CODE =:drugstore ");
						}
			sb.append("AND (t.Drug_Type='A' or 'A'='A')  AND ((rm").append(i).append(".Out_Type = 'Z1') or (rm").append(i).append(".Out_Type = 'Z2')) "
					+ "GROUP BY t.DRUG_NAME, t.DRUG_SPEC, rm").append(i).append(".OUT_STATE, t.DRUG_CODE, "
					+ "DECODE(rm").append(i).append(".OUT_STATE,'0', '申请', '1', '审批', '2', '核准'), "
					+ "DECODE(rm").append(i).append(".out_type,'Z1','住院摆药','Z2','住院退药'), rm").append(i).append(".DRUG_TYPE, rm").append(i).append(".DRUG_QUALITY,rm").append(i).append(".retail_price, t.DRUG_PACKAGINGNUM, "
					+ "t.DRUG_PACKAGINGUNIT ");
			
		}
		sb.append("))");
			Map<String, Object> paraMap = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(startData)){
				paraMap.put("startData", startData);
			}
			if(StringUtils.isNotBlank(endData)){
				paraMap.put("endData", endData);
			}
			if(StringUtils.isNotBlank(drugstore)&&!"1".equals(drugstore)){
				paraMap.put("drugstore", drugstore);
			}
			String str = this.getSession().createSQLQuery(sb.toString()).setProperties(paraMap).uniqueResult().toString();
			int int1 = Integer.parseInt(str);
			return int1;
			}
}