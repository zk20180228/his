package cn.honry.assets.deviceseRvice.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDeviceseRvice;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DeviceseRviceDao extends EntityDao<AssetsDeviceseRvice>{
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	List<AssetsDeviceseRvice> queryDeviceseRvice(AssetsDeviceseRvice DeviceseRvice);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	int getDeviceseRviceCount(AssetsDeviceseRvice DeviceseRvice);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceseRvice> findAll();
	/**
	 * 根据厂商名称查询模糊
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceseRvice> findbyName(String name);
	
	/***
	 * name 验证
	 * @Title: queryListByName 
	 * @author zpty
	 * @date 2017-11-14
	 * @param name
	 * @return List<AssetsDeviceseRvice>
	 * @version 1.0
	 */
	List<AssetsDeviceseRvice> queryListByName(String name);
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	List<AssetsDeviceseRvice> queryDeviceseRviceforXR();
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceseRvice> findOffice();
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceseRvice> findClass();
	
	/**
	 *  查询所有符合条件的数据---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	List<AssetsDeviceseRvice> queryDeviceseRviceForjsp(AssetsDeviceseRvice DeviceseRvice);
	/**
	 *  获取所有符合条件的数据的总数---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	int getDeviceseRviceCountForjsp(AssetsDeviceseRvice DeviceseRvice);
}
