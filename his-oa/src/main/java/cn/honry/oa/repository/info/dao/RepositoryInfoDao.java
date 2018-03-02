package cn.honry.oa.repository.info.dao;

import java.util.List;

import cn.honry.base.bean.model.RepositoryInfo;
import cn.honry.base.dao.EntityDao;

public interface RepositoryInfoDao extends EntityDao<RepositoryInfo>  {
	/**  
	 * <p>获取知识库信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午3:50:01 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午3:50:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 编码
	 * @param name 名称
	 * @param isOvert 状态是否公开(0否1是)
	 * @param isCollect 是否为收藏(0否1是)
	 * @param page
	 * @param rows
	 * @return
	 * List<RepositoryInfo>
	 */
	List<RepositoryInfo> getRepositoryInfo(String code,String name,int isOvert,int isCollect,String page,String rows,int pubFlg,String nodeType);
	int getRepositoryInfoTotal(String code,String name,int isOvert,int isCollect,int pubFlg,String nodeType);
	/**  
	 * <p>将信息内容放到mongoDB中 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午8:53:20 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午8:53:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param content
	 * @param contentId
	 * void
	 */
	void insertIntoMongo(String content,String contentId);
	/**  
	 * <p>从mongoDB中取出文章信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午8:55:26 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午8:55:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param contentId
	 * @return
	 * String
	 */
	String getContentFromMongo(String contentId);
	/**  
	 * <p>从mongoDB中删除原来的文章内容 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月16日 上午9:40:39 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月16日 上午9:40:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param contentId
	 * void
	 */
	void deleteFromMongo(String contentId);
	/**  
	 * <p>更新查看次数，每次+1 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月18日 下午12:53:35 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月18日 下午12:53:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * void
	 */
	void updateViews(String infoid);
}
