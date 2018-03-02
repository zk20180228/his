package cn.honry.statistics.bi.bistac.registerTotal.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.bi.bistac.registerTotal.dao.RegisterTotalDao;
import cn.honry.statistics.bi.bistac.registerTotal.service.RegisterTotalService;
import cn.honry.utils.DateUtils;
@Service("registerTotalService")
public class RegisterTotalServiceImpl implements RegisterTotalService{
	@Resource
	private RegisterTotalDao registerTotalDao;
	/** 参数管理接口 **/
	private final static DateFormat df=new SimpleDateFormat("HH:mm");
	private final static Pattern pattern = Pattern.compile("((08)|(12)|(18)):0\\d{1}");

	@Override
	public void initRegisterTotal(String begin,String end,Integer type) {
	if(1==type){//日数据 dateformate:yyyy-MM-dd
		registerTotalDao.initRegisterTotal(begin);
		registerTotalDao.initRegisterDoctorTotal(begin);
	}else if(2==type){//月数据 dateformate:yyyy-MM 
		Date beginDate=DateUtils.parseDateY_M(begin);
		Calendar ca=Calendar.getInstance();
		ca.setTime(beginDate);
		Date endDate=DateUtils.parseDateY_M(end);
		while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
			begin=DateUtils.formatDateY_M(ca.getTime());//本月第一天
			registerTotalDao.initRegisterDoctorTotalMonthAndYear(begin, "2");
			registerTotalDao.initRegisterTotalMonthAndYear(begin, "2");
			ca.add(Calendar.MONTH, 1);//下一月
		}
	}else if(3==type){//年数据dateformate:yyyy
		Date beginDate=DateUtils.parseDateY(begin);
		Date endDate=DateUtils.parseDateY(end);
		Calendar ca=Calendar.getInstance();
		ca.setTime(beginDate);
		while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
			begin=DateUtils.formatDateY(ca.getTime());
			registerTotalDao.initRegisterDoctorTotalMonthAndYear(begin, "3");
			registerTotalDao.initRegisterTotalMonthAndYear(begin, "3");
			ca.add(Calendar.YEAR, 1);
		}
	}
		
	}

	@Override
	public void tenTimingPerformRegister(String begin) {
		registerTotalDao.tenTimingPerformRegisterDoctorTotal(begin);
		registerTotalDao.tenTimingPerformRegisterTotal(begin);
		
		if(pattern.matcher(df.format(new Date())).matches()){
//			SmsWebService_Service webService_Service = new SmsWebService_Service();
//			SmsWebService smsWebService = webService_Service.getSmsWebServiceImplPort();
//			smsWebService.send("scheduler", "移动端挂号量数据处理完成");
		}
	}

}
