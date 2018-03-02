package cn.honry.oa.repository.basedata.action;

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

import cn.honry.base.bean.model.RepositoryBaseData;
import cn.honry.oa.repository.basedata.service.ReposirotyBaseDataService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/reposirotyBaseData")
public class ReposirotyBaseDataAction {
	@Autowired
	@Qualifier("reposirotyBaseDataService")
	private ReposirotyBaseDataService reposirotyBaseDataService;
	private String dataid;
	private String name;
	private String page;
	private String rows;
	private String ids;
	private RepositoryBaseData data;
	/**  
	 * <p> </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月24日 上午11:21:37 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月24日 上午11:21:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * String
	 */
	@Action(value="toList",results={@Result(name="list",location="/WEB-INF/pages/oa/repository/baseData/repositoryBaseDataList.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toList(){
		return "list";
	}
	@Action(value="toADD",results={@Result(name="add",location="/WEB-INF/pages/oa/repository/baseData/repositoryBaseDataEdit.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toADD(){
		if(StringUtils.isBlank(dataid)){
			data = new RepositoryBaseData();
		}else{
			data = reposirotyBaseDataService.getByid(dataid);
		}
		return "add";
	}
	@Action(value="toViews",results={@Result(name="view",location="/WEB-INF/pages/oa/repository/baseData/repositoryBaseDataView.jsp")},interceptorRefs={@InterceptorRef("manageInterceptor")})
	public String toViews(){
		data = reposirotyBaseDataService.getByid(dataid);
		return "view";
	}
	@Action("delData")
	public void delData(){
		Map<String ,String > map = new HashMap<String, String>();
		try {
			reposirotyBaseDataService.delBaseData(ids);
			map.put("resCode", "success");
			map.put("resMsg", "删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resaCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("saveData")
	public void saveData(){
		Map<String,String > map = new HashMap<String, String>();
		try {
			map = reposirotyBaseDataService.saveBaseData(data);
		} catch (Exception e) {
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	@Action("getLists")
	public void getList(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<RepositoryBaseData> list = reposirotyBaseDataService.queryBaseData(name, page, rows);
			int total = reposirotyBaseDataService.queryBaseDataTotal(name);
			map.put("rows", list);
			map.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("resCode", "error");
			map.put("resMsg", "系统异常："+e.getLocalizedMessage());
		}
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
	
	
	
	
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
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
	public RepositoryBaseData getData() {
		return data;
	}
	public void setData(RepositoryBaseData data) {
		this.data = data;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
