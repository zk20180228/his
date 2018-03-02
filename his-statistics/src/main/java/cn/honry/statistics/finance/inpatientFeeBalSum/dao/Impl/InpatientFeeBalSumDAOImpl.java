package cn.honry.statistics.finance.inpatientFeeBalSum.dao.Impl;

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

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.inpatientFeeBalSum.dao.InpatientFeeBalSumDAO;
import cn.honry.statistics.finance.inpatientFeeBalSum.vo.FeeBalSumVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Repository("inpatientFeeBalSumDAO")
@SuppressWarnings({ "all" })
public class InpatientFeeBalSumDAOImpl extends HibernateEntityDao<InpatientInfo> implements InpatientFeeBalSumDAO{
	private static final int List = 0;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public String joint(String startTime, String endTime, String typeSerc) throws Exception {
		SysDepartment dept= ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String sql="SELECT FEE.FEE_STAT_CODE feeStatCode,SUM(FF.TOT_COST) cost,FF.PACT_CODE pactCode,MAX(FF.PAYKIND_CODE) paykindCode ";		
			sql+="FROM T_INPATIENT_FEEINFO_NOW FF, T_CHARGE_MINFEETOSTAT FEE WHERE 1=1 ";
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sql+="and FF.FEE_DATE >= TO_DATE('"+startTime+"', 'yyyy-MM-dd hh24:mi:ss') AND FF.FEE_DATE < TO_DATE('"+endTime+"', 'yyyy-MM-dd hh24:mi:ss') ";
			}
			if("01".equals(typeSerc)){
				sql+="AND (FF.inhos_deptcode = '"+dept.getDeptCode()+"' OR 'ALL' = '"+dept.getDeptCode()+"') ";	
			}
			if("02".equals(typeSerc)){
				sql+="AND (FF.EXECUTE_DEPTCODE = '"+dept.getDeptCode()+"' OR 'ALL' = '"+dept.getDeptCode()+"') ";	
			}
			if("03".equals(typeSerc)){
				sql+="AND (FF.RECIPE_DEPTCODE = '"+dept.getDeptCode()+"' OR 'ALL' = '"+dept.getDeptCode()+"') ";	
			}
			sql+="AND FEE.REPORT_CODE = 'ZY01' AND FF.FEE_CODE = FEE.MINFEE_CODE GROUP BY FEE.FEE_STAT_CODE, FF.PACT_CODE";
			return sql;
	}

	@Override
	public List<FeeBalSumVo> getPage(List<String> tnL,String page, String rows, String typeSerc,String etime,String stime,String deptCode) throws Exception {
//		SysDepartment dept= ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(tnL==null||tnL.size()<0){
			return new ArrayList<FeeBalSumVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM( ");
		sb.append("SELECT feeStatCode,cost,pactCode,paykindCode,rownum as rn FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append("SELECT DISTINCT FEE.FEE_STAT_CODE AS feeStatCode,SUM(rm").append(i).append(".TOT_COST) AS cost,rm")
			.append(i).append(".PACT_CODE AS pactCode,MAX(rm").append(i).append(".PAYKIND_CODE) AS paykindCode ");
			sb.append(" FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append( ",T_CHARGE_MINFEETOSTAT FEE").append(" WHERE 1=1");
			if(StringUtils.isNotBlank(stime)&&StringUtils.isNotBlank(etime)){
				sb.append(" and rm").append(i).append(".FEE_DATE  >=  to_date(:stime ,'yyyy-MM-dd hh24:mi:ss') ");
				sb.append(" and rm").append(i).append(".FEE_DATE  <   to_date(:etime ,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if("01".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".inhos_deptcode = '"+deptCode+"' OR 'ALL' = '"+deptCode+"') ");	
			}
			if("02".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".EXECUTE_DEPTCODE = '"+deptCode+"' OR 'ALL' = '"+deptCode+"') ");	
			}
			if("03".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".RECIPE_DEPTCODE = '"+deptCode+"' OR 'ALL' = '"+deptCode+"') ");	
			}
			sb.append(" and FEE").append(".REPORT_CODE = 'ZY01' AND rm").append(i).append(".FEE_CODE = FEE.MINFEE_CODE GROUP BY FEE.FEE_STAT_CODE, rm").append(i).append(".PACT_CODE");
		}
		sb.append(") )tt where  rn > ('"+page+"' -1) * '"+rows+"' and rn <=('"+page+"') * '"+rows+"'");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(stime)){
			 paraMap.put("stime", stime);
		 }
		if(StringUtils.isNotBlank(etime)){
			paraMap.put("etime", etime);
		}
		List<FeeBalSumVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<FeeBalSumVo>() {
			@Override
			public FeeBalSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeeBalSumVo vo = new FeeBalSumVo();
				vo.setFeeStatCode(rs.getString("feeStatCode"));
				vo.setCost(rs.getDouble("cost"));
				vo.setPactCode(rs.getString("pactCode"));
				vo.setPaykindCode(rs.getString("paykindCode"));
				return vo;
		}});
		return voList;
	}

	@Override
	public int getTotal(List<String> tnL,String stime, String etime, String typeSerc,String deptCode) throws Exception  {
//		SysDepartment dept= ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(1) FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append("SELECT DISTINCT FEE.FEE_STAT_CODE AS feeStatCode,SUM(rm").append(i).append(".TOT_COST) AS cost,rm")
			.append(i).append(".PACT_CODE AS pactCode,MAX(rm").append(i).append(".PAYKIND_CODE) AS paykindCode ");
			sb.append(" FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append( ",T_CHARGE_MINFEETOSTAT FEE").append(" WHERE 1=1");
			
			if(StringUtils.isNotBlank(stime)&&StringUtils.isNotBlank(etime)){
				sb.append(" and rm").append(i).append(".FEE_DATE  >=  to_date('"+stime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
				sb.append(" and rm").append(i).append(".FEE_DATE  <   to_date('"+etime+"' ,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if("01".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".inhos_deptcode = '"+deptCode+"' OR 'ALL' = '"+deptCode+"') ");	
			}
			if("02".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".EXECUTE_DEPTCODE = '"+deptCode+"' OR 'ALL' = '"+deptCode+"') ");	
			}
			if("03".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".RECIPE_DEPTCODE = '"+deptCode+"' OR 'ALL' = '"+deptCode+"') ");	
			}
			sb.append(" and FEE").append(".REPORT_CODE = 'ZY01' AND rm").append(i).append(".FEE_CODE = FEE.MINFEE_CODE GROUP BY FEE.FEE_STAT_CODE, rm").append(i).append(".PACT_CODE");
		}
		sb.append(") ");
		return  jdbcTemplate.queryForObject(sb.toString(), java.lang.Integer.class);
	}

	@Override
	public List<MinfeeStatCode> getFeeStatName(String feeStatCode)  throws Exception {
		String hql="from MinfeeStatCode t where t.stop_flg = 0 and t.del_flg = 0 and t.feeStatCode = '"+feeStatCode+"'";
		List<MinfeeStatCode> list = this.getSession().createQuery(hql).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<MinfeeStatCode>();
	}

	@Override
	public List<FeeBalSumVo> getFeeBalSum(List<String> tnL,String stime,String etime, String typeSerc) throws Exception  {
		SysDepartment dept= ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(tnL==null||tnL.size()<0){
			return new ArrayList<FeeBalSumVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT feeStatCode,cost,pactCode,paykindCode,rownum as rn FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append("SELECT DISTINCT FEE.FEE_STAT_CODE AS feeStatCode,SUM(rm").append(i).append(".TOT_COST) AS cost,rm")
			.append(i).append(".PACT_CODE AS pactCode,MAX(rm").append(i).append(".PAYKIND_CODE) AS paykindCode ");
			sb.append(" FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i).append( ",T_CHARGE_MINFEETOSTAT FEE").append(" WHERE 1=1");
			if(StringUtils.isNotBlank(stime)&&StringUtils.isNotBlank(etime)){
				sb.append(" and rm").append(i).append(".FEE_DATE  >=  to_date(:stime ,'yyyy-MM-dd hh24:mi:ss') ");
				sb.append(" and rm").append(i).append(".FEE_DATE  <   to_date(:etime ,'yyyy-MM-dd hh24:mi:ss') ");
			}
			if("01".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".inhos_deptcode = '"+dept.getDeptCode()+"' OR 'ALL' = '"+dept.getDeptCode()+"') ");	
			}
			if("02".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".EXECUTE_DEPTCODE = '"+dept.getDeptCode()+"' OR 'ALL' = '"+dept.getDeptCode()+"') ");	
			}
			if("03".equals(typeSerc)){
				sb.append(" AND (rm").append(i).append(".RECIPE_DEPTCODE = '"+dept.getDeptCode()+"' OR 'ALL' = '"+dept.getDeptCode()+"') ");	
			}
			sb.append(" and FEE").append(".REPORT_CODE = 'ZY01' AND rm").append(i).append(".FEE_CODE = FEE.MINFEE_CODE GROUP BY FEE.FEE_STAT_CODE, rm").append(i).append(".PACT_CODE");
		}
		sb.append(")");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(stime)){
			 paraMap.put("stime", stime);
		 }
		if(StringUtils.isNotBlank(etime)){
			paraMap.put("etime", etime);
		}
		List<FeeBalSumVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<FeeBalSumVo>() {
			@Override
			public FeeBalSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeeBalSumVo vo = new FeeBalSumVo();
				vo.setFeeStatCode(rs.getString("feeStatCode"));
				vo.setCost(rs.getDouble("cost"));
				vo.setPactCode(rs.getString("pactCode"));
				vo.setPaykindCode(rs.getString("paykindCode"));
				return vo;
		}});
		return voList;
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
		final String sql = "SELECT MAX(mn.FEE_DATE) AS eTime ,MIN(mn.FEE_DATE) AS sTime FROM T_INPATIENT_FEEINFO_NOW mn";
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
