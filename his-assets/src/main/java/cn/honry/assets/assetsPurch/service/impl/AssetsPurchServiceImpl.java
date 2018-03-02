package cn.honry.assets.assetsPurch.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.assetsPurch.dao.AssetsPurchDAO;
import cn.honry.assets.assetsPurch.service.AssetsPurchService;
import cn.honry.assets.assetsPurch.vo.AssetsPurchVo;
import cn.honry.assets.deviceContract.service.DeviceContractService;
import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("assetsPurchService")
@Transactional
@SuppressWarnings({ "all" })
public class AssetsPurchServiceImpl implements AssetsPurchService{
	@Autowired
	@Qualifier(value = "assetsPurchDAO")
	private AssetsPurchDAO assetsPurchDAO;
	@Override
	public AssetsPurch get(String id) {
		AssetsPurch assets = assetsPurchDAO.get(id);
		return assets;
	}
	//合同service
	private DeviceContractService deviceContractService;
	@Autowired
	@Qualifier(value = "deviceContractService")
	public void setDeviceContractService(DeviceContractService deviceContractService) {
		this.deviceContractService = deviceContractService;
	}
	public DeviceContractService getDeviceContractService() {
		return deviceContractService;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(AssetsPurch arg0) {
		
	}
	/**  
	 * 设备采购申报--提交申请（保存）
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void saveOrUpdateeditInfo(AssetsPurch assetsPurch) throws Exception{
		if(StringUtils.isBlank(assetsPurch.getId())){//添加
			assetsPurch.setId(null);
			assetsPurch.setApplDate(new Date());//申报时间
			assetsPurch.setApplAcc(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//申报人工号(当前登录的账号)
			assetsPurch.setApplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//申报人姓名(当前登录的员工)
			assetsPurch.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			assetsPurch.setCreateTime(DateUtils.getCurrentTime());//创建时间
			assetsPurch.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//当前登录科室
			assetsPurchDAO.save(assetsPurch);
		}else{//修改
			AssetsPurch purch = assetsPurchDAO.get(assetsPurch.getId());
			purch.setPurchNum(assetsPurch.getPurchNum());//采购数量 
			purch.setTranCost(assetsPurch.getTranCost());//运费(元)
			purch.setInstCost(assetsPurch.getInstCost());//安装费(元)
			purch.setPurchTotal(assetsPurch.getPurchTotal());//采购总价(元)
			purch.setApplState(assetsPurch.getApplState());//申报状态
			purch.setApplDate(new Date());//申报时间
			purch.setApplAcc(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//申报人工号(当前登录的账号)
			purch.setApplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//申报人姓名(当前登录的员工)
			purch.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//修改人
			purch.setUpdateTime(DateUtils.getCurrentTime());//修改时间
			assetsPurchDAO.save(purch);
		}
	}
	/**  
	 * 设备采购申报--已申报列表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurch> queryAllAssetsByData( AssetsPurch assets,String state)throws Exception {
		List<AssetsPurch> assetsList=assetsPurchDAO.queryAllAssetsByData(assets,state);
		return assetsList;
	}
	/**  
	 * 设备采购申报--已申报列表  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int getTotalList( AssetsPurch assets,String state) throws Exception {
		return assetsPurchDAO.getTotal(assets,state);
	}
	/**  
	 * 设备采购申报--已申报 停用按钮
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void stopAssets(String id, String flag) {
		assetsPurchDAO.stopAssets(id,flag);
	}
	/**  
	 * 设备采购申报--草稿箱 删除  
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void del(String id) {
		assetsPurchDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	}
	/**  
	 * 设备采购审批--待审批  通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void passAssets(String id) throws Exception {
		//审批通过采购数据
		assetsPurchDAO.passAssets(id);
		AssetsPurch assetsPurch = assetsPurchDAO.get(id);
		if(assetsPurch!=null){
			//同时需要向合同表中插入一条同样的数据
			AssetsDeviceContract entity=new AssetsDeviceContract();
			entity.setOfficeCode(assetsPurch.getOfficeCode());
			entity.setOfficeName(assetsPurch.getOfficeName());
			entity.setClassCode(assetsPurch.getClassCode());
			entity.setClassName(assetsPurch.getClassName());
			entity.setDeviceCode(assetsPurch.getDeviceCode());
			entity.setDeviceName(assetsPurch.getDeviceName());
			entity.setMeterUnit(assetsPurch.getMeterUnit());
			if(assetsPurch.getPurchPrice()!=null){
				entity.setPurchPrice(assetsPurch.getPurchPrice());
			}else{
				entity.setPurchPrice(0.0);
			}
			if(assetsPurch.getPurchNum()!=null){
				entity.setPurchNum(assetsPurch.getPurchNum());
			}else{
				entity.setPurchNum(0);
			}
			if(assetsPurch.getPurchTotal()!=null){
				entity.setPurchTotal(assetsPurch.getPurchTotal());
			}else{
				entity.setPurchTotal(0.0);
			}
			entity.setState(0);//未上传
			entity.setCreateUser(assetsPurch.getCreateUser());
			entity.setCreateTime(new Date());
			entity.setCreateDept(assetsPurch.getCreateDept());
			deviceContractService.save(entity);
		}
	}
	/**  
	 * 设备采购审批--待审批  不通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void noPassAssets(String id, String reason) throws Exception {
		assetsPurchDAO.noPassAssets(id,reason);
	}
	/**  
	 * 设备采购审批--待审批  不通过原因
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public AssetsPurch seeReason(String id) {
		return assetsPurchDAO.seeReason(id);
	}
	/**  
	 * 设备采购审批--待审批  通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurch> querySPAssetsByData(AssetsPurch assets, String state,String startTime,String endTime) {
		List<AssetsPurch> assetsList=assetsPurchDAO.querySPAssetsByData(assets,state, startTime, endTime);
		return assetsList;
	}
	/**  
	 * 设备采购审批--待审批  通过 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int getSPTotalList(AssetsPurch assets, String state,String startTime,String endTime) {
		return assetsPurchDAO.getSPTotalList(assets,state, startTime, endTime);
	}
	/**  
	 * 设备采购审批--采购计划列表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurchVo> queryPurchPlan(String page, String rows,AssetsPurchplan assets,String stated) {
		List<AssetsPurchVo> assetsList=assetsPurchDAO.queryPurchPlan(page,rows,assets,stated);
		for (AssetsPurchVo vo : assetsList) {
			if ((vo.getPlanNum()==null?0:vo.getPlanNum())-(vo.getPurchNum()==null?0:vo.getPurchNum())==0) {
				vo.setCondition("完成");
			}else{
				vo.setCondition("未完成");
			}
		}
		return assetsList;
	}

	@Override
	public int getTotalPlan(AssetsPurchplan assets,String stated) {
		return assetsPurchDAO.getTotalPlan(assets,stated);
	}
	@Override
	public int queryPlan(AssetsPurch assets) {
		return assetsPurchDAO.queryPlan(assets);
	}

}
