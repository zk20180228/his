package cn.honry.statistics.deptstat.criticallyPatientsAnalyse.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.deptstat.criticallyPatientsAnalyse.vo.CriticallyPatientsVO;

public interface CriticallyPatientsAnalyseDao  {
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
	 *
	 */
	List<SysDepartment> queryDeptList() throws Exception;
	
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
