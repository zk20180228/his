package cn.honry.statistics.bi.operation.operationDepartment.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.operation.operationDepartment.dao.OperationDepartmentDao;
import cn.honry.statistics.bi.operation.operationDepartment.service.OperationDepartmentService;
@Service("operationDepartmentService")
public class OperationDepartmentServiceImpl implements
		OperationDepartmentService {
	@Autowired
	@Qualifier(value="operationDepartmentDao")
	private OperationDepartmentDao operationDepartmentDao;
	
	public void setOperationDepartmentDao(
			OperationDepartmentDao operationDepartmentDao) {
		this.operationDepartmentDao = operationDepartmentDao;
	}

	@Override
	public Map<String, Object> queryoperationPatientDept(String[] depts, String begin,
			String end, Integer rows, Integer page) {
		return operationDepartmentDao.queryoperationPatientDept(depts, begin, end, rows, page);
	}

}
