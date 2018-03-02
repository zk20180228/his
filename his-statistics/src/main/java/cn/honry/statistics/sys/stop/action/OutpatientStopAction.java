package cn.honry.statistics.sys.stop.action;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
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
import cn.honry.statistics.sys.stop.service.OutpatientStopService;
import cn.honry.statistics.sys.stop.vo.OutPatientToReport;
import cn.honry.statistics.sys.stop.vo.OutPatientVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 门诊停诊原因统计表
 * @author  lyy
 * @createDate： 2016年6月23日 上午10:09:59 
 * @modifier lyy
 * @modifyDate：2016年6月23日 上午10:09:59
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value="manageInterceptor")})
@Namespace(value="/statistics/ReservationStatistics")
@SuppressWarnings({"all"})
public class OutpatientStopAction extends ActionSupport {

	//hedong 用于报表打印 使用java分隔符 否则 linux系统会找不到相应路径
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	@Autowired
	@Qualifier(value="iReportService")
	private IReportService iReportService;
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(OutpatientStopAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	private OutpatientStopService outpatientStopService;
	@Autowired
	@Qualifier(value="outpatientStopService")
	public void setOutpatientStopService(OutpatientStopService outpatientStopService) {
		this.outpatientStopService = outpatientStopService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/**
	 * 开始时间
	 */
	private String firstData;
	
	/**
	 * 结束时间
	 */
	private String  endData;
	
	/**
	 * 默认当前时间
	 */
	private String dataTime;
	
	
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public String getFirstData() {
		return firstData;
	}
	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	
	private static final long serialVersionUID = 1L;
	
	@Action(value="listOutpatientStop",results={@Result(name="list",location="/WEB-INF/pages/stat/outpatient/outpatientStopList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public String listOutpatientStop(){
		
		//获取时间
		dataTime = DateUtils.getDate();
		firstData=new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addMonth(DateUtils.getCurrentTime(),-1));
		
		return "list";
	}
	
	/**
	 * 查询门诊停诊原因所有人数
	 * @author  lyy
	 * @createDate： 2016年6月23日 上午10:09:53 
	 * @modifier lyy
	 * @modifyDate：2016年6月23日 上午10:09:53
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryOutpatientStop")
	public void queryOutpatientStop(){
		List<OutPatientVo> list=null;
		try {
			list=outpatientStopService.getPageOutpatientStop(firstData,endData);
			Integer Ssick=0; //生病总数
			Integer Sevection=0; //出差总数
			Integer Smeet=0; //开会总数
			Integer Sother=0; //其他总数
			Integer Ssum=0; //总数
			for (OutPatientVo outVo : list) {
				if(list!=null&&list.size()>0){
					Ssick=Ssick + outVo.getSumSick();
					Sevection=Sevection+outVo.getSumEvection();
					Smeet=Smeet+outVo.getSumMeet();
					Sother=Sother+outVo.getSumOther();
					Ssum=Ssum+outVo.getSum();
				}
			}
			OutPatientVo vo=new OutPatientVo();
			vo.setDeptName("全院合计");
			vo.setSumSick(Ssick);
			vo.setSumEvection(Sevection);
			vo.setSumMeet(Smeet);
			vo.setSumOther(Sother);
			vo.setSum(Ssum);
			list.add(vo);
			String json=JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			//发生异常后返回空的内容
			list= new ArrayList<OutPatientVo>();
			Integer Ssick=0; //生病总数
			Integer Sevection=0; //出差总数
			Integer Smeet=0; //开会总数
			Integer Sother=0; //其他总数
			Integer Ssum=0; //总数
			OutPatientVo vo=new OutPatientVo();
			vo.setDeptName("全院合计");
			vo.setSumSick(Ssick);
			vo.setSumEvection(Sevection);
			vo.setSumMeet(Smeet);
			vo.setSumOther(Sother);
			vo.setSum(Ssum);
			list.add(vo);
			String json=JSONUtils.toJson(list);
			
			WebUtils.webSendJSON(json);
			
		    e.printStackTrace();
		    logger.error("TJFXGL_MZTZTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZTZTJ", "门诊统计分析_门诊停诊统计", "2", "0"), e);
		}
	}
	
	/**
	 * 导出方法
	 * @author  lyy
	 * @createDate： 2016年6月24日 下午5:18:29 
	 * @modifier lyy
	 * @modifyDate：2016年6月24日 下午5:18:29
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 */
	@Action(value="expOutpatientStop")
	public void expOutpatientStop() throws IOException{
		
		try {
			List<OutPatientVo> outPatientList=outpatientStopService.getPageOutpatientStop(firstData,endData);
			Integer Ssick=0; //生病总数
			Integer Sevection=0; //出差总数
			Integer Smeet=0; //开会总数
			Integer Sother=0; //其他总数
			Integer Ssum=0; //总数
			for (OutPatientVo outVo : outPatientList) {
				if(outPatientList!=null&&outPatientList.size()>0){
					Ssick=Ssick + outVo.getSumSick();
					Sevection=Sevection+outVo.getSumEvection();
					Smeet=Smeet+outVo.getSumMeet();
					Sother=Sother+outVo.getSumOther();
					Ssum=Ssum+outVo.getSum();
				}
			}
			OutPatientVo vo=new OutPatientVo();
			vo.setDeptName("全院合计");
			vo.setSumSick(Ssick);
			vo.setSumEvection(Sevection);
			vo.setSumMeet(Smeet);
			vo.setSumOther(Sother);
			vo.setSum(Ssum);
			outPatientList.add(outPatientList.size(), vo);
			if(outPatientList==null||outPatientList.isEmpty()){
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录!");
			}
			String head ="";
			String name="门诊停诊统计";
			String[] headMessage={"出诊科室","生病","出差","开会","其他","停诊人次合计"};
			for (String manage : headMessage) {
				head+=","+manage;
			}
			head=head.substring(1);
			FileUtil fileUtil=new FileUtil();
			String fileName=name+DateUtils.formatDateY_M_D_H_M(new Date())+".csv";
			String filePath=ServletActionContext.getServletContext().getRealPath("/WEB-INF")+fileName;
			fileUtil.setFilePath(filePath);
			fileUtil.write(head);
			PrintWriter out=WebUtils.getResponse().getWriter();
			try {
				fileUtil=outpatientStopService.export(outPatientList,fileUtil);
				fileUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME+fileName);
				out.write("success");
			} catch (Exception e) {
				out.write("error");
				 e.printStackTrace();
				 logger.error("TJFXGL_MZTZTJ", e);
				 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZTZTJ", "门诊统计分析_门诊停诊统计", "2", "0"), e);
			}finally{
				out.flush();
				out.close();
			}
			
		} catch (Exception e) {
		
			 e.printStackTrace();
			 logger.error("TJFXGL_MZTZTJ", e);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZTZTJ", "门诊统计分析_门诊停诊统计", "2", "0"), e);
		}
		
	}

	/***
	 * @Description:科室门诊停诊原因报表打印
	 */
	@Action(value="iReportOutpatientStop")
	public void iReportOutpatientStop(){
		 
		try{
			
			   //jasper文件名称 不含后缀
			   String rows=request.getParameter("rows");
			   String fileName = request.getParameter("fileName");
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<OutPatientVo> list = JSONUtils.fromJson(rows, new TypeToken<List<OutPatientVo>>(){});

			   //javaBean数据封装
			   ArrayList<OutPatientToReport> voList = new ArrayList<OutPatientToReport>();
			   OutPatientToReport vo = new OutPatientToReport();
			   vo.setItemList(list);
			   voList.add(vo);
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("hName", HisParameters.PREFIXFILENAME);
			   parameters.put("firstData", firstData);
			   parameters.put("endData", endData);
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  
		}catch(Exception e){
			  
			 e.printStackTrace();
			 logger.error("TJFXGL_MZTZTJ", e);
			 hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZTZTJ", "门诊统计分析_门诊停诊统计", "2", "0"), e);
		 
		}
		 
	}
}
