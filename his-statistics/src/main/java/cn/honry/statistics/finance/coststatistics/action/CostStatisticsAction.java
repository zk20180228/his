package cn.honry.statistics.finance.coststatistics.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.coststatistics.service.CostStatisticsService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 病人费用汇总查询action
 * @author  lyy
 * @createDate： 2016年6月24日 上午10:58:51 
 * @modifier lyy
 * @modifyDate：2016年6月24日 上午10:58:51
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/CostStatistics")
public class CostStatisticsAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private CostStatisticsService costStatisticsService;
	@Autowired
	@Qualifier(value="costStatisticsService")
	public void setCostStatisticsService(CostStatisticsService costStatisticsService) {
		this.costStatisticsService = costStatisticsService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	/**报表打印**/
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/**收费员渲染**/
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	/**
	 * @see 渲染科室
	 */
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	/**
	 * 开始时间
	 */
	private String firstData;
	/**
	 * 结束时间
	 */
	private String endData;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 分页总条数
	 */
	private String page;
	/**
	 * 分页总页数
	 */
	private String rows;
	/**
	 * 查询条件  默认当前时间
	 */
	private String dataTime;
	
	/**
	 * 就诊卡号
	 */
	private String idCard;
	
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
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
	public String getFirstData() {
		return firstData;
	}
	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	/**
	 * 访问页面
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午10:58:59 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午10:58:59
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="listPatientCost",results={@Result(name="list",location="/WEB-INF/pages/stat/patinent/cost/patinentCostList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String listPatientCost(){
		//获取时间
		Date date = new Date();
		endData = DateUtils.formatDateY_M_D(date);
		firstData = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	/**
	 *  病人费用汇总查询
	 * @author  lyy
	 * @createDate： 2016年6月24日 上午10:59:13 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 上午10:59:13
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryPatientCostStatistice")
	public void queryPatientCostStatistice(){
		if(StringUtils.isNotBlank(inpatientNo)){
			
		
		SysDepartment deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(deptId!=null){
			if(StringUtils.isBlank(firstData)){
				Date date = new Date();
				firstData = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(endData)){
				Date date = new Date();
				endData = DateUtils.formatDateY_M_D(date);
			}
			if(!inpatientNo.startsWith("'")&&StringUtils.isNotBlank(inpatientNo)){
				inpatientNo="'"+inpatientNo+"'";
			}
			List<InpatientFeeInfo> list=costStatisticsService.getPageCostStatistics(firstData,endData,inpatientNo,page,rows);
			int total=costStatisticsService.getTotalCostStatistics(firstData,endData,inpatientNo);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", list);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}else{
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",0);
			map.put("rows", new ArrayList<InpatientFeeInfo>());
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
		}else{
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",0);
			map.put("rows", new ArrayList<InpatientFeeInfo>());
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 * @author conglin
	 * @see 病人费用汇总打印
	 */
	@Action(value="printPatientCostStatistice")
	public void printPatientCostStatistice(){
		SysDepartment deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			if(StringUtils.isBlank(firstData)){
				Date date = new Date();
				firstData = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(endData)){
				Date date = new Date();
				endData = DateUtils.formatDateY_M_D(date);
			}
			if(!inpatientNo.startsWith("'")&&StringUtils.isNotBlank(inpatientNo)){
				inpatientNo="'"+inpatientNo+"'";
			}
			List<InpatientFeeInfo> list= new ArrayList<InpatientFeeInfo>();
			InpatientFeeInfo inpatientFeeInfo=new InpatientFeeInfo();
			inpatientFeeInfo.setInpatientFeeInfoList(costStatisticsService.getPageCostStatistics(firstData,endData,inpatientNo,page,rows));
			//员工渲染
			Map<String,String> empMap= employeeInInterService.queryEmpCodeAndNameMap();
			//查询科室用作渲染
			Map<String,String> deptCodeAndName=deptInInterService.querydeptCodeAndNameMap();
			for(InpatientFeeInfo vo:inpatientFeeInfo.getInpatientFeeInfoList()){
				if(empMap.containsKey(vo.getFeeOpercode())){
					vo.setFeeOpercode(empMap.get(vo.getFeeOpercode()));
				}
				if(empMap.containsKey(vo.getRecipeDoccode())){
					vo.setRecipeDoccode(empMap.get(vo.getRecipeDoccode()));
				}
				if(StringUtils.isNotBlank(deptCodeAndName.get(vo.getExecuteDeptcode()))){
					vo.setExecuteDeptcode(deptCodeAndName.get(vo.getExecuteDeptcode()));
				}
				if(StringUtils.isNotBlank(vo.getRecipeDeptcode())){
					vo.setRecipeDeptcode(deptCodeAndName.get(vo.getRecipeDeptcode()));
				}
			}
			list.add(inpatientFeeInfo);
			request=ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/brfyhzcx.jasper";
			 HashMap<String, Object> parameters = new HashMap<String, Object>();
			 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			 parameters.put("firstTime", firstData);
			 parameters.put("endTime", endData);
			 parameters.put("deptName", deptId.getDeptName());
			 try {
				iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(list));
			} catch (Exception e) {
				e.printStackTrace();
			}


	
	}
	@Action(value="expPatientCostStatistice")
	public void expPatientCostStatistice() throws IOException{
		SysDepartment deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(deptId!=null){
			if(StringUtils.isNotBlank(inpatientNo)&&!inpatientNo.startsWith("'")){
				inpatientNo="'"+inpatientNo+"'";
			}
			List<InpatientFeeInfo> list=costStatisticsService.queryCostStatistice(firstData,endData,inpatientNo);
			if(list==null || list.isEmpty()){
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录!");
			}
			String head = "";
			String name = "病人费用汇总查询";
			String arrayHead[] ={"住院流水号","姓名","项目类别","合计金额","开立科室","开立医生","记账科室","收费员","收费日期"};
			for (String message: arrayHead) {
				head+=","+message;
			}
			head=head.substring(1);
			FileUtil fUtil=new FileUtil();
			String fileName=name+ DateUtils.formatDateY_M_D_H_M(new Date())+ ".csv";
			String filePath=ServletActionContext.getServletContext().getRealPath("/WEB-INF")+ "/downLoad/" +fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			PrintWriter out=WebUtils.getResponse().getWriter();
			try{
				fUtil=costStatisticsService.export(list,fUtil);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME+fileName);
				out.write("success");
			}catch (Exception e) {
				out.write("error");
			}finally {
				out.flush();
				out.close();
			}
		}
	}
	
	
	/**
	 * @Description:通过病历号住院主表的患者信息
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	**/
	@Action(value = "queryInpatientInfo")
	public void queryInpatientInfo(){
		List<InpatientInfoNow> info=costStatisticsService.queryInpatientInfo(idCard);
		String json=JSONUtils.toJson(info);
		WebUtils.webSendJSON(json);
	}
}
