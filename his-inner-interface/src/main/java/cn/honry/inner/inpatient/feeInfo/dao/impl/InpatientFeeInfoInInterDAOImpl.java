package cn.honry.inner.inpatient.feeInfo.dao.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.feeInfo.dao.InpatientFeeInfoInInterDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("inpatientFeeInfoInInterDAO")
@SuppressWarnings({"all"})
public class InpatientFeeInfoInInterDAOImpl extends HibernateEntityDao<InpatientFeeInfoNow> implements InpatientFeeInfoInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;

	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public void updateInpatientFeeInfoNow(final InpatientFeeInfoNow fee) {
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
			      ps.setInt(1, fee.getBalanceState());//结算标志 0:未结算；1:已结算 2:已结转   
			      ps.setInt(2, fee.getBalanceNo());//结算序号
			      ps.setDate(3, (Date) fee.getBalanceDate());//结算时间
			      ps.setString(4, fee.getCreateUser());//结算操作人
			      ps.setString(5, fee.getInvoiceNo());//结算发票号   
			   }
			});
	}
}
