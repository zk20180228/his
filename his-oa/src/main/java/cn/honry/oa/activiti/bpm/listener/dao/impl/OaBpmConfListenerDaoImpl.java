package cn.honry.oa.activiti.bpm.listener.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OaBpmConfListener;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.activiti.bpm.listener.dao.OaBpmConfListenerDao;

/**
 * 监听器DAO实现类
 * @author user
 *
 */
@Repository("oaBpmConfListenerDao")
@SuppressWarnings({ "all" })
public class OaBpmConfListenerDaoImpl extends
		HibernateEntityDao<OaBpmConfListener> implements OaBpmConfListenerDao {

	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 根据值、类型和所属节点id获取监听器
	 * @param value 值
	 * @param type 类型
	 * @param nodeCode 所属节点id
	 * @return
	 */
	public OaBpmConfListener findUnique(String value,int type,String nodeCode){
		String hql="from BpmConfListener where value=? and type=? and status=0 and nodeCode=?";
		List<OaBpmConfListener> list = this.createQuery(hql, value,type,nodeCode).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
