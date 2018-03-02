package cn.honry.statistics.sys.reportForms.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.sys.reportForms.vo.IncomeVo;
import cn.honry.statistics.sys.reportForms.vo.ReportVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;

/**
 * 住院各项统计jdbc
 * @author hzr
 *
 */
@SuppressWarnings({"all"})
public interface ReportFormsjdbcDao {

	/**
	 * 查询统计的费用类型
	 */
	public List<ReportVo> getEncode();
	
	/**
	 * 医生各项收入统计
	 * @param table 表名
	 * @param gcode 费用类别code
	 * @param dept 所在科室
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @return
	 */
	public List<StatisticsVo> getlist(List<String> table,List<ReportVo> gcode,String dept,String expxrts, String stime, String etime);
	
	/**
	 * 收入统计
	 * @param feedetialTabName 表名
	 * @param encode 
	 * @param emp 医生的id
	 * @param stime 时间
	 * @return
	 */
	public List<IncomeVo> queryfeedetall(List<String> feedetialTabName, List<ReportVo> encode, String emp, String sDate, String eDate);
	
	/**
	 * 门诊各项收入统计
	 * 2017-05-19
	 * zhangkui
	 * @return
	 */
	public List<StatisticsVo> listStatisticsQueryByMongo(String deptCodes,String expxrtCodes,String beginDate, String endDate)throws Exception;
	
	/**
	 * @Description:同比，6，月，日，柱状图
	 * @param dateSign 年月日的标记：分别是1,2,3
	 * @param searchTime 时间：yyyy-MM-dd
	 * @param tlList 分区表集合
	 * @param encode 费用类别
	 * @return 
	 * Map
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月26日 上午9:44:22
	 */
	public Map TBTotalIncome(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode)throws Exception;
	
	/**
	 * @Description:环比,6,年,月,日,柱状图
	 * @param dateSign 年月日的标记：分别是1,2,3
	 * @param searchTime 时间：yyyy-MM-dd
	 * @param tlList 分区表集合
	 * @param encode 费用类别
	 * @return
	 * Map
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月26日 上午9:44:22
	 */
	public Map HBTotalIncome(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode)throws Exception;
	
	/**
	 * @Description:科室前五
	 * @param dateSign 年月日的标记：分别是1,2,3
	 * @param searchTime 时间：yyyy-MM-dd
	 * @param tlList 分区表集合
	 * @param encode 费用类别
	 * @return
	 * Map
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月26日 上午9:44:22
	 */
	 public Map deptTopF(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode)throws Exception;
	
	 /**
		 * @Description:医生前五
		 * @param dateSign 年月日的标记：分别是1,2,3
		 * @param searchTime 时间：yyyy-MM-dd
		 * @param tlList 分区表集合
	     * @param encode 费用类别
		 * @return
		 * Map
		 * @exception:
		 * @author: zhangkui
		 * @time:2017年5月26日 上午9:44:22
		 */
	 public Map docterTopF(String dateSign, String searchTime,List<String> tlList, List<ReportVo> encode)throws Exception; 
	
	 /**
	  * 获查询门诊下的科室code和name
	  * @return
	  */
	 List<SysDepartment> getDept();
	
}
