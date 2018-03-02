package cn.honry.outpatient.webPreregister.dao;

import java.util.List;

import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.dao.EntityDao;

public interface WebPreregisterDao extends EntityDao<RegisterPreregisterNow>{

	/**
	 * 根据科室编码和日期查询某科室下医生的排班信息
	 * @param deptCode 科室编码
	 * @param rq 日期
	 * @param firstResult 开始位置
	 * @param rows 每页显示的记录数
	 * @return
	 */
	public List<RegisterScheduleNow> getRegisterList(String deptCode,String rq,int firstResult,int rows)throws Exception;
}
