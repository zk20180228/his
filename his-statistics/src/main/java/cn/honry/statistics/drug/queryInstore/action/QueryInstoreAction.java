package cn.honry.statistics.drug.queryInstore.action;


import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugSupplycompany;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.drug.queryInstore.service.QueryInstoreService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 药品入库查询(统计)
 * @Description:
 * @author: zpty
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/QueryInstore")
public class QueryInstoreAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(QueryInstoreAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	@Autowired
    @Qualifier(value = "queryInstoreService")
 	private QueryInstoreService queryInstoreService;
	public void setQueryInstoreService(QueryInstoreService queryInstoreService) {
		this.queryInstoreService = queryInstoreService;
	}
	
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;

	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	/**
	 * 药品
	 */
	private String drug;
	/**
	 * 开始时间
	 */
	private String Stime;
	/**
	 * 结束时间
	 */
	private String Etime;
	/**
	 * 生产厂家
	 */
	private String company;
	/**
	 * 执行人员
	 */
	private String user;
	
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	/**  
	 *  
	 * @Description： 药品入库查询(统计)页面
	 * @Author：zpty
	 * @CreateDate：2016-6-22 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions(value={"YPRKCX:function:view"}) 
	@Action(value = "listQueryInstore", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/queryInstore/queryInstoreList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listQueryInstore() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		Stime = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  药品入库查询(统计)列表
	 * @Author：zpty
	 * @CreateDate：2015-6-22 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryInstore", results = { @Result(name = "json", type = "json") })
	public void queryInstore() {
		try{
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<DrugInStore> list = queryInstoreService.queryInstore(Stime, Etime, drug, page, rows,company,user);
			int total = queryInstoreService.queryInstoreTotle(Stime, Etime, drug,company,user);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  报表打印
	 * @Author：yuke
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "printInstore", results = { @Result(name = "json", type = "json") })
	public void printInstore() {
		try {
			Map<String, String> map = queryInstoreService.getCompanyName();
			String companyName= request.getParameter("companyName");//jasper文件所用到的参数 companyName
			String root_path = request.getSession().getServletContext().getRealPath("/");
			 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/YPRKCXD.jasper";
			Map<String,Object> parameterMap = new HashMap<String,Object>();
			DrugInStore vo=new DrugInStore();
			List<DrugInStore> list2 = queryInstoreService.queryInstoreExp(Stime, Etime, drug,company,user);
			if(map != null){
				for (int i = 0; i < list2.size(); i++) {
					list2.get(i).setCompanyCode(map.get(list2.get(i).getCompanyCode()));;
				}
			}
			vo.setList(list2);
			List<DrugInStore> list=new ArrayList<DrugInStore>();
			list.add(vo);
			
			if("".equals(Stime)){
				parameterMap.put("STime", null);
			}else{
				parameterMap.put("STime", Stime);
			}
			if("".equals(Etime)){
				parameterMap.put("ETime", null);
			}else{
				parameterMap.put("ETime", Etime);
			}
			if("".equals(drug)){
				parameterMap.put("drug", null);
			}else{
				parameterMap.put("drug", drug);
			}
			if("".equals(company)){
				parameterMap.put("company", null);
			}else{
				parameterMap.put("company", company);
			}
			if("".equals(companyName)){
				parameterMap.put("companyName", null);
			}else{
				parameterMap.put("companyName",URLDecoder.decode(companyName,"UTF-8"));
			}
			if("".equals(user)){
				parameterMap.put("user", null);
			}else{
				parameterMap.put("user", user);
			}
			parameterMap.put("hospital", HisParameters.PREFIXFILENAME);
			parameterMap.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
			iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameterMap, new JRBeanCollectionDataSource(list));
		} catch (Exception e) {
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "drugNameComboboxRegister", results = { @Result(name = "json", type = "json") })
	public void drugNameComboboxRegister() {
		try{
			List<DrugInfo> drugList = new ArrayList<DrugInfo>();
			drugList = queryInstoreService.getComboboxdrug();
			String json = JSONUtils.toExposeJson(drugList, false, null, "name","code");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
		}
	}

	/**  
	 *  
	 * @Description：  生产厂家下拉框
	 * @Author：zpty
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "companyNameComboboxRegister", results = { @Result(name = "json", type = "json") })
	public void companyNameComboboxRegister() {
		try{
			List<DrugSupplycompany> companyList = new ArrayList<DrugSupplycompany>();
			companyList = queryInstoreService.getComboboxCompany();
			String json = JSONUtils.toJson(companyList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
		}
	}
	//渲染生产企业
	@Action(value = "companyMap", results = { @Result(name = "json", type = "json") })
	public void companyMap() {
		try{
			List<DrugSupplycompany> companyList = new ArrayList<DrugSupplycompany>();
			companyList = queryInstoreService.getComboboxCompany();
			Map<String, String> map=new HashMap<String, String>();
			for(DrugSupplycompany company:companyList){
				map.put(company.getId(),company.getCompanyName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description： 人员下拉框
	 * @Author：zpty
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "userNameComboboxRegister", results = { @Result(name = "json", type = "json") })
	public void userNameComboboxRegister() {
		try{
			List<User> userList = new ArrayList<User>();
			userList = queryInstoreService.getComboboxUser();
			String json = JSONUtils.toExposeJson(userList, false, null, "name","id");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:导出 
	 * @Author： lt @CreateDate： 2015-9-10
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "expQueInstoList", results = { @Result(name = "json", type = "json") })
	public void expQueInstoList() throws Exception {
		List<DrugInStore> list = queryInstoreService.queryInstoreExp(Stime, Etime, drug,company,user);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "药品入库查询";
		String[] headMessage = { "药品名称", "规格", "单位", "入库数","零售价", "购进价","购进金额", "生产企业","批号", "药品效期","发票号","票单号" };
		for (String message : headMessage) {
			head += "," + message;
		}
		head = head.substring(1);
		FileUtil fUtil = new FileUtil();
		String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
		String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
		fUtil.setFilePath(filePath);
		fUtil.write(head);
		PrintWriter out = WebUtils.getResponse().getWriter();
		try {
			fUtil = queryInstoreService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("ZYTJ_YPRKCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_YPRKCX", "住院统计_药品入库查询", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
}
