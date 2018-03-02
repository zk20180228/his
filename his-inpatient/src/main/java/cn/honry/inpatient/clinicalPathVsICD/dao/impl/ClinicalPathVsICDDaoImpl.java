package cn.honry.inpatient.clinicalPathVsICD.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.bean.model.PathVsIcd;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.clinicalPathVsICD.dao.ClinicalPathVsICDDao;

@Repository(value="clinicalPathVsICDDao")
@SuppressWarnings({"all"})
public class ClinicalPathVsICDDaoImpl extends HibernateEntityDao<PathVsIcd> implements ClinicalPathVsICDDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Integer queryClinicalPathVsICDNum(String keyWord,String modelId) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select count(1) from T_PATH_VS_ICD t ");
		if(StringUtils.isNotBlank(modelId)){
			buffer.append(" where t.CP_ID = '"+modelId+"'");
		}
		if(StringUtils.isNotBlank(keyWord)){
			buffer.append(" and t.ICD_NAME like '%"+keyWord+"%'");
		}
		Integer total = ((BigDecimal) super.getSession().createSQLQuery(buffer.toString()).uniqueResult()).intValue();
		return total;
	}

	@Override
	public List<PathVsIcd> queryClinicalPathVsICD(String keyWord,String modelId, String page, String rows) {
		List<PathVsIcd> list = new ArrayList<PathVsIcd>();
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		StringBuffer buffer=new StringBuffer();
		buffer.append("select * from (select row_.*, rownum rownum_ from (");
		buffer.append("select t.ID as id,t.CP_ID as cpId,t.ICD_NAME as icdName, ");
		buffer.append("t.ICD_CODE as icdCode,t.STOP_FLG as stop_flg,t.DEL_FLG as del_flg, ");
		buffer.append("t.HOSPITAL_ID as hospitalId,t.AREA_CODE as areaCode from T_PATH_VS_ICD t ");
		if(StringUtils.isNotBlank(modelId)){
			buffer.append(" where t.CP_ID = '"+modelId+"'");
		}
		if(StringUtils.isNotBlank(keyWord)){
			buffer.append(" and t.ICD_NAME like '%"+keyWord+"%'");
		}
		buffer.append(") row_ where rownum <= " + (p * r) + ") where rownum_ > " + ((p - 1) * r) + "");
		SQLQuery query = super.getSession().createSQLQuery(buffer.toString());
		query.addScalar("id").addScalar("cpId")
		.addScalar("icdName").addScalar("icdCode")
		.addScalar("stop_flg",Hibernate.INTEGER).addScalar("del_flg",Hibernate.INTEGER)
		.addScalar("hospitalId").addScalar("areaCode");
		list = query.setResultTransformer(Transformers.aliasToBean(PathVsIcd.class)).list();
		return list;
	}

	@Override
	public PathVsIcd findPathwayVsICDById(String id) {
		List<PathVsIcd> list = new ArrayList<PathVsIcd>();
		String hql="from PathVsIcd where id = '"+id+"'";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new PathVsIcd();
	}


}
