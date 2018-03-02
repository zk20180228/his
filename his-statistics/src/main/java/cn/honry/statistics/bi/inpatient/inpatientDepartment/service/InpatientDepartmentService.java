package cn.honry.statistics.bi.inpatient.inpatientDepartment.service;

import java.util.Map;

public interface InpatientDepartmentService {
	public Map<String,Object> queryInpatientDept(String[] depts, String begin,
			String end, Integer rows, Integer page);
}
