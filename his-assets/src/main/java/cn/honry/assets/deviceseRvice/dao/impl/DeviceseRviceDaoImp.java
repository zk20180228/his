package cn.honry.assets.deviceseRvice.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.AssetsDeviceseRvice;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.assets.deviceseRvice.dao.DeviceseRviceDao;

@Repository("DeviceseRviceDao")
@SuppressWarnings({ "all" })
public class DeviceseRviceDaoImp extends HibernateEntityDao<AssetsDeviceseRvice> implements DeviceseRviceDao{
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
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> queryDeviceseRvice(AssetsDeviceseRvice DeviceseRvice) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceseRvice where del_Flg=0 and stop_flg=0 ");
		this.whereJoin(DeviceseRvice,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), DeviceseRvice.getPage(), DeviceseRvice.getRows());
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> queryDeviceseRviceforXR() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceseRvice where del_Flg=0 and stop_flg=0 ");
		hql.append(" order by createTime desc ");
		List<AssetsDeviceseRvice> deviceseRviceList=super.find(hql.toString(), null);
		return deviceseRviceList;
	}
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	private StringBuilder whereJoin(AssetsDeviceseRvice deviceseRvice, StringBuilder hql) {
		if(StringUtils.isNotBlank(deviceseRvice.getOfficeName())){
			hql.append(" and officeName like '%"+deviceseRvice.getOfficeName()+"%'");
		}
		if(StringUtils.isNotBlank(deviceseRvice.getClassCode())){
			hql.append(" and classCode like '%"+deviceseRvice.getClassCode()+"%'");
		}
		if(StringUtils.isNotBlank(deviceseRvice.getClassName())){
			hql.append(" and className like '%"+deviceseRvice.getClassName()+"%'");
		}
		if(StringUtils.isNotBlank(deviceseRvice.getDeviceName())){
			hql.append(" and deviceName like '%"+deviceseRvice.getDeviceName()+"%'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public int getDeviceseRviceCount(AssetsDeviceseRvice DeviceseRvice) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceseRvice where del_Flg=0 and stop_flg=0 ");
		this.whereJoin(DeviceseRvice,hql);
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
	public List<AssetsDeviceseRvice> findAll() {
		String  sql = "select DEVICE_CODE as deviceCode,DEVICE_NAME as deviceName from T_ASSETS_DEVICESE_RVICE where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("deviceCode").addScalar("deviceName").setResultTransformer(Transformers.aliasToBean(AssetsDeviceseRvice.class)).list();
	}
	@Override
	public List<AssetsDeviceseRvice> findbyName(String name) {
		String hql="FROM AssetsDeviceseRvice d WHERE d.del_flg=0 and d.Name like '%"+name+"%'";
		List<AssetsDeviceseRvice> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsDeviceseRvice>();
	}
	
	@Override
	public List<AssetsDeviceseRvice> queryListByName(String name) {
		String hql = "from AssetsDeviceseRvice t where t.del_flg=0 and t.stop_flg = 0 and t.deviceseRviceName = ?";
		List<AssetsDeviceseRvice> list = super.find(hql, name);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<AssetsDeviceseRvice>();
	}
	@Override
	public List<AssetsDeviceseRvice> findOffice() {
		String  sql = "select distinct(OFFICE_CODE) as officeCode,OFFICE_NAME as officeName from T_ASSETS_DEVICESE_RVICE where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("officeCode").addScalar("officeName").setResultTransformer(Transformers.aliasToBean(AssetsDeviceseRvice.class)).list();
	}
	@Override
	public List<AssetsDeviceseRvice> findClass() {
		String  sql = "select distinct(CLASS_CODE) as classCode,CLASS_NAME as className from T_ASSETS_DEVICESE_RVICE where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("classCode").addScalar("className").setResultTransformer(Transformers.aliasToBean(AssetsDeviceseRvice.class)).list();
	}
	
	/**
	 *  查询所有符合条件的数据---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public List<AssetsDeviceseRvice> queryDeviceseRviceForjsp(AssetsDeviceseRvice DeviceseRvice) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceseRvice where del_Flg=0 and stop_flg=0 ");
		this.whereJoinForjsp(DeviceseRvice,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), DeviceseRvice.getPage(), DeviceseRvice.getRows());
	}
	
	/**
	 *  动态拼接条件---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	private StringBuilder whereJoinForjsp(AssetsDeviceseRvice deviceseRvice, StringBuilder hql) {
		if(StringUtils.isNotBlank(deviceseRvice.getOfficeName())){//只要一个不为空,其他三个肯定不为空
			hql.append(" and (officeName like '%"+deviceseRvice.getOfficeName()+"%'"
					+ " or className like '%"+deviceseRvice.getClassName()+"%'"
					+ " or deviceName like '%"+deviceseRvice.getDeviceName()+"%') ");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数---用于下拉框列表
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceseRvice
	 * @return
	 */
	@Override
	public int getDeviceseRviceCountForjsp(AssetsDeviceseRvice DeviceseRvice) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceseRvice where del_Flg=0 and stop_flg=0 ");
		this.whereJoinForjsp(DeviceseRvice,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
}
