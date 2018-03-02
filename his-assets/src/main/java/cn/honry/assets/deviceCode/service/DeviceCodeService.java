package cn.honry.assets.deviceCode.service;

import java.util.List;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface DeviceCodeService  extends BaseService<AssetsDeviceCode> {
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	List<AssetsDeviceCode> queryDeviceCode(AssetsDeviceCode DeviceCode);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	int getDeviceCodeCount(AssetsDeviceCode DeviceCode);
	/**
	 *  增加数据/修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	void saveOrupdata(AssetsDeviceCode deviceCode);
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	void delete(AssetsDeviceCode deviceCode);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceCode> findAll();
	
	/***
	 * 数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @param deviceCode
	 * @return String 标识，T & F
	 * @version 1.0
	 */
	String verification(AssetsDeviceCode deviceCode);
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	List<AssetsDeviceCode> queryDeviceCodeforXR();
	/**
	 *  打印数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	void disableDeviceCode(AssetsDeviceCode deviceCode);
	/**
	 *  从入库审批的时候插入新数据到条码管理
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	void newDeviceCode(AssetsDevice device);
	/**
	 *  通过设备码查询所有可以领用的条码数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	List<AssetsDeviceCode> getCodeList(String deviceCode);
	/**
	 *  标记领用过的条码数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param id
	 * @return
	 */
	void updateUseFlg(String id);
}
