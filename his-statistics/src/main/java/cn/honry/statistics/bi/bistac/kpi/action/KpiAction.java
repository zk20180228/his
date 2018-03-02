package cn.honry.statistics.bi.bistac.kpi.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.statistics.bi.bistac.kpi.service.KpiService;
import cn.honry.statistics.bi.bistac.kpi.vo.KpiVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/kpi")
public class KpiAction extends ActionSupport {
	private String danw;
	private String time;
	private KpiService kpiService;
	private String Etime;
	private String year;
	private String month;
	@Autowired
    @Qualifier(value = "kpiService")
	public void setKpiService(KpiService kpiService) {
		this.kpiService = kpiService;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEtime() {
		return Etime;
	}

	public void setEtime(String etime) {
		Etime = etime;
	}

	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Action(value = "kpilist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/kpi.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String kpilist() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		String s[] =Etime.split("-");
		year =s[0];
		month =s[0]+"-"+s[1];
		return "list";
	}
	@Action(value = "queryAllData", results = { @Result(name = "json", type = "json") })
	public void queryAllData() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
//			list<kpivo> kpilist = kpiservice.queryalldata(danw,time);
			map = kpiService.queryAllData(danw,time);
//			map.put("resCode", "success");
//			map.put("data", kpiList);
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("data", new ArrayList<KpiVo>());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "queryKPT", results = { @Result(name = "json", type = "json") })
	public void queryKPT() throws Exception{
		int[]  total = kpiService.getTotalTime(danw,time);
		String json=JSONUtils.toJson(total);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "queryEverMonth", results = { @Result(name = "json", type = "json") })
	public void queryEverMonth() throws Exception{
		List<Object[]>  total = kpiService.queryEverMonth(danw,time);
		String json=JSONUtils.toJson(total);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "compareToBefore", results = { @Result(name = "json", type = "json") })
	public void compareToBefore() throws Exception{
		int[]  total = kpiService.compareToBefore(danw,time);
		String json=JSONUtils.toJson(total);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "everMonthToCom", results = { @Result(name = "json", type = "json") })
	public void everMonthToCom() throws Exception{
		List<Object[]>  total = kpiService.everMonthToCom(danw,time);
		String json=JSONUtils.toJson(total);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查询当天的门急诊人次
	 */
	@Action(value = "getMJZCount", results = { @Result(name = "json", type = "json") })
	public void  getMJZCount(){
		Integer MJZCount= kpiService.getMJZCount();
		WebUtils.webSendString(MJZCount.toString());
	}
	
	
	
	
	
}

