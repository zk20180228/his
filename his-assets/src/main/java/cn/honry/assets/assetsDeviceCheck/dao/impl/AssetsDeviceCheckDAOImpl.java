package cn.honry.assets.assetsDeviceCheck.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.assets.assetsDeviceCheck.dao.AssetsDeviceCheckDAO;
import cn.honry.base.bean.model.AssetsDeviceCheck;
import cn.honry.base.dao.impl.HibernateEntityDao;
@Repository("assetsDeviceCheckDAO")
@SuppressWarnings({ "all" })
public class AssetsDeviceCheckDAOImpl extends HibernateEntityDao<AssetsDeviceCheck> implements AssetsDeviceCheckDAO{
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 
	 * 设备盘点管理list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<AssetsDeviceCheck> queryAssetsDeviceCheck(
			AssetsDeviceCheck deviceDossier) throws Exception {
		StringBuilder hql = this.getHql(deviceDossier);
		return super.getPage(hql.toString(), deviceDossier.getPage(), deviceDossier.getRows());
	}
	/**  
	 * 
	 * 设备盘点管理Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryTotal(AssetsDeviceCheck deviceDossier) throws Exception {
		StringBuilder hql = this.getHql(deviceDossier);
		return super.getTotal(hql.toString());
	}
	private StringBuilder getHql(AssetsDeviceCheck deviceDossier){
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceCheck WHERE stop_flg=0 AND del_flg=0 ");
		if (StringUtils.isNoneBlank(deviceDossier.getOfficeName())) {
			hql.append("and officeName like '%"+deviceDossier.getOfficeName()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getClassName())) {
			hql.append("and className like '%"+deviceDossier.getClassName()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getClassCode())) {
			hql.append("and classCode like '%"+deviceDossier.getClassCode()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getDeviceName())) {
			hql.append("and deviceName like '%"+deviceDossier.getDeviceName()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getDepotName())) {
			hql.append("and depotName like '%"+deviceDossier.getDepotName()+"%' ");
		}
		return hql;
	}
	/**  
	 * 
	 * 根据设备代码查询
	 * @Author: huzhenguo
	 * @CreateDate: 2017年12月4日 下午4:18:28 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年12月4日 下午4:18:28 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public AssetsDeviceCheck queryByDeviceCode(String deviceCode){
		StringBuilder hql = new StringBuilder();
		hql.append("from AssetsDeviceCheck where stop_flg=0 AND del_flg=0 ");
		if (StringUtils.isNoneBlank(deviceCode)) {
			hql.append("and deviceCode = "+deviceCode+" ");
		}
		List<AssetsDeviceCheck> list = super.find(hql.toString(),null);
		if (list!=null && list.size()>0) {
			return list.get(0);
		}
		return new AssetsDeviceCheck();
	}
}
