package cn.honry.inpatient.bill.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.bill.service.BillclassService;
import cn.honry.inpatient.bill.service.PhaStoControlService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inpatient/billpha")
public class PhaStoControlAction extends ActionSupport implements ModelDriven<OutpatientDrugcontrol>{
	private Logger logger=Logger.getLogger(PhaStoControlAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private OutpatientDrugcontrol outpatientDrugcontrol = new OutpatientDrugcontrol();
	
	private String  pids;
	
	private String ids;
	private String menuAlias;
	
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Override
	public OutpatientDrugcontrol getModel() {
		
		return outpatientDrugcontrol;
	}
	private List<OutpatientDrugcontrol> OutpatientDrugcontrolList;
	public List<OutpatientDrugcontrol> getPhaStoControlList() {
		return OutpatientDrugcontrolList;
	}
	public void setPhaStoControlList(List<OutpatientDrugcontrol> OutpatientDrugcontrolList) {
		OutpatientDrugcontrolList = OutpatientDrugcontrolList;
	}
	private PhaStoControlService phaStoControlService;
	public PhaStoControlService getPhaStoControlService() {
		return phaStoControlService;
	}
	@Autowired
	@Qualifier(value = "phaStoControlService")
	public void setPhaStoControlService(PhaStoControlService phaStoControlService) {
		this.phaStoControlService = phaStoControlService;
	}
	private BillclassService billclassService;
	public BillclassService getBillclassService() {
		return billclassService;
	}
	@Autowired
	@Qualifier(value = "billclassService")
	public void setBillclassService(BillclassService billclassService) {
		this.billclassService = billclassService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	//分页
		private String page;
		private String rows;
		
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
	/**
	 * @Description:list页面
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @return
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYTSZ:function:view"})
	@Action(value="phaStoControlList",results={@Result(name="list",location="/WEB-INF/pages/drug/bill/phaStoControlList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String phaStoControlList()throws Exception{
		try {
			request.setAttribute("step", "list");
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		return "list";
	}
	
	/**   
	*  
	* @description：跳转到添加页面
	* @author:ldl
	* @createDate：2015-10-19
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"BYTSZ:function:add"}) 
	@Action(value = "phaStoControlAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/bill/phaStoControlAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String phaStoControlAdd(){
		try {
			String feel = "1";
			ServletActionContext.getRequest().setAttribute("feel", feel);
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		return "list";
	}
	
	/**
	 * @throws Exception    
	*  
	* @description：跳转到修改页面(子表)
	*/
	@RequiresPermissions(value={"BYFLGL:function:edit"}) 
	@Action(value = "phaStoControlEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/bill/phaStoControlEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String phaStoControlEdit() throws Exception{
		try {
			String ids = ServletActionContext.getRequest().getParameter("ids");
			ServletActionContext.getRequest().setAttribute("ids", ids);
			String feel = "2";
			ServletActionContext.getRequest().setAttribute("feel", feel);
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		return "list";
	}
	
	/**
	 * @Description:切换到添加页面
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @return
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYTSZ:function:add"})
	@Action(value="addUIPhaStotorl",results={@Result(name="list",location="/WEB-INF/pages/drug/bill/phaStoControlList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String addUIPhaStotorl()throws Exception{
		try {
			String id = request.getParameter("id");
			request.setAttribute("step", "add");
			request.setAttribute("phaStoControl", new OutpatientDrugcontrol());
			request.setAttribute("id", id);
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-9-7
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value="queryPhaStoConstrol",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void queryPhaStoConstrol(){
		try{
			OutpatientDrugcontrol outpatientDrugcontrol = new OutpatientDrugcontrol();
			List<OutpatientDrugcontrol> list = phaStoControlService.getPage(request.getParameter("page"),request.getParameter("rows"),outpatientDrugcontrol);
			int total = phaStoControlService.getTotal(outpatientDrugcontrol);
			
			String json = JSONUtils.toJson(list);
			WebUtils.webSendString(json);
		}catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询列表
	 * @Author：  dh
	 * @CreateDate： 2015-12-28
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value="queryPhaStoConstrolView",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void queryPhaStoConstrolView()throws Exception{
		try{
			OutpatientDrugcontrol drugcontrol = new OutpatientDrugcontrol();//条件查询
			String deptCode=request.getParameter("deptCode");
			if(StringUtils.isNotEmpty(ServletActionContext.getRequest().getParameter("deptCode"))){
				if(StringUtils.isNotBlank(deptCode)){
					drugcontrol.setDeptCode(deptCode);
				}
			}
			OutpatientDrugcontrolList = phaStoControlService.QueryOutpatientDrugcontrol(request.getParameter("page"),request.getParameter("rows"),outpatientDrugcontrol);
			int total = phaStoControlService.getTotalOutpatientDrugcontrol(outpatientDrugcontrol);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", OutpatientDrugcontrolList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendString(json);
		}catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	/**
	 * @Description:添加、编辑
	 * @Author：  lt
	 * @CreateDate： 2015-9-7
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYTSZ:function:save"})
	@Action(value="savePhaStoControl",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void savePhaStoControl()throws Exception{
		try{
			String pid = request.getParameter("pid");
			String deptCode = request.getParameter("deptCode");
			if(StringUtils.isNotBlank(pid)){
				DrugBillclass billclass = billclassService.get(pid);
				if(billclass!=null){
					outpatientDrugcontrol.setDrugBillclass(billclass);
				}
			}
			if(StringUtils.isNotBlank(deptCode)){
				outpatientDrugcontrol.setDeptCode(deptCode);
			}
				phaStoControlService.saveOrUpdate(outpatientDrugcontrol);
				WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	/**
	 * @Description:删除
	 * @Author：  lt
	 * @CreateDate： 2015-10-12
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYTSZ:function:delete"})
	@Action(value="delPhaStoControl",results={@Result(name="json",type="json")})
	public void delPhaStoControl () throws Exception{
		String ids = request.getParameter("ids");
		try{
			phaStoControlService.delUpdate(ids);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	/**
	 * @Description：  根据id查询
	 * @Author：dh
	 * @CreateDate：2015-11-16 下午04:45:47  
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="queryPhaStoControlList",results={@Result(name="json",type="json")})
	public void queryPhaStoControlList(){
		try {
			String id = request.getParameter("id");
			OutpatientDrugcontrolList=phaStoControlService.getPageList(request.getParameter("page"),request.getParameter("rows"),outpatientDrugcontrol);
			int total = phaStoControlService.getTotal(outpatientDrugcontrol);
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("total", total);
			outmap.put("rows", OutpatientDrugcontrolList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	/**
	 * @Description：  保存
	 * @Author：dh
	 * @CreateDate：2015-11-16 下午04:45:47  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "saveOutpatientDrugcontrol", results = { @Result(name = "json", type = "json")})
	public void saveOutpatientDrugcontrol()  {
		try {
			if(StringUtils.isNotBlank(pids)){
				String[] pid_array=pids.split(",");
				for (String pid : pid_array) {
					outpatientDrugcontrol.setId(null);
					DrugBillclass drugBillclass=new DrugBillclass();
					drugBillclass.setId(pid);
					outpatientDrugcontrol.setDrugBillclass(drugBillclass);
					phaStoControlService.saveOutpatientDrugcontrol(outpatientDrugcontrol);
				}
			}
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	
	/**   
	 *  
	 * @description：查询修改的那一条(子表)
	 * @author：ldl
	 * @createDate：2015-10-20
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	@Action(value = "findphaStoControl", results = { @Result(name = "json", type = "json") })
	public void findphaStoControl() {
		try {
			List<OutpatientDrugcontrol> list = phaStoControlService.QueryOutpatientDrugcontrolupdate(idsC);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	/**   
	 *  
	 * @description：根据摆药台id 查询摆药单
	 * @author：dh
	 * @createDate：2015-10-20
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	@Action(value = "findDrugBillclass", results = { @Result(name = "json", type = "json") })
	public void findDrugBillclass()  {
		try {
			List<DrugBillclass> list = phaStoControlService.queryDrugBillclass(idsConId);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：  修改
	 * @Author：dh
	 * @CreateDate：2015-11-16 下午04:45:47  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "updateOutpatientDrugcontrol", results = { @Result(name = "json", type = "json")})
	public void updateOutpatientDrugcontrol() throws Exception {
		String result="";
		try{
			phaStoControlService.Update(classId,dept,ids,billJsonby);
			result="success";
		}catch(Exception e){
			result="error";
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
	/**
	 * 根据摆药台id查询摆药信息
	 * @Description：
	 * @author  dh
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "viewOutpatientDrugcontrol", interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void viewOutpatientDrugcontrol() throws Exception {
		try {
			String controlId=request.getParameter("controlId");
			List<OutpatientDrugcontrol> outpatientDrugcontrollist = new ArrayList<OutpatientDrugcontrol>();
			if(controlId!=null){
				if("".equals(controlId)){
					String json = JSONUtils.toJson(outpatientDrugcontrollist);
					WebUtils.webSendJSON(json);
				}else{
					List<OutpatientDrugcontrol> list = phaStoControlService.QueryOutpatientDrugcontrolupdate(controlId);
					String json = JSONUtils.toJson(list);
					WebUtils.webSendJSON(json);
				}
			}
		} catch (Exception e) {
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		
	}
	/**
	 * @Description:保存
	 * @Author：  donghe
	 * @CreateDate： 2016-3-26
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYFLGL:function:save"})
	@Action(value="savePhaSto",results={@Result(name="json",type="json")})
	public void savePhaSto(){
		String result="";
		try{
			phaStoControlService.saveOrUpdate(billClassIdss,deptCode5,billJsonby);
			result="success";
		}catch(Exception e){
			result="error";
			WebUtils.webSendJSON("error");
			logger.error("BYGL_BYTSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYTSZ", "摆药台管理_摆药设置", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
	/**
	 * 摆药单id
	 */
	private String classId;
	/**
	 * 科室
	 */
	private String dept;
	/**
	 * 要保存实体的json串
	 */
	private String billJsonn;
	/**
	 * 摆药台id
	 */
	private String idsConId;
	/**
	 * 要保存实体的json串
	 */
	private String billJson;
	/**
	 * 要保存实体的json串
	 */
	private String billJsonby;
	/**
	 * 摆药单id
	 */
	private String billClassIdss;
	/**
	 * 摆药name
	 */
	private String controlNamess;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getIdsConId() {
		return idsConId;
	}
	public void setIdsConId(String idsConId) {
		this.idsConId = idsConId;
	}
	public String getBillJsonn() {
		return billJsonn;
	}
	public void setBillJsonn(String billJsonn) {
		this.billJsonn = billJsonn;
	}
	public String getControlNamess() {
		return controlNamess;
	}
	public void setControlNamess(String controlNamess) {
		this.controlNamess = controlNamess;
	}
	/**
	 * 摆药台id
	 */
	private String idsC;
	public String getIdsC() {
		return idsC;
	}
	public void setIdsC(String idsC) {
		this.idsC = idsC;
	}
	/**
	 * 科室id
	 */
	private String deptCode;
	/**
	 * 科室id
	 */
	private String deptCode5;
	/**
	 * 科室id
	 */
	private String deptCode10;
	/**
	 * 摆药台名称
	 */
	private String controlNames;
	public String getDeptCode10() {
		return deptCode10;
	}
	public void setDeptCode10(String deptCode10) {
		this.deptCode10 = deptCode10;
	}
	public String getBillJsonby() {
		return billJsonby;
	}
	public void setBillJsonby(String billJsonby) {
		this.billJsonby = billJsonby;
	}
	public String getDeptCode5() {
		return deptCode5;
	}
	public void setDeptCode5(String deptCode5) {
		this.deptCode5 = deptCode5;
	}
	public String getBillClassIdss() {
		return billClassIdss;
	}
	public void setBillClassIdss(String billClassIdss) {
		this.billClassIdss = billClassIdss;
	}
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
	
}
