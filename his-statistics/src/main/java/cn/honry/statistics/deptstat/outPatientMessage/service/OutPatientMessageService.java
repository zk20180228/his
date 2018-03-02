package cn.honry.statistics.deptstat.outPatientMessage.service;

import java.util.List;

import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;


public interface OutPatientMessageService {

	/**  
	 * 
	 * 出院患者信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	List<OutPatientMessageVo> queryOutPatientMessage(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
	/**  
	 * 
	 * 出院患者信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	int getTotalOutPatientMessage(String startTime,String endTime,String deptCode,String menuAlias);
}
