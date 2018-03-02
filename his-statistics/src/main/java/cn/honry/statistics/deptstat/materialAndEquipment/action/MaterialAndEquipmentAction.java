package cn.honry.statistics.deptstat.materialAndEquipment.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.deptstat.materialAndEquipment.service.MaterialAndEquipmentService;
import cn.honry.statistics.deptstat.materialAndEquipment.vo.MaterialAndEquipmentVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/materialAndEquipment")
@SuppressWarnings({"all"})
public class MaterialAndEquipmentAction extends ActionSupport{
	
	private static final long serialVersionUID = 8535596438329082479L;
	
	@Autowired
	@Qualifier(value = "materialAndEquipmentService")
	private MaterialAndEquipmentService materialAndEquipmentService;
	public void setMaterialAndEquipmentService(
			MaterialAndEquipmentService materialAndEquipmentService) {
		this.materialAndEquipmentService = materialAndEquipmentService;
	}
	
	private Logger logger=Logger.getLogger(MaterialAndEquipmentAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private String menuAlias;//权限
	private String page;//页数
	private String rows;//每页数,
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String itemCode;//物资名称
	private String queryStorage;//库存科室
	private String q;//下拉表格模糊查询传递参数
	
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
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
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getQueryStorage() {
		return queryStorage;
	}
	public void setQueryStorage(String queryStorage) {
		this.queryStorage = queryStorage;
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
	/**
	 * 跳转到物资设备统计查询页面
	 * @return
	 */
	@RequiresPermissions(value={"WZSB:function:view"})
	@Action(value = "listMaterialAndEquipment", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/materialAndEquipment/materialAndEquipment.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listMaterialAndEquipment() {
		// 获取当月第一天至当天时间段
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		endTime=format.format(date);
		startTime=endTime.substring(0, 7)+"-01";
		return "list";
	}
	/**  
	 * 物资设备统计查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月10日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月10日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	
	@Action(value="queryMaterialAndEquipment")
	public void queryMaterialAndEquipment(){
			try {
				if(StringUtils.isBlank(startTime)){
					Date date = new Date();
					startTime = DateUtils.formatDateY_M_D(date);
				}else{
					startTime = startTime;
				}
				if(StringUtils.isBlank(endTime)){
					Date date = new Date();
					endTime = DateUtils.formatDateY_M_D(date);
				}else{
					endTime = endTime;
				}
			List<MaterialAndEquipmentVo> materialAndEquipmentList = materialAndEquipmentService.queryMaterialAndEquipment(itemCode,page,rows,queryStorage, startTime, endTime);
			int total=materialAndEquipmentService.getTotalMaterialAndEquipment(itemCode,queryStorage,startTime,endTime);
			Map<Object, Object> map=new HashMap<Object,Object>();
			map.put("total",total);
			map.put("rows", materialAndEquipmentList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("KSTJ_WZSB", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_WZSB", "科室统计_物资设备统计", "2", "0"), e);
		}
	}
	/**  
	 * 
	 * 仓库科室下拉框
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月15日 下午7:05:09 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月15日 下午7:05:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryStorageCode", results = { @Result(name = "json", type = "json") })
	public void queryStorageCode(){
		List<MaterialAndEquipmentVo> storageCode = materialAndEquipmentService.queryStorageCode();
		String json=JSONUtils.toJson(storageCode);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 物资名称下拉框
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月15日 下午7:05:09 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月15日 下午7:05:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value = "queryItemName", results = { @Result(name = "json", type = "json") })
	public void queryItemName(){
		List<MaterialAndEquipmentVo> storageCode = materialAndEquipmentService.queryItemName(page,rows,q);
		int total = materialAndEquipmentService.getTotalItemName(q);
		Map<Object, Object> map=new HashMap<Object,Object>();
		map.put("total",total);
		map.put("rows", storageCode);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
