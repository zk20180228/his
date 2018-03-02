package cn.honry.assets.deviceContract.service.Impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.assetsPurch.dao.AssetsPurchDAO;
import cn.honry.assets.deviceContract.dao.DeviceContractDao;
import cn.honry.assets.deviceContract.service.DeviceContractService;
import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.utils.ShiroSessionUtils;

@Service("deviceContractService")
@Transactional
@SuppressWarnings({"all"})
public class DeviceContractServiceImpl implements DeviceContractService{
	
	@Autowired
	@Qualifier(value = "deviceContractDao")
	private DeviceContractDao deviceContractDao;
	
	public void setDeviceContractDao(DeviceContractDao deviceContractDao) {
		this.deviceContractDao = deviceContractDao;
	}
	
	@Autowired
	@Qualifier(value = "assetsPurchDAO")
	private AssetsPurchDAO assetsPurchDAO;
	public AssetsPurchDAO getAssetsPurchDAO() {
		return assetsPurchDAO;
	}
	public void setAssetsPurchDAO(AssetsPurchDAO assetsPurchDAO) {
		this.assetsPurchDAO = assetsPurchDAO;
	}
	/**  
	 * 设备合同管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsDeviceContract> queryDeviceContract(AssetsDeviceContract contract, String page,String rows, String menuAlias) {
		
		return deviceContractDao.queryDeviceContract(contract,page, rows, menuAlias);
	}
	/**  
	 * 设备合同管理列表查询(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(AssetsDeviceContract contract) {
		return deviceContractDao.queryTotal(contract);
	}
	@Override
	public void save(AssetsDeviceContract entity) {
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(entity.getId())){
			entity.setId(null);
			entity.setCreateDept(deptCode);
			entity.setCreateTime(new Date());
			entity.setCreateUser(userAccount);
		}else{
			entity.setUpdateTime(new Date());
			entity.setUpdateUser(userAccount);
		}
		deviceContractDao.save(entity);
	}
	
	/**  
	 * 设备合同管理列表查询
	 * @Author: zpty
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurch> queryDevicePurch(String officeCode, String page,String rows, String menuAlias) {
		
		return assetsPurchDAO.queryDevicePurch(officeCode,page, rows, menuAlias);
	}
	/**  
	 * 设备合同管理列表查询(总条数)
	 * @Author: zpty
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotalPurch(String code) {
		return assetsPurchDAO.queryTotalPurch(code);
	}
	@Override
	public AssetsDeviceContract get(String id) {
		return deviceContractDao.get(id);
	}
	@Override
	public void removeUnused(String arg0) {
		
	}
	@Override
	public void saveOrUpdate(AssetsDeviceContract arg0) {
		
	}
}
