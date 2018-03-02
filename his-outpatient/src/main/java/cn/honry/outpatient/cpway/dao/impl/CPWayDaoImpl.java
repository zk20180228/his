package cn.honry.outpatient.cpway.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.outpatient.cpway.dao.CPWayDao;
import cn.honry.outpatient.cpway.vo.CPWayVo;
import cn.honry.outpatient.cpway.vo.ComboxVo;
import cn.honry.outpatient.cpway.vo.PatientVo;

@Repository
public class CPWayDaoImpl implements CPWayDao {
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<ComboxVo> inpatientDeptTree(String deptName) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT                  ");
		sb.append("	  DISTINCT              ");
		sb.append("		DEPT_CODE AS ID,    ");
		sb.append("		DEPT_NAME AS text,  ");
		sb.append("		'closed' state      ");//该节点默认是父节点，要关闭
		sb.append("	FROM                    ");
		sb.append("		T_DEPARTMENT        ");
		sb.append("	WHERE                   ");
		sb.append("		DEL_FLG = 0         ");
		sb.append("	AND STOP_FLG = 0        ");
		sb.append("	AND DEPT_TYPE = 'I'     ");
		if(StringUtils.isNotBlank(deptName)){
			sb.append("	AND DEPT_NAME like  '%"+deptName+"%' ");
		}
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ComboxVo.class));
	}

	@Override
	public List<ComboxVo> patientList(String id) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" SELECT                                                                 ");
		sb.append(" 	f.INPATIENT_NO AS ID,                                              ");
		sb.append(" 	'open' AS state,                                                   ");//该节点不在有子节点，因为要展开
		sb.append(" 	T .PATIENT_NAME AS text                                            ");
		sb.append(" FROM                                                                   ");
		sb.append(" 	T_PATH_APPLY f                                                     ");
		sb.append(" LEFT JOIN t_inpatient_info_now T ON f.INPATIENT_NO = T .INPATIENT_NO   ");
		sb.append(" AND T .DEL_FLG = 0                                                     ");
		sb.append(" AND T .STOP_FLG = 0                                                    ");
		sb.append(" WHERE                                                                  ");
		sb.append(" 	f.DEL_FLG = 0                                                      ");
		sb.append(" AND f.STOP_FLG = 0                                                     ");
		sb.append(" AND f.APPLY_CODE ='"+id+"'                                             ");
		
		
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ComboxVo.class));
	}

	@Override
	public List<CPWayVo> cPWayPatientList(String page, String rows, String id) {
		
		Integer p =(page==null?1:Integer.valueOf(page));
		Integer r =(rows==null?20:Integer.valueOf(rows));
		StringBuffer sb = new StringBuffer();
		
		sb.append(" 	SELECT                                                           ");
		sb.append(" 	M . ID AS ID,                                                    ");
		sb.append(" 	M .cpName AS cpName,                                             ");
		sb.append(" 	M .version_no AS version_no,                                     ");
		sb.append(" 	M .inpatient_no AS inpatient_no,                                 ");
		sb.append(" 	(                                                                ");
		sb.append(" 		SELECT                                                       ");
		sb.append(" 			n.PATIENT_NAME                                           ");
		sb.append(" 		FROM                                                         ");
		sb.append(" 			t_inpatient_info_now n                                   ");
		sb.append(" 		WHERE                                                        ");
		sb.append(" 			n.inpatient_no = M .inpatient_no                         ");
		sb.append(" 		AND ROWNUM <= 1                                              ");
		sb.append(" 	) AS patientName,                                                ");
		sb.append(" 	M .medicalrecord_id AS medicalrecord_id,                         ");
		sb.append(" 	M .apply_type AS apply_type,                                     ");
		sb.append(" 	M .apply_status AS apply_status,                                 ");
		sb.append(" 	(select d.DEPT_NAME  from T_DEPARTMENT d WHERE M .apply_code =d.DEPT_CODE and d.DEL_FLG = 0 and d.STOP_FLG = 0 ) AS apply_code,");
		sb.append(" 	M .apply_doct_code AS apply_doct_code,                           ");
		sb.append(" 	M .apply_date AS apply_date,                                     ");
		sb.append(" 	M .apply_memo AS apply_memo                                      ");
		sb.append(" FROM                                                                 ");
		sb.append(" 	(                                                                ");
		sb.append(" 		SELECT                                                       ");
		sb.append(" 			T .*, ROWNUM AS rs                                       ");
		sb.append(" 		FROM                                                         ");
		sb.append(" 			(                                                        ");
		sb.append(" 				SELECT                                               ");
		sb.append(" 					f.*, c.CP_NAME AS cpName                         ");
		sb.append(" 				FROM                                                 ");
		sb.append(" 					(                                                ");
		sb.append(" 						SELECT                                       ");
		sb.append(" 							P . ID AS ID,                            ");
		sb.append(" 							P .CP_ID AS CP_ID,                       ");
		sb.append(" 							P .version_no AS version_no,             ");
		sb.append(" 							P .inpatient_no AS inpatient_no,         ");
		sb.append(" 							P .medicalrecord_id AS medicalrecord_id, ");
		sb.append(" 							P .apply_type AS apply_type,             ");
		sb.append(" 							P .apply_status AS apply_status,         ");
		sb.append(" 							P .apply_code AS apply_code,             ");
		sb.append(" 							P .apply_doct_code AS apply_doct_code,   ");
		sb.append(" 							P .apply_date AS apply_date,             ");
		sb.append(" 							P .apply_memo AS apply_memo              ");
		sb.append(" 						FROM                                         ");
		sb.append(" 							T_PATH_APPLY P                           ");
		sb.append(" 						WHERE                                        ");
		sb.append(" 							P .DEL_FLG = 0                           ");
		sb.append(" 						AND P .STOP_FLG = 0                          ");
		sb.append(" 						AND (                                        ");
		sb.append(" 							P .APPLY_CODE = '"+id+"'                 ");
		sb.append(" 							OR P .INPATIENT_NO = '"+id+"'            ");
		sb.append(" 						)                                            ");
		sb.append(" 						ORDER BY                                     ");
		sb.append(" 							P .CREATETIME DESC,                      ");
		sb.append(" 							P .APPLY_STATUS ASC                      ");
		sb.append(" 					) f                                              ");
		sb.append(" 				LEFT JOIN T_CPWAY c ON c. ID = f.CP_ID               ");
		sb.append(" 			) T                                                      ");
		sb.append(" 		WHERE                                                        ");
		sb.append(" 			ROWNUM <= "+p*r+"                                        ");
		sb.append(" 	) M                                                              ");
		sb.append(" WHERE                                                                ");
		sb.append(" 	M .rs > "+(p-1)*r+"                                              ");
	
		
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(CPWayVo.class));
	}

	@Override
	public Integer cPWayPatientCount(String id) {
		
		String sql ="SELECT COUNT (1) FROM T_PATH_APPLY WHERE DEL_FLG = 0 AND STOP_FLG = 0 AND (APPLY_CODE = '"+id+"' OR INPATIENT_NO = '"+id+"')";
		
		return namedParameterJdbcTemplate.queryForObject(sql, new HashMap(), Integer.class);
	}

	@Override
	public void addCPWayPatient(CPWayVo cPWayVo) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" INSERT INTO T_PATH_APPLY ( ");
		sb.append(" 		ID,                ");
		sb.append(" 		CP_ID,             ");
		sb.append(" 		VERSION_NO,        ");
		sb.append(" 		APPLY_NO,          ");
		sb.append(" 		INPATIENT_NO,      ");
		sb.append(" 		MEDICALRECORD_ID,  ");
		sb.append(" 		APPLY_TYPE,        ");
		sb.append(" 		APPLY_STATUS,      ");
		sb.append(" 		APPLY_CODE,        ");
		sb.append(" 		APPLY_DOCT_CODE,   ");
		sb.append(" 		APPLY_DATE,        ");
		sb.append(" 		APPLY_MEMO,        ");
		sb.append(" 		APPROVAL_USER,     ");
		sb.append(" 		APPROVAL_DATE,     ");
		sb.append(" 		EXECUTE_DATE,      ");
		sb.append(" 		UPDATEUSER,        ");
		sb.append(" 		UPDATETIME,        ");
		sb.append(" 		STOP_FLG,          ");
		sb.append(" 		DEL_FLG,           ");
		sb.append(" 		HOSPITAL_ID,       ");
		sb.append(" 		AREA_CODE,         ");
		sb.append(" 		CREATEUSER,        ");
		sb.append(" 		CREATETIME         ");
		sb.append(" 	)                      ");
		sb.append(" 	VALUES                 ");
		sb.append(" 		(                  ");
		
		sb.append(" '"+cPWayVo.getId()+"',          		  ");
		sb.append(" '"+cPWayVo.getCp_id()+"',        		  ");
		sb.append(" '"+cPWayVo.getVersion_no()+"',            ");
		sb.append(" '"+cPWayVo.getApply_no()+"',          	  ");
		sb.append(" '"+cPWayVo.getInpatient_no()+"',          ");
		sb.append(" '"+cPWayVo.getMedicalrecord_id()+"',      ");
		sb.append(" '"+cPWayVo.getApply_type()+"',            ");
		sb.append(" '"+cPWayVo.getApply_status()+"',          ");
		sb.append(" '"+cPWayVo.getApply_code()+"',            ");
		sb.append(" '"+cPWayVo.getApply_doct_code()+"',       ");
		sb.append(" to_date('"+cPWayVo.getApply_date()+"','yyyy-mm-dd hh24:mi:ss'),      ");
		sb.append(" '"+cPWayVo.getApply_memo()+"',            ");
		sb.append(" decode('"+cPWayVo.getApproval_user()+"','null',null,'"+cPWayVo.getApproval_user()+"'),         ");
		sb.append(" to_date(decode('"+cPWayVo.getApproval_date()+"','null',null,'"+cPWayVo.getApproval_date()+"'),'yyyy-mm-dd hh24:mi:ss'),   ");
		sb.append(" to_date(decode('"+cPWayVo.getExecute_date()+"','null',null,'"+cPWayVo.getExecute_date()+"'),'yyyy-mm-dd hh24:mi:ss'),    ");
		sb.append(" '"+cPWayVo.getUpdateUser()+"',            "); 
		sb.append(" to_date('"+cPWayVo.getUpdateTime()+"','yyyy-mm-dd hh24:mi:ss'),      ");
		sb.append(" "+cPWayVo.getStop_flg()+",                ");
		sb.append(" "+cPWayVo.getDel_flg()+",                 ");
		sb.append(" '"+cPWayVo.getHospital_id()+"',           ");
		sb.append(" '"+cPWayVo.getArea_code()+"',             ");
		sb.append(" '"+cPWayVo.getCreateUser()+"',            ");
		sb.append(" to_date('"+cPWayVo.getCreateTime()+"','yyyy-mm-dd hh24:mi:ss')       ");
		
		
		sb.append(" 		)                  ");
			
		
		namedParameterJdbcTemplate.update(sb.toString(), new HashMap());
		
	}

	@Override
	public Integer checkIsAdd(String inpatient_no) {
		
		String sql="SELECT COUNT (1) FROM T_PATH_APPLY WHERE DEL_FLG = 0 AND STOP_FLG = 0 AND INPATIENT_NO = '"+inpatient_no+"'";
		
		return namedParameterJdbcTemplate.queryForObject(sql, new HashMap(), Integer.class);
	}

	@Override
	public PatientVo findPatient(String inpatient_no) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("	SELECT DISTINCT                           ");
		sb.append("		MEDICALRECORD_ID as medicalrecord_id, ");
		sb.append("		PATIENT_NAME AS patientName           ");
		sb.append("	FROM                                      ");
		sb.append("		T_INPATIENT_INFO_NOW                  ");
		sb.append("	WHERE                                     ");
		sb.append("		DEL_FLG = 0                           ");
		sb.append("	AND STOP_FLG = 0                          ");
		sb.append("	AND INPATIENT_NO = '"+inpatient_no+"'     ");
			
		List<PatientVo> list = namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(PatientVo.class));
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<ComboxVo> cPWList() {
		
		String sql="SELECT ID AS ID,CP_NAME AS text FROM T_CPWAY WHERE DEL_FLG = 0 AND STOP_FLG = 0";
		
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(ComboxVo.class));
	}

	@Override
	public List<ComboxVo> findVersionList(String cPWId) {
		
		String sql="SELECT VERSION_NO AS ID,VERSION_NO AS text FROM	T_CP_VCONTROL WHERE	DEL_FLG = 0 AND STOP_FLG = 0 and CP_ID='"+cPWId+"'";
		
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(ComboxVo.class));
	}

	@Override
	public void approveApply(String cPWAppId, CPWayVo vo) {
		
		String sql =" update  T_PATH_APPLY set apply_status='"+vo.getApply_status()+"', "
				+ " approval_user='"+vo.getApproval_user()+"',updateuser='"+vo.getUpdateUser()+"', "
			    + " approval_date=to_date('"+vo.getApproval_date()+"','yyyy-mm-dd hh24:mi:ss'), "
			    + " execute_date=to_date('"+vo.getExecute_date()+"','yyyy-mm-dd hh24:mi:ss'), "
			    + " updatetime=to_date('"+vo.getUpdateTime()+"','yyyy-mm-dd hh24:mi:ss') "
			    + "  WHERE ID in (:ids)";
		
		String[] ids=cPWAppId.split(",");
		Map map = new HashMap();
		map.put("ids", Arrays.asList(ids));
		
		
		namedParameterJdbcTemplate.update(sql, map);
	}

	@Override
	public void delCPWayPatient(String cPWAppId) {
		
		String[] ids=cPWAppId.split(",");
		
		String sql =" update  T_PATH_APPLY set DEL_FLG=1,STOP_FLG=1 WHERE ID in (:ids)";
		Map map = new HashMap();
		map.put("ids", Arrays.asList(ids));
		
		namedParameterJdbcTemplate.update(sql, map);
	}

	
	public String getArea_code(String deptCode){
		String sql = "SELECT DEPT_AREA_CODE  FROM T_DEPARTMENT where DEL_FLG =0 and STOP_FLG=0 and DEPT_CODE='"+deptCode+"' and ROWNUM <=1";
		return namedParameterJdbcTemplate.queryForObject(sql, new HashMap(), String.class);
	}
	
	
	public String getApply_no(){
		
		String sql=" select t_path_apply_seq.nextval from dual ";
		return namedParameterJdbcTemplate.queryForObject(sql, new HashMap(), String.class);
	}
}
