package cn.honry.statistics.bi.inpatient.inpatientDepartment.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.inpatient.inpatientDepartment.dao.InpatientDepartmentDao;
import cn.honry.statistics.bi.inpatient.inpatientDepartment.service.InpatientDepartmentService;
@Service("inpatientDepartmentService")
public class InpatientDepartmentServiceImpl implements
		InpatientDepartmentService {
	@Autowired
	@Qualifier(value="inpatientDepartmentDao")
	private InpatientDepartmentDao inpatientDepartmentDao;
	
	public void setInpatientDepartmentDao(
			InpatientDepartmentDao inpatientDepartmentDao) {
		this.inpatientDepartmentDao = inpatientDepartmentDao;
	}

	@Override
	public Map<String, Object> queryInpatientDept(String[] depts, String begin,
			String end, Integer rows, Integer page) {
		return inpatientDepartmentDao.queryInpatientDept(depts, begin, end, rows, page);
	}

}
