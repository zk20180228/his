package cn.honry.assets.device.service;

import java.util.List;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface DeviceService  extends BaseService<AssetsDevice> {
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
	 *  增加数据/修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void saveOrupdata(AssetsDevice device);
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void delete(AssetsDevice device);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDevice> findAll();
	
	/***
	 * 数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @param device
	 * @return String 标识，T & F
	 * @version 1.0
	 */
	String verification(AssetsDevice device);
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
	void disableDevice(AssetsDevice device);
	/**
	 *  通过数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void enableDevice(AssetsDevice device);
	/**
	 *  更新领用数量
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	void updateOutNum(AssetsDeviceDossier deviceDossier);
}
