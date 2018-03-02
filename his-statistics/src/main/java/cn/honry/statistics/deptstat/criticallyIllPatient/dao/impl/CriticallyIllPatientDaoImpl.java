package cn.honry.statistics.deptstat.criticallyIllPatient.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.criticallyIllPatient.dao.CriticallyIllPatientDao;
import cn.honry.statistics.deptstat.criticallyIllPatient.vo.CriticallyIllPatientVo;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;
import cn.honry.statistics.deptstat.materialAndEquipment.vo.MaterialAndEquipmentVo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("criticallyIllPatientDao")
@SuppressWarnings({ "all" })
public class CriticallyIllPatientDaoImpl extends HibernateEntityDao<CriticallyIllPatientVo> implements CriticallyIllPatientDao{
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
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	/**  
	 * 
	 * 病危患者信息统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月14日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月14日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<CriticallyIllPatientVo> queryCriticallyIllPatient(String startTime,String endTime,String deptCode,String sex,String menuAlias,String page,String rows) {
		String userAcc = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		StringBuffer sql=new StringBuffer();
//		sql.append("SELECT * from (");
		sql.append("SELECT  ROWNUM AS n, deptCode,deptName,medicalrecordId,idcardNo,patientName,sex,birthday,age,inDate,inCircs,diagName FROM( ");
		sql.append("select t.dept_code as deptCode,t.dept_name as deptName, t.medicalrecord_id as medicalrecordId,t.idcard_no as idcardNo, "
				+ "t.patient_name as patientName,t.report_sex as sex,t.report_birthday as birthday, "
				+ "t.report_age as age,t.in_date as inDate,t.in_circs as inCircs,t.diag_name as diagName  "
				+ "from t_inpatient_info_now t "
				+ "where t.in_state='I' and (t.critical_flag<>0 or t.critical_flag is null) ");
				if(StringUtils.isNotBlank(deptCode)){
					sql.append(" and t.dept_code in "+deptCode+"");
				}else{
					sql.append(" and t.dept_code in ("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,userAcc)+")");
				}
				if(StringUtils.isNotBlank(sex)){
					sql.append(" and t.report_sex in "+sex+"");
				}
				sql.append(" and t.report_birthday>=to_date('"+startTime+"','yyyy') and t.report_birthday<=to_date('"+endTime+"','yyyy') )  order by inDate desc "); 
//		sql.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
//		int start = Integer.parseInt(page == null ? "1" : page);
//		int count = Integer.parseInt(rows == null ? "20" : rows);
//		paraMap.put("page", start);
//		paraMap.put("rows", count);
		List<CriticallyIllPatientVo> CriticallyIllPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<CriticallyIllPatientVo>() {
			@Override
			public CriticallyIllPatientVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				CriticallyIllPatientVo vo = new CriticallyIllPatientVo();
				vo.setDeptCode(rs.getString("deptCode")); 
				vo.setDeptName(rs.getString("deptName")); 
				vo.setMedicalrecordId(rs.getString("medicalrecordId")); 
				vo.setIdcardNo(rs.getString("idcardNo"));
				vo.setPatientName(rs.getString("patientName")); 
				vo.setSex(rs.getString("sex")); 
				vo.setBirthday(rs.getString("birthday")); 
				vo.setAge(rs.getString("age")); 
				vo.setInDate(rs.getString("inDate")); 
				vo.setInCircs(rs.getString("inCircs")); 
				vo.setDiagName(rs.getString("diagName")); 
				return vo;
			}
		});
		if(CriticallyIllPatientVoList!=null&&CriticallyIllPatientVoList.size()>0){
			return CriticallyIllPatientVoList;
		}
		return new ArrayList<CriticallyIllPatientVo>();
	}

	@Override
	public int getTotalCriticallyIllPatient(String startTime,String endTime,String deptCode,String sex, String menuAlias) {
		String userAcc = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1) total FROM( ");
		sql.append("select t.dept_code as deptCode,t.dept_name as deptName, t.medicalrecord_id as medicalrecordId,t.idcard_no as idcardNo, "
				+ "t.patient_name as patientName,t.report_sex as sex,t.report_birthday as birthday, "
				+ "t.report_age as age,t.in_date as inDate,t.in_circs as inCircs,t.diag_name as diagName  "
				+ "from t_inpatient_info_now t "
				+ "where t.in_state='I' and (t.critical_flag<>0 or t.critical_flag is null) ");
				if(StringUtils.isNotBlank(deptCode)){
					sql.append(" and t.dept_code in "+deptCode+"");
				}else{
					sql.append(" and t.dept_code in ("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,userAcc)+")");
				}
				if(StringUtils.isNotBlank(sex)){
					sql.append(" and t.report_sex in "+sex+"");
				}
				sql.append(" and t.report_birthday>=to_date('"+startTime+"','yyyy') and t.report_birthday<=to_date('"+endTime+"','yyyy') "); 
		sql.append(" )");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<CriticallyIllPatientVo> CriticallyIllPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<CriticallyIllPatientVo>() {
			@Override
			public CriticallyIllPatientVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				CriticallyIllPatientVo vo = new CriticallyIllPatientVo();
//				vo.setItemCode(rs.getString("itemCode")); 
				return vo;
			}
		});
		if(CriticallyIllPatientVoList!=null&&CriticallyIllPatientVoList.size()>0){
			return CriticallyIllPatientVoList.size();
		}
		return 0;
	}


}
