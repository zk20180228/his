package cn.honry.inner.drug.adjustPriceInfo.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：AdjustPriceInfoInInterDAO
 * @Description：  药品调价审核
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface AdjustPriceInfoInInterDAO extends EntityDao<DrugAdjustPriceInfo>{

	/**  
	 *  
	 * @Description：  药品调价方法-定时器执行
	 * @Author：zpty
	 * @CreateDate：2016-6-29
	 *
	 */
	List<DrugAdjustPriceInfo> getDrugChangeList();
	
}
