package cn.honry.outpatient.cpway.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.outpatient.cpway.service.CPWayService;
import cn.honry.outpatient.cpway.vo.CPWayVo;
import cn.honry.outpatient.cpway.vo.ComboxVo;
import cn.honry.outpatient.cpway.vo.PatientVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>临床路径申请/审批 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年12月25日 上午11:10:09 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年12月25日 上午11:10:09 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Namespace("/outpatient/CPWay")
@ParentPackage("global")
@Scope("protopyte")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
public class CPWayAction extends ActionSupport {

	private static final long serialVersionUID = 2673240461881979754L;
	
	@Resource
	private CPWayService cPWayService;
	public void setcPWayService(CPWayService cPWayService) {
		this.cPWayService = cPWayService;
	}

	//科室名字
	private String deptName;
	
	//科室编号或者患者的住院流水号
	private String id;
	
	//临床路径的申请id
	private String cPWAppId;
	
	//临床路径的id
	private String cPWId;
	
	//患者的临床路径vo,用于添加
	private CPWayVo cPWayVo;
	
	//住院流水号
	private String 	inpatient_no;
	
	//审批意见
	private String apply_status;
	
	private String page;//当前页
	private String rows;//每页显示的记录数
	
	/**
	 * 
	 * <p>跳转到临床路径申请页面 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:12:34 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:12:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *http://localhost:8080/his-portal/outpatient/CPWay/toCPWayApplyUI.action
	 */
	@Action(value="/toCPWayApplyUI",results={@Result(name="list",location="/WEB-INF/pages/inpatient/cpway/toCPWayApplyUI.jsp")})
	public String toCPWayApplyUI(){
		
		return "list";
	}
	
	/**
	 * 
	 * <p>跳转到临床路径审批页面</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:12:34 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:12:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/toCPWayApproveUI.action
	 */
	@Action(value="/toCPWayApproveUI",results={@Result(name="list",location="/WEB-INF/pages/inpatient/cpway/toCPWayApproveUI.jsp")})
	public String toCPWayApproveUI(){
		
		return "list";
	}
	
	/**
	 * 
	 * <p>住院科室树</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:17:40 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:17:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/inpatientDeptTree.action
	 */
	@Action("/inpatientDeptTree")
	public void inpatientDeptTree(){
		
		List<ComboxVo> list=null;
		try {
			//查询住院科室列表,可以模糊搜索
			list=cPWayService.inpatientDeptTree(deptName);
		} catch (Exception e) {
			list= new ArrayList<ComboxVo>();
			e.printStackTrace();
		}
		if(list==null){
			list= new ArrayList<ComboxVo>();
		}
		
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		//构建根节点
		TreeJson pTreeJson = new TreeJson();
		pTreeJson.setId("root");
		pTreeJson.setText("住院科室");
		pTreeJson.setIconCls("icon-branch");
		
		//渲染节点样式
		List<TreeJson> treeJsonListD = new ArrayList<TreeJson>();
		for(ComboxVo c:list){
			TreeJson treeJson = new TreeJson();
			treeJson.setId(c.getId());
			treeJson.setText(c.getText());
			treeJson.setIconCls("icon-bullet_home");
			treeJson.setState(c.getState());
			treeJsonListD.add(treeJson);
		}
		pTreeJson.setChildren(treeJsonListD);
		treeJsonList.add(pTreeJson);
		
		String json = JSONUtils.toJson(treeJsonList);
		WebUtils.webSendJSON(json);
		
	}
	
	/**
	 * 
	 * <p>根据科室编号查询该科室下的临床路径对应的患者列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:36:20 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:36:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/patientList.action
	 */
	@Action("/patientList")
	public void patientList(){
		List<ComboxVo> list=null;
		try {
			if(StringUtils.isNotBlank(id)){
				list=cPWayService.patientList(id);
			}
		} catch (Exception e) {
			list= new ArrayList<ComboxVo>();
			e.printStackTrace();
		}
		if(list==null){
			list= new ArrayList<ComboxVo>();
		}
		
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		for(ComboxVo c:list){
			TreeJson treeJson = new TreeJson();
			treeJson.setId(c.getId());
			treeJson.setText(c.getText());
			treeJson.setIconCls("icon-user_brown");
			treeJson.setState(c.getState());
			treeJsonList.add(treeJson);
		}
		
		String json = JSONUtils.toJson(treeJsonList);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 
	 * <p>根据科室编号或者患者的住院流水号查询 临床路径患者列表</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午2:46:14 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午2:46:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *http://localhost:8080/his-portal/outpatient/CPWay/cPWayPatientList.action
	 */
	@Action("/cPWayPatientList")
	public void cPWayPatientList(){
		
		List<CPWayVo> list=null;
		Integer total=0;
		try {
			//id可能是科室编号或者住院流水号
			if(StringUtils.isNotBlank(id)&&!"root".equals(id)){//root节点不加载
				list=cPWayService.cPWayPatientList(page,rows,id);
				total=cPWayService.cPWayPatientCount(id);
			}
		} catch (Exception e) {
			list= new ArrayList<CPWayVo>();
			e.printStackTrace();
		}
		if(list==null){
			list= new ArrayList<CPWayVo>();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 
	 * <p>临床路径申请添加页面 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月26日 下午3:48:28 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月26日 下午3:48:28 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="/toAddApplyUI",results={@Result(name="list",location="/WEB-INF/pages/inpatient/cpway/toAddApplyUI.jsp")})
	public String toAddApplyUI(){
		
		ServletActionContext.getRequest().setAttribute("apply_code", id);//科室编号存起来
		return "list";
	}
	
	/**
	 * 
	 * <p>添加患者临床路径 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:02:39 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:02:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/addCPWayPatient.action
	 */
	@Action("/addCPWayPatient")
	public void addCPWayPatient(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data", "error");
		if(cPWayVo!=null){
			try {
				cPWayService.addCPWayPatient(cPWayVo);
				map.put("data", "success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * <p>校验该住院流水号对应的患者是否已经添加临床路径</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:02:39 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:02:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/checkIsAdd.action
	 */
	@Action("/checkIsAdd")
	public void checkIsAdd(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data", "false");
		if(StringUtils.isNotBlank(inpatient_no)){
			try {
				Integer num=cPWayService.checkIsAdd(inpatient_no);
				if(num>0){
					map.put("data", "true");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * <p>根据住院流水号查询患者信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:30:55 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:30:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/findPatient.action
	 */
	@Action("/findPatient")
	public void findPatient(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(inpatient_no)){
			try {
				PatientVo p=cPWayService.findPatient(inpatient_no);
				if(p==null){
					map.put("data", "empty");//没有该患者对应的信息
				}else{
					map.put("data", p);
				}
			} catch (Exception e) {
				map.put("data", "error");//发生了异常
				e.printStackTrace();
			}
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * <p>查询临床路径的列表，便于用户下拉框选择 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:33:33 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:33:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/cPWList.action
	 */
	@Action("/cPWList")
	public void cPWList(){
		
		List<ComboxVo> list=null;
		try {
			list=cPWayService.cPWList();
		} catch (Exception e) {
			list= new ArrayList<ComboxVo>();
			e.printStackTrace();
		}
		if(list==null){
			list= new ArrayList<ComboxVo>();
		}
		
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
		
	}
	
	
	/**
	 * 
	 * <p>根据临床路径的id查询对应的版本号 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:59:10 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:59:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/findVersionList.action
	 */
	@Action("/findVersionList")
	public void findVersionList(){
		
		List<ComboxVo> list=null;
		try {
			list=cPWayService.findVersionList(cPWId);
		} catch (Exception e) {
			list= new ArrayList<ComboxVo>();
			e.printStackTrace();
		}
		if(list==null){
			list= new ArrayList<ComboxVo>();
		}
		
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
		
	}
	
	
	
	/**
	 * 
	 * <p>根据临床路径的申请id审批临床路径申请：apply_status(同意或者不同意 )</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:16:19 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:16:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/approveApply.action
	 */
	@Action("/approveApply")
	public void approveApply(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data", "error");
		if(StringUtils.isNotBlank(cPWAppId)&&StringUtils.isNotBlank(apply_status)){
			try {
				cPWayService.approveApply(cPWAppId,apply_status);
				map.put("data", "success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		
	}
	
	
	/**
	 * 
	 * <p>根据临床路径的申请id删除临床路径患者列表 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月25日 下午3:24:55 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月25日 下午3:24:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * http://localhost:8080/his-portal/outpatient/CPWay/delCPWayPatient.action
	 */
	@Action("/delCPWayPatient")
	public void delCPWayPatient(){
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data", "error");
		if(StringUtils.isNotBlank(cPWAppId)){
			try {
				cPWayService.delCPWayPatient(cPWAppId);
				map.put("data", "success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getcPWAppId() {
		return cPWAppId;
	}

	public void setcPWAppId(String cPWAppId) {
		this.cPWAppId = cPWAppId;
	}

	public String getcPWId() {
		return cPWId;
	}

	public void setcPWId(String cPWId) {
		this.cPWId = cPWId;
	}

	public CPWayVo getcPWayVo() {
		return cPWayVo;
	}

	public void setcPWayVo(CPWayVo cPWayVo) {
		this.cPWayVo = cPWayVo;
	}

	public String getInpatient_no() {
		return inpatient_no;
	}

	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}

	public String getApply_status() {
		return apply_status;
	}

	public void setApply_status(String apply_status) {
		this.apply_status = apply_status;
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
