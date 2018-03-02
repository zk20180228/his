package cn.honry.oa.meeting.meetingSigned.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.oa.meeting.meetingSigned.service.MeetingSignedService;
import cn.honry.oa.meeting.meetingSigned.vo.MeetingSignedVo;
import cn.honry.oa.meeting.meetingSigned.vo.SignedPersonInfoVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>会议签到统计action </p>
 * @Author: zhangkui
 * @CreateDate: 2017年8月29日 下午6:36:46 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年8月29日 下午6:36:46 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Namespace("/meeting/meetingSigned")
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
public class MeetingSignedAction extends ActionSupport {


	private static final long serialVersionUID = -6154653115437803783L;
	
	private String id;
	private String meetingName;//会议名称
	private String meetingRoomName;//会议室的名字
	private String meetingStatusFlag;//会议的状态标记
	
	private String searchField;//根据姓名，或员工号，或者科室名字，和签到状态搜索签到列表字段
	private String page;//当前第几页
	private String rows;//每页显示的记录数
	
	@Autowired
	@Qualifier("meetingSignedService")
	private MeetingSignedService meetingSignedService;
	public void setMeetingSignedService(MeetingSignedService meetingSignedService) {
		this.meetingSignedService = meetingSignedService;
	}



	//1.跳转到会议签到统计记录页面
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/toMeetSignedUI.action
	 * <p>跳转到会议签到统计记录页面 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午10:20:44 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午10:20:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/toMeetSignedUI",results={@Result(name="list",location="/WEB-INF/pages/oa/meeting/meetSigned/meetSignedUI.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toMeetSignedUI(){
		
		return  "list";
	}

	
	//跳转到会议签到信息列表(准时签到，迟到，未到，临时参加)页面
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/toMeetSignedInfoUI.action
	 * <p> </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月1日 下午3:43:06 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月1日 下午3:43:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="/toMeetSignedInfoUI",results={@Result(name="list",location="/WEB-INF/pages/oa/meeting/meetSigned/meetSignedInfoUI.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toMeetSignedInfoUI(){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("id", id);
		//准时签到
		Long total1 = meetingSignedService.onTimeNum(id,null);
		Long total2 = meetingSignedService.isLateNum(id,null);
		Long total3 = meetingSignedService.noComeNum(id,null);
		Long total4 = meetingSignedService.tempComeNum(id,null);
		request.setAttribute("onTimeNum", total1);
		request.setAttribute("isLateNum", total2);
		request.setAttribute("noComeNum", total3);
		request.setAttribute("tempComeNum", total4);
		request.setAttribute("total", total4+total1+total2+total3);
		
		return  "list";
	}


	
	//2.加载会议签到统计列表，包含查询条件，分页，排序
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/meetingSignedList.action?page=1&rows=10
	 * <p>加载会议签到统计列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午10:54:33 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午10:54:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/meetingSignedList",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void meetingSignedList(){
		
		List<MeetingSignedVo> list =meetingSignedService.meetingSignedList(meetingName,meetingRoomName,meetingStatusFlag,page,rows);
		Long total=meetingSignedService.meetingSignedCount(meetingName,meetingRoomName,meetingStatusFlag);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		
		String json = JSONUtils.toJson(map);
		
		WebUtils.webSendJSON(json);
	}
	
	
	//3.根据会议签到的id批量删除
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/delMeetingSigned.action?id=1
	 * <p>根据会议申请的id批量删除会议 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午10:54:45 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午10:54:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/delMeetingSigned",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void delMeetingSigned(){
		
		String flag="true";//默认删除成功
		try {
			meetingSignedService.delMeetingSignedById(id);
		} catch (Exception e) {
			 flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	
	//4.准时签到
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/onTimeList.action?id=001&page=1&rows=10
	 * <p>根据会议签到id查询准时签到列表,包含当前页面搜索功能 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:15:23 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:15:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="/onTimeList",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void onTimeList(){
		
		 List<SignedPersonInfoVo> list=meetingSignedService.onTimeList(id,searchField,page,rows);
		 Long total = meetingSignedService.onTimeNum(id,searchField);
		 
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("rows", list);
		 map.put("total", total);
		 String json= JSONUtils.toJson(map);
		
		 WebUtils.webSendJSON(json);
	}
	
	//5.迟到
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/isLateList.action?id=001&page=1&rows=10
	 * <p>根据会议签到的id查询迟到列表 ,包含当前页面搜索功能</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:16:04 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:16:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="/isLateList",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void isLateList(){
		
		 List<SignedPersonInfoVo> list=meetingSignedService.isLateList(id,searchField,page,rows);
		 Long total = meetingSignedService.isLateNum(id,searchField);
		 
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("rows", list);
		 map.put("total", total);
		 String json= JSONUtils.toJson(map);
		
		 WebUtils.webSendJSON(json);
	}
	
	//6.未到
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/noComeList.action?id=001&page=1&rows=10
	 * <p>根据会议签到的id查询未到列表 ,包含当前页面搜索功能</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:16:50 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:16:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="/noComeList",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void noComeList(){
		
		 List<SignedPersonInfoVo> list=meetingSignedService.noComeList(id,searchField,page,rows);
		 Long total = meetingSignedService.noComeNum(id,searchField);
		 
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("rows", list);
		 map.put("total", total);
		 String json= JSONUtils.toJson(map);
		
		 WebUtils.webSendJSON(json);
	}
	
	//7.临时参加
	/**
	 * http://localhost:8080/his-portal/meeting/meetingSigned/tempComeList.action?id=001&page=1&rows=10
	 * <p>根据会议签到的id查询临时参加列表 ,包含当前页面搜索功能</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年8月30日 上午11:17:06 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年8月30日 上午11:17:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */	
	@Action(value="/tempComeList",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void tempComeList(){
		
		 List<SignedPersonInfoVo> list=meetingSignedService.tempComeList(id,searchField,page,rows);
		 Long total = meetingSignedService.tempComeNum(id,searchField);
		 
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("rows", list);
		 map.put("total", total);
		 String json= JSONUtils.toJson(map);
		
		 WebUtils.webSendJSON(json);
	}
	
	
	
	//8.修改
	
	
	
	//9.撤销
	
	//10.新建
	
	
	public String getMeetingName() {
		return meetingName;
	}


	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}


	public String getMeetingRoomName() {
		return meetingRoomName;
	}


	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}


	public String getMeetingStatusFlag() {
		return meetingStatusFlag;
	}


	public void setMeetingStatusFlag(String meetingStatusFlag) {
		this.meetingStatusFlag = meetingStatusFlag;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getSearchField() {
		return searchField;
	}



	public void setSearchField(String searchField) {
		this.searchField = searchField;
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
	
	
	
	
	
	
	
	
}
