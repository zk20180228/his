package cn.honry.statistics.drug.integratedQuery.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.drug.integratedQuery.service.InteQueryService;
import cn.honry.statistics.drug.integratedQuery.vo.InteQueryVO;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  药房药库综合查询Action
 * @Author:luyanshou
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/InteQuery")
@SuppressWarnings({"all"})
public class InteQueryAction extends ActionSupport{
	private Logger logger=Logger.getLogger(InteQueryAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private InteQueryService inteQueryService;

	@Autowired
	@Qualifier(value="inteQueryService")
	public void setInteQueryService(InteQueryService inteQueryService) {
		this.inteQueryService = inteQueryService;
	}
	/**
	 * 编码查询接口Service
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	
	private int kind;//汇总类型(0-按药品;1-按单据;2-按部门)
	private int type;//查询类别(0-入库;1-出库;2-盘点;3-调价)
	
	
	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	
	private InteQueryVO t;//封装的查询条件

	public InteQueryVO getT() {
		return t;
	}

	public void setT(InteQueryVO t) {
		this.t = t;
	}
	
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
    
	
	public CodeInInterService getInnerCodeService() {
		return innerCodeService;
	}

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	@RequiresPermissions(value={"YFYKZHCX:function:view"})
	@Action(value = "inteQueryView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/integratedQuery/integratedQuery.jsp") },
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inteQueryView(){
		return "list";
	}
	
	@Action(value="getDrugTree")
	public void getDrugTree(){//获取药房药库信息
			try {
				List<TreeJson> list = inteQueryService.getDrugStore();
				String json = JSONUtils.toJson(list);
				WebUtils.webSendJSON(json);
			} catch (Exception ex) {
				logger.error("ZYTJ_YFYKZHCX", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YFYKZHCX", "住院统计_药房药库综合查询", "2", "0"), ex);
			}
	}
	
	/**
	 * 药房药库综合信息查询
	 */
	@Action(value="getInteData")
	public void getInteData(){
		try {
			int firstResult=(page-1)*rows;
			if(t==null){
				t=new InteQueryVO();
				t.setEndTime(new Date());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		        String beginTime;  
		        // 获取前月的第一天  
		        Calendar  cale = Calendar.getInstance();  
		        cale.add(Calendar.MONTH, 0);  
		        cale.set(Calendar.DAY_OF_MONTH, 1);  
		        beginTime = format.format(cale.getTime());
		        Date start = format.parse(beginTime);
				t.setBeginTime(start);
			}
			Map map = inteQueryService.getListByPage(t, kind, type, firstResult, rows);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_YFYKZHCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YFYKZHCX", "住院统计_药房药库综合查询", "2", "0"), ex);
		}
	}
	
	/**
	 * 获取表头信息
	 */
	@Action(value="getColumns")
	public void getColumns(){
		try {
			String columns="[";
			String[] fieldNames = inteQueryService.getfields(kind, type);
			Map<String,String> map = inteQueryService.getName(kind, type);
			List<Map<String,String>> list=new ArrayList();
			for (String fieldName : fieldNames) {
				Map<String,String> m= new HashMap();
				String titleName = map.get(fieldName);
				m.put("fieldName", fieldName);
				m.put("titleName", titleName);
				list.add(m);
			}
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_YFYKZHCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YFYKZHCX", "住院统计_药房药库综合查询", "2", "0"), ex);
		}
	}
	
	/**
	 * 获取公司id和名称
	 */
	@Action(value="getCompanyName")
	public void getCompanyName(){
		try {
			List<Map<String,String>> list = inteQueryService.getCompanyName();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_YFYKZHCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YFYKZHCX", "住院统计_药房药库综合查询", "2", "0"), ex);
		}
	}
	
	/**
	 * 获取科室id和名称
	 */
	@Action(value="getDeptIdName")
	public void getDeptIdName(){
		try {
			List<SysDepartment> list = inteQueryService.getDeptIdName();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception ex) {
			logger.error("ZYTJ_YFYKZHCX", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YFYKZHCX", "住院统计_药房药库综合查询", "2", "0"), ex);
		}
	}
	
	/**
	 * 查询包装单位信息
	 */
	@Action(value="getPackUnit")
	public void getPackUnit(){
		List<BusinessDictionary> businessDictionaryList = innerCodeService.getDictionary("packunit");
		String json = JSONUtils.toJson(businessDictionaryList);
		WebUtils.webSendJSON(json);
	}
}
