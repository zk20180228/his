package cn.honry.inpatient.outBalance.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessEcoformula;
import cn.honry.base.bean.model.BusinessEcoicdfee;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientChangeprepay;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.outBalance.vo.InvoicePrintVo;

public interface OutBalanceDAO extends EntityDao<InpatientInfo>{
	/**  
	 * @Description： 根据病历号查询住院信息
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
	 * @Description： 根据住院流水号 查询住院信息
	 * @Author：dh
	 * @CreateDate：2015-12-2 上午10:10:12  
	 * @version 1.0
	 * @throws Exception 
	 */
	public InpatientInfoNow queryInfoByinpatientNo(String inpatientNo) throws Exception;
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
	 * @Description： 领取发票（工作方法）
	 * @author  dh
	 * @createDate： 2016年4月8日 上午10:28:53 
	 * @modifier dh
	 * @modifyDate：2016年4月8日 上午10:28:53
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String, String> queryFinanceInvoiceNo(String id, String invoiceType) throws Exception;
	/**
	 * @Description： 修改发票
	 * @author  dh
	 * @createDate： 2016年4月8日 上午10:28:53 
	 * @modifier dh
	 * @modifyDate：2016年4月8日 上午10:28:53
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveInvoiceFinance(String id, String invoiceNo, String invoiceType) throws Exception;
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
	 * @Description:根据住院流水号获取最大的结算序号
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
	 * @Description:根据住院流水号和时间查询预交金
	 * @Author： dh
	 * @CreateDate：2016/4/9
	 * @param @param InpatientInPrepay
	 * @param @return   
	 * @return List<InpatientInPrepay>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepayNow> queryInpatientInPrepay(String inpatientNo, String inDate, String outDate) throws Exception;
	/**
	 * @Description:根据ids查询预交金
	 * @Author： dh
	 * @CreateDate：2016/4/9
	 * @param @param InpatientInPrepayNow
	 * @param @return   
	 * @return List<InpatientInPrepayNow>  
	 * @version 1.0
	 * @throws Exception 
	 **/
	InpatientInPrepayNow queryInpatientInPrepayById(String ids) throws Exception;
	/**
	 * @Description: 根据住院流水号查询费用汇总列表
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientFeeInfo>  
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
	 * @Description:根据住院流水号，查询减免记录
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientDerate>  
	 * @version 1.0
	 * @throws Exception 
	 **/
	List<InpatientDerate> getDerate(String inpatientNo) throws Exception;
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
	 * 根据住院流水号，查询转入预交金信息
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientInfo>
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientChangeprepay> queryInpatientChangeprepay(String inpatientNo) throws Exception;
	/**
	 * 根据住院流水号，查询费用汇总信息
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientInfo>
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientFeeInfoNow> inpatientFeeInfoFee(String inpatientNo) throws Exception;
	/**
	 * 查询减免表最大的发生序号
	 * @throws Exception 
	 */
	InpatientDerate maxHappon(String inpatientNo) throws Exception;
	/**
	 * 查询资料变更表最大的发生序号
	 * @throws Exception 
	 */
	InpatientShiftData queryMaxHappon(String inpatientNo) throws Exception;
	/**
	 * 根据最小费用查询统计大类对照表
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<MinfeeStatCode>
	 * @version 1.0
	 * @throws Exception 
	 */
	List<MinfeeStatCode> queryMinfeeStatCode(String minfeeCode) throws Exception;
	/**
	 * 根据住院流水号 查询住院药品明细
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientMedicineList>
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientMedicineListNow> queryInpatientMedicineList(String inpatientNo) throws Exception;
	/**
	 * 根据住院流水号 查询住院非药品明细
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientItemList>
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientItemListNow> queryInpatientItemList(String inpatientNo) throws Exception;
	/**
	 * @Description:查询患者费用总信息
	 * @Author： dh
	 * @CreateDate： 2015-1-4
	 * @return List<InpatientFeeInfo>  
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientFeeInfoNow> inpatientNo(String inpatientNo,String inDate1,String outDate1) throws Exception;
	/**
	 * QBC查询
	 * @param minfeeCode
	 * @return
	 * @throws Exception 
	 */
	List<MinfeeStatCode> getList(String minfeeCode) throws Exception;
	/**
	 * @Description： 修改预交金信息
	 * @author  dh
	 * @createDate： 2016年4月8日 上午10:28:53 
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void updateInpatientInPrepayNow(InpatientInPrepayNow inPrepayNow,Date balanceDate) throws Exception;
	/**
	 * @Description： 修改费用减免信息
	 * @author  wsj
	 * @createDate： 2016年4月8日 上午15:28:53 
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
    void updateInpatientDerate(InpatientDerate inpatientDerate,Date balanceDate ) throws Exception;
    /**
     * @Description： 修改费用汇总信息
     * @author  donghe
     * @createDate： 2016年4月8日 上午15:28:53 
     * @modifyRmk：  
     * @version 1.0
     * @throws Exception 
     */
    void updateInpatientFee(InpatientFeeInfoNow inpatientFeeInfoNow) throws Exception;
    /**
     * @Description:出院信息打印
     * @param inpatientNo
     * @return list
     * @throws Exception 
     */
    List<InvoicePrintVo> printBalance(String inpatientNo) throws Exception;
	
}
