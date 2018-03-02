package cn.honry.statistics.deptstat.materialAndEquipment.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MatBaseRegInfo;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.materialAndEquipment.dao.MaterialAndEquipmentDao;
import cn.honry.statistics.deptstat.materialAndEquipment.service.MaterialAndEquipmentService;
import cn.honry.statistics.deptstat.materialAndEquipment.vo.MaterialAndEquipmentVo;

@Service("materialAndEquipmentService")
@Transactional
@SuppressWarnings({"all"})
public class MaterialAndEquipmentServiceImpl implements MaterialAndEquipmentService{
	@Autowired
	@Qualifier(value = "materialAndEquipmentDao")
	private MaterialAndEquipmentDao materialAndEquipmentDao;
	/**  
	 * 
	 * 物资设备统计查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月10日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月10日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<MaterialAndEquipmentVo> queryMaterialAndEquipment(String itemCode,String page,String rows,String queryStorage,String startTime,String endTime) {
		return materialAndEquipmentDao.queryMaterialAndEquipment(itemCode,page,rows,queryStorage, startTime, endTime);
	}
	
	@Override
	public int getTotalMaterialAndEquipment(String itemCode,String queryStorage,String startTime,String endTime) {
		return materialAndEquipmentDao.getTotalMaterialAndEquipment(itemCode,queryStorage,startTime,endTime);
	}
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
	@Override
	public List<MaterialAndEquipmentVo> queryItemName(String page,String rows,String q) {
		return materialAndEquipmentDao.queryItemName(page,rows,q);
	}
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
	@Override
	public List<MaterialAndEquipmentVo> queryStorageCode() {
		return materialAndEquipmentDao.queryStorageCode();
	}
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
	@Override
	public int getTotalItemName(String q) {
		return materialAndEquipmentDao.getTotalItemName(q);
	}

	
}
