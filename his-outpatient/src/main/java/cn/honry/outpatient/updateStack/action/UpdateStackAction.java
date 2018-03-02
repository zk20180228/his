package cn.honry.outpatient.updateStack.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.outpatient.updateStack.service.UpdateStackService;
import cn.honry.outpatient.updateStack.vo.StackAndStockInfoVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/business")
@Namespace(value = "/outpatient/updateStack")
public class UpdateStackAction extends ActionSupport {
	// 记录异常日志
	private Logger logger = Logger.getLogger(UpdateStackAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 * 组套业务层service
	 */
	private UpdateStackService updateStackService;
	@Autowired
	@Qualifier(value = "updateStackService")
	public void setUpdateStackService(UpdateStackService updateStackService) {
		this.updateStackService = updateStackService;
	}
	//注入code编码service
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**
	 * 组套编号
	 */
	private String infoId;
	/**
	 * 组套的id
	 */
	private String id;
	/**
	 * 组套类型
	 */
	private String drugType="2";//是否是收费类型   (1 收费  2是医嘱)
	private String feelType="住院";   //医嘱类型    需传是住院还是医嘱
	/**
	 * 诊断组套用的选择的药房
	 */
	private String drugstoreId;
	/**
	 * 组套实体类
	 */
	private BusinessStack businessStack;
	
	public String getDrugstoreId() {
		return drugstoreId;
	}
	public void setDrugstoreId(String drugstoreId) {
		this.drugstoreId = drugstoreId;
	}
	
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public BusinessStack getBusinessStack() {
		return businessStack;
	}
	public void setBusinessStack(BusinessStack businessStack) {
		this.businessStack = businessStack;
	}
	public String getFeelType() {
		return feelType;
	}
	public void setFeelType(String feelType) {
		this.feelType = feelType;
	}
	/**
	 * map对象
	 */
	private Map<Object, Object> map=new HashMap<Object, Object>();
	/**
	 * 跳转页面 
	 * @author  zhenglin
	 * @createDate：  
	 * @modifier lyy
	 * @modifyDate：2016年4月9日 下午2:16:49
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */ 
	@RequiresPermissions(value={"ZTGL:function:view"}) 
	@Action(value = "stackView", results = { @Result(name = "list", location = "/WEB-INF/pages/business/updateStack/newStackView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String stackView()  {
		return "list";
	}
	
	/**
	 * 组套树 
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月9日 上午9:40:44
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "stackAndStackInfoForTree")
	public void stackAndStackInfoForTree()  {
		try {
			String deptId=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			String userId=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			List<TreeJson> list=updateStackService.stackTree(id,deptId,userId,drugType,feelType);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
		    e.printStackTrace();
			logger.error("SSGL_SSPFGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSPFGL", "手术管理_手术批费管理", "2", "0"), e);
		}
	}
	/**
	 * 渲染频次表
	 * @author  zhenglin
	 * @createDate：  
	 * @modifier lyy
	 * @modifyDate：2016年4月9日 下午2:14:28
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getfreq")
	public void getfreq()  {
		try {
			for (BusinessFrequency qy : updateStackService.getFreq()) {
				map.put(qy.getId(), qy.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			 e.printStackTrace();
				logger.error("SSGL_SSPFGL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSPFGL", "手术管理_手术批费管理", "2", "0"), e);
		}
	}
	
	/**
	 * 渲染食用方法
	 * @author   zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月9日 下午2:14:03
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getuse")
	public void getuse()  {
			try {
				List<BusinessDictionary> codeuseageList=innerCodeService.getDictionary("useage");
				for (BusinessDictionary qy : codeuseageList) {
						map.put(qy.getEncode(), qy.getName());
				}
				String json=JSONUtils.toJson(map);
				WebUtils.webSendJSON(json);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("SSGL_SSPFGL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSPFGL", "手术管理_手术批费管理", "2", "0"), e);
			}
	}
	
	/**
	 * 查询组套详情
	 * @author  zhenglin
	 * @createDate：
	 * @modifier lyy
	 * @modifyDate：2016年4月9日 下午12:03:26
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getStackInfoByInfoIdForView")
	public void getStackInfoByInfoIdForView()  {
		try {
			List<StackAndStockInfoVo> stackInfoList=updateStackService.queryStackInfoById(infoId,feelType);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("rows", stackInfoList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SSGL_SSPFGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSPFGL", "手术管理_手术批费管理", "2", "0"), e);
		}
	}
}
