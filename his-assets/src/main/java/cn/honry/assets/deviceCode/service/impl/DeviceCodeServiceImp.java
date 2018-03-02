package cn.honry.assets.deviceCode.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.deviceCode.dao.DeviceCodeDao;
import cn.honry.assets.deviceCode.service.DeviceCodeService;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author sgt
 *
 */
@Service("deviceCodeService")
@Transactional
@SuppressWarnings({ "all" })
public class DeviceCodeServiceImp implements DeviceCodeService{
	@Autowired
	private DeviceCodeDao deviceCodeDao;
	public DeviceCodeDao getDeviceCodeDao() {
		return deviceCodeDao;
	}
	public void setDeviceCodeDao(DeviceCodeDao deviceCodeDao) {
		this.deviceCodeDao = deviceCodeDao;
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public List<AssetsDeviceCode> queryDeviceCode(AssetsDeviceCode DeviceCode) {
		return deviceCodeDao.queryDeviceCode(DeviceCode);
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public List<AssetsDeviceCode> queryDeviceCodeforXR() {
		return deviceCodeDao.queryDeviceCodeforXR();
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public int getDeviceCodeCount(AssetsDeviceCode DeviceCode) {
		return deviceCodeDao.getDeviceCodeCount(DeviceCode);
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
	public void saveOrupdata(AssetsDeviceCode deviceCode) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(deviceCode.getId())){//保存
			deviceCode.setId(null);
			deviceCode.setCreateDept(deptCode);
			deviceCode.setCreateTime(new Date());
			deviceCode.setCreateUser(userAccount);
		}else{
			deviceCode.setUpdateTime(new Date());
			deviceCode.setUpdateUser(userAccount);
		}
		deviceCode.setDel_flg(0);
//		deviceCode.setStop_flg(0);
		if(StringUtils.isBlank(deviceCode.getId())){
			deviceCode.setId(null);
			deviceCodeDao.save(deviceCode);
			OperationUtils.getInstance().conserve(null, "设备条码管理", "INSERT INTO", "T_ASSETS_DEVICE_CODE", OperationUtils.LOGACTIONINSERT);
		}else{
			deviceCodeDao.save(deviceCode);
			OperationUtils.getInstance().conserve(deviceCode.getId(), "设备条码管理", "UPDATE", "T_ASSETS_DEVICE_CODE", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public void delete(AssetsDeviceCode deviceCode) {
		deviceCodeDao.del(deviceCode.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(deviceCode.getId(), "设备条码管理", "UPDATE", "T_ASSETS_DEVICE_CODE", OperationUtils.LOGACTIONDELETE);
	}
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsDeviceCode> findAll() {
		return deviceCodeDao.findAll();
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public AssetsDeviceCode get(String id) {
		return deviceCodeDao.get(id);
	}
	@Override
	public void saveOrUpdate(AssetsDeviceCode entity) {
		
	}
	@Override
	public String verification(AssetsDeviceCode deviceCode) {
		String name = deviceCode.getOfficeName().replace(" ", "");
		List<AssetsDeviceCode> list;
		if(StringUtils.isNotBlank(deviceCode.getId())){
			list = deviceCodeDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}else{
				if(list.size() == 1){
					if(deviceCode.getId().equals(list.get(0).getId())){
						return "T";
					}
				}
			}
		}else{
			list = deviceCodeDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}
		}
		return "F";
	}
	@Override
	public void disableDeviceCode(AssetsDeviceCode deviceCode) {
		String id=deviceCode.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			deviceCodeDao.disableDeviceCode(ids[i]);
		}
		OperationUtils.getInstance().conserve(deviceCode.getId(), "设备条码管理", "UPDATE", "T_ASSETS_DEVICE_CODE", OperationUtils.LOGACTIONUPDATE);
	}
	@Override
	public void newDeviceCode(AssetsDevice device) {
		if(device.getDeviceNum()!=null){
			//查询出当前入库的设备设备码下的所有数据条数
			Integer num = 0;
			if(StringUtils.isNotBlank(device.getClassCode())){
				num = deviceCodeDao.queryNum(device.getClassCode());
			}
			num++;
			for(int i=0;i<device.getDeviceNum();i++){
				AssetsDeviceCode deviceCode = new AssetsDeviceCode();
				deviceCode.setId(null);
				deviceCode.setOfficeCode(device.getOfficeCode());//办公用途编码
				deviceCode.setOfficeName(device.getOfficeName());//办公用途名称
				deviceCode.setClassCode(device.getClassCode());//设备分类编码
				deviceCode.setClassName(device.getClassName());//设备分类名称
				String deciceNoNumber = device.getClassCode();
				if(num<10){
					deciceNoNumber += "000"+num+"";
				}else if(num>=10 && num<100){
					deciceNoNumber += "00"+num+"";
				}else if(num>=100 && num<1000){
					deciceNoNumber += "0"+num+"";
				}else{
					deciceNoNumber += num+"";
				}
				deviceCode.setDeviceNo(deciceNoNumber);//设备条码,这里需要预先设置
				num++;//条数+1,就不用每次都去查具体条数了
				deviceCode.setDeviceCode(device.getDeviceCode());//设备代码
				deviceCode.setDeviceName(device.getDeviceName());//设备名称
				deviceCode.setMeterUnit(device.getMeterUnit());//计量单位
				deviceCode.setPurchPrice(device.getPurchPrice());//采购单价(元)
				deviceCode.setDeviceDate(device.getDeviceDate());//入库时间
				deviceCode.setState(0);//打印
				deviceCode.setCreateTime(new Date());
				deviceCode.setCreateDept(device.getCreateDept());
				deviceCode.setCreateUser(device.getCreateUser());
				deviceCodeDao.save(deviceCode);
			}
		}
		OperationUtils.getInstance().conserve(null, "设备条码管理", "INSERT INTO", "T_ASSETS_DEVICE_CODE", OperationUtils.LOGACTIONINSERT);
	}
	@Override
	public List<AssetsDeviceCode> getCodeList(String deviceCode) {
		return deviceCodeDao.getCodeList(deviceCode);
	}
	@Override
	public void updateUseFlg(String id) {
		deviceCodeDao.updateUseFlg(id);
	}

}
