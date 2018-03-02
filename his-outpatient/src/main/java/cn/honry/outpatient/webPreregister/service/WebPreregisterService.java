package cn.honry.outpatient.webPreregister.service;

import java.util.List;

import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.service.BaseService;

public interface WebPreregisterService extends BaseService<RegisterPreregisterNow>{

	/**
	 * 根据科室编码和日期查询某科室下医生的排班信息
	 * @param deptCode 科室编码
	 * @param rq 日期
	 * @param firstResult 开始位置
	 * @param rows 每页显示的记录数
	 * @return
	 */
	public List<RegisterScheduleNow> getRegisterList(String deptCode,String rq,int firstResult,int rows)throws Exception;
	
	/**
	 * 保存预约挂号信息
	 * @param reg 预约挂号实体
	 */
	public String savePreregister(RegisterPreregisterNow reg);
	
	/**
	 * 获取预约编号
	 * @return
	 */
	public String getPreNo();
}
