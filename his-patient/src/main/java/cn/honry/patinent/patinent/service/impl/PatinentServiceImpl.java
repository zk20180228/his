package cn.honry.patinent.patinent.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.patient.patient.vo.CheckAccountVO;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.patinent.dao.PatinmentDao;
import cn.honry.patinent.patinent.service.PatinentService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

@Service("patinentService")
@Transactional
@SuppressWarnings({ "all" })
public class PatinentServiceImpl implements PatinentService{
		@Resource
		private PatinmentDao patinmentDao;
		
		@Resource
		private RedisUtil redis;
		/**
		 * 查询数据
		 * @date 2015-06-02
		 * @author sgt
		 * @version 1.0
		 * @return
		 */
		@Override
		public List<PatientIdcardVO> listPatient(PatientIdcardVO vo, String page,
				String rows) {
			return patinmentDao.listPatient(vo, page, rows);
		}
		/**
		 * 查询数据,第二版,用next标注
		 * @date 2015-12-23
		 * @author zpty
		 * @version 1.0
		 * @return
		 */
		@Override
		public List<PatientIdcardVO> listPatientNext(PatientIdcardVO vo,String idcardId) {
			return patinmentDao.listPatientNext(vo,idcardId);
		}
		/**
		 * 服务于就诊卡管理  多条数据时使用病历卡查询
		 * @date 2016年9月28日17:45:16
		 * @author GH
		 * @version 1.0
		 * @return
		 */
		@Override
		public List<PatientIdcardVO> listPatientByMedicalrecord(String idcardId) {
			return patinmentDao.listPatientByMedicalrecord(idcardId);
		}
		/**
		 * 查询数据总数
		 * @date 2015-06-02
		 * @author sgt
		 * @version 1.0
		 * @return
		 */	
		@Override
		public int getPatientCount(PatientIdcardVO vo) {
			return patinmentDao.getPatientCount(vo);
		}
		/**
		 * 修改
		 * @date 2015-06-02
		 * @author sgt
		 * @version 1.0
		 * @return
		 */	
		@Override
		public void edit(Patient patient) {
			patinmentDao.edit(patient);
		}
		/**
		 * 删除
		 * @date 2015-06-02
		 * @author sgt
		 * @version 1.0
		 * @return
		 */	
		@Override
		public void delete(Patient patient) {
			patinmentDao.delete(patient);
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
			return patinmentDao.queryById(patient);
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
			if(StringUtils.isBlank(entity.getId())){//保存		
				entity.setId(null);
				entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId());
				entity.setCreateTime(new Date());
			}else{//修改
				entity.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				entity.setUpdateTime(new Date());
			}
			String pyWb = patinmentDao.getSpellCode(entity.getPatientName());
			if(pyWb!=null&&pyWb.contains("$")){
				String pw[] = pyWb.split("\\$");
				entity.setPatientPinyin(pw[0]);
				entity.setPatientWb(pw[1]);
			}
			patinmentDao.save(entity);
			if(StringUtils.isBlank(entity.getId())){
				OperationUtils.getInstance().conserve(null, "就诊卡管理", "INSERT_INTO", "T_PATIENT", OperationUtils.LOGACTIONINSERT);
			}else{
				OperationUtils.getInstance().conserve(entity.getId(), "就诊卡管理", "UPDATE", "T_PATIENT", OperationUtils.LOGACTIONUPDATE);
			}
		}
		@Override
		public void removeUnused(String id) {
			patinmentDao.del(id, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			OperationUtils.getInstance().conserve(id, "就诊卡管理", "UPDATE", "T_PATIENT", OperationUtils.LOGACTIONUPDATE);
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
			return patinmentDao.get(id);
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
			return patinmentDao.gethzid(id,type,typeno);
		}
		@Override
		public boolean checkHandBook(String handBook,String patientId) {
			return patinmentDao.checkHandBook(handBook,patientId);
		}
		@Override
		public CheckAccountVO checkAllAccount(String medicalId) {
			return patinmentDao.checkAllAccount(medicalId);
		}
		@Override
		/** 
		 * @Description： 保存患者信息，保存挂号信息
		 * @Author：tcj 
		 */
		public RegisterInfo savePatient(Patient patient) {
			patient.setId(null);
			String str = patinmentDao.getSpellCode(patient.getPatientName());
			int index=str.indexOf("$");
			String pinyin=str.substring(0,index);
			String wb=str.substring(index+1);
			patient.setPatientPinyin(pinyin);
			patient.setPatientWb(wb);
			patient.setHospitalId(HisParameters.CURRENTHOSPITALID.toString());//所属医院
			patient.setAreaCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getAreaCode());//所属院区
			Patient p = patinmentDao.savePatient(patient);
			
			RegisterInfo info = new RegisterInfo();
			info.setId(null);
			info.setPatientId(p);
			info.setMidicalrecordId(p.getMedicalrecordId());
			
			patinmentDao.save(info);
			
			
			
			return info;
		}
		@Override
		public Patient queryByMedicalrecordId(String medicalrecordId) {
			return patinmentDao.queryByMedicalrecordId(medicalrecordId);
		}
		
		/**
		 * 根据员工工号查询员工
		 * zpty 20160324
		 * @param jobNo
		 * @return
		 */
		@Override
		public SysEmployee findEmpByjobNo(String jobNo) {
			List<SysEmployee> employeeList = patinmentDao.findEmpByjobNo(jobNo);
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
			List<BusinessContractunit> list = (List<BusinessContractunit>) redis.get("contractunit_queryAll");
			if(list!=null){
				return list;
			}
			return patinmentDao.queryUnitCombobox();
		}
}
