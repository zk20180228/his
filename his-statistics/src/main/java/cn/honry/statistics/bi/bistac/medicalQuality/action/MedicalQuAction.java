package cn.honry.statistics.bi.bistac.medicalQuality.action;

import java.util.Date;

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
import cn.honry.statistics.deptstat.journal.action.JournalAction;
import cn.honry.utils.DateUtils;
/**
 * 
 * 
 * <p>医疗质量运行监管指标统计 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月15日 上午10:28:09 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月15日 上午10:28:09 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/dept/medicalQuality")
public class MedicalQuAction {
	private Logger logger=Logger.getLogger(JournalAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private String menuAlias;
	private String time;
	private String begin;
	private String end;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

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

	/**
	 * 
	 * 
	 * <p>跳转到科室监督统计页面 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月14日 下午5:24:00 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月14日 下午5:24:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"YLZLYXJGZBTJ:function:view"}) 
	@Action(value = "showView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/medicalQuality.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showView() {
		try {
			Date date = new Date();
			begin = DateUtils.formatDateY_M(date)+"-01";
			end=DateUtils.formatDateY_M_D(date);
		} catch (Exception e) {	
			logger.error("YYFX_YLZLYXJGZBTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_YLZLYXJGZBTJ", "运营分析_医疗质量运行监管指标统计", "2", "0"), e);
		}
		return "list";
	}
}
