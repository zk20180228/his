package cn.honry.inner.drug.sendWicket.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessExtend;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.sendWicket.dao.SendWicketInInterDAO;

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
@Repository("sendWicketInInterDAO")
public class SendWicketInInterDAOImpl extends HibernateEntityDao<StoTerminal> implements SendWicketInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<StoTerminal> getAllSendWicket() {
		return super.findBy("type", 0);
	}

	@Override
	public Map<String, String> getBusinessExtend(String code) {
		Map<String,String> map = new HashMap<String, String>();
		String hql = " from  BusinessExtend where propertyCode in('TJFS','TJYJ') and itemCode = ? and stop_flg=0 and del_flg=0 ";
		List<BusinessExtend> find = super.find(hql, code);
		if(find!=null&&find.size()>0){
			for (BusinessExtend bin : find) {
				if("TJFS".equals(bin.getPropertyCode())){//调剂方式
					map.put("TJFS", bin.getNumberProperty().toString());
				}else if("TJYJ".equals(bin.getPropertyCode())){//调剂依据
					map.put("TJYJ", bin.getNumberProperty().toString());
				}
			}
			map.put("resCode", "success");
		}else{
			map.put("resCode", "error");
		}
		return map;
	}
	
}
