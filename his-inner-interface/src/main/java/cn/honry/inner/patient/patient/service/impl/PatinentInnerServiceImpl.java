package cn.honry.inner.patient.patient.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.patient.patientBlack.dao.PatientBlackInnerDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("patinentInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class PatinentInnerServiceImpl implements PatinentInnerService{
		@Resource
		@Qualifier(value="patinmentInnerDao")
		private PatinmentInnerDao patinmentInnerDao;
		
		@Resource
		private RedisUtil redis;
		
		@Autowired
		@Qualifier(value="patientBlackInnerDAO")
		private PatientBlackInnerDAO patientBlackInnerDAO;
		/**
		 * 删除
		 * @date 2015-06-02
		 * @author sgt
		 * @version 1.0
		 * @return
		 */	
		@Override
		public void delete(Patient patient) {
			String cardNo = patient.getCardNo();
			try {
				if(StringUtils.isNotBlank(cardNo)){
					redis.hset("patient", patient.getCardNo(), null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			patinmentInnerDao.delete(patient);
		}
		/**
		 * 根据ID返回数据
		 * @date 2015-06-02
		 * @author sgt
		 * @version 1.0
		 * @return
		 */	
		@Override
		public Patient queryById(String patient) {
			return patinmentInnerDao.queryById(patient);
		}
		
		/**  
		 *  
		 * @Description：保存或修改
		 * @Author：lt
		 * @CreateDate：2015-6-18  
		 * @Modifier：lt
		 * @ModifyDate：2015-6-18  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		@Override
		public void saveOrUpdate(Patient entity) {
			User user = (User) SessionUtils.getCurrentUserFromShiroSession();
			if(StringUtils.isBlank(entity.getId())){//保存	
				SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				entity.setId(null);
				entity.setCreateUser(user.getAccount());
				entity.setCreateDept(dept.getDeptCode());
				entity.setCreateTime(new Date());
				entity.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());//所属医院
				entity.setAreaCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getAreaCode());//所属院区
			}else{//修改
				entity.setUpdateUser(user.getAccount());
				entity.setUpdateTime(new Date());
			}
			String pyWb = patinmentInnerDao.getSpellCode(entity.getPatientName());
			if(pyWb!=null&&pyWb.contains("$")){
				String pw[] = pyWb.split("\\$");
				entity.setPatientPinyin(pw[0]);
				entity.setPatientWb(pw[1]);
			}
			if (StringUtils.isNotBlank(entity.getPatientName())) {//姓名
				entity.setPatientName(entity.getPatientName().trim());//患者姓名去掉空格
			}
			if (StringUtils.isNotBlank(entity.getPatientAddress())) {
				entity.setPatientAddress(entity.getPatientAddress().trim());//家庭详细地址去掉空格
			}
			if (StringUtils.isNotBlank(entity.getPatientPhone())) {
				entity.setPatientPhone(entity.getPatientPhone().trim());//联系方式去掉空格
			}
			if (StringUtils.isNotBlank(entity.getPatientCertificatesno())) {
				entity.setPatientCertificatesno(entity.getPatientCertificatesno().trim());//证件号码去掉空格
			}
			patinmentInnerDao.save(entity);
			String cardNo = entity.getCardNo();
			String medicalrecordId = entity.getMedicalrecordId();
			if(StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(medicalrecordId)){
				Patient patient = new Patient();
				patient.setCardNo(cardNo);
				patient.setMedicalrecordId(medicalrecordId);
				try {
					redis.hset("patient", cardNo, patient);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		@Override
		public void removeUnused(String id) {
			patinmentInnerDao.del(id, SessionUtils.getCurrentUserFromShiroSession().toString());
		}
		/**  
		 *  
		 * @Description：根据id拿到实体
		 * @Author：lt
		 * @CreateDate：2015-6-18  
		 * @Modifier：lt
		 * @ModifyDate：2015-6-18  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		@Override
		public Patient get(String id) {
			return patinmentInnerDao.get(id);
		}
		/**  
		 *  
		 * @Description： 点击树节点查询
		 * @Author：wujiao
		 * @CreateDate：2015-7-6 下午11:12:01  
		 * @Modifier：wujiao
		 * @ModifyDate：2015-7-6 下午11:12:01  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		@Override
		public Patient gethzid(String id ,String type,String typeno) {
			return patinmentInnerDao.gethzid(id,type,typeno);
		}
		@Override
		public boolean checkHandBook(String handBook,String patientId) {
			return patinmentInnerDao.checkHandBook(handBook,patientId);
		}
		@Override
		/** 
		 * @Description： 保存患者信息，保存挂号信息
		 * @Author：tcj 
		 */
		public RegisterInfo savePatient(Patient patient) {
			patient.setId(null);
			String str = patinmentInnerDao.getSpellCode(patient.getPatientName());
			int index=str.indexOf("$");
			String pinyin=str.substring(0,index);
			String wb=str.substring(index+1);
			patient.setPatientPinyin(pinyin);
			patient.setPatientWb(wb);
			Patient p = patinmentInnerDao.savePatient(patient);
			
			RegisterInfo info = new RegisterInfo();
			info.setId(null);
			info.setPatientId(p);
			info.setMidicalrecordId(p.getMedicalrecordId());
			
			patinmentInnerDao.save(info);
			String cardNo = p.getCardNo();
			String medicalrecordId = p.getMedicalrecordId();
			if(StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(medicalrecordId)){
				Patient pa = new Patient();
				pa.setCardNo(cardNo);
				pa.setMedicalrecordId(medicalrecordId);
				try {
					redis.hset("patient", cardNo, pa);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return info;
		}
		@Override
		public Patient queryByMedicalrecordId(String medicalrecordId) {
			return patinmentInnerDao.queryByMedicalrecordId(medicalrecordId);
		}
		
		/**
		 * 根据员工工号查询员工
		 * zpty 20160324
		 * @param jobNo
		 * @return
		 */
		@Override
		public SysEmployee findEmpByjobNo(String jobNo) {
			List<SysEmployee> employeeList = patinmentInnerDao.findEmpByjobNo(jobNo);
			return employeeList.size()>0?employeeList.get(0):new SysEmployee();
		}
		
		/**  
		 *  
		 * @Description：  合同单位下拉框
		 * @Author：zpty
		 * @CreateDate：2016-4-26 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		@Override
		public List<BusinessContractunit> queryUnitCombobox() {
			List<BusinessContractunit> list=null;
			try {
				list = (List<BusinessContractunit>) redis.get("contractunit_queryAll");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(list!=null){
				return list;
			}
			return patinmentInnerDao.queryUnitCombobox();
		}
		
		/**  
		 *  
		 * @Description：保存(黑名单)
		 * @Author：kjh
		 * @CreateDate：2015-11-5 上午11:59:48  
		 * @version 1.0
		 *
		 */
		@Override
		public void save(PatientBlackList entity) {
			if(StringUtils.isNotEmpty(entity.getPatientId())){
				Patient patient=patinmentInnerDao.get(entity.getPatientId());
				if(StringUtils.isNotEmpty(patient.getId())){
					entity.setPatient(patient);
				}
			}
			if(StringUtils.isEmpty(entity.getId())){
				entity.setId(null);
				entity.setCreateUser(SessionUtils.getCurrentUserFromShiroSession().toString());
				entity.setCreateDept(SessionUtils.getCurrentUserLoginDepartmentFromShiroSession().toString());
				entity.setCreateTime(DateUtils.getCurrentTime());
				patientBlackInnerDAO.save(entity);
			}else{
				entity.setUpdateUser(SessionUtils.getCurrentUserFromShiroSession().toString());
				entity.setUpdateTime(new Date());
				patientBlackInnerDAO.save(entity);
			}
		}
		
		/**
		 * 根据传过来的病历号来返回住院次数
		 * @date 2016-11-23
		 * @author zpty
		 * @version 1.0
		 * @return
		 */	
		@Override
		public Integer querySumByMedicalreId(String medicalrecordId) {
			return patinmentInnerDao.querySumByMedicalreId(medicalrecordId);
		}
		
		/**
		 * 根据传过来的病历号来更新此患者的住院次数和住院流水号
		 * @date 2016-11-23
		 * @author zpty
		 * @version 1.0
		 * @return
		 */	
		@Override
		public void updateByMedicalreId(String medicalrecordId,
				Integer inpatientSum, String inpatientNo) {
			if(StringUtils.isNotEmpty(medicalrecordId)){
				Patient patient=patinmentInnerDao.queryByMedicalrecordId(medicalrecordId);
				if(StringUtils.isNotEmpty(patient.getId())){
					patient.setInpatientSum(inpatientSum);
					patient.setInpatientNo(inpatientNo);
					patient.setUpdateUser(SessionUtils.getCurrentUserFromShiroSession().toString());
					patient.setUpdateTime(new Date());
					patientBlackInnerDAO.save(patient);
				}
			}
		}
		/**
		 * 根据就诊卡号获取病历号
		 * @date 2016-11-24
		 * @author zhangjin
		 * @version 1.0
		 * @return
		 */	
		@Override
		public String getMedicalrecordId(String cardNo) {
			String s=null;
			Patient hget=null;
			try {
				hget = (Patient) redis.hget("patient", cardNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(hget!=null){
				s=hget.getMedicalrecordId();
			}else{
				s=patinmentInnerDao.getMedicalrecordId(cardNo);
			}
			return s;
		}
		
		/**
		 * 更新患者数据中的cardNo字段  根据ID
		 * 2017年2月8日09:42:28
		 * GH
		 */
		@Override
		public void updCradNoByIdOrCard(String id,String idcard, String cradNo) {
			patinmentInnerDao.updCradNoByIdOrCard(id,idcard, cradNo);
		}
		
}
