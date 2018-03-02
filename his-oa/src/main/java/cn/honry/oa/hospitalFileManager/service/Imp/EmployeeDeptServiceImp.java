package cn.honry.oa.hospitalFileManager.service.Imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.EmployeeExtend;
import cn.honry.oa.hospitalFileManager.dao.EmployeeDeptDao;
import cn.honry.oa.hospitalFileManager.dao.FileManageDao;
import cn.honry.oa.hospitalFileManager.service.EmployeeDeptService;

@Service("employeeDeptService")
public class EmployeeDeptServiceImp implements EmployeeDeptService{

	@Autowired
	@Qualifier(value = "employeeDeptDao")
	private EmployeeDeptDao employeeDeptDaoImp;
	
	//根据部门编号查询部门内部人员
	@Override
	public List<EmployeeExtend> getDeptMan(String deptCode) {
		// TODO Auto-generated method stub
		return employeeDeptDaoImp.getDeptMan(deptCode);
	}

}
