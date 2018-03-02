package cn.honry.inner.statistics.deptWorkCount.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.deptWorkCount.dao.UpdateKSGZLTJDao;
import cn.honry.inner.statistics.deptWorkCount.service.UpdateKSGZLTJService;
import cn.honry.inner.statistics.deptWorkCount.vo.DoctorWorkloadStatistics;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * @Description:科室工作量统计定时更新数据
 * @author:zhangkui
 * @time:2017年6月30日 上午10:44:34
 */
@Transactional
@Service("updateKSGZLTJService")
@SuppressWarnings({"all"})
public class UpdateKSGZLTJServiceImpl implements UpdateKSGZLTJService {
	private final String[] registerMain={"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};
	private final String[] registerPregister={"T_REGISTER_PREREGISTER_NOW","T_REGISTER_PREREGISTER"};
	
	@Resource
	private UpdateKSGZLTJDao updateKSGZLTJDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Resource
	private WordLoadDocDao wordLoadDocDao;
	/**
	 * @Description:定时更新'科室工作量统计'在线表数据，也可以更新历史数据，级联更新当月，当年
	 * @param date yyyy-MM-dd 更新时间
	 * @param type 类型1
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月30日 上午11:22:19
	 */
	public void init_KSGZLTJ_ByDay(String menuAlias,String type,String date) {
		MongoLog mong = new MongoLog();//记录日志的mongo
		mong.setCountStartTime(new Date());
		int count=imTableData_MZTJFX_KSGZLTJ_DAY(date, date);//时间相同代表,更新当天
		Date d = DateUtils.parseDateY_M_D(date);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias +"_DAY");
		mong.setCountEndTime(new Date());
		mong.setTotalNum(count);//更新了count条
		mong.setCreateTime(new Date());
		
		MongoLog mong02 = new MongoLog();//记录日志的mongo
		mong02.setCountStartTime(new Date());
		int countM=imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(date, date, "2");//更新月
		// 记录日志
		mong02.setStartTime(DateUtils.parseDateY_M(date));
		mong02.setEndTime(DateUtils.parseDateY_M(date));
		mong02.setState(1);
		mong02.setMenuType(menuAlias+"_MONTH");
		mong02.setCountEndTime(new Date());
		mong02.setTotalNum(countM);//更新的条数
		mong02.setCreateTime(new Date());
		
		MongoLog mong03 = new MongoLog();//记录日志的mongo
		mong03.setCountStartTime(new Date());
		int conutY=imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(date, date, "3");//更新年
		// 记录日志
		mong03.setStartTime(DateUtils.parseDateY(date));
		mong03.setEndTime(DateUtils.parseDateY(date));
		mong03.setState(1);
		mong03.setMenuType(menuAlias+"_YEAR");
		mong03.setCountEndTime(new Date());
		mong03.setTotalNum(conutY);//更新的条数
		mong03.setCreateTime(new Date());
		
		

	}
	
	
	/**
	 * @Description:将挂号主表中有关'科室工作量统计'的数据导入mongodb中
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param ghList 挂号主表的分区表集合
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @return 
	 * @time:2017年6月28日 下午2:13:09
	 */
	public int imTableData_MZTJFX_KSGZLTJ_DAY(String stime, String etime) {
		List<String> tnL = new ArrayList<String>();
		try{
			
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
			
			//2.获取门诊数据保留时间S
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			if(DateUtils.compareDate(sTime, cTime)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
				if(DateUtils.compareDate(eTime, cTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
					
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",stime,etime);
				}else{//查询主表和分区表
					
					//获取时间差（年）
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), stime);
					
					//获取相差年份的分区集合 
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					tnL.add(0,"T_REGISTER_MAIN_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				
				tnL.add("T_REGISTER_MAIN_NOW");
			}
		}catch(Exception e){
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		
		
		return updateKSGZLTJDao.imTableData_MZTJFX_KSGZLTJ_DAY(stime, etime, tnL);
		
		
	}


	/**
	 * @Description:将'科室工作量统计'统计的数据初始化到月表或者年表中，根据type参数决定。也可以在定时更新日表的时候，级联更新月表，年表时使用
	 * @param stime 开始时间 yyyy-MM-dd 
	 * @param etime 结束时间yyyy-MM-dd 
	 * @param type 2->月 ,3->年
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @return 
	 * @time:2017年6月28日 下午2:13:09
	 */
	public int imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(String stime,String etime, String type) {
		
		return updateKSGZLTJDao.imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(stime, etime, type);
	}
	
	
	/**
	 * @Description:根据type的类型，初始化'科室工作量统计'数据,这个接口是为外边的包提供初始化日，月，年的数据的
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param type "1","2","3"
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月30日 上午10:16:52
	 */
	public void init_KSGZLTJ(String startTime,String endTime,Integer ty){
		String type=ty.toString();
		if("1".equals(type)){
			imTableData_MZTJFX_KSGZLTJ_DAY(startTime, endTime);
		}
		if("2".equals(type)||"3".equals(type)){
			imTableData_MZTJFX_KSGZLTJ_MONTH_OR_YEAR(startTime, endTime, type);
		}
	}
	

	@Override
	public void initPCdoctorWorkTotal(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		List<String> tnL=null;
		List<String> pretnl=null;
		String sTime=date;
		String eTime=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(sTime), 1));
		tnL=wordLoadDocDao.returnInTables(sTime, eTime, registerMain, "MZ");
		pretnl=wordLoadDocDao.returnInTables(sTime, eTime, registerPregister, "MZ");
		if(tnL!=null&&tnL.size()>0&&pretnl!=null&&pretnl.size()>0){
				StringBuffer sb = new StringBuffer();
				
				/**开始2017-04-17***/
				sb.append(" SELECT  deptcode,doct doct,dept dept,SUM(yyh) yyh,SUM(ghs) ghs,SUM(jzs) jzs,SUM(dhyy) dhyy,SUM(wlyy) wlyy,SUM(ghs) zhghs,SUM(jzs) zhjzs FROM( ");
				
				//循环挂号表
				for (int i = 0; i < tnL.size(); i++) {
					if(i!=0){	
						sb.append(" UNION ALL ");
						sb.append(" SELECT 	M.DOCT_CODE doctcode,M.DEPT_CODE deptcode, M .DOCT_NAME doct,M .DEPT_NAME dept,NULL yyh,COUNT (1) ghs,SUM(DECODE (M.YNSEE, 1, 1,0)) jzs,NULL dhyy,NULL wlyy FROM ");
						sb.append(tnL.get(i)).append(" m ");
						sb.append(" WHERE m.IN_STATE=0 AND m.REG_DATE>=TO_DATE(:stimew, 'yyyy-MM-dd') AND m.REG_DATE<TO_DATE(:etimew, 'yyyy-MM-dd') ");
						sb.append(" GROUP BY m.DEPT_NAME,m.DOCT_NAME,m.DOCT_CODE,m.DEPT_CODE ");
					}else{
						sb.append(" ( SELECT 	M.DOCT_CODE doctcode,M.DEPT_CODE deptcode, M .DOCT_NAME doct,M .DEPT_NAME dept,NULL yyh,COUNT (1) ghs,SUM(DECODE (M.YNSEE, 1, 1,0)) jzs,NULL dhyy,NULL wlyy FROM ");
						sb.append(tnL.get(i)).append(" m ");
						sb.append(" WHERE m.IN_STATE=0 AND m.REG_DATE>=TO_DATE(:stimew, 'yyyy-MM-dd') AND m.REG_DATE<TO_DATE(:etimew, 'yyyy-MM-dd') ");
						sb.append(" GROUP BY m.DEPT_NAME,m.DOCT_NAME,m.DOCT_CODE,m.DEPT_CODE ");
					}
								
				}
				
				//循环预约表
				for (int j = 0; j < pretnl.size(); j++) {
					if(j!=0){
						sb.append(" UNION ALL ");
						sb.append(" SELECT 	p.PREREGISTER_EXPERT DOCTCODE,p.PREREGISTER_DEPT deptcode,P .PREREGISTER_EXPERTNAME doct,P .PREREGISTER_DEPTNAME dept,COUNT (1) yyh,NULL ghs,NULL jzs,SUM(DECODE (P .PREREGISTER_ISPHONE, 1, 1,0)) dhyy,sum(DECODE (P .PREREGISTER_ISNET, 1, 1,0)) wlyy FROM ");
						sb.append(pretnl.get(j)).append(" p ");
						sb.append(" WHERE P .DEL_FLG = 0 AND P .STOP_FLG = 0 AND P .PREREGISTER_DATE >= TO_DATE (:stimew, 'yyyy-MM-dd') AND P .PREREGISTER_DATE < TO_DATE (:etimew, 'yyyy-MM-dd') ");
						sb.append(" GROUP BY p.PREREGISTER_DEPTNAME ,p.PREREGISTER_EXPERTNAME,p.PREREGISTER_EXPERT,p.PREREGISTER_DEPT ");
					}else{
						sb.append(" ) UNION ALL ");
						sb.append(" ( SELECT p.PREREGISTER_EXPERT DOCTCODE,p.PREREGISTER_DEPT deptcode, P .PREREGISTER_EXPERTNAME doct,P .PREREGISTER_DEPTNAME dept,COUNT (1) yyh,NULL ghs,NULL jzs,SUM(DECODE (P .PREREGISTER_ISPHONE, 1, 1,0)) dhyy,sum(DECODE (P .PREREGISTER_ISNET, 1, 1,0)) wlyy FROM ");
						sb.append(pretnl.get(j)).append(" p ");
						sb.append(" WHERE P .DEL_FLG = 0 AND P .STOP_FLG = 0 AND P .PREREGISTER_DATE >= TO_DATE (:stimew, 'yyyy-MM-dd') AND P .PREREGISTER_DATE < TO_DATE (:etimew, 'yyyy-MM-dd') ");
						sb.append(" GROUP BY p.PREREGISTER_DEPTNAME ,p.PREREGISTER_EXPERTNAME,p.PREREGISTER_EXPERT,p.PREREGISTER_DEPT ");
					}
				
				}
				
				//按照医生的编号和科室分组
				sb.append(" ) ) where dept is not null and doct is not null GROUP BY deptcode,doct,dept ");
				
				/**结束2017-04-17***/
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("stimew", sTime);
				paramMap.put("etimew", eTime);
				
				List<DoctorWorkloadStatistics> list = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<DoctorWorkloadStatistics>() {
					@Override
					public DoctorWorkloadStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
						DoctorWorkloadStatistics ds = new DoctorWorkloadStatistics();
						ds.setRegNo(rs.getInt("ghs"));
						ds.setBookNo(rs.getInt("yyh"));
						ds.setVisNo(rs.getInt("jzs"));
						ds.setTelBook(rs.getInt("dhyy"));
						ds.setNetBook(rs.getInt("wlyy"));
						ds.setName(rs.getString("doct"));
						ds.setDept(rs.getString("dept"));
						ds.setRegTot(rs.getInt("zhghs"));
						ds.setArrTot(rs.getInt("zhjzs"));
						ds.setDeptId(rs.getString("deptcode"));
						return ds;
					}
				});
				DBObject query = new BasicDBObject();
				query.put("searchDate", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_TOTAL_DAY", query);
				if(list.size()>0){
					List<DBObject> voList = new ArrayList<DBObject>();
					for(DoctorWorkloadStatistics vo:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("searchDate", date);
						obj.append("deptCode", vo.getDeptId());
						obj.append("deptName", vo.getDept());
						obj.append("doctor", vo.getName());
						obj.append("ghs", vo.getRegNo());
						obj.append("yyh", vo.getBookNo());
						obj.append("jzs", vo.getVisNo());
						obj.append("dhyy", vo.getTelBook());
						obj.append("wlyy", vo.getNetBook());
						obj.append("zhghs", vo.getRegTot());
						obj.append("zhjzs", vo.getArrTot());
						voList.add(obj);
					}
					new MongoBasicDao().insertDataByList(menuAlias+"_TOTAL_DAY", voList);//添加新数据
					wordLoadDocDao.saveMongoLog(beginDate, menuAlias, voList, date);
				}
		}
	}


	@Override
	public void initPCdoctorWork(String startTime, String endTime, Integer type) {
		String menuAlias="YSGZLTJ";
		if(type==1){
			this.initPCdoctorWorkTotal(menuAlias, null, startTime);
		}
	}

}
