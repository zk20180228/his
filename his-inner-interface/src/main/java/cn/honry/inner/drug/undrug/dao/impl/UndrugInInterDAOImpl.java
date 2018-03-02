package cn.honry.inner.drug.undrug.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("undrugInInterDAO")
@SuppressWarnings({ "all" })
public class UndrugInInterDAOImpl extends HibernateEntityDao<DrugUndrug> implements UndrugInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2015-06-06
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	@Override
	public List<DrugUndrug> getInfo() {
		String hql = " from DrugUndrug where del_Flg=0 order by createTime desc";
		List<DrugUndrug>  drugUndrug = super.findByObjectProperty(hql, null);
		if(drugUndrug!=null&& drugUndrug.size()>0){
			return drugUndrug;
		}
		return new ArrayList<DrugUndrug>();
	}
	
	/**  
	 *  
	 * @Description：  获得煎药方式
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午02:57:21  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-7 下午02:57:21  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public DrugUndrug getUnDrugByName(String name) {
		String hql="FROM DrugUndrug d WHERE d.del_flg = 0 AND d.stop_flg = 0 AND d.name = '"+name+"'";
		List<DrugUndrug> drugUndrugList=super.findByObjectProperty(hql, null);
		if(drugUndrugList!=null && drugUndrugList.size()==1){
			return drugUndrugList.get(0);
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(DrugUndrug undrug) {
		String hql=jointview(undrug);
		return  super.getTotal(hql);
	}
	
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-4-14上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugUndrug> getPage(String page, String rows, DrugUndrug undrug) {
		String hql=jointview(undrug);
		return super.getPage( hql,page, rows);
	}
	
	/**  
	 *  
	 * @Description：  获取sql语句
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-4-14上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private String jointview(DrugUndrug undrug) {
		String hql="FROM DrugUndrug d WHERE d.del_flg = 0 AND d.stop_flg = 0 ";
		if(undrug!=null){
			if(StringUtils.isNotEmpty(undrug.getName())){//项目名称
			String queryName=undrug.getName();
				hql = hql+" AND  (upper(d.name) LIKE '%"+queryName+"%'"
				 + " OR upper(d.undrugPinyin) LIKE '%"+queryName.toUpperCase()+"%'" 
				 + " OR upper(d.undrugWb) LIKE '%"+queryName.toUpperCase()+"%'" 
				 + " OR d.code LIKE '%"+queryName.toUpperCase()+"%'" 
				 + " OR upper(d.undrugInputcode) LIKE '%"+queryName+"%'"
				 +")";
			}
			if(StringUtils.isNotBlank(undrug.getUndrugDept())){//执行科室
				hql=hql+" and undrugDept ='"+undrug.getUndrugDept()+"'";
			}
			if(StringUtils.isNotBlank(undrug.getUndrugSystype())){//系统类别
				hql=hql+" and undrugSystype = '"+undrug.getUndrugSystype()+"'";
			}
			if(undrug.getUndrugIssubmit() != null){//是否确认
				hql=hql+" and undrugIssubmit = "+undrug.getUndrugIssubmit();
			}
			if(undrug.getUndrugState() != null){//状态
				hql=hql+" and undrugState = "+undrug.getUndrugState();
			}
			if(StringUtils.isNotBlank(undrug.getUndrugMinimumcost())){//最小费用
				hql=hql+" and undrugMinimumcost = '"+undrug.getUndrugMinimumcost()+"'";
			}
		}
		return hql;
	}
	
	/**  
	 * @Description： 非药品
	 * @Author：wujiao
	 * @CreateDate：2015-12-2 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-12-2上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugUndrug> likeUndrugMap() {
		String hql="FROM DrugUndrug d WHERE d.del_flg = 0 AND d.stop_flg = 0 ";
		List<DrugUndrug> undrugList=super.findByObjectProperty(hql, null);
		if(undrugList!=null && undrugList.size()>0){
			return undrugList;
		}
		return new ArrayList<DrugUndrug>();
	}
	
	/**  
	 *  
	 * @Description：  根据最小费用查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午06:10:54  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午06:10:54  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DrugUndrug> queryUndrugByDrugminimumcost(String id) {
		String hql="FROM DrugUndrug d WHERE d.del_flg = 0 AND d.stop_flg = 0 AND d.undrugMinimumcost = '"+id+"'";
		List<DrugUndrug> undrugList=super.findByObjectProperty(hql, null);
		if(undrugList!=null && undrugList.size()>0){
			return undrugList;
		}
		return new ArrayList<DrugUndrug>();
	}

	@Override
	public DrugUndrug getCode(String itemCode) {
		return findUniqueBy("code", itemCode);
	}

	@Override
	public List<DrugUndrug> queryNotTackUndrug(String page,String rows,String notTackUndrug) {
		StringBuffer sql=new StringBuffer();
		sql.append("FROM DrugUndrug t WHERE t.del_flg=0 and t.stop_flg=0 and t.undrugIsstack=0");
		//tackUndrug 拼音码 五笔码 自定义码 编码 名称
		if(notTackUndrug!=null){
			sql.append(" and (t.name      		like '%"+notTackUndrug+"%' ");
			sql.append(" or   t.undrugPinyin    like '%"+notTackUndrug+"%' ");
			sql.append(" or   t.undrugWb        like '%"+notTackUndrug+"%' ");
			sql.append(" or   t.undrugInputcode like '%"+notTackUndrug+"%' ");
			sql.append(" or   t.code    like '%"+notTackUndrug+"%')");
		}
		return super.getPage(sql.toString(), page, rows);
	}
	
}
