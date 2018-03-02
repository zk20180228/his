package cn.honry.inner.nursestation.nurseDateModc.service;

import cn.honry.base.bean.model.DeptDateModc;
import cn.honry.base.service.BaseService;

public interface NurseDateModcInInterService extends BaseService<DeptDateModc>  {

	/**
	 * 根据部门id查询医嘱分解时间设置
	 * @author  lyy
	 * @createDate： 2015年12月26日 上午10:15:40 
	 * @modifier lyy
	 * @modifyDate：2015年12月26日 上午10:15:40  
	 * @modifyRmk：  
	 * @param deptId 获得登录部门
	 * @version 1.0
	 */
	public DeptDateModc findNurseDateModcByDeptId(String deptId);

}
