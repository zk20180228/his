package cn.honry.assets.deviceContract.dao.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.assets.deviceContract.dao.DeviceContractDao;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.dao.impl.HibernateEntityDao;
@Repository("deviceContractDao")
@SuppressWarnings({"all"})
public class DeviceContractDaoImpl extends HibernateEntityDao<AssetsDeviceContract> implements DeviceContractDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 * 设备合同管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsDeviceContract> queryDeviceContract(AssetsDeviceContract contract, String page,String rows, String menuAlias) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceContract where del_Flg=0 and stop_flg=0 ");
		this.whereJoin(contract,hql);
		hql.append(" order by createTime desc ");
		return super.getPage(hql.toString(), page, rows);
	}
	/**  
	 * 设备合同管理(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(AssetsDeviceContract contract) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from AssetsDeviceContract where del_Flg=0 and stop_flg=0 ");
		this.whereJoin(contract,hql);
		hql.append(" order by createTime desc ");
		return super.getTotal(hql.toString());
		
	}
	
	/**
	 *  动态拼接条件
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Device
	 * @return
	 */
	private StringBuilder whereJoin(AssetsDeviceContract device, StringBuilder hql) {
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
}
