package cn.honry.inner.baseinfo.bedward.dao.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.bedward.dao.BedwardInInterDAO;

@Repository("bedwardInInterDAO")
@SuppressWarnings({ "all" })
public class BedwardInInterDAOImp extends HibernateEntityDao<BusinessBedward>  implements BedwardInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	private Logger logger=Logger.getLogger(BedwardInInterDAOImp.class);

	/**
	 * 根据ID查询病房信息
	 * @date 2015-05-21
	 * @author sgt
	 * @version 1.0
	 * @param id
	 * @return
	 */
	@Override
	public BusinessBedward getBedwardById(String id) {
		//StringBuilder sql = new StringBuilder();
		//sql.append("select BEDWARD_ID from t_business_bedward where bedward_id = "+id+" ");
		if(StringUtils.isNotBlank(id)){
			return (BusinessBedward) this.getSession().get(BusinessBedward.class, id);
		}
		return null;
	}
	
	
	
}
