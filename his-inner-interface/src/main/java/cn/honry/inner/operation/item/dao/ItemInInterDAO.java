package cn.honry.inner.operation.item.dao;

import cn.honry.base.bean.model.OperationItem;
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
public interface ItemInInterDAO extends EntityDao<OperationItem>{
	//修改
	void upda(OperationItem item);
	//获取患者的手术信息
	String getname(String id);

}
