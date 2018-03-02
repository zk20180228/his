package cn.honry.inner.statistics.kidneyDiseaseWithDept.dao.impl;

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
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.kidneyDiseaseWithDept.dao.InitKidneyDiseaseWithDeptDao;
import cn.honry.inner.statistics.kidneyDiseaseWithDept.vo.ItemVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("initKidneyDiseaseWithDeptDao")
@SuppressWarnings("all")
public class InitKidneyDiseaseWithDeptDaoImpl extends HibernateDaoSupport implements InitKidneyDiseaseWithDeptDao {
	private final String[] inpatient={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};//住院主表T_INPATIENT_INFO
	private final String[] register={"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};//门诊挂号：挂号主表T_REGISTER_MAIN
	private final String[] inpatientFeeinfo={"T_INPATIENT_FEEINFO_NOW","T_INPATIENT_FEEINFO"};//住院费用汇总表T_INPATIENT_FEEINFO
	private final String[] outpatientFeedetail={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};//门诊处方明细表T_OUTPATIENT_FEEDETAIL
	private final String ZY="ZY";//住院
	private final String MZ="MZ";//门诊
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	/**
	 * 科室对比表 (日)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_SBKSDBB(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL1=wordLoadDocDao.returnInTables(begin, end, inpatient, ZY);
			List<String> tnL2=wordLoadDocDao.returnInTables(begin, end, register, MZ);
			List<String> tnL3=wordLoadDocDao.returnInTables(begin, end, inpatientFeeinfo, ZY);
			List<String> tnL4=wordLoadDocDao.returnInTables(begin, end, outpatientFeedetail, MZ);
			StringBuffer sb = new StringBuffer();
			sb.append("select f.dept_name,f.dept_code as dept_code, nvl(sum(tt.chuYUNum), 0) as chuYUNum, nvl(sum(tt.avgInYuDays), 0) as avgInYuDays,nvl(sum(tt.ruYuNum), 0) as ruYuNum,");
			sb.append(" nvl(sum(tt.workNum), 0) as workNum, nvl(sum(tt.zhuCost), 0) as zhuCost, nvl(sum(tt.menCost),0) as menCost,");
			sb.append(" (select count(1) as beds from t_business_hospitalbed t where t.stop_flg = 0 and t.del_flg = 0) as beds,");
			sb.append(" (select count(1) from t_business_hospitalbed t where t.bed_state = '4' and t.stop_flg = 0 and t.del_flg = 0) as bedUsed ");
			sb.append(" from (select t.dept_code as dept_code,count(1) as chuYUNum,nvl(avg(trunc(t.out_date) - trunc(t.in_date)), 0) as avgInYuDays,");
			sb.append(" 0 as ruYuNum, 0 as workNum,0 as zhuCost,0 as menCost from "+tnL1.get(0)+" t");
			sb.append(" where t.out_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.out_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.dept_code");
			sb.append(" union all ");
			sb.append(" select t.dept_code as dept_code, 0 as chuYUNum,0 as avgInYuDays, count(1) as ruYuNum, 0 as workNum,0 as zhuCost,0 as menCost  from "+tnL1.get(0)+" t");
			sb.append(" where t.in_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.in_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.dept_code");
			sb.append(" union all ");
			sb.append(" select t.DEPT_CODE as dept_code, 0 as chuYUNum, 0 as avgInYuDays, 0 as ruYuNum,count(1) as workNum,0 as zhuCost,0 as menCost from "+tnL2.get(0)+" t");
			sb.append(" where t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.dept_code");
			sb.append(" union all ");
			sb.append(" select t.INHOS_DEPTCODE as dept_code,0 as chuYUNum, 0 as avgInYuDays,0 as ruYuNum, 0 as workNum,nvl(sum(t.tot_cost), 0) as zhuCost,0 as menCost from "+tnL3.get(0)+" t");
			sb.append(" where t.BALANCE_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.BALANCE_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.INHOS_DEPTCODE");
			sb.append(" union all ");
			sb.append(" select t.DOCT_DEPT as dept_code,0 as chuYUNum,0 as avgInYuDays,0 as ruYuNum, 0 as workNum,0 as zhuCost,nvl(sum(t.tot_cost), 0) as menCost from "+tnL4.get(0)+" t");
			sb.append(" where t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.DOCT_DEPT");
			sb.append("  ) tt ");
			sb.append(" right join T_FICTITIOUS_CONTACT f on f.DEPT_CODE = tt.dept_code");
			sb.append(" where f.STOP_FLG = 0 and f.DEL_FLG = 0 and f.type in ('C', 'I', 'N')");
			sb.append(" group by f.dept_code,f.dept_name");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("dept_name",Hibernate.STRING)
			   .addScalar("dept_code",Hibernate.STRING)
			   .addScalar("ruYuNum",Hibernate.INTEGER)
			   .addScalar("chuYUNum",Hibernate.INTEGER)
			   .addScalar("beds",Hibernate.INTEGER) 
			   .addScalar("bedUsed",Hibernate.INTEGER)
			   .addScalar("avgInYuDays",Hibernate.DOUBLE)
			   .addScalar("workNum",Hibernate.INTEGER)
			   .addScalar("menCost",Hibernate.DOUBLE)
			   .addScalar("zhuCost",Hibernate.DOUBLE);
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			List<ItemVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(ItemVo.class)).list();
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(ItemVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("selectTime", date);
					obj.append("deptCode", vo.getDept_code());
					obj.append("deptName", vo.getDept_name());
					obj.append("ruYuNum", vo.getRuYuNum());
					obj.append("chuYUNum", vo.getChuYUNum());
					obj.append("beds", vo.getBeds());
					obj.append("bedUsed", vo.getBedUsed());
					obj.append("avgInYuDays", vo.getAvgInYuDays());
					obj.append("workNum", vo.getWorkNum());
					obj.append("menCost", vo.getMenCost());
					obj.append("zhuCost", vo.getZhuCost());
					voList.add(obj);
				 }
				DBObject query = new BasicDBObject();
				query.put("selectTime", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_SBKSDBB_DAY", query);//删除原来的数据
				new MongoBasicDao().insertDataByList(menuAlias+"_SBKSDBB_DAY", voList);//添加新数据
				if(!"HIS".equals(type)){
					init_SBKSDBB_MONTH(menuAlias,"2",date);//月更新科室对比表
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_SBKSDBB_DAY", list, date);
		}
	}
	/**
	 * 科室对比表 (月)
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	@Override
	public void init_SBKSDBB_MONTH(String menuAlias, String type, String date) {
		if ("2".equals(type)) {
			Date beginDate=new Date();
			BasicDBObject bdObject = new BasicDBObject();
			String queryMongo=menuAlias+"_SBKSDBB_DAY";//查询的表
			String saveMongo=menuAlias+"_SBKSDBB_MONTH";//保存的表
			String temp=date.substring(0,7);//截取月时间
			String begin=temp+"-01";//开始时间
			String end=returnEndTime(date);//计算一个月最后日期
			BasicDBList dateList=new BasicDBList();
			BasicDBObject data1= new BasicDBObject();//查询开始时间
			BasicDBObject data2= new BasicDBObject();//查询结束时间
			data1.append("selectTime", new BasicDBObject("$gte",begin));
			data2.append("selectTime", new BasicDBObject("$lte",end));
			dateList.add(data1);
			dateList.add(data2);
			bdObject.put("$and", dateList);
			DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			Map<String, List<Object>> map=new HashMap<String, List<Object>>();
			List<Object> list=null;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
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
			DBObject query = new BasicDBObject();
			query.put("selectTime", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Entry entry = (Entry)it.next();
				BasicDBObject obj = new BasicDBObject();
				obj.append("selectTime", date);
				obj.append("deptName",  map.get(entry.getKey()).get(0));
				obj.append("deptCode", map.get(entry.getKey()).get(1));
				obj.append("ruYuNum",  map.get(entry.getKey()).get(2));
				obj.append("chuYUNum",  map.get(entry.getKey()).get(3));
				obj.append("beds",  map.get(entry.getKey()).get(4));
				obj.append("bedUsed",  map.get(entry.getKey()).get(5));
				obj.append("avgInYuDays",  map.get(entry.getKey()).get(6));
				obj.append("workNum",  map.get(entry.getKey()).get(7));
				obj.append("menCost",  map.get(entry.getKey()).get(8));
				obj.append("zhuCost",  map.get(entry.getKey()).get(9));
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
			wordLoadDocDao.saveMongoLog(beginDate, saveMongo,voList, date);
		}
	}
	
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
}
