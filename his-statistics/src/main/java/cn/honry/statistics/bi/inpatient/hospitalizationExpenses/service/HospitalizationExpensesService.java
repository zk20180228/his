package cn.honry.statistics.bi.inpatient.hospitalizationExpenses.service;


import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface HospitalizationExpensesService extends BaseService<BiRegister>{
	/**
	 * 查询列表数据
	 * @param timeone
	 * @param timetwo
	 * @param nameString
	 * @param type
	 * @return
	 */
	String querytDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);

}
