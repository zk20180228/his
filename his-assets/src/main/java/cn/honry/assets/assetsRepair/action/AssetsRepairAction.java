package cn.honry.assets.assetsRepair.action;

import java.util.ArrayList;
import java.util.HashMap;
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

import cn.honry.assets.assetsPurchase.action.AssetsPurchaseAction;
import cn.honry.assets.assetsRepair.service.AssetsRepairService;
import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
@Namespace(value="/assets/assetsRepair")
public class AssetsRepairAction extends ActionSupport implements ModelDriven<AssetsDeviceMaintain> {
	private static final long serialVersionUID = 1L;
	AssetsDeviceMaintain assetsDeviceMaintain = new AssetsDeviceMaintain();
	private Logger logger=Logger.getLogger(AssetsPurchaseAction.class);
	private String page;
	private String rows;
	@Override
	public AssetsDeviceMaintain getModel() {
		return assetsDeviceMaintain;
	}
	@Autowired
	@Qualifier(value = "assetsRepairService")
	private AssetsRepairService assetsRepairService;
	
	public void setAssetsRepairService(AssetsRepairService assetsRepairService) {
		this.assetsRepairService = assetsRepairService;
	}
	

	public AssetsDeviceMaintain getAssetsDeviceMaintain() {
		return assetsDeviceMaintain;
	}


	public void setAssetsDeviceMaintain(AssetsDeviceMaintain assetsDeviceMaintain) {
		this.assetsDeviceMaintain = assetsDeviceMaintain;
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
	@Action(value = "assetsRepair", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsRepair/assetsRepair.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String assetsRepair() {
		return "list";
	}
	@Action(value = "queryAssetsRepair")
	public void queryAssetsRepair() {
		String state = request.getParameter("state");
		String officeName = request.getParameter("officeName");
		String className = request.getParameter("className");
		String deviceCode = request.getParameter("deviceCode");
		String deviceName = request.getParameter("deviceName");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		AssetsDeviceMaintain assets = new AssetsDeviceMaintain();
		assets.setOfficeName(officeName);
		assets.setClassName(className);
		assets.setDeviceCode(deviceCode);
		assets.setDeviceName(deviceName);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<AssetsDeviceMaintain> assetsList = assetsRepairService.queryAssetsRepair(page,rows,assets);
			int total = assetsRepairService.queryTotalRepair(page,rows,assets);
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
	@Action(value = "repairRecord", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsRepair/assetsRepairRecord.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String repairRecod() {
		String deviceNo = request.getParameter("deviceNo");
		String state = request.getParameter("state");
		assetsDeviceMaintain.setDeviceNo(deviceNo);
		assetsDeviceMaintain.setState(state);
		return "list";
	}
	
	@Action(value = "queryRepairRecode")
	public void queryRepairRecode() {
		String state = request.getParameter("state");
		String deviceNo = request.getParameter("deviceNo");
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isNotBlank(deviceNo)){
				String page = request.getParameter("page");
				String rows = request.getParameter("rows");
				List<AssetsDeviceMaintain> assetsList = assetsRepairService.queryRepairRecode(page,rows,state,deviceNo);
				int total = assetsRepairService.getTotalList(page,rows,state,deviceNo);
				map.put("total", total);
				map.put("rows", assetsList);
			}else{
				map.put("total", 0);
				map.put("rows", new ArrayList<AssetsPurchplan>());
			}
			
			
		} catch (Exception e) {
			logger.info(e);
			map.put("total", 0);
			map.put("rows", new ArrayList<AssetsPurchplan>());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
}
