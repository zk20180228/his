package cn.honry.inner.inpatient.doctorDrugGrade.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.inner.inpatient.doctorDrugGrade.service.DoctorDrugGradeInInterService;

@Service("doctorDrugGradeInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class DoctorDrugGradeServiceInInterImpl implements DoctorDrugGradeInInterService {

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysDruggraDecontraStrank get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(SysDruggraDecontraStrank entity) {
		
	}
	
	
}
