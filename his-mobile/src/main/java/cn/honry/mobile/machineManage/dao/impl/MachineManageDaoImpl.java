package cn.honry.mobile.machineManage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MMachineManage;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.machineManage.dao.MachineManageDao;

@Repository("machineManageDao")
@SuppressWarnings({ "all" })
public class MachineManageDaoImpl extends HibernateEntityDao<MMachineManage> implements MachineManageDao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Integer getTotal(MMachineManage machineManage) throws Exception {
		StringBuffer hql = new StringBuffer("from MMachineManage t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(machineManage, hql);
		Integer total = super.getTotal(hql.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<MMachineManage> getList(String page, String rows,
			MMachineManage machineManage) throws Exception {
		StringBuffer hql = new StringBuffer("from MMachineManage t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(machineManage, hql);
		List<MMachineManage> list = super.getPage(hql.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MMachineManage>();
	}

	@Override
	public List<MMachineManage> getMachinesByUserAccunt(String userAccunt)
			throws Exception {
		String hql = "from MMachineManage where user_account = ? and stop_flg = 0 and del_flg = 0";
		List<MMachineManage> list = super.find(hql, userAccunt);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MMachineManage>();
	}


	@Override
	public void delMacByAccount(MMachineManage mac) {
		StringBuffer hql = new StringBuffer("update MMachineManage set del_flg = 1,deleteUser = ?,deleteTime = ? "); 
		hql.append("where user_account = ? and  machine_code =? and is_white = ? and is_black = ? ");
		hql.append("and stop_flg = 0 and del_flg = 0 ");
		super.excUpdateHql(hql.toString(), mac.getDeleteUser(),mac.getDeleteTime(),mac.getUser_account(),mac.getMachine_code(),mac.getIs_white(),mac.getIs_black());
	}

	@Override
	public void updateWOrB(MMachineManage machineManage) {
		StringBuffer hql = new StringBuffer("update MMachineManage set "); 
		hql.append("is_white=?,is_black=?,updateUser=?,updateTime= ? ");
		hql.append("where user_account = ? and machine_code =? and is_white = ? and is_black = ?");
		hql.append("and stop_flg = 0 and del_flg = 0 ");
		super.excUpdateHql(hql.toString(),machineManage.getIs_black(), machineManage.getIs_white(),machineManage.getUpdateUser(),
				machineManage.getUpdateTime(),machineManage.getUser_account(),machineManage.getMachine_code(),machineManage.getIs_white(),machineManage.getIs_black());
		
	}

	private StringBuffer getHql(MMachineManage machineManage, StringBuffer hql) {
		if (machineManage.getUser_account() != null) {
			hql.append(" and (t.user_account like '%");
			hql.append(machineManage.getUser_account());
			hql.append("%'");
			hql.append(" or t.machine_code like '%");
			hql.append("%'");
			hql.append(machineManage.getUser_account());
			hql.append("%'");
			hql.append(" or t.machine_mobile like '%");
			hql.append(machineManage.getUser_account());
			hql.append("%')");
		}
		if (machineManage.getIs_lost() != null) {
			hql.append(" and t.is_lost = ");
			hql.append(machineManage.getIs_lost());
		}
		if (machineManage.getIs_white() != null) {
			hql.append(" and t.is_white = ");
			hql.append(machineManage.getIs_white());
		}
		if (machineManage.getIs_black() != null) {
			hql.append(" and t.is_black = ");
			hql.append(machineManage.getIs_black());
		}
		return hql;
	}

	@Override
	public MMachineManage getMachineByMachineCode(String machineCode) {
		StringBuffer hql = new StringBuffer("from MMachineManage t where t.stop_flg = 0 and t.del_flg = 0 and t.machine_code=:machineCode");
		Query query=this.getSession().createQuery(hql.toString());
		query.setParameter("machineCode", machineCode);
		List<MMachineManage> list=query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MMachineManage> getMachineByMobileNum(List<String> mobileNum) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select MACHINE_ID as id,MACHINE_CODE as machine_code,USER_ACCOUNT as user_account,mobiles from(select t.MACHINE_ID, t.MACHINE_CODE, t.USER_ACCOUNT, regexp_substr(MACHINE_MOBILE, '[^,]+', 1, level) mobiles");
		sb.append(" from M_MACHINE_MANAGE t where DEL_FLG=0 and STOP_FLG=0");
		sb.append("  connect by level  <= regexp_count(MACHINE_MOBILE, ',') + 1  and MACHINE_ID = prior MACHINE_ID ");
		sb.append(" and prior dbms_random.value is not null ) where mobiles in (:mobileNum)");
		SQLQuery query=this.getSession().createSQLQuery(sb.toString());
		query.setParameterList("mobileNum", mobileNum);
		query.addScalar("id").addScalar("machine_code").addScalar("user_account").addScalar("mobiles");
		List<MMachineManage> list=query.setResultTransformer(Transformers.aliasToBean(MMachineManage.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MMachineManage>();
	}
	
	@Override
	public MMachineManage getMacByAccountAndMach(MMachineManage mac) {
		String hql = "from MMachineManage where user_account = ? and  machine_code =? and stop_flg = 0 and del_flg = 0";
		List<MMachineManage> list = super.find(hql, mac.getUser_account(),mac.getMachine_code());
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MMachineManage> checkIsLost(String userAccount) {
		String[] userAccounts=userAccount.split(",");
		String hql = "from MMachineManage where user_account in(:userAccount) and is_lost='2' and stop_flg = 0 and del_flg = 0";
		Query query=this.getSession().createQuery(hql.toString());
		query.setParameterList("userAccount", userAccounts);
		List<MMachineManage> list=query.list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}
