package cn.honry.oa.commonManager.action;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.honry.base.bean.model.OaCommon;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.commonLg.service.CommonLgService;
import cn.honry.oa.formInfo.service.FormInfoService;
import cn.honry.oa.formInfo.vo.KeyValVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/**
 * 
 * <p>
 * 常用语管理
 * </p>
 * 
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
@Namespace(value = "/oa/commonManager")
public class CommonManager extends ActionSupport implements
		ModelDriven<OaCommon> {

	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;

	public void setHiasExceptionService(
			HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}

	@Autowired
	@Qualifier(value = "commonLgService")
	private CommonLgService commonLgService;

	public void setCommonLgService(CommonLgService commonLgService) {
		this.commonLgService = commonLgService;
	}

	// 注入表单查询接口
	@Autowired
	@Qualifier(value = "formInfoService")
	private FormInfoService formInfoService;

	public void setFormInfoService(FormInfoService formInfoService) {
		this.formInfoService = formInfoService;
	}

	private OaCommon oaCommon = new OaCommon();

	public OaCommon getOaCommon() {
		return oaCommon;
	}

	public void setOaCommon(OaCommon oaCommon) {
		this.oaCommon = oaCommon;
	}

	/**
	 * 
	 * @Description： 跳转list页面
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value = { "CYYGL:function:view" })
	@Action(value = "list", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/commonLg/commonManager.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listMenu() {
		return "list";
	}

	@Action(value = "fandAllFrom")
	public void findAllFrom() {
		try {
			List<KeyValVo> list = formInfoService.getValidFormInfo();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Action(value ="findMyCommon")
	public void findMyCommon() {
		String account = request.getParameter("account");
		String tableCode = request.getParameter("tableCode");
		if (StringUtils.isBlank(account)) {
			account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		}
		try {
			List<OaCommon> list = commonLgService.findMyCommon(account,tableCode);
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Action(value = "findFrom")
	public void findFrom() {
		String account = request.getParameter("account");
		List<OaCommon> list = commonLgService.findFrom(account);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "saveCommon")
	public void saveCommon() {
		String account = request.getParameter("account");
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String common = request.getParameter("common");
		String[] sAccount = account.split("#");
		String[] sName = name.split("#");
		String[] sCode = code.split("#");
		String[] sCommon = common.split("#");
		for (int i = 0; i < sAccount.length; i++) {
			OaCommon oaCommon = new OaCommon();
			try {
				oaCommon.setUserAccount(sAccount[i]);
				oaCommon.setUserName(sName[i]);
				for (String Code : sCode) {
					BeanUtils.copyProperties(oaCommon, oaCommon);
					OaFormInfo oaFormInfo = formInfoService.getFromByCode(Code);
					oaCommon.setTableCode(Code);
					oaCommon.setTableName(oaFormInfo.getFormName());
					for (String Common : sCommon) {
						BeanUtils.copyProperties(oaCommon, oaCommon);
						oaCommon.setCommon(Common);
						commonLgService.saveOrUpddateCommon(oaCommon);
						oaCommon.setId(null);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Action(value = "editCommon")
	public void editCommon(){
		Map<String,Object> map=new HashMap<String,Object>();
		String newCommon = request.getParameter("newCommon");
		try {
			oaCommon.setCommon(newCommon);
			commonLgService.saveOrUpddateCommon(oaCommon);
			map.put("resCode", "success");
			map.put("resMsg", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "保存失败！");
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}

	@Override
	public OaCommon getModel() {
		return oaCommon;
	}

}
