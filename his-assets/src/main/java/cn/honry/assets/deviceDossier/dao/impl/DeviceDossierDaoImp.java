package cn.honry.assets.deviceDossier.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.assets.deviceDossier.dao.DeviceDossierDao;
import cn.honry.assets.deviceDossier.vo.DeviceDossierVo;

@Repository("DeviceDossierDao")
@SuppressWarnings({ "all" })
public class DeviceDossierDaoImp extends HibernateEntityDao<AssetsDeviceDossier> implements DeviceDossierDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> queryDeviceDossier(AssetsDeviceDossier DeviceDossier) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 ");
		this.whereJoin(DeviceDossier,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), DeviceDossier.getPage(), DeviceDossier.getRows());
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> queryDeviceDossierforXR() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 and stop_flg=0 ");
		hql.append(" order by createTime desc ");
		List<AssetsDeviceDossier> deviceDossierList=super.find(hql.toString(), null);
		return deviceDossierList;
	}
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	private StringBuilder whereJoin(AssetsDeviceDossier deviceDossier, StringBuilder hql) {
		if (StringUtils.isNoneBlank(deviceDossier.getOfficeCode())) {
			hql.append("and officeCode like '%"+deviceDossier.getOfficeCode()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getOfficeName())) {
			hql.append("and officeName like '%"+deviceDossier.getOfficeName()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getClassCode())) {
			hql.append("and classCode like '%"+deviceDossier.getClassCode()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getClassName())) {
			hql.append("and className like '%"+deviceDossier.getClassName()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getDeviceNo())) {
			hql.append("and deviceNo like '%"+deviceDossier.getDeviceNo()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getDeviceCode())) {
			hql.append("and deviceCode like '%"+deviceDossier.getDeviceCode()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getDeviceName())) {
			hql.append("and deviceName like '%"+deviceDossier.getDeviceName()+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceDossier.getMeterUnit())) {
			hql.append("and meterUnit like '%"+deviceDossier.getMeterUnit()+"%' ");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public int getDeviceDossierCount(AssetsDeviceDossier DeviceDossier) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 ");
		this.whereJoin(DeviceDossier,hql);
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
	public List<AssetsDeviceDossier> findAll() {
		String  sql = "select code as code,NAME as name from T_ASSETS_DEVICE_DOSSIER where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("code").addScalar("name").setResultTransformer(Transformers.aliasToBean(AssetsDeviceDossier.class)).list();
	}
	@Override
	public List<AssetsDeviceDossier> findbyName(String name) {
		String hql="FROM AssetsDeviceDossier d WHERE d.del_flg=0 and d.Name like '%"+name+"%'";
		List<AssetsDeviceDossier> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsDeviceDossier>();
	}
	
	@Override
	public List<AssetsDeviceDossier> queryListByName(String name) {
		String hql = "from AssetsDeviceDossier t where t.del_flg=0 and t.stop_flg = 0 and t.deviceDossierName = ?";
		List<AssetsDeviceDossier> list = super.find(hql, name);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<AssetsDeviceDossier>();
	}
	@Override
	public void disableDeviceDossier(String id) {
		String  sql = "update T_ASSETS_DEVICE_DOSSIER set stop_flg=1 where DEL_FLG=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public void enableDeviceDossier(String id) {
		String  sql = "update T_ASSETS_DEVICE_DOSSIER set stop_flg=0 where DEL_FLG=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public int getTotal(String page, String rows, AssetsDeviceDossier assets, String state) {
		String hql = joint(assets,state);
		return super.getTotal(hql);
	}
	@Override
	public List<AssetsDeviceDossier> queryAllAssetsByData(String page, String rows, AssetsDeviceDossier assets,
			String state) {
		String hql = joint(assets,state);
		return super.getPage(hql, page,rows);
	}
	public String joint(AssetsDeviceDossier entity,String state){
		String hql = "from AssetsDeviceDossier d where d.del_flg = 0 and d.stop_flg=0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getOfficeName())){
				hql = hql+" AND d.officeName LIKE '%"+entity.getOfficeName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassName())){
				hql = hql+" AND d.className LIKE '%"+entity.getClassName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getDeviceCode())){
				hql = hql+" AND d.deviceCode = '"+entity.getDeviceCode()+"'";
			}
			if(StringUtils.isNotBlank(entity.getDeviceName())){
				hql = hql+" AND d.deviceName like '%"+entity.getDeviceName()+"%'";
			}
		}
		if(StringUtils.isNotBlank(state)){
			hql = hql+" AND d.state = '"+state+"'";
		}
		return hql;
	}
	

	
	@Override
	public List<DeviceDossierVo> queryDeviceDossierValue(AssetsDeviceDossier DeviceDossier) throws Exception {
		final String hql = this.getHql3(DeviceDossier);
		final int start = Integer.parseInt(DeviceDossier.getPage() == null ? "1" : DeviceDossier.getPage());
		final int count = Integer.parseInt(DeviceDossier.getRows() == null ? "20" : DeviceDossier.getRows());
		List<DeviceDossierVo> voList = (List<DeviceDossierVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<DeviceDossierVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(hql)
					   .addScalar("officeName",Hibernate.STRING)
					   .addScalar("classCode",Hibernate.STRING)
					   .addScalar("className",Hibernate.STRING)
					   .addScalar("deviceName",Hibernate.STRING)
					   .addScalar("deviceNo",Hibernate.STRING)
					   .addScalar("meterUnit",Hibernate.STRING)
					   .addScalar("deviceNum",Hibernate.INTEGER)
					   .addScalar("purchPrice",Hibernate.DOUBLE)
					   .addScalar("tranCost",Hibernate.DOUBLE)
					   .addScalar("instCost",Hibernate.DOUBLE)
					   .addScalar("purchTotal",Hibernate.DOUBLE)
					   .addScalar("depreciation",Hibernate.INTEGER)
					   .addScalar("deviceDate",Hibernate.DATE);
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(DeviceDossierVo.class)).list();
			}
		});
		if (voList!=null && voList.size()>0) {
			return voList;
		}
		return new ArrayList<DeviceDossierVo>();
	}
	@Override
	public Integer queryDeviceDossierTotal(AssetsDeviceDossier DeviceDossier)
			throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.getHql3(DeviceDossier)+" ) ";
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	private String getHql3(AssetsDeviceDossier DeviceDossier){
		StringBuffer hql = new StringBuffer();
		hql.append("select t.office_name as officeName,t.class_code as classCode, t.class_name as className,t.device_name as deviceName,t.device_no as deviceNo,");
		hql.append("t.meter_unit as meterUnit, tt.device_num as deviceNum, d.purch_price as purchPrice,d.tran_cost as tranCost,d.inst_cost as instCost,");
		hql.append("d.purch_total as purchTotal,tt.depreciation as depreciation,tt.device_date as deviceDate ");
		hql.append("from T_ASSETS_DEVICE_DOSSIER t left join T_ASSETS_PURCH d on d.DEVICE_CODE=t.DEVICE_CODE ");
		hql.append("left join T_ASSETS_DEVICE tt on tt.DEVICE_CODE=t.DEVICE_CODE ");
		hql.append("where 1=1 ");
		if (StringUtils.isNoneBlank(DeviceDossier.getOfficeName())) {
			hql.append("and t.office_name like '%"+DeviceDossier.getOfficeName()+"%' ");
		}
		if (StringUtils.isNoneBlank(DeviceDossier.getClassName())) {
			hql.append("and t.class_name like '%"+DeviceDossier.getClassName()+"%' ");		
		}
		if (StringUtils.isNoneBlank(DeviceDossier.getClassCode())) {
			hql.append("and t.class_code like '%"+DeviceDossier.getClassCode()+"%' ");
		}
		if (StringUtils.isNoneBlank(DeviceDossier.getDeviceName())) {
			hql.append("and t.device_name like '%"+DeviceDossier.getDeviceName()+"%' ");
		}
		if (StringUtils.isNoneBlank(DeviceDossier.getDeviceNo())) {
			hql.append("and t.device_no like '%"+DeviceDossier.getDeviceNo()+"%' ");
		}
		hql.append("and d.appl_state=3 and tt.device_state=3 and t.del_flg = 0 and t.stop_flg = 0 and d.del_flg = 0 and d.stop_flg = 0 and tt.del_flg = 0 and tt.stop_flg = 0");
		return hql.toString();
		
	}
	
	

	
	//***************************************************以下是设备领用管理******************************************************//	
	
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public List<AssetsDeviceDossier> queryDeviceDossierMyUse(AssetsDeviceDossier DeviceDossier) {
		// 获取当前登录人
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 and stop_flg = 0 and useAcc='"+userId+"' ");
		this.whereJoinMyUse(DeviceDossier,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), DeviceDossier.getPage(), DeviceDossier.getRows());
	}
	
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	private StringBuilder whereJoinMyUse(AssetsDeviceDossier deviceDossier, StringBuilder hql) {
		if(StringUtils.isNotBlank(deviceDossier.getOfficeName())){
			hql.append(" and officeName like '%"+deviceDossier.getOfficeName()+"%'");
		}
		if(StringUtils.isNotBlank(deviceDossier.getClassCode())){
			hql.append(" and classCode like '%"+deviceDossier.getClassCode()+"%'");
		}
		if(StringUtils.isNotBlank(deviceDossier.getClassName())){
			hql.append(" and className like '%"+deviceDossier.getClassName()+"%'");
		}
		if(StringUtils.isNotBlank(deviceDossier.getDeviceName())){
			hql.append(" and deviceName like '%"+deviceDossier.getDeviceName()+"%'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	@Override
	public int getDeviceDossierCountMyUse(AssetsDeviceDossier DeviceDossier) {
		// 获取当前登录人
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 and stop_flg = 0 and useAcc='"+userId+"' ");
		this.whereJoinMyUse(DeviceDossier,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
	@Override
	public List<AssetsDeviceDossier> queryDeviceUseView(String deviceCode,AssetsDeviceDossier deviceDossier) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 and stop_flg = 0 and DEVICE_CODE='"+deviceCode+"' ");
		this.whereJoinMyUse(deviceDossier,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), deviceDossier.getPage(), deviceDossier.getRows());
	}
	@Override
	public int getDeviceDossierCount(String deviceCode,AssetsDeviceDossier deviceDossier) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceDossier where del_Flg=0 and stop_flg = 0 and DEVICE_CODE='"+deviceCode+"' ");
		this.whereJoinMyUse(deviceDossier,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
	
	@Override
	public void repairMyUse(String id) {
		String  sql = "update T_ASSETS_DEVICE_DOSSIER set state=1 where DEL_FLG=0 and stop_flg=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
}
