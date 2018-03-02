package cn.honry.outpatient.feedetail.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.outpatient.feedetail.service.FeedetailService;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**  
 *  
 * @className：FeedetailAction
 * @Description：  门诊划价Action 
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/outpatient")
@Namespace(value = "/outpatient/feedetail")
public class FeedetailAction extends ActionSupport implements ModelDriven<OutpatientFeedetail> {
	
	private static final long serialVersionUID = 1L;
	
	private OutpatientFeedetail feedetail = new OutpatientFeedetail();
	private FeedetailService feedetailService;
	
	@Override
	public OutpatientFeedetail getModel() {
		return feedetail;
	}
	
	@Autowired
	@Qualifier(value = "feedetailService")
	public void setFeedetailService(FeedetailService feedetailService){
		this.feedetailService = feedetailService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	
}
