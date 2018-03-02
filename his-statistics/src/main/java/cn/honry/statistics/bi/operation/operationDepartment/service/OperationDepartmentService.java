package cn.honry.statistics.bi.operation.operationDepartment.service;

import java.util.Map;

public interface OperationDepartmentService {
	public Map<String, Object> queryoperationPatientDept(String[] depts, String begin, String end, Integer rows,
			Integer page);
}
