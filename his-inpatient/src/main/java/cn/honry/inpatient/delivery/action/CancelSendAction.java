package cn.honry.inpatient.delivery.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inpatient.delivery.service.CancelSendService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 取消发送
 * @author  lyy
 * @createDate： 2015年12月30日 下午5:37:34 
 * @modifier lyy
 * @modifyDate：2015年12月30日 下午5:37:34  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/deliverySend")
public class CancelSendAction extends ActionSupport{
	private Logger logger=Logger.getLogger(CancelSendAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	private static final long serialVersionUID = 1L;
	private CancelSendService cancelSendService;
	@Autowired
	@Qualifier(value="cancelSendService")
	public void setCancelSendService(CancelSendService cancelSendService) {
		this.cancelSendService = cancelSendService;
	}
	/**
	 * 出库申请表
	 */
	private DrugApplyout applyOut;
	/**
	 * 栏目别名
	 */
	private String menuAlias;
	/**
	 * 分页用的页数
	 */
	private String page;
	/**
	 * 分页用的行数
	 */
	private String rows;
	/**
	 * 药房id
	 */
	private String deptCode;
	/**
	 * 摆药单号
	 */
	private String drugedBill;
	/**
	 * 开始时间
	 */
	private String applyDate;
	/**
	 * 结束时间
	 */
	private String applyEnd;
	/**
	 * 出库申请表主键id
	 */
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDrugedBill() {
		return drugedBill;
	}
	public void setDrugedBill(String drugedBill) {
		this.drugedBill = drugedBill;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyEnd() {
		return applyEnd;
	}
	public void setApplyEnd(String applyEnd) {
		this.applyEnd = applyEnd;
	}
	public DrugApplyout getApplyOut() {
		return applyOut;
	}
	public void setApplyOut(DrugApplyout applyOut) {
		this.applyOut = applyOut;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	/***
	 * 公共编码资料service实现层
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
    public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**
	 * 跳转到list界面
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:31:48 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:31:48
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPFSQX:function:view"})
	@Action(value="listDrug",results={@Result(name="list",location="/WEB-INF/pages/drug/delivery/deliveryEdit.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public  String listDrug(){
		return "list";
	}
	/**
	 * 药房下拉框json
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:14:57 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:14:57
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPFSQX:function:query"})
	@Action(value="cancelSendDept" )
	public void cancelSendDept(){
		try{
			List<SysDepartment> deptList = cancelSendService.queryDept();
			String json =JSONUtils.toJson(deptList);
			WebUtils.webSendJSON(json);
		}catch(Exception e ){
			logger.error("YPFSQX_YPFSQX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFSQX", "药品发送取消_药品发送取消", "2", "0"), e);
		}
	}
	/**
	 * 查询前7天内,当前这一天及后1天 (共9天的数据)
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:10:20 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:10:20
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPFSQX:function:query"})
	@Action(value="queryApplyOutList")
	public void queryApplyOutList(){
		List<DrugApplyoutNow> applyList=new ArrayList<DrugApplyoutNow>();
		DrugApplyoutNow drugApplyout=new DrugApplyoutNow();
		try {
		drugApplyout.setDrugDeptCode(deptCode);
		drugApplyout.setDrugedBill(drugedBill);
		if(StringUtils.isNotBlank(applyDate)){
			SimpleDateFormat simpleFormat = new SimpleDateFormat();
			String d=applyDate;
			String pattent="yyyy-MM-dd HH:mm:ss";
			simpleFormat.applyPattern(pattent);
			
				drugApplyout.setApplyDate(simpleFormat.parse(d));
			
		}
		if(StringUtils.isNotBlank(applyEnd)){
			SimpleDateFormat simpleFormat = new SimpleDateFormat();
			String d=applyEnd;
			String pattent="yyyy-MM-dd HH:mm:ss";
			simpleFormat.applyPattern(pattent);
			drugApplyout.setApplyEnd(simpleFormat.parse(d));
		}
		if((StringUtils.isNotBlank(drugApplyout.getDrugDeptCode())) || 
				(StringUtils.isNotBlank(drugApplyout.getDrugedBill()))|| 
				(drugApplyout.getApplyDate() != null)|| 
				(drugApplyout.getApplyEnd() != null)){
		    applyList =cancelSendService.getPageDrugApply(drugApplyout, page, rows);
		}
		} catch (Exception e ) {
			logger.error("YPFSQX_YPFSQX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_YPFSQX", "药品发送取消_药品发送取消", "2", "0"), e);
		}
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("rows",applyList);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 获得说有单位
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:30:50 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:30:50
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPFSQX:function:query"})
	@Action(value = "cancelsendUnitCombo")
	public void cancelsendUnitCombo() {
		List<BusinessDictionary> codeDrugpackagingunitList = innerCodeService.getDictionary("packunit");
		Map<String,String> unitMap = new HashMap<String, String>();
		for(BusinessDictionary unit : codeDrugpackagingunitList){
			unitMap.put(unit.getEncode(), unit.getName());
		}
		String json =JSONUtils.toJson(unitMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 取消发送功能
	 * @author  lyy
	 * @createDate： 2016年4月19日 下午5:31:08 
	 * @modifier lyy
	 * @modifyDate：2016年4月19日 下午5:31:08
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"YPFSQX:function:edit"})
	@Action(value = "editApplyOut")
	public void editApplyOut() throws Exception {
		try{
			String result = cancelSendService.editUpdate(id);
			if("ok".equals(result)){
				WebUtils.webSendString("success");
			}else{
				WebUtils.webSendString("error");
			}
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YPFSQX_QXYPFS", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPFSQX_QXYPFS", "药品发送取消_取消药品发送", "2", "0"), e);
		}
	}
}
