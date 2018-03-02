package cn.honry.statistics.finance.inpatientUDbalance.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.inpatientUDbalance.service.InpatientUDBService;
import cn.honry.statistics.finance.inpatientUDbalance.vo.InpatientUDBToReport;
import cn.honry.statistics.finance.inpatientUDbalance.vo.InpatientUDBVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @className：
 * @Description： 住院收费员缴款单Action  
 * @Author：tuchunajiang
 * @CreateDate：2016-6-24   
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/InpatientUDB")
@SuppressWarnings({"all"})
public class InpatientUDBAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(InpatientUDBAction.class);
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	@Autowired
	@Qualifier(value = "inpatientUDBService")
	private InpatientUDBService inpatientUDBService;
	
	public void setInpatientUDBService(InpatientUDBService inpatientUDBService) {
		this.inpatientUDBService = inpatientUDBService;
	}

	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	private String date;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@RequiresPermissions(value={"MZSKYJKD:function:view"}) 
	@Action(value = "listudbalance", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/outpatientUDbalance/outpatientUDbalance.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listudbalance() {
		
		return "list";
	}
	
	@Action(value = "querylist")
	public void querylist(){
		
		List<InpatientUDBVo> idbvol=null;
		try {
			idbvol=inpatientUDBService.queryDateInfo(date);
		} catch (Exception e) {
			
			idbvol=new ArrayList<InpatientUDBVo>();
			
			e.printStackTrace();
			logger.error("MZCX_MZSKYJKD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSKYJKD", "门诊查询_门诊收款员缴款单", "2","0"), e);
		}
		
		String json=JSONUtils.toJson(idbvol,DateUtils.DATE_FORMAT);
		WebUtils.webSendJSON(json);
	}
	

	/**
	 * 
	 * <p> 门诊收款员缴款单打印</p>
	 * @Author: 
	 * @CreateDate: 2017年7月4日 下午5:32:02 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午5:32:02 
	 * @ModifyRmk: 添加注释模板，异常处理 
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value="iReportInpatientUDB")
	public void iReportInpatientUDB(){
		 
		try{
			   //jasper文件名称 不含后缀
			   String rows = request.getParameter("rows"); 
			   String fileName = request.getParameter("fileName");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   Gson gson = new Gson();
			   List<InpatientUDBVo> list = gson.fromJson(rows, new TypeToken<List<InpatientUDBVo>>(){}.getType());
			  
			   //javaBean数据封装
			   ArrayList<InpatientUDBToReport> voList = new ArrayList<InpatientUDBToReport>();
			   InpatientUDBToReport vo = new InpatientUDBToReport();
			   vo.setList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("hName", HisParameters.PREFIXFILENAME);
			   parameters.put("date", date);
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		  }catch(Exception e){
		    
				e.printStackTrace();
				logger.error("MZCX_MZSKYJKD", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSKYJKD", "门诊查询_门诊收款员缴款单", "2","0"), e);
		  }
		 
	}
	

	/**
	 * 
	 * <p>门诊收款员缴款单导出 </p>
	 * @Author: 
	 * @CreateDate: 2017年7月4日 下午5:33:48 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午5:33:48 
	 * @ModifyRmk:  修改注释模板，添加异常处理
	 * @version: V1.0
	 * @throws IOException
	 * @throws:
	 *
	 */
	@Action(value="outpatientUDInfo")
	public void outpatientUDInfo(){
		
		try {
			List<InpatientUDBVo> idbvol=inpatientUDBService.queryDateInfo(date);
			if (idbvol == null || idbvol.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			
			String head = "";
			String name = "门诊收款员缴款单";
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
			String filename = new String(fileName.getBytes(),"ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+filename);
			ServletOutputStream stream = response.getOutputStream();
			
			inpatientUDBService.exportExcel(stream,idbvol);
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("MZCX_MZSKYJKD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSKYJKD", "门诊查询_门诊收款员缴款单", "2","0"), e);
		}

	}
}
