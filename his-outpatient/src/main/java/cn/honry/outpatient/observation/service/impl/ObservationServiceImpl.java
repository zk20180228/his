package cn.honry.outpatient.observation.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.outpatient.observation.dao.ObservationDao;
import cn.honry.outpatient.observation.service.ObservationService;
import cn.honry.outpatient.observation.vo.ComboxVo;
import cn.honry.outpatient.observation.vo.ObservationVo;
import cn.honry.outpatient.observation.vo.PatientRegisterVo;
import cn.honry.outpatient.observation.vo.PatientVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("observationService")
@Transactional
public class ObservationServiceImpl implements ObservationService {

	@Resource
	private  ObservationDao  observationDao;
	
	@Override
	public int saveObservation(ObservationVo observation)throws Exception {
		
		//id
		String id = UUID.randomUUID().toString().replace("-","");
		observation.setId(id);
		
		SysEmployee employee = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		//createUser
		observation.setCreateUser(employee.getJobNo());
		//createDept
		observation.setCreateDept(employee.getDeptCode()==null?"":employee.getDeptCode());
		//createTime
		observation.setCreateTime(DateUtils.formatDateY_M_D_H_M_S(new Date()));
		//updateUser
		observation.setUpdateUser(employee.getJobNo());
		//updateTime
		observation.setUpdateTime(DateUtils.formatDateY_M_D_H_M_S(new Date()));
		
		return observationDao.saveObservation(observation);
	}

	@Override
	public List<PatientVo> findPatientInfoByCardNo(String cardNo) {
		return observationDao.findPatientInfoByCardNo(cardNo);
	}

	@Override
	public List<PatientRegisterVo> findRegisterInfoByCardNo(String cardNo) {
		return observationDao.findRegisterInfoByCardNo(cardNo);
	}

	@Override
	public List<ComboxVo> findNurseCode() {
		return observationDao.findNurseCode();
	}

	@Override
	public List<ComboxVo> findDoctorCode() {
		return observationDao.findDoctorCode();
	}

}
