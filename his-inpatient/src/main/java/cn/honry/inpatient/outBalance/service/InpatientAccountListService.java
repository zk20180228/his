package cn.honry.inpatient.outBalance.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.service.BaseService;

public interface InpatientAccountListService extends BaseService<InpatientAccount>{
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientAccount>  
	 * @version 1.0
	**/
	List<InpatientAccount> getID(String inpatientNo) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> getmedicalrecordId(String medicalrecordId) throws Exception;
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
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> getAccount(String id) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> QueryPatientAccountrepaydetailID(String id) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> SumAccount(String id) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> QueryPatientAccountrepaydetail(String id) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<PatientAccountrepaydetail>  
	 * @version 1.0
	**/
	List<PatientAccountrepaydetail> Queryzhuanya(String id) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientInfo>  
	 * @version 1.0
	**/
	List<InpatientInfo> getinpatient(String inpatientNo) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientBalanceHead>  
	 * @version 1.0
	**/
	List<InpatientBalanceHead> queryinpatientNo(String inpatientNo) throws Exception;
	/**
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientBalancePay>  
	 * @version 1.0
	**/
	List<InpatientBalancePay> getbalanceOpercode(String balanceOpercode) throws Exception;
	/**
	 * @Description:删除
	 * @Author：  dh
	 * @CreateDate： 2015-12-3 下午02:36:53 
	 * @Modifier：dh
	 * @ModifyDate：2015-12-3 下午02:36:53 
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void UpdateInpatientInPrepay(String inpatientNo,String balanceNo,String invoiceNo);
	/**
	 * @Description:删除
	 * @Author：  dh
	 * @CreateDate： 2015-12-3 下午02:36:53 
	 * @Modifier：dh
	 * @ModifyDate：2015-12-3 下午02:36:53 
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void UpdateInpatientInfo(String inpatientNo);
	/**
	 * @Description:保存住院费用实付表
	 * @Author：  dh
	 * @CreateDate： 2015-1-7
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void saveInpatientBalancePay(InpatientBalancePay inpatientBalancePay);
	/**
	 * @Description:保存费用减免表
	 * @Author：  dh
	 * @CreateDate： 2015-1-7
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void saveInpatientDerate(InpatientDerate inpatientDerate);
	/**
	 * @Description:保存资料变更表
	 * @Author：  dh
	 * @CreateDate： 2015-1-7
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void saveInpatientShiftData(InpatientShiftData inpatientShiftData);
	/**
	 * @Description:根据json串保存
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @param infoJson   
	 * @return void  
	 * @version 1.0
	 * @param detailJson 
	**/
	void saveOrUpdate(String billJson);
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
	 * 结算保存
	 * @throws Exception 
	 */
	String saveBalance(Map<String, String> parameterMap,String zfJson) throws Exception;
	
}
