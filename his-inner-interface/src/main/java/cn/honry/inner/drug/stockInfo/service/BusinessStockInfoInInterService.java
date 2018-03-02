package cn.honry.inner.drug.stockInfo.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.service.BaseService;

/**库存维护表公共
 * @author abc
 *
 */
public interface BusinessStockInfoInInterService extends BaseService<DrugStockinfo>{
	/** 根据科室和药品得到该药品的可用数量（库存数量减预扣库存数量）
	 * @Description: 根据科室和药品得到该药品的可用数量（库存数量减预扣库存数量）
	 * @param storageDeptId 部门
	 * @param drugId 药品
	 * @Author: dutianliang
	 * @CreateDate: 2016年4月18日
	 * @Version: V 1.0
	 */
	Double getDrugStockInfoStoreNum(String storageDeptId,String drugId);
	
	/***
	 * 
	 * @Title: withholdStock 
	 * @Description:预扣库存并更新库存量，预扣库存量，进行单位数量计算
	 * @author  wfj
	 * @date 创建时间：2016年4月17日
	 * @param deptId ： 科室id
	 * @param drugId : 药品id
	 * @param applyNum : 预扣数量
	 * @param minunit ： 预扣数量的单位标记（0最小单位,1包装单位）
	 * @version 1.0
	 * @since
	 */
	String withholdStock(String deptId, String drugId,double applyNum,String minunit);
	/** 根据库存科室，药品编码，申请数量判断药品库存是否充足
	* @Title: enoughYn 
	* @Description: 根据库存科室，药品编码，申请数量判断药品库存是否充足
	* @param deptCode 库存科室
	* @param drugCode 药品code
	* @param applyNum 申请数量
	* @return String    充足返回null 
	* @author dtl 
	* @date 2017年1月11日
	*/
	String enoughYn(String deptCode, String drugCode,double applyNum);
	
	
	/** 查询药品是否可用并且库存充足（兼容数量未经过处理与已经过处理的库存判断）  <br>1.状态：（药品是否删除、停用、封账盘点） <br>2.库存：（需要判断库存的情况下库存数量-预扣数量-申请数量是否大于等于0，否则判断科室下是否有库存记录）
	 * 当isStatus为true时考虑药品状态，当isStock为true时考虑库存数量。
	 * <br>返回信息：Map valiuFlag（int）：0不通过，1可用；  failMesgs（String）：不可用信息
	* @Title: getValiuNum 
	* @param deptCode 库存科室code
	* @param drugCodes 申请药品code list
	* @param applyNums 申请数量 list
	* @param showFlags 申请单位 list(0最小单位,1包装单位)
	* @param showInfo 是否考虑状态（true：药品不能为删除、停用、封账盘点状态，false：对状态不做考虑）
	* @param isStock 是否考虑库存（true：需要判断库存的情况下库存数量-预扣数量-申请数量是否大于等于0；false：否则判断是否有库存记录）
	* @param isStatus 是否已于统一为drugInfo的单位（true：表示数量的单位已经统一为drugInfo的单位；false：表示未经过处理）
	* @return Map valiuFlag（int）：0不通过，1可用；  failMesgs（String）：不可用信息
	* @throws 
	* @author dtl 
	* @date 2017年3月6日
	*/
	Map<String, Object> ynValiuDrug(String deptCode, List<String> drugCodes, List<Double> applyNums, List<Integer> showFlags, boolean showInfo, boolean isStock, boolean isStatus);
	
}
