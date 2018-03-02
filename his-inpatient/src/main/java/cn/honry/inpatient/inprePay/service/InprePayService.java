package cn.honry.inpatient.inprePay.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.inprePay.vo.AcceptingGold;
import cn.honry.inpatient.inprePay.vo.PatientVo;

/**  
 *  
 * @className：InprePayService 
 * @Description：  住院预交金Service
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface InprePayService  extends BaseService<InpatientInPrepayNow>{

	/**
	 *
	 * @Description：通过就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param idcardNo 就诊卡号
	 * @throws Exception 
	 * @return：患者对象（PatientVo）
	 *
	 */
	PatientVo findPatientByIdcardNo(String idcardNo,String inState) throws Exception;

	/**
	 *
	 * @Description：通过就诊卡号查询就诊卡是否存在
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param idcardNo 就诊卡号
	 * @throws Exception 
	 * @return：true存在；false不存在
	 *
	 */
	boolean checkIdcardNo(String idcardNo) throws Exception;

	/**
	 *
	 * @Description：查询预交金信息 - 分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param：inpatientNo住院号
	 * @return 总条数
	 * @throws Exception 
	 */
	int getInprePayTotal(String inpatientNo) throws Exception;

	/**
	 *
	 * @throws Exception 
	 * @Description：查询预交金信息 - 分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @param2：page当前页数
	 * @param3：rows分页数量
	 * @return：分页信息
	 * 
	 */
	List<InpatientInPrepayNow> getInprePayPage(String inpatientNo, String page,String rows) throws Exception;

	/**
	 *
	 * @Description：获得银行Map
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 * @return:银行Map
	 *
	 */
	Map<String, String> getCodeBankMap();

	/**
	 *
	 * @Description：通过住院号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<PatientVo> findPatientByInpatientNo(String inpatientNo) throws Exception;
	/**
	 *
	 * @Description：通过住院号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	void removeInprePayByids(String[] ids) throws Exception;
	
	/**
	 *
	 * @Description：通过住院号获得患者信息
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月27日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	InpatientInfoNow findPatientByInpNo(String inpatientNo) throws Exception;
	
	/**
	 *
	 * @Description：修改住院主表中的预交金余额
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月27日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	void updatePatientByInpNo(String inpatientNo,Double prepayCost) throws Exception;
	
	/**
	 *
	 * @Description：保存预存金
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param inPrepay 预存金对象
	 * @throws Exception 
	 *
	 */
	String save(InpatientInPrepayNow inPrepay) throws Exception;
	/**
	 * 获取关账患者信息
	 * @param iinpatientNonp 住院流水号
	 * @author tuchuanjiang
	 * @return
	 * @throws Exception 
	 */
	InpatientInfoNow isStopAcountNow(String inpatientNo) throws Exception;

	/**
	 *
	 * @Description：获取发票
	 * @Author：zhuxiaolu
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @version： 1.0
	 * @param account  当前登录对象   invoiceType发票类型
	 *
	 */
	Map<String, String> queryFinanceInvoiceNo(String account, String invoiceType);
	/**
	 * @see 住院预交金报表数据
	 * 
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public List<AcceptingGold> iReportzyyjj(String id) throws Exception;
	
	/**@see 根据科室code获得所在院区
	 * @author conglin
	 * @param deptCode 科室code
	 * @return String 院区Code
	 * @throws Exception 
	 */
	String getDeptArea(String deptCode) throws Exception;
	
}
