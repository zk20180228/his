package cn.honry.inpatient.diagnose.dao.impl;

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

import cn.honry.base.bean.model.BusinessDiagnose;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.diagnose.dao.DiagnoseDAO;
import cn.honry.inpatient.diagnose.vo.DiagnoseVo;
import cn.honry.utils.HisParameters;

@Repository("diagnoseDAO")
@SuppressWarnings({ "all" })
public class DiagnoseDAOImpl extends HibernateEntityDao<BusinessDiagnose> implements DiagnoseDAO {
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * @Description：  根据住院流水号查询住院诊断
	 * @Author：zhangjin
	 * @CreateDate：2015-12-24下午01:45:32  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DiagnoseVo> queryDiagnoseBy(String inpatientNo) {
		String hql="select b.DETAIL_ID as detailId,b.DIAG_KIND as diagKind,b.DOCT_NAME as diagDoct,"
				+ "b.DIAG_DATE as diagDate, b.MAIN_FLAG as mainFlay, b.MAIN_FLAG as main,'ICD10' as icdCode,"
				+ "b.ICD_CODE as diagCode,e.ICD_DIAGNOSTICNAME as diagName from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_DIAGNOSE b "
				   		+ "left join T_BUSINESS_ICD10 e on e.ICD_DIAGNOSTICCODE=b.ICD_CODE  "
				   		+ "where b.del_flg=0 and b.stop_flg=0 and b.INPATIENT_NO=:inpatientNo "
					+ " union all "+
			        "select distinct c.ID as detailId,c.DIAG_KIND as diagKind,c.DIAG_DOCT as diagDoct,"
			        + "c.DIAG_DATE as diagDate,c.MAIN_FLAG as mainFlay,c.MAIN_FLAG as main,'医保' as icdCode, c.DIAG_CODE as diagCode,"+
					"c.DIAG_NAME as diagName from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_DIAGNOSE_MEDICARE c "
							+ "left join T_BUSINESS_ICDMEDICARE d on c.DIAG_CODE=d.ICD_CODE "
							+ "where c.del_flg=0 and c.INPATIENT_NO=:inpatientNo ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		List<DiagnoseVo> infoVoList =  namedParameterJdbcTemplate.query(hql,paraMap,new RowMapper<DiagnoseVo>() {

			@Override
			public DiagnoseVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DiagnoseVo vo = new DiagnoseVo();
				vo.setDetailId(rs.getString("detailId"));
				vo.setDiagKind(rs.getString("diagKind"));
				vo.setDiagDoct(rs.getString("diagDoct"));
				vo.setDiagDate(rs.getDate("diagDate"));
				vo.setMainFlay(rs.getObject("mainFlay")==null?null:rs.getInt("mainFlay"));
				vo.setMain(rs.getObject("main")==null?null:rs.getInt("main"));
				vo.setIcdCode(rs.getString("icdCode"));
				vo.setDiagCode(rs.getString("diagCode"));
				vo.setDiagName(rs.getString("diagName"));
				return vo;
			}
			
		});
		if(infoVoList!=null&&infoVoList.size()>0){
			return infoVoList;
		}
			return new ArrayList<DiagnoseVo>();
	}
	/**  
	 *  
	 * @Description：  查询icd诊断码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-25下午01:45:32  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DiagnoseVo> queryicdCode() {
		return null;
	}
	/**  
	 *  
	 * @Description：  查询医保诊断码
	 * @Author：zhangjin
	 * @CreateDate：2015-12-25下午01:45:32  
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DiagnoseVo> queryCode() {
		
		return null;
	}

	@Override
	public List<DiagnoseVo> getPages(String page, String rows,BusinessDiagnose diagnose) {
		String hql="";
		String sql="";
		if("2".equals(diagnose.getId())){
			 hql = jointIcd(diagnose.getDiagName());
			 sql ="select * from (select * from ("+hql+") n where n <= (:page) * :rows) where n > (:page - 1) * :rows";
		}else if("1".equals(diagnose.getId())){
			 hql = joint(diagnose.getDiagName());
			 sql ="select * from (select * from ("+hql+") n where n <= (:page) * :rows) where n > (:page - 1) * :rows";
		}
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"10":rows);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<DiagnoseVo> list =  namedParameterJdbcTemplate.query(sql,paraMap,new RowMapper<DiagnoseVo>() {

			@Override
			public DiagnoseVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DiagnoseVo vo = new DiagnoseVo(); 
				vo.setId(rs.getString("id"));
				vo.setCode(rs.getString("code"));
				vo.setDiagName(rs.getString("diagName"));
				return vo;
			}
			
		});
		return list;
	}

	@Override
	public int getTotals(BusinessDiagnose diagnose) {
		String hql="";
		String sql="";
		if("2".equals(diagnose.getId())){
			 hql = jointIcd(diagnose.getDiagName());
			 sql ="select count(1) from ("+hql+")";
		}else if("1".equals(diagnose.getId())){
			 hql = joint(diagnose.getDiagName());
			 sql ="select count(1) from ("+hql+")";
		}
		
		int count =jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
		return count;
	}
	/**  
	 *  
	 * @Description：(医保诊断)总记录
	 * @Author：zhangjin
	 * @CreateDate：2015-12-10 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String joint(String code) {
		String hql="select a.ICD_ID as id,a.ICD_CODE as code,a.ICD_NAME as diagName, "
				+ "ROWNUM n from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_ICDMEDICARE  a "
						+ "where a.del_flg=0 and a.pack_code in (02,03)";
		   if(code !=null){
			if(StringUtils.isNotBlank(code)){
				String queryName = code;
				hql+=" and a.ICD_CODE like '%"+queryName+"%'"
						+ " or a.ICD_NAME like '"+queryName+"%'";
			}
			  hql+=" order by a.ICD_CODE ";
		}
		return hql;
	}

	/**  
	 *  
	 * @Description：(icd代码)总记录
	 * @Author：zhangjin
	 * @CreateDate：2015-12-28 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String jointIcd(String code) {
		String hql="select b.ICD_ID as id,b.ICD_DIAGNOSTICCODE as code,b.ICD_DIAGNOSTICNAME as diagName, "
				+ "ROWNUM n from "+HisParameters.HISPARSCHEMAHISUSER+"T_BUSINESS_ICD10 b where del_flg=0 "
						+ "and stop_flg=0 ";
		  if(code !=null){
				if(StringUtils.isNotBlank(code)){
					String queryName = code;
					hql+=" and b.ICD_DIAGNOSTICCODE like '%"+queryName+"%'"
							+ " or b.ICD_DIAGNOSTICNAME like '"+queryName+"%'";
				}
				  hql+=" order by b.ICD_DIAGNOSTICCODE";
			}
			return hql;
	}
	/**  
	 *  
	 * @Description：  重新查询icd医保信息
	 * @Author：zhangjin
	 * @CreateDate：2015-12-30 
	 * @Modifier：
	 * @ModifyDate：  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public BusinessDiagnose getval(String id) {
		String hql="from BusinessDiagnose where del_flg=0 and stop_flg=0 ";
		if(StringUtils.isNotBlank(id)){
			hql=hql+" and id='"+id+"'";
		}
		List<BusinessDiagnose> list = this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return list.size()==0 ? new BusinessDiagnose() :list.get(0);
		
	}
	@Override
	public List<InpatientInfoNow> queryByMedicall(String medicalNo) {
		String hql="FROM InpatientInfoNow i WHERE i.medicalrecordId = :medicalNo and (i.inState='R' or i.inState='I')";
		List<InpatientInfoNow> iList = this.getSession().createQuery(hql).setParameter("medicalNo", medicalNo).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}

}


