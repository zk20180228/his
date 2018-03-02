package cn.honry.statistics.deptstat.outPatientMessage.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;

public interface OutPatientMessageDao extends EntityDao<OutPatientMessageVo>{
	/**  
	 * 
	 * 出院患者信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 List<OutPatientMessageVo> queryOutPatientMessage(List<String> tnL,String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
	/**  
	 * 
	 * 出院患者信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 int getTotalOutPatientMessage(List<String> tnL,String startTime,String endTime,String deptCode,String menuAlias);
	
	
	/**  
	 * 出院患者信息 从mongoDB查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 List<OutPatientMessageVo> queryOutPatientMessageForDB(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
}
