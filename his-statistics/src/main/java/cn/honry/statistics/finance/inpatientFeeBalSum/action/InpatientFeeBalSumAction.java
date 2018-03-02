package cn.honry.statistics.finance.inpatientFeeBalSum.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.finance.inpatientFeeBalSum.service.InpatientFeeBalSumService;
import cn.honry.statistics.finance.inpatientFeeBalSum.vo.FeeBalSumVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * <p> 在院（出院）病人医药费结算汇总表</p>
 * @Author: yuke
 * @CreateDate: 2017年7月4日 下午3:30:25 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月4日 下午3:30:25 
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
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/statistics/InpatientFeeBalSum")
@SuppressWarnings({"all"})
public class InpatientFeeBalSumAction extends ActionSupport  {
	private Logger logger=Logger.getLogger(InpatientFeeBalSumAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	@Autowired
	@Qualifier(value = "inpatientFeeBalSumService")
	private InpatientFeeBalSumService inpatientFeeBalSumService;		
	public void setInpatientFeeBalSumService(InpatientFeeBalSumService inpatientFeeBalSumService) {
		this.inpatientFeeBalSumService = inpatientFeeBalSumService;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	/**
	 * 起始页数
	 */
	private String page;
	/**
	 * 数据列数
	 */
	private String rows;
	/**
	 * 开始时间
	 */
	private String startTime; 
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 查询类型
	 */
	private String typeSerc;
	
	 //开始时间
	private String sTime;
	 //结束时间
	private String eTime;
	/**查询科室**/
	private String deptCode;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTypeSerc() {
		return typeSerc;
	}
	public void setTypeSerc(String typeSerc) {
		this.typeSerc = typeSerc;
	}
	/**
	 * @Description:获取list页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-22
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"STAT-BRYYFJSHZ:function:view"})
	@Action(value="toViewFeeBalSum",results={@Result(name="list",location = "/WEB-INF/pages/stat/inpatientFeeBalSum/inpatientFeeBalSum.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewFeeBalSum()throws Exception{
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(sys!=null){
			 ServletActionContext.getRequest().setAttribute("deptId",sys.getId());//获取当前病区
		}
		//获取时间
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateYM(date)+"-01";
		ServletActionContext.getRequest().setAttribute("sTime", sTime);
		ServletActionContext.getRequest().setAttribute("eTime", eTime);
		return "list";
	}
	
	/**
	 * @Description:查询List
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-6-20
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryInpatientFeeBalSum", results = { @Result(name = "json", type = "json") })
	public void queryInpatientFeeBalSum() throws Exception{
		if(StringUtils.isBlank(sTime)){
			Date date = new Date();
			sTime = DateUtils.formatDateYM(date)+"-01";
		}
		if(StringUtils.isBlank(eTime)){
			Date date = new Date();
			eTime = DateUtils.formatDateY_M_D(date);
		}
		List<FeeBalSumVo> list = inpatientFeeBalSumService.getPage(page, rows,typeSerc,eTime,sTime,deptCode);
		int total = inpatientFeeBalSumService.getTotal(sTime, eTime, typeSerc,deptCode);
		String jsons = "{\"total\":"+total+",\"rows\":[";
		if(list!=null && list.size()>0){
			int sizes = list.size();
			int num = 0;
			double totcost = 0;
			double totcosts = 0;
			double totCost = 0;
			double priCost = 0;
			double pubCost = 0;
			for(int z=0;z<list.size();z++){
				totCost +=list.get(z).getCost()!=null?list.get(z).getCost():0;
				if(list.get(z).getPaykindCode()!=null){
					if("01".equals(list.get(z).getPaykindCode())){
						priCost += list.get(z).getCost()!=null?list.get(z).getCost():0;
					}
					if("02".equals(list.get(z).getPaykindCode())){
						pubCost += list.get(z).getCost()!=null?list.get(z).getCost():0;
					}
				}
			}										
			int aa = Integer.valueOf(rows)/2+4;
			for(int i=0;i<aa;i++){
				num++;				
				int nums = num+aa;
				String feeStatCode = "";
				if(i<sizes){
					feeStatCode = list.get(i).getFeeStatCode()!=null?list.get(i).getFeeStatCode():"";
				}				
				List<MinfeeStatCode> minfeeStatlist = inpatientFeeBalSumService.getFeeStatName(feeStatCode);
				String feeStatName = "";
				if(minfeeStatlist!=null&&minfeeStatlist.size()>0){
					feeStatName = minfeeStatlist.get(0).getFeeStatName();
				}
				double cost = 0;
				if(i<sizes){
					cost = list.get(i).getCost()!=null?list.get(i).getCost():0;
				}				
				String feeStatCodes = sizes>=aa+i?list.get(i+aa-1).getFeeStatCode():"";
				List<MinfeeStatCode> minfeeStatlists = inpatientFeeBalSumService.getFeeStatName(feeStatCodes);
				String feeStatNames = "";
				if(minfeeStatlists!=null&&minfeeStatlists.size()>0){
					feeStatNames = minfeeStatlists.get(0).getFeeStatName();
				}
				double costs = sizes>=aa+i?list.get(i+aa-1).getCost():0;
				if(i<sizes){
					if(sizes>aa){
						if(i<sizes-2){
							totcost+=cost;
						}
					}
					if(sizes==aa){
						if(i<sizes-1){
							totcost+=cost;
						}
					}
					if(sizes<aa){
						if(i<sizes){
							totcost+=cost;
						}
					}
					totcosts+=costs;
				}																
				String a="\"projectName\":\""+feeStatName+"\",\"cost\":"+Double.valueOf(NumberUtil.init().format(cost,2))+"";
				String b="\"projectNames\":\"\",\"costs\":\"\"";
				String c="\"projectNames\":\""+feeStatNames+"\",\"costs\":"+Double.valueOf(NumberUtil.init().format(costs,2))+"";
				String d="\"projectName\":\"\",\"cost\":\"\"";
				String e="\"projectName\":\"小计\",\"cost\":"+Double.valueOf(NumberUtil.init().format(totcost,2))+"";
				String f="\"projectName\":\"合计\",\"cost\":"+Double.valueOf(NumberUtil.init().format(totCost,2))+"";
				String g="\"projectName\":\"其中：自费患者\",\"cost\":"+Double.valueOf(NumberUtil.init().format(priCost,2))+"";
				String h="\"projectName\":\"医疗保险\",\"cost\":"+Double.valueOf(NumberUtil.init().format(pubCost,2))+"";
				String j="\"projectName\":\"本院职工\",\"cost\":\"\"";
				String k="\"projectNames\":\"小计\",\"costs\":"+Double.valueOf(NumberUtil.init().format(totcosts,2))+"";
				String l="\"projectNames\":\"合计\",\"costs\":"+Double.valueOf(NumberUtil.init().format(totCost,2))+"";
				String m="\"projectNames\":\"其中：自费患者费用\",\"costs\":"+Double.valueOf(NumberUtil.init().format(priCost,2))+"";
				String n="\"projectNames\":\"医疗保险\",\"costs\":"+Double.valueOf(NumberUtil.init().format(pubCost,2))+"";
				String p="\"projectNames\":\"本院职工\",\"costs\":\"\"";
				String q="\"projectNames\":\"统计人数\",\"costs\":"+Double.valueOf(NumberUtil.init().format(sizes,2))+"";
				if(sizes<=aa-4){
					if(i<sizes){
						jsons +="{"+a+","+b+"},";
					}else if(sizes<=i&&i<sizes+4){
						if(sizes+4<aa){
							if(i==sizes){
								jsons +="{"+f+","+b+"},";
							}else if(i==sizes+1){
								jsons +="{"+g+","+b+"},";
							}else if(i==sizes+2){
								jsons +="{"+h+","+b+"},";
							}else if(i==sizes+3){
								jsons +="{"+j+","+b+"},";
							}
						}else{
							if(i==sizes){
								jsons +="{"+f+","+b+"},";
							}else if(i==sizes+1){
								jsons +="{"+g+","+b+"},";
							}else if(i==sizes+2){
								jsons +="{"+h+","+b+"},";
							}else if(i==sizes+3){
								jsons +="{"+j+","+q+"}]}";
							}			
						}						
					}else if(i>=sizes+4 &&i<aa-1){
						jsons +="{"+d+","+b+"},";
					}else{
						jsons +="{"+d+","+q+"}]}";
					}					
				}
				if(sizes==aa-3){
					if(i==0){
						jsons +="{"+a+","+p+"},";
					}else if(i>0&&i<sizes){
						jsons +="{"+a+","+b+"},";
					}else if(sizes<=i&&i<sizes+3){
						if(sizes+3<aa){
							jsons +="{"+f+","+b+"},{"+g+","+b+"},{"+h+","+b+"},";
							i=i+3;
						}else{
							jsons +="{"+f+","+b+"},{"+g+","+b+"},{"+h+","+q+"}]}";
							i=i+3;
						}						
					}					
				}
				if(sizes==aa-2){
					if(i==0){
						jsons +="{"+a+","+n+"},";
					}else if(i==1){
						jsons +="{"+a+","+p+"},";
					}else if(i>1&&i<sizes){
						jsons +="{"+a+","+b+"},";
					}else if(sizes<=i&&i<sizes+2){
						if(sizes+2<aa){
							jsons +="{"+f+","+b+"},{"+g+","+b+"},";
							i=i+2;
						}else{
							jsons +="{"+f+","+b+"},{"+g+","+q+"}]}";
							i=i+2;
						}						
					}					
				}
				if(sizes==aa-1){
					if(i==0){
						jsons +="{"+a+","+m+"},";
					}else if(i==1){
						jsons +="{"+a+","+n+"},";
					}else if(i==2){
						jsons +="{"+a+","+p+"},";
					}else if(i>2&&i<sizes){
						jsons +="{"+a+","+b+"},";
					}else if(sizes<=i&&i<sizes+1){
						if(sizes+1<aa){
							jsons +="{"+f+","+b+"},";
							i=i+1;
						}else{
							jsons +="{"+f+","+q+"}]}";
							i=i+1;
						}						
					}					
				}
				if(sizes>=aa){
					if(i<=sizes-aa){
						jsons +="{"+a+","+c+"},";
					}else if(i==sizes-aa+1){
						jsons +="{"+a+","+k+"},";
					}else if(i==sizes-aa+2){
						jsons +="{"+a+","+l+"},";
					}else if(i==sizes-aa+3){
						jsons +="{"+a+","+m+"},";
					}else if(i==sizes-aa+4){
						jsons +="{"+a+","+n+"},";
					}else if(i==sizes-aa+5){
						jsons +="{"+a+","+p+"},";
					}else if(sizes-aa+5<i){
						if(i==aa-1){
							jsons +="{"+e+","+q+"}]}";							
						}else{
							jsons +="{"+a+","+b+"},";
						}						
					}					
				}					
			}				
		}
		if(list==null||list.size()==0){
			jsons +="]}";
		}
		WebUtils.webSendJSON(jsons);
	}
	
	
	@Action(value = "expFeeBalSum", results = { @Result(name = "json", type = "json") })
	public void expFeeBalSum() throws Exception {
		List<FeeBalSumVo> list  = inpatientFeeBalSumService.getFeeBalSum(sTime, eTime, typeSerc);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "在院（出院）病人医药费结算汇总";
		String[] headMessage = {"序号","项目", "金额"};

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
			fUtil = inpatientFeeBalSumService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			logger.error("ZYTJ_ZYCYBRYYFJSHZ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYCYBRYYFJSHZ", "住院统计_在院(出院)病人医药费结算汇总", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
}
