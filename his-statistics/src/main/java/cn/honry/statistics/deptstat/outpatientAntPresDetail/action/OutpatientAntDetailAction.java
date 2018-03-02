package cn.honry.statistics.deptstat.outpatientAntPresDetail.action;

import java.util.Date;
import java.util.Map;

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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.outpatientAntPresDetail.service.OutpatientAntService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 
 * 
 * <p>门诊抗菌药物处方比例 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月6日 下午5:09:31 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月6日 下午5:09:31 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/outpatientAnt")
public class OutpatientAntDetailAction {
	private String deptCodes;//科室code
	private String searchBegin;//开始时间
	private String searchEnd;//结束时间
	private String menuAlias;
	private String rows;
	private String page;
	private Logger logger=Logger.getLogger(OutpatientAntDetailAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value="outpatientAntService")
	private OutpatientAntService outpatientAntService;
	
	public void setOutpatientAntService(OutpatientAntService outpatientAntService) {
		this.outpatientAntService = outpatientAntService;
	}
	
	public String getDeptCodes() {
		return deptCodes;
	}

	public void setDeptCodes(String deptCodes) {
		this.deptCodes = deptCodes;
	}

	public String getSearchBegin() {
		return searchBegin;
	}

	public void setSearchBegin(String searchBegin) {
		this.searchBegin = searchBegin;
	}

	public String getSearchEnd() {
		return searchEnd;
	}

	public void setSearchEnd(String searchEnd) {
		this.searchEnd = searchEnd;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * 
	 * 
	 * <p>跳转到门诊抗菌药物处方比例 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月7日 下午6:01:50 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月7日 下午6:01:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@Action(value = "listAntOut", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/outpatientAntPresDetail/outpatientAntPresDetailList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listAntOut(){
		Date date=new Date();
		searchBegin=DateUtils.formatDateY_M(date)+"-01";
		searchEnd=DateUtils.formatDateY_M_D(date);
		return "list";
	}
	/**
	 * 
	 * 
	 * <p>查询处方比 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月7日 下午6:02:22 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月7日 下午6:02:22 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryList")
	public void queryList(){
		try {
			Map<String,Object> map=outpatientAntService.queryList(searchBegin, searchEnd, deptCodes, menuAlias,rows,page);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("KSTJ_MZKETJ_MZKJYWCFBL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_MZKETJ_MZKJYWCFBL", "科室统计_门诊科室统计_门诊抗菌药物处方比例", "2", "0"), e);
		}
	}
	
	@Action(value = "printList")
	public void printList(){
		try {
//			List<OutpatientAntVo> list=outpatientAntService.queryList(searchBegin, searchEnd, deptCodes, menuAlias);
			
		} catch (Exception e) {
			logger.error("KSTJ_MZKETJ_MZKJYWCFBL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_MZKETJ_MZKJYWCFBL", "科室统计_门诊科室统计_门诊抗菌药物处方比例", "2", "0"), e);
		}
	}
	@Action(value = "exporList")
	public void exporList(){
		try {
//			List<OutpatientAntVo> list=outpatientAntService.queryList(searchBegin, searchEnd, deptCodes, menuAlias);
			
		} catch (Exception e) {
			logger.error("KSTJ_MZKETJ_MZKJYWCFBL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_MZKETJ_MZKJYWCFBL", "科室统计_门诊科室统计_门诊抗菌药物处方比例", "2", "0"), e);
		}
	}
}
