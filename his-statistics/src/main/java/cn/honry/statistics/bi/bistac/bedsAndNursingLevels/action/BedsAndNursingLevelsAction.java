package cn.honry.statistics.bi.bistac.bedsAndNursingLevels.action;

import java.util.List;

import org.apache.log4j.Logger;
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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.service.BedsAndNursingLevelsService;
import cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo.BedsAndNursingLevelsVo;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.action.ListTotalIncomeStaticAction;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
/**
 * 
* @ClassName: 床位及护理级别情况统计
* @author yuke
* @date 2017年7月3日
*
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/bedsAndNursingLevels")
public class BedsAndNursingLevelsAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "bedsAndNursingLevelsService")
	private BedsAndNursingLevelsService bedsAndNursingLevelsService;
	
	private Logger logger=Logger.getLogger(BedsAndNursingLevelsAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	
	/**
	 * 跳转到床位使用情况，护理级别情况统计页面
	 * @return
	 */
	@Action(value = "bedsAndNursingLevelslist", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/bedsAndNursingLevels.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String bedsAndNursingLevelslist() {
		return "list";
	}
	
	/**  
	 * 
	 * 床位使用情况统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryBeds")
	public void queryBeds(){
		List<BedsAndNursingLevelsVo> listBeds = null;
		try {
			listBeds = bedsAndNursingLevelsService.queryBeds();
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_CWJHLJBQKTJ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_CWJHLJBQKTJ", "运营分析_床位及护理级别情况统计", "2", "0"), e);
		}
		String json = JSONUtils.toJson(listBeds);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 护理级别统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年5月23日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年5月23日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryNursingLevels")
	public void queryNursingLevels(){
		List<BedsAndNursingLevelsVo> listNursingLevels = null;
		try {
			listNursingLevels = bedsAndNursingLevelsService.queryNursingLevels();
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_CWJHLJBQKTJ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_CWJHLJBQKTJ", "运营分析_床位及护理级别情况统计", "2", "0"), e);
		}
		String json = JSONUtils.toJson(listNursingLevels);
		WebUtils.webSendJSON(json);
	}
	
}
