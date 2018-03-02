package cn.honry.oa.patmenumanager.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.OaMenuRight;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.patmenumanager.service.PatMenuManagerService;
import cn.honry.oa.patmenumanager.service.PatMenuRightService;
import cn.honry.oa.patmenumanager.vo.MenuVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * <p>栏目维护</p>
 * @Author: yuke
 * @CreateDate: 2017年7月15日 下午4:38:20 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月15日 下午4:38:20 
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
@Namespace(value = "/oa/patMenuManager")
public class PatMenuManagerAction  extends ActionSupport implements ModelDriven<MenuVo> {
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private Logger logger=Logger.getLogger(PatMenuManagerAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 注入栏目管理service
	 */
	private PatMenuManagerService patMenuManagerService;
	@Autowired
	@Qualifier(value = "patMenuManagerService")
	public void setPatMenuManagerService(PatMenuManagerService patMenuManagerService) {
		this.patMenuManagerService = patMenuManagerService;
	}

	/**
	 * 注入栏目权限管理service
	 */
	private PatMenuRightService patMenuRightService;
	@Autowired
	@Qualifier(value="patMenuRightService")
	public void setPatMenuRightService(PatMenuRightService patMenuRightService) {
		this.patMenuRightService = patMenuRightService;
	}

	private MenuVo menu = new MenuVo();
	@Override
	public MenuVo getModel() {
		return menu;
	}
	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	private static final long serialVersionUID = 1L;

	/**  
	 *  
	 * @Description：  跳转list页面
	 * @version 1.0
	 *
	 */
	@Action(value = "listMenu", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/patmenu/menuList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listMenu() {
		return "list";
	}
	/**
	 * 
	 * <p>栏目数展示</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月15日 下午5:29:26 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月15日 下午5:29:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "showTree")
	public void showTree(){
		try {
			MenuVo vo = patMenuManagerService.showTree("");
			String json = "["+JSONUtils.toJson(vo)+"]";
			json = json.replaceAll("name", "text");
			json = json.replaceAll("code", "id");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("OALMGL_LMGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
		}
	}
	/**
	 * 
	 * <p>ID为栏目code的树</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月15日 下午5:29:26 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月15日 下午5:29:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "showTreeWithCode")
	public void showTreeWithCode(){
		try {
			MenuVo vo = patMenuManagerService.showTree("");
			String json = "["+JSONUtils.toJson(vo)+"]";
			json = json.replaceAll("name", "text");
			json = json.replaceAll("code", "id");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("OALMGL_LMGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
		}
	}
	/**
	 * 
	 * <p>ID为栏目code的树</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月15日 下午5:29:26 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月15日 下午5:29:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "showTreeWithCodeForTital")
	public void showTreeWithCodeForTital(){
		try {
			MenuVo vo = patMenuManagerService.showTreeTwo("");
			String json = "["+JSONUtils.toJson(vo)+"]";
			json = json.replaceAll("name", "text");
			json = json.replaceAll("code", "id");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("OALMGL_LMGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
		}
	}
	/**
	 * 
	 * <p>跳转到添加栏目列表页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月18日 上午10:13:52 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月18日 上午10:13:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"OALMGL:function:add"})
	@Action(value = "addMenu", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/patmenu/menuEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addMenu() {
		MenuVo menuVo = new MenuVo();
		List<MenuVo> vo = patMenuManagerService.queryMenuByCode(menu.getId());
		if(vo != null && vo.size()>0){
			menuVo.setParentcode(vo.get(0).getCode());
			menuVo.setParent(vo.get(0).getName());
		}
		menuVo.setPublishdirt(0);
		menuVo.setStop_flag(0);
		menuVo.setDel_flag(0);
		request.setAttribute("menu", menuVo);
		return "add";
	}
	
	/**
	 * 
	 * <p>跳转到修改栏目列表页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月18日 上午10:13:52 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月18日 上午10:13:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"OALMGL:function:edit"})
	@Action(value = "editMenu", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/patmenu/menuEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editMenu() {
		List<MenuVo> vo = patMenuManagerService.queryMenuByCode(menu.getCode());
		if(StringUtils.isBlank(vo.get(0).getParentcode())){
			vo.get(0).setParentcode("1");
		}
		List<OaMenuRight> list = patMenuRightService.findAllByMenuid(menu.getCode());
		String publish = "";
		String check = "";
		String view = "";
		String mpublish = "";
		String mcheck = "";
		String mview = "";
		
		String allp ="";
		String peoplep ="";
		String juesep ="";
		String keship ="";
		String zhiwup ="";
		
		String allc ="";
		String peoplec ="";
		String juesec ="";
		String keshic ="";
		String zhiwuc ="";
		
		String allv ="";
		String peoplev ="";
		String juesev ="";
		String keshiv ="";
		String zhiwuv ="";
		
		String peoplepp ="";
		String juesepp ="";
		String keshipp ="";
		String zhiwupp ="";
		
		String peoplecc ="";
		String juesecc ="";
		String keshicc ="";
		String zhiwucc ="";
		
		String peoplevv ="";
		String juesevv ="";
		String keshivv ="";
		String zhiwuvv ="";
		for (OaMenuRight oaRight : list) {
			//获取发布权限
			if ("1".equals(oaRight.getRightType())) {
				if ( "0".equals(oaRight.getRight())) {
					allp += oaRight.getCodeName();
				}
			}
			//获取审核权限
			if ("2".equals(oaRight.getRightType())) {
				if ( "0".equals(oaRight.getRight())) {
					allc += oaRight.getCodeName();
				}
			}
			//获取浏览权限
			if ("3".equals(oaRight.getRightType())) {
				if ( "0".equals(oaRight.getRight())) {
					allv += oaRight.getCodeName();
				}
			}
			
			peoplep = getRightName(peoplep,oaRight,"1","1");
			juesep = getRightName(juesep,oaRight,"1","2");
			keship = getRightName(keship,oaRight,"1","3");
			zhiwup = getRightName(zhiwup,oaRight,"1","4");
			peoplec = getRightName(peoplec,oaRight,"2","1");
			juesec = getRightName(juesec,oaRight,"2","2");
			keshic = getRightName(keshic,oaRight,"2","3");
			zhiwuc = getRightName(zhiwuc,oaRight,"2","4");
			peoplev = getRightName(peoplev,oaRight,"3","1");
			juesev = getRightName(juesev,oaRight,"3","2");
			keshiv = getRightName(keshiv,oaRight,"3","3");
			zhiwuv = getRightName(zhiwuv,oaRight,"3","4");
			
			peoplepp = getRightNameAndCode(peoplepp,oaRight,"1","1");
			juesepp = getRightNameAndCode(juesepp,oaRight,"1","2");
			keshipp = getRightNameAndCode(keshipp,oaRight,"1","3");
			zhiwupp = getRightNameAndCode(zhiwupp,oaRight,"1","4");
			peoplecc = getRightNameAndCode(peoplecc,oaRight,"2","1");
			juesecc = getRightNameAndCode(juesecc,oaRight,"2","2");
			keshicc = getRightNameAndCode(keshicc,oaRight,"2","3");
			zhiwucc = getRightNameAndCode(zhiwucc,oaRight,"2","4");
			peoplevv = getRightNameAndCode(peoplevv,oaRight,"3","1");
			juesevv = getRightNameAndCode(juesevv,oaRight,"3","2");
			keshivv = getRightNameAndCode(keshivv,oaRight,"3","3");
			zhiwuvv = getRightNameAndCode(zhiwuvv,oaRight,"3","4");
		}
		
		if (StringUtils.isNotBlank(allp)) {
			publish += "[全部人员]";
		}
		if (StringUtils.isNotBlank(peoplep)) {
			publish += "[人员]:"+peoplep;
		}
		if (StringUtils.isNotBlank(juesep)) {
			publish += "[级别]:"+juesep;
		}
		if (StringUtils.isNotBlank(keship)) {
			publish += "[科室]:"+keship;
		}
		if (StringUtils.isNotBlank(zhiwup)) {
			publish += "[职务]:"+zhiwup;
		}
		
		if (StringUtils.isNotBlank(allc)) {
			check += "[全部人员]";
		}
		if (StringUtils.isNotBlank(peoplec)) {
			check += "[人员]:"+peoplec;
		}
		if (StringUtils.isNotBlank(juesec)) {
			check += "[级别]:"+juesec;
		}
		if (StringUtils.isNotBlank(keshic)) {
			check += "[科室]:"+keshic;
		}
		if (StringUtils.isNotBlank(zhiwuc)) {
			check += "[职务]:"+zhiwuc;
		}
		
		if (StringUtils.isNotBlank(allv)) {
			view += "[全部人员]";
		}
		if (StringUtils.isNotBlank(peoplev)) {
			view += "[人员]:"+peoplev;
		}
		if (StringUtils.isNotBlank(juesev)) {
			view += "[级别]:"+juesev;
		}
		if (StringUtils.isNotBlank(keshiv)) {
			view += "[科室]:"+keshiv;
		}
		if (StringUtils.isNotBlank(zhiwuv)) {
			view += "[职务]:"+zhiwuv;
		}
		
		mpublish = "0_"+allp +"#1_"+peoplepp+ "#2_"+juesepp+"#3_"+keshipp+"#4_"+zhiwupp;
		mcheck = "0_"+allc +"#1_"+peoplecc+ "#2_"+juesecc+"#3_"+keshicc+"#4_"+zhiwucc;
		mview = "0_"+allv +"#1_"+peoplevv+ "#2_"+juesevv+"#3_"+keshivv+"#4_"+zhiwuvv;
		
		vo.get(0).setMpublish(mpublish);
		vo.get(0).setMcheck(mcheck);
		vo.get(0).setMview(mview);
		request.setAttribute("mmpublish", publish);
		request.setAttribute("mmcheck", check);
		request.setAttribute("mmview", view);
		request.setAttribute("menu", vo.get(0));
		return "edit";
	}
	/**
	 * 
	 * <p>跳转到修改栏目列表页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月18日 上午10:13:52 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月18日 上午10:13:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@RequiresPermissions(value={"OALMGL:function:info"})
	@Action(value = "menuInfo", results = { @Result(name = "info", location = "/WEB-INF/pages/oa/patmenu/menuInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String menuInfo() {
		List<MenuVo> vo = patMenuManagerService.queryMenuByCode(menu.getCode());
		if(StringUtils.isBlank(vo.get(0).getParentcode())){
			vo.get(0).setParentcode("1");
		}
		List<OaMenuRight> list = patMenuRightService.findAllByMenuid(menu.getCode());
		String publish = "";
		String check = "";
		String view = "";
		String mpublish = "";
		String mcheck = "";
		String mview = "";
		
		String allp ="";
		String peoplep ="";
		String juesep ="";
		String keship ="";
		String zhiwup ="";
		
		String allc ="";
		String peoplec ="";
		String juesec ="";
		String keshic ="";
		String zhiwuc ="";
		
		String allv ="";
		String peoplev ="";
		String juesev ="";
		String keshiv ="";
		String zhiwuv ="";
		
		for (OaMenuRight oaRight : list) {
			//获取发布权限
			if ("1".equals(oaRight.getRightType())) {
				if ( "0".equals(oaRight.getRight())) {
					allp += oaRight.getCodeName();
				}
			}
			//获取审核权限
			if ("2".equals(oaRight.getRightType())) {
				if ( "0".equals(oaRight.getRight())) {
					allc += oaRight.getCodeName();
				}
			}
			//获取浏览权限
			if ("3".equals(oaRight.getRightType())) {
				if ( "0".equals(oaRight.getRight())) {
					allv += oaRight.getCodeName();
				}
			}
			
			peoplep = getRightName(peoplep,oaRight,"1","1");
			juesep = getRightName(juesep,oaRight,"1","2");
			keship = getRightName(keship,oaRight,"1","3");
			zhiwup = getRightName(zhiwup,oaRight,"1","4");
			peoplec = getRightName(peoplec,oaRight,"2","1");
			juesec = getRightName(juesec,oaRight,"2","2");
			keshic = getRightName(keshic,oaRight,"2","3");
			zhiwuc = getRightName(zhiwuc,oaRight,"2","4");
			peoplev = getRightName(peoplev,oaRight,"3","1");
			juesev = getRightName(juesev,oaRight,"3","2");
			keshiv = getRightName(keshiv,oaRight,"3","3");
			zhiwuv = getRightName(zhiwuv,oaRight,"3","4");
			
		}
		
		if (StringUtils.isNotBlank(allp)) {
			publish += "[全部人员]";
		}
		if (StringUtils.isNotBlank(peoplep)) {
			publish += "[人员]:"+peoplep;
		}
		if (StringUtils.isNotBlank(juesep)) {
			publish += "[级别]:"+juesep;
		}
		if (StringUtils.isNotBlank(keship)) {
			publish += "[科室]:"+keship;
		}
		if (StringUtils.isNotBlank(zhiwup)) {
			publish += "[职务]:"+zhiwup;
		}
		
		if (StringUtils.isNotBlank(allc)) {
			check += "[全部人员]";
		}
		if (StringUtils.isNotBlank(peoplec)) {
			check += "[人员]:"+peoplec;
		}
		if (StringUtils.isNotBlank(juesec)) {
			check += "[级别]:"+juesec;
		}
		if (StringUtils.isNotBlank(keshic)) {
			check += "[科室]:"+keshic;
		}
		if (StringUtils.isNotBlank(zhiwuc)) {
			check += "[职务]:"+zhiwuc;
		}
		
		if (StringUtils.isNotBlank(allv)) {
			view += "[全部人员]";
		}
		if (StringUtils.isNotBlank(peoplev)) {
			view += "[人员]:"+peoplev;
		}
		if (StringUtils.isNotBlank(juesev)) {
			view += "[级别]:"+juesev;
		}
		if (StringUtils.isNotBlank(keshiv)) {
			view += "[科室]:"+keshiv;
		}
		if (StringUtils.isNotBlank(zhiwuv)) {
			view += "[职务]:"+zhiwuv;
		}
		
		vo.get(0).setMpublish(mpublish);
		vo.get(0).setMcheck(mcheck);
		vo.get(0).setMview(mview);
		request.setAttribute("mmpublish", publish);
		request.setAttribute("mmcheck", check);
		request.setAttribute("mmview", view);
		request.setAttribute("menu", vo.get(0));
		return "info";
	}
	
	private String getRightName(String publish, OaMenuRight oaRight,
			String qx, String xz) {
		if (qx.equals(oaRight.getRightType())){
			if (xz.equals(oaRight.getRight())) {
				if (!"".equals(publish)) {
					publish +=",";
				}
				publish += oaRight.getCodeName();
			}
		}
		return publish;
	}
	private String getRightNameAndCode(String publish, OaMenuRight oaRight,
			String qx, String xz) {
		if (qx.equals(oaRight.getRightType())){
			if (xz.equals(oaRight.getRight())) {
				if (!"".equals(publish)) {
					publish +=",";
				}
				publish += oaRight.getCodeName()+":"+oaRight.getCode();
			}
		}
		return publish;
	}
	/**
	 * 
	 * <p>跳转到权限添加页面</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月24日 下午2:45:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月24日 下午2:45:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "addRight", results = { @Result(name = "addright", location = "/WEB-INF/pages/oa/patmenu/menuRight.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addRight() {
		
		
		request.setAttribute("type", menu.getType());
		return "addright";
	}
	/**
	 * 
	 * <p>删除某个栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月18日 上午9:01:31 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月18日 上午9:01:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@RequiresPermissions(value={"OALMGL:function:delete"})
	@Action(value = "delMenu")
	public  void  delMenu() {
		try {
			menu.setId(request.getParameter("id"));
			request.setCharacterEncoding("utf-8");
			menu.setCode(request.getParameter("code"));
			patMenuManagerService.delMenu(menu);
		} catch (Exception e) {
			logger.error("OALMGL_LMGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
		}
	}
	/**
	 * 
	 * <p>根据Parentcode查询栏目信息</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月19日 下午6:37:36 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月19日 下午6:37:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryMenuById")
	public void queryMenuById(){
		List<MenuVo> vo =  patMenuManagerService.queryMenuByParentcode(menu.getId());
		String json = JSONUtils.toJson(vo);
		if (json != null) {
			WebUtils.webSendJSON(json);
		}else {
			WebUtils.webSendJSON("[]");
		}
	}
	/**
	 * <p>保存栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月19日 下午6:37:58 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月19日 下午6:37:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="saveMenu")
	public void saveMenu(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		
		List<MenuVo> list = patMenuManagerService.queryMenuByCode(menu.getCode());
		String newCode = menu.getCode();
			if(StringUtils.isNotBlank(menu.getId())){
				List<MenuVo> list2 = patMenuManagerService.queryMenuById(menu.getId());
				String oldCode = list2.get(0).getCode();
				//如果id不为空 走修改方法
				if(newCode.equals(oldCode)){
					try {
						patMenuManagerService.queryMenuByParentcode(menu.getId());
						patMenuManagerService.save(menu);
						retMap.put("resCode", "success");
						retMap.put("resMsg", "保存成功!");
					} catch (Exception e) {
						retMap.put("resCode", "error");
						retMap.put("resMsg", "保存失败!");
						logger.error("OALMGL_LMGL", e);
						hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
					}
				}else if(!(list.size()>0)){
					try {
						patMenuManagerService.queryMenuByParentcode(menu.getId());
						patMenuManagerService.save(menu);
						retMap.put("resCode", "success");
						retMap.put("resMsg", "保存成功!");
					} catch (Exception e) {
						retMap.put("resCode", "error");
						retMap.put("resMsg", "保存失败!");
						logger.error("OALMGL_LMGL", e);
						hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
					}
				}else{
					retMap.put("resCode", "error");
					retMap.put("resMsg", "栏目code已存在，请重新输入!");
				}
			}else {
				if(!(list.size()>0)){
					//如果id为空 走添加方法
					try {
						patMenuManagerService.save(menu);
						retMap.put("resCode", "success");
						retMap.put("resMsg", "保存成功!");
						
					} catch (Exception e) {
						retMap.put("resCode", "error");
						retMap.put("resMsg", "保存失败!");
						logger.error("OALMGL_LMGL", e);
						hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OALMGL_LMGL", "OA栏目信息管理_栏目管理", "3", "1"), e);
					}
				}else{
					retMap.put("resCode", "error");
					retMap.put("resMsg", "栏目code已存在，请重新输入!");
				}
			}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 栏目排序移动
	 * @return
	 */
	@Action(value="move")
	public void move(){
		String type = (String) request.getAttribute("type");
		if("up".equals(type)){
			patMenuManagerService.moveUp(menu);
		}else{
			patMenuManagerService.moveDown(menu);
		}
	}
}
