package cn.honry.statistics.deptstat.patientDiseaseType.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.patientDiseaseType.vo.PatientDiseaseType;

public interface PatientDiseaseTypeDAO extends EntityDao<PatientDiseaseType>{
	/**  
	 * 
	 * 患者疾病类型统计分析list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:56:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:56:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PatientDiseaseType> queryPatientDisease(String deptCode,String sex,String startTime,String endTime,String page, String rows) throws Exception;
	/**  
	 * 
	 * 患者疾病类型统计分析total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月13日 下午8:58:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月13日 下午8:58:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Integer queryTotal(String deptCode,String sex,String startTime,String endTime) throws Exception;
	/**  
	 * 
	 * 未治愈ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PatientDiseaseType> queryIcdHealed(String deptCode,String sex,String startTime,String endTime) throws Exception;
	/**  
	 * 
	 * 未死亡ICD
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月14日 上午10:27:50 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月14日 上午10:27:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<PatientDiseaseType> queryIcdDeath(String deptCode,String sex,String startTime,String endTime) throws Exception;
}
