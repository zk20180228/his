package cn.honry.oa.messagersend.action;

import java.util.Map;

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

import cn.honry.oa.messagersend.service.MessagerSendService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**  
 * <p> 通过页面发送短息</p>
 * @Author: mrb
 * @CreateDate: 2017年10月23日 上午11:46:53 
 * @Modifier: mrb
 * @ModifyDate: 2017年10月23日 上午11:46:53 
 * @ModifyRmk:  
 * @version: V1.0 
 *MessagerSendAction
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/messagerSend")
public class MessagerSendAction {
	@Autowired
	@Qualifier("messagerSendService")
	private MessagerSendService messagerSendService;
	/***
	 * 接收人
	 */
	private String acceptUser;
	/**
	 * 短信内容
	 */
	private String content;
	/**
	 * 自定义接收人
	 */
	private String definedaccept;
	
	/**  
	 * <p>转到信息发送界面 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月23日 下午4:18:17 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月23日 下午4:18:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value = "toView", results = { @Result(
			name = "view", location = "/WEB-INF/pages/oa/messagerSend/messagerSend.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView(){
		return "view";
	}
	/**  
	 * <p>选择短信接收人 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月23日 下午4:18:32 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月23日 下午4:18:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value = "toUserSelect", results = { @Result(
			name = "list", location = "/WEB-INF/pages/oa/messagerSend/acceptUser.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toUserSelect(){
		return "list";
	}
	/**  
	 * <p>短信发送 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月24日 下午5:14:57 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月24日 下午5:14:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action(value = "msgsendbyjsp")
	public void msgsendbyjsp(){
		Map<String, String> map = messagerSendService.msgSend(acceptUser, content, definedaccept);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
	
	
	
	public String getAcceptUser() {
		return acceptUser;
	}
	public void setAcceptUser(String acceptUser) {
		this.acceptUser = acceptUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDefinedaccept() {
		return definedaccept;
	}
	public void setDefinedaccept(String definedaccept) {
		this.definedaccept = definedaccept;
	}
	
}
