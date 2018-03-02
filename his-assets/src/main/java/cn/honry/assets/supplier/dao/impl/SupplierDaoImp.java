package cn.honry.assets.supplier.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.AssetsSupplier;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.assets.supplier.dao.SupplierDao;

@Repository("SupplierDao")
@SuppressWarnings({ "all" })
public class SupplierDaoImp extends HibernateEntityDao<AssetsSupplier> implements SupplierDao{
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
	 * @param Supplier
	 * @return
	 */
	@Override
	public List<AssetsSupplier> querySupplier(AssetsSupplier Supplier) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsSupplier where del_Flg=0 and stop_flg=0 ");
		this.whereJoin(Supplier,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), Supplier.getPage(), Supplier.getRows());
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	@Override
	public List<AssetsSupplier> querySupplierforXR() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsSupplier where del_Flg=0 and stop_flg=0 ");
		hql.append(" order by createTime desc ");
		List<AssetsSupplier> supplierList=super.find(hql.toString(), null);
		return supplierList;
	}
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	private StringBuilder whereJoin(AssetsSupplier supplier, StringBuilder hql) {
		if(StringUtils.isNotBlank(supplier.getName())){
			hql.append(" and name like '%"+supplier.getName()+"%'");
		}
		if(StringUtils.isNotBlank(supplier.getLegal())){
			hql.append(" and legal like '%"+supplier.getLegal()+"%'");
		}
		if(StringUtils.isNotBlank(supplier.getAddress())){
			hql.append(" and address like '%"+supplier.getAddress()+"%'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Supplier
	 * @return
	 */
	@Override
	public int getSupplierCount(AssetsSupplier Supplier) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsSupplier where del_Flg=0 and stop_flg=0 ");
		this.whereJoin(Supplier,hql);
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
	public List<AssetsSupplier> findAll() {
		String  sql = "select code as code,NAME as name from T_ASSETS_SUPPLIER where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("code").addScalar("name").setResultTransformer(Transformers.aliasToBean(AssetsSupplier.class)).list();
	}
	@Override
	public List<AssetsSupplier> findbyName(String name) {
		String hql="FROM AssetsSupplier d WHERE d.del_flg=0 and d.Name like '%"+name+"%'";
		List<AssetsSupplier> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsSupplier>();
	}
	
	@Override
	public List<AssetsSupplier> queryListByName(String name) {
		String hql = "from AssetsSupplier t where t.del_flg=0 and t.stop_flg = 0 and t.supplierName = ?";
		List<AssetsSupplier> list = super.find(hql, name);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<AssetsSupplier>();
	}
}
