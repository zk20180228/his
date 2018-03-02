package cn.honry.statistics.sys.reportForms.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.statistics.sys.reportForms.vo.IncomeVo;
import cn.honry.statistics.sys.reportForms.vo.PatientInfoVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;

@SuppressWarnings({"all"})
public interface ReportFormsService {

	/**  
	 *  
	 * @Description：  医生工作量统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 下午3:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptw科室
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 *
	 */
	List<DoctorWorkloadStatistics> queryReservation(String deptw, String stimew, String etimew,String menuAlias)throws Exception;
	
	/**  
	 *  
	 * @Description：  门诊住院情况统计统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 下午3:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	List<PatientInfoVo> queryPatientInfo(String dept, String stime, String etime,String menuType);
	
	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 下午3:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	List<StatisticsVo> queryStatisticsInfo(String dept,String expxrts, String stime, String etime);
	
	/**  
	 * @Description：  门诊收入统计图 elasticsearch实现
	 * @Author：朱振坤
	 * @param  date 日期 dateSign为0或4时，格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"
	 * @param  dateSign 按日月年查询的标记,0、初始化查询所有；1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
	 *
	 */
	Map<String, Object> queryOutpatientChartsByES(String date, String dateSign);
	
	/**
	 * <p>门诊各项收入统计 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017-05-19 5:18:28 
	 * @Modifier: zhangkui
	 * @ModifyDate:  2017-05-19 5:18:28 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCodes 科室编号
	 * @param expxrtCodes 医生编号
	 * @param beginDate 开始时间 yyyy-MM-dd
	 * @param endDate 结束时间 yyyy-MM-dd
	 * @return
	 * @throws Exception
	 * @throws:
	 *
	 */
	public List<StatisticsVo> listStatisticsQueryByMongo(String deptCodes,String expxrtCodes,String beginDate, String endDate)throws Exception;
	
	/**
	 * 门诊各项收入统计 elasticsearch实现
	 * 
	 * @Author: 朱振坤
	 * @param deptCodes 科室code字符串，多个code以“,”隔开
	 * @param expxrtCodes 医生code字符串，多个code以“,”隔开
	 * @param beginDate 查询开始时间 以“createtime”为查询字段
	 * @param endDate 查询结束时间 包括当日
	 * @param page easyUi分页参数
	 * @param rows easyUi分页参数
	 * @return 封装easyUi表格的json数据的集合
	 */
	public List<StatisticsVo> listStatisticsQueryByES(String deptCodes,String expxrtCodes,String beginDate, String endDate);
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
	Map TBTotalIncome(String dateSign, String searchTime)throws Exception;
	
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
	Map HBTotalIncome(String dateSign, String searchTime)throws Exception;
	
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
	 Map deptTopF(String dateSign, String searchTime)throws Exception;
	
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
	 Map docterTopF(String dateSign, String searchTime)throws Exception;
	 
	   /**
		 * 门诊收入统计--从mongodb中获取数据
		 * @param date 日期
		 * @param dateSign 日期类型
		 * @return
		 */
	String queryOutpatientChartsByMongo(String date,String dateSign);

}
