package cn.honry.assets.assetsScrap.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.assets.assetsPurchase.action.AssetsPurchaseAction;
import cn.honry.assets.assetsScrap.service.AssetsScrapService;
import cn.honry.base.bean.model.AssetsDeviceScrap;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
@Namespace(value="/assets/assetsScrap")
public class AssetsScrapAction extends ActionSupport implements ModelDriven<AssetsDeviceScrap> {
	private static final long serialVersionUID = 1L;
	AssetsDeviceScrap assetsDeviceScrap = new AssetsDeviceScrap();
	private Logger logger=Logger.getLogger(AssetsPurchaseAction.class);
	private String page;
	private String rows;
	@Override
	public AssetsDeviceScrap getModel() {
		return assetsDeviceScrap;
	}
	@Autowired
	@Qualifier(value = "assetsScrapService")
	private AssetsScrapService assetsScrapService;
	public void setAssetsScrapService(AssetsScrapService assetsScrapService) {
		this.assetsScrapService = assetsScrapService;
	}

	public AssetsDeviceScrap getAssetsDeviceScrap() {
		return assetsDeviceScrap;
	}

	public void setAssetsDeviceScrap(AssetsDeviceScrap assetsDeviceScrap) {
		this.assetsDeviceScrap = assetsDeviceScrap;
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
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse  response =ServletActionContext.getResponse();
	@Action(value = "assetsScrap", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsScrap/assetsScrap.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String assetsScrap() {
		return "list";
	}
	@Action(value = "queryAssetsScrap")
	public void queryAssetsScrap() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String state = request.getParameter("state");
			String officeName = request.getParameter("officeName");
			String className = request.getParameter("className");
			String deviceCode = request.getParameter("deviceCode");
			String deviceName = request.getParameter("deviceName");
			AssetsDeviceScrap assets = new AssetsDeviceScrap();
			assets.setOfficeName(officeName);
			assets.setClassName(className);
			assets.setDeviceCode(deviceCode);
			assets.setDeviceName(deviceName);
			List<AssetsDeviceScrap> assetsList = assetsScrapService.queryRepairRecode(page,rows,assets);
			int total = assetsScrapService.getTotalList(page,rows,assets);
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
}
