package cn.honry.statistics.bi.operation.operatioNum.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import cn.honry.statistics.bi.operation.operatioNum.service.OperationNumService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 手术分类人次统计
 * @author tangfeisuai
 * @createDate：2016/8/5
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/bioperationNum")
@SuppressWarnings({ "all" })
public class OperationNumAction extends ActionSupport{
	/**
	 * service注入
	 */
	@Autowired
    @Qualifier(value = "operationNumService")
	private OperationNumService operationNumService;
	/**
	 * service注入
	 */
	private OutpatientWorkloadService outpatientWorkloadService;
	@Autowired
    @Qualifier(value = "outpatientWorkloadService")
	public void setOutpatientWorkloadService(
			OutpatientWorkloadService outpatientWorkloadService) {
		this.outpatientWorkloadService = outpatientWorkloadService;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	private int dateType;
	private String dimensionString;
	private String dimensionValue;
	private int[] dateArray ;
	private String dateString;
	private HttpServletRequest request= ServletActionContext.getRequest();
	public OperationNumService getOperationNumService() {
		return operationNumService;
	}

	public void setOperationNumService(OperationNumService operationNumService) {
		this.operationNumService = operationNumService;
	}

	public CodeInInterService getInnerCodeService() {
		return innerCodeService;
	}

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public String getDimensionString() {
		return dimensionString;
	}

	public void setDimensionString(String dimensionString) {
		this.dimensionString = dimensionString;
	}

	public String getDimensionValue() {
		return dimensionValue;
	}

	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}

	public int[] getDateArray() {
		return dateArray;
	}

	public void setDateArray(int[] dateArray) {
		this.dateArray = dateArray;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	/**  
	 * @Description：手术台次统计跳转页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016年7月25日
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "listOperationFeeType", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/operation/operationNum/operationNumList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationFeeType() {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("exec_dept");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="exec_dept,科室";
		return "list";
	}
	
	/**  
	 * @Description： 手术台次统计
	 * @Author：tangfeishuai
	 * @CreateDate：2016年7月25日
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryOperationNum")
	public void queryOperationNum() {
		
		String[] dimStringArray = null;
		String[] dateArray =null;
		try {
			dimStringArray = request.getParameterValues("dimStringArray[]"); 
			 dateArray=request.getParameterValues("dateArray[]");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		DateVo datevo =new DateVo();
		datevo.setYear2(Integer.valueOf(dateArray[1]));
		datevo.setYear1(Integer.valueOf(dateArray[0]));
		datevo.setQuarter2(Integer.valueOf(dateArray[3]));
		datevo.setQuarter1(Integer.valueOf(dateArray[2]));
		datevo.setMonth2(Integer.valueOf(dateArray[5]));
		datevo.setMonth1(Integer.valueOf(dateArray[4]));
		datevo.setDay2(Integer.valueOf(dateArray[7]));
		datevo.setDay1(Integer.valueOf(dateArray[6]));
		String resultData =operationNumService.queryOperationNum(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	
	/**
	 * 查询所有手术类型
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryOperatetype")
	public void queryOperatetype(){
		List<BusinessDictionary> deptList=innerCodeService.getDictionary("operatetype");
		BusinessDictionary b=new BusinessDictionary();
		b.setId("1");
		b.setEncode("1");
		b.setName("全部");
		deptList.add(0, b);
		String json=JSONUtils.toExposeJson(deptList, false, null, "encode","name");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询所有手术类型
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryOperatetypeMap")
	public void queryOperatetypeMap(){
		List<BusinessDictionary> deptList=innerCodeService.getDictionary("operatetype");
		HashMap<String, String> deptMap=new HashMap<String,String>();
		for (BusinessDictionary b : deptList) {
			deptMap.put(b.getEncode(), b.getName());
		}
		String json=JSONUtils.toJson(deptMap);
		WebUtils.webSendJSON(json);
	}
}
