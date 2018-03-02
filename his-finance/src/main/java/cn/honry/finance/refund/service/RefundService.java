package cn.honry.finance.refund.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

public interface RefundService extends BaseService<OutpatientFeedetail>{
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
	 * @param value 系统参数有效期
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
	 * @Description：部分退费患者账户费保存
	 * @Author：ldl
	 * @CreateDate：2016-06-08
	 * @ModifyRmk：  
	 * @param： patientNo 病历号
	 * @param： drugList 收费药品
	 * @param： undDrugList 收费非药品
	 * @param： refundDrugList 退费药品
	 * @param： refundUnDrugList 退费非药品
	 * @param： maney 退费金额
	 * @param： invoiceNo 原来的发票号
	 * @param： price 
	 * @param： payType 退费路径
	 * @param newInvoiceNo 新发票
	 * @param： invoiceNo 原发票号
	 * @version 1.0
	 */
	Map<String, String> saveRefund(String payType, String drugList,String undDrugList, String refundDrugList, String refundUnDrugList,String patientNo, Double maney, Double price,String invoiceNo, String newInvoiceNo);
	/**
	 * @Description 根据发票号退费保存
	 * @author  marongbin
	 * @createDate： 2016年12月15日 下午7:47:30 
	 * @modifier 
	 * @modifyDate：
	 * @param payType 支付类型
	 * @param drugList 药品集合
	 * @param undDrugList 非药品集合
	 * @param refundDrugList 药品退费集合
	 * @param refundUnDrugList 非药品退费集合
	 * @param patientNo 病历号
	 * @param maney 退费金额
	 * @param price 退费价钱
	 * @param invoiceNos 发票号集合
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String, String> saveRefundNow(String payType, String drugList,String undDrugList, String refundDrugList, String refundUnDrugList,String patientNo, Double maney, Double price,String invoiceNos);
	/**
	 * @Description 根据发票号查询退费申请项目
	 * @author  marongbin
	 * @createDate： 2017年3月6日 上午10:36:36 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceNo
	 * @return: List<InpatientCancelitemNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> getCancelItemByInvoiceNo(String invoiceNo);
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
	
}
