package cn.honry.finance.contractunit.action;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.finance.contractunit.service.RegisterFeeService;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description：  挂号费维护
 * @Author：liudelin
 * @CreateDate：2015-6-4 下午05:12:16  
 * @Modifier：liudelin
 * @ModifyDate：2015-6-4 下午05:12:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/registerFee")
public class RegisterFeeAction extends ActionSupport implements ModelDriven<RegisterFee>{
	private static final long serialVersionUID = 1L;
	
	@Override
	public RegisterFee getModel() {
		return registerFee;
	}
	//栏目别名,在主界面中点击栏目时传到action的参数
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private RegisterFee registerFee = new RegisterFee();
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	
    //挂号费列表
	private List<RegisterFee> registerFeeList;

	private RegisterFeeService registerFeeService;
	
	@Autowired 
	@Qualifier(value = "registerFeeService")
	public void setRegisterFeeService(RegisterFeeService registerFeeService) {
		this.registerFeeService = registerFeeService;
	}
	
	private Logger logger=Logger.getLogger(RegisterFeeAction.class);
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	/**  
	 *  
	 * @Description：  挂号费维护列表
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-4 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"HTDWGL:function:query"}) 
	@Action(value = "queryRegisterFee", results = { @Result(name = "json", type = "json") })
	public void queryRegisterFee() {
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			String registerGrade = ServletActionContext.getRequest().getParameter("registerGrade");
			if(StringUtils.isNotBlank(id)){
				registerFee.setUid(id);
			}
			if(StringUtils.isNotBlank(registerGrade)){
				registerFee.setRegisterGrade(registerGrade);
			}
			registerFeeList = registerFeeService.getPage(request.getParameter("page"),request.getParameter("rows"),registerFee);
			int total = registerFeeService.getTotal(registerFee);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", registerFeeList);
			String menuJson = JSONUtils.toJson(map);
			WebUtils.webSendJSON(menuJson);
		}
		catch (Exception ex) {
			WebUtils.webSendJSON("error");
			logger.error("HTDWGL_GHFLB", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HTDWGL_GHFLB", "合同单位管理_挂号费列表", "2", "0"), ex);
		}
	}

	
	/**  
	 *  
	 * @Description：  挂号费维护添加&修改
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveOrUpdateRegisterFee", results = { @Result(name = "list", location = "/WEB-INF/pages/business/contractunit/businessContractunitList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void saveOrUpdagrade() throws Exception {
		String treeCode = ServletActionContext.getRequest().getParameter("treeCode");
		RegisterFee registerFeeSave=new RegisterFee();
		if(StringUtils.isNotBlank(registerFee.getId())){
			registerFeeSave=registerFeeService.get(registerFee.getId());
		}
		registerFeeSave.setRegisterGrade(registerFee.getRegisterGrade());//挂号级别
		registerFeeSave.setRegisterFee(registerFee.getRegisterFee());//挂号费
		registerFeeSave.setCheckFee(registerFee.getCheckFee());//检查费
		registerFeeSave.setTreatmentFee(registerFee.getTreatmentFee());//治疗费
		registerFeeSave.setOtherFee(registerFee.getOtherFee());//其他费
		registerFeeSave.setOrder(registerFee.getOrder());//排序
		registerFeeSave.setDescription(registerFee.getDescription());//说明
		registerFeeService.save(registerFeeSave,treeCode);
	}
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：liudelin
	 * @CreateDate：2015-6-4 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-4 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"HTDWGL:function:delete"}) 
	@Action(value = "delRegisterFee", results = { @Result(name = "list", location = "/WEB-INF/pages/business/contractunit/businessContractunitList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delRegisterFee() throws Exception {
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			registerFeeService.del(registerFee.getId());			
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
			logger.error("HTDWGL_SC", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HTDWGL_SC", "合同单位管理_删除", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
		return "list";
	}
	/**  
	 *  
	 * @Description：  挂号级别
	 * @Author：ldl
	 * @CreateDate：2015-7-9  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "gradeFeeCombobox", results = { @Result(name = "json", type = "json") })
	public void gradeFeeCombobox() {
		List<RegisterGrade> registerGradeList = registerFeeService.gradeFeeCombobox();
		String json = JSONUtils.toJson(registerGradeList);
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description： 费用添加
	 * @Author：ldl
	 * @CreateDate：2015-10-13  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HTDWGL:function:add"}) 
	@Action(value = "feeAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/business/contractunit/businessContractunitAddFee.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String feeAdd() {
		String treeCode = ServletActionContext.getRequest().getParameter("treeCode");
		Integer order = registerFeeService.queryOrder(treeCode);
		registerFee.setOrder(order+1);
		ServletActionContext.getRequest().setAttribute("registerFee", registerFee);
		request.setAttribute("treeCode", treeCode);
		return "list";
	}
	/**  
	 *  
	 * @Description： 费用修改
	 * @Author：ldl
	 * @CreateDate：2015-10-13  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HTDWGL:function:edit"}) 
	@Action(value = "feeEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/business/contractunit/businessContractunitAddFee.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String feeEdit() {
		String id = ServletActionContext.getRequest().getParameter("feeId");
		String treeCode = ServletActionContext.getRequest().getParameter("treeCode");
		registerFee = registerFeeService.get(id);
		ServletActionContext.getRequest().setAttribute("registerFee", registerFee);
		request.setAttribute("treeCode", treeCode);
		return "list";
	}
	/**  
	 *  
	 * @Description：  查询费用修改数据
	 * @Author：ldl
	 * @CreateDate：2015-10-15  下午14:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "findRegisterFee", results = { @Result(name = "json", type = "json") })
	public void findRegisterFee() {
		try {
			String ids = ServletActionContext.getRequest().getParameter("id");
			List<RegisterFee> registerFeeList = registerFeeService.findRegisterFee(ids);
			String menuJson = JSONUtils.toJson(registerFeeList);
			WebUtils.webSendJSON(menuJson);
		}
		catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("HTDWGL_CXFY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HTDWGL_CXFY", "合同单位管理_查询费用修改", "2", "0"), e);
		}
	}
	/**  
	 * 验证挂号级别是否存在
	 * @Description：  
	 * @Author：lyy
	 * @CreateDate：2015-11-26 下午05:37:56  
	 * @Modifier：lyy
	 * @ModifyDate：2015-11-26 下午05:37:56  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryFeeValidate", results = { @Result(name = "json", type = "json") })
	public void queryFeeValidate() {
		try {
			String unitId = ServletActionContext.getRequest().getParameter("unid");
			String gradeId = ServletActionContext.getRequest().getParameter("gradeId");
			boolean isSave = registerFeeService.queryFeeValidate(unitId,gradeId);
			String json = JSONUtils.toJson(isSave);
			WebUtils.webSendJSON(json);
		}
		catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("HTDWGL_YZGHJB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("HTDWGL_YZGHJB", "合同单位管理_验证挂号级别", "2", "0"), e);
		}
	}

}
