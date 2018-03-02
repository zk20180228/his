package cn.honry.inpatient.outBalance.dao;


import java.util.List;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;


public interface InpatientAccountListDAO extends EntityDao<InpatientAccount>{
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientAccount>  
	 * @version 1.0
	**/
	List<InpatientAccount> getID(String id);
	/**
	 * @Description：根据病历号查询担保金
	 * @author  dh
	 * @createDate： 2016年4月5日 下午7:53:57 
	 * @modifier dh
	 * @modifyDate：2016年4月5日 下午7:53:57
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<PatientAccountrepaydetail> getmedicalrecordId(String id);
	/**
	 * @Description：根据病历号查询转押金
	 * @author  dh
	 * @createDate： 2016年4月5日 下午7:53:57 
	 * @modifier dh
	 * @modifyDate：2016年4月5日 下午7:53:57
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<PatientAccountrepaydetail> getmedicalrecordIdZhuanya(String medicalrecordId);
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> QueryPatientAccountrepaydetail(String id);
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> getAccount(String id);
	/**
	 * @Description:根据患者账户表id查询预交金
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param PatientAccountrepaydetail
	 * @param @return   
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> QueryPatientAccountrepaydetailID(String id);
	/**
	 * @Description:根据患者账户表id查询转押金
	 * @Author： dh
	 * @CreateDate： 2015-1-8
	 * @param @param PatientAccountrepaydetail
	 * @param @return   
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> Queryzhuanya(String id);
	/**
	 * @Description:查询预交金总额
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> SumAccount(String id);
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientInfo>  
	 * @version 1.0
	**/
	List<InpatientInfo> getinpatient(String inpatientNo);
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientBalanceHead>  
	 * @version 1.0
	**/
	List<InpatientBalanceHead> queryinpatientNo(String inpatientNo);
	/**
	 * @Description:查询
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientBalancePay>  
	 * @version 1.0
	**/
	List<InpatientBalancePay> getbalanceOpercode(String balanceOpercode);
	/**
	 * @Description：更新预交金记录为结算状态
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */
	void UpdateInpatientInPrepay(String inpatientNo,String balanceNo,String invoiceNo);
	/**
	 * @Description：将患者未结算的转入预交金设置为结算状态，记录操作人，操作时间和结算序号。
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */
	void UpdateInpatientChangeprepay(String inpatientNo,String balanceNo);
	/**
	 * @Description：更新减免信息为结算状态
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */
	void UpdateInpatientDerate(String inpatientNo,String balanceNo,String invoiceNo);
	/**
	 * @Description：更新结算费用汇总信息为结算状态
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */
	void UpdateInpatientFeeInfo(String inpatientNo,String balanceNo,String invoiceNo);
	/**
	 * @Description：更新住院药品未结算状态
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */
	void UpdateInpatientMedicineList(String inpatientNo,String balanceNo,String invoiceNo);
	/**
	 * @Description：更新非药品明细未结算状态
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */
	void UpdateInpatientItemList(String inpatientNo,String balanceNo,String invoiceNo);
	/**
	 * @Description：更新患者住院状态
	 * @Author：dh
	 * @CreateDate： 2015-1-4
	 * @param inpatientNo 住院流水号
	 */ 
	void UpdateInpatientInfo(String inpatientNo);
	/**
	 * @Description：保存住院结算实付表
	 * @Author：dh
	 * @CreateDate： 2015-1-7
	 * @param 
	 */
	void saveInpatientBalancePay(InpatientBalancePay inpatientBalancePay);
	/**
	 * @Description：保存费用减免表
	 * @Author：dh
	 * @CreateDate： 2015-1-7
	 * @param 
	 */
	void saveInpatientDerate(InpatientDerate inpatientDerate);
	/**
	 * @Description：保存资料变更表
	 * @Author：dh
	 * @CreateDate： 2015-1-7
	 * @param 
	 */
	void saveInpatientShiftData(InpatientShiftData inpatientShiftData);
	/**
	 * 保存结算头表
	 */
	void saveInpatientBalanceHead(InpatientBalanceHead inpatientBalanceHead);
	/**
	 * 根据病历号查询预交金列表
	 */
	List<PatientAccountrepaydetail> getpayment(String medicalrecordId);
	/**
	 * 根据病历号查询预交金总额
	 */
	List<PatientAccountrepaydetail> getpaymentzonge(String medicalrecordId);
	/**
	 * 根据userId查询员工id
	 */
	SysEmployee queryEmployee(String userId);
}
