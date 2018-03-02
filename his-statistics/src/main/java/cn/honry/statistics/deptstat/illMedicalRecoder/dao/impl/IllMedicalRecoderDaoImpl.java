package cn.honry.statistics.deptstat.illMedicalRecoder.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;
import cn.honry.statistics.deptstat.illMedicalRecoder.dao.IllMedicalRecoderDao;
import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("illMedicalRecoderDao")
@SuppressWarnings({ "all" })
public class IllMedicalRecoderDaoImpl extends HibernateEntityDao<IllMedicalRecoderVo> implements IllMedicalRecoderDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<IllMedicalRecoderVo> queryIllMedicalRecoder(String deptCode,String menuAlias) {
		StringBuffer sql=new StringBuffer();
		sql.append("select  t.dept_code as deptCode,t.dept_name as deptName,count(1) as num,"
				+ "round(sum(t.BALANCE_PREPAY)/count(1),2) as averFeeCost,"
				+ "round(sum(trunc(t.out_date - t.in_date ))/count(1)) as averInhost,"
				+ "sum(case when b.diag_kind = '3' then 1 else 0 end) as compliInterface, "
				+ "sum(case when b.diag_outstate = '0' then 1 else 0 end) as cured,"
				+ "sum(case when b.diag_outstate = '1' then 1 else 0 end) as better,"
				+ "sum(case when b.diag_outstate = '2' then 1 else 0 end) as noCured,"
				+ "sum(case when b.diag_outstate = '3' then 1 else 0 end) as death,"
				+ "sum(case when (t.diag_outstate = '4' or b.diag_outstate is null) then 1 else 0 end) as other,"
				+ "round((sum(case when b.diag_outstate = '0' then 1 else 0 end)/count(1)),2)*100 as cureRate,"
				+ "round((sum(case when b.diag_outstate = '3' then 1 else 0 end)/count(1)),2)*100 as deathRate,"
				+ "(select count(1) from t_inpatient_info t3 where t3.in_state='O') as allIll,"
				+ "round(( count(1)/(select count(1) from t_inpatient_info t3 where t3.in_state='O')),2)*100 as thanFloor "
				+ "FROM T_INPATIENT_INFO t left join t_business_diagnose b on t.inpatient_no = b.inpatient_no"
				+ " where t.in_state='O' "); 
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptCode)){
			deptCode=deptCode.replace(",", "','");
			sql.append("and  t.dept_code in('"+deptCode+"')");
		}else{
			sql.append("and  t.dept_code in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+")");
		}
		sql.append("group by t.dept_code,t.dept_name");
		
		
		List<IllMedicalRecoderVo> IllMedicalRecoderVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<IllMedicalRecoderVo>() {
			@Override
			public IllMedicalRecoderVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				IllMedicalRecoderVo vo = new IllMedicalRecoderVo();
				vo.setDeptName(rs.getString("deptName"));
				vo.setNum(rs.getString("num"));
				vo.setAverFeeCost(rs.getString("averFeeCost"));
				vo.setAverInhost(rs.getString("averInhost"));
				vo.setCompliInterface(rs.getString("compliInterface"));
				vo.setCured(rs.getString("cured"));
				vo.setBetter(rs.getString("better"));
				vo.setNoCured(rs.getString("noCured"));
				vo.setDeath(rs.getString("death"));
				vo.setOther(rs.getString("other"));
				vo.setCureRate(rs.getString("cureRate"));
				vo.setDeathRate(rs.getString("deathRate"));
				vo.setAllIll(rs.getString("allIll"));
				vo.setThanFloor(rs.getString("thanFloor"));
				return vo;
			}
		});
		if(IllMedicalRecoderVoList!=null&&IllMedicalRecoderVoList.size()>0){
			return IllMedicalRecoderVoList;
		}
		return new ArrayList<IllMedicalRecoderVo>();
	}

	@Override
	public void saveIllMedicalToDB(List<String> tnL, List<String> mainL, String begin, String end) {
//		new MongoBasicDao().deleteData("WZBLRSBLTJFX");
		StringBuffer sql=new StringBuffer();
		sql.append("select  t.dept_code as deptCode,t.dept_name as deptName,count(1) as num,"
				+ "round(sum(t.BALANCE_PREPAY)/count(1),2) as averFeeCost,"
				+ "round(sum(trunc(t.out_date - t.in_date ))/count(1)) as averInhost,"
				+ "sum(case when b.diag_kind = '3' then 1 else 0 end) as compliInterface, "
				+ "sum(case when b.diag_outstate = '0' then 1 else 0 end) as cured,"
				+ "sum(case when b.diag_outstate = '1' then 1 else 0 end) as better,"
				+ "sum(case when b.diag_outstate = '2' then 1 else 0 end) as noCured,"
				+ "sum(case when b.diag_outstate = '3' then 1 else 0 end) as death,"
				+ "sum(case when (t.diag_outstate = '4' or b.diag_outstate is null) then 1 else 0 end) as other,"
				+ "round((sum(case when b.diag_outstate = '0' then 1 else 0 end)/count(1)),2)*100 as cureRate,"
				+ "round((sum(case when b.diag_outstate = '3' then 1 else 0 end)/count(1)),2)*100 as deathRate,"
				+ "(select count(1) from t_inpatient_info t3 where t3.in_state='O') as allIll,"
				+ "round(( count(1)/(select count(1) from t_inpatient_info t3 where t3.in_state='O')),2)*100 as thanFloor "
				+ "FROM T_INPATIENT_INFO t left join t_business_diagnose b on t.inpatient_no = b.inpatient_no"
				+ " where t.in_state='O' "); 
		sql.append("group by t.dept_code,t.dept_name");
		List<IllMedicalRecoderVo> IllMedicalRecoderVoList =  namedParameterJdbcTemplate.query(sql.toString(),new HashMap(),new RowMapper<IllMedicalRecoderVo>() {
			@Override
			public IllMedicalRecoderVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				IllMedicalRecoderVo vo = new IllMedicalRecoderVo();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setNum(rs.getString("num"));
				vo.setAverFeeCost(rs.getString("averFeeCost"));
				vo.setAverInhost(rs.getString("averInhost"));
				vo.setCompliInterface(rs.getString("compliInterface"));
				vo.setCured(rs.getString("cured"));
				vo.setBetter(rs.getString("better"));
				vo.setNoCured(rs.getString("noCured"));
				vo.setDeath(rs.getString("death"));
				vo.setOther(rs.getString("other"));
				vo.setCureRate(rs.getString("cureRate"));
				vo.setDeathRate(rs.getString("deathRate"));
				vo.setAllIll(rs.getString("allIll"));
				vo.setThanFloor(rs.getString("thanFloor"));
				return vo;
			}
		});
		if(IllMedicalRecoderVoList!=null&&IllMedicalRecoderVoList.size()>0){
			for(IllMedicalRecoderVo vo:IllMedicalRecoderVoList){
				if(null!=vo.getDeptCode()){
					Document doucment1=new Document();
					doucment1.append("deptCode",vo.getDeptCode());
					Document document = new Document();
					document.append("deptCode",vo.getDeptCode());
					document.append("deptName", vo.getDeptName());
					document.append("num", vo.getNum());
					document.append("averFeeCost", vo.getAverFeeCost());
					document.append("averInhost", vo.getAverInhost());
					document.append("compliInterface", vo.getCompliInterface());
					document.append("cured", vo.getCured());
					document.append("better", vo.getBetter());
					document.append("noCured",vo.getNoCured());
					document.append("death", vo.getDeath());
					document.append("other", vo.getOther());
					document.append("cureRate",vo.getCureRate());
					document.append("deathRate", vo.getDeathRate());
					document.append("allIll", vo.getAllIll());
					document.append("thanFloor", vo.getThanFloor());
					new MongoBasicDao().update("WZBLRSBLTJFX", doucment1, document, true);
				}
				
			}
		}
	}

	@Override
	public List<IllMedicalRecoderVo> queryIllMedicalRecoderForDB(String deptCode,String menuAlias) {
		
		BasicDBObject bdObject = new BasicDBObject();
		List<IllMedicalRecoderVo> list=new ArrayList<IllMedicalRecoderVo>();
		BasicDBList deptList=new BasicDBList();
		if(StringUtils.isNotBlank(deptCode)){
			
			String[] deptArr=deptCode.split(",");
			for(int i=0,len=deptArr.length;i<len;i++){
				deptList.add(new BasicDBObject("deptCode", deptArr[i]));
			}
			bdObject.append("$or", deptList);
		}else{
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			List<SysDepartment> dept = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
			for(int i = 0,len=dept.size();i<len;i++){
				deptList.add(new BasicDBObject("deptCode",dept.get(i).getDeptName()));
			}
			bdObject.put("$or", deptList);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("WZBLRSBLTJFX", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			IllMedicalRecoderVo vo=new  IllMedicalRecoderVo();
			 dbCursor = cursor.next();
			 vo.setDeptCode((String)dbCursor.get("deptCode"));
			 vo.setDeptName((String)dbCursor.get("deptName"));
			 vo.setNum((String)dbCursor.get("num"));
			 vo.setAverFeeCost((String)dbCursor.get("averFeeCost"));
			 vo.setAverInhost((String)dbCursor.get("averInhost"));
			 vo.setCompliInterface((String)dbCursor.get("compliInterface"));
			 vo.setCured((String)dbCursor.get("cured"));
			 vo.setBetter((String)dbCursor.get("better"));
			 vo.setNoCured((String)dbCursor.get("noCured"));
			 vo.setDeath((String)dbCursor.get("death"));
			 vo.setOther((String)dbCursor.get("other"));
			 vo.setCureRate((String)dbCursor.get("cureRate"));
			 vo.setDeathRate((String)dbCursor.get("deathRate"));
			 vo.setAllIll((String)dbCursor.get("allIll"));
			 vo.setThanFloor((String)dbCursor.get("thanFloor"));
			list.add(vo);
			}
		return list;
	}
	
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<IllMedicalRecoderVo> queryIllMedical(List<String> tnL,String startTime,String endTime,String deptCode,String menuAlias) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<IllMedicalRecoderVo>();
		}
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT deptCode,(select t.dept_name from t_department t where rownum<2 and  t.dept_code=deptName) deptName,num,cured,better,noCured,death,other,cureRate,deathRate,averInhost,averFeeCost,compliInterface,allIll,thanFloor  FROM( ");
		
		sql.append("select dept_code deptCode, dept_code deptName,num,cured, better, noCured, death,other, cureRate, deathRate, averInhost, averFeeCost, compliInterface, allIll, thanFloor  "
				+ "from (select dept_code from t_inpatient_dayreport cg  where cg.date_stat = trunc(TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss'))) BB  "
				+ "left join (select cb.dept_code deptCode,cb.dept_name deptName, num, cured, better, noCured, death, (num - cured - better - noCured - death) other, "
				+ "round(((num - better - noCured - death) * 100) / num,  1) cureRate,  round(((death) * 100) / num, 1) deathRate, round(allDay / num, 1) averInhost, "
				+ "round(allFree / num, 1) averFeeCost,  compliInterface, allIll, to_char((num * 100) / allIll, '0.00') thanFloor  "
				+ "from (select BB.*,  (select sum(ccg.out_normal)  from t_inpatient_dayreport ccg  "
				+ "where ccg.date_stat >=  TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss')  "
				+ "and ccg.date_stat <=  TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss')) allIll, "
				+ "row_number() over(partition by BB.dept_code order by num desc) as row_index from (select cg.dept_code, cg.dept_name,  count(1) num,  "
				+ "sum(cg.pi_days) allDay, sum(cg.feesCost) allFree, sum(DECODE(cg.zg, '1', 1, 0)) cured, sum(DECODE(cg.zg, '2', 1, 0)) better, "
				+ "sum(DECODE(cg.zg, '3', 1, 0)) noCured, sum(DECODE(cg.zg, '4', 1, 0)) death, sum(decode(cg.OPS_COMPLICATIONS, '2',  1,  0)) compliInterface  "
				+ "from (select BB.*, mcg.OPS_COMPLICATIONS, mcg.OPS_COMPLICATIONS_DESC, mcg.MED_REACTIONS, mcg.MED_REACTIONS_DESC, mcg.HOS_INFECTION, mcg.HOS_INFECTION_DESC, "
				+ "mcg.DRUG_REACTIONS,mcg.DRUG_REACTIONS_DESC,mcg.TUMORSTAGE_T, mcg.TUMORSTAGE_N, mcg.TUMORSTAGE_M, mcg.CLINICAL_PATHWAYS,  "
				+ "mcg.VENTILATORUSE, mcg.VENTILATORUSETIME, mcg.OPS_ELECTIVE, mcg.ZZ_HXJXGFY, mcg.ZZ_XLGR,  mcg.ZZ_NNXTGR  "
				+ "from (select c.*, feesCost from t_emr_base c left join ( ");
				for(int i=0;i<tnL.size();i++){
					if(i!=0){
						sql.append(" UNION ALL ");
					}
				sql.append("select rm"+i+".inpatient_no, (rm").append(i).append(".tot_cost + rm").append(i).append(".balance_prepay) feesCost from " ) ;
				sql.append(tnL.get(i)+" rm").append(i);
				}
				sql.append( ") rm on  rm.inpatient_no=c.inpatient_no   )  BB  "
				+ "left join t_emr_base_ext mcg on mcg.inpatient_no = BB.inpatient_no) cg  "
				+ "where trunc(cg.out_date) >= TO_DATE('"+startTime+"','yyyy-mm-dd hh24:mi:ss') and trunc(cg.out_date) <= TO_DATE('"+endTime+"','yyyy-mm-dd hh24:mi:ss')  "
				+ "and cg.case_stus in ('3', '4') and (cg.medical_type in ('D'))  and (fun_splitstring(cg.henanadd2012,  '|',  29) != 'DOC' or fun_splitstring(cg.henanadd2012, '|', 29) is null)  "
				+ "group by cg.dept_code, cg.dept_name order by cg.dept_code, count(1) desc) BB) CB) AA on BB.dept_code =  AA.deptCode "); 
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptCode)){
			deptCode=deptCode.replace(",", "','");
			sql.append("where AA.deptCode in('"+deptCode+"') )");
		}else{
			sql.append("where and AA.deptCode in("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())+") )");
		}
		List<IllMedicalRecoderVo> IllMedicalRecoderVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<IllMedicalRecoderVo>() {
			@Override
			public IllMedicalRecoderVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				IllMedicalRecoderVo vo = new IllMedicalRecoderVo();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setNum(rs.getString("num"));
				vo.setCured(rs.getString("cured"));
				vo.setBetter(rs.getString("better"));
				vo.setNoCured(rs.getString("noCured"));
				vo.setDeath(rs.getString("death"));
				vo.setOther(rs.getString("other"));
				vo.setDeathRate(rs.getString("deathRate"));
				vo.setAverInhost(rs.getString("averInhost"));
				vo.setAverFeeCost(rs.getString("averFeeCost"));
				vo.setCompliInterface(rs.getString("compliInterface"));
				vo.setCureRate(rs.getString("cureRate"));
				vo.setAllIll(rs.getString("allIll"));
				vo.setThanFloor(rs.getString("thanFloor"));
				return vo;
			}
		});
		if(IllMedicalRecoderVoList!=null&&IllMedicalRecoderVoList.size()>0){
			return IllMedicalRecoderVoList;
		}
		return new ArrayList<IllMedicalRecoderVo>();
	
}


}
