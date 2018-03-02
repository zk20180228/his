package cn.honry.inpatient.surety.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.surety.service.SuretyService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**  
 *  
 * @className：SuretyAction 
 * @Description：  住院担保金Action
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/surety")
public class SuretyAction extends ActionSupport {
	private Logger logger=Logger.getLogger(SuretyAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	
	/**
	 * 注入住院担保金Service
	 */
	@Autowired 
	@Qualifier(value = "suretyService")
	private SuretyService suretyService;
	
	/**
	 * 住院担保金实体类
	 */
	private InpatientSurety surety;
	
	/**
	 * 患者名称，用于保存
	 */
	private String name;
	
	/**
	 * 患者住院号，用于保存及查询预交金信息
	 */
	private String inpatientNo;
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * id集合
	 */
	private String[] ids;
	/**
	 * setters and getters
	 */
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public void setSuretyService(SuretyService suretyService) {
		this.suretyService = suretyService;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public InpatientSurety getSurety() {
		return surety;
	}

	public void setSurety(InpatientSurety surety) {
		this.surety = surety;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
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
	 *  
	 * @Description：  跳转到添加担保金页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-09 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-09 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "surety", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/inprePay/surety.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String surety() {
		return "list";
	}
	
	/**
	 *
	 * @Description：保存担保金
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "savaSurety")
	public void savaSurety() {
		Map<String,Object> retMap=new HashMap<String,Object>();
		try {
			surety.setInpatientNo(inpatientNo);
			String msg=suretyService.save(surety);
			if("success".equals(msg)){
				retMap.put("resMsg", "success");
				retMap.put("resCode","缴纳担保金成功！");
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode","缴纳担保金失败！");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			retMap.put("resMsg", "error");
			retMap.put("resCode","缴纳担保金失败！");
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("ZYSF_ZYYJJGL", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZYYJJGL", "住院收费_住院预交金管理", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 *
	 * @Description：查询预交金信息 - 分页查询
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "querySurety")
	public void querySurety() {
		Map<String,Object> retMap=new HashMap<String,Object>();
		if(StringUtils.isBlank(inpatientNo)){
			retMap.put("total", 0);
			retMap.put("rows", new ArrayList<InpatientInPrepay>());
		}else{
			int total = suretyService.getSuretyTotal(inpatientNo);
			List<InpatientSurety> list = suretyService.getSuretyPage(inpatientNo,page,rows);
			retMap.put("total", total);
			retMap.put("rows", list);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 *
	 * @Description：返回预交金 
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月26日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "removeSurety")
	public void removeSurety() {
		suretyService.removeSuretyByids(ids);
		String json = JSONUtils.toJson("success");
		WebUtils.webSendJSON(json);
	}
}
