package cn.honry.statistics.drug.dosageDetail.dao.impl;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.drug.dosageDetail.dao.DosDetailDao;
import cn.honry.statistics.drug.dosageDetail.vo.DetailVo;
import cn.honry.utils.DateUtils;

@Repository("dosDetailDao")
@SuppressWarnings({"all"})
public class DosDetailDaoImpl extends HibernateEntityDao<StoRecipe> implements DosDetailDao {
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<SysDepartment> deptForType(String type) {
		
		String hql = "from SysDepartment where deptType ='"+type+"' and stop_flg=0 and del_flg=0 ";
		List<SysDepartment> list = this.getSession().createQuery(hql).list();
		
		if(list !=null && list.size() > 0){
			return list;
		}
		
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<DetailVo> queryDetail0(List<String> table,String typeValue, String beginDate, String endDate,String param,String code,String typeView) {
		
		Map<String, String> pMap = new HashMap<String, String>();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("SELECT ");
		sBuffer.append(" code , name,sum(cfs) as cfs, sum(ypzl) as ypzl ,sum(cfje) as cfje,sum(js) as js FROM  (");//name没意义，为了不进行分组
		for (int i = 0; i < table.size(); i++) {
			if(i!=0){
				sBuffer.append(" UNION ALL ");
			}
		
		sBuffer.append("select");
				if("1".equals(param)){
					sBuffer.append(" r.DRUGED_TERMINAL_CODE code,r.DRUGED_TERMINAL_NAME name,");
				}else{
					sBuffer.append(" r.DRUGED_TERMINAL_CODE code,r.SEND_TERMINAL_NAME name,");
				}
			sBuffer.append(" COUNT(1) cfs,sum(r.RECIPE_QTY) ypzl,SUM(r.RECIPE_COST) cfje,SUM(r.SUM_DAYS) js FROM ");
			sBuffer.append(table.get(i)).append(" r ");
			sBuffer.append(" WHERE r.STOP_FLG = 0 AND r.DEL_FLG = 0 AND r.TRANS_TYPE = 1 AND r.VALID_STATE = 1 ");
				if("1".equals(param)){
					sBuffer.append(" AND r.RECIPE_STATE IN (2,3,4)");
				}else{
					sBuffer.append(" and r.recipe_state = 3");
				}
				if(StringUtils.isNotBlank(beginDate)){
					sBuffer.append(" AND r.FEE_DATE>=TO_DATE('"+beginDate+"', 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endDate)){
					sBuffer.append(" AND r.FEE_DATE<TO_DATE('"+endDate+"', 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(typeValue)){
					sBuffer.append(" AND r.DRUG_DEPT_CODE ='"+code+"' ");//发药药房code
				}
			if("1".equals(param)){
				sBuffer.append(" GROUP BY r.DRUGED_TERMINAL_CODE,r.DRUGED_TERMINAL_NAME");
			}else{
				sBuffer.append(" GROUP BY  r.DRUGED_TERMINAL_CODE,r.SEND_TERMINAL_NAME");
			}
			}
			sBuffer.append(" ) group by code,name");
			
			List<DetailVo> list=namedParameterJdbcTemplate.query(sBuffer.toString(),new HashMap(), new RowMapper<DetailVo>() {
				@Override
				public DetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DetailVo vo = new DetailVo();
					vo.setDrugedTerminal(rs.getString("name"));
					vo.setRecipeCount(rs.getDouble("cfs"));//处方数
					vo.setRecipeQty(rs.getDouble("ypzl"));//处方中药品数量
					vo.setRecipeCost(rs.getDouble("cfje"));//处方金额
					vo.setSumDays(rs.getDouble("js"));	//处方内药品剂数合计
					return vo;
				}
			});
		
			return list;
	}
	
	@Override
	public List<DetailVo> queryDetail1(List<String> table,String typeValue, String beginDate, String endDate,String param,String code) {
		
		Map<String, String> pMap = new HashMap<String, String>();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("SELECT ");
		sBuffer.append(" code , name,sum(cfs) as cfs, sum(ypzl) as ypzl ,sum(cfje) as cfje,sum(js) as js FROM  (");//name没意义，为了不进行分组
		for (int i = 0; i < table.size(); i++) {
			if(i!=0){
				sBuffer.append(" UNION ALL ");
			}
			sBuffer.append("select");
				if("1".equals(param)){
					sBuffer.append(" r.DRUGED_OPER code,r.DRUGED_OPER_NAME name,");
				}else{
					sBuffer.append(" r.SEND_OPER code,r.SEND_OPER_NAME name,");
				}
			sBuffer.append(" COUNT(1) cfs,sum(r.RECIPE_QTY) ypzl,SUM(r.RECIPE_COST) cfje,SUM(r.SUM_DAYS) js FROM  ");
			sBuffer.append(table.get(i)).append(" r ");
			sBuffer.append(" WHERE r.STOP_FLG = 0 AND r.DEL_FLG = 0 AND r.TRANS_TYPE = 1 AND r.VALID_STATE = 1 ");
				if("1".equals(param)){
					sBuffer.append(" AND r.RECIPE_STATE IN (2,3,4)");
				}else{
					sBuffer.append(" and r.recipe_state = 3");
				}
				if(StringUtils.isNotBlank(beginDate)){
					sBuffer.append(" AND r.FEE_DATE>=TO_DATE('"+beginDate+"', 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endDate)){
					sBuffer.append(" AND r.FEE_DATE<TO_DATE('"+endDate+"', 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(typeValue)){
					sBuffer.append(" AND r.DRUG_DEPT_CODE ='"+code+"' ");//发药药房code
				}
			if("1".equals(param)){
				sBuffer.append(" GROUP BY r.DRUGED_OPER,r.DRUGED_OPER_NAME");
			}else{
				sBuffer.append(" GROUP BY r.SEND_OPER,r.SEND_OPER_NAME");
			}
			}
			sBuffer.append(" ) group by code,name");
			List<DetailVo> list=namedParameterJdbcTemplate.query(sBuffer.toString(), new HashMap(), new RowMapper<DetailVo>() {
				@Override
				public DetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DetailVo vo = new DetailVo();
					vo.setJobNo(rs.getString("code"));
					vo.setDrugedOper(rs.getString("name"));
					vo.setRecipeCount(rs.getDouble("cfs"));
					vo.setRecipeQty(rs.getDouble("ypzl"));
					vo.setRecipeCost(rs.getDouble("cfje"));
					vo.setSumDays(rs.getDouble("js"));
					return vo;
				}
			});
		
			return list;
	}
	
	
	@Override
	public List<DetailVo> queryDetail2(List<String> table,String typeValue, String beginDate, String endDate,String param,String code) {
		
		Map<String, String> pMap = new HashMap<String, String>();
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("SELECT ");
		sBuffer.append(" code,name,sum(cfs) as cfs, sum(ypzl) as ypzl,sum(cfje) as cfje,sum(js) as js,dname FROM  (");
		for (int i = 0; i < table.size(); i++) {
			if(i!=0){
				sBuffer.append(" UNION ALL ");
			}
			sBuffer.append("select");
				if("1".equals(param)){
					sBuffer.append(" r.DRUGED_OPER code,r.DRUGED_OPER_NAME name,r.DRUGED_TERMINAL_NAME dname,");
				}else{
					sBuffer.append(" r.SEND_OPER code,r.SEND_OPER_NAME name,r.SEND_TERMINAL_NAME dname,");
				}
			sBuffer.append(" COUNT(1) cfs,sum(r.RECIPE_QTY) ypzl,SUM(r.RECIPE_COST) cfje,SUM(r.SUM_DAYS) js FROM  ");
			sBuffer.append(table.get(i)).append(" r ");
			sBuffer.append(" WHERE r.STOP_FLG = 0 AND r.DEL_FLG = 0 AND r.TRANS_TYPE = 1 AND r.VALID_STATE = 1 ");
				if("1".equals(param)){
					sBuffer.append(" AND r.RECIPE_STATE IN (2,3,4)");
				}else{
					sBuffer.append(" and r.recipe_state = 3");
				}
				if(StringUtils.isNotBlank(beginDate)){
					sBuffer.append(" AND r.FEE_DATE>=TO_DATE('"+beginDate+"', 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endDate)){
					sBuffer.append(" AND r.FEE_DATE<TO_DATE('"+endDate+"', 'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(typeValue)){
					if("1".equals(param)){
						sBuffer.append(" AND r.DRUGED_OPER ='"+code+"'");//配药时按人员显示
					}else{
						sBuffer.append(" AND r.SEND_OPER ='"+code+"'");
					}
				}
			if("1".equals(param)){
				sBuffer.append(" GROUP BY r.DRUGED_OPER,r.DRUGED_OPER_NAME,r.DRUGED_TERMINAL_NAME");
			}else{
				sBuffer.append(" GROUP BY r.SEND_OPER,r.SEND_OPER_NAME,SEND_TERMINAL_NAME");
			}
			}
			sBuffer.append(" ) group by code,name,dname");

			
			List<DetailVo> list=namedParameterJdbcTemplate.query(sBuffer.toString(), new HashMap(), new RowMapper<DetailVo>() {
				@Override
				public DetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DetailVo vo = new DetailVo();
					vo.setJobNo(rs.getString("code"));
					vo.setDrugedOper(rs.getString("name"));
					vo.setDrugedTerminal(rs.getString("dname"));
					vo.setRecipeCount(rs.getDouble("cfs"));
					vo.setRecipeQty(rs.getDouble("ypzl"));
					vo.setRecipeCost(rs.getDouble("cfje"));
					vo.setSumDays(rs.getDouble("js"));
					return vo;
				}
			});
		
			return list;
	}

	@Override
	public String queryEmployee(String empID) {
		
		String hql = "select t.employee_name from t_employee t where t.EMPLOYEE_JOBNO='"+empID+"'";
		String name = (String) this.getSession().createSQLQuery(hql).uniqueResult();
		
		return name;
	}
	
}
