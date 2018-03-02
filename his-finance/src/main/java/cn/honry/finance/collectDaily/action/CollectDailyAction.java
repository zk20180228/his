package cn.honry.finance.collectDaily.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientScDreport;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.finance.collectDaily.service.CollectDailyService;
import cn.honry.finance.collectDaily.vo.ColDaiVo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**   
*  
* @className：CcollectDaily
* @description：结算员日结action
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/collectDaily")
//@Namespace(value = "/inpatient")
public class CollectDailyAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(CollectDailyAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 注入collectDailyService
	 */
	@Autowired
	@Qualifier(value = "collectDailyService")
	private CollectDailyService collectDailyService;
	public void setCollectDailyService(CollectDailyService collectDailyService) {
		this.collectDailyService = collectDailyService;
	}
	
	/**
	 * 注入inpatientInfoInInterService公共接口
	 */
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;

	public void setParameterInnerService(
			ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 * 日结时间段的开始时间
	 * @return
	 */
	private String startTime;
	/**
	 * 日结时间段内的结束时间
	 * @return
	 */
	private String endTime;
	/**
	 * 收费员日结vo类
	 * @return
	 */
	private ColDaiVo colDaiVo;
	/**
	 * 日结明细json字符串
	 * @return
	 */
	private String date;
	/**
	 * 序列生成的统计序号
	 * @return
	 */
	private Integer sqcnum;
	/**
	 * 查询项目明细的类别标识1：医疗预收款借方金额2：医疗应收款贷方金额
	 * @return
	 */
	private String state;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getSqcnum() {
		return sqcnum;
	}

	public void setSqcnum(Integer sqcnum) {
		this.sqcnum = sqcnum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ColDaiVo getColDaiVo() {
		return colDaiVo;
	}

	public void setColDaiVo(ColDaiVo colDaiVo) {
		this.colDaiVo = colDaiVo;
	}

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

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * @Description:获取结算员日结list页面
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	@RequiresPermissions(value={"JSYRJ:function:view"})
	@Action(value="collectDailylist",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/collectDaily/collectDailyList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String collectDailylist() {
		return "list";
	}
	/**
	 * @Description:查询当前结算员日结结算表中最大的结算日期
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	**/
	@Action(value = "queryCollectMaxTime")
	public void queryCollectMaxTime(){
		try{
			List<InpatientScDreport> infol=collectDailyService.queryCollectMaxTime();
			Date date=new Date();
			Integer values=0;
			String value=parameterInnerService.getparameter("startTime");
			if(StringUtils.isNotBlank(value)){
				values=Integer.parseInt(value);
			}
			Calendar calendar = Calendar.getInstance();  
			calendar.setTime(date);  
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.add(Calendar.DAY_OF_MONTH, -values);  
			date = calendar.getTime();  
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			startTime=sdf.format(date);
			String json=JSONUtils.toJson(infol.size()>0?infol.get(0).getOperDate():startTime);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询结算时间内住院药品住院非药品的明细（条件结算人、开始时间、结束时间）
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	 * @throws ParseException 
	**/
	@Action(value = "querydetalDaily")
	public void querydetalDaily() throws ParseException{
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		 Date date1= sdf.parse(startTime);
		 Date date2= sdf.parse(endTime);
		 long day=(date2.getTime()-date1.getTime())/(24*60*60*1000); 
		 Integer values=0;
		 String value=parameterInnerService.getparameter("startTime");
		 if(StringUtils.isNotBlank(value)){
			 values=Integer.parseInt(value);
		 }
		 if(day-values>0){
			 Calendar calendar = Calendar.getInstance();  
	         calendar.setTime(date2);  
	         calendar.add(Calendar.DAY_OF_MONTH, -values);  
	         date1 = calendar.getTime();  
	         startTime=sdf.format(date1);
		 }
		 try{
			 List<ColDaiVo> infol=collectDailyService.querydetalDaily(startTime,endTime);
			 String json=JSONUtils.toJson(infol);
			 WebUtils.webSendJSON(json);
		 }catch(Exception e){
		    logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		 }
	}
	/**
	 * @Description:查询结算时间内收费员的日结费用信息（条件结算人、开始时间、结束时间）
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	**/
	@Action(value = "queryTableDaily")
	public void queryTableDaily(){
		try{
			ColDaiVo infol=collectDailyService.queryTableDaily(startTime,endTime);
			String json=JSONUtils.toJson(infol);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * 保存日结信息
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	**/
	@Action(value = "saveFromDaily")
	public void saveFromDaily(){
		Integer num=null;
		try {
			num = collectDailyService.saveFromDaily(colDaiVo,date,startTime,endTime);
		} catch (Exception e) {
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(num);
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:结算项目明细(结算头表)
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	**/
	@Action(value = "querymedicdatagridDaily")
	public void querymedicdatagridDaily() {
		try{
			List<InpatientBalanceHeadNow> num=collectDailyService.querymedicdatagridDaily(state,startTime,endTime);
			String json=JSONUtils.toJson(num);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * @Description:结算项目明细（预交金表）
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	**/
	@Action(value = "queryYjjDatagridDaily")
	public void queryYjjDatagridDaily() {
		try{
			List<InpatientInPrepayNow> num=collectDailyService.queryYjjDatagridDaily(startTime,endTime);
			String json=JSONUtils.toJson(num);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * 查询员工list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	**/
	@Action(value = "queryEmplistdaily")
	public void queryEmplistdaily() {
		try{
			List<SysEmployee> empList=inpatientInfoInInterService.queryEmpMapPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(SysEmployee emp : empList){
				map.put(emp.getJobNo(), emp.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * 查询用户list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	**/
	@Action(value = "queryUselistdaily")
	public void queryUselistdaily() {
		try{
			List<User> userlist=inpatientInfoInInterService.queryUserListPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(User user : userlist){
				map.put(user.getAccount(), user.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * 查询结算类别list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 **/
	@Action(value = "querySettdaily")
	public void querySettdaily() {
		try{
			String type="";
			String encode=null;
			List<BusinessDictionary> setmlist=inpatientInfoInInterService.queryDictionaryListForcomboboxPublic(type, encode);
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary codeSettlement : setmlist){
				map.put(codeSettlement.getEncode(), codeSettlement.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	/**
	 * 查询统计大类list
	 * @Author：  tcj
	 * @CreateDate： 2016-4-15
	 * @version 1.0
	 **/
	@Action(value = "queryfreecodedaily")
	public void queryfreecodedaily() {
		try{
			List<MinfeeStatCode> codefreelist=collectDailyService.queryfreecodedaily();
			Map<String,String> map=new HashMap<String,String>();
			for(MinfeeStatCode minfeeStatCode : codefreelist){
				map.put(minfeeStatCode.getFeeStatCode(), minfeeStatCode.getFeeStatName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_JSYRJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSYRJ", "住院收费_结算员日结", "2", "0"), e);
		}
	}
	
}
