package cn.honry.inpatient.doctorAdvice.action;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.system.user.service.UserInInterService;
import cn.honry.inpatient.docAdvManage.vo.SystemtypeVo;
import cn.honry.inpatient.doctorAdvice.service.DoctorAdviceService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: DoctorAdviceAction.java 
 * @Description: 患者树action
 * @author kjh
 * @date 2015-6-29
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/doctorAdvice")
@SuppressWarnings({"all"})
public class DoctorAdviceAction extends ActionSupport {
	private Logger logger=Logger.getLogger(DoctorAdviceAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	@Autowired
	@Qualifier(value = "doctorAdviceService")
	private DoctorAdviceService doctorAdviceService;
	@Qualifier(value = "inpatientInfoInInterService")
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	@Qualifier(value = "userInInterService")
	private UserInInterService userInInterService;
	
	public void setUserInInterService(UserInInterService userInInterService) {
		this.userInInterService = userInInterService;
	}

	public void setDoctorAdviceService(DoctorAdviceService doctorAdviceService) {
		this.doctorAdviceService = doctorAdviceService;
	}
	
	public void setInpatientInfoInInterService(InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}

	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private InpatientInfo inpatientInfo;
	private InpatientKind inpatientKind;
	private InpatientExecbill inpatientExecbill;
	private InpatientDrugbilldetail inpatientDrugbilldetail;
	private String id;
	private DrugUndrug undrug;
	private String page;//起始页数
	private String rows;//数据列数
	private String billNo;//执行单号
	private String billName;//执行单名称
    private String typeCodeId;
    private String drugTypeId;
    private String billType;
    private String usageCodeId;
    
	public String getTypeCodeId() {
		return typeCodeId;
	}

	public void setTypeCodeId(String typeCodeId) {
		this.typeCodeId = typeCodeId;
	}

	public String getDrugTypeId() {
		return drugTypeId;
	}

	public void setDrugTypeId(String drugTypeId) {
		this.drugTypeId = drugTypeId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getUsageCodeId() {
		return usageCodeId;
	}

	public void setUsageCodeId(String usageCodeId) {
		this.usageCodeId = usageCodeId;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public InpatientInfo getInpatientInfo() {
		return inpatientInfo;
	}
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public void setInpatientInfo(InpatientInfo inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}	
	public InpatientKind getInpatientKind() {
		return inpatientKind;
	}
	public void setInpatientKind(InpatientKind inpatientKind) {
		this.inpatientKind = inpatientKind;
	}	
	public InpatientExecbill getInpatientExecbill() {
		return inpatientExecbill;
	}
	public void setInpatientExecbill(InpatientExecbill inpatientExecbill) {
		this.inpatientExecbill = inpatientExecbill;
	}	
	public InpatientDrugbilldetail getInpatientDrugbilldetail() {
		return inpatientDrugbilldetail;
	}
	public void setInpatientDrugbilldetail(
			InpatientDrugbilldetail inpatientDrugbilldetail) {
		this.inpatientDrugbilldetail = inpatientDrugbilldetail;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DrugUndrug getUndrug() {
		return undrug;
	}
	public void setUndrug(DrugUndrug undrug) {
		this.undrug = undrug;
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

	private HttpServletRequest request = ServletActionContext.getRequest();
	/**  
	 *  
	 * @Description：住院分类下的医生站的患者树
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-9  
	 * @Modifier：ygq
	 * @ModifyDate：2015-12-9 下午04:26:39  
	 * @version 1.0
	 *
	 */
	@Action(value = "treeDoctorAdvice")
	public void treeDoctorAdvice() {		
		List<TreeJson> treeJsonList = inpatientInfoInInterService.treeInpatient(id);		
		String json = JSONUtils.toJson(treeJsonList);
		WebUtils.webSendJSON(json);	
	}
	
	/**
	 * @Description:获取患者信息页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-10
	 * @return String  
	 * @version 1.0
	**/
	@Action(value="inpatientInfos",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/doctorAdvice/patientInfo.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientInfo()throws Exception{
		String id = request.getParameter("id");
		request.setAttribute("id", id);//获取住院主表Id
		return "list";
	}
	
	/**
	 * @Description:获取医嘱执行单页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-11
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZZXD:function:view"})
	@Action(value="docAdvExeInfos",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/doctorAdvice/docAdvExe.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String docAdvExeInfos(){
		try {
			List<InpatientExecbill> inpatientExecbillList = doctorAdviceService.queryDocAdvExe(null,null);				
			request.setAttribute("json", inpatientExecbillList);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
		return "list";
	}
	/**
	 * @Description:获取添加医嘱执行单页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-11
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZZXD:function:view"})
	@Action(value="addDocAdvExeInfo",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/doctorAdvice/addDocAdvExeInfo.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addDocAdvExeInfo()throws Exception{
		return "list";
	}
	/**
	 * @Description:获取修改医嘱执行单页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-11
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZZXD:function:view"})
	@Action(value="updDocAdvExeInfo",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/doctorAdvice/docAdvExeInfo.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String updDocAdvExeInfo(){
		try {
			String ids = request.getParameter("ids");	
			List<InpatientExecbill> inpatientExecbillList = doctorAdviceService.queryDocAdvExe2(ids,null);
			if( inpatientExecbillList != null && inpatientExecbillList.size() > 0 ){
				inpatientExecbill=inpatientExecbillList.get(0);
			}else{
				 inpatientExecbill=new InpatientExecbill();
			}
			request.setAttribute("inpatientExecbill", inpatientExecbill);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}		
		return "list";
	}
	/**  
	 *  
	 * @Description：药物执行单树1
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-12  
	 * @Modifier：yeguanqun
	 * @ModifyDate：2015-12-12 下午04:26:39  
	 * @version 1.0
	 *
	 */
	@Action(value = "treeDrugExe",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/doctorAdvice/docAdvExe.jsp")})
	public String treeDrugExe() {		
		try {
			List<InpatientKind> inpatientKind= doctorAdviceService.treeDrugExe();		
			request.setAttribute("inpatientKind", inpatientKind);
			int a=1;
			request.setAttribute("b", a);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
		return "list";		
	}
	/**  
	 *  
	 * @Description：药物执行单树
	 * @Author：yeguanqun
	 * @CreateDate：2016-3-30  
	 * @Modifier：
	 * @ModifyDate： 
	 * @version 1.0
	 *
	 */
	@Action(value = "treeDrugExes")
	public void treeDrugExes() {	
		try {
			List<TreeJson> treeJsonList = doctorAdviceService.treeDrugExes(id);		
			String json = JSONUtils.toJson(treeJsonList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}			
	}
	/**  
	 *  
	 * @Description：非药物执行单树
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-14  
	 * @Modifier：ygq
	 * @ModifyDate：2015-12-14 下午04:26:39  
	 * @version 1.0
	 *
	 */
	@Action(value = "treeNoDrugExe")
	public void treeNoDrugExe() {	
		List<TreeJson> treeJsonList = doctorAdviceService.treeNoDrugExe(id);		
		String json = JSONUtils.toJson(treeJsonList);
		WebUtils.webSendJSON(json);			
	}
	
	/**  
	 *  
	 * @Description：查询执行单名称
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-14  
	 * @Modifier：ygq
	 * @ModifyDate：2015-12-14 下午04:26:39  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryDocAdvExe", results = { @Result(name = "json", type = "json") })
	public void queryDocAdvExe() {	
			try {
				String id = request.getParameter("id");
				String billName = request.getParameter("billName");
				List<InpatientExecbill> inpatientExecbillList = doctorAdviceService.queryDocAdvExe2(null,billName);				
					if("".equals(id)&&inpatientExecbillList.size()==0){
						WebUtils.webSendString("success");
					}else if(!"".equals(id)&&inpatientExecbillList.size()==0){					
						WebUtils.webSendString("success");
					}else if(StringUtils.isNotBlank(id)&&inpatientExecbillList.size()!=0){					
						if(id.equals(inpatientExecbillList.get(0).getId())){
							WebUtils.webSendString("success");
						}else{
							WebUtils.webSendString("error");
						}
					}
			} catch (Exception e) {
				logger.error("ZYHSZ_YZZXDSZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
			}
	}
	
	/**  
	 *  
	 * @Description：  添加&修改执行单
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-12 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZZXD:function:save"})
	@Action(value = "saveDocAdvExe", results = { @Result(name = "json", type = "json") })
	public void saveDocAdvExe() {
		String billName = request.getParameter("billName");
		String mark = request.getParameter("mark");
		String itemFlag = request.getParameter("itemFlag");
		String id = request.getParameter("id");
		String nurseCellCode = request.getParameter("nurseCellCode");
		String billNo = request.getParameter("billNo");
		InpatientExecbill inpatientExecbill = new InpatientExecbill();
		if(StringUtils.isNotBlank(id)){
			inpatientExecbill.setId(id);
			inpatientExecbill.setNurseCellCode(nurseCellCode);
			inpatientExecbill.setBillNo(billNo);
		}
		inpatientExecbill.setBillName(billName);
		inpatientExecbill.setMark(mark);
		inpatientExecbill.setItemFlag(itemFlag);
		try{
			doctorAdviceService.saveOrUpdateInpatientExecbill(inpatientExecbill);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}		
	}
	/**  
	 *  
	 * @Description： 保存医嘱执行单列表新增行数据
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-19 14:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZZXD:function:save"})
	@Action(value = "saveInpatientDrugbilldetail", results = { @Result(name = "json", type = "json") })
	public void saveInpatientDrugbilldetail() {
		try {
			String str = request.getParameter("str");
			String billNo=request.getParameter("billNo");
			SysDepartment  dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//获取登录科室
			if(dept==null){
				WebUtils.webSendString("error");
			}else{
				try{		
					doctorAdviceService.saveOrUpdateInpatientDrugbilldetail(str,billNo);							
					WebUtils.webSendString("success");
				}catch(Exception e){
					WebUtils.webSendJSON("error");
					logger.error("ZYHSZ_YZZXDSZ", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
				}
			}
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  删除执行单tab
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZZXD:function:delete"}) 
	@Action(value = "delDocAdvExe",  interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void delDocAdvExe() {
		String ids = ServletActionContext.getRequest().getParameter("ids");
		try{
			doctorAdviceService.del(ids);		
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}		
	}
	/**  
	 *  
	 * @Description：  删除执行单明细
	 * @Author：yeguanqun
	 * @CreateDate：2015-12-17 10:00 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"YZZXD:function:delete"}) 
	@Action(value = "delDrugbilldetail",  interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void delDrugbilldetail() {
		String ids = ServletActionContext.getRequest().getParameter("ids");
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			doctorAdviceService.delDrugbilldetail(ids);		
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:查询列表
	 * @Author：  yeguanqun
	 * @CreateDate： 2015-12-17
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryInpatientDrugbilldetail")
	public void queryInpatientDrugbilldetail(){
		try {
			String drugType = null;
			List<InpatientDrugbilldetail> drugbilldetail = doctorAdviceService.queryDrugbilldetail(inpatientDrugbilldetail,page,rows);	
			int total = doctorAdviceService.getTotalBilldetail(inpatientDrugbilldetail);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", drugbilldetail);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
		
	/**
	 * 医嘱类别表
	 * 
	 */
	@Action(value="likeDocAdv")
	public void likeDocAdv(){
		try {
			List<InpatientKind> kindList = doctorAdviceService.treeDrugExe();
			String json = JSONUtils.toJson(kindList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}	
	}
	/**
	 * 系统类别表
	 * 
	 */
	@Action(value="likeSystemtype")
	public void likeSystemtype(){
		List<BusinessDictionary> systemtypeList = innerCodeService.getDictionary("systemType");
		List<SystemtypeVo> list = null;
		if(systemtypeList.size()>0){
			list = new ArrayList<SystemtypeVo>();
			for (BusinessDictionary businessDictionary : systemtypeList) {
				SystemtypeVo systemtypeVo = new SystemtypeVo();
				systemtypeVo.setName(businessDictionary.getName());
				systemtypeVo.setCode(businessDictionary.getEncode());
				systemtypeVo.setPinyin(businessDictionary.getPinyin());
				systemtypeVo.setWb(businessDictionary.getWb());
				systemtypeVo.setInputCode(businessDictionary.getInputCode());
				list.add(systemtypeVo);
			}
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 药品类别表
	 * 
	 */
	@Action(value="likeDrugtype")
	public void likeDrugtype(){
		List<BusinessDictionary> drugtypeList = innerCodeService.getDictionary("drugType");
		String json = JSONUtils.toJson(drugtypeList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 用法类别表
	 * 
	 */
	@Action(value="likeUseage")
	public void likeUseage(){
		List<BusinessDictionary> useageList = innerCodeService.getDictionary("useage");
		String json = JSONUtils.toJson(useageList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询非药品信息资料
	 * 
	 */
	@Action(value="queryUndrugInfo")
	public void queryUndrugInfo(){
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			int total = doctorAdviceService.getTotal(undrug);
			List<DrugUndrug> unDrugList = doctorAdviceService.queryUndrugInfo(page,rows,undrug);
			map.put("total", total);
			map.put("rows", unDrugList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	/**
	 * 查询非药品信息资料
	 * 
	 */
	@Action(value="queryUndrug")
	public void queryUndrug(){
		try {
			List<DrugUndrug> unDrugList = doctorAdviceService.queryUndrugInfo();
			String json = JSONUtils.toJson(unDrugList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	/**
	 * 查询用户渲染
	 * 
	 */
	@Action(value="queryUser")
	public void queryUser(){
		Map<String,String> map = userInInterService.getUserMap();
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 加载执行单tree
	 * qh
	 * 2017-30-30
	 */
	@Action(value = "treeBills", results = { @Result(name = "json", type = "json") })
	public void treeBills() {	
		try {
			List list = doctorAdviceService.queryDocAdvExe(null,null);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:跳转编辑页面
	 * @Author： qh
	 * @CreateDate： 2015-12-11
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"YZZXD:function:view"})
	@Action(value="viewAddBill",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/doctorAdvice/editDrugBillDetail.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewAddBill(){
		inpatientDrugbilldetail=new InpatientDrugbilldetail();
		inpatientDrugbilldetail.setBillNo(billNo);
		inpatientDrugbilldetail.setBillName(billName);     
		if(id!=null){
			inpatientDrugbilldetail.setId(id);
		}
		if(typeCodeId!=null){
			inpatientDrugbilldetail.setTypeCode(typeCodeId);
		}
		if(drugTypeId!=null){
			inpatientDrugbilldetail.setDrugType(drugTypeId);
		}
		if(billType!=null){
			inpatientDrugbilldetail.setBillType(billType);
		}
		if(usageCodeId!=null){
			inpatientDrugbilldetail.setUsageCode(usageCodeId);
		}
		request.setAttribute("inpatientDrugbilldetail", inpatientDrugbilldetail);
		return "list";
	}
	
	
	
	/**
	 * 加载药品类型下拉
	 * qh
	 * 2017-03-30
	 */
	@Action(value = "queryDrugType", results = { @Result(name = "json", type = "json") })
	public void queryDrugType() {	
		try {
			List list = doctorAdviceService.queryDrugType();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	/**
	 * 加载药品用法下拉
	 * qh
	 * 2017-03-30
	 */
	@Action(value = "queryDrugUsage", results = { @Result(name = "json", type = "json") })
	public void queryDrugUsage() {	
		try {
			List list = doctorAdviceService.queryDrugUsage();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	/**
	 * 加载非药品名称下拉
	 * qh
	 * 2017-03-30
	 */
	@Action(value = "queryAllUndrug", results = { @Result(name = "json", type = "json") })
	public void queryAllUndrug() {	
		try {
			List list = doctorAdviceService.queryAllUndrug();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:保存医嘱执行单
	 * @Author： qh
	 * @return String  
	 * @version 1.0
	 **/
	@Action(value="saveDrugBillDetail",interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void saveDrugBillDetail(){
	   try{
		   String id=inpatientDrugbilldetail.getId();
		   if(StringUtils.isNotBlank(id)){
			   doctorAdviceService.updateDrugBillDetail(inpatientDrugbilldetail);
		   }else{
			   doctorAdviceService.saveDrugBillDetail(inpatientDrugbilldetail);	
		   }
	   }catch(Exception e){
		   e.printStackTrace();
		   WebUtils.webSendJSON("error");
		 logger.error("ZYHSZ_YZZXDSZ", e);
		 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
	   }
	}
	/**
	 * 根据billNo查询所有医嘱执行单
	 * qh
	 * 2017-04-01
	 */
	@Action(value = "queryAllBillDetail", results = { @Result(name = "json", type = "json") })
	public void queryAllBillDetail() {	
	    try {
			inpatientDrugbilldetail =new InpatientDrugbilldetail();
			inpatientDrugbilldetail.setBillNo(billNo);
			List list = doctorAdviceService.queryAllBillDetail(inpatientDrugbilldetail);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_YZZXDSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_YZZXDSZ", "住院护士站_医嘱执行单设置", "2", "0"), e);
		}
	}
	/**
	 * 根据billNo查询所有医嘱执行单
	 * qh
	 * 2017-04-01
	 */
	@Action(value = "queryAllType", results = { @Result(name = "json", type = "json") })
	public void queryAllType() {
		List list = new ArrayList<>();
		Map<String,String> map1 = new HashMap<>();
		Map<String,String> map2 = new HashMap<>();
		if(StringUtils.isBlank(page)){
			map1.put("billType", "1");
			map1.put("typeName", "药品执行单");
			map2.put("billType", "2");
			map2.put("typeName", "非药品执行单");
			list.add(map1);
			list.add(map2);			
		}else if("1".equals(page)){
			map1.put("billType", "1");
			map1.put("typeName", "药品执行单");
			list.add(map1);
		}else if("2".equals(page)){
			map2.put("billType", "2");
			map2.put("typeName", "非药品执行单");
	        list.add(map2);
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}


}
