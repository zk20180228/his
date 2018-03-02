package cn.honry.oa.meeting.meetingInfo.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.OaMeetingInfo;
import cn.honry.oa.meeting.meetingInfo.service.OaMeetingInfoService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/meeting/meetingInfo")
@SuppressWarnings({"all"})
public class OaMeetingInfoAction extends ActionSupport implements ModelDriven<OaMeetingInfo>{

	private OaMeetingInfo oaMeetingInfo = new OaMeetingInfo();

	public OaMeetingInfo getOaMeetingInfo() {
		return oaMeetingInfo;
	}

	public void setOaMeetingInfo(OaMeetingInfo oaMeetingInfo) {
		this.oaMeetingInfo = oaMeetingInfo;
	}

	@Autowired
	@Qualifier(value = "oaMeetingInfoService")
	private OaMeetingInfoService oaMeetingInfoService;

	public void setOaMeetingInfoService(OaMeetingInfoService oaMeetingInfoService) {
		this.oaMeetingInfoService = oaMeetingInfoService;
	}

	private List<OaMeetingInfo> oaMeetingInfoList;
	
	
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
	
	@Override
	public OaMeetingInfo getModel(){
		return oaMeetingInfo;
	}

	/**
	 * @Author zxh
	 * @time 2017年7月15日
	 * @return
	 */
	@Action(value = "showMeeting", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingInfo/meetingInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMeeting(){
		return "list";
	}
	
	
	/**
	 * 查询会议基本信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月17日 上午10:46:08 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月17日 上午10:46:08 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "queryMeeting")
	public void queryMeeting(){
		try{
			int total = oaMeetingInfoService.getTotal(oaMeetingInfo);
			oaMeetingInfo.setPage(page);
			oaMeetingInfo.setRows(rows);
			oaMeetingInfoList = oaMeetingInfoService.query(oaMeetingInfo);
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("total", total);
			map.put("rows", oaMeetingInfoList);
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 增加会议基本信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月15日 上午11:31:44 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月15日 上午11:31:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "addMeeting", results = { @Result(name = "add", location = "/WEB-INF/pages/oa/meeting/meetingInfo/addMeetingInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addMeeting(){
		return "add";
	}
	
	/**
	 * 保存会议基本信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月17日 下午4:21:49 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月17日 下午4:21:49 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "saveMeetingInfo")
	public void saveMeetingInfo(){
		try{
			if(StringUtils.isNotBlank(oaMeetingInfo.getMeetCode())){
				String string = oaMeetingInfoService.checkMeetCode(oaMeetingInfo.getMeetCode(),oaMeetingInfo.getId());
				if("ok".equals(string)){
					oaMeetingInfoService.saveOrUpdate(oaMeetingInfo);
					WebUtils.webSendString("success");
				}else{
					WebUtils.webSendString(string);
				}
			}else{
				WebUtils.webSendString("code为空");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 检查会议室编号是否已经被使用。此方法目前没用
	 */
	@Action(value = "checkMeetId")
	public void checkMeetId(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String meetId = request.getParameter("meetId");
//			oaMeetingInfoService.checkMeetId(meetId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除会议基本信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月17日 下午4:22:05 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月17日 下午4:22:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "delMeeting", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/meeting/meetingInfo/meetingInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delMeeting(){
		try{
			oaMeetingInfoService.del(oaMeetingInfo.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "list";
	}
	
	/**
	 * 修改会议基本信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月17日 下午4:23:51 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月17日 下午4:23:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value = "editMeeting", results = { @Result(name = "edit", location = "/WEB-INF/pages/oa/meeting/meetingInfo/addMeetingInfo.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editMeeting(){
		try{
			oaMeetingInfo = oaMeetingInfoService.get(oaMeetingInfo.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "edit";
	}
	@Action(value = "showMeetingRoom", results = { @Result(name = "show", location = "/WEB-INF/pages/oa/meeting/meetingInfo/showMeetingRoom.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMeetingRoom(){
		try{
			oaMeetingInfo = oaMeetingInfoService.get(oaMeetingInfo.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "show";
	}
	
	/**
	 * 查询所有会议室
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月19日 下午5:27:59 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月19日 下午5:27:59 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "findMeetingRoom")
	public void findMeetingRoom(){
		try{
			OaMeetingInfo o = new OaMeetingInfo();
			o.setMeetState("findY");
			List<OaMeetingInfo> oaMeetingInfoList = oaMeetingInfoService.query(o);
			String json = JSONUtils.toJson(oaMeetingInfoList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}