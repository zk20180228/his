package cn.honry.oa.documentManage.dao;

import cn.honry.base.bean.model.SysDepartment;

public interface DepartmentDocDao {

	/* 获取科室名称*/
	SysDepartment getDeptName(String uploadDept);

	/* 获取科室编号*/
	SysDepartment getDeptCode(String uploadDept);

}
