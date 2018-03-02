package cn.honry.inner.drug.apply.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({ "all" })
public interface DrugApplyInInterDAO extends EntityDao<InpatientCancelitemNow>{
	/****
	 * 根据处方号和处方流水号，获取住院药品明细实体
	 * @Title: getChildByRecipe 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param recipeNo
	 * @param sequenceNo
	 * @return InpatientMedicineListNow
	 * @version 1.0
	 */
	InpatientMedicineListNow getChildByRecipe(String recipeNo,Integer sequenceNo);
	
	/***
	 * 根据处方号和处方流水号，获取住院非药品明细实体
	 * @Title: getChildByRecipe 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param recipeNo ：处方号
	 * @param sequenceNo ：处方内序号
	 * @return InpatientItemList
	 * @version 1.0
	 */
	InpatientItemList getItemListByRecipe(String recipeNo,Integer sequenceNo);
	
	/***
	 * 根据处方号和处方流水号获取物资出库记录
	 * @Title: getOutputByRecAndSeq 
	 * @author  WFJ
	 * @createDate ：2016年5月18日
	 * @param recipeNo 处方号
	 * @param sequenceNo 处方内部流水号
	 * @return MatOutput 物资出库记录表实体
	 * @version 1.0
	 */
	MatOutput getOutputByRecAndSeq(String recipeNo,Integer sequenceNo);
	
	/***
	 * 根据处方号和处方流水号，获得住院摆药的出库申请实体
	 * 条件：住院摆药，有效的申请
	 * @Title: obtainApplyout 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param recipeNo ：处方号
	 * @param sequenceNo ：处方流水号
	 * @return DrugApplyout
	 * @version 1.0
	 */
	DrugApplyoutNow obtainApplyout(String recipeNo,Integer sequenceNo);
	
	
	
	void saveOrUpdateList1(List<Object> list);
}
