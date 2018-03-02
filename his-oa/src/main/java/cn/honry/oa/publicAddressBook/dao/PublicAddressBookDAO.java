package cn.honry.oa.publicAddressBook.dao;

import java.util.List;

import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface PublicAddressBookDAO extends EntityDao<PublicAddressBook> {
	/**  
	 * 
	 * 公共通讯录树
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 上午10:37:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 上午10:37:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> findTree();
	/**  
	 * 
	 * 获得栏目的全部子栏目
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午5:53:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午5:53:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getChildById(String superPath);
	/**  
	 * 
	 * 获得栏目的全部子栏目
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午5:53:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午5:53:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getChildByIds(String ids);
	/**  
	 * 
	 * 获得栏目的全部工作站
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 上午11:55:51 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 上午11:55:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getPublicBookList(String page, String rows,String id,String nodeType,String areaCode,String noString,String floor,String typeName,String deptCode);
	/**  
	 * 
	 * 获得栏目的全部工作站数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月25日 下午6:24:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月25日 下午6:24:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	int getPublicBookTotal(String page, String rows,String id,String nodeType,String areaCode,String noString,String floor,String typeName,String deptCode);
	/**  
	 * 
	 * 获得栏目的楼号
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getVoNoStringList();
	/**  
	 * 
	 * 获得栏目的楼层
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getVoFloorList();
	/**  
	 * 
	 * 科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getVoDeptList();
	/**  
	 * 
	 * 类别名称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getVoTypeList();
	/**  
	 * 
	 * 院区
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<PublicAddressBook> getVoAreaList();
	/**  
	 * 
	 * 根据科室分类查所有科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 下午5:15:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 下午5:15:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<SysDepartment> getDept(String deptType);
}
