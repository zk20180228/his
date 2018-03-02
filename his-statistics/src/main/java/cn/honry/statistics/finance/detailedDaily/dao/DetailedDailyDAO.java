package cn.honry.statistics.finance.detailedDaily.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.detailedDaily.vo.VdetailedDaily;

@SuppressWarnings({"all"})
public interface DetailedDailyDAO extends EntityDao<VdetailedDaily>{
	/***
	 * 
	 * @Description:查询结账明细日报  当天的
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	List<VdetailedDaily> queryVdetailedDaily(List<String> tnl,String beginDate,String endDate,String page,String rows) throws Exception;
	/***
	 * 
	 * @Description:查询结账明细日报  当天的
	 * @author:  donghe
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	int queryVdetailedDailyTotal(List<String> tnl,String beginDate,String endDate) throws Exception;
	StatVo findMaxMin() throws Exception;
}
