package cn.honry.statistics.bi.bistac.listOperationStatic.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.statistics.bi.bistac.listOperationStatic.service.ListOperationStaticService;
import cn.honry.statistics.bi.bistac.listOperationStatic.vo.ListOperationStaticVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/listOperationStatic")
public class ListOperationStaticAction {
	@Autowired
	@Qualifier(value = "listOperationStaticService")
	private ListOperationStaticService listOperationStaticService;
	
	/**
	 * 开始时间
	 * */
	private String endTime;
	
	/**
	 * 结束时间
	 * */
	private String startTime;
	
	/**
	 * 区分标记
	 * */
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Action(value="queryListOperationStatic")
	public void queryListOperationStatic(){
		if(StringUtils.isBlank(startTime)){
			if("1".equals(type)){
				startTime=DateUtils.formatDateY(new Date())+"-01-01 00:00:00";
			}else if("2".equals(type)){
				startTime=DateUtils.formatDateY_M(new Date())+"-01 00:00:00";
			}else{
				startTime=DateUtils.formatDateY_M_D(new Date())+" 00:00:00";
			}
			endTime=DateUtils.formatDateY_M_D_H_M_S(new Date());
		}else{
			if("1".equals(type)){
				if(DateUtils.formatDateY(new Date()).equals(startTime)){
					endTime=DateUtils.formatDateY_M_D_H_M_S(new Date());
				}else{
					endTime=startTime+"-12-31 23:59:59";
				}
				startTime=startTime+"-01-01 00:00:00";
			}else if("2".equals(type)){
				if(DateUtils.formatDateY_M(new Date()).equals(startTime)){
					startTime=startTime+"-01 00:00:00";
					endTime=DateUtils.formatDateY_M_D_H_M_S(new Date());
				}else{
					SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM");
					try {
						startTime=startTime+"-01 00:00:00";
						Date date = sdf.parse(startTime);
						Calendar calendar = Calendar.getInstance();
				        calendar.setTime(date);
				        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				        endTime=DateUtils.formatDateY_M_D(calendar.getTime())+" 23:59:59";
					} catch (ParseException e) {
					}
				}
			}else{
				if(DateUtils.formatDateY_M_D(new Date()).equals(startTime)){
					endTime=DateUtils.formatDateY_M_D_H_M_S(new Date());
				}else{
					endTime=startTime+" 23:59:59";
				}
				startTime=startTime+" 00:00:00";
			}
		}
		
		ListOperationStaticVo queryVo = listOperationStaticService.queryVo(startTime,endTime);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("num1", queryVo.getNum1());
		map.put("num2", queryVo.getNum2());
		map.put("num3",0);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
