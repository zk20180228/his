package cn.honry.oa.activiti.delegateInfo.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaDelegateInfo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.activiti.delegateInfo.service.OaDelegateInfoService;
import cn.honry.oa.activiti.delegateInfo.vo.DelegateInfoVo;
import cn.honry.oa.activiti.queryFlow.action.queryFlowAction;
import cn.honry.oa.activiti.queryFlow.service.QueryFlowService;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/**
 * 我的代理 Action
 * @author user
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/delegateInfo")
@SuppressWarnings({ "all" })
public class DelegateInfoAction extends ActionSupport{
	
	private Logger logger=Logger.getLogger(queryFlowAction.class);
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	
	@Autowired
	@Qualifier(value = "oaDelegateInfoService")
	private OaDelegateInfoService oaDelegateInfoService;
	
	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	
	HttpServletRequest request = ServletActionContext.getRequest();
	
	public void setOaDelegateInfoService(OaDelegateInfoService oaDelegateInfoService) {
		this.oaDelegateInfoService = oaDelegateInfoService;
	}
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/**页码**/
	private int page;
	
	/**每页记录数**/
	private int rows;
	
	/**我的代理表主键id**/
	private String delInfoId;
	
	/**我的代理实体**/
	private OaDelegateInfo oadeInfo;
	
	/**流程id**/
	private String processDefinitionId;
	
	/**根据流程节点查询环节的内容 **/
	private String bpmjson;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public OaDelegateInfo getOadeInfo() {
		return oadeInfo;
	}

	public void setOadeInfo(OaDelegateInfo oadeInfo) {
		this.oadeInfo = oadeInfo;
	}

	public String getDelInfoId() {
		return delInfoId;
	}

	public void setDelInfoId(String delInfoId) {
		this.delInfoId = delInfoId;
	}

	public String getBpmjson() {
		return bpmjson;
	}

	public void setBpmjson(String bpmjson) {
		this.bpmjson = bpmjson;
	}

	/**
	 * 修改跳转代理页面
	 * @return
	 */
	@Action(value = "delegateinfoView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/addDelegate.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delegateinfoView(){
		OaBpmConfNode o = new OaBpmConfNode();
		oadeInfo = oaDelegateInfoService.findMydelegateInfo(delInfoId);
		List<OaBpmConfNode> list =oaDelegateInfoService.queryOaBpmConfNode(oadeInfo.getProcessDefinitionId());
		if(list.size()>0&&list!=null){
			list.remove(0);
			o.setCode("process");
			o.setName("全部");
			list.add(0, o);
		}
		bpmjson=JSONUtils.toJson(list);
		return "list";
	}
	
	/**
	 * 添加跳转代理页面
	 * @return
	 */
	@Action(value = "addDelegateinfoView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/addDelegate.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addDelegateinfoView(){
		return "list";
	}
	
	/**
	 * 跳转选择人员页面
	 * @return
	 */
	@Action(value = "addExtendView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/queryFlow/addExtend.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addExtendView(){
		return "list";
	}

	/**
	 * 查询流程
	 * @return
	 */
	@Action(value = "queryDeleList")
	public void queryDeleList(){
		List<DelegateInfoVo> deleList = oaDelegateInfoService.queryProcess();
		String json = JSONUtils.toJson(deleList);
		WebUtils.webSendJSON(json);
		
	}
	
	/**
	 * 查询环节
	 * @return
	 */
	@Action(value = "OaBpmConfNodeList")
	public void OaBpmConfNodeList(){
		OaBpmConfNode o = new OaBpmConfNode();
		List<OaBpmConfNode> list =oaDelegateInfoService.queryOaBpmConfNode(processDefinitionId);
		if(list.size()>0&&list!=null){
			list.remove(0);
			o.setCode("process");
			o.setName("全部");
			list.add(0, o);
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
		
	}
	
	/**
	 * 
	 * 查询我的代理
	 * @return
	 */
	@Action(value = "listMydelegate")
	public void listMydelegate(){
		List<OaDelegateInfo> list = oaDelegateInfoService.queryMyDelegate(page, rows);
		Integer total = oaDelegateInfoService.queryMyDelegateRotal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total",total);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * 添加我的代理
	 * @return
	 */
	@Action(value = "addMydelegate")
	public void addMydelegate(){
		oaDelegateInfoService.addOaDelegateInfo(oadeInfo);
	}
	
	/**
	 * 
	 * 删除我的代理
	 * @return
	 */
	@Action(value = "delMydelegate")
	public void delMydelegate(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try{
			oaDelegateInfoService.delMydelegateInfo(delInfoId);
			retMap.put("resCode", "success");
		}catch(Exception e ){
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
		
		
	}
}
