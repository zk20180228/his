package cn.honry.statistics.doctor.registerInfoGzltj.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;







import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.deptWorkCount.vo.DeptWorkCountVo;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoGzltjDao;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoStatDao;
import cn.honry.statistics.doctor.registerInfoGzltj.service.RegisterInfoGzltjService;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.DoctorVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.PieNameAndValue;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsjdbcDao;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

@Service("registerInfoGzltjService")
@Transactional
public class RegisterInfoGzltjServiceImpl implements RegisterInfoGzltjService{
	
	private Logger logger = Logger.getLogger(RegisterInfoGzltjServiceImpl.class);
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "registerInfoGzltjDAO")
	private RegisterInfoGzltjDao registerInfoGzltjDAO;
	@Autowired
	@Qualifier(value = "registerInfoStatDao")
	private RegisterInfoStatDao registerInfoStatDao;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	

	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	/**zxk **/
	@Resource(name = "reportFormsjdbcDao")
	private ReportFormsjdbcDao reportFormsjdbcDao;
	
	@Resource(name = "client")
	private Client client;

	@Value("${es.register_main.index}")
	private String register_main_index;

	@Value("${es.register_main.type}")
	private String register_main_type;
	
	/**  
	 *  
	 * @Description： 查询工作量列表
	 * @param 
	 * @Author：zpty
	 * @CreateDate：2015-8-27  
	 * @ModifyRmk：  
	 * @version 1.0
	 @Author：wujiao
	 * @CreateDate：2016-4-27  
	 * @ModifyRmk：  
	 * @version 2.0
	 * @throws ParseException 
	 */
	@Override
	public List<RegisterInfoGzltjVo> findInfo(String Stime,String Etime,String dept,String expxrt) throws ParseException {
			List<RegisterInfoGzltjVo> list=registerInfoGzltjDAO.findInfo(Stime,Etime,dept,expxrt);
			List<RegisterInfoGzltjVo> listNew=new ArrayList<>();
			Map<Integer, String> map=new HashMap<Integer, String>();
			String strSeparator = "-"; //日期分隔符
			String[] oDate1 = Stime.split(strSeparator);
			String[]  oDate2= Etime.split(strSeparator);
			String strDateS = oDate1[0]+ oDate1[1]+oDate1[2];
			String strDateE = oDate2[0]+oDate2[1]+oDate2[2];
			Integer Sdate=Integer.parseInt(strDateS);//开始时间转int类型
			Integer Edate=Integer.parseInt(strDateE);//结束时间转int类型
			Integer iDays = Edate - Sdate;//把相差的毫秒数转换为天数
			String time =Stime;
			List<Date> weekList=new ArrayList<>();
			SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
				//统计所有的日期
			for (int i = 0; i <=iDays; i++) { 
					Date date = sim.parse(time);
					if (i == 0) {
						weekList.add(date);
					} else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.add(calendar.DATE, 1);// +1time的时间加一天
						date = calendar.getTime();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						time = formatter.format(date);
						weekList.add(date);
					}
			}
					//遍历有的日期
			for (Date date : weekList) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
					time = formatter.format(date);
					Integer week=DateUtils.getWeekOfDay(time);
					if (map.get(week) == null) {
						String s = time;
						map.put(week, s);
					} else {
						String s = map.get(week) + "," + time;
						map.put(week, s);
					}
			}
			
			//遍历vo
			for (RegisterInfoGzltjVo vo : list) {
					 List<RegisterInfo> listInfo=new ArrayList<>();
					 List<RegisterInfoGzltjVo> listsum=registerInfoGzltjDAO.findPrereSum(map,vo.getDeptId(),vo.getExpxrtId());
					 listInfo=registerInfoGzltjDAO.findPrereInfo(time,vo.getDeptId(),vo.getExpxrtId());
					if (listInfo.size() > 0) {
						 vo.setMonCost(listsum.get(0).getMonNum()*listInfo.get(0).getFee());
						 vo.setMonNum(listsum.get(0).getMonNum());
						 vo.setTueCost(listsum.get(0).getTueNum()*listInfo.get(0).getFee());
						 vo.setTueNum(listsum.get(0).getTueNum());
						 vo.setWedCost(listsum.get(0).getWedNum()*listInfo.get(0).getFee());
						 vo.setWedNum(listsum.get(0).getWedNum());
						 vo.setThuCost(listsum.get(0).getThuNum()*listInfo.get(0).getFee());
						 vo.setThuNum(listsum.get(0).getThuNum());
						 vo.setFriCost(listsum.get(0).getFriNum()*listInfo.get(0).getFee());
						 vo.setFriNum(listsum.get(0).getFriNum());
						 vo.setSatCost(listsum.get(0).getSatNum()*listInfo.get(0).getFee());
						 vo.setSatNum(listsum.get(0).getSatNum());
						 vo.setSunCost(listsum.get(0).getSunNum()*listInfo.get(0).getFee());
						 vo.setSunNum(listsum.get(0).getSunNum());
						 vo.setNum(listsum.get(0).getMonNum()+listsum.get(0).getTueNum()+listsum.get(0).getWedNum()
								  +listsum.get(0).getThuNum()+listsum.get(0).getFriNum()+listsum.get(0).getSatNum()
								  +listsum.get(0).getSunNum());
						 vo.setCost((listsum.get(0).getMonNum()+listsum.get(0).getTueNum()+listsum.get(0).getWedNum()
								  +listsum.get(0).getThuNum()+listsum.get(0).getFriNum()+listsum.get(0).getSatNum()
								  +listsum.get(0).getSunNum())*listInfo.get(0).getFee());
						 listNew.add(vo);
					 }
				 }
			
				 return listNew;
	}
	

	/**  
	 *  
	 * @Description：  修改
	 * @param id
	 * @Author：liudelin
	 * @CreateDate：2015-6-24 下午05:45:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void registerTriageSave(String id, String expxrt) {
		
		registerInfoGzltjDAO.registerTriageSave(id,expxrt);
	}
	
	/**  
	 *  
	 * @Description： 查询
	 * @Author：liudelin
	 * @CreateDate：2015-6-25 下午01:44:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo queryRegisterTiragegl(String sEncode) {
		
		return registerInfoGzltjDAO.queryRegisterTiragegl(sEncode);
	}

	/**  
	 * 
	 * <p> 挂号医生工作量统计 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:stime开始时间etime结束时间dept科室编码expxrt医生编码
	 *
	 */
	@Override
	public List<RegisterInfoGzltjVo> statRegDorWorkload(String stime,String etime,String dept,String expxrt) throws Exception{
		List<RegisterInfoGzltjVo> voList = null;
		try{
			
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
			
			//2.获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			
			//获取当前表最大时间及最小时间
			List<String> tnL = new ArrayList<String>();
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					tnL.add(0,"T_REGISTER_MAIN_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				
				tnL.add("T_REGISTER_MAIN_NOW");
			}
			voList = registerInfoGzltjDAO.statRegDorWorkloadFindDept(tnL,stime,etime,dept,expxrt);
			if(voList.size()>0){
				for(RegisterInfoGzltjVo vo : voList){
					vo = registerInfoGzltjDAO.statRegDorWorkloadFindInfo(tnL,stime,etime,vo);
				}
			}
		}catch(Exception e){
			voList = new ArrayList<RegisterInfoGzltjVo>();
			throw new RuntimeException(e);
		}
		
		return voList;
	}
	
	@Override
	public Map<String,Object> statRegDorWork(String stime,String etime,String dept,String expxrt,String page,String rows,String menuAlias)throws Exception {
		List<RegisterInfoGzltjVo> voList = null;
		List<String> tnL = new ArrayList<String>();
		try{
			
			//转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
			
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					tnL.add(0,"T_REGISTER_MAIN_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_REGISTER_MAIN_NOW");
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		if(StringUtils.isBlank(dept)||StringUtils.isBlank(expxrt)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			if(deptList == null || deptList.size() == 0){
				dept = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			}else{
				dept = "s";
				for(int i = 0;i<deptList.size();i++){
					dept += ","+deptList.get(i).getDeptCode();
				}
			}
		}
		
		voList = registerInfoStatDao.statRegDorWork(tnL,stime,etime,dept,expxrt,page,rows);
		String redKey = "GHYSGZLTJ"+stime+"_"+etime+"_"+dept+"_"+expxrt;
		redKey=redKey.replaceAll(",", "-");
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			totalNum = registerInfoStatDao.getTotal(tnL, stime, etime, dept, expxrt);
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(val!=null&&!"".equals(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", voList);
		map.put("total", totalNum);
		
		return map;
	}


	@Override
	public List<DoctorVo> getDoctorBydeptCodes(String deptCodes) {
		return registerInfoStatDao.getDoctorBydeptCodes(deptCodes);
	}

	@Override
	public List<MenuListVO> getDoctorList(String deptTypes,String menuAlias) {
		return registerInfoStatDao.getDoctorList(deptTypes,menuAlias);
	}
	
	@Override
	public Map<String, Object> statRegDorWorkByMongo(String stime,
	String etime, String dept, String expxrt, String page, String rows,String menuAlias) throws Exception {
		
		Map<String, Object> map = registerInfoStatDao.statRegDorWorkByMongo(stime,etime,dept,expxrt,page,rows,menuAlias);
	
		return map;
	}
	
	/**
	 * 挂号医生工作量统计 elasticsearch实现
	 * @Author: 朱振坤
	 * @param stime 查询开始时间 以“reg_date”为查询字段
	 * @param etime 查询结束时间 不包括当日，因为前台传来的结束日期已经加过一天，所有这里不再加一天，后期可能修改
	 * @param dept 科室id字符串，多个id以“,”隔开
	 * @param expxrt 医生id字符串，多个id以“,”隔开
	 * @param page easyUi分页参数
	 * @param rows easyUi分页参数
	 * @param menuAlias 权限别名
	 * @return 封装easyUi表格的json数据的集合
	 */
	@Override
	public Map<String, Object> statRegDorWorkByES(String stime, String etime,
	String dept, String expxrt, String page, String rows,String menuAlias) {
		List<RegisterInfoGzltjVo> list = new ArrayList<RegisterInfoGzltjVo>();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("trans_type", 1))//交易类型,1正交易，2反交易
				.filter(QueryBuilders.termQuery("in_state", 0))//状态[0正常，1换科，2退号，3退费]
				.filter(QueryBuilders.termQuery("valid_flag", 1))//0退费,1有效,2作废
				.filter(QueryBuilders.rangeQuery("reg_date")
						.gte(DateUtils.parseDateY_M_D(stime))
						.lt(DateUtils.parseDateY_M_D(etime)));
		if(StringUtils.isBlank(dept)&&StringUtils.isBlank(expxrt)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			if(deptList == null || deptList.size() == 0){
				expxrt = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			}else{
				dept = "s";
				for(int i = 0;i<deptList.size();i++){
					dept += ","+deptList.get(i).getDeptCode();
				}
			}
		}
		
		if (StringUtils.isNotBlank(dept)&&!"all".equals(dept)) {
			String[] depts = dept.split(",");
			boolQuery.filter(QueryBuilders.termsQuery("dept_code", depts));
		} 
		if (StringUtils.isNotBlank(expxrt)&&!"all".equals(expxrt)) {
			String[] expxrts = expxrt.split(",");
			boolQuery.filter(QueryBuilders.termsQuery("doct_code", expxrts));
		}
		SearchResponse searchResponse = client.prepareSearch(register_main_index).setTypes(register_main_type)
				.setQuery(boolQuery)
				.addAggregation(AggregationBuilders.terms("deptCode").field("dept_code").size(0)
						.subAggregation(AggregationBuilders.terms("doctCode").field("doct_code").size(0)
								.subAggregation(AggregationBuilders.sum("sumCostByDoct").field("sum_cost"))
								.subAggregation(AggregationBuilders.terms("dayOfWeek").field("day_of_week").size(0)
										.subAggregation(AggregationBuilders.sum("sumCostByDay").field("sum_cost")))))//总费用
										.setSize(0).execute().actionGet();
		
		logger.info("门诊统计分析_挂号医生工作量统计查询用时："+searchResponse.getTookInMillis()+"ms,  分片个数："+searchResponse.getTotalShards());
		Terms deptCodes = searchResponse.getAggregations().get("deptCode");
		for (Terms.Bucket deptCode : deptCodes.getBuckets()) {
			Terms docts = deptCode.getAggregations().get("doctCode");
			for (Terms.Bucket doct : docts.getBuckets()) {
				RegisterInfoGzltjVo vo = new RegisterInfoGzltjVo();
				vo.setDept(deptCode.getKeyAsString());
				vo.setExpxrt(doct.getKeyAsString());
				Sum sumCostByDoct = doct.getAggregations().get("sumCostByDoct");
				double totCostByDoc = sumCostByDoct.getValue();
				vo.setNum((int)doct.getDocCount());
				vo.setCost(totCostByDoc);
				Terms daysOfWeek = doct.getAggregations().get("dayOfWeek");
				for (Terms.Bucket dayOfWeek : daysOfWeek.getBuckets()) {
					Sum sumCostByDay = dayOfWeek.getAggregations().get("sumCostByDay");
					double totCostByDay = sumCostByDay.getValue();
					this.setDayCost(vo, (int)dayOfWeek.getDocCount(), totCostByDay, dayOfWeek.getKeyAsString());
				}
				list.add(vo);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(page) && StringUtils.isNotBlank(rows)) {
			List<RegisterInfoGzltjVo> pageList = new ArrayList<RegisterInfoGzltjVo>();
			for (int i = (Integer.valueOf(page)-1) * Integer.valueOf(rows); i < list.size() && i < Integer.valueOf(page) * Integer.valueOf(rows); i++) {
				pageList.add(list.get(i));
			}
			map.put("rows", pageList);
		} else {
			map.put("rows", list);
		}
		map.put("total", list.size());
		return map;
	}
	
	private void setDayCost(RegisterInfoGzltjVo vo, int count, double totCost,
			String dayOfWeek) {
		switch (dayOfWeek) {
		case "1":// 周日
			vo.setSunNum(count);
			vo.setSunCost(totCost);
			break;
		case "2":// 周一
			vo.setMonNum(count);
			vo.setMonCost(totCost);
			break;
		case "3":// 周二
			vo.setTueNum(count);
			vo.setTueCost(totCost);
			break;
		case "4":// 周三
			vo.setWedNum(count);
			vo.setWedCost(totCost);
			break;
		case "5":// 周四
			vo.setThuNum(count);
			vo.setThuCost(totCost);
			break;
		case "6":// 周五
			vo.setFriNum(count);
			vo.setFriCost(totCost);
			break;
		case "7":// 周六
			vo.setSatNum(count);
			vo.setSatCost(totCost);
			break;
		}
	}

	@Override
	public Map<String,Object> getRegisterDeptInfo(String stime, String etime,
	String deptCodes,String page,String rows) throws Exception {
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):50;
		Map<String,Object> returnmap = new HashMap<String, Object>();
		List<RegisterInfoVo> list= new ArrayList<RegisterInfoVo>();
		List<RegisterInfoVo> resultlist= new ArrayList<RegisterInfoVo>();
		Map<String, List<String>> map = ResultUtils.getDate(stime, etime);
		List<String> day = map.get("day");
		List<String> month = map.get("month");
		List<String> year = map.get("year");
		List<String> deptCode = new ArrayList<String>();
		String[] split = deptCodes.split(",");
		deptCode.addAll(Arrays.asList(split));
		if(day!=null&&day.size()>0){
			List<RegisterInfoVo> daylist = registerInfoStatDao.findRegisterDeptInfo("GHKSGZLTJ_DAY", day, deptCode);
			if(daylist!=null&&daylist.size()>0){
				list.addAll(daylist);
			}
		}
		if(month!=null&&month.size()>0){
			List<RegisterInfoVo> monthList = registerInfoStatDao.findRegisterDeptInfo("GHKSGZLTJ_MONTH", month, deptCode);
			if(monthList!=null&&monthList.size()>0){
				list.addAll(monthList);
			}
		}
		if(year!=null&&year.size()>0){
			List<RegisterInfoVo> yearlist = registerInfoStatDao.findRegisterDeptInfo("GHKSGZLTJ_YEAR", year, deptCode);
			if(yearlist!=null&&yearlist.size()>0){
				list.addAll(yearlist);
			}
		}
		Map<String, RegisterInfoVo> maplist = new HashMap<String, RegisterInfoVo>();
		for (RegisterInfoVo vo : list) {
			if(maplist.containsKey(vo.getDeptCode())){
				RegisterInfoVo infoVo = maplist.get(vo.getDeptCode());
				infoVo.setCost(infoVo.getCost()+vo.getCost());
				infoVo.setFriCost(infoVo.getFriCost()+vo.getFriCost());
				infoVo.setFriNum(infoVo.getFriNum()+vo.getFriNum());
				infoVo.setMonCost(infoVo.getMonCost()+vo.getMonCost());
				infoVo.setMonNum(infoVo.getMonNum()+vo.getMonNum());
				infoVo.setNum(infoVo.getNum()+vo.getNum());
				infoVo.setSatCost(infoVo.getSatCost()+vo.getSatCost());
				infoVo.setSatNum(infoVo.getSatNum()+vo.getSatNum());
				infoVo.setSunCost(infoVo.getSunCost()+vo.getSunCost());
				infoVo.setSunNum(infoVo.getSunNum()+vo.getSunNum());
				infoVo.setThuCost(infoVo.getThuCost()+vo.getThuCost());
				infoVo.setThuNum(infoVo.getThuNum()+vo.getThuNum());
				infoVo.setTueCost(infoVo.getTueCost()+vo.getTueCost());
				infoVo.setTueNum(infoVo.getTueNum()+vo.getTueNum());
				infoVo.setWedCost(infoVo.getWedCost()+vo.getWedCost());
				infoVo.setWedNum(infoVo.getWedNum()+vo.getWedNum());
				maplist.put(vo.getDeptCode(), infoVo);
			}else{
				maplist.put(vo.getDeptCode(), vo);
			}
		}
		for (Entry<String,RegisterInfoVo> m : maplist.entrySet()) {
			resultlist.add(m.getValue());
		}
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new BeanComparator("deptCode"), false);
		Collections.sort(resultlist, chain);
		List<RegisterInfoVo> returnList = new ArrayList<RegisterInfoVo>();
		int end = p*r>resultlist.size()?resultlist.size():p*r;
		for (int i = (p-1)*r; i < end; i++) {
			returnList.add(resultlist.get(i));
		}
		
		returnmap.put("total", resultlist.size());
		returnmap.put("rows", returnList);
		
		return returnmap;
	}


	@Override
	public String queryRegisterCharts(String searchTime, String dateSign) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		map=this.queryRegisterMomYoy(searchTime, dateSign);
		if(!"4".equals(dateSign)){
			List<PieNameAndValue> huanbi=new ArrayList<PieNameAndValue>();
			List<PieNameAndValue> tongbi=new ArrayList<PieNameAndValue>();
			String sendSearchTimeM = "";
			String sendSearchTimeY = "";
				for(int i = 5;i>=0;i--){
					switch (dateSign) {
					case "3":
						Date y = DateUtils.parseDateY(searchTime);
						Date searchDateY = DateUtils.addYear(y, -i);
						sendSearchTimeM = DateUtils.formatDateY(searchDateY);
						break;
					case "2":
						Date m = DateUtils.parseDateY_M(searchTime);
						Date searchDateMm = DateUtils.addMonth(m, -i);
						Date searchDateMy = DateUtils.addYear(m, -i);
						sendSearchTimeM = DateUtils.formatDateY_M(searchDateMm);
						sendSearchTimeY = DateUtils.formatDateY_M(searchDateMy);
						break;
					case "1":
						Date d = DateUtils.parseDateY_M_D(searchTime);
						Date srearchDateDm = DateUtils.addDay(d, -i);
						Date srearchDateDy = DateUtils.addYear(d, -i);
						sendSearchTimeM = DateUtils.formatDateY_M_D(srearchDateDm);
						sendSearchTimeY = DateUtils.formatDateY_M_D(srearchDateDy);
						break;
		
					default:
						break;
					}
					PieNameAndValue huanbivo=new PieNameAndValue();
					PieNameAndValue tongbiVo=new PieNameAndValue();
					Integer mom= queryDate(sendSearchTimeM,dateSign);
					huanbivo.setValue(mom);
					huanbivo.setName(sendSearchTimeM);
					huanbi.add(huanbivo);
					if(StringUtils.isNotBlank(sendSearchTimeY)){
						Integer yoy=queryDate(sendSearchTimeY,dateSign);
						tongbiVo.setName(sendSearchTimeY);
						tongbiVo.setValue(yoy);
						tongbi.add(tongbiVo);
					}
				}
				 map.put("Huanbi", huanbi);
			     map.put("Tongbi", tongbi);
		}
		return JSONUtils.toJson(map);
	}
	
	
	/**
	 * 获取挂号量同环比
	 * @throws Exception 
	 */
	public Map<String,Object> queryRegisterMomYoy(String searchTime,String dateSign) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		
		List<PieNameAndValue> fee=new ArrayList<>();
		List<PieNameAndValue> area=new ArrayList<>();
		//获取同环比
		if("4".equals(dateSign)){
			return totalNumber(searchTime,dateSign);
		}
		fee=registerCountByType(searchTime,dateSign);
		area=queryLevelGHGZL(searchTime,dateSign);
        map.put("fee", fee);
        map.put("area", area);
        Integer total=0;
        if(area!=null){
           total=area.get(area.size()-1).getTotal();
        }
        map.put("total",total);
		return map;
	}
	
	
	private Map<String,Object> totalNumber(String date,String dateSign) throws Exception{
		
		Map<String,Object> map=new HashMap<String,Object>();
		if (StringUtils.isBlank(date)) {
            return map;
        }
		
        String[] dates = date.split(",");
        Map<String, List<String>> dateMap = ResultUtils.getDate(dates[0], dates[1]);
        List<String> yearList = dateMap.get("year");//按年统计的list
        List<Map<String,Object>> huizong=new ArrayList<Map<String,Object>>();
        if (yearList.size() > 0) {
            for (String dateY : yearList) {
            	Map<String,Object> mapVo=new HashMap<String,Object>();
            	mapVo= queryRegisterMomYoy(dateY, "3");
                if (mapVo.size()>0) {
                	huizong.add(mapVo);
                }
            }
        }
        List<String> monthList = dateMap.get("month");//按月统计的list
        if (monthList.size() > 0) {
            for (String dateM : monthList) {
            	Map<String,Object> mapVo=new HashMap<String,Object>();
            	mapVo= queryRegisterMomYoy(dateM, "2");
                if (mapVo.size()>0) {
                	huizong.add(mapVo);
                }
            }
        }
        List<String> dayList = dateMap.get("day");//按日统计的list
        if (dayList.size() > 0) {
            for (String dateD : dayList) {
            	Map<String,Object> mapVo=new HashMap<String,Object>();
            	mapVo= queryRegisterMomYoy(dateD, "1");
                if (mapVo.size()>0) {
                	huizong.add(mapVo);
                }
            }
        }
        Integer total=0;
        Map<String,Integer> feeMap=new LinkedHashMap<String,Integer>();
        Map<String,Integer> areaMap=new LinkedHashMap<String,Integer>();
        for(Map<String,Object> mapVo:huizong){
        	List<PieNameAndValue> list=(List<PieNameAndValue>) mapVo.get("fee");
        	if(list!=null&&list.size()>0){
        		for(PieNameAndValue vo:list){
        			String key=vo.getName();
        			if(StringUtils.isNotBlank(key)&&vo.getValue()!=null){
        				if(feeMap.containsKey(key)){
            				Integer tempValue=feeMap.get(key);
            				tempValue+=vo.getValue();
            				feeMap.put(key, tempValue);
            			}else{
            				feeMap.put(key, vo.getValue());
            			}
        			}
        		}
        	}
        	@SuppressWarnings("unchecked")
			List<PieNameAndValue> list1=(List<PieNameAndValue>) mapVo.get("area");
        	if(list1!=null&&list1.size()>0){
        		for(PieNameAndValue vo:list1){
        			String key=vo.getName();
        			if(StringUtils.isNotBlank(key)&&vo.getValue()!=null){
        				if(areaMap.containsKey(key)){
            				Integer tempValue=areaMap.get(key);
            				tempValue+=vo.getValue();
            				areaMap.put(key, tempValue);
            				
            			}else{
            				areaMap.put(key, vo.getValue());
            			}
        			}
        		}
        	}
        	total+=(Integer)mapVo.get("total");
        }
        List<PieNameAndValue> feeList=new ArrayList<PieNameAndValue>();
        for(String key:feeMap.keySet()){
        	PieNameAndValue vo=new PieNameAndValue();
        	vo.setName(key);
        	vo.setValue(feeMap.get(key));
        	feeList.add(vo);
        }
        List<PieNameAndValue> areaList=new ArrayList<PieNameAndValue>();
        for(String key:areaMap.keySet()){
        	PieNameAndValue vo=new PieNameAndValue();
        	vo.setName(key);
        	vo.setValue(areaMap.get(key));
        	areaList.add(vo);
        }
        map.put("fee", feeList);
        map.put("area", areaList);
        map.put("total", total);
		return map;
	}
	private Integer queryDate(String searchTime,String dateSign) throws Exception{
		DeptWorkCountVo vo=null;
		DBCursor dbCursorNow = queryDataByMongo(searchTime, dateSign, "KSGZLTJ_");
		Integer nowNum = 0;
		while(dbCursorNow.hasNext()){
			String json =dbCursorNow.next().get("value").toString();
			vo = JSONUtils.fromJson(json, DeptWorkCountVo.class);
			
			nowNum+=vo.getTotalcount();
		}
		return nowNum;
	}
	
	/**
	 * 
	 * 
	 * <p>获取查询mongodb</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月26日 下午8:12:20 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月26日 下午8:12:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date
	 * @param dataSign
	 * @param basicMenuAlias
	 * @return
	 * @throws Exception:
	 *
	 */
	private DBCursor queryDataByMongo(String date,String dataSign,String basicMenuAlias)throws Exception{
		BasicDBObject where = new BasicDBObject();
		if("3".equals(dataSign)){//年
			basicMenuAlias+="YEAR";
			date=date.substring(0, 4);//yyyy
		}
		if("2".equals(dataSign)){//月
			basicMenuAlias+="MONTH";
			date=date.substring(0, 7);//yyyy-MM
		}
		if("1".equals(dataSign)){//日
			basicMenuAlias+="DAY";
			date=date.substring(0, 10);//yyyy-MM-dd
		}
		where.append("regDate", date);
		DBCursor dbCursor = new MongoBasicDao().findAlldata(basicMenuAlias, where);
		return dbCursor;
	}
	
	/**
	 * 获取专家教授数据
	 * @throws Exception 
	 */
	private List<PieNameAndValue> queryLevelGHGZL(String searchTime,String dateSign) throws Exception{
		String basicMenuAlias="KSGZLTJ_";//科室工作量统计
		DBCursor dbCursor = queryDataByMongo(searchTime, dateSign, basicMenuAlias);
		List<PieNameAndValue> list = new ArrayList<PieNameAndValue>();
		Integer gjjzmzjcount=0;//国家级知名专家，数量
		Integer sjzmzjcount=0;//省级知名专家
		Integer zmzjcount=0;//知名专家
		Integer jscount=0;//教授
		Integer fjscount=0;//副教授
		Integer jymzcount=0;//简易门诊
		Integer ybyscount=0;//一般医生
		Integer zzyscount=0;//主治医生
		Integer lnyzcount=0;//老年优诊
		Integer slzcfcount=0;//视力诊查费
		Integer jmjskcount=0;//居民健身卡
		Integer totalcount=0;//total
		if(dbCursor.hasNext()){
			while(dbCursor.hasNext()){
				String json = dbCursor.next().get("value").toString();
				int gjjzmzjEach = Integer.parseInt(json.substring(json.indexOf("gjjzmzjcount"),json.length()).substring(14, json.substring(json.indexOf("gjjzmzjcount"),json.length()).indexOf(",")));
				int sjzmzjEach = Integer.parseInt(json.substring(json.indexOf("sjzmzjcount"),json.length()).substring(13, json.substring(json.indexOf("sjzmzjcount"),json.length()).indexOf(",")));
				int zmzjEach = Integer.parseInt(json.substring(json.indexOf("\"zmzjcount"),json.length()).substring(12, json.substring(json.indexOf("\"zmzjcount"),json.length()).indexOf(",")));
				int jsEach = Integer.parseInt(json.substring(json.indexOf("\"jscount"),json.length()).substring(10, json.substring(json.indexOf("\"jscount"),json.length()).indexOf(",")));
				int fjsEach = Integer.parseInt(json.substring(json.indexOf("fjscount"),json.length()).substring(10, json.substring(json.indexOf("fjscount"),json.length()).indexOf(",")));
				int jymzEach = Integer.parseInt(json.substring(json.indexOf("jymzcount"),json.length()).substring(11, json.substring(json.indexOf("jymzcount"),json.length()).indexOf(",")));
				int ybysEach = Integer.parseInt(json.substring(json.indexOf("ybyscount"),json.length()).substring(11, json.substring(json.indexOf("ybyscount"),json.length()).indexOf(",")));
				int zzysEach = Integer.parseInt(json.substring(json.indexOf("zzyscount"),json.length()).substring(11, json.substring(json.indexOf("zzyscount"),json.length()).indexOf(",")));
				int lnyzEach = Integer.parseInt(json.substring(json.indexOf("lnyzcount"),json.length()).substring(11, json.substring(json.indexOf("lnyzcount"),json.length()).indexOf(",")));
				int slzcfEach = Integer.parseInt(json.substring(json.indexOf("slzcfcount"),json.length()).substring(12, json.substring(json.indexOf("slzcfcount"),json.length()).indexOf(",")));
				int jmjskEach = Integer.parseInt(json.substring(json.indexOf("jmjskcount"),json.length()).substring(12, json.substring(json.indexOf("jmjskcount"),json.length()).indexOf(",")));
				int totalEach = Integer.parseInt(json.substring(json.indexOf("totalcount"),json.length()).substring(12, json.substring(json.indexOf("totalcount"),json.length()).indexOf(",")));
				gjjzmzjcount+=gjjzmzjEach;
				sjzmzjcount +=sjzmzjEach; 
				zmzjcount   +=zmzjEach; 
				jscount     +=jsEach ;
				fjscount    +=fjsEach ;
				jymzcount   +=jymzEach  ;
				ybyscount   +=ybysEach ;
				zzyscount   +=zzysEach  ;
				lnyzcount   +=lnyzEach  ;
				slzcfcount  +=slzcfEach ;
				jmjskcount  +=jmjskEach ;
				totalcount  +=totalEach;
			}
		}else{
			//初始值：量为0，其他为-
			PieNameAndValue voInit = new PieNameAndValue();
			voInit.setName("--");
			voInit.setValue(0);
			list.add(voInit);
			return list;
		}
		//创建11个对象，这11个对象是11个医生类别
		String[] doctorType={"国家级知名专家","省级知名专家","知名专家","教授","副教授","简易门诊","一般医生","主治医生","老年优诊","视力诊查费","居民健身卡"};
		Integer[] typeCount=new Integer[]{gjjzmzjcount,sjzmzjcount,zmzjcount,jscount,fjscount,jymzcount,ybyscount,zzyscount,lnyzcount,slzcfcount,jmjskcount};
//		int sum = 0;
		for(int i=0;i<doctorType.length;i++){
//			sum += typeCount[i];
			PieNameAndValue doctor = new PieNameAndValue();
			 doctor.setName(doctorType[i]);
			 doctor.setValue(typeCount[i]);
			 list.add(doctor);
		}
		List<PieNameAndValue> list2 = sortList(list, 5);
		PieNameAndValue otherVo = new PieNameAndValue();
		otherVo.setName("其他");
		otherVo.setTotal(totalcount);
		for(PieNameAndValue d : list2){
			totalcount = totalcount - d.getValue();  
		}
		otherVo.setValue(totalcount);
		list2.add(otherVo);
		return list2;
	}
	public List<PieNameAndValue> registerCountByType(String searchTime, String dateSign) throws Exception{
		String basicMenuAlias="KSGZLTJ_";//科室工作量统计
		DBCursor dbCursor = queryDataByMongo(searchTime, dateSign, basicMenuAlias);
		Integer jzcount=0;//急诊
		Integer yzcount=0;//优诊
		Integer yycount=0;//预约
		Integer ghcount=0;//过号
		Integer fzcount=0;//复诊
		Integer pzcount=0;//平诊
		Integer totalcount=0;////合计:数量
		
		while(dbCursor.hasNext()){
			String json =dbCursor.next().get("value").toString();
			DeptWorkCountVo vo = JSONUtils.fromJson(json, DeptWorkCountVo.class);
			jzcount+=vo.getJzcount();//急诊
			yzcount+=vo.getYzcount();//优诊
			yycount+=vo.getYycount();//预约
			ghcount+=vo.getGhcount();//过号
			fzcount+=vo.getFzcount();//复诊
			pzcount+=vo.getPzcount();//平诊
			totalcount+=vo.getTotalcount();
			
		}
		
		String[] doctorName={"急诊","优诊","预约","过号","复诊","平诊"};
		Integer[] doctorGzlNum={jzcount,yzcount,yycount,ghcount,fzcount,pzcount};
		List<PieNameAndValue> list = new ArrayList<PieNameAndValue>();
		for(int i=0;i<doctorName.length;i++){
			PieNameAndValue doctor = new PieNameAndValue();
			doctor.setName(doctorName[i]);
			doctor.setValue(doctorGzlNum[i]);
			list.add(doctor);
		}
		List<PieNameAndValue> list2 = new ArrayList<PieNameAndValue>();
		list2= sortList(list,6);
		return list2;
	}
public List<PieNameAndValue> sortList(List<PieNameAndValue> list,Integer topNumber){
		//对结果list排序
		Collections.sort(list, new Comparator<PieNameAndValue>() {// 自定义比较器,按照doctorGzlNum降序排序
					public int compare(PieNameAndValue o1, PieNameAndValue o2) {

						int num1 = o1.getValue();
						int num2 = o2.getValue();
						if (num1 > num2) {
							return -1;
						}
						if (num1 < num2) {
							return 1;
						}

						return 0;
					}
				});
				
		List<PieNameAndValue> resultList = new ArrayList<PieNameAndValue>();
		// 1.当list的长度>=topNumber,取出前topnumber，+其他
		PieNameAndValue otherVo = new PieNameAndValue();
//		otherVo.setDoctorName("其他");
		int doctorGzlNum = 0;
		int topNum =topNumber;
		if (list.size() >= topNum) {
			for (int i = 0; i < list.size(); i++) {
				if (i < topNum ) {// 因为角标从0开始，所以<要查询的数量
					if("其他".equals(list.get(i).getName())){
						doctorGzlNum += list.get(i).getValue();
					}else{
						resultList.add(list.get(i));
					}
				} else {
					doctorGzlNum += list.get(i).getValue();
				}
			}
		} else {
			// 2.当list的长度<topNumber,取出list的全部，+其他，其中其他的值都是0
			for (int i = 0; i < list.size(); i++) {
				resultList.add(list.get(i));
			}
		}
		return resultList;
	}
}
