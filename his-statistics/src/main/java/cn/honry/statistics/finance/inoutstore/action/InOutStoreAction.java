package cn.honry.statistics.finance.inoutstore.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import cn.honry.statistics.finance.inoutstore.service.InOutStoreService;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.statistics.finance.inoutstore.vo.StoreSearchVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**  
 *  住院药房入出库台账查询Action
 * @Author:luyanshou
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/InOutStore")
@SuppressWarnings({"all"})
public class InOutStoreAction extends ActionSupport{
	
	private Logger logger=Logger.getLogger(InOutStoreAction.class);
	private InOutStoreService inOutStoreService;

	@Autowired
	@Qualifier(value="inOutStoreService")
	public void setInOutStoreService(InOutStoreService inOutStoreService) {
		this.inOutStoreService = inOutStoreService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;

	public void setHiasExceptionService(
			HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private String q;//自动补全参数
	
	public String getQ() {
		if(q==null){
			q="";
		}
		return q.trim();
	}
	public void setQ(String q) {
		this.q = q;
	}
	
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private StoreSearchVo t1;
	
	public StoreSearchVo getT1() {
		return t1;
	}
	public void setT1(StoreSearchVo t1) {
		this.t1 = t1;
	}
	
	private int page;//页码
	private int rows;//每页记录数

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String sTime;
	public String eTime;
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
	@RequiresPermissions(value={"ZYYFTZ:function:view"})
	@Action(value = "inOutStoreView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/drug/inoutstore/inoutStore.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inOutStoreView(){
		Date date = new Date();
		
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateYM(date)+"-01";   
		ServletActionContext.getRequest().setAttribute("sTime", sTime);
		ServletActionContext.getRequest().setAttribute("eTime", eTime);
		return "list";
	}
	
	/**
	 * 药品名称下拉列表数据
	 */
	@Action(value="getDrugInfo",results = { @Result(name = "json", type = "json") })
	public void getDrugInfo(){
			try {
				List<StoreResultVO> list = inOutStoreService.getdrugInfo(q);
				if(list!=null && list.size()>0){
					String json = JSONUtils.toJson(list);
					WebUtils.webSendJSON(json);
				}
			} catch (Exception ex) {
				logger.error("ZYGL_ZYYFRCKTZCX", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYFRCKTZCX", "住院管理_住院药房入出库台账查询", "2", "0"), ex);
			}
	}
	
	/**
	 * 入出库记录数据
	 */
	@Action(value="inOutStoreData",results = { @Result(name = "json", type = "json") })
	public void inOutStoreData(){
			try {
				if(t1==null){
					t1=new StoreSearchVo();
				}
				if(t1.getType()==null){
					t1.setType(0);
				}
				Date date = new Date();
				eTime = DateUtils.formatDateY_M_D(date);
				sTime = DateUtils.formatDateYM(date)+"-01";  
				if(StringUtils.isBlank(t1.getBeginTime())){
					t1.setBeginTime(sTime);

				}
				if(StringUtils.isBlank(t1.getEndTime())){
					t1.setEndTime(eTime);
					
				}
				int firstResult=(page-1)*rows;                                                              
				Map map = inOutStoreService.getStoreData(t1,firstResult,page,rows);
				if(map!=null && map.size()>0){
					String json = JSONUtils.toJson(map);
					WebUtils.webSendJSON(json);
				}
			} catch (Exception ex) {
				logger.error("ZYGL_ZYYFRCKTZCX", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYFRCKTZCX", "住院管理_住院药房入出库台账查询", "2", "0"), ex);
			}
	}
}
