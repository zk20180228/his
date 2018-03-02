package cn.honry.assets.depot.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.assets.depot.dao.DepotDao;

@Repository("DepotDao")
@SuppressWarnings({ "all" })
public class DepotDaoImp extends HibernateEntityDao<AssetsDepot> implements DepotDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public List<AssetsDepot> queryDepot(AssetsDepot Depot) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDepot where del_Flg=0 ");
		this.whereJoin(Depot,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), Depot.getPage(), Depot.getRows());
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public List<AssetsDepot> queryDepotforXR() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDepot where del_Flg=0 and stop_flg=0 ");
		hql.append(" order by createTime desc ");
		List<AssetsDepot> depotList=super.find(hql.toString(), null);
		return depotList;
	}
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	private StringBuilder whereJoin(AssetsDepot depot, StringBuilder hql) {
		if(StringUtils.isNotBlank(depot.getDepotName())){
			hql.append(" and depotName like '%"+depot.getDepotName()+"%'");
		}
		if(StringUtils.isNotBlank(depot.getAddress())){
			hql.append(" and address like '%"+depot.getAddress()+"%'");
		}
		if(StringUtils.isNotBlank(depot.getManageName())){
			hql.append(" and manageName like '%"+depot.getManageName()+"%'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	@Override
	public int getDepotCount(AssetsDepot Depot) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDepot where del_Flg=0 ");
		this.whereJoin(Depot,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
	
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	@Override
	public List<AssetsDepot> findAll() {
		String  sql = "select DEPOT_CODE as depotCode,DEPOT_NAME as depotName from T_ASSETS_DEPOT where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("depotCode").addScalar("depotName").setResultTransformer(Transformers.aliasToBean(AssetsDepot.class)).list();
	}
	@Override
	public List<AssetsDepot> findbyName(String name) {
		String hql="FROM AssetsDepot d WHERE d.del_flg=0 and d.Name like '%"+name+"%'";
		List<AssetsDepot> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsDepot>();
	}
	
	@Override
	public List<AssetsDepot> queryListByName(String name) {
		String hql = "from AssetsDepot t where t.del_flg=0 and t.stop_flg = 0 and t.depotName = ?";
		List<AssetsDepot> list = super.find(hql, name);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<AssetsDepot>();
	}
	@Override
	public void disableDepot(String id) {
		String  sql = "update T_ASSETS_DEPOT set stop_flg=1 where DEL_FLG=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public void enableDepot(String id) {
		String  sql = "update T_ASSETS_DEPOT set stop_flg=0 where DEL_FLG=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
}
