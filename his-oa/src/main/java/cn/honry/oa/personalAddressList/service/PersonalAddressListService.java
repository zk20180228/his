package cn.honry.oa.personalAddressList.service;

import java.util.List;

import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.utils.TreeJson;

public interface PersonalAddressListService {

	/**  
	 *  获取通讯录树
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： groupCode 
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> QueryTree(String groupCode);

	/**  
	 *  保存分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： personalAddress 个人通讯录实体 
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdateTree(PersonalAddressList personalAddress) throws Exception;

	/**  
	 *  删除分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id 分组id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void delGroup(String id) throws Exception;

	/**  
	 * 获取总条数
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 下午8:01:07 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 下午8:01:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:parentCode 上级id
	 * @param:queryName 封装查询参数
	 * @throws:
	 * @return: 
	 *
	 */	
	int getPersonalTotal(String parentCode, String queryName);

	/**  
	 * 获取当前页数据
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 下午8:01:07 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 下午8:01:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:parentCode 上级id
	 * @param:queryName 封装查询参数
	 * @throws:
	 * @return: 
	 *
	 */
	List<PersonalAddressList> getPersonalLists(String page, String rows,
			String parentCode, String queryName);

	/**  
	 *  保存个人信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： personalAddress 个人通讯录实体 
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void savePersonalAddress(PersonalAddressList personalAddress) throws Exception;

	/**  
	 * 根据id获取个人信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	PersonalAddressList personalAddressById(String id);

	/**  
	 * 根据id删除个人信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void delPersonalMes(String id) throws Exception;

	/**  
	 * 查询所有分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param：parentCode 上级id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<PersonalAddressList> findAllGroup(String parentCode);

	/**  
	 * 
	 * 个人信息移至其他分组
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id 需要移动的id
	 * @param:parentCode 移至上级id
	 * @throws:
	 * @return: String
	 *
	 */
	void movePersonalToGroup(String id, String parentCode) throws Exception;

}
