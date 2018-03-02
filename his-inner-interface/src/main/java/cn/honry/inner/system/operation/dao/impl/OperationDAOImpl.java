package cn.honry.inner.system.operation.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysUseroperation;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.operation.dao.OperationDAO;
import cn.honry.inner.system.utli.DataRightUtils;

@Repository("operationDAO")
@SuppressWarnings({ "all" })
public class OperationDAOImpl extends HibernateEntityDao<SysUseroperation> implements OperationDAO {
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 *  
	 * @Description：  分页查询-获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-17 上午11:33:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-17 上午11:33:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(SysUseroperation operation) {
		return super.getTotal(joint(operation));
	}

	/**  
	 *  
	 * @Description：  分页查询-获得列表信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-17 上午11:33:44  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-17 上午11:33:44  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysUseroperation> getPage(String page, String rows,SysUseroperation operation) {
		return super.getPage(joint(operation), page, rows);
	}
	
	public String joint(SysUseroperation operation){
		String hql="FROM SysUseroperation o WHERE "+DataRightUtils.connectHQLSentence("o");
		if(operation!=null){
			if(StringUtils.isNotBlank(operation.getAction())){
				String queryName = operation.getAction();
				hql+=" and (o.action like '%"+queryName+"%'"
					+" or o.user in (SELECT u.id FROM User u WHERE u.name LIKE '%"+queryName+"%')"
					+" or o.deptId like '%"+queryName+"%'"
					+" or upper(o.menuId) like '%"+queryName+"%'"
					+" or upper(o.table) like '%"+queryName+"%')";
			}
		}
		hql+=" ORDER BY o.time DESC";
		return hql;
	}

}
