package cn.honry.statistics.bi.bistac.inpatientIncome.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.bi.bistac.inpatientIncome.service.InpatientIncomeService;
import cn.honry.statistics.bi.bistac.inpatientIncome.vo.InpatientIncome;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
* @ClassName: 住院收入分析 
* @author yuke
* @date 2017年7月3日
*
*/
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/inpatientIncome")
@SuppressWarnings({"all"})
public class InpatientIncomeAction extends ActionSupport {
	
	private Logger logger=Logger.getLogger(InpatientIncomeAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	//定制百分比
	private	DecimalFormat nf = new DecimalFormat("0.00%");
	@Autowired
	@Qualifier(value = "inpatientIncomeService")
	private InpatientIncomeService inpatientIncomeService;
	private MongoBasicDao mbDao = null;
	private String Etime1;
	private String Etime2;
	private String time1;
	private String time2;
	
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	
	public String getEtime1() {
		return Etime1;
	}
	public void setEtime1(String etime1) {
		Etime1 = etime1;
	}
	public String getEtime2() {
		return Etime2;
	}
	public void setEtime2(String etime2) {
		Etime2 = etime2;
	}
	@Action(value = "inpatientIncomeList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/inpatientIncome.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String analyzeList() {
		Date date = new Date();
		Etime1 = DateUtils.formatDateY(date)+"-01";
		Etime2 = DateUtils.formatDateYM(date);
		return "list";
	}
	/**  
	 * 
	 * 住院收入分析
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月20日 上午11:10:02 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月20日 上午11:10:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryInpatientIncomeVo")
	public void queryInpatientIncomeVo(){
		try {
			//月住院总金额1
			List<String> supplyCosts1=new ArrayList<String>();
			//月住院总金额2
			List<String> supplyCosts2=new ArrayList<String>();
			//月住院总金额同比增长1
			List<String> tongbi1=new ArrayList<String>();
			//月住院总金额同比增长2
			List<String> tongbi2=new ArrayList<String>();
			//两个时间的下单科室
			List<String> deptNames = new ArrayList<String>();  
			List<InpatientIncome> inpatientIncomeVos =new ArrayList<InpatientIncome>();
			InpatientIncome income=null;
			Map<String,Object> strMap=new HashMap<String, Object>();
			List<OutpatientUseMedicVo> queryVoByMongo1 = null;
			List<OutpatientUseMedicVo> queryVoByMongo2 = null;
			Map<String, String> codeAndNameMap = null;
			OutpatientUseMedicVo voByMongo1 =null;
			OutpatientUseMedicVo voByMongo2 =null;
			mbDao = new MongoBasicDao();
			boolean collection = mbDao.isCollection("T_ZYSRTJ");//判断mongon中是否存在该表
			if (collection==false) {
				queryVoByMongo1 = inpatientIncomeService.queryOutpatientUseMedicVo(time1);
				queryVoByMongo2 = inpatientIncomeService.queryOutpatientUseMedicVo(time2);
				codeAndNameMap = inpatientIncomeService.querydeptCodeAndNameMap();
			}else{
				queryVoByMongo1 = inpatientIncomeService.queryVoByMongo(time1);
				queryVoByMongo2 = inpatientIncomeService.queryVoByMongo(time2);
			}
			for (OutpatientUseMedicVo vo1 : queryVoByMongo1) {
				if (collection==false) {
					voByMongo1 =inpatientIncomeService.queryOneOutpatientUseMedicVo(this.getLastYear(time1),codeAndNameMap.get(vo1.getRegDpcdName()));
				}else{
					voByMongo1 = inpatientIncomeService.queryOneVoByMongo(this.getLastYear(time1),vo1.getRegDpcdName());
				}
				Double LastCost1_1 = voByMongo1.getCost1();//上一年住院药品金额1
				Double LastCost1_2 = voByMongo1.getCost2();//上一年住院非药品金额1
				Double cost1_1 = vo1.getCost1();//住院药品金额1
				Double cost1_2 = vo1.getCost2();//住院非药品金额1
				deptNames.add(vo1.getRegDpcdName());
				income=new InpatientIncome();
				income.setDeptName(vo1.getRegDpcdName());
				income.setCost1(cost1_1);
				income.setNoCost1(cost1_2);
				income.setCosts1(cost1_1+cost1_2);
				if ((LastCost1_1+LastCost1_2)==0) {
					income.setTongbi1("0.00");
				}else{
					income.setTongbi1(NumberUtil.init().format((cost1_1+cost1_2-LastCost1_1-LastCost1_2)/(LastCost1_1+LastCost1_2)*100,2));
				}
				income.setCost2(0.00);
				income.setNoCost2(0.00);
				income.setCosts2(0.00);
				income.setTongbi2("0.00");
				income.setCost3(cost1_1);
				income.setNoCost3(cost1_2);
				income.setCosts3(cost1_1+cost1_2);
				if ((LastCost1_1+LastCost1_2)==0) {
					income.setTongbi3("0.00");
				}else{
					income.setTongbi3(NumberUtil.init().format((cost1_1+cost1_2-LastCost1_1-LastCost1_2)/(LastCost1_1+LastCost1_2)*100,2));
				}
				strMap.put(vo1.getRegDpcdName(), income);
			}
			for (OutpatientUseMedicVo vo2 : queryVoByMongo2) {
				if (collection==false) {
					voByMongo2 =inpatientIncomeService.queryOneOutpatientUseMedicVo(this.getLastYear(time2),codeAndNameMap.get(vo2.getRegDpcdName()));
				}else{
					voByMongo2 = inpatientIncomeService.queryOneVoByMongo(this.getLastYear(time2),vo2.getRegDpcdName());
				}
				Double LastCost2_1 = voByMongo2.getCost1();//上一年住院药品金额2
				Double LastCost2_2 = voByMongo2.getCost2();//上一年住院非药品金额2
				Double cost2_1 = vo2.getCost1();//住院药品金额2
				Double cost2_2 = vo2.getCost2();//住院非药品金额2
				
				boolean flag = strMap.containsKey(vo2.getRegDpcdName());
				if (flag==true) {
					if (collection==false) {
						voByMongo1 =inpatientIncomeService.queryOneOutpatientUseMedicVo(this.getLastYear(time1),codeAndNameMap.get(vo2.getRegDpcdName()));
					}else{
						voByMongo1 = inpatientIncomeService.queryOneVoByMongo(this.getLastYear(time1),vo2.getRegDpcdName());
					}
					InpatientIncome vo =(InpatientIncome)strMap.get(vo2.getRegDpcdName());
					vo.setCost2(cost2_1);
					vo.setNoCost2(cost2_2);
					vo.setCosts2(cost2_1+cost2_2);
					if ((LastCost2_1+LastCost2_2)==0) {
						vo.setTongbi2("0.00");
					}else{
						vo.setTongbi2(NumberUtil.init().format((cost2_1+cost2_2-LastCost2_1-LastCost2_2)/(LastCost2_1+LastCost2_2)*100,2));
					}
					vo.setCost3(cost2_1+vo.getCost3());
					vo.setNoCost3(cost2_2+vo.getNoCost3());
					vo.setCosts3(cost2_1+cost2_2+vo.getCosts3());
					if ((LastCost2_1+LastCost2_2+voByMongo1.getCost1()+voByMongo1.getCost2())==0) {
						vo.setTongbi3("0.00");
					}else{
						vo.setTongbi3(NumberUtil.init().format((cost2_1+cost2_2+vo.getCosts3()-LastCost2_1-LastCost2_2-voByMongo1.getCost1()-voByMongo1.getCost2())/(LastCost2_1+LastCost2_2+voByMongo1.getCost1()+voByMongo1.getCost2())*100,2));
					}
				}else{
					deptNames.add(vo2.getRegDpcdName());
					income=new InpatientIncome();
					income.setDeptName(vo2.getRegDpcdName());
					income.setCost1(0.00);
					income.setNoCost1(0.00);
					income.setCosts1(0.00);
					income.setTongbi1("0.00");
					income.setCost2(cost2_1);
					income.setNoCost2(cost2_2);
					income.setCosts2(cost2_1+cost2_2);
					if ((LastCost2_1+LastCost2_2)==0) {
						income.setTongbi2("0.00");
					}else{
						income.setTongbi2(NumberUtil.init().format((cost2_1+cost2_2-LastCost2_1+LastCost2_2)/(LastCost2_1+LastCost2_2)*100,2));
					}
					income.setCost3(cost2_1);
					income.setNoCost3(cost2_2);
					income.setCosts3(cost2_1+cost2_2);
					if ((LastCost2_1+LastCost2_2)==0) {
						income.setTongbi3("0.00");
					}else{
						income.setTongbi3(NumberUtil.init().format((cost2_1+cost2_2-LastCost2_1-LastCost2_2)/(LastCost2_1+LastCost2_2)*100,2));
					}
					strMap.put(vo2.getRegDpcdName(), income);
				}
				
			}
			for (String deptName : deptNames) {
				InpatientIncome vo =(InpatientIncome)strMap.get(deptName);
				supplyCosts1.add(NumberUtil.init().format(vo.getCosts1(),2));
				supplyCosts2.add(NumberUtil.init().format(vo.getCosts2(),2));
				tongbi1.add(vo.getTongbi1());
				tongbi2.add(vo.getTongbi2());
				inpatientIncomeVos.add(vo);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("inpatientIncomeVos", inpatientIncomeVos);
			map.put("supplyCosts1", supplyCosts1);
			map.put("supplyCosts2", supplyCosts2);
			map.put("tongbi1", tongbi1);
			map.put("tongbi2", tongbi2);
			map.put("deptNames", deptNames);
			String js= JSONUtils.toJson(map);
			WebUtils.webSendJSON(js);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("YZJC_ZYSRFX", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_ZYSRFX", "运营分析_住院收入分析", "2", "0"), e);
	
		}
	}
	/**  
	 * 
	 * 封装求上一年同月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastYear(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.YEAR, -1);
        Date d = calendar.getTime();
        //上年
        String lastDate = format.format(d);
		return lastDate;
	}
}
