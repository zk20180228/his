/**
 * 
 */
package cn.honry.statistics.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.DateUtils;

/**
 * 将统计数据按要求封装成json格式
 * @author: luyanshou
 *
 */
public class ResultUtils {

	/**
	 * 封装json格式数据
	 * @param t1 起始时间
	 * @param t2 结束时间
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计,4-按日统计)
	 * @param list 统计数据(list中的string为json格式)
	 * @param map 统计维度列表
	 * @param length 被替换的数组长度
	 * @return
	 */
	public static String getResult(Date t1,Date t2,int n,List<String> list,Map<String,List<String>> map,int length){
		String[] fields = getFields(t1, t2, n);
		String result = getResult(n, list, map, fields,length);
		return result;
	}
	
	/**
	 * 封装json格式数据
	 * @param vo 时间
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计,4-按日统计)
	 * @param list 统计数据(list中的string为json格式)
	 * @param map 统计维度列表
	 * @param length 被替换的数组长度
	 * @return
	 */
	public static String getResult(DateVo vo,int n,List<String> list,Map<String,List<String>> map,int length){
		String[] fields = getFields(vo, n);
		String result = getResult(n, list, map, fields,length);
		return result;
	}
	
	/**
	 * 封装json格式数据
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计,4-按日统计)
	 * @param list 统计数据(list中的string为json格式)
	 * @param map 统计维度列表
	 * @param fields 时间字符串
	 * @return
	 */
	private static String getResult(int n,List<String> list,Map<String,List<String>> map,String[] fields,int length){
		
		StringBuffer sbf=new StringBuffer("[{");
		//遍历map 将key和value拼接成json形式 如 "dept":"耳鼻喉科"
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> li = entry.getValue();
			List<String> list1= new ArrayList<>();
			for (String str : li) {
				String v="\""+key+"\":"+"\""+str+"\"";
				list1.add(v);
			}
			map.put(key, list1);
		}
		//遍历map 将 key 对应的数据进行组合拼接
		int i=1;
		List<String> l=new ArrayList<>();
		for(Map.Entry<String, List<String>> entry : map.entrySet()){
			List<String> li = entry.getValue();
			List<String> lt=new ArrayList<>();
			if(i==1){
				l.addAll(li);
			}
			if(i>=2){
				for(String nexs : li){
					for (String pres : l) {
						String v=pres+","+nexs;
						lt.add(v);
					}
				}
				l.clear();
				l.addAll(lt);
			}
			i++;
		}
		//将替换字段列表拼接成json形式
		String ss="";
		String v="";
		for (int j=0;j<fields.length;j++) {
			String fie= fields[j];
			for (int k = 1;k <= length; k++) {
				if(j==fields.length-1 && k==length){
					v+="\""+fie+k+"\":"+"\"0\"";
				}else{
					v+="\""+fie+k+"\":"+"\"0\",";
				}
				ss+=","+fie+k;
			}
		}
		//将字段列表与从map得到的数据进行拼接
		for (String str : l) {
			if(l.indexOf(str)<l.size()-1){
				sbf.append(str+","+v+"},{");
			}
			else{
				sbf.append(str+","+v+"}]");
			}
		}
		//将结果集list中的数据替换到json串中相应的位置
		int size = map.size();
		if(list==null ||list.size()==0){
			return sbf.toString();
		}
		for (String str : list) {
			int m = (str.indexOf("{"))+1;
			int j = str.indexOf("}");
			//去除大括号以后,按逗号进行切割
			String js = str.substring(m, j);
			String[] val = js.split(",");
			String shead = getString(val, l, size);//获取结果集list中的数据在json串中的位置
			if(StringUtils.isNotBlank(shead)){
				for (String st : val) {
					String s0 = st.substring(1, st.indexOf(":")-1);
					if(ss.indexOf(s0)>=0){
						int index = sbf.indexOf(shead);
						int start = sbf.indexOf(s0, index);
						if(start>0){
							int k = sbf.indexOf("\"", start+13)+1;
							sbf.replace(start-1, k, st);
						}
					}
					
				}
			}
		}
		return sbf.toString();
	}
	
	/**
	 * 用于确定结果集list中的数据在json串中的位置(根据每行数据的"开头"不同进行区分)
	 * @param val 结果集list中数据 按","切割得到的数组
	 * @param list 维度的所有组合值
	 * @param n 维度的个数
	 * @return
	 */
	private static String getString(String[] val,List<String> list,int n){
		
		for (String str : list) {//list中的数据格式例如("dept":"耳鼻喉科","area":"北京","sex":"男")
			int i=0;
			for (String s: val) {//在数组val中应该有字符串"area":"北京"、"sex":"男"和"dept":"耳鼻喉科"
				if(str.contains(s)){
					i++;
				}
			}
			if(i==n){//只有在val中同时包含字符串"area":"北京"、"sex":"男"和"dept":"耳鼻喉科"时才返回 list中相应的字符串
				return str;
			}
		}
		return null;
	}
	
	/**
	 * 根据时间和统计方式生成替换的字段列表 
	 * 生成规则:年(4位)+季(1位)+月(2位)+日(2位)共9位
	 * 例如:201400000代表2014年(按年统计)
	 *     201420000代表2014年第2季度(按季度统计)
	 *     201400800代表2014年8月(按月统计)
	 *     201401001代表2014年10月1日(按日统计)
	 * @param t1 起始时间
	 * @param t2 结束时间
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计,4-按日统计)
	 * @return
	 */
	public static String[] getFields(Date t1,Date t2,int n){
		Calendar c = Calendar.getInstance();
		c.setTime(t1);
		int year1 = c.get(Calendar.YEAR);//获取年份
		int month1 = c.get(Calendar.MONTH)+1;//获取月份
		int day1 = c.get(Calendar.DAY_OF_MONTH);//获取日
		c.setTime(t2);
		int year2 = c.get(Calendar.YEAR);//获取年份
		int month2 = c.get(Calendar.MONTH)+1;//获取月份
		if(n==1){//按年统计
			int y=year2-year1;//计算年份差
			int l=y>1?y:1;
			String[] a= new String[l];
			for (int i = 0; i < l; i++) {
				a[i]=year1+i+"00000";
			}
			return a;
		}
		if(n==2){//按季统计
			int m=((year2-year1)*12+month2-month1)/3; //计算季差
			int l=m>1?m:1;//数组长度
			int j=month1/3;
			String[] a= new String[l];
			for(int i=0;i<l;i++){
				a[i]=year1+(j+i)/4+""+((j+i)%4)+"0000";
			}
			return a;
		}
		if(n==3){//按月统计
			int m=(year2-year1)*12+month2-month1;//计算月差
			int l=m>1?m:1;//数组长度
			int j=month1%12;
			String[] a= new String[l];
			for(int i=0;i<l;i++){
				a[i]=year1+(j+i)/12+"0"+((j+i)%12)+"00";
			}
			return a;
		}
		if(n==4){//按日统计
			int d = DateUtils.subDateGetDay(t1, t2);//计算日差
			int l=d>1?d:1;//数组长度
			String[] a= new String[l];
			c.set(year1, month1, day1);
			for (int i=0;i<l;i++) {
				int y = c.get(Calendar.YEAR);
				int m = c.get(Calendar.MONTH);
				String mon=""+(m>=10?m:("0"+m));
				int da = c.get(Calendar.DAY_OF_MONTH);
				String day=""+(da>=10?da:("0"+da));
				a[i]=y+"0"+mon+day;
				c.add(Calendar.HOUR, 24);
			}
			return a;
		}
		return null;
	}
	
	/**
	 * 根据时间和统计方式生成替换的字段列表 
	 * 生成规则:年(4位)+季(1位)+月(2位)+日(2位)共9位
	 * 例如:201400000代表2014年(按年统计)
	 *     201420000代表2014年第2季度(按季度统计)
	 *     201400800代表2014年8月(按月统计)
	 *     201401001代表2014年10月1日(按日统计)
	 * @param vo 时间
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计,4-按日统计)
	 * @return
	 */
	public static String[] getFields(DateVo vo,int n){
		int year1 = vo.getYear1();//获取开始年份
		int year2 = vo.getYear2();//获取结束年份
		int y=year2-year1+1;//计算年份差
		int l=y>1?y:1;
		int[] years= new int [l];
		if(n>=1){//按年统计
			String[] a= new String[l];
			for (int i = 0; i < l; i++) {
				years[i]=year1+i;
				a[i]=year1+i+"00000";
			}
			if(n==1){
				return a;	
			}
		}
		int quarter1 = vo.getQuarter1();//获取开始季度
		int quarter2 = vo.getQuarter2();//获取结束季度
		int q = quarter2-quarter1+1;//计算季差
		 int lq=q>1?q:1;
		if(n==2){//按季统计
			String[] a= new String[l*lq];
			int z=0;
			for(int i=0;i<l;i++){
				for(int j=0;j<lq;j++){
					a[z]=years[i]+""+(quarter1+j)+"0000";
					z++;
				}
			}
		return a;	
		}
		
		int month1 = vo.getMonth1();//获取开始月份
		int month2 = vo.getMonth2();//获取结束月份
		int m= month2-month1+1;//计算月差
		int lm=m>1?m:1;
		String[] a= new String[lm*l];
		if(n>=3){
			String[] months= new String[lm*l];
			int z=0;
			for(int i=0;i<l;i++){
				for(int j=0;j<lm;j++){
					int mo=month1+j;
					String mon=""+(mo>=10?mo:("0"+mo));
					a[z] = years[i]+"0"+(mon);
					months[z]=years[i]+"0"+(mon)+"00";
					z++;
				}
			}
			if(n==3){//按月统计
				return months;
			}
		}
		
		int day1 = vo.getDay1();//获取开始日
		int day2 = vo.getDay2();//获取结束日
		int d= day2-day1+1;//计算日差
		int ld=d>1?d:1;
		if(n==4){//按日统计
			String[] days= new String[lm*l*ld];
			int z=0;
			for (String str : a) {
				for (int i = 0; i <ld; i++) {
					days[z]=str+((day1+i)>=10?(day1+i):("0"+(day1+i)));
					z++;
				}
			}
			return days;
		}
		return null;
	}
	
	/**
	 * 将json中 的列字段替换为相应的时间字符串格式
	 * @param json json数据
	 * @param n 统计方式(1-按年统计;2-按季统计;3-按月统计,4-按日统计)
	 * @param columns 需要被替换的列字段(按顺序排列)
	 * @param d json数据相应的时间
	 * @return
	 */
	public static String getnewJson(String json,int n,String[] columns,String d){
		if(n==1){
			d= d+"/01/01";
		}else if(n==2){
			String y = d.substring(0, 4);
			String s = d.substring(5);
			int i = Integer.parseInt(s)*3;
			d=y+"/"+i+"/01";
		}else if(n==3){
			d= d+"/01";
		}
		DateVo vo = new DateVo();
		StringBuffer sbf=new StringBuffer(json);
		Calendar c = Calendar.getInstance();
		String d1=d.replace("/", "-");//将"/"替换为"-"(否则DateUtils工具类无法转换)
		Date s=DateUtils.parseDateY_M_D(d1);
		c.setTime(s);
		int year = c.get(Calendar.YEAR);//获取年份
		int month = c.get(Calendar.MONTH)+1;//获取月份
		int day = c.get(Calendar.DAY_OF_MONTH);//获取日
		vo.setDay1(day);
		vo.setDay2(day);
		vo.setMonth1(month);
		vo.setMonth2(month);
		vo.setQuarter1(month/3);
		vo.setQuarter2(month/3);
		vo.setYear1(year);
		vo.setYear2(year);
		String[] fields = getFields(vo, n);
		String field= fields[0];
		int i=1;
		for (String str : columns) {
			int index = sbf.indexOf(str);
			if(index>-1){
				int end = sbf.indexOf(":", index)-1;
				sbf.replace(index, end, field+i);
			}
			i++;
		}
		return sbf.toString();
	}
	//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
	public static Map<String,List<String>> prepareParamMap(String dimensionValue){
		//获得维度种类和相应的维度值的字符串
		if(dimensionValue.indexOf("?")!=-1){
			String[] dimensionValueArray = dimensionValue.split("\\?");
			//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
			Map<String,List<String>> map=new HashMap<String, List<String>>();
			List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
			for(int i=0;i<dimensionValueArray.length;i++){
				//将维度种类和维度值放入数组中，第一个元素为维度种类
				String [] diArrayValue = dimensionValueArray[i].split(",");
				List<String> diList=new ArrayList<String>();//放置维度值的list
				for(int j=0;j<(diArrayValue.length-1);j++){
					diList.add(diArrayValue[j+1]);
				}
				map.put(diArrayValue[0], diList);
				Map<String,List<String>> map1=new HashMap<String, List<String>>();
				map1.put(diArrayValue[0], diList);
				list.add(map1);
			}
			return map;
		}else {
			String [] dimensionValueArray = {dimensionValue};
			//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
			Map<String,List<String>> map=new HashMap<String, List<String>>();
			List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
			for(int i=0;i<dimensionValueArray.length;i++){
				//将维度种类和维度值放入数组中，第一个元素为维度种类
				String [] diArrayValue = dimensionValueArray[i].split(",");
				List<String> diList=new ArrayList<String>();//放置维度值的list
				for(int j=0;j<(diArrayValue.length-1);j++){
					diList.add(diArrayValue[j+1]);
				}
				map.put(diArrayValue[0], diList);
				Map<String,List<String>> map1=new HashMap<String, List<String>>();
				map1.put(diArrayValue[0], diList);
				list.add(map1);
			}
			return map;
		}
	}
	//组织参数list：list中的元素为map
	public static List<Map<String,List<String>>> prepareParamList(String dimensionValue){
		//获得维度种类和相应的维度值的字符串
			if(dimensionValue.indexOf("?")!=-1){
				String[] dimensionValueArray = dimensionValue.split("\\?");
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				for(int i=0;i<dimensionValueArray.length;i++){
					//将维度种类和维度值放入数组中，第一个元素为维度种类
					String [] diArrayValue = dimensionValueArray[i].split(",");
					List<String> diList=new ArrayList<String>();//放置维度值的list
					for(int j=0;j<(diArrayValue.length-1);j++){
						diList.add(diArrayValue[j+1]);
					}
					map.put(diArrayValue[0], diList);
					Map<String,List<String>> map1=new HashMap<String, List<String>>();
					map1.put(diArrayValue[0], diList);
					list.add(map1);
				}
				return list;
			}else {
				String [] dimensionValueArray = {dimensionValue};
				//组织参数map：key为各维度的名字（code），value为各维度所选择的值的list
				Map<String,List<String>> map=new HashMap<String, List<String>>();
				List<Map<String,List<String>>> list=new ArrayList<Map<String,List<String>>>();
				for(int i=0;i<dimensionValueArray.length;i++){
					//将维度种类和维度值放入数组中，第一个元素为维度种类
					String [] diArrayValue = dimensionValueArray[i].split(",");
					List<String> diList=new ArrayList<String>();//放置维度值的list
					for(int j=0;j<(diArrayValue.length-1);j++){
						diList.add(diArrayValue[j+1]);
					}
					map.put(diArrayValue[0], diList);
					Map<String,List<String>> map1=new HashMap<String, List<String>>();
					map1.put(diArrayValue[0], diList);
					list.add(map1);
				}
				return list;
			}
	
	}
	
	
	/**
	 * 根据开始时间和结束时间计算统计的年月日--用于自定义日期统计的计算
	 * @param startDate 开始时间 
	 * @param endDate 结束时间
	 * @return
	 */
	public static Map<String,List<String>> getDate(String startDate,String endDate){
		Map<String,List<String>> map = new HashMap<>();
		Date sdate_D = DateUtils.parseDateY_M_D(startDate);
		Date edate_D = DateUtils.parseDateY_M_D(endDate);
		Date sdate_M = DateUtils.parseDateY_M(startDate);
		Date edate_M = DateUtils.parseDateY_M(endDate);
		
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdate_D);
		c2.setTime(edate_D);
		int eMonth = c2.get(Calendar.MONTH)+1;//结束月份
		
		List<String> dayList =new ArrayList<>();
		List<String> monthList=new ArrayList<>();
		List<String> yearList=new ArrayList<>();
		map.put("day", dayList);
		map.put("month", monthList);
		map.put("year", yearList);
		
		
		
		//判断开始时间是否为某月的第一天，如果不是，将该月的日期(从该日期开始到该月的最后一天)加入dayList中
		if(DateUtils.compareDate(sdate_D, sdate_M)>0){
			Date nextMonth = DateUtils.addMonth(sdate_M, 1);//下个月的一号
			while(DateUtils.compareDate(sdate_D, nextMonth)<0){//开始时间小于下个月的一号(即本月的日期)加入dayList中
				if(DateUtils.compareDate(sdate_D, edate_D)>0){//如果开始时间大于结束时间,return;
					return map;
				}
				String date = DateUtils.formatDateY_M_D(sdate_D);
				dayList.add(date);
				sdate_D=DateUtils.addDay(sdate_D, 1);
			}
			sdate_M=nextMonth;//将开始月份的下个月作为新的开始时间
		}
		c1.setTime(DateUtils.addDay(edate_D, 1));//结束日期如果是本月的最后一天,那么+1后的月份大于结束日期的月份;否则说明不是最后一天
		int i = c1.get(Calendar.MONTH)+1;
		if(i==eMonth){//判断结束日期是否是某月的最后一天，如果不是，将该月的日期(从1号到该日期之间的)加入到dayList中
			while(DateUtils.compareDate(edate_D, edate_M)>=0){
				String date = DateUtils.formatDateY_M_D(edate_D);
				dayList.add(date);
				edate_D=DateUtils.addDay(edate_D, -1);
			}
			edate_M=DateUtils.addMonth(edate_M, -1);//将结束月份-1，作为新的结束时间
		}
		
		if(DateUtils.compareDate(edate_M, sdate_M)<0){//如果结束日期的月份小于开始时间的月份,return;
			return map;
		}
		if(DateUtils.compareDate(edate_M, sdate_M)==0){//如果结束日期的月份等于开始时间的月份
			monthList.add(DateUtils.formatDateY_M(edate_M));//将结束日期的月份(或开始日期的月份)加入到monthList中
			return map;
		}
		
		String newDate = DateUtils.formatDateY_M_D(sdate_M);//新的开始时间
		Date sdate_Y = DateUtils.parseDateY(newDate);//获取某年的1月1日（即第一天）
		//判断开始时间newDate是否为某年的第一月，如果不是，将这一年的这个月之后的月份加入monthList中
		if(DateUtils.compareDate(sdate_M,sdate_Y)>0){
			Date nextYear = DateUtils.addYear(sdate_Y, 1);
			while(DateUtils.compareDate(sdate_M, nextYear)<0){
				if(DateUtils.compareDate(sdate_M, edate_M)>0){
					return map;
				}
				String date = DateUtils.formatDateY_M(sdate_M);
				monthList.add(date);
				sdate_M=DateUtils.addMonth(sdate_M, 1);
			}
			sdate_Y=nextYear;//将开始年份的下一年作为新的开始时间
		}
		
		c1.setTime(edate_M);
		int j = c1.get(Calendar.YEAR);//获取年份
		if(c1.get(Calendar.MONTH)<11){//判断结束时间是否是某年的最后一月，如果不是，将这个月之前的月份加入monthList中
			Date date = DateUtils.parseDateY(j+"");
			while(DateUtils.compareDate(edate_M, date)>=0){
				String m = DateUtils.formatDateY_M(edate_M);
				monthList.add(m);
				edate_M=DateUtils.addMonth(edate_M, -1);
			}
			edate_M = DateUtils.addMonth(date, -1);//将结束年份的上一年作为新的结束时间
		}
		while(DateUtils.compareDate(edate_M, sdate_Y)>=0){
			String y = DateUtils.formatDateY(edate_M);
			yearList.add(y);
			edate_M = DateUtils.addYear(edate_M, -1);
		}
		
		
		
		return map;
	}
	
	/**
	 * 测试getDate的方法
	 */
//	@Test
//	public  void getT(){
//		String startDate = "2008-06-08";
//		String endDate = "2013-05-18";
//		Map<String, List<String>> map = getDate(startDate, endDate);
//		List<String> yearList = map.get("year");
//		List<String> monthList = map.get("month");
//		List<String> dayList = map.get("day");
//		System.out.println("************年***********");
//		if(yearList.size()>0){
//			for (String s : yearList) {
//				System.out.println(s);
//			}
//		}
//		System.out.println("*********月***********");
//		if(monthList.size()>0){
//			for (String s : monthList) {
//				System.out.println(s);
//			}
//		}
//		System.out.println("**********日************");
//		if(dayList.size()>0){
//			for (String s : dayList) {
//				System.out.println(s);
//			}
//		}
//	}
}
