package cn.honry.statistics.deptstat.materialAndEquipment.dao.impl;

import java.sql.ResultSet;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.statistics.deptstat.materialAndEquipment.dao.MaterialAndEquipmentDao;
import cn.honry.statistics.deptstat.materialAndEquipment.vo.MaterialAndEquipmentVo;
import cn.honry.utils.ShiroSessionUtils;

@Repository("materialAndEquipmentDao")
@SuppressWarnings({ "all" })
public class MaterialAndEquipmentDaoImpl extends HibernateEntityDao<MaterialAndEquipmentVo> implements MaterialAndEquipmentDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	/**  
	 * 
	 * 物资设备统计查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<MaterialAndEquipmentVo> queryMaterialAndEquipment(String itemCode,String page,String rows,String queryStorage,final String startTime,final String endTime) {
		final String hql = this.hql(itemCode, queryStorage, startTime, endTime);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<MaterialAndEquipmentVo> voList = (List<MaterialAndEquipmentVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<MaterialAndEquipmentVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(hql)
					   .addScalar("deptCode",Hibernate.STRING)
					   .addScalar("itemCode",Hibernate.STRING)
					   .addScalar("itemName",Hibernate.STRING)
					   .addScalar("specs",Hibernate.STRING)
					   .addScalar("minUnit",Hibernate.STRING)
					   .addScalar("packQty",Hibernate.STRING)
					   .addScalar("packUnit",Hibernate.STRING)
					   .addScalar("inNum",Hibernate.INTEGER)
					   .addScalar("outNum",Hibernate.INTEGER)
					   .addScalar("storeCount",Hibernate.INTEGER);
				if(StringUtils.isNotBlank(startTime)){
					queryObject.setParameter("startTime",startTime);
				}
				if(StringUtils.isNotBlank(endTime)){
					queryObject.setParameter("endTime",endTime );
				}
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(MaterialAndEquipmentVo.class)).list();
			}
		});
		if(voList!=null&&voList.size()>0){
			return voList;
		}
		return new ArrayList<MaterialAndEquipmentVo>();
	}
	
	/**  
	 * 
	 * 物资设备统计查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月10日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月10日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public int getTotalMaterialAndEquipment(String itemCode,String queryStorage,String startTime,String endTime) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.hql(itemCode, queryStorage, startTime, endTime)+" ) ";
		if (StringUtils.isNotBlank(startTime)) {
			paraMap.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			paraMap.put("endTime", endTime);
		}
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}

	private String hql(String itemCode,String queryStorage,String startTime,String endTime){
		StringBuffer sql=new StringBuffer();
		sql.append("select a.deptCode as deptCode,a.itemCode as itemCode,a.itemName as itemName,a.specs as specs, ");
		sql.append("a.minUnit as minUnit,a.packQty as packQty,a.packUnit as packUnit, ");
		sql.append("nvl(b.inum,0) as inNum, nvl(c.outnum,0) as outNum,nvl(d.num,0) as storeCount ");
		sql.append("from (select t.STORAGE_CODE as deptCode,t.item_name as itemName, t.specs as specs, ");
		sql.append("t.min_unit as minUnit,t.pack_unit as packUnit,t.pack_qty as packQty,t.item_code as itemCode ");
		sql.append("from (select * from T_MAT_BASEINFO f where 1=1");
		if (StringUtils.isNoneBlank(itemCode)) {
			sql.append("and f.item_code='"+itemCode+"' ");
		}
		if (StringUtils.isNoneBlank(queryStorage)) {
			sql.append("and f.STORAGE_CODE='"+queryStorage+"' ");
		}
		sql.append("and f.STOP_FLG=0 and f.DEL_FLG=0 )t order by t.item_code) a ");
		sql.append("left join (select i.item_code as itemCode, sum(i.in_num) as inum from ( select * from T_MAT_INPUT mi where 1=1 ");
		if (StringUtils.isNoneBlank(startTime)) {
			sql.append("and mi.IN_DATE >= to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNoneBlank(endTime)) {
			sql.append("and mi.IN_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append("and mi.STOP_FLG=0 and mi.DEL_FLG=0 )i  group by i.item_code) b on a.itemCode = b.itemCode ");
		sql.append("left join (select o.item_code as itemCode, sum(o.OUT_NUM) as outnum from (select * from T_MAT_OUTPUT mo where 1=1 ");
		if (StringUtils.isNoneBlank(startTime)) {
			sql.append("and mo.OUT_DATE >= to_date(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		if (StringUtils.isNoneBlank(endTime)) {
			sql.append("and mo.OUT_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append("and mo.STOP_FLG=0 and mo.DEL_FLG=0)o group by o.item_code) c on a.itemCode = c.itemCode ");
		sql.append("left join (select p.item_code as itemCode, sum(p.STORE_SUM) as num from T_MAT_STOCKINFO p ");
		sql.append("where p.STOP_FLG=0 and p.DEL_FLG=0 group by p.item_code)d on a.itemCode = d.itemCode");
		return sql.toString();
	}
	/**  
	 * 
	 * 仓库科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月15日 下午7:05:09 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月15日 下午7:05:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<MaterialAndEquipmentVo> queryStorageCode() {
		String hql="select distinct d.dept_code as code,d.dept_name as name from t_mat_baseinfo t left join t_department d on d.dept_code=t.STORAGE_CODE ";
		hql+="where t.stop_flg=0 and t.del_flg=0 and  d.stop_flg=0 and d.del_flg=0";
		SQLQuery queryObject = getSession().createSQLQuery(hql);
		queryObject.addScalar("code",Hibernate.STRING)
				.addScalar("name", Hibernate.STRING);
		List<MaterialAndEquipmentVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(MaterialAndEquipmentVo.class)).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MaterialAndEquipmentVo>();
	}
	/**  
	 * 
	 * 物资名称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月15日 下午7:05:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月15日 下午7:05:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<MaterialAndEquipmentVo> queryItemName(String page, String rows,String q) {
		final String hql= this.getSql(q);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<MaterialAndEquipmentVo> list = (List<MaterialAndEquipmentVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<MaterialAndEquipmentVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(hql)
						.addScalar("itemCode",Hibernate.STRING)
						.addScalar("itemName",Hibernate.STRING)
						.addScalar("specs",Hibernate.STRING)
						.addScalar("minUnit", Hibernate.STRING);
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(MaterialAndEquipmentVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MaterialAndEquipmentVo>();
	}
	 /**  
	 * 
	 * 物资名称Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月15日 下午8:39:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月15日 下午8:39:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public int getTotalItemName(String q) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.getSql(q)+" )";
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	private String getSql(String q){
		String hql="select t.item_code as itemCode,t.item_name as itemName,t.specs as specs,t.min_unit as minUnit from t_mat_baseinfo t where t.del_flg=0 and t.stop_flg=0 ";
		if (StringUtils.isNoneBlank(q)) {
			hql+="and t.item_code like '%"+q+"%' ";
			hql+="or t.item_name like '%"+q+"%' ";
			hql+="or t.specs like '%"+q+"%' ";
			hql+="or t.min_unit like '%"+q+"%' ";
		}
		return hql;
	}
}
