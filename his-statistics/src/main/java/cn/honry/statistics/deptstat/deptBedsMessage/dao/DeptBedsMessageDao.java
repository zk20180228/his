package cn.honry.statistics.deptstat.deptBedsMessage.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;

public interface DeptBedsMessageDao extends EntityDao<DeptBedsMessageVo>{
	/**  
	 * 
	 * 科室床位信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 List<DeptBedsMessageVo> queryDeptBedsMessage(String deptCode,String page,String rows,String menuAlias);
	/**  
	 * 
	 * 科室床位信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 int getTotalDeptBedsMessage(String deptCode,String menuAlias);
	
	
	/**  
	 * 
	 * 危重病历人数比例统计分析 从mongoDB查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 List<DeptBedsMessageVo> queryDeptBedsMessageForDB(String deptCode);
}
