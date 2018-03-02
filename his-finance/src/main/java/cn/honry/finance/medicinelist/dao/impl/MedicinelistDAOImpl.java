package cn.honry.finance.medicinelist.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.medicinelist.dao.MedicinelistDAO;
import cn.honry.finance.medicinelist.vo.RecipeNoVo;
import cn.honry.finance.medicinelist.vo.SpeNalVo;
import cn.honry.finance.medicinelist.vo.UndrugAndWare;
import cn.honry.inner.vo.MedicalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("medicinelistDAO")
@SuppressWarnings({ "all" })
public class MedicinelistDAOImpl extends HibernateEntityDao<OutpatientFeedetailNow> implements MedicinelistDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public SysEmployee queryEmployee(String userId) {
		String hql = " from SysEmployee where userId ='"+userId+"' and del_flg=0 and stop_flg=0 ";
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList==null||employeeList.size()<=0){
			return new SysEmployee();
		}
		return employeeList.get(0);
	}

	public List<Patient> vagueFindPatientById(String patientNo) {
		String hql=" from Patient t where t.del_flg=0 and t.stop_flg=0 and t.medicalrecordId = '"+patientNo+"'";
		List<Patient> patientList = super.find(hql, null);
		if(patientList!=null&&patientList.size()>=0){
			return patientList;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public PatientIdcard queryPatientIdcardByBlh(String cardNo) {
		String hql = " from PatientIdcard where idcardNo ='"+cardNo+"' and del_flg=0 and stop_flg=0 ";
		List<PatientIdcard> patientIdcardList = super.find(hql, null);
		if(patientIdcardList==null||patientIdcardList.size()<=0){
			return new PatientIdcard();
		}
		return patientIdcardList.get(0);
	}

	@Override
	public List<Patient> findPatientById(String patientNo) {
		String hql = " from Patient where medicalrecordId ='"+patientNo+"' and del_flg=0 and stop_flg=0 ";
		List<Patient> patientList = super.find(hql, null);
		if(patientList==null||patientList.size()<=0){
			return new ArrayList<Patient>();
		}
		return patientList;
	}

	@Override
	public PatientIdcard queryIdcard(String patientNo) {
		String hql = " from PatientIdcard where patient.medicalrecordId = '"+patientNo+"' and del_flg=0 and stop_flg=0";
		List<PatientIdcard> idcardList = super.find(hql, null);
		if(idcardList==null||idcardList.size()<=0){
			return new PatientIdcard();
		}
		return idcardList.get(0);
	}

	@Override
	public PatientBlackList queryBlackList(String patientNo) {
		String hql = " from PatientBlackList where medicalrecordId = '"+patientNo+"' and del_flg=0 and stop_flg=0";
		List<PatientBlackList> blackList = super.find(hql, null);
		if(blackList==null||blackList.size()<=0){
			return new PatientBlackList();
		}
		return blackList.get(0);
	}

	@Override
	public List<RegistrationNow> findRegisterInfo(String patientNo,String parameterValue) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		Date date=new Date();
		Integer dates = Integer.parseInt(parameterValue);
		//延后日期
		Date date2 = DateUtils.addDay(date, -dates);
		String hql = " from RegistrationNow where midicalrecordId = '"+patientNo+"' and del_flg=0 and stop_flg=0 and to_char(regDate,'yyyy-MM-dd') <='"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'  and del_flg=0 and stop_flg=0 and to_char(regDate,'yyyy-MM-dd') >= '"+DateUtils.formatDateY_M_D(date2)+"' and inState= 0 and transType = 1  Order By regDate Desc ";
		List<RegistrationNow> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new ArrayList<RegistrationNow>();
		}
		return infoList;
	}

	@Override
	public HospitalParameter queryParameterInfoTime() {
		String hql = " from HospitalParameter where parameterCode = 'infoTime' ";
		List<HospitalParameter> parameterList = super.find(hql, null);
		if(parameterList==null||parameterList.size()<=0){
			return new HospitalParameter();
		}
		return parameterList.get(0);
	}

	@Override
	public List<SysDepartment> findDepartment() {
		String hql = " from SysDepartment where del_flg=0 and stop_flg=0";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return departmentList;
	}

	@Override
	public List<BusinessContractunit> findContractunit() {
		StringBuffer sb = new StringBuffer();
		sb.append(" Select t.unit_code As encode,t.PAYKIND_CODE as paykindCode,t.unit_name As name,t.MCARD_FLAG as mcardFlag,t.PUB_RATIO as pubRatio,t.PAY_RATIO as payRatio,"
				+ "t.OWN_RATIO as ownRatio,t.ECO_RATIO as ecoRatio,t.UNIT_PINYIN as pinyin,t.UNIT_WB as wb,t.UNIT_INPUTCODE as inputCode,MCARD_FLAG as mcardFlag "
				+ " from t_business_contractunit t Where t.stop_flg = 0 And t.del_flg = 0 ");
		List<BusinessContractunit> contractunitList = namedParameterJdbcTemplate.query(sb.toString(), new RowMapper<BusinessContractunit>() {
			@Override
			public BusinessContractunit mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BusinessContractunit bc = new BusinessContractunit();
				bc.setEncode(rs.getString("encode"));
				bc.setName(rs.getString("name"));
				bc.setMcardFlag(rs.getInt("mcardFlag"));
				bc.setPayRatio(rs.getDouble("payRatio"));
				bc.setPubRatio(rs.getDouble("pubRatio"));
				bc.setOwnRatio(rs.getDouble("ownRatio"));
				bc.setEcoRatio(rs.getDouble("ecoRatio"));
				bc.setPinyin(rs.getString("pinyin"));
				bc.setWb(rs.getString("wb"));
				bc.setInputCode(rs.getString("inputCode"));
				bc.setPaykindCode(rs.getString("paykindCode"));
				bc.setMcardFlag(rs.getInt("mcardFlag"));
				return bc;
			}
		});
//		String hql = " from BusinessContractunit where del_flg=0 and stop_flg=0";
//		List<BusinessContractunit> contractunitList = super.find(hql, null);
		if(contractunitList==null||contractunitList.size()<=0){
			return new ArrayList<BusinessContractunit>();
		}
		return contractunitList;
	}

	@Override
	public List<SysEmployee> findEmployee() {
		String hql = " from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList==null||employeeList.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return employeeList;
	}

	@Override
	public List<SysEmployee> findEmployeeList(String regDpcd) {
		String hql = " from SysEmployee where del_flg=0 and stop_flg=0";
		if(StringUtils.isNotBlank(regDpcd)){
			hql = hql + " and deptId = '"+regDpcd+"'";
		}
		List<SysEmployee> employeeList = super.find(hql, null);
		if(employeeList==null||employeeList.size()<=0){
			return new ArrayList<SysEmployee>();
		}
		return employeeList;
	}

	@Override
	public List<RecipeNoVo> findFeedetailRecipeNo(String clinicCode) {
//		String hql = "select distinct  f.recipe_no as recipeNo, f.reg_dpcd as dept,f.doct_code emp from "+HisParameters.HISPARSCHEMAHISUSER+"t_outpatient_feedetail f where to_char(f.oper_date,'yyyy-MM-dd')= '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"' and f.pay_flag=0 and f.del_flg=0 and f.stop_flg=0 and f.clinic_code in ('"+clinicCode+"')";
		String hql = "select distinct  f.recipe_no as recipeNo, f.reg_dpcd as dept,f.doct_code emp from "+HisParameters.HISPARSCHEMAHISUSER+"T_OUTPATIENT_FEEDETAIL_NOW f where f.pay_flag=0 and f.del_flg=0 and f.stop_flg=0 and f.clinic_code in ('"+clinicCode+"')";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("recipeNo").addScalar("dept").addScalar("emp");
		List<RecipeNoVo> recipeNoVoList = queryObject.setResultTransformer(Transformers.aliasToBean(RecipeNoVo.class)).list();
		if(recipeNoVoList==null||recipeNoVoList.size()<=0){
			return new ArrayList<RecipeNoVo>();
		}
		return recipeNoVoList;
	}

	@Override
	public List<OutpatientFeedetailNow> queryFeedetailList(String recipeNo) {
		String hql = "from OutpatientFeedetailNow where recipeNo ='"+recipeNo+"' and del_flg=0 and stop_flg=0  and payFlag=0  ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public BusinessContractunit queryCountByPaykindCode(String count) {
		String hql = " from BusinessContractunit where encode = '"+count+"' and del_flg=0 and stop_flg=0";
		List<BusinessContractunit> contractunit = super.find(hql, null);
		if(contractunit==null||contractunit.size()<=0){
			return new BusinessContractunit();
		}
		return contractunit.get(0);
	}

	@Override
	public List<OutpatientFeedetailNow> findFeedetailDetails(String recipeNo) {
		String hql = "from OutpatientFeedetailNow where recipeNo in ('"+recipeNo+"') and del_flg=0 and stop_flg=0 and payFlag=0 order by combNo asc";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public MinfeeStatCode minfeeStatCodeByEncode(String encode) {
		String hql = " from MinfeeStatCode where minfeeCode  ='"+encode+"' and del_flg=0 and stop_flg=0 ";
		List<MinfeeStatCode> minfeeStatCodeList = super.find(hql, null);
		if(minfeeStatCodeList==null||minfeeStatCodeList.size()<=0){
			return new MinfeeStatCode();
		}
		return minfeeStatCodeList.get(0);
	}

	@Override
	public List<SysDepartment> quertComboboxDept() {
		String hql = " from SysDepartment where del_flg=0 and stop_flg=0 and deptIsforregister = '1'";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return departmentList;
	}

	@Override
	public int getTotalUndrug(String undrugCodes) {
		String sql = "select count(1) as totle from t_drug_undrug u where u.stop_flg = 0 and u.del_flg = 0 ";
		sql= this.joint(undrugCodes,sql);
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("totle",Hibernate.INTEGER);
		List<UndrugAndWare> undrugAndWareList = queryObject.setResultTransformer(Transformers.aliasToBean(UndrugAndWare.class)).list();
		if(undrugAndWareList!=null&&undrugAndWareList.size()>0){
			return undrugAndWareList.get(0).getTotle();
		}
		return 0;
	}

	@Override
	public List<UndrugAndWare> findUndrugAndWareList(String undrugCodes,String page,String rows) {
		String sql = "select u.undrug_id as undrugCode,u.undrug_name as undrugName,u.undrug_pinyin as pin,u.undrug_wb as wb,u.undrug_inputcode as undrugInputcode, u.undrug_dept as undrugDept,u.undrug_systype as undrugSystype, u.undrug_minimumcost as undrugMinCode,u.undrug_spec as spec,u.undrug_unit as unit, u.undrug_defaultprice as defaultprice, u.undrug_notes as undrugNotes,u.undrug_issubmit as undrugIssubmit,"+
                     " u.undrug_issubmit as issubmit "+
                     " from t_drug_undrug u where u.stop_flg = 0 and u.del_flg = 0 ";
		sql= this.joint(undrugCodes,sql);
		SQLQuery queryObject = this.getSession().createSQLQuery(sql).addScalar("undrugCode").addScalar("undrugName").addScalar("pin").addScalar("wb").addScalar("undrugInputcode").addScalar("undrugDept")
				.addScalar("undrugSystype").addScalar("undrugMinCode").addScalar("spec").addScalar("unit").addScalar("defaultprice",Hibernate.DOUBLE).addScalar("undrugNotes").addScalar("undrugIssubmit",Hibernate.INTEGER).addScalar("issubmit",Hibernate.INTEGER);
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		List<UndrugAndWare> undrugAndWareList = queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(UndrugAndWare.class)).list();
		if(undrugAndWareList!=null&&undrugAndWareList.size()>0){
			return undrugAndWareList;
		}
		return new ArrayList<UndrugAndWare>();
	}
	
	/**  
	 * @Description： 查询条件拼接
	 * @Author：ldl
	 * @CreateDate：2016-04-01
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	public String joint(String undrugCodes,String sql){
		if(StringUtils.isNotBlank(undrugCodes)){
			sql = sql+" AND (u.undrug_name) LIKE '%"+undrugCodes+"%'"+
	          " or u.undrug_pinyin LIKE '%"+undrugCodes+"%'"+
	          " or u.undrug_wb LIKE '%"+undrugCodes+"%'"+
	          " or u.undrug_inputcode LIKE '%"+undrugCodes+"%'";
		}
		return sql;
	}

	@Override
	public List<MedicalVo> findOdditionalitemByTypeCode(String undrugId) {
		String hql = "select DISTINCT d.undrug_id as FeedetailId,d.undrug_name as adviceName,d.undrug_minimumcost as feeCode,o.qty as totalNum,d.undrug_unit as unit,o.total_price as adPriceSum,o.price as adPrice,d.undrug_remark as remark,d.undrug_systype as sysType,m.fee_stat_code as feeStatCode,m.fee_stat_name as feeStatName  from t_business_odditionalitem o left join t_drug_undrug d on o.item_code=d.undrug_id left join t_business_dictionary p on  d.undrug_minimumcost = p.code_encode left join t_charge_minfeetostat m on p.code_encode = m.minfee_code  where o.type_code = '"+undrugId+"' and p.code_type = 'drugMinimumcost' and o.stop_flg=0 and o.del_flg=0 ";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("FeedetailId").addScalar("adviceName").addScalar("feeCode").addScalar("totalNum",Hibernate.INTEGER).addScalar("unit").addScalar("adPriceSum",Hibernate.DOUBLE).addScalar("adPrice",Hibernate.DOUBLE).addScalar("remark").addScalar("feeStatCode").addScalar("feeStatName");
		List<MedicalVo> medicalVoList = queryObject.setResultTransformer(Transformers.aliasToBean(MedicalVo.class)).list();
		if(medicalVoList!=null&&medicalVoList.size()>0){
			  return medicalVoList;
		  }
		return new ArrayList<MedicalVo>();
	}

	@Override
	public List<BusinessFrequency> findFrequency() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.frequency_encode as encode,t.frequency_name as name from t_business_frequency t Where t.del_flg = 0 And t.stop_flg = 0 ");
		List<BusinessFrequency> frequencyList = namedParameterJdbcTemplate.query(sb.toString(),new RowMapper<BusinessFrequency>() {

			@Override
			public BusinessFrequency mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				BusinessFrequency bf = new BusinessFrequency();
				bf.setEncode(rs.getString("encode"));
				bf.setName(rs.getString("name"));
				return bf;
			}
		});
//		String hql = "from BusinessFrequency where del_flg=0 and stop_flg=0 ";
//		List<BusinessFrequency> frequencyList = super.find(hql, null);
		if(frequencyList==null||frequencyList.size()<=0){
			return new ArrayList<BusinessFrequency>();
		}
		return frequencyList;
	}

	@Override
	public RegistrationNow findRegisterInfoByNo(String clinicCode) {
		String hql = " from RegistrationNow where clinicCode = '"+clinicCode+"' and del_flg=0 and stop_flg=0";
		List<RegistrationNow> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new RegistrationNow();
		}
		return infoList.get(0);
	}

	@Override
	public OutpatientFeedetailNow queryNOByclinicCode(String clinicCode) {
		String hql = " from OutpatientFeedetailNow where clinicCode = '"+clinicCode+"'  and del_flg=0 and stop_flg=0  ";
		List<OutpatientFeedetailNow> outpatientFeedetailList = super.find(hql, null);
		if(outpatientFeedetailList==null||outpatientFeedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return outpatientFeedetailList.get(0);
	}

	@Override
	public List<SysDepartment> queryEdComboboxDept() {
		String hql = " from SysDepartment where del_flg=0 and stop_flg=0 ";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return departmentList;
	}

	@Override
	public RegistrationNow queryInfoByNo(String clinicCode) {
		String hql = " from RegistrationNow where no = '"+clinicCode+"' and del_flg=0 and stop_flg=0";
		List<RegistrationNow> registerInfoList = super.find(hql, null);
		if(registerInfoList==null||registerInfoList.size()<=0){
			return new RegistrationNow();
		}
		return registerInfoList.get(0);
	}

	@Override
	public List<SysDepartment> chargeImplementDepartmentList() {
		String hql = " from SysDepartment where del_flg=0 and stop_flg=0 and deptType = 'T' ";
		List<SysDepartment> departmentList = super.find(hql, null);
		if(departmentList==null||departmentList.size()<=0){
			return new ArrayList<SysDepartment>();
		}
		return departmentList;
	}

	@Override
	public OutpatientFeedetailNow queryFeedetailInvoiceNo(String no) {
		String hql = " from OutpatientFeedetailNow where clinicCode = '"+no+"' and del_flg=0 and stop_flg=0";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}

	@Override
	public List<OutpatientRecipedetailNow> queryRecipedetailList(String recipedetailIds) {
		String hql = " from OutpatientRecipedetailNow where recipeNo in ('"+recipedetailIds+"')";
		List<OutpatientRecipedetailNow> recipedetailList = super.find(hql, null);
		if(recipedetailList==null||recipedetailList.size()<=0){
			return new ArrayList<OutpatientRecipedetailNow>();
		}
		return recipedetailList;
	}

	@Override
	public List<OutpatientFeedetailNow> saveOrUpdateFeedetailListNot(String feedetaiNotIds) {
		String hql = " from OutpatientFeedetailNow where del_flg=0 and stop_flg=0 and drugFlag=0 and id in ('"+feedetaiNotIds+"')";
		List<OutpatientFeedetailNow> feedetailListNot = super.find(hql, null);
		if(feedetailListNot==null||feedetailListNot.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailListNot;
	}

	@Override
	public List<OutpatientFeedetailNow> saveOrUpdateFeedetailList(String feedetailIds) {
		String hql = " from OutpatientFeedetailNow where id in ('"+feedetailIds+"')";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public List<OutpatientFeedetailNow> queryFeedetailRecipeNo(String recipeNoArr) {
		String hql = " from OutpatientFeedetailNow where recipeNo = '"+recipeNoArr+"' and drugFlag = '1' and del_flg=0 and stop_flg=0";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public StoTerminal queryStoTerminal(String execDpcd) {
		String hql = " from StoTerminal where deptCode = '"+execDpcd+"' and type='1' and closeFlag='0' and  del_flg=0 and stop_flg=0  order by drugQty ";
		List<StoTerminal> stoTerminalList = super.find(hql, null);
		if(stoTerminalList==null||stoTerminalList.size()<=0){
			return new StoTerminal();
		}
		return stoTerminalList.get(0);
	}

	@Override
	public StoTerminal queryStoTerminalNo(String id) {
		String hql = " from StoTerminal where id = '"+id+"' and type='1'  and  del_flg=0 and stop_flg=0  ";
		List<StoTerminal> stoTerminalList = super.find(hql, null);
		if(stoTerminalList==null||stoTerminalList.size()<=0){
			return new StoTerminal();
		}
		return stoTerminalList.get(0);
	}

	@Override
	public OutpatientFeedetailNow queryDrugInfoList(String feedetailIdsArr) {
		String hql = " from OutpatientFeedetailNow where id = '"+feedetailIdsArr+"' and del_flg=0 and stop_flg=0 ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}

	@Override
	public DrugInfo queryDrugInfoById(String itemCode) {
		String hql = " from DrugInfo where code = '"+itemCode+"'  and del_flg=0 and stop_flg=0 ";
		List<DrugInfo> drugInfoList = super.find(hql, null);
		if(drugInfoList==null||drugInfoList.size()<=0){
			return new DrugInfo();
		}
		return drugInfoList.get(0);
	}

	@Override
	public void saveInvoiceFinance(String id, String invoiceNo,String invoiceType) {
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
	public List<OutpatientFeedetailNow> findDrugStorage(String feedetailIds) {
		String hql = " from OutpatientFeedetailNow where id in ('"+feedetailIds+"') and  del_flg=0 and stop_flg=0 and drugFlag='1' ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	@Override
	public DrugStockinfo findStockinfoList(String execDpcd, String itemCode) {
		String hql = " from DrugStockinfo where storageDeptid ='"+execDpcd+"' and drugId = '"+itemCode+"'  and del_flg=0 and stop_flg=0";
		List<DrugStockinfo> stockinfoList = super.find(hql, null);
		if(stockinfoList==null||stockinfoList.size()<=0){
			return new DrugStockinfo();
		}
		return stockinfoList.get(0);
	}

	@Override
	public DrugInfo findDruginfoList(String itemCode) {
		String hql = " from DrugInfo where code ='"+itemCode+"' and del_flg=0 and stop_flg=0 ";
		List<DrugInfo> infoList = super.find(hql, null);
		if(infoList==null||infoList.size()<=0){
			return new DrugInfo();
		}
		return infoList.get(0);
	}

	@Override
	public OutpatientAccount getAccountByMedicalrecord(String midicalrecordId) {
		String hql = " from OutpatientAccount where medicalrecordId ='"+midicalrecordId+"' and del_flg=0 and stop_flg=0 ";
		List<OutpatientAccount> accountList = super.find(hql, null);
		if(accountList==null||accountList.size()<=0){
			return new OutpatientAccount();
		}
		return accountList.get(0);
	}

	@Override
	public List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId) {
		String hql = " from OutpatientAccountrecord where medicalrecordId ='"+midicalrecordId+"' and del_flg=0 and stop_flg=0 and TO_CHAR(operDate,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(DateUtils.getCurrentTime())+"'";
		List<OutpatientAccountrecord> accountrecordList = super.find(hql, null);
		if(accountrecordList==null||accountrecordList.size()<=0){
			return new ArrayList<OutpatientAccountrecord>();
		}
		return accountrecordList;
	}

	@Override
	public OutpatientAccount veriPassWord(String md5Hex, String patientNo) {
		String hql = " from OutpatientAccount where medicalrecordId = '"+patientNo+"' and accountPassword='"+md5Hex+"'  and  del_flg=0 and stop_flg=0  ";
		List<OutpatientAccount> patientAccountList = super.find(hql, null);
		if(patientAccountList==null||patientAccountList.size()<=0){
			return new OutpatientAccount();
		}
		return patientAccountList.get(0);
	}

	@Override
	public SpeNalVo querySpeNalVoBy(String execDpcd, String id,Integer itemType) {
		String hql = "select t.t_code as tCode,t.item_code as itemCode,t.item_type as itemType,a.dept_code as deptCode from  t_sto_terminal_spe t left join t_sto_terminal a on t.t_code = a.id where t.item_type = '"+itemType+"' and t.item_code ='"+id+"' and a.dept_code='"+execDpcd+"'";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("tCode").addScalar("itemCode").addScalar("itemType",Hibernate.INTEGER).addScalar("deptCode");
		List<SpeNalVo> SpeNalVoList = queryObject.setResultTransformer(Transformers.aliasToBean(SpeNalVo.class)).list();
		if(SpeNalVoList!=null&&SpeNalVoList.size()>0){
			  return SpeNalVoList.get(0);
		  }
		return new SpeNalVo();
	}

	@Override
	public OutpatientFeedetailNow queryOutFeedetail(String ids) {
		String hql = " from OutpatientFeedetailNow where id = '"+ids+"' and  del_flg=0 and stop_flg=0  ";
		List<OutpatientFeedetailNow>  feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new OutpatientFeedetailNow();
		}
		return feedetailList.get(0);
	}

	@Override
	public DrugUndrug queryUnDrugById(String feedetailId) {
		String hql = " from DrugUndrug where id = '"+feedetailId+"' and  del_flg=0 and stop_flg=0  ";
		List<DrugUndrug> undrugList = super.find(hql, null);
		if(undrugList==null||undrugList.size()<=0){
			return new DrugUndrug();
		}
		return undrugList.get(0);
	}

	@Override
	public List<OutpatientFeedetailNow> findMatFee(String feedetaiNotIds) {
		String hql = " from OutpatientFeedetailNow where id in ('"+feedetaiNotIds+"')  and  del_flg=0 and stop_flg=0  ";
		List<OutpatientFeedetailNow> feedetailList = super.find(hql, null);
		if(feedetailList==null||feedetailList.size()<=0){
			return new ArrayList<OutpatientFeedetailNow>();
		}
		return feedetailList;
	}

	
}
