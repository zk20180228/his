package cn.honry.statistics.icd.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.statistics.icd.dao.IcdAssortDao;
import cn.honry.statistics.icd.vo.IcdAssortTree;
import cn.honry.statistics.icd.vo.IcdAssortVo;

@Repository("icdAssortDao")
public class IcdAssortDaoImpl implements IcdAssortDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<IcdAssortTree> icdTree(String parent_Id) {
		
		StringBuffer sb = new StringBuffer();                                      
		sb.append(" SELECT                                                       ");
		sb.append(" 	ID AS id,                                                ");
		sb.append(" 	ASSORT_NAME AS text,                                     ");
		sb.append(" 	DECODE (IS_PARENT,0,'open',1,'closed','closed') AS state ");
		sb.append(" FROM                                                         ");
		sb.append(" 	T_BUSINESS_ICD10_ASSORT                                  ");
		sb.append("	WHERE                            							 ");
		sb.append("		STOP_FLAG != 1                 							 ");
		sb.append("		AND DEL_FLAG != 1                  						 ");
		sb.append("     AND PARENT_ID='"+parent_Id+"' ");
		sb.append(" ORDER BY CREATETIME                                          ");
		
		return  namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(IcdAssortTree.class));
	}
	
	@Override
	public void updateParentNode(String parent_Id) {
		String sql =" UPDATE T_BUSINESS_ICD10_ASSORT SET IS_PARENT=1 where id='"+parent_Id+"'";
		
		namedParameterJdbcTemplate.update(sql, new HashMap());
	}

	@Override
	public void addIcdAssort(IcdAssortVo icdAssortVo) {
		
		StringBuffer sb = new StringBuffer();	
		sb.append("	INSERT INTO T_BUSINESS_ICD10_ASSORT ( ");
		sb.append("			ID,                           ");
		sb.append("			ASSORT_NAME,                  ");
		sb.append("			IS_PARENT,                    ");
		sb.append("			PARENT_ID,                    ");
		sb.append("			STOP_FLAG,                    ");
		sb.append("			DEL_FLAG,                     ");
		sb.append("			CREATEUSER,                   ");
		sb.append("			UPDATEUSER,                   ");
		sb.append("			CREATETIME,                   ");
		sb.append("			UPDATETIME                   ");
		sb.append("		)                                 ");
		sb.append("		VALUES                            ");
		sb.append("			(                             ");
		sb.append(" '"+icdAssortVo.getId()+"', ");
		sb.append(" '"+icdAssortVo.getAssort_Name()+"', ");
		sb.append(" '"+icdAssortVo.getIs_Parent()+"', ");
		sb.append(" '"+icdAssortVo.getParent_Id()+"', ");
		sb.append(" '"+icdAssortVo.getStop_Flag()+"', ");
		sb.append(" '"+icdAssortVo.getDel_Flag()+"', ");
		sb.append(" '"+icdAssortVo.getCreateUser()+"', ");
		sb.append(" '"+icdAssortVo.getUpdateUser()+"', ");
		sb.append(" '"+icdAssortVo.getCreateTime()+"', ");
		sb.append(" '"+icdAssortVo.getUpdateTime()+"' ");
		sb.append("			)                             ");
		
		namedParameterJdbcTemplate.update(sb.toString(), new HashMap());
	}

	@Override
	public List<IcdAssortVo> findIcdList(String page, String rows,String icdCode) {
		
		Integer p=(page==null?0:Integer.valueOf(page));
		Integer r=(rows==null?30:Integer.valueOf(rows));
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT C.*, D .ASSORT_NAME AS assort_Name FROM( 	   ");
		
		sb.append("	SELECT                                             ");
		sb.append("		b. ID AS ID,                                   ");
		sb.append("		b.icdCode AS icdCode,                          ");
		sb.append("		b.icdName AS icdName,                          ");
		sb.append("		b.ASSORTID AS assortId                        ");
		sb.append("	FROM                                               ");
		sb.append("		(                                              ");
		sb.append("			SELECT                                     ");
		sb.append("				ROWNUM AS rn,                          ");
		sb.append("				A .*                                   ");
		sb.append("			FROM                                       ");
		sb.append("				(                                      ");
		sb.append("					SELECT                             ");
		sb.append("						ICD_ID AS ID,                  ");
		sb.append("						ICD_DIAGNOSTICCODE AS icdCode, ");
		sb.append("						ICD_DIAGNOSTICNAME AS icdName, ");
		sb.append("						ASSORT_ID AS assortId          ");
		sb.append("					FROM                               ");
		sb.append("						T_BUSINESS_ICD10               ");
		sb.append("					WHERE                              ");
		sb.append("						STOP_FLG != 1                  ");
		sb.append("					AND DEL_FLG != 1                   ");
		if(StringUtils.isNoneBlank(icdCode)){
			sb.append("				AND ICD_DIAGNOSTICCODE like '%"+icdCode.trim()+"%' ");
		}
		sb.append("					ORDER BY                           ");
		sb.append("						ICD_DIAGNOSTICCODE             ");
		sb.append("				) A                                    ");
		sb.append("			WHERE                                      ");
		sb.append("				ROWNUM <="+p*r);
		sb.append("		) b                                            ");
		sb.append("	WHERE                                              ");
		sb.append("		b.rn >"+(p-1)*r);
		
		sb.append("	) C LEFT JOIN T_BUSINESS_ICD10_ASSORT D ON C.ASSORTID = D . ID");
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(IcdAssortVo.class));
	}

	@Override
	public Integer findIcdCount(String page, String rows, String icdCode) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT                           ");
		sb.append("		COUNT (1) as totalPages      ");
		sb.append("	FROM                             ");
		sb.append("		T_BUSINESS_ICD10             ");
		sb.append("	WHERE                            ");
		sb.append("		STOP_FLG != 1                ");
		sb.append("	AND DEL_FLG != 1                 ");
		if(StringUtils.isNoneBlank(icdCode)){
			sb.append("	AND ICD_DIAGNOSTICCODE like '%"+icdCode.trim()+"%' ");
		}
		sb.append("	ORDER BY                         ");
		sb.append("		ICD_DIAGNOSTICCODE           ");
		
		List<IcdAssortVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(IcdAssortVo.class));
		
		if(list!=null&&list.size()>0){
			return list.get(0).getTotalPages();
		}
		
		return 0;
	}

	@Override
	public void updateIcdSorrt(String icdId, String assortId) {
		
		String sql =" UPDATE T_BUSINESS_ICD10 SET ASSORT_ID='"+assortId+"' WHERE ICD_ID IN(:icdIds)";
		
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		List<String> icdIds=new ArrayList<String>();
		if(StringUtils.isNoneBlank(icdId)){
			String[] s=icdId.split(","); 
			icdIds=Arrays.asList(s);
		}
		map.put("icdIds", icdIds);
		
		namedParameterJdbcTemplate.update(sql, map);
	}

	@Override
	public List<String> querySubIcdAssortIdsByIcdAssortId(String icdAssortId) {
		String sql = "SELECT t.id FROM T_BUSINESS_ICD10_ASSORT t WHERE t.PARENT_ID = ? AND t.stop_flag != 1 AND t.del_flag != 1";
		return jdbcTemplate.queryForList(sql, String.class, icdAssortId);
	}

	@Override
	public List<String> queryIcdIdsByIcdAssortId(String icdAssortId) {
		String sql = "SELECT t.icd_id FROM T_BUSINESS_ICD10 t WHERE t.ASSORT_ID = ? AND t.stop_flg != 1 AND t.del_flg != 1";
		return jdbcTemplate.queryForList(sql, String.class, icdAssortId);
	}

	@Override
	public String queryIcdAssortNameByIcdAssortId(String icdAssortId) {
		String sql = "SELECT t.assort_name FROM T_BUSINESS_ICD10_ASSORT t WHERE t.id = ? AND t.stop_flag != 1 AND t.del_flag != 1";
		return jdbcTemplate.queryForObject(sql, String.class, icdAssortId);
	}

	@Override
	public String queryIcdNameByIcdId(String icdId) {
		String sql = "SELECT t.icd_diagnosticname FROM T_BUSINESS_ICD10 t WHERE t.icd_id = ? AND t.stop_flg != 1 AND t.del_flg != 1";
		return jdbcTemplate.queryForObject(sql, String.class, icdId);
	}

	@Override
	public String queryIcdCodeByIcdId(String icdId) {
		String sql = "SELECT t.icd_diagnosticcode FROM T_BUSINESS_ICD10 t WHERE t.icd_id = ? AND t.stop_flg != 1 AND t.del_flg != 1";
		return jdbcTemplate.queryForObject(sql, String.class, icdId);
	}

	@Override
	public String queryIcdNameByIcdCode(String icdCode) {
		String sql = "SELECT t.icd_diagnosticname FROM T_BUSINESS_ICD10 t WHERE t.icd_diagnosticcode = ? AND t.stop_flg != 1 AND t.del_flg != 1";
		List<String> list = jdbcTemplate.queryForList(sql, String.class, icdCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<String> queryAllIcdCodes() {
		String sql = "SELECT t.icd_diagnosticcode FROM T_BUSINESS_ICD10 t WHERE t.stop_flg != 1 AND t.del_flg != 1 ORDER BY t.icd_diagnosticcode";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public List<String> queryIcdCodeByLike(String where) {
		String sql = "SELECT t.icd_diagnosticcode FROM T_BUSINESS_ICD10 t" +
				" WHERE (t.icd_diagnosticcode LIKE ? OR t.icd_diagnosticname LIKE ?) AND t.stop_flg != 1 AND t.del_flg != 1 ORDER BY t.icd_diagnosticcode";
		return jdbcTemplate.queryForList(sql, String.class, "%" + where + "%", "%" + where + "%");
	}

	@Override
	public List<String> findIcdAssortIds(String id) {

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                               ");
		sb.append(" 	ID AS id                         ");
		sb.append(" FROM                                 ");
		sb.append(" 	T_BUSINESS_ICD10_ASSORT          ");
		sb.append("	WHERE                            	 ");
		sb.append("		STOP_FLAG != 1                   ");
		sb.append("		AND DEL_FLAG != 1                ");
		sb.append("     AND PARENT_ID=:assortId ");

		Map<String, String> map = new HashMap<>(1);
		map.put("assortId", id);
		return namedParameterJdbcTemplate.queryForList(sb.toString(), map, String.class);
	}

	@Override
	public List<IcdAssortTree> findIcdAssortTreeByAssortId(List<String> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ICD_DIAGNOSTICCODE AS id,ICD_DIAGNOSTICNAME AS text");
		sb.append("	FROM T_BUSINESS_ICD10");
		sb.append("	WHERE STOP_FLG != 1 AND DEL_FLG != 1");
		sb.append("	AND ASSORT_ID in (:assortId)");
		sb.append("	ORDER BY ICD_DIAGNOSTICCODE");
		HashMap<String, List<String>> map = new HashMap<String, List<String>>(1);
		map.put("assortId", list);
		return namedParameterJdbcTemplate.query(sb.toString(), map, new BeanPropertyRowMapper(IcdAssortTree.class));
	}

	@Override
	public List<IcdAssortTree> findSons(String id, String assortName) {

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT                                                       ");
		sb.append(" 	ID AS id,                                                ");
		sb.append(" 	ASSORT_NAME AS text                                      ");
		sb.append(" FROM                                                         ");
		sb.append(" 	T_BUSINESS_ICD10_ASSORT                                  ");
		sb.append("	WHERE                            							 ");
		sb.append("		STOP_FLAG != 1                 							 ");
		sb.append("		AND DEL_FLAG != 1                  						 ");
		if (StringUtils.isNotBlank(id)) {
			sb.append("     AND PARENT_ID='" + id + "' ");
		}
		if (StringUtils.isNotBlank(assortName)) {
			sb.append("     AND ASSORT_NAME like '%" + assortName + "%' ");
		}
		sb.append(" ORDER BY CREATETIME                                          ");

		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(IcdAssortTree.class));
	}

	@Override
	public List<String> findIcdCodesByAssortId(List<String> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("					SELECT                             ");
		sb.append("						ICD_DIAGNOSTICCODE AS code     ");
		sb.append("					FROM                               ");
		sb.append("						T_BUSINESS_ICD10               ");
		sb.append("					WHERE                              ");
		sb.append("						STOP_FLG != 1                  ");
		sb.append("					AND DEL_FLG != 1                   ");
		sb.append("					AND ASSORT_ID in (:assortId)       ");

		HashMap<String, List<String>> map = new HashMap<>(1);
		map.put("assortId", list);

		return namedParameterJdbcTemplate.queryForList(sb.toString(), map, String.class);
	}


}
