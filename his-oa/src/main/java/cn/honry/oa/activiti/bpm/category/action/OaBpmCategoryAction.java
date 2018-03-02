package cn.honry.oa.activiti.bpm.category.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.oa.activiti.bpm.category.service.OaBpmCategoryService;
import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/**
 * 流程分类Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/category")
@SuppressWarnings({ "all" })
public class OaBpmCategoryAction extends ActionSupport {

	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "oaBpmCategoryService")
	private OaBpmCategoryService oaBpmCategoryService;

	public void setOaBpmCategoryService(OaBpmCategoryService oaBpmCategoryService) {
		this.oaBpmCategoryService = oaBpmCategoryService;
	}

	@Autowired
	@Qualifier(value = "tenantService")
	private TenantService tenantService;
	
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	
	private String id;
	
	private Integer pageIndex;
	private Integer pageSize;
	private String name;
	private Integer rows;
	private Integer page;
	private OaBpmCategory category;
	
	
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OaBpmCategory getCategory() {
		return category;
	}

	public void setCategory(OaBpmCategory category) {
		this.category = category;
	}

	
	/**
	 * 流程分类视图
	 * @return
	 */
	@Action(value = "categoryView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/category/categoryView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String categoryView(){
		return "list";
	}

	/**
	 * 流程分类视图
	 * @return
	 */
	@Action(value = "categoryViewPage")
	public void categoryViewPage(){
		Map<String,Object> retMap= new HashMap<>();
		String tenantId = tenantService.getTenantId();//租户id
		int total = oaBpmCategoryService.getTotal(tenantId,name);//流程分类总数
		List<OaBpmCategory> list = oaBpmCategoryService.getListByPage(tenantId,name,(page-1)*rows,rows);
		retMap.put("total", total);
		retMap.put("rows", list);
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 编辑流程分类
	 * @return
	 */
	@Action(value = "editView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/category/editView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editView(){
		
		if(StringUtils.isNotBlank(id)){
			OaBpmCategory bpmCategory = oaBpmCategoryService.get(id);
			ServletActionContext.getRequest().setAttribute("model", bpmCategory);
		}
		return "list";
	}
	
	/**
	 * 保存流程分类
	 */
	@Action(value="save")
	public void save(){
		String cid = category.getId();
		if(StringUtils.isNotBlank(cid)){
			OaBpmCategory oaBpmCategory = oaBpmCategoryService.get(cid);
			oaBpmCategory.setName(category.getName());
			oaBpmCategory.setPriority(category.getPriority());
			oaBpmCategory.setUpdateTime(new Date());
			oaBpmCategory.setUpdateUser(getAccount());
			oaBpmCategoryService.saveOrUpdate(oaBpmCategory);
		}else{
			String tenantId = tenantService.getTenantId();
			category.setTenantId(tenantId);
			category.setCreateTime(new Date());
			category.setCreateDept(getDept());
			category.setCreateUser(getAccount());
			oaBpmCategoryService.saveOrUpdate(category);
		}
		Map<String,Object> map= new HashMap<>();
		map.put("resMsg", "success");
		map.put("resCode", "保存成功！");
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 查询功能
	 */
	@Action(value="categoryList")
	public void categoryList(){
		
	}
	
	/**
     * 登录用户
     * @return
     */
    public String getAccount(){
    	String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
    	return account;
    }
    
    /**
     * 登录科室
     * @return
     */
    public String getDept(){
    	SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();//当前科室;
		if(department != null){
			return department.getDeptCode();
		}else{
			return null;
		}
    }
    
}
