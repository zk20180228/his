package cn.honry.statistics.finance.department.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.finance.department.service.DeptIncomeService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/**
* @ClassName: 科室收入统计
* @author yuke
* @date 2017年7月3日
*
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/deptIncome")
public class DeptIncomeAction {
	private String sTime;
	private String eTime;
	private String date;
	private String dateSign;
	private String deptCodes;
	private String menuAlias;
	@Resource(name = "deptIncomeService")
	private DeptIncomeService deptIncomeService;
	
	private Logger logger=Logger.getLogger(DeptIncomeAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	@Resource(name = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateSign() {
		return dateSign;
	}

	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}

	public String getDeptCodes() {
		return deptCodes;
	}

	public void setDeptCodes(String deptCodes) {
		this.deptCodes = deptCodes;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public DeptIncomeService getDeptIncomeService() {
		return deptIncomeService;
	}

	public void setDeptIncomeService(DeptIncomeService deptIncomeService) {
		this.deptIncomeService = deptIncomeService;
	}

	/**
	 * 科室收入统计 跳转jsp页面
	 */
	@Action(value = "listDeptIncome", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/reportForms/deptIncomeStatistics.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDeptIncome() {
		Date date = new Date();
		this.sTime = DateUtils.formatDateY_M_D(date);
		this.eTime = DateUtils.formatDateY_M_D(DateUtils.addDay(date, -7));
		return "list";
	}
	
	/**
	 * @Description：  科室收入统计  elasticsearch实现
	 * @Author：朱振坤
	 * @param  date 日期 dateSign为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"，为4时格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，
	 * @param  dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
	 *
	 */
	@Action(value="deptIncomeCharts",results={@Result(name="json",type="json")})
	public void deptIncomeCharts(){
		try {
			Map<String,Object> map = new HashMap<>();
			if (!"1".equals(dateSign) && !"2".equals(dateSign) && !"3".equals(dateSign) && !"4".equals(dateSign)) {
				String json=JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
				return;
			}
            //该用户拥有的权限，能查看的科室
			List<String> permitCodes = new ArrayList<>();
            //过滤掉前台传来没有权限的科室
			String filterCodes = "";
			List<SysDepartment> dept=deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
            for (SysDepartment aDept : dept) {
                permitCodes.add(aDept.getDeptCode());
            }
            //如果科室为空，则获取当前登录用户能查看的科室
			if (StringUtils.isBlank(deptCodes)) {
				for(String permitCode : permitCodes){
                    filterCodes += permitCode + ",";
				}
			} else {
                //如果科室不为空，则校验前台传来的科室是否是当前登录用户能查看的科室
				String[] deptCodeArray = deptCodes.split(",");
                for (String code : deptCodeArray) {
                    for (String permitCode : permitCodes) {
                        if (code.equals(permitCode)) {
                            filterCodes += code + ",";
                        }
                    }
                }
			}
            if (filterCodes.endsWith(",")) {
                filterCodes = filterCodes.substring(0, filterCodes.length() - 1);
			}
			map.putAll(deptIncomeService.deptIncomeChartsByES(date, dateSign, filterCodes));
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("KSTJ_KSSRTJ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_KSSRTJ", "科室统计_科室收入统计", "2", "0"), e);
		}
	}
}
