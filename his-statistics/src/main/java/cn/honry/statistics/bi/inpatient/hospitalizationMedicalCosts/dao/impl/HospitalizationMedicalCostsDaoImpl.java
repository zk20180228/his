package cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.business.Page;
import cn.honry.base.bean.model.BiInpatientFeeinfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.dao.HospitalizationMedicalCostsDao;
import cn.honry.statistics.bi.inpatient.hospitalizationMedicalCosts.vo.HospitalizationMedicalCostsVo;


@Repository("hospitalizationMedicalCostsDao")
@SuppressWarnings({ "all" })
public class HospitalizationMedicalCostsDaoImpl extends HibernateEntityDao<BiInpatientFeeinfo> implements HospitalizationMedicalCostsDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	*	!!!!!!    t_ 之后要全部转为bi_
	*/
	@Override
	public List<HospitalizationMedicalCostsVo> querytDatagrid(String time,String nameString) {
		//查询字段 sql
		StringBuffer selectSql=new StringBuffer();

		selectSql.append("select d.dept_name as deptname,"
				+ "nvl(round(sum(t.zje),2),0) as totcost,"
				+ "count(t.cou) as coun,"
				+ " nvl(t.fee_code,0) as feecode"
				+ " from t_department d ");
		selectSql.append(" left join v_hospitalization_medicalcosts t on t.no=d.dept_code");
		if(time!=null&&time.length()>0){
			selectSql.append(" and t.fee_date= '"+time+"'");
		}
		selectSql.append(" where d.dept_type = 'I' ");
		if(!("'1'").equals(nameString)&&nameString.length()>0){
			selectSql.append(" and d.dept_code in ("+nameString+") ");
		}
		
		selectSql.append(" group by d.dept_name,t.fee_code ");
		
		final String sql=selectSql.toString(); 
			
		SQLQuery queryObject=this.getSession().createSQLQuery(sql);
		queryObject.addScalar("deptname");
		queryObject.addScalar("totcost",Hibernate.DOUBLE);
		queryObject.addScalar("coun",Hibernate.INTEGER);
		queryObject.addScalar("feecode");
		queryObject.setResultTransformer(Transformers.aliasToBean(HospitalizationMedicalCostsVo.class));
		List<HospitalizationMedicalCostsVo> list= queryObject.list();
		return list;
	}
	
	
//	@Override
//	public List<HospitalizationMedicalCostsVo> querytStatData(String timeString,String type,String nameString) {
//		final StringBuffer sqlNew=new StringBuffer();
//		
//		sqlNew.append("select dept.dept_name as deptName,count(dept.dept_code) as passengers  ");
//		sqlNew.append(" from t_INPATIENT_INFO t  left join T_DEPARTMENT dept on dept.dept_id=t.dept_code where t.in_state='I' and dept.dept_type = 'I'");
//		if(nameString!=null&&!nameString.equals("")){
//			sqlNew.append(" and dept.dept_ID in("+nameString+")");
//		}
//		if(type.equals("1")){//同比
//			sqlNew.append(" and to_char(t.IN_DATE,'yyyy')='"+timeString+"' ");
//		}else if(type.equals("2")){//环比
//			sqlNew.append("and to_char(t.IN_DATE,'mm') ='"+timeString+"' ");	
//		}
//		sqlNew.append(" group by dept.dept_name");
//		List<HospitalizationMedicalCostsVo> list = (List<HospitalizationMedicalCostsVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
//			@Override
//			public Object doInHibernate(Session session) throws HibernateException,SQLException {
//				SQLQuery queryObject = session.createSQLQuery(sqlNew.toString())
//						.addScalar("deptName").addScalar("passengers",Hibernate.INTEGER);
//				return queryObject.setResultTransformer(Transformers.aliasToBean(HospitalizationMedicalCostsVo.class)).list();
//			}
//		});
//		return list;
//	}


	@Override
	public List<SysDepartment> queryAllDept() {
		//以后会将dept_id更改为dept_code
		final String sql="select bd.DEPT_code as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd where bd.dept_type = 'I'";
		List<SysDepartment> bdl = (List<SysDepartment>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql).addScalar("deptCode").addScalar("deptName");
				return queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
			}
		});
		if(bdl!=null&&bdl.size()>0){
			SysDepartment bd=new SysDepartment();
			bd.setDeptCode("1");
			bd.setDeptName("全部");
			bdl.add(0, bd);
			return bdl;
		}
		return new ArrayList<SysDepartment>();
	}

	
}