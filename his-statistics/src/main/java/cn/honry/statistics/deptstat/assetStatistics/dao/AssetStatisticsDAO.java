package cn.honry.statistics.deptstat.assetStatistics.dao;

import java.util.List;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.AssetsDeviceUse;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.statistics.deptstat.assetStatistics.vo.AssetsDeviceVo;

public interface AssetStatisticsDAO{
	/**  
	 * 
	 * 资产分类list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<AssetsDevice> queryAssetsDevice(String officeName,String className,String classCode,String deviceName,String page, String rows) throws Exception;
	/**  
	 * 
	 * 资产分类Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Integer queryAssetsDeviceTotal(String officeName,String className,String classCode,String deviceName) throws Exception;
	/**  
	 * 
	 * 领用部门list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<AssetsDeviceUse> queryAssetsDeviceUse(String deptCode,String page, String rows) throws Exception;
	/**  
	 * 
	 * 领用部门Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Integer queryAssetsDeviceUseTotal(String deptCode) throws Exception; 
	/**  
	 * 
	 * 资产价值list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<AssetsDeviceVo> queryAssetsDeviceValue(String officeName,String className,String classCode,String deviceName,String page, String rows) throws Exception;
	/**  
	 * 
	 * 资产价值Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Integer queryAssetsDeviceValueTotal(String officeName,String className,String classCode,String deviceName) throws Exception; 
}
