package cn.honry.assets.assetsDeviceCheck.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.assetsDeviceCheck.dao.AssetsDeviceCheckDAO;
import cn.honry.assets.assetsDeviceCheck.service.AssetsDeviceCheckService;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceCheck;
import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("assetsDeviceCheckService")
@Transactional
@SuppressWarnings({ "all" })
public class AssetsDeviceCheckServiceImpl implements AssetsDeviceCheckService{
	@Autowired
	@Qualifier(value = "assetsDeviceCheckDAO")
	private AssetsDeviceCheckDAO assetsDeviceCheckDAO;
	
	@Override
	public AssetsDeviceCheck get(String arg0) {
		return assetsDeviceCheckDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
	}
	/**  
	 * 
	 * 盘点
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void saveOrUpdate(AssetsDeviceCheck assetsDeviceCheck) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		AssetsDeviceCheck vo=new AssetsDeviceCheck();
		if(StringUtils.isNotBlank(assetsDeviceCheck.getId())){
			vo = assetsDeviceCheckDAO.get(assetsDeviceCheck.getId());//先用ID查出该数据
			vo.setManageAcc(userId);
			vo.setManageName(ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getName());
			vo.setCheckNum(assetsDeviceCheck.getCheckNum());
			vo.setCheckDate(new Date());
			vo.setUpdateTime(new Date());
			vo.setUpdateUser(userId);
			assetsDeviceCheckDAO.save(vo);
			OperationUtils.getInstance().conserve(vo.getId(), "设备盘点管理", "UPDATE", "T_ASSETS_DEVICE_CHECK", OperationUtils.LOGACTIONUPDATE);
		}
	}
	/**  
	 * 
	 * 设备盘点管理list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<AssetsDeviceCheck> queryAssetsDeviceCheck(
			AssetsDeviceCheck deviceDossier) throws Exception {
		return assetsDeviceCheckDAO.queryAssetsDeviceCheck(deviceDossier);
	}
	/**  
	 * 
	 * 设备盘点管理Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryTotal(AssetsDeviceCheck deviceDossier) throws Exception {
		return assetsDeviceCheckDAO.queryTotal(deviceDossier);
	}
	/**  
	 * 
	 * 校正
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午11:07:31 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午11:07:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void Correcting(AssetsDeviceCheck assetsDeviceCheck)
			throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		AssetsDeviceCheck vo=new AssetsDeviceCheck();
		if(StringUtils.isNotBlank(assetsDeviceCheck.getId())){
			vo = assetsDeviceCheckDAO.get(assetsDeviceCheck.getId());//先用ID查出该数据
		}
		vo.setReperNum(vo.getCheckNum());
		vo.setUpdateTime(new Date());
		vo.setUpdateUser(userId);
		assetsDeviceCheckDAO.save(vo);
		OperationUtils.getInstance().conserve(vo.getId(), "设备盘点管理", "UPDATE", "T_ASSETS_DEVICE_CHECK", OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void newDeviceCode(AssetsDevice device) {
		if(StringUtils.isNoneBlank(device.getDeviceCode())){
			String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			AssetsDeviceCheck deviceCheck = assetsDeviceCheckDAO.queryByDeviceCode(device.getDeviceCode());
			if (StringUtils.isNoneBlank(deviceCheck.getId())) {
				deviceCheck.setReperNum(deviceCheck.getReperNum()+device.getDeviceNum());
				deviceCheck.setUpdateTime(new Date());
				deviceCheck.setUpdateUser(userId);
				assetsDeviceCheckDAO.save(deviceCheck);
				OperationUtils.getInstance().conserve(null, "设备盘点管理", "UPDATE", "T_ASSETS_DEVICE_CHECK", OperationUtils.LOGACTIONINSERT);
			}else {
				AssetsDeviceCheck deviceCode = new AssetsDeviceCheck();
				deviceCode.setId(null);
				deviceCode.setOfficeCode(device.getOfficeCode());//办公用途编码
				deviceCode.setOfficeName(device.getOfficeName());//办公用途名称
				deviceCode.setClassCode(device.getClassCode());//设备分类编码
				deviceCode.setClassName(device.getClassName());//设备分类名称
				deviceCode.setDeviceCode(device.getDeviceCode());//设备代码
				deviceCode.setDeviceName(device.getDeviceName());//设备名称
				deviceCode.setMeterUnit(device.getMeterUnit());//计量单位
				deviceCode.setReperNum(device.getDeviceNum());//入库数量
				deviceCode.setCreateTime(new Date());
				deviceCode.setCreateDept(device.getCreateDept());
				deviceCode.setCreateUser(device.getCreateUser());
				assetsDeviceCheckDAO.save(deviceCode);
				OperationUtils.getInstance().conserve(null, "设备盘点管理", "INSERT INTO", "T_ASSETS_DEVICE_CHECK", OperationUtils.LOGACTIONINSERT);
			}
		}
	}
}
