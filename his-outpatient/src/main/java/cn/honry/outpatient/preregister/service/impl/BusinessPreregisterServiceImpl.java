package cn.honry.outpatient.preregister.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.outpatient.register.vo.InfoInInterPatient;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.preregister.dao.BusinessPreregisterDAO;
import cn.honry.outpatient.preregister.service.BusinessPreregisterService;
import cn.honry.outpatient.preregister.vo.EmpScheduleVo;
import cn.honry.outpatient.preregister.vo.IdCardPreVo;
import cn.honry.outpatient.preregister.vo.RegInfoInInterVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("businessPreregisterService")
@Transactional
@SuppressWarnings({ "all" })
public class BusinessPreregisterServiceImpl implements BusinessPreregisterService{

	@Autowired
	@Qualifier(value = "registerInfoInInterDAO")
	private RegisterInfoInInterDAO registerInfoInInterDAO;
	
	@Autowired
	private KeyvalueInInterDAO keyvalueInInterDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterInfo get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(RegisterInfo entity) {
		
	}
	
	@Autowired
	@Qualifier(value = "businessPreregisterDAO")
	private BusinessPreregisterDAO businessPreregisterDAO;
	
	/**  
	 *  
	 * @Description： 数据源
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public SysEmployee queryInfoPeret(String userId) {
		SysEmployee sysEmployee = businessPreregisterDAO.queryInfoPeret(userId);
		return sysEmployee;
	}
	/**  
	 * @Description：查询排班信息（剩余号数）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegInfoInInterVo> findInfoList(String id) {
		List<RegInfoInInterVo> lst = businessPreregisterDAO.findInfoList(id);
		return lst;
	}
	/**  
	 * @Description：  预约挂号（医生站）
	 * @Author：liudelin
	 * @CreateDate：2015-12-02
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public IdCardPreVo searchIdcard(String idcardNo) {
		IdCardPreVo idCardPreVo = businessPreregisterDAO.searchIdcard(idcardNo);
		return idCardPreVo;
	}
	/**  
	 * @Description：添加&修改
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public void editPreregisterVo(RegisterPreregisterNow preregister) {
		String deptName = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();
		String name = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getName();
		if(StringUtils.isBlank(preregister.getId())){//保存
			Integer value = keyvalueInInterDAO.getVal("RegisterPreregister");
			preregister.setPreregisterNo(DateUtils.getStringYear()+DateUtils.getStringMonth()+DateUtils.getStringDay()+count(6,value.toString().length())+value);
			preregister.setId(null);
			preregister.setCreateTime(new Date());
			preregister.setDel_flg(0);
			preregister.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			preregister.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			preregister.setCreateTime(DateUtils.getCurrentTime());
			preregister.setPreregisterDeptName(deptName);
			preregister.setPreregisterExpertName(name);
			if("年".equals(preregister.getPreregisterAgeunit())){
				preregister.setPreregisterAgeunit("岁");
			}
			if(preregister.getStop_flg()==null){
				preregister.setStop_flg(0);
			}
			OperationUtils.getInstance().conserve(null,"预约挂号","INSERT INTO","T_REGISTER_PREREGISTER_NOW",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			preregister.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			preregister.setUpdateTime(new Date());
			OperationUtils.getInstance().conserve(preregister.getId(),"预约挂号","UPDATE","T_REGISTER_PREREGISTER_NOW",OperationUtils.LOGACTIONUPDATE);

		}
		businessPreregisterDAO.save(preregister);
	}
	public String count(Integer length,Integer dlen){
		String cV = "";
		Integer wS = length - dlen;
		if(wS>0){
			for (int i = 1; i <= wS; i++) {
				cV += "0";
			}
		}
		return cV;
	}
	/**  
	 * @Description：级别转换
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public RegisterGrade findGradeEdit(String gradeId) {
		RegisterGrade grade = businessPreregisterDAO.findGradeEdit(gradeId);
		return grade;
	}
	/**  
	 * @Description：添加&修改
	 * @Author：ldl
	 * @CreateDate：2015-12-4
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InfoInInterPatient queryRegisterInfo(String idcardNo) {
		InfoInInterPatient infoPatient = registerInfoInInterDAO.queryRegisterInfo(idcardNo);
		return infoPatient;
	}
	/**  
	 * @Description：  通过预约午别带入开始结束时间
	 * @Author：wujiao
	 * @CreateDate：2016-1-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public Map<String, String> queryAll(String time, String gradeid,String deptid,String middy) {
		RegisterScheduleNow schList=businessPreregisterDAO.queryAll(time,gradeid,deptid,middy);
		Map<String,String> timeMap = new HashMap<String, String>();
		if(schList!=null){
			timeMap.put("resMsg", "success");
			timeMap.put("startTime",schList.getStartTime());
			timeMap.put("endTime",schList.getEndTime());
			timeMap.put("date",new SimpleDateFormat("yyyy-MM-dd").format(schList.getDate()));
			timeMap.put("id",schList.getId());
		}else{
			timeMap.put("resMsg", "error");
		}
		return timeMap;
	}

	@Override
	public Map<String, String> queryPreInfo(String dates, String idCardno,String empId, String midday) {
		Map<String,String> map = new HashMap<String, String>();
		PatientIdcard patientIdcard = businessPreregisterDAO.queryPatientIdcard(idCardno);
		idCardno = patientIdcard.getIdcardNo();
		RegisterPreregisterNow preregister = businessPreregisterDAO.queryPreInfo(dates,idCardno,empId,midday);
		if(StringUtils.isBlank(preregister.getId())){
			map.put("resMsg", "success");
		}else{
			map.put("resMsg", "error");
		}
		return map;
	}

	@Override
	public EmpScheduleVo getEmpee(String empId) {
		
		return businessPreregisterDAO.getEmpee(empId);
	}
}
