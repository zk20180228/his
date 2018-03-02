package cn.honry.statistics.finance.inoutstore.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.statistics.finance.inoutstore.vo.StoreSearchVo;

/**  
 *  住院药房入出库台账查询Service接口
 * @Author:luyanshou
 * @version 1.0
 */
public interface InOutStoreService {

	/**
	 * 查询药品名称和编码
	 */
	public List<StoreResultVO> getdrugInfo(String q) throws Exception;
	
	/**
	 * 查询入出库记录数据
	 */
	public Map getStoreData(StoreSearchVo vo,int firstResult,int page,int rows) throws Exception;
}
