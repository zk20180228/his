package cn.honry.inner.material.baseinfo.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MatBaseinfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.material.baseinfo.dao.BaseinfoInInterDAO;

/**  
 *  
 * @className：BaseinfoInInterDAOImpl
 * @Description：    物资分类字典
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("baseinfoInInterDAO")
public class BaseinfoInInterDAOImpl extends HibernateEntityDao<MatBaseinfo> implements BaseinfoInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<MatBaseinfo> queryByItemCode(String itemCode) {
		String hql ="from MatBaseinfo t where t.stop_flg = 0 and t.del_flg = 0 and itemCode='"+itemCode+"'";
		List<MatBaseinfo> list=super.find(hql, null);
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<MatBaseinfo>();
	}
	
}
