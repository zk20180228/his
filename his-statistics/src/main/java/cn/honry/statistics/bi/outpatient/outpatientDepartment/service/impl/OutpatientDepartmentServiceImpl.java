package cn.honry.statistics.bi.outpatient.outpatientDepartment.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.outpatient.outpatientDepartment.dao.OutpatientDepartmentDao;
import cn.honry.statistics.bi.outpatient.outpatientDepartment.service.OutpatientDepartmentService;
@Service("outpatientDepartmentService")
public class OutpatientDepartmentServiceImpl implements
		OutpatientDepartmentService {
	@Autowired
	@Qualifier(value="outpatientDepartmentDao")
	private OutpatientDepartmentDao outpatientDepartmentDao;
	@Override
	public Map<String, Object> queryOutPatientDept(String[] depts,
			 String begin, String end, Integer rows,
			Integer page) {
		return outpatientDepartmentDao.queryOutPatientDept(depts,  begin, end, rows, page);
	}

}
