package cn.honry.statistics.finance.inoutstore.dao.impl;

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
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.statistics.finance.inoutstore.dao.InOutStoreDao;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.statistics.finance.inoutstore.vo.StoreSearchVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

/**  
 *  住院药房入出库台账查询DAO实现类
 * @Author:luyanshou
 * @version 1.0
 */
@Repository("inOutStoreDao")
@SuppressWarnings({"all"})
public class InOutStoreDaoImpl extends HibernateDaoSupport implements InOutStoreDao {

	
	@Resource(name = "sessionFactory")
	// 为HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 查询药品名称和编码
	 */
	public List<StoreResultVO> getdrugInfo(String q) throws Exception{
		StringBuffer buffer = new StringBuffer("select distinct t.drug_code as drugCode,t.trade_name as tradeName from ");
		buffer.append(HisParameters.HISPARSCHEMAHISUSER).append("T_DRUG_INSTORE t ");
		if(StringUtils.isNotBlank(q)){
			buffer.append("where t.trade_name like '%"+q+"%'");
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(buffer.toString()).addScalar("drugCode").addScalar("tradeName");
		List<StoreResultVO> list = queryObject.setResultTransformer(Transformers.aliasToBean(StoreResultVO.class)).list();
		return list ;
	}
	
	/**
	 * 查询入出库记录数据
	 */
	public List<StoreResultVO> getStoreData(List<String> tnL,StoreSearchVo vo,int firstResult,int page,int rows) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<StoreResultVO>();
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", page);
		paraMap.put("row", rows);
	    if(vo!=null){
	    	if(vo.getBeginTime()!=null){
				String begin =vo.getBeginTime();
				paraMap.put("begin", begin);

			}
			if(vo.getEndTime()!=null){
				String end = vo.getEndTime();
				paraMap.put("end", end);

			}
			if(StringUtils.isNotBlank(vo.getCode())){
				paraMap.put("code", vo.getCode());
			}	
		}
	    
	    if(vo!=null && vo.getType()!=1){
	    	List<StoreResultVO> list =  namedParameterJdbcTemplate.query(getSql(tnL, vo),paraMap,new RowMapper<StoreResultVO>() {

				@Override
				public StoreResultVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					StoreResultVO vo = new StoreResultVO();
					vo.setDrugCode(rs.getString("drug_code"));
					vo.setTradeName(rs.getString("trade_name"));
					vo.setSpecs(rs.getString("specs"));
					vo.setPackQty(rs.getBigDecimal("PACK_QTY"));
					vo.setDrugDeptCode(rs.getString("DRUG_DEPT_CODE"));
					vo.setApprove(rs.getString("APPROVE_OPERCODE"));
					vo.setInoutDate(rs.getDate("APPROVE_DATE"));
					vo.setDrugDeptName(rs.getString("DRUG_DEPT_NAME"));
					vo.setUserName(rs.getString("APPROVE_OPERNAME"));
					vo.setInoutNum(rs.getBigDecimal("inoutNum"));
					return vo;
				}
			});
		    return list ;
	    }else{
	     	List<StoreResultVO> list =  namedParameterJdbcTemplate.query(getSql(tnL, vo),paraMap,new RowMapper<StoreResultVO>() {

				@Override
				public StoreResultVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					StoreResultVO vo = new StoreResultVO();
					String Dcode=rs.getString("DRUG_DEPT_CODE");
					String Ucode=rs.getString("APPROVE_OPERCODE");
//					String sql="select t.dept_name from t_department t where t.dept_code='"+Dcode+"'";
//					String sql2="select * from t_sys_user t where t.user_code='"+Ucode+"'";
//					SQLQuery query = getSession().createSQLQuery(sql);
//					List list2 = query.list();
//					if(list2!=null && list2.size()>0){
//						vo.setDrugDeptName(list2.get(0).toString());
//					}else{
						vo.setDrugDeptName(Dcode);
//					}
//					SQLQuery query2 = getSession().createSQLQuery(sql2);
//					List list3 = query2.list();
//					if(list3!=null && list3.size()>0){
//						vo.setUserName(list3.get(0).toString());
//					}else{
						vo.setUserName(Ucode);
//					}
					vo.setDrugCode(rs.getString("drug_code"));
					vo.setTradeName(rs.getString("trade_name"));
					vo.setSpecs(rs.getString("specs"));
					vo.setPackQty(rs.getBigDecimal("PACK_QTY"));
					vo.setDrugDeptCode(rs.getString("DRUG_DEPT_CODE"));
					vo.setApprove(rs.getString("APPROVE_OPERCODE"));
					vo.setInoutDate(rs.getDate("APPROVE_DATE"));
					vo.setInoutNum(rs.getBigDecimal("inoutNum"));
					return vo;
				}
			});
		    return list ;
	    }
		
		
	}
	
	/**
	 * 查询入出库记录总数
	 */
	public Integer getCount(List<String> tnL,StoreSearchVo vo,int page,int rows) throws Exception{
		if(tnL==null||tnL.size()<0){
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT count(1)  FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append("  UNION ");
			}
			buffer.append("select  t.drug_code as drugCode from ");
			if(vo!=null && vo.getType()==1){
				buffer.append(HisParameters.HISPARSCHEMAHISUSER).append("T_DRUG_INSTORE t").append(" ");
				buffer.append(" where t").append(".in_state=2 ");
			}else{
				buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(" ");
				buffer.append(" where t").append(".out_state=2 ");
			}
			if(vo!=null){
				if(vo.getBeginTime()!=null){
					String begin =vo.getBeginTime();
					buffer.append(" and t").append(".APPROVE_DATE >=  to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
				}
				if(vo.getEndTime()!=null){
					String end = vo.getEndTime();
					buffer.append(" and t").append(".APPROVE_DATE < to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
				}
				if(StringUtils.isNotBlank(vo.getCode())){
					buffer.append(" and t").append(".DRUG_CODE= :code");
				}
			}
		}
		buffer.append(") ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
	    if(vo!=null){
	    	if(vo.getBeginTime()!=null){
				String begin =vo.getBeginTime();
				paraMap.put("begin", begin);
			}
			if(vo.getEndTime()!=null){
				String end = vo.getEndTime();
				paraMap.put("end", end);
			}
			if(StringUtils.isNotBlank(vo.getCode())){
				paraMap.put("code", vo.getCode());
			}	
		}
	   
		return namedParameterJdbcTemplate.queryForObject(buffer.toString(),paraMap, java.lang.Integer.class);
		
	}
	
	/**
	 * 根据科室id查询科室名称
	 */
	public String getDeptName(String id) throws Exception{
		String hql ="select deptName from SysDepartment where id= ? and stop_flg= ? and del_flg= ?";
		List<String> list = (List<String>) getHibernateTemplate().find(hql,id,0,0);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 根据id查询员工姓名
	 */
	public String getUserName(String id) throws Exception{
		String hql="select name from SysEmployee where id=? or userId.id=?";
		List<String> list = (List<String>) getHibernateTemplate().find(hql,id,id);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
	private String getSql(List<String> tnL,StoreSearchVo vo) throws Exception{
		StringBuffer buffer = new StringBuffer();
		if(vo!=null && vo.getType()!=1){		
		buffer.append("select * from(");
		buffer.append("SELECT n.drug_code, n.trade_name,n.specs,n.PACK_QTY,");
		buffer.append("n.DRUG_DEPT_CODE,n.APPROVE_OPERCODE,n.APPROVE_DATE,n.DRUG_DEPT_NAME,n.APPROVE_OPERNAME,inoutData inoutNum, rownum as rn FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" UNION ");
			}
			buffer.append("select t.drug_code ,t.DRUG_DEPT_NAME,t.trade_name ,t.specs ,");
			buffer.append("t.PACK_QTY ,t.DRUG_DEPT_CODE,t.APPROVE_OPERCODE ,t.APPROVE_OPERNAME,t.APPROVE_DATE,");
				buffer.append("round(t.out_num / t.pack_qty, 2) inoutData from ");
				buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
				buffer.append(" where t.out_state=2 ");
			if(vo!=null){
				if(vo.getBeginTime()!=null){
					String begin =vo.getBeginTime();
					buffer.append(" and t").append(".APPROVE_DATE >=  to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
				}
				if(vo.getEndTime()!=null){
					String end = vo.getEndTime();
					buffer.append(" and t").append(".APPROVE_DATE < to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
				}
				if(StringUtils.isNotBlank(vo.getCode())){
					buffer.append(" and t").append(".DRUG_CODE= :code");
				}
			}
		}
		buffer.append(") n  where ROWNUM  <= (:page) * :row ");
		buffer.append(" ) where rn > (:page -1) * :row  ");
		return buffer.toString();
		}else{
			buffer.append("select * from (");
			buffer.append("SELECT n.drug_code, n.trade_name,n.specs,n.PACK_QTY,");
			buffer.append("(select t.dept_name from t_department t where t.del_flg=0 and t.stop_flg=0 and  t.dept_code=n.DRUG_DEPT_CODE ) DRUG_DEPT_CODE,(select t.employee_name  from t_employee t where t.del_flg=0 and t.stop_flg=0 and t.EMPLOYEE_JOBNO=n.APPROVE_OPERCODE) APPROVE_OPERCODE,n.APPROVE_DATE,inoutData inoutNum, rownum as rn FROM( ");
			for(int i=0;i<tnL.size();i++){
				if(i>0){
					buffer.append(" UNION ");
				}
				buffer.append("select t.drug_code ,t.trade_name ,t.specs ,");
				buffer.append("t.PACK_QTY ,t.DRUG_DEPT_CODE,t.APPROVE_OPERCODE ,t.APPROVE_DATE,");
				buffer.append("round(t.in_num / t.pack_qty, 2)  inoutData from ");
				buffer.append(HisParameters.HISPARSCHEMAHISUSER).append("T_DRUG_INSTORE t ");
				buffer.append(" where t.in_state=2 ");
				if(vo!=null){
					if(vo.getBeginTime()!=null){
						String begin =vo.getBeginTime();
						buffer.append(" and t").append(".APPROVE_DATE >=  to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
					}
					if(vo.getEndTime()!=null){
						String end = vo.getEndTime();
						buffer.append(" and t").append(".APPROVE_DATE < to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
					}
					if(StringUtils.isNotBlank(vo.getCode())){
						buffer.append(" and t").append(".DRUG_CODE= :code");
					}
				}
			}
			buffer.append(") n  where ROWNUM  <= (:page) * :row ");
			buffer.append(" ) where rn > (:page -1) * :row  ");
			return buffer.toString();
		}
	}
	@Override
	public StatVo findMaxMin() throws Exception {
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
}
