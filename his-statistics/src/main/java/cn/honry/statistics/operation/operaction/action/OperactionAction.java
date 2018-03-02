package cn.honry.statistics.operation.operaction.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.statistics.drug.anesthetic.service.AnestheticService;
import cn.honry.statistics.operation.operaction.service.OperactionActionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/***
 * 手术器材统计Action
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/Operaction")
public class OperactionAction extends ActionSupport{
	private Logger logger=Logger.getLogger(OperactionAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="parameterInnerService")
	private ParameterInnerService parameterInnerService;
	
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 开始时间
	 */
	private String login;
	/**
	 * 结束时间
	 */
	private String end;
	/**
	 * 处方号
	 */
	private String repno;
	/**
	 * 单价
	 */
	private Double price;
	/**
	 * 科室code
	 */
	private String deptId;
	/**
	 * 科室name
	 */
	private String deptName;
	/**
	 * 页数
	 */
	private String page;
	/**
	 * 行数
	 */
	private String rows;
	
	/***
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public RedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
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

	public String getRepno() {
		return repno;
	}

	public void setRepno(String repno) {
		this.repno = repno;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public OperactionActionService getOperactionActionService() {
		return operactionActionService;
	}

	public void setOperactionActionService(
			OperactionActionService operactionActionService) {
		this.operactionActionService = operactionActionService;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	@Autowired
	@Qualifier(value = "operactionActionService")
	private OperactionActionService operactionActionService;
	@Autowired
	@Qualifier(value="anestheticService")
	private AnestheticService anestheticService;
	/***
	 * @Description:手术耗材统计
	 * @Description:
	 * @author:  zhangjin
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	@Action(value="operactionlist",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/operation/operaction/operaction.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String operactionlist(){
		SysDepartment sys=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(sys!=null&&StringUtils.isNotBlank(sys.getId())){
			deptName=anestheticService.getDeptName(sys.getId());
		}else{
			deptName="全部";
		}
		login=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.addDay(new Date(), -1));
		end=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return "list";
	}
	
	/**  
	 * 
	 * 手术耗材统计查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno 记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="getOperactionlist")
	public void getOperactionlist(){
		List<InpatientItemListNow> list=new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		String redKey = "SSHCTJ"+login+"_"+end+"_"+price+"_"+repno+"_"+deptId;
		redKey=redKey.replaceAll(",", "-");
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum == null){
			totalNum =operactionActionService.getOperationListTotal(login,end,price,repno,deptId);
			redisUtil.set(redKey, totalNum);
		}
		if (totalNum != null && totalNum - 1 >= 0) {
			list = operactionActionService.getOperactionlist(login,end,price,repno,deptId,page,rows);
		}
		String val=parameterInnerService.getparameter("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		map.put("total", totalNum);
		map.put("rows", list);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}

	/**  
	 * 
	 * 导出手术耗材统计
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:login 开始时间
	 * @param:end 结束时间
	 * @param:price 单价
	 * @param:deptId 科室code
	 * @param:repno 记账编号
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value = "expOperactionlist", results = { @Result(name = "json", type = "json") })
	public void expOperactionlist() throws Exception {
		List<InpatientItemListNow> list = operactionActionService.queryInvLogExp(login, end,price, repno);
		if (list == null || list.isEmpty()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
		}
		String head = "";

		String name = "手术耗材统计";
		String[] headMessage = { "记帐编号", "治疗项目", "单价", "数量","合计金额" };

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
			fUtil = operactionActionService.export(list, fUtil);
			fUtil.close();
			DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
			out.write("success");
		} catch (Exception e) {
			out.write("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("SSGL_SSHCTJ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("SSGL_SSHCTJ", "手术管理_手术耗材统计", "2", "0"), e);
		}finally {
			out.flush();
			out.close();
		}
	}
}
