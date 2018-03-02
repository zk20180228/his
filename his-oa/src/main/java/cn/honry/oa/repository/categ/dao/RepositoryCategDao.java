package cn.honry.oa.repository.categ.dao;

import java.util.List;

import cn.honry.base.bean.model.RepositoryCateg;
import cn.honry.base.dao.EntityDao;

public interface RepositoryCategDao extends EntityDao<RepositoryCateg> {
	/**  
	 * <p>获取list集合 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月14日 下午5:52:56 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月14日 下午5:52:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode
	 * @param page
	 * @param rows
	 * @param name
	 * @return
	 * List<RepositoryCateg>
	 */
	List<RepositoryCateg> getCateg(String deptCode,String page,String rows,String name,String nodeType);
	
	/**  
	 * <p> 获取总页数</p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月14日 下午5:53:16 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月14日 下午5:53:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode
	 * @param name
	 * @return
	 * int
	 */
	int getCategTotal(String deptCode,String name,String nodeType);
	/**  
	 * <p>根据编码获取知识库分类 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 上午11:11:15 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 上午11:11:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code
	 * @return
	 * RepositoryCateg
	 */
	RepositoryCateg checkCode(String code);
	/**  
	 * <p>获取所有的只是库分类 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午2:02:02 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午2:02:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 */
	List<RepositoryCateg> getAllCate();
}
