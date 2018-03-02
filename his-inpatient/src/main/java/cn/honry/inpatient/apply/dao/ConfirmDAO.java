package cn.honry.inpatient.apply.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.apply.vo.ApplyVo;
/**
 * 确认退费信息
 * @author  lyy
 * @createDate： 2016年1月30日 下午4:00:21 
 * @modifier lyy
 * @modifyDate：2016年1月30日 下午4:00:21  
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({ "all" })
public interface ConfirmDAO extends  EntityDao<InpatientCancelitemNow> {
	/**
	 * 查询药品退费信息的总条数
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:04:49 
	 * @modifier lyy
	 * @modifyDate：2016年1月30日 下午4:04:49  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTatalDrugConfirm(ApplyVo entity) throws Exception;
	/**
	 * 查询非药品退费信息
	 * @author  lyy
	 * @createDate： 2016年2月1日 上午10:01:01 
	 * @modifier lyy
	 * @modifyDate：2016年2月1日 上午10:01:01  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<ApplyVo> getPageDrugConfirm(ApplyVo apply,String page,String rows) throws Exception;
	/**
	 * 查询非药品退费信息的总条数
	 * @author  lyy
	 * @createDate： 2016年1月30日 下午4:04:49 
	 * @modifier lyy
	 * @modifyDate：2016年1月30日 下午4:04:49  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	int getTatalNotDrugConfirm(ApplyVo entity) throws Exception;
	/**
	 * 查询非药品退费信息
	 * @author  lyy
	 * @createDate： 2016年2月1日 上午10:01:32 
	 * @modifier lyy
	 * @modifyDate：2016年2月1日 上午10:01:32  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<ApplyVo> getPageNotDrugConfirm(ApplyVo apply,String page,String rows) throws Exception;
	/**
	 * 退费通过
	 * @author  lyy
	 * @createDate： 2016年2月3日 上午11:59:50 
	 * @modifier lyy
	 * @modifyDate：2016年2月3日 上午11:59:50  
	 * @modifyRmk：  
	 * @version 1.0
	 * @param deptName 
	 * @param userName 
	 * @throws Exception 
	 */
	String confirmBack(String ids,String userId, String deptId, String userName, String deptName) throws Exception;
	/**
	 * 根据退费ids查询
	 * @author  lyy
	 * @createDate： 2016年2月3日 上午11:59:50 
	 * @modifier lyy
	 * @modifyDate：2016年2月3日 上午11:59:50  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientCancelitemNow> getChildByIds(String ids) throws Exception;
/****************************************************************   分割线       *************************************************************************************/
	
	/****
	 * 根据处方号和处方流水号，获取住院药品明细实体
	 * @Title: getChildByRecipe 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param recipeNo
	 * @param sequenceNo
	 * @return InpatientMedicineList
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientMedicineListNow getChildByRecipe(String recipeNo,Integer sequenceNo) throws Exception;
	
	/***
	 * 根据处方号和处方流水号，获取住院非药品明细实体
	 * @Title: getChildByRecipe 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param recipeNo ：处方号
	 * @param sequenceNo ：处方内序号
	 * @return InpatientItemList
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientItemListNow getItemListByRecipe(String recipeNo,Integer sequenceNo) throws Exception;
	
	/***
	 * 根据处方号和处方流水号获取物资出库记录
	 * @Title: getOutputByRecAndSeq 
	 * @author  WFJ
	 * @createDate ：2016年5月18日
	 * @param recipeNo 处方号
	 * @param sequenceNo 处方内部流水号
	 * @return MatOutput 物资出库记录表实体
	 * @version 1.0
	 * @throws Exception 
	 */
	MatOutput getOutputByRecAndSeq(String recipeNo,Integer sequenceNo) throws Exception;
	
	/***
	 * 根据处方号和处方流水号获取物资出库记录
	 * @Title: getOutputByRecAndSeq 
	 * @author  WFJ
	 * @throws Exception 
	 */
	void updateMatOutput(MatOutput output) throws Exception;
	
	
	/***
	 * 根据手术序号更新手术申请表的收费标记
	 * @Title: updateOperApply 
	 * @author  zxl
	 * @createDate ：2016年5月18日
	 * @param operationId 手术序号
	 * @return  
	 * @version 1.0
	 * @throws Exception 
	 */
	void updateOperApply(List<String> newList) throws Exception;
	
	/***
	 * 根据手术序号更新手术记录表的收费标记
	 * @Title: updateOperRecord 
	 * @author  zxl
	 * @createDate ：2016年5月18日
	 * @param operationId 手术序号
	 * @return  
	 * @version 1.0
	 * @throws Exception 
	 */
	void updateOperRecord(List<String> newList) throws Exception;
	
	/***
	 * 根据手术序号更新手术申请表的收费标记
	 * @Title: updateOperApply 
	 * @author  zxl
	 * @createDate ：2016年5月18日
	 * @param operationId 手术序号
	 * @return  
	 * @version 1.0
	 * @throws Exception 
	 */
	void updateOperApply(List<String> newList,String empJobNo) throws Exception;
	/***
	 * 根据手术序号更新手术记录表的收费标记
	 * @Title: updateOperRecord 
	 * @author  zxl
	 * @createDate ：2016年5月18日
	 * @param operationId 手术序号
	 * @return  
	 * @version 1.0
	 * @throws Exception 
	 */
	void updateOperRecord(List<String> newList,String empJobNo) throws Exception;
	
}
