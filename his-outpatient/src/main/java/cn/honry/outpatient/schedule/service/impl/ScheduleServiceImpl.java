 package cn.honry.outpatient.schedule.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.Clinic;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.clinic.dao.ClinicInInterDAO;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.inner.outpatient.registration.service.RegistrationInInterService;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.grade.dao.GradeDAO;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.outpatient.newInfo.vo.HospitalVo;
import cn.honry.outpatient.schedule.dao.ScheduleDAO;
import cn.honry.outpatient.schedule.service.ScheduleService;
import cn.honry.outpatient.scheduleModel.dao.ScheduleModelDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

@Service("scheduleService")
@Transactional
@SuppressWarnings({ "all" })
public class ScheduleServiceImpl implements ScheduleService{

	@Resource
	private RedisUtil redis;
	
	@Autowired
	@Qualifier(value = "scheduleDAO")
	private ScheduleDAO scheduleDAO;
	
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "scheduleModelDAO")
	private ScheduleModelDAO scheduleModelDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier(value = "gradeDAO")
	private GradeDAO gradeDAO;
	@Autowired
	@Qualifier(value = "clinicInInterDAO")
	private ClinicInInterDAO clinicInInterDAO;
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeInInterDAO;
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value = "registrationInInterService")
	private RegistrationInInterService registrationInInterService;
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "ationDAO")
	private RegistrationDAO ationDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterScheduleNow get(String id) {
		return scheduleDAO.get(id);
	}

	@Override
	public void saveOrUpdate(RegisterScheduleNow entity) {
		
	}

	@Override
	public List<RegisterScheduleNow> getPage(String page, String rows,RegisterScheduleNow registerSchedule) {
		return scheduleDAO.getPage(registerSchedule, page, rows);
	}

	@Override
	public int getTotal(RegisterScheduleNow registerSchedule) {
		return scheduleDAO.getTotal(registerSchedule);
	}

	@Override
	public String del(String id) {
		//判断： 如果已有人挂号 则不可删除 by GH
		if(StringUtils.isNotBlank(id)){
			String[] str = id.split(",");//可能删除多条数据
			for (String s : str) {
				RegisterDocSource info = registrationInInterService.getDocSourceId(s);
				if(info.getClinicSum()!=null&&info.getClinicSum()>0){
					return "clincSumExp";
				}else{
					scheduleDAO.del(s,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					//更新医生号源表
					registrationInInterService.delDocSource(s);
				}
			}
		}
		return "success";
	}

	@Override
	public RegisterScheduleNow getzj(String id,String time) {
		List<RegisterScheduleNow> list = scheduleDAO.getzj(id,time);
		RegisterScheduleNow schedule = new RegisterScheduleNow();
		int pre=0;
		int phone=0;
		int net=0;
		for(RegisterScheduleNow registerSchedule :list){
			pre+=registerSchedule.getPreLimit();
			phone+=registerSchedule.getPhoneLimit();
			net+=registerSchedule.getNetLimit();
			schedule.setPreLimit(pre);
			schedule.setPhoneLimit(phone);
			schedule.setNetLimit(net);
		}
		return schedule;
		
	}

	@Override
	public List<RegisterScheduleNow> findSchedule(String dept, String grade,String emplo) {
		return scheduleDAO.findSchedule(dept,grade,emplo);
	}

	/**  
	 *  
	 * @Description：  保存排班信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午04:08:09  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午04:08:09  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean saveOrUpdateSchedule(RegisterScheduleNow schedule)throws Exception {
		SysDepartment d = deptInInterDAO.get(schedule.getScheduleWorkdept());
		schedule.setScheduleWorkdept(d.getDeptCode());
		schedule.setDepartment(d.getDeptCode());
		Clinic clinic = clinicInInterDAO.get(schedule.getClinic());
		SysEmployee emp = employeeInInterDAO.getEmpByJobNo(schedule.getDoctor());
		BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("midday", schedule.getMidday().toString());
		if(StringUtils.isBlank(schedule.getId())){
			boolean isNotSave = scheduleDAO.findModelByDateAndDoctor(null,schedule.getDepartment(),schedule.getDate(),schedule.getDoctor(),schedule.getMidday());
			if(isNotSave){
				return false;
			}
			schedule.setId(null);
			if(StringUtils.isNotEmpty(schedule.getStoprEason())){
				String stopreason = "";
				stopreason = schedule.getStoprEason().replaceAll("\\r\\n", "</br>");
				stopreason = stopreason.replaceAll("\\n", "</br>");
				schedule.setStoprEason(stopreason);
			}
			if(StringUtils.isNotEmpty(schedule.getRemark())){
				String remark = "";
				remark = schedule.getRemark().replaceAll("\\r\\n", "</br>");
				remark = remark.replaceAll("\\n", "</br>");
				schedule.setRemark(remark);
			}
			if(schedule.getIsStop()==1){
				User doc = new User();
				doc.setId(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
				schedule.setStopDoctor(doc);
				schedule.setStopTime(new Date());
			}
			if(StringUtils.isNotBlank(schedule.getScheduleWorkdept())){
				HospitalVo hospitalVo = ationDAO.queryHospitalInfo(schedule.getScheduleWorkdept());
				//设置所属医院
				schedule.setHospitalId(hospitalVo.getHospital_id());
				//设置所属院区
				schedule.setAreaCode(hospitalVo.getDept_area_code());
			}
			schedule.setWeek(DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D(schedule.getDate())));
			schedule.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			schedule.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			schedule.setCreateTime(new Date());
			schedule.setScheduleDeptname(d.getDeptName());//所属科室名称
			schedule.setScheduleClinicname(clinic.getClinicName());//工作诊室名称
			schedule.setScheduleDoctorname(emp.getName());//医生姓名
			schedule.setScheduleMiddayname(dictionary.getName());//午别
			scheduleDAO.save(schedule);
			//把号源信息存入redies中
			String date = DateUtils.getDate();
			String d2 = DateUtils.formatDateY_M_D(schedule.getDate());
			if(date.equals(d2)){//当天数据
				String field=date+"-"+schedule.getScheduleWorkdept()+"-"+schedule.getDoctor()+"-"+schedule.getMidday();
				Boolean flag = redis.hexists("MZGH", field);
				if(!flag){
					redis.hincr("MZGH", field,Long.valueOf(schedule.getLimit()));
				}
			}
			OperationUtils.getInstance().conserve(null,"挂号排班信息","INSERT INTO","T_REGISTER_SCHEDULE_NOW",OperationUtils.LOGACTIONINSERT);
			
		}else{
			boolean isNotSave = scheduleDAO.findModelByDateAndDoctor(schedule.getId(),schedule.getDepartment(),schedule.getDate(),schedule.getDoctor(),schedule.getMidday());
			List<RegisterScheduleNow> list =  scheduleDAO.findOldSchedule(schedule.getId());
			schedule.setCreateTime(list.get(0).getCreateTime());
			if(isNotSave){
				return false;
			}
			if(schedule.getScheduleClass()==2){
				schedule.setLimit(null);
				schedule.setPreLimit(null);
				schedule.setPhoneLimit(null);
				schedule.setNetLimit(null);
				schedule.setSpeciallimit(null);
				schedule.setAppFlag(0);
				schedule.setIsStop(2);
				schedule.setStopDoctor(null);
				schedule.setStopTime(null);
				schedule.setStoprEason(null);
				schedule.setReggrade(null);
			}
			if(schedule.getIsStop()==1){
				User doc = new User();
				doc.setId(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
				schedule.setStopDoctor(doc);
				schedule.setStopTime(new Date());
			}else{
				schedule.setStopDoctor(null);
				schedule.setStopTime(null);
				schedule.setStoprEason(null);
			}
			if(StringUtils.isNotEmpty(schedule.getStoprEason())){
				String stopreason = "";
				stopreason = schedule.getStoprEason().replaceAll("\\r\\n", "</br>");
				stopreason = stopreason.replaceAll("\\n", "</br>");
				schedule.setStoprEason(stopreason);
			}
			if(StringUtils.isNotEmpty(schedule.getRemark())){
				String remark = "";
				remark = schedule.getRemark().replaceAll("\\r\\n", "</br>");
				remark = remark.replaceAll("\\n", "</br>");
				schedule.setRemark(remark);
			}
			schedule.setWeek(DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D(schedule.getDate())));
			schedule.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			schedule.setUpdateTime(new Date());
			schedule.setScheduleDeptname(d.getDeptName());//所属科室名称
			schedule.setScheduleClinicname(clinic.getClinicName());//工作诊室名称
			schedule.setScheduleDoctorname(emp.getName());//医生姓名
			schedule.setScheduleMiddayname(dictionary.getName());//午别
			String date = DateUtils.getDate();
			String d2 = DateUtils.formatDateY_M_D(schedule.getDate());
			
				if (schedule.getScheduleClass()==1){//挂号排班
					String field=schedule.getScheduleWorkdept()+"-"+schedule.getDoctor()+"-"+schedule.getMidday();
					if(!date.equals(d2)){//非当天数据
						//2017-02-17 改为预约限额
						Integer preLimit = schedule.getPreLimit();//新的预约限额
						Integer netLimit = scheduleDAO.getNetLimit(schedule.getId());//获取原来的网络限额(已改为预约限额)
						Integer n=preLimit-netLimit;
						if(n>0||(n<0 && netLimit>0)){
							Boolean flag = redis.hexists(d2, field);
							if(flag){
								redis.hincr(d2, field,n.longValue());
							}
						}
					}else{//当天数据
						Integer limit1 = schedule.getLimit();//新的挂号限额
						Integer limit2 = scheduleDAO.getLimit(schedule.getId());//获取原来的挂号限额
						Integer n= limit1-limit2;
						if(n>0||(n<0 && limit2>0)){
							field=date+"-"+field;
							Boolean flag = redis.hexists("MZGH", field);
							if(flag){
								redis.hincr("MZGH", field,n.longValue());
							}else{
								redis.hincr("MZGH", field,Long.valueOf(limit1));
							}
						}
					}
				}
				
			if(StringUtils.isNotBlank(schedule.getScheduleWorkdept())){
				HospitalVo hospitalVo = ationDAO.queryHospitalInfo(schedule.getScheduleWorkdept());
				//设置所属医院
				schedule.setHospitalId(hospitalVo.getHospital_id());
				//设置所属院区
				schedule.setAreaCode(hospitalVo.getDept_area_code());
			}
			scheduleDAO.save(schedule);
			OperationUtils.getInstance().conserve(schedule.getId(),"挂号排班信息","UPDATE","T_REGISTER_SCHEDULE_NOW",OperationUtils.LOGACTIONUPDATE);
		}
		//更新医生号源表 by GH
		//当排班类别为挂号排班
		if (schedule.getScheduleClass()==1) {
			Date scheduleDate=schedule.getDate();
			Date nowData = DateUtils.parseDateY_M_D(DateUtils.getDate());
			//判断  挂号数据时间，时间大于当天数据
			if(nowData.getTime()<=scheduleDate.getTime()){
				 registrationInInterService.saveDocSource(schedule);
			}
		}
		
		return true;
	}

	/**  
	 *  
	 * @Description：  排班同步
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午05:37:30  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午05:37:30  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int schedulSynch(String deptId) {
		int retVal = 0;
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String dId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String dayParam = null;
		dayParam =  parameterInnerDAO.getParameterByCode(HisParameters.SCHEDULEDAY);
		Integer dayP = Integer.parseInt(dayParam);
		Date nowData = DateUtils.parseDateY_M_D(DateUtils.getDate());
		SysDepartment dept = deptInInterDAO.get(deptId);
		for(int i=0;i<dayP;i++){
			Date date = DateUtils.addDay(nowData, i);
			int week = DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D(date));
			List<RegisterSchedulemodel> modelList = scheduleModelDAO.getScheduleByDeptAndWeek(dept.getDeptCode(),week,null);
			if(modelList!=null && modelList.size()>0){
				for(RegisterSchedulemodel model : modelList){
					RegisterScheduleNow schedule = new RegisterScheduleNow();
					schedule.setId(null);
					schedule.setScheduleClass(model.getModelClass());
					schedule.setScheduleWorkdept(model.getModelWorkdept());
					schedule.setModelId(model.getId());//模板编号
					schedule.setType(model.getModeType());
					schedule.setDepartment(model.getDepartment());
					schedule.setClinic(model.getClinic());
					schedule.setDoctor(model.getModelDoctor());
					schedule.setWeek(model.getModelWeek());
					schedule.setDate(date);
					schedule.setMidday(model.getModelMidday());
					schedule.setLimit(model.getModelLimit());
					schedule.setPreLimit(model.getModelPrelimit());
					schedule.setPhoneLimit(model.getModelPhonelimit());
					schedule.setNetLimit(model.getModelNetlimit());
					schedule.setSpeciallimit(model.getModelSpeciallimit());
					schedule.setStartTime(model.getModelStartTime());
					schedule.setEndTime(model.getModelEndTime());
					schedule.setAppFlag(model.getModelAppflag());
					schedule.setIsStop(2);
					schedule.setRemark(model.getModelRemark());
					schedule.setReggrade(model.getModelReggrade());
					schedule.setCreateUser(userId);
					schedule.setCreateDept(dId);
					schedule.setCreateTime(new Date());
					SysDepartment dd = deptInInterDAO.getByCode(model.getModelWorkdept());
					Clinic clinic = clinicInInterDAO.get(schedule.getClinic());
					SysEmployee emp = employeeInInterDAO.getEmpByJobNo(schedule.getDoctor());
					BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("midday", schedule.getMidday().toString());
					schedule.setScheduleDeptname(dd.getDeptCode());//所属科室名称
					schedule.setScheduleClinicname(clinic.getClinicName());//工作诊室名称
					schedule.setScheduleDoctorname(emp.getName());//医生姓名
					schedule.setScheduleMiddayname(dictionary.getName());//午别
					boolean isNotSave = scheduleDAO.findModelByDateAndDoctor(null,schedule.getDepartment(),schedule.getDate(),schedule.getDoctor(),schedule.getMidday());
					if(!isNotSave){
						scheduleDAO.save(schedule);
						retVal+=1;
						
						//把号源信息存入redies中
						String today = DateUtils.getDate();
						String d2 = DateUtils.formatDateY_M_D(schedule.getDate());
						if(today.equals(d2)){//当天数据
							String field=date+"-"+schedule.getScheduleWorkdept()+"-"+schedule.getDoctor()+"-"+schedule.getMidday();
							Boolean flag = redis.hexists("MZGH", field);
							if(!flag){
								redis.hincr("MZGH", field,Long.valueOf(schedule.getLimit()));
							}
						}
						
						//更新医生号源表 by GH
						//排班类别为挂号排班
						if (schedule.getScheduleClass()==1) {
							Date scheduleDate=schedule.getDate();
							//判断  挂号数据时间，时间大于当天数据
							if(nowData.getTime()<=scheduleDate.getTime()){
								 registrationInInterService.saveDocSource(schedule);
							}
						}
						
					}
				}
			}
		}
		return retVal;
	}

	/**  
	 *  
	 * @Description：  模板导入
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午05:37:30  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午05:37:30  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> schedulAddTemp(String ids,String deptId,String dateTime) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		int retSuc = 0;
		int retFai = 0;
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String dId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		Date date = DateUtils.parseDateY_M_D(dateTime);
		int week = DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D(date));
		List<RegisterSchedulemodel> modelList = scheduleModelDAO.getScheduleByidsAndDeptIdAndRq(ids,deptId,week);
		if(modelList!=null && modelList.size()>0){
			for(RegisterSchedulemodel model : modelList){
				RegisterScheduleNow schedule = new RegisterScheduleNow();
				schedule.setId(null);
				schedule.setScheduleClass(model.getModelClass());
				schedule.setScheduleWorkdept(model.getModelWorkdept());
				schedule.setModelId(model.getId());//模板编号
				schedule.setType(model.getModeType());
				schedule.setDepartment(model.getDepartment());
				schedule.setClinic(model.getClinic());
				schedule.setDoctor(model.getModelDoctor());
				schedule.setWeek(model.getModelWeek());
				schedule.setDate(date);
				schedule.setMidday(model.getModelMidday());
				schedule.setLimit(model.getModelLimit());
				schedule.setPreLimit(model.getModelPrelimit());
				schedule.setPhoneLimit(model.getModelPhonelimit());
				schedule.setSpeciallimit(model.getModelSpeciallimit());
				schedule.setNetLimit(model.getModelNetlimit());
				schedule.setStartTime(model.getModelStartTime());
				schedule.setEndTime(model.getModelEndTime());
				schedule.setAppFlag(model.getModelAppflag());
				schedule.setIsStop(2);
				schedule.setRemark(model.getModelRemark());
				schedule.setReggrade(model.getModelReggrade());
				schedule.setCreateUser(userId);
				schedule.setCreateDept(dId);
				schedule.setCreateTime(new Date());
				SysDepartment dd = deptInInterDAO.getByCode(model.getModelWorkdept());
				Clinic clinic = clinicInInterDAO.get(schedule.getClinic());
				SysEmployee emp = employeeInInterDAO.getEmpByJobNo(schedule.getDoctor());
				BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("midday", schedule.getMidday().toString());
				schedule.setScheduleDeptname(dd.getDeptCode());//所属科室名称
				schedule.setScheduleClinicname(clinic.getClinicName());//工作诊室名称
				schedule.setScheduleDoctorname(emp.getName());//医生姓名
				schedule.setScheduleMiddayname(dictionary.getName());//午别
				boolean isNotSave = scheduleDAO.findModelByDateAndDoctor(null,schedule.getDepartment(),schedule.getDate(),schedule.getDoctor(),schedule.getMidday());
				if(!isNotSave){
					scheduleDAO.save(schedule);
					retSuc+=1;
					
					//把号源信息存入redies中
					String today = DateUtils.getDate();
					String d2 = DateUtils.formatDateY_M_D(schedule.getDate());
					if(today.equals(d2)){//当天数据
						String field=dateTime+"-"+schedule.getScheduleWorkdept()+"-"+schedule.getDoctor()+"-"+schedule.getMidday();
						Boolean flag = redis.hexists("MZGH", field);
						if(!flag){
							redis.hincr("MZGH", field,Long.valueOf(schedule.getLimit()));
						}
					}
					
				}else{
					retFai+=1;
				}
			}
			if(modelList.size()==retSuc){//如果导入条数等于总数说明全部导入
				retMap.put("resMsg", "allSucc");
				retMap.put("resSuc", retSuc);
			}else if(modelList.size()==retFai){//如果未导入条数等于总数说明没有导入
				retMap.put("resMsg", "notImp");
			}else{//既有导入又有未导入
				retMap.put("resMsg", "partSucc");
				retMap.put("resSuc", retSuc);
				retMap.put("retFai", retFai);
			}
		}else{//没有可导入数据
			retMap.put("resMsg", "notExist");
		}
		return retMap;
	}
	/**  
	 *  
	 * @Description：  排班导入
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-28 下午02:12:16  
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-28 下午02:12:16  
	 * @ModifyRmk：  
	 * @version 2.0
	 *
	 */
	@Override
	public Map<String,Object> schedulAddTTemp(String ids,String deptId,String dateTime,String mdateTime) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		int retSuc = 0;
		int retFai = 0;
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysDepartment s = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String dId ="";
		if(null!=s&&StringUtils.isNotBlank(s.getDeptCode())){
			dId= s.getDeptCode();
		}
		Date date = DateUtils.parseDateY_M_D(mdateTime);
		Date datet= DateUtils.parseDateY_M_D(dateTime);
		int week = DateUtils.getWeekOfDay(DateUtils.formatDateY_M_D(date));
		SysDepartment dept = deptInInterDAO.get(deptId);
		List<RegisterSchedulemodel> modelList = scheduleModelDAO.getScheduleByidsAndDeptIdAndRq(ids,dept.getDeptCode(),week);
		if(modelList!=null && modelList.size()>0){
			for(RegisterSchedulemodel model : modelList){
				RegisterScheduleNow schedule = new RegisterScheduleNow();
				schedule.setId(null);
				schedule.setScheduleClass(model.getModelClass());
				schedule.setScheduleWorkdept(model.getModelWorkdept());
				schedule.setModelId(model.getId());//模板编号
				schedule.setType(model.getModeType());
				schedule.setDepartment(model.getDepartment());
				schedule.setClinic(model.getClinic());
				schedule.setDoctor(model.getModelDoctor());
				schedule.setWeek(model.getModelWeek());
				schedule.setDate(datet);
				schedule.setMidday(model.getModelMidday());
				schedule.setLimit(model.getModelLimit());
				schedule.setPreLimit(model.getModelPrelimit());
				schedule.setPhoneLimit(model.getModelPhonelimit());
				schedule.setSpeciallimit(model.getModelSpeciallimit());
				schedule.setNetLimit(model.getModelNetlimit());
				schedule.setStartTime(model.getModelStartTime());
				schedule.setEndTime(model.getModelEndTime());
				schedule.setAppFlag(model.getModelAppflag());
				schedule.setIsStop(2);
				schedule.setRemark(model.getModelRemark());
				schedule.setReggrade(model.getModelReggrade());
				schedule.setCreateUser(userId);
				schedule.setCreateDept(dId);
				schedule.setCreateTime(new Date());
				SysDepartment dd = deptInInterDAO.getByCode(model.getModelWorkdept());
				Clinic clinic = clinicInInterDAO.get(schedule.getClinic());
				SysEmployee emp = employeeInInterDAO.getEmpByJobNo(schedule.getDoctor());
				BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("midday", schedule.getMidday().toString());
				schedule.setScheduleDeptname(dd.getDeptName());//所属科室名称
				schedule.setScheduleClinicname(clinic.getClinicName());//工作诊室名称
				schedule.setScheduleDoctorname(emp.getName());//医生姓名
				schedule.setScheduleMiddayname(dictionary.getName());//午别
				boolean isNotSave = scheduleDAO.findModelByDateAndDoctor(null,schedule.getDepartment(),schedule.getDate(),schedule.getDoctor(),schedule.getMidday());
				if(!isNotSave){
					scheduleDAO.save(schedule);
					retSuc+=1;
					
					//把号源信息存入redies中
					String today = DateUtils.getDate();
					String d2 = DateUtils.formatDateY_M_D(schedule.getDate());
					if(today.equals(d2)){//当天数据
						String field=mdateTime+"-"+schedule.getScheduleWorkdept()+"-"+schedule.getDoctor()+"-"+schedule.getMidday();
						Boolean flag = redis.hexists("MZGH", field);
						if(!flag){
							if(schedule.getLimit()!=null){
								redis.hincr("MZGH", field,Long.valueOf(schedule.getLimit()));
							}
						}
					}
					
					//更新医生号源表 by GH
					//排班类别为挂号排班
					if (schedule.getScheduleClass()==1) {
						Date scheduleDate=schedule.getDate();
						Date nowData = DateUtils.parseDateY_M_D(DateUtils.getDate());
						//判断  挂号数据时间，时间大于当天数据
						if(nowData.getTime()<=scheduleDate.getTime()){
							 registrationInInterService.saveDocSource(schedule);
						}
					}
				}else{
					retFai+=1;
				}
				
			}
			if(modelList.size()==retSuc){//如果导入条数等于总数说明全部导入
				retMap.put("resMsg", "allSucc");
				retMap.put("resSuc", retSuc);
			}else if(modelList.size()==retFai){//如果未导入条数等于总数说明没有导入
				retMap.put("resMsg", "notImp");
			}else{//既有导入又有未导入
				retMap.put("resMsg", "partSucc");
				retMap.put("resSuc", retSuc);
				retMap.put("retFai", retFai);
			}
		}else{//没有可导入数据
			retMap.put("resMsg", "notExist");
		}
		return retMap;
	}
	
	
	
}




