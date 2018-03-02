package cn.honry.inner.operation.itemList.service;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;

public interface ItemListInInterService {

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
	void save(InpatientItemListNow item);

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
	void update(InpatientItemList seque);

}
