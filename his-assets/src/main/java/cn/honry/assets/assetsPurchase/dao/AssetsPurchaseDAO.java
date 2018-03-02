package cn.honry.assets.assetsPurchase.dao;

import java.util.List;
import java.util.Map;

import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;


/**
 * @Description  医院员工DAO层
 * @author    zpty
 * @version   1.0 
 * @CreateDate 2015-5-20
 * @Modifier：tangfeishuai
 * @ModifyDate：2016-4-13上午12:02:16  
 * @ModifyRmk：
 */
@SuppressWarnings({"all"})
public interface AssetsPurchaseDAO extends EntityDao<AssetsPurchplan>{

	List<AssetsPurchplan> queryAllAssetsByData(String page, String rows, AssetsPurchplan assets,String state)throws Exception;

	int getTotal(String page, String rows, AssetsPurchplan assets,String state)throws Exception;

	void stopAssets(String id, String flag);

	void passAssets(String id)throws Exception;

	void noPassAssets(String id, String reason)throws Exception;

	AssetsPurchplan seeReason(String id);

	int getSPTotalList(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd);

	Map<String, Object> querySPAssetsByData(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd);

	List<AssetsPurchplan> queryAllAssetsStat(String page, String rows, AssetsPurchplan assets);

	int queryAllAssetsStat(AssetsPurchplan assets);
	/**  
	 * 
	 * 通过设备代码查询设备采购计划
	 * @Author: huzhenguo
	 * @CreateDate: 2017年12月7日 下午2:47:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年12月7日 下午2:47:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	int queryPlan(AssetsPurch assets);


}
