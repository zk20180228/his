package cn.honry.oa.hospitalFileManager.service;

import java.util.List;

public interface EmployeeDeptService {

	//根据部门编号查询部门内部人员
	List<cn.honry.base.bean.model.EmployeeExtend> getDeptMan(String deptCode);

}
