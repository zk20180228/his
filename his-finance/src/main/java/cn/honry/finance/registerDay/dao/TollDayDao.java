package cn.honry.finance.registerDay.dao;


import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.dao.EntityDao;
import cn.honry.finance.registerDay.vo.DayBalanceVO;
import cn.honry.finance.registerDay.vo.InfoVo;

@SuppressWarnings({"all"})
public interface TollDayDao extends EntityDao<OutpatientDaybalance>{

	/***
	 * 当前登录人的日结开始时间
	 * <li>查询日结信息的结束时间，若不存在则查找最开始收费时间，若还不存在则返回当天零点</li>
	 * @Title: getBeginDate 
	 * @author  WFJ
	 * @createDate ：2016年6月13日
	 * @return Date
	 * @version 1.0
	 */
	Date getBeginDate(String userid);
	
	/***
	 * 在指定时间段内，查询收费信息
	 * @Title: queryInvoiceinfo 
	 * @author  WFJ
	 * @createDate ：2016年6月13日
	 * @param userid : 用户id
	 * @param beginDate ： 统计开始时间
	 * @param endDate ： 统计终止时间
	 * @param state ： 正反交易状态（1正，2反）
	 * @return List<BusinessInvoiceInfo>
	 * @version 1.0
	 */
	List<InfoVo> queryInvoiceinfo(String userid,Date beginDate, Date endDate);
	/**
	 * @Description 更新处方明细表的日结标识
	 * @author  marongbin
	 * @createDate： 2017年1月11日 下午2:22:24 
	 * @modifier 
	 * @modifyDate：
	 * @param userid
	 * @param beginDate
	 * @param endDate: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int updateOutFeedetail(String userid,Date beginDate, Date endDate);
	/**
	 * @Description 更新发票头表日结标识
	 * @author  marongbin
	 * @createDate： 2017年1月11日 下午3:36:38 
	 * @modifier 
	 * @modifyDate：
	 * @param userid
	 * @param beginDate
	 * @param endDate
	 * @return: int
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int updateInvoiceInfo(String userName,String dayBalanceNo,String userid,Date beginDate, Date endDate);
	/**
	 * @Description 更新发票明细日结标识
	 * @author  marongbin
	 * @createDate： 2017年1月11日 下午3:39:01 
	 * @modifier 
	 * @modifyDate：
	 * @param dayBalanceNo
	 * @param userid
	 * @param beginDate
	 * @param endDate
	 * @return: int
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int updateInvoicedetail(String userName,String dayBalanceNo,String userid,Date beginDate, Date endDate);
	/**
	 * @Description 更新T_BUSINESS_PAYMODE_NOW日结信息 
	 * @author  marongbin
	 * @createDate： 2017年1月12日 下午4:45:29 
	 * @modifier 
	 * @modifyDate：
	 * @param userName
	 * @param dayBalanceNo
	 * @param userid
	 * @param beginDate
	 * @param endDate
	 * @return: int
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int updatePaymodeNow(String userName,String dayBalanceNo,String userid,Date beginDate, Date endDate);
	/**
	 * @Description 获取日结信息明细
	 * @author  marongbin
	 * @createDate： 2017年4月10日 下午4:57:10 
	 * @modifier 
	 * @modifyDate：
	 * @param code
	 * @param startDate
	 * @param endDate
	 * @return: List<DayBalanceVO>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DayBalanceVO> getBalance(String code, String startDate,String endDate);
	/**
	 * @Description 获取日结信息，各种费用
	 * @author  marongbin
	 * @createDate： 2017年4月13日 上午8:58:57 
	 * @modifier 
	 * @modifyDate：
	 * @param code
	 * @param startDate
	 * @param endDate
	 * @return: OutpatientDaybalance
	 * @modifyRmk：  
	 * @version 1.0
	 */
	OutpatientDaybalance getOutDayBlance(String code, String startDate,String endDate);
}
