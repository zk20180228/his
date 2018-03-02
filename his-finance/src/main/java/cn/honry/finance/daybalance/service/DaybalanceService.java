package cn.honry.finance.daybalance.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.service.BaseService;
import cn.honry.finance.daybalance.vo.PayTypeVo;


public interface DaybalanceService extends BaseService<RegisterDaybalance>{

	/**  
	 *  
	 * @Description：  获得某一时间段的日结详细信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterBalancedetail> getBalance(RegisterDaybalance daybalance,String registrarId);

	/**  
	 *  
	 * @Description：  获得某一时间段的日结信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterDaybalance getDaybalance(RegisterDaybalance daybalance,String registrarId);
	/**
	 * @Description 获得某一时间段的日结信息
	 * @author  marongbin
	 * @createDate： 2017年1月3日 上午9:48:59 
	 * @modifier 
	 * @modifyDate：
	 * @param balancedetailList
	 * @param daybalance
	 * @param registrarId
	 * @return: RegisterDaybalance
	 * @modifyRmk：  
	 * @version 1.0
	 */
	RegisterDaybalance getDaybalanceNow(List<RegisterBalancedetail> balancedetailList);
	

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
	 * @Description：  保存挂号日结档信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String,Object> saveDaybalance(RegisterDaybalance daybalance);
	
	/**  
	 *  
	 * @Description：  获得支付方式
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：zhuxiaolu
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<PayTypeVo> getPayType();

}
