package cn.honry.statistics.operation.syntheticalStat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.operation.syntheticalStat.dao.SyntheticalStatDao;
import cn.honry.statistics.operation.syntheticalStat.vo.InvoiceInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.MedicalInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.PatientInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.RegisterInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.TreeInfoVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

/**  
 *  
 * @className：SyntheticalStatDAOImpl
 * @Description： 门诊综合查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("syntheticalStatDAO")
@SuppressWarnings({"all"})
public class SyntheticalStatDaoImpl extends HibernateEntityDao<RegisterInfoVo> implements SyntheticalStatDao{
	
	@Resource
	private RedisUtil redisUtil;
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**  
	 *  
	 * 查询患者信息 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> getRegisterInfo(String page, String rows,final String startTime,final String endTime,final String type,final String para,final String vague,final List<String> tnL) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		if(tnL==null||tnL.size()<0){
			retMap.put("total",0);
			retMap.put("rows",new ArrayList<RegisterInfoVo>());
			return retMap;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tnL.size(); i++) {
			if(i>0){
				buffer.append("UNION ALL ");
			}
			buffer.append("SELECT ");
			buffer.append("T_REGISTER_MAIN_NOW".equals(tnL.get(i))?"'T_OUTPATIENT_FEEDETAIL_NOW'":"'T_OUTPATIENT_FEEDETAIL'").append( " tab, ");
			buffer.append("m.MEDICALRECORDID AS recordNo, ");//病历号
			buffer.append("m.clinic_code AS registerNo, ");//门诊流水号
			buffer.append("p.patinent_id AS patientId, ");//患者Id
			buffer.append("m.patient_name AS patientName, ");//患者姓名
			buffer.append("m.patient_age||m.patient_ageunit AS patientAge, ");//年龄
			buffer.append("TO_CHAR(m.reg_date,'yyyy-mm-dd hh24:mi:ss') AS registerDate, ");//挂号日期
			buffer.append("m.PATIENT_IDENNO AS idCard, ");//身份证号码
			buffer.append("TO_CHAR(m.PATIENT_BIRTHDAY,'yyyy-mm-dd hh24:mi:ss') AS birthday, ");//出生日期
			buffer.append("m.paykind_name AS payKindcode, ");//结算类别
			buffer.append("m.pact_name AS contractunit, ");//合同单位
			buffer.append("m.card_no AS idcardNo, ");//医疗证号
			buffer.append("m.reglevl_name AS grade, ");//挂号级别
			buffer.append("m.dept_name AS regdept, ");//挂号科室
			buffer.append("m.doct_name AS regdoc, ");//挂号医生
			buffer.append("m.dept_name AS seedept, ");//看诊科室
			buffer.append("m.doct_name AS seedoc, ");//看诊医生
			buffer.append("m.invoice_no AS invoice, ");//挂号发票
			buffer.append("TO_CHAR (m.seeno) AS orderNo, ");//看诊序列
			buffer.append("DECODE(m.valid_flag,0,'退费',1,'有效',2,'无效') AS status, ");//是否有效
			buffer.append("TO_CHAR(m.cancel_date,'yyyy-mm-dd hh24:mi:ss') AS quitreason ");//退号时间
			buffer.append("FROM ").append(tnL.get(i)).append(" m ");
			buffer.append("LEFT JOIN t_patient p ON p.card_no = m.card_no ");
			buffer.append("WHERE m.TRANS_TYPE = :TRANS_TYPE ");
			buffer.append("AND m.VALID_FLAG = :VALID_FLAG ");
			buffer.append("AND m.REG_DATE>=TO_DATE(:startTime, 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND m.REG_DATE<TO_DATE(:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
			if("0".equals(vague)){//精确查询
				if(StringUtils.isNotBlank(para)){
					
					//类型0全部1挂号科室2挂号医生3挂号级别4合同单位
					if("0".equals(type)){
						buffer.append("AND (m")
						.append(".DEPT_NAME = :para OR m")
						.append(".DOCT_NAME = :para OR m")
						.append(".REGLEVL_NAME = :para OR m")
						.append(".PACT_NAME = :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND m").append(".DEPT_NAME = :para ");
					}else if("2".equals(type)){
						buffer.append("AND m").append(".DOCT_NAME = :para ");
					}else if("3".equals(type)){
						buffer.append("AND m").append(".REGLEVL_NAME = :para ");
					}else if("4".equals(type)){
						buffer.append("AND m").append(".PACT_NAME = :para ");
					}
				}
			}else{//模糊查询
				if(StringUtils.isNotBlank(para)){
					
					//类型0全部1挂号科室2挂号医生3挂号级别4合同单位
					if("0".equals(type)){
						buffer.append("AND (m")
						.append(".DEPT_NAME LIKE :para OR m")
						.append(".DOCT_NAME LIKE :para OR m")
						.append(".REGLEVL_NAME LIKE :para OR m")
						.append(".PACT_NAME LIKE :para ) ");
					}else if("1".equals(type)){
						buffer.append("AND m").append(".DEPT_NAME LIKE :para ");
					}else if("2".equals(type)){
						buffer.append("AND m").append(".DOCT_NAME LIKE :para ");
					}else if("3".equals(type)){
						buffer.append("AND m").append(".REGLEVL_NAME LIKE :para ");
					}else if("4".equals(type)){
						buffer.append("AND m").append(".PACT_NAME LIKE :para ");
					}
				}
			}
		}
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("TRANS_TYPE", 1);
		paraMap.put("VALID_FLAG", 1);
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
		String redKey = "MZZHCX"+startTime+"_"+endTime+"_"+para+"_"+type+"_"+vague;
		Integer total = (Integer) redisUtil.get(redKey);
		if(total==null){
			total = namedParameterJdbcTemplate.queryForObject(bufferTotal.toString(), paraMap, java.lang.Integer.class);
			redisUtil.set(redKey, total);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
				
		//查询对象的sql
		StringBuffer bufferRows = new StringBuffer(buffer.toString());
		bufferRows.insert(0, "SELECT * FROM( SELECT tab.*,ROWNUM rn FROM ( ");
		bufferRows.append(") tab WHERE ROWNUM <= :end ) WHERE rn > :start ");
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		paraMap.put("start", (start - 1) * count);
		paraMap.put("end", start * count);
		List<RegisterInfoVo> voList =  namedParameterJdbcTemplate.query(bufferRows.toString(),paraMap,new RowMapper<RegisterInfoVo>() {
			@Override
			public RegisterInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegisterInfoVo vo = new RegisterInfoVo();
				vo.setTab(rs.getString("tab"));
				vo.setRecordNo(rs.getString("recordNo"));
				vo.setRegisterNo(rs.getString("registerNo"));
				vo.setPatientId(rs.getString("patientId"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setPatientAge(rs.getString("patientAge"));
				vo.setRegisterDate(rs.getString("registerDate"));
				vo.setIdCard(rs.getString("idCard"));
				vo.setBirthday(rs.getString("birthday"));
				vo.setPayKindcode(rs.getString("payKindcode"));
				vo.setContractunit(rs.getString("contractunit"));
				vo.setIdcardNo(rs.getString("idcardNo"));
				vo.setGrade(rs.getString("grade"));
				vo.setRegdept(rs.getString("regdept"));
				vo.setRegdoc(rs.getString("regdoc"));
				vo.setSeedept(rs.getString("seedept"));
				vo.setSeedoc(rs.getString("seedoc"));
				vo.setInvoice(rs.getString("invoice"));
				vo.setOrderNo(rs.getString("orderNo"));
				vo.setStatus(rs.getString("status"));
				vo.setQuitreason(rs.getString("quitreason"));
				return vo;
		}});
		
		retMap.put("total", total);
		retMap.put("rows", voList==null?new ArrayList<RegisterInfoVo>():voList);
		
		return retMap;
	}

	/**  
	 *  
	 * 查询患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientInfoVo queryPatientInfo(String patientId) {
		
		if(StringUtils.isBlank(patientId)){
			return new PatientInfoVo();
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("p.PATIENT_NAME as name, ");
		buffer.append("p.PATIENT_SEX as sex, ");
		buffer.append("c.CODE_NAME as unit, ");
		buffer.append("p.PATIENT_HANDBOOK as handbook, ");
		buffer.append("cc.CODE_NAME as nationality, ");
		buffer.append("cn.CODE_NAME as nation, ");
		buffer.append("TO_CHAR(PATIENT_BIRTHDAY,'yyyy-MM-dd') as  birthday, ");
		buffer.append("p.PATIENT_AGE ||p.patient_ageunit as age, ");
		buffer.append("d.city_name as birthplace, ");
		buffer.append("co.CODE_NAME as occupation, ");
		buffer.append("tcc.CODE_NAME as certificatesType, ");
		buffer.append("p.PATIENT_CERTIFICATESNO as  certificatesNo, ");
		buffer.append("p.PATIENT_WORKUNIT as workUnit, ");
		buffer.append("p.PATIENT_WORKPHONE as workPhone, ");
		buffer.append("cm.CODE_NAME as warriage, ");
		buffer.append("p.PATIENT_NATIVEPLACE as nativeplace, ");
		buffer.append("p.PATIENT_ADDRESS as city, ");
		buffer.append("p.PATIENT_DOORNO as doorno, ");
		buffer.append("p.PATIENT_PHONE as phone, ");
		buffer.append("p.PATIENT_LINKMAN as linkMan, ");
		buffer.append("cr.CODE_NAME as linkrelation, ");
		buffer.append("p.PATIENT_LINKADDRESS as linkaddress, ");
		buffer.append("p.PATIENT_LINKDOORNO as linkdoorno, ");
		buffer.append("p.PATIENT_LINKPHONE as linkphone, ");
		buffer.append("p.PATIENT_EMAIL as email, ");
		buffer.append("p.PATIENT_MOTHER as mother ");
		buffer.append("FROM T_PATIENT p ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY c ON c.CODE_ENCODE = p.UNIT_ID AND c.CODE_TYPE = 'contractunit' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY cc ON cc.CODE_ENCODE = p.PATIENT_NATIONALITY AND cc.CODE_TYPE= 'country' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY cn ON cn.CODE_ENCODE = p.PATIENT_NATION AND cn.CODE_TYPE= 'nationality' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY co ON co.CODE_ENCODE = p.PATIENT_OCCUPATION AND co.CODE_TYPE= 'occupation' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY tcc ON tcc.CODE_ENCODE = p.PATIENT_CERTIFICATESTYPE AND tcc.CODE_TYPE= 'certificate' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY cm ON cm.CODE_ENCODE = p.PATIENT_WARRIAGE AND cm.CODE_TYPE= 'marry' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY cr ON cr.CODE_ENCODE = p.PATIENT_LINKRELATION AND cr.CODE_TYPE= 'relation' ");
		buffer.append("LEFT JOIN t_district d ON d.CITY_CODE = p.PATIENT_BIRTHPLACE ");
		buffer.append("WHERE p.PATINENT_ID = :patientId ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("patientId", patientId);
		PatientInfoVo vo = namedParameterJdbcTemplate.queryForObject(buffer.toString(), paramMap, new RowMapper<PatientInfoVo>(){
			@Override
			public PatientInfoVo mapRow(ResultSet rs, int i) throws SQLException {
				PatientInfoVo patientInfoVo = new PatientInfoVo();
				patientInfoVo.setName(rs.getString("name"));
				patientInfoVo.setSex(rs.getInt("sex"));
				patientInfoVo.setUnit(rs.getString("unit"));
				patientInfoVo.setHandbook(rs.getString("handbook"));
				patientInfoVo.setNationality(rs.getString("nationality"));
				patientInfoVo.setNation(rs.getString("nation"));
				patientInfoVo.setBirthday(rs.getString("birthday"));
				patientInfoVo.setAge(rs.getString("age"));
				patientInfoVo.setBirthplace(rs.getString("birthplace"));
				patientInfoVo.setOccupation(rs.getString("occupation"));
				patientInfoVo.setCertificatesType(rs.getString("certificatesType"));
				patientInfoVo.setCertificatesNo(rs.getString("certificatesNo"));
				patientInfoVo.setWorkUnit(rs.getString("workUnit"));
				patientInfoVo.setWorkPhone(rs.getString("workPhone"));
				patientInfoVo.setWarriage(rs.getString("warriage"));
				patientInfoVo.setNativeplace(rs.getString("nativeplace"));
				patientInfoVo.setCity(rs.getString("city"));
				patientInfoVo.setDoorno(rs.getString("doorno"));
				patientInfoVo.setPhone(rs.getString("phone"));
				patientInfoVo.setLinkMan(rs.getString("linkMan"));
				patientInfoVo.setLinkrelation(rs.getString("linkrelation"));
				patientInfoVo.setLinkaddress(rs.getString("linkaddress"));
				patientInfoVo.setLinkdoorno(rs.getString("linkdoorno"));
				patientInfoVo.setLinkphone(rs.getString("linkphone"));
				patientInfoVo.setEmail(rs.getString("email"));
				patientInfoVo.setMother(rs.getString("mother"));
				return patientInfoVo;
			}
		});
		return vo;
	}
	
	/**  
	 *  
	 * 查询患者发票信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<InvoiceInfoVo> queryInvoiceInfo(String registerNo,String tab) {
		if(StringUtils.isBlank(registerNo)){
			return new ArrayList<InvoiceInfoVo>();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("f.INVOICE_NO AS invoiceNo, ");
		buffer.append("DECODE(f.PAY_FLAG,0,'划价', 1,'收费', 3,'预收费团体体检',4,'药品预审核') AS priceCost, ");
		buffer.append("DECODE(f.CANCEL_FLAG, 0, '退费', 1, '正常', 2, '重打', 3, '注销') AS status, ");
		buffer.append("DECODE(f.COST_SOURCE, 0, '操作员', 1, '医嘱', 2, '终端', 3, '体检') AS source, ");
		buffer.append("f.ITEM_CODE AS itemCode, ");
		buffer.append("f.ITEM_NAME AS itemName, ");
		buffer.append("f.COMB_NO AS groupNo, ");
		buffer.append("f.COMB_NO AS groupName, ");
		buffer.append("f.SEQUENCE_NO AS inOrderNo, ");
		buffer.append("f.MO_ORDER AS seqNo, ");
		buffer.append("f.SPECS AS spec, ");
		buffer.append("f.QTY AS qty, ");
		buffer.append("f.UNIT_PRICE AS unitPrice, ");
		buffer.append("f.TOT_COST AS totalAmount, ");
		buffer.append("f.PAY_COST AS shoufuAmount, ");
		buffer.append("f.TOT_COST AS tallyAmount, ");
		buffer.append("f.reg_dpcdname AS billDept, ");
		buffer.append("f.doct_codename AS billdoc, ");
		buffer.append("f.oper_name AS inputPerson, ");
		buffer.append("TO_CHAR(f.OPER_DATE, 'yyyy-MM-dd HH24:mm:ss') AS inputTime, ");
		buffer.append("f.EXEC_DPNM AS exeDept, ");
		buffer.append("f.fee_cpcdname AS receiver, ");
		buffer.append("TO_CHAR(f.FEE_DATE, 'yyyy-MM-dd HH24:mm:ss') AS chargeDate, ");
		buffer.append("TO_CHAR(f.CONFIRM_DATE, 'yyyy-MM-dd HH24:mm:ss') AS conExeTime, ");
		buffer.append("f.confirm_deptname AS conExeDept, ");
		buffer.append("f.confirm_name AS conExePerson ");
		buffer.append("FROM ").append(tab).append(" f ");
		buffer.append("WHERE f.STOP_FLG = 0 ");
		buffer.append("AND f.DEL_FLG = 0 ");
		buffer.append("AND f.INVOICE_NO IS NOT NULL ");
		buffer.append("AND f.CLINIC_CODE = :registerNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("registerNo", registerNo);
		List<InvoiceInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<InvoiceInfoVo>() {
			@Override
			public InvoiceInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InvoiceInfoVo vo = new InvoiceInfoVo();
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				vo.setPriceCost(rs.getString("priceCost"));
				vo.setStatus(rs.getString("status"));
				vo.setSource(rs.getString("source"));
				vo.setItemCode(rs.getString("itemCode"));
				vo.setItemName(rs.getString("itemName"));
				vo.setGroupNo(rs.getString("groupNo"));
				vo.setGroupName(rs.getString("groupName"));
				vo.setInOrderNo(rs.getInt(("inOrderNo")));
				vo.setSeqNo(rs.getString("seqNo"));
				vo.setSpec(rs.getString("spec"));
				vo.setQty(rs.getInt("qty"));
				vo.setUnitPrice(rs.getDouble("unitPrice"));
				vo.setTotalAmount(rs.getDouble("totalAmount"));
				vo.setShoufuAmount(rs.getDouble("shoufuAmount"));
				vo.setTallyAmount(rs.getDouble("tallyAmount"));
				vo.setBillDept(rs.getString("billDept"));
				vo.setBilldoc(rs.getString("billdoc"));
				vo.setInputPerson(rs.getString("inputPerson"));
				vo.setInputTime(rs.getString("inputTime"));
				vo.setExeDept(rs.getString("exeDept"));
				vo.setReceiver(rs.getString("receiver"));
				vo.setChargeDate(rs.getString("chargeDate"));
				vo.setConExeTime(rs.getString("conExeTime"));
				vo.setConExeDept(rs.getString("conExeDept"));
				vo.setConExePerson(rs.getString("conExePerson"));
				return vo;
		}});
		
		return voList==null?new ArrayList<InvoiceInfoVo>():voList;
	}

	/**  
	 *  
	 * 查询患者历史医嘱树
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeInfoVo> queryMedicalTree(String recordNo) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT clinicNo,dept,doc,tab FROM ( ");
		buffer.append("SELECT ");
		buffer.append("distinct r.CLINIC_CODE AS clinicNo, ");
		buffer.append("r.DOCT_DPCD_NAME AS dept, ");
		buffer.append("r.DOCT_NAME AS doc, ");
		buffer.append("'T_OUTPATIENT_RECIPEDETAIL' tab ");
		buffer.append("FROM T_OUTPATIENT_RECIPEDETAIL r ");
		buffer.append("WHERE r.PATIENT_NO = :recordNo ");
		buffer.append("UNION ALL ");
		buffer.append("SELECT ");
		buffer.append("distinct r.CLINIC_CODE AS clinicNo, ");
		buffer.append("r.DOCT_DPCD_NAME AS dept, ");
		buffer.append("r.DOCT_NAME AS doc, ");
		buffer.append("'T_OUTPATIENT_RECIPEDETAIL_NOW' tab ");
		buffer.append("FROM T_OUTPATIENT_RECIPEDETAIL_NOW r ");
		buffer.append("WHERE r.PATIENT_NO = :recordNo ");
		buffer.append(") ORDER BY clinicNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("recordNo", recordNo);
		List<TreeInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<TreeInfoVo>() {
			@Override
			public TreeInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TreeInfoVo vo = new TreeInfoVo();
				vo.setClinicNo(rs.getString("clinicNo"));
				vo.setDept(rs.getString("dept"));
				vo.setDoc(rs.getString("doc"));
				vo.setTab(rs.getString("tab"));
				return vo;
		}});
		
		return voList==null?new ArrayList<TreeInfoVo>():voList;
	}

	/**  
	 *  
	 * 查询患者历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<MedicalInfoVo> queryMedicalInfo(String registerNo,String tab) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("s.CODE_NAME AS sysType, ");
		buffer.append("r.ITEM_NAME AS itemName, ");
		buffer.append("f.FREQUENCY_NAME AS freName, ");
		buffer.append("u.CODE_NAME AS usage, ");
		buffer.append("r.QTY AS qty, ");
		buffer.append("d.CODE_NAME AS unit, ");
		buffer.append("TO_CHAR(r.OPER_DATE,'yyyy-MM-dd HH:mm:dd') AS openDate, ");
		buffer.append("TO_CHAR(r.CANCEL_DATE,'yyyy-MM-dd HH:mm:dd') AS cancelDate ");
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tab).append(" r  ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY d ON d.CODE_ENCODE = r.ITEM_UNIT  AND d.CODE_TYPE = 'drugPackagingunit'");
		buffer.append("LEFT JOIN T_BUSINESS_FREQUENCY f ON f.FREQUENCY_ENCODE = r.FREQUENCY_CODE ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY u ON u.CODE_ENCODE = r.USAGE_CODE AND u.CODE_TYPE = 'useage' ");
		buffer.append("LEFT JOIN T_BUSINESS_DICTIONARY s ON s.CODE_ENCODE = r.CLASS_CODE AND s.CODE_TYPE = 'systemType'  ");
		buffer.append("WHERE r.CLINIC_CODE = :registerNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("registerNo", registerNo);
		List<MedicalInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<MedicalInfoVo>() {
			@Override
			public MedicalInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MedicalInfoVo vo = new MedicalInfoVo();
				vo.setSysType(rs.getString("sysType"));
				vo.setItemName(rs.getString("itemName"));
				vo.setFreName(rs.getString("freName"));
				vo.setUsage(rs.getString("usage"));
				vo.setQty(rs.getInt("qty"));
				vo.setUnit(rs.getString("unit"));
				vo.setOpenDate(rs.getString("openDate"));
				vo.setCancelDate(rs.getString("cancelDate"));
				return vo;
		}});
		
		return voList==null?new ArrayList<MedicalInfoVo>():voList;
	}

	/**  
	 *  
	 * 获取最小最大时间
	 * @Author：zhangjin
	 * @CreateDate：2016-12-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public StatVo findMaxMin() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime ,MIN(mn.REG_DATE) AS sTime FROM T_REGISTER_MAIN_NOW mn";
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
	 * 获取最小最大时间（收费明细）
	 * @Author：zhangjin
	 * @CreateDate：2016-12-2 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public StatVo findMaxMinfee() {
		final String sql = "SELECT MAX(mn.createtime) AS eTime ,MIN(mn.createtime) AS sTime FROM T_OUTPATIENT_FEEDETAIL_NOW mn";
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

}
