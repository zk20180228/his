package cn.honry.statistics.deptstat.assetStatistics.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.AssetsDeviceUse;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.deptstat.assetStatistics.dao.AssetStatisticsDAO;
import cn.honry.statistics.deptstat.assetStatistics.vo.AssetsDeviceVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
@Repository("assetStatisticsDAO")
@SuppressWarnings({ "all" })
public class AssetStatisticsDAOImpl extends HibernateDaoSupport implements AssetStatisticsDAO{
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 * 
	 * 资产分类list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<AssetsDevice> queryAssetsDevice(String officeName,
			String className, String classCode, String deviceName, String page,
			String rows) throws Exception {
		final String hql = this.getHql(officeName, className, classCode, deviceName);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<AssetsDevice> voList = (List<AssetsDevice>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<AssetsDevice> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(hql)
					   .addScalar("officeName",Hibernate.STRING)
					   .addScalar("classCode",Hibernate.STRING)
					   .addScalar("className",Hibernate.STRING)
					   .addScalar("deviceName",Hibernate.STRING)
					   .addScalar("meterUnit",Hibernate.STRING)
					   .addScalar("deviceNum",Hibernate.INTEGER);
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(AssetsDevice.class)).list();
			}
		});
		if (voList!=null && voList.size()>0) {
			return voList;
		}
		return new ArrayList<AssetsDevice>();
	}
	/**  
	 * 
	 * 资产分类Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryAssetsDeviceTotal(String officeName, String className,
			String classCode, String deviceName) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.getHql(officeName, className, classCode, deviceName)+" ) ";
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	private String getHql(String officeName, String className,String classCode, String deviceName){
		StringBuffer hql = new StringBuffer();
		hql.append("select a.office_name as officeName,a.class_code as classCode,a.class_name as className,a.device_name as deviceName, a.meter_unit as meterUnit,b.num as deviceNum from(  ");
		hql.append("select distinct t.office_name,t.class_code,t.class_name, t.device_code,t.device_name,t.meter_unit from t_assets_device t where t.device_state=3 and t.del_flg = 0 and t.stop_flg = 0 ");
		hql.append(")a left join (select tt.device_code,nvl(sum(tt.device_num),0) as num from t_assets_device tt where tt.device_state=3 and tt.del_flg = 0 and tt.stop_flg = 0 group by tt.device_code)b ");
		hql.append("on b.device_code=a.device_code where 1=1 ");
		if (StringUtils.isNoneBlank(officeName)) {
			hql.append("and a.office_name like '%"+officeName+"%' ");
		}
		if (StringUtils.isNoneBlank(className)) {
			hql.append("and a.class_name like '%"+className+"%' ");		
		}
		if (StringUtils.isNoneBlank(classCode)) {
			hql.append("and a.class_code like '%"+classCode+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceName)) {
			hql.append("and a.device_name like '%"+deviceName+"%' ");
		}
		return hql.toString();
	}
	/**  
	 * 
	 * 领用部门list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<AssetsDeviceUse> queryAssetsDeviceUse(final String deptCode,
			String page, String rows) throws Exception {
		final String hql = this.getHql2(deptCode);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<AssetsDeviceUse> voList = (List<AssetsDeviceUse>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<AssetsDeviceUse> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(hql)
					   .addScalar("createDept",Hibernate.STRING)
					   .addScalar("createUser",Hibernate.STRING)
					   .addScalar("useDate",Hibernate.DATE)
					   .addScalar("classCode",Hibernate.STRING)
					   .addScalar("deviceName",Hibernate.STRING)
					   .addScalar("deviceNo",Hibernate.STRING)
					   .addScalar("meterUnit",Hibernate.STRING);
				if(StringUtils.isNotBlank(deptCode)){
					queryObject.setParameterList("deptCode",deptCode.split(","));
				}
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(AssetsDeviceUse.class)).list();
			}
		});
		if (voList!=null && voList.size()>0) {
			return voList;
		}
		return new ArrayList<AssetsDeviceUse>();
	}
	/**  
	 * 
	 * 领用部门Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryAssetsDeviceUseTotal(String deptCode) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.getHql2(deptCode)+" ) ";
		if (StringUtils.isNotBlank(deptCode)) {
			paraMap.put("deptCode", Arrays.asList(deptCode.split(",")));
		}
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	private String getHql2(String deptCode){
		StringBuffer hql = new StringBuffer();
		hql.append("select d.dept_name as createDept,e.employee_name as createUser,t.use_date as useDate,t.class_code as classCode,");
		hql.append("t.device_name as deviceName,t.device_no as deviceNo,t.METER_UNIT as meterUnit from t_assets_device_use t ");
		hql.append("left join t_department d on d.dept_code=t.createdept ");
		hql.append("left join t_employee e on e.employee_jobno=t.createuser where ");
		if (StringUtils.isNoneBlank(deptCode)) {
			hql.append("and d.dept_code in (:deptCode) ");
		}
		hql.append("t.del_flg=0 and t.stop_flg=0 and d.del_flg=0 and d.stop_flg=0 and e.del_flg=0 and e.stop_flg=0 ");
		return hql.toString();
		
	}
	/**  
	 * 
	 * 资产价值list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<AssetsDeviceVo> queryAssetsDeviceValue(String officeName,
			String className, String classCode, String deviceName, String page,
			String rows) throws Exception {
		final String hql = this.getHql3(officeName, className, classCode, deviceName);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<AssetsDeviceVo> voList = (List<AssetsDeviceVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<AssetsDeviceVo> doInHibernate(Session session) throws HibernateException,SQLException  {
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
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(AssetsDeviceVo.class)).list();
			}
		});
		if (voList!=null && voList.size()>0) {
			return voList;
		}
		return new ArrayList<AssetsDeviceVo>();
	}
	/**  
	 * 
	 * 资产价值Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Integer queryAssetsDeviceValueTotal(String officeName, String className,
			String classCode, String deviceName) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.getHql3(officeName, className, classCode, deviceName)+" ) ";
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	private String getHql3(String officeName, String className,String classCode, String deviceName){
		StringBuffer hql = new StringBuffer();
		hql.append("select t.office_name as officeName,t.class_code as classCode, t.class_name as className,t.device_name as deviceName,t.device_no as deviceNo,");
		hql.append("t.meter_unit as meterUnit, t.device_num as deviceNum, t.purch_price as purchPrice,d.tran_cost as tranCost,d.inst_cost as instCost,");
		hql.append("d.purch_total as purchTotal,t.depreciation as depreciation,t.device_date as deviceDate ");
		hql.append("from t_assets_device t left join T_ASSETS_PURCH d on d.DEVICE_CODE=t.DEVICE_CODE ");
		hql.append("where t.device_state=3 ");
		if (StringUtils.isNoneBlank(officeName)) {
			hql.append("and t.office_name like '%"+officeName+"%' ");
		}
		if (StringUtils.isNoneBlank(className)) {
			hql.append("and t.class_name like '%"+className+"%' ");		
		}
		if (StringUtils.isNoneBlank(classCode)) {
			hql.append("and t.class_code like '%"+classCode+"%' ");
		}
		if (StringUtils.isNoneBlank(deviceName)) {
			hql.append("and t.device_name like '%"+deviceName+"%' ");
		}
		hql.append("and d.appl_state=3 and t.del_flg = 0 and t.stop_flg = 0 and d.del_flg = 0 and d.stop_flg = 0");
		return hql.toString();
		
	}
}
