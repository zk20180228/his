package cn.honry.inpatient.info.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.hospitalbed.dao.HospitalbedInInterDAO;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.info.dao.InpatientInfoNowDAO;
import cn.honry.inpatient.info.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;


/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
@Repository("inpatientInfoDAO")
@SuppressWarnings({"all"})
public  class InpatientInfoDAOImpl extends HibernateEntityDao<InpatientInfoNow> implements InpatientInfoDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterDAO;//医院参数dao
	@Autowired
	@Qualifier(value = "inpatientInfoNowDAO")
	private InpatientInfoNowDAO inpatientInfoNowDAO;//医院参数dao
	
	@Autowired
	private PatinmentInnerDao patinmentDao;
	@Autowired
	@Qualifier(value = "hospitalbedInInterDAO")
	private HospitalbedInInterDAO hospitalbedDAO;
	@Override
	public List<InpatientInfoNow> getPage(String page, String rows, InpatientInfoNow entity) {
		String hql = joint(entity);
		return inpatientInfoNowDAO.getinfopage(hql, page, rows);
	}

	@Override
	public int getTotal(InpatientInfoNow entity) {
		String hql = joint(entity);
		return inpatientInfoNowDAO.getinfoTotal(hql);
	}
	
	public String joint(InpatientInfoNow entity){
		String hql="FROM InpatientInfoNow i WHERE 1=1 ";
		// 修改--模糊查询
		//zhenglin
		//2015-2-3
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
				hql = hql+" AND i.medicalrecordId ='"+entity.getMedicalrecordId()+"' ";
			}
		}
		hql = hql+" ORDER BY i.id";
		return hql;
	}
	
	@Override
	public InpatientInfoNow queryByMedical(String medicalNo) {
		String hql="FROM InpatientInfoNow i WHERE i.medicalrecordId = :medicalNo ";
		List<InpatientInfoNow> list = this.getSession().createQuery(hql).setParameter("medicalNo", medicalNo).list();
		return list.size()==0 ? null : list.get(0);
	}

	/**  
	 *  
	 * @Description：  查询住院患者
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryPatient() {
		String hql = "from InpatientInfoNow i"; 
		List<InpatientInfoNow> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	/**  
	 *  
	 * @Description：  查询出院登记
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-26 下午02:06:02  
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-04 下午02:00:00  
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-21 下午02:00:00 
	 * @ModifyRmk： 根据病历号和住院号模糊查询 
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryInpinfo(String type, String no) {
	      String hql ="select distinct i.INPATIENT_NO as id,i.TOT_COST as totCost,"
					+ "i.MEDICALRECORD_ID as medicalrecordId,i.PATIENT_NAME as patientName,"
					+ "i.REPORT_SEX as reportSex,i.REPORT_SEX_NAME as reportSexName ,i.IN_DATE as inDate,i.PAYKIND_CODE as paykindCode,"
					+ "i.bed_Name as bedId,i.PREPAY_OUTDATE as prepayOutdate,i.IDCARD_NO as idcardNo,i.REPORT_AGE as reportAge,i.REPORT_BIRTHDAY as reportBirthday, " 
	    	        +" b.DEPT_NAME as deptName,i.FREE_COST as balance,";
					if(type!=""&&type!=null){
					    hql+="i.BALANCE_COST as accountAmount,i.CERTIFICATES_NO as certificatesNo, s.bed_name as bedName"
					    + " from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i left join "
					    + ""+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT b on b.DEPT_CODE=i.DEPT_CODE left join "
					    + ""+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_HOSPITALBED s on s.bed_id = i.bed_id "
					    + "where i.MEDICALRECORD_ID = :type  and i.IN_STATE='I'";
					}else{
						hql+="i.BALANCE_COST as accountAmount,i.CERTIFICATES_NO as certificatesNo, s.bed_name as bedName"
							    + " from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i left join "
							     + ""+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT b on b.DEPT_CODE=i.DEPT_CODE left join "
					     + ""+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_HOSPITALBED s on s.bed_id = i.bed_id "
						+ "where i.MEDICALRECORD_ID = :type and i.del_flg=0 and i.stop_flg=0 and i.IN_STATE='R'";
					}
	     
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("type", type);
					List<InpatientInfoNow> iList =  namedParameterJdbcTemplate.query(hql,paraMap,new RowMapper<InpatientInfoNow>() {

						@Override
						public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
							InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
							inpatientInfoNow.setId(rs.getString("id"));
							inpatientInfoNow.setTotCost(rs.getObject("totCost")==null?null:rs.getDouble("totCost"));
							inpatientInfoNow.setMedicalrecordId(rs.getString("medicalrecordId"));
							inpatientInfoNow.setPatientName(rs.getString("patientName"));
							inpatientInfoNow.setReportSex(rs.getString("reportSex"));
							inpatientInfoNow.setReportSexName(rs.getString("reportSexName"));
							inpatientInfoNow.setInDate(rs.getDate("inDate"));
							inpatientInfoNow.setPaykindCode(rs.getString("paykindCode"));
							inpatientInfoNow.setBedId(rs.getString("bedId"));
							inpatientInfoNow.setPrepayOutdate(rs.getDate("prepayOutdate"));
							inpatientInfoNow.setIdcardNo(rs.getString("idcardNo"));
							inpatientInfoNow.setReportAge(rs.getObject("reportAge")==null?null:rs.getInt("reportAge"));
							inpatientInfoNow.setReportBirthday(rs.getDate("reportBirthday"));
							inpatientInfoNow.setDeptName(rs.getString("deptName"));
							inpatientInfoNow.setBalance(rs.getObject("balance")==null?null:rs.getDouble("balance"));
							inpatientInfoNow.setAccountAmount(rs.getObject("accountAmount")==null?null:rs.getDouble("accountAmount"));
							inpatientInfoNow.setCertificatesNo(rs.getString("certificatesNo"));
							inpatientInfoNow.setBedName(rs.getString("bedName"));
							return inpatientInfoNow;
						}
						
					});

			if(iList!=null&&iList.size()>0){
				return iList;
			}
	
		return null;
	}
	
	/**  
	 *  
	 * @Description：  查询出院登记
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-26 下午02:06:02  
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-04 下午02:00:00  
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryInpinfoList(String type, String no) {
		String hql = "select i.INPATIENT_ID as id,i.INPATIENT_NO as inpatientNo,i.MEDICALRECORD_ID as medicalrecordId,"
				+ "i.PATIENT_NAME as patientName,i.REPORT_SEX as reportSex,i.IN_DATE as inDate,"
				+ "i.PAYKIND_CODE as paykindCode,i.BEDINFO_ID as bedId,i.PREPAY_OUTDATE as prepayOutdate,"
				+ "i.IDCARD_ID as idcardNo,";
			if(no==""){
				hql=hql+"(select ct.DEPT_NAME from "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT ct "
						+ "where ct.del_flg=0 and ct.stop_flg=0 and ct.DEPT_ID="
						+ "(select i.DEPT_CODE from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i "
								+ "where i.MEDICALRECORD_ID like '%"+type+"')) as deptName,"+
			            "(select a.INPATIENT_BALANCE from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNT a "
			            		+ "where a.IDCARD_ID =(select idc.IDCARD_ID "
			            		+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW idc "
			            				+ "where idc.MEDICALRECORD_ID like '%"+type+"') and a.stop_flg = 0 and a.del_flg = 0) as balance,"
					      +"(select sum(ac.DETAIL_CREDITAMOUNT) "
					      + "from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNTREPAYDETAIL ac "
					      		+ "where ac.del_flg=0 and ac.stop_flg=0 and ac.ACCOUNT_ID =(select a.ACCOUNT_ID "
					      		+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNT a "
					      				+ "where a.del_flg=0 and a.stop_flg=0 and a.IDCARD_ID ="
					      				+ "(select i.IDCARD_ID from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i "
					      						+ "where i.del_flg=0 and i.stop_flg=0 and i.MEDICALRECORD_ID = '"+type+"'))) as accountAmount "
					      								+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i "
					      										+ "left join "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_ORDER t "
					      												+ "on i.MEDICALRECORD_ID=t.PATIENT_NO "
					      		+ "where i.MEDICALRECORD_ID = '"+type+"' and IN_STATE='R' and t.DC_FLAG=1";
			}else {
				hql=hql+"(select ct.DEPT_NAME from "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT ct "
						+ "where ct.del_flg=0 and ct.stop_flg=0 and ct.DEPT_ID="
						+ "(select i.DEPT_CODE from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i "
								+ "where i.INPATIENT_NO like '%"+no+"')) as deptName,"+
						"(select a.INPATIENT_BALANCE from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNT a "
								+ "where a.IDCARD_ID =(select idc.IDCARD_ID "
								+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW idc "
										+ "where idc.INPATIENT_NO like '% "+no+"') and a.stop_flg = 0 and a.del_flg = 0) as balance,"
					      +"(select sum(ac.DETAIL_CREDITAMOUNT) "
					      + "from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNTREPAYDETAIL ac "
					      		+ "where ac.del_flg=0 and ac.stop_flg=0 and ac.ACCOUNT_ID =(select a.ACCOUNT_ID "
					      		+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_ACCOUNT a "
					      				+ "where a.del_flg=0 and a.stop_flg=0 and a.IDCARD_ID =(select i.IDCARD_ID "
					      				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i "
					      						+ "where i.del_flg=0 and i.stop_flg=0 and i.INPATIENT_NO = '"+no+"'))) as accountAmount "
					      								+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW i left join "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_ORDER t on i.INPATIENT_NO =t.INPATIENT_NO  "
					      		+ "where i.INPATIENT_NO = '"+no+"' and IN_STATE='R' and t.DC_FLAG=1";
			}
			SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("inpatientNo").addScalar("medicalrecordId").addScalar("patientName").addScalar("reportSex").addScalar("inDate",Hibernate.DATE)
					.addScalar("paykindCode").addScalar("bedId").addScalar("prepayOutdate",Hibernate.DATE).addScalar("idcardNo").addScalar("deptName").addScalar("balance",Hibernate.DOUBLE).addScalar("accountAmount",Hibernate.INTEGER);
			List<InpatientInfoNow> iList = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
			if(iList!=null&&iList.size()>0){
				return iList;
			}
			return new ArrayList<InpatientInfoNow>();
	}

	/**  
	 * @Description：  保存查到保存信息
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientInfoNow inpatientIdGet(String id) {
		String  hql = " from InpatientInfoNow where inpatientNo = '"+id+"'";
		List<InpatientInfoNow>  inpatientInfoList = super.findByObjectProperty(hql, null);
		if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
			return inpatientInfoList.get(0);
		}
		return new InpatientInfoNow();
	}
	/**  
	 * @Description：  查询床号
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientInfoNow ajaxBedId(String id) {
		String hql = " select BED_NAME as bedName from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_HOSPITALBED "
				+ "where BED_ID= '"+id+"' and del_flg=0 and stop_flg=0";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("bedName");
		List<InpatientInfoNow> iList = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();;
		if(iList!=null&&iList.size()>0){
			return iList.get(0);
		}

	return null;
		
		
	}
	/**  
	 * @Description：  查询退费申请
	 * @Author：zj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<InpatientCancelitemNow> ajaxCanceliem(String no) {
		String hql = "from InpatientCancelitemNow where del_flg=0 and stop_flg=0 and inpatientNo= ? and chargeFlag = 0 ";
		List<InpatientCancelitemNow>  inpatientCancelitem = super.find(hql, no);
		if(inpatientCancelitem!=null&&inpatientCancelitem.size()>0){
			return inpatientCancelitem;
		}
		return null;
	}

	


	/**  
	 * @Description：根据患者id查询挂号信息表
	 * @Author：zhenglin
	 * @CreateDate：2015-12-10
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public RegisterInfo getInfoByPatientNO(String patientNo) {
		String hql="from RegisterInfo where del_flg=0 and stop_flg=0 and patientId= ?";
		List<RegisterInfo> list=super.find(hql, patientNo);
		if(list!=null&&list.size()>0){
			return  list.get(0);
		}
		return new RegisterInfo();
	}
	
	/**  
	 * @Description：根据门诊号查询开院证明表
	 * @Author：zhenglin
	 * @CreateDate：2015-12-12
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public InfoVo  getProof(String id,String idcardNo ) {
		//查询系统参数(挂号有效期)
		String infoTime=parameterDAO.getParameterByCode("infoTime");
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		int days=1;
		days = Integer.valueOf(infoTime);
		Date date =  new Date();
		Date date2 = DateUtils.addDay(new Date(), -days);
		String sql="select i.REPORT_ISSUINGDOC as reportIssuingdoc,i.patient_name as patientName,"
				+ "i.report_sex as reportSex,i.MEDICALRECORD_ID as medicalrecordId,"
				+ "r.CARD_NO as idno,"
				+ "i.report_age as reportAge,"
				+ "i.report_birthday as reportBirthday,"
				+ "i.report_dept as reportDept,"
				+ "i.certificates_type as certificatesType,"
				+ "i.certificates_no as certificatesNo,"
				+ "i.report_diagnose as reportDiagnose,"
				+ "i.report_situation as reportSituation,"
				+ "i.report_bedward as reportBedward,"
				+ "c.paykind_code as paykindcode "
				+ "from "+HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_MAIN_NOW  r "
				+ "left join "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_proof  i "
				+ "on r.CLINIC_CODE = i.idcard_no "
				+ "left join "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_CONTRACTUNIT c "
				+ " on i.contract_unit =  c.UNIT_CODE "  
				+ "where i.MEDICALRECORD_ID = :id and i.idcard_no= :idcardNo and i.del_flg=0 and i.stop_flg= 0  "
				+ "and (r.REG_DATE between to_date(:date2,'yyyy/MM/dd hh24:mi:ss') and to_date(:date,'yyyy/MM/dd hh24:mi:ss')) "
				+ "and (i.REPORT_ISSUINGDATE between to_date(:date2,'yyyy/MM/dd hh24:mi:ss') and to_date(:date,'yyyy/MM/dd hh24:mi:ss'))";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("id", id);
		paraMap.put("idcardNo", idcardNo);
		paraMap.put("date2", (dateFormater.format(date2))+" 00:00:00");
		paraMap.put("date",(dateFormater.format(date))+" 23:59:59");
		List<InfoVo> list =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InfoVo>() {

			@Override
			public InfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InfoVo vo = new InfoVo();
				vo.setReportIssuingdoc(rs.getString("reportIssuingdoc"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setReportSex(rs.getString("reportSex"));
				vo.setMedicalrecordId(rs.getString("medicalrecordId"));
				vo.setIdno(rs.getString("idno"));
				vo.setReportAge(rs.getObject("reportAge")==null?null:rs.getInt("reportAge"));
				vo.setReportBirthday(rs.getDate("reportBirthday"));
				vo.setReportDept(rs.getString("reportDept"));
				vo.setCertificatesType(rs.getString("certificatesType"));
				vo.setCertificatesNo(rs.getString("certificatesNo"));
				vo.setReportDiagnose(rs.getString("reportDiagnose"));
				vo.setReportSituation(rs.getObject("reportSituation")==null?null:rs.getInt("reportSituation"));
				vo.setReportBedward(rs.getString("reportBedward"));
				vo.setPaykindcode(rs.getString("paykindcode"));
				return vo;
			}
			
		});		
		
		 if(list!=null&&list.size()>0){
			 return (InfoVo)list.get(0);
		 }
		   return new InfoVo();
	}

	/**  
	 * @Description：根据住院状态查询住院登记表
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public List<InpatientInfoNow> queryPatientByInState(String state) {
		String hql="from InpatientInfoNow i where i.inState= ?";
		List<InpatientInfoNow> list=super.find(hql, state);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	
	/**  
	 *  
	 * @Description：  （根据病历号查询）住院诊断.
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15 下午05:37:12  
	 * @Modifier：
	 * @ModifyDate：2015-12-15 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow query(String no) {
		String hql="from InpatientInfoNow  where (inState='R' or inState='I') and inpatientNo = ?";
		List<InpatientInfoNow> prooflist = super.find(hql, no);
		if(prooflist!=null&&prooflist.size()>0){
			return prooflist.get(0);
		}
		return new  InpatientInfoNow();
	}


	/**  
	 * @Description： 转入病人信息查询
	 *@Author：zhenglin
	 * @CreateDate：2015-12-19 上午10:56:35  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-19 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	public List<InpatientInfoNow> getInfoByApply() {
	    String sql="select * from InpatientInfoNow where inpatientNo in "
	    		+ "(select inpatientNo from InpatientShiftApplyNow where oldBedCode is not null)";
	    SQLQuery query = this.getSession().createSQLQuery(sql);
		return query.list();
	}

	
	
	/**  
	 * @Description： 查询患者
	 *@Author：zhenglin
	 * @CreateDate：2015-12-19 上午10:56:35  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-19 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	public List<VidBedInfo> queryPatientList() {
		String hql = "from VidBedInfo i WHERE i.stop_flg=0 AND i.del_flg=0"; 
		List<VidBedInfo> iList = super.find(hql, null);
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<VidBedInfo>();
	}

	/**  
	 * @Description： 根据患者的住院状态显示患者信息
	 *@Author：zhenglin
	 * @CreateDate：2015-12-19 上午10:56:35  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-19 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	public List<VidBedInfo> queryPatientByInStateList(String state) {
		String hql="from InpatientInfoNow i where i.inState= ?";
		List<VidBedInfo> list=super.find(hql, state);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<VidBedInfo>();
	}

	/**
	 * 分页带条件查询患者警戒线
	 */
	public List<InpatientInfoNow> getInfoByMoney(InpatientInfoNow info, String rows,
			String page) {
		String hql=joints(info);
		return super.getPage(hql, page, rows);
	}

	/**
	 * 获得分页总数
	 */
	public int getTotals(InpatientInfoNow entity) {
		String hql=joints(entity);
		return super.getTotal(hql);
	}
	
	public String joints(InpatientInfoNow entity){
		String hql="FROM InpatientInfoNow i WHERE i.freeCost <5 ";
		hql = hql+" ORDER BY i.id";
		return hql;
	}
	/**
	 * @Description:获取患者树(护士站收费)
	 * @Author： zhangjin
	 * @CreateDate： 2016-1—5
	 * @param @param 
	 * @return 
	 * @version 1.0
	**/
	@Override
	public List<InfoVo> treeNurseCharge(String deptId,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime) {
            		String hql="select DISTINCT b.INPATIENT_ID as id,b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
				+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,"
				+ "b.EMPL_CODE as emplCode,"
				+ "e.bed_name as bedName,b.INPATIENT_NO as inpatientNo from "+HisParameters.HISPARSCHEMAHISUSER+""
						+ "T_INPATIENT_INFO_NOW b left join "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT_CONTACT f "
				+ "on b.dept_code = f.dept_id left join T_INPATIENT_BEDINFO_NOW t on t.BEDINFO_ID=b.BEDINFO_ID"
				+ " left join "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_HOSPITALBED e on t.BED_ID = e.BED_ID where "
				+ " e.del_flg=0 and e.stop_flg=0 and t.del_flg=0 and t.stop_flg=0 and f.del_flg=0 and f.stop_flg=0 ";
				
            	if("1".equals(a)){
            		hql+=" and b.IN_STATE in('I') ";
            	}
            	if("2".equals(a)){
            		hql+=" and b.IN_STATE in('B','P') ";
            	}
            	if("3".equals(a)){
            		hql+=" and b.INPATIENT_NO in(select q.PATIENT_ID from T_DRUG_APPLYOUT_NOW q "
            				+ "where q.APPLY_STATE!=2 and q.del_flg=0 and q.stop_flg=0 ) ";
            	}
            	if("4".equals(a)){
            		hql+=" and b.INPATIENT_NO in(select q.PATIENT_ID from T_DRUG_APPLYOUT_NOW q "
            				+ "where q.APPLY_STATE=2 and q.del_flg=0 and q.stop_flg=0 ) ";
            	}
            	if(StringUtils.isNotBlank(deptId)){
            		hql+=" and f.PARDEPT_ID in(select t.id from t_department_contact t where t.dept_id = '"+deptId+"') ";
            	}
				if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
					hql +="and b.MEDICALRECORD_ID like '%"+inpatientInfo.getMedicalrecordId()+"' ";
				}
				if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
					hql +="and  b.in_Date between to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') "
							+ "and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
				}
				hql += "order by e.bed_name ";					
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("medicalrecordId").addScalar("patientName")
				.addScalar("inDate",Hibernate.DATE).addScalar("deptCode").addScalar("bedId").addScalar("moneyAlert",Hibernate.DOUBLE)
				.addScalar("freeCost",Hibernate.DOUBLE).addScalar("emplCode").addScalar("bedName").addScalar("inpatientNo");
		List<InfoVo> iList = null;
		try {
			iList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoVo.class)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		if(iList!=null&&iList.size()>0){
			return iList; 
		}

	 return null;
	}
	/**  
	 * @Description：  病区（下拉框）
	 * @Author：tcj
	 * @CreateDate：2015-12-30 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> querydeptCombobox(String id) {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0 and deptType='N' ";
		List<SysDepartment> binqulist = new ArrayList<SysDepartment>();
		if(StringUtils.isNotBlank(id)){
			hql+=" and id in  ( select dc2.deptId from DepartmentContact dc2 where dc2.id in "
					+ "(select dc1.pardeptId  from DepartmentContact dc1  where dc1.deptId= ? and dc1.referenceType='03' ))";
		}
		if(StringUtils.isNotBlank(id)){
			binqulist = super.find(hql,id);
		}else{
			binqulist = super.find(hql,null);
		}
		if(binqulist!=null&&binqulist.size()>0){
			return binqulist;
		}
		return new ArrayList<SysDepartment>();
	}
	/**  
	 * @Description：  查询病房树
	 * @Author：tcj
	 * @CreateDate：2015-12-30  上午15:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessBedward> queryPatientRoom(String id ) {
		String hql=" from BusinessBedward where del_flg = 0 and nursestation = ?";
		List<BusinessBedward> businessBedwardList = super.find(hql, id);
		if(businessBedwardList!=null&&businessBedwardList.size()>0){
			return businessBedwardList;
		}
		return new ArrayList<BusinessBedward>();
	}
	/**  
	 * @Description：  点击病房树查询病床
	 * @Author：tcj
	 * @CreateDate：2015-12-30  下午18:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessHospitalbed> queryPatientRoomBed(String noId,String page,String rows,BusinessHospitalbed bedEntity) {
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String hql="from BusinessHospitalbed bh  where del_flg = 0 and bedState= '7'   ";
		if(StringUtils.isNotBlank(noId)&& !"1".equals(noId)){
			hql= hql+"and businessBedward.id = '"+noId+"'";
		}
		if(StringUtils.isNotBlank(deptId)){
			hql=hql+" and bh.businessBedward in (select b.id from BusinessBedward b where b.nursestation in "
					+ "(select t.deptCode from DepartmentContact t where t.deptCode = '"+deptId+"'))";
		}
		if(StringUtils.isNotBlank(bedEntity.getBedName())){
			hql+=" and bh.bedName = '"+bedEntity.getBedName()+"'";
		}
		if(StringUtils.isNotBlank(bedEntity.getBedLevel())){
			hql+=" and bh.bedLevel='"+bedEntity.getBedLevel()+"'";
		}
		List<BusinessHospitalbed> businessHospitalbedList = hospitalbedDAO.getPage(hql, page, rows);
		if(businessHospitalbedList!=null&&businessHospitalbedList.size()>0){
			return businessHospitalbedList;
		}
		return new ArrayList<BusinessHospitalbed>();
	}
	/**
	 * @Description:查询列表总条数
	 * @Author：  tcj
	 * @CreateDate： 2016-1-4 11:38
	 * @param @return   
	 * @return int  
	 * @version 1.0
	**/
	@Override
	public int getTotal(BusinessHospitalbed bedEntity,String noId) {
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String hql="select bh.BEDWARD_ID from T_BUSINESS_HOSPITALBED bh where bh.DEL_FLG = 0 and bh.STOP_FLG= 0 and bh.BED_STATE = '7'";
		if(StringUtils.isNotBlank(noId)&&!"1".equals(noId)){
			hql= hql+" and bh.BEDWARD_ID = '"+noId+"'";
		}
		if(StringUtils.isNotBlank(deptId)){
			hql= hql+" and bh.BEDWARD_ID in (select b.BEDWARD_ID from T_BUSINESS_BEDWARD b "
					+ "where b.BEDWARD_NURSESTATION in (select t.DEPT_CODE from T_DEPARTMENT_CONTACT t where t.DEPT_CODE = '"+deptId+"'))";
		}
		if(StringUtils.isNotBlank(bedEntity.getBedName())){
			hql+=" and bh.BED_NAME = '"+bedEntity.getBedName()+"'";
		}
		if(StringUtils.isNotBlank(bedEntity.getBedLevel())){
			hql+=" and bh.BED_LEVEL='"+bedEntity.getBedLevel()+"'";
		}
		if(StringUtils.isNotBlank(bedEntity.getBedState())){
			hql+=" and bh.BED_STATE='"+bedEntity.getBedState()+"'";
		}
		return super.getSqlTotal(hql.toString());
	}

	
	/**
	 * @Description:根据id查询患者信息
	 * @Author：  zhangjin
	 * @CreateDate： 2016-1-6
	 * @param @return   
	 * @return   
	 * @version 1.0 
	**/
	@Override
	public InpatientInfoNow querNurseCharge(String no) {
		String hql="from InpatientInfoNow where id= ?";
		List<InpatientInfoNow> list=super.find(hql, no);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientInfoNow();
	}
	/**  
	 * @Description：  查询患者的住院信息（登记、接诊，以及相应的时间）
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午16:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public String queryMedicalrecordIdDate(String medicalrecordId,
			Date now) {
		String result="none";
		String hql="from InpatientInfoNow where  inpatientNo=? and inState ='R' ";
		List<InpatientInfoNow> ipfR=super.find(hql, medicalrecordId);
		if(ipfR!=null&&ipfR.size()>0){
			 result="R";
			 return result;
		}
		String hql1="from InpatientInfoNow where  inpatientNo=? and inState ='I' ";
		List<InpatientInfoNow> ipfI=super.find(hql1, medicalrecordId);
		if(ipfI!=null&&ipfI.size()>0){
			result="I";
			return result;
		}
		String hql2="from InpatientInfoNow where  inpatientNo=? and inState ='B' ";
		List<InpatientInfoNow> ipfI2=super.find(hql2, medicalrecordId);
		if(ipfI2!=null&&ipfI2.size()>0){
			result="B";
			return result;
		}
		String hql3="from InpatientInfoNow where  inpatientNo=? and inState ='P' ";
		List<InpatientInfoNow> ipf3=super.find(hql3, medicalrecordId);
		if(ipf3!=null&&ipf3.size()>0){
			result="P";
			return result;
		}
		return result;
	}
	/**  
	 * @Description：  查询住院总次数
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int queryCount(String medicalrecordId) {
		String hql="from InpatientInfoNow where del_flg = 0 and medicalrecordId='"+medicalrecordId+"'";
		return super.getTotal(hql);
	}
	/**  
	 *  
	 * @Description：根据病历号查询患者信息
	 * @Author：zhangjin
	 * @CreateDate：2016-1-18
	 * @Modifier：zhangjin
	 * @ModifyDate：  2016-3-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	@Override
	public List<InpatientInfoNow> queryNurseChargeInpinfo(String no,String dept) {
		String hql="select b.MEDICALRECORD_ID as medicalrecordId,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
				+ "b.PACT_CODE as pactCode, b.DEPT_CODE as deptCode,b.EMPL_CODE as emplCode,b.FREE_COST as freeCost,"
				+ "b.MONEY_ALERT as moneyAlert,b.BEDINFO_ID as bedId,b.IN_STATE as inState,b.STOP_ACOUNT as stopAcount,"
				+ "b.TOT_COST as totCost,b.REPORT_AGE as reportAge,b.PAYKIND_CODE as paykindCode,"
				+ "b.INPATIENT_NO as inpatientNo,b.BABY_FLAG as babyFlag,b.REPORT_SEX as reportSex,b.CERTIFICATES_NO as certificatesNo "
				+ " from T_INPATIENT_INFO_NOW b left join T_DEPARTMENT_CONTACT f on b.dept_code = f.dept_id "+                 
                 " where  b.IN_STATE in ('R','I') and b.MEDICALRECORD_ID like '%"+no+"'";
		if(StringUtils.isNotBlank(dept)){
			hql+=" and f.PARDEPT_ID in(select e.id from t_department_contact e where e.dept_id = '"+dept+"')";
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("medicalrecordId").addScalar("patientName")
				.addScalar("inDate",Hibernate.DATE).addScalar("pactCode").addScalar("deptCode").addScalar("emplCode")
				.addScalar("freeCost",Hibernate.DOUBLE).addScalar("moneyAlert",Hibernate.DOUBLE).addScalar("bedId")
				.addScalar("inState").addScalar("stopAcount",Hibernate.INTEGER).addScalar("totCost",Hibernate.DOUBLE)
				.addScalar("reportAge",Hibernate.INTEGER).addScalar("paykindCode").addScalar("inpatientNo")
				.addScalar("babyFlag",Hibernate.INTEGER).addScalar("reportSex").addScalar("certificatesNo");
		List<InpatientInfoNow> list= queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	/**  
	 * @Description： 根据住院流水号查询处于接诊状态的患者
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientInfoNow queryByInpatientNo(String inpatientNo) {
		String hql ="from InpatientInfoNow where inpatientNo=? and inState= 'I'";
		List<InpatientInfoNow> infols = super.find(hql, inpatientNo);
		for(int i=0;i<infols.size();i++){
			return infols.get(i);
		}
		return new InpatientInfoNow();
	}
	/**  
	 * @Description： 根据住院流水号查询患者
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientInfoNow queryByInpatientNot(String inpatientNo) {
		String hql ="from InpatientInfoNow where inpatientNo=? ";
		List<InpatientInfoNow> infols = super.find(hql, inpatientNo);
		for(int i=0;i<infols.size();i++){
			return infols.get(i);
		}
		return new InpatientInfoNow();
	}

	@Override
	public String queryProofInfo(String certificatesNo) {
		
		//查询系统参数(挂号有效期)
		String infoTime=parameterDAO.getParameterByCode("infoTime");
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd");
		int days=1;
		days = Integer.valueOf(infoTime);
		Date date = new Date();
		Date date2 = DateUtils.addDay(new Date(), -days);
		String result="no";
		String sql ="select t.PROOF_ID as id from T_INPATIENT_PROOF t where t.DEL_FLG=0 and t.STOP_FLG=0 "
				+ "and t.MEDICALRECORD_ID =:medicalrecordId "
				+ "and (t.REPORT_ISSUINGDATE between to_date(:date2,'yyyy/MM/dd hh24:mi:ss') "
				+ "and to_date(:date,'yyyy/MM/dd hh24:mi:ss'))";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", certificatesNo);
		paraMap.put("date2", (dateFormater.format(date2))+" 00:00:00");
		paraMap.put("date", (dateFormater.format(date))+" 23:59:59");
		
		List<InpatientProof> inproof =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientProof>() {

			@Override
			public InpatientProof mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientProof inpatientProof = new InpatientProof();
				inpatientProof.setId(rs.getString("id"));
				return inpatientProof;
			}});
		if(inproof!=null&&inproof.size()>0){
			 result="yes";
			 return result;
		}
		return result;
	}
	/**  
	 *  
	 * @Description： 根据科室编号查询住院患者信息
	 * @Author：tfs
	 * @CreateDate：2016-3-5 下午14:12:01  
	 * @Modifier：
	 * @ModifyDate：2015-3-5 下午14:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoNow> getInfoByDeptCode(String deptCode) {
		String hql="from InpatientInfoNow i where 1=1 ";
		if(StringUtils.isNotBlank(deptCode)){
			hql = hql+"AND i.deptCode = '"+deptCode+"' ";
		}
		List<InpatientInfoNow>  inpatientInfoList = super.findByObjectProperty(hql, null);
		if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
			return inpatientInfoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	/**  
	 *  
	 * @Description： 根据科室编号和住院号查询住院患者信息
	 * @Author：tfs
	 * @CreateDate：2016-3-21 上午11:12:01  
	 * @Modifier：
	 * @ModifyDate：2015-3-21 上午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoNow> getInfoByDeptCodeAndMid(String deptCode, String medicalrecordId) {
		String hql="from InpatientInfoNow i where 1=1 ";
		if(StringUtils.isNotBlank(deptCode)){
			hql = hql+"AND i.deptCode = '"+deptCode+"' ";
		}
		if(StringUtils.isNotBlank(medicalrecordId)){
			hql = hql+"AND i.medicalrecordId like '"+medicalrecordId+"%' ";
		}
		List<InpatientInfoNow>  inpatientInfoList = super.findByObjectProperty(hql, null);
		if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
			return inpatientInfoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public List<InpatientInfoNow> getQueryInfo(String medicalrecordId) {
		String hql="from InpatientInfoNow i where (i.medicalrecordId = ? ) ";
		List<InpatientInfoNow> infoList=super.find(hql, medicalrecordId);
		if(infoList!=null&&infoList.size()>0){
			return infoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public InpatientBedinfoNow queryBedId(String bedInfoId) {
		String hql="from InpatientBedinfoNow b where b.del_flg=0";
		if(StringUtils.isNotBlank(bedInfoId)){
			hql+=" and b.id='"+bedInfoId+"'";
		}
		List<InpatientBedinfoNow> bedInfoList=super.find(hql, null);
		if(bedInfoList!=null&&bedInfoList.size()>0){
			return bedInfoList.get(0);
		}
		return new InpatientBedinfoNow();
	}

	@Override
	public List<InpatientProof> getdengjiInfo(String medicalrecordId) {
		String sql ="select t.MEDICALRECORD_ID as medicalrecordId,t.REPORT_SEX as reportSex,"
				+ "t.PATIENT_NAME as patientName,t.IDCARD_NO as idcardNo ,t.REPORT_AGE as reportAge,"
				+ "t.REPORT_AGEUNIT as reportAgeunit,t.CERTIFICATES_NO as certificatesNo"
				+ " from T_INPATIENT_PROOF t where t.MEDICALRECORD_ID =:medicalrecordId and t.del_flg=0 and t.stop_flg=0 ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", medicalrecordId);
		List<InpatientProof> patientlist =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientProof>() {

			@Override
			public InpatientProof mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientProof inpatientProof =new InpatientProof();
				inpatientProof.setMedicalrecordId(rs.getString("medicalrecordId"));
				inpatientProof.setReportSex(rs.getString("reportSex"));
				inpatientProof.setPatientName(rs.getString("patientName"));
				inpatientProof.setIdcardNo(rs.getString("idcardNo"));
				inpatientProof.setReportAge(rs.getObject("reportAge")==null?null:rs.getInt("reportAge"));
				inpatientProof.setReportAgeunit(rs.getString("reportAgeunit"));
				inpatientProof.setCertificatesNo(rs.getString("certificatesNo"));
				return inpatientProof;
			}
		});
		if(patientlist.size()>0&&patientlist!=null){
			return patientlist;
		}
		return new ArrayList<InpatientProof>();
	}
	@Override
	public List<SysDepartment> zyDeptCombobox() {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0 and deptType='I'";
		List<SysDepartment> sysdl=super.find(hql, null);
		if(sysdl!=null&&sysdl.size()>0){
			return sysdl;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public Patient queryPatientInfomation(String medicalrecordId) {
		String hql="from Patient where del_flg=0 and stop_flg=0 and medicalrecordId=?";
		List<Patient> plist=super.find(hql, medicalrecordId);
		if(plist!=null&&plist.size()>0){
			return plist.get(0);
		}
		return new Patient();
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
	public void editInpatientInfo(InpatientInfoNow inpatientInfo, Patient patient) {
		inpatientInfo.setCreateTime(DateUtils.getCurrentTime());
		inpatientInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		inpatientInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		inpatientInfo.setInState("R");
		if(inpatientInfo.getBabyFlag()==null){
			inpatientInfo.setBabyFlag(0);
		}
		if(inpatientInfo.getHaveBabyFlag()==null){
			inpatientInfo.setHaveBabyFlag(0);
		}
		this.save(inpatientInfo);
		
		List<InpatientPrepayin> list = queryPrepayinInfo(inpatientInfo.getMedicalrecordId());
		if(list !=null && list.size()>0){
			InpatientPrepayin inpatientPrepayin =list.get(0);
			inpatientPrepayin.setPreState(2);
			this.save(inpatientPrepayin);
		}
		
		patient.setPatientName(inpatientInfo.getPatientName());//患者姓名
		patient.setPatientSex(Integer.valueOf(inpatientInfo.getReportSex()));//患者性别
		patient.setPatientAge((double)inpatientInfo.getReportAge());//患者年龄
		patient.setPatientBirthday(inpatientInfo.getReportBirthday());//出生日期
		patient.setPatientCertificatesno(inpatientInfo.getCertificatesNo());//身份证号
		patient.setPatientCertificatestype(inpatientInfo.getCertificatesType());
		patient.setPatientNationality(inpatientInfo.getCounCode());//国籍
		patient.setPatientNativeplace(inpatientInfo.getDist());//籍贯
		patient.setPatientNation(inpatientInfo.getNationCode());//民族
		patient.setPatientWorkunit(inpatientInfo.getWorkName());//工作单位
		patient.setPatientWorkphone(inpatientInfo.getWorkTel());//工作电话
		patient.setPatientWarriage(inpatientInfo.getMari());//婚姻状况
		patient.setPatientOccupation(inpatientInfo.getProfCode());//职业
		patient.setPatientLinkman(inpatientInfo.getLinkmanName());//联系人
		patient.setPatientLinkrelation(inpatientInfo.getRelaCode());//关系
		patient.setPatientLinkaddress(inpatientInfo.getLinkmanAddress());//联系人地址
		patient.setPatientLinkphone(inpatientInfo.getLinkmanTel());//联系人电话
		patient.setPatientPhone(inpatientInfo.getHomeTel());//家庭电话
		patient.setPatientAddress(inpatientInfo.getHome());//家庭地址
		patient.setPatientBirthplace(inpatientInfo.getBirthArea());//出生地哦
		patient.setUpdateTime(DateUtils.getCurrentTime());
		patient.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		
		//20161114这里增加一个方法,调用的时候为当前患者的住院次数加一,并更新此患者的住院流水号
		Integer num = patient.getInpatientSum();
		if(num==null){
			num =1;
		}else{
			num+=1;
		}
		patient.setInpatientSum(num);
		this.save(patient);
	}
	@Override
	public List<SysEmployee> querykaijudocDj() {
		String hql="from SysEmployee where del_flg=0 and stop_flg=0";
		List<SysEmployee> empl=super.find(hql, null);
		if(empl!=null&&empl.size()>0){
			return empl;
		}
		return new ArrayList<SysEmployee>();
	}

	@Override
	public List<Patient> getIdcardInfo(String medinfoId) {
		String hql=" from Patient where del_flg=0 and stop_flg=0 and medicalrecordId =?";
		List<Patient> patientlist=super.find(hql, medinfoId);
		if(patientlist!=null&&patientlist.size()>0){
			return patientlist;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public List<Patient> getPatientInfoByCerNo(String cerno) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from T_PATIENT t where t.DEL_FLG=0 and t.STOP_FLG=0 "
				+ "and t.MEDICALRECORD_ID =:medicalrecordId");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("medicalrecordId", cerno);
		RowMapper<Patient> rm = new BeanPropertyRowMapper<Patient>(Patient.class) ;
		List<Patient>  pl =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,rm);
		if(pl!=null && pl.size()>0){
			return pl;
		}
		return new ArrayList<Patient>();
	}
	@Override
	public List<InpatientPrepayin> queryPrepayinInfo(String medicalrecordId) {
		String hql="from InpatientPrepayin where del_flg=0 and stop_flg=0 and medicalrecordId=? and preState=0";
		List<InpatientPrepayin> inpyl=super.find(hql, medicalrecordId);
		if(inpyl!=null&&inpyl.size()>0){
			return inpyl;
		}
		return new ArrayList<InpatientPrepayin>();
	}

	@Override
	public String queryMedicalrecordIdDateByMid(String medicalrecordId) {
		String result="none";
		String hql="from InpatientInfoNow where  medicalrecordId=? and inState ='R' ";
		List<InpatientInfoNow> ipfR=super.find(hql, medicalrecordId);
		if(ipfR!=null&&ipfR.size()>0){
			 result="R";
			 return result;
		}
		String hql1="from InpatientInfoNow where  medicalrecordId=? and inState ='I' ";
		List<InpatientInfoNow> ipfI=super.find(hql1, medicalrecordId);
		if(ipfI!=null&&ipfI.size()>0){
			result="I";
			return result;
		}
		String hql2="from InpatientInfoNow where  medicalrecordId=? and inState ='B' ";
		List<InpatientInfoNow> ipfI2=super.find(hql2, medicalrecordId);
		if(ipfI2!=null&&ipfI2.size()>0){
			result="B";
			return result;
		}
		String hql3="from InpatientInfoNow where  medicalrecordId=? and inState ='P' ";
		List<InpatientInfoNow> ipf3=super.find(hql3, medicalrecordId);
		if(ipf3!=null&&ipf3.size()>0){
			result="P";
			return result;
		}
		return result;
	}

	@Override
	public List<Patient> getPatientInfo(String medicalrecordId) {
		String hql ="from Patient where medicalrecordId =? and stop_flg=0 and del_flg=0";
		List<Patient> patient=super.find(hql, medicalrecordId);
		if(patient!=null&&patient.size()>0){
			return patient;
		}
		return new ArrayList<Patient>();
	}

	@Override
	public String getPaykind(String unit) {
		String sql = "select t.PAYKIND_CODE from t_business_contractunit t where 1=1 ";
		if(StringUtils.isBlank(unit)){
			return null;
		}else{
			sql+=" and t.UNIT_CODE =:unit";
		}
		Query queryObject=this.getSession().createSQLQuery(sql);
		if(StringUtils.isBlank(unit)){
		}else{
			queryObject.setParameter("unit", unit);
		}
		String payKind = (String) queryObject.list().get(0);
		return payKind;
	}

	@Override
	public void updateInpatientFeeInfoNow(final InpatientInfoNow info1) {
		int t = jdbcTemplate.update("insert into T_INPATIENT_BALANCEPAY_NOW values(?,?,?,?,?,?,?,?,?,?,?)",new PreparedStatementSetter() {    
			   public void setValues(PreparedStatement ps) throws SQLException { 
			      ps.setString(1, info1.getInState());
			      ps.setInt(2, info1.getStopAcount());
			      ps.setInt(3, info1.getBalanceNo());
			      ps.setDate(4, (java.sql.Date) info1.getBalanceDate());
			      ps.setDouble(5, info1.getTotCost());
			      ps.setDouble(6, info1.getOwnCost());
			      ps.setDouble(8, info1.getPayCost());
			      ps.setDouble(9, info1.getPubCost());
			      ps.setDouble(10, info1.getEcoCost());
			      ps.setDouble(11, info1.getBalancePrepay());
			   }
			});
	}
	@Override
	public int updeteOut(InpatientInfoNow p) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql = "update T_INPATIENT_INFO_NOW set OUT_DATE=?,DIAG_OUTSTATE=?,IN_STATE=?,UPDATEUSER=?,"
				+ "UPDATETIME=? where INPATIENT_ID=? ";
		Object args[] = new Object[] { p.getOutDate(),p.getOutState(), "B", userId,new Date(),p.getId()};
		int t = jdbcTemplate.update(sql, args);
		return t;
	}

	@Override
	public void updateInpatientInfoNow(InpatientInfoNow i) {
		String sql ="update T_INPATIENT_INFO_NOW set FREE_COST=?,PREPAY_COST=? where INPATIENT_ID=?";
		Object args[] = new Object[]{i.getFreeCost(),i.getPrepayCost(),i.getId()};  
		int t = jdbcTemplate.update(sql,args); 
	}

	@Override
	public List<DrugApplyoutNow> getDrugApplyoutNowList(String inpatientNo) {
		String hql1 = "from DrugApplyoutNow h where h.patientId = ? and h.applyState in ('1','0','5') and "
				+ "h.opType in('4','5')";
		List<DrugApplyoutNow>  list = super.find(hql1, inpatientNo);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DrugApplyoutNow>();
	}

	@Override
	public List<InpatientOrderNow> getInpatientOrderNowList(String inpatientNo) {
		String hql1 = "from InpatientOrderNow h where h.inpatientNo = ? and h.moStat in('1','2') ";
		List<InpatientOrderNow>  list = super.find(hql1, inpatientNo);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientOrderNow>();
	}
}
