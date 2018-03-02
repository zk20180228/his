package cn.honry.oa.repository.categ.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.RepositoryCateg;
import cn.honry.oa.repository.categ.service.RepositoryCategService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/repositoryCateg")
public class RepositoryCategAction {
	@Autowired
	@Qualifier("repositoryCategService")
	private RepositoryCategService repositoryCategService;
	private RepositoryCateg cate;
	
	private String deptCode;
	private String name;
	private String page;
	private String rows;
	private String cateid;
	private String nodeType;
	
	@Action(value="toCategList",results={@Result(name="list",location="/WEB-INF/pages/oa/repository/categ/repositoryCategList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toCategList(){
		return "list";
	}
	@Action(value="toCategView",results={@Result(name="view",location="/WEB-INF/pages/oa/repository/categ/repositoryCateView.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toCategView(){
		cate = repositoryCategService.get(cateid);
		return "view";
	}
	@Action(value="toCategEdit",results={@Result(name="edit",location="/WEB-INF/pages/oa/repository/categ/repositoryCateEdit.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toCategEdit(){
		cate = repositoryCategService.get(cateid);
		return "edit";
	}
	@Action(value="toCategADD",results={@Result(name="add",location="/WEB-INF/pages/oa/repository/categ/repositoryCateEdit.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toCategADD(){
		cate = new RepositoryCateg();
		cate.setDiseaseCode(deptCode);
		return "add";
	}
	@Action("getCateg")
	public void getCateg(){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			List<RepositoryCateg> list = repositoryCategService.getCateg(deptCode, page, rows, name,nodeType);
			int total = repositoryCategService.getCategTotal(deptCode, name,nodeType);
			map.put("rows", list);
			map.put("total", total);
		}catch(Exception e){
			e.printStackTrace();
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	/**  
	 * <p>获取知识库分类树 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月14日 下午5:17:36 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月14日 下午5:17:36 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	@Action("getCategTree")
	public void getCategTree(){
		List<TreeJson> categTree = repositoryCategService.getCategTree();
		System.err.println(JSONUtils.toJson(categTree));
		WebUtils.webSendJSON(JSONUtils.toJson(categTree));
	}
	@Action("saveCateg")
	public void saveCateg(){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			if(StringUtils.isBlank(cate.getId())){
				Map<String, String> checkCode = repositoryCategService.checkCode(cate.getCode());
				if("success".equals(checkCode.get("resCode"))){
					repositoryCategService.saveCateg(cate);
					map.put("resCode", "success");
					map.put("resMsg", "保存成功!");
				}else{
					map.put("resCode", "error");
					map.put("resMsg", "编码重复!");
				}
			}else{
				repositoryCategService.updateCateg(cate);
				map.put("resCode", "success");
				map.put("resMsg", "修改成功!");
			}
		}catch(Exception e){
			e.printStackTrace();
			String localizedMessage = e.getLocalizedMessage();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常:"+localizedMessage);
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("updateCateg")
	public void updateCateg(){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			repositoryCategService.updateCateg(cate);
			map.put("resCode", "success");
			map.put("resMsg", "修改成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "修改失败!");
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("delCate")
	public void delCate(){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			String[] split = cateid.split(",");
			for (String id : split) {
				repositoryCategService.delCate(id);
			}
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		}catch(Exception e){
			map.put("resCode", "error");
			map.put("resMsg", "删除失败!");
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("checkCode")
	public void checkCode(){
		Map<String, String> map = repositoryCategService.checkCode(cate.getCode());
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public RepositoryCateg getCate() {
		return cate;
	}
	public void setCate(RepositoryCateg cate) {
		this.cate = cate;
	}
	public String getCateid() {
		return cateid;
	}
	public void setCateid(String cateid) {
		this.cateid = cateid;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
}
