package cn.honry.inpatient.delivery.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inpatient.delivery.service.DeliveryService;
import cn.honry.inpatient.delivery.vo.DeliveryVo;
import cn.honry.report.service.IReportService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 药品集中发送
 * @author  lyy
 * @createDate： 2015年12月26日 下午3:36:22 
 * @modifier lyy
 * @modifyDate：2015年12月26日 下午3:36:22  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/deliveryDelivery")
public class DeliveryAction extends ActionSupport{
	private Logger logger=Logger.getLogger(DeliveryAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	/**
	 * 摆药单业务层 service
	 */
	private DeliveryService deliveryService;
	@Autowired
	@Qualifier(value="deliveryService")
	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}
	//编码接口service

	@Autowired
	@Qualifier(value="innerCodeService")

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	private CodeInInterService  innerCodeService;

	@Autowired
	@Qualifier(value="employeeInInterService")
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	private EmployeeInInterService  employeeInInterService;
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	/**
	 * 出库申请实体
	 */
	private DrugApplyoutNow applyout;
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * 已发送或者未摆药树上id主键
	 */
	private String id;
	/**
	 * 起始页数
	 */
	private String page;
	/**
	 * 数据列数
	 */
	private String rows;
	/**
	 * 集中发送的虚拟实体类
	 */
	private List<DeliveryVo> deliveryVoList;
	/**
	 * 摆药单分类
	 */
	private String billCode;
	/**
	 * 摆药单号
	 */
	private String drugEdBill;
	/**
	 * 发送状态
	 */
	private Integer sendType;
	/**
	 * 摆药类型
	 */
	private String billType;
	private String inpatientNo;
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getDrugEdBill() {
		return drugEdBill;
	}
	public void setDrugEdBill(String drugEdBill) {
		this.drugEdBill = drugEdBill;
	}
	public DrugApplyoutNow getApplyout() {
		return applyout;
	}
	public void setApplyout(DrugApplyoutNow applyout) {
		this.applyout = applyout;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public List<DeliveryVo> getDeliveryVoList() {
		return deliveryVoList;
	}
	public void setDeliveryVoList(List<DeliveryVo> deliveryVoList) {
		this.deliveryVoList = deliveryVoList;
	}
	/**
	 * 跳转到list界面
	 * @author  lyy
	 * @createDate： 2016年4月20日 下午6:38:54 
	 * @modifier lyy
	 * @modifyDate：2016年4月20日 下午6:38:54
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:view"})
	@Action(value = "listDelivery", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/delivery/deliveryList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDelivery() {
		return "list";
	}
	/**
	 * 未发送状态的摆药分类
	 * @author  lyy
	 * @createDate： 2016年4月14日 下午7:19:46 
	 * @modifier lyy
	 * @modifyDate：2016年4月14日 下午7:19:46
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:query"})
	@Action(value="treeDelivery")
	public void treeDelivery(){
		try {
			List<TreeJson> billClass=deliveryService.queryBillClassName(id);
			String json=JSONUtils.toJson(billClass);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 * 已发送状态的摆药单号
	 * @author  lyy
	 * @createDate： 2016年4月15日 下午5:55:49 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 下午5:55:49
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:query"})
	@Action(value="treeApplyOut")
	public void treeApplyOut(){
		try {
			String applyOut=deliveryService.queryApply(id);
			WebUtils.webSendJSON(applyOut);
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 * 查询要发送的药品的列表
	 * @author  lyy
	 * @createDate： 2016年4月15日 下午6:28:14 
	 * @modifier lyy
	 * @modifyDate：2016年4月15日 下午6:28:14
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:query"})
	@Action(value = "queryDelivery")
	public void queryDelivery() throws Exception{
		try {
			SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			/**
			 * 病区集合id
			 */
			String deptCodeIds=""; 
			/**
			 * 病区id 
			 */
			String deptCode="";
			if(!"N".equals(dept.getDeptType())){
				deptCodeIds=dept.getId();
				
			}else{
				List<DepartmentContact> deptContactList =deliveryService.queryDeptContact(dept.getId());
				if(deptContactList!=null&&deptContactList.size()>0){
					for (DepartmentContact dc : deptContactList) {
						if(!"".equals(deptCode)){
							deptCode+="','";
							
						}
						deptCode+=dc.getDeptId();
					}
					deptCodeIds=deptCode;
				}
			}
			if(billCode==null){
				deliveryVoList = new ArrayList<DeliveryVo>();
				String json = JSONUtils.toJson(deliveryVoList);
				WebUtils.webSendJSON(json);
			}else{
				DeliveryVo deliverySerch = new DeliveryVo();//条件查询
				deliverySerch.setBillclassCode(billCode);
				deliverySerch.setDeptCode(deptCodeIds);
				deliveryVoList = deliveryService.getPage(deliverySerch,page,rows,deptCodeIds);
				String json = JSONUtils.toJson(deliveryVoList);
				WebUtils.webSendJSON(json);
			}
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 * 查询已摆药的列表
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午2:33:53 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午2:33:53
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:query"})
	@Action(value = "queryAlreadyBill")
	public void queryAlreadyBill() throws Exception{
		try {
			DeliveryVo deliverySerch = new DeliveryVo();//条件查询
			int total =0;
			deliveryVoList=new ArrayList<DeliveryVo>();
			
			deliverySerch.setInpatientNo(inpatientNo);
			deliverySerch.setDrugEdBill(drugEdBill);
			if((StringUtils.isNotBlank(deliverySerch.getInpatientNo()))
					||(StringUtils.isNotBlank(deliverySerch.getDrugEdBill()))){
				total = deliveryService.getTotalBill(deliverySerch);
				deliveryVoList = deliveryService.getPageBill(deliverySerch, page,rows);
			}
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", deliveryVoList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 * 药品集中发送保存方法(未打印)
	 * @author  lyy
	 * @createDate： 2016年4月20日 下午6:34:52 
	 * @modifier lyy
	 * @modifyDate：2016年4月20日 下午6:34:52
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:save"})
	@Action(value = "saveApplyOut")
	public void saveApplyOut() throws Exception {
		
		try{
			String result = deliveryService.applyOutUpdate(id,sendType);
			if(result.length()>1){
				WebUtils.webSendString(result);
			}
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 *  药品集中发送保存方法(已打印)
	 * @author  lyy
	 * @createDate： 2016年4月20日 下午6:35:23 
	 * @modifier lyy
	 * @modifyDate：2016年4月20日 下午6:35:23
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "updateApplyOut")
	public void updateApplyOut() throws Exception {
		try{
			String result = deliveryService.applyOutUpdateStamp(id,sendType);
			if("ok".equals(result)){
				WebUtils.webSendString("success");
			}else{
				WebUtils.webSendString("error");
			}
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 * 用户渲染map
	 * @author  lyy
	 * @createDate： 2016年4月20日 下午6:35:58 
	 * @modifier lyy
	 * @modifyDate：2016年4月20日 下午6:35:58
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPJZFS:function:query"})
	@Action(value = "queryEmpName")
	public void queryEmpName() {
		try {
			Map<String, String> mapEmp=employeeInInterService.queryEmpCodeAndNameMap();
			String json=JSONUtils.toJson(mapEmp);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
		
	}
	/**
	 * 
	 * @Description 包装单位渲染
	 * @author  lyy
	 * @createDate： 2016年7月25日 下午1:47:45 
	 * @modifier lyy
	 * @modifyDate：2016年7月25日 下午1:47:45
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="likeDrugPackagingunit")
	public void likeDrugPackagingunit(){
		try {
			List<BusinessDictionary> dicCertificate=innerCodeService.getDictionary("minunit");
			String json=JSONUtils.toJson(dicCertificate);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
	/**
	 * 摆药单打印
	 * @author  lyy
	 * @createDate： 2016年5月13日 下午4:57:25 
	 * @modifier lyy
	 * @modifyDate：2016年5月13日 下午4:57:25
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "iReportInvoiceBill")
	public void iReportInvoiceBill()throws Exception {
		try{	
			HttpServletRequest request = ServletActionContext.getRequest();
			String drugedbill=request.getParameter("drugedbill");    //摆药单号
			String inpatientNo= request.getParameter("inpatientNo");    //住院流水号
			List<DeliveryVo> list=deliveryService.iReportInvoiceBill(inpatientNo, drugedbill);
			request=ServletActionContext.getRequest();
			String root_path = request.getSession().getServletContext().getRealPath("/");
			String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/baiyaodan.jasper";
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("HOSPITALNAME", HisParameters.PREFIXFILENAME);
		    parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(list));
		} catch (Exception e) {
			logger.error("YPFSQX_YPFS", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFS", "药品发送取消_药品发送", "2", "0"), e);
		}
	}
}
