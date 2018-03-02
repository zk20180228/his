package cn.honry.inner.outpatient.registration.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface RegistrationInInterDAO extends EntityDao<RegistrationNow>{
	/** 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Title: registerList 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Description: 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @param deptCode 科室code
	* @param no 病历号/门诊号/就诊卡号
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param parameter 挂号有效期（参数表中获得）
	* @param rows
	* @param page
	* @author dtl 
	* @date 2016年11月10日
	*/
	List<RegistrationNow> registerList(String deptCode, String no, String sTime,
			String eTime, String parameter, String rows, String page);
	
	/** 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Title: registerTotal 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Description: 门诊患者列表(根据科室，病历号/门诊号，挂号时间段) 
	* @param deptCode 科室code
	* @param no 病历号/门诊号/就诊卡号
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param parameter 挂号有效期（参数表中获得）
	* @author dtl 
	* @date 2016年11月10日
	*/
	int registerTotal(String deptCode, String no, String sTime, String eTime, String parameter);
	
	
	/** 保存或更新 号源数据
	* @param info 结束时间
	* @author GH 
	* @date 2016年12月6日14:55:36
	*/
	void saveDocSource(RegisterDocSource info);
	
	/**
	 * 根据排班数据id获取医生号源数据Id (判断医生号源数据是否存在以便于保存或更新)
	 * @author GH 
	 * @time 2016年12月7日09:49:28
	 * @param scheduleId 挂号排班id
	 */
	RegisterDocSource getDocSourceId(String scheduleId);

	/**
	 * 删除号源表信息，在排班信息删除时调用
	 * @param scheduleId 挂号数据id
	 */
	void delDocSource(String scheduleId);



	/**
	 * 根据门诊号
	 * @author zhangjin
	 * @time 2016年12月12日
	 * @param 门诊号（clinicCode）
	 */
	RegistrationNow getRegistration(String clinicCode);
}



