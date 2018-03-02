package cn.honry.oa.repository.repositoryStat.action;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.repository.repositoryStat.service.RepositoryStatService;
import cn.honry.oa.repository.repositoryStat.vo.RepositoryStatVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 * 
 * 知识库统计
 * @Author: wangshujuan
 * @CreateDate: 2017年11月14日 下午4:09:43 
 * @Modifier: wangshujuan
 * @ModifyDate: 2017年11月14日 下午4:09:43 
 * @ModifyRmk:  
 * @version: V1.0
 * @param deptCode 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/oa/repositoryInfo")
@SuppressWarnings({"all"})
public class RepositoryStatAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	@Autowired
	@Qualifier(value = "repositoryStatService")
	private RepositoryStatService repositoryStatService;
	public void setRepositoryStatService(
			RepositoryStatService repositoryStatService) {
		this.repositoryStatService = repositoryStatService;
	}
	
	private Logger logger=Logger.getLogger(RepositoryStatAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String deptCode;
	private String menuAlias;
	private String page;
	private String rows;
	private String topnum;//发表量排行TOP值
	private String readtopnum;//阅读量排行TOP值
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	/**
	 * 知识库统计页面
	 * @return
	 */
	@RequiresPermissions(value={"ZSKTJ:function:view"})
	@Action(value = "listRepositoryStat", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/repository/info/repositoryStat.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRepositoryStat() {
		return "list";
	}
	
	
	/**  
	 * 
	 * 知识库统计  科室
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	
	@Action(value="queryRepositoryStatKS")
	public void queryRepositoryStatKS(){
		try {
			topnum = StringUtils.isBlank(topnum)?"10":topnum;
			List<RepositoryStatVo> DeathPatientList = repositoryStatService.queryRepositoryStatKS(deptCode,menuAlias,"1",topnum);
//			int total=repositoryStatService.getTotalKS(deptCode,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
			ComparatorChain chain = new ComparatorChain();
			chain.addComparator(new BeanComparator("total"), true);
			Collections.sort(DeathPatientList, chain);
//			map.put("total",total);
			map.put("rows", DeathPatientList);
			String json = JSONUtils.toJson(DeathPatientList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("OAZSK_ZSKTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAZSK_ZSKTJ", "知识库_知识库统计", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 知识库统计  作者
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	
	@Action(value="queryRepositoryStatZZ")
	public void queryRepositoryStatZZ(){
		try {
			topnum = StringUtils.isBlank(topnum)?"10":topnum;
			List<RepositoryStatVo> DeathPatientList = repositoryStatService.queryRepositoryStatZZ(deptCode,menuAlias,"1",topnum);
//			int total=repositoryStatService.getTotalZZ(deptCode,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
//			map.put("total",total);
			map.put("rows", DeathPatientList);
			String json = JSONUtils.toJson(DeathPatientList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("OAZSK_ZSKTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAZSK_ZSKTJ", "知识库_知识库统计", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 知识库统计  阅读量
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	
	@Action(value="queryRepositoryStatYDL")
	public void queryRepositoryStatYDL(){
		try {
			readtopnum = StringUtils.isBlank(readtopnum)?"10":readtopnum;
			List<RepositoryStatVo> DeathPatientList = repositoryStatService.queryRepositoryStatYDL(deptCode,menuAlias,"1",readtopnum);
			int total=repositoryStatService.getTotalYDL(deptCode,menuAlias);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", DeathPatientList);
			String json = JSONUtils.toJson(DeathPatientList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("OAZSK_ZSKTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OAZSK_ZSKTJ", "知识库_知识库统计", "2", "0"), e);
		}
	}
	
	
	
	
	public String getTopnum() {
		return topnum;
	}
	public void setTopnum(String topnum) {
		this.topnum = topnum;
	}
	public String getReadtopnum() {
		return readtopnum;
	}
	public void setReadtopnum(String readtopnum) {
		this.readtopnum = readtopnum;
	}
	
	
}
