package cn.honry.statistics.bi.operation.operationDoctor.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.operation.operationDoctor.dao.OperationDoctorDao;
import cn.honry.statistics.bi.operation.operationDoctor.service.OperationDoctorService;
@Service("operationDoctorService")
public class OperationDoctorServiceImpl implements OperationDoctorService {
	@Autowired
	@Qualifier(value="operationDoctorDao")
	private OperationDoctorDao operationDoctorDao;
	
	public void setOperationDoctorDao(OperationDoctorDao operationDoctorDao) {
		this.operationDoctorDao = operationDoctorDao;
	}

	@Override
	public Map<String, Object> queryoperationPatientDoctor(String[] depts,
			String[] doctors, String begin, String end, Integer rows,
			Integer page) {
		return operationDoctorDao.queryoperationPatientDoctor(depts, doctors, begin, end, rows, page);
	}

}
