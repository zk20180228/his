package cn.honry.migrate.synDateManage.action;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import cn.honry.base.bean.model.IDataSynch;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.synDateManage.service.SynDateService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**
 * 
 * 
 * <p>数据同步管理</p>
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
@Namespace("/migrate/SynDateManager")
public class SynDateAction extends ActionSupport implements ModelDriven<IDataSynch> {
	private static final long serialVersionUID = -12575988050794941L;
	private Logger logger=Logger.getLogger(SynDateAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	private String menuAlias;
	private String id;
	private IDataSynch dataSynch=new IDataSynch();
	private String queryCode;
	private String rows;
	private String page;
	private String dateState;//状态
	
	@Autowired
	@Qualifier(value="synDateService")
	private SynDateService synDateService;
	
	public void setSynDateService(SynDateService synDateService) {
		this.synDateService = synDateService;
	}
	
	public IDataSynch getDataSynch() {
		return dataSynch;
	}

	public void setDataSynch(IDataSynch dataSynch) {
		this.dataSynch = dataSynch;
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

	public String getDateState() {
		return dateState;
	}

	public void setDateState(String dateState) {
		this.dateState = dateState;
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
	@Override
	public IDataSynch getModel() {
		return dataSynch;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 
	 * 
	 * <p>跳转到数据同步管理页面</p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月20日 下午4:44:59 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月20日 下午4:44:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"SSTBGL:function:view"})
	@Action(value="showSynDate" ,results={@Result(name="list",location = "/WEB-INF/pages/migrate/synDateManage/synDateManager.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
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
	@RequiresPermissions(value={"SSTBGL:function:add"})
	@Action(value="addSynDate" ,results={@Result(name="list",location = "/WEB-INF/pages/migrate/synDateManage/synDateManagerEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String addSynDate(){
		if(StringUtils.isNotBlank(queryCode)){
			dataSynch=synDateService.getOneDate(queryCode);
			if(dataSynch!=null){
				try {
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
					if(dataSynch.getNewestTime()!=null){
						
							dataSynch.setNewesTimeStr(df.format(dataSynch.getNewestTime()));
						
					}
					if(dataSynch.getDefaTime()!=null){
							dataSynch.setDefaTimeStr(df.format(dataSynch.getDefaTime()));
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("CSJKGL_SSTBGL", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_SSTBGL", "厂商接口管理_数据同步管理", "2", "0"), e);
				}
			}
			
		}
		
		return "list";
	}
	/**
	 * 
	 * 
	 * <p>跳转到展示页面 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月28日 下午2:07:21 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月28日 下午2:07:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"SSTBGL:function:view"})
	@Action(value="viewSynDate" ,results={@Result(name="list",location = "/WEB-INF/pages/migrate/synDateManage/synDateManagerView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String viewSynDate(){
		if(StringUtils.isNotBlank(queryCode)){
			dataSynch=synDateService.getOneDate(queryCode);
			if(dataSynch!=null){
				try {
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
					if(dataSynch.getNewestTime()!=null){
							dataSynch.setNewesTimeStr(df.format(dataSynch.getNewestTime()));
					}
					if(dataSynch.getDefaTime()!=null){
							dataSynch.setDefaTimeStr(df.format(dataSynch.getDefaTime()));
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("CSJKGL_SSTBGL", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_SSTBGL", "厂商接口管理_数据同步管理", "2", "0"), e);
				}
			}
		}
		return "list";
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
	@RequiresPermissions(value={"SSTBGL:function:add"})
	@Action(value="editSynDate")
	public void editSynCh(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			synDateService.saveOrUpdateSynDate(dataSynch);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			e.printStackTrace();
			logger.error("CSJKGL_SSTBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_SSTBGL", "厂商接口管理_数据同步管理", "2", "0"), e);
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
	@RequiresPermissions(value={"SSTBGL:function:add"})
	@Action(value="delSynDate")
	public void delSynDate(){
		Map<String,String> map=new HashMap<String,String>();
		try {
			synDateService.delSynDate(queryCode);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("CSJKGL_SSTBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_SSTBGL", "厂商接口管理_数据同步管理", "2", "0"), e);
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
	@Action(value="querySynDate")
	public void querySynDate(){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			List<IDataSynch> list=synDateService.querySynDate(queryCode, rows, page, menuAlias,dateState);
			Integer total=synDateService.querySynDateTotal(queryCode, menuAlias,dateState);
			map.put("rows", list);
			map.put("total", total);
		}catch(Exception e){
			map.put("rows", new ArrayList<IDataSynch>());
			map.put("total", 0);
			e.printStackTrace();
			logger.error("CSJKGL_SSTBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_SSTBGL", "厂商接口管理_数据同步管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendJSON(json);
	}
	@Action(value="updateState")
	public void updateState(){
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			
			synDateService.updateState(dataSynch.getId(), dataSynch.getState().toString());
			if(dataSynch==null){
				throw new Exception();
			}
			String tip=null;
			if(dataSynch.getState()==null){
				throw new Exception("数据同步状态转换失败");
			}
			if("1".equals(dataSynch.getState().toString())){
				tip="停用";
			}else{
				tip="启用";
			}
			map.put("resCode", "success");
			map.put("resMsg", tip+"成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "修改失败！");
			e.printStackTrace();
			logger.error("CSJKGL_SSTBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_SSTBGL", "厂商接口管理_数据同步管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss");
		WebUtils.webSendJSON(json);
	}
}
