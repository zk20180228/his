package cn.honry.assets.deviceseRvice.service;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDeviceseRvice;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface DeviceseRviceService  extends BaseService<AssetsDeviceseRvice> {
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
	 *  增加数据/修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	void saveOrupdata(AssetsDeviceseRvice deviceseRvice);
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	void delete(AssetsDeviceseRvice deviceseRvice);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceseRvice> findAll();
	
	/***
	 * 数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @param deviceseRvice
	 * @return String 标识，T & F
	 * @version 1.0
	 */
	String verification(AssetsDeviceseRvice deviceseRvice);
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
