package cn.honry.inner.outpatient.registration.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.outpatient.registration.dao.RegistrationInInterDAO;
import cn.honry.inner.outpatient.registration.service.RegistrationInInterService;
import cn.honry.inner.outpatient.schedule.dao.ScheduleInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.utils.ShiroSessionUtils;

@Service("registrationInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class RegistrationInInterServiceImpl implements RegistrationInInterService{

	@Autowired
	@Qualifier(value = "registrationInInterDAO")
	private RegistrationInInterDAO registrationInInterDAO;
	
	/** 
	* @Fields parameterInnerDAO : 参数接口 
	*/ 
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/** 
	 * 挂号接口
	 */ 
	@Autowired
	@Qualifier(value = "scheduleInInterDAO")
	private ScheduleInInterDAO scheduleInInterDAO;
	
	@Override
	public RegistrationNow get(String arg0) {
		return registrationInInterDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(RegistrationNow arg0) {
		
	}

	@Override
	public List<RegistrationNow> registerList(String deptCode, String no,
			String sTime, String eTime, String rows, String page) {
		String parameter = parameterInnerDAO.getParameterByCode("infoTime");
		return registrationInInterDAO.registerList(deptCode, no, sTime, eTime, parameter, rows, page);
	}

	@Override
	public int registerTotal(String deptCode, String no, String sTime,
			String eTime) {
		String parameter = parameterInnerDAO.getParameterByCode("infoTime");
		return registrationInInterDAO.registerTotal(deptCode, no, sTime, eTime, parameter);
	}

	@Override
	public void saveDocSource(RegisterScheduleNow info) {
		//获取挂号级别Map
		Map<String,String> map =scheduleInInterDAO.getRegisterGrade();
		SysDepartment s = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptCode = "";
		if(null!=s&&StringUtils.isNotBlank(s.getDeptCode())){
			deptCode = s.getDeptCode();//当前登录科室
		}
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//获取当前登录人
		RegisterDocSource docInfo=this.getDocSourceId(info.getId());
		docInfo.setScheduleId(info.getId());
		docInfo.setAppFlag(info.getAppFlag());//是否加号
		docInfo.setClinicCode(info.getClinic());//诊室
		docInfo.setClinicName(info.getScheduleClinicname());//诊室名称
		docInfo.setCreateDept(deptCode);
		docInfo.setCreateTime(new Date());
		docInfo.setCreateUser(userId);
		docInfo.setDeptCode(info.getScheduleWorkdept());//科室code
		docInfo.setDeptName(info.getScheduleDeptname());//科室
		docInfo.setEmployeeCode(info.getDoctor());//医生code
		docInfo.setEmployeeName(info.getScheduleDoctorname());//医生 
		docInfo.setGradeCode(info.getReggrade());//挂号级别code
		docInfo.setGradeName(map.get(info.getReggrade()));//挂号级别 汉字
		docInfo.setMiddayCode(info.getMidday().toString());//午别code
		docInfo.setMiddayName(info.getScheduleMiddayname());//午别 汉字
//		docInfo.setLimitSum(info.getLimit()==0?info.getSpeciallimit():info.getLimit()); //挂号总额  //(limit-preLimit)+(preLimit-午别预约数)
		docInfo.setLimitSum(info.getLimit()); //挂号总额  
		docInfo.setPeciallimitSum(info.getSpeciallimit());// 特诊限额
		docInfo.setIsStop(info.getIsStop());//是否停诊
		docInfo.setStopReason(info.getStoprEason());//停诊原因
		docInfo.setRegDate(info.getDate());//排班时间
		registrationInInterDAO.saveDocSource(docInfo);
	}

	@Override
	public RegisterDocSource getDocSourceId(String scheduleId) {
		return registrationInInterDAO.getDocSourceId(scheduleId);
	}

	@Override
	public void delDocSource(String scheduleId) {
		registrationInInterDAO.delDocSource(scheduleId);
	}

}	