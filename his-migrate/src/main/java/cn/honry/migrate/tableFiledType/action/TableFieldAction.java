package cn.honry.migrate.tableFiledType.action;

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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.TableFiledType;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.tableFiledType.service.TableFieldService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**
 * 
 * 
 * <p>表字段类型管理</p>
 * @Author: XCL
 * @CreateDate: 2017年9月19日 下午2:53:44 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月19日 下午2:53:44 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/migrate/tableField")
public class TableFieldAction extends ActionSupport  {
	private static final long serialVersionUID = -12575988050794941L;
	private Logger logger=Logger.getLogger(TableFieldAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	private String menuAlias;
	private String id;
	private TableFiledType tableFiled=new TableFiledType();
	private String queryCode;
	private String rows;
	private String page;
	@Autowired
	@Qualifier(value="tableFieldService")
	private TableFieldService tableFieldService;

	public void setTableFieldService(TableFieldService tableFieldService) {
		this.tableFieldService = tableFieldService;
	}

	public TableFiledType getTableFiled() {
		return tableFiled;
	}

	public void setTableFiled(TableFiledType tableFiled) {
		this.tableFiled = tableFiled;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 
	 * 
	 * <p>跳转到表字段类型管理页面</p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:44:59 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:44:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"BZDLX:function:view"})
	@Action(value="showTableField" ,results={@Result(name="list",location = "/WEB-INF/pages/migrate/tableFiled/tableFiledList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String showSynDate(){
		
		return "list";
	}
	/**
	 * 
	 * 
	 * <p> 跳转到添加修改页面</p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:45:27 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:45:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"BZDLX:function:add"})
	@Action(value="addTableField" ,results={@Result(name="list",location = "/WEB-INF/pages/migrate/tableFiled/tableFiledEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String addSynDate(){
		if(StringUtils.isNotBlank(queryCode)){
			tableFiled=tableFieldService.getOneDate(queryCode);
		}
		return "list";
	}
	/**
	 * 
	 * 
	 * <p> 跳转到添加修改页面</p>
	 * @Author: yuke
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"BZDLX:function:add"})
	@Action(value="viewTableField" ,results={@Result(name="view",location = "/WEB-INF/pages/migrate/tableFiled/tableFiledView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String viewTableField(){
		if(StringUtils.isNotBlank(queryCode)){
			tableFiled=tableFieldService.getOneDate(queryCode);
		}
		return "view";
	}
	/**
	 * 
	 * 
	 * <p>添加或修改 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:45:41 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:45:41 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@RequiresPermissions(value={"BZDLX:function:add"})
	@Action(value="editTableField")
	public void editSynCh(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			tableFieldService.saveOrUpdateTableField(tableFiled);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			e.printStackTrace();
			logger.error("CSJKGL_BZDLX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_BZDLX", "厂商接口管理_表字段类型管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>删除  </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:45:58 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:45:58 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@RequiresPermissions(value={"BZDLX:function:add"})
	@Action(value="delTableField")
	public void delSynDate(){
		Map<String,String> map=new HashMap<String,String>();
		try {
			tableFieldService.delTableField(id);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("CSJKGL_BZDLX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_BZDLX", "厂商接口管理_表字段类型管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>查询 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:46:12 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:46:12 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="queryTableField")
	public void querySynDate(){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			List<TableFiledType> list=tableFieldService.queryTableField(queryCode, rows, page, menuAlias);
			Integer total=tableFieldService.queryTableFieldTotal(queryCode, menuAlias);
			map.put("rows", list);
			map.put("total", total);
		}catch(Exception e){
			map.put("rows", new ArrayList<TableFiledType>());
			map.put("total", 0);
			e.printStackTrace();
			logger.error("CSJKGL_BZDLX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_BZDLX", "厂商接口管理_表字段类型管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendJSON(json);
	}
}
