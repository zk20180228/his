package cn.honry.oa.workProcessManage.action;

import java.util.ArrayList;
import java.util.List;

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

import cn.honry.oa.workProcessManage.service.WorkProcessManageService;
import cn.honry.oa.workProcessManage.vo.ProcessInfoVo;
import cn.honry.oa.workProcessManage.vo.WorkProcessManageVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>
 * 工作流程管理的控制器
 * </p>
 * 
 * @Author: zhangkui
 * @CreateDate: 2017年7月18日 上午11:30:10
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月18日 上午11:30:10
 * @ModifyRmk:
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Namespace("/oa/WorkProcessManage")
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
public class WorkProcessManageAction extends ActionSupport {

		private static final long serialVersionUID = -598107012972037522L;
		
		@Autowired
		@Qualifier("workProcessManageService")
		private WorkProcessManageService workProcessManageService;
		public void setWorkProcessManageService(WorkProcessManageService workProcessManageService) {
			this.workProcessManageService = workProcessManageService;
		}
		
		private String menuCode;//栏目的code
		private String processId;//流程id
		
		// 1.跳转到工作流程管理页面 http://localhost:8080/his-portal/oa/WorkProcessManage/workProcessMangerUI.action
		/**
		 * 
		 * <p> 跳转到工作流程管理页面</p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:37:43
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:37:43
		 * @ModifyRmk:
		 * @version: V1.0
		 * @return
		 * @throws:Exception
		 *
		 */
		@Action(value="workProcessMangerUI",results={@Result(name="list",location="/WEB-INF/pages/oa/workProcessManger/workProcessMangerUI.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String workProcessMangerUI(){
			HttpServletRequest request = ServletActionContext.getRequest();
			// 查询左侧栏目
			List<WorkProcessManageVo> list;
			try {
				list = workProcessManageService.fatherMenuList();
			} catch (Exception e) {
				list=new ArrayList<WorkProcessManageVo>();
				e.printStackTrace();
			}
			
			request.setAttribute("list", list);
			return "list";
		}
	
		// 2.根据父栏目code:展现左侧栏目的--子栏目http://localhost:8080/his-portal/oa/WorkProcessManage/spreadSonMenu.action?menuCode=100
		/**
		 * 
		 * <p>根据父栏目code:展现左侧栏目的--子栏目 </p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:38:42
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:38:42
		 * @ModifyRmk:
		 * @version: V1.0
		 * @param menuCode 栏目的code
		 * @throws:Exception
		 */
		@Action(value="spreadSonMenu" ,results={@Result(name="json",type="json")})
		public void spreadSonMenu(){
			
			List<WorkProcessManageVo> list=null;
			try {
				list = workProcessManageService.spreadSonMenu(menuCode);
			} catch (Exception e) {
				list=new ArrayList<WorkProcessManageVo>();
				e.printStackTrace();
			}
			String json = JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
		}
	
		//3.根据子栏目code，查询子栏目下的流程定义，倒序http://localhost:8080/his-portal/oa/WorkProcessManage/queryProcessList.action?menuCode=200
		/**
		 * 
		 * <p>根据子栏目code，查询子栏目下的流程详情列表，升序 </p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:38:42
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:38:42
		 * @ModifyRmk:
		 * @version: V1.0
		 * @param menuCode 栏目的code
		 * @throws:Exception
		 */
		@Action(value="queryProcessList",results={@Result(name="json",type="json")})
		public void queryProcessList(){
			
			List<ProcessInfoVo> list=null;
			try {
				list = workProcessManageService.queryProcessList(menuCode);
			} catch (Exception e) {
				list= new ArrayList<ProcessInfoVo>();
				e.printStackTrace();
			}
			String json = JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
			
			
		}
		
		
		//4.根据流程详情的id，查询流程说明http://localhost:8080/his-portal/oa/WorkProcessManage/queryProcessInfo.action?processId=001
		/**
		 * 
		 * <p>根据流程详情的id，查询流程说明 </p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年7月18日 上午11:38:42
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月18日 上午11:38:42
		 * @ModifyRmk:
		 * @version: V1.0
		 * @param menuCode 栏目的code
		 * @throws:Exception
		 */
		@Action(value="queryProcessInfo",results={@Result(name="json",type="json")})
		public void queryProcessInfo(){
			
			String info=null;
			try {
				info = workProcessManageService.queryProcessInfo(processId);
			} catch (Exception e) {
				info="";
				e.printStackTrace();
			}
			
			WebUtils.webSendString(info);
			
		}

		
		
		
		
		
		public String getMenuCode() {
			return menuCode;
		}

		public void setMenuCode(String menuCode) {
			this.menuCode = menuCode;
		}

		public String getProcessId() {
			return processId;
		}

		public void setProcessId(String processId) {
			this.processId = processId;
		}
		
		
		
		
		
		
		
	
	
}
