package cn.honry.assets.device.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.assetsDeviceCheck.service.AssetsDeviceCheckService;
import cn.honry.assets.device.dao.DeviceDao;
import cn.honry.assets.device.service.DeviceService;
import cn.honry.assets.deviceCode.service.DeviceCodeService;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author sgt
 *
 */
@Service("deviceService")
@Transactional
@SuppressWarnings({ "all" })
public class DeviceServiceImp implements DeviceService{
	@Autowired
	private DeviceDao deviceDao;
	public DeviceDao getDeviceDao() {
		return deviceDao;
	}
	public void setDeviceDao(DeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}
	
	@Autowired
	private DeviceCodeService deviceCodeService;
	public DeviceCodeService getDeviceCodeService() {
		return deviceCodeService;
	}
	public void setDeviceCodeService(DeviceCodeService deviceCodeService) {
		this.deviceCodeService = deviceCodeService;
	}
	@Autowired
	private AssetsDeviceCheckService assetsDeviceCheckService;
	public AssetsDeviceCheckService getAssetsDeviceCheckService() {
		return assetsDeviceCheckService;
	}
	public void setAssetsDeviceCheckService(
			AssetsDeviceCheckService assetsDeviceCheckService) {
		this.assetsDeviceCheckService = assetsDeviceCheckService;
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceStorage(AssetsDevice Device) {
		return deviceDao.queryDeviceStorage(Device);
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceforXR() {
		return deviceDao.queryDeviceforXR();
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountStorage(AssetsDevice Device) {
		return deviceDao.getDeviceCountStorage(Device);
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceDraft(AssetsDevice Device) {
		return deviceDao.queryDeviceDraft(Device);
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountDraft(AssetsDevice Device) {
		return deviceDao.getDeviceCountDraft(Device);
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceApproval(AssetsDevice Device) {
		return deviceDao.queryDeviceApproval(Device);
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountApproval(AssetsDevice Device) {
		return deviceDao.getDeviceCountApproval(Device);
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceNotApp(AssetsDevice Device) {
		return deviceDao.queryDeviceNotApp(Device);
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountNotApp(AssetsDevice Device) {
		return deviceDao.getDeviceCountNotApp(Device);
	}
	/**
	 *  
	 * @Description：  增加数据&修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveOrupdata(AssetsDevice device) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String userName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		if(StringUtils.isBlank(device.getId())){//保存
			device.setId(null);
			device.setApplAcc(userAccount);//申报人工号
			device.setApplName(userName);//申报人姓名
			device.setCreateDept(deptCode);
			device.setCreateTime(new Date());
			device.setCreateUser(userAccount);
		}else{
			device.setUpdateTime(new Date());
			device.setUpdateUser(userAccount);
		}
		device.setDel_flg(0);
		device.setStop_flg(0);
		if(StringUtils.isBlank(device.getId())){
			device.setId(null);
			deviceDao.save(device);
			OperationUtils.getInstance().conserve(null, "设备入库管理", "INSERT INTO", "T_ASSETS_DEVICE", OperationUtils.LOGACTIONINSERT);
		}else{
			deviceDao.save(device);
			OperationUtils.getInstance().conserve(device.getId(), "设备入库管理", "UPDATE", "T_ASSETS_DEVICE", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public void delete(AssetsDevice device) {
		deviceDao.del(device.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(device.getId(), "设备入库管理", "UPDATE", "T_ASSETS_DEVICE", OperationUtils.LOGACTIONDELETE);
	}
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsDevice> findAll() {
		return deviceDao.findAll();
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public AssetsDevice get(String id) {
		return deviceDao.get(id);
	}
	@Override
	public void saveOrUpdate(AssetsDevice entity) {
		
	}
	@Override
	public String verification(AssetsDevice device) {
		String name = device.getOfficeName().replace(" ", "");
		List<AssetsDevice> list;
		if(StringUtils.isNotBlank(device.getId())){
			list = deviceDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}else{
				if(list.size() == 1){
					if(device.getId().equals(list.get(0).getId())){
						return "T";
					}
				}
			}
		}else{
			list = deviceDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}
		}
		return "F";
	}

	@Override
	public void disableDevice(AssetsDevice device) {
		String id=device.getId();
		String reason = device.getReason();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			deviceDao.disableDevice(ids[i],reason);
		}
		OperationUtils.getInstance().conserve(device.getId(), "设备入库管理", "UPDATE", "T_ASSETS_DEVICE", OperationUtils.LOGACTIONUPDATE);
	}
	@Override
	public void enableDevice(AssetsDevice device) {
		String id=device.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			deviceDao.enableDevice(ids[i]);
			AssetsDevice deviceCodeNew = deviceDao.get(ids[i]);
			deviceCodeService.newDeviceCode(deviceCodeNew);
			assetsDeviceCheckService.newDeviceCode(deviceCodeNew);//加入入库数据
		}
		OperationUtils.getInstance().conserve(device.getId(), "设备入库管理", "UPDATE", "T_ASSETS_DEVICE", OperationUtils.LOGACTIONUPDATE);
	}
	@Override
	public void updateOutNum(AssetsDeviceDossier deviceDossier) {
		AssetsDevice assetsDevice = deviceDao.get(deviceDossier.getId());//先取出要更新的数据
		deviceDao.updateOutNum(deviceDossier.getId(),assetsDevice.getOutNum()+deviceDossier.getUseNum());
		OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备入库管理", "UPDATE", "T_ASSETS_DEVICE", OperationUtils.LOGACTIONUPDATE);
	}
	
}
