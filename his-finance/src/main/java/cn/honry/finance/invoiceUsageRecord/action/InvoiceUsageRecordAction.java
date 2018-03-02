package cn.honry.finance.invoiceUsageRecord.action;

import java.io.PrintWriter;
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

import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.User;
import cn.honry.finance.invoiceUsageRecord.service.InvoiceUsageRecordService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * <p>发票使用记录维护 </p>
 * @Author: xueconglin
 * @CreateDate: 2017年7月4日 上午9:42:55 
 * @Modifier: xueconglin
 * @ModifyDate: 2017年7月4日 上午9:42:55 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
//@Namespace(value = "/finance")
@Namespace(value = "/finance/invoiceUsageRecord")
@SuppressWarnings({"all"})
public class InvoiceUsageRecordAction extends ActionSupport{
	private Logger logger=Logger.getLogger(InvoiceUsageRecordAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Autowired
	@Qualifier(value = "invoiceUsageRecordService")
	private InvoiceUsageRecordService invoiceUsageRecordService;
	
	public void setInvoiceUsageRecordService(
			InvoiceUsageRecordService invoiceUsageRecordService) {
		this.invoiceUsageRecordService = invoiceUsageRecordService;
	}
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * 分页所用的page、rows
	 */
	private String page;
	private String rows;
	/**
	 * 账务组或员工编号
	 */
	private String code;
	/**
	 * 领取人类型
	 */
	private String type;
	/**
	 * 发票号
	 */
	private String num;
	/**
	 * 进行召回操作时,所选择的行的Json字符串
	 */
	private String rowss;
	
	
	public String getRowss() {
		return rowss;
	}

	public void setRowss(String rowss) {
		this.rowss = rowss;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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
	 *   发票使用记录维护
	 * @Author：tcj
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"FPSYWH:function:view"})
	@Action(value="invoiceUsageRecordList",results={@Result(name="list",location = "/WEB-INF/pages/finance/invoiceUsageRecord/invoiceUsageRecordList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String invoiceUsageRecordList()throws Exception{
		return "list";
	}
	/**
	 * 查询发票使用记录
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDatagrid")
	public void queryDatagrid(){
		try {
		List<InvoiceUsageRecord> iurl =invoiceUsageRecordService.queryDatagrid(page,rows,code,type,num);
		int total=invoiceUsageRecordService.getTotal(code,type,num);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd hh:mm:ss").create();
		String json =gson.toJson(iurl);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("{\"total\":" + total + ",\"rows\":" + json + "}");
			out.close();
		}
		catch (Exception ex) {
			logger.error("CWGL_FPSYWH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPSYWH", "发票管理_发票使用维护", "2", "0"), ex);
		}
	}
	/**
	 * 将选中的未使用发票召回
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "saveData")
	public void saveData(){
		String result;
		try {
			result = invoiceUsageRecordService.saveData(rowss);
			Map<String,String> map=new HashMap<String,String>();
			if("success".equals(result)){
				map.put("key", "success");
				map.put("value", "召回成功");
			}else if("used".equals(result)){
				map.put("key", "used");
				map.put("value", "请选择未使用的发票进行召回");
			}else if("zhaohui".equals(result)){
				map.put("key", "zhaohui");
				map.put("value", "发票已经召回不能重复操作");
			}else if("null".equals(result)){
				map.put("key", "null");
				map.put("value", "发票无使用状态不能进行召回操作");
			}else{
				map.put("key", "error");
				map.put("value", "召回失败");
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPSYWH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPSYWH", "发票管理_发票使用维护", "2", "0"), e);
		}
	}
	/**
	 * 查询用户Map
	 * @author tuchuanjiang
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryUserRecord")
	public void queryUserRecord(){
		List<User> ul;
		try {
			ul = invoiceUsageRecordService.queryUserRecord();
			Map<String,String> map=new HashMap<String,String>();
			for(User user:ul){
				map.put(user.getAccount(), user.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("CWGL_FPSYWH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CWGL_FPSYWH", "发票管理_发票使用维护", "2", "0"), e);
		}
	}
}
