package cn.honry.finance.refund.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoRecipeNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.TecApply;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.dao.EntityDao;

public interface RefundDAO extends EntityDao<OutpatientFeedetail>{
	/**  
	 * @Description：  根据发票号查询发票序号
	 * @Author：ldl
	 * @CreateDate：2016-04-08
	 * @ModifyRmk：
	 * @param:invoiceNo 发票号
	 * @version 1.0
	 */
	List<FinanceInvoiceInfoNow> findInvoiceInfoByInvoiceNo(String invoiceNo);
	/**  
	 * @Description： 根据所有的发票序号所有的发票号
	 * @Author：ldl
	 * @CreateDate：2016-04-08
	 * @ModifyRmk：
	 * @param:invoiceSeqs 发票序号
	 * @version 1.0
	 */
	List<FinanceInvoiceInfoNow> findInvoiceInfoByInvoiceSeqs(String invoiceSeqs);
	/**
	 * @Description 根据发票号集合查询发票主表
	 * @author  marongbin
	 * @createDate： 2016年12月15日 下午8:01:58 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNos 发票号集合
	 * @return: List<FinanceInvoiceInfoNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<FinanceInvoiceInfoNow> findInvoiceInfoByInvoiceNos(String invoiceNos);
	/**  
	 * @Description： 查询系统参数退费有效期限
	 * @Author：ldl
	 * @CreateDate：2016-04-08
	 * @ModifyRmk：
	 * @version 1.0
	 */
	HospitalParameter queryParameter();
	/**  
	 * @Description： 根据所有的发票号查询收费明细表
	 * @Author：ldl
	 * @CreateDate：2016-04-08
	 * @ModifyRmk：
	 * @param:invoiceNos 发票号(多)
	 * @param value  系统参数有效期
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findFeedetailByInvoiceNos(String invoiceNos, String value,String clinicCode);
	/**  
	 * @Description： 根据合同单位代码查询合同单位名称
	 * @Author：ldl
	 * @CreateDate：2016-04-09
	 * @ModifyRmk：
	 * @param:paykindCode 合同单位代码
	 * @version 1.0
	 */
	BusinessContractunit queryContractunit(String paykindCode);
	/**  
	 * @Description：  渲染员工
	 * @Author：ldl
	 * @CreateDate：2016-04-09
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> queryEmpFunction();
	/**  
	 * @Description：  查询系统参数根据收费员进行退费
	 * @Author：ldl
	 * @CreateDate：2016-04-09
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	HospitalParameter queryParameterByRj();
	/**  
	 * @Description：  查询系统参数退费支付方式
	 * @Author：ldl
	 * @CreateDate：2016-04-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	HospitalParameter queryParameterByPayType();
	/**  
	 * @Description： 根据所有的发票序号所有记录(发票明细表)
	 * @Author：ldl
	 * @CreateDate：2016-04-11
	 * @ModifyRmk：
	 * @param:invoiceSeq 发票序号
	 * @version 1.0
	 */
	List<FinanceInvoicedetailNow> findInvoiceTailByInvoiceSeq(String invoiceSeq);
	/**
	 * @Description 根据发票号查询发票明细
	 * @author  marongbin
	 * @createDate： 2016年12月15日 下午7:59:55 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNos 发票号集合
	 * @return: List<FinanceInvoicedetailNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<FinanceInvoicedetailNow> findInvoiceTailByInvoiceNos(String invoiceNos);
	/**  
	 * @Description： 根据所有的发票序号所有记录（支付情况记录表）
	 * @Author：ldl
	 * @CreateDate：2016-04-11
	 * @ModifyRmk：
	 * @param:invoiceSeq 发票序号
	 * @version 1.0
	 */
	List<BusinessPayModeNow> findPayModeListBySeq(String invoiceSeq);
	/**
	 * @Description 根据多个发票号查询支付信息
	 * @author  marongbin
	 * @createDate： 2016年12月15日 下午8:05:37 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNos 发票号集合
	 * @return: List<BusinessPayModeNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessPayModeNow> findPayModeListByNos(String invoiceNos);
	/**  
	 * @Description： 根据所有的发票序号所有记录（收费明细表）
	 * @Author：ldl
	 * @CreateDate：2016-04-11
	 * @ModifyRmk：
	 * @param:invoiceSeq 发票序号
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findFeedetailListBySeq(String invoiceSeq);
	/**
	 * @Description  根据多个发票号查询处方明细
	 * @author  marongbin
	 * @createDate： 2016年12月15日 下午8:07:44 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNos 发票号集合
	 * @return: List<OutpatientFeedetailNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findFeedetailListByNos(String invoiceNos);
	/**  
	 * @Description： 根据处方号查询出所有的记录（处方调剂头表）
	 * @Author：ldl
	 * @CreateDate：2016-04-11
	 * @ModifyRmk：
	 * @param:recipeNo 处方号
	 * @version 1.0
	 */
	List<StoRecipeNow> findStoRecipeByRecipNo(String recipeNo);
	/**
	 * @Description 取最新的数据
	 * @author  marongbin
	 * @createDate： 2016年12月17日 下午4:20:52 
	 * @modifier 
	 * @modifyDate：
	 * @param recipeNo
	 * @return: StoRecipeNow
	 * @modifyRmk：  
	 * @version 1.0
	 */
	StoRecipeNow findStoRecipeByRecipNoOne(String recipeNo);
	/**  
	 * @Description： 根据药品ID查询出所有的记录（出库申请表）
	 * @Author：ldl
	 * @CreateDate：2016-04-12
	 * @ModifyRmk：
	 * @param:drugIds 药品ID集合
	 * @version 1.0
	 */
	List<DrugApplyoutNow> findApplyoutByItemCode(String drugIds,String moOrder);
	/**  
	 * @Description： 根据药品ID查询出所有的记录（收费明细表）
	 * @Author：ldl
	 * @CreateDate：2016-04-12
	 * @ModifyRmk：
	 * @param:drugIds 药品ID集合
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findOutFeedetail(String drugIds);
	/**  
	 * @Description： 根据收费与退费ID得出未退费的项目
	 * @Author：ldl
	 * @CreateDate：2016-04-12
	 * @ModifyRmk：
	 * @param: feeId 收费项目
	 * @param : refundId 退费项目
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findFeedetailListById(String feeId,String refundId);
	/**  
	 * @Description： 根据最小费用代码encode查询统计大类
	 * @Author：ldl
	 * @CreateDate：2016-04-12
	 * @ModifyRmk：
	 * @param : encode 最小费用代码encode
	 * @version 1.0
	 */
	MinfeeStatCode findMinfeeStatCode(String encode);
	/**  
	 * @Description： 根据门诊号查询挂号信息
	 * @Author：ldl
	 * @CreateDate：2016-04-12
	 * @ModifyRmk：
	 * @param : clinicCode 门诊号
	 * @version 1.0
	 */
	RegistrationNow queryRegisterInfo(String clinicCode);
	/**  
	 * @Description： 查询药品重新生成记录
	 * @Author：ldl
	 * @CreateDate：2016-04-12
	 * @ModifyRmk：
	 * @param : feeId 收费项目ID
	 * @param : refundId 退费项目ID
	 * @version 1.0
	 */
	List<OutpatientFeedetailNow> findFeedetailListByIdAndType(String feeId,String refundId);
	/**  
	 * @Description：根据ID查询收费明细表
	 * @Author：ldl
	 * @CreateDate：2016-06-06
	 * @ModifyRmk：
	 * @param : id 收费明细表ID
	 * @version 1.0
	 */
	OutpatientFeedetailNow queryFeedetailById(String id);
	/**  
	 * @Description：根据ID查询收费明细表
	 * @Author：ldl
	 * @CreateDate：2016-06-12
	 * @ModifyRmk：
	 * @param : contractunit 合同单位ID
	 * @version 1.0
	 */
	BusinessContractunit queryContractunitById(String contractunit);
	/**  
	 * @Description：查询患者院内账户
	 * @Author：ldl
	 * @CreateDate：2016-06-12
	 * @ModifyRmk：
	 * @param : patientNo 病历号
	 * @version 1.0
	 */
	OutpatientAccount getAccountByMedicalrecord(String patientNo);
	
	/**
	 * @Description 根据日结号查询日结信息
	 * @author  marongbin
	 * @createDate： 2017年1月12日 上午9:33:38 
	 * @modifier 
	 * @modifyDate：
	 * @param balanceNo
	 * @return: OutpatientDaybalance
	 * @modifyRmk：  
	 * @version 1.0
	 */
	OutpatientDaybalance getDayBalancebyNO(String balanceNo);
	/**
	 * @Description 根据发票号查询退费申请项目
	 * @author  marongbin
	 * @createDate： 2017年3月6日 上午10:38:11 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: List<InpatientCancelitemNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> getCancelItemByInvoiceNo(String invoiceNo);
	/**
	 * @Description 根据申请流水号查询终端确认的项目
	 * @author  marongbin
	 * @createDate： 2017年3月7日 下午7:30:37 
	 * @modifier 
	 * @modifyDate：
	 * @return: DrugUndrug
	 * @modifyRmk：  
	 * @version 1.0
	 */
	TecTerminalApply getTerByapplyNum(String no);
	/**
	 * @Description 根据预约单号获取医技预约项目
	 * @author  marongbin
	 * @createDate： 2017年3月7日 下午7:51:05 
	 * @modifier 
	 * @modifyDate：
	 * @param no
	 * @return: TecApply
	 * @modifyRmk：  
	 * @version 1.0
	 */
	TecApply getTecByapplyNum(String no);
	/**
	 * @Description 根据预约单号查询医技预约申请表
	 * @author  marongbin
	 * @createDate： 2017年3月30日 下午8:32:23 
	 * @modifier 
	 * @modifyDate：
	 * @param no 预约单号 类型  例 : 123','12','321','345
	 * @return: List<TecApply>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<TecApply> getTecByapplyNumList(String no);
	/**
	 * @Description 根据预约单号查询医技终端确认表
	 * @author  marongbin
	 * @createDate： 2017年3月30日 下午8:42:26 
	 * @modifier 
	 * @modifyDate：
	 * @param no 申请流水号 类型  例 : 123','12','321','345
	 * @return: List<TecTerminalApply> 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<TecTerminalApply> getTerByapplyNumList(String no);
	/**
	 * @Description 根据医嘱流水号辅材的主药的已经发药发药状态
	 * @author  marongbin
	 * @createDate： 2017年3月24日 下午7:04:45 
	 * @modifier 
	 * @modifyDate：
	 * @param moorder 医嘱流水号
	 * @return: String 处方状态: 0申请,1打印,2配药,3发药,4还药(当天未发的药品返回货架)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String checkISsend(String moorder);
	/**@see 根据科室code获得所在院区
	 * @author conglin
	 * @param deptCode 科室code
	 * @return String 院区Code
	 */
	String getDeptArea(String deptCode);
	
}
