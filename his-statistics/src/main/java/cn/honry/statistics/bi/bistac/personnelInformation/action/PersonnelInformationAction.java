package cn.honry.statistics.bi.bistac.personnelInformation.action;

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
import cn.honry.statistics.bi.bistac.personnelInformation.service.PersonnelInformationService;
import cn.honry.statistics.bi.bistac.personnelInformation.vo.PersonnelInformationVo;
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
@Namespace(value = "/statistics/personnelInformation")
public class PersonnelInformationAction extends ActionSupport{
	private Logger logger=Logger.getLogger(PersonnelInformationAction.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	@Qualifier(value = "personnelInformationService")
	private PersonnelInformationService personnelInformationService;
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	private String deptCode;//查询科室
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * 跳转到人事信息统计与分析页面
	 * @return
	 */
	@Action(value = "personnelInformationList", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/personnelInformation.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String personnelInformationList() {
		return "list";
	}
	
	/**  
	 * 
	 * 学历
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value="queryEducation")
	public void queryEducation(){
		List<PersonnelInformationVo> educations = null;
		try {
			educations = personnelInformationService.queryEducation(deptCode);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YYFX_RSXXTJYFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_RSXXTJYFX", "运营分析_人事信息统计与分析", "2", "0"), e);
		}
		String json = JSONUtils.toJson(educations);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 职称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value="queryTitle")
	public void queryTitle(){
		List<PersonnelInformationVo> titles = null;
		try {
			titles = personnelInformationService.queryTitle(deptCode);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YYFX_RSXXTJYFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_RSXXTJYFX", "运营分析_人事信息统计与分析", "2", "0"), e);
		}
		String json = JSONUtils.toJson(titles);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 性别
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value="querySex")
	public void querySex(){
		List<PersonnelInformationVo> sexs = null;
		try {
			sexs = personnelInformationService.querySex(deptCode);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YYFX_RSXXTJYFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_RSXXTJYFX", "运营分析_人事信息统计与分析", "2", "0"), e);
		}
		String json = JSONUtils.toJson(sexs);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 年龄
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月10日 上午11:21:39 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月10日 上午11:21:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Action(value="queryAge")
	public void queryAge(){
		List<PersonnelInformationVo> ages = null;
		try {
			ages = personnelInformationService.queryAge(deptCode);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("YYFX_RSXXTJYFX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YYFX_RSXXTJYFX", "运营分析_人事信息统计与分析", "2", "0"), e);
		}
		String json = JSONUtils.toJson(ages);
		WebUtils.webSendJSON(json);
	}
	
}
