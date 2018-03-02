package cn.honry.statistics.deptstat.peopleNumOfOperation.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.peopleNumOfOperation.dao.InnerPeopleNumOfOperationDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.operationDeptLevel.vo.OperationDeptLevelVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;
import cn.honry.statistics.deptstat.peopleNumOfOperation.dao.PeopleNumOfOperationDao;
import cn.honry.statistics.deptstat.peopleNumOfOperation.service.PeopleNumOfOperationService;
import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
@Service("peopleNumOfOperationService")
@Transactional
public class PeopleNumOfOperationServiceImpl implements PeopleNumOfOperationService {
	@Autowired
	@Qualifier(value="innerPeopleNumOfOperationDao")
	private InnerPeopleNumOfOperationDao innerPeopleNumOfOperationDao;
	@Autowired
	private PeopleNumOfOperationDao peopleNumOfOperationDao;

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public Map<String,Object> listPeopleNumOfOperation(String page,String rows, String begin) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isBlank(begin)){
			return map;
		}
		boolean sign=new MongoBasicDao().isCollection("SSKSSSRSTJ_MONTH");
		if(sign){
			map=peopleNumOfOperationDao.queryForDBList(begin,rows,page);
			return map;
		}else{
			Map<String,String> map1=getTimes(begin);
			String startDate=map1.get("start");
			String endDate =map1.get("end");
			
			List<String> tnL = null;
			try {
				//1.时间转换
				Date sTime = DateUtils.parseDateY_M_D(startDate);
				Date eTime = DateUtils.parseDateY_M_D(endDate);
				//2.获取住院数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if("1".equals(dateNum)){
					dateNum="30";
				}
				//3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startDate,endDate);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startDate);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
						tnL.add(0,"T_INPATIENT_INFO_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_INPATIENT_INFO_NOW");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tnL = null;
			}
			List<PeopleNumOfOperationVo> vos = (List<PeopleNumOfOperationVo>)peopleNumOfOperationDao.listPeopleNumOfOperation(tnL,page,rows,startDate,endDate);
			if(vos!=null && vos.size()>0){
				DecimalFormat df = new DecimalFormat("######0.00"); 
				for (PeopleNumOfOperationVo vo : vos) {
					Double optnums = vo.getOptnums();
					Double cyzs = vo.getCyzs();
					Double totalPatient = vo.getTotalPatient();
					vo.setSsrszqyb(df.format((optnums/totalPatient)*100));
					vo.setSszb(df.format((optnums/cyzs)*100));
				}
			}
			int num= peopleNumOfOperationDao.findTotalRecord(startDate,endDate);
			map.put("total", num);
			map.put("rows", vos);
		}		
		return map;
	}
	@Override
	public Integer findTotalRecord( String begin, String end) throws Exception {
		
		return peopleNumOfOperationDao.findTotalRecord(begin,end);
	}
	@Override
	public FileUtil export(List<PeopleNumOfOperationVo> peopleNumOfOperationVo,
			FileUtil fUtil) throws IOException {
		if(peopleNumOfOperationVo == null || peopleNumOfOperationVo.isEmpty()){
			return fUtil;
		}
		for (PeopleNumOfOperationVo vo : peopleNumOfOperationVo) {
			String record="";
			record += StringUtils.isBlank(vo.getDept_name()) ? "," : vo.getDept_name() + ",";//科室名称
			record += vo.getOptnums()== null ? "0," : vo.getOptnums() + ",";//手术人数
			record += vo.getOptcounts() == null ? "0," : vo.getOptcounts() + ",";//手术例数
			record += vo.getCyzs() == null ? "0," : vo.getCyzs() + ",";//出院总数
			record += StringUtils.isBlank(vo.getOperaPro()) ? "," : vo.getOperaPro() + ",";//手术占比
			record += vo.getTotalPatient() == null ? "0," : vo.getTotalPatient() + ",";//全院患者
			record += null==vo.getSsrszqybb() ? "," : vo.getSsrszqybb() + ",";//手术人数占全院比
			fUtil.write(record);
		}
		return fUtil;
	}
	@Override
	public void initPeopleNumOfOperation(String begin, String end) throws Exception {
		peopleNumOfOperationDao.initPeopleNumOfOperation(begin,end);
		
	}

	public Map<String,String> getTimes(String searchTime) {
		Map<String,String> map=new HashMap<String, String>();
		String begin=searchTime+"-01 00:00:00";
		String end="";
		if(DateUtils.formatDateY_M(new Date()).equals(searchTime)){
			end=DateUtils.formatDateY_M_D_H_M_S(new Date());
		}else{
			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(begin);
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		        end=DateUtils.formatDateY_M_D_H_M_S(calendar.getTime());
			} catch (ParseException e) {
			}
		}
		map.put("start", begin);
		map.put("end", end);
		return map;
	}
	@Override
	public List<PeopleNumOfOperationVo> exportPeopleNumOfOperation(String begin) throws Exception {
		List<PeopleNumOfOperationVo> list;
		if(StringUtils.isBlank(begin)){
			return new ArrayList<PeopleNumOfOperationVo>();
		}
		boolean sign=new MongoBasicDao().isCollection("SSKSSSRSTJ_MONTH");
		if(sign){
			list=peopleNumOfOperationDao.queryExportForDBList(begin);
			return list;
		}else{
			Map<String,String> map1=getTimes(begin);
			String startDate=map1.get("start");
			String endDate =map1.get("end");
			list = (List<PeopleNumOfOperationVo>)peopleNumOfOperationDao.listPeopleNumToDB(startDate,endDate);
			
			if(list!=null && list.size()>0){
				DecimalFormat df = new DecimalFormat("######0.00"); 
				for (PeopleNumOfOperationVo vo : list) {
					Double optnums = vo.getOptnums();
					Double cyzs = vo.getCyzs();
					Double totalPatient = vo.getTotalPatient();
					vo.setSsrszqyb(df.format((optnums/totalPatient)*100));
					vo.setSszb(df.format((optnums/cyzs)*100));
				}
			}
		}		
		return list;
	}
	@Override
	public void init_SSKSSSRSTJ(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="SSKSSSRSTJ";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				innerPeopleNumOfOperationDao.init_SSKSSSRSTJ_Day(menuAlias, null, begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					innerPeopleNumOfOperationDao.init_SSKSSSRSTJ(menuAlias, null, begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
			
			}
		}
		
	}
	/**  
	 * 手术科室手术人数统计(含心内)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public Map<String,Object> queryPeopleNumOfOperation(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		boolean flag=new MongoBasicDao().isCollection("SSKSSSRSTJ_DAY");
		Map<String,Object> map=new HashMap<String,Object>();
		List<PeopleNumOfOperationVo> list=new ArrayList<PeopleNumOfOperationVo>();
		if(flag){
			map=peopleNumOfOperationDao.queryPeopleNumOfOperationForDB(startTime,endTime,deptCode,menuAlias,page,rows);
		}else{
			list=peopleNumOfOperationDao.queryPeopleNumOfOperation(startTime,endTime,deptCode,menuAlias,page, rows);	
			int total=peopleNumOfOperationDao.getTotalPeopleNumOfOperation(startTime,endTime,deptCode,menuAlias,page,rows);
			map.put("rows", list);
			map.put("total", total);
			return map;
		}
		return map;
	}
	
	/**  
	 * 手术科室手术人数统计(含心内)  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalPeopleNumOfOperation(String startTime, String endTime,String deptCode, String menuAlias, String page, String rows) {
		boolean flag=new MongoBasicDao().isCollection("SSKSSSRSTJ");
		List<PeopleNumOfOperationVo> list=new ArrayList<PeopleNumOfOperationVo>();
		if(flag){
			
		}else{
			return peopleNumOfOperationDao.getTotalPeopleNumOfOperation(startTime,endTime,deptCode,menuAlias,page,rows);
		}
		return list.size();
	}
	
}
