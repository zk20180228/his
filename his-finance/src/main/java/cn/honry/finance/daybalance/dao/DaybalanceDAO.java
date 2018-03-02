package cn.honry.finance.daybalance.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.dao.EntityDao;
import cn.honry.finance.daybalance.vo.AllPayTypeVo;

@SuppressWarnings({"all"})
public interface DaybalanceDAO extends EntityDao<RegisterDaybalance>{

	/**  
	 *  
	 * @Description：   获得开始时间 (上一次的结算时间 ,如果没有返回当天的0点)
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Date getStartTime();

	/**  
	 *  
	 * @Description：   获得日结序号
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String getNextBalanceNo();

	String getSettlementByName(String stringzifei);
	/**
	 * @Description 根据时间及支付类型查询挂号信息
	 * @author  marongbin
	 * @createDate： 2016年12月30日 下午5:40:21 
	 * @modifier 
	 * @modifyDate：
	 * @param startTime
	 * @param endTime
	 * @param registrarId
	 * @return: List<Registration>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	AllPayTypeVo getInfoByTimeNow(Date startTime, Date endTime,String registrarId);
	
	/**
	 * @Description  更新挂号主表日结信息
	 * @author  marongbin
	 * @createDate： 2017年1月5日 下午8:45:28 
	 * @modifier 
	 * @modifyDate：
	 * @param acount
	 * @param startTime
	 * @param endTime: void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int updateRegDaybalance(String balanceNo,String acount,Date startTime, Date endTime);
	
}
