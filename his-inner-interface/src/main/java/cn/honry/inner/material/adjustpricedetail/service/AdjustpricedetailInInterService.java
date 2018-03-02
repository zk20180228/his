package cn.honry.inner.material.adjustpricedetail.service;

import cn.honry.base.bean.model.TMatAdjustpricedetail;
import cn.honry.base.service.BaseService;

/**  
 *  
 * @className：AdjustpricedetailInInterService
 * @Description：  物资调价
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface AdjustpricedetailInInterService  extends BaseService<TMatAdjustpricedetail>{
	
	/**
	 * 修改物资字典表、库存明细表、库管表的零售价格
	 * @param storageCode 仓库编码
	 * @param code 物资编码
	 * @param salePrice 零售价格(调价后)
	 */
	public void updateSalePrice(String storageCode, String storageCode2,Double salePrice);
	
}
