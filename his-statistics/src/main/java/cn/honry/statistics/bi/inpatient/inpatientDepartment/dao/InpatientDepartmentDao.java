package cn.honry.statistics.bi.inpatient.inpatientDepartment.dao;

import java.util.Map;


public interface InpatientDepartmentDao {
	/**
	 * 
	 * 
	 * <p>新添PC端住院科室工作量统计</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月31日 下午3:47:46 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月31日 下午3:47:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param depts
	 * @param begin
	 * @param end
	 * @param rows
	 * @param page
	 * @return:
	 *
	 */
	Map<String,Object> queryInpatientDept(String[] depts,String begin,String end,Integer rows,Integer page);
}
