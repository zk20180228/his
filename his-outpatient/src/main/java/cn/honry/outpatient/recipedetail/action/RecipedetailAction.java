package cn.honry.outpatient.recipedetail.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.outpatient.recipedetail.service.RecipedetailService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**  
 *  
 * @className：RecipedetailAction 
 * @Description：  门诊处方Action
 * @Author：aizhonghua
 * @CreateDate：2015-7-6 下午04:47:44  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-7-6 下午04:47:44  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/recipedetail")
public class RecipedetailAction extends ActionSupport implements ModelDriven<OutpatientRecipedetail> {
	
	private static final long serialVersionUID = 1L;
	// 记录异常日志
	private Logger logger = Logger.getLogger(RecipedetailAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Override
	public OutpatientRecipedetail getModel() {
		return recipedetail;
	}
	
	@Autowired
	@Qualifier(value = "recipedetailService")
	private RecipedetailService recipedetailService;
	public void setRecipedetailService(RecipedetailService recipedetailService) {
		this.recipedetailService = recipedetailService;
	}
	
	private OutpatientRecipedetail recipedetail = new OutpatientRecipedetail();
	private HttpServletRequest request = ServletActionContext.getRequest();
	private ArrayList<OutpatientRecipedetail> children= new ArrayList<OutpatientRecipedetail>(); 
	
	public ArrayList<OutpatientRecipedetail> getChildren() {
		return children;
	}


	public void setChildren(ArrayList<OutpatientRecipedetail> children) {
		this.children = children;
	}
	
	@Action(value = "listRecipedetail", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/medicalrecord/recipedetail/recipedetailList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRecipedetail(){
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  分类组套树
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 上午10:42:41  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 上午10:42:41  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "sortStackTree", results = { @Result(name = "json", type = "json") })
	public void sortStackTree() {
		try {
			List<TreeJson> treeJsonList = recipedetailService.getSortStackTree();
			List<TreeJson> treeJson =  TreeJson.formatTree(treeJsonList);
			String json = JSONUtils.toJson(treeJson);
	
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
		    e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description： 查询历史医嘱
	 * @param 
	 * @Author：liuhanliang
	 * @CreateDate：2015-7-17 上午10:16:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */	
	@RequiresPermissions(value={"MZBL:function:view"})
	@Action(value = "queryhisRecipedetail", results = { @Result(name = "json", type = "json") })
	public void queryhisRecipedetail() {
		try{
			String patientNo = ServletActionContext.getRequest().getParameter("patientNo");
			List<OutpatientRecipedetail> list = recipedetailService.query(patientNo);//历史医嘱的数据
			List ists = recipedetailService.queryDate();
			String ids="";
			for (int j = 0; j < ists.size(); j++) {
			for (int i = 0; i < list.size(); i++) {
				//递归添加节点
				String s=list.get(i).getOperDate().toString().replaceAll("-","/").substring(0,10);
				if(ists.get(j).equals(s)) { 
					if (ids=="") {
					ids=list.get(i).getId();
				}
					list.get(i).set_parentId(ids);
					children.add(list.get(i));
					}
				}
				ids="";
			}
			int total = recipedetailService.getTotal(recipedetail);
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).getId()==children.get(i).get_parentId()) {
					children.get(i).set_parentId("");
				}
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", children);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendString(json);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：   添加&修改
	 * @Description：   添加
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-13 下午05:13:26  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-13 下午05:13:26  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveRecipedetail", results = { @Result(name = "list", location = "/WEB-INF/pages/register/scheduleModel/scheduleModelList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String saveRecipedetail() {
		try{
			String recNonHerMedJson = request.getParameter("recNonHerMedJson");
			String recHerMedJson = request.getParameter("recHerMedJson");
			
			recipedetailService.saveRecipedetail(recipedetail,recNonHerMedJson,recHerMedJson);
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			e.printStackTrace();
			logger.error("GHPBGL_GHPBMBGL", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHPBGL_GHPBMBGL", "挂号排班管理_挂号排班模板管理", "2", "0"), e);
		}
		return "list";
	}
	
}
