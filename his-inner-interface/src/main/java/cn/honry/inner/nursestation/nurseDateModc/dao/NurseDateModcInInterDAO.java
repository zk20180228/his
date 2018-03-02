package cn.honry.inner.nursestation.nurseDateModc.dao;

import cn.honry.base.bean.model.DeptDateModc;
import cn.honry.base.dao.EntityDao;

public interface NurseDateModcInInterDAO extends EntityDao<DeptDateModc> {

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
	DeptDateModc findNurseDateModcByDeptId(String deptId);
	
}
