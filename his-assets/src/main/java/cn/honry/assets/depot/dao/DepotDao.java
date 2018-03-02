package cn.honry.assets.depot.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DepotDao extends EntityDao<AssetsDepot>{
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	List<AssetsDepot> queryDepot(AssetsDepot Depot);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	int getDepotCount(AssetsDepot Depot);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDepot> findAll();
	/**
	 * 根据厂商名称查询模糊
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDepot> findbyName(String name);
	
	/***
	 * name 验证
	 * @Title: queryListByName 
	 * @author zpty
	 * @date 2017-11-14
	 * @param name
	 * @return List<AssetsDepot>
	 * @version 1.0
	 */
	List<AssetsDepot> queryListByName(String name);
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	List<AssetsDepot> queryDepotforXR();
	/**
	 *  停用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void disableDepot(String id);
	/**
	 *  启用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void enableDepot(String id);
}
