package cn.honry.statistics.deptstat.difficultCaseHistoryAnalyse.action;

import java.util.Date;

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
 * 疑难重症病历人数比例统计分析
 * @author  yuke
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/DifficultCaseHistoryAnalyseAction")
//@Namespace(value = "/patient")
public class DifficultCaseHistoryAnalyseAction extends ActionSupport {

	private static final long serialVersionUID = 1L;


	/**
	 * 开始时间
	 */
	private String firstData;
	/**
	 * 结束时间
	 */
	private String endData;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 分页总条数
	 */
	private String page;
	/**
	 * 分页总页数
	 */
	private String rows;
	/**
	 * 查询条件  默认当前时间
	 */
	private String dataTime;
	
	/**
	 * 就诊卡号
	 */
	private String idCard;
	
	private String menuAlias;

	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	
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
	public String getFirstData() {
		return firstData;
	}
	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	/**
	 * 访问页面
	 * @author  yuke
	 */
	@Action(value="listPatientCost",results={@Result(name="list",location="/WEB-INF/pages/stat/deptstat/difficultCaseHistoryAnalyse/difficultCaseHistoryAnalyse.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String listPatientCost(){
		//获取时间
		Date date = new Date();
		endData = DateUtils.formatDateY_M_D(date);
		firstData = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
}
