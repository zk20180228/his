package cn.honry.inpatient.exitNoFee.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.BusinessMedicalGroupInfo;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientHangbedinfo;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InsuranceSiitem;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
import cn.honry.inpatient.exitNoFee.dao.ExitNoFeeDAO;
import cn.honry.inpatient.exitNoFee.dao.InpatientHangbedinfoDAO;
import cn.honry.inpatient.exitNoFee.vo.InpatientInfoVo;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.MD5;
import cn.honry.utils.ShiroSessionUtils;

@Repository("exitNoFeeDAO")
@SuppressWarnings({"all"})
public class ExitNoFeeDAOImpl extends HibernateEntityDao<InpatientInfo> implements ExitNoFeeDAO{
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private HospitalbedInInterDAO hospitalbedDAO;
	@Autowired
	private InpatientInfoDAO inpatientInfoDAO;
	@Autowired
	private InpatientHangbedinfoDAO inpatientHangbedinfoDAO;
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}

	@Override
	public List<InpatientInfoVo> getInpatientInfo(InpatientInfoNow entity) {		
		String sql = "select t.IDCARD_NO as idcardNo,t.INPATIENT_ID as id,"
				+ "t.INPATIENT_NO as inpatientNo,"
				+ "t.MEDICALRECORD_ID as medicalrecordId,"
				+ "t.PATIENT_NAME as patientName,"
				+ "t.IN_DATE as inDate,"
				+ "t.PACT_CODE as pactCode,"
				+ "t.DEPT_CODE as deptCode,"
				+ "t.BEDINFO_ID as bedId,"
				+ "t.nurse_cell_code as nurseCellCode,"
				+ "t.PREPAY_COST as prepayCost,"
				+ "t.PAY_COST             as payCost,"
				+ "t.OWN_COST             as ownCost,"
				+ "t.TOT_COST             as totCost,"
				+ "t.PUB_COST             as pubCost,"
				+ "t.FREE_COST            as freeCost,"
				+ "t.IN_STATE             as inState,"
				+ "t.bed_name             as bedName,"
				+ "t.DEPT_NAME            as deptName,"
				+ "t.nurse_cell_name      as nurseCellName,"
				+ "t.HOUSE_DOC_NAME      as houseDocName ,  "
				+ "t.DEPT_NAME as deptName , "
				+" t.report_sex_name as reportSexName ," 
				+" t.report_age as reportAge, t.REPORT_AGEUNIT as reportAgeUnit,"
				+" t.out_date as outDate,t.REPORT_BIRTHDAY as reportBirthday "
				+ "from T_INPATIENT_INFO_NOW t   "
				+ "where 1=1 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				sql = sql+" AND (t.MEDICALRECORD_ID = :inpatientNo )";
				sql = sql+" ORDER BY t.IN_DATE desc nulls last";

			}
			if(StringUtils.isNotBlank(entity.getInpatientNo())){
				sql = sql+" AND (t.INPATIENT_NO = :inpatientNo )";
				sql = sql+" ORDER BY t.IN_DATE desc nulls last";

			}
			if(StringUtils.isNotBlank(entity.getId())){
				sql = sql+" AND t.INPATIENT_NO = :id";
				sql = sql+" and (t.in_state ='R' or t.in_state ='I') ORDER BY t.IN_DATE desc nulls last";
			}
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				paraMap.put("inpatientNo", entity.getMedicalrecordId());
			}
			if(StringUtils.isNotBlank(entity.getInpatientNo())){
				paraMap.put("inpatientNo", entity.getInpatientNo());
			}
			if(StringUtils.isNotBlank(entity.getId())){
				paraMap.put("id", entity.getId());
			}
		}
		List<InpatientInfoVo> inpatientInfo =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientInfoVo>() {
			@Override
			public InpatientInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoVo inpatientInfoVo = new InpatientInfoVo();
				inpatientInfoVo.setIdcardNo(rs.getString("idcardNo"));
				inpatientInfoVo.setId(rs.getString("id"));
				inpatientInfoVo.setInpatientNo(rs.getString("inpatientNo"));
				inpatientInfoVo.setMedicalrecordId(rs.getString("medicalrecordId"));
				inpatientInfoVo.setPatientName(rs.getString("patientName"));
				inpatientInfoVo.setInDate(rs.getDate("inDate"));
				inpatientInfoVo.setPactCode(rs.getString("pactCode"));
				inpatientInfoVo.setDeptCode(rs.getString("deptCode"));
				inpatientInfoVo.setBedId(rs.getString("bedId"));
				inpatientInfoVo.setNurseCellCode(rs.getString("nurseCellCode"));
				inpatientInfoVo.setPrepayCost(rs.getObject("prepayCost")==null?null:rs.getDouble("prepayCost"));
				inpatientInfoVo.setPayCost(rs.getObject("payCost")==null?null:rs.getDouble("payCost"));
				inpatientInfoVo.setOwnCost(rs.getObject("ownCost")==null?null:rs.getDouble("ownCost"));
				inpatientInfoVo.setTotCost(rs.getObject("totCost")==null?null:rs.getDouble("totCost"));
				inpatientInfoVo.setPubCost(rs.getObject("pubCost")==null?null:rs.getDouble("pubCost"));
				inpatientInfoVo.setFreeCost(rs.getObject("freeCost")==null?null:rs.getDouble("freeCost"));
				inpatientInfoVo.setInState(rs.getString("inState"));
				inpatientInfoVo.setBedName(rs.getString("bedName"));
				inpatientInfoVo.setDeptName(rs.getString("deptName"));
				inpatientInfoVo.setNurseCellName(rs.getString("nurseCellName"));
				inpatientInfoVo.setHouseDocName(rs.getString("houseDocName"));
				inpatientInfoVo.setDeptName(rs.getString("deptName"));
				inpatientInfoVo.setReportSexName(rs.getString("reportSexName"));
				inpatientInfoVo.setOutDate(rs.getDate("outDate"));
				inpatientInfoVo.setReportBirthday(rs.getDate("reportBirthday"));
				inpatientInfoVo.setReportAge(rs.getObject("reportAge")==null?null:rs.getInt("reportAge"));
				inpatientInfoVo.setReportAgeUnit(rs.getString("reportAgeUnit"));
				return inpatientInfoVo;
			}
		});
		if(inpatientInfo!=null && inpatientInfo.size()>0){
			return inpatientInfo;
		}		
		return new ArrayList<InpatientInfoVo>();
	}

	@Override
	public String changeHospitalState(String ids) throws Exception {
		String flag="0";
		String[] idsArg= ids.split(",");
		if(idsArg.length==1){
			String sql="select * from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_hospitalbed b "
					+ "where b.BED_ID=(select t.bed_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info_now t "
							+ "where t.inpatient_id = '"+ids+"')";
			String sql1="select * from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info_now t "
					+ "where t.inpatient_id = '"+ids+"'";
			String sql2="select * from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_hangbedinfo c "
					+ "where c.inpatient_no =(select t.inpatient_no from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info_now t "
							+ "where t.inpatient_id = '"+ids+"')";
			BusinessHospitalbed hospitalbed = (BusinessHospitalbed) getSession().createSQLQuery(sql).addEntity(BusinessHospitalbed.class).uniqueResult();
			InpatientInfoNow inpatientInfo = (InpatientInfoNow) getSession().createSQLQuery(sql1).addEntity(InpatientInfoNow.class).uniqueResult();
			InpatientHangbedinfo inpatientHangbedinfo = (InpatientHangbedinfo) getSession().createSQLQuery(sql2).addEntity(InpatientHangbedinfo.class).uniqueResult();
			
			inpatientInfo.setInState("N");
			inpatientInfoDAO.upd(ids,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		
			if(hospitalbed!=null){
				hospitalbed.setBedState("1");
				hospitalbed.setPatientId("");				
				hospitalbedDAO.saveOrUpdate(hospitalbed);				
				hospitalbedDAO.upd(hospitalbed.getId(), ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			}
			if(inpatientHangbedinfo!=null){
				inpatientHangbedinfo.setStatus(1);
				inpatientHangbedinfo.setBedKind(null);
				inpatientHangbedinfo.setHospitalId(HisParameters.CURRENTHOSPITALID);
				inpatientHangbedinfo.setAreaCode(inprePayDAO.getDeptArea(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode()));
				inpatientHangbedinfoDAO.upd(inpatientHangbedinfo.getId(), ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			}
		}else{
			for(int i=0;i<idsArg.length;i++){
				String sql="select * from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_hospitalbed b "
						+ "where b.BED_ID=(select t.bed_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info t "
								+ "where t.inpatient_id = '"+idsArg[i]+"')";
				
				BusinessHospitalbed hospitalbed = (BusinessHospitalbed) getSession().createSQLQuery(sql).addEntity(BusinessHospitalbed.class).uniqueResult();

				if(hospitalbed!=null){
					hospitalbed.setBedState("1");
					hospitalbedDAO.saveOrUpdate(hospitalbed);
				}
				inpatientInfoDAO.del(ids,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());

			}
		}
		return flag;
	}

	@Override
	public List<InpatientInfoVo> queryInpatientInfoById(String id) {
	
	       String sql="select t.INPATIENT_ID as id, t.INPATIENT_NO  as inpatientNo, t.MEDICALRECORD_ID  as medicalrecordId, t.PATIENT_NAME as patientName, t.IN_DATE as inDate,  t.PACT_CODE as pactCode, t.DEPT_CODE as deptCode, t.BEDINFO_ID as bedId, t.nurse_cell_code as nurseCellCode,";
	       sql+="t.PREPAY_COST  as prepayCost, t.PAY_COST as payCost, t.OWN_COST as ownCost, t.TOT_COST as totCost, t.PUB_COST as pubCost, t.FREE_COST as freeCost,t.IN_STATE as inState, t.bed_name   as bedName, t.DEPT_NAME  as deptName,t.nurse_cell_name as nurseCellName from HONRYHIS.T_INPATIENT_INFO_NOW t";
	       sql+= " where 1=1 ";
	       if(StringUtils.isNotBlank(id)){
			sql =sql+" and t.INPATIENT_ID=:id";
	       }
	       Map<String, Object> paraMap = new HashMap<String, Object>();
	       if(StringUtils.isNotBlank(id)){
		       paraMap.put("id", id);
	       }
	       List<InpatientInfoVo> inpatientInfo =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientInfoVo>() {

			@Override
			public InpatientInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoVo vo = new InpatientInfoVo();
				vo.setId(rs.getString("id"));
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setInDate(rs.getTimestamp("inDate"));
				vo.setPactCode(rs.getString("pactCode"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setBedId(rs.getString("bedId"));
				vo.setNurseCellCode(rs.getString("nurseCellCode"));
				vo.setPrepayCost(rs.getObject("prepayCost")==null?null:rs.getDouble("prepayCost"));
				vo.setPayCost(rs.getObject("payCost")==null?null:rs.getDouble("payCost"));
				vo.setOwnCost(rs.getObject("ownCost")==null?null:rs.getDouble("ownCost"));
				vo.setTotCost(rs.getObject("totCost")==null?null:rs.getDouble("totCost"));
				vo.setPubCost(rs.getObject("pubCost")==null?null:rs.getDouble("pubCost"));
				vo.setFreeCost(rs.getObject("freeCost")==null?null:rs.getDouble("freeCost"));
				vo.setInState(rs.getString("inState"));
				vo.setBedName(rs.getString("bedName"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setNurseCellName(rs.getString("nurseCellName"));
				return vo;
			}
	    	   
	       });
		if(inpatientInfo!=null && inpatientInfo.size()>0){
			return inpatientInfo;
		}		
		return new ArrayList<InpatientInfoVo>();
	}

	@Override
	public List<InpatientShiftData> queryMaxHappenNo() {
		String hql="from InpatientShiftData t where t.happenNo in(select max(b.happenNo) from InpatientShiftData b)";
		List<InpatientShiftData> inpatientShiftData=this.getSession().createQuery(hql).list();
		if(inpatientShiftData!=null && inpatientShiftData.size()>0){
			return inpatientShiftData;
		}		
		return new ArrayList<InpatientShiftData>();		
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String inpatientNo) {
		String hql = "from InpatientInfoNow t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(inpatientNo)){
			hql = hql+" and t.inpatientNo = '"+inpatientNo+"'";
		}
		List<InpatientInfoNow> inpatientInfo=this.getSession().createQuery(hql).list();
		if(inpatientInfo!=null && inpatientInfo.size()>0){
			return inpatientInfo;
		}		
		return new ArrayList<InpatientInfoNow>();	
	}

	@Override
	public List<DrugInfo> queryDrugInfo(String id) {
		String hql = "from DrugInfo t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(id)){
			hql =hql+" and t.id='"+id+"'";
		}
		List<DrugInfo> drugInfo=this.getSession().createQuery(hql).list();
		if(drugInfo!=null && drugInfo.size()>0){
			return drugInfo;
		}		
		return new ArrayList<DrugInfo>();
	}

	@Override
	public List<BusinessMedicalGroupInfo> queryMedicalGroupInfo(String docCode) {
		String hql = "from BusinessMedicalGroupInfo t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(docCode)){
			hql =hql+" and t.doctorId='"+docCode+"'";
		}
		List<BusinessMedicalGroupInfo> businessMedicalGroupInfo=this.getSession().createQuery(hql).list();
		if(businessMedicalGroupInfo!=null && businessMedicalGroupInfo.size()>0){
			return businessMedicalGroupInfo;
		}		
		return new ArrayList<BusinessMedicalGroupInfo>();
	}

	@Override
	public List<BusinessContractunit> queryContractunit(String paykindCode) {
		String hql = "from BusinessContractunit t where t.stop_flg = 0 and t.del_flg = 0 "
				+ "and t.paykindCode in(select i.encode from CodeSettlement i where i.id = '"+paykindCode+"' )";	
		List<BusinessContractunit> businessContractunit=this.getSession().createQuery(hql).list();
		if(businessContractunit!=null && businessContractunit.size()>0){
			return businessContractunit;
		}		
		return new ArrayList<BusinessContractunit>();
	}

	@Override
	public List<InpatientMedicineList> queryInpatientMedicineList(String recipeNo) {
		String hql = "from InpatientMedicineList t where t.sequenceNo in(select max(b.sequenceNo) "
				+ "from InpatientMedicineList b where b.recipeNo='"+recipeNo+"') and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientMedicineList> inpatientMedicineList=this.getSession().createQuery(hql).list();
		if(inpatientMedicineList!=null && inpatientMedicineList.size()>0){
			return inpatientMedicineList;
		}		
		return new ArrayList<InpatientMedicineList>();
	}

	@Override
	public List<InpatientItemList> queryInpatientItemList(String recipeNo) {
		String hql = "from InpatientItemList t where t.sequenceNo in(select max(b.sequenceNo) "
				+ "from InpatientItemList b where b.recipeNo='"+recipeNo+"') and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientItemList> inpatientItemList=this.getSession().createQuery(hql).list();
		if(inpatientItemList!=null && inpatientItemList.size()>0){
			return inpatientItemList;
		}		
		return new ArrayList<InpatientItemList>();
	}

	@Override
	public List<InpatientFeeInfo> queryInpatientFeeInfo(String recipeNo) {
		String hql = "from InpatientFeeInfo t where t.stop_flg = 0 and t.del_flg = 0 and t.recipeNo='"+recipeNo+"'";	
		List<InpatientFeeInfo> inpatientFeeInfo=this.getSession().createQuery(hql).list();
		if(inpatientFeeInfo!=null && inpatientFeeInfo.size()>0){
			return inpatientFeeInfo;
		}		
		return new ArrayList<InpatientFeeInfo>();
	}

	@Override
	public List<InsuranceSiitem> queryInsuranceSiitem(String itemCode) {
		String hql = "from InsuranceSiitem t where t.stop_flg = 0 and t.del_flg = 0 and t.itemCode "
				+ "in(select i.centerCode from InsuranceCompare i where i.hisCode='"+itemCode+"')";
		List<InsuranceSiitem> insuranceSiitem=this.getSession().createQuery(hql).list();
		if(insuranceSiitem!=null && insuranceSiitem.size()>0){
			return insuranceSiitem;
		}		
		return new ArrayList<InsuranceSiitem>();
	}

	@Override
	public List<InpatientSurety> querysuretyCost(String inpatientNo) {
		String hql = "from InpatientSurety t where t.stop_flg = 0 and t.del_flg = 0 "
				+ "and t.inpatientNo ='"+inpatientNo+"'";
		List<InpatientSurety> inpatientSurety=this.getSession().createQuery(hql).list();
		if(inpatientSurety!=null && inpatientSurety.size()>0){
			return inpatientSurety;
		}		
		return new ArrayList<InpatientSurety>();
	}

	@Override
	public List<User> confirmPassword(User user) {
		String hql = "from User t where t.stop_flg = 0 and t.del_flg = 0 and t.account='"+user.getId()+"' "
				+ "and t.password='"+MD5.MD5Encode(user.getPassword())+"'";
		List<User> userList=this.getSession().createQuery(hql).list();
		if(userList!=null && userList.size()>0){
			return userList;
		}	
		return new ArrayList<User>();
	}

	@Override
	public List<InpatientMedicineList> queryInpatientMedicineList(String recipeNo, Integer sequenceNo) {
		String hql = "from InpatientMedicineList t where t.recipeNo ='"+recipeNo+"' "
				+ "and t.sequenceNo ="+sequenceNo+" and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientMedicineList> inpatientMedicineList=this.getSession().createQuery(hql).list();
		if(inpatientMedicineList!=null && inpatientMedicineList.size()>0){
			return inpatientMedicineList;
		}		
		return new ArrayList<InpatientMedicineList>();
	}

	@Override
	public List<InpatientItemList> queryInpatientItemList(String recipeNo,Integer sequenceNo) {
		String hql = "from InpatientItemList t where t.recipeNo='"+recipeNo+"' "
				+ "and t.sequenceNo ="+sequenceNo+" and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientItemList> inpatientItemList=this.getSession().createQuery(hql).list();
		if(inpatientItemList!=null && inpatientItemList.size()>0){
			return inpatientItemList;
		}		
		return new ArrayList<InpatientItemList>();
	}

	@Override
	public String queryInpatientNoByMid(String medicalrecordId) {
		
		String sql = "select t.INPATIENT_NO from T_INPATIENT_INFO_NOW t "
				+ "where t.MEDICALRECORD_ID = :medicalrecordId and t.IN_STATE = 'R'";
		Query queryObj = this.getSession().createSQLQuery(sql).setParameter("medicalrecordId", medicalrecordId);
		String inpatientNo = (String) queryObj.list().get(0);
		if(StringUtils.isNotBlank(inpatientNo)){
			return inpatientNo;
		}
		return null;
	}

	@Override
	public String isExitNoFee(String inpatientNo) {
		String sql ="select t.ID as id from T_INPATIENT_FEEINFO_NOW t where t.INPATIENT_NO =:inpatientNo";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientFeeInfoNow> inpatientFeeInfoNow =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientFeeInfoNow>() {

			@Override
			public InpatientFeeInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientFeeInfoNow inpatientFeeInfoNow = new InpatientFeeInfoNow();
				inpatientFeeInfoNow.setId(rs.getString("id"));
				return inpatientFeeInfoNow;
			}
			
		});
		if(inpatientFeeInfoNow!=null && inpatientFeeInfoNow.size()>0){
			return "have";
		}else{
			return "none";
		}
	}

}
