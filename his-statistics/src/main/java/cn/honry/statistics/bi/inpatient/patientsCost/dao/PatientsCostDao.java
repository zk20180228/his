/**
 * 
 */
package cn.honry.statistics.bi.inpatient.patientsCost.dao;

import java.util.List;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.patientsCost.vo.PatientCostVo;
import cn.honry.statistics.util.dateVo.DateVo;

/**
 * 在院病人费用分析DAO接口
 * @author luyanshou
 *
 */
public interface PatientsCostDao {

	/**
	 * 查询住院下的科室信息
	 */
	public List<SysDepartment> getDept();
	
	/**
	 * 查询统计费用名称列表
	 * 
	 */
	public List<MinfeeStatCode> getFeeName();
	
	/**
	 * 查询非药品费用
	 * @param dept 科室
	 * @param feeName 统计费用名称
	 * @param datevo 时间vo
	 * @param dateType 统计方式
	 * @return
	 */
	public List<PatientCostVo> getItemCost(String dept,String feeName,DateVo datevo,int dateType);
	
	/**
	 * 查询药品费用
	 * @param dept 科室
	 * @param feeName 统计费用名称
	 * @param datevo 时间vo
	 * @param dateType 统计方式
	 * @return
	 */
	public List<PatientCostVo> getMedicineCost(String dept,String feeName,DateVo datevo,int dateType);
	
	/**
	 * 查询科室名称
	 * @param code
	 * @return
	 */
	public String getdeptName(String code);
}
