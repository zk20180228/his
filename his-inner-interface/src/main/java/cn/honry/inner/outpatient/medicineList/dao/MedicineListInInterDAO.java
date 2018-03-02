package cn.honry.inner.outpatient.medicineList.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.outpatient.medicineList.vo.DrugOrUNDrugVo;
import cn.honry.inner.outpatient.medicineList.vo.SpeNalVo;

@SuppressWarnings({"all"})
public interface MedicineListInInterDAO extends EntityDao<OutpatientFeedetailNow>{
	/**  
	 * @Description：收费接口 ： 根据医嘱ID集合查询医嘱
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param：chargeVo 收费参数  
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findByIds(String doctorId);
	/**  
	 * @Description：收费接口 ： 根据门诊号查询挂号信息表
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param： clinicCode 门诊号
	 * @version 1.0
	 */
	RegistrationNow queryInfoByNo(String clinicCode);
	/**  
	 * @Description：收费接口 ： 根据患者病历号查询患者账户
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	OutpatientAccount getAccountByMedicalrecord(String midicalrecordId);
	/**  
	 * @Description：收费接口 ： 如果有交易上限的话查询当天已用金额
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId);
	/**  
	 * @Description：收费接口 ： 根据用户查询员工
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param： userId 用户ID
	 * @version 1.0
	 */
	SysEmployee queryEmployee(String userId);
	/**  
	 * @Description：收费接口 ：查询所有的医技科室
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> chargeImplementDepartmentList();
	/**  
	 * @Description：收费接口 ：查询已经存在的收费序列
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param: clinicCode 门诊号
	 * @version 1.0
	 */
	OutpatientFeedetail queryNOByclinicCode(String clinicCode);
	/**  
	 * @Description：收费接口 ：根据处方号查询要修改的处方
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param: recipeNo 处方号
	 * @version 1.0
	 */
	List<OutpatientRecipedetailNow> queryRecipedetailList(String recipeNo);
	/**  
	 * @Description：收费接口 ：根据处方号查询要修改的处方
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param: recipeNo 处方号
	 * @version 1.0
	 */
	List<OutpatientFeedetail> queryFeedetailRecipeNo(String recipeNo);
	/**  
	 * @Description：收费接口 ：特殊项目配药台
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk：  
	 * @param：execDpcd 药房
	 * @param:id 收费科室
	 * @param:itemType 特殊收费窗口
	 * @version 1.0
	 */
	SpeNalVo querySpeNalVoBy(String execDpcd, String id, Integer itemType);
	/**  
	 * @Description：  发药终端
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk： 
	 * @param: gettCode 终端ID
	 * @version 1.0
	 */
	StoTerminal queryStoTerminalNo(String gettCode);
	/**  
	 * @Description：  查询最小数量的配药终端
	 * @Author：ldl
	 * @CreateDate：2016-04-26
	 * @ModifyRmk： 
	 * @param:execDpcd 执行科室
	 * @version 1.0
	 */
	StoTerminal queryStoTerminal(String execDpcd,String tjfs);
	/**  
	 * @Description：药品表
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-26
	 * @ModifyRmk：  
	 * @param:feedetailIdsArr 药品ID集合
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryDrugInfoList(String feedetailIdsArr);
	/**  
	 * @Description：药品表（一条）
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-26
	 * @ModifyRmk：  
	 * @param:itemCode 药品ID
	 * @version 1.0
	 */
	DrugInfo queryDrugInfoById(String itemCode);
	/**  
	 * @Description：每条药品的医嘱信息
	 * @Author：ldl
	 * @ModifyDate：2016-04-26
	 * @ModifyRmk：  
	 * @param:id 医嘱ID
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryOutFeedetail(String id);
	/**  
	 * @Description：修改发票
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-04-26
	 * @ModifyRmk：  
	 * @param：id 领取人ID； invoiceNo 发票号； invoiceType 发票类型；invoiceNoId发票号所在发票组ID；
	 * @version 1.0
	 */
	void saveInvoiceFinance(String id, String invoiceNo, String invoiceType,String invoiceNoId);
	
	/**  
	 * @Description：  领取发票（工作方法）
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：
	 * @param：id 员工ID
	 * @param:invoiceType 发票类型
	 * @version 1.0
	 */
	Map<String, String> queryFinanceInvoiceNo(String id, String invoiceType);
	/**
	 * @Description 领取多张发票
	 * @author  marongbin
	 * @createDate： 2016年11月14日 下午4:19:45 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String, String> queryFinanceInvoiceNoByNum(String id, String invoiceType,int num);
	/**  
	 * @Description：  查询统计大类名称
	 * @Author：ldl
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：
	 * @param:encode 最小费用encode
	 * @version 1.0
	 */
	MinfeeStatCode minfeeStatCodeByEncode(String encode);
	
	/**获取药品非药品的相关数据
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2016年10月21日 下午5:43:21 
	 * @modifier marongbin
	 * @modifyDate：2016年10月21日 下午5:43:21
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	DrugOrUNDrugVo getDrugOrUNDrugVo (String itemCode,String drugFlag);
	/**
	 * @Description 根据code获取发药终端
	 * @author  marongbin
	 * @createDate： 2016年11月24日 下午6:29:00 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	StoTerminal getStoTerminal(String code);
	
	/**
	 * @Description 根据处方明细id集合获取处方明细
	 * @author  marongbin
	 * @createDate： 2017年2月24日 下午2:04:50 
	 * @modifier 
	 * @modifyDate：
	 * @param feeID
	 * @return: List<OutpatientFeedetailNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> getFeedetailByids(String feeID);
	/**
	 * @Description 根据合同单位code获取合同单位
	 * @author  marongbin
	 * @createDate： 2017年2月24日 下午2:06:07 
	 * @modifier 
	 * @modifyDate：
	 * @param count
	 * @return: BusinessContractunit
	 * @modifyRmk：  
	 * @version 1.0
	 */
	BusinessContractunit queryCountByPaykindCode(String count);
	
	/**
	 * @Description 根据id查询发票组
	 * @author  marongbin
	 * @createDate： 2017年3月11日 下午6:01:07 
	 * @modifier 
	 * @modifyDate：
	 * @param id
	 * @return: FinanceInvoice
	 * @modifyRmk：  
	 * @version 1.0
	 */
	FinanceInvoice getFinInvoiceById(String id);
	/**
	 * @Description 获取该员工下某类型的所有未使用的发票组
	 * @author  marongbin
	 * @createDate： 2017年3月11日 下午6:12:04 
	 * @modifier 
	 * @modifyDate：
	 * @param type 发票类型
	 * @param code 用户工号
	 * @return: List<FinanceInvoice>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<FinanceInvoice> getFinInvoice(String type,String code);
	/**
	 * @Description 根据门诊号，执行科室查询未发药信息
	 * @author  marongbin
	 * @createDate： 2017年3月15日 下午3:40:51 
	 * @modifier 
	 * @modifyDate：
	 * @param clinicCode
	 * @param exeCode
	 * @return: StoTerminal
	 * @modifyRmk：  
	 * @version 1.0
	 */
	StoTerminal checkIshadUnsend(String clinicCode,String exeCode);
	/**  
	 * <p>根据处方号更新处方明细表的收费状态 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月1日 下午6:00:22 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月1日 下午6:00:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param receipeNO
	 * void
	 */
	void updateRecipedetialCharge(String receipeNO);
}
