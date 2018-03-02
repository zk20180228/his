package cn.honry.statistics.bi.bistac.temporary.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.User;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.bi.bistac.temporary.service.CheckListReportService;
import cn.honry.statistics.bi.bistac.temporary.vo.CheckListReportVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @className：CheckListReportAction
 * @Description：门诊就医-检验单
 * @Author：gaotiantian
 * @CreateDate：2017-4-10 下午2:09:12 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-10 下午2:09:12
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/iReport/iReportPrint")
public class CheckListReportAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -380398878583124665L;

	
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	private CheckListReportService CheckListReportService;
	@Autowired
	@Qualifier(value = "CheckListReportService")
	public void setCheckListReportService(CheckListReportService CheckListReportService) {
		this.CheckListReportService = CheckListReportService;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	@Action(value = "iReportToMenZhenJY")
	public void iReportToMenZhenJY() throws Exception {
		
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String CLINIC_CODE= request.getParameter("clinicCode");//jasper文件所用到的参数 PATIENT_NO
			
			//String DEPT_IDS = request.getParameter("DEPT_IDS");
			
			String MedicalrecordId = request.getParameter("MedicalrecordId");
			User user = (User) SessionUtils.getCurrentUserFromShiroSession();
			String APPLY_DOC = user.getName();//取当前用户
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			
			List<HashMap<String,Object>> o=new ArrayList<HashMap<String,Object>>();
			JRDataSource jrd = null;
			List<CheckListReportVo> CheckListReportList= null;
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath +fileName+".jasper";

			StringBuffer path=new StringBuffer();
			path.append(request.getSession().getServletContext().getRealPath("/"));
			path.append(webPath);

			List<CheckListReportVo> list=CheckListReportService.getCheckListReport(CLINIC_CODE, MedicalrecordId);				
			for(CheckListReportVo vo : list){
				HashMap<String,Object> parameterMap=new HashMap<String,Object>();
				CheckListReportList=new ArrayList<CheckListReportVo>();
				parameterMap.put("hospitalName", HisParameters.PREFIXFILENAME);
				parameterMap.put("SUBREPORT_DIR", path+"menzhenjianyanshenqingSub.jasper");
				parameterMap.put("CLINIC_CODE", CLINIC_CODE);
				//parameterMap.put("DEPT_ID", DEPT_IDS);
				parameterMap.put("APPLY_DOC", APPLY_DOC);
				parameterMap.put("Medicalrecord_Id", MedicalrecordId);
				CheckListReportList.add(vo);
				jrd = new JRBeanCollectionDataSource(CheckListReportList);
				parameterMap.put("dataSource", jrd);	
				o.add(parameterMap);
			}
							
			iReportService.doReportToJavaBean2(request,WebUtils.getResponse(),reportFilePath,o);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

