package cn.honry.inpatient.outBalance.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessEcoformula;
import cn.honry.base.bean.model.BusinessEcoicdfee;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.outBalance.vo.InvoicePrintVo;

public interface OutBalanceService extends BaseService<InpatientInfo> {
	/**  
	 * @Description： 根据住院号查询住院信息
	 * @Author：hedong
	 * @CreateDate：2015-12-2 上午10:10:12  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<InpatientInfoNow> queryInfoByPatientNo(String medicalrecordId) throws Exception;
	/**  
	 * @Description： 根据部门id获得科室名称
	 * @Author：hedong
	 * @CreateDate：2015-12-3 下午19:50:12  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	public String getDeptName(String deptCode) throws Exception;
	/**  
	 * @Description：修改发票号
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2016-01-12
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param invoiceType 
	 * @param string 
	 * @return 
	 */
	void saveInvoiceFinance(String id, String invoiceNo, String invoiceType);
	/**  
	 * @Description：根据员工Id 查询发票
	 * @param:
	 * @Author：dh
	 * @ModifyDate：2015-12-25
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param invoiceType 
	 */
	Map<String, Object> findFinanceInvoice(String id, String invoiceType);
	/**  
	 * @Description：根据当前登陆用户 查询登陆员工
	 * @param:
	 * @Author：dh
	 * @ModifyDate：2015-12-25
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	SysEmployee findEmployee(String userId);
	/**  
	 * @Description：  领取发票（工作方法）
	 * @Author：dh
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：
	 * @param：id 员工ID
	 * @param:invoiceType 发票类型
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String, String> queryFinanceInvoiceNo(String id, String invoiceType) throws Exception;
	/**  
	 * @Description：  根据当前登陆用户ID查询员工ID
	 * @Author：dh
	 * @CreateDate：2016-03-30
	 * @ModifyRmk：  
	 * @param：id 用户ID
	 * @version 1.0
	 * @throws Exception 
	 */
	SysEmployee queryEmployee(String id) throws Exception;
	/**
	 * @Description:获取最大的结算序号
	 * @Author： dh
	 * @CreateDate： 2015-1-15
	 * @return List<InpatientInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> getbalanceNo(String inpatientNo) throws Exception;
	/**
	 * @Description:根据住院流水号和时间查询担保金
	 * @Author： dh
	 * @CreateDate： 2015-1-15
	 * @param inpatientNo
	 * @param outDate
	 * @param inDate
	 * @return List<InpatientSurety>
	 * @throws Exception 
	 */
	List<InpatientSurety> queryInpatientSurety(String inpatientNo,String outDate,String inDate) throws Exception;
	/**
	 * @Description:根据住院流水号查询   患者是否存在未确认的退费申请或退药申请
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientCancelitem>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientCancelitemNow> getInpatientCancelitem(String inpatientNo) throws Exception;
	/**
	 * @Description:根据住院流水号查询   查询是否存在有效的退药台
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientCancelitem>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<OutpatientDrugcontrol> queryDrugcontrol(String inpatientNo) throws Exception;
	/**
	 * @Description:根据住院流水号查询   查询住院预交金表的转押金是否打印
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<inpatientInPrepay>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepayNow> queryInPrepay(String inpatientNo) throws Exception;
	/**
	 * @Description:根据住院流水号查询   对患者进行封账
	 * @Author：  dh
	 * @CreateDate： 2015-12-3 下午02:36:53 
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	**/
	String UpdateInpatientInfoList(String inpatientNo) throws Exception;
	/**
	 * @Description:根据住院流水号和时间查询预交金
	 * @Author： dh
	 * @CreateDate：2016/4/9
	 * @param @param InpatientInPrepay
	 * @param @return   
	 * @return List<InpatientInPrepayNow>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepayNow> queryInpatientInPrepay(String inpatientNo, String inDate, String outDate) throws Exception;
	/**
	 * @Description: 根据住院流水号查询费用汇总列表
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientFeeInfoNow>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientFeeInfoNow> InpatientFeeList(String inpatientNo,String inDate1,String outDate1) throws Exception;
	/**
	 * @Description:查询优惠套餐表，是否有单病种优惠
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<BusinessEcoformula>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<BusinessEcoformula> getclinicCode(String clinicCode) throws Exception;
	/**
	 * @Description:根据住院号，单病种编码和当前时间获取单病种优惠信息
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<BusinessEcoicdfee>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<BusinessEcoicdfee> getcost(String sysdate,String inpatientNo) throws Exception;
	/**
	 * @Description:根据住院流水号，查询减免金额
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientDerate>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientDerate> getclinicNo(String inpatientNo,String inDate1,String outDate1) throws Exception;
	/**
	 * 根据住院流水号，查询住院主表的费用信息
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientInfo>
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientInfoNow> InpatientInfoqueryFee(String inpatientNo) throws Exception;
	/**
	 * 出院结算保存
	 * @throws Exception 
	 */
	String saveBalance(Map<String, String> parameterMap,String zfJson,String costJson) throws Exception;
	/**
	 * 中途和欠费结算保存
	 * @throws Exception 
	 */
	String saveBalanceZhongtu(Map<String, String> parameterMap,String zfJson,String costJson) throws Exception;
	/**
	 * 出院结算报表打印
	 * @param inpatientNo
	 * @return
	 * @throws Exception 
	 */
	public List<InvoicePrintVo> printBalance(String inpatientNo) throws Exception;
}
