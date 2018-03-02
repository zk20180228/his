package cn.honry.statistics.deptstat.materialAndEquipment.dao;

import java.util.List;

import cn.honry.base.bean.model.MatBaseRegInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.materialAndEquipment.vo.MaterialAndEquipmentVo;

public interface MaterialAndEquipmentDao extends EntityDao<MaterialAndEquipmentVo>{
	/**  
	 * 
	 * 物资设备统计查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月10日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月10日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 List<MaterialAndEquipmentVo> queryMaterialAndEquipment(String itemCode,String page,String rows,String queryStorage,String startTime,String endTime);
	/**  
	 * 
	 * 物资设备统计查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月10日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月10日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 int getTotalMaterialAndEquipment(String itemCode, String queryStorage,String startTime,String endTime);
	 /**  
		 * 
		 * 物资名称
		 * @Author: huzhenguo
		 * @CreateDate: 2017年11月15日 下午7:05:07 
		 * @Modifier: huzhenguo
		 * @ModifyDate: 2017年11月15日 下午7:05:07 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param:
		 * @throws:
		 * @return: 
		 *
		 */
		List<MaterialAndEquipmentVo> queryItemName(String page,String rows,String q);
		 /**  
		 * 
		 * 物资名称Total
		 * @Author: huzhenguo
		 * @CreateDate: 2017年11月15日 下午8:39:14 
		 * @Modifier: huzhenguo
		 * @ModifyDate: 2017年11月15日 下午8:39:14 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param:
		 * @throws:
		 * @return: 
		 *
		 */
		int getTotalItemName(String q);
		/**  
		 * 
		 * 仓库科室
		 * @Author: huzhenguo
		 * @CreateDate: 2017年11月15日 下午7:05:09 
		 * @Modifier: huzhenguo
		 * @ModifyDate: 2017年11月15日 下午7:05:09 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param:
		 * @throws:
		 * @return: 
		 *
		 */
		List<MaterialAndEquipmentVo> queryStorageCode();
	
}
