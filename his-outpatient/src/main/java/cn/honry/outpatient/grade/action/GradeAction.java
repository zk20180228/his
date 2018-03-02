package cn.honry.outpatient.grade.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.outpatient.grade.service.GradeService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**   
*  
* @className：GradeAction
* @description：  挂号级别Action
* @author：wujiao
* @createDate：2015-6-8 上午09:50:36  
* @modifier：姓名
* @modifyDate：2015-6-8 上午09:50:36  
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/grade")
public class GradeAction extends ActionSupport implements ModelDriven<RegisterGrade>{
	
	private Logger logger=Logger.getLogger(GradeAction.class);
	
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private GradeService gradeService;
	private List<RegisterGrade> registerGradeList;
	private RegisterGrade registerGrade=new RegisterGrade() ;
	// 分页
	private String page;//起始页数
	private String rows;//数据列数
	
	@Override
	public RegisterGrade getModel() {
		return registerGrade;
	}
	
	public String getPage() {
		return page;
	}
	
	@Autowired
	@Qualifier(value = "gradeService")
	public void setGradeService(GradeService gradeService) {
		this.gradeService = gradeService;
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
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	*  
	* @description：  挂号级别list页面
	* @author：wujiao
	* @createDate：2015-6-8 上午09:50:36  
	* @modifier：姓名
	* @modifyDate：2015-6-8 上午09:50:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:view"}) 
	@Action(value = "listgrade", results = { @Result(name = "list", location = "/WEB-INF/pages/register/grade/gradeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listgrade() {
		return "list";
	}
	
   /**   
	*  
	* @description：挂号级别信息
	* @author：wujiao
	* @createDate：2015-6-8 上午09:50:36  
	* @modifier：姓名
	* @modifyDate：2015-6-8 上午09:50:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:query"}) 
	@Action(value = "queryGrade", results = { @Result(name = "json", type = "json") })
	public void queryGrade() {
		try {
			if(StringUtils.isNotBlank(registerGrade.getName())){
				registerGrade.setName(registerGrade.getName().trim());
			}
			if(StringUtils.isNotBlank(registerGrade.getCode())){
				registerGrade.setCode(registerGrade.getCode().trim());
			}
			int total = gradeService.getTotal(registerGrade);
			registerGradeList = gradeService.getPage(page,rows,registerGrade);
			String json = JSONUtils.toJson(registerGradeList);
		
			WebUtils.webSendString("{\"total\":" + total + ",\"rows\":" + json + "}");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("GHGL_GHJBGL", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), ex);
		}
	}

   /**   
	*  
	* @description：挂号级别信息添加&修改
	* @author：wujiao
	* @createDate：2015-6-8 上午09:50:36  
	* @modifier：姓名
	* @modifyDate：2015-6-8 上午09:50:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Action(value = "saveOrUpdagrade", results = { @Result(name = "list", location = "/WEB-INF/pages/register/grade/gradeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveOrUpdagrade(){
		try {
			if(StringUtils.isBlank(registerGrade.getId())){
				if(SecurityUtils.getSubject().isPermitted("GHJBGL:function:add")){
					gradeService.saveOrUpdagrade(registerGrade);
					WebUtils.webSendString("success");
				}
			}else {
				if(SecurityUtils.getSubject().isPermitted("GHJBGL:function:edit")){
					String id=ServletActionContext.getRequest().getParameter("id");
					registerGrade.setId(id);
					gradeService.saveOrUpdagrade(registerGrade);
					WebUtils.webSendString("success");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
		return "list";
	}
	
   /**   
	*  
	* @description：挂号级别信息删除
	* @author：wujiao
	* @createDate：2015-6-8 上午09:50:36  
	* @modifier：姓名
	* @modifyDate：2015-6-8 上午09:50:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:delete"}) 
	@Action(value = "delGrade", results = { @Result(name = "list", location = "/WEB-INF/pages/register/grade/gradeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delGrade() {
		try{
			gradeService.del(registerGrade.getId());			
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
		return "list";
	}
	/**   
	*  
	* @description：挂号级别信息跳转到添加页面
	* @author:ldl
	* @createDate：2015-10-14 上午15:38
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:add"}) 
	@Action(value = "gradeAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/register/grade/gradeEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String gradeAdd(){
		String feel = "1";
		ServletActionContext.getRequest().setAttribute("feel", feel);
		return "list";
	}
	/**   
	*  
	* @description：挂号级别信息跳转到修改页面
	* @author:ldl
	* @createDate：2015-10-14 上午15:38
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:edit"}) 
	@Action(value = "gradeEdit", results = { @Result(name = "list", location = "/WEB-INF/pages/register/grade/gradeEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String gradeEdit(){
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			registerGrade = gradeService.get(id);
			ServletActionContext.getRequest().setAttribute("registerGrade", registerGrade);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
		return "list";
	}
	
	/**   
	*  
	* @description：查询修改的那一条级别信息
	* @author：ldl
	* @createDate：2015-10-15 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:query"}) 
	@Action(value = "findGrade")
	public void findGrade() {
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			List<RegisterGrade> list = gradeService.findGradeEdit(id);
			String mapJosn = JSONUtils.toJson(list);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
	}
	
	/**   
	*  
	* @description：从编码表里读取职称
	* @author：ldl
	* @createDate：2015-10-22 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Action(value = "titleCombobox")
	public void titleCombobox() {
		try {
			List<BusinessDictionary> list = innerCodeService.getDictionary("title");
			String mapJosn = JSONUtils.toJson(list);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
	}
	
	/**   
	*  
	* @description：唯一验证
	* @author：ldl
	* @createDate：2015-11-4 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@RequiresPermissions(value={"GHJBGL:function:query"}) 
	@Action(value = "findGradeSize")
	public void findGradeSize() {
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			String id = ServletActionContext.getRequest().getParameter("id");
			List<RegisterGrade> list = gradeService.findGradeSize(id);
			if(list.size()==0 || list==null){
				map.put("resMsg", "Success");
			}else if(list.size()!=0 && list!=null){
				map.put("list", list);
				map.put("resMsg", "Failure");
			}
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
	}
	
	/**   
	 *  
	 * @description：获得挂号级别id和title的Map
	 * @author：wj
	 * @createDate：2015-11-23 上午10:13:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="getGradeIdTitleMap")
	public void getGradeIdTitleMap(){
		try {
			List<RegisterGrade> gradeList = gradeService.queryAll();
			Map<String,String> gradeMap = new HashMap<String, String>();
			if(gradeList!=null && gradeList.size()>0){
				for(RegisterGrade grade : gradeList){
					gradeMap.put(grade.getEncode(),grade.getCode());
				}
			}
			String mapJosn = JSONUtils.toJson(gradeMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
	}
	/**   
	 *  
	 * @description：获得挂号级别title和name的Map
	 * @author：wj
	 * @createDate：2015-11-23 上午10:13:36  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="getGradeTitleNameMap")
	public void getGradeTitleMap(){
		try {
			List<RegisterGrade> gradeList = gradeService.queryAll();
			Map<String,String> gradeMap = new HashMap<String, String>();
			if(gradeList!=null && gradeList.size()>0){
				for(RegisterGrade grade : gradeList){
					gradeMap.put(grade.getCode(), grade.getName());
				}
			}
			String mapJosn = JSONUtils.toJson(gradeMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
	}
	/**   
	*  
	* @description：获得挂号级别map id和name
	* @author：wj
	* @createDate：2015-11-23 上午10:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Action(value="getGradeMap")
	public void getGradeMap(){
		try {
			List<RegisterGrade> gradeList = gradeService.queryAll();
			Map<String,String> gradeMap = new HashMap<String, String>();
			if(gradeList!=null && gradeList.size()>0){
				for(RegisterGrade grade : gradeList){
					gradeMap.put(grade.getCode(), grade.getName());
				}
			}
			String mapJosn = JSONUtils.toJson(gradeMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHJBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHJBGL", "挂号管理_挂号级别管理", "2", "0"), e);
		}
	}
}
