package cn.honry.finance.outpatientAccount.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.outpatientAccount.dao.PatientAccountDAO;

/**
 * 账户
 * @author  wangfujun
 * @date 创建时间：2016年3月30日 下午5:01:33
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Repository("patientAccountDAO")
@SuppressWarnings({"all"})
public class PatientAccountDAOImpl extends HibernateEntityDao<OutpatientAccount> implements PatientAccountDAO {
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void updateAccount(OutpatientAccount account, String menuAlias) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("update T_OUTPATIENT_ACCOUNT t set t.ACCOUNT_STATE = ?");
		sBuffer.append(" ,t.updateUser = ?");
		sBuffer.append(" ,t.updateTime = ?");
		if(!StringUtils.isEmpty(account.getIdcardId())){
			sBuffer.append(" ,t.IDCARD_ID = '"+account.getIdcardId()+"'");
		}
		sBuffer.append(" where t.ACCOUNT_ID = ?");
		SQLQuery query =  (SQLQuery) this.getSession().createSQLQuery(sBuffer.toString())
				.setParameter(0, account.getAccountState())
				.setParameter(1, account.getUpdateUser())
				.setParameter(2, account.getUpdateTime())
				.setParameter(3, account.getId());
				
		query.executeUpdate();
	}

	@Override
	public OutpatientAccount get(String arg0) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientAccount t");
		sBuffer.append(" where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.id = ?");
		List<OutpatientAccount> list = super.find(sBuffer.toString(), arg0);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Patient getForblh(String blhString) {
		String hql = "from Patient t where t.stop_flg = 0 and t.del_flg = 0 and  t.medicalrecordId = ?";
		List<Patient> list = super.find(hql, blhString);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public PatientIdcard getForPatientId(String patientid) {
		String hql = "from PatientIdcard t where t.stop_flg = 0 and t.del_flg = 0 and t.patient.id = ?";
		List<PatientIdcard> list = super.find(hql, patientid);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public OutpatientAccount getAccounForblh(String blhString) {
		String hql = "from OutpatientAccount t where t.stop_flg = 0 and t.del_flg = 0 and t.medicalrecordId = ?";
		List<OutpatientAccount> list = super.find(hql, blhString);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
}
