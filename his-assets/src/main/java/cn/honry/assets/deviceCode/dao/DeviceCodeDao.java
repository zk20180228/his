package cn.honry.assets.deviceCode.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DeviceCodeDao extends EntityDao<AssetsDeviceCode>{
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
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceCode> findAll();
	/**
	 * 根据厂商名称查询模糊
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceCode> findbyName(String name);
	
	/***
	 * name 验证
	 * @Title: queryListByName 
	 * @author zpty
	 * @date 2017-11-14
	 * @param name
	 * @return List<AssetsDeviceCode>
	 * @version 1.0
	 */
	List<AssetsDeviceCode> queryListByName(String name);
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
	void disableDeviceCode(String id);
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
	/**
	 *  查询当前设备类别代码下的设备条数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param id
	 * @return
	 */
	Integer queryNum(String classCode);
}
