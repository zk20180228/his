package cn.honry.inpatient.settlementRecall.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.settlementRecall.vo.VSettlementRecall;

public interface SettlementRecallDAO extends EntityDao<VSettlementRecall>{
	/**
	 * 根据患者住院流水号获取新的结算序号
	 * @param medicalrecordId
	 * @return
	 * @throws Exception
	 */
	List<InpatientInfoNow> querybalanceNo(String inpatientNo) throws Exception;
	/**
	 * 根据病历号查询患者是否存在
	 * @param medicalrecordId
	 * @return
	 * @throws Exception
	 */
	List<InpatientInfoNow> queryInfomedicalrecordId(String medicalrecordId) throws Exception;
	List<InpatientInfoNow> queryInfo(String inpatientNo) throws Exception;
	/**
	 * 根据发票号查询获取第一条头表信息作为结算对象
	 * @param invoiceNoSearch
	 * @return
	 * @throws Exception 
	 */
	InpatientBalanceHeadNow queryHeadByInvoiceNo(String invoiceNoSearch) throws Exception;
	/**
	 * 通过住院号和结算序号 查询住院结算头表 取得结算头表发票组结算信息
	 * @param inpatientNoSearch
	 * @param balanceNoSearch
	 * @return
	 * @throws Exception 
	 */
	List<InpatientBalanceHeadNow> queryHeadByInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception;
	/**
	 * 根据住院流水号及  结算序号，查询住院结算明细表，获取结算明细
	 * @param inpatientNoSearch
	 * @param balanceNoSearch
	 * @return
	 * @throws Exception 
	 */
	List<InpatientBalanceListNow> getBalanceListInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception;
	/**
	 * 根据结算序号，住院流水号，查询账户明细表，获取预交金信息
	 * @param inpatientNoSearch
	 * @param balanceNoSearch
	 * @return
	 */
	List<PatientAccountrepaydetail>  getPatientAccountrepaydetail(
			String id, String balanceNoSearch);
	/**
	 * 根据结算序号，住院流水号，查询预交金表，获取预交金信息
	 * @param inpatientNoSearch
	 * @param balanceNoSearch
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInPrepayNow>  getPrepayByInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception;
	/**
	 * 按住院号线查询  根据住院号查询结算投标 获取为作废的发票号  List 结算对象
	 * @param inpatientNo
	 * @return
	 * @throws Exception
	 */
	List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception;
	/**
	 * 根据发票号 结算序号 查询住院收费结算实付表
	 * @param invoiceNo
	 * @param balanceNo
	 * @return
	 * @throws Exception 
	 */
	List<InpatientBalancePayNow> getBlancePayList(String invoiceNo,
			String balanceNo) throws Exception;
	/**
	 * 根据发票号及结算序号获取费用汇总表信息
	 * @param invoiceNo
	 * @param inpatientNo
	 * @return
	 * @throws Exception 
	 */
	List<InpatientFeeInfoNow> getFeeInfo(String invoiceNo, String inpatientNo) throws Exception;
	/**
	 * 根据住院号从住院主表中  获取新的结算序号 
	 * @param inpatientNo
	 * @return
	 * @throws Exception 
	 */
	int getNewBalanceNo(String inpatientNo) throws Exception;
	/**
	 * 根据结算序号住院号更新费用明细
	 * @param balanceNo
	 * @param inpatientNo
	 * @param newBalanceNo 
	 * @throws Exception 
	 */
	void updateMedicineList(String balanceNo, String inpatientNo, int newBalanceNo) throws Exception;
	/**
	 * 更新非药品明细
	 * @param balanceNo
	 * @param inpatientNo
	 * @param newBalanceNo
	 * @throws Exception 
	 */
	void updateItemList(String balanceNo, String inpatientNo, int newBalanceNo) throws Exception;
	/**
	 * 更新住院主表信息
	 * @param inpatientNo
	 * @param balanceHeadId
	 * @param newBalanceNo 
	 * @param patientInfoId 
	 * @throws Exception 
	 */
	void updateInpatientInfo(String inpatientNo, String balanceHeadId, int newBalanceNo, String patientInfoId) throws Exception;
	/**
	 * 处理预交金
	 * @param balanceHeadId
	 * @param curDate 
	 * @param newBalanceNo 
	 * @param prePayIds 
	 * @param financeGroupId 
	 * @param maxInvoiceNo 
	 * @throws Exception 
	 */
	void dealPrepay(String inpatientNo,String balanceHeadId, Date curDate, int newBalanceNo, String prePayIds, String financeGroupId, String maxInvoiceNo) throws Exception;
	/**
	 * 处理结算明细
	 * @param balanceListIds
	 * @param newBalanceNo 
	 * @param curDate 
	 * @throws Exception 
	 */
	void dealBalanceList(String balanceListIds, int newBalanceNo, Date curDate) throws Exception;
	/**
	 * 处理结算头表
	 * @param newBalanceNo 
	 * @param curDate 
	 * @param financeGroupId 
	 * @param balanceListIds
	 * @throws Exception 
	 */
	void dealBalanceHead(String balanceHeadIds, int newBalanceNo, Date curDate, String financeGroupId) throws Exception;
	/**
	 * 处理结算实付表
	 * @param payModeList
	 * @param newBalanceNo
	 * @param curDate 
	 * @throws Exception 
	 */
	void dealBalancePay(List<InpatientBalancePayNow> payModeList, int newBalanceNo, Date curDate) throws Exception;
	/**
	 * 处理减免表
	 * @param inpatientNo
	 * @param balanceNo
	 * @param curDate 
	 * @param newBalanceNo 
	 * @throws Exception 
	 */
	void dealDerate(String inpatientNo, String balanceNo, Date curDate, int newBalanceNo) throws Exception;
	/**
	 * 保存转科信息
	 * @param inpatientNo
	 * @param balanceNo balanceNo newBalanceNo
	 * @param curDate 
	 * @param newBalanceNo 
	 * @throws Exception 
	 */
	void insertShiftData(String inpatientNo, String balanceNo, int newBalanceNo) throws Exception;
	/**
	 * 根据科室code获取科室名称
	 * @param deptCode
	 * @throws Exception 
	 */
	String getdeptNameById(String deptCode);
	/**
	 * 根据病床编号获取床位名称
	 * @param bedId
	 * @param bedId
	 * @param curDate 
	 * @param newBalanceNo 
	 * @throws Exception 
	 */
	String getBedNameById(String bedId);
	/**
	 * 根据医生编号获取医生姓名
	 * @param houseDocCode
	 * @throws Exception 
	 */
	String getEmployeeName(String houseDocCode);
	/**
	 * 根据用户id获取所在财务组id
	 * @param userId
	 * @throws Exception 
	 */
	public String getFinanceGroupIdByUserId(String userId);
	/**
	 * 根据用户id获取其员工id
	 * @param userId
	 * @return
	 */
	public String getEmployeeId(String userId);
	/**
	 * 根据当前用户的员工id，发票类型id 获得最大的未使用到的发票号  其获取过程模拟来自RegisterDAOImpl saveOrUpdate 
	 */
	public String getMaxInvoiceNo(String employeeId,String invoiceTypeId);
	/**
	 * 根据当前用户的员工id，发票类型id 已使用发票号 更新发票状态及已使用发票号
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	public void updateFinanceInvoiceState(String employeeId,String invoiceTypeId,String invoiceUseNO);
	/**
	 * 根据主表的床号查询床号编码表的信息
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	List<InpatientBedinfoNow> getInpatientBedinfo(String benId) throws Exception;
	/**
	 * 根据床号编码表的床号查询病床信息表的信息
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	List<BusinessHospitalbed> getBusinessHospitalbed(String bedId) throws Exception;
	/**
	 * 根据病床信息表的病床编号查询病房信息表的信息
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	List<BusinessBedward> getBusinessBedward(String businessBedward) throws Exception;
	/**
	 * 根据护士站编号查询病区名字
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	List<SysDepartment> getSysDepartment(String nursestation) throws Exception;
	/**
	 * 合同单位
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	List<BusinessContractunit> getBusinessContractunit(String encode) throws Exception;
	/**
	 * 员工信息
	 * @param employeeId
	 * @param invoiceTypeId
	 * @param invoiceUseNO
	 */
	List<SysEmployee> getSysEmployee(String houseDocCode) throws Exception;
	/**  
	 * @Description： 根据用户ID查询员工
	 * @Author：ldl
	 * @CreateDate：2016-01-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	SysEmployee queryEmployee(String userId);
	/**  
	 * @Description： 领取发票工作方法
	 * @Author：ldl
	 * @CreateDate：2016-01-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String, Object> queryFinanceInvoiceNo(String id, String invoiceType) throws Exception;
	/**  
	 * @Description：修改发票
	 * @param:
	 * @Author：ldl
	 * @ModifyDate：2016-02-03
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveInvoiceFinance(String id, String maxInvoiceNo, String invoiceType1) throws Exception;
	/**  
	 * @Description：根据员工id查询员工名称
	 * @param:
	 * @Author：dh
	 * @ModifyDate：2016-04-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> getSysEmpName();
	/**  
	 * @Description：根据userid查询员工名称
	 * @param:
	 * @Author：dh
	 * @ModifyDate：2016-04-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<User> getUserIdEmpName() throws Exception;
	/**  
	 * @Description：查询最大的发生序号
	 * @param:
	 * @Author：dh
	 * @ModifyDate：2016-04-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientInPrepayNow queryHappenNo(String inpatientNo) throws Exception;
}
