package cn.honry.oa.activiti.bpm.node.action;

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

import cn.honry.oa.activiti.bpm.node.service.OaBpmConfNodeService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程节点配置Action
 * @author luyanshou
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/activiti/node")
@SuppressWarnings({ "all" })
public class OaBpmConfNodeAction extends ActionSupport {

	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "oaBpmConfNodeService")
	private OaBpmConfNodeService oaBpmConfNodeService;

	public void setOaBpmConfNodeService(OaBpmConfNodeService oaBpmConfNodeService) {
		this.oaBpmConfNodeService = oaBpmConfNodeService;
	}

	/**
	 * 节点配置页面
	 * @return
	 */
	@Action(value = "nodeView", results = { @Result(name = "list", 
			location = "/WEB-INF/pages/oa/activiti/node/nodeView.jsp")},
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String nodeView(){
		String bpmConfBaseId = (String) ServletActionContext.getRequest().getAttribute("bpmConfBaseId");//获取流程配置id
		//查询相关节点信息
		
		return "list";
	}
}
