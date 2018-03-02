package cn.honry.assets.depot.service;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface DepotService  extends BaseService<AssetsDepot> {
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
	 *  增加数据/修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void saveOrupdata(AssetsDepot depot);
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void delete(AssetsDepot depot);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDepot> findAll();
	
	/***
	 * 数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @param depot
	 * @return String 标识，T & F
	 * @version 1.0
	 */
	String verification(AssetsDepot depot);
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
	void disableDepot(AssetsDepot depot);
	/**
	 *  启用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void enableDepot(AssetsDepot depot);
}
