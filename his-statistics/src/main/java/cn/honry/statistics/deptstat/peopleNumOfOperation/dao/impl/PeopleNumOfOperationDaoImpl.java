package cn.honry.statistics.deptstat.peopleNumOfOperation.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.peopleNumOfOperation.dao.PeopleNumOfOperationDao;
import cn.honry.statistics.deptstat.peopleNumOfOperation.vo.PeopleNumOfOperationVo;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("peopleNumOfOperationDao")
public class PeopleNumOfOperationDaoImpl extends HibernateEntityDao<PeopleNumOfOperationVo> implements PeopleNumOfOperationDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<PeopleNumOfOperationVo> listPeopleNumOfOperation(List<String> tnL, final String page,final String rows,String begin, String end) throws Exception{
		if(tnL==null||tnL.size()<0){
			return new ArrayList<PeopleNumOfOperationVo>();
		}
		final StringBuffer sb = new StringBuffer(1500);
		sb.append(" select b.dept_name,count(distinct ss.clinic_code) as optnums,count(ss.id) as optcounts,");
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
		sb.append("group by b.dept_name ,to_char(info.out_date,'yyyy-MM'),cs.totalpatient");
		Map<String,Object> paramMap=new HashMap<String, Object>();  
		paramMap.put("begin", begin);
		paramMap.put("end", end);
		final DecimalFormat df = new DecimalFormat("######0.00"); 
		List<PeopleNumOfOperationVo> vos = namedParameterJdbcTemplate.query(sb.toString(), paramMap, new RowMapper<PeopleNumOfOperationVo>() {
			@Override
			public PeopleNumOfOperationVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				PeopleNumOfOperationVo vo = new PeopleNumOfOperationVo();
				vo.setDept_name(rs.getString("dept_name"));
				vo.setOptnums(rs.getDouble("optnums"));
				vo.setOptcounts(rs.getDouble("optcounts"));
				vo.setCyzs(rs.getDouble("cyzs"));
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
		if(vos!=null&&vos.size()>0){
			return vos;
		}
		return new ArrayList<PeopleNumOfOperationVo>();
	}
	@Override
	public Integer findTotalRecord(String begin, String end) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1)");
		sb.append(" from (");
		sb.append(" select b.dept_name,count(distinct ss.clinic_code) as optnums,count(ss.id) as optcounts,");
		sb.append(" count(distinct info.inpatient_no) as cyzs,(select  count(distinct t.inpatient_no) from t_inpatient_info t");
		sb.append(" join t_operation_record t1 on t.inpatient_no =t1.clinic_code and t1.ynvalid=1 and t1.del_flg=0 and t1.stop_flg=0 ");
		sb.append(" and t.out_date between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and to_date(:end,'yyyy-MM-dd HH24:MI:SS') ");
		sb.append(" where t.in_state = 'O') as totalpatient , to_char(info.out_date,'yyyy-MM') as finalDate  from t_department b inner join t_inpatient_info info on b.dept_code = info.dept_code ");
		sb.append(" and info.in_state = 'O' and info.out_date between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and to_date(:end,'yyyy-MM-dd HH24:MI:SS') ");
		sb.append(" left join (select a.clinic_code, a.dept_code, a.id  from t_operation_record a where a.ynvalid = 1 and a.stop_flg = 0 ");
		sb.append(" and a.del_flg = 0) ss on b.dept_code = ss.dept_code and info.inpatient_no = ss.clinic_code group by b.dept_name ,to_char(info.out_date,'yyyy-MM') ");
		sb.append(")");
		Map<String,Object> paramMap=new HashMap<String, Object>();  
		paramMap.put("begin", begin);
		paramMap.put("end", end);
		return namedParameterJdbcTemplate.queryForObject(sb.toString(),paramMap, java.lang.Integer.class);
	
	}
	@Override
	public void initPeopleNumOfOperation(String begin, String end)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append(" select b.dept_name,count(distinct ss.clinic_code) as optnums,count(ss.id) as optcounts,");
		sb.append(" count(distinct info.inpatient_no) as cyzs, to_char(info.out_date,'yyyy-MM') as finalDate,");
		sb.append(" cs.totalpatient as totalpatient   from t_department b inner join t_inpatient_info info on b.dept_code = info.dept_code ");
		sb.append(" and info.in_state = 'O' and info.out_date between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and  to_date(:end,'yyyy-MM-dd HH24:MI:SS') ");
		sb.append(" left join (select a.clinic_code, a.dept_code, a.id  from t_operation_record a where a.ynvalid = 1 and a.stop_flg = 0 ");
		sb.append(" and a.del_flg = 0) ss on b.dept_code = ss.dept_code and info.inpatient_no = ss.clinic_code  ");
		sb.append(" left join (select  count(distinct t.inpatient_no )as totalpatient, to_char(t.out_date, 'yyyy-MM') as finalDate from t_inpatient_info t ");
		sb.append(" join t_operation_record t1 on t.inpatient_no = t1.clinic_code  and t1.ynvalid = 1 and t1.del_flg = 0  and t1.stop_flg = 0 ");
		sb.append(" and t.out_date Between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and to_date(:end,'yyyy-MM-dd HH24:MI:SS' ) where t.in_state = 'O'");
		sb.append(" group by  to_char(t.out_date,'yyyy-MM')) cs on to_char(info.out_date,'yyyy-MM') = cs.finalDate ");
		sb.append("group by b.dept_name ,to_char(info.out_date,'yyyy-MM'),cs.totalpatient");
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
				vo.setTotalPatient(rs.getDouble("totalpatient"));
				vo.setSsrszqyb(df.format((rs.getDouble("optnums")/rs.getDouble("totalpatient"))*100));
				vo.setSszb(df.format((rs.getDouble("optnums")/rs.getDouble("cyzs"))*100));
				vo.setFinalDate(rs.getString("finalDate"));
				return vo;
			}
		});
		 if(vos!=null && vos.size()>0){
				for(PeopleNumOfOperationVo vo:vos){
					    Document document1 = new Document();
						document1.append("finalDate", vo.getFinalDate());//统计时间
						document1.append("dept_name", vo.getDept_name());//科室名称
						Document document = new Document();
						document.append("finalDate", vo.getFinalDate());//统计时间
						document.append("dept_name", vo.getDept_name());
						document.append("optnums", vo.getOptnums());
						document.append("optcounts", vo.getOptcounts());
						document.append("cyzs", vo.getCyzs());
						document.append("totalPatient", vo.getTotalPatient());
						document.append("sszb", vo.getSszb());
						document.append("ssrszqyb", vo.getSsrszqyb());
						new MongoBasicDao().update("SSKSSSRSTJ_MONTH", document1, document, true);
				}
		}
		
	}
	@Override
	public Map<String, Object> queryForDBList(String begin, String rows,
			String page)  throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total", 0);
		map.put("rows", new ArrayList<PeopleNumOfOperationVo>());
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", begin);
		List<PeopleNumOfOperationVo> list=new ArrayList<PeopleNumOfOperationVo>();
		boolean sign=new MongoBasicDao().isCollection("SSKSSSRSTJ_MONTH");
		if(!sign){
			return map;
		}else{
			DBCursor cursor1 = new MongoBasicDao().findAlldata("SSKSSSRSTJ_MONTH",bdObject);
			DBObject dbCursor1;
			int num=0;
			while(cursor1.hasNext()){
				 dbCursor1 = cursor1.next();
				 num++;
			 }
			DBCursor cursor = new MongoBasicDao().findAllDataSortBy("SSKSSSRSTJ_MONTH", "dept_name", bdObject,Integer.parseInt(rows),Integer.parseInt( page));
			DBObject dbCursor;
			while(cursor.hasNext()){
				PeopleNumOfOperationVo voOne=new  PeopleNumOfOperationVo();
				 dbCursor = cursor.next();
					 String deptName=(String)dbCursor.get("dept_name");//科室
					 Double optnums = (Double) dbCursor.get("optnums") ;
					 Double optcounts=(Double) dbCursor.get("optcounts") ;
					 Double cyzs=(Double)dbCursor.get("cyzs");
					 Double totalPatient=(Double)dbCursor.get("totalPatient");
					 String sszb=(String)dbCursor.get("sszb");
					 String ssrszqyb=(String)dbCursor.get("ssrszqyb");
					 voOne.setDept_name(deptName);
					 voOne.setOptnums(optnums);
					 voOne.setOptcounts(optcounts);
					 voOne.setCyzs(cyzs);
					 voOne.setTotalPatient(totalPatient);
					 voOne.setSszb(sszb);
					 voOne.setSsrszqyb(ssrszqyb);
					 list.add(voOne);
			  }
			map.put("total", num);
			map.put("rows", list);
		}
		return map;
	}
	@Override
	public List<PeopleNumOfOperationVo> queryExportForDBList(String begin)  throws Exception{
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("finalDate", begin);
		DBCursor cursor = new MongoBasicDao().findAlldataBySort("SSKSSSRSTJ_MONTH", bdObject,"dept_name");
		DBObject dbCursor;
		List<PeopleNumOfOperationVo> list=new ArrayList<PeopleNumOfOperationVo>();
		while(cursor.hasNext()){
			PeopleNumOfOperationVo voOne=new  PeopleNumOfOperationVo();
			 dbCursor = cursor.next();
				 String deptName=(String)dbCursor.get("dept_name");//科室
				 Double optnums = (Double) dbCursor.get("optnums") ;
				 Double optcounts=(Double) dbCursor.get("optcounts") ;
				 Double cyzs=(Double)dbCursor.get("cyzs");
				 Double totalPatient=(Double)dbCursor.get("totalPatient");
				 String sszb=(String)dbCursor.get("sszb");
				 String ssrszqyb=(String)dbCursor.get("ssrszqyb");
				 voOne.setDept_name(deptName);
				 voOne.setOptnums(optnums);
				 voOne.setOptcounts(optcounts);
				 voOne.setCyzs(cyzs);
				 voOne.setTotalPatient(totalPatient);
				 voOne.setSszb(sszb);
				 voOne.setSsrszqyb(ssrszqyb);
				 list.add(voOne);
		  }
		return list;
	}
	@Override
	public List<PeopleNumOfOperationVo> listPeopleNumToDB(String startDate,
			String endDate) throws Exception {
		final StringBuffer sb = new StringBuffer();
		sb.append(" select b.dept_name,count(distinct ss.clinic_code) as optnums,count(ss.id) as optcounts,");
		sb.append(" count(distinct info.inpatient_no) as cyzs,(select  count(distinct t.inpatient_no) from t_inpatient_info t");
		sb.append(" join t_operation_record t1 on t.inpatient_no =t1.clinic_code and t1.ynvalid=1 and t1.del_flg=0 and t1.stop_flg=0 ");
		sb.append(" and t.out_date between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and to_date(:end,'yyyy-MM-dd HH24:MI:SS') ");
		sb.append(" where t.in_state = 'O') as totalpatient , to_char(info.out_date,'yyyy-MM') as finalDate  from t_department b inner join t_inpatient_info info on b.dept_code = info.dept_code ");
		sb.append(" and info.in_state = 'O' and info.out_date between to_date(:begin,'yyyy-MM-dd HH24:MI:SS') and to_date(:end,'yyyy-MM-dd HH24:MI:SS') ");
		sb.append(" left join (select a.clinic_code, a.dept_code, a.id  from t_operation_record a where a.ynvalid = 1 and a.stop_flg = 0 ");
		sb.append(" and a.del_flg = 0) ss on b.dept_code = ss.dept_code and info.inpatient_no = ss.clinic_code group by b.dept_name ,to_char(info.out_date,'yyyy-MM') ");
		Map<String,Object> paramMap=new HashMap<String, Object>();  
		paramMap.put("begin", startDate);
		paramMap.put("end", endDate);
		List<PeopleNumOfOperationVo> vos = namedParameterJdbcTemplate.query(sb.toString(), paramMap, new RowMapper<PeopleNumOfOperationVo>() {

			@Override
			public PeopleNumOfOperationVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				PeopleNumOfOperationVo vo = new PeopleNumOfOperationVo();
				vo.setDept_name(rs.getString("dept_name"));
				vo.setOptnums(rs.getDouble("optnums"));
				vo.setOptcounts(rs.getDouble("optcounts"));
				vo.setCyzs(rs.getDouble("cyzs"));
				vo.setTotalPatient(rs.getDouble("totalpatient"));
				return vo;
			}

			
		});
		if(vos!=null&&vos.size()>0){
			return vos;
		}
		return new ArrayList<PeopleNumOfOperationVo>();
	}
	/**  
	 * 手术科室手术人数统计(含心内)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<PeopleNumOfOperationVo> queryPeopleNumOfOperation(String startTime,String endTime,String deptCode,String menuAlias,String page, String rows) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		sql.append("SELECT * from (");
		sql.append("SELECT  ROWNUM AS n, (select t.dept_name from t_department t where t.dept_code= b.dept_name) dept_name,b.optnums,b.optcounts,b.cyzs,b.transOut,b.outPatTrans,b.operaPro,b.totalPatient,b.ssrszqybb  FROM( ");
		sql.append("Select dept_name As dept_name,optnums As optnums,optcounts As optcounts,cyzs As cyzs,transOut As transOut,"
				+ "outPatTrans As outPatTrans,operaPro As operaPro,totalPatient As totalPatient,ssrszqybb As ssrszqybb  "
				+ "From (select dept_name,optnums,optcounts,cyzs,transOut,(cyzs+transOut) outPatTrans,"
				+ "round((optnums * 100) / (cyzs+transOut), 2) operaPro,totalPatient,"
				+ "round((optnums * 100) / (totalPatient), 2) ssrszqybb "
				+ "from (select nvl(dept_code, 'total') dept_name,sum(optnums) optnums,sum(optcounts) optcounts,sum(num) cyzs,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg  "
				+ "where cg.date_stat>=TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') "
				+ "and cg.date_stat<=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
				sql.append( "  and cg.dept_code=ASD.dept_code) transOut,"
				+ "(select sum(ccg.out_normal) from t_inpatient_dayreport ccg  "
				+ "where ccg.date_stat >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ "and ccg.date_stat <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')) totalPatient  "
				+ "from (select c.dept_code,count(distinct c.inpatient_no) num, sum(decode(c.in_times, '1', 1, '0')) firstInpat,"
				+ "sum(case when c.OPERATION_CODE is null or c.OPERATION_CODE = '' then 0 else  1 end) optnums,"
				+ "(select sum(AA.optcounts) from (select BB.*, row_number() over(partition by BB.inpatient_no order by BB.oper_type desc) as row_index  "
				+ "from (select p.inpatient_no, p.oper_type, count(1) optcounts from t_operation_detail p group by p.inpatient_no, p.oper_type) BB) AA  "
				+ "where AA.row_index = 1 and AA.inpatient_no = c.inpatient_no) optcounts, (select sum(CB.beforeoper_days) "
				+ "from (select p.*, row_number() over(partition by p.inpatient_no order by p.oper_type desc) as row_index  "
				+ " from t_operation_detail p  where p.stat_flag = '0') CB where CB.row_index = 1  and CB.inpatient_no = c.inpatient_no) beforeOperaDay,"
				+ "sum(c.pi_days) outPatBedDay from t_emr_base c  "
				+ "where trunc(c.out_date) >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  "
				+ "and trunc(c.out_date) <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
				if(StringUtils.isNotBlank(deptCode)){
					sql.append(" and c.dept_code in('").append(deptCode.replace(",", "','")).append("') ");
				}else{
					sql.append(" and c.dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				sql.append(" and c.case_stus in ('3', '4') group by c.dept_code, c.inpatient_no) ASD  group by dept_code) CC ) ");
				sql.append(") b where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
				
				int start = Integer.parseInt(page == null ? "1" : page);
				int count = Integer.parseInt(rows == null ? "20" : rows);
				paraMap.put("page", start);
				paraMap.put("rows", count);
				
				List<PeopleNumOfOperationVo> PeopleNumOfOperationVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<PeopleNumOfOperationVo>() {
					@Override
					public PeopleNumOfOperationVo mapRow(ResultSet rs, int rowNum)throws SQLException {
						PeopleNumOfOperationVo vo = new PeopleNumOfOperationVo();
						vo.setDept_name(rs.getString("dept_name"));
						vo.setOptnums(rs.getDouble("optnums"));
						vo.setOptcounts(rs.getDouble("optcounts"));
						vo.setCyzs(rs.getDouble("cyzs"));
						vo.setTransOut(rs.getString("transOut"));
						vo.setOutPatTrans(rs.getString("outPatTrans"));
						vo.setOperaPro(rs.getString("operaPro"));
						vo.setTotalPatient(rs.getDouble("totalPatient"));
						vo.setSsrszqybb(rs.getDouble("ssrszqybb"));
						return vo;
					}
				});
			if(PeopleNumOfOperationVoList.size()>0){
				return PeopleNumOfOperationVoList;
			}
			return new ArrayList<PeopleNumOfOperationVo>();
	}
	
	/**  
	 * 手术科室手术人数统计(含心内)  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalPeopleNumOfOperation(String startTime, String endTime, String deptCode, String menuAlias, String page, String rows) {
		Map<String,String> map=new HashMap<String,String>();
		StringBuffer sql=new StringBuffer();
		sql.append("Select nvl(count(1),0) total From (select dept_name,optnums,optcounts,cyzs,transOut,(cyzs+transOut) outPatTrans,"
				+ "round((optnums * 100) / (cyzs+transOut), 2) operaPro,totalPatient,"
				+ "round((optnums * 100) / (totalPatient), 2) ssrszqyb "
				+ "from (select nvl(dept_code, 'total') dept_name,sum(optnums) optnums,sum(optcounts) optcounts,sum(num) cyzs,"
				+ "(select sum(cg.out_transfer) from t_inpatient_dayreport cg  "
				+ "where cg.date_stat>=TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') "
				+ "and cg.date_stat<=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')  and cg.dept_code=ASD.dept_code) transOut,"
				+ "(select sum(ccg.out_normal) from t_inpatient_dayreport ccg  "
				+ "where ccg.date_stat >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
				+ "and ccg.date_stat <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')) totalPatient  "
				+ "from (select c.dept_code,count(distinct c.inpatient_no) num, sum(decode(c.in_times, '1', 1, '0')) firstInpat,"
				+ "sum(case when c.OPERATION_CODE is null or c.OPERATION_CODE = '' then 0 else  1 end) optnums,"
				+ "(select sum(AA.optcounts) from (select BB.*, row_number() over(partition by BB.inpatient_no order by BB.oper_type desc) as row_index  "
				+ "from (select p.inpatient_no, p.oper_type, count(1) optcounts from t_operation_detail p group by p.inpatient_no, p.oper_type) BB) AA  "
				+ "where AA.row_index = 1 and AA.inpatient_no = c.inpatient_no) optcounts, (select sum(CB.beforeoper_days) "
				+ "from (select p.*, row_number() over(partition by p.inpatient_no order by p.oper_type desc) as row_index  "
				+ " from t_operation_detail p  where p.stat_flag = '0') CB where CB.row_index = 1  and CB.inpatient_no = c.inpatient_no) beforeOperaDay,"
				+ "sum(c.pi_days) outPatBedDay from t_emr_base c  "
				+ "where trunc(c.out_date) >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  "
				+ "and trunc(c.out_date) <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
				if(StringUtils.isNotBlank(deptCode)){
					sql.append(" and c.dept_code in('").append(deptCode.replace(",", "','")).append("') ");
				}else{
					sql.append(" and c.dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
			sql.append("and c.case_stus in ('3', '4') group by c.dept_code, c.inpatient_no) ASD  group by dept_code) CC ) ");
		
		return namedParameterJdbcTemplate.query(sql.toString(),map ,new RowMapper<Integer>(){
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("total");
			}
			
		}).get(0);
	}
	
	/**  
	 * 手术科室手术人数统计(含心内)  mongdb
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public  Map<String,Object> queryPeopleNumOfOperationForDB(String startTime, String endTime, String deptCode, String menuAlias, String page, String rows) {
		Map<String,Object> map=new HashMap<String, Object>();
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		
		BasicDBList mongoDeptList = new BasicDBList();
		
		bdObjectTimeS.put("finalDate",new BasicDBObject("$gte",startTime));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("finalDate",new BasicDBObject("$lte",endTime));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		
		if(StringUtils.isBlank(deptCode)){//如果科室为空 查询授权科室
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
				for(int i = 0,len=deptList.size();i<len;i++){
					mongoDeptList.add(new BasicDBObject("deptCode",deptList.get(i).getDeptCode()));
				}
				bdObject.put("$or", mongoDeptList);
		}else{
			String[] dept= deptCode.split(",");
			for(String dep:dept){
				mongoDeptList.add(new BasicDBObject("deptCode",dep));
			}
			bdObject.put("$or", mongoDeptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldataBySort("SSKSSSRSTJ_DAY", bdObject,"optnums");
		
		
		DBObject dbCursor;
		String key;//主键
		
		List<Double> value;//值
		List<Double> valueVo;//
		List<Double> tempValue;//中间list
		Map<String,List<Double>> valueMap=new LinkedHashMap<String,List<Double>>();
		while(cursor.hasNext()){
				 	dbCursor = cursor.next();
				 	
				 	valueVo=new ArrayList<Double>(6);
					 String deptName = (String) dbCursor.get("dept_name") ;//科室
					 valueVo.add((Double)dbCursor.get("optnums")) ;//手术人数
					 valueVo.add((Double)dbCursor.get("optcounts")) ;//手术例数
					 valueVo.add((Double)dbCursor.get("cyzs")) ;//出院总数
					 valueVo.add(Double.parseDouble((String)dbCursor.get("operaPro")));
					 valueVo.add((Double)dbCursor.get("ssrszqyb")) ;//手术人数占全院比
					 valueVo.add(Double.parseDouble((String)dbCursor.get("transOut")));//转出人数
					 key=deptName;
					 if(valueMap.containsKey(key)){
						 tempValue=new ArrayList<Double>(6);
						 value=valueMap.get(key);
						 for(int i=0,len=value.size();i<len;i++){
							tempValue.add(value.get(i)+valueVo.get(i)); 
						 }
						 valueMap.put(key, tempValue);
						 tempValue=null;
					 }else{
						 valueMap.put(key, valueVo);
						 valueVo=null;
					 }
					 
			}
		value=null;
		valueVo=null;
		
		StringBuffer buffer=new StringBuffer(100);
		
		buffer.append("select nvl(sum(ccg.out_normal),0) totalPatient from t_inpatient_dayreport ccg  "
		+ "where ccg.date_stat >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')"
		+ "and ccg.date_stat <= TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		Integer totalPatient=namedParameterJdbcTemplate.query(buffer.toString(),map ,new RowMapper<Integer>(){
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("totalPatient");
			}
			
		}).get(0);
		
		int total=0;
		Integer start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
		Integer end=Integer.parseInt(page)*Integer.parseInt(rows);
		List<PeopleNumOfOperationVo> list=new ArrayList<PeopleNumOfOperationVo>();
		Double temp=null;
		for(String keys:valueMap.keySet()){
			if(total>=start&&total<end){
				tempValue=valueMap.get(keys);
				PeopleNumOfOperationVo vo=new PeopleNumOfOperationVo();
				vo.setDept_name(keys);
				vo.setOptnums(tempValue.get(0));
				vo.setOptcounts(tempValue.get(1));
				vo.setCyzs(tempValue.get(2));
				vo.setTotalPatient(Double.parseDouble(totalPatient.toString()));
				temp=tempValue.get(2)+tempValue.get(5);
				vo.setOperaPro(NumberUtil.init().format(temp==null||temp==0.0d?0.0d:(tempValue.get(0)*100)/(temp),2));
				vo.setSsrszqybb(Double.parseDouble(NumberUtil.init().format(totalPatient==null||totalPatient==Integer.valueOf(0)?0.0d:(tempValue.get(0)*100)/totalPatient,2)));
				
				list.add(vo);
				if(total+1==end){
					break;
				}
			}
			total++;
		}
		map.put("total", valueMap.size());
		map.put("rows", list);
		return map;
	}
}
