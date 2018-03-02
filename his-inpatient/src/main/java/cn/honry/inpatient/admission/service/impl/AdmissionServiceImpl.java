package cn.honry.inpatient.admission.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPostureInfo;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.admission.dao.AdmissionDAO;
import cn.honry.inpatient.admission.service.AdmissionService;
import cn.honry.inpatient.admission.vo.AdmissionVO;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Service("admissionService")
@Transactional
@SuppressWarnings({"all"}) 
public class AdmissionServiceImpl implements AdmissionService{
	@Autowired
	@Qualifier(value = "admissionDAO")
	private AdmissionDAO admissionDAO;
	@Override
	public void removeUnused(String id) {
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}
	@Override
	public AdmissionVO get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(AdmissionVO entity) {
	}

	@Override
	public AdmissionVO getVOByPatientNo(String medicalrecordId) throws Exception{
		return admissionDAO.getVOByPatientNo(medicalrecordId);
	}

	@Override
	public String saveAdmission(AdmissionVO admissionVO) throws Exception{
		AdmissionVO advo=admissionDAO.getVOByPatientNo(admissionVO.getInpatientNo());
		Integer hospitalId=HisParameters.CURRENTHOSPITALID;//医院ID
		String  areaDept=inprePayDAO.getDeptArea(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		if((StringUtils.isNotBlank(admissionVO.getPatientName())) && !admissionVO.getPatientName().equals(advo.getPatientName())){
			 InpatientShiftData inpatientShiftData = new InpatientShiftData();
			 inpatientShiftData.setId(null);
			 inpatientShiftData.setClinicNo(admissionVO.getInpatientNo());
			 List<InpatientShiftData> inpatientShift= admissionDAO.queryMaxHappenNo();
			 inpatientShiftData.setHappenNo(inpatientShift.get(0).getHappenNo()+1);
			 inpatientShiftData.setShiftType("K");
			 inpatientShiftData.setOldDataName(advo.getPatientName());
			 inpatientShiftData.setNewDataName(admissionVO.getPatientName());
			 inpatientShiftData.setShiftCause("住院接诊");
			 inpatientShiftData.setTableName("T_INPATIENT_INFO_NOW");
			 inpatientShiftData.setChangePropertyName("patientName");
			 inpatientShiftData.setCreateTime(DateUtils.getCurrentTime());
			 inpatientShiftData.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			 inpatientShiftData.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			 inpatientShiftData.setAreaCode(areaDept);
			 inpatientShiftData.setHospitalId(hospitalId);
			 admissionDAO.save(inpatientShiftData);
			 OperationUtils.getInstance().conserve(null,"资料变更","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
		}
		if(StringUtils.isNotBlank(admissionVO.getCertificatesNo()) && !admissionVO.getCertificatesNo().equals(advo.getCertificatesNo())){
			 InpatientShiftData inpatientShiftData1 = new InpatientShiftData();
			 inpatientShiftData1.setId(null);
			 inpatientShiftData1.setClinicNo(admissionVO.getInpatientNo());
			 List<InpatientShiftData> inpatientShift= admissionDAO.queryMaxHappenNo();
			 inpatientShiftData1.setHappenNo(inpatientShift.get(0).getHappenNo()+1);
			 inpatientShiftData1.setShiftType("K");
			 inpatientShiftData1.setOldDataName(advo.getCertificatesNo());
			 inpatientShiftData1.setNewDataName(admissionVO.getCertificatesNo());
			 inpatientShiftData1.setShiftCause("住院接诊");
			 inpatientShiftData1.setTableName("T_INPATIENT_INFO_NOW");
			 inpatientShiftData1.setChangePropertyName("certificatesNo");
			 inpatientShiftData1.setCreateTime(DateUtils.getCurrentTime());
			 inpatientShiftData1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			 inpatientShiftData1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			 inpatientShiftData1.setAreaCode(areaDept);
			 inpatientShiftData1.setHospitalId(hospitalId);
			 admissionDAO.save(inpatientShiftData1);
			 OperationUtils.getInstance().conserve(null,"资料变更","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
		}
		if((StringUtils.isNotBlank(admissionVO.getBedId()))&&!admissionVO.getBedId().equals(advo.getBedId())){
			 InpatientShiftData inpatientShiftData2 = new InpatientShiftData();
			 inpatientShiftData2.setId(null);
			 inpatientShiftData2.setClinicNo(admissionVO.getInpatientNo());
			 List<InpatientShiftData> inpatientShift= admissionDAO.queryMaxHappenNo();
			 inpatientShiftData2.setHappenNo(inpatientShift.get(0).getHappenNo()+1);
			 inpatientShiftData2.setShiftType("K");
			 inpatientShiftData2.setOldDataCode(advo.getBedId());
			 inpatientShiftData2.setNewDataCode(admissionVO.getBedId());
			 inpatientShiftData2.setShiftCause("住院接诊");
			 inpatientShiftData2.setTableName("T_INPATIENT_INFO_NOW");
			 inpatientShiftData2.setChangePropertyName("BEDINFO_ID");
			 inpatientShiftData2.setCreateTime(DateUtils.getCurrentTime());
			 inpatientShiftData2.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			 inpatientShiftData2.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			 inpatientShiftData2.setAreaCode(areaDept);
			 inpatientShiftData2.setHospitalId(hospitalId);
			 admissionDAO.save(inpatientShiftData2);
			 OperationUtils.getInstance().conserve(null,"资料变更","INSERT INTO","T_INPATIENT_SHIFTDATA",OperationUtils.LOGACTIONINSERT);
		}
		OperationUtils.getInstance().conserve(admissionVO.getOldMedicalrecordId(),"住院接诊_住院主表","UPDATE","T_INPATIENT_INFO_NOW",OperationUtils.LOGACTIONUPDATE);
		OperationUtils.getInstance().conserve(admissionVO.getOldMedicalrecordId(),"住院接诊_患者信息表","UPDATE","T_PATIENT",OperationUtils.LOGACTIONUPDATE);
		OperationUtils.getInstance().conserve(admissionVO.getOldMedicalrecordId(),"住院接诊_患者体征信息表","UPDATE","T_INPATIENT_POSTUREINFO",OperationUtils.LOGACTIONUPDATE);
		OperationUtils.getInstance().conserve(admissionVO.getOldMedicalrecordId(),"住院接诊_床位状态信息表","UPDATE","T_BUSINESS_HOSPITALBED",OperationUtils.LOGACTIONUPDATE);
		return admissionDAO.saveAdmission(admissionVO);
	}


	@Override
	public List<SysEmployee> getZYDepartmentDoctors(String deptName) throws Exception {
		return admissionDAO.getZYDepartmentDoctors(deptName);
	}

	@Override
	public List<VidBedInfo> getBedInfoWithDeptId(String id, String status)throws Exception {
		return admissionDAO.getBedInfoWithDeptId(id,status);
	}

	@Override
	public BusinessHospitalbed getBedState(String bedId)throws Exception {
		return admissionDAO.getBedState(bedId);
	}

	@Override
	public List<SysEmployee> queryZerenhushi() throws Exception{
		return admissionDAO.queryZerenhushi();
	}

	@Override
	public List<Patient> queryPatientexist(String medicalrecordId)throws Exception {
		return admissionDAO.queryPatientexist(medicalrecordId);
	}

	@Override
	public List<SysEmployee> queryZhuyuanDoc(String q)throws Exception {
		return admissionDAO.queryZhuyuanDoc(q);
	}

	@Override
	public String queryPatientStatInfo(String medId)throws Exception {
		return admissionDAO.queryPatientStatInfo(medId);
	}

	@Override
	public List<SysEmployee> findZhuyuanDoc(String name, String type,String page,String row)throws Exception {
		return admissionDAO.findZhuyuanDoc(name,type,page,row);
	}

	@Override
	public int getTotalemp(String name, String type)throws Exception {
		return admissionDAO.getTotalemp(name,type);
	}

	@Override
	public List<InpatientInfoNow> getQueryInfo(String medicalrecordId)throws Exception {
		return admissionDAO.getQueryInfo(medicalrecordId);
	}

	@Override
	public String isExistBed(String bed, String bedWard,String inpatientNo)throws Exception {
		return admissionDAO.isExistBed(bed,bedWard,inpatientNo);
	}

	@Override
	public List<InpatientInfoNow>  querAdmiss(String medicalrecordId)throws Exception {
		return admissionDAO.querAdmiss(medicalrecordId);
	}

	@Override
	public List<InpatientPostureInfo> queryPostureInfo(String medicalrecordId)throws Exception {
		return admissionDAO.queryPostureInfo(medicalrecordId);
	}


}
