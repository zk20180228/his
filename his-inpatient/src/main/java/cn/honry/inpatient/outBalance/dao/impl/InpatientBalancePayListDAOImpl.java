package cn.honry.inpatient.outBalance.dao.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientBalancePayListDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("inpatientBalancePayListDAO")
@SuppressWarnings("all")
public class InpatientBalancePayListDAOImpl extends HibernateEntityDao<InpatientBalancePay> implements InpatientBalancePayListDAO{
	
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;

	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public int updateInpatientBalancePayNow(final InpatientBalancePayNow entity) {
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
			      ps.setString(1, entity.getId());
			      ps.setString(2, entity.getInvoiceNo());
			      ps.setInt(3, entity.getTransType());
			      ps.setInt(4, entity.getTransKind());
			      ps.setInt(5, entity.getBalanceNo());
			      ps.setInt(6, entity.getReutrnorsupplyFlag());
			      ps.setString(7, entity.getBalanceOpercode());
			      ps.setDate(8, new Date(entity.getBalanceDate().getTime()));
			      ps.setString(9, entity.getCreateUser());
			      ps.setDate(10, new Date(entity.getCreateTime().getTime()));
			      ps.setString(11, entity.getUpdateUser());
			      ps.setDate(12, new Date(entity.getUpdateTime().getTime()));
			   }
			});
		return t;
	}
}
