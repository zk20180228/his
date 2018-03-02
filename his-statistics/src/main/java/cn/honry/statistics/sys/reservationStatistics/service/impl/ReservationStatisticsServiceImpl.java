package cn.honry.statistics.sys.reservationStatistics.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.inneReservation.dao.InnerReserVaDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsDao;
import cn.honry.statistics.sys.reservationStatistics.dao.ReservationStatisticsDao;
import cn.honry.statistics.sys.reservationStatistics.service.ReservationStatisticsService;
import cn.honry.statistics.sys.reservationStatistics.vo.RegGradeVo;
import cn.honry.statistics.sys.reservationStatistics.vo.ReservationStatistics;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

@Service("reservationStatisticsService")
@Transactional
@SuppressWarnings({ "all" })
public class ReservationStatisticsServiceImpl implements ReservationStatisticsService{

	@Autowired
	@Qualifier(value = "reservationStatisticsDao")
	private ReservationStatisticsDao reservationStatisticsDao;
	@Autowired
	@Qualifier(value = "reportFormsDAO")
	private ReportFormsDao reportFormsDAO;

	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value="innerReserVaDao")
	private InnerReserVaDao innerReserVaDao;
	
	public void setInnerReserVaDao(InnerReserVaDao innerReserVaDao) {
		this.innerReserVaDao = innerReserVaDao;
	}

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterPreregister get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(RegisterPreregister entity) {
		
	}

	/**  
	 *  
	 * @Description：  预约统计查询
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<ReservationStatistics> getInfo(String dept, String stime,String etime) {
		ReservationStatistics reser=null;
		List<ReservationStatistics> countVoList=new ArrayList<ReservationStatistics>();
		List<RegisterInfo> infoList=reservationStatisticsDao.findinfo(dept,stime,etime);
		List<SysDepartment> deptList=reservationStatisticsDao.finddept();
		List<RegisterPreregister> registerList=reservationStatisticsDao.findregister(dept,stime,etime);
		for (SysDepartment sys : deptList) {
			reser = new ReservationStatistics();
			reser.setDeptName(sys.getDeptName());
		for (RegisterInfo info : infoList) {
			if(info.getDept().equals(sys.getId())){
				List<RegisterGrade> gradeList=reservationStatisticsDao.findgrade(info.getGrade());
				for (RegisterGrade grade : gradeList) {
					if(grade.getSpecialistno()==1){//普通号，含专科号
						reser.setCommonNumber(reser.getCommonNumber()+1);
					}if(grade.getExpertno()==1){//专家号
						reser.setNumberExpert(reser.getNumberExpert()+1);
					}
					reser.setCountAllInfo(reser.getCommonNumber()+reser.getNumberExpert());
				}
			
				//复诊
				if(info.getIsfiest()==0){
					reser.setFurtherConsultation(reser.getFurtherConsultation()+1);
				}if(info.getIsfiest()==1){//初诊
					reser.setFirstVisit(reser.getFirstVisit()+1);
				}
				reser.setCountDoctorVisits(reser.getFurtherConsultation()+reser.getFirstVisit());
			}
		   }
		for (RegisterPreregister re : registerList) {
			if(re.getPreregisterDept().equals(sys.getId())){
				List<RegisterGrade> gradeList=reservationStatisticsDao.findgrade(re.getPreregisterGrade());
                 for (RegisterGrade regrade : gradeList) {
                	 if(regrade.getSpecialistno()==1){
                		 reser.setCommonNumberRe(reser.getCommonNumberRe()+1);
                	 }if(regrade.getExpertno()==1){
                		 reser.setNumberExpertRe(reser.getNumberExpertRe()+1);
                	 }
				}
			if(re.getIsFirst()==1){//初诊预约挂号
				reser.setFirstVisitRe(reser.getFirstVisitRe()+1);
			}if(re.getIsFirst()==2){//复诊预约挂号
				reser.setFurtherConsultationRe(reser.getFurtherConsultationRe()+1);
			}if(re.getPreregisterIsphone()==1){//电话
				reser.setPhoneBooking(reser.getPhoneBooking()+1);
			}if(re.getPreregisterIsnet()==1){//网络
				reser.setNetBooking(reser.getNetBooking()+1);
			}if(re.getPreregisterIsphone()==0||re.getPreregisterIsnet()==0){
				reser.setWindowBooking(reser.getWindowBooking()+1);
			}
			reser.setTotal(reser.getNumberExpertRe()+reser.getCommonNumberRe()+reser.getWindowBooking()+reser.getPhoneBooking()+reser.getNetBooking()+reser.getOtherBooking()+reser.getFirstVisitRe()+reser.getFurtherConsultationRe());
		  }
		}
		if(reser.getTotal()!=0||reser.getCountAllInfo()!=0||reser.getCountDoctorVisits()!=0){
			countVoList.add(reser);
		}
	  }
		
		return countVoList;
	}
	
	public Map<String,Object> getInfoNow(String dept, String stime,String etime,String page,String rows,String jobNo,String menuAlias) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(dept)){
			map.put("rows", new ArrayList<ReservationStatistics>());
			map.put("total", 0);
			return map;
		}
		boolean sign=new MongoBasicDao().isCollection("YYTJ_DAY");
//		sign=false;
		if(sign){
			map=reservationStatisticsDao.getInfoNow(dept, stime, etime, page, rows, menuAlias);
		}else{
			List<String> pretnl = new ArrayList<String>();
			List<String> maintnl = new ArrayList<String>();
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
				if(DateUtils.compareDate(sTime, dTime)!=0){//在线表只保存当天数据
					if(DateUtils.compareDate(eTime, cTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
						pretnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_PREREGISTER",stime,etime);
						maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",stime,etime);
					}else{//查询主表和分区表
					
						//获取时间差（年）
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stime);
						
						//获取相差年份的分区集合 
						pretnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_PREREGISTER",yNum+1);
						pretnl.add(0,"T_REGISTER_PREREGISTER_NOW");
						maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
						maintnl.add(0,"T_REGISTER_MAIN_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					pretnl.add("T_REGISTER_PREREGISTER_NOW");
					maintnl.add("T_REGISTER_MAIN_NOW");
	
				}
			}catch(Exception e){
				
				pretnl = new ArrayList<String>();
				maintnl = new ArrayList<String>();
				throw new RuntimeException(e);
			}
			
			List<RegGradeVo> regGrade = reservationStatisticsDao.getRegGrade();
			List<ReservationStatistics> list = reservationStatisticsDao.getInfoNow(regGrade, pretnl, maintnl, dept, stime, etime,page,rows,jobNo,menuAlias);
			for (ReservationStatistics re : list) {
				re.setTotal(re.getCommonNumberRe()+re.getNumberExpertRe()+re.getWindowBooking()+re.getPhoneBooking()+re.getNetBooking()+re.getOtherBooking()+re.getFirstVisitRe()+re.getFurtherConsultationRe());
			}
			String redKey = "YYTJ"+stime+"_"+etime+"_"+dept+"_"+jobNo;
			redKey=redKey.replaceAll(",", "-");
			Integer total = (Integer) redisUtil.get(redKey);
			if(total==null){
				total = reservationStatisticsDao.getTotal(regGrade, pretnl, maintnl, dept, stime, etime,jobNo,menuAlias);
				redisUtil.set(redKey, total);
			}
			String val = parameterInnerDAO.getParameterByCode("deadTime");
			if(StringUtils.isNotBlank(val)){
				redisUtil.expire(redKey,Integer.valueOf(val));
			}else{
				redisUtil.expire(redKey, 300);
			}
			map.put("rows", list);
			map.put("total", total);
		}
		return map;
	}
	
	/**  
	 *  
	 * @Description：  科室下拉框(挂号科室)
	 * @Author：wujiao
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getComboboxdept() {
		
		return reservationStatisticsDao.getComboboxdept();
	
	}
	@Override
	public void initReservation(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="YYTJ";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				innerReserVaDao.initReserVation(menuAlias, null, begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
			}else if(3==type){//年数据dateformate:yyyy
			
			}
		}
		
	}

}
