package cn.honry.statistics.deptstat.criticallyIllPatient.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.criticallyIllPatient.vo.CriticallyIllPatientVo;

public interface CriticallyIllPatientDao extends EntityDao<CriticallyIllPatientVo>{
	/**  
	 * 
	 * 病危患者信息统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月14日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月14日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	public List<CriticallyIllPatientVo> queryCriticallyIllPatient(String startTime,String endTime,String deptCode,String sex,String menuAlias,String page,String rows);
	
	/**  
	 * 
	 * 病危患者信息统计  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月14日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月14日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	public int getTotalCriticallyIllPatient(String startTime,String endTime,String deptCode,String sex, String menuAlias);
}
