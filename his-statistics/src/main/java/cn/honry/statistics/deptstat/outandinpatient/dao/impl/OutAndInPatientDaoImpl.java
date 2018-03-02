package cn.honry.statistics.deptstat.outandinpatient.dao.impl;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.outandinpatient.dao.OutAndInPatientDao;
import cn.honry.statistics.deptstat.outandinpatient.vo.GetOrOutPatient;
import cn.honry.statistics.deptstat.outandinpatient.vo.InPatientVo;
import cn.honry.statistics.deptstat.outandinpatient.vo.OutPatientVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("outAndInPatientDao")
@Transactional
@SuppressWarnings("all")
public class OutAndInPatientDaoImpl  extends HibernateEntityDao<InPatientVo> implements OutAndInPatientDao {
	@Resource(name = "sessionFactory")
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<InPatientVo> queryinPatientMsg(List<String> tnl,
			String startTime, String endTime, String dept,String menuAlias,String page,String rows) throws Exception {
		if (tnl == null || tnl.size() < 0) {
			return new ArrayList<InPatientVo>();
		}
		StringBuffer sb = new StringBuffer(500);
		sb.append("select patientno,name,sex,ageUnit,age,bedNumber,job,indate,adress,tel,clinic,pact from ( ");
 		sb.append("select rownum rn,t.patientno patientno,t.name name,t.sex sex,t.ageUnit ageUnit,t.age age,t.bedNumber bedNumber,(SELECT D.CODE_NAME FROM t_business_dictionary D WHERE D.CODE_TYPE = 'occupation'  AND D.CODE_ENCODE=t.PROF_CODE) job,");
		sb.append("t.indate indate,t.adress adress,t.tel tel,(select t.unit_name from t_business_contractunit t where t.unit_code=t.PACT_CODE) pact,(select b.clinic_diagicdname from t_emr_base b where t.Inpatient_No=b.INPATIENT_NO) clinic from ( ");
 		for (int i = 0; i < tnl.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append("SELECT t").append(i).append(".MEDICALRECORD_ID patientno,").append("t")
					.append(i).append(".PATIENT_NAME name,").append("t")
					.append(i).append(".REPORT_SEX sex,t"+i+".REPORT_AGEUNIT as ageUnit,");
			sb.append("t").append(i).append(".REPORT_AGE age,").append("t").append(i).append(".BED_NAME bedNumber,")
					.append("t").append(i).append(".PROF_CODE,");
			sb.append("t").append(i).append(".IN_DATE indate,").append("t")
					.append(i).append(".HOME adress,").append("t").append(i)
					.append(".HOME_TEL tel,").append("t").append(i).append(".PACT_CODE,");
			sb.append("t").append(i).append(".Inpatient_No ");
			sb.append(" from ");
			sb.append(tnl.get(i)).append("  t" + i).append(" where  ");
			sb.append(" t").append(i).append(".IN_DATE>=to_date('" + startTime+ "','yyyy-MM-dd')");
			sb.append(" and t").append(i).append(".IN_DATE<=to_date('" + endTime+ "','yyyy-MM-dd')");
			
		}
		sb.append(" ) t,t_inpatient_shiftdata H where H.SHIFT_TYPE='B' and H.Clinic_No=t.inpatient_no and rownum<="+page+"*"+rows+" ");
 		if (StringUtils.isNotBlank(dept)) {
			sb.append(" and H.NEW_DATA_CODE in('"+dept.replace(",", "','")+"') ");
		}else{
			sb.append(" and H.NEW_DATA_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
		}
 		sb.append("order by indate desc  ");
		if(StringUtils.isNotBlank(page)){
			sb.append(" ) AL where AL.rn>=("+page+"-1)*"+rows+" ");
		}else{
			sb.append(" ) L  ");
		}
		
		SQLQuery sqlQuery=super.getSession().createSQLQuery(sb.toString())
				.addScalar("patientno").addScalar("name").addScalar("sex").addScalar("age",Hibernate.INTEGER)
				.addScalar("bedNumber").addScalar("job").addScalar("indate",Hibernate.TIMESTAMP).addScalar("adress").addScalar("tel")
				.addScalar("pact").addScalar("clinic").addScalar("ageUnit");
		List<InPatientVo> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(InPatientVo.class)).list();
		if(list.size() > 0){
			return list;
		}
		return new ArrayList<InPatientVo>();
	}

	@Override
	public List<OutPatientVo> queryOutPatientMsg(List<String> tnL,
			String startTime, String endTime, String dept,String menuAlias,String page,String rows) throws Exception {
		if(tnL.size()>0){
			StringBuffer buffer=new StringBuffer(1800);
			if(StringUtils.isNotBlank(page)){
				buffer.append("select * from ( ");
			}
			buffer.append("SELECT distinct(I.MEDICALRECORD_ID) patient,rownum as rn,");
			buffer.append("I.PATIENT_NAME name,I.REPORT_SEX sex,I.REPORT_AGE age,I.REPORT_AGEUNIT as ageUnit,");
			buffer.append("I.BED_NAME bedNumber,i.CHARGE_DOC_NAME doctor,I.DUTY_NURSE_NAME nurse,");
			buffer.append("I.IN_DATE indate,I.out_date outdate,");
			buffer.append("(SELECT D.CODE_NAME FROM t_business_dictionary D ");
			buffer.append("WHERE D.CODE_TYPE = 'healthState' ");
			buffer.append("AND D.CODE_ENCODE =I.ZG) status,(select t.unit_name from t_business_contractunit t where t.unit_code=I.PACT_CODE) pact,");
			buffer.append(" (select b.main_diagicdname from t_emr_base b where   I.Inpatient_No  = b.INPATIENT_NO) as clinic ");
			buffer.append("FROM t_inpatient_shiftdata H,(");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select I"+i+".MEDICALRECORD_ID,I"+i+".PATIENT_NAME,I"+i+".REPORT_SEX,I"+i+".REPORT_AGE,I"+i+".REPORT_AGEUNIT,");
				buffer.append("I"+i+".BED_NAME,I"+i+".CHARGE_DOC_NAME,I"+i+".DUTY_NURSE_NAME,I"+i+".IN_DATE,out_date,I"+i+".ZG,I"+i+".PACT_CODE,I"+i+".INPATIENT_NO,I"+i+".DEPT_CODE ");
				buffer.append("from "+tnL.get(i)+" I"+i+" ");
			}
			buffer.append(") I ");
			buffer.append("WHERE H.SHIFT_TYPE IN ('O') ");
			buffer.append("AND I.OUT_DATE >= TO_DATE('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND I.OUT_DATE < TO_DATE('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			if(StringUtils.isNotBlank(dept)){//如果查询科室不为空  走选中科室
				buffer.append("AND I.DEPT_CODE in('"+dept.replace(",", "','")+"') ");
			}else{//如果为空 走授权科室
				buffer.append("AND I.DEPT_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
			buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO ");
			if(StringUtils.isNotBlank(page)){
				buffer.append(" AND rownum <="+page+"*"+rows+" ");
				buffer.append("ORDER BY I.out_date ) AL where AL.rn>=("+page+"-1)*"+rows+" ");
			}else{
				buffer.append("ORDER BY I.out_date" );
			}
			Map<String,String> map=new HashMap<String,String>();
			List<OutPatientVo> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<OutPatientVo>() {
				@Override
				public OutPatientVo mapRow(ResultSet rs, int rownum)
						throws SQLException {
					OutPatientVo vo=new OutPatientVo();
					vo.setPatientno(rs.getString("patient"));	
					vo.setName(rs.getString("name"));
					vo.setSex(rs.getString("sex"));
					vo.setAge(rs.getInt("age"));
					vo.setAgeUnit(rs.getString("ageUnit"));
					vo.setBedNumber(rs.getString("bedNumber"));
					vo.setDoctor(rs.getString("doctor"));
					vo.setNurse(rs.getString("nurse"));
					vo.setIndate(rs.getTimestamp("indate"));
					vo.setOutdate(rs.getTimestamp("outdate"));
					vo.setStatus(rs.getString("status"));
					vo.setPact(rs.getString("pact"));
					vo.setClinic(rs.getString("clinic"));
					return vo;
				}
			});
			if( list != null && list.size() > 0 ){
				return list;
			}
		}
		return new ArrayList<OutPatientVo>();
	}

	@Override
	public List<GetOrOutPatient> queryGetInPatientMsg(List<String> tnL,
			String startTime, String endTime, String dept,String menuAlias,String page,String rows) {
 		if(tnL.size()>0){
			StringBuffer buffer=new StringBuffer(800);
			if(StringUtils.isNotBlank(page)){
				buffer.append("select * from (");
			}
			buffer.append("SELECT I.MEDICALRECORD_ID patientno,rownum as rn,");
			buffer.append("I.PATIENT_NAME name, I.REPORT_SEX sex,");
			buffer.append("(select t.unit_name from t_business_contractunit t where t.unit_code=I.PACT_CODE) pact,H.OLD_DATA_NAME beforeDept,");
			buffer.append("(SELECT SHI.NEW_DATA_CODE FROM t_inpatient_shiftdata SHI WHERE SHI.SHIFT_TYPE = 'RB' AND SHI.CLINIC_NO = H.CLINIC_NO AND abs(SHI.Createtime - H.Createtime)*24*60<2 AND rownum =1 ) beforeBedNo,");
			buffer.append("H.NEW_DATA_NAME afterDept,");
			buffer.append("(SELECT SHI.OLD_DATA_CODE FROM t_inpatient_shiftdata SHI WHERE SHI.SHIFT_TYPE = 'RB' AND SHI.CLINIC_NO = H.CLINIC_NO AND abs(SHI.Createtime - H.Createtime)*24*60<2 AND rownum =1 ) afterBedNo,");
			buffer.append("I.IN_DATE inDate,H.Createtime turnDate,");
			buffer.append("(select   b.clinic_diagicdname from t_emr_base b where   I.Inpatient_No  = b.INPATIENT_NO) clinic ");
			buffer.append("FROM t_inpatient_shiftdata H,(");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select I"+i+".MEDICALRECORD_ID,I"+i+".PATIENT_NAME,I"+i+".REPORT_SEX,I"+i+".PACT_CODE,");
				buffer.append("I"+i+".IN_DATE,I"+i+".Inpatient_No ");
				buffer.append("from "+tnL.get(i)+" I"+i+" ");
			}
			buffer.append(") I ");
			buffer.append("WHERE H.SHIFT_TYPE = 'RO' ");
			buffer.append("AND H.Createtime >= TO_DATE('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND H.Createtime < TO_DATE('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO ");
			if(StringUtils.isNotBlank(dept)){//查询选中科室
				buffer.append("AND H.NEW_DATA_CODE in('"+dept.replace(",", "','")+"') ");
			}else{//查询授权科室
				buffer.append("AND H.NEW_DATA_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
			if(StringUtils.isNotBlank(page)){
				buffer.append("and rownum<="+page+"*"+rows+" ORDER BY H.Createtime ) AL where AL.rn>=("+page+"-1)*"+rows+" ");
			}else{
				buffer.append("ORDER BY H.Createtime ");
			}
			Map<String,String> map=new HashMap<String,String>();
			List<GetOrOutPatient> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<GetOrOutPatient>() {
				@Override
				public GetOrOutPatient mapRow(ResultSet rs, int rownum)
						throws SQLException {
					GetOrOutPatient vo=new GetOrOutPatient();
					vo.setPatientno(rs.getString("patientno"));	
					vo.setName(rs.getString("name"));
					vo.setSex(rs.getString("sex"));
					vo.setBeforeDept(rs.getString("beforeDept"));
					vo.setPact(rs.getString("pact"));
					vo.setClinic(rs.getString("clinic"));
					vo.setBeforeBedNo(rs.getString("beforeBedNo"));
					vo.setAfterDept(rs.getString("afterDept"));
					vo.setAfterBedNo(rs.getString("afterBedNo"));
					vo.setInDate(rs.getTimestamp("inDate"));
					vo.setTurnDate(rs.getTimestamp("turnDate"));
					return vo;
				}
			});
			if( list != null && list.size() > 0 ){
				return list;
			}
		}
		return new ArrayList<GetOrOutPatient>();
	}

	@Override
	public List<GetOrOutPatient> queryGetOutPatientMsg(List<String> tnL,
			String startTime, String endTime, String dept,String menuAlias,String page,String rows) {
		if(tnL.size()>0){
			StringBuffer buffer=new StringBuffer(800);
			if(StringUtils.isNotBlank(page)){
				buffer.append("select * from ( ");
			}
			buffer.append("SELECT I.MEDICALRECORD_ID patient, I.PATIENT_NAME name,");
			buffer.append("I.REPORT_SEX sex,(select t.unit_name from t_business_contractunit t where t.unit_code=I.PACT_CODE) pact,H.OLD_DATA_NAME beforeDept,");
			buffer.append("(SELECT SHI.NEW_DATA_CODE FROM t_inpatient_shiftdata SHI WHERE SHI.SHIFT_TYPE = 'RB' AND SHI.CLINIC_NO = H.CLINIC_NO AND abs(SHI.Createtime - H.Createtime)*24*60<2 AND rownum =1 ) beforeBedNo,");
			buffer.append("H.NEW_DATA_NAME afterDept,rownum as rn,");
			buffer.append("(SELECT SHI.OLD_DATA_CODE FROM t_inpatient_shiftdata SHI WHERE SHI.SHIFT_TYPE = 'RB' AND SHI.CLINIC_NO = H.CLINIC_NO AND abs(SHI.Createtime - H.Createtime)*24*60<2 AND rownum =1 ) afterBedNo,");
			buffer.append("I.IN_DATE inDate, H.Createtime turnDate,");
			buffer.append("(select  b.clinic_diagicdname from t_emr_base b where   I.Inpatient_No  = b.INPATIENT_NO) clinic ");
			buffer.append("FROM t_inpatient_shiftdata H,(");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select I"+i+".MEDICALRECORD_ID,I"+i+".PATIENT_NAME,I"+i+".REPORT_SEX,I"+i+".PACT_CODE,I"+i+".IN_DATE,I"+i+".Inpatient_No ");
				buffer.append("from " +tnL.get(i) +" I"+i+" " );
			}
			buffer.append(") I ");
			buffer.append("WHERE H.SHIFT_TYPE = 'RO' ");
			buffer.append("AND H.Createtime >= TO_DATE('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND H.Createtime < TO_DATE('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append(" ");
			if(StringUtils.isNotBlank(dept)){
				buffer.append("AND H.OLD_DATA_CODE in('"+dept.replace(",", "','")+"') ");
			}else{
				buffer.append("AND H.OLD_DATA_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
			if(StringUtils.isNotBlank(page)){
				buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO and rownum<="+page+"*"+rows+" ");
			}else{
				buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO ");
			}
			buffer.append("ORDER BY  H.Createtime ");
			if(StringUtils.isNotBlank(page)){
			buffer.append(" ) AL where AL.rn>=("+page+"-1)*"+rows+" ");
			}
			Map<String,String> map=new HashMap<String,String>();
			List<GetOrOutPatient> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<GetOrOutPatient>() {
				@Override
				public GetOrOutPatient mapRow(ResultSet rs, int rownum)
						throws SQLException {
					GetOrOutPatient vo=new GetOrOutPatient();
					vo.setPatientno(rs.getString("patient"));	
					vo.setName(rs.getString("name"));
					vo.setSex(rs.getString("sex"));
					vo.setBeforeDept(rs.getString("beforeDept"));
					vo.setPact(rs.getString("pact"));
					vo.setClinic(rs.getString("clinic"));
					vo.setBeforeBedNo(rs.getString("beforeBedNo"));
					vo.setAfterDept(rs.getString("afterDept"));
					vo.setAfterBedNo(rs.getString("afterBedNo"));
					vo.setInDate(rs.getTimestamp("inDate"));
					vo.setTurnDate(rs.getTimestamp("turnDate"));
					return vo;
				}
			});
			if( list != null && list.size() > 0 ){
				return list;
			}
		}
		return new ArrayList<GetOrOutPatient>();
	}

	@Override
	public int queryinPatientTotal(List<String> tnL, String startTime,
			String endTime, String dept, String menuAlias) throws Exception {
		if (tnL == null || tnL.size() < 0) {
			return 0;
		}
		StringBuffer sb = new StringBuffer(500);
		sb.append(" select count(1) as age from( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append("select t").append(i).append(".Inpatient_No ");
			sb.append(" from ");
			sb.append(tnL.get(i)).append("  t" + i).append(" where  ");
			sb.append(" t").append(i).append(".IN_DATE>=to_date('" + startTime+ "','yyyy-MM-dd')");
			sb.append(" and t").append(i).append(".IN_DATE<=to_date('" + endTime+ "','yyyy-MM-dd')");
		}
		sb.append(" ) t,t_inpatient_shiftdata H where H.SHIFT_TYPE='B' and H.Clinic_No=t.inpatient_no ");
 		if (StringUtils.isNotBlank(dept)) {
			sb.append(" and H.NEW_DATA_CODE in('"+dept.replace(",", "','")+"') ");
		}else{
			sb.append(" and H.NEW_DATA_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
		}
		SQLQuery sqlQuery=	super.getSession().createSQLQuery(sb.toString())
				.addScalar("age",Hibernate.INTEGER);
		List<InPatientVo> list =sqlQuery.setResultTransformer(Transformers.aliasToBean(InPatientVo.class)).list();
		if(list.size()>0){
			return list.get(0).getAge();
		}
		return 0;
	}

	@Override
	public int queryOutPatientMsg(List<String> tnL, String startTime,
			String endTime, String dept, String menuAlias) throws Exception {
		if(tnL.size()>0){
			StringBuffer buffer=new StringBuffer(1800);
			buffer.append("SELECT count(1) age ");
			buffer.append("FROM t_inpatient_shiftdata H,(");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select I"+i+".MEDICALRECORD_ID,I"+i+".PATIENT_NAME,I"+i+".REPORT_SEX,I"+i+".REPORT_AGE,");
				buffer.append("I"+i+".BED_NAME,I"+i+".CHARGE_DOC_NAME,I"+i+".DUTY_NURSE_NAME,I"+i+".IN_DATE,out_date,I"+i+".ZG,I"+i+".PACT_CODE,I"+i+".INPATIENT_NO,I"+i+".DEPT_CODE ");
				buffer.append("from "+tnL.get(i)+" I"+i+" ");
			}
			buffer.append(") I ");
			buffer.append("WHERE H.SHIFT_TYPE IN ('O') ");
			buffer.append("AND I.OUT_DATE >= TO_DATE('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND I.OUT_DATE < TO_DATE('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			if(StringUtils.isNotBlank(dept)){//如果查询科室不为空  走选中科室
				buffer.append("AND I.DEPT_CODE in('"+dept.replace(",", "','")+"') ");
			}else{//如果为空 走授权科室
				buffer.append("AND I.DEPT_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
			buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO ");
			Map<String,String> map=new HashMap<String,String>();
			List<OutPatientVo> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<OutPatientVo>() {
				@Override
				public OutPatientVo mapRow(ResultSet rs, int rownum)
						throws SQLException {
					OutPatientVo vo=new OutPatientVo();
					vo.setAge(rs.getInt("age"));
					return vo;
				}
			});
			if( list != null && list.size() > 0 ){
				return list.get(0).getAge();
			}
		}
		return 0;
	}

	@Override
	public int queryGetInPatientMsg(List<String> tnL, String startTime,
			String endTime, String dept, String menuAlias) {
		if(tnL.size()>0){
			StringBuffer buffer=new StringBuffer(800);
			buffer.append("SELECT to_char(nvl(count(1),0)) as patient ");
			buffer.append("FROM t_inpatient_shiftdata H,(");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select I"+i+".MEDICALRECORD_ID,I"+i+".PATIENT_NAME,I"+i+".REPORT_SEX,I"+i+".PACT_CODE,");
				buffer.append("I"+i+".IN_DATE,I"+i+".Inpatient_No ");
				buffer.append("from "+tnL.get(i)+" I"+i+" ");
			}
			buffer.append(") I ");
			buffer.append("WHERE H.SHIFT_TYPE = 'RO' ");
			buffer.append("AND H.Createtime >= TO_DATE('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND H.Createtime < TO_DATE('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO ");
			if(StringUtils.isNotBlank(dept)){//查询选中科室
				buffer.append("AND H.NEW_DATA_CODE in('"+dept.replace(",", "','")+"') ");
			}else{//查询授权科室
				buffer.append("AND H.NEW_DATA_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
			Map<String,String> map=new HashMap<String,String>();
			List<GetOrOutPatient> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<GetOrOutPatient>() {
				@Override
				public GetOrOutPatient mapRow(ResultSet rs, int rownum)
						throws SQLException {
					GetOrOutPatient vo=new GetOrOutPatient();
					vo.setPatientno(rs.getString("patient"));	
					return vo;
				}
			});
			if( list != null && list.size() > 0 ){
				return Integer.parseInt(list.get(0).getPatientno());
			}
		}
		return 0;
	}

	@Override
	public int queryGetOutPatientMsg(List<String> tnL, String startTime,
			String endTime, String dept, String menuAlias) {
		if(tnL.size()>0){
			StringBuffer buffer=new StringBuffer(800);
			buffer.append("SELECT to_char(nvl(count(1),0)) as patient ");
			buffer.append("FROM t_inpatient_shiftdata H,(");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select I"+i+".MEDICALRECORD_ID,I"+i+".PATIENT_NAME,I"+i+".REPORT_SEX,I"+i+".PACT_CODE,I"+i+".IN_DATE,I"+i+".Inpatient_No ");
				buffer.append("from " +tnL.get(i) +" I"+i+" " );
			}
			buffer.append(") I ");
			buffer.append("WHERE H.SHIFT_TYPE = 'RO' ");
			buffer.append("AND H.Createtime >= TO_DATE('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND H.Createtime < TO_DATE('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append(" ");
			if(StringUtils.isNotBlank(dept)){
				buffer.append("AND H.OLD_DATA_CODE in('"+dept.replace(",", "','")+"') ");
			}else{
				buffer.append("AND H.OLD_DATA_CODE in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") ");
			}
			buffer.append("AND H.CLINIC_NO = I.INPATIENT_NO ");
			
			Map<String,String> map=new HashMap<String,String>();
			List<GetOrOutPatient> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<GetOrOutPatient>() {
				@Override
				public GetOrOutPatient mapRow(ResultSet rs, int rownum)
						throws SQLException {
					GetOrOutPatient vo=new GetOrOutPatient();
					vo.setPatientno(rs.getString("patient"));	
					return vo;
				}
			});
			if( list != null && list.size() > 0 ){
				return Integer.parseInt(list.get(0).getPatientno());
			}
		}
		return 0;
	}

}
