package cn.honry.inner.operation.itemList.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.inner.operation.itemList.dao.ItemListInInterDAO;
import cn.honry.inner.operation.itemList.service.ItemListInInterService;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;

@Service("itemListInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class ItemListInInterServiceImpl implements ItemListInInterService{

	@Autowired
	@Qualifier(value = "itemListInInterDAO")
	private ItemListInInterDAO itemListInInterDAO;
	
	/**  
	 * 
	 * 保存住院非药品明细信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月4日 下午6:54:00 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午6:54:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void save(InpatientItemListNow item) {
		itemListInInterDAO.save(item);
		
	}

	/**  
	 * 
	 * 更新住院非药品明细信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月4日 下午6:54:00 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午6:54:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void update(InpatientItemList seque) {
		itemListInInterDAO.update(seque);
		
	}

}
