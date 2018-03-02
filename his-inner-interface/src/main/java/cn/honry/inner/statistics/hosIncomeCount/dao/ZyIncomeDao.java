package cn.honry.inner.statistics.hosIncomeCount.dao;

import java.util.List;

import cn.honry.inner.statistics.hosIncomeCount.vo.MapVo;

public interface ZyIncomeDao {
	
	
	/**
	 * 
	 * @Description:TODO
	 * @param date
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月24日 上午10:02:59
	 */
	public List<MapVo>init_ZYSRTJ_dataByDay(String date);
	
	/**
	 * 
	 * @Description:将操作日志保存到mongo中
	 * @param obj
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月24日 下午 4:56:50
	 */
	public void save(Object obj);
}
