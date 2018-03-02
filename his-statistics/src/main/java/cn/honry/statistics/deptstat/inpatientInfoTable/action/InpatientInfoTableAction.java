package cn.honry.statistics.deptstat.inpatientInfoTable.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import cn.honry.statistics.deptstat.deptBedsMessage.action.DeptBedsMessageAction;
import cn.honry.statistics.deptstat.inpatientInfoTable.service.InpatientInfoTableService;
import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 住院病人动态报表 ACTION
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/InpatientInfoTable")
// @Namespace(value = "/stat")
public class InpatientInfoTableAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "inpatientInfoTableService")
	private InpatientInfoTableService inpatientInfoTableService;
	public void setInpatientInfoTableService(
			InpatientInfoTableService inpatientInfoTableService) {
		this.inpatientInfoTableService = inpatientInfoTableService;
	}

	private Logger logger=Logger.getLogger(DeptBedsMessageAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	private String startTime;
	private String endTime;
	private String menuAlias;
	private String page;//页数
	private String rows;//每页数
	private String deptCode;//科室

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

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	@SuppressWarnings("unused")
	private String webPath = "WEB-INF" + File.separator + "reportFormat" + File.separator + "jasper" + File.separator;

	/**
	 * 
	 * @Description： 获取list页面
	 * 
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41 @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value = { "ZZBRDTBB:function:view" })
	@Action(value = "listInpatientInfoTable", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/inpatientInfoTable/inpatientInfoTable.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String listRegisDocScheInfo() {
		// 获取当月第一天至当天时间段
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
		
	}
	
	/**  
	 * 住院病人动态报表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@SuppressWarnings("deprecation")
	@Action(value="queryInpatientInfoTable")
	public void queryInpatientInfoTable(){
		try {
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}else{
				startTime = startTime+" 00:00:00";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}else{
				endTime = endTime+" 23:59:59";
			}
			List<InpatientInfoTableVo> inpatientInfoTableList = inpatientInfoTableService.queryInpatientInfoTable(startTime,endTime,deptCode,menuAlias,page,rows);
			int total=inpatientInfoTableService.getTotalInpatientInfoTable(startTime,endTime,deptCode,menuAlias,page,rows);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", inpatientInfoTableList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("KSTJ_ZZBRDTBB", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_ZZBRDTBB", "科室统计_住院病人动态报表", "2", "0"), e);
		}
	}
	
}
