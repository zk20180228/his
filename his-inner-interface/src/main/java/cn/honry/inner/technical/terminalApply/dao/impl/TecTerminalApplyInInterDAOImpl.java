package cn.honry.inner.technical.terminalApply.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.technical.terminalApply.dao.TerminalApplyInInterDAO;

/**
 *
 * @Description：医技终端确认DAOImpl
 * @Author：aizhonghua
 * @CreateDate：2016年4月18日 上午8:58:52 
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version： 1.0：
 *
 */
@Repository("terminalApplyInInterDAO")
@SuppressWarnings({ "all" })
public class TecTerminalApplyInInterDAOImpl extends HibernateEntityDao<TecTerminalApply> implements TerminalApplyInInterDAO {
	
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 * @author  kjh
	 * @date 2015-11-2 16:00
	 * @version 1.0
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	/**  
	 * @Description： 获得原有医技信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-7 下午08:24:25  
	 * @Modifier：wanxing
	 * @ModifyDate：2016-04-05 下午16:36:25  
	 * @ModifyRmk：  将hql中拼接参数的形式修改为预编译的形式
	 * @version 1.0
	 */
	@Override
	public TecTerminalApply getApplyApplyNo(String applyNo) {
		if(StringUtils.isBlank(applyNo)){
			return null;
		}
		StringBuffer hql = new StringBuffer("FROM TecTerminalApply c WHERE c.del_flg = 0 AND c.stop_flg = 0 ");
		hql.append(" AND c.applyNumber = ").append(Long.parseLong(applyNo));
		List<TecTerminalApply> list=super.find(hql.toString(), null);
		
		if(list!=null && list.size()==1){
			return list.get(0);
		}
		return null;
	}
	
} 
