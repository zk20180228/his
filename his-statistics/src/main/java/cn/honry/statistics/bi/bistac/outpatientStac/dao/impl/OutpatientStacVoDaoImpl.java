package cn.honry.statistics.bi.bistac.outpatientStac.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.outpatientStac.dao.OutpatientStacVoDao;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.BusinessContractunitVo;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;

@Repository("outpatientStacVoDao")
@SuppressWarnings({ "all" })
public class OutpatientStacVoDaoImpl extends HibernateEntityDao<OutpatientStacVo> implements OutpatientStacVoDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 * 
	 * 查询本日门诊量\本日门诊急诊量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryRegistrationVo() {
		String sdate=(new SimpleDateFormat("yyyy-MM-dd")).format(new Date()); 
		sdate = sdate+" 00:00:00";
		final String sql="select count(1) as countRegistration from T_REGISTER_MAIN_NOW t where t.in_state = 0 and t.valid_flag = 1 and "
				+ "to_char(t.reg_date,'yyyy-MM-dd HH:mm:ss') >= '"+sdate+"'";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("countRegistration",Hibernate.INTEGER);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	

	/**  
	 * 
	 * 查询当日手术例数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryOperationApplyVo() {
		final String sql="select (select count(t.op_id) from T_OPERATION_APPLY t where to_char(t.pre_date,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') and t.stop_flg=0 and t.del_flg=0 and t.pasource=2 and t.status=4) as dayOperationApply, "
				+ "(select count(t.op_id) from T_OPERATION_APPLY t where t.pasource=2 and t.status=4 and t.stop_flg=0 and t.del_flg=0) as operationApplys from dual";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("dayOperationApply",Hibernate.INTEGER).addScalar("operationApplys",Hibernate.INTEGER);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 * 
	 * 查询当前在院人数\当前出院人数\新增住院人数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryInpatientInfoNowVo() {
		final String sql="select (select count(t.inpatient_id) from T_INPATIENT_INFO_NOW t where t.in_state='I' and t.stop_flg=0 and t.del_flg=0) as inpatientInfoNow, "
				+ "(select count(t.inpatient_id) from T_INPATIENT_INFO_NOW t where t.in_state='O' and t.stop_flg=0 and t.del_flg=0) as inpatientInfoNowGo, "
				+ "(select count(t.inpatient_id) from T_INPATIENT_INFO_NOW t where t.in_state='I'and to_char(t.in_date,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') and t.stop_flg=0 and t.del_flg=0 ) as newInpatientInfoNow from dual";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("inpatientInfoNow",Hibernate.INTEGER).addScalar("inpatientInfoNowGo",Hibernate.INTEGER).addScalar("newInpatientInfoNow",Hibernate.INTEGER);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 * 
	 * 查询住院核定床位
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryBusinessHospitalbedVo() {
		final String sql="select (select count(t.bed_id) from T_BUSINESS_HOSPITALBED t where t.bed_organ='4' and t.hospital_id=1 and t.stop_flg=0 and t.del_flg=0 ) as businessHospitalbed, "
				+ "(select count(t.bed_id) from T_BUSINESS_HOSPITALBED t where t.hospital_id=1 and t.stop_flg=0 and t.del_flg=0) as businessHospitalbedTotals from dual";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("businessHospitalbed",Hibernate.INTEGER).addScalar("businessHospitalbedTotals",Hibernate.INTEGER);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 * 
	 * 查询当日门诊实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoD() {
		String sdate=(new SimpleDateFormat("yyyy-MM-dd")).format(new Date()); 
		sdate = sdate+" 00:00:00";
		final String sql="select sum(t.tot_cost) as dayMCost from T_OUTPATIENT_FEEDETAIL_NOW t where t.CANCEL_FLAG = 1 and t.trans_type = 1 and to_date('"+sdate+"','yyyy-MM-dd HH24:mi:ss') <= t.fee_date";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("dayMCost",Hibernate.DOUBLE);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		if (vo.getDayMCost()==null) {
			vo.setDayMCost(0.0);
		}
		return vo;
	}
	/**  
	 * 
	 * 查询当月门诊实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoM(List<String> tnL,final String stime,final String etime) {
		if(tnL==null||tnL.size()<0){
			return new OutpatientStacVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select sum(monthMCost) AS monthMCost  from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
			sb.append(" select sum(t.tot_cost) AS monthMCost  from ").append(tnL.get(i)).append(" t ").append(" where trunc(t.fee_date,'dd') >= to_date(:stime,'yyyy-MM-dd') ");;
			sb.append(" and trunc(t.fee_date,'dd') <= to_date(:etime,'yyyy-MM-dd') and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 and t.trans_type = 1 ");
		}
		sb.append(" ) ");
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("monthMCost",Hibernate.DOUBLE);
				queryObject.setParameter("etime", etime);
				queryObject.setParameter("stime", stime);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		if (vo.getMonthMCost()==null) {
			vo.setMonthMCost(0.0);
		}
		return vo;
	}
	/**  
	 * 
	 * 查询当年门诊实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryOutpatientFeedetailNowCostVoY(List<String> tnL,final String stime,final String etime) {
		if(tnL==null||tnL.size()<0){
			return new OutpatientStacVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select sum(yearMCost) AS yearMCost  from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
			sb.append(" select sum(t.tot_cost) AS yearMCost  from ").append(tnL.get(i)).append(" t ").append(" where trunc(t.fee_date,'dd') >= to_date(:stime,'yyyy-MM-dd') ");;
			sb.append(" and trunc(t.fee_date,'dd') <= to_date(:etime,'yyyy-MM-dd') and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 and t.trans_type = 1 ");
		}
		sb.append(" ) ");
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("yearMCost",Hibernate.DOUBLE);
				queryObject.setParameter("etime", etime);
				queryObject.setParameter("stime", stime);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		if (vo.getYearMCost()==null) {
			vo.setYearMCost(0.0);
		}
		return vo;
	}
	/**  
	 * 
	 * 查询当日住院实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryInpatientFeeInfoNowCostD() {
		final String sql="select(select sum(t.tot_cost) from T_INPATIENT_FEEINFO_NOW t where trunc(t.balance_date,'dd') = trunc(sysdate,'dd') and t.stop_flg=0 and t.del_flg=0 and t.trans_type=1) as dayZCost from dual";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("dayZCost",Hibernate.DOUBLE);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		if (vo.getDayZCost()==null) {
			vo.setDayZCost(0.0);
		}
		return vo;
	}
	/**  
	 * 
	 * 查询当月住院实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryInpatientFeeInfoNowCostM(List<String> tnL,final String stime,final String etime) {
		if(tnL==null||tnL.size()<0){
			return new OutpatientStacVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select sum(monthZCost) AS monthZCost  from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
			sb.append(" select sum(t.tot_cost) AS monthZCost  from ").append(tnL.get(i)).append(" t ").append(" where trunc(t.balance_date,'dd') >= to_date(:stime,'yyyy-MM-dd') ");;
			sb.append(" and trunc(t.balance_date,'dd') <= to_date(:etime,'yyyy-MM-dd') and t.stop_flg = 0 and t.del_flg = 0 and t.trans_type = 1 ");
		}
		sb.append(" ) ");
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("monthZCost",Hibernate.DOUBLE);
				queryObject.setParameter("etime", etime);
				queryObject.setParameter("stime", stime);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		if (vo.getMonthZCost()==null) {
			vo.setMonthZCost(0.0);
		}
		return vo;
	}
	/**  
	 * 
	 * 查询当年住院实收
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月24日 下午8:17:05 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月24日 下午8:17:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	@Override
	public OutpatientStacVo queryInpatientFeeInfoNowCostY(List<String> tnL,final String stime,final String etime) {
		if(tnL==null||tnL.size()<0){
			return new OutpatientStacVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select sum(yearZCost) AS yearZCost  from ( ");
		for (int i = 0; i < tnL.size(); i++) {
		if(i!=0){
			sb.append(" union all ");
		}
			sb.append(" select sum(t.tot_cost) AS yearZCost  from ").append(tnL.get(i)).append(" t ").append(" where trunc(t.balance_date,'dd') >= to_date(:stime,'yyyy-MM-dd') ");;
			sb.append(" and trunc(t.balance_date,'dd') <= to_date(:etime,'yyyy-MM-dd') and t.stop_flg=0 and t.del_flg=0 and t.trans_type=1 ");
		}
		sb.append(" ) ");
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
				queryObject.addScalar("yearZCost",Hibernate.DOUBLE);
				queryObject.setParameter("etime", etime);
				queryObject.setParameter("stime", stime);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		if (vo.getYearZCost()==null) {
			vo.setYearZCost(0.0);
		}
		return vo;
	}

	@Override
	public OutpatientStacVo findMaxMin() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime ,MIN(mn.REG_DATE) AS sTime FROM t_outpatient_feedetail_now mn";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		return vo;
	}

	@Override
	public OutpatientStacVo findMaxMinZ() {
		final String sql = "SELECT MAX(mn.CREATETIME) AS eTime ,MIN(mn.CREATETIME) AS sTime FROM t_inpatient_feeinfo_now mn";
		OutpatientStacVo vo = (OutpatientStacVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientStacVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (OutpatientStacVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientStacVo.class)).uniqueResult();
			}
		});
		return vo;
	}

	@Override
	public List<BusinessContractunitVo> queryBusinessContractunit() {
//		String sql1="select t.unit_code as encode,t.unit_name as name from T_BUSINESS_CONTRACTUNIT t";
		String sql = "	select t.unit_code as encode,t.unit_name as name, nvl(sum(decode(r.emergency_flag,1,1,0)),0) as jiNum, nvl(sum(r.pact_code),0) as mzNum from T_BUSINESS_CONTRACTUNIT t left join t_register_main_now r on t.unit_code=r.pact_code and to_char(r.reg_date, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd') and  r.valid_flag = 1 and r.in_state = 0 and r.stop_flg = 0 and r.del_flg = 0 group by t.unit_code,t.unit_name";
		List<BusinessContractunitVo> result =  jdbcTemplate.query(sql,new RowMapper<BusinessContractunitVo>() {
			@Override
			public BusinessContractunitVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessContractunitVo bc = new BusinessContractunitVo();
				bc.setEncode(rs.getString("encode"));
				bc.setName(rs.getString("name"));
				bc.setJiNum(rs.getInt("jiNum"));
				bc.setMzNum(rs.getInt("mzNum"));
				return bc;
			}});
		return result; 
	}
	@Override
	public List<BusinessContractunitVo> queryInOutNum() {
		String sql = "select t.unit_code as encode,t.unit_name as name, nvl(sum(decode(r.in_state,'O',1,0)),0) as jiNum, nvl(sum(decode(r.in_state,'I',1,0)),0) as mzNum from T_BUSINESS_CONTRACTUNIT t left join t_inpatient_info_now r on t.unit_code=r.pact_code and to_char(r.in_date, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd') and r.stop_flg = 0 and r.del_flg = 0 group by t.unit_code,t.unit_name";
		List<BusinessContractunitVo> result =  jdbcTemplate.query(sql,new RowMapper<BusinessContractunitVo>() {
			@Override
			public BusinessContractunitVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessContractunitVo bc = new BusinessContractunitVo();
				bc.setEncode(rs.getString("encode"));
				bc.setName(rs.getString("name"));
				bc.setJiNum(rs.getInt("jiNum"));
				bc.setMzNum(rs.getInt("mzNum"));
				return bc;
			}});
		return result; 
	}


	@Override
	public int queryBusinessContractunitTotal(String encode) {
		String sql="select count(t.id) from t_register_main_now t where t.pact_code='"+encode+"' and to_char(t.reg_date,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') and t.in_state=0 and t.stop_flg=0 and t.del_flg=0 and t.valid_flag=1";
		return namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sql, java.lang.Integer.class);
	}


	@Override
	public int queryBusinessContractunitTotalJi(String encode) {
		String sql="select count(t.id) from t_register_main_now t where t.pact_code='"+encode+"' and to_char(t.reg_date,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') and t.emergency_flag=1 and t.valid_flag=1 and t.in_state=0 and t.stop_flg=0 and t.del_flg=0";
		return namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sql, java.lang.Integer.class);
	}


	@Override
	public int queryBusinessContractunitTotalGo(String encode) {
		String sql="select count(t.inpatient_id) from t_inpatient_info_now t where t.pact_code='"+encode+"' and to_char(t.in_date,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') and t.in_state='O' and t.stop_flg=0 and t.del_flg=0";
		return namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sql, java.lang.Integer.class);
	}


	@Override
	public int queryBusinessContractunitTotalNew(String encode) {
		String sql="select count(t.inpatient_id) from t_inpatient_info_now t where t.pact_code='"+encode+"' and t.in_state='I' and to_char(t.in_date,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') and t.stop_flg=0 and t.del_flg=0";
		return namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sql, java.lang.Integer.class);
	}
}
