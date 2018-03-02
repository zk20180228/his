package cn.honry.assets.assetsPurch.action;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import cn.honry.assets.assetsPurch.service.AssetsPurchService;
import cn.honry.assets.assetsPurch.vo.AssetsPurchVo;
import cn.honry.assets.assetsPurchase.service.AssetsPurchaseService;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
@Namespace(value="/assets/assetsPurch")
public class AssetsPurchAction extends ActionSupport implements ModelDriven<AssetsPurch>{
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(AssetsPurchAction.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse  response =ServletActionContext.getResponse();
	private AssetsPurch assetsPurch=new AssetsPurch();
	@Override
	public AssetsPurch getModel() {
		return assetsPurch;
	}
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	private String overNum="";//剩余采购数量
	private String startTime;//开始时间
	private String endTime;//结束时间
	@Autowired
	@Qualifier(value = "assetsPurchService")
	private AssetsPurchService assetsPurchService;
	public AssetsPurchService getAssetsPurchService() {
		return assetsPurchService;
	}
	public void setAssetsPurchService(AssetsPurchService assetsPurchService) {
		this.assetsPurchService = assetsPurchService;
	}
	@Autowired
	@Qualifier(value = "assetsPurchaseService")
	private AssetsPurchaseService assetsPurchaseService;
	
	public AssetsPurchaseService getAssetsPurchaseService() {
		return assetsPurchaseService;
	}
	public void setAssetsPurchaseService(AssetsPurchaseService assetsPurchaseService) {
		this.assetsPurchaseService = assetsPurchaseService;
	}
	public AssetsPurch getAssetsPurch() {
		return assetsPurch;
	}
	public void setAssetsPurch(AssetsPurch assetsPurch) {
		this.assetsPurch= assetsPurch;
	}
	public String getOverNum() {
		return overNum;
	}
	public void setOverNum(String overNum) {
		this.overNum = overNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	//设备采购申报
	@Action(value = "assetsPurch", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsPurch/assetsPurch.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange() {
		return "list";
	}
	//设备采购申报--添加页面
	@Action(value = "assetsPurchAdd", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsPurch/assetsPurchAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange1() {
		try {
			String row = new String(request.getParameter("row").getBytes("ISO8859-1"),"UTF-8");
			AssetsPurchVo vo = JSONUtils.fromJson(row,AssetsPurchVo.class);
			assetsPurch.setOfficeCode(vo.getOfficeCode());
			assetsPurch.setOfficeName(vo.getOfficeName());
			assetsPurch.setClassCode(vo.getClassCode());
			assetsPurch.setClassName(vo.getClassName());
			assetsPurch.setDeviceCode(vo.getDeviceCode());
			assetsPurch.setDeviceName(vo.getDeviceName());
			assetsPurch.setMeterUnit(vo.getMeterUnit());
			assetsPurch.setPurchPrice(vo.getPlanPrice());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	//设备采购审批页面
	@Action(value = "assetsPurchApproval", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsPurch/assetsPurchApproval.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listChange2() {
		// 获取当月第一天至当天时间段
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
	}
	//修改草稿页面
	@Action(value = "editAssets", results = { @Result(name = "edit", location = "/WEB-INF/pages/assets/assetsPurch/assetsPurchAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editAssets() {
		assetsPurch = assetsPurchService.get(assetsPurch.getId());
		int purch = assetsPurchService.queryPlan(assetsPurch);
		int queryPlan = assetsPurchaseService.queryPlan(assetsPurch);
		overNum=(queryPlan-purch)+"";
		return "edit";
	}
	/**  
	 * 设备采购申报--提交申请（保存）
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "saveOrUpdateAssets", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsPurch/assetsPurch.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void saveOrUpdateAssets(){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			assetsPurchService.saveOrUpdateeditInfo(assetsPurch);
			map.put("resCode","success");
			map.put("resMsg","保存成功！");
		} catch (Exception e) {
			map.put("resCode","error");
			map.put("resMsg","保存失败");
			logger.error("saveOrUpdateAssets", e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * 设备采购申报--采购计划列表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "queryPurchPlan")
	public void queryPurchPlan() {
		try {
			String stated = request.getParameter("stated");
			AssetsPurchplan assets = new AssetsPurchplan();
			assets.setOfficeName(assetsPurch.getOfficeName());
			assets.setClassName(assetsPurch.getClassName());
			assets.setClassCode(assetsPurch.getClassCode());
			assets.setDeviceName(assetsPurch.getDeviceName());
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				List<AssetsPurchVo> assetsList = assetsPurchService.queryPurchPlan(assetsPurch.getPage(),assetsPurch.getRows(),assets,stated);
				int total = assetsPurchService.getTotalPlan(assets,stated);
				map.put("total", total);
				map.put("rows", assetsList);
				
			} catch (Exception e) {
				map.put("total", 0);
				map.put("rows", new ArrayList<AssetsPurch>());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBCGSB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBCGSB", "医疗设备管理_设备采购申报", "2", "0"), e);
		}
	}
	/**  
	 * 设备采购申报--已申报列表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "queryAllAssets")
	public void queryAllAssets() {
		try {
			String state = request.getParameter("state");
			String applState = request.getParameter("applState");
			if(StringUtils.isNotBlank(applState)){
				assetsPurch.setApplState(Integer.parseInt(applState));
			}
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				List<AssetsPurch> assetsList = assetsPurchService.queryAllAssetsByData(assetsPurch,state);
				int total = assetsPurchService.getTotalList(assetsPurch,state);
				map.put("total", total);
				map.put("rows", assetsList);
				
			} catch (Exception e) {
				map.put("total", 0);
				map.put("rows", new ArrayList<AssetsPurch>());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBCGSB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBCGSB", "医疗设备管理_设备采购申报", "2", "0"), e);
		}
	}
	/**  
	 * 设备采购审批--待审批  通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "querySPAssets")
	public void querySPAssets() {
		try{
			String state = request.getParameter("state");
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				List<AssetsPurch> assetsList = assetsPurchService.querySPAssetsByData(assetsPurch,state,startTime,endTime);
				int total = assetsPurchService.getSPTotalList(assetsPurch,state,startTime,endTime);
				map.put("total", total);
				map.put("rows", assetsList);
				
			} catch (Exception e) {
				map.put("total", 0);
				map.put("rows", new ArrayList<AssetsPurch>());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBCGSP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBCGSP", "医疗设备管理_设备采购审批", "2", "0"), e);
		}
		
	}
	/**  
	 * 设备采购申报--已申报 停用按钮
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "stopAssets")
	public void stopAssets() {
		try{
			String flag = request.getParameter("flag");
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				String id = request.getParameter("id");
				assetsPurchService.stopAssets(id,flag);
				map.put("resCode", "success");
				map.put("resMsg", "操作成功!");
			} catch (Exception e) {
				map.put("resCode", "error");
				map.put("resMsg", "操作失败!");
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBCGSB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBCGSB", "医疗设备管理_设备采购申报", "2", "0"), e);
		}
	}
	/**  
	 * 设备采购审批--待审批  通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "passAssets")
	public void passAssets() {
		try{
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				String id = request.getParameter("id");
				assetsPurchService.passAssets(id);
				map.put("resCode", "success");
				map.put("resMsg", "操作成功!");
			} catch (Exception e) {
				map.put("resCode", "error");
				map.put("resMsg", "操作失败!");
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBCGSP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBCGSP", "医疗设备管理_设备采购审批", "2", "0"), e);
		}
	}
	
	/**  
	 * 设备采购审批--待审批  不通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "noPassAssets")
	public void noPassAssets() {
		try{
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=utf-8");
				String id = request.getParameter("id");
				String reason = request.getParameter("reason");
				reason = URLDecoder.decode(reason, "utf-8");// url解码
				assetsPurchService.noPassAssets(id,reason);
				map.put("resCode", "success");
				map.put("resMsg", "操作成功!");
			} catch (Exception e) {
				map.put("resCode", "error");
				map.put("resMsg", "操作失败!");
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YLSBGL_SBCGSP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YLSBGL_SBCGSP", "医疗设备管理_设备采购审批", "2", "0"), e);
		}
	}
	/**  
	 * 设备采购审批--待审批  不通过原因
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "seeReason")
	public void seeReason() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			AssetsPurch assets =assetsPurchService.seeReason(id);
			map.put("resCode", "success");
			if(assets!=null){
				map.put("resMsg", assets.getReason());
			}else{
				map.put("resMsg","没有该信息");
			}
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg","操作失败");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 设备采购申报--草稿箱 删除  
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "delAssets")
	public void delAssets()  {
		try {
			String id = request.getParameter("id");
			assetsPurchService.del(id);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZZJGGL_YYYGGL", e);
		}
	}
	/**  
	 * 设备采购审批--已审批  删除
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Action(value = "delAssetsed", results = { @Result(name = "list", location = "/WEB-INF/pages/assets/assetsPurch/assetsPurchApproval.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void delAssetsed()  {
		try {
			String id = request.getParameter("id");
			assetsPurchService.del(id);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZZJGGL_YYYGGL", e);
		}
	}
}
