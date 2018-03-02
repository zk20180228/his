package cn.honry.oa.extend.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.process.dao.OaBpmProcessDao;
import cn.honry.oa.extend.dao.ExtendDao;
import cn.honry.oa.extend.vo.RecordVo;
import cn.honry.oa.kv.record.dao.RecordDao;
import cn.honry.utils.ShiroSessionUtils;
@Repository("extendDao")
@SuppressWarnings({"all"})
public class ExtendDaoImpl extends HibernateEntityDao<Schedule> implements ExtendDao{
	 // 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "oaBpmProcessDao")
	private OaBpmProcessDao oaBpmProcessDao;
	@Autowired
	@Qualifier(value = "recordDao")
	private RecordDao recordDao;
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<RecordVo> queryLeaveComplete(String type,final String topFlow) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT "); 
		buffer.append("r.NAME name, "); 
		buffer.append("r.FLOW_CODE code, "); 
		buffer.append("r.FLOW_STATE state, "); 
		buffer.append("p.START_TIME_ startTime, "); 
		buffer.append("p.END_TIME_ endTime "); 
		buffer.append("FROM ACT_HI_PROCINST p  "); 
		buffer.append("INNER JOIN T_OA_KV_RECORD r ON r.BUSINESS_KEY = p.BUSINESS_KEY_  "); 
		buffer.append("WHERE p.END_TIME_ IS NOT NULL  "); 
		buffer.append("AND r.STATUS = 0 "); 
		buffer.append("AND p.START_USER_ID_ = '").append(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount()).append("' ");
		buffer.append("AND r.CATEGORY = ? ");
		if("0".equals(type)){
			buffer.append("AND r.FLOW_STATE = 0 ");//已完成未销假
		}else if("1".equals(type)){
			buffer.append("AND r.FLOW_STATE = 1 ");//销假中
		}else if("2".equals(type)){
			buffer.append("AND r.FLOW_STATE = 2 ");//已销假
		}else{
			buffer.append("AND r.FLOW_STATE IN (0,1,2) ");//已完成未销假&已销假
		}
		List<RecordVo> voList = (List<RecordVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("name")
						.addScalar("code")
						.addScalar("state",Hibernate.INTEGER)
						.addScalar("startTime",Hibernate.TIMESTAMP)
						.addScalar("endTime",Hibernate.TIMESTAMP);
				return queryObject.setString(0, topFlow).setResultTransformer(Transformers.aliasToBean(RecordVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<RecordVo>();
	}

	@Override
	public OaBpmProcess getProcessbyBusinessKey(String id) {
		String hql = "from OaBpmProcess p where p.id = (select r.category from OaKVRecord r where r.businessKey = ?)";
		OaBpmProcess process = (OaBpmProcess) oaBpmProcessDao.excHqlGetUniqueness(hql, id);
		if(process!=null&&StringUtils.isNotBlank(process.getId())){
			return process;
		}
		return null;
	}

	@Override
	public OaKVRecord getRecordByBusinessKeyAndFlowCode(String topFlow,String leaveCode) {
		String hql = "from OaKVRecord r WHERE r.category = ? AND r.flowCode = ?";
		OaKVRecord record = (OaKVRecord) recordDao.excHqlGetUniqueness(hql,topFlow,leaveCode);
		if(record!=null&&StringUtils.isNotBlank(record.getId())){
			return record;
		}
		return null;
	}

	@Override
	public void saveRecord(OaKVRecord record) {
		recordDao.save(record);
	}

	@Override
	public OaKVRecord getRecordByBusinessKey(String businessKey) {
		String hql = "from OaKVRecord r WHERE r.businessKey = ?";
		OaKVRecord record = (OaKVRecord) recordDao.excHqlGetUniqueness(hql,businessKey);
		if(record!=null&&StringUtils.isNotBlank(record.getId())){
			return record;
		}
		return null;
	}

	@Override
	public OaBpmProcess getProcessbyId(String id) {
		String hql = "from OaBpmProcess p where p.id = (select r.category from OaKVRecord r where r.id = ?)";
		OaBpmProcess process = (OaBpmProcess) oaBpmProcessDao.excHqlGetUniqueness(hql, id);
		if(process!=null&&StringUtils.isNotBlank(process.getId())){
			return process;
		}
		return null;
	}
	
}
