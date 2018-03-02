package cn.honry.inpatient.bill.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.bill.service.OfficinaTreeService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@SuppressWarnings({"all"})
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/billoffic")
public class OfficinaTreeAction extends ActionSupport implements ModelDriven<SysDepartment>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 栏目参数
	 */
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 科室
	 */
	private SysDepartment sysDepartment;
	
	private OfficinaTreeService officinaTreeService;
	
	public OfficinaTreeService getOfficinaTreeService() {
		return officinaTreeService;
	}
	@Autowired
	@Qualifier(value = "officinaTreeService")
	public void setOfficinaTreeService(OfficinaTreeService officinaTreeService) {
		this.officinaTreeService = officinaTreeService;
	}

	@Override
	public SysDepartment getModel() {
		return sysDepartment;
	}
	
	public SysDepartment getSysDepartment() {
		return sysDepartment;
	}
	public void setSysDepartment(SysDepartment sysDepartment) {
		this.sysDepartment = sysDepartment;
	}
	/**
	 * 标志
	 */
	private String flag;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**  
	 *  
	 * @Description：  药房药库下科室信息
	 * @Author：dh
	 * @CreateDate：2015-12-24 下午05:11:49
	 * @version 1.0
	 */
	@Action(value = "QuerytreeSysDepart")
	public void QuerytreeSysDepart() {
		List<TreeJson> treeDepar =  officinaTreeService.QueryTreeDepartmen(Integer.parseInt(flag));
		String json=JSONUtils.toJson(treeDepar);
		WebUtils.webSendJSON(json);
	}
}
