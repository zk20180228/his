package cn.honry.statistics.deptstat.diagnosticRate.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.honry.statistics.deptstat.diagnosticRate.dao.DiagnosticRateDao;
import cn.honry.statistics.deptstat.diagnosticRate.vo.DiagnosticRateVo;

@Repository("diagnosticRateDao")
public class DiagnosticRateDaoImpl implements DiagnosticRateDao {

	@Override
	public List<DiagnosticRateVo> queryDiagnoSticRate(String beginTime,
			String endTime, String rows, String page, String menuAlias,
			String depts, String campus) {
		
		return null;
	}

}
