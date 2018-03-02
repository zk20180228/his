package cn.honry.oa.hospitalFileManager.dao;

import java.util.List;

import cn.honry.base.bean.model.EmployeeExtend;

public interface EmployeeDeptDao {

	/* 根据部门编号查询部门人员  */
	List<EmployeeExtend> getDeptMan(String deptCode);
}
