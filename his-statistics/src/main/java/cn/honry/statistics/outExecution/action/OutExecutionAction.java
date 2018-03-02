package cn.honry.statistics.outExecution.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.outExecution.service.OutExecutionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/outExecution")
public class OutExecutionAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private List<InpatientExecbill> jsson=new ArrayList<InpatientExecbill>();
	public List<InpatientExecbill> getJsson() {
		return jsson;
	}
	public void setJsson(List<InpatientExecbill> jsson) {
		this.jsson = jsson;
	}
	/**栏目别名,在主界面中点击栏目时传到action的参数**/
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**  
	 *  
	 * 跳转医嘱执行单查询分解页面
	 * @Author：GH
	 * @CreateDate：2017-1-13 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CYZXDCX:function:view"})
	@Action(value = "list", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/outExecution/implementationList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String list() {
		try {
			SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			if (dept != null) {
				jsson = outExecutionService.queryDocAdvExe(null, null,dept);
			}else {
				ServletActionContext.getRequest().setAttribute("deptFlg", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	@Action(value = "queryLSHList", results = { @Result(name = "json",type="json") })
	public void queryLSHList(){
		Map<String, Object> map=outExecutionService.queryLSHList(queryBlh,queryLsh,startTime,endTime);
		String json=JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
		WebUtils.webSendJSON(json);
	}

	@Action(value = "queryDrugLists", results = { @Result(name = "json",type="json") })
	public void queryDrugLists(){
		Map<String, Object> map=outExecutionService.queryDrugLists(queryLsh,page,rows,startTime,endTime);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "queryUnDrugList", results = { @Result(name = "json",type="json") })
	public void queryUnDrugList(){
		Map<String, Object> map=outExecutionService.queryUnDrugList(queryLsh, page, rows, startTime, endTime);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * GH
	 * 弹窗 
	 * @return
	 */
	@Action(value = "selectDialogURL", results = { @Result(name = "selectDialogURL", location = "/WEB-INF/pages/stat/outExecution/selectDialog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String selectDialogURL(){
		return "selectDialogURL";
	}

	@Autowired
	@Qualifier(value = "outExecutionService")
	private OutExecutionService outExecutionService;
	
	public void setOutExecutionService(OutExecutionService outExecutionService) {
		this.outExecutionService = outExecutionService;
	}
	//病历号
	private String queryBlh;
	//流水号
	private String queryLsh;

	//开始时间
	private String startTime;
	
	//结束时间
	private String endTime;
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

	public String getQueryBlh() {
		return queryBlh;
	}


	public void setQueryBlh(String queryBlh) {
		this.queryBlh = queryBlh;
	}


	public String getQueryLsh() {
		return queryLsh;
	}


	public void setQueryLsh(String queryLsh) {
		this.queryLsh = queryLsh;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	//查询所有科室
	@Action(value = "queryDeptList", results = { @Result(name = "json",type="json") })
	public void queryDeptList(){
		List list=outExecutionService.queryDeptList();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	private String  deptCode;
	private String id;
	public void setId(String id) {
		this.id = id;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}	
	//根据科室查询病人
	@Action(value = "queryInpatientByDept", results = { @Result(name = "json",type="json") })
	public void queryInpatientByDept(){
		Map map=outExecutionService.queryInpatientByDept(deptCode);
		List  list = new ArrayList<>();
		list.add(map);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	//异步加载tree
	@Action(value = "treeInpatient", results = { @Result(name = "json",type="json") })
	public  void treeInpatient(){
		String json = outExecutionService.treeInpatient(id, deptCode,beginDate,endDate);
		WebUtils.webSendJSON(json);
	}
	
	@RequiresPermissions(value={"CYZXDCX:function:view"})
	@Action(value = "list2", results = { @Result(name = "json",type="json") })
	public void list2() {
		jsson = outExecutionService.queryDocAdvExe(null, null,deptCode);
		ServletActionContext.getRequest().setAttribute("jsson", jsson);
		String json = JSONUtils.toJson(jsson);
		WebUtils.webSendJSON(json);
	}
	/**开始时间-用于查询**/
	private String beginDate;
	/**结束时间-用于查询**/
	private String endDate;
	/**执行单号-用于传值**/
	private String billNo;
	/**有效状态-用于传值**/
	private String validFlag;
	/**发送状态-用于传值**/
	private String drugedFlag;
	/**患者住院流水号-用于传值**/
	private String patNoData;
	
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getDrugedFlag() {
		return drugedFlag;
	}
	public void setDrugedFlag(String drugedFlag) {
		this.drugedFlag = drugedFlag;
	}
	public String getPatNoData() {
		return patNoData;
	}
	public void setPatNoData(String patNoData) {
		this.patNoData = patNoData;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public OutExecutionService getOutExecutionService() {
		return outExecutionService;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public String getId() {
		return id;
	}
	//查询非药瞩执行列表
	@Action(value = "queryunExecdrugList")
	public void queryunExecdrugList(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		List<InpatientExecundrugNow> list = outExecutionService.queryExecundrugpage(billNo,validFlag,drugedFlag,beginDate,endDate, page, rows,patNoData);
		int total=outExecutionService.queryExecundrugToatl(billNo,validFlag,drugedFlag,beginDate,endDate,patNoData);
		
		List<InpatientExecdrugNow> Ex =new ArrayList<InpatientExecdrugNow>();
		for (InpatientExecundrugNow Execundrug : list) {
			InpatientExecdrugNow in = new InpatientExecdrugNow();
				in.setId(Execundrug.getId());
				in.setDrugName(Execundrug.getUndrugName());
				in.setSpecs(Execundrug.getLabCode());
				in.setPriceUnit(Execundrug.getStockUnit());
				in.setQtyTot(Double.valueOf(Execundrug.getQtyTot()));
				in.setDocName(Execundrug.getDocName());
				in.setValidFlag(Execundrug.getValidFlag());
				in.setFrequencyName(Execundrug.getDfqCexp());
				in.setMoDate(Execundrug.getMoDate());
				in.setUseTime(Execundrug.getUseTime());
				in.setDecoDate(Execundrug.getDecoDate());
				in.setExecDpcd(Execundrug.getExecDpnm());
				Ex.add(in);
		}
		retMap.put("total", total);
		retMap.put("rows", Ex);
		String json=JSONUtils.toJson(retMap, "yyyy-MM-dd HH:mm");
		WebUtils.webSendJSON(json);
	}
	
	//查询药品执行列表
	@Action(value = "queryExecdrugList")
	public void queryExecdrugList(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		List list = outExecutionService.queryExecdrugpage(billNo,validFlag,drugedFlag,beginDate,endDate, page, rows,patNoData);
		int total=outExecutionService.queryExecdrugToatl(billNo,validFlag,drugedFlag,beginDate,endDate,patNoData);
		retMap.put("total", total);
		retMap.put("rows", list);
		String json=JSONUtils.toJson(retMap, "yyyy-MM-dd HH:mm");
		WebUtils.webSendJSON(json);
	}	
		/**查询药嘱执行单明细**/
		@Action(value = "queryDrugBillDetail")
		public void queryDrugBillDetail(){
			Map<String,Object> retMap = new HashMap<String, Object>();
			Integer type= outExecutionService.queryDrugBillDetail(billNo);
			retMap.put("total", type);
			String json=JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}	
}
