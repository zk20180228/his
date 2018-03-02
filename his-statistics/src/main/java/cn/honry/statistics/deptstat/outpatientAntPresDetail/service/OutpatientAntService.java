package cn.honry.statistics.deptstat.outpatientAntPresDetail.service;

import java.util.Map;


public interface OutpatientAntService {
	/**
	 * 
	 * 
	 * <p>门诊抗菌药物处方比例预处理  </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月6日 下午6:58:43 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月6日 下午6:58:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param type: 统计类型 1 天  2 月 3 年
	 *
	 */
	public void init_MZKJYWCFBL(String begin,String end,Integer type);
	
	/**
	 * 
	 * 
	 * <p>从mongoDB中查询门诊抗菌药物处方比例 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月7日 上午9:36:49 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月7日 上午9:36:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param searchBegin 开始时间
	 * @param searchEnd 结束时间
	 * @param deptCodes 科室code
	 * @return:
	 *
	 */
	public Map<String,Object> queryList(String searchBegin,String searchEnd,String deptCodes,String menuAlias,String rows,String page);
}
