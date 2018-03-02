package cn.honry.inner.statistics.peopleNumOfOperation.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.peopleNumOfOperation.dao.InnerPeopleNumOfOperationDao;
import cn.honry.inner.statistics.peopleNumOfOperation.vo.PeopleNumOfOperationVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("innerPeopleNumOfOperationDao")
public class InnerPeopleNumOfOperationDaoImpl implements InnerPeopleNumOfOperationDao {
	private DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private final String[] inpatientInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	
	@Override
	public void init_SSKSSSRSTJ(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			date=date.substring(0,7);
			String begin=date+"-01 00:00:00";
			Calendar ca=Calendar.getInstance(Locale.CHINESE);
			String end=null;
			try {
				ca.setTime(df.parse(date+"-01"));
				ca.add(Calendar.MONTH, 1);
				ca.add(Calendar.DATE, -1);
				end=df.format(ca.getTime())+" 23:59:59";
			} catch (ParseException e) {
				e.printStackTrace();
			}
			List<String> tnL= wordLoadDocDao.returnInTables(begin, end, inpatientInfo, "ZY");
		if(tnL!=null&&tnL.size()>0){
			final StringBuffer sb = new StringBuffer(800);
			sb.append(" select b.dept_code deptCode,b.dept_name,count(distinct ss.clinic_code) as optnums,count(ss.id) as optcounts,");
			sb.append(" count(distinct info.inpatient_no) as cyzs, to_char(info.out_date,'yyyy-MM') as finalDate,");
			sb.append(" nvl(cs.totalpatient,0) as totalpatient from t_department b inner join (" );
			for(int i=0,len=tnL.size();i<len;i++){
				sb.append("select i"+i+".inpatient_no,i"+i+".out_date,i"+i+".dept_code,i"+i+".in_state ");
				sb.append("from "+tnL.get(i)+" i"+i);
			}
			sb.append(") info on b.dept_code = info.dept_code ");
			sb.append(" and info.in_state = 'O' and info.out_date between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and  to_date(:end,'yyyy-MM-dd HH24:MI:SS') ");
			sb.append(" left join (select a.clinic_code, a.dept_code, a.id  from t_operation_record a where a.ynvalid = 1 and a.stop_flg = 0 ");
			sb.append(" and a.del_flg = 0) ss on b.dept_code = ss.dept_code and info.inpatient_no = ss.clinic_code  ");
			sb.append(" left join (  ");
			for(int i=0,len=tnL.size();i<len;i++){
				sb.append(" select  count(distinct i"+i+".inpatient_no )as totalpatient, to_char(i"+i+".out_date, 'yyyy-MM') as finalDate ");
				sb.append(" from t_inpatient_info i"+i+" ");
				sb.append(" join t_operation_record t1 on i"+i+".inpatient_no = t1.clinic_code  and t1.ynvalid = 1 and t1.del_flg = 0  and t1.stop_flg = 0 ");
				sb.append(" and i"+i+".out_date Between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and to_date(:end,'yyyy-MM-dd HH24:MI:SS' ) where i"+i+".in_state = 'O'");
				sb.append(" group by  to_char(i"+i+".out_date,'yyyy-MM')");
			}
			
			sb.append(" ) cs on to_char(info.out_date,'yyyy-MM') = cs.finalDate ");
			sb.append("group by b.dept_name ,to_char(info.out_date,'yyyy-MM'),cs.totalpatient,b.dept_code");
			Map<String,Object> paramMap=new HashMap<String, Object>();  
			paramMap.put("begin", begin);
			paramMap.put("end", end);
			final DecimalFormat df = new DecimalFormat("######0.00"); 
			List<PeopleNumOfOperationVo> vos = namedParameterJdbcTemplate.query(sb.toString(), paramMap, new RowMapper<PeopleNumOfOperationVo>() {
				@Override
				public PeopleNumOfOperationVo mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					PeopleNumOfOperationVo vo = new PeopleNumOfOperationVo();
					vo.setDept_name(rs.getString("dept_name"));
					vo.setOptnums(rs.getDouble("optnums"));
					vo.setOptcounts(rs.getDouble("optcounts"));
					vo.setCyzs(rs.getDouble("cyzs"));
					vo.setDeptCode(rs.getString("deptCode"));
					vo.setTotalPatient(rs.getDouble("totalpatient"));
					Double totalpatient=rs.getDouble("totalpatient");
					if(totalpatient.equals(0.0d)){
						totalpatient=1.0;
					}
					vo.setSsrszqyb(df.format((rs.getDouble("optnums")/totalpatient)*100));
					vo.setSszb(df.format((rs.getDouble("optnums")/rs.getDouble("cyzs"))*100));
					vo.setFinalDate(rs.getString("finalDate"));
					return vo;
				}
			});
			
			DBObject query = new BasicDBObject();
			query.put("finalDate", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_MONTH", query);//删除原来的数据
			
			 if(vos!=null && vos.size()>0){
					for(PeopleNumOfOperationVo vo:vos){
						
						    Document document1 = new Document();
							document1.append("finalDate", vo.getFinalDate());//统计时间
							document1.append("dept_name", vo.getDept_name());//科室名称
							document1.append("deptCode", vo.getDeptCode());//科室名称
							Document document = new Document();
							document.append("finalDate", vo.getFinalDate());//统计时间
							document.append("dept_name", vo.getDept_name());
							document.append("deptCode", vo.getDeptCode());//科室名称
							document.append("optnums", vo.getOptnums());
							document.append("optcounts", vo.getOptcounts());
							document.append("cyzs", vo.getCyzs());
							document.append("totalPatient", vo.getTotalPatient());
							document.append("sszb", vo.getSszb());
							document.append("ssrszqyb", vo.getSsrszqyb());
							new MongoBasicDao().update(menuAlias+"_MONTH", document1, document, true);
					}
			}
			 wordLoadDocDao.saveMongoLog(beginDate, menuAlias, vos, date);
		}
		
		}
	}
	
	@Override
	public void init_SSKSSSRSTJ_Day(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();
			String begin=date+" 00:00:00";
			String end=date+" 23:59:59";
			final StringBuffer sql = new StringBuffer(800);
			sql.append("SELECT  (select t.dept_name from t_department t where t.dept_code= b.dept_name) dept_name,b.dept_name deptCode, b.optnums, b.optcounts,cast('"+date+"' as varchar(10)) as findDate, b.cyzs, b.transOut, b.outPatTrans, b.operaPro, b.totalPatient, b.ssrszqybb  FROM( ");
			sql.append("Select dept_name As dept_name,optnums As optnums,optcounts As optcounts,cyzs As cyzs,transOut As transOut,"
					+ "outPatTrans As outPatTrans,operaPro As operaPro,totalPatient As totalPatient,ssrszqybb As ssrszqybb  "
					+ "From (select dept_name,optnums,optcounts,cyzs,transOut,(cyzs+transOut) outPatTrans,"
					+ "round((optnums * 100) / decode((cyzs+transOut),0,1,(cyzs+transOut)), 2) operaPro,totalPatient,"
					+ "round((optnums * 100) / decode(nvl(totalPatient,0),0,1,totalPatient), 2) ssrszqybb "
					+ "from (select nvl(dept_code, 'total') dept_name,sum(optnums) optnums,sum(optcounts) optcounts,sum(num) cyzs,"
					+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg  "
					+ "where cg.date_stat>=TO_DATE('"+begin+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and cg.date_stat<=TO_DATE('"+end+"','yyyy-mm-dd hh24:mi:ss')  and cg.dept_code=ASD.dept_code) transOut,"
					+ "(select sum(ccg.out_normal) from t_inpatient_dayreport ccg  "
					+ "where ccg.date_stat >= TO_DATE('"+begin+"','yyyy-mm-dd hh24:mi:ss')"
					+ "and ccg.date_stat <= TO_DATE('"+end+"','yyyy-mm-dd hh24:mi:ss')) totalPatient  "
					+ "from (select c.dept_code,count(distinct c.inpatient_no) num, sum(decode(c.in_times, '1', 1, '0')) firstInpat,"
					+ "sum(case when c.OPERATION_CODE is null or c.OPERATION_CODE = '' then 0 else  1 end) optnums,"
					+ "(select sum(AA.optcounts) from (select BB.*, row_number() over(partition by BB.inpatient_no order by BB.oper_type desc) as row_index  "
					+ "from (select p.inpatient_no, p.oper_type, count(1) optcounts from t_operation_detail p group by p.inpatient_no, p.oper_type) BB) AA  "
					+ "where AA.row_index = 1 and AA.inpatient_no = c.inpatient_no) optcounts, (select sum(CB.beforeoper_days) "
					+ "from (select p.*, row_number() over(partition by p.inpatient_no order by p.oper_type desc) as row_index  "
					+ " from t_operation_detail p  where p.stat_flag = '0') CB where CB.row_index = 1  and CB.inpatient_no = c.inpatient_no) beforeOperaDay,"
					+ "sum(c.pi_days) outPatBedDay from t_emr_base c  "
					+ "where trunc(c.out_date) >= TO_DATE('"+begin+"','yyyy-mm-dd hh24:mi:ss')  "
					+ "and trunc(c.out_date) <= TO_DATE('"+end+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and c.case_stus in ('3', '4') group by c.dept_code, c.inpatient_no) ASD  group by dept_code) CC )) b ");
					List<PeopleNumOfOperationVo> vos =  namedParameterJdbcTemplate.query(sql.toString(),new HashMap<String,String>(),new RowMapper<PeopleNumOfOperationVo>() {
						@Override
						public PeopleNumOfOperationVo mapRow(ResultSet rs, int rowNum)throws SQLException {
							PeopleNumOfOperationVo vo = new PeopleNumOfOperationVo();
							vo.setDept_name(rs.getString("dept_name"));
							vo.setDeptCode(rs.getString("deptCode"));
							vo.setOptnums(rs.getDouble("optnums"));
							vo.setOptcounts(rs.getDouble("optcounts"));
							vo.setCyzs(rs.getDouble("cyzs"));
							vo.setTransOut(rs.getString("transOut"));
							vo.setOutPatTrans(rs.getString("outPatTrans"));
							vo.setOperaPro(rs.getString("operaPro"));
							vo.setTotalPatient(rs.getDouble("totalPatient"));
							vo.setSsrszqybb(rs.getDouble("ssrszqybb"));
							vo.setFinalDate(rs.getString("findDate"));
							return vo;
						}
					});
			
			DBObject query = new BasicDBObject();
			query.put("finalDate", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
			 if(vos!=null && vos.size()>0){
				 List<DBObject> voList = new ArrayList<DBObject>();
					for(PeopleNumOfOperationVo vo:vos){
						if(vo.getOptnums()==null){
							vo.setOptnums(0.0d);
						}
						if(vo.getOptcounts()==null){
							vo.setOptcounts(0.0d);
						}
						if(vo.getCyzs()==null){
							vo.setCyzs(0.0d);
						}
						if(StringUtils.isBlank(vo.getSsrszqyb())){
							vo.setSsrszqyb("0");
						}
						if(vo.getSsrszqybb()==null){
							vo.setSsrszqybb(0.0d);
						}
						if(StringUtils.isBlank(vo.getOperaPro())){
							vo.setOperaPro("0");
						}
						if(StringUtils.isBlank(vo.getTransOut())){
							vo.setTransOut("0");
						}
						BasicDBObject obj = new BasicDBObject();
						obj.append("finalDate", vo.getFinalDate());//统计时间
						obj.append("dept_name", vo.getDept_name());
						obj.append("deptCode", vo.getDeptCode());
						obj.append("optnums", vo.getOptnums());
						obj.append("optcounts", vo.getOptcounts());
						obj.append("cyzs", vo.getCyzs());
						obj.append("totalPatient", vo.getTotalPatient());
						obj.append("operaPro", vo.getOperaPro());
						obj.append("ssrszqyb", vo.getSsrszqybb());
						obj.append("transOut", vo.getTransOut());
						voList.add(obj);
					}
				new MongoBasicDao().insertDataByList(menuAlias+"_DAY", voList);//添加新数据
			}
			 wordLoadDocDao.saveMongoLog(beginDate, menuAlias, vos, date);
		}
		
	}
	

}
