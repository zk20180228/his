package cn.honry.inpatient.bill.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.bill.service.BillclassService;
import cn.honry.inpatient.bill.service.BilllistService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inpatient/billlist")
public class BilllistAction extends ActionSupport implements ModelDriven<DrugBilllist>{
	
	private Logger logger=Logger.getLogger(BilllistAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 摆药明细
	 */
	private DrugBilllist billlist = new DrugBilllist();
	@Override
	public DrugBilllist getModel() {
		return billlist;
	}
	/**
	 * 栏目权限
	 */
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private BilllistService billlistService;
	
	public BilllistService getBilllistService() {
		return billlistService;
	}
	@Autowired
	@Qualifier(value = "billlistService")
	public void setBilllistService(BilllistService billlistService) {
		this.billlistService = billlistService;
	}
	private BillclassService billclassService;
	public BillclassService getBillclassService() {
		return billclassService;
	}
	@Autowired
	@Qualifier(value = "billclassService")
	public void setBillclassService(BillclassService billclassService) {
		this.billclassService = billclassService;
	}
	private String page;//起始页数
	private String rows;//数据列数
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
	/**
	 * pid    明细id
	 */
	private String pid;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	/**
	 * idss    明细id
	 */
	private String idss;
	
	public String getIdss() {
		return idss;
	}
	public void setIdss(String idss) {
		this.idss = idss;
	}
	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-8-28
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryBilllist")
	public void queryBilllist()throws Exception{
		try {
			DrugBilllist billlistSerc = new DrugBilllist();
			if(StringUtils.isNotBlank(pid)){
				DrugBillclass billclass = billclassService.get(pid);
				if(billclass != null){
					billlistSerc.setDrugBillclass(billclass);
				}
			}
			List<DrugBilllist> list = billlistService.getPage(page,rows,billlistSerc);
			int total = billlistService.getTotal(billlistSerc);
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("total", total);
			outmap.put("rows", list);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("BYGL_BYMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYMX", "摆药管理_摆药明细", "2", "0"), e);
		}
	}
	/**
	 * @Description:删除
	 * @Author：  lt
	 * @CreateDate： 2015-8-26
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYFLGL:function:delete"})
	@Action(value="delBilllist")
	public void delBilllist () throws Exception{
		String result="";
		try{
			billlistService.removeUnused(idss);
			result="success";
		}catch(Exception e){
			result="error";
			logger.error("BYGL_BYMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("BYGL_BYMX", "摆药管理_摆药明细", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
}
