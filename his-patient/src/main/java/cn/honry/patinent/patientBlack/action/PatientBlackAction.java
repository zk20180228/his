package cn.honry.patinent.patientBlack.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;
import cn.honry.patinent.patientBlack.service.PatientBlackService;
import cn.honry.patinent.patinent.service.PatinentService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 患者黑名单
 * @author  kjh
 * @date 2015-11-5 16：40
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/patient")
@Namespace(value = "/patient/patientBlack")
public class PatientBlackAction extends ActionSupport implements ModelDriven<PatientBlackList>{
	
	@Override
	public PatientBlackList getModel() {
		return patientBlack;
	}
	private static final long serialVersionUID = 1L;
	
	private PatientBlackService patientBlackService;
	@Autowired 
	@Qualifier(value = "patientBlackService")
	public void setPatientBlackService(PatientBlackService patientBlackService) {
		this.patientBlackService = patientBlackService;
	}
	@Autowired
	@Qualifier(value = "patinentService")
	private PatinentService patinentService;
	
	public void setPatinentService(PatinentService patinentService) {
		this.patinentService = patinentService;
	}
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	

	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	// 分页
	private String page;//起始页数
	private String rows;//数据列数
	
	private PatientBlackList patientBlack = new PatientBlackList();
	private String patientBlackId;
	
	public PatientBlackList getPatientBlack() {
		return patientBlack;
	}

	public void setPatientBlack(PatientBlackList patientBlack) {
		this.patientBlack = patientBlack;
	}
	
	public String getPatientBlackId() {
		return patientBlackId;
	}

	public void setPatientBlackId(String patientBlackId) {
		this.patientBlackId = patientBlackId;
	}
	
	private String reason;//退出原因
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	//栏目别名
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}		
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	/**
	 * 信息列表
	 * @author  kjh
	 * @date 2015-11-5 16:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZHMD:function:view"}) 
	@Action(value = "listPatientBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/patientBlack/patientBlackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listPatientBlack() {
		//User user = WebUtils.getSessionUser();
		return "list";
	}

	/**
	 * 查询信息
	 * @author  kjh
	 * @date 2015-11-5 16:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZHMD:function:query"}) 
	@Action(value = "queryPatientBlack", results = { @Result(name = "json", type = "json") })
	public void queryPatientBlack() throws Exception{
		String name = ServletActionContext.getRequest().getParameter("name");
		String blackType = ServletActionContext.getRequest().getParameter("blackType");
		PatientBlackList patientBlackSearch = new PatientBlackList();
		patientBlackSearch.setBlacklistType(blackType);
		patientBlackSearch.setMedicalrecordId(name);
		int total = patientBlackService.getTotal(patientBlackSearch);
		List<PatientBlackList> patientBlackList =patientBlackService.getPage(request.getParameter("page"),request.getParameter("rows"),patientBlackSearch);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd").create();
		String json = gson.toJson(patientBlackList);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("{\"total\":" + total + ",\"rows\":" + json + "}");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * 添加 
	 * @author  kjh
	 * @date 2015-11-5 16:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZHMD:function:add"}) 
	@Action(value = "addPatientBlack", results = { @Result(name = "add", location = "/WEB-INF/pages/patient/patientBlack/patientBlackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addPatientBlack() {
		patientBlack = new PatientBlackList();
		request.setAttribute("patientBlack", patientBlack);
		return "add";
	}
	
	/**
	 * 修改
	 * @author  kjh
	 * @date 2015-11-5 16:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZHMD:function:edit"}) 
	@Action(value = "editPatientBlack", results = { @Result(name = "edit", location = "/WEB-INF/pages/patient/patientBlack/patientBlackEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editPatientBlack() {
		patientBlack = patientBlackService.get(patientBlack.getId());
		if(patientBlack.getBlacklistStarttime()!=null){
			String dateViewStart = patientBlack.getBlacklistStarttime().toString();
			patientBlack.setBlacklistStarttimeView(dateViewStart.split(" ")[0]);
		}
		if(patientBlack.getBlacklistEndtime()!=null){
			String dateViewEnd = patientBlack.getBlacklistEndtime().toString();
			patientBlack.setBlacklistEndtimeView(dateViewEnd.split(" ")[0]);
		}
		request.setAttribute("patientId", patientBlack.getPatient().getId());
		request.setAttribute("patientBlack", patientBlack);
		return "edit";
	}
	
	/**
	 * 添加&修改
	 * @author  kjh
	 * @date 2015-11-5
	 * @version 1.0
	 */
	@Action(value = "saveOrUpdatePatientBlack", results = { @Result(name = "json", type = "json") })
	public void saveOrUpdatePatientBlack() throws Exception {
		PrintWriter out = WebUtils.getResponse().getWriter();
		if(StringUtils.isNotEmpty(patientBlackId)){
			patientBlack.setId(patientBlackId);
		}
		try{
			patientBlackService.save(patientBlack);
			out.write("success");
		}catch(Exception e){
			e.printStackTrace();
			out.write("error");
		}
	}
	/**
	 * 浏览
	 * @author  kjh
	 * @date 2015-11-5 16:00
	 * @version 1.0
	 * @editer GH
	 * @time 2016年10月27日
	 */
	@RequiresPermissions(value={"HZHMD:function:view"}) 
	@Action(value = "viewPatientBlack", results = { @Result(name = "view", location = "/WEB-INF/pages/patient/patientBlack/patientBlackView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewPatientBlack() {
		patientBlack = patientBlackService.get(patientBlackId);
		Map<String, String> mapCertificate = innerCodeService.getBusDictionaryMap("blacklisttype");
		request.setAttribute("patientBlack", patientBlack);
		request.setAttribute("blacklisttype",mapCertificate.get(patientBlack.getBlacklistType()));
		return "view";
	}
	
	
	/**
	 * 删除
	 * @author  kjh
	 * @date 2015-11-5
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZHMD:function:delete"}) 
	@Action(value = "delPatientBlack", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/patientBlack/patientBlackList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delPatientBlack() throws Exception {
		patientBlackService.del(patientBlack.getId(),reason);
		return "list";
	}
	/**
	 * 弹出患者列表
	 * @author  kjh
	 * @date 2015-11-6 16:00
	 * @version 1.0
	 */
	@Action(value = "selecPatientUrl", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/patientBlack/selecPatient.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String selecPatientUrl() {
		//User user = WebUtils.getSessionUser();
		return "list";
	}
	/**
	 * 查询就诊卡信息
	 * @Author：kjh
	 * @CreateDate： 2015-11-6
	 * @version 1.0
	 *
	 */
	@Action(value = "queryIdcard", results = { @Result(name = "json", type = "json") })
	public void queryIdcard() {
		String name=ServletActionContext.getRequest().getParameter("q");
		String idcardType = request.getParameter("idcardType");
		String patientName = request.getParameter("patientName");
		PatientIdcardVO vo = new PatientIdcardVO();
		if(StringUtils.isNotBlank(idcardType)){
			vo.setIdcardType(idcardType);
		}
		if(StringUtils.isNotBlank(patientName)){
			vo.setPatientName(patientName);
		}
		if(StringUtils.isNotBlank(name)){
			vo.setName(name);
		}
		List<PatientIdcardVO> list = patinentService.listPatient(vo,page, rows);
		int total =patinentService.getPatientCount(vo);
		Map<Object, Object> map=new HashMap<>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
