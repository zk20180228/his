package cn.honry.inpatient.outBalance.action;

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

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.inpatient.outBalance.service.InpatientInPrepayService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inpatient/outbalanceInpre")
public class InpatientInPrepayAction extends ActionSupport{
	/**
	 * 预交金		
	 */
	private InpatientInPrepay inpatientInPrepay=new InpatientInPrepay();
	private List<InpatientInPrepay> inpatientInPrepayList;
	@Autowired
	@Qualifier(value = "inpatientInPrepayService")
	private  InpatientInPrepayService inpatientInPrepayService;
	public void setInpatientInPrepayService(
			InpatientInPrepayService inpatientInPrepayService) {
		this.inpatientInPrepayService = inpatientInPrepayService;
	}
	/**
	 * 担保金
	 */
	private List<InpatientSurety> inpatientSuretyList;
	//分页
	private String page;
	private String rows;
	
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
	 * 住院号
	 */
	private String inpatientNo;
	/**
	 * 入院时间
	 */
	private String inDate;
	/**
	 * 当前时间
	 */
	private String outDate;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	/**
	 * @Description：  根据住院号查询预交金总额
	 * @Author：dh
	 * @CreateDate：2015-1-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */				
	@Action(value="queryPrepayCost")
	public void queryPrepayCost(){
		inpatientInPrepayList=inpatientInPrepayService.queryprepayCost(inpatientNo, outDate, inDate);
		String json=JSONUtils.toJson(inpatientInPrepayList);
		WebUtils.webSendJSON(json);
	}
}
