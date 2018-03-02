package cn.honry.oa.meeting.emGroup.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.oa.meeting.emGroup.service.EmGroupService;
import cn.honry.oa.meeting.emGroup.vo.EmGroupVo;
import cn.honry.oa.meeting.emGroup.vo.GroupVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * <p>会议组action </p>
 * @Author: zhangkui
 * @CreateDate: 2017年9月5日 上午9:30:45 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年9月5日 上午9:30:45 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Namespace("/meeting/emGroup")
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
public class EmGroupAction extends ActionSupport{

	private static final long serialVersionUID = 841619132398550374L;
	
	private String id;//节点id(也可以作为会议组成员id)，用于根据节点id查询对应的datagrid列表
	private String text;//节点名，用于节点搜多
	
	private String employee_name;//员工姓名
	private String employee_jobon;//员工号
	private String dept_name;//科室名字
	
	private String page;//当前页
	private String rows;//每页显示的记录数
	
	
	
	@Autowired
	@Qualifier("emGroupService")
	private EmGroupService emGroupService;
	public void setEmGroupService(EmGroupService emGroupService) {
		this.emGroupService = emGroupService;
	}
	
	//1.加载组数据：tree树，包含搜索的关键字
	/**
	 * 
	 * <p>加载组的tree树，包含搜索条件</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午9:58:28 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午9:58:28 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/loadGroup",results={@Result(name="json",type="json")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public void loadGroup(){
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		List<GroupVo> childern =emGroupService.loadGroup(text);
		if(childern!=null&&childern.size()>0){
			childern.get(0).setChecked(true);//默认选中第一个节点
		}
		
		map.put("id", "ybGroup");//构建根节点，只有一个
		map.put("text", "组名");
		map.put("state", "open");
		map.put("children", childern);//构建根节点下的子节点
		
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(map);
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}

	//2.创建会议组,这个功能调用了<%=basePath %>baseinfo/pubCodeMaintain/addPubCode.action?type=ybGroup接口

	
	//3.根据组id加载datagrid，包含搜索条件，分页
	/**
	 * <p>加载组列表成员</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午10:28:10 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午10:28:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/groupList",results={@Result(name="json",type="json")})
	public void groupList(){
		
		List<EmGroupVo> list = new ArrayList<EmGroupVo>();
		int total=0;
		if(StringUtils.isNotBlank(id)){
			list=emGroupService.groupList(id,employee_name,employee_jobon,dept_name,page,rows);
			total=emGroupService.groupCount(id,employee_name,employee_jobon,dept_name);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		String json = JSONUtils.toJson(map);

		WebUtils.webSendJSON(json);
	}
	
	//4.添加人员页面
	/**
	 * http://localhost:8080/his-portal/meeting/emGroup/addEmpUI.action
	 * <p>添加人员页面 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月6日 上午9:58:40 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月6日 上午9:58:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="addEmpUI",results={@Result(name="list",location="/WEB-INF/pages/oa/meeting/meetSigned/addEmpUI.jsp")})
	public String addEmpUI(){
		
		//把组id，和名字一起传递
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("id", id);
		
		//组名为中文，所以需要转码
		 try {
			text=new String (request.getParameter("text").getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("text", text);
		return "list";
	}
	
	
	//5.保存添加人员
	/**
	 * 
	 * <p>前台传递过来员工号，底层根据员工号查询出员工信息，插入到会议组人员表，需要组id，组名 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月6日 下午2:18:15 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月6日 下午2:18:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/addEmp",results={@Result(name="json",type="json")})
	public void addEmp(){
		
		String flag="flase";
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(text)&&StringUtils.isNotBlank(employee_jobon)){
			
			emGroupService.addEmp(id,text,employee_jobon);
			flag="true";
		}
		
		WebUtils.webSendString(flag);
	}
	
	
	
	
	//6.删除人员
	/**
	 * 
	 * <p>根据id删除组成员 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月5日 上午10:28:39 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月5日 上午10:28:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action("/delEmployee")
	public void delEmployee(){
		
		String flag="false";
		if(StringUtils.isNotBlank(id)){
			emGroupService.delEmployeeById(id);
			flag="true";
		}
		
		WebUtils.webSendString(flag);
	}
	
	
	//7.加载指定科室
	/**
	 * http://localhost:8080/his-portal/meeting/emGroup/loadDept.action
	 * <p> 加载科室</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月7日 下午1:50:05 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月7日 下午1:50:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	
	@Action(value="loadDept",results={@Result(name="json",type="json")})
	public void loadDept(){

		List<GroupVo> list=emGroupService.loadDept(id);
		if(list==null){
			list= new ArrayList<GroupVo>();
		}
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getEmployee_jobon() {
		return employee_jobon;
	}
	
	public void setEmployee_jobon(String employee_jobon) {
		this.employee_jobon = employee_jobon;
	}
	
	public String getDept_name() {
		return dept_name;
	}
	
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
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
