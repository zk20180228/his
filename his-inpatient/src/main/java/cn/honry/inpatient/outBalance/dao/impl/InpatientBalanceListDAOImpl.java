package cn.honry.inpatient.outBalance.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceHeadDAO;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceListDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("inpatientBalanceListDAO")
@SuppressWarnings("all")
public class InpatientBalanceListDAOImpl extends HibernateEntityDao<InpatientBalanceList> implements InpatientBalanceListDAO{
	
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
	public void updateInpatientFeeInfoNow(final InpatientBalanceListNow inpatientBalanceList) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
				  User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
				  ps.setString(1, inpatientBalanceList.getInvoiceNo());//发票号码
				  ps.setInt(2, inpatientBalanceList.getTransType());//交易类型 ,1正交易，2反交易
				  ps.setString(3, inpatientBalanceList.getName());//姓名
				  ps.setString(4, inpatientBalanceList.getInpatientNo());//流水号
			      ps.setString(5, inpatientBalanceList.getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
			      ps.setString(6, inpatientBalanceList.getPactCode());//合同单位
			      ps.setString(7, inpatientBalanceList.getDeptCode());//在院科室
			      ps.setString(8, inpatientBalanceList.getStatCode());//统计大类
			      ps.setString(9, inpatientBalanceList.getStatName());//统计大类名称
			      ps.setDouble(10, inpatientBalanceList.getTotCost());//费用金额
			      ps.setDouble(11, inpatientBalanceList.getOwnCost());//自费金额
			      ps.setDouble(12, inpatientBalanceList.getPayCost());//自付金额
			      ps.setDouble(13, inpatientBalanceList.getPubCost());//公费金额
			      ps.setDouble(14, inpatientBalanceList.getEcoCost());//优惠金额
			      ps.setString(15, inpatientBalanceList.getBalanceOpercode());//结算人代码
			      ps.setDate(16, (java.sql.Date) inpatientBalanceList.getBalanceDate());//结算时间
			      ps.setInt(17, inpatientBalanceList.getBalanceType());//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
			      ps.setInt(18, inpatientBalanceList.getBalanceNo());//结算序号
			      ps.setString(19, inpatientBalanceList.getBalanceoperDeptcode());//结算员科室
			      ps.setString(20, inpatientBalanceList.getCreateUser());
			      ps.setDate(21, (java.sql.Date) new Date(inpatientBalanceList.getCreateTime().getTime()));
			      ps.setString(22, inpatientBalanceList.getCreateUser());
			      ps.setDate(23, (java.sql.Date) new Date(inpatientBalanceList.getUpdateTime().getTime()));
			   }
		});
	}
}
