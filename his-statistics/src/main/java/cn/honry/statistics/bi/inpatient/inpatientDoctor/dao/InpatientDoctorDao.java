package cn.honry.statistics.bi.inpatient.inpatientDoctor.dao;

import java.util.Map;

public interface InpatientDoctorDao {
	/**
	 * 
	 * 
	 * <p>Pc端住院医生工作量统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月31日 下午5:33:37 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月31日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date:
	 *
	 */
	public void pcDoctorWorkTotal(String date);
	
	/**
	 * 
	 * 
	 * <p> Pc端住院医生工作量查询</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月1日 上午10:27:24 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月1日 上午10:27:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param depts
	 * @param doctors
	 * @param begin
	 * @param end
	 * @param rows
	 * @param page
	 * @return:
	 *
	 */
	Map<String,Object> queryInPatientDoc(String[] depts,
			String[] doctors,String begin,String end, Integer rows, Integer page);
}
