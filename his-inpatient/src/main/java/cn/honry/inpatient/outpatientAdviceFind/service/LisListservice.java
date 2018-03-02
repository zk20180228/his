package cn.honry.inpatient.outpatientAdviceFind.service;

import java.util.List;

import cn.honry.inpatient.outpatientAdviceFind.vo.LisInspectionSample;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceDetailVo;


public interface LisListservice {

	/**  
	 * @Description：  门诊医嘱lis查询  左侧表格数据（分页）
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-10 下午14:17:01  
	 * @version 1.0
	 * @param id 
	 */
	List<LisInspectionSample> findLisInfoPage(String page, String rows,String queryName,String type, String id);

	/**  
	 * @Description：  门诊医嘱lis查询  左侧表格数据（总条数）
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-10 下午14:17:01  
	 * @version 1.0
	 */
	int findLisInfoTotal(String queryName,String type);

	

	/**  
	 * @Description：  门诊医嘱lis查询  右侧Detail表格数据（分页）
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-10 下午14:17:01
	 * @param  id  
	 * @version 1.0
	 */
	List<OutpatientAdviceDetailVo> findLisDetail(String id);
	


}
