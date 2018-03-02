package cn.honry.inpatient.bill.action;

import java.util.Date;
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
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inpatient.bill.service.BillclassService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inpatient/bill")
public class BillclassAction extends ActionSupport implements ModelDriven<DrugBillclass>{
	private Logger logger=Logger.getLogger(BillclassAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 摆药单表
	 */
	private DrugBillclass billclass = new DrugBillclass();
	/**
	 * 摆药台表
	 */
	OutpatientDrugcontrol outpatientDrugcontrol=new OutpatientDrugcontrol();
	@Override
	public DrugBillclass getModel() {
		return billclass;
	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 摆药id
	 */
	private String  ids;
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	/**
	 * 摆药台id
	 */
	private String controlId1;
	
	public String getControlId1() {
		return controlId1;
	}
	public void setControlId1(String controlId1) {
		this.controlId1 = controlId1;
	}
	private BillclassService billclassService;
	
	public BillclassService getBillclassService() {
		return billclassService;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "billclassService")
	public void setBillclassService(BillclassService billclassService) {
		this.billclassService = billclassService;
	}
	HttpServletRequest request = ServletActionContext.getRequest();
	
	private List<DrugBillclass> DrugBillclassList;
	
	public List<DrugBillclass> getDrugBillclassList() {
		return DrugBillclassList;
	}
	public void setDrugBillclassList(List<DrugBillclass> drugBillclassList) {
		DrugBillclassList = drugBillclassList;
	}
	
	
	public DrugBillclass getDrugBillclass() {
		return outpatientDrugcontrol.getDrugBillclass();
	}
	public void setDrugBillclass(DrugBillclass drugBillclass) {
		outpatientDrugcontrol.setDrugBillclass(drugBillclass);
	}
	public String getDeptCode() {
		return outpatientDrugcontrol.getDeptCode();
	}
	public void setDeptCode(String deptCode) {
		outpatientDrugcontrol.setDeptCode(deptCode);
	}
	public String getControlCode() {
		return outpatientDrugcontrol.getControlCode();
	}
	public void setControlCode(String controlCode) {
		outpatientDrugcontrol.setControlCode(controlCode);
	}
	public String getControlName() {
		return outpatientDrugcontrol.getControlName();
	}
	public void setControlName(String controlName) {
		outpatientDrugcontrol.setControlName(controlName);
	}
	public String getControlAttr() {
		return outpatientDrugcontrol.getControlAttr();
	}
	public void setControlAttr(String controlAttr) {
		outpatientDrugcontrol.setControlAttr(controlAttr);
	}
	public Integer getSendType() {
		return outpatientDrugcontrol.getSendType();
	}
	public void setSendType(Integer sendType) {
		outpatientDrugcontrol.setSendType(sendType);
	}
	public int hashCode() {
		return outpatientDrugcontrol.hashCode();
	}
	public String getMark() {
		return outpatientDrugcontrol.getMark();
	}
	public void setMark(String mark) {
		outpatientDrugcontrol.setMark(mark);
	}
	public Integer getShowLevel() {
		return outpatientDrugcontrol.getShowLevel();
	}
	public void setShowLevel(Integer showLevel) {
		outpatientDrugcontrol.setShowLevel(showLevel);
	}
	public String getId() {
		return outpatientDrugcontrol.getId();
	}
	public void setId(String id) {
		outpatientDrugcontrol.setId(id);
	}
	public Integer getAutoPrint() {
		return outpatientDrugcontrol.getAutoPrint();
	}
	public String getCreateUser() {
		return outpatientDrugcontrol.getCreateUser();
	}
	public void setAutoPrint(Integer autoPrint) {
		outpatientDrugcontrol.setAutoPrint(autoPrint);
	}
	public void setCreateUser(String createUser) {
		outpatientDrugcontrol.setCreateUser(createUser);
	}
	public Integer getPrintLabel() {
		return outpatientDrugcontrol.getPrintLabel();
	}
	public void setPrintLabel(Integer printLabel) {
		outpatientDrugcontrol.setPrintLabel(printLabel);
	}
	public String getCreateDept() {
		return outpatientDrugcontrol.getCreateDept();
	}
	public void setCreateDept(String createDept) {
		outpatientDrugcontrol.setCreateDept(createDept);
	}
	public Integer getBillPreview() {
		return outpatientDrugcontrol.getBillPreview();
	}
	public void setBillPreview(Integer billPreview) {
		outpatientDrugcontrol.setBillPreview(billPreview);
	}
	public Date getCreateTime() {
		return outpatientDrugcontrol.getCreateTime();
	}
	public void setCreateTime(Date createTime) {
		outpatientDrugcontrol.setCreateTime(createTime);
	}
	public String getExtendFlag() {
		return outpatientDrugcontrol.getExtendFlag();
	}
	public void setExtendFlag(String extendFlag) {
		outpatientDrugcontrol.setExtendFlag(extendFlag);
	}
	public String getUpdateUser() {
		return outpatientDrugcontrol.getUpdateUser();
	}
	public void setUpdateUser(String updateUser) {
		outpatientDrugcontrol.setUpdateUser(updateUser);
	}
	public String getExtendFlag1() {
		return outpatientDrugcontrol.getExtendFlag1();
	}
	public void setExtendFlag1(String extendFlag1) {
		outpatientDrugcontrol.setExtendFlag1(extendFlag1);
	}
	public Date getUpdateTime() {
		return outpatientDrugcontrol.getUpdateTime();
	}
	public void setUpdateTime(Date updateTime) {
		outpatientDrugcontrol.setUpdateTime(updateTime);
	}
	public String getDeleteUser() {
		return outpatientDrugcontrol.getDeleteUser();
	}
	public void setDeleteUser(String deleteUser) {
		outpatientDrugcontrol.setDeleteUser(deleteUser);
	}
	public Date getDeleteTime() {
		return outpatientDrugcontrol.getDeleteTime();
	}
	public void setDeleteTime(Date deleteTime) {
		outpatientDrugcontrol.setDeleteTime(deleteTime);
	}
	public Integer getStop_flg() {
		return outpatientDrugcontrol.getStop_flg();
	}
	public void setStop_flg(Integer stop_flg) {
		outpatientDrugcontrol.setStop_flg(stop_flg);
	}
	public Integer getDel_flg() {
		return outpatientDrugcontrol.getDel_flg();
	}
	public void setDel_flg(Integer del_flg) {
		outpatientDrugcontrol.setDel_flg(del_flg);
	}
	public String toString() {
		return outpatientDrugcontrol.toString();
	}
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
	/**
	 * 保存的Json
	 */
	private String billJson;
	/**
	 * 修改的Json
	 */
	private String billInfoJson;
	/**
	 * 保存的Json
	 */
	private String billId;
	/**
	 * 删除id
	 */
	private String idss;
	
	/**
	 * 添加所需id
	 */
	private String idclass;
	/**
	 * 明细id
	 */
	private String billIdd;
	/**
	 * 修改明细id
	 */
	private String idinfos;
	/**
	 * 科室
	 */
	private String deptCode;
	/**
	 * 摆药台名称
	 */
	private String controlName;
	
	/**
	 * 修改明细id
	 */
	private String idyy;
	
	public String getIdyy() {
		return idyy;
	}
	public void setIdyy(String idyy) {
		this.idyy = idyy;
	}
	public String getIdinfos() {
		return idinfos;
	}
	public void setIdinfos(String idinfos) {
		this.idinfos = idinfos;
	}
	public String getBillIdd() {
		return billIdd;
	}
	public void setBillIdd(String billIdd) {
		this.billIdd = billIdd;
	}
	public String getIdclass() {
		return idclass;
	}
	public void setIdclass(String idclass) {
		this.idclass = idclass;
	}
	public String getIdss() {
		return idss;
	}
	public void setIdss(String idss) {
		this.idss = idss;
	}
	public String getBillInfoJson() {
		return billInfoJson;
	}
	public void setBillInfoJson(String billInfoJson) {
		this.billInfoJson = billInfoJson;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillJson() {
		return billJson;
	}
	public void setBillJson(String billJson) {
		this.billJson = billJson;
	}
	/**
	 * 医嘱类别id
	 */
	private String yzlbid;
	/**
	 * 药品类别id
	 */
	private String yplbid;
	/**
	 * 药品性质id
	 */
	private String ypxzid;
	/**
	 * 药品药剂id
	 */
	private String ypyjid;
	/**
	 * 药品用法id
	 */
	private String yzyfid;
	
	/**
	 * 医嘱类别 Json字符串
	 */
	private String billJsonYzsx;
	/**
	 * 药品类别id Json字符串
	 */
	private String billJsonYplb;
	/**
	 * 药品性质id Json字符串
	 */
	private String billJsonYpxz;
	/**
	 * 药品药剂id Json字符串
	 */
	private String billJsonYpyj;
	/**
	 * 药品用法id Json字符串
	 */
	private String billJsonYpyf;
	public String getBillJsonYzsx() {
		return billJsonYzsx;
	}
	public void setBillJsonYzsx(String billJsonYzsx) {
		this.billJsonYzsx = billJsonYzsx;
	}
	public String getBillJsonYplb() {
		return billJsonYplb;
	}
	public void setBillJsonYplb(String billJsonYplb) {
		this.billJsonYplb = billJsonYplb;
	}
	public String getBillJsonYpxz() {
		return billJsonYpxz;
	}
	public void setBillJsonYpxz(String billJsonYpxz) {
		this.billJsonYpxz = billJsonYpxz;
	}
	public String getBillJsonYpyj() {
		return billJsonYpyj;
	}
	public void setBillJsonYpyj(String billJsonYpyj) {
		this.billJsonYpyj = billJsonYpyj;
	}
	public String getBillJsonYpyf() {
		return billJsonYpyf;
	}
	public void setBillJsonYpyf(String billJsonYpyf) {
		this.billJsonYpyf = billJsonYpyf;
	}
	public String getYzlbid() {
		return yzlbid;
	}
	public void setYzlbid(String yzlbid) {
		this.yzlbid = yzlbid;
	}
	public String getYplbid() {
		return yplbid;
	}
	public void setYplbid(String yplbid) {
		this.yplbid = yplbid;
	}
	public String getYpxzid() {
		return ypxzid;
	}
	public void setYpxzid(String ypxzid) {
		this.ypxzid = ypxzid;
	}
	public String getYpyjid() {
		return ypyjid;
	}
	public void setYpyjid(String ypyjid) {
		this.ypyjid = ypyjid;
	}
	public String getYzyfid() {
		return yzyfid;
	}
	public void setYzyfid(String yzyfid) {
		this.yzyfid = yzyfid;
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
	@RequiresPermissions(value={"BYFLGL:function:view"})
	@Action(value="billclassList",results={@Result(name="list",location="/WEB-INF/pages/drug/bill/billclassList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String billclassList()throws Exception{
		return "list";
	}
	
	/**
	 * @Description:保存
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYFLGL:function:save"})
	@Action(value="saveBillclass")
	public void saveBillclass () throws Exception{
		Map<String,String> parameterMap=new HashMap<String,String>();
		parameterMap.put("yzlbid", billJsonYzsx);//医嘱类别id
		parameterMap.put("yplbid", billJsonYplb);//药品类别id 
		parameterMap.put("ypxzid", billJsonYpxz);//药品性质id
		parameterMap.put("ypyjid", billJsonYpyj);//药品药剂id
		parameterMap.put("ypyfid", billJsonYpyf);//药品用法id
		String result="";
		try{
			billclassService.saveOrUpdate(parameterMap,billJson);
			result="success";
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
	
	/**
	 * @Description:保存(子表)
	 * @Author：  ldl
	 * @CreateDate： 2015-10-20
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYFLGL:function:save"})
	@Action(value="saveBillInfo")
	public void saveBillInfo () throws Exception{
		String result="";
		try{
			billclassService.saveOrUpdateInfo(billInfoJson,billId);
			result="success";
		}catch(Exception e){
			result="error";
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYFLGL:function:query"})
	@Action(value = "queryBillclass")
	public void queryBillclass(){
		try {
			DrugBillclass billclassSerc = new DrugBillclass();
			if(StringUtils.isNotBlank(controlId1)){
				billclassSerc.setControlId(controlId1);
			}
			List<DrugBillclass> list = billclassService.getPage(page,rows,billclassSerc);
			int total = billclassService.getTotal(billclassSerc);
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("total", total);
			outmap.put("rows", list);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
	}
	/**
	 * @Description:删除
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYFLGL:function:delete"})
	@Action(value="delBillclass")
	public void delBillclass () {
			try {
				String result="";
				billclassService.removeUnused(idss);
				result="success";
				WebUtils.webSendString(result);
			} catch (Exception e) {
				logger.error("BYGL_BYFL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
			}
	}
	/**   
	*  
	* @description：跳转到添加页面
	* @author:ldl
	* @createDate：2015-10-19
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"BYFLGL:function:add"}) 
	@Action(value = "billAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/bill/billclassAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String billAdd(){
		try {
			String feel = "1";
			ServletActionContext.getRequest().setAttribute("feel", feel);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
		return "list";
	}
	/**   
	*  
	* @description：跳转到修改页面
	* @author:ldl
	* @createDate：2015-10-19 
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"BYFLGL:function:edit"}) 
	@Action(value = "billEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/bill/billclassAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String billEdit(){
		try {
			String ids = ServletActionContext.getRequest().getParameter("ids");
			ServletActionContext.getRequest().setAttribute("ids", ids);
			String feel = "2";
			ServletActionContext.getRequest().setAttribute("feel", feel);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
		return "list";
	}
	
	/**   
	*  
	* @description：查询修改的那一条
	* @author：ldl
	* @createDate：2015-10-19
	* @modifyRmk：  
	* @version 1.0
	*/
	@Action(value = "findBill")
	public void findBill() {
		try {
			List<DrugBillclass> list = billclassService.findBillEdit(idclass);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
	}
	
	/**   
	*  
	* @description：跳转到添加页面(子表)
	* @author:ldl
	* @createDate：2015-10-20
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"BYFLGL:function:add"}) 
	@Action(value = "billInfoAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/bill/billclassEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String billInfoAdd(){
		try {
			String feel = "1";
			ServletActionContext.getRequest().setAttribute("feel", feel);
			ServletActionContext.getRequest().setAttribute("billId", billIdd);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
		return "list";
	}
	/**   
	*  
	* @description：跳转到修改页面(子表)
	* @author:ldl
	* @createDate：2015-10-20
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"BYFLGL:function:edit"}) 
	@Action(value = "billInfoEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/drug/bill/billclassEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String billInfoEdit(){
		try {
			ServletActionContext.getRequest().setAttribute("ids", idinfos);
			String feel = "2";
			ServletActionContext.getRequest().setAttribute("feel", feel);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
		return "list";
	}
	
	/**   
	 *  
	 * @description：查询修改的那一条(子表)
	 * @author：ldl
	 * @createDate：2015-10-20
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findBillInfo")
	public void findBillInfo() {
		try {
			List<DrugBilllist> list = billclassService.findBillInfoEdit(idyy);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
	}
	/**
	 * @Description：  根据id查询
	 * @Author：dh
	 * @CreateDate：2015-12-25 下午04:45:47  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	@Action(value="queryDrugBillclassList")
	public void queryDrugBillclassList() throws Exception{
		try {
			billclass.setDeptCode(deptCode);
			outpatientDrugcontrol.setControlName(controlName);
			DrugBillclassList=billclassService.getPageList(request.getParameter("page"),request.getParameter("rows"),billclass);
			int total = billclassService.getTotal(billclass);
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("total", total);
			outmap.put("rows", DrugBillclassList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYFL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYFL", "摆药管理_摆药分类管理", "2", "0"), e);
		}
	}
	
}
