package cn.honry.oa.wages.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisClusterCommands.AddSlots;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.bean.model.OaWages;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.adviceToSystemclass.dao.AdviceTScInInterDao;
import cn.honry.inner.baseinfo.adviceToSystemclass.vo.CodeSystemtypeVo;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.inpatient.doctorDrugGrade.dao.DoctorDrugGradeInInterDAO;
import cn.honry.inner.inpatient.kind.dao.InpatientKindInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.oa.wages.dao.WagesDAO;
import cn.honry.oa.wages.vo.AESUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

/** 
* @Description: 工资管理DaoImpl
* @author zx
* @date 2017年7月17日
*  
*/
@Repository("wagesDAO")
@SuppressWarnings({ "all" })
public class WagesDAOImpl  extends HibernateEntityDao<OaWages> implements WagesDAO{
	private  String key ="123";
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/** 
	* @Description: 工资账号是否存在
	* @param weagesAccount 工资账号
	* @throws Exception
	* @return List<SysEmployee>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public List<SysEmployee> isExistWadges(String weagesAccount) throws Exception{
		String sql = "select t.WAGES_ACCOUNT as wagesAccount from T_EMPLOYEE t where t.WAGES_ACCOUNT = :account  and t.STOP_FLG=0 and t.DEL_FLG=0";
		List<SysEmployee> list =this.getSession().createSQLQuery(sql).addScalar("wagesAccount").setParameter("account", weagesAccount).setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<SysEmployee>();
	}
	/** 
	* @Description: 修改工资查询密码 
	* @param weagesAccount 工资账号
	* @param password 密码
	* @throws Exception
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public String initPasswordToWeages(String weagesAccount, String password) throws Exception{
		String userAccount = "";
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();// 当前用户
		if (user != null && StringUtils.isNotBlank(user.getAccount())) {
			userAccount = user.getAccount();
		}
		String sql = "update T_EMPLOYEE set WAGES_PASSWORD = ?,UPDATEUSER=?,UPDATETIME=? where EMPLOYEE_JOBNO=? and stop_flg = 0 and del_flg=0";  
		Object args[] = new Object[]{password,userAccount,new Date(),weagesAccount};  
		int t = jdbcTemplate.update(sql,args); 
		if(t>0){
			return "success";
		}else{
			return "error";
		}
	}
	/** 
	* @Description: 查询原密码 
	* @param weagesAccount 查询账号
	* @param weagesPassword 密码
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee checkAccount(String weagesAccount, String weagesPassword) throws Exception{
		String sql = "select t.WAGES_ACCOUNT as wagesAccount,t.WAGES_PASSWORD as wagesPassword from T_EMPLOYEE t where t.WAGES_ACCOUNT = :account  "
				+ "and t.WAGES_PASSWORD=:password and t.stop_flg = 0 and t.del_flg=0";
		SQLQuery queryObject = (SQLQuery) this.getSession().createSQLQuery(sql)
				.addScalar("wagesAccount")
				.addScalar("wagesPassword")
				.setParameter("account", weagesAccount).setParameter("password", weagesPassword);
		List<SysEmployee> employee =queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(employee!=null&&employee.size()>0){
			return employee.get(0);
		}
		return new SysEmployee();
	}

	/** 
	* @Description: 查询工资数据
	* @param wageAccount 工资账号
	* @param name 身份证号
	* @param wagesTime 查询时间
	* @param page
	* @param rows
	* @throws Exception
	* @return List<OaWages>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public Map<String, Object> listWagesQuery(String wageAccount, String name, String wagesTime, String page, String rows) throws Exception{
		StringBuffer sb = new StringBuffer();
		Map<String,Object> retMap = new HashMap<String, Object>();
		int total = 0;
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("start", (start - 1) * count);
		paraMap.put("end", start * count);
		sb.append("select ROWNUM n,");
		sb.append("t.WAGES_ACCOUNT as wagesAccount,t.WAGES_NAME as name,t.DEPTNAME as deptName,t.CATEGORY as category,t.POSTPAY as postPay,");
		sb.append("t.BASEPAY as basePay,t.NURSINTEACH as nursinTeach,t.ACHIEVEMENTS as achievements,");
		sb.append("t.NURSINTEACHING as nursinTeaching,t.KEEPTHINK as keepThink,t.HEALTH_ALLOWANCE as healthAllowance,");
		sb.append("t.ONLYCHILDFEE as onlyChildFee,t.HYGIENEFEE as hygieneFee,t.PHDFEE as PHDFee,t.SUBSIDYFEE as subsidyFee,t.INCREASED as increased,");
		sb.append("t.INCREASING as increasing,t.TOTALSHOULD as totalShould,t.DEDUCTRENT as deductRent,t.HOUSINGFUND as housingFund,t.BOARDINGFEE as boardingFee,t.MEDICAL_INSURANCE as medicalInsurance,");
		sb.append("t.OVERALL_PLANNING as overallPlanning,t.UNEMPLOYMENT_INSURANCE as unemploymentInsurance,t.DEDUCTWAGES as deductWages,");
		sb.append("t.HEATINGCOSTS as heatingCosts,t.ACCOUNT_EECEIVABLE as accountEeceivable,t.TOTALACTUAL as totalActual,t.PROVIDENTFUNDACCOUNT as providentFundAccount,");
		sb.append("t.IDCARD  as IDCard,t.WAGES_TIME as wagesTime ");
		sb.append(" from t_oa_wages t where t.DEL_FLG=0 and t.STOP_FLG=0 ");
		if(StringUtils.isNotBlank(wageAccount)){
			sb.append(" and  t.WAGES_ACCOUNT='"+wageAccount+"'");
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" and t.IDCARD = '"+name+"'");
		}
		if(StringUtils.isNotBlank(wagesTime)){
			Date date1 = DateUtils.parseDateY_M(wagesTime);
			String sdate = DateUtils.formatDateY_M(date1);//当前月
			Date nextDate = DateUtils.addMonth(date1, 1);//下一月
			String edate = DateUtils.formatDateY_M(nextDate);
			sb.append(" and t.WAGES_TIME >=to_date('"+(wagesTime+"-01")+"', 'yyyy-MM-dd hh24:mi:ss') and t.WAGES_TIME<to_date('"+(edate+"-01")+"', 'yyyy-MM-dd hh24:mi:ss')");
		}
		sb.append("   order by t.wages_time desc");
		String s ="select * from (select * from("+sb.toString()+" ) where  n <= :end ) WHERE n > :start  ";
		List<OaWages> list =  namedParameterJdbcTemplate.query(s.toString(),paraMap,new RowMapper<OaWages>() {

			@Override
			public OaWages mapRow(ResultSet rs, int rowNum) throws SQLException {
				OaWages wages = new OaWages();
				wages.setWagesAccount(rs.getString("wagesAccount"));
				wages.setName(rs.getString("name"));
				wages.setDeptName(rs.getString("deptName"));
				wages.setCategory(rs.getString("category"));
				wages.setPostPay(rs.getString("postPay"));
				wages.setBasePay(rs.getString("basePay"));
				wages.setNursinTeach(rs.getString("nursinTeach"));
				wages.setAchievements(rs.getString("achievements"));
				wages.setNursinTeaching(rs.getString("nursinTeaching"));
				wages.setKeepThink(rs.getString("keepThink"));
				wages.setHealthAllowance(rs.getString("healthAllowance"));
				wages.setOnlyChildFee(rs.getString("onlyChildFee"));
				wages.setHygieneFee(rs.getString("hygieneFee"));
				wages.setPHDFee(rs.getString("PHDFee"));
				wages.setSubsidyFee(rs.getString("subsidyFee"));
				wages.setIncreased(rs.getString("increased"));
				wages.setIncreasing(rs.getString("increasing"));
				wages.setTotalShould(rs.getString("totalShould"));
				wages.setDeductRent(rs.getString("deductRent"));
				wages.setHousingFund(rs.getString("housingFund"));
				wages.setBoardingFee(rs.getString("boardingFee"));
				wages.setMedicalInsurance(rs.getString("medicalInsurance"));
				wages.setOverallPlanning(rs.getString("overallPlanning"));
				wages.setUnemploymentInsurance(rs.getString("unemploymentInsurance"));
				wages.setDeductWages(rs.getString("deductWages"));
				wages.setHeatingCosts(rs.getString("heatingCosts"));
				wages.setAccountEeceivable(rs.getString("accountEeceivable"));
				wages.setTotalActual(rs.getString("totalActual"));
				wages.setProvidentFundAccount(rs.getString("providentFundAccount"));
				wages.setIDCard(rs.getString("IDCard"));
				wages.setWagesTime(rs.getDate("wagesTime"));
				return wages;
			}
			
		});
		//密钥--解密金额数据
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				list.get(i).setPostPay(StringUtils.isNotBlank(list.get(i).getPostPay())?AESUtil.getInstance().decode(list.get(i).getPostPay(), key):"0");
				list.get(i).setBasePay(StringUtils.isNotBlank(list.get(i).getBasePay())?AESUtil.getInstance().decode(list.get(i).getBasePay(), key):"0");
				list.get(i).setNursinTeach(StringUtils.isNotBlank(list.get(i).getNursinTeach())?AESUtil.getInstance().decode(list.get(i).getNursinTeach(), key):"0");
				list.get(i).setAchievements(StringUtils.isNotBlank(list.get(i).getAchievements())?(AESUtil.getInstance().decode(list.get(i).getAchievements(), key)):"0");
				list.get(i).setNursinTeaching(StringUtils.isNotBlank(list.get(i).getNursinTeaching())?AESUtil.getInstance().decode(list.get(i).getNursinTeaching(), key):"0");
				list.get(i).setKeepThink(StringUtils.isNotBlank(list.get(i).getKeepThink())?AESUtil.getInstance().decode(list.get(i).getKeepThink(), key):"0");
				list.get(i).setHealthAllowance(StringUtils.isNotBlank(list.get(i).getHealthAllowance())?AESUtil.getInstance().decode(list.get(i).getHealthAllowance(), key):"0");
				list.get(i).setOnlyChildFee(StringUtils.isNotBlank(list.get(i).getOnlyChildFee())?AESUtil.getInstance().decode(list.get(i).getOnlyChildFee(), key):"0");
				list.get(i).setHygieneFee(StringUtils.isNotBlank(list.get(i).getHygieneFee())?AESUtil.getInstance().decode(list.get(i).getHygieneFee(), key):"0");
				list.get(i).setPHDFee(StringUtils.isNotBlank(list.get(i).getPHDFee())?AESUtil.getInstance().decode(list.get(i).getPHDFee(), key):"0");
				list.get(i).setSubsidyFee(StringUtils.isNotBlank(list.get(i).getSubsidyFee())?AESUtil.getInstance().decode(list.get(i).getSubsidyFee(), key):"0");
				list.get(i).setIncreased(StringUtils.isNotBlank(list.get(i).getIncreased())?AESUtil.getInstance().decode(list.get(i).getIncreased(), key):"0");
				list.get(i).setIncreasing(StringUtils.isNotBlank(list.get(i).getIncreasing())?AESUtil.getInstance().decode(list.get(i).getIncreasing(), key):"0");
				list.get(i).setTotalShould(StringUtils.isNotBlank(list.get(i).getTotalShould())?AESUtil.getInstance().decode(list.get(i).getTotalShould(), key):"0");
				list.get(i).setDeductRent(StringUtils.isNotBlank(list.get(i).getDeductRent())?AESUtil.getInstance().decode(list.get(i).getDeductRent(), key):"0");
				list.get(i).setHousingFund(StringUtils.isNotBlank(list.get(i).getHousingFund())?AESUtil.getInstance().decode(list.get(i).getHousingFund(), key):"0");
				list.get(i).setBoardingFee(StringUtils.isNotBlank(list.get(i).getBoardingFee())?(AESUtil.getInstance().decode(list.get(i).getBoardingFee(), key)):"0");
				list.get(i).setMedicalInsurance(StringUtils.isNotBlank(list.get(i).getMedicalInsurance())?AESUtil.getInstance().decode(list.get(i).getMedicalInsurance(), key):"0");
				list.get(i).setOverallPlanning(StringUtils.isNotBlank(list.get(i).getOverallPlanning())?AESUtil.getInstance().decode(list.get(i).getOverallPlanning(), key):"0");
				list.get(i).setUnemploymentInsurance(StringUtils.isNotBlank(list.get(i).getUnemploymentInsurance())?AESUtil.getInstance().decode(list.get(i).getUnemploymentInsurance(), key):"0");
				list.get(i).setDeductWages(StringUtils.isNotBlank(list.get(i).getDeductWages())?AESUtil.getInstance().decode(list.get(i).getDeductWages(), key):"0");
				list.get(i).setHeatingCosts(StringUtils.isNotBlank(list.get(i).getHeatingCosts())?AESUtil.getInstance().decode(list.get(i).getHeatingCosts(), key):"0");
				list.get(i).setAccountEeceivable(StringUtils.isNotBlank(list.get(i).getAccountEeceivable())?AESUtil.getInstance().decode(list.get(i).getAccountEeceivable(), key):"0");
				list.get(i).setTotalActual(StringUtils.isNotBlank(list.get(i).getTotalActual())?AESUtil.getInstance().decode(list.get(i).getTotalActual(), key):"0");
			}
		}
		//查询总条数
		StringBuffer bufferTotal = new StringBuffer(sb.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
		retMap.put("total", total);
		retMap.put("rows", list==null?new ArrayList<OaWages>():list);
		return retMap;
	}
	/** 
	* @Description: 总条数 
	* @param wageAccount 工资账号
	* @param name 身份证号
	* @param wagesTime 查询时间
	* @throws Exception
	* @return int    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public int getTotal(String wageAccount, String name, String wagesTime) throws Exception{
		int total = 0;
		String hql = "FROM OaWages  WHERE stop_flg = 0 and del_flg = 0";
		if(StringUtils.isNotBlank(wageAccount)){
			hql+=" and wagesAccount='"+wageAccount+"'";
		}
		if(StringUtils.isNotBlank(name)){
			hql+=" and IDCard = '"+name+"'";
		}
		if(StringUtils.isNotBlank(wagesTime)){
			Date date1 = DateUtils.parseDateY_M(wagesTime);
			String sdate = DateUtils.formatDateY_M(date1);//当前月
			Date nextDate = DateUtils.addMonth(date1, 1);//下一月
			String edate = DateUtils.formatDateY_M(nextDate);
			hql+=" and wagesTime >=to_date('"+(wagesTime+"-01")+"', 'yyyy-MM-dd hh24:mi:ss') and wagesTime<to_date('"+(edate+"-01")+"', 'yyyy-MM-dd hh24:mi:ss')";
		}
		hql +=" order by wagesTime desc";
		return super.getTotal(hql);
	}
	/** 
	* @Description: 批量插入数据
	* @param oaWagesList 工资数据集合
	* @throws HibernateException
	* @throws SQLException
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public void saveBatch(List<OaWages> oaWagesList) throws SQLException {  
		//密钥---加密金额数据
		Session session = getHibernateTemplate().getSessionFactory().openSession();
        Connection conn = session.connection();  
        PreparedStatement stmt = null;  
        String sql = "INSERT INTO T_OA_WAGES("
           		+ "WAGES_ACCOUNT,WAGES_NAME,DEPTNAME,CATEGORY,POSTPAY,BASEPAY,NURSINTEACH,ACHIEVEMENTS,NURSINTEACHING,KEEPTHINK,HEALTH_ALLOWANCE,"
           		+ "ONLYCHILDFEE,HYGIENEFEE,PHDFEE,SUBSIDYFEE,INCREASED,INCREASING,TOTALSHOULD,DEDUCTRENT,HOUSINGFUND,BOARDINGFEE,MEDICAL_INSURANCE,"
           		+ "OVERALL_PLANNING,UNEMPLOYMENT_INSURANCE,DEDUCTWAGES,HEATINGCOSTS,ACCOUNT_EECEIVABLE,TOTALACTUAL,PROVIDENTFUNDACCOUNT,IDCARD,CREATEUSER,CREATEDEPT,"
           		+ "CREATETIME,STOP_FLG,DEL_FLG,WAGES_TIME,ID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
  
        stmt = conn.prepareStatement(sql);  
        conn.setAutoCommit(false);
        for (int i = 0; i < oaWagesList.size(); i++) {  
        	   stmt.setString(1, oaWagesList.get(i).getWagesAccount());
        	   stmt.setString(2, oaWagesList.get(i).getName());
        	   stmt.setString(3, oaWagesList.get(i).getDeptName());
        	   stmt.setString(4, oaWagesList.get(i).getCategory());
        	   stmt.setString(5, AESUtil.getInstance().encode(oaWagesList.get(i).getPostPay(),key));
        	   stmt.setString(6, AESUtil.getInstance().encode(oaWagesList.get(i).getBasePay(),key));
        	   stmt.setString(7, AESUtil.getInstance().encode(oaWagesList.get(i).getNursinTeach(),key));
        	   stmt.setString(8, AESUtil.getInstance().encode(oaWagesList.get(i).getAchievements(),key));
        	   stmt.setString(9, AESUtil.getInstance().encode(oaWagesList.get(i).getNursinTeaching(),key));
        	   stmt.setString(10,AESUtil.getInstance().encode(oaWagesList.get(i).getKeepThink(),key));
        	   stmt.setString(11,AESUtil.getInstance().encode(oaWagesList.get(i).getHealthAllowance(),key));
        	   stmt.setString(12, AESUtil.getInstance().encode(oaWagesList.get(i).getOnlyChildFee(),key));
        	   stmt.setString(13, AESUtil.getInstance().encode(oaWagesList.get(i).getHygieneFee(),key));
        	   stmt.setString(14, AESUtil.getInstance().encode(oaWagesList.get(i).getPHDFee(),key));
        	   stmt.setString(15, AESUtil.getInstance().encode(oaWagesList.get(i).getSubsidyFee(),key));
        	   stmt.setString(16, AESUtil.getInstance().encode(oaWagesList.get(i).getIncreased(),key));
        	   stmt.setString(17, AESUtil.getInstance().encode(oaWagesList.get(i).getIncreasing(),key));
        	   stmt.setString(18, AESUtil.getInstance().encode(oaWagesList.get(i).getTotalShould(),key));
        	   stmt.setString(19, AESUtil.getInstance().encode(oaWagesList.get(i).getDeductRent(),key));
        	   stmt.setString(20, AESUtil.getInstance().encode(oaWagesList.get(i).getHousingFund(),key));
        	   stmt.setString(21, AESUtil.getInstance().encode(oaWagesList.get(i).getBoardingFee(),key));
        	   stmt.setString(22, AESUtil.getInstance().encode(oaWagesList.get(i).getMedicalInsurance(),key));
        	   stmt.setString(23, AESUtil.getInstance().encode(oaWagesList.get(i).getOverallPlanning(),key));
        	   stmt.setString(24, AESUtil.getInstance().encode(oaWagesList.get(i).getUnemploymentInsurance(),key));
        	   stmt.setString(25, AESUtil.getInstance().encode(oaWagesList.get(i).getDeductWages(),key));
        	   stmt.setString(26, AESUtil.getInstance().encode(oaWagesList.get(i).getHeatingCosts(),key));
        	   stmt.setString(27, AESUtil.getInstance().encode(oaWagesList.get(i).getAccountEeceivable(),key));
        	   stmt.setString(28, AESUtil.getInstance().encode(oaWagesList.get(i).getTotalActual(),key));
        	   stmt.setString(29, oaWagesList.get(i).getProvidentFundAccount());
        	   stmt.setString(30, oaWagesList.get(i).getIDCard());
        	   stmt.setString(31, oaWagesList.get(i).getCreateUser());
        	   stmt.setString(32, oaWagesList.get(i).getCreateDept());
        	   stmt.setTimestamp(33, new Timestamp(oaWagesList.get(i).getCreateTime().getTime())); 
        	   stmt.setInt(34, 0);
        	   stmt.setInt(35, 0);
		       stmt.setTimestamp(36, new Timestamp(oaWagesList.get(i).getWagesTime().getTime())); 
        	   stmt.setString(37,UUID.randomUUID().toString().replace("-", ""));
        	   stmt.addBatch(); 
                // 每一万次执行并清除session  
            if (i % 10000 == 0) {  
            	stmt.executeBatch();  
            	conn.commit(); 
            	session.flush();  
                session.clear();  
            }  
        }  
        stmt.executeBatch();  
        conn.commit();
        session.flush();  
        session.close(); 
	}

	/** 
	* @Description: 查询所有的数据用于导出 
	* @param wagesAccount 工资账号
	* @param wagesName 身份账号
	* @param wagesTime 工资月份
	* @throws Exception
	* @return List<OaWages>    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public List<OaWages> listWagesQueryForExport(String wagesAccount, String wagesName, String wagesTime) throws Exception{
		String hql = "FROM OaWages  WHERE stop_flg = 0 and del_flg = 0";
		if(StringUtils.isNotBlank(wagesAccount)){
			hql+=" and wagesAccount='"+wagesAccount+"'";
		}
		if(StringUtils.isNotBlank(wagesName)){
			hql+=" and name like '%"+wagesName+"%'";
		}
		if(StringUtils.isNotBlank(wagesTime)){
			Date date1 = DateUtils.parseDateY_M(wagesTime);
			String sdate = DateUtils.formatDateY_M(date1);//当前月
			Date nextDate = DateUtils.addMonth(date1, 1);//下一月
			String edate = DateUtils.formatDateY_M(nextDate);
			hql+=" and wagesTime >=to_date('"+(wagesTime+"-01")+"', 'yyyy-MM-dd hh24:mi:ss') and wagesTime<to_date('"+(edate+"-01")+"', 'yyyy-MM-dd hh24:mi:ss')";
		}
		List<OaWages> list =super.find(hql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaWages>();
	}
	/** 
	* @Description: 验证账户密码
	* @param wagesAccount 工资账号
	* @param weagesPassword 密码
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee checkAccountByAId(String wagesAccount, String weagesPassword) throws Exception{
		String sql = "select t.WAGES_ACCOUNT as wagesAccount,t.WAGES_PASSWORD as wagesPassword from T_EMPLOYEE t "
				+ "where (t.WAGES_ACCOUNT = :account or t.EMPLOYEE_IDENTITYCARD =:account ) and t.WAGES_PASSWORD=:password"
				+ " and t.stop_flg = 0 and t.del_flg=0";
		SQLQuery queryObject = (SQLQuery) this.getSession().createSQLQuery(sql)
				.addScalar("wagesAccount")
				.addScalar("wagesPassword")
				.setParameter("account", wagesAccount).setParameter("password", weagesPassword);
		List<SysEmployee> employee =queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(employee!=null&&employee.size()>0){
			return employee.get(0);
		}
		return new SysEmployee();
	}
	/** 
	* @Description: 查询是否已经设置过密码
	* @param account 工资账号
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee checkAccountForInit(String account) throws Exception{
		String sql = "select t.WAGES_ACCOUNT as wagesAccount, t.WAGES_PASSWORD as wagesPassword from T_EMPLOYEE t where t.WAGES_ACCOUNT = :account and t.stop_flg = 0 and t.del_flg=0 ";
		SQLQuery queryObject = (SQLQuery) this.getSession().createSQLQuery(sql)
				.addScalar("wagesAccount")
				.addScalar("wagesPassword")
				.setParameter("account", account);
		List<SysEmployee> employee =queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(employee!=null&&employee.size()>0){
			return employee.get(0);
		}
		return new SysEmployee();
	}
	/** 
	* @Description: 查询当前登陆人的身份证信息 
	* @param account 账号
	* @throws Exception
	* @return SysEmployee    返回类型 
	* @author zx 
	* @date 2017年7月24日
	*/
	@Override
	public SysEmployee getIcdFoUser(String account) throws Exception{
		String sql = "select t.WAGES_ACCOUNT as wagesAccount,t.WAGES_PASSWORD as wagesPassword ,t.EMPLOYEE_IDENTITYCARD as idEntityCard,t.EMPLOYEE_JOBNO as jobNo from T_EMPLOYEE t where t.EMPLOYEE_JOBNO=:account "
				+ " and t.stop_flg = 0 and t.del_flg=0";
		SQLQuery queryObject = (SQLQuery) this.getSession().createSQLQuery(sql)
				.addScalar("wagesAccount")
				.addScalar("wagesPassword")
				.addScalar("idEntityCard")
				.addScalar("jobNo")
				.setParameter("account", account);
		List<SysEmployee> employee =queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(employee!=null&&employee.size()>0){
			return employee.get(0);
		}
		return new SysEmployee();
	}
}
