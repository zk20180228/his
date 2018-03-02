package cn.honry.statistics.finance.inpatientUDbalance.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.finance.inpatientUDbalance.dao.InpatientUDBDao;
import cn.honry.statistics.finance.inpatientUDbalance.vo.InpatientUDBVo;
@Repository("inpatientUDBDao")
@SuppressWarnings({ "all" })
public class InpatientUDBDaoImpl extends HibernateEntityDao<InpatientUDBVo> implements InpatientUDBDao {
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<InpatientUDBVo> queryDateInfo(String start, String end) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("code, ");//收款员code
		buffer.append("name, ");//收款员
		buffer.append("SUM(srxj) srxj , ");//收入-现金
		buffer.append("SUM(srzp) srzp, ");//收入-支票
		buffer.append("SUM(srqt) srqt, ");//收入-其他
		buffer.append("SUM(zcxj) zcxj, ");//支出-现金
		buffer.append("SUM(zczp) zczp, ");//支出-支票
		buffer.append("SUM(zcqt) zcqt, ");//支出-其他
		buffer.append("SUM(ssxj) ssxj, ");//实收-现金
		buffer.append("SUM(sszp) sszp, ");//实收-支票
		buffer.append("SUM(ssqt) ssqt, ");//实收-其他
		buffer.append("operDate ");
		buffer.append("FROM ( ");
		buffer.append("SELECT ");
		buffer.append("d.OPER_CODE code, ");//收款员code
		buffer.append("d.OPER_NAME name, ");//收款员
		buffer.append("DECODE(d.BALANCE_ITEM,0,d.CA_COST,0) srxj, ");//收入-现金
		buffer.append("DECODE(d.BALANCE_ITEM,0,d.CH_COST,0) srzp, ");//收入-支票
		buffer.append("DECODE(d.BALANCE_ITEM,0,NVL(d.TOT_COST,0)-NVL(d.CA_COST,0)-NVL(d.CH_COST,0),0) srqt, ");//收入-其他
		buffer.append("DECODE(d.BALANCE_ITEM,1,d.CA_COST,0) zcxj, ");//支出-现金
		buffer.append("DECODE(d.BALANCE_ITEM,1,d.CH_COST,0) zczp, ");//支出-支票
		buffer.append("DECODE(d.BALANCE_ITEM,1,NVL(d.TOT_COST,0)-NVL(d.CA_COST,0)-NVL(d.CH_COST,0),0) zcqt, ");//支出-其他
		buffer.append("d.CA_COST ssxj, ");//实收-现金
		buffer.append("d.CH_COST sszp, ");//实收-支票
		buffer.append("NVL(d.TOT_COST,0)-NVL(d.CA_COST,0)-NVL(d.CH_COST,0) ssqt, ");//实收-其他
		buffer.append("TO_CHAR(d.OPER_DATE,'yyyy-MM-dd') operDate ");
		buffer.append("FROM T_OUTPATIENT_DAYBALANCE d ");
		buffer.append("WHERE d.STOP_FLG = :STOP_FLG ");
		buffer.append("AND d.DEL_FLG = :DEL_FLG ");
		buffer.append("AND d.oper_date >= TO_DATE(:start, 'yyyy-mm-dd hh24:mi:ss') ");
		buffer.append("AND d.oper_date <= TO_DATE(:end, 'yyyy-mm-dd hh24:mi:ss') ");
		buffer.append("GROUP BY d.OPER_CODE,d.OPER_NAME,d.BALANCE_ITEM,d.TOT_COST,d.CA_COST,d.CH_COST,d.OPER_DATE ");
		buffer.append(") GROUP BY code,name,operDate ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("STOP_FLG", 0);
		paraMap.put("DEL_FLG", 0);
		paraMap.put("start", start);
		paraMap.put("end", end);
		List<InpatientUDBVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<InpatientUDBVo>() {
			@Override
			public InpatientUDBVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientUDBVo vo = new InpatientUDBVo();
				vo.setName(rs.getString("name"));
				vo.setIcash(rs.getDouble("srxj"));
				vo.setIcheck(rs.getDouble("srzp"));
				vo.setIother(rs.getDouble("srqt"));
				vo.setOcash(rs.getDouble("zcxj"));
				vo.setOcheck(rs.getDouble("zczp"));
				vo.setOother(rs.getDouble("zcqt"));
				vo.setScash(rs.getDouble("ssxj"));
				vo.setScheck(rs.getDouble("sszp"));
				vo.setSother(rs.getDouble("ssqt"));
				vo.setTime(rs.getString("operDate"));
				return vo;
		}});
		
		return voList==null?new ArrayList<InpatientUDBVo>():voList;
	}
}
