package cn.honry.oa.repository.info.service;

import java.util.List;

import cn.honry.base.bean.model.RepositoryInfo;

public interface RepositoryInfoService {
	/**  
	 * <p>获取公共信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午5:07:18 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午5:07:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 分类编码
	 * @param name 分类名称
	 * @param page
	 * @param rows
	 * @param nodeType 
	 * @return
	 * List<RepositoryInfo>
	 */
	List<RepositoryInfo> getPublicInfo(String code,String name,String page,String rows,String nodeType );
	int getPublicInfoTotal(String code,String name,String nodeType);
	/**  
	 * <p>个人信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午5:07:18 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午5:07:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 分类编码
	 * @param name 分类名称
	 * @param page
	 * @param rows
	 * @return
	 * List<RepositoryInfo>
	 */
	List<RepositoryInfo> getPersonalInfo(String code,String name,String page,String rows );
	int getPersonalInfoTotal(String code,String name);
	/**  
	 * <p>个人收藏信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午5:07:18 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午5:07:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 分类编码
	 * @param name 分类名称
	 * @param page
	 * @param rows
	 * @return
	 * List<RepositoryInfo>
	 */
	List<RepositoryInfo> getPersonalCollectionInfo(String code,String name,String page,String rows );
	int getPersonalCollectionInfoTotal(String code,String name);
	/**  
	 * <p>收藏功能</p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午5:11:49 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午5:11:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * void
	 */
	void savePersonalCollection(String infoid);
	/**  
	 * <p>保存或更新信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午6:02:20 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午6:02:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * void
	 */
	void saveOrUpdateInfo(RepositoryInfo info);
	/**  
	 * <p>根据id获取信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月15日 下午8:58:47 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月15日 下午8:58:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return
	 * RepositoryInfo
	 */
	RepositoryInfo getInfoByid(String id);
	/**  
	 * <p>根据ids删除 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月16日 下午8:56:32 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月16日 下午8:56:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * void
	 */
	void delInfo(String infoid);
	/**  
	 * <p>更新查看次数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月18日 下午12:52:42 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月18日 下午12:52:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * void
	 */
	void updateViews(String infoid);
}
