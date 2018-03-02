package cn.honry.inner.system.pretreatment.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.HandingStatus;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.TimingRules;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.pretreatment.dao.PretreatmentDao;
import cn.honry.utils.DateUtils;

/**
 * 预处理DAO实现类
 * @author user
 *
 */
@Repository("pretreatmentDao")
@SuppressWarnings({ "all" })
public class PretreatmentDaoImpl extends HibernateEntityDao<TimingRules>
		implements PretreatmentDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 获取有效的、开启状态的预处理定时规则
	 * @return 
	 */
	public List<TimingRules> getRulesList(){
		String hql="select t.menuAlias as menuAlias,t.type as type,t.rules as rules from TimingRules t "
				+  "where t.state=1 and t.stop_flg=0 and t.del_flg=0 ";
		List<TimingRules> list = this.createQuery(hql)
				.setResultTransformer(Transformers.aliasToBean(TimingRules.class)).list();
		return list;
	}
	
	/**
	 * 根据栏目别名、类型、开始时间获取相应的处理状态数据
	 * @param menuAlias 栏目别名
	 * @param type 类型(3年，2月，1日；)
	 * @param date 开始时间
	 * @return
	 */
	public List<HandingStatus> getHandingStatus(String menuAlias,String type,String date){
		StringBuffer hql=new StringBuffer(" from HandingStatus t where 1=1 ");
		if(StringUtils.isNotBlank(menuAlias)){
			hql.append(" and t.menuAlias='"+menuAlias+"'");
		}
		if(StringUtils.isNotBlank(type)){
			hql.append(" and t.type='"+type+"'");
		}
		if(StringUtils.isNotBlank(date)){
			hql.append(" and t.begainTime='"+date+"'");
		}
		List<HandingStatus> list = createQuery(hql.toString()).list();
		return list;
	}
	
	/**
	 * 根据栏目别名、类型获取处理失败的日志
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @return
	 */
	public List<MongoLog> getMongoLog(String menuAlias,String type){
		StringBuffer hql=new StringBuffer("from MongoLog t where t.state=0 ");
		if(StringUtils.isNotBlank(menuAlias) && StringUtils.isNotBlank(type)){
			if("1".equals(type)){//按天执行
				menuAlias+="_DAY";
			}
			if("2".equals(type)){//按月执行
				menuAlias+="_MONTH";
			}
			if("3".equals(type)){//按年执行
				menuAlias+="_YEAR";
			}
			hql.append(" and t.menuType='"+menuAlias+"'");
		}
		List<MongoLog> list = createQuery(hql.toString()).list();
		return list;
	}
	
	/**
	 * 删除三天前的执行成功的日志
	 */
	public void delMongoLog(){
		Date date = DateUtils.addDay(new Date(), -3);
		String d = DateUtils.formatDateY_M_D(date);
		StringBuffer sbf=new StringBuffer(" delete from MongoLog t where t.state=1 and t.startTime<to_date('");
		sbf.append(d).append("','yyyy-mm-dd')");
		int i = createQuery(sbf.toString()).executeUpdate();
	}
}
