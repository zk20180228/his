package cn.honry.statistics.finance.detailedDaily.dao.Impl;

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

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.detailedDaily.dao.DetailedDailyDAO;
import cn.honry.statistics.finance.detailedDaily.vo.VdetailedDaily;
import cn.honry.statistics.sys.invoiceChecks.vo.VinpatirntInfoBalance;
import cn.honry.utils.ShiroSessionUtils;

@Repository("detailedDailyDAO")
@SuppressWarnings({ "all" })
public class DetailedDailyDAOImpl extends HibernateEntityDao<VdetailedDaily> implements DetailedDailyDAO{
	
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
		
	@Override
	public List<VdetailedDaily> queryVdetailedDaily(List<String> tnl, String beginDate,
			String endDate, String page, String rows) throws Exception{
		if(tnl==null||tnl.size()<0){
			return new ArrayList<VdetailedDaily>();
		}
		StringBuffer sql=null;
		if(null==page){
			 sql=querySqlAll(tnl,beginDate,endDate,page,rows);
		}else{
			sql=querySql(tnl,beginDate,endDate,page,rows);
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<VdetailedDaily> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<VdetailedDaily>() {
			@Override
			public VdetailedDaily mapRow(ResultSet rs, int rowNum) throws SQLException {
				VdetailedDaily vo = new VdetailedDaily();
				vo.setTotCost(rs.getDouble("totCost"));
				vo.setStatName(rs.getString("statName"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setName(rs.getString("name"));
				vo.setBalanceOpername(rs.getString("balanceOpername"));
				vo.setOperName(rs.getString("operName"));
				return vo;
		}});
		return voList;
	}

	@Override
	public int queryVdetailedDailyTotal(List<String> tnL,String beginDate, String endDate) throws Exception {
//		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		final StringBuffer sb = new StringBuffer();
			
			sb.append("SELECT COUNT(1) FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("	select b").append(i).append(".dept_name as deptName,b").append(i).append(".inpatient_no as inpatientNo,b")
			.append(i).append(".name as name,b").append(i).append(".stat_name as statName,b")
			.append(i).append(".tot_cost as totCost,b").append(i).append(".BALANCE_OPERNAME as operName,b").append(i).append(".balance_opername as balanceOpername");
			sb.append("  from ").append(tnL.get(i)).append(" b").append(i).append(" where b").append(i).append(".trans_type=2 ");   
			if(StringUtils.isNotBlank(beginDate)){
				sb.append("AND b").append(i).append(".BALANCE_DATE >= to_date('"+beginDate+"','yyyy-MM-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(endDate)){
				sb.append("AND b").append(i).append(".BALANCE_DATE <= to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss') ");
			}
		}
		sb.append("ORDER BY name ");
			sb.append(" )");
		return jdbcTemplate.queryForObject(sb.toString(), java.lang.Integer.class);
	}
	/**
	 * 
	 * @param tnL
	 * @param beginDate
	 * @param endDate
	 * @param page
	 * @param rows
	 * @return
	 */
	private StringBuffer querySql(List<String> tnL,String beginDate,String endDate, String page, String rows)  throws Exception{
//		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		final StringBuffer sb = new StringBuffer();
		
			sb.append("SELECT * FROM( ");
			sb.append("SELECT deptName,inpatientNo,name,statName,totCost,balanceOpername,operName,rownum as rn FROM( ");
		
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("	select b").append(i).append(".dept_name as deptName,b").append(i).append(".inpatient_no as inpatientNo,b")
			.append(i).append(".name as name,b").append(i).append(".stat_name as statName,b")
			.append(i).append(".tot_cost as totCost,b").append(i).append(".BALANCE_OPERNAME as operName,b").append(i).append(".balance_opername as balanceOpername");
			sb.append("  from ").append(tnL.get(i)).append(" b").append(i).append(" where b").append(i).append(".trans_type=2 ");   
			if(StringUtils.isNotBlank(beginDate)){
				sb.append("AND b").append(i).append(".BALANCE_DATE >= to_date('"+beginDate+"','yyyy-MM-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNotBlank(endDate)){
				sb.append("AND b").append(i).append(".BALANCE_DATE <= to_date('"+endDate+"','yyyy-MM-dd hh24:mi:ss') ");
			}
		}
		sb.append("ORDER BY name ");
		
			sb.append(") )tt where  rn > ('"+page+"' -1) * '"+rows+"' and rn <=('"+page+"') * '"+rows+"' ");
		
		return sb;
	}
	/**
	 * @author conglin
	 * @See 结账处冲单明细日报 报表数据查询
	 * @param tnL
	 * @param beginDate
	 * @param endDate
	 * @return
	 */private StringBuffer querySqlAll(List<String> tnL,String beginDate,String endDate, String page, String rows){
//			String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			final StringBuffer sb = new StringBuffer();
			
				sb.append("SELECT * FROM( ");
				sb.append("SELECT deptName,inpatientNo,name,statName,totCost,balanceOpername,operName,rownum as rn FROM( ");
			
			for(int i=0;i<tnL.size();i++){
				if(i!=0){
					sb.append("UNION ");
				}
				sb.append("	select b").append(i).append(".dept_name as deptName,b").append(i).append(".inpatient_no as inpatientNo,b")
				.append(i).append(".name as name,b").append(i).append(".stat_name as statName,b")
				.append(i).append(".tot_cost as totCost,b").append(i).append(".BALANCE_OPERNAME as operName,b").append(i).append(".balance_opername as balanceOpername");
				sb.append("  from ").append(tnL.get(i)).append(" b").append(i).append(" where b").append(i).append(".trans_type=2 ");   
				if(StringUtils.isNotBlank(beginDate)){
					sb.append("AND trunc(b").append(i).append(".BALANCE_DATE,'dd') >= to_date('"+beginDate+"','yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endDate)){
					sb.append("AND trunc(b").append(i).append(".BALANCE_DATE,'dd') <= to_date('"+endDate+"','yyyy-MM-dd') ");
				}
			}
			sb.append("ORDER BY name ");
			
				sb.append(") )tt ");
			
			return sb;
		}
	
	@Override
	public StatVo findMaxMin()  throws Exception{
		final String sql = "SELECT MAX(mn.BALANCE_DATE) AS eTime ,MIN(mn.BALANCE_DATE) AS sTime FROM T_INPATIENT_BALANCELIST_NOW mn";
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
