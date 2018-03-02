package cn.honry.oa.chatOnline.action;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/chatOnline")
public class ChatOnlineAction {

	@RequiresPermissions(value={"KSJDZBTJ:function:view"}) 
	@Action(value = "showChat", results = { @Result(name = "list", location = "/WEB-INF/pages/sys/chatRoom/chatOnline.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showView() {
		
		return "list";
	}
}
