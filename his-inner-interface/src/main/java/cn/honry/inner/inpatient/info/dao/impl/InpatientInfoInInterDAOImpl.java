package cn.honry.inner.inpatient.info.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessIcd10;
import cn.honry.base.bean.model.BusinessMedicalGroupInfo;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientConsultation;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientItemListNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.InsuranceSiitem;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.UndrugZtinfo;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.inner.inpatient.info.vo.FeeInInterVo;
import cn.honry.inner.inpatient.info.vo.InfoInInterVo;
import cn.honry.inner.inpatient.info.vo.InpatientInfoInInterVo;
import cn.honry.inner.patient.patient.dao.PatinmentInnerDao;
import cn.honry.inner.vo.FixedChargeVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.MD5;
import cn.honry.utils.SessionUtils;


/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
@Repository("inpatientInfoInInterDAO")
@SuppressWarnings({"all"})
public  class InpatientInfoInInterDAOImpl extends HibernateEntityDao<InpatientInfoNow> implements InpatientInfoInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	private PatinmentInnerDao patinmentInnerDao; 						
	
	@Override
	public InpatientInfoNow queryByMedical(String medicalNo) {
		String hql="FROM InpatientInfoNow i WHERE i.medicalrecordId = :medicalNo  ";
		List<InpatientInfoNow> list = this.getSession().createQuery(hql).setParameter("medicalNo", medicalNo).list();
		return list.size()==0 ? null : list.get(0);
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
		String hql="from InpatientInfoNow where id='"+no+"'";
		List<InpatientInfoNow> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpatientInfoNow();
		
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
	public List<InfoInInterVo> treeNurseCharge(String deptId,InpatientInfoNow inpatientInfo,String a,String startTime,String endTime) {
		String hql="select DISTINCT b.INPATIENT_ID as id,b.MEDICALRECORD_ID as medicalrecordId,b.IDCARD_NO as idcardNo,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
				+ "b.DEPT_CODE as deptCode,b.PACT_CODE as pactCode,b.BEDINFO_ID as bedId,b.MONEY_ALERT as moneyAlert,b.FREE_COST as freeCost,b.EMPL_CODE as emplCode,"
				+ "b.BED_NAME as bedName,b.INPATIENT_NO as inpatientNo,b.BABY_FLAG as babyFlag,b.report_sex as reportSex from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW b ";
            		if("5".equals(a)){
            			hql+=" left join "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT_CONTACT f on b.dept_code = f.dept_id ";
            		}
            		hql+=" where "
				+ " b.IN_STATE in('I') ";
				
//            	if("1".equals(a)){
//            		hql+=" and b.IN_STATE in('I') ";
//            	}
//            	if("2".equals(a)){
//            		hql+=" and b.IN_STATE in('B','P') ";
//            	}
//            	if("3".equals(a)){
//            		hql+=" and b.INPATIENT_NO in(select q.PATIENT_ID from T_DRUG_APPLYOUT q where q.APPLY_STATE!=2 and q.del_flg=0 and q.stop_flg=0 ) ";
//            	}
//            	if("4".equals(a)){
//            		hql+=" and b.INPATIENT_NO in(select q.PATIENT_ID from T_DRUG_APPLYOUT q where q.APPLY_STATE=2 and q.del_flg=0 and q.stop_flg=0 ) ";
//            	}
        		if("5".equals(a)){
        			hql+=" and f.del_flg=0 and f.stop_flg=0 and f.PARDEPT_ID=(select t.id from t_department_contact t where t.dept_code = '"+deptId+"')";
        		}else{
        			if(StringUtils.isNotBlank(deptId)){
                		hql+=" and  b.dept_code='"+deptId+"'";
                	}
        		}
				if(StringUtils.isNotBlank(inpatientInfo.getMedicalrecordId())){
					hql +="and b.MEDICALRECORD_ID = '"+inpatientInfo.getMedicalrecordId()+"' ";
				}
				if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
					hql +="and b.in_Date between to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ";
				}
				hql += "order by b.bed_name ";					
		SQLQuery queryObject = this.getSession().createSQLQuery(hql).addScalar("id").addScalar("medicalrecordId").addScalar("patientName")
				.addScalar("inDate",Hibernate.DATE).addScalar("deptCode").addScalar("bedId").addScalar("moneyAlert",Hibernate.DOUBLE).addScalar("babyFlag",Hibernate.INTEGER)
				.addScalar("reportSex")
				.addScalar("freeCost",Hibernate.DOUBLE).addScalar("emplCode").addScalar("bedName").addScalar("inpatientNo").addScalar("idcardNo");
		List<InfoInInterVo> iList = null;
		try {
			iList = queryObject.setResultTransformer(Transformers.aliasToBean(InfoInInterVo.class)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		if(iList!=null&&iList.size()>0){
			return iList; 
		}

	 return null;
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
		String hql="select b.INPATIENT_ID as id,b.MEDICALRECORD_ID as medicalrecordId,b.IDCARD_NO as idcardNo,b.PATIENT_NAME as patientName,b.IN_DATE as inDate,"
				+ "b.PACT_CODE as pactCode, b.DEPT_CODE as deptCode,b.EMPL_CODE as emplCode,b.FREE_COST as freeCost,"
				+ "b.MONEY_ALERT as moneyAlert,b.BEDINFO_ID as bedId,b.IN_STATE as inState,b.STOP_ACOUNT as stopAcount,"
				+ "b.TOT_COST as totCost,b.REPORT_AGE as reportAge,b.PAYKIND_CODE as paykindCode,"
				+ "b.INPATIENT_NO as inpatientNo,b.BABY_FLAG as babyFlag,b.REPORT_SEX as reportSex,b.CERTIFICATES_NO as certificatesNo,"
				+ "b.DEPT_NAME as deptName, b.EMPL_NAME as emplName "
				+ " from T_INPATIENT_INFO_NOW b left join T_DEPARTMENT_CONTACT f on b.dept_code = f.dept_id "+                 
                 " where b.IN_STATE  ='I' and b.MEDICALRECORD_ID = '"+no+"'";
		if(StringUtils.isNotBlank(dept)){
			hql+=" and f.PARDEPT_ID in(select e.id from t_department_contact e where e.dept_id = '"+dept+"')";
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(hql).addScalar("id").addScalar("medicalrecordId").addScalar("patientName").addScalar("idcardNo")
				.addScalar("inDate",Hibernate.TIMESTAMP).addScalar("pactCode").addScalar("deptCode").addScalar("emplCode")
				.addScalar("freeCost",Hibernate.DOUBLE).addScalar("moneyAlert",Hibernate.DOUBLE).addScalar("bedId")
				.addScalar("inState").addScalar("stopAcount",Hibernate.INTEGER).addScalar("totCost",Hibernate.DOUBLE)
				.addScalar("reportAge",Hibernate.INTEGER).addScalar("paykindCode").addScalar("inpatientNo")
				.addScalar("babyFlag",Hibernate.INTEGER).addScalar("reportSex").addScalar("certificatesNo")
				.addScalar("deptName").addScalar("emplName");
		List<InpatientInfoNow> list= queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
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
	 * @Description： 根据住院流水号查询患者
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public InpatientInfoNow queryByInpatientNot(String inpatientNo) {
		String hql ="from InpatientInfoNow where  inpatientNo=? ";
		List<InpatientInfoNow> infols = super.find(hql, inpatientNo);
		for(int i=0;i<infols.size();i++){
			return infols.get(i);
		}
		return new InpatientInfoNow();
	}
	
	@Override
	public List<InpatientInfoNow> getInfo(String juri,String id, String no, String sTime,String eTime) {
		String hql="from InpatientInfoNow i where 1=1 ";
		if(juri!=null&&"2".equals(juri)){//部门级权限  可以查询该部门下的所有患者
			hql = hql+" AND i.deptCode = '"+id+"'";
		}else{//个人级权限  只可以查询自己治疗的患者
			hql = hql+" AND i.houseDocCode = '"+id+"'";
		}
		if(StringUtils.isNotBlank(sTime)){
			hql = hql+" AND TO_CHAR(i.inDate,'YYYY-MM-DD ') >= '"+sTime+"' ";
		}
		if(StringUtils.isNotBlank(eTime)){
			hql = hql+" AND TO_CHAR(i.inDate,'YYYY-MM-DD') <= '"+eTime+"' ";
		}
		if(StringUtils.isNotBlank(no)){
			hql = hql+" AND (i.medicalrecordId like '%"+no+"' OR i.idcardNo like '%"+no+"' or i.inpatientNo like'%"+no+"')";
		}
		List<InpatientInfoNow>  inpatientInfoList = super.findByObjectProperty(hql, null);
		if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
			return inpatientInfoList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	
	@Override
	public String getIdByNo(String certificatesType,String certificatesNo) {
		return patinmentInnerDao.getIdByNo(certificatesType,certificatesNo);
	}
	
	/**  
	 *  
	 * @Description：  用病历号查询当前住院的患者信息(id是病历号)
	 * @param:id
	 * @Author：zpty
	 * @ModifyDate：2015-8-20
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public InpatientInfoNow getPeopleById(String id) {
		String hql="from InpatientInfoNow i where 1=1 ";
		if(StringUtils.isNotBlank(id)){
			hql = hql+" AND i.medicalrecordId = '"+id+"' ";
		}
		List<InpatientInfoNow>  inpatientInfoList = super.findByObjectProperty(hql, null);
		if(inpatientInfoList!=null&&inpatientInfoList.size()>0){
			return inpatientInfoList.get(0);
		}
		return new InpatientInfoNow();
	}
	@Override
	public List<SysEmployee> employeeComboboxProof(String departmentCode,String q) {
		String sql="select e.EMPLOYEE_JOBNO as jobNo,e.EMPLOYEE_NAME as name, e.EMPLOYEE_PINYIN as pinyin,e.EMPLOYEE_WB as wb,e.EMPLOYEE_INPUTCODE as inputCode from T_EMPLOYEE e where e.del_flg=0 and e.stop_flg=0";
		if(StringUtils.isNotBlank(departmentCode)){
			sql +=" and e.DEPT_ID='"+departmentCode+"' ";
		}
		sql +="and e.EMPLOYEE_TYPE in ('1') ";
		if(StringUtils.isNotBlank(q)){
			sql+=" and (upper(e.EMPLOYEE_ID) like '%"+q+"%' or upper(e.EMPLOYEE_JOBNO) like '%"+q+"%' or e.EMPLOYEE_NAME like '%"+q+"%' or e.EMPLOYEE_PINYIN like '%"+q+"%' or e.EMPLOYEE_WB like '%"+q+"%' or upper(e.EMPLOYEE_INPUTCODE) like '%"+q+"%' )";
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("jobNo").addScalar("name").addScalar("pinyin").addScalar("wb").addScalar("inputCode");
		List<SysEmployee> emplist=queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(emplist!=null&&emplist.size()>0){
			return emplist;
		}
		return new ArrayList<SysEmployee>();
	}

	@Override
	public List<SysEmployee> queryEmpMapPublic() {
		String sql="select e.EMPLOYEE_JOBNO as jobNo,e.EMPLOYEE_NAME as name from T_EMPLOYEE e where e.del_flg=0 and e.stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("jobNo").addScalar("name");
		List<SysEmployee> emplist=queryObject.setResultTransformer(Transformers.aliasToBean(SysEmployee.class)).list();
		if(emplist!=null&&emplist.size()>0){
			return emplist;
		}
		return new ArrayList<SysEmployee>();
	}

	@Override
	public List<SysDepartment> queryDeptMapPublic() {
		String sql="select bd.DEPT_CODE as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd where bd.del_flg=0 and bd.stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("deptName");
		List<SysDepartment> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<BusinessContractunit> queryContractunitListForcombobox() {
		String sql=" select c.UNIT_CODE as encode,c.UNIT_NAME as name from T_BUSINESS_CONTRACTUNIT c where c.del_flg=0 and stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("encode").addScalar("name");
		List<BusinessContractunit> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(BusinessContractunit.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<BusinessContractunit>();
	}

	@Override
	public List<BusinessDictionary> queryDictionaryListForcomboboxPublic(String type, String encode) {
		String sql="select c.CODE_ENCODE as encode,c.CODE_NAME as name from T_BUSINESS_DICTIONARY c where c.del_flg=0 and c.stop_flg=0";
		if(type!=null){
			sql += "and c.CODE_TYPE = '"+type+"' ";
		}
		if(encode!=null){
			sql += "and c.CODE_ENCODE = '"+encode+"' ";
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("encode").addScalar("name");
		List<BusinessDictionary> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(BusinessDictionary.class)).list();
		if(bdl!=null&&bdl.size()>0){
			return bdl;
		}
		return new ArrayList<BusinessDictionary>();
	}

	@Override
	public List<User> queryUserListPublic() {
		String sql="select u.USER_ACCOUNT as account, u.USER_NAME as name from T_SYS_USER u where u.del_flg=0 and u.stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("account").addScalar("name");
		List<User> userl=queryObject.setResultTransformer(Transformers.aliasToBean(User.class)).list();
		if(userl!=null){
			return userl;
		}
		return new ArrayList<User>();
	}

	@Override
	public List<BusinessFrequency> queryFrequencyListPublic() {
		String sql="select f.FREQUENCY_ENCODE as encode,f.FREQUENCY_NAME as name  from T_BUSINESS_FREQUENCY f where f.del_flg=0 and f.stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("encode").addScalar("name");
		List<BusinessFrequency> bfl=queryObject.setResultTransformer(Transformers.aliasToBean(BusinessFrequency.class)).list();
		if(bfl!=null){
			return bfl;
		}
		return new ArrayList<BusinessFrequency>();
	}

	@Override
	public List<BusinessBedward> findNurseCellCode(String inpatientNo) {
		//String hql = "from BusinessBedward t where t.id in(select b.businessBedward from BusinessHospitalbed b where b.id in( select i.bedId from InpatientBedinfoNow i where i.id in(select h.bedId from InpatientInfoNow h where h.inpatientNo = '"+inpatientNo+"'))) and t.stop_flg = 0 and t.del_flg = 0";
		String hql = "from BusinessBedward t where t.id in(select b.businessBedward from BusinessHospitalbed b where b.id in(select h.bedNo from InpatientInfoNow h where h.inpatientNo = '"+inpatientNo+"')) and t.stop_flg = 0 and t.del_flg = 0";
		hql = hql+" ORDER BY t.id";
		List<BusinessBedward> businessBedward=this.getSession().createQuery(hql).list();
		if(businessBedward!=null && businessBedward.size()>0){
			return businessBedward;
		}		
		return new ArrayList<BusinessBedward>();
	}
	
	@Override
	public List<InpatientConsultation> findInpatientConsultation(String inpatientNo) {
		String hql = "from InpatientConsultation t where t.inpatientNo = '"+inpatientNo+"' and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientConsultation> inpatientConsultation=this.getSession().createQuery(hql).list();
		if(inpatientConsultation!=null && inpatientConsultation.size()>0){
			return inpatientConsultation;
		}		
		return new ArrayList<InpatientConsultation>();
	}
	
	@Override
	public List<InpatientKind> treeDrugExe() {
		String hql = "from InpatientKind t where t.stop_flg = 0 and t.del_flg = 0";
		hql = hql+" ORDER BY t.id";
		List<InpatientKind> inpatientKind=this.getSession().createQuery(hql).list();
		if(inpatientKind!=null && inpatientKind.size()>0){
			return inpatientKind;
		}		
		return new ArrayList<InpatientKind>();
	}
	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids,String billName) {
		String hql = "from InpatientExecbill t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(ids)){
			hql =hql+" and t.id='"+ids+"'";
		}
		if(StringUtils.isNotBlank(billName)){
			hql =hql+" and t.billName='"+billName+"'";
		}
		hql = hql+" ORDER BY t.id";
		List<InpatientExecbill> inpatientExecbill=this.getSession().createQuery(hql).list();
		if(inpatientExecbill!=null && inpatientExecbill.size()>0){
			return inpatientExecbill;
		}		
		return new ArrayList<InpatientExecbill>();
	}

	@Override
	public List<InpatientInfoNow> findTree(String id) {
		User user = (User)SessionUtils.getCurrentUserFromShiroSession();
		SysDepartment  dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String userid=user.getAccount();
		String deptcode=dept.getDeptCode();
		String now=DateUtils.formatDateY_M_D_H_M_S(new Date());	
		String hql = null;
		if("1".equals(id)){
			hql =" select t.id as id,t.patientName as patientName,t.inpatientNo as inpatientNo,t.medicalrecordId as medicalrecordId,"
					+ "t.deptCode as deptCode,t.babyFlag as babyFlag,t.reportSex as reportSex,t.freeCost as freeCost,t.pactCode as pactCode,"
					+ "t.leaveFlag as leaveFlag,t.inDate as inDate,t.nurseCellCode as nurseCellCode,t.bedName as bedName,t.totCost as totCost,t.prepayCost as prepayCost from InpatientInfoNow t where "
			        + " t.houseDocCode ='"+userid+"' and t.inState='I' and "
			        + " t.nurseCellCode <> (select department1_.deptCode from DepartmentContact department1_ where department1_.id in"
			        + "(select department2_.pardeptId from DepartmentContact department2_ where department2_.deptCode = '"+deptcode+"' and department2_.referenceType = '03')) order by t.bedName";
		}else if("2".equals(id)){
			hql =" select t.id as id,t.patientName as patientName,t.inpatientNo as inpatientNo,t.medicalrecordId as medicalrecordId,"
					+ "t.deptCode as deptCode,t.babyFlag as babyFlag,t.reportSex as reportSex,t.freeCost as freeCost,t.pactCode as pactCode,"
					+ "t.leaveFlag as leaveFlag,t.inDate as inDate,t.nurseCellCode as nurseCellCode,t.bedName as bedName,t.totCost as totCost,t.prepayCost as prepayCost from InpatientInfoNow t where "
			        + "(t.deptCode='"+deptcode+"' or t.nurseCellCode in(select t1.deptCode from DepartmentContact t1 where t1.id in (select t2.pardeptId from DepartmentContact t2 where t2.deptCode='"+deptcode+"' and t2.referenceType='03'))) and t.inState='I' order by t.bedName";
		}else if("3".equals(id)){
			hql ="select t.id as id,t.patientName as patientName,t.inpatientNo as inpatientNo,t.medicalrecordId as medicalrecordId,"
					+ " t.deptCode as deptCode,t.babyFlag as babyFlag,t.reportSex as reportSex,t.freeCost as freeCost,t.pactCode as pactCode,"
					+ "t.leaveFlag as leaveFlag,t.inDate as inDate,t.nurseCellCode as nurseCellCode,t.bedName as bedName,t.totCost as totCost,t.prepayCost as prepayCost from InpatientInfoNow t where "
					+ "t.inpatientNo in(select i.inpatientNo from InpatientConsultation i where (i.cnslDoccd ='"+userid+"' or i.cnslDeptcd ='"+deptcode+"') and to_date('"+now+"','yyyy-mm-dd hh24:mi:ss') between i.moStdt and i.moEddt and i.del_flg = 0 and i.stop_flg = 0) and t.inState='I' order by t.bedName ";
		}else if("4".equals(id)){
			hql ="select t.id as id,t.patientName as patientName,t.inpatientNo as inpatientNo,t.medicalrecordId as medicalrecordId,"
					+ "t.deptCode as deptCode,t.babyFlag as babyFlag,t.reportSex as reportSex,t.freeCost as freeCost,t.pactCode as pactCode,"
					+ "t.leaveFlag as leaveFlag,t.inDate as inDate,t.nurseCellCode as nurseCellCode,t.bedName as bedName,t.totCost as totCost,t.prepayCost as prepayCost from InpatientInfoNow t where"
					+ " t.inpatientNo in(select i.inpatientNo from InpatientPermission i where (i.deptCode='"+deptcode+"' or i.docCode ='"+userid+"') and to_date('"+now+"','yyyy-mm-dd hh24:mi:ss') between i.moStdt and i.moEddt and i.del_flg = 0) and t.inState='I'  order by t.bedName";
		}
		List<InpatientInfoNow> inpatientList =this.getSession().createQuery(hql).setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(inpatientList!=null && inpatientList.size()>0){
			return inpatientList;
		}
		return new ArrayList<InpatientInfoNow>();
	}
	
	@Override
	public List<InpatientInfoInInterVo> getInpatientInfo(InpatientInfoNow entity) {		
		 /*String sql = "select t.INPATIENT_ID as id,t.INPATIENT_NO as inpatientNo,t.MEDICALRECORD_ID as medicalrecordId,t.PATIENT_NAME as patientName,t.IN_DATE as inDate,t.PACT_CODE as pactCode,t.DEPT_CODE as deptCode,t.BEDINFO_ID as bedId,c.BEDWARD_NURSESTATION as nurseCellCode,t.PREPAY_COST as prepayCost,t.PAY_COST as payCost,t.OWN_COST as ownCost,t.TOT_COST as totCost,t.PUB_COST as pubCost,t.FREE_COST as freeCost,t.IN_STATE as inState,j.bed_name as bedName,m.DEPT_NAME as deptName,n.DEPT_NAME as nurseCellName from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW t ";
		      sql+= " left join "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_BEDWARD c on c.BEDWARD_ID in( select b.BEDWARD_ID from "+HisParameters.HISPARSCHEMAHISUSER+"t_business_hospitalbed b where b.BED_ID in( select i.bed_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_bedinfo_now i where i.bedinfo_id in(select h.bedinfo_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_info_now h where h.INPATIENT_NO = t.INPATIENT_NO))) ";
		       sql+= " left join "+HisParameters.HISPARSCHEMAHISUSER+"t_business_hospitalbed j on j.bed_id in( select k.bed_id from "+HisParameters.HISPARSCHEMAHISUSER+"t_inpatient_bedinfo k where k.bedinfo_id = t.bedinfo_id)";
		       sql+= " left join "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT m on m.DEPT_ID = t.DEPT_CODE";
		       sql+= " left join "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT n on n.DEPT_ID = c.BEDWARD_NURSESTATION";*/
		String sql = "select t.INPATIENT_ID as id,t.INPATIENT_NO as inpatientNo,t.MEDICALRECORD_ID as medicalrecordId,t.PATIENT_NAME as patientName,t.IN_DATE as inDate,t.PACT_CODE as pactCode,t.DEPT_CODE as deptCode,t.BEDINFO_ID as bedId,t.nurse_cell_code as nurseCellCode,t.PREPAY_COST as prepayCost,t.PAY_COST as payCost,t.OWN_COST as ownCost,t.TOT_COST as totCost,t.PUB_COST as pubCost,t.FREE_COST as freeCost,t.IN_STATE as inState,t.bed_name as bedName,t.DEPT_NAME as deptName, t.nurse_cell_name as nurseCellName from "+HisParameters.HISPARSCHEMAHISUSER+"T_INPATIENT_INFO_NOW t ";      
		sql+= " where 1=1 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getInpatientNo())){
				sql = sql+" AND (t.INPATIENT_NO like '%"+entity.getInpatientNo()+"'" + " or t.MEDICALRECORD_ID like '%"+entity.getInpatientNo()+"') ";
			}
			if(StringUtils.isNotBlank(entity.getId())){
				sql = sql+" AND t.INPATIENT_ID = '"+entity.getId()+"'";
			}
		}
		sql = sql+" ORDER BY t.IN_DATE desc nulls last";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
							.addScalar("id")
							.addScalar("inpatientNo")
							.addScalar("medicalrecordId")
							.addScalar("patientName")
							.addScalar("inDate",Hibernate.TIMESTAMP)
							.addScalar("pactCode")
							.addScalar("deptCode")
							.addScalar("bedId")
							.addScalar("nurseCellCode")
							.addScalar("prepayCost",Hibernate.DOUBLE)
							.addScalar("payCost",Hibernate.DOUBLE)
							.addScalar("ownCost",Hibernate.DOUBLE)
							.addScalar("totCost",Hibernate.DOUBLE)
							.addScalar("pubCost",Hibernate.DOUBLE)
							.addScalar("freeCost",Hibernate.DOUBLE)	
							.addScalar("inState")
							.addScalar("bedName")
							.addScalar("deptName")
							.addScalar("nurseCellName");
		List<InpatientInfoInInterVo> inpatientInfo=queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoInInterVo.class)).list();
		if(inpatientInfo!=null && inpatientInfo.size()>0){
			return inpatientInfo;
		}		
		return new ArrayList<InpatientInfoInInterVo>();
	}
	
	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String inpatientNo) {
		String hql = " from InpatientInfoNow t where t.stop_flg=0 and t.del_flg=0 ";
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
			hql =hql+" and t.code='"+id+"'";
		}
		List<DrugInfo> drugInfo=this.getSession().createQuery(hql).list();
		if(drugInfo!=null && drugInfo.size()>0){
			return drugInfo;
		}		
		return new ArrayList<DrugInfo>();
	}

	@Override
	public BusinessMedicalGroupInfo queryMedicalGroupInfo(String docCode) {
		String hql = "select t.id as id, t.businessMedicalgroup as businessMedicalgroup "
				+ "from BusinessMedicalGroupInfo t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(docCode)){
			hql =hql+" and t.doctorId='"+docCode+"'";
		}
		List<BusinessMedicalGroupInfo> list=this.createQuery(hql)
				.setResultTransformer(Transformers.aliasToBean(BusinessMedicalGroupInfo.class)).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}		
		return null;
	}

	@Override
	public List<BusinessContractunit> queryContractunit(String pactCode) {
		String hql = "from BusinessContractunit t where t.stop_flg = 0 and t.del_flg = 0 and t.encode =?";	
		List<BusinessContractunit> businessContractunit=this.createQuery(hql,pactCode).list();
		if(businessContractunit!=null && businessContractunit.size()>0){
			return businessContractunit;
		}		
		return new ArrayList<BusinessContractunit>();
	}

	@Override
	public List<InpatientMedicineListNow> queryInpatientMedicineList(String recipeNo) {
		String hql = "from InpatientMedicineListNow t where t.sequenceNo in(select max(b.sequenceNo) from InpatientMedicineList b where b.recipeNo='"+recipeNo+"') and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientMedicineListNow> inpatientMedicineList=this.getSession().createQuery(hql).list();
		if(inpatientMedicineList!=null && inpatientMedicineList.size()>0){
			return inpatientMedicineList;
		}		
		return new ArrayList<InpatientMedicineListNow>();
	}

	@Override
	public List<InpatientItemListNow> queryInpatientItemList(String recipeNo) {
		String hql = "from InpatientItemListNow t where t.sequenceNo in(select max(b.sequenceNo) from InpatientItemList b where b.recipeNo='"+recipeNo+"') and t.stop_flg = 0 and t.del_flg = 0";
		List<InpatientItemListNow> inpatientItemList=this.getSession().createQuery(hql).list();
		if(inpatientItemList!=null && inpatientItemList.size()>0){
			return inpatientItemList;
		}		
		return new ArrayList<InpatientItemListNow>();
	}

	@Override
	public List<InpatientFeeInfoNow> queryInpatientFeeInfo(String recipeNo) {
		if(StringUtils.isBlank(recipeNo)){
			return new ArrayList<InpatientFeeInfoNow>();
		}
		String hql = "from InpatientFeeInfoNow t where t.stop_flg = 0 and t.del_flg = 0 and t.recipeNo='"+recipeNo
				+"' and t.extFlag1='0' ";	
		List<InpatientFeeInfoNow> inpatientFeeInfo=this.getSession().createQuery(hql).list();
		if(inpatientFeeInfo!=null && inpatientFeeInfo.size()>0){
			return inpatientFeeInfo;
		}		
		return new ArrayList<InpatientFeeInfoNow>();
	}

	@Override
	public List<InsuranceSiitem> queryInsuranceSiitem(String itemCode) {
		String hql = "select t.id as id,t.itemGrade as itemGrade,t.rate as rate from InsuranceSiitem t "
				+ "where t.stop_flg = 0 and t.del_flg = 0 and t.itemCode in"
				+ "(select i.centerCode from InsuranceCompare i where i.hisCode=?)";
		List<InsuranceSiitem> insuranceSiitem=this.createQuery(hql,itemCode)
				.setResultTransformer(Transformers.aliasToBean(InsuranceSiitem.class)).list();
		if(insuranceSiitem!=null && insuranceSiitem.size()>0){
			return insuranceSiitem;
		}		
		return new ArrayList<InsuranceSiitem>();
	}

	@Override
	public List<InpatientSurety> querysuretyCost(String inpatientNo) {
		if(StringUtils.isBlank(inpatientNo)){
			return new ArrayList<InpatientSurety>();
		}
		String hql = "select t.id as id,t.suretyCost as suretyCost from InpatientSurety t where t.stop_flg = 0 and t.del_flg = 0 and t.inpatientNo =?";
		List<InpatientSurety> inpatientSurety=this.createQuery(hql,inpatientNo)
				.setResultTransformer(Transformers.aliasToBean(InpatientSurety.class)).list();
		if(inpatientSurety!=null && inpatientSurety.size()>0){
			return inpatientSurety;
		}		
		return new ArrayList<InpatientSurety>();
	}

	@Override
	public List<User> confirmPassword(User user) {
		String hql = "from User t where t.stop_flg = 0 and t.del_flg = 0 and t.id='"+user.getId()+"' and t.password='"+MD5.MD5Encode(user.getPassword())+"'";
		List<User> userList=this.createQuery(hql).list();
		if(userList!=null && userList.size()>0){
			return userList;
		}	
		return new ArrayList<User>();
	}

	@Override
	public List<InpatientMedicineListNow> queryInpatientMedicineList(String recipeNo, Integer sequenceNo) {
		String hql = "from InpatientMedicineListNow t where t.recipeNo ='"+recipeNo+"' and t.sequenceNo ="
	+sequenceNo+" and t.stop_flg = 0 and t.del_flg = 0 and t.extFlag1=0 ";
		List<InpatientMedicineListNow> inpatientMedicineList=this.getSession().createQuery(hql).list();
		if(inpatientMedicineList!=null && inpatientMedicineList.size()>0){
			return inpatientMedicineList;
		}		
		return new ArrayList<InpatientMedicineListNow>();
	}

	@Override
	public List<InpatientItemListNow> queryInpatientItemList(String recipeNo,Integer sequenceNo) {
		String hql = "from InpatientItemListNow t where t.recipeNo='"+recipeNo+"' and t.sequenceNo ="
	+sequenceNo+" and t.stop_flg = 0 and t.del_flg = 0 and t.extFlag1=0";
		List<InpatientItemListNow> inpatientItemList=this.getSession().createQuery(hql).list();
		if(inpatientItemList!=null && inpatientItemList.size()>0){
			return inpatientItemList;
		}		
		return new ArrayList<InpatientItemListNow>();
	}
	
	@Override
	public List<DrugUndrug> queryNoDrugInfo(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		StringBuffer hql= new StringBuffer("select t.undrugMinimumcost as undrugMinimumcost,t.name as name,");
		hql.append("t.unit as unit,t.defaultprice as defaultprice,t.undrugEquipmentno as undrugEquipmentno ")
		.append(" from DrugUndrug t where t.stop_flg = 0 and t.del_flg = 0 and t.code=?");
		List<DrugUndrug> drugUndrug=this.createQuery(hql.toString(),id)
				.setResultTransformer(Transformers.aliasToBean(DrugUndrug.class)).list();
		if(drugUndrug!=null && drugUndrug.size()>0){
			return drugUndrug;
		}		
		return new ArrayList<DrugUndrug>();
	}

	@Override
	public List<BusinessIcd10> queryICD() {
		String sql="select e.ICD_DIAGNOSTICCODE as code,e.ICD_DIAGNOSTICNAME as name from T_BUSINESS_ICD10  e where e.del_flg=0 and e.stop_flg=0";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("code").addScalar("name");
		List<BusinessIcd10> emplist=queryObject.setResultTransformer(Transformers.aliasToBean(BusinessIcd10.class)).list();
		if(emplist!=null&&emplist.size()>0){
			return emplist;
		}
		return new ArrayList<BusinessIcd10>();
	}

	/**  
	 *  
	 * @Description：查询当前病区
	 * @Author：zhangjin
	 * @CreateDate：2016-10-25 
	 * @version 1.0
	 *
	 */
	@Override
	public List<InfoInInterVo> treeNursegetDept(String deptId) {
		String hql="select f.dept_code as deptCode,f.dept_name as deptName from t_department_contact f "
				+ "where f.stop_flg=0 and f.PARDEPT_ID = "
				+ "(select t.id from t_department_contact t where t.dept_code = '"+deptId+"' and t.reference_type='03' and t.del_flg=0)";
		List<InfoInInterVo> list=this.getSession().createSQLQuery(hql).addScalar("deptCode").addScalar("deptName")
		.setResultTransformer(Transformers.aliasToBean(InfoInInterVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<UndrugZtinfo> queryUndrugById(String undrugId) {
		if(StringUtils.isBlank(undrugId)){
			return new ArrayList<>();
		}
		StringBuffer sbf=new StringBuffer("select t.id as id,t.itemCode as itemCode,t.qty as qty ");
		sbf.append(" FROM UndrugZtinfo t WHERE t.del_flg = 0 and t.stop_flg = 0 and t.itemCode=? ");
		String hql=sbf.toString();
		List<UndrugZtinfo> list= this.createQuery(hql,undrugId)
				.setResultTransformer(Transformers.aliasToBean(UndrugZtinfo.class)).list();
		return list;
	}

	@Override
	public List<InpatientInfoNow> inpatientList(String deptCode, String no,
			String sTime, String eTime, String rows, String page) {
		String hql="from InpatientInfoNow i where i.inState = 'I'";
		hql = join(hql, deptCode, no, sTime, eTime);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int inpatientTotal(String deptCode, String no, String sTime,
			String eTime) {
		String hql="from InpatientInfoNow i where i.inState = 'I'";
		hql = join(hql, deptCode, no, sTime, eTime);
		return super.getTotal(hql);
	}
	
	private String join(String hql, String deptCode, String no, String sTime,
			String eTime) {
		if(StringUtils.isNotBlank(deptCode)){
			hql = hql+" AND i.deptCode = '"+deptCode+"'";
		}
		if(StringUtils.isNotBlank(sTime)){
			hql = hql+" AND TO_CHAR(i.inDate,'YYYY-MM-DD ') >= '"+sTime+"' ";
		}
		if(StringUtils.isNotBlank(eTime)){
			hql = hql+" AND TO_CHAR(i.inDate,'YYYY-MM-DD') <= '"+eTime+"' ";
		}
		if(StringUtils.isNotBlank(no)){
			hql = hql+" AND i.inpatientNo ='"+no+"'";
		}
		return hql;
	}

	
	@Override
	public List<DrugUndrug> queryUnDrug(String id) {
		String hql = "from DrugUndrug t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(id)){
			hql =hql+" and t.id='"+id+"'";
		}
		List<DrugUndrug> drugUndrug=this.createQuery(hql).list();
		if(drugUndrug!=null && drugUndrug.size()>0){
			return drugUndrug;
		}		
		return new ArrayList<DrugUndrug>();
	}

	@Override
	public List<BusinessContractunit> queryContractunit() {
		String hql = "from BusinessContractunit t where t.stop_flg = 0 and t.del_flg = 0";
		List<BusinessContractunit> businessContractunits=this.createQuery(hql).list();
		if(businessContractunits!=null && businessContractunits.size()>0){
			return businessContractunits;
		}		
		return new ArrayList<BusinessContractunit>();
	}

	@Override
	public InpatientInfoNow getInpatientInfoNow(
			DetachedCriteria detachedCriteria) {
		List<InpatientInfoNow> list = (List<InpatientInfoNow>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list!=null){
			return list.get(0);
		}
		return null;
	}
	
	
	

	@Override
	public List<FixedChargeVo> getInpatientBedInfoNow(String inpatientNo) {//SQL查顺序勿动
		if(StringUtils.isBlank(inpatientNo)){inpatientNo="'";}
		String sql="SELECT I.INPATIENT_NO  as inpatientNo,I.PATIENT_NAME as patientName,I.PAYKIND_CODE as paykindCode,I.PACT_CODE as pactCode,I.DEPT_CODE  as deptCode,I.NURSE_CELL_CODE as nurseCellCode,"
				+ "I.HOUSE_DOC_CODE as houseDocCode,U.UNDRUG_ID as undrugId,U.UNDRUG_MINIMUMCOST  as undrugMinimumcost,U.UNDRUG_NAME as undrugName,F.CHARGE_UNITPRICE as chargeUnitprice,I.BABY_FLAG as babyFlag,"
				+ "F.CHARGE_AMOUNT as chargeAmount,F.CREATEUSER as createUser,F.CREATEDEPT as createDept,F.CREATETIME as createTime,F.UPDATEUSER  as updateUser,F.UPDATETIME as updateTime,F.DELETEUSER as deleteUser,"
				+ "F.DELETETIME as deleteTime,I.PREFIXFEE_DATE as prefixfeeDate  FROM  T_INPATIENT_INFO_NOW I,T_BUSINESS_HOSPITALBED B,T_FINANCE_FIXEDCHARGE F,"
				+ "T_DRUG_UNDRUG U  WHERE I.BED_ID = B.BED_ID  AND B.BED_LEVEL = F.CHARGE_BEDLEVEL AND F.UNDRUG_ID = U.UNDRUG_CODE AND"
				+ " I.IN_STATE = 'I'  AND B.BED_STATE IN ('2', '4')  AND B.DEL_FLG = '0'   AND B.STOP_FLG = '0'  "
				+ "AND F.STOP_FLG = '0' AND F.DEL_FLG = '0'  AND U.STOP_FLG = '0' AND U.DEL_FLG = '0'  AND F.CHARGE_STATE = '1'"
				+ " AND (I.PREFIXFEE_DATE < trunc(sysdate) OR I.PREFIXFEE_DATE is null) and i.inpatient_no='"+inpatientNo+"'";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("inpatientNo").addScalar("patientName").addScalar("paykindCode").addScalar("pactCode").addScalar("deptCode").addScalar("nurseCellCode")
		.addScalar("houseDocCode").addScalar("undrugId").addScalar("undrugMinimumcost").addScalar("undrugName").addScalar("chargeUnitprice",Hibernate.DOUBLE).addScalar("babyFlag",Hibernate.INTEGER)
		.addScalar("chargeAmount",Hibernate.DOUBLE).addScalar("createUser").addScalar("createDept").addScalar("createDept").addScalar("createTime",Hibernate.DATE).addScalar("updateTime",Hibernate.DATE)
		.addScalar("deleteUser").addScalar("deleteTime",Hibernate.DATE).addScalar("prefixfeeDate",Hibernate.DATE);
		List<FixedChargeVo> list = query.setResultTransformer(Transformers.aliasToBean(FixedChargeVo.class)).list();
		return list;
	}

	
	@Override
	public List<FixedChargeVo> getInpatientBedInfoTend(String inpatientNo) {//SQL查顺序勿动
		String sql = "SELECT i.INPATIENT_NO as inpatientNo,i.PATIENT_NAME as patientName,i.PAYKIND_CODE as paykindCode,i.PACT_CODE as pactCode,"
				+ "i.DEPT_CODE as deptCode,i.NURSE_CELL_CODE as nurseCellCode,i.HOUSE_DOC_CODE as houseDocCode,u.UNDRUG_ID as undrugId,u.UNDRUG_MINIMUMCOST as undrugMinimumcost,"
				+ "u.UNDRUG_NAME as undrugName,u.UNDRUG_DEFAULTPRICE as undrugDefaultprice,i.BABY_FLAG as babyFlag,u.UNDRUG_UNIT as undrugUnit,o.LIST_DPCD as listDpcd,"
				+ "o.DOC_CODE as houseDocCode,o.DOC_NAME as  houseDocName,o.DEPT_NAME as  deptName,o.LIST_DPCD_NAME as listDpcdName FROM T_INPATIENT_ORDER_NOW o,"
				+ " T_INPATIENT_INFO_NOW i, T_DRUG_UNDRUG u WHERE i.INPATIENT_NO = o.INPATIENT_NO AND u.UNDRUG_CODE = o.ITEM_CODE AND o.CLASS_CODE = 14 AND o.CONFIRM_FLAG = 1"
				+ " AND o.MO_STAT IN ('1', '2') AND o.ITEM_TYPE = '2' AND i.IN_STATE = 'I' AND u.UNDRUG_STATE = '1' AND u.STOP_FLG = '0'  AND u.DEL_FLG = '0'"
				+ "  AND (i.PREFIXFEE_DATE < trunc(sysdate) OR i.PREFIXFEE_DATE is null)  and i.inpatient_no='"+inpatientNo+"'";
		  SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("inpatientNo").addScalar("patientName").addScalar("paykindCode")
		.addScalar("pactCode").addScalar("deptCode").addScalar("nurseCellCode").addScalar("houseDocCode").addScalar("undrugId").addScalar("undrugMinimumcost")
		.addScalar("undrugName").addScalar("undrugDefaultprice",Hibernate.DOUBLE).addScalar("babyFlag",Hibernate.INTEGER).addScalar("undrugUnit").addScalar("listDpcd").addScalar("houseDocCode")
		.addScalar("houseDocName").addScalar("deptName").addScalar("listDpcdName");
		List<FixedChargeVo> list = query.setResultTransformer(Transformers.aliasToBean(FixedChargeVo.class)).list();
		return list;
	}


	@Override
	public List<InpatientOrderNow> getOrders(DetachedCriteria detachedCriteria) {
		List<InpatientOrderNow> list = (List<InpatientOrderNow>) this.getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	
	/**
	 * 根据药品code获取药品部分信息(包装单位、包装数量、价格、药品性质、规格等)
	 * @param itemCode
	 * @return
	 */
	public DrugInfo getDrugInfoByCode(String itemCode){
		if(StringUtils.isBlank(itemCode)){
			return null;
		}
		StringBuffer hql= new StringBuffer("select d.id as id,d.drugPackagingunit as drugPackagingunit,");
		hql.append("d.packagingnum as packagingnum,d.drugRetailprice as drugRetailprice,d.name as name,")
		.append("d.drugMinimumcost as drugMinimumcost,d.spec as spec,d.drugType as drugType,")
		.append("d.drugNature as drugNature from DrugInfo d where d.code=? and d.stop_flg=0 and d.del_flg=0");
		List<DrugInfo> list = this.createQuery(hql.toString(), itemCode).
		setResultTransformer(Transformers.aliasToBean(DrugInfo.class)).list();
		if(list!=null&& list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取非药品明细表(或药品明细表)中某处方号最大的处方内流水号
	 * @param recipeNo 处方号
	 * @param n(1-药品明细表,2-非药品明细表)
	 * @return
	 */
	public int getMaxSequenceNo(String recipeNo,int n){
		StringBuffer hql=new StringBuffer("select max(t.sequenceNo) from ");
		if(n==2){
			hql.append("InpatientItemListNow ");
		}else{
			hql.append("InpatientMedicineListNow ");
		}
		hql.append(" t where t.recipeNo=? and t.stop_flg=0 and t.del_flg=0");
		String s = this.createQuery(hql.toString(), recipeNo).uniqueResult().toString();
		if(s!=null){
			return Integer.parseInt(s);
		}
		return 1;
	}

	@Override
	public String queryMedicalrecordId(String IdcardOrRe) {
		String sql = "select t.MEDICALRECORD_ID as medicalrecordId from t_patient t where (t.CARD_NO='"+IdcardOrRe+"' or t.PATIENT_CERTIFICATESNO='"+IdcardOrRe+"')"
				+ " and t.STOP_FLG=0 and t.DEL_FLG=0";
		List<Patient> list=this.getSession().createSQLQuery(sql).addScalar("medicalrecordId")
		.setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0).getMedicalrecordId();
		}
		return null;
	}
    //查询住院患者信息
	@Override
	public List<FixedChargeVo> getInpatientBedInfo() {
		String sql="SELECT distinct I.INPATIENT_NO  as inpatientNo,I.PATIENT_NAME as patientName,I.PAYKIND_CODE as paykindCode,I.PACT_CODE as pactCode,I.DEPT_CODE as deptCode,I.BABY_FLAG as babyFlag,"
				+ "I.NURSE_CELL_CODE as nurseCellCode,I.HOUSE_DOC_CODE as houseDocCode from  T_INPATIENT_INFO_NOW I ";
		List<FixedChargeVo> list=this.getSession().createSQLQuery(sql).addScalar("inpatientNo").addScalar("patientName")
				.addScalar("paykindCode").addScalar("pactCode").addScalar("deptCode").addScalar("babyFlag",Hibernate.INTEGER).addScalar("nurseCellCode").addScalar("houseDocCode")
				.setResultTransformer(Transformers.aliasToBean(FixedChargeVo.class)).list();
		return list;
	}

	@Override
	public String getDeptArea(String deptCode) {
		String sql="select t.dept_area_code as itemCode from t_department t where t.dept_code='"+deptCode+"'";
		List<FeeInInterVo> list=super.getSession().createSQLQuery(sql)
				.addScalar("itemCode").setResultTransformer(Transformers.aliasToBean(FeeInInterVo.class)).list();
				if(list.size()>0){
					return list.get(0).getItemCode();
				}
		return "";
	}
}
