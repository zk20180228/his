package cn.honry.statistics.deptstat.deathPatient.dao.impl;

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
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.criticallyIllPatient.vo.CriticallyIllPatientVo;
import cn.honry.statistics.deptstat.deathPatient.dao.DeathPatientDao;
import cn.honry.statistics.deptstat.deathPatient.vo.DeathPatientVo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("deathPatientDao")
@SuppressWarnings({ "all" })
public class DeathPatientDaoImpl extends HibernateEntityDao<DeathPatientVo> implements DeathPatientDao{
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
	 * 患者死亡信息统计
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
	public List<DeathPatientVo> queryDeathPatient(String startTime,String endTime,String deptCode,String sex,String menuAlias,String page,String rows) {
		StringBuffer sql=new StringBuffer();
		String userAcc = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		sql.append("SELECT  ROWNUM AS n, deptCode,deptName,patientNo,cardNo,patientName,sex,birthday,age,ageunit,inDate,inCircs,diagName FROM( ");
		sql.append("select t.dept_code as deptCode,t.dept_name as deptName, t.PATIENT_NO as patientNo,t.CARD_NO as cardNo, "
				+ "t.name as patientName,t.sex_code as sex ,t.birthday as birthday, "
				+ "t.age as age,t.age_unit as ageunit,t.in_date as inDate,t.in_circs as inCircs,t.inhos_diagicdname as diagName "
				+ "from t_emr_base t " 
				+ "where t.dead_date <> to_date('0001/1/1','YYYY-mm-DD') and "
				+ "t.birthday>=to_date('"+startTime+"','yyyy') and t.birthday<=to_date('"+endTime+"','yyyy') ");
		if(StringUtils.isNotBlank(deptCode)){
			sql.append(" and t.dept_code in "+deptCode+"");
		}else{
			sql.append(" and t.dept_code in ("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,userAcc)+")");
		}
		if(StringUtils.isNotBlank(sex)){
			sql.append(" and t.sex_code = '"+sex+"'"); 
		}
		sql.append(" ) order by inDate desc "); 
//		sql.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
//		int start = Integer.parseInt(page == null ? "1" : page);
//		int count = Integer.parseInt(rows == null ? "20" : rows);
//		paraMap.put("page", start);
//		paraMap.put("rows", count);
		List<DeathPatientVo> DeathPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DeathPatientVo>() {
			@Override
			public DeathPatientVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DeathPatientVo vo = new DeathPatientVo();
				vo.setDeptCode(rs.getString("deptCode")); 
				vo.setDeptName(rs.getString("deptName")); 
				vo.setPatientNo(rs.getString("patientNo")); 
				vo.setCardNo(rs.getString("cardNo"));
				vo.setPatientName(rs.getString("patientName")); 
				vo.setSex(rs.getString("sex")); 
				vo.setBirthday(rs.getDate("birthday")); 
				vo.setAge(rs.getInt("age")); 
				vo.setAgeunit(rs.getString("ageunit")); 
				vo.setInDate(rs.getDate("inDate")); 
				vo.setInCircs(rs.getString("inCircs")); 
				vo.setDiagName(rs.getString("diagName")); 
				return vo;
			}
		});
		if(DeathPatientVoList!=null&&DeathPatientVoList.size()>0){
			return DeathPatientVoList;
		}
		return new ArrayList<DeathPatientVo>();
	
}

	@Override
	public int getTotalDeathPatient(String startTime, String endTime,String deptCode,String sex, String menuAlias) {
		String userAcc = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT  count(1) total FROM( ");
		sql.append("select t.dept_code as deptCode,t.dept_name as deptName, t.PATIENT_NO as patientNo,t.CARD_NO as cardNo, "
				+ "t.name as patientName,t.sex_code as sex ,t.birthday as birthday, "
				+ "t.age as age,t.in_date as inDate,t.in_circs as inCircs,t.inhos_diagicdname as diagName "
				+ "from t_emr_base t " 
				+ "where t.dead_date <> to_date('0001/1/1','YYYY-mm-DD') and "
				+ "t.birthday>=to_date('"+startTime+"','yyyy') and t.birthday<=to_date('"+endTime+"','yyyy') "); 
		if(StringUtils.isNotBlank(deptCode)){
			sql.append(" and t.dept_code in "+deptCode+"");
		}else{
			sql.append(" and t.dept_code in ("+dataJurisInInterDAO.getJurisDeptSql(menuAlias,userAcc)+")");
		}
		if(StringUtils.isNotBlank(sex)){
			sql.append(" and t.sex_code = '"+sex+"'"); 
		}
		sql.append(" )");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<DeathPatientVo> CriticallyIllPatientVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DeathPatientVo>() {
			@Override
			public DeathPatientVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DeathPatientVo vo = new DeathPatientVo();
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
