package cn.honry.assets.deviceDossier.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.honry.assets.assetsRepair.dao.AssetsRepairDAO;
import cn.honry.assets.assetsRepair.service.AssetsRepairService;
import cn.honry.assets.assetsScrap.dao.AssetsScrapDAO;

import cn.honry.assets.device.service.DeviceService;
import cn.honry.assets.deviceCode.service.DeviceCodeService;

import cn.honry.assets.deviceDossier.dao.DeviceDossierDao;
import cn.honry.assets.deviceDossier.service.DeviceDossierService;
import cn.honry.assets.deviceDossier.vo.DeviceDossierVo;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.base.bean.model.AssetsDeviceDossier;

import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsDeviceScrap;

import cn.honry.base.bean.model.AssetsDeviceUse;

import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @author sgt
 *
 */
@Service("deviceDossierService")
@Transactional
@SuppressWarnings({ "all" })
public class DeviceDossierServiceImp implements DeviceDossierService{
	@Autowired
	private DeviceDossierDao deviceDossierDao;
	@Autowired
	private  AssetsScrapDAO assetsScrapDAO;
	@Autowired
	private  AssetsRepairService assetsRepairService;
	public DeviceDossierDao getDeviceDossierDao() {
		return deviceDossierDao;
	}
	public void setDeviceDossierDao(DeviceDossierDao deviceDossierDao) {
		this.deviceDossierDao = deviceDossierDao;
	}
	
	@Autowired
	private DeviceService deviceService;
	public DeviceService getDeviceService() {
		return deviceService;
	}
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	@Autowired
	private DeviceCodeService deviceCodeService;
	public DeviceCodeService getDeviceCodeService() {
		return deviceCodeService;
	}
	public void setDeviceCodeService(DeviceCodeService deviceCodeService) {
		this.deviceCodeService = deviceCodeService;
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> queryDeviceDossier(AssetsDeviceDossier DeviceDossier) {
		return deviceDossierDao.queryDeviceDossier(DeviceDossier);
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> queryDeviceDossierforXR() {
		return deviceDossierDao.queryDeviceDossierforXR();
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public int getDeviceDossierCount(AssetsDeviceDossier DeviceDossier) {
		return deviceDossierDao.getDeviceDossierCount(DeviceDossier);
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
	public void saveOrupdata(AssetsDeviceDossier deviceDossier) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(deviceDossier.getId())){//保存
			deviceDossier.setId(null);
			deviceDossier.setCreateDept(deptCode);
			deviceDossier.setCreateTime(new Date());
			deviceDossier.setCreateUser(userAccount);
		}else{
			deviceDossier.setUpdateTime(new Date());
			deviceDossier.setUpdateUser(userAccount);
		}
		deviceDossier.setDel_flg(0);
//		deviceDossier.setStop_flg(0);
		if(StringUtils.isBlank(deviceDossier.getId())){
			deviceDossier.setId(null);
			deviceDossierDao.save(deviceDossier);
			OperationUtils.getInstance().conserve(null, "设备档案管理", "INSERT INTO", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONINSERT);
		}else{
			deviceDossierDao.save(deviceDossier);
			OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备档案管理", "UPDATE", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public void delete(AssetsDeviceDossier deviceDossier) {
		deviceDossierDao.del(deviceDossier.getId(),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备档案管理", "UPDATE", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONDELETE);
	}
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> findAll() {
		return deviceDossierDao.findAll();
	}
	@Override
	public void removeUnused(String id) {
		
	}
	@Override
	public AssetsDeviceDossier get(String id) {
		return deviceDossierDao.get(id);
	}
	@Override
	public void saveOrUpdate(AssetsDeviceDossier entity) {
		
	}
	@Override
	public String verification(AssetsDeviceDossier deviceDossier) {
		String name = deviceDossier.getOfficeName().replace(" ", "");
		List<AssetsDeviceDossier> list;
		if(StringUtils.isNotBlank(deviceDossier.getId())){
			list = deviceDossierDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}else{
				if(list.size() == 1){
					if(deviceDossier.getId().equals(list.get(0).getId())){
						return "T";
					}
				}
			}
		}else{
			list = deviceDossierDao.queryListByName(name);
			if(list.size() == 0){
				return "T";
			}
		}
		return "F";
	}
	@Override
	public void disableDeviceDossier(AssetsDeviceDossier deviceDossier) {
		String id=deviceDossier.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			deviceDossierDao.disableDeviceDossier(ids[i]);
		}
		OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备档案管理", "UPDATE", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONUPDATE);
	}
	@Override
	public void enableDeviceDossier(AssetsDeviceDossier deviceDossier) {
		String id=deviceDossier.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			deviceDossierDao.enableDeviceDossier(ids[i]);
		}
		OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备档案管理", "UPDATE", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public List<AssetsDeviceDossier> queryAllAssetsByData(String page, String rows, AssetsDeviceDossier assets,
			String state) throws Exception {
		List<AssetsDeviceDossier> assetsList=deviceDossierDao.queryAllAssetsByData(page,rows,assets,state);
		return assetsList;
	}

	@Override
	public List<DeviceDossierVo> queryDeviceDossierValue(AssetsDeviceDossier DeviceDossier) throws Exception {
		 List<DeviceDossierVo> list = deviceDossierDao.queryDeviceDossierValue(DeviceDossier);
		 for (DeviceDossierVo vo : list) {
			double month = this.getMonth(vo.getDeviceDate(), new Date());
			double depreciation=vo.getDepreciation()==null?1:vo.getDepreciation();
			vo.setNewValue(vo.getPurchTotal()*(1-month/depreciation/12));
		}
		return list;
	}
	@Override
	public Integer queryDeviceDossierTotal(AssetsDeviceDossier DeviceDossier)
			throws Exception {
		return deviceDossierDao.queryDeviceDossierTotal(DeviceDossier);
	}



	@Override
	public int getTotalList(String page, String rows, AssetsDeviceDossier assets, String state) throws Exception {
		return deviceDossierDao.getTotal(page,rows,assets,state);
	}
	@Override
	public void saveRepairReson(AssetsDeviceDossier assetsDeviceDossier, String repairReson) throws Exception {
		if(assetsDeviceDossier!=null){
			AssetsDeviceMaintain assets = new AssetsDeviceMaintain();
			assets.setOfficeName(assetsDeviceDossier.getOfficeName());
			assets.setOfficeCode(assetsDeviceDossier.getOfficeCode());
			assets.setClassName(assetsDeviceDossier.getClassName());
			assets.setClassCode(assetsDeviceDossier.getClassCode());
			assets.setDeviceNo(assetsDeviceDossier.getDeviceNo());
			assets.setDeviceCode(assetsDeviceDossier.getDeviceCode());
			assets.setDeviceName(assetsDeviceDossier.getDeviceName());
			if(assetsDeviceDossier.getMaintainNum()!=null){
				assets.setMaintainNum(assetsDeviceDossier.getMaintainNum()+1);
			}else{
				assets.setMaintainNum(1);
			}
			assets.setApplDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			assets.setApplDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
			assets.setApplAcc(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assets.setApplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			assets.setApplDate(DateUtils.getCurrentTime());
			assets.setRepairReson(repairReson);
			assets.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assets.setCreateTime(DateUtils.getCurrentTime());
			assets.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			assetsRepairService.save(assets);
			AssetsDeviceDossier deviceDossier  = deviceDossierDao.get(assetsDeviceDossier.getId());
			deviceDossier.setState(0);
			if(deviceDossier.getMaintainNum()!=null){
				deviceDossier.setMaintainNum(deviceDossier.getMaintainNum()+1);

			}else{
				deviceDossier.setMaintainNum(1);
			}
			deviceDossier.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			deviceDossier.setUpdateTime(DateUtils.getCurrentTime());
			deviceDossierDao.update(deviceDossier);
		}
	}
	@Override
	public void saveRepairScrap(AssetsDeviceDossier deviceDossier, String assetScrap) throws Exception {
		if(deviceDossier!=null){
			AssetsDeviceScrap assets = new AssetsDeviceScrap();
			assets.setOfficeName(deviceDossier.getOfficeName());
			assets.setOfficeCode(deviceDossier.getOfficeCode());
			assets.setClassName(deviceDossier.getClassName());
			assets.setClassCode(deviceDossier.getClassCode());
			assets.setDeviceNo(deviceDossier.getDeviceNo());
			assets.setDeviceCode(deviceDossier.getDeviceCode());
			assets.setDeviceName(deviceDossier.getDeviceName());
			assets.setMeterUnit(deviceDossier.getMeterUnit());
			assets.setPurchPrice(deviceDossier.getPurchPrice());
			assets.setScrapDate(DateUtils.getCurrentTime());
			assets.setManageAcc(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assets.setManageName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			assets.setScrapReason(assetScrap);
			assets.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assets.setCreateTime(DateUtils.getCurrentTime());
			assets.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			assetsScrapDAO.save(assets);
			AssetsDeviceDossier adeviceDossier  = deviceDossierDao.get(deviceDossier.getId());
			adeviceDossier.setStop_flg(1);;
			adeviceDossier.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			adeviceDossier.setUpdateTime(DateUtils.getCurrentTime());
			deviceDossierDao.update(adeviceDossier);
		}
		
	}


	/** 
     * 获取两个日期相差几个月 
     * @param start 
     * @param end 
     * @return 
     */  
    public static int getMonth(Date start, Date end) {  
        if (start.after(end)) {  
            Date t = start;  
            start = end;  
            end = t;  
        }  
        Calendar startCalendar = Calendar.getInstance();  
        startCalendar.setTime(start);  
        Calendar endCalendar = Calendar.getInstance();  
        endCalendar.setTime(end);  
        Calendar temp = Calendar.getInstance();  
        temp.setTime(end);  
        temp.add(Calendar.DATE, 1);  
  
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);  
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);  
  
        if ((startCalendar.get(Calendar.DATE) == 1)&& (temp.get(Calendar.DATE) == 1)) {  
            return year * 12 + month + 1;  
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {  
            return year * 12 + month;  
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {  
            return year * 12 + month;  
        } else {  
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);  
        }  
    }  

	
	
	//***************************************************以下是设备领用管理******************************************************//
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> queryDeviceDossierMyUse(AssetsDeviceDossier DeviceDossier) {
		return deviceDossierDao.queryDeviceDossierMyUse(DeviceDossier);
	}
	
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public int getDeviceDossierCountMyUse(AssetsDeviceDossier DeviceDossier) {
		return deviceDossierDao.getDeviceDossierCountMyUse(DeviceDossier);
	}
	
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceDossierReceive(AssetsDeviceDossier DeviceDossier) {
		//这里通过调用,去查询所有的已经经过入库审批的数据
		AssetsDevice assetsDevice = new AssetsDevice();
		assetsDevice.setOfficeName(DeviceDossier.getOfficeName());//查询条件放进去
		assetsDevice.setClassCode(DeviceDossier.getClassCode());//查询条件放进去
		assetsDevice.setClassName(DeviceDossier.getClassName());//查询条件放进去
		assetsDevice.setDeviceName(DeviceDossier.getDeviceName());//查询条件放进去
		List<AssetsDevice> assetsDeviceList = deviceService.queryDeviceApproval(assetsDevice);
		if(assetsDeviceList!=null && assetsDeviceList.size()>0){
			for(int i=0;i<assetsDeviceList.size();i++){
				assetsDeviceList.get(i).setRestNum(assetsDeviceList.get(i).getDeviceNum()-assetsDeviceList.get(i).getOutNum());
			}
		}
		return assetsDeviceList;
	}
	
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public int getDeviceDossierCountReceive(AssetsDeviceDossier DeviceDossier) {
		//这里通过调用,去查询所有的已经经过入库审批的数据
		AssetsDevice assetsDevice = new AssetsDevice();
		assetsDevice.setOfficeName(DeviceDossier.getOfficeName());//查询条件放进去
		return deviceService.getDeviceCountApproval(assetsDevice);
	}
	
	
	@Override
	public void saveDeviceUse(AssetsDeviceDossier deviceDossier) {
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();//领用人工号
		String userName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();//领用人姓名
		String loginDeptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();//领用人科室Code
		String loginDeptName = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();//领用人科室名称
		if(StringUtils.isNotBlank(deviceDossier.getId())){
			AssetsDevice assetsDevice = deviceService.get(deviceDossier.getId());//点击列表中的数据拿出来的ID是入库审批成功的数据ID
			String deviceCode = assetsDevice.getDeviceCode();//取出设备代码
			List<AssetsDeviceCode> deviceCodeList = deviceCodeService.getCodeList(deviceCode);//通过设备码查询出所有可领用的设备条码数据
			//查出来没有领用的数据
			if(deviceCodeList!=null && deviceCodeList.size()>0){
				//没有领用的条数必须大于或者等于领用人输入的领用数量,否则数据存在问题
				if(deviceCodeList.size()>=deviceDossier.getUseNum()){
					for(int i=0;i<deviceDossier.getUseNum();i++){
						AssetsDeviceCode assetsDeviceCode = deviceCodeList.get(i);
						//首先,先用条码数据存入档案表中去
						AssetsDeviceDossier deviceDossierSave=new AssetsDeviceDossier();
						deviceDossierSave.setOfficeCode(assetsDeviceCode.getOfficeCode());//办公用途编码
						deviceDossierSave.setOfficeName(assetsDeviceCode.getOfficeName());//办公用途名称
						deviceDossierSave.setClassCode(assetsDeviceCode.getClassCode());//设备分类编码
						deviceDossierSave.setClassName(assetsDeviceCode.getClassName());//设备分类名称
						deviceDossierSave.setDeviceNo(assetsDeviceCode.getDeviceNo());//设备条码号
						deviceDossierSave.setDeviceCode(assetsDeviceCode.getDeviceCode());//设备代码
						deviceDossierSave.setDeviceName(assetsDeviceCode.getDeviceName());//设备名称
						deviceDossierSave.setMeterUnit(assetsDeviceCode.getMeterUnit());//计量单位
						deviceDossierSave.setPurchPrice(assetsDeviceCode.getPurchPrice());//采购单价(元)
						deviceDossierSave.setUseDeptCode(loginDeptCode);//领用部门编码
						deviceDossierSave.setUseDeptName(loginDeptName);//领用部门名称
						deviceDossierSave.setUseAcc(userAccount);//领用人工号
						deviceDossierSave.setUseName(userName);//领用人姓名
						deviceDossierSave.setState(0);//状态0:正常
						deviceDossierSave.setCreateDept(loginDeptCode);
						deviceDossierSave.setCreateTime(new Date());
						deviceDossierSave.setCreateUser(userAccount);
						deviceDossierSave.setDel_flg(0);
						deviceDossierSave.setStop_flg(0);
						deviceDossierDao.save(deviceDossierSave);//保存档案数据
						//其次,保存领用表
						AssetsDeviceUse deviceUse=new AssetsDeviceUse();
						deviceUse.setOfficeCode(assetsDeviceCode.getOfficeCode());//办公用途编码
						deviceUse.setOfficeName(assetsDeviceCode.getOfficeName());//办公用途名称
						deviceUse.setClassCode(assetsDeviceCode.getClassCode());//设备分类编码
						deviceUse.setClassName(assetsDeviceCode.getClassName());//设备分类名称
						deviceUse.setDeviceNo(assetsDeviceCode.getDeviceNo());//设备条码号
						deviceUse.setDeviceCode(assetsDeviceCode.getDeviceCode());//设备代码
						deviceUse.setDeviceName(assetsDeviceCode.getDeviceName());//设备名称
						deviceUse.setMeterUnit(assetsDeviceCode.getMeterUnit());//计量单位
						deviceUse.setPurchPrice(assetsDeviceCode.getPurchPrice());//采购单价(元)
						deviceUse.setUseDate(new Date());//领用时间
						deviceUse.setCreateDept(loginDeptCode);
						deviceUse.setCreateTime(new Date());
						deviceUse.setCreateUser(userAccount);
						deviceUse.setDel_flg(0);
						deviceUse.setStop_flg(0);
						deviceDossierDao.save(deviceUse);//保存领用数据
						//再次,修改条码数据的领用标记
						deviceCodeService.updateUseFlg(assetsDeviceCode.getId());
					}
					//最后,需要在入库审核成功的记录中加入领用的数量
					deviceService.updateOutNum(deviceDossier);
				}
			}
			OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备档案管理", "INSERT INTO", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONINSERT);
			OperationUtils.getInstance().conserve(null, "设备领用管理", "INSERT INTO", "T_ASSETS_DEVICE_USE", OperationUtils.LOGACTIONINSERT);
		}
	}
	@Override
	public List<AssetsDeviceDossier> queryDeviceUseView(String deviceCode,AssetsDeviceDossier deviceDossier) {
		return deviceDossierDao.queryDeviceUseView(deviceCode,deviceDossier);
	}
	@Override
	public int getDeviceCountUseView(String deviceCode,AssetsDeviceDossier deviceDossier) {
		return deviceDossierDao.getDeviceDossierCount(deviceCode,deviceDossier);
	}
	
	@Override
	public void repairMyUse(AssetsDeviceDossier deviceDossier) {
		String id=deviceDossier.getId();
		String[] ids=id.split(",");
		for(int i=0;i<ids.length;i++){
			deviceDossierDao.repairMyUse(ids[i]);
		}
		OperationUtils.getInstance().conserve(deviceDossier.getId(), "设备领用管理", "UPDATE", "T_ASSETS_DEVICE_DOSSIER", OperationUtils.LOGACTIONUPDATE);
	}
}
