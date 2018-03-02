package cn.honry.statistics.bi.bistac.operationNum.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.statistics.bi.bistac.deptAndFeeData.vo.StatisticsVo;
import cn.honry.statistics.bi.bistac.operationNum.service.OperationNumsService;
import cn.honry.statistics.bi.bistac.operationNum.vo.OperationNumsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/operationNums")
@SuppressWarnings({ "all" })
public class OperationNumsAction {

	@Autowired
	@Qualifier(value = "operationNumsService")
	private OperationNumsService operationNumsService;

	public void setOperationNumsService(OperationNumsService operationNumsService) {
		this.operationNumsService = operationNumsService;
	}
	
	//开始时间
	private String startDate;
	//结束时间
	private String endDate;
	//时间类型
	private String timeType;
    //今天	
	private String todayTime;
	//昨天
	private String yesterDayTime; 

	//开始时间
	private String startMonthDate;
	
	//开始时间
	private String startYearDate;
	
	/***
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public String getStartYearDate() {
		return startYearDate;
	}

	public void setStartYearDate(String startYearDate) {
		this.startYearDate = startYearDate;
	}

	public String getStartMonthDate() {
		return startMonthDate;
	}

	public void setStartMonthDate(String startMonthDate) {
		this.startMonthDate = startMonthDate;
	}

	public String getTodayTime() {
		return todayTime;
	}

	public void setTodayTime(String todayTime) {
		this.todayTime = todayTime;
	}

	public String getYesterDayTime() {
		return yesterDayTime;
	}

	public void setYesterDayTime(String yesterDayTime) {
		this.yesterDayTime = yesterDayTime;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

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

	
	@Action(value="query")
	public void query(){
		Map<String , Object> map=new HashMap<String , Object>();
		//门诊或住院
		List<OperationNumsVo> mzZyList=operationNumsService.query(startDate, Integer.valueOf(timeType));
		List<StatisticsVo> sVoList = new ArrayList<StatisticsVo>();
		map.put("zyMzBar",JSONUtils.toJson(sVoList));
		for(OperationNumsVo vo : mzZyList){
			Integer totalNum = vo.getMzssNum()+vo.getZyssNum();
			switch (vo.getDistrict()) {//0整体 2郑东 3惠济 0-2-3=河医院区
			case "0":
				StatisticsVo mzVo = new StatisticsVo();
				mzVo.setName("门诊");
				mzVo.setValue((double)vo.getMzssNum());
				StatisticsVo zyVo = new StatisticsVo();
				zyVo.setName("住院");
				zyVo.setValue((double)vo.getZyssNum());
				sVoList.add(zyVo);
				sVoList.add(mzVo);
				map.put("zyMzBar",JSONUtils.toJson(sVoList));//饼图json
				map.put("zjNum", totalNum);
				break;
			case "1":
				map.put("hyNum", totalNum);
				break;
			case "2":
				map.put("zdNum", totalNum);
				break;
			case "3":
				map.put("hjNum", totalNum);
				break;
			default:
				break;
			}
		}
		Integer zjNum = (Integer) map.get("zjNum")==null?0:(Integer) map.get("zjNum");
		Integer zdNum = (Integer) map.get("zdNum")==null?0:(Integer) map.get("zdNum");
		Integer hjNum = (Integer) map.get("hjNum")==null?0:(Integer) map.get("hjNum");
		Integer hyNum = (Integer) map.get("hyNum")==null?(zjNum-zdNum-hjNum):(Integer) map.get("hyNum");
		map.put("zjNum", zjNum);
		map.put("zdNum", zdNum);
		map.put("hjNum", hjNum);
		map.put("hyNum", hyNum);
		
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 环比
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	@Action(value="queryMom")
	public void queryMom() throws NumberFormatException, ParseException{
		Map<String , Object> map=new HashMap<String , Object>();
		List<StatisticsVo> mzZyList=operationNumsService.queryMom(startDate, Integer.valueOf(timeType));
		List<String> dateList = new ArrayList<String>(); 
		List<String> valList = new ArrayList<String>(); 
		for(StatisticsVo vo : mzZyList){
			dateList.add(vo.getName());
			valList.add(String.valueOf(vo.getValue()));
		}
		map.put("dateList", dateList);
		map.put("valList", valList);
		
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 同比
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	@Action(value="queryYoy")
	public void queryYoy() throws NumberFormatException, ParseException{
		Map<String , Object> map=new HashMap<String , Object>();
		List<StatisticsVo> mzZyList=operationNumsService.queryYoy(startDate, Integer.valueOf(timeType));
		List<String> dateList = new ArrayList<String>(); 
		List<String> valList = new ArrayList<String>(); 
		for(StatisticsVo vo : mzZyList){
			dateList.add(vo.getName());
			valList.add(String.valueOf(vo.getValue()));
		}
		map.put("dateList", dateList);
		map.put("valList", valList);
		
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	
	
	
	/***
	 * @Description:手术例数统计
	 * @Description:
	 * @author:  zhangjin
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	@Action(value="operactionNumslist",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/operation/operationNums/operationNums.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String operactionlist(){
		 todayTime=DateUtils.formatDateY_M_D_H_M_S(new Date());
		 yesterDayTime=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), -1));
		return "list";
	}
	
	/**  
	 * 
	 * 查询正在做和已完成的手术例数（当天）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 *
	 */
	@Action(value="getDoingOrFinish")
	public void getDoingOrFinish(){
		Map map=new HashMap();
		//科室前五
		List<OperationNumsVo> list1=operationNumsService.queryOperationNumsTopDept(DateUtils.formatDateY_M_D(new Date()),"3");
		map.put("deptList", list1);
		//门诊或住院
		List<OperationNumsVo> mzlist=operationNumsService.queryTotalCount(DateUtils.formatDateY_M_D(new Date()),"3");
		map.put("mzOrZy", mzlist);
		//急诊或普通
		List<OperationNumsVo> list2=operationNumsService.queryJzOrPtCount(DateUtils.formatDateY_M_D(new Date()), "3");
		map.put("jzOrPt", list2);
		//列表数据
		List<OperationNumsVo> list=operationNumsService.getDoingOrFinish(DateUtils.formatDateY_M_D(new Date())+" 00:00:00", DateUtils.formatDateY_M_D_H_M_S(new Date()));
		map.put("wchy", 0);
		map.put("wczd", 0);
		map.put("wchj", 0);
		map.put("zzhy", 0);
		map.put("zzzd", 0);
		map.put("zzhj", 0);
		for(int i=0;i<list.size();i++){
			if("wc".equals(list.get(i).getFinalType())){
				if("1".equals(list.get(i).getDistrict())){
					map.put("wchy", list.get(i).getNums());
				}else if("2".equals(list.get(i).getDistrict())){
					map.put("wczd", list.get(i).getNums());
				}else{
					map.put("wchj", list.get(i).getNums());
				}
			}else{
				if("1".equals(list.get(i).getDistrict())){
					map.put("zzhy", list.get(i).getNums());
				}else if("2".equals(list.get(i).getDistrict())){
					map.put("zzzd", list.get(i).getNums());
				}else{
					map.put("zzhj", list.get(i).getNums());
				}
			}
			
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询已完成手术例数（昨天）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 *
	 */
	@Action(value="getYesterDay")
	public void getYesterDay(){
		String startYesDate="";
		String endYesDate="";
		if(StringUtils.isNotBlank(yesterDayTime)){
			startYesDate=yesterDayTime+" 00:00:00";
			endYesDate=yesterDayTime+" 23:59:59";
		}else{
			yesterDayTime=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), -1));
			startYesDate=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), -1))+" 00:00:00";
			endYesDate=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), -1))+" 23:59:59";
		}
		
		List<OperationNumsVo> list=operationNumsService.getYesterDayFinish(startYesDate, endYesDate);
		Map map=new HashMap();
		map.put("wchy", 0);
		map.put("wczd", 0);
		map.put("wchj", 0);
		for(int i=0;i<list.size();i++){
			if("1".equals(list.get(i).getDistrict())){
				map.put("wchy", list.get(i).getNums());
			}else if("2".equals(list.get(i).getDistrict())){
				map.put("wczd", list.get(i).getNums());
			}else{
				map.put("wchj", list.get(i).getNums());
			}
		}
		List<OperationNumsVo> yoylist=operationNumsService.queryYoyCount(yesterDayTime,"3");
		map.put("yoylist", yoylist);
		List<OperationNumsVo> ratiolist=operationNumsService.queryRatioCount(yesterDayTime,"3");
		map.put("ratiolist", ratiolist);
		List<OperationNumsVo> mzlist=operationNumsService.queryTotalCount(yesterDayTime,"3");
		map.put("mzlist", mzlist);
		//急诊或普通
		List<OperationNumsVo> list2=operationNumsService.queryJzOrPtCount(yesterDayTime, "3");
		map.put("jzOrPt", list2);
		//急诊或普通
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**  
	 * 
	 * 查询已完成手术例数（月份）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 * @param startMonthDate 时间
	 */
	@Action(value="getMonthData")
	public void getMonthData(){
		Map map=new HashMap();
		List<OperationNumsVo> mzlist=null;
		//急诊或普通
		List<OperationNumsVo> list2=null;
		String monthStart="";
		String monthEnd="";
		if(StringUtils.isBlank(startMonthDate)){
			//门诊或住院
			mzlist=operationNumsService.queryTotalCount(DateUtils.formatDateY_M(new Date()),"2");
			map.put("mzlist", mzlist);
			//急诊或普通
			list2=operationNumsService.queryJzOrPtCount(DateUtils.formatDateY_M(new Date()), "2");
			map.put("jzOrPt", list2);
			monthStart=DateUtils.formatDateY_M(new Date())+"-01 00:00:00";
			monthEnd=DateUtils.formatDateY_M_D_H_M_S(new Date());
		}else{
			//门诊或住院
			mzlist=operationNumsService.queryTotalCount(startMonthDate,"2");
			map.put("mzlist", mzlist);
			//急诊或普通
			list2=operationNumsService.queryJzOrPtCount(startMonthDate, "2");
			map.put("jzOrPt", list2);
			
			if(DateUtils.formatDateY_M(new Date()).equals(startMonthDate)){
				monthStart=startMonthDate+"-01 00:00:00";
				monthEnd=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM");
				try {
					monthStart=startMonthDate+"-01 00:00:00";
					Date date = sdf.parse(startMonthDate);
					Calendar calendar = Calendar.getInstance();
			        calendar.setTime(date);
			        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			        monthEnd=DateUtils.formatDateY_M_D(calendar.getTime())+" 23:59:59";
				} catch (ParseException e) {
				}
			}
		} 
		List<OperationNumsVo> tableList=operationNumsService.getYesterDayFinish(monthStart, monthEnd);
		map.put("wchy", 0);
		map.put("wczd", 0);
		map.put("wchj", 0);
		for(int i=0;i<tableList.size();i++){
			if("1".equals(tableList.get(i).getDistrict())){
				map.put("wchy", tableList.get(i).getNums());
			}else if("2".equals(tableList.get(i).getDistrict())){
				map.put("wczd", tableList.get(i).getNums());
			}else{
				map.put("wchj", tableList.get(i).getNums());
			}
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**  
	 * 
	 * 查询已完成手术例数（昨天+月+年）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 * @param startMonthDate 时间
	 *
	 */
	@Action(value="getYesterDayFinish")
	public void getYesterDayFinish(){
		String monthStart="";
		String monthEnd="";
		if("1".equals(timeType)){
			if(StringUtils.isBlank(startYearDate)){
				monthStart=DateUtils.formatDateY(new Date())+"-01-01 00:00:00";
				monthEnd=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				if(DateUtils.formatDateY(new Date()).equals(startYearDate)){
					monthEnd=DateUtils.formatDateY_M_D_H_M_S(new Date());
				}else{
					monthEnd=startYearDate+"-12-31 23:59:59";
				}
				monthStart=startYearDate+"-01-01 00:00:00";
			}
			
		}
		List<OperationNumsVo> list=operationNumsService.getYesterDayFinish(monthStart, monthEnd);
		Map map=new HashMap();
		map.put("wchy", 0);
		map.put("wczd", 0);
		map.put("wchj", 0);
		for(int i=0;i<list.size();i++){
			if("1".equals(list.get(i).getDistrict())){
				map.put("wchy", list.get(i).getNums());
			}else if("2".equals(list.get(i).getDistrict())){
				map.put("wczd", list.get(i).getNums());
			}else{
				map.put("wchj", list.get(i).getNums());
			}
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**  
	 * 
	 * 查询已完成手术例数（当天）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 * @param startMonthDate 时间
	 * @param timeType 区分标记  年 月 日
	 *
	 */
	@Action(value="getMzOrZy")
	public void getMzOrZy(){
		Map map=new HashMap();
		String monthStart="";
		if(StringUtils.isBlank(startYearDate)&&"1".equals(timeType) ){
			monthStart=DateUtils.formatDateY(new Date());
		}else if(StringUtils.isBlank(startYearDate)&&"2".equals(timeType) ){
			monthStart=DateUtils.formatDateY_M(new Date());
		}else if(StringUtils.isBlank(startYearDate)&&"3".equals(timeType)){
			monthStart=DateUtils.formatDateY_M_D(new Date());
		}else{
			monthStart=startYearDate;
		}
		List<OperationNumsVo> list=operationNumsService.queryTotalCount(monthStart,timeType);
		map.put("mzOrZy", list);
		//急诊或普通
		List<OperationNumsVo> list2=operationNumsService.queryJzOrPtCount(monthStart, timeType);
		map.put("jzOrPt", list2);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**  
	 * 
	 * 查询已完成手术例数（同比）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 * @param startMonthDate 时间
	 * @param timeType 区分标记  年 月 日
	 *
	 */
	@Action(value="queryYoyCount")
	public void queryYoyCount(){
		String monthStart="";
		if(StringUtils.isBlank(startDate)&&"2".equals(timeType) ){
			monthStart=DateUtils.formatDateY_M(new Date());
		}else if(StringUtils.isBlank(startDate)&&"3".equals(timeType)){
			monthStart=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), -1));
		}else{
			monthStart=startDate;
		}
		List<OperationNumsVo> list=operationNumsService.queryYoyCount(monthStart,timeType);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询已完成手术例数（环比）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 * @param startMonthDate 时间
	 * @param timeType 区分标记  年 月 日
	 *
	 */
	@Action(value="queryRatioCount")
	public void queryRatioCount(){
		String monthStart="";
		if(StringUtils.isBlank(startDate)&&"1".equals(timeType) ){
			monthStart=DateUtils.formatDateY(new Date());
		}else if(StringUtils.isBlank(startDate)&&"2".equals(timeType) ){
			monthStart=DateUtils.formatDateY_M(new Date());
		}else if(StringUtils.isBlank(startDate)&&"3".equals(timeType)){
			monthStart=DateUtils.formatDateY_M_D(new Date());
		}else{
			monthStart=startDate;
		}
		List<OperationNumsVo> list=operationNumsService.queryRatioCount(monthStart,timeType);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
}
