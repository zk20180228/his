package cn.honry.oa.activiti.bpm.process.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfBase;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.process.dao.OaBpmProcessDao;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;

/**
 * 流程配置DAO实现类
 * @author luyanshou
 *
 */
@Repository("oaBpmProcessDao")
@SuppressWarnings({ "all" })
public class OaBpmProcessDaoImpl extends HibernateEntityDao<OaBpmProcess>
		implements OaBpmProcessDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**
	 * 根据租户id获取流程配置分页数据
	 * @param tenantId 租户id
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<OaBpmProcess> getListByPage(String name ,String tenantId,int firstResult,int maxResults){
		StringBuffer sbf = new StringBuffer("from OaBpmProcess t where t.del_flg=0 ");
		Query query=null;
		if(StringUtils.isNotBlank(tenantId)){
			sbf.append(" and t.tenantId=? ");
			if(StringUtils.isNotBlank(name)){
				sbf.append(" and t.name like ? ");
				query = this.createQuery(sbf.toString(), tenantId,"%"+name+"%");
			}else{
				query = this.createQuery(sbf.toString(), tenantId);
			}
		}else{
			if (StringUtils.isNotBlank(name)) {
				sbf.append(" and t.name like ? ");
				query = this.createQuery(sbf.toString(),"%"+name+"%");
			}else{
				query = this.createQuery(sbf.toString());
			}
		}
		
		if(firstResult>=0 && maxResults>0){
			query = query.setFirstResult(firstResult).setMaxResults(maxResults);
		}
		List<OaBpmProcess> list = query.list();
		return list;
	}
	
	/**
	 * 根据租户id获取流程配置总数
	 * @param tenantId 租户id
	 */
	public int getTotal(String name,String tenantId){
		String hql="select count(t.id) from OaBpmProcess t where t.tenantId=? and t.stop_flg=0 and t.del_flg=0";
		
		String string =null;
		if(StringUtils.isNotBlank(name)){
			hql+=" and t.name like ? ";
			string=this.createQuery(hql, tenantId,"%"+name+"%").uniqueResult().toString();
		}else{
			string=this.createQuery(hql, tenantId).uniqueResult().toString();
		}
		if(StringUtils.isNotBlank(string)){
			return Integer.parseInt(string);
		}
		return 0;
	}
	
	/**
	 * 获取流程定义信息列表及所属分类
	 * @return
	 */
	public List<OaProcessVo> getCategoryInfo(int page, int rows,String param,String category,String treeId){
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from ( " );
		sql.append(" select rownum as n , aa.* from ( " );
		sql.append(" select t.id as id,t.name as name,t.descn as descn, ");
		sql.append(" t.CATEGORY_CODE as categoryCode,c.name as categoryName, ");
		sql.append(" t.top_Flow as topFlow ");
		sql.append(" from t_oa_bpm_process t ");
		sql.append(" left join t_oa_bpm_category c on t.CATEGORY_CODE=c.id ");
		sql.append(" where t.stop_flg=0 and t.del_flg=0 ");
		if(StringUtils.isNotBlank(param)){
			sql.append(" and t.name like '%"+param+"%' ");
		}
		if(StringUtils.isNotBlank(category)){
			sql.append(" and c.name like '%"+category+"%'");
		}
		if(StringUtils.isNotBlank(treeId)&&!"root".equals(treeId)&&!"1".equals(treeId)&&!"2".equals(treeId)){
			sql.append(" and (t.DEPT_CODE in ('"+treeId+"') or t.CATEGORY_CODE in ('"+treeId+"')) ");
		}
		sql.append(" order by c.PRIORITY ");
		sql.append(" ) aa ");
		sql.append(" where rownum <= :page * :rows) ");
		sql.append(" where n > (:page - 1) * :rows ");
		System.out.println(sql.toString());
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt((page+"" == null||page == 0) ? "1" : page+"");
		int count = Integer.parseInt((rows+"" == null||rows == 0) ? "20" : rows+"");
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<OaProcessVo> list = namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<OaProcessVo>() {

			@Override
			public OaProcessVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				OaProcessVo vo = new OaProcessVo();
				vo.setCategoryCode(rs.getString("categoryCode"));
				vo.setCategoryName(rs.getString("categoryName"));
				vo.setDescn(rs.getString("descn"));
				vo.setId(rs.getString("id"));
				vo.setName(rs.getString("name"));
				vo.setTopFlow(rs.getString("topFlow"));
				return vo;
			}
			
		});
//		List<OaProcessVo> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
//		.addScalar("id").addScalar("name").addScalar("descn").addScalar("categoryCode").addScalar("categoryName")
//		.setResultTransformer(Transformers.aliasToBean(OaProcessVo.class)).list();
		return list;
	}
	/**
	 * 获取流程定义信息列表及所属分类
	 * @return
	 */
	public List<OaProcessVo> getCategoryInfo(){
		StringBuffer sql=new StringBuffer("select t.id as id,t.name as name,t.descn as descn,");
		sql.append(" t.CATEGORY_CODE as categoryCode,c.name as categoryName from t_oa_bpm_process t ");
		sql.append(" left join t_oa_bpm_category c on t.CATEGORY_CODE=c.id ");
		sql.append(" where t.stop_flg=0 and t.del_flg=0 order by c.name");
		List<OaProcessVo> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
				.addScalar("id").addScalar("name").addScalar("descn").addScalar("categoryCode").addScalar("categoryName")
				.setResultTransformer(Transformers.aliasToBean(OaProcessVo.class)).list();
		return list;
	}
	public Integer getCategoryInfo(String param,String category,String treeId){
		StringBuffer sql=new StringBuffer("select count(1) from t_oa_bpm_process t ");
		sql.append(" left join t_oa_bpm_category c on t.CATEGORY_CODE=c.id ");
		sql.append(" where t.stop_flg=0 and t.del_flg=0 ");
		if(StringUtils.isNotBlank(param)){
			sql.append(" and t.name like '%"+param+"%' ");
		}
		if(StringUtils.isNotBlank(category)){
			sql.append(" and c.name like '%"+category+"%'");
		}
		if(StringUtils.isNotBlank(treeId)&&!"root".equals(treeId)&&!"1".equals(treeId)&&!"2".equals(treeId)){
			sql.append(" and (t.DEPT_CODE in ('"+treeId+"') or t.CATEGORY_CODE in ('"+treeId+"')) ");
		}
//		sql.append(" order by c.name");
//		List<OaProcessVo> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
//				.addScalar("id").addScalar("name").addScalar("descn").addScalar("categoryCode").addScalar("categoryName")
//				.setResultTransformer(Transformers.aliasToBean(OaProcessVo.class)).list();
		BigDecimal re = (BigDecimal)this.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).uniqueResult();
		return re.intValue();
	}
	
	/**
	 * 根据流程定义信息id获取配置信息
	 * @param processId
	 * @return
	 */
	public OaBpmConfBase getConfBase(String processId){
		StringBuffer sql=new StringBuffer("select b.id as id,b.code as code,b.PROCESS_DEFINITION_ID as processDefinitionId,");
		sql.append(" b.PROCESS_DEFINITION_KEY as processDefinitionKey,b.PROCESS_DEFINITION_VERSION as processDefinitionVersion ");
		sql.append(" from t_oa_bpm_process t left join t_oa_bpm_conf_base b on b.id=t.conf_base_code ");
		sql.append(" where t.stop_flg=0 and t.del_flg=0 and t.id= ?");
		List<OaBpmConfBase> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
		.addScalar("id").addScalar("code").addScalar("processDefinitionId")
		.addScalar("processDefinitionKey").addScalar("processDefinitionVersion",Hibernate.INTEGER)
		.setParameter(0, processId).setResultTransformer(Transformers.aliasToBean(OaBpmConfBase.class)).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据流程定义id获取流程定义信息
	 * @param processDefinitionId
	 * @return
	 */
	public OaBpmProcess getProcessInfo(String processDefinitionId){
		StringBuffer sbf = new StringBuffer("select t.name,t.action from t_oa_bpm_process t ");
		sbf.append(" left join t_oa_bpm_conf_base b on t.conf_base_code=b.id");
		sbf.append(" where b.process_definition_id=? and t.stop_flg=0 and t.del_flg=0");
		List<OaBpmProcess> list = this.getSessionFactory().getCurrentSession().createSQLQuery(sbf.toString())
		.addScalar("name").addScalar("action").setParameter(0, processDefinitionId).setResultTransformer(Transformers.aliasToBean(OaBpmProcess.class)).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return new OaBpmProcess();
	}
}
