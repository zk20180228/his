package cn.honry.inpatient.admission.dao.impl;

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
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPostureInfo;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
import cn.honry.inpatient.admission.dao.AdmissionDAO;
import cn.honry.inpatient.admission.vo.AdmissionVO;
import cn.honry.inpatient.info.dao.InpatientBedInfoDAO;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@SuppressWarnings({"all"})
@Repository("admissionDAO")
public class AdmissionDaoImpl extends HibernateEntityDao<AdmissionVO> implements AdmissionDAO{
	/**
	 * 注入session
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier(value = "inpatientBedInfoDAO")
	private InpatientBedInfoDAO inpatientBedInfoDAO;
	@Autowired
	@Qualifier(value = "hospitalbedInInterDAO")
	private HospitalbedInInterDAO hospitalbedDAO;
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}

	public InpatientBedInfoDAO getInpatientBedInfoDAO() {
		return inpatientBedInfoDAO;
	}

	public void setInpatientBedInfoDAO(InpatientBedInfoDAO inpatientBedInfoDAO) {
		this.inpatientBedInfoDAO = inpatientBedInfoDAO;
	}

	public HospitalbedInInterDAO getHospitalbedDAO() {
		return hospitalbedDAO;
	}

	public void setHospitalbedDAO(HospitalbedInInterDAO hospitalbedDAO) {
		this.hospitalbedDAO = hospitalbedDAO;
	}
	private String mainId;//床位ID
	@Override
	public AdmissionVO getVOByPatientNo(String medicalrecordId){
		InpatientInfoNow info = null;
		String hql="FROM InpatientInfoNow i WHERE i.inState = 'R' ";
		hql = hql +" and i.inpatientNo = :medicalrecordId  order by createTime desc";
		List<InpatientInfoNow> lst = null;
		try {
			lst =  this.getSession().createQuery(hql).setParameter("medicalrecordId", medicalrecordId).list();
			if(lst!=null){
				if(lst.size()>0){
					info= lst.get(0);//获得第一条记录的头表信息
				}
			}
		} catch (Exception e) {
				 e.printStackTrace();;
		} 
		
		if(info!=null){
			AdmissionVO vo = new AdmissionVO();
			vo.setInState(info.getInState());//住院状态 
			vo.setBingqu(info.getNurseCellCode());//病区
			vo.setInpatientNo(info.getInpatientNo());//住院流水号
			vo.setOldMedicalrecordId(info.getMedicalrecordId());//病例号
			vo.setPatientInfoId(info.getId());//住院主表id
			vo.setPatientName(info.getPatientName());//患者姓名
			vo.setSex(info.getReportSex());//性别
			vo.setBlNumber(info.getMedicalrecordId());//病例号
			vo.setSettlement(info.getPaykindCode());//结算方式
			vo.setBirthArea(info.getBirthArea());//出生地
			vo.setCountry(info.getCounCode());//国籍
			vo.setNation(info.getNationCode());//民族
			vo.setReportBirthday(info.getReportBirthday()==null?null:new SimpleDateFormat("yyyy-MM-dd").format(info.getReportBirthday()));//出生日期
			vo.setReportAge(info.getReportAge());//年龄
			vo.setDist(info.getDist());//籍贯
			vo.setOccupation(info.getProfCode());//职业
			vo.setCertificatesNo(info.getCertificatesNo());//身份证号码
			vo.setCertificatesType(info.getCertificatesType());
			vo.setWorkName(info.getWorkName());//工作单位
			vo.setWorkTel(info.getWorkTel());//单位电话
			vo.setMarry(info.getMari());//婚姻状况
			vo.setHome(info.getHome());//家庭地址
			vo.setHomeTel(info.getHomeTel());//家庭电话
			vo.setHaveBabyFlag(info.getHaveBabyFlag());//是否有婴儿
			vo.setSource(info.getInSource());//入院来源
			vo.setInAvenue(info.getInAvenue());//入院途径
			vo.setInCircs(info.getInCircs());//入院情况
			vo.setFeeInterval(info.getFeeInterval()==null?new Integer(1):info.getFeeInterval()); //床费间隔 **
			vo.setInDate(info.getInDate()==null?null:new SimpleDateFormat("yyyy-MM-dd").format(info.getInDate()));//入院日期
			vo.setRemark(info.getRemark());//备注
			vo.setDiagName(info.getDiagName());//入院诊断
			vo.setHouseDocCode(info.getHouseDocCode());
			vo.setHouseDocName(info.getHouseDocName());
			vo.setBedId(info.getBedId());//病床使用记录表主键Id
 			vo.setBedNo(info.getBedNo());
			vo.setBedName(info.getBedName());
			BusinessBedward businessBedward = new BusinessBedward();
			businessBedward.setId(info.getBedwardId());
			businessBedward.setBedwardName(info.getBedwardName());;
			vo.setBusinessBedward(businessBedward);
			return vo;
		}else{
			return new AdmissionVO();
		}
		
	}

	private String getBedName(String bedId) {
		if(bedId!=null){
			return this.get(BusinessHospitalbed.class,bedId).getBedName();
		}
		return null;
	}

	@Override
	public String saveAdmission(AdmissionVO admissionVO) throws Exception {
		InpatientInfoNow oldPatientInfo = null;
		if(null!=admissionVO.getPatientInfoId()){
			String flg="0";
			//根据住院主表id获取住院主表信息
			oldPatientInfo = this.getPatientInfo(admissionVO.getPatientInfoId());
			if(oldPatientInfo==null){
				//无患者住院登记信息，无法更新住院登记信息！
				flg="1";
				return flg;
			}
			//根据病例号获得患者信息
			String patientId="";
			List<Patient> oldPatientList = this.getPatientByMedicalRecordId(admissionVO.getOldMedicalrecordId());
			if(oldPatientList!=null&&oldPatientList.size()>0){
				//1:根据住院主表id 更新住院主表信息及 住院状态为接诊状态
				oldPatientInfo.setInState("I");//住院状态更新为接诊状态
				oldPatientInfo.setPatientName(admissionVO.getPatientName());//患者姓名
				oldPatientInfo.setReportSex(admissionVO.getSex());//性别
				oldPatientInfo.setMedicalrecordId(admissionVO.getOldMedicalrecordId());//病历号
				oldPatientInfo.setHaveBabyFlag(admissionVO.getHaveBabyFlag());
				oldPatientInfo.setBirthArea(admissionVO.getBirthArea());//出生地
				oldPatientInfo.setCounCode(admissionVO.getCountry());//国籍
				oldPatientInfo.setNationCode(admissionVO.getNation());//民族
				oldPatientInfo.setReportBirthday(admissionVO.getReportBirthday()==null?null:new SimpleDateFormat("yyyy-MM-dd").parse(admissionVO.getReportBirthday()));//出生日期
				oldPatientInfo.setReportAge(admissionVO.getReportAge());//年龄
				oldPatientInfo.setDist(admissionVO.getDist());//籍贯
				oldPatientInfo.setProfCode(admissionVO.getOccupation());//职业
				oldPatientInfo.setCertificatesNo(admissionVO.getCertificatesNo());
				oldPatientInfo.setCertificatesType(admissionVO.getCertificatesType());
				oldPatientInfo.setWorkName(admissionVO.getWorkName());//工作单位
				oldPatientInfo.setWorkTel(admissionVO.getWorkTel());//单位电话
				oldPatientInfo.setMari(admissionVO.getMarry());//婚姻状况
				oldPatientInfo.setHome(admissionVO.getHome());//家庭地址
				oldPatientInfo.setHomeTel(admissionVO.getHomeTel());//家庭电话
				oldPatientInfo.setInSource(admissionVO.getSource());//入院来源
				oldPatientInfo.setInAvenue(admissionVO.getInAvenue());//入院途径
				oldPatientInfo.setInCircs(admissionVO.getInCircs());//入院情况
				oldPatientInfo.setFeeInterval(admissionVO.getFeeInterval());//费用间隔天数
				oldPatientInfo.setRemark(admissionVO.getRemark());//备注
				oldPatientInfo.setDiagName(admissionVO.getDiagName());//入院诊断
				oldPatientInfo.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				oldPatientInfo.setUpdateTime(DateUtils.getCurrentTime());
				oldPatientInfo.setBedId(admissionVO.getBedId());//病床使用记录表主键ID
				oldPatientInfo.setBedwardId(admissionVO.getBusinessBedward().getId());
				oldPatientInfo.setBedwardName(admissionVO.getBusinessBedward().getBedwardName());
				oldPatientInfo.setBedName(admissionVO.getBedName());
				oldPatientInfo.setBedNo(admissionVO.getBedNo());
				oldPatientInfo.setChargeDocCode(admissionVO.getChargeDocCode());
				oldPatientInfo.setChargeDocName(admissionVO.getChargeDocName());
				oldPatientInfo.setChiefDocCode(admissionVO.getChiefDocCode());
				oldPatientInfo.setChiefDocName(admissionVO.getChiefDocName());
				oldPatientInfo.setDutyNurseCode(admissionVO.getDutyNurseCode());
				oldPatientInfo.setDutyNurseName(admissionVO.getDutyNurseName());
				oldPatientInfo.setHouseDocCode(admissionVO.getHouseDocCode());
				oldPatientInfo.setHouseDocName(admissionVO.getHouseDocName());
				this.update(oldPatientInfo);
				//2:根据住院主表的病例号更新患者信息表（页面中的相关患者信息）
				for(int i=0;i<oldPatientList.size();i++){
					Patient patient = oldPatientList.get(i);
					if(i==oldPatientList.size()-1){
						patientId=patientId+patient.getId();
					}else{
						patientId=patientId+patient.getId()+"_";
					}
					patient.setPatientName(admissionVO.getPatientName());
					patient.setPatientSex(admissionVO.getSex()==null?null:new Integer(admissionVO.getSex()));
					patient.setMedicalrecordId(admissionVO.getOldMedicalrecordId());//病历号
					patient.setPatientBirthplace(admissionVO.getBirthArea());//出生地
					patient.setPatientNationality(admissionVO.getCountry());//国籍
					patient.setPatientNation(admissionVO.getNation());//民族
					patient.setPatientBirthday(admissionVO.getReportBirthday()==null?null:new SimpleDateFormat("yyyy-MM-dd").parse(admissionVO.getReportBirthday()));
					patient.setPatientAge(admissionVO.getReportAge()==null?null:new Double(admissionVO.getReportAge()));
					patient.setPatientNativeplace(admissionVO.getDist());//籍贯
					patient.setPatientOccupation(admissionVO.getOccupation());//职业
					patient.setPatientCertificatesno(admissionVO.getCertificatesNo());//身份证号码
					patient.setPatientWorkunit(admissionVO.getWorkName());//工作单位
					patient.setPatientWorkphone(admissionVO.getWorkTel());//单位电话
					patient.setPatientWarriage(admissionVO.getMarry());//婚姻状况
					patient.setPatientAddress(admissionVO.getHome());//家庭地址
					patient.setPatientPhone(admissionVO.getHomeTel());//家庭电话
					patient.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					patient.setUpdateTime(DateUtils.getCurrentTime());
					this.update(patient);
				}
			}else{
				flg="2";
				return flg;
			}
			 //4:插入患者体征信息
			if(StringUtils.isNotBlank(patientId)){
				InpatientPostureInfo  posture = new InpatientPostureInfo();
				posture.setPatientNo(oldPatientInfo.getMedicalrecordId());
				posture.setName(admissionVO.getPatientName());
				posture.setTemperature(admissionVO.getTemperature());
				posture.setPulse(admissionVO.getPulse());
				posture.setBreath(admissionVO.getBreath());
				posture.setPressure(admissionVO.getPressure());
				posture.setWeight(admissionVO.getWeight());
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(admissionVO.getPostureDate());
				posture.setPostureDate(date);
				posture.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				posture.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				posture.setCreateTime(DateUtils.getCurrentTime());
				posture.setAreaCode(inprePayDAO.getDeptArea(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode()));
				posture.setHospitalId(HisParameters.CURRENTHOSPITALID);
				this.save(posture);
			}else{
				flg="4";
				return flg;
			}
			 //更新床位状态信息
			if(oldPatientInfo != null){
				BusinessHospitalbed businessHospitalbed = this.get(BusinessHospitalbed.class,admissionVO.getBedNo());
				businessHospitalbed.setBedState("4");//占用
				businessHospitalbed.setPatientId(admissionVO.getInpatientNo());//病历号
				businessHospitalbed.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				businessHospitalbed.setUpdateTime(DateUtils.getCurrentTime());
				this.updateBusinessHospitalbed(businessHospitalbed);
			}else{
				flg="5";
				return flg;
			}
			return flg;
		}else{
			return "1";
		}
		 
	}
	private void updateBusinessHospitalbed(BusinessHospitalbed businessHospitalbed) {
		String sql ="update T_BUSINESS_HOSPITALBED set BED_STATE=?,PATIENT_ID=?,UPDATEUSER=?,UPDATETIME=? "
				+ "where BED_ID=?";
		Object args[] = new Object[]{"4",businessHospitalbed.getPatientId(),businessHospitalbed.getUpdateUser(),businessHospitalbed.getUpdateTime(),businessHospitalbed.getId()};  
		int t = jdbcTemplate.update(sql,args);  
	}

	private InpatientInfoNow getPatientInfo(String patientInfoId) throws Exception {
		return this.get(InpatientInfoNow.class,patientInfoId);
	}

	private List<Patient> getPatientByMedicalRecordId(String oldMedicalrecordId) throws Exception {
		List<Patient> lst = null;
		String hql="FROM Patient i WHERE i.del_flg = 0 and i.stop_flg=0 and ";
		hql = hql +"  i.medicalrecordId = :oldMedicalrecordId";
		try {
			lst =  this.getSession().createQuery(hql).setParameter("oldMedicalrecordId", oldMedicalrecordId).list();
			return lst;
		} catch (Exception e) {
			throw e;
		} 
		
	}

	@Override
	public List<SysEmployee> getZYDepartmentDoctors(String deptName) throws Exception {
		List<SysEmployee> employeeList = null;
		String hql="from SysEmployee d where  d.deptId in(select c.id from SysDepartment c "
				+ "where  c.del_flg=0 and c.stop_flg=0  and c.deptParent in(select t.id "
				+ "from SysDepartment t where t.deptName like :deptName and t.del_flg=0 and t.stop_flg=0))";
		try {
			employeeList =  this.getSession().createQuery(hql).setParameter("deptName", "%"+deptName+"%").list();
			return employeeList;
		} catch (Exception e) {
			throw e;
		} 
	}

	@Override
	public List<VidBedInfo> getBedInfoWithDeptId(String id, String status) {
		String hql="FROM VidBedInfo where del_flg=0 and stop_flg=0 and deptCode='"+id+"'";
		if("7".equals(status)){
			hql=hql+" and bedState='"+status+"'";
		}
		List<VidBedInfo> patientList=super.find(hql, null);
		if(patientList==null&&patientList.size()>0){
			return new ArrayList<VidBedInfo>();
		}
		return patientList;
	}

	@Override
	public BusinessHospitalbed getBedState(String bedId) {
		return this.get(BusinessHospitalbed.class,bedId);
	}

	@Override
	public void updateInpatientProof(AdmissionVO admissionVO) {
		String hql="from RegisterInfo where del_flg=0 and stop_flg=0 and midicalrecordId= ?";
		List<RegisterInfo> resinfo=super.find(hql, admissionVO.getOldMedicalrecordId());
		String sql="update "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_proof set stop_flg = 1 "
				+ "where del_flg=0 and idcard_no = '"+resinfo.get(0).getNo()+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public List<InpatientShiftData> queryMaxHappenNo() {
		String hql="from InpatientShiftData t where t.happenNo "
				+ "in(select max(b.happenNo) from InpatientShiftData b)";
		List<InpatientShiftData> inpatientShiftData=this.getSession().createQuery(hql).list();
		if(inpatientShiftData!=null && inpatientShiftData.size()>0){
			return inpatientShiftData;
		}		
		return new ArrayList<InpatientShiftData>();		
	}

	@Override
	public List<SysEmployee> queryZerenhushi() {
		String hql="from SysEmployee where del_flg=0 and stop_flg=0 and type='2'";
		List<SysEmployee> sysl=super.find(hql, null);
		if(sysl!=null&&sysl.size()>0){
			return sysl;
		}
		return new ArrayList<SysEmployee>();
	}

	@Override
	public List<Patient> queryPatientexist(String num) {
		String sql=" select p.patinent_id as id,p.medicalrecord_id  as medicalrecordId "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+" t_patient p "
				+ "left join "+HisParameters.HISPARSCHEMAHISUSER+" T_REGISTER_MAIN_NOW r "
				+ "on p.medicalrecord_id=r.MEDICALRECORDID  "
				+ "where  r.MEDICALRECORDID = :num ";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("id").addScalar("medicalrecordId").setParameter("num", num);
		List<Patient> plist=queryObject.setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
		if(plist!=null&&plist.size()>0){
			return plist;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public InpatientBedinfoNow querybedInfo(String bedId) {
		String hql="from InpatientBedinfoNow where del_flg=0 and stop_flg=0 and id= ?";
		List<InpatientBedinfoNow> bedInfolist=super.find(hql, bedId);
		if(bedInfolist!=null&&bedInfolist.size()>0){
			return bedInfolist.get(0);
		}
		return new InpatientBedinfoNow();
	}

	@Override
	public List<SysEmployee> queryZhuyuanDoc(String q) {
		String sql=" select e.EMPLOYEE_JOBNO as jobNo,e.employee_name  as name "
				+ "from t_employee e where e.del_flg=0 and e.stop_flg=0 "
				+ "and e.EMPLOYEE_TYPE='1' ";
		if(StringUtils.isNotBlank(q)){
			sql = sql+" AND (e.EMPLOYEE_NAME LIKE :name OR e.EMPLOYEE_JOBNO LIKE :name OR "
					+ "EMPLOYEE_OLDNAME LIKE :name OR EMPLOYEE_PINYIN LIKE :name OR EMPLOYEE_WB LIKE :name "
					+ "OR EMPLOYEE_INPUTCODE LIKE :name)";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString())
				.addScalar("jobNo").addScalar("name");
		if(StringUtils.isNotBlank(q)){
			queryObject.setParameter("name", "%"+q+"%");
		}
		List<SysEmployee> plist=queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(plist!=null&&plist.size()>0){
			return plist;
		}
		return new ArrayList<SysEmployee>();
		
	}

	@Override
	public String queryPatientStatInfo(String medId) {
		String sql = "select t.PATINENT_ID as id from T_PATIENT t where t.DEL_FLG=0 and t.STOP_FLG=0 "
				+ "and t.MEDICALRECORD_ID =:medicalrecordId ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", medId);
		List<Patient> pl =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<Patient>() {
			@Override
			public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
				Patient patient = new Patient();
				patient.setId(rs.getString("id"));
				return patient;
			}
		});

		if(pl!=null&&pl.size()>0){
			String sql1 ="select t.INPATIENT_ID as id from T_INPATIENT_INFO_NOW t "
					+ "where t.MEDICALRECORD_ID=:medicalrecordId and t.IN_STATE='R'";
			Map<String, Object> paraMap1 = new HashMap<String, Object>();
			paraMap1.put("medicalrecordId", medId);
			List<InpatientInfoNow> infol =  namedParameterJdbcTemplate.query(sql1,paraMap1,new RowMapper<InpatientInfoNow>() {

				@Override
				public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
					InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
					inpatientInfoNow.setId(rs.getString("id"));
					return inpatientInfoNow;
				}
				
			});
			if(infol!=null&&infol.size()>0){
				return "3";
			}else{
				String sql2 ="select t.INPATIENT_ID as id from T_INPATIENT_INFO_NOW t where t.MEDICALRECORD_ID=:medicalrecordId and t.IN_STATE='I'";
				Map<String, Object> paraMap2 = new HashMap<String, Object>();
				paraMap2.put("medicalrecordId", medId);
				List<InpatientInfoNow> info2 =  namedParameterJdbcTemplate.query(sql2,paraMap2,new RowMapper<InpatientInfoNow>() {

					@Override
					public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
						InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
						inpatientInfoNow.setId(rs.getString("id"));
						return inpatientInfoNow;
					}
					
				});
				if(info2!=null&&info2.size()>0){
					return "I";
				}
				return "2";
			}
		}
		return "1";
	}

	@Override
	public List<SysEmployee> findZhuyuanDoc(String name,String type,String page, String row) {
		String sql="select * from ("+jointemp(name,type)+" and ROWNUM  <= (:page) * :row ) where rn >(:page -1) * :row";
		
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(row==null?"20":row);
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", start);
		paraMap.put("row", count);
		if("ys".equals(type)){
			paraMap.put("type", "1");
		}else{
			paraMap.put("type", "2");
		}
		
		if(StringUtils.isNotBlank(name)){
			paraMap.put("name", "%"+name+"%");
		}
		List<SysEmployee> plist =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<SysEmployee>() {

			@Override
			public SysEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysEmployee sysEmployee = new SysEmployee();
				sysEmployee.setJobNo(rs.getString("jobNo"));
				sysEmployee.setName(rs.getString("name"));
				sysEmployee.setDeptName(rs.getString("deptName"));
				return sysEmployee;
			}
			
		});
		
		if(plist!=null&&plist.size()>0){
			return plist;
		}
		return new ArrayList<SysEmployee>();
	}

	private String jointemp(String name,String type){
		String sql="select e.EMPLOYEE_JOBNO as jobNo,e.employee_name  as name , t.dept_name as deptName,ROWNUM as rn   "
				+ "from t_employee e left join t_department t on e.dept_id=t.dept_code where e.del_flg=0 and e.stop_flg=0 "
				+ "and e.EMPLOYEE_TYPE=:type ";
		if(StringUtils.isNotBlank(name)){
			sql = sql+" AND (e.EMPLOYEE_NAME LIKE :name OR e.EMPLOYEE_JOBNO LIKE :name OR EMPLOYEE_OLDNAME LIKE :name OR EMPLOYEE_PINYIN LIKE :name OR EMPLOYEE_WB LIKE :name OR EMPLOYEE_INPUTCODE LIKE :name)";
		}
		return sql;
	}
	
	@Override
	public int getTotalemp(String name, String type) {
		String sql=jointemp(name,type);
		if(sql==null){
			return 0;
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				.addScalar("jobNo").addScalar("name").addScalar("deptName");
		if("ys".equals(type)){
			queryObject.setParameter("type", "1");
		}else{
			queryObject.setParameter("type", "2");
		}
		
		if(StringUtils.isNotBlank(name)){
			queryObject.setParameter("name", "%"+name+"%");
		}
		
		List<Object> list = queryObject.list();
		if(list!=null&&list.size()>0){
			return list.size();
		}else{
			return 0;
		}
	}

	@Override
	public List<InpatientInfoNow> getQueryInfo(String medicalrecordId) {
		String hql="from InpatientInfoNow i where i.medicalrecordId = ? and i.inState  ='R' ";
		List<InpatientInfoNow> infoList=super.find(hql, medicalrecordId);
		if(infoList!=null&&infoList.size()>0){
			return infoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public String isExistBed(String bed, String bedWard,String inpatientNo) {
		String hql="from BusinessHospitalbed i where i.id = ? and i.businessBedward.id  =? ";
		List<BusinessHospitalbed> infoList=super.find(hql, bed,bedWard);
		if(infoList!=null&&infoList.size()>0){
			if("4".equals(infoList.get(0).getBedState())){
				if(infoList.get(0).getPatientId().equals(inpatientNo)){
					return "yes";
				}else{
					if(StringUtils.isBlank(infoList.get(0).getPatientId()) || infoList.get(0).getPatientId().equals("N")){
						return "yes";
					}else{
						return "no";
					}
				}
			}else{
				return "yes";
			}
		}
		return "yes";
	}
	@Override
	public List<InpatientInfoNow>  querAdmiss(String medicalrecordId) {
		String hql="from InpatientInfoNow i where i.medicalrecordId = ? and i.inState  ='I' ";
		List<InpatientInfoNow> infoList=super.find(hql, medicalrecordId);
		if(infoList!=null&&infoList.size()>0){
			return infoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public List<InpatientPostureInfo> queryPostureInfo(String medicalrecordId) {
		String hql="from InpatientPostureInfo i where i.patientNo = ?";
		List<InpatientPostureInfo> infoList=super.find(hql, medicalrecordId);
		if(infoList!=null&&infoList.size()>0){
			return infoList;
		}
		return new ArrayList<InpatientPostureInfo>();
	}

}
