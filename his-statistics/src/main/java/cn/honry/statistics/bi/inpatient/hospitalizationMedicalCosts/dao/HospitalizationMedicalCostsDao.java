package cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.dao;

import java.util.List;

import cn.honry.base.bean.model.BiInpatientFeeinfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.vo.HospitalizationMedicalCostsVo;

public interface HospitalizationMedicalCostsDao extends EntityDao<BiInpatientFeeinfo>{
	
	/**
	 * 查询所有科室
	 * @author Gengh
	 * @createDate：2016/7/15
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	

	List<HospitalizationMedicalCostsVo> querytDatagrid(String time,String nameString);

	/**
	 * 统计图数据
	 * @param dateType  同比OR环比
	 * @return
	 */
//	List<HospitalizationMedicalCostsVo> querytStatData(String timeString,String dateType,String nameString);
}
