package cn.honry.oa.formInfo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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

import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.oa.formInfo.service.FormInfoService;
import cn.honry.oa.formInfo.vo.FielVo;
import cn.honry.oa.formInfo.vo.InfoVo;
import cn.honry.oa.formInfo.vo.KeyValVo;
import cn.honry.oa.formInfo.vo.SectVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;


/**  
 *  
 * @className：FormInfoAction
 * @Description：  自定义表单维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
/**
 * @author Administrator
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/formInfo")
public class FormInfoAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "formInfoService")
	private FormInfoService formInfoService;
	public void setFormInfoService(FormInfoService formInfoService) {
		this.formInfoService = formInfoService;
	}

	private String menuAlias;
	
	private String page;
	
	private String rows;
	
	private String search;
	
	private String code;
	
	private String id;
	
	private OaFormInfo formInfo;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public OaFormInfo getFormInfo() {
		return formInfo;
	}

	public void setFormInfo(OaFormInfo formInfo) {
		this.formInfo = formInfo;
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
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**  
	 *  
	 * 跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:view"})
	@Action(value = "listFormInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/formInfoList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listFormInfo() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转候选组选择页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//	@RequiresPermissions(value={"ZDYBDWH:function:userModlue"})
	@Action(value = "deptModlueAccredit", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/diyDeptSelectActiviti.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String deptModlueAccredit() {
		return "list";
	}
	/**  
	 *  
	 * 跳转授权人员选择页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//	@RequiresPermissions(value={"ZDYBDWH:function:userModlue"})
	@Action(value = "userModlueAccredit", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/diyformUseruserModlueAccredit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String userModlueAccredit() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转授权页面activiti
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//	@RequiresPermissions(value={"ZDYBDWH:function:userModlue"})
	@Action(value = "conditionActiviti", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/conditionActiviti.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String conditionActiviti() {
		return "list";
	}	
	/**  
	 *  
	 * 跳转授权人员选择页面activiti
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//	@RequiresPermissions(value={"ZDYBDWH:function:userModlue"})
	@Action(value = "userModlueActiviti", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/diyUserSelectActiviti.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String userModlueActiviti() {
		return "list";
	}
	
	/**  
	 *  
	 * 跳转人员选择页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//	@RequiresPermissions(value={"ZDYBDWH:function:userModlue"})
	@Action(value = "userModlue", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/diyformUser.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String userModlue() {
		return "list";
	}
	/**  
	 *  
	 * 跳转科室选择页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
//	@RequiresPermissions(value={"ZDYBDWH:function:departmentModlue"})
	@Action(value = "departmentModlue", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/diyformDepartment.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String departmentModlue() {
		return "list";
	}
	/**  
	 *  
	 * 跳转添加页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:add"})
	@Action(value = "addFormInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/formInfoEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addFormInfo() {
		formInfo = new OaFormInfo();
		return "list";
	}
	
	/**  
	 *  
	 * 跳转修改页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:edit"})
	@Action(value = "editFormInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/formInfo/formInfoEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editFormInfo() {
		
		try {
			formInfo = formInfoService.get(formInfo.getId());
			formInfo.setFormInfoStr(formInfoService.ClobToString(formInfo.getFormInfo()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	/**  
	 *  
	 * 获取列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:query"}) 
	@Action(value = "queryFormInfoList")
	public void queryMenuHeaderList() {
		Map<String,Object> map = new HashMap<String, Object>();
		int total = formInfoService.getFormInfoTotal(StringUtils.isNotBlank(search)?search.toUpperCase():null);
		List<OaFormInfo> list = formInfoService.getFormInfoRows(page,rows,StringUtils.isNotBlank(search)?search.toUpperCase():null);
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 停用
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:stop"}) 
	@Action(value = "stopFormInfo")
	public void stopFormInfo() {
		Map<String,String> retMap = new HashMap<>();
		try {
			int stopNum = formInfoService.stopflgFormInfo(search,1);
			retMap.put("resCode", "success");
			retMap.put("resMsg", stopNum==0?"没有需要停用的信息":"成功停用"+stopNum+"条信息!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "停用失败!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 启用
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:enable"}) 
	@Action(value = "enableFormInfo")
	public void enableFormInfo() {
		Map<String,String> retMap = new HashMap<>();
		try {
			int enableNum = formInfoService.stopflgFormInfo(search,0);
			retMap.put("resCode", "success");
			retMap.put("resMsg", enableNum==0?"没有需要启用的信息":"成功启用"+enableNum+"条信息!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "启用失败!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZDYBDWH:function:delete"}) 
	@Action(value = "delFormInfo")
	public void delFormInfo() {
		Map<String,String> retMap = new HashMap<>();
		try {
			int delNum = formInfoService.delFormInfo(search);
			retMap.put("resCode", "success");
			retMap.put("resMsg", delNum==0?"没有需要删除的信息":"成功删除"+delNum+"条信息!");
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 保存
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-18 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-18 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveFormInfo")
	public void saveFormInfo() {
		Map<String,String> retMap = new HashMap<>();
		boolean isJuri = false;
		if(StringUtils.isNotBlank(formInfo.getId())){
			if(SecurityUtils.getSubject().isPermitted("ZDYBDWH:function:edit")){
				isJuri = true;
			}
		}else{
			if(SecurityUtils.getSubject().isPermitted("ZDYBDWH:function:add")){
				isJuri = true;
			}
		}
		if(isJuri){
			boolean isVali = false;
			StringBuffer msg = new StringBuffer();
			if(StringUtils.isBlank(formInfo.getFormCode())){
				msg.append("表单标识不能为空!");
			}
			if(StringUtils.isNotBlank(formInfo.getFormCode())&&!formInfo.getFormCode().matches("[A-Za-z0-9_-]+")){
				msg.append("表单标识必须为字母、数字、连接符(-)或下划线(_)!");
			}
			if(StringUtils.isBlank(formInfo.getFormName())){
				msg.append("表单名称不能为空!");
			}
			try {
				InfoVo vo = JSONUtils.fromJson(formInfo.getFormInfoStr(), InfoVo.class);
				ArrayList<KeyValVo> keyValList = null;
				if(vo==null){
					msg.append("表单对象不能为空!");
				}else {
					Map<String,List<FielVo>> nameMap = new HashMap<String, List<FielVo>>();
					keyValList = new ArrayList<KeyValVo>();
					KeyValVo keyVal = null;
					for(SectVo sectVo : vo.getSections()){
						if(sectVo.getFields()!=null&&sectVo.getFields().size()>0){
							for(FielVo fielVo : sectVo.getFields()){
								if(!"label".equals(fielVo.getType()) && !"LabelText".equals(fielVo.getType())){
									if(StringUtils.isBlank(fielVo.getName())){
										if(msg.length()>0){
											msg.append("<br>");
										}
										msg.append("坐标：第").append(Integer.parseInt(fielVo.getRow())+1).append("行,第").append(Integer.parseInt(fielVo.getCol())+1).append("列,元素[").append(fielVo.getType()).append("],name不能为空!");
									}
									if(StringUtils.isNotBlank(fielVo.getName())&&!fielVo.getName().matches("[A-Za-z0-9_-]+")){
										if(msg.length()>0){
											msg.append("<br>");
										}
										msg.append("坐标：第").append(Integer.parseInt(fielVo.getRow())+1).append("行,第").append(Integer.parseInt(fielVo.getCol())+1).append("列,元素[").append(fielVo.getType()).append("],name必须为字母、数字、连接符(-)或下划线(_)!");
									}else{
										if(nameMap.get(fielVo.getName())==null){
											List<FielVo> fvoList = new ArrayList<FielVo>();
											fvoList.add(fielVo);
											nameMap.put(fielVo.getName(), fvoList);
										}else{
											nameMap.get(fielVo.getName()).add(fielVo);
										}
									}
									keyVal = new KeyValVo();
									keyVal.setCode(fielVo.getType());
									keyVal.setName(fielVo.getName());
									keyValList.add(keyVal);
								}
							}
						}
					}
					if(nameMap.size()>0){
						for(Map.Entry<String,List<FielVo>> map : nameMap.entrySet()){
							if(map.getValue().size()>1){
								if(msg.length()>0){
									msg.append("<br>");
								}
								msg.append("坐标：");
								for(FielVo fvo : map.getValue()){
									msg.append("[第").append(Integer.parseInt(fvo.getRow())+1).append("行,第").append(Integer.parseInt(fvo.getCol())+1).append("列]");
								}
								msg.append("元素name不能重复!");
							}
						}
					}
				}
				if(msg.length()==0){
					isVali = true;
				}
				if(!isVali){
					retMap.put("resCode", "error");
					retMap.put("resMsg", msg.toString());
				}else {
					retMap = formInfoService.saveFormInfo(formInfo,keyValList);
				}
			} catch (Exception e) {
				retMap.put("resCode", "error");
				retMap.put("resMsg", "表单对象异常!");
			}
		}else{
			retMap.put("resCode", "error");
			retMap.put("resMsg", "您没有"+(StringUtils.isNotBlank(formInfo.getId())?"修改":"添加")+"权限,请联系管理员!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 接口-获取可用表单
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getValidFormInfo")
	public void getValidFormInfo() {
		List<KeyValVo> keyValList = formInfoService.getValidFormInfo();
		String json = JSONUtils.toJson(keyValList);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 接口-获取表单主件<br>
	 * code表单编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getPartOfFormInfo")
	public void getPartOfFormInfo() {
		List<KeyValVo> keyValList = formInfoService.getPartOfFormInfo(code);
		String json = JSONUtils.toJson(keyValList);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * 跳转修改页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "editFormInfoJson")
	public void editFormInfoJson() {
		Map<String,String> retMap = new HashMap<>();
		try {
			formInfo = formInfoService.get(id);
			retMap.put("resCode", "success");
			retMap.put("resMsg", formInfoService.ClobToString(formInfo.getFormInfo()));
		} catch (Exception e) {
			retMap.put("resCode", "error");
			retMap.put("resMsg", "表单获取失败!");
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	
}
