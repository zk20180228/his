/**
 * 
 */
package cn.honry.statistics.bi.inpatient.patientsCost.service;

import java.util.List;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.util.dateVo.DateVo;

/**
 * 在院病人费用分析Service接口
 * @author luyanshou
 *
 */
public interface PatientsCostService {

	/**
	 * 获取住院下的科室信息
	 * 
	 */
	public List<SysDepartment> getDeptInfo();
	
	/**
	 * 查询统计费用名称列表
	 * 
	 */
	public List<MinfeeStatCode> getFeeName();
	
	/**
	 * 获取统计结果
	 * @param datevo 时间条件
	 * @param disease 疾病类别
	 * @param cost 费用类别
	 * @param dept 科室
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计;4-按日统计)
	 * @return
	 */
	public String getResults(DateVo datevo,String disease,String cost,String dept,int n);
}
