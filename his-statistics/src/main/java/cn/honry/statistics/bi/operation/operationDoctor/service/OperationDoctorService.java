package cn.honry.statistics.bi.operation.operationDoctor.service;

import java.util.Map;

public interface OperationDoctorService {
	public Map<String, Object> queryoperationPatientDoctor(String[] depts,String[] doctors, String begin, String end, Integer rows,
			Integer page);
}
