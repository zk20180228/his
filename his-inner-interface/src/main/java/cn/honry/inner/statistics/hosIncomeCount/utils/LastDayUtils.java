package cn.honry.inner.statistics.hosIncomeCount.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LastDayUtils {
			
			//获取某月的最后一天
			/**
			 * @Description:获取某个月的最后一天
			 * @param date 时间格式必须是大于：yyyy-mm-dd长度的格式
			 * @return
			 * String 返回最后一天的字符串表示形式
			 * @exception:
			 * @author: zhangkui
			 * @time:2017年6月24日 上午10:21:49
			 */
		   public static String getLastDay(String date){
			   	date= date.substring(0, 7);
			   	//System.out.println(date);
			   	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			   	Date time =null;
			   	try {
			   		 time = dateFormat.parse(date);
			   	} catch (ParseException e) {
			   		e.printStackTrace();
			   	}
			   	 Calendar  calendar =  Calendar.getInstance(); 
			   	 calendar.setTime(time);
			   	 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
			   	 Date   lastDate   =   calendar.getTime();  
			        lastDate.setDate(lastDay);  
			        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		   	return dateFormat1.format(lastDate).substring(8, 10);
		   }
		   
		   
		   
		
}
