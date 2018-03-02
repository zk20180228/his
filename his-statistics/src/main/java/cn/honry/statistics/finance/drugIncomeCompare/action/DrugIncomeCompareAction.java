package cn.honry.statistics.finance.drugIncomeCompare.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.finance.drugIncomeCompare.service.DrugIncomeCompareService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
* @ClassName: 住院药品收入统计分析
* @author yuke
* @date 2017年7月3日
*
*/
@Controller
@Scope("prototype")
@Namespace(value = "/statistics/DrugIncome")
@SuppressWarnings({ "all" })
public class DrugIncomeCompareAction extends ActionSupport {
	
	private Logger logger=Logger.getLogger(DrugIncomeCompareAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	private static final long serialVersionUID = 1L;
	private String SearchTime;
	private String YTime;
	private String MTime;
	private String DTime;
	private String type;
	private String startTime;
	private String endTime;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSearchTime() {
		return SearchTime;
	}

	public void setSearchTime(String searchTime) {
		SearchTime = searchTime;
	}

	public String getYTime() {
		return YTime;
	}

	public void setYTime(String yTime) {
		YTime = yTime;
	}

	public String getMTime() {
		return MTime;
	}

	public void setMTime(String mTime) {
		MTime = mTime;
	}

	public String getDTime() {
		return DTime;
	}

	public void setDTime(String dTime) {
		DTime = dTime;
	}

	@Autowired
	@Qualifier("drugIncomeCompareService")
	private DrugIncomeCompareService drugIncomeCompareService;
	private List<Dashboard> docTop5;
	private List<Dashboard> deptTop5;
	private List<Dashboard> queryFee;
	private List<Dashboard> list2;
	private List<Dashboard> list3;
	Map<String, List<Dashboard>> map = new HashMap<>();

	public void setDrugIncomeCompareService(
			DrugIncomeCompareService drugIncomeCompareService) {
		this.drugIncomeCompareService = drugIncomeCompareService;
	}

	/**
	* @Title: 药品统计分析视图
	* @param @return    参数
	* @return String    返回类型
	* @throws
	*/
	@Action(value = "drugIncomeCompare", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/drugDataAnalysis.jsp") }
	)
	public String monthlyDashboardlist() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		YTime = DateUtils.formatDateY(date);
		MTime = format.format(date);
		DTime = DateUtils.formatDateY_M_D(date);
		return "list";
	}

	/**
	 * 
	* @Title: 查询药品数据
	* @param     参数
	* @return void    返回类型
	* @throws
	 */
	@Action(value = "queryDrugFee")
	public void queryDrugFee() {
		List<Dashboard> dashboards = new ArrayList<Dashboard>();
		List<Map<String, List<Dashboard>>> list = new ArrayList<>();
		String stime = "";
		String etime = "";
		String totalQuery=null;//费用查询名
		String deptQuery=null;//科室top表名
		String docQuery=null;//医生top
		try {
			if ("年".equals(type)) {
				totalQuery="YPSRFXHZ_TOTAL_YEAR";
				deptQuery="YPSRFXHZ_DEPT_YEAR";
				docQuery="YPSRFXHZ_DOC_YEAR";
				startTime = SearchTime + "-01-01";
				endTime = SearchTime + "-12-31";
				stime = Integer.parseInt(SearchTime) - 6 + "-01-01";
				etime = SearchTime + "-12-31";
			} else if ("月".equals(type)) {
				totalQuery="YPSRFXHZ_TOTAL_MONTH";
				deptQuery="YPSRFXHZ_DEPT_MONTH";
				docQuery="YPSRFXHZ_DOC_MONTH";
				startTime = SearchTime + "-01";
				String[] arr = SearchTime.split("-");
				int year = Integer.parseInt(arr[0]);
				int month = Integer.parseInt(arr[1]);
				Calendar cal = Calendar.getInstance();
				// 设置年份
				cal.set(Calendar.YEAR, year);
				// 设置月份
				cal.set(Calendar.MONTH, month - 1);
				// 获取某月最大天数
				int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				// 设置日历中月份的最大天数
				cal.set(Calendar.DAY_OF_MONTH, lastDay);
				// 格式化日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				endTime = sdf.format(cal.getTime());
				// 获取环比开始时间 结束时间
				Calendar cal1 = Calendar.getInstance();
				cal1.set(year, month, 1);
				cal1.add(Calendar.MONTH, -6);
				stime = sdf.format(cal1.getTime());
				etime = endTime;
			} else if ("日".equals(type)) {
				totalQuery="YPSRFXHZ_TOTAL_DAY";
				deptQuery="YPSRFXHZ_DEPT_DAY";
				docQuery="YPSRFXHZ_DOC_DAY";
				startTime = SearchTime;
				endTime = startTime;
				String[] arr = SearchTime.split("-");
				int year = Integer.parseInt(arr[0]);
				int month = Integer.parseInt(arr[1]);
				int day = Integer.parseInt(arr[2]);
				// 获取环比开始时间 结束时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal1 = Calendar.getInstance();
				cal1.set(year, month - 1, day);
				cal1.add(Calendar.DATE, -6);
				stime = sdf.format(cal1.getTime());
				etime = endTime;
			}
			Map<String, List<Dashboard>> map = new HashMap<>();
			// 费用
			if (drugIncomeCompareService.isCollection(totalQuery)) {
				queryFee = drugIncomeCompareService.queryFeeByMongo(SearchTime,type,totalQuery);
			} else {
				queryFee = drugIncomeCompareService.queryFee(startTime, endTime,type);
			}
			// 科室前5
			if (drugIncomeCompareService.isCollection(deptQuery)) {
				deptTop5 = drugIncomeCompareService.queryDeptTop5ByMongo(SearchTime, type,deptQuery);
			} else {
				deptTop5 = drugIncomeCompareService.queryDeptTop5(startTime,endTime, type);
			}

			// 医生前五
			if (drugIncomeCompareService.isCollection(docQuery)) {
				docTop5 = drugIncomeCompareService.queryDocTop5ByMongo(SearchTime,type,docQuery);
			} else {
				docTop5 = drugIncomeCompareService.queryDocTop5(startTime, endTime,type);
			}
			// 所用环比
			if (drugIncomeCompareService.isCollection(totalQuery)) {
				list2 = drugIncomeCompareService.querysequentialDrugByMongo(SearchTime, type,totalQuery);
			} else {
				list2 = drugIncomeCompareService.compareFeeByYear(stime, etime,type);
			}
			// 同比
			if (("日".equals(type) && SearchTime.endsWith("02-29")) && "年".equals(type) ) {
				list3 = new ArrayList<Dashboard>();
			}else{
				if (drugIncomeCompareService.isCollection(totalQuery)) {
					list3 = new ArrayList<Dashboard>();
					list3 = drugIncomeCompareService.queryAllSameFeeByMongo(SearchTime,type,totalQuery);
				}else{
					List<Map<String, String>> monthsOrDays = getMonthsOrDays(SearchTime, type);
					for (Map<String, String> m : monthsOrDays) {
						Dashboard dashboard = drugIncomeCompareService.queryAllSameFee(m.get("startTime"),m.get("endTime"), type);
						if (dashboard.getName() == null) {
							if ("月".equals(type)) {
								dashboard = new Dashboard();
								dashboard.setName(m.get("startTime").substring(0, 7));
							} else if ("日".equals(type)) {
								dashboard = new Dashboard();
								dashboard.setName(m.get("startTime"));
							}
							list3.add(dashboard);
						}
					}
					
				}
			}
			map.put("1", queryFee);
			map.put("2", deptTop5);
			map.put("3", docTop5);
			map.put("4", list2);
			map.put("5", list3);
			list.add(map);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_YPSRFXHZ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_YPSRFXHZ", "运营分析_药品收入分析汇总", "2", "0"), e);
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	* @Title: getMonthsOrDays
	* @param @param date
	* @param @param dateSing
	* @param @return    参数
	* @return List<Map<String,String>>    返回类型
	* @throws
	 */
	public List<Map<String, String>> getMonthsOrDays(String date,
			String dateSing) {
		String[] strArr = new String[7];
		String[] dateArr = date.split("-");
		int dateTemp = Integer.parseInt(dateArr[0]);
		for (int i = 0; i < 7; i++) {
			if ("月".equals(dateSing)) {// 月同比
				strArr[i] = (dateTemp - i) + "-" + dateArr[1];
			} else {
				strArr[i] = (dateTemp - i) + "-" + dateArr[1] + "-"
						+ dateArr[2];
			}
		}
		List<Map<String, String>> dateList = new ArrayList<>();
		for (int i = 0; i < strArr.length; i++) {
			if ("月".equals(type)) {
				String sTime = strArr[i] + "-01";
				String[] arr = strArr[i].split("-");
				int year = Integer.parseInt(arr[0]);
				int month = Integer.parseInt(arr[1]);
				Calendar cal = Calendar.getInstance();
				// 设置年份
				cal.set(Calendar.YEAR, year);
				// 设置月份
				cal.set(Calendar.MONTH, month - 1);
				// 获取某月最大天数
				int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				// 设置日历中月份的最大天数
				cal.set(Calendar.DAY_OF_MONTH, lastDay);
				// 格式化日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String eTime = sdf.format(cal.getTime());
				Map<String, String> map = new HashMap<>();
				map.put("startTime", sTime);
				map.put("endTime", eTime);
				dateList.add(map);
			} else if ("日".equals(type)) {
				String sTime = strArr[i];
				Map<String, String> map = new HashMap<>();
				map.put("startTime", sTime);
				map.put("endTime", sTime);
				dateList.add(map);
			}
		}
		return dateList;
	}
}
