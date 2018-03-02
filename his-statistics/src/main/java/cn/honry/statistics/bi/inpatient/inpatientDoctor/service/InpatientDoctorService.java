package cn.honry.statistics.bi.inpatient.inpatientDoctor.service;

import java.util.Map;

public interface InpatientDoctorService {
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
