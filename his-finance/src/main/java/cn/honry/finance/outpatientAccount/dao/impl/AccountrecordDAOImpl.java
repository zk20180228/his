package cn.honry.finance.outpatientAccount.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.outpatientAccount.dao.AccountrecordDAO;

/**
 * 患者账户操作流水表
 * @author  wangfujun
 * @date 创建时间：2016年3月30日 下午4:50:36
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Repository("accountrecordDAO")
@SuppressWarnings({"all"})
public class AccountrecordDAOImpl extends HibernateEntityDao<OutpatientAccountrecord> implements AccountrecordDAO {
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};

	@Override
	public List<OutpatientAccountrecord> queryDatailed(String accountID,String menuAlias,String page,String rows) {
		StringBuffer sBuffer =jointHQL(accountID,menuAlias);
		Query query = this.getSession().createQuery(sBuffer.toString())
				.setParameter(0, accountID)
				.setParameter(1, 0);
		int start = Integer.parseInt(page==null?"1":page);
		int count = Integer.parseInt(rows==null?"20":rows);
		
		List<OutpatientAccountrecord> list = query.setFirstResult((start-1)*count).setMaxResults(count).list();
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<OutpatientAccountrecord>();
	}
	
	/***hql 拼接**/
	public StringBuffer jointHQL(String accountID,String menuAlias){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientAccountrecord t where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.accountId = ?");
		sBuffer.append(" and t.valid = ?");
		sBuffer.append(" order by t.updateTime desc");
		return sBuffer;
	}

	@Override
	public int getTotal(String accountID, String menuAlias) {
		StringBuffer sBuffer =jointHQL(accountID,menuAlias);
		Query query = this.getSession().createQuery(sBuffer.toString())
				.setParameter(0, accountID)
				.setParameter(1, 0);
		List<OutpatientAccountrecord> list = query.list();
		if(list != null){
			return list.size();
		}
		return 0;
	}
}
