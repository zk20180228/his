package cn.honry.inpatient.variationInfo.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.CpVariation;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.variationInfo.service.VariationInfoService;
import cn.honry.inpatient.variationInfo.vo.ComboxVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/variationInfo")
public class VariationInfoAction extends ActionSupport {
	private Logger logger=Logger.getLogger(VariationInfoAction.class);
	private static final long serialVersionUID = 1L;
	private String page;
	private String rows;
	@Autowired
	@Qualifier(value = "variationInfoService")
	private VariationInfoService variationInfoService;
	public void setVariationInfoService(VariationInfoService variationInfoService) {
		this.variationInfoService = variationInfoService;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private CpVariation cpVariation = new CpVariation();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	public CpVariation getCpVariation() {
		return cpVariation;
	}

	public void setCpVariation(CpVariation cpVariation) {
		this.cpVariation = cpVariation;
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
	
	@Action(value="queryList",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/variationInfo/variationInfo.jsp") })
	public String queryList(){
		return "list";
	}
	@Action(value = "addVariationInfo", results = { @Result(name = "listBedUrl", location = "/WEB-INF/pages/inpatient/variationInfo/variationInfoAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addVariationInfo() {
		String id = request.getParameter("id");
		
		if(StringUtils.isNotBlank(id)){
			cpVariation = variationInfoService.findVariationInfoById(id);
		}else{
			String inpatientNo = request.getParameter("inpatientNo");
			PathApply pathApply = variationInfoService.getPathApplyByInNo(inpatientNo);
			cpVariation.setInpatientNo(pathApply.getInpatientNo());
			cpVariation.setMedicalrecordId(pathApply.getMedicalrecordId());
			request.setAttribute("cpId", pathApply.getCpId());
			request.setAttribute("versionNo", pathApply.getVersionNo());
		}
		return "listBedUrl";
	}
	@Action(value = "nurseStation")
	public void nurseStation() {
		try{
			String id = ServletActionContext.getRequest().getParameter("id");//需要显示科室树的条件
			List<TreeJson> ward =  variationInfoService.listTree(id);
			String json=JSONUtils.toJson(ward);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("LCLJ_BYXXJL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("LCLJ_BYXXJL", "临床路径_变异信息记录", "2", "0"), e);
		}
	}
	@Action(value="queryPathVsIcdList", results = {@Result(name = "json", type = "json") })
	public void queryPathVsIcdList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String inpatientNo = request.getParameter("inpatientNo");
			if(StringUtils.isNotBlank(inpatientNo)){
				Integer total = variationInfoService.queryPathVsIcdNum(inpatientNo);
				List<CpVariation> list = variationInfoService.queryPathVsIcdList(inpatientNo,page,rows);
				map.put("total", total);
				map.put("rows", list);
			}else{
				map.put("total", 0);
				map.put("rows", new ArrayList<CpVariation>());
			}
			
		}catch(Exception e){
			e.printStackTrace();
			map.put("total", 0);
			map.put("rows", new ArrayList<CpVariation>());
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryDictionary")
	public void queryDictionary(){	
		String q = request.getParameter("q");
		String type = request.getParameter("type");
		List<ComboxVo> dictionaryList = variationInfoService.queryDictionary(q,type);
		String json = JSONUtils.toJson(dictionaryList);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryStageId")
	public void queryStageId(){	
		String q = request.getParameter("q");
		String cpId = request.getParameter("cpId");
		String versionNo = request.getParameter("versionNo");
		List<ComboxVo> dictionaryList = variationInfoService.queryStageId(q,cpId,versionNo);
		String json = JSONUtils.toJson(dictionaryList);
		WebUtils.webSendJSON(json);
	}
	@Action(value="saveOrUpdate", results = { @Result(name = "json", type = "json") })
	public void saveOrUpdate(){
		String result = "true";
		try{
			cpVariation.setStageId("1");
			variationInfoService.saveOrUpdate(cpVariation);
		}catch(Exception e){
			e.printStackTrace();
			result = "false";
		}
		WebUtils.webSendString(result);
	}
	@Action(value = "deleteCpVariation",results = { @Result(name = "json", type = "json") })
	public void deleteCpVariation(){
		try{
			String[] ids=request.getParameterValues("ids");
			//判断要删除的床位中是否存在占用床位
			for(String id:ids){
				variationInfoService.batchUpDe(id);
			}
			String json = JSONUtils.toJson("删除成功！");
			WebUtils.webSendString(json);
		}catch(Exception e){
			logger.error("ZYGL_QYBCGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_QYBCGL", "住院管理_全院病床管理", "2", "0"), e);
		}
	}
	@Action(value = "queryDeptLsit")
	public void queryDeptLsit() throws UnsupportedEncodingException {
	    try {
	    	String queryName = request.getParameter("queryName");
			List<SysDepartment> treeDeptLsit=variationInfoService.getDeptName(queryName);
			WebUtils.webSendJSON(JSONUtils.toJson(treeDeptLsit, "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			logger.error("ZYGL_QYBCGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_QYBCGL", "住院管理_全院病床管理", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
}
