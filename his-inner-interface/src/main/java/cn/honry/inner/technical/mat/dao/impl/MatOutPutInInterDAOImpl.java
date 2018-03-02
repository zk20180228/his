package cn.honry.inner.technical.mat.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MatApply;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.bean.model.MatStockInfo;
import cn.honry.base.bean.model.MatStockdetail;
import cn.honry.inner.technical.mat.dao.MatOutPutInInterDAO;

/**  
 *  
 * @Author：luyanshou
 * @version 1.0
 *
 */
@Repository("matOutPutInInterDAO")
@SuppressWarnings({"all"})
public class MatOutPutInInterDAOImpl extends HibernateDaoSupport implements MatOutPutInInterDAO {

	@Resource(name = "sessionFactory")
	// 为HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 根据住院流水号查询患者Id
	 */
	public String getPatientID(String inpatientNo){
		//根据住院流水号查询病历号
		String hql1="select medicalrecordId from InpatientInfo where inpatientNo = ? and stop_flg= ? and del_flg= ?";
		List<String> list = (List<String>) getHibernateTemplate().find(hql1, inpatientNo,0,0);
		if(list==null||list.size()==0){
			return null;
		}
		String medicalrecordId = list.get(0);//获取病历号
		if(medicalrecordId==null){
			return null;
		}
		//根据病历号查询患者Id
		String hql2="select Id from Patient where medicalrecordId = ? and stop_flg= ? and del_flg= ?";
		List<String> list2 = (List<String>) getHibernateTemplate().find(hql2, medicalrecordId,0,0);
		if(list2==null||list2.size()==0){
			return null;
		}
		String patientId = list2.get(0);//获取患者Id
		return patientId;
	}
	/**
	 * 根据查询条件查询出库记录
	 */
	public List<MatOutput> getMatOutput(MatOutput t1,MatOutput t2){
		DetachedCriteria dc=getDetachedCriteria(t1,t2);
		List<MatOutput> list = (List<MatOutput>) getHibernateTemplate().findByCriteria(dc);
		return list;
	}
	
	/**
	 * 构建查询条件
	 */
	public DetachedCriteria getDetachedCriteria(MatOutput t1,MatOutput t2){
		DetachedCriteria dc=DetachedCriteria.forClass(MatOutput.class);
		if(t1!=null){
			if(StringUtils.isNotBlank(t1.getRecipeNo())){//处方号
				dc.add(Restrictions.eq("recipeNo", t1.getRecipeNo()));
			}
			if(t1.getSequenceNo() != null){//处方内流水号
				dc.add(Restrictions.eq("sequenceNo", t1.getSequenceNo()));
			}
			if(StringUtils.isNotBlank(t1.getItemCode())){//物资编码
				dc.add(Restrictions.eq("itemCode", t1.getItemCode()));
			}
			if(t1.getTransType() != null){//交易类型
				dc.add(Restrictions.eq("transType", t1.getTransType()));
			}
			if(StringUtils.isNotBlank(t1.getStorageCode())){//仓库编码
				dc.add(Restrictions.eq("storageCode", t1.getStorageCode()));
			}
			if(StringUtils.isNotBlank(t1.getTargetDept())){//领用科室编码
				dc.add(Restrictions.eq("targetDept", t1.getTargetDept()));
			}
			if(StringUtils.isNotBlank(t1.getOutListCode())){//出库单号
				dc.add(Restrictions.eq("outListCode", t1.getOutListCode()));
			}
		}
		dc.addOrder(Order.asc("batchNo"));
		return dc;
	}
	
	/**
	 * 根据非药品编号查询物资编号
	 */
	public String getItemCode(String undrugItemCode){
		String hql="select matItemCode from MatUndrugCompare where undrugItemCode= ? and stop_flg= ? and del_flg= ?";
		List<String> list = (List<String>) getHibernateTemplate().find(hql, undrugItemCode,0,0);
		if(list==null||list.size()==0){
			return null;
		}
		String itemCode = list.get(0);
		return itemCode;
	}
	/**
	 * 根据门诊号查询患者编号
	 */
	public String getPatientIdByNo(String no){
		String hql="select patientId from RegisterInfo where no= ? and stop_flg= ? and del_flg= ?";
		List<String> list = (List<String>) getHibernateTemplate().find(hql, no,0,0);
		if(list==null||list.size()==0){
			return null;
		}
		String s = list.get(0);
		return s;
	}
	/**
	 * 根据物资编码查询当前库存总数
	 */
	public Integer getSum(String itemCode,String storageCode){
		String hql= "select storeSum from MatStockInfo where itemCode= ? and itemDeptCode= ? and stop_flg= ? and del_flg= ?";
		List<Double> list = (List<Double>) getHibernateTemplate().find(hql, itemCode,storageCode,0,0);
		if(list==null||list.size()==0){
			return 0;
		}
		if(list.get(0)==null){
			return 0;
		}
		int sum = list.get(0).intValue();
		return sum;
	}
	/**
	 * 根据物资编码查询当前库存预扣库存 
	 * @param itemCode
	 * @param storageCode
	 * @return
	 */
	public Integer getpreoutSum(String itemCode,String storageCode){
		String hql= "select preoutSum from MatStockInfo where itemCode= ? and itemDeptCode= ? and stop_flg= ? and del_flg= ?";
		List<Long> list = (List<Long>) getHibernateTemplate().find(hql, itemCode,storageCode,0,0);
		if(list==null||list.size()==0){
			return 0;
		}
		Long l = list.get(0);
		if(l==null){
			return 0;
		}
		int sum = list.get(0).intValue();
		return sum;
	}
	
	/**
	 * 根据物资编码和仓库查询库存明细
	 */
	public List<MatStockdetail> getList(String itemCode,String storageCode){
		
		String hql="from MatStockdetail where itemCode= ? and storageCode = ? and stop_flg= ? and del_flg= ? order by batchNo";
		List<MatStockdetail> list = (List<MatStockdetail>) getHibernateTemplate().find(hql,itemCode,storageCode,0,0);
		return list;
	}
	/**
	 * 添加出库记录
	 */
	public void add(MatOutput m){
		getHibernateTemplate().save(m);
	}
	/**
	 * 添加出库申请
	 */
	public void add(MatApply m){
		getHibernateTemplate().save(m);
	}
	
	
	/**
	 *返回相应的序列,用于生成 申请单号、申请流水号、出库流水号、出库单号等
	 * @param seq
	 * @return
	 */
	public String getMaxNo(String seq){
		String hql= "SELECT "+seq+".NEXTVAL FROM DUAL";
		List list = getSession().createSQLQuery(hql).list();
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0).toString();
	}
	
	/**
	 * 查询库存明细
	 */
	public MatStockdetail getMatStockdetail(MatStockdetail t){
		DetachedCriteria dc=DetachedCriteria.forClass(MatStockdetail.class);
		if(t!=null){
			if(StringUtils.isNotBlank(t.getItemCode())){//物资编码
				dc.add(Restrictions.eq("itemCode", t.getItemCode()));
			}
			if(StringUtils.isNotBlank(t.getStorageCode())){//仓库编码
				dc.add(Restrictions.eq("storageCode", t.getStorageCode()));
			}
			if(StringUtils.isNotBlank(t.getBatchNo())){//批号
				dc.add(Restrictions.eq("batchNo", t.getBatchNo()));
			}
			if(StringUtils.isNotBlank(t.getPlaceCode())){//库位号
				dc.add(Restrictions.eq("placeCode", t.getPlaceCode()));
			}
		}
		List<MatStockdetail> list = (List<MatStockdetail>) getHibernateTemplate().findByCriteria(dc);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	/**
	 * 查询物资库管表记录
	 */
	public MatStockInfo getMatStockInfo(MatStockInfo t){
		DetachedCriteria dc=DetachedCriteria.forClass(MatStockInfo.class);
		if(t!=null){
			if(StringUtils.isNotBlank(t.getItemCode())){//物资编码
				dc.add(Restrictions.eq("itemCode", t.getItemCode()));
			}
			if(StringUtils.isNotBlank(t.getItemDeptCode())){//仓库编码
				dc.add(Restrictions.eq("itemDeptCode", t.getItemDeptCode()));
			}
			if(StringUtils.isNotBlank(t.getPlaceCode())){//库位号
				dc.add(Restrictions.eq("placeCode", t.getPlaceCode()));
			}
		}
		List<MatStockInfo> list = (List<MatStockInfo>) getHibernateTemplate().findByCriteria(dc);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 更新库存明细表记录
	 */
	public void updateStockdetail(MatStockdetail t){
		getHibernateTemplate().update(t);
	}
	/**
	 * 更新库管表记录
	 */
	public void updateStockInfo(MatStockInfo t){
		getHibernateTemplate().update(t);
	}
}
