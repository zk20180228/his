package cn.honry.oa.kv.record.dao.impl;

import java.sql.SQLException;
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
@Repository("recordDao")
@SuppressWarnings({"all"})
public class RecordDaoImpl extends HibernateEntityDao<OaKVRecord> implements RecordDao{
	 // 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
