package cn.honry.statistics.bi.inpatient.settlementAnalysis.service;

import cn.honry.base.bean.model.BiInpatientBalancehead;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface SettlementAnalysisService extends BaseService<BiInpatientBalancehead>{
	/**
	 * @param yearStart
	 * @param yearEnd
	 * @param quarterStart
	 * @param quarterEnd
	 * @param monthStart
	 * @param monthEnd
	 * @param dayStart
	 * @param dayEnd
	 * @param dimensionString
	 * @param dimensionOne
	 * @param dimensionTwo
	 * @param dimensionThree
	 * @return
	 */
	String querytSettlementDatagrid(DateVo datevo, String dimensionString,
			int dateType, String dimensionValue);
	
}
