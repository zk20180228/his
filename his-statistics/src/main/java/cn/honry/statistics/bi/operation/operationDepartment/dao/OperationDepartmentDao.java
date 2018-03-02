package cn.honry.statistics.bi.operation.operationDepartment.dao;

import java.util.Map;

public interface OperationDepartmentDao {
	
	public Map<String, Object> queryoperationPatientDept(String[] depts, String begin, String end, Integer rows,
			Integer page);
}
