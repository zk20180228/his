package cn.honry.statistics.sys.reportForms.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.query.StringBasedMongoQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.zxing.common.detector.MathUtils;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.FinanceInvoicedetail;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsDao;
import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.statistics.sys.reportForms.vo.IncomeVo;
import cn.honry.statistics.sys.reportForms.vo.PatientInfoVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

@Repository("reportFormsDAO")
@SuppressWarnings({"all"})
public class ReportFormsDaoImpl extends HibernateEntityDao<DoctorWorkloadStatistics> implements ReportFormsDao{
	
		// 为父类HibernateDaoSupport注入sessionFactory的值
		@Resource(name = "sessionFactory")
		public void setSuperSessionFactory(SessionFactory sessionFactory) {

			super.setSessionFactory(sessionFactory);
		}
		@Resource
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
		
		@Autowired
		@Qualifier(value = "dataJurisInInterDAO")
		private DataJurisInInterDAO dataJurisInInterDAO;
		
		/**  
		 *  
		 * @Description：  医生工作量统计查询
		 * @Author：wujiao
		 * @CreateDate：2016-5-3 10:12:16  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param  deptw科室
		 * @param  stimew开始时间
		 * @param  etimew结束时间
		 *
		 */
	@Override
	public List<DoctorWorkloadStatistics> queryReservation(String deptw, String stimew, String etimew) {
		StringBuffer sb  = new StringBuffer();
		sb.append(" select d.dept_id as deptId,d.dept_name as dept ,e.employee_id as nameId ,e.employee_name as name ");
		sb.append(" from  t_employee  e  left join t_department d on d.dept_id=e.dept_id ");
		sb.append(" where  e.employee_type=1 and d.DEPT_ISFORREGISTER=1 and d.stop_flg=0 and d.del_flg=0 and e.stop_flg=0 and e.del_flg=0 ");
	
		if(StringUtils.isNotBlank(deptw)){
			sb.append(" AND d.dept_id = '"+deptw+"' ");
		}
		sb.append(" order by d.dept_id ");
		
		Query query = this.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("nameId")
				.addScalar("dept").addScalar("deptId");
		
		List<DoctorWorkloadStatistics> list = query.setResultTransformer(Transformers.aliasToBean(DoctorWorkloadStatistics.class)).list();
		return list;
	}

	/**  
	 *  
	 * @Description：  医生工作量统计查询   看诊信息  挂号数
	 * @Author：wujiao
	 * @CreateDate：2016-5-3  10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
		@Override
		public Integer queryInfo(List<String> tnL,String nameId, final String deptId, final String stimew, final String etimew) {
			if(tnL==null||tnL.size()<0){
				return 0;
			}
			final StringBuffer sb = new StringBuffer();
			sb.append(" select count(1) from (");
			for (int j = 0; j <tnL.size(); j++) {
				if(j!=0){
					sb.append(" UNION ");
				}
				sb.append(" select id from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(j));
				sb.append(" where stop_flg=0 and del_flg=0 and dept_code = :deptId and trunc(REG_DATE,'dd') between to_date(:stimew,'yyyy-MM-dd') And to_date(:etimew,'yyyy-MM-dd') ");
			}
			sb.append(" )");
			BigDecimal total = (BigDecimal)this.getHibernateTemplate().execute(new HibernateCallback() {

				@Override
				public BigDecimal doInHibernate(Session session)
						throws HibernateException, SQLException {
					return (BigDecimal)session.createSQLQuery(sb.toString()).setParameter("deptId", deptId).setParameter("stimew", stimew).setParameter("etimew", etimew).uniqueResult();
				}
			});
			
			return total.intValue();
		}
		
		/**  
		 *  
		 * @Description：  医生工作量统计查询   预约号信息
		 * @Author：wujiao
		 * @CreateDate：2016-5-3 10:12:16  
		 * @ModifyRmk：  
		 * @version 1.0
		 * @param  deptId科室id
		 * @param  stimew开始时间
		 * @param  etimew结束时间
		 * @param  nameId员工id
		 */
	@Override
	public List<RegisterPreregister> querRegister(String nameId, String deptId, String stimew, String etimew) {
		String hql="from RegisterPreregister i where i.del_flg=0 and i.stop_flg=0 and i.preregisterDept='"+deptId+"' and i.preregisterExpert='"+nameId+"'";
		if(StringUtils.isNotBlank(stimew) && StringUtils.isNotBlank(etimew)){
			hql=hql+" and preregisterDate Between  to_date('"+stimew+"','yyyy-MM-dd') And to_date('"+etimew+"','yyyy-MM-dd')";
		}
		List<RegisterPreregister> infoList=this.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList;
		}	
		
		 return new ArrayList<RegisterPreregister>();
	}

	/**  
	 *  
	 * @Description：  医生工作量统计查询   看诊信息就诊数
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	@Override
	public Integer queryInfonum(List<String> tnL,String nameId, final String deptId, final String stimew, final String etimew) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from (");
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select id from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i));
			sb.append(" where stop_flg=0 and del_flg=0 and dept_code = :deptId and  trunc(SEE_DATE,'dd') between to_date(:stimew,'yyyy-MM-dd') And to_date(:etimew,'yyyy-MM-dd') ");
		}
		sb.append(" )");
		BigDecimal total = (BigDecimal)this.getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createSQLQuery(sb.toString()).setParameter("deptId", deptId).setParameter("stimew", stimew).setParameter("etimew", etimew).uniqueResult();
			}
		});
		
		 return total.intValue();
	}

	/**  
	 *  
	 * @Description：  医生工作量统计查询   电话预约
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	@Override
	public Integer queryPhonenum(String nameId, String deptId, String stimew, String etimew) {
		String hql="from RegisterPreregister i where i.del_flg=0 and i.stop_flg=0 and i.preregisterDept='"+deptId+"' and i.preregisterExpert='"+nameId+"'"
				+ " and i.preregisterIsphone=1";
		if(StringUtils.isNotBlank(stimew) && StringUtils.isNotBlank(etimew)){
			hql=hql+" and preregisterDate Between  to_date('"+stimew+"','yyyy-MM-dd') And to_date('"+etimew+"','yyyy-MM-dd')";
		}
		List<RegisterPreregister> infoList=this.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList.size();
		}	
		
		return 0;
	}
	
	/**  
	 *  
	 * @Description：  医生工作量统计查询   网络预约
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	@Override
	public Integer querynetnum(String nameId, String deptId, String stimew, String etimew) {
		String hql="from RegisterPreregister i where i.del_flg=0 and i.stop_flg=0 and i.preregisterDept='"+deptId+"' and i.preregisterExpert='"+nameId+"'"
				+ " and i.preregisterIsnet=1";
		if(StringUtils.isNotBlank(stimew) && StringUtils.isNotBlank(etimew)){
			hql=hql+" and preregisterDate Between  to_date('"+stimew+"','yyyy-MM-dd') And to_date('"+etimew+"','yyyy-MM-dd')";
		}
		List<RegisterPreregister> infoList=this.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList.size();
		}	
		
		 return 0;
	}

	/**  
	 *  
	 * @Description：  医生工作量统计查询   网络预约
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  stimew开始时间
	 * @param  etimew结束时间
	 * @param  nameId员工id
	 */
	@Override
	public Integer querynownum(String nameId, String deptId, String stimew, String etimew) {
		String hql="from RegisterPreregister i where i.del_flg=0 and i.stop_flg=0 and i.preregisterDept='"+deptId+"' and i.preregisterExpert='"+nameId+"'"
				+ " and i.preregisterIsnet=0 and i.preregisterIsphone=0";
		if(StringUtils.isNotBlank(stimew) && StringUtils.isNotBlank(etimew)){
			hql=hql+" and preregisterDate Between  to_date('"+stimew+"','yyyy-MM-dd') And to_date('"+etimew+"','yyyy-MM-dd')";
		}
		List<RegisterPreregister> infoList=this.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList.size();
		}	
		
		 return 0;
	}
	
	public List<DoctorWorkloadStatistics> queryReservation(List<String> tnL,List<String> pretnl,String deptw, String stimew, String etimew,String menuType){
		if(tnL==null||tnL.size()<=0||pretnl==null||pretnl.size()<=0){
			return new ArrayList<DoctorWorkloadStatistics>();
		}
		StringBuffer sb = new StringBuffer();
		
		/**开始2017-04-17***/
		sb.append(" SELECT max(doct) doct,max(dept) dept,SUM(yyh) yyh,SUM(ghs) ghs,SUM(jzs) jzs,SUM(dhyy) dhyy,SUM(wlyy) wlyy,SUM(ghs) zhghs,SUM(jzs) zhjzs FROM( ");
		
		//循环挂号表
		List<String> deptcode = Arrays.asList(deptw.split(","));
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){	
				sb.append(" UNION ALL ");
				sb.append(" SELECT 	M.DOCT_CODE doctcode,M.DEPT_CODE deptcode, M .DOCT_NAME doct,M .DEPT_NAME dept,NULL yyh,COUNT (1) ghs,SUM(DECODE (M.YNSEE, 1, 1,0)) jzs,NULL dhyy,NULL wlyy FROM ");
				sb.append(tnL.get(i)).append(" m ");
				sb.append(" WHERE m.IN_STATE=0 AND m.REG_DATE>=TO_DATE(:stimew, 'yyyy-MM-dd') AND m.REG_DATE<TO_DATE(:etimew, 'yyyy-MM-dd') ");
				if(StringUtils.isNotBlank(deptw)&&!"all".equals(deptw)){
					sb.append(" AND m.DEPT_CODE in (:deptw) ");
				}else{
					sb.append(" AND m.DEPT_CODE in (").append(dataJurisInInterDAO.getJurisDeptSql(menuType, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				sb.append(" GROUP BY m.DEPT_NAME,m.DOCT_NAME,m.DOCT_CODE,m.DEPT_CODE ");
			}else{
				sb.append(" ( SELECT 	M.DOCT_CODE doctcode,M.DEPT_CODE deptcode, M .DOCT_NAME doct,M .DEPT_NAME dept,NULL yyh,COUNT (1) ghs,SUM(DECODE (M.YNSEE, 1, 1,0)) jzs,NULL dhyy,NULL wlyy FROM ");
				sb.append(tnL.get(i)).append(" m ");
				sb.append(" WHERE m.IN_STATE=0 AND m.REG_DATE>=TO_DATE(:stimew, 'yyyy-MM-dd') AND m.REG_DATE<TO_DATE(:etimew, 'yyyy-MM-dd') ");
				if(StringUtils.isNotBlank(deptw)&&!"all".equals(deptw)){
					sb.append(" AND m.DEPT_CODE in (:deptw) ");
				}else{
					sb.append(" AND m.DEPT_CODE in (").append(dataJurisInInterDAO.getJurisDeptSql(menuType, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
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
				if(StringUtils.isNotBlank(deptw)&&!"all".equals(deptw)){
					sb.append(" AND p.PREREGISTER_DEPT in (:deptw) ");
				}else{
					sb.append(" AND p.PREREGISTER_DEPT in (").append(dataJurisInInterDAO.getJurisDeptSql(menuType, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				sb.append(" GROUP BY p.PREREGISTER_DEPTNAME ,p.PREREGISTER_EXPERTNAME,p.PREREGISTER_EXPERT,p.PREREGISTER_DEPT ");
			}else{
				sb.append(" ) UNION ALL ");
				sb.append(" ( SELECT p.PREREGISTER_EXPERT DOCTCODE,p.PREREGISTER_DEPT deptcode, P .PREREGISTER_EXPERTNAME doct,P .PREREGISTER_DEPTNAME dept,COUNT (1) yyh,NULL ghs,NULL jzs,SUM(DECODE (P .PREREGISTER_ISPHONE, 1, 1,0)) dhyy,sum(DECODE (P .PREREGISTER_ISNET, 1, 1,0)) wlyy FROM ");
				sb.append(pretnl.get(j)).append(" p ");
				sb.append(" WHERE P .DEL_FLG = 0 AND P .STOP_FLG = 0 AND P .PREREGISTER_DATE >= TO_DATE (:stimew, 'yyyy-MM-dd') AND P .PREREGISTER_DATE < TO_DATE (:etimew, 'yyyy-MM-dd') ");
				if(StringUtils.isNotBlank(deptw)&&!"all".equals(deptw)){
					sb.append(" AND p.PREREGISTER_DEPT  in (:deptw) ");
				}else{
					sb.append(" AND p.PREREGISTER_DEPT in (").append(dataJurisInInterDAO.getJurisDeptSql(menuType, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
				}
				sb.append(" GROUP BY p.PREREGISTER_DEPTNAME ,p.PREREGISTER_EXPERTNAME,p.PREREGISTER_EXPERT,p.PREREGISTER_DEPT ");
			}
		
		}
		
		//按照医生的编号和科室分组
		sb.append(" ) ) where dept is not null GROUP BY DOCTCODE,deptcode ");
		
		/**结束2017-04-17***/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deptw",  deptcode);
		paramMap.put("stimew", stimew);
		paramMap.put("etimew", etimew);
		
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
				return ds;
			}
		});
		
		if(list!=null&&list.size()>0){
			int bookSum=0;
			for(DoctorWorkloadStatistics s:list){
				bookSum=s.getBookNo()+s.getTelBook()+s.getNetBook();
				s.setBookTot(bookSum);
				s.setValidTot(0);
				s.setBookTot(s.getBookNo());
			}
			return list;
		}
		
		return new ArrayList<DoctorWorkloadStatistics>();
	}
	
	/**  
	 *  
	 * @Description：  门诊住院情况统计统计查询
	 * @Author：wujiao
	 * @CreateDate：2016-5-3 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	@Override
	public List<PatientInfoVo> queryPatientInfo(List<String> infotnl,List<String> feedetialtnl,List<String> notmednl,List<String> mednl, String dept, String stime, String etime,String menuType) {
		List<String> deptcode = Arrays.asList(dept.split(","));
		if(infotnl==null||infotnl.size()<0||feedetialtnl==null||feedetialtnl.size()<0){
			return new ArrayList<PatientInfoVo>();
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < infotnl.size(); i++) {
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append(" SELECT i.MEDICALRECORD_ID medicalrecordId,i.PATIENT_NAME name,i.REPORT_SEX sex,i.REPORT_BIRTHDAY age,");
			sb.append(" i.DIST address,i.DEPT_NAME dept,i.CHARGE_DOC_NAME doctor,nvl(fn.mzylfy,0) medicalExpense,nvl(fn.mzypfy,0) drugCost,nvl(fn.mzfyhj,0) total,");
			sb.append(" nvl(sum(y.ttotal),0) intotal,nvl(sum(m.ttotal),0) metotal,(nvl(sum(y.ttotal),0)+nvl(sum(m.ttotal),0)) totalcost,");
			sb.append(" TRUNC(nvl(i.OUT_DATE,sysdate)-i.IN_DATE) dayNum");
			sb.append(" from ").append(infotnl.get(i)).append(" i LEFT JOIN (");
			sb.append(" SELECT blh,SUM(mzylfy) mzylfy,SUM(mzypfy) mzypfy,SUM(mzfyhj) mzfyhj FROM ( ");
			for (int j = 0; j < feedetialtnl.size(); j++) {
				if(j!=0){
					sb.append(" UNION ALL ");
				}
				sb.append(" SELECT f.PATIENT_NO blh,DECODE(f.DRUG_FLAG,'0', f.TOT_COST) mzylfy,DECODE(f.DRUG_FLAG,'1', f.TOT_COST) mzypfy,TOT_COST mzfyhj  ");
				sb.append(" from ").append(feedetialtnl.get(j)).append(" f WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.PAY_FLAG = 1 AND f.TRANS_TYPE = 1");
			}
			sb.append(" GROUP BY f.PATIENT_NO,f.DRUG_FLAG,f.TOT_COST ");
			sb.append(" ) GROUP BY blh ) fn ON i.MEDICALRECORD_ID = fn.blh ");
			sb.append(" left join ( ");
			for(int j = 0; j<mednl.size();j++){
				if(j!=0){
					sb.append(" UNION ALL ");
				}
				sb.append("select sum(t.tot_cost) ttotal,t.inpatient_no inpatient_no from ").append(mednl.get(j));
				sb.append(" t group by t.inpatient_no");
			}
			sb.append(") m on i.inpatient_no = m.INPATIENT_NO ");
			sb.append(" left join ( ");
			for(int j = 0; j<mednl.size();j++){
				if(j!=0){
					sb.append(" UNION ALL ");
				}
				sb.append("select sum(t.tot_cost) ttotal,t.inpatient_no inpatient_no from ").append(notmednl.get(j));
				sb.append(" t group by t.inpatient_no");
			}
			sb.append(") y on i.inpatient_no = y.INPATIENT_NO ");
			sb.append(" WHERE i.IN_SOURCE = '1' AND i.STOP_FLG = 0 AND i.DEL_FLG = 0 ");
			if(StringUtils.isNotBlank(stime)){
				sb.append("  AND i.IN_DATE>=TO_DATE(:stime, 'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append(" AND i.IN_DATE<TO_DATE(:etime, 'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(dept)&&!"all".equals(dept)){
				sb.append(" and i.DEPT_CODE in (:dept) ");
			}else{
				sb.append(" and i.DEPT_CODE in (").append(dataJurisInInterDAO.getJurisDeptSql(menuType, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
			}
			sb.append(" group by i.MEDICALRECORD_ID,i.PATIENT_NAME, i.REPORT_SEX,i.DIST,i.DEPT_NAME,i.CHARGE_DOC_NAME ,fn.mzylfy,fn.mzypfy,fn.mzfyhj,i.REPORT_BIRTHDAY,i.OUT_DATE,i.in_date");
		}
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stime", stime);
		paramMap.put("etime", etime);
		paramMap.put("dept", deptcode);
		List<PatientInfoVo> list =namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<PatientInfoVo>() {
			@Override
			public PatientInfoVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				PatientInfoVo vo = new PatientInfoVo();
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getInt("sex"));
				vo.setAge(DateUtils.formatDateY_M_D(rs.getDate("age")));
				vo.setAddress(rs.getString("address"));
				vo.setDept(rs.getString("dept"));
				vo.setDoctor(rs.getString("doctor"));
				vo.setMedicalExpense(rs.getDouble("medicalExpense"));
				vo.setDrugCost(rs.getDouble("drugCost"));
				vo.setTotal(rs.getDouble("total"));
				vo.setDays(rs.getInt("dayNum"));
				vo.setMedicalExpensez(rs.getDouble("intotal"));
				vo.setDrugCostz(rs.getDouble("metotal"));
				vo.setTotalz(rs.getDouble("totalcost"));
				vo.setTotalall(vo.getTotal()+vo.getTotalz());

				return vo;
			}
		});
		if(list==null||list.size()<0){
			return new ArrayList<PatientInfoVo>();
		}
		Double totalInspectCost = 0.0;
		Double totalTreatmentCost = 0.0;
		Double totalmz = 0.0;
		Integer totaldays = 0;
		Double totalRadiationCost = 0.0;
		Double totalBloodCost = 0.0;
		Double totalTestCost = 0.0;
		Double totaltotalz = 0.0;
		Double totaltotalall = 0.0;
		for(PatientInfoVo svo : list){
			totalInspectCost+=svo.getMedicalExpense();
			totalTreatmentCost+=svo.getDrugCost();
			totalmz+=svo.getTotal();
			totaldays+=svo.getDays();
			totalRadiationCost+=svo.getMedicalExpensez();
			totalBloodCost+=svo.getDrugCostz();
			totalTestCost+=svo.getOtherExpensesz();
			totaltotalz+=svo.getTotalz();
			totaltotalall+=svo.getTotalall();
		}
		PatientInfoVo pvo = new PatientInfoVo();
		
		pvo.setDoctor("合计");
		pvo.setMedicalExpense(totalInspectCost);
		pvo.setDrugCost(totalTreatmentCost);
		pvo.setTotal(totalmz);
		pvo.setDays(totaldays);
		pvo.setMedicalExpensez(totalRadiationCost);
		pvo.setDrugCostz(totalBloodCost);
		pvo.setOtherExpensesz(totalTestCost);
		pvo.setTotalz(totaltotalz);
		pvo.setTotalall(totaltotalall);
		list.add(pvo);
	
		return list;
	}

	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 下午3:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  dept科室
	 * @param  stime开始时间
	 * @param  etime结束时间
	 */
	@Override
	public List<StatisticsVo> queryStatisticsInfo(String dept, String stime, String etime) {
		
		StringBuffer sb  = new StringBuffer();
		sb.append(" select d.dept_code as deptId,d.dept_name as dept ,e.employee_code as nameId ,e.employee_name as name, ");
		sb.append(" u.user_code as userid ,u.user_name as userName from  t_employee  e ");
		sb.append(" left join t_department d on d.dept_code=e.dept_id ");
		sb.append(" left join t_sys_user u on u.user_code=e.user_id ");
		sb.append(" where e.employee_type=1 and  d.DEPT_ISFORREGISTER=1 and d.stop_flg=0 and d.del_flg=0 ");
		sb.append(" and e.stop_flg=0 and e.del_flg=0 ");
		
		
			if(StringUtils.isNotBlank(dept)){
				sb.append(" AND d.dept_code = '"+dept+"' ");
			}
			sb.append(" order by d.dept_code");
			Query query = this.getSession().createSQLQuery(sb.toString())
					.addScalar("deptId").addScalar("dept")
					.addScalar("nameId").addScalar("name")
					.addScalar("userid").addScalar("userName");
			List<StatisticsVo> list = query.setResultTransformer(Transformers.aliasToBean(StatisticsVo.class)).list();
		
			return list;
	}

	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-12 下午3:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  userid用户id
	 */
	@Override
	public List<OutpatientFeedetail> queryFeedetailInfo(List<String> feedetialPartitionName,final String deptId,final String userid,final String stime,final String etime) {
		
		final StringBuffer sb = new StringBuffer();
		if(feedetialPartitionName==null||feedetialPartitionName.size()<0){
			return new ArrayList<OutpatientFeedetail>();
		}
		for (int i = 0; i < feedetialPartitionName.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select  distinct(t.INVOICE_NO) as invoiceNo  from ").append(feedetialPartitionName.get(i)).append(" t where t.doct_dept= :deptId ");
			sb.append(" and t.doct_code=:userid and t.stop_flg=0 and t.del_flg=0 ");
			if(StringUtils.isNotBlank(etime)&&StringUtils.isNotBlank(stime)){
				sb.append(" and trunc(t.REG_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ");
			}
		}
		List<OutpatientFeedetail> list = (List<OutpatientFeedetail>)this.getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public List<OutpatientFeedetail> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString()).addScalar("invoiceNo");
				query.setParameter("deptId", deptId).setParameter("userid", userid).setParameter("stime", stime).setParameter("etime", etime);
				List<OutpatientFeedetail> list = query.setResultTransformer(Transformers.aliasToBean(OutpatientFeedetail.class)).list();
				return list;
			}
		});
		if(list!=null&&list.size()>0){
			Set<OutpatientFeedetail> st = new HashSet<OutpatientFeedetail>();
			st.addAll(list);
			List<OutpatientFeedetail> arrayList = new ArrayList<OutpatientFeedetail>(st);
			return arrayList;
		}
		
		return new ArrayList<OutpatientFeedetail>();
	}

	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-13 10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  invoiceNo发票号
	 */
	@Override
	public List<FinanceInvoicedetailNow> queryinvo(String invoiceDetialTabName,String invoiceNo) {
		String sql=" select t.invo_code as invoCode,t.invo_name as invoName,t.pay_cost as payCost from "+invoiceDetialTabName+" t "
				+ "  where t.trans_type=1 and t.invoice_no='"+invoiceNo+"' and t.stop_flg=0 and t.del_flg=0";
		Query query = this.getSession().createSQLQuery(sql)
		.addScalar("invoCode").addScalar("invoName").addScalar("payCost",Hibernate.DOUBLE);
		List<FinanceInvoicedetailNow> inlist = query.setResultTransformer(Transformers.aliasToBean(FinanceInvoicedetailNow.class)).list();
		
		return inlist;
	}
	
	/**
	 * @Description 根据分区和发票号进行
	 * @author  marongbin
	 * @createDate： 2016年12月3日 下午5:21:39 
	 * @modifier 
	 * @modifyDate：
	 * @param invoicePartitionName
	 * @param invoiceNo
	 * @return: List<BusinessInvoicedetail>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public StatisticsVo queryinvoNew(List<String> feeDetialPartitionName,final String deptCode,final String doctCode,final String stime,final String etime){
		
		//费用类别及code  01:西药费 ；02 中成药费；03 中草药费 ；05 治疗费； 07 检查费； 08   放射费；09 化验费；11 输血费； 17 其他费用
		if(feeDetialPartitionName==null&&feeDetialPartitionName.size()<0){
			return new StatisticsVo();
		}
		final StringBuffer sb = new StringBuffer();
		if(feeDetialPartitionName.size()>1){
			sb.append(" select sum(westernNum) as westernNum,sum(westernCost) as westernCost,sum(chineseNum) as chineseNum ,");
			sb.append("  sum(chineseCost) as chineseCost,sum(herbalNum) as herbalNum,sum(herbalCost) as herbalCost ,");
			sb.append("  sum(treatmentNum) as treatmentNum,sum(treatmentCost) as treatmentCost,sum(inspectNum) as inspectNum ,");
			sb.append("  sum(radiationNum) as radiationNum,sum(radiationCost) as radiationCost,sum(testNum) as testNum ,");
			sb.append("  sum(testCost) as testCost,sum(bloodNum) as bloodNum,sum(bloodCost) as bloodCost,sum(otherNum) as otherNum ,sum(otherCost) as otherCost from( ");
		}
		for (int i = 0; i < feeDetialPartitionName.size(); i++) {
			if(i!=0){
				sb.append(" UNION ");
			}
			sb.append(" select ");
			
			//西药费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '01' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode  and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as westernNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '01' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as westernCost,");
			
			//02 中成药费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '02' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as chineseNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '02' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as chineseCost,");
			
			//03 中草药费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '03' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as herbalNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '03' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as herbalCost,");
			
			//05 治疗费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '05' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as treatmentNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '05' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as treatmentCost,");
			
			//07 检查费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '07' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as inspectNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '07' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) ,0.0)as inspectCost,");
			
			//08   放射费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '08' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as radiationNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '08' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as radiationCost,");
			
			//09 化验费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '09' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as testNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '09' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as testCost,");
			
			//11 输血费
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '11' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as bloodNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '11' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as bloodCost,");
			
			//17 其他费用
			sb.append(" (select count(1) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '17' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ) as otherNum,");
			sb.append(" NVL((select sum(PAY_COST) from ").append(feeDetialPartitionName.get(i)).append(" where stop_flg=0 and del_flg=0 and INVO_CODE = '17' and REG_DPCD = :deptCode and DOCT_CODE = :doctCode and INVOICE_NO Is Not Null and trunc(OPER_DATE,'dd') between to_date(:stime,'yyyy-MM-dd') and to_date(:etime,'yyyy-MM-dd') ),0.0) as otherCost ");
			sb.append(" from dual ");
		}
		if(feeDetialPartitionName.size()>1){
			sb.append(" )");
		}
		StatisticsVo vo = (StatisticsVo)getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatisticsVo doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.addScalar("inspectNum", Hibernate.INTEGER).addScalar("inspectCost", Hibernate.DOUBLE)
				.addScalar("treatmentNum", Hibernate.INTEGER).addScalar("treatmentCost", Hibernate.DOUBLE)
				.addScalar("radiationNum", Hibernate.INTEGER).addScalar("radiationCost", Hibernate.DOUBLE)
				.addScalar("testNum", Hibernate.INTEGER).addScalar("testCost", Hibernate.DOUBLE)
				.addScalar("bloodNum", Hibernate.INTEGER).addScalar("bloodCost", Hibernate.DOUBLE)
				.addScalar("otherNum", Hibernate.INTEGER).addScalar("otherCost", Hibernate.DOUBLE)
				.addScalar("westernNum", Hibernate.INTEGER).addScalar("westernCost", Hibernate.DOUBLE)
				.addScalar("chineseNum", Hibernate.INTEGER).addScalar("chineseCost", Hibernate.DOUBLE)
				.addScalar("herbalNum", Hibernate.INTEGER).addScalar("herbalCost", Hibernate.DOUBLE);
				query.setParameter("deptCode", deptCode).setParameter("doctCode", doctCode).setParameter("stime", stime).setParameter("etime", etime);
				StatisticsVo result = (StatisticsVo) query.setResultTransformer(Transformers.aliasToBean(StatisticsVo.class)).uniqueResult();
				
				return result;
			}
		});
		
		return vo;
	}
	
	/**
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2016年12月5日 下午2:15:44 
	 * @modifier 
	 * @modifyDate：
	 * @param invoicePartitionName
	 * @param invoiceNo
	 * @param doctCode
	 * @return: StatisticsVO
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<FinanceInvoicedetailNow> queryinvoNow(List<String> ipn,String invoiceNo){
		
		if(ipn.size()<0){
			return new ArrayList<FinanceInvoicedetailNow>();
		}
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ipn.size(); i++) {
			if(ipn.size()>1){
				sb.append(" UNION ");
			}
			sb.append(" select t.invo_code as invoCode,t.invo_name as invoName,t.pay_cost as payCost from ").append(ipn.get(i));
			sb.append(" where t.trans_type=1 and t.invoice_no=:invoiceNo and t.stop_flg=0 and t.del_flg=0 ");
		}
		SQLQuery query = this.getSession().createSQLQuery(sb.toString());
			query.setParameter("invoiceNo", invoiceNo);
			query.addScalar("invoCode").addScalar("invoName").addScalar("payCost",Hibernate.DOUBLE);
		List<FinanceInvoicedetailNow> inlist = query.setResultTransformer(Transformers.aliasToBean(FinanceInvoicedetailNow.class)).list();
		
		return inlist;
	}

	/**  
	 *  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-16 11:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  emp用户
	 * @param  stime开始时间
	 * @param  etime结束时间
	 *
	 */
	@Override
	public List<IncomeVo> queryIncomeInfo(String tableName,String emp, String stime) {
		
		String sql=" select distinct t.schedule_date as dates , e.EMPLOYEE_JOBNO as userId,t.schedule_deptid as deptId from  "+tableName+" t "
				+ "  left join T_EMPLOYEE  e on e.del_flg=0 and e.stop_flg=0 and e.Employee_Jobno=t.schedule_doctor"
				+ " where t.SCHEDULE_DOCTOR='"+emp+"' and  "
				+ " TO_CHAR(t.SCHEDULE_DATE, 'YYYY-MM-DD')   like'%"+stime+"%' and t.stop_flg=0 and t.del_flg=0 order by  t.schedule_date";
		
		Query query = this.getSession().createSQLQuery(sql).addScalar("dates",Hibernate.DATE).addScalar("userId").addScalar("deptId");
		List<IncomeVo> list = query.setResultTransformer(Transformers.aliasToBean(IncomeVo.class)).list();
		
		return list;
	}

	/**
	 * @Description 获取表中某个字段的最大值和最小值
	 * @author  marongbin
	 * @createDate： 2016年12月3日 上午10:51:30 
	 * @modifier 
	 * @modifyDate：
	 * @param tabName 表明
	 * @param fieldName 字段名
	 * @return: StatVo
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public StatVo findMaxMinByTabNameAndField(String tabName,String fieldName){
		final String sql = "SELECT MAX(mn."+fieldName+") AS eTime ,MIN(mn."+fieldName+") AS sTime FROM "+tabName+" mn";
		MapSqlParameterSource parame = new MapSqlParameterSource();
		StatVo sv = namedParameterJdbcTemplate.queryForObject(sql.toString(),parame, new BeanPropertyRowMapper<StatVo>(StatVo.class));
		if(sv.getsTime()==null){
			Date date = new Date();
			sv.setsTime(DateUtils.addDay(date,3650));
		}
		
		return sv;
	}
	
	/**  
	 *  
	 * @Description：  通过员工id得到用户id
	 * @Author：wujiao
	 * @CreateDate：2016-5-16 11:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  emp员工id
	 *
	 */
	@Override
	public String queryUserByemp(String emp) {
		String hql=" from SysEmployee e where  e.stop_flg=0 and e.del_flg=0 and e.id='"+emp+"'";
		List<SysEmployee> infoList=this.find(hql, null);
		if(infoList!=null && infoList.size()>0){
			return infoList.get(0).getUserId().getId();
		}
		
		 return null;
	}
	
	/**  
	 * @Description：  医院各项收入统计
	 * @Author：wujiao
	 * @CreateDate：2016-5-17 5:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param  deptId科室id
	 * @param  userId用户id
	 * @param  dates时间
	 *
	 */
	@Override
	public List<OutpatientFeedetail> queryFeedetailInfo(String invoiceDetialTabName,String deptId, String userId, Date dates) {
		String sql="select  distinct(t.INVOICE_NO) as invoiceNo  from "+invoiceDetialTabName+" t where t.doct_dept='"+deptId+"' "
				+ " and t.doct_code='"+userId+"' and t.stop_flg=0 and t.del_flg=0";
		if(dates!=null){
			sql=sql+"and TO_CHAR(t.fee_date, 'YYYY-MM-DD')='"+dates+"' ";
		}
		Query query = this.getSession().createSQLQuery(sql)
		.addScalar("invoiceNo");
		List<OutpatientFeedetail> list = query.setResultTransformer(Transformers.aliasToBean(OutpatientFeedetail.class)).list();
		return list;
	}

	@Override
	public List<OutpatientFeedetail> queryFeelist(String medicalrecordId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct f.drug_flag as drugFlag,f.tot_cost as totCost ");
		sb.append(" from t_register_main t ");
		sb.append(" left join t_outpatient_feedetail f on f.patient_no=t.medicalrecordid ");
		sb.append(" where f.trans_type=1  and f.pay_flag=1  and f.cancel_flag=1  and t.stop_flg=0  and t.del_flg=0 ");
		
		if(StringUtils.isNotBlank(medicalrecordId) ){
			sb.append(" and t.medicalrecordid ='"+medicalrecordId+"' ");
		}
		Query query = this.getSession().createSQLQuery(sb.toString())
		.addScalar("drugFlag")
		.addScalar("totCost",Hibernate.DOUBLE);
		List<OutpatientFeedetail> list = query.setResultTransformer(Transformers.aliasToBean(OutpatientFeedetail.class)).list();
		
		return list;
	}

	@Override
	public List<InpatientMedicineList> querymedilist(String medicalrecordId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct (to_date(trunc(f.out_date))-to_date(trunc(f.in_date))) as days ");
		sb.append(" from t_inpatient_info f ");
		sb.append(" left join t_inpatient_medicinelist m on m.inpatient_no=f.inpatient_no ");
		sb.append("  where   f.board_state=0 ");
		
		if(StringUtils.isNotBlank(medicalrecordId)){
			sb.append(" and f.medicalrecordid ='"+medicalrecordId+"' ");
		}
		Query query = this.getSession().createSQLQuery(sb.toString())
		.addScalar("days",Hibernate.INTEGER);
		List<InpatientMedicineList> list = query.setResultTransformer(Transformers.aliasToBean(InpatientMedicineList.class)).list();
		
		return list;
	}

	@Override
	public List<DoctorWorkloadStatistics> queryReservationMongDB(String deptw,
			String stimew, String etimew) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		//分组
		DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("deptCode", "$deptCode").append("doctor", "$doctor").append("deptName", "$deptName"));
		groupFields.put("ghs", new BasicDBObject("$sum","$ghs"));  
		groupFields.put("yyh", new BasicDBObject("$sum","$yyh"));  
		groupFields.put("jzs", new BasicDBObject("$sum","$jzs"));  
		groupFields.put("dhyy", new BasicDBObject("$sum","$dhyy"));  
		groupFields.put("wlyy", new BasicDBObject("$sum","$wlyy"));  
		groupFields.put("zhghs", new BasicDBObject("$sum","$zhghs"));  
		groupFields.put("zhjzs", new BasicDBObject("$sum","$zhjzs"));  
		
		//查询
		bdObjectTimeS.append("searchDate", new BasicDBObject("$gte",stimew));
		bdObjectTimeE.append("searchDate", new BasicDBObject("$lte",etimew));
		condList.add(bdObjectTimeS);
		condList.add(bdObjectTimeE);
		bdbObject.put("$and", condList);
		if(StringUtils.isNotBlank(deptw)){
			String [] dept=deptw.split(",");
			BasicDBList mongoDeptList = new BasicDBList();
			for(String vo:dept){
				mongoDeptList.add(new BasicDBObject("deptCode",vo));
			}
			bdbObject.put("$or",mongoDeptList);
		}
		DBObject match = new BasicDBObject("$match", bdbObject); 
		DBObject group = new BasicDBObject("$group", groupFields);
		AggregationOutput output =new MongoBasicDao().findGroupBy("YSGZLTJ_TOTAL_DAY", match, group);
		Iterator<DBObject> it = output.results().iterator();
		List<DoctorWorkloadStatistics> list=new ArrayList<DoctorWorkloadStatistics>();
		while(it.hasNext()){
			DoctorWorkloadStatistics vo=new DoctorWorkloadStatistics();
			BasicDBObject dbo = ( BasicDBObject ) it.next();
			BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
			vo.setDept(keyValus.getString("deptName"));
			vo.setName(keyValus.getString("doctor"));
			vo.setRegNo(dbo.getInt("ghs"));
			vo.setBookNo(dbo.getInt("yyh"));
			vo.setVisNo(dbo.getInt("jzs"));
			vo.setTelBook(dbo.getInt("dhyy"));
			vo.setNetBook(dbo.getInt("wlyy"));
			vo.setRegTot(dbo.getInt("zhghs"));
			vo.setArrTot(dbo.getInt("zhjzs"));
			list.add(vo);
		}
		if(list!=null&&list.size()>0){
			int bookSum=0;
			for(DoctorWorkloadStatistics s:list){
				bookSum=s.getBookNo()+s.getTelBook()+s.getNetBook();
				s.setBookTot(bookSum);
				s.setValidTot(0);
				s.setBookTot(s.getBookNo());
			}
			return list;
		}
		
		return new ArrayList<DoctorWorkloadStatistics>();
	}

}
