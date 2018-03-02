package cn.honry.inpatient.inprePay.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.inpatient.inprePay.vo.AcceptingGold;
import cn.honry.inpatient.inprePay.vo.PatientVo;

/**  
 *  
 * @className：InprePayDAOImpl 
 * @Description： 住院预交金DAOImpl 
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
@Repository("inprePayDAO")
public class InprePayDAOImpl extends HibernateEntityDao<InpatientInPrepayNow> implements InprePayDAO{
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 *
	 * @Description：通过就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param idcardNo 就诊卡号
	 * @return：患者对象（PatientVo）
	 *
	 */
	@Override
	public PatientVo findPatientByIdcardNo(String idcardNo,final String inState) throws Exception {
		if(StringUtils.isBlank(idcardNo)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT P.In_State as inState, p.MEDICALRECORD_ID AS medicale,P.IDCARD_NO AS idcard,p.PATIENT_NAME name, P.Report_Sex_Name as sexName,");
		sb.append("  (SELECT e.CODE_NAME FROM T_BUSINESS_DICTIONARY e WHERE e.code_type = 'certificate' and e.CODE_encode = P.CERTIFICATES_TYPE) AS certificatesType,");
		sb.append(" p.CERTIFICATES_NO AS certificatesNo, p.REPORT_BIRTHDAY AS birthDay, P.INPATIENT_NO as inpatientNo,(SELECT c.CODE_NAME  FROM T_BUSINESS_DICTIONARY c ");
		sb.append(" WHERE c.code_type = 'nationality' and c.code_encode = p.NATION_CODE) AS nation,p.LINKMAN_TEL AS phone, p.MCARD_NO AS handBook,  ");
		sb.append(" (SELECT n.CODE_NAME  FROM T_BUSINESS_DICTIONARY n WHERE n.code_type = 'country' and n.code_encode = p.COUN_CODE) AS nationAlity FROM T_INPATIENT_INFO_NOW P ");
		sb.append("  WHERE p.IN_STATE not in ('O','N','C') AND P.IDCARD_NO = :idcardNo");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("idcardNo", idcardNo);
		List<PatientVo> patientVoList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<PatientVo>() {

			@Override
			public PatientVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PatientVo vo = new PatientVo();
				vo.setMedicale(rs.getString("medicale"));
				vo.setIdcard(rs.getString("idcard"));
				vo.setInpatientNo(rs.getString("inpatientNo"));
				vo.setName(rs.getString("name"));
				vo.setSexName(rs.getString("sexName"));
				vo.setCertificatesType(rs.getString("certificatesType"));
				vo.setCertificatesNo(rs.getString("certificatesNo"));
				vo.setBirthDay(rs.getTimestamp("birthDay"));
				vo.setNation(rs.getString("nation"));
				vo.setPhone(rs.getString("phone"));
				vo.setHandBook(rs.getString("handBook"));
				vo.setInState(rs.getString("inState"));
				vo.setNationAlity(rs.getString("nationAlity"));
				return vo;
			}
			
		});
		
		if(patientVoList!=null&&patientVoList.size()>0){
			return patientVoList.get(0);
		}
		return null;
	}

	/**
	 *
	 * @Description：通过就诊卡号查询就诊卡是否存在
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param idcardNo 就诊卡号
	 * @return：true存在；false不存在
	 *
	 */
	@Override
	public boolean checkIdcardNo(String idcardNo) throws Exception{
		String sql ="select count(1) from (select t.IDCARD_ID as id from T_PATIENT_IDCARD t where t.IDCARD_NO =:idcardNo and t.STOP_FLG = 0 and t.DEL_FLG= 0 and  t.IDCARD_STATUS=1) ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("idcardNo", idcardNo);
		int count =  namedParameterJdbcTemplate.queryForObject(sql,paraMap, java.lang.Integer.class);
		if(count>0){
			return true;
		}
		return false;
	}

	/**
	 *
	 * @Description：查询预交金信息 - 分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param：inpatientNo住院号
	 * @return 总条数
	 */
	@Override
	public int getInprePayTotal(String inpatientNo) throws Exception{
		String sql ="select count(1) from ( select t.ID as id, t.INPATIENT_NO as inpatientNo, t.prepay_cost as prepayCost,t.pay_way as payWay, t.open_accounts as openAccounts,"
				+ "t.open_bank as openBank,t.prepay_state as prepayState, t.CREATETIME as createTime "
				+ "from T_INPATIENT_INPREPAY_NOW t  where t.inpatient_no = :inpatientNo and t.STOP_FLG = 0 and t.DEL_FLG = 0 "
				+ " )  ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		int c =  namedParameterJdbcTemplate.queryForObject(sql,paraMap, java.lang.Integer.class);
		return c;
	}

	/**
	 *
	 * @Description：查询预交金信息 - 分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @param2：page当前页数
	 * @param3：rows分页数量
	 * @return：分页信息
	 * 
	 */
	@Override
	public List<InpatientInPrepayNow> getInprePayPage(String inpatientNo,String page, String rows) throws Exception{
		String sql ="select * from (select t.ID as id,t.INPATIENT_NO as inpatientNo,t.prepay_cost as prepayCost,t.pay_way as payWay, t.open_accounts as openAccounts,"
				+ "t.open_bank as openBank,t.prepay_state as prepayState, t.CREATETIME as createTime,ROWNUM as n  "
				+ "from T_INPATIENT_INPREPAY_NOW t  where t.inpatient_no = :inpatientNo and t.STOP_FLG = 0 and t.DEL_FLG = 0 "
				+ " and ROWNUM  <= (:page) * :row  )  "
				+ " where n > (:page -1) * :row ORDER BY createTime DESC ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", page);
		paraMap.put("row", rows);
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientInPrepayNow> inpatientInPrepayNowList =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientInPrepayNow>() {

			@Override
			public InpatientInPrepayNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInPrepayNow  inpatientInPrepayNow = new InpatientInPrepayNow();
				inpatientInPrepayNow.setId(rs.getString("id"));
				inpatientInPrepayNow.setInpatientNo(rs.getString("inpatientNo"));
				inpatientInPrepayNow.setPrepayCost(rs.getObject("prepayCost")==null?null:rs.getDouble("prepayCost"));
				inpatientInPrepayNow.setPayWay(rs.getString("payWay"));
				inpatientInPrepayNow.setOpenAccounts(rs.getString("openAccounts"));
				inpatientInPrepayNow.setOpenBank(rs.getString("openBank"));
				inpatientInPrepayNow.setPrepayState(rs.getObject("prepayState")==null?null:rs.getInt("prepayState"));
				inpatientInPrepayNow.setCreateTime(rs.getTimestamp("createTime"));
				return inpatientInPrepayNow;
			}
			
		});
		return inpatientInPrepayNowList;
	}

	/**
	 *
	 * @Description：查询预交金信息 - 分页查询 - 获得查询语句
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @return：分页信息
	 * 
	 */
	private String jointHql(String inpatientNo) throws Exception{
		String hql = "FROM InpatientInPrepayNow i WHERE i.inpatientNo = '"+inpatientNo+"' AND i.stop_flg = 0 AND i.del_flg = 0 ORDER BY i.createTime DESC";
		return hql;
	}

	/**
	 *
	 * @Description：通过病历号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午3:53:37 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<PatientVo> findPatientByInpatientNo(String medicalrecordId) throws Exception{
		if(StringUtils.isNotBlank(medicalrecordId)){
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT p.MEDICALRECORD_ID AS medicale, P.IDCARD_NO AS idcard,p.PATIENT_NAME name, P.Report_Sex_Name as sexName,");
			sb.append(" (SELECT e.CODE_NAME FROM T_BUSINESS_DICTIONARY e WHERE e.code_type = 'certificate'  and e.CODE_encode = P.CERTIFICATES_TYPE) AS certificatesType,");
			sb.append("   p.CERTIFICATES_NO AS certificatesNo, p.REPORT_BIRTHDAY AS birthDay, P.INPATIENT_NO as inpatientNo,");
			sb.append("(SELECT c.CODE_NAME  FROM T_BUSINESS_DICTIONARY c  WHERE c.code_type = 'nationality' and c.code_encode = p.NATION_CODE) AS nation,");
			sb.append("   p.LINKMAN_TEL AS phone, p.MCARD_NO AS handBook,(SELECT n.CODE_NAME FROM T_BUSINESS_DICTIONARY n  WHERE n.code_type = 'country' and n.code_encode = p.COUN_CODE) AS nationAlity ");
			sb.append("  FROM T_INPATIENT_INFO_NOW P WHERE p.IN_STATE not in ('O','N','C') AND p.MEDICALRECORD_ID = :medicalrecordId");
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("medicalrecordId", medicalrecordId);
			List<PatientVo> patientVoList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<PatientVo>() {

				@Override
				public PatientVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					PatientVo vo = new PatientVo();
					vo.setMedicale(rs.getString("medicale"));
					vo.setIdcard(rs.getString("idcard"));
					vo.setInpatientNo(rs.getString("inpatientNo"));
					vo.setName(rs.getString("name"));
					vo.setSexName(rs.getString("sexName"));
					vo.setCertificatesType(rs.getString("certificatesType"));
					vo.setCertificatesNo(rs.getString("certificatesNo"));
					vo.setBirthDay(rs.getDate("birthDay"));
					vo.setNation(rs.getString("nation"));
					vo.setPhone(rs.getString("phone"));
					vo.setHandBook(rs.getString("handBook"));
					vo.setNationAlity(rs.getString("nationAlity"));
					return vo;
				}
				
			});
			if(patientVoList!=null&&patientVoList.size()>0){
				return patientVoList;
			}
		}
		return new ArrayList<PatientVo>();
	}

	@Override
	public InpatientInfoNow findPatientByInpNo(String inpatientNo) throws Exception{
		String hql="from InpatientInfoNow i where i.inpatientNo=? ";
		List<InpatientInfoNow> list=super.find(hql, inpatientNo);
		if(list.size()>0&&list!=null){
			return list.get(0);
		}
		return new InpatientInfoNow();
	}
	/**
	 * 通过患者的病历号和住院状态查询患者的住院主表中的记录
	 * @param inpatientNo 患者的住院流水号
	 * @author tuchuanjiang
	 * @return
	 */
	@Override
	public InpatientInfoNow queryInpatientInfoByInNo(String medicalrecordId) throws Exception{
		String hql=" from InpatientInfoNow where del_flg=0 and stop_flg=0 and medicalrecordId=? and inState in ('I','R')";
		List<InpatientInfoNow> inpaientInfoList=super.find(hql, medicalrecordId);
		if(inpaientInfoList!=null&&inpaientInfoList.size()>0){
			return inpaientInfoList.get(0);
		}
		return null;
	}

	@Override
	public InpatientInfoNow isStopAcountNow(String inpatientNo) throws Exception{
		String sql ="select t.STOP_ACOUNT as stopAcount from T_INPATIENT_INFO_NOW t where t.INPATIENT_NO =:inpatientNo";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<InpatientInfoNow> list =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<InpatientInfoNow>() {

			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInfoNow inpatientInfoNow = new InpatientInfoNow();
				inpatientInfoNow.setStopAcount(rs.getObject("stopAcount")==null?null:rs.getInt("stopAcount"));
				return inpatientInfoNow;
			}
		});

		if(list.size()>0&&list!=null){
			return list.get(0);
		}
		return new InpatientInfoNow();
	
	}

	@Override
	public void updateInpatientInPrepayNow(InpatientInPrepayNow inp) throws Exception{
		String sql ="update T_INPATIENT_INPREPAY_NOW set prepay_state =? where ID=?";
		Object args[] = new Object[]{1,inp.getId()};  
		jdbcTemplate.update(sql,args);  
	}

	@Override
	public List<AcceptingGold> iReportzyyjj(String id) throws Exception{
		StringBuffer buffer=new StringBuffer(610);
		buffer.append("SELECT （TO_CHAR(T.CREATETIME, 'yyyy')||'年'||to_char(T.CREATETIME, 'mm')||'月'||to_char(T.CREATETIME, 'dd')||'日'） AS createTime,T.NAME AS name,T.DEPT_NAME AS deptName,T.RECEIPT_NO AS receiptNo ,TO_CHAR(T.INPATIENT_NO) AS inpatientNo,TO_CHAR(T.PREPAY_COST) AS prepayCost,T.CREATEUSER AS createUser,");
		buffer.append("TO_CHAR((SELECT I.CODE_NAME FROM T_BUSINESS_DICTIONARY I WHERE I.CODE_TYPE='PAYWAY' AND I.CODE_ENCODE=T.PAY_WAY)) AS payWay,");
		buffer.append("(SELECT K.UNIT_NAME FROM T_BUSINESS_CONTRACTUNIT K WHERE K.UNIT_CODE=H.PACT_CODE) AS pactCode,");
		buffer.append("MONEY2CHINESE(T.PREPAY_COST) AS costd FROM T_INPATIENT_INPREPAY_NOW T JOIN T_INPATIENT_INFO_NOW H ON H.INPATIENT_NO=T.INPATIENT_NO");
		buffer.append(" WHERE  T.PREPAY_COST>0 AND  T.ID='"+id+"'");
		List<AcceptingGold> list=super.getSession().createSQLQuery(buffer.toString()).addScalar("createTime")
				.addScalar("name").addScalar("deptName").addScalar("receiptNo").addScalar("inpatientNo")
				.addScalar("prepayCost").addScalar("createUser").addScalar("payWay").addScalar("pactCode")
				.addScalar("costd").setResultTransformer(Transformers.aliasToBean(AcceptingGold.class)).list();
		if(null!=list&&list.size()>0){
			return list;
		}
		return new ArrayList<AcceptingGold>();
	}

	@Override
	public String getDeptArea(String deptCode) throws Exception{
		String sql="select t.dept_area_code as hospitalName from t_department t where t.dept_code='"+deptCode+"'";
		List<AcceptingGold> list=super.getSession().createSQLQuery(sql)
				.addScalar("hospitalName").setResultTransformer(Transformers.aliasToBean(AcceptingGold.class)).list();
				if(list.size()>0){
					return list.get(0).getHospitalName();
				}
		return "";
	}
	
}
