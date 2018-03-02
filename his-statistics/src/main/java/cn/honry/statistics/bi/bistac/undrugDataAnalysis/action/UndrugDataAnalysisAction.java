package cn.honry.statistics.bi.bistac.undrugDataAnalysis.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.service.UndrugDataAnalysisService;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @ClassName: 住院非药品收入统计分析
 * @Description: 住院非药品收入统计分析
 * @author yuke
 * @date 2017年7月3日
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/undrugAnalysis")
public class UndrugDataAnalysisAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(UndrugDataAnalysisAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;

	public void setHiasExceptionService(
			HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}

	@Autowired
	@Qualifier(value = "undrugAnalysisService")
	private UndrugDataAnalysisService undrugDataAnalysisService;
	@Autowired
	@Qualifier(value = "monthlyDashboardService")
	private MonthlyDashboardService monthlyDashboardService;
	private String SearchTime;
	private String YTime;
	private String MTime;
	private String DTime;
	private String type;
	private String startTime;
	private String endTime;
	private String t;

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

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

	/**
	 * 
	 * <p>非药品数据分析view </p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:06:19 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:06:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "undrugAnalysislist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/undrugDataAnalysis.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
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
	 * <p>非药品数据查询</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:06:34 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:06:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "queryUndrugData")
	public void queryUndrugData() throws ParseException {
		List<Map<String, List<UndrugDataVo>>> list = new ArrayList<>();
		String stime = "";
		String etime = "";
		String totalQuery = null;// 费用查询名
		String deptQuery = null;// 科室top表名
		String docQuery = null;// 医生top
		try {
			if ("年".equals(type)) {
				totalQuery = "FYPSRFXHZ_TOTAL_YEAR";
				deptQuery = "FYPSRFXHZ_DEPT_YEAR";
				docQuery = "FYPSRFXHZ_DOC_YEAR";
				startTime = SearchTime + "-01-01";
				endTime = SearchTime + "-12-31";
				stime = Integer.parseInt(SearchTime) - 6 + "-01-01";
				etime = SearchTime + "-12-31";
				t = "1";
			} else if ("月".equals(type)) {
				totalQuery = "FYPSRFXHZ_TOTAL_MONTH";
				deptQuery = "FYPSRFXHZ_DEPT_MONTH";
				docQuery = "FYPSRFXHZ_DOC_MONTH";
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
				t = "2";
			} else if ("日".equals(type)) {
				totalQuery = "FYPSRFXHZ_TOTAL_DAY";
				deptQuery = "FYPSRFXHZ_DEPT_DAY";
				docQuery = "FYPSRFXHZ_DOC_DAY";
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
			Map<String, List<UndrugDataVo>> map = new HashMap<>();
			// 费别统计
			Boolean b1 = monthlyDashboardService.isCollection(totalQuery);

			if (b1) {
				List<UndrugDataVo> list1 = undrugDataAnalysisService
						.queryUndrugTypeFeeFromDB(SearchTime, t, totalQuery);
				map.put("1", list1);
			} else {
				List<UndrugDataVo> list1 = undrugDataAnalysisService
						.queryUndrugTypeFee(startTime, endTime);
				map.put("1", list1);
			}
			// 科室统计top5
			Boolean b2 = monthlyDashboardService.isCollection(deptQuery);

			if (b2) {
				List<UndrugDataVo> list2 = undrugDataAnalysisService
						.queryUndrugDeptFeeTop5FromDB(SearchTime, t, deptQuery);
				double dou1 = 0.0;
				double dou11 = 0.0;
				Integer j = null;
				for (int i = list2.size() - 1; i >= 0; i--) {
					if (list2.get(i).getTotal() != 0) {
						if ("其他".equals(list2.get(i).getDeptName())) {
							j = i;
							dou11 += list2.get(i).getTotal();
						} else {
							dou1 += list2.get(i).getTotal();
						}
					}
				}
				if (j != null) {
					list2.get(j).setTotal(dou11 - dou1);
				}
				map.put("2", list2);
			} else {
				List<UndrugDataVo> list2 = undrugDataAnalysisService
						.queryUndrugDeptFeeTop5(startTime, endTime);
				Map<String, String> deptMap = undrugDataAnalysisService
						.querydeptCodeAndNameMap();
				if (list2 != null && list2.size() > 0) {
					for (UndrugDataVo u : list2) {
						if (StringUtils
								.isNotBlank(deptMap.get(u.getDeptName()))) {
							u.setDeptName(deptMap.get(u.getDeptName()));
						}
					}
				}
				map.put("2", list2);
			}
			// 医生统计top5
			Boolean b3 = monthlyDashboardService.isCollection(docQuery);
			
			if (b3) {
				List<UndrugDataVo> list3 = undrugDataAnalysisService
						.queryUndrugDocFeeTop5FromDB(SearchTime, t, docQuery);
				double dou1 = 0.0;
				double dou11 = 0.0;
				Integer j = null;
				for (int i = list3.size() - 1; i >= 0; i--) {
					if (list3.get(i).getTotal() != 0) {
						if ("其他".equals(list3.get(i).getDocName())) {
							j = i;
							dou11 += list3.get(i).getTotal();
						} else {
							dou1 += list3.get(i).getTotal();
						}
					}
				}
				if (j != null) {
					list3.get(j).setTotal(dou11 - dou1);
				}
				map.put("3", list3);
			} else {
				List<UndrugDataVo> list3 = undrugDataAnalysisService
						.queryUndrugDocFeeTop5(startTime, endTime);
				map.put("3", list3);
			}

			// 同比
			List<UndrugDataVo> list5 = new ArrayList<>();
			List<UndrugDataVo> list4 = new ArrayList<>();
			if (!"日".equals(type) || !SearchTime.endsWith("02-29")) {// 闰年不可日同比
				Boolean b5 = monthlyDashboardService.isCollection(totalQuery);
				if (b5) {
					list5 = undrugDataAnalysisService
							.queryUndrugFeeSameDOMFromDB(SearchTime, t,
									totalQuery);
					map.put("5", list5);
					list4 = undrugDataAnalysisService.queryUndrugFeeMoMFromDB(
							SearchTime, t, totalQuery);
					map.put("4", list4);
				} else {
					if ("年".equals(type)) {
						list5 = new ArrayList<UndrugDataVo>();
					} else {
						List<Map<String, String>> monthsOrDays = getMonthsOrDays(
								SearchTime, type);
						for (Map<String, String> m : monthsOrDays) {
							UndrugDataVo vo = undrugDataAnalysisService
									.queryUndrugFeeSameDOM(m.get("startTime"),
											m.get("endTime"), type);
							// 当月或当日无数据
							if (vo.getName() == null) {
								if ("月".equals(type)) {
									vo = new UndrugDataVo();
									vo.setName(m.get("startTime").substring(0,
											7));
								} else if ("日".equals(type)) {
									vo = new UndrugDataVo();
									vo.setName(m.get("startTime"));
								}
							}
							list5.add(vo);
						}
					}
					map.put("5", list5);
					// 环比
					list4 = undrugDataAnalysisService.queryUndrugFeeMoM(stime,
							etime, type);
					map.put("4", list4);
				}
			}
			list.add(map);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			// hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_FYPSRFXHZ", e);

			// hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(
					new RecordToHIASException("YZJC_FYPSRFXHZ",
							"运营分析_非药品收入分析汇总", "2", "0"), e);
		}
	}

	/**
	 * 
	 * @Title: getMonthsOrDays
	 * @param @param date
	 * @param @param dateSing
	 * @param @return 参数
	 * @return List<Map<String,String>> 返回类型
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
