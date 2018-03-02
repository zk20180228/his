package cn.honry.assets.deviceseRvice.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.deviceseRvice.dao.DeviceseRviceDao;
import cn.honry.assets.deviceseRvice.service.DeviceseRviceService;
import cn.honry.base.bean.model.AssetsDeviceseRvice;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author sgt
 *
 */
@Service("deviceseRviceService")
@Transactional
@SuppressWarnings({ "all" })
public class DeviceseRviceServiceImp implements DeviceseRviceService{
	@Autowired
	private DeviceseRviceDao deviceseRviceDao;
	public DeviceseRviceDao getDeviceseRviceDao() {
		return deviceseRviceDao;
	}
	public void setDeviceseRviceDao(DeviceseRviceDao deviceseRviceDao) {
		this.deviceseRviceDao = deviceseRviceDao;
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> queryDeviceseRvice(AssetsDeviceseRvice DeviceseRvice) {
		return deviceseRviceDao.queryDeviceseRvice(DeviceseRvice);
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> queryDeviceseRviceforXR() {
		return deviceseRviceDao.queryDeviceseRviceforXR();
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public int getDeviceseRviceCount(AssetsDeviceseRvice DeviceseRvice) {
		return deviceseRviceDao.getDeviceseRviceCount(DeviceseRvice);
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
	public void saveOrupdata(AssetsDeviceseRvice deviceseRvice) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(deviceseRvice.getId())){//保存
			deviceseRvice.setId(null);
			deviceseRvice.setCreateDept(deptCode);
			deviceseRvice.setCreateTime(new Date());
			deviceseRvice.setCreateUser(userAccount);
		}else{
			deviceseRvice.setUpdateTime(new Date());
			deviceseRvice.setUpdateUser(userAccount);
		}
		deviceseRvice.setDel_flg(0);
		deviceseRvice.setStop_flg(0);
		if(StringUtils.isBlank(deviceseRvice.getId())){
			deviceseRvice.setId(null);
			deviceseRviceDao.save(deviceseRvice);
			OperationUtils.getInstance().conserve(null, "设备维护", "INSERT INTO", "T_ASSETS_DEVICESE_RVICE", OperationUtils.LOGACTIONINSERT);
		}else{
			deviceseRviceDao.save(deviceseRvice);
			OperationUtils.getInstance().conserve(deviceseRvice.getId(), "设备维护", "UPDATE", "T_ASSETS_DEVICESE_RVICE", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public void delete(AssetsDeviceseRvice deviceseRvice) {
		deviceseRviceDao.del(deviceseRvice.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(deviceseRvice.getId(), "设备维护", "UPDATE", "T_ASSETS_DEVICESE_RVICE", OperationUtils.LOGACTIONDELETE);
	}
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> findAll() {
		return deviceseRviceDao.findAll();
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public AssetsDeviceseRvice get(String id) {
		return deviceseRviceDao.get(id);
	}
	@Override
	public void saveOrUpdate(AssetsDeviceseRvice entity) {
		
	}
	@Override
	public String verification(AssetsDeviceseRvice deviceseRvice) {
		String name = deviceseRvice.getOfficeName().replace(" ", "");
		List<AssetsDeviceseRvice> list;
		if(StringUtils.isNotBlank(deviceseRvice.getId())){
			list = deviceseRviceDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}else{
				if(list.size() == 1){
					if(deviceseRvice.getId().equals(list.get(0).getId())){
						return "T";
					}
				}
			}
		}else{
			list = deviceseRviceDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}
		}
		return "F";
	}
	@Override
	public List<AssetsDeviceseRvice> findOffice() {
		return deviceseRviceDao.findOffice();
	}
	@Override
	public List<AssetsDeviceseRvice> findClass() {
		return deviceseRviceDao.findClass();
	}

	/**
	 *  查询所有符合条件的数据---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> queryDeviceseRviceForjsp(AssetsDeviceseRvice DeviceseRvice) {
		return deviceseRviceDao.queryDeviceseRviceForjsp(DeviceseRvice);
	}
	/**
	 *  获取所有符合条件的数据的总数---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public int getDeviceseRviceCountForjsp(AssetsDeviceseRvice DeviceseRvice) {
		return deviceseRviceDao.getDeviceseRviceCountForjsp(DeviceseRvice);
	}
}
