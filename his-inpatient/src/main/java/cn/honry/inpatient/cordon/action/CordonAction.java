package cn.honry.inpatient.cordon.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.cordon.service.CordonService;
import cn.honry.inpatient.cordon.vo.CordonVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**  
 * @Description： 护士站患者警戒线action 
 * @Author：lyy
 * @CreateDate：2015-12-9 下午02:33:46  
 * @Modifier：lyy
 * @ModifyDate：2015-12-9 下午02:33:46  
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/cordon")
public class CordonAction extends ActionSupport{
	private Logger logger=Logger.getLogger(CordonAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	private CordonService cordonService;
	@Autowired
	@Qualifier(value="cordonService")
	public void setCordonService(CordonService cordonService) {
		this.cordonService = cordonService;
	}
	/**
	 * 护士站患者虚拟实体
	 */
	private CordonVo cordonVo = new CordonVo();
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * 科室实体
	 */
	private SysDepartment department=new SysDepartment();
	/**
	 * 住院主表
	 */
	private InpatientInfoNow inpatientInfo;
	/**
	 * 分页用的页数
	 */
	private String page;
	/**
	 * 分页用的总条数
	 */
	private String rows;
	/**
	 * 患者警戒线主键
	 */
	private String pids; 
	/**
	 * 患者名称    查询条件
	 */
	private String queryName;  
	/**
	 * 病区树的deptId值
	 */
	private String deptId;
	
	public InpatientInfoNow getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(InpatientInfoNow inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	public CordonVo getCordonVo() {
		return cordonVo;
	}
	public void setCordonVo(CordonVo cordonVo) {
		this.cordonVo = cordonVo;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public SysDepartment getDepartment() {
		return department;
	}
	public void setDepartment(SysDepartment department) {
		this.department = department;
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
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * @Description：  跳转到护士站患者警戒线界面
	 * @Author：lyy
	 * @CreateDate：2015-12-9 下午03:20:52  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-9 下午03:20:52  
	 * @return Stirng
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZJJX:function:view"})
	@Action(value = "listCordon", results = { @Result(name = "list", location = "/WEB-INF/pages/patient/cordon/cordonList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listCordon() {
		SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(dept!=null){
			deptId=dept.getId();
		}else{
			ServletActionContext.getRequest().setAttribute("deptFlg", "false");
		}
		return "list";
	}
	/**
	 * @Description：  护士工作站
	 * @Author：lyy
	 * @CreateDate：2015-12-9 下午06:20:52  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-9 下午06:20:52 
	 * @return void 
	 * @ModifyRmk：   
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZJJX:function:query"})
	@Action(value="TreeDeptcordon")
	public void TreeDeptcordon(){
		try {
			List<TreeJson> treeDepat= cordonService.TreeDeptCordon(department.getId());
			String json=JSONUtils.toJson(treeDepat);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_JJXSZ", "住院护士站_警戒线设置", "2", "0"), e);
		}
	}
	/**
	 * 跳转到设置警戒线页面
	 * @author  lyy
	 * @createDate： 2016年5月10日 上午10:46:09 
	 * @modifier lyy
	 * @modifyDate：2016年5月10日 上午10:46:09
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZJJX:function:set"})
	@Action(value = "setCordonUrl", results = { @Result(name = "cordonUrl", location = "/WEB-INF/pages/patient/cordon/cordonSet.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String setCordonUrl(){
		inpatientInfo=cordonService.get(pids);
		return "cordonUrl";
	} 
	/**
	 * 查询警戒线列表(分页)根据科室Id
	 * @author  lyy
	 * @createDate： 2015年12月19日 上午11:32:14 
	 * @modifier lyy
	 * @modifyDate：2015年12月19日 上午11:32:14  
	 * @return void
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZJJX:function:query"})
	@Action(value = "queryCordon")
	public void queryCordon(){
		try {
			/**
			 * 获得登录科室部门
			 */
			SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			/**
			 * 条件查询new出一个新对象
			 */
			cordonVo=new CordonVo();
			if(StringUtils.isNotBlank(queryName)){
				queryName=queryName.trim();
				}
			cordonVo.setNationCode(deptId);;
			cordonVo.setPatientName(queryName);
			int total = cordonService.getTotal(cordonVo,dept.getDeptCode());
			List<CordonVo> cordonList = cordonService.getPage(cordonVo,page,rows,dept.getDeptCode());
			for (int i = 0; i < cordonList.size(); i++) {
				if(i==0){
					cordonList.get(i).setOrderId(Integer.parseInt(page)*Integer.parseInt(rows)-Integer.parseInt(rows)+1);
				}else{
					cordonList.get(i).setOrderId(cordonList.get(i-1).getOrderId()+1);
				}
			}
			Map<String, Object> outmap=new HashMap<String,Object>();
			outmap.put("total", total);
			outmap.put("rows", cordonList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYHSZ_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_JJXSZ", "住院护士站_警戒线设置", "2", "0"), e);
		}
	}
	/**
	 * 警戒线保存
	 * @author  lyy
	 * @createDate： 2015年12月21日 上午10:32:14 
	 * @modifier lyy
	 * @modifyDate：2015年12月21日 上午10:32:14  
	 * @return void
	 * @modifyRmk：  代码规范
	 * @version 1.0
	 */
	@RequiresPermissions(value={"HZJJX:function:save"})
	@Action(value = "saveCordon")
	public void saveCordon(){
		String retVal="no";
		try {
			cordonService.saveCordonVo(cordonVo);
			retVal="yes";
		} catch (Exception e) {
			retVal="no";
			WebUtils.webSendJSON("error");
			logger.error("ZYHSZ_JJXSZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYHSZ_JJXSZ", "住院护士站_警戒线设置", "2", "0"), e);
		}
		WebUtils.webSendString(retVal);
	}
	

}
