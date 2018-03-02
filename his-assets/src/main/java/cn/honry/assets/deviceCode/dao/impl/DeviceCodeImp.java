package cn.honry.assets.deviceCode.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.AssetsDeviceCode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.assets.deviceCode.dao.DeviceCodeDao;

@Repository("DeviceCodeDao")
@SuppressWarnings({ "all" })
public class DeviceCodeImp extends HibernateEntityDao<AssetsDeviceCode> implements DeviceCodeDao{
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
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public List<AssetsDeviceCode> queryDeviceCode(AssetsDeviceCode DeviceCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceCode where del_Flg=0 ");
		this.whereJoin(DeviceCode,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), DeviceCode.getPage(), DeviceCode.getRows());
	}
	
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public List<AssetsDeviceCode> queryDeviceCodeforXR() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceCode where del_Flg=0 and stop_flg=0 ");
		hql.append(" order by createTime desc ");
		List<AssetsDeviceCode> deviceCodeList=super.find(hql.toString(), null);
		return deviceCodeList;
	}
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	private StringBuilder whereJoin(AssetsDeviceCode deviceCode, StringBuilder hql) {
		if(StringUtils.isNotBlank(deviceCode.getOfficeName())){
			hql.append(" and officeName like '%"+deviceCode.getOfficeName()+"%'");
		}
		if(StringUtils.isNotBlank(deviceCode.getClassCode())){
			hql.append(" and classCode like '%"+deviceCode.getClassCode()+"%'");
		}
		if(StringUtils.isNotBlank(deviceCode.getClassName())){
			hql.append(" and className like '%"+deviceCode.getClassName()+"%'");
		}
		if(StringUtils.isNotBlank(deviceCode.getDeviceName())){
			hql.append(" and deviceName like '%"+deviceCode.getDeviceName()+"%'");
		}
		return hql;
	}
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	@Override
	public int getDeviceCodeCount(AssetsDeviceCode DeviceCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceCode where del_Flg=0 ");
		this.whereJoin(DeviceCode,hql);
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
	public List<AssetsDeviceCode> findAll() {
		String  sql = "select code as code,NAME as name from T_ASSETS_DEVICE_CODE where DEL_FLG=0 and STOP_FLG=0";
		return  this.getSession().createSQLQuery(sql).addScalar("code").addScalar("name").setResultTransformer(Transformers.aliasToBean(AssetsDeviceCode.class)).list();
	}
	@Override
	public List<AssetsDeviceCode> findbyName(String name) {
		String hql="FROM AssetsDeviceCode d WHERE d.del_flg=0 and d.officeName like '%"+name+"%'";
		List<AssetsDeviceCode> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsDeviceCode>();
	}
	
	@Override
	public List<AssetsDeviceCode> queryListByName(String name) {
		String hql = "from AssetsDeviceCode t where t.del_flg=0 and t.stop_flg = 0 and t.officeName = ?";
		List<AssetsDeviceCode> list = super.find(hql, name);
		if(list != null && list.size() > 0)
			return list;
		return new ArrayList<AssetsDeviceCode>();
	}
	@Override
	public void disableDeviceCode(String id) {
		String  sql = "update T_ASSETS_DEVICE_CODE set STATE=1 where DEL_FLG=0 and stop_flg=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public List<AssetsDeviceCode> getCodeList(String deviceCode) {
		String hql="FROM AssetsDeviceCode d WHERE d.del_flg=0 and d.stop_flg=0 and useFlg=0 and d.deviceCode = '"+deviceCode+"'";
		List<AssetsDeviceCode> list=this.findByObjectProperty(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<AssetsDeviceCode>();
	}
	@Override
	public void updateUseFlg(String id) {
		String  sql = "update T_ASSETS_DEVICE_CODE set USE_FLG=1 where DEL_FLG=0 and stop_flg=0 and USE_FLG=0 and id='"+id+"'";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public Integer queryNum(String classCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceCode where del_Flg=0 and deviceNo like '"+classCode+"%' order by createTime desc ");
		return super.getTotal(hql.toString());
	}
}
