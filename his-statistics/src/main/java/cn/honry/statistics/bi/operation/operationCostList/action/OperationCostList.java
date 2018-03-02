package cn.honry.statistics.bi.operation.operationCostList.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.bi.operation.operationCostList.service.OperationCostListService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={ @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/operationCost")
@SuppressWarnings({ "all" })
public class OperationCostList extends ActionSupport{
	
	private int dateType;
	private String dimensionValue;
	private String dateString;
	private String dimensionString;
	private OutpatientWorkloadService outpatientWorkloadService;
	@Autowired
    @Qualifier(value = "outpatientWorkloadService")
	public void setOutpatientWorkloadService(
			OutpatientWorkloadService outpatientWorkloadService) {
		this.outpatientWorkloadService = outpatientWorkloadService;
	}
	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getDimensionString() {
		return dimensionString;
	}

	public void setDimensionString(String dimensionString) {
		this.dimensionString = dimensionString;
	}

	public int[] getDateArray() {
		return dateArray;
	}

	public void setDateArray(int[] dateArray) {
		this.dateArray = dateArray;
	}

	private int[] dateArray;
	
	private HttpServletRequest request=ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value="operationCostListService")
	private OperationCostListService operationCostListService;
	
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService innerCodeService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public OperationCostListService getOperationCostListService() {
		return operationCostListService;
	}

	public void setOperationCostListService(
			OperationCostListService operationCostListService) {
		this.operationCostListService = operationCostListService;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public String getDimensionValue() {
		return dimensionValue;
	}

	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}

	@Action(value = "operationCostList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/operation/operationCostList/operationCostlistbi.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String operationCostList() throws Exception {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		List<BusinessDictionary> biorglist = innerCodeService.getDictionary("operatetype");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("ops_kind");
		for(BusinessDictionary bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getEncode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="ops_kind,手术类型";
		return "list";
	}
	
	/**
	 * 加载统计
	 * @author zhangjin
	 * @createDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "treeOperationCost")
	public void treeOperationCost()  {
		List<BusinessDictionary> codeSettlementList = innerCodeService.getDictionary("operatetype");
		String json=JSONUtils.toExposeJson(codeSettlementList, false, null, "encode","name");
		WebUtils.webSendJSON(json);
	}
	@Action(value = "treeOperationCostmap")
	public void treeOperationCostmap()  {
		List<BusinessDictionary> codeSettlementList = innerCodeService.getDictionary("operatetype");
		Map<String,String> map=new HashMap<String,String>();
		for(BusinessDictionary biorg:codeSettlementList){
			map.put(biorg.getEncode(), biorg.getName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 加载统计
	 * @author zhangjin
	 * @createDate：2016-8-15
	 * @version 1.0
	 */
	@Action(value = "queryOperationCost")
	public void queryOperationCost()  {
		String[] dimStringArray =request.getParameterValues("dimStringArray[]"); 
		String[] dateArray =request.getParameterValues("dateArray[]");
		DateVo datevo =new DateVo();
		datevo.setYear2(Integer.valueOf(dateArray[1]));
		datevo.setYear1(Integer.valueOf(dateArray[0]));
		datevo.setQuarter2(Integer.valueOf(dateArray[3]));
		datevo.setQuarter1(Integer.valueOf(dateArray[2]));
		datevo.setMonth2(Integer.valueOf(dateArray[5]));
		datevo.setMonth1(Integer.valueOf(dateArray[4]));
		datevo.setDay2(Integer.valueOf(dateArray[7]));
		datevo.setDay1(Integer.valueOf(dateArray[6]));
		String resultData =operationCostListService.queryOperationCost(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendJSON(resultData);
	}
}
