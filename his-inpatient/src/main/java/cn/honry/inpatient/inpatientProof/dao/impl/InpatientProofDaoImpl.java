package cn.honry.inpatient.inpatientProof.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inpatient.inpatientProof.dao.InpatientProofDao;
import cn.honry.inpatient.inpatientProof.vo.PoorfBill;
import cn.honry.inpatient.inpatientProof.vo.proReg;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("inpatientProofDAO")
@SuppressWarnings({ "all" })
public class InpatientProofDaoImpl extends HibernateEntityDao<InpatientProof>
		implements InpatientProofDao {

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterDAO;//医院参数dao

	@Override
	public InpatientProof getProof(String id) {

		String hql = "from InpatientProof i WHERE i.idcardNo= ?";
		List<InpatientProof> prooflist = super.find(hql, id);
		if (prooflist != null && prooflist.size() > 0) {
			return prooflist.get(0);
		}
		return new InpatientProof();
	}

	@Override
	public List<InpatientProof> findTree(boolean b) {
		String hql = "FROM InpatientProof i WHERE i.reportIssuingdoc='32311' ";

		List<InpatientProof> proofList = super.findByObjectProperty(hql, null);
		if (proofList != null && proofList.size() > 0) {
			return proofList;
		}
		return new ArrayList<InpatientProof>();
	}

	/**
	 * @Description： 根据挂号记录ID查询患者信息
	 * @Author：tcj
	 * @CreateDate：2015-12-3
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public RegistrationNow queryMedicalrecordId(String no){
		String sql = "select r.PATIENT_NAME as patientName,r.ADDRESS as address,r.PATIENT_IDENNO as patientIdenno,"
				+ "r.PATIENT_BIRTHDAY as patientBirthday,r.CLINIC_CODE as clinicCode,r.DOCT_CODE as doctCode,"
				+ "r.BACKNUMBER_REASON as backnumberReason,r.PATIENT_SEX as patientSex,r.PACT_CODE as pactCode,"
				+ "r.CARD_TYPE as cardType,r.MEDICALRECORDID as midicalrecordId from T_REGISTER_MAIN_NOW r "
				+ "where r.DEL_FLG=0 and r.CLINIC_CODE= :clinicCode";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("clinicCode", no);
		List<RegistrationNow> infoList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<RegistrationNow>() {
			@Override
			public RegistrationNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegistrationNow registrationNow = new RegistrationNow();
				registrationNow.setPatientName(rs.getString("patientName"));
				registrationNow.setAddress(rs.getString("address"));
				registrationNow.setPatientIdenno(rs.getString("patientIdenno"));
				registrationNow.setPatientBirthday(rs.getDate("patientBirthday"));
				registrationNow.setClinicCode(rs.getString("clinicCode"));
				registrationNow.setDoctCode(rs.getString("doctCode"));
				registrationNow.setBacknumberReason(rs.getString("backnumberReason"));
				registrationNow.setPatientSex(rs.getObject("patientSex")==null?null:rs.getInt("patientSex"));
				registrationNow.setPactCode(rs.getString("pactCode"));
				registrationNow.setCardType(rs.getString("cardType"));
				registrationNow.setMidicalrecordId(rs.getString("midicalrecordId"));
				return registrationNow;
			}
		});
		if (infoList!=null && infoList.size() > 0) {
			
			//查询患者的门诊诊断
			String hql1=" from OutpatientMedicalrecord where del_flg=0 and stop_flg=0 and  clinicCode=? ";
			List<OutpatientMedicalrecord> opml=super.find(hql1, no);
			
			if(opml!=null && opml.size()>0){
				
				//挂号表中的退号原因字段，暂存门诊诊断（门诊号唯一、一一对应）
				infoList.get(0).setBacknumberReason(opml.get(0).getDiagnose1());
			}
			return infoList.get(0);
		}
		return new RegistrationNow();
	}
	
	/**
	 * @Description： 根据病历号查询患者信息和挂号信息
	 * @Author：tcj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@Override
	public List<proReg> queryInfoListHis(String medicalrecordId, Date now) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		//查询系统参数(挂号有效期)
		String infoTime=parameterDAO.getParameterByCode("infoTime");
		int days=1;
		days = Integer.valueOf(infoTime);
		Date date = new Date();
		Date date2 = DateUtils.addDay(date, -days);
		String sql = "select r.id as id,r.YNBOOK as type,r.CLINIC_CODE as no,r.MEDICALRECORDID as medicalrecordId,"
				+ "r.DEPT_CODE as dept,r.DOCT_CODE as expxrt,"
				+ "r.REG_DATE as rdate,r.PATIENT_NAME as patientName,r.PATIENT_SEX as patientSex "
				+ "from "
				+ " T_REGISTER_MAIN_NOW r "
				+ "where r.VALID_FLAG =1 and  r.del_flg = 0  and r.stop_flg = 0  and r.TRANS_TYPE= 1 "
				+ "and (r.CARD_ID =:medicalrecordId   or "
				+ "r.MEDICALRECORDID =:medicalrecordId or r.CLINIC_CODE =:medicalrecordId ) "
				+"   and r.in_state =0 "
				+ "and r.REG_DATE >= to_date(:first,'yyyy-MM-dd HH24:mi:ss')"
				+ "and r.REG_DATE <= to_date(:end,'yyyy-MM-dd HH24:mi:ss')"
				+ "  order by r.reg_date desc ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId",medicalrecordId);
		paraMap.put("first",dateFormater.format(date2)+" 00:00:00");
		paraMap.put("end",dateFormater.format(date)+" 23:59:59");
		List<proReg> proregList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<proReg>() {

			@Override
			public proReg mapRow(ResultSet rs, int rowNum) throws SQLException {
				proReg vo = new proReg();
				vo.setId(rs.getString("id"));
				vo.setType(rs.getString("type"));
				vo.setNo(rs.getString("no"));
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setDept(rs.getString("dept"));
				vo.setExpxrt(rs.getString("expxrt"));
				vo.setRdate(rs.getTimestamp("rdate"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setPatientSex(rs.getObject("patientSex")==null?null:rs.getInt("patientSex"));
				return vo;
			}});
		if (proregList != null && proregList.size() > 0) {
			for (int i = 0; i < proregList.size(); i++) {
				//查询挂号的患者是否收费项目
				String sql1 = "select outpatient0_.pay_flag as payFlag from T_OUTPATIENT_FEEDETAIL_NOW outpatient0_ "
						+ "where outpatient0_.DEL_FLG = 0 and outpatient0_.STOP_FLG = 0 and outpatient0_.CLINIC_CODE = :clinicCode";
				Map<String, Object> paraMap1 = new HashMap<String, Object>();
				paraMap1.put("clinicCode",proregList.get(i).getNo());
				List<OutpatientFeedetailNow> outfeedlist =  namedParameterJdbcTemplate.query(sql1,paraMap1,new RowMapper<OutpatientFeedetailNow>() {

					@Override
					public OutpatientFeedetailNow mapRow(ResultSet rs, int rowNum) throws SQLException {
						OutpatientFeedetailNow vo = new OutpatientFeedetailNow();
						vo.setPayFlag(rs.getObject("payFlag")==null?null:rs.getInt("payFlag"));
						return vo;
					}});
				for (int j = 0; j < outfeedlist.size(); j++) {
					if (outfeedlist.get(j).getPayFlag() != 1) {
						proregList.get(i).setState(2);//收费状态  1 表示没有未收费项目  2 表示有未收费项目
					}
				}
			}
			return proregList;
		}
		return new ArrayList<proReg>();
	}

	/**
	 * @Description： 查询全部住院证明
	 * @Author：zhenglin
	 * @CreateDate：2015-12-17
	 * @ModifyRmk：
	 * @version 1.0
	 */
	public List<InpatientProof> allProofInfo() {
		String hql = "from InpatientProof i ";
		List<InpatientProof> proof = super.find(hql, null);
		if (proof != null && proof.size() > 0) {
			return proof;
		}
		return new ArrayList<InpatientProof>();
	}

	@Override
	public List<RegistrationNow> queryDateInfo(String num) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		//查询系统参数(挂号有效期)
		String infoTime=parameterDAO.getParameterByCode("infoTime");
		int days=1;
		if("".equals(infoTime)){
			 days=7;
		}else{
			days=Integer.valueOf(infoTime);
		}
		Date date = new Date();
		Date date2 = DateUtils.addDay(date,-days);
		String sql = " select r.ID as id,r.MEDICALRECORDID as midicalrecordId,r.CLINIC_CODE as clinicCode "
				+ "from "
				+ HisParameters.HISPARSCHEMAHISUSER
				+ "T_REGISTER_MAIN_NOW r "
				+ "left join "
				+ HisParameters.HISPARSCHEMAHISUSER
				+ " t_patient p  "
				+ "on p.medicalrecord_id=r.MEDICALRECORDID  and p.del_flg=0 and p.stop_flg=0  "
				+ "where   r.TRANS_TYPE= 1 and ( r.CARD_ID like :num or p.PATIENT_CERTIFICATESNO like :num "
				+ "or  r.MEDICALRECORDID like :num or r.CLINIC_CODE like :num and r.del_flg=0  and r.stop_flg=0  "
				+ " and trunc(r.REG_DATE,'dd')  between to_date(:first,'yyyy-MM-dd HH24:mi:ss') and to_date(:end,'yyyy-MM-dd HH24:mi:ss')";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("id").addScalar("midicalrecordId")
				.addScalar("clinicCode")
				.setParameter("num", "%"+num+"%")
				.setParameter("first", dateFormater.format(date2)).setParameter("end", dateFormater.format(date));
		List<RegistrationNow> plist = queryObject.setResultTransformer(Transformers.aliasToBean(RegistrationNow.class)).list();
		if (plist != null && plist.size() > 0) {
			return plist;
		}
		return new ArrayList<RegistrationNow>();
	}

	/**
	 * 根据病历号查询患者当日的开证记录
	 * 
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 */
	@Override
	public List<InpatientProof> queryProofDate(String no, Date now) {
		String hql = "from InpatientProof where certificatesNo = ? and reportIssuingdate = ?";
		List<InpatientProof> ipf = super.find(hql, no, now);
		if (ipf != null && ipf.size() > 0) {
			return ipf;
		}
		return new ArrayList<InpatientProof>();
	}

	@Override
	public String queryInpatientInfo(String zjhm) {
		String result = "";
		String sql1 ="select t.INPATIENT_ID as id from T_INPATIENT_INFO_NOW t where t.IN_STATE in ('R','I') and t.MEDICALRECORD_ID =:medicalrecordId";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", zjhm);
		List<InpatientInfoNow> inInfo =  namedParameterJdbcTemplate.query(sql1,paraMap,new RowMapper<InpatientInfoNow>() {

			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
				inpatientInfoNow.setId(rs.getString("id"));
				return inpatientInfoNow;
			}
			
		});
		if (inInfo != null && inInfo.size() > 0) {
			result = "yesRI";
			return result;
		}
		result = "none";
		return result;
	}

	@Override
	public List<Patient> queryByNumber(String num) {
		String sql = " select p.patinent_id as id,p.medicalrecord_id  as medicalrecordId "
				+ "from "
				+ HisParameters.HISPARSCHEMAHISUSER
				+ " t_patient p "
				+ "left join "
				+ HisParameters.HISPARSCHEMAHISUSER
				+ " t_register_info r "
				+ "on p.medicalrecord_id=r.medicalrecord_id  "
				+ "where ( r.IDCARD_ID like :num  or p.PATIENT_CERTIFICATESNO like :num "
				+ "or  r.medicalrecord_id like :num or r.REGISTER_NO like :num or p.medicalrecord_id like :num ) ";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql);
		queryObject.addScalar("id").addScalar("medicalrecordId")
		.setParameter("num", "%"+num+"%");
		List<Patient> plist = queryObject.setResultTransformer(
				Transformers.aliasToBean(Patient.class)).list();
		if (plist != null && plist.size() > 0) {
			return plist;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public List<InpatientProof> searchProofByResinfo(String medicalrecordId,String regisNo) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		//查询系统参数(挂号有效期)
		String infoTime=parameterDAO.getParameterByCode("infoTime");
		int days=1;
		days = Integer.valueOf(infoTime);
		Date date = new Date();
		Date date2 = DateUtils.addDay(date, -days);
		String hql1 = "select * from T_INPATIENT_PROOF p where p.IDCARD_NO= :regisNo and p.MEDICALRECORD_ID = :medicalrecordId"
				+ " and  (p.REPORT_ISSUINGDATE between to_date(:first,'yyyy-MM-dd HH24:mi:ss') and to_date(:end,'yyyy-MM-dd HH24:mi:ss'))";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("regisNo", regisNo);
		paraMap.put("medicalrecordId", medicalrecordId);
		paraMap.put("first", (dateFormater.format(date2))+" 00:00:00");
		paraMap.put("end", (dateFormater.format(date))+" 23:59:59");
		List<InpatientProof> ipl =  namedParameterJdbcTemplate.query(hql1,paraMap,new RowMapper<InpatientProof>() {
			@Override
			public InpatientProof mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientProof  proof = new InpatientProof();
				proof.setCertificatesNo(rs.getString("CERTIFICATES_NO"));
				proof.setCertificatesType(rs.getString("CERTIFICATES_TYPE"));
				proof.setContractUnit(rs.getString("CONTRACT_UNIT"));
				proof.setCreateDept(rs.getString("CREATEDEPT"));
				proof.setCreateTime(rs.getTimestamp("CREATETIME"));
				proof.setCreateUser(rs.getString("CREATEUSER"));
				proof.setId(rs.getString("PROOF_ID"));
				proof.setIdcardNo(rs.getString("IDCARD_NO"));
				proof.setInpatientDaycount(rs.getObject("INPATIENT_DAYCOUNT")==null?null:rs.getInt("INPATIENT_DAYCOUNT"));
				proof.setMedicalrecordId(rs.getString("MEDICALRECORD_ID"));
				proof.setPatientName(rs.getString("PATIENT_NAME"));
				proof.setReportAddress(rs.getString("REPORT_ADDRESS"));
				
				proof.setReportAge(rs.getObject("REPORT_AGE")==null?null:rs.getInt("REPORT_AGE"));
				
				proof.setReportAgeunit(rs.getString("REPORT_AGEUNIT"));
				proof.setReportBathflag(rs.getObject("REPORT_BATHFLAG")==null?null:rs.getInt("REPORT_BATHFLAG"));
				proof.setReportBedward(rs.getString("REPORT_BEDWARD"));
				proof.setReportBirthday(rs.getDate("REPORT_BIRTHDAY"));
				proof.setReportBloodqty(rs.getObject("REPORT_BLOODQTY")==null?null:rs.getInt("REPORT_BLOODQTY"));
				proof.setReportClinostatism(rs.getObject("REPORT_CLINOSTATISM")==null?null:rs.getInt("REPORT_CLINOSTATISM"));
				proof.setReportDept(rs.getString("REPORT_DEPT"));
				proof.setReportDiagnose(rs.getString("REPORT_DIAGNOSE"));
				proof.setReportDiet(rs.getObject("REPORT_DIET")==null?null:rs.getInt("REPORT_DIET"));
				proof.setReportDrugflag(rs.getObject("REPORT_DRUGFLAG")==null?null:rs.getInt("REPORT_DRUGFLAG"));
				proof.setReportHaircut(rs.getObject("REPORT_HAIRCUT")==null?null:rs.getInt("REPORT_HAIRCUT"));
				proof.setReportIntext(rs.getString("REPORT_INTEXT"));
				proof.setReportIssuingdate(rs.getTimestamp("REPORT_ISSUINGDATE"));
				proof.setReportIssuingdoc(rs.getString("REPORT_ISSUINGDOC"));
				proof.setReportOpstype(rs.getObject("REPORT_OPSTYPE")==null?null:rs.getInt("REPORT_OPSTYPE"));
				proof.setReportRemark(rs.getString("REPORT_REMARK"));
				proof.setReportSex(rs.getString("REPORT_SEX"));
				proof.setReportShillflag(rs.getObject("REPORT_SHILLFLAG")==null?null:rs.getInt("REPORT_SHILLFLAG"));
				proof.setReportSituation(rs.getObject("REPORT_SITUATION")==null?null:rs.getInt("REPORT_SITUATION"));
				proof.setReportStatus(rs.getObject("REPORT_STATUS")==null?null:rs.getInt("REPORT_STATUS"));
				proof.setReportXflag(rs.getObject("REPORT_XFLAG")==null?null:rs.getInt("REPORT_XFLAG"));
				return proof;
			}
			
		});
		if (ipl != null && ipl.size() > 0) {
			return ipl;
		}
		return new ArrayList<InpatientProof>();
	}

	@Override
	public List<SysDepartment> queryInHosDept(String param) {
		String sql = "select t.DEPT_CODE as deptCode , t.DEPT_NAME as deptName from T_DEPARTMENT t  where t.DEL_FLG=0 and t.STOP_FLG=0 and t.DEPT_TYPE='I'";
		if(StringUtils.isNotBlank(param)){
			sql+=" and (t.DEPT_CODE like :param or t.DEPT_NAME like :param or t.DEPT_BREV like :param  "
					+ "or t.DEPT_ENAME like :param "
					+ "or upper(t.DEPT_PINYIN) like upper(:param) "
					+ "or upper(t.DEPT_WB) like upper(:param) "
					+ "or upper(t.DEPT_INPUTCODE) like upper(:param))";
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(param)){
			paraMap.put("param", "%"+param+"%");
		}
		List<SysDepartment> sysdl =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<SysDepartment>() {

			@Override
			public SysDepartment mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysDepartment sysDepartment = new SysDepartment();
				sysDepartment.setDeptCode(rs.getString("deptCode"));
				sysDepartment.setDeptName(rs.getString("deptName"));
				return sysDepartment;
			}
			
		});

		if (sysdl != null && sysdl.size() > 0) {
			return sysdl;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public String queryShoufeiState(String no) {
		// 用于判断是否有未收费的标志字段 1 表示没有未收费，2表示有未收费项目
		String state = "1";
		String sql = "select o.PAY_FLAG as payFlag from T_OUTPATIENT_FEEDETAIL_NOW o where o.DEL_FLG=0 and o.STOP_FLG=0 and o.CLINIC_CODE =:clinicCode ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("clinicCode", no);
		List<OutpatientFeedetailNow> outfeedlist =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<OutpatientFeedetailNow>() {
			@Override
			public OutpatientFeedetailNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				OutpatientFeedetailNow outpatientFeedetailNow = new OutpatientFeedetailNow();
				outpatientFeedetailNow.setPayFlag(rs.getObject("payFlag")==null?null:rs.getInt("payFlag"));
				return outpatientFeedetailNow;
			}
			
		});
		
		if (outfeedlist != null && outfeedlist.size() > 0) {
			for (int i = 0; i < outfeedlist.size(); i++) {
				if (outfeedlist.get(i).getPayFlag() != null && outfeedlist.get(i).getPayFlag().equals(0)) {
					state = "2";
					return state;
				}
			}
		}
		return state;
	}

	@Override
	public String queryPatientStateBymz(String medicalrecordId) {
		String sql="select i.in_state as inState  from "
				+ " t_inpatient_info_now i where i.medicalrecord_id=:medicalrecordId order by i.in_date desc";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", medicalrecordId);
		List<InpatientInfoNow> plist =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientInfoNow>() {
			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
				inpatientInfoNow.setInState(rs.getString("inState"));
				return inpatientInfoNow;
			}
			
		});

		if(plist!=null&&plist.size()>0){
			if("I".equals(plist.get(0).getInState())){
				return "I";
			}else if("R".equals(plist.get(0).getInState())){
				return "R";
			}else if("B".equals(plist.get(0).getInState())){
				return "B";
			}else if("O".equals(plist.get(0).getInState())){
				return "O";
			}else if("P".equals(plist.get(0).getInState())){
				return "P";
			}else if("N".equals(plist.get(0).getInState())){
				return "N";
			}
		}
		return "none";
	}
	/**
	 * 查询病区
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	@Override
	public List<SysDepartment> querybingqu(String id,String param) {
		String sql ="select t.DEPT_CODE as deptCode,t.DEPT_NAME as deptName from T_DEPARTMENT t where t.DEL_FLG=0 and t.STOP_FLG=0 and t.DEPT_TYPE='N' ";
		if(StringUtils.isNotBlank(param)){
			sql+=" and (t.DEPT_CODE like :param or t.DEPT_NAME like :param or t.DEPT_BREV like :param  "
					+ "or t.DEPT_ENAME like :param or t.DEPT_PINYIN like :param or t.DEPT_WB like :param  "
					+ "or t.DEPT_INPUTCODE like :param)";
		}
		
		
		List<SysDepartment> deptlist=new ArrayList<SysDepartment>();
		List<SysDepartment> binqulist = new ArrayList<SysDepartment>();
		if (StringUtils.isNotBlank(id)) {
			String sql1="select d.DEPT_ID as id from T_DEPARTMENT d  where d.del_flg=0 and d.stop_flg=0 and d.DEPT_CODE =:id";
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("id", id);
		    deptlist =  namedParameterJdbcTemplate.query(sql1,paraMap,new RowMapper<SysDepartment>(){

				@Override
				public SysDepartment mapRow(ResultSet rs, int rowNum) throws SQLException {
					SysDepartment SysDepartment = new SysDepartment();
					SysDepartment.setId(rs.getString("id"));
					return SysDepartment;
				}
				
			});
		    if(deptlist.size()>0 && deptlist !=null){
		    	sql += " and t.DEPT_ID in  ( select dc2.DEPT_ID from T_DEPARTMENT_CONTACT dc2 where dc2.ID in (select dc1.PARDEPT_ID  from T_DEPARTMENT_CONTACT dc1  where dc1.DEPT_ID= :deptId"
		    			+ " and dc1.REFERENCE_TYPE='03' ))";
		    }
		}
		Map<String, Object> paraMap1 = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(param)){
			paraMap1.put("param", "%"+param+"%");
		}
		if(deptlist != null && deptlist.size()>0){
			paraMap1.put("deptId", deptlist.get(0).getId());
		}
		 binqulist =  namedParameterJdbcTemplate.query(sql,paraMap1,new RowMapper<SysDepartment>() {

			@Override
			public SysDepartment mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysDepartment sysDepartment = new SysDepartment();
				sysDepartment.setDeptCode(rs.getString("deptCode"));
				sysDepartment.setDeptName(rs.getString("deptName"));
				return sysDepartment;
			}
			
		});
		if (binqulist != null && binqulist.size() > 0) {
			return binqulist;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<SysDepartment> queryKeshi(String id, String param) {
		String hql = "from SysDepartment where del_flg=0 and stop_flg=0 and deptType='I' ";
		if(StringUtils.isNotBlank(param)){
			hql+=" and (deptCode like :param or deptName like :param or deptBrev like :param  "
					+ "or deptEname like :param or deptPinyin like :param or deptWb like :param  "
					+ "or deptInputcode like :param)";
		}
		List<SysDepartment> deptlist=new ArrayList<SysDepartment>();
		List<SysDepartment> binqulist = new ArrayList<SysDepartment>();
		if (StringUtils.isNotBlank(id)) {
			String sql="select d.DEPT_ID as id from T_DEPARTMENT d  where d.del_flg=0 and d.stop_flg=0 and d.DEPT_CODE =:id";
			Query queryObject=this.getSession().createSQLQuery(sql).addScalar("id").setParameter("id", id);
		    deptlist=queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		    if(deptlist.size()>0 && deptlist !=null){
		    	hql += " and id in  ( select dc2.deptId from DepartmentContact dc2 where dc2.pardeptId in (select dc1.id  from DepartmentContact dc1  where dc1.deptId= :deptId"
						+ " and dc1.referenceType='03' ))";
		    }
		}
		Query querySql = this.getSession().createQuery(hql);
		if(StringUtils.isNotBlank(param)){
			querySql.setParameter("param", "%"+param+"%");
		}
		if(deptlist!= null && deptlist.size()> 0){
			querySql.setParameter("deptId", deptlist.get(0).getId());
		}
		binqulist = querySql.list();
		if (binqulist!= null && binqulist.size()> 0) {
			return binqulist;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public void savePreregister(final InpatientProof inpatientProof) {
		  int t = jdbcTemplate.update("insert into T_INPATIENT_PROOF values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, (StringUtils.isNotBlank(inpatientProof.getCertificatesNo())?inpatientProof.getCertificatesNo():""));
				ps.setString(2, (StringUtils.isNotBlank(inpatientProof.getCertificatesType())?inpatientProof.getCertificatesType():""));
				ps.setString(3, (StringUtils.isNotBlank(inpatientProof.getContractUnit())?inpatientProof.getContractUnit():""));
				ps.setString(4, (StringUtils.isNotBlank(inpatientProof.getCreateDept())?inpatientProof.getCreateDept():""));
				ps.setString(5, (StringUtils.isNotBlank(inpatientProof.getCreateUser())?inpatientProof.getCreateUser():""));
				ps.setString(6, (StringUtils.isNotBlank(inpatientProof.getDeleteUser())?inpatientProof.getDeleteUser():""));
				ps.setString(7, inpatientProof.getId());
				ps.setString(8, (StringUtils.isNotBlank(inpatientProof.getIdcardNo())?inpatientProof.getIdcardNo():""));
				ps.setString(9, (StringUtils.isNotBlank(inpatientProof.getMedicalrecordId())?inpatientProof.getMedicalrecordId():""));
				ps.setString(10, (StringUtils.isNotBlank(inpatientProof.getPatientName())?inpatientProof.getPatientName():""));
				ps.setString(11, (StringUtils.isNotBlank(inpatientProof.getReportAddress())?inpatientProof.getReportAddress():""));
				ps.setString(12, (StringUtils.isNotBlank(inpatientProof.getReportAgeunit())?inpatientProof.getReportAgeunit():""));
				ps.setString(13, (StringUtils.isNotBlank(inpatientProof.getReportBedward())?inpatientProof.getReportBedward():""));
				ps.setString(14, (StringUtils.isNotBlank(inpatientProof.getReportDept())?inpatientProof.getReportDept():""));
				ps.setString(15, (StringUtils.isNotBlank(inpatientProof.getReportDiagnose())?inpatientProof.getReportDiagnose():""));
				ps.setString(16, (StringUtils.isNotBlank(inpatientProof.getReportIntext())?inpatientProof.getReportIntext():""));
				ps.setString(17, (StringUtils.isNotBlank(inpatientProof.getReportIssuingdoc())?inpatientProof.getReportIssuingdoc():""));
				ps.setString(18, (StringUtils.isNotBlank(inpatientProof.getReportRemark())?inpatientProof.getReportRemark():""));
				ps.setString(19, (StringUtils.isNotBlank(inpatientProof.getReportSex())?inpatientProof.getReportSex():""));
				ps.setString(20, (StringUtils.isNotBlank(inpatientProof.getUpdateUser())?inpatientProof.getUpdateUser():""));
				ps.setDate(21,   new  java.sql.Date(inpatientProof.getCreateTime().getTime()));
				ps.setInt(22, inpatientProof.getDel_flg());
				ps.setInt(23, inpatientProof.getInpatientDaycount());
				ps.setInt(24, inpatientProof.getReportAge());
				ps.setInt(25, inpatientProof.getReportBathflag());
				ps.setDate(26,new java.sql.Date(inpatientProof.getReportBirthday().getTime()));
				ps.setInt(27, inpatientProof.getReportBloodqty());
				ps.setInt(28, inpatientProof.getReportClinostatism());
				ps.setInt(29, inpatientProof.getReportDiet());
				ps.setInt(30, inpatientProof.getReportDrugflag());
				ps.setInt(31, inpatientProof.getReportHaircut());
				ps.setDate(32,new java.sql.Date(inpatientProof.getReportIssuingdate().getTime()));
				ps.setInt(33, inpatientProof.getReportOpstype());
				ps.setInt(34, inpatientProof.getReportShillflag());
				ps.setInt(35, inpatientProof.getReportSituation());
				ps.setInt(36, inpatientProof.getReportStatus());
				ps.setInt(37, inpatientProof.getReportXflag());
				ps.setInt(38, inpatientProof.getStop_flg());
				ps.setDate(39,new java.sql.Date(inpatientProof.getUpdateTime().getTime()));			}
			  
		  });		
	}

	@Override
	public int updatePreregister(final InpatientProof inpatientProof) {
		   String sql = "update T_INPATIENT_PROOF set PROOF_ID = ? ,IDCARD_NO = ?,CONTRACT_UNIT= ?,PATIENT_NAME=?,"
		   		+ " CERTIFICATES_TYPE =?,CERTIFICATES_NO =? , REPORT_SEX=?,REPORT_BIRTHDAY=?,"
		   		+ "REPORT_AGE=?,REPORT_AGEUNIT=?,REPORT_DEPT=?,REPORT_BEDWARD =?,"
		   		+ "REPORT_DIAGNOSE=?,REPORT_ADDRESS =?,REPORT_INTEXT=?,REPORT_SITUATION=?,"
		   		+ "REPORT_STATUS=?,REPORT_CLINOSTATISM=?,REPORT_DIET=?,REPORT_SHILLFLAG =?,"
		   		+ "REPORT_BATHFLAG=?,REPORT_HAIRCUT =?,REPORT_ISSUINGDATE=?,REPORT_ISSUINGDOC=?,"
		   		+ "INPATIENT_DAYCOUNT=?,REPORT_DRUGFLAG=?,REPORT_OPSTYPE=?,REPORT_BLOODQTY=?"
		   		+ "REPORT_XFLAG=?,REPORT_REMARK=?,CREATEUSER=?,CREATEDEPT=?,"
		   		+ "CREATETIME=?,UPDATEUSER=?,UPDATETIME=?,DELETEUSER=?,"
		   		+ "DELETETIME=?,DEL_FLG=?,STOP_FLG=?,MEDICALRECORD_ID=?"
		   		+ " where PROOF_ID = ?";  
		   Object args[] = new Object[]{inpatientProof.getId(),inpatientProof.getIdcardNo(),inpatientProof.getContractUnit(),inpatientProof.getPatientName(),
				   inpatientProof.getCertificatesType(),inpatientProof.getCertificatesNo(),inpatientProof.getReportSex(),inpatientProof.getReportBirthday(),
				   inpatientProof.getReportAge(),inpatientProof.getReportAgeunit(),inpatientProof.getReportDept(),inpatientProof.getReportBedward(),
				   inpatientProof.getReportDiagnose(),inpatientProof.getReportAddress(),inpatientProof.getReportIntext(),inpatientProof.getReportSituation(),
				   inpatientProof.getReportStatus(),inpatientProof.getReportClinostatism(),inpatientProof.getReportDiet(),inpatientProof.getReportShillflag(),
				   inpatientProof.getReportBathflag(),inpatientProof.getReportHaircut(),(java.sql.Date) new Date(inpatientProof.getReportIssuingdate().getTime()),inpatientProof.getReportIssuingdoc(),
				   inpatientProof.getInpatientDaycount(),inpatientProof.getReportDrugflag(),inpatientProof.getReportOpstype(),inpatientProof.getReportBloodqty(),
				   inpatientProof.getReportXflag(),inpatientProof.getReportRemark(),inpatientProof.getCreateUser(),inpatientProof.getCreateDept(),
				   (java.sql.Date) new Date(inpatientProof.getCreateTime().getTime()),inpatientProof.getUpdateUser(),(java.sql.Date) new Date(inpatientProof.getUpdateTime().getTime()),inpatientProof.getDeleteUser(),
				   (java.sql.Date) new Date(inpatientProof.getDeleteTime().getTime()),inpatientProof.getDel_flg(),inpatientProof.getStop_flg(),inpatientProof.getMedicalrecordId(),
				   inpatientProof.getId()};  
		   int t = jdbcTemplate.update(sql,args);  
		   return t;		
		
	}

	@Override
	public List<PoorfBill> queryProofByIds(String ids) {
		String sql="select (SELECT HOSPITAL_NAME FROM T_HOSPITAL WHERE HOSPITAL_ID = '1') as hName,(select "
				+ "e.employee_name from T_EMPLOYEE e where rownum<2 and e.employee_type ='1' and e.employee_jobno = t.report_issuingdoc and e.del_flg=0 and e.stop_flg=0 ) as PDoc,"
				+ "t.IDCARD_NO as idCardno,t.CERTIFICATES_NO as certificatesNo,t.PATIENT_NAME as pName,t.REPORT_AGEUNIT as nldw,"
				+ "CASE t.REPORT_SEX WHEN '1' THEN '男' WHEN '2' THEN '女' END  as rSex,(SELECT DEPT_NAME FROM T_DEPARTMENT WHERE dept_id = t.REPORT_DEPT) as rDept,"
				+ "t.REPORT_ADDRESS as rAddress,(SELECT CASE c.paykind_code WHEN '01' THEN '自费'  WHEN '02' THEN  '保险 '  WHEN '03' THEN  '公费在职'  WHEN '04' THEN  '公费退休' "
				+ " ELSE '公费干部' end FROM T_BUSINESS_CONTRACTUNIT c where c.unit_code = t.CONTRACT_UNIT) as contractUnit,"
				+ "t.REPORT_DIAGNOSE as rDiagnose,decode(t.REPORT_SITUATION,'1','√ ','2','','3','') as reportSituation1,"
				+ "decode(t.REPORT_SITUATION,'1','','2','√ ','3','') as reportSituation2,decode(t.REPORT_SITUATION,'1','','2','','3','√ ') as reportSituation3,"
				+ "decode(t.REPORT_STATUS,'1','√','2','','3','') as reportStatus1,decode(t.REPORT_STATUS,'1','','2','√ ','3','') as reportStatus2,"
				+ "decode(t.REPORT_STATUS,'1','','2','','3','√ ') as reportStatus3,to_char(t.REPORT_ISSUINGDATE, 'yyyy') as rIssuingdate1,"
				+ "to_char(t.REPORT_ISSUINGDATE, 'MM') as rIssuingdate2,to_char(t.REPORT_ISSUINGDATE, 'dd') as rIssuingdate3,(SELECT DEPT_ADDRESS "
				+ "FROM T_DEPARTMENT t1 WHERE t1.DEPT_ID = t.REPORT_DEPT) as deptAddress,t.MEDICALRECORD_ID as med "
				+ "from T_INPATIENT_PROOF t where t.proof_id = '"+ids+"'  and DEL_FLG=0";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("hName").addScalar("PDoc").addScalar("idCardno").addScalar("certificatesNo").addScalar("pName").addScalar("nldw").addScalar("rSex",Hibernate.CHARACTER).addScalar("rDept")
		.addScalar("rAddress").addScalar("contractUnit").addScalar("rDiagnose").addScalar("reportSituation1").addScalar("reportSituation2").addScalar("reportSituation3")
		.addScalar("reportStatus1").addScalar("reportStatus2").addScalar("reportStatus3").addScalar("rIssuingdate1").addScalar("rIssuingdate2").addScalar("rIssuingdate3")
		.addScalar("deptAddress").addScalar("med").setResultTransformer(Transformers.aliasToBean(PoorfBill.class));
		List<PoorfBill> list = query.list();
		return list;
	}
}
