package cn.honry.inpatient.settlementRecall.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.settlementRecall.vo.VSettlementRecall;

public interface SettlementRecallService extends BaseService<VSettlementRecall>{
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
	 * 根据结算序号，住院流水号，查询预交金表，获取预交金信息
	 * @param inpatientNoSearch
	 * @param balanceNoSearch
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInPrepayNow> getPrepayByInpatientNoAndBalanceNo(
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
	 * 根据住院号查询结算投标 获取为作废的发票号  List 结算对象
	 * @param inpatientNo
	 * @return
	 * @throws Exception 
	 */
	List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception;
	/**
	 * 根据病历号查询患者是否存在
	 * @param medicalrecordId
	 * @return
	 * @throws Exception
	 */
	List<InpatientInfoNow> queryInfomedicalrecordId(String medicalrecordId) throws Exception;
	/**
	 * 根据住院号查询住院信息
	 * @param inpatientNo
	 * @return
	 * @throws Exception
	 */
	List<InpatientInfoNow> queryInfo(String inpatientNo) throws Exception;
	/**
	 * 根据科室编号获取科室名称
	 * @param deptCode
	 * @return
	 * @throws Exception
	 */
	String getdeptNameById(String deptCode);
	/**
	 * 根据床位编号获取床位名称
	 * @param bedId
	 * @return
	 * @throws Exception
	 */
	String getBedNameById(String bedId);
	/**
	 * 根据医生编号获取医生姓名
	 * @param houseDocCode
	 * @return
	 * @throws Exception
	 */
	String getEmployeeName(String houseDocCode);
	
	/**
	 * 结算召回
	 * @param invoiceNo 结算对象中的发票号
	 * @param balanceNo 结算对象的结算序号
	 * @param inpatientNo 结算对象中的住院号
	 * @param balanceHeadId  
	 * @param prePayIds
	 * @param payObjId
	 * @param balanceListIds
	 * @param patientInfoId
	 * @return
	 * @throws Exception
	 */
	String balanceRecall(String invoiceNo, String balanceNo, String inpatientNo, String balanceHeadId, String prePayIds, String payObjId, String balanceListIds, String patientInfoId) throws Exception;
	
	
	/**
	 * 根据根据主表的床号查询床号编码表的信息
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
	 * 根据病床信息表的病床编号查询病房信息表的信息
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
	List<User> getUseridEmpName() throws Exception;
}
