package cn.honry.statistics.leaveOrder.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.statistics.leaveOrder.service.LeaveOrderService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/leaveOrder")
public class LeaveOrderAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	/**栏目别名,在主界面中点击栏目时传到action的参数**/
	private String menuAlias;
	//注入service
	@Autowired 
	@Qualifier(value = "leaveOrderService")
	private LeaveOrderService leaveOrderService;
	public void setLeaveOrderService(LeaveOrderService leaveOrderService) {
		this.leaveOrderService = leaveOrderService;
	}


	//病历号
	private String queryBlh;
	//流水号
	private String queryLsh;

	//开始时间
	private String startTime;
	
	//结束时间
	private String endTime;

	private String page;
	
	private String rows;


	public String getQueryBlh() {
		return queryBlh;
	}

	public void setQueryBlh(String queryBlh) {
		this.queryBlh = queryBlh;
	}

	public String getQueryLsh() {
		return queryLsh;
	}

	public void setQueryLsh(String queryLsh) {
		this.queryLsh = queryLsh;
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

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}	
		/**
		 * 跳转页面
		 * @return
		 */
		@SuppressWarnings("deprecation")
		@RequiresPermissions(value={"CYHZYZCX:function:view"})
		@Action(value = "list", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/leaveOrders/leaveOrderList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String list(){
			Date date = new Date();
			startTime = DateUtils.formatDateYM(date)+"-01";
			endTime = DateUtils.formatDateY_M_D(date);
			ServletActionContext.getRequest().setAttribute("strat", startTime);
			ServletActionContext.getRequest().setAttribute("end", endTime);
			return "list";
		}
	
		
		/**
		 * 根据病历号查询患者信息
		 */
		@Action(value = "queryLSHList")
		public void queryLSHList(){
			List<InpatientInfoNow> infoList=leaveOrderService.queryLSHList(queryBlh,queryLsh,startTime,endTime);
			String json=JSONUtils.toJson(infoList,DateUtils.DATE_FORMAT);
			WebUtils.webSendJSON(json);
		}
		
		
		@Action(value = "queryDrugLists")
		public void queryDrugLists(){
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<InpatientExecdrugNow> list=null;
			int total = 0;
			if(StringUtils.isBlank(queryLsh)){
				list=new ArrayList<InpatientExecdrugNow>();
			}else{
				total = leaveOrderService.queryDrugListsTotal(queryLsh, startTime, endTime);
				list=leaveOrderService.queryDrugLists(queryLsh,page,rows,startTime,endTime);
			}
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}
		
		
		@Action(value = "queryUnDrugList")
		public void queryUnDrugList(){
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<InpatientExecundrugNow> list=null;
			int total = 0;
			if(StringUtils.isBlank(queryLsh)){
				list=new ArrayList<InpatientExecundrugNow>();
			}else{
				total = leaveOrderService.queryUnDrugListTotal(queryLsh, startTime, endTime);
				list=leaveOrderService.queryUnDrugList(queryLsh,page,rows,startTime,endTime);
			}
			retMap.put("total", total);
			retMap.put("rows", list);
			String json=JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}
		
		/**
		 * GH
		 * 弹窗 
		 * @return
		 */
		@Action(value = "selectDialogURL", results = { @Result(name = "selectDialogURL", location = "/WEB-INF/pages/stat/leaveOrders/selectDialog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String selectDialogURL(){
			return "selectDialogURL";
		}
		
}
