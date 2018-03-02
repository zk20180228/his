package cn.honry.inner.statistics.operationIncome.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.operationIncome.dao.InnerOperationIncomeDao;
import cn.honry.inner.statistics.operationIncome.vo.InnerOperationIncomeVo;
import cn.honry.inner.statistics.operationNum.dao.InnerOperationNumDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("innerOperationIncomeDao")
@SuppressWarnings("all")
public class InnerOperationIncomeDaoImpl  extends  HibernateEntityDao<InnerOperationIncomeVo>  implements InnerOperationIncomeDao{
	private final String[] itemList={"T_INPATIENT_ITEMLIST_NOW","T_INPATIENT_ITEMLIST"};//住院非药品明细表
	private final String[] feeList={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};//门诊费用
	private final String ZY="ZY";
	private final String MZ="MZ";
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value="parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	@Autowired
	@Qualifier(value="innerOperationNumDao")
	private InnerOperationNumDao innerOperationNumDao;
	
	/**  
	 * 
	 * 在线更新返回分区表(住院非药品明细表或门诊费用明细）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime  开始时间
	 * @param endTime 结束时间
	 * @param tables String[]表名
	 * @param type MZ ZY 门诊表或住院表
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<String> getTableTnl(String begin, String end,String[] tables, String flg) {
		if(!"".equals(begin)&&!"".equals(end)){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date sTime = DateUtils.parseDateY_M_D(begin);//当天
			Date eTime = DateUtils.parseDateY_M_D(end);
			List<String> tnL = null;
			try {
				String dateNum;
				if(MZ.equals(flg)){
					dateNum=parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				}else{
					dateNum = parameterInnerDAO.getParameterByCode("saveTime");
					if(dateNum.equals("1")){
						dateNum="30";
					}
				}
				
				//3.获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,tables[1],begin,end);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,tables[1],yNum+1);
						tnL.add(0,tables[0]);
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add(tables[0]);
				}
				} catch (Exception e) {
					tnL = new ArrayList<String>();
				}
			return tnL;
			}
		return null;
	}
	
	
	/**  
	 * 
	 * 获取结束时间方法
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	public String returnEndTime(String date){
		String end=null;
		date=date.substring(0,7)+"-01";
		Calendar ca=Calendar.getInstance(Locale.CHINESE);
		try {
			ca.setTime(df.parse(date));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			end=df.format(ca.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}
	
	/**  
	 * 
	 * 初始化手术收入统计（门诊+住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeMZ(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> itemTnL=getTableTnl(begin, end, itemList, ZY);
			List<String> feeTnL=getTableTnl(begin, end, feeList, MZ);
			List<InnerOperationIncomeVo> list=null;
			if(itemTnL!=null&&feeTnL!=null){
				StringBuffer buffer=new StringBuffer();
				buffer.append("select sum(nvl(outInSum1,0)) as outInSum , finalDate,classType from( ");
				for(int i=0;i<feeTnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append(" select  t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'yyyy-mm-dd') as finalDate ,cast('MZ01' as varchar(8)) AS classType ");
					buffer.append(" from "+feeTnL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10'");
					buffer.append(" where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".trans_type = 1 and t"+i+".pay_flag = 1 ");
					buffer.append(" and t"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				buffer.append(" union All ");
				for(int i=0;i<itemTnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append(" select n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'yyyy-mm-dd') as finalDate,cast('ZY01' as varchar(8)) AS classType ");
					buffer.append(" from "+itemTnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10'");
					buffer.append(" where n"+i+".trans_type = 1 and  n"+i+".SEND_FLAG = 1 and  ");
					buffer.append(" n"+i+".SEND_FLAG = 1 and  n"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				buffer.append(") group by finalDate,classType ");
				 
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("outInSum",Hibernate.DOUBLE).addScalar("finalDate").addScalar("classType",Hibernate.STRING)
							.setResultTransformer(Transformers.aliasToBean(InnerOperationIncomeVo.class)).list();
				 
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERINCOMEMZ_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationIncomeVo vo:list){
						 if(vo.getOutInSum()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("classType", vo.getClassType());//类别
								obj.append("outInSum", vo.getOutInSum());//金额
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERINCOMEMZ_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperIncomeMZYearOrMonth(menuAlias,"2",date);
						initOperIncomeMZYearOrMonth(menuAlias,"3",date);
					}
				}
				 innerOperationNumDao.saveMongoLog(beginDate, menuAlias+"_OPERINCOMEMZ_DAY", list, date);
			}
		}
		
	}

	@Override
	public void initOperIncomeMZYearOrMonth(String menuAlias, String type,
			String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期例数 来源
		String temp;//月数据
		Double outInSum;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERINCOMEMZ_DAY";
			saveMongo=menuAlias+"_OPERINCOMEMZ_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERINCOMEMZ_MONTH";
			saveMongo=menuAlias+"_OPERINCOMEMZ_YEAR";
		}
		
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			
		    data1.append("finalDate", new BasicDBObject("$gte",begin));
		    data2.append("finalDate", new BasicDBObject("$lte",end));
		    dateList.add(data1);
		    dateList.add(data2);
		    bdObject.put("$and", dateList);
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("outInSum") ;//金额
				 String name = (String) dbCursor.get("classType");//类别
				 temp1=temp+"&"+name;
				 if(map.containsKey(temp1)){//如果key存在 比较name
					 outInSum=map.get(temp1);
					 outInSum+=value;
					 map.put(temp1,outInSum);
				 }else{//如果key不存在   添加到map1中
					 map.put(temp1, value);
				 }
			}
			DBObject query = new BasicDBObject();
			query.put("finalDate", temp);//移除数据条件
			new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
			
			List<DBObject> numsList = new ArrayList<DBObject>();
			String[] stArr=null;
			for(String key:map.keySet()){
				stArr=key.split("&");
				BasicDBObject obj = new BasicDBObject();
				obj.append("outInSum",map.get(key));
				obj.append("classType", stArr[1]);
				obj.append("finalDate", stArr[0]);
				numsList.add(obj);
			}
			map=null;
			new MongoBasicDao().insertDataByList(saveMongo, numsList);
			//保存操作日志
			innerOperationNumDao.saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（手术类别）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeType(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> itemTnL=getTableTnl(begin, end, itemList, ZY);
			List<String> feeTnL=getTableTnl(begin, end, feeList, MZ);
			List<InnerOperationIncomeVo> list=null;
			if(itemTnL!=null&&feeTnL!=null){
				StringBuffer buffer=new StringBuffer();
				buffer.append("select encode as operationTypeCode  ,name as operationTypeName,finalDate ,sum(nvl(outInSum1,0)) as outInSum  from ( ");
				for(int i=0;i<feeTnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append(" select t.code_encode as encode,t.code_name as name, t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate ");
					buffer.append("  from T_OPERATION_APPLY app  ");
					buffer.append(" left join  "+feeTnL.get(i)+" t"+i+" on t"+i+".clinic_code = app.clinic_code  and t"+i+".pay_flag = '1' and t"+i+".trans_type = 1  and t"+i+".del_flg = 0 and t"+i+".stop_flg = 0");
					buffer.append("  and  t"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					buffer.append(" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10' ");
					buffer.append(" inner join  t_business_dictionary t on app.op_type =t.code_encode   and t.code_type = 'operatetype' ");
					buffer.append(" where  app.stop_flg = 0 and app.del_flg = 0 and app.PASOURCE=1 ");
				}
				buffer.append(" union All ");
				for(int i=0;i<itemTnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append(" select t.code_encode as encode,t.code_name as name, n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'yyyy-MM-dd') as finalDate ");
					buffer.append(" from  T_OPERATION_APPLY app  ");
					buffer.append(" left join  "+itemTnL.get(i)+" n"+i+" on  n"+i+".inpatient_no = app.clinic_code  and  n"+i+".send_flag=1 and  n"+i+".trans_type = 1 ");
					buffer.append(" and n"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					buffer.append(" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10' ");
					buffer.append(" inner join t_business_dictionary t on app.op_type = t.code_encode  and t.code_type = 'operatetype'  ");
					buffer.append(" where app.stop_flg = 0 and app.del_flg = 0 and app.PASOURCE=2 ");
				}
				buffer.append(") group by encode,name,finalDate ");
				 
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("outInSum",Hibernate.DOUBLE).addScalar("finalDate").addScalar("operationTypeCode").addScalar("operationTypeName")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationIncomeVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERINCOMETYPE_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationIncomeVo vo:list){
						 if(vo.getOutInSum()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("operationTypeCode", vo.getOperationTypeCode());//类别
								obj.append("operationTypeName", vo.getOperationTypeName());//名称
								obj.append("outInSum", vo.getOutInSum());//金额
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERINCOMETYPE_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperIncomeTypeYearOrMonth(menuAlias,"2",date);
						initOperIncomeTypeYearOrMonth(menuAlias,"3",date);
					}
				}
				 innerOperationNumDao.saveMongoLog(beginDate, menuAlias+"_OPERINCOMETYPE_DAY", list, date);
			}
		}
		
	}


	/**  
	 * 
	 * 初始化手术收入统计（手术类别）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeTypeYearOrMonth(String menuAlias, String type,
			String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期例数 来源
		String temp;//月数据
		Double outInSum;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERINCOMETYPE_DAY";
			saveMongo=menuAlias+"_OPERINCOMETYPE_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERINCOMETYPE_MONTH";
			saveMongo=menuAlias+"_OPERINCOMETYPE_YEAR";
		}
		
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		
	    data1.append("finalDate", new BasicDBObject("$gte",begin));
	    data2.append("finalDate", new BasicDBObject("$lte",end));
	    dateList.add(data1);
	    dateList.add(data2);
	    bdObject.put("$and", dateList);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("outInSum") ;//金额
			 String code = (String) dbCursor.get("operationTypeCode");//类别
			 String name = (String) dbCursor.get("operationTypeName");//类别
			 temp1=temp+"&"+code+"&"+name;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 outInSum=map.get(temp1);
				 outInSum+=value;
				 map.put(temp1,outInSum);
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, value);
			 }
		}
		DBObject query = new BasicDBObject();
		query.put("finalDate", temp);//移除数据条件
		new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
		
		List<DBObject> numsList = new ArrayList<DBObject>();
		String[] stArr=null;
		for(String key:map.keySet()){
			stArr=key.split("&");
			BasicDBObject obj = new BasicDBObject();
			obj.append("outInSum",map.get(key));
			obj.append("operationTypeCode", stArr[1]);
			obj.append("operationTypeName", stArr[2]);
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		//保存操作日志
		innerOperationNumDao.saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（科室）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeDept(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> itemTnL=getTableTnl(begin, end, itemList, ZY);
			List<String> feeTnL=getTableTnl(begin, end, feeList, MZ);
			List<InnerOperationIncomeVo> list=null;
			if(itemTnL!=null&&feeTnL!=null){
				 StringBuffer buffer=new StringBuffer();
				 buffer.append(" select  f.finalDate as finalDate,f.totCost as outInSum,(select e.dept_name from t_department e where e.dept_code=f.execDept  ) as deptName, f.execDept AS deptCode from( ");
				 buffer.append(" select sum(nvl(tot_cost, 0)) as totCost, execDept,  finalDate  from (");
				 for(int i=0;i<itemTnL.size();i++){
						if(i>0){
							buffer.append(" Union All ");
						}
						 buffer.append("select t"+i+".tot_cost, t"+i+".execute_deptcode as execDept,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate  from  "+itemTnL.get(i)+" t"+i);
						 buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code and c.report_code = 'ZY01'  and c.fee_stat_code = '10'  where  t"+i+".send_flag = 1  and t"+i+".trans_type = 1 ");
						 buffer.append(" and t"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					}
					buffer.append(" union All ");
					for(int i=0;i<feeTnL.size();i++){
						if(i>0){
							buffer.append(" Union All ");
						}
						 buffer.append(" select n"+i+".tot_cost,n"+i+".exec_dpcd as execDept,to_char(n"+i+".fee_date,'yyyy-MM-dd')  as finalDate  from "+feeTnL.get(i)+" n"+i+" join t_charge_minfeetostat c   on c.minfee_code = n"+i+".fee_code");
						 buffer.append(" and c.report_code = 'MZ01' and c.fee_stat_code = '10' where n"+i+".del_flg = 0 and n"+i+".stop_flg = 0  and n"+i+".pay_flag = 1 and n"+i+".trans_type = 1");
						 buffer.append(" and n"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					}
				 buffer.append("  )group by execDept, finalDate ) f ");
				 
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("outInSum",Hibernate.DOUBLE).addScalar("finalDate").addScalar("deptCode").addScalar("deptName")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationIncomeVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERINCOMEDEPT_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationIncomeVo vo:list){
						 if(vo.getOutInSum()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("deptCode", vo.getDeptCode());//CODE
								obj.append("deptName", vo.getDeptName());//名称
								obj.append("outInSum", vo.getOutInSum());//金额
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERINCOMEDEPT_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperIncomeDeptYearOrMonth(menuAlias,"2",date);
						initOperIncomeDeptYearOrMonth(menuAlias,"3",date);
					}
				}
				 innerOperationNumDao.saveMongoLog(beginDate, menuAlias+"_OPERINCOMEDEPT_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（科室）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeDeptYearOrMonth(String menuAlias, String type,
			String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期例数 来源
		String temp;//月数据
		Double outInSum;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERINCOMEDEPT_DAY";
			saveMongo=menuAlias+"_OPERINCOMEDEPT_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERINCOMEDEPT_MONTH";
			saveMongo=menuAlias+"_OPERINCOMEDEPT_YEAR";
		}
		
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		
	    data1.append("finalDate", new BasicDBObject("$gte",begin));
	    data2.append("finalDate", new BasicDBObject("$lte",end));
	    dateList.add(data1);
	    dateList.add(data2);
	    bdObject.put("$and", dateList);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("outInSum") ;//金额
			 String code = (String) dbCursor.get("deptCode");//类别
			 String name = (String) dbCursor.get("deptName");//类别
			 temp1=temp+"&"+code+"&"+name;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 outInSum=map.get(temp1);
				 outInSum+=value;
				 map.put(temp1,outInSum);
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, value);
			 }
		}
		DBObject query = new BasicDBObject();
		query.put("finalDate", temp);//移除数据条件
		new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
		
		List<DBObject> numsList = new ArrayList<DBObject>();
		String[] stArr=null;
		for(String key:map.keySet()){
			stArr=key.split("&");
			BasicDBObject obj = new BasicDBObject();
			obj.append("outInSum",map.get(key));
			obj.append("deptCode", stArr[1]);
			obj.append("deptName", stArr[2]);
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		//保存操作日志
		innerOperationNumDao.saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	
	/**  
	 * 
	 * 初始化手术收入统计（医生）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeDoc(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> itemTnL=getTableTnl(begin, end, itemList, ZY);
			List<String> feeTnL=getTableTnl(begin, end, feeList, MZ);
			List<InnerOperationIncomeVo> list=null;
			if(itemTnL!=null&&feeTnL!=null){
				StringBuffer buffer=new StringBuffer();
				 buffer.append(" select  f.finalDate as finalDate,f.totCost as outInSum,(select e.employee_name from t_employee e where e.employee_jobno=f.docCode  ) as docName,f.docCode AS docCode from( ");
				 buffer.append(" select sum(nvl(tot_cost, 0)) as totCost, docCode,  finalDate  from (");
				 for(int i=0;i<itemTnL.size();i++){
						if(i>0){
							buffer.append(" Union All ");
						}
						 buffer.append("select t"+i+".tot_cost, t"+i+".RECIPE_DOCCODE as docCode,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate  from  "+itemTnL.get(i)+" t"+i);
						 buffer.append(" join t_charge_minfeetostat c on c.minfee_code = t"+i+".fee_code and c.report_code = 'ZY01'  and c.fee_stat_code = '10'  where  t"+i+".send_flag = 1  and t"+i+".trans_type = 1 ");
						 buffer.append(" and t"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					}
					buffer.append(" union All ");
					for(int i=0;i<feeTnL.size();i++){
						if(i>0){
							buffer.append(" Union All ");
						}
						 buffer.append(" select n"+i+".tot_cost,n"+i+".DOCT_CODE as docCode,to_char(n"+i+".fee_date,'yyyy-MM-dd')  as finalDate  from "+feeTnL.get(i)+" n"+i+" join t_charge_minfeetostat c   on c.minfee_code = n"+i+".fee_code");
						 buffer.append(" and c.report_code = 'MZ01' and c.fee_stat_code = '10' where n"+i+".del_flg = 0 and n"+i+".stop_flg = 0  and n"+i+".pay_flag = 1 and n"+i+".trans_type = 1");
						 buffer.append(" and n"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					}
					buffer.append("  )group by docCode, finalDate ) f ");
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("outInSum",Hibernate.DOUBLE).addScalar("finalDate").addScalar("docCode").addScalar("docName")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationIncomeVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERINCOMEDOC_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationIncomeVo vo:list){
						 if(vo.getOutInSum()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("docCode", vo.getDocCode());//CODE
								obj.append("docName", vo.getDocName());//名称
								obj.append("outInSum", vo.getOutInSum());//金额
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERINCOMEDOC_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperIncomeDocYearOrMonth(menuAlias,"2",date);
						initOperIncomeDocYearOrMonth(menuAlias,"3",date);
					}
				}
				 innerOperationNumDao.saveMongoLog(beginDate, menuAlias+"_OPERINCOMEDOC_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（医生）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeDocYearOrMonth(String menuAlias, String type,
			String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期例数 来源
		String temp;//月数据
		Double outInSum;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERINCOMEDOC_DAY";
			saveMongo=menuAlias+"_OPERINCOMEDOC_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERINCOMEDOC_MONTH";
			saveMongo=menuAlias+"_OPERINCOMEDOC_YEAR";
		}
		
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		
	    data1.append("finalDate", new BasicDBObject("$gte",begin));
	    data2.append("finalDate", new BasicDBObject("$lte",end));
	    dateList.add(data1);
	    dateList.add(data2);
	    bdObject.put("$and", dateList);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("outInSum") ;//金额
			 String code = (String) dbCursor.get("docCode");//类别
			 String name = (String) dbCursor.get("docName");//类别
			 temp1=temp+"&"+code+"&"+name;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 outInSum=map.get(temp1);
				 outInSum+=value;
				 map.put(temp1,outInSum);
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, value);
			 }
		}
		DBObject query = new BasicDBObject();
		query.put("finalDate", temp);//移除数据条件
		new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
		
		List<DBObject> numsList = new ArrayList<DBObject>();
		String[] stArr=null;
		for(String key:map.keySet()){
			stArr=key.split("&");
			BasicDBObject obj = new BasicDBObject();
			obj.append("outInSum",map.get(key));
			obj.append("docCode", stArr[1]);
			obj.append("docName", stArr[2]);
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		//保存操作日志
		innerOperationNumDao.saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	/**  
	 * 
	 * 初始化手术收入统计（同环比）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeYoyRatio(String menuAlias, String type,
			String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> itemTnL=getTableTnl(begin, end, itemList, ZY);
			List<String> feeTnL=getTableTnl(begin, end, feeList, MZ);
			List<InnerOperationIncomeVo> list=null;
			if(itemTnL!=null&&feeTnL!=null){
				StringBuffer buffer=new StringBuffer();
				buffer.append("select sum(nvl(outInSum1,0)) as outInSum , finalDate from( ");
				for(int i=0;i<feeTnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append(" select  t"+i+".tot_cost outInSum1,to_char(t"+i+".fee_date,'yyyy-MM-dd') as finalDate ");
					buffer.append(" from "+feeTnL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code and c.report_code='MZ01' and c.fee_stat_code = '10'");
					buffer.append(" where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".trans_type = 1 and t"+i+".pay_flag = 1 and  t"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				buffer.append(" union All ");
				for(int i=0;i<itemTnL.size();i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append(" select n"+i+".tot_cost as outInSum1,to_char(n"+i+".fee_date,'yyyy-MM-dd') as finalDate  ");
					buffer.append(" from "+itemTnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code and c.report_code='ZY01' and c.fee_stat_code = '10'");
					buffer.append(" where  n"+i+".trans_type = 1 and n"+i+".SEND_FLAG = 1 and  n"+i+".fee_date between to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
				}
				buffer.append(") group by finalDate ");
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("outInSum",Hibernate.DOUBLE).addScalar("finalDate")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationIncomeVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERINCOMEYOYRATIO_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationIncomeVo vo:list){
						 if(vo.getOutInSum()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("outInSum", vo.getOutInSum());//金额
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERINCOMEYOYRATIO_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperIncomeYoyRatioYearOrMonth(menuAlias,"2",date);
						initOperIncomeYoyRatioYearOrMonth(menuAlias,"3",date);
					}
				}
				 innerOperationNumDao.saveMongoLog(beginDate, menuAlias+"_OPERINCOMEYOYRATIO_DAY", list, date);
			}
		}
		
	}


	/**  
	 * 
	 * 初始化手术收入统计（同环比）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperIncomeYoyRatioYearOrMonth(String menuAlias,
			String type, String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();//保存日期例数 来源
		String temp;//月数据
		Double outInSum;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERINCOMEYOYRATIO_DAY";
			saveMongo=menuAlias+"_OPERINCOMEYOYRATIO_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERINCOMEYOYRATIO_MONTH";
			saveMongo=menuAlias+"_OPERINCOMEYOYRATIO_YEAR";
		}
		
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		
	    data1.append("finalDate", new BasicDBObject("$gte",begin));
	    data2.append("finalDate", new BasicDBObject("$lte",end));
	    dateList.add(data1);
	    dateList.add(data2);
	    bdObject.put("$and", dateList);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("outInSum") ;//金额
			 temp1=temp;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 outInSum=map.get(temp1);
				 outInSum+=value;
				 map.put(temp1,outInSum);
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, value);
			 }
		}
		DBObject query = new BasicDBObject();
		query.put("finalDate", temp);//移除数据条件
		new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
		
		List<DBObject> numsList = new ArrayList<DBObject>();
		String[] stArr=null;
		for(String key:map.keySet()){
			stArr=key.split("&");
			BasicDBObject obj = new BasicDBObject();
			obj.append("outInSum",map.get(key));
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		//保存操作日志
		innerOperationNumDao.saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}


	/**  
	 * 
	 * 初始化手术类别
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void initOperOpTypeToDB() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.code_encode as encode,t.code_name as name,t.code_type as type from t_business_dictionary t  where t.code_type = 'operatetype'  and t.del_flg=0 and t.stop_flg=0 ");
		List<BusinessDictionary> list =  list=super.getSession().createSQLQuery(buffer.toString())
				.addScalar("encode").addScalar("Name").addScalar("type")
				.setResultTransformer(Transformers.aliasToBean(BusinessDictionary.class)).list();
		if(list!=null && list.size()>0){
			 List<DBObject> numsList = new ArrayList<DBObject>();
			 for(BusinessDictionary vo:list){
				 if(StringUtils.isNotBlank(vo.getName())){
						BasicDBObject obj = new BasicDBObject();
						obj.append("encode", vo.getEncode());
						obj.append("name", vo.getName());
						obj.append("type", vo.getType());
						numsList.add(obj);
					}
			}
			if(numsList.size()>0){
				new MongoBasicDao().deleteData("OPERATIONTYPE");//删除原来的数据
				if(!new MongoBasicDao().isCollection("OPERATIONTYPE")){
					new MongoBasicDao().insertDataByList("OPERATIONTYPE", numsList);
				}
			}		
		}
	}


}
