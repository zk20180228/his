package cn.honry.inpatient.costDerate.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
//import cn.honry.inpatient.info.dao.InpatientBedInfoDAO;
import cn.honry.inpatient.costDerate.dao.CostDerateDao;
import cn.honry.inpatient.costDerate.vo.DerateVo;
import cn.honry.inpatient.costDerate.vo.ThreeSearchVo;
import cn.honry.inpatient.inprePay.vo.PatientVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Repository("costDerateDao")
@SuppressWarnings({ "all" })
public class CostDerateDaoImpl  extends HibernateEntityDao<InpatientDerate> implements CostDerateDao{
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
	
	@Autowired
	@Qualifier(value = "hospitalbedInInterDAO")
	private HospitalbedInInterDAO hospitalbedDAO;
	
	/**  
	 * @Description：  根据病历号查询最新的接诊记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public DerateVo queryInpatientInfoObj(String medicalrecordId,String deptCode) {
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select t.inpatient_no as inpatientNo,t.patient_name as patientName,t.report_birthday as reportBirthday,");
		sql1.append("t.in_date as inDate,t.dept_code as deptCode,t.pact_code as pactCode,t.bedinfo_id as bedId,");
		sql1.append("t.PREPAY_COST as  prepayCost,t.TOT_COST as totCost,t.OWN_COST as ownCost,t.PAY_COST as payCost,");
		sql1.append("t.PUB_COST as pubCost,t.FREE_COST as freeCost,t.bed_name  as bedName, house_doc_code as houseDocCode , t.house_doc_name as houseDocName,");
		sql1.append(" t.nurse_cell_name  as nurseCellName,t.nurse_cell_code as nurseCellCode ,t.dept_name as deptName  ");
		sql1.append("from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info_now t  where  t.in_state = 'I' ");
		sql1.append("  and  t.inpatient_no = :medicalrecordId   and  t.NURSE_CELL_CODE=:deptCode ");
		sql1.append("order by t.inpatient_no");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", medicalrecordId);
		paraMap.put("deptCode", deptCode);
		List<InpatientInfoNow> inList =  namedParameterJdbcTemplate.query(sql1.toString(),paraMap,new RowMapper<InpatientInfoNow>() {

			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
				inpatientInfoNow.setInpatientNo(rs.getString("inpatientNo"));
				inpatientInfoNow.setPatientName(rs.getString("patientName"));
				inpatientInfoNow.setReportBirthday(rs.getDate("reportBirthday"));
				inpatientInfoNow.setInDate(rs.getDate("inDate"));
				inpatientInfoNow.setDeptCode(rs.getString("deptCode"));
				inpatientInfoNow.setPactCode(rs.getString("pactCode"));
				inpatientInfoNow.setBedId(rs.getString("bedId"));
				inpatientInfoNow.setPrepayCost(rs.getObject("prepayCost")==null?null:rs.getDouble("prepayCost"));
				inpatientInfoNow.setTotCost(rs.getObject("totCost")==null?null:rs.getDouble("totCost"));
				inpatientInfoNow.setOwnCost(rs.getObject("ownCost")==null?null:rs.getDouble("ownCost"));
				inpatientInfoNow.setPayCost(rs.getObject("payCost")==null?null:rs.getDouble("payCost"));
				inpatientInfoNow.setPubCost(rs.getObject("pubCost")==null?null:rs.getDouble("pubCost"));
				inpatientInfoNow.setFreeCost(rs.getObject("freeCost")==null?null:rs.getDouble("freeCost"));
				inpatientInfoNow.setBedName(rs.getString("bedName"));
				inpatientInfoNow.setHouseDocCode(rs.getString("houseDocCode"));
				inpatientInfoNow.setHouseDocName(rs.getString("houseDocName"));
				inpatientInfoNow.setNurseCellName(rs.getString("nurseCellName"));
				inpatientInfoNow.setNurseCellCode(rs.getString("nurseCellCode"));
				inpatientInfoNow.setDeptName(rs.getString("deptName"));
				return inpatientInfoNow;
			}
			
		});
		InpatientInfoNow iif=null;
		if(inList!=null&&inList.size()>0){
			iif=new InpatientInfoNow();
			iif =inList.get(0);
		}
		DerateVo dv=new DerateVo();
		if(iif!=null){
			dv.setInpatientNo(iif.getInpatientNo());//住院流水号
			dv.setPatientName(iif.getPatientName());//患者姓名
			dv.setReportBirthday(iif.getReportBirthday());//出生日期
			dv.setInDate(iif.getInDate());//入院时间
			dv.setDeptCode(iif.getDeptCode());//科室
			dv.setPactCode(iif.getPactCode());//合同单位
			dv.setPrepayCost(iif.getPrepayCost());//预交金
			dv.setTotCost(iif.getTotCost());//费用金额
			dv.setOwnCost(iif.getOwnCost());//自付金额
			dv.setPayCost(iif.getPayCost());//自付金额
			dv.setPubCost(iif.getPubCost());//公费金额
			dv.setFreeCost(iif.getFreeCost());//余额
			dv.setBedInfoId(iif.getBedId());//病床使用记录表主键
			dv.setBedName(iif.getBedName());//床号
			dv.setHouseDocCode(iif.getHouseDocCode());
			dv.setHouseDocName(iif.getHouseDocName());
			dv.setNurseCellCode(iif.getNurseCellCode());
			dv.setNurseCellName(iif.getNurseCellName());
			dv.setDeptName(iif.getDeptName());
			return dv;
		}else{
			return new DerateVo();
		}
	}
	/**  
	 * @Description：  根据住院流水号查询ThreeSarchVo的信息
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<ThreeSearchVo> queryThreeSearch(String inpatientNo) {
		//查询费用汇总表中的费用记录
		String hql="select t.FEE_CODE as feeCode,t.TOT_COST as totCost,t.OWN_COST as ownCost,t.PAY_COST as payCost,t.PUB_COST as pubCost "
				+ " from T_INPATIENT_FEEINFO_NOW t where t.del_flg=0 and INPATIENT_NO=:inpatientNo";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientFeeInfoNow> lst1 =  namedParameterJdbcTemplate.query(hql.toString(),paraMap,new RowMapper<InpatientFeeInfoNow>() {

			@Override
			public InpatientFeeInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientFeeInfoNow inpatientFeeInfoNow = new InpatientFeeInfoNow();
				inpatientFeeInfoNow.setFeeCode(rs.getString("feeCode"));
				inpatientFeeInfoNow.setTotCost(rs.getObject("totCost")==null?null:rs.getDouble("totCost"));	
				inpatientFeeInfoNow.setOwnCost(rs.getObject("ownCost")==null?null:rs.getDouble("ownCost"));
				inpatientFeeInfoNow.setPayCost(rs.getObject("payCost")==null?null:rs.getDouble("payCost"));
				inpatientFeeInfoNow.setPubCost(rs.getObject("pubCost")==null?null:rs.getDouble("pubCost"));
				return inpatientFeeInfoNow;
			}
			
		});
		List<ThreeSearchVo> lstVo=new ArrayList<ThreeSearchVo>();
		ThreeSearchVo tsVo=null;
		if(lst1!=null&&lst1.size()>0){
			for(int i=0;i<lst1.size();i++){
				tsVo=new ThreeSearchVo();
				tsVo.setFeeCode(lst1.get(i).getFeeCode());
				tsVo.setTotCost(lst1.get(i).getTotCost());
				tsVo.setOwnCost(lst1.get(i).getOwnCost());
				tsVo.setPayCost(lst1.get(i).getPayCost());
				tsVo.setPubCost(lst1.get(i).getPubCost());
				lstVo.add(tsVo);
			}
		}
		//去重（当最小费用代码相同时将相同的费用相加然后去除最小费用代码相同项）
		for ( int i = 0 ; i < lstVo.size() - 1 ; i ++ ) {
			 for ( int j = lstVo.size() - 1 ; j > i; j -- ) {
			       if (lstVo.get(j).getFeeCode() != null && lstVo.get(j).getFeeCode().equals(lstVo.get(i).getFeeCode())) {
			    	   //将查出来的数据进行四舍五入
			    	   BigDecimal fee1 = new   BigDecimal(lstVo.get(i).getOwnCost()+lstVo.get(j).getOwnCost());
					   Double fees1 = fee1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			    	   lstVo.get(i).setOwnCost(fees1);
			    	   //将查出来的数据进行四舍五入
			    	   BigDecimal fee2 = new   BigDecimal(lstVo.get(i).getPayCost()+lstVo.get(j).getPayCost());
					   Double fees2 = fee2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			    	   lstVo.get(i).setPayCost(fees2);
			    	   //将查出来的数据进行四舍五入
			    	   BigDecimal fee3 = new   BigDecimal(lstVo.get(i).getPubCost()+lstVo.get(j).getPubCost());
					   Double fees3 = fee3.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			    	   lstVo.get(i).setPubCost(fees3);
			    	   //将查出来的数据进行四舍五入
			    	   BigDecimal fee4 = new   BigDecimal(lstVo.get(i).getTotCost()+lstVo.get(j).getTotCost());
					   Double fees4 = fee4.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			    	   lstVo.get(i).setTotCost(fees4);
			    	   //去重的移除
			    	   lstVo.remove(j);
			       }
			 }
		}
		return lstVo;
	}
	/**  
	 * @Description：  根据住院流水号查询费用减免表中的记录
	 * @Author：TCJ
	 * @CreateDate：2015-1-11 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientDerate> queryDerate(String inpatientNo) {
		List<InpatientDerate> inpatientDerateList=new ArrayList<InpatientDerate>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.fee_code as feeCode,sum(t.derate_cost) as derateCost from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_derate t where t.clinic_no=:clinicNo ");
		sql.append("group by t.fee_code having sum(t.derate_cost) >0 ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("clinicNo", inpatientNo);
		List<InpatientDerate> list =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientDerate>() {

			@Override
			public InpatientDerate mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientDerate inpatientDerate = new InpatientDerate();
				inpatientDerate.setFeeCode(rs.getString("feeCode"));
				inpatientDerate.setDerateCost(rs.getObject("derateCost")==null?null:rs.getDouble("derateCost"));
				return inpatientDerate;
			}
		});
		
		for(int i=0;i<list.size();i++ ){
			StringBuilder sql1=new StringBuilder();
			sql1.append("select t.ID as id,t.clinic_no as clinicNo,t.happen_no as happenNo,t.sequence_no as sequenceNo,t.updatetime as updateTime,"
					+ " t.updateuser as updateUser,t.valid_flag as validFlag,t.derate_kind as derateKind,"
					+ " t.derate_type as derateType,t.fee_code as feeCode,t.derate_cost as derateCost,t.dept_code as deptCode  "
					+ " from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_derate t where t.balance_state = '0'  and t.clinic_no = :inpatientNo and t.fee_code=:feeCode "
					+ " and t.happen_no = (select max(r.happen_no) from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_derate r where r.fee_code =:feeCode "
					+ " and r.clinic_no=:clinicNo)");
			Map<String, Object> paraMap1 = new HashMap<String, Object>();
			paraMap1.put("inpatientNo", inpatientNo);
			paraMap1.put("feeCode", list.get(i).getFeeCode());
			paraMap1.put("clinicNo", inpatientNo);
			List<InpatientDerate> derateList =  namedParameterJdbcTemplate.query(sql1.toString(),paraMap1,new RowMapper<InpatientDerate>() {

				@Override
				public InpatientDerate mapRow(ResultSet rs, int rowNum) throws SQLException {
					InpatientDerate derate = new InpatientDerate();
					derate.setId(rs.getString("id"));
					derate.setClinicNo(rs.getString("clinicNo"));
					derate.setHappenNo(rs.getObject("happenNo")==null?null:rs.getInt("happenNo"));
					derate.setSequenceNo(rs.getObject("sequenceNo")==null?null:rs.getInt("sequenceNo"));
					derate.setUpdateTime(rs.getDate("updateTime"));
					derate.setUpdateUser(rs.getString("updateUser"));
					derate.setValidFlag(rs.getString("validFlag"));
					derate.setDerateKind(rs.getString("derateKind"));
					derate.setDerateType(rs.getString("derateType"));
					derate.setFeeCode(rs.getString("feeCode"));
					derate.setDerateCost(rs.getObject("derateCost")==null?null:rs.getDouble("derateCost"));
					derate.setDeptCode(rs.getString("deptCode"));
					return derate;
				}
				
			});
			for(int j=0;j<derateList.size();j++){
				inpatientDerateList.add(derateList.get(j));
			}
		}
		return inpatientDerateList;
	}
	/**  
	 * @Description：  查询最小费用名称Map
	 * @Author：TCJ
	 * @CreateDate：2015-1-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<MinfeeStatCode> quertFreeCodeMap() {
		String hql="from MinfeeStatCode where del_flg=0 ";
		List<MinfeeStatCode> mfsc = super.find(hql, null);
		if(mfsc!=null&&mfsc.size()>0){
			return mfsc;
		}
		return new ArrayList<MinfeeStatCode>();
	}
	
	@Override
	public List<InpatientDerate> queryDerateMoneySum(String inpatientNo) {
		String sql="select t.fee_code as feeCode, sum(t.derate_cost) as derateCost from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_derate t "
				+ "where t.CLINIC_NO=:inpatientNo"
				+ " group by t.fee_code";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientDerate> derateCostList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientDerate>() {

			@Override
			public InpatientDerate mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientDerate inpatientDerate = new InpatientDerate();
				inpatientDerate.setFeeCode(rs.getString("feeCode"));
				inpatientDerate.setDerateCost(rs.getObject("derateCost")==null?null:rs.getDouble("derateCost"));
				return inpatientDerate;
			}
			
		});
		if(derateCostList!=null&&derateCostList.size()>0){
			return derateCostList;
		}
		return new ArrayList<InpatientDerate>();
	}
	@Override
	public InpatientInfoNow querNurseCharge(String inpatientId) {
		String hql=" from InpatientInfoNow where id= ?";
		List<InpatientInfoNow> inlist=super.find(hql, inpatientId);
		if(inlist.size()>0&&inlist!=null){
			return inlist.get(0);
		}
		return new InpatientInfoNow();
	}

	@Override
	public List<InpatientInfoNow> patientNarTree(String deptCode) {
		StringBuffer sql =new StringBuffer();
		sql.append(" select i.medicalrecord_id as medicalrecordId,i.patient_name as patientName, i.IDCARD_NO as idcardNo ,i.INPATIENT_NO as inpatientNo");
		sql.append(" from t_inpatient_info_now i where  ");
		sql.append(" i.nurse_cell_code =:deptCode and i.IN_STATE='I'");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("deptCode", deptCode);
		List<InpatientInfoNow> iList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInfoNow>() {

			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
				inpatientInfoNow.setMedicalrecordId(rs.getString("medicalrecordId"));
				inpatientInfoNow.setPatientName(rs.getString("patientName"));
				inpatientInfoNow.setIdcardNo(rs.getString("idcardNo"));
				inpatientInfoNow.setInpatientNo(rs.getString("inpatientNo"));
				return inpatientInfoNow;
			}
		});
		if(iList!=null&&iList.size()>0){
			return iList; 
		}
	 return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfoList(String inpatientNo) {
		String hql=" from InpatientInfoNow where medicalrecordId = :inpatientNo";
		Query query =this.getSession().createQuery(hql);
		query.setParameter("inpatientNo", inpatientNo);
		List<InpatientInfoNow> inlist=query.list();
		if(inlist.size()>0&&inlist!=null){
			return inlist;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	@Override
	public List<SysEmployee> empComboboxDerate() {
		String hql=" from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empl=super.find(hql, null);
		if(empl!=null&&empl.size()>0){
			return empl;
		}
		return new ArrayList<SysEmployee>();
	}
	@Override
	public List<User> queryUserDerates() {
		String hql="select USER_ACCOUNT as account ,USER_NAME as name from T_SYS_USER where del_flg=0 and stop_flg=0";
		SQLQuery sqlQuery = this.getSession().createSQLQuery(hql);
		sqlQuery.addScalar("account").addScalar("name");
		List<User> userlist=sqlQuery.setResultTransformer(Transformers.aliasToBean(User.class)).list();
		if(userlist.size()>0&&userlist!=null){
			return userlist;
		}
		return new ArrayList<User>();
	}

	@Override
	public List<User> queryUserDerate() {
		String hql=" from T_SYS_USER where del_flg=0 and stop_flg=0";
		List<User> userlist=super.find(hql, null);
		if(userlist.size()>0&&userlist!=null){
			return userlist;
		}
		return new ArrayList<User>();
	}
	
	@Override
	public void updateinpatientFeeInfoNow(final InpatientDerate inpatientDerate) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
			      ps.setString(1, inpatientDerate.getId());
			      ps.setInt(2, inpatientDerate.getTransType());//交易类型
			      ps.setString(3, inpatientDerate.getClinicNo());//流水号
			      ps.setInt(4, inpatientDerate.getHappenNo());//发生序号
			      ps.setString(5, inpatientDerate.getDerateKind());//减免种类
			      ps.setString(6, inpatientDerate.getRecipeNo());//处方号
			      ps.setString(7, inpatientDerate.getFeeCode());//最小费用代码
			      ps.setDouble(8, inpatientDerate.getDerateCost());//减免金额
			      ps.setString(9, inpatientDerate.getDerateCause());//减免原因
			      ps.setString(10, inpatientDerate.getDerateType());//减免类型
			      ps.setString(11, inpatientDerate.getBalanceState());//结算状态
			      ps.setInt(12, inpatientDerate.getBalanceNo());//结算序号
			      ps.setString(13, inpatientDerate.getDeptCode());//减免科室
			      ps.setString(14, inpatientDerate.getCreateUser());
			      ps.setDate(15, (java.sql.Date) new Date(inpatientDerate.getCreateTime().getTime()));
			      ps.setString(16, inpatientDerate.getUpdateUser());
			      ps.setDate(17, (java.sql.Date) new Date(inpatientDerate.getUpdateTime().getTime()));
			   }
			});
	}
}
