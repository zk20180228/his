package cn.honry.inner.drug.stockInfo.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugCheckdetail;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.dao.EntityDao;

/**库存维护表可用公共
 * @author abc
 *
 */
@SuppressWarnings({"all"})
public interface BusinessStockInfoInInterDAO extends EntityDao<DrugStockinfo>{
	
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
	 * 根据药品id获取药品基本信息
	 * @Title: findInfoBydrug 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param drugId : 药品id
	 * @return DrugInfo
	 * @version 1.0
	 */
	DrugInfo findInfoById(String drugId);
	
	/***
	 * 根据科室id和药品id,获取info表库存量
	 * @Title: getStockInfo 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param deptId ： 科室id
	 * @param drugId : 药品id
	 * @return DrugStockinfo
	 * @version 1.0
	 */
	DrugStockinfo getStockInfo(String deptId,String drugId);


	/** 
	* @Title: getDrugChecks 
	* @Description: 根据药品code科室code获得药品封账盘点记录
	* @param drugCode 药品code
	* @param deptCode 科室code
	* @author dtl 
	* @date 2017年3月6日
	*/
	List<DrugCheckdetail> getDrugChecks(String drugCode, String deptCode);


	
}
