package cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.dao.OutpatientIndicatorsDao;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;
@Repository("outpatientIndicatorsDao")
@SuppressWarnings("all")
public class OutpatientIndicatorsDaoImpl extends HibernateDaoSupport implements OutpatientIndicatorsDao {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<OutpatientIndicatorsVO> queryTotalOutpatientClinicVisits(
			String tableName, final String sDate, final String eDate,boolean flag,boolean areaFlag) {
		final StringBuffer sb = new StringBuffer();
		if(areaFlag){
			sb.append(" Select t.AREA_CODE deptCode, NVL(to_number(Count(1)),0.0) numerator ");
		}else{
			sb.append(" Select t.Dept_Code deptCode, NVL(to_number(Count(1)),0.0) numerator ");
		}
		sb.append(" from ").append(tableName).append(" t ");
		sb.append(" where t.Stop_Flg = 0 and t.Del_Flg = 0 And t.Valid_Flag = 1 And t.In_State = 0 And t.Ynregchrg = 1 ");
		sb.append(" And t.Reg_Date >= To_Date(:sDate, 'yyyy-mm-dd') And t.Reg_Date < To_Date(:eDate, 'yyyy-mm-dd') ");
		if(flag){
			sb.append(" and t.YNSEE = 1 ");
		}
		if(areaFlag){
			sb.append(" Group By t.AREA_CODE ");
		}else{
			sb.append(" Group By t.Dept_Code ");
		}
		List<OutpatientIndicatorsVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<OutpatientIndicatorsVO>>() {

			@Override
			public List<OutpatientIndicatorsVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("sDate", sDate);
				query.setParameter("eDate", eDate);
				query.addScalar("deptCode");
				query.addScalar("numerator",Hibernate.DOUBLE);
				return query.setResultTransformer(Transformers.aliasToBean(OutpatientIndicatorsVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OutpatientIndicatorsVO>();
	}

	@Override
	public List<OutpatientIndicatorsVO> queryClinicWorkTotalTime(
			String tableName, final String sDate, final String eDate,boolean areaFlag) {
		final StringBuffer sb = new StringBuffer();
		if(areaFlag){
			sb.append(" Select t.AREA_CODE deptCode, NVL(to_number(Count(1)/2),0.0) denominator ");
		}else{
			sb.append(" Select t.Schedule_Deptid deptCode, NVL(to_number(Count(1)/2),0.0) denominator ");
		}
		sb.append(" from ").append(tableName).append(" t ");
		sb.append(" Where t.Schedule_Isstop = 0  ");
		sb.append(" And t.Schedule_Date >= To_Date(:sDate, 'yyyy-mm-dd') ");
		sb.append(" And t.Schedule_Date < To_Date(:eDate, 'yyyy-mm-dd') ");
		if(areaFlag){
			sb.append(" Group By t.AREA_CODE ");
		}else{
			sb.append(" Group By t.Schedule_Deptid ");
		}
		List<OutpatientIndicatorsVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<OutpatientIndicatorsVO>>() {

			@Override
			public List<OutpatientIndicatorsVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("sDate", sDate).setParameter("eDate", eDate);
				query.addScalar("deptCode").addScalar("denominator",Hibernate.DOUBLE);
				return query.setResultTransformer(Transformers.aliasToBean(OutpatientIndicatorsVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OutpatientIndicatorsVO>();
	}

	@Override
	public List<OutpatientIndicatorsVO> queryTotalOutpatientIncome(
			String tableName, final String sDate, final String eDate,boolean areaFlag) {
		final StringBuffer sb = new StringBuffer();
		if(areaFlag){
			sb.append(" Select t.AREA_CODE deptCode, NVL(to_number(Sum(t.Tot_Cost)),0.0) numerator ");
		}else{
			sb.append(" Select t.Reg_Dpcd deptCode, NVL(to_number(Sum(t.Tot_Cost)),0.0) numerator ");
		}
		sb.append(" from ").append(tableName).append(" t ");
		sb.append(" Where t.Trans_Type = 1 And t.Cancel_Flag = 1 And t.Pay_Flag = 1 ");
		sb.append(" and t.FEE_DATE >= to_date(:sDate,'yyyy-mm-dd') ");
		sb.append(" and t.FEE_DATE < to_date(:eDate,'yyyy-mm-dd') ");
		if(areaFlag){
			sb.append(" Group By t.AREA_CODE ");
		}else{
			sb.append(" Group By t.reg_dpcd ");
		}
		List<OutpatientIndicatorsVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<OutpatientIndicatorsVO>>() {

			@Override
			public List<OutpatientIndicatorsVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("sDate", sDate).setParameter("eDate", eDate);
				query.addScalar("deptCode").addScalar("numerator",Hibernate.DOUBLE);
				return query.setResultTransformer(Transformers.aliasToBean(OutpatientIndicatorsVO.class)).list();
			}
		});
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<OutpatientIndicatorsVO>();
	}

	@Override
	public List<OutpatientIndicatorsVO> queryTotalOutpatientAndEmergencyTime(
			String tableName, final String sDate, final String eDate,boolean areaFlag) {
		final StringBuffer sb = new StringBuffer();
		if(areaFlag){
			sb.append(" Select m.AREA_CODE deptCode, NVL(TO_NUMBER(Count(1)),0.0) numerator ");
		}else{
			sb.append(" Select m.Dept_Code deptCode, NVL(TO_NUMBER(Count(1)),0.0) numerator ");
		}
		sb.append(" from ").append(tableName).append(" m, t_Inpatient_Proof t ");
		sb.append(" Where m.Clinic_Code = t.Idcard_No And t.STOP_FLG = 0 And t.DEL_FLG = 0 And m.YNREGCHRG = 1 And m.VALID_FLAG = 1 And m.IN_STATE = 0 And m.STOP_FLG = 0 And m.DEL_FLG = 0 ");
		sb.append(" And m.Reg_Date >= To_Date(:sDate, 'yyyy-mm-dd') And m.Reg_Date < To_Date(:eDate, 'yyyy-mm-dd') ");
		if(areaFlag){
			sb.append(" Group By m.AREA_CODE ");
		}else{
			sb.append(" Group By m.Dept_Code ");
		}
		List<OutpatientIndicatorsVO> list = this.getHibernateTemplate().execute(new HibernateCallback<List<OutpatientIndicatorsVO>>() {

			@Override
			public List<OutpatientIndicatorsVO> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.setParameter("sDate", sDate).setParameter("eDate", eDate);
				query.addScalar("deptCode").addScalar("numerator", Hibernate.DOUBLE);
				return query.setResultTransformer(Transformers.aliasToBean(OutpatientIndicatorsVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OutpatientIndicatorsVO>();
	}
	
}
