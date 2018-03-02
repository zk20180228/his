package cn.honry.statistics.deptstat.out_inpatient_work.action;

import java.io.File;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.out_inpatient_work.service.OutInpatientWorkService;
import cn.honry.statistics.deptstat.out_inpatient_work.vo.OutInpatientWorkReport;
import cn.honry.statistics.deptstat.out_inpatient_work.vo.OutInpatientWorkVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>门诊住院工作(某)月份同期对比表 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年5月31日 下午7:46:13 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月5日 下午5:44:13 
 * @ModifyRmk:  添加注释模板,添加异常处理机制
 * @version: V1.0
 * @throws:
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/iReport/OutInpatientWork")
public class OutInpatientWorkAction extends ActionSupport {
		
		private static final long serialVersionUID = 99196252414497985L;
		
		private String bTime;//开始时间
		private String eTime;//结束时间
		private String menuAlias;//栏目别名
		
		private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
		
		@Resource
		private OutInpatientWorkService outInpatientWorkService;
		
		@Resource
		private IReportService iReportService;
		
		private HttpServletRequest request = ServletActionContext.getRequest();
		private HttpServletResponse response = ServletActionContext.getResponse();
		
		private Logger logger=Logger.getLogger(OutInpatientWorkAction.class);
	
		@Resource
		private HIASExceptionService hiasExceptionService;
		public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
			this.hiasExceptionService = hiasExceptionService;
		}


		//门诊住院工作同期对比表http://localhost:8080/his-portal/iReport/OutInpatientWork/outInpatientWorkList.action
		//对比某个月
		@Action(value = "outInpatientWorkList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/outInpatientWork/outInpatientWorkList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String outInpatientWorkList(){
			
			Date date = new Date();
			eTime = DateUtils.formatDateY_M(date);
			ServletActionContext.getRequest().setAttribute("eTime", eTime);
			
			
			return "list";
		}
	
		
		//门诊住院工作同期对比表http://localhost:8080/his-portal/iReport/OutInpatientWork/outInpatientWorkListBWT.action
		//对比某年，指定月区间：
		@Action(value = "outInpatientWorkListBWT", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/outInpatientWork/outInpatientWorkListBWT.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String outInpatientWorkListBWT(){
			
			Date date = new Date();
			eTime = DateUtils.formatDateY_M(date);
			ServletActionContext.getRequest().setAttribute("eTime", eTime);
			ServletActionContext.getRequest().setAttribute("bTime", eTime.substring(0,4)+"-01");

			
			return "list";
		}

		
		//加载datagrid列表,先从mongo中读，没有数据才会去oracle去读
		@Action(value="loadDataByOracle",results={@Result(name="json",type="json")})
		public void  loadDataByOracle(){
			
			List<OutInpatientWorkVo> outInpatientWorkVoList=null;
			
			try {
				String[] areaCode = request.getParameterValues("areaCode[]");//这里我也真不知道什么鬼，传递的参数变成了带'[]'的，这个参数可以从前台的请求头中看到
				String areaCodeStr="";
				if(areaCode!=null){
					for(String s:areaCode){
						if(!"0".equals(s)){
							areaCodeStr+=s+",";
						}
						
					}
				}
				outInpatientWorkVoList=outInpatientWorkService.outInpatientWorkListByMongo(bTime, eTime, areaCodeStr);
				if(outInpatientWorkVoList==null||outInpatientWorkVoList.size()<=0){//没有数据走oracle
					 outInpatientWorkVoList=outInpatientWorkService.outInpatientWorkList(bTime, eTime, areaCodeStr);
				}
			} catch (Exception e) {
					
					outInpatientWorkVoList=new ArrayList<OutInpatientWorkVo>();
					e.printStackTrace();
					
					//记录日志
					logger.error("KSTJ_MZZYGZTQDBB", e);
					
					//将日志信息存入mongodb中
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_MZZYGZTQDBB", "科室统计_门诊住院工作同期对比表", "2", "0"), e);
			}
			
			String json = JSONUtils.toJson(outInpatientWorkVoList);
			
			WebUtils.webSendJSON(json);
			
		}
		
		/**
		 * 
		 * <p> 门诊住院工作(某)月份同期对比表---报表打印</p>
		 * @Author: zhangkui
		 * @CreateDate: 2017年6月07日 下午7:27:13 
		 * @Modifier: zhangkui
		 * @ModifyDate: 2017年7月5日 下午5:55:52 
		 * @ModifyRmk:  修改注释模板
		 * @version: V1.0
		 * @throws:
		 *
		 */
		@Action(value="reportList")
		public void reportList(){
			 try{
			   
				 //jasper文件名称 不含后缀
			   String rows=request.getParameter("reportJson");
			   
			   String bReportTime= request.getParameter("bReportTime");//表头时间
			   String eReportTime= request.getParameter("eReportTime");//表头时间
			   
			   String fileName = request.getParameter("fileName");//fileName=ROIW
			   String root_path = request.getSession().getServletContext().getRealPath("/");
			   root_path = root_path.replace('\\', '/');
			   String reportFilePath = root_path + webPath +fileName+".jasper";
			   List<OutInpatientWorkVo> list01 = JSONUtils.fromJson(rows, new TypeToken<List<OutInpatientWorkVo>>(){});
			   List<OutInpatientWorkVo> list=new ArrayList<OutInpatientWorkVo>(); 
			   if(list01!=null){
				   for(OutInpatientWorkVo v: list01){
						String vname=v.getProjectName();
						if(vname.contains("&nbsp;")){
							vname=vname.replace("&nbsp;", "   ");//替换空格,加2个空格,打印时的格式化，相当于tab键
							v.setProjectName(vname);
						}		
					   list.add(v);
				   }
			   }
			   
			   //javaBean数据封装
			   ArrayList<OutInpatientWorkReport> voList = new ArrayList<OutInpatientWorkReport>();
			   OutInpatientWorkReport vo = new OutInpatientWorkReport();
			   vo.setItemList(list);
			   voList.add(vo);  
			   JRDataSource jrd=new JRBeanCollectionDataSource(voList);
			  
			   String year= eReportTime.substring(0, 4);
			   String beforeYear=new Integer(Integer.parseInt(year)-1).toString();//前一年
			   String math="";//yyyy-MM
			   if(StringUtils.isNotBlank(bReportTime)){
				   String b=bReportTime.substring(5, 7);
				   String c=eReportTime.substring(5,7);
				   if(b.startsWith("0")){
					   b=b.substring(1, 2);
				   }
				   if(c.startsWith("0")){
					   c=c.substring(1, 2);
				   }
				   math=b+"-"+c;
			   }else{
				   math= eReportTime.substring(5,7);
				   if(math.startsWith("0")){
					   math=math.substring(1,2);
				   }
			   }
			   
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("tableTitle", year+"年"+math+"月门诊住院工作同期对比表");
			   if(StringUtils.isNotBlank(bReportTime)){
				   parameters.put("bTime",beforeYear+"年"+math+"月");
			   }else{
				   parameters.put("bTime",beforeYear+"年"+math+"月");
			   }
			  
			   if(StringUtils.isNotBlank(bReportTime)){
				   parameters.put("eTime",year+"年"+math+"月");
			   }else{
				   parameters.put("eTime",year+"年"+math+"月");
			   }
				String userName = ShiroSessionUtils.getCurrentUserFromShiroSession().getName();//获得当前登录的人名
				SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				String deptName=department.getDeptName();//当前科室;
			    parameters.put("userName", userName);
			    parameters.put("deptName", deptName);
			   
			   parameters.put("SUBREPORT_DIR", root_path + webPath);
			   iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
			  }catch(Exception e){
				
				  //e.printStackTrace();//不写这个是因为，能打印出来，但是却报打印异常
				//记录日志
				logger.error("KSTJ_MZZYGZTQDBB", e);
				
				//将日志信息存入mongodb中
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_MZZYGZTQDBB", "科室统计_门诊住院工作同期对比表", "2", "0"), e);
			  }
			 
		}
		
		//导出
		@Action(value="exportList")
		public void exportList(){
			   try {
				   
				   //jasper文件名称 不含后缀
				   String rows=request.getParameter("exportJson");
				   
				   String bReportTime= request.getParameter("bReportTime");//表头时间
				   String eReportTime= request.getParameter("eReportTime");//表头时间
				   
				   Gson gson = new Gson();
				   List<OutInpatientWorkVo> list01 = gson.fromJson(rows, new TypeToken<List<OutInpatientWorkVo>>(){}.getType());
				   List<OutInpatientWorkVo> list=new ArrayList<OutInpatientWorkVo>(); 
				   for(OutInpatientWorkVo v: list01){
						String vname=v.getProjectName();
						if(vname.contains("&nbsp;")){
							vname=vname.replace("&nbsp;", " ");//替换空格,加2个空格,导出时的格式化，相当于tab键
							v.setProjectName(vname);
						}		
					   list.add(v);
				   }
				   String year= eReportTime.substring(0, 4);
				   String beforeYear=new Integer(Integer.parseInt(year)-1).toString();//前一年
				   String math="";//yyyy-MM
				   if(StringUtils.isNotBlank(bReportTime)){
					   String b=bReportTime.substring(5, 7);
					   String c=eReportTime.substring(5,7);
					   if(b.startsWith("0")){
						   b=b.substring(1, 2);
					   }
					   if(c.startsWith("0")){
						   c=c.substring(1, 2);
					   }
					   math=b+"-"+c;
				   }else{
					   math= eReportTime.substring(5,7);
					   if(math.startsWith("0")){
						   math=math.substring(1,2);
					   }
				   }
				  
				  if(list==null||list.isEmpty()){
						response.setContentType("text/html;charset=utf-8");
						response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录!");
					}
				  	String name =year+"年"+math+"月门诊住院工作同期对比表";
				  	String head = "";
				  	
					if(StringUtils.isNotBlank(bReportTime)){
						String []headMessage={ "项目",  beforeYear+"年"+math+"月",  year+"年"+math+"月","增减数", "增减(%)"};
						for (String message : headMessage) {
							head += "," + message;
						}
					  	head = head.substring(1);
						FileUtil fUtil = new FileUtil();
						String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
						String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
						fUtil.setFilePath(filePath);
						fUtil.write(head);
						fUtil = outInpatientWorkService.export(list, fUtil);
						fUtil.close();
						DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
					}else{
						String [] headMessage={ "项目",beforeYear+"年"+math+"月", year+"年"+math+"月","增减数", "增减(%)"};
						for (String message : headMessage) {
							head += "," + message;
						}
					  	head = head.substring(1);
						FileUtil fUtil = new FileUtil();
						String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
						String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
						fUtil.setFilePath(filePath);
						fUtil.write(head);
						fUtil = outInpatientWorkService.export(list, fUtil);
						fUtil.close();
						DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
					}
			} catch(Exception e) {
						e.printStackTrace();
						
						//记录日志
						logger.error("KSTJ_MZZYGZTQDBB", e);
						
						//将日志信息存入mongodb中
						hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_MZZYGZTQDBB", "科室统计_门诊住院工作同期对比表", "2", "0"), e);
			}

			 
		}
		
		
		

		public String getbTime() {
			return bTime;
		}


		public void setbTime(String bTime) {
			this.bTime = bTime;
		}


		public String geteTime() {
			return eTime;
		}


		public void seteTime(String eTime) {
			this.eTime = eTime;
		}


		public String getMenuAlias() {
			return menuAlias;
		}


		public void setMenuAlias(String menuAlias) {
			this.menuAlias = menuAlias;
		}

		
		
	

}
