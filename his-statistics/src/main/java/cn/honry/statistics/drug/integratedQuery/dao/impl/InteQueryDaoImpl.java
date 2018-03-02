package cn.honry.statistics.drug.integratedQuery.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.DrugCheckdetail;
import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.drug.integratedQuery.dao.InteQueryDao;
import cn.honry.statistics.drug.integratedQuery.vo.InteQueryVO;
import cn.honry.utils.HisParameters;

/**  
 *  药房药库综合查询DAO实现类
 * @Author:luyanshou
 * @version 1.0
 */
@Repository("inteQueryDao")
@SuppressWarnings({"all"})
public class InteQueryDaoImpl extends HibernateDaoSupport implements InteQueryDao{

	@Resource(name = "sessionFactory")
	// 为HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 分页 查询
	 * @param vo 查询条件 
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 * @param firstResult 起始位置
	 * @param maxResults 每页显示的记录数
	 * @return
	 */
	public List getListByPage(InteQueryVO vo,int kind, int type,int firstResult,int maxResults) throws Exception {
		
		DetachedCriteria dc = getDetachedCriteria(vo,kind,type)	;
		return getHibernateTemplate().findByCriteria(dc, firstResult, maxResults);
	}
	
	/**
	 * 统计记录数
	 * @param vo 查询条件
	 * @param kind 汇总类型(0-按药品;1-按单据;2-按部门)
	 * @param type 查询类别(0-入库;1-出库;2-盘点;3-调价)
	 * @return
	 */
	public Integer getCount(InteQueryVO vo,int kind,int type) throws Exception {
		DetachedCriteria dc = getDetachedCriteria(vo,kind,type)	;
		dc.setProjection(Projections.rowCount());		
		List<Integer> list= (List<Integer>) getHibernateTemplate().findByCriteria(dc);
		return Integer.valueOf(list.get(0));
	}
	
	/**
	 * 获取药房药库信息
	 */
	public List<Object[]> getDrugStore() throws Exception{
		String hql="select id,deptName from SysDepartment t where t.deptType like ?";
		List<Object[]> list = (List<Object[]>) getHibernateTemplate().find(hql,"P%");
		return list;
	}
	
	/**
	 * 构建查询条件
	 */
	private DetachedCriteria getDetachedCriteria(InteQueryVO vo,int kind,int type) {
		DetachedCriteria dc=null;
		if(type==0){//入库记录
			dc=DetachedCriteria.forClass(DrugInStore.class);
			dc.add(Restrictions.eq("inState", 2));//状态:核准
			if(kind==1){
				dc.addOrder(Order.asc("inListCode"));
			}
			if(kind==2){
				dc.addOrder(Order.asc("companyCode"));
			}
		}else if(type==1){//出库记录
			dc=DetachedCriteria.forClass(DrugOutstoreNow.class);
			dc.add(Restrictions.eq("outState", 2));//状态:核准
			if(kind==1){
				dc.addOrder(Order.asc("outListCode"));
			}
			if(kind==2){
				dc.addOrder(Order.asc("drugDeptCode"));
			}
		}else if(type==2){//盘点记录
			dc=DetachedCriteria.forClass(DrugCheckdetail.class);
			dc.add(Restrictions.eq("checkState", 2));//盘点状态2-解封
			if(kind==1){
				dc.addOrder(Order.asc("checkNo"));//流水号
			}
			if(kind==2){
				dc.addOrder(Order.asc("drugDeptCode"));//科室编码
			}
		}else{//调价记录
			dc=DetachedCriteria.forClass(DrugAdjustPriceInfo.class);
			dc.add(Restrictions.eq("currentState", 1));//状态:1-已调价
		}
		if(vo!=null){
			if(vo.getBeginTime()!=null){
				dc.add(Restrictions.ge("createTime", vo.getBeginTime()));//创建时间
			}
			if(vo.getEndTime()!=null){
				dc.add(Restrictions.lt("createTime", vo.getEndTime()));//创建时间
			}
			if(StringUtils.isNotBlank(vo.getDrugCode())){//药品编码
				dc.add(Restrictions.eq("drugCode", vo.getDrugCode()));
			}
			if(StringUtils.isNotBlank(vo.getDrugDeptCode())){//库房编码
				if(type==3){//调价记录中 该字段名为"drugDept"
					dc.add(Restrictions.eq("drugDept", vo.getDrugDeptCode()));
				}else{//入库记录表、出库记录表、盘点表中该字段名为"drugDeptCode"
					dc.add(Restrictions.eq("drugDeptCode", vo.getDrugDeptCode()));
				}
			}
		}
		if(kind==0){
			dc.addOrder(Order.asc("tradeName"));//药品名称
		}
		return dc;
	}

	/**
	 * 获取公司id和名称
	 */
	public List<Object[]> getCompanyName() throws Exception{
		String hql="select id, companyName from DrugSupplycompany ";
		List<Object[]> list = (List<Object[]>) getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 查询科室信息
	 */
	public List<SysDepartment> getDept() throws Exception{
		String sql="select t.DEPT_ID as id,t.DEPT_NAME as deptName from "+HisParameters.HISPARSCHEMAHISUSER+"T_DEPARTMENT t";
		SQLQuery queryObject = this.getSession().createSQLQuery(sql)
				.addScalar("id")
				.addScalar("deptName");
		List<SysDepartment> list =queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<SysDepartment>();
	}
}
