package cn.honry.statistics.bi.inpatient.inpatientDoctor.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.inpatient.inpatientDoctor.dao.InpatientDoctorDao;
import cn.honry.statistics.bi.inpatient.inpatientDoctor.service.InpatientDoctorService;
@Service("inpatientDoctorService")
public class InpatientDoctorServiceImpl implements InpatientDoctorService {
	@Autowired
	@Qualifier(value="inpatientDoctorDao")
	private InpatientDoctorDao inpatientDoctorDao;
	
	public void setInpatientDoctorDao(InpatientDoctorDao inpatientDoctorDao) {
		this.inpatientDoctorDao = inpatientDoctorDao;
	}

	@Override
	public Map<String, Object> queryInPatientDoc(String[] depts,
			String[] doctors, String begin, String end, Integer rows,
			Integer page) {
		return inpatientDoctorDao.queryInPatientDoc(depts, doctors, begin, end, rows, page);
	}

}
