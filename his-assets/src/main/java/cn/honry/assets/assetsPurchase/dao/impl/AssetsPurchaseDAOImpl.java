package cn.honry.assets.assetsPurchase.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.assets.assetsPurchase.dao.AssetsPurchaseDAO;
import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.BusinessChangeRecord;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import freemarker.template.utility.StringUtil;

@Repository("assetsPurchaseDAO")
@SuppressWarnings({ "all" })
public class AssetsPurchaseDAOImpl extends HibernateEntityDao<AssetsPurchplan> implements AssetsPurchaseDAO {
	private Logger logger=Logger.getLogger(AssetsPurchaseDAOImpl.class);
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public List<AssetsPurchplan> queryAllAssetsByData(String page,String rows,AssetsPurchplan assets,String state) throws Exception {
		String hql = joint(assets,state);
		return super.getPage(hql, page,rows);
	}
	public String joint(AssetsPurchplan entity,String state){
		String hql="FROM AssetsPurchplan d WHERE d.del_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getOfficeName())){
				hql = hql+" AND d.officeName LIKE '%"+entity.getOfficeName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassName())){
				hql = hql+" AND d.className LIKE '%"+entity.getClassName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassCode())){
				hql = hql+" AND d.classCode = '"+entity.getClassCode()+"'";
			}
			if(StringUtils.isNotBlank(entity.getDeviceName())){
				hql = hql+" AND d.deviceName = '%"+entity.getDeviceName()+"%'";
			}
			if(entity.getStop_flg()!=null){
				if(entity.getStop_flg().equals("1")||entity.getStop_flg().equals("0")){
					hql = hql+" AND d.stop_flg = '"+entity.getStop_flg()+"'";

				}
			}
			
		}
		if(StringUtils.isNotBlank(state)){
			hql = hql+" AND d.applState = '"+state+"'";
		}
		return hql;
	}
	@Override
	public int getTotal(String page, String rows, AssetsPurchplan assets,String state) throws Exception {
		String hql = joint(assets,state);
		return super.getTotal(hql);
	}
	@Override
	public void stopAssets(String id, String flag) {
		id=id.replaceAll(",", "','");
		String sql="update T_ASSETS_PURCHPLAN d set d.STOP_FLG ="+flag+" where d.ID in('"+id+"') ";
		this.getSession().createSQLQuery(sql).executeUpdate();
		
	}
	@Override
	public void passAssets(String id) throws Exception {
		String sql="update T_ASSETS_PURCHPLAN d set d.APPL_STATE =3 where d.ID='"+id+"' ";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	@Override
	public void noPassAssets(String id, String reason) throws Exception {
		String sql="update T_ASSETS_PURCHPLAN d set d.APPL_STATE =2, d.REASON ='"+reason+"' where d.ID='"+id+"' ";
		this.getSession().createSQLQuery(sql).executeUpdate();
		
	}
	@Override
	public AssetsPurchplan seeReason(String id) {
		String hql="FROM AssetsPurchplan d WHERE d.del_flg = 0 and d.stop_flg =0 and d.id ='"+id+"'";
		AssetsPurchplan assets = new AssetsPurchplan();
		List<AssetsPurchplan> adjustPriceList=super.find(hql,null);
		if(adjustPriceList!=null && adjustPriceList.size()>0){
			return adjustPriceList.get(0);
		}
		return assets;
	}
	@Override
	public Map<String, Object> querySPAssetsByData(String pages, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = jointSP(assets,state,timeBegin,timeEnd);
		Integer page = pages == null ? 1 : Integer.valueOf(pages);
		Integer row = rows == null ? 20 : Integer.valueOf(rows);
		map.put("page", page);
		map.put("row", row);
		List<AssetsPurchplan> list =  namedParameterJdbcTemplate.query(hql.toString(), map, new RowMapper<AssetsPurchplan>() {

			@Override
			public AssetsPurchplan mapRow(ResultSet rs, int rowNum) throws SQLException {
				AssetsPurchplan plan = new AssetsPurchplan();
				plan.setId(rs.getString("id"));
				plan.setApplName(rs.getString("applName"));
				plan.setOfficeName(rs.getString("officeName"));
				plan.setClassName(rs.getString("className"));
				plan.setClassCode(rs.getString("classCode"));
				plan.setDeviceName(rs.getString("deviceName"));
				plan.setMeterUnit(rs.getString("meterUnit"));
				plan.setPlanPrice(rs.getObject("planPrice")==null?null:rs.getDouble("planPrice"));
				plan.setPlanNum(rs.getObject("planNum")==null?null:rs.getInt("planNum"));
				plan.setPlanTotal(rs.getObject("planTotal")==null?null:rs.getDouble("planTotal"));
				plan.setApplDate(rs.getTimestamp("applDate"));
				plan.setApplState(rs.getObject("applState")==null?null:rs.getInt("applState"));
				return plan;
			}
			
		});
		if(list!=null&&list.size()>0){
			resultMap.put("rows", list);
			resultMap.put("total", list.size());

		}else{
			resultMap.put("rows", new ArrayList<AssetsPurchplan>());
			resultMap.put("total", 0);
		}
		return resultMap;
	}
	@Override
	public int getSPTotalList(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd) {
		String hql = jointSP(assets,state,timeBegin,timeEnd);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("row", rows);
		Integer value = namedParameterJdbcTemplate.queryForObject(hql.toString(), map, Integer.class);
		if (value != null) {
			return value;
		}
		return 0;
	}
	public String jointSP(AssetsPurchplan entity,String state,String timeBegin, String timeEnd){
		String hql="select * from(select * from(select rownum as n,t.ID as id,t.APPL_NAME as applName,t.OFFICE_NAME as officeName,t.CLASS_NAME as className,t.CLASS_CODE as classCode,t.DEVICE_NAME as deviceName,t.METER_UNIT as  meterUnit,"
				+ "t.PLAN_PRICE as planPrice,t.PLAN_NUM as planNum,t.PLAN_TOTAL as planTotal,t.APPL_DATE as applDate,t.APPL_STATE as applState "
				+ " from T_ASSETS_PURCHPLAN t WHERE t.del_flg = 0 and t.stop_flg =0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getOfficeName())){
				hql = hql+" AND t.office_name LIKE '%"+entity.getOfficeName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassName())){
				hql = hql+" AND t.class_name LIKE '%"+entity.getClassName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassCode())){
				hql = hql+" AND t.class_code = '"+entity.getClassCode()+"'";
			}
			if(StringUtils.isNotBlank(entity.getDeviceName())){
				hql = hql+" AND t.device_name = '%"+entity.getDeviceName()+"%'";
			}
		}
		if(StringUtils.isNotBlank(state)){
			if("0".equals(state)){
				hql = hql+" AND t.APPL_STATE = '1'";
			}else if("1".equals(state)){
				hql = hql+" AND t.APPL_STATE in ('2','3')";
			}
		}
		if(StringUtils.isNotBlank(timeBegin) && StringUtils.isNotBlank(timeEnd)){
			hql = hql+" and (t.APPL_DATE>=to_date('"+timeBegin+" 00:00:00"+"','yyyy-MM-dd HH24:mi:ss') and t.APPL_DATE<=to_date('"+timeEnd+" 23:59:59"+"','yyyy-MM-dd HH24:mi:ss'))";
		}
		hql = hql+") aa  where rownum <= :page * :row ) where n>(:page -1) * :row ";
		return hql;
	}
	@Override
	public List<AssetsPurchplan> queryAllAssetsStat(String pages, String rows, AssetsPurchplan assets) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer.append("select * from(select * from(select rownum as n,t.ID as id,t.APPL_NAME as applName,t.OFFICE_NAME as officeName,t.CLASS_NAME as className,t.CLASS_CODE as classCode,t.DEVICE_NAME as deviceName,t.METER_UNIT as  meterUnit,"
				+ "t.PLAN_PRICE as planPrice,t.PLAN_NUM as planNum,t.PLAN_TOTAL as planTotal,t.APPL_DATE as applDate,t.APPL_STATE as applState "
				+ " from T_ASSETS_PURCHPLAN t WHERE t.del_flg = 0 and t.stop_flg =0 and t.APPL_STATE = '3'");
		if(assets!=null){
			if(StringUtils.isNotBlank(assets.getOfficeName())){
				hqlBuffer.append(" and t.office_name like '%"+assets.getOfficeName()+"%'");
			}
			if(StringUtils.isNotBlank(assets.getClassCode())){
				hqlBuffer.append(" and t.class_code = '"+assets.getClassCode()+"'");

			}
			if(StringUtils.isNotBlank(assets.getClassName())){
				hqlBuffer.append(" and t.class_name like '%"+assets.getClassName()+"%'");

			}
			if(StringUtils.isNotBlank(assets.getDeviceName())){
				hqlBuffer.append(" and t.device_name like '%"+assets.getDeviceName()+"%'");
			}
		}
		hqlBuffer.append(") aa  where rownum <= :page * :row ) where n>(:page -1) * :row ");
		Integer page = pages == null ? 1 : Integer.valueOf(pages);
		Integer row = rows == null ? 20 : Integer.valueOf(rows);
		map.put("page", page);
		map.put("row", row);
		List<AssetsPurchplan> list =  namedParameterJdbcTemplate.query(hqlBuffer.toString(), map, new RowMapper<AssetsPurchplan>() {

			@Override
			public AssetsPurchplan mapRow(ResultSet rs, int rowNum) throws SQLException {
				AssetsPurchplan vo = new AssetsPurchplan();
				vo.setId(rs.getString("id"));
				vo.setOfficeName(rs.getString("officeName"));
				vo.setClassCode(rs.getString("classCode"));
				vo.setClassName(rs.getString("className"));
				vo.setDeviceName(rs.getString("deviceName"));
				vo.setMeterUnit(rs.getString("meterUnit"));
				vo.setPlanNum(rs.getObject("planNum")==null?null:rs.getInt("planNum"));
				return vo;
			}
			
		});

		if (list != null && list.size() > 0) {
			return list;
		}else {
			return new ArrayList<AssetsPurchplan>();
		}
	}
	@Override
	public int queryAllAssetsStat(AssetsPurchplan assets) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer.append("select count(1) from(select rownum as n,t.APPL_NAME as applName,t.OFFICE_NAME as officeName,t.CLASS_NAME as className,t.CLASS_CODE as classCode,t.DEVICE_NAME as deviceName,t.METER_UNIT as  meterUnit,"
				+ "t.PLAN_PRICE as planPrice,t.PLAN_NUM as planNum,t.PLAN_TOTAL as planTotal,t.APPL_DATE as applDate,t.APPL_STATE as applState "
				+ " from T_ASSETS_PURCHPLAN t WHERE t.del_flg = 0 and t.stop_flg =0 and t.APPL_STATE = '3'");
		if(assets!=null){
			if(StringUtils.isNotBlank(assets.getOfficeName())){
				hqlBuffer.append(" and t.office_name like '%"+assets.getOfficeName()+"%'");
			}
			if(StringUtils.isNotBlank(assets.getClassCode())){
				hqlBuffer.append(" and t.class_code = '"+assets.getClassCode()+"'");

			}
			if(StringUtils.isNotBlank(assets.getClassName())){
				hqlBuffer.append(" and t.class_name like '%"+assets.getClassName()+"%'");

			}
			if(StringUtils.isNotBlank(assets.getDeviceName())){
				hqlBuffer.append(" and t.device_name like '%"+assets.getDeviceName()+"%'");
			}
		}
		hqlBuffer.append(")");
		Integer value = namedParameterJdbcTemplate.queryForObject(hqlBuffer.toString(), map, Integer.class);
		if (value != null) {
			return value;
		}
		return 0;
	}
	@Override
	public int queryPlan(AssetsPurch assets) {
		String hql="select nvl(sum(d.PLAN_NUM),0) FROM T_ASSETS_PURCHPLAN d WHERE d.APPL_STATE=3 and d.del_flg = 0 and d.stop_flg =0 ";
		hql+="and d.APPL_ACC='"+assets.getApplAcc()+"' ";
		hql+="and d.DEVICE_CODE='"+assets.getDeviceCode()+"' ";
		hql+="and d.PLAN_PRICE='"+assets.getPurchPrice()+"' ";
		return namedParameterJdbcTemplate.queryForObject(hql, new HashMap<String, Object>(), java.lang.Integer.class);
	}
} 
