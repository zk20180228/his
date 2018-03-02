package cn.honry.patinent.idcard.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.PatientIdcardChange;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.idcard.dao.IdcardDAO;
import cn.honry.patinent.idcard.service.IdcardService;
import cn.honry.utils.ShiroSessionUtils;

@Service("idcardService")
@Transactional
@SuppressWarnings({ "all" })
public class IdcardServiceImpl implements IdcardService{
	
	@Autowired
	@Qualifier(value = "patinentInnerService")
	private PatinentInnerService patinentInnerService;
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	
	/** 就诊卡数据库操作类 **/
	@Autowired
	@Qualifier(value = "idcardDAO")
	private IdcardDAO idcardDAO;
	/** 账户数据库操作类 **/
	//物理删除
	@Override
	public void removeUnused(String id) {
		idcardDAO.removeById(id);
	}

	@Override
	public PatientIdcard get(String id) {
		return idcardDAO.get(id);
	}
	@Override
	public void saveOrUpdate(PatientIdcard entity) {
		if(StringUtils.isNotBlank(entity.getId())){
			idcardDAO.update(entity);
		}else{
			entity.setId(null);
			idcardDAO.save(entity);
		}
	}
	@Override
	public void saveOrUpdate(PatientIdcard entity,Patient patient) {
		patinentInnerService.saveOrUpdate(patient);
		if(StringUtils.isBlank(entity.getId())){//保存		
			entity.setId(null);
			//20160323zpty前台操作事件和操作人员去掉了,这里直接加入系统时间和登录人员工号
			entity.setIdcardCreatetime(new Date());//操作时间
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			if(jobNo!=null){
				entity.setIdcardOperator(jobNo);//操作人员员工号
			}
			entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			entity.setCreateTime(new Date());			
		}else{//修改
			entity.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setUpdateTime(new Date());
		}
		idcardDAO.save(entity);
		if(StringUtils.isBlank(entity.getId())){
			OperationUtils.getInstance().conserve(null,"就诊卡管理","INSERT INTO","T_PATIENT_IDCARD",OperationUtils.LOGACTIONINSERT);
		}else{
			OperationUtils.getInstance().conserve(entity.getId(),"就诊卡管理","UPDATE","T_PATIENT_IDCARD",OperationUtils.LOGACTIONUPDATE);
		}
	}

	@Override
	public List<PatientIdcard> getPage(String page, String rows,PatientIdcard idcard) {
		return idcardDAO.getPage(idcard, page, rows);
	}

	@Override
	public int getTotal(PatientIdcard idcard) {
		return idcardDAO.getTotal(idcard);
	}

	@Override
	public void del(String id) {
		idcardDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(id,"就诊卡管理","UPDATE","T_PATIENT_IDCARD",OperationUtils.LOGACTIONDELETE);
	}
	@Override
	public List<PatientIdcard> findAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("del_flg", 0);
		List<PatientIdcard>  idcardList = idcardDAO.findByObjectProperty(params);
		return idcardList;
	}
	@Override
	public PatientIdcard queryById(String PatientIdcard) {
		return idcardDAO.queryById(PatientIdcard);
	}

	@Override
	public PatientIdcard queryByIdcardNo(String idcardNo) {
		return idcardDAO.queryByIdcardNo(idcardNo);
	}
	@Override
	public PatientIdcard getkh(String idcardNo) {
		return idcardDAO.getkh(idcardNo);
	}

	@Override
	public String checkIdcardNoVSMedicalNO(String idcardNo,String medicalrecordId,String idcardId) {
		return idcardDAO.checkIdcardVSMedicalNO(idcardNo,medicalrecordId,idcardId);
	}

	@Override
	public String checkIdcardName(String name, String sex, String birthday, String contact, String certificate, String number, String patientCitys,String pid) {
		return idcardDAO.checkIdcardName(name,sex,birthday,contact,certificate,number,patientCitys,pid);
	}
	
	@Override
	public boolean activateIdcard(String idcardId) {
		boolean flag = false;
		//通过就诊卡ID号查出对应的卡对象
		PatientIdcard idcard = idcardDAO.get(idcardId);
		if(idcard != null){
			idcard.setIdcardStatus(1);//1正常
			idcardDAO.update(idcard);
			OperationUtils.getInstance().conserve(idcardId, "就诊卡管理", "UPDATE", "T_PATIENT_IDCARD", OperationUtils.LOGACTIONUPDATE);
			flag = true;
		}
		return flag;
	}

	@Override
	public Map<String, String> getCardNoMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<PatientIdcard> list = idcardDAO.getAll();
		if(list!=null&&list.size()>0){
			for(PatientIdcard idcard : list){
				map.put(idcard.getId(), idcard.getIdcardNo());
			}
		}
		return map;
	}
	/**  
	 * @Description： 读卡查询信息
	 * @Author：wujiao
	 * @CreateDate：2016-3-23 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public PatientIdcard queryIdcadAllInfo(String idcardNo) {
		return idcardDAO.queryIdcadAllInfo(idcardNo);
	}
	
	/**
	 * @Description:执行挂失操作
	 * @Author：  lt
	 * @CreateDate： 2015-11-18
	 * @return void  
	 * @param zpty20160324从inpitaent包中移植过来
	 * @version 1.0
	**/
	@Override
	public InpatientAccount queryByIdcardId(String idcardId){
		return idcardDAO.queryByIdcardId(idcardId);
	}	
	
	@Override
	public void inpatientAccountsaveOrUpdate(InpatientAccount entity) {
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateUser("");
			entity.setCreateDept("");
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "住院账户管理", "INSERT_INTO", "T_INPATIENT_ACCOUNT", OperationUtils.LOGACTIONINSERT);
		}else{
			entity.setUpdateUser("");
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "住院账户管理", "UPDATE", "T_INPATIENT_ACCOUNT", OperationUtils.LOGACTIONUPDATE);
		}
		idcardDAO.save(entity);
	}
	
	/**
	 * 根据员工工号查询员工
	 * zpty 20160324
	 * @param jobNo
	 * @return
	 */
	@Override
	public SysEmployee findEmpByjobNo(String jobNo) {
		List<SysEmployee> employeeList = idcardDAO.findEmpByjobNo(jobNo);
		return employeeList.size()>0?employeeList.get(0):new SysEmployee();
	}
	
	/**
	 * @Description:通过就诊卡ID查找就诊卡
	 * @Author：  zpty
	 * @CreateDate： 2016-03-27
	 * @return void  
	 * @version 1.0
	**/
	@Override
	public PatientIdcard queryOldIdcardById(String idcardId){
		return idcardDAO.queryOldIdcardById(idcardId);
	}	
	
	/**
	 * @Description:储存就诊卡变更数据
	 * @Author：  zpty
	 * @CreateDate： 2016-3-27
	 * @version 1.0
	**/
	@Override
	public void changeIdcardsaveOrUpdate(PatientIdcardChange entity) {
		entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		entity.setCreateTime(new Date());			
		OperationUtils.getInstance().conserve(null, "就诊卡变更表", "INSERT_INTO", "T_PATIENT_IDCARDCHANGE", OperationUtils.LOGACTIONINSERT);
		idcardDAO.save(entity);
	}
	
	/**
	 * @Description:根据病历号获取住院账户信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return   
	 * @return InpatientAccount  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	@Override
	public InpatientAccount queryByMedical(String string) {
		return idcardDAO.queryByMedical(string);
	}
	
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @return void  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	@Override
	public void delByParentIdDetail(String id) {
		idcardDAO.delByParentIdDetail(id);
	}	
	
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @return void  
	 * @modify zpty20160327移植过来
	 * @version 1.0
	**/
	@Override
	public void delByParentIdRepaydetail(String id) {
		idcardDAO.delByParentIdRepaydetail(id);
	}	
	
	/**
	 * @Description:物理删除账号信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return 
	 * @modify zpty20160327移植过来  
	 * @version 1.0
	**/
	@Override
	public void delInpatientAccount(String id) {
		idcardDAO.delInpatientAccount(id);
	}
	
	/**
	 * @Description:通过就诊卡号查询患者账户
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @Modifier：zpty
	 * @ModifyDate：2016-4-07
	 * @ModifyRmk：  account包中引用过来
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return OutpatientAccount 实体
	**/
	@Override
	public OutpatientAccount queryByIdcardIdOut(String idcardId) {
		return idcardDAO.queryByIdcardIdOut(idcardId);
	}
	
	/**
	 * @Description:保存修改方法(门诊患者账户)
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @Modifier：zpty
	 * @ModifyDate：2016-4-07
	 * @ModifyRmk：  account包中引用过来
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return OutpatientAccount 实体
	**/
	@Override
	public void saveOrUpdateOut(OutpatientAccount entity) {
		if(StringUtils.isBlank(entity.getId())){//保存		
			entity.setId(null);
			entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			entity.setCreateTime(new Date());
			OperationUtils.getInstance().conserve(null, "患者账户管理", "INSERT_INTO", "T_OUTPATIENT_ACCOUNT", OperationUtils.LOGACTIONINSERT);
			idcardDAO.save(entity);
		}else{//修改
			entity.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(entity.getId(), "患者账户管理", "UPDATE", "T_OUTPATIENT_ACCOUNT", OperationUtils.LOGACTIONUPDATE);
			idcardDAO.update(entity);
		}
	}

}
