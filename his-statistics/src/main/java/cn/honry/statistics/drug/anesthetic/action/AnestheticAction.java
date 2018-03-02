package cn.honry.statistics.drug.anesthetic.action;


import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.anesthetic.service.AnestheticService;
import cn.honry.statistics.drug.anesthetic.vo.Anestheticvo;
import cn.honry.statistics.drug.anesthetic.vo.ReportAnesthetics;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/Anesthetic")
public class AnestheticAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(AnestheticAction.class);
	/**错误日志存储**/
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private DrugOutstore drugOutstore;
	
	private String login;//开始时间
	private String end;//结束时间
	private String drug;//药品性质
	private String deptName;//科室名字
	private String deptId;//科室code
	private String q;//查询参数
	private RedisUtil redisUtil;
	/** 
	* @Fields rows : 行数
	*/ 
	private String rows;
	/** 
	* @Fields page : 页数
	*/ 
	private String page;
	
	@Autowired
	@Qualifier(value = "redisUtil")
	public void setRedisUtil(RedisUtil redisUtil) { 
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	private String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
	
	
	@Autowired
	@Qualifier(value="anestheticService")
	private AnestheticService anestheticService;
	/**
	 * 编码查询接口Service
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	
	/** 
	* @Fields deptInInterService : 科室接口
	*/ 
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();

	/** 
	* @Fields drugType : 药品类型
	*/ 
	private String drugType;
	
	
	public String getDrugType() {
		return drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public DrugOutstore getDrugOutstore() {
		return drugOutstore;
	}

	public void setDrugOutstore(DrugOutstore drugOutstore) {
		this.drugOutstore = drugOutstore;
	}
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public AnestheticService getAnestheticService() {
		return anestheticService;
	}

	public void setAnestheticService(AnestheticService anestheticService) {
		this.anestheticService = anestheticService;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public void setCodeInInterService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**
	 *
	 * @Description：麻醉药品统计
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value="anesthetic",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/drug/anesthetic/anesthetic.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String anesthetic(){
		SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		deptId = dept == null ? "" : dept.getDeptCode();
		deptName = dept == null ? "" : dept.getDeptName();
		end=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		login=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.addDay(new Date(), -1));
		drug="1";
		return "list";
	}
	
	/**
	 *
	 * @Description：麻醉精神药品统计
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value="anestheList")
	public void anestheList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Anestheticvo> list = new ArrayList<>();
		Integer totalNum = 0;
		if(StringUtils.isNotBlank(login) && StringUtils.isNotBlank(end) && StringUtils.isNotBlank(drug) && StringUtils.isNotBlank(deptId)){
			String redKey = "MZJSYPTJ" + "_"+ login + "_" + end + "_" + drug + "_" + deptId;
			redKey=redKey.replaceAll(",", "-");
			totalNum = (Integer) redisUtil.get(redKey);
			boolean collection = new MongoBasicDao().isCollection("MZJSYPTJ");//判断mongon中是否存在该表
			if(totalNum == null){
				if (collection) {
					totalNum = anestheticService.queryAnestheticvo(login, end,drug,deptId,null,null).size();
				}else{
					totalNum = anestheticService.getAnestheTotal(login,end,drug,deptId);
				}
				redisUtil.set(redKey, totalNum);
			}
			String val=parameterInnerService.getparameter("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			if (totalNum != null && totalNum - 0 > 0) {
				if (collection) {
					list = anestheticService.queryAnestheticvo(login, end,drug,deptId, rows, page);
				}else{
					list = anestheticService.getAnestheList(login,end,drug,deptId,rows,page,"0");
				}
				
			}
		}
		map.put("total", totalNum);
		map.put("rows", list);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 *
	 * @Description：药品性质
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：hedong
	 * @ModifyDate：2016/8/12
	 * @ModifyRmk： 改用 BusinessDictionary获得drugType编码
	 * @version： 1.0：
	 *
	 */
	@Action(value="anestheDrug")
	public void anestheDrug(){
		List<BusinessDictionary> businessDictionaryList = innerCodeService.getDictionary("drugType");
		if(businessDictionaryList!=null&&businessDictionaryList.size()>0){
			BusinessDictionary code=new BusinessDictionary();
			code.setEncode("1");
			code.setName("全部");
			businessDictionaryList.add(0, code);
		}
		String json = JSONUtils.toExposeJson(businessDictionaryList, false, null, "encode","name");
		WebUtils.webSendJSON(json);
	}
	/** 科室下拉框
	* @Title: deptCombobox 
	* @author dtl 
	* @date 2017年3月27日
	*/
	@Action(value="deptCombobox")
	public void deptCombobox(){
		SysDepartment departSearch = new SysDepartment();
		if(StringUtils.isNotBlank(q)){
			departSearch.setDeptPinyin(q);
		}
		List<SysDepartment> sysDepartmentList = new ArrayList<>();
		List<SysDepartment> sysDepartmentList1 = new ArrayList<>();
		SysDepartment department = new SysDepartment();
		department.setDeptCode("1");
		department.setDeptName("全部");
		sysDepartmentList.add(department);
		//直接查询数据库会很慢,这里判断如果q为空,则直接调用接口
		if(StringUtils.isBlank(q)){
			sysDepartmentList1 = deptInInterService.queryAllDept();
		}else{
			sysDepartmentList1 = deptInInterService.getDeptByQ(q);
		}
		sysDepartmentList.addAll(sysDepartmentList1);
		deptId = "1";
		String json = JSONUtils.toJson(sysDepartmentList);			
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * @Description:导出 
	 * @Author： zhangjin @CreateDate： 2016-6-24
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "expOperactionlist", results = { @Result(name = "json", type = "json") })
	public void expOperactionlist()  {
		PrintWriter out=null;
		try {
		List<Anestheticvo> list = new ArrayList<Anestheticvo>();
		boolean collection = new MongoBasicDao().isCollection("MZJSYPTJ");//判断mongon中是否存在该表
		if (collection) {
			list = anestheticService.queryAnestheticvo(login, end,drug,deptId, null, null);
		}else{
			list = anestheticService.queryInvLogExp(login,end,drug,deptId);
		}
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "麻醉精神药品统计";
		String[] headMessage = { "科室", "住院号", "患者姓名", "医生姓名","药品名称","规格","单位","数量","取药日期","回收情况","经手人","备注" };

		for (String message : headMessage) {
			head += "," + message;
		}
		head = head.substring(1);
		FileUtil fUtil = new FileUtil();
		String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
		String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
		fUtil.setFilePath(filePath);
		fUtil.write(head);
		
		 out = WebUtils.getResponse().getWriter();
		
			fUtil = anestheticService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("YPGL_MZJSYPTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPGL_MZJSYPTJ", "药品管理_麻醉精神药品统计", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}

	/** 报表打印MZJSYP 
	* @Title: iReportMZJSYP 
	* @author dtl 
	* @date 2017年3月27日
	*/
	@Action(value = "iReportMZJSYP")
	public void iReportMZJSYP()  {
		try{
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀	 
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			List<Anestheticvo> anestheticvos = new ArrayList<Anestheticvo>();
			boolean collection = new MongoBasicDao().isCollection("MZJSYPTJ");//判断mongon中是否存在该表
			if (collection) {
				anestheticvos = anestheticService.queryAnestheticvo(login, end,drug,deptId, null, null);
			}else{
				anestheticvos = anestheticService.getAnestheList(login,end,drug,deptId,null, null, "1");
			}
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
			ReportAnesthetics reportAnesthetics = new ReportAnesthetics(login, end, deptName, drugType, anestheticvos);
			List<ReportAnesthetics> anesthetics = new ArrayList<>();
			anesthetics.add(reportAnesthetics);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(anesthetics);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", root_path + webPath);
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		}catch(Exception e){
			logger.error("YPGL_MZJSYPTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YPGL_MZJSYPTJ", "药品管理_麻醉精神药品统计", "2", "0"), e);
		}
	}
	
	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
