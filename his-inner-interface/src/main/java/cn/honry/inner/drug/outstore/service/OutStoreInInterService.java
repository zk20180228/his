package cn.honry.inner.drug.outstore.service;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.service.BaseService;

public interface OutStoreInInterService extends BaseService<DrugOutstore> {


	
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
	void withholdStock(String deptId, String drugId,double applyNum,Integer minunit);
}
