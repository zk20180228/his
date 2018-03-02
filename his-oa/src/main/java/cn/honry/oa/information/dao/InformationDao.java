package cn.honry.oa.information.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.InformationSubscripe;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.information.vo.MenuCkeckedVO;
import cn.honry.oa.information.vo.MenuVO;
import cn.honry.oa.information.vo.SubscripeVO;

public interface InformationDao extends EntityDao<Information> {
	/**  
	 * 
	 * <p>根据id获取栏目信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月17日 下午3:49:51 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月17日 下午3:49:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return
	 * @throws:
	 * @return: MenuVO
	 *
	 */
	MenuVO findMenuByid(String id);
	/**  
	 * 
	 * <p> 根据栏目code和权限类型获取相应数据</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月17日 下午7:58:56 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月17日 下午7:58:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuid 栏目code
	 * @param type 权限类型
	 * @return
	 * @throws:
	 * @return: MenuCkeckedVO
	 *
	 */
	List<MenuCkeckedVO> queryMenuCheckByid(String menuid,String type);
	/**  
	 * 
	 * <p>查询栏目权限下的人员 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 上午10:56:06 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 上午10:56:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId 栏目id
	 * @param post 职务
	 * @param title 职称
	 * @return
	 * @throws:
	 * @return: List<SysEmployee>
	 *
	 */
	List<SysEmployee> queryAuthEmp(String menuId,String post,String title,String page,String rows);
	/**  
	 * 
	 * <p>查询栏目权限下的人员总条数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 上午11:27:11 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 上午11:27:11 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @param post
	 * @param title
	 * @return
	 * @throws:
	 * @return: int
	 *
	 */
	int queryAuthEmpTotal(String menuId,String post,String title);
	/**  
	 * 
	 * <p>获取浏览权限（角色） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 下午7:46:31 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 下午7:46:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * @return: List<MenuCkeckedVO>
	 *
	 */
	List<MenuCkeckedVO> queryRoleAuth(String menuId);
	/**  
	 * 
	 * <p>获取浏览权限（科室） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 下午8:09:46 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 下午8:09:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * @throws:
	 * @return: List<MenuCkeckedVO>
	 *
	 */
	List<MenuCkeckedVO> queryDeptAuth(String menuId);
	/**  
	 * 
	 *  <p>获取浏览权限（职务） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月19日 上午10:07:04 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月19日 上午10:07:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * @throws:
	 * @return: List<MenuCkeckedVO>
	 *
	 */
	List<MenuCkeckedVO> queryDutyAuth(String menuId);
	/**  
	 * 
	 * <p>获取发布权限下的栏目 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月20日 下午3:21:09 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月20日 下午3:21:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 科室code
	 * @param dutyCode 职务code
	 * @param roleCode 角色code
	 * @param acount 用户code
	 * @throws:
	 * @return: List<MenuVO>
	 *
	 */
	List<MenuVO> findMenuVo(String deptCode,String dutyCode,String roleCode,String acount);
	/**  
	 * 
	 * <p>检测当前用户是都有栏目的发布权限 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月22日 下午6:05:04 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月22日 下午6:05:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode
	 * @param dutyCode
	 * @param roleCode
	 * @param acount
	 * @return
	 * @throws:
	 * @return: Map<String,String>
	 *
	 */
	List<MenuCkeckedVO> checkAuth(String menuid,String deptCode,String dutyCode,String roleCode,String acount);
	/**  
	 * <p>查询文章信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午2:44:33 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午2:44:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param menuid
	 * @param page
	 * @param rowss
	 * @return
	 * List<Information>
	 */
	List<Information> findinformationList(String name,String menuid,String page,String rows,String type);
	/**  
	 * <p>总页数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午3:16:25 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午3:16:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param menuid
	 * @param type
	 * @return
	 * int
	 */
	int findinformationTotal(String name,String menuid,String type);
	/**  
	 * <p>获取订阅信息（个人） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 上午9:28:09 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 上午9:28:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param type
	 * @param code
	 * @return
	 * List<SubscripeVO>
	 */
	List<SubscripeVO> findSub(String type,String code);
	/**  
	 * <p>更改已读 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午7:58:54 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午7:58:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @param acount
	 * void
	 */
	void updateSubscripe(String menuId,String acount);
	/**  
	 * <p>查询栏目信息（本人发布的和待本人审核的） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月31日 下午8:16:30 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月31日 下午8:16:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param menuid
	 * @param page
	 * @param rows
	 * @param type
	 * @return
	 * List<Information>
	 */
	List<Information> queryInformationList(String name, String menuid,String page, String rows,String checkflag,String pubflag);
	/**  
	 * <p>总页数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年8月1日 上午9:54:05 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年8月1日 上午9:54:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param menuid
	 * @return
	 * int
	 */
	int queryInformationTotal(String name, String menuid,String checkflag,String pubflag);
	/**  
	 * <p>获取栏目下拥有查看权限的所有栏目 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年8月1日 上午9:54:02 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年8月1日 上午9:54:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * List<Information>
	 */
	List<Information> getInfomationView(String menuId,String page,String rows);
	/**  
	 * <p>总页数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年8月1日 上午9:55:29 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年8月1日 上午9:55:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * int
	 */
	int getInfomationViewTotal(String menuId);
	/**<p>根据栏目code获取栏目一级父节点的code和name</p>
	 * @param menuCode
	 * @return
	 */
	MenuVO getparentCodeAndName(String menuCode);
	/**<p>根据栏目code和类型判断权限</>
	 * @param menuid
	 * @param type
	 * @return
	 */
	List<MenuCkeckedVO> judgeAuthBymenuCode(String menuid ,String type);
	/**删除审核信息
	 * @param infoid
	 */
	void deleteCkeck(String infoid);
	/**  
	 * <p>审核的总页数 </p>
	 * @Author:zpty
	 * @CreateDate: 2017年8月6日 上午9:55:29 
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * int
	 */
	int getInformationCheckTotal(String menuId);
	/**  
	 * <p>获取栏目下拥有审核权限的所有栏目 </p>
	 * @Author: zpty
	 * @CreateDate: 2017年8月6日 上午9:54:02 
	 * @version: V1.0
	 * @return
	 * List<Information>
	 */
	List<Information> getInformationCheck(String menuId, String page,String rows);
	/**  
	 * 获取当前信息的订阅表信息
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 上午9:52:44 
	 * @version: V1.0
	 * @return
	 * Map<String,Object>
	 */
	InformationSubscripe querySubscripe(String infoMenuid, String account);
	/**  
	 * <p>更新浏览次数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月18日 下午3:19:43 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月18日 下午3:19:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * void
	 */
	void updViews(String infoid);
}
