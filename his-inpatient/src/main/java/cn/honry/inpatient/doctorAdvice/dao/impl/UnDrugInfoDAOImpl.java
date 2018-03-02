package cn.honry.inpatient.doctorAdvice.dao.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.doctorAdvice.dao.UnDrugInfoDAO;

@Repository("unDrugInfoDAO")
@SuppressWarnings({ "all" })
public class UnDrugInfoDAOImpl extends HibernateEntityDao<DrugUndrug> implements UnDrugInfoDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<DrugUndrug> queryUndrugInfo(String page,String rows,DrugUndrug undrug) {
		String hql = joint(undrug);
		return super.getPage(hql, page, rows);
	}
	public String joint(DrugUndrug undrug){
		String hql = "from DrugUndrug t where t.stop_flg = 0 and t.del_flg = 0";
		if(StringUtils.isNotBlank(undrug.getUndrugSystype())){
			hql =hql+" and t.undrugSystype='"+undrug.getUndrugSystype()+"'";
		}
		if(StringUtils.isNotBlank(undrug.getName())){
			hql =hql+" and (t.name like '%"+undrug.getName()+"%' or t.undrugPinyin like '%"+undrug.getName()+"%' or t.undrugWb like '%"+undrug.getName()+"%' or t.undrugInputcode like '%"+undrug.getName()+"%' or t.code like '%"+undrug.getName()+"%' ) ";
		}
		return hql;
	}

	@Override
	public int getTotal(DrugUndrug undrug) {
		String hql = joint(undrug);
		return super.getTotal(hql);
	}

	@Override
	public List<DrugUndrug> queryUndrugInfo() {
		String hql = "select t.UNDRUG_ID as id, t.UNDRUG_NAME as name ,t.UNDRUG_CODE as code from T_DRUG_UNDRUG t "
				+ "where t.stop_flg = 0 and t.del_flg = 0";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql.toString()).addScalar("id").addScalar("name").addScalar("code");
		List<DrugUndrug> undrugList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugUndrug.class)).list();
		if(undrugList!=null&&undrugList.size()>0){
			return undrugList;
		}
		return new ArrayList<DrugUndrug>();
	}
    //加载所有非药品名称下拉
	@Override
	public List queryAllUndrug() {
		String hql = "select t.UNDRUG_ID as id, t.UNDRUG_NAME as name ,t.UNDRUG_CODE as code from T_DRUG_UNDRUG t "
				+ "where t.stop_flg = 0 and t.del_flg = 0";
		SQLQuery queryObject = this.getSession().createSQLQuery(hql.toString()).addScalar("id").addScalar("name").addScalar("code");
		List<DrugUndrug> undrugList = queryObject.setResultTransformer(Transformers.aliasToBean(DrugUndrug.class)).list();
		if(undrugList!=null&&undrugList.size()>0){
			return undrugList;
		}
		return new ArrayList<DrugUndrug>();
	}	
}
