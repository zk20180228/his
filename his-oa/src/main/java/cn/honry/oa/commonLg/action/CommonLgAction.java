package cn.honry.oa.commonLg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.OaCommon;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.commonLg.service.CommonLgService;
import cn.honry.oa.commonLg.vo.CommonVo;
import cn.honry.oa.formInfo.service.FormInfoService;
import cn.honry.oa.formInfo.vo.KeyValVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * <p>常用语管理</p>
 * @Author: yuke
 * @CreateDate: 2017年9月23日 上午10:31:29 
 * @Modifier: yuke
 * @ModifyDate: 2017年9月23日 上午10:31:29 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/commonLg")
public class CommonLgAction extends ActionSupport implements ModelDriven<OaCommon>{
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value = "commonLgService")
	private CommonLgService commonLgService;
	public void setCommonLgService(CommonLgService commonLgService) {
		this.commonLgService = commonLgService;
	}
	//注入表单查询接口
	@Autowired
	@Qualifier(value = "formInfoService")
	private FormInfoService formInfoService;
	public void setFormInfoService(FormInfoService formInfoService) {
		this.formInfoService = formInfoService;
	}
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private Logger logger=Logger.getLogger(CommonLgAction.class);
	
	private OaCommon oaCommon = new OaCommon();
	
	public OaCommon getOaCommon() {
		return oaCommon;
	}

	public void setOaCommon(OaCommon oaCommon) {
		this.oaCommon = oaCommon;
	}

	/**  
	 *  
	 * @Description：  跳转list页面
	 * @version 1.0
	 *
	 */
	@Action(value = "list", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/commonLg/commonList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listMenu() {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		request.setAttribute("account", account);
		return "list";
	}
	
	@Action(value ="findMyCommon")
	public void findMyCommon() {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		try {
			List<OaCommon> list = commonLgService.findMyCommon(account , oaCommon.getTableCode());
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * <p>跳到添加页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:56 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "addCommon", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/commonLg/editCommon.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addCommon() {
		oaCommon = new OaCommon();
		ServletActionContext.getRequest().setAttribute("oaCommon", oaCommon);
		return "add";
	}
	
	/**
	 * 
	 * <p>跳到修改页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月21日 下午7:30:56 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月21日 下午7:30:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "editCommon", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/commonLg/editCommon.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInter() {
		try {
			String id =  request.getParameter("id");
			oaCommon = commonLgService.findById(id);
			ServletActionContext.getRequest().setAttribute("oaCommon", oaCommon);
		} catch (Exception e) {
			logger.error("CYYGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CYYGL", "接口信息管理", "2", "0"), e);
		}
		return "edit";
	}
	
	@Action(value = "saveCommon")
	public void saveCommon(){
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String name = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();
		OaFormInfo oaFormInfo = formInfoService.getFromByCode(oaCommon.getTableCode());
		oaCommon.setUserAccount(account);
		oaCommon.setUserName(name);
		oaCommon.setTableName(oaFormInfo.getFormName());
		Map<String,Object> map=new HashMap<String,Object>();
		
		try {
			commonLgService.saveOrUpddateCommon(oaCommon);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CYYGL", "接口信息管理", "2", "0"), e);
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	
	@Action(value = "delCommon")
	public void delCommon() {
		String ids =  request.getParameter("ids");
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			commonLgService.delCommon(ids);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "删除失败！");
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CYYGL", "接口信息管理", "2", "0"), e);

		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "fandAllFrom")
	public void findAllFrom() {
		try {
			List<KeyValVo> list = formInfoService.getValidFormInfo( );
			String json = JSONUtils.toJson(list);
			json = json.replace("code", "id").replace("name", "text");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("CYYGL", "接口信息管理", "2", "0"), e);
		}
	}
	
	@Action(value = "findCommon")
	public void findCommon() {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		List<CommonVo> list = null;
		try {
			list = commonLgService.findCommon(account , oaCommon.getTableCode());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if ( list.size() <= 0)  {
				CommonVo common = new CommonVo();
				CommonVo common2 = new CommonVo();
				common.setText("同意");
				common2.setText("不同意");
				common.setValue("1");
				common2.setValue("1");
				list.add(common);
				list.add(common2);
			}
		}
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}

	@Override
	public OaCommon getModel() {
		return oaCommon;
	}

}
