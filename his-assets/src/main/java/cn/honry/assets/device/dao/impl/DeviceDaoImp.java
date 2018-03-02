package cn.honry.assets.device.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.assets.device.dao.DeviceDao;

@Repository("DeviceDao")
@SuppressWarnings({ "all" })
public class DeviceDaoImp extends HibernateEntityDao<AssetsDevice> implements DeviceDao{
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
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceStorage(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=1 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), Device.getPage(), Device.getRows());
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceforXR() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 ");
		hql.append(" order by createTime desc ");
		List<AssetsDevice> deviceList=super.find(hql.toString(), null);
		return deviceList;
	}
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	private StringBuilder whereJoin(AssetsDevice device, StringBuilder hql) {
		if(StringUtils.isNotBlank(device.getOfficeName())){
			hql.append(" and officeName like '%"+device.getOfficeName()+"%'");
		}
		if(StringUtils.isNotBlank(device.getClassCode())){
			hql.append(" and classCode like '%"+device.getClassCode()+"%'");
		}
		if(StringUtils.isNotBlank(device.getClassName())){
			hql.append(" and className like '%"+device.getClassName()+"%'");
		}
		if(StringUtils.isNotBlank(device.getDeviceName())){
			hql.append(" and deviceName like '%"+device.getDeviceName()+"%'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountStorage(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=1 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
	
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceDraft(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=0 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), Device.getPage(), Device.getRows());
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountDraft(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=0 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceApproval(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=3 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), Device.getPage(), Device.getRows());
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountApproval(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=3 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
	}
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public List<AssetsDevice> queryDeviceNotApp(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=2 ");
		this.whereJoin(Device,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), Device.getPage(), Device.getRows());
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	@Override
	public int getDeviceCountNotApp(AssetsDevice Device) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDevice where del_Flg=0 and stop_flg=0 and deviceState=2 ");
		this.whereJoin(Device,hql);
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
	public List<AssetsDevice> findAll() {
		String  sql = "select code as code,NAME as name from T_ASSETS_DEVICE where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("code").addScalar("name").setResultTransformer(Transformers.aliasToBean(AssetsDevice.class)).list();
	}
	@Override
	public List<AssetsDevice> findbyName(String name) {
		String hql="FROM AssetsDevice d WHERE d.del_flg=0 and d.Name like '%"+name+"%'";
		List<AssetsDevice> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsDevice>();
	}
	
	@Override
	public List<AssetsDevice> queryListByName(String name) {
		String hql = "from AssetsDevice t where t.del_flg=0 and t.stop_flg = 0 and t.deviceName = ?";
		List<AssetsDevice> list = super.find(hql, name);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<AssetsDevice>();
	}
	@Override
	public void disableDevice(String id,String reason) {
		String  sql = "update T_ASSETS_DEVICE set DEVICE_STATE=2,DEVICE_DATE=sysdate,REASON='"+reason+"' where DEL_FLG=0 and stop_flg=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public void enableDevice(String id) {
		String  sql = "update T_ASSETS_DEVICE set DEVICE_STATE=3,DEVICE_DATE=sysdate where DEL_FLG=0 and stop_flg=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public void updateOutNum(String id, int num) {
		String  sql = "update T_ASSETS_DEVICE set OUT_NUM="+num+" where DEL_FLG=0 and stop_flg=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
}
