package cn.honry.oa.itemManage.dao;

import java.util.List;

import cn.honry.base.bean.model.TmItems;
import cn.honry.base.dao.EntityDao;

public interface ItemManageDao extends EntityDao<TmItems> {

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
	 * <p>根据type查找当前分类下最大的code</p>
	 * @Author: yuke
	 * @throws:type
	 *
	 */
	String getMaxCodeByType(Integer type);

	/** 
	 * <p>查找最大的type</p>
	 * @Author: yuke
	 *
	 */
	Integer getMaxType(Integer type);

	/** 
	 * <p>根据type查找分类名称</p>
	 * @Author: yuke
	 * @throws:type
	 *
	 */
	TmItems getTypeName(Integer type);

	/** 
	 * <p>根据id删除</p>
	 * @Author: yuke
	 * @throws:id
	 *
	 */
	void delItem(String id);

	/** 
	 * <p>根据type查找分类名称</p>
	 * @Author: yuke
	 * @throws:type
	 *
	 */
	void delType(Integer type,String parentId,String id);
	/**
	 * 根据级别查询分类
	 */
	List<TmItems> queryItems(String isParent,String type,String parentId);
	/**
	 * 查询分类数据
	 * 
	 */
	List<TmItems> queryTmItems(String id,String parentId,String path);
}
