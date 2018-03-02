package cn.honry.statistics.bi.bistac.temporary.action;

import java.util.HashMap;
import java.util.List;
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

import cn.honry.statistics.bi.bistac.temporary.service.HistoryRecordsService;
import cn.honry.statistics.bi.bistac.temporary.vo.HistoryRecordsInfoVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @Description： 历史病历
 * @Author：gaotiantian
 * @CreateDate：2017-4-1 下午5:41:12  
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-1 下午5:41:12 
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/advice")
public class HistoryRecordsAction extends ActionSupport {
	
	private String clinicNo;
	
	private HistoryRecordsService HistoryRecordsService;
	@Autowired
	@Qualifier(value = "HistoryRecordsService")
	public void setHistoryRecordsService(HistoryRecordsService HistoryRecordsService) {
		this.HistoryRecordsService = HistoryRecordsService;
	}
	/**
	 * getters and setters
	 */
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	
	@Action(value = "historyRecords", results = { @Result(name = "list", location = "/WEB-INF/pages/outpatient/advice/historyRecords.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showHistoryRecords(){
		return "list";
	}
	/**  
	 *  
	 * 根据门诊号或住院号查询病历信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-4-5 下午2:51:16  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-4-5 下午2:51:16   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryHistoryRecordsInfo")
	public void historyRecordsInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<HistoryRecordsInfoVo> rows = HistoryRecordsService.getHistoryRecordsInfo(clinicNo);
			map.put("resCode",rows);
			map.put("resMsg", "success");
		} catch (Exception e) {
			map.put("resCode","请求错误");
			map.put("resMsg", "error");
		}		
		String retJson = JSONUtils.toJson(map);
		WebUtils.webSendJSON(retJson);
	}
	
}

