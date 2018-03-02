package cn.honry.inpatient.prepayin.dao.impl;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VIdcardPatient;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.info.vo.InfoVo;
import cn.honry.inpatient.prepayin.dao.InpatientPrepayinDAO;
import cn.honry.inpatient.prepayin.vo.PrepayinVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("inpatientPrepayinqDAO")
@SuppressWarnings({"all"})
public class InpatientPrepayinDAOImpl extends HibernateEntityDao<InpatientPrepayin> implements InpatientPrepayinDAO{

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterDAO;//医院参数dao
	/**
	 * 获取记录条数
	 * @param entity 查询条件封装实体类
	 * @author  liudelin
	 * @date 2015-08-11
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getTotal(InpatientPrepayin entity) {
		String sql = "select COUNT(1) from T_INPATIENT_PREPAYIN t  where t.DEL_FLG=0 and t.STOP_FLG=0";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				sql= sql+" and  t.MEDICALRECORD_ID = :medicalrecordId";
			}
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				paraMap.put("medicalrecordId", entity.getMedicalrecordId());
			}
		}
		int c =  namedParameterJdbcTemplate.queryForObject(sql,paraMap, java.lang.Integer.class);
		return c;
	}
	/**
	 * 列表查询
	 * @param page 页码
	 * @param rows 显示列表数据
	 * @param entity 查询条件封装实体类
	 * @author  liudelin
	 * @date 2015-07-28
	 * @version 1.0
	 * @param string2 
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List query(String page, String rows, InpatientPrepayin entity) {
		String sql ="select * from( ";
		sql += "select t.*,ROWNUM n from T_INPATIENT_PREPAYIN t  where t.DEL_FLG=0 and t.STOP_FLG=0";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				sql= sql+" and  t.MEDICALRECORD_ID = :medicalrecordId";
			}
		}
		sql +="  and ROWNUM  <= (:page) * :row  )   where n > (:page -1) * :row";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", page);
		paraMap.put("row", rows);
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				paraMap.put("medicalrecordId", entity.getMedicalrecordId());
			}
		}
		List<InpatientPrepayin> inpatientPrepayinList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientPrepayin>() {

			@Override
			public InpatientPrepayin mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientPrepayin inpatientPrepayin = new InpatientPrepayin();
				inpatientPrepayin.setId(rs.getString("ID"));
				inpatientPrepayin.setName(rs.getString("NAME"));
				inpatientPrepayin.setMedicalrecordId(rs.getString("MEDICALRECORD_ID"));
				inpatientPrepayin.setBedId(rs.getString("BED_NO"));
				inpatientPrepayin.setPreDate(rs.getTimestamp("PRE_DATE"));
				inpatientPrepayin.setPredoctName(rs.getString("PREDOCT_NAME"));
				inpatientPrepayin.setDeptCodeName(rs.getString("DEPT_CODE_NAME"));
				inpatientPrepayin.setMcardNo(rs.getString("MCARD_NO"));
				inpatientPrepayin.setLinkmaName(rs.getString("LINKMA_NAME"));
				inpatientPrepayin.setLinkmaTel(rs.getString("LINKMAN_TEL"));
				inpatientPrepayin.setHome(rs.getString("HOME"));
				inpatientPrepayin.setHomeTel(rs.getString("HOME_TEL"));
				return inpatientPrepayin;
			}
			
		});
		return inpatientPrepayinList;
	}
	
	/**  
	 *  
	 * @Description： 查询条件拼接
	 * @Author：liudelin
	 * @CreateDate：2015-8-11 下午02:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String joint(InpatientPrepayin entity,String hql){
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				hql= hql+" and  medicalrecordId like '%"+entity.getMedicalrecordId()+"%'";
			}
		}
		return hql;
	}
	/**  
	 *  
	 * @Description：查询并赋值
	 * @Author：ldl
	 * @CreateDate：2015-8-11 下午16:29:35 
	 * @param：medicalrecordId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow queryPrepayinInpatient(String medicalrecordId) {
		String hql = "from InpatientInfoNow ip where inState='R' and  medicalrecordId= ?";
		List<InpatientInfoNow> inpatientInfoList = super.find(hql, medicalrecordId);
		if(inpatientInfoList!=null && inpatientInfoList.size()>0){
			return inpatientInfoList.get(0);
		}
		return new InpatientInfoNow();
	}
	/**  
	 *  
	 * @Description：查询患者信息
	 * @Author：ldl
	 * @CreateDate：2015-8-11 下午16:29:35 
	 * @param：medicalrecordId（病例号） 
	 * @ModifyRmk：  当查询住院表中没有数据时根据病历号查询 就诊卡表----患者信息表视图
	 * @version 1.0
	 *
	 */
	@Override
	public VIdcardPatient queryVIdcardPatient(String medicalrecordId) {
		String hql = "from VIdcardPatient where  medicalrecordId= ?";
		List<VIdcardPatient> vIdcardPatientList = super.find(hql, medicalrecordId);
		if(vIdcardPatientList!=null && vIdcardPatientList.size()>0){
			return vIdcardPatientList.get(0);
		}
		return new VIdcardPatient();
	}
	/**  
	 *  
	 * @Description：  保存
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void savePrepayin(InpatientPrepayin entity) {
		entity.setId(null);
		entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		//预约状态 预约
		entity.setPreState(0);
		//发生序号
		int happenNo=Integer.parseInt(this.getSequece("seq_inprepay_happenno"));// 根据sequence,获取applyNumber
		entity.setHappenNo(happenNo);
		entity.setStop_flg(0);
		entity.setDel_flg(0);
		super.save(entity);
		OperationUtils.getInstance().conserve(null,"住院预约","INSERT INTO","T_INPATIENT_PREPAYIN",OperationUtils.LOGACTIONINSERT);
	}
	/**  
	 *  
	 * @Description：  下拉诊断
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessIcd> queryPrepayinIcd() {
		String hql = " from  BusinessIcd where del_flg=0 and stop_flg=0";
		List<BusinessIcd> icdList = super.find(hql, null);
		if(icdList!=null && icdList.size()>0){
			return icdList;
		}
		return new ArrayList<BusinessIcd>();
	}
	/**  
	 *  
	 * @Description：  下拉病床
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessHospitalbed> queryPrepayinBed() {
		String hql = " from  BusinessHospitalbed where del_flg=0 and stop_flg=0";
		List<BusinessHospitalbed> hospitalbedList = super.find(hql, null);
		if(hospitalbedList!=null && hospitalbedList.size()>0){
			return hospitalbedList;
		}
		return new ArrayList<BusinessHospitalbed>();
	}
	/**  
	 *  
	 * @Description：  下拉医生
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryPrepayinPredoct() {
		String hql = " from  SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList!=null && employeeList.size()>0){
			return employeeList;
		}
		return new ArrayList<SysEmployee>();
	}
	
	/**  
	 *  
	 * @Description：  查询病人及登记信息
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<PrepayinVo> queryPrepayinVo(String mId,String no) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select C.IDCARD_NO as idcardNo,p.idcard_no as no,i.medicalrecord_id as medicalrecordId,"
				+ "p.report_dept as reportDept,i.patient_name as patientName,i.patient_sex as reportSex,");
		sb.append(" (select m.dept_name FROM T_DEPARTMENT  m where m.DEPT_CODE=p.report_dept ) as reportDeptName,");
		sb.append(" p.contract_unit as pactCode,i.patient_nativeplace as dist,i.patient_nation as nationCode,"
				+ "i.patient_city as patientCity,i.patient_birthday as reportBirthday,");
		sb.append(" i.patient_warriage as mari, p.certificates_no as certificatesNo, "
				+ "p.certificates_type as certificatesType,i.patient_occupation as profCode,"
				+ "i.patient_birthplace as birthArea,i.patient_nationality as counCode,");
		sb.append(" p.report_diagnose as reportDiagnose,i.patient_address as home,i.patient_phone as homeTel,"
				+ "i.patient_workunit as workName,i.patient_workphone as workTel,i.patient_linkphone as linkmanTel,");
		sb.append(" i.patient_linkman as linkmanName,i.patient_linkrelation as relaCode,"
				+ "i.patient_linkaddress as linkmanAddress,"
				+ "p.report_bedward as reportBedward,i.patient_handbook as mcardNo");
		sb.append(" FROM T_PATIENT_IDCARD c, T_PATIENT i,t_inpatient_proof p "
				+ "WHERE c.PATINENT_ID = i.PATINENT_ID and p.medicalrecord_id = i.medicalrecord_id and c.stop_flg=0 and c.del_flg=0 ");
		sb.append(" and i.del_flg=0 and i.stop_flg=0 and p.del_flg=0 and p.stop_flg=0 and i.medicalrecord_id = :mId");
		if(StringUtils.isNotBlank(no)){
			sb.append(" and p.idcard_no = :no");
		}
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("mId", mId);
		if(StringUtils.isNotBlank(no)){
			paraMap.put("no", no);
		}
		List<PrepayinVo> PrepayinVoList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<PrepayinVo>() {

			@Override
			public PrepayinVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PrepayinVo vo = new PrepayinVo();
				vo.setIdcardNo(rs.getString("idcardNo"));
				vo.setNo(rs.getString("no"));
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setReportDept(rs.getString("reportDept"));
				vo.setReportDeptName(rs.getString("reportDeptName"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setReportSex(rs.getInt("reportSex"));
				vo.setPactCode(rs.getString("pactCode"));
				vo.setDist(rs.getString("dist"));
				vo.setNationCode(rs.getString("nationCode"));
				vo.setHome(rs.getString("home"));
				vo.setReportBirthday(rs.getDate("reportBirthday"));
				vo.setMari(rs.getString("mari"));
				vo.setCertificatesNo(rs.getString("certificatesNo"));
				vo.setCertificatesType(rs.getString("certificatesType"));
				vo.setProfCode(rs.getString("profCode"));
				vo.setBirthArea(rs.getString("birthArea"));
				vo.setPatientCity(rs.getString("patientCity"));
				vo.setCounCode(rs.getString("counCode"));
				vo.setReportDiagnose(rs.getString("reportDiagnose"));
				vo.setHomeTel(rs.getString("homeTel"));
				vo.setWorkName(rs.getString("workName"));
				vo.setWorkTel(rs.getString("workTel"));
				vo.setLinkmanName(rs.getString("linkmanName"));
				vo.setRelaCode(rs.getString("relaCode"));
				vo.setLinkmanTel(rs.getString("linkmanTel"));
				vo.setLinkmanAddress(rs.getString("linkmanAddress"));
				vo.setReportBedward(rs.getString("reportBedward"));
				vo.setMcardNo(rs.getString("mcardNo"));
				return vo;
			}
			
		});
		if(PrepayinVoList!=null&&PrepayinVoList.size()>0){
			return PrepayinVoList;
		}
		return new ArrayList<PrepayinVo>();
	}
	
	/**  
	 *  
	 * @Description：  根据病区id查询床号
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessHospitalbed> queryBedByNurse(String nurId) {
		String sql="select h.bed_name as bedName,h.bed_id as id from t_business_hospitalbed h where h.bedward_id in (select bedward_id from t_business_bedward b ";
		sql=sql+" where b.del_flg=0 and b.stop_flg= 0)  and h.del_flg=0 and h.stop_flg= 0";
		SQLQuery query=getSession().createSQLQuery(sql).addScalar("bedName").addScalar("id");
		List<BusinessHospitalbed> list=query.setResultTransformer(Transformers.aliasToBean(BusinessHospitalbed.class)).list();
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<BusinessHospitalbed>();
	}
	
	
	/**  
	 *  
	 * @Description：  合同单位渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessContractunit> queryContractunit() {
		String sql ="select t.UNIT_CODE as encode, t.UNIT_NAME as name from T_BUSINESS_CONTRACTUNIT t where t.DEL_FLG =0 and t.STOP_FLG =0 ";
		List<BusinessContractunit> list =  jdbcTemplate.query(sql,new RowMapper<BusinessContractunit>() {

			@Override
			public BusinessContractunit mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessContractunit businessContractunit = new BusinessContractunit();
				businessContractunit.setEncode(rs.getString("encode"));
				businessContractunit.setName(rs.getString("name"));
				return businessContractunit;
			}
			 
		 });
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<BusinessContractunit>();
	}
	
	
	/**  
	 *  
	 * @Description：  就诊卡号渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<PatientIdcard> queryIdCard() {
		String hql="from PatientIdcard where stop_flg=0 and del_flg=0";
		List<PatientIdcard> list = super.find(hql, null);
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<PatientIdcard>();
	}
	
	/**  
	 *  
	 * @Description：查询住院主表是否有该患者
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-21 下午16:29:35 
	 * @param：mId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow queryInpatientInfo(String mId) {
		String hql = "from InpatientInfoNow ip where  ip.medicalrecordId = ? and inState not in ('N','O')";
		List<InpatientInfoNow> inpatientInfoList = super.find(hql, mId);
		if(inpatientInfoList!=null && inpatientInfoList.size()>0){
			return inpatientInfoList.get(0);
		}
		return new InpatientInfoNow();
	}
	
	
	/**  
	 *  
	 * @Description：渲染城市
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-21 下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> queryDistinct() {
		String sql = "select  t.CITY_CODE as cityCode, t.CITY_NAME as cityName from T_DISTRICT t ";
		List<District> distinctList =  jdbcTemplate.query(sql,new RowMapper<District>() {

			@Override
			public District mapRow(ResultSet rs, int rowNum) throws SQLException {
				District district = new District();
				district.setCityCode(rs.getString("cityCode"));
				district.setCityName(rs.getString("cityName"));
				return district;
			}
			
		});
		if(distinctList!=null && distinctList.size()>0){
			return distinctList;
		}
		return new ArrayList<District>();
	}
	
	/**  
	 *  
	 * @Description：查询开立住院证表是否有该患者
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-21 下午16:29:35 
	 * @param：mId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientProof queryInpatientProof(String mId,String no) {
		String sql ="select t.PROOF_ID as id  from T_INPATIENT_PROOF t where t.DEL_FLG=0 "
				+ "and t.STOP_FLG=0 and t.MEDICALRECORD_ID = :medicalrecordId and t.IDCARD_NO =:idcardNo";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", mId);
		paraMap.put("idcardNo", no);
		List<InpatientProof> inpatientInfoList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientProof>() {
			@Override
			public InpatientProof mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientProof inpatientProof= new InpatientProof();
				inpatientProof.setId(rs.getString("id"));
				return inpatientProof;
			}
			
		});
		if(inpatientInfoList!=null && inpatientInfoList.size()>0){
			return inpatientInfoList.get(0);
		}
		return new InpatientProof();
	}
	
	/**  
	 *  
	 * @Description：根据病床id查询维护该病床的医生
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-24下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysEmployee> queryInpatientBedinfo(String bedId) {
		String hql=" from InpatientBedinfoNow i where i.stop_flg=0 and i.del_flg=0 and i.bedId = '"+bedId+"'";
		List<InpatientBedinfoNow> list=super.find(hql, null);
		if(list.size()>0&&list!=null){
			String hql2="from SysEmployee s where s.id = '"+list.get(0).getNurseCellCode()+"' "
					+ "and s.del_flg=0 and s.stop_flg = 0";
			List<SysEmployee> list2=super.find(hql2, null);
			if(list2.size()>0&&list2!=null){
				return list2;
			}
			return new ArrayList<SysEmployee>();
		}
		return new ArrayList<SysEmployee>();
	}
	
	/**  
	 *  
	 * @Description：查询患者是否已经住院预约
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-24下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientPrepayin inpPrepayin(String mId) {
		String sql ="select t.ID as id from T_INPATIENT_PREPAYIN t where t.DEL_FLG=0 and t.STOP_FLG=0 "
				+ "and t.MEDICALRECORD_ID =:medicalrecordId and t.PRE_STATE =0" ;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", mId);
		List<InpatientPrepayin> inpatientInfoList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientPrepayin>() {

			@Override
			public InpatientPrepayin mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientPrepayin inpatientPrepayin = new InpatientPrepayin();
				inpatientPrepayin.setId(rs.getString("id"));
				return inpatientPrepayin;
			}
			
		});
		if(inpatientInfoList!=null && inpatientInfoList.size()>0){
			return inpatientInfoList.get(0);
		}
		return new InpatientPrepayin();
	}
	@Override
	public List<DepartmentContact> queryNurInfo(String dept) {
		String sql = "select t1.DEPT_CODE as deptCode, t1.DEPT_NAME as deptName from T_DEPARTMENT_CONTACT t1 where t1.ID in (select t.PARDEPT_ID from T_DEPARTMENT_CONTACT t "
				+ "where t.DEPT_ID =:dept and t.REFERENCE_TYPE ='03') ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("dept", dept);
		List<DepartmentContact> nurInfoList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<DepartmentContact>() {

			@Override
			public DepartmentContact mapRow(ResultSet rs, int rowNum) throws SQLException {
				DepartmentContact departmentContact = new DepartmentContact();
				departmentContact.setDeptCode(rs.getString("deptCode"));
				departmentContact.setDeptName(rs.getString("deptName"));
				return departmentContact;
			}
			
		});
		if(nurInfoList!=null && nurInfoList.size()>0){
			return nurInfoList;
		}
		return new ArrayList<DepartmentContact>();
	}
	@Override
	public List<InpatientProof> queryProofList(String medId) {
		String sql ="select * from T_INPATIENT_PROOF t where t.MEDICALRECORD_ID =:medicalrecordId";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", medId);
		List<InpatientProof> proofList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientProof>() {
			@Override
			public InpatientProof mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientProof inpatientProof = new InpatientProof();
				inpatientProof.setPatientName(rs.getString("PATIENT_NAME"));
				inpatientProof.setReportSex(rs.getString("REPORT_SEX"));
				inpatientProof.setMedicalrecordId(rs.getString("MEDICALRECORD_ID"));
				inpatientProof.setIdcardNo(rs.getString("IDCARD_NO"));
				return inpatientProof;
			}
		});
		if(proofList!=null && proofList.size()>0){
			return proofList;
		}
		return new ArrayList<InpatientProof>();
	}
	@Override
	public List<BusinessBedward> queryBedWardInfo(String bedId) {
		String sql ="select t.BEDWARD_ID  as id from T_BUSINESS_HOSPITALBED t where t.BED_ID = :bedId";
		Query query = this.getSession().createSQLQuery(sql.toString()).setParameter("bedId", bedId);
		String id = (String) query.list().get(0);
		String sql1 =" select t.BEDWARD_ID as id, t.BEDWARD_NAME as bedwardName "
				+ "from T_BUSINESS_BEDWARD t where t.BEDWARD_ID =:id";
		Map<String, Object> paraMap1 = new HashMap<String, Object>();
		paraMap1.put("id", id);
		List<BusinessBedward> proofList =  namedParameterJdbcTemplate.query(sql1,paraMap1,new RowMapper<BusinessBedward>() {

			@Override
			public BusinessBedward mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessBedward businessBedward =new BusinessBedward();
				businessBedward.setId(rs.getString("id"));
				businessBedward.setBedwardName(rs.getString("bedwardName"));
				return businessBedward;
			}
			
		});
		if(proofList!=null && proofList.size()>0){
			return proofList;
		}
		return new ArrayList<BusinessBedward>();
	}
	@Override
	public InpatientProof getProof(String medicalrecordId, String medId) {
		String infoTime=parameterDAO.getParameterByCode("infoTime");
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		int days=1;
		days = Integer.valueOf(infoTime);
		Date date = new Date();
		Date date2 = DateUtils.addDay(new Date(), -days);
		String sql="select i.PATIENT_NAME as patientName "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_MAIN_NOW  r "
				+ "left join "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_proof  i "
				+ "on r.CLINIC_CODE = i.idcard_no "
				+ "left join "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_CONTRACTUNIT c "
				+ " on i.contract_unit =  c.UNIT_CODE "  
				+ "where i.MEDICALRECORD_ID = :medicalrecordId and i.idcard_no= :medId and i.del_flg=0 and i.stop_flg= 0  "
				+ "and r.REG_DATE between to_date(:date2,'yyyy/MM/dd hh24:mi:ss') and to_date(:date,'yyyy/MM/dd hh24:mi:ss') "
				+ "and i.REPORT_ISSUINGDATE between to_date(:date2,'yyyy/MM/dd hh24:mi:ss') and to_date(:date,'yyyy/MM/dd hh24:mi:ss')";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", medicalrecordId);
		paraMap.put("medId", medId);
		paraMap.put("date2",(dateFormater.format(date2))+" 00:00:00");
		paraMap.put("date", (dateFormater.format(date))+" 23:59:59");
		
		List<InpatientProof> list =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientProof>() {

			@Override
			public InpatientProof mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientProof inpatientProof = new InpatientProof();
				inpatientProof.setPatientName(rs.getString("patientName"));
				return inpatientProof;
			}
			
		});
		 if(list!=null&&list.size()>0){
			 return (InpatientProof)list.get(0);
		 }
		   return new InpatientProof();
	}
	@Override
	public int updatePrepayin(InpatientPrepayin i) {
		String sql ="update T_INPATIENT_PREPAYIN set DEL_FLG =?,STOP_FLG=?,PRE_STATE=? ,DELETEUSER=?,"
				+ "DELETETIME=?,UPDATEUSER=?,UPDATETIME=? where ID=?";
		Object args[] = new Object[]{1,1,1,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),new Date(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),new Date(),i.getId()}; 
		int t = jdbcTemplate.update(sql,args);
		return t;  
		
	}
	
}
