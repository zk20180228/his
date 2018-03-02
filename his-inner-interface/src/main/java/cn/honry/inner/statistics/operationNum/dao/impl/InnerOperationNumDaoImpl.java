package cn.honry.inner.statistics.operationNum.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.operationNum.dao.InnerOperationNumDao;
import cn.honry.inner.statistics.operationNum.vo.InnerOperationNumsVo;
import cn.honry.inner.statistics.operationNum.vo.pcOperationWork;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("innerOperationNumDao")
@SuppressWarnings("all")
public class InnerOperationNumDaoImpl  extends  HibernateEntityDao<InnerOperationNumsVo>  implements InnerOperationNumDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	/**  
	 * 
	 * 手术例数（门诊、住院、介入）
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
	public void initOperNumsMZJ(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=getOperationRecordTnl(begin,end);
			List<InnerOperationNumsVo> list=null;
			if(tnL!=null&&tnL.size()>0){
				 StringBuffer buffer=new StringBuffer();
				 buffer.append(" select count(1) as nums, finalDate ,pasource from (");
				 for(int i=0,len=tnL.size();i<len;i++){
						if(i>0){
							buffer.append(" union all ");
						}
						buffer.append(" select t"+i+".pasource,  to_char(t"+i+".createtime, 'yyyy-MM-dd') as finalDate ");
						buffer.append(" from "+tnL.get(i)+" t"+i);
						buffer.append(" where t"+i+".ynvalid = 1 and t"+i+".stop_flg = 0  and t"+i+".del_flg = 0  and t"+i+".createtime between  ");
						buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS') ");
					}
				 buffer.append(" ) group by finalDate,pasource");
				 
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("nums",Hibernate.INTEGER).addScalar("finalDate").addScalar("pasource")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationNumsVo.class)).list();
				 
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERNUMSMZJ_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationNumsVo vo:list){
						 if(vo.getNums()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("pasource", vo.getPasource());//来源
								obj.append("nums", vo.getNums());//人数
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERNUMSMZJ_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperNumsMZJYearOrMonth(menuAlias,"2",date);
						initOperNumsMZJYearOrMonth(menuAlias,"3",date);
					}
				}
				saveMongoLog(beginDate, menuAlias+"_OPERNUMSMZJ_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 获取手术登记表的区表
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime  开始时间
	 * @param endTime 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<String> getOperationRecordTnl(String beginTime, String endTime) {
		List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",beginTime,endTime);
		if(tnL!=null&&tnL.size()>0){
			return tnL;
		}
		return new ArrayList<String>();
		
		
	}

	/**  
	 * 
	 * 保存日志
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date 开始时间
	 * @param menuAlias 模块名
	 * @param list 大小
	 * @param queryDate 在线更新时间
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public void saveMongoLog(Date date, String menuAlias, List list,
			String queryDate) {
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(date);
		int len=queryDate.length();
		Date d;
		if(4==len){
			d=DateUtils.parseDateY(queryDate);
		}else if(7==len){
			d=DateUtils.parseDateY_M(queryDate);
		}else{
			d=DateUtils.parseDateY_M_D(queryDate);
		}
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias);
		mong.setCountEndTime(new Date());
		mong.setTotalNum(list.size());
		mong.setCreateTime(new Date());
		innerRegisterInfoGzltjDao.save(mong);//保存日志
		
	}

	/**  
	 * 
	 * 手术例数（门诊、住院、介入） 年月
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
	public void initOperNumsMZJYearOrMonth(String menuAlias, String type,
			String date) {
        Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Integer> map=new HashMap<String,Integer>();//保存日期例数 来源
		String temp;//月数据
		Integer nums;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERNUMSMZJ_DAY";
			saveMongo=menuAlias+"_OPERNUMSMZJ_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERNUMSMZJ_MONTH";
			saveMongo=menuAlias+"_OPERNUMSMZJ_YEAR";
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
				 Integer value = (Integer) dbCursor.get("nums") ;//金额
				 String name = (String) dbCursor.get("pasource");//来源
				 temp1=temp+"&"+name;
				 if(map.containsKey(temp1)){//如果key存在 比较name
					 nums=map.get(temp1);
					 nums+=value;
					 map.put(temp1,nums);
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
				obj.append("nums",map.get(key));
				obj.append("pasource", stArr[1]);
				obj.append("finalDate", stArr[0]);
				numsList.add(obj);
			}
			map=null;
			new MongoBasicDao().insertDataByList(saveMongo, numsList);
			
			saveMongoLog(beginDate, saveMongo, numsList, date);
			
		
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
	 * 手术例数（手术类别）
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
	public void initOperNumsType(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=getOperationRecordTnl(begin,end);
			List<InnerOperationNumsVo> list=null;
			if(tnL!=null&&tnL.size()>0){
				 StringBuffer buffer=new StringBuffer();
				 buffer.append("select count(1) as nums, finalDate ,opType , b.code_name as opTypeName  from (");
				 for(int i=0,len=tnL.size();i<len;i++){
						if(i>0){
							buffer.append(" union all ");
						}
						buffer.append(" select  to_char(t"+i+".createtime, 'yyyy-MM-dd') as finalDate,t"+i+".ops_kind as opType ");
						buffer.append(" from "+tnL.get(i)+" t"+i+"  ");
						buffer.append(" where t"+i+".ynvalid = 1 and t"+i+".stop_flg = 0  and t"+i+".del_flg = 0  and t"+i+".createtime between ");
						buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS')");
					}
				 buffer.append(" ) ");
				 buffer.append("left join t_business_dictionary b on opType=b.code_encode  and b.stop_flg=0 and b.del_flg=0 and b.code_type= 'operatetype' ");
				 buffer.append(" group by finalDate,opType, b.code_name");
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("nums",Hibernate.INTEGER).addScalar("finalDate").addScalar("opType").addScalar("opTypeName")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationNumsVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERNUMSTYPE_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationNumsVo vo:list){
						 if(vo.getNums()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("opType", vo.getOpType());//类别code
								obj.append("opTypeName", vo.getOpTypeName());//类别name
								obj.append("nums", vo.getNums());//人数
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERNUMSTYPE_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperNumsTypeYearOrMonth(menuAlias,"2",date);
						initOperNumsTypeYearOrMonth(menuAlias,"3",date);
					}
				}
				saveMongoLog(beginDate, menuAlias+"_OPERNUMSTYPE_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 手术例数（手术类别）年月
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
	public void initOperNumsTypeYearOrMonth(String menuAlias, String type,
			String date) {
	        Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			Map<String,Integer> map=new HashMap<String,Integer>();//保存日期例数 来源
			String temp;//月数据
			Integer nums;
			String temp1;//key
			
			String begin=null;//开始时间
			String end=null;//结束时间
			String queryMongo=null;//查询的表
			String saveMongo=null;//保存的表
			if("2".equals(type)){
				//计算最后一个月日期
				temp=date.substring(0,7);
				begin=temp+"-01";//开始时间
				queryMongo=menuAlias+"_OPERNUMSTYPE_DAY";
				saveMongo=menuAlias+"_OPERNUMSTYPE_MONTH";
				end=returnEndTime(date);
			}else{
				temp=date.substring(0,4);
				begin=temp+"-01";
				end=temp+"-12";
				queryMongo=menuAlias+"_OPERNUMSTYPE_MONTH";
				saveMongo=menuAlias+"_OPERNUMSTYPE_YEAR";
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
				 Integer value = (Integer) dbCursor.get("nums") ;//金额
				 String opType = (String) dbCursor.get("opType");//code
				 String opTypeName = (String) dbCursor.get("opTypeName");//name
				 temp1=temp+"&"+opType+"&"+opTypeName;
				 if(map.containsKey(temp1)){//如果key存在 比较name
					 nums=map.get(temp1);
					 nums+=value;
					 map.put(temp1,nums);
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
				obj.append("nums",map.get(key));
				obj.append("opType", stArr[1]);
				obj.append("opTypeName", stArr[2]);
				obj.append("finalDate", stArr[0]);
				numsList.add(obj);
			}
			map=null;
			new MongoBasicDao().insertDataByList(saveMongo, numsList);
			saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	/**  
	 * 
	 * 手术例数（科室）
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
	public void initOperNumsDept(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=getOperationRecordTnl(begin,end);
			List<InnerOperationNumsVo> list=null;
			if(tnL!=null&&tnL.size()>0){
				 StringBuffer buffer=new StringBuffer();
				 buffer.append(" select d.dept_name as deptName, t3.exec_dept as deptCode, t3.createtime as finalDate, t3.num as nums from ");
				 buffer.append(" (select t2.exec_dept,t2.createtime, t2.num from (select t1.exec_dept, count(1) as num, t1.createtime from ( ");
				 for(int i=0,len=tnL.size();i<len;i++){
					 if(i>0){
						 buffer.append(" union all ");
					 }
					 buffer.append(" select to_char(t"+i+".createtime, 'yyyy-MM-dd') as createtime,t"+i+".exec_dept ");
					 buffer.append(" from "+tnL.get(i)+" t"+i+" ");
					 buffer.append(" where t"+i+".del_flg = 0  and t"+i+".stop_flg = 0 and t"+i+".ynvalid = '1' and t"+i+".createtime between ");
					 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS')");
				 }
				 buffer.append(" ) t1 group by t1.exec_dept, t1.createtime) t2) t3 ");
				 buffer.append(" left join t_department d on t3.exec_dept = d.dept_code  ");
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("nums",Hibernate.INTEGER).addScalar("finalDate").addScalar("deptCode").addScalar("deptName")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationNumsVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERNUMSDEPT_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationNumsVo vo:list){
						 if(vo.getNums()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("deptName", vo.getDeptName());//科室name
								obj.append("deptCode", vo.getDeptCode());//科室code
								obj.append("nums", vo.getNums());//人数
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERNUMSDEPT_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperNumsDeptYearOrMonth(menuAlias,"2",date);
						initOperNumsDeptYearOrMonth(menuAlias,"3",date);
					}
				}
				saveMongoLog(beginDate, menuAlias+"_OPERNUMSDEPT_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 手术例数（科室）年月
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
	public void initOperNumsDeptYearOrMonth(String menuAlias, String type,
			String date) {
        Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Integer> map=new HashMap<String,Integer>();//保存日期例数 来源
		String temp;//月数据
		Integer nums;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERNUMSDEPT_DAY";
			saveMongo=menuAlias+"_OPERNUMSDEPT_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERNUMSDEPT_MONTH";
			saveMongo=menuAlias+"_OPERNUMSDEPT_YEAR";
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
			 Integer value = (Integer) dbCursor.get("nums") ;//金额
			 String deptCode = (String) dbCursor.get("deptCode");//code
			 String deptName = (String) dbCursor.get("deptName");//name
			 temp1=temp+"&"+deptCode+"&"+deptName;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 nums=map.get(temp1);
				 nums+=value;
				 map.put(temp1,nums);
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
			obj.append("nums",map.get(key));
			obj.append("deptCode", stArr[1]);
			obj.append("deptName", stArr[2]);
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	/**  
	 * 
	 * 手术例数（医生）
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
	public void initOperNumsDoc(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=getOperationRecordTnl(begin,end);
			List<InnerOperationNumsVo> list=null;
			if(tnL!=null&&tnL.size()>0){
				 StringBuffer buffer=new StringBuffer();
				 buffer.append(" select e.employee_name as docName, t3.createtime as finalDate , t3.num as nums,t3.OPS_DOCD as docCode  ");
				 buffer.append(" from (select t2.ops_docd ,t2.createtime,t2.num from (select t1.ops_docd, count(1) as num, t1.createtime from ( ");
				 for(int i=0,len=tnL.size();i<len;i++){
					 if(i>0){
						 buffer.append(" union all ");
					 }
					 buffer.append(" select to_char(t"+i+".createtime, 'yyyy-MM-dd') as createtime,t"+i+".ops_docd   ");
					 buffer.append(" from "+tnL.get(i)+" t"+i+" ");
					 buffer.append(" where t"+i+".del_flg = 0  and t"+i+".stop_flg = 0 and t"+i+".ynvalid = '1' and t"+i+".createtime between ");
					 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS')");
				 }
				 buffer.append("  ) t1 group by t1.ops_docd, t1.createtime) t2) t3 left join t_employee e on t3.OPS_DOCD = e.employee_jobno ");
				
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("nums",Hibernate.INTEGER).addScalar("finalDate").addScalar("docCode").addScalar("docName")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationNumsVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERNUMSDOC_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationNumsVo vo:list){
						 if(vo.getNums()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("docName", vo.getDocName());//医生name
								obj.append("docCode", vo.getDocCode());//医生code
								obj.append("nums", vo.getNums());//人数
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERNUMSDOC_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperNumsDocYearOrMonth(menuAlias,"2",date);
						initOperNumsDocYearOrMonth(menuAlias,"3",date);
					}
				}
				saveMongoLog(beginDate, menuAlias+"_OPERNUMSDOC_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 手术例数（医生）年月
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
	public void initOperNumsDocYearOrMonth(String menuAlias, String type,
			String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Integer> map=new HashMap<String,Integer>();//保存日期例数 来源
		String temp;//月数据
		Integer nums;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERNUMSDOC_DAY";
			saveMongo=menuAlias+"_OPERNUMSDOC_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERNUMSDOC_MONTH";
			saveMongo=menuAlias+"_OPERNUMSDOC_YEAR";
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
			 Integer value = (Integer) dbCursor.get("nums") ;//金额
			 String docCode = (String) dbCursor.get("docCode");//code
			 String docName = (String) dbCursor.get("docName");//name
			 temp1=temp+"&"+docCode+"&"+docName;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 nums=map.get(temp1);
				 nums+=value;
				 map.put(temp1,nums);
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
			obj.append("nums",map.get(key));
			obj.append("docCode", stArr[1]);
			obj.append("docName", stArr[2]);
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		saveMongoLog(beginDate, saveMongo, numsList, date);
		
	}

	/**  
	 * 
	 * 手术例数（同环比）
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
	public void initOperNumsYR(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=getOperationRecordTnl(begin,end);
			List<InnerOperationNumsVo> list=null;
			if(tnL!=null&&tnL.size()>0){
				 StringBuffer buffer=new StringBuffer();
				 buffer.append("select count(1) as nums, finalDate from (");
				 for(int i=0,len=tnL.size();i<len;i++){
					 if(i>0){
						 buffer.append(" union all ");
					 }
					 buffer.append(" select  to_char(t"+i+".createtime, 'yyyy-MM-dd') as finalDate  ");
					 buffer.append(" from "+tnL.get(i)+" t"+i+" ");
					 buffer.append(" where t"+i+".del_flg = 0  and t"+i+".stop_flg = 0 and t"+i+".ynvalid = '1' and t"+i+".createtime between ");
					 buffer.append(" to_date('"+begin+"','yyyy-MM-dd HH24:MI:SS') and to_date('"+end+"','yyyy-MM-dd HH24:MI:SS')");
				 }
				 buffer.append(") group by finalDate");
				 list=super.getSession().createSQLQuery(buffer.toString())
							.addScalar("nums",Hibernate.INTEGER).addScalar("finalDate")
							.setResultTransformer(Transformers.aliasToBean(InnerOperationNumsVo.class)).list();
				 DBObject query = new BasicDBObject();
				 query.put("finalDate", date);//移除数据条件
				 new MongoBasicDao().remove(menuAlias+"_OPERNUMSYR_DAY", query);//删除原来的数据
				 if(list!=null && list.size()>0){
					 List<DBObject> numsList = new ArrayList<DBObject>();
					 for(InnerOperationNumsVo vo:list){
						 if(vo.getNums()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("finalDate", vo.getFinalDate());//统计时间
								obj.append("nums", vo.getNums());//人数
								numsList.add(obj);
							}
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_OPERNUMSYR_DAY", numsList);
					if(!"HIS".equals(type)){
						initOperNumsYRYearOrMonth(menuAlias,"2",date);
						initOperNumsYRYearOrMonth(menuAlias,"3",date);
					}
				}
				saveMongoLog(beginDate, menuAlias+"_OPERNUMSYR_DAY", list, date);
			}
		}
		
	}

	/**  
	 * 
	 * 手术例数（同环比）年月
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
	public void initOperNumsYRYearOrMonth(String menuAlias, String type,
			String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Integer> map=new HashMap<String,Integer>();//保存日期例数 来源
		String temp;//月数据
		Integer nums;
		String temp1;//key
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_OPERNUMSYR_DAY";
			saveMongo=menuAlias+"_OPERNUMSYR_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_OPERNUMSYR_MONTH";
			saveMongo=menuAlias+"_OPERNUMSYR_YEAR";
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
			 Integer value = (Integer) dbCursor.get("nums") ;//金额
			 temp1=temp;
			 if(map.containsKey(temp1)){//如果key存在 比较name
				 nums=map.get(temp1);
				 nums+=value;
				 map.put(temp1,nums);
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
			obj.append("nums",map.get(key));
			obj.append("finalDate", stArr[0]);
			numsList.add(obj);
		}
		map=null;
		new MongoBasicDao().insertDataByList(saveMongo, numsList);
		saveMongoLog(beginDate, saveMongo, numsList, date);
		
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
		buffer.append("select t.code_encode as encode,t.code_name as Name,t.code_type as type from t_business_dictionary t  where t.code_type = 'operatetype'  and t.del_flg=0 and t.stop_flg=0 ");
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

	@Override
	public void initPcOperationNumbers(String menuAlias, String type,
			String date) {
		String begin=date+" 00:00:00";
		String end=date+" 23:59:59";
		StringBuffer buffer=new StringBuffer();
		buffer.append("select  count(1) operationNums,sum(r.zeqi) opskindzq, ");
		buffer.append("sum(r.putong) opskingpt,sum(r.jizhen) opskingjz,");
		buffer.append("sum(r.ganran) opskinggr,sum(r.mazui) anesmz,");
		buffer.append("sum(r.bumazui) anesbmz,sum(r.dashoushu) bigoperation,");
		buffer.append("sum(r.zhongshoushu) middleoperation,sum(r.xiaoshoushu) smalloperation,");
		buffer.append("sum(r.conputong) consolept,sum(r.conjiatai) consolejt,");
		buffer.append("sum(r.condiantai) consoledt,sum(r.conjiajitai) consolejjt,");
		buffer.append("r.deptCode deptCode,r.doctorCode docCode,");
		buffer.append("(select t.employee_name from t_employee t where t.employee_jobno=r.doctorCode and t.stop_flg=0 and t.del_flg=0) docName,");
		buffer.append("(select t.DEPT_NAME from t_department t where t.DEPT_CODE=r.deptCode and t.del_flg=0 and t.stop_flg=0) deptName, ");
		buffer.append("r.ownDept ownDeptCode,");
		buffer.append("(select t.DEPT_NAME from t_department t  where t.DEPT_CODE = r.ownDept and t.del_flg = 0 and t.stop_flg = 0) ownDeptName ");
		buffer.append("from( ");
		buffer.append("select decode(t.ops_kind, 0, 1, 0) zeqi,");
		buffer.append("decode(t.ops_kind, 1, 1, 0) putong,");
		buffer.append("decode(t.ops_kind, 2, 1, 0) jizhen,");
		buffer.append("decode(t.ops_kind, 3, 1, 0) ganran,");
		buffer.append("decode(nvl(t.anes_type, 'null'), 'null', 0, 1) mazui,");
		buffer.append("decode(nvl(t.anes_type, 'null'), 'null', 1, 0) bumazui,");
		buffer.append("decode(t.degree, 1, 1, 0) dashoushu,");
		buffer.append("decode(t.degree, 2, 1, 0) zhongshoushu,");
		buffer.append("decode(t.degree, 3, 1, 0) xiaoshoushu,");
		buffer.append("decode(to_number(nvl(t.console_type, '01')), 1, 1, 0) conputong,");
		buffer.append("decode(to_number(t.console_type), 2, 1, 0) conjiatai,");
		buffer.append("decode(to_number(t.console_type), 3, 1, 0) condiantai,");
		buffer.append("decode(to_number(t.console_type), 4, 1, 0) conjiajitai,");
		buffer.append("t.exec_dept deptCode,");
		buffer.append("t.ops_docd doctorCode, ");
		buffer.append("(select e.dept_id from t_employee e where e.employee_jobno = t.ops_docd and e.stop_flg = 0 and e.del_flg = 0) ownDept ");
		buffer.append("from t_operation_record t ");
		buffer.append("where t.ynvalid = 1 ");
		buffer.append("and t.stop_flg = 0 and t.del_flg = 0 ");
		buffer.append("and t.createtime >=to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.createtime <=to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss')");
		buffer.append(") r group by   r.deptCode,r.doctorCode,r.ownDept ");
		
		List<pcOperationWork> list=this.getSession().createSQLQuery(buffer.toString()).addScalar("deptCode")
		.addScalar("deptName").addScalar("docCode").addScalar("docName").addScalar("opskindzq",Hibernate.INTEGER).addScalar("opskingpt",Hibernate.INTEGER).addScalar("opskingjz",Hibernate.INTEGER)
		.addScalar("opskinggr",Hibernate.INTEGER).addScalar("anesmz",Hibernate.INTEGER).addScalar("anesbmz",Hibernate.INTEGER).addScalar("bigoperation",Hibernate.INTEGER).addScalar("middleoperation",Hibernate.INTEGER).addScalar("smalloperation",Hibernate.INTEGER)
		.addScalar("consolept",Hibernate.INTEGER).addScalar("consolejt",Hibernate.INTEGER).addScalar("consoledt",Hibernate.INTEGER).addScalar("consolejjt",Hibernate.INTEGER).addScalar("operationNums",Hibernate.INTEGER)
		.addScalar("ownDeptCode").addScalar("ownDeptName").setResultTransformer(Transformers.aliasToBean(pcOperationWork.class)).list();
		
		String mongodbCollection="OPERATION_PC_DETAIL_DAY";
		
		DBObject query = new BasicDBObject();
		query.put("date", date);//移除数据条件
		new MongoBasicDao().remove(mongodbCollection, query);//删除原来的数据
		if(list.size()>0){
			List<DBObject> voList = new ArrayList<DBObject>();
			for(pcOperationWork vo:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("date", date);
				obj.append("operationNums", vo.getOperationNums());
				obj.append("deptCode", vo.getDeptCode());
				obj.append("deptName", vo.getDeptName());
				obj.append("ownDeptName", vo.getOwnDeptName());
				obj.append("ownDeptCode", vo.getOwnDeptCode());
				
				obj.append("docCode", vo.getDocCode());
				obj.append("docName", vo.getDocName());
				obj.append("opskindzq", vo.getOpskindzq());
				obj.append("opskingpt", vo.getOpskingpt());
				obj.append("opskingjz", vo.getOpskingjz());
				obj.append("opskinggr", vo.getOpskinggr());
				obj.append("anesmz", vo.getAnesmz());
				obj.append("anesbmz",vo.getAnesbmz());
				obj.append("bigoperation", vo.getBigoperation());
				obj.append("middleoperation",vo.getMiddleoperation());
				obj.append("smalloperation",vo.getSmalloperation());
				obj.append("consolept", vo.getConsolept());
				obj.append("consolejt", vo.getConsolejt());
				obj.append("consoledt", vo.getConsoledt());
				obj.append("consolejjt", vo.getConsolejjt());
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(mongodbCollection, voList);//添加新数据
		}
		
	}

	@Override
	public void initPcOperationNumbersMonthAndYear(String menuAlias,
			String type, String date) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		String begin=null;
		String end=null;
		String savemongoCollection=null;
		String querymongoCollection=null;
		if("2".equals(type)){
			begin=date+"-01";
			end=returnEndTime(begin);
			querymongoCollection="OPERATION_PC_DETAIL_DAY";
			savemongoCollection="OPERATION_PC_DETAIL_MONTH";
		}else{
			begin=date.substring(0,4)+"-01";
			end=date.substring(0,4)+"-12";
			querymongoCollection="OPERATION_PC_DETAIL_MONTH";
			savemongoCollection="OPERATION_PC_DETAIL_YEAR";
		}
		
		bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
		condList.add(bdObjectTimeE);
		bdbObject.put("$and", condList);
		
		DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("deptName", "$deptName").append("docCode", "$docCode").append("docName", "$docName").append("ownDeptName", "$ownDeptName").append("ownDeptCode", "$ownDeptCode");  
		DBObject groupFields = new BasicDBObject("_id", _group);
		groupFields.put("opskindzq", new BasicDBObject("$sum","$opskindzq")); 
		groupFields.put("opskingpt", new BasicDBObject("$sum","$opskingpt")); 
		groupFields.put("opskingjz", new BasicDBObject("$sum","$opskingjz")); 
		groupFields.put("operationNums", new BasicDBObject("$sum","$operationNums")); 
		groupFields.put("opskinggr", new BasicDBObject("$sum","$opskinggr")); 
		groupFields.put("anesmz", new BasicDBObject("$sum","$anesmz")); 
		groupFields.put("anesbmz", new BasicDBObject("$sum","$anesbmz")); 
		groupFields.put("bigoperation", new BasicDBObject("$sum","$bigoperation")); 
		groupFields.put("middleoperation", new BasicDBObject("$sum","$middleoperation")); 
		groupFields.put("smalloperation", new BasicDBObject("$sum","$smalloperation")); 
		groupFields.put("consolept", new BasicDBObject("$sum","$consolept")); 
		groupFields.put("consolejt", new BasicDBObject("$sum","$consolejt")); 
		groupFields.put("consoledt", new BasicDBObject("$sum","$consoledt")); 
		groupFields.put("consolejjt", new BasicDBObject("$sum","$consolejjt")); 
		
		DBObject match = new BasicDBObject("$match", bdbObject);
		DBObject group = new BasicDBObject("$group", groupFields);
		AggregationOutput output = new MongoBasicDao().findGroupBy(querymongoCollection, match, group);
		Iterator<DBObject> it = output.results().iterator();
		List<pcOperationWork> list=new ArrayList<pcOperationWork>();
		while(it.hasNext()){
			pcOperationWork vo=new pcOperationWork();
			BasicDBObject dbo = ( BasicDBObject ) it.next();
			BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
			vo.setDeptName((String)keyValus.get("deptName"));
			vo.setDeptCode((String)keyValus.get("deptCode"));
			vo.setDocCode((String)keyValus.get("docCode"));
			vo.setDocName((String)keyValus.get("docName"));
			vo.setOwnDeptCode((String)keyValus.get("ownDeptCode"));
			vo.setOwnDeptName((String)keyValus.get("ownDeptName"));
			vo.setOpskindzq(dbo.getInt("opskindzq"));
			vo.setOpskingpt(dbo.getInt("opskingpt"));
			vo.setOpskingjz(dbo.getInt("opskingjz"));
			vo.setOpskinggr(dbo.getInt("opskinggr"));
			vo.setOperationNums(dbo.getInt("operationNums"));
			vo.setOpskinggr(dbo.getInt("opskinggr"));
			vo.setAnesmz(dbo.getInt("anesmz"));
			
			vo.setAnesbmz(dbo.getInt("anesbmz"));
			vo.setBigoperation(dbo.getInt("bigoperation"));
			vo.setMiddleoperation(dbo.getInt("middleoperation"));
			vo.setSmalloperation(dbo.getInt("smalloperation"));
			vo.setConsolept(dbo.getInt("consolept"));
			vo.setConsolejt(dbo.getInt("consolejt"));
			vo.setConsoledt(dbo.getInt("consoledt"));
			vo.setConsolejjt(dbo.getInt("consolejjt"));
			list.add(vo);
		}
		
		DBObject query = new BasicDBObject();
		query.put("date", date);//移除数据条件
		new MongoBasicDao().remove(savemongoCollection, query);//删除原来的数据
		
		List<DBObject> voList = new ArrayList<DBObject>();
		if(list.size()>0){
			for(pcOperationWork vo:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("date", date);
	    		obj.append("deptCode", vo.getDeptCode());
	    		obj.append("deptName", vo.getDeptName());
	    		obj.append("ownDeptName", vo.getOwnDeptName());
	    		obj.append("ownDeptCode", vo.getOwnDeptCode());
	    		obj.append("docCode", vo.getDocCode());
	    		obj.append("docName", vo.getDocName());
	    		
	    		obj.append("opskindzq", vo.getOpskindzq());
	    		obj.append("opskingpt", vo.getOpskingpt());
	    		obj.append("opskingjz", vo.getOpskingjz());
	    		obj.append("opskinggr", vo.getOpskinggr());
	    		obj.append("anesmz", vo.getAnesmz());
	    		
	    		obj.append("anesbmz", vo.getAnesbmz());
	    		obj.append("bigoperation", vo.getBigoperation());
	    		obj.append("middleoperation", vo.getMiddleoperation());
	    		obj.append("smalloperation", vo.getSmalloperation());
	    		obj.append("consolept", vo.getConsolept());
	    		obj.append("consolejt", vo.getConsolejt());
	    		obj.append("consoledt", vo.getConsoledt());
	    		obj.append("consolejjt", vo.getConsolejjt());
	    		obj.append("operationNums", vo.getOperationNums());
	    		voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(savemongoCollection, voList);//添加新数据
			
		}
	}
		
}
