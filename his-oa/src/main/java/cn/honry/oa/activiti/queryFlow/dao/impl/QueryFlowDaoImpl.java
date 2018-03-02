package cn.honry.oa.activiti.queryFlow.dao.impl;

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
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaReminders;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.queryFlow.dao.QueryFlowDao;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @Description：  流程查询
 * @Author：donghe
 * @CreateDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("queryFlowDao")
@SuppressWarnings({ "all" })
public class QueryFlowDaoImpl extends HibernateEntityDao<OaTaskInfo> implements QueryFlowDao{
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public OaTaskInfo querybyInstanceId(String instanceId) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select t.ATTR2 as attr2,t.COMPLETE_STATUS as completeStatus from T_OA_TASK_INFO t where t.PROCESS_INSTANCE_ID = '"+instanceId+"' ");
		List<OaTaskInfo> list = (List<OaTaskInfo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("attr2").addScalar("completeStatus");
				return queryObject.setResultTransformer(Transformers.aliasToBean(OaTaskInfo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OaTaskInfo> queryOaTaskInfo() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select t.ATTR2 as attr2,t.PROCESS_INSTANCE_ID as processInstanceId from T_OA_TASK_INFO t");
		List<OaTaskInfo> list = (List<OaTaskInfo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("attr2").addScalar("processInstanceId");
				return queryObject.setResultTransformer(Transformers.aliasToBean(OaTaskInfo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}

	@Override
	public List<OaKVRecord> queryOaKVRecord(String id,int page,int rows,String category,String startTime,String endTime) {
		final StringBuffer buffer = new StringBuffer();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		buffer.append("select * from (select ROWNUM AS n,aa.* from (");
		buffer.append("select t.id as id,t.CATEGORY as category,t.STATUS as status,t.REF as ref,"
				+ "t.USER_ID as userId,t.name as name,t.FORM_TEMPLATE_CODE as formTemplateCode,"
				+ "t.TENANT_ID as tenantId,t.CREATEUSER createUser,t.CREATETIME createTime from T_OA_KV_RECORD t "
				+ " where t.STATUS=1 and t.del_flg = 0 and "
				+ "t.CREATEUSER = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and t.name like '%"+category+"%'");
		}
		if(startTime!=null&&!"".equals(startTime)){
			buffer.append(" and t.CREATETIME > to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
			paraMap.put("startTime", startTime);
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and t.CREATETIME <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
			paraMap.put("endTime", endTime);
		}
		buffer.append(" order by t.CREATETIME desc )aa where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		int start = Integer.parseInt(page+"" == null ? "1" : page+"");
		int count = Integer.parseInt(rows+"" == null ? "20" : rows+"");
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<OaKVRecord> list = namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<OaKVRecord>() {
			@Override
			public OaKVRecord mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaKVRecord vo = new OaKVRecord();
				vo.setId(rs.getString("id"));
				vo.setCategory(rs.getString("category"));
				vo.setStatus(rs.getInt("status"));
				vo.setRef(rs.getString("ref")); 
				vo.setUserId(rs.getString("userId")); 
				vo.setName(rs.getString("name")); 
				vo.setFormTemplateCode(rs.getString("formTemplateCode")); 
				vo.setTenantId(rs.getString("tenantId")); 
				vo.setCreateUser(rs.getString("createUser")); 
				vo.setCreateTime(rs.getTimestamp("createTime")); 
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaKVRecord>();
	}

	@Override
	public int queryOaKVRecordtotal(String id,String category,String startTime,String endTime) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select count(1) from T_OA_KV_RECORD t "
				+ " where t.STATUS=1 and t.del_flg = 0 and "
				+ "t.CREATEUSER = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and t.name like '%"+category+"%'");
		}
		if(startTime!=null&&!"".equals(startTime)){
			buffer.append(" and t.CREATETIME > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and t.CREATETIME <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		return jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
	}
	@Override
	public List<OaTaskInfo> queryOaTaskInfoVAct(String param,int page,int rows,String category,String startTime,String endTime) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(" select * from ( SELECT row_.*, ROWNUM rownum_ FROM ( ");
		buffer.append("select t.START_TIME_ as startTime,i.ASSIGNEE as assignee,i.CODE as code,i.PROCESS_INSTANCE_ID as processInstanceId ,i.id as businessKey,i.COMPLETE_TIME as completeTime,i.name as name,i.attr2 as attr2, "
				+ " i.COMPLETE_STATUS as completeStatus,i.EXPIRATION_TIME as expirationTime,NVL(r.REMINDERNUM, 0) AS reminderNum "//,ROWNUM AS RN "
				+ " from act_hi_procinst t join t_oa_task_info i on i.business_key=t.business_key_ and i.DEL_FLG=0 and i.STOP_FLG=0 left join T_OA_REMINDERS r on r.TASKINFOID = i.id "
				+ " where t.end_time_ is null "
				+ " and i.STATUS='active' and t.START_USER_ID_='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and i.attr2 like '%"+category+"%'");
		}
		if(startTime!=null&&!"".equals(startTime)){
			buffer.append(" and t.START_TIME_ > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and t.START_TIME_ <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		buffer.append(" order by t.START_TIME_ desc ) row_ where ROWNUM <="+(page*rows)+"  ) WHERE rownum_ >"+(page-1)*rows);
		List<OaTaskInfo> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaTaskInfo>() {
			@Override
			public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaTaskInfo vo = new OaTaskInfo();
				vo.setAssignee(rs.getString("assignee"));
				vo.setProcessInstanceId(rs.getString("processInstanceId"));
				vo.setBusinessKey(rs.getString("businessKey"));
				vo.setCompleteTime(rs.getTimestamp("completeTime")); 
				vo.setExpirationTime(rs.getTimestamp("expirationTime")); 
				vo.setName(rs.getString("name")); //当前环节
				vo.setAttr2(rs.getString("attr2")); //标题
				vo.setCode(rs.getString("code")); 
				vo.setCompleteStatus(rs.getString("completeStatus")); 
				vo.setStartTime(rs.getTimestamp("startTime"));
				vo.setReminderNum(rs.getInt("reminderNum"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}
	
	@Override
	public int queryOaTaskInfoVActTotal(String param, String category,
			String startTime, String endTime) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select count(1) from act_hi_procinst t join t_oa_task_info i on i.business_key=t.business_key_ and i.DEL_FLG=0 and i.STOP_FLG=0 "
				+ " left join T_OA_REMINDERS r on  r.TASKINFOID = i.id "
				+ " where t.end_time_ is null "
				+ " and i.STATUS='active' and t.START_USER_ID_='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and i.attr2 like '%"+category+"%'");
		}
		if(startTime!=null&&!"".equals(startTime)){
			buffer.append(" and t.START_TIME_ > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and t.START_TIME_ <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		return jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
	}

	@Override
	public List<OaTaskInfo> tuijianqueryOaTaskInfoVAct(String param,int page,int rows,String category,String startTime,String endTime) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM (");
		buffer.append("select m.processinstanceid  as processInstanceId,");
		buffer.append("       m.id                 as businessKey,");
		buffer.append("       m.attr2              as attr2,");
		buffer.append("       m.createtime         as startTime,");//申请时间
		buffer.append("       m.operationTime      as completeTime,");//撤回时间
		buffer.append("       n.name               as name,");
		buffer.append("       n.last_modified_time as rollbackTime,");//退回时间
		buffer.append("       n.status             as completeStatus");
		buffer.append("  from (select t.process_instance_id as processinstanceid,");
		buffer.append("               t.createtime as operationTime,");
		buffer.append("               t.id,");
		buffer.append("               t.attr2,");
		buffer.append("               t.process_instance_id,");
		buffer.append("               i.createtime as createtime");
		buffer.append("          from t_oa_task_info t, t_oa_task_info i, act_hi_procinst a");
		buffer.append("         where t.createuser = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		buffer.append("           and t.status = 'active'");
		buffer.append("           and t.assignee = t.createuser");
		buffer.append("           and t.business_key = a.business_key_");
		buffer.append("           and a.end_time_ is null");
		buffer.append("           and a.delete_reason_ is null");
		buffer.append("           and i.process_instance_id = t.process_instance_id");
		buffer.append("           and i.catalog = 'start' and i.DEL_FLG=0 and i.STOP_FLG=0 ) m,");
		buffer.append("       (select a.name, a.status, a.last_modified_time, a.process_instance_id");
		buffer.append("          from (select *");
		buffer.append("                  from t_oa_task_info t");
		buffer.append("                 where t.status in ('rollback', 'withdraw')) a,");
		buffer.append("               (select t.process_instance_id as process_instance_id,");
		buffer.append("                       max(t.createtime) as createtime");
		buffer.append("                  from t_oa_task_info t");
		buffer.append("                 where t.status <> 'active'");
		buffer.append("                 group by t.process_instance_id) b");
		buffer.append("         where a.process_instance_id = b.process_instance_id");
		buffer.append("           and a.createtime = b.createtime) n");
		buffer.append(" where m.process_instance_id = n.process_instance_id");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and m.attr2 like '%"+category+"%' ");
		}
		if(StringUtils.isNoneBlank(startTime)){
			buffer.append(" and m.createtime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and m.createtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		buffer.append(" order by createtime desc");
		
		//buffer.append(" select tab.* from ( ");
//		buffer.append(" SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM (");
//		buffer.append(" select t1.process_instance_id as processInstanceId,t1.id as businessKey,t1.createtime as completeTime,t1.name as name,t1.attr2 as attr2,t1.complete_status as completeStatus,t2.createtime as startTime ");//,rownum rn
//		buffer.append(" from (select t.process_instance_id, t.code,t.id,t.complete_status,t.name,t.attr2,t.CREATETIME ");
//		buffer.append(" from t_oa_task_info t where t.Createuser = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' and t.status = 'active' and t.catalog <> 'start' ) t1, ");
//		buffer.append(" (select t.process_instance_id,t.createtime, t.code from t_oa_task_info t where t.createuser = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' and t.catalog = 'start') t2 ");
//		buffer.append(" where t1.code = t2.code and t1.process_instance_id = t2.process_instance_id ");
//		if(StringUtils.isNoneBlank(category)){
//			buffer.append(" and t1.attr2 like '%"+category+"%' ");
//		}
//		if(StringUtils.isNoneBlank(startTime)){
//			buffer.append(" and t1.createtime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
//		}
//		if(StringUtils.isNoneBlank(endTime)){
//			buffer.append(" and t1.createtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
//		}
//		buffer.append(" order by t1.CREATETIME desc ");
		//buffer.append(" and rownum <= "+page*rows);
		//buffer.append(" order by t1.CREATETIME desc ) tab where tab.rn > "+(page-1)*rows);
		buffer.append(" ) row_ WHERE ROWNUM <= "+page*rows+" ) WHERE rownum_ > "+(page-1)*rows);
		List<OaTaskInfo> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaTaskInfo>() {
			@Override
			public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaTaskInfo vo = new OaTaskInfo();
				String status = rs.getString("completeStatus");
				String nameStatus = "";
				if("withdraw".equals(status)){
					nameStatus = "(撤回)";
				}else if("rollback".equals(status)){
					nameStatus = "(退回)";
				}
				vo.setProcessInstanceId(rs.getString("processInstanceId"));
				vo.setBusinessKey(rs.getString("businessKey"));
				vo.setCompleteTime(rs.getTimestamp("rollbackTime")==null ? rs.getTimestamp("completeTime") : rs.getTimestamp("rollbackTime")); 
				vo.setStartTime(rs.getTimestamp("startTime"));
				vo.setName(rs.getString("name")+nameStatus); 
				vo.setAttr2(rs.getString("attr2"));
				vo.setCompleteStatus(status); 
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}
	
	@Override
	public int tuijianqueryOaTaskInfoVActToal(String param, String category,
			String startTime, String endTime) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select count(1) from ( ");
		buffer.append("select m.processinstanceid  as processInstanceId,");
		buffer.append("       m.id                 as businessKey,");
		buffer.append("       m.attr2              as attr2,");
		buffer.append("       m.createtime         as startTime,");//申请时间
		buffer.append("       m.operationTime      as completeTime,");//撤回时间
		buffer.append("       n.name               as name,");
		buffer.append("       n.last_modified_time as rollbackTime,");//退回时间
		buffer.append("       n.status             as completeStatus");
		buffer.append("  from (select t.process_instance_id as processinstanceid,");
		buffer.append("               t.createtime as operationTime,");
		buffer.append("               t.id,");
		buffer.append("               t.attr2,");
		buffer.append("               t.process_instance_id,");
		buffer.append("               i.createtime as createtime");
		buffer.append("          from t_oa_task_info t, t_oa_task_info i, act_hi_procinst a");
		buffer.append("         where t.createuser = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		buffer.append("           and t.status = 'active'");
		buffer.append("           and t.assignee = t.createuser");
		buffer.append("           and t.business_key = a.business_key_");
		buffer.append("           and a.end_time_ is null");
		buffer.append("           and a.delete_reason_ is null");
		buffer.append("           and i.process_instance_id = t.process_instance_id");
		buffer.append("           and i.catalog = 'start' and i.DEL_FLG=0 and i.STOP_FLG=0) m,");
		buffer.append("       (select a.name, a.status, a.last_modified_time, a.process_instance_id");
		buffer.append("          from (select *");
		buffer.append("                  from t_oa_task_info t");
		buffer.append("                 where t.status in ('rollback', 'withdraw')) a,");
		buffer.append("               (select t.process_instance_id as process_instance_id,");
		buffer.append("                       max(t.createtime) as createtime");
		buffer.append("                  from t_oa_task_info t");
		buffer.append("                 where t.status <> 'active'");
		buffer.append("                 group by t.process_instance_id) b");
		buffer.append("         where a.process_instance_id = b.process_instance_id");
		buffer.append("           and a.createtime = b.createtime) n");
		buffer.append(" where m.process_instance_id = n.process_instance_id");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and m.attr2 like '%"+category+"%' ");
		}
		if(StringUtils.isNoneBlank(startTime)){
			buffer.append(" and m.createtime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and m.createtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		buffer.append(" order by m.CREATETIME desc ) tab ");
		return jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
	}

	@Override
	public int queryListSize(String userAccount,String startTime,String endTime,String title) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(" select count(1) from ( ");
		buffer.append(" select t.ID as id, ");
		buffer.append(" t.PROCEDUREID as procedureId,");
		buffer.append(" t.PORCEDURENAME as procedureName,");
		buffer.append(" t.REMINDERNUM as reminderNum,");
		buffer.append(" t.REMINDTIME as remindTime,");
		buffer.append(" t.REMINDEREDNAME as reminderdName,");
		buffer.append(" t.REMINDENODENAME as remindenodeName,");
		buffer.append(" t.remindrestatus as remindreStatus,");
		buffer.append(" t.REMINDRECONTENT as remindreContent,");
		buffer.append(" t.CREATEUSER as createUser,");
		buffer.append(" t.CREATETIME as createTime,");
		buffer.append(" t.REMINDNODE as remindnode,");
		buffer.append(" t.REMIDERETIME as remideretime,");
		buffer.append(" t.REMINDER,t.DEL_FLG, ");
		buffer.append(" (row_number()");
		buffer.append(" over(partition by procedureid order by remindtime desc)) mn");
		buffer.append(" from t_oa_reminders t");
		buffer.append(" where t.procedureid is not null and t.DEL_FLG=0");
		buffer.append(" and t.REMINDER = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' ");
		if(StringUtils.isNotBlank(startTime)){
			buffer.append(" and t.REMINDTIME > to_date('"+ startTime+"','yyyy-MM-DD hh24:mi:ss')" );
		}
		if(StringUtils.isNotBlank(endTime)){
			buffer.append(" and t.REMINDTIME <= to_date('"+ endTime+"','yyyy-MM-DD hh24:mi:ss')");
		}
		if(StringUtils.isNotBlank(title)){
			buffer.append(" and t.PORCEDURENAME like '%" +title+"%'");
		}
		buffer.append(" ) rm");
		buffer.append(" where rm.mn = 1  ");
		
		return jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
	}
	@Override
	public List<OaReminders> queryList(String userAccount, String startTime,
			String endTime, String param, int page, int rows) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(" select * from ");
		buffer.append(" (select rownum as n, aa.* from ( ");
		buffer.append(" select id,procedureId,procedureName,reminderNum,remindTime,reminderdName,remindenodeName,remindreStatus,"
				+ "remindreContent,createUser,createTime,remindnode, remideretime,remindcontent from ( ");
		buffer.append(" select t.ID as id, ");
		buffer.append(" t.PROCEDUREID as procedureId,");
		buffer.append(" t.REMINDCONTENT As remindcontent, ");
		buffer.append(" t.PORCEDURENAME as procedureName,");
		buffer.append(" t.REMINDERNUM as reminderNum,");
		buffer.append(" t.REMINDTIME as remindTime,");
		buffer.append(" t.REMINDEREDNAME as reminderdName,");
		buffer.append(" t.REMINDENODENAME as remindenodeName,");
		buffer.append(" t.remindrestatus as remindreStatus,");
		buffer.append(" t.REMINDRECONTENT as remindreContent,");
		buffer.append(" t.CREATEUSER as createUser,");
		buffer.append(" t.CREATETIME as createTime,");
		buffer.append(" t.REMINDNODE as remindnode,");
		buffer.append(" t.REMIDERETIME as remideretime,");
		buffer.append(" t.REMINDER,t.DEL_FLG, ");
		buffer.append(" (row_number()");
		buffer.append(" over(partition by procedureid order by remindtime desc)) mn");
		buffer.append(" from t_oa_reminders t");
		buffer.append(" where t.procedureid is not null ");
		buffer.append(" and t.REMINDER = '"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' ");
		if(StringUtils.isNotBlank(startTime)){
			buffer.append(" and t.REMINDTIME > to_date('"+ startTime+"','yyyy-MM-DD hh24:mi:ss')" );
		}
		if(StringUtils.isNotBlank(endTime)){
			buffer.append(" and t.REMINDTIME <= to_date('"+ endTime+"','yyyy-MM-DD hh24:mi:ss')");
		}
		if(StringUtils.isNotBlank(param)){
			buffer.append(" and t.PORCEDURENAME like '%" +param+"%'");
		}
		buffer.append(" ) rm");
		buffer.append(" where rm.mn = 1 and rm.DEL_FLG=0 ");
		buffer.append("  order by rm.remindtime desc ");
		buffer.append(" ) aa ");
		buffer.append(" where rownum <= :page * :rows) ");
		buffer.append(" where n > (:page - 1) * :rows ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page+"" == null ? "1" : page+"");
		int count = Integer.parseInt(rows+"" == null ? "20" : rows+"");
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<OaReminders> list = namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<OaReminders>() {
			@Override
			public OaReminders mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaReminders vo = new OaReminders();
				vo.setId(rs.getString("id"));
				vo.setProcedureId(rs.getString("procedureId"));
				vo.setProcedureName(rs.getString("procedureName"));
				vo.setReminderNum(rs.getInt("reminderNum"));
				vo.setRemindTime(rs.getTimestamp("remindTime"));
				vo.setReminderdName(rs.getString("reminderdName"));
				vo.setRemindreStatus(rs.getInt("remindreStatus"));
				vo.setRemindenodeName(rs.getString("remindenodeName"));
				vo.setRemindreContent(rs.getString("remindreContent"));
				vo.setCreateUser(rs.getString("createUser"));
				vo.setCreateTime(rs.getTimestamp("createTime"));
				vo.setRemindNode(rs.getString("remindnode"));
				vo.setRemidereTime(rs.getTimestamp("remideretime"));
				vo.setRemindcontent(rs.getString("remindcontent"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaReminders>();
	}

	@Override
	public List<OaReminders> queryListpan(String taskInfoId) {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuilder hql = new StringBuilder();
		hql.append("from OaReminders t where t.del_flg=0 and t.reminder=:account and t.taskInfoId=:taskInfoId ");
		List<OaReminders> iList=this.getSession().createQuery(hql.toString()).setParameter("account",account).setParameter("taskInfoId",taskInfoId).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}
	@Override
	public List<OaReminders> queryListId(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("from OaReminders t where t.id=:id");
		List<OaReminders> iList=this.getSession().createQuery(hql.toString()).setParameter("id",id).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return null;
	}

	@Override
	public List<OaTaskInfo> getListForTask(String account, String tenantId) {
		StringBuilder hql = new StringBuilder();
		hql.append("from OaTaskInfo where assignee like :account and tenantId=:tenantId and status='active' and stop_flg=0 and del_flg=0 order by createTime desc");
		List<OaTaskInfo> list=this.getSession().createQuery(hql.toString()).setParameter("account","%"+account+"%").setParameter("tenantId",tenantId).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}

	@Override
	public List<OaReminders> queryListcui(String userAccount) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select t.ID as id ,t.PROCEDUREID as procedureId,"
				+ "t.PORCEDURENAME as procedureName,t.REMINDERNUM as reminderNum,"
				+ "t.REMINDTIME as remindTime,t.REMINDEREDNAME as reminderdName,"
				+ "t.REMINDRECONTENT as remindreContent,t.CREATEUSER as createUser,"
				+ "t.CREATETIME as createTime  from T_OA_REMINDERS t where t.DEL_FLG=0 and t.REMINDERED='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		List<OaReminders> list = (List<OaReminders>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("id").addScalar("procedureId")
						.addScalar("procedureName").addScalar("reminderNum",Hibernate.INTEGER)
						.addScalar("remindTime",Hibernate.TIMESTAMP).addScalar("reminderdName")
						.addScalar("remindreContent").addScalar("createUser").addScalar("createTime",Hibernate.TIMESTAMP);
				return queryObject.setResultTransformer(Transformers.aliasToBean(OaReminders.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<OaTaskInfo> querylishijili(String param) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select p.business_key as businessKey,p.process_instance_id as processInstanceId,"
				+ "p.attr2 name,p.createuser as createUser,p.createtime as createTime,"
				+ "p.PROCESS_STARTER processStarter,p.COMPLETE_TIME  as completeTime from act_hi_taskinst t "
				+ " join t_oa_task_info p on p.process_instance_id=t.proc_inst_id_ where t.assignee_='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' and t.tenant_id_='1' "
				+ "and t.END_TIME_ is not null ");//and p.STATUS='complete'
		List<OaTaskInfo> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaTaskInfo>() {
			@Override
			public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaTaskInfo vo = new OaTaskInfo();
				vo.setBusinessKey(rs.getString("businessKey"));
				vo.setProcessInstanceId(rs.getString("processInstanceId"));
				vo.setName(rs.getString("name")); 
				vo.setCreateUser(rs.getString("createUser")); 
				vo.setCreateTime(rs.getTimestamp("createTime")); 
				vo.setProcessStarter(rs.getString("processStarter")); 
				vo.setCompleteTime(rs.getTimestamp("completeTime")); 
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<OaTaskInfo> querylistyijie(String param,int page,int rows,String category,String startTime,String endTime) {
		final StringBuffer buffer = new StringBuffer();
//		buffer.append(" select * from ( ");
		buffer.append(" SELECT * FROM (SELECT row_.*, ROWNUM rownum_ FROM ( ");
		buffer.append("select i.PROCESS_INSTANCE_ID as processInstanceId ,i.id as businessKey,t.start_time_ as startTime,i.attr2 as name, "
				+ " i.COMPLETE_STATUS as completeStatus,t.end_time_ as completeTime,NVL(r.REMINDERNUM,0) AS reminderNum,t.end_act_id_ as endActId "//,ROWNUM AS RN "
				+ " from act_hi_procinst t join t_oa_task_info i on i.business_key=t.business_key_ and i.DEL_FLG=0 and i.STOP_FLG=0  left join T_OA_REMINDERS r on r.procedureid = i.PROCESS_INSTANCE_ID and i.code=r.remindnode "
				+ " where t.end_time_ is not null "
				+ " and t.START_USER_ID_='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'"
				+ " and i.catalog = 'start' ");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and i.attr2 like '%"+category+"%' ");
		}
		if(StringUtils.isNoneBlank(startTime)){
			buffer.append(" and t.end_time_ > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and t.end_time_ <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
//		buffer.append(" AND ROWNUM <="+(page*rows)+" order by t.start_time_ desc ) WHERE RN >"+(page-1)*rows);
		buffer.append(" order by t.start_time_ desc) row_ WHERE ROWNUM <= "+(page*rows)+") WHERE rownum_ > "+(page-1)*rows);
		List<OaTaskInfo> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaTaskInfo>() {
			@Override
			public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaTaskInfo vo = new OaTaskInfo();
				vo.setProcessInstanceId(rs.getString("processInstanceId"));
				vo.setBusinessKey(rs.getString("businessKey"));
				vo.setCompleteTime(rs.getTimestamp("completeTime")); //完成时间
				vo.setStartTime(rs.getTimestamp("startTime")); //申请时间
				vo.setName(rs.getString("name")); 
				vo.setReminderNum(rs.getInt("reminderNum"));
				vo.setCompleteStatus(rs.getString("completeStatus")); 
				vo.setEndActId(rs.getString("endActId")); 
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}

	@Override
	public int querylistyijieTotal(String param, String category, String startTime,
			String endTime) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(1) from act_hi_procinst t join t_oa_task_info i on i.business_key=t.business_key_ and i.DEL_FLG=0 and i.STOP_FLG=0 "
				+ " left join T_OA_REMINDERS r on r.procedureid = i.PROCESS_INSTANCE_ID and i.code=r.remindnode "
				+ " where t.end_time_ is not null "
				+ " and t.START_USER_ID_='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'"
				+ " and i.catalog = 'start' ");
		if(StringUtils.isNoneBlank(category)){
			buffer.append(" and i.attr2 like '%"+category+"%' ");
		}
		if(StringUtils.isNoneBlank(startTime)){
			buffer.append(" and t.end_time_ > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(endTime)){
			buffer.append(" and t.end_time_ <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		return jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
	}

	@Override
	public List<OaBpmCategory> quert() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select t.id id,t.name name from t_oa_bpm_category t");
		List<OaBpmCategory> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaBpmCategory>() {
			@Override
			public OaBpmCategory mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaBpmCategory vo = new OaBpmCategory();
				vo.setId(rs.getString("id"));
				vo.setName(rs.getString("name"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public void deleteMyCuiBan(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update t_oa_reminders t set t.DEL_FLG = 1 where t.id = '"+id+"'");
		jdbcTemplate.update(sb.toString());
	}

	@Override
	public void deleteMyCuiBanByProcess(String processId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update t_oa_reminders t set t.DEL_FLG = 1 where t.PROCEDUREID = '"+processId+"'");
		jdbcTemplate.update(sb.toString());
	}

	@Override
	public List<OaReminders> getMyCuiBanByProecssID(String processid) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select t.ID as id ,t.PROCEDUREID as procedureId,"
		+ "t.PORCEDURENAME as procedureName,t.REMINDERNUM as reminderNum,"
		+ "t.REMINDTIME as remindTime,t.REMINDEREDNAME as reminderdName,"
		+ "t.REMINDENODENAME as remindenodeName,t.remindrestatus  as remindreStatus,"
		+ "t.REMINDRECONTENT as remindreContent,t.CREATEUSER as createUser,"
		+ "t.CREATETIME as createTime, t.REMINDNODE as remindnode,t.REMIDERETIME as remideretime,t.REMINDCONTENT As remindcontent  from T_OA_REMINDERS t  "
		+ "where t.DEL_FLG=0 and t.REMINDER='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'");
		buffer.append(" and t.PROCEDUREID = '"+processid+"'");
		buffer.append(" order by t.REMINDTIME desc ");
		List<OaReminders> list = namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<OaReminders>() {
			@Override
			public OaReminders mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaReminders vo = new OaReminders();
				vo.setId(rs.getString("id"));
				vo.setProcedureId(rs.getString("procedureId"));
				vo.setProcedureName(rs.getString("procedureName"));
				vo.setReminderNum(rs.getInt("reminderNum"));
				vo.setRemindTime(rs.getTimestamp("remindTime"));
				vo.setReminderdName(rs.getString("reminderdName"));
				vo.setRemindreStatus(rs.getInt("remindreStatus"));
				vo.setRemindenodeName(rs.getString("remindenodeName"));
				vo.setRemindreContent(rs.getString("remindreContent"));
				vo.setCreateUser(rs.getString("createUser"));
				vo.setCreateTime(rs.getTimestamp("createTime"));
				vo.setRemindNode(rs.getString("remindnode"));
				vo.setRemidereTime(rs.getTimestamp("remideretime"));
				vo.setRemindcontent(rs.getString("remindcontent"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaReminders>();
	}

	@Override
	public List<OaTaskInfo> getListForTask(String account, String tenantId, String startTime, String endTime, String title, int page, int rows) {
		
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (select ROWNUM AS n,aa.* from (");
		buffer.append("select t.id,t.attr2,t.createuser,t.createtime,");
		buffer.append("t.process_starter as processStarter,t.complete_time as completeTime,t.name,NVL(r.REMINDERNUM,0) as REMINDERNUM,");
		buffer.append("(t.EXPIRATION_TIME-t.CREATETIME)/(12*60*60) as day,t.CREATEUSERNAME as createuserName,t.PROCESS_STARTER_NAME as processStarterName ");
		buffer.append(" from t_oa_task_info t  left join T_OA_REMINDERS r ");
		buffer.append(" on t.id=r.taskinfoid where 1=1  ");
//		assignee like :account and tenantId=:tenantId
		buffer.append("and t.status='active' and t.stop_flg=0 and t.del_flg=0 ");
		buffer.append(" and t.assignee like '%"+account+"%'  and t.tenant_id = '"+ tenantId+"'" );
		if(StringUtils.isNoneBlank(startTime)&&StringUtils.isNoneBlank(endTime)){
			buffer.append("and t.createtime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("and t.createtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(title)){
			buffer.append("and t.attr2 like :title ");
		}
		buffer.append(" order by t.createTime desc )aa where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page+"" == null ? "1" : page+"");
		int count = Integer.parseInt(rows+"" == null ? "20" : rows+"");
		paraMap.put("page", start);
		paraMap.put("rows", count);
		if(StringUtils.isNoneBlank(title)){
			paraMap.put("title", "%"+title+"%");
		}
		
		List<OaTaskInfo> list = namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<OaTaskInfo>() {
			@Override
			public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaTaskInfo vo = new OaTaskInfo();
				vo.setId(rs.getString("id"));
				vo.setAttr2(rs.getString("attr2"));//事务标题
				vo.setCreateUser(rs.getString("createUser")); //申请人
				vo.setCreateTime(rs.getTimestamp("createTime")); //申请时间
				vo.setProcessStarter(rs.getString("processStarter")); //提交人
				vo.setCompleteTime(rs.getTimestamp("completeTime")); //提交时间
				vo.setName(rs.getString("name"));//当前环节 
				vo.setAttr4(rs.getString("day"));//办理时限
				vo.setAttr5(rs.getString("REMINDERNUM"));//催办次数
				vo.setCreateuserName(rs.getString("createuserName"));
				vo.setProcessStarterName(rs.getString("processStarterName"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
		
		
		
		
		/*StringBuilder hql = new StringBuilder();
		hql.append("from OaTaskInfo where assignee like :account and tenantId=:tenantId  ");
		if(StringUtils.isNoneBlank(startTime)&&StringUtils.isNoneBlank(endTime)){
			hql.append("and completeTime > to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
			hql.append("and completeTime <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		if(StringUtils.isNoneBlank(title)){
			hql.append("and attr2 like :title ");
		}
		hql.append(" and stop_flg=0 and del_flg=0 order by createTime desc ");
		
		Query query = this.getSession().createQuery(hql.toString())
				.setParameter("account",account).setParameter("tenantId",tenantId);
		if(StringUtils.isNoneBlank(startTime)&&StringUtils.isNoneBlank(endTime)){
			query = query.setParameter("startTime",startTime).setParameter("endTime",endTime);
		}
		if(StringUtils.isNoneBlank(title)){
			query = query.setParameter("title", "%"+title+"%");
		}
		List<OaTaskInfo> list = query.setMaxResults(rows).setFirstResult(start).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();*/
	}




		@Override
	public int getNumberForTask(String account, String tenantId, String startTime, String endTime, String title) {

			final StringBuffer buffer = new StringBuffer();
			buffer.append("select t.id,t.attr2,t.createuser,t.createtime,");
			buffer.append("t.process_starter as processStarter,t.complete_time as completeTime,t.name,");
			buffer.append("(t.EXPIRATION_TIME-t.CREATETIME)/(12*60*60) as day");
			buffer.append(" from t_oa_task_info t   left join T_OA_REMINDERS r  ");
			buffer.append(" on t.id=r.taskinfoid where 1=1  ");
//			assignee like :account and tenantId=:tenantId
			buffer.append("and t.status='active' and t.stop_flg=0 and t.del_flg=0 ");
			buffer.append(" and t.assignee like '%"+account+"%'  and t.tenant_id = '"+ tenantId+"'" );
			if(StringUtils.isNoneBlank(startTime)&&StringUtils.isNoneBlank(endTime)){
				buffer.append("and t.complete_time > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
				buffer.append("and t.complete_time <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
			}
			if(StringUtils.isNoneBlank(title)){
				buffer.append("and t.attr2 like :title ");
			}
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			if(StringUtils.isNoneBlank(title)){
				paraMap.put("title", "%"+title+"%");
			}
			
			List<OaTaskInfo> list = namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<OaTaskInfo>() {
				@Override
				public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
					OaTaskInfo vo = new OaTaskInfo();
//					vo.setId(rs.getString("id"));
//					vo.setAttr2(rs.getString("attr2"));//事务标题
//					vo.setCreateUser(rs.getString("createUser")); //申请人
//					vo.setCreateTime(rs.getTimestamp("createTime")); //申请时间
//					vo.setProcessStarter(rs.getString("processStarter")); //提交人
//					vo.setCompleteTime(rs.getTimestamp("completeTime")); //提交时间
//					vo.setName(rs.getString("name"));//当前环节 
//					vo.setAttr4(rs.getString("day"));//办理时限
//					vo.setAttr5(rs.getString("REMINDERNUM"));//催办次数
					return vo;
				}
			});
		if(list!=null && list.size()>0){
			return list.size();
		}
		return 0;
	}




	@Override
	public List<OaTaskInfo> querylishijili(String startTime, String endTime, String title, int page, int rows) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (select ROWNUM AS n,aa.* from (");
		buffer.append("select p.task_id as taskId,p.business_key as businessKey,p.process_instance_id as processInstanceId,p.id as id, ");
		buffer.append("p.attr2, p.name,p.createuser as createUser,p.createtime as createTime,p.CREATEUSERNAME as createuserName,p.last_modifier_name as processStarterName, ");
		buffer.append("p.PROCESS_STARTER processStarter,p.complete_time  as completeTime from t_oa_task_info p ");
		buffer.append("where p.last_modifier='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' ");
		buffer.append(" and p.catalog <> 'start' and p.status <> 'active' ");
		if(StringUtils.isNotBlank(title)){
			buffer.append("and p.attr2 like '%"+title+"%' ");
		}
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			buffer.append("and p.createtime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("and p.createtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		buffer.append(" order by p.complete_time desc )aa where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page+"" == null ? "1" : page+"");
		int count = Integer.parseInt(rows+"" == null ? "20" : rows+"");
		paraMap.put("page", start);
		paraMap.put("rows", count);
		//事务标题、申请人、申请时间、提交人、提交时间、办理环节、当前环节、办理时限
		List<OaTaskInfo> list = namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<OaTaskInfo>() {
			@Override
			public OaTaskInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaTaskInfo vo = new OaTaskInfo();
				vo.setTaskId(rs.getString("taskId"));
				vo.setBusinessKey(rs.getString("businessKey"));
				vo.setProcessInstanceId(rs.getString("processInstanceId"));
				vo.setId(rs.getString("id"));
				vo.setAttr2(rs.getString("attr2"));//事务标题
				vo.setName(rs.getString("name"));//当前环节 
				vo.setCreateUser(rs.getString("createUser")); //申请人
				vo.setCreateTime(rs.getTimestamp("createTime")); //申请时间
				vo.setProcessStarter(rs.getString("processStarter")); //提交人
				vo.setCompleteTime(rs.getTimestamp("completeTime")); //提交时间
				vo.setProcessStarterName(rs.getString("processStarterName"));
				vo.setCreateuserName(rs.getString("createuserName"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaTaskInfo>();
	}





	@Override
	public int querylishijiliNum(String startTime, String endTime,String title) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select count(1) from t_oa_task_info p ");
		buffer.append("where p.last_modifier='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"' ");
		buffer.append(" and p.catalog <> 'start' and p.status <> 'active' ");
		if(StringUtils.isNotBlank(title)){
			buffer.append("and p.attr2 like '%"+title+"%' ");
		}
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			buffer.append("and p.createtime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("and p.createtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		return jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
	}




	/**
	 * 业务催办
	 */
	@Override
	public List<OaReminders> queryList1(String user_account,String startTime, String endTime,
			String title, int page, int rows) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (select ROWNUM AS n,aa.* from (");
		buffer.append("select t.ID as id ,t.PROCEDUREID as procedureId,t.REMIDERETIME    as remidereTime,t.REMINDNODE as remindNode,");
		buffer.append("t.PORCEDURENAME as procedureName,t.REMINDENODENAME as remindenodeName,t.REMINDERNUM as reminderNum,");
		buffer.append("t.REMINDERNAME    as reminderName,t.REMINDEREDNAME as reminderdName,");
		buffer.append("t.REMINDTIME      as remindTime,t.REMINDRECONTENT as remindreContent,t.CREATEUSER as creatUser,");
		buffer.append("t.CREATETIME as createTime,t.REMINDRESTATUS as remindreStatus,t.REMINDCONTENT as remindcontent,T.TASKINFOID as taskInfoId from T_OA_REMINDERS t LEFT JOIN T_OA_TASK_INFO f on t.TASKINFOID=f.ID where T.DEL_FLG=0 and f.STATUS='active'");
		buffer.append("and t.REMINDERED like '%"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"%' and t.TASKINFOID is not null " );
		if(StringUtils.isNotBlank(title)){
			buffer.append("and t.PORCEDURENAME like '%"+title+"%' ");
		}
		if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
			buffer.append("and t.REMINDTIME > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("and t.REMINDTIME <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
		}
		buffer.append(" order by REMINDTIME desc)aa where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page+"" == null ? "1" : page+"");
		int count = Integer.parseInt(rows+"" == null ? "20" : rows+"");
		paraMap.put("page", start);
		paraMap.put("rows", count);
		
		List<OaReminders> list = namedParameterJdbcTemplate.query(buffer.toString(),paraMap,new RowMapper<OaReminders>() {
			@Override
			public OaReminders mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaReminders vo = new OaReminders();
				vo.setId(rs.getString("id"));
				vo.setProcedureId(rs.getString("procedureId"));//实例id
				vo.setRemindNode(rs.getString("remindNode"));//催办环节
				vo.setProcedureName(rs.getString("procedureName"));//催办标题
				vo.setRemindenodeName(rs.getString("remindenodeName"));//催办环节
				vo.setReminderName(rs.getString("reminderName"));//催办人
				vo.setRemidereTime(rs.getTimestamp("remidereTime"));//回复时间
				vo.setRemindreContent(rs.getString("remindreContent"));//回复内容
				vo.setRemindreStatus(rs.getInt("remindreStatus"));//已读未读
				vo.setRemindcontent(rs.getString("remindcontent"));
				vo.setTaskInfoId(rs.getString("taskInfoId"));//任务id
				return vo;
			}
		});
		/*List<OaReminders> list = (List<OaReminders>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("id").addScalar("procedureId")
						.addScalar("procedureName").addScalar("reminderNum",Hibernate.INTEGER)
						.addScalar("remindTime",Hibernate.TIMESTAMP).addScalar("reminderdName")
						.addScalar("remindreContent").addScalar("creatUser").addScalar("createTime",Hibernate.TIMESTAMP);
				return queryObject.setResultTransformer(Transformers.aliasToBean(OaReminders.class)).list();
			}
		});*/
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaReminders>();
	}

	@Override
	public int queryNum(String startTime, String endTime, String title) {
		int i = 0;
		try{
			final StringBuffer buffer = new StringBuffer();
			buffer.append("select count(1) from T_OA_REMINDERS t LEFT JOIN T_OA_TASK_INFO f on t.TASKINFOID=f.ID where T.DEL_FLG=0 and f.STATUS='active' ");
			buffer.append("and t.REMINDERED like '%"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"%' and t.TASKINFOID is not null ");
			if(StringUtils.isNotBlank(title)){
				buffer.append("and t.PORCEDURENAME like '%"+title+"%' ");
			}
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				buffer.append("and t.remidereTime > to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') ");
				buffer.append("and t.remidereTime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') ");
			}
			
			i = jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public void deleteMyGao(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update T_OA_KV_RECORD t set t.DEL_FLG = 1 where t.id = '"+id+"'");
		jdbcTemplate.update(sb.toString());
	}

	@Override
	public List<String> queryProcessDefinitionIdByProcessInstanceId(
			String processInstanceId) {
		String sql = "select t.process_definition_id from T_OA_TASK_INFO t where t.process_instance_id = " + processInstanceId;
		
		List<String> list = namedParameterJdbcTemplate.query(sql,new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String process_definition_id = rs.getString("process_definition_id");
				return process_definition_id;
			}
			
		});
		
		return list;
	}

	@Override
	public List<OaBpmCategory> getAllOaBpmCategory() {
		StringBuffer sb = new StringBuffer();
		sb.append(" from OaBpmCategory where stop_flg=0 and del_flg=0 order by priority");
		List<OaBpmCategory> list = this.find(sb.toString());
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaBpmCategory>();
	}

	@Override
	public List<OaActivitiDept> getAllOaActivitiDept() {
		StringBuffer sb = new StringBuffer();
		sb.append(" from OaActivitiDept where deptType = 0 ");
		List<OaActivitiDept> list = this.find(sb.toString());
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaActivitiDept>();
	}

	@Override
	public OaTaskInfo queryOaTaskInfo(String processInstanceId, String code) {
		StringBuilder hql = new StringBuilder();
		hql.append("from OaTaskInfo t where t.del_flg=0 and t.processInstanceId=:processInstanceId and t.code=:code and t.status='active' ");
		List<OaTaskInfo> iList=this.getSession().createQuery(hql.toString()).setParameter("processInstanceId",processInstanceId).setParameter("code",code).list();
		if(iList!=null&&iList.size()>0){
			return iList.get(0);
		}
		return null;
	}

	@Override
	public int queryRemindernum(String processInstanceId) {
		int i = 0;
		try{
			final StringBuffer buffer = new StringBuffer();
			buffer.append("select count(t.remindernum) from T_OA_REMINDERS t where 1=1 ");
			buffer.append("and t.procedureid ='"+processInstanceId+"'");
			i = jdbcTemplate.queryForObject(buffer.toString(), java.lang.Integer.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return i;
	}
}
