package cn.honry.statistics.bi.outpatient.outpatientDoctor.service;

import java.util.Map;


public interface OutPatientDoctorService {
	/**
	 * 
	 * 
	 * <p>新加PC门诊医生工作量统计</p>
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
	public Map<String,Object> queryOutPatientDoc(String[] depts,String[] doctors,String begin,String end,Integer rows,Integer page);
}
