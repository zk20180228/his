package cn.honry.inpatient.apply.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.inpatient.apply.Service.DrugApplyService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 查询患者退费功能
 * @author  donghe
 * @createDate： 2017年4月12日14:19:17 
 * @modifier donghe
 * @modifyDate：2016年4月12日14:19:17
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/cancelitem")
public class InpatientCancelitemAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	//病历号
    private String medicalrecordId; 
    //住院流水号
    private String inpatientNo;
    // 分页
 	private String page;//起始页数
 	private String rows;//数据列数
    
 	
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
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	@Autowired
	@Qualifier(value="drugApplyService")
	private DrugApplyService drugApplyService;
	public void setDrugApplyService(DrugApplyService drugApplyService) {
		this.drugApplyService = drugApplyService;
	}
	@RequiresPermissions(value={"TFCX:function:view"})
	@Action(value="listCancelitem",results={@Result(name="list",location="/WEB-INF/pages/drug/apply/queryCancelitemList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public  String listCancelitem(){
		return "list";
	}
	
	/**
	 * 根据病历号查询病人信息
	 * @author  qh
	 * @createDate： 2017年4月12日 下午7:20:06 
	 */
	@Action(value="queryInpatientByMedicalRecordId")
	public void queryInpatientByMedicalRecordId(){
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isBlank(page)){
			page="1";
		}if(StringUtils.isBlank(rows)){
			rows="20";
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			medicalrecordId=medicalrecordId.trim();
			List<InpatientInfoNow> list = drugApplyService.queryInpatientByMedicalRecordId(medicalrecordId,page,rows);
			int total=drugApplyService.queryTotal(medicalrecordId);
			map.put("total", total);
			map.put("rows", list);
		}else{
			List<InpatientInfoNow> list  = new ArrayList<InpatientInfoNow>();
			map.put("total", 0);
			map.put("rows", list);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendString(json);
	}
	
	/**
	 * 查询病人退费信息
	 * @author  qh
	 * @createDate： 2017年4月13日 上午10:29:06 
	 */
	@Action(value="queryInpatientReturns")
	public void queryInpatientReturns(){
		try {
		if(StringUtils.isBlank(page)){
			page="1";
		}if(StringUtils.isBlank(rows)){
			rows="20";
		}
		List<InpatientCancelitemNow> list;
			list = drugApplyService.queryInpatientReturns(inpatientNo,page,rows);
		int total=drugApplyService.queryTotalBy(inpatientNo);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendString(json);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
