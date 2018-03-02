package cn.honry.statistics.deptstat.medicalIndex.action;

import java.io.File;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.utils.DateUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 肾病科室住院病人动态报表 ACTION
 * 
 * @author tangfeishuai
 * @CreateDate 2016年6月22日 上午9:47:41
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/MedicalIndex")
// @Namespace(value = "/stat")
public class MedicalIndexAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private String menuAlias;
	private String Stime;
	private String Etime;
	
	public String getStime() {
		return Stime;
	}

	public void setStime(String stime) {
		Stime = stime;
	}

	public String getEtime() {
		return Etime;
	}

	public void setEtime(String etime) {
		Etime = etime;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	private String webPath = "WEB-INF" + File.separator + "reportFormat" + File.separator + "jasper" + File.separator;
	/**

	/**
	 * 
	 * @Description： 获取list页面
	 * 
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41 @ModifyRmk：
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value = { "SBKELCGZYLZBTJBB:function:view" })
	@Action(value = "medicalIndexList", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/medicalIndex/medicalIndex.jsp") }, interceptorRefs = {@InterceptorRef(value = "manageInterceptor") })
	public String listRegisDocScheInfo() {
		return "list";

	}

}
