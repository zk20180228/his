package cn.honry.inner.operation.item.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OperationItem;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.operation.item.dao.ItemInInterDAO;

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
@Repository("itemInInterDAO")
@SuppressWarnings({"all"})
public class ItemInInterDAOImpl extends HibernateEntityDao<OperationItem> implements ItemInInterDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * @Description:修改
	 * @Author：  zhangjin
	 * @CreateDate： 2016-12-28
	 * @param @param id
	 * @param @return   
	 * @version 1.0
	**/
	@Override
	public void upda(OperationItem item) {
		StringBuffer sb=new StringBuffer();
		sb.append(" update OperationItem SET happenNo=:happenNo,itemName=:itemName,itemId=:itemId"
				+ " where id=:id");
		this.getSession().createQuery(sb.toString()).setParameter("happenNo", item.getHappenNo())
		.setParameter("itemName", item.getItemName()).setParameter("itemId", item.getItemId())
		.setParameter("id", item.getId()).executeUpdate();
		
	}
	/**
	 * @Description:获取手术信息
	 * @Author：  zhangjin
	 * @CreateDate： 2016-12-29
	 * @param @param id
	 * @param @return   
	 * @version 1.0
	**/
	@Override
	public String getname(String id) {
		StringBuffer sb=new StringBuffer();
		String str="";
		sb.append(" select i.ITEM_NAME as itemName  from T_OPERATION_ITEM i");
		sb.append(" where i.OPERATION_ID=:no");
		sb.append(" and i.ITEM_FLAG = '1'");
		sb.append(" and i.STOP_FLG = 0");
		sb.append("and i.DEL_FLG = 0");
		List<OperationItem> list=this.getSession().createSQLQuery(sb.toString()).addScalar("itemName").setParameter("no", id).setResultTransformer(Transformers.aliasToBean(OperationItem.class)).list();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				str+=list.get(i).getItemName()+" ";
			}
			return str;
		}
		return "";
	}
	
}
