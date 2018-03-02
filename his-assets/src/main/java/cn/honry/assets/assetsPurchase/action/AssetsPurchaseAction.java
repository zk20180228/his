package cn.honry.assets.assetsPurchase.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.opensymphony.xwork2.ModelDriven;

import cn.honry.assets.assetsPurchase.service.AssetsPurchaseService;
import cn.honry.assets.deviceseRvice.service.DeviceseRviceService;
import cn.honry.base.bean.model.AssetsDeviceseRvice;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
@Namespace(value="/assets/assetsPurchase")
public class AssetsPurchaseAction extends ActionSupport implements ModelDriven<AssetsPurchplan> {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(AssetsPurchaseAction.class);
	private AssetsPurchplan assetsPurchplan=new AssetsPurchplan();
	private String page;
	private String rows;
	@Override
	public AssetsPurchplan getModel() {
		return assetsPurchplan;
	}
	@Autowired
	@Qualifier(value = "assetsPurchaseService")
	private AssetsPurchaseService assetsPurchaseService;
	public void setAssetsPurchaseService(AssetsPurchaseService assetsPurchaseService) {
		this.assetsPurchaseService = assetsPurchaseService;
	}
	@Autowired
	@Qualifier(value = "deviceseRviceService")
	private DeviceseRviceService deviceseRviceService;

	public void setDeviceseRviceService(DeviceseRviceService deviceseRviceService) {
		this.deviceseRviceService = deviceseRviceService;
	}
	public AssetsPurchplan getAssetsPurchplan() {
		return assetsPurchplan;
	}
	public void setAssetsPurchplan(AssetsPurchplan assetsPurchplan) {
		this.assetsPurchplan = assetsPurchplan;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse  response =ServletActionContext.getResponse();
	@Action(value = "assetsApply", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange() {
		return "list";
	}
	@Action(value = "assetsApplyAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsApplyAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange1() {
		return "list";
	}
	@Action(value = "assetsApproval", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsApproval.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange2() {
		return "list";
	}
	@Action(value = "assetsPlanStat", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsPlanStat.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange3() {
		return "list";
	}
	@Action(value = "editAssets", results = { @Result(name = "edit", location = "/WEB-INF/pages/assets/assetsApply/assetsApplyAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editAssets() {
		assetsPurchplan = assetsPurchaseService.get(assetsPurchplan.getId());
		return "edit";
	}
	@Action(value = "delEmployee", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delEmployee()  {
		try {
			assetsPurchaseService.del(assetsPurchplan.getId());
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZZJGGL_YYYGGL", e);
		}
		return "list";
	}
	@Action(value = "saveOrUpdateAssets", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsApply.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void saveOrUpdateAssets(){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			assetsPurchaseService.saveOrUpdateeditInfo(assetsPurchplan);
			map.put("resCode","success");
			map.put("resMsg","保存成功！");
		} catch (Exception e) {
			map.put("resCode","error");
			map.put("resMsg","保存失败");
			logger.error("saveOrUpdateAssets", e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action(value = "queryAllAssets")
	public void queryAllAssets() {
		String state = request.getParameter("state");
		String officeName = request.getParameter("officeName");
		String className = request.getParameter("className");
		String classCode = request.getParameter("classCode");
		String deviceName = request.getParameter("deviceName");
		String applState = request.getParameter("applState");
		String stopFlag = request.getParameter("stop_flg");
		AssetsPurchplan assets = new AssetsPurchplan();
		assets.setOfficeName(officeName);
		assets.setClassName(className);
		assets.setClassCode(classCode);
		assets.setDeviceName(deviceName);
		if(StringUtils.isNotBlank(applState)){
			assets.setApplState(Integer.parseInt(applState));
		}
		if(StringUtils.isNotBlank(stopFlag)){
			assets.setStop_flg(Integer.parseInt(stopFlag));
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<AssetsPurchplan> assetsList = assetsPurchaseService.queryAllAssetsByData(page,rows,assets,state);
			int total = assetsPurchaseService.getTotalList(page,rows,assets,state);
			map.put("total", total);
			map.put("rows", assetsList);
			
		} catch (Exception e) {
			logger.info(e);
			map.put("total", 0);
			map.put("rows", new ArrayList<AssetsPurchplan>());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "querySPAssets")
	public void querySPAssets() {
		String state = request.getParameter("state");
		String officeName = request.getParameter("officeName");
		String className = request.getParameter("className");
		String classCode = request.getParameter("classCode");
		String deviceName = request.getParameter("deviceName");
		String applState = request.getParameter("applState");
		String timeBegin = request.getParameter("timeBegin");
		String timeEnd = request.getParameter("timeEnd");
		AssetsPurchplan assets = new AssetsPurchplan();
		assets.setOfficeName(officeName);
		assets.setClassName(className);
		assets.setClassCode(classCode);
		assets.setDeviceName(deviceName);
		if(StringUtils.isNotBlank(applState)){
			assets.setApplState(Integer.parseInt(applState));
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map = assetsPurchaseService.querySPAssetsByData(page,rows,assets,state,timeBegin,timeEnd);
		} catch (Exception e) {
			logger.error("saveOrUpdateAssets", e);
			map.put("total", 0);
			map.put("rows", new ArrayList<AssetsPurchplan>());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "stopAssets")
	public void stopAssets() {
		String flag = request.getParameter("flag");
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			assetsPurchaseService.stopAssets(id,flag);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "passAssets")
	public void passAssets() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			assetsPurchaseService.passAssets(id);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			logger.error(e);
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "noPassAssets")
	public void noPassAssets() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=utf-8");
			String id = request.getParameter("id");
			String reason = request.getParameter("reason");
			reason = URLDecoder.decode(reason, "utf-8");// url解码
			assetsPurchaseService.noPassAssets(id,reason);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			logger.error(e);
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "seeReason")
	public void seeReason() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			AssetsPurchplan assets =assetsPurchaseService.seeReason(id);
			map.put("resCode", "success");
			if(assets!=null){
				map.put("resMsg", assets.getReason());
			}else{
				map.put("resMsg","没有该信息");
			}
		} catch (Exception e) {
			logger.error(e);
			map.put("resCode", "error");
			map.put("resMsg","操作失败");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "delAssets", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/employee/employeeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delAssets()  {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			assetsPurchaseService.del(id);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			logger.error(e);
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		return "list";
	}
	@Action(value = "delAssetsed", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsApply/assetsApproval.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delAssetsed()  {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			assetsPurchaseService.del(id);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		} catch (Exception e) {
			logger.error(e);
			map.put("resCode", "error");
			map.put("resMsg", "操作失败!");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		return "list";
	}
	@Action(value = "queryAllAssetsStat")
	public void queryAllAssetsStat() {
		String officeName = request.getParameter("officeName");
		String className = request.getParameter("className");
		String classCode = request.getParameter("classCode");
		String deviceName = request.getParameter("deviceName");
		AssetsPurchplan assets = new AssetsPurchplan();
		assets.setOfficeName(officeName);
		assets.setClassName(className);
		assets.setClassCode(classCode);
		assets.setDeviceName(deviceName);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<AssetsPurchplan> assetsList = assetsPurchaseService.queryAllAssetsStat(page,rows,assets);
			int total = assetsPurchaseService.getTotalListStat(assets);
			map.put("total", total);
			map.put("rows", assetsList);
			
		} catch (Exception e) {
			logger.info(e);
			map.put("total", 0);
			map.put("rows", new ArrayList<AssetsPurchplan>());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	@Action(value = "queryAssetsDeviceseRvice",results={@Result(name="json",type="json")})
	public void queryAssetsDeviceseRvice(){
		AssetsDeviceseRvice deviceseRvice = new AssetsDeviceseRvice();
		String name = request.getParameter("q");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		deviceseRvice.setPage(page);
		deviceseRvice.setRows(rows);
		deviceseRvice.setOfficeName(name);
		deviceseRvice.setClassName(name);
		deviceseRvice.setDeviceName(name);//用设备名称来查询可以精确到一个具体的设备方便选择
		List<AssetsDeviceseRvice> list = deviceseRviceService.queryDeviceseRviceForjsp(deviceseRvice);
		int total = deviceseRviceService.getDeviceseRviceCountForjsp(deviceseRvice);
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
