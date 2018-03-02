package cn.honry.oa.workProcessCount.action;

import java.util.ArrayList;
import java.util.HashMap;



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

import cn.honry.base.bean.model.User;
import cn.honry.oa.workProcessCount.service.WorkProcessCountService;
import cn.honry.oa.workProcessCount.vo.WorkProcessCountVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * <p> 工作流程控制器</p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月19日 下午7:01:00 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月19日 下午7:01:00 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Namespace("/oa/WorkProcessCount")
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
public class WorkProcessCountAction extends ActionSupport implements ModelDriven<WorkProcessCountVo>{

	private static final long serialVersionUID = -3154186804366568502L;
	
	private  WorkProcessCountVo workProcessCountVo=new WorkProcessCountVo();
	@Override
	public WorkProcessCountVo getModel() {
		
		return workProcessCountVo;
	}
	
	private String workId;//工作的id
	private String work_Flag;//work的类型,待办，办结，关注，挂起。。。编号，定义为常量，在常量包下
//	private String optionFlag;//操作的标识符，委托，挂起，批注，主办，导出，删除,催办,收回,收回委托,挂起恢复,取消关注。。。
	private String work_Name;//工作名称/文号
	private String serial_Number;//流水号
	
	private String page;//当前页
	private String rows;//每页显示的记录数
	
	@Autowired
	@Qualifier("workProcessCountService")
	private WorkProcessCountService workProcessCountService;
	public void setWorkProcessCountService(WorkProcessCountService workProcessCountService) {
		this.workProcessCountService = workProcessCountService;
	}

 	//1.跳转到工作流统计页面 http://localhost:8080/his-portal/oa/WorkProcessCount/workProcessCountUI.action
	/**
	 * 
	 * <p>跳转到工作流统计页面 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月19日 下午7:23:05 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月19日 下午7:23:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="workProcessCountUI",results={@Result(name="list",location="/WEB-INF/pages/oa/workProcessCount/workProcessCountUI.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String workProcessCountUI(){
		
		return "list";
	}
	
	
	public String getWork_Flag() {
		return work_Flag;
	}

	public void setWork_Flag(String work_Flag) {
		this.work_Flag = work_Flag;
	}

	//2.根据当前登录人的ID,workFlag(待办，办结，关注。。。),流水号或者工作名称/文号,展现工作流程列表，包含了查询功能，分页功能page,rows
	//http://localhost:8080/his-portal/oa/WorkProcessCount/workList.action?work_Flag=1&page=1&rows=10
	@Action(value="workList",results={@Result(name="json",type="json")})
	public void workList(){
		
		Map<String,Object> map=null;
		String userId=null;
	
		try {
//			if(work_Name!=null){//解码
//				work_Name=new String(work_Name.getBytes("iso-8859-1"),"utf-8");
//			}
			userId=this.getLoginUserId();
			System.out.println(work_Name);
			System.out.println(userId);//4028809e5c637ea8015c63b847c00003
			map=workProcessCountService.workList(userId,work_Flag,serial_Number,work_Name,page,rows);
		} catch (Exception e) {
			map=new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", new ArrayList<WorkProcessCountVo>());
			e.printStackTrace();
		}
		String json= JSONUtils.toJson(map);
		
		WebUtils.webSendJSON(json);
	}
	
	
	//3.根据workFlag(待办，办结，关注。。。),work实体新建工作
	@Action(value="addWork",results={@Result(name="json",type="json")})
	public void addWork(){
		
		String flag="true";//默认
		try {
			if(workProcessCountVo!=null){
				workProcessCountService.addWork(work_Flag,workProcessCountVo);
			}
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	
	
	
	
	
	//4.根据workFlag(待办，办结，关注。。。),工作流程的id，操作对应的编号(委托，挂起，批注，主办，导出，删除,催办,收回,收回委托,挂起恢复,取消关注。。。)进行相应操作
	//4.1 委托
	@Action(value="entrustWork",results={@Result(name="json",type="json")})
	public void entrustWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.entrustWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.2挂起
	@Action(value="hangUpwork",results={@Result(name="json",type="json")})
	public void hangUpwork(){
		
		String flag="true";//默认
		try {
			if(workId.split(",")!=null){
				workProcessCountService.hangUpwork(workId.split(","));
			}else{
				flag="false";
			}
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.3批注
	@Action(value="postilWork",results={@Result(name="json",type="json")})
	public void postilWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.postilWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.4主办
	@Action(value="hostWork",results={@Result(name="json",type="json")})
	public void hostWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.hostWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	} 
	
	//4.5导出
	@Action(value="exportWork",results={@Result(name="json",type="json")})
	public void exportWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.exportWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.6删除
	@Action(value="delWork",results={@Result(name="json",type="json")})
	public void delWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.delWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.7催办
	@Action(value="urgeDoWork",results={@Result(name="json",type="json")})
	public void urgeDoWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.urgeDoWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.8收回委托
	@Action(value="takeBackEntrustWork",results={@Result(name="json",type="json")})
	public void takeBackEntrustWork(){
		
		String flag="true";//默认
		try {
			workProcessCountService.takeBackEntrustWork(work_Flag,workId.split(","));
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	
	//4.9挂起恢复
	@Action(value="hangUpRecoveryWork",results={@Result(name="json",type="json")})
	public void hangUpRecoveryWork(){
		
		String flag="true";//默认
		try {
			if(workId.split(",")!=null){
				workProcessCountService.hangUpRecoveryWork(workId.split(","));
			}else{
				flag="false";
			}
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	//4.10取消关注
	@Action(value="cancelAttention",results={@Result(name="json",type="json")})
	public void cancelAttention(){
		
		String flag="true";//默认
		try {
			if(workId.split(",")!=null){
				workProcessCountService.deleteAttention(workId.split(","));
			}else{
				flag="fasle";
			}
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	
	//4.11关注
	@Action(value="attention",results={@Result(name="json",type="json")})
	public void attention(){
		
		String flag="true";//默认
		String userId=null;
		try {
			if(workId.split(",")!=null){
				userId=this.getLoginUserId();
				workProcessCountService.attention(userId,workId.split(","));
			}else{
				flag="fasle";
			}
		} catch (Exception e) {
			flag="false";
			e.printStackTrace();
		}
		
		WebUtils.webSendString(flag);
	}
	
	/**
	 * 
	 * <p>获取当前登录人的id </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月24日 下午2:17:14 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月24日 下午2:17:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	public String  getLoginUserId(){
		
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		
		return user.getId();
	}
	
	
	//启动一个流程实例，测试 http://localhost:8080/his-portal/oa/WorkProcessCount/startProcDef.action
	@Action(value="startProcDef")
	public void startProcDef(){
		String processDefId="permission:1:97504";
		workProcessCountService.startProcDef(processDefId);
	}
	
	//删除运行中的流程实例 http://localhost:8080/his-portal/oa/WorkProcessCount/delTask.action
	@Action(value="delTask")
	public void delTask(){
		String processInstanceId="125001";
		workProcessCountService.delTask(processInstanceId);
	}
	
	//办理任务,根据任务的id办理任务http://localhost:8080/his-portal/oa/WorkProcessCount/completeTask.action
	@Action(value="completeTask")
	public void completeTask(){
		String taskId="127505";
		workProcessCountService.complete(taskId);
	}
	
	//挂起任务,根据运行中的流程实例的id挂起一个流程实例http://localhost:8080/his-portal/oa/WorkProcessCount/suspendProcessInstanceById.action
	@Action(value="suspendProcessInstanceById")
	public void suspendProcessInstanceById(){
		
		String exectionId="135001";
		workProcessCountService.suspendProcessInstanceById(exectionId);
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

	public WorkProcessCountVo getWorkProcessCountVo() {
		return workProcessCountVo;
	}

	public void setWorkProcessCountVo(WorkProcessCountVo workProcessCountVo) {
		this.workProcessCountVo = workProcessCountVo;
	}


	public String getWork_Name() {
		return work_Name;
	}

	public void setWork_Name(String work_Name) {
		this.work_Name = work_Name;
	}

	public String getSerial_Number() {
		return serial_Number;
	}

	public void setSerial_Number(String serial_Number) {
		this.serial_Number = serial_Number;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}
	
	
	
	
	
}
