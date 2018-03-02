package cn.honry.inner.operation.itemList.dao;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface ItemListInInterDAO extends EntityDao<InpatientItemListNow>{
	/**
	 *
	 * 通过处方号和处方流水号查询非药品明细
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月28日 下午9:05:03 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	InpatientItemList getitemListByNo(String recipeNo, Integer sequenceNo);
}
