package cn.honry.statistics.deptstat.diagnosticRate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.statistics.deptstat.diagnosticRate.dao.DiagnosticRateDao;
import cn.honry.statistics.deptstat.diagnosticRate.service.DiagnosticRateService;
import cn.honry.statistics.deptstat.diagnosticRate.vo.DiagnosticRateVo;

@Service("diagnosticRateService")
public class DiagnosticRateServiceImpl implements DiagnosticRateService {
	@Autowired
	@Qualifier(value="diagnosticRateDao")
	private DiagnosticRateDao diagnosticRateDao;
	
	public void setDiagnosticRateDao(DiagnosticRateDao diagnosticRateDao) {
		this.diagnosticRateDao = diagnosticRateDao;
	}

	@Override
	public List<DiagnosticRateVo> queryDiagnoSticRate(String beginTime,
			String endTime, String rows, String page, String menuAlias,
			String depts, String campus) {
		return diagnosticRateDao.queryDiagnoSticRate(beginTime, endTime, rows, page, menuAlias, depts, campus);
	}

}
