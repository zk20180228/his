package cn.honry.inpatient.clinicalPathwayModel.dao.impl;

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

import cn.honry.base.bean.model.CpwayPlan;
import cn.honry.base.bean.model.ModelDict;
import cn.honry.base.bean.model.ModelVsItem;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.clinicalPathwayModel.dao.ClinicalPathwayModelDao;
import cn.honry.utils.ShiroSessionUtils;

@Repository(value="clinicalPathwayModelDao")
@SuppressWarnings({"all"})
public class ClinicalPathwayModelDaoImpl extends HibernateEntityDao<ModelDict> implements ClinicalPathwayModelDao {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 查询模板字典表中数据，临床路径系统分类的二级树
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月16日 上午10:16:50 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月16日 上午10:16:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<ModelDict> queryTree() {
		List<ModelDict> list = new ArrayList<ModelDict>();
		String hql ="From ModelDict where del_flg=0 and stop_flg=0 order by modelClass ";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public ModelDict findPathwayModelById(String id) {
		List<ModelDict> list = new ArrayList<ModelDict>();
		String hql="from ModelDict where stop_flg=0 and del_flg=0 and id = '"+id+"'";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new ModelDict();
	}

	@Override
	public List<ModelVsItem> queryClinicalPathModelDetail(String modelId,String page, String rows) {
		List<ModelVsItem> list = new ArrayList<ModelVsItem>();
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		StringBuffer buffer=new StringBuffer();
		buffer.append("select * from (select row_.*, rownum rownum_ from (");
		buffer.append("select t.id,t.item_code as itemCode,t.item_name as itemName, ");
		buffer.append("t.flag ,t.choose_flag as chooseFlag,t.unit,t.num,t.frequency_code as frequencyCode, ");
		buffer.append("t.direction_code as directionCode from t_model_vs_item t ");
		buffer.append("where t.del_flg = 0 and t.stop_flg = 0 ");
		if(StringUtils.isNotBlank(modelId)){
			buffer.append(" and t.model_id = '"+modelId+"'");
		}
		buffer.append(") row_ where rownum <= " + (p * r) + ") where rownum_ > " + ((p - 1) * r) + "");
		SQLQuery query = super.getSession().createSQLQuery(buffer.toString());
		query.addScalar("id").addScalar("itemCode").addScalar("itemName").addScalar("flag").addScalar("chooseFlag").addScalar("unit").addScalar("num",Hibernate.DOUBLE).addScalar("frequencyCode").addScalar("directionCode");
		list = query.setResultTransformer(Transformers.aliasToBean(ModelVsItem.class)).list();
		return list;
	}

	@Override
	public Integer queryClinicalPathModelDetailNum(String modelId) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select count(1) from t_model_vs_item t ");
		buffer.append("where t.del_flg = 0 and t.stop_flg = 0 ");
		if(StringUtils.isNotBlank(modelId)){
			buffer.append(" and t.model_id = '"+modelId+"'");
		}
		Integer total = ((BigDecimal) super.getSession().createSQLQuery(buffer.toString()).uniqueResult()).intValue();
		return total;
	}

	@Override
	public ModelVsItem findPathwayModelDetailById(String id) {
		List<ModelVsItem> list = new ArrayList<ModelVsItem>();
		String hql="from ModelVsItem where stop_flg=0 and del_flg=0 and id = '"+id+"'";
		list = super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new ModelVsItem();
	}

	@Override
	public void delPathwayDetail(String id) {
		String longinUserAccountCode = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String sql=" update t_model_vs_item set DEL_FLG=1,UPDATETIME=sysdate ,UPDATEUSER='"+longinUserAccountCode+"' where ID in("+id+")";
		super.getSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public List<ModelDict> searchClinicalModelByNature(String modelNature) {
		List<ModelDict> list = new ArrayList<ModelDict>();
		String hql ="From ModelDict where modelNature = '"+modelNature+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		return list;
	}

	@Override
	public List<CpwayPlan> searchClinicalModelByStage(String planId,
			String modelNature,String stageId) {
		List<CpwayPlan> list = new ArrayList<CpwayPlan>();
		String hql ="From CpwayPlan where cpId = '"+planId+"' and planCode = '"+modelNature+"' and stageId = '"+stageId+"' and del_flg=0 and stop_flg=0";
		list = super.find(hql, null);
		return list;
	}
}
