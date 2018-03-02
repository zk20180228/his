package cn.honry.inpatient.clinicalPathVsICD.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.bean.model.PathVsIcd;
import cn.honry.inner.baseinfo.icd.service.IcdInnerService;
import cn.honry.inpatient.clinicalPathVsICD.service.ClinicalPathVsICDService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/clinicalPathVsICD")
public class ClinicalPathVsICDAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String page;
	private String rows;
	@Autowired
	@Qualifier(value = "clinicalPathVsICDService")
	private ClinicalPathVsICDService clinicalPathVsICDService;
	public void setClinicalPathVsICDService(ClinicalPathVsICDService clinicalPathVsICDService) {
		this.clinicalPathVsICDService = clinicalPathVsICDService;
	}
	@Autowired
	@Qualifier(value = "icdInnerService")
	private IcdInnerService icdInnerService;

	public void setIcdInnerService(IcdInnerService icdInnerService) {
		this.icdInnerService = icdInnerService;
	}
	private PathVsIcd pathVsIcd = new PathVsIcd();
	
	public PathVsIcd getPathVsIcd() {
		return pathVsIcd;
	}

	public void setPathVsIcd(PathVsIcd pathVsIcd) {
		this.pathVsIcd = pathVsIcd;
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

	@Action(value="queryList",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/clinicalPathVsICD/clinicalPathVsICDList.jsp") })
	public String queryList(){
		return "list";
	}
	@Action(value = "addClinicalPathVsICD", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/clinicalPathVsICD/clinicalPathVsICDAdd.jsp") })
	public String addClinicalPathVsICD() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			pathVsIcd = clinicalPathVsICDService.findPathwayVsICDById(id);
		}else{
			String modelId = request.getParameter("modelId");
			pathVsIcd.setCpId(modelId);
		}
		return "list";
	}
	@Action(value="queryClinicalPathVsICD", results = {@Result(name = "json", type = "json") })
	public void queryClinicalPathVsICD(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String keyWord = request.getParameter("keyWord");
			String modelId = request.getParameter("modelId");
			if(StringUtils.isNotBlank(modelId)){
				Integer total = clinicalPathVsICDService.queryClinicalPathVsICDNum(keyWord,modelId);
				List<PathVsIcd> list = clinicalPathVsICDService.queryClinicalPathVsICD(keyWord,modelId,page,rows);
				map.put("total", total);
				map.put("rows", list);
			}else{
				map.put("total", 0);
				map.put("rows", new ArrayList<PathVsIcd>());
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("total", 0);
			map.put("rows", new ArrayList<ModelVsItem>());
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value="saveOrUpdate", results = { @Result(name = "json", type = "json") })
	public void saveOrUpdate(){
		String result = "true";
		try{
			clinicalPathVsICDService.saveOrUpdate(pathVsIcd);
		}catch(Exception e){
			e.printStackTrace();
			result = "false";
		}
		WebUtils.webSendString(result);
	}
	@Action(value="queryICDDictionary")
	public void queryICDDictionary(){	
		HttpServletRequest request = ServletActionContext.getRequest();
		String q = request.getParameter("q");
		List<BusinessIcd10> dictionaryList = icdInnerService.getIcd10ByQ(q);
		String json = JSONUtils.toJson(dictionaryList);
		WebUtils.webSendJSON(json);
	}
}
