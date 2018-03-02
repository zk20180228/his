package cn.honry.oa.personalAddressList.dao;

import java.util.List;

import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.PersonalAddressList;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：PersonalAddressListDao
 * @Description： 个人通讯录
 * @Author：zxl
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：zxl
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface PersonalAddressListDao extends EntityDao<PersonalAddressList>{

	/**  
	 * 
	 * 获取所有分组
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 上午10:26:01 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 上午10:26:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param userAccount 所属用户
	 * @param groupCode  分组code
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PersonalAddressList> queryAllGroup(String groupCode,String userAccount);

	/**  
	 * 
	 * 根据当前登录人获取该分组下信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月19日 上午10:26:01 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月19日 上午10:26:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param userAccount 所属用户
	 * @param:groupCode 所属分组code
	 * @throws:
	 * @return: 
	 *
	 */
	List<PersonalAddressList> queryAllPersonal(String groupCode,
			String userAccount);

	/**  
	 *  获取该分组下所有信息
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id 分组id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<PersonalAddressList> findAllByParentCode(String id);

	/**  
	 *  根据id删除该分组
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id 分组id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void delGroupById(String id) throws Exception;
	

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
	 * 根据当前登录用户获取所有科室
	 * @Author：zxl
	 * @CreateDate：2015-9-9 上午11:21:55 
	 * @param： id
	 * @Modifier：zxl
	 * @ModifyDate：2015-9-9 上午11:21:55  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<PersonalAddressList> findAllGroup(String userAccount);
	
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
	 * @param:personal 上级分组信息
	 * @throws:
	 * @return: String
	 *
	 */
	void movePersonalToGroup(String id, PersonalAddressList personal) throws Exception;

	
	/**  
	 * 
	 * 获取最大排序
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: String
	 *
	 */
	int getMaxOrder();

}
