package cn.honry.statistics.bi.bistac.temporary.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.bistac.temporary.dao.CheckListReportDAO;
import cn.honry.statistics.bi.bistac.temporary.vo.CheckListReportVo;
import cn.honry.utils.HisParameters;

/**  
 *  
 * @className：CheckListReportDAOImpl
 * @Description： 门诊就医-检验单
 * @Author：gaotiantian
 * @CreateDate：2017-4-10 下午2:09:12 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-10 下午2:09:12
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("CheckListReportDAO")
public class CheckListReportDAOImpl extends HibernateEntityDao<CheckListReportVo> implements CheckListReportDAO {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<CheckListReportVo> getCheckListReport(String clinicCode,String medicalrecordId) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("r.APPLY_NO as applyNo, ");//
		buffer.append("to_char(sysdate,'yyyy-mm-dd hh:mm:ss') as nowtime, ");//当前时间
		buffer.append("p.patient_name as name, ");//姓名
		buffer.append("decode(p.patient_sex,1,'男',2,'女',3,'未知') as sex, ");//性别
		buffer.append("p.patient_age||p.PATIENT_AGEUNIT as age, ");//年龄
		buffer.append("i.dept_name as dept, ");//科别
		buffer.append("p.MEDICALRECORD_ID as medicalrecordid, ");//病历号
		buffer.append("m.HISTORYSPECIL as histroySpecil, ");//病史及特征
		buffer.append("m.DIAGNOSE1||','|| m.DIAGNOSE2 as diagnose, ");//临床诊断
		buffer.append("m.ADVICE as advice, ");//注意事项
		buffer.append("m.ADDRESS as address, ");//地址
		buffer.append("r.EXEC_DPCD_NAME as eDept, ");//执行科室
		buffer.append("(select sum(nvl(t.PAY_COST,0)) from t_outpatient_recipedetail_now t where t.RECIPE_NO = r.RECIPE_NO)||'元' as pay, ");
		buffer.append("r.ITEM_NAME as itemName ");
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("t_patient").append(" p ");
		buffer.append("inner join ").append(HisParameters.HISPARSCHEMAHISUSER).append("t_register_main_now").append(" i ").append("on i.card_no = p.card_no ");
		buffer.append("inner join ").append(HisParameters.HISPARSCHEMAHISUSER).append("t_outpatient_medicalrecord").append(" m ").append(" on m.PATIENT_NO = i.MEDICALRECORDID ");
		buffer.append("left join ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_OUTPATIENT_RECIPEDETAIL_NOW").append(" r ").append(" on i.CLINIC_CODE = r.CLINIC_CODE ");
		buffer.append("WHERE i.clinic_code = :clinicCode ");
		buffer.append("and m.CLINIC_CODE = :clinicCode ");
		buffer.append("and r.clinic_code = :clinicCode ");
		buffer.append("and r.PATIENT_NO = :medicalrecordId ");
		buffer.append("and r.class_code = (select code_encode from t_business_dictionary where code_name = '检验') ");
		buffer.append("AND r.DEL_FLG=0 ");
		buffer.append("AND r.STATUS=0 ");
		buffer.append("order by r.comb_no,r.DATAORDER ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clinicCode", clinicCode);
		paramMap.put("medicalrecordId", medicalrecordId);
		List<CheckListReportVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<CheckListReportVo>() {
			@Override
			public CheckListReportVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CheckListReportVo vo = new CheckListReportVo();
				vo.setApplyNo(rs.getString("applyNo"));
				vo.setNowtime(rs.getString("nowtime"));
				vo.setName(rs.getString("name"));
				vo.setSex(rs.getString("sex"));
				vo.setAge(rs.getString("age"));
				vo.setDept(rs.getString("dept"));
				vo.setMedicalRecordId(rs.getString("medicalrecordid"));
				vo.setHistorySpecil(rs.getString("histroySpecil"));
				vo.setDiagnose(rs.getString("diagnose"));
				vo.setAdvice(rs.getString("advice"));
				vo.setAddress(rs.getString("address"));
				vo.seteDept(rs.getString("eDept"));
				vo.setPay(rs.getString("pay"));
				vo.setItemName(rs.getString("itemName"));
				return vo;
		}});
		return voList;
			
	}
}

