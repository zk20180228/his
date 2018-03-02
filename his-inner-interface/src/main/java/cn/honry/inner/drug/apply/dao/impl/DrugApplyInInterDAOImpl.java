package cn.honry.inner.drug.apply.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.MatOutput;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.apply.dao.DrugApplyInInterDAO;

@Repository("bdrugApplyInInterDao")
@SuppressWarnings({ "all" })
public class DrugApplyInInterDAOImpl extends HibernateEntityDao<InpatientCancelitemNow> implements DrugApplyInInterDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public InpatientMedicineListNow getChildByRecipe(String recipeNo, Integer sequenceNo) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from InpatientMedicineListNow t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.recipeNo=?");
		sBuffer.append(" and t.sequenceNo=?");
		InpatientMedicineListNow mList=null;
		mList=(InpatientMedicineListNow) super.excHqlGetUniqueness(sBuffer.toString(), recipeNo,sequenceNo);
		return mList;
	}
	
	@Override
	public InpatientItemList getItemListByRecipe(String recipeNo, Integer sequenceNo) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from InpatientItemList t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.recipeNo=?");
		sBuffer.append(" and t.sequenceNo=?");
		InpatientItemList model = (InpatientItemList) this.getSession().createQuery(sBuffer.toString())
				.setParameter(0, recipeNo)
				.setParameter(1, Integer.valueOf(sequenceNo)).uniqueResult();
		return model;
	}
	@Override
	public MatOutput getOutputByRecAndSeq(String recipeNo, Integer sequenceNo) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from MatOutput t");
		sBuffer.append(" where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.transType = 1");
		sBuffer.append(" and t.recipeNo =?");
		sBuffer.append(" and t.sequenceNo =?");
		
		MatOutput model = (MatOutput) this.getSession().createQuery(sBuffer.toString())
				.setParameter(0, recipeNo)
				.setParameter(1, sequenceNo)
				.uniqueResult();
		return model;
	}
	
	@Override
	public DrugApplyoutNow obtainApplyout(String recipeNo, Integer sequenceNo) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from DrugApplyoutNow t where t.stop_flg=0 and t.del_flg=0");
		sBuffer.append(" and t.opType=4");
		sBuffer.append(" and t.validState=1");
		sBuffer.append(" and t.recipeNo=?");
		sBuffer.append(" and t.sequenceNo=?");
		
		DrugApplyoutNow model = (DrugApplyoutNow) this.getSession().createQuery(sBuffer.toString())
				.setParameter(0, recipeNo)
				.setParameter(1, sequenceNo)
				.uniqueResult();
		return model;
	}

	@Override
	public void saveOrUpdateList1(List<Object> list) {
		for(Object o : list){
			this.getHibernateTemplate().saveOrUpdate(o);
		}
	}
}
