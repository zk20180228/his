package cn.honry.portal.procedure.action;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.portal.procedure.service.ProcedureService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 数据库添加冗余字段
 * @author hzr
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/procedure/procedure")
public class ProcedureAction extends ActionSupport{
	Logger logger = Logger.getLogger(ProcedureAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "procedureService")
	ProcedureService procedureService;
	
	//冗余字段的表名
	private String table;
	//存储过程表名
	private String protable;
	//最大最小时间
	private String mindate;
	private String maxdate;
	
	public String getMindate() {
		return mindate;
	}

	public void setMindate(String mindate) {
		this.mindate = mindate;
	}

	public String getMaxdate() {
		return maxdate;
	}

	public void setMaxdate(String maxdate) {
		this.maxdate = maxdate;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getProtable() {
		return protable;
	}

	public void setProtable(String protable) {
		this.protable = protable;
	}
	@Action(value = "gettime")
	public void gettime(){
		Map<String, Object> map = procedureService.gettime(table);
		String mapJosn = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapJosn);
	}
	
	@Action(value = "updateprodure")
	public void updateprodure(){
		logger.info("存储过程名称："+protable+",开始更新日期："+mindate+",结束更新日期："+maxdate);
		if(StringUtils.isBlank(protable)||StringUtils.isBlank(mindate)||StringUtils.isBlank(maxdate)){
			logger.error("参数有空值，更新终止！");
			return;
		}
		Date date;//时间循环变量
		String movedate;//时间循环变量对应的字符串
		String maxsdate;//时间循环变量对应的字符串
		Date minDate=DateUtils.parseDateY_M_D_SLASH(mindate);//获取最小时间参数  yyyy/MM/dd
		Date maxDate=DateUtils.parseDateY_M_D_SLASH(maxdate);//获取最大时间参数
		if(maxDate.compareTo(minDate)>0){
			date=minDate;//时间循环变量
			do{
				movedate=DateUtils.formatDateY_M_SLASH(date);
				date=DateUtils.addDay(date, 1);
				maxsdate=DateUtils.formatDateY_M_SLASH(date);
				try{
					procedureService.getupdate(protable, maxsdate, movedate);
					logger.info("存储过程名称："+protable+",日期【"+movedate+"】数据更新成功！");
				}catch(Exception e){
					logger.error("存储过程名称："+protable+",日期【"+movedate+"】数据更新失败！任务终止！");
					break;
				}
			}while(date.before(maxDate));
		}
	}
}
