package cn.honry.finance.outpatientAccount.service;

import java.util.List;

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.service.BaseService;
/**
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午1:59:36
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface OutAccountService extends BaseService<OutpatientOutprepay>{
	
	/***
	 * 根据就诊卡号（idcardNo），获取患者就诊卡信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月29日
	 * @version 1.0
	 * @param 就诊卡号
	 * @since
	 */
	PatientIdcard getForidcardNo(String idcardNo,String menuAlias);
	
	/***
	 * 根据就诊卡编号（uuid）,获取患者账户信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 * @param	栏目别名
	 * @param	就诊卡编号（32位）
	 * @since
	 */
	OutpatientAccount getForidcardid(String menuAlias,String idcardid);
	
	/***
	 * 
	 * @Title: getForblh
	 * @Description:通过病历号，获得患者信息（Patient）
	 * @author  wfj
	 * @date 创建时间：2016年4月7日
	 * @param blhString	病历号
	 * @return  Patient
	 * @version 1.0
	 * @since
	 */
	Patient getForblh(String blhString);

	/***
	 * 
	 * @Title: getForPatientId
	 * @Description:根据患者编号，获得就诊卡信息
	 * @author  wfj
	 * @date 创建时间：2016年4月7日
	 * @param patientid	患者编号
	 * @return  PatientIdcard
	 * @version 1.0
	 * @since
	 */
	PatientIdcard getForPatientId(String patientid);
	
	/***
	 * 
	 * @Title: getAccounForblh
	 * @Description:通过患者病历号查询患者门诊账户信息
	 * @author  wfj
	 * @date 创建时间：2016年4月7日
	 * @param blhString	患者病历号
	 * @return  OutpatientAccount
	 * @version 1.0
	 * @since
	 */
	OutpatientAccount getAccounForblh(String blhString);
	
	/***
	 * 根据患者账户主键id,获取患者账户信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	OutpatientAccount getAccountForID(String arg0);
	
	/***
	 * 根据患者就诊卡号，和患者账户信息，创建患者账户
	 * idcardNo	：就诊卡号（非ID）；
	 * account	：收集的患者账户信息；
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 */
	void addAccount(String idcardNo,OutpatientAccount account,String menuAlias);
	
	/***
	 * 根据患者就诊卡号，和预存金信息，添加预存金记录
	 * idcardNo	：就诊卡号（非ID）；
	 * outprepay	：收集的预存金信息；
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月30日
	 * @version 1.0
	 * @throws Exception 
	 */ 
	void saveOutprepay(String idcardNo,OutpatientOutprepay outprepay,String menuAlias);
	
	/***
	 * 
	 * @Title: updateAccount 
	 * @Description:更新患者账户状态（账户停用，重启）
	 * @author  wfj
	 * @date 创建时间：2016年4月7日
	 * @param account	患者账户状态
	 * @param idcardNo	就诊卡号
	 * @param blhString	病历号
	 * @param idcardQuery 当账户是重启时，判断账户的读取方式
	 *  1 就诊卡号读取   0 患者病历号读取
	 * @param menuAlias
	 * @return void
	 * @version 1.0
	 * @since
	 */
	void updateAccount(OutpatientAccount account,String idcardNo,String blhString,String idcardQuery,String menuAlias);

	/**
	 * 结清账户余额
	 * idcardNo	：就诊卡号
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	void settleAccount(String idcardNo,String menuAlias);
	
	/***
	 * 退预交金操作，
	 * accountID  患者账户id
	 * outAccountID  预交金id
	 * resType  退款方式：（1 部分退款，0 全额退款）
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	void returnOutprepay(String accountID,String outAccountID,String resType);
	
	/****
	 * 修改密码操作
	 * idcardNo	就诊卡号
	 * oldpwd	原密码
	 * nowpwd	新密码
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	Boolean uppwdAccount(String idcardNo,String oldpwd,String nowpwd,String menuAlias);
	
	
	/***
	 * 补打操作
	 * outprepayID	： 预交金ID
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	void makeOutprepay(String outprepayID);
	
	/***
	 * 根据患者账户主键id  查询患者预存金记录
	 * idcardID ：患者账户主键id
	 * ishistory ：是否历史信息 1是，0否
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	List<OutpatientOutprepay> queryPrestore(String accountID,String ishistory,String menuAlias,String page,String rows);
	
	/***
	 * 根据患者账户主键id,查询患者账户操作明细
	 * accountID ：患者账户主键id
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	List<OutpatientAccountrecord> queryDatailed(String accountID,String menuAlias,String page,String rows);
	
	
	/***
	 * 患者账户操作明细，总条数
	 * accountID	：患者账户主键id
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月5日
	 * @version 1.0
	 * @param
	 * @since
	 */
	int getTotal(String accountID,String menuAlias);
	
	/***
	 * 患者预交金，总条数
	 * accountID	：患者账户主键id
	 * ishistory	：是否历史预交金
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月5日
	 * @version 1.0
	 * @param
	 * @since
	 */
	int getTotal(String accountID,String ishistory,String menuAlias);
	
	/**
	 * 根据就诊卡号，查询患者账户状态
	 * @Title: findAccounState 
	 * @author  WFJ
	 * @createDate 2016年5月9日
	 * @param idcardNo
	 *            就诊卡号
	 * @return int  账户的状态
	 * @version 1.0
	 */
	int findAccounState(String idcardNo);
	
	/***
	 * 根据就诊卡编号，修改账户单日消费限额
	 * @Title: updayLimit 
	 * @author  WFJ
	 * @createDate ：2016年5月11日
	 * @param idcardNo
	 * @return void
	 * @version 1.0
	 */
	void updayLimit(String idcardNo,Double accountDaylimit);
	
	
}
