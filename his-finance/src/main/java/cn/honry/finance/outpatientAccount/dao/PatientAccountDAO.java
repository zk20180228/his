package cn.honry.finance.outpatientAccount.dao;

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.dao.EntityDao;

/**
 * 患者账户表
 * @author  wangfujun
 * @date 创建时间：2016年3月30日 下午4:59:41
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface PatientAccountDAO extends EntityDao<OutpatientAccount>{
	
	/***
	 * 跟新患者账户状态（账户停用，重启，注销）
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年3月31日
	 * @version 1.0
	 * @param
	 * @since
	 */
	void updateAccount(OutpatientAccount account,String menuAlias);
	
	/***
	 * 根据患者账户主键id,获取患者账户信息
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	OutpatientAccount get(String arg0);
	
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
	 * @param patientid
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
	
}
