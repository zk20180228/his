package cn.honry.inner.statistics.deptWorkCount.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.deptWorkCount.dao.UpdateKSGZLTJDao;
import cn.honry.inner.statistics.deptWorkCount.vo.DeptWorkCountVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("updateKSGZLTJDao")
@SuppressWarnings({"all"})
public class UpdateKSGZLTJDaoImpl implements UpdateKSGZLTJDao{
	
	
	
	public static final String TABLENAME_KSGZLTJ_DAY="KSGZLTJ_DAY";//科室工作量统计日表
	public static final String TABLENAME_KSGZLTJ_MONTH="KSGZLTJ_MONTH";//科室工作量统计月表
	public static final String TABLENAME_KSGZLTJ_YEAR="KSGZLTJ_YEAR";//科室工作量统计月表
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
	/**
	 * @Description:将挂号主表中有关'科室工作量统计'的数据导入mongodb中,按日在oracle中进行查询一次，存一次。初始化数据可用，每天定时更新在线表数据到mongo中也可用，也可以按天更新历史数据
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param ghList 挂号主表的分区表集合
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月28日 下午2:13:09
	 */
	public int imTableData_MZTJFX_KSGZLTJ_DAY(String stime, String etime,List ghList) {
		
		//拼接sql，sql按天已经分好组了，不需要去重
		StringBuffer sb  = new StringBuffer();
		sb.append(" select ");
		sb.append(" d.DEPT_NAME as deptName, ");
		sb.append(" f.*, ");
		sb.append(" nvl((jzmoney+yzmoney+yymoney+ghmoney+fzmoney+pzmoney),0) as totalmoney, ");
		sb.append(" nvl((jzcount+yzcount+yycount+ghcount+fzcount+pzcount),0)as totalcount ");
		sb.append(" from ( ");
		sb.append(" SELECT ");
		sb.append(" r.deptCode as deptCode, ");
		sb.append(" r.REGDATE as regDate, ");
		sb.append(" nvl(sum(r.gjjzmzjcount),0) as gjjzmzjcount, ");
		sb.append(" nvl(sum(r.gjjzmzjmoney),0) as gjjzmzjmoney, ");
		sb.append(" nvl(sum(r.sjzmzjcount),0) as sjzmzjcount, ");
		sb.append(" nvl(sum(r.sjzmzjmoney),0) as sjzmzjmoney, ");
		sb.append(" nvl(sum(r.zmzjcount),0) as zmzjcount, ");
		sb.append(" nvl(sum(r.zmzjmoney),0) as zmzjmoney, ");
		sb.append(" nvl(sum(r.jscount),0) as jscount, ");
		sb.append(" nvl(sum(r.jsmoney),0) as jsmoney, ");
		sb.append(" nvl(sum(r.fjscount),0) as fjscount, ");
		sb.append(" nvl(sum(r.fjsmoney),0) as fjsmoney, ");
		sb.append(" nvl(sum(r.jymzcount),0) as jymzcount, ");
		sb.append(" nvl(sum(r.jymzmoney),0) as jymzmoney, ");
		sb.append(" nvl(sum(r.ybyscount),0) as ybyscount, ");
		sb.append(" nvl(sum(r.ybysmoney),0) as ybysmoney, ");
		sb.append(" nvl(sum(r.zzyscount),0) as zzyscount, ");
		sb.append(" nvl(sum(r.zzysmoney),0) as zzysmoney, ");
		sb.append(" nvl(sum(r.lnyzcount),0) as lnyzcount, ");
		sb.append(" nvl(sum(r.lnyzmoney),0) as lnyzmoney, ");
		sb.append(" nvl(sum(r.slzcfcount),0) as slzcfcount, ");
		sb.append(" nvl(sum(r.slzcfmoney),0) as slzcfmoney, ");
		sb.append(" nvl(sum(r.jmjskcount),0) as jmjskcount, ");
		sb.append(" nvl(sum(r.jmjskmoney),0) as jmjskmoney, ");
		sb.append(" nvl(sum(r.jzcount),0) as jzcount, ");
		sb.append(" nvl(sum(r.jzmoney),0) as jzmoney, ");
		sb.append(" nvl(sum(r.yzcount),0) as yzcount, ");
		sb.append(" nvl(sum(r.yzmoney),0) as yzmoney, ");
		sb.append(" nvl(sum(r.yycount),0) as yycount, ");
		sb.append(" nvl(sum(r.yymoney),0) as yymoney, ");
		sb.append(" nvl(sum(r.ghcount),0) as ghcount, ");
		sb.append(" nvl(sum(r.ghmoney),0) as ghmoney, ");
		sb.append(" nvl(sum(r.fzcount),0) as fzcount, ");
		sb.append(" nvl(sum(r.fzmoney),0) as fzmoney, ");
		sb.append(" nvl(sum(r.pzcount),0) as pzcount, ");
		sb.append(" nvl(sum(r.pzmoney),0) as pzmoney ");
		sb.append(" from ( ");
		
		for(int i=0;i<ghList.size();i++){
			if(i!=0){
				sb.append(" union all ");
			}
			sb.append(" select ");
			sb.append(" t.DEPT_CODE as deptCode, ");
			sb.append(" to_char(T.REG_DATE,'yyyy-mm-dd') as regDate, ");
			sb.append(" sum(decode(t.reglevl_code,'37',1,0)) as gjjzmzjcount, ");
			sb.append(" sum(decode(t.reglevl_code,'37',nvl(t.sum_cost,0),0)) as gjjzmzjmoney, ");//nvl(t.sum_cost,0),虽然sum_cost是number类型,但是默认值并不是0，而是null，为了防止null，取0
			sb.append(" sum(decode(t.reglevl_code,'36',1,0)) as sjzmzjcount, ");
			sb.append(" sum(decode(t.reglevl_code,'36',nvl(t.sum_cost,0),0)) as sjzmzjmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'01',1,0)) as zmzjcount, ");
			sb.append(" sum(decode(t.reglevl_code,'01',nvl(t.sum_cost,0),0)) as zmzjmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'02',1,0)) as jscount, ");
			sb.append(" sum(decode(t.reglevl_code,'02',nvl(t.sum_cost,0),0)) as jsmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'03',1,0)) as fjscount, ");
			sb.append(" sum(decode(t.reglevl_code,'03',nvl(t.sum_cost,0),0)) as fjsmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'05',1,0)) as jymzcount, ");
			sb.append(" sum(decode(t.reglevl_code,'05',nvl(t.sum_cost,0),0)) as jymzmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'38',1,0)) as ybyscount, ");
			sb.append(" sum(decode(t.reglevl_code,'38',nvl(t.sum_cost,0),0)) as ybysmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'04',1,0)) as zzyscount, ");
			sb.append(" sum(decode(t.reglevl_code,'04',nvl(t.sum_cost,0),0)) as zzysmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'06',1,0)) as lnyzcount, ");
			sb.append(" sum(decode(t.reglevl_code,'06',nvl(t.sum_cost,0),0)) as lnyzmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'07',1,0)) as slzcfcount, ");
			sb.append(" sum(decode(t.reglevl_code,'07',nvl(t.sum_cost,0),0)) as slzcfmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'66',1,0)) as jmjskcount, ");
			sb.append(" sum(decode(t.reglevl_code,'66',nvl(t.sum_cost,0),0)) as jmjskmoney, ");
			
			sb.append(" sum(decode(nvl(g.TriageType_Code,'null'), '0', 1, 0)) as jzcount, ");
			sb.append(" sum(decode( nvl(g.TriageType_Code,'null'), '0', nvl(t.sum_cost, 0), 0)) as jzmoney, ");
			
			sb.append(" sum(decode(nvl(g.TriageType_Code,'null'), '1', 1, 0)) as yzcount, ");
			sb.append(" sum(decode(nvl(g.TriageType_Code,'null'),'1',nvl(t.sum_cost, 0),0)) as yzmoney, ");
			
			sb.append(" sum(decode(decode(nvl(g.TriageType_Code,t.YNBOOK||'yynull'),'1yynull','1yynull','2','1yynull',decode(nvl(instr('345',g.TriageType_Code),0),0,'null',t.YNBOOK||'yynull') ), '1yynull', 1, 0)) as yycount, ");
			sb.append(" sum(decode(decode(nvl(g.TriageType_Code,t.YNBOOK||'yynull'),'1yynull','1yynull','2','1yynull',decode(nvl(instr('345',g.TriageType_Code),0),0,'null',t.YNBOOK||'yynull') ), '1yynull', nvl(t.sum_cost, 0), 0)) as yymoney, ");
			
			sb.append(" sum(decode(g.TriageType_Code,'3',decode(t.YNBOOK,'0',1,0),0)) as ghcount, ");
			sb.append(" sum(decode(g.TriageType_Code,'3',decode(t.YNBOOK,'0',nvl(t.sum_cost, 0),0),0)) as ghmoney, ");
			
			sb.append(" sum(decode(g.TriageType_Code,'5',decode(t.YNBOOK,'0',1,0),0)) as fzcount, ");
			sb.append(" sum(decode(g.TriageType_Code,'5',decode(t.YNBOOK,'0',nvl(t.sum_cost, 0),0),0)) as fzmoney, ");
			
			sb.append(" sum(decode(nvl(g.TriageType_Code,t.YNBOOK||'pznull'),'0pznull',1,'4',decode(t.YNBOOK,'0',1,0))) as pzcount, ");
			sb.append(" sum(decode(nvl(g.TriageType_Code,t.YNBOOK||'pznull'),'0pznull',nvl(t.sum_cost, 0),'4',decode(t.YNBOOK,'0',nvl(t.sum_cost, 0),0))) as pzmoney ");
//			TriageType_Code is null 
//			YNBOOK=0   平诊
//			YNBOOK=1 预约
//
//			TriageType_Code  not is null
//			TriageType_Code=1  优诊
//			TriageType_Code=0  急诊
//			TriageType_Code=2 预约
//			TriageType_Code=3
//			    YNBOOK=1  预约
//			    YNBOOK=0 过号
//			TriageType_Code=4
//			    YNBOOK=1  预约
//			    YNBOOK=0 平诊
//			TriageType_Code=5
//			    YNBOOK=1  预约
//			    YNBOOK=0 复诊 
			sb.append(" FROM ");
			sb.append(ghList.get(i)+" t left join T_REGISTER_TRIAGE g on t.CARD_NO=g.CARD_NO and t.CLINIC_CODE=g.CLINIC_CODE ");
			sb.append(" WHERE ");
			sb.append(" t.DEL_FLG = 0 ");
			sb.append(" AND t.STOP_FLG = 0 ");
			sb.append(" AND t.trans_type = 1 ");
			sb.append(" AND t.IN_STATE = 0 ");
			sb.append(" AND t.VALID_FLAG = 1");	
			sb.append(" AND t.REG_DATE >= TO_DATE (:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" AND t.REG_DATE <= TO_DATE (:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" GROUP BY ");
			sb.append(" t.DEPT_CODE, ");
			sb.append(" t.REG_DATE ");
			
		}
		sb.append(" ) r ");
		sb.append(" group by ");
		sb.append(" r.deptCode, ");
		sb.append(" r.REGDATE ");
		sb.append(" ) f ,T_DEPARTMENT d  ");
		sb.append(" where ");
		sb.append(" f.DEPTCODE=d.dept_code ");
		
		//按天查询，按天更新,调用者是一天一天传的时间
		HashMap<String,String> map = new HashMap<String, String>();
		String mid=stime.substring(0, 10);//yyyy-MM-dd HH:mm:ss
		stime=mid+" 00:00:00";
		map.put("startTime", stime);
		etime=mid+" 23:59:59";
		map.put("endTime", etime);
		
		int count =0;//记录更新的次数
		List<DeptWorkCountVo> list = namedParameterJdbcTemplate.query(sb.toString(), map, new BeanPropertyRowMapper(DeptWorkCountVo.class));
		if(list!=null&&list.size()>0){
					for(DeptWorkCountVo v:list){
							Document document1 = new Document();
							document1.append("deptCode", v.getDeptCode());
							document1.append("regDate", v.getRegDate());
							Document document = new Document();
							String deptCode = v.getDeptCode();
							String regDate =v.getRegDate();
							document.append("deptCode", deptCode);
							document.append("regDate", regDate);
							v.setDeptCode(null);
							v.setRegDate(null);
							String json = JSONUtils.toJson(v);
							document.append("value", json);
							new MongoBasicDao().update(TABLENAME_KSGZLTJ_DAY, document1,document, true);//如果存在document1,那么document将覆盖document1,否则新增
							count++;
						}
				}
		
			return count;	
		}
	
		
	/**
	 * @Description:将'科室工作量统计'统计的数据初始化到月表或者年表中，根据type参数决定。也可以在定时更新日表的时候，级联更新月表，年表时使用
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param type 2->月 ,3->年
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月28日 下午2:13:09
	 */
	public int imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(String stime, String etime,String type) {
		
		int st = Integer.parseInt(stime.substring(0,4));//得到开始的年
		int et =Integer.parseInt(etime.substring(0, 4));//得到结束的年
		int year =et-st;//得到年的差
		int count =0;
		for(int i=0;i<=year;i++){//年,从0开始，包含自己
			
			if("2".equals(type)){//初始化月
				for(int m =1;m<=12;m++){//月
					String month =null;
					if(m<10){
						month="0"+m;
					}
					if(m>=10){
						month=m+"";
					}
					
					//得到当年的月的最后一天
					String ym=new Integer(st+i).toString()+"-"+month;
					String lastDay= getLastDay(ym);
					String startTime = ym+"-01";
					String endTime = ym+"-"+lastDay;
					count+= insertDataToMongo(TABLENAME_KSGZLTJ_DAY,TABLENAME_KSGZLTJ_MONTH, startTime, endTime, type);
				}
			}
			
			
			if("3".equals(type)){//初始化年
				String ym=new Integer(st+i).toString();
				String startTime = ym+"-01";
				String endTime = ym+"-12";
				count+=  insertDataToMongo(TABLENAME_KSGZLTJ_MONTH,TABLENAME_KSGZLTJ_YEAR, startTime, endTime, type);
			}
			
		}
		
		return count;
	}
		

	/**
	 * @Description:初始化'科室工作量统计'数据
	 * @param readClooection 要读的表
	 * @param insertCollection 数据要插入的表
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param type 2--->月,3---->年,代表所要更新表的级别
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月30日 上午9:29:24
	 */
	public int insertDataToMongo(String readCollection,String insertCollection,String startTime,String endTime,String type){
		int count =0;
		
		//构建mongo 的查询条件{$and:[{regDate:{$gte:startTime}},{regDate:{$lte:endTime}}]}
		BasicDBObject dbObject = new BasicDBObject();
		BasicDBList dbList = new BasicDBList();
		dbList.add(new BasicDBObject("regDate",new BasicDBObject("$gte",startTime)));
		dbList.add(new BasicDBObject("regDate",new BasicDBObject("$lte",endTime)));
		dbObject.put("$and",dbList);
		DBCursor dbCursor = new MongoBasicDao().findAlldata(readCollection, dbObject);
		HashMap<String,DeptWorkCountVo> map = new HashMap<String, DeptWorkCountVo>();//去重时的map
		while(dbCursor.hasNext()){//查询，本月/年内，相同科室编号的要相加，不能重复,去重时放入map
			DBObject cursor = dbCursor.next();
			String s = cursor.get("value").toString();//一个value就是一个对应的vo
			String code =cursor.get("deptCode").toString();
			DeptWorkCountVo v=null;
			try {
				v = JSONUtils.fromJson(s,DeptWorkCountVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			v.setDeptCode(code);//设置科室编号
			DeptWorkCountVo vo = map.get(code);
			if(vo!=null){//相同编号的，结果进行相加
				vo.setGjjzmzjcount(vo.getGjjzmzjcount()+v.getGjjzmzjcount());
				vo.setGjjzmzjmoney(vo.getGjjzmzjmoney()+v.getGjjzmzjmoney());
				vo.setSjzmzjcount(vo.getSjzmzjcount()+v.getSjzmzjcount());
				vo.setSjzmzjmoney(vo.getSjzmzjmoney()+v.getSjzmzjmoney());
				vo.setZmzjcount(vo.getZmzjcount()+v.getZmzjcount());
				vo.setZmzjmoney(vo.getZmzjmoney()+v.getZmzjmoney());
				vo.setJscount(vo.getJscount()+v.getJscount());
				vo.setJsmoney(vo.getJsmoney()+v.getJsmoney());
				vo.setFjscount(vo.getFjscount()+v.getFjscount());
				vo.setFjsmoney(vo.getFjsmoney()+v.getFjsmoney());
				vo.setJymzcount(vo.getJymzcount()+v.getJymzcount());
				vo.setJymzmoney(vo.getJymzmoney()+v.getJymzmoney());
				vo.setYbyscount(vo.getYbyscount()+v.getYbyscount());
				vo.setYbysmoney(vo.getYbysmoney()+v.getYbysmoney());
				vo.setZzyscount(vo.getZzyscount()+v.getZzyscount());
				vo.setZzysmoney(vo.getZzysmoney()+v.getZzysmoney());
				vo.setLnyzcount(vo.getLnyzcount()+v.getLnyzcount());
				vo.setLnyzmoney(vo.getLnyzmoney()+v.getLnyzmoney());
				vo.setSlzcfcount(vo.getSlzcfcount()+v.getSlzcfcount());
				vo.setSlzcfmoney(vo.getSlzcfmoney()+v.getSlzcfmoney());
				vo.setJmjskcount(vo.getJmjskcount()+v.getJmjskcount());
				vo.setJmjskmoney(vo.getJmjskmoney()+v.getJmjskmoney());
				vo.setJzcount(vo.getJzcount()+v.getJzcount());
				vo.setJzmoney(vo.getJzmoney()+v.getJzmoney());
				vo.setYzcount(vo.getYzcount()+v.getYzcount());
				vo.setYzmoney(vo.getYzmoney()+v.getYzmoney());
				vo.setYycount(vo.getYycount()+v.getYycount());
				vo.setYymoney(vo.getYymoney()+v.getYymoney());
				vo.setGhcount(vo.getGhcount()+v.getGhcount());
				vo.setGhmoney(vo.getGhmoney()+v.getGhmoney());
				vo.setFzcount(vo.getFzcount()+v.getFzcount());
				vo.setFzmoney(vo.getFzmoney()+v.getFzmoney());
				vo.setPzcount(vo.getPzcount()+v.getPzcount());
				vo.setPzmoney(vo.getPzmoney()+v.getPzmoney());
				vo.setTotalcount(vo.getTotalcount()+v.getTotalcount());
				vo.setTotalmoney(vo.getTotalmoney()+v.getTotalmoney());			
			}else{
				try {
					DeptWorkCountVo d = new DeptWorkCountVo();
					BeanUtils.copyProperties(d, v);
					map.put(code, d);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			}
			
		}
		
		//将以上的数据存入mongo中
		ArrayList<DeptWorkCountVo> list = new ArrayList<DeptWorkCountVo>();
		list.addAll(map.values());
		String sMonth=startTime.substring(0, 7);
		String sYear=startTime.substring(0, 4);
		
		//遍历，一个科室一个科室存
		for(DeptWorkCountVo dv:list){
			DBObject query = new BasicDBObject();//删除相同的时间和科室的文档
			Document document = new Document();//新的要插入的文档
			if("2".equals(type)){
				document.append("regDate", sMonth);//存月
				query.put("regDate", sMonth);
			}
			if("3".equals(type)){
				document.append("regDate", sYear);//存年
				query.put("regDate", sYear);
			}
			
			String dCode=dv.getDeptCode();
			document.append("deptCode",dCode);
			query.put("deptCode",dCode);
			dv.setDeptCode(null);//科室编号单独存，不存入value字段
			String json= JSONUtils.toJson(dv);
			document.append("value", json);
			new MongoBasicDao().remove(insertCollection, query);//先删除
			new MongoBasicDao().insertData(insertCollection, document);//存入mongo
			count++;
		}
		
		return count;
	}
	
	//获取某月的最后一天
	/**
	 * @Description:给定时间，最短格式：年-月-日，返回该月最后一天字符串表示形式，平年2月28天，闰年，29天
	 * @param date 最短格式：yyyy-MM-dd 比此格式长的都可以
	 * @return
	 * String 该月最后一天的字符串穿表示形式
	 * @exception: 日期格式转换异常
	 * @author: zhangkui
	 * @time:2017年7月3日 下午1:53:57
	 */
	public String getLastDay(String date){
		date= date.substring(0, 7);
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
