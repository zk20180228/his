package cn.honry.statistics.emr.emrStat.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.emr.emrStat.service.EmrStatService;
import cn.honry.statistics.emr.emrStat.vo.EmrBaseVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**  
 * 
 * <p>电子病历统计分析Action</p>
 * @Author: dutianliang
 * @CreateDate: 2017年11月13日 下午2:34:45 
 * @Modifier: dutianliang
 * @ModifyDate: 2017年11月13日 下午2:34:45 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/emr/emrStat")
public class EmrStatAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(EmrStatAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 注入电子病历统计Service
	 */
	@Autowired 
	@Qualifier(value = "emrStatService")
	private EmrStatService emrStatService;
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	/**
	 * 开始时间
	 */
	private String beginTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	/**  
	 * 
	 * @Fields type  TODO(用一句话描述这个变量表示什么) 
	 *
	 */
	private String type;
	
	private String page;
	private String rows;
	
	
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void setEmrStatService(EmrStatService emrStatService) {
		this.emrStatService = emrStatService;
	}
	/**  
	 * 
	 * <p>电子病历统计页面</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年11月13日 下午4:49:09 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年11月13日 下午4:49:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@RequiresPermissions(value={"EMR-DZBLTJ:function:view"}) 
	@Action(value = "listEmrStat", results = { @Result(name = "list", location = "/WEB-INF/pages/emrs/stat/emrStatList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listEmrStat() {
		beginTime = "2017/01/01";
		endTime = "2017/05/01";
		return "list";
	}
	/**  
	 * 
	 * <p>查询数据</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年11月13日 下午4:49:47 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年11月13日 下午4:49:47 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryValues")
	public void queryValues() {
		try {
			String json = emrStatService.getJson(beginTime, endTime);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("EMR_DZBLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("EMR_DZBLTJ", "电子病历_电子病历统计", "2", "0"), e);
		}
	}
	
	
	@Action(value = "queryList")
	public void queryList() {
		List<EmrBaseVo> list = new ArrayList<EmrBaseVo>();
		Map<String, Object> map = new HashMap<String, Object>();
		Integer total = 0;
		try {
			total = emrStatService.getCount(beginTime, endTime, Integer.valueOf(type));
			list= emrStatService.getList(beginTime, endTime, Integer.valueOf(type), page, rows);
			map.put("rows", list);
			map.put("total", total);
		} catch (Exception e) {
			list = new ArrayList<EmrBaseVo>();
			map.put("rows", list);
			map.put("total", total);
			logger.error("EMR_DZBLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("EMR_DZBLTJ", "电子病历_电子病历统计", "2", "0"), e);
		}finally{
			String json = JSONUtils.toJson(map, "yyyy/MM/dd");
			WebUtils.webSendJSON(json);
		}
	}
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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

}
