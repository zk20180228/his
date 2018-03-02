package cn.honry.statistics.sys.medicalFeeDetail.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.finance.statistic.action.StatisticAction;
import cn.honry.statistics.sys.medicalFeeDetail.service.MedicalFeeDetailService;
import cn.honry.statistics.sys.medicalFeeDetail.vo.FeeNameVo;
import cn.honry.statistics.sys.medicalFeeDetail.vo.InpatientFeeVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/statistics/MedicalFeeDetail")
@SuppressWarnings({"all"})
public class MedicalFeeDetailAction extends ActionSupport{

	private ObjectMapper mapperObject = new ObjectMapper();
    public static final String CODEPREFIX = "totCost";
	@Autowired
	@Qualifier(value = "medicalFeeDetailService")
	private MedicalFeeDetailService medicalFeeDetailService;	
	// 记录异常日志
	private Logger logger = Logger.getLogger(StatisticAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	public void setMedicalFeeDetailService(
			MedicalFeeDetailService medicalFeeDetailService) {
		this.medicalFeeDetailService = medicalFeeDetailService;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
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
	 * 住院患者信息实体类
	 */
	private InpatientInfo inpatientInfo;
	
	public InpatientInfo getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(InpatientInfo inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @Description:获取list页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-5-29
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"STAT-ZYYYFYMX:function:view"})
	@Action(value="toViewMedicalFeeDetail",results={@Result(name="list",location = "/WEB-INF/pages/stat/medicalFeeDetail/medicalFeeDetail.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewMedicalFeeDetail()throws Exception{
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(sys!=null){
			 ServletActionContext.getRequest().setAttribute("deptId",sys.getId());//获取当前病区
		}
		return "list";
	}
	/**
	 * @Description:查询列标题
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-20
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryMedicalFeeDetailTitle", results = { @Result(name = "json", type = "json") })
	public void queryMedicalFeeDetailTitle(){
		try{
			FeeNameVo feeNameVo = new FeeNameVo();
			FeeNameVo feeNameVo1 = new FeeNameVo();
			FeeNameVo feeNameVo2 = new FeeNameVo();
			FeeNameVo feeNameVo3 = new FeeNameVo();
			FeeNameVo feeNameVo4 = new FeeNameVo();
			List<FeeNameVo> feeNameList = medicalFeeDetailService.queryFeeName();
			int listSize = 0;
			if(feeNameList!=null && feeNameList.size()>0){
				listSize = feeNameList.size();
				for(int z=0;z<feeNameList.size();z++){
					if("其它".equals(feeNameList.get(z).getFeeStatName())){
						feeNameList.add(feeNameList.size(), feeNameList.get(z));
						feeNameList.remove(z);
					}
				}
			}
			feeNameVo.setFeeStatCode("medicalrecordId");
			feeNameVo.setFeeStatName("病历号");
			feeNameList.add(0, feeNameVo);
			feeNameVo1.setFeeStatCode("patientName");
			feeNameVo1.setFeeStatName("姓名");
			feeNameList.add(1, feeNameVo1);
			feeNameVo2.setFeeStatCode("prepayCost");
			feeNameVo2.setFeeStatName("预付款");
			feeNameList.add(2, feeNameVo2);
			feeNameVo3.setFeeStatCode("feeTot");
			feeNameVo3.setFeeStatName("费用合计");
			feeNameList.add(3+listSize, feeNameVo3);
			feeNameVo4.setFeeStatCode("exitFee");
			feeNameVo4.setFeeStatName("出院退款");
			feeNameList.add(4+listSize, feeNameVo4);
			String json = JSONUtils.toJson(feeNameList);
			WebUtils.webSendJSON(json);		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_ZYHZZYYYFMX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZZYYYFMX", "住院统计分析_在院患者在院费用及明细查询", "2","0"), e);
		}
	}
	
	/**
	 * @Description:查询列标题
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-20
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryMedicalFeeDetails", results = { @Result(name = "json", type = "json") })
	public void queryMedicalFeeDetails(){
		try{
			List<InpatientInfoNow> inpatientInfoList = medicalFeeDetailService.queryInpatientInfo(inpatientInfo.getMedicalrecordId(),startTime,endTime);
			StringBuffer jsons = new StringBuffer();
			jsons.append("[");
			DecimalFormat df   = new DecimalFormat("######0.00");
			if(inpatientInfoList!=null && inpatientInfoList.size()>0){
				List<FeeNameVo> feeNameList = medicalFeeDetailService.queryFeeName();
				Map<String,Double> feeNameAndCostMap=new HashMap<String,Double>();
				if(inpatientInfo == null){
					inpatientInfo = new InpatientInfo();
				}
				//判断inpatient是否已被计算
				Map<String,Boolean> map=new HashMap<String,Boolean>();
				double feeTot;//费用合计
				double exitFee;//出院退费
				double prepayCost=0.0d ;//预交金
				boolean flag=true;;//判断是否有多条记录
				double tempCost=0.0d;//临时
				/**患者最小费用明细汇总**/
				List feeCodeList=new ArrayList();
				for(int i=0,len=inpatientInfoList.size()+1,j;i<len;i++){
					feeTot = 0;
					exitFee = 0;
					if(flag){
						prepayCost = 0.0d;
					}
					//当前住院号
					String inpatientNo;
					if(i<len-1){
						inpatientNo=inpatientInfoList.get(i).getInpatientNo();
					}else{
						inpatientNo="";
					}
					if(i<len-1&&null!=map.get(inpatientNo)){//如果inpatientNo已经存在在map，统计最小费用集合
						//最小费用患者统计到集合中
						feeNameAndCostMap.put(inpatientInfoList.get(i).getProfCode(),inpatientInfoList.get(i).getHeight());
						continue;
					}else{
						if(i>0&&i<len){
							j=i-1;
						}else{
							j=i;
						}
						if(null!=map.get(inpatientInfoList.get(j).getInpatientNo())){
							for(int q=0,lenName=feeNameList.size();q<lenName;q++){//从feeNameAndCostMap循环出feecode和花费 并计算						
								String tempCode=feeNameList.get(q).getFeeStatCode();
								if(feeNameAndCostMap.get(tempCode)!=null){
									tempCost=feeNameAndCostMap.get(tempCode);
									jsons.append("\""+tempCode+"\":"+df.format(tempCost)+",");
									feeNameAndCostMap.remove(tempCode);
								}else{
									tempCost=0.0d;
									jsons.append("\""+tempCode+"\":0,");
								}
								feeTot =feeTot+tempCost;
							}
							exitFee = prepayCost - feeTot;
							if((j<len-2) ){
								jsons.append("\"feeTot\":"+df.format(feeTot)+",\"exitFee\":"+df.format(exitFee)+"},");
							}else if((j==len-2)){
								jsons.append("\"feeTot\":"+df.format(feeTot)+",\"exitFee\":"+df.format(exitFee)+"}]");
							}
							flag=true;
						}
					}
					if(i<len-1&&null==map.get(inpatientNo)){
						prepayCost = inpatientInfoList.get(i).getPrepayCost();
						map.put(inpatientNo, flag);
						jsons .append("{\"inpatientNo\":\""+inpatientNo+"\",\"patientName\":\""+inpatientInfoList.get(i).getPatientName()+"\",\"prepayCost\":"+prepayCost+",");	
						feeNameAndCostMap.put(inpatientInfoList.get(i).getProfCode(),inpatientInfoList.get(i).getHeight());
						flag=false;
					}
				}
			}
			WebUtils.webSendJSON(jsons.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error(menuAlias, e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZZYYYFMX", "住院统计分析_在院患者在院费用及明细查询", "2","0"), e);
		}
	}

	@Action(value = "queryMedicalFeeDetailsByES", results = { @Result(name = "json", type = "json") })
	public void queryMedicalFeeDetailsByES() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<InpatientFeeVo> list = new ArrayList<>();
            map.putAll(this.medicalFeeDetailService.queryFeeDetailsByES(inpatientInfo.getMedicalrecordId(), startTime, endTime, page, rows));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(menuAlias, e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZZYYYFMX", "住院统计分析_患者在院费用明细查询", "2","0"), e);
		} finally{
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
	}
}
