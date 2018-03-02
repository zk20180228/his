package cn.honry.inner.drug.apply.service;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.service.BaseService;

public interface DrugApplyInInterService extends BaseService<InpatientCancelitemNow>{
	
	/***
	 * 根据退药单，生成退费申请
	 * @Title: saveAdd 
	 * @author  WFJ
	 * @createDate ：2016年5月9日
	 * @param druglist 退药单集合
	 * @return void
	 * @version 1.0
	 */
	void saveAdd(List<DrugApplyoutNow> druglist);
	
	/***
	 * 根据退药单，直接退费
	 * @Title: directSave 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @param druglist 退药单
	 * @return void
	 * @version 1.0
	 */
	void directSave(List<DrugApplyoutNow> druglist);
	
	
	
	
}
