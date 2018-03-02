package cn.honry.oa.repository.basedata.dao;

import java.util.List;

import cn.honry.base.bean.model.RepositoryBaseData;
import cn.honry.base.dao.EntityDao;

public interface RepositoryBaseDataDao extends EntityDao<RepositoryBaseData> {
	/**  
	 * <p>分页获取列表 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月24日 上午10:46:00 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月24日 上午10:46:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param page
	 * @param rows
	 * @return
	 * List<RepositoryBaseData>
	 */
	List<RepositoryBaseData> getBaseData(String name,String page,String rows); 
	int getBaseDataTotal(String name);
}
