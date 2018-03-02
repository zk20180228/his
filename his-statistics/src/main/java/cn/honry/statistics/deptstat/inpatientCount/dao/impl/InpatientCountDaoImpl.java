package cn.honry.statistics.deptstat.inpatientCount.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.antimicrobialDrugAccess.vo.AntimicrobialDrugAccessVo;
import cn.honry.statistics.deptstat.inpatientCount.dao.InpatientCountDao;
@Repository("inpatientCountDao")
@SuppressWarnings({"all"})
public class InpatientCountDaoImpl extends HibernateEntityDao<InpatientInfoNow> implements InpatientCountDao{

	        // 为父类HibernateDaoSupport注入sessionFactory的值
			@Resource(name = "sessionFactory")
			public void setSuperSessionFactory(SessionFactory sessionFactory) {
				super.setSessionFactory(sessionFactory);
			}
			@Resource
			private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
			@Override
			public List<InpatientInfoNow> queryInpatientCountList(String dept,String page,String rows) {
				if(StringUtils.isBlank(page)){
					page="1";
				}
				if(StringUtils.isBlank(rows)){
					rows="20";
				}
				Integer p=Integer.parseInt(page);
				Integer r=Integer.parseInt(rows);
				Integer s=(p-1)*r+1;
				Integer e=s+r-1;
				String sql="select * from (select t.MEDICALRECORD_ID inpatientNo,t.patient_name patientName,DECODE(t.report_sex ,1,'男','女')  reportSexName,"
						+ "t.report_age reportAge,t.in_date inDate,t.dept_name deptName,t.bed_name bedName,t.charge_doc_name  chargeDocName,"
						+ "t.chief_doc_name  chiefDocName,t.report_ageunit reportAgeunit,rownum rn from t_inpatient_info_now t where t.in_state in"
						+ "('R','I','B','P','N','C') and t.del_flg=0 and t.stop_flg=0 ";
				if(StringUtils.isNotBlank(dept)){
					sql+="  and t.dept_code in ("+dept+")";
				}
				sql+="  ) m where rn between  "+s+"  and  "+e;
				List list = this.getSession().createSQLQuery(sql).addScalar("inpatientNo").addScalar("patientName").addScalar("reportSexName").addScalar("reportAge",Hibernate.INTEGER)
				.addScalar("reportAgeunit").addScalar("chargeDocName").addScalar("chiefDocName").addScalar("inDate",Hibernate.TIMESTAMP).addScalar("deptName").addScalar("bedName")
				.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
				return list;
			}
			@Override
			public int queryTotal(String dept) {
				String sql="select count(1)  feeInterval from t_inpatient_info_now t  where t.in_state in ('R', 'I', 'B', 'P', 'N', 'C')  and t.del_flg = 0  and t.stop_flg = 0 ";
				if(StringUtils.isNotBlank(dept)){
					sql+="  and t.dept_code in ("+dept+")";
				}
				InpatientInfoNow inpatientInfoNow = (InpatientInfoNow) this.getSession().createSQLQuery(sql).addScalar("feeInterval",Hibernate.INTEGER).setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class))
				.uniqueResult();
				if(inpatientInfoNow!=null&&inpatientInfoNow.getFeeInterval()!=null){
					return inpatientInfoNow.getFeeInterval();
				}
				return 0;
			}
}
