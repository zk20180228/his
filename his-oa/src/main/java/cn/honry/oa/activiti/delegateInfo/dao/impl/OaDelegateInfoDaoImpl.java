package cn.honry.oa.activiti.delegateInfo.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.OaBpmConfNode;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaDelegateInfo;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.delegateInfo.dao.OaDelegateInfoDao;
import cn.honry.oa.activiti.delegateInfo.vo.DelegateInfoVo;
import cn.honry.utils.ShiroSessionUtils;

/**
 * 代理配置DAO实现类
 * @author luyanshou
 *
 */
@Repository("oaDelegateInfoDao")
@SuppressWarnings({ "all" })
public class OaDelegateInfoDaoImpl extends HibernateEntityDao<OaDelegateInfo> implements OaDelegateInfoDao{

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**
	 * 获取代理配置
	 * @param assignee
	 * @param processDefinitionId
	 * @param activityId
	 * @param tenantId
	 * @return
	 */
	public List<OaDelegateInfo> getInfo(String assignee,String processDefinitionId,String activityId,String tenantId){
		StringBuffer sbf = new StringBuffer("select t.assignee as assignee,t.attorney as attorney,t.startTime as startTime,");
		sbf.append(" t.endTime as endTime from OaDelegateInfo t ");
		sbf.append(" where t.stop_flg=0 and del_flg=0 and t.processDefinitionId=? and tenantId=? and taskDefinitionKey=?");
		sbf.append(" and assignee in ? ");
		String[] split = assignee.split(",");
		List<String> assList = Arrays.asList(split);
		List<OaDelegateInfo> list = this.createQuery(sbf.toString(),processDefinitionId,tenantId,activityId,assList)
		.setResultTransformer(Transformers.aliasToBean(OaDelegateInfo.class)).list();
		return list;
	}

	@Override
	public List<OaDelegateInfo> queryMyDelegate(int page, int rows) {
		final StringBuffer buffer = new StringBuffer(); 
		buffer.append(" SELECT * FROM (SELECT row_.*, ROWNUM rownum_ FROM ( ");
		buffer.append("select t.ID as id ,t.ASSIGNEE as assignee,t.ATTORNEY as attorney,t.START_TIME as startTime,t.TENANT_ID as tenantId, "
				+ " t.END_TIME as endTime,t.PROCESS_DEFINITION_ID as processDefinitionId,t.TASK_DEFINITION_KEY AS taskDefinitionKey,t.status as status, "
				+ " t.PROCESS_NAME as processName,t.ACTIVITY_NAME as activityName,t.ASSIGNEE_NAME as assingeName,t.ATTORNEY_NAME as attorneyName from T_OA_DELEGATE_INFO t  "
				+ " where t.assignee='"+ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()+"'"
				+ " and t.DEL_FLG = 0 and t.STOP_FLG = 0 ");
		
//		buffer.append(" AND ROWNUM <="+(page*rows)+" order by t.start_time_ desc ) WHERE RN >"+(page-1)*rows);
		buffer.append(" order by t.createTime desc) row_ WHERE ROWNUM <= "+(page*rows)+") WHERE rownum_ > "+(page-1)*rows);
		List<OaDelegateInfo> list = jdbcTemplate.query(buffer.toString(),new RowMapper<OaDelegateInfo>() {
			@Override
			public OaDelegateInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
				OaDelegateInfo vo = new OaDelegateInfo();
				vo.setId(rs.getString("id"));
				vo.setAssignee(rs.getString("assignee"));
				vo.setAttorney(rs.getString("attorney")); 
				vo.setStartTime(rs.getTimestamp("startTime")); //开始时间
				vo.setEndTime(rs.getTimestamp("endTime")); //开始时间
				vo.setProcessDefinitionId(rs.getString("processDefinitionId")); 
				vo.setTaskDefinitionKey(rs.getString("taskDefinitionKey"));
				vo.setStatus(rs.getInt("status")); 
				vo.setActivityName(rs.getString("activityName")); 
				vo.setProcessName(rs.getString("processName")); 
				vo.setAssingeName(rs.getString("assingeName")); 
				vo.setAttorneyName(rs.getString("attorneyName")); 
				vo.setTenantId(rs.getString("tenantId")); 
				return vo;
			}
		});
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<OaDelegateInfo>();
	}

	@Override
	public int queryMyDelegateTotal() {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		int i = 0;
		try{
			String sql = "select count(1) from t_oa_delegate_info m where m.ASSIGNEE = '"+account+"' and m.stop_flg = 0 and m.del_flg = 0 ";
			i = jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<DelegateInfoVo> queryProcess() {
		String sql = "select t.name as processName,b.process_definition_id as processDefinitionId from t_oa_bpm_process t left join t_oa_bpm_conf_base b on t.conf_base_code=b.id";
		SQLQuery query=getSession().createSQLQuery(sql)
				.addScalar("processName").addScalar("processDefinitionId");
		List<DelegateInfoVo> list=query.setResultTransformer(Transformers.aliasToBean(DelegateInfoVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<DelegateInfoVo>();
	}

	@Override
	public List<OaBpmConfNode> queryOaBpmConfNode(String proDefId) {
		String sql = "select t.code as code ,t.name as name from t_oa_bpm_conf_node t left join t_oa_bpm_conf_base b on t.conf_base_code=b.id where t.type='userTask' and b.process_definition_id=? order by to_number(t.priority)";
		SQLQuery query=getSession().createSQLQuery(sql)
				.addScalar("code").addScalar("name");
		List<OaBpmConfNode> list=query.setParameter(0, proDefId).setResultTransformer(Transformers.aliasToBean(OaBpmConfNode.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<OaBpmConfNode>();
	}

	@Override
	public void addOaDelegateInfo(OaDelegateInfo oadeInfo) {
		getHibernateTemplate().saveOrUpdate(oadeInfo);
	}

	@Override
	public void delMydelegateInfo(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update T_OA_DELEGATE_INFO t set t.DEL_FLG = 1 where t.id =?");
		jdbcTemplate.update(sb.toString(),id);
	}

	@Override
	public OaDelegateInfo findMydelegateInfo(String id) {
		String hql="from OaDelegateInfo t where t.id = ?";
		List<OaDelegateInfo> list = super.find(hql, id);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new OaDelegateInfo();
		
	}
	
}
