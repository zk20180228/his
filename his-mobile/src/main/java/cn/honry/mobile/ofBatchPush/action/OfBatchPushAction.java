package cn.honry.mobile.ofBatchPush.action;

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

import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.ofBatchPush.service.OfBatchPushService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/ofBatchPush")
public class OfBatchPushAction {
	
	private Logger logger=Logger.getLogger(OfBatchPushAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	@Autowired
	@Qualifier(value = "ofBatchPushService")
	private OfBatchPushService ofBatchPushService;
	public void setOfBatchPushService(OfBatchPushService ofBatchPushService) {
		this.ofBatchPushService = ofBatchPushService;
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
	 * 跳转至广播页面
	 * @Author: zxl
	 * @CreateDate: 2017年7月17日 下午7:44:03 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月17日 下午7:44:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: String 返回值类型
	 *
	 */
	@RequiresPermissions(value={"GBGL:function:view"})
	@Action(value = "ofBatchPushList", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/ofBatchPush/ofBatchPushList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String  ofBatchPushList(){
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
	@RequiresPermissions(value={"GBGL:function:query"})
	@Action(value = "findOfBatchPushList")
	public void findOfBatchPushList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<OFBatchPush> list = ofBatchPushService.getOfBatchPushList(rows, page, queryName);
			Integer total = ofBatchPushService.getOfBatchPushCount(queryName);
			map.put("total", total);
			map.put("rows", list);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GBGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("GBGL","广播管理","2","0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	
	/** 删除广播信息
	* @param request
	* @param response
	* @param ids 要删除的id字符串，以逗号分割
	* @author zxl 
	* @date 2017年6月20日
	*/
	@RequiresPermissions(value={"GBGL:function:del"})
	@Action(value = "delOfBatchPush")
	public void delOfBatchPush(){
		Map<String, String> map = new HashMap<String, String>();
		try {
			ofBatchPushService.delOfBatchPush(ids);
			map.put("resCode", "0");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			map.put("resCode", "1");
			map.put("resMsg", "删除失败！");
			e.printStackTrace();
			logger.error("GBGL", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("GBGL","广播管理","2","1"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	

}
