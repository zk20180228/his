package cn.honry.inpatient.outpatientAdviceFind.action;

import java.util.ArrayList;
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

import cn.honry.inpatient.outpatientAdviceFind.service.LisListservice;
import cn.honry.inpatient.outpatientAdviceFind.vo.LisInspectionSample;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceAnalysisVO;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceDetailVo;
import cn.honry.inpatient.outpatientAdviceFind.vo.OutpatientAdviceVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: OutpatientAdviceActio
 * @Description: 门诊就医查询action
 * @author zl
 * @date 2015-12-3
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/lisAction")
@SuppressWarnings({"all"})
public class LisListAction extends ActionSupport {
	
	private LisListservice lisListservice;
	@Autowired 
	@Qualifier(value = "lisListservice")
	public void setLisListservice(LisListservice lisListservice) {
		this.lisListservice = lisListservice;
	}
	
	/**
	 * 起始页数
	 */
	private String page;
	
	
	/**
	 * 数据列数
	 */
	private String rows;
	
	/**
	 * info 中的id
	 */
	private String id;
	
	/**
	 * 查询字段
	 */
	private String queryName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	/**
	 * 门诊或住院类型
	 */
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**  
	 * @Description：  lis查询左侧数据
	 * @Author：tcj
	 * @CreateDate：2015-12-30 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "outpatientAdviceLisLis",results={@Result(name="json",type="json")})
	public void outpatientLisList(){
		List<LisInspectionSample>  list=lisListservice.findLisInfoPage(page,rows,queryName,type,id);
		int total = lisListservice.findLisInfoTotal(queryName,type);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		String json =JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	
	
	/**  
	 * @Description：  lis查询右侧Detail数据
	 * @Author：tcj
	 * @CreateDate：2015-12-30 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "outpatientLisDetailLis",results={@Result(name="json",type="json")})
	public void outpatientLisDetailList(){
		List<OutpatientAdviceDetailVo> list = new ArrayList<OutpatientAdviceDetailVo>();
		if(StringUtils.isNotBlank(id)){
			 list=lisListservice.findLisDetail(id);
		}
		String json =JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	

}
