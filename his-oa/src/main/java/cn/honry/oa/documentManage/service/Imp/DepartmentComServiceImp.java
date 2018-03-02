package cn.honry.oa.documentManage.service.Imp;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import antlr.collections.List;
import cn.honry.base.bean.model.DocManage;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.documentManage.dao.DepartmentDocDao;
import cn.honry.oa.documentManage.dao.DocumentManageDao;
import cn.honry.oa.documentManage.service.DepartmentDocService;
import cn.honry.oa.documentManage.service.DocumentManageService;

@Service("departmentDocService")
public class DepartmentComServiceImp implements DepartmentDocService{

	@Autowired
	@Qualifier(value = "departmentDoc")
	private DepartmentDocDao departmentDoc;

	/* 获取科室名称*/
	@Override
	public SysDepartment getDeptName(String uploadDept) {
		// TODO Auto-generated method stub
		return departmentDoc.getDeptName(uploadDept);
	}

	/* 获取科室编号*/
	@Override
	public SysDepartment getDeptCode(String uploadDept) {
		// TODO Auto-generated method stub
		return departmentDoc.getDeptCode(uploadDept);
	}

}
