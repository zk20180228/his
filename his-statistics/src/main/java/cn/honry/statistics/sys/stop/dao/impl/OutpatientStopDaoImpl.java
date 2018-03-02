package cn.honry.statistics.sys.stop.dao.impl;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.sys.stop.dao.OutpatientStopDao;
import cn.honry.statistics.sys.stop.vo.OutPatientVo;
import cn.honry.utils.HisParameters;

/***
 * 门诊停诊原因统计表DAO实现层
 * @author  lyy
 * @createDate： 2016年6月23日 上午10:52:31 
 * @modifier lyy
 * @modifyDate：2016年6月23日 上午10:52:31
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("outpatientStopDao")
@SuppressWarnings({ "all" })
public class OutpatientStopDaoImpl extends HibernateEntityDao<OutPatientVo> implements OutpatientStopDao{

	@Resource(name = "sessionFactory")
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 查询门诊停诊原因所有人数
	 * @author  lyy
	 * @createDate： 2016年6月23日 上午10:56:17 
	 * @modifier lyy
	 * @modifyDate：2016年6月23日 上午10:56:17
	 * @param：   firstData 开始时间  endData 结束时间 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<OutPatientVo> getPageOutpatientStop(final String firstData, final String endData,final List<String> tnL) {
		
		if(tnL==null||tnL.size()<0){
			return new ArrayList<OutPatientVo>();
		}
		final String sql=this.sqlHer(firstData,endData,tnL);
		List<OutPatientVo> voList = (List<OutPatientVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<OutPatientVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(sql)
						.addScalar("deptName").addScalar("deptId");
				if(StringUtils.isNotBlank(firstData)){
					queryObject.setParameter("begin",firstData);
				}
				if(StringUtils.isNotBlank(endData)){
					queryObject.setParameter("end",endData );
				}
				
				return queryObject.setResultTransformer(Transformers.aliasToBean(OutPatientVo.class)).list();
			}
		});
		
		if(voList!=null&&voList.size()>0){
		
			return voList;
		}
		
		return new ArrayList<OutPatientVo>();
	}
	
	
	private String sqlHer(String firstData, String endData,List<String> tnL){
		StringBuffer sb = new StringBuffer();
		if(tnL.size()>1){
			sb.append("SELECT deptName,deptId FROM( ");
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append(" select distinct s").append(i).append(".schedule_deptname as deptName,s")
			.append(i).append(".schedule_deptid as deptId from ")
			.append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" s").append(i)
			.append(" where s").append(i).append(".del_flg=0 and s").append(i).append(".stop_flg=0");
			if(StringUtils.isNotBlank(firstData)){
				sb.append("AND trunc(s").append(i).append(".schedule_date,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append("AND trunc(s").append(i).append(".schedule_date,'dd') <= to_date(:end,'yyyy-MM-dd') ");
			}
		}
		
		if(tnL.size()>1){
		
			sb.append(" ) ");
		}
		
		return sb.toString();
	}
	
	/**
	 * @Description:获取表中最小时间
	 * @Author: zhangjin
	 * @CreateDate: 2016年11月30日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	public StatVo findMaxMin() {
		 final String sql = "SELECT MAX(mn.schedule_Date) AS eTime ,MIN(mn.schedule_Date) AS sTime FROM T_REGISTER_SCHEDULE_NOW mn";
			StatVo vo = (StatVo) this.getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public StatVo doInHibernate(Session session) throws HibernateException,SQLException {
					SQLQuery queryObject = session.createSQLQuery(sql.toString());
					queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
					return (StatVo) queryObject.setResultTransformer(Transformers.aliasToBean(StatVo.class)).uniqueResult();
				}
			});
			
			return vo;
	}
	
	/**
	 * @Description:获取停诊原因
	 * @Author: zhangjin
	 * @CreateDate: 2016年12月1日
	 * @param:
	 * @return 
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */	
	@Override
	public OutPatientVo getPageOutpatientStopMX(final OutPatientVo vo,final String firstData,final String endData,final List<String> tnL) {
		
		if(tnL==null||tnL.size()<0){
			return new OutPatientVo();
		}
		
		final StringBuffer sb = new StringBuffer();
		
		if(tnL.size()>1){
			sb.append("SELECT sumSick,sumEvection,sumMeet,sumOther,"+ "sum FROM( ");
		}
		
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("SELECT ");
			sb.append("(select count(*) from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".schedule_isstop = '1' and t")
			.append(i).append(".schedule_stopreason = '1' and t").append(i).append(".schedule_deptid='"+vo.getDeptId()+"'");
			if(StringUtils.isNotBlank(firstData)){
				sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') ");
			}
			sb.append(" ) as sumSick,")
			.append("(select count(*) from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".schedule_isstop = '1' and t")
			.append(i).append(".schedule_stopreason = '2' and t").append(i).append(".schedule_deptid='"+vo.getDeptId()+"'");
			if(StringUtils.isNotBlank(firstData)){
						sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
					}
					if(StringUtils.isNotBlank(endData)){
						sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') ");
			}			 		
					
			sb.append(" ) as sumEvection,")
			.append("(select count(*) from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".schedule_isstop = '1' and t")
			.append(i).append(".schedule_stopreason = '3' and t").append(i).append(".schedule_deptid='"+vo.getDeptId()+"'");
			if(StringUtils.isNotBlank(firstData)){
					sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endData)){
					sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') ");
			}
					
			sb.append(" ) as sumMeet,")
			.append("(select count(*) from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".schedule_isstop = '1' and t")
			.append(i).append(".schedule_stopreason = '4' and t").append(i).append(".schedule_deptid='"+vo.getDeptId()+"'");
			if(StringUtils.isNotBlank(firstData)){
				sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(endData)){
				sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') ");
			}		 		
			sb.append(" ) as sumOther,")
			.append("(select count(*) from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".schedule_isstop = '1' and t")
			.append(i).append(".schedule_stopreason in ('1','2','3','4') and t").append(i).append(".schedule_deptid='"+vo.getDeptId()+"'");
			if(StringUtils.isNotBlank(firstData)){
					sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
				}
				if(StringUtils.isNotBlank(endData)){
						sb.append("AND trunc(t").append(i).append(".schedule_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') ");
				}			 		
			sb.append(" ) as sum ")
			.append(" from dual ");
		}
		
		if(tnL.size()>1){
			
			sb.append(" ) ");
		}
		
		OutPatientVo voList = (OutPatientVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutPatientVo doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(sb.toString())
						.addScalar("sumSick",Hibernate.INTEGER)
						.addScalar("sumEvection",Hibernate.INTEGER).addScalar("sumMeet",Hibernate.INTEGER)
						.addScalar("sumOther",Hibernate.INTEGER).addScalar("sum",Hibernate.INTEGER);
				if(StringUtils.isNotBlank(firstData)){
					queryObject.setParameter("begin",firstData);
				}
				if(StringUtils.isNotBlank(endData)){
					queryObject.setParameter("end",endData );
				}
				
				return (OutPatientVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutPatientVo.class)).uniqueResult();
			}
		});
		
		vo.setSumSick(voList.getSumSick()==null?0:voList.getSumSick());
		vo.setSumEvection(voList.getSumEvection()==null?0:voList.getSumEvection());
		vo.setSumMeet(voList.getSumMeet()==null?0:voList.getSumMeet());
		vo.setSumOther(voList.getSumOther()==null?0:voList.getSumOther());
		vo.setSum(voList.getSum()==null?0:voList.getSum());
		
		return vo;
	}
	
	
	public Map<String,String> getStopReason(){
		
		Map<String,String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append(" FROM BusinessDictionary d WHERE d.type = 'stopReason'");
		List<BusinessDictionary> list = this.getSessionFactory().getCurrentSession().createQuery(sb.toString()).list();
		
		for (BusinessDictionary bd : list) {
		
			map.put(bd.getName(), bd.getEncode());
		}
		
		return map;
	}
	
	/**
	 * @Description 门诊停诊统计
	 * @author  marongbin
	 * @createDate： 2016年12月21日 上午11:43:06 
	 * @modifier 
	 * @modifyDate：
	 * @param map
	 * @param tnL
	 * @param firstData
	 * @param endData
	 * @return: List<OutPatientVo>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public List<OutPatientVo> getOutpatientStop(Map<String,String> map,List<String> tnl,String firstData,String endData){
		
		StringBuffer sb = new StringBuffer();
		// TODO 参数写死，待处理...
		
		//出差
		String cc = StringUtils.isNotBlank(map.get("进修、出差、扶贫"))?map.get("进修、出差、扶贫"):"";
		
		//开会
		String kh = StringUtils.isNotBlank(map.get("参加会议、教学"))?map.get("参加会议、教学"):"";
		
		//生病
		String sick = StringUtils.isNotBlank(map.get("因病、调休、事假"))?map.get("因病、调休、事假"):"";
		if(tnl!=null||tnl.size()>0){
			sb.append(" SELECT * FROM (");
			sb.append(" SELECT deptName,deptId,SUM(sb) as sumSick ,SUM(cc) as sumEvection,SUM(kh) as sumMeet,SUM(hj)-NVL(SUM(kh),0)-NVL(SUM(cc),0)-NVL(SUM(sb),0) as sumOther,SUM(hj) as sum FROM (");
			for (int i = 0; i < tnl.size(); i++) {
				if(i!=0){
					sb.append(" UNION ALL ");
				}
				sb.append(" SELECT s.SCHEDULE_DEPTID deptId,s.SCHEDULE_DEPTNAME deptName, ");
				sb.append(" sum(DECODE(s.SCHEDULE_STOPREASON,'").append(sick).append("',1)) sb, ");
				sb.append(" sum(DECODE(s.SCHEDULE_STOPREASON,'").append(cc).append("',1)) cc, ");
				sb.append(" sum(DECODE(s.SCHEDULE_STOPREASON,'").append(kh).append("',1)) kh, ");
				sb.append(" sum(DECODE (s.SCHEDULE_STOPREASON,null,0,1)) hj FROM ").append(tnl.get(i)).append(" s ");
				sb.append(" WHERE s.SCHEDULE_ISSTOP = 0 ");
				sb.append(" AND s.SCHEDULE_DATE>=TO_DATE(:firstData, 'yyyy-MM-dd') ");
				sb.append(" AND s.SCHEDULE_DATE<TO_DATE(:endData, 'yyyy-MM-dd') ");
				sb.append(" and s.SCHEDULE_STOPREASON is not null ");
				sb.append(" GROUP BY s.SCHEDULE_DEPTID,s.SCHEDULE_DEPTNAME ");
			}
			sb.append(" ) GROUP BY deptId,deptName");
			sb.append(" )ORDER BY SUM DESC");
		}
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("firstData", firstData);
		paramMap.put("endData", endData);
		List<OutPatientVo> list = namedParameterJdbcTemplate.query(sb.toString(), paramMap, new RowMapper<OutPatientVo>() {
			@Override
			public OutPatientVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				OutPatientVo vo = new OutPatientVo();
				vo.setDeptId(rs.getString("deptId"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setSum(rs.getInt("sum"));
				vo.setSumEvection(rs.getInt("sumEvection"));
				vo.setSumMeet(rs.getInt("sumMeet"));
				vo.setSumOther(rs.getInt("sumOther"));
				vo.setSumSick(rs.getInt("sumSick"));
				
				return vo;
			}
		});
		
		return list;
	}
}
