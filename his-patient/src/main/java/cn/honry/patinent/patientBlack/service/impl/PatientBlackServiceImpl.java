package cn.honry.patinent.patientBlack.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.patinent.patientBlack.dao.PatientBlackDAO;
import cn.honry.patinent.patientBlack.service.PatientBlackService;
import cn.honry.patinent.patinent.dao.PatinmentDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("patientBlackService")
@Transactional
@SuppressWarnings({ "all" })

public class PatientBlackServiceImpl implements PatientBlackService{
	@Autowired
	@Qualifier(value = "patientBlackDAO")
	private PatientBlackDAO patientBlackDAO;
	@Autowired
	@Qualifier(value = "patinmentDao")
	private PatinmentDao patinmentDao;
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public void saveOrUpdate(PatientBlackList entity) {
	}
	
	@Override
	public PatientBlackList get(String id) {
		return patientBlackDAO.get(id);
	}

	@Override
	public void save(PatientBlackList entity) {
		if(StringUtils.isNotEmpty(entity.getPatientId())){
			Patient patient=patinmentDao.get(entity.getPatientId());
			if(StringUtils.isNotEmpty(patient.getId())){
				entity.setPatient(patient);
			}
		}
		if(StringUtils.isEmpty(entity.getId())){
			entity.setId(null);
			entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId());
			entity.setCreateTime(DateUtils.getCurrentTime());
			patientBlackDAO.save(entity);
			OperationUtils.getInstance().conserve(null,"患者黑名单管理","INSERT INTO","T_PATIENT_BLACKLIST",OperationUtils.LOGACTIONINSERT);
		}else{
			PatientBlackList patientBlack = patientBlackDAO.get(entity.getId());
			patientBlack.setPatientId(entity.getPatientId());//患者姓名
			patientBlack.setMedicalrecordId(entity.getMedicalrecordId());//病历号
			patientBlack.setBlacklistType(entity.getBlacklistType());//类型
			patientBlack.setBlacklistStarttime(entity.getBlacklistStarttime());//有效开始
			patientBlack.setBlacklistEndtime(entity.getBlacklistEndtime());//有效结束
			patientBlack.setBlacklistIntoreason(entity.getBlacklistIntoreason());//进入原因
			patientBlack.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			patientBlack.setUpdateTime(new Date());
			patientBlackDAO.save(patientBlack);
			OperationUtils.getInstance().conserve(entity.getId(),"患者黑名单管理","UPDATE","T_PATIENT_BLACKLIST",OperationUtils.LOGACTIONUPDATE);
		}
	}

	@Override
	public int getTotal(PatientBlackList patientBlack) {
		return patientBlackDAO.getTotal(patientBlack);
	}

	@Override
	public List<PatientBlackList> getPage(String page, String rows, PatientBlackList patientBlack) {
		return patientBlackDAO.getPage(page, rows, patientBlack);
	}

	@Override
	public void del(String ids,String reason) {
		String[] split = ids.split(",");
		for (String string : split) {
			PatientBlackList list = patientBlackDAO.get(string);
			list.setBlacklistOutreason(reason);//退出原因
			list.setDel_flg(1);
			list.setDeleteTime(new Date());
			list.setDeleteUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			patientBlackDAO.update(list);
		}
		OperationUtils.getInstance().conserve(ids,"患者黑名单管理","UPDATE","T_PATIENT_BLACKLIST",OperationUtils.LOGACTIONDELETE);
	}

	
}
