package cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.service;

import java.util.List;

import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.vo.HospitalizationMedicalCostsVo;

public interface HospitalizationMedicalCostsService extends BaseService<BiRegister> {

	
	/**
	 * 查询所有科室
	 * @author tuchuanjiang
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	/**
	 * 查询列表数据
	 * @param timeone
	 * @param timetwo
	 * @param nameString
	 * @param type
	 * 2016年8月11日16:54:37
	 * @return
	 */
	List<HospitalizationMedicalCostsVo> querytDatagrid(int timeone,String nameString);

	/**
	 *	查询统计图数据 
	 *2016年8月11日16:54:44
	 */
//	String queryStatDate(String typeString,String nameString);
}
