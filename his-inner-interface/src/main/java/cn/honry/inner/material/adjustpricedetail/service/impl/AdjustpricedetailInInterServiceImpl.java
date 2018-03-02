package cn.honry.inner.material.adjustpricedetail.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MatBaseinfo;
import cn.honry.base.bean.model.MatStockInfo;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.base.bean.model.TMatAdjustpricedetail;
import cn.honry.inner.material.adjustpricedetail.dao.AdjustpricedetailInInterDAO;
import cn.honry.inner.material.adjustpricedetail.service.AdjustpricedetailInInterService;
import cn.honry.inner.material.baseinfo.dao.BaseinfoInInterDAO;

/**  
 *  
 * @className：AdjustpricedetailInInterServiceImpl
 * @Description：  物资调价
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("adjustpricedetailInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class AdjustpricedetailInInterServiceImpl implements AdjustpricedetailInInterService{

	@Autowired
	@Qualifier(value = "adjustpricedetailInInterDAO")
	private AdjustpricedetailInInterDAO adjustpricedetailInInterDAO;
	
	@Autowired	//物资字典dao
	@Qualifier(value = "baseinfoInInterDAO")
	private BaseinfoInInterDAO baseinfoInInterDAO;
	
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public TMatAdjustpricedetail get(String id) {
		return adjustpricedetailInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(TMatAdjustpricedetail entity) {
		
	}
	
	/**
	 * 修改物资字典表、库存明细表、库管表的零售价格
	 * @param storageCode 仓库编码
	 * @param code 物资编码
	 * @param salePrice 零售价格(调价后)
	 */
	@Override
	public void updateSalePrice(String storageCode,String code,Double salePrice){
		List<MatStockdetail> matStockdetailList=adjustpricedetailInInterDAO.matisaAmple(storageCode,code);//获取库存明细信息
		if(matStockdetailList.size()>0){
			for (MatStockdetail matStockdetail : matStockdetailList) {//修改库存明细信息的零售价格和零售金额
				matStockdetail.setSalePrice(salePrice);
				matStockdetail.setSaleCost(salePrice*matStockdetail.getStoreNum());
				adjustpricedetailInInterDAO.saveOrupdate(matStockdetail);
			}
		}
		List<MatStockInfo> stockinfoList = adjustpricedetailInInterDAO.queryStockinfo(code);//获取库管表信息
		if(stockinfoList.size()>0){
			for (MatStockInfo matStockInfo : stockinfoList) {//修改库管表记录的零售价格
				matStockInfo.setRetailPrice(salePrice);
			}
		}
		List<MatBaseinfo> baseinfoList = baseinfoInInterDAO.queryByItemCode(code);//获取物资字典表的信息
		if(baseinfoList.size()>0){
			MatBaseinfo matBaseinfo = baseinfoList.get(0);
			matBaseinfo.setSalePrice(salePrice);//修改物资字典表信息中的零售价格
		}
	}

}
