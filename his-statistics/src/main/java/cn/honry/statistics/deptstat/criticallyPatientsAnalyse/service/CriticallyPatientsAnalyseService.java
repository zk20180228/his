package cn.honry.statistics.deptstat.criticallyPatientsAnalyse.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.vo.CriticallyPatientsVO;
import cn.honry.statistics.deptstat.operationProportion.vo.OperationProportionVo;
import cn.honry.utils.FileUtil;

public interface CriticallyPatientsAnalyseService {

	/**
	 * 
	 * <p>查询危重疑难患者数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:34:47 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:34:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<CriticallyPatientsVO>
	 *
	 */
	List<CriticallyPatientsVO> queryCriticallyPatients(String deptName,String begin,String end,String menuAlias) throws Exception;

	/**
	 * 
	 * <p>渲染科室</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:35:29 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:35:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Map<String,String>
	 * @throws Exception 
	 *
	 */
	Map<String, String> queryDeptMap() throws Exception;
	/**
	 * 
	 * <p>报表打印</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:35:52 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:35:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: FileUtil
	 *
	 */
	FileUtil exportList(List<CriticallyPatientsVO> list, FileUtil fUtil) throws Exception;
	/**
	 * 
	 * <p>初始化mongo数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:36:06 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:36:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 * @throws Exception 
	 *
	 */
	void initMongoDb() throws Exception;
	/**
	 * 
	 * <p>从mongo中查询数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:36:25 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:36:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<CriticallyPatientsVO>
	 *
	 */
	List<CriticallyPatientsVO> queryCriticallyPatientsFromMongo() throws Exception;

}
