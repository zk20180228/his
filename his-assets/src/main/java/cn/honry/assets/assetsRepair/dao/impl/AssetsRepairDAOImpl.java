package cn.honry.assets.assetsRepair.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.honry.assets.assetsRepair.dao.AssetsRepairDAO;
import cn.honry.assets.deviceDossier.dao.DeviceDossierDao;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.dao.impl.HibernateEntityDao;
import freemarker.template.utility.StringUtil;

@Repository("assetsRepairDAO")
@SuppressWarnings({ "all" })
public class AssetsRepairDAOImpl extends HibernateEntityDao<AssetsDeviceMaintain> implements AssetsRepairDAO {
	private Logger logger=Logger.getLogger(AssetsRepairDAOImpl.class);
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	private  DeviceDossierDao deviceDossierDao;
	@Override
	public List<AssetsDeviceMaintain> queryRepairRecode(String page, String rows, String state, String deviceNo)
			throws Exception {
		String hql = joint(state,deviceNo);
		return super.getPage(hql, page,rows);
	}
	@Override
	public int getTotalList(String page, String rows, String state, String deviceNo) throws Exception {
		String hql = joint(state,deviceNo);
		return super.getTotal(hql);
	}
	public String joint(String state,String deviceNo){
		String hql =" from AssetsDeviceMaintain where del_flg=0 and stop_flg=0 and deviceNo='"+deviceNo+"'";
		return hql;
	}
	@Override
	public List<AssetsDeviceMaintain> queryAssetsRepair(String page, String rows, AssetsDeviceMaintain assets)
			throws Exception {
		String hql = joint(assets);
		return super.getPage(hql, page,rows);
	}
	@Override
	public int queryTotalRepair(String page, String rows, AssetsDeviceMaintain assets) throws Exception {
		String hql = joint(assets);
		return super.getTotal(hql);
	}
	public String joint(AssetsDeviceMaintain assets){
		String hql =" from AssetsDeviceMaintain d where d.del_flg=0 and d.stop_flg=0 ";
		if(assets!=null){
			if(StringUtils.isNotBlank(assets.getOfficeName())){
				hql = hql+" AND d.officeName LIKE '%"+assets.getOfficeName()+"%'";
			}
			if(StringUtils.isNotBlank(assets.getClassName())){
				hql = hql+" AND d.className LIKE '%"+assets.getClassName()+"%'";
			}
			if(StringUtils.isNotBlank(assets.getDeviceCode())){
				hql = hql+" AND d.deviceCode = '"+assets.getDeviceCode()+"'";
			}
			if(StringUtils.isNotBlank(assets.getDeviceName())){
				hql = hql+" AND d.deviceName like '%"+assets.getDeviceName()+"%'";
			}
		}
		return hql;
	}
} 
