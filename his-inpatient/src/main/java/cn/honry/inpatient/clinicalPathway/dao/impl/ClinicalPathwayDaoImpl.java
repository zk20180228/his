package cn.honry.inpatient.clinicalPathway.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.CpExecute;
import cn.honry.base.bean.model.CpExecuteDetail;
import cn.honry.base.bean.model.CpMasterIndex;
import cn.honry.base.bean.model.CpVcontrol;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.InoroutStandard;
import cn.honry.base.bean.model.InpAccess;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.PathApply;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.clinicalPathway.dao.ClinicalPathwayDao;
import cn.honry.utils.ShiroSessionUtils;

@Repository(value="clinicalPathwayDao")
@SuppressWarnings({"all"})
public class ClinicalPathwayDaoImpl extends HibernateEntityDao<CpWay> implements ClinicalPathwayDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<CpVcontrol> queryTree() {
		List<CpVcontrol> list = new ArrayList<CpVcontrol>();
		String hql ="From CpVcontrol where del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public List<CpWay> queryDisease() {
		List<CpWay> list = new ArrayList<CpWay>();
		String hql ="From CpWay where del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		return list;
	}
	
	@Override
	public List<CpVcontrol> queryCpWayVersion(String cpId) {
		List<CpVcontrol> list = new ArrayList<CpVcontrol>();
		String hql ="From CpVcontrol where cpId = '"+cpId+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public List<InoroutStandard> queryStand() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.stand_code as standCode, t.stand_name as standName ");
		buffer.append("from t_inorout_standard t where del_flg=0 and stop_flg=0 ");
		buffer.append("group by t.stand_code, t.stand_name ");
		List list = super.getSession().createSQLQuery(buffer.toString()).addScalar("standCode").addScalar("standName").setResultTransformer(Transformers.aliasToBean(InoroutStandard.class)).list();
		return list;
	}

	@Override
	public List<InoroutStandard> queryVersion(String standCode) {
		List<InoroutStandard> list = new ArrayList<InoroutStandard>();
		String hql ="From InoroutStandard where standCode = '"+standCode+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public CpWay findCpWayById(String id) {
		List<CpWay> list = new ArrayList<CpWay>();
		String hql ="From CpWay where id = '"+id+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new CpWay();
	}

	/**
	 * 判断版本是否唯一
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月21日 下午3:47:03 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月21日 下午3:47:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param cpVcontrol
	 * @return:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public boolean checkIsOnly(CpVcontrol cpVcontrol) {
		List<CpVcontrol> list = new ArrayList<CpVcontrol>();
		String hql ="From CpVcontrol  where cpName = '"+cpVcontrol.getCpName()+"' and versionNo = '"+cpVcontrol.getVersionNo()+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){//不为空，有值
			return false;
		}else{
			return true;
		}
	}

	@Override
	public CpVcontrol findCpVcontrolById(String id) {
		List<CpVcontrol> list = new ArrayList<CpVcontrol>();
		String hql ="From CpVcontrol  where id = '"+id+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){//不为空，有值
			return list.get(0);
		}else{
			return new CpVcontrol();
		}
	}

	@Override
	public List<CpwayPlan> queryTimeTree(String versionId) {
		List<CpwayPlan> list = new ArrayList<CpwayPlan>();
		String hql ="From CpwayPlan  where versionNo = '"+versionId+"' and del_flg=0 and stop_flg=0 and planCode is null order by stageId";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public CpwayPlan findCpWayPlanById(String id) {
		List<CpwayPlan> list = new ArrayList<CpwayPlan>();
		String hql ="From CpwayPlan  where id = '"+id+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){//不为空，有值
			return list.get(0);
		}else{
			return new CpwayPlan();
		}
	}

	@Override
	public void delOldPlan(String modelNature, String cpId, String stageId) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("delete from t_cpway_plan t where t.cp_id = '"+cpId+"' and t.plan_code = '"+modelNature+"' and t.stage_id ='"+stageId+"'" );
//		SQLQuery query = super.getSession().createSQLQuery(buffer.toString());
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(buffer.toString());
		int executeUpdate = query.executeUpdate();
	}

	@Override
	public List<InpatientInfoNow> findPatientByDeptCode(String treeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select i.patient_name as pname, i.inpatient_no as ino, ");
		buffer.append(" nvl(i.baby_flag, 0 ) as bf, i.report_sex as sex ");
		buffer.append("  from t_path_apply t ");
		buffer.append("  left join t_inpatient_info_now i on t.inpatient_no = i.inpatient_no ");
		buffer.append("  where t.apply_status = '1' and t.apply_type='1' ");
		buffer.append(" and t.apply_code = :deptCode ");
		buffer.append(" and t.del_flg = '0' and t.stop_flg='0' ");
		map.put("deptCode", treeId);
		
		List<InpatientInfoNow> list = namedParameterJdbcTemplate.query(buffer.toString(), map, new RowMapper<InpatientInfoNow>() {

			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InpatientInfoNow pa = new InpatientInfoNow();
				pa.setPatientName(rs.getString("pname"));//用于存放患者姓名
				pa.setInpatientNo(rs.getString("ino"));//患者的住院流水号
				pa.setBabyFlag(Integer.valueOf(rs.getString("bf")));
				pa.setReportSex(rs.getString("sex"));
				return pa;
			}
		});
		return list;
	}
	
	@Override
	public List<InpatientInfoNow> findInPatientByDeptCode(String treeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select i.patient_name as pname, i.inpatient_no as ino, ");
		buffer.append(" nvl(i.baby_flag, 0 ) as bf, i.report_sex as sex ");
		buffer.append("  from t_cp_master_index t ");
		buffer.append("  left join t_inpatient_info_now i on t.inpatient_no = i.inpatient_no ");
		buffer.append("  where t.dept_code = :deptCode ");
		buffer.append(" and t.del_flg = '0' and t.stop_flg='0' ");
		map.put("deptCode", treeId);
		
		List<InpatientInfoNow> list = namedParameterJdbcTemplate.query(buffer.toString(), map, new RowMapper<InpatientInfoNow>() {
			
			@Override
			public InpatientInfoNow mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InpatientInfoNow pa = new InpatientInfoNow();
				pa.setPatientName(rs.getString("pname"));//用于存放患者姓名
				pa.setInpatientNo(rs.getString("ino"));//患者的住院流水号
				pa.setBabyFlag(Integer.valueOf(rs.getString("bf")));
				pa.setReportSex(rs.getString("sex"));
				return pa;
			}
		});
		return list;
	}
	
	public List<CpwayPlan> findStageByInpatientNo(String inpatientNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select distinct p.stage_id as stageId,p.id as id ");
		buffer.append("  from t_cp_execute e ");
		buffer.append("  left join t_cpway_plan p on e.cp_id = p.id ");
		buffer.append("  left join t_inpatient_info_now i on e.inpatient_no = i.inpatient_no ");
		buffer.append(" where e.cp_id in ");
		buffer.append(" (select e.cp_id ");
		buffer.append(" from t_cp_master_index m ");
		buffer.append(" left join t_cp_execute e on m.inpatient_no = e.inpatient_no ");
		buffer.append(" and m.version_code = e.model_version ");
		buffer.append(" where m.inpatient_no = :inpatientNo) ");
		buffer.append(" and e.delect_flag = 0 and e.stop_flag = 0 ");
		buffer.append("  order by p.stage_id ");
		map.put("inpatientNo", inpatientNo);
		
		List<CpwayPlan> list = namedParameterJdbcTemplate.query(buffer.toString(), map, new RowMapper<CpwayPlan>() {
			
			@Override
			public CpwayPlan mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CpwayPlan cp = new CpwayPlan();
				cp.setId(rs.getString("id"));
				cp.setStageId(rs.getString("stageId"));
				return cp;
			}
		});
		return list;
	}

	@Override
	public List<CpExecute> executeInfoByInpatientNo(String inpatientNo) {
		List<CpExecute> list = new ArrayList<CpExecute>();
		String hql ="From CpExecute  where inpatientNo = '"+inpatientNo+"' order by planCode";
		list = super.find(hql, null);
		return list;
	}
	
	@Override
	public List<CpExecuteDetail> executeDetail(String inpatientNo,String cpId,String modelCode) {
		List<CpExecuteDetail> list = new ArrayList<CpExecuteDetail>();
		String hql ="From CpExecuteDetail  where inpatientNo = '"+inpatientNo+"' and cpId = '"+cpId+"' and modelCode = '"+modelCode+"'";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public void outPath(String inpatientNo) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("update t_cp_master_index t set t.outpath_flag='1' , t.outpathtime = sysdate where t.inpatient_no='"+inpatientNo+"'" );
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(buffer.toString());
		int executeUpdate = query.executeUpdate();
	}

	@Override
	public void executePath(String executeId) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuffer buffer=new StringBuffer();
		buffer.append("update t_cp_execute t set t.EXECUTE_FLAG = '1' , t.EXECUTE_DATE=sysdate , t.EXECUTE_USER = '"+longinUserAccountCode+"' where id='"+executeId+"'" );
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(buffer.toString());
		int executeUpdate = query.executeUpdate();
	}

	@Override
	public List<InpAccess> queryAssess(String inpatientNo, String stage,
			String page, String rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hqlBuffer=new StringBuffer();
		hqlBuffer.append("select  * from( select t.*,rownum as n from ( ");
		
		hqlBuffer.append(" select t.id as id, t.inpatient_no as inpatientNo, t.stage_id as stageId, t.access_date as accessDate,t.access_user as accessUser, ");
		hqlBuffer.append(" t.access_result as accessResult,t.role_flag as roleFlag,t.access_check_user as accessCheckUser,t.access_check_date as accessCheckDate,t.access_check_info as accessCheckInfo,t.days as days ");
		hqlBuffer.append("  from t_inp_access t ");
		hqlBuffer.append("  where t.inpatient_no = :inpatientNo ");
		hqlBuffer.append(" and t.stage_id = :stage ");
		hqlBuffer.append(" and t.delect_flag = '0' and t.stop_flag = '0' ");
		Integer p = StringUtils.isBlank(page) ? 1 : Integer.valueOf(page);
		Integer r = StringUtils.isBlank(rows) ? 20 : Integer.valueOf(rows);
		map.put("page", p);
		map.put("row", r);
		
		map.put("inpatientNo", inpatientNo);
		map.put("stage", stage);
		hqlBuffer.append(" ) t where rownum <= :page * :row) t1 where t1.n > (:page -1) * :row ");
		List<InpAccess> list =  namedParameterJdbcTemplate.query(hqlBuffer.toString(), map, new RowMapper<InpAccess>() {
			@Override
			public InpAccess mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpAccess ia = new InpAccess();
				ia.setId(rs.getString("id"));
				ia.setInpatientNo(rs.getString("inpatientNo"));
				ia.setStageId(rs.getString("stageId"));
				ia.setAccessDate(rs.getTimestamp("accessDate"));
				ia.setAccessUser(rs.getString("accessUser"));
				ia.setAccessResult(rs.getString("accessResult"));
				ia.setRoleFlag(rs.getString("roleFlag"));
				ia.setAccessCheckUser(rs.getString("accessCheckUser"));
				ia.setAccessCheckDate(rs.getTimestamp("accessCheckDate"));
				ia.setAccessCheckInfo(rs.getString("accessCheckInfo"));
				ia.setDays(rs.getString("days"));
				return ia;
			}
		});
		return list;
	}

	@Override
	public InpAccess findInpAccess(String id) {
		List<InpAccess> list = new ArrayList<InpAccess>();
		String hql ="From InpAccess where id = '"+id+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new InpAccess();
	}

	@Override
	public int queryAssessNum(String inpatientNo, String stage) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ");
		
		sb.append(" select t.id as id, t.inpatient_no as inpatientNo, t.stage_id as stageId, t.access_date as accessDate,t.access_user as accessUser, ");
		sb.append(" t.access_result as accessResult,t.role_flag as roleFlag,t.access_check_user as accessCheckUser,t.access_check_date as accessCheckDate,t.access_check_info as accessCheckInfo,t.days as days ");
		sb.append("  from t_inp_access t ");
		sb.append("  where t.inpatient_no = '"+inpatientNo+"' ");
		sb.append(" and t.stage_id = '"+stage+"' ");
		sb.append(" and t.delect_flag = '0' and t.stop_flag = '0' ");
		
		sb.append(" )");
		Object count=getSession().createSQLQuery(sb.toString()).uniqueResult();
		return Integer.valueOf((count==null?0:count).toString());
	}

	@Override
	public CpMasterIndex findCmiByInpatient(String inpatientNo) {
		List<CpMasterIndex> list = new ArrayList<CpMasterIndex>();
		String hql ="From CpMasterIndex where inpatientNo = '"+inpatientNo+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
