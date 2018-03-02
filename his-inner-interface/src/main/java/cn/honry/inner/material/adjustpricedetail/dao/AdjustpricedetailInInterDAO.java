package cn.honry.inner.material.adjustpricedetail.dao;

import java.util.List;

import cn.honry.base.bean.model.MatStockInfo;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.base.bean.model.TMatAdjustpricedetail;
import cn.honry.base.bean.model.TMatAdjustpricehead;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：AdjustpricedetailInInterDAO
 * @Description：   物资调价
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface AdjustpricedetailInInterDAO extends EntityDao<TMatAdjustpricedetail>{
	
	/***
	 * 
	 * @Description:根据仓库代码和物品码查询物品库存信息
	 * @author  wfj
	 * @date 创建时间：2016年3月3日
	 * @version 1.0
	 * @param:storageCode：仓库代码  
	 * itemCode：物品编码
	 */
	List<MatStockdetail> matisaAmple(String storageCode,String itemCode);
	
	/***
	 * 保存或更新库存明细表
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月4日
	 * @version 1.0
	 */
	void saveOrupdate(MatStockdetail model);
	
	/**
	 * @Description:根据物资编码查询库存信息
	 * @param itemCode 物资编码
	 */
	List<MatStockInfo> queryStockinfo(String itemCode);
	
	/***
	 * 根据调价单据号，查询调价主表信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月2日
	 * @version 1.0
	 */
	List<TMatAdjustpricehead> queryForAdjustListCode(String adjustListCode);

}
