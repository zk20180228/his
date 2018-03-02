package cn.honry.finance.outpatientAccount.dao;

import java.util.List;

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.dao.EntityDao;

/**
 *预交金
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午1:58:31
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@SuppressWarnings({"all"})
public interface OutAccountDAO extends EntityDao<OutpatientOutprepay>{
	
	/***
	 * 根据预交金id,获取预交金记录
	 * @Description:
	 * @author  wfj
	 * @date 创建时间：2016年4月1日
	 * @version 1.0
	 * @param
	 * @since
	 */
	OutpatientOutprepay get(String arg0);
	
	
	
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
	
	
	
	
	
	
	
}
