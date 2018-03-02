package cn.honry.statistics.bi.bistac.operationIncome.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.bi.bistac.operationIncome.service.OperationIncomeService;
import cn.honry.statistics.bi.bistac.operationIncome.vo.OperationIncomeVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/operationIncome")
public class OperationIncomeAction {
	@Resource
	private OperationIncomeService operationIncomeService;
	
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	
	/**
	 * 时间类别
	 */
	private String dateSign;
	/**
	 * 查询时间
	 */
	private String searchTime;
	/**
	 * 当前时间
	 */
	private String currDate;
	
	
	public String getCurrDate() {
		return currDate;
	}

	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}

	public String getDateSign() {
		return dateSign;
	}

	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}

	public String getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}

	/***
	 * @Description:手术收入统计
	 * @Description:
	 * @author:  zhangjin
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 */
	@Action(value="operactionIncomelist",results={ @Result(name = "list",location= "/WEB-INF/pages/stat/operation/operationIncome/operationIncome.jsp") },interceptorRefs={@InterceptorRef(value= "manageInterceptor") })
	public String operactionlist(){
		currDate=DateUtils.formatDateY_M_D(new Date());
		return "list";
	}
	
	/**  
	 * 
	 * 手术收入统计（门诊住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param response
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @throws:
	 * @return: void
	 *
	 */
	@Action(value="queryOperationNums")
	public void queryOperationNums(){
		if(StringUtils.isBlank(searchTime)){
			searchTime=new  SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
		}
		List<OperationIncomeVo> ListSum = operationIncomeService.queryOperationNums( searchTime,dateSign);
		Map<String,Object> map=new HashMap<String,Object>();
		double b=0;
		for(OperationIncomeVo vo :ListSum){
				if(null!=vo.getTotalAmount()){
					b=b+Double.valueOf(vo.getTotalAmount());
				}
		}
		map.put("count",b);
		map.put("sum", ListSum);
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 手术收入统计（手术类别）
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Action(value="queryOperationOpType")
	public void queryOperationOpType(){
		if(StringUtils.isBlank(searchTime)){
			searchTime=new  SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
		}
		List<OperationIncomeVo> list = operationIncomeService.queryOperationOpType( searchTime,dateSign);
		List<BusinessDictionary> list1=innerCodeService.getDictionary("operatetype");
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				for(int j=0;j<list1.size();j++){
					if(list1.get(j).getName().equals(list.get(i).getName())){
						list1.remove(list1.get(j));
					}
				}
			}
			for(int j=0;j<list1.size();j++){
				OperationIncomeVo vo=new OperationIncomeVo();
				vo.setName(list1.get(j).getName());
				vo.setTotalAmount("0");
				list.add(vo);
			}
		}
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 手术收入统计（科室前五）
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Action(value="queryOperationTopFiveDept")
	public void queryOperationTopFiveDept(){
		if(StringUtils.isBlank(searchTime)){
			searchTime=new  SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
		}
		List<OperationIncomeVo> list = operationIncomeService.queryOperationTOPFiveDept( searchTime,dateSign);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}

	/**
	 * 手术收入统计（医生前五）
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Action(value="queryOperationTopFiveDoc")
	public void queryOperationTopFiveDoc(){
		if(StringUtils.isBlank(searchTime)){
			searchTime=new  SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
		}
		List<OperationIncomeVo> list = operationIncomeService.queryOperationTOPFiveDoc( searchTime,dateSign);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询已完成手术收入（同比）
	 * @version: V1.0
	 * @author zxl
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	@Action(value="queryYoyCount")
	public void queryYoyCount(){
		if(StringUtils.isBlank(searchTime)&&"2".equals(dateSign) ){
			searchTime=DateUtils.formatDateY_M(new Date());
		}else if(StringUtils.isBlank(searchTime)&&"3".equals(dateSign)){
			searchTime=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.getCurrentTime(), -1));
		}
		List<OperationIncomeVo> list=operationIncomeService.queryYoyCount(searchTime,dateSign);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 * 
	 * 查询已完成手术例数（环比）
	 * @Author: zhuxiaolu
	 * @CreateDate: 2017年5月22日 下午8:28:24 
	 * @version: V1.0
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 *
	 */
	@Action(value="queryRatioCount")
	public void queryRatioCount(){
		if(StringUtils.isBlank(searchTime)&&"1".equals(dateSign) ){
			searchTime=DateUtils.formatDateY(new Date());
		}else if(StringUtils.isBlank(searchTime)&&"2".equals(dateSign) ){
			searchTime=DateUtils.formatDateY_M(new Date());
		}else if(StringUtils.isBlank(searchTime)&&"3".equals(dateSign)){
			searchTime=DateUtils.formatDateY_M_D(new Date());
		}
		List<OperationIncomeVo> list=operationIncomeService.queryRatioCount(searchTime,dateSign);
		String json=JSONUtils.toJson(list);
		WebUtils.webSendJSON(json);
	}
}
