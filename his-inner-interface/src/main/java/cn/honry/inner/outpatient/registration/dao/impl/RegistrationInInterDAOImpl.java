package cn.honry.inner.outpatient.registration.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.registration.dao.RegistrationInInterDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("registrationInInterDAO")
@SuppressWarnings({ "all" })
public class RegistrationInInterDAOImpl extends HibernateEntityDao<RegistrationNow> implements RegistrationInInterDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<RegistrationNow> registerList(String deptCode, String no,
			String sTime, String eTime, String parameter, String rows, String page) {
		String hql = "FROM RegistrationNow r WHERE r.del_flg=0 and r.stop_flg=0";
		hql = joinReg(hql, deptCode, no, sTime, eTime, parameter);
		hql += " ORDER BY r.regDate DESC";
		return super.getPage(hql, page, rows);
	}

	@Override
	public int registerTotal(String deptCode, String no, String sTime,
			String eTime, String parameter) {
		String hql = "FROM RegistrationNow r WHERE r.del_flg=0 and r.stop_flg=0";
		hql = joinReg(hql, deptCode, no, sTime, eTime, parameter);
		return super.getTotal(hql);
	}
	
	/** 门诊条件拼接
	* @Title: joinReg 门诊条件拼接
	* @Description: 门诊条件拼接
	* @author dtl 
	* @date 2016年11月10日
	*/
	private String joinReg(String hql, String deptCode, String no,
			String sTime, String eTime, String parameter) {
		if(StringUtils.isNotBlank(deptCode)){
			hql += " AND r.deptCode = '" + deptCode + "'";
		}
		if(StringUtils.isNotBlank(no)){
			hql += " AND r.clinicCode ='" + no + "'";
		}
		if(StringUtils.isNotBlank(sTime)){
			hql = hql+" AND r.regDate >= TO_DATE('" + sTime + "', 'YYYY-MM-DD')";
		}
		if(StringUtils.isNotBlank(eTime)){
			hql = hql+" AND r.regDate <= TO_DATE('" + eTime + "', 'YYYY-MM-DD')";
		}
		if(StringUtils.isNotBlank(parameter)){
			hql = hql+" AND r.regDate >= SYSDATE-" + parameter;
		}
		hql = hql+" AND r.inState = 0";
		return hql;
	}

	@Override
	public void saveDocSource(RegisterDocSource info) {
		this.getSession().saveOrUpdate(info);
	}

	@Override
	public RegisterDocSource getDocSourceId(String scheduleId) {
		String sql="select t.ID as id,t.CLINIC_SUM as clinicSum,t.version as version,t.preclinic_sum as preclinicSum,t.preregister_sum as preregisterSum from t_register_doc_source t where t.SCHEDULE_ID=:scheduleId";
		SQLQuery sqlQuery=this.getSession().createSQLQuery(sql);
		sqlQuery.addScalar("id").addScalar("clinicSum",Hibernate.INTEGER).addScalar("version", Hibernate.INTEGER)
		.addScalar("preclinicSum",Hibernate.INTEGER).addScalar("preregisterSum",Hibernate.INTEGER);
		sqlQuery.setParameter("scheduleId",scheduleId);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(RegisterDocSource.class));
		List<RegisterDocSource> list= sqlQuery.list();
		if(list.size()==0||list==null){
			return new RegisterDocSource();
		}else{
			return list.get(0);
		}
	}

	@Override
	public RegistrationNow getRegistration(String clinicCode) {
		StringBuffer buf =new StringBuffer();
		buf.append(" from RegistrationNow where del_flg=0 and stop_flg=0");
		if(StringUtils.isNotBlank(clinicCode)){
			buf.append(" and clinicCode=?");
		}
		List<RegistrationNow> reg=super.find(buf.toString(), clinicCode);
		if(reg!=null&&reg.size()>0){
			return reg.get(0);
		}
		return null;
	}

	@Override
	public void delDocSource(String scheduleId) {
		String acc=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql="update t_register_doc_source t set t.del_flg=1 ,t.DELETEUSER='"+acc+"',t.DELETETIME=sysdate where  t.SCHEDULE_ID='"+scheduleId+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

}

