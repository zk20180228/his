package cn.honry.statistics.deptstat.internalCompare1.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.internalCompare1.dao.InternalCompare1Dao;
import cn.honry.statistics.deptstat.internalCompare1.vo.FicDeptVO;
import cn.honry.statistics.deptstat.internalCompare1.vo.InternalCompare1Vo;
import cn.honry.utils.NumberUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**  
 *  
 * @className：InternalCompare1DaoImpl
 * @Description： 郑州大学第一附属医院儿外一门诊和内二医学部对比表1
 * @Author：gaotiantian
 * @CreateDate：2017-6-5 下午4:49:53
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Repository("internalCompare1Dao")
@SuppressWarnings({ "all" })
public class InternalCompare1DaoImpl extends HibernateEntityDao<InternalCompare1Vo> implements InternalCompare1Dao {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<InternalCompare1Vo> getInternalCompare1(List<String> tnL,List<String> tnL1,
			String prevTime, String time, String dept) {
		StringBuffer buffer= new StringBuffer(1000);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		buffer.append("select deptName,AA.deptCode,district,nvl(registerCountPrev,0) registerCountPrev,nvl(registerCount,0) registerCount,");
		buffer.append("nvl(inHospitalCountPrev,0) inHospitalCountPrev,nvl(inHospitalCount,0) inHospitalCount,");
		buffer.append("nvl(outHospitalCountPrev,0) outHospitalCountPrev,nvl(outHospitalCount,0) outHospitalCount,");
		buffer.append("nvl(avgInpatientCountPrev,0) avgInpatientCountPrev,nvl(avgInpatientCount,0) avgInpatientCount,");
		buffer.append("nvl(bedTurnoverCount,0) bedTurnoverCount,nvl(bedTurnoverCountPrev,0) bedTurnoverCountPrev,");
		buffer.append("nvl(realBed,0) realBed,nvl(realUsedBed,0) realUsedBed,");
		buffer.append("nvl(prevRealBed,0) prevRealBed,nvl(prevRealUsedBed,0) prevRealUsedBed,");
		buffer.append("nvl(PrevNodeath,0) PrevNodeath,nvl(prevDeath,0) prevDeath,");
		buffer.append("nvl(nodeath,0) nodeath,nvl(death,0) death ");
		
		buffer.append("from ");
		buffer.append("(SELECT d.DEPT_NAME as deptName,d.DEPT_CODE as deptCode,d.DEPT_AREA_CODE as district FROM T_DEPARTMENT d WHERE DEPT_CODE in('"+dept+"') ) AA ,");
		buffer.append("(SELECT count(1) as registerCountPrev,DEPT_CODE deptCode  FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,REG_DATE ");
			buffer.append("from ").append(tnL.get(i));
		}
		buffer.append(") WHERE TO_CHAR(REG_DATE,'YYYY-MM') = '"+prevTime+"' AND DEPT_CODE in( '"+dept+"') group by DEPT_CODE) BB,");
		buffer.append("(SELECT count(1)  as registerCount,DEPT_CODE deptCode FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,REG_DATE ");
			buffer.append("from ").append(tnL.get(i));
		}
		buffer.append(" )WHERE TO_CHAR(REG_DATE,'YYYY-MM') = '"+time+"' AND DEPT_CODE in ('"+dept+"') group by DEPT_CODE) CC,");
		buffer.append("(SELECT count(1) as inHospitalCountPrev,DEPT_CODE deptCode FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,IN_DATE ");
			buffer.append("from ").append(tnL1.get(i));
		}
		buffer.append(") WHERE TO_CHAR(IN_DATE,'YYYY-MM') = '"+prevTime+"' AND DEPT_CODE in('"+dept+"') group by DEPT_CODE ) DD,");
		buffer.append("(SELECT count(1) as inHospitalCount,DEPT_CODE deptCode FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,IN_DATE ");
			buffer.append("from ").append(tnL1.get(i));
		}
		buffer.append(") WHERE TO_CHAR(IN_DATE,'YYYY-MM') = '"+time+"' AND DEPT_CODE in ('"+dept+"') group by DEPT_CODE) EE ,");
		buffer.append("(SELECT count(1)  as outHospitalCountPrev,DEPT_CODE deptCode FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,OUT_DATE ");
			buffer.append("from ").append(tnL1.get(i));
		}
		buffer.append(") WHERE TO_CHAR(OUT_DATE,'YYYY-MM') = '"+prevTime+"' AND DEPT_CODE in('"+dept+"') group by DEPT_CODE ) FF,");
		buffer.append("(SELECT count(1)  as outHospitalCount,DEPT_CODE deptCode  FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,OUT_DATE ");
			buffer.append("from ").append(tnL1.get(i));
		}
		buffer.append(") WHERE TO_CHAR(OUT_DATE,'YYYY-MM') = '"+time+"' AND DEPT_CODE in('"+dept+"') group by DEPT_CODE) GG,"); 
		buffer.append("(SELECT ROUND(AVG(TRUNC(OUT_DATE - IN_DATE)),1) as avgInpatientCountPrev ,DEPT_CODE deptCode FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,OUT_DATE,IN_DATE,IN_STATE ");
			buffer.append("from ").append(tnL1.get(i));
		}
		buffer.append(") WHERE IN_STATE= 'O' and TO_CHAR(OUT_DATE,'YYYY-MM')  ='"+prevTime+"' AND DEPT_CODE in('"+dept+"') group by DEPT_CODE) HH,");
		buffer.append("(SELECT ROUND(AVG(TRUNC(OUT_DATE - IN_DATE)),1)  as avgInpatientCount,DEPT_CODE deptCode FROM (");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("SELECT DEPT_CODE,OUT_DATE,IN_DATE,IN_STATE ");
			buffer.append("from ").append(tnL1.get(i));
		}
		buffer.append(") WHERE IN_STATE= 'O' and TO_CHAR(OUT_DATE,'YYYY-MM')  = '"+time+"' AND DEPT_CODE in ('"+dept+"') group by DEPT_CODE ) JJ,");
		buffer.append("(select sum(t1.out_transfer)  as bedTurnoverCount, t1.DEPT_CODE deptCode from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT, 'YYYY-MM') = '"+time+"'  and   t1.DEPT_CODE in('"+dept+"') group by  t1.DEPT_CODE) OO,");
		buffer.append("(select sum(t1.out_transfer) as bedTurnoverCountPrev, t1.DEPT_CODE deptCode  from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT, 'YYYY-MM') = '"+prevTime+"' and   t1.DEPT_CODE in('"+dept+"') group by  t1.DEPT_CODE) PP ,");
		buffer.append("(select sum(t1.bed_stand) realBed, decode(sum(t1.end_num),null,0) realUsedBed,t1.DEPT_CODE deptCode from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT, 'YYYY-MM') = '"+time+"' and   t1.DEPT_CODE in('"+dept+"') group by t1.DEPT_CODE) QQ,");
		buffer.append("(select sum(t1.bed_stand) prevRealBed,t1.DEPT_CODE deptCode ,sum(t1.end_num) prevRealUsedBed  from t_ARCLOG_inpatient_dayreport t1 where TO_CHAR(DATE_STAT,'YYYY-MM') = '"+prevTime+"' and   t1.DEPT_CODE in('"+dept+"') group by t1.DEPT_CODE) WW,");
		buffer.append("(select (t.OUT_UNCURE+t.OUT_better+t.OUT_other+t.OUT_CURE) PrevNodeath ,t.OUT_DEATH prevDeath,t.DEPT_CODE deptCode  from t_ARCLOG_inpatient_dayreport t where TO_CHAR(t.DATE_STAT, 'YYYY-MM') = '"+prevTime+"'  and t.DEPT_CODE in('"+dept+"')) YY,");
		buffer.append("(select (t.OUT_UNCURE+t.OUT_better+t.OUT_other+t.OUT_CURE) nodeath ,t.OUT_DEATH death,t.DEPT_CODE deptCode  from t_ARCLOG_inpatient_dayreport t where TO_CHAR(t.DATE_STAT, 'YYYY-MM') = '"+time+"'  and t.DEPT_CODE in ('"+dept+"') ) ZZ ");
		buffer.append("where AA.deptCode =BB.deptCode (+)  and AA.deptCode =CC.deptCode(+) and AA.deptCode =DD.deptCode(+) ");
		buffer.append("and AA.deptCode =EE.deptCode (+) and AA.deptCode =FF.deptCode (+) and AA.deptCode =GG.deptCode (+) ");
		buffer.append("and AA.deptCode =HH.deptCode(+) and AA.deptCode =JJ.deptCode(+) and AA.deptCode =OO.deptCode(+) ");
		buffer.append("and AA.deptCode =PP.deptCode (+) and AA.deptCode =QQ.deptCode (+) and AA.deptCode =WW.deptCode (+) and AA.deptCode=YY.deptCode (+) and AA.deptCode =ZZ.deptCode(+) ");

		List<InternalCompare1Vo> voList = namedParameterJdbcTemplate.query(buffer.toString(), paraMap, new RowMapper<InternalCompare1Vo>(){
			NumberUtil n = NumberUtil.init();
			@Override
			public InternalCompare1Vo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				
				InternalCompare1Vo vo = new InternalCompare1Vo();
				
				vo.setDeptName(rs.getString("deptName"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDistrict(rs.getString("district"));
				
				int registerCountPrev = rs.getInt("registerCountPrev");//门诊量 去年
				int registerCount = rs.getInt("registerCount");//门诊量 今年
				
				vo.setRegisterCountPrev(registerCountPrev);
				vo.setRegisterCount(registerCount);
				
					
				
				int inHospitalCountPrev = rs.getInt("inHospitalCountPrev");//入院人数去年
				int inHospitalCount = rs.getInt("inHospitalCount");//入院人数 今年
				vo.setInHospitalCountPrev(inHospitalCountPrev);
				vo.setInHospitalCount(inHospitalCount);
				
				
				int outHospitalCountPrev = rs.getInt("outHospitalCountPrev");//出院人数（上一年）
				int outHospitalCount = rs.getInt("outHospitalCount");//出院人数（当年）
				vo.setOutHospitalCountPrev(outHospitalCountPrev);
				vo.setOutHospitalCount(outHospitalCount);
				
				
				double avgInpatientCountPrev = rs.getDouble("avgInpatientCountPrev");//平均住院天数（上一年）
				double avgInpatientCount = rs.getDouble("avgInpatientCount");//平均住院天数（当年）
				vo.setAvgInpatientCountPrev(avgInpatientCountPrev);
				vo.setAvgInpatientCount(avgInpatientCount);
				
				
				
				Integer bedTurnoverCountPrev = rs.getInt("bedTurnoverCountPrev");//病床周转次数（上年）
				Integer bedTurnoverCount = rs.getInt("bedTurnoverCount");//病床周转次数（当年）
				vo.setBedTurnoverCount(bedTurnoverCount);
				vo.setBedTurnoverCountPrev(bedTurnoverCountPrev);
				
				int prevRealUsedBed=rs.getInt("prevRealUsedBed");//使用病床(上年)
				int prevRealBed=rs.getInt("prevRealBed");//病床(上年)
				vo.setPrevRealBed(prevRealBed);
				vo.setPrevRealUsedBed(prevRealUsedBed);
				
				
				
				int realUsedBed=rs.getInt("realUsedBed");//使用病床(当年)
				int realBed=rs.getInt("realBed");//病床(当年)
				vo.setRealUsedBed(realUsedBed);
				vo.setRealBed(realBed);
				
				int prevNodeath=rs.getInt("PrevNodeath");//危重未死（上年）
				vo.setPrevNodeath(prevNodeath);
				int prevDeath=rs.getInt("prevDeath");//危重死亡（上年）
				vo.setPrevDeath(prevDeath);
				int nodeath=rs.getInt("nodeath");//危重未死（当年）
				vo.setNodeath(nodeath);
				int death=rs.getInt("death");//危重死亡（当年）
				vo.setDeath(death);
				
				
				return vo;
			}			
		});
		if(voList != null && voList.size() > 0){
			return voList;
		}
		return new ArrayList<InternalCompare1Vo>();
	}

	@Override
	public void saveInternalCompare1ToDB(List<String> tnL,
			List<String> tnL1, String begin, String end, String dept) {
		StringBuffer s = new StringBuffer();
		
		s.append("SELECT d.DEPT_NAME as deptName,d.DEPT_CODE as deptCode,d.DEPT_AREA_CODE as district,");
		s.append("(SELECT nvl(count(1),0) FROM T_REGISTER_MAIN WHERE TO_CHAR(REG_DATE,'YYYY-MM-dd') BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as registerCountPrev,");
		s.append("(SELECT nvl(count(1),0) FROM T_REGISTER_MAIN WHERE TO_CHAR(REG_DATE,'YYYY-MM-dd') BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as registerCount,");
		s.append("(SELECT nvl(count(1),0) FROM T_INPATIENT_INFO WHERE TO_CHAR(IN_DATE,'YYYY-MM-dd') BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as inHospitalCountPrev,");
		s.append("(SELECT nvl(count(1),0) FROM T_INPATIENT_INFO WHERE TO_CHAR(IN_DATE,'YYYY-MM-dd') BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as inHospitalCount,");
		s.append("(SELECT nvl(count(1),0) FROM T_INPATIENT_INFO WHERE TO_CHAR(OUT_DATE,'YYYY-MM-dd') BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as outHospitalCountPrev,");
		s.append("(SELECT nvl(count(1),0) FROM T_INPATIENT_INFO WHERE TO_CHAR(OUT_DATE,'YYYY-MM-dd') BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as outHospitalCount,");
		s.append("(SELECT nvl(ROUND(AVG(TRUNC(OUT_DATE - IN_DATE)),1),0) FROM T_INPATIENT_INFO WHERE IN_STATE= 'O' and TO_CHAR(OUT_DATE,'YYYY-MM-dd')  BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as avgInpatientCountPrev,");
		s.append("(SELECT nvl(ROUND(AVG(TRUNC(OUT_DATE - IN_DATE)),1),0) FROM T_INPATIENT_INFO WHERE IN_STATE= 'O' and TO_CHAR(OUT_DATE,'YYYY-MM-dd')  BETWEEN '"+begin+"' AND '"+end+"' AND DEPT_CODE = '"+dept+"') as avgInpatientCount");
		s.append(" FROM T_DEPARTMENT d WHERE DEPT_CODE = '"+dept+"'");	

		List<InternalCompare1Vo> list = super.getSession().createSQLQuery(s.toString()).addScalar("deptCode").addScalar("deptName").addScalar("district",Hibernate.INTEGER).addScalar("registerCountPrev",Hibernate.INTEGER)
				.addScalar("registerCount",Hibernate.INTEGER).addScalar("inHospitalCountPrev",Hibernate.INTEGER).addScalar("inHospitalCount",Hibernate.INTEGER).addScalar("outHospitalCountPrev",Hibernate.INTEGER)
				.addScalar("outHospitalCount",Hibernate.INTEGER).addScalar("avgInpatientCountPrev",Hibernate.DOUBLE).addScalar("avgInpatientCount",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(InternalCompare1Vo.class)).list();
		if (list != null && list.size() > 0) {
			for(InternalCompare1Vo vo : list){
				BasicDBObject bdObject1 = new BasicDBObject();
				Document document1 = new Document();
				document1.append("searchTime", end.substring(0, 7));
				document1.append("deptCode", vo.getDeptCode());
				
				Document document = new Document();
				document.append("deptName", vo.getDeptName());
				document.append("deptCode", vo.getDeptCode());
				document.append("searchTime", end.substring(0, 7));
				document.append("district", vo.getDistrict());
				document.append("registerCountPrev", vo.getRegisterCountPrev());
				document.append("registerCount", vo.getRegisterCount());
				document.append("inHospitalCountPrev", vo.getInHospitalCountPrev());
				document.append("inHospitalCount", vo.getInHospitalCount());
				document.append("outHospitalCountPrev", vo.getOutHospitalCountPrev());
				document.append("outHospitalCount", vo.getOutHospitalCount());
				document.append("avgInpatientCountPrev", vo.getAvgInpatientCountPrev());
				document.append("avgInpatientCount", vo.getAvgInpatientCount());
				
				new MongoBasicDao().update("YYNKYXBHNEYXBDBBO", document1, document, true);
			}
		}
	
	}

	@Override
	public List<InternalCompare1Vo> queryForDBSque(String prevTime,
			String time, String depts1, String deptName) {
		List<InternalCompare1Vo> listPrev1 = new ArrayList<InternalCompare1Vo>();
		List<InternalCompare1Vo> list1 = new ArrayList<InternalCompare1Vo>();
		NumberUtil num = NumberUtil.init();	//工具类
		//查询内科医学部上年的数据
			BasicDBObject bdObject = new BasicDBObject();
//			BasicDBObject beginbasic = new BasicDBObject();//开始
//			BasicDBObject endBasic = new BasicDBObject();//结束	
			String sql="select d.dept_code as deptCode from t_department d right join  t_fictitious_contact t on t.dept_code=d.dept_code where t.fict_code='"+depts1+"'";
			List<FicDeptVO> listDep=this.getSession().createSQLQuery(sql).addScalar("deptCode")
					.setResultTransformer(Transformers.aliasToBean(FicDeptVO.class)).list();
			
//			BasicDBList timeList=new BasicDBList();
			
			bdObject.append("time", time);
			if(listDep.size()>0){
				BasicDBList deptList=new BasicDBList();
				for(FicDeptVO vo:listDep){
					deptList.add(new BasicDBObject("deptCode",vo.getDeptCode()));//住院科室
				}
				bdObject.put("$or", deptList);
			}
			DBCursor cursor = new MongoBasicDao().findAlldata("YYNKYXBHNEYXBDBBO_MONTH_NOW", bdObject);
			DBObject dbCursor;
//			BigDecimal dou = null;
			while(cursor.hasNext()){
				dbCursor = cursor.next();
				InternalCompare1Vo vo = new InternalCompare1Vo();
				vo.setDeptCode((String)dbCursor.get("deptCode"));
				vo.setDeptName((String)dbCursor.get("deptName"));
				vo.setDistrict((String)dbCursor.get("district"));
				vo.setRegisterCountPrev((Integer)dbCursor.get("registerCountPrev"));
				vo.setRegisterCount((Integer)dbCursor.get("registerCount"));
				vo.setInHospitalCountPrev((Integer)dbCursor.get("inHospitalCountPrev"));
				vo.setInHospitalCount((Integer)dbCursor.get("inHospitalCount"));
				vo.setOutHospitalCountPrev((Integer)dbCursor.get("outHospitalCountPrev"));
				vo.setOutHospitalCount((Integer)dbCursor.get("outHospitalCount"));
				vo.setAvgInpatientCountPrev((Double)dbCursor.get("avgInpatientCountPrev"));
				vo.setAvgInpatientCount((Double)dbCursor.get("avgInpatientCount"));
				vo.setBedTurnoverCountPrev((Integer)dbCursor.get("bedTurnoverCountPrev"));
				vo.setBedTurnoverCount((Integer)dbCursor.get("bedTurnoverCount"));
				vo.setPrevRealUsedBed((Integer)dbCursor.get("prevRealUsedBed"));
				vo.setPrevRealBed((Integer)dbCursor.get("prevRealBed"));
				vo.setRealUsedBed((Integer)dbCursor.get("realUsedBed"));
				vo.setRealBed((Integer)dbCursor.get("realBed"));
				vo.setPrevNodeath((Integer)dbCursor.get("PrevNodeath"));
				vo.setPrevDeath((Integer)dbCursor.get("prevDeath"));
				vo.setNodeath((Integer)dbCursor.get("nodeath"));
				vo.setDeath((Integer)dbCursor.get("death"));
				listPrev1.add(vo);
			}
		return listPrev1;
	}

	@Override
	public List<FicDeptVO> getDept(final String ficDeptCode) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" Select Distinct c.DEPT_CODE id,c.DEPT_NAME name,d.DEPT_DISTRICT district,c.fict_code ficCode,c.fict_name ficName ");
		sb.append(" From t_Fictitious_Contact c, t_Fictitious_Dept d,T_SYS_COLUMN_DEPT cd Where c.Fict_Code = d.Dept_Code ");
		if(StringUtils.isNotBlank(ficDeptCode)){
			sb.append(" And c.FICT_CODE = :ficDeptCode ");
		}

		List<FicDeptVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<FicDeptVO>>() {

			@Override
			public List<FicDeptVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				if(StringUtils.isNotBlank(ficDeptCode)){
					query.setParameter("ficDeptCode", ficDeptCode);
				}
				query.addScalar("id").addScalar("name").addScalar("district",Hibernate.INTEGER)
					.addScalar("ficCode").addScalar("ficName");
				return query.setResultTransformer(Transformers.aliasToBean(FicDeptVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<FicDeptVO>();
	}
}

