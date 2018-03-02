package cn.honry.finance.outpatientAccount.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.outpatientAccount.dao.OutAccountDAO;

/**
 * 门诊患者账户管理(预交金Dao)
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午1:59:09
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Repository("outAccountDAO")
@SuppressWarnings({"all"})
public class OutAccountDAOImpl extends HibernateEntityDao<OutpatientOutprepay> implements OutAccountDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
	@Override
	public OutpatientOutprepay get(String arg0) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientOutprepay t");
		sBuffer.append(" where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.id = ?");
		List<OutpatientOutprepay> list = super.find(sBuffer.toString(), arg0);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public PatientIdcard getForidcardNo(String idcardNo,String menuAlias) {
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append("from PatientIdcard t where t.stop_flg = 0 and t.del_flg = 0");
		sbBuffer.append(" and t.idcardNo = ?");
		sbBuffer.append(" order by t.updateTime desc");
		List<PatientIdcard> list = super.find(sbBuffer.toString(), idcardNo);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public OutpatientAccount getForidcardid(String menuAlias, String idcardid) {
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(" from OutpatientAccount t where t.stop_flg = 0 and t.del_flg = 0");
		sbBuffer.append(" and t.idcardId = ?");
		List<OutpatientAccount> list=super.find(sbBuffer.toString(),idcardid);
		if(list != null && list.size() > 0){
			return list.get(0);		
		}
		return null;
	}

	@Override
	public List<OutpatientOutprepay> queryPrestore(String accountID,String ishistory, String menuAlias,String page,String rows) {
		StringBuffer sBuffer = jointHQL(menuAlias);
		//不分页
		if(StringUtils.isEmpty(page)){
			List<OutpatientOutprepay> list=super.find(sBuffer.toString(),accountID, Integer.valueOf(ishistory));
			if(list != null && list.size() > 0){
				return list;
			}
			return new ArrayList<OutpatientOutprepay>();
		}else{
			int start = Integer.parseInt(page);
			int count = Integer.parseInt(rows);
			Query query = this.getSession().createQuery(sBuffer.toString())
					.setParameter(0, accountID)
					.setParameter(1, Integer.valueOf(ishistory))
					.setFirstResult((start-1)*count)
					.setMaxResults(count);
			List<OutpatientOutprepay> list = query.list();
			if(list != null && list.size() > 0){
				return list;
			}
			return new ArrayList<OutpatientOutprepay>();
		}
	}

	
	/***hql 拼接**/
	public StringBuffer jointHQL(String menuAlias){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from OutpatientOutprepay t where t.stop_flg = 0 and t.del_flg = 0");
		sBuffer.append(" and t.accountId = ?");
		sBuffer.append(" and t.ishistory = ?");
		sBuffer.append(" order by t.updateTime desc");
		return sBuffer;
	}

	@Override
	public int getTotal(String accountID, String ishistory, String menuAlias) {
		StringBuffer sBuffer = jointHQL(menuAlias);
		List<OutpatientOutprepay> list=super.find(sBuffer.toString(),accountID, Integer.valueOf(ishistory));
		if(list != null){
			return list.size();
		}
		return 0;
	}
	
	
}
