package cn.honry.statistics.bi.outpatient.outpatientDepartment.dao;

import java.util.Map;

public interface OutpatientDepartmentDao {
	/**
	 * 
	 * 
	 * <p>新加PC门诊科室工作量统计</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月30日 下午2:37:47 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月30日 下午2:37:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param depts查询科室
	 * @param doctors查询医生
	 * @param menuAlias栏目名
	 * @param rows
	 * @param page
	 * @return:
	 *
	 */
	public Map<String,Object> queryOutPatientDept(String[] depts,String begin,String end,Integer rows,Integer page);
}
