package cn.honry.inpatient.inpatientNumber.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientNumber;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.inpatientNumber.service.InpatientNumberService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/inpatient/InpatientNumber")
public class InpatientNumberAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private String medicalrecordId;
	private String beganTime;
	private String endTime;
	private String page;
	private String rows;
	
	@Autowired
	@Qualifier(value="inpatientNumberService")
	private InpatientNumberService inpatientNumberService;
	
	public InpatientNumberService getInpatientNumberService() {
		return inpatientNumberService;
	}
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public PatinentInnerService getPatinentInnerService() {
		return patinentInnerService;
	}
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	public void setInpatientNumberService(
			InpatientNumberService inpatientNumberService) {
		this.inpatientNumberService = inpatientNumberService;
	}
	
	public String getMedicalrecordId() {
		return medicalrecordId;
	}

	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}

	public String getBeganTime() {
		return beganTime;
	}

	public void setBeganTime(String beganTime) {
		this.beganTime = beganTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	@Action(value="numberList",results={ @Result(name="list", location = "/WEB-INF/pages/inpatient/inpatientNumber/inpatientNumber.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String numberList(){
		
		return "list";
	}
	
	/**  
	 * @Description：  初始化
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0getNumberList
	 */
	@Action(value = "getInpatientNumber", results = { @Result(name = "json", type = "json") })
	public void getInpatientNumber(){
		String bolean=inpatientNumberService.getInpatientNumber();
		WebUtils.webSendString(bolean);
	}
	/**  
	 * @Description：  加载数据
	 * @Author：zhangjin
	 * @CreateDate：2016-11-14
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0getNumberList
	 */
	@Action(value="getNumberList",results={ @Result( name="json",type="json") } )
	public void getNumberList(){
		//通过接口查询就诊卡号对应的病历号
		String mid = "";
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(medicalrecordId)){
			medicalrecordId=medicalrecordId.trim();
			mid = medicalrecordId;
			if(StringUtils.isBlank(mid)){
				mid = "wuzhi";
			}
		}
		List<InpatientNumber> list=inpatientNumberService.getNumberList(mid,beganTime,endTime,page,rows);
		int total=inpatientNumberService.getNumberTotal(mid,beganTime,endTime,page,rows);
		map.put("rows",list);
		map.put("total", total);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * @Description：获取床号
	 * @Author：zhangjin
	 * @CreateDate：2016-11-18
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Action(value="getBedinfoId" )
	public void getBedinfoId(){
		List<BusinessHospitalbed> list=inpatientNumberService.getBedinfoId();
		Map<String,Object> map = new HashMap<String,Object>();
		if(list!=null&&list.size()>0){
			for(BusinessHospitalbed li:list){
				map.put(li.getId(), li.getBedName());
			}
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
}
