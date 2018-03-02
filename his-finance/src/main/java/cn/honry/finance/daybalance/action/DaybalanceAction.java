package cn.honry.finance.daybalance.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterBalancedetailVo;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.User;
import cn.honry.finance.daybalance.service.DaybalanceService;
import cn.honry.finance.daybalance.vo.PayTypeVo;
import cn.honry.report.service.IReportService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**  
 *  
 * @className：DaybalanceAction
 * @Description：  DaybalanceAction
 * @Author：aizhonghua
 * @CreateDate：2015-6-26 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-26 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/finance/daybalance")
//@Namespace(value = "/register")
public class DaybalanceAction extends ActionSupport implements ModelDriven<RegisterDaybalance> {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public RegisterDaybalance getModel() {
		return daybalance;
	}
	@Autowired
	@Qualifier(value = "daybalanceService")
	private DaybalanceService daybalanceService;
	public void setDaybalanceService(DaybalanceService daybalanceService) {
		this.daybalanceService = daybalanceService;
	}
	private RegisterDaybalance daybalance = new RegisterDaybalance();
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/**
	 * @see注入报表借口口
	 */
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	
	/**  
	 *  
	 * @Description：  跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHYRJ:function:view"})
	@Action(value = "listDaybalance", results = { @Result(name = "list", location = "/WEB-INF/pages/register/daybalance/daybalanceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listDaybalance() {
		//获得开始时间(上一次的结算时间 ,如果没有返回当天的0点)
		Date startTime = daybalanceService.getStartTime();
		RegisterDaybalance registerDaybalance = new RegisterDaybalance();
		registerDaybalance.setStartTime(startTime);
		//结束时间为当前时间
		registerDaybalance.setEndTime(new Date());
		request.setAttribute("registerDaybalance", registerDaybalance);
		return "list";
	}
	
	
	/**  
	 *  
	 * @Description：  获得某一时间段的日结信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHYRJ:function:query"})
	@Action(value = "queryDaybalance", results = { @Result(name = "list", location = "/WEB-INF/pages/register/daybalance/daybalanceList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String queryDaybalance() {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//计算日结详细信息
		List<RegisterBalancedetail> balancedetailList = daybalanceService.getBalance(daybalance,userId);
		request.setAttribute("balancedetailList", balancedetailList);
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		String detailJson = gson.toJson(balancedetailList);
		request.setAttribute("detailJson", detailJson);
		//获得支付类型
		Map<String, String> psyTypeMap = getPayTypeMap();
		request.setAttribute("psyTypeMap", psyTypeMap);
		//计算日结信息
//		RegisterDaybalance registerDaybalance = daybalanceService.getDaybalance(daybalance,userId);
		RegisterDaybalance registerDaybalance = daybalanceService.getDaybalanceNow(balancedetailList);
		registerDaybalance.setStartTime(daybalance.getStartTime());
		registerDaybalance.setEndTime(daybalance.getEndTime());
		request.setAttribute("registerDaybalance", registerDaybalance); 
		//获得开始时间(上一次的结算时间 ,如果没有返回当天的0点)
		Date startTime = daybalanceService.getStartTime();
		request.setAttribute("startTime", startTime); 
		//结束时间为当前时间
		request.setAttribute("endTime", new Date());
		return "list";
	}
	
	public Map<String, String> getPayTypeMap(){
		Map<String, String> psyTypeMap = new HashMap<String, String>();
		List<PayTypeVo> psy = daybalanceService.getPayType();
		
		//[{id:'1',value:'现金'},{id:'2',value:'银联卡'},{id:'3',value:'支票'},{id:'4',value:'院内账户'}]
		/*psyTypeMap.put("1", "现金");
		psyTypeMap.put("2", "银联卡");
		psyTypeMap.put("3", "支票");
		psyTypeMap.put("4", "院内账户");*/
		for(int i=0;i<psy.size();i++){
			psyTypeMap.put(psy.get(i).getId(), psy.get(i).getName());
		}
		
		return psyTypeMap;
	}
	
	/**  
	 *  
	 * @Description：  保存挂号日结档信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-30 下午01:42:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-30 下午01:42:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	@RequiresPermissions(value={"GHYRJ:function:checkout"})
	@Action(value = "saveDaybalance", results = { @Result(name = "json", type = "json") })
	public void saveDaybalance() throws IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			map = daybalanceService.saveDaybalance(daybalance);
		} catch (Exception e) {
			map.put("resMsg", "error");
			map.put("resCode", "未知错误请联系管理员!");
		}
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
			out.write(gson.toJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**  
	 *  
	 * @Description：  查看打印信息
	 * @Author：donghe
	 * @CreateDate：2015-12-7 下午02:34:39  
	 * @Modifier：donghe
	 * @ModifyDate：2015-12-7 下午02:34:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *  
	 *
	 */
	@RequiresPermissions(value={"GHYRJ:function:query"})
	@Action(value = "printList", results = { @Result(name = "list", location = "/WEB-INF/pages/register/daybalance/daybalanceprint.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String printList() {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getId();
		//计算日结详细信息
		List<RegisterBalancedetail> balancedetailList = daybalanceService.getBalance(daybalance,userId);
		request.setAttribute("balancedetailList", balancedetailList);
		Gson gson = new GsonBuilder()
		.registerTypeAdapterFactory(HibernateCascade.FACTORY)
		.create();
		String detailJson = gson.toJson(balancedetailList);
		request.setAttribute("detailJson", detailJson);
		//获得支付类型
		Map<String, String> psyTypeMap = getPayTypeMap();
		request.setAttribute("psyTypeMap", psyTypeMap);
		//计算日结信息
		RegisterDaybalance registerDaybalance = daybalanceService.getDaybalance(daybalance,userId);
		request.setAttribute("registerDaybalance", registerDaybalance); 
		//获得开始时间(上一次的结算时间 ,如果没有返回当天的0点)
		Date startTime = daybalanceService.getStartTime();
		request.setAttribute("startTime", startTime); 
		//结束时间为当前时间
		request.setAttribute("endTime", new Date());
		return "list";
	}
	
	
	/**  
	 *  
	 * @Description：门诊挂号员日结单
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-21  下午1:14 
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-21  下午1:14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "iReportToInspectionDailySettlement")
	public void iReportToInspectionDailySettlement() throws Exception {
		try {
			String fileName = request.getParameter("fileName");//jasper文件名称 不含后缀
			String startTime= request.getParameter("startTime");//jasper文件所用到的参数starttime
			String endTime= request.getParameter("endTime");//jasper文件所用到的参数 endtime
			User user = (User) SessionUtils.getCurrentUserFromShiroSession();
			String empName=user.getName();//jasper文件所用到的参数 员工姓名
			//By GH  数据变更， 取账号名关联员工表
//			String eid=user.getId();
			String eid=user.getAccount();
			String rid=request.getParameter("rid");//jasper文件所用到的参数 该记录主键id
			String webPath="WEB-INF"+File.separator+"reportFormat"+File.separator+"jasper"+File.separator;
			String root_path = request.getSession().getServletContext().getRealPath("/");
			root_path = root_path.replace('\\', '/');
			String reportFilePath = root_path + webPath+fileName+".jasper";
			//设置数据 将报表所用到的参数存入至HashMap中（不能使用其他Map类型）
			HashMap<String,Object> parameterMap=new HashMap<String,Object>();
			//javaBean数据封装（注：数据源可参考该示例各自进行创建）
		    JRDataSource dataSource = this.inspectionDailySettlement(startTime,endTime);
		    Map<String, Object> parameters = new HashMap<String, Object>();
		    parameters.put("SUBREPORT_DIR", root_path + webPath);
		    parameters.put("STARTTIMEE", startTime);
		    parameters.put("ENDTIMEE", endTime);
		    parameters.put("USERNAME", empName);
		    parameters.put("FS", "1@现金,2@银联卡,3@支票,4@院内账户");
		    iReportService.doReportToJavaBean(request,WebUtils.getResponse(),reportFilePath,parameters,dataSource);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**  
	 * @Description：门诊挂号员日结单使用javaBean封装
	 * @Author：zpty
	 * @CreateDate：2017-3-7   
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	private JRDataSource inspectionDailySettlement(String startTime,String endTime) {
		ArrayList<RegisterBalancedetailVo> iReportList = new ArrayList<RegisterBalancedetailVo>();
		RegisterBalancedetailVo registerBalancedetail = new RegisterBalancedetailVo();
		//父表数据封装
		registerBalancedetail.setStartTimeStr(startTime);
		registerBalancedetail.setEndTimeStr(endTime);
		//计算日结详细信息
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		RegisterDaybalance daybalance = new RegisterDaybalance();
		try{ //日期转换
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    Date start = sdf.parse(startTime);  
			    Date end = sdf.parse(endTime);  
				daybalance.setStartTime(start);
				daybalance.setEndTime(end);
			}  
		catch (ParseException e){  
			    System.out.println(e.getMessage());  
			} 
		//子表数据封装
		List<RegisterBalancedetail> balancedetailList = daybalanceService.getBalance(daybalance,userId);
		for(int i=0;i<balancedetailList.size();i++){
			if("CA".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("现金");
			}else if("AJ".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("转押金");
			}else if("CD".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("信用卡");
			}else if("CH".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("支票");
			}else if("DB".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("银联卡");
			}else if("HP".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("垫付款");
			}else if("PB".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("统筹");
			}else if("PO".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("汇票");
			}else if("PS".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("保险账户");
			}else if("TJ".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("体检患者优惠");
			}else if("YS".equals(balancedetailList.get(i).getPayType())){
				balancedetailList.get(i).setPayType("院内账户");
			}
		}
		//计算日结信息(总表)
		RegisterDaybalance registerDaybalance = daybalanceService.getDaybalanceNow(balancedetailList);
		/**添加字段（收入）**/
		registerBalancedetail.setIncome(registerDaybalance.getIncome());;
		/**添加字段（退费）**/
		registerBalancedetail.setRefund(registerDaybalance.getRefund());;
		/**添加字段（实际收入）**/
		registerBalancedetail.setActualIncome(registerDaybalance.getActualIncome());;
		/**添加字段（总挂号人数）**/
		registerBalancedetail.setInNum(registerDaybalance.getInNum());;
		/**添加字段（退号人数）**/
		registerBalancedetail.setOutNum(registerDaybalance.getOutNum());;
		/**添加字段（实际看诊人数）**/
		registerBalancedetail.setActualNum(registerDaybalance.getActualNum());
		registerBalancedetail.setRegBaldetList(balancedetailList);
		//对象放入list
		iReportList.add(registerBalancedetail);
		return new JRBeanCollectionDataSource(iReportList);
	}
	
	
	
}
