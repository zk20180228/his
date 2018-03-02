package cn.honry.statistics.drug.deptLeadDrug.dao.impl;

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

import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.deptLeadDrug.dao.DeptLeadDrugDao;
import cn.honry.statistics.drug.patientDispensing.vo.VinpatientApplyout;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.utils.HisParameters;


@Repository("deptLeadDrugDao")
@SuppressWarnings({ "all" })
public class DeptLeadDrugdaoImpl extends HibernateEntityDao<DrugApplyout> implements DeptLeadDrugDao {
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
	public List<DrugApplyoutNow> queryTableList(List<String> tnL,String stime, String etime,
			String drugDept, String drugxz, String drugName,String page,String rows) throws Exception {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<DrugApplyoutNow>();
		}
		final StringBuffer sb = new StringBuffer(1500);
		sb.append("SELECT * from (");
		sb.append("SELECT  ROWNUM AS n, deptCode,drugCode,drugCnameinputcode,drugNamewb,tradeName,specs,packUnit,applyNum,sumCost,drugPlaceoforigin FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT  ").append(" rm").append(i).append(".DEPT_CODE AS deptCode,rm")
			.append(i).append(".DRUG_CODE AS drugCode,i.DRUG_CNAMEINPUTCODE AS drugCnameinputcode,i.DRUG_BASICWB AS drugNamewb,rm").append(i).append(".TRADE_NAME AS tradeName,rm").append(i).append(".SPECS AS specs,rm").append(i)
			.append(".PACK_UNIT as packUnit, rm")
		    .append(i).append(".APPLY_NUM as applyNum,rm").append(i).append(".APPLY_NUM*rm").append(i).append(".RETAIL_PRICE AS sumCost, i.DRUG_PLACEOFORIGIN AS drugPlaceoforigin  ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append(", T_DRUG_INFO i ")
			.append("  WHERE rm").append(i).append(".DRUG_CODE = i.DRUG_CODE and rm").append(i).append(".APPLY_STATE IN ('2') AND rm").append(i).append(".OP_TYPE IN(4,5)");
			if(StringUtils.isNotBlank(stime)&&StringUtils.isNotBlank(etime)){
				sb.append(" AND rm").append(i).append(".APPLY_DATE >=  to_date(:stime ,'yyyy-MM-dd') ");
				sb.append(" and rm").append(i).append(".apply_date <=  to_date(:etime ,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(drugName)&&!"1".equals(drugName)){
				sb.append(" and rm").append(i).append(".DRUG_CODE =:drugName ");
			}
			if(StringUtils.isNotBlank(drugxz)&&!"1".equals(drugxz)){
				sb.append(" and rm").append(i).append(".DRUG_QUALITY  =:drugxz");
			}
			if(StringUtils.isNotBlank(drugDept)&&!"1".equals(drugDept)){
				sb.append(" and rm").append(i).append(".DEPT_CODE in "+drugDept+" " );
			}
		}
		sb.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(StringUtils.isNotBlank(stime)){
			paraMap.put("stime", stime);
		}
		if(StringUtils.isNotBlank(etime)){
			paraMap.put("etime", etime);
		}
		
		if(StringUtils.isNotBlank(drugName)&&!"1".equals(drugName)){
			paraMap.put("drugName", drugName);
		}
		if(StringUtils.isNotBlank(drugxz)&&!"1".equals(drugxz)){
			paraMap.put("drugxz", drugxz);
		}
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<DrugApplyoutNow> VinpatientApplyoutList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<DrugApplyoutNow>() {
			@Override
			public DrugApplyoutNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				DrugApplyoutNow vo = new DrugApplyoutNow();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDrugCnameinputcode(rs.getString("drugCnameinputcode"));
				vo.setDrugNamewb(rs.getString("drugNamewb"));
				vo.setTradeName(rs.getString("tradeName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setPackUnit(rs.getString("packUnit"));
				vo.setApplyNum(rs.getDouble("applyNum"));
				vo.setSumCost(rs.getDouble("sumCost"));
				vo.setDrugPlaceoforigin(rs.getString("drugPlaceoforigin"));
				return vo;
			}
			
		});

		if(VinpatientApplyoutList!=null&&VinpatientApplyoutList.size()>0){
			return VinpatientApplyoutList;
		}
		return new ArrayList<DrugApplyoutNow>();
	}
	
	@Override
	public int queryTableListToatl(List<String> tnL,String stime,String etime, String drugDept, String drugxz, String drugName) throws Exception{
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from( ");
		sb.append("SELECT deptCode FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION all ");
			}
			
			sb.append("SELECT  ").append(" rm").append(i).append(".dept_name as deptCode,rm")
			.append(i).append(".DRUG_CODE as drugCode ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append("  WHERE rm").append(i).append(".APPLY_STATE IN ('2') and rm").append(i).append(".OP_TYPE in(4,5)");
			
			if(StringUtils.isNotBlank(stime)&&StringUtils.isNotBlank(etime)){
				sb.append(" AND rm").append(i).append(".APPLY_DATE >=  to_date('"+stime+"' ,'yyyy-MM-dd') ");
				sb.append(" AND rm").append(i).append(".APPLY_DATE <=  to_date('"+etime+"' ,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(drugName)&&!"1".equals(drugName)){
				sb.append(" AND rm").append(i).append(".DRUG_CODE =:drugName ");
			}
			if(StringUtils.isNotBlank(drugxz)&&!"1".equals(drugxz)){
				sb.append(" AND rm").append(i).append(".DRUG_QUALITY  =:drugxz");
			}
			if(StringUtils.isNotBlank(drugDept)&&!"1".equals(drugDept)){
				sb.append(" AND rm").append(i).append(".DEPT_CODE in "+drugDept);
			}
		}
		sb.append("))");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(drugName)&&!"1".equals(drugName)){
			paraMap.put("drugName", drugName);
		}
		if(StringUtils.isNotBlank(drugxz)&&!"1".equals(drugxz)){
			paraMap.put("drugxz", drugxz);
		}
		String str = this.getSession().createSQLQuery(sb.toString()).setProperties(paraMap).uniqueResult().toString();
		int int1 = Integer.parseInt(str);
		return int1;
	}
	
	@Override
	public List<SysDepartment> querydrugDept() throws Exception {
		String hql=" from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> deptl=super.find(hql, null);
		if(deptl!=null&&deptl.size()>0){
			return deptl;
		}
		return new ArrayList<SysDepartment>();
	}
	@Override
	public List<DrugInfo> querydrugName() throws Exception {
		String hql=" from DrugInfo where del_flg=0 and stop_flg=0";
		List<DrugInfo> deptl=super.find(hql, null);
		if(deptl!=null&&deptl.size()>0){
			return deptl;
		}
		return new ArrayList<DrugInfo>();
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
	public StatVo findMaxMin() throws Exception{
		final String sql = "SELECT MAX(mn.druged_date) AS eTime ,MIN(mn.druged_date) AS sTime FROM T_DRUG_APPLYOUT_NOW mn";
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
