package cn.honry.oa.repository.categ.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RepositoryCateg;
import cn.honry.utils.TreeJson;

public interface RepositoryCategService {
	/**  
	 * <p>保存方法 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月14日 下午5:29:02 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月14日 下午5:29:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param cate
	 * void
	 */
	void saveCateg(RepositoryCateg cate);
	/**  
	 * <p>更新 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月14日 下午5:30:08 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月14日 下午5:30:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param cate
	 * void
	 */
	void updateCateg(RepositoryCateg cate);
	/**  
	 * <p>查询知识库分类 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月14日 下午5:31:48 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月14日 下午5:31:48 
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
	int getCategTotal(String deptCode,String name,String nodeType);
	void delCate(String cateid);
	RepositoryCateg get(String id);
	/**  
	 * <p>检查编码是否已使用 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 上午11:10:14 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 上午11:10:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code
	 * @return
	 * Map<String,String>
	 */
	Map<String,String> checkCode(String code);
	/**  
	 * <p> 获取知识库分类库</p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午1:58:59 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午1:58:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * List<TreeJson>
	 */
	List<TreeJson> getCategTree();
}
