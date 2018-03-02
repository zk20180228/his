package cn.honry.mobile.dbFile.action;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.inner.mobile.dbFile.service.ContactDBFileService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 联系人定时任务
 * @author hedong 
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace("/mosys/quartzToDBFile")
public class QuartzToDBFileAction extends ActionSupport{
	private Logger logger=Logger.getLogger(QuartzToDBFileAction.class);
	@Autowired
	@Qualifier(value = "contactDBFileService")
	private ContactDBFileService contactDBFileService;
	
	public void setContactDBFileService(ContactDBFileService contactDBFileService) {
		this.contactDBFileService = contactDBFileService;
	}

	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	
	/**
	 * 更新db文件-----清空db中的记录，插入新的数据，db文件上传至文件服务器，更新m_contactdb_version表最大版本号的文件地址。
	 * @param request
	 * @param response
	 */
	@Action(value = "quartzToDBFile")
	public void quartzToDBFile(){
		Map<String, String> map = new HashMap<String, String>();
		try {  
			//查询数据库最新版本号的db文件地址是否为null 如果为null则更新 否则不更新
				String path = Thread.currentThread().getContextClassLoader().getResource("contacts.db").getPath();
				String finalPath ="";
				if(path.indexOf(":")!=-1){//windows
					//path = path.replace("/D:", "d:");
					int index3 = path.indexOf(":");
					String tmpPath = path.substring(index3, path.length());// :/20170317HisEclipseWorkSpace/mobile/build/classes/contacts.db
					char tmpChar = path.charAt(index3-1);//D
					String lowerCaseChar = String.valueOf(tmpChar).toLowerCase();
					finalPath = lowerCaseChar+tmpPath;
					//d:/20170317HisEclipseWorkSpace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/mobile/WEB-INF/classes/contacts.db
				}else{//linux
					finalPath=path;
				}
			contactDBFileService.updateDataToDBFile(finalPath);
			map.put("resCode", "success");
			map.put("resMsg", "初始化成功！");
		}catch (Exception e) {
			logger.error(e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("LXRDSRW","联系人定时任务","2","0"), e);
			e.printStackTrace();
			map.put("resCode", "success");
			map.put("resMsg", "初始化成功！");
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
