package cn.honry.inner.system.clearStoTerminal.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.clearStoTerminal.dao.ClearStoTerminalDAO;

/** <p>门诊终端定时任务接口</p>
* @ClassName: StoTerminalDAOImpl 
* @author dtl
* @date 2017年3月20日
*  
*/
@Repository("clearStoTerminalDAO")
@SuppressWarnings({ "all" })
public class ClearStoTerminalDAOImpl extends HibernateEntityDao<StoTerminal> implements ClearStoTerminalDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void clearStoTerminal() {
		StringBuffer sql = new StringBuffer("UPDATE StoTerminal t SET t.sendQty = 0,t.drugQty = 0,t.averageNum = 0 WHERE t.type = 1");
		super.excUpdateHql(sql.toString());
	}

	
}
