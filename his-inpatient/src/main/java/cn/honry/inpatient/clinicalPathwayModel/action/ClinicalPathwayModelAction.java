package cn.honry.inpatient.clinicalPathwayModel.action;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.inner.inpatient.kind.service.InpatientKindInInterService;
import cn.honry.inpatient.clinicalPathwayModel.service.ClinicalPathwayModelService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;



@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/clinicalPathwayModelAction")
public class ClinicalPathwayModelAction extends ActionSupport {

	/**  
	 * 临床路径模板
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 下午8:29:08 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 下午8:29:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "clinicalPathwayModelService")
	private ClinicalPathwayModelService clinicalPathwayModelService;
	
	public void setClinicalPathwayModelService(
			ClinicalPathwayModelService clinicalPathwayModelService) {
		this.clinicalPathwayModelService = clinicalPathwayModelService;
	}
	@Autowired
	@Qualifier(value = "inpatientKindInInterService")
	private InpatientKindInInterService inpatientKindInInterService;
	
	public void setInpatientKindInInterService(
			InpatientKindInInterService inpatientKindInInterService) {
		this.inpatientKindInInterService = inpatientKindInInterService;
	}
	private ModelDict modelDict = new ModelDict();

	public ModelDict getModelDict() {
		return modelDict;
	}

	public void setModelDict(ModelDict modelDict) {
		this.modelDict = modelDict;
	}

	private ModelVsItem modelVsItem = new ModelVsItem();
	
	public ModelVsItem getModelVsItem() {
		return modelVsItem;
	}

	public void setModelVsItem(ModelVsItem modelVsItem) {
		this.modelVsItem = modelVsItem;
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
	 * 展示临床路径模板页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 下午8:52:09 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 下午8:52:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="queryList",results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/clinicalPathwayModel/clinicalPathwayModelList.jsp") })
	public String queryList(){
		return "list";
	}
	
	/**
	 * 添加模板页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 上午10:07:09 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 上午10:07:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="addPathwayModel",results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/clinicalPathwayModel/clinicalPathwayModelEdit.jsp") })
	public String addPathwayModel(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String modelClass = request.getParameter("type");
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){//修改，回显
			modelDict = clinicalPathwayModelService.findPathwayModelById(id);
		}else{
			modelDict.setModelClass(modelClass);
		}
		return "add";
	}
	
	/**
	 * 展示模板页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 上午10:07:25 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 上午10:07:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="showPathwayModel",results = { @Result(name = "show", location = "/WEB-INF/pages/inpatient/clinicalPathwayModel/viewclinicalPathwayModel.jsp") })
	public String showPathwayModel(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){//修改，回显
			modelDict = clinicalPathwayModelService.findPathwayModelById(id);
		}
		return "show";
	}
	/**
	 * 添加明细页面
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 上午10:07:37 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 上午10:07:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String
	 *
	 */
	@Action(value="addPathwayDetail",results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/clinicalPathwayModel/pathwayDetail.jsp") })
	public String addPathwayDetail(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			modelVsItem = clinicalPathwayModelService.findPathwayModelDetailById(id);
		}else{
			String modelId = request.getParameter("modelId");
			String modelClass = request.getParameter("modelClass");
			modelVsItem.setModelId(modelId);
			modelVsItem.setModelClass(modelClass);
		}
		return "add";
	}
	
	
	@Action(value="showPathwayDetail",results = { @Result(name = "show", location = "/WEB-INF/pages/inpatient/clinicalPathwayModel/showPathwayDetail.jsp") })
	public String showPathwayDetail(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		modelVsItem = clinicalPathwayModelService.findPathwayModelDetailById(id);
		return "show";
	}
	
	
	
	
	/**
	 * 查询模板树
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月16日 上午10:52:52 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月16日 上午10:52:52 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="treeClinicalPath", results = { @Result(name = "json", type = "json") })
	public void treeClinicalPath(){
		List<TreeJson> treeDepar = new ArrayList<TreeJson>();
		treeDepar = clinicalPathwayModelService.queryTree();
		String json = JSONUtils.toJson(treeDepar);
		System.out.println(json);
		WebUtils.webSendJSON(json);
	}
	

	/**
	 * 添加或修改模板
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月16日 下午8:48:36 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月16日 下午8:48:36 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="saveOrUpdate", results = { @Result(name = "json", type = "json") })
	public void saveOrUpdate(){
		String result = "true";
		try{
			clinicalPathwayModelService.saveOrUpdate(modelDict);
		}catch(Exception e){
			e.printStackTrace();
			result = "false";
		}
		WebUtils.webSendString(result);
	}
	
	/**
	 * 保存或修改明细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 下午4:17:56 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 下午4:17:56 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="saveOrUpdatePathwayDetail", results = { @Result(name = "json", type = "json") })
	public void saveOrUpdatePathwayDetail(){
		String result = "true";
		try{
			clinicalPathwayModelService.saveOrUpdatePathwayDetail(modelVsItem);
		}catch(Exception e){
			e.printStackTrace();
			result = "false";
		}
		WebUtils.webSendString(result);
	}
	/**
	 * 医嘱类型下拉
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 下午1:52:01 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 下午1:52:01 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="flagCombobox", results = {@Result(name = "json", type = "json") })
	public void flagCombobox(){
		List<InpatientKind> kindInfo = inpatientKindInInterService.queryKindInfo();
		WebUtils.webSendString(JSONUtils.toJson(kindInfo));
	}
	
	/**
	 * 查询模板下的明细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 下午5:22:46 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 下午5:22:46 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryClinicalPathModelDetail", results = {@Result(name = "json", type = "json") })
	public void queryClinicalPathModelDetail(){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String modelId = request.getParameter("modelId");
			if(StringUtils.isNotBlank(modelId)){
				Integer total = clinicalPathwayModelService.queryClinicalPathModelDetailNum(modelId);
				List<ModelVsItem> list = clinicalPathwayModelService.queryClinicalPathModelDetail(modelId,page,rows);
				map.put("total", total);
				map.put("rows", list);
			}else{
				map.put("total", 0);
				map.put("rows", new ArrayList<ModelVsItem>());
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("total", 0);
			map.put("rows", new ArrayList<ModelVsItem>());
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 批量删除明细
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月17日 下午8:39:28 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月17日 下午8:39:28 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="delPathwayDetail")
	public void delPathwayDetail(){
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			if(StringUtils.isNotBlank(id)){
				id = "'"+id.replaceAll(",", "','")+"'";
				clinicalPathwayModelService.delPathwayDetail(id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 临床路径模板列表下拉
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月22日 下午7:24:54 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月22日 下午7:24:54 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="searchClinicalModelByNature", results = { @Result(name = "json", type = "json") })
	public void searchClinicalModelByNature(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String modelNature = request.getParameter("modelNature");
		List<ModelDict> list = clinicalPathwayModelService.searchClinicalModelByNature(modelNature);
		WebUtils.webSendString(JSONUtils.toJson(list));
	}
	/**
	 * 查询组套已勾选的临床路径模板
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月24日 下午2:47:22 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月24日 下午2:47:22 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="searchClinicalModelByStage", results = { @Result(name = "json", type = "json") })
	public void searchClinicalModelByStage(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String modelNature = request.getParameter("modelNature");
		String planId = request.getParameter("planId");
		String stageId = request.getParameter("stageId");
		List<CpwayPlan> list = clinicalPathwayModelService.searchClinicalModelByStage(planId,modelNature,stageId);
		WebUtils.webSendString(JSONUtils.toJson(list));
	}
	/**
	 * 查询所有临床路径模板（用于渲染）
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月24日 下午4:26:18 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月24日 下午4:26:18 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="searchAllClinicalModel", results = { @Result(name = "json", type = "json") })
	public void searchAllClinicalModel(){
		List<ModelDict> list = clinicalPathwayModelService.searchAllClinicalModel();
		WebUtils.webSendJSON(JSONUtils.toJson(list));
	}
}
