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
import cn.honry.statistics.bi.bistac.temporary.dao.HistoryRecordsDAO;
import cn.honry.statistics.bi.bistac.temporary.vo.HistoryRecordsInfoVo;
import cn.honry.utils.HisParameters;

/**  
 *  
 * @className：HistoryRecordsDAOImpl
 * @Description： 历史病历
 * @Author：gaotiantian
 * @CreateDate：2017-4-5 下午2:51:16 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-5 下午2:51:16
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@Repository("HistoryRecordsDAO")
public class HistoryRecordsDAOImpl extends HibernateEntityDao<HistoryRecordsInfoVo> implements HistoryRecordsDAO {
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**  
	 *  
	 * 根据门诊号或住院号查询病历信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-4-5 下午2:51:16  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-4-5 下午2:51:16   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<HistoryRecordsInfoVo> getHistoryRecordsInfo(String clinicNo) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append("m.MAINDESC mainDesc, ");//主诉
		buffer.append("m.ALLERGICHISTORY allergicHistory, ");//过敏史
		buffer.append("m.HEREDITYHIS heredityHis, ");//家族遗传史
		buffer.append("m.PRESENTILLNESS presentIllness, ");//现病史
		buffer.append("m.TEMPERATURE temperature, ");//体温
		buffer.append("m.PULSE pulse, ");//脉搏
		buffer.append("m.BREATHING breathing, ");//呼吸
		buffer.append("m.BLOOD_PRESSURE bloodPressure, ");//血压
		buffer.append("m.PHYSICAL_EXAMINATION physicalExamination, ");//体格检查
		buffer.append("m.CHECKRESULT checkResult, ");//校验检查
		buffer.append("m.DIAGNOSE1 diagnose1, ");//诊断检查
		buffer.append("m.ADVICE advice, ");//医嘱建议
		buffer.append("m.HISTORYSPECIL historySpecil ");//病史和特征
		buffer.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append("T_OUTPATIENT_MEDICALRECORD").append(" m ");
		buffer.append("WHERE m.STOP_FLG = 0 ");
		buffer.append("AND m.DEL_FLG = 0 ");
		buffer.append("AND m.CLINIC_CODE = :clinicNo ");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clinicNo", clinicNo);
		List<HistoryRecordsInfoVo> voList =  namedParameterJdbcTemplate.query(buffer.toString(),paramMap,new RowMapper<HistoryRecordsInfoVo>() {
			@Override
			public HistoryRecordsInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistoryRecordsInfoVo vo = new HistoryRecordsInfoVo();
				vo.setMainDesc(rs.getString("mainDesc"));
				vo.setAllergicHistory(rs.getString("allergicHistory"));
				vo.setHeredityHis(rs.getString("heredityHis"));
				vo.setPresentIllness(rs.getString("presentIllness"));
				vo.setTemperature(rs.getDouble("temperature"));
				vo.setPulse(rs.getDouble("pulse"));
				vo.setBreathing(rs.getDouble("breathing"));
				vo.setBloodPressure(rs.getDouble("bloodPressure"));
				vo.setPhysicalExamination(rs.getString("physicalExamination"));
				vo.setCheckResult(rs.getString("checkResult"));
				vo.setDiagnose1(rs.getString("diagnose1"));
				vo.setAdvice(rs.getString("advice"));
				vo.setHistorySpecil(rs.getString("historySpecil"));
				return vo;
		}});
		return voList;
	}

}

