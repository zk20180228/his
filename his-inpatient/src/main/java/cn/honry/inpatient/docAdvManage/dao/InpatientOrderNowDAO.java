package cn.honry.inpatient.docAdvManage.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.dao.EntityDao;

public interface InpatientOrderNowDAO extends EntityDao<InpatientOrderNow>{
	/**  
	 *  
	 * @Description： 查询历史医嘱资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	List<InpatientOrderNow> getPage(String page, String rows, String hql);

	/**  
	 *  
	 * @Description： 查询历史医嘱资料总条数
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-11   
	 * @version 1.0
	 *
	 */
	int getTotal(String hql);
}
