package cn.honry.finance.registerDay.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.service.BaseService;
import cn.honry.finance.registerDay.vo.DayBalanceVO;
import cn.honry.finance.registerDay.vo.ViewVo;

public interface TollDayService extends BaseService<OutpatientDaybalance>{
	
	/***
	 * 当前登录用户的日结开始时间
	 * <li>查询日结信息的结束时间，若不存在则查找最开始收费时间，若还不存在则返回当天零点</li>
	 * @Title: getBeginDate 
	 * @author  WFJ
	 * @createDate ：2016年6月13日
	 * @return Date
	 * @version 1.0
	 */
	Date getBeginDate();
	
	/***
	 * 根据时间区间查询日结信息
	 * @Title: queryInvoiceinfo 
	 * @author  WFJ
	 * @createDate ：2016年6月13日
	 * @param dayBalance
	 * @return Map<String,Object>
	 * @version 1.0
	 */
	List<ViewVo> queryInvoiceinfo(OutpatientDaybalance dayBalance);

	/***
	 * 保存日结信息
	 * @author aizhonghua
	 * @createDate ：2016年6月15日
	 * @return Map<String,Object>
	 * @version 1.0
	 */
	Map<String, Object> saveDaybalance(OutpatientDaybalance dayBalance);
	/**
	 * @Description 收费员日结保存方法
	 * @author  marongbin
	 * @createDate： 2017年4月12日 下午2:05:05 
	 * @modifier 
	 * @modifyDate：
	 * @param dayBalance
	 * @return: Map<String,Object>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String, Object> saveDaybalanceNew(String beginDate,String endDate);
	/**
	 * @Description 获取日结信息
	 * @author  marongbin
	 * @createDate： 2017年4月10日 下午4:11:16 
	 * @modifier 
	 * @modifyDate：
	 * @param code 员工号
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return: List<DayBalanceVO> 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<DayBalanceVO> getBalance(String code,String startDate,String endDate); 
}
