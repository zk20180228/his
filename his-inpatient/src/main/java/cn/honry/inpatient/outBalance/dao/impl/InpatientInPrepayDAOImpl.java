package cn.honry.inpatient.outBalance.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientChangeprepay;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientInPrepayDAO;
import cn.honry.utils.HisParameters;

@Repository("inpatientInPrepayDAO")
@SuppressWarnings("all")
public class InpatientInPrepayDAOImpl extends HibernateEntityDao<InpatientInPrepay> implements InpatientInPrepayDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientInPrepay> queryprepayCost(String inpatientNo,String outDate,String inDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(t.prepay_cost) as prepaycost from t_inpatient_inprepay t "
				+" where (t.inpatient_no = '"+inpatientNo+"') "
			    +" and t.stop_flg=0 and t.del_flg=0 and t.balance_state=0");
		if(inDate!=null&&inDate!=""){
			if(outDate!=null&&outDate!=""){
				sql.append("and (to_char(t.createtime,'yyyy-MM-dd hh:mm:ss')<'"+outDate+"') "); 
				sql.append("and (to_char(t.createtime,'yyyy-MM-dd hh:mm:ss')>'"+inDate+"') ");
			}
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("prepayCost",Hibernate.DOUBLE);
		List<InpatientInPrepay> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInPrepay.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientInPrepay>();
	}

	@Override
	public List<InpatientChangeprepay> queryInpatientChangeprepay(
			String inpatientNo, String outDate, String inDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(t.prepay_cost) as prepaycost from t_inpatient_changeprepay t "
				+" where (t.clinic_no = '"+inpatientNo+"') "
			    +" and t.stop_flg=0 and t.del_flg=0 and t.balance_state=0");
		if(inDate!=null&&inDate!=""){
			if(outDate!=null&&outDate!=""){
				sql.append("and (to_char(t.createtime,'yyyy-MM-dd hh:mm:ss')<'"+outDate+"') "); 
				sql.append("and (to_char(t.createtime,'yyyy-MM-dd hh:mm:ss')>'"+inDate+"') ");
			}
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("prepayCost",Hibernate.DOUBLE);
		List<InpatientChangeprepay> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientChangeprepay.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientChangeprepay>();
	}
}
