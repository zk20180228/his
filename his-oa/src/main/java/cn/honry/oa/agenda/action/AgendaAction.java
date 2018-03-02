package cn.honry.oa.agenda.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.Schedule;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.oa.agenda.service.AgendaService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;
/** 
* @ClassName: AgendaAction 
* @Description: 日程安排
* @author qh
* @date 2017年7月17日
*  
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/oa/agenda/agendaAction")
public class AgendaAction {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(AgendaAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "agendaService")
	private AgendaService agendaService;
	public void setAgendaService(AgendaService agendaService) {
		this.agendaService = agendaService;
	}
	private String t;
	private String stime;
	private String stimef;
	private String etimef;
	private String start;
	private String end;
	private String page;
	private String rows;
	private String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	private Schedule schedulel;
	public Schedule getSchedulel() {
		return schedulel;
	}
	public void setSchedulel(Schedule schedulel) {
		this.schedulel = schedulel;
	}
	public String getStimef() {
		return stimef;
	}
	public void setStimef(String stimef) {
		this.stimef = stimef;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtimef() {
		return etimef;
	}
	public void setEtimef(String etimef) {
		this.etimef = etimef;
	}
	private Schedule schedule;
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@RequiresPermissions(value={"RCAP:function:view"}) 
	@Action(value = "agendaActionToView", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/schedule/scheduleList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String agendaActionToView() {
		return "list";
	}
	@Action(value = "toLead", results = { @Result(name = "lead", location = "/WEB-INF/pages/oa/schedule/lead.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toLead() {
		schedule= agendaService.queryById(t);
		if(schedule.getDayFlg()==0){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			schedule.setBackgroundColor(format.format(schedule.getStart()));
			schedule.setTextColor(format.format(schedule.getEnd()));
		}else{
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			schedule.setBackgroundColor(format.format(schedule.getStart())+" 00:00:00");
			schedule.setTextColor(format.format(schedule.getEnd())+" 23:59:59");
		}
		return "lead";
	}
	@Action(value = "sheetListToView", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/schedule/scheduleSheetList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String sheetListToView() {
		return "list";
	}
	@Action(value = "toEditView", results = { @Result(name = "lead", location = "/WEB-INF/pages/oa/schedule/scheduleEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEditView() {
		schedule= agendaService.queryById(t);
		Integer dayFlg = schedule.getDayFlg();
		if(dayFlg==0){//非全天
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String s1 = format.format(schedule.getStart());
			String[] arr1 = s1.split(" ");
			stime=arr1[0];
			stimef=arr1[1];
			String s2 = format.format(schedule.getEnd());
			String[] arr2 = s2.split(" ");
			etimef=arr2[1];
		}else{//全天
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			stime=format.format(schedule.getStart());
			stimef="00:00";
			etimef="23:59";
		}
		if(flag=="1"){
			ServletActionContext.getRequest().getSession().setAttribute("flag", "1");
		}
		return "lead";
	}
	@Action(value = "toEdit",results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/schedule/scheduleEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toEdit(){
		try{
			//全天
			stime=t;
			Date date =new Date();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date);
			cal1.add(Calendar.HOUR_OF_DAY, 1);
			Date time = cal1.getTime();
			cal1.setTime(time); 
			int i = cal1.get(Calendar.HOUR_OF_DAY); 
			//非全天时间
			stimef=i+":00";
			etimef="23:59";
			if(flag=="1"){
				ServletActionContext.getRequest().getSession().setAttribute("flag", "1");
			}
			return "edit";
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			return "edit";
		}
	}
	@Action(value = "toAdd",results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/schedule/scheduleAdd.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toAdd(){
		try{
			//全天
			stime=t;
			Date date =new Date();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date);
			cal1.add(Calendar.HOUR_OF_DAY, 1);
			Date time = cal1.getTime();
			cal1.setTime(time); 
			int i = cal1.get(Calendar.HOUR_OF_DAY);
			//非全天时间
			stimef=i+":00";
			etimef="23:59";
			if(flag=="1"){
				ServletActionContext.getRequest().getSession().setAttribute("flag", "1");
			}
			return "edit";
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			return "edit";
		}
	}
	/** 
	* @Description: 查询当前登陆者所有日程
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value = "qeryScheduleList")
	public void  qeryScheduleList(){
		try{
			List<Schedule>  list=new ArrayList<Schedule>();
			String id = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			list=agendaService.qeryScheduleList(id);
			for(Schedule s:list){
				if(s.getDayFlg()==0){
					s.setAllDay(false);
					s.setEditable(true);
				}else{
					s.setAllDay(true);
					s.setEditable(false);
				}
			}
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
		}
	}
	/** 
	 * @Description: 分页查询当前登陆者所有日程
	 * @author qh
	 * @date 2017年7月19日
	 *  
	 */
	@Action(value = "queryScheduleList")
	public void  queryScheduleList(){
		try{
			List<Schedule>  list=new ArrayList<Schedule>();
			String id = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			list=agendaService.queryScheduleList(id,page,rows);
			int total=agendaService.queryScheduleListTotal(id);
			for(Schedule s:list){
				s.setAllDay(false);
				s.setEditable(true);
			}
			String json=JSONUtils.toJson(list);
			String json2="{\"total\":" +total+ ",\"rows\":" + json + "}";
			WebUtils.webSendJSON(json2);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
		}
	}
	/** 
	* @Description: 保存或者更新
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value="saveOrUpdate")
	public void saveOrUpdate(){
		try{
			String id = schedule.getId();
			if(StringUtils.isBlank(id)){//保存
				agendaService.save(schedule);
			}else{//修改
				Schedule schedule2 = agendaService.queryById(id);
				schedule.setCreateTime(schedule2.getCreateTime());
				schedule.setCreateDept(schedule2.getCreateDept());
				schedule.setCreateUser(schedule2.getCreateUser());
				schedule.setUserId(schedule2.getUserId());
				schedule.setUpdateTime(new Date());
				schedule.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				schedule.setIsFinish(schedule2.getIsFinish());
				agendaService.update(schedule);
			}
			String str="0";
			WebUtils.webSendJSON(str);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			String str="1";
			WebUtils.webSendJSON(str);
		}

	}
	/** 
	* @Description: 删除
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value="del")
	public void del(){
		Map<String,String> retMap = new HashMap<>();
		try{
			agendaService.del(t);
			retMap.put("resCode", "success");
			retMap.put("resMsg", "删除成功！");
		}catch(Exception e){
			retMap.put("resCode", "error");
			retMap.put("resMsg", "删除失败！");
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(retMap));
	}
	/** 
	* @Description: 动态更新(初始时间改变)
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value="liveUpdate")
	public void liveUpdate(){
		try{
			Schedule sch = agendaService.queryById(t);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newstart = format.parse(start.toString());
			Date newend = format.parse(end.toString());
		    long c=newstart.getTime()-sch.getStart().getTime();
		    Date newtime=new Date(sch.getTime().getTime()+c);
		    sch.setStart(newstart);
		    sch.setEnd(newend);
		    sch.setTime(newtime);
		    sch.setUpdateTime(new Date());
		    sch.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		    agendaService.update(sch);
			String str="0";
			WebUtils.webSendJSON(str);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			String str="1";
			WebUtils.webSendJSON(str);
		}
		
	}
	/** 
	 * @Description: 动态更新(时间长度改变)
	 * @author qh
	 * @date 2017年7月19日
	 *  
	 */
	@Action(value="sizeUpdate")
	public void sizeUpdate(){
		try{
			Schedule sch = agendaService.queryById(t);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newend = format.parse(end.toString());
			sch.setEnd(newend);
			sch.setUpdateTime(new Date());
			sch.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			agendaService.update(sch);
			String str="0";
			WebUtils.webSendJSON(str);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			String str="1";
			WebUtils.webSendJSON(str);
		}
		
	}
	/** 
	 * @Description:更新事件是否过期
	 * @author qh
	 * @date 2017年7月23日
	 *  
	 */
	@Action(value="updateFlag")
	public void updateFlag(){
		try{
			Schedule sch = agendaService.queryById(t);
			sch.setIsFinish(Integer.parseInt(flag));
			sch.setUpdateTime(new Date());
			sch.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			agendaService.update(sch);
			String str="0";
			WebUtils.webSendJSON(str);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			String str="1";
			WebUtils.webSendJSON(str);
		}
		
	}
	/** 
	 * @Description:更新事件是否过期
	 * @author qh
	 * @date 2017年7月23日
	 *  
	 */
	@Action(value="toFinished")
	public void toFinished(){
		try{
			Schedule sch = agendaService.queryById(t);
			sch.setIsFinish(1);
			sch.setUpdateTime(new Date());
			sch.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			agendaService.update(sch);
			String str="0";
			WebUtils.webSendJSON(str);
		}catch(Exception e){
			logger.error("OA_RCAP", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("OA_RCAP", "日程安排", "2", "0"), e);
			String str="1";
			WebUtils.webSendJSON(str);
		}
		
	}
	
	
	
	/** 
	* @Description: 查询下拉
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value="queryMR1")
	public void queryMR1(){
		List<Map<String,String>> list= new ArrayList<>();
		Map<String,String > map =new HashMap<String,String>();
		map.put("id", "0");		
	    map.put("text","准时");
	    Map<String,String > map2 =new HashMap<String,String>();
	    map2.put("id", "1");		
	    map2.put("text","10分钟之前");
	    Map<String,String > map3 =new HashMap<String,String>();
	    map3.put("id", "2");		
	    map3.put("text","30分钟之前");
	    Map<String,String > map4 =new HashMap<String,String>();
	    map4.put("id", "3");		
	    map4.put("text","一小时之前");
	    Map<String,String > map5 =new HashMap<String,String>();
	    map5.put("id", "4");		
	    map5.put("text","一天之前");
	    list.add(map);
	    list.add(map2);
	    list.add(map3);
	    list.add(map4);
	    list.add(map5);
	    String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/** 
	* @Description: 查询下拉
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value="queryMR2")
	public void queryMR2(){
		List<Map<String,String>> list= new ArrayList<>();
		Map<String,String > map =new HashMap<String,String>();
		map.put("id", "0");		
		map.put("text","当天9:00");
		Map<String,String > map2 =new HashMap<String,String>();
		map2.put("id", "1");		
		map2.put("text","前一天17:00");
		Map<String,String > map3 =new HashMap<String,String>();
		map3.put("id", "2");		
		map3.put("text","前一天9:00");
		Map<String,String > map4 =new HashMap<String,String>();
		map4.put("id", "3");		
		map4.put("text","前两天9:00");
		Map<String,String > map5 =new HashMap<String,String>();
		map5.put("id", "4");		
		map5.put("text","前一周9:00");
		list.add(map);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/** 
	* @Description: 查询下拉
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	@Action(value="queryUnit")
	public void queryUnit(){
		List<Map<String,String>> list= new ArrayList<>();
		Map<String,String > map =new HashMap<String,String>();
		map.put("id", "m");		
		map.put("text","分钟");
		Map<String,String > map2 =new HashMap<String,String>();
		map2.put("id", "h");		
		map2.put("text","小时");
		Map<String,String > map3 =new HashMap<String,String>();
		map3.put("id", "d");		
		map3.put("text","天");
		Map<String,String > map4 =new HashMap<String,String>();
		map4.put("id", "w");		
		map4.put("text","周");
		list.add(map);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/** 
	* @Description: 查询下拉
	* @author qh
	* @date 2017年7月23日
	*  
	*/
	@Action(value="queryFlag")
	public void queryFlag(){
		List<Map<String,String>> list= new ArrayList<>();
		Map<String,String > map =new HashMap<String,String>();
		map.put("id", "0");		
		map.put("text","未完成");
		Map<String,String > map2 =new HashMap<String,String>();
		map2.put("id", "1");		
		map2.put("text","已完成");
		list.add(map);
		list.add(map2);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
}
