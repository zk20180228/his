package cn.honry.statistics.deptstat.internalCompare2.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.basic.MongoManager;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.deptstat.internalCompare1.service.InternalCompare1Service;
import cn.honry.statistics.deptstat.internalCompare2.service.InternalCompare2Service;
import cn.honry.statistics.deptstat.internalCompare2.vo.InternalCompare2Vo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**  
 * 
 * <p>医院内科医学部和内二医学部对比表2 </p>
 * @Author: yuke
 * @CreateDate: 2017年7月3日 下午4:30:44 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月3日 下午4:30:44 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/deptstat/internalCompare2")
public class InternalCompare2Action extends MongoBasicDao{
	private Logger logger=Logger.getLogger(InternalCompare2Action.class);
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	private String deptCode1;
	private String deptCode2;
	private String Stime;
	private String Etime;
	private String exportJson;
//	private String district;
	private String date;
	private String collo;//集合
	private String inde1;//索引1
	private String inde2;//索引2
	
	public String getInde1() {
		return inde1;
	}
	public void setInde1(String inde1) {
		this.inde1 = inde1;
	}
	public String getInde2() {
		return inde2;
	}
	public void setInde2(String inde2) {
		this.inde2 = inde2;
	}
	public String getCollo() {
		return collo;
	}
	public void setCollo(String collo) {
		this.collo = collo;
	}
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="internalCompare1Service")
	private InternalCompare1Service internalCompare1Service;
	
	public void setInternalCompare1Service(
			InternalCompare1Service internalCompare1Service) {
		this.internalCompare1Service = internalCompare1Service;
	}
	@Autowired
    @Qualifier(value = "internalCompare2Service")
 	private InternalCompare2Service internalCompare2Service;
	private HttpServletResponse response = ServletActionContext.getResponse();
	private HttpServletRequest request = ServletActionContext.getRequest();
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	@Autowired
	@Qualifier("deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getExportJson() {
		return exportJson;
	}
	public void setExportJson(String exportJson) {
		this.exportJson = exportJson;
	}
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	public InternalCompare2Service getInternalCompare2Service() {
		return internalCompare2Service;
	}
	public void setInternalCompare2Service(InternalCompare2Service internalCompare2Service) {
		this.internalCompare2Service = internalCompare2Service;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public String getDeptCode1() {
		return deptCode1;
	}
	public void setDeptCode1(String deptCode1) {
		this.deptCode1 = deptCode1;
	}
	public String getDeptCode2() {
		return deptCode2;
	}
	public void setDeptCode2(String deptCode2) {
		this.deptCode2 = deptCode2;
	}
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	@RequiresPermissions(value={"YYNKYXBHNEYXBDBBT:function:view"}) 
	@Action(value = "internalCompare2list", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/internalCompare2/internalCompare2.jsp") }, 
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String internalCompare2list() {
		return "list";
	}
	@Action(value = "getIndex")
	public void getIndex() {
		DB DBDatabase = MongoManager.getDBDatabase();
		List<DBObject> listIndex=DBDatabase.getCollection(collo).getIndexInfo();
		String json=JSONUtils.toJson(listIndex);
		WebUtils.webSendJSON(json);
	}
	@Action(value = "initDate")
	public void initDate() {
			new Thread(new Runnable() {
				@Override
				public void run() {
						try {
							internalCompare1Service.initCompare1("2003-01", "2017-09", 2);
							internalCompare2Service.init_YYNKYXBHNEYXBDBBT("2003-01", "2017-09", 2);
							new Exception();
						} catch (Exception e) {
							logger.error("KSTJ_YYNKYXBHNEYXBDBBT2", e);
							hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YCLSHWC", "预处理数据完成", "2", "0"), e);
						}
				}
			}).start();
	}
	@Action(value = "creteIndex")
	public void creteIndex() {
		String json=null;
		try {
		 DB DBDatabase = MongoManager.getDBDatabase();
		 DBCollection colle=DBDatabase.getCollection(collo);
		 BasicDBObject obj=  new BasicDBObject();
		 obj.append(inde1, 1);
		 obj.append(inde2, -1);
		 colle.createIndex(obj);
		 json=JSONUtils.toJson("success");
		} catch (Exception e) {
		 json=JSONUtils.toJson("error");
			e.printStackTrace();
		}
		WebUtils.webSendJSON(json);
	}
	@Action(value = "delIndex")
	public void delIndex() {
		String json=null;
		try {
		 DB DBDatabase = MongoManager.getDBDatabase();
		 DBCollection colle=DBDatabase.getCollection(collo);
		 colle.dropIndex(inde1);
		 json=JSONUtils.toJson("success");
		} catch (Exception e) {
		 json=JSONUtils.toJson("error");
			e.printStackTrace();
		}
		WebUtils.webSendJSON(json);
	}
//	@Action(value = "initinternalCompare2list", results = { @Result(name = "json", type = "json") })
//	public void initinternalCompare2list() {
//		try {
//			internalCompare2Service.initCompare2list(Stime,Etime);
//		} catch (Exception e) {
//			logger.error("KSTJ_YYNKYXBHNEYXBDBBT2", e);
//			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_YYNKYXBHNEYXBDBBT2", "科室统计_医院内科医学部和内二医学部对比表2", "2", "0"), e);
//		}
//	} 
	/**
	 * 
	 * <p>查询数据</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:31:05 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:31:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "printinternalCompare2list")
	public void printinternalCompare2list() {
		String[] arr = Stime.split("-");
		String dateView=arr[0]+"年"+arr[1]+"月";
		int time = Integer.parseInt(arr[0])-1;
		String timed = time+"-"+arr[1];
		String [] codeArr={deptCode1,deptCode2};
		List<InternalCompare2Vo> allList=new ArrayList<InternalCompare2Vo>(); 
	try {
		for(int i=0;i<codeArr.length;i++){
			List<InternalCompare2Vo> internalCompare2List = internalCompare2Service.queryinternalCompare2list(timed,Stime,codeArr[i]);
			InternalCompare2Vo vo=new InternalCompare2Vo();
			if(i==0){
				vo.setDeptCode("内科医学部");
			}else{
				vo.setDeptCode("内二医学部");
			}
			internalCompare2List.add(0,vo);
			int size=allList.size();
			allList.addAll(size,internalCompare2List);
		}
		   String root_path = request.getSession().getServletContext().getRealPath("/");
		   root_path = root_path.replace('\\', '/');
		   String reportFilePath = root_path + webPath +"NKYXBNEYXBDBB.jasper";
		   InternalCompare2Vo vo=new InternalCompare2Vo();
		 //渲染科室
			Map<String,String> map=deptInInterService.querydeptCodeAndNameMap();
			String dept;
			String depts;
			for(int i=5,len=allList.size();i<len;i++){
				dept=allList.get(i).getDeptCode();
				depts=map.get(dept);
				if(depts==null){
					allList.get(i).setDeptCode(dept);
					i=i+4;
				}else{
					allList.get(i).setDeptCode(depts);
				}
			}
		   vo.setList(allList);
		   List<InternalCompare2Vo> list=new ArrayList<InternalCompare2Vo>(1);
		   list.add(vo);
		   JRDataSource jrd=new JRBeanCollectionDataSource(list);
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("tital", dateView+"郑州大学第一附属医院内科医学部和内二医学部对比表2");
		   parameters.put("dateOne", timed.split("-")[0]+"年");
		   parameters.put("dateTwo", Stime.split("-")[0]+"年");
		   parameters.put("SUBREPORT_DIR", root_path + webPath);
		
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,jrd);
		} catch (Exception e) {
			logger.error("KSTJ_YYNKYXBHNEYXBDBBT2", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_YYNKYXBHNEYXBDBBT2", "科室统计_医院内科医学部和内二医学部对比表2", "2", "0"), e);
		}
		
	}
	/**  
	 * 
	 * <p>带合计的统计 </p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:30:23 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:30:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "queryinternalCompare2listHJ", results = { @Result(name = "json", type = "json") })
	public void queryinternalCompare2listHJ() {
		String[] arr = Stime.split("-");
		int time = Integer.parseInt(arr[0])-1;
		String timed = time+"-"+arr[1];
		String [] codeArr={deptCode1,deptCode2};
		List<InternalCompare2Vo> allList=new ArrayList<InternalCompare2Vo>(); 
		try {
		for(int i=0;i<codeArr.length;i++){
			List<InternalCompare2Vo> internalCompare2List;
				internalCompare2List = internalCompare2Service.queryinternalCompare2list(timed,Stime,codeArr[i]);
				InternalCompare2Vo vo=new InternalCompare2Vo();
				vo.setDeptCode("combobox"+i);
				internalCompare2List.add(0,vo);
				int size=allList.size();
				allList.addAll(size,internalCompare2List);
		}
		} catch (Exception e) {
			logger.error("KSTJ_YYNKYXBHNEYXBDBBT2", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("KSTJ_YYNKYXBHNEYXBDBBT2", "科室统计_医院内科医学部和内二医学部对比表2", "2", "0"), e);
		}
		
		String json = JSONUtils.toJson(allList,true, null, false);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 
	 * <p>导出功能</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:40:39 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:40:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "excelPort", results = { @Result(name = "json", type = "json") })
	public void excelPort() throws Exception  {
		List<InternalCompare2Vo> journalVos = JSONUtils.fromJson(exportJson, new TypeToken<List<InternalCompare2Vo>>(){});
		if (journalVos == null || journalVos.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String name =date+"月"+HisParameters.PREFIXFILENAME+"科室对比表";
		String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".xls";
		String filename = new String(fileName.getBytes(),"ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		ServletOutputStream stream = response.getOutputStream();
		internalCompare2Service.exportExcel(stream,journalVos,date);
//		WebUtils.webSendJSON("ok");
		}
}
