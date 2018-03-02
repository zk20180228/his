package cn.honry.statistics.drug.billsearch.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.honry.statistics.drug.billsearch.service.BillSearchService;
import cn.honry.statistics.drug.billsearch.vo.BillClassHzVo;
import cn.honry.statistics.drug.billsearch.vo.BillClassMxVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 摆药单分类Action
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/BillSearch")
public class BillSearchAction extends ActionSupport{
	
	/**
	 * id
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(BillSearchAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	/**
	 * 开始时间
	 */
	private String beginTime;
	
	/**
	 * 结束时间 
	 */
	private String endTime;
	
	/**
	 * 摆药单号
	 */
	private String drugedBill;
	/**
	 * 摆药单分类
	 */
	private String billCode;
	
	/**
	 * 申请状态
	 */
	private String applyState;
	/**
	 * 分页 页数
	 */
	private String page;
	/**
	 * 分页 记录数
	 */
	private String rows;
	
	/**
	 * 查询条件 名称 拼音码 五笔码
	 */
	private String bname;
	/**
	 * 摆药单分类service
	 */
	@Autowired
    @Qualifier(value = "billSearchService")
 	private BillSearchService billSearchService;
	
	
	
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDrugedBill() {
		return drugedBill;
	}

	public void setDrugedBill(String drugedBill) {
		this.drugedBill = drugedBill;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setBillSearchService(BillSearchService billSearchService) {
		this.billSearchService = billSearchService;
	}

	/***
	 * 摆药单查询
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月12日 
	 * @version 1.0
	 */
	@Action(value="listBillSearch",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/drug/billSearch/billSearchList.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String listBillSearch(){
		return "list";
	}
	
	/**
	 * 病区下对应科室下的摆药单树
	 * @author tangfeishuai
	 * @createDate：
	 * @modifier 
	 * @modifyDate：2016年6月12日 上午11:00:02  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getBillSearchTree")
	public void getBillSearchTree() {
		try{
			List<TreeJson> treeBillSearch=billSearchService.treeBillSearch(beginTime, endTime,  drugedBill, applyState);
			String treeBillSearchjson = JSONUtils.toJson(treeBillSearch);
			WebUtils.webSendJSON(treeBillSearchjson);
		}catch(Exception e){
			logger.error("ZYTJ_BYDCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_摆药单查询_导出明细", "2", "0"), e);
		}
	}
	
	/**
	 *
	 * @Description：摆药单汇总查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月13日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryBillClassHz")
	public void queryBillClassHz() {
		try{
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<BillClassHzVo> list = billSearchService.getBillClassHzVo(drugedBill, applyState,bname, page, rows);
			int total = billSearchService.getBillHzTotal(drugedBill,bname, applyState);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_BYDCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_摆药单查询_导出明细", "2", "0"), e);
		}
	}
	/**
	 *
	 * @Description：摆药单明细查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月13日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryBillClassMx")
	public void queryBillClassMx() {
		try{
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<BillClassMxVo> list = billSearchService.getBillClassMxVo(drugedBill, applyState,bname, page, rows);
			int total = billSearchService.getBillMxTotal(drugedBill, bname,applyState);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_BYDCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_摆药单查询_导出明细", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：导出摆药单汇总信息
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月29日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 */
	@Action(value = "outBillClassHzVo")
	public void outBillClassHzVo() throws Exception {
		List<BillClassHzVo> list = billSearchService.getAllBillClassHzVo(drugedBill, applyState, bname);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "摆药单汇总查询统计";
		String[] headMessage = { "药品名称", "规格", "总量", "申请科室","领药药房", "摆药单","拼音码", "五笔码" };

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
			fUtil = billSearchService.exportBillClassHzVo(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("ZYTJ_BYDCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_摆药单查询_导出汇总", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
	/**
	 * @Description：导出摆药单明细信息
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月29日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 */
	@Action(value = "outBillClassMxVo")
	public void outBillClassMxVo() throws Exception {
		List<BillClassMxVo> list = billSearchService.getAllBillClassMxVo(drugedBill, applyState, bname);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";
		
		String name = "摆药单明细查询统计";
		String[] headMessage = { "床号", "姓名", "住院号", "药品名称","规格", "每次量","剂量单位", "频次","用法","总量", "申请科室","取药药房", "摆药单","发药时间","拼音码" ,"五笔码","有效性","状态"};
		
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
			fUtil = billSearchService.exportBillClassMxVo(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("ZYTJ_BYDCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_BYDCX", "住院统计_摆药单查询_导出明细", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}

}
