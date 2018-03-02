package cn.honry.inpatient.clinicalPathway.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.CpExecute;
import cn.honry.base.bean.model.CpExecuteDetail;
import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.bean.model.CpVcontrol;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InpAccess;
import cn.honry.inpatient.clinicalPathway.service.ClinicalPathwayService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/clinicalPathwayAction")
public class ClinicalPathwayAction extends ActionSupport {

	/**  
	 * 临床路径
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月18日 下午5:01:34 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月18日 下午5:01:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "clinicalPathwayService")
	private ClinicalPathwayService clinicalPathwayService;
	
	public void setClinicalPathwayService(
			ClinicalPathwayService clinicalPathwayService) {
		this.clinicalPathwayService = clinicalPathwayService;
	}

	private CpWay cpWay = new CpWay();

	public CpWay getCpWay() {
		return cpWay;
	}

	public void setCpWay(CpWay cpWay) {
		this.cpWay = cpWay;
	}
	
	private CpVcontrol cpVcontrol = new CpVcontrol();

	public CpVcontrol getCpVcontrol() {
		return cpVcontrol;
	}

	public void setCpVcontrol(CpVcontrol cpVcontrol) {
		this.cpVcontrol = cpVcontrol;
	}

	private CpwayPlan cpwayPlan = new CpwayPlan();
	
	public CpwayPlan getCpwayPlan() {
		return cpwayPlan;
	}

	public void setCpwayPlan(CpwayPlan cpwayPlan) {
		this.cpwayPlan = cpwayPlan;
	}

	/**
	 * 临床路径页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月18日 下午5:10:17 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月18日 下午5:10:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="queryList",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/clinicalPathway/clinicalPathwayList.jsp") })
	public String queryList(){
		return "list";
	}
	/**
	 * 患者临床路径页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月18日 下午5:10:17 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月18日 下午5:10:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="queryPatientList",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/patientClinicalPath/patientClinicalPath.jsp") })
	public String queryPatientList(){
		return "list";
	}
	/**
	 * 临床路径阶段评估界面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月26日 上午9:47:14 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月26日 上午9:47:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="showClinicalStageAssess",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/clinicalStageAssess/clinicalStageAssess.jsp") })
	public String showClinicalStageAssess(){
		return "list";
	}
	/**
	 * 导入临床路径页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月22日 下午3:22:38 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月22日 下午3:22:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="showImpPath",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/patientClinicalPath/impPatientClinicalPath.jsp") })
	public String showImpPath(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String impPatientNo = request.getParameter("id");
		String impdeptCode = request.getParameter("deptCode");
		request.setAttribute("impPatientNo", impPatientNo);
		request.setAttribute("impdeptCode", impdeptCode);
		return "list";
	}
	
	
	/**
	 * 查询病种（临床路径）树
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月20日 下午2:57:26 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月20日 下午2:57:26 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="treeClinicalPath", results = { @Result(name = "json", type = "json") })
	public void treeClinicalPath(){
		List<TreeJson> treeDepar = new ArrayList<TreeJson>();
		treeDepar = clinicalPathwayService.queryTree();
		String json = JSONUtils.toJson(treeDepar);
		System.out.println(json);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 跳转到添加病种窗口
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月20日 上午10:36:03 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月20日 上午10:36:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *                                                         
	 */
	@Action(value="addDisease",results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/clinicalPathway/addDisease.jsp") })
	public String addDisease(){
		return "add";
	}
	
	/**
	 * 跳转到添加版本界面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月21日 上午9:36:16 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月21日 上午9:36:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="addVersion",results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/clinicalPathway/addVersion.jsp") })
	public String addVersion(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cpId = request.getParameter("type");
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){//是修改
			cpVcontrol = clinicalPathwayService.findCpVcontrolById(id);
		}else{
			CpWay cpWayChoose = clinicalPathwayService.findCpWayById(cpId);
			cpVcontrol.setCpId(cpId);
			cpVcontrol.setCpName(cpWayChoose.getCpName());
		}
		return "add";
	}
	
	/**
	 * 判断该版本是否审批，审批不能修改
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月21日 下午6:04:58 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月21日 下午6:04:58 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="checkApproval" , results = { @Result(name = "json", type = "json") })
	public void checkApproval(){
		String result = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){//是修改
			CpVcontrol cv = clinicalPathwayService.findCpVcontrolById(id);
			if("0".equals(cv.getApprovalFlag())){
				result="已审批，不能修改";
			}else{
				result = "success";
			}
		}else{
			result = "网络异常，请联系管理员";
		}
		WebUtils.webSendString(result);
	}
	/**
	 * 添加临床路径（病种）
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月20日 下午3:31:12 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月20日 下午3:31:12 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="addPathway" , results = { @Result(name = "json", type = "json") })
	public void addPathway(){
		String result = "true";
		try{
			clinicalPathwayService.addPathway(cpWay);
		}catch(Exception e){
			e.printStackTrace();
			result = "false";
		}
		WebUtils.webSendString(result);
	}
	
	/**
	 * 添加版本
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月20日 下午3:35:10 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月20日 下午3:35:10 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="saveVersion", results = { @Result(name = "json", type = "json") })
	public void saveVersion(){
		String result = "true";
		try{
			if(clinicalPathwayService.checkIsOnly(cpVcontrol)){
				clinicalPathwayService.addVersion(cpVcontrol);
			}else{
				result = "此临床路径已有该版本号";
			}
		}catch(Exception e){
			e.printStackTrace();
			result = "false";
		}
		WebUtils.webSendString(result);
	}
	
	/**
	 * 标准名称下拉
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月20日 下午5:27:10 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月20日 下午5:27:10 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryStand", results = { @Result(name = "json", type = "json") })
	public void queryStand(){
		List<InoroutStandard> list = new ArrayList<InoroutStandard>();
		list = clinicalPathwayService.queryStand();
		WebUtils.webSendString(JSONUtils.toJson(list));
	}
	
	/**
	 * 临床路径（病种）下拉
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月21日 上午10:32:02 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月21日 上午10:32:02 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryCpWay", results = { @Result(name = "json", type = "json") })
	public void queryCpWay(){
		List<CpWay> list = new ArrayList<CpWay>();
		list = clinicalPathwayService.queryCpWay();
		WebUtils.webSendString(JSONUtils.toJson(list));
	}
	/**
	 * 查询临床路径下的版本
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月22日 下午3:51:27 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月22日 下午3:51:27 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryVersionByCpWay", results = { @Result(name = "json", type = "json") })
	public void queryCpWayVersion(){
		List<CpVcontrol> list = new ArrayList<CpVcontrol>();
		try{
			String cpId = ServletActionContext.getRequest().getParameter("cpId");
			if(StringUtils.isNotBlank(cpId)){
				list = clinicalPathwayService.queryCpWayVersion(cpId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		WebUtils.webSendString(JSONUtils.toJson(list));
	}
	
	/**
	 * 标准版本号下拉
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月21日 上午11:41:16 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月21日 上午11:41:16 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryVersion", results = { @Result(name = "json", type = "json") })
	public void queryVersion(){
		List<InoroutStandard> list = new ArrayList<InoroutStandard>();
		HttpServletRequest request = ServletActionContext.getRequest();
		String standCode = request.getParameter("standCode");
		list = clinicalPathwayService.queryVersion(standCode);
		WebUtils.webSendString(JSONUtils.toJson(list));
	}
	/**
	 * 根据ID临床路径版本明细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月21日 下午8:27:11 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月21日 下午8:27:11 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryVersionById", results = { @Result(name = "json", type = "json") })
	public void queryVersionById(){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		List<CpVcontrol> list = new ArrayList<CpVcontrol>();
		if(StringUtils.isNotBlank(id)){
			CpVcontrol cv = clinicalPathwayService.findCpVcontrolById(id);
			list.add(cv);
			map.put("rows", list);
		}else{
			map.put("rows", list);
		}
		WebUtils.webSendString(JSONUtils.toJson(map));
	}
	
	/**
	 * 根据ID查临床路径明细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月22日 上午9:54:29 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月22日 上午9:54:29 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryDiseaseById", results = { @Result(name = "json", type = "json") })
	public void queryDiseaseById(){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		String cpId = request.getParameter("cpId");
		List<CpWay> list = new ArrayList<CpWay>();
		if(StringUtils.isNotBlank(cpId)){
			CpWay cw = clinicalPathwayService.findCpWayById(cpId);
			list.add(cw);
			map.put("rows", list);
		}else{
			map.put("rows", list);
		}
		WebUtils.webSendString(JSONUtils.toJson(map));
	}
	
	/**
	 * 往计划表中添加时间段（给临床路径版本添加时间段）
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月22日 下午1:43:52 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月22日 下午1:43:52 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="addTimeToClinical", results = { @Result(name = "json", type = "json")})
	public void addTimeToClinical(){
		String result = "";
		try{
			/**还需要检验时间段的一步！！！！！！**/
			HttpServletRequest request = ServletActionContext.getRequest();
			String s = request.getParameter("sTime");
			String e = request.getParameter("eTime");
			String cpId = request.getParameter("cpId");
			String versionId = request.getParameter("versionId");
			if(StringUtils.isNotBlank(cpId)){
				String stageId = "";
				if(s.equals(e)){
					stageId = s;
				}else{
					stageId = s + "-" + e;
				}
				cpwayPlan.setStageId(stageId);
				cpwayPlan.setCpId(cpId);
				cpwayPlan.setVersionNo(versionId);
				clinicalPathwayService.addTimeToClinical(cpwayPlan);
				result = "success";
			}else{
				result = "网络异常。联系管理员";
			}
		}catch(Exception e){
			e.printStackTrace();
			result = "网络异常。联系管理员";
		}
		WebUtils.webSendString(result);
	}
	/**
	 * 查询阶段树
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月22日 下午3:21:42 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月22日 下午3:21:42 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="treeStage", results = { @Result(name = "json", type = "json") })
	public void treeStage(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cpId = request.getParameter("cpId");
		if(StringUtils.isNotBlank(cpId)){
			List<TreeJson> treeDepar = new ArrayList<TreeJson>();
			treeDepar = clinicalPathwayService.treeStage(cpId);
			String json = JSONUtils.toJson(treeDepar);
			System.out.println(json);
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 * 保存组套的临床路径模板
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月24日 上午9:37:45 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月24日 上午9:37:45 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="saveModelToPlan", results = { @Result(name = "json", type = "json") })
	public void saveModelToPlan(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		String modelCode = request.getParameter("modelCode");
		String modelNature = request.getParameter("modelNature");
		String cpId = request.getParameter("cpId");
		String stageId = request.getParameter("stageId");
		if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(modelCode)&&StringUtils.isNotBlank(modelNature)&&StringUtils.isNotBlank(stageId)){
			clinicalPathwayService.saveModelToPlan(id,modelCode,modelNature,cpId,stageId);
		}
	}
	
	/**
	 * 患者导入临床路径
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月22日 下午7:19:36 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月22日 下午7:19:36 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="savePatientClincal", results = { @Result(name = "json", type = "json") })
	public void savePatientClincal(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			Date currentTime = DateUtils.getCurrentTime();

			String versionId = request.getParameter("versionId");//临床路径版本
//			String stageId = request.getParameter("stageId");//阶段id
			String impPatientNo = request.getParameter("impPatientNo");//住院流水号
			String impdeptCode = request.getParameter("impdeptCode");//住院科室
			String cpId = request.getParameter("cpId");//临床路径id==临床路径版本的cpid
			
			String pageData = request.getParameter("data");
//			if(StringUtils.isNotBlank(versionId)&&StringUtils.isNotBlank(impPatientNo)&&StringUtils.isNotBlank(impdeptCode)&&StringUtils.isNotBlank(pageData)){
				JSONObject jsonData = new JSONObject(pageData);
				Iterator iterator = jsonData.keys();
				
				CpMasterIndex cmi = new CpMasterIndex();
				cmi.setInpatientNo(impPatientNo);
				cmi.setCpId(cpId);
				cmi.setVersionCode(versionId);
				cmi.setInpathFlag("1");//入径
				cmi.setDeptCode(impdeptCode);//入径科室
				cmi.setCreateUser(longinUserAccountCode);
				cmi.setCreateTime(currentTime);
				
				clinicalPathwayService.saveCmi(cmi);
				
				while(iterator.hasNext()){
					
//					String idDetails = "";//模板明细id(多个)
					String modelCode = "";//模板id(多个)
					String modelNature = "";//计划类别
					String id = "";//临床路径计划-----t_cpway_plan,应该是存这个，能看到准确的临床路径
					
					String modelNatureAndId = (String) iterator.next();//计划类别,和,临床路径计划
					modelNature = modelNatureAndId.split("\\|")[0];
					id = modelNatureAndId.split("\\|")[1];
			        String modelAndDetail = jsonData.get(modelNatureAndId).toString();//选中的临床路径模板和模板明细
			        JSONObject jsonModelAndDetail = new JSONObject(modelAndDetail);
			        Iterator iteratorM = jsonModelAndDetail.keys();
			        while(iteratorM.hasNext()){
			        	String modelId = iteratorM.next().toString();//临床路径模板id
			        	
			        	CpExecute ce = new CpExecute();
						ce.setInpatientNo(impPatientNo);
						ce.setModelCode(modelId);//可能有多个
						ce.setCpId(id);
						ce.setModelVersion(versionId);
//						ce.setStageId(stageId);
//						ce.setExecuteDate(currentTime);
//						ce.setExecuteUser(longinUserAccountCode);
						ce.setCreateUser(longinUserAccountCode);
						ce.setCreateTime(currentTime);
						ce.setPlanCode(modelNature);
						clinicalPathwayService.saveCe(ce);
			        	
//			        	if(StringUtils.isNotBlank(modelCode)){
//			        		modelCode += ","+modelId;
//			        	}else{
//			        		modelCode += modelId;
//			        	}
			        	String modelDetailId = jsonModelAndDetail.get(modelId).toString();//模板明细id
			        	JSONObject jsonModelDetailId = new JSONObject(modelDetailId);
				        Iterator iteratorD = jsonModelDetailId.keys();
				        while(iteratorD.hasNext()){
				        	String detailId = iteratorD.next().toString();//临床路径明细id
				        	
				        	String detailIdStatus = jsonModelDetailId.get(detailId).toString();//模板明细id的状态：true选中。false没选中
				        	if("true".equals(detailIdStatus)){
					        	CpExecuteDetail ced = new CpExecuteDetail();
								ced.setItemCode(detailId);
								ced.setInpatientNo(impPatientNo);
								ced.setCpId(id);
								ced.setModelCode(modelId);
								ced.setVersionCode(versionId);
								ced.setExecuteDeptCode(impdeptCode);
								ced.setCreateTime(currentTime);
								ced.setCreateUser(longinUserAccountCode);
								clinicalPathwayService.saveCed(ced);
//				        		if(StringUtils.isNotBlank(idDetails)){
//				        			idDetails += ","+detailId;
//				        		}else{
//				        			idDetails += detailId;
//				        		}
				        	}
				        }
			        }
			        
//					clinicalPathwayService.saveCed(ced);
				}
//			}else{
//				System.out.println("数据不全");
//			}
			
//			String modelCode = request.getParameter("modelCode");//模板id(多个)
//			String idDetails = request.getParameter("iddetails");//模板明细id(多个)
//			String modelNature = request.getParameter("modelNature");//计划类别
//			String id = request.getParameter("id");//临床路径计划-----t_cpway_plan,应该是存这个，能看到准确的临床路径
//			String cpId = request.getParameter("cpId");//临床路径id-----t_cpway
//			String versionId = request.getParameter("versionId");//临床路径版本
//			String stageId = request.getParameter("stageId");//阶段id
//			String impPatientNo = request.getParameter("impPatientNo");//住院流水号
//			String impdeptCode = request.getParameter("impdeptCode");//住院科室
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 患者临床路径树===患者临床路径
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月21日 下午5:39:15 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月21日 下午5:39:15 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="treePatientClinicalPath", results = { @Result(name = "json", type = "json") })
	public void treePatientClinicalPath(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String treeId = request.getParameter("id");//选中的科室
		List<TreeJson> treeDepar = new ArrayList<TreeJson>();
		treeDepar = clinicalPathwayService.queryPatientTree(treeId);
		String json = JSONUtils.toJson(treeDepar);
		System.out.println(json);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 患者以入径树====临床路径评估
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月26日 上午9:49:51 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月26日 上午9:49:51 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="treeClinicalStageAssess", results = { @Result(name = "json", type = "json") })
	public void treeClinicalStageAssess(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String treeId = request.getParameter("id");//选中的科室
		List<TreeJson> treeDepar = new ArrayList<TreeJson>();
		treeDepar = clinicalPathwayService.queryInPatientTree(treeId);
		String json = JSONUtils.toJson(treeDepar);
		System.out.println(json);
		WebUtils.webSendJSON(json);
	}	
	
	/**
	 * 根据住院流水号。查询患者执行信息
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月23日 下午2:31:09 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月23日 下午2:31:09 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="executeInfoByInpatientNo", results = { @Result(name = "json", type = "json") })
	public void executeInfoByInpatientNo(){
		List<CpExecute> cpExecuteList = new ArrayList<CpExecute>();
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			String inpatientNo = request.getParameter("id");
			if(StringUtils.isNotBlank(inpatientNo)){
				cpExecuteList = clinicalPathwayService.executeInfoByInpatientNo(inpatientNo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("rows", cpExecuteList);
		WebUtils.webSendString(JSONUtils.toJson(map));
	}
	
	/**
	 *  查询患者的执行信息明细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月23日 下午2:40:20 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月23日 下午2:40:20 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="executeDetail", results = { @Result(name = "json", type = "json") })
	public void executeDetail(){
		List<CpExecuteDetail> cpExecuteList = new ArrayList<CpExecuteDetail>();
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			String inpatientNo = request.getParameter("inpatientNo");
			String cpId = request.getParameter("cpId");
			String modelCode = request.getParameter("modelCode");
			if(StringUtils.isNotBlank(inpatientNo)){
				cpExecuteList = clinicalPathwayService.executeDetail(inpatientNo,cpId,modelCode);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("rows", cpExecuteList);
		WebUtils.webSendString(JSONUtils.toJson(map));
	}
	
	/**
	 * 患者退出路径
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月23日 下午4:29:44 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月23日 下午4:29:44 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="outPath", results = { @Result(name = "json", type = "json") })
	public void outPath(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String inpatientNo = request.getParameter("id");
		clinicalPathwayService.outPath(inpatientNo);
	}
	
	/**
	 * 执行临床路径
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月23日 下午4:40:42 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月23日 下午4:40:42 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="executePath", results = { @Result(name = "json", type = "json") })
	public void executePath(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String executeId = request.getParameter("id");
		List<String> list = Arrays.asList(executeId.split(","));
		for(String id : list){
			clinicalPathwayService.executePath(id);
		}
	}
	
	/**
	 * 添加或者修改评估
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月26日 下午4:15:50 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月26日 下午4:15:50 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="saveOrUpdateAssess", results = { @Result(name = "json", type = "json") })
	public void saveOrUpdateAssess(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		String inpatientNo = request.getParameter("inpatientNo");//住院流水号
//		String cpId = request.getParameter("cpId");//临床计划表id
		String stage = request.getParameter("stage");//阶段id
		String dayNums = request.getParameter("dayNums");//天数
		String assessResult = request.getParameter("assessResult");//评估结果
		InpAccess ia = new InpAccess();
		ia.setId(id);
		ia.setInpatientNo(inpatientNo);
		ia.setStageId(stage);
		ia.setDays(dayNums);
		ia.setAccessResult(assessResult);
		clinicalPathwayService.saveOrUpdateAssess(ia);
	}
	/**
	 * 评估审核
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月26日 下午7:21:49 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月26日 下午7:21:49 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="approveAssess", results = { @Result(name = "json", type = "json") })
	public void approveAssess(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		String accessCheckInfo = request.getParameter("accessCheckInfo");
		InpAccess ia = new InpAccess();
		ia.setId(id);
		ia.setAccessCheckInfo(accessCheckInfo);
		clinicalPathwayService.approveAssess(ia);
	}
	
	/**
	 * 展示评估列表
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年12月26日 下午5:14:06 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年12月26日 下午5:14:06 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="assessList", results = { @Result(name = "json", type = "json") })
	public void assessList(){
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		List<InpAccess>  list = new ArrayList<InpAccess>();
		int total = 10;
		try{
			String inpatientNo = request.getParameter("id");//住院流水号
			String stage = request.getParameter("stage");//阶段id
			String page = request.getParameter("page");
			String rows = request.getParameter("rows");
			if(StringUtils.isNotBlank(inpatientNo)&&StringUtils.isNotBlank(stage)){
				list = clinicalPathwayService.queryAssess(inpatientNo, stage, page, rows);
				total = clinicalPathwayService.queryAssessNum(inpatientNo, stage);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("rows", list);
		map.put("total", total);
		WebUtils.webSendString(JSONUtils.toJson(map));
	}
}
