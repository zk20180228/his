package cn.honry.mobile.openFireInit.action;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.base.bean.model.User;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.mobile.openFireInit.service.OpenFireInitService;
import cn.honry.mobile.utils.CreateOpenfireConnectionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * openfire账户初始化
 * @author hedong 20170623 源自侯兆琦zkhonry-mobile-interface服务接口
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/openFireInitAction")
public class OpenFireInitAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OpenFireInitAction.class);
	
	@Autowired
	@Qualifier(value = "openFireInitService")
	private OpenFireInitService openFireInitService;
	
	public void setOpenFireInitService(OpenFireInitService openFireInitService) {
		this.openFireInitService = openFireInitService;
	}

	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}

	/**
	 * Openfire账户初始化接口
	 * @param list
	 * @param response
	 */
	@RequiresPermissions(value={"YWSJCSH:function:view"})
	@Action(value = "openFireInit")
	public void openFireInit(){
		String res = "success";
		AbstractXMPPConnection connection = null;
		try {
			connection = CreateOpenfireConnectionUtils.getOpenfireConnection();
			//初始化openfire账户前先清空除amdin和20161209以外的所有账户
			/*
			 * 每次初始化账户时不删除这两个用户
			 */
			String account1 = "admin";
			String account2 = "20161209','managerinit";
			openFireInitService.delOfUserAccount(account1,account2);
			//查询所有用户
			List<User>users = openFireInitService.findUsers();
			//附加信息  
			HashMap<String, String> attributes =new HashMap<String, String>();
			AccountManager accountManager = AccountManager.getInstance(connection);
			accountManager.sensitiveOperationOverInsecureConnection(true);
			for (User mUser : users) {
				String user_account = mUser.getAccount();
				//创建账户
				attributes.put("name", mUser.getName());
				accountManager.createAccount(user_account,user_account+"123",attributes);				
			}
		} catch (Exception e) {
			res = "error";
			System.out.println(e);
			logger.error(e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YWSJCSH","业务数据初始化","2","1"), e);
		}finally{
			if(connection.isConnected()){
				connection.disconnect();
			}
			WebUtils.webSendString(res);
		}
	}
}
