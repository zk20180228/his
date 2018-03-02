package cn.honry.statistics.deptstat.hospitalday.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.Dashboard;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.statistics.deptstat.hospitalday.dao.HospitaldayDao;
import cn.honry.statistics.deptstat.hospitalday.vo.HospitaldayVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("hospitaldayDao")
@SuppressWarnings({ "all" })
public class HospitaldayDaoImpl extends HibernateEntityDao<HospitaldayVo> implements HospitaldayDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<HospitaldayVo> queryHospitaldayList(List<String> tnL,
			String parameter, String startTime, String endTime) {
		Date beginDate=new Date();
		String begin=endTime+" 00:00:00";//开始时间
		String end=endTime+" 23:59:59";//结束时间
		List<HospitaldayVo> list=null;
			StringBuffer buffer=new StringBuffer();
			buffer.append("    select sum(inHospitalNum) inHospitalNum,sum(inHospitalNumHy) inHospitalNumHy,sum(inHospitalNumZd) inHospitalNumZd,sum(inHospitalNumHj) inHospitalNumHj,timeValue from "
					+ "(select count(1) as inHospitalNum,"
					+ "count((select p.dept_code from t_department p where p.dept_code = t.dept_code and p.dept_area_code = 1)) as inHospitalNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code = t.dept_code and p.dept_area_code = 2)) as inHospitalNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code = t.dept_code and p.dept_area_code = 3)) as inHospitalNumHj,"
					+ "'"+endTime+"' timeValue from t_inpatient_info t "
					+ "where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.in_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "union "
					+ "select count(1) as inHospitalNum,"
					+ "count((select p.dept_code from t_department p where p.dept_code = t.dept_code and p.dept_area_code = 1)) as inHospitalNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code = t.dept_code and p.dept_area_code = 2)) as inHospitalNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code = t.dept_code and p.dept_area_code = 3)) as inHospitalNumHj,"
					+ "'"+endTime+"' timeValue from t_inpatient_info_now t "
					+ "where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.in_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')) group by timeValue");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setInHospitalNum(rs.getString("inHospitalNum")); 
					vo.setInHospitalNumHy(rs.getString("inHospitalNumHy")); 
					vo.setInHospitalNumZd(rs.getString("inHospitalNumZd")); 
					vo.setInHospitalNumHj(rs.getString("inHospitalNumHj"));
					vo.setTimeValue(rs.getString("timeValue")); 
					return vo;
				}
			});
		return list;
	}
	
	@Override
	public void init_YYMRHZ(String menuAlias, Integer type, String date) {
		//在院人次初始化
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<HospitaldayVo> list=null;
				StringBuffer buffer=new StringBuffer();
				buffer.append(" SELECT sum(pp.inpatientNum) inpatientNum,sum(pp.inpatientNumHy) inpatientNumHy,sum(pp.inpatientNumZd) inpatientNumZd,sum(pp.inpatientNumHj) inpatientNumHj,pp.timeValue as timeValue FROM ( ");
				buffer.append("select count(1) as inpatientNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as inpatientNumHy,"
						+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as inpatientNumZd,"
						+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as inpatientNumHj,"
						+ "'"+date+"' timeValue  "
						+ "from t_inpatient_info t where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
						+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')");
				buffer.append(" union ");
				buffer.append("select count(1) as inpatientNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as inpatientNumHy,"
						+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as inpatientNumZd,"
						+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as inpatientNumHj,"
						+ "'"+date+"' timeValue  "
						+ "from t_inpatient_info_now t where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
						+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')"
						+ " union"
						+ " select count(1) as inpatientNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as inpatientNumHy,"
						+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as inpatientNumZd,"
						+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as inpatientNumHj,"
						+ "'"+date+"' timeValue  "
						+ "from t_inpatient_info_now t where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') and t.in_state='I'"
						+ ")pp group by pp.timeValue");
				list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
					@Override
					public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
						HospitaldayVo vo = new HospitaldayVo();
						vo.setInpatientNum(rs.getString("inpatientNum")); 
						vo.setInpatientNumHy(rs.getString("inpatientNumHy")); 
						vo.setInpatientNumZd(rs.getString("inpatientNumZd")); 
						vo.setInpatientNumHj(rs.getString("inpatientNumHj"));
						vo.setTimeValue(rs.getString("timeValue")); 
						return vo;
					}
				});
				
				DBObject query = new BasicDBObject();
				query.put("timeValue", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_HOSPITAL_DAY_INPA", query);//删除原来的数据
				
				if(list!=null && list.size()>0){
					List<DBObject> userList = new ArrayList<DBObject>();
						for(HospitaldayVo vo:list){
							BasicDBObject obj = new BasicDBObject();
							obj.append("inpatientNum", vo.getInpatientNum());
							obj.append("inpatientNumHy", vo.getInpatientNumHy());
							obj.append("inpatientNumHj", vo.getInpatientNumHj());
							obj.append("inpatientNumZd", vo.getInpatientNumZd());
							obj.append("timeValue", vo.getTimeValue());
							userList.add(obj);
						}
					new MongoBasicDao().insertDataByList(menuAlias+"_HOSPITAL_DAY_INPA", userList);
				}
				wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_HOSPITAL_DAY_INPA", list, date);
			}
		if(StringUtils.isNotBlank(date)){
			//出院人次初始化
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<HospitaldayVo> list=null;
			StringBuffer buffer=new StringBuffer();
			buffer.append(" SELECT sum(leaveHospitalNum) leaveHospitalNum,sum(leaveHospitalNumHy) leaveHospitalNumHy,sum(leaveHospitalNumZd) leaveHospitalNumZd,sum(leaveHospitalNumHj) leaveHospitalNumHj,pp.timeValue as timeValue FROM ( ");
			buffer.append("select count(1) as leaveHospitalNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as leaveHospitalNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as leaveHospitalNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as leaveHospitalNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info t where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')");
			buffer.append(" union ");
			buffer.append("select count(1) as leaveHospitalNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as leaveHospitalNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as leaveHospitalNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as leaveHospitalNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info_now t where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss'))pp group by pp.timeValue");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setLeaveHospitalNum(rs.getString("leaveHospitalNum")); 
					vo.setLeaveHospitalNumHy(rs.getString("leaveHospitalNumHy")); 
					vo.setLeaveHospitalNumZd(rs.getString("leaveHospitalNumZd")); 
					vo.setLeaveHospitalNumHj(rs.getString("leaveHospitalNumHj"));
					vo.setTimeValue(rs.getString("timeValue")); 
					return vo;
				}
			});
			
			DBObject query = new BasicDBObject();
			query.put("timeValue", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_HOSPITAL_DAY_LEAVE", query);//删除原来的数据
			
			if(list!=null && list.size()>0){
				List<DBObject> userList = new ArrayList<DBObject>();
					for(HospitaldayVo vo:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("leaveHospitalNum", vo.getLeaveHospitalNum());
						obj.append("leaveHospitalNumHy", vo.getLeaveHospitalNumHy());
						obj.append("leaveHospitalNumHj", vo.getLeaveHospitalNumHj());
						obj.append("leaveHospitalNumZd", vo.getLeaveHospitalNumZd());
						obj.append("timeValue", vo.getTimeValue());
						userList.add(obj);
					}
				new MongoBasicDao().insertDataByList(menuAlias+"_HOSPITAL_DAY_LEAVE", userList);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_HOSPITAL_DAY_LEAVE", list, date);
		}
		if(StringUtils.isNotBlank(date)){
			//手术例树
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<HospitaldayVo> list=null;
			StringBuffer buffer=new StringBuffer();
			buffer.append("select count(1) operationNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as operationNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as operationNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as operationNumHj,'"+date+"' timeValue  "
					+ "from t_operation_record t where t.createtime >=to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.createtime <=to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') and t.del_flg = 0 and t.stop_flg = 0 and t.Ynvalid = 1");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setOperationNum(rs.getString("operationNum")); 
					vo.setOperationNumHy(rs.getString("operationNumHy")); 
					vo.setOperationNumZd(rs.getString("operationNumZd")); 
					vo.setOperationNumHj(rs.getString("operationNumHj"));
					vo.setTimeValue(rs.getString("timeValue")); 
					return vo;
				}
			});
			
			DBObject query = new BasicDBObject();
			query.put("timeValue", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_HOSPITAL_DAY_OPERATION", query);//删除原来的数据
			
			if(list!=null && list.size()>0){
				List<DBObject> userList = new ArrayList<DBObject>();
					for(HospitaldayVo vo:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("operationNum", vo.getOperationNum());
						obj.append("operationNumHy", vo.getOperationNumHy());
						obj.append("operationNumHj", vo.getOperationNumHj());
						obj.append("operationNumZd", vo.getOperationNumZd());
						obj.append("timeValue", vo.getTimeValue());
						userList.add(obj);
					}
				new MongoBasicDao().insertDataByList(menuAlias+"_HOSPITAL_DAY_OPERATION", userList);
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias+"_HOSPITAL_DAY_OPERATION", list, date);
		}
			
	}

	@Override
	public List<HospitaldayVo> querymz(List<String> tnL, String parameter,
			String startTime, String date) {
		List<HospitaldayVo> list = new ArrayList<HospitaldayVo>();
		//门诊收入
		try {	
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(date);
			Date eTime = DateUtils.parseDateY_M_D(date);
			//2.获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			//5.定义常量
			List<String> tnL1 = new ArrayList<String>();
			//6.判断是否查询分区
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//只查询分区表
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",date,date);
				}else{//查询在线表及分区表
					//获取时间差（年）
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), date);
					//获取相差年份的分区集合 
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL1.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{
				//只查询在线表
				tnL1.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			StringBuffer buffer=new StringBuffer();
			buffer.append(" select sum(t.tot_cost) cost,'hyY' flag from "+tnL1.get(0)+" t left join T_CHARGE_MINFEETOSTAT cc on  cc.MINFEE_CODE=t.FEE_CODE "
					+ "left join t_department pp on pp.dept_code=t.REG_DPCD where t.CANCEL_FLAG = 1  and cc.report_code='MZ01' "
					+ "and (cc.fee_stat_code='01' or cc.fee_stat_code='02' or cc.fee_stat_code='03') and pp.dept_area_code=1 "
					+ " and t.PAY_FLAG = 1 and t.FEE_DATE >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ " and t.FEE_DATE <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss')   "
					+ "	union  "
					+ " select sum(t.tot_cost) cost,'zdY' flag "
					+ " from "+tnL1.get(0)+" t left join T_CHARGE_MINFEETOSTAT cc on  cc.MINFEE_CODE=t.FEE_CODE left join t_department pp on pp.dept_code=t.REG_DPCD "
					+ " where t.CANCEL_FLAG = 1  and cc.report_code='MZ01' and (cc.fee_stat_code='01' or cc.fee_stat_code='02' or cc.fee_stat_code='03') and pp.dept_area_code=2 "
					+ " and t.PAY_FLAG = 1 and t.FEE_DATE >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ " and t.FEE_DATE <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss')   "
					+ " union "
					+ " select sum(t.tot_cost) cost,'hjY' flag "
					+ " from "+tnL1.get(0)+" t left join T_CHARGE_MINFEETOSTAT cc on  cc.MINFEE_CODE=t.FEE_CODE left join t_department pp on pp.dept_code=t.REG_DPCD "
					+ " where t.CANCEL_FLAG = 1  and cc.report_code='MZ01' and (cc.fee_stat_code='01' or cc.fee_stat_code='02' or cc.fee_stat_code='03') and pp.dept_area_code=3 "
					+ " and t.PAY_FLAG = 1 and t.FEE_DATE >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.FEE_DATE <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss')    "
					+ " union "
					+ " select sum(t.tot_cost) cost,'hyZJE' flag "
					+ " from "+tnL1.get(0)+" t left join T_CHARGE_MINFEETOSTAT cc on  cc.MINFEE_CODE=t.FEE_CODE left join t_department pp on pp.dept_code=t.REG_DPCD "
					+ " where t.CANCEL_FLAG = 1  and cc.report_code='MZ01' and pp.dept_area_code=1 "
					+ " and t.PAY_FLAG = 1 and t.FEE_DATE >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')  "
					+	 "   and t.FEE_DATE <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss')   "
					+ " union  "
					+ " select sum(t.tot_cost) cost,'zdZJE' flag "
					+ "  from "+tnL1.get(0)+" t left join T_CHARGE_MINFEETOSTAT cc on  cc.MINFEE_CODE=t.FEE_CODE left join t_department pp on pp.dept_code=t.REG_DPCD "
					+ " where t.CANCEL_FLAG = 1  and cc.report_code='MZ01' and  pp.dept_area_code=2 "
					+ "   and t.PAY_FLAG = 1 and t.FEE_DATE >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "   and t.FEE_DATE <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ " union "
					+ " select sum(t.tot_cost) cost,'hjZJE' flag "
					+ "  from "+tnL1.get(0)+" t left join T_CHARGE_MINFEETOSTAT cc on  cc.MINFEE_CODE=t.FEE_CODE left join t_department pp on pp.dept_code=t.REG_DPCD "
					+ " where t.CANCEL_FLAG = 1  and cc.report_code='MZ01' and  pp.dept_area_code=3 "
					+ "   and t.PAY_FLAG = 1 and t.FEE_DATE >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "   and t.FEE_DATE <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss')   ");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setCost(rs.getString("cost")); 
					vo.setFlag(rs.getString("flag")); 
					return vo;
				}
			});
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<HospitaldayVo> queryHospitaldayVoin(String date) {
		List<HospitaldayVo> list=null;
		//在院人次初始化
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			StringBuffer buffer=new StringBuffer();
			buffer.append(" SELECT sum(pp.inpatientNum) inpatientNum,sum(pp.inpatientNumHy) inpatientNumHy,sum(pp.inpatientNumZd) inpatientNumZd,sum(pp.inpatientNumHj) inpatientNumHj,pp.timeValue as timeValue FROM ( ");
			buffer.append("select count(1) as inpatientNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as inpatientNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as inpatientNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as inpatientNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info t where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')");
			buffer.append(" union ");
			buffer.append("select count(1) as inpatientNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as inpatientNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as inpatientNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as inpatientNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info_now t where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')"
					+ " union"
					+ " select count(1) as inpatientNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as inpatientNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as inpatientNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as inpatientNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info_now t where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') and t.in_state='I'"
					+ ")pp group by pp.timeValue");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setInpatientNum(rs.getString("inpatientNum")); 
					vo.setInpatientNumHy(rs.getString("inpatientNumHy")); 
					vo.setInpatientNumZd(rs.getString("inpatientNumZd")); 
					vo.setInpatientNumHj(rs.getString("inpatientNumHj"));
					vo.setTimeValue(rs.getString("timeValue")); 
					return vo;
				}
			});
		}
		return list;
	}

	@Override
	public List<HospitaldayVo> queryHospitaldayVoopear(String date) {
		List<HospitaldayVo> list=null;
		if(StringUtils.isNotBlank(date)){
			//手术例树
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			StringBuffer buffer=new StringBuffer();
			buffer.append("select count(1) operationNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as operationNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as operationNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as operationNumHj,'"+date+"' timeValue  "
					+ "from t_operation_record t where t.createtime >=to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.createtime <=to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') and t.del_flg = 0 and t.stop_flg = 0 and t.Ynvalid = 1");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setOperationNum(rs.getString("operationNum")); 
					vo.setOperationNumHy(rs.getString("operationNumHy")); 
					vo.setOperationNumZd(rs.getString("operationNumZd")); 
					vo.setOperationNumHj(rs.getString("operationNumHj"));
					vo.setTimeValue(rs.getString("timeValue")); 
					return vo;
				}
			});
		}
		return list;
	}

	@Override
	public List<HospitaldayVo> queryHospitaldayVooutpa(String date) {
		List<HospitaldayVo> list=null;
		if(StringUtils.isNotBlank(date)){
			//出院人次初始化
			Date beginDate=new Date();
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			StringBuffer buffer=new StringBuffer();
			buffer.append(" SELECT sum(leaveHospitalNum) leaveHospitalNum,sum(leaveHospitalNumHy) leaveHospitalNumHy,sum(leaveHospitalNumZd) leaveHospitalNumZd,sum(leaveHospitalNumHj) leaveHospitalNumHj,pp.timeValue as timeValue FROM ( ");
			buffer.append("select count(1) as leaveHospitalNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as leaveHospitalNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as leaveHospitalNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as leaveHospitalNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info t where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss')");
			buffer.append(" union ");
			buffer.append("select count(1) as leaveHospitalNum,count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=1)) as leaveHospitalNumHy,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=2)) as leaveHospitalNumZd,"
					+ "count((select p.dept_code from t_department p where p.dept_code=t.dept_code and p.dept_area_code=3)) as leaveHospitalNumHj,"
					+ "'"+date+"' timeValue  "
					+ "from t_inpatient_info_now t where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss'))pp group by pp.timeValue");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
				@Override
				public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					HospitaldayVo vo = new HospitaldayVo();
					vo.setLeaveHospitalNum(rs.getString("leaveHospitalNum")); 
					vo.setLeaveHospitalNumHy(rs.getString("leaveHospitalNumHy")); 
					vo.setLeaveHospitalNumZd(rs.getString("leaveHospitalNumZd")); 
					vo.setLeaveHospitalNumHj(rs.getString("leaveHospitalNumHj"));
					vo.setTimeValue(rs.getString("timeValue")); 
					return vo;
				}
			});
		}
		return list;
	}

	@Override
	public List<HospitaldayVo> queryList(String date) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select "+date+" as timeValue,");
		buffer.append("sum(case when t.yq=0 then t.ghhj_rs else 0 end) as outpatientNum,");
		buffer.append("sum(case when t.yq=1 then t.ghhj_rs else 0 end) as outpatientNumHy,");
		buffer.append("sum(case when t.yq=2 then t.ghhj_rs else 0 end) as outpatientNumZd,");
		buffer.append("sum(case when t.yq=3 then t.ghhj_rs else 0 end) as outpatientNumHj,");
		buffer.append("sum(case when t.yq=0 then t.zyhj_num else 0 end) as inpatientNum,");
		buffer.append("sum(case when t.yq=1 then t.zyhj_num else 0 end) as inpatientNumHy,");
		buffer.append("sum(case when t.yq=2 then t.zyhj_num else 0 end) as inpatientNumZd,");
		buffer.append("sum(case when t.yq=3 then t.zyhj_num else 0 end) as inpatientNumHj,");
		buffer.append("sum(case when t.yq=0 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end) as operationNum,");
		buffer.append("sum(case when t.yq=1 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end) as operationNumHy,");
		buffer.append("sum(case when t.yq=2 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end) as operationNumZd,");
		buffer.append("sum(case when t.yq=3 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end) as operationNumHj,");
		buffer.append("sum(case when t.yq=0 then (t.MZYP_COST + t.MZYL_COST + t.ZYYP_COST + t.ZYYL_COST) else 0 end) as incomeCost,");
		buffer.append("sum(case when t.yq=1 then (t.MZYP_COST + t.MZYL_COST + t.ZYYP_COST + t.ZYYL_COST) else 0 end) as incomeCostHy,");
		buffer.append("sum(case when t.yq=2 then (t.MZYP_COST + t.MZYL_COST + t.ZYYP_COST + t.ZYYL_COST) else 0 end) as incomeCostZd,");
		buffer.append("sum(case when t.yq=3 then (t.MZYP_COST + t.MZYL_COST + t.ZYYP_COST + t.ZYYL_COST) else 0 end) as incomeCostHj,");
		buffer.append("sum(case when t.yq=0 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end) as leaveHospitalNum,");
		buffer.append("sum(case when t.yq=1 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end) as leaveHospitalNumHy,");
		buffer.append("sum(case when t.yq=2 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end) as leaveHospitalNumZd,");
		buffer.append("sum(case when t.yq=3 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end) as leaveHospitalNumHj,");
		buffer.append("sum(case when t.yq=0 then t.RY_NUM else 0 end) as inHospitalNum,");
		buffer.append("sum(case when t.yq=1 then t.RY_NUM else 0 end) as inHospitalNumHy,");
		buffer.append("sum(case when t.yq=2 then t.RY_NUM else 0 end) as inHospitalNumZd,");
		buffer.append("sum(case when t.yq=3 then t.RY_NUM else 0 end) as inHospitalNumHj,");
		buffer.append("sum(case when t.yq=0 then (t.MZYP_COST+ZYYP_COST) else 0 end) as drugProportion,");
		buffer.append("sum(case when t.yq=1 then (t.MZYP_COST+ZYYP_COST) else 0 end) as drugProportionHy,");
		buffer.append("sum(case when t.yq=2 then (t.MZYP_COST+ZYYP_COST) else 0 end) as drugProportionZd,");
		buffer.append("sum(case when t.yq=3 then (t.MZYP_COST+ZYYP_COST) else 0 end) as drugProportionHj");
		buffer.append("  from T_BUSINESS_YZCX_ZHCX t where t.oper_date = to_date('"+date+"', 'yyyy-mm-dd')");
		List<HospitaldayVo> list = jdbcTemplate.query(buffer.toString(),new RowMapper<HospitaldayVo>() {
			@Override
			public HospitaldayVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				HospitaldayVo vo = new HospitaldayVo();
				vo.setTimeValue(rs.getString("timeValue")); 
				vo.setOutpatientNum(rs.getString("outpatientNum")); 
				vo.setOutpatientNumHy(rs.getString("outpatientNumHy")); 
				vo.setOutpatientNumZd(rs.getString("outpatientNumZd")); 
				vo.setOutpatientNumHj(rs.getString("outpatientNumHj")); 
				vo.setInpatientNum(rs.getString("inpatientNum")); 
				vo.setInpatientNumHy(rs.getString("inpatientNumHy")); 
				vo.setInpatientNumZd(rs.getString("inpatientNumZd"));
				vo.setInpatientNumHj(rs.getString("inpatientNumHj")); 
				vo.setOperationNum(rs.getString("operationNum")); 
				vo.setOperationNumHy(rs.getString("operationNumHy")); 
				vo.setOperationNumZd(rs.getString("operationNumZd")); 
				vo.setOperationNumHj(rs.getString("operationNumHj")); 
				vo.setIncomeCost(rs.getString("incomeCost")); 
				vo.setIncomeCostHy(rs.getString("incomeCostHy"));
				vo.setIncomeCostZd(rs.getString("incomeCostZd"));
				vo.setIncomeCostHj(rs.getString("incomeCostHj"));
				vo.setLeaveHospitalNum(rs.getString("leaveHospitalNum")); 
				vo.setLeaveHospitalNumHy(rs.getString("leaveHospitalNumHy"));
				vo.setLeaveHospitalNumZd(rs.getString("leaveHospitalNumZd"));
				vo.setLeaveHospitalNumHj(rs.getString("leaveHospitalNumHj"));
				vo.setInHospitalNum(rs.getString("inHospitalNum")); 
				vo.setInHospitalNumHy(rs.getString("inHospitalNumHy"));
				vo.setInHospitalNumZd(rs.getString("inHospitalNumZd"));
				vo.setInHospitalNumHj(rs.getString("inHospitalNumHj"));
				vo.setDrugProportion(rs.getString("drugProportion")); 
				vo.setDrugProportionHy(rs.getString("drugProportionHy"));
				vo.setDrugProportionZd(rs.getString("drugProportionZd"));
				vo.setDrugProportionHj(rs.getString("drugProportionHj"));
				return vo;
			}
		});
		return list;
	}

	@Override
	public Map<String, Object> queryDate(String begin, String end,
			String queryMong,String rows,String page) {
		BasicDBObject bdObject = new BasicDBObject();
		
		
		if(StringUtils.isNotBlank(begin)||StringUtils.isNotBlank(end)){
			BasicDBList condList = new BasicDBList(); 
			if(StringUtils.isNotBlank(begin)){
				BasicDBObject bdObjectTimeS = new BasicDBObject();
				bdObjectTimeS.put("timeValue",new BasicDBObject("$gte",begin));
				condList.add(bdObjectTimeS);
			}
			if(StringUtils.isNotBlank(end)){
				BasicDBObject bdObjectTimeE = new BasicDBObject();
				bdObjectTimeE.put("timeValue",new BasicDBObject("$lte",end));
				condList.add(bdObjectTimeE);
			}
			bdObject.put("$and", condList);
		}
		//查询总条数
		Long total= new MongoBasicDao().findAllCountBy(queryMong, bdObject);
		
		DBCursor cursor = new MongoBasicDao().findAllDataSortBy(queryMong, "timeValue", bdObject, Integer.parseInt(rows), Integer.parseInt(page));
		DBObject dbCursor;
		List<HospitaldayVo> hostList=new ArrayList();
		while(cursor.hasNext()){
		 	dbCursor = cursor.next();
		 	HospitaldayVo vo = new HospitaldayVo();
			vo.setTimeValue((String)dbCursor.get(("timeValue"))); 
			vo.setOutpatientNum((String)dbCursor.get("outpatientNum")); 
			vo.setOutpatientNumHy((String)dbCursor.get("outpatientNumHy")); 
			vo.setOutpatientNumZd((String)dbCursor.get("outpatientNumZd")); 
			vo.setOutpatientNumHj((String)dbCursor.get("outpatientNumHj")); 
			vo.setInpatientNum((String)dbCursor.get("inpatientNum")); 
			vo.setInpatientNumHy((String)dbCursor.get("inpatientNumHy")); 
			vo.setInpatientNumZd((String)dbCursor.get("inpatientNumZd"));
			vo.setInpatientNumHj((String)dbCursor.get("inpatientNumHj")); 
			vo.setOperationNum((String)dbCursor.get("operationNum")); 
			vo.setOperationNumHy((String)dbCursor.get("operationNumHy")); 
			vo.setOperationNumZd((String)dbCursor.get("operationNumZd")); 
			vo.setOperationNumHj((String)dbCursor.get("operationNumHj")); 
			vo.setIncomeCost((String)dbCursor.get("incomeCost")); 
			vo.setIncomeCostHy((String)dbCursor.get("incomeCostHy"));
			vo.setIncomeCostZd((String)dbCursor.get("incomeCostZd"));
			vo.setIncomeCostHj((String)dbCursor.get("incomeCostHj"));
			vo.setLeaveHospitalNum((String)dbCursor.get("leaveHospitalNum")); 
			vo.setLeaveHospitalNumHy((String)dbCursor.get("leaveHospitalNumHy"));
			vo.setLeaveHospitalNumZd((String)dbCursor.get("leaveHospitalNumZd"));
			vo.setLeaveHospitalNumHj((String)dbCursor.get("leaveHospitalNumHj"));
			vo.setInHospitalNum((String)dbCursor.get("inHospitalNum")); 
			vo.setInHospitalNumHy((String)dbCursor.get("inHospitalNumHy"));
			vo.setInHospitalNumZd((String)dbCursor.get("inHospitalNumZd"));
			vo.setInHospitalNumHj((String)dbCursor.get("inHospitalNumHj"));
			vo.setDrugProportion((String)dbCursor.get("drugProportion")); 
			vo.setDrugProportionHy((String)dbCursor.get("drugProportionHy"));
			vo.setDrugProportionZd((String)dbCursor.get("drugProportionZd"));
			vo.setDrugProportionHj((String)dbCursor.get("drugProportionHj"));
			hostList.add(vo);
		}
		Map<String,Object> returnMap=new HashMap();
		returnMap.put("total", total);
		returnMap.put("rows", hostList);
		
		return returnMap;
	}
	
	
}
