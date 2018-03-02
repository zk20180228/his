package cn.honry.statistics.doctor.regisdocscheinfo.dao.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.regisdocscheinfo.dao.RegisDocScheInfoDao;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import freemarker.template.SimpleDate;
/***
 * 挂号医生排班信息查询DAO实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Repository("regisDocScheInfoDAO")
@SuppressWarnings({"all"})
public class RegisDocScheInfoDaoImpl extends HibernateEntityDao<RegisDocScheInfoVo> implements RegisDocScheInfoDao{
	
	// 为父类HibernateDaoSupport注入sessionFactory的值
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名； page 当前页数 ；  rows 分页条数
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	@Override
	public List<RegisDocScheInfoVo> getReRegisDocVoList(final String menutype,final String deptName, final String doctorName,String page,String rows, final String begin,final String end,final List<String> tnL) throws Exception {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<RegisDocScheInfoVo>();
		}
		final String sb=getHql(deptName,doctorName,begin, end,tnL);
		final int start = Integer.parseInt(page == null ? "1" : page);
		final int count = Integer.parseInt(rows == null ? "20" : rows);
		List<RegisDocScheInfoVo> voList = (List<RegisDocScheInfoVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<RegisDocScheInfoVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(sb)
						.addScalar("deptName").addScalar("doctorName").addScalar("reglevlName")
						.addScalar("noonName",Hibernate.INTEGER).addScalar("empRemark").addScalar("empPinyin")
						.addScalar("weekday",Hibernate.INTEGER).addScalar("seeDate",Hibernate.TIMESTAMP);
				if(StringUtils.isNotBlank(begin)){
					queryObject.setParameter("begin",begin);
				}
				if(StringUtils.isNotBlank(end)){
					queryObject.setParameter("end",end );
				}
				if(StringUtils.isNotBlank(deptName)){
					queryObject.setParameterList("deptName",deptName.split(","));
					
				}
				if(StringUtils.isNotBlank(doctorName)){
					queryObject.setParameterList("doctorName",doctorName.split(","));
				}
				if("all".equals(deptName)){
					queryObject.setParameter("usercode", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					queryObject.setParameter("menutype", menutype);
				}
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(RegisDocScheInfoVo.class)).list();
			}
		});
		
		if(voList!=null&&voList.size()>0){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for(RegisDocScheInfoVo rdsi:voList){
				rdsi.setSeeDate(dateFormat.parse(dateFormat.format(rdsi.getSeeDate())));
			}
			
			return voList;
		}

		return new ArrayList<RegisDocScheInfoVo>();
	}

	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public String getHql(String deptName,String doctorName,String begin,String end,List<String> tnL) {
		StringBuffer sb = new StringBuffer();
		if(tnL.size()>1){
			sb.append("SELECT deptName,doctorName,reglevlName,weekday,noonName,"
					+ "empRemark,seeDate,empPinyin FROM( ");
		}
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append(" select distinct tpt.DEPT_NAME as deptName,bb")
			.append(i).append(".EMPLOYEE_NAME as doctorName, ")
			.append(" (select trg.GRADE_TITLE from T_REGISTER_GRADE trg where trg.del_flg=0 and trg.GRADE_CODE=trs").append(i).append(".SCHEDULE_REGGRADE) as reglevlName, DECODE ( trs")
			.append(i).append(".SCHEDULE_WEEK,0,7,").append(" trs").append(i).append(".SCHEDULE_WEEK) as weekday, trs").append(i)
			.append(".SCHEDULE_MIDDAY as noonName,bb").append(i)
			.append(".employee_remark as empRemark,trs").append(i).append(".SCHEDULE_DATE as seeDate, bb").append(i).append(".employee_pinyin as empPinyin")
					.append(" from ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" trs").append(i).append(",t_employee bb").append(i).append(",T_DEPARTMENT tpt ")
			.append("  where trs").append(i).append(".SCHEDULE_DOCTOR = bb").append(i).append(".employee_jobno  ").append(" AND trs").append(i).append(".SCHEDULE_DEPTID ").append(" =tpt.DEPT_ID ");//trs0.SCHEDULE_DEPTID = tpt.DEPT_ID
			if(StringUtils.isNotBlank(doctorName)){
				sb.append(" and trs").append(i).append(".SCHEDULE_DOCTOR in (:doctorName) ");
			}
			if(StringUtils.isNotBlank(deptName)&&!"all".equals(deptName)){
				sb.append(" and  trs").append(i).append(".schedule_deptid in (:deptName) ");
			}else if(StringUtils.isBlank(deptName)){
			}else{
				sb.append(" and  trs").append(i).append(".schedule_deptid in (select distinct t.deptcode from t_sys_column_dept t where t.del_flg=0 and t.stop_flg=0 and t.usercode = :usercode and t.menutype = :menutype) ");
			}
			if(StringUtils.isNotBlank(begin)){
				sb.append(" AND trunc(trs").append(i).append(".schedule_date,'dd') >= to_date(:begin,'yyyy-MM-dd') ");
			}
			if(StringUtils.isNotBlank(end)){
				sb.append(" AND trunc(trs").append(i).append(".schedule_date,'dd') < to_date(:end,'yyyy-MM-dd') ");
			}
			sb.append(" AND tpt.DEPT_TYPE = 'C' ");
		}
		if(tnL.size()>1){
			sb.append(" ) ");
		}
		if(StringUtils.isNotBlank(deptName)&&StringUtils.isNotBlank(doctorName)){
			sb.append(" order by deptName , doctorName");
		}else{
			if(StringUtils.isNotBlank(deptName)){
				sb.append(" order by deptName ");
			}
			if(StringUtils.isNotBlank(doctorName)){
				sb.append(" order by doctorName ");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * @Description:根据条件查询医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名； page 当前页数 ；  rows 分页条数
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	public int  getTotal(final String deptName,final String doctorName,final String begin,final String end,final List<String> tnL,String menuAlias){
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final String sb=" SELECT COUNT(1) FROM ( "+getHql(deptName,doctorName,begin,end,tnL)+" ) ";
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		
		if (StringUtils.isNotBlank(begin)) {
			paraMap.put("begin", begin);
		}
		if (StringUtils.isNotBlank(end)) {
			paraMap.put("end", end);
		}
		if (StringUtils.isNotBlank(deptName)) {
			paraMap.put("deptName", Arrays.asList(deptName.split(",")));
		}
		if (StringUtils.isNotBlank(doctorName)) {
			paraMap.put("doctorName", Arrays.asList(doctorName.split(",")));
		}
		if("all".equals(deptName)){
			paraMap.put("usercode", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			paraMap.put("menutype", menuAlias);
		}
				
		
		return namedParameterJdbcTemplate.queryForObject(sb, paraMap, java.lang.Integer.class);
	}

	/**
	 * @Description:根据条件查询所有医生排班信息
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 医生姓名
	 * @return List<RegisDocScheInfoVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	@Override
	public List<RegisDocScheInfoVo> getAllReRegisDocVoList(final String doctorName,final String begin,final String end,final List<String> tnL) throws Exception {
		
		if(tnL==null||tnL.size()<0){
			return new ArrayList<RegisDocScheInfoVo>();
		}
		final String sb=getHql(null,doctorName,begin, end,tnL);
		List<RegisDocScheInfoVo> voList = (List<RegisDocScheInfoVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<RegisDocScheInfoVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(sb)
						.addScalar("deptName").addScalar("doctorName").addScalar("reglevlName")
						.addScalar("noonName",Hibernate.INTEGER).addScalar("empRemark").addScalar("empPinyin")
						.addScalar("weekday",Hibernate.INTEGER).addScalar("seeDate",Hibernate.DATE);
				if(StringUtils.isNotBlank(begin)){
					queryObject.setParameter("begin",begin);
				}
				if(StringUtils.isNotBlank(end)){
					queryObject.setParameter("end",end );
				}
				if(StringUtils.isNotBlank(doctorName)){
					queryObject.setParameterList("doctorName",doctorName.split(","));
				}
				
				return queryObject.setResultTransformer(Transformers.aliasToBean(RegisDocScheInfoVo.class)).list();
			}
		});
		
		if(voList!=null&&voList.size()>0){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			for(RegisDocScheInfoVo rdsi:voList){
				rdsi.setSeeDate(dateFormat.parse(dateFormat.format(rdsi.getSeeDate())));
			}
		
			return voList;
		}
		
		return new ArrayList<RegisDocScheInfoVo>();
	}
	
	/**
	 * @Description:渲染星期的方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:doctorName 星期id
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public String getWeekName(int weekName) {
		
		if(weekName==1){
			return "星期一";
		}else if(weekName==2){
			return "星期二";
		}else if(weekName==3){
			return "星期三";
		}else if(weekName==4){
			return "星期四";
		}else if(weekName==5){
			return "星期五";
		}else if(weekName==6){
			return "星期六";
		}else{
			return "星期日";
		}
	}
	
	/**
	 * @Description:渲染午别的方法
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月22日
	 * @param:noonName 午别id
	 * @return String
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public String getNoonName(int noonName) {
		
		if(noonName==1){
			return "上午";
		}else if(noonName==2){
			return "下午";
		}else {
			return "晚上";
		}
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
	 */	@Override
	public StatVo findMaxMin() {
		 final String sql = "SELECT MAX(mn.schedule_date) AS eTime ,MIN(mn.schedule_date) AS sTime FROM T_REGISTER_SCHEDULE_NOW mn";
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
		 * 
		 * @Description:打印报表
		 */
	@Override
	public List<RegisDocScheInfoVo> regisDocVoList(final String deptName,
	final String doctorName, final String begin, final String end, List<String> tnL,final String menutype) throws Exception {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<RegisDocScheInfoVo>();
		}
		final String sb=getHql(deptName,doctorName,begin, end,tnL);
		
		List<RegisDocScheInfoVo> voList = (List<RegisDocScheInfoVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public List<RegisDocScheInfoVo> doInHibernate(Session session) throws HibernateException,SQLException  {
				SQLQuery queryObject = session.createSQLQuery(sb)
						.addScalar("deptName").addScalar("doctorName").addScalar("reglevlName")
						.addScalar("noonName",Hibernate.INTEGER).addScalar("empRemark").addScalar("empPinyin")
						.addScalar("weekday",Hibernate.INTEGER).addScalar("seeDate",Hibernate.DATE);
				if(StringUtils.isNotBlank(begin)){
					queryObject.setParameter("begin",begin);
				}
				if(StringUtils.isNotBlank(end)){
					queryObject.setParameter("end",end );
				}
				if(StringUtils.isNotBlank(deptName)){

					queryObject.setParameterList("deptName",deptName.split(","));
				}
				
				if("all".equals(deptName)){
					queryObject.setParameter("usercode", ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					queryObject.setParameter("menutype", menutype);
				}
				
				if(StringUtils.isNotBlank(doctorName)){

					queryObject.setParameterList("doctorName",doctorName.split(","));
				}
				
				return queryObject.setResultTransformer(Transformers.aliasToBean(RegisDocScheInfoVo.class)).list();
			}
		});
		
		if(voList!=null&&voList.size()>0){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for(RegisDocScheInfoVo rdsi:voList){
				rdsi.setSeeDate(dateFormat.parse(dateFormat.format(rdsi.getSeeDate())));
			}
			
			return voList;
		}
		
		return new ArrayList<RegisDocScheInfoVo>();
	}	

}
