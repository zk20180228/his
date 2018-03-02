package cn.honry.statistics.bi.inpatient.dischargePerson.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.dischargePerson.dao.DischargePersonDao;
import cn.honry.statistics.bi.inpatient.dischargePerson.service.DischargePersonService;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.dao.OutpatientWorkloadDao;

@Service("dischargePersonService")
@Transactional
@SuppressWarnings({ "all" })
public class DischargePersonServiceImpl implements DischargePersonService{
	
	@Autowired
	@Qualifier(value = "dischargePersonDao")
	private DischargePersonDao dischargePersonDao;

	@Override
	public List<DischargePersonVo> queryDischargePersonList(String deptCode,
			String years, String quarters, String months, String days) {
		List<DischargePersonVo> lists=dischargePersonDao.queryDischargePersonList(deptCode, years,quarters,months,days);
		return lists;
	}

	@Override
	public List<SysDepartment> queryAllDept(String type) {
		return dischargePersonDao.queryAllDept(type);
	}

	@Override
	public BiInpatientInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(BiInpatientInfo arg0) {
		
	}

	@Override
	public List<DischargePersonVo> loadPersonList(String type) {
		List<DischargePersonVo> lists=dischargePersonDao.loadPersonList(type);
		
		return lists;
	}

	@Override
	public List<DischargePersonVo> loadBarPersonList(String type,
			String deptCode, String years, String quarters, String months,
			String days) {
		List<DischargePersonVo> lists=dischargePersonDao.loadBarPersonList(type, deptCode, years, quarters, months, days);
		return lists;
	}

	
}
