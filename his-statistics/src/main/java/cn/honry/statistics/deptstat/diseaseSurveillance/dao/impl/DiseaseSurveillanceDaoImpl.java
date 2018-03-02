package cn.honry.statistics.deptstat.diseaseSurveillance.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.deptstat.diseaseSurveillance.dao.DiseaseSurveillanceDao;
import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;

@Repository("diseaseSurveillanceDao")
@SuppressWarnings({ "all" })
public class DiseaseSurveillanceDaoImpl extends HibernateEntityDao<DiseaseSurveillanceVo> implements DiseaseSurveillanceDao{
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
	/**  
	 * 
	 * 重点疾病监测汇总
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<DiseaseSurveillanceVo> queryDiseaseSurveillance( ) {
		StringBuffer sql=new StringBuffer();
		sql.append("select d.code_name as diseasesName, (select count(1)"
				+ " from t_inpatient_info kk "
				+ " join t_business_diagnose bb on kk.inpatient_no = bb.inpatient_no "
				+ " where kk.inpatient_no = i.inpatient_no) as count"
				+ " from t_inpatient_info i "
				+ " join t_business_diagnose b on i.inpatient_no = b.inpatient_no "
				+ " join t_business_icd10 t on t.icd_diagnosticcode = b.icd_code "
				+ " join t_business_dictionary d on d.code_encode = t.icd_diseasetype "
				+ " where i.in_state = 'O' and d.code_type = 'diseasetype' "
				+ " and to_char(i.out_date, 'yyyy-MM-dd HH:mi:ss') > '' "
				+ " and to_char(i.out_date, 'yyyy-MM-dd HH:mi:ss') < '' "
				+ " group by d.code_encode, d.code_name,i.inpatient_no");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<DiseaseSurveillanceVo> DiseaseSurveillanceVoList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<DiseaseSurveillanceVo>() {
			@Override
			public DiseaseSurveillanceVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DiseaseSurveillanceVo vo = new DiseaseSurveillanceVo();
//				vo.setValue(rs.getString("value"));
//				vo.setName(rs.getString("name"));
				return vo;
			}
		});
		if(DiseaseSurveillanceVoList!=null&&DiseaseSurveillanceVoList.size()>0){
			return DiseaseSurveillanceVoList;
		}
		return null;
	}
}
