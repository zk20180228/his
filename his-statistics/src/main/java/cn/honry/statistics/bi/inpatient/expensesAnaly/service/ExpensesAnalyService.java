package cn.honry.statistics.bi.inpatient.expensesAnaly.service;

import cn.honry.base.bean.model.BiInpatientFeeinfo;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface ExpensesAnalyService extends BaseService<BiInpatientFeeinfo>{
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
	String querytExpensesAnalyDatagrid(DateVo datevo,String dimensionString,int dateType,String dimensionValue);
}
