package cn.honry.statistics.bi.bistac.kpi.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.bistac.kpi.vo.KpiVo;
import cn.honry.base.service.BaseService;
@SuppressWarnings({"all"})
public interface KpiService extends BaseService<KpiVo>{
	int[] getTotalTime(String danw, String time);
	List<Object[]> queryEverMonth(String danw, String time);
	int[] compareToBefore(String danw, String time);
	List<Object[]> everMonthToCom(String danw, String time);
	public int getMJZCount();
	void init_MZKPI(String beginDate,String endDate,Integer type)throws Exception;
	Map<String, Object> queryAllData(String danw, String time)throws Exception;
}
