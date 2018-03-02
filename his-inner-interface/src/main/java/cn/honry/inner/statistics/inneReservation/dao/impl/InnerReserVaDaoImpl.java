package cn.honry.inner.statistics.inneReservation.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.inneReservation.dao.InnerReserVaDao;
import cn.honry.inner.statistics.inneReservation.vo.InnerRegGradeVo;
import cn.honry.inner.statistics.inneReservation.vo.ReservationStatisticsVo;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("innerReserVaDao")
public class InnerReserVaDaoImpl implements InnerReserVaDao {
	private final String[] pret={"T_REGISTER_PREREGISTER_NOW","T_REGISTER_PREREGISTER"};
	private final String[] main={"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public void initReserVation(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		String stime=date+" 00:00:00";
		String etime=date+" 23:59:59";
		
		List<String> pretnl=wordLoadDocDao.returnInTables(stime, etime, pret, "MZ");
		List<String> maintnl=wordLoadDocDao.returnInTables(stime, etime, main, "MZ");
		if(pretnl!=null&&pretnl.size()>0&&maintnl!=null&&maintnl.size()>0){
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT g.GRADE_CODE as gradeCode,g.GRADE_EXPERTNO as gradeExpertNo,g.GRADE_SPECIALISTNO as gradePecialListNo FROM T_REGISTER_GRADE g WHERE g.GRADE_EXPERTNO=1 and g.DEL_FLG = 0 AND g.STOP_FLG = 0 ");
			List<InnerRegGradeVo> exepertList = namedParameterJdbcTemplate.query(sb.toString(), new RowMapper<InnerRegGradeVo>() {
				@Override
				public InnerRegGradeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					InnerRegGradeVo vo = new InnerRegGradeVo();
					vo.setGradeCode(rs.getString("gradeCode"));
					vo.setGradeExpertNo(rs.getInt("gradeExpertNo"));
					vo.setGradePecialListNo(rs.getInt("gradePecialListNo"));
					
					return vo;
				}
			});
			sb = new StringBuffer();
			sb.append("SELECT dept,(select t.dept_code from t_department t where rownum<2 and t.del_flg=0 and t.stop_flg=0 and t.dept_name=dept ) deptCode,");//科室
			sb.append("nvl(SUM(qbhypth),0) qbhypth, ");//全部号源-普通号
			sb.append("nvl(SUM(qbhyzjh),0) qbhyzjh, ");//全部号源-专家号
			sb.append("nvl(SUM(qbhyhj),0) qbhyhj, ");//全部号源-合计
			sb.append("nvl(SUM(jzrccz),0) jzrccz, ");//就诊人次-初诊
			sb.append("nvl(SUM(jzrcfz),0) jzrcfz, ");//就诊人次-复诊
			sb.append("nvl(SUM(jzrchj),0) jzrchj, ");//就诊人次-合计
			sb.append("nvl(SUM(ghypth),0) ghypth, ");//挂号源-普通号
			sb.append("nvl(SUM(ghyzjh),0) ghyzjh, ");//挂号源-专家号
			sb.append("nvl(SUM(yyfsdhyy),0) yyfsdhyy, ");//电话预约
			sb.append("nvl(SUM(yyfswlyy),0) yyfswlyy, ");//网络预约
			sb.append("nvl(SUM(jzlbcz),0) jzlbcz, ");//就诊人次-初诊
			sb.append("nvl(SUM(jzlbfz),0) jzlbfz, ");//就诊人次-复诊
			sb.append("nvl(SUM(hj),0) hj ");//预约挂号数量合计
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
					sb.append("sum(DECODE(m.REGLEVL_CODE ");
					for (InnerRegGradeVo reg : exepertList) {
						sb.append(",'").append(reg.getGradeCode()).append("',1");
					}
					sb.append(",0)) qbhyzjh, ");
					sb.append("SUM(DECODE(m.YNFR,1,1)) jzrccz, ");//就诊人次-初诊
					sb.append("SUM(DECODE(m.YNFR,0,1)) jzrcfz, ");//就诊人次-复诊
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
						sb.append(" AND m.REG_DATE>=TO_DATE('"+stime+"', 'yyyy-MM-dd HH24:mi:ss') ");
					}
					if(StringUtils.isNotBlank(etime)){
						sb.append(" AND m.REG_DATE<=TO_DATE('"+etime+"', 'yyyy-MM-dd HH24:mi:ss') ");
					}
					sb.append(" GROUP BY d.dept_name,m.REGLEVL_CODE ");
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
					sb.append("sum(DECODE(p.PREREGISTER_GRADE ");
					for (InnerRegGradeVo reg : exepertList) {
						sb.append(",'").append(reg.getGradeCode()).append("',1");
					}
					sb.append(",0)) ghyzjh, ");
					sb.append("sum(DECODE(p.PREREGISTER_ISPHONE ,1,1,0)) yyfsdhyy, ");//电话预约
					sb.append("sum(DECODE(p.PREREGISTER_ISNET ,1,1,0)) yyfswlyy, ");//网络预约
					sb.append("SUM(DECODE(p.PREREGISTER_ISFIRST,1,1)) jzlbcz, ");//就诊人次-初诊
					sb.append("SUM(DECODE(p.PREREGISTER_ISFIRST,2,1)) jzlbfz, ");//就诊人次-复诊
					sb.append("COUNT(p.SCHEDULE_ID) hj ");
					sb.append("FROM ").append(pretnl.get(i)).append(" p ");
					sb.append(" inner join t_department d on p.preregister_dept = d.dept_code ");
					sb.append("WHERE p.DEL_FLG = 0 and d.dept_type = 'C' ");
					sb.append("AND p.STOP_FLG = 0 ");
					if(StringUtils.isNotBlank(stime)){
						sb.append(" AND p.PREREGISTER_DATE>=TO_DATE('"+stime+"', 'yyyy-MM-dd HH24:mi:ss') ");
					}
					if(StringUtils.isNotBlank(etime)){
						sb.append(" AND p.PREREGISTER_DATE<=TO_DATE('"+etime+"', 'yyyy-MM-dd HH24:mi:ss') ");
					}
					sb.append(" GROUP BY d.dept_name ");
				}
				sb.append(") GROUP BY dept ");
			}
			sb.append(") GROUP BY dept");
			
			List<ReservationStatisticsVo> list = namedParameterJdbcTemplate.query(sb.toString(), new HashMap<String,String>(),new RowMapper<ReservationStatisticsVo>() {
				@Override
				public ReservationStatisticsVo mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					ReservationStatisticsVo vo = new ReservationStatisticsVo();
					vo.setCommonNumber(rs.getInt("qbhypth"));
					vo.setCountAllInfo(rs.getInt("qbhyhj"));
					vo.setCountDoctorVisits(rs.getInt("jzrchj"));
					vo.setDeptName(rs.getString("dept"));
					vo.setDeptCode(rs.getString("deptCode"));
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
			
			DBObject query = new BasicDBObject();
			query.put("time", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
			if(list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(ReservationStatisticsVo vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("time", date);
					
					obj.append("qbhypth", vo.getCommonNumber());
					
					obj.append("qbhyhj", vo.getCountAllInfo());
					
					obj.append("jzrchj", vo.getCountDoctorVisits());
					
					obj.append("deptName", vo.getDeptName());
					
					obj.append("deptCode", vo.getDeptCode());
					
					obj.append("jzrccz", vo.getFirstVisit());
					
					obj.append("jzrcfz", vo.getFurtherConsultation());
					
					obj.append("jzlbfz", vo.getFurtherConsultationRe());
					
					obj.append("jzlbcz", vo.getFirstVisitRe());
					
					obj.append("yyfswlyy", vo.getNetBooking());
					
					obj.append("qbhyzjh", vo.getNumberExpert());
					
					obj.append("ghyzjh", vo.getNumberExpertRe());
					
					obj.append("yyfsdhyy", vo.getPhoneBooking());
					
					obj.append("hj", vo.getTotal());
					voList.add(obj);
				}
				new MongoBasicDao().insertDataByList(menuAlias+"_DAY", voList);
				wordLoadDocDao.saveMongoLog(beginDate, menuAlias, voList, date);
			}
		}
	}

}
