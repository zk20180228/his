package cn.honry.statistics.bi.bistac.kpi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisChargeVo;
import cn.honry.statistics.bi.bistac.kpi.dao.KpiDao;
import cn.honry.statistics.bi.bistac.kpi.vo.KpiVo;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Repository("kpiDao")
@SuppressWarnings({"all"})
public class KpiDaoImpl extends HibernateEntityDao<KpiVo> implements KpiDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	private int[] param;
	private List<Object[]> object = new ArrayList<Object[]>();
	@Override
	public StatVo findMaxMin() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime ,MIN(mn.REG_DATE) AS sTime FROM T_REGISTER_MAIN_NOW mn";
	     List<StatVo> list =namedParameterJdbcTemplate.query(sql, new RowMapper<StatVo>() {
				@Override
				public StatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StatVo vo  = new StatVo();
					vo.setsTime(rs.getDate("sTime"));
					vo.seteTime(rs.getDate("eTime"));
					return vo;
				}
			});
		return list.get(0); 
	}
	@Override
	public List<Object[]> queryEverMonth(List<String> tnL, String danw, String time) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		buffer.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		buffer1.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		buffer2.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		buffer3.append("select * from(SELECT SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '12', 1, 0)) M12  "
				+ "FROM T_OPERATION_RECORD t  WHERE  t.OPS_KIND='2' and 	TO_CHAR(t.OPERATIONDATE , 'yyyy') = :data )");
		buffer4.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
				buffer1.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
				buffer2.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
				buffer4.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
			}
			buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer.append("WHERE TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
			buffer1.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer1.append("WHERE  t.EMERGENCY_FLAG=0 and 	TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
			buffer2.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer2.append("left join T_EMPLOYEE e on e.EMPLOYEE_JOBNO = t.DOCT_CODE WHERE TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
			buffer4.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer4.append("WHERE  t.EMERGENCY_FLAG=1 and TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
		}
		buffer.append(")");
		buffer1.append(")");
		buffer2.append(")");
		buffer4.append(")");
		SQLQuery queryObject = (SQLQuery) this.getSession().createSQLQuery(buffer.toString()).setParameter("data", time);
		SQLQuery queryObject1 = (SQLQuery) this.getSession().createSQLQuery(buffer1.toString()).setParameter("data", time);
		SQLQuery queryObject2 = (SQLQuery) this.getSession().createSQLQuery(buffer2.toString()).setParameter("data", time);
		SQLQuery queryObject3 = (SQLQuery) this.getSession().createSQLQuery(buffer3.toString()).setParameter("data", time);
		SQLQuery queryObject4 = (SQLQuery) this.getSession().createSQLQuery(buffer4.toString()).setParameter("data", time);
		Object[] s = (Object[]) queryObject.list().get(0);
		for(int i =0;i<s.length;i++){
			  if( s[i] == null){
				  s[i] =0;
			  }
		}
		Object[] s1 = (Object[]) queryObject1.list().get(0);
		for(int i =0;i<s1.length;i++){
			  if( s1[i] == null){
				  s1[i] =0;
			  }
		}
		Object[] s2 = (Object[]) queryObject2.list().get(0);
		for(int i =0;i<s2.length;i++){
			  if( s2[i] == null){
				  s2[i] =0;
			  }
		}
		Object[] s3 = (Object[]) queryObject3.list().get(0);
		for(int i =0;i<s3.length;i++){
			if( s3[i] == null){
				s3[i] =0;
			}
		}
		Object[] s4 = (Object[]) queryObject4.list().get(0);
		for(int i =0;i<s4.length;i++){
			if( s4[i] == null){
				s4[i] =0;
			}
		}
		List<Object[]> dataList = new ArrayList<Object[]>();
		dataList.add(s);
		dataList.add(s1);
		dataList.add(s2);
		dataList.add(s3);
		dataList.add(s4);
		object = dataList;
		return  dataList;
	}
	@Override
	public int[] getTotalTime(List<String> tnL, String danw, String time) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		buffer.append("select count(1) from (select t.id from ");
		buffer1.append("select count(1) from (select t.id from ");
		buffer2.append("select count(1) from (select t.id from ");
		buffer3.append("select count(1) from (select t.id from T_OPERATION_RECORD t where t.OPS_KIND='2' and( t.OPERATIONDATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss')");
		buffer3.append("and t.OPERATIONDATE <= to_date(:end, 'yyyy-MM-dd HH24:mi:ss')))");
		buffer4.append("select count(1) from (select t.id from  ");

		for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" UNION select t.id from ");
				buffer1.append(" UNION select t.id from ");
				buffer2.append(" UNION select t.id from ");
				buffer4.append(" UNION select t.id from ");
			}
			buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer.append("where t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss')");
			
			
			buffer1.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer1.append("where  t.EMERGENCY_FLAG=0 and (t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss'))");
			
			buffer2.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer2.append( " left join T_EMPLOYEE e on e.EMPLOYEE_JOBNO = t.DOCT_CODE ");
			buffer2.append("where (t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss')) and e.EMPLOYEE_ISEXPERT ='1'");
			
			
			
			buffer4.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer4.append("where  t.EMERGENCY_FLAG=1 and (t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss'))");
		}
		buffer.append(")");
		buffer1.append(")");
		buffer2.append(")");
		buffer4.append(")");
		String first ="";
		String end ="";
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if("0".equals(danw)){
			first = time +"-01-01 00:00:00";
			end = time +"-12-31 23:59:59";
			paraMap.put("first", first);
			paraMap.put("end", end);

		}else if("1".equals(danw)){
			 //获取当前时间  
	        Calendar cal = Calendar.getInstance();  
		    int year =Integer.parseInt(time.split("-")[0]);
	        //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推  
	        cal.set(Calendar.MONTH, Integer.parseInt(time.split("-")[1]));  
	        //调到上个月  
	        cal.add(Calendar.MONTH, -1);  
	        //得到一个月最最后一天日期(31/30/29/28)  
	        int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
		    first = time+"-01 00:00:00";
			end = time+"-"+MaxDay+" 23:59:59";
			paraMap.put("first", first);
			paraMap.put("end", end);
			
		}else if("2".equals(danw)){
			 //获取当前时间  
	        Calendar cal = Calendar.getInstance(); 
	        int year = cal.get(Calendar.YEAR);
	        if("1".equals(time)){
	        	first = year+"-01-01 00:00:00";
	 			end = year+"-03-31 23:59:59";
	        }else if("2".equals(time)){
	        	first = year+"-04-01 00:00:00";
	 			end = year+"-06-30 23:59:59";
	        }else if("3".equals(time)){
	        	first = year+"-07-01 00:00:00";
	        	end = year+"-09-30 23:59:59";
	        }else if("4".equals(time)){
	        	first = year+"-10-01 00:00:00";
	        	end = year+"-12-31 23:59:59";
	        }
			paraMap.put("first", first);
			paraMap.put("end", end);
		}
		int total = namedParameterJdbcTemplate.queryForObject(buffer.toString(), paraMap,java.lang.Integer.class);
		int menzhen = namedParameterJdbcTemplate.queryForObject(buffer1.toString(), paraMap,java.lang.Integer.class);
		int zhuanjia = namedParameterJdbcTemplate.queryForObject(buffer2.toString(), paraMap,java.lang.Integer.class);
		int shoushu = namedParameterJdbcTemplate.queryForObject(buffer3.toString(), paraMap,java.lang.Integer.class);
		int jizhen = namedParameterJdbcTemplate.queryForObject(buffer4.toString(), paraMap,java.lang.Integer.class);
		int[] result = new int[]{total,menzhen,zhuanjia,shoushu,jizhen};
		param = result;
		return result;	
	}
	@Override
	public int[] compareToBefore(List<String> tnL, String danw, String time) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		buffer.append("select count(1) from (select t.id from ");
		buffer1.append("select count(1) from (select t.id from ");
		buffer2.append("select count(1) from (select t.id from ");
		buffer3.append("select count(1) from (select t.id from T_OPERATION_RECORD t where t.OPS_KIND='2' and( t.OPERATIONDATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss')");
		buffer3.append("and t.OPERATIONDATE <= to_date(:end, 'yyyy-MM-dd HH24:mi:ss')))");
		buffer4.append("select count(1) from (select t.id from  ");

		for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append(" UNION select t.id from ");
				buffer1.append(" UNION select t.id from ");
				buffer2.append(" UNION select t.id from ");
				buffer4.append(" UNION select t.id from ");
			}
			buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer.append("where t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss')");
			
			
			buffer1.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer1.append("where  t.EMERGENCY_FLAG=0 and (t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss'))");
			
			buffer2.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer2.append( " left join T_EMPLOYEE e on e.EMPLOYEE_JOBNO = t.DOCT_CODE ");
			buffer2.append("where (t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss')) and e.EMPLOYEE_ISEXPERT ='1'");
			
			
			
			buffer4.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer4.append("where  t.EMERGENCY_FLAG=1 and (t.REG_DATE >= to_date(:first, 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE<= to_date(:end, 'yyyy-MM-dd HH24:mi:ss'))");
		}
		buffer.append(")");
		buffer1.append(")");
		buffer2.append(")");
		buffer4.append(")");
		String first ="";
		String end ="";
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if("0".equals(danw)){
			first = time +"-01-01 00:00:00";
			end = time +"-12-31 23:59:59";
			paraMap.put("first", first);
			paraMap.put("end", end);

		}else if("1".equals(danw)){
			 //获取当前时间  
	        Calendar cal = Calendar.getInstance();  
//	        int year = cal.get(Calendar.YEAR)-1;
	        int year =Integer.parseInt(time.split("-")[0])-1;
	        int month =Integer.parseInt(time.split("-")[1]);
	        //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推  
//	        cal.set(Calendar.MONTH, Integer.parseInt(time));  
	        cal.set(Calendar.MONTH, month);  
	        //调到上个月  
	        cal.add(Calendar.MONTH, -1);  
	        //得到一个月最最后一天日期(31/30/29/28)  
	        int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
		    first = year+"-"+month+"-01 00:00:00";
			end = year+"-"+month+"-"+MaxDay+" 23:59:59";
//			first = year+"-"+time+"-01 00:00:00";
//			end = year+"-"+time+"-"+MaxDay+" 23:59:59";
			paraMap.put("first", first);
			paraMap.put("end", end);
			
		}else if("2".equals(danw)){
			 //获取当前时间  
	        Calendar cal = Calendar.getInstance(); 
	        int year = cal.get(Calendar.YEAR)-1;
	        if("1".equals(time)){
	        	first = year+"-01-01 00:00:00";
	 			end = year+"-03-31 23:59:59";
	        }else if("2".equals(time)){
	        	first = year+"-04-01 00:00:00";
	 			end = year+"-06-30 23:59:59";
	        }else if("3".equals(time)){
	        	first = year+"-07-01 00:00:00";
	        	end = year+"-09-30 23:59:59";
	        }else if("4".equals(time)){
	        	first = year+"-10-01 00:00:00";
	        	end = year+"-12-31 23:59:59";
	        }
			paraMap.put("first", first);
			paraMap.put("end", end);
		}
		int total = namedParameterJdbcTemplate.queryForObject(buffer.toString(), paraMap,java.lang.Integer.class);
		int menzhen = namedParameterJdbcTemplate.queryForObject(buffer1.toString(), paraMap,java.lang.Integer.class);
		int zhuanjia = namedParameterJdbcTemplate.queryForObject(buffer2.toString(), paraMap,java.lang.Integer.class);
		int shoushu = namedParameterJdbcTemplate.queryForObject(buffer3.toString(), paraMap,java.lang.Integer.class);
		int jizhen = namedParameterJdbcTemplate.queryForObject(buffer4.toString(), paraMap,java.lang.Integer.class);
		int[] s = new int[]{total,menzhen,zhuanjia,shoushu,jizhen};
		int[] result = new int[5];
		for(int i=0;i<s.length;i++){
			result[i] = param[i]-s[i];
		}
		return result;
	}
	@Override
	public List<Object[]> everMonthToCom(List<String> tnL,String danw, String time) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		buffer.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		buffer1.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		buffer2.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		buffer3.append("select * from(SELECT SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.OPERATIONDATE , 'MM'), '12', 1, 0)) M12  "
				+ "FROM T_OPERATION_RECORD t  WHERE  t.OPS_KIND='2' and 	TO_CHAR(t.OPERATIONDATE , 'yyyy') = :data )");
		buffer4.append("select sum(M1),sum(M1),sum(M2),sum(M3),sum(M4),sum(M5),sum(M6),sum(M7),sum(M8),sum(M9),sum(M10),sum(M11),sum(M12) from ( "
				+ "SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
				+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				buffer.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
				buffer1.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
				buffer2.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
				buffer4.append("UNION SELECT SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '01', 1,0)) M1, SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '02', 1,0)) M2,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '03', 1, 0)) M3,   SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '04', 1, 0)) M4,  "
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '05', 1, 0)) M5,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '06', 1, 0)) M6,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '07', 1, 0)) M7,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '08',1, 0)) M8,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '09',1, 0)) M9,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '10', 1, 0)) M10,"
						+ "SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '11', 1, 0)) M11,  SUM(DECODE(TO_CHAR(t.REG_DATE , 'MM'), '12', 1, 0)) M12  FROM ");
			}
			buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer.append("WHERE TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
			buffer1.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer1.append("WHERE  t.EMERGENCY_FLAG=0 and 	TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
			buffer2.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer2.append("left join T_EMPLOYEE e on e.EMPLOYEE_JOBNO = t.DOCT_CODE WHERE TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
			buffer4.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
			buffer4.append("WHERE  t.EMERGENCY_FLAG=1 and TO_CHAR(t.REG_DATE , 'yyyy') = :data ");
		}
		buffer.append(")");
		buffer1.append(")");
		buffer2.append(")");
		buffer4.append(")");
		SQLQuery queryObject = (SQLQuery) this.getSession().createSQLQuery(buffer.toString()).setParameter("data", time);
		SQLQuery queryObject1 = (SQLQuery) this.getSession().createSQLQuery(buffer1.toString()).setParameter("data", time);
		SQLQuery queryObject2 = (SQLQuery) this.getSession().createSQLQuery(buffer2.toString()).setParameter("data", time);
		SQLQuery queryObject3 = (SQLQuery) this.getSession().createSQLQuery(buffer3.toString()).setParameter("data", time);
		SQLQuery queryObject4 = (SQLQuery) this.getSession().createSQLQuery(buffer4.toString()).setParameter("data", time);
		Object[] s = (Object[]) queryObject.list().get(0);
		for(int i =0;i<s.length;i++){
			  if( s[i] == null){
				  s[i] =0;
			  }
		}
		Object[] s1 = (Object[]) queryObject1.list().get(0);
		for(int i =0;i<s1.length;i++){
			  if( s1[i] == null){
				  s1[i] =0;
			  }
		}
		Object[] s2 = (Object[]) queryObject2.list().get(0);
		for(int i =0;i<s2.length;i++){
			  if( s2[i] == null){
				  s2[i] =0;
			  }
		}
		Object[] s3 = (Object[]) queryObject3.list().get(0);
		for(int i =0;i<s3.length;i++){
			if( s3[i] == null){
				s3[i] =0;
			}
		}
		Object[] s4 = (Object[]) queryObject4.list().get(0);
		for(int i =0;i<s4.length;i++){
			if( s4[i] == null){
				s4[i] =0;
			}
		}
		List<Object[]> dataList = new ArrayList<Object[]>();
		dataList.add(s);
		dataList.add(s1);
		dataList.add(s2);
		dataList.add(s3);
		dataList.add(s4);
		return  dataList;
	}
	
	/**查询当天的门急诊人次
	 * zhangkui
	 * 2017-05-12
	 */
	public int getMJZCount() {
		Date date = new Date();
		String mid  = DateUtils.formatDateY_M_D(date);
		String today= mid+" 23:59:59";
		String yesterday = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(mid), -1))+" 23:59:59";
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT COUNT(1) FROM T_REGISTER_MAIN_NOW T ");
		sb.append(" WHERE T.DEL_FLG=0 AND T.STOP_FLG=0 AND T.TRANS_TYPE=1 AND T.VALID_FLAG=1 ");
		sb.append(" AND  T.REG_DATE> TO_DATE('"+yesterday+"','YYYY-MM-DD HH24:MI:SS') AND T.REG_DATE<=TO_DATE('"+today+"','YYYY-MM-DD HH24:MI:SS')");
		//this.getSession().createCriteria(sql);
		return jdbcTemplate.queryForObject(sb.toString(), null, Integer.class);
	}
	@Override
	public List<KpiVo> initMZKPI(String beginDate, String endDate) {
		List<String> tnL= getTnL(beginDate, endDate);
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		buffer.append("select count(1) as num from (select t.id from ");
		buffer1.append("select count(1) as num from (select t.id from ");
		buffer2.append("select count(1) as num from (select t.id from ");
		buffer3.append("select count(1) as num from (select t.id from T_OPERATION_RECORD t where t.OPS_KIND='2' and( t.OPERATIONDATE >= to_date('"+beginDate+"', 'yyyy-MM-dd HH24:mi:ss')");
		buffer3.append("and t.OPERATIONDATE < to_date('"+endDate+"', 'yyyy-MM-dd HH24:mi:ss')))");
		buffer4.append("select count(1) as num from  (select t.id from  ");
		if(tnL!=null||tnL!=null){
			for(int i=0;i<tnL.size();i++){
				if(i>0){
					buffer.append(" UNION select t.id from ");
					buffer1.append(" UNION select t.id from ");
					buffer2.append(" UNION select t.id from ");
					buffer4.append(" UNION select t.id from ");
				}
				buffer.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
				buffer.append("where t.REG_DATE >= to_date('"+beginDate+"', 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE< to_date('"+endDate+"', 'yyyy-MM-dd HH24:mi:ss')");
				
				
				buffer1.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
				buffer1.append("where  t.EMERGENCY_FLAG=0 and (t.REG_DATE >= to_date('"+beginDate+"', 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE< to_date('"+endDate+"', 'yyyy-MM-dd HH24:mi:ss'))");
				
				buffer2.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
				buffer2.append( " left join T_EMPLOYEE e on e.EMPLOYEE_JOBNO = t.DOCT_CODE ");
				buffer2.append("where (t.REG_DATE >= to_date('"+beginDate+"', 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE< to_date('"+endDate+"', 'yyyy-MM-dd HH24:mi:ss')) and e.EMPLOYEE_ISEXPERT ='1'");
				
				
				
				buffer4.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t ");
				buffer4.append("where  t.EMERGENCY_FLAG=1 and (t.REG_DATE >= to_date('"+beginDate+"', 'yyyy-MM-dd HH24:mi:ss') and t.REG_DATE< to_date('"+endDate+"', 'yyyy-MM-dd HH24:mi:ss'))");
			}
		}
		buffer.append(")");
		buffer1.append(")");
		buffer2.append(")");
		buffer4.append(")");
		StringBuffer sb = new StringBuffer();
		sb.append("select num  as numFix from ( "+buffer+" union all  "+buffer1+" union all "+buffer2+" union all "+buffer3+" union all "+buffer4+" )");
		List<KpiVo> list=namedParameterJdbcTemplate.query(sb.toString(),new RowMapper<KpiVo>(){

			@Override
			public KpiVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				KpiVo vo = new KpiVo();
				vo.setNumFix(rs.getString("numFix"));
				return vo;
			}
		
		});
		
	    if(list!=null && list.size()>0){
        	return list;
        }
        return new ArrayList<KpiVo>();
	}
	public List<String> getTnL(String startTime,String endTime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date sTime = DateUtils.parseDateY_M_D(startTime);//当天
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		
		List<String> tnL = new ArrayList<String>();
		//获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		//获得当前时间
		Date dTime=null;
		try {
			dTime = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",startTime,endTime);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
				tnL.add(0,"T_REGISTER_MAIN_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_REGISTER_MAIN_NOW");
		}	
		return tnL;
	}
}
