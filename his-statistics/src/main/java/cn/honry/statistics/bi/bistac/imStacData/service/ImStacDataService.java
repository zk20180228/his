  package cn.honry.statistics.bi.bistac.imStacData.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletContext;

import cn.honry.statistics.sys.reportForms.vo.ReportVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;


public interface ImStacDataService {

	/**
	 * 定时任务方法
	 * @Author zxh
	 * @time 2017年5月9日
	 */
	public void imEachDay();
	/**
	 * 导入历史数据
	 * @Author zxh
	 * @time 2017年5月9日
	 */
	public void imTableData();
	/**
	 * 门急诊人次统计-------------有参
	 * @Author zxh
	 * @time 2017年5月23日
	 * @param startTime
	 * @param endTime
	 */
	public void imMZJZRCTJ(String startTime ,String endTime);
	/**
	 * 
	 * @Description:向mongodb更新每天的数据
	 * void
	 * @exception:
	 * @author: zk
	 * @time:2017年5月11日 下午5:27:28
	 */
	public void imEachDay_T_INPATIENT_INFO();
	
	/**
	 * 
	 * @Description:向mongodb导入历史数据
	 * void
	 * @exception:
	 * @author: zk
	 * @time:2017年5月11日 下午5:27:28
	 */
	public void imTableData_T_INPATIENT_INFO();
	/**  
	 * 
	 * 将处方明细表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月11日 下午3:48:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月11日 下午3:48:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData(String begin,String end);
	/**  
	 * 
	 * 药占比（YZB）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午2:11:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午2:11:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inEachDay_YZB();
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午3:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午3:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_YYTS(String begin,String end);
	/**  
	 * 
	 * 门诊用药天数（YYTS）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午3:29:00 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午3:29:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inEachDay_YYTS();
	/**  
	 * 
	 * 将医生用药金额表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_YSYYJE(String begin,String end);
	/**  
	 * 
	 * 医生用药金额表（YSYYJE）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inEachDay_YSYYJE();
	/**  
	 * 
	 * 将科室用药金额表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_KSYYJE(String begin,String end);
	/**  
	 * 
	 * 科室用药金额表（KSYYJE）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inEachDay_KSYYJE();
	/**
	 * 导入门诊工作量历史数据
	 * @Author zxh
	 * @time 2017年5月12日
	 */
	public void imTableData_GZL();
	/**
	 * 门诊工作量-----按天-----有参
	 * @Author zxh
	 * @time 2017年5月23日
	 * @param startTime
	 * @param endTime
	 */
	public void imTableData_GHYSGZLDAY(String startTime ,String endTime);
	/**
	 * 门诊工作量-----按月-----有参
	 * @Author zxh
	 * @time 2017年5月23日
	 * @param startTime
	 * @param endTime
	 */
	public void imTableData_GZLMonth1(String startTime ,String endTime);
	/**
	 * 导入挂号医生工作量历史数据
	 * @Author zxh
	 * @time 2017年5月16日
	 */
	public void imTableData_GHYSGZL();
	/**
	 * 按月导入工作量
	 * @Author zxh
	 * @time 2017年5月17日
	 */
	public void imTableData_GZLMonth();
	/**
	 * 按年导入工作量
	 * @Author zxh
	 * @time 2017年5月17日
	 */
	public void imTableData_GZLYear();
	/**
	 * 按天导入医生处方量历史数据
	 * @Author zxh
	 * @time 2017年5月18日
	 */
	public void imTableData_MZCFGZL();
	/**
	 * 按年导入医生处方量历史数据
	 * @Author zxh
	 * @time 2017年5月18日
	 */
	public void imTableData_MZCFGZLYear();
	/**
	 * 按月导入医生处方量历史数据
	 * @Author zxh
	 * @time 2017年5月18日
	 */
	public void imTableData_MZCFGZLMonth();
	/**
	 * 导入每天的门诊工作量
	 * @Author zxh
	 * @time 2017年5月13日
	 */
	public void imEachDay_GZL();
	/**
	 * 按天导入每天工作量
	 * @Author zxh
	 * @time 2017年5月17日
	 */
	public void imEachDay_GHYSGZL();
	/**
	 * 按月导入工作量
	 * @Author zxh
	 * @time 2017年5月17日
	 */
	public void imEachDay_GZLMonth();
	/**
	 * 按年导入工作量
	 * @Author zxh
	 * @time 2017年5月17日
	 */
	public void imEachDay_GZLYear();
	/**
	 * @Description:门诊药房退费统计：把now表中的数据更新到mongodb中
	 * 结算信息表：T_INPATIENT_CANCELITEM_NOW
	 * 病区退费申请表：T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 * @author: zhangkui
	 * @time:2017年5月13日 下午8:04:54
	 */
	public void imEachDay_T_INPATIENT_CANCELITEM();
	
	/**
	 * @Description:门诊药房退费统计：把数据导入mongodb中
	 * 结算信息表：T_FINANCE_INVOICEINFO_NOW，T_FINANCE_INVOICEINFO
	 * 病区退费申请表：T_FINANCE_INVOICEINFO_NOW，T_FINANCE_INVOICEINFO 
	 * @author: zhangkui
	 * @time:2017年5月12日 下午8:04:54
	 */
	public void imTableData_T_INPATIENT_CANCELITEM();
	
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_YPJE();
	public void inTableData_YPJE2(String begin,String end);
	/**  
	 * 
	 * 门诊月药品金额，用药数量，人次表(YPJE)向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午5:45:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午5:45:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inEachDay_YPJE2();
	/**  
	 * 
	 * 住院收入统计表（ZYSRTJ）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月17日 下午7:07:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月17日 下午7:07:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_ZYSRTJ();
	
	/**
	 *@Description:门诊各项收入统计：把数据导入mongodb中
	 *处方明细表:T_OUTPATIENT_FEEDETAIL  T_OUTPATIENT_FEEDETAIL_NOW 
	 * @author: zhangkui
	 * @time:2017年5月18日 
	 */
	public void imTableData_T_OUTPATIENT_FEEDETAIL();
	
	/**
	 *@Description:把门诊各项收入统计：把每天now表数据更新到mongodb中
	 *处方明细表: T_OUTPATIENT_FEEDETAIL_NOW 
	 * @author: zhangkui
	 * @time:2017年5月20日 
	 */
	public void imEachDay_T_OUTPATIENT_FEEDETAIL();
	

	/**
	 * 向mongodb中导入历史数据和在线库数据：治疗效果数据分析
	 * @param startTime 开始时间,格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间,格式：yyyy-MM-dd HH:mm:ss
	  * @param l  分区表集合
	 * @throws ParseException 
	 *  @Author zhangkui
	 * @time 2017年5月22日
	 */
	public void imTableData_T_INPATIENT_INFO(String startTime, String endTime)throws ParseException;
	
	/**
	 * @Description:门诊药房退费统计：把数据导入mongodb中
	 *病区退费申请表：T_INPATIENT_CANCELITEM_NOW,lT_INPATIENT_CANCELITEM
	 *结算信息表:T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 *@param startTime:开始时间 
	 *@param endTime :结束时间
	 *@param invoiceInfoPartName 结算信息表分区表
	 *@param cancelPartName 病区退费申请表分区表
	 * @author: zhangkui
	 * @throws Exception 
	 * @time:2017年5月22日 下午8:04:54
	 */
	public void imTableData_T_INPATIENT_CANCELITEM(String startTime,String endTime) throws Exception;
	
	/**
	 *@Description:门诊各项收入统计：把数据导入mongodb中
	 *处方明细表:T_OUTPATIENT_FEEDETAIL  T_OUTPATIENT_FEEDETAIL_NOW 
	 *@param startTime：开始时间
	 *@param endTime：结束时间
	 *@param table:T_OUTPATIENT_FEEDETAIL,处方明细表
	 *@param gcode:费用类别
	 * @author: zhangkui
	 * @throws ParseException 
	 * @time:2017年5月22日 
	 */
	public void imTableData_T_OUTPATIENT_FEEDETAIL(String startTime ,String endTime) throws ParseException;
	
	
	/**
	 *@Description: 科室统计--门诊住院工作同期对比表 病床信息表:T_BUSINESS_HOSPITALBED---->没有分区表，没有在线表  
	 * 挂号主表:T_REGISTER_MAIN----->有分区表 ,有在线表 ,走分区
	 * 住院表：T_INPATIENT_INFO----->没有分区表，有在线表 ，但是还要走分区
	 *@param:Btime 开始时间 :注意按月更新,如果时间为2017-01-01 00:00:00-2017-06-07 00:00:00 更新数据为为1-6月的所有数据
	 *@param:Etime 结束时间:注意按月更新,如果时间为2017-01-01 00:00:00-2017-06-07 00:00:00 更新数据为为1-6月的所有数据
	 *@param:areaCode 院区编号，默认全部
	 *@param:ghList 挂号分表集合
	 *@param:zyList 住院分表集合
	 *@author zhangkui
	 * @throws Exception 
	 */
	public void imTableData_KSTJ_MZZYGZTQDBB(String Btime,String Etime,String areaCode) throws Exception;
	
	void inindata(List<StatisticsVo> list,String date);
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月5日 下午4:42:03 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月5日 下午4:42:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public void inTableData_KSDBB(String begin,String end);
	
	
}
