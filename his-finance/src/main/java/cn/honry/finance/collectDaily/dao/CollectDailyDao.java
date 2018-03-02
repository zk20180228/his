package cn.honry.finance.collectDaily.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientScDreport;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
import cn.honry.finance.collectDaily.vo.ColDaiVo;

/**   
*  
* @className：CollectDailyDao
* @description：结算员日结Dao
* @author：tcj
* @createDate：2016-04-12  
* @modifyRmk：  
* @version 1.0
 */
public interface CollectDailyDao extends EntityDao<ColDaiVo>{
	/**
	 * @Description:查询当前结算员日结结算表中最大的结算日期
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientScDreport> queryCollectMaxTime() throws Exception;
	/**
	 * @Description:查询结算时间内住院药品住院非药品的明细（条件结算人、开始时间、结束时间）
	 * @Author：  tcj
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	List<ColDaiVo> querydetalDaily(String startTime,String endTime) throws Exception;
	/**
	 * @Description:查询结算时间内收费员的日结费用信息（条件结算人、开始时间、结束时间）
	 * @Author：  tcj
	 * @param startTime开始时间
	 * @param endTime结束时间
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	ColDaiVo queryTableDaily(String startTime,String endTime) throws Exception;
	/**
	 * @Description:通过userId查询员工对象
	 * @Author：  tcj
	 * @param id用户Id
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	SysEmployee queryEmpByUserId(String id) throws Exception;
	/**
	 * @Description:结算项目明细
	 * @Author：  tcj
	 *  @param startTime开始时间
	 * @param endTime结束时间
	 * @param state 查询项目明细的类别标识1：医疗预收款借方金额2：医疗应收款贷方金额
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientBalanceHeadNow> querymedicdatagridDaily(String state,String startTime,String endTime) throws Exception;
	/**
	 * @Description:结算项目明细（预交金表）
	 * @Author：  tcj
	 * @param startTime开始时间
	 * @param endTime结束时间
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInPrepayNow> queryYjjDatagridDaily(String startTime,
			String endTime) throws Exception;
	/**
	 * 查询当前操作人时间段内的结算记录
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param id 当前登录用户Id
	 * @return
	 * @throws Exception 
	 */
	List<InpatientInPrepayNow> queryInpreList(String startTime, String endTime,
			String id) throws Exception;
	/**
	 *  查询当前操作人在时间段内的结算数据
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param id 员工Id
	 * @return
	 * @throws Exception 
	 */
	List<InpatientBalanceHeadNow> queryinbanheadList(String startTime,
			String endTime, String id) throws Exception;
	/**
	 * 查询员工list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 * @throws Exception 
	**/
	List<SysEmployee> queryEmplistdaily() throws Exception;
	/**
	 * 查询用户list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 * @throws Exception 
	**/
	List<User> queryUselistdaily() throws Exception;
	/**
	 * 查询统计大类list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 * @throws Exception 
	 **/
	List<MinfeeStatCode> queryfreecodedaily() throws Exception;

}
