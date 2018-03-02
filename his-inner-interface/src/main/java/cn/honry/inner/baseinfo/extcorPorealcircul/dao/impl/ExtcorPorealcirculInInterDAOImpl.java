package cn.honry.inner.baseinfo.extcorPorealcircul.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessExtcorPorealcircul;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.extcorPorealcircul.dao.ExtcorPorealcirculInInterDAO;

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
@Repository("extcorPorealcirculInInterDAO")
public class ExtcorPorealcirculInInterDAOImpl extends HibernateEntityDao<BusinessExtcorPorealcircul> implements ExtcorPorealcirculInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 *  
	 * @Description：  根据患者Id查询出患者做过的体外循环
	 * @param:patientId(患者id)
	 * @Author：liudelin
	 * @ModifyDate：2015-7-2上午09：11
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessExtcorPorealcircul> findExtcorPorealcircul(String patMedicalrecordId) {
		String hql=" from BusinessExtcorPorealcircul where patientNo = '"+patMedicalrecordId+"'";
		List<BusinessExtcorPorealcircul> extcorPorealcirculList = super.find(hql, null);
		if(extcorPorealcirculList==null||extcorPorealcirculList.size()<=0){
			return new ArrayList<BusinessExtcorPorealcircul>();
		}
		return extcorPorealcirculList;
	}
	
}
