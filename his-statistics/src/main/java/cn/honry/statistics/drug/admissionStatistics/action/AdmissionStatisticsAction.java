package cn.honry.statistics.drug.admissionStatistics.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.drug.admissionStatistics.service.AdmissionStatisticsService;
import cn.honry.statistics.drug.admissionStatistics.vo.AdmissionStatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>住院部用药统计</p>
 * @Author: yuke
 * @CreateDate: 2017年7月4日 下午3:48:37 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月4日 下午3:48:37 
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
@Namespace(value="/statistics/AdmissionStatistics")
public class AdmissionStatisticsAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(AdmissionStatisticsAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 注入住院用药统计Service
	 */
	@Autowired 
	@Qualifier(value = "admissionStatisticsService")
	private AdmissionStatisticsService admissionStatisticsService;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	/**
	 * 开始时间
	 */
	private String beginTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 发药药房
	 */
	private String deptCode;
	
	/**
	 * 入库科室
	 */
	private String storageCode;
	
	/**
	 * 药品类别
	 */
	private String drugType; 
	
	/**
	 * 出库类型
	 */
	private String outType;
	
	/**
	 * 前页数 
	 */
	private String page;
	
	/**
	 * 记录数
	 */
	private String rows;
	
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getDrugType() {
		return drugType;
	}
	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}
	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public void setRows(String rows) {
		this.rows = rows;
	}
	public void setAdmissionStatisticsService(AdmissionStatisticsService admissionStatisticsService) {
		this.admissionStatisticsService = admissionStatisticsService;
	}
	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016-5-27上午10:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYBYYTJ:function:view"}) 
	@Action(value = "listAdmissionStatistics", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/drug/admissionStatistics/admissionStatisticsList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listAdmissionStatistics() {
		return "list";
	}
	/**
	 *
	 * @Description：住院用药查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年5月31日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryAdmissionStatistics")
	public void queryAdmissionStatistics() {
		try {
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<AdmissionStatisticsVo> list = admissionStatisticsService.getAdmissionStatisticsVo(beginTime, endTime, deptCode, storageCode, drugType, outType, page, rows);
			int total = admissionStatisticsService.getTotal(beginTime, endTime, deptCode, storageCode, drugType, outType);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJ_ZYBYYTj", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYBYYTj", "住院统计_住院部用药统计", "2", "0"), e);
		}
	}
	/**
	 *
	 * @Description：药品类型查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月23日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "queryCodeDrugType")
	public void queryCodeDrugType() {
		List<BusinessDictionary> list = innerCodeService.getDictionary("drugtype");
		String json = JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	/**
	 *
	 * @Description：药房查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月23日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Action(value = "querySysYaoFang")
	public void querySysYaoFang() {
		try {
			List<SysDepartment> list = admissionStatisticsService.getSysDepartment();
			String json = JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYTJ_ZYBYYTj", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYBYYTj", "住院统计_住院部用药统计", "2", "0"), e);
		}
	}
	
	/**
	 * @Description：住院用药信息查询
	 * @Author：tangfeishuai
	 * @CreateDate：2016年6月22日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 */
	@Action(value = "outAdmissionStatisticsVo")
	public void outAdmissionStatisticsVo() {
		PrintWriter out = null;
		try {
		List<AdmissionStatisticsVo> list = admissionStatisticsService.getAllAdmissionStatisticsVo(beginTime, endTime, deptCode, storageCode, drugType, outType);
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head = "";
			String name = "住院部用药统计";
			String[] headMessage = { "基本药物码", "招标流水码", "药品编码", "药品名称","当前状态", "摆药状态","规格", "数量", "单位","单价", "金额"};
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
			
				fUtil = admissionStatisticsService.export(list, fUtil);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
				out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("ZYTJ_ZYBYYTj", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYBYYTj", "住院统计_住院部用药统计", "2", "0"), e);
		} finally {
			out.flush();
			out.close();
		}
	}
	

}
