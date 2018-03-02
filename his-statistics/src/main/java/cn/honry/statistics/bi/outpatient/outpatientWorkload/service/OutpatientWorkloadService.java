package cn.honry.statistics.bi.outpatient.outpatientWorkload.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.util.dateVo.DateVo;

public interface OutpatientWorkloadService extends BaseService<BiRegister>{

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
	String querytWordloadDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);

	/**
	 * 查询科室code、name
	 * @author tcj
	 * @return
	 */
	List<BiBaseOrganization> queryDeptForBiPublic(String deptType);


}
