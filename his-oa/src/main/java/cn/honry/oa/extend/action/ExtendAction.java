package cn.honry.oa.extend.action;

import java.util.List;

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

import cn.honry.oa.extend.service.ExtendService;
import cn.honry.oa.extend.vo.RecordVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


/**  
 *  
 * @className：ExtendAction
 * @Description：  扩展
 * @Author：aizhonghua
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/extend")
public class ExtendAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "extendService")
	private ExtendService extendService;
	public void setExtendService(ExtendService extendService) {
		this.extendService = extendService;
	}

	private String id;
	
	private String topFlow;//关联流程
	
	private String type;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTopFlow() {
		return topFlow;
	}

	public void setTopFlow(String topFlow) {
		this.topFlow = topFlow;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**  
	 *  
	 * 跳转到因私销假绑定页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "sickLeavePrivateList", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/extend/sickLeavePrivateList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String sickLeavePrivateList() {
		return "list";
	}
	
	
	@Action(value = "queryLeaveComplete")
	public void queryLeaveComplete() {
		List<RecordVo> infoList = extendService.queryLeaveComplete(type,topFlow);
		String json = JSONUtils.toJson(infoList);
		WebUtils.webSendJSON(json);
	}
	
}
