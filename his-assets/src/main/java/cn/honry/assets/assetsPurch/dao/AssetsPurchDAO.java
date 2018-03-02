package cn.honry.assets.assetsPurch.dao;

import java.util.List;

import cn.honry.assets.assetsPurch.vo.AssetsPurchVo;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.dao.EntityDao;


/**  
 * 设备采购管理Dao
 * @Author: wangshujuan
 * @CreateDate: 2017年11月15日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月15日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 */
@SuppressWarnings({"all"})
public interface AssetsPurchDAO extends EntityDao<AssetsPurch>{

	List<AssetsPurch> queryAllAssetsByData(AssetsPurch assets,String state)throws Exception;

	int getTotal(AssetsPurch assets,String state)throws Exception;

	void stopAssets(String id, String flag);

	void passAssets(String id)throws Exception;

	void noPassAssets(String id, String reason)throws Exception;

	AssetsPurch seeReason(String id);

	int getSPTotalList(AssetsPurch assets, String state,String startTime,String endTime);

	List<AssetsPurch> querySPAssetsByData( AssetsPurch assets, String state,String startTime,String endTime);

	List<AssetsPurchVo> queryPurchPlan(String page, String rows,AssetsPurchplan assets,String stated);

	int getTotalPlan(AssetsPurchplan assets,String stated);
	
	List<AssetsPurch> queryDevicePurch(String officeCode,String page, String rows, String menuAlias);

	int queryTotalPurch(String code);
	
	int queryPlan(AssetsPurch assets);
}
