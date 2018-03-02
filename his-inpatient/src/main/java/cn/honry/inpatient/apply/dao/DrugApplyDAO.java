package cn.honry.inpatient.apply.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.MatBaseinfo;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.base.bean.model.MatUndrugCompare;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.apply.vo.ApplyVo;
/**
 * 申请退费数据库层
 * @author  lyy
 * @createDate： 2016年1月6日 下午2:19:38 
 * @modifier lyy
 * @modifyDate：2016年1月6日 下午2:19:38  
 * @modifyRmk：  
 * @version 1.0
 */
@SuppressWarnings({ "all" })
public interface DrugApplyDAO extends EntityDao<InpatientCancelitemNow>{
	/**
	 * 登录科室下的患者树
	 * @author  lyy
	 * @createDate： 2016年1月6日 下午2:36:54 
	 * @modifier lyy
	 * @modifyDate：2016年1月6日 下午2:36:54  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> treeQuery(String deptId) throws Exception;
	
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
	
	/**
	 * 药品退费申请查询
	 * @author  lyy
	 * @createDate： 2016年1月22日 上午11:19:38 
	 * @modifier wfj
	 * @modifyDate：2016年1月22日 上午11:19:38  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<ApplyVo> getPageBack(ApplyVo entity);
	
	/**
	 * 药品的总数
	 * @author  lyy
	 * @createDate： 2016年1月7日 下午3:43:25 
	 * @modifier lyy
	 * @modifyDate：2016年1月7日 下午3:43:25  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotal(ApplyVo vo);
	/**
	 * 药品的查询
	 * @author  lyy
	 * @createDate： 2016年1月7日 下午3:43:25 
	 * @modifier lyy
	 * @modifyDate：2016年1月7日 下午3:43:25  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<ApplyVo> getPage(ApplyVo entity);
	
	/***
	 * 非药品列表信息查询
	 * @Title: getPageNotDrug 
	 * @author  WFJ
	 * @createDate ：2016年4月25日
	 * @param entity
	 * @return List<ApplyVo>
	 * @version 1.0
	 */
	List<ApplyVo> getPageNotDrug(ApplyVo entity);
	
	/***
	 * 根据库存的流水号，查询库存序号
	 * @Title: findStockNo 
	 * @author  WFJ
	 * @createDate ：2016年4月25日
	 * @param stockCode
	 * @return String
	 * @version 1.0
	 */
	String findStockNo(String stockCode);
	
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
	
	/***
	 * 根据id,获取住院药品明细实体
	 * @Title: getChildById 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param id
	 * @return InpatientMedicineList 祝愿药品明细实体
	 * @version 1.0
	 */
	InpatientMedicineListNow getChildById(String id);
	
	/****
	 * 根据处方号和处方流水号，获取住院药品明细实体
	 * @Title: getChildByRecipe 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param recipeNo
	 * @param sequenceNo
	 * @return InpatientMedicineList
	 * @version 1.0
	 */
	InpatientMedicineListNow getChildByRecipe(String recipeNo,Integer sequenceNo);
	
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
	
	/***
	 * 根据处方号和处方流水号，获得住院摆药的出库申请实体
	 * 条件：住院摆药，无效的申请，按照UP时间倒叙，第一条
	 * @Title: obtainApplyout 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param recipeNo ：处方号
	 * @param sequenceNo ：处方流水号
	 * @return DrugApplyout
	 * @version 1.0
	 */
	DrugApplyoutNow obtainApplyoutdesc(String recipeNo,Integer sequenceNo);
	
	/***
	 * 根据处方号和处方内序号，查询出库申请表的住院退费信息
	 * @Title: findByApplyout 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param recipeNo
	 * @param sequenceNo
	 * @return DrugApplyout
	 * @version 1.0
	 */
	DrugApplyoutNow findByApplyout(String recipeNo,Integer sequenceNo);
	
	
	/***
	 * 根据处方号和处方流水号，查询出在退费申请过程中被标为无效的住院摆药
	 * @Title: validApplyout 
	 * @author  WFJ
	 * @createDate ：2016年4月28日
	 * @param recipeNo
	 * @param sequenceNo
	 * @return DrugApplyout
	 * @version 1.0
	 * @throws Exception 
	 */
	DrugApplyoutNow oldApplyout(String recipeNo,Integer sequenceNo) throws Exception;
	
	
	/***
	 * DrugApplyout 批量保存更新
	 * @Title: saveOrUpdate 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param listApplyout
	 * @return void
	 * @version 1.0
	 */
	void saveOrUpdate(List<DrugApplyoutNow> listApplyout);
	
	/**
	 * 根据非药品物资对照表的物资编码去查物资字典表
	 * @author  lyy
	 * @createDate： 2016年2月27日 下午12:05:05 
	 * @modifier lyy
	 * @modifyDate：2016年2月27日 下午12:05:05  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	MatBaseinfo querybaseInfo(String compareId);
	
	/**
	 * 根据非药品id 去查询非药品物资对照表
	 * @author  lyy
	 * @createDate： 2016年2月22日 下午1:53:15 
	 * @modifier lyy
	 * @modifyDate：2016年2月22日 下午1:53:15  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	MatUndrugCompare queryUndrugCompare(String itemCode);
	
	/***
	 * 根据id,获取住院非药品明细实体
	 * @Title: getChildById 
	 * @author  WFJ
	 * @createDate ：2016年4月26日
	 * @param id
	 * @return InpatientMedicineList 住院非药品明细实体
	 * @version 1.0
	 */
	InpatientItemListNow getItemListById(String id);
	
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
	InpatientItemListNow getItemListByRecipe(String recipeNo,Integer sequenceNo);
	
	/***
	 * 根据出库流水号，获取物资出库记录实体
	 * @Title: findMatOPbyNO 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param outNo
	 * @return MatOutput
	 * @version 1.0
	 */
	MatOutput findMatOPbyNO(String outNo);
	
	
	/***
	 * 根据id,获取病区退费申请实体集合
	 * @Title: findCancelitemByIds 
	 * @author  WFJ
	 * @createDate ：2016年4月27日
	 * @param ids
	 * @return List<InpatientCancelitem>
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> findCancelitemByIds(String ids[]);
	
	/***
	 * 获取摆药通知实体
	 * @Title: getMsg 
	 * @author  WFJ
	 * @createDate ：2016年5月19日
	 * @param billclassCode 摆药单分类代码
	 * @param sendType 摆药类型  1集中发送，2临时发送，3全部 
	 * @param sendFlag 摆药标记0-通知1-已摆
	 * @param medDeptCode 取药科室
	 * @return InpatientStoMsg 摆药通知实体
	 * @version 1.0
	 */
	InpatientStoMsgNow getMsg(String  billclassCode,String  sendType,String  sendFlag,String   medDeptCode);
	
	/***
	 * 根据摆药通知类型，查询出库申请表中相关类型的通知数据
	 * @Title: getApplyout 
	 * @author  WFJ
	 * @createDate ：2016年5月19日
	 * @param billclassCode 摆药单分类代码
	 * @param sendType 摆药类型  1集中发送，2临时发送，3全部 
	 * @param sendFlag 摆药标记0-通知1-已摆
	 * @param medDeptCode 取药科室
	 * @return DrugApplyout 出库申请集合
	 * @version 1.0
	 */
	List<DrugApplyoutNow> getApplyout(String  billclassCode,String  sendType,String   medDeptCode);
	
/*********************************************************  渲染  ******************************************************************************/
	
	
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
	 * 合同单位下拉框
	 * @author  lyy
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessContractunit> likeContractunit();
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
	 * 根据退费数量去更改药品明细表的可退数量
	 * @author  lyy
	 * @createDate： 2016年1月14日 上午10:29:46 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 上午10:29:46  
	 * @modifyRmk：   amount 退费数量
	 * @version 1.0
	 */
	String updateMediceList(int amount,String id);
	/**
	 * 根据药品明细表id查询
	 * @author  lyy
	 * @createDate： 2016年1月14日 上午10:29:46 
	 * @modifier lyy
	 * @modifyDate：2016年1月14日 上午10:29:46  
	 * @modifyRmk：   amount 退费数量
	 * @version 1.0
	 */
	List<InpatientMedicineListNow> getChildByIds(String ids);
	/**
	 * 根据id把申请状态修改为 作废 状态  （多条修改）
	 * @author  lyy
	 * @createDate： 2016年1月18日 上午10:44:47 
	 * @modifier lyy
	 * @modifyDate：2016年1月18日 上午10:44:47  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String updateApplyOutState(String ids);
	/**
	 * 修改库存表
	 * @author  lyy
	 * @createDate： 2016年1月19日 下午2:41:40 
	 * @modifier lyy
	 * @modifyDate：2016年1月19日 下午2:41:40  
	 * @modifyRmk： num修改后的数量   drugId 药品名称 dept 扣库科室 
	 * @version 1.0
	 */
	String updateStorage(int num,String id,String dept);
	/**
	 * 修改库存维护表
	 * @author  lyy
	 * @createDate： 2016年1月20日 上午10:24:25 
	 * @modifier lyy
	 * @modifyDate：2016年1月20日 上午10:24:25  
	 * @modifyRmk：   num修改后的数量   drugId 药品名称 dept 扣库科室
	 * @version 1.0
	 */
	String updateStockInfo(int num,String drugId,String dept);
	/**
	 * 药品退费申请总条数
	 * @author  lyy
	 * @createDate： 2016年1月22日 上午11:18:47 
	 * @modifier lyy
	 * @modifyDate：2016年1月22日 上午11:18:47  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotalBack(ApplyVo vo);
	
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
	 * 根据处方号和处方流水号查询药品明细
	 * @author  lyy
	 * @createDate： 2016年2月17日 下午1:58:39 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午1:58:39  
	 * @modifyRmk：   recipeNo 处方号 、 sequenceNo 处方流水号
	 * @version 1.0
	 */
	List<InpatientMedicineListNow> getChild(String recipeNo, String sequenceNo);
	/**
	 * 根据处方号、处方流水号、可退数量和结算状态修改药品明细表
	 * @author  lyy
	 * @createDate： 2016年2月17日 下午1:58:39 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午1:58:39  
	 * @modifyRmk：  recipeNo 处方号 、 sequenceNo 处方流水号 amount 可退数量、 balanceState 结算状态
	 * @version 1.0
	 */
	String updateInpatientMedList(String recipeNo, String sequenceNo, int amount, int balanceState);
	/**
	 * 根据处方号和处方流水号修改出库表中的状态
	 * @author  lyy
	 * @createDate： 2016年2月17日 下午5:07:23 
	 * @modifier lyy
	 * @modifyDate：2016年2月17日 下午5:07:23  
	 * @modifyRmk： deptId 登录科室  recipeNo 处方号 、 sequenceNo 处方流水号
	 * @version 1.0
	 */
	void updateApplyState(String deptId,String recipeNo, String sequenceNo);
	/**
	 * 根据处方号和处方流水号查询出库表中的
	 * @author  lyy
	 * @createDate： 2016年2月18日 上午10:55:39 
	 * @modifier lyy
	 * @modifyDate：2016年2月18日 上午10:55:39  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	DrugApplyoutNow QueryApplyOut(String recipeNo, String sequenceNo);
	/**
	 * 修改药品库存表中（还回）预扣库存
	 * @author  lyy
	 * @createDate： 2016年2月18日 下午2:56:46 
	 * @modifier lyy
	 * @modifyDate：2016年2月18日 下午2:56:46  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String amendStorage(int amount, String drugId);
	/**
	 * 修改药品库存维护表中（还回）预扣库存
	 * @author  lyy
	 * @createDate： 2016年2月18日 下午2:56:46 
	 * @modifier lyy
	 * @modifyDate：2016年2月18日 下午2:56:46  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String amendStockInfo(int amount, String drugId);
	/**
	 * 根据处方号和处方流水号去查询
	 * @author  lyy
	 * @createDate： 2016年2月19日 上午10:38:51 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 上午10:38:51  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	DrugApplyoutNow queryDrdugApply(String recipeNo,String sequenceNo);
	/**
	 * 根据处方号和处方流水号非药品明细表
	 * @author  lyy
	 * @createDate： 2016年2月19日 上午11:00:17 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 上午11:00:17  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientItemListNow> getChildNotDrug(String recipeNo, String sequenceNo);
	/**
	 * 根据处方号和处方流水号去修改有效标记为有效
	 * @author  lyy
	 * @createDate： 2016年2月19日 上午11:25:55 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 上午11:25:55  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	void updateApplyOut(String recipeNo, String sequenceNo);
	/**
	 * 根据处方号和处方流水号去修改摆药通知单通知摆药标记为通知状态
	 * @author  lyy
	 * @createDate： 2016年2月19日 上午11:36:47 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 上午11:36:47  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	void updateStoMsg(String bill);
	/**
	 * 根据处方号、处方流水号、可退数量和结算状态修改非药品明细表
	 * @author  lyy
	 * @createDate： 2016年2月19日 下午1:51:42 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 下午1:51:42  
	 * @modifyRmk：  recipeNo 处方号 、 sequenceNo 处方流水号 amount 可退数量、 balanceState 结算状态
	 * @version 1.0
	 */
	String updateInpatientItemList(String recipeNo, String sequenceNo, int amount, int balanceState);
	/**
	 * 根据处方号、处方流水号、修改查询物资出库表
	 * @author  lyy
	 * @createDate： 2016年2月19日 下午2:14:24 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 下午2:14:24  
	 * @modifyRmk：   recipeNo  处方号  sequenceNo  处方流水号
	 * @version 1.0
	 */
	MatOutput queryOutPut(String recipeNo, String sequenceNo);
	/**
	 * 根据申请流水号、出库单流水号以及库存序  修改物资退费申请表可退数量
	 * @author  lyy
	 * @createDate： 2016年2月19日 下午2:46:16 
	 * @modifier lyy
	 * @modifyDate：2016年2月19日 下午2:46:16  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String cancelmetList(int amount,String applyNo,String outNo, String stockNo);

	/**
	 * 根据物资编号 去查询物资字典表
	 * @author  lyy
	 * @createDate： 2016年2月22日 下午2:08:21 
	 * @modifier lyy
	 * @modifyDate：2016年2月22日 下午2:08:21  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<MatBaseinfo> queryUnDrugCode(String compareId);
	/**
	 *  根据物资编号 去查询物资明细表
	 * @author  lyy
	 * @createDate： 2016年2月22日 下午3:04:22 
	 * @modifier lyy
	 * @modifyDate：2016年2月22日 下午3:04:22  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	MatStockdetail queryStockNo(String compareId);
	
	List<ApplyVo> queryInpatientInfo(String medicalrecordId) throws Exception;

	/**
	 * 根据字典表的id和批次管理查询物资字典表
	 * @author  lyy
	 * @createDate： 2016年2月27日 下午3:21:03 
	 * @modifier lyy
	 * @modifyDate：2016年2月27日 下午3:21:03  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	MatBaseinfo queryMatBase(String baseId, int batchFlag);
	/**
	 *根据id修改非药品中的数量
	 * @author  lyy
	 * @createDate： 2016年2月29日 上午9:46:22 
	 * @modifier lyy
	 * @modifyDate：2016年2月29日 上午9:46:22  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String updateItemList(int amount, String id);
	/**
	 * 根据id去查询退费申请表
	 * @author  lyy
	 * @createDate： 2016年2月29日 上午10:17:20 
	 * @modifier lyy
	 * @modifyDate：2016年2月29日 上午10:17:20  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InpatientCancelitemNow queryCancelItem(String id);
	/**
	 * 根据住院流水号查询费用汇总表
	 * @author  lyy
	 * @createDate： 2016年2月29日 下午2:07:35 
	 * @modifier lyy
	 * @modifyDate：2016年2月29日 下午2:07:35  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	InpatientFeeInfoNow queryFeeInfo(String ipatientNo);
	/**
	 * 根据住院流水号去修改住院主表中的费用金额
	 * @author  lyy
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @modifier lyy
	 * @modifyDate：2016年2月29日 下午4:00:54  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String updateInpatientInfo(Double money,String ipatientNo);
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
	 * 根据id去查询（ 申请退费实体类）
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	InpatientCancelitemNow getById(String applyNo);

	/**
	 * 获取序列
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @param seq  序列名   i生成位数
	 * @version 1.0
	 */
	String getSeqByNameorNumNew(String seq, int i);

	/**
	 * 更新住院药品明细表
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void updateMedicine(InpatientMedicineListNow medicine);

	/**
	 * 更新住院药品出申请表
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void updateDrugApplyout(DrugApplyoutNow applyout);

	/**
	 * 更新住院非药品明细表
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void updateInpatientItem(InpatientItemListNow itemList);

	/**
	 * 更新退费申请实体类
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void saveOrUpdateCancelitemList(List<InpatientCancelitemNow> list);

	/**
	 * 根据sequence 获取申请流水号
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	Object getDrugApplyoutSequece(String string);

	/**
	 * 更新出库申请表
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void saveOrUpdateListApplyout(List<DrugApplyoutNow> listApplyout);
	/**
	 * 更新出库申请表
	 * @author  donghe
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void saveOrUpdateList1(List<DrugApplyoutNow> listApplyout);
	/**
	 * 根据病历号查询患者信息
	 * @param rows 
	 * @param page 
	 */
	List queryInpatientByMedicalRecordId(String medicalrecordId, String page, String rows);
	/**
	 * 分页查询退费信息
	 * @param rows 
	 * @param page 
	 */
	List queryInpatientReturns(String inpatientNo, String page, String rows);
	/**
	 * 根据病历号查询患者数量
	 * @param rows 
	 * @param page 
	 */
	int queryTotal(String medicalrecordId);
	/**
	 * 根据住院流水号查询信息总量
	 * @param rows 
	 * @param page 
	 */
	int queryTotalBy(String inpatientNo);
	
	
	/**
	 * 更新退费申请实体类
	 * @author  zhuxiaolu
	 * @createDate： 2016年2月29日 下午4:00:54 
	 * @version 1.0
	 */
	void saveOrUpdateCancelitemList(List<InpatientCancelitemNow> list,String empJobNo);
}
