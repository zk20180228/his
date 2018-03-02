package cn.honry.statistics.bi.inpatient.peopleInHospital.service;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.inpatient.peopleInHospital.vo.PeopleInHospitalVo;
import cn.honry.statistics.util.dateVo.DateVo;

public interface PeopleInHospitalService extends BaseService<PeopleInHospitalVo>{

	/**
	 * 获取统计结果
	 * @param datevo 时间条件
	 * @param disease 疾病类别
	 * @param cost 费用类别
	 * @param dept 科室
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计;4-按日统计)
	 * @return
	 */
	String queryregisterid(DateVo datevo, String[] dimStringArray, int dateType, String dimensionValue);
}
