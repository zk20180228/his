package cn.honry.assets.assetsPurch.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.assets.assetsPurch.dao.AssetsPurchDAO;
import cn.honry.assets.assetsPurch.vo.AssetsPurchVo;
import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.dao.impl.HibernateEntityDao;

@Repository("assetsPurchDAO")
@SuppressWarnings({ "all" })
public class AssetsPurchDAOImpl extends HibernateEntityDao<AssetsPurch> implements AssetsPurchDAO {
	private Logger logger=Logger.getLogger(AssetsPurchDAOImpl.class);
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 * 设备采购申报--已申报列表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurch> queryAllAssetsByData(AssetsPurch assets,String state) throws Exception {
		String hql = joint(assets,state);
		return super.getPage(hql, assets.getPage(),assets.getRows());
	}
	public String joint(AssetsPurch entity,String state){
		String hql="FROM AssetsPurch d WHERE d.del_flg = 0 ";
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
				hql = hql+" AND d.deviceName LIKE '%"+entity.getDeviceName()+"%'";
			}
		}
		if(StringUtils.isNotBlank(state)){
			hql = hql+" AND d.applState = '"+state+"'";
		}
		hql+=" order by d.applDate desc";
		return hql;
	}
	@Override
	public int getTotal( AssetsPurch assets,String state) throws Exception {
		String hql = joint(assets,state);
		return super.getTotal(hql);
	}
	/**  
	 * 设备采购申报--已申报  停用
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void stopAssets(String id, String flag) {
		id=id.replaceAll(",", "','");
		String sql="update T_ASSETS_PURCH d set d.STOP_FLG ="+flag+" where d.ID ('"+id+"') ";
		this.getSession().createSQLQuery(sql).executeUpdate();
		
	}
	/**  
	 * 设备采购审批--待审批  通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void passAssets(String id) throws Exception {
		String sql="update T_ASSETS_PURCH d set d.APPL_STATE =3 where d.ID='"+id+"' ";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	/**  
	 * 设备采购审批--待审批  不通过
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void noPassAssets(String id, String reason) throws Exception {
		String sql="update T_ASSETS_PURCH d set d.APPL_STATE =2, d.REASON ='"+reason+"' where d.ID='"+id+"' ";
		this.getSession().createSQLQuery(sql).executeUpdate();
		
	}
	/**  
	 * 设备采购审批--已审批  不通过原因
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public AssetsPurch seeReason(String id) {
		String hql="FROM AssetsPurch d WHERE d.del_flg = 0 and d.stop_flg =0 and d.id ='"+id+"'";
		AssetsPurch assets = new AssetsPurch();
		List<AssetsPurch> adjustPriceList=super.find(hql,null);
		if(adjustPriceList!=null && adjustPriceList.size()>0){
			return adjustPriceList.get(0);
		}
		return assets;
	}
	/**  
	 * 设备采购审批--待审批列表
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurch> querySPAssetsByData(AssetsPurch assets, String state,String startTime,String endTime) {
		String hql = jointSP(assets,state, startTime, endTime);
		return super.getPage(hql, assets.getPage(),assets.getRows());
	}
	
	/**  
	 * 设备采购审批--待审批 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int getSPTotalList(AssetsPurch assets, String state,String startTime,String endTime) {
		String hql = jointSP(assets,state, startTime, endTime);
		return super.getTotal(hql);
	}
	public String jointSP(AssetsPurch entity,String state,String startTime,String endTime){
		String hql="FROM AssetsPurch d WHERE d.del_flg = 0 and d.stop_flg =0";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getOfficeName())){
				hql+=" AND d.officeName LIKE '%"+entity.getOfficeName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassName())){
				hql+=" AND d.className LIKE '%"+entity.getClassName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassCode())){
				hql+=" AND d.classCode = '"+entity.getClassCode()+"'";
			}
			if(StringUtils.isNotBlank(entity.getDeviceName())){
				hql+=" AND d.deviceName LIKE '%"+entity.getDeviceName()+"%'";
			}
			if (StringUtils.isNoneBlank(startTime)) {
				hql+=" AND to_char(d.applDate,'yyyy-mm-dd') >= '"+startTime+"' ";
			}
			if (StringUtils.isNoneBlank(endTime)) {
				hql+=" AND to_char(d.applDate,'yyyy-mm-dd') <= '"+endTime+"' ";
			}
			if(StringUtils.isNotBlank(state)){
				if("0".equals(state)){
					hql = hql+" AND d.applState = '1'";
				}else if("1".equals(state)){
					hql = hql+" AND d.applState in ('2','3')";
				}
			}
		}
		hql+=" order by d.applDate desc";
		return hql;
	}
	
	@Override
	public List<AssetsPurchVo> queryPurchPlan(String page, String rows,AssetsPurchplan assets,String stated) {
		final String hql = this.jointPlan(assets, stated);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "10" : rows);
		List<AssetsPurchVo> voList = (List<AssetsPurchVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<AssetsPurchVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(hql)
					   .addScalar("applName",Hibernate.STRING)
					   .addScalar("officeCode",Hibernate.STRING)
					   .addScalar("officeName",Hibernate.STRING)
					   .addScalar("classCode",Hibernate.STRING)
					   .addScalar("className",Hibernate.STRING)
					   .addScalar("deviceCode",Hibernate.STRING)
					   .addScalar("deviceName",Hibernate.STRING)
					   .addScalar("meterUnit",Hibernate.STRING)
					   .addScalar("planPrice",Hibernate.DOUBLE)
					   .addScalar("planNum",Hibernate.INTEGER)
					   .addScalar("planPriceTotal",Hibernate.DOUBLE)
					   .addScalar("purchNum",Hibernate.INTEGER);
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(AssetsPurchVo.class)).list();
			}
		});
		if (voList!=null && voList.size()>0) {
			return voList;
		}
		return new ArrayList<AssetsPurchVo>();
	}
	public String jointPlan(AssetsPurchplan entity,String stated){
		String hql="select a.APPL_NAME as applName, b.OFFICE_NAME as officeName,b.CLASS_CODE as classCode,b.CLASS_NAME as className,  ";
		hql+="  b.DEVICE_NAME as deviceName,b.METER_UNIT as meterUnit,a.PLAN_PRICE as planPrice,a.planNum as planNum,";
		hql+=" a.planPriceTotal as planPriceTotal,a.purchNum as purchNum,b.office_code as officeCode,b.device_code as deviceCode ";
		hql+=" from (select d.APPL_ACC,d.DEVICE_CODE,d.APPL_NAME,d.PLAN_PRICE, ";
		hql+="sum(d.PLAN_NUM) as planNum,sum(d.PLAN_TOTAL) as planPriceTotal,sum(t.purchNum) as purchNum FROM T_ASSETS_PURCHPLAN d ";
		hql+=" left join (select sum(PURCH_NUM) as purchNum,DEVICE_CODE from T_ASSETS_PURCH  where APPL_STATE=3 and STOP_FLG=0 and DEL_FLG=0 group by DEVICE_CODE) t on d.DEVICE_CODE=t.DEVICE_CODE where 1=1 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getOfficeName())){
				hql = hql+" AND d.OFFICE_NAME LIKE '%"+entity.getOfficeName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassName())){
				hql = hql+" AND d.CLASS_NAME LIKE '%"+entity.getClassName()+"%'";
			}
			if(StringUtils.isNotBlank(entity.getClassCode())){
				hql = hql+" AND d.CLASS_CODE = '"+entity.getClassCode()+"'";
			}
			if(StringUtils.isNotBlank(entity.getDeviceName())){
				hql = hql+" AND d.DEVICE_NAME LIKE '%"+entity.getDeviceName()+"%'";
			}
		}
		hql+=" and d.DEL_FLG = 0 and d.APPL_STATE=3 ";
		hql+=" group by d.APPL_ACC,d.APPL_NAME,d.DEVICE_CODE,d.PLAN_PRICE)a ";
		hql+=" left join T_ASSETS_DEVICESE_RVICE b on b.device_code=a.DEVICE_CODE where b.STOP_FLG=0 and b.del_flg=0 ";
		if("完成".equals(stated)){
			hql+=" and purchNum = planNum ";
		}
		if("未完成".equals(stated)){
			hql+=" and purchNum < planNum or purchNum is null ";
		}
		return hql;
	}
	
	@Override
	public int getTotalPlan(AssetsPurchplan assets,String stated) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String sb=" SELECT COUNT(1) FROM ( "+this.jointPlan(assets,stated)+" ) ";
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}
	
	/**  
	 * 设备合同管理用到
	 * @Author: zpty
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<AssetsPurch> queryDevicePurch(String officeCode,String page, String rows, String menuAlias) {
		AssetsPurch assets =new AssetsPurch();
		assets.setOfficeName(officeCode);
		String hql = jointNew(assets);
		return super.getPage(hql, page,rows);
	}
	public String jointNew(AssetsPurch entity){
		String hql="FROM AssetsPurch d WHERE d.del_flg = 0 ";
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
				hql = hql+" AND d.deviceName LIKE '%"+entity.getDeviceName()+"%'";
			}
		}
			hql = hql+" AND d.applState = 3 ";
			hql+=" order by d.applDate desc";
		return hql;
	}
	
	@Override
	public int queryTotalPurch(String code) {
		AssetsPurch assets =new AssetsPurch();
		assets.setOfficeName(code);
		String hql = jointNew(assets);
		return super.getTotal(hql);
	}
	@Override
	public int queryPlan(AssetsPurch assets) {
		String hql="select nvl(sum(d.PURCH_NUM),0) FROM t_assets_purch d WHERE d.APPL_STATE=3 and d.del_flg = 0 and d.stop_flg =0 ";
		hql+="and d.APPL_ACC='"+assets.getApplAcc()+"' ";
		hql+="and d.DEVICE_CODE='"+assets.getDeviceCode()+"' ";
		hql+="and d.purch_price='"+assets.getPurchPrice()+"' ";
		return namedParameterJdbcTemplate.queryForObject(hql, new HashMap<String, Object>(), java.lang.Integer.class);
	}
} 
