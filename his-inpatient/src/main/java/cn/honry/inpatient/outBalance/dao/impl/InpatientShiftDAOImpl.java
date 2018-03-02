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

import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientShiftDAO;

@Repository("inpatientShiftDAO")
@SuppressWarnings("all")
public class InpatientShiftDAOImpl extends HibernateEntityDao<InpatientShiftData> implements InpatientShiftDAO{
	
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
	public void updateInpatientShiftData(final InpatientShiftData inpatientShiftData) {
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
			      ps.setString(1, inpatientShiftData.getClinicNo());//住院流水号
			      ps.setString(2, inpatientShiftData.getShiftType());//变更类型
			      ps.setInt(3, inpatientShiftData.getHappenNo());//发生序号
			   }
			});
	}
}
