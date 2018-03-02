package cn.honry.inpatient.outpatientAdviceFind.dao.impI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.inpatient.outpatientAdviceFind.dao.LisListDao;
import cn.honry.inpatient.outpatientAdviceFind.vo.LisInspectionSample;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceAnalysisVO;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceDetailVo;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceVo;



@Repository("lisListDao")
@SuppressWarnings({"all"})
public class LisListDaoImpl  implements LisListDao{

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Resource
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public List<LisInspectionSample> findLisInfoPage(String page, String rows,String queryName,String type, String id) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from (select s.INSPECTION_ID,s.AGE_INPUT,s.INPATIENT_ID,s.PATIENT_DEPT_NAME,s.PATIENT_NAME,s.PATIENT_SEX,s.TEST_ORDER_NAME,rownum n from LIS_INSPECTION_SAMPLE s where s.OUTPATIENT_ID = :id ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append("  and ( s.TEST_ORDER_NAME like :queryName) ");
		}
		sb.append(" AND rownum <=:rows* :page) where n>(:page - 1) * :rows");
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		paramMap.put("page", start);	
		paramMap.put("rows", count);	
		if(StringUtils.isNotBlank(queryName)){
			paramMap.put("queryName", "%"+queryName+"%");			
		}
		paramMap.put("id", id);
		List<LisInspectionSample> voList =namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<LisInspectionSample>(){
			@Override
			public LisInspectionSample mapRow(ResultSet rs, int rowNum) throws SQLException {
				LisInspectionSample vo = new LisInspectionSample();
				vo.setINSPECTION_ID(rs.getString("INSPECTION_ID"));
				vo.setAGE_INPUT(rs.getString("AGE_INPUT"));
				vo.setINPATIENT_ID(rs.getString("INPATIENT_ID"));
				vo.setPATIENT_DEPT_NAME(rs.getString("PATIENT_DEPT_NAME"));
				vo.setPATIENT_NAME(rs.getString("PATIENT_NAME"));
				vo.setPATIENT_SEX(rs.getString("PATIENT_SEX"));
				vo.setTEST_ORDER_NAME(rs.getString("TEST_ORDER_NAME"));
				return vo;
		}});
		return voList ;
		
	}

	@Override
	public int findLisInfoTotal(String queryName,String type) {
		StringBuffer sb=new StringBuffer();
		sb.append("select count(INSPECTION_ID)  from LIS_INSPECTION_SAMPLE t where 1=1  ");
		if(StringUtils.isNotBlank(queryName)){
			sb.append("  and ( t.TEST_ORDER_NAME like :queryName ) ");
		}
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		if(StringUtils.isNotBlank(queryName)){
			paramMap.put("queryName", "%"+queryName+"%");			
		}
		int count =namedParameterJdbcTemplate.queryForObject(sb.toString(),paramMap, java.lang.Integer.class);
		return count;
	}


	@Override
	public List<OutpatientAdviceDetailVo> findLisDetail(String id) {
		StringBuffer sb=new StringBuffer();
		sb.append("select t.INSPECTION_ID,t.TEST_ITEM_ID,t.TEST_ITEM_SORT,t.CHINESE_NAME,t.ORIGINAL_RESULT,t.QUANTITATIVE_RESULT,t.TEST_ITEM_REFERENCE,t.TEST_ITEM_UNIT,t.SAMPLE_NUMBER from LIS_INSPECTION_RESULT t ");
		sb.append(" where t.INSPECTION_ID= :id");
		Map<String,Object> paramMap = new HashMap<String, Object>();						
		paramMap.put("id", id);	
		List<OutpatientAdviceDetailVo> voList =namedParameterJdbcTemplate.query(sb.toString(),paramMap,new RowMapper<OutpatientAdviceDetailVo>(){
			@Override
			public OutpatientAdviceDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OutpatientAdviceDetailVo vo = new OutpatientAdviceDetailVo();
				vo.setId(rs.getString("INSPECTION_ID"));
				vo.setParentId(rs.getString("TEST_ITEM_ID"));
				vo.setCode(rs.getString("TEST_ITEM_SORT"));//项目分类
				vo.setName(rs.getString("CHINESE_NAME"));//项目名称
				vo.setResult(rs.getString("ORIGINAL_RESULT"));//原始结果
				vo.setLower(rs.getString("QUANTITATIVE_RESULT"));//详情结果
				vo.setUpper(rs.getString("TEST_ITEM_REFERENCE"));//参考结果
				vo.setUnit(rs.getString("TEST_ITEM_UNIT"));//单位
				vo.setNumber(rs.getString("SAMPLE_NUMBER"));//样品数量
				return vo;
		}});
		return voList ;
	}


}
