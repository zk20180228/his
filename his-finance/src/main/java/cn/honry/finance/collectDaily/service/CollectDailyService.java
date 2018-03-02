package cn.honry.finance.collectDaily.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientScDreport;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;
import cn.honry.finance.collectDaily.vo.ColDaiVo;

/**   
*  
* @className：CollectDailyService
* @description：结算员日结service
* @author：tcj
* @createDate：2016-04-12  
* @modifyRmk：  
* @version 1.0
 */
public interface CollectDailyService extends BaseService<ColDaiVo>{
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
	 * @param startTime开始时间
	 * @param endTime结束时间
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	List<ColDaiVo> querydetalDaily(String startTime,
			String endTime) throws Exception;
	/**
	 * @Description:查询结算时间内收费员的日结费用信息（条件结算人、开始时间、结束时间）
	 * @Author：  tcj
	 * @param startTime开始时间
	 * @param endTime结束时间
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws Exception 
	**/
	ColDaiVo queryTableDaily(String startTime, String endTime) throws Exception;
	/**
	 * @Description:保存日结明细
	 * @Author：  tcj
	 * @param date 结算明细json字符串
	 * @param startTime开始时间
	 * @param endTime结束时间
	 * @param sqc 统计序号
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	**/
	void saveDatagridDaily (String date,String startTime,String endTime,Integer sqc) throws Exception;
	/**
	 * @Description:保存日结信息
	 * @Author：  tcj
	 * @param cdv 日结Vo
	 * @param sqcnum 统计序号
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 * @param endTime 
	 * @param startTime 
	 * @param endTime2 
	**/
	Integer saveFromDaily(ColDaiVo cdv,String date,String startTime, String endTime)throws Exception;
	/**
	 * @Description:结算项目明细
	 * @Author：  tcj
	 * @param state 查询项目明细的类别标识1：医疗预收款借方金额2：医疗应收款贷方金额
	 * @param startTime开始时间
	 * @param endTime结束时间
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
	List<InpatientInPrepayNow> queryYjjDatagridDaily(String startTime,String endTime) throws Exception;
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
