package cn.honry.assets.assetsScrap.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.assets.assetsPurchase.dao.AssetsPurchaseDAO;
import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.assets.assetsRepair.dao.AssetsRepairDAO;
import cn.honry.assets.assetsScrap.dao.AssetsScrapDAO;
import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsDeviceScrap;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.BusinessChangeRecord;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import freemarker.template.utility.StringUtil;

@Repository("assetsScrapDAO")
@SuppressWarnings({ "all" })
public class AssetsScrapDAOImpl extends HibernateEntityDao<AssetsDeviceScrap> implements AssetsScrapDAO {
	private Logger logger=Logger.getLogger(AssetsScrapDAOImpl.class);
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<AssetsDeviceScrap> queryRepairRecode(String page, String rows, AssetsDeviceScrap assets)
			throws Exception {
		String hql = joint(assets);
		return super.getPage(hql, page,rows);
	}
	@Override
	public int getTotalList(String page, String rows, AssetsDeviceScrap assets) throws Exception {
		String hql = joint(assets);
		return super.getTotal(hql);
	}
	public String joint(AssetsDeviceScrap assets){
		String hql =" from AssetsDeviceScrap d where d.stop_flg=0 and d.del_flg=0";
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
