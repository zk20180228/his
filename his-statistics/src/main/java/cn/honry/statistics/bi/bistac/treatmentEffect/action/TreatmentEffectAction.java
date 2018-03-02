package cn.honry.statistics.bi.bistac.treatmentEffect.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import cn.honry.statistics.bi.bistac.imStacData.service.ImStacDataService;
import cn.honry.statistics.bi.bistac.treatmentEffect.service.TreatmentEffectService;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/treatmentEffect")
public class TreatmentEffectAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	@Resource
	private ImStacDataService imStacDataService;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@Autowired
    @Qualifier(value = "treatmentEffectService")
 	private TreatmentEffectService treatmentEffectService;
	public void setTreatmentEffectService(TreatmentEffectService treatmentEffectService) {
		this.treatmentEffectService = treatmentEffectService;
	}
	
	/**
	 * 当前在院人数
	 * @return
	 */
	int inPeople;
	public int getInPeople() {
		return inPeople;
	}
	public void setInPeople(int inPeople) {
		this.inPeople = inPeople;
	}
	/**
	 * 治疗效果数据分析
	 * @return
	 */
	@Action(value = "treatmentEffectlist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/treatmentEffect.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String treatmentEffectlist() {
		//跳转的时候查询出当前在院人数
		inPeople=treatmentEffectService.queryInPeople();		
		return "list";
	}
	
	@Action(value = "treatmentEffectlist1", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/eachTreatmentEffect.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String treatmentEffectlist1() {
		return "list";
	}
	@Action(value = "treatmentEffectlist2", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/outTreatmentEffect.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String treatmentEffectlist2() {
		return "list";
	}
	@Action(value = "treatmentEffectlist3", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/outAreaConfirmed.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String treatmentEffectlist3() {
		return "list";
	}
	@Action(value = "treatmentEffectlist4", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/avgInHis.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String treatmentEffectlist4() {
		return "list";
	}
	
	/**
	 * 查询各科室治疗效果的人数
	 * @author zpty
	 * @CreateDate：2017-03-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */ 
	@Action(value="queryUserRecord")
	public void queryTreament(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String yearSelect = request.getParameter("yearSelect");
		List<TreatmentEffectVo> treatmentEffectList=treatmentEffectService.queryUserRecord(yearSelect);
		String json=JSONUtils.toJson(treatmentEffectList);
		WebUtils.webSendJSON(json);
	}
	
	
	
	/*
	 * 测试将数据同步到mongodb中
	 * @author zhangkui
	 * @CreateDate：2017-05-10
	 */
	//http://localhost:8080/his-portal/statistics/treatmentEffect/testSyncMongoDb.action
//	@Action(value="testSyncMongoDb")
//	public void testSyncMongoDb(){
//		try {
//			imStacDataService.imTableData_T_INPATIENT_INFO();
//			imStacDataService.imEachDay_T_INPATIENT_INFO();
//			imStacDataService.imTableData_T_INPATIENT_CANCELITEM();
//			imStacDataService.imEachDay_T_INPATIENT_CANCELITEM();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 查询各科室治疗效果的人数
	 * @author zhangkuis
	 * @CreateDate：2017-05-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */ 
	@Action(value="queryTreamentByMongo")
	public void queryTreamentByMongo(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String yearSelect = request.getParameter("yearSelect");
		List<TreatmentEffectVo> treatmentEffectList=treatmentEffectService.queryUserRecordByMongo(yearSelect);
		if(treatmentEffectList==null){
			 treatmentEffectList=treatmentEffectService.queryUserRecord(yearSelect);
		}else if(treatmentEffectList.size()<=0){
			 treatmentEffectList=treatmentEffectService.queryUserRecord(yearSelect);
		}
		String json=JSONUtils.toJson(treatmentEffectList);
		WebUtils.webSendJSON(json);
		
	}
	
	
	
}

