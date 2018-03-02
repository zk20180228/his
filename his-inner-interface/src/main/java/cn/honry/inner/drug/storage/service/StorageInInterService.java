package cn.honry.inner.drug.storage.service;

import java.util.List;

import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

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
public interface StorageInInterService  extends BaseService<DrugStorage>{

	List<TreeJson> findTree(int parseInt);
	
}
