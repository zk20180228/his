package cn.honry.inner.statistics.totalIncomeCount.service;


public interface TotalIncomeCountService {

	/**
	 * @Description:按天统计'总收入情况统计'的数据，会级联更新本月，本年的数据，而dao层与此同名的方法只是通过oracle统计当天的数据
	 * @param date 时间(格式为:YYYY-MM-DD)
	 * @param menuAlias：栏目名：ZSRQKTJ,这个和mongo中的集合名字也有关系
	 * @param type 取值1,2,3分别代表更新日表，月表，年表,此方法只接受1，别瞎传，数据会出错
	 * @return 
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月21日 下午2:54:41
	 */
	public void init_ZSRQKTJ_dataByDay(String menuAlias,String type,String date);
	
	
	
}
