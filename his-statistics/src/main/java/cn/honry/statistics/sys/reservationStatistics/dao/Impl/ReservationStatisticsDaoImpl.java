package cn.honry.statistics.sys.reservationStatistics.dao.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.outpatient.grade.dao.GradeInInterDAO;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.sys.reservationStatistics.dao.ReservationStatisticsDao;
import cn.honry.statistics.sys.reservationStatistics.vo.RegGradeVo;
import cn.honry.statistics.sys.reservationStatistics.vo.ReservationStatistics;
import cn.honry.utils.ShiroSessionUtils;


@Repository("reservationStatisticsDao")
@SuppressWarnings({"all"})
public class ReservationStatisticsDaoImpl extends HibernateEntityDao<RegisterPreregister> implements ReservationStatisticsDao{
	private final String[] column={"qbhypth","qbhyhj","jzrchj","jzrccz","jzrcfz","jzlbfz","jzlbcz","yyfswlyy","qbhyzjh","ghyzjh","yyfsdhyy","hj"};
	
	@Autowired
	@Qualifier(value = "registerInfoInInterDAO")
	private RegisterInfoInInterDAO registerInfoDAO;
	@Autowired
	@Qualifier(value = "gradeInInterDAO")
	private GradeInInterDAO gradeDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterDAO(DeptInInterDAO deptInInterDAO) {
		this.deptInInterDAO = deptInInterDAO;
	}

	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	/**  
	 *  
	 * @Description：  根据时间部门查询挂号全部
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterInfo> findinfo(String dept, String stime, String etime) {
		
		String hql=" FROM RegisterInfo r where r.stop_flg = 0 and r.del_flg = 0 ";
		
		if(StringUtils.isNotBlank(dept)){
			hql = hql+" AND r.dept LIKE '%"+dept+"%'";
		}
		if(StringUtils.isNotBlank(stime)&& StringUtils.isNotBlank(etime)){
			hql=hql+" and date Between  to_date('"+stime+"','yyyy-mm-dd hh24:mi:ss') And to_date('"+etime+"','yyyy-mm-dd hh24:mi:ss')";
		}
	  
		List<RegisterInfo>  infoList=registerInfoDAO.findByObjectProperty(hql, null);
	   
	    if(infoList!=null && infoList.size()>0){
		
		    return infoList;
	    }	
	 
	    return new ArrayList<RegisterInfo>();
  }
	
	/**  
	 *  
	 * @Description：  查询科室部门
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> finddept() {
		
		String hql=" FROM SysDepartment d where d.stop_flg = 0 and d.del_flg = 0 and d.deptIsforregister = '1'";
		
		List<SysDepartment> deptList=deptInInterDAO.findByObjectProperty(hql, null);
		
		if(deptList!=null && deptList.size()>0){
		
			return deptList;
		}	
		
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 *  
	 * @Description：  查询科室部门
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterPreregister> findregister(String dept, String stime,String etime) {
		
		String hql=" FROM RegisterPreregister r where r.stop_flg = 0 and r.del_flg = 0";
		
		if(StringUtils.isNotBlank(dept)){
			hql = hql+" AND r.preregisterDept LIKE '%"+dept+"%'";
		}
		
		if(StringUtils.isNotBlank(stime) && StringUtils.isNotBlank(etime)){
			hql=hql+" and preregisterDate Between  to_date('"+stime+"','yyyy-mm-dd hh24:mi:ss') And to_date('"+etime+"','yyyy-mm-dd hh24:mi:ss')";
		}
		
		List<RegisterPreregister> deptList = null; 
		
		deptList=this.find(hql, null);
		
		if(deptList!=null && deptList.size()>0){
			
			return deptList;
		}	
		
		return new ArrayList<RegisterPreregister>();
	}
	
	/**  
	 *  
	 * @Description：  查询挂号级别
	 * @Author：wujiao
	 * @CreateDate：2015-12-24 下午10:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterGrade> findgrade(String grade) {
		
		String hql=" FROM RegisterGrade g where g.stop_flg = 0 and g.del_flg = 0 and id='"+grade+"'";
		
		List<RegisterGrade> gradeList=gradeDAO.findByObjectProperty(hql, null);
		
		if(gradeList!=null && gradeList.size()>0){
		
			return gradeList;
		}	
		 
		return new ArrayList<RegisterGrade>();
	}
	
	/**  
	 *  
	 * @Description：  科室下拉框(挂号科室)
	 * @Author：wujiao
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getComboboxdept() {
		
		String hql=" FROM SysDepartment d where d.stop_flg = 0 and d.del_flg = 0 and d.deptIsforregister = '1'";
		
		List<SysDepartment> deptList=deptInInterDAO.findByObjectProperty(hql, null);
		
		if(deptList!=null && deptList.size()>0){
			
			return deptList;
		}	
		
		return new ArrayList<SysDepartment>();
	}
	
	public List<RegGradeVo> getRegGrade(){
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT g.GRADE_CODE as gradeCode,g.GRADE_EXPERTNO as gradeExpertNo,g.GRADE_SPECIALISTNO as gradePecialListNo FROM T_REGISTER_GRADE g WHERE g.DEL_FLG = 0 AND g.STOP_FLG = 0 ");
		List<RegGradeVo> list = namedParameterJdbcTemplate.query(sb.toString(), new RowMapper<RegGradeVo>() {
			@Override
			public RegGradeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegGradeVo vo = new RegGradeVo();
				vo.setGradeCode(rs.getString("gradeCode"));
				vo.setGradeExpertNo(rs.getInt("gradeExpertNo"));
				vo.setGradePecialListNo(rs.getInt("gradePecialListNo"));
				
				return vo;
			}
		});
		
		if(list==null||list.size()<=0){
		
			return new ArrayList<RegGradeVo>();
		}
		
		return list;
	}
	

	/**
	 * @Description 预约统计查询
	 * @author  marongbin
	 * @createDate： 2016年12月24日 下午2:03:43 
	 * @modifier zhangkui
	 * @modifyDate：2017-07-03 19:06:43 
	 * @param regGradeVO 所有挂号级别的集合
	 * @param tnl T_REGISTER_PREREGISTER 预约挂号表分区表集合
	 * @param T_REGISTER_MAIN 挂号主表分区表集合
	 * @param dept 部门编号
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @param page 当前页
	 * @param rows 每页显示的行
	 * @param jobNo 医院员工工作号
	 * @return: List<ReservationStatistics> vo
	 * @modifyRmk：  修改注释内容
	 * @version 1.0
	 */
	public List<ReservationStatistics> getInfoNow(List<RegGradeVo> regGradeVO,List<String> pretnl,List<String> maintnl,String dept, String stime,String etime,String page,String rows,String jobNo,String menuAlias){
		
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):50;
		List<String> deptCodeList = Arrays.asList(dept.split(","));
		String sql = this.getSQL(regGradeVO, pretnl, maintnl, dept, stime, etime,jobNo,menuAlias);
		
		if(sql==null){
			return new ArrayList<ReservationStatistics>();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from (select tab.*,rownum rn from( ");
		sb.append(sql);
		sb.append(") tab where dept is not null and  rownum <= ").append(p*r).append(" ) where  rn> ").append((p-1)*r);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stime", stime+" 00:00:00");
		paramMap.put("etime", etime+" 23:59:59");
		paramMap.put("dept", deptCodeList);
		List<ReservationStatistics> list = namedParameterJdbcTemplate.query(sb.toString(), paramMap,new RowMapper<ReservationStatistics>() {
			@Override
			public ReservationStatistics mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				ReservationStatistics vo = new ReservationStatistics();
				vo.setCommonNumber(rs.getInt("qbhypth"));
				vo.setCountAllInfo(rs.getInt("qbhyhj"));
				vo.setCountDoctorVisits(rs.getInt("jzrchj"));
				vo.setDeptName(rs.getString("dept"));
				vo.setFirstVisit(rs.getInt("jzrccz"));
				vo.setFurtherConsultation(rs.getInt("jzrcfz"));
				vo.setFurtherConsultationRe(rs.getInt("jzlbfz"));
				vo.setFirstVisitRe(rs.getInt("jzlbcz"));
				vo.setNetBooking(rs.getInt("yyfswlyy"));
				vo.setNumberExpert(rs.getInt("qbhyzjh"));
				vo.setNumberExpertRe(rs.getInt("ghyzjh"));
				vo.setPhoneBooking(rs.getInt("yyfsdhyy"));
				vo.setTotal(rs.getInt("hj"));
			
				return vo;
			}
		});
		
		if(list==null||list.size()<=0){
			return new ArrayList<ReservationStatistics>();
		}
		
		return list;
	}
	
	public String getSQL(List<RegGradeVo> regGradeVO,List<String> pretnl,List<String> maintnl,String dept, String stime,String etime,String jobNo,String menuAlias){
		
		if(pretnl!=null&&pretnl.size()>0&&maintnl!=null&&maintnl.size()>0){
			StringBuffer sb = new StringBuffer();
			List<RegGradeVo> exepertList = new ArrayList<RegGradeVo>();
			for (RegGradeVo re : regGradeVO) {
				if(re.getGradeExpertNo()==1){
					exepertList.add(re);
				}
			}
			sb.append("SELECT dept, ");//科室
			sb.append("SUM(qbhypth) qbhypth, ");//全部号源-普通号
			sb.append("SUM(qbhyzjh) qbhyzjh, ");//全部号源-专家号
			sb.append("SUM(qbhyhj) qbhyhj, ");//全部号源-合计
			sb.append("SUM(jzrccz) jzrccz, ");//就诊人次-初诊
			sb.append("SUM(jzrcfz) jzrcfz, ");//就诊人次-复诊
			sb.append("SUM(jzrchj) jzrchj, ");//就诊人次-合计
			sb.append("SUM(ghypth) ghypth, ");//挂号源-普通号
			sb.append("SUM(ghyzjh) ghyzjh, ");//挂号源-专家号
			sb.append("SUM(yyfsdhyy) yyfsdhyy, ");//电话预约
			sb.append("SUM(yyfswlyy) yyfswlyy, ");//网络预约
			sb.append("SUM(jzlbcz) jzlbcz, ");//就诊人次-初诊
			sb.append("SUM(jzlbfz) jzlbfz, ");//就诊人次-复诊
			sb.append("SUM(hj) hj ");//预约挂号数量合计
			sb.append("FROM( ");//
			if(maintnl.size()>0){
				
				sb.append("SELECT dept, ");//科室
				sb.append("NVL(SUM(qbhyhj), 0)-NVL(SUM(qbhyzjh), 0) qbhypth, ");//全部号源-普通号
				sb.append("SUM(qbhyzjh) qbhyzjh, ");//全部号源-专家号
				sb.append("SUM(qbhyhj) qbhyhj, ");//全部号源-合计
				sb.append("SUM(jzrccz) jzrccz, ");//就诊人次-初诊
				sb.append("SUM(jzrcfz) jzrcfz, ");//就诊人次-复诊
				sb.append("SUM(jzrchj) jzrchj, ");//就诊人次-合计
				sb.append("NVL(SUM(ghyhj), 0)-NVL(SUM(ghyzjh), 0) ghypth, ");//挂号源-普通号
				sb.append("SUM(ghyzjh) ghyzjh, ");//挂号源-专家号
				sb.append("SUM(yyfsdhyy) yyfsdhyy, ");//电话预约
				sb.append("SUM(yyfswlyy) yyfswlyy, ");//网络预约
				sb.append("SUM(jzlbcz) jzlbcz, ");//就诊人次-初诊
				sb.append("SUM(jzlbfz) jzlbfz, ");//就诊人次-复诊
				sb.append("SUM(hj) hj ");//预约挂号数量合计
				sb.append("FROM( ");
				for(int i = 0; i < maintnl.size(); i++){
					if(i!=0){
						sb.append("UNION ALL ");
					}
					sb.append("SELECT ");
					sb.append("d.dept_name dept, ");//科室
					sb.append("COUNT(m.REGLEVL_CODE) qbhyhj, ");//全部号源-合计
					
					sb.append(" sum(DECODE(m.REGLEVL_CODE ");
					for (RegGradeVo reg : exepertList) {
						sb.append(",'").append(reg.getGradeCode()).append("',1");
					}
					sb.append(",0)) qbhyzjh, ");
					
					
					sb.append("SUM(DECODE(m.YNFR,1,1,0)) jzrccz, ");//就诊人次-初诊
					sb.append("SUM(DECODE(m.YNFR,0,1,0)) jzrcfz, ");//就诊人次-复诊
					sb.append("COUNT(m.YNFR) jzrchj, ");///就诊人次-合计
					sb.append("NULL ghyhj, ");//挂号源-合计
					sb.append("NULL ghyzjh, ");//挂号源-专家号
					sb.append("NULL yyfsdhyy, ");//电话预约
					sb.append("NULL yyfswlyy, ");//网络预约
					sb.append("NULL jzlbcz, ");//就诊人次-初诊
					sb.append("NULL jzlbfz, ");//就诊人次-复诊
					sb.append("NULL hj ");//合计
					sb.append("FROM ").append(maintnl.get(i)).append(" m ");
					sb.append(" inner join t_department d on m.dept_code = d.dept_code ");
					sb.append("WHERE m.IN_STATE=0 and d.dept_type = 'C' ");
					if(StringUtils.isNotBlank(stime)){
						sb.append(" AND m.REG_DATE>=TO_DATE(:stime, 'yyyy-MM-dd HH24:mi:ss') ");
					}
					if(StringUtils.isNotBlank(etime)){
						sb.append(" AND m.REG_DATE<TO_DATE(:etime, 'yyyy-MM-dd HH24:mi:ss') ");
					}
					if(StringUtils.isNotBlank(dept)&&!"all".equals(dept)){
						sb.append(" AND m.DEPT_CODE in (:dept) ");
					}else{
						sb.append(" AND m.DEPT_CODE in (").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
					}
					sb.append(" GROUP BY d.dept_name ");
				}
				sb.append(") GROUP BY dept ");
			}
			
			
			
			sb.append("UNION ALL ");
			
			if(pretnl.size()>0){
				sb.append("SELECT dept, ");//科室
				sb.append("NVL(SUM(qbhyhj), 0)-NVL(SUM(qbhyzjh), 0) qbhypth, ");//全部号源-普通号
				sb.append("SUM(qbhyzjh) qbhyzjh, ");//全部号源-专家号
				sb.append("SUM(qbhyhj) qbhyhj, ");//全部号源-合计
				sb.append("SUM(jzrccz) jzrccz, ");//就诊人次-初诊
				sb.append("SUM(jzrcfz) jzrcfz, ");//就诊人次-复诊
				sb.append("SUM(jzrchj) jzrchj, ");//就诊人次-合计
				sb.append("NVL(SUM(ghyhj), 0)-NVL(SUM(ghyzjh), 0) ghypth, ");//挂号源-普通号
				sb.append("SUM(ghyzjh) ghyzjh, ");//挂号源-专家号
				sb.append("SUM(yyfsdhyy) yyfsdhyy, ");//电话预约
				sb.append("SUM(yyfswlyy) yyfswlyy, ");//网络预约
				sb.append("SUM(jzlbcz) jzlbcz, ");//就诊人次-初诊
				sb.append("SUM(jzlbfz) jzlbfz, ");//就诊人次-复诊
				sb.append("SUM(hj) hj ");//预约挂号数量合计
				sb.append("FROM( ");//
				for(int i = 0; i < pretnl.size(); i++){
					if(i!=0){
						sb.append("UNION ALL ");
					}
					sb.append("SELECT ");
					sb.append("d.dept_name dept, ");
					sb.append("NULL qbhyhj, ");
					sb.append("NULL qbhyzjh, ");
					sb.append("NULL jzrccz, ");
					sb.append("NULL jzrcfz, ");
					sb.append("NULL jzrchj, ");
					sb.append("COUNT(p.PREREGISTER_GRADE ) ghyhj, ");//挂号源-合计
					sb.append(" sum(DECODE(p.PREREGISTER_GRADE ");
					for (RegGradeVo reg : exepertList) {
						sb.append(",'").append(reg.getGradeCode()).append("',1");
					}
					sb.append(",0)) ghyzjh, ");
					sb.append("sum(DECODE(p.PREREGISTER_ISPHONE ,1,1,0)) yyfsdhyy, ");//电话预约
					sb.append("sum(DECODE(p.PREREGISTER_ISNET ,1,1,0)) yyfswlyy, ");//网络预约
					sb.append("SUM(DECODE(p.PREREGISTER_ISFIRST,1,1,0)) jzlbcz, ");//就诊人次-初诊
					sb.append("SUM(DECODE(p.PREREGISTER_ISFIRST,2,1,0)) jzlbfz, ");//就诊人次-复诊
					sb.append("COUNT(p.SCHEDULE_ID) hj ");
					sb.append("FROM ").append(pretnl.get(i)).append(" p ");
					sb.append(" inner join t_department d on p.preregister_dept = d.dept_code ");
					sb.append("WHERE p.DEL_FLG = 0 and d.dept_type = 'C' ");
					sb.append("AND p.STOP_FLG = 0 ");
					if(StringUtils.isNotBlank(stime)){
						sb.append(" AND p.PREREGISTER_DATE>=TO_DATE(:stime, 'yyyy-MM-dd HH24:mi:ss') ");
					}
					if(StringUtils.isNotBlank(etime)){
						sb.append(" AND p.PREREGISTER_DATE<TO_DATE(:etime, 'yyyy-MM-dd HH24:mi:ss') ");
					}
					if(StringUtils.isNotBlank(dept)){
						sb.append(" AND p.PREREGISTER_DEPT in (:dept)");
					}
					sb.append(" GROUP BY d.dept_name ");
				}
				sb.append(") GROUP BY dept ");
			}
			sb.append(") GROUP BY dept ORDER BY dept ");
	 	
			return sb.toString();
		}
		
		return null;
	}
	
	/**
	 * 
	 * <p>预约统计查询获取总记录数</p>
	 * @author  marongbin
	 * @createDate： 2016年12月24日 下午2:03:43 
	 * @modifier zhangkui
	 * @modifyDate：2017-07-03 19:06:43 
	 * @param regGradeVO 所有挂号级别的集合
	 * @param tnl T_REGISTER_PREREGISTER 预约挂号表分区表集合
	 * @param T_REGISTER_MAIN 挂号主表分区表集合
	 * @param dept 部门编号
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @param page 当前页
	 * @param rows 每页显示的行
	 * @param jobNo 医院员工工作号
	 * @return: List<ReservationStatistics> vo
	 * @modifyRmk：  修改注释内容
	 * @version 1.0
	 *
	 */
	public int getTotal(List<RegGradeVo> regGradeVO, List<String> pretnl,List<String> maintnl,
	String dept, String stime, String etime,String jobNo,String menuAlias) {
		
		StringBuffer sb = new StringBuffer();
		String sql = this.getSQL(regGradeVO, pretnl, maintnl, dept, stime, etime,jobNo,menuAlias);
		if(sql==null){
			return 0;
		}
		
		sb.append(" select count(1) from (").append(sql).append(")  where dept is not null");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stime", stime);
		paramMap.put("etime", etime);
		paramMap.put("dept", Arrays.asList(dept.split(",")));
		
		return namedParameterJdbcTemplate.queryForObject(sb.toString(), paramMap, Integer.class);
	}

	@Override
	public Map<String, Object> getInfoNow(String depts, String time,String etime, String page, String rows, String menuAlias)
			throws Exception {
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		BasicDBList mongoDeptList = new BasicDBList(); 
		
		bdObjectTimeS.put("time",new BasicDBObject("$gte",time));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("time",new BasicDBObject("$lte",etime));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		
		if(StringUtils.isBlank(depts)){//如果科室为空 查询授权科室
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
			if(deptList == null && deptList.size() == 0){//如果授权科室为空 查询当前医生工作量
				bdObject.append("doctor", jobNo);
			}else{
				int len=deptList.size();
				if(len>0){
					for(int i = 0;i<len;i++){
						mongoDeptList.add(new BasicDBObject("deptCode",deptList.get(i).getDeptCode()));
					}
					bdObject.put("$or", mongoDeptList);
				}
			}
		}else{
			String[] deptArr=depts.split(",");
			for(String dept:deptArr){
				mongoDeptList.add(new BasicDBObject("deptCode",dept));
			}
			bdObject.put("$or", mongoDeptList);
		}
		Map<String,List<Integer>> valueMap=new LinkedHashMap();
		List<Integer> valueList=null;
		List<Integer> tempList=null;
		List<Integer> keyList=null;
		String key=null;
		
		DBCursor cursor = new MongoBasicDao().findAlldataBySort("YYTJ_DAY", bdObject,"deptCode");
		DBObject dbCursor;
		while(cursor.hasNext()){
			valueList=new ArrayList(12);
			dbCursor=cursor.next();
			key=(String)dbCursor.get("deptName");
			for(int i=0,len=column.length;i<len;i++){
				valueList.add((Integer)dbCursor.get(column[i]));
			}
			if(valueMap.containsKey(key)){
				keyList=new ArrayList();
				tempList=valueMap.get(key);
				for(int i=0,len=tempList.size();i<len;i++){
					keyList.add(tempList.get(i)+valueList.get(i));
				}
				valueMap.put(key, keyList);
			}else{
				valueMap.put(key, valueList);
			}
		}
		tempList=null;
		valueList=null;
		Integer start=Integer.parseInt(rows)*(Integer.parseInt(page)-1);
		Integer end=Integer.parseInt(rows)*Integer.parseInt(page);
		int total=0;
		List<ReservationStatistics> reserList=new ArrayList();
		if(valueMap.size()>0){
			
			for(String keys:valueMap.keySet()){
				if(total>=start&&total<end){
					keyList=valueMap.get(keys);
					ReservationStatistics vo=new ReservationStatistics(keys,keyList);
					reserList.add(vo);
				}
				total++;
			}
		}
		Map<String,Object> returnMap=new HashMap();
		returnMap.put("total", valueMap.size());
		returnMap.put("rows", reserList);
		
		return returnMap;
	}
}
