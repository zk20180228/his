package cn.honry.statistics.bi.bistac.kpi.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.bi.bistac.kpi.vo.KpiVo;
@SuppressWarnings({"all"})
public interface KpiDao extends EntityDao<KpiVo> {
	StatVo findMaxMin();

	List<Object[]> queryEverMonth(List<String> tnL, String danw, String time);

	int[] getTotalTime(List<String> tnL, String danw, String time);

	int[] compareToBefore(List<String> tnL, String danw, String time);

	List<Object[]> everMonthToCom(List<String> tnL,String danw, String time);
	
	
	/**查询当天的门急诊人次
	 * zhangkui
	 * 2017-05-12
	 */
	public int getMJZCount();

	List<KpiVo> initMZKPI(String beginDate, String endDate);
	
	
}
