package cn.honry.oa.otherProjectEntries.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.User;
import cn.honry.utils.ReaderProperty;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/otherProjectEntries")
/***
 * 其他项目入口跳转action
 * @author houzq
 *
 * cn.honry.oa.otherProjectEntries.action
 * 2017年10月16日
 */
public class OtherProjectEntries extends ActionSupport {

	@Resource RedisUtil redis;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private static ReaderProperty readerProperty = new ReaderProperty("otherProjectList.properties");
	@Action(value = "otherProjectList", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/otherProjectEntries/otherProjectEntriesList.jsp") })
	public String otherProjectList(){
		
		User currentUser = ShiroSessionUtils.getCurrentUserFromShiroSession();
		
		String ssoToken = (String)redis.hget("sso", currentUser.getAccount());
		
		String otherProjectList1 = readerProperty.getValue_String("otherProjectList1");
		String[] split1 = otherProjectList1.split(",");
		
		String projectName1=split1[0];
		String projectUrl1=split1[1];
		request.setAttribute("projectName1", projectName1);
		request.setAttribute("ssoToken", ssoToken);
		request.setAttribute("currentUserAccount", currentUser.getAccount());
		request.setAttribute("projectUrl1", projectUrl1);
		
		return "list";
	}

}
