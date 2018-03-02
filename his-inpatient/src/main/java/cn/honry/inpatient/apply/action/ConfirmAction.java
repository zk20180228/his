package cn.honry.inpatient.apply.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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
import cn.honry.inpatient.apply.Service.ConfirmService;
import cn.honry.inpatient.apply.Service.DrugApplyService;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 退费确认action
 * @author  lyy
 * @createDate： 2016年1月29日 下午2:02:02 
 * @modifier lyy
 * @modifyDate：2016年1月29日 下午2:02:02  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/applycon")
public class ConfirmAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(ConfirmAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/***
	 * 注入本类service
	 */
	@Autowired
	@Qualifier("confirmService")
	private ConfirmService confirmService;
	public void setConfirmService(ConfirmService confirmService) {
		this.confirmService = confirmService;
	}
	
	
	@Autowired
	@Qualifier(value="drugApplyService")
	private DrugApplyService drugApplyService;
	private List<ApplyVo> applyVoList;
	private String cancelitemJson;  //json文件
	
	public String getCancelitemJson() {
		return cancelitemJson;
	}
	public void setCancelitemJson(String cancelitemJson) {
		this.cancelitemJson = cancelitemJson;
	}
	public List<ApplyVo> getApplyVoList() {
		return applyVoList;
	}
	public void setApplyVoList(List<ApplyVo> applyVoList) {
		this.applyVoList = applyVoList;
	}
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Action(value="listConfirm",results={@Result(name="list",location="/WEB-INF/pages/drug/apply/confirmList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public  String listConfirm(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledte = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String first=simpledte.format(calendar.getTime());
        String end = simpledateformat.format(calendar.getTime());
        ServletActionContext.getRequest().setAttribute("first", first);
		ServletActionContext.getRequest().setAttribute("end",end);
		return "list";
	}
	
	@RequiresPermissions(value={"QRTF:function:query"})
	@Action(value = "queryDrugConfirm", results = { @Result(name = "json", type = "json") })
	public void queryDrugConfirm() throws Exception{
		try {
			ApplyVo applySerch = new ApplyVo();//条件查询
			String inpatientNo=ServletActionContext.getRequest().getParameter("inpatientNo");
			String beginDate=ServletActionContext.getRequest().getParameter("beginDate");
			String endDate=ServletActionContext.getRequest().getParameter("endDate");
			String drugName=ServletActionContext.getRequest().getParameter("drugName");
			applySerch.setDrugName(drugName);
			applySerch.setInpatientNo(inpatientNo);
			if(StringUtils.isNotBlank(beginDate)){
				SimpleDateFormat simpleFormat = new SimpleDateFormat();
				String pattent="yyyy-MM-dd HH:mm:ss";
				simpleFormat.applyPattern(pattent);
				
			}
			if(StringUtils.isNotBlank(endDate)){
				SimpleDateFormat simpleFormat = new SimpleDateFormat();
				String pattent="yyyy-MM-dd HH:mm:ss";
				simpleFormat.applyPattern(pattent);
			}
			int total = confirmService.getTatalDrugConfirm(applySerch);
			applyVoList = confirmService.getPageDrugConfirm(applySerch,ServletActionContext.getRequest().getParameter("page"),ServletActionContext.getRequest().getParameter("rows"));
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", applyVoList);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_QRTF", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QRTF", "住院收费_确认退费", "2", "0"), ex);
		}
	}
	@RequiresPermissions(value={"QRTF:function:query"})
	@Action(value = "queryNotDrugConfirm", results = { @Result(name = "json", type = "json") })
	public void queryNotDrugConfirm() throws Exception{
		try {
			ApplyVo applySerch = new ApplyVo();//条件查询
			String inpatientNo=ServletActionContext.getRequest().getParameter("inpatientNo");
			String beginDate=ServletActionContext.getRequest().getParameter("beginDate");
			String endDate=ServletActionContext.getRequest().getParameter("endDate");
			String drugName=ServletActionContext.getRequest().getParameter("drugName");
			applySerch.setInpatientNo(inpatientNo);
			applySerch.setObjName(drugName);
			if(StringUtils.isNotBlank(beginDate)){
				SimpleDateFormat simpleFormat = new SimpleDateFormat();
				String pattent="yyyy-MM-dd HH:mm:ss";
				simpleFormat.applyPattern(pattent);
			}
			if(StringUtils.isNotBlank(endDate)){
				SimpleDateFormat simpleFormat = new SimpleDateFormat();
				String pattent="yyyy-MM-dd HH:mm:ss";
				simpleFormat.applyPattern(pattent);
			}
			int total = confirmService.getTatalNotDrugConfirm(applySerch);
			applyVoList = confirmService.getPageNotDrugConfirm(applySerch,ServletActionContext.getRequest().getParameter("page"),ServletActionContext.getRequest().getParameter("rows"));
		
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", applyVoList);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_QRTF", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QRTF", "住院收费_确认退费", "2", "0"), ex);
			System.out.println(ex.getMessage());
		}
	}
	@Action(value = "confirmBack", results = { @Result(name = "update", location = "/WEB-INF/pages/drug/apply/confirmList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String confirmBack() throws Exception {
		String ids=ServletActionContext.getRequest().getParameter("id");
		try{
			String result = confirmService.confirmBack(ids);
			if("ok".equals(result)){
				WebUtils.webSendString("success");
			}else{
				WebUtils.webSendString("error");
			}
		}catch(Exception e){
			logger.error("ZYSF_QRTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QRTF", "住院收费_确认退费", "2", "0"), e);
			WebUtils.webSendString("error");
		}
		return "update";
	}
	
/****************************************************************   分割线       *************************************************************************************/
	
	//要退费的id集合
	private String ids[];
	
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
	
	/***
	 * 退费操作--计算金额
	 * @Title: applyState 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "applyState")
	public void applyState() {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map = confirmService.applyState(ids);
			if("0".equals(map.get("resCode"))){
				Double sum = (Double) map.get("resMes");
				map.put("resMes", String.format("%.2f", sum));
			}
			
			String joString = JSONUtils.toJson(map);
			WebUtils.webSendJSON(joString);
		}catch(Exception ex){
			logger.error("ZYSF_QRTF", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QRTF", "住院收费_确认退费", "2", "0"), ex);
		}
	}
	
	/**
	 * 退费操作
	 * @Title: confirm 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "applyConfirm")
	public void applyConfirm(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resCode", "success");
		
		try {
			confirmService.applyConfirm(ids,null);
		} catch (Exception e) {
			map.put("resCode", "error");
			logger.error("ZYSF_QRTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QRTF", "住院收费_确认退费", "2", "0"), e);
			e.printStackTrace();
		}
		String joString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(joString);
	}
	
	
}
