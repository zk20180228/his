package cn.honry.outpatient.observation.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.business.Json;
import cn.honry.outpatient.observation.service.ObservationService;
import cn.honry.outpatient.observation.vo.ComboxVo;
import cn.honry.outpatient.observation.vo.ObservationVo;
import cn.honry.outpatient.observation.vo.PatientRegisterVo;
import cn.honry.outpatient.observation.vo.PatientVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * <p>门诊留观控制器层</p>
 * @Author: zhangkui
 * @CreateDate: 2017年11月16日 上午11:13:55 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年11月16日 上午11:13:55 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Namespace("/observation")
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
public class ObservationAction extends ActionSupport{

	private static final long serialVersionUID = -2030873958726041168L;
	
	private ObservationVo observation;
	public ObservationVo getObservation() {
		return observation;
	}
	public void setObservation(ObservationVo observation) {
		this.observation = observation;
	}
	

	@Resource
	private ObservationService observationService;
	public void setObservationService(ObservationService observationService) {
		this.observationService = observationService;
	}
	
	private String cardNo;//就诊卡号
	
	
	/**
	 * 
	 * <p>跳转到留观页面</p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午1:41:17 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午1:41:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *http://localhost:8080/his-portal/observation/toSaveObservationUI.action
	 */
	@Action(value="/toSaveObservationUI",results={@Result(name="list",location = "/WEB-INF/pages/register/observation/toSaveObservationUI.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String toSaveObservationUI(){
		
		return "list";
	}
	
	/**
	 * 
	 * <p>保存留观信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午1:42:08 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午1:42:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	@Action(value="/saveObservation")
	public void saveObservation(){
		
		String flag="false";
		if(observation!=null){
			try {
				int i=observationService.saveObservation(observation);//i大于0,说明保存成功
				if(i>0){
					flag="true";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String json = JSONUtils.toJson(flag);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 
	 * <p>根据就诊卡号查询胡患者基本信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午5:34:37 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午5:34:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action("/findPatientInfoByCardNo")
	public void findPatientInfoByCardNo(){
		
		List<PatientVo> p=null;
		PatientVo pa=null;
		if(StringUtils.isNotBlank(cardNo)){
			p=observationService.findPatientInfoByCardNo(cardNo);
			if(p!=null&&p.size()>0){
				pa=p.get(0);
			}
		}
		
		String json = JSONUtils.toJson(pa);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * <p>根据就诊卡号查询患者挂号信息 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月16日 下午5:36:20 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月16日 下午5:36:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action("/findRegisterInfoByCardNo")
	public void findRegisterInfoByCardNo(){
		List<PatientRegisterVo> p=null;
		
		if(StringUtils.isNotBlank(cardNo)){
			p=observationService.findRegisterInfoByCardNo(cardNo);
		}
		
		String json = JSONUtils.toJson(p);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 
	 * <p>责任医师拉框 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月17日 下午1:55:08 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月17日 下午1:55:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action("/findDoctorCode")
	public void findDoctorCode(){
		List<ComboxVo> list=observationService.findDoctorCode();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json); 
	}
	
	/**
	 * 
	 * <p>责任护士下拉框 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年11月17日 下午1:55:12 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年11月17日 下午1:55:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action("/findNurseCode")
	public void findNurseCode(){
		List<ComboxVo> list=observationService.findNurseCode();
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json); 
	}
	
	
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	
	
	
	
	
	
}
