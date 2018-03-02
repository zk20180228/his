package cn.honry.statistics.deptstat.outandinpatient.service;

import java.util.List;

import cn.honry.statistics.deptstat.outandinpatient.vo.GetOrOutPatient;
import cn.honry.statistics.deptstat.outandinpatient.vo.InPatientVo;
import cn.honry.statistics.deptstat.outandinpatient.vo.OutPatientVo;



public interface OutAndInPatientService {
	/**
	 * <p>查询入院患者</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	List<InPatientVo> queryinPatientMsg(String startTime, String endTime, String dept,String menuAlias,String page,String rows) throws Exception;

	/**
	 * <p>查询入院患者总条数</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	int queryinPatientTotal(String startTime, String endTime, String dept,String menuAlias) throws Exception;
	/**
	 * <p>查询出院患者</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	List<OutPatientVo> queryOutPatientMsg(String startTime, String endTime,
			String dept,String menuAlias,String page,String rows) throws Exception;
	/**
	 * <p>查询出院患者总条数</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	int queryOutPatientMsg( String startTime,
			String endTime, String dept,String menuAlias) throws Exception;
	/**
	 * <p>查询转入院患者</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	List<GetOrOutPatient> queryGetInPatientMsg(String startTime,
			String endTime, String dept,String menuAlias,String page,String rows) throws Exception;

	/**
	 * <p>查询转入院患者总条数</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	int queryGetInPatientMsg(
			String startTime, String endTime, String dept,String menuAlias);
	
	/**
	 * <p>查询转出院患者</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	List<GetOrOutPatient> queryGetOutPatientMsg(String startTime,
			String endTime, String dept,String menuAlias,String page,String rows) throws Exception;
	/**
	 * <p>查询转出院患者</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月7日 上午11:49:59 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月7日 上午11:49:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<InPatientVo>
	 *
	 */
	int queryGetOutPatientMsg(String startTime, String endTime, String dept,String menuAlias);

}
