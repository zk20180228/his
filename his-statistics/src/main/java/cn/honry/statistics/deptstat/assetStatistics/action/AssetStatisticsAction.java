package cn.honry.statistics.deptstat.assetStatistics.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.payloads.IntegerEncoder;
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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceUse;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.assetStatistics.service.AssetStatisticsService;
import cn.honry.statistics.deptstat.assetStatistics.vo.AssetsDeviceVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/assetStatistics")
@SuppressWarnings({"all"})
public class AssetStatisticsAction extends ActionSupport{
	private static final long serialVersionUID = 8535596438329082479L;
	private Logger logger=Logger.getLogger(AssetStatisticsAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "assetStatisticsService")
	private AssetStatisticsService assetStatisticsService;
	public AssetStatisticsService getAssetStatisticsService() {
		return assetStatisticsService;
	}
	public void setAssetStatisticsService(
			AssetStatisticsService assetStatisticsService) {
		this.assetStatisticsService = assetStatisticsService;
	}
	private String officeName;//办公用途
	private String className;//设备分类
	private String classCode;//类别代码
	private String deviceName;//设备名称
	private String deptCode;//部门
	private String page;//当前页数
	private String rows;//分页条数
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
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
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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
	 * 资产统计分析管理
	 * @return
	 */
	@RequiresPermissions(value={"ZCTJFXGL:function:view"})
	@Action(value = "assetStatisticsList", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetStatistics/assetStatistics.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String assetStatisticsList() {
		return "list";
	}
	/**  
	 * 
	 * 资产分类
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"ZCTJFXGL:function:query"})
	@Action(value = "querylistAssetsDevice", results = { @Result(name = "json",type = "json") })
	public void querylistAssetsDevice(){
		try {
			Integer totalType=0;//资产分类总计
			List<AssetsDevice> list = assetStatisticsService.queryAssetsDevice(officeName, className, classCode, deviceName, page, rows);
			for (AssetsDevice vo : list) {
				totalType+=vo.getDeviceNum();
			}
			int total=assetStatisticsService.queryAssetsDeviceTotal(officeName, className, classCode, deviceName);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			map.put("totalType", totalType);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBZCGL_ZCTJFXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_ZCTJFXGL", "医疗设备资产管理_资产统计分析管理", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 领用部门
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"ZCTJFXGL:function:query"})
	@Action(value = "queryAssetsDeviceUsec", results = { @Result(name = "json",type = "json") })
	public void queryAssetsDeviceUse(){
		try {
			List<AssetsDeviceUse> list = assetStatisticsService.queryAssetsDeviceUse(deptCode, page, rows);
			int total=assetStatisticsService.queryAssetsDeviceUseTotal(deptCode);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBZCGL_ZCTJFXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_ZCTJFXGL", "医疗设备资产管理_资产统计分析管理", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 资产价值
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@RequiresPermissions(value={"ZCTJFXGL:function:query"})
	@Action(value = "queryAssetsDeviceValue", results = { @Result(name = "json",type = "json") })
	public void queryAssetsDeviceValue(){
		try {
			Double usedValue=0.0;//资产原值
			Double newValue=0.0;//资产现值
			List<AssetsDeviceVo> list = assetStatisticsService.queryAssetsDeviceValue(officeName, className, classCode, deviceName, page, rows);
			List<AssetsDeviceVo> voList = assetStatisticsService.queryAssetsDeviceValue(officeName, className, classCode, deviceName, null, null);
			for (AssetsDeviceVo vo : voList) {
				usedValue+=vo.getPurchTotal();
				newValue+=vo.getNewValue();
			}
			int total=assetStatisticsService.queryAssetsDeviceValueTotal(officeName, className, classCode, deviceName);
			Map<String,Object> map = new HashMap<String, Object>();
			String used = NumberUtil.init().format(usedValue, 2);
			String newV = NumberUtil.init().format(newValue, 2);
			map.put("total", total);
			map.put("rows", list);
			map.put("used", used);
			map.put("newV", newV);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBZCGL_ZCTJFXGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBZCGL_ZCTJFXGL", "医疗设备资产管理_资产统计分析管理", "2", "0"), e);
		}
	}
}
