package cn.honry.oa.itemManage.service;

import java.util.List;

import cn.honry.base.bean.model.TmItems;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

public interface ItemManageService  extends BaseService<TmItems>{

	/** 
	 * <p>查询所有父级</p>
	 * @Author: yuke
	 * @throws:
	 *
	 */
	List<TmItems> getAllType();

	/** 
	 * <p>根据分类查栏目</p>
	 * @Author: yuke
	 * @throws:分类type
	 *
	 */
	List<TmItems> getItemsByType(Integer type);

	/** 
	 * <p>根据id查找</p>
	 * @Author: yuke
	 * @throws:id
	 *
	 */
	List<TmItems> getItemsById(String id);

	/** 
	 * <p>保存</p>
	 * @Author: yuke
	 * @throws:tmItems
	 *
	 */
	void saveOrUpdateItem(TmItems tmItems);

	/** 
	 * <p>保存分类</p>
	 * @Author: yuke
	 * @throws:tmItems
	 *
	 */
	void saveOrUpdateItemType(TmItems tmItems);

	/** 
	 * <p>根据id删除</p>
	 * @Author: yuke
	 * @throws:id
	 *
	 */
	void delItem(String ids);

	/** 
	 * <p>根据type删除</p>
	 * @Author: yuke
	 * @throws:type
	 *
	 */
	void delType(Integer type,String parentId,String id);

	/** 
	 * <p>根据type查找分类名称</p>
	 * @Author: yuke
	 * @throws:type
	 *
	 */
	TmItems getTypeName(Integer type);
	/**
	 * 
	 */
	List<TreeJson> queryAllType(String id);
	/**
	 * 查询分类数据
	 * 
	 */
	List<TmItems> queryTmItems(String id,String parentId,String path);
}
