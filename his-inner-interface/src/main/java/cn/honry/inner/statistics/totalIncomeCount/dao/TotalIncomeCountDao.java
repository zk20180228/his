package cn.honry.inner.statistics.totalIncomeCount.dao;

import java.util.List;

import cn.honry.inner.statistics.totalIncomeCount.vo.MapVo;

public interface TotalIncomeCountDao {
	
	
	/**
	 * 
	 * @Description:按天统计'总收入情况统计'的数据，你可以用此接口将数据导入mongodb中，更新当天的数据
	 * @param date 时间(格式为:YYYY-MM-DD)
	 * @return 
	 * List
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月21日 下午2:54:41
	 */
	public List<MapVo> init_ZSRQKTJ_dataByDay(String date);
	
	/**
	 * 
	 * @Description:将操作日志保存到mongo中
	 * @param obj
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月21日 下午7:02:50
	 */
	public void save(Object obj);
	

}
