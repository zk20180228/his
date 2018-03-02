package cn.honry.statistics.bi.bistac.imStacData.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.imStacData.service.InMonthTableService;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.dao.ItemVoDao;
import cn.honry.utils.DateUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service("inMonthTableService")
@Transactional
@SuppressWarnings({ "all" })
public class InMonthTableServiceImpl implements InMonthTableService{
	@Autowired
	@Qualifier(value = "itemVoDao")
	private ItemVoDao itemVoDao;
	private MongoBasicDao mbDao = null;
	public static final String TABLENAME1_1 = "YZB_1";//药占比(天)
	public static final String TABLENAME1 = "T_YZB";//药占比(月)
	public static final String TABLENAME2_1 = "YYTS_1";//门诊用药天数(天)
	public static final String TABLENAME2 = "T_YYTS";//门诊用药天数(月)
	public static final String TABLENAME3_1 = "YSYYJE_1";//门诊医生用药金额表(天)
	public static final String TABLENAME3 = "T_YSYYJE";//门诊医生用药金额表(月)
	public static final String TABLENAME4_1 = "KSYYJE_1";//门诊科室用药金额表(天)
	public static final String TABLENAME4 = "T_KSYYJE";//门诊科室用药金额表(月)
	public static final String TABLENAME6_1 = "YPJE_11";//门诊月药品金额，用药数量，人次表（修改）(天)
	public static final String TABLENAME6 = "T_YPJE";//门诊月药品金额，用药数量，人次表（修改）(月)
	public static final String TABLENAME5 = "T_ZYSRTJ";//将住院的药品和非药品表导入mongodb(月)
	private static final String Integer = null;

	/**  
	 * 
	 * 将处方明细表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月23日 下午8:14:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月23日 下午8:14:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableDataM(String begin, String end) {
		mbDao = new MongoBasicDao();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				Double drugFee = 0.0d;
				Double totCost = 0.0d;
				Integer total= 0;
				BasicDBObject bdObject = null;
				Document document1 = new Document();
				document1.append("selectTime", newMonth);
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", format2.format(format2.parseObject(newMonth+"-"+j)));
					DBCursor cursor = mbDao.findAlldata(TABLENAME1_1, bdObject);
					while(cursor.hasNext()){
						DBObject dbCursor = cursor.next();
						drugFee = (Double) dbCursor.get("drugFee")+drugFee;
						totCost = (Double) dbCursor.get("totCost")+totCost;
						total=(Integer) dbCursor.get("total")+total;	
					}
				}
				Document document = new Document();
				document.append("selectTime", newMonth);
				document.append("totCost", totCost);
				document.append("drugFee", drugFee);
				document.append("total", total);
				mbDao.update(TABLENAME1, document1,document,true);
				
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 上午10:50:15 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 上午10:50:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableDataM_YYTS(String begin, String end) {
		mbDao = new MongoBasicDao();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				Double days = 0.0d;
				Integer total= 0;
				BasicDBObject bdObject = null;
				Document document1 = new Document();
				document1.append("selectTime", newMonth);
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", format2.format(format2.parseObject(newMonth+"-"+j)));
					DBCursor cursor = mbDao.findAlldata(TABLENAME2_1, bdObject);
					while(cursor.hasNext()){
						DBObject dbCursor = cursor.next();
						days += (Double) dbCursor.get("days");
						total+=(Integer) dbCursor.get("total");	
					}
				}
				Document document = new Document();
				document.append("selectTime", newMonth);
				if (total==0) {
					document.append("avgDays", 0);
				}else{
					document.append("avgDays", days/total);
				}
				mbDao = new MongoBasicDao();
				mbDao.update(TABLENAME2, document1,document,true);
				
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将前5名医生用药金额表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 上午11:44:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 上午11:44:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableDataM_YSYYJE(String begin, String end) {
		try {
			mbDao = new MongoBasicDao();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				BasicDBObject bdObject = null;
				Map<String,List<Double>> map=new HashMap<String, List<Double>>();
				List<Double> list=null;
				Document document1 = null;
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", format2.format(format2.parseObject(newMonth+"-"+j)));
					DBCursor cursor = mbDao.findAlldata(TABLENAME3_1, bdObject);
					while(cursor.hasNext()){
						DBObject dbCursor = cursor.next();
						Double totCost=(Double) dbCursor.get("totCost");	
						Double num=(Double) dbCursor.get("num");
						String doctCodeName=(String)dbCursor.get("doctCodeName");
						boolean flag = map.containsKey(doctCodeName);
						if (flag==true) {
							list=new ArrayList<Double>();
							List<Double> dList = map.get(doctCodeName);
							list.add(dList.get(0)+totCost);
							list.add(dList.get(1)+num);
							map.put(doctCodeName, list);
						}else{
							list=new ArrayList<Double>();
							list.add(totCost);
							list.add(num);
							map.put(doctCodeName, list);
						}
					}
				}
				Iterator it = map.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					document1 = new Document();
					document1.append("selectTime", newMonth);
					document1.append("doctCodeName", entry.getKey());
					
					Document document = new Document();
					document.append("selectTime", newMonth);
					document.append("doctCodeName",entry.getKey());
					document.append("totCost", map.get(entry.getKey()).get(0) );
					document.append("num", map.get(entry.getKey()).get(1));
					mbDao = new MongoBasicDao();
					mbDao.update(TABLENAME3, document1,document,true);
				}
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将前5名科室用药金额表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 下午12:41:47 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 下午12:41:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableDataM_KSYYJE(String begin, String end) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				BasicDBObject bdObject = null;
				Map<String,List<Double>> map=new HashMap<String, List<Double>>();
				List<Double> list=null;
				Document document1 = null;
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", format2.format(format2.parseObject(newMonth+"-"+j)));
					mbDao = new MongoBasicDao();
					DBCursor cursor = mbDao.findAlldata(TABLENAME4_1, bdObject);
					while(cursor.hasNext()){
						DBObject dbCursor = cursor.next();
						Double totCost=(Double) dbCursor.get("totCost");	
						Double num=(Double) dbCursor.get("num");
						String regDpcdName=(String)dbCursor.get("regDpcdName");
						boolean flag = map.containsKey(regDpcdName);
						if (flag==true) {
							list=new ArrayList<Double>();
							List<Double> dList = map.get(regDpcdName);
							list.add(dList.get(0)+totCost);
							list.add(dList.get(1)+num);
							map.put(regDpcdName, list);
						}else{
							list=new ArrayList<Double>();
							list.add(totCost);
							list.add(num);
							map.put(regDpcdName, list);
						}
						}
					}
				Iterator it = map.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					document1 = new Document();
					document1.append("selectTime", newMonth);
					document1.append("regDpcdName", entry.getKey());
					
					Document document = new Document();
					document.append("selectTime", newMonth);
					document.append("regDpcdName",entry.getKey());
					document.append("totCost", map.get(entry.getKey()).get(0) );
					document.append("num", map.get(entry.getKey()).get(1));
					mbDao = new MongoBasicDao();
					mbDao.update(TABLENAME4, document1,document,true);
				}
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月24日 下午2:24:59 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月24日 下午2:24:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableDataM_YPJE2(String begin, String end) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				BasicDBObject bdObject = null;
				Map<String,List<Object>> map=new HashMap<String, List<Object>>();
				List<Object> list=null;
				Document document1 = null;
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", format2.format(format2.parseObject(newMonth+"-"+j)));
					mbDao = new MongoBasicDao();
					DBCursor cursor = mbDao.findAlldata(TABLENAME6_1, bdObject);
					while(cursor.hasNext()){
						DBObject dbCursor = cursor.next();
						Double totCost=(Double) dbCursor.get("totCost");	
						Double num=(Double) dbCursor.get("num");
						Integer total=(Integer) dbCursor.get("total");
						String type=(String)dbCursor.get("type");
						boolean flag = map.containsKey(type);
						if (flag==true) {
							list=new ArrayList<Object>();
							List<Object> dList = map.get(type);
							list.add((Double)dList.get(0)+totCost);
							list.add((Integer)dList.get(1)+total);
							list.add((Double)dList.get(2)+num);
							map.put(type, list);
						}else{
							list=new ArrayList<Object>();
							list.add(totCost);
							list.add(total);
							list.add(num);
							map.put(type, list);
						}
					}
				}
				Iterator it = map.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					document1 = new Document();
					document1.append("selectTime", newMonth);
					document1.append("type", entry.getKey());
					
					Document document = new Document();
					document.append("selectTime", newMonth);
					document.append("type",entry.getKey());
					document.append("totCost", map.get(entry.getKey()).get(0) );
					document.append("total", map.get(entry.getKey()).get(1));
					document.append("num", map.get(entry.getKey()).get(2));
					mbDao = new MongoBasicDao();
					mbDao.update(TABLENAME6, document1,document,true);
				}
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将住院的药品和非药品表导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月27日 下午4:18:17 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月27日 下午4:18:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableDataM_DRUGANDNO(String begin, String end) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				BasicDBObject bdObject = null;
				Map<String,List<Double>> strMap=new HashMap<String, List<Double>>();
				List<Object> list=null;
				List<Double> dous=null;
				Document document1 = null;
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("name", format2.format(format2.parseObject(newMonth+"-"+j)));
					mbDao = new MongoBasicDao();
					DBCursor cursor1 = mbDao.findAlldata("YPSRFXHZ_DEPT_DAY", bdObject);
					mbDao = new MongoBasicDao();
					DBCursor cursor2 = mbDao.findAlldata("FYPSRFXHZ_DEPT_DAY", bdObject);
					while(cursor1.hasNext()){
						DBObject dbCursor = cursor1.next();
						boolean flag = strMap.containsKey((String)dbCursor.get("dept"));
						dous=new ArrayList<Double>();
						if (flag==true) {
							Double double1 = strMap.get((String)dbCursor.get("dept")).get(0);
							double1+=(Double) dbCursor.get("douValue");
							strMap.get((String)dbCursor.get("dept")).set(0, double1);
							strMap.get((String)dbCursor.get("dept")).set(1, 0.0);
						}else{
							dous=new ArrayList<Double>();
							dous.add((Double) dbCursor.get("douValue"));
							dous.add(0.0d);
							strMap.put((String)dbCursor.get("dept"),dous);
						}
						}
					while(cursor2.hasNext()){
						DBObject dbCursor = cursor2.next();
						boolean flag = strMap.containsKey((String)dbCursor.get("dept"));
						if (flag==true) {
							Double double1 = strMap.get((String)dbCursor.get("dept")).get(1);
							double1+=(Double) dbCursor.get("douValue");
							strMap.get((String)dbCursor.get("dept")).set(1, double1);
						}else{
							dous=new ArrayList<Double>();
							dous.add(0.0);
							dous.add((Double) dbCursor.get("douValue"));
							strMap.put((String)dbCursor.get("dept"),dous);
						}
					}
				}
				Iterator it = strMap.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					document1 = new Document();
					document1.append("selectTime", newMonth);
					document1.append("dept", entry.getKey());
					
					Document document = new Document();
					document.append("selectTime", newMonth);
					document.append("dept",entry.getKey());
					document.append("drugCost", strMap.get(entry.getKey()).get(0) );
					document.append("noDrugCost", strMap.get(entry.getKey()).get(1));
					mbDao = new MongoBasicDao();
					mbDao.update(TABLENAME5, document1,document,true);
				}
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb(月)
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午3:54:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午3:54:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_KSDBB(String begin, String end) {
		try {
//			List<FicDeptVO> depts = itemVoDao.queryFicDeptVO();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			int months = this.getMonth(begin, end);
			String newMonth = format.format(format.parse(end));//当前月
			for (int i = 0; i <= months; i++) {
				Date stime = format.parse(newMonth);
				Calendar a=Calendar.getInstance();
				a.setTime(stime); 
				a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
				int MaxDate=a.get(Calendar.DATE);
				Map<String, List<Object>> map=new HashMap<String, List<Object>>();
				List<Object> list=null;
				BasicDBObject bdObject = null;
				Document document1 = null;
				for (int j = 1; j <= MaxDate; j++) {
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", format2.format(format2.parseObject(newMonth+"-"+j)));
					mbDao = new MongoBasicDao();
					DBCursor cursor = mbDao.findAlldata("KSDBB", bdObject);
					while(cursor.hasNext()){
						DBObject dbCursor = cursor.next();
						String dept_name = (String)dbCursor.get("deptName");
						String dept_code = (String)dbCursor.get("deptCode");
						Integer ruYuNum = (Integer) dbCursor.get("ruYuNum");	
						Integer chuYUNum = (Integer) dbCursor.get("chuYUNum");
						Integer beds = (Integer) dbCursor.get("beds");
						Integer bedUsed = (Integer)dbCursor.get("bedUsed");
						Double avgInYuDays = (Double) dbCursor.get("avgInYuDays");
						Integer workNum = (Integer) dbCursor.get("workNum");
						Double menCost = (Double) dbCursor.get("menCost");
						Double zhuCost = (Double) dbCursor.get("zhuCost");
						boolean flag = map.containsKey(dept_code);
						if (flag==true) {
							list=new ArrayList<Object>();
							List<Object> dList = map.get(dept_code);
							list.add(dList.get(0));
							list.add(dList.get(1));
							list.add((Integer)dList.get(2)+ruYuNum);
							list.add((Integer)dList.get(3)+chuYUNum);
							list.add((Integer)dList.get(4)+beds);
							list.add((Integer)dList.get(5)+bedUsed);
							list.add((Double)dList.get(6)+avgInYuDays);
							list.add((Integer)dList.get(7)+workNum);
							list.add((Double)dList.get(8)+menCost);
							list.add((Double)dList.get(9)+zhuCost);
							map.put(dept_code, list);
						}else{
							list=new ArrayList<Object>();
							list.add(dept_name);
							list.add(dept_code);
							list.add(ruYuNum);
							list.add(chuYUNum);
							list.add(beds);
							list.add(bedUsed);
							list.add(avgInYuDays);
							list.add(workNum);
							list.add(menCost);
							list.add(zhuCost);
							map.put(dept_code, list);
						}
					}
				}
				Iterator it = map.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry)it.next();
					document1 = new Document();
					document1.append("selectTime", newMonth);
					document1.append("deptCode", entry.getKey());
					
					Document document = new Document();
					document.append("selectTime", newMonth);
					document.append("deptName",  map.get(entry.getKey()).get(0));
					document.append("deptCode", map.get(entry.getKey()).get(1));
					document.append("ruYuNum",  map.get(entry.getKey()).get(2));
					document.append("chuYUNum",  map.get(entry.getKey()).get(3));
					document.append("beds",  map.get(entry.getKey()).get(4));
					document.append("bedUsed",  map.get(entry.getKey()).get(5));
					document.append("avgInYuDays",  map.get(entry.getKey()).get(6));
					document.append("workNum",  map.get(entry.getKey()).get(7));
					document.append("menCost",  map.get(entry.getKey()).get(8));
					document.append("zhuCost",  map.get(entry.getKey()).get(9));
					mbDao = new MongoBasicDao();
					mbDao.update("T_KSDBB", document1,document,true);
				}
				newMonth=this.getLastMonth(newMonth);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
/***********************************************************************************************************/	
	
	/** 
	 * 获取两个日期相差几个月 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月23日 下午8:14:30 
	 * @param start 
	 * @param end 
	 * @return 
	 */  
	private int getMonth(String stime, String etime) {  
		Date start = DateUtils.parseDateY_M_D_H_M_S(stime);
		Date end = DateUtils.parseDateY_M_D_H_M_S(etime);
		if (start.after(end)) {  
			Date t = start;  
			start = end;  
			end = t;  
		}  
		Calendar startCalendar = Calendar.getInstance();  
		startCalendar.setTime(start);  
		Calendar endCalendar = Calendar.getInstance();  
		endCalendar.setTime(end);  
		Calendar temp = Calendar.getInstance();  
		temp.setTime(end);  
		temp.add(Calendar.DATE, 1);  
		
		int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);  
		int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);  
		
		if ((startCalendar.get(Calendar.DATE) == 1)&& (temp.get(Calendar.DATE) == 1)) {  
			return year * 12 + month + 1;  
		} else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {  
			return year * 12 + month;  
		} else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {  
			return year * 12 + month;  
		} else {  
			return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);  
		}  
	}
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
	private String getLastMonth(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
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
