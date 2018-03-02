package cn.honry.mobile.iosUpdateVersion.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MIosApkVersion;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.iosUpdateVersion.dao.IosUpdateVersionDao;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-23
 * @version 1.0
 * @remark:平台异常信息记录
 */
@Repository("iosUpdateVersionDao")
@SuppressWarnings({"all"})
public class IosUpdateVersionDaoImpl extends HibernateEntityDao<MIosApkVersion> implements IosUpdateVersionDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Integer getTotal(MIosApkVersion mApkVersion) throws Exception {
		StringBuffer hql = new StringBuffer("from MIosApkVersion t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(mApkVersion, hql);
		Integer total = super.getTotal(hql.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<MIosApkVersion> getList(MIosApkVersion mApkVersion, String rows,
			String page) throws Exception {
		StringBuffer hql = new StringBuffer("from MIosApkVersion t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(mApkVersion, hql);
		List<MIosApkVersion> list = super.getPage(hql.toString(), page, rows);
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MIosApkVersion>();
	}
	
	
	private StringBuffer getHql(MIosApkVersion mApkVersion, StringBuffer hql) {
		if (mApkVersion.getApkMinimumNum() != null) {
			hql.append(" and (t.apkMinimumNum = ");
			hql.append(mApkVersion.getApkMinimumNum());
			hql.append(" or t.apkCurrentVnum = ");
			hql.append(mApkVersion.getApkMinimumNum());
			hql.append(" or t.apkNewestVnum = ");
			hql.append(mApkVersion.getApkMinimumNum());
			hql.append(")");
		}
		if (StringUtils.isNotBlank(mApkVersion.getApkVersionName())) {
			hql.append(" and t.apkVersionName like '%");
			hql.append(mApkVersion.getApkVersionName());
			hql.append("%'");
		}
		if (mApkVersion.getForceUpdateFlg() != null) {
			hql.append(" and t.forceUpdateFlg = ");
			hql.append(mApkVersion.getForceUpdateFlg());
		}
		if (mApkVersion.getSendRadio() != null) {
			hql.append(" and t.sendRadio = ");
			hql.append(mApkVersion.getSendRadio());
		}
		return hql;
	}
}
