package cn.honry.assets.supplier.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsSupplier;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface SupplierDao extends EntityDao<AssetsSupplier>{
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	List<AssetsSupplier> querySupplier(AssetsSupplier Supplier);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	int getSupplierCount(AssetsSupplier Supplier);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsSupplier> findAll();
	/**
	 * 根据厂商名称查询模糊
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsSupplier> findbyName(String name);
	
	/***
	 * name 验证
	 * @Title: queryListByName 
	 * @author zpty
	 * @date 2017-11-14
	 * @param name
	 * @return List<AssetsSupplier>
	 * @version 1.0
	 */
	List<AssetsSupplier> queryListByName(String name);
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	List<AssetsSupplier> querySupplierforXR();
}
