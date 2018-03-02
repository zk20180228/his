package cn.honry.oa.humantask.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfForm;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.humantask.dao.HumanTaskDao;

@Repository("humanTaskDao")
@SuppressWarnings({ "all" })
public class HumanTaskDaoImpl extends HibernateEntityDao<OaKVRecord> implements HumanTaskDao{
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public OaKVRecord queryOaKVRecordById(String id) {
		String hql = "from OaKVRecord t where t.id = '"+id+"'";
		List<OaKVRecord> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return new OaKVRecord();
	}

	@Override
	public OaBpmProcess queryOaBpmProcessById(String id) {
		String hql = "from OaBpmProcess t where t.id = '"+id+"'";
		List<OaBpmProcess> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return new OaBpmProcess();
	}
	
	@Override
	public OaFormInfo queryOaFormInfoById(String formCode) {
		String hql = "from OaFormInfo t where t.formCode = '"+formCode+"' and t.stop_flg=0 and t.del_flg=0";
		List<OaFormInfo> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return new OaFormInfo();
	}
	
	@Override
	public List<OaKVProp> queryOaKVPropById(String recordId) {
		String hql = "from OaKVProp t where t.recordId = '"+recordId+"'";
		List<OaKVProp> list=find(hql.toString(), null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<OaKVProp>();
	}

	@Override
	public List<OaBpmConfForm> queryOaBpmConfFormBybaseId(String baseId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT t.properties as properties from t_oa_bpm_conf_form t ");
		buffer.append(" LEFT JOIN t_oa_bpm_conf_node n on t.conf_node_code = n.id ");
		buffer.append(" LEFT JOIN t_oa_bpm_conf_base b on n.conf_base_code = b.id ");
		buffer.append(" WHERE b.id='"+baseId+"' and n.type='userTask' and t.stop_flg=0 and t.del_flg=0 ");
		buffer.append(" ORDER BY to_number(n.priority)");
		List<OaBpmConfForm> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaBpmConfForm>() {
			@Override
			public OaBpmConfForm mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaBpmConfForm vo = new OaBpmConfForm();
				vo.setProperties(rs.getString("properties"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaBpmConfForm>();
	}
	
	/**
	 * 查询上一个节点
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */
	public String findPreviousActivities(String processInstanceId,String taskId){
		if(StringUtils.isBlank(taskId)){
			return "";
		}
		String hql="select t.name from OaTaskInfo t where t.status<>'active' and t.processInstanceId=? and t.stop_flg=0 and t.del_flg=0 "
				+ "  order by t.createTime desc, to_number(taskId) desc";
		List<String> list = this.createQuery(hql, processInstanceId).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return "";
	}
	
	
	/**
	 * 获取所有已处理节点
	 * @param processInstanceId
	 * @param taskId
	 * @return
	 */
	public List<OaTaskInfo> findAllPreviousActivities(String processInstanceId,String taskId){
		if(StringUtils.isBlank(taskId)){
			return null;
		}
		String hql="select t.id as id,t.code as code,t.name as name,t.completeStatus as completeStatus,t.attr3 as attr3"
				+ " from OaTaskInfo t where t.status='complete' and t.processInstanceId=? and t.stop_flg=0 and t.del_flg=0"
				+ "  order by t.createTime desc, to_number(t.taskId) desc";
		List<OaTaskInfo> list = this.createQuery(hql, processInstanceId)
				.setResultTransformer(Transformers.aliasToBean(OaTaskInfo.class)).list();
		return list;
	}
	
	/**
	 * 获取已处理节点code和name(去重)
	 * @param processInstanceId 流程实例id
	 * @return
	 */
	public List<OaTaskInfo> findAllPreviousActivitieCode(String processInstanceId){
		if(StringUtils.isBlank(processInstanceId)){
			return null;
		}
		String hql="select distinct t.code as code,t.name as name"
				+ " from OaTaskInfo t where t.status='complete' and t.processInstanceId=? and t.stop_flg=0 and t.del_flg=0";
		List<OaTaskInfo> list = this.createQuery(hql, processInstanceId)
				.setResultTransformer(Transformers.aliasToBean(OaTaskInfo.class)).list();
		return list;
	}
	
	/**
	 * 根据节点id获取任务节点信息(有可能有多个,因为有驳回)
	 * @param activityId
	 * @return
	 */
	public List<OaTaskInfo> findActivitiesByActId(String processInstanceId,String activityId){
		String hql = "select t.code as code,t.lastModifier as lastModifier , t.attr2 as attr2 from OaTaskInfo t "
				+ " where t.status='complete' and t.processInstanceId=? and t.code=? and t.stop_flg=0 and t.del_flg=0"
				+ " order by t.createTime desc";
		List<OaTaskInfo> list = this.createQuery(hql,processInstanceId,activityId)
				.setResultTransformer(Transformers.aliasToBean(OaTaskInfo.class)).list();
		
		return list;
	}
	
	public String getEmpExtendName(String jobNo){
		String hql = "select employeeName from EmployeeExtend where employeeJobNo=?";
		List list = this.createQuery(hql, jobNo).list();
		if(list!=null && list.size()>0){
			return list.get(0).toString();
		}
		return "";
	}

	@Override
	public String getAssigneeName(String jobNo) {
		String hql = "select employeeName from EmployeeExtend where employeeJobNo in ('"+jobNo.replaceAll(",", "','")+"')";
		List list = this.createQuery(hql).list();
		String assigneeName = "";
		if(list!=null && list.size()>0){
			for(Object s : list){
				if(StringUtils.isBlank(assigneeName)){
					assigneeName = s.toString();
					continue;
				}
				assigneeName += ","+s.toString();
			}
			return assigneeName;
		}
		return "";
	}
	
	/**
	 * 根据流程实例获取已完成的任务
	 * 根据创建时间和任务id正序排列
	 * @param processInstanceId
	 * @return
	 */
	public List<OaTaskInfo> findCompleteActs(String processInstanceId){
		String hql ="select t.taskId as taskId,t.code as code,t.name as name from OaTaskInfo t "
				+ " where t.status<>'rollback' and t.processInstanceId=? and t.stop_flg=0 and t.del_flg=0 "
				+ " order by t.createTime,to_number(t.taskId)";
		List<OaTaskInfo> list = this.createQuery(hql, processInstanceId).setResultTransformer(Transformers.aliasToBean(OaTaskInfo.class)).list();
		return list;
	}
	
	/**
	  * 根据流程实例id删除当前活动的节点
	  * @param processInstanceId 流程实例id
	  */
	public void removeHumanTaskByProcessInstanceId(String processInstanceId){
		String hql="update OaTaskInfo t set status='withdraw',action='撤回' where t.status='delete' and t.processInstanceId=? ";
		this.createQuery(hql, processInstanceId).executeUpdate();
	}
	
	/**
	 * 根据流程任务id删除当前活动的节点
	 * @param taskId 任务id
	 */
	public void removeHumanTaskByTaskId(String taskId){
		String hql="update OaTaskInfo t set action='撤回' where t.status='active' and t.taskId=? ";
		this.createQuery(hql, taskId).executeUpdate();
	}
	
	/**
	 * 撤回流程只能由撤回人进行提交
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2018年1月22日 下午8:28:29 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2018年1月22日 下午8:28:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id:
	 * @throws:
	 * @return: 
	 *
	 */
	public void changeAssignee(String id, String assignee, String assigneeName){
		String hql="update OaTaskInfo t set assignee='"+assignee+"' , assigneeName='"+assigneeName+"' where t.id=?";
		this.createQuery(hql, id).executeUpdate();
	}
}
