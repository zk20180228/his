package cn.honry.hiasMongo.exception.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/except/HIASMongo")
public class HIASExceptionAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(HIASExceptionAction.class);
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	
	private String page;//起始页数
	private String rows;//数据列数 
	
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
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
	/**
	 * 异常list界面
	 * @Author zxh
	 * @time 2017年3月27日
	 * @return
	 */
	@RequiresPermissions(value={"YCRZ:function:view"})
	@Action(value = "listHIASException", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/hiasException/hiasExceptionList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listHIASException() {
		return "list";
	}
	/**
	 * 查询异常信息
	 * @Author zxh
	 * @time 2017年3月27日
	 */
	@RequiresPermissions(value={"YCRZ:function:query"})
	@Action(value = "queryHIASException")
	public void queryHIASException(){
		RecordToHIASException hiasException = new RecordToHIASException();
		String qCodeName = request.getParameter("codeName");
		String qWarnLevel = request.getParameter("warnLevelSelect");
		String qWarnType = request.getParameter("warnTypeSelect");
		String qWarnDate = request.getParameter("startWarnDate");
		String qEndWarnDate = request.getParameter("endWarnDate");
		String qProcessStatus = request.getParameter("processStatus");
		hiasException.setCodeName(qCodeName);
		hiasException.setWarnLevel(qWarnLevel);
		hiasException.setWarnType(qWarnType);
		if(StringUtils.isNotBlank(qWarnDate)){//告警查询起始时间
			hiasException.setWarnDate(DateUtils.parseDateY_M_D(qWarnDate));
		}
		if(StringUtils.isNotBlank(qEndWarnDate)){//告警查询截止时间，暂存放在处理时间处
			hiasException.setProcessTime(DateUtils.parseDateY_M_D(qEndWarnDate));
		}
		hiasException.setProcessStatus(qProcessStatus);
		JSONArray jsonArray = hiasExceptionService.getHIASExceptionByPage(page, rows, hiasException);
		Long total = hiasExceptionService.getTotalByPage(hiasException);
		try {
			WebUtils.webSendJSON("{\"total\":" + total + ",\"rows\":" + jsonArray + "}");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.error("XTGL_YCRZ", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_YCRZ", "系统管理_异常日志", "2", "0"), ex);
		}
	}
	/**
	 * 开始处理
	 * @Author zxh
	 * @time 2017年3月28日
	 */
	@RequiresPermissions(value={"YCRZ:function:operation"})
	@Action(value = "startDeal")
	public void startDeal(){
		PrintWriter out = null;
		String id = request.getParameter("id");
		try {
			out = WebUtils.getResponse().getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.error("XTGL_YCRZ", e1);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_YCRZ", "系统管理_异常日志", "2", "0"), e1);
		}
		try{
			hiasExceptionService.startDeal(id);
			out.write("success");
		}catch(Exception e){
			out.write("error");
			logger.error("XTGL_YCRZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_YCRZ", "系统管理_异常日志", "2", "0"), e);
		}
	}
	/**
	 * 处理完成
	 * @Author zxh
	 * @time 2017年3月28日
	 */
	@RequiresPermissions(value={"YCRZ:function:operation"})
	@Action(value = "endDeal")
	public void endDeal(){
		PrintWriter out = null;
		String id = request.getParameter("id");
		try {
			out = WebUtils.getResponse().getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.error("XTGL_YCRZ", e1);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_YCRZ", "系统管理_异常日志", "2", "0"), e1);
		}
		try{
			hiasExceptionService.endDeal(id);
			out.write("success");
		}catch(Exception e){
			out.write("error");
			logger.error("XTGL_YCRZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_YCRZ", "系统管理_异常日志", "2", "0"), e);
		}
	}
}
