package cn.honry.inner.drug.storage.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.SysDepartment;
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
public interface StorageInInterDAO extends EntityDao<DrugStorage>{

	List<SysDepartment> findTree(int flag);

}
