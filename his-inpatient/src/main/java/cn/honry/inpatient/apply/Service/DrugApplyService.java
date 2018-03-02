package cn.honry.inpatient.apply.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MaterialsCancelmetlist;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.TreeJson;
/**
 * 申请退费业务层
 * @author  lyy
 * @createDate： 2016年1月6日 下午2:21:48 
 * @modifier lyy
 * @modifyDate：2016年1月6日 下午2:21:48  
 * @modifyRmk：  
 * @version 1.0
 */
public interface DrugApplyService extends BaseService<InpatientCancelitemNow> {
	/**
	 * 登录科室下的患者树
	 * @author  lyy
	 * @createDate： 2016年1月6日 下午2:38:09 
	 * @modifier lyy
	 * @modifyDate：2016年1月6日 下午2:38:09  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<TreeJson> treePatient(String deptId) throws Exception;
	
	/***
	 * 根据病历号查询患者住院基本信息
	 * @Title: patientBasicData 
	 * @author  WFJ
	 * @createDate ：2016年4月20日
	 * @param medicalrecordId
	 * 			患者病历号
	 * @return InpatientInfo 住院总表实体
	 * @version 1.0
	 */
	InpatientInfoNow patientBasicData(String inpatientNo);
	
	/***
	 * 根据病历号，查询患者住院流水号
	 * @Title: findInpatientNo 
	 * @author  WFJ
	 * @createDate ：2016年4月21日
	 * @param medicalrecordId
	 * @return String
	 * @version 1.0
	 */
	String findInpatientNo(String medicalrecordId);

/*********************************************************患者药品接口区******************************************************************************/	
	/***																																		
	 * 患者的药品明细信息																																
	 * @Title: getPage 
	 * @author  WFJ
	 * @createDate ：2016年4月20日
	 * @param applySerch
	 * 			存放条件的vo实体
	 * @return List<ApplyVo>
 * 				前台展示vo集合
	 * @version 1.0
	 */
	List<ApplyVo> getPage(ApplyVo applySerch);
	
	/**
	 * 药品退费信息查询
	 * @author  lyy
	 * @createDate： 2016年1月22日 上午11:19:38 
	 * @modifier wfj
	 * @modifyDate：2016年1月22日 上午11:19:38  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<ApplyVo> getPageBack(ApplyVo entity);	
	
	/**
	 * 非药品的查询
	 * @author  lyy
	 * @createDate： 2016年1月8日 下午5:06:43 
	 * @modifier lyy
	 * @modifyDate：2016年1月8日 下午5:06:43  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<ApplyVo> getPageNotDrug(ApplyVo entity);
	
	/**
	 * 非药品退费申请查询
	 * @author  lyy
	 * @createDate： 2016年1月23日 上午11:59:20 
	 * @modifier lyy
	 * @modifyDate：2016年1月23日 上午11:59:20  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<ApplyVo> getPageDrugBack(ApplyVo entity);
	
/*********************************************************患者药品接口区  end******************************************************************************/		
	/***
	 * 从药品和非药品申请列表中获取退费申请单号
	 * @Title: obtainApplyNo 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param list0 ： 药品申请
	 * @param list1 ： 非药品申请
	 * @return String ： 申请单据号
	 * @version 1.0
	 */
	String obtainApplyNo(List<ApplyVo> list0,List<ApplyVo> list1);
	
	/**
	 * 对于已摆药的信息，需要生成退药申请单
	 * @Title: obtainBackDrug 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param vo
	 * @return DrugApplyout
	 * @version 1.0
	 */
	DrugApplyoutNow obtainBackDrug(ApplyVo vo)throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	/***
	 * 根据药品明细表，生成退费申请记录
	 * @Title: obtainVoForMedicine 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param medicine0	: 根据药品id获取的药品明细实体
	 * @param vo	： 前台传输的退药数量
	 * @return InpatientCancelitem
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientCancelitemNow obtainVoForMedicine(InpatientMedicineListNow medicine0,ApplyVo vo) throws Exception;
	
	/***
	 * 住院更新药品预扣库存
	 * @Title: BackingStore 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param applyout
	 * @param num
	 * @return void
	 * @version 1.0
	 */
	void backingStore(DrugApplyoutNow applyout,Double num);
	
	/***
	 * 应对部分退药，根据出库申请重新生成出库申请记录
	 * @Title: againObtainApplyout 
	 * @author  WFJ
	 * @createDate ：2016年5月7日
	 * @param model0 ：出库申请信息
	 * @param vo
	 * @return DrugApplyout
	 * @version 1.0
	 */
	DrugApplyoutNow againObtainApplyout(DrugApplyoutNow model0 ,ApplyVo vo);
	
	/***
	 * 根据非药品明细，产生病区退费申请物资信息
	 * @Title: obtainMatmetlist 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param itemList
	 * @return void
	 * @version 1.0
	 */
	MaterialsCancelmetlist obtainMatmetlist(InpatientItemListNow itemList,ApplyVo vo);
	
	
	/***
	 * 根据住院非药品明细，产生病区退费申请
	 * @Title: obtainVoForItemlist 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param itemList
	 * @param vo
	 * @return InpatientCancelitem
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientCancelitemNow obtainVoForItemlist(InpatientItemListNow itemList,ApplyVo vo) throws Exception;
	
	/***
	 * 直接退费的保存过程条件判断
	 * @Title: directSavePD 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @param medicalrecordId 患者病历号
	 * @param drugList 患者的退费药品集合
	 * @param notDrugList 患者退费的非药品集合
	 * @return Map<String,Object> 提示信息map
	 * @version 1.0
	 */
	Map<String, Object> directSavePD(String medicalrecordId, List<ApplyVo> drugList, List<ApplyVo> notDrugList);
	
	
/*********************************************************退费申请的操作    ******************************************************************************/	
	/***
	 * 退费申请的保存
	 * @Title: saveAdd 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param medicalrecordId ： 患者病历号
	 * @param drugJson	： 药品申请json数据
	 * @param notDrugJson ：非药品申请json数据
	 * @return void
	 * @version 1.0
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	Map<String, Object> saveAdd(String medicalrecordId,String drugJson,String notDrugJson) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception;
	
	/***
	 * 取消申请操作
	 * @Title: delDrugOrNotDrugApply 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param ids :要取消申请的id集合
	 * @return void
	 * @version 1.0
	 */
	Map<String,Object> delDrugOrNotDrugApply(String ids[]);
	
	/***
	 * 直接退费的保存过程
	 * @Title: directSave 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @param medicalrecordId 患者病历号
	 * @param drugList 患者的退费药品集合
	 * @param notDrugList 患者退费的非药品集合
	 * @return Map<String,Object> 提示信息map
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String, Object> directSave(String medicalrecordId, List<ApplyVo> drugList, List<ApplyVo> notDrugList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception;
	
/*********************************************************  渲染  ******************************************************************************/	
	/**
	 * 合同单位下拉框
	 * @author  lyy
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessContractunit> likeContractunit();
	
	
	/***
	 * 根据id获取Bedinfo
	 * @Title: bedinfo 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @param bedinfoId 病床使用表实体
	 * @return InpatientBedinfo id主键
	 * @version 1.0
	 */
	InpatientBedinfoNow getBedinfo(String bedinfoId);

	/***
	 * 根据id获取Bed
	 * @Title: getBed 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @param bedId 病床表主键
	 * @return BusinessHospitalbed 病床表实体
	 * @version 1.0
	 */
	BusinessHospitalbed  getBed(String bedId);
	
	
	
/*-----------------------------------------------------		以下是未知领域  		--------------------------------------------------------------------------------------*/
	/**
	 * 保存申请单
	 * @author  lyy
	 * @createDate： 2016年1月14日 下午7:56:52 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 下午7:56:52  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	void saveAdd(String cancelitemJson,String amount,String deptCode,String drugId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	/**
	 * 药品的总数
	 * @author  lyy
	 * @createDate： 2016年1月7日 下午3:43:25 
	 * @modifier wfj
	 * @modifyDate：2016年1月7日 下午3:43:25  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotal(ApplyVo applySerch);
	
	/**
	 * 退费申请总条数
	 * @author  lyy
	 * @createDate： 2016年1月22日 上午11:18:47 
	 * @modifier lyy
	 * @modifyDate：2016年1月22日 上午11:18:47  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalBack(ApplyVo vo);
	/**
	 * 非药品的总数
	 * @author  lyy
	 * @createDate： 2016年1月8日 下午5:06:03 
	 * @modifier lyy
	 * @modifyDate：2016年1月8日 下午5:06:03  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalNotDrug(ApplyVo vo);

	/**
	 * 住院科室下拉框
	 * @author  lyy
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> likeDept();

	/**
	 * 修改住院药品明细表可退数量 ， 生成药品出库信息
	 * @author  lyy
	 * @createDate： 2016年1月14日 下午7:56:48 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 下午7:56:48  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String editUpdate(String amount,String ids,String cancelitemJson);
	/**
	 * 根据id去查询药品明细表
	 * @author  lyy
	 * @createDate： 2016年1月14日 下午7:56:45 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 下午7:56:45  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientMedicineListNow> getChildByIds(String ids);
	/**
	 * 根据药品编码修改摆药单类型
	 * @author  lyy
	 * @createDate： 2016年1月15日 下午2:33:10 
	 * @modifier lyy
	 * @modifyDate：2016年1月15日 下午2:33:10  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String UpdateApplyOutState(String ids);

	
	String directUpdate(String amounts, String ids,String cancelitemJson,String moneys) throws Exception;


	/**
	 * 非药品退费申请总条数
	 * @author  lyy
	 * @createDate： 2016年1月23日 上午11:58:55 
	 * @modifier lyy
	 * @modifyDate：2016年1月23日 上午11:58:55  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalDrugBack(ApplyVo vo);
	
	/**
	 * 取消退费申请
	 * @author  lyy
	 * @createDate： 2016年1月29日 下午4:37:54 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午4:37:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String delDrugOrNotDrugApply(String id,String flags,String recipeNos,String sequenceNos,String amounts,String balanceStates,String drugId);
	/**
	 * 根据病历号查询患者信息
	 * @author  lyy
	 * @createDate： 2016年1月29日 下午4:37:54 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午4:37:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<ApplyVo> queryInpatientInfo(String medicalrecordId) throws Exception;
	/**
	 * 根据信息查询住院主表
	 * 
	 */
	List<InpatientInfoNow> getInfoList(InpatientInfoNow info);
	/**
	 * 查询床位信息
	 */
	BusinessHospitalbed getBedName(String bedInfoId);
	/**
	 * 查询患者信息
	 * @param rows 
	 * @param page 
	 */
	List<InpatientInfoNow> queryInpatientByMedicalRecordId(String medicalrecordId, String page, String rows);
	/**
	 * 分页查询退费患者信息
	 * @author  lyy
	 * @createDate： 2016年1月29日 下午4:37:54 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午4:37:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> queryInpatientReturns(String inpatientNo, String page, String rows) throws Exception;
	/**
	 * 根据病历号查询患者数量
	 * @author  lyy
	 * @createDate： 2016年1月29日 下午4:37:54 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午4:37:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int queryTotal(String medicalrecordId);
	/**
	 * 根据住院流水号查询患者数量
	 * @author  lyy
	 * @createDate： 2016年1月29日 下午4:37:54 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午4:37:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int queryTotalBy(String inpatientNo);
	
	//以下为移动端调用接口
			/***
			 * 直接退费的保存过程  移动端调用
			 * @Title: directSave 
			 * @author  WFJ
			 * @createDate ：2016年5月16日
			 * @param medicalrecordId 患者病历号
			 * @param drugList 患者的退费药品集合
			 * @param notDrugList 患者退费的非药品集合
			 * @return Map<String,Object> 提示信息map
			 * @version 1.0
			 * @throws Exception 
			 */
			Map<String, Object> directSave(String medicalrecordId,List<ApplyVo> drugList, List<ApplyVo> notDrugList, String deptCode,
					String empJobNo) throws Exception;
			
			/***
			 * 取消申请操作
			 * @Title: delDrugOrNotDrugApply 
			 * @author  WFJ
			 * @createDate ：2016年4月27日
			 * @param ids :要取消申请的id集合
			 * @return void
			 * @version 1.0
			 */
			Map<String, Object> delDrugOrNotDrugApply(String[] idsss, String deptCode,
					String empJobNo);
			
			
			/***
			 * 退费申请的保存  移动端调用
			 * @Title: saveAdd 
			 * @author  WFJ
			 * @createDate ：2016年4月26日
			 * @param medicalrecordId ： 患者病历号
			 * @param drugJson	： 药品申请json数据
			 * @param notDrugJson ：非药品申请json数据
			 * @return void
			 * @version 1.0
			 * @throws NoSuchMethodException 
			 * @throws InvocationTargetException 
			 * @throws IllegalAccessException 
			 * @throws Exception 
			 */
			Map<String, Object> saveAdd(String medicalrecordId, String drugJson,
					String notDrugJson, String deptCode, String empJobNo) throws Exception;

}