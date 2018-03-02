package cn.honry.inner.outpatient.registration.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.service.BaseService;


public interface RegistrationInInterService extends BaseService<RegistrationNow>{
	/** 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Title: registerList 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Description: 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @param deptCode 科室code
	* @param no 病历号/门诊号/就诊卡号
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param rows
	* @param page
	* @author dtl 
	* @date 2016年11月10日
	*/
	List<RegistrationNow> registerList(String deptCode, String no, String sTime,
			String eTime, String rows, String page);
	
	/** 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Title: registerTotal 门诊患者列表(根据科室，病历号/门诊号，挂号时间段)
	* @Description: 门诊患者列表(根据科室，病历号/门诊号，挂号时间段) 
	* @param deptCode 科室code
	* @param no 病历号/门诊号/就诊卡号
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @author dtl 
	* @date 2016年11月10日
	*/
	int registerTotal(String deptCode, String no, String sTime, String eTime);
	
	
	/**
	 * 更新医生号源表
	 * @author GH 
	 * @time 2016年12月5日09:49:28
	 * @param scheduleInfo 保存的挂号表实体
	 */
	void saveDocSource(RegisterScheduleNow info);

	/**
	 * 根据排班数据id获取医生号源数据Id和已挂人数 (判断医生号源数据是否存在以便于保存或更新)
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
}
