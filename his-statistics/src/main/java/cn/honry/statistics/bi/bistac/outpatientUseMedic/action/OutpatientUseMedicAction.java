package cn.honry.statistics.bi.bistac.outpatientUseMedic.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.action.ListTotalIncomeStaticAction;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.service.OutpatientUseMedicService;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/outpatientUseMedic")
public class OutpatientUseMedicAction extends ActionSupport {
	private Logger logger=Logger.getLogger(ListTotalIncomeStaticAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	//定制百分比
	private	DecimalFormat nf = new DecimalFormat("0.00%");
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService  innerCodeService;
	@Autowired
	@Qualifier(value = "outpatientUseMedicService")
	private OutpatientUseMedicService outpatientUseMedicService;
	private HttpServletRequest request = ServletActionContext.getRequest();
	private SimpleDateFormat format;
	private String date;
	private String Etime;
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * 门诊用药监控
	 * @return
	 */
	@Action(value = "outpatientUseMediclist", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/outpatientUseMedic.jsp") }, 
			interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String outpatientUseMediclist() {
		Date date = new Date();
		Etime = DateUtils.formatDateYM(date);
		return "list";
	}
	/**  
	 * 
	 * 门诊药占比/环比
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月12日 下午5:33:37 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月12日 下午5:33:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryCost")
	public void queryCost(){
		//药占比
		Double costPer=0.0;
		//环比
		String lastCostPer=null;
		OutpatientUseMedicVo cost=new OutpatientUseMedicVo();
		OutpatientUseMedicVo lastCost = new OutpatientUseMedicVo();
		boolean collection = new MongoBasicDao().isCollection("MZYYJK_YZB_MONTH");//判断mongon中是否存在该表
		if (collection==false) {
			cost = outpatientUseMedicService.queryCost(date);
			lastCost = outpatientUseMedicService.queryCost(this.getLastMonth());
		}else{
			cost = outpatientUseMedicService.queryCostByMongo(date);
			lastCost = outpatientUseMedicService.queryCostByMongo(this.getLastMonth());
		}
		Double drugFee = cost.getDrugFee()==null?0.0:cost.getDrugFee();//月药品收入
		Double totCost=cost.getTotCost()==null?0.0:cost.getTotCost();//月总收入
		Double lastDrugFee = lastCost.getDrugFee()==null?0.0:lastCost.getDrugFee();//上月药品收入
		if (totCost==0.0) {
			costPer=0.0;
		}else{
			costPer=drugFee/totCost;
		}
		if (lastDrugFee==0.0) {
			lastCostPer="--";
		}else{
			Double dou=(drugFee-lastDrugFee)/lastDrugFee;
			lastCostPer=nf.format(dou);
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("costPer", costPer*100);
		map.put("lastCostPer", lastCostPer);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 最近12月的人均药费
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryStatisticsCost")
	public void queryStatisticsCost(){
		format = new SimpleDateFormat("yyyy-MM");
		String[] dateList = request.getParameterValues("dateList[]");
		List<String> avgTotCosts=new ArrayList<String>();//人均用药费用
		List<String> totCosts=new ArrayList<String>();//用药费用
		List<String> nums=new ArrayList<String>();//用药数量
		List<String> typeName=new ArrayList<String>();//药品名称
		
		String da = null;
		Map<String,List<String>> strMap=new HashMap<String, List<String>>();
		int i=0;
		List<Map> maps=new ArrayList<Map>();
		List<OutpatientUseMedicVo> vos = new ArrayList<OutpatientUseMedicVo>();
		List<OutpatientUseMedicVo> costByMongos = new ArrayList<OutpatientUseMedicVo>();
		boolean collection = new MongoBasicDao().isCollection("MZYYJK_YPJE_MONTH");//判断mongon中是否存在该表
		if (collection==false) {
			vos = outpatientUseMedicService.queryStatisticsCost(dateList[0],dateList[dateList.length-1]);
		}
		for (String sDate : dateList) {
			int total=0;//用药数量
			int num=0;//人次
			Double cost=0.0;//用药金额
			try {
				i++;
				da = format.format(format.parse(sDate));
				if (collection==false) {
					for (OutpatientUseMedicVo vo : vos) {
						if (vo.getSelectTime().equals(da)) {
							costByMongos.add(vo);
						}
					}
				}else{
					costByMongos = outpatientUseMedicService.queryStatisticsCostByMongo(da);
				}
				for (OutpatientUseMedicVo costByMongo : costByMongos) {
					cost+=(costByMongo.getTotCost()==null?0.0:costByMongo.getTotCost());
					total+=(costByMongo.getNum()==null?0.0:costByMongo.getNum());
					num+=(costByMongo.getTotal()==null?0:costByMongo.getTotal());
					boolean flag = strMap.containsKey(costByMongo.getType());
					if (flag==true ) {
						List<String> list = strMap.get(costByMongo.getType());
						list.set(i-1,NumberUtil.init().format(costByMongo.getNum()==null?0.0:costByMongo.getNum(),0));
						strMap.put(costByMongo.getType(), list);
						
					}else{
						List<String> list=new ArrayList<String>();
						for (int j = 0; j < 12; j++) {
							list.add("0");
						}
						list.set(i-1,NumberUtil.init().format(costByMongo.getNum()==null?0.0:costByMongo.getNum(),0));
						strMap.put(costByMongo.getType(), list);
					}
				}
				if (costByMongos==null || costByMongos.size()==0) {
					totCosts.add("0");
					nums.add("0");
					avgTotCosts.add("0");
				}else{
					totCosts.add( NumberUtil.init().format(cost/10000,2));
					nums.add( NumberUtil.init().format(total,0));
					if (num==0) {
						avgTotCosts.add("0");
					}else{
						avgTotCosts.add(NumberUtil.init().format(cost/num,2));
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				WebUtils.webSendJSON("error");
				logger.error("YZJC_MZYYJK", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MZYYJK", "运营分析_门诊用药监控", "2", "0"), e);
			}
		}
		strMap.put("总药费", totCosts);
		strMap.put("总用药数量", nums);
		maps.add(strMap);
		for (Map map : maps) {
			Set set = map.entrySet();
			Iterator it = set.iterator(); 
			while(it.hasNext()){      
			     Map.Entry<String, String> entry1=(Map.Entry<String, String>)it.next();    
			     typeName.add(entry1.getKey());    
			}
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("avgTotCosts", avgTotCosts);
		map.put("typeName", typeName);
		map.put("maps", maps);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 最近12月的人均要用药天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月17日 上午9:57:27 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月17日 上午9:57:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryMedicationDays")
	public void queryMedicationDays(){
		format = new SimpleDateFormat("yyyy-MM");
		String[] dateList = request.getParameterValues("dateList[]");
		List<String> avgDays=new ArrayList<String>();
		String mDays = null;
		String daNew = null;
		List<OutpatientUseMedicVo> queryMedicationDays = new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo =new OutpatientUseMedicVo();
		boolean collection = new MongoBasicDao().isCollection("MZYYJK_YYTS_MONTH");//判断mongon中是否存在该表
		if (collection==false) {
			queryMedicationDays = outpatientUseMedicService.queryMedicationDays(dateList[0],dateList[dateList.length-1]);
		}
		for (String strDate : dateList) {
			String da;
			try {
				da = format.format(format.parse(strDate));
				daNew = format.format(format.parse(date));
				if (collection==false) {
					for (OutpatientUseMedicVo ovo : queryMedicationDays) {
						if (StringUtils.isNoneBlank(da) && da.equals(ovo.getSelectTime())) {
							vo.setAvgDays(ovo.getAvgDays()==null?0.0:ovo.getAvgDays());
						}
					}
				}else{
					vo = outpatientUseMedicService.queryMedicationDaysByMongo(da);
				}
				avgDays.add(NumberUtil.init().format(vo.getAvgDays()==null?0.0:vo.getAvgDays(),0));
				if (da.equals(daNew)) {
					mDays = NumberUtil.init().format(vo.getAvgDays()==null?0.0:vo.getAvgDays(),0);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				WebUtils.webSendJSON("error");
				logger.error("YZJC_MZYYJK", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("YZJC_MZYYJK", "运营分析_门诊用药监控", "2", "0"), e);
			}
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("avgDays", avgDays);
		map.put("mDays", mDays);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 医生用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryDoctCost")
	public void queryDoctCost(){
		List<String> doctNameList=new ArrayList<String>();
		List<String> costList=new ArrayList<String>();
		List<String> numList=new ArrayList<String>();
		List<OutpatientUseMedicVo> doctCostByMongo = new ArrayList<OutpatientUseMedicVo>();
		boolean collection = new MongoBasicDao().isCollection("MZYYJK_YSYYJE_MONTH");//判断mongon中是否存在该表
		if (collection==false) {
			doctCostByMongo = outpatientUseMedicService.queryDoctCost(date);
		}else{
			doctCostByMongo = outpatientUseMedicService.queryDoctCostByMongo(date);
		}
		for (OutpatientUseMedicVo vo : doctCostByMongo) {
			doctNameList.add(vo.getDoctCodeName());
			costList.add(NumberUtil.init().format((vo.getTotCost()==null?0.0:vo.getTotCost())/10000,4));
			numList.add(NumberUtil.init().format(vo.getNum()==null?0.0:vo.getNum(),0));
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("doctNameList", doctNameList);
		map.put("doctCostList", costList);
		map.put("doctNumList", numList);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * 
	 * 科室用药前5名
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月13日 下午4:41:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月13日 下午4:41:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Action(value="queryDeptCost")
	public void queryDeptCost(){
		List<String> deptNameList=new ArrayList<String>();
		List<String> costList=new ArrayList<String>();
		List<String> numList=new ArrayList<String>();
		List<OutpatientUseMedicVo> deptCostByMongo = new ArrayList<OutpatientUseMedicVo>();
		boolean collection = new MongoBasicDao().isCollection("MZYYJK_KSYYJE_MONTH");//判断mongon中是否存在该表
		if (collection==false) {
			deptCostByMongo = outpatientUseMedicService.queryDeptCost(date);
		}else{
			deptCostByMongo = outpatientUseMedicService.queryDeptCostByMongo(date);
		}
		for (OutpatientUseMedicVo vo : deptCostByMongo) {
			deptNameList.add(vo.getRegDpcdName());
			costList.add(NumberUtil.init().format((vo.getTotCost()==null?0.0:vo.getTotCost())/10000,4));
			numList.add(NumberUtil.init().format(vo.getNum()==null?0.0:vo.getNum(),0));
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deptNameList", deptNameList);
		map.put("deptCostList", costList);
		map.put("deptNumList", numList);
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
/******************************************************************************************************/
	/**  
	 * 
	 * 封装求上一月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastMonth(){
		format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.MONTH, -1);
        Date dd = calendar.getTime();
        //上月
        String lastM = format.format(dd);
		return lastM;
	}
}

