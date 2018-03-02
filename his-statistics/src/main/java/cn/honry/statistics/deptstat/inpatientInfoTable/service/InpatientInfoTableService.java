package cn.honry.statistics.deptstat.inpatientInfoTable.service;

import java.util.List;

import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;


public interface InpatientInfoTableService {

	/**  
	 * 住院病人动态报表 
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	List<InpatientInfoTableVo> queryInpatientInfoTable(String startTime,String endTime, String deptCode, String page, String rows,String menuAlias);
	/**  
	 * 住院病人动态报表  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	int getTotalInpatientInfoTable(String startTime,String endTime,String deptCode, String page, String rows,String menuAlias);


}
