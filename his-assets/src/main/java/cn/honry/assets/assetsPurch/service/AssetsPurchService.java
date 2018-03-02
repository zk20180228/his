package cn.honry.assets.assetsPurch.service;

import java.util.List;

import cn.honry.assets.assetsPurch.vo.AssetsPurchVo;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.service.BaseService;
/**  
 * 设备采购管理Service
 * @Author: wangshujuan
 * @CreateDate: 2017年11月15日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月15日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 */
public interface AssetsPurchService extends BaseService<AssetsPurch>{

	void saveOrUpdateeditInfo(AssetsPurch assetsPurch) throws Exception;

	List<AssetsPurch> queryAllAssetsByData(AssetsPurch assets,String state)throws Exception;

	int getTotalList(AssetsPurch assets,String state)throws Exception;
	
	void stopAssets(String id, String flag);
	
	void del(String id);
	
	void passAssets(String id)throws Exception;
	
	void noPassAssets(String id, String reason)throws Exception;

	AssetsPurch seeReason(String id);

	List<AssetsPurch> querySPAssetsByData(AssetsPurch assets, String state,String startTime,String endTime);

	int getSPTotalList( AssetsPurch assets, String state,String startTime,String endTime);

	List<AssetsPurchVo> queryPurchPlan(String page, String rows,AssetsPurchplan assets,String stated);

	int getTotalPlan(AssetsPurchplan assets,String stated);
	
	int queryPlan(AssetsPurch assets);
}
