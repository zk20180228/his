package cn.honry.statistics.deptstat.deptBedsMessage.service;

import java.util.List;

import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;


public interface DeptBedsMessageService {

	/**  
	 * 
	 * 科室床位信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	List<DeptBedsMessageVo> queryDeptBedsMessage(String deptCode,String page,String rows,String menuAlias);
	/**  
	 * 
	 * 科室床位信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	int getTotalDeptBedsMessage(String deptCode,String menuAlias);
}
