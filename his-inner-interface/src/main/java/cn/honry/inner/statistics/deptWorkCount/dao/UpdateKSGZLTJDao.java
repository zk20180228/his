package cn.honry.inner.statistics.deptWorkCount.dao;

import java.util.List;

@SuppressWarnings({"all"})
public interface UpdateKSGZLTJDao {

	/**
	 * @Description:将挂号主表中有关'科室工作量统计'的数据导入mongodb中,按日在oracle中进行查询一次，存一次。初始化数据可用，每天定时更新在线表数据到mongo中也可用，也可以按天更新历史数据
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param ghList 挂号主表的分区表集合
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月28日 下午2:13:09
	 */
	public int imTableData_MZTJFX_KSGZLTJ_DAY(String stime,String etime,List ghList);
	
	/**
	 * @Description:将'科室工作量统计'统计的数据初始化到月表或者年表中，根据type参数决定。也可以在定时更新日表的时候，级联更新月表，年表时使用
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param type 2->月 ,3->年
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月28日 下午2:13:09
	 */
	public int imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(String stime, String etime,String type);
	
}
