package cn.honry.assets.supplier.service;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsSupplier;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface SupplierService  extends BaseService<AssetsSupplier> {
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
	 *  增加数据/修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	void saveOrupdata(AssetsSupplier supplier);
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	void delete(AssetsSupplier supplier);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsSupplier> findAll();
	
	/***
	 * 数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @param supplier
	 * @return String 标识，T & F
	 * @version 1.0
	 */
	String verification(AssetsSupplier supplier);
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
