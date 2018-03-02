package cn.honry.statistics.bi.bistac.totalDrugUsed.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.statistics.bi.bistac.totalDrugUsed.service.TotalDrugUsedService;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/totalDrugUsed")
public class TotalDrugUsedAction {
	@Autowired
	@Qualifier("totalDrugUsedService")
	private  TotalDrugUsedService totalDrugUsedService;

	public void setTotalDrugUsedService(TotalDrugUsedService totalDrugUsedService) {
		this.totalDrugUsedService = totalDrugUsedService;
	}
	
	private String begin;
	private String end;
	
	
	
	public String getBegin() {
		return begin;
	}



	public void setBegin(String begin) {
		this.begin = begin;
	}



	public String getEnd() {
		return end;
	}



	public void setEnd(String end) {
		this.end = end;
	}



	@Action(value="imtotalDrugDate")
	public void imtotalDrugDate(){
		totalDrugUsedService.initGroupDrugByOneDay(begin, end);
	}
}
