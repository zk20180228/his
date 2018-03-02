package cn.honry.inner.statistics.hospitalday.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessYzcxzhcx;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.hospitalday.dao.HospitaldayinnerDao;
import cn.honry.inner.statistics.hospitalday.vo.HospitaldayVo;
import cn.honry.inner.statistics.hospitalday.vo.ResultVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository("hospitaldayinnerDao")
@SuppressWarnings({ "all" })
public class HospitaldayinnerDaoImpl extends HibernateEntityDao<BusinessYzcxzhcx> implements HospitaldayinnerDao{
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
	public void init_YYMRHZ(String menuAlias, String type, String date) {
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
	public List<HospitaldayVo> queryHospitaldayList(List<String> tnL,
			String parameter, String startTime, String endTime) {
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
	public List<ResultVo> queryzhuyanList(List<String> tnL, String endTime) {
		String begin=endTime+" 00:00:00";//开始时间
		String end=endTime+" 23:59:59";//结束时间
		List<ResultVo> list=null;
			StringBuffer buffer=new StringBuffer();
			buffer.append("select dd.inhos_deptcode inhosDeptcode,sum(case when dd.fee_stat_code = 01 then dd.tot_cost else 0 end) as totCost01, "
					+ "sum(case when dd.fee_stat_code = 02 then dd.tot_cost else 0 end) as totCost02,"
					+ "sum(case when dd.fee_stat_code = 03 then dd.tot_cost else 0 end) as totCost03,"
					+ "sum(case when dd.fee_stat_code = 04 then dd.tot_cost else 0 end) as totCost04,"
					+ "sum(case when dd.fee_stat_code = 05 then dd.tot_cost else 0 end) as totCost05,"
					+ "sum(case when dd.fee_stat_code = 07 then dd.tot_cost else 0 end) as totCost07,"
					+ "sum(case when dd.fee_stat_code = 08 then dd.tot_cost else 0 end) as totCost08,"
					+ "sum(case when dd.fee_stat_code = 09 then dd.tot_cost else 0 end) as totCost09,"
					+ "sum(case when dd.fee_stat_code = 10 then dd.tot_cost else 0 end) as totCost10,"
					+ "sum(case when dd.fee_stat_code = 11 then dd.tot_cost else 0 end) as totCost11,"
					+ "sum(case when dd.fee_stat_code = 12 then dd.tot_cost else 0 end) as totCost12,"
					+ "sum(case when dd.fee_stat_code = 13 then dd.tot_cost else 0 end) as totCost13,"
					+ "sum(case when dd.fee_stat_code = 14 then dd.tot_cost else 0 end) as totCost14,"
					+ "sum(case when dd.fee_stat_code = 15 then dd.tot_cost else 0 end) as totCost15,"
					+ "sum(case when dd.fee_stat_code = 16 then dd.tot_cost else 0 end) as totCost16"
					+ "  from (select sum(t.tot_cost) as tot_cost,"
					+ " t.inhos_deptcode,p.fee_stat_code from t_inpatient_feeinfo_now t "
					+ " join t_charge_minfeetostat p on t.fee_code = p.minfee_code "
					+ " where p.report_code = 'ZY01' "
					+ "  and t.CREATETIME >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ "  and t.CREATETIME <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') "
					+ " group by t.inhos_deptcode, p.fee_stat_code) dd group by inhos_deptcode");
			list = jdbcTemplate.query(buffer.toString(),new RowMapper<ResultVo>() {
				@Override
				public ResultVo mapRow(ResultSet rs, int rowNum)throws SQLException {
					ResultVo vo = new ResultVo();
					vo.setInhosDeptcode(rs.getString("inhosDeptcode")); 
					vo.setTotCost01(rs.getDouble("totCost01")); 
					vo.setTotCost02(rs.getDouble("totCost02"));
					vo.setTotCost03(rs.getDouble("totCost03")); 
					vo.setTotCost04(rs.getDouble("totCost04")); 
					vo.setTotCost05(rs.getDouble("totCost05")); 
					vo.setTotCost07(rs.getDouble("totCost07")); 
					vo.setTotCost08(rs.getDouble("totCost08")); 
					vo.setTotCost09(rs.getDouble("totCost09")); 
					vo.setTotCost10(rs.getDouble("totCost10")); 
					vo.setTotCost11(rs.getDouble("totCost11")); 
					vo.setTotCost12(rs.getDouble("totCost12")); 
					vo.setTotCost13(rs.getDouble("totCost13")); 
					vo.setTotCost14(rs.getDouble("totCost14")); 
					vo.setTotCost15(rs.getDouble("totCost15")); 
					vo.setTotCost16(rs.getDouble("totCost16")); 
					return vo;
				}
			});
		return list;
	}
	@Override
	public List<HospitaldayVo> queryListinner(String date) {
		final StringBuffer buffer=new StringBuffer();
		buffer.append("select "+date+" as timeValue,");
		buffer.append("to_char(sum(case when t.yq=0 then t.ghhj_rs else 0 end)) as outpatientNum,");
		buffer.append("to_char(sum(case when t.yq=1 then t.ghhj_rs else 0 end)) as outpatientNumHy,");
		buffer.append("to_char(sum(case when t.yq=2 then t.ghhj_rs else 0 end)) as outpatientNumZd,");
		buffer.append("to_char(sum(case when t.yq=3 then t.ghhj_rs else 0 end)) as outpatientNumHj,");
		buffer.append("to_char(sum(case when t.yq=0 then t.zyhj_num else 0 end)) as inpatientNum,");
		buffer.append("to_char(sum(case when t.yq=1 then t.zyhj_num else 0 end)) as inpatientNumHy,");
		buffer.append("to_char(sum(case when t.yq=2 then t.zyhj_num else 0 end)) as inpatientNumZd,");
		buffer.append("to_char(sum(case when t.yq=3 then t.zyhj_num else 0 end)) as inpatientNumHj,");
		buffer.append("to_char(sum(case when t.yq=0 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end)) as operationNum,");
		buffer.append("to_char(sum(case when t.yq=1 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end)) as operationNumHy,");
		buffer.append("to_char(sum(case when t.yq=2 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end)) as operationNumZd,");
		buffer.append("to_char(sum(case when t.yq=3 then (t.MZSS_NUM+t.ZYSS_NUM) else 0 end)) as operationNumHj,");
		buffer.append("to_char(sum(case when t.yq=0 then (t.MZYP_COST+MZYL_COST+ZYYP_COST+ZYYL_COST) else 0 end)) as incomeCost,");
		buffer.append("to_char(sum(case when t.yq=1 then (t.MZYP_COST+MZYL_COST+ZYYP_COST+ZYYL_COST) else 0 end)) as incomeCostHy,");
		buffer.append("to_char(sum(case when t.yq=2 then (t.MZYP_COST+MZYL_COST+ZYYP_COST+ZYYL_COST) else 0 end)) as incomeCostZd,");
		buffer.append("to_char(sum(case when t.yq=3 then (t.MZYP_COST+MZYL_COST+ZYYP_COST+ZYYL_COST) else 0 end)) as incomeCostHj,");
		buffer.append("to_char(sum(case when t.yq=0 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end)) as leaveHospitalNum,");
		buffer.append("to_char(sum(case when t.yq=1 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end)) as leaveHospitalNumHy,");
		buffer.append("to_char(sum(case when t.yq=2 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end)) as leaveHospitalNumZd,");
		buffer.append("to_char(sum(case when t.yq=3 then (t.CYWJ_NUM+CYYJ_NUM) else 0 end)) as leaveHospitalNumHj,");
		buffer.append("to_char(sum(case when t.yq=0 then t.RY_NUM else 0 end)) as inHospitalNum,");
		buffer.append("to_char(sum(case when t.yq=1 then t.RY_NUM else 0 end)) as inHospitalNumHy,");
		buffer.append("to_char(sum(case when t.yq=2 then t.RY_NUM else 0 end)) as inHospitalNumZd,");
		buffer.append("to_char(sum(case when t.yq=3 then t.RY_NUM else 0 end)) as inHospitalNumHj,");
		buffer.append("to_char(sum(case when t.yq=0 then (t.MZYP_COST+ZYYP_COST) else 0 end)) as drugProportion,");
		buffer.append("to_char(sum(case when t.yq=1 then (t.MZYP_COST+ZYYP_COST) else 0 end)) as drugProportionHy,");
		buffer.append("to_char(sum(case when t.yq=2 then (t.MZYP_COST+ZYYP_COST) else 0 end)) as drugProportionZd,");
		buffer.append("to_char(sum(case when t.yq=3 then (t.MZYP_COST+ZYYP_COST) else 0 end)) as drugProportionHj");
		buffer.append("  from T_BUSINESS_YZCX_ZHCX t where t.oper_date = to_date('"+date+"', 'yyyy-mm-dd')");
		List<HospitaldayVo> list = (List<HospitaldayVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("outpatientNum",Hibernate.STRING).addScalar("outpatientNumHy",Hibernate.STRING)
						.addScalar("outpatientNumZd",Hibernate.STRING).addScalar("outpatientNumHj",Hibernate.STRING)
						.addScalar("inpatientNum",Hibernate.STRING).addScalar("inpatientNumHy",Hibernate.STRING)
						.addScalar("inpatientNumZd",Hibernate.STRING).addScalar("inpatientNumHj",Hibernate.STRING)
						.addScalar("operationNum",Hibernate.STRING).addScalar("operationNumHy",Hibernate.STRING)
						.addScalar("operationNumZd",Hibernate.STRING).addScalar("operationNumHj",Hibernate.STRING)
						.addScalar("incomeCost",Hibernate.STRING).addScalar("incomeCostHy",Hibernate.STRING)
						.addScalar("incomeCostZd",Hibernate.STRING).addScalar("incomeCostHj",Hibernate.STRING)
						.addScalar("leaveHospitalNum",Hibernate.STRING).addScalar("leaveHospitalNumHy",Hibernate.STRING)
						.addScalar("leaveHospitalNumZd",Hibernate.STRING).addScalar("leaveHospitalNumHj",Hibernate.STRING)
						.addScalar("inHospitalNum",Hibernate.STRING).addScalar("inHospitalNumHy",Hibernate.STRING)
						.addScalar("inHospitalNumZd",Hibernate.STRING).addScalar("inHospitalNumHj",Hibernate.STRING)
						.addScalar("drugProportion",Hibernate.STRING).addScalar("drugProportionHy",Hibernate.STRING)
						.addScalar("drugProportionZd",Hibernate.STRING).addScalar("drugProportionHj",Hibernate.STRING);
				return queryObject.setResultTransformer(Transformers.aliasToBean(HospitaldayVo.class)).list();
			}
		});
		return list;
	}
	@Override
	public void save(BusinessYzcxzhcx businessYzcxzhcx) {
		super.save(businessYzcxzhcx);
		super.flush();
	}
	@Override
	public BusinessYzcxzhcx queryBusinessYzcxzhcx(Date operDate, String yq) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select t.PZ_NUM    as pzNum,t.JYMZ_NUM  as jymzNum,");
		buffer.append("t.QTPT_NUM  as qtptNum,");
		buffer.append("t.ZMZJ_NUM  as zmzjNum,");
		buffer.append("t.JS_NUM    as jsNum,");
		buffer.append("t.FJS_NUM   as fjsNum,");
		buffer.append("t.JZ_NUM    as jzNum,");
		buffer.append("t.SFHJ_NUM  as sfhjNum,");
		buffer.append("t.SFZF_NUM  as sfzfNum,");
		buffer.append("t.CFHJ_NUM  as cfhjNum,");
		buffer.append("t.CFZF_NUM  as cfzfNum,");
		buffer.append("t.ZDCF_COST as zdcfCost,");
		buffer.append("t.ZXCF_COST as zxcfCost,");
		buffer.append("t.CFHJ_COST as cfhjCost,");
		buffer.append("t.RY_NUM    as ryNum,");
		buffer.append("t.ZK_NUM    as zkNum,");
		buffer.append("t.CYWJ_NUM  as cywjNum,");
		buffer.append("t.CYYJ_NUM  as cyyjNum,");
		buffer.append("t.ZYHJ_NUM  as zyhjNum,");
		buffer.append("t.ZYZF_NUM  as zyzfNum,");
		buffer.append("t.ZYNH_NUM  as zynhNum,");
		buffer.append("t.MZYP_COST as mzypCost,");
		buffer.append("t.MZYL_COST as mzylCost,");
		buffer.append("t.ZYYP_COST as zyypCost,");
		buffer.append("t.ZYYL_COST as zyylCost,");
		buffer.append("t.MZSS_NUM  as mzssNum,");
		buffer.append("t.MZSS_COST as mzssCost,");
		buffer.append("t.ZYSS_NUM  as zyssNum,");
		buffer.append("t.ZYSS_COST as zyssCost,");
		buffer.append("t.OPER_DATE as operDate,");
		buffer.append("t.BED_BZ    as bedBz,");
		buffer.append("t.SJRY_NUM  as sjryNum,");
		buffer.append("t.GHSR_COST as ghsrCost,");
		buffer.append("t.TSH_NUM   as tshNum,");
		buffer.append("t.GHHJ_RS   as ghhjRs,");
		buffer.append("t.YQ        as yq,");
		buffer.append("t.GHHJ_NUM  as ghhjNum,");
		buffer.append("t.SSTS      as ssts ");
		buffer.append("from T_BUSINESS_YZCX_ZHCX t ");
		buffer.append("where t.OPER_DATE = to_date('"+operDate+"', 'yyyy-MM-dd') ");
		buffer.append("and t.YQ = '"+yq+"'");
		List<BusinessYzcxzhcx> list = (List<BusinessYzcxzhcx>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("pzNum",Hibernate.BIG_DECIMAL).addScalar("jymzNum",Hibernate.BIG_DECIMAL)
						.addScalar("qtptNum",Hibernate.BIG_DECIMAL).addScalar("zmzjNum",Hibernate.BIG_DECIMAL)
						.addScalar("jsNum",Hibernate.BIG_DECIMAL).addScalar("fjsNum",Hibernate.BIG_DECIMAL)
						.addScalar("jzNum",Hibernate.BIG_DECIMAL).addScalar("sfhjNum",Hibernate.BIG_DECIMAL)
						.addScalar("sfzfNum",Hibernate.BIG_DECIMAL).addScalar("cfhjNum",Hibernate.BIG_DECIMAL)
						.addScalar("cfzfNum",Hibernate.BIG_DECIMAL).addScalar("zdcfCost",Hibernate.BIG_DECIMAL)
						.addScalar("zxcfCost",Hibernate.BIG_DECIMAL).addScalar("cfhjCost",Hibernate.BIG_DECIMAL)
						.addScalar("ryNum",Hibernate.BIG_DECIMAL).addScalar("zkNum",Hibernate.BIG_DECIMAL)
						.addScalar("cywjNum",Hibernate.BIG_DECIMAL).addScalar("cyyjNum",Hibernate.BIG_DECIMAL)
						.addScalar("zyhjNum",Hibernate.BIG_DECIMAL).addScalar("zyzfNum",Hibernate.BIG_DECIMAL)
						.addScalar("zynhNum",Hibernate.BIG_DECIMAL).addScalar("mzypCost",Hibernate.BIG_DECIMAL)
						.addScalar("mzylCost",Hibernate.BIG_DECIMAL).addScalar("zyypCost",Hibernate.BIG_DECIMAL)
						.addScalar("zyylCost",Hibernate.BIG_DECIMAL).addScalar("mzssNum",Hibernate.BIG_DECIMAL)
						.addScalar("mzssCost",Hibernate.BIG_DECIMAL).addScalar("zyssNum",Hibernate.BIG_DECIMAL)
						.addScalar("zyssCost",Hibernate.BIG_DECIMAL).addScalar("operDate",Hibernate.DATE)
						.addScalar("bedBz",Hibernate.BIG_DECIMAL).addScalar("sjryNum",Hibernate.BIG_DECIMAL)
						.addScalar("ghsrCost",Hibernate.BIG_DECIMAL).addScalar("tshNum",Hibernate.BIG_DECIMAL)
						.addScalar("ghhjRs",Hibernate.BIG_DECIMAL).addScalar("yq",Hibernate.STRING)
						.addScalar("ghhjNum",Hibernate.BIG_DECIMAL).addScalar("ssts",Hibernate.BIG_DECIMAL);
				return queryObject.setResultTransformer(Transformers.aliasToBean(BusinessYzcxzhcx.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public String update(BusinessYzcxzhcx businessYzcxzhcx) {
		BigDecimal mzcost = businessYzcxzhcx.getMzssCost();
		if(mzcost==null){
			mzcost = new BigDecimal(0);
		}
		BigDecimal zycost = businessYzcxzhcx.getZyssCost();
		if(zycost==null){
			zycost = new BigDecimal(0);
		}
		BigDecimal bedBz = businessYzcxzhcx.getBedBz();
		if(bedBz==null){
			bedBz = new BigDecimal(0);
		}
		BigDecimal sjryNum = businessYzcxzhcx.getSjryNum();
		if(sjryNum==null){
			sjryNum = new BigDecimal(0);
		}
		String hql="update BusinessYzcxzhcx set pzNum=?, jymzNum=?,qtptNum=?,zmzjNum=?,jsNum=?,fjsNum=?,jzNum=?,"
				+ "sfhjNum=?,sfzfNum=?,cfhjNum=?,cfzfNum=?,zdcfCost=?,zxcfCost=?,cfhjCost=?,ryNum=?,zkNum=?,"
				+ "cywjNum=?,cyyjNum=?,zyhjNum=?,zyzfNum=?,zynhNum=?,mzypCost=?,mzylCost=?,zyypCost=?,zyylCost=?,"
				+ "mzssNum=?,mzssCost=?,zyssNum=?,zyssCost=?,bedBz=?,sjryNum=?,ghsrCost=?,tshNum=?,ghhjRs=?,ghhjNum=?,ssts=? where "
				+ "operDate = ?  and yq = ?";
		int c = super.excUpdateHql(hql, new Object[]{businessYzcxzhcx.getPzNum(),businessYzcxzhcx.getJymzNum(),businessYzcxzhcx.getQtptNum(),
				businessYzcxzhcx.getZmzjNum(),businessYzcxzhcx.getJsNum(),businessYzcxzhcx.getFjsNum(),businessYzcxzhcx.getJzNum(),
				businessYzcxzhcx.getSfhjNum(),businessYzcxzhcx.getSfzfNum(),businessYzcxzhcx.getCfhjNum(),businessYzcxzhcx.getCfzfNum(),
				businessYzcxzhcx.getZdcfCost(),businessYzcxzhcx.getZxcfCost(),businessYzcxzhcx.getCfhjCost(),businessYzcxzhcx.getRyNum(),
				businessYzcxzhcx.getZkNum(),businessYzcxzhcx.getCywjNum(),businessYzcxzhcx.getCyyjNum(),businessYzcxzhcx.getZyhjNum(),
				businessYzcxzhcx.getZyzfNum(),businessYzcxzhcx.getZynhNum(),businessYzcxzhcx.getMzypCost(),businessYzcxzhcx.getMzylCost(),
				businessYzcxzhcx.getZyypCost(),businessYzcxzhcx.getZyylCost(),businessYzcxzhcx.getMzssNum(),mzcost,
				businessYzcxzhcx.getZyssNum(),zycost,bedBz,sjryNum,
				businessYzcxzhcx.getGhsrCost(),businessYzcxzhcx.getTshNum(),businessYzcxzhcx.getGhhjRs(),businessYzcxzhcx.getGhhjNum(),
				businessYzcxzhcx.getSsts(),businessYzcxzhcx.getOperDate(),businessYzcxzhcx.getYq()});
		if(c>0){
			return "ok";
		}else{
			return "error";
		}
	}
	@Override
	public List<BusinessYzcxzhcx> queryBusinessYzcxzhcxMaxdate() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select ");
		buffer.append(" t.OPER_DATE as operDate ");
		buffer.append(" from T_BUSINESS_YZCX_ZHCX t ");
		buffer.append("order by t.OPER_DATE desc ");
		List<BusinessYzcxzhcx> list = (List<BusinessYzcxzhcx>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("operDate",Hibernate.DATE);
				return queryObject.setResultTransformer(Transformers.aliasToBean(BusinessYzcxzhcx.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public List<BusinessYzcxzhcx> queryBusinessYzcxzhcx(String date) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select t.PZ_NUM    as pzNum,t.JYMZ_NUM  as jymzNum,");
		buffer.append("t.QTPT_NUM  as qtptNum,");
		buffer.append("t.ZMZJ_NUM  as zmzjNum,");
		buffer.append("t.JS_NUM    as jsNum,");
		buffer.append("t.FJS_NUM   as fjsNum,");
		buffer.append("t.JZ_NUM    as jzNum,");
		buffer.append("t.SFHJ_NUM  as sfhjNum,");
		buffer.append("t.SFZF_NUM  as sfzfNum,");
		buffer.append("t.CFHJ_NUM  as cfhjNum,");
		buffer.append("t.CFZF_NUM  as cfzfNum,");
		buffer.append("t.ZDCF_COST as zdcfCost,");
		buffer.append("t.ZXCF_COST as zxcfCost,");
		buffer.append("t.CFHJ_COST as cfhjCost,");
		buffer.append("t.RY_NUM    as ryNum,");
		buffer.append("t.ZK_NUM    as zkNum,");
		buffer.append("t.CYWJ_NUM  as cywjNum,");
		buffer.append("t.CYYJ_NUM  as cyyjNum,");
		buffer.append("t.ZYHJ_NUM  as zyhjNum,");
		buffer.append("t.ZYZF_NUM  as zyzfNum,");
		buffer.append("t.ZYNH_NUM  as zynhNum,");
		buffer.append("t.MZYP_COST as mzypCost,");
		buffer.append("t.MZYL_COST as mzylCost,");
		buffer.append("t.ZYYP_COST as zyypCost,");
		buffer.append("t.ZYYL_COST as zyylCost,");
		buffer.append("t.MZSS_NUM  as mzssNum,");
		buffer.append("t.MZSS_COST as mzssCost,");
		buffer.append("t.ZYSS_NUM  as zyssNum,");
		buffer.append("t.ZYSS_COST as zyssCost,");
		buffer.append("t.OPER_DATE as operDate,");
		buffer.append("t.BED_BZ    as bedBz,");
		buffer.append("t.SJRY_NUM  as sjryNum,");
		buffer.append("t.GHSR_COST as ghsrCost,");
		buffer.append("t.TSH_NUM   as tshNum,");
		buffer.append("t.GHHJ_RS   as ghhjRs,");
		buffer.append("t.YQ        as yq,");
		buffer.append("t.GHHJ_NUM  as ghhjNum,");
		buffer.append("t.SSTS      as ssts ");
		buffer.append("from T_BUSINESS_YZCX_ZHCX t ");
		buffer.append("where t.OPER_DATE = to_date('"+date+"', 'yyyy-MM-dd') ");
		List<BusinessYzcxzhcx> list = (List<BusinessYzcxzhcx>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("pzNum",Hibernate.BIG_DECIMAL).addScalar("jymzNum",Hibernate.BIG_DECIMAL)
						.addScalar("qtptNum",Hibernate.BIG_DECIMAL).addScalar("zmzjNum",Hibernate.BIG_DECIMAL)
						.addScalar("jsNum",Hibernate.BIG_DECIMAL).addScalar("fjsNum",Hibernate.BIG_DECIMAL)
						.addScalar("jzNum",Hibernate.BIG_DECIMAL).addScalar("sfhjNum",Hibernate.BIG_DECIMAL)
						.addScalar("sfzfNum",Hibernate.BIG_DECIMAL).addScalar("cfhjNum",Hibernate.BIG_DECIMAL)
						.addScalar("cfzfNum",Hibernate.BIG_DECIMAL).addScalar("zdcfCost",Hibernate.BIG_DECIMAL)
						.addScalar("zxcfCost",Hibernate.BIG_DECIMAL).addScalar("cfhjCost",Hibernate.BIG_DECIMAL)
						.addScalar("ryNum",Hibernate.BIG_DECIMAL).addScalar("zkNum",Hibernate.BIG_DECIMAL)
						.addScalar("cywjNum",Hibernate.BIG_DECIMAL).addScalar("cyyjNum",Hibernate.BIG_DECIMAL)
						.addScalar("zyhjNum",Hibernate.BIG_DECIMAL).addScalar("zyzfNum",Hibernate.BIG_DECIMAL)
						.addScalar("zynhNum",Hibernate.BIG_DECIMAL).addScalar("mzypCost",Hibernate.BIG_DECIMAL)
						.addScalar("mzylCost",Hibernate.BIG_DECIMAL).addScalar("zyypCost",Hibernate.BIG_DECIMAL)
						.addScalar("zyylCost",Hibernate.BIG_DECIMAL).addScalar("mzssNum",Hibernate.BIG_DECIMAL)
						.addScalar("mzssCost",Hibernate.BIG_DECIMAL).addScalar("zyssNum",Hibernate.BIG_DECIMAL)
						.addScalar("zyssCost",Hibernate.BIG_DECIMAL).addScalar("operDate",Hibernate.DATE)
						.addScalar("bedBz",Hibernate.BIG_DECIMAL).addScalar("sjryNum",Hibernate.BIG_DECIMAL)
						.addScalar("ghsrCost",Hibernate.BIG_DECIMAL).addScalar("tshNum",Hibernate.BIG_DECIMAL)
						.addScalar("ghhjRs",Hibernate.BIG_DECIMAL).addScalar("yq",Hibernate.STRING)
						.addScalar("ghhjNum",Hibernate.BIG_DECIMAL).addScalar("ssts",Hibernate.BIG_DECIMAL);
				return queryObject.setResultTransformer(Transformers.aliasToBean(BusinessYzcxzhcx.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessYzcxzhcx>();
	}
}
