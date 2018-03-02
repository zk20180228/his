package cn.honry.statistics.bi.inpatient.settlementInHospital.service;

import cn.honry.base.bean.model.BiInpatientBalancelist;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface SettlementInHospitalService extends BaseService<BiInpatientBalancelist>{
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
	String querytInHospitalDatagrid(DateVo datevo, String dimensionString,
			int dateType, String dimensionValue);

}
