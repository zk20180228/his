package cn.honry.inpatient.pathwaydept.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.CpDept;
import cn.honry.base.bean.model.CpWay;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.pathwaydept.dao.PathWayDao;
@Repository("pathWayDao")
@SuppressWarnings({"all"})
public class PathWayDaoImpl extends HibernateEntityDao<CpDept> implements PathWayDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<CpDept> getcpDept(String page, String rows, String deptCode,
			String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from  CpDept ");
		if(StringUtils.isNotBlank(deptCode) && StringUtils.isNotBlank(name)){
			sb.append(" where deptCode = '"+deptCode+"'");
			sb.append(" and ( ");
			sb.append(" deptName like '%"+name+"%' ");
			sb.append(" or inputCode like '%"+name+"%' ");
			sb.append(" or inputCodeWB like '%"+name+"%' ");
			sb.append(" or customCode like '%"+name+"%' ");
			sb.append(" ) ");
		}else if(StringUtils.isNotBlank(deptCode)){
			sb.append(" where deptCode = '"+deptCode+"'");
		}else if(StringUtils.isNotBlank(name)){
			sb.append(" where  ");
			sb.append(" deptName like '%"+name+"%' ");
			sb.append(" or inputCode like '%"+name+"%' ");
			sb.append(" or inputCodeWB like '%"+name+"%' ");
			sb.append(" or customCode like '%"+name+"%' ");
		}
		List<CpDept> list = super.getPage(sb.toString(), page, rows);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<CpDept>();
	}

	@Override
	public int getcpDeptTotal(String deptCode, String name) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from  CpDept ");
		if(StringUtils.isNotBlank(deptCode) && StringUtils.isNotBlank(name)){
			sb.append(" where deptCode = '"+deptCode+"'");
			sb.append(" and ( ");
			sb.append(" deptName like '%"+name+"%' ");
			sb.append(" or inputCode like '%"+name+"%' ");
			sb.append(" or inputCodeWB like '%"+name+"%' ");
			sb.append(" or customCode like '%"+name+"%' ");
			sb.append(" ) ");
		}else if(StringUtils.isNotBlank(deptCode)){
			sb.append(" where deptCode = '"+deptCode+"'");
		}else if(StringUtils.isNotBlank(name)){
			sb.append(" where  ");
			sb.append(" deptName like '%"+name+"%' ");
			sb.append(" or inputCode like '%"+name+"%' ");
			sb.append(" or inputCodeWB like '%"+name+"%' ");
			sb.append(" or customCode like '%"+name+"%' ");
		}
		return super.getTotal(sb.toString());
	}
	@Override
	public List<CpWay> getCpWay() {
		StringBuffer sb = new StringBuffer();
		sb.append(" from CpWay where stop_flg=0 and del_flg=0 ");
		List<CpWay> list = super.find(sb.toString());
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<CpWay>();
	}
	
}
