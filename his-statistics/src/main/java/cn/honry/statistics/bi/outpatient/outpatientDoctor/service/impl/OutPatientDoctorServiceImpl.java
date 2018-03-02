package cn.honry.statistics.bi.outpatient.outpatientDoctor.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.bi.outpatient.outpatientDoctor.dao.OutPatientDoctorDao;
import cn.honry.statistics.bi.outpatient.outpatientDoctor.service.OutPatientDoctorService;
@Service("outPatientDoctorService")
public class OutPatientDoctorServiceImpl implements OutPatientDoctorService {
	@Autowired
	@Qualifier(value="outPatientDoctorDao")
	private OutPatientDoctorDao outPatientDoctorDao;
	
	public void setOutPatientDoctorDao(OutPatientDoctorDao outPatientDoctorDao) {
		this.outPatientDoctorDao = outPatientDoctorDao;
	}

	@Override
	public Map<String,Object> queryOutPatientDoc(String[] depts,
			String[] doctors, String begin, String end, Integer rows,
			Integer page) {
		return outPatientDoctorDao.queryOutPatientDoc(depts, doctors, begin, end, rows, page);
	}

}
