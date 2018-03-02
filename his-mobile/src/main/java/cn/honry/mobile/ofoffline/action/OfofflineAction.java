package cn.honry.mobile.ofoffline.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.MOfoffline;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.ofoffline.service.OfofflineService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/ofoffline")
public class OfofflineAction {

private Logger logger=Logger.getLogger(OfofflineAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "ofofflineService")
	private OfofflineService ofofflineService;
	public void setOfofflineService(OfofflineService ofofflineService) {
		this.ofofflineService = ofofflineService;
	}
	
	private String menuAlias;
	/**
	 * 查询参数
	 * */
	private String queryName;
	/**
	 * 分页页数
	 */
	private String page;
	/**
	 * 分页行数
	 */
	private String rows;
	/**
	 * id.多个用逗号分隔
	 */
	private String ids;
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**  
	 * 
	 * 跳转至移动端图标维护页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"LXGL:function:view"})
	@Action(value = "ofofflineList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/ofoffline/ofofflineList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  ofofflineList(){
		return "list";
	}
	
	
	/**  
	 * 
	 * 列表数据
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:43:56 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月21日 上午10:43:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"LXGL:function:query"})
	@Action(value = "findOfofflineList")
	public void findOfofflineList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<MOfoffline> list = ofofflineService.getOfofflineList(rows, page, queryName);
			Integer total = ofofflineService.getOfofflineCount(queryName);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("LXGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LXGL","离线管理","2","0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	
	/** 删除离线信息
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"LXGL:function:del"})
	@Action(value = "delOfoffline")
	public void delOfoffline(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			ofofflineService.delOfoffline(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("LMTBWH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LMTBWH","栏目图片维护","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
