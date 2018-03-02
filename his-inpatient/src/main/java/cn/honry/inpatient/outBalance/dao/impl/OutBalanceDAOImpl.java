package cn.honry.inpatient.outBalance.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessEcoformula;
import cn.honry.base.bean.model.BusinessEcoicdfee;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientChangeprepay;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.OutBalanceDAO;
import cn.honry.inpatient.outBalance.vo.InvoicePrintVo;
import cn.honry.utils.ShiroSessionUtils;
@Repository("outBalanceDAO")
@SuppressWarnings({ "all" })
public class OutBalanceDAOImpl extends HibernateEntityDao<InpatientInfo> implements OutBalanceDAO{
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
	public List<InpatientInfoNow> queryInfoByPatientNo(String medicalrecordId) throws Exception {
		String hql="FROM InpatientInfoNow i WHERE i.del_flg = 0 ";
		hql = hql +" and i.medicalrecordId = ? ";
		List<InpatientInfoNow> iList = super.find(hql, medicalrecordId);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public InpatientInfoNow queryInfoByinpatientNo(String inpatientNo) throws Exception{
		String sql = "select i.INPATIENT_ID as id,i.IN_STATE as inState from T_INPATIENT_INFO_NOW i "
				+ "where i.INPATIENT_NO =:inpatientNo";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientInfoNow> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInfoNow>() {
			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow vo = new InpatientInfoNow();
				vo.setId(rs.getString("id"));
				vo.setInState(rs.getString("inState"));
				return vo;
			}});
		return cbl.get(0);
	}
	@Override
	public String getDeptName(String deptCode) throws Exception {
		String deptName="";
		String hql="FROM SysDepartment i WHERE i.del_flg = 0 and i.stop_flg=0 and i.id=:deptCode ";
		try {
			List lst =  this.getSession().createQuery(hql).setParameter("deptCode", deptCode).list();
			if(lst!=null){
				if(lst.size()>0){
					SysDepartment s= (SysDepartment)lst.get(0);//获得第一条记录的头表信息
					deptName=s.getDeptName();
				}
			}
		} catch (Exception e) {
			throw e;
		} 
		return deptName;
	}
	@Override
	public Map<String, String> queryFinanceInvoiceNo(String id,
			String invoiceType) throws Exception{
		//初始化发票号
		int invoiceNo = 0;
		//初始化发票字母后数字
		String invoiceNosq = "";
		//初始化发票号字母
		String invoiceNoas = "";
		//初始化发票号补0之后的号码
		String invoiceUsednoa ="";	
		//new一个map
		Map<String,String> map=new HashMap<String,String>();
		//根据领取人（员工ID）发票类型查询发票信息
		String hql = " from FinanceInvoice where invoiceGetperson= ? and invoiceType =?  and  del_flg=0 and stop_flg=0 and  invoiceUsestate in ('0','1') order by invoiceStartno";
		List<FinanceInvoice> invoiceList = super.find(hql, id,invoiceType);
		if(invoiceList.size()>0){//判断是否已经领取发票
			//获得当前已使用号
			String invoiceNos = invoiceList.get(0).getInvoiceUsedno();
			//截取后面的数字
			invoiceNosq = invoiceNos.substring(1);
			//前面的字母
			invoiceNoas = invoiceNos.substring(0, 1);
			//转为int类型
			int invoiceNoa = Integer.parseInt(invoiceNosq);
			//加1是下一个要使用的还未使用的发票号
			invoiceNo = invoiceNoa+1;
			//获取补0的长度
			int lengths = 13-((invoiceNo+"").length());
			for(int a=0;a<lengths;a++){//循环补0
				invoiceUsednoa=invoiceUsednoa+"0";
			}
			//拼接发票号
			String invoiceUserNo = invoiceNoas + invoiceUsednoa + invoiceNo;
			map.put("resMsg", "success");
			map.put("resCode", invoiceUserNo);
		}else{
			map.put("resMsg", "error");
			map.put("resCode", "发票已用完请重新领取!");
		}
		return map;
	}
	@Override
	public void saveInvoiceFinance(String id, String invoiceNo,
			String invoiceType) throws Exception{
		//判断这个发票号是不是本号段最后一位
		String hql = "from FinanceInvoice  where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"'";
		List<FinanceInvoice> financeInvoiceList = super.find(hql, null);
		if(financeInvoiceList.size()>0){
			String invoiceEndno = financeInvoiceList.get(0).getInvoiceEndno();//得到终止号
			String invoiceStartno = financeInvoiceList.get(0).getInvoiceStartno();//得到开始号
			if(invoiceEndno.equals(invoiceNo)){//当等于终止号的时候相当最后一个
				String hql2 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql2).setString(0,invoiceNo).setLong(1,-1 ).executeUpdate();
			}else if(invoiceStartno.equals(invoiceNo)){//当等于开始号的时候相当于第一个
				String hql3 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"' ";
				this.createQuery(hql3).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}else{
				String hql4 = "update FinanceInvoice set invoiceUsedno= ?,invoiceUsestate= ? where invoiceGetperson = '"+id+"' and  invoiceType= '"+invoiceType+"'";
				this.createQuery(hql4).setString(0,invoiceNo).setLong(1,1 ).executeUpdate();
			}
		}
	}
	@Override
	public SysEmployee queryEmployee(String id) throws Exception{
		String hql = " from SysEmployee where userId =? and del_flg=? and stop_flg=? ";
		List<SysEmployee> employeeList = super.find(hql, id,0,0);
		if(employeeList==null||employeeList.size()<=0){
			return new SysEmployee();
		}
		return employeeList.get(0);
	}
	@Override
	public List<InpatientInfoNow> getbalanceNo(String inpatientNo) throws Exception{
		String sql = "select t.balance_no as balanceNo,t.INPATIENT_NO as inpatientNo from t_inpatient_info_now t "
				+ "where t.inpatient_no=:inpatientNo order by t.balance_no desc ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientInfoNow> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInfoNow>() {
			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow vo = new InpatientInfoNow();
				vo.setBalanceNo(rs.getInt("balanceNo")); 
				vo.setInpatientNo(rs.getString("inpatientNo")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<InpatientSurety> queryInpatientSurety(String inpatientNo, String outDate, String inDate) throws Exception{
		String sql = "select sum(t.surety_cost) as suretyCost from t_inpatient_surety t "
				+" where (t.inpatient_no = :inpatientNo) "
			    +" and t.stop_flg=0 and t.del_flg=0";
		if(StringUtils.isNotBlank(inDate)){
			if(StringUtils.isNotBlank(outDate)){
				sql+="and t.createtime>=to_date(:inDate,'yyyy-MM-dd hh24:mi:ss') "; 
				sql+="and t.createtime<=to_date(:outDate,'yyyy-MM-dd hh24:mi:ss') "; 
			}
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		if(StringUtils.isNotBlank(inDate)){
			if(StringUtils.isNotBlank(outDate)){
				paraMap.put("inDate", inDate);
				paraMap.put("outDate", outDate);
			}
		}
		List<InpatientSurety> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientSurety>() {
			@Override
			public InpatientSurety mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientSurety vo = new InpatientSurety();
				vo.setSuretyCost(rs.getDouble("suretyCost")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<InpatientCancelitemNow> getInpatientCancelitem(String inpatientNo) throws Exception{
		String sql = "select i.APPLY_NO as id  from T_INPATIENT_CANCELITEM_NOW i where i.INPATIENT_NO =:inpatientNo "
				+ " and i.del_flg = 0 and i.stop_flg=0 and i.CHARGE_FLAG = 0";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientCancelitemNow> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientCancelitemNow>() {
			@Override
			public InpatientCancelitemNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientCancelitemNow vo = new InpatientCancelitemNow();
				vo.setId(rs.getString("id")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<OutpatientDrugcontrol> queryDrugcontrol(String inpatientNo) throws Exception{
		String controlName = "退药台";
		String sql = "select d.control_name as controlName from t_outpatient_drugcontrol d  "
				+ "join t_drug_billclass aa on aa.control_id=d.control_id "
				+ "join t_drug_applyout t on t.billclass_code=aa.billclass_code "
				+ " where t.valid_state=1 and t.patient_id=:inpatientNo and t.del_flg = 0 and d.del_flg = 0 and aa.del_flg = 0"
						+ " and d.control_name=:controlName ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		paraMap.put("controlName", controlName);
		List<OutpatientDrugcontrol> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<OutpatientDrugcontrol>() {
			@Override
			public OutpatientDrugcontrol mapRow(ResultSet rs, int rowNum) throws SQLException {
				OutpatientDrugcontrol vo = new OutpatientDrugcontrol();
				vo.setControlName(rs.getString("controlName")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<InpatientInPrepayNow> queryInPrepay(String inpatientNo) throws Exception{
		String sql = "select i.ID as id  from T_INPATIENT_INPREPAY_NOW i where i.INPATIENT_NO =:inpatientNo "
				+ " and i.del_flg = 0 and i.stop_flg=0 and i.TRANS_FLAG = 1";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientInPrepayNow> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInPrepayNow>() {
			@Override
			public InpatientInPrepayNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInPrepayNow vo = new InpatientInPrepayNow();
				vo.setId(rs.getString("id")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<InpatientInPrepayNow> queryInpatientInPrepay(String inpatientNo,
			String inDate, String outDate) throws Exception{
		String hql="FROM InpatientInPrepayNow i WHERE i.del_flg = :del ";
		hql = hql +" and i.inpatientNo= :inpatientNo and i.balanceState=:balanceState and i.prepayState=0 ";
		if(StringUtils.isNotBlank(inDate)){
			if(StringUtils.isNotBlank(outDate)){
				hql=hql+" and i.createTime>=to_date(:inDate,'yyyy-MM-dd hh24:mi:ss') "
						+ " and i.createTime<=to_date(:outDate,'yyyy-MM-dd hh24:mi:ss')";
			}
		}
		Query queryObject = this.getSession().createQuery(hql.toString()).setParameter("inpatientNo", inpatientNo)
				.setParameter("balanceState", 0).setParameter("del", 0);
		if(StringUtils.isNotBlank(inDate)){
			if(StringUtils.isNotBlank(outDate)){
				queryObject.setParameter("inDate", inDate).setParameter("outDate", outDate);
			}
		}
		List<InpatientInPrepayNow> iList = queryObject.list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInPrepayNow>();
	}
	@Override
	public List<InpatientFeeInfoNow> InpatientFeeList(String inpatientNo, String inDate1, String outDate1) throws Exception{
		String sql = "select fee_code as feeCode,sum(tot_cost) as totCost,"
				+ "sum(own_cost) as ownCost,sum(pub_cost) as pubCost,"
				+ "sum(pay_cost) as payCost,sum(eco_cost) as ecoCost "
			    +" from t_inpatient_feeinfo_now "
			    +" where (inpatient_no = :inpatientNo ) "
			    +" and (balance_state = :balanceState) ";
		if(StringUtils.isNotBlank(inDate1)){
			if(StringUtils.isNotBlank(outDate1)){
				sql+="and createtime>=to_date(:inDate1,'yyyy-MM-dd hh24:mi:ss') "; 
				sql+="and createtime<=to_date(:outDate1,'yyyy-MM-dd hh24:mi:ss') "; 
			}
		}
		sql+=(" group by FEE_CODE");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		paraMap.put("balanceState", 0);
		if(StringUtils.isNotBlank(inDate1)){
			if(StringUtils.isNotBlank(outDate1)){
				paraMap.put("inDate1", inDate1);
				paraMap.put("outDate1", outDate1);
			}
		}
		List<InpatientFeeInfoNow> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientFeeInfoNow>() {
			@Override
			public InpatientFeeInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientFeeInfoNow vo = new InpatientFeeInfoNow();
				vo.setFeeCode(rs.getString("feeCode")); 
				vo.setTotCost(rs.getDouble("totCost")); 
				vo.setOwnCost(rs.getDouble("ownCost")); 
				vo.setPubCost(rs.getDouble("pubCost")); 
				vo.setPayCost(rs.getDouble("payCost")); 
				vo.setEcoCost(rs.getDouble("ecoCost")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<BusinessEcoformula> getclinicCode(String clinicCode) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.clinic_code as clinicCode,icdcode_flag as icdcodeFlag from t_business_ecoformula t"
				+ "  where t.clinic_code=:clinicCode and del_flg = :del and t.icdcode_flag = :icdcode ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("clinicCode").addScalar("icdcodeFlag",Hibernate.INTEGER).setParameter("clinicCode", clinicCode)
		.setParameter("del", 0).setParameter("icdcode", 1);
		List<BusinessEcoformula> list = queryObject.setResultTransformer(Transformers.aliasToBean(BusinessEcoformula.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessEcoformula>();
	}
	@Override
	public List<BusinessEcoicdfee> getcost(String sysdate, String inpatientNo) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.cost as Cost from t_business_met_diagnose t join t_business_ecoicdfee b on b.icd_code=t.icd_code "
				+ "where b.begin_date  < to_date(:sysdate,'yyyy-mm-dd hh24:mi:ss') "
				+ "and b.end_date > to_date(:sysdate,'yyyy-mm-dd hh24:mi:ss')  "
				+ "and t.inpatient_no = :inpatientNo and t.del_flg=:del ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("Cost",Hibernate.DOUBLE).setParameter("sysdate", sysdate).setParameter("inpatientNo", inpatientNo)
		.setParameter("del", 0);
		List<BusinessEcoicdfee> list = queryObject.setResultTransformer(Transformers.aliasToBean(BusinessEcoicdfee.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessEcoicdfee>();
	}
	@Override
	public List<InpatientDerate> getclinicNo(String inpatientNo, String inDate1, String outDate1) throws Exception{
		String sql = "select sum(t.DERATE_COST) as derateCost from T_INPATIENT_DERATE t "
				+ " where t.CLINIC_NO=:inpatientNo and (t.BALANCE_STATE=:state or t.BALANCE_STATE is null)"
				+ " and t.DEL_FLG=:del ";
		if(StringUtils.isNotBlank(inDate1)){
			if(StringUtils.isNotBlank(outDate1)){
				sql+="and t.createtime>=to_date(:inDate1,'yyyy-MM-dd HH24:mi:ss') "; 
				sql+="and t.createtime<=to_date(:outDate1,'yyyy-MM-dd HH24:mi:ss') "; 
			}
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		paraMap.put("state", 0);
		paraMap.put("del", 0);
		if(StringUtils.isNotBlank(inDate1)){
			if(StringUtils.isNotBlank(outDate1)){
				paraMap.put("inDate1", inDate1);
				paraMap.put("outDate1", outDate1);
			}
		}
		List<InpatientDerate> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientDerate>() {
			@Override
			public InpatientDerate mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientDerate vo = new InpatientDerate();
				vo.setDerateCost(rs.getDouble("derateCost")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<InpatientInfoNow> InpatientInfoqueryFee(String inpatientNo) throws Exception{
		String hql="from InpatientInfoNow where inpatientNo=? ";
		List<InpatientInfoNow> iList = super.find(hql, inpatientNo);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public InpatientInPrepayNow queryInpatientInPrepayById(String ids) throws Exception {
		String hql="from InpatientInPrepayNow where id = ? and del_flg = 0 and stop_flg=0 ";
		List<InpatientInPrepayNow> iList = super.find(hql, ids);
		if(iList!=null&&iList.size()>0){
			return iList.get(0);
		}
		return new InpatientInPrepayNow();
	}
	@Override
	public List<InpatientChangeprepay> queryInpatientChangeprepay(
			String inpatientNo) throws Exception{
		String hql="from InpatientChangeprepay where clinicNo = ? "
				+ " and del_flg = 0 and stop_flg=0 ";
		List<InpatientChangeprepay> iList = super.find(hql, inpatientNo);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientChangeprepay>();
	}
	@Override
	public List<InpatientDerate> getDerate(String inpatientNo) throws Exception{
		String sql = "select i.ID as id  from T_INPATIENT_DERATE i where i.CLINIC_NO =:clinicNo "
				+ " and i.del_flg = 0 and i.stop_flg=0 ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("clinicNo", inpatientNo);
		List<InpatientDerate> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientDerate>() {
			@Override
			public InpatientDerate mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientDerate vo = new InpatientDerate();
				vo.setId(rs.getString("id")); 
				return vo;
			}});
		return cbl;
	}
	@Override
	public List<InpatientFeeInfoNow> inpatientFeeInfoFee(String inpatientNo) throws Exception{
		String hql="from InpatientFeeInfoNow where inpatientNo=? "
				+ " and del_flg = 0 and stop_flg=0";
		List<InpatientFeeInfoNow> iList = super.find(hql, inpatientNo);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientFeeInfoNow>();
	}
	@Override
	public InpatientDerate maxHappon(String inpatientNo) throws Exception{
		String sql = "select max(t.happen_no) as happenNo from T_INPATIENT_DERATE t  where t.CLINIC_NO=:inpatientNo ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientDerate> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientDerate>() {
			@Override
			public InpatientDerate mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientDerate vo = new InpatientDerate();
				vo.setHappenNo(rs.getInt("happenNo")); 
				return vo;
			}});
		return cbl.get(0);
	}
	@Override
	public List<MinfeeStatCode> queryMinfeeStatCode(String minfeeCode) throws Exception{
		String hql="from MinfeeStatCode t where t.reportCode=? and t.del_flg = ? and t.stop_flg=?";
		List<MinfeeStatCode> iList=super.find(hql,"ZY01",0,0);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<MinfeeStatCode>();
	}
	@Override
	public List<InpatientMedicineListNow> queryInpatientMedicineList(String inpatientNo) throws Exception{
		String hql="from InpatientMedicineListNow where inpatientNo=? and balanceState=? ";
		List<InpatientMedicineListNow> iList = super.find(hql, inpatientNo,0);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientMedicineListNow>();
	}
	@Override
	public List<InpatientItemListNow> queryInpatientItemList(String inpatientNo) throws Exception{
		String hql="from InpatientItemListNow where inpatientNo=? and balanceState=? ";
		List<InpatientItemListNow> iList = super.find(hql, inpatientNo,0);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientItemListNow>();
	}
	@Override
	public InpatientShiftData queryMaxHappon(String inpatientNo) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("select max(t.happen_no) as happenNo from T_INPATIENT_SHIFTDATA t  where t.CLINIC_NO=:inpatientNo ");
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("happenNo",Hibernate.INTEGER).setParameter("inpatientNo", inpatientNo);
		List<InpatientShiftData> list=null;
		try {
			list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientShiftData.class)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientShiftData();
	}
	@Override
	public List<InpatientFeeInfoNow> inpatientNo(String inpatientNo,String inDate1, String outDate1) throws Exception{//备注一下,有点问题需要解决
		String sql = "SELECT sum(tot_cost) as totCost,"
					+ "sum(own_cost) ownCost, sum(pub_cost) pubCost,"
					+ "sum(pay_cost) payCost,sum(eco_cost) ecoCost "
				    +" FROM T_INPATIENT_FEEINFO_NOW "
				    +"WHERE (INPATIENT_NO = :inpatientNo ) "
				    +"AND (BALANCE_STATE = :state) "
				    +"HAVING sum(tot_cost) <> :cost ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		paraMap.put("state", 0);
		paraMap.put("cost", 0);
		List<InpatientFeeInfoNow> cbl =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientFeeInfoNow>() {
			@Override
			public InpatientFeeInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientFeeInfoNow vo = new InpatientFeeInfoNow();
				vo.setTotCost(rs.getDouble("totCost"));
				vo.setOwnCost(rs.getDouble("ownCost"));
				vo.setPubCost(rs.getDouble("pubCost"));
				vo.setPayCost(rs.getDouble("payCost"));
				vo.setEcoCost(rs.getDouble("ecoCost"));
				return vo;
			}});
			return cbl;
	}
	public List<MinfeeStatCode> getList(String minfeeCode) throws Exception{
		DetachedCriteria dc=DetachedCriteria.forClass(MinfeeStatCode.class);
		return (List<MinfeeStatCode>) getHibernateTemplate().findByCriteria(dc);
	}
	@Override
	public void updateInpatientInPrepayNow(InpatientInPrepayNow inPrepayNow,Date balanceDate) throws Exception{
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String sql = "update T_INPATIENT_INPREPAY_NOW set BALANCE_DATE=?,BALANCE_STATE = ?,BALANCE_OPERCODE = ?,BALANCE_OPERNAME=?,"
				+ "BALANCE_NO = ? ,INVOICE_NO = ? where ID = ?";  
	    Object args[] = new Object[]{new Date(balanceDate.getTime()),1,user.getAccount(),user.getName(),inPrepayNow.getBalanceNo(),inPrepayNow.getInvoiceNo(),inPrepayNow.getId()};  
	    int t = jdbcTemplate.update(sql,args);
	}
	@Override
	public void updateInpatientDerate(InpatientDerate inpatientDerate,Date balanceDate) throws Exception{
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String sql = "update T_INPATIENT_DERATE set BALANCE_NO = ?,BALANCE_STATE = ?,INVOICE_NO = ?,UPDATEUSER=?,"
				+ "UPDATETIME = ? where ID = ?";  
	    Object args[] = new Object[]{inpatientDerate.getBalanceNo(),1,inpatientDerate.getInvoiceNo(),user.getAccount(), new Date(balanceDate.getTime()),inpatientDerate.getId()};  
	    int t = jdbcTemplate.update(sql,args);
	}
	@Override
	public void updateInpatientFee(InpatientFeeInfoNow inpatientFeeInfoNow) throws Exception{
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String sql = "update T_INPATIENT_FEEINFO_NOW set BALANCE_NO = ?,BALANCE_STATE = ?,INVOICE_NO = ?,UPDATEUSER=?,"
				+ "UPDATETIME = ?,BALANCE_DATE = ?,BALANCE_OPERCODE = ? where ID = ?";  
	    Object args[] = new Object[]{inpatientFeeInfoNow.getBalanceNo(),1,inpatientFeeInfoNow.getInvoiceNo(),user.getAccount(), new Date(),new Date(inpatientFeeInfoNow.getBalanceDate().getTime()),user.getAccount(),inpatientFeeInfoNow.getId()};  
	    int t = jdbcTemplate.update(sql,args);
	}
	@Override
	public List<InvoicePrintVo> printBalance(String inpatientNo) throws Exception{
		StringBuffer buffer=new StringBuffer(700);
		//TODO 此处应有发票号
		buffer.append("SELECT I.PATIENT_NAME AS pName,BH.INVOICE_NO AS invoiceNo,I.MEDICALRECORD_ID AS medicalRecordId,");
		buffer.append("TO_CHAR(BH.BALANCE_NO) AS balanceNo,TO_CHAR(I.IN_DATE, 'yyyy-MM--dd') as inDate,I.DEPT_NAME AS deptCode,");
		buffer.append("BH.PREPAY_COST AS prepayCost,BH.SUPPLY_COST AS supplyCost,BH.RETURN_COST AS returnCost,");
		buffer.append("TO_CHAR(BH.BALANCE_DATE, 'yyyy-MM-dd') AS balaceDate,");
		buffer.append("(select EMPLOYEE_NAME from T_EMPLOYEE where EMPLOYEE_ID = bh.balance_opercode) as operCode,BH.TOT_COST AS sumPay,");
		buffer.append("MONEY2CHINESE(BH.TOT_COST) as sumMoney ");
		buffer.append("FROM T_INPATIENT_BALANCEHEAD_NOW BH INNER JOIN T_INPATIENT_INFO_NOW I ON BH.INPATIENT_NO = I.INPATIENT_NO ");
		buffer.append("WHERE BH.INPATIENT_NO='"+inpatientNo+"' AND BH.TRANS_TYPE='1'");
		List<InvoicePrintVo> list=super.getSession().createSQLQuery(buffer.toString()).addScalar("pName").addScalar("invoiceNo")
							.addScalar("medicalRecordId").addScalar("balanceNo")
							.addScalar("inDate").addScalar("deptCode").addScalar("prepayCost",Hibernate.DOUBLE)
							.addScalar("supplyCost",Hibernate.DOUBLE).addScalar("returnCost",Hibernate.DOUBLE).addScalar("sumMoney")
							.addScalar("balaceDate").addScalar("operCode").addScalar("sumPay",Hibernate.DOUBLE)
							.setResultTransformer(Transformers.aliasToBean(InvoicePrintVo.class)).list();
		String sql="SELECT distinct T.STAT_NAME AS statName,trunc(T.TOT_COST,2) AS prepayCost FROM T_INPATIENT_BALANCELIST_NOW T LEFT JOIN T_INPATIENT_INFO_NOW d on d.inpatient_no = t.inpatient_no LEFT JOIN T_BUSINESS_CONTRACTUNIT e on e.UNIT_CODE = t.pact_code WHERE T.INPATIENT_NO='"+inpatientNo+"' AND T.TRANS_TYPE='1'";
		List<InvoicePrintVo> listCost=super.getSession().createSQLQuery(sql).addScalar("statName").addScalar("prepayCost",Hibernate.DOUBLE)
										.setResultTransformer(Transformers.aliasToBean(InvoicePrintVo.class)).list();
		List<InvoicePrintVo> list1=new ArrayList<InvoicePrintVo>();
		List<InvoicePrintVo> list2=new ArrayList<InvoicePrintVo>();
		List<InvoicePrintVo> list3=new ArrayList<InvoicePrintVo>();
		List<InvoicePrintVo> list4=new ArrayList<InvoicePrintVo>();
		for(int i=0,len=listCost.size();i<len;i++){
			if(i%4==0){
				list1.add(listCost.get(i));
			}else if(i%4==1){
				list2.add(listCost.get(i));
			}else if(i%4==2){
				list3.add(listCost.get(i));
			}else{
				list4.add(listCost.get(i));
			}
		}
		list.get(0).setInvoiceList1(list1);
		list.get(0).setInvoiceList2(list2);
		list.get(0).setInvoiceList3(list3);
		list.get(0).setInvoiceList4(list4);
		return list;
	}
}
