package cn.honry.migrate.firmMainTain.action;

import java.util.Date;
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

import cn.honry.base.bean.model.FirmMainTain;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.migrate.firmMainTain.service.FirmMainTainService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/migrate/firmMainTain")
@SuppressWarnings({"all"})	
public class FirmMainTainAction extends ActionSupport{
	private FirmMainTainService firmMainTainService;
	@Autowired
	@Qualifier(value = "firmMainTainService")
	public void setFirmMainTainService(
			FirmMainTainService firmMainTainService) {
		this.firmMainTainService = firmMainTainService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private Logger logger=Logger.getLogger(FirmMainTainAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	private String userId;
	private String userPas;
	private String page;
	private String rows;
	private String code;
	private String menuAlias;
	private FirmMainTain firmMainTain=new FirmMainTain();
	
	public String getUserPas() {
		return userPas;
	}
	public void setUserPas(String userPas) {
		this.userPas = userPas;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public FirmMainTain getFirmMainTain() {
		return firmMainTain;
	}
	public void setFirmMainTain(FirmMainTain firmMainTain) {
		this.firmMainTain = firmMainTain;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 跳转到厂商维护页面
	 * @return
	 */
	@RequiresPermissions(value={"CSWH:function:view"})
	@Action(value = "listFirmMainTain", results = { @Result(name = "list", location = "/WEB-INF/pages/migrate/firmMainTain/firmMainTain.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listFirmMainTain() {
		return "list";
	}
	/**
	 * 
	 * 
	 * <p>弹出修改密码 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 下午8:11:56 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 下午8:11:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	@RequiresPermissions(value={"CSWH:function:edit"})
	@Action(value = "resPassword", results = { @Result(name = "list", location = "/WEB-INF/pages/migrate/firmMainTain/firmResetPas.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String resPassword() {
		return "list";
	}
	/**  
	 * 厂商维护列表(查询)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	
	@Action(value="queryFirmMainTain")
	public void queryFirmMainTain(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<FirmMainTain> list = firmMainTainService.queryFirmMainTain(code,page,rows,menuAlias);
			int total = firmMainTainService.queryTotal(code);
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("CSJKGL_CSWH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_CSWH", "厂商接口管理_厂商维护", "2", "0"), e);
		}
	}
	/**  
	 * 厂商维护 (跳转到添加页面)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@RequiresPermissions(value={"CSWH:function:add"}) 
	@Action(value = "addFirmMainTain", results = { @Result(name = "view", location = "/WEB-INF/pages/migrate/firmMainTain/firmMainTainEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addFirmMainTain() {
		String	time = null;
		if(StringUtils.isNotBlank(code)){
			firmMainTain=firmMainTainService.getOnedata(code);
			if(firmMainTain.getCreateTime()==null){
				time = DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				time = DateUtils.formatDateY_M_D_H_M_S(firmMainTain.getCreateTime());
			}
		}else{
			time = DateUtils.formatDateY_M_D_H_M_S(new Date());
		}
		ServletActionContext.getRequest().setAttribute("firmMainTain", firmMainTain);
		ServletActionContext.getRequest().setAttribute("createTime", time);
		return "view";
	}
	/**  
	 * 厂商维护 (跳转到展示页面)
	 * @Author: yuke
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@RequiresPermissions(value={"CSWH:function:view"}) 
	@Action(value = "viewFirmMainTain", results = { @Result(name = "view", location = "/WEB-INF/pages/migrate/firmMainTain/firmMainTainView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewFirmMainTain() {
		String	time = null;
		if(StringUtils.isNotBlank(code)){
			firmMainTain=firmMainTainService.getOnedata(code);
			if(firmMainTain.getCreateTime()==null){
				time = DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				time = DateUtils.formatDateY_M_D_H_M_S(firmMainTain.getCreateTime());
			}
		}else{
			time = DateUtils.formatDateY_M_D_H_M_S(new Date());
		}
		ServletActionContext.getRequest().setAttribute("firmMainTain", firmMainTain);
		ServletActionContext.getRequest().setAttribute("createTime", time);
		return "view";
	}
	/**  
	 * 厂商维护 (添加/修改)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@RequiresPermissions(value={"CSWH:function:add"})
	@Action(value="saveFirmMainTain")
	public void saveFirmMainTain(){
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			firmMainTainService.saveFirmMainTain(firmMainTain);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 厂商维护 (删除)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value="delFirmMainTain",results={@Result(name="json",type="json")})
	public void delFirmMainTain () throws Exception{
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			firmMainTainService.delFirmMainTain(code);
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
			logger.error("CSJKGL_CSWH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_CSWH", "厂商接口管理_厂商维护", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	
	@RequiresPermissions(value={"CSWH:function:edit"})
	@Action(value="updatePassword")
	public void updatePassword (){
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			firmMainTainService.updatePasswor(userId, userPas);
			resMap.put("resCode", "success");
			resMap.put("resMsg", "修改密码成功!");
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "修改密码失败!");
			logger.error("CSJKGL_CSWH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CSJKGL_CSWH", "厂商接口管理_厂商维护", "2", "0"), e);
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * 
	 * <p>渲染厂商 </p>
	 * @CreateDate: 2017年9月26日 下午6:46:45 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "renderFirm")
	public void renderFirm(){
		List<FirmMainTain> list = firmMainTainService.queryFirm();
		Map<String,String> serverMap=new HashMap<String,String>();
		for(FirmMainTain vo:list){
			serverMap.put(vo.getFirmCode(), vo.getFirmName());
		}
		String json = JSONUtils.toJson(serverMap);
		WebUtils.webSendJSON(json);
	}
}