package cn.honry.statistics.bi.operation.operationDoctor.dao;

import java.util.Map;

public interface OperationDoctorDao {
	public Map<String, Object> queryoperationPatientDoctor(String[] depts,String[] doctors, String begin, String end, Integer rows,
			Integer page);
}
