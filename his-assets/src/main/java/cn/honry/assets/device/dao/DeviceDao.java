package cn.honry.assets.device.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DeviceDao extends EntityDao<AssetsDevice>{
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	List<AssetsDevice> queryDeviceStorage(AssetsDevice Device);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	int getDeviceCountStorage(AssetsDevice Device);
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	List<AssetsDevice> queryDeviceDraft(AssetsDevice Device);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	int getDeviceCountDraft(AssetsDevice Device);
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	List<AssetsDevice> queryDeviceApproval(AssetsDevice Device);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	int getDeviceCountApproval(AssetsDevice Device);
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	List<AssetsDevice> queryDeviceNotApp(AssetsDevice Device);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	int getDeviceCountNotApp(AssetsDevice Device);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDevice> findAll();
	/**
	 * 根据厂商名称查询模糊
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDevice> findbyName(String name);
	
	/***
	 * name 验证
	 * @Title: queryListByName 
	 * @author zpty
	 * @date 2017-11-14
	 * @param name
	 * @return List<AssetsDevice>
	 * @version 1.0
	 */
	List<AssetsDevice> queryListByName(String name);
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	List<AssetsDevice> queryDeviceforXR();
	/**
	 *  不通过数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void disableDevice(String id,String reason);
	/**
	 *  通过数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void enableDevice(String id);
	/**
	 *  更新领用数量
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void updateOutNum(String id, int num);
}
