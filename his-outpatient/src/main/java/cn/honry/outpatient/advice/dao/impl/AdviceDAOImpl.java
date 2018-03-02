package cn.honry.outpatient.advice.dao.impl;

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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
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
import cn.honry.outpatient.advice.dao.AdviceDAO;
import cn.honry.outpatient.advice.vo.DrugAndUnDrugVo;
import cn.honry.outpatient.advice.vo.InpatientInfoVo;
import cn.honry.outpatient.advice.vo.InpatientStatVo;
import cn.honry.outpatient.advice.vo.InspectionReportList;
import cn.honry.outpatient.advice.vo.IreportPatientVo;
import cn.honry.outpatient.advice.vo.KeyValueVo;
import cn.honry.outpatient.advice.vo.LisVo;
import cn.honry.outpatient.advice.vo.OdditionalitemAndUnDrugVo;
import cn.honry.outpatient.advice.vo.OutpatientVo;
import cn.honry.outpatient.advice.vo.PatientVo;
import cn.honry.outpatient.advice.vo.RecipelInfoVo;
import cn.honry.outpatient.advice.vo.RecipelStatVo;
import cn.honry.outpatient.advice.vo.RegisterMainVo;
import cn.honry.outpatient.advice.vo.ViewInfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * 门诊医嘱  DAOImpl
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("adviceDAO")
@SuppressWarnings({ "all" })
public class AdviceDAOImpl extends HibernateEntityDao<OutpatientRecipedetailNow> implements AdviceDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "inpatientKindInInterDAO")
	private InpatientKindInInterDAO InpatientKindInInterDAOInfo;
	
	@Autowired
	@Qualifier(value = "adviceTScInInterDao")
	private AdviceTScInInterDao adviceTScInInterDao;
	
	@Autowired
	@Qualifier(value = "doctorDrugGradeInInterDAO")
	private DoctorDrugGradeInInterDAO doctorDrugGradeInInterDAO;
	
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**  
	 *  
	 * 获得信息树-异步查询-查询患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<KeyValueVo> queryAdviceTreeForPatient(String type) {
		final SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(dept==null){
			return null;
		}
		final SysEmployee emp = (SysEmployee) SessionUtils.getCurrentEmployeeFromShiroSession();
		if(emp==null){
			return null;
		}
		//获得系统参数，是否可查看未分诊信息
		String isSeeNoTriageStr = parameterInnerDAO.getParameterByCode(HisParameters.ISSEENOTRIAGE);
		boolean isSeeNoTriage = false;
		if(HisParameters.ISSEENOTRIAGEONE.equals(isSeeNoTriageStr)){
			isSeeNoTriage = true;
		}
		/**START 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		//获得挂号有效期
		String infoTime = parameterInnerDAO.getParameterByCode(HisParameters.INFOTIME);
		if(StringUtils.isBlank(infoTime)){
			infoTime = "7";
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("DISTINCT m.CARD_NO AS key, ");
		sb.append("m.PATIENT_NAME||'['||m.CARD_NO||']' AS value, ");
		sb.append("m.MEDICALRECORDID AS param ");
		sb.append("FROM T_REGISTER_MAIN_NOW m WHERE m.STOP_FLG =0 AND m.DEL_FLG=0 AND m.IN_STATE = 0 AND m.TRANS_TYPE = 1 ");
		if("1".equals(type)){//待诊
			sb.append("AND m.YNSEE = 0 ");
		}else if("2".equals(type)){//2已诊
			sb.append("AND m.YNSEE = 1 ");
		}
		sb.append("AND m.DEPT_CODE = ? ");
		sb.append("AND m.DOCT_CODE = ? ");
		if("1".equals(type)&&!isSeeNoTriage){//不可以查看未分诊信息
			sb.append("AND m.TRIAGE_FLAG = 1 ");
		}
		sb.append("AND m.REG_DATE >= TO_DATE(?,'yyyy-MM-dd') ");
		sb.append("AND m.REG_DATE <= TO_DATE(?,'yyyy-mm-dd HH24:MI:SS') ");
		//获得当前时间
		final Date endDate = new Date();
		//获得挂号有效期前的日期
		final Date startDate = DateUtils.addDay(endDate, -Integer.parseInt(infoTime)+1);
		/**END 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("key")
						.addScalar("value")
						.addScalar("param");
				return queryObject
						.setString(0, dept.getDeptCode())
						.setString(1, emp.getJobNo())
						.setString(2, DateUtils.formatDateY_M_D(startDate))
						.setString(3, DateUtils.formatDateY_M_D(endDate)+" 23:59:59")
						.setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	/**  
	 *  
	 * 获得信息树-异步查询-查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:type 1待诊2已诊
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<KeyValueVo> queryAdviceTreeForRegister(String type, final String id) {
		final SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		final SysEmployee emp = (SysEmployee) SessionUtils.getCurrentEmployeeFromShiroSession();
		/**START 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		//获得挂号有效期
		String infoTime = parameterInnerDAO.getParameterByCode(HisParameters.INFOTIME);
		if(StringUtils.isBlank(infoTime)){
			infoTime = "7";
		}
		//获得系统参数，是否可查看未分诊信息
		String isSeeNoTriageStr = parameterInnerDAO.getParameterByCode(HisParameters.ISSEENOTRIAGE);
		boolean isSeeNoTriage = false;
		if(HisParameters.ISSEENOTRIAGEONE.equals(isSeeNoTriageStr)){
			isSeeNoTriage = true;
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("m.CLINIC_CODE AS key, ");
		sb.append("'['||TO_CHAR(m.REG_DATE,'yyyy-MM-dd')||']'||'['||m.DEPT_NAME||']'||'['||DECODE(m.YNSEE,0,'<span style=\"color:#FF0000\">未诊出</span>',1,'<span style=\"color:#00FF80\">已诊出</span>')||']' AS value, ");
		sb.append("m.MEDICALRECORDID AS param, ");
		sb.append("m.PATIENT_SEX AS sex ");
		sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_REGISTER_MAIN_NOW m WHERE m.STOP_FLG =0 AND m.DEL_FLG=0 AND m.IN_STATE = 0 AND m.TRANS_TYPE = 1 ");
		if("1".equals(type)){//待诊
			sb.append("AND m.YNSEE = 0 ");
		}else if("2".equals(type)){//2已诊
			sb.append("AND m.YNSEE = 1 ");
		}
		sb.append("AND m.DEPT_CODE = ? ");
		sb.append("AND m.DOCT_CODE = ? ");
		sb.append("AND m.REG_DATE >= TO_DATE(?,'yyyy-MM-dd') ");
		sb.append("AND m.REG_DATE <= TO_DATE(?,'yyyy-mm-dd HH24:MI:SS') ");
		sb.append("AND m.CARD_NO = ? ");
		if("1".equals(type)&&!isSeeNoTriage){//不可以查看未分诊信息
			sb.append("AND m.TRIAGE_FLAG = 1 ");
		}
		//获得当前时间
		final Date endDate = new Date();
		//获得挂号有效期前的日期
		final Date startDate = DateUtils.addDay(endDate, -Integer.parseInt(infoTime)+1);
		/**END 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("key")
						.addScalar("value")
						.addScalar("param")
						.addScalar("sex",Hibernate.INTEGER);
				return queryObject
						.setString(0, dept.getDeptCode())
						.setString(1, emp.getJobNo())
						.setString(2, DateUtils.formatDateY_M_D(startDate))
						.setString(3, DateUtils.formatDateY_M_D(endDate)+" 23:59:59")
						.setString(4, id)
						.setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:idCardNo就诊卡号type类型1已诊2待诊
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PatientVo> queryPatientByidCardNo(final String idCardNo,String type) {
		final SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(dept==null||StringUtils.isBlank(dept.getDeptCode())){
			return null;
		}
		final SysEmployee emp = (SysEmployee) SessionUtils.getCurrentEmployeeFromShiroSession();
		//获得系统参数，是否可查看未分诊信息
		String isSeeNoTriageStr = parameterInnerDAO.getParameterByCode(HisParameters.ISSEENOTRIAGE);
		boolean isSeeNoTriage = false;
		if(HisParameters.ISSEENOTRIAGEONE.equals(isSeeNoTriageStr)){
			isSeeNoTriage = true;
		}
		/**START 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		//获得挂号有效期
		String infoTime = parameterInnerDAO.getParameterByCode(HisParameters.INFOTIME);
		if(StringUtils.isBlank(infoTime)){
			infoTime = "7";
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("m.CARD_NO AS idCardNo, ");
		sb.append("m.PATIENT_NAME AS name, ");
		sb.append("m.DEPT_NAME AS dept, ");
		sb.append("m.DOCT_NAME AS doc, ");
		sb.append("m.CLINIC_CODE AS clinicNo, ");
		sb.append("m.PACT_NAME AS assUnit, ");
		sb.append("m.MEDICALRECORDID AS caseNo, ");
		sb.append("m.PATIENT_SEXNAME AS sex, ");
		sb.append("m.PATIENT_AGE||m.PATIENT_AGEUNIT AS age, ");
		sb.append("m.ORDER_NO AS orderNo, ");
		sb.append("TO_CHAR(m.REG_DATE,'yyyy-MM-dd') AS regDate, ");
		sb.append("DECODE(m.YNSEE,0,'<span style=\"color:#FF0000\">未诊出</span>',1,'<span style=\"color:#00FF80\">已诊出</span>') AS ynsee ");
		sb.append("FROM T_REGISTER_MAIN_NOW m WHERE m.STOP_FLG =0 AND m.DEL_FLG=0 AND m.IN_STATE = 0 AND m.TRANS_TYPE = 1 ");
		if("1".equals(type)){//待诊
			sb.append("AND m.YNSEE = 0 ");
		}else if("2".equals(type)){//2已诊
			sb.append("AND m.YNSEE = 1 ");
		}
		sb.append("AND m.DEPT_CODE = ? ");
		sb.append("AND m.DOCT_CODE = ? ");
		if(!isSeeNoTriage){//不可以查看未分诊信息
			sb.append("AND m.TRIAGE_FLAG = 1 ");
		}
		sb.append("AND m.REG_DATE >= TO_DATE(?,'yyyy-MM-dd') ");
		sb.append("AND m.REG_DATE <= TO_DATE(?,'yyyy-mm-dd HH24:MI:SS') ");
		sb.append("AND m.CARD_NO LIKE ? ");
		sb.append("ORDER BY m.REG_DATE DESC");
		//获得当前时间
		final Date endDate = new Date();
		//获得挂号有效期前的日期
		final Date startDate = DateUtils.addDay(endDate, -Integer.parseInt(infoTime)+1);
		/**END 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		List<PatientVo> voList = (List<PatientVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
				.addScalar("idCardNo")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("doc")
				.addScalar("clinicNo")
				.addScalar("assUnit")
				.addScalar("caseNo")
				.addScalar("sex")
				.addScalar("age")
				.addScalar("orderNo",Hibernate.INTEGER)
				.addScalar("regDate")
				.addScalar("ynsee");
				return queryObject
						.setString(0, dept.getDeptCode())
						.setString(1, emp.getJobNo())
						.setString(2, DateUtils.formatDateY_M_D(startDate))
						.setString(3, DateUtils.formatDateY_M_D(endDate)+" 23:59:59")
						.setString(4, "%"+idCardNo+"%").setResultTransformer(Transformers.aliasToBean(PatientVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	/**  
	 *  
	 * 获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-26 上午11:56:59  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-26 上午11:56:59  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:clinicNo门诊号
	 *
	 */
	@Override
	public PatientVo queryPatientByclinicNo(final String clinicNo) {
		final SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		final SysEmployee emp = (SysEmployee) SessionUtils.getCurrentEmployeeFromShiroSession();
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("m.CARD_NO AS idCardNo, ");
		sb.append("m.PATIENT_NAME AS name, ");
		sb.append("m.DEPT_NAME AS dept, ");
		sb.append("m.DOCT_NAME AS doc, ");
		sb.append("m.CLINIC_CODE AS clinicNo, ");
		sb.append("m.PACT_NAME AS assUnit, ");
		sb.append("m.MEDICALRECORDID AS caseNo, ");
		sb.append("m.PATIENT_SEXNAME AS sex, ");
		sb.append("m.PATIENT_AGE||m.PATIENT_AGEUNIT AS age, ");
		sb.append("m.ORDER_NO AS orderNo ");
		sb.append("FROM T_REGISTER_MAIN_NOW m WHERE m.STOP_FLG =0 AND m.DEL_FLG=0 AND m.IN_STATE = 0 AND m.TRANS_TYPE = 1 ");
		sb.append("AND m.DEPT_CODE = ? ");
		sb.append("AND m.CLINIC_CODE = ?");
		PatientVo vo = (PatientVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("idCardNo")
						.addScalar("name")
						.addScalar("dept")
						.addScalar("doc")
						.addScalar("clinicNo")
						.addScalar("assUnit")
						.addScalar("caseNo")
						.addScalar("sex")
						.addScalar("age")
						.addScalar("orderNo",Hibernate.INTEGER);
				return (PatientVo) queryObject.setString(0, dept.getDeptCode()).setString(1, clinicNo).setResultTransformer(Transformers.aliasToBean(PatientVo.class)).uniqueResult();
			}
		});
		if(vo!=null&&StringUtils.isNotBlank(vo.getName())){
			return vo;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  获得医嘱项目信息-统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int getViewInfoTotal(ViewInfoVo vo) {
		final String hql = getViewInfoHql(vo);
		if(StringUtils.isBlank(hql)){
			return 0;
		}
		List<Object> list = (List<Object>)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				return session.createSQLQuery(hql).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	
	/**  
	 *  
	 * @Description：  获得医嘱项目信息-查询信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ViewInfoVo> getViewInfoPage(final String page, final String rows,ViewInfoVo vo) {
		final String hql = getViewInfoHql(vo);
		if(StringUtils.isBlank(hql)){
			return new ArrayList<ViewInfoVo>();
		}
		List<ViewInfoVo> list = (List<ViewInfoVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(hql)
						.addScalar("id")//id
						.addScalar("name")//名称
						.addScalar("code")//编码
						.addScalar("type")//类型
						.addScalar("sysType")//系统类型
						.addScalar("delFlg",Hibernate.INTEGER)//删除标记
						.addScalar("stopFlg",Hibernate.INTEGER)//停用标记
						.addScalar("price",Hibernate.DOUBLE)//价格
						.addScalar("ty",Hibernate.INTEGER)//药品或非药品
						.addScalar("surSum",Hibernate.INTEGER)//剩余数量
						.addScalar("spec")//规格
						.addScalar("unit")//单位
						.addScalar("insured",Hibernate.INTEGER)//医保标记
						.addScalar("inputcode")//自定义码
						.addScalar("commonName")//通用名
						.addScalar("inspectionsite")//检查检体
						.addScalar("diseaseclassification")//疾病分类
						.addScalar("specialtyName")//专科名称
						.addScalar("medicalhistory")//病史及检查
						.addScalar("requirements")//检查要求
						.addScalar("notes")//注意事项
						.addScalar("minimumUnit")//最小单位
						.addScalar("basicdose",Hibernate.DOUBLE)//基本剂量
						.addScalar("doseunit")//剂量单位
						.addScalar("frequency")//频次
						.addScalar("usemode")//用法
						.addScalar("remark")//备注
						.addScalar("istestsensitivity",Hibernate.INTEGER)//0不需要皮试1青霉素皮试2原药皮试
						.addScalar("dept")//执行科室:从部门表获取
						.addScalar("gbcode")//国家编码
						.addScalar("grade")//药品等级
						.addScalar("instruction")//说明书
						.addScalar("stop_flg",Hibernate.INTEGER)//停用
						.addScalar("minimumcost")//最小费用代码
						.addScalar("packagingnum",Hibernate.INTEGER)//包装数量
						.addScalar("nature")//药品性质
						.addScalar("ismanufacture",Hibernate.INTEGER)//自制药标志
						.addScalar("dosageform")//剂型
						.addScalar("oncedosage",Hibernate.INTEGER)//每次用量
						.addScalar("labsample")//样本类型
						.addScalar("isProvincelimit",Hibernate.INTEGER)//是否省限制
						.addScalar("isCitylimit",Hibernate.INTEGER)//是否市限制
						.addScalar("drugGrade")//药品等级
						.addScalar("isInformedconsent",Hibernate.INTEGER)//是否知情同意书
						.addScalar("restrictionofantibiotic",Hibernate.INTEGER)//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
						.addScalar("splitattr",Hibernate.INTEGER)//药品表拆分属性
						.addScalar("property",Hibernate.INTEGER);//药品拆分维护表属性0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整
				int start = Integer.parseInt(page==null?"1":page);
				int count = Integer.parseInt(rows==null?"20":rows);
				return query.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(ViewInfoVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<ViewInfoVo>();
	}
	
	/**  
	 *  
	 * @Description：  获得医嘱项目信息-hql
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-04 下午02:04:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-04 下午02:04:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String getViewInfoHql(ViewInfoVo vo) {
		StringBuffer buffer = new StringBuffer();
		if(vo.getTy()==null){//药品及非药品联查
			SysDepartment phar = (SysDepartment) SessionUtils.getCurrentUserLoginPharmacyFromShiroSession();
			if(phar==null){//药房为空是返回null
				return null;
			}
			/**业务变更 开立医嘱时需要关联药品拆分属性 2014-03-02 10:55 aizhonghua**/
			SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			if(dept==null){//登录科室为空是返回null
				return null;
			}
			if(StringUtils.isNotBlank(vo.getSysType())){//当系统类别不为null时，查询该类别下的信息
				buffer.append("SELECT ");
				buffer.append("D.DRUG_ID AS id, ");
				buffer.append("D.DRUG_COMMONNAME AS name, ");
				buffer.append("D.DRUG_CODE AS code, ");
				buffer.append("D.DRUG_TYPE AS type, ");
				buffer.append("D.DRUG_SYSTYPE AS sysType, ");//系统类型
				buffer.append("D.DEL_FLG AS delFlg, "); 
				buffer.append("D.STOP_FLG AS stopFlg, "); 
				buffer.append("NVL(D.DRUG_RETAILPRICE,0) AS price, "); 
				buffer.append("'1' AS ty, ");
				buffer.append("NVL(S.STORE_SUM,0)-NVL(S.PREOUT_SUM,0) AS surSum, ");
				buffer.append("D.DRUG_SPEC AS spec, ");//规格
				buffer.append("D.DRUG_PACKAGINGUNIT AS unit, ");//单位
				buffer.append("D.DRUG_ISCOOPERATIVEMEDICAL AS insured, ");//医保标记
				buffer.append("D.DRUG_NAMEINPUTCODE AS inputcode, ");//自定义码
				buffer.append("D.DRUG_NAME AS commonName, ");//通用名
				buffer.append("NULL AS inspectionsite, ");//检查检体
				buffer.append("NULL AS diseaseclassification, ");//疾病分类
				buffer.append("NULL AS specialtyName, ");//专科名称
				buffer.append("NULL AS medicalhistory, ");//病史及检查
				buffer.append("NULL AS requirements, ");//检查要求 
				buffer.append("D.DRUG_NOTES AS notes, ");//注意事项
				buffer.append("D.DRUG_MINIMUMUNIT AS minimumUnit, ");//最小单位
				buffer.append("D.DRUG_BASICDOSE AS basicdose, ");//基本剂量
				buffer.append("D.DRUG_DOSEUNIT AS doseunit, ");//剂量单位
				buffer.append("D.DRUG_FREQUENCY AS frequency, ");//频次
				buffer.append("D.DRUG_USEMODE AS usemode, ");//用法
				buffer.append("D.DRUG_REMARK AS remark, ");//备注
				buffer.append("D.DRUG_ISTESTSENSITIVITY AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
				buffer.append("NULL AS dept, ");//执行科室:从部门表获取
				buffer.append("D.DRUG_GBCODE AS gbcode, ");//国家编码
				buffer.append("D.DRUG_GRADE AS grade, ");//药品等级
				buffer.append("D.DRUG_INSTRUCTION AS instruction, ");//说明书
				buffer.append("D.STOP_FLG AS stop_flg, ");//停用标志
				buffer.append("D.DRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
				buffer.append("D.DRUG_PACKAGINGNUM AS packagingnum, ");//包装数量
				buffer.append("D.DRUG_NATURE AS nature, ");//药品性质
				buffer.append("D.DRUG_ISMANUFACTURE AS ismanufacture, ");//自制药标志
				buffer.append("D.DRUG_DOSAGEFORM AS dosageform, ");//剂型
				buffer.append("D.DRUG_ONCEDOSAGE AS oncedosage, ");//每次用量
				buffer.append("NULL AS labsample, ");//样本类型
				buffer.append("D.DRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
				buffer.append("D.DRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
				buffer.append("D.DRUG_GRADE AS drugGrade, ");//药品等级
				buffer.append("NULL AS isInformedconsent, ");//是否知情同意书
				buffer.append("D.DRUG_RESTRICTIONOFANTIBIOTIC AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
				buffer.append("D.DRUG_SPLITATTR AS splitattr, ");//药品拆分属性
				buffer.append("B.PROPERTY_CODE AS property ");//药品维护拆分属性
				buffer.append("FROM ").append("T_DRUG_INFO D ");
				buffer.append("LEFT JOIN ").append("T_DRUG_STOCKINFO S ");
				buffer.append("ON D.DRUG_CODE = S.DRUG_ID AND S.STORAGE_DEPTID = '").append(phar.getDeptCode()).append("' ");
				buffer.append("LEFT JOIN T_BUSINESS_DRUGPROPERTY B ON D.DRUG_CODE = B.DRUG_CODE AND B.DEPT_CODE = '").append(dept.getDeptCode()).append("' AND B.STOP_FLG = 0 AND B.DEL_FLG = 0 ");
				buffer.append("WHERE D.DRUG_SYSTYPE = '").append(vo.getSysType()).append("' ");
				buffer.append("AND D.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(vo.getName())){
					buffer.append("AND(D.DRUG_NAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEPINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEWB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEINPUTCODE LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_COMMONNAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEPINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEWB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEINPUTCODE LIKE '%").append(vo.getName()).append("%') ");
				}
				buffer.append("UNION ");
				buffer.append("SELECT ");
				buffer.append("U.UNDRUG_ID  AS id, "); 
				buffer.append("U.UNDRUG_NAME AS name, ");
				buffer.append("U.UNDRUG_CODE AS code, "); 
				buffer.append("NULL AS type, ");
				buffer.append("U.UNDRUG_SYSTYPE AS sysType, ");
				buffer.append("U.DEL_FLG AS delFlg, "); 
				buffer.append("U.STOP_FLG AS stopFlg, "); 
				buffer.append("NVL(U.UNDRUG_DEFAULTPRICE,0) AS price, "); 
				buffer.append("'0' AS ty, ");
				buffer.append("NULL AS surSum, ");
				buffer.append("U.UNDRUG_SPEC AS spec, ");//规格
				buffer.append("U.UNDRUG_UNIT AS unit, ");//单位
				buffer.append("U.UNDRUG_ISPROVINCELIMIT AS insured, ");//医保标记
				buffer.append("U.UNDRUG_INPUTCODE AS inputcode, ");//自定义码
				buffer.append("NULL AS commonName, ");//通用名
				buffer.append("U.UNDRUG_INSPECTIONSITE AS inspectionsite, ");//检查检体
				buffer.append("U.UNDRUG_DISEASECLASSIFICATION AS diseaseclassification, ");//疾病分类
				buffer.append("U.UNDRUG_SPECIALTYNAME AS specialtyName, ");//专科名称
				buffer.append("U.UNDRUG_MEDICALHISTORY AS medicalhistory, ");//病史及检查
				buffer.append("U.UNDRUG_REQUIREMENTS AS requirements, ");//检查要求 
				buffer.append("U.UNDRUG_NOTES AS notes, ");//注意事项
				buffer.append("NULL AS minimumUnit, ");//最小单位
				buffer.append("NULL AS basicdose, ");//基本剂量
				buffer.append("NULL AS doseunit, ");//剂量单位
				buffer.append("NULL AS frequency, ");//频次
				buffer.append("NULL AS usemode, ");//用法
				buffer.append("U.UNDRUG_REMARK AS remark, ");//备注
				buffer.append("NULL AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
				buffer.append("U.UNDRUG_DEPT AS dept, ");//执行科室:从部门表获取
				buffer.append("NULL AS gbcode, ");//国家编码
				buffer.append("NULL AS grade, ");//药品等级
				buffer.append("NULL AS instruction, ");//说明书
				buffer.append("U.STOP_FLG AS stop_flg, ");//停用
				buffer.append("U.UNDRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
				buffer.append("NULL AS packagingnum, ");//包装数量
				buffer.append("NULL AS nature, ");//药品性质
				buffer.append("NULL AS ismanufacture, ");//自制药标志
				buffer.append("NULL AS dosageform, ");//剂型
				buffer.append("NULL AS oncedosage, ");//每次用量
				buffer.append("U.UNDRUG_LABSAMPLE AS labsample, ");//样本类型
				buffer.append("U.UNDRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
				buffer.append("U.UNDRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
				buffer.append("NULL AS drugGrade, ");//药品等级
				buffer.append("U.UNDRUG_ISINFORMEDCONSENT AS isInformedconsent, ");//是否知情同意书
				buffer.append("NULL AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
				buffer.append("NULL splitattr, ");//药品拆分属性
				buffer.append("NULL AS property ");//药品维护拆分属性
				buffer.append("FROM ").append("T_DRUG_UNDRUG U "); 
				buffer.append("WHERE U.UNDRUG_SYSTYPE = '").append(vo.getSysType()).append("' ");
				buffer.append("AND U.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(vo.getName())){
					buffer.append("AND (U.UNDRUG_NAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_PINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_WB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_INPUTCODE LIKE '%").append(vo.getName()).append("%') ");
				}
			}else{//当系统类别为null时，查询门诊医嘱可以开立的系统类别所对应的信息
				//获得门诊可以查询的系统类别
				String id = InpatientKindInInterDAOInfo.queryKindInfoByName(HisParameters.ADVICETYPEOUTPATIENT);
				List<CodeSystemtypeVo> voList = adviceTScInInterDao.querySystemTypesByTypeId(id, false);
				if(voList==null||voList.size()==0){
					return null;
				}
				StringBuffer ids = new StringBuffer();
				for(CodeSystemtypeVo svo : voList){
					if(ids.length()>0){
						ids.append(",");
					}
					ids.append("'"+svo.getCode()+"'");
				}
				buffer.append("SELECT "); 
				buffer.append("D.DRUG_ID AS id, ");  
				buffer.append("D.DRUG_COMMONNAME AS name, ");  
				buffer.append("D.DRUG_CODE AS code, ");  
				buffer.append("D.DRUG_TYPE AS type, ");  
				buffer.append("D.DRUG_SYSTYPE AS sysType, "); //系统类型
				buffer.append("D.DEL_FLG AS delFlg, ");  
				buffer.append("D.STOP_FLG AS stopFlg, ");  
				buffer.append("NVL(D.DRUG_RETAILPRICE,0) AS price, ");  
				buffer.append("'1' AS ty, ");  
				buffer.append("NVL(S.STORE_SUM,0)-NVL(S.PREOUT_SUM,0) AS surSum, "); 
				buffer.append("D.DRUG_SPEC AS spec, "); //规格
				buffer.append("D.DRUG_PACKAGINGUNIT AS unit, "); //单位
				buffer.append("D.DRUG_ISCOOPERATIVEMEDICAL AS insured, "); //医保标记
				buffer.append("D.DRUG_NAMEINPUTCODE AS inputcode, "); //自定义码
				buffer.append("D.DRUG_NAME AS commonName, "); //通用名
				buffer.append("NULL AS inspectionsite, "); //检查检体
				buffer.append("NULL AS diseaseclassification, "); //疾病分类
				buffer.append("NULL AS specialtyName, "); //专科名称
				buffer.append("NULL AS medicalhistory, "); //病史及检查
				buffer.append("NULL AS requirements, "); //检查要求 
				buffer.append("D.DRUG_NOTES AS notes, "); //注意事项
				buffer.append("D.DRUG_MINIMUMUNIT AS minimumUnit, "); //最小单位
				buffer.append("D.DRUG_BASICDOSE AS basicdose, "); //基本剂量
				buffer.append("D.DRUG_DOSEUNIT AS doseunit, "); //剂量单位
				buffer.append("D.DRUG_FREQUENCY AS frequency, "); //频次
				buffer.append("D.DRUG_USEMODE AS usemode, "); //用法
				buffer.append("D.DRUG_REMARK AS remark, "); //备注
				buffer.append("D.DRUG_ISTESTSENSITIVITY AS istestsensitivity, "); //0不需要皮试1青霉素皮试2原药皮试
				buffer.append("NULL AS dept, "); //执行科室:从部门表获取
				buffer.append("D.DRUG_GBCODE AS gbcode, "); //国家编码
				buffer.append("D.DRUG_GRADE AS grade, "); //药品等级
				buffer.append("D.DRUG_INSTRUCTION AS instruction, "); //说明书
				buffer.append("D.STOP_FLG AS stop_flg, "); //停用标志
				buffer.append("D.DRUG_MINIMUMCOST AS minimumcost, "); //最小费用代码
				buffer.append("D.DRUG_PACKAGINGNUM AS packagingnum, "); //包装数量
				buffer.append("D.DRUG_NATURE AS nature, "); //药品性质
				buffer.append("D.DRUG_ISMANUFACTURE AS ismanufacture, "); //自制药标志
				buffer.append("D.DRUG_DOSAGEFORM AS dosageform, "); //剂型
				buffer.append("D.DRUG_ONCEDOSAGE AS oncedosage, "); //每次用量
				buffer.append("NULL AS labsample, "); //样本类型
				buffer.append("D.DRUG_ISPROVINCELIMIT AS isProvincelimit, "); //是否省限制
				buffer.append("D.DRUG_ISCITYLIMIT AS isCitylimit, "); //是否市限制
				buffer.append("D.DRUG_GRADE AS drugGrade, "); //药品等级
				buffer.append("NULL AS isInformedconsent, "); //是否知情同意书
				buffer.append("D.DRUG_RESTRICTIONOFANTIBIOTIC AS restrictionofantibiotic, "); //抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
				buffer.append("D.DRUG_SPLITATTR AS splitattr, ");//药品拆分属性
				buffer.append("B.PROPERTY_CODE AS property ");//药品维护拆分属性
				buffer.append("FROM ").append("T_DRUG_INFO D "); 
				buffer.append("LEFT JOIN ").append("T_DRUG_STOCKINFO S ");
				buffer.append("ON D.DRUG_CODE = S.DRUG_ID AND S.STORAGE_DEPTID='").append(phar.getDeptCode()).append("' "); 
				buffer.append("LEFT JOIN T_BUSINESS_DRUGPROPERTY B ON D.DRUG_CODE = B.DRUG_CODE AND B.DEPT_CODE = '").append(dept.getDeptCode()).append("' AND B.STOP_FLG = 0 AND B.DEL_FLG = 0 ");
				buffer.append("WHERE D.DRUG_SYSTYPE IN (").append(ids.toString()).append(") ");
				buffer.append("AND D.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(vo.getName())){
					buffer.append("AND (D.DRUG_NAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEPINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEWB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEINPUTCODE LIKE '%").append(vo.getName()).append("%' ");  
					buffer.append("OR D.DRUG_COMMONNAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEPINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEWB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEINPUTCODE LIKE '%").append(vo.getName()).append("%') "); 
				}
				buffer.append("UNION ");
				buffer.append("SELECT "); 
				buffer.append("U.UNDRUG_ID  AS id, "); 
				buffer.append("U.UNDRUG_NAME AS name, "); 
				buffer.append("U.UNDRUG_CODE AS code, "); 
				buffer.append("NULL AS type, "); 
				buffer.append("U.UNDRUG_SYSTYPE AS sysType, "); 
				buffer.append("U.DEL_FLG AS delFlg, "); 
				buffer.append("U.STOP_FLG AS stopFlg, "); 
				buffer.append("NVL(U.UNDRUG_DEFAULTPRICE,0) AS price, ");
				buffer.append("'0' AS ty, "); 
				buffer.append("NULL AS surSum, ");
				buffer.append("U.UNDRUG_SPEC AS spec, ");//规格
				buffer.append("U.UNDRUG_UNIT AS unit, ");//单位
				buffer.append("U.UNDRUG_ISPROVINCELIMIT AS insured, ");//医保标记
				buffer.append("U.UNDRUG_INPUTCODE AS inputcode, ");//自定义码
				buffer.append("NULL AS commonName, ");//通用名
				buffer.append("U.UNDRUG_INSPECTIONSITE AS inspectionsite, ");//检查检体
				buffer.append("U.UNDRUG_DISEASECLASSIFICATION AS diseaseclassification, ");//疾病分类
				buffer.append("U.UNDRUG_SPECIALTYNAME AS specialtyName, ");//专科名称
				buffer.append("U.UNDRUG_MEDICALHISTORY AS medicalhistory, ");//病史及检查
				buffer.append("U.UNDRUG_REQUIREMENTS AS requirements, ");//检查要求 
				buffer.append("U.UNDRUG_NOTES AS notes, ");//注意事项
				buffer.append("NULL AS minimumUnit, ");//最小单位
				buffer.append("NULL AS basicdose, ");//基本剂量
				buffer.append("NULL AS doseunit, ");//剂量单位
				buffer.append("NULL AS frequency, ");//频次
				buffer.append("NULL AS usemode, ");//用法
				buffer.append("U.UNDRUG_REMARK AS remark, ");//备注
				buffer.append("NULL AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
				buffer.append("U.UNDRUG_DEPT AS dept, ");//执行科室:从部门表获取
				buffer.append("NULL AS gbcode, ");//国家编码
				buffer.append("NULL AS grade, ");//药品等级
				buffer.append("NULL AS instruction, ");//说明书
				buffer.append("U.STOP_FLG AS stop_flg, ");//停用
				buffer.append("U.UNDRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
				buffer.append("NULL AS packagingnum, ");//包装数量
				buffer.append("NULL AS nature, ");//药品性质
				buffer.append("NULL AS ismanufacture, ");//自制药标志
				buffer.append("NULL AS dosageform, ");//剂型
				buffer.append("NULL AS oncedosage, ");//每次用量
				buffer.append("U.UNDRUG_LABSAMPLE AS labsample, ");//样本类型
				buffer.append("U.UNDRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
				buffer.append("U.UNDRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
				buffer.append("NULL AS drugGrade, ");//药品等级
				buffer.append("U.UNDRUG_ISINFORMEDCONSENT AS isInformedconsent, ");//是否知情同意书
				buffer.append("NULL AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
				buffer.append("NULL AS splitattr, ");//药品拆分属性
				buffer.append("NULL AS property ");//药品维护拆分属性
				buffer.append("FROM ").append("T_DRUG_UNDRUG U ");
				buffer.append("WHERE U.UNDRUG_SYSTYPE IN (").append(ids.toString()).append(") ");
				buffer.append("AND U.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(vo.getName())){
					buffer.append("AND (U.UNDRUG_NAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_PINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_WB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_INPUTCODE LIKE '%").append(vo.getName()).append("%') ");
				}
			}
		}else{//单独查询药品或非药品
			if(vo.getTy()==1){//只查询药品
				SysDepartment phar = (SysDepartment) SessionUtils.getCurrentUserLoginPharmacyFromShiroSession();
				if(phar==null){
					return null;
				}
				/**业务变更 开立医嘱时需要关联药品拆分属性 2014-03-02 10:55 aizhonghua**/
				SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				if(dept==null){//登录科室为空是返回null
					return null;
				}
				buffer.append("SELECT ");
				buffer.append("D.DRUG_ID AS id, ");//id
				buffer.append("D.DRUG_COMMONNAME AS name, ");//名称
				buffer.append("D.DRUG_CODE AS code, "); 
				buffer.append("D.DRUG_TYPE AS type, ");//类型
				buffer.append("D.DRUG_SYSTYPE AS sysType, ");//系统类型
				buffer.append("D.DEL_FLG AS delFlg, ");//删除标志
				buffer.append("D.STOP_FLG AS stopFlg, ");//停用标志
				buffer.append("NVL(D.DRUG_RETAILPRICE,0) AS price, ");//价格
				buffer.append("'1' AS ty, ");
				buffer.append("NVL(S.STORE_SUM,0)-NVL(S.PREOUT_SUM,0) AS surSum, ");//剩余数量
				buffer.append("D.DRUG_SPEC AS spec, ");//规格
				buffer.append("D.DRUG_PACKAGINGUNIT AS unit, ");//单位
				buffer.append("D.DRUG_ISCOOPERATIVEMEDICAL AS insured, ");//医保标记
				buffer.append("D.DRUG_NAMEINPUTCODE AS inputcode, ");//自定义码
				buffer.append("D.DRUG_NAME AS commonName, ");//通用名
				buffer.append("NULL AS inspectionsite, ");//检查检体
				buffer.append("NULL AS diseaseclassification, ");//疾病分类
				buffer.append("NULL AS specialtyName, ");//专科名称
				buffer.append("NULL AS medicalhistory, ");//病史及检查
				buffer.append("NULL AS requirements, ");//检查要求 
				buffer.append("D.DRUG_NOTES AS notes, ");//注意事项
				buffer.append("D.DRUG_MINIMUMUNIT AS minimumUnit, ");//最小单位
				buffer.append("D.DRUG_BASICDOSE AS basicdose, ");//基本剂量
				buffer.append("D.DRUG_DOSEUNIT AS doseunit, ");//剂量单位
				buffer.append("D.DRUG_FREQUENCY AS frequency, ");//频次
				buffer.append("D.DRUG_USEMODE AS usemode, ");//用法
				buffer.append("D.DRUG_REMARK AS remark, ");//备注
				buffer.append("D.DRUG_ISTESTSENSITIVITY AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
				buffer.append("NULL AS dept, ");//执行科室:从部门表获取
				buffer.append("D.DRUG_GBCODE AS gbcode, ");//国家编码
				buffer.append("D.DRUG_GRADE AS grade, ");//药品等级
				buffer.append("D.DRUG_INSTRUCTION AS instruction, ");//说明书
				buffer.append("D.STOP_FLG AS stop_flg, ");//停用标志
				buffer.append("D.DRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
				buffer.append("D.DRUG_PACKAGINGNUM AS packagingnum, ");//包装数量
				buffer.append("D.DRUG_NATURE AS nature, ");//药品性质
				buffer.append("D.DRUG_ISMANUFACTURE AS ismanufacture, ");//自制药标志
				buffer.append("D.DRUG_DOSAGEFORM AS dosageform, ");//剂型
				buffer.append("D.DRUG_ONCEDOSAGE AS oncedosage, ");//每次用量
				buffer.append("NULL AS labsample, ");//样本类型
				buffer.append("D.DRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
				buffer.append("D.DRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
				buffer.append("D.DRUG_GRADE AS drugGrade, ");//药品等级
				buffer.append("NULL AS isInformedconsent, ");//是否知情同意书
				buffer.append("D.DRUG_RESTRICTIONOFANTIBIOTIC AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
				buffer.append("D.DRUG_SPLITATTR AS splitattr, ");//药品拆分属性
				buffer.append("B.PROPERTY_CODE AS property ");//药品维护拆分属性
				buffer.append("FROM ").append("T_DRUG_INFO D ");
				buffer.append("LEFT JOIN ").append("T_DRUG_STOCKINFO S ");
				buffer.append("ON D.DRUG_CODE = S.DRUG_ID AND S.STORAGE_DEPTID='").append(phar.getDeptCode()).append("' ");
				buffer.append("LEFT JOIN T_BUSINESS_DRUGPROPERTY B ON D.DRUG_CODE = B.DRUG_CODE AND B.DEPT_CODE = '").append(dept.getDeptCode()).append("' AND B.STOP_FLG = 0 AND B.DEL_FLG = 0 ");
				buffer.append("WHERE D.DRUG_TYPE = '").append(vo.getType()).append("' ");
				buffer.append("AND D.DRUG_SYSTYPE = '").append(vo.getSysType()).append("' ");
				buffer.append("AND D.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(vo.getName())){
					buffer.append("AND(D.DRUG_NAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEPINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEWB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_NAMEINPUTCODE LIKE '%").append(vo.getName()).append("%' "); 
					buffer.append("OR D.DRUG_COMMONNAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEPINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEWB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR D.DRUG_CNAMEINPUTCODE LIKE '%").append(vo.getName()).append("%') ");
				}
			}else if(vo.getTy()==0){//只查询非药品
				buffer.append("SELECT ");
				buffer.append("U.UNDRUG_ID  AS id, "); 
				buffer.append("U.UNDRUG_NAME AS name, "); 
				buffer.append("U.UNDRUG_CODE AS code, "); 
				buffer.append("NULL AS type, ");
				buffer.append("U.UNDRUG_SYSTYPE AS sysType, "); 
				buffer.append("U.DEL_FLG AS delFlg, ");
				buffer.append("U.STOP_FLG AS stopFlg, "); 
				buffer.append("NVL(U.UNDRUG_DEFAULTPRICE,0) AS price, "); 
				buffer.append("'0' AS ty, "); 
				buffer.append("NULL AS surSum, ");
				buffer.append("U.UNDRUG_SPEC AS spec, ");//规格
				buffer.append("U.UNDRUG_UNIT AS unit, ");//单位
				buffer.append("U.UNDRUG_ISPROVINCELIMIT AS insured, ");//医保标记
				buffer.append("U.UNDRUG_INPUTCODE AS inputcode, ");//自定义码
				buffer.append("NULL AS commonName, ");//通用名
				buffer.append("U.UNDRUG_INSPECTIONSITE AS inspectionsite, ");//检查检体
				buffer.append("U.UNDRUG_DISEASECLASSIFICATION AS diseaseclassification, ");//疾病分类
				buffer.append("U.UNDRUG_SPECIALTYNAME AS specialtyName, ");//专科名称
				buffer.append("U.UNDRUG_MEDICALHISTORY AS medicalhistory, ");//病史及检查
				buffer.append("U.UNDRUG_REQUIREMENTS AS requirements, ");//检查要求 
				buffer.append("U.UNDRUG_NOTES AS notes, ");//注意事项
				buffer.append("NULL AS minimumUnit, ");//最小单位
				buffer.append("NULL AS basicdose, ");//基本剂量
				buffer.append("NULL AS doseunit, ");//剂量单位
				buffer.append("NULL AS frequency, ");//频次
				buffer.append("NULL AS usemode, ");//用法
				buffer.append("U.UNDRUG_REMARK AS remark, ");//备注
				buffer.append("NULL AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
				buffer.append("U.UNDRUG_DEPT AS dept, ");//执行科室:从部门表获取
				buffer.append("NULL AS gbcode, ");//国家编码
				buffer.append("NULL AS grade, ");//药品等级
				buffer.append("NULL AS instruction, ");//说明书
				buffer.append("U.STOP_FLG AS stop_flg, ");//停用
				buffer.append("U.UNDRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
				buffer.append("NULL AS packagingnum, ");//包装数量
				buffer.append("NULL AS nature, ");//药品性质
				buffer.append("NULL AS ismanufacture, ");//自制药标志
				buffer.append("NULL AS dosageform, ");//剂型
				buffer.append("NULL AS oncedosage, ");//每次用量
				buffer.append("U.UNDRUG_LABSAMPLE AS labsample, ");//样本类型
				buffer.append("U.UNDRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
				buffer.append("U.UNDRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
				buffer.append("NULL AS drugGrade, ");//药品等级
				buffer.append("U.UNDRUG_ISINFORMEDCONSENT AS isInformedconsent, ");//是否知情同意书
				buffer.append("NULL AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
				buffer.append("NULL AS splitattr, ");//药品拆分属性
				buffer.append("NULL AS property ");//药品维护拆分属性
				buffer.append("FROM ").append("T_DRUG_UNDRUG U ");
				buffer.append("WHERE U.UNDRUG_SYSTYPE = '").append(vo.getSysType()).append("' ");
				buffer.append("AND U.DEL_FLG = 0 ");
				if(StringUtils.isNotBlank(vo.getName())){
					buffer.append("AND (U.UNDRUG_NAME LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_PINYIN LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_WB LIKE '%").append(vo.getName()).append("%' ");
					buffer.append("OR U.UNDRUG_INPUTCODE LIKE '%").append(vo.getName()).append("%')");
				}
			}else{//类别不符合以上条件时返回null
				return null;
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}
	
	/**  
	 *  
	 * @Description：  获得医生职级和药品等级对照关系key药品等级编码value等级名称
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-12 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-12 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> queryJudgeDocDrugGradeMap() {
		Map<String, String> map = new HashMap<String, String>();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(user==null){
			return map;
		}
		String hql = "FROM SysDruggraDecontraStrank d WHERE d.tpost = (SELECT e.title FROM SysEmployee e WHERE e.userId = '"+user.getId()+"')";
		List<SysDruggraDecontraStrank> list = doctorDrugGradeInInterDAO.findByObjectProperty(hql,null);
		if(list!=null&&list.size()>0){
			for(SysDruggraDecontraStrank strank : list){
				map.put(strank.getDruggraade(), strank.getGraadename());
			}
		}
		return map;
	}
	
	/**  
	 *  
	 * @Description：   门诊医嘱-根据药房和id查询药品或非药品库存信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-10 下午06:00:21  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-10 下午06:00:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public DrugAndUnDrugVo finfDrugAndUnDrugById(String adviceId,String minusDeptHid) {
		if(StringUtils.isBlank(adviceId)){
			return null;
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT  ");
		sb.append("U.UNDRUG_ID  AS id, ");//id
		sb.append("U.UNDRUG_NAME AS name, ");//名称
		sb.append("U.UNDRUG_CODE AS code, ");//名称
		sb.append("NULL AS type, ");//药品类型
		sb.append("U.DEL_FLG AS delFlg, ");//删除标记
		sb.append("U.STOP_FLG AS stopFlg, ");//停用标记
		sb.append("NVL(U.UNDRUG_DEFAULTPRICE,0) AS price, ");//价格
		sb.append("NVL(U.UNDRUG_CHILDRENPRICE,0) AS priceChil, ");//儿童价格
		sb.append("0 AS ty, ");//非药品 
		sb.append("NULL AS surSum, ");//剩余数量
		sb.append("NULL AS grade, ");//等级
		sb.append("NULL AS splitattr, ");//拆分属性
		sb.append("U.UNDRUG_ISSUBMIT AS isSubmit, ");//是否终端确认
		sb.append("U.UNDRUG_ISPREORDER AS isMake, ");//是否终端预约
		sb.append("U.UNDRUG_ISSTACK AS isStack ");//是否组套
		sb.append("FROM T_DRUG_UNDRUG U ");
		sb.append("WHERE U.UNDRUG_CODE = '").append(adviceId).append("' ");
		if(StringUtils.isNotBlank(minusDeptHid)){
			sb.append(" UNION ");
			sb.append("SELECT ");
			sb.append("D.DRUG_ID AS id, ");//id
			sb.append("D.DRUG_NAME AS name, ");//名称
			sb.append("D.DRUG_CODE AS code, ");//名称
			sb.append("D.DRUG_TYPE AS type, ");//药品类型
			sb.append("D.DEL_FLG AS delFlg, ");//删除标记
			sb.append("D.STOP_FLG AS stopFlg, ");//停用标记
			sb.append("NVL(D.DRUG_RETAILPRICE,0) AS price, ");//价格
			sb.append("NVL(D.DRUG_RETAILPRICE,0) AS priceChil, ");//儿童价格
			sb.append("1 AS ty, ");//药品
			sb.append("NVL(S.STORE_SUM,0)-NVL(S.PREOUT_SUM,0) AS surSum, ");//剩余数量
			sb.append("D.DRUG_GRADE AS grade, ");//等级
			sb.append("D.DRUG_SPLITATTR AS splitattr, ");//拆分属性
			sb.append("D.DRUG_ISTERMINALSUBMIT AS isSubmit, ");//是否终端确认
			sb.append("NULL AS isMake, ");//是否终端确认
			sb.append("NULL AS isStack ");//是否组套
			sb.append("FROM T_DRUG_INFO D ");
			sb.append("LEFT JOIN T_DRUG_STOCKINFO S ON D.DRUG_CODE = S.DRUG_ID AND S.STORAGE_DEPTID='").append(minusDeptHid).append("' ");	
			sb.append("WHERE D.DRUG_CODE = '").append(adviceId).append("' ");
		}
		List<DrugAndUnDrugVo> list = (List<DrugAndUnDrugVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString())
						.addScalar("id")//id
						.addScalar("name")//名称
						.addScalar("code")//名称
						.addScalar("type")//类型
						.addScalar("delFlg",Hibernate.INTEGER)//删除标记
						.addScalar("stopFlg",Hibernate.INTEGER)//停用标记
						.addScalar("price",Hibernate.DOUBLE)//价格
						.addScalar("priceChil",Hibernate.DOUBLE)//价格
						.addScalar("ty",Hibernate.INTEGER)//药品或非药品
						.addScalar("surSum",Hibernate.INTEGER)//剩余数量
						.addScalar("grade")//等级
						.addScalar("splitattr",Hibernate.INTEGER)//拆分属性
						.addScalar("isSubmit",Hibernate.INTEGER)//是否终端确认
						.addScalar("isMake",Hibernate.INTEGER)//是否终端预约
						.addScalar("isStack",Hibernate.INTEGER);//是否组套
				return query.setResultTransformer(Transformers.aliasToBean(DrugAndUnDrugVo.class)).list();
			}
		});
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description： 获得药品或非药品的附材
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-29 下午03:17:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-29 下午03:17:28  
	 * @ModifyRmk：  ty医嘱类型1药品2非药品 drugUsage药品的用法 undrugId非药品的id
	 * @version 1.0
	 *
	 */
	@Override
	public List<OdditionalitemAndUnDrugVo> findOdditionalitem(Integer ty, String usageNameHid,String adviceId,String deptId) {
		final StringBuffer hql = new StringBuffer();;
		if(ty==1){//药品
			if(StringUtils.isNotBlank(usageNameHid)){
				hql.append("SELECT ");
				hql.append("O.ID AS ID, ");//附材id
				hql.append("O.QTY AS qty, ");//数量
				hql.append("O.PRICE AS price, ");//价格
				hql.append("O.UNIT AS unit, ");//单位
				hql.append("O.TOTAL_PRICE AS totalPrice, ");//总金额
				hql.append("U.UNDRUG_CODE AS unDrugId, ");//非药品id
				hql.append("U.UNDRUG_NAME AS name, ");//非药品名称
				hql.append("U.UNDRUG_SPEC AS spec, ");//非药品规格
				hql.append("U.UNDRUG_SYSTYPE AS sysType, ");//非药品系统类型
				hql.append("U.UNDRUG_MINIMUMCOST AS minimumCost, ");//非药品最小费用
				hql.append("U.UNDRUG_INSPECTIONSITE AS inspectionsite, ");//检查部位或标本
				hql.append("U.UNDRUG_DEPT AS dept ");//执行科室
				hql.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_BUSINESS_ODDITIONALITEM O ");
				hql.append("LEFT JOIN ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_DRUG_UNDRUG U ");
				hql.append("ON U.UNDRUG_CODE = O.ITEM_CODE ");
				hql.append("WHERE "); 
				hql.append("O.DRUG_FLAG = 1 ");
				hql.append("AND O.TYPE_CODE = '").append(usageNameHid).append("' "); 
				hql.append("AND O.DEPT_CODE = '").append(deptId).append("' "); 
				hql.append("AND O.DEL_FLG = 0 "); 
				hql.append("AND O.STOP_FLG = 0"); 
			}
		}else if(ty==2){//非药品
			if(StringUtils.isNotBlank(adviceId)){
				hql.append("SELECT ");
				hql.append("O.ID AS ID, ");//附材id
				hql.append("O.QTY AS qty, ");//数量
				hql.append("O.PRICE AS price, ");//价格
				hql.append("O.UNIT AS unit, ");//单位
				hql.append("O.TOTAL_PRICE AS totalPrice, ");//总金额
				hql.append("U.UNDRUG_CODE AS unDrugId, ");//非药品id
				hql.append("U.UNDRUG_NAME AS name, ");//非药品名称
				hql.append("U.UNDRUG_SPEC AS spec, ");//非药品规格
				hql.append("U.UNDRUG_SYSTYPE AS sysType, ");//非药品系统类型
				hql.append("U.UNDRUG_MINIMUMCOST AS minimumCost, ");//非药品最小费用
				hql.append("U.UNDRUG_INSPECTIONSITE AS inspectionsite, ");//检查部位或标本
				hql.append("U.UNDRUG_DEPT AS dept ");//执行科室
				hql.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_BUSINESS_ODDITIONALITEM O ");
				hql.append("LEFT JOIN ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_DRUG_UNDRUG U ");
				hql.append("ON U.UNDRUG_CODE = O.ITEM_CODE ");
				hql.append("WHERE ");
				hql.append("O.DRUG_FLAG = 2 ");
				hql.append("AND O.TYPE_CODE = '").append(adviceId).append("' "); 
				hql.append("AND O.DEPT_CODE = '").append(deptId).append("' "); 
				hql.append("AND O.DEL_FLG = 0 ");
				hql.append("AND O.STOP_FLG = 0");
			}
		}
		if(StringUtils.isBlank(hql)){
			return null;
		}
		List<OdditionalitemAndUnDrugVo> list = (List<OdditionalitemAndUnDrugVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(hql.toString())
						.addScalar("id")//id
						.addScalar("qty",Hibernate.INTEGER)//数量
						.addScalar("price",Hibernate.DOUBLE)//价格
						.addScalar("unit")//单位
						.addScalar("totalPrice",Hibernate.DOUBLE)//总金额
						.addScalar("unDrugId")//非药品id
						.addScalar("name")//非药品名称
						.addScalar("spec")//非药品规格
						.addScalar("sysType")//非药品系统类型
						.addScalar("minimumCost")//非药品最小费用
						.addScalar("inspectionsite")//检查部位或标本
						.addScalar("dept");//执行科室
				return query.setResultTransformer(Transformers.aliasToBean(OdditionalitemAndUnDrugVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  根据看诊号获得历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OutpatientRecipedetailNow> queryMedicalrecordHisList(final String clinicNo,String para,String q) {
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT R.id AS id, R.SEE_NO AS seeNo, R.CLINIC_CODE AS clinicCode, R.PATIENT_NO AS patientNo, ");
		sb.append("R.REG_DATE AS regDate, R.REG_DEPT AS regDept, R.ITEM_CODE AS itemCode, R.ITEM_NAME AS itemName, R.SPECS AS specs, ");
		sb.append("R.DRUG_FLAG AS drugFlag, R.CLASS_CODE AS classCode, R.FEE_CODE AS feeCode, R.UNIT_PRICE AS unitPrice, R.QTY AS qty, ");
		sb.append("R.DAYS AS days, R.PACK_QTY AS packQty, R.ITEM_UNIT AS itemUnit, R.OWN_COST AS ownCost, R.PAY_COST AS payCost, ");
		sb.append("R.PUB_COST AS pubCost, R.BASE_DOSE AS baseDose, R.SELF_MADE AS selfMade, R.DRUG_QUANLITY AS drugQuanlity, R.ONCE_DOSE AS onceDose, ");
		sb.append("R.ONCE_UNIT AS onceUnit, R.DOSE_MODEL_CODE AS doseModelCode, R.FREQUENCY_CODE AS frequencyCode, R.USAGE_CODE AS usageCode, ");
		sb.append("R.EXEC_DPCD AS execDpcd, R.MAIN_DRUG AS mainDrug, R.COMB_NO AS combNo, R.HYPOTEST AS hypotest, R.INJECT_NUMBER AS injectNumber, ");
		sb.append("R.REMARK AS remark, R.DOCT_CODE AS doctCode, R.DOCT_DPCD AS doctDpcd, R.OPER_DATE AS operDate, R.STATUS AS status, R.CANCEL_USERID AS cancelUserid, ");
		sb.append("R.CANCEL_DATE AS cancelDate, R.EMC_FLAG AS emcFlag, R.LAB_TYPE AS labType, R.CHECK_BODY AS checkBody, R.APPLY_NO AS applyNo, R.SUBTBL_FLAG AS subtblFlag, ");
		sb.append("R.NEED_CONFIRM AS needConfirm, R.CONFIRM_CODE AS confirmCode, R.CONFIRM_DEPT AS confirmDept, R.CONFIRM_DATE AS confirmDate, R.CHARGE_FLAG AS chargeFlag, ");
		sb.append("R.CHARGE_CODE AS chargeCode, R.CHARGE_DATE AS chargeDate, R.RECIPE_NO AS recipeNo, R.PHAMARCY_CODE AS phamarcyCode, R.MINUNIT_FLAG AS minunitFlag, ");
		sb.append("R.DATAORDER AS dataorder, R.PRINT_FLAG AS printFlag, R.CREATEUSER AS createUser, R.CREATEDEPT AS createDept, R.CREATETIME AS createTime, ");
		sb.append("R.UPDATEUSER AS updateUser, R.UPDATETIME AS updateTime, R.DELETEUSER AS deleteUser, R.DELETETIME AS deleteTime, R.STOP_FLG AS stop_flg, ");
		sb.append("R.DEL_FLG AS del_flg, R.SEQUENCE_NO AS sequencenNo, R.RECIPE_FEESEQ AS recipeFeeseq, R.RECIPE_SEQ AS recipeSeq, R.AUDIT_FLG AS auditFlg, ");
		sb.append("R.AUDIT_REMARK AS auditRemark, R.REG_DEPT_NAME AS regDeptName, R.CLASS_NAME AS className, R.FEE_NAME AS feeName, R.ONCE_UNIT_NAME AS onceUnitName, ");
		sb.append("R.DOSE_MODEL_NAME AS doseModelName, R.FREQUENCY_NAME AS frequencyName, R.USAGE_NAME AS usageName, R.EXEC_DPCD_NAME AS execDpcdName, R.DOCT_NAME AS doctName, ");
		sb.append("R.DOCT_DPCD_NAME AS doctDpcdName FROM ").append(StringUtils.isNotBlank(para)?para:"T_OUTPATIENT_RECIPEDETAIL_NOW");
		if(StringUtils.isNotBlank(para)&&"T_OUTPATIENT_RECIPEDETAIL".equals(para)){
			sb.append(ZoneManageUtil.getInstance().getZoneName(HisParameters.HISONLINEDB, para, q, true));
		}
		sb.append(" R ");
		sb.append("WHERE R.CLINIC_CODE = '"+clinicNo+"' and R.STOP_FLG=0 and R.DEL_FLG=0 ORDER BY R.COMB_NO");
		List<OutpatientRecipedetailNow> voList = (List<OutpatientRecipedetailNow>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<OutpatientRecipedetailNow> doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
				.addScalar("id").addScalar("seeNo").addScalar("clinicCode").addScalar("patientNo")
				.addScalar("regDate").addScalar("regDept").addScalar("itemCode").addScalar("itemName")
				.addScalar("specs").addScalar("drugFlag",Hibernate.INTEGER).addScalar("classCode").addScalar("feeCode")
				.addScalar("unitPrice",Hibernate.DOUBLE).addScalar("qty",Hibernate.DOUBLE).addScalar("days",Hibernate.INTEGER).addScalar("packQty",Hibernate.INTEGER)
				.addScalar("itemUnit").addScalar("ownCost",Hibernate.DOUBLE).addScalar("payCost",Hibernate.DOUBLE).addScalar("pubCost",Hibernate.DOUBLE)
				.addScalar("baseDose",Hibernate.DOUBLE).addScalar("selfMade",Hibernate.INTEGER).addScalar("drugQuanlity").addScalar("onceDose",Hibernate.DOUBLE)
				.addScalar("onceUnit").addScalar("doseModelCode").addScalar("frequencyCode").addScalar("usageCode")
				.addScalar("execDpcd").addScalar("mainDrug",Hibernate.INTEGER).addScalar("combNo").addScalar("hypotest",Hibernate.INTEGER).addScalar("injectNumber",Hibernate.INTEGER)
				.addScalar("remark").addScalar("doctCode").addScalar("doctDpcd").addScalar("operDate",Hibernate.DATE).addScalar("status",Hibernate.INTEGER)
				.addScalar("cancelUserid").addScalar("cancelDate",Hibernate.DATE).addScalar("emcFlag",Hibernate.INTEGER).addScalar("labType").addScalar("checkBody")
				.addScalar("applyNo").addScalar("subtblFlag",Hibernate.INTEGER).addScalar("needConfirm",Hibernate.INTEGER).addScalar("confirmCode").addScalar("confirmDept")
				.addScalar("confirmDate",Hibernate.DATE).addScalar("chargeFlag",Hibernate.INTEGER).addScalar("chargeCode").addScalar("chargeDate",Hibernate.DATE)
				.addScalar("recipeNo").addScalar("phamarcyCode").addScalar("minunitFlag",Hibernate.INTEGER).addScalar("dataorder",Hibernate.INTEGER)
				.addScalar("printFlag",Hibernate.INTEGER).addScalar("createUser").addScalar("createDept").addScalar("createTime",Hibernate.DATE)
				.addScalar("updateUser").addScalar("updateTime",Hibernate.DATE).addScalar("deleteUser").addScalar("deleteTime",Hibernate.DATE)
				.addScalar("stop_flg",Hibernate.INTEGER).addScalar("del_flg",Hibernate.INTEGER).addScalar("sequencenNo").addScalar("recipeFeeseq")
				.addScalar("recipeSeq",Hibernate.INTEGER).addScalar("auditFlg",Hibernate.INTEGER).addScalar("auditRemark").addScalar("regDeptName")
				.addScalar("className").addScalar("feeName").addScalar("onceUnitName").addScalar("doseModelName")
				.addScalar("frequencyName").addScalar("usageName").addScalar("execDpcdName").addScalar("doctName")
				.addScalar("doctDpcdName");
				return queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientRecipedetailNow.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description： 根据药品或非药品获得项目
	 * @Author：liudelin
	 * @CreateDate：2015-12-4
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-28 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public ViewInfoVo findDrugAndUnDrugById(String id,Integer drugFlag) {
		final StringBuffer buffer = new StringBuffer();
		if(drugFlag==1){//药品
			SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
			if(dept==null){//登录科室为空是返回null
				return null;
			}
			buffer.append("SELECT ");
			buffer.append("D.DRUG_ID AS id, ");
			buffer.append("D.DRUG_NAME AS name, ");
			buffer.append("D.DRUG_CODE AS code, ");
			buffer.append("D.DRUG_TYPE AS type, ");
			buffer.append("D.DRUG_SYSTYPE AS sysType, ");//系统类型
			buffer.append("D.DEL_FLG AS delFlg, ");
			buffer.append("D.STOP_FLG AS stopFlg, ");
			buffer.append("NVL(D.DRUG_RETAILPRICE,0) AS price, ");
			buffer.append("'1' AS ty, ");
			buffer.append("D.DRUG_SPEC AS spec, ");//规格
			buffer.append("D.DRUG_PACKAGINGUNIT AS unit, ");//单位
			buffer.append("D.DRUG_ISCOOPERATIVEMEDICAL AS insured, ");//医保标记
			buffer.append("D.DRUG_NAMEINPUTCODE AS inputcode, ");//自定义码
			buffer.append("D.DRUG_COMMONNAME AS commonName, ");//通用名
			buffer.append("NULL AS inspectionsite, ");//检查检体
			buffer.append("NULL AS diseaseclassification, ");//疾病分类
			buffer.append("NULL AS specialtyName, ");//专科名称
			buffer.append("NULL AS medicalhistory, ");//病史及检查
			buffer.append("NULL AS requirements, ");//检查要求 
			buffer.append("D.DRUG_NOTES AS notes, ");//注意事项
			buffer.append("D.DRUG_MINIMUMUNIT AS minimumUnit, ");//最小单位
			buffer.append("D.DRUG_BASICDOSE AS basicdose, ");//基本剂量
			buffer.append("D.DRUG_DOSEUNIT AS doseunit, ");//剂量单位
			buffer.append("D.DRUG_FREQUENCY AS frequency, ");//频次
			buffer.append("D.DRUG_USEMODE AS usemode, ");//用法
			buffer.append("D.DRUG_REMARK AS remark, ");//备注
			buffer.append("D.DRUG_ISTESTSENSITIVITY AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
			buffer.append("NULL AS dept, ");//执行科室:从部门表获取
			buffer.append("D.DRUG_GBCODE AS gbcode, ");//国家编码
			buffer.append("D.DRUG_GRADE AS grade, ");//药品等级
			buffer.append("D.DRUG_INSTRUCTION AS instruction, ");//说明书
			buffer.append("D.STOP_FLG AS stop_flg, ");//停用标志
			buffer.append("D.DRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
			buffer.append("D.DRUG_PACKAGINGNUM AS packagingnum, ");//包装数量
			buffer.append("D.DRUG_NATURE AS nature, ");//药品性质
			buffer.append("D.DRUG_ISMANUFACTURE AS ismanufacture, ");//自制药标志
			buffer.append("D.DRUG_DOSAGEFORM AS dosageform, ");//剂型
			buffer.append("D.DRUG_ONCEDOSAGE AS oncedosage, ");//每次用量
			buffer.append("NULL AS labsample, ");//样本类型
			buffer.append("D.DRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
			buffer.append("D.DRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
			buffer.append("D.DRUG_GRADE AS drugGrade, ");//药品等级
			buffer.append("NULL AS isInformedconsent, ");//是否知情同意书
			buffer.append("D.DRUG_RESTRICTIONOFANTIBIOTIC AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
			buffer.append("D.DRUG_SPLITATTR AS splitattr, ");//药品拆分属性
			buffer.append("B.PROPERTY_CODE AS property ");//药品维护拆分属性
			buffer.append("FROM T_DRUG_INFO D ");
			buffer.append("LEFT JOIN T_BUSINESS_DRUGPROPERTY B ON D.DRUG_CODE = B.DRUG_CODE AND B.DEPT_CODE = '").append(dept.getDeptCode()).append("' AND B.STOP_FLG = 0 AND B.DEL_FLG = 0 ");
			buffer.append("WHERE d.DRUG_CODE = '"+id+"' ");
		}else if(drugFlag==0){//非药品
			buffer.append("SELECT "); 
			buffer.append("U.UNDRUG_ID  AS id, "); 
			buffer.append("U.UNDRUG_NAME AS name, "); 
			buffer.append("U.UNDRUG_CODE AS code, "); 
			buffer.append("NULL AS type, "); 
			buffer.append("U.UNDRUG_SYSTYPE AS sysType, "); 
			buffer.append("U.DEL_FLG AS delFlg, ");
			buffer.append("U.STOP_FLG AS stopFlg, ");
			buffer.append("NVL(U.UNDRUG_DEFAULTPRICE,0) AS price, "); 
			buffer.append("'0' AS ty, "); 
			buffer.append("U.UNDRUG_SPEC AS spec, ");//规格
			buffer.append("U.UNDRUG_UNIT AS unit, ");//单位
			buffer.append("U.UNDRUG_ISPROVINCELIMIT AS insured, ");//医保标记
			buffer.append("U.UNDRUG_INPUTCODE AS inputcode, ");//自定义码
			buffer.append("NULL AS commonName, ");//通用名
			buffer.append("U.UNDRUG_INSPECTIONSITE AS inspectionsite, ");//检查检体
			buffer.append("U.UNDRUG_DISEASECLASSIFICATION AS diseaseclassification, ");//疾病分类
			buffer.append("U.UNDRUG_SPECIALTYNAME AS specialtyName, ");//专科名称
			buffer.append("U.UNDRUG_MEDICALHISTORY AS medicalhistory, ");//病史及检查
			buffer.append("U.UNDRUG_REQUIREMENTS AS requirements, ");//检查要求 
			buffer.append("U.UNDRUG_NOTES AS notes, ");//注意事项
			buffer.append("NULL AS minimumUnit, ");//最小单位
			buffer.append("NULL AS basicdose, ");//基本剂量
			buffer.append("NULL AS doseunit, ");//剂量单位
			buffer.append("NULL AS frequency, ");//频次
			buffer.append("NULL AS usemode, ");//用法
			buffer.append("U.UNDRUG_REMARK AS remark, ");//备注
			buffer.append("NULL AS istestsensitivity, ");//0不需要皮试1青霉素皮试2原药皮试
			buffer.append("U.UNDRUG_DEPT AS dept, ");//执行科室:从部门表获取
			buffer.append("NULL AS gbcode, ");//国家编码
			buffer.append("NULL AS grade, ");//药品等级
			buffer.append("NULL AS instruction, ");//说明书
			buffer.append("U.STOP_FLG AS stop_flg, ");//停用
			buffer.append("U.UNDRUG_MINIMUMCOST AS minimumcost, ");//最小费用代码
			buffer.append("NULL AS packagingnum, ");//包装数量
			buffer.append("NULL AS nature, ");//药品性质
			buffer.append("NULL AS ismanufacture, ");//自制药标志
			buffer.append("NULL AS dosageform, ");//剂型
			buffer.append("NULL AS oncedosage, ");//每次用量
			buffer.append("U.UNDRUG_LABSAMPLE AS labsample, ");//样本类型
			buffer.append("U.UNDRUG_ISPROVINCELIMIT AS isProvincelimit, ");//是否省限制
			buffer.append("U.UNDRUG_ISCITYLIMIT AS isCitylimit, ");//是否市限制
			buffer.append("NULL AS drugGrade, ");//药品等级
			buffer.append("U.UNDRUG_ISINFORMEDCONSENT AS isInformedconsent, ");//是否知情同意书
			buffer.append("NULL AS restrictionofantibiotic, ");//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
			buffer.append("NULL AS splitattr, ");//药品拆分属性
			buffer.append("NULL AS property ");//药品维护拆分属性
			buffer.append("FROM T_DRUG_UNDRUG U "); 
			buffer.append("WHERE u.UNDRUG_CODE = '"+id+"' ");
		}
		if(StringUtils.isBlank(buffer.toString())){
			return null;
		}
		List<ViewInfoVo> list = (List<ViewInfoVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(buffer.toString())
						.addScalar("id")//id
						.addScalar("name")//名称
						.addScalar("code")//名称
						.addScalar("type")//类型
						.addScalar("sysType")//系统类型
						.addScalar("delFlg",Hibernate.INTEGER)//删除标记
						.addScalar("stopFlg",Hibernate.INTEGER)//停用标记
						.addScalar("price",Hibernate.DOUBLE)//价格
						.addScalar("ty",Hibernate.INTEGER)//药品或非药品
						.addScalar("spec")//规格
						.addScalar("unit")//单位
						.addScalar("insured",Hibernate.INTEGER)//医保标记
						.addScalar("inputcode")//自定义码
						.addScalar("commonName")//通用名
						.addScalar("inspectionsite")//检查检体
						.addScalar("diseaseclassification")//疾病分类
						.addScalar("specialtyName")//专科名称
						.addScalar("medicalhistory")//病史及检查
						.addScalar("requirements")//检查要求
						.addScalar("notes")//注意事项
						.addScalar("minimumUnit")//最小单位
						.addScalar("basicdose",Hibernate.DOUBLE)//基本剂量
						.addScalar("doseunit")//剂量单位
						.addScalar("frequency")//频次
						.addScalar("usemode")//用法
						.addScalar("remark")//备注
						.addScalar("istestsensitivity",Hibernate.INTEGER)//0不需要皮试1青霉素皮试2原药皮试
						.addScalar("dept")//执行科室:从部门表获取
						.addScalar("gbcode")//国家编码
						.addScalar("grade")//药品等级
						.addScalar("instruction")//说明书
						.addScalar("stop_flg",Hibernate.INTEGER)//停用
						.addScalar("minimumcost")//最小费用代码
						.addScalar("packagingnum",Hibernate.INTEGER)//包装数量
						.addScalar("nature")//药品性质
						.addScalar("ismanufacture",Hibernate.INTEGER)//自制药标志
						.addScalar("dosageform")//剂型
						.addScalar("oncedosage",Hibernate.INTEGER)//每次用量
						.addScalar("labsample")//样本类型
						.addScalar("isProvincelimit",Hibernate.INTEGER)//是否省限制
						.addScalar("isCitylimit",Hibernate.INTEGER)//是否市限制
						.addScalar("drugGrade")//药品等级
						.addScalar("isInformedconsent",Hibernate.INTEGER)//是否知情同意书
						.addScalar("restrictionofantibiotic",Hibernate.INTEGER)//抗菌药限制1非抗菌药2无限制3职级限制4特殊管理
						.addScalar("splitattr",Hibernate.INTEGER)//药品表拆分属性
						.addScalar("property",Hibernate.INTEGER);//药品拆分维护表属性0-不可拆分  1-可拆分,配药时不取整  2-可拆分配药时上取整 3不可拆分，当日取整
				return query.setResultTransformer(Transformers.aliasToBean(ViewInfoVo.class)).list();
			}
		});
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}

	/**  
	 *  
	 * @Description：查询历史医嘱
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<KeyValueVo> queryHisAdvice(final String patientNo,String isParDb) {
		final StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("m.CLINIC_CODE AS key, ");
		sb.append("'['||m.DEPT_NAME||']['||m.DOCT_NAME||']");
		sb.append(" '||m.PATIENT_NAME||' ");
		sb.append("['||m.CLINIC_CODE||']");
		sb.append("['||DECODE(m.YNSEE,0,'<span style=\"color:#FF0000\">未诊出</span>',1,'<span style=\"color:#00FF80\">已诊出</span>')||']' AS value, ");
		sb.append("'T_OUTPATIENT_RECIPEDETAIL_NOW' AS param ");
		sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_REGISTER_MAIN_NOW m WHERE m.MEDICALRECORDID = ? AND m.IN_STATE = 0");
		if("1".equals(isParDb) ){//如果分库需要关联历史表中的信息
			sb.append("UNION ALL ");
			sb.append("SELECT ");
			sb.append("r.CLINIC_CODE AS key, ");
			sb.append("'['||r.DEPT_NAME||']['||r.DOCT_NAME||']");
			sb.append(" '||r.PATIENT_NAME||' ");
			sb.append("['||r.CLINIC_CODE||']");
			sb.append("['||DECODE(r.YNSEE,0,'<span style=\"color:#FF0000\">未诊出</span>',1,'<span style=\"color:#00FF80\">已诊出</span>')||']' AS value, ");
			sb.append("'T_OUTPATIENT_RECIPEDETAIL' AS param ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_REGISTER_MAIN r WHERE r.MEDICALRECORDID = ").append(patientNo).append(" AND r.IN_STATE = 0");
		}
		List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("key")
						.addScalar("value")
						.addScalar("param",Hibernate.STRING);
				return queryObject.setString(0, patientNo).setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}

	/**  
	 *  
	 * @Description： 根据处方id获得处方
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OutpatientRecipedetailNow> getAdviceListByIds(String id) {
		final String[] ids = id.split(",");
		final String hql="FROM OutpatientRecipedetailNow r WHERE r.id IN (:ids) ";
		List<OutpatientRecipedetailNow> list = (List<OutpatientRecipedetailNow>) this.getHibernateTemplate().execute(new HibernateCallback(){
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query queryObject = session.createQuery(hql);
				return queryObject.setParameterList("ids", ids).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**  
	 *  
	 * @Description： 获得医生可开立的全部特限药品
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, String> querySpecialDrugMap() {
		Map<String, String> map = new HashMap<String, String>();
		final String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId();//当前科室
		final String empId = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getId();
		final String sql = "SELECT DISTINCT(s.DRUG_CODE) AS key,s.TRADE_NAME AS value FROM T_BUSINESS_SPEDRUG s WHERE ((s.SPE_TYPE = 0 AND s.SPE_CODE = ?) OR (s.SPE_TYPE = 1 AND s.SPE_CODE = ?)) AND s.STOP_FLG = 0 AND s.DEL_FLG = 0";
		List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql)
						.addScalar("key")
						.addScalar("value");
				return queryObject.setString(0, deptId).setString(1, empId).setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			for(KeyValueVo vo : voList){
				map.put(vo.getKey(), vo.getValue());
			}
		}
		return map;
	}

	/**  
	 *  
	 * @Description：  查询该医师是否有审核权限
	 * @Author：aizhonghua
	 * @CreateDate：2015-01-21 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-01-21 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean queryAuditing() {
		SysEmployee emp = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		if(emp==null){
			return false;
		}
		BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("title",emp.getTitle());
		if(dictionary==null){
			return false;
		}
		String val = parameterInnerDAO.getParameterByCode("yzshzj");
		if(StringUtils.isBlank(val)){
			return false;
		}
		if(val.contains(",")){
			String[] valArr = val.split(",");
			for(int i=0;i<valArr.length;i++){
				if(valArr[i].equals(dictionary.getEncode())){
					return true;
				}
			}
		}else{
			if(val.equals(dictionary.getEncode())){
				return true;
			}
		}
		return false;
	}

	/**  
	 *  
	 * @Description： 获得待审核的患者信息树
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<KeyValueVo> queryAuditTree(final String id) {
		final String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		final StringBuffer sb = new StringBuffer();
		/**START 业务变更 查询挂号有效期内的挂号信息 2017-03-01 aizhonghua**/
		//获得挂号有效期
		String infoTime = parameterInnerDAO.getParameterByCode(HisParameters.INFOTIME);
		if(StringUtils.isBlank(infoTime)){
			infoTime = "7";
		}
		//获得当前时间
		final Date endDate = new Date();
		//获得挂号有效期前的日期
		final Date startDate = DateUtils.addDay(endDate, -Integer.parseInt(infoTime)+1);
		if(StringUtils.isBlank(id)){
			sb.append("SELECT ");
			sb.append("DISTINCT r.DOCT_CODE AS key, ");
			sb.append("e.EMPLOYEE_NAME||'['||r.DOCT_CODE||']' AS value ");
			sb.append("FROM ");
			sb.append("T_OUTPATIENT_RECIPEDETAIL_NOW r ");
			sb.append("LEFT JOIN T_EMPLOYEE e ON e.EMPLOYEE_JOBNO = r.DOCT_CODE ");
			sb.append("WHERE "); 
			sb.append("r.STATUS = 4 ");
			sb.append("AND r.DEL_FLG = 0 ");
			sb.append("AND r.STOP_FLG = 0 ");
			sb.append("AND r.REG_DEPT = ? ");
			sb.append("AND r.OPER_DATE >= TO_DATE(?,'yyyy-MM-dd') ");
			sb.append("AND r.OPER_DATE <= TO_DATE(?,'yyyy-mm-dd HH24:MI:SS') ");
			List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString())
							.addScalar("key")
							.addScalar("value");
					return queryObject.setString(0,deptCode).setString(1,DateUtils.formatDateY_M_D(startDate)).setString(2, DateUtils.formatDateY_M_D(endDate)+" 23:59:59").setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
				}
			});
			if(voList!=null&&voList.size()>0){
				return voList;
			}
			return null;
		}else{
			sb.append("SELECT ");
			sb.append("DISTINCT r.CLINIC_CODE AS key, ");
			sb.append("m.PATIENT_NAME||'['||r.PATIENT_NO||']['||r.CLINIC_CODE||']' AS value, ");
			sb.append("r.PATIENT_NO AS param, ");
			sb.append("m.PATIENT_SEX AS sex ");
			sb.append("FROM ");
			sb.append("T_OUTPATIENT_RECIPEDETAIL_NOW r ");
			sb.append("INNER JOIN T_REGISTER_MAIN_NOW m ON m.CLINIC_CODE = r.CLINIC_CODE ");
			sb.append("WHERE ");
			sb.append("r.STATUS = 4 ");
			sb.append("AND r.DEL_FLG = 0 ");
			sb.append("AND r.STOP_FLG = 0 ");
			sb.append("AND r.DOCT_CODE = ? ");
			sb.append("AND r.REG_DEPT = ? ");
			sb.append("AND r.OPER_DATE >= TO_DATE(?,'yyyy-MM-dd') ");
			sb.append("AND r.OPER_DATE <= TO_DATE(?,'yyyy-mm-dd HH24:MI:SS') ");
			List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sb.toString())
							.addScalar("key")
							.addScalar("value")
							.addScalar("param")
							.addScalar("sex",Hibernate.INTEGER);
					return queryObject.setString(0,id).setString(1,deptCode).setString(2,DateUtils.formatDateY_M_D(startDate)).setString(3, DateUtils.formatDateY_M_D(endDate)+" 23:59:59").setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
				}
			});
			if(voList!=null&&voList.size()>0){
				return voList;
			}
			return null;
		}
	}

	/**  
	 *  
	 * @Description：查询历史医嘱（加载更多）
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<KeyValueVo> queryHisAdviceNext(String id,final String patientNo,String para) {
		final StringBuffer sb = new StringBuffer();
		if("1".equals(id)){//分库
			
		}else{//不分库
			sb.append("SELECT ");
			sb.append("r.CLINIC_CODE AS key, ");
			sb.append("'['||r.DEPT_NAME||']['||r.DOCT_NAME||']");
			sb.append(" '||r.PATIENT_NAME||' ");
			sb.append("['||r.CLINIC_CODE||']");
			sb.append("['||DECODE(r.YNSEE,0,'<span style=\"color:#FF0000\">未诊出</span>',1,'<span style=\"color:#00FF80\">已诊出</span>')||']' AS value, ");
			sb.append("'T_OUTPATIENT_RECIPEDETAIL' AS param ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER);
			sb.append("T_REGISTER_MAIN");
			if(StringUtils.isNotBlank(para)){
				sb.append(ZoneManageUtil.getInstance().getZoneName(HisParameters.HISONLINEDB, "T_REGISTER_MAIN", para, true));
			}
			sb.append(" r WHERE r.MEDICALRECORDID = ? AND r.IN_STATE = 0");
		}
		List<KeyValueVo> voList = (List<KeyValueVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("key")
						.addScalar("value")
						.addScalar("param",Hibernate.STRING);
				return queryObject.setString(0, patientNo).setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return null;
	}
	
	/**获取最小最大时间
	 * @Description 
	 * @author  zhangjin
	 * @createDate： 2016年12月3日 
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public StatVo findMaxMin() {
		final String sql = "SELECT MAX(mn.FEE_DATE) AS eTime ,MIN(mn.FEE_DATE) AS sTime FROM t_sto_recipe_now mn";
		StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	/**  
	 *  
	 * 获得处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> queryPatientInfo(final String page,final String rows,final String startTime,final String endTime,final List<String> tnL,String type,String para,String vague) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(tnL==null||tnL.size()<0){
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RecipelStatVo>());
			return retMap;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				buffer.append("UNION ALL ");
			}
			buffer.append("SELECT DISTINCT ");
			buffer.append("r.RECIPE_NO AS recipeNo, ");//处方号
			buffer.append("'").append("T_STO_RECIPE_NOW".equals(tnL.get(i))?"T_DRUG_APPLYOUT_NOW":"T_DRUG_APPLYOUT").append("' tab, ");//详情信息需要查询的表T_DRUG_APPLYOUT/T_DRUG_APPLYOUT_NOW
			buffer.append("r.PATIENT_NAME AS name, ");//患者姓名
			buffer.append("r.SEX_NAME sex, ");//性别
//			buffer.append("r.BIRTHDAY age, ");//年龄
			buffer.append("r.CARD_NO recordNo, ");//病历号
			buffer.append("r.INVOICE_NO invoiceNo, ");//发票号
			buffer.append("r.SEND_TERMINAL_NAME disTable, ");//配药台
			buffer.append("r.DRUGED_OPER_NAME disUser, ");//配药人
			buffer.append("TO_CHAR(r.DRUGED_DATE,'yyyy-MM-dd') disTime, ");//配药时间
			buffer.append("r.SEND_TERMINAL_NAME medTable, ");//发药台
			buffer.append("r.SEND_OPER_NAME medUser, ");//发药人
			buffer.append("TO_CHAR(r.SEND_DATE,'yyyy-MM-dd') medTime, ");//发药时间
			buffer.append("r.DOCT_NAME squareDoc ");//开方医生
			buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" r ");
			buffer.append("WHERE r.STOP_FLG = :STOP_FLG ");
			buffer.append("AND r.DEL_FLG = :DEL_FLG ");
			buffer.append("AND r.DOCT_CODE IS NOT NULL ");
			buffer.append("AND r.VALID_STATE = :VALID_STATE ");
			buffer.append("AND r.FEE_DATE>=TO_DATE(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND r.FEE_DATE<=TO_DATE(:endTime, 'yyyy-mm-dd hh24:mi:ss') "); 
			if("0".equals(vague)){//精确查询
				if(StringUtils.isNotBlank(para)){
					//类型0全部1病历卡号2发票号3姓名4处方号
					if("0".equals(type)){
						buffer.append("AND (r")
						.append(".CARD_NO = :para OR r")
						.append(".INVOICE_NO = :para OR r")
						.append(".PATIENT_NAME = :para OR r")
						.append(".RECIPE_NO = :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND r").append(".CARD_NO = :para ");
					}else if("2".equals(type)){
						buffer.append("AND r").append(".INVOICE_NO = :para ");
					}else if("3".equals(type)){
						buffer.append("AND r").append(".PATIENT_NAME = :para ");
					}else if("4".equals(type)){
						buffer.append("AND r").append(".RECIPE_NO = :para ");
					}
				}
			}else{//模糊查询
				if(StringUtils.isNotBlank(para)){
					//类型0全部1病历卡号2发票号3姓名4处方号
					if("0".equals(type)){
						buffer.append("AND (r")
						.append(".CARD_NO LIKE :para OR r")
						.append(".INVOICE_NO LIKE :para OR r")
						.append(".PATIENT_NAME LIKE :para OR r")
						.append(".RECIPE_NO LIKE :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND r").append(".CARD_NO LIKE :para ");
					}else if("2".equals(type)){
						buffer.append("AND r").append(".INVOICE_NO LIKE :para ");
					}else if("3".equals(type)){
						buffer.append("AND r").append(".PATIENT_NAME LIKE :para ");
					}else if("4".equals(type)){
						buffer.append("AND r").append(".RECIPE_NO LIKE :para ");
					}
				}
			}
		}
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("STOP_FLG", 0);
		paraMap.put("DEL_FLG", 0);
//		paraMap.put("DOCT_CODE", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		paraMap.put("VALID_STATE", 1);
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		if(StringUtils.isNotBlank(para)){
			if("0".equals(vague)){
				paraMap.put("para", para);
			}else{
				paraMap.put("para", "%"+para+"%");
			}
		}
		
		//查询总条数
		StringBuffer bufferTotal = new StringBuffer(buffer.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		int total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
				
		//查询对象的sql
		StringBuffer bufferRows = new StringBuffer(buffer.toString());
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :end ) WHERE rn > :start ");
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("end", start * count);
		List<RecipelStatVo> voList =  namedParameterJdbcTemplate.query(bufferRows.toString(),paraMap,new RowMapper<RecipelStatVo>() {
			@Override
			public RecipelStatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecipelStatVo vo = new RecipelStatVo();
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setTab(rs.getString("tab"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
//				vo.setAge(rs.getString("age"));
				vo.setRecordNo(rs.getString("recordNo"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setDisTable(rs.getString("disTable"));
				vo.setDisUser(rs.getString("disUser"));
				vo.setDisTime(rs.getString("disTime"));
				vo.setMedTable(rs.getString("medTable"));
				vo.setMedUser(rs.getString("medUser"));
				vo.setMedTime(rs.getString("medTime"));
				vo.setSquareDoc(rs.getString("squareDoc"));
				return vo;
		}});
		retMap.put("total", total);
		retMap.put("rows", voList==null?new ArrayList<RecipelStatVo>():voList);
		return retMap;
	}
	
	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RecipelInfoVo> getRecipelInfoRows(String recipeNo,String tab) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("a.TRADE_NAME||'['||a.SPECS||']' goodsName, ");//商品名
		buffer.append("a.DOSE_ONCE oneDosage, ");//每次量
		buffer.append("a.USE_NAME usage, ");//用法
		buffer.append("a.DFQ_CEXP frequency, ");//频次
		buffer.append("a.APPLY_NUM gross, ");//总量
		buffer.append("a.RETAIL_PRICE retailPrice, ");//零售价
		buffer.append("NVL(a.APPLY_NUM,1)*NVL(a.RETAIL_PRICE,0) money, ");//金额
		buffer.append("a.DAYS dosageNum, ");//剂数
		buffer.append("a.VALID_STATE validity ");//有效性
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tab).append(" a ");
		buffer.append("WHERE a.STOP_FLG = 0 ");
		buffer.append("AND a.DEL_FLG = 0 ");
		buffer.append("AND a.RECIPE_NO = :recipeNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("recipeNo", recipeNo);
		List<RecipelInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<RecipelInfoVo>() {
			@Override
			public RecipelInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecipelInfoVo vo = new RecipelInfoVo();
				vo.setGoodsName(rs.getString("goodsName"));
				vo.setOneDosage(rs.getString("oneDosage"));
				vo.setUsage(rs.getString("usage"));
				vo.setFrequency(rs.getString("frequency"));
				vo.setGross(rs.getString("gross"));
				vo.setRetailPrice(rs.getDouble("retailPrice"));
				vo.setMoney(rs.getDouble("money"));
				vo.setDosageNum(rs.getInt("dosageNum"));
				vo.setValidity(rs.getString("validity"));
				return vo;
		}});
		if(voList!=null&&voList.size()>0){
			RecipelInfoVo vo = new RecipelInfoVo();
			vo.setGoodsName("合计");
			vo.setMoney(0d);
			for(RecipelInfoVo info : voList){
				vo.setMoney(vo.getMoney()+(info.getMoney()==null?0d:info.getMoney()));
			}
			voList.add(vo);
			return voList;
		}
		return new ArrayList<RecipelInfoVo>();
	}
	/**  
	 * 
	 * 打印医嘱患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午5:26:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午5:26:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<RegisterMainVo> getRegisterMainVo(List<String> array) {
		String sql="SELECT "
				+"DISTINCT "
				+"i.DEPT_NAME AS dept," 
				+"o.RECIPE_NO AS recipeNo,"
				+"i.pact_code AS code,"
				+"i.pact_name AS cont,"
				+"TO_CHAR(o.OPER_DATE,'yyyy-MM-dd HH:mm:ss') AS time,"
				+"i.MEDICALRECORDID AS mediNo,"
				+"i.PATIENT_NAME AS name,"
				+"DECODE(i.PATIENT_SEX,1, '男',2,'女',3,'未知') AS sex,"
				+"i.PATIENT_AGE||i.patient_ageunit AS age,"
				+"i.DOCT_NAME AS dct,"
				+"m.MAINDESC||','||m.DIAGNOSE1 AS dia,"
				+"(SELECT to_char(Sum(Nvl(f.Pay_Cost, 0)),'FM99999990.0999') FROM T_OUTPATIENT_RECIPEDETAIL_now f WHERE f.RECIPE_NO = o.RECIPE_NO)||'元' AS pay "
				+"FROM t_Register_Main_Now i " 
				+"INNER JOIN T_OUTPATIENT_RECIPEDETAIL_NOW o ON o.RECIPE_NO in (:recipeNos) "
				+"LEFT JOIN T_OUTPATIENT_MEDICALRECORD m ON m.CLINIC_CODE = i.clinic_code "
						+"WHERE " 
						+"i.clinic_code in " 
						+"(SELECT DISTINCT o.CLINIC_CODE "  
								+"FROM T_OUTPATIENT_RECIPEDETAIL_NOW o " 
								+"WHERE o.RECIPE_NO in (:recipeNos) )";
		Map<String,Object> paramMap = new HashMap<String, Object>();
		if(array!=null&&array.size()>0){
			paramMap.put("recipeNos", array);
		}
		List<RegisterMainVo> voList =  namedParameterJdbcTemplate.query(sql,paramMap,new RowMapper<RegisterMainVo>() {
			@Override
			public RegisterMainVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegisterMainVo vo = new RegisterMainVo();
				vo.setDept(rs.getString("dept"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setCode(rs.getString("code"));
				vo.setCont(rs.getString("cont"));
				vo.setTime(rs.getString("time"));
				vo.setMediNo(rs.getString("mediNo"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setDct(rs.getString("dct"));
				vo.setDia(rs.getString("dia"));
				vo.setPay(rs.getString("pay"));
				return vo;
		}});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<RegisterMainVo>();
	}
	/**  
	 * 
	 * 打印医嘱处方信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月7日 下午5:26:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月7日 下午5:26:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientVo> getOutpatientVo(String clinicCode,String patientNo) {
		String sql="SELECT " 
				+"r.ITEM_NAME AS name,"
				+"r.RECIPE_NO as recipeNo,"
				+"r.SPECS AS specs,"
				+"r.BASE_DOSE AS dose,"
				+"r.USAGE_NAME  AS usage,"
				+"r.FREQUENCY_NAME AS freq,"
				+"r.QTY|| r.once_unit_name AS qty,"
				+"r.REMARK AS rema,"
				+"r.comb_no,r.DATAORDER "
				+"FROM T_OUTPATIENT_RECIPEDETAIL_now r " 
				+"WHERE r.CLINIC_CODE ='"+clinicCode+"'"
				+"AND r.PATIENT_NO = '"+patientNo+"' "
				+"AND (r.CLASS_CODE NOT IN ('402880b751f104d10151f192e0970005',"
						+"'402880b751f104d10151f192b0630004')) "
						+"order by r.comb_no,r.DATAORDER";
		List<OutpatientVo> voList =  namedParameterJdbcTemplate.query(sql,new RowMapper<OutpatientVo>() {
			@Override
			public OutpatientVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OutpatientVo vo = new OutpatientVo();
				vo.setName(rs.getString("name"));
				vo.setSpces(rs.getString("specs"));
				vo.setDose(rs.getString("dose"));
				vo.setUsage(rs.getString("usage"));
				vo.setFreq(rs.getString("freq"));
				vo.setQty(rs.getString("qty"));
				vo.setRema(rs.getString("rema"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				return vo;
		}});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<OutpatientVo>(); 
	}
	/**  
	 * 
	 * 打印检查单患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月8日 下午5:21:38 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月8日 下午5:21:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<IreportPatientVo> getIreportPatientVo(String patientNo,String clinicCode) {
		String sql="select p.patient_name as name,"
				+"decode(P.Patient_Sex,1,'男',2,'女',3,'未知') as sex,"
				+"p.patient_age ||p.patient_ageunit as  age,"
				+"o.ITEM_NAME as itemName,"
				+"i.dept_name as dept,"
				+"i.MEDICALRECORDID as medicalrecordid,"
				+"m.HISTORYSPECIL as historyspecil,"
				+"m.DIAGNOSE1 as diagnse,"
				+"m.ADVICE as advice,"
				+"m.ADDRESS as address,"
				+"o.EXEC_DPCD_NAME as dd,"
				+"(select sum(nvl(t.PAY_COST,0)) from t_outpatient_recipedetail_now t where t.RECIPE_NO = o.RECIPE_NO) ||'元' as pay,"
				+"o.comb_no,o.DATAORDER "
				+"from t_patient p "
				+"inner join t_register_main_now i on i.card_no = p.card_no "
				+"inner join t_outpatient_recipedetail_now o on o.PATIENT_NO ='"+patientNo+"' "
				+"inner join t_outpatient_medicalrecord m on m.PATIENT_NO = i.MEDICALRECORDID "
				+"where  i.clinic_code = '"+clinicCode+"' and m.clinic_code ='"+clinicCode+"' and o.clinic_code = '"+clinicCode+"' and o.class_code = (select code_encode from t_business_dictionary where code_name = '检查') "
				+"order by o.comb_no,o.DATAORDER";
		Map<String,Object> paramMap = new HashMap<String, Object>();
		List<IreportPatientVo> voList =  namedParameterJdbcTemplate.query(sql,new RowMapper<IreportPatientVo>() {
			@Override
			public IreportPatientVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				IreportPatientVo vo = new IreportPatientVo();
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setItemName(rs.getString("itemName"));
				vo.setDept(rs.getString("dept"));
				vo.setMedicalrecordid(rs.getString("medicalrecordid"));
				vo.setHistoryspecil(rs.getString("historyspecil"));
				vo.setDiagnse(rs.getString("diagnse"));
				vo.setAdvice(rs.getString("advice"));
				vo.setAddress(rs.getString("address"));
				vo.setDd(rs.getString("dd"));
				vo.setPay(rs.getString("pay"));
				return vo;
		}});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<IreportPatientVo>();
	}
	
	/**  
	 *  
	 * 获得住院患者信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31    
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, Object> getRows(String page, String rows,
			String startTime, String endTime, String type, String para,
			String vague, List<String> tnL) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(tnL==null||tnL.size()<0){
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<InpatientStatVo>());
			return retMap;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				buffer.append("UNION ALL ");
			}
			buffer.append("SELECT DISTINCT ");
			buffer.append("r.INPATIENT_NO AS inpatientNo, ");//住院号
			buffer.append("'").append("T_INPATIENT_INFO_NOW".equals(tnL.get(i))?"T_INPATIENT_ORDER_NOW":"T_INPATIENT_ORDER").append("' tab, ");//详情信息需要查询的表T_INPATIENT_ORDER_NOW
			buffer.append("r.PATIENT_NAME AS name, ");//患者姓名
			buffer.append("r.REPORT_SEX sex, ");//性别
			buffer.append("r.REPORT_BIRTHDAY age, ");//年龄
			buffer.append("r.IDCARD_NO recordNo ");//病历号
			buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" r ");
			buffer.append("WHERE r.STOP_FLG = :STOP_FLG ");
			buffer.append("AND r.DEL_FLG = :DEL_FLG ");
			buffer.append("AND r.IN_DATE>=TO_DATE(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND r.IN_DATE<=TO_DATE(:endTime, 'yyyy-mm-dd hh24:mi:ss') "); 
			if("0".equals(vague)){//精确查询
				if(StringUtils.isNotBlank(para)){
					//类型0全部1病历卡号2姓名3住院号
					if("0".equals(type)){
						buffer.append("AND (r")
						.append(".IDCARD_NO = :para OR r")
						.append(".PATIENT_NAME = :para OR r")
						.append(".INPATIENT_NO = :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND r").append(".IDCARD_NO = :para ");
					}else if("2".equals(type)){
						buffer.append("AND r").append(".PATIENT_NAME = :para ");
					}else if("3".equals(type)){
						buffer.append("AND r").append(".INPATIENT_NO = :para ");
					}
				}
			}else{//模糊查询
				if(StringUtils.isNotBlank(para)){
					//类型0全部1病历卡号2姓名3住院号
					if("0".equals(type)){
						buffer.append("AND (r")
						.append(".IDCARD_NO LIKE :para OR r")
						.append(".PATIENT_NAME LIKE :para OR r")
						.append(".INPATIENT_NO LIKE :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND r").append(".IDCARD_NO LIKE :para ");
					}else if("2".equals(type)){
						buffer.append("AND r").append(".PATIENT_NAME LIKE :para ");
					}else if("3".equals(type)){
						buffer.append("AND r").append(".INPATIENT_NO LIKE :para ");
					}
				}
			}
		}
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("STOP_FLG", 0);
		paraMap.put("DEL_FLG", 0);
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		if(StringUtils.isNotBlank(para)){
			if("0".equals(vague)){
				paraMap.put("para", para);
			}else{
				paraMap.put("para", "%"+para+"%");
			}
		}
		
		//查询总条数
		StringBuffer bufferTotal = new StringBuffer(buffer.toString());
		bufferTotal.insert(0, "SELECT COUNT(1) FROM (" );
		bufferTotal.append(" )");
		int total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
				
		//查询对象的sql
		StringBuffer bufferRows = new StringBuffer(buffer.toString());
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :end ) WHERE rn > :start ");
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("end", start * count);
		List<InpatientStatVo> voList =  namedParameterJdbcTemplate.query(bufferRows.toString(),paraMap,new RowMapper<InpatientStatVo>() {
			@Override
			public InpatientStatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientStatVo vo = new InpatientStatVo();
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setTab(rs.getString("tab"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setRecordNo(rs.getString("recordNo"));
				return vo;
		}});
		retMap.put("total", total);
		retMap.put("rows", voList==null?new ArrayList<InpatientStatVo>():voList);
		return retMap;
	}
	
	/**  
	 *  
	 * 根据住院号查询医嘱信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-3-31 下午02:05:31  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-3-31 下午02:05:31   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InpatientInfoVo> getInpatientInfoRows(String inpatientNo,
			String tab) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("a.TYPE_NAME typeName, ");//医嘱类型
		buffer.append("a.ITEM_NAME itemName, ");//医嘱名称
		buffer.append("a.COMB_NO combNo, ");//组
		buffer.append("a.QTY_TOT qtyTot, ");//总量
		buffer.append("a.PRICE_UNIT priceUnit, ");//单位（总量单位）
		buffer.append("a.DRUGPACKAGING_UNIT drugpackagingUnit, ");//包装单位
		buffer.append("a.DOSE_UNIT doseUnit, ");//单位（剂量单位）
		buffer.append("a.DOSE_ONCE doseOnce, ");//每次剂量
		buffer.append("a.USE_DAYS useDays, ");//付数
		buffer.append("a.FREQUENCY_NAME frequencyName, ");//频次
		buffer.append("a.USE_NAME useName, ");//用法名称
		buffer.append("a.DATE_BGN dateBgn, ");//开始时间
		buffer.append("a.DATE_END dateEnd, ");//停止时间
		buffer.append("a.MO_DATE moDate, ");//开立时间
		buffer.append("a.DOC_NAME docName, ");//开立医生
		buffer.append("a.EXEC_DPNM execDpnm, ");//执行科室
		buffer.append("a.EMC_FLAG isUrgent, ");//加急
		buffer.append("a.LAB_CODE labCode, ");//样本类型
		buffer.append("a.ITEM_NOTE itemNote, ");//检查部位
		buffer.append("a.PHARMACY_CODE pharmacyCode, ");//扣库科室
		buffer.append("a.MO_NOTE2 moNote2, ");//备注
		buffer.append("a.REC_USERNM recUsernm, ");//录入人
		buffer.append("a.LIST_DPCD_NAME listDpcdName, ");//开立科室
		buffer.append("a.DC_USERNM dcUsernm, ");//停止人
		buffer.append("a.HYPOTEST hypotest, ");//皮试
		buffer.append("a.SORT_ID sortId ");//顺序号
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tab).append(" a ");
		buffer.append("WHERE a.STOP_FLG = 0 ");
		buffer.append("AND a.DEL_FLG = 0 ");
		buffer.append("AND a.INPATIENT_NO = :inpatientNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("inpatientNo", inpatientNo);
		List<InpatientInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<InpatientInfoVo>() {
			@Override
			public InpatientInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoVo vo = new InpatientInfoVo();
				vo.setTypeName(rs.getString("typeName"));
				vo.setItemName(rs.getString("itemName"));
				vo.setCombNo(rs.getString("combNo"));
				vo.setQtyTot(rs.getDouble("qtyTot"));
				vo.setPriceUnit(rs.getString("priceUnit"));
				vo.setDrugpackagingUnit(rs.getString("drugpackagingUnit"));
				vo.setDoseUnit(rs.getString("doseUnit"));
				vo.setDoseOnce(rs.getString("doseOnce"));
				vo.setUseDays(rs.getDouble("useDays"));
				vo.setFrequencyName(rs.getString("frequencyName"));
				vo.setUseName(rs.getString("useName"));
				vo.setDateBgn(rs.getString("dateBgn"));
				vo.setDateEnd(rs.getString("dateEnd"));
				vo.setMoDate(rs.getString("moDate"));
				vo.setDocName(rs.getString("docName"));
				vo.setExecDpnm(rs.getString("execDpnm"));
				vo.setIsUrgent(rs.getInt("isUrgent"));
				vo.setLabCode(rs.getString("labCode"));
				vo.setItemNote(rs.getString("itemNote"));
				vo.setPharmacyCode(rs.getString("pharmacyCode"));
				vo.setMoNote2(rs.getString("moNote2"));
				vo.setRecUsernm(rs.getString("recUsernm"));
				vo.setListDpcdName(rs.getString("listDpcdName"));
				vo.setDcUsernm(rs.getString("dcUsernm"));
				vo.setHypotest(rs.getInt("hypotest"));
				vo.setSortId(rs.getInt("sortId"));
				return vo;
		}});
		return voList;

	}

	@Override
	public List<LisVo> findLis(String cardNo,String page, String rows) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		buffer.append("select t.inspection_id as no,t.test_order_name as pro, ");
		buffer.append("t.patient_name as name,t.patient_sex as sex,t.age_input as age,t.patient_dept_name as dept ");
		buffer.append("from lis_inspection_sample t  ");
		buffer.append("where t.outpatient_id = :outpatientId ");
		buffer.append("and t.requisition_id is not null ");
		buffer.append("order by t.check_time ");
		buffer.append(") tab WHERE ROWNUM <= :end ) WHERE rn > :start ");
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("outpatientId", cardNo);
		paramMap.put("start", (start - 1) * count);
		paramMap.put("end", start * count);
		List<LisVo> list = namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<LisVo>(){
			@Override
			public LisVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				LisVo vo = new LisVo();
				vo.setNo(rs.getString("no"));
				vo.setPro(rs.getString("pro"));
				vo.setName(rs.getString("name"));
				String sex = rs.getString("sex");
				if("1".equals(sex)){
					sex = "男";
				}else if("2".equals(sex)){
					sex = "女";
				}
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setDept(rs.getString("dept"));
				return vo;
			}
		});
		return list;
	}

	@Override
	public Integer findLisNum(String cardNo) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(1) ");
		buffer.append("from lis_inspection_sample t  ");
		buffer.append("where t.outpatient_id = :outpatientId ");
		buffer.append("and t.requisition_id is not null ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("outpatientId", cardNo);
		return namedParameterJdbcTemplate.queryForObject(buffer.toString(), paramMap, java.lang.Integer.class);
	}

	@Override
	public List<InspectionReportList> findLisDetail(String inspectionId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT r.english_name        as code,");
		buffer.append("       r.chinese_name        as name,");
		buffer.append("       r.quantitative_result as result,");
		buffer.append("       r.test_item_unit      as unit,");
		buffer.append("       r.test_item_reference as scope,");
		buffer.append("       r.qualitative_result  as state,");
		buffer.append("       r.original_result     as num ");
		buffer.append("  FROM LIS_INSPECTION_RESULT r");
		buffer.append("		 WHERE r.INSPECTION_ID = :inspectionId");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("inspectionId", inspectionId);
		List<InspectionReportList> list = namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<InspectionReportList>(){
			@Override
			public InspectionReportList mapRow(ResultSet rs, int rowNum) throws SQLException {
				InspectionReportList vo = new InspectionReportList();
				String scope = rs.getString("scope");
				if(StringUtils.isNotBlank(scope)&&scope.contains("-")){
					vo.setLower(scope.split("-")[0]);
					vo.setUpper(scope.split("-")[0]);
				}
				vo.setCode(rs.getString("code"));
				vo.setName(rs.getString("name"));
				vo.setResult(rs.getString("result"));
				Integer state = null;
				if("l".equals(rs.getString("state"))){
					state = -1;
				}else if("h".equals(rs.getString("state"))){
					state = 1;
				}
				vo.setState(state);
				
				vo.setScope(scope);
				vo.setUnit(rs.getString("unit"));
				vo.setNum(rs.getString("num"));
				return vo;
			}
		});
		return list;
	}
}
