package cn.honry.statistics.bi.bistac.outpatientStac.action;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.statistics.bi.bistac.outpatientStac.service.OutpatientStacVoService;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.BusinessContractunitVo;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/outpatientStac")
public class OutpatientStacAction extends ActionSupport {
	@Autowired
	@Qualifier(value = "outpatientStacVoService")
	private OutpatientStacVoService outpatientStacVoService;
	//定制百分比
	private	DecimalFormat nf = new DecimalFormat("00.00%");
	
	@Action(value = "outpatientStacList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/outpatientStac.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String analyzeIndexList() {
		return "list";
	}
	@Action(value="queryOperationApplyVo")
	public void queryOperationApplyVo(){
		//手术例数vo
		OutpatientStacVo queryOperationApplyVo = outpatientStacVoService.queryOperationApplyVo();
		//当日手术例数
		int operationApply = queryOperationApplyVo.getDayOperationApply();
		//总手术例数
		int operationApplys = queryOperationApplyVo.getOperationApplys();
		//当日手术例数百分比
		String operationApplyPer=nf.format((float)operationApply/operationApplys);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("operationApply", operationApply);
		map.put("operationApplyPer", operationApplyPer);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryBusinessHospitalbedVo")
	public void queryBusinessHospitalbedVo(){
		//全院床位vo
		OutpatientStacVo queryBusinessHospitalbedVo = outpatientStacVoService.queryBusinessHospitalbedVo();
		//全院核定床位
		int businessHospitalbedTotal=queryBusinessHospitalbedVo.getBusinessHospitalbed();
		//全院总床位
		int businessHospitalbedTotals=queryBusinessHospitalbedVo.getBusinessHospitalbedTotals();
		//全院展开床位
		int businessHospitalbedTotalOver=businessHospitalbedTotals-businessHospitalbedTotal;
		//全院核定床位百分比
		String bedOrganPer = nf.format((float)businessHospitalbedTotal/businessHospitalbedTotals);
		//全院展开床位百分比
		String busTotalOverPer=nf.format(1-(float)businessHospitalbedTotal/businessHospitalbedTotals);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("businessHospitalbedTotal", businessHospitalbedTotal);
		map.put("businessHospitalbedTotalOver", businessHospitalbedTotalOver);
		map.put("bedOrganPer", bedOrganPer);
		map.put("busTotalOverPer", busTotalOverPer);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 住院量
	 * @Author zxh
	 * @time 2017年5月9日
	 */
	@Action(value="queryInpatientInfoNowVo")
	public void queryInpatientInfoNowVo(){
		//在院人数vo
		OutpatientStacVo queryInpatientInfoNowVo = outpatientStacVoService.queryInpatientInfoNowVo();
		//当前在院人数
		int inpatientInfoNowTotal=queryInpatientInfoNowVo.getInpatientInfoNow();
		
		List<BusinessContractunitVo> list = outpatientStacVoService.queryInOutNum();
		int goTotals=0;
		int newTotals=0;
		for(BusinessContractunitVo bcv : list){
			int outTotal = bcv.getJiNum();
			int inTotal = bcv.getMzNum();
			goTotals+=outTotal;
			newTotals+=inTotal;
		}
		
		for(BusinessContractunitVo bcv : list){
			int outTotal = bcv.getJiNum();
			int inTotal = bcv.getMzNum();
			if (inpatientInfoNowTotal==0) {
				bcv.setJiPer("00.00%");
				bcv.setMzPer("00.00%");
			}else{
				String outPer=nf.format((float)outTotal/inpatientInfoNowTotal);
				String inPer=nf.format((float)inTotal/inpatientInfoNowTotal);
				bcv.setJiPer(outPer);
				bcv.setMzPer(inPer);
			}
		}
		/*for (BusinessContractunitVo bcv : list) {
			int total = outpatientStacVoService.queryBusinessContractunitTotalGo(bcv.getEncode());
			goTotals+=total;
			if (inpatientInfoNowTotal==0) {
				bcv.setTotalPer("00.00%");
			}else{
				String bcvPer=nf.format((float)total/inpatientInfoNowTotal);
				bcv.setTotalPer(bcvPer);
			}
			bcv.setTotal(total);
		}
		List<BusinessContractunitVo> newList = outpatientStacVoService.queryBusinessContractunit();
		for (BusinessContractunitVo bcv : newList) {
			int total = outpatientStacVoService.queryBusinessContractunitTotalNew(bcv.getEncode());
			newTotals+=total;
			if (inpatientInfoNowTotal==0) {
				bcv.setTotalPer("00.00%");
			}else{
				String bcvPer=nf.format((float)total/inpatientInfoNowTotal);			
				bcv.setTotalPer(bcvPer);
			}
			bcv.setTotal(total);
		}*/
		String goPer=nf.format((float)goTotals/inpatientInfoNowTotal);
		String newPer=nf.format((float)newTotals/inpatientInfoNowTotal);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("inpatientInfoNowTotal", inpatientInfoNowTotal);
		map.put("goTotals", goTotals);
		map.put("newTotals", newTotals);
		map.put("goPer", goPer);
		map.put("newPer", newPer);
		map.put("listGo", list);
//		map.put("newList", newList);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value="queryRegistrationTotal")
	public void queryRegistrationTotal(){
		//门诊量vo
		OutpatientStacVo queryRegistrationVo = outpatientStacVoService.queryRegistrationVo();
		 //本日门诊量
		int registrationTotal =queryRegistrationVo.getCountRegistration();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("registrationTotal", registrationTotal);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 门诊量
	 * @Author zxh
	 * @time 2017年5月9日
	 */
	@Action(value="queryRegistrationVo")
	public void queryRegistrationVo(){
//		//门诊量vo
//		OutpatientStacVo queryRegistrationVo = outpatientStacVoService.queryRegistrationVo();
//		 //本日门诊量
//		int registrationTotal =queryRegistrationVo.getCountRegistration();
		//门诊总数
		int totalD=0;
		//急诊数量
		int totalJi=0;
		String dPer=null;
		String jiPer=null;
		List<BusinessContractunitVo> list = outpatientStacVoService.queryBusinessContractunit();
		for (BusinessContractunitVo bcv : list) {
			int mzTotal = bcv.getMzNum();
			totalD+=mzTotal;
		}
		int registrationTotal = totalD;
		for (BusinessContractunitVo bcv : list) {
			int mzTotal = bcv.getMzNum();
			int jiTotal = bcv.getJiNum();
			totalD+=mzTotal;
			totalJi+=jiTotal;
			if (registrationTotal==0) {
				bcv.setMzPer("00.00%");
				bcv.setJiPer("00.00%");
			}else{
				String mzOPer=nf.format((float)mzTotal/registrationTotal);
				bcv.setMzPer(mzOPer);
				String jiOPer=nf.format((float)jiTotal/registrationTotal);
				bcv.setJiPer(jiOPer);
			}
		}
		/*List<BusinessContractunitVo> list = outpatientStacVoService.queryBusinessContractunit();
		for (BusinessContractunitVo bcv : list) {
			int total = outpatientStacVoService.queryBusinessContractunitTotal(bcv.getEncode());
			totalD+=total;
			if (registrationTotal==0) {
				bcv.setTotalPer("00.00%");
			}else{
				String bcvPer=nf.format((float)total/registrationTotal);
				bcv.setTotalPer(bcvPer);
			}
			bcv.setTotal(total);
		}
		List<BusinessContractunitVo> listJi = outpatientStacVoService.queryBusinessContractunit();
		for (BusinessContractunitVo bcv : listJi) {
			int jiTotal = outpatientStacVoService.queryBusinessContractunitTotalJi(bcv.getEncode());
			totalJi+=jiTotal;
			if (registrationTotal==0) {
				bcv.setTotalPer("00.00%");
			}else{
				String bcvPer=nf.format((float)jiTotal/registrationTotal);
				bcv.setTotalPer(bcvPer);
			}
			bcv.setTotal(jiTotal);
		}*/
		if (registrationTotal==0) {
			dPer="100.00%";
			jiPer="00.00%";
		}else{
			dPer=nf.format((float)totalD/registrationTotal);
			jiPer=nf.format((float)totalJi/registrationTotal);
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("registrationTotal", registrationTotal);
		map.put("dPer", dPer);
		map.put("jiPer", jiPer);
		map.put("totalD", totalD);
		map.put("totalJi", totalJi);
		map.put("list", list);
//		map.put("listJi", listJi);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 门诊住院收入
	 * @Author zxh
	 * @time 2017年5月9日
	 */
	@Action(value="queryOutpatientStacVoD")
	public void queryOutpatientStacVoD(){
		//当日住院实收vo
		OutpatientStacVo queryInpatientFeeInfoNowCostD = outpatientStacVoService.queryInpatientFeeInfoNowCostD();
		//门诊实收vo
		OutpatientStacVo queryOutpatientFeedetailNowCostVoD = outpatientStacVoService.queryOutpatientFeedetailNowCostVoD();
		
		//当日门诊实收
		String outCost=NumberUtil.init().format(queryOutpatientFeedetailNowCostVoD.getDayMCost()/10000, 4);		
		//当日住院实收
		String inpCost=NumberUtil.init().format(queryInpatientFeeInfoNowCostD.getDayZCost()/10000,4);
		//当日实收
		Double costs=NumberUtil.init().toDoubleOrZero(outCost) + NumberUtil.init().toDoubleOrZero(inpCost);
		String inpCostPer=null;
		String outCostPer=null;
		if (costs==0.0) {
			inpCostPer="00.00%";
			outCostPer="00.00%";
		}else{
			//当日住院实收百分比
			inpCostPer=nf.format(NumberUtil.init().toDoubleOrZero(inpCost)/costs);
			//当日门诊实收百分比
			outCostPer=nf.format(NumberUtil.init().toDoubleOrZero(outCost)/costs);
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("outCostD", outCost);
		map.put("inpCostD", inpCost);
		map.put("costsD", costs);
		map.put("inpCostPerD", inpCostPer);
		map.put("outCostPerD", outCostPer);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryOutpatientStacVoM")
	public void queryOutpatientStacVoM(){
		//当月住院实收vo
		OutpatientStacVo queryInpatientFeeInfoNowCostM = outpatientStacVoService.queryInpatientFeeInfoNowCostM();
		//当月门诊实收vo
		OutpatientStacVo queryOutpatientFeedetailNowCostVoM = outpatientStacVoService.queryOutpatientFeedetailNowCostVoM();
		
		//当月门诊实收
		String outCostM=NumberUtil.init().format(queryOutpatientFeedetailNowCostVoM.getMonthMCost()/10000,4);
		
		//当月住院实收
		String inpCostM=NumberUtil.init().format(queryInpatientFeeInfoNowCostM.getMonthZCost()/10000,4);	
		
		//当月实收
		Double costsM=NumberUtil.init().toDoubleOrZero(outCostM) + NumberUtil.init().toDoubleOrZero(inpCostM);
		String inpCostPerM=null;
		String outCostPerM=null;
		if (costsM==0.0) {
			inpCostPerM="00.00%";
			outCostPerM="00.00%";
		}else{
			//当月住院实收百分比
			inpCostPerM=nf.format(NumberUtil.init().toDoubleOrZero(inpCostM)/costsM);	
			//当月门诊实收百分比
			outCostPerM=nf.format(NumberUtil.init().toDoubleOrZero(outCostM)/costsM);
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("outCostM", outCostM);
		map.put("inpCostM", inpCostM);
		map.put("costsM", costsM);
		map.put("inpCostPerM", inpCostPerM);
		map.put("outCostPerM", outCostPerM);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	@Action(value="queryOutpatientStacVoY")
	public void queryOutpatientStacVoY(){
		//当年住院实收vo
		OutpatientStacVo queryInpatientFeeInfoNowCostY = outpatientStacVoService.queryInpatientFeeInfoNowCostY();
		//当年门诊实收vo
		OutpatientStacVo queryOutpatientFeedetailNowCostVoY = outpatientStacVoService.queryOutpatientFeedetailNowCostVoY();
		
		//当年门诊实收
		String outCostY=NumberUtil.init().format(queryOutpatientFeedetailNowCostVoY.getYearMCost()/10000,4);
		//当年住院实收
		String inpCostY=NumberUtil.init().format(queryInpatientFeeInfoNowCostY.getYearZCost()/10000,4);
		
		//当年实收
		Double costsY=NumberUtil.init().toDoubleOrZero(outCostY) + NumberUtil.init().toDoubleOrZero(inpCostY);
		String inpCostPerY=null;
		String outCostPerY=null;
		if (costsY==0.0) {
			inpCostPerY="00.00%";
			outCostPerY="00.00%";
		}else{
			//当年住院实收百分比
			inpCostPerY=nf.format(NumberUtil.init().toDoubleOrZero(inpCostY)/costsY);	
			//当年门诊实收百分比
			outCostPerY=nf.format(NumberUtil.init().toDoubleOrZero(outCostY)/costsY);
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("outCostY", outCostY);
		map.put("inpCostY", inpCostY);
		map.put("costsY", costsY);
		map.put("inpCostPerY", inpCostPerY);
		map.put("outCostPerY", outCostPerY);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
}

