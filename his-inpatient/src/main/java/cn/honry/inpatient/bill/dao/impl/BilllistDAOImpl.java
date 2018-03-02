package cn.honry.inpatient.bill.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.utli.DataRightUtils;
import cn.honry.inpatient.bill.dao.BilllistDAO;
import cn.honry.utils.ShiroSessionUtils;
@Repository("billlistDAO")
@SuppressWarnings("all")
public class BilllistDAOImpl extends HibernateEntityDao<DrugBilllist> implements BilllistDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<DrugBilllist> getPage(String page, String rows,
			DrugBilllist billlistSerc) {
		String hql = "from DrugBilllist db where db.del_flg = 0 and db.stop_flg = 0 AND "+DataRightUtils.connectHQLSentence("db")+"";
		if(billlistSerc != null){
			if(billlistSerc.getDrugBillclass()!=null){
				if(StringUtils.isNotBlank(billlistSerc.getDrugBillclass().getId())){
					hql += "and db.drugBillclass.id = '" + billlistSerc.getDrugBillclass().getId() + "'";
				}
			}
		}
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(DrugBilllist billlistSerc) {
		String hql = "from DrugBilllist db where db.del_flg = 0 and db.stop_flg = 0 AND "+DataRightUtils.connectHQLSentence("db")+"";
		if(billlistSerc != null){
			if(billlistSerc.getDrugBillclass()!=null){
				if(StringUtils.isNotBlank(billlistSerc.getDrugBillclass().getId())){
					hql += "and db.drugBillclass.id = '" + billlistSerc.getDrugBillclass().getId() + "'";
				}
			}
		}
		return super.getTotal(hql);
	}

	@Override
	public void deleteBypid(String id) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		id=id.replaceAll(",", "','");
		this.getSession().createQuery("UPDATE DrugBilllist d SET del_flg = 1 ,deleteUser = ? ,deleteTime = ?  WHERE d.drugBillclass.id IN ('"+id+"')AND "+DataRightUtils.connectHQLSentence("d")+"")
			.setParameter(0, user.getAccount())
			.setTimestamp(1, new Date())
			.executeUpdate();
	}

	@Override
	public void saveStatesObject(Object object) {
		StatelessSession session=getHibernateTemplate().getSessionFactory().openStatelessSession();
		session.insert(object);
	}
	
}
