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
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceHeadDAO;
import cn.honry.utils.ShiroSessionUtils;

@Repository("inpatientBalanceHeadDAO")
@SuppressWarnings("all")
public class InpatientBalanceHeadDAOImpl extends HibernateEntityDao<InpatientBalanceHead> implements InpatientBalanceHeadDAO{
	
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
	public void updateHead(final InpatientBalanceHeadNow head) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
				  User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			      ps.setString(1, head.getId());
			      ps.setString(2, head.getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
			      ps.setString(3, head.getInvoiceNo());//发票号码
			      ps.setInt(4, head.getTransType());//交易类型 
			      ps.setString(5, head.getInpatientNo());//流水号
			      ps.setString(6, head.getName());//姓名
			      ps.setInt(7, head.getBalanceNo());//结算序号
			      ps.setString(8, head.getPactCode());//合同代码
			      ps.setDouble(9, head.getPrepayCost());//预交金
			      ps.setDouble(10, head.getChangePrepaycost());//转入预交金
			      ps.setDouble(11, head.getTotCost());//费用金额
			      ps.setDouble(12, head.getOwnCost());//自费金额
			      ps.setDouble(13, head.getPayCost());//自付金额
			      ps.setDouble(14, head.getPubCost());//公费金额
			      ps.setDouble(15, head.getEcoCost());//优惠金额
			      ps.setDouble(16, head.getDerCost());//减免金额
			      ps.setInt(17, head.getWasteFlag());//扩展标志1
			      ps.setDouble(18, head.getSupplyCost());//补收金额
			      ps.setDouble(19, head.getReturnCost());//返还金额
			      ps.setDate(20, (java.sql.Date) head.getBeginDate());//起始日期
			      ps.setDate(21, (java.sql.Date) head.getEndDate());//终止日期
			      ps.setInt(22, head.getBalanceType());//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
			      ps.setString(23, head.getBalanceOpercode());//结算人代码
			      ps.setDate(24, (java.sql.Date) head.getBalanceDate());//结算时间
			      ps.setString(25, head.getBalanceoperDeptcode());//结算员科室
			      ps.setInt(26, head.getCheckFlag());//0未核查/1已核查
			      ps.setString(27, head.getCreateUser());
			      ps.setDate(28, (java.sql.Date) new Date(head.getCreateTime().getTime()));
			      ps.setString(29, head.getCreateUser());
			      ps.setDate(30, (java.sql.Date) new Date(head.getUpdateTime().getTime()));
			   }
		});
	}
}
