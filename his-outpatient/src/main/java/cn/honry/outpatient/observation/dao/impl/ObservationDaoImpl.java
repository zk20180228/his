package cn.honry.outpatient.observation.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.outpatient.observation.dao.ObservationDao;
import cn.honry.outpatient.observation.vo.ComboxVo;
import cn.honry.outpatient.observation.vo.ObservationVo;
import cn.honry.outpatient.observation.vo.PatientRegisterVo;
import cn.honry.outpatient.observation.vo.PatientVo;

@Repository
public class ObservationDaoImpl implements ObservationDao {

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
	@Override
	public int saveObservation(ObservationVo observation) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" INSERT INTO T_OUTPATIENT_OBSERVATION ( ");
		sb.append("	ID,CARD_NO,PATIENT_NO,OBSERVATION_INTIME,OBSERVATION_OUTTIME, "
				+ " OBSERVATION_DAYS,DIAGNOSIS,LAPSE_TO,DOCTOR_NAME,DOCTOR_CODE, "
				+ " NURSE_NAME,NURSE_CODE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER, "
				+ " UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG) ");
		sb.append(" VALUES ");	
		sb.append(" ('"+observation.getId()+"','"+observation.getCardNo()+"','"+observation.getPatientNo()+"',to_date('"
					   +observation.getObservationIntime()+"','yyyy-mm-dd hh24:mi:ss'),to_date('"+observation.getObservationOuttime()+"','yyyy-mm-dd hh24:mi:ss'),"
					   +observation.getObservationDays()+",'"+observation.getDiagnosis()+"','"+observation.getLapseTo()+"','"
					   +observation.getDoctorName()+"','"+observation.getDoctorCode()+"','"+observation.getNurseName()+"','"
					   +observation.getNurseCode()+"','"+observation.getCreateUser()+"','"+observation.getCreateDept()+"',to_date('"
					   +observation.getCreateTime()+"','yyyy-mm-dd hh24:mi:ss'),'"+observation.getUpdateUser()+"',to_date('"+observation.getUpdateTime()+"','yyyy-mm-dd hh24:mi:ss'),'"
					   +observation.getDeleteUser()+"',"
					   + "to_date(decode('"+observation.getDeleteTime()+"','null',null),'yyyy-mm-dd hh24:mi:ss'),"
					   +observation.getStop_flg()+","
					   +observation.getDel_flg()+") ");
		System.out.println(sb.toString());
		return namedParameterJdbcTemplate.update(sb.toString(), new HashMap());
		
	}


	@Override
	public List<PatientVo> findPatientInfoByCardNo(String cardNo) {
		
		StringBuffer sb =new StringBuffer();                                   
		sb.append("	select 	                                                     ");
		sb.append("		t3.patientName as patientName,                           ");
		sb.append("		t3.sex as sex,                                           ");
		sb.append("		t3.age as age,                                           ");
		sb.append("		to_char(t3.birthday,'yyyy-mm-dd') as birthday,           ");
		sb.append("		t3.identityType as identityType,                         ");
		sb.append("		t3.identityCode as identityCode,                         ");
		sb.append("		t3.address as address,                                   ");
		sb.append("		t4.UNIT_NAME as unionUnit,                               ");
		sb.append("		t3.patientNo as patientNo                                ");
		sb.append("	from(                                                        ");
		sb.append("	SELECT                                                       ");
		sb.append("		t1.PATIENT_NAME as patientName,                          ");
		sb.append("		t2.CODE_NAME as sex,                                     ");
		sb.append("		t1.PATIENT_AGE as age,                                   ");
		sb.append("		t1.PATIENT_BIRTHDAY as birthday,                         ");
		sb.append("		t1.CODE_NAME as identityType,                            ");
		sb.append("		t1.PATIENT_CERTIFICATESNO as identityCode,               ");
		sb.append("		t1.PATIENT_ADDRESS as address,                           ");
		sb.append("		t1.UNIT_ID as unionUnit,                                 ");
		sb.append("		t1.MEDICALRECORD_ID as patientNo                         ");
		sb.append("	FROM(                                                        ");
		sb.append("			SELECT                                               ");
		sb.append("				t.PATIENT_NAME,                                  ");
		sb.append("				t.PATIENT_SEX,                                   ");
		sb.append("				DECODE (t.PATIENT_AGE, 0, '') as PATIENT_AGE,    ");
		sb.append("				t.PATIENT_BIRTHDAY,                              ");
		sb.append("				b.CODE_NAME as CODE_NAME,                        ");
		sb.append("				t.PATIENT_CERTIFICATESNO,                        ");
		sb.append("				t.PATIENT_ADDRESS,                               ");
		sb.append("				t.UNIT_ID,                                       ");
		sb.append("				t.MEDICALRECORD_ID                               ");
		sb.append("			FROM                                                 ");
		sb.append("				T_PATIENT t,T_BUSINESS_DICTIONARY b              ");
		sb.append("			WHERE                                                ");
		sb.append("				CARD_NO = '"+cardNo+"'                           ");
		sb.append("				and t.PATIENT_CERTIFICATESTYPE=b.CODE_ENCODE     ");
		sb.append("				and b.CODE_TYPE='certificate'                    ");
		sb.append("	                                                             ");
		sb.append("		) t1,T_BUSINESS_DICTIONARY t2                            ");
		sb.append("	WHERE                                                        ");
		sb.append("		T1.PATIENT_SEX = T2.CODE_ENCODE                          ");
		sb.append("	AND T2.CODE_TYPE = 'sex') t3,T_BUSINESS_CONTRACTUNIT t4      ");
		sb.append("	where T3.unionUnit=t4.UNIT_CODE AND ROWNUM <= 1              ");
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(PatientVo.class));
	}


	@Override
	public List<PatientRegisterVo> findRegisterInfoByCardNo(String cardNo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT t.* FROM(                                                   ");
		sb.append("	SELECT                                                             ");
		sb.append("		PATIENT_NAME as patientName,                                   ");
		sb.append("		DECODE (PATIENT_SEX,1,'男',2,'女',3,'未知') as patientSex,       ");
		sb.append("		CLINIC_CODE as clinicCode,                                     ");
		sb.append("		DEPT_NAME as dept,                                             ");
		sb.append("		DOCT_NAME as expxrt,                                           ");
		sb.append("		DECODE (EMERGENCY_FLAG,0,'普通门诊',1,'急诊') as type,            ");
		sb.append("		TO_CHAR (REG_DATE,'yyyy-mm-dd hh24:mi:ss') as regDate          ");
		sb.append("	FROM                                                               ");
		sb.append("		T_REGISTER_MAIN_NOW                                            ");
		sb.append("	WHERE                                                              ");
		sb.append("		CARD_NO = '"+cardNo+"'                                         ");
		sb.append("	ORDER BY                                                           ");
		sb.append("		REG_DATE DESC                                                  ");
		sb.append(" ) t where ROWNUM <= 1  ");
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(PatientRegisterVo.class));
	}


	@Override
	public List<ComboxVo> findNurseCode() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT                  ");
		sb.append("		EMPLOYEE_CODE as id,    ");
		sb.append("		EMPLOYEE_NAME as text   ");
		sb.append("	FROM                        ");
		sb.append("		T_EMPLOYEE   			");
		sb.append("	WHERE                       ");
		sb.append("		EMPLOYEE_TYPE ='2'     ");
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ComboxVo.class));
	}


	@Override
	public List<ComboxVo> findDoctorCode() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("		SELECT                  ");
		sb.append("		EMPLOYEE_CODE as id,    ");
		sb.append("		EMPLOYEE_NAME as text   ");
		sb.append("	FROM                        ");
		sb.append("		T_EMPLOYEE   			");
		sb.append("	WHERE                       ");
		sb.append("		EMPLOYEE_TYPE ='1'      ");
		
		return namedParameterJdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(ComboxVo.class));
	}
	
}
