package cn.honry.statistics.bi.bistac.monthlyDashboard.action;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
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
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service.ListTotalIncomeStaticService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.service.MonthlyDashboardService;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientStac.service.OutpatientStacVoService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;



/**
* @ClassName: 每月综合仪表盘 
* @author yuke
* @date 2017年7月3日
*
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/monthlyDashboard")
public class MonthlyDashboardAction extends ActionSupport{
	
	private Logger logger=Logger.getLogger(MonthlyDashboardAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//定制百分比
	private	DecimalFormat nf = new DecimalFormat("00.00%");
	@Autowired
	@Qualifier(value = "monthlyDashboardService")
	private MonthlyDashboardService monthlyDashboardService;
	@Autowired
	@Qualifier(value = "outpatientStacVoService")
	private OutpatientStacVoService outpatientStacVoService;
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "listTotalIncomeStaticService")
	private ListTotalIncomeStaticService listTotalIncomeStaticService;
	private String date;
	private String deptName;
	private String deptCode;
	private String Etime;
	private String startDate;
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * 每月综合仪表盘 
	 * @return
	 */
	@Action(value = "monthlyDashboardlist", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/monthlyDashboard.jsp") }, 
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String monthlyDashboardlist() {
		Date date = new Date();
		Etime = DateUtils.formatDateYM(date);
		return "list";
	}
	/**  
	 * 
	 * 月出院人数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryInpatientInfoNowGo")
	public void queryInpatientInfoNowGo(){
		List<MonthlyDashboardVo> list =new ArrayList<MonthlyDashboardVo>(1);
		try {
			if ("全部".equals(deptName)) {
				deptName="";
			}
			if(monthlyDashboardService.isCollection("MYZHYBP_INPATIENT_MONTH")){
				list = monthlyDashboardService.queryInpatientInfoNowGo(date, deptName);
			}else{
				String[] arr = date.split("-");
				int year=Integer.parseInt(arr[0]);
				int month=Integer.parseInt(arr[1]);
				String year_1=year-1+"";
				String month1=month<10?"0"+month:""+month;
				String startTime=year_1+"-"+month1+"-01";
				Calendar cal = Calendar.getInstance();
				//设置年份
				cal.set(Calendar.YEAR,year);
				//设置月份
				cal.set(Calendar.MONTH, month-1);
				//获取某月最大天数
				int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				//设置日历中月份的最大天数
				cal.set(Calendar.DAY_OF_MONTH, lastDay);
				//格式化日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String endTime = sdf.format(cal.getTime());
				list= monthlyDashboardService.queryInpatientInfoFromOracle(startTime, endTime, deptName);
			}
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_MYZHYBP", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MYZHYBP", "运营分析_每月综合仪表盘", "2", "0"), e);
		}
        String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryWardApply")
	public void queryWardApply() throws ParseException{
		List<Dashboard> list =new ArrayList<Dashboard>(1);
		try {
			if ("全部".equals(deptName)) {
				deptName="";
			}
			Boolean b=monthlyDashboardService.isCollection("MYZHYBP_WARDAPPLY_MONTH");
			if(b){
				list= monthlyDashboardService.queryWardApply(date, deptName);
			}else{
				String[] arr = date.split("-");
				int year=Integer.parseInt(arr[0]);
				int month=Integer.parseInt(arr[1]);
				String year_1=year-1+"";
				String month1=month<10?"0"+month:""+month;
				String startTime=year_1+"-"+month1+"-01";
				Calendar cal = Calendar.getInstance();
				//设置年份
				cal.set(Calendar.YEAR,year);
				//设置月份
				cal.set(Calendar.MONTH, month-1);
				//获取某月最大天数
				int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				//设置日历中月份的最大天数
				cal.set(Calendar.DAY_OF_MONTH, lastDay);
				//格式化日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String endTime = sdf.format(cal.getTime());
				list= monthlyDashboardService.queryWardApplyFromOracle(startTime,endTime, deptName);
			}
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_MYZHYBP", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MYZHYBP", "运营分析_每月综合仪表盘", "2", "0"), e);
		}
	
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 月手术例数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午9:10:43 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午9:10:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryOperationApply")
	public void queryOperationApply(){
		String data =request.getParameter("date");
		List<Dashboard> list = null;
		try {
			list= monthlyDashboardService.querySurgeryForDB(data,deptName);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YZJC_MYZHYBP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MYZHYBP", "运营分析_每月综合仪表盘", "2", "0"), e);
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 平均住院日
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryInpatientInfo")
	public void queryInpatientInfo(){	
        //上年
        String lastY = this.getLastYear();
        //上月
        String lastM = this.getLastMonth();
        List<MonthlyDashboardVo> inpatientInfo = null;
        try {
            inpatientInfo = monthlyDashboardService.queryInpatientInfo(deptName);
    		long avgTotal = this.getAvgTotal(date,inpatientInfo);
    		long avgLastYTotal = this.getAvgTotal(lastY,inpatientInfo);
    		long avgLastMTotal = this.getAvgTotal(lastM,inpatientInfo);
    		//同比
    		String avgLastYTotalPer=null;
    		//环比
    		String avgLastMTotalPer=null;
    		if (avgLastYTotal==0) {
    			avgLastYTotalPer="--";
    		}else{
    			avgLastYTotalPer=nf.format((float)avgTotal/avgLastYTotal);
    		}
    		if (avgLastMTotal==0) {
    			avgLastMTotalPer="--";
    		}else{
    			avgLastMTotalPer=nf.format((float)avgTotal/avgLastMTotal);
    		}
    		Map<String,Object> map=new HashMap<String,Object>();
    		map.put("avgTotal", avgTotal);
    		map.put("avgLastYTotalPer", avgLastYTotalPer);
    		map.put("avgLastMTotalPer", avgLastMTotalPer);
    		String json = JSONUtils.toJson(map);
    		WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_MYZHYBP", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MYZHYBP", "运营分析_每月综合仪表盘", "2", "0"), e);
		}
    
	}
	/**  
	 * 
	 * 治疗数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryTreatment")
	public void queryTreatment(){
		List<Dashboard> list = null;
		try {
			if ("全部".equals(deptName)) {
				deptName="";
			}
			String date=request.getParameter("date");
			list=monthlyDashboardService.queryTreatmentFormDB(date, deptName);
			String[] date1 = null;
			date1 = returnNowMonth(date);
			if(list.size()==0){
				list = monthlyDashboardService.queryTreatment2(date1[0],date1[1], deptName);
			}
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_MYZHYBP", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MYZHYBP", "运营分析_每月综合仪表盘", "2", "0"), e);
		}
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 住院费用
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryHospExpenses")
	public void queryHospExpenses(){
		HashMap<String, Double> map=new HashMap<String,Double>();
		try {
			if ("全部".equals(deptName)) {
				deptName="";
			}
			if(StringUtils.isBlank(startDate)){
				startDate=new  SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
			}
			if(StringUtils.isBlank(endDate)){
				endDate=new  SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
			}
			List<MonthlyDashboardVo> list = monthlyDashboardService.queryHospExpenses(startDate,endDate, deptName);
			//判断mongdb中是否有数据，没有数据的时候，分区查询数据
			String[] date1 = null;
				date1 = returnNowMonth(date);
			if(list.size()==0){
				 list = monthlyDashboardService.queryHospExpenses2(date1[0],date1[1], deptName);
			}
			for(MonthlyDashboardVo vo:list){
				map.put(vo.getYearAndMonth(), vo.getTotCost0());
			}
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_MYZHYBP", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MYZHYBP", "运营分析_每月综合仪表盘", "2", "0"), e);
		}
		String json= JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 封装求上一年同月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastYear(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.YEAR, -1);
        Date d = calendar.getTime();
        //上年
        String lastDate = format.format(d);
		return lastDate;
	}
	/**  
	 * 
	 * 封装求上一月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.MONTH, -1);
        Date dd = calendar.getTime();
        //上月
        String lastM = format.format(dd);
		return lastM;
	}
	/**  
	 * 
	 * 封装平均住院日
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 下午6:27:31 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 下午6:27:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private long getAvgTotal(String date,List<MonthlyDashboardVo> inpatientInfo ){
		long nd = 1000*24*60*60;//一天的毫秒数
		
		Date firstInDate=DateUtils.parseDateY_M_D(date+"-01");
		Calendar a=Calendar.getInstance();
	    a.setTime(firstInDate); 
	    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
	    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
	    int MaxDate=a.get(Calendar.DATE);
		Date lastOutDate=DateUtils.parseDateY_M_D(date+"-"+MaxDate);
		
		long days=0;
		long dayTotal=0;
		long avgTotal=0;
		int i=0;
		long t1 = 0;
		long t2 = 0;
		long t3 = 0;
		long t4 = 0;
		long t5 = 0;
		for (MonthlyDashboardVo monthlyDashboardVo : inpatientInfo) {
			Date inDate = monthlyDashboardVo.getInDate();
			Date outDate = monthlyDashboardVo.getOutDate();
			if (inDate!=null) {
				t1 = inDate.getTime();
				t3 = lastOutDate.getTime();
				t4 = firstInDate.getTime();
				t5=new Date().getTime();
				if (outDate!=null ) {
					t2 = outDate.getTime();
					if (t2>=t4 && t2>=t1) {
						if (t1>=t4 ) {
							if (t2>=t3) {
								days=(t3-t1)/nd;
							}else{
								days=(t2-t1)/nd;
								
							}
							dayTotal+=(days+1);
							i++;
						}else{
							if (t2>=t3) {
								days=(t3-t4)/nd;
							}else{
								days=(t2-t4)/nd;
							}
							dayTotal+=(days+1);
							i++;
						}
					}
				}else{
					if (t5<t3) {
						if (t1>=t4) {
							days=(t5-t1)/nd;
						}else{
							days=(t5-t4)/nd;
						}
					}else{
						if (t1>=t4) {
							days=(t3-t1)/nd;
						}else{
							days=(t3-t4)/nd;
						}	
					}
					dayTotal+=(days+1);
					i++;
				}	
			}
			
		}
		if (i!=0) {
			avgTotal=dayTotal/i;
		}else{
			avgTotal=0;
		}
		return avgTotal;
		
	}
	public String[] returnNowMonth(String date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		String date1=null;
		try {
			date1 = sdf.format(sdf1.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String[] date2=date1.split("-");
		Calendar ca=Calendar.getInstance();
		ca.set(Integer.parseInt(date2[0]), Integer.parseInt(date2[1])-1,1);
		ca.add(Calendar.MONTH,1);
		ca.add(Calendar.DATE,-1);
		
		Date date3=ca.getTime();
		String lastMonth=sdf.format(date3);
		 ca.set(GregorianCalendar.DAY_OF_MONTH, 1); 
		 date3=ca.getTime();
		 ca.add(Calendar.YEAR, -1);
		 ca.set(Calendar.DATE, 0);
		 ca.add(Calendar.DATE, 1);
		 String fristMonth=sdf.format(ca.getTime());
		 String[] month={fristMonth,lastMonth};
		 return month;
	}
}

