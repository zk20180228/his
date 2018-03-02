package cn.honry.statistics.deptstat.outandinpatient.dao;

import java.util.List;

import cn.honry.statistics.deptstat.outandinpatient.vo.GetOrOutPatient;
import cn.honry.statistics.deptstat.outandinpatient.vo.InPatientVo;
import cn.honry.statistics.deptstat.outandinpatient.vo.OutPatientVo;


public interface OutAndInPatientDao {

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
	List<InPatientVo> queryinPatientMsg(List<String> tnL,String startTime, String endTime, String dept,String menuAlias,String page,String rows) throws Exception;
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
	int queryinPatientTotal(List<String> tnL,String startTime, String endTime, String dept,String menuAlias) throws Exception;
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
	List<OutPatientVo> queryOutPatientMsg(List<String> tnL, String startTime,
			String endTime, String dept,String menuAlias,String page,String rows) throws Exception;
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
	int queryOutPatientMsg(List<String> tnL, String startTime,
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
	List<GetOrOutPatient> queryGetInPatientMsg(List<String> tnL,
			String startTime, String endTime, String dept,String menuAlias,String page,String rows);
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
	int queryGetInPatientMsg(List<String> tnL,
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
	List<GetOrOutPatient> queryGetOutPatientMsg(List<String> tnL,
			String startTime, String endTime, String dept,String menuAlias,String page,String rows);
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
	int queryGetOutPatientMsg(List<String> tnL,
			String startTime, String endTime, String dept,String menuAlias);


}
