package cn.honry.statistics.bi.bistac.treatmentEffect.dao.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.groovy.tools.shell.util.NoExitSecurityManager;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.model.Filters;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.treatmentEffect.dao.TreatmentEffectDao;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;
import cn.honry.utils.DateUtils;
/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单dao实现类
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
@Repository("treatmentEffectDao")
@SuppressWarnings({"all"})
public class TreatmentEffectDaoImpl extends HibernateEntityDao<TreatmentEffectVo> implements TreatmentEffectDao {
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 * @param sessionFactory
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	//把数据存入mongodb中
	public static final String TABLENAME = "ZLXGSJFX";//治疗效果数据分析
	
	
	

	/**
	 * @Description:查询当前在院人数
	 * @Author：  zpty
	 * @CreateDate： 2017-3-28
	 * @version 1.0
	**/
	@Override
	public int queryInPeople() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(1) from t_inpatient_info_now t  where  t.del_flg = 0 and t.stop_flg = 0 and t.in_state = 'I' ");
		Object count=getSession().createSQLQuery(sql.toString()).uniqueResult();
		return Integer.valueOf(count.toString());
	}

	/**
	 * 查询各科室治疗效果的人数
	 * @author zpty
	 * @CreateDate：2017-03-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreatmentEffectVo> queryUserRecord(List<String> tnL,final String date,final String deptName,final String begin,final String end) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<TreatmentEffectVo>();
		}
		StringBuffer hql=new StringBuffer();
		final StringBuffer sb = new StringBuffer();
		sb.append(" select outState,deptname,sum(outStateTotal) as outStateTotal from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if(i!=0){
				sb.append(" union all ");
			}
			sb.append(" select nvl(t.diag_outstate,1) as outState,t.DEPT_CODE as deptname,count(t.inpatient_id) as outStateTotal from ").append(tnL.get(i)).append(" t ").append(" where  ");
			if(StringUtils.isNotBlank(deptName)){
				sb.append(" t.DEPT_NAME = :deptName and ");
			}
			if(StringUtils.isNotBlank(begin)){
				sb.append(" trunc(t.OUT_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') and ");
			}
			if(StringUtils.isNotBlank(end)){
				sb.append(" trunc(t.OUT_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') and ");
			}
			sb.append(" t.stop_flg=0 and t.del_flg=0 group by t.diag_outstate,t.DEPT_CODE");
		}
		sb.append(" ) group by outState,DEPTNAME ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("outStateTotal",Hibernate.INTEGER).addScalar("outState",Hibernate.INTEGER).addScalar("deptName");
		if(StringUtils.isNotBlank(deptName)){
			queryObject.setParameter("deptName",deptName );
		}
		if(StringUtils.isNotBlank(begin)){
			queryObject.setParameter("begin", begin);
		}
		if(StringUtils.isNotBlank(end)){
			queryObject.setParameter("end", end);
		}
		List<TreatmentEffectVo> treatmentEffectVo=queryObject.setResultTransformer(Transformers.aliasToBean(TreatmentEffectVo.class)).list();
		if(treatmentEffectVo!=null&&treatmentEffectVo.size()>0){
			return treatmentEffectVo;
		}
		return new ArrayList<TreatmentEffectVo>();
	}

	@Override
	public TreatmentEffectVo findMaxMin() {
		final String sql = "SELECT MAX(mn.IN_DATE) AS eTime ,MIN(mn.IN_DATE) AS sTime FROM T_INPATIENT_INFO_NOW mn";
		TreatmentEffectVo vo = (TreatmentEffectVo)this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public TreatmentEffectVo doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
				sqlQuery.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (TreatmentEffectVo)sqlQuery.setResultTransformer(Transformers.aliasToBean(TreatmentEffectVo.class)).uniqueResult();
			}
		});
		return vo;
	}

	/**
	 * 
	 * @Description:根据年份，从mongodb中查数据
	 * @param date :年份，如：2017
	 * @return 返回封装TreatmentEffectVo的list集合
	 * List<TreatmentEffectVo>
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月11日 上午11:31:26
	 */
	public List<TreatmentEffectVo> queryUserRecordByMongo(String date){
		List<TreatmentEffectVo> list = new ArrayList<TreatmentEffectVo>();
		TreatmentEffectVo vo =null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("yearSelect", date);
		DBCursor cursor = new MongoBasicDao().findAlldata(TABLENAME, bdObject);
		while(cursor.hasNext()){
			vo = new TreatmentEffectVo();
			DBObject dbCursor = cursor.next();
			String  deptName =(String) dbCursor.get("deptName");
			Integer outState = (Integer) dbCursor.get("outState");
			if(outState>4){
				outState = 4;
			}
			Integer outStateTotal=(Integer) dbCursor.get("outStateTotal");	
			vo.setDeptName(deptName);
			vo.setOutState(outState);
			vo.setOutStateTotal(outStateTotal);
			list.add(vo);
		}
		return list;
	}
}
